package com.ecs.ucm.ccla.data;

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

public class AccountFlagApplied implements Persistable {
	
	private int accountFlagAppliedId;
	private int accountId;
	private int accountFlagId;
	private Date dateAdded;

	
	public AccountFlagApplied(int accountFlagAppliedId, int accountId, int accountFlagId, Date dateAdded)
	{
		this.accountFlagAppliedId = accountFlagAppliedId;
		this.accountId = accountId;
		this.accountFlagId = accountFlagId;
		this.dateAdded = dateAdded;
	}
	
	
	public static AccountFlagApplied get(int accountId, int accountFlagId, FWFacade facade) 
	throws DataException
	{
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, "ACCOUNT_ID", accountId);
		CCLAUtils.addQueryIntParamToBinder(binder, "ACCOUNT_FLAG_ID", accountFlagId);
		DataResultSet rs = facade.createResultSet("qClientServices_GetAccountFlagAppliedByFlag", binder);		
		return get(rs);		
	}
	
	public static AccountFlagApplied get(DataResultSet rs) throws DataException
	{
	 if (rs.isEmpty())
	 {
		return null; 
	 } else {
		return new AccountFlagApplied(
				CCLAUtils.getResultSetIntegerValue(rs, "ACCOUNT_FLAG_APPLIED_ID"),
				CCLAUtils.getResultSetIntegerValue(rs, "ACCOUNT_ID"),
				CCLAUtils.getResultSetIntegerValue(rs, "ACCOUNT_FLAG_ID"),
				rs.getDateValueByName("DATE_ADDED"));
	 }
	}

	public static AccountFlagApplied add(int accountId, int accountFlagId, Date dateAdded,
			FWFacade facade, String username) throws NumberFormatException, DataException
	{
		AccountFlagApplied existingFlag = AccountFlagApplied.get(accountId, accountFlagId, facade);
		if (existingFlag != null)
		{
			return existingFlag;
		}
		else
		{
			int accountFlagAppliedId = Integer.parseInt
			 (CCLAUtils.getNewKey("AccountFlagApplied", facade));
			
			AccountFlagApplied flag = new AccountFlagApplied
			 (accountFlagAppliedId, accountId, accountFlagId, dateAdded);
			DataBinder binder = new DataBinder();
			flag.addFieldsToBinder(binder);
			facade.execute("qClientServices_AddAccountFlagApplied", binder);
			
			// add audit
			HashMap<Integer, String> auditRelations = 
				 new HashMap<Integer, String>();
				
			auditRelations.put
			 (flag.getAccountFlagAppliedId(), 
			 ElementType.SecondaryElementType.AccountFlag.toString());
			auditRelations.put(flag.getAccountId(), ElementType.ACCOUNT.getName());
			
			Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
			 Globals.AuditActions.ADD.toString(), 
			 ElementType.SecondaryElementType.AccountFlag.toString(), 
			 null, getData(flag.getAccountFlagAppliedId(),facade), auditRelations);
			
			return flag;
		}
	}
	
	public static DataResultSet getData(int accountFlagAppliedId, FWFacade facade) throws DataException
	{
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "ACCOUNT_FLAG_APPLIED_ID", accountFlagAppliedId);
		
		return facade.createResultSet
		 ("qClientServices_GetAccountFlagAppliedbyId", binder);
	}
	
	public static void remove(int accountId, int accountFlagId, FWFacade facade, String username) 
	throws DataException
	{
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, "ACCOUNT_ID", accountId);
		CCLAUtils.addQueryIntParamToBinder(binder, "ACCOUNT_FLAG_ID", accountFlagId);
		
		AccountFlagApplied flag = get(accountId, accountFlagId, facade);
		if (flag != null)
		{
			// add audit
			HashMap<Integer, String> auditRelations = 
				 new HashMap<Integer, String>();
				
				auditRelations.put
				 (flag.getAccountFlagAppliedId(), 
				 ElementType.SecondaryElementType.AccountFlag.toString());
				auditRelations.put(flag.getAccountId(), ElementType.ACCOUNT.getName());
				
				
				Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
				 Globals.AuditActions.DELETE.toString(), 
				 ElementType.SecondaryElementType.AccountFlag.toString(), 
				  getData(flag.getAccountFlagAppliedId(), facade), null, auditRelations);
			
			
		facade.execute("qClientServices_DeleteAccountFlagApplied", binder);
		}
	}
	
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		CCLAUtils.addQueryIntParamToBinder(binder, "ACCOUNT_FLAG_APPLIED_ID", this.getAccountFlagAppliedId());
		CCLAUtils.addQueryIntParamToBinder(binder, "ACCOUNT_ID", this.getAccountId());
		CCLAUtils.addQueryIntParamToBinder(binder, "ACCOUNT_FLAG_ID", this.getAccountFlagId());
		CCLAUtils.addQueryDateParamToBinder(binder, "DATE_ADDED", this.getDateAdded());

	}

	public void persist(FWFacade facade, String username) throws DataException {
		// TODO Auto-generated method stub
		//not needed
	}

	public void setAttributes(DataBinder binder) throws DataException {
		// TODO Auto-generated method stub

	}

	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub

	}

	public int getAccountFlagAppliedId() {
		return accountFlagAppliedId;
	}

	public void setAccountFlagAppliedId(int accountFlagAppliedId) {
		this.accountFlagAppliedId = accountFlagAppliedId;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public int getAccountFlagId() {
		return accountFlagId;
	}

	public void setAccountFlagId(int accountFlagId) {
		this.accountFlagId = accountFlagId;
	}

	public Date getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}
	
	/** Fetches all applied Account Flags for this account. 
	 * 
	 * @param accountId
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static Vector<AccountFlag> get(int accountId, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, SharedCols.ACCOUNT, accountId);
		
		DataResultSet rs = facade.createResultSet
		 ("qClientServices_GetAccountFlagApplied", binder);
		
		return AccountFlag.getAll(rs);
	}
	
	/** Adds or removes the given set of Account Flags from the given Account ID.
	 *
	 * @param accountId
	 * @throws DataException 
	 * @throws NumberFormatException 
	 */
	public static void addOrRemove
	 (int accountId, Vector<Integer> accountFlagIds, Relation.UpdateType updateType, 
	 FWFacade facade, String userName) throws DataException {
		
		Vector<AccountFlag> accountFlagsApplied = 
		 get(accountId, facade);
		
		for (Integer accountFlagId : accountFlagIds) {
			boolean flagApplied = false;
			
			for (AccountFlag accountFlagApplied : accountFlagsApplied) {
				if (accountFlagApplied.getAccountFlagId() == accountFlagId) {
					flagApplied = true;
					break;
				}
			}
			
			if (updateType == Relation.UpdateType.ADD) {
				// Add flag if the account doesn't already have it.
				if (!flagApplied) {
					add(accountId, accountFlagId, new Date(), facade, userName);
				}
			} else {
				// Remove flag if the account already has it set.
				if (flagApplied) {
					remove(accountId, accountFlagId, facade, userName);
				}
			}
		}
	}
}
