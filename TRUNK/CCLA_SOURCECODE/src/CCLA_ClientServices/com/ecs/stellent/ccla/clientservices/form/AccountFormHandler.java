package com.ecs.stellent.ccla.clientservices.form;

import intradoc.common.ServiceException;
import intradoc.data.DataException;

import java.util.Hashtable;
import java.util.Vector;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.AuroraClient;
import com.ecs.ucm.ccla.data.Element;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Fund;
import com.ecs.ucm.ccla.data.form.Form;
import com.ecs.ucm.ccla.data.form.FormElementApplied;
import com.ecs.ucm.ccla.data.form.FormHandler;
import com.ecs.ucm.ccla.data.form.FormType;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Generic single-account form handler. 
 * 
 *  Will not generate forms - you need a more specific subclass to handle this.
 * 
 * @author Tom
 *
 */
public class AccountFormHandler extends FormHandler {
	
	public AccountFormHandler(Form form, String userId, FWFacade facade) {
		Log.debug("Initialized generic Account Form Handler");
		
		this.form = form;
		this.userId = userId;
		this.facade = facade;
	}
	
	public AccountFormHandler(String userId, FWFacade facade) {
		Log.debug("Initialized generic Account Form Handler");
		
		this.userId = userId;
		this.facade = facade;
	}
	
	/** Nothing special happens when one of these forms is returned.
	 * 
	 */
	@Override
	public void doPostCheckinActions(int docId) throws Exception {
		super.doPostCheckinActions(docId);
	}

	@Override
	public Hashtable<String, String> getDocMetaMapping() throws DataException {
		Hashtable<String, String> map = super.getDocMetaMapping();
		
		// Fetch mapped accounts.
		Vector<Account> accounts = FormElementApplied.getFormAccounts
		 (form.getFormId(), ElementType.ACCOUNT, facade);
		
		// Add data for single mapped account
		if (accounts.size() == 1) {
			Account account = accounts.get(0);
	
			addAccountDocMeta(map, account);
		
		} else {
			Log.warn("Incorrect number of mapped accounts to account form: " +
			 "expected 1, got " + accounts.size() + " (Form ID: " + form.getFormId() + 
			 ")");
		}
		
		return map;
	}
	
	@Override
	public Form generateForm(FormType formType, Element element, Integer investmentId) 
	 throws ServiceException, DataException {	
		throw new DataException("Can't generate forms using AccountFormHandler");
	}
	
	/** Appends the passed Hashtable with Account metadata values for the given
	 *  Account.
	 *  
	 *  Fixes the padding on the existing Client Number, if one is present.
	 *  
	 * @param map
	 * @param account
	 * @throws DataException 
	 */
	public static void addAccountDocMeta
	 (Hashtable<String, String> map, Account account) throws DataException {
		
		Fund fund = Fund.getCache().getCachedInstance(account.getFundCode());
		String accNumberStr = account.getAccountNumberStringWithPadding
		 (fund.getCompany());
		
		map.put("xAccountNumber", accNumberStr);
		map.put("xFund", account.getFundCode());
		map.put("xCompany", fund.getCompany().getCode());
		
		// Ensure padding is correct on the Client Number.
		String clientNumber = map.get(Globals.UCMFieldNames.ClientNumber);
		
		if (!StringUtils.stringIsBlank(clientNumber)) {
			// Convert to Integer and back to String to remove leading zeros.
			clientNumber = Integer.toString(Integer.parseInt(clientNumber));
			
			clientNumber = CCLAUtils.padClientNumber
			 (clientNumber, fund.getCompany());
		
			map.put(Globals.UCMFieldNames.ClientNumber, clientNumber);
		}
	}
}
