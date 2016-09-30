package com.ecs.ucm.ccla.experian;

public class IdentityCheckGlobals {
	
	// **** CUSTOM ERROR CODES ****//
	public static final int CCLA_CANNOT_CREATE_PERSON_ERROR_CODE = 10001;
	public static final String CCLA_CANNOT_CREATE_PERSON_MSG = "Could not create record for person";
	
	/// Error codes for validation //////
	public static final String CCLA_VALIDATION_CANNOT_SAVE = "Could not save the results of the validation.  If the problem persists then contact the system administrator";
	public static final String CCLA_VALIDATION_SERVICE = "There was an error running the validation service. Please contact the system administrator if this persists.";
	public static final String CCLA_VALIDATION_DRIVING_LICENCE_FIELDS = "Date of birth, driving licence and first name/last name or driving licence name";
	public static final String CCLA_VALIDATION_MAILSORT_FIELDS = "Utility Mailsort, Utility Mailsort Date";
	public static final String CCLA_VALIDATION_PASSPORT_FIELDS = "Date of birth, gender, first name, last name";

	/// Error codes for authentication
	public static final String CCLA_AUTHENTICATION_MISSING_ADDRESS_MSG = "Please ensure there is an address linked to this person that is marked for experian lookup.";
	public static final String CCLA_AUTHENTICATION_FAILED_MSG = "There was an error whilst authenticating person. If the problem persists then please contact a system administrator.";
	public static final String CCLA_AUTHENTICATION_FAILED_SERVICE_MSG = "Could not run the Experian webservice to authenticate person.";
	public static final String CCLA_AUTHENTICATION_MISSING_ADDRESS_DETAIL = ". Please ensure that flat number, house number or house name fields are filled in.";
	public static final String CCLA_AUTHENTICATION_INCOMPLETE_ADDRESS = "This is usually caused by an incomplete address or name. Please check the name and address with previous documentation or contact the person to confirm the correct details.";
	// number of pass conditions required to make a pass
	public static final int PASS_CONDITION_THRESHOLD = 1;
	
	// These are the happy scores from the CCLA_CS_IDENTITY_CHECK_LOOKUP table
	// put here because they won't change
	public static final int QAS_VALID_SCORE = 2;
	public static final int EXPERIAN_AUTHENTICATED_SCORE=13;
	public static final int PASSPORT_VALIDATION_PASSED_SCORE=7;
	public static final int DRIVING_LICENCE_VALIDATION_PASSED_SCORE=5;
	public static final int UTILITY_MAILSORT_VALIDATION_PASSED_SCORE=11;
	public static final int LEGACY_IVS_CHECKED=17;
	public static final int MANUAL_OVERRIDE=19;
	public static final int CROCKFORDS_VALIDATION_PASSED_SCORE=23;
	public static final int MANUAL_PEP_OVERRIDE=29;
	public static final int MANUAL_PERSON_ACCOUNT_OVERRIDE=31;
	public static final int LEGACY_AML_TRACKER_CHECKED = 89;
	
	// These are the unhappy scores from the CCLA_CS_IDENTITY_CHECK_LOOKUP table
	// put here because they won't change
	public static final int MANUAL_ADDRESS_SCORE = 2;
	public static final int AWAITING_EXPERIAN_AUTHENTICATED_SCORE=5;
	public static final int PASSPORT_VALIDATION_FAILED_SCORE=7;
	public static final int DRIVING_LICENCE_VALIDATION_FAILED_SCORE=71;
	public static final int UTILITY_MAILSORT_VALIDATION_FAILED_SCORE=11;
	public static final int EXPERIAN_FAILED_AUTHENTICATION_SCORE=19;
	public static final int ADDRESS_IS_DUBIOUS = 67;
	public static final int IDENTITY_CHECK_EXPIRED=17;
	public static final int CORE_DATA_CHANGED=79;
	public static final int CROCKFORDS_VALIDATION_FAILED_SCORE=83;
	public static final int RISK_PERSON_ON_PEP_FILE=31;
	public static final int RISK_PEP_OR_SANCTIONS=53;
}
