package com.ecs.ucm.ccla.commproc;

import intradoc.common.ServiceException;
import intradoc.data.DataException;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import com.ecs.stellent.auditlog.AuditUtils;
import com.ecs.stellent.spp.data.QueryJobData;
import com.ecs.stellent.spp.fundprocessdetails.FundProcessDetailsManager;
import com.ecs.ucm.ccla.Globals;
import com.ecs.ucm.ccla.data.DataType;
import com.ecs.ucm.ccla.data.Interaction;
import com.ecs.ucm.ccla.data.comm.Comm;
import com.ecs.ucm.ccla.data.comm.CommGroup;
import com.ecs.ucm.ccla.data.comm.CommSource;
import com.ecs.ucm.ccla.data.comm.CommStatus;
import com.ecs.ucm.ccla.data.comm.CommType;
import com.ecs.ucm.ccla.data.instruction.ApplicableInstructionData;
import com.ecs.ucm.ccla.data.instruction.Instruction;
import com.ecs.ucm.ccla.data.instruction.InstructionData;
import com.ecs.ucm.ccla.data.instruction.InstructionDataApplied;
import com.ecs.ucm.ccla.data.instruction.InstructionDataFieldValue;
import com.ecs.ucm.ccla.data.instruction.InstructionStatus;
import com.ecs.ucm.ccla.data.instruction.InstructionType;

import com.ecs.ucm.ccla.utils.DateUtils;
import com.ecs.ucm.ccla.utils.PriorityCalculationUtils;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

public class InteractionTranslator {

	private boolean persist;
	private int interactionId;
	private FWFacade facade;
	private String userName;
	private QueryJobData queryJobData = null;
	
	public InteractionTranslator
	 (int interactionId, QueryJobData queryJobData, boolean persist, 
	 FWFacade facade, String userName) throws DataException, ServiceException {
		this.interactionId = interactionId;
		this.persist = persist;
		this.facade = facade;
		this.userName = userName;
		this.queryJobData = queryJobData;
	}
	
	
	// Translate an interaction to a single instruction.
	public Instruction translate() throws DataException, ServiceException 
	{
		Log.debug("=====================================================");
		Log.debug("Converting Interaction ID " + interactionId);
		Log.debug("Persist conversion? " + persist);

		Interaction interaction = Interaction.get(interactionId, facade);
		
		if (interaction==null) {
			String msg = "Cannot find Interaction with id: "+interactionId;
			
			Log.error(msg);
			throw new ServiceException(msg);
		}
			
		if (!(interaction.isQuery() || interaction.isBreach() || 
				interaction.isComplaint() || interaction.isError())) {
			String msg = "Interaction " + interactionId + " is not a instruction type."+
			 " No Instruction can be created";
			
			Log.error(msg);
			throw new ServiceException(msg);			
		}
		
		if (queryJobData==null)
			throw new ServiceException("Cannot create an instruction, no QueryJobData");
		
		CommGroup commGroup = null;
		Comm comm = null;
		Instruction instr = null;
		
		try {		
			// First we'll need to create a new Group ID and add a new COMM_GROUP entry
			// to the database.
			
			if (persist) {
				commGroup = CommGroup.add(null, null, facade, userName);
				
				// Assign an SPP Batch ID up-front, we know these guys will end up in
				// SPP sooner or later
				CommUtils.prepareCommGroupForSpp(commGroup, facade);
				
				// Apply the SPP Batch Ref to the interaction
				interaction.setJobId(commGroup.getSppBatchRef().toString());
				interaction.persist(facade, Globals.Users.System);
			} else {
				commGroup = new CommGroup(); // create temp CommGroup instead
			}

			if (persist) {
				comm = Comm.add(getCommSource(interaction), CommType.INTERACTION, 
				 interaction.getPersonId(), interaction.getOrganisationId(), 
				 new Date(), userName, null, interactionId, 
				 commGroup.getCommGroupId(), facade, userName);
				
				Log.debug("Generated new Comm instance for Interaction instruction, ID="
				  +comm.getCommId());
				
			} else {
				comm = new Comm(0, getCommSource(interaction), CommType.INTERACTION, CommStatus.READY, 
						interaction.getPersonId(), interaction.getOrganisationId(), DateUtils.getNow(), 
						userName, null, interactionId, commGroup.getCommGroupId());
			}
			
			instr = generateInstruction(interaction, comm, facade);

			//Generate Instruction Data	
			Vector<InstructionDataApplied> instructionDataVec = 
			 generateInstructionData(instr, interaction, queryJobData, facade);	

			//Set the priority for the instruction.
			instr.setDataApplied(instructionDataVec);
			
			//PriorityCalculationUtils.getPriority(instr);		
			instr.setPriority(Instruction.DEFAULT_PRIORITY);
		
			if (persist) {
				// Ensure Comm exists before continuing.
				Comm testComm = Comm.get(comm.getCommId(), facade);
				
				if (testComm == null) {
					Log.warn("Couldn't find Comm with ID: " + comm.getCommId());
					//throw new ServiceException("Unable to add Instruction: " +
					// "Comm with ID " + comm.getCommId() + " not found");
				}
				
				Instruction.add(instr, userName, facade);
			}
			
			if (persist) {
				InstructionDataApplied.addOrUpdateAll
				 (instructionDataVec, facade, userName);
				
			} else {
				instr.setDataApplied(instructionDataVec);
			}
				
			// Add audit log against the interaction, indicating a new instruction was
			// generated.
			interaction.addActivity(instr.getType().getName(), 
			 instr.getType().getName() + 
			 " added to Instruction Register with SPP batch number: " + 
			 commGroup.getSppBatchRef(), userName, facade);
			
		} catch (Exception e) {
			throw new ServiceException(e.getMessage(), e);
		}
		return instr;
	}

	public void setPersist(boolean persist) {
		this.persist = persist;
	}

	public boolean isPersist() {
		return persist;
	}
	
	private Vector<InstructionDataApplied> generateInstructionData
	(Instruction instr, Interaction interaction, QueryJobData queryJobData, FWFacade facade) 
	throws DataException, ServiceException 
	{
		InstructionType instrType = instr.getType();
		
		Vector<ApplicableInstructionData> applicableFields = 
		 instrType.getApplicableInstructionData();
		
		Vector<InstructionDataApplied> instrDataApplied = 
		 new Vector<InstructionDataApplied>();
		
		
		if (applicableFields == null || applicableFields.isEmpty()) {
			// No applicable instruction data fields for this instruction type.
			String msg = "Instruction type " + instrType.getName() +
			 " has no applicable data fields set.";
			throw new ServiceException(msg);
		}

		InstructionDataFieldValue fieldValue = null;
		String strValue = null;
		Integer intValue = null;
		Date dateValue = null;
		BigDecimal bigDecimalValue = null;
		Boolean boolValue = null;
		DataType dataType = null;
		
		for (ApplicableInstructionData applicableField : applicableFields) 
		{
			fieldValue = null;
			strValue = null;
			intValue = null;
			dateValue = null;
			bigDecimalValue = null;
			boolValue = null;
			dataType = null;
			
			interaction.getAccountId();
			InstructionData dataField = applicableField.getInstructionData();
			
			Log.debug("Translating field: " + dataField.getName());
			
			switch(dataField.getInstructionDataId()) {
			case InstructionData.Fields.SOURCE_ACCOUNT_ID:
				intValue = interaction.getAccountId();
				dataType = DataType.INT;
				break;
			case InstructionData.Fields.ORGANISATION_ID:
				intValue = interaction.getOrganisationId();
				dataType = DataType.INT;
				break;
			case InstructionData.Fields.QUERY_CAUSE_ID:
				intValue = queryJobData.causeId;
				dataType = DataType.INT;
				break;
			case InstructionData.Fields.QUERY_SUB_CAUSE_ID:
				intValue = queryJobData.subCauseId;
				dataType = DataType.INT;
				break;
			case InstructionData.Fields.QUERY_SUMMARY:
				strValue = queryJobData.summary; 
				dataType = DataType.STRING;
				break;
			case InstructionData.Fields.QUERY_HOW_IDENTIFIED:
				strValue = queryJobData.howIdentified; 
				dataType = DataType.STRING;
				break;
			case InstructionData.Fields.QUERY_REQUIRED_ACTION:
				strValue = queryJobData.requiredAction; 
				dataType = DataType.STRING;
				break;
			case InstructionData.Fields.QUERY_DATE_IDENTIFED:
				dateValue = queryJobData.dateIdentified; 
				dataType = DataType.DATE;
				break;
			case InstructionData.Fields.QUERY_DATE_OCCURRED:
				dateValue = queryJobData.dateOccurred; 
				dataType = DataType.DATE;
				break;
			case InstructionData.Fields.QUERY_DATE_RESOLVED:
				dateValue = queryJobData.dateResolved; 
				dataType = DataType.DATE;
				break;
			case InstructionData.Fields.UCM_JOB_ID:
				intValue = queryJobData.ucmJobId; 
				dataType = DataType.INT;
				break;
			case InstructionData.Fields.DOC_DATE:
			case InstructionData.Fields.FORM_ID:
			case InstructionData.Fields.INSTRUCTION_COMMENTS:
			case InstructionData.Fields.WITH_INSTRUCTION:
			case InstructionData.Fields.WORKFLOW_DATE:
			case InstructionData.Fields.SIGNATURES_VALID:
				//these are all document related
				break;
			default:
				//Log.error("Unknown applicable data for interaction, ignoring");
				break;
			}			
		
			if (dataType!=null && 
					(!StringUtils.stringIsBlank(strValue) || 
							intValue!=null || dateValue!=null)) 
			{
				fieldValue = new InstructionDataFieldValue(dataType);
				
				if (dataType.equals(DataType.STRING)) {
					fieldValue.setStringValue(strValue);
				} else if (dataType.equals(DataType.INT)) {
					fieldValue.setIntValue(intValue);
				} else if (dataType.equals(DataType.DATE)) {
					fieldValue.setDateValue(dateValue);
				} else if (dataType.equals(DataType.BOOL)) {
					fieldValue.setBoolValue(boolValue);
				} else if (dataType.equals(DataType.FLOAT)) {
					fieldValue.setBigDecimalValue(bigDecimalValue);
				} 
				
				InstructionDataApplied instrData = new InstructionDataApplied
				(instr.getInstructionId(), applicableField, fieldValue);
			
				instrDataApplied.add(instrData);
			}
		}
		
		Log.debug("Resolved "+instrDataApplied.size()+" Instruction Data fields.");
		return instrDataApplied;
	}
	
	private Instruction generateInstruction
	 (Interaction interaction, Comm comm, FWFacade facade) 
	 throws DataException, ServiceException {
		String instrTypeStr = null;
		//Work out the instruction type
		if (interaction.isQuery()) {
			instrTypeStr = "CLIENTQUERY";
		} else if (interaction.isBreach()) {
			instrTypeStr = "BREACH";
		} else if (interaction.isComplaint()) {
			instrTypeStr = "COMPL";
		} else if (interaction.isError()) {
			instrTypeStr = "ERROR";
		}
		
		if (StringUtils.stringIsBlank(instrTypeStr)) {
			Log.error("Unknown Interaction Instruction");
			throw new ServiceException("Unknown interaction instruction");
		}
		
		InstructionType instrType = 
		 InstructionType.getNameCache().getCachedInstance(instrTypeStr);
		
		if (instrType==null) {
			Log.error("Unknow instructionType "+instrTypeStr);
			throw new ServiceException("Unknow instructionType "+instrTypeStr);
		}
		
		// TM: Commented out the dealing date calculation - this appears to corrupt
		// the transaction block after its called.
		/*
		Date processDate = FundProcessDetailsManager.getDealingDate
		 (null, null, instrTypeStr, 
		 null, false, null, 
		 interaction.getDate(), false, facade);
		*/
		
		// Just use current date as process date.
		Date processDate = new Date();
		
		Instruction instr = new Instruction
		 (comm.getCommId(), instrType, null, null, persist, userName, facade);
		
		instr.setProcessDate(processDate);
		instr.setOriginalProcessDate(processDate);

		return instr;
	}
	
	
	private CommSource getCommSource(Interaction interaction) 
	throws ServiceException 
	{
		if (interaction==null)
			throw new ServiceException("Interaction is null");
		
		String type = interaction.getType();
		
		if (StringUtils.stringIsBlank(type))
			throw new ServiceException("Interaction Type is null for InteractionId:"+interaction.getInteractionId());
			
		type = type.intern();
		if (type=="Incoming call") {
			return CommSource.INCOMING_CALL;
		} else if (type=="Outcoming call") {
			return CommSource.OUTGOING_CALL;		
		} else if (type=="Email") {
			return CommSource.EMAIL;
		} else if (type=="Letter") {
			return CommSource.POST;
		} else if (type=="Note") {
			return CommSource.POST;
		} else if (type=="Close interaction") {
			return CommSource.USER;
		} else {
			//Default type
			return CommSource.POST;
		}
	}
}
