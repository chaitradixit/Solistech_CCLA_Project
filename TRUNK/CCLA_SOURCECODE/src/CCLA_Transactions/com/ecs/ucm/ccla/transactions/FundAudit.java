package com.ecs.ucm.ccla.transactions;

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Audit;
import com.ecs.ucm.ccla.data.Globals;
import com.ecs.ucm.ccla.data.Persistable;
import com.ecs.ucm.ccla.transactions.globals.TransactionGlobals;
import com.ecs.ucm.ccla.transactions.utils.TransactionUtils;

import com.ecs.utils.stellent.embedded.FWFacade;

public class FundAudit implements Persistable{

	private int fundAuditId;
	private String fundCode;
	private int shareClassId;
	private String totalExpense;
	public String getTotalExpense() {
		return totalExpense;
	}

	public void setTotalExpense(String totalExpense) {
		this.totalExpense = totalExpense;
	}

	private Date fundAuditDate;	
	private String incomePerUnit;
	private String openingUnits;
	private String closingUnits;
	private String grossYield;
	private String fundYield;
	private String netYield;


	public FundAudit(int fundAuditId, String fundCode, int shareClassId, String totalExpense, Date fundAuditDate,  
			String incomePerUnit, String openingUnits, String closingUnits, String grossYield, String fundYield, String netYield)
	{
		this.fundAuditId = fundAuditId;
		this.fundCode = fundCode;
		this.shareClassId = shareClassId;
		this.totalExpense = totalExpense;
		this.fundAuditDate = fundAuditDate;
		this.incomePerUnit = incomePerUnit;
		this.openingUnits = openingUnits;
		this.closingUnits = closingUnits;
		this.grossYield = grossYield;
		this.fundYield = fundYield;
		this.netYield = netYield;
	}
	
	public FundAudit(DataBinder binder) throws DataException
	{
		this.setAttributes(binder);
	}
	
	public static FundAudit get(DataResultSet rs) throws DataException
	{
		if (rs.isEmpty())
			return null;
		else
			return new FundAudit(
					CCLAUtils.getResultSetIntegerValue(rs, "FUND_AUDIT_ID"),
					CCLAUtils.getResultSetStringValue(rs, "FUND_CODE"),
					CCLAUtils.getResultSetIntegerValue(rs, "SHARE_CLASS_ID"),
					CCLAUtils.getResultSetStringValue(rs, "TOTAL_EXPENSE"),
					rs.getDateValueByName("FUND_AUDIT_DATE"),
					CCLAUtils.getResultSetStringValue(rs, "INCOME_PER_UNIT"),
					CCLAUtils.getResultSetStringValue(rs, "OPENING_UNITS"),
					CCLAUtils.getResultSetStringValue(rs, "CLOSING_UNITS"),
					CCLAUtils.getResultSetStringValue(rs, "GROSS_YIELD"),
					CCLAUtils.getResultSetStringValue(rs, "FUND_YIELD"),
					CCLAUtils.getResultSetStringValue(rs, "NET_YIELD")
			);
	}

	public static DataResultSet getData(int fundAuditId, FWFacade facade) throws DataException
	{
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, "FUND_AUDIT_ID", fundAuditId);
		return facade.createResultSet("qTransactions_GetFundAuditById", binder);
	}
	
	public static FundAudit add(String fundCode, int shareClassId, String totalExpense,
			Date fundAuditDate, String incomePerUnit, String openingUnits, String closingUnits, 
			String grossYield, String fundYield, String netYield,
			String username, FWFacade facade) 
	throws DataException
	{
		// get new id value
		int fundAuditId = Integer.parseInt(
				 TransactionUtils.getNewKey("FundAudit", facade));
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, "FUND_AUDIT_ID", fundAuditId);
		CCLAUtils.addQueryParamToBinder(binder, "FUND_CODE", fundCode);
		CCLAUtils.addQueryIntParamToBinder(binder, "SHARE_CLASS_ID", shareClassId);
		CCLAUtils.addQueryParamToBinder(binder, "TOTAL_EXPENSE", totalExpense);
		CCLAUtils.addQueryDateParamToBinder(binder, "FUND_AUDIT_DATE", fundAuditDate);
		CCLAUtils.addQueryParamToBinder(binder, "INCOME_PER_UNIT", incomePerUnit);
		CCLAUtils.addQueryParamToBinder(binder, "OPENING_UNITS", openingUnits);
		CCLAUtils.addQueryParamToBinder(binder, "CLOSING_UNITS", closingUnits);
		CCLAUtils.addQueryParamToBinder(binder, "GROSS_YIELD", grossYield);
		CCLAUtils.addQueryParamToBinder(binder, "FUND_YIELD", fundYield);
		CCLAUtils.addQueryParamToBinder(binder, "NET_YIELD", netYield);
		FundAudit fundaudit = new FundAudit(binder);
		facade.execute("qTransactions_AddFundAudit", binder);
		
		// Add audit record
		DataResultSet afterData = FundAudit.getData(fundAuditId, facade);
		
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(fundAuditId, TransactionGlobals.SecondaryElementType.FundAudit.toString());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.UPDATE.toString(), 
		 TransactionGlobals.SecondaryElementType.FundAudit.toString(), 
		 null, afterData, auditRelations);			
		
		
		return fundaudit;
		
	}
	
	public static boolean hasFundAudit(int shareClassId, Date auditDate, FWFacade facade) 
	throws DataException
	{
		boolean hasAudit = false;
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, "SHARE_CLASS_ID", shareClassId);
		CCLAUtils.addQueryDateParamToBinder(binder, "FUND_AUDIT_DATE", auditDate);
		DataResultSet rs = facade.createResultSet("qTransactions_GetFundAuditByDate", binder);
		if (!rs.isEmpty())
			hasAudit = true;
		return hasAudit;
	}
	
	public static FundAudit get(int shareClassId, Date auditDate, FWFacade facade) 
	throws DataException
	{
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, "SHARE_CLASS_ID", shareClassId);
		CCLAUtils.addQueryDateParamToBinder(binder, "FUND_AUDIT_DATE", auditDate);
		DataResultSet rs = facade.createResultSet("qTransactions_GetFundAuditByDate", binder);
		if (!rs.isEmpty())
			return get(rs);
		else
			return null;
	}
	
	/**
	 * Gets a vector of fund audits for a fund for today's date 
	 * 
	 * @param fundCode
	 * @param auditDate
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Vector<FundAudit> getTodaysFundAuditold(String fundCode,  FWFacade facade) 
	throws DataException
	{
		Vector<FundAudit> fundAudits = new Vector();
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryParamToBinder(binder, "FUND_CODE", fundCode);
		DataResultSet rs = facade.createResultSet("qTransactions_GetTodaysFundAuditByFund", binder);
		if (!rs.isEmpty())
			{
				do {
					FundAudit fa = get(rs);
					fundAudits.add(fa);
				} while (rs.next());
				return fundAudits;
			}
		else
			return null;
	}

	/**
	 * Gets a vector of fund audits for a fund for today's date 
	 * 
	 * @param fundCode
	 * @param auditDate
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Vector<FundAudit> getFundAuditByDate(String fundCode,  Date fundAuditDate, FWFacade facade) 
	throws DataException
	{
		Vector<FundAudit> fundAudits = new Vector();
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryParamToBinder(binder, "FUND_CODE", fundCode);
		CCLAUtils.addQueryDateParamToBinder(binder, "FUND_AUDIT_DATE", fundAuditDate);
		DataResultSet rs = facade.createResultSet("qTransactions_GetFundAuditDetailByDate", binder);
		if (!rs.isEmpty())
			{
				do {
					FundAudit fa = get(rs);
					fundAudits.add(fa);
				} while (rs.next());
				return fundAudits;
			}
		else
			return null;
	}

	
	
	/**
	 * Gets a vector of fund audits for a fund for today's date 
	 * 
	 * @param fundCode
	 * @param auditDate
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getFundAuditByDateRS(String fundCode, Date fundAuditDate, FWFacade facade) 
	throws DataException
	{
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryParamToBinder(binder, "FUND_CODE", fundCode);
		CCLAUtils.addQueryDateParamToBinder(binder, "FUND_AUDIT_DATE", fundAuditDate);
		DataResultSet rs = facade.createResultSet("qTransactions_GetFundAuditDetailByDate", binder);
		if (!rs.isEmpty())
			{
				return rs;
			}
		else
			return null;
	}	
	

	
	/**
	 * Gets a vector of fund audits for a fund for today's date 
	 * 
	 * @param fundCode
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static String getTotalClosingUnits(String fundCode, Date fundAuditDate,  FWFacade facade) 
	throws DataException
	{
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryParamToBinder(binder, "FUND_CODE", fundCode);
		CCLAUtils.addQueryDateParamToBinder(binder, "FUND_AUDIT_DATE", fundAuditDate);
		DataResultSet rs = facade.createResultSet("qTransactions_GetTotalClosingUnitsByFund", binder);
		if (!rs.isEmpty())
			{
				return rs.getStringValueByName("UNIT_TOTAL");
			}
		else
			return null;
	}		
	
	public static FundAudit get(int fundAuditId, FWFacade facade) throws DataException
	{
		DataResultSet rs = getData(fundAuditId, facade);
		return get(rs);
	}
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		CCLAUtils.addQueryIntParamToBinder(binder, "FUND_AUDIT_ID", this.getFundAuditId());
		CCLAUtils.addQueryParamToBinder(binder, "FUND_CODE", this.getFundCode());
		CCLAUtils.addQueryIntParamToBinder(binder, "SHARE_CLASS_ID", this.getShareClassId());
		CCLAUtils.addQueryParamToBinder(binder, "TOTAL_EXPENSE", this.getTotalExpense());
		CCLAUtils.addQueryDateParamToBinder(binder, "FUND_AUDIT_DATE", this.getFundAuditDate());
		CCLAUtils.addQueryParamToBinder(binder, "INCOME_PER_UNIT", this.getIncomePerUnit());
		CCLAUtils.addQueryParamToBinder(binder, "OPENING_UNITS", this.getOpeningUnits());
		CCLAUtils.addQueryParamToBinder(binder, "CLOSING_UNITS", this.getClosingUnits());
		CCLAUtils.addQueryParamToBinder(binder, "GROSS_YIELD", this.getGrossYield());
		CCLAUtils.addQueryParamToBinder(binder, "FUND_YIELD", this.getFundYield());
		CCLAUtils.addQueryParamToBinder(binder, "NET_YIELD", this.getNetYield());

	}
	
	
	public void persist(FWFacade facade, String username) throws DataException {
		DataResultSet beforeData = FundAudit.getData(this.getFundAuditId(), facade);
		
		this.validate(facade);
		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);
		facade.execute("qTransactions_UpdateFundAudit", binder);
		
		// Add audit record
		DataResultSet afterData = FundAudit.getData(this.getFundAuditId(), facade);
		
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(fundAuditId, TransactionGlobals.SecondaryElementType.FundAudit.toString());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.UPDATE.toString(), 
		 TransactionGlobals.SecondaryElementType.FundAudit.toString(), 
		 beforeData, afterData, auditRelations);	
	}

	
	public void setAttributes(DataBinder binder) throws DataException {		
		this.setFundAuditId(CCLAUtils.getBinderIntegerValue(binder, "FUND_AUDIT_ID"));
		this.setFundCode(CCLAUtils.getBinderStringValue(binder, "FUND_CODE"));
		this.setShareClassId(CCLAUtils.getBinderIntegerValue(binder, "SHARE_CLASS_ID"));
		this.setTotalExpense(CCLAUtils.getBinderStringValue(binder, "TOTAL_EXPENSE"));
		this.setFundAuditDate(CCLAUtils.getBinderDateValue(binder, "FUND_AUDIT_DATE"));
		this.setIncomePerUnit(CCLAUtils.getBinderStringValue(binder, "INCOME_PER_UNIT"));
		this.setOpeningUnits(CCLAUtils.getBinderStringValue(binder, "OPENING_UNITS"));
		this.setClosingUnits(CCLAUtils.getBinderStringValue(binder, "CLOSING_UNITS"));	
		this.setGrossYield(CCLAUtils.getBinderStringValue(binder, "GROSS_YIELD"));	
		this.setFundYield(CCLAUtils.getBinderStringValue(binder, "FUND_YIELD"));	
		this.setNetYield(CCLAUtils.getBinderStringValue(binder, "NET_YIELD"));	
	}
	
	
	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub
}

	public int getFundAuditId() {
		return fundAuditId;
	}

	public void setFundAuditId(int fundAuditId) {
		this.fundAuditId = fundAuditId;
	}

	public String getFundCode() {
		return fundCode;
	}

	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}

	public int getShareClassId() {
		return shareClassId;
	}

	public void setShareClassId(int shareClassId) {
		this.shareClassId = shareClassId;
	}



	public Date getFundAuditDate() {
		return fundAuditDate;
	}

	public void setFundAuditDate(Date fundAuditDate) {
		this.fundAuditDate = fundAuditDate;
	}

	public String getIncomePerUnit() {
		return incomePerUnit;
	}

	public void setIncomePerUnit(String incomePerUnit) {
		this.incomePerUnit = incomePerUnit;
	}

	public String getOpeningUnits() {
		return openingUnits;
	}

	public void setOpeningUnits(String openingUnits) {
		this.openingUnits = openingUnits;
	}

	public String getClosingUnits() {
		return closingUnits;
	}

	public void setClosingUnits(String closingUnits) {
		this.closingUnits = closingUnits;
	}
	
	public String getGrossYield() {
		return grossYield;
	}

	public void setGrossYield(String grossYield) {
		this.grossYield = grossYield;
	}

	public String getFundYield() {
		return fundYield;
	}

	public void setFundYield(String fundYield) {
		this.fundYield = fundYield;
	}

	public String getNetYield() {
		return netYield;
	}

	public void setNetYield(String netYield) {
		this.netYield = netYield;
	}
}
