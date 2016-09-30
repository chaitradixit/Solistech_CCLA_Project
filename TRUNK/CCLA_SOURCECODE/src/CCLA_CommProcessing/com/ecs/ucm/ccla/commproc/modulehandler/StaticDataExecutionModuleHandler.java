package com.ecs.ucm.ccla.commproc.modulehandler;

import intradoc.data.DataException;
import intradoc.shared.SharedObjects;

import java.util.Date;
import java.util.Vector;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals;
import com.ecs.ucm.ccla.aurora.AuroraAccountHandler;
import com.ecs.ucm.ccla.aurora.AuroraAccountUtils;
import com.ecs.ucm.ccla.aurora.AuroraClientHandler;
import com.ecs.ucm.ccla.aurora.AuroraClientUtils;
import com.ecs.ucm.ccla.aurora.AuroraCorrespondentHandler;
import com.ecs.ucm.ccla.aurora.compare.AccountFieldSet;
import com.ecs.ucm.ccla.aurora.compare.AuroraFieldSet;
import com.ecs.ucm.ccla.aurora.compare.ClientFieldSet;
import com.ecs.ucm.ccla.aurora.compare.ComparisonUtils;
import com.ecs.ucm.ccla.aurora.compare.CorrespondentFieldSet;
import com.ecs.ucm.ccla.commproc.InstructionLockUtils;
import com.ecs.ucm.ccla.commproc.InstructionUtils;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.Company;
import com.ecs.ucm.ccla.data.DataType;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.SystemConfigVar;
import com.ecs.ucm.ccla.data.instruction.ApplicableInstructionData;
import com.ecs.ucm.ccla.data.instruction.Instruction;
import com.ecs.ucm.ccla.data.instruction.InstructionAudit;
import com.ecs.ucm.ccla.data.instruction.InstructionAuditAction;
import com.ecs.ucm.ccla.data.instruction.InstructionData;
import com.ecs.ucm.ccla.data.instruction.InstructionDataApplied;
import com.ecs.ucm.ccla.data.instruction.InstructionDataFieldValue;
import com.ecs.ucm.ccla.data.instruction.InstructionProcess;
import com.ecs.ucm.ccla.data.instruction.InstructionStatus;
import com.ecs.ucm.ccla.data.instruction.InstructionType;
import com.ecs.ucm.ccla.utils.DateUtils;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

public class StaticDataExecutionModuleHandler extends RoutingModuleHandler {

	/** Whether or not an instruction-based workflow job is triggered when an SDU 
	 *  instruction is rejected/de-authorised by a user.
	 */
	private static boolean TRIGGER_SPP_JOB_ON_EXEC_FAILURE = 
	 SharedObjects.getEnvValueAsBoolean
	 ("CCLA_CommProc_TriggerNotificationJobOnSDUInstructionExecutionFailure", false);
	
	/**
	 * Gets the suspend instruction process unique to this module.
	 * 
	 * @override
	 * @return
	 */
	public InstructionProcess getSuspendInstructionProcess() throws DataException {
		return InstructionProcess.getCache().getCachedInstance
		 (InstructionProcess.ProcessIds.SDU_AURORA_EXECUTION_FAILURE);
	}
	
	
	/** Delay period in seconds added to each call to the eval() method. Used to simulate
	 *  long running times in test environments.
	 */
	private static Integer EVAL_DELAY_PERIOD;
	
	static {
		String delayPeriodStr = SharedObjects.getEnvironmentValue
		 ("CCLA_CP_StaticDataExecutionModuleEvalDelayPeriod");
		
		if (!StringUtils.stringIsBlank(delayPeriodStr))
			EVAL_DELAY_PERIOD = Integer.parseInt(delayPeriodStr);
	}
	

	/**
	 * only accepts instructions marked for static data processing, 
	 */
	protected boolean accept(Instruction instr, FWFacade facade)
	throws DataException 
	{
		if (instr.getType().isStaticDataUpdate()) {
			return true;
		} else 
			return false; //Will apply the skip status.
	}
	
	
	@Override
	protected EvalOutcome eval(Instruction instr, FWFacade facade) 
	throws DataException 
	{
		if (EVAL_DELAY_PERIOD != null) {
			Log.debug("Delaying eval execution for " 
			 + EVAL_DELAY_PERIOD + " seconds");
			try {
				Thread.sleep(EVAL_DELAY_PERIOD * 1000);
			} catch (InterruptedException e) {}
		}
		
        InstructionDataApplied isAuthorisedDataApplied  = 
        	InstructionDataApplied.getDataApplied(
        			instr.getInstructionId(), 
        			InstructionData.Fields.IS_AUTHORISED, 
        			facade);
		
        InstructionDataApplied isExecutedDataApplied  = 
        	InstructionDataApplied.getDataApplied(
        			instr.getInstructionId(), 
        			InstructionData.Fields.IS_EXECUTION_SUCCESS, 
        			facade);
        
        
        if (isAuthorisedDataApplied!=null && 
        		!isAuthorisedDataApplied.getDataFieldValue().isEmpty() && 
        		isAuthorisedDataApplied.getDataFieldValue().getBoolValue().booleanValue()) 
        {
        	//check the execution status to see if it has been a successful execution
        	if (isExecutedDataApplied!=null && 
            		!isExecutedDataApplied.getDataFieldValue().isEmpty() && 
            		isExecutedDataApplied.getDataFieldValue().getBoolValue().booleanValue())
        	{
        		//Already been submit fail.
        		Log.error("Error, instruction has been executed already...setting to fail.");
        		return EvalOutcome.FAIL;
        	} else {
        		switch (instr.getType().getInstructionTypeId()) {
	        		case InstructionType.Ids.CREATE_CLIENT:
	        		case InstructionType.Ids.UPDATE_CLIENT:
	        			return addUpdateClient(instr, facade);
	        		case InstructionType.Ids.CREATE_CORRESPONDENT:
	        		case InstructionType.Ids.UPDATE_CORRESPONDENT:
	        			return addUpdateCorrespondent(instr, facade);
	        		case InstructionType.Ids.CREATE_ACCOUNT:
	        		case InstructionType.Ids.UPDATE_ACCOUNT:
	        			return addUpdateAccount(instr, facade);
	        		default:
	        			Log.error("Unknown instruction type "+instr.getType().getInstructionTypeId()+", instrID:"+instr.getInstructionId());
	        			return EvalOutcome.FAIL;
        		}
        	}
        } else {
        	Log.error("Instruction has not been authorised, suspending instruction");
            return EvalOutcome.SUSPEND;
        }
	}
	
	
	@Override
	protected void applySuspendAction(Instruction instr, FWFacade facade,
			String userName) throws DataException {

		try {
			if (TRIGGER_SPP_JOB_ON_EXEC_FAILURE) {
				InstructionUtils.triggerSppNotificationJob
				 (instr, Globals.Users.System, facade);
			}
		} catch (Exception e) {
			Log.error("Failed to trigger Workflow Notification", e);
			// Don't throw the error, we are suspending the instruction anyway at
			// this point!
			//throw new DataException("Failed to trigger Workflow Notification", e);
		}
		
		super.applySuspendAction(instr, facade, userName);
	}
	
	/** Sets the status back to Pending Static Data Auth.
	 * 
	 */
	@Override
	protected void applyResubmitAction(Instruction instr, FWFacade facade,
			String userName) throws DataException {

		instr.setStatus(InstructionStatus.getCache().getCachedInstance
		 (InstructionStatus.StatusID.PENDING_STATIC_DATA_REAUTHORISATION));
		instr.persist(facade, userName);
		
		InstructionLockUtils.unlockInstruction
		 (instr.getInstructionId(), this.getRoutingModule(), facade, userName);
	}

	/**
	 * Gets the suspend message unique to the module
	 * @return
	 */
	public String getSuspendMessage() {
		return "Failed to execute static data update instruction - check the " +
		 "instruction's Error Message or the Register Log for more details";
	}	
	
	/**
	 * Internal method for creating/updating aurora accounts
	 * 
	 * @param instr
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	private EvalOutcome addUpdateAccount(Instruction instr, FWFacade facade) 
	 throws DataException {
		
		InstructionDataApplied accDataApplied = InstructionDataApplied.getDataApplied(
		 instr.getInstructionId(), 
		 InstructionData.Fields.SOURCE_ACCOUNT_ID, 
		 facade
		);
		
		if (accDataApplied == null || accDataApplied.getDataFieldValue().isEmpty()) {
			// Missing Source Account ID instruction data field.
			Log.error("Cannot create/update Aurora Account, " +
			 "missing Account ID reference on Instruction");
			
			return EvalOutcome.SUSPEND;
		}
		
		Account account = Account.get
		 (accDataApplied.getDataFieldValue().getIntValue(), facade);
		
		if (account == null) {
			Log.error("Cannot create/update Aurora Account, "
			 + "Account ID reference invalid");
			return EvalOutcome.SUSPEND;
		}

		try {
			// Determine the (Aurora) Company this account belongs to
			Vector<Company> companies = account.getAuroraCompanies(facade);
			if (companies.isEmpty()) {
				String errorMsg = "Unable to determine Aurora Company for Account";
				
				updateSDUExecInstruction
				 (instr, false, errorMsg, Globals.Users.System, facade);	
				return EvalOutcome.SUSPEND;
			}
			
			Company company = companies.get(0);
			AuroraAccountHandler handler = new AuroraAccountHandler();
			
			boolean isCreate = false;
			
			if (instr.getType().getInstructionTypeId()
				==InstructionType.Ids.CREATE_ACCOUNT) {
				isCreate = true;
				
				handler.addAuroraEntity(account, company, facade);
			} else {
				// Do a partial update of the Account record.
				Vector<String> fieldGroupNames = null;
			
				// Fetch previously-calculated list of Aurora Field Groups to update
				InstructionDataApplied updatedGroupsVal = 
				 InstructionDataApplied.getDataApplied(
						instr.getInstructionId(), 
						InstructionData.Fields.SDU_FIELD_GROUPS, 
						facade);
				
				if (isRefreshFieldGroupsPreExecution()) {
					Log.debug("Refreshing Field Group update list pre-execution");

					// Rebuild and store the list of updated Aurora Field Groups.
					AccountFieldSet cfs = new AccountFieldSet();
					cfs.calculateGroupAmendmentDates(account, company, facade);

					fieldGroupNames = 
					 refreshInstructionFieldGroups(instr, cfs, facade);
					
					// Perhaps now there is nothing to update - the list may be empty!
					// Suspend the instruction in this case.
					if (fieldGroupNames.isEmpty()) {
						String msg = "Cancelling Aurora update. No field groups appear "+
						 "to have been updated recently. Confirm and apply updates " +
						 "before retrying execution, or cancel the instruction.";
						
						Log.error(msg);
						updateSDUExecInstruction
						 (instr, false, msg, Globals.Users.System, facade);
						return EvalOutcome.SUSPEND;
					}
					
				} else {
					if (updatedGroupsVal != null)
						fieldGroupNames = CCLAUtils.getStringListFromCSV
						 (updatedGroupsVal.getDataFieldValue().getStringValue());
				}
				
				handler.updateAuroraEntity
				 (account, company, fieldGroupNames, facade); 
			}
			
			//add audit:
			addAuroraAuditLog(instr.getInstructionId(), 
				isCreate,  
				instr.getStatus().getInstructionStatusId(),
				Globals.Users.System, facade);
			
			//Aurora Success, update instruction
			updateSDUExecInstruction
			 (instr, true, null, Globals.Users.System, facade);	
			
			return EvalOutcome.PASS;
			
		} catch (Exception se) {
			String errorMsg = se.getMessage();
			Log.error(errorMsg);
			
			// Execution failed, update instruction
			updateSDUExecInstruction
			 (instr, false, errorMsg, Globals.Users.System, facade);
			
			return EvalOutcome.SUSPEND;
		}	
	}
	
	/**
	 * Internal method for creating/updating Aurora correspondents
	 * 
	 * @param instr
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	private EvalOutcome addUpdateCorrespondent(Instruction instr, FWFacade facade) 
	 throws DataException {
		
		InstructionDataApplied compDataApplied = 
			InstructionDataApplied.getDataApplied(
					instr.getInstructionId(), 
					InstructionData.Fields.COMPANY_ID, 
					facade);
		InstructionDataApplied perDataApplied = 
			InstructionDataApplied.getDataApplied(
					instr.getInstructionId(), 
					InstructionData.Fields.PERSON_ID, 
					facade);
		
		if ((perDataApplied!=null && !perDataApplied.getDataFieldValue().isEmpty())
			&& 
			(compDataApplied!=null && !compDataApplied.getDataFieldValue().isEmpty())) {
			// Valid
		} else {
			Log.error("Cannot create/update Aurora Correspondent, missing Company ID or " +
			 " Person ID");
			return EvalOutcome.SUSPEND;
		}
				
		Company company = Company.getCache().getCachedInstance
		 (compDataApplied.getDataFieldValue().getIntValue());
		
		Person person = Person.get(perDataApplied.getDataFieldValue().getIntValue(),
		 true, facade);
		
		if (company==null || person==null) {
			Log.error("Cannot create/update Aurora Correspondent, invalid Company or" +
			 " Person ID");
			return EvalOutcome.SUSPEND;
		}
		
		try {
			AuroraCorrespondentHandler auroraHandler = new AuroraCorrespondentHandler();
			
			boolean isCreate = instr.getType().getInstructionTypeId()
			 == InstructionType.Ids.CREATE_CORRESPONDENT;
			
			if (isCreate) {
				// Add the new Correspondent to Aurora
				auroraHandler.addAuroraEntity(person, company, facade);
				
			} else {
				// Do a partial update of the Correspondent record.
				Vector<String> fieldGroupNames = null;
			
				// Fetch previously-calculated list of Aurora Field Groups to update
				InstructionDataApplied updatedGroupsVal = 
				 InstructionDataApplied.getDataApplied(
						instr.getInstructionId(), 
						InstructionData.Fields.SDU_FIELD_GROUPS, 
						facade);
				
				if (isRefreshFieldGroupsPreExecution()) {
					Log.debug("Refreshing Field Group update list pre-execution");

					// Rebuild and store the list of updated Aurora Field Groups.
					CorrespondentFieldSet cfs = new CorrespondentFieldSet();
					cfs.calculateGroupAmendmentDates(person, company, facade);

					fieldGroupNames = 
					 refreshInstructionFieldGroups(instr, cfs, facade);
					
					// Perhaps now there is nothing to update - the list may be empty!
					// Suspend the instruction in this case.
					if (fieldGroupNames.isEmpty()) {
						String msg = "Cancelling Aurora update. No field groups appear "+
						 "to have been updated recently. Confirm and apply updates " +
						 "before retrying execution, or cancel the instruction.";
						
						Log.error(msg);
						updateSDUExecInstruction
						 (instr, false, msg, Globals.Users.System, facade);
						return EvalOutcome.SUSPEND;
					}
					
				} else {
					if (updatedGroupsVal != null)
						fieldGroupNames = CCLAUtils.getStringListFromCSV
						 (updatedGroupsVal.getDataFieldValue().getStringValue());
				}
				
				auroraHandler.updateAuroraEntity
				 (person, company, fieldGroupNames, facade); 
			}
			
			addAuroraAuditLog(instr.getInstructionId(), 
			 isCreate,  
			 instr.getStatus().getInstructionStatusId(),
			 Globals.Users.System, facade);
			
			//Aurora Success, update instruction
			updateSDUExecInstruction(instr, true, null, Globals.Users.System, facade);	
			
		} catch (Exception se) {
			String errorMsg = se.getMessage();
			Log.error(errorMsg);
			//Aurora Failed, update instruction
			updateSDUExecInstruction(instr, false, errorMsg, Globals.Users.System, facade);
			return EvalOutcome.SUSPEND;
		}
		
		return EvalOutcome.PASS;
	}
	
	/** Refreshes and returns the list of Field Groups that have been updated since
	 *  the SDU instruction's parent instruction was created.
	 *  
	 *  The refreshed list of Field Groups is updated against the instruction data.
	 *  
	 * @param sduInstr
	 * @param fieldSet
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	private Vector<String> refreshInstructionFieldGroups
	 (Instruction sduInstr, AuroraFieldSet<?,?> fieldSet, FWFacade facade) 
	 throws DataException {
		
		// Fetch source instruction to determine the base date.
		Date baseDate = null;
		
		InstructionDataApplied parentInstrId = 
			InstructionDataApplied.getDataApplied(
			sduInstr.getInstructionId(), 
			InstructionData.Fields.PARENT_INSTR_REF, 
			facade);
		
		if (parentInstrId != null) {
			Log.debug("Found parent Instruction ID reference: " +
			parentInstrId.getDataFieldValue().getIntValue()); 
			
			if (parentInstrId.getDataFieldValue().getIntValue() != null) {
				Instruction parentInstr = Instruction.get(
				 parentInstrId.getDataFieldValue().getIntValue(),
				 facade);
				
				baseDate = parentInstr.getDateAdded();
			}
		}
		
		if (baseDate == null)
			Log.warn("Unable to find parent instruction ref. " + 
			 "Using current date to determine amendment limit date");
			baseDate = new Date();
		
		Date limitDate = ComparisonUtils.getAmendmentLimitDate(baseDate);

		Vector<String> fieldGroupNames = 
		 fieldSet.getFilteredGroupNamesByAmendmentDate(limitDate);
		
		// Persist the list against the instruction
		InstructionDataFieldValue val = 
		 new InstructionDataFieldValue(DataType.STRING);
		val.setStringValue
		 (CCLAUtils.getCSVFromStringList(fieldGroupNames, true));
		
		InstructionDataApplied.addOrUpdate
		 (sduInstr, InstructionData.getCache().getCachedInstance
		 (InstructionData.Fields.SDU_FIELD_GROUPS), 
		 val, facade, Globals.Users.System);
		
		return fieldGroupNames;
	}
	
	/** Whether or not the list of Aurora Field Groups to be updated is refreshed just
	 *  before an SDU Update instruction is executed.
	 *  
	 *  If set, this will ensure that any extra static data updates carried out after the
	 *  Static Data Update instruction was created will be considered when building the
	 *  list of Field Groups.
	 *  
	 *  If not set, just refer to the list of Field Groups captured and stored
	 *  in the Instruction Data during instruction creation (SDU_FIELD_GROUPS data field)
	 *  
	 * @return
	 * @throws DataException 
	 */
	public boolean isRefreshFieldGroupsPreExecution() throws DataException {
		
		SystemConfigVar refreshFieldGroupsVar = SystemConfigVar.getCache()
		 .getCachedInstance("SDU_RefreshFieldGroupsPreExecution");
		
		if (refreshFieldGroupsVar == null)
			return false;
		else 
			return refreshFieldGroupsVar.getBoolValue();
	}

	/**
	 * Internal method for creating/updating Aurora Client
	 * 
	 * @param instr
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	private EvalOutcome addUpdateClient
	 (Instruction instr, FWFacade facade) throws DataException {
		
		InstructionDataApplied compDataApplied = 
			InstructionDataApplied.getDataApplied(
			instr.getInstructionId(), 
			InstructionData.Fields.COMPANY_ID, 
			facade);
		InstructionDataApplied orgDataApplied = 
			InstructionDataApplied.getDataApplied(
			instr.getInstructionId(), 
			InstructionData.Fields.ORGANISATION_ID, 
			facade);
	
	
		if ( (orgDataApplied!=null && !orgDataApplied.getDataFieldValue().isEmpty())
			&& 
			(compDataApplied!=null && !compDataApplied.getDataFieldValue().isEmpty()) ) {
			// OK
		} else {
			Log.error("Cannot create/update Aurora Client, missing Organisation/Company "+
			 " on SDU instruction");
			
			return EvalOutcome.SUSPEND;
		}	
			
		Company company = Company.getCache().getCachedInstance
		 (compDataApplied.getDataFieldValue().getIntValue());
		Entity org = Entity.get
		 (orgDataApplied.getDataFieldValue().getIntValue(), true, facade);
		
		if (company==null || org==null) {
			Log.error("Cannot create/update Aurora Client, Organisation/Company " +
			 "references invalid");
			return EvalOutcome.SUSPEND;
		}
		
		try {
			AuroraClientHandler auroraHandler = new AuroraClientHandler();
			
			boolean isCreate = instr.getType().getInstructionTypeId()
			 == InstructionType.Ids.CREATE_CLIENT;
			
			if (isCreate) {
				// Create a new Aurora Client record.
				auroraHandler.addAuroraEntity(org, company, facade);
			} else { 
				// Do a partial update of the Client record.
				Vector<String> fieldGroupNames = null;
			
				// Fetch previously-calculated list of Aurora Field Groups to update
				InstructionDataApplied updatedGroupsVal = 
				 InstructionDataApplied.getDataApplied(
						instr.getInstructionId(), 
						InstructionData.Fields.SDU_FIELD_GROUPS, 
						facade);
				
				if (isRefreshFieldGroupsPreExecution()) {
					Log.debug("Refreshing Field Group update list pre-execution");

					// Rebuild and store the list of updated Aurora Field Groups.
					ClientFieldSet cfs = new ClientFieldSet();
					cfs.calculateGroupAmendmentDates(org, company, facade);

					fieldGroupNames = 
					 refreshInstructionFieldGroups(instr, cfs, facade);
					
					// Perhaps now there is nothing to update - the list may be empty!
					// Suspend the instruction in this case.
					if (fieldGroupNames.isEmpty()) {
						String msg = "Cancelling Aurora update. No field groups appear "+
						 "to have been updated recently. Confirm and apply updates " +
						 "before retrying execution, or cancel the instruction.";
						
						Log.error(msg);
						updateSDUExecInstruction
						 (instr, false, msg, Globals.Users.System, facade);
						return EvalOutcome.SUSPEND;
					}
					
				} else {
					if (updatedGroupsVal != null)
						fieldGroupNames = CCLAUtils.getStringListFromCSV
						 (updatedGroupsVal.getDataFieldValue().getStringValue());
				}
				
				auroraHandler.updateAuroraEntity
				 (org, company, fieldGroupNames, facade);      
			}
			
			//add audit
			addAuroraAuditLog(instr.getInstructionId(), 
			 isCreate,  
			 instr.getStatus().getInstructionStatusId(),
			 Globals.Users.System, facade);
			
			//Aurora Success, update instruction
			updateSDUExecInstruction(instr, true, null, Globals.Users.System, facade);
			
		} catch (Exception se) {
			String errorMsg = se.getMessage();
			Log.error(errorMsg);
			//Aurora Failed, update instruction
			updateSDUExecInstruction(instr, false, errorMsg, Globals.Users.System, facade);
			return EvalOutcome.SUSPEND;
		}
		
		return EvalOutcome.PASS;
	}
	
	/**
	 * Update the instruction with the outcome of the Static Data execution attempt.
	 * 
	 * Stores whether or not the execution completed successfully, the execution date
	 * and an error message explaining the failure, if applicable.
	 * 
	 * @param instr
	 * @param isSucessful
	 * @param errorMsg
	 * @param userName
	 * @param facade
	 * @throws DataException
	 */
	private static void updateSDUExecInstruction
	 (Instruction instr, boolean isSucessful, String errorMsg, 
	 String userName, FWFacade facade) throws DataException {
		
		if (instr!=null) {
		
			int instrId = instr.getInstructionId();
			int instrTypeId = instr.getType().getInstructionTypeId();
			ApplicableInstructionData appData = null;
			InstructionDataApplied dataApplied = null;
			InstructionDataFieldValue fieldValue = null;
			InstructionData instrData = null;
			
			appData = 
				ApplicableInstructionData.getApplicableInstructionDataById(
						instrTypeId, 
						InstructionData.Fields.IS_EXECUTION_SUCCESS);
			
			if (appData!=null) {
				dataApplied = InstructionDataApplied.getDataApplied(
						instrId, 
						InstructionData.Fields.IS_EXECUTION_SUCCESS, 
						facade);
				
				if (dataApplied!=null) {
					dataApplied.getDataFieldValue().setBoolValue(isSucessful);
				} else {
					instrData = InstructionData.getCache().getCachedInstance(InstructionData.Fields.IS_EXECUTION_SUCCESS);
					fieldValue = new InstructionDataFieldValue(instrData.getDataType());
					fieldValue.setBoolValue(isSucessful);
					dataApplied = new InstructionDataApplied(instr.getInstructionId(), appData, fieldValue);
				}
				InstructionDataApplied.addOrUpdate(dataApplied, facade, userName);
			}
			
			
			appData = 
				ApplicableInstructionData.getApplicableInstructionDataById(
						instrTypeId, 
						InstructionData.Fields.ERROR_MESSAGE);
			
			if (appData!=null) {
				dataApplied = InstructionDataApplied.getDataApplied(
						instrId, 
						InstructionData.Fields.ERROR_MESSAGE, 
						facade);
			}

			if (dataApplied!=null) {
				fieldValue = dataApplied.getDataFieldValue();
				fieldValue.setStringValue(errorMsg);
				dataApplied.setDataFieldValue(fieldValue);
			} else {
				instrData = InstructionData.getCache().getCachedInstance(InstructionData.Fields.ERROR_MESSAGE);
				fieldValue = new InstructionDataFieldValue(instrData.getDataType());
				fieldValue.setStringValue(errorMsg);
				dataApplied = new InstructionDataApplied(instr.getInstructionId(), appData, fieldValue);
			}
			InstructionDataApplied.addOrUpdate(dataApplied, facade, userName);
		}
		
		// Add/set Execution Date field
		InstructionDataFieldValue execDate = new InstructionDataFieldValue
		 (DataType.DATE);
		
		if (isSucessful)
			execDate.setDateValue(new Date());
		
		InstructionDataApplied.addOrUpdate
		 (instr, InstructionData.getCache().getCachedInstance
		  (InstructionData.Fields.EXECUTION_DATE), execDate, facade, userName);
		
		instr.setLastUpdated(DateUtils.getNow());
		instr.setLastUpdatedBy(userName);
		instr.persist(facade, userName);
		
		//Audit to update instruction.
		InstructionAudit instructionAudit = 
			new InstructionAudit(null, instr.getInstructionId(), 
			 InstructionAuditAction.ACTION_STATUS_UPDATE, null, 
			 instr.getStatus().getInstructionStatusId(), 
			 null, null, 0, userName);
		
		InstructionAudit.add(instructionAudit, facade);
		
	}
	
	/** Adds an Instruction Audit enty indicating the outcome of the SDU execution.
	 *  
	 * @param instrId
	 * @param isCreate
	 * @param instrStatusId
	 * @param userName
	 * @param facade
	 * @throws DataException
	 */
	private void addAuroraAuditLog(int instrId, boolean isCreate, int instrStatusId, 
	 String userName, FWFacade facade) throws DataException{
		
		//Audit to update instruction.
		InstructionAudit instructionAudit = new InstructionAudit(
			null, instrId, 
			isCreate ?
			InstructionAuditAction.ACTION_SDU_AURORA_CREATE
			: 
			InstructionAuditAction.ACTION_SDU_AURORA_UPDATE,
			null,instrStatusId,null, null, 0, userName
		);
		
		InstructionAudit.add(instructionAudit, facade);
	}
}
