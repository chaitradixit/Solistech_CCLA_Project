package com.ecs.ucm.ccla.transactions;

import java.math.BigDecimal;
import java.util.Date;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.data.Persistable;
import com.ecs.ucm.ccla.transactions.utils.TransactionUtils;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.DataResultSetUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

public class FundValuation implements Persistable{

	private String fundCode;
	private Date valuationDate;	
	private String shareClassId;
	private boolean isBuy;
	private String basic;
	private String bid;
	private String offer;
	private String updatedBy;
	private Date updatedDate;

	public FundValuation(String fundCode,  String shareClassId, Date valuationDate, 
			 String basic, String bid, String offer, boolean isBuy,
			String updatedBy, Date updatedDate)
	{
		this.fundCode = fundCode;
		this.valuationDate = valuationDate;
		this.shareClassId = shareClassId;
		this.basic = basic;
		this.bid = bid;
		this.offer = offer;
		this.isBuy = isBuy;
		this.updatedBy = updatedBy;
		this.updatedDate = updatedDate;
	}
	
	public FundValuation(DataBinder binder) throws DataException
	{
		this.setAttributes(binder);
	}
	
	public static FundValuation get(DataResultSet rs) throws DataException
	{
		if (rs.isEmpty())
			return null;
		else
			return new FundValuation(
					DataResultSetUtils.getResultSetStringValue(rs, "FUND_CODE"),
					DataResultSetUtils.getResultSetStringValue(rs, "SHARE_CLASS_ID"),
					rs.getDateValueByName("VALUATION_DATE"),					
					DataResultSetUtils.getResultSetStringValue(rs, "BASIC"),
					DataResultSetUtils.getResultSetStringValue(rs, "BID"),
					DataResultSetUtils.getResultSetStringValue(rs, "OFFER"),
					DataResultSetUtils.getResultSetBoolValue(rs, "IS_BUY"),
					DataResultSetUtils.getResultSetStringValue(rs, "UPDATED_BY"),
					rs.getDateValueByName("LAST_UPDATED"));
	}

	public static DataResultSet getData(String fundCode, Date valuationDate, FWFacade facade) throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addParamToBinder(binder, "FUND_CODE", fundCode);
		BinderUtils.addDateParamToBinder(binder, "VALUATION_DATE", valuationDate);
		return facade.createResultSet("qTransactions_GetFundValuation", binder);
	}
	
	public static FundValuation add(String fundCode, String shareClassId, Date valuationDate, 
			String basic, String bid, String offer, boolean isBuy, String updatedBy,
			Date updatedDate, FWFacade facade) 
	throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addParamToBinder(binder, "FUND_CODE", fundCode);
		BinderUtils.addDateParamToBinder(binder, "VALUATION_DATE", valuationDate);
		BinderUtils.addParamToBinder(binder, "SHARE_CLASS_ID", shareClassId);
		BinderUtils.addParamToBinder(binder, "BASIC", basic);
		BinderUtils.addParamToBinder(binder, "BID", bid);
		BinderUtils.addParamToBinder(binder, "OFFER", offer);
		BinderUtils.addBooleanParamToBinder(binder, "IS_BUY", isBuy);
		BinderUtils.addParamToBinder(binder, "UPDATED_BY", updatedBy);
		BinderUtils.addDateParamToBinder(binder, "LAST_UPDATED", updatedDate);
		FundValuation fundaudit = new FundValuation(binder);
		facade.execute("qTransactions_AddFundValuation", binder);
		
		// TODO auditing
		return fundaudit;		
	}
	
	public static boolean hasFundAudit(String fundCode, Date valuationDate, FWFacade facade) 
	throws DataException
	{
		boolean hasAudit = false;
		DataBinder binder = new DataBinder();
		BinderUtils.addParamToBinder(binder, "FUND_CODE", fundCode);
		BinderUtils.addDateParamToBinder(binder, "VALUATION_DATE", valuationDate);
		DataResultSet rs = facade.createResultSet("qTransactions_GetFundValuationByDate", binder);
		if (!rs.isEmpty())
			hasAudit = true;
		return hasAudit;
	}
	
	public static FundValuation get(String fundCode, String shareClassId, Date valuationDate, FWFacade facade) 
	throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addParamToBinder(binder, "FUND_CODE", fundCode);
		BinderUtils.addParamToBinder(binder, "SHARE_CLASS_ID", shareClassId);
		BinderUtils.addDateParamToBinder(binder, "VALUATION_DATE", valuationDate);
		DataResultSet rs = facade.createResultSet("qTransactions_GetFundValuationByShareClass", binder);
		if (!rs.isEmpty())
			return get(rs);
		else
			return null;
	}
	
	public static FundValuation get(String fundCode, Date valuationDate, FWFacade facade) throws DataException
	{
		DataResultSet rs = getData(fundCode, valuationDate, facade);
		return get(rs);
	}
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		BinderUtils.addParamToBinder(binder, "FUND_CODE", this.getFundCode());
		BinderUtils.addParamToBinder(binder, "SHARE_CLASS_ID", this.getShareClassId());
		BinderUtils.addDateParamToBinder(binder, "VALUATION_DATE", this.getValuationDate());
		BinderUtils.addParamToBinder(binder, "BASIC", this.getBasic());
		BinderUtils.addParamToBinder(binder, "BID", this.getBid());
		BinderUtils.addParamToBinder(binder, "OFFER", this.getOffer());
		BinderUtils.addBooleanParamToBinder(binder, "IS_BUY", this.isBuy());
		BinderUtils.addParamToBinder(binder, "UPDATED_BY", this.getUpdatedBy());
		BinderUtils.addDateParamToBinder(binder, "LAST_UPDATED", this.getUpdatedDate());
	}
	
	
	public void persist(FWFacade facade, String username) throws DataException {
		this.validate(facade);
		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);		
		// TODO auditing
		facade.execute("qTransactions_UpdateFundAudit", binder);
	}

	
	public void setAttributes(DataBinder binder) throws DataException {		
		this.setFundCode(BinderUtils.getBinderStringValue(binder, "FUND_CODE"));
		this.setShareClassId(BinderUtils.getBinderStringValue(binder, "SHARE_CLASS_ID"));
		this.setValuationDate(BinderUtils.getBinderDateValue(binder, "VALUATION_DATE"));
		this.setBasic(BinderUtils.getBinderStringValue(binder, "BASIC"));
		this.setBid(BinderUtils.getBinderStringValue(binder, "BID"));
		this.setOffer(BinderUtils.getBinderStringValue(binder, "OFFER"));	
		this.setBuy(BinderUtils.getBinderBoolValue(binder, "IS_BUY"));
		this.setUpdatedBy(BinderUtils.getBinderStringValue(binder, "UPDATED_BY"));
		this.setUpdatedDate(BinderUtils.getBinderDateValue(binder, "LAST_UPDATED"));
	}
	
	
	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub
}

	public String getFundCode() {
		return fundCode;
	}

	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}

	public Date getValuationDate() {
		return valuationDate;
	}

	public void setValuationDate(Date valuationDate) {
		this.valuationDate = valuationDate;
	}

	public String getBasic() {
		return basic;
	}

	public void setBasic(String basic) {
		this.basic = basic;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

	public String getOffer() {
		return offer;
	}

	public void setOffer(String offer) {
		this.offer = offer;
	}

	public String getShareClassId() {
		return shareClassId;
	}

	public void setShareClassId(String shareClassId) {
		this.shareClassId = shareClassId;
	}


	public boolean isBuy() {
		return isBuy;
	}

	public void setBuy(boolean isBuy) {
		this.isBuy = isBuy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}


}
