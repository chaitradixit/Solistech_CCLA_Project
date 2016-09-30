package com.ecs.ucm.ccla.commproc.translation;

import java.util.Vector;

import intradoc.common.Log;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.DataType;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.instruction.InstructionDataFieldValue;
import com.ecs.ucm.ccla.data.instruction.UCMFieldTranslator;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.embedded.FWFacade;

public class CorrespondentIdFieldHandler implements UCMFieldTranslator {

	public InstructionDataFieldValue getInstructionFieldValue(LWDocument lwd,
			String ucmFieldName, FWFacade facade) throws DataException {
		
		String clientNumStr, accountNumStr;
		
		try {
			clientNumStr 	= lwd.getAttribute("xClientNumber");
			accountNumStr 	= lwd.getAttribute("xAccountNumber"); 
			
			return getCorrespondentId(clientNumStr, accountNumStr, facade);
		} catch (Exception e) {
			throw new DataException("Unable to translate field: " + e.getMessage(), e);
		}
	}

	public InstructionDataFieldValue getInstructionFieldValue(DataResultSet rs,
			String ucmFieldName, FWFacade facade) throws DataException {
		try {
			String clientNumStr 	= rs.getStringValueByName("xClientNumber");
			String accountNumStr 	= rs.getStringValueByName("xAccountNumber"); 
			
			return getCorrespondentId(clientNumStr, accountNumStr, facade);
		} catch (Exception e) {
			throw new DataException("Unable to translate field: " + e.getMessage(), e);
		}
	}

	
	private static InstructionDataFieldValue getCorrespondentId( String clientNumStr, String accountNumStr, FWFacade facade) 
	throws DataException {
		try {
			if (StringUtils.stringIsBlank(clientNumStr) ||
				StringUtils.stringIsBlank(accountNumStr)) 
			{
				return new InstructionDataFieldValue(DataType.INT);
			}
				
			Account account = 
				Account.get(Account.getAccountByIndexingValues(clientNumStr, accountNumStr, facade));
			
			Person person = null;
			
			if (account!=null) {
				
				person = Account.getNominatedCorrespondent(account.getAccountId(), false, facade);
				
				//only interested in the 1 entry.
				if (person==null) {
					Log.warn("Cannot find any or too many correspondants for accountId:"+account.getAccountId());
				}
			} else {
				Log.warn("Cannot find account with indexing values clientNum:"+clientNumStr+", accountNum:"+accountNumStr);
			}

			InstructionDataFieldValue fieldValue = new InstructionDataFieldValue
			 (DataType.INT);
			
			if (person!=null)
				fieldValue.setIntValue(person.getPersonId());
			
			return fieldValue;
			
     	} catch (Exception e) {
			throw new DataException(e.getMessage(), e);
		}
	}
}
