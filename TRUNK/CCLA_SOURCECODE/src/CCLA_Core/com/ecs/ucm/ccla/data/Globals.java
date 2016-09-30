package com.ecs.ucm.ccla.data;

import com.ecs.utils.StringUtils;

import intradoc.server.Service;
import intradoc.shared.SharedObjects;

public class Globals extends Service {
	
	// Result limits for auto-search queries
	public static final int DEFAULT_NUMROWS = 10;
	
	//Extended Account Number String 
	public static final int DEFAULT_ACCOUNT_NUMBER_PADDED_LENGTH = 8;
	public static final char DEFAULT_ACCOUNT_NUMBER_PADDED_CHAR = '0';
	

	public static int ACCOUNT_NUMBER_PADDED_LENGTH;
	public static char ACCOUNT_NUMBER_PADDED_CHAR;
	
	public static int AUTOSEARCH_ENTITY_NUMROWS;
	public static int AUTOSEARCH_PERSON_NUMROWS;
	public static int AUTOSEARCH_ACCOUNT_NUMROWS;
	public static int AUTOSEARCH_BANK_ACCOUNT_NUMROWS;
	
	public static boolean USE_STORAGE_RULES;
	public static String STORAGE_RULE_NAME = "";
	
	/** The 'audit name' which is passed into Static Data audit events
	*/
	public static final String STATIC_DATA_AUDIT_PROFILE = "SDAUDIT";
	
	/** The available 'audit actions' which can be passed into Static
	 *  Data audit events
	 * 
	 */
	public static enum AuditActions {
		ADD,
		UPDATE,
		DELETE
	}
	
	/** List of characters that JSON doesn't like - these characters
	 * need to be stripped out before passing to JSON
	 */
	public static final String[] BAD_JSON_CHARS = {
		"'",
		"\"",		
		","
	};

	
	static {
		AUTOSEARCH_ENTITY_NUMROWS =
		 SharedObjects.getEnvironmentInt
		 ("CCLA_CS_AutoSearch_EntityNumRows", DEFAULT_NUMROWS);
		
		AUTOSEARCH_PERSON_NUMROWS =
		 SharedObjects.getEnvironmentInt
		 ("CCLA_CS_AutoSearch_PersonNumRows", DEFAULT_NUMROWS);
		
		AUTOSEARCH_ACCOUNT_NUMROWS =
		 SharedObjects.getEnvironmentInt
		 ("CCLA_CS_AutoSearch_AccountNumRows", DEFAULT_NUMROWS);
		
		AUTOSEARCH_BANK_ACCOUNT_NUMROWS =
		 SharedObjects.getEnvironmentInt
		 ("CCLA_CS_AutoSearch_BankAccountNumRows", DEFAULT_NUMROWS);
		
		ACCOUNT_NUMBER_PADDED_LENGTH =
			 SharedObjects.getEnvironmentInt
			 ("ACCOUNT_NUMBER_PADDED_LENGTH", DEFAULT_ACCOUNT_NUMBER_PADDED_LENGTH);

		if (!StringUtils.stringIsBlank(
				SharedObjects.getEnvironmentValue("ACCOUNT_NUMBER_PADDED_CHAR"))){
			ACCOUNT_NUMBER_PADDED_CHAR = SharedObjects.getEnvironmentValue("ACCOUNT_NUMBER_PADDED_CHAR").charAt(0);
		} else {
			ACCOUNT_NUMBER_PADDED_CHAR = DEFAULT_ACCOUNT_NUMBER_PADDED_CHAR;
		}
		
		
		USE_STORAGE_RULES = SharedObjects.getEnvValueAsBoolean("USE_STORAGE_RULES", false);
		if (!StringUtils.stringIsBlank(
				SharedObjects.getEnvironmentValue("STORAGE_RULE_NAME"))){
			STORAGE_RULE_NAME = SharedObjects.getEnvironmentValue("STORAGE_RULE_NAME");
		} 
		
	}
	
}
