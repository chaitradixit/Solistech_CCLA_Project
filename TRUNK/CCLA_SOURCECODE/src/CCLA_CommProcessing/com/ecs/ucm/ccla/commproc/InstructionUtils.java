package com.ecs.ucm.ccla.commproc;

import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.shared.SharedObjects;

import com.ecs.stellent.auditlog.AuditUtils;
import com.ecs.stellent.spp.Variable;
import com.ecs.stellent.spp.data.QueryJobData;
import com.ecs.stellent.spp.data.WorkflowJobResponse;
import com.ecs.stellent.spp.data.SPPJobProfile;
import com.ecs.stellent.spp.data.SPPJobProfile.JobVariable;
import com.ecs.stellent.spp.service.SppIntegrationUtils;
import com.ecs.ucm.ccla.commproc.DocumentBundleTranslator.FailReason;
import com.ecs.ucm.ccla.commproc.filter.UpdateFilter;
import com.ecs.ucm.ccla.Globals;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals.UCMFieldNames;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.AuroraClient;
import com.ecs.ucm.ccla.data.BankAccount;
import com.ecs.ucm.ccla.data.DataType;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Fund;
import com.ecs.ucm.ccla.data.Interaction;
import com.ecs.ucm.ccla.data.SharedCols;
import com.ecs.ucm.ccla.data.comm.Comm;
import com.ecs.ucm.ccla.data.comm.CommGroup;
import com.ecs.ucm.ccla.data.comm.CommType;
import com.ecs.ucm.ccla.data.instruction.ApplicableInstructionData;
import com.ecs.ucm.ccla.data.instruction.Instruction;
import com.ecs.ucm.ccla.data.instruction.InstructionAudit;
import com.ecs.ucm.ccla.data.instruction.InstructionAuditAction;
import com.ecs.ucm.ccla.data.instruction.InstructionData;
import com.ecs.ucm.ccla.data.instruction.InstructionData.Fields;
import com.ecs.ucm.ccla.data.instruction.InstructionDataApplied;
import com.ecs.ucm.ccla.data.instruction.InstructionDataFieldValue;
import com.ecs.ucm.ccla.data.instruction.InstructionDataValue;
import com.ecs.ucm.ccla.data.instruction.InstructionStatus;
import com.ecs.ucm.ccla.data.instruction.InstructionType;
import com.ecs.ucm.ccla.data.instruction.UCMMetadataTranslation;
import com.ecs.ucm.ccla.data.staticdataupdate.StaticDataUpdateApplied;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.DateFormatter;
import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Helper functions for dealing with Instruction objects.
 * 
 * @author Tom
 *
 */
public class InstructionUtils {
	
	/** Updates an existing instruction using the metadata of its linked UCM content 
	 *  item.
	 *  
	 *  Should be executed in a transaction block.
	 *  
	 *  This is currently executed via 
	 *  
	 * @param docId
	 * @param facade
	 * @throws ServiceException 
	 * @throws DataException 
	 */
	public static void updateInstructionFromDocMeta
	 (Instruction instr, Integer dID, FWFacade facade, String userName) 
	 throws ServiceException, DataException {
		
		if (instr.getInstructionDocGuid() == null) {
			String msg = "Unable to update Instruction ID " + instr.getInstructionId() +
			 " from UCM metadata: no mapped document GUID";
			Log.error(msg);
			throw new ServiceException(msg);
		}	
			
		DataResultSet docMeta = CCLAUtils.getUCMDocMeta(dID, CCLAUtils.getFacade(false));
		
		if (docMeta.isEmpty()) {
			String msg = "Unable to update Instruction ID " + instr.getInstructionId() +
			 " from UCM metadata: unable to query data for DocGuiD: " + instr.getInstructionDocGuid();
			
			Log.error(msg);
			throw new ServiceException(msg);
		}
		
		String docName = docMeta.getStringValueByName(UCMFieldNames.DocName);
		// Look up the associated Instruction Type from the name cache.
		String instrTypeName = docMeta.getStringValueByName(UCMFieldNames.DocClass);		
		
		if (StringUtils.stringIsBlank(instrTypeName)) {
			String msg = "Document " + docName + " did not have a " +
			 "Document Class set.";
			
			Log.error(msg);
			throw new BundleTranslateException
			 (msg, FailReason.UNKNOWN_INSTRUCTION_TYPE, docName);
		}
		
		InstructionType docInstrType = 
		 InstructionType.getNameCache().getCachedInstance(instrTypeName);
		
		if (docInstrType == null) {
			String msg = "No bound Instruction Type for Document Class: '" + 
			 instrTypeName + "'. Document has already been converted into a " +
			 instr.getType().getName() + " instruction with ID " + 
			 instr.getInstructionId();
			
			Log.error(msg);
			throw new ServiceException(msg);
		}
		
		if (!instr.getType().equals(docInstrType)) {
			// Updated instruction type.
			instr.setType(docInstrType);
		}
	
		// Copy Process Date from source document
		instr.setProcessDate(docMeta.getDateValueByName(UCMFieldNames.ProcessDate));
		// Copy SPP Job ID (Batch Number) from source document
		instr.setSppJobId(docMeta.getStringValueByName(UCMFieldNames.BatchNumber));
		
		instr.persist(facade, userName);
		
		// Calculate all applied instruction data fields
		Vector<InstructionDataApplied> instrDataApplied = 
		 generateInstructionData(instr, docMeta, facade);
		
		InstructionDataApplied.addOrUpdateAll(instrDataApplied, facade, userName);
	}
	
	/** Generates the applied Instruction Data fields/values for the given Instruction,
	 *  using metadata values taken from the passed docMeta ResultSet.
	 * 
	 * @param instr
	 * @param docName
	 * @return the set of InstructionDataApplied instances for the given Instruction.
	 * @throws DataException 
	 * @throws ServiceException 
	 */
	public static Vector<InstructionDataApplied> generateInstructionData
	 (Instruction instr, DataResultSet docMeta, FWFacade facade) 
	 throws DataException, ServiceException {
		
		InstructionType instrType = instr.getType();
		
		// Fetch the applicable fields for this Instruction Type
		Vector<ApplicableInstructionData> applicableFields = 
		 instrType.getApplicableInstructionData();
		
		// Step through each applicable Instruction Data field for this Instruction
		// Type and gather up field values.
		Vector<InstructionDataApplied> instrDataApplied = 
		 new Vector<InstructionDataApplied>();
		
		String docName = docMeta.getStringValueByName(UCMFieldNames.DocName);
		
		if (applicableFields == null || applicableFields.isEmpty()) {
			// No applicable instruction data fields for this instruction type.
			String msg = "Instruction type " + instrType.getName() +
			 " has no applicable data fields set.";
			
			throw new BundleTranslateException
			 (msg, FailReason.NO_MAPPED_APPLICABLE_DATA_FIELDS, docName);
		}
		
		for (ApplicableInstructionData applicableField : applicableFields) {
			InstructionDataFieldValue fieldValue = null;
			
			InstructionData dataField = applicableField.getInstructionData();
			
			UCMMetadataTranslation translation =
			 dataField.getUCMMetadataTranslation();
			
			if (translation != null) {
				fieldValue = translation.translate(docMeta, facade);

				if (fieldValue.isEmpty() && !applicableField.isOptional()) {
					// Required field value is missing.
					String msg = "Field '" + dataField.getLabel() + 
					 "' is required for instruction type " + instrType.getName() +
					 ". Ensure this field has a value set, or change the " +
					 "instruction type.";
					
					throw new BundleTranslateException
					 (msg, FailReason.MISSING_REQUIRED_FIELD, docName);
				}

				InstructionDataApplied instrData = new InstructionDataApplied
				 (instr.getInstructionId(), applicableField, fieldValue);
				
				instrDataApplied.add(instrData);
				
			} else {
				//ensure that all mandatory fields are translated.
				if (!applicableField.isOptional()) {
					String msg = "Field '" + dataField.getLabel() + 
					 "' is required for instruction type " + instrType.getName() +
					 ". Ensure this field has a value set, or change the " +
					 "instruction type.";
					
					throw new BundleTranslateException
					 (msg, FailReason.MISSING_REQUIRED_FIELD, docName);
				} else 
					Log.debug("No translator for Instruction Data field '" 
							+ dataField.getName() + "', ignoring.");
			}
		}
		
		return instrDataApplied;
	}
	
	/** Triggers a standard SPP job for the given Instruction.
	 *  
	 *  First the parent CommGroup is checked for an existing SPP Batch/Workflow ID.
	 *  If it doesn't have one, it will be generated here and the CommGroup is updated
	 *  with the new number.
	 *  
	 *  The actual method for translating instruction/document data to SPP job variables
	 *  varies depending on the Comm Type.
	 * 
	 *  For document-based instructions, the backing UCM document metadata is directly
	 *  translated.
	 *  
	 *  For instructions without a backing document, the instruction data must be
	 *  translated.
	 * 
	 * @param instr
	 * @param userName
	 * @param cdbFacade
	 * @return true if the job trigger was successful, false otherwise.
	 * @throws DataException
	 * @throws ServiceException
	 */
	public static boolean submitInstructionToSpp(Instruction instr, String userName, 
	 FWFacade cdbFacade) throws DataException, ServiceException {
		
		Log.debug("Submitting Instruction ID " + instr.getInstructionId() + " to SPP");

		// We'll use this to fetch document metadata later on.
		FWFacade ucmFacade = CCLAUtils.getFacade(false);
		
		// Fail immediately if we aren't in a transaction block
		if (!cdbFacade.isInTransaction()) {
			String msg = "Unable to submit Instruction to SPP: not inside " +
			 "a transaction block";
			
			Log.error(msg);
			throw new ServiceException(msg);
		}
		
		Comm comm = Comm.get(instr.getCommId(), cdbFacade);
		CommGroup commGroup = CommGroup.get(comm.getGroupId(), cdbFacade);
		
		Log.debug("Comm Type: " + comm.getType().getName());

		// Allocate SPP Batch Number to the instruction's Comm Group.
		CommUtils.prepareCommGroupForSpp(commGroup, cdbFacade);
			
		// UCM Document-based instructions. Trigger a workflow job against the UCM
		// document, as opposed to the instruction data directly.
		// The SPP Batch Ref, Job ID and Workflow Date will be automatically copied
		// to the Instruction data when the document is updated.
		if (comm.getType().equals(CommType.DOCUMENT) 
			&& !instr.getType().isStaticDataUpdate()) {
			
			if (instr.getInstructionDocGuid() == null) {
				String msg = "Unable to submit Document-based instruction to SPP: " +
				 "no mapped DOC_GUID";
				Log.error(msg);
				throw new DataException(msg);
			}
			
			LWDocument lwDoc = null;
			
			try {
				lwDoc = CCLAUtils.getLwdByRevisionFromDocGuid
				 (instr.getInstructionDocGuid());
				
				/* Running non-transactional metadata updates is now very dangerous
				 * as we are primarily using the CCLA facade.
				 * 
				// Apply the SPP Batch Number to the document metadata. This must be set
				// otherwise the triggerSppWorkflow method won't work.
				// The applied value will propagate down to the related Instruction.
			
				
				lwDoc.setUseTransaction(false);
				
				lwDoc.setAttribute(UCMFieldNames.BatchNumber, 
									commGroup.getSppBatchRef().toString());
				lwDoc.update();
				*/
				
				// Apply the SPP Batch Number to the document metadata.
				lwDoc.setAttribute(UCMFieldNames.BatchNumber, 
						commGroup.getSppBatchRef().toString());
				// Ensure cascaded instruction update is prevented, to avoid potential
				// deadlock.
				lwDoc.setAttribute(UpdateFilter.PREVENT_INSTRUCTION_UPDATE, "1");
				lwDoc.update();
				
			} catch (Exception e) {
				String msg = "Failed to set SPP Batch Number on document GUID " 
				 + instr.getInstructionDocGuid() + ": " + e.getMessage();
				
				Log.error(msg, e);
				throw new ServiceException(msg, e);
			}
				
			Integer docId = null;
			String docName = null;
			
			try {
				docId = Integer.parseInt(lwDoc.getId());
				docName = lwDoc.getDocName();
			} catch (Exception e1) {
				String msg = "Unable to get dID/dDocName from : "
				 + instr.getInstructionDocGuid();
				
				Log.error(msg, e1);
				throw new ServiceException(msg, e1);
			}
			
			
			DataResultSet rsDocMeta = CCLAUtils.getUCMDocMeta(docId, ucmFacade);
			
			SPPJobProfile jobProfile = SPPJobProfile.JOB_PROFILE;
					
			Date date = SppIntegrationUtils.triggerSppWorkflow
			 (docName, true, userName, new DataBinder(), 
			 SppIntegrationUtils.SINGLE_SUBMISSION, cdbFacade, jobProfile);
			
			// Cascade the document metadata changes directly to the instruction.
			// Fetch latest doc meta first.
			rsDocMeta = CCLAUtils.getUCMDocMeta(docId, ucmFacade);
			
			Log.debug("Cascading UCM document metadata updates to Instruction");
			InstructionUtils.updateInstructionFromDocMeta
			 (instr, docId, cdbFacade, userName);
			
			if (date != null) {
				// Success - add another note to the bundle item, indicating one of its
				// items was submitted to SPP.
				Vector<String> params = new Vector<String>();
				params.add(rsDocMeta.getStringValueByName(UCMFieldNames.DocName));
				
				try {
					AuditUtils.addAuditEntry
						("IRIS", "SPP-SUBMIT-DOC", 
						lwDoc.getDocName(), lwDoc.getTitle(), 
						Globals.Users.System,
						"Instruction Register started workflow job for "  
						 + rsDocMeta.getStringValueByName(UCMFieldNames.DocName), 
						params);
				
				} catch (Exception e) {
					String msg = "Failed to add audit message to parent bundle item GUID: "
					 + instr.getInstructionDocGuid() + ": " + e.getMessage();
					
					Log.error(msg, e);
					throw new ServiceException(msg, e);
				} 
			}
			
			return (date != null);
		
		// Interaction-based instruction. Requires triggering a CEQB Job.
		} else if (comm.getType().equals(CommType.INTERACTION)) {
			
			if (comm.getInteractionId() == null) {
				String msg = "Unable to submit Interaction-based instruction to SPP: " +
				 "no mapped Interaction ID";
				
				Log.error(msg);
				throw new DataException(msg);
			}
			
			int workflowId = commGroup.getSppBatchRef();
			
			QueryJobData queryJobData = 
			 convertInstructionToQueryJobData(instr, workflowId, userName, cdbFacade);
			
			if (queryJobData == null || queryJobData.isEmpty()) {
				String msg = "Cannot submit job to spp, failed to create queryJobData";
				Log.error(msg);
				throw new ServiceException(msg);
			}
			
			WorkflowJobResponse response = 
			 SppIntegrationUtils.triggerSppQueryJob(queryJobData, userName, cdbFacade);
		
			doPostWorkflowSubmissionUpdates(instr, response, cdbFacade, userName);
			
			Interaction interaction = Interaction.get
			 (comm.getInteractionId(), cdbFacade);
			
			// Add an activity to the Interaction, indicating a job was started.
			interaction.addActivity
			 (instr.getType().getName(), "Client Query job started with ID: " + 
			 workflowId, userName, cdbFacade);	
					
			return true;
			
		} else if (comm.getType().equals(CommType.COMM_FIRST_TRANS_FEED)
			||
			comm.getType().equals(CommType.TRANSACTION_BATCH)) {
			// Transaction Batch instructions won't have a document. Must generate an 
			// SPP job directly from the Comm/Instruction data.
						
			doPreWorkflowSubmissionUpdates(instr, cdbFacade, userName);
			
			SPPJobProfile jobProfile = SPPJobProfile.JOB_PROFILE;
			
			Variable[] initParams = getSppVariables
			 (instr, comm, jobProfile, null, userName, cdbFacade);
			
			WorkflowJobResponse response = 
			 SppIntegrationUtils.triggerSppJob(initParams, jobProfile);
			
			doPostWorkflowSubmissionUpdates(instr, response, cdbFacade, userName);
		
			return true;
			
		} else if ((	
				comm.getType().equals(CommType.DOCUMENT) 
				&& 
				instr.getType().isStaticDataUpdate())
			) {
			
			doPreWorkflowSubmissionUpdates(instr, cdbFacade, userName);
			
			SPPJobProfile jobProfile = SPPJobProfile.JOB_PROFILE;
			
			Variable[] initParams = getSppVariables
			 (instr, comm, jobProfile, null, userName, cdbFacade);
			
			WorkflowJobResponse response = 
			 SppIntegrationUtils.triggerSppJob(initParams, jobProfile);
			
			doPostWorkflowSubmissionUpdates(instr, response, cdbFacade, userName);
		
			return true;

		} else {
			Log.error("Unknown instruction type, cannot submit to SPP, instructionId: "
			 + instr.getInstructionId());
			
			return false;
		}
	}
	
	/** Updates applied Instruction Data field values just before a workflow
	 *  submission.
	 *  
	 *  Currently this method will allocate a new UCM Job ID value to the instruction.
	 *  
	 * @param instruction
	 * @param facade
	 * @param userName
	 * @throws DataException
	 */
	private static void doPreWorkflowSubmissionUpdates(Instruction instruction,
	 FWFacade facade, String userName) throws DataException {
		
		// Add the UCM_JOB_ID field value to the instruction, if applicable.
		if (SppIntegrationUtils.SET_JOB_ID) {
			
			//Set the Job ID for each job that starts in SPP
			int jobId = CCLAUtils.getNextSppJobId(facade);
		
			InstructionData ucmJobIdDataField = 
			 InstructionData.getCache().getCachedInstance
			 (InstructionData.Fields.UCM_JOB_ID);
			
			InstructionDataFieldValue ucmJobIdValue = 
			 new InstructionDataFieldValue(DataType.INT); 
			
			ucmJobIdValue.setIntValue(jobId);
			
			InstructionDataApplied.addOrUpdate
			 (instruction, ucmJobIdDataField, ucmJobIdValue, facade, userName);
		}
	}	
		
	/** Updates applied Instruction Data field values following a successful workflow
	 *  submission.
	 *  
	 *  Just adds a Workflow Date value currently.
	 *  
	 * @param instruction
	 * @param response
	 * @param facade
	 * @param userName
	 * @throws DataException 
	 */
	private static void doPostWorkflowSubmissionUpdates
	 (Instruction instruction, WorkflowJobResponse response, 
	 FWFacade facade, String userName) throws DataException {
		
		// Add the WORKFLOW_DATE field value to the instruction, if applicable.
		InstructionData workflowDateDataField = 
		 InstructionData.getCache().getCachedInstance
		 (InstructionData.Fields.WORKFLOW_DATE);
		
		InstructionDataFieldValue workflowDateValue = 
		 new InstructionDataFieldValue(DataType.DATE); 
		
		workflowDateValue.setDateValue(new Date());
		
		InstructionDataApplied.addOrUpdate
		 (instruction, workflowDateDataField, workflowDateValue, facade, userName);
	}
		
	/** Updates the xStatus value of a given UCM Document.
	 *  
	 *  The useTransaction flag should be false if you want to prevent UCM running the
	 *  metadata update service in its own transaction block.
	 *  
	 *  A flag is always added to the LWDocument attributes prior to calling the update
	 *  to prevent a cascading update call to the document's mapped instruction.
	 *  
	 * @param docId
	 * @param status
	 * @param useTransaction
	 * @throws ServiceException
	 */
	public static void setUCMDocStatus
	 (int docId, String status, boolean useTransaction)
	 throws DataException {
		
		try {
			LWDocument lwDoc = new LWDocument(docId, true);
			lwDoc.setUseTransaction(useTransaction);
			
			lwDoc.setAttribute(UCMFieldNames.Status, status);
			
			// Prevent cascading update to generated Instruction, if one exists.
			lwDoc.setAttribute(UpdateFilter.PREVENT_INSTRUCTION_UPDATE, "1");
			
			lwDoc.update();
			
			Log.debug("Updated xStatus value of UCM Document ID: " + docId + 
			 ", DocName: " + lwDoc.getDocName() + " to " + status);
			
		} catch (Exception e) {
			String msg = "Failed to set xStatus value for UCM Document ID: " + docId;
			
			Log.error(msg, e);
			throw new DataException(msg, e);
		}
	}
	
	/** 
	 * Determines whether a Batch Number should be set against a workflow job for an
	 * Instruction with the given type.
	 * 
	 * Tina said it was undesirable to have batch numbers against the STP update jobs,
	 * hence the need for this control.
	 * 
	 * @return true if a batch number should be set in the job data.
	 */
	public static boolean applyBatchNumberToWorkflowJob(InstructionType instrType) {
		if (instrType.isStaticDataUpdate())
			return false;
		else
			return true;
	}
	
	/** Generates a HashMap of UCM Metadata names and their associated values from the
	 *  data in the passed Instruction.
	 *  
	 *  Useful when generating SPP jobs, as these use UCM Metadata names in their
	 *  mappings.
	 *  
	 *  TODO: add Instruction ID field, Fund field
	 *  
	 * @param instruction
	 * @param userName
	 * @param facade
	 * @return
	 * @throws Exception 
	 */
	public static HashMap<String, String> getUcmVariables
	 (Instruction instruction, Comm comm, String userName, FWFacade facade) 
	 throws Exception {
		
		Log.debug("getUcmVariables:InstructionId:" + instruction.getInstructionId());
		
		HashMap<String, String> ucmVarMap = new HashMap<String, String>();
		
		/* 1. Do the 'easy' fields first. This data comes directly from Comm/Instruction
		 * attributes.
	 	*/
		
		// Instruction URL (DocUrl)
		ucmVarMap.put(UCMFieldNames.DocUrl, getInstructionURL(instruction));
		
		// Instruction ID (not yet used)
		ucmVarMap.put(UCMFieldNames.InstructionId, 
		 Integer.toString(instruction.getInstructionId()));
		
		// Workflow/Batch Number. Should be stored against the Comm Group.
		CommGroup commGroup = CommGroup.get(comm.getGroupId(), facade);
		
		if (applyBatchNumberToWorkflowJob(instruction.getType())) {
			if (commGroup.getSppBatchRef() != null) {
				ucmVarMap.put(UCMFieldNames.BatchNumber, 
				commGroup.getSppBatchRef().toString());
			}
		}
		
		// dID
		if (instruction.getInstructionDocGuid() != null) {
			LWDocument lwd;
			String docId;
			try {
				lwd = CCLAUtils.getLwdByRevisionFromDocGuid(instruction.getInstructionDocGuid());
				docId = lwd.getId();
			} catch (ServiceException e) {
				String err = "Unable to get lwd for docGuid: " + instruction.getInstructionDocGuid();
				Log.error(err);
				throw new ServiceException(err);
			}
			ucmVarMap.put
			 (UCMFieldNames.DocID, docId);
		}
		
		// dDocName TODO
		
		// dDocType
		ucmVarMap.put(UCMFieldNames.DocType, "Instruction");
		// dDocTitle (just use 'Instruction ID: XXX')
		ucmVarMap.put(UCMFieldNames.DocTitle, "Instruction ID "
		 + instruction.getInstructionId());
		// xSource (taken from Comm instance)
		ucmVarMap.put(UCMFieldNames.Source, comm.getSource().getName());
		
		// xProcessDate
		ucmVarMap.put(UCMFieldNames.ProcessDate, 
		 DateFormatter.getTimeStamp(instruction.getProcessDate()));
		
		// xDocumentClass (Instruction Type)
		ucmVarMap.put(UCMFieldNames.DocClass, instruction.getType().getName());
		
		/* 2. Now do the direct translation maps.
		 *
		 * Check to see if we can directly map this SPP variable by checking
		 * for a UCM metadata mapping.
		 */
		
		// Currently this will work for the following UCM fields:
		/*	xAmount
			xUnits
			xDocumentDate
			xFormId
			xComments
			xJobId
			xDestinationAccount
			xWorkflowDate
			xSignaturesValid
			xClientServicesRef*/
		HashMap<String, List<UCMMetadataTranslation>> directTranslations =
		 UCMMetadataTranslation.getUCMFieldCache().getCache();
	
		Log.debug("Found " + directTranslations.size() +
		 " available direct UCM field translations");
		
		for (String ucmFieldName : directTranslations.keySet()) {
			List<UCMMetadataTranslation> fieldTranslations = 
			 directTranslations.get(ucmFieldName);
			
			Log.debug("->" + ucmFieldName + 
			 " has " + fieldTranslations.size() + " available translations");
			
			for (UCMMetadataTranslation directTranslation : fieldTranslations) {
				// Find the corresponding applied instruction data value for this UCM field
				// (if it exists)
				InstructionDataFieldValue instrFieldValue = 
				 InstructionDataApplied.getFieldValue(instruction.getDataApplied(), 
				 directTranslation.getInstructionData().getInstructionDataId());
				
				Log.debug("Instr Data Field " +
				 directTranslation.getInstructionData().getName() + " [ID " +
				 directTranslation.getInstructionData().getInstructionDataId() + 
				 "] value=" +  ((instrFieldValue != null) ? 
						 instrFieldValue.getRawValue() : "[none]"));
				
				if (instrFieldValue != null && instrFieldValue.getRawValue() != null) {
					ucmVarMap.put(ucmFieldName, instrFieldValue.getRawValue());
				}
			}
		}
		
		/* 3. Finally do the normalized fields. This generally requires extracting the
		 *    corresponding static data records from the database to obtain the
		 *    required UCM field values.
		 */
		
		// Organisation/Client fields
		InstructionDataFieldValue orgDataValue =
		 instruction.getDataApplied(InstructionData.Fields.ORGANISATION_ID);
		
		if (orgDataValue != null && !orgDataValue.isEmpty()) {
			// xClientName
			Entity org = Entity.get(orgDataValue.getIntValue(), facade);
			ucmVarMap.put(UCMFieldNames.ClientName, org.getOrganisationName());
			
			Vector<AuroraClient> auroraClients = 
			 Entity.getAuroraClientMapping(org.getOrganisationId(), facade);
				
			if (!auroraClients.isEmpty()) {
				// xCompany
				ucmVarMap.put(UCMFieldNames.Company, 
				 auroraClients.get(0).getCompany().getCode());
				
				// xClientNumber
				ucmVarMap.put(UCMFieldNames.ClientNumber, 
				 auroraClients.get(0).getPaddedClientNumber());
			} else {
				Log.warn("Unable to apply Aurora Client Number and Company data " +
				 "for Instruction ID " + instruction.getInstructionId() + ", no " +
				 "mapped Aurora Client data for Organisation ID " + 
				 orgDataValue.getIntValue());
			}
		}
		
		// Account fields
		InstructionDataFieldValue accountDataValue =
		 instruction.getDataApplied(InstructionData.Fields.SOURCE_ACCOUNT_ID);
		
		if (accountDataValue != null && !accountDataValue.isEmpty()) {
			Account account = Account.get(accountDataValue.getIntValue(), facade);
			
			// xAccountNumber
			ucmVarMap.put(UCMFieldNames.AccountNumber, 
			 account.getAccountNumberString());
			
			// xFund
			Fund fund = Fund.getCache().getCachedInstance(account.getFundCode());
			
			ucmVarMap.put(UCMFieldNames.Fund, fund.getFundCode());
			ucmVarMap.put(UCMFieldNames.Company, fund.getCompany().getCode());
			
		} else {
			// Check for Fund field instead. Will be present on some instructions where 
			// only the Fund is known but not the account.
			InstructionDataFieldValue fundDataValue =
			 instruction.getDataApplied(InstructionData.Fields.FUND_CODE);
			
			if (fundDataValue != null && !fundDataValue.isEmpty()) {
				// xFund
				Fund fund = Fund.getCache().getCachedInstance
				 (fundDataValue.getStringValue());
				
				ucmVarMap.put(UCMFieldNames.Fund, fund.getFundCode());
			}
		}
		
		// TODO Destination Account ID? Map to Dest. AccNumExt and Dest Fund?
		
		// Destination Fund
		InstructionDataFieldValue destAccNumExtDataValue =
		 instruction.getDataApplied(InstructionData.Fields.DEST_ACCNUMEXT);
			
		if (destAccNumExtDataValue != null && !destAccNumExtDataValue.isEmpty()) {
			ucmVarMap.put(UCMFieldNames.DestinationFund, 
			 CCLAUtils.getSuffixChars(destAccNumExtDataValue.getStringValue()));
		}
		
		// Bank Account fields
		InstructionDataFieldValue bankAccountDataValue =
		 instruction.getDataApplied(InstructionData.Fields.SOURCE_BANK_ACCOUNT_ID);
		
		if (bankAccountDataValue != null && !bankAccountDataValue.isEmpty()) {
			BankAccount bankAccount = BankAccount.get
			 (bankAccountDataValue.getIntValue(), facade);
			
			// xBankAccountNumber
			ucmVarMap.put(UCMFieldNames.BankAccountNumber, 
			 bankAccount.getAccountNumber());
			
			// xSortCode
			ucmVarMap.put(UCMFieldNames.SortCode, 
			 bankAccount.getSortCode());
		}
		
		return ucmVarMap;
	}
	
	public static String getInstructionURL(Instruction instruction) {
		
		String httpServerAddress = 
		 SharedObjects.getEnvironmentValue("HttpServerAddress");
			
		return "http://" + httpServerAddress + "/ucm/idcplg?" +
		 "IdcService=CCLA_CP_INSTRUCTION_INFO" +
		 "&INSTRUCTION_ID=" + instruction.getInstructionId();
	}
	
	/**
	 * Translates the given instruction and its applied data into an array of SPP job
	 * variables, matching the given WorkflowProfile.
	 * 
	 * @param ucmVars optional list of UCM name-value pairs. Anything present in this
	 * 				  map will override the values extracted from Instruction data.
	 * 				  Good way to override parts of the data that will be dispatched
	 * 				  to workflow.
	 */
	public static Variable[] getSppVariables(Instruction instruction, Comm comm, 
	 SPPJobProfile profile, HashMap<String, String> ucmVars, 
	 String userName, FWFacade facade) 
	 throws DataException, ServiceException {
		
		// Ensure applicable data is loaded
		instruction.loadDataApplied(facade);
		
		// Fetch UCM variable mapping
		HashMap<String, String> ucmVarMap;
		try {
			ucmVarMap = getUcmVariables
			 (instruction, comm, userName, facade);
		} catch (Exception e) {
			throw new ServiceException("Unable to get UcmVariables: "+e.getMessage());
		}
		
		Log.debug("Obtained " + ucmVarMap.size() + " mapped UCM variables:");
		
		for (Map.Entry<String, String> ucmVarMapping : ucmVarMap.entrySet()) {
			Log.debug(ucmVarMapping.getKey() + "=" + ucmVarMapping.getValue());
		}
		
		if (ucmVars != null) {
			Log.debug(ucmVars.size() + " override vars were passed in: ");
			
			for (Map.Entry<String, String> ucmVarMapping : ucmVars.entrySet()) {
				Log.debug(ucmVarMapping.getKey() + "=" + ucmVarMapping.getValue());
				
				ucmVarMap.put(ucmVarMapping.getKey(), ucmVarMapping.getValue());
			}
		}
		
		Variable[] variables = new Variable[profile.getNumVariables()];
		int varIndex = 0;
		
		// Step through each of the workflow variables and populate where applicable
		int boundVars = 0;
		for (JobVariable workflowVariable : profile.getJobVariables()) {
			Variable variable = null;
			
			Log.debug("Searching for workflow var value: " 
			 + workflowVariable.getUcmVarName());
			
			String ucmVarValue = ucmVarMap.get(workflowVariable.getUcmVarName());
			
			Log.debug("Value was: " + ucmVarValue);
			
			if (ucmVarValue == null) {
				variable = workflowVariable.getEmptyVariableInstance();
			} else {
				variable = workflowVariable.getVariableInstance(ucmVarValue);
				boundVars++;
			}
			
			// Add new variable to array.
			Log.debug("Adding Job Variable: " + workflowVariable.getUcmVarName() + 
			 ", value: " + variable.getValue());
			
			variables[varIndex++] = variable;
		}
		
		Log.debug("Mapped values to " + boundVars + " out of "
		 + profile.getJobVariables().size() + " job variables");
		
		return variables;
	}
	
	/** Creates a QueryJobData object from Instruction/Comm/Interaction attributes.
	 *  
	 *  TODO: refactor into getSPPVariables() method instead.
	 *  
	 * @param instruction
	 * @param workflowId
	 * @param userName
	 * @param facade
	 * @return
	 * @throws DataException
	 * @throws ServiceException
	 */
	public static QueryJobData convertInstructionToQueryJobData
	 (Instruction instruction, int workflowId, String userName, FWFacade facade) 
	 throws DataException, ServiceException {
		
		if (instruction==null)
			throw new DataException("Cannot convert Instruction to QueryJobData, " +
			 "instruction is null");
		
		Comm comm = Comm.get(instruction.getCommId(), facade);
		
		if (comm==null || comm.getInteractionId()==null)
			throw new ServiceException
			 ("Cannot convert instruction data, comm or interaction id is null");
		
		Interaction interaction = Interaction.get(comm.getInteractionId(), facade);
		
		if (interaction==null)
			throw new ServiceException
			 ("Cannot convert instruction data, interaction is null for id: " 
			 + comm.getInteractionId());
		
		QueryJobData queryJobData = new QueryJobData();
		
		if (interaction.getOrganisationId() != null) {
			Vector<AuroraClient> auroraClients = 
				Entity.getAuroraClientMapping(interaction.getOrganisationId(), facade);
		
			if (auroraClients.size() > 0) {
				// Pick the first Aurora Client for now.
				AuroraClient auroraClient = auroraClients.get(0);
				String company 		= auroraClient.getCompany().getCode();
				String clientNumber	= auroraClient.getPaddedClientNumber();
				String fundCode		= null;
				String accountNumber = null;
				
				if (interaction.getAccountId() != null) {
					Account account = 
						Account.get(interaction.getAccountId(), facade);
					
					accountNumber = account.getAccountNumberString();
					fundCode = account.getFundCode();
				} else if (!StringUtils.stringIsBlank(interaction.getFundCode())) {
					fundCode = interaction.getFundCode();
				}
				
				// Build the query job data object. This will contain all required
				// parameters for starting a query job.
				
				queryJobData.createdBy = userName;
				
				queryJobData.date = new Date();
				
				queryJobData.documentClass = instruction.getType().getName();
				
				if (queryJobData.documentClass.equals("CLIENTQUERY")) {
					queryJobData.isClientQuery = true;
				} else if (queryJobData.documentClass.equals("COMPL")) {
					queryJobData.isComplaint = true;
				} else if (queryJobData.documentClass.equals("BREACH")) {
					queryJobData.isBreach = true;
				} else if (queryJobData.documentClass.equals("ERROR")) {
					queryJobData.isError = true;
				}

				queryJobData.company = company;
				queryJobData.clientNumber = clientNumber;
				queryJobData.accountNumber = accountNumber;
				queryJobData.fundCode = fundCode;
				
				queryJobData.workflowId = workflowId;
				queryJobData.instructionId = instruction.getInstructionId();
				
				instruction.loadDataApplied(facade);
				Vector<InstructionDataApplied> dataAppliedVec = instruction.getDataApplied();
				
				for (InstructionDataApplied dataApplied: dataAppliedVec) 
				{
					InstructionDataFieldValue fieldValue = dataApplied.getDataFieldValue();
					InstructionData instrData = dataApplied.getInstructionData();
					
					if (fieldValue!=null && instrData!=null) 
					{
						switch(instrData.getInstructionDataId()) 
						{
							case InstructionData.Fields.QUERY_CAUSE_ID:
								queryJobData.causeId = fieldValue.getIntValue();
								break;
							case InstructionData.Fields.QUERY_SUB_CAUSE_ID:
								queryJobData.subCauseId = fieldValue.getIntValue();
								break;
							case InstructionData.Fields.QUERY_SUMMARY:
								queryJobData.summary = fieldValue.getStringValue();
								break;
							case InstructionData.Fields.QUERY_HOW_IDENTIFIED:
								queryJobData.howIdentified = fieldValue.getStringValue();
								break;
							case InstructionData.Fields.QUERY_REQUIRED_ACTION:
								queryJobData.requiredAction = fieldValue.getStringValue();
								break;
							case InstructionData.Fields.QUERY_DATE_IDENTIFED:
								queryJobData.dateIdentified = fieldValue.getDateValue();
								break;
							case InstructionData.Fields.QUERY_DATE_OCCURRED:
								queryJobData.dateOccurred = fieldValue.getDateValue();
								break;
							case InstructionData.Fields.QUERY_DATE_RESOLVED:
								queryJobData.dateResolved = fieldValue.getDateValue();
								break;
							case InstructionData.Fields.UCM_JOB_ID:
								queryJobData.ucmJobId = fieldValue.getIntValue();
								break;
							case InstructionData.Fields.DOC_DATE:
							case InstructionData.Fields.FORM_ID:
							case InstructionData.Fields.INSTRUCTION_COMMENTS:
							case InstructionData.Fields.WITH_INSTRUCTION:
							case InstructionData.Fields.WORKFLOW_DATE:
							case InstructionData.Fields.SIGNATURES_VALID:
							case InstructionData.Fields.SOURCE_ACCOUNT_ID:
							case InstructionData.Fields.ORGANISATION_ID:
								//these are all document 
								break;
							default:
								break;						
						}
					}				
				}
			} else {
				Log.error("Unable to trigger SPP Query job, no Aurora " +
				 "Client Numbers found for Entity ID: " + 
				 interaction.getOrganisationId());
				return null;
			}
		} else {
			Log.error("Unable to trigger SPP Query job, no Entity " +
			 "ID specified with interaction.");
			return null;
		}	
		
		return queryJobData;
	}

	/**
	 * Generates a new Instruction instance, with the given Type and Status set. Other
	 * fields (such as commId and dates) are copied from the passed source instruction
	 * (sourceInstr).
	 * 
	 * The instruction is not added to the database via this call.
	 * 
	 * @param sourceInstr
	 * @param instrType
	 * @param instrStatus
	 * @param comm
	 * @param userName
	 * @param assignId whether or not a new Instruction ID will be assigned to it
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Instruction generateInstruction
	 (Instruction sourceInstr, InstructionType instrType, 
	 InstructionStatus instrStatus, 
	 boolean assignId, 
	 String userName, FWFacade facade) 
	 throws DataException {
		
		//1. check data, need to see if a instruction type and comm is valid.
		if (instrType==null) {
			String msg = "Cannot create instruction, type is missing";
			Log.error(msg);
			throw new DataException(msg);
		}
		
		if (sourceInstr==null) {
			String msg = "Cannot create instruction, source instruction is missing"; 
			Log.error(msg);
			throw new DataException(msg);
		}
		
		Log.debug("Generating new Instruction of type " + instrType.getName() + 
		 " from source instruction " + sourceInstr.getInstructionId());
			
		Comm comm = Comm.get(sourceInstr.getCommId(),facade);
		
		if (comm==null) {
			String msg = "Cannot create instruction of type " +
			 instrType.getName()+", comm is missing";
			
			Log.error(msg);
			throw new DataException(msg);
		}
		
		//2. create instruction
		// Create new instruction, but don't add it to the DB yet.
		Instruction thisInstruction = new Instruction
		 (comm.getCommId(), instrType, instrStatus, null, assignId, userName, facade);
		
		//Populate the process dates from the main instruction (if it exist, otherwise use new dates)
		thisInstruction.setProcessDate(
			sourceInstr.getProcessDate()==null?new Date():sourceInstr.getProcessDate());
		thisInstruction.setOriginalProcessDate(
			sourceInstr.getOriginalProcessDate()==null?new Date():sourceInstr.getOriginalProcessDate());
		
		try {
			thisInstruction.validate(facade);
		} catch (DataException e) {
			String msg = "Failed to generate Instruction due to validation error:"
			 + e.getMessage();
			
			Log.error(msg);
			throw new DataException(msg);
		}
		
		return thisInstruction;
	}	
	
	/**
	 * Copies applied Instruction Data from a source data list (sourceDataAppl) to the
	 * destination instruction (destInstr). If the overwrite flag is set, non-empty
	 * source data values will overwrite destination data values. If the flag isn't
	 * set, any existing data values on the destination instruction won't be touched.
	 * 
	 * Adds empty data values for any applicable fields which weren't specified in
	 * sourceDataAppl or destDataAppl.
	 * 
	 * NOTE: the sourceDataAppl instruction type doesn't have to match the destination
	 * instruction's type.
	 * 
	 * @param sourceInstr
	 * @param destInstr
	 * @param overrideIfEmpty
	 * @return
	 * @throws DataException
	 */
	public static Vector<InstructionDataApplied> copyInstructionData
	 (Vector<InstructionDataApplied> sourceDataAppl,
	 Instruction destInstr, Vector<InstructionDataApplied> destDataAppl,
	 boolean overwrite, FWFacade facade) 
	 throws DataException {
		
		//check source data
		if (sourceDataAppl == null)
			throw new DataException
			 ("Cannot copy instruction data, source data is null");
			
		if (destInstr == null || destInstr.getType() == null)
			throw new DataException
			 ("Cannot copy instruction data, destination instruction is null, or its "+
			 "Instruction Type hasn't been set");

		Vector<InstructionDataApplied> newDataAppl = 
		 new Vector<InstructionDataApplied>();
		
		// Add all existing destination instruction data to the new list
		if (destDataAppl != null) {
			for (InstructionDataApplied existingDataAppl : destDataAppl) {
				newDataAppl.add(existingDataAppl);
			}
		}
		
		//Loop through ApplicableInstructionData fields in the destination instruction
		//and see if the source contains a value. Rules are:
		//1. If source contains a value and dest doesn't, then use the source value.
		//2. If source contains a value and dest contains a value, 
		//   then use the source value if overrideIfNotEmpty is true.
		Vector<ApplicableInstructionData> appDataVec = destInstr.getType()
		 .getApplicableInstructionData();
		
		for (ApplicableInstructionData appData : appDataVec) {
			
			InstructionData instrData = appData.getInstructionData();
			int instrDataId = instrData.getInstructionDataId();
			
			// Was this applicable field set against the source data?
			InstructionDataApplied sourceDataApplied = 
			 InstructionDataApplied.getDataAppled
			 (sourceDataAppl, instrDataId);
			
			// Was this applicable field already set against the destination 
			// instruction?
			InstructionDataApplied destDataApplied = 
			 InstructionDataApplied.getDataAppled
			 (newDataAppl, instrDataId);	
			
			if (sourceDataApplied!=null 
				&& !sourceDataApplied.getDataFieldValue().isEmpty()) {
				// Non-empty source data value.
				
				if (destDataApplied!=null && !destDataApplied.getDataFieldValue().isEmpty()) {
					if (overwrite) // Update existing applied data value
						destDataApplied.setDataFieldValue(sourceDataApplied.getDataFieldValue());
				} else {
					destDataApplied = new InstructionDataApplied // Add new applied data value
					 (destInstr.getInstructionId(), appData, sourceDataApplied.getDataFieldValue());
				}
				
			} else {
				if (destDataApplied==null) {
					// Set empty value
					InstructionDataFieldValue fieldValue = 
					 new InstructionDataFieldValue(instrData.getDataType());
					destDataApplied = new InstructionDataApplied
					 (destInstr.getInstructionId(), appData, fieldValue);
				} 
			}
			
			newDataAppl.add(destDataApplied);
		}
		
		return newDataAppl;
	}
	
	/** Determines whether the passed set of applied data fields are valid for the
	 *  passed Instruction type.
	 *  
	 * @param instr
	 * @param dataApplied
	 * @throws DataException if a required field value is empty/missing
	 */
	public static void validateAppliedData(Instruction instr, 
	 Vector<InstructionDataApplied> dataApplied) throws DataException {
		
		// Fetch set of applicable data fields for the instruction's type.
		Vector<ApplicableInstructionData> applInstrData = ApplicableInstructionData
		 .getInstructionTypeCache().getCachedInstance
		 (instr.getType().getInstructionTypeId());
		
		InstructionData missingDataField = null;
		
		for (ApplicableInstructionData applData : applInstrData) {
			if (!applData.isOptional()) {
				// This field is required for this instruction type - make sure
				// we have a non-empty applied data value set.
				int instrDataId = applData.getInstructionData().getInstructionDataId();
				
				boolean foundValue = false;
				
				for (InstructionDataApplied instrData : dataApplied) {
					if (instrData.getInstructionData().getInstructionDataId()
						==
						instrDataId) {
						// Data field has been mapped, but is it empty?
						if (instrData.getDataFieldValue().isEmpty()) {
							missingDataField = applData.getInstructionData();
							break;
						} else {
							foundValue = true;
						}
					}
					
					if (!foundValue)
						missingDataField = applData.getInstructionData();
				}
				
				if (!foundValue) {
					String msg = "Field '" + missingDataField.getLabel() + 
					 "' is required for instruction type " + instr.getType().getName() +
					 ". Ensure this field has a value set, or change the " +
					 "instruction type.";
					
					Log.error(msg);
					throw new DataException(msg);
				}
			}
		}
	}
	
	public static boolean checkDuplicateSDUInstruction
	 (Instruction instr, FWFacade facade) throws DataException {
		
		if (InstructionUtils.getDuplicateSDUInstructionId(instr, facade)!=null)
			return true;
		
		return false;
	}
	
	/**
	 * Checks for a duplicate instruction of the passed SDU Instruction. If one is found,
	 * its Instruction ID is returned.
	 * 
	 * @param instr
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Integer getDuplicateSDUInstructionId
	 (Instruction instr, FWFacade facade) throws DataException {
		
		Integer duplicateId = null;
		
		if (instr==null)
			return duplicateId;
		
		//Check if the Instruction instance has attribute data loaded, if not load it
		if (instr.getDataApplied()==null)
			instr.loadDataApplied(facade);
		
		if (instr.getDataApplied()==null) {
			return duplicateId;
		}
		
		// Extract SDU field values
		InstructionDataFieldValue companyIdFieldValue = 
		 instr.getDataApplied(InstructionData.Fields.COMPANY_ID);
		
		InstructionDataFieldValue orgIdFieldValue = 
		 instr.getDataApplied(InstructionData.Fields.ORGANISATION_ID);
		
		InstructionDataFieldValue accountIdFieldValue = 
		 instr.getDataApplied(InstructionData.Fields.SOURCE_ACCOUNT_ID);
		
		InstructionDataFieldValue personIdFieldValue = 
		 instr.getDataApplied(InstructionData.Fields.PERSON_ID);
		
		return getLatestSDUInstructionIdByValues(
			instr.getType(), 
			instr.getInstructionId(), 
			companyIdFieldValue != null ? companyIdFieldValue.getIntValue() : null, 
			orgIdFieldValue != null ? orgIdFieldValue.getIntValue() : null, 
			accountIdFieldValue != null ? accountIdFieldValue.getIntValue() : null, 
			personIdFieldValue != null ? personIdFieldValue.getIntValue() : null, 
			facade
		);
	}
	
	/** 
	 * Checks for an SDU instruction of the given type, with a matching set of passed
	 * attributes. If any are found, the INSTRUCTION_ID of the latest one is returned.
	 * 
	 * The underlying view used to run the search against ignores any instructions with 
	 * the given status IDs: 7,11,29,32
	 * 
	 * @param instrType
	 * @param excludedInstructionId		Instruction ID to filter from results. Useful
	 * 									when running a duplicate check on an existing 
	 * 									instruction. Can be null.
	 * @param companyId
	 * @param orgId
	 * @param accountId
	 * @param personId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Integer getLatestSDUInstructionIdByValues(
	 InstructionType instrType,
	 Integer excludedInstructionId,
	 Integer companyId,
	 Integer orgId,
	 Integer accountId,
	 Integer personId,
	 FWFacade facade) throws DataException {
		
		Log.debug("Fetching latest SDU Instruction for Instr Type: " 
		 + instrType.getName() + ", Excl. Instr ID: " + excludedInstructionId + 
		 ", Company ID: " + companyId + ", Org ID: " + orgId +
		 ", Account ID: " + accountId + ", Person ID: " + personId);

		StringBuffer query = new StringBuffer();
		
		query.append("SELECT * FROM V_SDU_INSTRUCTION_DATA WHERE");

		//Append instruction type
		query.append(" INSTRUCTION_TYPE_ID="+instrType.getInstructionTypeId());
		
		//hard code lookup for now to match view.
		//TODO write proper queries for this.
		
		//Check company
		query.append(" AND"); 

		if (companyId != null) {
			query.append(" COMPANY_ID="+companyId);
		} else {
			query.append(" COMPANY_ID IS NULL");			
		}

		//check person
		query.append(" AND"); 

		if (personId != null) {
			query.append(" PERSON_ID="+personId);
		} else {
			query.append(" PERSON_ID IS NULL");
		}
		
		if (instrType.getInstructionTypeId()==InstructionType.Ids.CREATE_ACCOUNT ||
			instrType.getInstructionTypeId()==InstructionType.Ids.UPDATE_ACCOUNT) {			
			//check account
			query.append(" AND"); 
	
			if (accountId != null) {
				query.append(" SOURCE_ACCOUNT_ID="+accountId);
			} else {
				query.append(" SOURCE_ACCOUNT_ID IS NULL");			
			}
		}
		
		if ((instrType.getInstructionTypeId()==InstructionType.Ids.CREATE_CLIENT ||
			instrType.getInstructionTypeId()==InstructionType.Ids.UPDATE_CLIENT)
			||
			(instrType.getInstructionTypeId()==InstructionType.Ids.CREATE_ACCOUNT ||
			instrType.getInstructionTypeId()==InstructionType.Ids.UPDATE_ACCOUNT)) {			
			//check organisation
			query.append(" AND"); 
	
			if (orgId != null) {
				query.append(" ORGANISATION_ID="+orgId);
			} else {
				query.append(" ORGANISATION_ID IS NULL");
			}
		}
		
		// Exclude the instruction ID from results.
		if (excludedInstructionId != null)
			query.append(" AND INSTRUCTION_ID <> "+excludedInstructionId);
		
		query.append(" ORDER BY INSTRUCTION_ID DESC");
		
		Log.debug("Checking for existing SDU Instruction with query string: " 
		 + query.toString());
		
		DataResultSet rs = facade.createResultSetSQL(query.toString());
		
		if (rs!=null && !rs.isEmpty()) {
			Integer instrId = CCLAUtils.getResultSetIntegerValue
			 (rs, SharedCols.INSTRUCTION);
			
			Log.debug("Found matching SDU Instruction with Instruction ID: " + instrId);
			return instrId;
			
		} else {
			Log.debug("No matching SDU Instruction found");
			return null;
		}
	}
	
	/**
	 * Return a new/current InstructionDataApplied object for the instruction and instruction data.
	 * 
	 * @param instr
	 * @param instrDataId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static InstructionDataApplied getDataApplied
	 (Instruction instr, int instrDataId, FWFacade facade) throws DataException {
		
		//update the isAuthorisedFlag
		ApplicableInstructionData appData = null;
		InstructionData instrData = null;
		InstructionDataApplied instrDataApplied = 
			InstructionDataApplied.getDataApplied(
					instr.getInstructionId(), 
					instrDataId,
					facade); 
		
		
		if (instrDataApplied==null) {
 
			instrData = InstructionData.getCache().getCachedInstance(instrDataId);
			
			if (instrData == null) {
				Log.error("Cannot find instructionData for id:"+instrDataId);
				throw new DataException("Cannot find instructionData for id:"+instrDataId);
			}
			
			appData = ApplicableInstructionData.getApplicableInstructionDataByName(
						instr.getType().getInstructionTypeId(), 
						instrData.getName(),
						facade);
			
			if (appData==null) {
				Log.error("Instruction:"+instr.getInstructionId()+
						" is not applicable for instructionData:"+
						instrDataId);
				
				throw new DataException("Instruction:"+instr.getInstructionId()+
						" is not applicable for instructionData:"+
						instrDataId);
			}
			
			InstructionDataFieldValue fieldValue = new InstructionDataFieldValue(instrData.getDataType());
			instrDataApplied = new InstructionDataApplied(instr.getInstructionId(), appData, fieldValue);
		} 
		return instrDataApplied;
		
	}
	
	/** Triggers a job based off the passed Instruction data, without actually 
	 *  sending the instruction itself through workflow, or touching its Instruction 
	 *  status.
	 *  
	 *  It is designed so that this method can be called multiple times for the same
	 *  Instruction.
	 *  
	 *  Adds a special audit note to the Instruction, indicating that a notification
	 *  job was triggered along with the associated UCM_JOB_ID value.
	 *  
	 * @param instr
	 * @param userName
	 * @param facade
	 * @return
	 * @throws ServiceException
	 * @throws DataException
	 */
	public static WorkflowJobResponse triggerSppNotificationJob
	 (Instruction instr, String userName, FWFacade facade)
	 throws ServiceException, DataException {
		
		Log.debug("Triggering SPP workflow notification for instruction:" 
				+ instr.getInstructionId() 
				+ ", type: "+instr.getType().getName());
		
		SPPJobProfile jobProfile = SPPJobProfile.JOB_PROFILE;
		
		int commId = instr.getCommId();
		
		Comm comm = Comm.get(commId, facade);
		CommGroup commGroup = CommGroup.get(comm.getGroupId(), facade);
		
		// Ensure the Comm Group has been stamped with a batch number.
		CommUtils.prepareCommGroupForSpp(commGroup, facade);
		
		//Set the Job ID for this notification job
		int jobId = CCLAUtils.getNextSppJobId(facade);
		
		HashMap<String, String> customJobVars = new HashMap<String, String>();
		customJobVars.put(UCMFieldNames.JobID, Integer.toString(jobId));
		
		Variable[] initParams = InstructionUtils.getSppVariables
		 (instr, comm, jobProfile, customJobVars, userName, facade);
		
		WorkflowJobResponse response = 
		 SppIntegrationUtils.triggerSppJob(initParams, jobProfile);

		Log.debug("Started workflow notification job with UCM Job ID: " + jobId);
		
		// Add a special 'Workflow Notification Job Dispatched' Instruction Audit
		InstructionAudit instrAudit = new InstructionAudit
		 (null,instr.getInstructionId(),
		 InstructionAuditAction.ACTION_START_NOTIFICATION, 
		 null, 
		 instr.getStatus().getInstructionStatusId(), 
		 null, null, 0, Globals.Users.System);
		
		InstructionAudit.add(instrAudit, facade);
		
		return response;
	}
	
	/** Combines two sets of applied instruction data instances.
	 * 
	 *  All items from the original set are added to a new list. Then each item from
	 *  the append data is stepped through and checked to see if it already exists in
	 *  the new list.
	 *  
	 *  If it was already present in the original list, the value will be overwritten
	 *  if:
	 *  
	 *  -the original value is empty, or
	 *  -the overwrite flag is set
	 * 
	 *  It is expected that the passed data sets both correspond to the same Instruction
	 *  ID.
	 * 
	 * @param origData
	 * @param appendData
	 * @param overwrite
	 * @return
	 */
	public static Vector<InstructionDataApplied> appendDataApplied(
		Vector<InstructionDataApplied> origData,
		Vector<InstructionDataApplied> appendData,
		boolean overwrite) {
		
		Log.debug("Appending applied data sets. " + origData.size() + 
		 " original items, " + appendData.size() + " append items");
		
		// Create mapping between Instruction Data IDs and applied instruction data
		// instances.
		HashMap<Integer, InstructionDataApplied> newDataSet = 
		 new HashMap<Integer, InstructionDataApplied>();
		
		// Add all original applied data to the mapping
		for (InstructionDataApplied origDataAppl : origData) {
			newDataSet.put(origDataAppl.getApplicableInstructionData()
			 .getInstructionData().getInstructionDataId(),
			 origDataAppl);
		}
		
		// Step through all data to be appended
		for (InstructionDataApplied appendDataAppl : appendData) {
			int instrDataId = appendDataAppl.getApplicableInstructionData()
			 .getInstructionData().getInstructionDataId();
			
			InstructionDataApplied origDataAppl = newDataSet.get(instrDataId);
			
			if (origDataAppl != null) {
				// May need to overwrite existing value here.
				if (origDataAppl.getDataFieldValue().isEmpty()
					||
					overwrite) {
					// overwrite the data field value
					Log.debug("Overwriting original data value [" + 
					 origDataAppl.getDataFieldValue().getRawValue() + "] with " +
					 "append value [" + appendDataAppl.getDataFieldValue().getRawValue() 
					 + "]");
					
					origDataAppl.setDataFieldValue(appendDataAppl.getDataFieldValue());
				}
			} else {
				newDataSet.put(instrDataId, appendDataAppl);
			}
		}
		
		Vector<InstructionDataApplied> newData = new Vector<InstructionDataApplied>();
		newData.addAll(newDataSet.values());
		
		Log.debug("Returning appended set of " + newData.size() + " data values");
		
		return newData;
	}

	/**
	 * Determines whether or not this instruction is eligible for generating Static
	 * Data Update instructions.
	 * 
	 * The Instruction Type must have 1 or more Static Data Updates mapped to it, and
	 * the corresponding account must have the 'Aurora Account' flag set.
	 * 
	 * @param type
	 * @return
	 * @throws DataException 
	 */
	public static boolean isEligibleToGenerateStaticDataUpdates
	 (Instruction instr, FWFacade facade) throws DataException{
		
		if (StaticDataUpdateApplied.getIdCache()
				.getCachedInstance(instr.getType().getInstructionTypeId())!=null) {
			InstructionDataApplied accountIdDataApplied = InstructionDataApplied
			 .getDataApplied(instr.getInstructionId(), 
			 InstructionData.Fields.SOURCE_ACCOUNT_ID, facade);
			
			if (accountIdDataApplied != null) {
				InstructionDataFieldValue accountIdValue = 
				 accountIdDataApplied.getDataFieldValue();
				
				// Instruction Type is eligible for generating SDU instructions. Confirm
				// the linked Account is an Aurora Account.
				if (!accountIdValue.isEmpty()) {
					Account account = Account.get(accountIdValue.getIntValue(), facade);
					return (account.isAuroraAccount());
				}
			}
			
			return true;
		} else
			return false;
	}
}
