package com.ecs.ucm.ccla.commproc.translation;

import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.DataType;
import com.ecs.ucm.ccla.data.instruction.InstructionDataFieldValue;
import com.ecs.ucm.ccla.data.instruction.UCMFieldTranslator;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Converts UCM account metadata fields into an Account ID.
 * 
 * @author Tom
 *
 */
public class AccountIdFieldHandler implements UCMFieldTranslator {

	public InstructionDataFieldValue getInstructionFieldValue
	 (LWDocument lwd, String ucmFieldName, FWFacade facade) throws DataException {
		
		String clientNumStr, accountNumStr;
		
		try {
			clientNumStr 	= lwd.getAttribute("xClientNumber");
			accountNumStr 	= lwd.getAttribute("xAccountNumber"); 
			
			return getAccountId(clientNumStr, accountNumStr, facade);
		} catch (Exception e) {
			throw new DataException("Unable to translate field: " + e.getMessage(), e);
		}
	}
	
	public InstructionDataFieldValue getInstructionFieldValue
	 (DataResultSet rs, String ucmFieldName, FWFacade facade) throws DataException {
		
		try {
			String clientNumStr 	= rs.getStringValueByName("xClientNumber");
			String accountNumStr 	= rs.getStringValueByName("xAccountNumber"); 
			
			return getAccountId(clientNumStr, accountNumStr, facade);
		} catch (Exception e) {
			throw new DataException("Unable to translate field: " + e.getMessage(), e);
		}
	}

	public static InstructionDataFieldValue getAccountId
	 (String clientNumStr, String accountNumStr, FWFacade facade)
     throws DataException {
		
		try {
			if (StringUtils.stringIsBlank(clientNumStr)
				||
				StringUtils.stringIsBlank(accountNumStr)) {
				return new InstructionDataFieldValue(DataType.INT);
			}
				
			Account account = Account.get(Account.getAccountByIndexingValues
			 (clientNumStr, accountNumStr, facade));
			
			Integer accountId = account.getAccountId();
			
			InstructionDataFieldValue fieldValue = new InstructionDataFieldValue
			 (DataType.INT);
			
			fieldValue.setIntValue(accountId);
			return fieldValue;
			
     	} catch (Exception e) {
			throw new DataException(e.getMessage(), e);
		}
	}
}
