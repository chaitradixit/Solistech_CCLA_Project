package com.ecs.ucm.ccla.commproc.fieldhandler;

import java.util.Vector;

import intradoc.data.DataException;

import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.DataType;
import com.ecs.ucm.ccla.data.instruction.FieldValueHandler;
import com.ecs.ucm.ccla.data.instruction.Instruction;
import com.ecs.ucm.ccla.data.instruction.InstructionData;
import com.ecs.ucm.ccla.data.instruction.InstructionDataApplied;
import com.ecs.ucm.ccla.data.instruction.InstructionDataFieldValue;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Account Status, i.e. OPEN, FROZ, CLOS
 * 
 * @author Tom
 *
 */
public class AccountStatus extends FieldValueHandler {
	
	/** Returns the Account Status ID, e.g. 1
	 * 
	 *  @return null, if the instruction has no mapped Account ID field, or the mapped
	 *  		field is empty.
	 */
	@Override
	public InstructionDataFieldValue getValue(Instruction instr, FWFacade facade)
	 throws DataException {
		
		// Fetch Account ID value
		InstructionDataFieldValue accountIdFieldValue = 
		 instr.getDataApplied(InstructionData.Fields.SOURCE_ACCOUNT_ID);

		if (accountIdFieldValue == null || accountIdFieldValue.isEmpty()) {
			return null; // missing/empty Account ID field
		}
		
		Account account = Account.get(accountIdFieldValue.getIntValue(), facade);
		
		if (account == null)
			throw new DataException("Invalid Account ID: " + 
			 accountIdFieldValue.getIntValue());
		
		InstructionDataFieldValue fieldValue = 
		 new InstructionDataFieldValue(DataType.INT);
		
		fieldValue.setIntValue(account.getStatus());
		return fieldValue;
	}
}
