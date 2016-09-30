package com.ecs.ucm.ccla.data;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.CacheManager;
import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.DateFormatter;
import com.ecs.utils.stellent.embedded.FWFacade;

public class Fund implements Persistable, Comparable<Fund> {
	
	/* **************** Constants ******************** */
	public static final int FUND_STATUS_OPEN 	= 1;
	public static final int FUND_STATUS_CLOSED 	= 2;
	public static final int FUND_STATUS_PENDING = 3;
	
	//Column names
	public static final class Cols {
		public static final String COMPANY_ID			= "COMPANY_ID";
		public static final String FUND_CODE			= "FUND_CODE";
	}
	
	public static final String COL_FUND_CODE 			= "FUND_CODE";
	public static final String COL_COMPANY_ID			= "COMPANY_ID";
	public static final String COL_FUND_STATUS_ID		= "FUND_STATUS_ID";
	public static final String COL_FUND_TYPECODE_ID 	= "FUND_TYPE_CODE_ID";
	public static final String COL_INCOME_TYPECODE_ID 	= "INCOME_TYPECODE_ID";
	public static final String COL_FUND_NAME			= "FUND_NAME";
	public static final String COL_OPENING_DATE			= "OPENINGDATE";
	public static final String COL_DISPLAY_NAME			= "DISPLAYNAME";
	public static final String COL_CHAPS_CODE			= "CHAPSCODE";
	public static final String COL_FUND_GROUP_ID		= "FUNDGROUPID";
	public static final String COL_LAST_UPDATED			= "LAST_UPDATED";
	public static final String COL_FUND_SHORT_NAME		= "FUND_SHORT_NAME";
	public static final String COL_FUND_DEFCODE_ID 		= "FUND_DEFCODE_ID";
	public static final String COL_COMM_FIRST_IS_ELIGIBLE = "COMMUNITY_FIRST_IS_ELIGIBLE";
	
//    "PAYA" = "Pay to bank"
//    "RETN" = "Interest Retained In This Account" OR "Interest Paid To Another Deposit Account"
//    "TXRI" = "Pay Dividend To A Deposit Fund Account"
//    "REIN" = "Dividends Reinvested To Purchase Further Shares"
	public static final String COL_IS_INTEREST_BAC = "IS_INTEREST_BAC";
	public static final String COL_IS_INTEREST_RETAINED = "IS_INTEREST_RETAINED";
	public static final String COL_IS_INTEREST_TRANSFERABLE = "IS_INTEREST_TRANSFERABLE";
	public static final String COL_IS_DIVIDEND_BAC = "IS_DIV_BAC";
	public static final String COL_IS_DIVIDEND_TRANSFER_TO_DEPOSIT = "IS_DIV_TRANSFER_TO_DEPOSIT";
	public static final String COL_IS_DIVIDEND_REINVESTED = "IS_DIV_REINVESTED";
	public static final String COL_IS_DIVIDEND_REINVESTED_ACCOUNT = "IS_DIV_REINVESTED_ACCOUNT";
	public static final String COL_IS_DIVIDEND_REINVESTED_OTHER_FUNDS = "IS_DIV_REINVESTED_OTHER_FUNDS";
	public static final String COL_IS_SHARE_CLASS = "IS_SHARE_CLASS";
	public static final String COL_IS_DIVENDEND_REINVESTED_CAPITAL_BALANCE = "IS_DIV_REINVESTED_CAP_BAL";
	
	public static final String COL_ALLOW_PAYA = "ALLOW_PAYA";
	public static final String COL_ALLOW_RETN = "ALLOW_RETN";
	public static final String COL_ALLOW_REIN = "ALLOW_REIN";
	public static final String COL_ALLOW_TXRI = "ALLOW_TXRI";
	
	public static final String COL_ALLOW_SAME_MANDATED_ACCOUNT_FUND = "ALLOW_SAME_MANDATED_ACC_FUND";
	public static final String COL_ALLOW_REIN_MANDATED_ACCOUNT_IN_OTHER_FUNDS = "ALLOW_REIN_MAND_ACC_OTH_FUND";
	
	/* **************** Properties ******************* */
	public enum FundCodes {
		// COIF
		C,
		
		// CBF
		D,
		
		// PSDF
		PC,
		PI,
		
		// Special
		// Community First
		CF
	}
	
	/* **************** Properties ******************* */
	private Company company = null;
	private FundStatus status = null;
	private FundTypeCode typeCode = null;
	private FundIncomeTypeCode incomeTypeCode = null;
	private FundDefinitionCode definitionCode = null;
	
	private String fundCode	= null;
	private String name = null;
	private String shortName = null;
	private String displayName = null;
	private String chapsCode = null;	
	private String groupId = null;
	private Date lastUpdated = null;
	private Date openingDate = null;
	
	private boolean eligibleForCommunityFirst = false;
	
//	private boolean isInterestBac = false;
//	private boolean isInterestRetained = false;
//	private boolean isInterestTransferable = false;
//	private boolean isDividendBAC = false;
//	private boolean isDividendTransferableToDeposit = false;
//	private boolean isDividendReinvested = false;
//	private boolean isDividendReinvestedAccount = false;
//	private boolean isDividendReinvestedToOtherFunds = false;
//	private boolean isShareClassFund = false;
//	private boolean isDividendReinvestedCapitalBalance = false;
	
	private boolean allowPAYA = false;
	private boolean allowRETN = false;
	private boolean allowREIN = false;
	private boolean allowTXRI = false;
	
	private boolean allowSameMandatedAccountFund = false;
	private boolean allowMandatedAccountInOtherFunds = false;
	
	/* **************** Constructor ****************** */
	/**
	 * Constructor for Fund
	 * @param company
	 * @param fundCode
	 * @param name
	 * @param status
	 * @param openingDate
	 * @param typeCode
	 * @param incomeTypeCode
	 * @param displayName
	 * @param chapsCode
	 * @param groupId
	 * @param lastUpdated
	 */
	public Fund(Company company, String fundCode, String name,
			FundStatus status, Date openingDate, FundTypeCode typeCode,
			FundIncomeTypeCode incomeTypeCode, String displayName, String chapsCode,
			String groupId, Date lastUpdated, String shortName, 
			FundDefinitionCode definitionCode, boolean eligibleForCommunityFirst,
			boolean allowPAYA, boolean allowRETN,
			boolean allowREIN, boolean allowTXRI,
			boolean allowSameMandatedAccountFund, boolean allowMandatedAccountInOtherFunds) {
		this.company = company;
		this.fundCode = fundCode;
		this.name = name;
		this.status = status;
		this.openingDate = openingDate;
		this.typeCode = typeCode;
		this.incomeTypeCode = incomeTypeCode;
		this.displayName = displayName;
		this.chapsCode = chapsCode;
		this.groupId = groupId;
		this.lastUpdated = lastUpdated;
		this.shortName = shortName;
		this.definitionCode = definitionCode;
		this.eligibleForCommunityFirst = eligibleForCommunityFirst;
		this.allowPAYA = allowPAYA;
		this.allowREIN = allowREIN;
		this.allowRETN = allowRETN;
		this.allowTXRI = allowTXRI;
		this.allowSameMandatedAccountFund = allowSameMandatedAccountFund;
		this.allowMandatedAccountInOtherFunds = allowMandatedAccountInOtherFunds;
	}
	
	/**
	 * Static getter for Fund
	 * @param fundCode
	 * @param facade
	 * @return Fund
	 * @throws DataException
	 */
	public static Fund get(String fundCode, FWFacade facade) 
	 throws DataException {
		DataResultSet rsFund = getData(fundCode, facade);
		
		if (rsFund == null || rsFund.isEmpty())
			return null;
		
		Fund fund = get(rsFund);
		
		return fund;
	}
	
	public static boolean hasShareClass(String fundCode, FWFacade facade) throws DataException
	{
		boolean hasShareClass = false;
		DataBinder binder = new DataBinder();
		BinderUtils.addParamToBinder(binder, "FUND_CODE", fundCode);
		DataResultSet rs = facade.createResultSet("qCore_GetShareClassByFund", binder);
		if (!rs.isEmpty())
			hasShareClass = true;
		return hasShareClass;
	}
	
	   /**
     * Gets dataresultset of share classes for a particular fund where the is_enabled flag is 1
     * 
     * @return DataResultSet of share classes, or null if none exist for fund
     * @throws DataException
     */		
	public static DataResultSet getEnabledShareClassesByFund(String fundCode, FWFacade facade) 
	throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addParamToBinder(binder, "FUND_CODE", fundCode);
		DataResultSet rsShareClasses = facade.createResultSet("qCore_GetEnabledShareClassbyFund", binder);
		
		if (!rsShareClasses.isEmpty())
			return rsShareClasses;
		else
			return null;
	}

	
	public static Vector<Fund> getAll(FWFacade facade) throws DataException {
		
		DataResultSet rsFunds = facade.createResultSet
		("qClientServices_GetFunds", new DataBinder());
		
		Vector<Fund> funds = new Vector<Fund>();
		
		if (rsFunds.first()) {
			do {
				funds.add(get(rsFunds));
			} while (rsFunds.next());
		}
		
		return funds;
	}
	
	/**
	 * Static Getter for DataResultSet
	 * @param fundCode
	 * @param facade
	 * @return DataResultSet
	 * @throws DataException
	 */
	public static DataResultSet getData(String fundCode, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		binder.putLocal("fundCode", fundCode);
		
		DataResultSet rsFund = 
		 facade.createResultSet("qClientServices_GetFund", binder);
		
		return rsFund;
	}
	
	/**
	 * Static Getter for Fund
	 * @param rsFund
	 * @return
	 * @throws DataException
	 */
	public static Fund get(DataResultSet rsFund) throws DataException {
		
		Fund obj = null;

		
		if (rsFund==null || rsFund.isEmpty())
			return obj;
		
		int companyId = CCLAUtils.getResultSetIntegerValue(rsFund, COL_COMPANY_ID);
				
		Company company = Company.getCache().getCachedInstance(companyId);

		
		FundStatus fundStatus = FundStatus.get(rsFund);
		FundTypeCode fundTypeCode = FundTypeCode.get(rsFund);
		FundIncomeTypeCode fundIncomeTypeCode = FundIncomeTypeCode.get(rsFund);
		FundDefinitionCode fundDefintionCode = FundDefinitionCode.get(rsFund);
		
		obj = new Fund(
			company, 
			rsFund.getStringValueByName(COL_FUND_CODE),
			rsFund.getStringValueByName(COL_FUND_NAME),
			fundStatus,
			rsFund.getDateValueByName(COL_OPENING_DATE),
			fundTypeCode,
			fundIncomeTypeCode,
			rsFund.getStringValueByName(COL_DISPLAY_NAME),
			rsFund.getStringValueByName(COL_CHAPS_CODE),
			rsFund.getStringValueByName(COL_FUND_GROUP_ID),
			rsFund.getDateValueByName(COL_LAST_UPDATED),
			rsFund.getStringValueByName(COL_FUND_SHORT_NAME),
			fundDefintionCode,
			CCLAUtils.getResultSetBoolValue(rsFund, COL_COMM_FIRST_IS_ELIGIBLE),
			CCLAUtils.getResultSetBoolValue(rsFund, Fund.COL_ALLOW_PAYA),
			CCLAUtils.getResultSetBoolValue(rsFund, Fund.COL_ALLOW_RETN),
			CCLAUtils.getResultSetBoolValue(rsFund, Fund.COL_ALLOW_REIN),
			CCLAUtils.getResultSetBoolValue(rsFund, Fund.COL_ALLOW_TXRI),
			CCLAUtils.getResultSetBoolValue(rsFund, Fund.COL_ALLOW_SAME_MANDATED_ACCOUNT_FUND),
			CCLAUtils.getResultSetBoolValue(rsFund, Fund.COL_ALLOW_REIN_MANDATED_ACCOUNT_IN_OTHER_FUNDS)	
		);
		
		return obj;
	}

	/**
	 * Getter for fundCode
	 * @return String (fundCode)
	 */
	public String getFundCode() {
		return fundCode;
	}

	/**
	 * Setter for fundCode
	 * @param fundCode
	 */
	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}
	
	/**
	 * Getter for Company
	 * @return Company (Company)
	 */
	public Company getCompany() {
		return company;
	}

	/**
	 * Setter for Company
	 * @param company
	 */
	public void setCompany(Company company) {
		this.company = company;
	}

	
	/**
	 * Getter for openingDate
	 * @return Date (openingDate)
	 */
	public Date getOpeningDate() {
		return openingDate;
	}

	/**
	 * Setter for openingDate
	 * @param openingDate
	 */
	public void setOpeningDate(Date openingDate) {
		this.openingDate = openingDate;
	}
	
	/**
	 * Getter for TypeCode
	 * @return FundTypeCode (typeCode)
	 */
	public FundTypeCode getTypeCode() {
		return typeCode;
	}

	/**
	 * Setter for typeCode
	 * @param typeCode
	 */
	public void setTypeCode(FundTypeCode typeCode) {
		this.typeCode = typeCode;
	}
	
	/**
	 * Getter for incomeTypeCode
	 * @return FundIncomeTypeCode (incomeTypeCode)
	 */
	public FundIncomeTypeCode getIncomeTypeCode() {
		return incomeTypeCode;
	}
	
	/**
	 * Setter for incomeTypeCode
	 * @param incomeTypeCode
	 */
	public void setIncomeTypeCode(FundIncomeTypeCode incomeTypeCode) {
		this.incomeTypeCode = incomeTypeCode;
	}

	/**
	 * Getter for displayName
	 * @return String (displayName)
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Setter for displayName
	 * @param displayName
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	/**
	 * Getter for chapsCode
	 * @return String (chapsCode)
	 */
	public String getChapsCode() {
		return chapsCode;
	}

	/**
	 * Setter for chapsCode
	 * @param chapsCode
	 */
	public void setChapsCode(String chapsCode) {
		this.chapsCode = chapsCode;
	}
	
	/**
	 * Getter from groupId
	 * @return String (groupId)
	 */
	public String getGroupId() {
		return groupId;
	}

	/**
	 * Setter for groupId
	 * @param groupId
	 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	/**
	 * Getter for lastUpdated
	 * @return Date (lastUpdated)
	 */
	public Date getLastUpdated() {
		return lastUpdated;
	}
	
	/**
	 * Setter for lastUpdated
	 * @param lastUpdated 
	 */
	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	
	/**
	 * Getter for DefintionCode
	 * @return FundDeinitionCode (typeCode)
	 */
	public FundDefinitionCode getDefinitionCode() {
		return definitionCode;
	}

	/**
	 * Setter for DefintionCode
	 * @param FundDeinitionCode
	 */
	public void setTypeCode(FundDefinitionCode definitionCode) {
		this.definitionCode = definitionCode;
	}
	
	public void setAttributes(DataBinder binder) throws DataException {
		// TODO Auto-generated method stub
	}

	public void addFieldsToBinder(DataBinder binder) throws DataException {
		// TODO Auto-generated method stub
	}

	public void persist(FWFacade facade, String username) throws DataException {
		// TODO Auto-generated method stub
	}

	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub
	}
	
	public void setEligibleForCommunityFirst(boolean eligibleForCommunityFirst) {
		this.eligibleForCommunityFirst = eligibleForCommunityFirst;
	}

	public boolean isEligibleForCommunityFirst() {
		return eligibleForCommunityFirst;
	}
	
	public boolean equals(Object fund) {
		if (fund instanceof Fund) {
			return (this.getFundCode().equals(((Fund) fund).getFundCode()));
		}
		
		return false;
	}
	
	/*
	 *  Caching stuff
	 */
	private static Cache CACHE = new Cache();
	
	public static Cachable<String, Fund> getCache() {
		return CACHE;
	}

	/** Fund cache implementor.
	 *  
	 **/
	private static class Cache extends Cachable<String, Fund> {

		public Cache() {
			super("Fund");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<Fund> objs = Fund.getAll(facade);
			HashMap<String, Fund> newCache = new HashMap<String, Fund>();
			
			for (Fund obj : objs) {
				newCache.put(obj.getFundCode(), obj);
			}
			
			this.CACHE_MAP = newCache;
		}
	}


	/*
	 *  Caching stuff
	 */
	private static InterFundCache INTERFUNDCACHE = new InterFundCache();
	
	public static Cachable<String, String> getInterFundCache() {
		return INTERFUNDCACHE;
	}

	/** Fund cache implementor.
	 *  
	 **/
	private static class InterFundCache extends Cachable<String, String> {

		public InterFundCache() {
			super("InterFundTransactions");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			DataResultSet rsInterfunds = facade.createResultSet("qCore_GetInterFundTransactions", new DataBinder());
			HashMap<String, String> newCache = new HashMap<String, String>();
			
			if (rsInterfunds!=null && !rsInterfunds.isEmpty()) {
				
				if (rsInterfunds.first()) {
					do {
						String sourceFund = 
							CCLAUtils.getResultSetStringValue(rsInterfunds, "SOURCE_FUND_CODE");
						String destinationFund = 
							CCLAUtils.getResultSetStringValue(rsInterfunds, "DESTINATION_FUND_CODE");
						
						String value = sourceFund + "|" + destinationFund;
						newCache.put(value, value);
					
					} while (rsInterfunds.next());
				}
			}
			this.CACHE_MAP = newCache;
		}
	}
	
	
	
	public FundStatus getStatus() {
		return status;
	}

	public void setStatus(FundStatus status) {
		this.status = status;
	}

	public boolean isAllowPAYA() {
		return allowPAYA;
	}

	public void setAllowPAYA(boolean allowPAYA) {
		this.allowPAYA = allowPAYA;
	}

	public boolean isAllowRETN() {
		return allowRETN;
	}

	public void setAllowRETN(boolean allowRETN) {
		this.allowRETN = allowRETN;
	}

	public boolean isAllowREIN() {
		return allowREIN;
	}

	public void setAllowREIN(boolean allowREIN) {
		this.allowREIN = allowREIN;
	}

	public boolean isAllowTXRI() {
		return allowTXRI;
	}

	public void setAllowTXRI(boolean allowTXRI) {
		this.allowTXRI = allowTXRI;
	}

	public boolean isAllowSameMandatedAccountFund() {
		return allowSameMandatedAccountFund;
	}

	public void setAllowSameMandatedAccountFund(boolean allowSameMandatedAccountFund) {
		this.allowSameMandatedAccountFund = allowSameMandatedAccountFund;
	}

	public boolean isAllowREINMandatedAccountInOtherFunds() {
		return allowMandatedAccountInOtherFunds;
	}

	public void setAllowREINMandatedAccountInOtherFunds(
			boolean allowMandatedAccountInOtherFunds) {
		this.allowMandatedAccountInOtherFunds = allowMandatedAccountInOtherFunds;
	}
	
	/** Compares the Fund Code strings */
	public int compareTo(Fund o) {
		return this.getFundCode().compareTo(o.getFundCode());
	}

	@Override
	public int hashCode() {
		return this.getFundCode().hashCode();
	}

//	public boolean isInterestBac() {
//		return isInterestBac;
//	}
//
//	public void setInterestBac(boolean isInterestBac) {
//		this.isInterestBac = isInterestBac;
//	}
//
//	public boolean isInterestRetained() {
//		return isInterestRetained;
//	}
//
//	public void setInterestRetained(boolean isInterestRetained) {
//		this.isInterestRetained = isInterestRetained;
//	}
//
//	public boolean isInterestTransferable() {
//		return isInterestTransferable;
//	}
//
//	public void setInterestTransferable(boolean isInterestTransferable) {
//		this.isInterestTransferable = isInterestTransferable;
//	}
//
//	public boolean isDividendBAC() {
//		return isDividendBAC;
//	}
//
//	public void setDividendBAC(boolean isDividendBAC) {
//		this.isDividendBAC = isDividendBAC;
//	}
//
//	public boolean isDividendTransferableToDeposit() {
//		return isDividendTransferableToDeposit;
//	}
//
//	public void setDividendTransferableToDeposit(
//			boolean isDividendTransferableToDeposit) {
//		this.isDividendTransferableToDeposit = isDividendTransferableToDeposit;
//	}
//
//	public boolean isDividendReinvested() {
//		return isDividendReinvested;
//	}
//
//	public void setDividendReinvested(boolean isDividendReinvested) {
//		this.isDividendReinvested = isDividendReinvested;
//	}
//
//	public boolean isDividendReinvestedAccount() {
//		return isDividendReinvestedAccount;
//	}
//
//	public void setDividendReinvestedAccount(
//			boolean isDividendReinvestedAccount) {
//		this.isDividendReinvestedAccount = isDividendReinvestedAccount;
//	}
//
//	public boolean isDividendReinvestedToOtherFunds() {
//		return isDividendReinvestedToOtherFunds;
//	}
//
//	public void setDividendReinvestedToOtherFunds(
//			boolean isDividendReinvestedToOtherFunds) {
//		this.isDividendReinvestedToOtherFunds = isDividendReinvestedToOtherFunds;
//	}
//
//	public boolean isShareClassFund() {
//		return isShareClassFund;
//	}
//
//	public void setShareClassFund(boolean isShareClassFund) {
//		this.isShareClassFund = isShareClassFund;
//	}
//
//	public boolean isDividendReinvestedCapitalBalance() {
//		return isDividendReinvestedCapitalBalance;
//	}
//
//	public void setDividendReinvestedCapitalBalance(
//			boolean isDividendReinvestedCapitalBalance) {
//		this.isDividendReinvestedCapitalBalance = isDividendReinvestedCapitalBalance;
//	}
}
