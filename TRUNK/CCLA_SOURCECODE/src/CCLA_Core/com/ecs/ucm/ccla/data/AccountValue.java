package com.ecs.ucm.ccla.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.utils.stellent.embedded.FWFacade;
import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.DataResultSetUtils;

public class AccountValue implements Persistable {
	
	private int accountId;
	private Date lastRefresh;
	private String cash;
	private String units;
	private String accruedIncome;

	
	public AccountValue( int accountId, String cash, String units, String accruedIncome, Date lastRefresh)
		
	{
		this.accountId = accountId;
		this.cash = cash;
		this.units = units;
		this.accruedIncome = accruedIncome;
		this.lastRefresh = lastRefresh;
	}
	
	
	public static AccountValue get(int accountId,  FWFacade facade) 
	throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addIntParamToBinder(binder, "ACCOUNT_ID", accountId);
		DataResultSet rs = facade.createResultSet("qClientServices_GetAccountValue", binder);		
		return get(rs);		
	}
	
	public static AccountValue get(DataResultSet rs) throws DataException
	{
	 if (rs.isEmpty())
	 {
		return null; 
	 } else {
		return new AccountValue(
				DataResultSetUtils.getResultSetIntegerValue(rs, "ACCOUNT_ID"),				
				DataResultSetUtils.getResultSetStringValue(rs, "ACC_CASH"),
				DataResultSetUtils.getResultSetStringValue(rs, "ACC_UNITS"),
				DataResultSetUtils.getResultSetStringValue(rs, "ACC_ACCRUED_INCOME"),
				rs.getDateValueByName("DATE_LAST_REFRESH")
				);
	 }
	}

	/** Gets the unit total for all open accounts with a particular share class
	 * 
	 * @param shareClassId
	 * @param facade
	 * @return Sum of units of open accounts in the share class
	 * @throws DataException 
	 */
	public static String getUnitTotalByShareClass(int shareClassId, FWFacade facade) throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addIntParamToBinder(binder, "SHARE_CLASS_ID", shareClassId);
		DataResultSet rs = facade.createResultSet("qClientServices_getUnitTotalByShareClass", binder);
		if (rs.isEmpty())
			return null;
		else
		{
			return rs.getStringValueByName("UNIT_TOTAL");
		}
		
	}

	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		BinderUtils.addIntParamToBinder(binder, "ACCOUNT_ID", this.getAccountId());
		BinderUtils.addDateParamToBinder(binder, "DATE_LAST_REFRESH", this.getLastRefresh());
		BinderUtils.addParamToBinder(binder, "ACC_CASH", this.getCash());
		BinderUtils.addParamToBinder(binder, "ACC_UNITS", this.getUnits());
		BinderUtils.addParamToBinder(binder, "ACC_ACCRUED_INCOME", this.getAccruedIncome());

	}

	public void persist(FWFacade facade, String username) throws DataException {
		this.validate(facade);
		DataBinder binder = new DataBinder();
		addFieldsToBinder(binder);
		facade.execute("qClientServices_UpdateAccountValue", binder);
	}

	public void setAttributes(DataBinder binder) throws DataException {
		this.setAccountId(BinderUtils.getBinderIntegerValue(binder, "ACCOUNT_ID"));
		this.setLastRefresh(BinderUtils.getBinderDateValue(binder, "DATE_LAST_REFRESH"));
		this.setCash(BinderUtils.getBinderStringValue(binder, "ACC_CASH"));
		this.setUnits(BinderUtils.getBinderStringValue(binder, "ACC_UNITS"));
		this.setAccruedIncome(BinderUtils.getBinderStringValue(binder, "ACC_ACCRUED_INCOME"));

	}

	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub

	}



	public int getAccountId() {
		return accountId;
	}


	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}


	public Date getLastRefresh() {
		return lastRefresh;
	}


	public void setLastRefresh(Date lastRefresh) {
		this.lastRefresh = lastRefresh;
	}


	public String getCash() {
		return cash;
	}


	public void setCash(String cash) {
		this.cash = cash;
	}


	public String getUnits() {
		return units;
	}


	public void setUnits(String units) {
		this.units = units;
	}


	public String getAccruedIncome() {
		return accruedIncome;
	}


	public void setAccruedIncome(String accruedIncome) {
		this.accruedIncome = accruedIncome;
	}






}
