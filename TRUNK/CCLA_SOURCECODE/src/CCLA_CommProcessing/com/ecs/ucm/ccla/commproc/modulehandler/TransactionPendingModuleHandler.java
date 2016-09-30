package com.ecs.ucm.ccla.commproc.modulehandler;

import intradoc.data.DataException;

import com.ecs.ucm.ccla.commproc.InstructionProcessUtils;
import com.ecs.ucm.ccla.commproc.events.IInstructionEvent;
import com.ecs.ucm.ccla.commproc.events.ITransactionReferenceEvent;
import com.ecs.ucm.ccla.data.instruction.Instruction;
import com.ecs.ucm.ccla.data.instruction.InstructionAction;
import com.ecs.ucm.ccla.data.instruction.InstructionData;
import com.ecs.ucm.ccla.data.instruction.InstructionDataApplied;
import com.ecs.ucm.ccla.data.instruction.InstructionProcessApplied;
import com.ecs.ucm.ccla.data.instruction.InstructionType;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.embedded.FWFacade;

public class TransactionPendingModuleHandler extends RoutingModuleHandler {

	@Override
	protected boolean accept(Instruction instr, FWFacade facade)
	throws DataException 
	{
		int instrTypeId = instr.getType().getInstructionTypeId(); 

		if (instrTypeId==InstructionType.Ids.PREADVICE || 
				instrTypeId==InstructionType.Ids.DICONDIN) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	protected EvalOutcome eval(Instruction instr, FWFacade facade) 
	throws DataException 
	{
        InstructionDataApplied transRefData = InstructionDataApplied.getDataApplied
        (instr.getInstructionId(), InstructionData.Fields.TRANSACTION_REFERENCE, facade);
        
        if (transRefData == null || transRefData.getDataFieldValue().isEmpty()) {
              return EvalOutcome.SUSPEND;
        } else {
              return EvalOutcome.PASS;
        }
	}

	/**
	 * Override this class to do something.
	 * @param event
	 */
	protected void processEvent(IInstructionEvent event) {
		if (event instanceof ITransactionReferenceEvent) {
			//do something
			int instrId = ((ITransactionReferenceEvent)event).getInstructionId();
			String transRef = ((ITransactionReferenceEvent)event).getTransactionRefence();
			FWFacade facade = ((ITransactionReferenceEvent)event).getFacade();
			String userName = ((ITransactionReferenceEvent)event).getUserName();
			
			Log.debug("Processing TransactionRef Event:- instrId:"+instrId+", TransRef:"+transRef);
			
			try {
				InstructionProcessApplied processApplied = InstructionProcessApplied.getActiveProcess(instrId, facade);

				InstructionProcessUtils.applyAction(
						processApplied, 
						InstructionAction.RETRY, 
						facade, 
						userName);	
				
				Log.debug("Finished Processing TransactionRef Event");
			} catch (DataException de) {
				Log.error("Cannot processTransactionReferenceEvent "+de.getMessage(), de);
			}
		}
	}
	
	/**
	 * Gets the suspend message unique to the module;
	 * @return
	 */
	public String getSuspendMessage() {
		return "Unable to process, instruction doesn't contain a transaction reference number";
	}

}
