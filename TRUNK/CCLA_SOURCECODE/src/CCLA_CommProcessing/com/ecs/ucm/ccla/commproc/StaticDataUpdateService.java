package com.ecs.ucm.ccla.commproc;

import java.rmi.RemoteException;
import java.util.Vector;

import com.aurora.webservice.Client;
import com.aurora.webservice.Correspondent;
import com.ecs.stellent.spp.Variable;
import com.ecs.stellent.spp.data.SPPJobProfile;
import com.ecs.stellent.spp.data.WorkflowJobResponse;
import com.ecs.stellent.spp.service.SppIntegrationUtils;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals;
import com.ecs.ucm.ccla.Globals.Users;
import com.ecs.ucm.ccla.aurora.AuroraAccountUtils;
import com.ecs.ucm.ccla.aurora.AuroraClientUtils;
import com.ecs.ucm.ccla.aurora.AuroraCorrespondentHandler;
import com.ecs.ucm.ccla.aurora.AuroraCorrespondentUtils;
import com.ecs.ucm.ccla.aurora.AuroraWebServiceHandler;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.AuroraClient;
import com.ecs.ucm.ccla.data.AuroraCorrespondent;
import com.ecs.ucm.ccla.data.Company;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.SharedCols;
import com.ecs.ucm.ccla.data.instruction.Instruction;
import com.ecs.ucm.ccla.data.instruction.InstructionAction;
import com.ecs.ucm.ccla.data.instruction.InstructionAudit;
import com.ecs.ucm.ccla.data.instruction.InstructionAuditAction;
import com.ecs.ucm.ccla.data.instruction.InstructionData;
import com.ecs.ucm.ccla.data.instruction.InstructionDataApplied;
import com.ecs.ucm.ccla.data.instruction.InstructionProcessApplied;
import com.ecs.ucm.ccla.data.instruction.InstructionStatus;
import com.ecs.ucm.ccla.utils.DateUtils;
import com.ecs.ucm.ccla.utils.ObjectUtils;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ServiceException;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.server.Service;
import intradoc.shared.SharedObjects;

public class StaticDataUpdateService extends Service {

	/** Whether or not an instruction-based workflow job is triggered when an SDU 
	 *  instruction is rejected/de-authorised by a user.
	 */
	private static boolean TRIGGER_SPP_JOB_ON_REJECT = 
	 SharedObjects.getEnvValueAsBoolean
	 ("CCLA_CommProc_TriggerNotificationJobOnSDUInstructionRejection", false);
	
	
	public enum SDUUpdateType {
		AUTHORISE_OR_RETRY,
		REJECT,
		TERMINATE,
		RESUBMIT
	}
	
	/**
	 * Service method exposed to SPP via AuthoriseSDUInstruction web service.
	 * 
	 * Takes 3 lists of Instruction IDs and performs various tasks on each, depending
	 * on the instruction's current state.
	 * 
	 * -AUTHORISED_INSTR_IDS: these instructions will be marked as Authorised and
	 * 						  therefore ready for SDU execution. If they are currently
	 * 						  suspended, the retry action will be called.
	 * 
	 * -FAILED_INSTR_IDS:	  these instructions will be marked as Unathorised/Rejected.
	 * 
	 * -TERMINATED_INSTR_IDS: these instructions will be marked as Terminated and no
	 * 						  longer supplied to SPP via the GetPendingSDUInstructions
	 *  					  web service.
	 *  
	 *  -RESUBMIT_INSTR_IDS:  these instructions will be marked as Pending 
	 *  				  	  Re-authorisation. This action will be applied on SDU
	 *  					  instructions in one of the 2 failed states (Rejected or 
	 *  					  Failed Execution). The IS_AUTHORISED flag value is wiped
	 *  					  off the instruction and their statuses are updated.
	 * 
	 *  Also requires the following parameter set:
	 *  
	 *  -AUTHORISED_USER:	  user who called the service
	 *  
	 *  If the Failed/Terminated lists are non-empty, you must also specify:
	 *  
	 *  -REJECT_REASON:		  reason for rejecting/terminating instructions.
	 *  
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void updateSDUInstruction() throws ServiceException {
		
		String authorisedInstrIdsStr 	= m_binder.getLocal("AUTHORISED_INSTR_IDS");
		String failedInstrIdsStr 		= m_binder.getLocal("FAILED_INSTR_IDS");
		String terminatedIdsStr 		= m_binder.getLocal("TERMINATED_INSTR_IDS");
		String resubmitInstrIdsStr		= m_binder.getLocal("RESUBMIT_INSTR_IDS");
		
		Vector<Integer> authorisedInstrIds = 
		 CCLAUtils.getIntegerList(authorisedInstrIdsStr);
		Vector<Integer> failedInstrIds = 
		 CCLAUtils.getIntegerList(failedInstrIdsStr);
		Vector<Integer> terminatedInstrIds =
		 CCLAUtils.getIntegerList(terminatedIdsStr);
		Vector<Integer> resubmitInstrIds =
		 CCLAUtils.getIntegerList(resubmitInstrIdsStr);
		
		Log.debug("Updating SDU Instructions: " 
		 + authorisedInstrIds.size() + " to authorise/retry, "
		 + failedInstrIds.size() + " to unauthorise/reject, "
		 + terminatedInstrIds.size() + " to terminate, "
		 + resubmitInstrIds.size() + " to resubmit for authorisation");
		
		if (authorisedInstrIds.isEmpty() 
			&& failedInstrIds.isEmpty()
			&& terminatedInstrIds.isEmpty()
			&& resubmitInstrIds.isEmpty()) {
			String msg = "Unable to reject SDU instructions: Instruction IDs missing";
			
			Log.error(msg);			
			throw new ServiceException(msg);
		}
		
		String authorisedUser = m_binder.getLocal("AUTHORISED_USER");
		String rejectReason = m_binder.getLocal("REJECT_REASON");
		
		if (authorisedUser != null)
			authorisedUser = CCLAUtils.normalizeUserName(authorisedUser);
		
		Log.debug("Auth User: " + authorisedUser + ", Reject Reason: " + rejectReason); 
		
		if (StringUtils.stringIsBlank(authorisedUser)) {
			String msg = "Unable to update SDU instructions: Authorised User missing";
			
			Log.error(msg);			
			throw new ServiceException(msg);
		}

		// Fail if there are instructions to reject/terminate, and no reject reason 
		// given
		if ((!failedInstrIds.isEmpty() || !terminatedInstrIds.isEmpty())
			&& StringUtils.stringIsBlank(rejectReason)) {
			String msg = "Unable to reject/terminate SDU instructions: " +
			 "Reject Reason missing";
			
			Log.error(msg);			
			throw new ServiceException(msg);
		}

		FWFacade facade; 
		
		try {
			facade = CCLAUtils.getFacade(m_workspace, true);
		} catch (Exception e) {
			String msg = "Failed to instantiate facade: " + e.getMessage();
			
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
			
		boolean error = false;
		int successCount = 0;
		
		/* Update each instruction in its own transaction block. You can't afford to
		 * roll back if some instructions have already been executed in downstream
		 * systems or the whole thing will get very confused.
		 * 
		 * The IS_SUCCESSFUL flag is only set to 1 if ALL instructions were updated
		 * successfully, otherwise an exception is thrown back to the caller.
		 */
		
		// Mark all instructions here as Authorised
		try {
			for (Integer instrId : authorisedInstrIds) {
				
				try {
					facade.beginTransaction();
					
					doUpdateSDUInstruction
					 (instrId, SDUUpdateType.AUTHORISE_OR_RETRY, authorisedUser, null, 
					 null, facade);
					
					facade.commitTransaction();
					successCount++;
					
				} catch (DataException e) {
					facade.rollbackTransaction();
					
					Log.error("Failed to update SDU Instruction " + instrId, e);
					throw e;
				}
			}
				
			// Mark all instructions here as Unauthorised/rejected.
			for (Integer instrId : failedInstrIds) {
				
				try {
					facade.beginTransaction();
				
					doUpdateSDUInstruction
					 (instrId, SDUUpdateType.REJECT, 
					 authorisedUser, rejectReason, null, facade);	
					
					facade.commitTransaction();
					successCount++;
					
				} catch (DataException e) {
					facade.rollbackTransaction();
					
					Log.error("Failed to update SDU Instruction " + instrId, e);
					throw e;
				}
			}
			
			// Mark all instructions here as Terminated.
			for (Integer instrId : terminatedInstrIds) {
				
				try {
					facade.beginTransaction();
				
					doUpdateSDUInstruction
					 (instrId, SDUUpdateType.TERMINATE, authorisedUser, 
					 rejectReason, null, facade);	
					
					facade.commitTransaction();
					successCount++;
					
				} catch (DataException e) {
					facade.rollbackTransaction();
					
					Log.error("Failed to update SDU Instruction " + instrId, e);
					throw e;
				}
			}
			
		} catch (DataException de) {
			// Append progress counter to the error message and re-throw
			String successCountStr = "(succesfully updated " + successCount + " of " +
			 (authorisedInstrIds.size() + failedInstrIds.size()) + ")";
			
			Log.error(de.getMessage() + " " + successCountStr, de);
			throw new ServiceException(de.getMessage() + " " + successCountStr, de);
		}
		
		CCLAUtils.addQueryBooleanParamToBinder
		 (m_binder, "IS_SUCCESSFUL", !error);
	}
	
	/** Responsible for authorising, rejecting or retrying SDU Execution for the
	 *  passed Instruction ID.
	 *  
	 * @param instrId
	 * @param isAuthorised
	 * @param user				Username performing the update
	 * @param rejectReason
	 * @param facade
	 * @throws DataException
	 */
	private static void doUpdateSDUInstruction(int instrId, SDUUpdateType updateType, 
	 String user, String rejectReason, String comment, FWFacade facade) 
	 throws DataException {
		
		Log.debug("Updating SDU instruction ID: "+instrId);
		
		Instruction instr = Instruction.get(instrId, facade);
		
		Log.debug("Update Type? " + updateType.toString() + 
		 ", User: " + user + 
		 ", Reject Reason: " + rejectReason +
		 ", Comment: " + comment);

		if (instr == null) {
			String msg = 
			 "Cannot update SDU Instruction, cannot find instruction with ID: " 
			 + instrId;
			
			Log.error(msg);
			throw new DataException(msg);			
		}		
		
		Log.debug("Current Instruction Status: " + 
		 instr.getStatus().getInstructionStatusName());

		Integer newInstrStatusId = null;
		
		// Check for an applied process on this instruction (indicates its been 
		// suspended)
		InstructionProcessApplied instrProcAppl = 
		 InstructionProcessApplied.getActiveProcess(instrId, facade);
		
		InstructionAction actionToApply = null;
		
		// Perform appropriate action based on the instruction's current status.
		switch (instr.getStatus().getInstructionStatusId()) {
		
			// Pending Auth status. Set the Authorised data flag and Instruction Status
			// to Ready for Static Data Execution
			case InstructionStatus.StatusID.PENDING_STATIC_DATA_AUTHORISATION : {
				if (updateType == SDUUpdateType.TERMINATE) {
					newInstrStatusId = InstructionStatus.StatusID.TERMINATED;
					
				} else if (updateType == SDUUpdateType.AUTHORISE_OR_RETRY) {
					newInstrStatusId = 
					 InstructionStatus.StatusID.READY_FOR_STATIC_DATA_EXECUTION;
				} else if (updateType == SDUUpdateType.REJECT) {
					newInstrStatusId = 
					 InstructionStatus.StatusID.FAILED_STATIC_DATA_AUTHORISATION;
				} else {
					throw new DataException("Invalid status for update action: "
					 + updateType);
				}
				
				break;
			}
			
			// Pending Re-auth status. Identical to above.
			case InstructionStatus.StatusID.PENDING_STATIC_DATA_REAUTHORISATION : {
				if (updateType == SDUUpdateType.TERMINATE) {
					newInstrStatusId = InstructionStatus.StatusID.TERMINATED;
					
				} else if (updateType == SDUUpdateType.AUTHORISE_OR_RETRY) {
					newInstrStatusId = 
					 InstructionStatus.StatusID.READY_FOR_STATIC_DATA_EXECUTION;
				} else if (updateType == SDUUpdateType.REJECT) {
					newInstrStatusId = 
					 InstructionStatus.StatusID.FAILED_STATIC_DATA_AUTHORISATION;
				} else {
					throw new DataException("Invalid status for update action: "
					 + updateType);
				}
				
				break;
			}
			
			// Failed Static Data Authorisation status. Authorised flag should already
			// be set.
			// Providing the latest auth flag is set, Set the Authorised data flag and 
			// Instruction Status to Ready for Static Data Execution
			case InstructionStatus.StatusID.FAILED_STATIC_DATA_AUTHORISATION : {
				
				if (updateType == SDUUpdateType.TERMINATE) {
					newInstrStatusId = InstructionStatus.StatusID.TERMINATED;
					
				} else if (updateType == SDUUpdateType.AUTHORISE_OR_RETRY) {
					newInstrStatusId = 
					 InstructionStatus.StatusID.READY_FOR_STATIC_DATA_EXECUTION;
				
				} else if (updateType == SDUUpdateType.RESUBMIT) {
					newInstrStatusId = 
					 InstructionStatus.StatusID.PENDING_STATIC_DATA_REAUTHORISATION;
				}
				
				break;
			}
			
			// Ready for Static Data Execution status. Authorised flag should already be
			// set. Not much we can do here.
			case InstructionStatus.StatusID.READY_FOR_STATIC_DATA_EXECUTION : {
				
				String msg = "Cannot update SDU instruction ID: " + instrId + 
				 ", already pending execution";
				
				Log.error(msg);
				throw new DataException(msg);
			}
			
			// Previous attempt to execute actual Static Data Update associated with 
			// this instruction failed. Should be in a suspended state. If so, apply
			// Retry action to retry the execution immediately.
			case InstructionStatus.StatusID.FAILED_STATIC_DATA_EXECUTION : {
				
				Log.debug("Instruction is currently in Failed SDU Execution status. " +
				 "Checking if its currently suspended.");

				if (instrProcAppl != null) {
					if (updateType == SDUUpdateType.AUTHORISE_OR_RETRY) {
						Log.debug("Instruction was suspended. Applying Retry action");

						actionToApply = InstructionAction.RETRY;
					} else if (updateType == SDUUpdateType.REJECT) {
						Log.debug("Instruction was suspended. Applying Fail action");

						actionToApply = InstructionAction.FAIL;
					} else if (updateType == SDUUpdateType.TERMINATE) {
						Log.debug
						 ("Instruction was suspended. Applying Terminate action");

						actionToApply = InstructionAction.TERMINATE;
						
					} else if (updateType == SDUUpdateType.RESUBMIT) {
						Log.debug
						 ("Instruction was suspended. Applying Resubmit action");
						
						actionToApply = InstructionAction.RESUBMIT;
					}
					
				} else {
					// No active process. Just set status accordingly.
					Log.debug("Instruction wasn't suspended. Applying status update");

					if (updateType == SDUUpdateType.AUTHORISE_OR_RETRY) {
						newInstrStatusId = 
						 InstructionStatus.StatusID.READY_FOR_STATIC_DATA_EXECUTION;
						
					} else if (updateType == SDUUpdateType.REJECT) {
						newInstrStatusId = 
						 InstructionStatus.StatusID.FAILED_STATIC_DATA_AUTHORISATION;
					} else if (updateType == SDUUpdateType.TERMINATE) {
						newInstrStatusId = InstructionStatus.StatusID.TERMINATED;
						
					} else if (updateType == SDUUpdateType.RESUBMIT) {
						newInstrStatusId = 
						 InstructionStatus.StatusID.PENDING_STATIC_DATA_REAUTHORISATION;
					}
				}
				
				break;
			}
			
			// Other status. Throw error
			default : {
				String msg = "Cannot update SDU instruction ID: " + instrId + 
				 ", invalid instruction status: " 
				 +instr.getStatus().getInstructionStatusName();
				
				Log.error(msg);
				throw new DataException(msg);
			}
		}

		// Add/Update instruction comment
		if (comment != null) {
			InstructionDataApplied instrComment = InstructionUtils.getDataApplied
			 (instr, InstructionData.Fields.INSTRUCTION_COMMENTS, facade);
			instrComment.getDataFieldValue().setStringValue(comment);
			
			InstructionDataApplied.addOrUpdate(instrComment, facade, user);
		}
		
		// Update instruction status
		if (newInstrStatusId != null) {
			InstructionStatus newInstrStatus = 
			 InstructionStatus.getCache().getCachedInstance(newInstrStatusId);
			
			Log.debug("Updating Instruction Status to: " + 
			 newInstrStatus.getInstructionStatusName());
			
			instr.setStatus(newInstrStatus);
			instr.persist(facade, user);

			// If instruction was manually rejected, submit notification job to SPP.
			if (TRIGGER_SPP_JOB_ON_REJECT
				&& 
				(newInstrStatus.getInstructionStatusId() ==
				InstructionStatus.StatusID.FAILED_STATIC_DATA_AUTHORISATION)) {
				
				Log.debug("Instruction was rejected by user. " +
				 "Triggering notification workflow job");
				
				try {
					InstructionUtils.triggerSppNotificationJob
					 (instr, Globals.Users.System, facade);
				} catch (Exception e) {
					Log.error("Failed to trigger SPP Job for instruction "
							+instr.getInstructionId()+", "+e.getMessage(), e);
				}
			}
		}
		
		InstructionDataApplied instrDataApplied = null; 
		
		if (updateType == SDUUpdateType.RESUBMIT) {
			// Resubmit action must clear the IS_AUTHORISED and AUTHORISED_DATE fields
			Log.debug("Resubmit Action applied. Removing Auth Flag, Auth Date");
			
			instrDataApplied = InstructionUtils.getDataApplied
			 (instr, InstructionData.Fields.IS_AUTHORISED, facade);
			instrDataApplied.getDataFieldValue().setBoolValue(null);
			
			InstructionDataApplied.addOrUpdate(instrDataApplied, facade, user);
			
			instrDataApplied = InstructionUtils.getDataApplied
			 (instr, InstructionData.Fields.AUTHORISED_DATE, facade);
			instrDataApplied.getDataFieldValue().setDateValue(null);
			
			InstructionDataApplied.addOrUpdate(instrDataApplied, facade, user);
			
			instrDataApplied = InstructionUtils.getDataApplied
			 (instr, InstructionData.Fields.AUTHORISED_USER, facade);
			instrDataApplied.getDataFieldValue().setStringValue(user);
			
			InstructionDataApplied.addOrUpdate(instrDataApplied, facade, user);
			
			// Audit for reset Auth data
			InstructionAudit instructionAudit = 
				new InstructionAudit(null, instr.getInstructionId(), 
				 InstructionAuditAction.ACTION_SDU_RESET_AUTHORISATION,
				 null, 
				 instr.getStatus().getInstructionStatusId(), 
				 null, null, 0, user);
			
			InstructionAudit.add(instructionAudit, facade);
			
		} else {
			// Other actions will require updating the Auth fields.
			
			boolean isAuthorised = (updateType == SDUUpdateType.AUTHORISE_OR_RETRY);
			
			// Add/update isAuthorised Flag
			Log.debug("Updating Instruction Auth Flag to: " + isAuthorised);
			
			instrDataApplied = InstructionUtils.getDataApplied
			 (instr, InstructionData.Fields.IS_AUTHORISED, facade);
			instrDataApplied.getDataFieldValue().setBoolValue(isAuthorised);
			InstructionDataApplied.addOrUpdate
			 (instrDataApplied, facade, user);
			
			//update or add reject reason
			if (!StringUtils.stringIsBlank(rejectReason) && !isAuthorised) {
				instrDataApplied = InstructionUtils.getDataApplied
				 (instr, InstructionData.Fields.REJECT_REASON, facade);
				instrDataApplied.getDataFieldValue().setStringValue(rejectReason);
				InstructionDataApplied.addOrUpdate
				 (instrDataApplied, facade, user);
			}
			
			//update or add authoriseDate
			instrDataApplied = InstructionUtils.getDataApplied
			 (instr, InstructionData.Fields.AUTHORISED_DATE, facade);
			instrDataApplied.getDataFieldValue().setDateValue(DateUtils.getNow());
			InstructionDataApplied.addOrUpdate(instrDataApplied, facade, user);
			
			//update or add authoriseUser
			instrDataApplied = InstructionUtils.getDataApplied
			 (instr, InstructionData.Fields.AUTHORISED_USER, facade);
			instrDataApplied.getDataFieldValue().setStringValue(user);
			InstructionDataApplied.addOrUpdate(instrDataApplied, facade, user);
			
			// Audit for change in Auth flag
			Integer instrAuditActionId = null;
			
			// Audit whether or not the instruction is being authorised
			if (isAuthorised)
				instrAuditActionId = InstructionAuditAction.ACTION_SDU_AUTHORISED;
			else
				instrAuditActionId = InstructionAuditAction.ACTION_SDU_NOT_AUTHORISED;
			
			InstructionAudit instructionAudit = 
				new InstructionAudit(null, instr.getInstructionId(), 
				 instrAuditActionId,
				 null, 
				 instr.getStatus().getInstructionStatusId(), 
				 null, null, 0, user);
			
			InstructionAudit.add(instructionAudit, facade);
		}
			
		
		if (actionToApply != null) {
			Log.debug("Applying instruction action: " + actionToApply.getLabel());
			InstructionProcessUtils.applyAction
			 (instrProcAppl, actionToApply, 
			 facade, user);
		} else if (updateType == SDUUpdateType.TERMINATE) {
			Log.debug("Adding Instruction Terminated audit entry");

			InstructionAudit terminateAudit = 
				new InstructionAudit(null, instr.getInstructionId(), 
				 InstructionAuditAction.ACTION_TERMINATED,
				 null, 
				 instr.getStatus().getInstructionStatusId(), 
				 null, null, 0, user);
			
			InstructionAudit.add(terminateAudit, facade);
		}
		
		Log.debug("Finished updating SDU instruction ID: "+instrId);
	}
	
	/** Adds a ResultSet of pending Static Data Update instructions to the binder.
	 * 
	 * @throws DataException 
	 * 
	 */
	public void getPendingSDUInstructions() throws DataException {
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		CCLAUtils.addQueryIntParamToBinder
		 (m_binder, SharedCols.INSTRUCTION, SharedObjects.getEnvironmentInt
		 ("CCLA_CommProc_PendingSDUInstructionStatusId", 0));
		
		DataResultSet rsPendingSDUInstructions = 
		 facade.createResultSet("qCommProc_GetPendingSDUInstructions", m_binder);
	
		m_binder.addResultSet("PendingSDUInstructions", rsPendingSDUInstructions);
	}
	
	/** Adds a ResultSet to the binder 'SDUInstruction' containing information relating
	 *  to a single SDU instruction.
	 *  
	 * @throws DataException 
	 * @throws ServiceException 
	 */
	public void getSDUInstructionInfo() throws DataException, ServiceException {
		
		Integer instrId = CCLAUtils.getBinderIntegerValue(m_binder, "INSTRUCTION_ID");
		
		if (instrId == null)
			throw new ServiceException("Instruction ID missing");
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		DataResultSet rsSDUInstruction = 
		 facade.createResultSet("qCommProc_GetSDUInstruction", m_binder);
		
		if (rsSDUInstruction.isEmpty()) {
			throw new ServiceException("Static Data Update instruction with ID " 
			 + instrId + " not found");
		}
		
		m_binder.addResultSet("SDUInstruction", rsSDUInstruction);
	}
	
	/** Triggered by web service request from Workflow system.
	 *  
	 *  When an SDU instruction fails execution, or is rejected, a notification job
	 *  is automatically triggered by UCM. A user can trigger the below service when
	 *  they have investigated the issue and deemed it ready for re-authorisation.
	 *  
	 *  The optional comment string that gets passed down will be set as the original
	 *  instruction's Comment data field.
	 *  
	 * @throws DataException
	 * @throws ServiceException 
	 */
	public void resubmitSDUInstructionForAuthorisation() 
	 throws DataException, ServiceException {
		
		Integer instrId 	= CCLAUtils.getBinderIntegerValue
								(m_binder, "INSTRUCTION_ID");
		
		if (instrId == null)
			throw new ServiceException("Instruction ID missing");
		
		String resubmitUser = m_binder.getLocal("RESUBMIT_USER");
		
		if (StringUtils.stringIsBlank(resubmitUser))
			throw new ServiceException("Resubmit User Name missing");
		
		String comment 		= m_binder.getLocal("COMMENT");
		
		try {
			FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
			
			doUpdateSDUInstruction(instrId, SDUUpdateType.RESUBMIT, 
			 CCLAUtils.normalizeUserName(resubmitUser), null, comment, facade);
		} catch (DataException e) {
			String msg = "Failed to resubmit Instruction ID " + instrId + 
			 " for authorisation by user '" + resubmitUser + "': " + e.getMessage();
			
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}
	
	/** Test method for directly creating a new Aurora Correspondent from an existing
	 *  Person record.
	 *  
	 * @throws ServiceException
	 * @throws DataException
	 */
	public void addAuroraCorrespondent() throws ServiceException, DataException {
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace);
		
		Person p = Person.get(CCLAUtils.getBinderIntegerValue
		 (m_binder, SharedCols.PERSON), true, facade);
		
		Company company = Company.getCache().getCachedInstance
		 (CCLAUtils.getBinderIntegerValue(m_binder, AuroraClient.Cols.COMPANY));
		
		AuroraCorrespondentHandler corrHandler = new AuroraCorrespondentHandler();
		corrHandler.addAuroraEntity(p, company, facade);
	}

	/** Test service, used to fetch a single correspondent from Aurora.
	 * @throws DataException 
	 * @throws ServiceException 
	 * 
	 */
	public void testGetCorrespondent() throws DataException, ServiceException {
		int corrId = CCLAUtils.getBinderIntegerValue
		 (m_binder, AuroraCorrespondent.Cols.CORR);
		String company = m_binder.getLocal("COMPANY_CODE");
	}
	
}
