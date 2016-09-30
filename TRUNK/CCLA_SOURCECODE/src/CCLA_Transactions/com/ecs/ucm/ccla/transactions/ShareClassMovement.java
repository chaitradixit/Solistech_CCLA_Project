package com.ecs.ucm.ccla.transactions;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.data.Audit;
import com.ecs.ucm.ccla.data.Globals;
import com.ecs.ucm.ccla.data.Persistable;
import com.ecs.ucm.ccla.transactions.globals.TransactionGlobals;
import com.ecs.ucm.ccla.transactions.utils.TransactionUtils;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.DataResultSetUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

public class ShareClassMovement implements Persistable{

	private int movementId;
	private int accountId;
	private int moveTypeId;
	private Date moveDate;	
	private String newShareClassId;
	private String oldShareClassId;

	public ShareClassMovement(int movementId, int accountId, int moveTypeId, Date moveDate,  
			String newShareClassId, String oldShareClassId)
	{
		this.movementId = movementId;
		this.accountId = accountId;
		this.moveDate = moveDate;
		this.moveTypeId = moveTypeId;
		this.newShareClassId = newShareClassId;
		this.oldShareClassId = oldShareClassId;
	}
	
	public ShareClassMovement(DataBinder binder) throws DataException
	{
		this.setAttributes(binder);
	}
	
	public static ShareClassMovement get(DataResultSet rs) throws DataException
	{
		if (rs.isEmpty())
			return null;
		else
			return new ShareClassMovement(
					DataResultSetUtils.getResultSetIntegerValue(rs, "SHARE_CLASS_MOVEMENT_ID"),
					DataResultSetUtils.getResultSetIntegerValue(rs, "ACCOUNT_ID"),
					DataResultSetUtils.getResultSetIntegerValue(rs, "MOVE_TYPE_ID"),
					rs.getDateValueByName("MOVE_DATE"),
					DataResultSetUtils.getResultSetStringValue(rs, "NEW_SHARE_CLASS_ID"),
					DataResultSetUtils.getResultSetStringValue(rs, "OLD_SHARE_CLASS_ID"));
		
	}

	public static DataResultSet getData(int incomeExpenseId, FWFacade facade) throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addIntParamToBinder(binder, "SHARE_CLASS_MOVEMENT_ID", incomeExpenseId);
		return facade.createResultSet("qTransactions_GetShareClassMovementById", binder);
	}
	
	public static ShareClassMovement get(int incomeExpenseId, FWFacade facade) throws DataException
	{
		DataResultSet rs = getData(incomeExpenseId, facade);
		return get(rs);
	}
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		BinderUtils.addIntParamToBinder(binder, "SHARE_CLASS_MOVEMENT_ID", this.getMovementId());
		BinderUtils.addIntParamToBinder(binder, "ACCOUNT_ID", this.getAccountId());
		BinderUtils.addDateParamToBinder(binder, "MOVE_DATE", this.getMoveDate());
		BinderUtils.addIntParamToBinder(binder, "MOVE_TYPE_ID", this.getMoveTypeId());
		BinderUtils.addParamToBinder(binder, "NEW_SHARE_CLASS_ID", this.getNewShareClassId());
		BinderUtils.addParamToBinder(binder, "OLD_SHARE_CLASS_ID", this.getOldShareClassId());

	}
	
	public static ShareClassMovement add(DataBinder binder, FWFacade facade, String username) throws NumberFormatException, DataException
	{
		// get new id value
		int movementId = Integer.parseInt(
				 TransactionUtils.getNewKey("ShareClassMovement", facade));
		ShareClassMovement movement = new ShareClassMovement(binder);
		
		movement.setMovementId(movementId);
		movement.validate(facade);
		movement.addFieldsToBinder(binder);
		
		facade.execute("qTransactions_InsertShareClassMovement", binder);
		
		// Add audit record
		DataResultSet afterData = ShareClassMovement.getData(movementId, facade);

		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(movementId, TransactionGlobals.SecondaryElementType.ShareClassMovement.toString());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.ADD.toString(), 
		 TransactionGlobals.SecondaryElementType.ShareClassMovement.toString(), 
		 null, afterData, auditRelations);	
		return movement;
		
	}

	
	public static ShareClassMovement add(int accountId, Date moveDate, int moveTypeId, 
			String newShareClassId, String oldShareClassId, FWFacade facade, String username) throws NumberFormatException, DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addIntParamToBinder(binder, "ACCOUNT_ID", accountId);
		BinderUtils.addDateParamToBinder(binder, "MOVE_DATE", moveDate);
		BinderUtils.addIntParamToBinder(binder, "MOVE_TYPE_ID", moveTypeId);
		BinderUtils.addParamToBinder(binder, "OLD_SHARE_CLASS_ID", oldShareClassId);
		BinderUtils.addParamToBinder(binder, "NEW_SHARE_CLASS_ID", newShareClassId);
		ShareClassMovement move = ShareClassMovement.add(binder, facade, username);
		return move;
	}
	
	public void persist(FWFacade facade, String username) throws DataException {
		DataResultSet beforeData = ShareClassMovement.getData(this.getMovementId(), facade);
		this.validate(facade);
		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);
		facade.execute("qTransactions_UpdateShareClassMovement", binder);
		
		// Add audit record
		DataResultSet afterData = ShareClassMovement.getData(this.getMovementId(), facade);

		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(this.getMovementId(), TransactionGlobals.SecondaryElementType.ShareClassMovement.toString());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.UPDATE.toString(), 
		 TransactionGlobals.SecondaryElementType.ShareClassMovement.toString(), 
		 beforeData, afterData, auditRelations);	
	}

	
	public void setAttributes(DataBinder binder) throws DataException {
		
		this.setAccountId(BinderUtils.getBinderIntegerValue(binder, "ACCOUNT_ID"));
		this.setMoveDate(BinderUtils.getBinderDateValue(binder, "MOVE_DATE"));
		this.setMoveTypeId(BinderUtils.getBinderIntegerValue(binder, "MOVE_TYPE_ID"));
		this.setOldShareClassId(BinderUtils.getBinderStringValue(binder, "OLD_SHARE_CLASS_ID"));
		this.setNewShareClassId(BinderUtils.getBinderStringValue(binder, "NEW_SHARE_CLASS_ID"));
	
	}
	
	public static void remove(int expenseId, FWFacade facade) throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addIntParamToBinder(binder, "SHARE_CLASS_MOVEMENT_ID", expenseId);
		facade.execute("qTransactions_DeleteShareClassMovement", binder);
		
	}
	public static void removeType(Date date, String moveType, FWFacade facade) throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addParamToBinder(binder, "MOVE_TYPE_ID", moveType);
		BinderUtils.addDateParamToBinder(binder, "MOVE_DATE", date);
		facade.execute("qTransactions_DeleteDailyShareClassMovementbyType", binder);
		
	}
	/**
	 * Gets the shareclassmovement for an account on a particular day or null if no such
	 * share class movement exists
	 * 
	 * @param accountId
	 * @param date
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static ShareClassMovement getByAccountId(int accountId, int moveType, Date date, FWFacade facade) 
	throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addIntParamToBinder(binder, "ACCOUNT_ID", accountId);
		BinderUtils.addIntParamToBinder(binder, "MOVE_TYPE_ID", moveType);
		BinderUtils.addDateParamToBinder(binder, "MOVE_DATE", date);
		DataResultSet rsSCM = facade.createResultSet("qTransactions_GetShareClassMovement", binder);
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
	 * Gets the shareclassmovement for an account on a particular day or null if no such
	 * share class movement exists
	 * 
	 * @param accountId
	 * @param date
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static Vector<ShareClassMovement> getByDateFund(String fundCode, int moveType, Date date, FWFacade facade) 
	throws DataException
	{
		Vector<ShareClassMovement> v = new Vector<ShareClassMovement>();
		DataBinder binder = new DataBinder();
		BinderUtils.addParamToBinder(binder, "FUND_CODE", fundCode);
		BinderUtils.addIntParamToBinder(binder, "MOVE_TYPE_ID", moveType);
		BinderUtils.addDateParamToBinder(binder, "MOVE_DATE", date);
		DataResultSet rsSCM = facade.createResultSet("qTransactions_GetShareClassMovementbyFund", binder);
		if (rsSCM.isEmpty())
			return null;
		else
		{
			do {
				v.add(get(rsSCM));
			} while (rsSCM.next());
			return v;
		}
		
	}	
	
	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub
		
	}

	public int getMovementId() {
		return movementId;
	}

	public void setMovementId(int movementId) {
		this.movementId = movementId;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public Date getMoveDate() {
		return moveDate;
	}

	public void setMoveDate(Date moveDate) {
		this.moveDate = moveDate;
	}

	public String getOldShareClassId() {
		return oldShareClassId;
	}

	public void setOldShareClassId(String oldShareClassId) {
		this.oldShareClassId = oldShareClassId;
	}

	public int getMoveTypeId() {
		return moveTypeId;
	}

	public void setMoveTypeId(int moveTypeId) {
		this.moveTypeId = moveTypeId;
	}

	public String getNewShareClassId() {
		return newShareClassId;
	}

	public void setNewShareClassId(String newShareClassId) {
		this.newShareClassId = newShareClassId;
	}


}
