package com.ecs.ucm.ccla.data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.DataResultSetUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

public class ShareClassGroupApplied implements Persistable{

	private int groupAppliedId;
	private int groupId;
	private int accountId;
	private Date lastUpdated;
	private String lastUpdatedBy;

	public ShareClassGroupApplied(int groupAppliedId, int groupId, int accountId,
			Date lastUpdated, String lastUpdatedBy)
	{
		this.groupAppliedId = groupAppliedId;
		this.groupId = groupId;
		this.accountId = accountId;
		this.lastUpdated = lastUpdated;
		this.lastUpdatedBy=lastUpdatedBy;
	}
	
	public ShareClassGroupApplied(DataBinder binder) throws DataException
	{
		this.setAttributes(binder);
	}
	
	public static ShareClassGroupApplied get(DataResultSet rs) throws DataException
	{
		if (rs.isEmpty())
			return null;
		else
			return new ShareClassGroupApplied(
					DataResultSetUtils.getResultSetIntegerValue(rs, "GROUP_APPLIED_ID"),
					DataResultSetUtils.getResultSetIntegerValue(rs, "GROUP_ID"),
					DataResultSetUtils.getResultSetIntegerValue(rs, "ACCOUNT_ID"),
					rs.getDateValueByName("LAST_UPDATED"),
					DataResultSetUtils.getResultSetStringValue(rs, "LAST_UPDATED_BY"));
		
	}

	public static DataResultSet getData(int groupAppliedId, FWFacade facade) throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addIntParamToBinder(binder, "GROUP_APPLIED_ID", groupAppliedId);
		return facade.createResultSet("qCore_GetShareClassGroupApplied", binder);
	}
	
	
	public static void delete(int groupAppliedId, FWFacade facade, String username) throws DataException
	{
		DataResultSet beforeData = ShareClassGroupApplied.getData(groupAppliedId, facade);
		DataBinder binder = new DataBinder();
		BinderUtils.addIntParamToBinder(binder, "GROUP_APPLIED_ID", groupAppliedId);
		facade.execute("qCore_DeleteShareClassGroupApplied", binder);
		
		// Add audit record
		DataResultSet afterData = null;

		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(groupAppliedId, ElementType.SecondaryElementType.ShareClassGroupApplied.toString());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.DELETE.toString(), 
		 ElementType.SecondaryElementType.ShareClassGroupApplied.toString(), 
		 beforeData, afterData, auditRelations);	

	}
	
	public static ShareClassGroupApplied get(int groupAppliedId, FWFacade facade) throws DataException
	{
		DataResultSet rs = getData(groupAppliedId, facade);
		return get(rs);
	}
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		BinderUtils.addIntParamToBinder(binder, "GROUP_APPLIED_ID", this.getGroupAppliedId());
		BinderUtils.addIntParamToBinder(binder, "GROUP_ID", this.getGroupId());
		BinderUtils.addIntParamToBinder(binder, "ACCOUNT_ID", this.getAccountId());
		BinderUtils.addDateParamToBinder(binder, "LAST_UPDATED", this.getLastUpdated());
		BinderUtils.addParamToBinder(binder, "LAST_UPDATED_BY", this.getLastUpdatedBy());
	}
	
	public static ShareClassGroupApplied add(DataBinder binder, FWFacade facade, String username) throws NumberFormatException, DataException
	{
		// get new id value
		int groupAppliedId = Integer.parseInt(
				 CCLAUtils.getNewKey("ShareClassGroupApplied", facade));
		ShareClassGroupApplied group = new ShareClassGroupApplied(binder);
		
		group.setGroupAppliedId(groupAppliedId);
		group.setLastUpdatedBy(username);
		group.validate(facade);
		group.addFieldsToBinder(binder);
		
		facade.execute("qCore_InsertShareClassGroupApplied", binder);
		
		// Add audit record
		DataResultSet afterData = ShareClassGroupApplied.getData(groupAppliedId, facade);

		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(groupAppliedId, ElementType.SecondaryElementType.ShareClassGroupApplied.toString());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.ADD.toString(), 
		 ElementType.SecondaryElementType.ShareClassGroupApplied.toString(), 
		 null, afterData, auditRelations);	
		return group;
		
	}

	
	public static ShareClassGroupApplied add(int groupAppliedId, int groupId, int accountId,
			Date lastUpdated, String lastUpdatedBy, FWFacade facade, String username) throws NumberFormatException, DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addIntParamToBinder(binder, "GROUP_APPLIED_ID", groupAppliedId);
		BinderUtils.addIntParamToBinder(binder, "GROUP_ID", groupId);
		BinderUtils.addIntParamToBinder(binder, "ACCOUNT_ID", accountId);
		BinderUtils.addDateParamToBinder(binder, "LAST_UPDATED", lastUpdated);
		BinderUtils.addParamToBinder(binder, "LAST_UPDATED_BY", lastUpdatedBy);
		ShareClassGroupApplied move = ShareClassGroupApplied.add(binder, facade, username);
		return move;
	}
	
	public void persist(FWFacade facade, String username) throws DataException {
		DataResultSet beforeData = ShareClassGroupApplied.getData(this.getGroupAppliedId(), facade);
		this.validate(facade);
		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);
		facade.execute("qCore_UpdateShareClassGroupApplied", binder);
		
		// Add audit record
		DataResultSet afterData = ShareClassGroupApplied.getData(this.getGroupAppliedId(), facade);

		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(this.getGroupAppliedId(), ElementType.SecondaryElementType.ShareClassGroupApplied.toString());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.UPDATE.toString(), 
		 ElementType.SecondaryElementType.ShareClassGroupApplied.toString(), 
		 beforeData, afterData, auditRelations);	
	}

	
	public void setAttributes(DataBinder binder) throws DataException {

		this.setGroupId(BinderUtils.getBinderIntegerValue(binder, "GROUP_ID"));
		this.setAccountId(BinderUtils.getBinderIntegerValue(binder, "ACCOUNT_ID"));
		this.setLastUpdated(BinderUtils.getBinderDateValue(binder, "LAST_UPDATED"));
		this.setLastUpdatedBy(BinderUtils.getBinderStringValue(binder, "LAST_UPDATED_BY"));
	
	}
	

	/**
	 * Gets the shareClassGroupApplied for a particular account
	 * 
	 * @param accountId
	 * @param date
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static ShareClassGroupApplied getByAccountId(int accountId,  FWFacade facade) 
	throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addIntParamToBinder(binder, "ACCOUNT_ID", accountId);
		DataResultSet rsSCM = facade.createResultSet("qCore_GetShareClassGroupAppliedByAccount", binder);
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
	 * Gets the shareClassGroupApplied for a particular account
	 * 
	 * @param accountId
	 * @param date
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static ShareClassGroupApplied getByAccountIdGroupId(int accountId,  int groupId, FWFacade facade) 
	throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addIntParamToBinder(binder, "ACCOUNT_ID", accountId);
		BinderUtils.addIntParamToBinder(binder, "GROUP_ID", groupId);
		DataResultSet rsSCM = facade.createResultSet("qCore_GetShareClassGroupAppliedByAccountGroup", binder);
		if (rsSCM.isEmpty())
			return null;
		else
		{
			do {
				return (get(rsSCM));
			} while (rsSCM.next());
		}
		
	}
	
	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub
		
	}

	public int getGroupAppliedId() {
		return groupAppliedId;
	}

	public void setGroupAppliedId(int groupAppliedId) {
		this.groupAppliedId = groupAppliedId;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
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

	
}
