package com.ecs.ucm.ccla.commproc.modulehandler;

import java.util.Vector;

import intradoc.common.ServiceException;
import intradoc.data.DataException;

import com.ecs.ucm.ccla.Globals;
import com.ecs.ucm.ccla.commproc.InstructionUtils;
import com.ecs.ucm.ccla.data.comm.Comm;
import com.ecs.ucm.ccla.data.comm.CommGroup;
import com.ecs.ucm.ccla.data.instruction.Instruction;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Used to route Static Data Update instructions e.g. MAND, APP to the Static Data Update
 *  module, and apply higher priority on items that have arrived in a bundle containing
 *  transactions.
 *  
 *  Only accepts instructions eligible for generating Static Data Update instructions.
 *  
 * @author tm
 *
 */
public class StaticDataRouterModuleHandler extends RoutingModuleHandler {

	@Override
	protected EvalOutcome eval(Instruction instr, FWFacade facade)
			throws DataException {
		
		// Determine whether or not this instruction's Comm Group contains
		// at least 1 transaction.
		boolean hasTransactionsInGroup = false;
		Comm comm = Comm.get(instr.getCommId(), facade);
		
		try {
			Log.debug("Checking Instruction ID " + instr.getInstructionId() + 
				" for associated transactions");
			Vector<Instruction> instructions = CommGroup.getInstructions
			 (comm.getGroupId(), facade);
			
			for (Instruction groupInstr : instructions) {
				if (groupInstr.getType().isFinancialTransaction()) {
					Log.debug("Found a transaction of type " 
						+ groupInstr.getType().getName() + ", ID " + groupInstr.getInstructionId());
					
					hasTransactionsInGroup = true;
					break;
				}
			}
			
		} catch (ServiceException e) {
			Log.error("Failed to fetch instruction group", e);
		}
		
		if (hasTransactionsInGroup) {
			Log.debug("Increasing instruction priority");
			
			instr.setPriority(Instruction.HIGH_PRIORITY);
			instr.persist(facade, Globals.Users.System);
			
		} else {
			Log.debug("No transactions found - leaving instruction priority as-is");
		}
		
		return EvalOutcome.PASS;
	}

	/** Only accept SDU instructions. Others will have the Skip status applied
	 * 
	 */
	protected boolean accept(Instruction instr, FWFacade facade)
			throws DataException {
		return InstructionUtils.isEligibleToGenerateStaticDataUpdates(instr, facade);
	}
}
