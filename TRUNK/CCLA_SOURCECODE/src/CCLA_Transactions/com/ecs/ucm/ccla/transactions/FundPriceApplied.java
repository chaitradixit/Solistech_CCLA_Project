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

public class FundPriceApplied implements Persistable{

	private int fundPriceAppliedId;
	private int fundPriceId;
	private int dealTypeId;
	private Date priceDate;

	public FundPriceApplied(int fundPriceAppliedId, int fundPriceId, int dealTypeId, Date priceDate)
	{
		this.fundPriceAppliedId = fundPriceAppliedId;
		this.fundPriceId = fundPriceId;
		this.dealTypeId = dealTypeId;
		this.priceDate = priceDate;
	}
	
	public FundPriceApplied(DataBinder binder) throws DataException
	{
		this.setAttributes(binder);
	}
	
	public static FundPriceApplied get(DataResultSet rs) throws DataException
	{
		if (rs.isEmpty())
			return null;
		else
			return new FundPriceApplied(
					DataResultSetUtils.getResultSetIntegerValue(rs, "FUND_PRICE_APPLIED_ID"),
					DataResultSetUtils.getResultSetIntegerValue(rs, "FUND_PRICE_ID"),
					DataResultSetUtils.getResultSetIntegerValue(rs, "DEAL_TYPE_ID"),
					rs.getDateValueByName("FUND_PRICE_DATE")
					);
	}

	public static DataResultSet getData(int fundPriceAppliedId, FWFacade facade) throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addIntParamToBinder(binder, "FUND_PRICE_APPLIED_ID", fundPriceAppliedId);
		return facade.createResultSet("qTransactions_GetFundPriceApplied", binder);
	}
	
	public static DataResultSet getFundPriceAppliedByDate(int fundPriceId, Date priceDate, FWFacade facade) throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addIntParamToBinder(binder, "FUND_PRICE_ID", fundPriceId);
		BinderUtils.addDateParamToBinder(binder, "FUND_PRICE_DATE", priceDate);
		return facade.createResultSet("qTransactions_GetFundPriceAppliedByDate", binder);
	}
	
	public static FundPriceApplied add(int fundPriceId, int dealTypeId, Date priceDate, String username,
			FWFacade facade) 
	throws DataException
	{
		// get new id value
		int fundPriceAppliedId = Integer.parseInt(
				 TransactionUtils.getNewKey("FundPriceApplied", facade));
		
		DataBinder binder = new DataBinder();
		BinderUtils.addIntParamToBinder(binder, "FUND_PRICE_APPLIED_ID", fundPriceAppliedId);
		BinderUtils.addIntParamToBinder(binder, "FUND_PRICE_ID", fundPriceId);
		BinderUtils.addIntParamToBinder(binder, "DEAL_TYPE_ID", dealTypeId);
		BinderUtils.addDateParamToBinder(binder, "FUND_PRICE_DATE", priceDate);
		FundPriceApplied fundPrice = new FundPriceApplied(binder);
		facade.execute("qTransactions_AddFundPriceApplied", binder);
		
		// Add audit record
		DataResultSet afterData = FundPriceApplied.getData(fundPriceAppliedId,facade);

		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(fundPriceAppliedId, TransactionGlobals.SecondaryElementType.FundPriceApplied.toString());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.ADD.toString(), 
		 TransactionGlobals.SecondaryElementType.FundPriceApplied.toString(), 
		 null, afterData, auditRelations);			
		return fundPrice;		
	}
	
	public static boolean hasFundPrice(int fundPriceId, Date priceDate, FWFacade facade) 
	throws DataException
	{
		boolean hasPrice = false;
		DataBinder binder = new DataBinder();
		BinderUtils.addIntParamToBinder(binder, "FUND_PRICE_ID", fundPriceId);
		BinderUtils.addDateParamToBinder(binder, "FUND_PRICE_DATE", priceDate);
		DataResultSet rs = facade.createResultSet("qTransactions_GetFundPriceAppliedByDate", binder);
		if (!rs.isEmpty())
			hasPrice = true;
		return hasPrice;
	}
	
	
	public static FundPriceApplied get(int fundPriceId, Date priceDate,  FWFacade facade) throws DataException
	{
		DataResultSet rs = getFundPriceAppliedByDate(fundPriceId, priceDate,  facade);
		return get(rs);
	}

	public static FundPriceApplied get( FWFacade facade, DataBinder binder) throws DataException
	{
		int fundPriceId = BinderUtils.getBinderIntegerValue(binder, "FUND_PRICE_ID");
		Date priceDate = BinderUtils.getBinderDateValue(binder, "FUND_PRICE_DATE");
		DataResultSet rs = getFundPriceAppliedByDate(fundPriceId, priceDate, facade);
		return get(rs);
	}
	
	public static FundPriceApplied getToday(String fundCode, FWFacade facade) 
	throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addParamToBinder(binder, "FUND_CODE", fundCode);
		DataResultSet rs = facade.createResultSet("qTransactions_GetTodaysFundPriceApplied", binder);
		return get(rs);
		
	}
	
	public static FundPriceApplied getFundPriceAppliedByFundAndDate(String fundCode, Date priceDate, FWFacade facade) 
	throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addParamToBinder(binder, "FUND_CODE", fundCode);
		BinderUtils.addDateParamToBinder(binder, "FUND_PRICE_DATE", priceDate);
		DataResultSet rs = facade.createResultSet("qTransactions_GetFundPriceAppliedByFundCodeAndDate", binder);
		return get(rs);
		
	}
	public static DataResultSet getFundPriceAppliedDetail(int fundPriceAppliedId, FWFacade facade) 
	throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addIntParamToBinder(binder, "FUND_PRICE_APPLIED_ID", fundPriceAppliedId);
		DataResultSet rs = facade.createResultSet("qTransactions_GetFundPriceAppliedDetail", binder);
		if (rs.isEmpty())
			return null;
		else 
			return rs;
		
	}
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		BinderUtils.addDateParamToBinder(binder, "FUND_PRICE_DATE", this.getPriceDate());
		BinderUtils.addIntParamToBinder(binder, "FUND_PRICE_ID", this.getFundPriceId());
		BinderUtils.addIntParamToBinder(binder, "DEAL_TYPE_ID", this.getDealTypeId());
		BinderUtils.addIntParamToBinder(binder, "FUND_PRICE_APPLIED_ID", this.getFundPriceAppliedId());
	}
	
	
	public void persist(FWFacade facade, String username) throws DataException {
		DataResultSet beforeData = FundPriceApplied.getData(this.getFundPriceAppliedId(), facade);

		this.validate(facade);
		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);	
		facade.execute("qTransactions_UpdateFundPriceApplied", binder);
		
		// Add audit record
		DataResultSet afterData = FundPriceApplied.getData(this.getFundPriceAppliedId(),  facade);

		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(this.getFundPriceAppliedId(), TransactionGlobals.SecondaryElementType.FundPriceApplied.toString());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.UPDATE.toString(), 
		 TransactionGlobals.SecondaryElementType.FundPriceApplied.toString(), 
		 beforeData, afterData, auditRelations);	
	}

	public void setAttributes(DataBinder binder) throws DataException {		
		this.setFundPriceId(BinderUtils.getBinderIntegerValue(binder, "FUND_PRICE_ID"));
		this.setDealTypeId(BinderUtils.getBinderIntegerValue(binder, "DEAL_TYPE_ID"));
		this.setPriceDate(BinderUtils.getBinderDateValue(binder, "FUND_PRICE_DATE"));
		this.setFundPriceAppliedId(BinderUtils.getBinderIntegerValue(binder, "FUND_PRICE_APPLIED_ID"));
	}
	
	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub
	}

	public int getFundPriceId() {
		return fundPriceId;
	}

	public void setFundPriceId(int fundPriceId) {
		this.fundPriceId = fundPriceId;
	}

	public Date getPriceDate() {
		return priceDate;
	}

	public void setPriceDate(Date priceDate) {
		this.priceDate = priceDate;
	}

	public int getFundPriceAppliedId() {
		return fundPriceAppliedId;
	}

	public void setFundPriceAppliedId(int fundPriceAppliedId) {
		this.fundPriceAppliedId = fundPriceAppliedId;
	}

	public int getDealTypeId() {
		return dealTypeId;
	}

	public void setDealTypeId(int dealTypeId) {
		this.dealTypeId = dealTypeId;
	}


}
