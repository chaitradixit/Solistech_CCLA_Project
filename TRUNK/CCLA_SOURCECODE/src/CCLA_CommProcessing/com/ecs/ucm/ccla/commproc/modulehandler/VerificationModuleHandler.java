package com.ecs.ucm.ccla.commproc.modulehandler;

import intradoc.data.DataException;

import com.ecs.ucm.ccla.data.instruction.Instruction;
import com.ecs.utils.stellent.embedded.FWFacade;

/** All instructions which refer to a client/account will be eligible for
 *  consumption by this module.
 *  
 *  It will have a series of accept/check rules configured in the DB - these
 *  will cover business validation/verification checks, e.g: 
 *  
 *  -Is the account active? 
 *  -Is there sufficient balance in the account?
 *  -Has the client/account passed KYC checks?
 *  
 *  If an instruction doesn't pass any of the check rules, a special SPP
 *  notification job is started so that a user can attempt to rectify the
 *  issue.
 *  
 * @author Tom
 *
 */
public class VerificationModuleHandler extends RoutingModuleHandler {

	@Override
	protected boolean accept(Instruction instr, FWFacade facade)
			throws DataException {
		// TODO Auto-generated method stub
		return super.accept(instr, facade);
	}

	@Override
	protected EvalOutcome eval(Instruction instr, FWFacade facade)
			throws DataException {
		
		if (instr.getType().isFinancialTransaction())
			return EvalOutcome.SUSPEND;
		else
			return EvalOutcome.PASS;
	}

}
