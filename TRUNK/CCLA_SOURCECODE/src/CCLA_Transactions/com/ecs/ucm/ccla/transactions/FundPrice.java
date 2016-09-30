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

public class FundPrice implements Persistable{

	private int fundPriceId;
	private String fundCode;
	private String bid;
	private String basic;
	private String offer;
	private String addedBy;
	private Date addedDate;

	public FundPrice(int fundPriceId, String fundCode,  String bid, String basic, String offer,
			 String addedBy, Date addedDate)
	{
		this.fundPriceId = fundPriceId;
		this.fundCode = fundCode;
		this.bid = bid;
		this.basic = basic;
		this.offer = offer;
		this.addedBy = addedBy;
		this.addedDate = addedDate;
	}
	
	public FundPrice(DataBinder binder) throws DataException
	{
		this.setAttributes(binder);
	}
	
	public static FundPrice get(DataResultSet rs) throws DataException
	{
		if (rs.isEmpty())
			return null;
		else
			return new FundPrice(
					DataResultSetUtils.getResultSetIntegerValue(rs, "FUND_PRICE_ID"),
					DataResultSetUtils.getResultSetStringValue(rs, "FUND_CODE"),					
					DataResultSetUtils.getResultSetStringValue(rs, "BID"),	
					DataResultSetUtils.getResultSetStringValue(rs, "BASIC"),	
					DataResultSetUtils.getResultSetStringValue(rs, "OFFER"),	
					DataResultSetUtils.getResultSetStringValue(rs, "ADDED_BY"),
					rs.getDateValueByName("DATE_ADDED")
					);
	}

	public static FundPrice get(int fundPriceId, FWFacade facade) 
	throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addIntParamToBinder(binder, "FUND_PRICE_ID", fundPriceId);
		DataResultSet rs = facade.createResultSet("qTransactions_GetFundPriceById", binder);
		return get(rs);
	}
	
	public static DataResultSet getData(String fundCode, FWFacade facade) throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addParamToBinder(binder, "FUND_CODE", fundCode);
		return facade.createResultSet("qTransactions_GetLatestFundPrice", binder);
	}

	/** Gets the daily unit price for a fund - this is defined as the latest entry in the FUND_PRICE
	 * table for the current date
	 * 
	 * @param fundCode
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getDailyData(String fundCode, FWFacade facade) throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addParamToBinder(binder, "FUND_CODE", fundCode);
		return facade.createResultSet("qTransactions_GetDailyFundPrice", binder);
	}	
	
	public static FundPrice add(String fundCode, Date dateAdded, 
			String bid, String basic, String offer, String addedBy,
			FWFacade facade) 
	throws DataException
	{
		// get new id value
		int fundPriceId = Integer.parseInt(
				 TransactionUtils.getNewKey("FundPrice", facade));
		
		DataBinder binder = new DataBinder();
		BinderUtils.addParamToBinder(binder, "FUND_CODE", fundCode);
		BinderUtils.addDateParamToBinder(binder, "DATE_ADDED", dateAdded);
		BinderUtils.addParamToBinder(binder, "BID", bid);
		BinderUtils.addParamToBinder(binder, "BASIC", basic);
		BinderUtils.addParamToBinder(binder, "OFFER", offer);
		BinderUtils.addParamToBinder(binder, "ADDED_BY", addedBy);
		BinderUtils.addIntParamToBinder(binder, "FUND_PRICE_ID", fundPriceId);

		FundPrice fundPrice = new FundPrice(binder);
		fundPrice.setFundPriceId(fundPriceId);
		facade.execute("qTransactions_AddFundPrice", binder);
		
		// Add audit record
		DataResultSet afterData = FundPrice.getData(fundCode, facade);

		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(fundPriceId, TransactionGlobals.SecondaryElementType.FundPrice.toString());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, addedBy, 
		 Globals.AuditActions.ADD.toString(), 
		 TransactionGlobals.SecondaryElementType.FundPrice.toString(), 
		 null, afterData, auditRelations);			
		return fundPrice;		
	}
	
	public static boolean hasFundPrice(String fundCode, Date dateAdded, FWFacade facade) 
	throws DataException
	{
		boolean hasPrice = false;
		DataBinder binder = new DataBinder();
		BinderUtils.addParamToBinder(binder, "FUND_CODE", fundCode);
		BinderUtils.addDateParamToBinder(binder, "DATE_ADDED", dateAdded);
		DataResultSet rs = facade.createResultSet("qTransactions_GetFundPriceByDate", binder);
		if (!rs.isEmpty())
			hasPrice = true;
		return hasPrice;
	}
/**
 * Returns the price for a fund for SOURCE ACCOUNT
 * 
 * 	
 * @param fundPriceId
 * @param dataTypeId
 * @param facade
 * @return
 * @throws DataException
 */
	public static String getSourcePrice(int fundPriceId, int dataTypeId, FWFacade facade) 
		throws DataException
	{
		FundPrice fp = FundPrice.get(fundPriceId, facade);
		if (dataTypeId == TransactionGlobals.TRANSACTION_DEAL_TYPE_MID)
		{
		// type is mid so use basic pricing model
			return fp.getBasic();
		} else {
		// assume spread	
			return fp.getBid();
		}
	}

	/**
	 * Returns the price for a fund for DEST ACCOUNT
	 * 
	 * 	
	 * @param fundPriceId
	 * @param dataTypeId
	 * @param facade
	 * @return
	 * @throws DataException
	 */	
	public static String getDestPrice(int fundPriceId, int dataTypeId, FWFacade facade) 
	throws DataException
{
	FundPrice fp = FundPrice.get(fundPriceId, facade);
	if (dataTypeId == TransactionGlobals.TRANSACTION_DEAL_TYPE_MID)
	{
	// type is mid so use basic pricing model
		return fp.getBasic();
	} else {
	// assume spread	
		return fp.getOffer();
	}
}	

	/**
	 * Returns the basic price (used for calculating final units from final cash value of accounts)
	 * 
	 * 	
	 * @param fundPriceId
	 * @param dataTypeId
	 * @param facade
	 * @return
	 * @throws DataException
	 */	
	public static String getBasicPrice(int fundPriceId,  FWFacade facade) 
	throws DataException
{
	FundPrice fp = FundPrice.get(fundPriceId, facade);
		return fp.getBasic();
}		
	
	
	public static FundPrice get(String fundCode, FWFacade facade) throws DataException
	{
		DataResultSet rs = getData(fundCode,  facade);
		return get(rs);
	}

	public static FundPrice get( FWFacade facade, DataBinder binder) throws DataException
	{
		String fundCode = BinderUtils.getBinderStringValue(binder, "FUND_CODE");
		DataResultSet rs = getData(fundCode, facade);
		return get(rs);
	}
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		BinderUtils.addParamToBinder(binder, "FUND_CODE", this.getFundCode());
		BinderUtils.addDateParamToBinder(binder, "DATE_ADDED", this.getAddedDate());
		BinderUtils.addParamToBinder(binder, "BID", this.getBid());
		BinderUtils.addParamToBinder(binder, "BASIC", this.getBasic());
		BinderUtils.addParamToBinder(binder, "OFFER", this.getOffer());
		BinderUtils.addParamToBinder(binder, "ADDED_BY", this.getAddedBy());
		BinderUtils.addIntParamToBinder(binder, "FUND_PRICE_ID", this.getFundPriceId());
	}
	
	
	public void persist(FWFacade facade, String username) throws DataException {
		DataResultSet beforeData = FundPrice.getData(this.getFundCode(), facade);
		this.validate(facade);
		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);	
		facade.execute("qTransactions_UpdateFundPrice", binder);
		// Add audit record
		DataResultSet afterData = FundPrice.getData(this.getFundCode(), facade);

		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(this.getFundPriceId(), TransactionGlobals.SecondaryElementType.FundPrice.toString());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, addedBy, 
		 Globals.AuditActions.UPDATE.toString(), 
		 TransactionGlobals.SecondaryElementType.FundPrice.toString(), 
		 beforeData, afterData, auditRelations);			
	
	}

	
	public void setAttributes(DataBinder binder) throws DataException {		
		this.setFundCode(BinderUtils.getBinderStringValue(binder, "FUND_CODE"));
		this.setAddedDate(BinderUtils.getBinderDateValue(binder, "DATE_ADDED"));
		this.setBid(BinderUtils.getBinderStringValue(binder, "BID"));
		this.setBasic(BinderUtils.getBinderStringValue(binder, "BASIC"));
		this.setOffer(BinderUtils.getBinderStringValue(binder, "OFFER"));
		this.setAddedBy(BinderUtils.getBinderStringValue(binder, "ADDED_BY"));
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

	public String getFundCode() {
		return fundCode;
	}

	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}


	public String getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(String addedBy) {
		this.addedBy = addedBy;
	}

	public Date getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(Date addedDate) {
		this.addedDate = addedDate;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getBasic() {
		return basic;
	}

	public void setBasic(String basic) {
		this.basic = basic;
	}

	public String getOffer() {
		return offer;
	}

	public void setOffer(String offer) {
		this.offer = offer;
	}


}
