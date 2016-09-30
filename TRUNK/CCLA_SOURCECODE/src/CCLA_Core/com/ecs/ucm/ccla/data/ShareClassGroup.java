package com.ecs.ucm.ccla.data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.DataResultSetUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

public class ShareClassGroup implements Persistable{

	private int groupId;
	private String groupName;
	private String fundCode;
	private boolean isOverridden;	
	private Integer shareClassId;
	private String totalCash;
	private Date lastUpdated;
	private String lastUpdatedBy;

	public ShareClassGroup(int groupId, String groupName, String fundCode, boolean isOverridden,  
			Integer shareClassId, String totalCash, Date lastUpdated, String lastUpdatedBy)
	{
		this.groupId = groupId;
		this.groupName = groupName;
		this.fundCode = fundCode;
		this.isOverridden = isOverridden;
		this.shareClassId = shareClassId;
		this.totalCash = totalCash;
		this.lastUpdated = lastUpdated;
		this.lastUpdatedBy=lastUpdatedBy;
	}
	
	public ShareClassGroup(DataBinder binder) throws DataException
	{
		this.setAttributes(binder);
	}
	
	public static ShareClassGroup get(DataResultSet rs) throws DataException
	{
		if (rs.isEmpty())
			return null;
		else
			return new ShareClassGroup(
					DataResultSetUtils.getResultSetIntegerValue(rs, "GROUP_ID"),
					DataResultSetUtils.getResultSetStringValue(rs, "GROUP_NAME"),
					DataResultSetUtils.getResultSetStringValue(rs, "FUND_CODE"),
					DataResultSetUtils.getResultSetBoolValue(rs, "IS_OVERRIDDEN"),
					DataResultSetUtils.getResultSetIntegerValue(rs, "SHARE_CLASS_ID"),
					DataResultSetUtils.getResultSetStringValue(rs, "TOTAL_CASH_VALUE"),
					rs.getDateValueByName("LAST_UPDATED"),
					DataResultSetUtils.getResultSetStringValue(rs, "LAST_UPDATED_BY"));
		
	}

	public static DataResultSet getData(int groupId, FWFacade facade) throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addIntParamToBinder(binder, "GROUP_ID", groupId);
		return facade.createResultSet("qCore_GetShareClassGroup", binder);
	}
	
	public static ShareClassGroup get(int groupId, FWFacade facade) throws DataException
	{
		DataResultSet rs = getData(groupId, facade);
		return get(rs);
	}
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		BinderUtils.addIntParamToBinder(binder, "GROUP_ID", this.getGroupId());
		BinderUtils.addParamToBinder(binder, "GROUP_NAME", this.getGroupName());
		BinderUtils.addParamToBinder(binder, "FUND_CODE", this.getFundCode());
		BinderUtils.addBooleanParamToBinder(binder, "IS_OVERRIDDEN", this.isOverridden());
		BinderUtils.addIntParamToBinder(binder, "SHARE_CLASS_ID", this.getShareClassId());
		BinderUtils.addParamToBinder(binder, "TOTAL_CASH_VALUE", this.getTotalCash());
		BinderUtils.addDateParamToBinder(binder, "LAST_UPDATED", this.getLastUpdated());
		BinderUtils.addParamToBinder(binder, "LAST_UPDATED_BY", this.getLastUpdatedBy());
	}
	
	public static ShareClassGroup add(DataBinder binder, FWFacade facade, String username) throws NumberFormatException, DataException
	{
		// get new id value
		int groupId = Integer.parseInt(
				 CCLAUtils.getNewKey("ShareClassGroup", facade));
		ShareClassGroup group = new ShareClassGroup(binder);
		
		group.setGroupId(groupId);
		group.setLastUpdatedBy(username);
		group.validate(facade);
		group.addFieldsToBinder(binder);
		
		facade.execute("qCore_InsertShareClassGroup", binder);
		
		// Add audit record
		DataResultSet afterData = ShareClassGroup.getData(groupId, facade);

		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(groupId, ElementType.SecondaryElementType.ShareClassGroup.toString());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.ADD.toString(), 
		 ElementType.SecondaryElementType.ShareClassGroup.toString(), 
		 null, afterData, auditRelations);	
		return group;
		
	}

	
	public static ShareClassGroup add(int groupId, String groupName, String fundCode,
			boolean isOverridden, Integer shareClassId, String totalCashValue,
			Date lastUpdated, String lastUpdatedBy, FWFacade facade, String username) throws NumberFormatException, DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addIntParamToBinder(binder, "GROUP_ID", groupId);
		BinderUtils.addParamToBinder(binder, "GROUP_NAME", groupName);
		BinderUtils.addParamToBinder(binder, "FUND_CODE", fundCode);
		BinderUtils.addBooleanParamToBinder(binder, "IS_OVERRIDDEN", isOverridden);
		BinderUtils.addIntParamToBinder(binder, "SHARE_CLASS_ID", shareClassId);
		BinderUtils.addParamToBinder(binder, "TOTAL_CASH_VALUE", totalCashValue);
		BinderUtils.addDateParamToBinder(binder, "LAST_UPDATED", lastUpdated);
		BinderUtils.addParamToBinder(binder, "LAST_UPDATED_BY", lastUpdatedBy);
		ShareClassGroup move = ShareClassGroup.add(binder, facade, username);
		return move;
	}
	
	public void persist(FWFacade facade, String username) throws DataException {
		DataResultSet beforeData = ShareClassGroup.getData(this.getGroupId(), facade);
		this.validate(facade);
		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);
		facade.execute("qCore_UpdateShareClassGroup", binder);
		
		// Add audit record
		DataResultSet afterData = ShareClassGroup.getData(this.getGroupId(), facade);

		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(this.getGroupId(), ElementType.SecondaryElementType.ShareClassGroup.toString());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.UPDATE.toString(), 
		 ElementType.SecondaryElementType.ShareClassGroup.toString(), 
		 beforeData, afterData, auditRelations);	
	}

	
	public void setAttributes(DataBinder binder) throws DataException {
		this.setGroupName(BinderUtils.getBinderStringValue(binder, "GROUP_NAME"));
		this.setFundCode(BinderUtils.getBinderStringValue(binder, "FUND_CODE"));
		this.setOverridden(BinderUtils.getBinderBoolValue(binder, "IS_OVERRIDDEN"));
		this.setShareClassId(BinderUtils.getBinderIntegerValue(binder, "SHARE_CLASS_ID"));
		this.setTotalCash(BinderUtils.getBinderStringValue(binder, "TOTAL_CASH_VALUE"));
		this.setLastUpdated(BinderUtils.getBinderDateValue(binder, "LAST_UPDATED"));
		this.setLastUpdatedBy(BinderUtils.getBinderStringValue(binder, "LAST_UPDATED_BY"));	
	}
	
	/**
	 * Gets the shareClassGroup of a particular account
	 * 
	 * @param accountId
	 * @param date
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static ShareClassGroup getByAccountId(int accountId, FWFacade facade) 
	throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addIntParamToBinder(binder, "ACCOUNT_ID", accountId);
		DataResultSet rsSCM = facade.createResultSet("qCore_GetShareClassGroupByAccount", binder);
		if (rsSCM.isEmpty())
			return null;
		else
		{
			do {
				return (get(rsSCM));
			} while (rsSCM.next());
		}
		
	}

	/**
	 * Gets the accounts that are in a particular group
	 * 
	 * @param accountId
	 * @param date
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getShareClassGroupAccounts(int groupId, FWFacade facade) 
	throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addIntParamToBinder(binder, "GROUP_ID", groupId);
		DataResultSet rsSCM = facade.createResultSet("qCore_GetShareClassGroupAccounts", binder);
		if (rsSCM.isEmpty())
			return null;
		else
		{
				return (rsSCM);
		}		
	}	

	/**
	 * Gets the share class groups
	 * 
	 * @param accountId
	 * @param date
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getAllShareClassGroups(FWFacade facade) 
	throws DataException
	{
		DataBinder binder = new DataBinder();
		DataResultSet rsSCM = facade.createResultSet("qCore_GetShareClassGroups", binder);
		if (rsSCM.isEmpty())
			return null;
		else
		{
				return (rsSCM);
		}
		
	}	
	
	/**
	 * Gets the share class groups
	 * 
	 * @param accountId
	 * @param date
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getShareClassGroupsByFund(String fundCode, FWFacade facade) 
	throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addParamToBinder(binder, "FUND_CODE", fundCode);
		DataResultSet rsSCM = facade.createResultSet("qCore_GetShareClassGroupsByFund", binder);
		if (rsSCM.isEmpty())
			return null;
		else
		{
				return (rsSCM);
		}
		
	}	
	
	
	public static void updateShareClassGroupTotalCash(String fundCode, String username, FWFacade facade) throws DataException
	{
		updateShareClassGroupTotalCash(fundCode, null, username, facade);
	}
	/** Updates the share class group object by getting all accounts in the share class group
	 * 1) look for account_value_audit for today's date
	 * 2) if no account value audit then use cash value from account_value table
	 * 
	 * 
	 * @param fundCode
	 * @param username
	 * @param facade
	 * @throws DataException 
	 */
	public static void updateShareClassGroupTotalCash(String fundCode, Date eodDate, String username, FWFacade facade) throws DataException
	{
		if (eodDate==null)
			eodDate = new Date();
		
		// get all share class groups for the fund
		DataResultSet rsGroups = ShareClassGroup.getShareClassGroupsByFund(fundCode, facade);
		if (rsGroups!=null)
		{
			do {
				ShareClassGroup group = get(rsGroups);
				Log.debug("getting accounts for group " + group.getGroupId());
				BigDecimal groupTotal = new BigDecimal("0");
				DataResultSet rsAccounts = ShareClassGroup.getShareClassGroupAccounts(group.getGroupId(), facade);
				if (rsAccounts!=null)
				{
					do {
						// get the account
						Account account = Account.get(rsAccounts);
						AccountValueAudit ava = AccountValueAudit.get(account.getAccountId(), eodDate, facade);
						if (ava != null)
						{
							// use this value
							String avaCash = ava.getClosingCash();
							Log.debug("got ava and using value:" + avaCash);
							if (!StringUtils.stringIsBlank(avaCash))
								groupTotal = groupTotal.add(new BigDecimal(avaCash));
						} else
						{
							// get the value from the account_value table
							AccountValue av = AccountValue.get(account.getAccountId(), facade);
							String avCash = av.getCash();
							if (!StringUtils.stringIsBlank(avCash))
								groupTotal = groupTotal.add(new BigDecimal(avCash));
						}
					} while (rsAccounts.next());
				}
				Log.debug("groupTotal is " + groupTotal.toString()+" for group id:"+group.getGroupId());
				group.setTotalCash(groupTotal.toString());
				group.persist(facade, username);
				Log.debug("saved group " + group.getGroupId() + " with cash value " + groupTotal.toString());
			} while (rsGroups.next());
		}
		
		
	}
	
	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub
		
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getFundCode() {
		return fundCode;
	}

	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}

	public boolean isOverridden() {
		return isOverridden;
	}

	public void setOverridden(boolean isOverridden) {
		this.isOverridden = isOverridden;
	}



	public String getTotalCash() {
		return totalCash;
	}

	public void setTotalCash(String totalCash) {
		this.totalCash = totalCash;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Integer getShareClassId() {
		return shareClassId;
	}

	public void setShareClassId(Integer shareClassId) {
		this.shareClassId = shareClassId;
	}



}
