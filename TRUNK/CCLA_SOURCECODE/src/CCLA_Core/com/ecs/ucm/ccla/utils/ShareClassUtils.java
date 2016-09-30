package com.ecs.ucm.ccla.utils;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.shared.SharedObjects;

import java.math.BigDecimal;
import java.util.Date;

import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.AccountValue;
import com.ecs.ucm.ccla.data.AccountValueAudit;
import com.ecs.ucm.ccla.data.ElementAttribute;
import com.ecs.ucm.ccla.data.ElementAttributeApplied;
import com.ecs.ucm.ccla.data.ShareClass;
import com.ecs.ucm.ccla.data.ShareClassGroup;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.DataResultSetUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

public class ShareClassUtils {

	//Default share classes from the ccla_transactions_environment.cgf
	
	private static final boolean PSDF_IDENTIFY_SEED_FUNDERS_AND_EARLY_INVESTORS = 
		SharedObjects.getEnvValueAsBoolean("PSDF_IDENTIFY_SEED_FUNDERS_AND_EARLY_INVESTORS", false);
	
	private static final String	PSDF_SEED_FUND_CUTOFF_DATE = 
		 SharedObjects.getEnvironmentValue("PSDF_SEED_FUND_CUTOFF_DATE");
	
	private static final String PSDF_EARLY_INVESTOR_FUND_TOTAL_CUTOFF = 
		 SharedObjects.getEnvironmentValue("PSDF_EARLY_INVESTOR_FUND_TOTAL_CUTOFF");
	
	private static final Integer PSDF_SEED_FUND_AND_EARLY_INVESTOR_SHARE_CLASS_ID = 
		Integer.parseInt(SharedObjects.getEnvironmentValue("PSDF_SEED_FUND_AND_EARLY_INVESTOR_SHARE_CLASS_ID"));

	private static Integer PSDF_INTERNAL_ACCOUNT_SHARE_CLASS_ID = 
		Integer.parseInt(SharedObjects.getEnvironmentValue("PSDF_INTERNAL_ACCOUNT_SHARE_CLASS_ID"));
	
	
	/**
	 * Method to get the expected share class for the account.
	 * If the expectedAmount is specified, it will use the expected amount to work out the shareclass
	 * else if will use the actual account closing balance (0 if doesn't exist).
	 * @param accountId
	 * @param cashValue (Can be null)
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Integer getExpectedShareClass(int accountId, String expectedAmount, FWFacade facade) throws DataException {
		Integer finalShareClass = null;
		BigDecimal shareClassGroupBalance = new BigDecimal("0");
		Account account = Account.get(accountId, facade);
		
		if (account==null) {
			throw new DataException("Account cannot be null");
		}
		
		String cashValue = expectedAmount;

		if (StringUtils.stringIsBlank(cashValue)) {
			
			//set default cash value
			cashValue = "0";
			
			AccountValueAudit ava = AccountValueAudit.get(accountId, new Date(), facade);
			if (ava!=null && StringUtils.stringIsBlank(ava.getClosingCash())) {
				cashValue = ava.getClosingCash();
			} else {
				AccountValue av = AccountValue.get(accountId, facade);
				if (av!=null && !StringUtils.stringIsBlank(av.getCash())) {
					cashValue = av.getCash();
				} 
			}
		}  
		
		Log.info("Checking account id: " + accountId + ", with expected closing account balance:" + cashValue);		

		
		// Get fund code for account
		String fundCode = account.getFundCode();
		
		if (PSDF_IDENTIFY_SEED_FUNDERS_AND_EARLY_INVESTORS) {
			
			//first check for internal account
			if (isInternalAccount(account, facade)) {
				finalShareClass = PSDF_INTERNAL_ACCOUNT_SHARE_CLASS_ID;
				Log.debug("using internal account share class value of " + finalShareClass);
				return finalShareClass;
			}

			if (isEarlyInvestorAccount(account, facade) || 
					isSeedFundAccount(account, facade)) {
				finalShareClass = PSDF_SEED_FUND_AND_EARLY_INVESTOR_SHARE_CLASS_ID;
				Log.debug("using early investor or seed fund share class value of " + finalShareClass);
				return finalShareClass;
			}
		} 
		
		// is account part of a share class group?		
		ShareClassGroup scGroup = ShareClassGroup.getByAccountId(accountId, facade);
		if (scGroup != null) {
			// does this group have an override?
			if (scGroup.isOverridden()) {
				finalShareClass = scGroup.getShareClassId();
				Log.debug("using overridden share class value of " + finalShareClass);
				return finalShareClass;
			} else {
				Log.debug("using share class group total cash value of " + scGroup.getTotalCash());
				// use the total cash from the share class group as the account balance
				if (scGroup.getTotalCash() != null)
					shareClassGroupBalance = new BigDecimal(scGroup.getTotalCash());
			}
		}
		
		// is share class value overridden?
		Integer overrideShareClass = ShareClass.getShareClassOverride(accountId, facade);
		if (overrideShareClass!=null) {
			ShareClass shareClass = ShareClass.get(overrideShareClass, facade);
			finalShareClass = shareClass.getShareClassId();
			Log.info("Found override shareclass with value:" + finalShareClass);
			return finalShareClass;
		}
		
		//TODO check custom eligibility criteria as this will override next check (if present)
		
		// if no previous match then look at share class min thresholds to get share class
		boolean inRange = false;
		// rsShareClasses is ordered list (by min_threshold)

		// get share classes for this fund code
		DataResultSet rsShareClasses = ShareClass.getEnabledShareClassesWithMovementByFund(fundCode, facade);
		if (rsShareClasses!= null) {
			Log.info("Found " + rsShareClasses.getNumRows() + " share classes for fund:" + fundCode); 
			// get accountBalance as BigDecimal
			BigDecimal accountBalance = new BigDecimal(cashValue);
			// if shareClassGroupBalance has been set then use this value instead
			if (shareClassGroupBalance.compareTo(BigDecimal.ZERO)!=0)
				accountBalance = shareClassGroupBalance;
			do {
				int shareClassId = DataResultSetUtils.getResultSetIntegerValue(rsShareClasses, "SHARE_CLASS_ID");
				BigDecimal threshold = new BigDecimal(DataResultSetUtils.getResultSetStringValue(rsShareClasses, "MIN_THRESHOLD"));
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
	
	/**
	 * Checks if the account is an early investor account
	 * @param accountId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static boolean isEarlyInvestorAccount(Account account, FWFacade facade) throws DataException 
	{	
		ElementAttributeApplied earlyInvestorElem = getElementAttribute(account.getAccountId(), ElementAttribute.AccountAttributes.PSDF_EARLY_INVESTOR, facade);

		//If early investor attribute get the result.
		if (earlyInvestorElem!=null) {
			return earlyInvestorElem.getStatus();
		}

		ElementAttributeApplied seedFundElem = getElementAttribute(account.getAccountId(), ElementAttribute.AccountAttributes.PSDF_SEED_FUND, facade);
		
		//Need to refactor FundNavAudit class but use this for now
		DataBinder binder = new DataBinder();
		BinderUtils.addParamToBinder(binder, "FUND_CODE", account.getFundCode());
		DataResultSet rs = facade.createResultSet("qTransactions_GetLatestFundNavAudit", binder);
		
		BigDecimal cutOffValue = new BigDecimal(PSDF_EARLY_INVESTOR_FUND_TOTAL_CUTOFF);
		
		boolean isPotentialEI = true;
		
		if (!rs.isEmpty()) {
			BigDecimal nav = new BigDecimal(rs.getStringValueByName("NAV"));
			if (nav.compareTo(cutOffValue)!=-1) {
				isPotentialEI = false;
			}
		}
		
		if (isPotentialEI) {
			if (seedFundElem==null) {				
				return true;
			} 
		}	
		return false;
	}
	
	/**
	 * Checks if the account is a seedFundAccount
	 * @param account
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static boolean isSeedFundAccount(Account account, FWFacade facade) throws DataException {
		ElementAttributeApplied elemAttrAppl = 
			getElementAttribute(account.getAccountId(), ElementAttribute.AccountAttributes.PSDF_SEED_FUND, facade);
		
		if (elemAttrAppl!=null) {
			return elemAttrAppl.getStatus(); 
		} else {
			Date cutOffDate = DateUtils.parseddsMMsyyyy(PSDF_SEED_FUND_CUTOFF_DATE);
			if (cutOffDate!=null) {
				if (cutOffDate.before(DateUtils.getNow())) {	
					return true;
				} 
			} 
		}
		return false;
	}	

	/**
	 * Check if the account is a internal account
	 * @param account
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static boolean isInternalAccount(Account account, FWFacade facade) throws DataException {
		ElementAttributeApplied elemAttrAppl = getElementAttribute(account.getAccountId(), ElementAttribute.AccountAttributes.INTERNAL_ACCOUNT, facade);
		return (elemAttrAppl!=null && elemAttrAppl.getStatus());
	}	
	
	/**
	 * Get Element Attribute Applied
	 * @param elementId
	 * @param attributeId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	private static ElementAttributeApplied getElementAttribute(int elementId, int attributeId, FWFacade facade) throws DataException {
		return ElementAttributeApplied.get(elementId, attributeId, facade);
	}
}
