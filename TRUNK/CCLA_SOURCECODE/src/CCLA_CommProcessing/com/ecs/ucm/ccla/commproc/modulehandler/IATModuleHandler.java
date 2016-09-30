package com.ecs.ucm.ccla.commproc.modulehandler;

import intradoc.data.DataException;
import com.ecs.ucm.ccla.data.instruction.Instruction;
import com.ecs.ucm.ccla.data.instruction.InstructionType;
import com.ecs.utils.stellent.embedded.FWFacade;

public class IATModuleHandler extends RoutingModuleHandler {

	protected EvalOutcome eval(Instruction instr, FWFacade facade)
	 throws DataException {

		int instrTypeId = instr.getType().getInstructionTypeId(); 
			
		if (instrTypeId==InstructionType.Ids.BNKCONDIN) {
			return EvalOutcome.FAIL;
		} else if (instrTypeId==InstructionType.Ids.DEPBNK || 
					instrTypeId==InstructionType.Ids.BUYBNK) {
			return EvalOutcome.PASS;	
		} else {
			//should never happen.
			return EvalOutcome.SUSPEND;
		}
	}

	/**
	 * only accepts bnkcondin, dicondin, depbnk, buybnk 
	 */
	protected boolean accept(Instruction instr, FWFacade facade)
	throws DataException 
	{
		int instrTypeId = instr.getType().getInstructionTypeId(); 

		if (instrTypeId==InstructionType.Ids.BNKCONDIN ||
				instrTypeId==InstructionType.Ids.DEPBNK ||
				instrTypeId==InstructionType.Ids.BUYBNK) {
			return true;
		} else {
			return false;
		}
	}

}
