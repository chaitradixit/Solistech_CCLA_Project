package com.ecs.ucm.ccla.experian;

import intradoc.shared.SharedObjects;

import com.experian.webservice.EngineType;
import com.experian.webservice.QAConfigType;

public class ExperianGlobals {
	/** Country dataset passed to Experian lookup services */
	public static String COUNTRY_DATASET;
	public static String COUNTRY_DATASET_BACKUP;
	
	/** Layout used when fetching address data */
	public static String PRO_WEB_LAYOUT;
	
	/** Layout used when fetching authentication data */
	public static String AUTH_PRO_LAYOUT;
	
	
	public static final int IDENTITY_CHECK_DAYS_RESULT_VALID;
	
	
	public static final int IDENTITY_CHECK_DAYS_SCORE_VALID;
	
	/** Names of configuration variables stored in the SYSTEM_CONFIG_VAR table.
	 * 
	 *  These can be accessed/updated via the SystemConfigVarCache.
	 *  
	 * @author Tom
	 *
	 */
	public static enum ConfigVarNames {
		/** No. of days which a cached Identity Check result is valid for. Results
		are automatically recalculated when this period has exceeded.
		*/
		IdentityCheck_DaysResultValid,
		
		/** No. of days which Identity Check scores are valid for. When this period
		expires, a flag is set to indicate the score has expired and will need
		to be re-checked.
		*/
		IdentityCheck_DaysCheckValid,
		
		/** Fixed Expiry Date. This will always be allocated as the Expiry Date value
		 *  if it is non-null.
		 *  
		 */
		IdentityCheck_FixedExpiryDate
	}
	
	/** Location of Experian config file */
	public static final String CONFIG_FILE_PATH = 
	 "\\\\10.15.4.12\\d$\\Program Files\\QAS\\Authenticate Pro Web 1.15\\Qawserve.ini";
	
	/** Engine used for single-line address lookup with 'flattened'
	 *  results (i.e. no tree-based results) */
	public static EngineType SINGLE_LINE_SEARCH_ENGINE_FLATTENED = 
	 new EngineType("Singleline");
	
	/** Engine used for single-line address lookup with tree-based
	 *  results */
	public static EngineType SINGLE_LINE_SEARCH_ENGINE_TREE = 
	 new EngineType("Singleline");
	
	/** Engine used for identity authentication */
	public static EngineType AUTHENTICATE_ENGINE = 
	 new EngineType("Authenticate");
	
	/** Configuration object passed to Experian lookup services */
	public static QAConfigType EXPERIAN_CONFIG = new QAConfigType(CONFIG_FILE_PATH, null);
	
	/** Value returned when authentication is successful **/
	public static String SUCCESS_CODE ;
	
	/** Value returned when authentication is not successful **/
	public static String FAILURE_CODE = "NA00" ;
	
	/** Value returned when authentication returns a risk code **/
	public static String REFER_CODE = "RA00" ;
	
	/** No. of milliseconds per day. Used to generate new expiry dates */
	public static final long MILLIS_PER_DAY = 24 * 60 * 60 * 1000;
	
	/** List of mandatory authentication parameters which must be
	 *  used in conjunction with the Authenticate engine
	 */
	public static final String[] authParams = {
		"CTRL_SEARCHCONSENT",
		"NAME_TITLE",
		
		"NAME_FORENAME",
		"NAME_SURNAME",
		"NAME_DATEOFBIRTH",
		
		"ADDR_FLAT",
		"ADDR_HOUSENAME",
		"ADDR_HOUSENUMBER",
		"ADDR_STREET",
		"ADDR_DISTRICT",
		"ADDR_TOWN",
		"ADDR_COUNTY",
		"ADDR_POSTCODE"
	};

	/** List of field names in the resultset returned when doing an address lookup
	 * in QAS
	 */
	public static final String[] addressCols = new String[] {
			"FLAT","HOUSENAME","HOUSENUMBER",
			"STREET","DISTRICT","CITY","COUNTY",
			"POSTCODE","TITLE","FIRST_NAME",
			"MIDDLE_NAME","LAST_NAME"
	};
	static {
		COUNTRY_DATASET = 
		 SharedObjects.getEnvironmentValue("EXPERIAN_CountryDataset");
		
		PRO_WEB_LAYOUT = 
		 SharedObjects.getEnvironmentValue("EXPERIAN_ProWebLayout");
		
		AUTH_PRO_LAYOUT =
		 SharedObjects.getEnvironmentValue("EXPERIAN_AuthenticateProLayout");
		
		// Set attributes for engines
		SINGLE_LINE_SEARCH_ENGINE_FLATTENED.setFlatten(true);
		SINGLE_LINE_SEARCH_ENGINE_TREE.setFlatten(false);
		AUTHENTICATE_ENGINE.setFlatten(false);
		
		SUCCESS_CODE =
		 SharedObjects.getEnvironmentValue("EXPERIAN_SuccessCode");
		
		IDENTITY_CHECK_DAYS_RESULT_VALID = Integer.parseInt(
		 SharedObjects.getEnvironmentValue("IDENTITY_CHECK_DaysResultValid"));
		
		IDENTITY_CHECK_DAYS_SCORE_VALID = Integer.parseInt(
		 SharedObjects.getEnvironmentValue("IDENTITY_CHECK_DaysScoreValid"));
	}

	// **** CUSTOM ERROR CODES ****//
	public static final int CCLA_CANNOT_SEARCH_ERROR_CODE = 10001;
	public static final String CCLA_CANNOT_SEARCH_ERROR_MSG = "Address Search is not available at the moment.  If this persists then please contact a system administrator.";
	public static final int CCLA_SERVICE_CALL_FAILED_ERROR_CODE = 10002;
	public static final String CCLA_SERVICE_CALL_FAILED_ERROR_MSG = "Failed to call Experian web service. If this persists then please contact a system administrator.";

	/** List of friendly name of parameters passed to driving licence
	 * validation method.  This is used to create the error message the user
	 * sees on the page
	 */
	public static final String[] validateDrivingLicenceParameters = {
		"Driving Licence Number",
		"Date of Birth",		
		"Name on Licence/First Name Second Name",
		"Gender"
	};
	/** List of friendly name of parameters passed to passport
	 * validation method.  This is used to create the error message the user
	 * sees on the page DOB, PASSPORT_NUMBER, GENDER
	 */
	public static final String[] validatePassportParameters = {
		"Date of Birth",		
		"Passport Number",
		"Gender"
	};
	/** List of friendly name of parameters passed to passport
	 * validation method.  This is used to create the error message the user
	 * sees on the page UTILITY_MAILSORT_DATE, UTILITY_MAILSORT, postCode
	 */
	public static final String[] validateMailsortParameters = {
		"Utility Mailsort Date",		
		"Utility Mailsort",
		"Postcode"
	};
}
