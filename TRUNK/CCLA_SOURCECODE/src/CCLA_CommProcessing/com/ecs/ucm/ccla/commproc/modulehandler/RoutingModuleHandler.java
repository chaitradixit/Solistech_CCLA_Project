package com.ecs.ucm.ccla.commproc.modulehandler;

import intradoc.data.DataException;

import java.util.Vector;

import com.ecs.ucm.ccla.commproc.InstructionLockUtils;
import com.ecs.ucm.ccla.commproc.events.IInstructionEvent;
import com.ecs.ucm.ccla.data.instruction.Instruction;
import com.ecs.ucm.ccla.data.instruction.InstructionProcess;
import com.ecs.ucm.ccla.data.instruction.InstructionProcessApplied;
import com.ecs.ucm.ccla.data.instruction.InstructionStatus;
import com.ecs.ucm.ccla.data.instruction.ModuleRuleApplied;
import com.ecs.ucm.ccla.data.instruction.RoutingModule;

import com.ecs.utils.stellent.embedded.FWFacade;

/** This base class, and all its sub-classes, are used to control a RoutingModule's
 *  logic and actions.
 *  
 *  If a RoutingMoudle requires custom execution logic, it will reference a 
 *  specific subclass of this class which will override the methods here where
 *  neccessary.
 *  
 * @author Tom
 *
 */
public class RoutingModuleHandler extends AbstractModuleHandler {

	
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
	protected boolean accept(Instruction instr, FWFacade facade) throws DataException {
		// Fetch all 'Accept' rules for this module
		Vector<ModuleRuleApplied> acceptRules = 
		 routingModule.getRulesByType(RoutingModule.RuleType.Accept);
		
		if (acceptRules == null || acceptRules.isEmpty())
			return true; 	// if no acceptance rules defined, automatically accept
							// every instruction
		
		// Step through each accept rule. If one passes, accept the instruction.
		for (ModuleRuleApplied acceptRuleAppl : acceptRules) {
			if (acceptRuleAppl.getInstructionRule().eval(instr)) {
				return true;
			}
		}
		
		return false;
	}
	
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
	protected EvalOutcome eval(Instruction instr, FWFacade facade) 
	 throws DataException {
		
		
		
		// Fetch all 'Check' rules for this module
		Vector<ModuleRuleApplied> checkRules = 
		 routingModule.getRulesByType(RoutingModule.RuleType.Check);
		
		// Automatically pass if the Routing Module has no defined 'check' rules.
		if (checkRules == null || checkRules.isEmpty())
			return EvalOutcome.PASS;
		
		for (ModuleRuleApplied checkRuleApplied : checkRules) {
			if (!checkRuleApplied.getInstructionRule().eval(instr)) {
				// Failed Check Rule evaluation.
				// See if it has an associated Instruction process.
				
				InstructionProcess failProcess = 
				 checkRuleApplied.getInstructionProcess();
				
				if (failProcess != null) {
					// Create new Process instance
					InstructionProcessApplied.add
					 (instr.getInstructionId(), failProcess, null, facade);
					
					return EvalOutcome.SUSPEND; // Suspend instruction
				} else {
					return EvalOutcome.FAIL; 	// Fail for any unmatched 'check' rule
												// which doesn't have an associated
												// Instruction Process.
				}
			}
		}
		
		return EvalOutcome.PASS; // Pass for all matching 'check' rule
	}
	


	
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
	protected void applyPassAction(Instruction instr, FWFacade facade, String userName) 
	 throws DataException {
		
		InstructionStatus passStatus = this.getRoutingModule().getPassStatus();
		
		if (passStatus == null)
			throw new DataException("Module Pass Status missing");
		
		instr.setStatus(passStatus);
		instr.persist(facade, userName);
		
		InstructionLockUtils.unlockInstruction
		 (instr.getInstructionId(), this.getRoutingModule(), facade, 
	     userName);
	}
	
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
	protected void applyFailAction(Instruction instr, FWFacade facade, String userName) 
	 throws DataException {
		
		InstructionStatus failStatus = this.getRoutingModule().getFailStatus();
		
		if (failStatus == null)
			throw new DataException("Module Fail Status missing");
		
		instr.setStatus(failStatus);
		instr.persist(facade, userName);
		
		InstructionLockUtils.unlockInstruction
		 (instr.getInstructionId(), this.getRoutingModule(), facade, userName);
	}
	
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
	protected void applySuspendAction
	 (Instruction instr, FWFacade facade, String userName) throws DataException {
		
		InstructionLockUtils.suspendInstruction
		 (instr.getInstructionId(), this.getRoutingModule(), facade, userName);
		
		instr.setStatus(this.getRoutingModule().getFailStatus());
		instr.persist(facade, userName);
		
		// Subclasses will trigger their own suspend actions here instead of the
		// default one.
		InstructionProcessApplied.add(
			instr.getInstructionId(), 
			getSuspendInstructionProcess(), 
			getSuspendMessage(), 
			facade
		);
	}
	
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
	protected void applySkipAction(Instruction instr, FWFacade facade, String userName) 
	 throws DataException {
		
		InstructionStatus skipStatus = this.getRoutingModule().getSkipStatus();
		
		if (skipStatus == null)
			throw new DataException("Module Skip Status missing");
		
		instr.setStatus(skipStatus);
		instr.persist(facade, userName);
		
		InstructionLockUtils.unlockInstruction
		 (instr.getInstructionId(), this.getRoutingModule(), facade, userName);
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
	protected void applyEntryAction(Instruction instr, FWFacade facade, String userName) 
	throws DataException{
		
		InstructionStatus entryStatus = this.getRoutingModule().getEntryStatus();
				
		if (entryStatus != null){			
			instr.setStatus(entryStatus);
			instr.persist(facade, userName);
		}
	}
	
	/**
	 * This method applies the 'terminated' status.
	 * 
	 * @param instr
	 * @param facade
	 * @param userName
	 * @throws DataException 
	 */
	protected void applyTerminateAction(Instruction instr, FWFacade facade,
			String userName) throws DataException {

		instr.setStatus(InstructionStatus.getCache().getCachedInstance
		 (InstructionStatus.StatusID.TERMINATED));
		instr.persist(facade, userName);
		
		InstructionLockUtils.unlockInstruction
		 (instr.getInstructionId(), this.getRoutingModule(), facade, userName);
	}
	
	/**
	 * This method must be overriden by implementors, raises an exception by default.
	 * 
	 * @param instr
	 * @param facade
	 * @param userName
	 * @throws DataException 
	 */
	protected void applyResubmitAction(Instruction instr, FWFacade facade,
	 String userName) throws DataException {
		
		throw new DataException("Unbound module action: Resubmit");
	}

	/**
	 * Gets the suspend message unique to the module;
	 * @return
	 */
	public String getSuspendMessage() {
		return "The instruction was suspended, not sure why? Figure it out yourself";
	}
	
	/**
	 * Gets the suspend instruction process unique to this module.
	 * @return
	 */
	public InstructionProcess getSuspendInstructionProcess() throws DataException {
		return InstructionProcess.getCache().getCachedInstance(RoutingModuleHandler.DEFAULT_SUSPEND_PROCESS_ID);
	}
}
