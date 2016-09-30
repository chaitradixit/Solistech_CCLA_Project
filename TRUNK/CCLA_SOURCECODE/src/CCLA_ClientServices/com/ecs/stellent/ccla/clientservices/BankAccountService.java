package com.ecs.stellent.ccla.clientservices;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.aurora.webservice.BankDetails;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.CacheManager;
import com.ecs.ucm.ccla.aurora.AuroraWebServiceHandler;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.BankAccount;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.Globals;
import com.ecs.ucm.ccla.data.Relation;
import com.ecs.ucm.ccla.data.RelationName;
import com.ecs.ucm.ccla.data.RelationType;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.json.JsonUtils;
import intradoc.server.Service;

public class BankAccountService extends Service {
	
	/** Regexp used to validate Bank Account search strings before attempting
	 *  the search. Uses the format:
	 *  
	 *  <Bank Acc No.>[-<Sort Code>]
	 *  
	 *  Match #1 = Bank Account Number
	 *  Match #4 = Sort Code, if applicable
	 */
	static Pattern BANK_ACCOUNT_SEARCH_PATTERN = 
	 Pattern.compile("([\\d]{1,8})((\\-)([\\d]{1,6})?)?");
	
	/** Fetches data for a single Bank Account, based on the
	 *  BANK_ACCOUNT_ID in the binder.
	 *  
	 *  This includes data from the CCLA_BANK_ACCOUNT table,
	 *  plus a list of associated Accounts.
	 *  
	 * @throws DataException 
	 * @throws ServiceException 
	 *  
	 */
	public void getBankAccount() throws DataException, ServiceException {
		String bankAccountIdStr = m_binder.getLocal("BANK_ACCOUNT_ID"); 
			
		if (StringUtils.stringIsBlank(bankAccountIdStr))
			throw new ServiceException("Unable to fetch bank account: " +
			 " BANK_ACCOUNT_ID missing");
		
		int bankAccountId 	= Integer.parseInt(bankAccountIdStr);
		FWFacade facade 	= CCLAUtils.getFacade(m_workspace, true);
		
		DataResultSet rsBankAccount = 
		 BankAccount.getData(bankAccountId, facade);
		
		if (rsBankAccount.isEmpty())
			throw new ServiceException("No Bank Account found with ID: " 
			 + bankAccountId);
	
		m_binder.addResultSet("rsBankAccount", rsBankAccount);

		// List of related accounts.
		DataResultSet rsRelatedAccounts = 
		 Relation.getRelatedAccountsData
		 (bankAccountId, ElementType.BANK_ACCOUNT, facade);
		
		m_binder.addResultSet("rsRelatedAccounts", rsRelatedAccounts);
		
		RelationType relType = RelationType.getCache().getCachedInstance
		 (RelationType.ACCOUNT_BANKACCOUNT);
		
		// Account relations
		DataResultSet rsAccountRelations = 
		 Relation.getRelationData(null, bankAccountId, 
		 relType, null, facade);
		
		m_binder.addResultSet("rsAccountRelations", rsAccountRelations);
		
		// Bank/Branch details
		BankAccount bankAccount = BankAccount.get(rsBankAccount);
		
		try {
			m_binder.addResultSet
			 ("rsBankDetails", getBankDetailsResultSet(bankAccount.getSortCode()));
			
		} catch (Exception e) {
			Log.error("Failed to add rsBankDetails ResultSet", e);
		}
	}
	
	/** Fetches Bank/Branch details for a given Sort Code, via a web service provided
	 *  by Kainos.
	 *  
	 *  These are added to the binder in a ResultSet called rsBankDetails
	 * @throws ServiceException 
	 *  
	 */
	public void getBankDetails() throws ServiceException {
		
		String sortCode = m_binder.getLocal(BankAccount.Cols.SORT_CODE);
		
		if (StringUtils.stringIsBlank(sortCode))
			throw new ServiceException("Unable to perform bank details lookup: " +
			 "Sort Code missing");
		
		try {
			m_binder.addResultSet
			 ("rsBankDetails", getBankDetailsResultSet(sortCode));
			
		} catch (Exception e) {
			String msg = e.getMessage();
			
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}
	
	public static DataResultSet getBankDetailsResultSet(String sortCode) 
	 throws ServiceException {
		
		DataResultSet rsBankDetails = new DataResultSet( new String[] {
			"BANK_NAME", "BRANCH_NAME"	
		});
		
		try {
			// Fetch Bank Details via Aurora web service. If we are in dev/UAT, this
			// service will always return null.
			BankDetails bankDetails  = 
			 AuroraWebServiceHandler.getBankDetails(sortCode);
			
			Vector<String> v = new Vector<String>();
			
			if (bankDetails != null) {
				v.add(bankDetails.getBankName());
				v.add(bankDetails.getBranchTitle());
			} else {
				// Assume we are in dev/UAT environment. Insert test data instead.
				v.add("Test Bank Name (" + sortCode + ")");
				v.add("Test Branch Name  (" + sortCode + ")");
			}
			
			rsBankDetails.addRow(v);
			return rsBankDetails;
			
		} catch (Exception e) {
			String msg = "Unable to perform bank details lookup: " + e.getMessage();
			Log.error(msg, e);
			
			throw new ServiceException(msg, e);
		}
	}
	
	/** Special service method which places a single JSON string into the
	 *  binder, containing a serialized ResultSet of matched bank accounts.
	 *  
	 *  See the ContactLookup.doJsonContactLookup() for the reasoning of why
	 *  things are done this way, as opposed to just using the IsJson=1 switch.
	 *  
	 *  Used as the auto-complete lookup service when searching/selecting
	 *  existing bank accounts.
	 *  
	 *  Expects a single parameter in the DataBinder: 
	 *  BANK_ACCOUNT_SEARCH_STRING. 
	 *  This is expected in the following format:
	 *  
	 *  <Bank Account No.>[-<Sort Code>]
	 *  
	 *  The Bank Account No. portion will be stripped of all leading zeroes, 
	 *  before a substring search is executed to fetch all matching accounts.
	 * 
	 * @throws DataException 
	 */
	public void doJsonBankAccountSearch() throws ServiceException, DataException {
		
		String searchStr = m_binder.getLocal("BANK_ACCOUNT_SEARCH_STRING");
		
		if (StringUtils.stringIsBlank(searchStr)) {
			Log.debug("Aborting bank account search, no " +
			 "BANK_ACCOUNT_SEARCH_STRING parameter");
			return;
		}
		
		Matcher matcher = BANK_ACCOUNT_SEARCH_PATTERN.matcher(searchStr);

		if (!matcher.matches())
			throw new ServiceException("Invalid Bank Account search string. " +
			 "Please check format and try again.");
		
		String accountNo 	= matcher.group(1);
		String sortCode		= matcher.group(4); // optional, may be null.

		// Trim off leading zeroes.
		//accountNo = CCLAUtils.trimLeadingChars
		// (accountNo.trim(), '0');
	
		//Log.debug("Bank account search: " +
		// "Timmed off leading zeroes from account no: '" + 
		// searchStr + "', left with: '" + accountNo + "'");
		
		//if (accountNo.length() <= 2) {
		//	Log.debug("Aborting bank account search, trimmed " +
		//	 "ACCOUNT_NO parameter too short.");
		//	return;
		//}
		
		// Add wildcard character to front of account no. search parameter
		accountNo = accountNo + "%";
		
		m_binder.putLocal(BankAccount.Cols.ACCOUNT_NO, accountNo);
		
		CCLAUtils.addQueryIntParamToBinder
		 (m_binder, "numRows", Globals.AUTOSEARCH_BANK_ACCOUNT_NUMROWS);
		
		if (sortCode != null) {
			//sortCode = CCLAUtils.trimLeadingChars
			// (sortCode.trim(), '0');
			
			// Add wildcard character to front of sort code search parameter
			sortCode = sortCode + "%";
		} else {
			sortCode = "%";
		}
		
		Log.debug("Running Bank account search: " +
		 "Account no: '" + accountNo + "', Sort Code: '" + sortCode + "'");
		
		m_binder.putLocal(BankAccount.Cols.SORT_CODE, sortCode);

		FWFacade facade 	= CCLAUtils.getFacade(m_workspace, true);
		
		DataResultSet rsBankAccounts = 
		 facade.createResultSet
		 ("qClientServices_GetBankAccountsBySubstringValues", m_binder);
		
		Log.debug("Found " + rsBankAccounts.getNumRows() + " results (max: " + 
		 Globals.AUTOSEARCH_BANK_ACCOUNT_NUMROWS + ")");
		
		Vector<String> stringFields = new Vector<String>();
		stringFields.add(BankAccount.Cols.ACCOUNT_NAME);
		stringFields.add(BankAccount.Cols.BUILDING_SOCIETY_ROLL_NUMBER);
		stringFields.add(BankAccount.Cols.ACCOUNT_SHORT_NAME);
		
		// Clean the account names in the ResultSet so the returned JSON is
		// valid.
		CCLAUtils.removeResultSetValueChars
		 (rsBankAccounts, Globals.BAD_JSON_CHARS, stringFields);
		
		DataBinder jsonBinder = new DataBinder();
		
		jsonBinder.addResultSet("rsBankAccounts", rsBankAccounts);
		
		String jsonData = JsonUtils.binderToJsonString(jsonBinder);
		m_binder.putLocal("jsonData", jsonData);
	}
	
	public static void main(String[] args) {
		
		/*
		String accountNo = "2";
		
		int startingIndex = 0;
	
		while (startingIndex<accountNo.length() 
				 && accountNo.charAt(startingIndex) == '0') {
			startingIndex++;
		}
		
		System.out.println(startingIndex);
		accountNo = accountNo.substring(startingIndex, accountNo.length());
		System.out.println(accountNo);
		*/
		
		
		Matcher matcher = BANK_ACCOUNT_SEARCH_PATTERN.matcher("1234-1234");
		System.out.println(matcher.matches());
		
		Pattern x = 
		 Pattern.compile("([\\d]{1,8})((\\-)([\\d]{1,4})?)?");
		
		Matcher m1 = x.matcher("12345678-4321");
		System.out.println(m1.matches());
		System.out.println(m1.group(1));
		System.out.println(m1.group(2));
		System.out.println(m1.group(3));
		System.out.println(m1.group(4));
		//System.out.println(m1.group(6));
		
		System.out.println(m1.groupCount());
		
		Matcher m2 = x.matcher("123456-");
		System.out.println(m2.matches());
		System.out.println(m2.group(1));
		System.out.println(m2.group(2));
		System.out.println(m2.group(3));
		System.out.println(m2.group(4));
		System.out.println(m2.groupCount());
	}
	
	/** Adds a new bank account. Requires ACCOUNT_NO and SORT_CODE in
	 *  the DataBinder. ACCOUNT_NAME and BUILDING_SOCIETY_NUMBER are optional.
	 * 
	 * @throws DataException
	 * @throws ServiceException
	 * @throws DataException 
	 */
	public void addBankAccount() throws ServiceException, DataException {
		
		FWFacade facade 		= CCLAUtils.getFacade(m_workspace, true);

		try {
			facade.beginTransaction();
			
			BankAccount bankAccount = addOrCloneBankAccount
			 (m_binder, facade, m_userData.m_name);
		
			Log.debug("Added new bank account ID: " + bankAccount.getBankAccountId());
		
			facade.commitTransaction();
			
			String redirectUrl = m_binder.getLocal("RedirectUrl");
			
			if (!StringUtils.stringIsBlank(redirectUrl)) {
				redirectUrl += bankAccount.getBankAccountId();
				m_binder.putLocal("RedirectUrl", redirectUrl);
			}
			
		} catch (Exception e) {
			facade.rollbackTransaction();
			
			String msg = "Unable to add bank account: " + e.getMessage();
			
			Log.error(msg);
			throw new ServiceException(msg);
		}
	}
	
	/** Adds a brand new bank account, or clones an existing one with a user-supplied
	 *  Account Name/Building Soc. No/Short Name.
	 *  
	 *  If a BANK_ACCOUNT_ID is not present in the binder, a brand new bank account is
	 *  generating, with all field values supplied from the passed binder.
	 *  
	 *  If it is present, and a special flag is too (addNewBuildingSocAccountCheck), then
	 *  the referenced Bank Account is cloned, with various label fields supplied from
	 *  the passed binder.
	 * 
	 *  
	 * @param binder
	 * @param facade
	 * @param userName
	 * @return
	 * @throws DataException
	 * @throws ServiceException 
	 * @throws DataException 
	 */
	private static BankAccount addOrCloneBankAccount
	 (DataBinder binder, FWFacade facade, String userName) 
	 throws ServiceException, DataException {
		
		// Get the bank account id from binder
		Integer bankAccId = CCLAUtils.getBinderIntegerValue
		 (binder, BankAccount.Cols.ID);
		
		// Look for the flag indicating that an existing Bank Account record must
		// be 'cloned' with a distinct Building Society Reference number
		boolean addNewBuildingSocietyAccount = CCLAUtils.getBinderBoolValue
		 (binder, "addNewBuildingSocAccountCheck");

		if (bankAccId == null) {
			
			// Need to create a new bank account from scratch.
			BankAccount bankAccount = BankAccount.add(binder, facade, userName);
			
			Log.debug("Generated new bank account with ID " 
			 + bankAccount.getBankAccountId());
			
			return bankAccount;
			
		} else if (addNewBuildingSocietyAccount) {
			// Clone existing account.
			DataBinder addBinder = new DataBinder();
			
			BankAccount existingBankAccount = BankAccount.get(bankAccId, facade);
			
			if (existingBankAccount == null) {
				String msg = "Unable to clone bank account, no existing bank account " +
				 "with ID " + bankAccId;
				
				Log.error(msg);
				throw new ServiceException(msg);
			}
			
			existingBankAccount.addFieldsToBinder(addBinder);
			
			// Write user-editable fields over the top.
			CCLAUtils.addQueryParamToBinder
			 (addBinder, BankAccount.Cols.BUILDING_SOCIETY_ROLL_NUMBER, 
			 binder.getLocal(BankAccount.Cols.BUILDING_SOCIETY_ROLL_NUMBER));
			
			CCLAUtils.addQueryParamToBinder
			 (addBinder, BankAccount.Cols.ACCOUNT_NAME, 
			 binder.getLocal(BankAccount.Cols.ACCOUNT_NAME));
			
			CCLAUtils.addQueryParamToBinder
			 (addBinder, BankAccount.Cols.ACCOUNT_SHORT_NAME, 
			 binder.getLocal(BankAccount.Cols.ACCOUNT_SHORT_NAME));
			
			Log.debug("Cloning existing bank account with ID " 
			 + existingBankAccount.getBankAccountId());
			
			// need to create a new bank account
			BankAccount bankAccount = BankAccount.add(addBinder, facade, userName);
			
			Log.debug("Generated cloned bank account with ID " 
			 + bankAccount.getBankAccountId());
			
			return bankAccount;
			
		} else {
			String msg = "Unable to add/clone bank account, parameters missing";
			
			Log.error(msg);
			throw new DataException(msg);
		}
	}
	
	/** Updates an existing bank account.
	 * 
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void updateBankAccount() throws DataException, ServiceException {
		String bankAccountIdStr = m_binder.getLocal("BANK_ACCOUNT_ID"); 
		
		if (StringUtils.stringIsBlank(bankAccountIdStr))
			throw new ServiceException("Unable to fetch bank account: " +
			 " BANK_ACCOUNT_ID missing");
		
		int bankAccountId 	= Integer.parseInt(bankAccountIdStr);
		FWFacade facade 	= CCLAUtils.getFacade(m_workspace, true);
		
		BankAccount bankAccount = BankAccount.get(bankAccountId, facade);
		if (bankAccount == null)
			throw new ServiceException("Unable to update bank account with " +
			 "ID: " + bankAccountId + " (not found)");
		
		bankAccount.setAttributes(m_binder);
		// user needed for audit table
		String username = m_userData.m_name;
		
		try {
			bankAccount.persist(facade, username);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage(), e);
		}
		
		Log.debug("Updated bank account ID: " + bankAccount.getBankAccountId());
	}
	
	/** Adds relations between a given Account and Bank Account.
	 * 
	 *  If a BANK_ACCOUNT_ID is present in the binder, we are linking to an existing
	 *  Bank Account record. If not, a new bank account record must be generated first,
	 *  taking all its values from the binder.
	 * 
	 *  If the addNewBuildingSocAccountCheck flag is present, a new Bank Account record
	 *  must be generated by cloning the one referenced in the binder, with a distinct
	 *  Building Society Number.
	 * 
	 * @throws DataException 
	 * @throws ServiceException 
	 *  
	 */
	public void addRelation() throws DataException, ServiceException {
		
		String username = m_userData.m_name;
		
		// Grab the Account ID we are linking the Bank Account to
		Integer accountId = CCLAUtils.getBinderIntegerValue(m_binder, Account.Cols.ID);
		
		if (accountId == null)
			throw new ServiceException
			 ("Cannot create a bank account without a parent account");
		
		// Get the bank account id from binder
		Integer bankAccId = CCLAUtils.getBinderIntegerValue
		 (m_binder, BankAccount.Cols.ID);

		// Look for the flag indicating that an existing Bank Account record must
		// be 'cloned' with a distinct Building Society Reference number
		boolean addNewBuildingSocietyAccount = CCLAUtils.getBinderBoolValue
		 (m_binder, "addNewBuildingSocAccountCheck");
		
		FWFacade facade 	= CCLAUtils.getFacade(m_workspace, true);
		
		Account account = Account.get(accountId, facade);
		BankAccount bankAccount = null;
		
		try {
			facade.beginTransaction();
			
			if ((bankAccId == null)
				||
				(bankAccId != null && addNewBuildingSocietyAccount)) {
		
				bankAccount = addOrCloneBankAccount
				 (m_binder, facade, m_userData.m_name);
				
			} else {
				// Fetch existing Bank Account
				bankAccount = BankAccount.get(bankAccId, facade);
			}
			
			Integer newRelationId = null;
			
			if (bankAccount != null) {
				// create Account - Bank Account relations
				if (!StringUtils.stringIsBlank(m_binder.getLocal("WITHDRAWAL")))
				{
					// create withdrawal relationship
					RelationName relName = RelationName.getCache().getCachedInstance
					 (RelationName.AccountBankAccountRelation.WITHDRAWAL);
					
					Relation withRelation = Relation.add
					 (account.getAccountId(), bankAccount.getBankAccountId(),
					  relName, facade, username);
					
					Log.debug("created withdrawal relation with id " 
					 + withRelation.getRelationId());
					
					newRelationId = withRelation.getRelationId();
				}
				// create an Account - Bank Account relation
				if (!StringUtils.stringIsBlank(m_binder.getLocal("INCOME")))
				{
					// create withdrawal relationship
					RelationName relName = RelationName.getCache().getCachedInstance
					 (RelationName.AccountBankAccountRelation.INCOME);
					
					Relation incRelation = Relation.add
					 (account.getAccountId(), bankAccount.getBankAccountId(),
					  relName, facade, username);
					
					Log.debug("created income relation with id " 
					 + incRelation.getRelationId());
					
					newRelationId = incRelation.getRelationId();
				}			
			} else {
				String msg = "Unable to add Bank Account relations, " +
				 "Bank Account ID missing ";
				
				Log.error(msg);
				throw new ServiceException(msg);
			}
			
			CCLAUtils.appendToBinderParam(m_binder, 
			 "RedirectUrl", "&newRelationId=" + newRelationId);
			
			facade.commitTransaction();
			
		} catch (Exception e) {
			facade.rollbackTransaction();
			
			String msg = "Unable to add Bank Account relation: " + e.getMessage();
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}	
}
