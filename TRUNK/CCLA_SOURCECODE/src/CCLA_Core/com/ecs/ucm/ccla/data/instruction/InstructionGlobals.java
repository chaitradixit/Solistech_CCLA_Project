package com.ecs.ucm.ccla.data.instruction;

import java.math.BigDecimal;

import intradoc.shared.SharedObjects;

public class InstructionGlobals {
	
	// rounding variables for all divide calculations
	// number of decimal places to use
	public static int decimalPlaces = 10;
	// rounding rule
	public static int roundRule = BigDecimal.ROUND_DOWN;
	
	public static String
	TRANSACTION_ROLLOVER_TIME = 
		 SharedObjects.getEnvironmentValue("TRANSACTION_ROLLOVER_TIME");

	public static String
	PSDF_FUND_CODE = 
		 SharedObjects.getEnvironmentValue("PSDF_FUND_CODE");

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
	
	// deal types
	public static int
	TRANSACTION_DEAL_TYPE_MID = 1;
	public static int 
	TRANSACTION_DEAL_TYPE_SPREAD = 2;
	
}
