package com.ecs.ucm.ccla.commproc.translation;

import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.Company;
import com.ecs.ucm.ccla.data.DataType;
import com.ecs.ucm.ccla.data.Fund;
import com.ecs.ucm.ccla.data.instruction.InstructionDataFieldValue;
import com.ecs.ucm.ccla.data.instruction.UCMFieldTranslator;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.embedded.FWFacade;

public class DestAccountIdFieldHandler implements UCMFieldTranslator {

	
	/**
	 * Gets the destination account id from the lwdocument
	 */
	public InstructionDataFieldValue getInstructionFieldValue(LWDocument lwd,
			String ucmFieldName, FWFacade facade) throws DataException 
	{
		try {
			String destAccNumExt 	= lwd.getAttribute(Globals.UCMFieldNames.DestinationAccount);
			return getDestAccountId(destAccNumExt, facade);
		} catch (Exception e) {
			throw new DataException("Unable to translate field: " + e.getMessage(), e);
		}
	}

	public InstructionDataFieldValue getInstructionFieldValue(DataResultSet rs,
			String ucmFieldName, FWFacade facade) throws DataException 
	{
		try {
			String destAccNumExt 	= rs.getStringValueByName(Globals.UCMFieldNames.DestinationAccount);
			return getDestAccountId(destAccNumExt, facade);
		} catch (Exception e) {
			throw new DataException("Unable to translate field: " + e.getMessage(), e);
		}
	}

	/**
	 * Try to calculate the Destination AccountId from the AccNumExt
	 * @param destAccNumExt
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	private InstructionDataFieldValue getDestAccountId(String destAccNumExt, FWFacade facade)
	throws DataException {
		
		if (destAccNumExt!=null)
			destAccNumExt = destAccNumExt.trim();
		
		if (StringUtils.stringIsBlank(destAccNumExt))
			throw new DataException("Unable to translate field: Destination Account Number is null");
		
		Account destAccount = Account.get(destAccNumExt, facade);
		InstructionDataFieldValue fieldValue =  null;

		if (destAccount==null) {
			//Need to work out the account based on some rules
			String fundCode = CCLAUtils.getSuffixChars(destAccNumExt);
			
			if (!StringUtils.stringIsBlank(fundCode)) {
				Fund fund = Fund.getCache().getCachedInstance(fundCode);
				
				if (fund==null)
					throw new DataException("Unable to translate field: Cannot find fund with code "+fundCode);
				
				Company company = fund.getCompany();
				
				int clientNoPadding = company.getClientNumberPadding();
				int accNoPadding = company.getAccountNumberPadding();
				
				int totalLength = clientNoPadding + accNoPadding + fundCode.length();
				
				if (destAccNumExt.length()!=totalLength) {
					throw new DataException("Unable to translate field: Padding differs for company "+company.getCode());					
				} else {
					try {
						String clientNumStr = destAccNumExt.substring(0, clientNoPadding);
						String accountNumStr = destAccNumExt.substring(clientNoPadding);
						
						destAccount = Account.get(Account.getAccountByIndexingValues
								 (clientNumStr, accountNumStr, facade));
						
						if (destAccount==null) {
							throw new DataException("Unable to translate field: Cannot find Account with clientNum:"
									+clientNumStr+", accountNum:"+accountNumStr);
						}
					} catch (Exception e) {
						throw new DataException(e.getMessage(), e);
					}
				}
			}
			else
				throw new DataException("Unable to translate field: Cannot find fund code from Destination Account");
		} 
		
		//Populate the instructionDataFieldValue.
		if (destAccount!=null) {
			fieldValue = new InstructionDataFieldValue(DataType.INT);
			fieldValue.setIntValue(destAccount.getAccountId());	
		}
		return fieldValue;
	}
}
