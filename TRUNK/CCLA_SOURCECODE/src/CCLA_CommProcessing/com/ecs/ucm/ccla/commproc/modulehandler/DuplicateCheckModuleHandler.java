package com.ecs.ucm.ccla.commproc.modulehandler;

import intradoc.data.DataException;

import com.ecs.ucm.ccla.data.instruction.Instruction;

import com.ecs.utils.stellent.embedded.FWFacade;

/** Any communications with a non-null account and cash/units field are
 *  eligible for this module.
 *  
 *  Duplicate checking involves running a query to check for any items with
 *  matching data fields within a given date range.
 *  
 *  If the item is a possible duplicate, its status is updated to 'Suspected 
 *  Duplicate' and it will be suspended in this module until a user has
 *  confirmed it as a duplicate or ignored it.
 *  
 *  Documents which are positively marked as Duplicate will terminate 
 *  processing.
 *  
 * @author Tom
 *
 */
public class DuplicateCheckModuleHandler extends RoutingModuleHandler {

	@Override
	protected boolean accept(Instruction instr, FWFacade facade)
			throws DataException {
		return super.accept(instr, facade);
	}

	@Override
	protected EvalOutcome eval(Instruction instr, FWFacade facade) 
	 throws DataException {

		// Determine whether the given instruction is a suspected duplicate.
		// TODO Run query..
		
		//BundleServices.getSuspectedDuplicateItemsData(docName, 
		// itemDate, clientNumber, accountNumStr, amount, facade);
		
		boolean isSuspectedDuplicate = false;
		
		if (isSuspectedDuplicate) {
			// TODO
			// Kick off an SPP 'Suspected Duplicate' notification job for 
			// this item.
			
			return EvalOutcome.SUSPEND;
		} else {
			// Item is not suspected to be a Duplicate. Mark as Passed.
			return EvalOutcome.PASS;
		}
	}
}
