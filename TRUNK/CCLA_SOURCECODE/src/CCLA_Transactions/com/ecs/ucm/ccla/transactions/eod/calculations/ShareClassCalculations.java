package com.ecs.ucm.ccla.transactions.eod.calculations;

import java.math.BigDecimal;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.ShareClass;
import com.ecs.ucm.ccla.transactions.utils.TransactionUtils;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ServiceException;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.server.Service;

public class ShareClassCalculations extends Service {
	
	// get expected share class for account but do NOT apply it
	
	// pass in value (can be current or expected account balance) and account id
	// get fund code
	// look for share classes against that fund code
	// 1) look for share classes with custom eligibility criteria
	// if 1) found then see if account matches, if it does apply return that share class
	// if 1) not found then loop through share class where balance value is greater than or equal
	// to threshold but less than next threshold (CHECK)
	// return this share class id
	
	
    /**
     * gets the expected share class of an account based on account balance passed to method
     * 
     * @param int accountId
     * @param BigDecimal accountBalance
     * @param FWFacade facade
     * @return int - the share_class_id 
     * @throws  ServiceException, DataException
     */	
	public static int getExpectedShareClass(int accountId, BigDecimal accountBalance, FWFacade facade) 
	throws DataException
	{
		Integer finalShareClass = null;
		boolean hasMatchedShareClass = false;
		
		Log.info("Checking account id: " + accountId + ", with expected account balance:" + accountBalance);		
		Account account = Account.get(accountId, facade);
		
		// Get fund code for account
		String fundCode = account.getFundCode();
		Log.debug("fundcode is " + fundCode);	
		// is share class value overridden?
		Integer overrideShareClass = ShareClass.getShareClassOverride(accountId, facade);
		if (overrideShareClass!=null)
		{
			hasMatchedShareClass=true;
			ShareClass shareClass = ShareClass.get(overrideShareClass, facade);
			finalShareClass = shareClass.getShareClassId();
			Log.info("Found override shareclass with value:" + finalShareClass);
		}
		
		//TODO check custom eligibility criteria as this will override next check (if present)
		
		// if no previous match then look at share class min thresholds to get share class
		boolean inRange = false;
		// rsShareClasses is ordered list (by min_threshold)
		if (!hasMatchedShareClass)			
		{
			// get share classes for this fund code
			DataResultSet rsShareClasses = ShareClass.getShareClassesByFund(fundCode, facade);
			Log.info("Found " + rsShareClasses.getNumRows() + " share classes for fund:" + fundCode); 
				
			do {
				int shareClassId = CCLAUtils.getResultSetIntegerValue(rsShareClasses, "SHARE_CLASS_ID");
				BigDecimal threshold = TransactionUtils.getResultSetBigDecimalValue(rsShareClasses, "MIN_THRESHOLD");
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
		
		return finalShareClass;
	}
	

		

}
