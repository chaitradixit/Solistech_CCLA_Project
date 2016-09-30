package com.ecs.ucm.ccla.data;

import java.util.Date;
import java.util.HashMap;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.DataResultSetUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

public class ShareClass implements Persistable{

	private int shareClassId;
	private String fundCode;
	private String shareClassName;
	private String shareClassDescription;
	private boolean isEnabled;
	private boolean isExcludedFromMovement;
	private String minThreshold;
	private String managementRate; 
	private String initialCharge;
	private String exitCharge;
	private Date dateAdded;
	private Date dateUpdated;
	
	public ShareClass(int shareClassId, String fundCode, String shareClassName, String shareClassDescription,
			boolean isEnabled, String minThreshold, String managementRate, String initialCharge, String exitCharge,
			Date dateAdded, Date dateUpdated, boolean isExcludedFromMovement)
	{
		this.shareClassId = shareClassId;
		this.fundCode = fundCode;
		this.shareClassName = shareClassName;
		this.shareClassDescription = shareClassDescription;
		this.isEnabled = isEnabled;
		this.minThreshold = minThreshold;
		this.managementRate = managementRate;
		this.initialCharge = initialCharge;
		this.exitCharge = exitCharge;
		this.dateAdded = dateAdded;
		this.dateUpdated = dateUpdated;
		this.isExcludedFromMovement = isExcludedFromMovement;
	}
	
	public static ShareClass get(DataResultSet rs) throws DataException
	{
		if (rs.isEmpty())
			return null;
		else
			return new ShareClass(
					DataResultSetUtils.getResultSetIntegerValue(rs, "SHARE_CLASS_ID"),
					DataResultSetUtils.getResultSetStringValue(rs, "FUND_CODE"),
					DataResultSetUtils.getResultSetStringValue(rs, "SHARE_CLASS_NAME"),
					DataResultSetUtils.getResultSetStringValue(rs, "DESCRIPTION"),
					DataResultSetUtils.getResultSetBoolValue(rs, "IS_ENABLED"),
					DataResultSetUtils.getResultSetStringValue(rs, "MIN_THRESHOLD"),
					DataResultSetUtils.getResultSetStringValue(rs, "MANAGEMENT_RATE"),
					DataResultSetUtils.getResultSetStringValue(rs, "INITIAL_CHARGE"),
					DataResultSetUtils.getResultSetStringValue(rs, "EXIT_CHARGE"),
					rs.getDateValueByName("DATE_ADDED"),
					rs.getDateValueByName("LAST_UPDATED"),
					DataResultSetUtils.getResultSetBoolValue(rs, "IS_EXCLUDED_FROM_MOVEMENT"));
	}
	
	public static ShareClass get(int shareClassId, FWFacade facade) throws DataException
	{
		DataResultSet rs = getData(shareClassId, facade);
		return get(rs);
	}
	
	public static DataResultSet getData(int shareClassId, FWFacade facade) throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addIntParamToBinder
		 (binder, "SHARE_CLASS_ID", shareClassId);
		
		return facade.createResultSet
		 ("qTransactions_GetShareClassbyId", binder);
	}
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		BinderUtils.addIntParamToBinder(binder, "SHARE_CLASS_ID", this.getShareClassId());
		BinderUtils.addParamToBinder(binder, "FUND_CODE", this.getFundCode());
		BinderUtils.addParamToBinder(binder, "SHARE_CLASS_NAME", this.getShareClassName());
		BinderUtils.addParamToBinder(binder, "DESCRIPTION", this.getShareClassDescription());
		BinderUtils.addBooleanParamToBinder(binder, "IS_ENABLED", this.isEnabled);
		BinderUtils.addParamToBinder(binder, "MIN_THRESHOLD", this.getMinThreshold());
		BinderUtils.addParamToBinder(binder, "MANAGEMENT_RATE", this.getManagementRate());
		BinderUtils.addParamToBinder(binder, "INITIAL_CHARGE", this.getInitialCharge());
		BinderUtils.addParamToBinder(binder, "EXIT_CHARGE", this.getExitCharge());
		BinderUtils.addDateParamToBinder(binder, "DATE_ADDED", this.getDateAdded());
		BinderUtils.addBooleanParamToBinder(binder, "IS_EXCLUDED_FROM_MOVEMENT", this.isExcludedFromMovement());
		
	}

	
	public void persist(FWFacade facade, String username) throws DataException {
		DataResultSet beforeData = ShareClass.getData(this.getShareClassId(), facade);
		this.validate(facade);
		DataBinder binder = new DataBinder();		
		this.addFieldsToBinder(binder);
		facade.execute("qTransactions_UpdateShareClass", binder);
		
		// Add audit record
		DataResultSet afterData = ShareClass.getData(this.getShareClassId(), facade);

		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(this.getShareClassId(), com.ecs.ucm.ccla.data.ElementType.SecondaryElementType.ShareClass.toString());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.UPDATE.toString(), 
		 com.ecs.ucm.ccla.data.ElementType.SecondaryElementType.ShareClass.toString(), 
		 beforeData, afterData, auditRelations);	
	}

	
	public void setAttributes(DataBinder binder) throws DataException {
		// mandatory fields
		this.setFundCode(BinderUtils.getBinderStringValue(binder,"FUND_CODE"));
		this.setShareClassName(BinderUtils.getBinderStringValue(binder,"SHARE_CLASS_NAME"));
		this.setEnabled(BinderUtils.getBinderBoolValue(binder, "IS_ENABLED"));
		
		// other updateable fields
		this.setShareClassDescription(BinderUtils.getBinderStringValue(binder,"DESCRIPTION"));
		this.setMinThreshold(BinderUtils.getBinderStringValue(binder, "MIN_THRESHOLD"));
		this.setManagementRate(BinderUtils.getBinderStringValue(binder, "MANAGEMENT_RATE"));
		this.setInitialCharge(BinderUtils.getBinderStringValue(binder, "INITIAL_CHARGE"));
		this.setExitCharge(BinderUtils.getBinderStringValue(binder, "EXIT_CHARGE"));
		this.setExcludedFromMovement(BinderUtils.getBinderBoolValue(binder, "IS_EXCLUDED_FROM_MOVEMENT"));
	}

	public ShareClass(DataBinder binder) throws DataException
	{
		this.setAttributes(binder);
	}	
	
	/**
	 *  Creates a new share class object
	 * 
	 * @param binder
	 * @param facade
	 * @param username
	 * @return
	 * @throws NumberFormatException
	 * @throws DataException
	 */
	public static ShareClass add(DataBinder binder, FWFacade facade, String username) throws NumberFormatException, DataException
	{
		// get new id value
		int shareClassId = Integer.parseInt(
				CCLAUtils.getNewKey("ShareClass", facade));
		ShareClass shareclass = new ShareClass(binder);
		shareclass.setShareClassId(shareClassId);
		if (StringUtils.stringIsBlank(shareclass.getMinThreshold()))
			shareclass.setMinThreshold("0");		
		shareclass.addFieldsToBinder(binder);
		facade.execute("qTransactions_InsertShareClass", binder);
		
		// Add audit record
		DataResultSet afterData = ShareClass.getData(shareclass.getShareClassId(), facade);

		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(shareclass.getShareClassId(), ElementType.SecondaryElementType.ShareClass.toString());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.ADD.toString(), 
		 ElementType.SecondaryElementType.ShareClass.toString(), 
		 null, afterData, auditRelations);	
		
		return shareclass;
		
	}
	
	
	
    /**
     * Gets if account has a share class override set and returns it if so, null otherwise
     * 
     * 
     * @return Integer value of share class override or null if no override exists
     * @throws DataException
     */	
	
	//
	public static Integer getShareClassOverride(int accountId, FWFacade facade) 
	throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addIntParamToBinder(binder, "ACCOUNT_ID", accountId);	
		
		DataResultSet rs = facade.createResultSet("qTransactions_GetShareClassOverride", binder);
		if (rs.isEmpty())
		{
			return null;
		}
		else
		{
			// should only have one override value
			Integer overrideId ;
			do {
				overrideId = DataResultSetUtils.getResultSetIntegerValue(rs, "SHARE_CLASS_ID");
			} while (rs.next());
			return overrideId;
		}
			
	}
	
    /**
     * Gets dataresultset of share classes for a particular fund
     * 
     * @return DataResultSet of share classes, or null if none exist for fund
     * @throws DataException
     */		
	public static DataResultSet getShareClassesByFund(String fundCode, FWFacade facade) 
	throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addParamToBinder(binder, "FUND_CODE", fundCode);
		DataResultSet rsShareClasses = facade.createResultSet("qTransactions_GetShareClassbyFund", binder);
		
		if (!rsShareClasses.isEmpty())
			return rsShareClasses;
		else
			return null;
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
		DataResultSet rsShareClasses = facade.createResultSet("qTransactions_GetEnabledShareClassbyFund", binder);
		
		if (!rsShareClasses.isEmpty())
			return rsShareClasses;
		else
			return null;
	}
	
    /**
     * Gets dataresultset of share classes for a particular fund where the is_enabled flag is 1
     * ORDERED BY SHARE CLASS ID
     * 
     * @return DataResultSet of share classes, or null if none exist for fund
     * @throws DataException
     */		
	public static DataResultSet getEnabledShareClassesByFundOrdered(String fundCode, FWFacade facade) 
	throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addParamToBinder(binder, "FUND_CODE", fundCode);
		DataResultSet rsShareClasses = facade.createResultSet("qTransactions_GetEnabledShareClassbyFundOrdered", binder);
		
		if (!rsShareClasses.isEmpty())
			return rsShareClasses;
		else
			return null;
	}
	

    /**
     * Gets dataresultset of share classes for a particular fund where the is_enabled flag is 1
     * 
     * @return DataResultSet of share classes, or null if none exist for fund
     * @throws DataException
     */		
	public static DataResultSet getEnabledShareClassesWithMovementByFund(String fundCode, FWFacade facade) 
	throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addParamToBinder(binder, "FUND_CODE", fundCode);
		DataResultSet rsShareClasses = facade.createResultSet("qTransactions_GetEnabledShareClassWithAllowedMovementbyFund", binder);
		
		if (!rsShareClasses.isEmpty())
			return rsShareClasses;
		else
			return null;
	}
	
    /**
     * Gets dataresultset of share class expenses for a particular fund
     * 
     * @return DataResultSet of share class expenses, or null if none exist for fund
     * @throws DataException
     */		
	public static DataResultSet getShareClassExpensesByFund(String fundCode, int expenseType, FWFacade facade) 
	throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addParamToBinder(binder, "FUND_CODE", fundCode);
		BinderUtils.addIntParamToBinder(binder, "INCOME_EXPENSE_TYPE_ID", expenseType);
		DataResultSet rsShareClassExpenses = facade.createResultSet("qTransactions_GetShareClassExpenses", binder);
		
		if (!rsShareClassExpenses.isEmpty())
			return rsShareClassExpenses;
		else
			return null;
	}	
	
	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub		
	}


	public int getShareClassId() {
		return shareClassId;
	}


	public void setShareClassId(int shareClassId) {
		this.shareClassId = shareClassId;
	}


	public String getFundCode() {
		return fundCode;
	}


	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}


	public boolean isEnabled() {
		return isEnabled;
	}


	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}


	public String getManagementRate() {
		return managementRate;
	}


	public void setManagementRate(String managementRate) {
		this.managementRate = managementRate;
	}


	public String getInitialCharge() {
		return initialCharge;
	}


	public void setInitialCharge(String initialCharge) {
		this.initialCharge = initialCharge;
	}


	public String getExitCharge() {
		return exitCharge;
	}


	public void setExitCharge(String exitCharge) {
		this.exitCharge = exitCharge;
	}


	public Date getDateAdded() {
		return dateAdded;
	}


	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}


	public Date getDateUpdated() {
		return dateUpdated;
	}


	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}


	public String getMinThreshold() {
		return minThreshold;
	}


	public void setMinThreshold(String minThreshold) {
		this.minThreshold = minThreshold;
	}


	public String getShareClassName() {
		return shareClassName;
	}


	public void setShareClassName(String shareClassName) {
		this.shareClassName = shareClassName;
	}


	public String getShareClassDescription() {
		return shareClassDescription;
	}


	public void setShareClassDescription(String shareClassDescription) {
		this.shareClassDescription = shareClassDescription;
	}

	public void setExcludedFromMovement(boolean isExcludedFromMovement) {
		this.isExcludedFromMovement = isExcludedFromMovement;
	}
	
	public boolean isExcludedFromMovement() {
		return isExcludedFromMovement;
	}
	
	


}
