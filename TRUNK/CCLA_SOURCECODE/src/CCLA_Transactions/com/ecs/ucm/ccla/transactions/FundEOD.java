package com.ecs.ucm.ccla.transactions;

import java.util.Date;
import java.util.HashMap;

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

public class FundEOD implements Persistable{

	///Binder Values
	public static final String EOD_ID = "EOD_ID";
	public static final String FUND_CODE = "FUND_CODE";
	public static final String FUND_PRICE_APPLIED_ID = "FUND_PRICE_APPLIED_ID";
	public static final String FUND_EOD_STATUS_ID = "FUND_EOD_STATUS_ID";
	public static final String FUND_NAV_ID = "FUND_NAV_ID";
	public static final String TOTAL_SHARE_CLASS_EXPENSE = "TOTAL_SHARE_CLASS_EXPENSE";
	public static final String TOTAL_NET_INCOME_DISTRIBUTION = "TOTAL_NET_INCOME_DISTRIBUTION";
	public static final String TOTAL_RATE_EXPENSE = "TOTAL_RATE_EXPENSE";
	public static final String TOTAL_CASH_EXPENSE = "TOTAL_CASH_EXPENSE";
	public static final String RUN_BY = "RUN_BY";
	public static final String RUN_DATE = "RUN_DATE";
	public static final String DATE_ADDED = "DATE_ADDED";
	public static final String RUN_START_DATE = "RUN_START_DATE";
	
	private int eodId;
	private String fundCode;
	private Integer fundPriceAppliedId;
	private int eodStatusId;
	private Integer fundNavId;
	private String totalShareClassExpense;
	private String totalCashExpense;
	private String totalRateExpense;
	private String totalNetIncomeDistribution;
	private String runBy;
	private Date runDate;
	private Date dateAdded;
	private Date runStartDate;

	public FundEOD(int eodId, String fundCode, Integer fundPriceAppliedId, int eodStatusId, Integer fundNavId,  
			String totalShareClassExpense, String totalCashExpense, String totalRateExpense, 
			String totalNetIncomeDistribution, String runBy, Date runDate, Date dateAdded, Date runStartDate)
	{
		this.eodId = eodId;
		this.fundCode = fundCode;
		this.fundPriceAppliedId = fundPriceAppliedId;
		this.eodStatusId = eodStatusId;
		this.fundNavId = fundNavId;
		this.totalShareClassExpense = totalShareClassExpense;
		this.totalCashExpense = totalCashExpense;
		this.totalRateExpense = totalRateExpense;
		this.totalNetIncomeDistribution = totalNetIncomeDistribution;
		this.runBy = runBy;
		this.runDate = runDate;
		this.dateAdded = dateAdded;
		this.runStartDate = runStartDate;
	}
	
	public FundEOD(DataBinder binder) throws DataException
	{
		this.setAttributes(binder);
	}
	
	public static FundEOD get(DataResultSet rs) throws DataException
	{
		if (rs.isEmpty())
			return null;
		else
			return new FundEOD(
					DataResultSetUtils.getResultSetIntegerValue(rs, EOD_ID),
					DataResultSetUtils.getResultSetStringValue(rs, FUND_CODE),
					DataResultSetUtils.getResultSetIntegerValue(rs, FUND_PRICE_APPLIED_ID),
					DataResultSetUtils.getResultSetIntegerValue(rs, FUND_EOD_STATUS_ID),
					DataResultSetUtils.getResultSetIntegerValue(rs, FUND_NAV_ID),
					DataResultSetUtils.getResultSetStringValue(rs, TOTAL_SHARE_CLASS_EXPENSE),
					DataResultSetUtils.getResultSetStringValue(rs, TOTAL_CASH_EXPENSE),
					DataResultSetUtils.getResultSetStringValue(rs, TOTAL_RATE_EXPENSE),
					DataResultSetUtils.getResultSetStringValue(rs, TOTAL_NET_INCOME_DISTRIBUTION),
					DataResultSetUtils.getResultSetStringValue(rs, RUN_BY),					
					rs.getDateValueByName(RUN_DATE),
					rs.getDateValueByName(DATE_ADDED),
					rs.getDateValueByName(RUN_START_DATE)
			);
	}

	public static DataResultSet getData(int eodId, FWFacade facade) throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addIntParamToBinder(binder, EOD_ID, eodId);
		return facade.createResultSet("qTransactions_GetEOD", binder);
	}
	
	public static FundEOD add(String fundCode, Integer fundPriceAppliedId, int eodStatusId, Integer fundNavId,
			String shareClassExpense, String totalCashExpense, String totalRateExpense, String totalNetIncomeDistribution,
			String runBy, Date runDate, Date systemRunDate, Date runStartDate, String username, FWFacade facade) 
	throws DataException
	{
		// get new id value
		int eodId = Integer.parseInt(
				 TransactionUtils.getNewKey("EOD", facade));
		DataBinder binder = new DataBinder();
		BinderUtils.addIntParamToBinder(binder, EOD_ID, eodId);
		BinderUtils.addParamToBinder(binder, FUND_CODE, fundCode);
		BinderUtils.addIntParamToBinder(binder, FUND_PRICE_APPLIED_ID, fundPriceAppliedId);
		BinderUtils.addIntParamToBinder(binder, FUND_EOD_STATUS_ID, eodStatusId);
		BinderUtils.addIntParamToBinder(binder, FUND_NAV_ID, fundNavId);
		BinderUtils.addParamToBinder(binder, TOTAL_SHARE_CLASS_EXPENSE, shareClassExpense);
		BinderUtils.addParamToBinder(binder, TOTAL_RATE_EXPENSE, totalRateExpense);
		BinderUtils.addParamToBinder(binder, TOTAL_CASH_EXPENSE, totalCashExpense);
		BinderUtils.addParamToBinder(binder, TOTAL_NET_INCOME_DISTRIBUTION, totalNetIncomeDistribution);
		BinderUtils.addParamToBinder(binder, RUN_BY, runBy);
		BinderUtils.addDateParamToBinder(binder, RUN_DATE, runDate);
		BinderUtils.addDateParamToBinder(binder, RUN_START_DATE, runStartDate);
		BinderUtils.addDateParamToBinder(binder, DATE_ADDED, systemRunDate);
		FundEOD eod = new FundEOD(binder);
		facade.execute("qTransactions_AddFundEOD", binder);
		
		// Add audit record
		DataResultSet afterData = FundEOD.getData(eodId, facade);
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(eodId, TransactionGlobals.SecondaryElementType.FundEOD.toString());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.ADD.toString(), 
		 TransactionGlobals.SecondaryElementType.FundEOD.toString(), 
		 null, afterData, auditRelations);			
				
		return eod;
		
	}
	
	
	public static FundEOD get(int eodId, FWFacade facade) throws DataException
	{
		DataResultSet rs = getData(eodId, facade);
		return get(rs);
	}
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		BinderUtils.addIntParamToBinder(binder, EOD_ID, this.getEodId());
		BinderUtils.addParamToBinder(binder, FUND_CODE, this.getFundCode());
		BinderUtils.addIntParamToBinder(binder, FUND_PRICE_APPLIED_ID, this.getFundPriceAppliedId());
		BinderUtils.addIntParamToBinder(binder, FUND_EOD_STATUS_ID, this.getEodStatusId());
		BinderUtils.addIntParamToBinder(binder, FUND_NAV_ID, this.getFundNavId());
		BinderUtils.addParamToBinder(binder, TOTAL_SHARE_CLASS_EXPENSE, this.getTotalShareClassExpense());
		BinderUtils.addParamToBinder(binder, TOTAL_CASH_EXPENSE, this.getTotalCashExpense());
		BinderUtils.addParamToBinder(binder, TOTAL_RATE_EXPENSE, this.getTotalRateExpense());
		BinderUtils.addParamToBinder(binder, TOTAL_NET_INCOME_DISTRIBUTION, this.getTotalNetIncomeDistribution());
		BinderUtils.addParamToBinder(binder, RUN_BY, this.getRunBy());
		BinderUtils.addDateParamToBinder(binder, RUN_DATE, this.getRunDate());
		BinderUtils.addDateParamToBinder(binder, RUN_START_DATE, this.getRunStartDate());
		BinderUtils.addDateParamToBinder(binder, DATE_ADDED, this.getDateAdded());
	}
	
	
	public static FundEOD getLatest(String fundCode, FWFacade facade) throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addParamToBinder(binder, FUND_CODE, fundCode);
		DataResultSet rs = facade.createResultSet("qTransactions_GetLatestEOD", binder);
		return get(rs);		
	}
	
	public static FundEOD getLatestByStatus(String fundCode, int statusId, FWFacade facade) throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addParamToBinder(binder, FUND_CODE, fundCode);
		BinderUtils.addIntParamToBinder(binder, "FUND_EOD_STATUS_ID", statusId);
		DataResultSet rs = facade.createResultSet("qTransactions_GetLatestEODByStatus", binder);
		return get(rs);		
	}
	
	
	
	public static FundEOD getLastCompleted(String fundCode, FWFacade facade) throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addParamToBinder(binder, FUND_CODE, fundCode);
		DataResultSet rs = facade.createResultSet("qTransactions_GetLastCompletedEOD", binder);
		return get(rs);		
	}
	
	public static FundEOD getByStatusDate(String fundCode, Date date, int statusId, FWFacade facade) throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addParamToBinder(binder, FUND_CODE, fundCode);
		BinderUtils.addDateParamToBinder(binder, RUN_DATE, date);
		BinderUtils.addIntParamToBinder(binder, FUND_EOD_STATUS_ID, statusId);
		DataResultSet rs = facade.createResultSet("qTransactions_GetEODByStatusDate", binder);
		return get(rs);		
	}
	
	public static FundEOD getByStatusRunDate(String fundCode, Date date, int statusId, FWFacade facade) throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addParamToBinder(binder, FUND_CODE, fundCode);
		BinderUtils.addDateParamToBinder(binder, RUN_DATE, date);
		BinderUtils.addIntParamToBinder(binder, FUND_EOD_STATUS_ID, statusId);
		DataResultSet rs = facade.createResultSet("qTransactions_GetEODByStatusRunDate", binder);
		return get(rs);		
	}	
	
	public void persist(FWFacade facade, String username) throws DataException {
		DataResultSet beforeData = FundEOD.getData(this.getEodId(), facade);
		
		this.validate(facade);
		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);
		facade.execute("qTransactions_UpdateEOD", binder);
		
		// Add audit record
		DataResultSet afterData = FundEOD.getData(this.getEodId(), facade);
		
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(this.getEodId(), TransactionGlobals.SecondaryElementType.FundEOD.toString());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.UPDATE.toString(), 
		 TransactionGlobals.SecondaryElementType.FundEOD.toString(), 
		 beforeData, afterData, auditRelations);	
	}

	
	public void setAttributes(DataBinder binder) throws DataException {		
		this.setEodId(BinderUtils.getBinderIntegerValue(binder, EOD_ID));
		this.setFundCode(BinderUtils.getBinderStringValue(binder, FUND_CODE));
		this.setFundPriceAppliedId(BinderUtils.getBinderIntegerValue(binder, FUND_PRICE_APPLIED_ID));
		this.setEodStatusId(BinderUtils.getBinderIntegerValue(binder, FUND_EOD_STATUS_ID));
		this.setFundNavId(BinderUtils.getBinderIntegerValue(binder, FUND_NAV_ID));
		this.setTotalShareClassExpense(BinderUtils.getBinderStringValue(binder, TOTAL_SHARE_CLASS_EXPENSE));
		this.setTotalCashExpense(BinderUtils.getBinderStringValue(binder, TOTAL_CASH_EXPENSE));
		this.setTotalRateExpense(BinderUtils.getBinderStringValue(binder, TOTAL_RATE_EXPENSE));
		this.setTotalNetIncomeDistribution(BinderUtils.getBinderStringValue(binder, TOTAL_NET_INCOME_DISTRIBUTION));
		this.setRunBy(BinderUtils.getBinderStringValue(binder, RUN_BY));
		this.setRunDate(BinderUtils.getBinderDateValue(binder, RUN_DATE));
		this.setRunStartDate(BinderUtils.getBinderDateValue(binder, RUN_START_DATE));
		this.setDateAdded(BinderUtils.getBinderDateValue(binder, DATE_ADDED));
	}
	
	
	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub
}

	public int getEodId() {
		return eodId;
	}

	public void setEodId(int eodId) {
		this.eodId = eodId;
	}

	public String getFundCode() {
		return fundCode;
	}

	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}

	public Integer getFundPriceAppliedId() {
		return fundPriceAppliedId;
	}

	public void setFundPriceAppliedId(Integer fundPriceAppliedId) {
		this.fundPriceAppliedId = fundPriceAppliedId;
	}

	public int getEodStatusId() {
		return eodStatusId;
	}

	public void setEodStatusId(int eodStatusId) {
		this.eodStatusId = eodStatusId;
	}


	public String getTotalShareClassExpense() {
		return totalShareClassExpense;
	}

	public void setTotalShareClassExpense(String totalShareClassExpense) {
		this.totalShareClassExpense = totalShareClassExpense;
	}

	public String getRunBy() {
		return runBy;
	}

	public void setRunBy(String runBy) {
		this.runBy = runBy;
	}

	public Date getRunDate() {
		return runDate;
	}

	public void setRunDate(Date runDate) {
		this.runDate = runDate;
	}

	public Integer getFundNavId() {
		return fundNavId;
	}

	public void setFundNavId(Integer fundNavId) {
		this.fundNavId = fundNavId;
	}

	public void setTotalCashExpense(String totalCashExpense) {
		this.totalCashExpense = totalCashExpense;
	}

	public String getTotalCashExpense() {
		return totalCashExpense;
	}

	public void setTotalRateExpense(String totalRateExpense) {
		this.totalRateExpense = totalRateExpense;
	}

	public String getTotalRateExpense() {
		return totalRateExpense;
	}

	public void setTotalNetIncomeDistribution(String totalNetIncomeDistribution) {
		this.totalNetIncomeDistribution = totalNetIncomeDistribution;
	}

	public String getTotalNetIncomeDistribution() {
		return totalNetIncomeDistribution;
	}
	public Date getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}

	public Date getRunStartDate() {
		return runStartDate;
	}

	public void setRunStartDate(Date runStartDate) {
		this.runStartDate = runStartDate;
	}


}
