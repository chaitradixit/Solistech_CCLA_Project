package com.ecs.ucm.ccla.transactions.psdf;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.comm.Comm;
import com.ecs.ucm.ccla.data.comm.CommGroup;
import com.ecs.ucm.ccla.data.comm.CommSource;
import com.ecs.ucm.ccla.data.comm.CommType;
import com.ecs.ucm.ccla.data.instruction.Instruction;
import com.ecs.ucm.ccla.data.instruction.InstructionData;
import com.ecs.ucm.ccla.data.instruction.InstructionDataApplied;
import com.ecs.ucm.ccla.data.instruction.InstructionStatus;
import com.ecs.ucm.ccla.data.instruction.InstructionType;
import com.ecs.ucm.ccla.transactions.FundAudit;
import com.ecs.ucm.ccla.transactions.FundEOD;
import com.ecs.ucm.ccla.transactions.globals.TransactionGlobals;
import com.ecs.ucm.ccla.transactions.utils.TransactionUtils;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.server.Service;

public class EndofDayTransactionService extends Service {
	
	/**
	 * Gets the End of day objects for PSDF fund code
	 * 
	 * 
	 * @throws ServiceException
	 * @throws DataException 
	 */
	public void createEndOfDayTransactions() throws ServiceException, DataException
	{
		// create communication object
		// look for PI accounts that are REIN and have mandated accounts
		// if some exist without mandated accounts then give error!
		// otherwise create REINDIV instruction for transfer of whole PI account value
		// from PI to PC (which should be mandated account) value
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		facade.beginTransaction();
		
		try {
			String fundCode = TransactionGlobals.PSDF_FUND_CODE;
			String piCode = TransactionGlobals.PI_FUND_CODE;
			String username = m_userData.m_name;
			DataBinder binder = new DataBinder();
			int counter = 0 ;
			// create comm group
			CommGroup group = CommGroup.add(null, null, facade, username);
			Comm eodComm = Comm.add(CommSource.SYSTEM, CommType.ENDOFDAY, 
					null, null,
					new Date(), username, null, null, group.getCommGroupId(), facade, username);
			
			Log.debug("created communication object");
			DataResultSet rsPI = Account.getOpenAccountsDataByFund(piCode, facade);
			Vector<Account> vAccounts = Account.getAccounts(rsPI);			
			Log.debug("got " + vAccounts.size() + " pi accounts");
			
			for (Account account:vAccounts)
			{
				Log.debug("account:" + account.getAccountId());
				Log.debug("got income distribution method:" + account.getIncomeDistMethod());
				if (account.getIncomeDistMethod().equalsIgnoreCase(Account.IncomeDistMethod.REIN.name()))
				{
					Log.debug("got account:" + account.getAccountId() + " which is " + account.getIncomeDistMethod());
					if (account.getMandatedAccId() != null)
					{
						Account mandAccount = Account.get(account.getMandatedAccId(), facade);
						Log.debug("got mandated account:" + mandAccount.getAccountId());
						// check this is a PC account?
						String mandFund = mandAccount.getFundCode();
						Log.debug("got fund code for mandated account:" + mandFund);
						if (mandFund.equalsIgnoreCase(TransactionGlobals.PSDF_FUND_CODE))
						{
							// create instruction
							// get PI account unit balance
							String transferUnits = Account.getAccountUnits(account.getAccountId(), facade);
							Log.debug("transferring " + transferUnits + " to account " + mandAccount.getAccountId());

							if (!StringUtils.stringIsBlank(transferUnits) && !transferUnits.equals("0"))
							{
							// Create new instruction
							InstructionStatus status = InstructionStatus.getCache().getCachedInstance(Integer.parseInt(TransactionGlobals.PROCESS_INSTRUCTION_STATUS_ID));
							
							Instruction ins = new Instruction
							 (eodComm.getCommId(), InstructionType.getIdCache().getCachedInstance(TransactionGlobals.PSDF_INSTRUCTION_TYPE_REINVEST_DIVDEND), 
									 status, 
									 null, true, username, facade);
							// need to set process date to before end of day process date so instruction
							// gets picked up
							Date rolloverDate = EndOfDayService.getRolloverDate(null).getTime();
							Calendar processCal = Calendar.getInstance();
							processCal.setTime(rolloverDate);
							processCal.add(Calendar.HOUR, -1);
							ins.setProcessDate(processCal.getTime());
							ins.setOriginalProcessDate(processCal.getTime());
							ins = Instruction.add(ins, username, facade);
							Log.debug("CREATED INSTRUCTION:" + ins.getInstructionId());
							counter = counter + 1;
							// applicable instruction data is
							// SOURCE_ACCOUNT_ID 1
							// DEST_ACCOUNT_ID 6
							// UNITS 4
							InstructionDataApplied.updateDataValueById(ins.getInstructionId(), 
									InstructionData.Fields.SOURCE_ACCOUNT_ID, Integer.toString(account.getAccountId()),
									username, facade);
							InstructionDataApplied.updateDataValueById(ins.getInstructionId(), 
									InstructionData.Fields.DEST_ACCOUNT_ID, Integer.toString(mandAccount.getAccountId()),
									username, facade);
							InstructionDataApplied.updateDataValueById(ins.getInstructionId(), 
									InstructionData.Fields.UNITS, transferUnits,
									username, facade);							
							}
							
							
						} else {
							Log.error("MANDATED ACCOUNT IS NOT PC ACCOUNT");
						}
					}
					else
					{
						Log.error("NO MANDATED ACCOUNT FOUND");
					}
				}
			}
			
			// return the instructions just created
			// will be PI accounts with status PROCESS_INSTRUCTION_STATUS_ID
			CCLAUtils.addQueryIntParamToBinder(m_binder, "TRANSACTION_NUMBERS", counter);
		
			
			facade.commitTransaction();
		} catch (DataException e) {
			String msg = "Unable to create transaction: " + e.getMessage();
			Log.error(msg, e);	
			facade.rollbackTransaction();
			throw new ServiceException(msg, e);
		}
	}
	
	/**
	 * Gets the End of day objects for a particular end of day
	 * 
	 * 
	 * @throws ServiceException
	 */
	public void getEndofDayDetails() throws ServiceException
	{
		// get process date for today
		// get fund code from config file

		try {
			Integer eodId = CCLAUtils.getBinderIntegerValue(m_binder, "EOD_ID");
			String fundCode =  TransactionGlobals.PSDF_FUND_CODE;
			if (eodId==null)
				throw new ServiceException("Cannot find eod_id");
			FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
			DataBinder binder = new DataBinder();
			CCLAUtils.addQueryIntParamToBinder(binder, "EOD_ID", eodId);
			DataResultSet rsEOD = facade.createResultSet("qTransactions_GetEndOfDayDetail", binder);
			CCLAUtils.addQueryIntParamToBinder(binder, "EOD_ID", eodId);
			DataResultSet rsFundAudit = facade.createResultSet("qTransactions_GetEndOfDayFundAudit", binder);
			DataResultSet rsIncomeExpense = facade.createResultSet("qTransactions_GetEndOfDayIncomeExpenseApplied", binder);
			if (!rsEOD.isEmpty())
				m_binder.addResultSet("rsEOD", rsEOD);
			if (!rsFundAudit.isEmpty())
				m_binder.addResultSet("rsFundAudit", rsFundAudit);	
			if (!rsIncomeExpense.isEmpty())
				m_binder.addResultSet("rsIncomeExpense", rsIncomeExpense);		
			FundEOD eod = FundEOD.get(eodId, facade);
			boolean isSameDay = TransactionUtils.isSameDay(eod.getRunDate(), eod.getRunStartDate());
			CCLAUtils.addQueryBooleanParamToBinder(m_binder, "isSameDay", isSameDay);
			CCLAUtils.addQueryDateParamToBinder(m_binder, "START_DATE", eod.getRunStartDate());
			CCLAUtils.addQueryDateParamToBinder(m_binder, "END_DATE", eod.getRunDate());
			
			// calculate percentage of shares in each class
			String strTotalUnits = FundAudit.getTotalClosingUnits(fundCode, eod.getRunDate(), facade);
			Log.debug("got total units of :" + strTotalUnits);
			BigDecimal TotalUnits = new BigDecimal(strTotalUnits);
			
			String[] shareClassCols = new String[] {
					"SHARE_CLASS_ID","PERCENTAGE", "CLOSING_UNITS", "TOTAL_EXPENSE", "INCOME_DISTRIBUTION"};
			DataResultSet rsShareClassDetails = new DataResultSet(shareClassCols);	
			BigDecimal hundred = new BigDecimal("100");
			do {
				// vector to hold resultset row
				Vector scV = new Vector();	
				
				FundAudit fa = FundAudit.get(rsFundAudit);
				scV.add(fa.getShareClassId());
				BigDecimal proportionUnits = new BigDecimal("0");
				BigDecimal closingUnits = new BigDecimal(fa.getClosingUnits());
				
			if (closingUnits.intValue() != 0)
				 proportionUnits = 
					closingUnits.divide(TotalUnits, TransactionGlobals.decimalPlaces, TransactionGlobals.roundRule);
				BigDecimal percentageUnits = proportionUnits.multiply(hundred);
				
				scV.add(percentageUnits);
				scV.add(closingUnits);
				scV.add(fa.getTotalExpense());
			
			// calculate net income distribution (income per unit multiplied by no of units)
			if (!StringUtils.stringIsBlank(fa.getIncomePerUnit()) && !fa.getIncomePerUnit().equalsIgnoreCase("0"))
			{
				BigDecimal unitIncome = new BigDecimal(fa.getIncomePerUnit());
				scV.add(closingUnits.multiply(unitIncome).divide(hundred, TransactionGlobals.decimalPlaces, TransactionGlobals.roundRule));				
			} else
			{
				scV.add("0");
			}			
			rsShareClassDetails.addRow(scV);			
			} while (rsFundAudit.next());
			
			m_binder.addResultSet("rsShareClassDetails", rsShareClassDetails);
			
		} catch (DataException e) {
			String msg = "Unable to get Expense: " + e.getMessage();
			Log.error(msg, e);		
			throw new ServiceException(msg, e);
		}
	}
	
	/**
	 * Gets the End of day objects for a particular end of day
	 * 
	 * 
	 * @throws ServiceException
	 */
	public void getEndofDayDetailsOld() throws ServiceException
	{
		// get process date for today
		// get fund code from config file

		try {
			Integer eodId = CCLAUtils.getBinderIntegerValue(m_binder, "EOD_ID");
			if (eodId==null)
				throw new ServiceException("Cannot find eod_id");
			FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
			DataBinder binder = new DataBinder();
			CCLAUtils.addQueryIntParamToBinder(binder, "EOD_ID", eodId);
			DataResultSet rsEOD = facade.createResultSet("qTransactions_GetEndOfDayDetail", binder);
			CCLAUtils.addQueryIntParamToBinder(binder, "EOD_ID", eodId);
			DataResultSet rsFundAudit = facade.createResultSet("qTransactions_GetEndOfDayFundAudit", binder);
			DataResultSet rsIncomeExpense = facade.createResultSet("qTransactions_GetEndOfDayIncomeExpenseApplied", binder);
			if (!rsEOD.isEmpty())
				m_binder.addResultSet("rsEOD", rsEOD);
			if (!rsFundAudit.isEmpty())
				m_binder.addResultSet("rsFundAudit", rsFundAudit);	
			if (!rsIncomeExpense.isEmpty())
				m_binder.addResultSet("rsIncomeExpense", rsIncomeExpense);		
		
			
		} catch (DataException e) {
			String msg = "Unable to get Expense: " + e.getMessage();
			Log.error(msg, e);		
			throw new ServiceException(msg, e);
		}
	}	
	
	public void getShareClassMovementReport() throws ServiceException
	{
		try {
			FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
			String fundCode = TransactionGlobals.PSDF_FUND_CODE;
			DataBinder binder = new DataBinder();
			DataResultSet rsSCM = facade.createResultSet("qTransactions_GetTodayShareClassMovementReport", binder);
			if (!rsSCM.isEmpty())
				m_binder.addResultSet("rsSCM", rsSCM);
			// get if fund eod exists - if not then end of day not run so no share class movements generated
			FundEOD eodInProgress = FundEOD.getByStatusRunDate(fundCode, new Date(), TransactionGlobals.FUND_EOD_STATUS_IN_PROGRESS_ID, facade);
			boolean hasEODStarted = false;
			boolean hasEODCompleted = false;
			if (eodInProgress != null)
			{
				// has fund audit been created for today? if so then there are genuinely no share class movements
				DataResultSet  rsFa = FundAudit.getFundAuditByDateRS(fundCode, eodInProgress.getRunDate(), facade);
				if (rsFa != null && !rsFa.isEmpty())
					hasEODStarted = true;
			}
			FundEOD eodComplete = FundEOD.getByStatusRunDate(fundCode, new Date(), TransactionGlobals.FUND_EOD_STATUS_COMPLETE_ID, facade);
			if (eodComplete != null)
				hasEODCompleted = true;
			CCLAUtils.addQueryBooleanParamToBinder(m_binder, "EOD_STARTED", hasEODStarted);
			CCLAUtils.addQueryBooleanParamToBinder(m_binder, "EOD_COMPLETED", hasEODCompleted);
			
		} catch (DataException e) {
			String msg = "Unable to get facade: " + e.getMessage();
			Log.error(msg, e);		
			throw new ServiceException(msg, e);
		}
		
	}
}
