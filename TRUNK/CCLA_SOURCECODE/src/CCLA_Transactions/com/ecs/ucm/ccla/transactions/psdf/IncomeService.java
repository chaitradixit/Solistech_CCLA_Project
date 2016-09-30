package com.ecs.ucm.ccla.transactions.psdf;

import java.util.Date;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.transactions.FundEOD;
import com.ecs.ucm.ccla.transactions.IncomeExpenseApplied;
import com.ecs.ucm.ccla.transactions.globals.TransactionGlobals;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.server.Service;

public class IncomeService extends Service {
	
	/**
	 * Gets the daily income from the income_expense_table and the nav from the
	 * fund_nav_audit table
	 * 
	 * 
	 * @throws ServiceException
	 * @throws DataException 
	 */
	public void getIncome() throws ServiceException, DataException
	{
		// get process date for today
		// get fund code from config file
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		try {
			String fundCode = TransactionGlobals.PSDF_FUND_CODE;
			//Date reportDate = EndOfDayService.getRolloverDate().getTime();
			FundEOD latestEOD = FundEOD.getLatestByStatus(fundCode, TransactionGlobals.FUND_EOD_STATUS_IN_PROGRESS_ID, facade);
			Date incomeDate = latestEOD.getRunDate();
			int incomeTypeId = TransactionGlobals.FUND_INCOME_TYPE_ID;
			int grossIncomeTypeId = TransactionGlobals.GROSS_INCOME_TYPE_ID;
			
			IncomeExpenseApplied incomeApplied = IncomeExpenseApplied.getByDateFundType(fundCode, incomeTypeId, incomeDate, facade);
			if (incomeApplied != null)
				CCLAUtils.addQueryParamToBinder(m_binder, "dailyIncome", incomeApplied.getIncomeExpenseValue());

			IncomeExpenseApplied grossIncomeApplied = IncomeExpenseApplied.getByDateFundType(fundCode, grossIncomeTypeId, incomeDate, facade);
			if (grossIncomeApplied != null)
				CCLAUtils.addQueryParamToBinder(m_binder, "grossIncome", grossIncomeApplied.getIncomeExpenseValue());			
			

			DataBinder binder = new DataBinder();
			CCLAUtils.addQueryParamToBinder(binder, "FUND_CODE", fundCode);
			CCLAUtils.addQueryDateParamToBinder(binder, "NAV_DATE", incomeDate);
			DataResultSet rsNav = facade.createResultSet("qTransactions_GetFundNav", binder);
			if (!rsNav.isEmpty())
			{
			do {
				String nav = rsNav.getStringValueByName("NAV");
				if (!StringUtils.stringIsBlank(nav))
					CCLAUtils.addQueryParamToBinder(m_binder, "dailyNav", nav);				
			} while (rsNav.next());
			}
		} catch (DataException e) {
			String msg = "Unable to get Expense: " + e.getMessage();
			Log.error(msg, e);		
			throw new ServiceException(msg, e);
		}
	}
	

}
