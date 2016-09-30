package com.ecs.ucm.ccla.transactions.services;

import java.util.Date;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.transactions.FundEOD;
import com.ecs.ucm.ccla.transactions.FundNavAudit;
import com.ecs.ucm.ccla.transactions.IncomeExpense;
import com.ecs.ucm.ccla.transactions.IncomeExpenseApplied;
import com.ecs.ucm.ccla.transactions.globals.TransactionGlobals;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.server.Service;

public class IncomeExpenseService extends Service {
	
	
	public void getIncomeExpenseBasic() throws ServiceException
	{
		String strExpenseId = BinderUtils.getBinderStringValue(m_binder, "INCOME_EXPENSE_ID");
		if (StringUtils.stringIsBlank(strExpenseId))
			throw new ServiceException("Missing INCOME_EXPENSE_ID");
		
		try {
			
			int expenseId = BinderUtils.getBinderIntegerValue(m_binder, "INCOME_EXPENSE_ID");		
			FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
			DataResultSet rsExpense = IncomeExpense.getData(expenseId, facade);
			if (!rsExpense.isEmpty())
				m_binder.addResultSet("rsExpense", rsExpense);	
			
			
		} catch (DataException e) {
			String msg = "Unable to get Expense: " + e.getMessage();
			Log.error(msg, e);		
			throw new ServiceException(msg, e);
		}
	}
	
	public void updateExpense() throws ServiceException, DataException {
		
		String strExpenseId = BinderUtils.getBinderStringValue(m_binder, "INCOME_EXPENSE_ID");
		if (StringUtils.stringIsBlank(strExpenseId))
			throw new ServiceException("Missing INCOME_EXPENSE_ID");
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		try {			
			int expenseId = BinderUtils.getBinderIntegerValue(m_binder, "INCOME_EXPENSE_ID");					
			facade.beginTransaction();
			IncomeExpense expense = IncomeExpense.get(expenseId, facade);
			expense.setAttributes(m_binder);
			String username = m_userData.m_name;
			m_binder.putLocal("USER_ID", username);	
			expense.persist(facade, username);
			facade.commitTransaction();
			
		} catch (DataException e) {
			String msg = "Unable to update expense : "+strExpenseId + "," + e.getMessage();
			Log.error(msg, e);		
			facade.rollbackTransaction();
			throw new ServiceException(msg, e);
		}
	}
	/** Adds a new value for the daily income and adds/updates the daily nav
	 * 
	 * @throws ServiceException
	 * @throws DataException
	 */
	public void updateIncome() throws ServiceException, DataException
	{
		Log.debug("starting updateIncome");
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		String username = m_userData.m_name;
		String fundCode = TransactionGlobals.PSDF_FUND_CODE;
		try {
			facade.beginTransaction();
			// 9 June 2011 - have to use fundeod object run date instead of report date
			// as new requirement is to run eods for past dates
			//Date reportDate = EndOfDayService.getRolloverDate().getTime();
			FundEOD latestEOD = FundEOD.getLatestByStatus(fundCode, TransactionGlobals.FUND_EOD_STATUS_IN_PROGRESS_ID, facade);
			Date incomeDate = latestEOD.getRunDate();
			String incomeExpenseValue = BinderUtils.getBinderStringValue(m_binder, "DAILY_VALUE");
			Log.debug("got fund income value:" + incomeExpenseValue);
			String grossIncomeValue = BinderUtils.getBinderStringValue(m_binder, "DAILY_GROSS_VALUE");
			Log.debug("got gross value:" + grossIncomeValue);
			String navValue = BinderUtils.getBinderStringValue(m_binder, "NAV");
			Log.debug("got navValue:" + navValue);
			
			// create incomeExpense object for fund income (daily value)
			IncomeExpense income = IncomeExpense.add(fundCode, "", "Fund Income",
					TransactionGlobals.FUND_INCOME_TYPE_ID, "", incomeExpenseValue, 
					false, true, true, username, facade);
			Log.debug("created fund income with id " + income.getIncomeExpenseId());

			// create incomeExpense object for fund income (gross value)
			IncomeExpense grossIncome = IncomeExpense.add(fundCode, "", "Gross Income",
					TransactionGlobals.GROSS_INCOME_TYPE_ID, "", grossIncomeValue, 
					false, true, true, username, facade);
			Log.debug("created GROSS income with id " + grossIncome.getIncomeExpenseId());			
			
			// now create or update IncomeExpenseApplied objects

			// does the fund income applied already exist?
			boolean incomeAppliedExists 
			= getIsIncomeAppliedByDate(fundCode, TransactionGlobals.FUND_INCOME_TYPE_ID, incomeDate, facade);
			Log.debug("incomeAppliedExists is " + incomeAppliedExists );
			
			if (incomeAppliedExists)
			{
			// need to update
				Log.debug("updating fund income");
				IncomeExpenseApplied applied = IncomeExpenseApplied.getIncomeAppliedByDate(fundCode, TransactionGlobals.FUND_INCOME_TYPE_ID, incomeDate, facade);
				if (StringUtils.stringIsBlank(incomeExpenseValue))
					applied.setIncomeExpenseValue("");
				else
					applied.setIncomeExpenseValue(incomeExpenseValue);
				applied.persist(facade, username);
			} else
			{
			// need to create	
				Log.debug("report date for create is " + incomeDate);
				DataBinder binder = new DataBinder();
				BinderUtils.addIntParamToBinder(binder, "INCOME_EXPENSE_ID", income.getIncomeExpenseId());
				BinderUtils.addParamToBinder(binder, "INCOME_EXPENSE_VALUE", incomeExpenseValue);
				BinderUtils.addDateParamToBinder(binder, "INCOME_EXPENSE_DATE", incomeDate);
				BinderUtils.addParamToBinder(binder, "ADDED_BY", username);
				IncomeExpenseApplied applied = IncomeExpenseApplied.add(binder, facade, username);
				Log.debug("Created incomeApplied with id:" + applied.getIncomeExpenseAppliedId());
			}

			// does the gross income applied already exist?
			boolean grossIncomeAppliedExists 
			= getIsIncomeAppliedByDate(fundCode, TransactionGlobals.GROSS_INCOME_TYPE_ID, incomeDate, facade);
			Log.debug("grossIncomeAppliedExists is " + grossIncomeAppliedExists );
			
			if (grossIncomeAppliedExists)
			{
			// need to update
				Log.debug("updating GROSS fund income");
				IncomeExpenseApplied gross = IncomeExpenseApplied.getIncomeAppliedByDate(fundCode, TransactionGlobals.GROSS_INCOME_TYPE_ID, incomeDate, facade);
				if (StringUtils.stringIsBlank(incomeExpenseValue))
					gross.setIncomeExpenseValue("");
				else
					gross.setIncomeExpenseValue(grossIncomeValue);
				gross.persist(facade, username);
			} else
			{
			// need to create	
				Log.debug("report date for create is " + incomeDate);
				DataBinder binder = new DataBinder();
				BinderUtils.addIntParamToBinder(binder, "INCOME_EXPENSE_ID", grossIncome.getIncomeExpenseId());
				BinderUtils.addParamToBinder(binder, "INCOME_EXPENSE_VALUE", grossIncomeValue);
				BinderUtils.addDateParamToBinder(binder, "INCOME_EXPENSE_DATE", incomeDate);
				BinderUtils.addParamToBinder(binder, "ADDED_BY", username);
				IncomeExpenseApplied applied = IncomeExpenseApplied.add(binder, facade, username);
				Log.debug("Created incomeApplied with id:" + applied.getIncomeExpenseAppliedId());
			}			
			
			// create the nav
			FundNavAudit fna = FundNavAudit.get(fundCode, incomeDate, facade);
			if (fna==null)
			{
				// create
				fna = FundNavAudit.add(fundCode, incomeDate, navValue, username, facade);
			}  else
			{
				// update
				fna.setNav(navValue);
				fna.persist(facade, username);
			}
			
			facade.commitTransaction();
			
		} catch (DataException e) {
			String msg = "Unable to update income : " + e.getMessage();
			Log.error(msg, e);		
			facade.rollbackTransaction();
			throw new ServiceException(msg, e);
		}
		
	}
	
	public void removeExpense() throws ServiceException, DataException 
	{
		String strExpenseId = BinderUtils.getBinderStringValue(m_binder, "INCOME_EXPENSE_ID");
		if (StringUtils.stringIsBlank(strExpenseId))
			throw new ServiceException("Missing INCOME_EXPENSE_ID");
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		String username = m_userData.m_name;
		try {			
			int expenseId = BinderUtils.getBinderIntegerValue(m_binder, "INCOME_EXPENSE_ID");					
			facade.beginTransaction();
			IncomeExpense.remove(expenseId, username, facade);
			facade.commitTransaction();
			
		} catch (DataException e){
		facade.rollbackTransaction();
		String msg = "Unable to remove expense : "+strExpenseId + "," + e.getMessage();
		Log.error(msg, e);	
		throw new ServiceException(msg, e);
		}

	}
	
	public void addExpense() throws DataException, ServiceException
	{
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		String username = m_userData.m_name;
		
		try {
			facade.beginTransaction();
			IncomeExpense expense = IncomeExpense.add(m_binder, facade, username);
			
			Log.debug("added expense with id:" + expense.getIncomeExpenseId());
			String redirectUrl = m_binder.getLocal("RedirectUrl");
			redirectUrl = redirectUrl + expense.getIncomeExpenseId();
			m_binder.putLocal("RedirectUrl", redirectUrl);
			
			facade.commitTransaction();
		} catch (DataException e) {
			String msg = "Unable to create expense " + e.getMessage();
			Log.error(msg, e);		
			facade.rollbackTransaction();
			throw new ServiceException(msg, e);
		}
		
	}
	
	public boolean getIsIncomeAppliedByDate(String fundCode, int incomeExpenseTypeId, Date date, FWFacade facade) 
	throws DataException
	{
	boolean isApplied = false;
	// get dataresultset for incomeApplied
	IncomeExpenseApplied applied
	 = IncomeExpenseApplied.getIncomeAppliedByDate(fundCode, incomeExpenseTypeId, date, facade);

		if (applied != null)
			isApplied = true;
	
	return isApplied;
	}

	
}
