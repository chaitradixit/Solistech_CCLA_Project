package com.ecs.ucm.ccla.transactions.globals;

import java.math.BigDecimal;

import intradoc.shared.SharedObjects;

public class TransactionGlobals {
	
	// rounding variables for all divide calculations
	// number of decimal places to use
	public static int decimalPlaces = 
		 SharedObjects.getEnvironmentInt("PSDF_DECIMAL_PLACES", 10);
	// rounding rule
	public static int roundRule = SharedObjects.getEnvironmentInt("PSDF_ROUNDING_RULE", BigDecimal.ROUND_DOWN);

	public static final String PSDF_DECIMAL_FORMAT_STR = 
		SharedObjects.getEnvironmentValue("PSDF_DECIMAL_FORMAT_STR");
	
	
	public static String
	TRANSACTION_ROLLOVER_TIME = 
		 SharedObjects.getEnvironmentValue("TRANSACTION_ROLLOVER_TIME");

	public static String
	PSDF_FUND_CODE = 
		 SharedObjects.getEnvironmentValue("PSDF_FUND_CODE");
	
	public static String
	PI_FUND_CODE = "PI";

	/** STATUSES **/
	public static String
	PROCESS_INSTRUCTION_STATUS_ID = 
		 SharedObjects.getEnvironmentValue("PROCESS_INSTRUCTION_STATUS_ID");
	
	public static String
	IN_PROCESS_INSTRUCTION_STATUS_ID = 
		 SharedObjects.getEnvironmentValue("IN_PROCESS_INSTRUCTION_STATUS_ID");
	
	public static String
	PROCESSED_INSTRUCTION_STATUS_ID = 
		 SharedObjects.getEnvironmentValue("PROCESSED_INSTRUCTION_STATUS_ID");
	
	public static String
	COMPLETED_INSTRUCTION_STATUS_ID = 
		 SharedObjects.getEnvironmentValue("COMPLETED_INSTRUCTION_STATUS_ID");
		
	/** move type names for share class movement **/
	public static int
	MOVE_TYPE_NAME_APPLY = 1;	
	public static int
	MOVE_TYPE_NAME_UNDO = 2;	
	
	
	// these values are the values held in the TRANSACTION_TYPE_ID column of the REF_TRANSACTION_TYPE table
	public static String
	TRANSACTION_TYPE_BUY = "Buy";
	public static String
	TRANSACTION_TYPE_SELL = "Sell";
	public static String
	TRANSACTION_TYPE_TRANSFER = "Transfer";
	public static int
	TRANSACTION_TYPE_BUY_ID = 1;
	public static int
	TRANSACTION_TYPE_SELL_ID = 2;
	public static int
	TRANSACTION_TYPE_TRANSFER_ID = 3;
	
	
	
	// TRANSACTION TYPES IDS FOR SHARE CLASS EXPENSES
	public static int GENERAL_EXPENSE_TYPE_ID = 1;
	public static int SHARE_CLASS_EXPENSE_TYPE_ID = 2;
	public static int FUND_INCOME_TYPE_ID = 3;
	public static int GROSS_INCOME_TYPE_ID = 4;

	// DEFAULT UNIT PRICE FOR PSDF
	public static String 
	DEFAULT_PSDF_UNIT_PRICE = 
		SharedObjects.getEnvironmentValue("TRANSACTION_DEFAULT_PSDF_UNIT_PRICE");
	
	// APPLICABLE_INSTRUCTION_DATA names
	public static String
	SOURCE_ACCOUNT = "SOURCE_ACCOUNT_ID";
	public static String
	DEST_ACCOUNT = "DEST_ACCOUNT_ID";
	public static String
	CASH_VALUE = "CASH";
	public static String
	UNIT_VALUE = "UNITS";
	
	// APPLICABLE INSTRUCTION DATA ID
	public static int EOD_ID_DATA_ID = 13;
	
	// deal types
	public static int
	TRANSACTION_DEAL_TYPE_MID = 1;
	public static int 
	TRANSACTION_DEAL_TYPE_SPREAD = 2;
	
	/** 'Secondary' element types aren't currently listed in the 
	 * REF_ELEMENT_TYPE table, because they don't refer to the shared 
	 * ELEMENT_ID key sequence. The names are enumerated here for
	 * auditing purposes.
	 * 
	 */
	public static enum SecondaryElementType {
	    FundAudit, FundNavAudit, FundPrice, FundPriceApplied, IncomeExpense,
	    IncomeExpenseApplied, ShareClass, ShareClassMovement, FundEOD, ShareClassGroup, ShareClassGroupApplied
	}
	
	/** EOD status ids **/
	
	public static int
	FUND_EOD_STATUS_IN_PROGRESS_ID = 1;
	public static int
	FUND_EOD_STATUS_COMPLETE_ID = 2;
	// status when eod is rerun (superseded)
	public static int
	FUND_EOD_STATUS_NOT_COMPLETE_ID = 3;
	
	
	/**  WARNING MESSAGES **/
	public static String ERROR_MSG_CANNOT_RESTART_COMPLETED_EOD = "Today's EOD is marked as complete and cannot be restarted.";
	public static String ERROR_MSG_CANNOT_START_COMPLETED_EOD = "EOD has already been completed for today.";
	public static String ERROR_MSG_NO_CURRENT_EOD = "No current end of day exists, you need to restart the end of day.";
	public static String ERROR_MSG_NEED_RESTART_EOD = "There is already an end of day in progress.  You will need to restart the end of day to continue.";

	public static String ERROR_MSG_SPECIFIED_RUN_DATE_IN_FUTURE = "The specific end date is in the future. Please enter an earlier end date.";
	public static String ERROR_MSG_SPECIFIED_START_DATE_OVERLAP = "The specific start date overlaps with a previously ran EOD. Please enter a valid start date.";
	public static String ERROR_MSG_SPECIFIED_START_DATE_GAP = "The specific start date contains a gap between the last completed EOD. Please enter a valid start date.";
	public static String ERROR_MSG_SPECIFIED_RUN_DATE_BEFORE_START_DATE = "The specific run date is before the start run date. Please enter a valid start/run date.";
	
	
	/** SEED FUND AND EARLY INVESTOR PARAMETERS **/
	
	public static String
	PSDF_IDENTIFY_SEED_FUNDERS_AND_EARLY_INVESTORS = SharedObjects.getEnvironmentValue("PSDF_IDENTIFY_SEED_FUNDERS_AND_EARLY_INVESTORS");
	
	
	public static String
	PSDF_SEED_FUND_CUTOFF_DATE = 
		 SharedObjects.getEnvironmentValue("PSDF_SEED_FUND_CUTOFF_DATE");
	
	public static String
	PSDF_EARLY_INVESTOR_FUND_TOTAL_CUTOFF = 
		 SharedObjects.getEnvironmentValue("PSDF_EARLY_INVESTOR_FUND_TOTAL_CUTOFF");
	
	public static String
	PSDF_EARLY_INVESTOR_ELEMENT_ATTRIBUTE_ID = SharedObjects.getEnvironmentValue("PSDF_EARLY_INVESTOR_ELEMENT_ATTRIBUTE_ID");

	public static String
	PSDF_SEED_FUND_ELEMENT_ATTRIBUTE_ID = SharedObjects.getEnvironmentValue("PSDF_SEED_FUND_ELEMENT_ATTRIBUTE_ID");
	
	public static Integer
	PSDF_SEED_FUND_AND_EARLY_INVESTOR_SHARE_CLASS_ID = Integer.parseInt(SharedObjects.getEnvironmentValue("PSDF_SEED_FUND_AND_EARLY_INVESTOR_SHARE_CLASS_ID"));

	public static Integer
	PSDF_INTERNAL_ACCOUNT_SHARE_CLASS_ID = Integer.parseInt(SharedObjects.getEnvironmentValue("PSDF_INTERNAL_ACCOUNT_SHARE_CLASS_ID"));
	
	
	public static String
	PSDF_SEED_FUND_EARLY_INVESTOR_OVERRIDE_REASON = "Identified by end of day as seed fund/early investor";
	
	public static String
	INTERNAL_OVERRIDE_REASON = "Identified as internal account";
	
	public static Integer
	PSDF_INSTRUCTION_TYPE_REINVEST_DIVDEND = Integer.parseInt(SharedObjects.getEnvironmentValue("PSDF_INSTRUCTION_TYPE_REINVEST_DIVDEND"));
}
