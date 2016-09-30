package com.ecs.ucm.ccla.transactions;

import java.util.Date;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.data.Persistable;
import com.ecs.ucm.ccla.transactions.utils.TransactionUtils;

import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.DataResultSetUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

public class FundNavAudit implements Persistable{

	private int fundNavId;
	private String fundCode;
	private Date navDate;	
	private String nav;

	public FundNavAudit(int fundNavId, String fundCode, Date navDate, String nav)
	{
		this.fundNavId = fundNavId;
		this.fundCode = fundCode;
		this.navDate = navDate;
		this.nav = nav;
	}
	
	public FundNavAudit(DataBinder binder) throws DataException
	{
		this.setAttributes(binder);
	}
	
	public static FundNavAudit get(DataResultSet rs) throws DataException
	{
		if (rs.isEmpty())
			return null;
		else
			return new FundNavAudit(
					DataResultSetUtils.getResultSetIntegerValue(rs, "FUND_NAV_ID"),
					DataResultSetUtils.getResultSetStringValue(rs, "FUND_CODE"),
					rs.getDateValueByName("NAV_DATE"),
					DataResultSetUtils.getResultSetStringValue(rs, "NAV"));
	}

	public static DataResultSet getData(int fundNavId, FWFacade facade) throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addIntParamToBinder(binder, "FUND_NAV_ID", fundNavId);
		return facade.createResultSet("qTransactions_GetFundNavById", binder);
	}
	/** Adds a new fund nav audit entry or updates and existing one if one is already
	 * present for the date.  Only one fund nav audit is possible per day
	 * 
	 * @param fundCode
	 * @param auditDate
	 * @param nav
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static FundNavAudit add(String fundCode, Date auditDate, String nav, String username, FWFacade facade) 
	throws DataException
	{
		DataBinder binder = new DataBinder();
			// get new id value
			int fundNavId = Integer.parseInt(
					 TransactionUtils.getNewKey("FundNavAudit", facade));				
			BinderUtils.addIntParamToBinder(binder, "FUND_NAV_ID", fundNavId);
			BinderUtils.addParamToBinder(binder, "FUND_CODE", fundCode);
			BinderUtils.addDateParamToBinder(binder, "NAV_DATE", auditDate);
			BinderUtils.addParamToBinder(binder, "NAV", nav);
			FundNavAudit fundaudit = new FundNavAudit(binder);
			facade.execute("qTransactions_AddFundNavAudit", binder);
			
		return fundaudit;
		
	}
	

	public void addFieldsToBinder(DataBinder binder) throws DataException {
		BinderUtils.addIntParamToBinder(binder, "FUND_NAV_ID", this.getFundNavId());
		BinderUtils.addParamToBinder(binder, "FUND_CODE", this.getFundCode());
		BinderUtils.addDateParamToBinder(binder, "NAV_DATE", this.getNavDate());
		BinderUtils.addParamToBinder(binder, "NAV", this.getNav());
	}
	
	
	public void persist(FWFacade facade, String username) throws DataException {
		this.validate(facade);
		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);
		facade.execute("qTransactions_UpdateFundNavAudit", binder);
	}

	
	public void setAttributes(DataBinder binder) throws DataException {	
		this.setFundNavId(BinderUtils.getBinderIntegerValue(binder, "FUND_NAV_ID"));
		this.setFundCode(BinderUtils.getBinderStringValue(binder, "FUND_CODE"));
		this.setNavDate(BinderUtils.getBinderDateValue(binder, "NAV_DATE"));
		this.setNav(BinderUtils.getBinderStringValue(binder, "NAV"));

	}
	
	public static FundNavAudit get(String fundCode, Date auditDate, FWFacade facade) 
	throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addParamToBinder(binder, "FUND_CODE", fundCode);
		BinderUtils.addDateParamToBinder(binder, "NAV_DATE", auditDate);
		DataResultSet rs = facade.createResultSet("qTransactions_GetFundNav", binder);
		if (!rs.isEmpty())
			return get(rs);
		else
			return null;
	}

	public static FundNavAudit getLatest(String fundCode,  FWFacade facade) 
	throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addParamToBinder(binder, "FUND_CODE", fundCode);
		DataResultSet rs = facade.createResultSet("qTransactions_GetLatestFundNavAudit", binder);
		if (!rs.isEmpty())
			return get(rs);
		else
			return null;
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

	public String getNav() {
		return nav;
	}

	public void setNav(String nav) {
		this.nav = nav;
	}

	public int getFundNavId() {
		return fundNavId;
	}

	public void setFundNavId(int fundNavId) {
		this.fundNavId = fundNavId;
	}

	public Date getNavDate() {
		return navDate;
	}

	public void setNavDate(Date navDate) {
		this.navDate = navDate;
	}

}
