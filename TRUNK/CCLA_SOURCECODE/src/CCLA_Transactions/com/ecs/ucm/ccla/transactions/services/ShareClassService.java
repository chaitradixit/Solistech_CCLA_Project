package com.ecs.ucm.ccla.transactions.services;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.aurora.AuroraAccountHandler;
import com.ecs.ucm.ccla.aurora.AuroraAccountUtils;
import com.ecs.ucm.ccla.aurora.AuroraShareClassUtils;
import com.ecs.ucm.ccla.aurora.AuroraWebServiceHandler;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.AccountValue;
import com.ecs.ucm.ccla.data.Company;
import com.ecs.ucm.ccla.data.Fund;
import com.ecs.ucm.ccla.data.ShareClass;
import com.ecs.ucm.ccla.data.ShareClassGroup;
import com.ecs.ucm.ccla.data.ShareClassOverride;
import com.ecs.ucm.ccla.data.instruction.Instruction;
import com.ecs.ucm.ccla.data.instruction.InstructionData;
import com.ecs.ucm.ccla.data.instruction.InstructionDataApplied;
import com.ecs.ucm.ccla.data.instruction.InstructionGlobals;
import com.ecs.ucm.ccla.transactions.globals.TransactionGlobals;
import com.ecs.ucm.ccla.utils.DateUtils;
import com.ecs.ucm.ccla.utils.ShareClassUtils;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;


import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.server.Service;

public class ShareClassService extends Service {

	
    /**
     * Gets share class record from database from SHARE_CLASS_ID passed to binder
     * 
     * @return void but adds dataresultset rsShareClass to binder if share class exists
     * @throws  ServiceException
     */	
	public void getShareClassBasic() throws ServiceException
	{
		String strShareClassId = CCLAUtils.getBinderStringValue(m_binder, "SHARE_CLASS_ID");
		if (StringUtils.stringIsBlank(strShareClassId))
			throw new ServiceException("Missing SHARE_CLASS_ID");
		
		try {
			
			int shareClassId = CCLAUtils.getBinderIntegerValue(m_binder, "SHARE_CLASS_ID");		
			FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
			DataResultSet rsShareClass = ShareClass.getData(shareClassId, facade);
			if (!rsShareClass.isEmpty())
				m_binder.addResultSet("rsShareClass", rsShareClass);			
			
		} catch (DataException e) {
			String msg = "Unable to get Share Class: " + e.getMessage();
			Log.error(msg, e);		
			throw new ServiceException(msg, e);
		}
	}

    /**
     * Gets any override or share class grouping for an account
     * 
     * @return void but adds dataresultset rsShareClass to binder if share class exists
     * @throws  ServiceException
     * @throws DataException 
     */	
	public void getAccountShareClassDetail() throws ServiceException, DataException
	{

		try {	
			Integer accountId = CCLAUtils.getBinderIntegerValue(m_binder, "ACCOUNT_ID");
			if (accountId == null)
				throw new ServiceException("Missing ACCOUNT_ID");			
			FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
			
			// get share class override if it exists
			ShareClassOverride sco = ShareClassOverride.get(accountId, facade);
			if (sco != null)
			{
				int scoId = sco.getShareClassId();
				CCLAUtils.addQueryIntParamToBinder(m_binder, "SHARE_CLASS_OVERRIDE_ID", scoId);
				ShareClass overrideClass = ShareClass.get(scoId, facade);
				CCLAUtils.addQueryParamToBinder(m_binder, "SHARE_CLASS_OVERRIDE_NAME", overrideClass.getShareClassName());
			}
			
			// get share class group if it exists
			ShareClassGroup group = ShareClassGroup.getByAccountId(accountId, facade);
			if (group != null)
			{
				int groupId = group.getGroupId();
				CCLAUtils.addQueryIntParamToBinder(m_binder, "SHARE_CLASS_GROUP_ID", groupId);
				CCLAUtils.addQueryParamToBinder(m_binder, "SHARE_CLASS_GROUP_NAME", group.getGroupName());				
			}
			
		} catch (DataException e) {
			String msg = "Unable to get Share Class details: " + e.getMessage();
			Log.error(msg, e);		
			throw new ServiceException(msg, e);
		}
	}
	
	
    /**
     * updates a share class with values passed to binder
     * 
     * @return void 
     * @throws  ServiceException, DataException
     */	
	public void updateShareClass() throws ServiceException, DataException {
		
		String strShareClassId = CCLAUtils.getBinderStringValue(m_binder, "SHARE_CLASS_ID");
		if (StringUtils.stringIsBlank(strShareClassId))
			throw new ServiceException("Missing SHARE_CLASS_ID");
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		try {
			
			int shareClassId = CCLAUtils.getBinderIntegerValue(m_binder, "SHARE_CLASS_ID");		
			
			facade.beginTransaction();
			ShareClass shareClass = ShareClass.get(shareClassId, facade);
			shareClass.setAttributes(m_binder);
			String username = m_userData.m_name;
			m_binder.putLocal("USER_ID", username);	
			shareClass.persist(facade, username);
			facade.commitTransaction();
			
		} catch (DataException e) {
			String msg = "Unable to update Share Class: "+strShareClassId + "," + e.getMessage();
			Log.error(msg, e);		
			facade.rollbackTransaction();
			throw new ServiceException(msg, e);
		}
	}


    /**
     * adds a new share class with values passed to binder
     * 
     * @return void 
     * @throws  ServiceException, DataException
     */	
	public void addShareClass() throws ServiceException, DataException {
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		try {
			facade.beginTransaction();
			String username = m_userData.m_name;
			ShareClass shareClass = ShareClass.add(m_binder, facade, username);
			CCLAUtils.appendToBinderParam(m_binder, "RedirectUrl", Integer.toString(shareClass.getShareClassId()));
			facade.commitTransaction();
			
		} catch (DataException e) {
			String msg = "Unable to create new Share Class: " + e.getMessage();
			Log.error(msg, e);		
			facade.rollbackTransaction();
			throw new ServiceException(msg, e);
		}
	}	
	
	
	 /**
     * Gets all share classes from ref_share_class table
     * 
     * @return void but adds dataresultset rsShareClass to binder if share class exists
     * @throws  ServiceException
     */	
	public void getShareClassListing() throws ServiceException
	{		
		try {
			FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
			DataResultSet rsShareClasses = facade.createResultSet("qTransactions_GetShareClassListing", m_binder);
			if (!rsShareClasses.isEmpty())
				m_binder.addResultSet("rsShareClasses", rsShareClasses);			
			
		} catch (DataException e) {
			String msg = "Unable to get Share Class: " + e.getMessage();
			Log.error(msg, e);		
			throw new ServiceException(msg, e);
		}
	}
	
	 /**
     * gets the expected share class of an account based on account balance passed to method
     * 
     * @param int accountId
     * @param String cash value of account
     * @param FWFacade facade
     * @return Integer - the share_class_id 
     * @throws  ServiceException, DataException
     */	
	public static Integer getExpectedShareClass(int accountId, String cashValue, FWFacade facade) 
	throws DataException
	{
		Integer finalShareClass = null;
		boolean hasMatchedShareClass = false;
		BigDecimal shareClassGroupBalance = new BigDecimal("0");
		
		Log.info("Checking account id: " + accountId + ", with expected closing account balance:" + cashValue);		
		Account account = Account.get(accountId, facade);

		
		// Get fund code for account
		String fundCode = account.getFundCode();
		Log.debug("fundcode is " + fundCode);	
		
		// is account part of a share class group?
		ShareClassGroup scGroup = ShareClassGroup.getByAccountId(accountId, facade);
		if (scGroup != null)
		{
			// does this group have an override?
			if (scGroup.isOverridden())
			{
				finalShareClass = scGroup.getShareClassId();
				Log.debug("using overridden share class value of " + finalShareClass);
				hasMatchedShareClass = true;
			} else
			{
				Log.debug("using share class group total cash value of " + scGroup.getTotalCash());
				// use the total cash from the share class group as the account balance
				if (scGroup.getTotalCash() != null)
					shareClassGroupBalance = new BigDecimal(scGroup.getTotalCash());
			}
		}
		
		
		// is share class value overridden?
		if (!hasMatchedShareClass)			
		{		
			Integer overrideShareClass = ShareClass.getShareClassOverride(accountId, facade);
			if (overrideShareClass!=null)
			{
				hasMatchedShareClass=true;
				ShareClass shareClass = ShareClass.get(overrideShareClass, facade);
				finalShareClass = shareClass.getShareClassId();
				Log.info("Found override shareclass with value:" + finalShareClass);
			}
		}
		//TODO check custom eligibility criteria as this will override next check (if present)
		
		// if no previous match then look at share class min thresholds to get share class
		boolean inRange = false;
		// rsShareClasses is ordered list (by min_threshold)
		if (!hasMatchedShareClass)			
		{
			// get share classes for this fund code
			DataResultSet rsShareClasses = ShareClass.getEnabledShareClassesWithMovementByFund(fundCode, facade);
			if (rsShareClasses!= null)
			{
				Log.info("Found " + rsShareClasses.getNumRows() + " share classes for fund:" + fundCode); 
				// get accountBalance as BigDecimal
				BigDecimal accountBalance = new BigDecimal(cashValue);
				// if shareClassGroupBalance has been set then use this value instead
				if (shareClassGroupBalance.compareTo(BigDecimal.ZERO)!=0)
					accountBalance = shareClassGroupBalance;
				do {
					int shareClassId = CCLAUtils.getResultSetIntegerValue(rsShareClasses, "SHARE_CLASS_ID");
					BigDecimal threshold = new BigDecimal(CCLAUtils.getResultSetStringValue(rsShareClasses, "MIN_THRESHOLD"));
					Log.debug("checking share classs id " + shareClassId + " against threshold:" + threshold);
					// -1 is less than, 0 is equals, 1 is greater than account balance
					if (threshold.compareTo(accountBalance)!=1)
					{
						inRange = true;
						Log.debug("threshold is less than accountBalance");
						finalShareClass=shareClassId;
					} else {
						Log.debug("threshold is greater than or equal to accountBalance");
					}
						
				} while (rsShareClasses.next());
			}
		}
		
		return finalShareClass;
	}	
	
	/**
	 * share class calculator
	 */
	public void calculateExpectedShareClass() throws DataException, ServiceException {
		
		//Expects client number
		String clientNumStr = CCLAUtils.getBinderStringValue(m_binder, "ClientNo");
		//Expects account number
		String accountNumStr = CCLAUtils.getBinderStringValue(m_binder, "AccountNo");
		//CDB Account Number
		Integer accountId = CCLAUtils.getBinderIntegerValue(m_binder, Account.Cols.ID);
		
		//speculative account value or 
		String expectedValue = CCLAUtils.getBinderStringValue(m_binder, "ExpectedValue");
		
		if (accountId==null &&
				StringUtils.stringIsBlank(clientNumStr) 
				&& StringUtils.stringIsBlank(accountNumStr)) {
			throw new DataException("Cannot calculate, Client and Account Number or AccountID is empty");
		}
		
		FWFacade facade = CCLAUtils.getFacade(true);
		Account account = null;
		
		if (accountId!=null) {
			account = Account.get(accountId, facade);
		}
		
		if (account==null && !StringUtils.stringIsBlank(clientNumStr) 
				&& !StringUtils.stringIsBlank(accountNumStr)) {
			account = Account.get(
						Account.getAccountByIndexingValues(clientNumStr, accountNumStr, false, facade));
		} else {
			throw new DataException("Cannot calculate, Client and Account Number is empty");			
		}
		
		if (account==null)
			throw new DataException("find account");			
		
		if (StringUtils.stringIsBlank(expectedValue)) {
			expectedValue = Account.getAccountCash(account.getAccountId(), facade);
		}
		
		Integer expectedShareClass = ShareClassUtils.getExpectedShareClass(account.getAccountId(), expectedValue, facade);
		
		CCLAUtils.addQueryParamToBinder(
				m_binder,
				"expectedShareClass", 
				expectedShareClass==null?"Not Applicable":String.valueOf(expectedShareClass));
		
	}
	
	
	/**
	 * Report to show cdb shareclass against account 
	 * with projected closing balance for today against aurora share class.
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void showShareClassDiscrepencies() throws DataException, ServiceException 
	{	
		HashMap<Integer, ShareClassAccountInfo> shareClassAccountInfoMap = 
			new HashMap<Integer, ShareClassAccountInfo>();

		Vector<Instruction> transactionVec = new Vector<Instruction>();
		Vector<Integer> currentIdsVec = new Vector<Integer>();
		FWFacade facade = CCLAUtils.getFacade(true);
		
		//1. Get list of current account values, shareclass, aurora client number and account number. 
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryParamToBinder(binder, "FUND_CODE", TransactionGlobals.PSDF_FUND_CODE);
		CCLAUtils.addQueryDateParamToBinder(binder, "PROCESS_DATE", DateUtils.getNow());
		
		//Query will return account details (including current shareclass, account value. 
		DataResultSet rsShareClassAccounts = facade.createResultSet("qTransactions_GetCurrentAccountShareClassValues", binder);
		
		if (rsShareClassAccounts!=null && !rsShareClassAccounts.isEmpty()) {
			
			do {
				ShareClassAccountInfo info = new ShareClassAccountInfo();
				info.accountId = CCLAUtils.getResultSetIntegerValue(rsShareClassAccounts, Account.Cols.ID);
				info.accountNumber = CCLAUtils.getResultSetIntegerValue(rsShareClassAccounts, Account.Cols.ACCOUNTNUMBER);
				info.fundCode = CCLAUtils.getResultSetStringValue(rsShareClassAccounts, Account.Cols.FUND_CODE);
				info.isEarlyInvestor = CCLAUtils.getResultSetBoolValue(rsShareClassAccounts, "EARLY_INVESTOR");
				info.isSeedFunder = CCLAUtils.getResultSetBoolValue(rsShareClassAccounts, "SEED_FUNDER");
				info.isInternalAccount = CCLAUtils.getResultSetBoolValue(rsShareClassAccounts, "INTERNAL_ACCOUNT");
				
				try {
					info.currentUnits = 
						CCLAUtils.getResultSetBigDecimalValue(rsShareClassAccounts, "ACC_UNITS");
					
					if (info.currentUnits==null)
						info.currentUnits = BigDecimal.ZERO;
					
					info.closingUnits = info.currentUnits;
				} catch (Exception e) { 
					info.currentUnits = BigDecimal.ZERO; 
					info.closingUnits = info.currentUnits;
				}
				
				try {
					info.currentShareClass = 
						CCLAUtils.getResultSetIntegerValue(rsShareClassAccounts, Account.Cols.SHARE_CLASS);
				} catch (Exception e) {
					info.currentShareClass = null;
				}
				
				try {
					info.overrideShareClass = 
						CCLAUtils.getResultSetIntegerValue(rsShareClassAccounts, "OVERRIDE_SHARE_CLASS_ID");
				} catch (Exception e) {
					info.overrideShareClass = null;
				}
				 shareClassAccountInfoMap.put(info.accountId, info);
				
			} while (rsShareClassAccounts.next());
		}
		
		//Get todays transactions and work out share classes for each account.
		DataResultSet rsDestTransactionToday = 
			facade.createResultSet("qTransactions_GetDestTransactionsByProcessDateFund", binder);
		DataResultSet rsTransactionToday = 
			facade.createResultSet("qTransactions_GetTransactionsByProcessDateFund", binder);

		if (rsTransactionToday!=null && !rsTransactionToday.isEmpty())
		{ 
			do {
				Instruction trans = Instruction.get(rsTransactionToday);
				Log.debug("Adding SourceAcc Instruction for processing:"+trans.getInstructionId());
				transactionVec.add(trans);
				currentIdsVec.add(trans.getInstructionId());
			} while (rsTransactionToday.next());
		}
		// look for transactions where pc account is in destination field
		if (rsDestTransactionToday!=null && !rsDestTransactionToday.isEmpty())
		{
			do {
				Instruction trans = Instruction.get(rsDestTransactionToday);
				if (!currentIdsVec.contains(trans.getInstructionId())) {
					Log.debug("Adding DestAcc Instruction for processing:"+trans.getInstructionId());					
					transactionVec.add(trans);
					currentIdsVec.add(trans.getInstructionId());
				} else {
					Log.debug("Not Adding DestAcc Instruction for processing:"+trans.getInstructionId());

				}
			} while (rsDestTransactionToday.next());
		}
		
		//process transactions if any!
		if (transactionVec!=null && !transactionVec.isEmpty()) {
			
			for (Instruction instr : transactionVec) {
				int typeId = instr.getType().getTransactionType().getTransactionTypeId();
				Integer accId = null;
				Integer destAccId = null;
				BigDecimal cash = BigDecimal.ZERO;
				BigDecimal units = BigDecimal.ZERO;
				ShareClassAccountInfo info = null;				
				
				//Get Source Account ID
				InstructionDataApplied accIdDataApplied = InstructionDataApplied.getDataApplied(
						instr.getInstructionId(), InstructionData.Fields.SOURCE_ACCOUNT_ID, facade);
				if (accIdDataApplied==null || accIdDataApplied.getDataFieldValue().isEmpty()) {
					//account id must not be null, continue if this happens
					continue;
				} else {
					accId = accIdDataApplied.getDataFieldValue().getIntValue(); 	
				}
				
				//Check the source account
				if (!shareClassAccountInfoMap.containsKey(accId)) {
					continue;
				}
					
				if (typeId==InstructionGlobals.TRANSACTION_TYPE_TRANSFER_ID) {
					
					InstructionDataApplied destAccIdDataApplied = InstructionDataApplied.getDataApplied(
							instr.getInstructionId(), InstructionData.Fields.DEST_ACCOUNT_ID, facade);
					if (destAccIdDataApplied==null || destAccIdDataApplied.getDataFieldValue().isEmpty()) {
						//account id must not be null, continue if this happens
						continue;
					} else {
						destAccId = destAccIdDataApplied.getDataFieldValue().getIntValue(); 	
					}
					
					//check the dest Acc
					if (!shareClassAccountInfoMap.containsKey(destAccId)) {
						continue;
					}
					
					InstructionDataApplied unitsDataApplied = InstructionDataApplied.getDataApplied(
							instr.getInstructionId(), InstructionData.Fields.UNITS, facade);
					if (unitsDataApplied==null || unitsDataApplied.getDataFieldValue().isEmpty()) {
						continue;
					} else {
						units =  unitsDataApplied.getDataFieldValue().getBigDecimalValue();
					}
					
					//deduct units from source acc
					info = shareClassAccountInfoMap.get(accId);
					info.closingUnits = info.closingUnits.subtract(units);					
					shareClassAccountInfoMap.put(accId, info);
					
					//increase units to dest acc
					info = shareClassAccountInfoMap.get(destAccId);
					info.closingUnits = info.closingUnits.add(units);
					shareClassAccountInfoMap.put(destAccId, info);					
					
				} else if (typeId==InstructionGlobals.TRANSACTION_TYPE_BUY_ID ||
						typeId==InstructionGlobals.TRANSACTION_TYPE_SELL_ID) 
				{
					InstructionDataApplied cashDataApplied = InstructionDataApplied.getDataApplied(
							instr.getInstructionId(), InstructionData.Fields.CASH, facade);
					if (cashDataApplied==null || cashDataApplied.getDataFieldValue().isEmpty()) {
						continue;
					} else {
						cash =  cashDataApplied.getDataFieldValue().getBigDecimalValue();
					}
					
					info = shareClassAccountInfoMap.get(accId);
					
					if (typeId==InstructionGlobals.TRANSACTION_TYPE_BUY_ID) {
						info.closingUnits = info.closingUnits.add(cash);
					} 
					
					if (typeId==InstructionGlobals.TRANSACTION_TYPE_SELL_ID) {
						info.closingUnits = info.closingUnits.subtract(cash);						
					} 
					shareClassAccountInfoMap.put(accId, info);
				}
			}
		}
		
		//Workout Aurora shareclasses
		if (rsShareClassAccounts!=null && !rsShareClassAccounts.isEmpty()) {
			
			if (rsShareClassAccounts.first()) {
				do {
					Integer accountId = null;
					ShareClassAccountInfo info = null;
					try {
						accountId = CCLAUtils.getResultSetIntegerValue(rsShareClassAccounts, Account.Cols.ID);
						
						if (accountId!=null && accountId.intValue()!=0) {
							info = shareClassAccountInfoMap.get(accountId);
							 Integer expectedSC = ShareClassUtils.getExpectedShareClass(info.accountId, info.closingUnits.toString(), facade);
							 info.expectedShareClass = expectedSC;

							 Log.debug("Calculated Expected ShareClass of "+(expectedSC==null?"N/A":String.valueOf(expectedSC))+
									 " for accountID "+info.accountId);
						}
						
						Account acc = Account.get(rsShareClassAccounts);
						
						AuroraAccountHandler handler = new AuroraAccountHandler();
						
						com.aurora.webservice.Account auroraAcc = 
						 handler.getExistingAuroraEntity
						 (acc, (Company)acc.getAuroraCompanies(facade).get(0), facade);
						
						if (auroraAcc!=null) {
							info.auroraShareClass = auroraAcc.getShareClassCode();		
							info.auroraClientNum = auroraAcc.getContributorCode();
							info.auroraAccountNum = auroraAcc.getAccountNumber();
							
							shareClassAccountInfoMap.put(accountId, info);
						}
					} catch (Exception e) {						
						if (accountId!=null && accountId.intValue()!=0) {
							if (info!=null) {
								info.isError = true;
								info.errorMsg = e.getMessage();
								shareClassAccountInfoMap.put(accountId, info);
							}
						}
					}
				} while (rsShareClassAccounts.next());
			}	
		}
		
		DataResultSet rsAccountInfo = new DataResultSet( new String[] {
				"ACCOUNT_ID",
				"ACCOUNT_NUMBER",
				"AURORA_CLIENT_NUMBER",
				"AURORA_ACCOUNT_NUMBER",
				"CURRENT_SHARE_CLASS",
				"AURORA_SHARE_CLASS",
				"EXPECTED_SHARE_CLASS",
				"OVERRIDE_SHARE_CLASS",
				"EARLY_INVESTOR",
				"SEED_FUNDER",
				"INTERNAL_ACCOUNT",
				"CURRENT_UNITS",
				"CLOSING_UNITS",
				"FUND_CODE",
				"IS_ERROR",
				"ERROR_MSG"
		});
		
		if (shareClassAccountInfoMap!=null && !shareClassAccountInfoMap.isEmpty()) {
			for (ShareClassAccountInfo info:shareClassAccountInfoMap.values()) {
				Vector<String> data = new Vector<String>();
				
				data.add(String.valueOf(info.accountId)); //ACCOUNT_ID
				data.add(String.valueOf(info.accountNumber)); //ACCOUNT_NUMBER
				data.add(String.valueOf(info.auroraClientNum)); //AURORA_CLIENT_NUMBER
				data.add(String.valueOf(info.auroraAccountNum)); //AURORA_ACCOUNT_NUMBER
				data.add(info.currentShareClass==null?"n/a":String.valueOf(info.currentShareClass)); //CURRENT_SHARE_CLASS
				data.add(info.auroraShareClass==null?"n/a":String.valueOf(info.auroraShareClass)); //AURORA_SHARE_CLASS 
				data.add(info.expectedShareClass==null?"n/a":String.valueOf(info.expectedShareClass)); //EXPECTED_SHARE_CLASS
				data.add(info.overrideShareClass==null?"n/a":String.valueOf(info.overrideShareClass)); //OVERRIDE_SHARE_CLASS
				data.add(info.isEarlyInvestor?"1":"0"); //EARLY_INVESTOR
				data.add(info.isSeedFunder?"1":"0"); //SEED_FUNDER
				data.add(info.isInternalAccount?"1":"0"); //INTERNAL_ACCOUNT
				data.add(info.currentUnits.toPlainString()); //CURRENT_UNITS
				data.add(info.closingUnits.toPlainString()); //CLOSING_UNITS
				data.add(info.fundCode); //FUND_CODE
				data.add(info.isError?"1":"0"); //IS_ERROR
				data.add(info.errorMsg); //ERROR_MSG
				
				rsAccountInfo.addRow(data);
			}
		}
		m_binder.addResultSet("rsAccountInfo", rsAccountInfo);
	}
	
	/**
	 * Send pending aurora share class movement. 
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void createPendingShareClassMovementToAurora() throws DataException, ServiceException 
	{
		//comma separated string of [ACCOUNT_ID]:[ShareClassCode]
		String accountShareClassMovement = m_binder.getLocal("accountShareClassMovement");

		DataResultSet rsSuccess = new DataResultSet(new String[] {"Message"});
		DataResultSet rsFailure = new DataResultSet(new String[] {"Message"});
		
		if (StringUtils.stringIsBlank(accountShareClassMovement)) {
			Vector<String> failedVec = new Vector<String>();
			failedVec.add("No pending share class movement created as accountShareClassMovement is empty");
			rsFailure.addRow(failedVec);
			
		} else {
			FWFacade facade = CCLAUtils.getFacade(true);
			StringTokenizer st = new StringTokenizer(accountShareClassMovement, ",");
	
			while (st.hasMoreTokens()) 
			{	
				Vector<String> successVec = new Vector<String>();
				Vector<String> failedVec = new Vector<String>();
				
				String pendingAccSCMovement = st.nextToken();
				
				String[] splitData = pendingAccSCMovement.split(":");
				if (splitData.length!=2) {
					String msg = "Ignored pendingShareClassMovement for "+pendingAccSCMovement;
					Log.error(msg);
					failedVec.add(msg);
				} else {
					Integer accountId = null;
					Integer shareClassCode = null;
					Account account = null;
					
					try {
						accountId = Integer.parseInt(splitData[0]);
						shareClassCode = Integer.parseInt(splitData[1]);
					} catch (NumberFormatException nfe) {
						String msg = "Cannot parse accountId: "+splitData[0]+
							", or shareClassCode: "+splitData[1]+
							" for "+pendingAccSCMovement; 
	
						Log.error(msg);
						failedVec.add(msg);
					}
					
					if (accountId!=null) {
						account = Account.get(accountId, facade);
			
						if (account==null) {
							String msg = "Cannot find account with accountId:"+accountId+" for "+pendingAccSCMovement;
							Log.error(msg);
							failedVec.add(msg);
						}
					}
					
					if (account!=null && shareClassCode!=null) {
						boolean success = false;
						
						try {
							success = AuroraShareClassUtils.createAuroraPendingShareClassMovement(
									account, 
									shareClassCode, 
									facade);
							
							if (success) {
								String msg = "Created movement for "+account+" with ShareClass "+shareClassCode;
								Log.error(msg);
								successVec.add(msg);
							} else {
								String msg = "Failed create movement for "+pendingAccSCMovement;
								Log.error(msg);
								failedVec.add(msg);							
							}	
						} catch (Exception e) {
							String msg = "Cannot create movement:"+e.getMessage()+" for "+pendingAccSCMovement;
							Log.error(msg);
							failedVec.add(msg);
						}
					}
				}
				
				if (!successVec.isEmpty())
					rsSuccess.addRow(successVec);
	
				if (!failedVec.isEmpty())
					rsFailure.addRow(failedVec);
			}
		}
		
		//Finally append the results to the binder.
		if (!rsFailure.isEmpty())
			m_binder.addResultSet("rsFailure", rsFailure);
		
		if (!rsSuccess.isEmpty())
			m_binder.addResultSet("rsSuccess", rsSuccess);

	}
	
	/**
	 * Inner Class for handling share class and account information.
	 * @author Cam
	 *
	 */
	private class ShareClassAccountInfo {
		
		public int accountId = 0;
		public int accountNumber = 0;
		public String fundCode = null;
		public int auroraClientNum = 0;
		public int auroraAccountNum = 0;	
		public BigDecimal currentUnits = BigDecimal.ZERO;
		public BigDecimal closingUnits = BigDecimal.ZERO;
		public Integer currentShareClass = null;
		public Integer overrideShareClass = null;
		public Integer expectedShareClass = null;
		public Integer auroraShareClass = null;		
		public boolean isEarlyInvestor = false;
		public boolean isSeedFunder = false;
		public boolean isInternalAccount = false;
		public boolean isError = false;
		public String errorMsg = "";
		
	}
}
