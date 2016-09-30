package com.ecs.ucm.ccla.data;

/** Stores the names of commonly-used database columns.
 * 
 * @author Tom
 *
 */
public class SharedCols {
	
	// Date/user columns
	public static final String DATE_ADDED = "DATE_ADDED";
	public static final String LAST_UPDATED = "LAST_UPDATED";
	public static final String USER = "USER_ID";
	public static final String LAST_UPDATED_BY = "LAST_UPDATED_BY";
	
	public static final String ELEMENT = "ELEMENT_ID";
	
	public static final String START_DATE = "START_DATE";
	public static final String END_DATE = "END_DATE";
	
	// Element type ID columns
	public static final String ORG = "ORGANISATION_ID";
	public static final String PERSON = "PERSON_ID";
	public static final String ACCOUNT = "ACCOUNT_ID";
	public static final String BANK_ACCOUNT = "BANK_ACCOUNT_ID";
	
	public static final String RELATION = "RELATION_ID";
	public static final String RELATION_NAME = "RELATION_NAME_ID";
	public static final String PROPERTY = "PROPERTY_ID";
	
	public static final String FUND = "FUND_CODE";
	
	public static final String CASH = "CASH";
	public static final String UNITS = "UNITS";
	
	// Comm/instruction columns
	public static final String COMM = "COMM_ID";
	public static final String INSTRUCTION = "INSTRUCTION_ID";
	public static final String DOC = "DOC_ID";
	
	// Campaign columns
	public static final String CAMPAIGN = "CAMPAIGN_ID";
	public static final String CAMPAIGN_ENROLMENT = "CAMPAIGN_ENROLMENT_ID";
	public static final String CAMPAIGN_ACTIVITY = "CAMPAIGN_ACTIVITY_ID";
	public static final String FORM = "FORM_ID";
	
	// UCM Doc Ref columns
	public static final String 		UCM_DOC_NAME = "UCM_DOC_NAME";
	public static final String		UCM_REVISION_ID = "UCM_REVISION_ID";
	public static final String 		GUID = "DOC_GUID";
}
