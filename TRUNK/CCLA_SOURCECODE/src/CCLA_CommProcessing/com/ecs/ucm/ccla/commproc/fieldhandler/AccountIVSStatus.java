package com.ecs.ucm.ccla.commproc.fieldhandler;

import java.util.Vector;

import intradoc.data.DataException;

import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.DataType;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Account.IVSCheck;
import com.ecs.ucm.ccla.data.instruction.FieldValueHandler;
import com.ecs.ucm.ccla.data.instruction.Instruction;
import com.ecs.ucm.ccla.data.instruction.InstructionData;
import com.ecs.ucm.ccla.data.instruction.InstructionDataFieldValue;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Returns either true/false, depending on associated account IVS status.
 *  
 * @author Tom
 *
 */
public class AccountIVSStatus extends FieldValueHandler {

	/** Returns a value of 'true' if the mapped Account has passed IVS checks, false
	 *  otherwise.
	 *  
	 *  @return null if there was no mapped Account ID data field
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
		 new InstructionDataFieldValue(DataType.BOOL);
		
		Integer orgId = account.getOwnerOrganisationId(facade);
		Entity org = Entity.get(orgId, facade);
		
		IVSCheck check = account.getIVSCheck(org, facade);
		
		if (check.statusCode.equals(Account.IVSStatusCode.PASSED))
			fieldValue.setBoolValue(true);
		else
			fieldValue.setBoolValue(false);
		
		return fieldValue;
	}
}
