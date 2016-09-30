package com.ecs.ucm.ccla.commproc;

import intradoc.common.ServiceException;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.ecs.stellent.auditlog.AuditUtils;
import com.ecs.stellent.iris.batch.BatchDocumentServices;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals;
import com.ecs.ucm.ccla.Globals.UCMFieldNames;
import com.ecs.ucm.ccla.commproc.translation.OrganisationIdFieldHandler;
import com.ecs.ucm.ccla.data.comm.Comm;
import com.ecs.ucm.ccla.data.comm.CommGroup;
import com.ecs.ucm.ccla.data.comm.CommSource;
import com.ecs.ucm.ccla.data.comm.CommType;
import com.ecs.ucm.ccla.data.instruction.ApplicableInstructionData;
import com.ecs.ucm.ccla.data.instruction.Instruction;
import com.ecs.ucm.ccla.data.instruction.InstructionData;
import com.ecs.ucm.ccla.data.instruction.InstructionDataApplied;
import com.ecs.ucm.ccla.data.instruction.InstructionDataFieldValue;
import com.ecs.ucm.ccla.data.instruction.InstructionStatus;
import com.ecs.ucm.ccla.data.instruction.InstructionType;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Used to convert UCM document bundles (i.e. Envelopes) into Comm/Instruction 
 *  entities.
 *  
 *  Also handles UCM Metadata -> Instruction Data conversion.
 *  
 * @author Tom
 *
 */
public class DocumentBundleTranslator {
	
	private String bundleRef;
	private String userName;
	
	private FWFacade facade;
	
	private CommGroup commGroup;
	
	/** Determines whether the database will be updated when running the translate
	 *  method.
	 */
	private boolean persist;
	
	/** Determines whether the translate method runs in 'append' mode - this will
	 *  assume the bundle has been previously translated, and will only add instructions
	 *  for documents that haven't been translated yet.
	 */
	private boolean append;
	
	public static class FailReason {
		// Lists of translation failure types. Used when throwing
		// BundleTranslateExceptions.
		public static FailReason EMPTY_BUNDLE 
		 = new FailReason("Empty Bundle");
		public static FailReason COMM_GEN_FAILURE
		 = new FailReason("Failed to generate Communication entry");
		public static FailReason INSTRUCTION_GEN_FAILURE
		 = new FailReason("Failed to generate Instruction entry");
		public static FailReason INSTRUCTION_DATA_GEN_FAILURE
		 = new FailReason("Failed to generate Instruction data");
		public static FailReason COMM_NOT_FOUND_FOR_INSTRUCTION
		 = new FailReason("Failed to match Instruction against a " +
		   "Communication entry");
		public static FailReason UNKNOWN_DOC_SOURCE 
		 = new FailReason("Unknown Document Source");
		public static FailReason UNKNOWN_INSTRUCTION_TYPE
		 = new FailReason("Unknown Instruction Type");
		public static FailReason UNKNOWN_CLIENT
		 = new FailReason("Unknown Client");
		public static FailReason MISSING_DEPENDANT_DOC
		 = new FailReason("Missing Dependant Document");
		public static FailReason MISSING_DEPENDANT_INSTRUCTION
		 = new FailReason("Missing Dependant Instruction");
		public static FailReason MISSING_PARENT_DOC 
		 = new FailReason("Missing Parent Document");
		public static FailReason MISSING_REQUIRED_FIELD
		 = new FailReason("Missing Required Field");
		public static FailReason NO_MAPPED_APPLICABLE_DATA_FIELDS
		 = new FailReason("No mapped applicable data fields");
		
		private String label;
		
		public FailReason(String label) {
			this.setLabel(label);
		}

		public void setLabel(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}
	}
	
	/** Stores all the Comm instances that will be generated for the documents in
	   	this bundle.
	
		One Comm is created per unique PDF document in the bundle, i.e. every
	 	bundle item with a dDocType of 'Document'
	 	
	 	Existing Comms for this CommGroup are preloaded here when running in append
	 	mode.
	 */
	private Vector<Comm> comms = new Vector<Comm>();
	
	/** Stores the instructions that belong to the CommGroup.
	 * 
	 * Existing Instructions for this CommGroup are preloaded here when running in 
	 * append mode.
	 */
	private Vector<Instruction> instructions = new Vector<Instruction>();
	
	/** Stores any newly-created Instructions, which must be added to the DB when
	 *  running in persist mode.
	 * 
	 */
	private Vector<Instruction> newInstructions = new Vector<Instruction>();
	
	/** This stores a mapping between the required Comm instances and their
	 * 	linked Instructions.
	 *
	 * 	One Comm is created per unique PDF document in the bundle, i.e. every
	 * 	bundle item with a dDocType of 'Document'.
	 */ 
	private HashMap<Comm, Vector<Instruction>> commInstructionMap = 
	 new HashMap<Comm, Vector<Instruction>>();
	
	/**	Cache of dDocName -> dIDs. Used for resolving dID values from referenced
	 * 	dDocName values in ChildDocument data.
	 */
	private HashMap<String, Integer> docIdMap = new HashMap<String, Integer>();
	
	/**	Cache of dIDs -> dDocNames. Used for resolving dDocName values from referenced
	 * 	dID values in generated Instruction instances.
	 */
	private HashMap<Integer, String> docNameMap = new HashMap<Integer, String>();
	
	/**	Cache of dIDs -> docGuids. Used for resolving docGuid values from referenced
	 * 	dID values in generated Instruction instances.
	 */
	private HashMap<Integer, String> docGuidMap = new HashMap<Integer, String>();
	
	
	/** Cache of dID -> Instructions.
	 * 
	 */
	private HashMap<Integer, Instruction> docInstructionMap = 
	 new HashMap<Integer, Instruction>();
	
	/** dID -> dependant dIDs. Only relevant for bundle documents withan 
	 *  xDependantDocName value set.
	 *  
	 * 	These are saved in an early pass of the bundle items and converted to
	 * 	dependant Instruction IDs later on.
	 */
	HashMap<Integer, Integer> dependantDocIdMap = new HashMap<Integer, Integer>();
	
	/** Stores new Instruction IDs and InstructionDataApplied (i.e. the Instruction
	 * field values)
	 */
	HashMap<Instruction, Vector<InstructionDataApplied>> instructionFieldValues = 
	 new HashMap<Instruction, Vector<InstructionDataApplied>>();
		
	/** ResultSet of bundle documents data.
	 * 
	 */
	DataResultSet rsBatchItems;
	
	/** Field translator to acquire Organisation IDs from UCM doc metadata for the Comm 
	 *  instances */
	OrganisationIdFieldHandler orgIdTranslator;
	
	/** Builds a new bundle translator. 
	 * 
	 *  The bundleRef must correspond to an existing Iris bundle reference.
	 *  
	 *  If the persist flag is enabled, new CommGroup/Comm/Instruction/InstructionData
	 *  entries will be added to the database. Running without this flag set should
	 *  be used to 'test' whether the bundle can be successfully converted into a
	 *  Comms/Instruction set.
	 *  
	 *  If the append flag is enabled, the translation method expects that the bundle
	 *  has been previously translated and will attempt to add any new instructions that
	 *  weren't translated before.
	 * 
	 * @param bundleRef
	 * @param sppBatchId
	 * @param persist
	 * @param append
	 * @param facade
	 * @param userName
	 * @throws DataException
	 * @throws ServiceException
	 */
	public DocumentBundleTranslator
	 (String bundleRef, boolean persist, boolean append, 
	 FWFacade facade, String userName) 
	 throws DataException, ServiceException {
		
		this.bundleRef = bundleRef;
		this.setPersist(persist);
		
		this.facade = facade;
		this.userName = userName;
		
		this.persist = persist;
		
		this.append = append;
		
		this.orgIdTranslator = new OrganisationIdFieldHandler();
	}
	
	public Vector<Instruction> translate() throws DataException, ServiceException {
		if (StringUtils.stringIsBlank(bundleRef))
			throw new ServiceException("Bundle Reference is empty/missing");
		
		Log.info("=====================================================");
		Log.info("Converting UCM document bundle " + bundleRef);
		Log.info("Persist conversion? " + persist);
		Log.info("Append mode? " + append);
		
		// Load parent bundle document data
		FWFacade ucmFacade = CCLAUtils.getFacade(false);
		DataResultSet rsParentBatchItem = 
		 BatchDocumentServices.getParentBatchItem(bundleRef, ucmFacade);
		
		if (rsParentBatchItem == null)
			throw new ServiceException("Unable to convert bundle: no parent bundle " +
			 "document with bundle ref: " + this.bundleRef);
		
		getBundleItems(ucmFacade);
		
		if (append) {
			// Fetch the existing CommGroup/Comms/Instructions.
			
			// Get CommGroup
			this.commGroup = CommGroup.getByUCMBatchRef(bundleRef, facade);
			
			// Fail if no CommGroup, i.e. the bundle hasn't been persisted yet.
			if (this.commGroup == null)
				throw new ServiceException("Unable to append new Instructions: " +
				 "document bundle hasn't been added to the Instruction Register yet");
			
			// Get Comms
			this.comms = CommGroup.getComms
			 (commGroup.getCommGroupId(), facade);
			
			// Get Instructions
			this.instructions = CommGroup.getInstructions
			 (commGroup.getCommGroupId(), facade);
			
			Log.info("Existing CommGroup ID " + this.commGroup.getCommGroupId() + 
			 " had " +  this.comms.size() + " Comms and " + this.instructions + 
			 " Instructions");
		}
		
		Integer sppBatchId = null;
		
		if (!append) {
			// First we'll need to create a new Group ID and add a new COMM_GROUP entry
			// to the database.
			if (persist) {
				// Check for an existing SPP Batch ID on the parent item.
				sppBatchId = CCLAUtils.getResultSetIntegerValue
				 (rsParentBatchItem, UCMFieldNames.BatchNumber);
				
				if (sppBatchId != null && sppBatchId == 0)
					sppBatchId = null;
				
				this.commGroup = CommGroup.add(bundleRef, sppBatchId, facade, userName);
			} else
				this.commGroup = new CommGroup(); // create temp CommGroup instead
		}

		// Generate new Comm entities
		generateComms();
		// Generate new Instruction entities, along with their data fields
		generateInstructions();
		
		// Set dependant instruction IDs last, if running in persist mode.
		if (persist)
			applyInstructionDependencies();
		
		if (persist) {
			// Add audit message to the UCM Document bundle indicating how many
			// instructions were created.
			Vector<String> params = new Vector<String>();
			params.add(Integer.toString(this.commGroup.getCommGroupId()));
			params.add(Integer.toString(this.comms.size()));
			params.add(Integer.toString(this.newInstructions.size()));
			if (sppBatchId != null)
				params.add(Integer.toString(sppBatchId));
			
			AuditUtils.addAuditEntry
			 ("IRIS", "ADD-BUNDLE-TO-INSTRREG", 
			 rsParentBatchItem.getStringValueByName(UCMFieldNames.DocName), 
			 rsParentBatchItem.getStringValueByName(UCMFieldNames.DocTitle), 
			 Globals.Users.System,
			 instructions.size() + " items added to Instruction Register" 
			 + ((sppBatchId != null) ? 
				" with pre-allocated workflow ID " + sppBatchId : ""),  
			 params);
		}
		
		return instructions;
	}
	
	/** Fetchs a ResultSet of content items in the bundle
	 * @throws DataException 
	 * @throws ServiceException 
	 * 
	 */
	private void getBundleItems(FWFacade facade) throws DataException, ServiceException {
		
		rsBatchItems = BatchDocumentServices.getBatchItems(bundleRef, facade);
		
		// Fail immediately if bundle is empty.
		if (rsBatchItems.isEmpty())
			throw new BundleTranslateException
			 ("No items found in bundle ref. " + bundleRef, 
			 FailReason.EMPTY_BUNDLE, null);
		
		Log.debug(rsBatchItems.getNumRows() + " documents in bundle");
	}
	
	/**	1.Generate new Comm instances for each Document in the bundle 
	 *	  (not ChildDocuments). They get added to the database here.
	 *  2.Build dDocName -> dID mapping, used to resolve parent document references 
	 *	  later.
	 *
	 * 	@throws DataException 
	 */
	private void generateComms() throws DataException, ServiceException {
		
		this.rsBatchItems.first();
		int newCommCount = 0;
		
		Log.info("Generating Comms");
		
		do {
			String docType 	= rsBatchItems.getStringValueByName(UCMFieldNames.DocType);
			String docName	= rsBatchItems.getStringValueByName(UCMFieldNames.DocName);
			Integer docId 	= CCLAUtils.getResultSetIntegerValue
			 (rsBatchItems, UCMFieldNames.DocID);
			
			String docGuid = CCLAUtils.getDocGuidFromDid(docId);
			
			// Add dDocName -> dID mapping for this item.
			this.docIdMap.put(docName, docId);
			// Add dID -> dDocName mapping for this item.
			this.docNameMap.put(docId, docName);
			// Add dID -> docGuid mapping for this item.
			this.docGuidMap.put(docId, docGuid);

			if (docType.equals("Document")) {
		
				// While running in append mode, check we haven't added the Comm to the
				// DB previously.
				if (append) {
					Comm existingComm = getExistingCommByDocGuid(docGuid);
			
					if (existingComm != null) {
						Log.debug("Comm already exists for Doc ID: " + docId + 
						 " (Comm ID :" + existingComm.getCommId() + 
						 ". Skipping document");
						continue;
					}
				}
					
				// New Comm required.
				CommSource source = CommSource.getByDocumentSource(
				 this.rsBatchItems.getStringValueByName(UCMFieldNames.Source));
				
				if (source == null) {
					String msg = "Unable to resolve source of document " + docName +
					 ", xSource value is missing or invalid";
					
					Log.error(msg);
					throw new BundleTranslateException
					 (msg, FailReason.UNKNOWN_DOC_SOURCE, docName);
				}
				
				Log.debug("Resolved Comm Source as: " + source.getName());
				
				String createdBy = null;
				
				// Special field xScanUser, populated by ODC with the name of the
				// logged in scan user. This should replace the UCM user name where
				// possible.
				// It is also populated as 'DripFeed' or 'DripFeed (with data)' by items
				// originating from the DripFeed uploader.
				String scanUser = this.rsBatchItems.getStringValueByName
				 (UCMFieldNames.ScanUser);

				if (!StringUtils.stringIsBlank(scanUser)) {
					Log.debug("Found a non-empty " + UCMFieldNames.ScanUser + 
					 " field value: " + scanUser);
					
					if (source.equals(CommSource.FAX)) 
						Log.debug("Item had a source of Fax, using DripFeed user");
					else {
						Log.debug("Is DripFeed user?: " + 
						 scanUser.startsWith(Globals.Users.DripFeed));
					}
					
					if (source.equals(CommSource.FAX)
						||
						scanUser.startsWith(Globals.Users.DripFeed))
						createdBy = Globals.Users.DripFeed;
					else
						createdBy = scanUser;
				} else
					createdBy = userName;
				
				// Grab the doc release date
				Date docCreateDate = this.rsBatchItems.getDateValueByName
				 (UCMFieldNames.DocInDate);
				
				// Resolve the Organisation ID from metadata values
				InstructionDataFieldValue orgIdValue = 
				 orgIdTranslator.getInstructionFieldValue
				  (this.rsBatchItems, null, facade);
				
				if (orgIdValue.isEmpty()) {
					// Unable to infer Organisation/Client data from this document.
					
					// Currently gets a free pass if the document's Instruction Type
					// has Organisation ID as an optional field.
					InstructionType instrType = getInstructionType();
					
					// If this is a MULTIDOC or similar, there won't be any bound
					// Instruction Type so we can safely skip the Client Number check.
					if (instrType != null) {
						// Fetch the Applicable Instruction Data for the Instruction 
						// Type and Organisation ID data field. All Instruction Types 
						// should have this set.
						ApplicableInstructionData applInstrOrgId = 
						 instrType.getApplicableInstructionData(
						 InstructionData.getCache().getCachedInstance
						 (InstructionData.Fields.ORGANISATION_ID)
						);
						
						if (applInstrOrgId == null) {
							String msg = "Unable to resolve whether Organisation/Client " +
							 "data is optional for " + instrType.getName();
							
							Log.error(msg);
							throw new BundleTranslateException
							 (msg, FailReason.UNKNOWN_CLIENT, docName);
						}
						
						if (applInstrOrgId.isOptional()) {
							Log.info("Unable to resolve Organisation/Client " +
							 "for document " + docName + ", data is optional though so " +
							 "we'll let it pass.");
						
						} else {
							String msg = "Unable to resolve Organisation/Client " +
							 "for document " + docName + ". Ensure the Client No. and " +
							 "Company fields are filled in and try again.";
							
							Log.error(msg);
							throw new BundleTranslateException
							 (msg, FailReason.UNKNOWN_CLIENT, docName);
						}
					}
				}
				
				Integer orgId = orgIdValue != null ? orgIdValue.getIntValue() : null;
				
				Comm newComm = null;
				
				// See if there is already a Comm entry for this Doc ID.

				if (!Comm.getDataByDocGuid
					(docGuid, facade).isEmpty()) {
					// If a Comm for this doc already exists in the DB, and we aren't in 
					// append mode, throw an error.
					throw new BundleTranslateException
					 ("Document has already been converted", 
					 FailReason.COMM_GEN_FAILURE, docName);
				}
				
				try {
					if (persist) {
						// Add a new Comm entry to DB
						newComm = Comm.add(source, CommType.DOCUMENT, 
						 null, orgId, docCreateDate, createdBy, 
						 docGuid, null, this.commGroup.getCommGroupId(), 
						 this.facade, this.userName);
					} else {
						// Test mode - create a temporary Comm instance and validate
						// it to see if any errors are thrown.
						newComm = new Comm(0, source, CommType.DOCUMENT, 
						 Comm.DEFAULT_COMM_STATUS, null, 
						 orgId, docCreateDate, createdBy, docGuid, 
						 null, this.commGroup.getCommGroupId());
						
						newComm.validate(facade);
					}
					
					newCommCount++;
					
				} catch (Exception e) {
					throw new BundleTranslateException
					 ("Unable to create Communication entry: " + e.getMessage(), 
					 FailReason.COMM_GEN_FAILURE, docName);
				}	
				
				Log.info("Created new Comm: " + newComm.toString());
				this.comms.add(newComm);
			}
			
		} while (this.rsBatchItems.next());

		Log.info("Generated " + newCommCount + " new Communication entries " +
		 "(" + this.comms.size() + " total)");
	}
	
	/** 1. Generate Instruction instances, tied to the previously-created Comm
	 *	   instances.
	 *	   
	 *  @throws DataException 
	 * @throws ServiceException 
	 *  @throws ServiceException 
	 */
	private void generateInstructions() throws DataException, ServiceException {
		
		this.rsBatchItems.first();
		
		Log.info("Generating Instructions");
		
		do {
			String docName 	= rsBatchItems.getStringValueByName(UCMFieldNames.DocName);
			Integer docId		= CCLAUtils.getResultSetIntegerValue
			 (rsBatchItems, UCMFieldNames.DocID); 
			Integer revisionId = 
				CCLAUtils.getResultSetIntegerValue(rsBatchItems,UCMFieldNames.RevisionId);
			
			Log.info("Attemping to convert document with dDocName=" + docName + 
			 ", dID=" + docId + ", revisionId=" + revisionId);
			
			// While running in append mode, check we haven't added the Instruction to 
			// the DB previously.
			if (append) {
				//TODO check if revId & docName is present 
				//if (docName == null || revisionId == null)
				//	throw new ServiceException("docName and revisionId must be present on the binder");
				//String docGuid = CCLAUtils.getDocGuidFromRawValues(docName, revisionId);
				
				
				if (docId == null) {
					String msg = "Unable to convert document, dID is missing from " +
					 "ResultSet";
					 
					Log.error(msg);
					throw new ServiceException(msg);
				}
				
				String docGuid = CCLAUtils.getDocGuidFromDid(docId);
				Instruction existingInstr = getExistingInstructionByDocGuid(docGuid);
		
				if (existingInstr != null) {
					Log.debug("Instruction already exists for Doc ID: " + docId + 
					 " (Instruction ID :" + existingInstr.getInstructionId() + 
					 ". Skipping document");
					continue;
				}
			}
			
			// Determine the base content item for this document first, so it can be
			// matched against one of the previously-generated Communication entries.
			Integer contentDocId = null;
			
			String docType 		= rsBatchItems.getStringValueByName
			 (UCMFieldNames.DocType);
			
			if (docType.equals("ChildDocument")) {
				// Resolve the referenced dID for this child document (i.e. the UCM doc
				// where the ChildDocument's content resides)
				String parentDocName = rsBatchItems.getStringValueByName
				 (UCMFieldNames.PdfDocName);
				
				if (StringUtils.stringIsBlank(parentDocName))
					parentDocName = rsBatchItems.getStringValueByName
					 (UCMFieldNames.ParentDocName);
				
				contentDocId = docIdMap.get(parentDocName);
				
				if (contentDocId == null) {
					String msg = "Unable to find PDF document for bundle item: " +
					 docName + ", parent item " + parentDocName + " not in bundle";
					
					Log.error(msg);
					throw new BundleTranslateException
					 (msg, FailReason.MISSING_PARENT_DOC, docName);
				}
			} else
				contentDocId = docId;
			
			// Check if we already have a Comm instance tied to the document ID
			Comm thisComm = null;
			String contentDocGuid = CCLAUtils.getDocGuidFromDid(contentDocId);
			
			Log.debug("Searching for Comm with docGuid: "+contentDocGuid);
			
			
			for (Comm comm : comms) {
				if (comm.getDocGuid().equals(contentDocGuid))
					thisComm = comm;
			}
			
			if (thisComm == null) {
				String msg = "No matching Communication entry for document " + 
				 docName;
				
				Log.error(msg);
				throw new BundleTranslateException
				 (msg, FailReason.COMM_NOT_FOUND_FOR_INSTRUCTION, docName);
			}
			
			if (thisComm.getOrganisationId() == null) {
				// This Comm didn't have an Organisation set previously - if we can 
				// extract one from the ChildDocument metadata, apply this to the Comm 
				// instead.
				
				// Resolve the Organisation ID from metadata values
				InstructionDataFieldValue orgIdValue = 
				 orgIdTranslator.getInstructionFieldValue
				  (this.rsBatchItems, null, facade);
				
				if (!orgIdValue.isEmpty()) {
					int orgId = orgIdValue.getIntValue();
					
					thisComm.setOrganisationId(orgId);
					
					if (persist) {
						Log.debug("Applying Organisation ID " + orgId + " to existing "+
						 "Comm  ID " + thisComm.getCommId());
						thisComm.persist(facade, Globals.Users.System);
					}
				}
			}

			// Generate a new Instruction instance. May return null if the passed
			// UCM document is not a true Instruction (e.g. MultiDoc types)
			Instruction thisInstruction = 
			 generateInstruction(thisComm, docName, docId);
			
			if (thisInstruction == null) {
				if (persist) {
					// Maybe this was a MultiDoc doc class (e.g. MULTIDOC). Update the
					// UCM document xStatus value to 'Archived' if this is the case.
					String docClass = rsBatchItems.getStringValueByName
					 (UCMFieldNames.DocClass);
					
					// TODO: externalise. Not sure where at the moment...
					if (docClass.equals("MULTIDOC")) {
						Log.debug("Found a MultiDoc document. " +
						 "Setting status to Archived");
						InstructionUtils.setUCMDocStatus(docId, 
						 InstructionStatus.ARCHIVED.getInstructionStatusName(), true);
					}
				}
				continue;
			}
			
			// Update maps
			if (!append)
				instructions.add(thisInstruction);
			
			newInstructions.add(thisInstruction);
			docInstructionMap.put(docId, thisInstruction);
			
			if (commInstructionMap.containsKey(thisComm)) {
				Vector<Instruction> instrs = commInstructionMap.get(thisComm);
				instrs.add(thisInstruction);
			} else {
				Vector<Instruction> instrs = new Vector<Instruction>();
				instrs.add(thisInstruction);
				
				commInstructionMap.put(thisComm, instrs);
			}
			
			// Generate Instruction Data field values and add to mapping
			try {
				Vector<InstructionDataApplied> thisInstructionData = 
				 InstructionUtils.generateInstructionData
				 (thisInstruction, rsBatchItems, facade);
				
				instructionFieldValues.put(thisInstruction, thisInstructionData);
			} catch (Exception e) {
				Log.error(e.getMessage(), e);
				
				throw new BundleTranslateException(e.getMessage(), 
				 FailReason.INSTRUCTION_DATA_GEN_FAILURE, docName);
			}

		} while (rsBatchItems.next());
		
		Log.debug("Generated " + newInstructions.size() + " Instruction records " +
		 "(" + instructions.size() + " total)");
		
		CommUtils.debugCommInstructionMap(commInstructionMap);
		
		// Add all newly-generated instructions to DB if running in persist mode,
		// just validate them otherwise.
		for (Instruction instruction : newInstructions) {
			try {	
				if (persist) {
					// Add new Instruction instances to the DB
					Instruction.add(instruction, userName, facade);
				} else {
					// Just validate them instead.
					instruction.validate(facade);
				}
			
			} catch (Exception e) {
				String msg = "Failed to generate Instruction for document " + 
				docNameMap.get(instruction.getInstructionDocGuid()) + ": " + e.getMessage();
				
				Log.error(msg, e);
				
				throw new BundleTranslateException
				 (msg, FailReason.INSTRUCTION_GEN_FAILURE, 
				 docGuidMap.get(instruction.getInstructionDocGuid()));
			}
		}
		
		if (persist) {
			// Add InstructionDataApplied instances to the DB
			for (Vector<InstructionDataApplied> instrDataApplied 
				 : 
				instructionFieldValues.values()) {
				
				InstructionDataApplied.addOrUpdateAll
				 (instrDataApplied, facade, userName);
			}
		}
	}
	
	/** Generates a single Instruction instance, linked to the given Comm.
	 *  
	 *  Uses the current row of rsBatchItems to harvest document metadata.
	 *  
	 *  docName and docID are passed out of convenience, they could easily be resolved
	 *  by pulling the values from rsBatchItems.
	 *  
	 * @param comm
	 * @param docName
	 * @param docId
	 * @return
	 * @throws DataException 
	 * @throws DataException
	 */
	public Instruction generateInstruction(Comm comm, String docName, int docId) 
	 throws BundleTranslateException, DataException {
		
		// Look up the associated Instruction Type from the name cache.
		InstructionType instrType = getInstructionType();
		
		if (instrType == null) {
			Log.debug("No bound Instruction Type for Document Class: " + 
			 rsBatchItems.getStringValueByName(UCMFieldNames.DocClass) + 
			 ". Ignoring and carrying on");
			
			return null;
		}
		
		// Check if this document has a dependant dDocName set.
		String dependantDocName = rsBatchItems.getStringValueByName
		 (UCMFieldNames.DependantDocName);
		
		if (!StringUtils.stringIsBlank(dependantDocName)) {
			Integer dependantDocId = docIdMap.get(dependantDocName);
			
			if (dependantDocId == null) {
				resolveExternalDependantDoc(docId, dependantDocName);
			} else {
				dependantDocIdMap.put(docId, dependantDocId);
			}
		}
	
		String docStatus = rsBatchItems.getStringValueByName(UCMFieldNames.Status);
		
		InstructionStatus initialStatus = null;
		
		if (docStatus.equals("Duplicate")) {
			Log.debug("Found a duplicate document: " + docName);
			initialStatus = InstructionStatus.DUPLICATE;
		}
		
		String docGuid = CCLAUtils.getDocGuidFromDid(docId);
		
		// Create new instruction, but don't add it to the DB yet.
		Instruction thisInstruction = new Instruction
		 (comm.getCommId(), instrType, initialStatus, docGuid, persist, userName, facade);
		
		// Copy Original Process Date from source document
		thisInstruction.setOriginalProcessDate
		 (rsBatchItems.getDateValueByName(UCMFieldNames.OriginalProcessDate));
		
		// Copy Process Date from source document
		thisInstruction.setProcessDate
		 (rsBatchItems.getDateValueByName(UCMFieldNames.ProcessDate));
		
		try {
			thisInstruction.validate(facade);
		} catch (DataException e) {
			throw new BundleTranslateException("Failed to generate Instruction: " 
			 + e.getMessage(), FailReason.INSTRUCTION_GEN_FAILURE, docName);
		}
		
		return thisInstruction;
	}
	
	/** Convert dependant dID values to their equivalent Instruction IDs, and
		update any affected instructions.
		
		This has to be done AFTER the new instructions are added to the database, to
		ensure the foreign key reference on DEPENDANT_INSTRUCTION_ID column holds.
		
	    @throws DataException 
	 */
	private void applyInstructionDependencies() throws DataException {

		for (Integer docId : dependantDocIdMap.keySet()) {
			Instruction instr		= docInstructionMap.get(docId);
			
			// Resolve depdendant instruction
			Integer depDocId 		= dependantDocIdMap.get(docId);			
			Instruction depInstr 	= docInstructionMap.get(depDocId);
			
			if (depInstr == null) {
				// No instruction that matches the dependant doc ID!
				// Output a warning here and don't set the dependant instruction ID.
				// This fixes issues with older bundles where items have a dependant
				// dDocName that (incorrectly) references a MULTIDOC etc.
				Log.warn("Unable to resolve dependant instruction for doc ID: " 
				 + depDocId + ". Skipping dependency assignment");
			} else {
				// Update original instruction
				instr.setDependentInstructionId(depInstr.getInstructionId());
				Log.debug("Set dependent Instruction ID to " + depInstr.getInstructionId() +
				 " for Instruction: " + instr.getInstructionId());
				
				instr.persist(facade, userName);
			}
		}
	}
	
	/** Used to resolve an external dependant doc ID.
	 * 
	 * @param docId
	 * @param dependantDocName
	 * @throws DataException
	 */
	private void resolveExternalDependantDoc(Integer docId, String dependantDocName) 
	 throws BundleTranslateException, DataException {
		Integer dependantDocId = null;
		String dependantDocGuid = null;
		
		// Dependant Document reference is not inside this bundle.
		Log.debug("Dependant docName " + dependantDocName + " not found in " +
		 "current bundle.");
		
		try {
			LWDocument depDoc = new LWDocument(dependantDocName, true);
			dependantDocId = Integer.parseInt(depDoc.getId());
			dependantDocGuid = CCLAUtils.getDocGuidFromLwd(depDoc);
		} catch (Exception e) {
			String msg = "Failed to instantiate dependant document" 
			 + dependantDocName;
			
			Log.error(msg, e);
			throw new DataException(msg, e);
		}
			
		Log.debug("Searching for associated Instruction for dependant " +
		 "document");
		
		Instruction externalInstr = 
		 Instruction.getByDocGuid(dependantDocGuid, facade);
		
		if (externalInstr == null) {
			String msg = "Unable to find corresponding Instruction for " +
			 "dependant UCM Document ID: " + dependantDocId;
			
			Log.error(msg);
			throw new BundleTranslateException
			 (msg, FailReason.MISSING_DEPENDANT_INSTRUCTION, getDocNameFromId(docId));
		}
		
		dependantDocIdMap.put(docId, dependantDocId);
		// Add a mapping to the external Instruction
		docInstructionMap.put(dependantDocId, externalInstr);
	}
	
	private String getDocNameFromId(Integer docId) {
		for (Map.Entry<String, Integer> entry : docIdMap.entrySet()) {
			if (entry.getValue().equals(docId)) {
				return entry.getKey();
			}
		}
		
		return null;
	}

	public void setPersist(boolean persist) {
		this.persist = persist;
	}

	public boolean isPersist() {
		return persist;
	}
	
	/** Checks if a Comm was previously generated. Used when translating bundles in
	 *  'append' mode, where some Comms may already be in the DB. These existing Comms
	 *  are preloaded into the comm Vector before translation begins.
	 *  
	 * @param comm
	 * @return
	 */
	private Comm getExistingCommByDocGuid(String docGuid) {
		for (Comm existingComm : this.comms) {
			if (existingComm.getDocGuid().equals(docGuid))
				return existingComm;
		}
		
		return null;
	}
	
	/** Checks if an Instruction was previously generated. Used when translating bundles
	 *  in 'append' mode, where some Instructions may already be in the DB. These 
	 *  existing Instructions are preloaded into the instructions Vector before 
	 *  translation begins.
	 *  
	 * @param comm
	 * @return
	 */
	private Instruction getExistingInstructionByDocGuid(String instructionDocGuid) {
		for (Instruction existingInstr : this.instructions) {
			if (existingInstr.getInstructionDocGuid() != null 
				&&
				existingInstr.getInstructionDocGuid().equals(instructionDocGuid))
				return existingInstr;
		}
		
		return null;
	}
	
	/** Infers the Instruction Type from the current row of the rsBatchItems ResultSet.
	 *  
	 *  Throws an error if the xDocumentClass field in the ResultSet is blank.
	 *  
	 * @return
	 * @throws DataException
	 * @throws BundleTranslateException 
	 */
	private InstructionType getInstructionType() 
	 throws DataException, BundleTranslateException {
		// Look up the associated Instruction Type from the name cache.
		String docName			= rsBatchItems.getStringValueByName
		 (UCMFieldNames.DocName);
		String instrTypeName 	= rsBatchItems.getStringValueByName
		 (UCMFieldNames.DocClass);
		
		if (!StringUtils.stringIsBlank(instrTypeName)) {
			return InstructionType.getNameCache().getCachedInstance(instrTypeName);
		} else {
			String msg = "Document " + docName + " did not have a Document" +
			 " Class set.";
			
			Log.error(msg);
			throw new BundleTranslateException
			 (msg, FailReason.UNKNOWN_INSTRUCTION_TYPE, docName);
		}
	}
	
	public Vector<Instruction> getNewInstructions() {
		return newInstructions;
	}
}
