package com.ecs.ucm.ccla.transactions;


import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Persistable;
import com.ecs.utils.stellent.embedded.FWFacade;

public class FundEODStatus implements Persistable{

	private int EODstatusId;
	private String statusName;	

	public FundEODStatus(int EODstatusId, String statusName)
	{
		this.EODstatusId = EODstatusId;
		this.statusName = statusName;
	}
	
	public FundEODStatus(DataBinder binder) throws DataException
	{
		this.setAttributes(binder);
	}
	
	public static FundEODStatus get(DataResultSet rs) throws DataException
	{
		if (rs.isEmpty())
			return null;
		else
			return new FundEODStatus(
					CCLAUtils.getResultSetIntegerValue(rs, "FUND_EOD_STATUS_ID"),
					CCLAUtils.getResultSetStringValue(rs, "FUND_EOD_STATUS_NAME"));
	}

	public static DataResultSet getData(int statusId, FWFacade facade) throws DataException
	{
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, "FUND_EOD_STATUS_ID", statusId);
		return facade.createResultSet("qTransactions_GetFundEodStatus", binder);
	}


	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		CCLAUtils.addQueryIntParamToBinder(binder, "FUND_EOD_STATUS_ID", this.getEODstatusId());
		CCLAUtils.addQueryParamToBinder(binder, "FUND_EOD_STATUS_NAME", this.getStatusName());
	}
	
	
	public void persist(FWFacade facade, String username) throws DataException {
		this.validate(facade);
		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);
		facade.execute("qTransactions_UpdateFundEodStatus", binder);
	}

	
	public void setAttributes(DataBinder binder) throws DataException {		
		this.setEODstatusId(CCLAUtils.getBinderIntegerValue(binder, "FUND_EOD_STATUS_ID"));
		this.setStatusName(CCLAUtils.getBinderStringValue(binder, "FUND_EOD_STATUS_NAME"));
	}
	
	
	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub
}

	public int getEODstatusId() {
		return EODstatusId;
	}

	public void setEODstatusId(int eODstatusId) {
		EODstatusId = eODstatusId;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}


}
