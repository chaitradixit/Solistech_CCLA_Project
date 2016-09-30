package com.ecs.ucm.ccla.commproc.translation;

import java.util.Vector;

import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.data.BankAccount;
import com.ecs.ucm.ccla.data.DataType;
import com.ecs.ucm.ccla.data.instruction.InstructionDataFieldValue;
import com.ecs.ucm.ccla.data.instruction.UCMFieldTranslator;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Converts UCM bank account metadata fields into a Bank Account ID, if a 
 *  corresponding Bank Account record exists in the system.
 *  
 * 
 * @author Tom
 *
 */
public class BankAccountIdFieldHandler implements UCMFieldTranslator {

	/**
	 *  Translates the xBankAccountNumber/xSortCode fields into a BANK_ACCOUNT_ID.
	 *  
	 *  @return null, if both values are missing, or the resolved BANK_ACCOUNT_ID
	 *  @throws DataException if one of the values is missing and the other present,
	 *  					or the 2 values don't correspond to a Bank Account on
	 *  					record.
	 */
	public InstructionDataFieldValue getInstructionFieldValue
	 (LWDocument lwd, String ucmFieldName, FWFacade facade) throws DataException {
		
		String accountNumStr, sortCodeStr;
		
		try {
			accountNumStr 	= lwd.getAttribute("xBankAccountNumber");
			sortCodeStr 	= lwd.getAttribute("xSortCode"); 
		} catch (Exception e) {
			throw new DataException("Unable to translate field: " + e.getMessage(), e);
		}
		
		return getBankAccountId(accountNumStr, sortCodeStr, facade);
	}
	
	public InstructionDataFieldValue getInstructionFieldValue
	 (DataResultSet rs, String ucmFieldName, FWFacade facade) throws DataException {

		String accountNumStr 	= rs.getStringValueByName("xBankAccountNumber");
		String sortCodeStr 		= rs.getStringValueByName("xSortCode"); 
		
		return getBankAccountId(accountNumStr, sortCodeStr, facade);
	}

	public static InstructionDataFieldValue getBankAccountId
	 (String accountNumStr, String sortCodeStr, FWFacade facade) throws DataException {
		
		if (StringUtils.stringIsBlank(accountNumStr)
			&&
			StringUtils.stringIsBlank(sortCodeStr)) {
			// No Bank Account data present on document.
			return new InstructionDataFieldValue(DataType.INT); 
		}
		
		if (StringUtils.stringIsBlank(accountNumStr)
			||
			StringUtils.stringIsBlank(sortCodeStr)) {
			// One of the two Bank Account fields is empty.
			throw new DataException("Incomplete Bank Account data, Account No. " +
			 "or Sort Code is missing");
		}
		
		// TODO: May need work to ensure interoperability with Building Soc. Numbers
		Vector<BankAccount> bankAccounts = BankAccount.getAllBySortCodeAndAccountNumber
		 (accountNumStr, sortCodeStr, facade);
		
		if (bankAccounts.isEmpty()) {
			throw new DataException("No bank account record with Account No: " +
			 accountNumStr + ", Sort Code: " + sortCodeStr);
		}

		Integer bankAccountId = bankAccounts.get(0).getBankAccountId();
		
		InstructionDataFieldValue fieldValue = new InstructionDataFieldValue
		 (DataType.INT);
		fieldValue.setIntValue(bankAccountId);
		
		return fieldValue;
	}
}
