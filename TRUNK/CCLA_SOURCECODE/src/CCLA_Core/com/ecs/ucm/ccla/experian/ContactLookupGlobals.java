package com.ecs.ucm.ccla.experian;

import com.ecs.ucm.ccla.data.Globals;

public class ContactLookupGlobals {
	
	/************** ERROR MESSAGES *****************/
	public static String CONTACT_LOOKUP_QAS_DOWN_ERROR_MSG = "Cannot use QAS search at this time, address will have to be manually entered. If the problem persists then contact a system administrator.";
	
	/******** row limit *************/
	public static String CONTACT_LOOKUP_ROW_LIMIT = " AND ROWNUM <= " + Globals.AUTOSEARCH_PERSON_NUMROWS;

	/************* base query for people, orgs and accounts **********/
	
	public static String CONTACT_PERSON_QRY_BASE = "SELECT per.person_id FROM person per";
	public static String CONTACT_ORG_QRY_BASE = "SELECT org.organisation_id FROM organisation org ";
	public static String CONTACT_ACCOUNT_QRY_BASE = "SELECT acc.account_id FROM account acc ";
	
	/***************** QUERIES FOR UNIQUE IDENTIFIERS*****************/
	
	public static String 
		CONTACT_UNIQUE_ID_PERSON_QRY = " UPPER(per.person_account_code) like ";
	public static String 
		CONTACT_UNIQUE_ID_ORG_QRY = " UPPER(org.org_account_code) like ";

	/*
	public static String
		CONTACT_ORG_IDENTIFIER_QRY 
			= " SELECT orgref.organisation_id FROM org_references orgref ";
	public static String
	CONTACT_ORG_IDENTIFIER_VALUE_WHERE_CLAUSE  = 
	" UPPER(orgref.identifier_value) = ";
	public static String
		CONTACT_ORG_IDENTIFIER_WHERE_CLAUSE 
			= " orgref.org_identifier_id = ";
	*/
	
	public static String CONTACT_ORG_IDENTIFIER_QRY = 
		" SELECT elemid.element_id AS ORGANISATION_ID " +
		"FROM ELEMENT_IDENTIFIERS_APPLIED elemid ";
	public static String CONTACT_ORG_IDENTIFIER_VALUE_WHERE_CLAUSE = 
		" UPPER(elemid.identifier_value) = ";
	public static String CONTACT_ORG_IDENTIFIER_WHERE_CLAUSE = 
		" elemid.element_identifier_id = ";

	public static String
		CONTACT_CAMPAIGN_NO_ORG_QRY_BASE = "SELECT organisation_id from ccla_process " ;
	public static String
		CONTACT_CAMPAIGN_NO_PERSON_QRY_BASE = "SELECT person_id from ccla_process " ;
		public static String
		CONTACT_CAMPAIGN_NO_QRY = " process_id = ";
	
	/***************** QUERIES FOR NAMES ************************/
	public static String
		CONTACT_NAME_PERSON_QRY = " UPPER(full_name) like ";
	public static String
		CONTACT_NAME_ORG_QRY = " UPPER(organisation_name) like ";	
	public static String
		CONTACT_POSTCODE_PERSON_QRY = " ";
	
	/****** QUERIES FOR EMAIL ***************/
	
	public static String CONTACT_PERSON_EMAIL_QRY_IN = 
		"  person_id IN ";
	
	public static String 
		CONTACT_PERSON_EMAIL_QRY_BASE 
			= " select p.person_id from PERSON p "+
			"INNER JOIN CONTACT_POINT cp ON (p.PERSON_ID = cp.ELEMENT_ID) "+
			"INNER JOIN REF_CONTACT_SUBMETHOD rcs on (cp.SUBMETHOD_ID = rcs.SUBMETHOD_ID) "+
			"INNER JOIN REF_CONTACT_METHOD rcm on (rcs.METHOD_ID = rcm.METHOD_ID) "+
			"WHERE rcm.METHOD_ID=3 AND ";
	
	public static String CONTACT_PERSON_EMAIL_QRY = " cp.VALUE LIKE ";
	
	/****** QUERIES FOR ADDRESSES ***************/
	
	public static String CONTACT_PERSON_ADDRESS_QRY_IN = 
		"  person_id IN ";
	public static String 
		CONTACT_PERSON_ADDRESS_QRY_BASE = " SELECT PERSON.person_id from PERSON "+
			"INNER JOIN contact_point on (PERSON.person_id = contact_point.element_id) "+
			"INNER JOIN address ON (contact_point.address_id = address.address_id) WHERE ";
				
	public static String CONTACT_ADDRESS_POSTCODE_QRY = " UPPER(address.POSTCODE) LIKE ";
	public static String CONTACT_ADDRESS_FLAT_QRY = " UPPER(address.FLAT) LIKE ";
	public static String CONTACT_ADDRESS_HOUSENAME_QRY = " UPPER(address.HOUSENAME) LIKE ";
	public static String CONTACT_ADDRESS_HOUSENUMBER_QRY = " UPPER(address.HOUSENUMBER) LIKE ";
	
//	public static String
//		CONTACT_PERSON_ADDRESS_QRY_JOIN 
//				= " AND address.address_id = contact_point.address_id AND PERSON.person_id = contact_point.element_id ";
	
	public static String CONTACT_ORG_ADDRESS_QRY_IN = 
		"  organisation_id IN ";
	public static String 
		CONTACT_ORG_ADDRESS_QRY_BASE 
				= " select ORG.organisation_id from ORGANISATION org " +
				"INNER JOIN contact_point on (org.organisation_id = contact_point.element_id) "+
				"INNER JOIN address ON (contact_point.address_id = address.address_id) WHERE ";

//	public static String
//	CONTACT_ORG_ADDRESS_QRY_JOIN 
//				= " AND address.address_id = contact_point.address_id AND ORG.organisation_id = contact_point.element_id ";

	/************** QUERY FOR CORRESPONDENT NUMBER ************/
	public static String
		CONTACT_CORRRESPONDENT_QRY = 
			" LEFT JOIN person_aurora_map map on (per.person_id = map.person_id) where map.corr_id = ";
	
	/************** QUERY FOR CLIENT NUMBER ************/
	public static String
		CONTACT_CLIENT_NUMBER_QRY = 
			" SELECT organisation_id from client_aurora_map where client_number = ";	
	
	/*************** QUERies FOR ACCOUNTS **********************/
	
	// OLD STYLE CLIENT_NO/ACCOUNT
	public static String
	CONTACT_ACCOUNT_CLIENT_OLD_QRY_START =
		" select acc.account_id from account acc inner join relations rel on (acc.account_id = rel.element_id2)  ";
	public static String
	CONTACT_ACCOUNT_CLIENT_OLD_QRY_JOIN =
		" select organisation_id from client_aurora_map where client_number =  ";
	public static String	
	CONTACT_ACCOUNT_CLIENT_OLD_QRY_JOIN_ON =
		" client on (rel.element_id1 = client.organisation_id) ";
	public static String
	CONTACT_ACCOUNT_CLIENT_OLD_QRY_WHERE =
		" acc.accountnumber = ";
	
	// NEW STYLE UNIQUE IDENTIFIER PLUS NEW ACCOUNT NO
	public static String
	CONTACT_ACCOUNT_CODE_QRY =
		" acc.ACCOUNTNUMBER = ";	
	public static String 
		CONTACT_ACCOUNT_NEW_QRY_START = 
			"select acc.account_id from account acc inner join relations rel on (acc.account_id = rel.element_id2) ";
	public static String 
	CONTACT_ACCOUNT_NEW_QRY_JOIN = 
		" client on (rel.element_id1 = client.organisation_id) ";	
	public static String 
	CONTACT_ACCOUNT_FUND_QUERY = 
		" acc.FUND_CODE = ";		
}
