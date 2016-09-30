package com.ecs.ucm.ccla.transactions;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.data.Audit;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.Globals;
import com.ecs.ucm.ccla.data.Persistable;
import com.ecs.ucm.ccla.transactions.globals.TransactionGlobals;
import com.ecs.ucm.ccla.transactions.utils.TransactionUtils;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.DataResultSetUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

public class IncomeExpense implements Persistable{
	

	private int incomeExpenseId;
	private String fundCode;
	private String shareClassId;
	private String incomeExpenseName;
	private Integer incomeExpenseTypeId;
	private String description;
	private String defaultValue;
	private boolean isPercentage;
	private boolean isExternal;
	private boolean isEnabled;
	private Date dateAdded;
	private Date dateUpdated;
	private String updatedBy;

	
	public IncomeExpense(int incomeExpenseId, String fundCode, String shareClassId, String incomeExpenseName,
			Integer incomeExpenseTypeId, String description, String defaultValue, boolean isPercentage, boolean isExternal,
			boolean isEnabled, Date dateAdded, Date dateUpdated, String updatedBy)
	{
		this.incomeExpenseId = incomeExpenseId;
		this.fundCode = fundCode;
		this.shareClassId = shareClassId;
		this.incomeExpenseName = incomeExpenseName;
		this.incomeExpenseTypeId = incomeExpenseTypeId;
		this.description = description;
		this.defaultValue = defaultValue;
		this.isPercentage = isPercentage;
		this.isExternal = isExternal;
		this.isEnabled = isEnabled;
		this.dateAdded = dateAdded;
		this.dateUpdated = dateUpdated;
		this.updatedBy = updatedBy;
	}
	
	public IncomeExpense(DataBinder binder) throws DataException
	{
		this.setAttributes(binder);
	}
	
	public static IncomeExpense get(DataResultSet rs) throws DataException
	{
		if (rs== null || rs.isEmpty())
			return null;
		else
			return new IncomeExpense(DataResultSetUtils.getResultSetIntegerValue(rs, "INCOME_EXPENSE_ID"),
					DataResultSetUtils.getResultSetStringValue(rs, "FUND_CODE"),
					DataResultSetUtils.getResultSetStringValue(rs, "SHARE_CLASS_ID"),
					DataResultSetUtils.getResultSetStringValue(rs, "INCOME_EXPENSE_NAME"),
					DataResultSetUtils.getResultSetIntegerValue(rs, "INCOME_EXPENSE_TYPE_ID"),
					DataResultSetUtils.getResultSetStringValue(rs, "DESCRIPTION"),
					DataResultSetUtils.getResultSetStringValue(rs, "DEFAULT_VALUE"),
					DataResultSetUtils.getResultSetBoolValue(rs, "IS_PERCENTAGE"),
					DataResultSetUtils.getResultSetBoolValue(rs, "IS_EXTERNAL"),
					DataResultSetUtils.getResultSetBoolValue(rs, "IS_ENABLED"),
					rs.getDateValueByName("DATE_ADDED"),
					rs.getDateValueByName("LAST_UPDATED"),
					DataResultSetUtils.getResultSetStringValue(rs, "LAST_UPDATED_BY"));
		
	}

	public static DataResultSet getData(int incomeExpenseId, FWFacade facade) throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addIntParamToBinder(binder, "INCOME_EXPENSE_ID", incomeExpenseId);
		return facade.createResultSet("qTransactions_GetIncomeExpense", binder);
	}
	
	public static IncomeExpense get(int incomeExpenseId, FWFacade facade) throws DataException
	{
		DataResultSet rs = getData(incomeExpenseId, facade);
		return get(rs);
	}
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		BinderUtils.addIntParamToBinder(binder, "INCOME_EXPENSE_ID", this.getIncomeExpenseId());
		BinderUtils.addParamToBinder(binder, "FUND_CODE", this.getFundCode());
		BinderUtils.addParamToBinder(binder, "SHARE_CLASS_ID", this.getShareClassId());
		BinderUtils.addParamToBinder(binder, "INCOME_EXPENSE_NAME", this.getIncomeExpenseName());
		BinderUtils.addIntParamToBinder(binder, "INCOME_EXPENSE_TYPE_ID", this.getIncomeExpenseTypeId());
		BinderUtils.addParamToBinder(binder, "DESCRIPTION", this.getDescription());
		BinderUtils.addParamToBinder(binder, "DEFAULT_VALUE", this.getDefaultValue());
		BinderUtils.addBooleanParamToBinder(binder, "IS_PERCENTAGE", this.isPercentage());
		BinderUtils.addBooleanParamToBinder(binder, "IS_EXTERNAL", this.isExternal());
		BinderUtils.addBooleanParamToBinder(binder, "IS_ENABLED", this.isEnabled());
		BinderUtils.addDateParamToBinder(binder, "DATE_ADDED", this.getDateAdded());
		BinderUtils.addDateParamToBinder(binder, "LAST_UPDATED", this.getDateUpdated());
		BinderUtils.addParamToBinder(binder, "LAST_UPDATED_BY", this.getUpdatedBy());
		
	}
	
	public static IncomeExpense add(DataBinder binder, FWFacade facade, String username) throws NumberFormatException, DataException
	{
		// get new id value
		int expenseId = Integer.parseInt(
				 TransactionUtils.getNewKey("IncomeExpense", facade));
		IncomeExpense expense = new IncomeExpense(binder);
		expense.setUpdatedBy(username);
		expense.setIncomeExpenseId(expenseId);
		expense.validate(facade);
		expense.addFieldsToBinder(binder);
		
		facade.execute("qTransactions_InsertIncomeExpense", binder);
		
		// Add audit record
		DataResultSet afterData = IncomeExpense.getData(expense.getIncomeExpenseId(), facade);

		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(expense.getIncomeExpenseId(), TransactionGlobals.SecondaryElementType.IncomeExpense.toString());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.ADD.toString(), 
		 TransactionGlobals.SecondaryElementType.IncomeExpense.toString(), 
		 null, afterData, auditRelations);	
		
		return expense;
		
	}

	public static IncomeExpense  add(String fundCode, String shareClassId, String incomeExpenseName,
			int incomeExpenseTypeId, String description, String defaultValue, boolean isPercentage, boolean isExternal,
			boolean isEnabled, String username, FWFacade facade) throws NumberFormatException, DataException
	{
		IncomeExpense ie = new IncomeExpense(0, fundCode, shareClassId, incomeExpenseName, incomeExpenseTypeId,
				description, defaultValue, isPercentage, isExternal, isEnabled, new Date(), new Date(), username);
		DataBinder binder = new DataBinder();
		ie.addFieldsToBinder(binder);
		ie = IncomeExpense.add(binder, facade, username);
		
		return ie;
	}
	
	public void persist(FWFacade facade, String username) throws DataException {
		DataResultSet beforeData = IncomeExpense.getData(this.getIncomeExpenseId(), facade);
		this.validate(facade);
		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);
		facade.execute("qTransactions_UpdateIncomeExpense", binder);
		DataResultSet afterData = IncomeExpense.getData(this.getIncomeExpenseId(), facade);
		
		// Add audit record
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(this.getIncomeExpenseId(), TransactionGlobals.SecondaryElementType.IncomeExpense.toString());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.UPDATE.toString(), 
		 TransactionGlobals.SecondaryElementType.IncomeExpense.toString(), 
		 beforeData, afterData, auditRelations);	
	}

	
	public void setAttributes(DataBinder binder) throws DataException {
		
		this.setFundCode(BinderUtils.getBinderStringValue(binder, "FUND_CODE"));
		this.setShareClassId(BinderUtils.getBinderStringValue(binder, "SHARE_CLASS_ID"));
		this.setIncomeExpenseTypeId(BinderUtils.getBinderIntegerValue(binder, "INCOME_EXPENSE_TYPE_ID"));
		this.setIncomeExpenseName(BinderUtils.getBinderStringValue(binder, "INCOME_EXPENSE_NAME"));
		this.setDescription(BinderUtils.getBinderStringValue(binder, "DESCRIPTION"));
		this.setDefaultValue(BinderUtils.getBinderStringValue(binder, "DEFAULT_VALUE"));
		this.setPercentage(BinderUtils.getBinderBoolValue(binder, "IS_PERCENTAGE"));
		this.setExternal(BinderUtils.getBinderBoolValue(binder, "IS_EXTERNAL"));
		this.setEnabled(BinderUtils.getBinderBoolValue(binder, "IS_ENABLED"));
		
		
	}
	/** Disables income expense (these should not be deleted as they are needed for auditing
	 * purposes)
	 * 
	 * @param expenseId
	 * @param facade
	 * @throws DataException
	 */
	public static void remove(int expenseId, String username, FWFacade facade) throws DataException
	{
		DataResultSet beforeData = IncomeExpense.getData(expenseId, facade);
		DataBinder binder = new DataBinder();
		BinderUtils.addIntParamToBinder(binder, "INCOME_EXPENSE_ID", expenseId);
		facade.execute("qTransactions_DisableIncomeExpense", binder);
		
		DataResultSet afterData = IncomeExpense.getData(expenseId, facade);
		
		// Add audit record
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(expenseId, TransactionGlobals.SecondaryElementType.IncomeExpense.toString());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.UPDATE.toString(), 
		 TransactionGlobals.SecondaryElementType.IncomeExpense.toString(), 
		 beforeData, afterData, auditRelations);			
	}

	
	public static DataResultSet getExpenseByTypeFund(int expenseType, String fundCode, FWFacade facade) 
	throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addIntParamToBinder(binder, "INCOME_EXPENSE_TYPE_ID", expenseType);
		BinderUtils.addParamToBinder(binder, "FUND_CODE", fundCode);
		DataResultSet rs = facade.createResultSet("qTransactions_GetIncomeExpenseByType", binder);	
		if (rs.isEmpty())
		{
			return null;
		} else
		{
			return rs;
		}	
	}
	
	public static DataResultSet getExpenseByFund(String fundCode, FWFacade facade) 
	throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addParamToBinder(binder, "FUND_CODE", fundCode);
		DataResultSet rs = facade.createResultSet("qTransactions_GetExpenseByFund", binder);	
		if (rs.isEmpty())
		{
			return null;
		} else
		{
			Log.debug("found income expense:" + rs.getNumRows());
			return rs;
		}	
	}	
	
	public static Vector<IncomeExpense> getIncomeExpense(DataResultSet rs, FWFacade facade) 
	throws DataException
	{
		Vector<IncomeExpense> vector = new Vector<IncomeExpense>();
		do {
			IncomeExpense ie = get(rs);
			vector.add(ie);
		} while (rs.next());
		return vector;
	}
	
	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub		
	}

	public int getIncomeExpenseId() {
		return incomeExpenseId;
	}

	public void setIncomeExpenseId(int incomeExpenseId) {
		this.incomeExpenseId = incomeExpenseId;
	}

	public String getFundCode() {
		return fundCode;
	}

	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}

	public String getShareClassId() {
		return shareClassId;
	}

	public void setShareClassId(String shareClassId) {
		this.shareClassId = shareClassId;
	}

	public String getIncomeExpenseName() {
		return incomeExpenseName;
	}

	public void setIncomeExpenseName(String incomeExpenseName) {
		this.incomeExpenseName = incomeExpenseName;
	}

	public int getIncomeExpenseTypeId() {
		return incomeExpenseTypeId;
	}

	public void setIncomeExpenseTypeId(int incomeExpenseTypeId) {
		this.incomeExpenseTypeId = incomeExpenseTypeId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
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

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public boolean isPercentage() {
		return isPercentage;
	}

	public void setPercentage(boolean isPercentage) {
		this.isPercentage = isPercentage;
	}

	public boolean isExternal() {
		return isExternal;
	}

	public void setExternal(boolean isExternal) {
		this.isExternal = isExternal;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}


}
