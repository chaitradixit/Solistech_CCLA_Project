package com.ecs.ucm.ccla.transactions;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
 
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Persistable;
import com.ecs.ucm.ccla.transactions.utils.TransactionUtils;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.DataResultSetUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

public class IncomeExpenseApplied implements Persistable{

	private int incomeExpenseAppliedId;
	private int incomeExpenseId;
	private String eodId;
	private Date incomeExpenseDate;
	private String incomeExpenseValue;
	private String addedBy;
	
	public IncomeExpenseApplied(int incomeExpenseAppliedId, int incomeExpenseId, String eodId,
			Date incomeExpenseDate, String incomeExpenseValue, String addedBy)
	{
		this.incomeExpenseAppliedId = incomeExpenseAppliedId;
		this.incomeExpenseId = incomeExpenseId;
		this.eodId = eodId;
		this.incomeExpenseDate = incomeExpenseDate;
		this.incomeExpenseValue = incomeExpenseValue;
		this.addedBy = addedBy;
	}
	
	public IncomeExpenseApplied(DataBinder binder) throws DataException
	{
		this.setAttributes(binder);
	}
	
	public static IncomeExpenseApplied get(DataResultSet rs) throws DataException
	{
		if (rs.isEmpty())
			return null;
		else
			return new IncomeExpenseApplied(DataResultSetUtils.getResultSetIntegerValue(rs, "INCOME_EXPENSE_APPLIED_ID"),
					DataResultSetUtils.getResultSetIntegerValue(rs, "INCOME_EXPENSE_ID"),
					DataResultSetUtils.getResultSetStringValue(rs, "EOD_ID"),
					rs.getDateValueByName("INCOME_EXPENSE_DATE"),
					DataResultSetUtils.getResultSetStringValue(rs, "INCOME_EXPENSE_VALUE"),
					DataResultSetUtils.getResultSetStringValue(rs, "ADDED_BY"));
		
	}

	public static DataResultSet getData(int incomeExpenseAppliedId, FWFacade facade) throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addIntParamToBinder(binder, "INCOME_EXPENSE_APPLIED_ID", incomeExpenseAppliedId);
		return facade.createResultSet("qTransactions_GetIncomeExpenseApplied", binder);
	}
	
	public static IncomeExpenseApplied get(int incomeExpenseAppliedId, FWFacade facade) throws DataException
	{
		DataResultSet rs = getData(incomeExpenseAppliedId, facade);
		return get(rs);
	}
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		BinderUtils.addIntParamToBinder(binder, "INCOME_EXPENSE_APPLIED_ID", this.getIncomeExpenseAppliedId());
		BinderUtils.addIntParamToBinder(binder, "INCOME_EXPENSE_ID", this.getIncomeExpenseId());
		BinderUtils.addParamToBinder(binder, "EOD_ID", this.getEodId());
		BinderUtils.addDateParamToBinder(binder, "INCOME_EXPENSE_DATE", this.getIncomeExpenseDate());
		BinderUtils.addParamToBinder(binder, "INCOME_EXPENSE_VALUE", this.getIncomeExpenseValue());
		BinderUtils.addParamToBinder(binder, "ADDED_BY", this.getAddedBy());		
	}
	
	public static IncomeExpenseApplied getByDateFundType (String fundCode, int incomeTypeId, Date incomeDate, FWFacade facade) 
	throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addDateParamToBinder(binder, "INCOME_EXPENSE_DATE", incomeDate);
		BinderUtils.addIntParamToBinder(binder, "INCOME_EXPENSE_TYPE_ID", incomeTypeId);
		BinderUtils.addParamToBinder(binder, "FUND_CODE", fundCode);
		DataResultSet rsIncome = facade.createResultSet("qTransactions_GetIncomeAppliedByDateAndFund", binder);		
		return get(rsIncome);
		
	}
	
	public static IncomeExpenseApplied add(DataBinder binder, FWFacade facade, String username) throws NumberFormatException, DataException
	{
		// get new id value
		int expenseAppliedId = Integer.parseInt(
				 TransactionUtils.getNewKey("IncomeExpenseApplied", facade));
		Log.debug("new key expenseAppliedId is :" + expenseAppliedId);
		IncomeExpenseApplied applied = new IncomeExpenseApplied(binder);		
		applied.setIncomeExpenseAppliedId(expenseAppliedId);
		applied.validate(facade);
		applied.addFieldsToBinder(binder);		
		facade.execute("qTransactions_InsertIncomeExpenseApplied", binder);		
		//TODO AUDITING		
		return applied;
		
	}
	
	public static IncomeExpenseApplied add(int incomeExpenseId, Date incomeExpenseDate, String incomeExpenseValue, String eodId,
			String username, FWFacade facade) throws DataException
	{
		DataBinder binder = new DataBinder();
		// if already exists for id and date then update, otherwise add
		IncomeExpenseApplied iea = getIncomeExpenseAppliedByDate(incomeExpenseId, incomeExpenseDate, facade);
		if (iea != null)
		{
			iea.setIncomeExpenseValue(incomeExpenseValue);
			iea.setAddedBy(username);
			iea.persist(facade, username);
		} else 
		{
			int expenseAppliedId = Integer.parseInt(TransactionUtils.getNewKey("IncomeExpenseApplied", facade));
			iea = new IncomeExpenseApplied(expenseAppliedId, incomeExpenseId, eodId,
					incomeExpenseDate, incomeExpenseValue, username);
			iea.addFieldsToBinder(binder);
			facade.execute("qTransactions_InsertIncomeExpenseApplied", binder);
		}
		return iea;
	}
	
	public void persist(FWFacade facade, String username) throws DataException {
		this.validate(facade);
		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);
		facade.execute("qTransactions_UpdateIncomeExpenseApplied", binder);
	}
	
	public void setAttributes(DataBinder binder) throws DataException {	
		this.setIncomeExpenseId(BinderUtils.getBinderIntegerValue(binder, "INCOME_EXPENSE_ID"));
		this.setEodId(BinderUtils.getBinderStringValue(binder, "EOD_ID"));
		this.setIncomeExpenseDate(BinderUtils.getBinderDateValue(binder, "INCOME_EXPENSE_DATE"));
		this.setIncomeExpenseValue(BinderUtils.getBinderStringValue(binder, "INCOME_EXPENSE_VALUE"));
		this.setAddedBy(BinderUtils.getBinderStringValue(binder, "ADDED_BY"));		
	}
	
	public static void remove(int expenseAppliedId, FWFacade facade) throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addIntParamToBinder(binder, "INCOME_EXPENSE_APPLIED_ID", expenseAppliedId);
		facade.execute("qTransactions_DeleteIncomeExpenseApplied", binder);		
	}

	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub		
	}

	public static IncomeExpenseApplied getIncomeExpenseAppliedByDate(int expenseId, Date expenseDate, FWFacade facade) 
	throws DataException
	{

	DataResultSet rs = getIncomeExpenseAppliedByDateRS(expenseId, expenseDate, facade) ;
	
	if (rs == null || rs.isEmpty())
		return null;
	else
		return get(rs);
	}
	
	
	
	public static DataResultSet getIncomeExpenseAppliedByDateRS(int expenseId, Date expenseDate, FWFacade facade) 
	throws DataException
	{
	DataBinder binder = new DataBinder();
	BinderUtils.addIntParamToBinder(binder, "INCOME_EXPENSE_ID", expenseId);
	BinderUtils.addDateParamToBinder(binder, "INCOME_EXPENSE_DATE", expenseDate);
	DataResultSet rs = facade.createResultSet("qTransactions_GetIncomeExpenseAppliedByDate", binder);
	
	if (rs.isEmpty())
		return null;
	else
		return rs;
	}

	
	public static IncomeExpenseApplied getIncomeAppliedByDate(String fundCode, int incomeTypeId, Date expenseDate, FWFacade facade) 
	throws DataException
	{

	DataResultSet rs = getIncomeAppliedByDateRS(fundCode, incomeTypeId, expenseDate, facade) ;
	
	if (rs == null || rs.isEmpty())
		return null;
	else
		return get(rs);
	}
	
	
	
	public static DataResultSet getIncomeAppliedByDateRS(String fundCode, int incomeTypeId,  Date expenseDate, FWFacade facade) 
	throws DataException
	{
	DataBinder binder = new DataBinder();
	Log.debug("getting income applied for " + expenseDate);
	BinderUtils.addDateParamToBinder(binder, "INCOME_EXPENSE_DATE", expenseDate);
	BinderUtils.addParamToBinder(binder, "FUND_CODE", fundCode);
	BinderUtils.addIntParamToBinder(binder, "INCOME_EXPENSE_TYPE_ID", incomeTypeId);
	DataResultSet rs = facade.createResultSet("qTransactions_GetIncomeAppliedForDate", binder);
	
	if (rs.isEmpty())
		return null;
	else
		return rs;
	}
	
	
	
	/** Gets the total fund expenses of a particular income expense type
	 * 
	 * @param expenseTypeId
	 * @param expenseDate
	 * @param fundCode
	 * @param facade
	 * @return String - total of general (ie not share specific) expenses for day
	 * @throws DataException
	 */
	public static String getTotalExpenseAppliedByDate(int expenseTypeId, Date expenseDate, String fundCode, FWFacade facade) 
	throws DataException
	{
	DataBinder binder = new DataBinder();
	BinderUtils.addIntParamToBinder(binder, "INCOME_EXPENSE_TYPE_ID", expenseTypeId);
	BinderUtils.addDateParamToBinder(binder, "INCOME_EXPENSE_DATE", expenseDate);
	BinderUtils.addParamToBinder(binder, "FUND_CODE", fundCode);
	DataResultSet rs = facade.createResultSet("qTransactions_GetTotalFundExpenseByDate", binder);
	
	if (rs.isEmpty() || StringUtils.stringIsBlank(rs.getStringValueByName("FUND_EXPENSE_TOTAL")))
		return "0";
	else
		return rs.getStringValueByName("FUND_EXPENSE_TOTAL");
	}


	/** Gets the total fund income of a particular income expense type
	 * 
	 * @param expenseTypeId
	 * @param expenseDate
	 * @param fundCode
	 * @param facade
	 * @return String - total the income (of the type passed) for day
	 * @throws DataException
	 */
	public static String getTotalIncomeAppliedByDate(int expenseTypeId, Date expenseDate, String fundCode, FWFacade facade) 
	throws DataException
	{
	DataBinder binder = new DataBinder();
	BinderUtils.addIntParamToBinder(binder, "INCOME_EXPENSE_TYPE_ID", expenseTypeId);
	BinderUtils.addDateParamToBinder(binder, "INCOME_EXPENSE_DATE", expenseDate);
	BinderUtils.addParamToBinder(binder, "FUND_CODE", fundCode);
	DataResultSet rs = facade.createResultSet("qTransactions_GetTotalFundIncomeByDate", binder);
	
	if (rs.isEmpty())
		return "0";
	else
		return rs.getStringValueByName("FUND_EXPENSE_TOTAL");
	}
	
	/** Gets the income applied objects for a particular date/fund code as a vector
	 * 
	 * @param expenseTypeId
	 * @param expenseDate
	 * @param fundCode
	 * @param facade
	 * @return String - total the income (of the type passed) for day
	 * @throws DataException
	 */
	public static Vector<IncomeExpenseApplied> getIncomeAppliedByDate(int expenseTypeId, Date expenseDate, String fundCode, FWFacade facade) 
	throws DataException
	{
	DataBinder binder = new DataBinder();
	Vector<IncomeExpenseApplied> iea = new Vector();
	BinderUtils.addIntParamToBinder(binder, "INCOME_EXPENSE_TYPE_ID", expenseTypeId);
	BinderUtils.addDateParamToBinder(binder, "INCOME_EXPENSE_DATE", expenseDate);
	BinderUtils.addParamToBinder(binder, "FUND_CODE", fundCode);
	DataResultSet rs = facade.createResultSet("qTransactions_GetIncomeByDate", binder);
	
	if (!rs.isEmpty())
		{
			do {
				IncomeExpenseApplied thisIEA = IncomeExpenseApplied.get(rs);
				iea.add(thisIEA);
			} while (rs.next());
		}
			return iea;
	}

	/** Gets the total fund expense of a particular income expense type for a share class id on a given date
	 * 
	 * @param expenseTypeId
	 * @param expenseDate
	 * @param shareClassId
	 * @param facade
	 * @return String - total the income (of the type passed) for day
	 * @throws DataException
	 */
	public static String getTotalCashShareClassExpenseAppliedByDate(int expenseTypeId, int shareClassId,  Date expenseDate,  FWFacade facade) 
	throws DataException
	{
	DataBinder binder = new DataBinder();
	BinderUtils.addIntParamToBinder(binder, "INCOME_EXPENSE_TYPE_ID", expenseTypeId);
	Log.debug("PASSING INCOME_EXPENSE_TYPE_ID:" + expenseTypeId);
	BinderUtils.addDateParamToBinder(binder, "INCOME_EXPENSE_DATE", expenseDate);
	Log.debug("PASSING INCOME_EXPENSE_DATE:" + expenseDate);
	BinderUtils.addIntParamToBinder(binder, "SHARE_CLASS_ID", shareClassId);
	Log.debug("PASSING SHARE_CLASS_ID:" + shareClassId);
	DataResultSet rs = facade.createResultSet("qTransactions_GetTotalCashShareClassExpenseByDate", binder);
	Log.debug("rs.isempty is " + rs.isEmpty());
	if (rs.isEmpty())
		return "0";
	else
	{
		String expenseTotal = "";
		do
		{
			expenseTotal = rs.getStringValueByName("SHARE_CLASS_EXPENSE_TOTAL");
		} while (rs.next());
		return expenseTotal;
	}
	}	

	/** Gets the total fund expense of expenses that are measured in percentages 
	 * of a particular income expense type for a share class id on a given date
	 * 
	 * @param expenseTypeId
	 * @param expenseDate
	 * @param shareClassId
	 * @param facade
	 * @return String - total the income (of the type passed) for day
	 * @throws DataException
	 */
	public static String getTotalRateShareClassExpenseAppliedByDate(int expenseTypeId, int shareClassId,  Date expenseDate,  FWFacade facade) 
	throws DataException
	{
	DataBinder binder = new DataBinder();
	BinderUtils.addIntParamToBinder(binder, "INCOME_EXPENSE_TYPE_ID", expenseTypeId);
	Log.debug("PASSING INCOME_EXPENSE_TYPE_ID:" + expenseTypeId);
	BinderUtils.addDateParamToBinder(binder, "INCOME_EXPENSE_DATE", expenseDate);
	Log.debug("PASSING INCOME_EXPENSE_DATE:" + expenseDate);
	BinderUtils.addIntParamToBinder(binder, "SHARE_CLASS_ID", shareClassId);
	Log.debug("PASSING SHARE_CLASS_ID:" + shareClassId);
	DataResultSet rs = facade.createResultSet("qTransactions_GetTotalRateShareClassExpenseByDate", binder);
	Log.debug("rs.isempty is " + rs.isEmpty());
	if (rs.isEmpty())
		return "0";
	else
	{
		String expenseTotal = "0";
		do
		{
			expenseTotal = rs.getStringValueByName("SHARE_CLASS_EXPENSE_TOTAL");
		} while (rs.next());
		return expenseTotal;
	}
	}		
	
	public int getIncomeExpenseAppliedId() {
		return incomeExpenseAppliedId;
	}
	
	
	
	public void setIncomeExpenseAppliedId(int incomeExpenseAppliedId) {
		this.incomeExpenseAppliedId = incomeExpenseAppliedId;
	}

	public int getIncomeExpenseId() {
		return incomeExpenseId;
	}

	public void setIncomeExpenseId(int incomeExpenseId) {
		this.incomeExpenseId = incomeExpenseId;
	}

	public Date getIncomeExpenseDate() {
		return incomeExpenseDate;
	}

	public void setIncomeExpenseDate(Date incomeExpenseDate) {
		this.incomeExpenseDate = incomeExpenseDate;
	}

	public String getIncomeExpenseValue() {
		return incomeExpenseValue;
	}

	public void setIncomeExpenseValue(String incomeExpenseValue) {
		this.incomeExpenseValue = incomeExpenseValue;
	}

	public String getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(String addedBy) {
		this.addedBy = addedBy;
	}

	public String getEodId() {
		return eodId;
	}

	public void setEodId(String eodId) {
		this.eodId = eodId;
	}





}
