package com.ecs.ucm.ccla.commproc.modulehandler;

import intradoc.data.DataException;

import com.ecs.ucm.ccla.Globals;
import com.ecs.ucm.ccla.commproc.InstructionLockUtils;
import com.ecs.ucm.ccla.commproc.InstructionUtils;
import com.ecs.ucm.ccla.commproc.RoutingModuleManager;
import com.ecs.ucm.ccla.commproc.events.IInstructionEvent;
import com.ecs.ucm.ccla.data.instruction.Instruction;
import com.ecs.ucm.ccla.data.instruction.InstructionAudit;
import com.ecs.ucm.ccla.data.instruction.InstructionAuditAction;
import com.ecs.ucm.ccla.data.instruction.InstructionData;
import com.ecs.ucm.ccla.data.instruction.InstructionDataApplied;
import com.ecs.ucm.ccla.data.instruction.InstructionProcess;
import com.ecs.ucm.ccla.data.instruction.InstructionProcessApplied;
import com.ecs.ucm.ccla.data.instruction.InstructionStatus;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Submits any captured items to SPP, i.e. triggers the standard SPP document job.
 * 
 * @author Tom
 *
 */
public class SPPJobModuleHandler extends RoutingModuleHandler {
	
	/** Attempts to start an SPP job for the given instruction.
	 * 
	 */
	protected EvalOutcome eval(Instruction instr, FWFacade facade)
	 throws DataException {
			
		boolean jobStarted = false;
		
		// Fetch the Workflow Date value. If this is present, the item has already gone
		// to SPP.
		InstructionDataApplied wfDate = InstructionDataApplied.getDataApplied
		 (instr.getInstructionId(), InstructionData.Fields.WORKFLOW_DATE, facade);
		
		if (wfDate != null && !wfDate.getDataFieldValue().isEmpty()) {
			Log.debug("Item has already been dispatched to SPP (workflow date " + 
			 wfDate.getDataFieldValue().getDateValue() + ")");
			
			return EvalOutcome.SUSPEND;
		}
		
		try {
			jobStarted = InstructionUtils.submitInstructionToSpp
			 (instr, Globals.Users.System, facade);
		} catch (Exception e) {
			String msg = "Failed to trigger SPP job for Instruction: " + e.getMessage();
			Log.error(msg, e);
			 // Append error to spooler							 
			 RoutingModuleManager.getManager().getInstructionErrorSpooler().
			 	append(instr, this.getRoutingModule(), e);
		}
		
		if (jobStarted) {
			// Add a special 'Workflow Job Started' Instruction Audit
			InstructionAudit instrAudit = new InstructionAudit
			 (null,instr.getInstructionId(),
			 InstructionAuditAction.ACTION_START_WORKFLOW, 
			 this.getRoutingModule().getModuleId(), 
			 instr.getStatus().getInstructionStatusId(), 
			 null, null, 0, Globals.Users.System);
			
			InstructionAudit.add(instrAudit, facade);
			
			return EvalOutcome.PASS;
		} else
			return EvalOutcome.SUSPEND;	
	}
	
	/**
	 * Gets the suspend message unique to the module;
	 * @return
	 */
	public String getSuspendMessage() {
		return "Unable to start an SPP job. It may have been sent to SPP already.";
	}
	
	/**
	 * Gets the suspend instruction process unique to this module.
	 * @return
	 */
	public InstructionProcess getSuspendInstructionProcess() throws DataException {
		return InstructionProcess.getCache().getCachedInstance(
			 InstructionProcess.ProcessIds.SPP_RELEASE_FAILURE);
	}

	/** Static Data Update source instructions (Mandates, Apps etc.) must be forwarded on
	 *  to a different module.
	 *  
	 */
	@Override
	protected void applyPassAction(Instruction instr, FWFacade facade,
			String userName) throws DataException {
		
		if (InstructionUtils.isEligibleToGenerateStaticDataUpdates(instr, facade)) {
			instr.setStatus(InstructionStatus.getCache().getCachedInstance
				(InstructionStatus.StatusID.READY_FOR_STATIC_DATA_ROUTING));
			
			instr.persist(facade, userName);
			
			InstructionLockUtils.unlockInstruction
			 (instr.getInstructionId(), this.getRoutingModule(), facade, 
		     userName);
			
		} else {
			super.applyPassAction(instr, facade, userName);
		}
	}
}
