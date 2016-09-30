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

public class AccountValueAudit implements Persistable {
	
	private int accountValueAuditId;
	private int accountId;
	private Date accountValueDate;
	private String openingCash;
	private String openingUnits;
	private String closingCash;
	private String closingUnits;

	
	public AccountValueAudit(int accountValueAuditId, int accountId, Date accountValueDate,
			String openingCash, String openingUnits, String closingCash, String closingUnits)
	{
		this.accountValueAuditId = accountValueAuditId;
		this.accountId = accountId;
		this.accountValueDate = accountValueDate;
		this.openingCash = openingCash;
		this.openingUnits = openingUnits;
		this.closingCash = closingCash;
		this.closingUnits = closingUnits;
	}
	
	
	public static AccountValueAudit get(int accountId, Date accountValueDate, FWFacade facade) 
	throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addIntParamToBinder(binder, "ACCOUNT_ID", accountId);
		BinderUtils.addDateParamToBinder(binder, "ACCOUNT_VALUE_DATE", accountValueDate);
		DataResultSet rs = facade.createResultSet("qClientServices_GetAccountValueAudit", binder);		
		return get(rs);		
	}
	
	public static AccountValueAudit get(DataResultSet rs) throws DataException
	{
	 if (rs.isEmpty())
	 {
		return null; 
	 } else {
		return new AccountValueAudit(
				DataResultSetUtils.getResultSetIntegerValue(rs, "ACCOUNT_VALUE_AUDIT_ID"),
				DataResultSetUtils.getResultSetIntegerValue(rs, "ACCOUNT_ID"),
				rs.getDateValueByName("ACCOUNT_VALUE_DATE"),
				DataResultSetUtils.getResultSetStringValue(rs, "OPENING_CASH"),
				DataResultSetUtils.getResultSetStringValue(rs, "OPENING_UNITS"),
				DataResultSetUtils.getResultSetStringValue(rs, "CLOSING_CASH"),
				DataResultSetUtils.getResultSetStringValue(rs, "CLOSING_UNITS")
				);
	 }
	}

	public static AccountValueAudit add(int accountId, Date accountValueDate,
			String openingCash, String openingUnits, String closingCash, String closingUnits, String unitPrice,
			FWFacade facade, String username) throws NumberFormatException, DataException
	{
		DataBinder binder = new DataBinder();
		// Fetch new ID for this account value audit.
		String accountValueAudit = CCLAUtils.getNewKey("AccountAudit", facade);
		Integer accValAuditId = Integer.parseInt(accountValueAudit);
		AccountValueAudit accValAudit = new AccountValueAudit(accValAuditId, accountId, accountValueDate, openingCash,
				openingUnits, closingCash, closingUnits);
		accValAudit.addFieldsToBinder(binder);
		facade.execute("qClientServices_addAccountValueAudit", binder);
		return accValAudit;
	}
	
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		BinderUtils.addIntParamToBinder(binder, "ACCOUNT_VALUE_AUDIT_ID", this.getAccountValueAuditId());
		BinderUtils.addIntParamToBinder(binder, "ACCOUNT_ID", this.getAccountId());
		BinderUtils.addDateParamToBinder(binder, "ACCOUNT_VALUE_DATE", this.getAccountValueDate());
		BinderUtils.addParamToBinder(binder, "OPENING_UNITS", this.getOpeningUnits());
		BinderUtils.addParamToBinder(binder, "OPENING_CASH", this.getOpeningCash());
		BinderUtils.addParamToBinder(binder, "CLOSING_CASH", this.getClosingCash());
		BinderUtils.addParamToBinder(binder, "CLOSING_UNITS", this.getClosingUnits());

	}

	public void persist(FWFacade facade, String username) throws DataException {
		this.validate(facade);
		DataBinder binder = new DataBinder();
		addFieldsToBinder(binder);
		facade.execute("qClientServices_UpdateAccountValueAudit", binder);
	}

	public void setAttributes(DataBinder binder) throws DataException {
		this.setAccountId(BinderUtils.getBinderIntegerValue(binder, "ACCOUNT_ID"));
		this.setAccountValueDate(BinderUtils.getBinderDateValue(binder, "ACCOUNT_VALUE_DATE"));
		this.setOpeningCash(BinderUtils.getBinderStringValue(binder, "OPENING_CASH"));
		this.setClosingCash(BinderUtils.getBinderStringValue(binder, "CLOSING_CASH"));
		this.setOpeningUnits(BinderUtils.getBinderStringValue(binder, "OPENING_UNITS"));
		this.setClosingUnits(BinderUtils.getBinderStringValue(binder, "CLOSING_UNITS"));
	}

	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub

	}


	public int getAccountValueAuditId() {
		return accountValueAuditId;
	}


	public void setAccountValueAuditId(int accountValueAuditId) {
		this.accountValueAuditId = accountValueAuditId;
	}


	public int getAccountId() {
		return accountId;
	}


	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}


	public Date getAccountValueDate() {
		return accountValueDate;
	}


	public void setAccountValueDate(Date accountValueDate) {
		this.accountValueDate = accountValueDate;
	}


	public String getOpeningCash() {
		return openingCash;
	}


	public void setOpeningCash(String openingCash) {
		this.openingCash = openingCash;
	}


	public String getOpeningUnits() {
		return openingUnits;
	}


	public void setOpeningUnits(String openingUnits) {
		this.openingUnits = openingUnits;
	}


	public String getClosingCash() {
		return closingCash;
	}


	public void setClosingCash(String closingCash) {
		this.closingCash = closingCash;
	}


	public String getClosingUnits() {
		return closingUnits;
	}


	public void setClosingUnits(String closingUnits) {
		this.closingUnits = closingUnits;
	}






}
