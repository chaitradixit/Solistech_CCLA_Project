package com.ecs.ucm.ccla.commproc.modulehandler;

import java.util.Vector;

import intradoc.data.DataException;

import com.ecs.ucm.ccla.commproc.events.IInstructionEvent;
import com.ecs.ucm.ccla.data.instruction.Instruction;
import com.ecs.ucm.ccla.data.instruction.InstructionAction;
import com.ecs.ucm.ccla.data.instruction.InstructionProcess;
import com.ecs.ucm.ccla.data.instruction.RoutingModule;
import com.ecs.utils.stellent.embedded.FWFacade;

public interface IModuleHandler {

	/** Executed for any instruction which has a matching 'accept' status for
	 *  this module.
	 *  
	 *  1. Runs the accept method to see if the Instruction is eligibile for processing
	 *     by this module. If not, the skip status is set and the instruction is
	 *     unlocked.
	 *  2. If the instruction is accepted, it is evaluated. This has 3 possible return
	 *     states:
	 *     
	 *     PASS - instruction passed. Pass status set and instruction unlocked.
	 *     FAIL - instruction failed. Fail status set and instruction unlocked.
	 *     SUSPEND - instruction is suspended and requires user decision/intervention.
	 *     			 Instruction Lock 'suspended' flag is set
	 *  
	 * @param comm
	 * @param facade
	 * @throws DataException 
	 */
	public void processInstruction(Instruction instr, FWFacade facade,
			String userName) throws DataException; 
	

	/** Default instruction action handlers.
	 * 
	 * @param instr
	 * @param action
	 * @param facade
	 * @param userName
	 * @throws DataException
	 */
	public void applyInstructionAction
	 (Instruction instr, InstructionAction action, FWFacade facade, String userName) 
	 throws DataException;	
		
	/**
	 * Getter for Routing Module
	 * @return
	 */
	public RoutingModule getRoutingModule();

	/**
	 * Setter for Routing Module
	 * @param routingModule
	 */
	public void setRoutingModule(RoutingModule routingModule);
	
	/**
	 * Gets the suspend message unique to the module;
	 * @return
	 */
	public String getSuspendMessage();
	
	/**
	 * Gets the suspend instruction process unique to this module.
	 * @return
	 */
	public InstructionProcess getSuspendInstructionProcess() throws DataException;
}
