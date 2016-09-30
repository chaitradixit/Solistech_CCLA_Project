package com.ecs.ucm.ccla.commproc;

import intradoc.data.DataException;

import java.util.Date;
import java.util.Vector;

import com.ecs.ucm.ccla.data.instruction.Instruction;
import com.ecs.ucm.ccla.data.instruction.InstructionAction;
import com.ecs.ucm.ccla.data.instruction.InstructionActionApplied;
import com.ecs.ucm.ccla.data.instruction.InstructionAudit;
import com.ecs.ucm.ccla.data.instruction.InstructionAuditAction;
import com.ecs.ucm.ccla.data.instruction.InstructionLock;
import com.ecs.ucm.ccla.data.instruction.InstructionProcessApplied;
import com.ecs.ucm.ccla.commproc.modulehandler.AbstractModuleHandler;
import com.ecs.ucm.ccla.commproc.modulehandler.IModuleHandler;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.embedded.FWFacade;

public class InstructionProcessUtils {
	
	/** Applies an action to an active Instruction Process.
	 *  
	 *  First checks that the applied Instruction Process is active, then ensures the
	 *  requested action is valid for this Instruction Process.
	 *  
	 *  Providing these initial checks pass, the 
	 *  
	 * @param processAppl
	 * @param action
	 * @param facade
	 * @param userName
	 * @throws DataException
	 */
	public static void applyAction
	 (InstructionProcessApplied processAppl, InstructionAction action, 
	 FWFacade facade, String userName) throws DataException {
		
		if (!processAppl.isActive())
			throw new DataException("Unable to apply instruction action, process is " +
			 "no longer active");
		
		Instruction instruction = Instruction.get
		 (processAppl.getInstructionId(), facade);
		
		Vector<InstructionActionApplied> availActions = 
		 processAppl.getProcess().getAvailableActions();
		
		boolean isValidAction = false;
		
		for (InstructionActionApplied availAction : availActions) {
			if (availAction.getInstructionActionId() == action.getActionId()) {
				// Requested action is valid for this Instruction Process.
				isValidAction = true;
				break;
			}
		}
		
		if (!isValidAction)
			throw new DataException("Unable to perform instruction action '" 
			 + action.getLabel() + "', not a valid action for this process");
		
		// Fetch the Instruction Lock to determine which module triggered the
		// process.
		InstructionLock instrLock = 
		 InstructionLockUtils.getLock(processAppl.getInstructionId(), facade);
		
		if (instrLock == null)
			throw new DataException("Unable to perform instruction action '" 
			 + action.getLabel() + "', instruction is not locked");
		
		Log.debug("Applying Process Action '" + action.getLabel() + 
		 "' to Instruction Process ID: " + processAppl.getInstructionProcessAppliedId() 
		 + ", Instruction ID: " + processAppl.getInstructionId());
		
		// Mark the applied Instruction Process as Complete.
		processAppl.setEndDate(new Date());
		processAppl.setUserName(userName);
		processAppl.setInstructionActionId(action.getActionId());
		
		processAppl.persist(facade, userName);
		
		// Add an Instruction Audit for the applied action.
		InstructionAudit actionAudit = new InstructionAudit(null, 
		 processAppl.getInstructionId(), InstructionAuditAction.ACTION_APPLIED,
		 instrLock.getModule().getModuleId(), null, action.getActionId(), 
		 null, null, userName);
		 
		InstructionAudit.add(actionAudit, facade);
		
		// Now apply the action via the Module Handler
		AbstractModuleHandler handler = instrLock.getModule().getModuleHandler();
		handler.applyInstructionAction(instruction, action, facade, userName);
	}
}
