package com.ecs.ucm.ccla.data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.utils.Log;
import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.DataResultSetUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

public class ShareClassOverride implements Persistable{

	private int accountId;
	private int shareClassId;
	private String userId;
	private Date overrideDate;	
	private String overrideReason;

	public ShareClassOverride(int accountId, int shareClassId, String userId, Date overrideDate,  
			String overrideReason)
	{
		this.accountId = accountId;
		this.shareClassId = shareClassId;
		this.userId = userId;
		this.overrideDate = overrideDate;
		this.overrideReason = overrideReason;
	}
	
	public ShareClassOverride(DataBinder binder) throws DataException
	{
		this.setAttributes(binder);
	}
	
	public static ShareClassOverride get(DataResultSet rs) throws DataException
	{
		if (rs.isEmpty())
			return null;
		else
			return new ShareClassOverride(
					DataResultSetUtils.getResultSetIntegerValue(rs, "ACCOUNT_ID"),
					DataResultSetUtils.getResultSetIntegerValue(rs, "SHARE_CLASS_ID"),
					DataResultSetUtils.getResultSetStringValue(rs, "OVERRIDE_USER_ID"),
					rs.getDateValueByName("OVERRIDE_DATE"),
					DataResultSetUtils.getResultSetStringValue(rs, "OVERRIDE_REASON"));
		
	}

	public static DataResultSet getData(int accountId, FWFacade facade) throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addIntParamToBinder(binder, "ACCOUNT_ID", accountId);
		return facade.createResultSet("qCore_GetShareClassOverride", binder);
	}
	
	public static ShareClassOverride get(int accountId, FWFacade facade) throws DataException
	{
		DataResultSet rs = getData(accountId, facade);
		return get(rs);
	}
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		BinderUtils.addIntParamToBinder(binder, "ACCOUNT_ID", this.getAccountId());
		BinderUtils.addIntParamToBinder(binder, "SHARE_CLASS_ID", this.getShareClassId());
		// BinderUtils.addDateParamToBinder(binder, "OVERRIDE_DATE", this.getOverrideDate());
		BinderUtils.addParamToBinder(binder, "OVERRIDE_USER_ID", this.getUserId());
		BinderUtils.addParamToBinder(binder, "OVERRIDE_REASON", this.getOverrideReason());

	}
	
	public static ShareClassOverride add(DataBinder binder, FWFacade facade, String username) throws NumberFormatException, DataException
	{
		ShareClassOverride override = new ShareClassOverride(binder);
		override.setUserId(username);
		override.validate(facade);
		override.addFieldsToBinder(binder);
		
		facade.execute("qCore_InsertShareClassOverride", binder);
		
		// Add audit record
		DataResultSet afterData = ShareClassOverride.getData(override.getAccountId(), facade);

		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(override.getAccountId(), com.ecs.ucm.ccla.data.ElementType.SecondaryElementType.ShareClassOverride.toString());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.ADD.toString(), 
		 com.ecs.ucm.ccla.data.ElementType.SecondaryElementType.ShareClassOverride.toString(), 
		 null, afterData, auditRelations);	
		return override;
		
	}


	
	public void persist(FWFacade facade, String username) throws DataException {
		DataResultSet beforeData = ShareClassOverride.getData(this.getAccountId(), facade);
		this.validate(facade);
		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);
		facade.execute("qCore_UpdateShareClassOverride", binder);
		
		// Add audit record
		DataResultSet afterData = ShareClassOverride.getData(this.getAccountId(), facade);

		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(this.getAccountId(), com.ecs.ucm.ccla.data.ElementType.SecondaryElementType.ShareClassOverride.toString());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.UPDATE.toString(), 
		 com.ecs.ucm.ccla.data.ElementType.SecondaryElementType.ShareClassOverride.toString(), 
		 beforeData, afterData, auditRelations);	
	}

	
	public void setAttributes(DataBinder binder) throws DataException {
		
		this.setAccountId(BinderUtils.getBinderIntegerValue(binder, "ACCOUNT_ID"));
		this.setShareClassId(BinderUtils.getBinderIntegerValue(binder, "SHARE_CLASS_ID"));
		//this.setUserId(BinderUtils.getBinderStringValue(binder, "OVERRIDE_USER_ID"));
		//this.setOverrideDate(BinderUtils.getBinderDateValue(binder, "OVERRIDE_DATE"));
		this.setOverrideReason(BinderUtils.getBinderStringValue(binder, "OVERRIDE_REASON"));
	
	}
	
	public static void remove(int accountId, FWFacade facade) throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addIntParamToBinder(binder, "ACCOUNT_ID", accountId);
		facade.execute("qCore_DeleteShareClassOverride", binder);
		
	}

	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub		
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public int getShareClassId() {
		return shareClassId;
	}

	public void setShareClassId(int shareClassId) {
		this.shareClassId = shareClassId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getOverrideDate() {
		return overrideDate;
	}

	public void setOverrideDate(Date overrideDate) {
		this.overrideDate = overrideDate;
	}

	public String getOverrideReason() {
		return overrideReason;
	}

	public void setOverrideReason(String overrideReason) {
		this.overrideReason = overrideReason;
	}

	
}
