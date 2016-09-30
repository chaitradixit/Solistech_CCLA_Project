package com.ecs.stellent.ccla.clientservices;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Vector;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.ClientProcess;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.DateFormatter;
import com.ecs.utils.stellent.embedded.FWFacade;

public class AMLUtils {
	
	/** Date when AML check procedure changed. After this date, all
	 *  new accounts had to undergo AML checks before being opened.
	 *  
	 *  So, any accounts opened before this date may still require
	 *  AML checking if the client has not been checked since.
	 */
	static Calendar REQ_AML_CHECKING_DATE;
	
	static {
		// 15th December 2007
		REQ_AML_CHECKING_DATE = new GregorianCalendar(2007, 11, 15);
	}
	
	/** Retrieves a list of accounts for a given client which are
	 *  not closed (i.e. open or frozen), which were opened before
	 *  a given date.
	 * @return
	 */
	public static DataResultSet getAMLAccounts
	 (int orgId, FWFacade facade) throws DataException {
		
		// Obtain AML checking date in UCM-friendly date format
		String requiredAMLCheckingDate = 
		 DateFormatter.getTimeStamp(REQ_AML_CHECKING_DATE.getTime());
		
		DataBinder binder = new DataBinder();
		
		Log.debug("AML: Fetching active accounts for Org ID: " 
		 + orgId + " opened prior to: " + requiredAMLCheckingDate);

		CCLAUtils.addQueryIntParamToBinder
		 (binder, "ORGANISATION_ID", orgId);
		
		binder.putLocal("maxDate", requiredAMLCheckingDate);
		
		DataResultSet amlAccounts = facade.createResultSet
		 ("qClientServices_GetActiveClientAccountsBeforeDate", binder);
		
		Log.debug("AML: Found " + amlAccounts.getNumRows() + " accounts."); 
		
		return amlAccounts;
	}
	
	/** Fetches all matching entries from the CCLA_ACCOUNT_AML_STATUS table
	 *  for the given client ID/company.
	 *  
	 *  Any existing rows returned indicate that the account has been
	 *  manually marked as having completed AML checking.
	 *  
	 * @param clientId
	 * @param company
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getAMLAccountsStatus
	 (int entityId, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "ORGANISATION_ID", entityId);
		
		// Fetch list of AML-checked accounts
		DataResultSet rsAmlChecked =
		 facade.createResultSet("qClientServices_GetAccountsAMLStatus", 
		 binder);
		
		return rsAmlChecked;
	}
	
	/** Determines whether all AML accounts for a particular client have been
	 *  manually marked as 'Completed' or not (i.e. completed AML checking)
	 *  
	 * @param clientId
	 * @param company
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static boolean isAMLAccountsCompleted
	 (int entityId, FWFacade facade) 
	 throws DataException {
		Log.debug("Checking if all AML accounts are complete for Entity: " +
		 entityId);
		
		// Fetch list of all AML accounts for client
		DataResultSet rsAmlAccounts = AMLUtils.getAMLAccounts
		 (entityId, facade);
		
		// Fetch list of AML-checked accounts for client
		DataResultSet rsAmlChecked = AMLUtils.getAMLAccountsStatus
		 (entityId, facade);
		
		if (rsAmlAccounts.isEmpty()) {
			Log.debug("No accounts found requiring AML check, returning true");
			return true;
		}
		
		if (rsAmlChecked.isEmpty()) {
			Log.debug("No accounts marked as completing AML checking, " +
			 "returning false");
			return false;
		}
		
		Vector<Account> accounts = new Vector<Account>();
		
		// Convert accounts ResultSet into CCLAAccount instances
		do {
			Account account = Account.get(rsAmlAccounts);
			accounts.add(account);
		} while (rsAmlAccounts.next());
		
		Iterator<Account> i = accounts.iterator();
		
		Log.debug("Checking " + accounts.size() + " for AML completion status");
		// Flip this flag to false for the first account found which wasn't
		// marked as completed.
		boolean allAccountsCompleted = true;
		
		// Cross-check list of AML accounts against completed accounts.
		while (i.hasNext() && allAccountsCompleted) {
			String accNumExt = i.next().getAccNumExt();
			boolean accountChecked = false;
			
			rsAmlChecked.first();
			
			do {
				String checkedAccNumExt =
				 rsAmlChecked.getStringValueByName("ACCNUMEXT");
				
				if (accNumExt.equals(checkedAccNumExt))
					accountChecked = true;
					
			} while (rsAmlChecked.next());
			
			if (accountChecked) {
				Log.debug("Account " + accNumExt + " was marked as complete.");
			} else {
				Log.debug("Account " + accNumExt + " was marked as incomplete.");
				allAccountsCompleted = false;
			}
		}
		
		Log.debug("AML account check finished. Returning " + allAccountsCompleted);
		return allAccountsCompleted;
	}
}
