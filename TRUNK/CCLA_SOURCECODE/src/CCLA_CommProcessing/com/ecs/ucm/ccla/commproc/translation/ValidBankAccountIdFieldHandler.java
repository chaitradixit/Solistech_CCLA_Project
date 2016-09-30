package com.ecs.ucm.ccla.commproc.translation;

import intradoc.common.ServiceException;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.Globals;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.BankAccount;
import com.ecs.ucm.ccla.data.DataType;
import com.ecs.ucm.ccla.data.Relation;
import com.ecs.ucm.ccla.data.RelationName;
import com.ecs.ucm.ccla.data.instruction.InstructionDataFieldValue;
import com.ecs.ucm.ccla.data.instruction.UCMFieldTranslator;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.embedded.FWFacade;

public class ValidBankAccountIdFieldHandler implements UCMFieldTranslator{

	public InstructionDataFieldValue getInstructionFieldValue(LWDocument lwd,
			String ucmFieldName, FWFacade facade) throws DataException {
	
		String clientNumStr , accNumStr, bankAccNumStr, sortCodeStr;
		
		try {
			clientNumStr 	= lwd.getAttribute(Globals.UCMFieldNames.ClientNumber);
			accNumStr 		= lwd.getAttribute(Globals.UCMFieldNames.AccountNumber);
			sortCodeStr 	= lwd.getAttribute(Globals.UCMFieldNames.SortCode);
			bankAccNumStr 	= lwd.getAttribute(Globals.UCMFieldNames.BankAccountNumber);			
		} catch (Exception e) {
			throw new DataException("Unable to translate field: " + e.getMessage(), e);
		}
		
		return getBankAccountNumber
		 (clientNumStr, accNumStr, sortCodeStr, bankAccNumStr, facade);
	}

	public InstructionDataFieldValue getInstructionFieldValue(DataResultSet rs,
			String ucmFieldName, FWFacade facade) throws DataException {
	
		String clientNumStr 	= rs.getStringValueByName(Globals.UCMFieldNames.ClientNumber);
		String accNumStr 		= rs.getStringValueByName(Globals.UCMFieldNames.AccountNumber);
		String bankAccNumStr 	= rs.getStringValueByName(Globals.UCMFieldNames.BankAccountNumber);
		String sortCodeStr 		= rs.getStringValueByName(Globals.UCMFieldNames.SortCode);
		
		return getBankAccountNumber
		 (clientNumStr, accNumStr, sortCodeStr, bankAccNumStr, facade);
	}
	
	/**
	 * Gets the bank account number
	 * @param clientNumStr
	 * @param accNumStr
	 * @param sortCodeStr
	 * @param bankAccNumStr
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	private InstructionDataFieldValue getBankAccountNumber(String clientNumStr,
	 String accNumStr, String sortCodeStr, String bankAccNumStr, FWFacade facade) 
	 throws DataException {
	
		BankAccount bankAccount = null;
		Account account = null;
		InstructionDataFieldValue fieldValue = null;
		
		try {
			account = Account.get(Account.getAccountByIndexingValues
				 (clientNumStr, accNumStr, facade));
		} catch (Exception se) {
			throw new DataException("Failed to resolve bank account, " +
			 "unable to resolve CCLA account: " + se.getMessage());
		}
		
		// Fetches the Instruction Data field value containing the corresponding
		// Bank Account ID. 
		// Will be empty if sort code and account number were null/empty.
		fieldValue = 
		 BankAccountIdFieldHandler.getBankAccountId(accNumStr, sortCodeStr, facade);
		
		if (!fieldValue.isEmpty()) {
			bankAccount = BankAccount.get(fieldValue.getIntValue(), facade);
			
			if (checkRelationship(account, bankAccount, facade)) {
				Integer bankAccountId = bankAccount.getBankAccountId();
				fieldValue = new InstructionDataFieldValue(DataType.INT);
				fieldValue.setIntValue(bankAccountId);
				
			} else {
				throw new DataException("Bank Account is not associated to the " +
				 "CCLA account");
			}
		} else {
			// Not sort code/account number to reference. Return empty InstrDataApplied.
		}
			
		return fieldValue;
	}
	
	/**
	 * Returns true if there is an Income or Withdrawal relationship between the given
	 * Account and Bank Account.
	 * 
	 * @param account
	 * @param bankAccount
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	private boolean checkRelationship
	 (Account account, BankAccount bankAccount, FWFacade facade) throws DataException {
		
		boolean exist = false;
		
		if (account==null || bankAccount==null) {
			Log.info("Account or Bank Account is null");
			return exist;
		}
		
		//check income relation
		exist = Relation.relationExists(account.getAccountId(), 
				bankAccount.getBankAccountId(), 
				RelationName.getCache().getCachedInstance(
						RelationName.AccountBankAccountRelation.INCOME), 
				facade);
		
		//check withdrawal relation
		if (!exist) {
			exist = Relation.relationExists(account.getAccountId(), 
					bankAccount.getBankAccountId(), 
					RelationName.getCache().getCachedInstance(
							RelationName.AccountBankAccountRelation.WITHDRAWAL), 
					facade);
		}
		
		return exist;
	}
	
}
