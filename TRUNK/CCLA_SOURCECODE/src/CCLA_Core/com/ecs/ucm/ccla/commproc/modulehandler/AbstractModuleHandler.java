package com.ecs.ucm.ccla.commproc.modulehandler;

import intradoc.data.DataException;

import com.ecs.ucm.ccla.Globals;
import com.ecs.ucm.ccla.commproc.events.IInstructionEvent;
import com.ecs.ucm.ccla.commproc.listener.IInstructionListener;
import com.ecs.ucm.ccla.data.instruction.Instruction;
import com.ecs.ucm.ccla.data.instruction.InstructionAction;
import com.ecs.ucm.ccla.data.instruction.InstructionAudit;
import com.ecs.ucm.ccla.data.instruction.InstructionAuditAction;
import com.ecs.ucm.ccla.data.instruction.RoutingModule;
import com.ecs.utils.stellent.embedded.FWFacade;

public abstract class AbstractModuleHandler implements IModuleHandler, IInstructionListener {

	/** Username that will be tagged against audit records etc. for any actions
	 *  performed implicitly by Routing Module Handlers.
	 *  
	 */
	public static final String MODULE_HANDLER_USER = Globals.Users.System;
	
	/** Instruction Process ID for the default process which is triggerd when an
	 *  instruction is suspended.
	 *  
	 */
	public static final int DEFAULT_SUSPEND_PROCESS_ID = 1;
	
	protected RoutingModule routingModule;
	
	public static enum EvalOutcome {
		PASS, FAIL, SUSPEND
	}
	
	public void processInstruction(Instruction instr, FWFacade facade,
			String userName) throws DataException {
		
		doEntryAction(instr, facade, userName);
		
		// Test acceptance conditions
		if (accept(instr, facade)) {
			
			EvalOutcome outcome = eval(instr, facade);
			
			if (outcome.equals(EvalOutcome.PASS)) {
				doPassAction(instr, facade, userName);
				
			} else if (outcome.equals(EvalOutcome.FAIL)) {
				doFailAction(instr, facade, userName);

			} else if (outcome.equals(EvalOutcome.SUSPEND)) {
				doSuspendAction(instr, facade, userName);
			}
			
		} else {
			// Comm was not accepted by module. Set skip status
			doSkipAction(instr, facade, userName);
		}
	}

	public void applyInstructionAction
	 (Instruction instr, InstructionAction action, FWFacade facade, String userName) 
	 throws DataException {
		
		if (action.equals(InstructionAction.RETRY)) {
			// Force module re-entry
			processInstruction(instr, facade, userName);
		
		} else if (action.equals(InstructionAction.PASS) 
					|| 
				   action.equals(InstructionAction.APPROVE)) {
			
			// Apply a PASS action
			doPassAction(instr, facade, userName);
			
		} else if (action.equals(InstructionAction.FAIL)
					|| 
				   action.equals(InstructionAction.REJECT)
				    ||
				   action.equals(InstructionAction.CANCEL)) {
			
			// Apply a FAIL action
			doFailAction(instr, facade, userName);	
		} else if (action.equals(InstructionAction.SUSPEND)) {
			doSuspendAction(instr, facade, userName);
			
		} else if (action.equals(InstructionAction.TERMINATE)) {
			doTerminateAction(instr, facade, userName);
		
		} else if (action.equals(InstructionAction.RESUBMIT)) {
			doResubmitAction(instr, facade, userName);
			
		} else {
			throw new DataException("Unbound instruction action: " + action);
		}
	}
		
	
	/**
	 * Do the entry action 
	 * @param instr
	 * @param facade
	 * @param userName
	 */
	protected void doEntryAction(Instruction instr, FWFacade facade, String userName) 
	 throws DataException{
		applyEntryAction(instr, facade, userName);
		applyAudit(instr, facade, userName, InstructionAuditAction.ACTION_ENTERED_MODULE);
		
	}

	/**
	 * Do the skip action 
	 * @param instr
	 * @param facade
	 * @param userName
	 */
	protected void doSkipAction(Instruction instr, FWFacade facade, String userName) 
	 throws DataException{
		applySkipAction(instr, facade, userName);
		applyAudit(instr, facade, userName, InstructionAuditAction.ACTION_SKIPPED);

	}	
	
	/**
	 * Do the suspend action 
	 * @param instr
	 * @param facade
	 * @param userName
	 */
	protected void doSuspendAction(Instruction instr, 
	 FWFacade facade, String userName) throws DataException {
		applySuspendAction(instr, facade, userName);
		applyAudit(instr, facade, userName, InstructionAuditAction.ACTION_SUSPENDED);
	}		
	
	/**
	 * Do the terminate action 
	 * @param instr
	 * @param facade
	 * @param userName
	 */
	protected void doTerminateAction(Instruction instr, 
	 FWFacade facade, String userName) throws DataException {
		applyTerminateAction(instr, facade, userName);
		applyAudit(instr, facade, userName, InstructionAuditAction.ACTION_TERMINATED);
	}		
	
	/**
	 * Do the resubmit action 
	 * @param instr
	 * @param facade
	 * @param userName
	 */
	protected void doResubmitAction(Instruction instr, 
	 FWFacade facade, String userName) throws DataException {
		applyResubmitAction(instr, facade, userName);
		applyAudit(instr, facade, userName, InstructionAuditAction.ACTION_RESUBMITTED);
	}		
	
	
	/**
	 * Do the pass action 
	 * @param instr
	 * @param facade
	 * @param userName
	 */
	protected void doPassAction(Instruction instr, FWFacade facade, String userName) 
	 throws DataException{
		applyPassAction(instr, facade, userName);
		applyAudit(instr, facade, userName, InstructionAuditAction.ACTION_PASSED);
	}	

	/**
	 * Do the fail action 
	 * @param instr
	 * @param facade
	 * @param userName
	 */
	protected void doFailAction(Instruction instr, FWFacade facade, String userName) 
	 throws DataException{
		applyFailAction(instr, facade, userName);
		applyAudit(instr, facade, userName, InstructionAuditAction.ACTION_FAILED);
	}		
	
	/**
	 * This method applies the entry status action when the processInstruction is 
	 * initiated.
	 * Applying an entry status is not mandatory, therefore if the entrystatus is null
	 * just pass over the set status action
	 * @param instr
	 * @param facade
	 * @param userName
	 * @throws DataException 
	 */
	protected abstract void applyEntryAction
	 (Instruction instr, FWFacade facade, String userName) throws DataException;
	
	/** Applies the Routing Module 'skip' action for the given instruction. Generally
	 *  executed on instructions which don't match any of the module's accept 
	 *  conditions.
	 *  
	 *  This includes setting the Skip status and unlocking the instruction.
	 *  
	 * @param instr
	 * @param facade
	 * @param userName
	 * @throws DataException
	 */
	protected abstract void applySkipAction
	 (Instruction instr, FWFacade facade, String userName) 
	 throws DataException;
	
	/** Applies the Routing Module 'suspend' action for the given instruction. Generally
	 *  executed on instructions which cannot proceed from this module until a user
	 *  makes a decision of some sort, via an InstructionProcessApplied instance.
	 *  
	 *  This sets the instruction module lock to a Suspended state.
	 *  
	 * @param instr
	 * @param facade
	 * @param userName
	 * @throws DataException
	 */
	protected abstract void applySuspendAction
	 (Instruction instr, FWFacade facade, String userName) throws DataException;
	
	/** Applies the Routing Module 'terminate' action for the given instruction. 
	 *    
	 *  By default, this will set the instruction to the 'Terminated' status. This
	 *  should be overriden by modules which require checks to ensure an instruction
	 *  is an a suitable state for termination.
	 *  
	 * @param instr
	 * @param facade
	 * @param userName
	 * @throws DataException
	 */
	protected abstract void applyTerminateAction
	 (Instruction instr, FWFacade facade, String userName) throws DataException;
	
	/** Applies the Routing Module 'resubmit' action for the given instruction. 
	 *    
	 *  This action should be overriden if there is ever a need to reset or resubmit an
	 *  item which has become suspended.
	 *  
	 * @param instr
	 * @param facade
	 * @param userName
	 * @throws DataException
	 */
	protected abstract void applyResubmitAction
	 (Instruction instr, FWFacade facade, String userName) throws DataException;
	
	/** Applies the Routing Module 'fail' action for the given instruction. Generally
	 *  exeucted on instructions which do not pass the module's check conditions
	 *  and must be set with a terminated or exception state, e.g. confirmed duplicate 
	 *  items.
	 *  
	 *  This includes setting the Fail status and unlocking the instruction.
	 *  
	 * @param instr
	 * @param facade
	 * @param userName
	 * @throws DataException
	 */
	protected abstract void applyFailAction
	 (Instruction instr, FWFacade facade, String userName) 
	 throws DataException;
	
	/** Applies the Routing Module 'pass' action for the given instruction. Generally
	 *  executed on instructions which pass acceptance and check conditions for this
	 *  module, allowing them to proceed to the next module in sequence.
	 *  
	 *  This includes setting the Pass status and unlocking the instruction.
	 *  
	 * @param instr
	 * @param facade
	 * @param userName
	 * @throws DataException
	 */
	protected abstract void applyPassAction
	 (Instruction instr, FWFacade facade, String userName) 
	 throws DataException;	
	
	
	/**
	 * Add a audit log to the action
	 * @param instr
	 * @param facade
	 * @param userName
	 * @param auditTypeId
	 */
	protected void applyAudit
	 (Instruction instr, FWFacade facade, String userName, int auditTypeId) 
	throws DataException{
		
		InstructionAudit instructionAudit = new InstructionAudit
				(null, instr.getInstructionId(),
				auditTypeId, routingModule.getModuleId(),
				instr.getStatus().getInstructionStatusId(),
				null, null, 0, userName);
		InstructionAudit.add(instructionAudit, facade);
	}
	
	/** Determines whether a given communication will be evaluated by the 
	 *  Module. By default this is based on the mapped acceptance rules. If
	 *  no acceptance rules are present, every Communication will be evaluated
	 *  by the module.
	 *  
	 *  If the Communication does not pass any acceptance rules, the 'skip' 
	 *  status value is applied to it.
	 *  
	 *  If the Communication is accepted by the module, the 'entry' status 
	 *  value is applied and then the evaluate() method	is executed.
	 *    
	 * @param comm
	 * @return
	 * @throws DataException 
	 */
	protected abstract boolean accept(Instruction instr, FWFacade facade) 
	 throws DataException;
	
	
	/** Applies evaluation and processing logic to accepted Instructions.
	 *  
	 *  By default, this will check whether or not the Instruction passes
	 *  all of the mapped 'Check' rules.
	 *  
	 *  If a 'Check' rule fails, it is checked for an associated Instruction Process. 
	 *  If present, a new process instance is kicked off and the instruction will be
	 *  suspended.
	 * 
	 * @param comm
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	protected abstract EvalOutcome eval(Instruction instr, FWFacade facade) 
	 throws DataException;
	
	
	public RoutingModule getRoutingModule() {
		return routingModule;
	}

	public void setRoutingModule(RoutingModule routingModule) {
		this.routingModule = routingModule;
	}	
	
	protected void processEvent(IInstructionEvent event) {
		//Do nothing
	}
	
	//Listener
	public void receivedEvent(IInstructionEvent event) {
		processEvent(event);
	}
	
	

}
