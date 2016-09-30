package com.ecs.ucm.ccla.transactions.psdf;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.AccountValue;
import com.ecs.ucm.ccla.data.AccountValueAudit;
import com.ecs.ucm.ccla.data.Audit;
import com.ecs.ucm.ccla.data.ElementAttribute;
import com.ecs.ucm.ccla.data.ElementAttributeApplied;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.Globals;

import com.ecs.ucm.ccla.data.ShareClass;
import com.ecs.ucm.ccla.data.ShareClassGroup;
import com.ecs.ucm.ccla.data.ShareClassOverride;
import com.ecs.ucm.ccla.data.SharedCols;
import com.ecs.ucm.ccla.data.instruction.Instruction;
import com.ecs.ucm.ccla.data.instruction.InstructionData;
import com.ecs.ucm.ccla.data.instruction.InstructionDataApplied;
import com.ecs.ucm.ccla.transactions.*;
import com.ecs.ucm.ccla.transactions.globals.TransactionGlobals;
import com.ecs.ucm.ccla.transactions.services.ShareClassService;
import com.ecs.ucm.ccla.transactions.utils.TransactionUtils;
import com.ecs.ucm.ccla.utils.DateUtils;
import com.ecs.ucm.ccla.utils.ShareClassUtils;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.server.Service;

public class EndOfDayService extends Service {

	private static final BigDecimal hundred = new BigDecimal(100);
	

	// Binder values for storing the start and run dates.
//	private static final String PSDF_RUN_DATE = "PSDF_RUN_DATE";
//	private static final String PSDF_RUN_START_DATE = "PSDF_RUN_START_DATE";

	/**
	 * Run pre checks for end of day. This means checking when the last end of
	 * day was completed, checking that an end of day has not already been
	 * completed for today.
	 * 
	 * @throws ServiceException
	 * @throws DataException
	 */
	public void runPreEODChecks() throws ServiceException, DataException {
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		String username = m_userData.m_name;
		boolean hasErrors = false;
		
		try {
			// Run date range specified
			Date runDate = CCLAUtils.getBinderDateValue(m_binder, "PSDF_RUN_DATE");
			Date runStartDate = CCLAUtils.getBinderDateValue(m_binder, "PSDF_RUN_START_DATE");
			boolean forceProcessing = CCLAUtils.getBinderBoolValue(m_binder, "FORCE_PROCESS");
			
			Log.debug("ForceProcessing :"+forceProcessing);
			
			if (runDate!=null && runStartDate!=null) {
				if (runDate.before(runStartDate)) {
					CCLAUtils.addQueryParamToBinder(m_binder, "ERROR_MESSAGE", TransactionGlobals.ERROR_MSG_SPECIFIED_RUN_DATE_BEFORE_START_DATE);
					CCLAUtils.addQueryIntParamToBinder(m_binder, "AllowRestart", 1);
					return;
				}
			}
			
			Log.debug("beginning runPreEODChecks");
			String fundCode = TransactionGlobals.PSDF_FUND_CODE;

			//Check to see if any FundEOD is in progress
			boolean alreadyInProgress = 
				checkIfFundEODInProgress(
						fundCode, 
						TransactionGlobals.FUND_EOD_STATUS_IN_PROGRESS_ID, 
						facade);

			if (alreadyInProgress) {
				CCLAUtils.addQueryParamToBinder(m_binder, "ERROR_MESSAGE", TransactionGlobals.ERROR_MSG_NEED_RESTART_EOD);
				CCLAUtils.addQueryIntParamToBinder(m_binder, "AllowRestart", 1);
				return;
			}
			
			//facade.beginTransaction();


			// check to see number of days since last end of day was run (use
			// run date)
			FundEOD lastCompletedEOD = FundEOD.getLastCompleted(fundCode, facade); 
			
			if ((lastCompletedEOD != null && lastCompletedEOD.getRunDate() != null)) 
			{
				//If the date range is specified, need to validate the ranges
				if (runDate!=null && runStartDate!=null) {
					
					int startDayDiff = DateUtils.differenceInDays(runStartDate, lastCompletedEOD.getRunDate());
					int endDayDiff = DateUtils.differenceInDays(runDate, new Date());
					
					Log.debug("Specified RunDate:"+runDate+", RunStartDate:"+runStartDate+", lastRunDate:"+lastCompletedEOD.getRunDate());
					Log.debug("-- startDayDiff:"+startDayDiff+", endDayDiff:"+endDayDiff);	
					
					if (startDayDiff!=1) {
						if (startDayDiff<1) {
							
							CCLAUtils.addQueryParamToBinder(m_binder, "ERROR_MESSAGE", TransactionGlobals.ERROR_MSG_SPECIFIED_START_DATE_OVERLAP);
						} else {
							CCLAUtils.addQueryParamToBinder(m_binder, "ERROR_MESSAGE", TransactionGlobals.ERROR_MSG_SPECIFIED_START_DATE_GAP);
						}
						CCLAUtils.addQueryIntParamToBinder(m_binder, "AllowRestart", 1);	
						Log.debug("Error with Start Date "+CCLAUtils.getBinderStringValue(m_binder, "ERROR_MESSAGE"));
						return;
					} 
					
					if (endDayDiff>0) {
						CCLAUtils.addQueryParamToBinder(m_binder, "ERROR_MESSAGE", TransactionGlobals.ERROR_MSG_SPECIFIED_RUN_DATE_IN_FUTURE);
						CCLAUtils.addQueryIntParamToBinder(m_binder, "AllowRestart", 1);
						Log.debug("Error with End Date "+CCLAUtils.getBinderStringValue(m_binder, "ERROR_MESSAGE"));

						return;
					} 		
					
					FundEOD completedEOD = FundEOD.getByStatusRunDate(fundCode,
							runDate,
							TransactionGlobals.FUND_EOD_STATUS_COMPLETE_ID,
							facade);
					
					if (completedEOD != null) {
						Log.error("Found completed EOD for "+completedEOD.getRunDate());
						m_binder.putLocal("ERROR_MESSAGE",
								TransactionGlobals.ERROR_MSG_CANNOT_START_COMPLETED_EOD);
						CCLAUtils.addQueryIntParamToBinder(m_binder, "AllowRestart", 0);	
						return;
					}
					
					
					FundEOD specifiedEOD = FundEOD.getByStatusRunDate(fundCode,
							runDate,
							TransactionGlobals.FUND_EOD_STATUS_IN_PROGRESS_ID,
							facade);

					if (specifiedEOD == null) {
						// create an end of day object with current date
						specifiedEOD = FundEOD
								.add(fundCode,
										null,
										TransactionGlobals.FUND_EOD_STATUS_IN_PROGRESS_ID,
										null, null, null, null, null, username,
										runDate, new Date(), runStartDate,
										username, facade);
						
						CCLAUtils.addQueryIntParamToBinder(m_binder, "runWithDateRange", 1);
						CCLAUtils.addQueryDateParamToBinder(m_binder, "START_DATE", runStartDate);
						CCLAUtils.addQueryDateParamToBinder(m_binder, "END_DATE", runDate);
						//OK Carry on.
						
					} else {
						Log.debug("end of day already found for date:"+new Date());
						CCLAUtils.addQueryParamToBinder(m_binder, "ERROR_MESSAGE", TransactionGlobals.ERROR_MSG_NEED_RESTART_EOD);
						CCLAUtils.addQueryDateParamToBinder(m_binder, "END_DATE", runDate);
						CCLAUtils.addQueryIntParamToBinder(m_binder, "AllowRestart", 1);
						return;
					}
				} else {
				
					int diffDays = DateUtils.differenceInDays(new Date(), lastCompletedEOD.getRunDate());
	
					if (diffDays > 1) {
						// in this case need to run an end of day effective from
						// yesterday
						Log.debug("Difference in days from last run is " + diffDays);
						// create an end of day object with previous date and add
						// START_DATE and END_DATE values to binder
	
						// check to see if End of day in progress for this date
						createPreviousEndOfDay(lastCompletedEOD, fundCode,
								username, m_binder, facade);
	
					} else {
						
						// check to see if an EOD has been completed for today's date
						// already
						// if so then return error to screen
						FundEOD completedEOD = FundEOD.getByStatusDate(fundCode, new Date(),
								TransactionGlobals.FUND_EOD_STATUS_COMPLETE_ID, facade);

						if (completedEOD != null) {
							Log.error("Found completed EOD for "+completedEOD.getRunDate());
							m_binder.putLocal("ERROR_MESSAGE",
									TransactionGlobals.ERROR_MSG_CANNOT_START_COMPLETED_EOD);
							CCLAUtils.addQueryIntParamToBinder(m_binder, "AllowRestart", 0);
							return;
						}
						
						FundEOD todayEOD = FundEOD.getByStatusRunDate(fundCode,
								new Date(),
								TransactionGlobals.FUND_EOD_STATUS_IN_PROGRESS_ID,
								facade);
	
						if (todayEOD == null) {
							// create an end of day object with current date
							todayEOD = FundEOD
									.add(fundCode,
											null,
											TransactionGlobals.FUND_EOD_STATUS_IN_PROGRESS_ID,
											null, null, null, null, null, username,
											new Date(), new Date(), new Date(),
											username, facade);
						} else {
							Log.debug("already an EOD in progress for date:"+new Date());
							CCLAUtils.addQueryParamToBinder(m_binder, "ERROR_MESSAGE", TransactionGlobals.ERROR_MSG_NEED_RESTART_EOD);
							CCLAUtils.addQueryDateParamToBinder(m_binder, "END_DATE", todayEOD.getRunDate());
							CCLAUtils.addQueryIntParamToBinder(m_binder, "AllowRestart", 1);
							return;
						}
					}
				}
			}

			CCLAUtils.addQueryBooleanParamToBinder(m_binder, "FORCE_PROCESS", forceProcessing);
			
			//facade.commitTransaction();

		} catch (DataException de) {
			//facade.rollbackTransaction();
			throw new ServiceException(de);
		}
	}

	/**
	 * Calculates the total units/cash based on the unit price entered and
	 * transactions for the day, calculates the share class movements based on
	 * these transactions, and then generates total closing units/cash for each
	 * share class in the fund based on the movements, plus existing account
	 * values in the fund.
	 * 
	 * @throws ServiceException
	 * @throws DataException
	 */
	public void startEOD() throws ServiceException, DataException 
	{
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		String username = m_userData.m_name;	
		boolean forceProcessing = CCLAUtils.getBinderBoolValue(m_binder, "FORCE_PROCESS");
		boolean hasTransactions = false;
		
		try {
			Log.debug("beginning startEOD");
			facade.beginTransaction();
			String fundCode = TransactionGlobals.PSDF_FUND_CODE;

			FundEOD checkEOD = FundEOD.getLatestByStatus(fundCode,
					TransactionGlobals.FUND_EOD_STATUS_IN_PROGRESS_ID, facade);

			
			// check to see if an EOD has been completed for today's date
			// already
			// if so then return error to screen
			FundEOD completeEOD = FundEOD.getByStatusDate(fundCode, checkEOD.getRunDate(),
					TransactionGlobals.FUND_EOD_STATUS_COMPLETE_ID, facade);

			if (completeEOD != null) {
				m_binder.putLocal("ERROR_MESSAGE",
						TransactionGlobals.ERROR_MSG_CANNOT_START_COMPLETED_EOD);
			} else {
				// first get latest end of day object
				FundEOD latestEOD = FundEOD.getLatest(fundCode, facade);

				if (latestEOD.getEodStatusId() != TransactionGlobals.FUND_EOD_STATUS_IN_PROGRESS_ID) {
					m_binder.putLocal("ERROR_MESSAGE",
							TransactionGlobals.ERROR_MSG_NO_CURRENT_EOD);
					m_binder.putLocal("AllowRestart", "1");
				} else {
					// get if it's a previous end of day (ie is weekend or bank
					// holiday eod)
					Date runDate = latestEOD.getRunDate();
					CCLAUtils.addQueryDateParamToBinder(m_binder, "RUN_DATE", runDate);
					Log.debug("Got latest run date of " + runDate+" with EOD_ID:"+latestEOD.getEodId());
					boolean isSameDay = TransactionUtils.isSameDay(runDate, new Date());
					Log.debug("isSameDay:" + isSameDay+", forceProcessing:"+forceProcessing);	
					CCLAUtils.addQueryBooleanParamToBinder(m_binder, "isSameDay", isSameDay);
				
					// if it's not the same day then we don't need to process
					// transactions, update share classes
					// or get final unit values
					CCLAUtils.addQueryDateParamToBinder(m_binder, "START_DATE", latestEOD.getRunStartDate());
					CCLAUtils.addQueryDateParamToBinder(m_binder, "END_DATE", latestEOD.getRunDate());

					if (isSameDay || forceProcessing) {
						// get transactions that need processing and mark them
						// as being 'in process'
						Vector<Instruction> vTrans = getPSDFTransactions(latestEOD.getRunStartDate(), latestEOD.getRunDate(), facade);

						// query unit price for the day and create a fund price
						// applied object for it
						// -- CL, removed this, as already done from the enter prices screen 26/01/12
						//getUnitPrice(fundCode, username, facade);

						if (vTrans == null || vTrans.isEmpty()) {
							Log.info("No transactions found to process");

							if (TransactionGlobals.PSDF_IDENTIFY_SEED_FUNDERS_AND_EARLY_INVESTORS.equalsIgnoreCase("1")) {
								markSpecialCaseAccounts(fundCode, latestEOD, username, facade);
							}
							
						} else {
							hasTransactions = true;
							Log.debug("got " + vTrans.size() + " transactions ");
							
							// marking all transaction with Processing state.
							markPSDFTransactions(vTrans, username, facade);

							// Get buys and add these values
							processBuysAndSells(vTrans, username, latestEOD, facade);
							
							// Get transactions and apply these
							processTransfers(vTrans, username, latestEOD, facade);

							// get the final cash value of the accounts using
							// the account value table
							setClosingCashValues(username, latestEOD ,facade);

							// check that there are no instructions left in
							// 'being processed' status
							boolean allProcessed = hasProcessedTransactions(vTrans);
							// TODO decide what to do with this, atm just pass
							// to binder
							CCLAUtils.addQueryBooleanParamToBinder(m_binder, "allProcessed", allProcessed);

							if (TransactionGlobals.PSDF_IDENTIFY_SEED_FUNDERS_AND_EARLY_INVESTORS.equalsIgnoreCase("1")) {
								markSpecialCaseAccounts(fundCode, latestEOD, username, facade);
							}

							// update any share class groups for this fund
							// this value will be used by the
							// getFinalShareClasses method
							ShareClassGroup.updateShareClassGroupTotalCash(fundCode,latestEOD.getRunDate(),  username, facade);

							// get final share class for day from cash value
							// held in account value audit
							// and create share class movement objects for share
							// class changes
							getFinalShareClasses(username, latestEOD, facade);

						}
						/*
						 * this was removed 22/3/2011 as it was decided the nav
						 * will be passed from HiPort // set the NAV (net asset
						 * value) which is total final units multiplied by basic
						 * unit price setNav(fundCode, username, facade);
						 */
					} else {
						Log.debug("Not sameDay and not forcing Processing");
					}
					
					// work out final unit values for the fund audit object
					getFinalUnitValues(username, latestEOD, facade);
				}
			}

			facade.commitTransaction();

			CCLAUtils.addQueryBooleanParamToBinder(m_binder, "hasTransactions",
					hasTransactions);

		} catch (Exception de) {
			facade.rollbackTransaction();

			Log.error(de.getMessage(), de);
			throw new ServiceException(de);
		}
	}

	/**
	 * Gets all account value audits for present date and uses the cash values
	 * on them (for each account) in order to calculate final share class
	 * movements for the day (in separate class)
	 * 
	 * @throws ServiceException
	 * @throws DataException
	 * 
	 * @throws DataException
	 * 
	 */
	private void setClosingCashValues(String username, FundEOD fundEOD, FWFacade facade)
			throws ServiceException, DataException 
	{
		Log.debug("....Working out ClosingCashValues...Start");
		Date eodDate = fundEOD.getRunDate();
		// get the account value audit entries for this day
		// this should correspond to all possible share class movements for
		// today
		Vector<AccountValueAudit> accV = getAccountAuditByDate(eodDate,
				TransactionGlobals.PSDF_FUND_CODE, facade);

		// for each account need to calculate final share class
		for (AccountValueAudit ava : accV) {
			// get account
			int accountId = ava.getAccountId();
			Account account = Account.get(accountId, facade);
			if (account==null) {
				Log.error("Cannot find account with id:"+accountId);
				throw new DataException("Cannot find account with id:"+accountId);
			}
			
			// get closing cash units
			String closing = ava.getClosingUnits();
			BigDecimal closingUnits = new BigDecimal(closing);
			Log.debug("got closing units:" + closingUnits+" for AccountID:"+ava.getAccountId());
			// get today's unit price (always use BASIC price here)
			FundPriceApplied fpa = FundPriceApplied.getFundPriceAppliedByFundAndDate(
					TransactionGlobals.PSDF_FUND_CODE, fundEOD.getRunDate(), facade);

			int fundPriceId = fpa.getFundPriceId();
			//FundPrice fp = FundPrice.get(fundPriceId, facade);
			String price = FundPrice.getBasicPrice(fundPriceId, facade);
			BigDecimal unitPrice = new BigDecimal(price);
			unitPrice = unitPrice.divide(hundred,
					TransactionGlobals.decimalPlaces,
					TransactionGlobals.roundRule);
			// get closing cash value from closing unit values
			// simply units x unit price
			BigDecimal closingCash = unitPrice.multiply(closingUnits);
			ava.setClosingCash(closingCash.toString());
			ava.persist(facade, username);
		}
	}

	/**
	 * Gets all account value audits for present date and uses the cash values
	 * on them (for each account) to calculate final share class movements for
	 * the day
	 * 
	 * @throws ServiceException
	 * @throws DataException
	 * 
	 * @throws DataException
	 * 
	 */
	private void getFinalShareClasses(String username, FundEOD fundEOD, FWFacade facade)
			throws ServiceException, DataException {
		
		Date eodDate = fundEOD.getRunDate();

		// get the account value audit entries for this day
		// this should correspond to all possible share class movements for
		// today EXCEPT THOSE FOR SHARE CLASS GROUPS
		Vector<AccountValueAudit> accV = getAccountAuditByDate(eodDate,
				TransactionGlobals.PSDF_FUND_CODE, facade);

		// for each account need to calculate final share class
		for (AccountValueAudit ava : accV) {
			// get account
			int accountId = ava.getAccountId();
			Account account = Account.get(accountId, facade);

			// if this is a special case account (seed funder, early investor,
			// internal) then ignore this
			ElementAttributeApplied internalAcc = ElementAttributeApplied
					.get(accountId,
							ElementAttribute.AccountAttributes.INTERNAL_ACCOUNT,
							facade);
			// is account seed fund or early investor?
			Integer seedFunderElement = Integer
					.parseInt(TransactionGlobals.PSDF_SEED_FUND_ELEMENT_ATTRIBUTE_ID);
			Integer earlyInvestorElement = Integer
					.parseInt(TransactionGlobals.PSDF_EARLY_INVESTOR_ELEMENT_ATTRIBUTE_ID);
			ElementAttributeApplied seedFund = ElementAttributeApplied.get(
					accountId, seedFunderElement, facade);
			ElementAttributeApplied earlyInvestor = ElementAttributeApplied
					.get(accountId, earlyInvestorElement, facade);
			if (!((seedFund != null && seedFund.getStatus())
					|| (earlyInvestor != null && earlyInvestor.getStatus()) || (internalAcc != null && internalAcc
					.getStatus()))) {
				// get current share class
				String currentShareClassId = account.getShareClass();
				Log.debug("Got currentShareClassId of " + currentShareClassId);
				// get closing cash value
				String closingCash = ava.getClosingCash();
				Log.debug("got closing cash:" + closingCash);
				// get final share class
				// this will take into account any share class overrides on the
				// account
				Integer newShareClassId = ShareClassService
						.getExpectedShareClass(accountId, closingCash, facade);
				
				//Refactor the above line to use this below. i.e common logic.
				//Integer newShareClassId = ShareClassUtils.getExpectedShareClass(accountId, closingCash, facade);
				
				
				Log.debug("Got expected share class of " + newShareClassId);
				// add share class movement but only if non-null for new share
				// class
				if (newShareClassId != null)
					ShareClassMovement.add(accountId, eodDate,
							TransactionGlobals.MOVE_TYPE_NAME_APPLY,
							Integer.toString(newShareClassId),
							currentShareClassId, facade, username);
			} else {
				Log.debug("ACCOUNT IS SPECIAL CASE, NOT CREATING MOVEMENT");
			}
		}

		// CHECK SHARE CLASS GROUPS FOR ANY CHANGE IN OVERALL SHARE CLASS GROUP

	}

	/**
	 * Returns boolean indicating whether all transactions passed to it have
	 * status indicating they have been processed
	 * 
	 * @throws DataException
	 * 
	 */
	private boolean hasProcessedTransactions(Vector<Instruction> vTrans) {
		boolean allProcessed = false;
		for (Instruction instruction : vTrans) {
			int transactionStatusId = instruction.getStatus().getInstructionStatusId();
			if (transactionStatusId == 
				Integer.parseInt(TransactionGlobals.PROCESSED_INSTRUCTION_STATUS_ID)) {
				allProcessed = true;
			} else {
				allProcessed = false;
				break;
			}
		}
		return allProcessed;
	}

	/**
	 * Adds or subtracts buys and sells from opening balance (in cash). This
	 * closing balance (in cash) is added to the account_value_audit table for
	 * the day. There should only ever be one account_audit_Value table for each
	 * day for an account. Note: does not include transfers
	 * 
	 * @throws DataException
	 * 
	 */
	public void processBuysAndSells(Vector<Instruction> vTrans,
			String username, FundEOD fundEOD, FWFacade facade) throws DataException 
	{
		//Common conversion for all buy and sell transactions.
		String unitPrice = null;
		FundPriceApplied fpa = FundPriceApplied.getFundPriceAppliedByFundAndDate(
				TransactionGlobals.PSDF_FUND_CODE, fundEOD.getRunDate(), facade);
		
		int fundPriceId = fpa.getFundPriceId();
		int dealTypeId = fpa.getDealTypeId();
		unitPrice = FundPrice.getSourcePrice(fundPriceId, dealTypeId, facade);
		
		// value is always in pence
		BigDecimal unitPriceValue = new BigDecimal(unitPrice);
		
		// convert to pounds
		unitPriceValue = unitPriceValue.divide(hundred,
				TransactionGlobals.decimalPlaces,
				TransactionGlobals.roundRule);

		Log.debug("Processing BuyAndSells with fpaId:"+fpa.getFundPriceAppliedId()+", unitPriceValue: "+unitPriceValue);
		
		for (Instruction transaction : vTrans) {
			int transactionId = transaction.getInstructionId();
			boolean isBuy = Instruction.isBuy(transactionId, facade);
			boolean isSell = Instruction.isSell(transactionId, facade);
			// get if buy or sell
			if (isBuy || isSell) {
				// get account
				int instructionId = transaction.getInstructionId();
				
				Log.info("Starting to process buy/sell transaction:"+instructionId);
				
				//Get the SourceAccountID
				Integer accId = null;
				InstructionDataApplied accIdDataApplied = InstructionDataApplied.getDataApplied(
						instructionId, InstructionData.Fields.SOURCE_ACCOUNT_ID, facade);
				if (accIdDataApplied==null || accIdDataApplied.getDataFieldValue().isEmpty()) {
					Log.error("Cannot find source account Id for instructionId:"+instructionId);
					throw new DataException("Cannot find source account Id for instructionId:"+instructionId);
				}
				
				accId = accIdDataApplied.getDataFieldValue().getIntValue(); 
				
				Account account = Account.get(accId, facade);
				if (account==null) {
					Log.error("Cannot find account with accId:"+accId);
					throw new DataException("Cannot find account with accId:"+accId);
				}
				
				//Get the Cash Value
				InstructionDataApplied cashDataApplied = InstructionDataApplied.getDataApplied(
						instructionId, InstructionData.Fields.CASH, facade);
				
				if (cashDataApplied==null || cashDataApplied.getDataFieldValue().isEmpty()) {
					Log.warn("COULD NOT FIND CASH VALUE FOR INSTRUCTION WITH ID:"+instructionId);
				} else {
					BigDecimal cash =  cashDataApplied.getDataFieldValue().getBigDecimalValue();
					Log.debug("GOT CASH VALUE:" +cash+", for instructionId:"+instructionId);

					//Get the account value audit
					AccountValueAudit ava = AccountValueAudit.get(
							account.getAccountId(), fundEOD.getRunDate(), facade);

					if (ava==null) {
						
						Log.debug("AccountID:"+account.getAccountId()+", doesn't have an AccountValueAudit. Creating one..");
						// add new audit value and continue
						String accCash = Account.getAccountCash(account.getAccountId(), facade);
						String accUnits = Account.getAccountUnits(account.getAccountId(), facade);
						ava = AccountValueAudit.add(
								account.getAccountId(), fundEOD.getRunDate(), accCash,
								accUnits, accCash, accUnits, null, facade,
								username);
					}

					Log.debug("AccountID:"+account.getAccountId()+", AccountValueAuditId:"+ava.getAccountValueAuditId());
					
					// update closing cash with transaction value
					/*
					 * String closingCash = ava.getClosingCash();
					 * Log.debug("closing cash is " + closingCash); BigDecimal
					 * closing = new BigDecimal(closingCash); if (isBuy) closing
					 * = closing.add(cash); else if (isSell) closing =
					 * closing.subtract(cash); Log.debug("final cash is " +
					 * closing); ava.setClosingCash(closing.toString());
					 */
					// update closing units with units total
					String closingUnits = ava.getClosingUnits();
					BigDecimal closingU = new BigDecimal("0");
					
					if (closingUnits != null)
						closingU = new BigDecimal(closingUnits);
					
					Log.debug("closing units before calculation is " + closingU);
					// get no of units that cash buys
					BigDecimal transUnits = cash.divide(unitPriceValue,
							TransactionGlobals.decimalPlaces,
							TransactionGlobals.roundRule);
					Log.debug("transaction units is " + transUnits+" for InstructionId:"+instructionId);
					if (isBuy)
						closingU = closingU.add(transUnits);
					if (isSell)
						closingU = closingU.subtract(transUnits);
					
					Log.debug("closing units is " + closingU+" for accountId:"+ava.getAccountId());
					
					ava.setClosingUnits(closingU.toString());
					ava.persist(facade, username);

					transaction.setStatusById(
							Integer.parseInt(
									TransactionGlobals.PROCESSED_INSTRUCTION_STATUS_ID));
					transaction.persist(facade, username);
				}
			}
		}
	}

	/**
	 * Processing transfers (in UNITS). This closing balance (in units) is
	 * added/updated to the account_value_audit table for the day. There should
	 * only ever be one account_audit_Value table for each day for an account.
	 * Note: does not include buys and sells
	 * 
	 * @throws DataException
	 * @throws ServiceException
	 * 
	 */
	public void processTransfers(Vector<Instruction> vTrans, String username, FundEOD fundEOD,
			FWFacade facade) throws DataException, ServiceException 
	{
		//Common conversion for all transfer transactions.
		String unitPrice = null;
		FundPriceApplied fpa = FundPriceApplied.getFundPriceAppliedByFundAndDate(
				TransactionGlobals.PSDF_FUND_CODE, fundEOD.getRunDate(), facade);
		
		int fundPriceId = fpa.getFundPriceId();
		int dealTypeId = fpa.getDealTypeId();
		unitPrice = FundPrice.getSourcePrice(fundPriceId, dealTypeId, facade);
		
		// value is always in pence
		BigDecimal unitPriceValue = new BigDecimal(unitPrice);
		
		// convert to pounds
		unitPriceValue = unitPriceValue.divide(hundred,
				TransactionGlobals.decimalPlaces,
				TransactionGlobals.roundRule);

		Log.debug("Processing Transfer with fpaId:"+fpa.getFundPriceAppliedId()+", unitPriceValue: "+unitPriceValue);
		
		for (Instruction transaction : vTrans) {
			int transactionId = transaction.getInstructionId();

			boolean isTransfer = Instruction.isTransfer(transactionId, facade);
			// if transfer then proceed
			if (isTransfer) {
				// get instruction id
				int instructionId = transaction.getInstructionId();
				Log.info("Starting to process transfer with id:"+instructionId);

				///////////////////////////
				//Get the SourceAccountID
				Integer accId = null;
				InstructionDataApplied accIdDataApplied = InstructionDataApplied.getDataApplied(
						instructionId, InstructionData.Fields.SOURCE_ACCOUNT_ID, facade);
				if (accIdDataApplied==null || accIdDataApplied.getDataFieldValue().isEmpty()) {
					Log.error("Cannot find source account Id for instructionId:"+instructionId);
					throw new DataException("Cannot find source account Id for instructionId:"+instructionId);
				} 
				
				accId = accIdDataApplied.getDataFieldValue().getIntValue(); 
				
				Account sourceAccount = Account.get(accId, facade);
				if (sourceAccount==null) {
					Log.error("Cannot find source account with accId:"+accId);
					throw new DataException("Cannot find source account with accId:"+accId);
				}
				
				//////////////////////////////////////
				//Get DestinationAccountId
				Integer destAccId = null;
				InstructionDataApplied destAccIdDataApplied = InstructionDataApplied.getDataApplied(
						instructionId, InstructionData.Fields.DEST_ACCOUNT_ID, facade);
				if (destAccIdDataApplied==null || destAccIdDataApplied.getDataFieldValue().isEmpty()) {
					Log.error("Cannot find dest account Id for instructionId:"+instructionId);
					throw new DataException("Cannot find dest account Id for instructionId:"+instructionId);
				} 
				
				destAccId = destAccIdDataApplied.getDataFieldValue().getIntValue(); 
				
				Account destAccount = Account.get(destAccId, facade);
				if (destAccount==null) {
					Log.error("Cannot find dest account with accId:"+destAccId);
					throw new DataException("Cannot find dest account with accId:"+destAccId);
				}
								
				//Get the Units
				InstructionDataApplied unitsDataApplied = InstructionDataApplied.getDataApplied(
						instructionId, InstructionData.Fields.UNITS, facade);
				
				if (unitsDataApplied==null || unitsDataApplied.getDataFieldValue().isEmpty()) {
					Log.error("unable to processTransfer, Unit Value is null for instructionId:"+instructionId);
					throw new ServiceException("unable to processTransfer, Unit Value is null for instructionId:"+instructionId);
				}
				
				BigDecimal units =  unitsDataApplied.getDataFieldValue().getBigDecimalValue();
				
				if (units.compareTo(BigDecimal.ZERO) == 0) {
					Log.error("unable to processTransfer,  Unit Value is 0 for instructionId:"
							+instructionId);
					throw new ServiceException(
							"unable to processTransfer,  Unit Value is 0 for instructionId:"
									+ instructionId);
				}

				Log.debug("GOT units VALUE:" + units+" for instructionId:"+instructionId);
				
				AccountValueAudit ava = AccountValueAudit.get(
						sourceAccount.getAccountId(), fundEOD.getRunDate(), facade);
				
				if (ava==null) {
					// add new audit value and continue
					String accCash = Account.getAccountCash(sourceAccount.getAccountId(), facade);
					String accUnits = Account.getAccountUnits(sourceAccount.getAccountId(), facade);
					ava = AccountValueAudit
							.add(sourceAccount.getAccountId(), fundEOD.getRunDate(),
									accCash, accUnits, accCash, accUnits, null,
									facade, username);
				}
				
				AccountValueAudit destAva = AccountValueAudit.get(
						destAccount.getAccountId(), fundEOD.getRunDate(), facade);
				
				if (destAva==null) {
					// add new audit value and continue
					String accCash = Account.getAccountCash(destAccount.getAccountId(), facade);
					String accUnits = Account.getAccountUnits(destAccount.getAccountId(), facade);
					destAva = AccountValueAudit
							.add(destAccount.getAccountId(), fundEOD.getRunDate(),
									accCash, accUnits, accCash, accUnits, null,
									facade, username);
				}

				BigDecimal cashValue = unitPriceValue.multiply(units);
				String cashV = cashValue.toString();
				Log.info("Got cash value of "+cashV+" = ("+unitPriceValue+" * "+units+")");
				InstructionDataApplied.updateDataValueByName(instructionId,
						TransactionGlobals.CASH_VALUE, cashV, username, facade);

				// update closing units of source account by subtracting the
				// unit value
				Log.debug("sourceAva is " + ava.getAccountValueAuditId());
				String closingUnits = ava.getClosingUnits();
				Log.debug("source closing units is " + closingUnits);
				BigDecimal closing = new BigDecimal(closingUnits);
				closing = closing.subtract(units);
				Log.debug("source final units is " + units);
				ava.setClosingUnits(closing.toString());
				ava.persist(facade, username);

				// update closing units of destination account by adding the
				// unit value
				Log.debug("destAva is " + destAva.getAccountValueAuditId());
				String closingunitsD = destAva.getClosingUnits();
				Log.debug("dest closing cash is " + closingunitsD);
				BigDecimal closingD = new BigDecimal("0");

				if (closingunitsD != null)
					closingD = new BigDecimal(closingunitsD);

				closingD = closingD.add(units);
				Log.debug("dest final units is " + closingD);
				destAva.setClosingUnits(closingD.toString());
				destAva.persist(facade, username);

				// Mark transaction as processed
				transaction.setStatusById(
						Integer.parseInt(
								TransactionGlobals.PROCESSED_INSTRUCTION_STATUS_ID));
				transaction.persist(facade, username);
			}
		}
	}

	/**
	 * updates a vector of Transaction objects for PSDF fund with status of in
	 * process
	 * 
	 * @return Calendar - report/rollover date
	 * @throws ServiceException
	 * @throws DataException
	 * @throws DataException
	 *             , ServiceException
	 */
	public void markPSDFTransactions(Vector<Instruction> vTrans,
			String username, FWFacade facade) throws ServiceException,
			DataException {
		String inProcessStatusId = TransactionGlobals.IN_PROCESS_INSTRUCTION_STATUS_ID;
		int newStatusId = Integer.parseInt(inProcessStatusId);
		Instruction.updateBulkInstructionStatus(vTrans, newStatusId, username,
				facade);
	}

	/**
	 * Gets a vector of Instruction objects for PSDF fund with passed status
	 * with process date before or after passed date
	 * 
	 * @return Calendar - report/rollover date
	 * @throws ServiceException
	 * @throws DataException
	 * @throws DataException
	 *             , ServiceException
	 */
	public Vector<Instruction> getPSDFTransactions(Date startDate, Date endDate, FWFacade facade)
			throws ServiceException, DataException {
		String fundCode = TransactionGlobals.PSDF_FUND_CODE;
		int statusId = Integer.parseInt(TransactionGlobals.PROCESS_INSTRUCTION_STATUS_ID);
		
		
		Date rolloverDate = getRolloverDate(endDate).getTime();
		Log.debug("Got time of :" + rolloverDate);
		Vector<Instruction> vTrans = Instruction.getTransactionsToProcess(
				rolloverDate, statusId, fundCode, facade);
		return vTrans;
	}

	/**
	 * Gets the rollover/report date for today. This is made up of today's date
	 * with the time taken from the component configuration file.
	 * 
	 * @return Calendar - report/rollover date
	 * @throws DataException
	 *             , ServiceException
	 */
	public static Calendar getRolloverDate(Date runDate) throws ServiceException {
		// Get today's date

		Calendar reportDate = Calendar.getInstance();
		
		if (runDate!=null) 
			reportDate.setTime(runDate); 
	
		// get rollover time (in date format)
		DateFormat sdf = new SimpleDateFormat("hh:mm:ss");
		String rolloverTime = TransactionGlobals.TRANSACTION_ROLLOVER_TIME;
		try {
			Date rolloverDate = sdf.parse(rolloverTime);
			Calendar rolloverCal = Calendar.getInstance();
			rolloverCal.setTime(rolloverDate);
			reportDate.set(Calendar.HOUR_OF_DAY,
					rolloverCal.get(Calendar.HOUR_OF_DAY));
			reportDate.set(Calendar.MINUTE, rolloverCal.get(Calendar.MINUTE));
			reportDate.set(Calendar.SECOND, rolloverCal.get(Calendar.SECOND));
	
			Log.info("USING PROCESS DATE OF " + reportDate.getTime());
			
			return reportDate;

		} catch (ParseException e) {
			String msg = "Cannot calculate process rollover date/time";
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}

	/**
	 * Gets a vector of the AccountValueAudits for a particular day
	 * 
	 * @return Vector
	 * @param date
	 *            - date of AccountValueAudit, this will cover the full 24hr
	 *            period
	 * @throws DataException
	 */
	public static Vector<AccountValueAudit> getAccountAuditByDate(Date date,
			String fundCode, FWFacade facade) throws DataException {
		Vector<AccountValueAudit> accVector = new Vector<AccountValueAudit>();
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryDateParamToBinder(binder, "ACCOUNT_VALUE_DATE", date);
		CCLAUtils.addQueryParamToBinder(binder, "FUND_CODE", fundCode);
		DataResultSet rsAccountValue = facade.createResultSet(
				"qTransactions_GetAccountAuditByDate", binder);
		if (rsAccountValue.isEmpty())
			return null;
		else {
			do {
				AccountValueAudit ava = AccountValueAudit.get(rsAccountValue);
				accVector.add(ava);
			} while (rsAccountValue.next());
			return accVector;
		}
	}

	/**
	 * Adds to binder DataResult sets of a) share classes, b) share class one of
	 * expenses, c) general expenses (all for fund)
	 * 
	 * @return No return, but adds DataResultSets to binder -
	 *         rsShareClassExpenses, rsShareClasses, rsGeneralExpenses
	 * @throws DataException
	 *             , ServiceException
	 */
	public void confirmIncomeExpense() throws DataException, ServiceException {
		// need to get share classes for fund

		// get one-off share class expenses (if set)

		// get fund specific expenses (plus previous values if persisted)

		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);

		String fundCode = TransactionGlobals.PSDF_FUND_CODE;

		// First check to see if an EOD has been completed for today's date
		// already
		// if so then return error to screen
		FundEOD completeEOD = FundEOD.getByStatusDate(fundCode, new Date(),
				TransactionGlobals.FUND_EOD_STATUS_COMPLETE_ID, facade);

		if (completeEOD != null) {
			m_binder.putLocal("ERROR_MESSAGE",
					TransactionGlobals.ERROR_MSG_CANNOT_START_COMPLETED_EOD);
		} else {

			// first get latest end of day object
			FundEOD latestEOD = FundEOD.getLatest(fundCode, facade);

			if (latestEOD.getEodStatusId() != TransactionGlobals.FUND_EOD_STATUS_IN_PROGRESS_ID) {
				m_binder.putLocal("ERROR_MESSAGE",
						TransactionGlobals.ERROR_MSG_NO_CURRENT_EOD);
				m_binder.putLocal("allowRestartEOD", "1");

			} else {
				// get if it's a previous end of day (ie is weekend or bank
				// holiday eod)
				Date runDate = latestEOD.getRunDate();
				CCLAUtils.addQueryDateParamToBinder(m_binder, "RUN_DATE", runDate);
				Log.debug("got latest run date of " + runDate);
				boolean isSameDay = TransactionUtils.isSameDay(runDate,
						new Date());
				Log.debug("isSameDay is " + isSameDay);
				CCLAUtils.addQueryBooleanParamToBinder(m_binder, "isSameDay",
						isSameDay);
				CCLAUtils.addQueryDateParamToBinder(m_binder, "START_DATE",
						latestEOD.getRunStartDate());
				CCLAUtils.addQueryDateParamToBinder(m_binder, "END_DATE",
						latestEOD.getRunDate());

				// get share classes for fund
				DataResultSet rsShareClasses = TransactionUtils
						.getEnabledShareClassesByFundOrdered(fundCode, facade);
				if (rsShareClasses != null)
					m_binder.addResultSet("rsShareClasses", rsShareClasses);

				// get one-off share class expenses
				DataResultSet rsShareClassExpenses = ShareClass
						.getShareClassExpensesByFund(fundCode,
								TransactionGlobals.SHARE_CLASS_EXPENSE_TYPE_ID,
								facade);
				if (rsShareClassExpenses != null)
					m_binder.addResultSet("rsShareClassExpenses",
							rsShareClassExpenses);

				// get general expenses
				DataResultSet rsGeneralExpenses = IncomeExpense
						.getExpenseByTypeFund(
								TransactionGlobals.GENERAL_EXPENSE_TYPE_ID,
								fundCode, facade);
				if (rsGeneralExpenses != null)
					m_binder.addResultSet("rsGeneralExpenses",
							rsGeneralExpenses);
			}

		}

	}

	/**
	 * Prepares totals for end of day calculations, updating
	 * income_expense_applied and created an FundEOD object
	 * 
	 * @throws DataException
	 * 
	 * @throws DataException
	 *             , ServiceException
	 * @throws ServiceException
	 */
	public void prepareEndOfDay() throws DataException, ServiceException {
		FWFacade facade =CCLAUtils.getFacade(m_workspace, true);
		String username = m_userData.m_name;
		Log.debug("starting prepareEndOfDay");
		try {
			facade.beginTransaction();
			String fundCode = TransactionGlobals.PSDF_FUND_CODE;
			// Date reportDate = getRolloverDate().getTime();
			FundEOD latestEOD = FundEOD.getLatestByStatus(fundCode,
					TransactionGlobals.FUND_EOD_STATUS_IN_PROGRESS_ID, facade);
			Date eodDate = latestEOD.getRunDate();
			
			
			FundEOD lastCompletedEOD = FundEOD.getLastCompleted(fundCode,
					facade);

			// First check to see if an EOD has been completed for today's date
			// already
			// if so then return error to screen
			FundEOD completeEOD = FundEOD.getByStatusDate(fundCode, eodDate,
					TransactionGlobals.FUND_EOD_STATUS_COMPLETE_ID, facade);

			if (completeEOD != null) {
				m_binder.putLocal("ERROR_MESSAGE",
						TransactionGlobals.ERROR_MSG_CANNOT_START_COMPLETED_EOD);
			} else {
				
				Log.debug("Running prepareEOD for FundEOD with id "+latestEOD.getEodId());
				// the total share class expenses are needed by hiport
				BigDecimal totalShareClassExpense = new BigDecimal("0");
				BigDecimal totalNetIncomeDistribution = new BigDecimal("0");
				BigDecimal totalCashExpense = new BigDecimal("0");
				BigDecimal totalRateExpense = new BigDecimal("0");

				// work out final unit values
				getFinalUnitValues(username, latestEOD, facade);

				// create an entry in INCOME_EXPENSE_APPLIED for all relevant
				// one-off share class expenses
				// create an entry in INCOME_EXPENSE_APPLIED for general
				// expenses
				DataResultSet rsExp = IncomeExpense.getExpenseByFund(fundCode,
						facade);
				Vector<IncomeExpenseApplied> iea = null;
				if (!rsExp.isEmpty()) {
					Vector<IncomeExpense> ie = IncomeExpense.getIncomeExpense(
							IncomeExpense.getExpenseByFund(fundCode, facade),
							facade);
					iea = createIncomeExpenseApplied(ie, eodDate, username,
							facade);
				}
				// get fund totals and return these
				DataResultSet rsFundAudit = FundAudit.getFundAuditByDateRS(
						fundCode, eodDate, facade);
				m_binder.addResultSet("rsFundAudit", rsFundAudit);

				// get total of all units (from Fund_Audit table)
				String strTotalUnits = FundAudit.getTotalClosingUnits(fundCode,
						eodDate, facade);
				CCLAUtils.addQueryParamToBinder(m_binder, "TotalUnits",
						strTotalUnits);
				Log.debug("got TotalUnits:" + strTotalUnits);
				BigDecimal TotalUnits = new BigDecimal(strTotalUnits);

				// get total fund expenses
				String totalFundExpenses = IncomeExpenseApplied
						.getTotalExpenseAppliedByDate(
								TransactionGlobals.GENERAL_EXPENSE_TYPE_ID,
								eodDate, fundCode, facade);
				Log.debug("totalfundexpenses is " + totalFundExpenses);
				CCLAUtils.addQueryParamToBinder(m_binder, "FUND_EXPENSE_TOTAL",
						totalFundExpenses);
				BigDecimal fundExpenses = new BigDecimal(totalFundExpenses);
				Log.debug("got total fund expense of " + fundExpenses);

				// get daily net income - this is net of fund expenses
				String totalFundIncome = IncomeExpenseApplied
						.getTotalIncomeAppliedByDate(
								TransactionGlobals.FUND_INCOME_TYPE_ID,
								eodDate, fundCode, facade);
				BigDecimal fundIncome = new BigDecimal(totalFundIncome);
				CCLAUtils.addQueryParamToBinder(m_binder, "FUND_INCOME_TOTAL",
						totalFundIncome);
				Log.debug("got total fund income of " + fundIncome);

				// get daily gross income - this is net of fund expenses
				String totalGrossIncome = IncomeExpenseApplied
						.getTotalIncomeAppliedByDate(
								TransactionGlobals.GROSS_INCOME_TYPE_ID,
								eodDate, fundCode, facade);
				BigDecimal grossIncome = new BigDecimal(totalGrossIncome);
				CCLAUtils.addQueryParamToBinder(m_binder, "GROSS_INCOME_TOTAL",
						totalGrossIncome);
				Log.debug("got total gross income of " + grossIncome);
				CCLAUtils.addQueryParamToBinder(m_binder, "GROSS_INCOME_TOTAL",
						totalGrossIncome);

				// get daily net asset value of fund (NAV)
				FundNavAudit fna = FundNavAudit.get(fundCode, eodDate, facade);
				if (fna == null)
					throw new ServiceException(
							"Missing today's Net Asset Value for the fund.  Cannot continue.");

				BigDecimal nav = new BigDecimal(fna.getNav());
				CCLAUtils.addQueryParamToBinder(m_binder, "FUND_NAV",
						fna.getNav());
				Log.debug("got nav of " + fna.getNav());

				// take expenses away from income
				// this does not include share class costs
				BigDecimal netFundIncome = fundIncome.subtract(fundExpenses);
				Log.debug("net fund income after fund expenses but before other expenses is "
						+ netFundIncome);
				CCLAUtils.addQueryParamToBinder(m_binder, "FUND_INCOME_NET",
						TransactionUtils.getFormattedCurrency(netFundIncome
								.toString()));

				
				//
				//Common conversion for all buy and sell transactions.
				String priceValue = null;
				FundPriceApplied fpa = FundPriceApplied.getFundPriceAppliedByFundAndDate(
						TransactionGlobals.PSDF_FUND_CODE, latestEOD.getRunDate(), facade);
				
				int fundPriceId = fpa.getFundPriceId();
				//int dealTypeId = fpa.getDealTypeId();
				priceValue = FundPrice.getBasicPrice(fundPriceId, facade);
				
				// value is always in pence
				BigDecimal unitPrice = new BigDecimal(priceValue);
				
				// convert to pounds
				unitPrice = unitPrice.divide(hundred,
						TransactionGlobals.decimalPlaces,
						TransactionGlobals.roundRule);
				
				CCLAUtils.addQueryParamToBinder(m_binder, "UNIT_PRICE",
						unitPrice.toString());

				DataResultSet rsFPA = FundPriceApplied
						.getFundPriceAppliedDetail(fpa.getFundPriceAppliedId(),
								facade);
				if (rsFPA != null)
					m_binder.addResultSet("rsFundPriceApplied", rsFPA);

				// share class unit allocation
				// this is the percentage of shares in each class
				// create dataresultset to hold these
				// creat dataresultset to hold the account data
				String[] shareClassCols = new String[] { "SHARE_CLASS_NAME",
						"PERCENTAGE", "TOTAL_BEFORE_COSTS", "MANAGEMENT_FEE",
						"SHARE_CLASS_EXPENSE", "INCOME_DISTRIBUTION",
						"NET_INCOME_PER_UNIT", "YIELD", "FUND_YIELD",
						"GROSS_YIELD", "CASH_VALUE", "CLOSING_UNITS" };

				DataResultSet rsShareClassDetails = new DataResultSet(
						shareClassCols);

				// calculate days since last complete run
				// this will be used for calculating expenses and yields
				int daysSinceLastRun = 1;

				if (lastCompletedEOD != null
						&& lastCompletedEOD.getRunDate() != null) {
					int diffDays = DateUtils.differenceInDays(eodDate,
							lastCompletedEOD.getRunDate());

					if (diffDays > 0) {
						daysSinceLastRun = diffDays;
						Log.debug("Difference in days from last run is "
								+ diffDays);
					} else
						Log.debug("Difference between LastRun is negative!, only calculating expenses for 1 day.");
				}

				do {
					// vector to hold resultset row
					Vector<Object> scV = new Vector<Object>();

					FundAudit fa = FundAudit.get(rsFundAudit);
					Log.debug("for share class with id " + fa.getShareClassId());
					// get share class and name
					ShareClass sc = ShareClass
							.get(fa.getShareClassId(), facade);

					// is share class enabled?
					if (sc.isEnabled()) {
						scV.add(sc.getShareClassName());
						// get closing units
						String strClosingUnits = rsFundAudit
								.getStringValueByName("CLOSING_UNITS");
						if (strClosingUnits != null
								&& !strClosingUnits.equalsIgnoreCase("")) {
							BigDecimal closingUnits = new BigDecimal(
									strClosingUnits);
							Log.debug("got closing units:" + closingUnits);
							BigDecimal proportionUnits = new BigDecimal("0");
							// if no closing units then proportion/percentage
							// always zero
							if (closingUnits.compareTo(BigDecimal.ZERO)!=0)
								proportionUnits = closingUnits.divide(
										TotalUnits,
										TransactionGlobals.decimalPlaces,
										TransactionGlobals.roundRule);
							BigDecimal percentageUnits = proportionUnits
									.multiply(hundred);

							Log.debug("got percentage: " + percentageUnits);
							scV.add(percentageUnits.toString());

							// TOTAL BEFORE COSTS this is the net fund income
							// multiplied by proportion
							BigDecimal totalBeforeCosts = proportionUnits
									.multiply(netFundIncome);

							scV.add(totalBeforeCosts.toString());
							Log.debug("got total_before_costs of "
									+ totalBeforeCosts);
							BigDecimal managementFee = new BigDecimal("0");

							if (proportionUnits.compareTo(BigDecimal.ZERO)!=0) {
								// this is the sum of the external percentage
								// expenses for share class multiplied by nav
								String shareClassRate = IncomeExpenseApplied
										.getTotalRateShareClassExpenseAppliedByDate(
												TransactionGlobals.SHARE_CLASS_EXPENSE_TYPE_ID,
												sc.getShareClassId(), eodDate,
												facade);
								if (shareClassRate == null
										|| shareClassRate == "")
									shareClassRate = "0";
								Log.debug("got share class rate of "
										+ shareClassRate);
								BigDecimal shareClassRateExpense = new BigDecimal(
										shareClassRate);
								if (shareClassRateExpense.compareTo(BigDecimal.ZERO)!=0)
									shareClassRateExpense = shareClassRateExpense
											.divide(hundred,
													TransactionGlobals.decimalPlaces,
													TransactionGlobals.roundRule);
								Log.debug("got share class rate expense:"
										+ shareClassRate);

								if (shareClassRateExpense.compareTo(BigDecimal.ZERO)!=0)
									managementFee = shareClassRateExpense
											.multiply(nav)
											.setScale(
													TransactionGlobals.decimalPlaces,
													TransactionGlobals.roundRule);
								// This fee should be multiplied by the
								// proportion of shares in this class
								Log.debug("got management fee before proportion of "
										+ managementFee);
								Log.debug("got proportion of "
										+ proportionUnits);
								if (managementFee.compareTo(BigDecimal.ZERO)!=0) {
									managementFee = managementFee.multiply(
											proportionUnits).setScale(
											TransactionGlobals.decimalPlaces,
											TransactionGlobals.roundRule);
									Log.debug("got management fee of "
											+ managementFee);
									managementFee = managementFee.divide(
											BigDecimal.valueOf(365),
											TransactionGlobals.decimalPlaces,
											TransactionGlobals.roundRule);
									Log.debug("got daily management fee of "
											+ managementFee);
								}

								managementFee = managementFee
										.multiply(BigDecimal
												.valueOf(daysSinceLastRun));
								Log.debug("got no. of days for expenses "
										+ daysSinceLastRun);
								Log.debug("got total management fee for this run  "
										+ managementFee);
							} else {
								Log.debug("proportion of Units is zero, no expenses calculated");
							}
							scV.add(managementFee);

							// add this to total share class expense (needed by
							// HiPort)
							totalShareClassExpense = totalShareClassExpense
									.add(managementFee);
							totalRateExpense = totalRateExpense
									.add(managementFee);

							// get ONE OFF SHARE CLASS COSTs for share class for
							// this day ( cash value expenses)
							String shareClassCost = IncomeExpenseApplied
									.getTotalCashShareClassExpenseAppliedByDate(
											TransactionGlobals.SHARE_CLASS_EXPENSE_TYPE_ID,
											sc.getShareClassId(), eodDate,
											facade);
							if (StringUtils.stringIsBlank(shareClassCost)
									|| closingUnits.compareTo(BigDecimal.ZERO)==0)
								shareClassCost = "0";

							BigDecimal shareClassExpense = new BigDecimal(
									shareClassCost);

							if (shareClassExpense.compareTo(BigDecimal.ZERO)!=0)
								shareClassExpense = shareClassExpense
										.multiply(BigDecimal
												.valueOf(daysSinceLastRun));
							Log.debug("got share class expense:"
									+ shareClassExpense);
							scV.add(shareClassExpense.toString());
							// add this to total share class expense (needed by
							// HiPort)
							totalShareClassExpense = totalShareClassExpense
									.add(shareClassExpense);
							totalCashExpense = totalCashExpense
									.add(shareClassExpense);

							// get net income distribution which is TOTAL BEFORE
							// COSTS minus the MANAGEMENT FEE
							// minus the ONE OFF SHARE CLASS COST
							BigDecimal netIncomeDistribution = new BigDecimal(
									"0");

							// gross income distribution is the TOTAL BEFORE ALL
							// COSTS multiplied by proportion of units
							BigDecimal grossIncomeDistribution = proportionUnits
									.multiply(grossIncome);

							// fund income distribution is the TOTAL BEFORE
							// COSTS but including the fund expenses
							// (not the share class expenses)
							BigDecimal fundIncomeDistribution = totalBeforeCosts;

							if (closingUnits.intValue() != 0) {
								netIncomeDistribution = totalBeforeCosts
										.subtract(managementFee).subtract(
												shareClassExpense);
							}
							Log.debug("dividend or net income distribution is "
									+ netIncomeDistribution);
							scV.add(netIncomeDistribution.toString());

							totalNetIncomeDistribution = totalNetIncomeDistribution
									.add(netIncomeDistribution);

							// total share class costs is management fee (rate
							// expenses) plus cash expenses
							BigDecimal totalShareClassCosts = managementFee
									.add(shareClassExpense);
							// save to fund audit
							fa.setTotalExpense(totalShareClassCosts.toString());

							// NET INCOME PER UNIT which is the
							// netIncomeDistribution divided by the total number
							// of units
							// GROSS INCOME PER UNIT is the
							// grossIncomeDistrubtion divided by total number of
							// units
							// FUND INCOME PER UNIT is the
							// fundIncomeDistribution divided by total number of
							// units.
							// if closing units is zero then this figure will be
							// zero
							BigDecimal netIncomePerUnit = new BigDecimal("0");
							BigDecimal grossIncomePerUnit = new BigDecimal("0");
							BigDecimal fundIncomePerUnit = new BigDecimal("0");
							if (closingUnits.compareTo(BigDecimal.ZERO)!=0) {
								netIncomePerUnit = netIncomeDistribution
										.divide(closingUnits,
												TransactionGlobals.decimalPlaces,
												TransactionGlobals.roundRule);
								fundIncomePerUnit = fundIncomeDistribution
										.divide(closingUnits,
												TransactionGlobals.decimalPlaces,
												TransactionGlobals.roundRule);
								grossIncomePerUnit = grossIncomeDistribution
										.divide(closingUnits,
												TransactionGlobals.decimalPlaces,
												TransactionGlobals.roundRule);
							}
							// income per unit is in pence
							netIncomePerUnit = netIncomePerUnit
									.multiply(hundred);
							grossIncomePerUnit = grossIncomePerUnit
									.multiply(hundred);
							fundIncomePerUnit = fundIncomePerUnit
									.multiply(hundred);
							scV.add(netIncomePerUnit);
							Log.debug("got net income per unit (p) of "
									+ netIncomePerUnit);
							Log.debug("got fund income per unit (p) of "
									+ fundIncomePerUnit);
							Log.debug("got gross income per unit (p) of "
									+ grossIncomePerUnit);

							// NET YIELD is netincomePerUnit multiplied by 365
							// divided by no of days since last run
							// same principle for other yields
							BigDecimal daysSinceLastEOD = new BigDecimal(
									daysSinceLastRun);
							Log.debug("daysSinceLastEOD is " + daysSinceLastEOD);
							BigDecimal yield = netIncomePerUnit.multiply(new BigDecimal("365"));
							Log.debug("net yield before day calculation is " + yield);
							yield = yield.divide(daysSinceLastEOD,
										TransactionGlobals.decimalPlaces,
										TransactionGlobals.roundRule);
							Log.debug("net yield is " + yield);
							BigDecimal fundYield = fundIncomePerUnit.multiply(new BigDecimal("365"));
							
							fundYield = fundYield.divide(daysSinceLastEOD,
										TransactionGlobals.decimalPlaces,
										TransactionGlobals.roundRule);
							Log.debug("fund yield is " + fundYield);
							// calculate gross yield
							BigDecimal grossYield = grossIncomePerUnit
									.multiply(new BigDecimal("365"));
							
							grossYield = grossYield.divide(
										daysSinceLastEOD,
										TransactionGlobals.decimalPlaces,
										TransactionGlobals.roundRule);
							Log.debug("gross yield is " + grossYield);

							// save this back to the fund audit object
							// save to fund audit
							fa.setTotalExpense(
									TransactionUtils.getPSDFDecimalFormat().format(
											totalShareClassCosts.doubleValue()));
							fa.setNetYield(
									TransactionUtils.getPSDFDecimalFormat().format(
											yield.doubleValue()));
							fa.setFundYield(
									TransactionUtils.getPSDFDecimalFormat().format(
											fundYield.doubleValue()));
							fa.setGrossYield(
									TransactionUtils.getPSDFDecimalFormat().format(
											grossYield.doubleValue()));
							fa.setIncomePerUnit(
									TransactionUtils.getPSDFDecimalFormat().format(
											netIncomePerUnit.doubleValue()));
							fa.persist(facade, username);

							scV.add(yield);
							scV.add(fundYield);
							scV.add(grossYield);

							// use unit price value to work out closing cash
							// value
							scV.add(unitPrice.multiply(closingUnits));

							// add the closing units here to make layout easier
							scV.add(fa.getClosingUnits());
						} else {
							scV.add("0");
							scV.add("0");
							scV.add("0");
							scV.add("0");
							scV.add("0");
							scV.add("0");
							scV.add("0");
						}

						rsShareClassDetails.addRow(scV);
					}

				} while (rsFundAudit.next());
				// add this to binder
				m_binder.addResultSet("rsShareClassDetails",
						rsShareClassDetails);

				// update the FundEOD object
				latestEOD.setFundPriceAppliedId(fpa.getFundPriceAppliedId());
				latestEOD.setFundNavId(fna.getFundNavId());
				
				latestEOD.setTotalShareClassExpense(
						TransactionUtils.getPSDFDecimalFormat().format(
								totalShareClassExpense.doubleValue()));
				latestEOD.setTotalCashExpense(
						TransactionUtils.getPSDFDecimalFormat().format(
								totalCashExpense.doubleValue()));
				latestEOD.setTotalRateExpense(
						TransactionUtils.getPSDFDecimalFormat().format(
								totalRateExpense.doubleValue()));
				latestEOD.setTotalNetIncomeDistribution(
						TransactionUtils.getPSDFDecimalFormat().format(
								totalNetIncomeDistribution.doubleValue()));
				
				latestEOD.persist(facade, username);

				boolean isSameDay = TransactionUtils.isSameDay(
						latestEOD.getRunDate(), new Date());
				CCLAUtils.addQueryBooleanParamToBinder(m_binder, "isSameDay",
						isSameDay);
				CCLAUtils.addQueryDateParamToBinder(m_binder, "START_DATE",
						latestEOD.getRunStartDate());
				CCLAUtils.addQueryDateParamToBinder(m_binder, "END_DATE",
						latestEOD.getRunDate());

				CCLAUtils.addQueryIntParamToBinder(m_binder, "EOD_ID",
						latestEOD.getEodId());
				CCLAUtils.addQueryParamToBinder(m_binder,
						"TOTAL_SHARE_CLASS_EXPENSE",
						totalShareClassExpense.toString());

				CCLAUtils.addQueryParamToBinder(m_binder,
						"TOTAL_NET_INCOME_DISTRIBUTION",
						totalNetIncomeDistribution.toString());

				CCLAUtils.addQueryParamToBinder(m_binder, "TOTAL_CASH_EXPENSE",
						totalCashExpense.toString());
				CCLAUtils.addQueryParamToBinder(m_binder, "TOTAL_RATE_EXPENSE",
						totalRateExpense.toString());

				// update the expense applied objects to reflect the eod_id
				for (IncomeExpenseApplied ieApplied : iea) {
					ieApplied.setEodId(Integer.toString(latestEOD.getEodId()));
					ieApplied.persist(facade, username);
				}

				// update the income applied object(s) to reflect the eod_id
				Vector<IncomeExpenseApplied> incomeApplied = IncomeExpenseApplied
						.getIncomeAppliedByDate(
								TransactionGlobals.FUND_INCOME_TYPE_ID,
								eodDate, fundCode, facade);
				for (IncomeExpenseApplied ieApplied : incomeApplied) {
					ieApplied.setEodId(Integer.toString(latestEOD.getEodId()));
					ieApplied.persist(facade, username);
				}

				// update the gross income applied object(s) to reflect the
				// eod_id
				Vector<IncomeExpenseApplied> grossIncomeApplied = IncomeExpenseApplied
						.getIncomeAppliedByDate(
								TransactionGlobals.GROSS_INCOME_TYPE_ID,
								eodDate, fundCode, facade);
				for (IncomeExpenseApplied ieApplied : grossIncomeApplied) {
					ieApplied.setEodId(Integer.toString(latestEOD.getEodId()));
					ieApplied.persist(facade, username);
				}
			}
			facade.commitTransaction();
		} catch (Exception e) {
			facade.rollbackTransaction();
			Log.error("Cannot run previewEndOfDay", e);
			throw new ServiceException(e);
		}
	}

	/**
	 * Creates an entry in the income_expense_applied table for each
	 * income_expense passed to it
	 * 
	 * @throws DataException
	 * 
	 * @throws DataException
	 *             , ServiceException
	 */
	private Vector<IncomeExpenseApplied> createIncomeExpenseApplied(
			Vector<IncomeExpense> incomeExpense, Date addDate, String username,
			FWFacade facade) throws DataException {
		// create an entry in INCOME_EXPENSE_APPLIED for all relevant one-off
		// share class expenses
		Vector<IncomeExpenseApplied> iea = new Vector();
		for (IncomeExpense ie : incomeExpense) {
			if (ie.getDefaultValue() != null) {
				IncomeExpenseApplied newIEA = IncomeExpenseApplied.add(
						ie.getIncomeExpenseId(), addDate, ie.getDefaultValue(),
						null, username, facade);
				if (newIEA != null)
					iea.add(newIEA);
			}
		}
		return iea;
	}

	/**
	 * Populates the fund_audit table with the total closing units per share
	 * class
	 * 
	 * @throws DataException
	 * @throws ServiceException
	 * 
	 */
	public static void getFinalUnitValues(String username, FundEOD fundEOD, FWFacade facade)
			throws DataException, ServiceException {

		String fundCode = TransactionGlobals.PSDF_FUND_CODE;
		Date eodDate = fundEOD.getRunDate();
		// get vector of accounts with
		// 1) account id
		// 2) share class_id (from account table or share_class_movement table),
		// 3) closing units (from account value table or account_value_audit
		// table)

		// first get all open accounts for a fund
		DataResultSet rsAccounts = 
			Account.getOpenAccountsDataByFund(fundCode, facade);
		Log.debug("got open accounts:" + rsAccounts.getNumRows());
		// creat dataresultset to hold the account data
		String[] accountCols = new String[] { 
				"ACCOUNT_ID", 
				"SHARE_CLASS_ID",
				"CLOSING_UNITS" };

		DataResultSet rsAccountData = new DataResultSet(accountCols);

		do {
			Vector<Object> accV = new Vector<Object>();
			Account account = Account.get(rsAccounts);
			if (account != null) {
				Log.debug("checking closing units for account:"
						+ account.getAccountId());
				// accountId
				accV.add(account.getAccountId());
				// share_class_id
				String shareClass = null;
				// look in share_class_movement table, then account table
				ShareClassMovement scm = ShareClassMovement.getByAccountId(
						account.getAccountId(),
						TransactionGlobals.MOVE_TYPE_NAME_APPLY, eodDate,
						facade);
				if (scm != null) {
					// check to see if there is an undo action, if there is then
					// take final share class from this
					ShareClassMovement undoSCM = ShareClassMovement
							.getByAccountId(account.getAccountId(),
									TransactionGlobals.MOVE_TYPE_NAME_UNDO,
									eodDate, facade);
					if (undoSCM != null)
						shareClass = undoSCM.getNewShareClassId();
					else
						shareClass = scm.getNewShareClassId();
				} else {
					shareClass = account.getShareClass();
				}
				Log.debug("got final share class of " + shareClass);
				accV.add(shareClass);
				// closing units
				String closingUnits = null;
				// first check account value audit table
				AccountValueAudit ava = AccountValueAudit.get(
						account.getAccountId(), eodDate, facade);
				if (ava != null) {
					closingUnits = ava.getClosingUnits();
					Log.debug("picked up account value audit: "
							+ ava.getAccountValueAuditId());
					Log.debug("with closing units " + closingUnits);
				} else {
					closingUnits = Account.getAccountUnits(
							account.getAccountId(), facade);
					Log.debug("not found account value audit for account:"
							+ account.getAccountId());
				}
				Log.debug("got closingUnits of " + closingUnits);
				accV.add(closingUnits);
				rsAccountData.addRow(accV);
			}
		} while (rsAccounts.next());
		Log.debug("Got rows for rsAccountData:" + rsAccountData.getNumRows());
		// populate fund_audit for each share class adding
		// 1) fund_code
		// 2) share_class_id
		// 3) management rate
		// 4) fund_audit_date
		// 5) opening_units (from previous day if available)
		// 6) closing units

		// get share classes for fund
		DataResultSet rsShareClass = ShareClass.getShareClassesByFund(fundCode,
				facade);

		if (rsShareClass != null) {
			do {
				ShareClass shareClass = ShareClass.get(rsShareClass);
				int shareClassId = shareClass.getShareClassId();
				// this was taken out as the management rate has been moved into
				// income_expense table as a general expense
				// String managementRate = shareClass.getManagementRate();
				String managementRate = "0.00";
				// Log.debug("got management rate of " + managementRate);
				BigDecimal closingUnits = new BigDecimal(0);
				// get closing units from rsAccountData resultset
				if (rsAccountData != null) {
					Log.debug("in rsaccountdata");
					rsAccountData.first();
					do {
						String scId = CCLAUtils
								.getResultSetStringValue(rsAccountData,
										"SHARE_CLASS_ID");
						if (scId != null) {
							Log.debug("got scId of " + scId);
							if (Integer.parseInt(scId) == shareClassId) {
								String closingU = CCLAUtils
										.getResultSetStringValue(rsAccountData,
												"CLOSING_UNITS");
								Log.debug("got closingU of " + closingU);
								if (closingU != null) {
									BigDecimal closing = new BigDecimal(
											closingU);
									closingUnits = closingUnits.add(closing);
								}
							}
						}
					} while (rsAccountData.next());
				}
				Log.info("got total closing units for share class "
						+ shareClassId + " of " + closingUnits);
				FundAudit fa = FundAudit.get(shareClassId, eodDate, facade);
				String openingUnits = AccountValue.getUnitTotalByShareClass(
						shareClassId, facade);
				if (fa != null) {
					fa.setClosingUnits(closingUnits.toString());
					// fa.setManagementRate(managementRate);
					// set opening units from Account_value table
					fa.setOpeningUnits(openingUnits);
					fa.persist(facade, username);
				} else {
					fa = FundAudit.add(fundCode,
							shareClassId, managementRate, eodDate, null,
							openingUnits, closingUnits.toString(), null, null,
							null, username, facade);
				}
			} while (rsShareClass.next());
		}

	}

//	/**
//	 * Gets the unit price set for the day and creates or updates the daily
//	 * fundPriceApplied object with this value
//	 * 
//	 * 
//	 * @param fundCode
//	 * @param facade
//	 * @throws DataException
//	 */
//	public static void getUnitPrice(String fundCode, String username,
//			FWFacade facade) throws DataException {
//		FundPrice fundPrice = FundPrice.get(FundPrice.getDailyData(fundCode,
//				facade));
//		Integer fundPriceId = null;
//
//		if (fundPrice != null) {
//			fundPriceId = fundPrice.getFundPriceId();
//		} else {
//			// use default values
//			String dailyUnitPrice = TransactionGlobals.DEFAULT_PSDF_UNIT_PRICE;
//			FundPrice newFundPrice = FundPrice.add(fundCode, new Date(),
//					dailyUnitPrice, dailyUnitPrice, dailyUnitPrice, username,
//					facade);
//			fundPriceId = newFundPrice.getFundPriceId();
//		}
//
//		Log.debug("Got fundPriceId of " + fundPriceId);
//
//		if (FundPriceApplied
//				.getToday(TransactionGlobals.PSDF_FUND_CODE, facade) != null) {
//			FundPriceApplied fpa = FundPriceApplied.getToday(
//					TransactionGlobals.PSDF_FUND_CODE, facade);
//			fpa.setFundPriceId(fundPriceId);
//			fpa.persist(facade, username);
//		} else {
//			FundPriceApplied fpa = FundPriceApplied.add(fundPriceId,
//					TransactionGlobals.TRANSACTION_DEAL_TYPE_MID, new Date(),
//					username, facade);
//		}
//	}

	public void getFundAudit() throws DataException, ServiceException {
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		//String username = m_userData.m_name;
		String fundCode = TransactionGlobals.PSDF_FUND_CODE;
		try {
			FundEOD latestEOD = FundEOD.getLatestByStatus(fundCode,
					TransactionGlobals.FUND_EOD_STATUS_IN_PROGRESS_ID, facade);
			Date eodDate = latestEOD.getRunDate();

			DataBinder binder = new DataBinder();
			CCLAUtils.addQueryParamToBinder(binder, "FUND_CODE", fundCode);
			CCLAUtils.addQueryDateParamToBinder(binder, "FUND_AUDIT_DATE",
					eodDate);
			DataResultSet rs = facade.createResultSet(
					"qTransactions_GetFundAuditDetailByDate", binder);
			if (rs != null)
				m_binder.addResultSet("rsFundAudit", rs);

		} catch (DataException e) {
			facade.rollbackTransaction();
			String msg = "Unable to get fund audit: " + e.getMessage();
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}

	}

	public void restartEOD() throws ServiceException, DataException {

		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		String fundCode = TransactionGlobals.PSDF_FUND_CODE;
		//DataBinder binder = new DataBinder();

		Log.debug("beginning restartEOD");		
		// First check to see if an EOD has been completed for today's date
		// already
		// if so then return error to screen
		FundEOD completeEOD = FundEOD.getByStatusDate(fundCode, new Date(),
				TransactionGlobals.FUND_EOD_STATUS_COMPLETE_ID, facade);

		if (completeEOD != null) {
			m_binder.putLocal("ERROR_MESSAGE",
					TransactionGlobals.ERROR_MSG_CANNOT_RESTART_COMPLETED_EOD);
		} else {
			Log.debug("allowed restartEOD");
			FundEOD latestEOD = FundEOD.getLatestByStatus(fundCode,
					TransactionGlobals.FUND_EOD_STATUS_IN_PROGRESS_ID, facade);
			clearDownEODBeforeFinalised(latestEOD, facade);
		}
	}

//	/**
//	 * Sets the NAV value (into Fund_nav_audit) table which is calculated by
//	 * multiplying the total closing units by the basic unit price for the day
//	 * 
//	 * @param fundCode
//	 * @param username
//	 * @param facade
//	 * @throws DataException
//	 * @throws ServiceException
//	 */
//	public static void setNav(String fundCode, String username, FWFacade facade)
//			throws DataException, ServiceException {
//		FundEOD latestEOD = FundEOD.getLatestByStatus(fundCode,
//				TransactionGlobals.FUND_EOD_STATUS_IN_PROGRESS_ID, facade);
//		Date eodDate = latestEOD.getRunDate();
//
//		FundPriceApplied fpa = FundPriceApplied.getToday(fundCode, facade);
//		String basicPrice = FundPrice.getBasicPrice(fpa.getFundPriceId(),
//				facade);
//		String closingUnits = FundAudit.getTotalClosingUnits(fundCode, eodDate,
//				facade);
//		if (StringUtils.stringIsBlank(basicPrice)
//				|| StringUtils.stringIsBlank(closingUnits))
//			throw new ServiceException(
//					"Cannot get basic price and closing units to calculate NAV");
//		BigDecimal basic = new BigDecimal(basicPrice);
//		// convert to pounds
//		basic = basic.divide(hundred, TransactionGlobals.decimalPlaces,
//				TransactionGlobals.roundRule);
//
//		BigDecimal units = new BigDecimal(closingUnits);
//		BigDecimal nav = basic.multiply(units);
//		Log.debug("got nav of " + nav.toString());
//
//		// create a fund nav audit entry for this
//		FundNavAudit fna = FundNavAudit.add(fundCode, new Date(),
//				nav.toString(), username, facade);
//	}

	/**
	 * Finishes the end of day. All transactions are marked as completed eod,
	 * and account has been updated with the latest share class movements.
	 * 
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void finishEndOfDay() throws DataException, ServiceException {
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		String username = m_userData.m_name;
		String fundCode = TransactionGlobals.PSDF_FUND_CODE;

		try {

			facade.beginTransaction();
			int eodId = CCLAUtils.getBinderIntegerValue(m_binder, "EOD_ID");

			FundEOD eod = FundEOD.get(eodId, facade);

			// First check to see if an EOD has been completed for today's date
			// already
			// if so then return error to screen
			FundEOD completeEOD = FundEOD.getByStatusDate(fundCode, eod.getRunDate(),
					TransactionGlobals.FUND_EOD_STATUS_COMPLETE_ID, facade);
			Log.debug("completeEOD is " + completeEOD);
			if (completeEOD != null) {
				m_binder.putLocal("ERROR_MESSAGE",
						TransactionGlobals.ERROR_MSG_CANNOT_START_COMPLETED_EOD);
			} else {

				Date eodDate = eod.getRunDate();

				// update instruction statuses
				// get transactions marked as processed
				Vector<Instruction> instructions = Instruction
						.getTransactionsByStatus(
								Integer.parseInt(TransactionGlobals.PROCESSED_INSTRUCTION_STATUS_ID),
								fundCode, facade);
				Log.debug("instructions is " + instructions);

				// update status to completed
				int completedStatusId = Integer
						.parseInt(TransactionGlobals.COMPLETED_INSTRUCTION_STATUS_ID);
				Log.debug("got status of " + completedStatusId);
				if (instructions != null && instructions.size() > 0)
					Instruction.updateBulkInstructionStatus(instructions,
							completedStatusId, username, facade);

				// add EOD Id to instructions data value

				if (instructions != null && instructions.size() > 0) {
					Log.debug("got instructions");
					for (Instruction instruction : instructions) {
						InstructionDataApplied.updateDataValueById(
								instruction.getInstructionId(),
								TransactionGlobals.EOD_ID_DATA_ID,
								Integer.toString(eodId), username, facade);
					}
				}

				// update instruction statuses FOR PI ACCOUNTS (for end of month
				// transfers)
				// get transactions marked as processed
				Vector<Instruction> piInstructions = Instruction
						.getTransactionsByStatus(
								Integer.parseInt(TransactionGlobals.PROCESSED_INSTRUCTION_STATUS_ID),
								TransactionGlobals.PI_FUND_CODE, facade);
				Log.debug("PI instructions is " + piInstructions);

				// update status to completed
				if (piInstructions != null && piInstructions.size() > 0)
					Instruction.updateBulkInstructionStatus(piInstructions,
							completedStatusId, username, facade);

				// add EOD Id to instructions data value
				if (piInstructions != null && piInstructions.size() > 0) {
					Log.debug("got instructions");
					for (Instruction instruction : piInstructions) {
						InstructionDataApplied.updateDataValueById(
								instruction.getInstructionId(),
								TransactionGlobals.EOD_ID_DATA_ID,
								Integer.toString(eodId), username, facade);
					}
				}
				// update accounts with new share classes based on share class
				// movement table
				Vector<ShareClassMovement> movement = ShareClassMovement
						.getByDateFund(fundCode,
								TransactionGlobals.MOVE_TYPE_NAME_APPLY,
								eodDate, facade);
				if (movement != null && movement.size() > 0) {
					Log.debug("got share class movements");
					for (ShareClassMovement scm : movement) {
						Account account = Account.get(scm.getAccountId(),
								facade);
						String newShareClassId = scm.getNewShareClassId();

						account.setShareClass(newShareClassId);
						account.persist(facade, username);
						Log.info("UPDATED ACCOUNT: " + scm.getAccountId()
								+ " with SHARE CLASS ID " + newShareClassId);
					}
				}
				// apply this to all accounts
				// TODO NEED to check with KD whether this is ok --- CL
				// 13/05/2011
				// Vector<AccountValueAudit> avaVec =
				// getAccountAuditByDate(reportDate, fundCode, facade);
				// for (AccountValueAudit ava : avaVec) {
				// AccountValue av = AccountValue.get(ava.getAccountId(),
				// facade);
				// if (av!=null) {
				// av.setCash(ava.getClosingCash());
				// av.setUnits(ava.getClosingUnits());
				// av.persist(facade, username);
				// } else {
				// Log.error("Cannot find account value for account id:"+ava.getAccountId());
				// throw new
				// DataException("Cannot find account value for account id:"+ava.getAccountId());
				// }
				// }

				// mark EOD as confirmed

				eod.setEodStatusId(TransactionGlobals.FUND_EOD_STATUS_COMPLETE_ID);
				eod.persist(facade, username);
			}
			facade.commitTransaction();
		} catch (NumberFormatException e) {
			facade.rollbackTransaction();
			throw new ServiceException(e);
		} catch (DataException e) {
			facade.rollbackTransaction();
			throw new ServiceException(e);
		}

	}

	/**
	 * Identify seed funders and early investors and ensure their accounts are
	 * in the correct share class (using share class override) UNLESS it's an
	 * internal account, in which case it should have share class 1.
	 * 
	 * @param fundCode
	 * @param username
	 * @param facade
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void updateSpecialCaseShareClass(String fundCode, FundEOD fundEOD, String username,
			FWFacade facade) throws DataException, ServiceException {

		Date eodDate = fundEOD.getRunDate();
		DataResultSet rsAccounts = Account.getOpenAccountsDataByFund(fundCode,
				facade);
		Vector<Account> accountV = Account.getAccounts(rsAccounts);
		Integer seedFunderElement = Integer
				.parseInt(TransactionGlobals.PSDF_SEED_FUND_ELEMENT_ATTRIBUTE_ID);
		Integer earlyInvestorElement = Integer
				.parseInt(TransactionGlobals.PSDF_EARLY_INVESTOR_ELEMENT_ATTRIBUTE_ID);
		Integer specialShareClass = TransactionGlobals.PSDF_SEED_FUND_AND_EARLY_INVESTOR_SHARE_CLASS_ID;
		Integer internalShareClass = TransactionGlobals.PSDF_INTERNAL_ACCOUNT_SHARE_CLASS_ID;
		for (Account account : accountV) {
			int accountId = account.getAccountId();
			// is account an internal account?
			ElementAttributeApplied internalAcc = ElementAttributeApplied
					.get(accountId,
							ElementAttribute.AccountAttributes.INTERNAL_ACCOUNT,
							facade);
			if (internalAcc != null && internalAcc.getStatus()) {
				Log.debug("FOUND internal account with id:" + accountId);
				// in this case set share class to internalShareClass
				ShareClassOverride sco = ShareClassOverride.get(accountId,facade);
				if (sco == null) {
					// add
					DataBinder binder = new DataBinder();
					ShareClassOverride newSco = new ShareClassOverride(
							accountId, internalShareClass, username, eodDate,
							TransactionGlobals.INTERNAL_OVERRIDE_REASON);
					newSco.addFieldsToBinder(binder);
					ShareClassOverride.add(binder, facade, username);
				} else if (sco.getShareClassId() != internalShareClass) {
					// update
					Log.debug("updating share class override for account:"
							+ accountId);
					DataBinder binder = new DataBinder();
					sco.setShareClassId(internalShareClass);
					sco.setOverrideReason(TransactionGlobals.INTERNAL_OVERRIDE_REASON);
					sco.setUserId(username);
					sco.addFieldsToBinder(binder);
					sco.persist(facade, username);
				}
				// create share class movement
				if (!Integer.toString(internalShareClass).equalsIgnoreCase(
						account.getShareClass())) {
					ShareClassMovement scm = ShareClassMovement.add(accountId,
							eodDate, TransactionGlobals.MOVE_TYPE_NAME_APPLY,
							Integer.toString(internalShareClass),
							account.getShareClass(), facade, username);
					Log.debug("Created share class movement with id:"
							+ scm.getMovementId());
				}
			} else {
				// is account seed fund or early investor?
				ElementAttributeApplied seedFund = ElementAttributeApplied.get(
						accountId, seedFunderElement, facade);
				ElementAttributeApplied earlyInvestor = ElementAttributeApplied
						.get(accountId, earlyInvestorElement, facade);
				if ((seedFund != null && seedFund.getStatus())
						|| (earlyInvestor != null && earlyInvestor.getStatus())) {
					// create or update share class override if no share class
					// set for account
					// or share class differs
					if (account.getShareClass() == null
							|| (!StringUtils.stringIsBlank(account
									.getShareClass()) && Integer
									.parseInt(account.getShareClass()) != specialShareClass)) {
						Log.debug("creating share class override for account:"
								+ accountId);
						// get share class override
						ShareClassOverride sco = ShareClassOverride.get(
								accountId, facade);
						if (sco == null) {
							// add
							DataBinder binder = new DataBinder();
							ShareClassOverride newSco = new ShareClassOverride(
									accountId,
									specialShareClass,
									username,
									eodDate,
									TransactionGlobals.PSDF_SEED_FUND_EARLY_INVESTOR_OVERRIDE_REASON);
							newSco.addFieldsToBinder(binder);
							ShareClassOverride.add(binder, facade, username);
						} else if (sco.getShareClassId() != specialShareClass) {
							// update
							Log.debug("updating share class override for account:"
									+ accountId);
							DataBinder binder = new DataBinder();
							sco.setShareClassId(specialShareClass);
							sco.setOverrideReason(TransactionGlobals.PSDF_SEED_FUND_EARLY_INVESTOR_OVERRIDE_REASON);
							sco.setUserId(username);
							sco.addFieldsToBinder(binder);
							sco.persist(facade, username);
						}
					}
					// create share class movement
					if (!Integer.toString(specialShareClass).equalsIgnoreCase(
							account.getShareClass())) {
						ShareClassMovement scm = ShareClassMovement.add(
								accountId, eodDate,
								TransactionGlobals.MOVE_TYPE_NAME_APPLY,
								Integer.toString(specialShareClass),
								account.getShareClass(), facade, username);
						Log.debug("Created share class movement with id:"
								+ scm.getMovementId());
					}
				}
			}
		}

	}

	/**
	 * Identified accounts that should be marked as seed fund accounts and makes
	 * sure they are marked as such
	 * 
	 * Seed funders are all accounts that are open and have non negative balance
	 * before the cut-off date (26 May)
	 * 
	 * @param fundCode
	 * @param username
	 * @param facade
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void identifyPSDFSeedFundAccounts(String fundCode, FundEOD fundEOD, String username,
			FWFacade facade) throws DataException, ServiceException {

		try {
			// get cut off date
			String cutoffStr = TransactionGlobals.PSDF_SEED_FUND_CUTOFF_DATE;
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			Date cutOffDate = df.parse(cutoffStr);
			Calendar cutoffCal = Calendar.getInstance();
			cutoffCal.setTime(cutOffDate);
			// if the date/time is after midnight on the cutoff date then
			// account is not a seed fund account
			DateFormat sdf = new SimpleDateFormat("hh:mm:ss");
			String cutOffTime = "23:59:59";
			Date cutOff = sdf.parse(cutOffTime);
			Calendar timeCal = Calendar.getInstance();
			timeCal.setTime(cutOff);

			cutoffCal.set(Calendar.HOUR_OF_DAY,
					timeCal.get(Calendar.HOUR_OF_DAY));
			cutoffCal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
			cutoffCal.set(Calendar.SECOND, timeCal.get(Calendar.SECOND));

			Log.debug("CutoffCal is " + cutoffCal.getTime());
			// current date
			Calendar currentDate = Calendar.getInstance();

			if (currentDate.after(cutoffCal)) {
				// in this case no accounts can be marked as a seed funder
			} else {
				// mark as seed funder if account value audit for today is
				// non-zero

				// get all open accounts for fundCode
				DataResultSet rsAccounts = Account.getOpenAccountsDataByFund(
						fundCode, facade);
				Log.debug("got rsAccounts:" + rsAccounts.getNumRows());
				Vector<Account> accountV = Account.getAccounts(rsAccounts);
				Integer seedFunderElement = Integer.parseInt(TransactionGlobals.PSDF_SEED_FUND_ELEMENT_ATTRIBUTE_ID);
				for (Account account : accountV) {
					// is account value audit non-zero
					int accountId = account.getAccountId();
					AccountValueAudit ava = AccountValueAudit.get(accountId, fundEOD.getRunDate(), facade);
					
					//boolean gotAVA = false;
					boolean setSF = false;
					if (ava != null) {
						//gotAVA = true;
						Log.debug("got ava with id "
								+ ava.getAccountValueAuditId()
								+ " for account " + account.getAccountId());
						if (!StringUtils.stringIsBlank(ava.getClosingCash())) {
							setSF = true;
						}
					} else {
						// check account balance in account value is non
						// negative
						AccountValue av = AccountValue.get(accountId, facade);
						if (av != null) {
							Log.debug("got account value with cash:"+av.getCash());
							if (!StringUtils.stringIsBlank(av.getCash())) {
								setSF = true;
							}
						}
					}
					if (setSF) {
						Log.debug("setting account "+accountId+" as seed funder");
						// Mark as seed funder
						ElementAttributeApplied.addOrUpdateSingle(accountId,
								seedFunderElement, true, "1", facade);
					}
				}
			}
		} catch (ParseException e) {
			String msg = "Cannot calculate process rollover date/time";
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}

	/**
	 * Identifies and marks early investor accounts
	 * 
	 * An early investor account is one that is not a seed funder, has positive
	 * balance, and is opened before the fund NAV reaches �2500000 (value set in
	 * component configuration).
	 * 
	 * @param fundCode
	 * @param username
	 * @param facade
	 * @throws DataException
	 */
	public void identifyEarlyInvestorAccounts(String fundCode, FundEOD fundEOD, String username,
			FWFacade facade) throws DataException {
		boolean markEI = false;
		// get the latest NAV figure, if this exceeds the cut off figure then no
		// accounts
		// are considered 'Early Investor'
		FundNavAudit fna = FundNavAudit.getLatest(fundCode, facade);
		if (fna == null) {
			// this should only happen on first day so in this case just carry
			// on but log it as an error
			Log.error("Unable to find latest fund nav audit object.");
			markEI = true;
		} else {
			BigDecimal nav = new BigDecimal(fna.getNav());

			String earlyInvestorThreshold = TransactionGlobals.PSDF_EARLY_INVESTOR_FUND_TOTAL_CUTOFF;
			BigDecimal cutOffValue = new BigDecimal(earlyInvestorThreshold);

			Log.debug("comparing cutoff " + cutOffValue.toString()
					+ " to nav of " + nav.toString());
			if (nav.compareTo(cutOffValue) == -1) {
				// if latest nav is less than cutoff value then continue
				Log.debug("latest nav is less than cut off value");
				markEI = true;
			}
		}

		if (markEI) {
			// look for all open accounts not marked as seed fund accounts
			// get all open accounts for fundCode
			DataResultSet rsAccounts = Account.getOpenAccountsDataByFund(fundCode, facade);
			Log.debug("got rsAccounts:" + rsAccounts.getNumRows());
			Vector<Account> accountV = Account.getAccounts(rsAccounts);
			Integer seedFunderElement = Integer
					.parseInt(TransactionGlobals.PSDF_SEED_FUND_ELEMENT_ATTRIBUTE_ID);
			Integer earlyInvestorElement = Integer
					.parseInt(TransactionGlobals.PSDF_EARLY_INVESTOR_ELEMENT_ATTRIBUTE_ID);
			for (Account account : accountV) {
				// is account value audit non-zero
				int accountId = account.getAccountId();
				Log.debug("starting with account: " + accountId);
				AccountValueAudit ava = AccountValueAudit.get(accountId,
						fundEOD.getRunDate(), facade);
				ElementAttributeApplied sfAttribute = ElementAttributeApplied
						.get(accountId, seedFunderElement, facade);
				//boolean gotAVA = false;
				boolean setEI = false;

				// if seed funder attribute is null than candidate for early
				// investor
				if (sfAttribute == null) {
					if (ava != null) {
						//gotAVA = true;
						Log.debug("got ava with id "
								+ ava.getAccountValueAuditId()
								+ " for account " + account.getAccountId());
						if (!StringUtils.stringIsBlank(ava.getClosingCash())) {
							setEI = true;
						}
					} else {
						// check account balance in account value is non
						// negative
						AccountValue av = AccountValue.get(accountId, facade);
						if (av != null) {
							Log.debug("got account value with cash:"
									+ av.getCash());
							if (!StringUtils.stringIsBlank(av.getCash())){
								setEI = true;
							}
						}
					}

					if (setEI) {
						Log.debug("setting account " + accountId
								+ " as early investor");
						// Mark as early investor
						ElementAttributeApplied.addOrUpdateSingle(accountId,
								earlyInvestorElement, true, "1", facade);
					}
				}
			}

		}

	}

	public void markSpecialCaseAccounts(String fundCode, FundEOD fundEOD, String username,
			FWFacade facade) throws DataException, ServiceException {
		// ensure all seed funder accounts are correctly identified and marked
		identifyPSDFSeedFundAccounts(fundCode, fundEOD, username, facade);

		// ensure that early investor accounts are identified and marked
		identifyEarlyInvestorAccounts(fundCode, fundEOD,username, facade);

		// get all seed funders or early investor accounts and make sure they
		// are in the correct
		// share class
		updateSpecialCaseShareClass(fundCode, fundEOD, username, facade);

	}

	/**
	 * Creates a previous end of day, first checking to see if one is already in
	 * progress
	 * 
	 * @param lastCompletedEOD
	 * @param fundCode
	 * @param username
	 * @param binder
	 * @param facade
	 * @throws DataException
	 */
	public static void createPreviousEndOfDay(FundEOD lastCompletedEOD,
			String fundCode, String username, DataBinder binder, FWFacade facade)
			throws DataException {
		Calendar yesterday = Calendar.getInstance();
		yesterday.add(Calendar.DATE, -1);
		Calendar lastCompleted = Calendar.getInstance();
		lastCompleted.setTime(lastCompletedEOD.getRunDate());
		lastCompleted.add(Calendar.DATE, +1);

		// check to see if an EOD has been completed for today's date
		// already
		// if so then return error to screen
		FundEOD completedEOD = FundEOD.getByStatusDate(fundCode, yesterday.getTime(),
				TransactionGlobals.FUND_EOD_STATUS_COMPLETE_ID, facade);

		if (completedEOD != null) {
			Log.error("Found completed EOD for "+completedEOD.getRunDate());
			binder.putLocal("ERROR_MESSAGE",
					TransactionGlobals.ERROR_MSG_CANNOT_START_COMPLETED_EOD);
			CCLAUtils.addQueryIntParamToBinder(binder, "AllowRestart", 0);			
			return;
		}
		
		FundEOD EOD = FundEOD.getByStatusRunDate(fundCode, yesterday.getTime(),
				TransactionGlobals.FUND_EOD_STATUS_IN_PROGRESS_ID, facade);
		if (EOD == null) {
			Log.debug("end of day NOT found for date:" + yesterday.getTime());
			FundEOD yEOD = FundEOD.add(fundCode, null,
					TransactionGlobals.FUND_EOD_STATUS_IN_PROGRESS_ID, null,
					null, null, null, null, username, yesterday.getTime(),
					new Date(), lastCompleted.getTime(), username, facade);
			binder.putLocal("runPreviousEOD", "1");
			CCLAUtils.addQueryDateParamToBinder(binder, "START_DATE",
					yEOD.getRunStartDate());
			CCLAUtils.addQueryDateParamToBinder(binder, "END_DATE",
					yEOD.getRunDate());
		} else {
			Log.debug("end of day already found for date:"
					+ yesterday.getTime());
			CCLAUtils.addQueryParamToBinder(binder, "ERROR_MESSAGE",
					TransactionGlobals.ERROR_MSG_NEED_RESTART_EOD);
			CCLAUtils.addQueryIntParamToBinder(binder, "AllowRestart", 1);
		}
	}

	/**
	 * Re-run EOD service. Allows the uses to re-run a previously completed eod.
	 * i.e. revert Instructions, ShareClassMovement, income-expense, FundAudit,
	 * AccountValues. This should only be ran for the latest FundEOD as all
	 * related values will be reseted to
	 * 
	 * NB This does not reset the account value amounts as this is controlled
	 * via Kainos. If the Kainos script has ran, then that needs to be reversed
	 * as the account balances will be incorrect.
	 * 
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void rerunEOD() throws DataException, ServiceException {

		// 1. Get necessary variables.
		String userId = m_userData.m_name;
		Integer eodId = CCLAUtils.getBinderIntegerValue(m_binder,
				FundEOD.EOD_ID);
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		String errorMsg; // Variable for storing error message.
		Date runDate = null;
		Date runStartDate = null;
		

		// 2. Check if allowed to re-run the eod.
		// 2.1 Check if eodId exist
		if (eodId == null) {
			errorMsg = "Cannot re-run EOD as EodID is null";
			Log.error(errorMsg);
			throw new DataException(errorMsg);
		}

		Log.debug("Re-running EOD for EOD_ID:" + eodId);

		// 2.2 Check if FundEOD exist
		DataResultSet rsFundEOD = FundEOD.getData(eodId, facade);
		FundEOD fundEOD = FundEOD.get(rsFundEOD);
		if (fundEOD == null) {
			errorMsg = "Cannot Re-run EOD, Cannot find FundEOD with id "
					+ eodId;
			Log.error(errorMsg);
			throw new DataException(errorMsg);
		}

		runDate = fundEOD.getRunDate();
		runStartDate = fundEOD.getRunStartDate();		
		
		// 2.3 Check if FundEOD is the latest.
		FundEOD lastFundEOD = FundEOD.getLatestByStatus(fundEOD.getFundCode(),
				TransactionGlobals.FUND_EOD_STATUS_COMPLETE_ID, facade);
		if (lastFundEOD.getEodId() != fundEOD.getEodId()) {
			errorMsg = "Cannot Re-run EOD, There is a later FundEOD with Id: "
					+ lastFundEOD.getEodId() + ", runDate: "
					+ DateUtils.formatddsMMsyyyy(lastFundEOD.getRunDate())
					+ ", status: " + lastFundEOD.getEodStatusId();
			Log.error(errorMsg);
			throw new ServiceException(errorMsg);
		}

		
		// 3. Begin the revert process.
		try {

			facade.beginTransaction();

			// 3.1 Reset Account ShareClass
			Vector<ShareClassMovement> movementsForEOD = ShareClassMovement
					.getByDateFund(fundEOD.getFundCode(),
							TransactionGlobals.MOVE_TYPE_NAME_APPLY,
							fundEOD.getRunDate(), facade);

			if (movementsForEOD != null && movementsForEOD.size() > 0) {
				String currShareClassId;
				String prevShareClassId;
				String currAccShareClassId;

				for (ShareClassMovement scm : movementsForEOD) {
					Account account = Account.get(scm.getAccountId(), facade);

					// Check if account is null.
					if (account == null) {
						errorMsg = "Cannot re-run FundEOD with id:"
								+ fundEOD.getEodId()
								+ ", Can't find Account with id:"
								+ scm.getAccountId();
						Log.error(errorMsg);
						throw new DataException(errorMsg);
					}

					currAccShareClassId = account.getShareClass();
					currShareClassId = scm.getNewShareClassId();
					prevShareClassId = scm.getOldShareClassId();

					// Check to see if the shareclass movement is the same as
					// the current movement
					if (!StringUtils.stringIsBlank(currAccShareClassId)
							&& !StringUtils.stringIsBlank(currShareClassId)) {
						if (!currShareClassId.equals(currAccShareClassId)) {
							errorMsg = "Cannot re-run FundEOD with id:"
									+ fundEOD.getEodId() + ", AccShareClassId:"
									+ currAccShareClassId
									+ " is not the same as scmShareClassId:"
									+ currShareClassId + ", for scmId:"
									+ scm.getMovementId();
							Log.error(errorMsg);
							throw new DataException(errorMsg);
						}
					}

					account.setShareClass(prevShareClassId);
					account.persist(facade, userId);
					Log.info("Re-Run EOD, UPDATED ACCOUNT:"
							+ scm.getAccountId() + ", set ShareClassId to:"
							+ prevShareClassId + " from:" + currShareClassId);
				}
			}

			// 3.2 Reset all Instructions to initial values
			// - Remove EOD_ID instruction data value
			// - Change Instruction Status to
			// TransactionGlobals.PROCESS_INSTRUCTION_STATUS_ID
			DataResultSet rsInstructions = facade.createResultSet(
					"qTransactions_GetInstructionIdsForEODId", m_binder);

			if (rsInstructions != null && !rsInstructions.isEmpty()) {
				do {
					Integer instructionId = CCLAUtils.getResultSetIntegerValue(
							rsInstructions, SharedCols.INSTRUCTION);

					if (instructionId != null) {
						Instruction instruction = Instruction.get(
								instructionId, facade);

						if (instruction != null) {
							// set the EOD_ID to null
							InstructionDataApplied.updateDataValueById(
									instructionId,
									TransactionGlobals.EOD_ID_DATA_ID, null,
									userId, facade);
							// set the status of the instruction.
							int statusId = Integer
									.parseInt(TransactionGlobals.PROCESS_INSTRUCTION_STATUS_ID);
							instruction.setStatusById(statusId);
							instruction.persist(facade, userId);
						}
					}
				} while (rsInstructions.next());
			}

			// 3.3 Clear the data for the fundEOD
			clearDownEODBeforeFinalised(fundEOD, facade);

			// 3.4 Add the date ranges to the m_binder so this is used for the
			//     date range.
			CCLAUtils.addQueryParamToBinder(m_binder, "PSDF_RUN_START_DATE", DateUtils.formatddsMMsyyyy(runStartDate));
			CCLAUtils.addQueryParamToBinder(m_binder, "PSDF_RUN_DATE", DateUtils.formatddsMMsyyyy(runDate));

			
			//3.5 AuditLog 
			Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, userId, 
			 Globals.AuditActions.DELETE.toString(), 
			 ElementType.SecondaryElementType.FundEOD.toString(), 
			 rsFundEOD, null, null);
			
			// 4. Finally commit the instruction and append
			facade.commitTransaction();
			
			Log.debug("Finished Re-running EOD for EOD_ID:" + eodId);

		} catch (Exception e) {
			// Rolls back the transaction and display the error
			facade.rollbackTransaction();
			Log.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage(), e);
		}
		// finally {
		//
		// }
	}

	/**
	 * Method to revert
	 * 
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void restartEODWithDates() throws DataException, ServiceException {
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		String fundCode = TransactionGlobals.PSDF_FUND_CODE;

//		Date runDate = CCLAUtils.getBinderDateValue(m_binder, "PSDF_RUN_DATE");
//		Date runStartDate = CCLAUtils.getBinderDateValue(m_binder,
//				"PSDF_RUN_START_DATE");
		Log.debug("beginning restartEOD with dates");		

		// First check to see if an EOD has been completed for today's date
		// already
		// if so then return error to screen
		FundEOD completeEOD = FundEOD.getByStatusDate(fundCode, new Date(),
				TransactionGlobals.FUND_EOD_STATUS_COMPLETE_ID, facade);

		if (completeEOD != null) {
			m_binder.putLocal("ERROR_MESSAGE",
					TransactionGlobals.ERROR_MSG_CANNOT_RESTART_COMPLETED_EOD);
		} else {
			Log.debug("restartEOD with dates allowed.");		

			FundEOD latestEOD = FundEOD.getLatestByStatus(fundCode,
					TransactionGlobals.FUND_EOD_STATUS_IN_PROGRESS_ID, facade);
			clearDownEODBeforeFinalised(latestEOD, facade);
		}

	}

	/**
	 * Clears down the current eod before it is finalised
	 * 
	 * @param eodId
	 * @throws DataException
	 * @throws ValidationException
	 */
	private static void clearDownEODBeforeFinalised(FundEOD fundEOD,
			FWFacade facade) throws DataException, ServiceException {

		DataBinder binder = new DataBinder();

		if (fundEOD!=null) 
		{
			
			Date eodDate = fundEOD.getRunDate();
			
			Log.debug("**** Clearing down all FUND_EOD with runDate "+eodDate);
			
			// change status of in process transactions back to '1'
			CCLAUtils.addQueryIntParamToBinder(
							binder,
							"READY_TO_PROCESS_STATUS_ID",
							Integer.parseInt(TransactionGlobals.PROCESS_INSTRUCTION_STATUS_ID));
			CCLAUtils.addQueryIntParamToBinder(binder, "PROCESSED_STATUS_ID", Integer
					.parseInt(TransactionGlobals.IN_PROCESS_INSTRUCTION_STATUS_ID));
			facade.execute("qTransactions_ResetProcessedTransactions", binder);
	
			// change status of transactions back to '1'
			CCLAUtils.addQueryIntParamToBinder(
							binder,
							"READY_TO_PROCESS_STATUS_ID",
							Integer.parseInt(TransactionGlobals.PROCESS_INSTRUCTION_STATUS_ID));
			CCLAUtils.addQueryIntParamToBinder(binder, "PROCESSED_STATUS_ID", Integer
					.parseInt(TransactionGlobals.PROCESSED_INSTRUCTION_STATUS_ID));
			facade.execute("qTransactions_ResetProcessedTransactions", binder);
	
			// delete share class movements for today
			CCLAUtils.addQueryDateParamToBinder(binder, "MOVE_DATE", eodDate);
			facade.execute("qTransactions_DeleteShareClassMovementForDate", binder);
	
			// remove account value audits for today
			CCLAUtils.addQueryDateParamToBinder(binder, "ACCOUNT_VALUE_DATE", eodDate);
			facade.execute("qTransactions_DeleteAccountValueAuditForDate", binder);
	
			// remove fund audit
			CCLAUtils.addQueryDateParamToBinder(binder, "FUND_AUDIT_DATE", eodDate);
			facade.execute("qTransactions_DeleteFundAuditForDate", binder);
	
			// remove income expenses
			CCLAUtils.addQueryDateParamToBinder(binder, "INCOME_EXPENSE_DATE", eodDate);
			facade.execute("qTransactions_DeleteIncomeExpenseAppliedByDate", binder);
	
			// remove fundEOD object
			CCLAUtils.addQueryDateParamToBinder(binder, "RUN_DATE", eodDate);
			facade.execute("qTransactions_DeleteFundEODForDate", binder);
		
		}
		// Clear down all FundEOD that are in the state IN_PROGRESS
		
		Log.debug("Clearing down all FUND_EOD with IN_PROGRESS_STATUS");
		
		CCLAUtils.addQueryIntParamToBinder(
				binder, 
				"FUND_EOD_STATUS_ID", 
				TransactionGlobals.FUND_EOD_STATUS_IN_PROGRESS_ID);
		facade.execute("qTransactions_DeleteFundEODForStatus", binder);
		
	}
	
	/**
	 * Checks if there is more than 1 FundEOD in progress and
	 * whether the current running one is completed before.
	 * @return
	 */
	private static boolean checkIfFundEODInProgress(String fundCode, int statusId, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryParamToBinder(binder, "FUND_CODE", fundCode);
		CCLAUtils.addQueryIntParamToBinder(binder, "FUND_EOD_STATUS_ID", statusId);
		DataResultSet rs = facade.createResultSet("qTransactions_GetEODByStatus", binder);
		return (rs!=null && !rs.isEmpty());
	}
}
