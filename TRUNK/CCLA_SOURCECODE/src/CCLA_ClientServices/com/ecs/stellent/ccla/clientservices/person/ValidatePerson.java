package com.ecs.stellent.ccla.clientservices.person;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.xml.rpc.soap.SOAPFaultException;

import com.ecs.stellent.ccla.clientservices.ValidatorPlusWebServiceHandler;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Address;
import com.ecs.ucm.ccla.data.Contact;
import com.ecs.ucm.ccla.data.Gender;
import com.ecs.ucm.ccla.data.IdentityCheckAudit;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.PersonTitle;
import com.ecs.ucm.ccla.experian.AuthenticationScoreUtils;
import com.ecs.ucm.ccla.experian.ExperianGlobals;
import com.ecs.ucm.ccla.experian.IdentityCheckGlobals;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;
import com.experian.validator.webservice.DrivingLicenceResults;
import com.experian.validator.webservice.MailsortResults;
import com.experian.validator.webservice.MicronumberResults;
import com.experian.validator.webservice.PassportNumberResults;

public class ValidatePerson extends ValidatorPlusWebServiceHandler {
	 
	private String DRIVING_LICENCE = "";
	private String PASSPORT_NUMBER = "";
	private String UTILITY_MAILSORT = "";
	private String UTILITY_MAILSORT_DATE = "";
	private String FIRST_NAME = "";
	private String MIDDLE_NAME = "";
	private String LAST_NAME = "";
	private String GENDER = "";
	private String GENDER_ID = "";
	private String TITLE_ID = "";
	private String TITLE = "";
	private String DOB = "";
	private String FULLNAME = "";
	private int CROCKFORDS_ID;
	private String CROCKFORDS_VALUE="";
	private boolean isPassDrivingLicence = false;
	private boolean tryDrivingLicence = false;
	private boolean isPassPassport = false;
	private boolean tryPassport = false;
	private boolean isPassMailsort = false;
	private boolean isPassCrockfords = false;
	private boolean tryMailsort = false;
	private int personId;
	private boolean hasValidationRecord = false;
	private String FullDrivingErrorMessage="";
	private String FullPassportErrorMessage="";
	private String FullMailsortErrorMessage="";
	private String FullCrockfordsErrorMessage="";
	
	/** TODO rewrite this entire fucking thing, its an abomination. 
	 *  
	 *  Have no idea why this class extends ValidatorPlusWebServiceHandler? Move this
	 *  all into a proper Service method. IdentityCheckService probably best.
	 *  
	 *  Don't want to see a single query being called directly. Need to create DAOs for 
	 *  IDENTITY_VALIDATION_CHECK and IDENTITY_CHECK_AUDIT tables.
	 * 
	 * @throws ServiceException
	 */
	public void doValidate() throws ServiceException
	{

		FWFacade fw;
		try {
			fw = CCLAUtils.getFacade(this.m_workspace, true); 
			String username = m_userData.m_name;
			initValues(m_binder, fw);		
		Log.debug("hasValidationRecord is " + hasValidationRecord);
		String auditString = TITLE + "|" + FIRST_NAME + "|" + MIDDLE_NAME + "|" + LAST_NAME + "|" + GENDER + "|" + DOB + "|"
				+ DRIVING_LICENCE + "|" + FULLNAME +"|" + PASSPORT_NUMBER + "|" + UTILITY_MAILSORT + "|" + CROCKFORDS_ID + "|" + CROCKFORDS_VALUE;
		Log.debug("validation authentication audit string is " + auditString);
		if ((DOB.length()>0) 
				&& (FULLNAME.length()>0 && DRIVING_LICENCE.length()>0))
		{
			doValidateLicence(fw);
		}
		else if (DRIVING_LICENCE.length()>0) {
			// IN THIS CASE SOME DATA IS MISSING SO DRIVING LICENCE CANNOT BE VALIDATED
			tryDrivingLicence = true;
			FullDrivingErrorMessage = "Please ensure the following fields are entered:" + IdentityCheckGlobals.CCLA_VALIDATION_DRIVING_LICENCE_FIELDS;
			if (!hasValidationRecord)
			{
				fw.execute("qClientServices_InsertValidationRecord", m_binder);
				hasValidationRecord=true;
			}
			m_binder.putLocal("result", "");
			fw.execute("qClientServices_UpdateLicenceValidation", m_binder);
		} else if (StringUtils.stringIsBlank(DRIVING_LICENCE))
		{
			// IN THIS CASE DRIVING_LICENCE SHOULD BE BLANKED OUT
			DataBinder binder = new DataBinder();
			binder.putLocal("DRIVING_LICENCE", "");
			CCLAUtils.addQueryIntParamToBinder(binder, "PERSON_ID", personId);
			fw.execute("qClientServices_UpdateLicenceValue", binder);
		}
		
		if ((DOB.length()>0 
				&& FIRST_NAME.length()>0 && LAST_NAME.length()>0 && PASSPORT_NUMBER.length()>0))
		{
			doValidatePassport(fw);
			
		} else if (PASSPORT_NUMBER.length()>0)
		{
			// IN THIS CASE SOME REQUIRED FIELDS ARE MISSING SO PASSPORT CANNNOT BE VALIDATED
			FullPassportErrorMessage = "Please ensure the following fields are entered:" + IdentityCheckGlobals.CCLA_VALIDATION_PASSPORT_FIELDS;
			if (!hasValidationRecord)
			{
				fw.execute("qClientServices_InsertValidationRecord", m_binder);
				hasValidationRecord=true;
			}
			m_binder.putLocal("result", "");
			fw.execute("qClientServices_UpdatePassportValidation", m_binder);
		} else if (StringUtils.stringIsBlank(PASSPORT_NUMBER))
		{
			// IN THIS CASE PASSPORT NUMBER NEEDS TO BE BLANKED OUT
			DataBinder binder = new DataBinder();
			binder.putLocal("PASSPORT_NUMBER", "");
			CCLAUtils.addQueryIntParamToBinder(binder, "PERSON_ID", personId);
			fw.execute("qClientServices_UpdatePassportNumber", binder);
		}
		
		if (UTILITY_MAILSORT.length()>0)
		{
			doValidateMailsort(fw, personId);
		} else if (StringUtils.stringIsBlank(UTILITY_MAILSORT))
		{
			// IN THIS CASE BLANK OUT MAILSORT VALUES
			DataBinder binder = new DataBinder();
			binder.putLocal("UTILITY_MAILSORT", "");
			binder.putLocal("UTILITY_MAILSORT_DATE", "");
			CCLAUtils.addQueryIntParamToBinder(binder, "PERSON_ID", personId);
			fw.execute("qClientServices_UpdateUtilityMailsortValue", binder);
		} 
		
		if (CROCKFORDS_ID !=0 && !StringUtils.stringIsBlank(CROCKFORDS_VALUE))
		{
			if (!hasValidationRecord)
			{
				fw.execute("qClientServices_InsertValidationRecord", m_binder);
				hasValidationRecord=true;
			}
			isPassCrockfords = true;
			DataBinder binder = new DataBinder();
			CCLAUtils.addQueryIntParamToBinder(binder, "CROCKFORDS_VALID", 1);
			CCLAUtils.addQueryIntParamToBinder(binder, "CROCKFORDS_ID", CROCKFORDS_ID);
			binder.putLocal("CROCKFORDS_VALUE", CROCKFORDS_VALUE);
			CCLAUtils.addQueryIntParamToBinder(binder, "PERSON_ID", personId);
			fw.execute("qClientServices_UpdateCrockfordsValue", binder);
			Date expDate = AuthenticationScoreUtils.getNewExpiryDate();
			m_binder.putLocalDate("EXPIRY_DATE", expDate);
			fw.execute("qClientServices_SetExpiryDate_Authenticate", m_binder);
			// clear any core data update flag
			CCLAUtils.addQueryIntParamToBinder(m_binder, "PERSON_ID", personId);
			fw.execute("qClientServices_ClearCoreDataAuthenticateRecord", m_binder);	
			AuthenticationScoreUtils.updateIdentityCheck(personId,fw);
			
		} else if (CROCKFORDS_ID != 0 || !StringUtils.stringIsBlank(CROCKFORDS_VALUE))
		{
			if (!hasValidationRecord)
			{
				fw.execute("qClientServices_InsertValidationRecord", m_binder);
				hasValidationRecord=true;
			}
			FullCrockfordsErrorMessage = "Please ensure that both Crockfords values are entered.";
			DataBinder binder = new DataBinder();
			Log.debug("saving values " + CROCKFORDS_ID + "," + CROCKFORDS_VALUE);
			binder.putLocal("CROCKFORDS_VALID", "");
			CCLAUtils.addQueryIntParamToBinder(binder, "CROCKFORDS_ID", CROCKFORDS_ID);
			binder.putLocal("CROCKFORDS_VALUE", CROCKFORDS_VALUE);
			CCLAUtils.addQueryIntParamToBinder(binder, "PERSON_ID", personId);
			fw.execute("qClientServices_UpdateCrockfordsValue", binder);
		} else {
			if (!hasValidationRecord)
			{
				fw.execute("qClientServices_InsertValidationRecord", m_binder);
				hasValidationRecord=true;
			}
			DataBinder binder = new DataBinder();
			binder.putLocal("CROCKFORDS_VALID", "");
			CCLAUtils.addQueryIntParamToBinder(binder, "CROCKFORDS_ID", 0);
			binder.putLocal("CROCKFORDS_VALUE", "");
			CCLAUtils.addQueryIntParamToBinder(binder, "PERSON_ID", personId);
			fw.execute("qClientServices_UpdateCrockfordsValue", binder);
			Date expDate = AuthenticationScoreUtils.getNewExpiryDate();
			m_binder.putLocalDate("EXPIRY_DATE", expDate);
			fw.execute("qClientServices_SetExpiryDate_Authenticate", m_binder);
			// clear any core data update flag
			CCLAUtils.addQueryIntParamToBinder(m_binder, "PERSON_ID", personId);
			fw.execute("qClientServices_ClearCoreDataAuthenticateRecord", m_binder);	
			AuthenticationScoreUtils.updateIdentityCheck(personId,fw);
		}
		
		String decision = "crockfords:" + isPassCrockfords + "|";
		if (tryDrivingLicence)
			decision = "licence:" + isPassDrivingLicence + "|";
		if (tryPassport)
			decision = decision + "passport:" + isPassPassport + "|";
		if (tryMailsort)
			decision = decision + "mailsort:" + isPassMailsort;

		/*
		AuthenticateAuditRecord.insertAuthenticateAuditRecord
		 ("Validate", Integer.toString(personId), auditString,decision , 
		  "", m_userData.m_name, m_binder, fw);
		*/
		
		IdentityCheckAudit.add(personId, IdentityCheckAudit.CheckType.Validate, 
		 m_userData.m_name, auditString, decision, null, fw);
		
		} catch (NumberFormatException ne) {
			Log.error("doValidate NumberFormatException:" + ne.getMessage());
			Log.error("doValidate NumberFormatException:" + ne.getCause().getMessage());
			throw new ServiceException(ne.getMessage());
		} catch (DataException de) {
			Log.error("doValidate DataException:" + de.getMessage());
			throw new ServiceException(de.getMessage());
		}
		
		String validationFailed = "";
		if (FullDrivingErrorMessage.length() > 0 || 
				FullPassportErrorMessage.length() > 0 || 
				FullMailsortErrorMessage.length() > 0 ||
				FullCrockfordsErrorMessage.length() >0){
			validationFailed="1";
		}
		
		String redirectUrl = m_binder.getLocal("RedirectUrl") 
		+ "&FullDrivingErrorMessage=" + FullDrivingErrorMessage
		+ "&FullPassportErrorMessage=" + FullPassportErrorMessage
		+ "&FullMailsortErrorMessage=" + FullMailsortErrorMessage
		+ "&FullCrockfordsErrorMessage=" + FullCrockfordsErrorMessage
		+ "&validationFailed=" + validationFailed 
		+ "#personValidation";

		m_binder.putLocal("RedirectUrl", redirectUrl);
	}
	
	public String getValidationFullName()
	{
		String fullName = "";
		fullName = TITLE + " " + FIRST_NAME + " " + LAST_NAME;
		
		if (MIDDLE_NAME.length()>0)
		fullName = TITLE + " " + FIRST_NAME + " " + MIDDLE_NAME + " " + LAST_NAME;
		
		return fullName;
	}
	
	
	/**
	 *  Validates the driving license number against passed inputs.
	 *  Does not perform an external lookup of any sort.
	 * 
	 * @throws RemoteException
	 * @throws ServiceException
	 */
	public void validateDrivingLicence(FWFacade fw) 
	 throws RemoteException, ServiceException {
	
		try {
			Log.debug("using FULLNAME:" + FULLNAME);
			Log.debug("using DOB:" + DOB);
			
			DrivingLicenceResults result = getValidatorPlusWS().doValidateDrivingLicence
			 (DRIVING_LICENCE, DOB, FULLNAME, GENDER);
			CCLAUtils.addQueryIntParamToBinder(m_binder, "PERSON_ID", personId);
			// Overall result
			boolean isResult		= result.isResult();
			Log.debug("licence result is " + isResult);
			
			try {
				if (!isResult) {
					m_binder.putLocal("result", "0");

					if (!hasValidationRecord)
					{
						fw.execute("qClientServices_InsertValidationRecord", m_binder);
						hasValidationRecord=true;
					}
					Log.debug("RUNNING UPDATE");
					fw.execute("qClientServices_UpdateLicenceValidation", m_binder);
					
					boolean isDobTest 		= result.isDobTest();
					boolean isInitialsTest 	= result.isInitialsTest();
					boolean isSurnameTest	= result.isSurnameTest();
					
					m_binder.putLocal("dobTest", Boolean.toString(isDobTest));
					m_binder.putLocal("initialsTest", Boolean.toString(isInitialsTest));
					m_binder.putLocal("surnameTest", Boolean.toString(isSurnameTest));
					
				} else {
					m_binder.putLocal("result", "1");
					isPassDrivingLicence = true;
					if (!hasValidationRecord)
					{
						fw.execute("qClientServices_InsertValidationRecord", m_binder);
						hasValidationRecord=true;
					}
					Log.debug("RUNNING UPDATE");
					fw.execute("qClientServices_UpdateLicenceValidation", m_binder);
					Date expDate = AuthenticationScoreUtils.getNewExpiryDate();
					m_binder.putLocalDate("EXPIRY_DATE", expDate);
					fw.execute("qClientServices_SetExpiryDate_Authenticate", m_binder);
					// clear any core data update flag
					CCLAUtils.addQueryIntParamToBinder(m_binder, "PERSON_ID", personId);
					fw.execute("qClientServices_ClearCoreDataAuthenticateRecord", m_binder);					
				}
				// recalculate score
				AuthenticationScoreUtils.updateIdentityCheck(personId,fw);
			} catch (DataException e) {
				Log.error("Error whilst validating:" + e.getMessage());
				FullDrivingErrorMessage = 
				 FullDrivingErrorMessage + IdentityCheckGlobals.CCLA_VALIDATION_CANNOT_SAVE;
			}
			
		} catch (SOAPFaultException se) {
			Log.error("Failed to call doValidateDrivingLicense", se);
			FullDrivingErrorMessage = FullDrivingErrorMessage + IdentityCheckGlobals.CCLA_VALIDATION_CANNOT_SAVE;
			throw new ServiceException(se);
		}
	}
	
	// Performs lookup against database of mailsort codes.
	//
	// Functions as an address check.
	public void validateMailsortNumber(FWFacade fw) throws RemoteException, ServiceException {
			
		// Postcode (not fussy about formatting)
		String postCode 	= m_binder.getLocal("POSTCODE");
		Log.debug("Validating with values:" + UTILITY_MAILSORT_DATE +","+ UTILITY_MAILSORT +"," +postCode);
		try {
        
			MailsortResults result = getValidatorPlusWS().doValidateMailsort
			 (UTILITY_MAILSORT_DATE, UTILITY_MAILSORT, postCode);
	
			// Overall result
			boolean isResult		= result.isResult();
			Log.debug("mailsort_result is " + isResult);
			if (!isResult) {				
				m_binder.putLocal("result", "FAIL");				
				// Essentially the same as isResult
				boolean isFullMatch = result.isFullMatchTest();				
				// Checks if the mail sort code exists in the database
				boolean isMailSortRangeTest	= result.isMailsortRangeTest();
				
				m_binder.putLocal("fullMatchTest", Boolean.toString(isFullMatch));
				m_binder.putLocal("mailSortRangeTest", Boolean.toString(isMailSortRangeTest));
				m_binder.putLocal("result", "0");
				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");				 
		          try {
					Date dateMailSort = df.parse(UTILITY_MAILSORT_DATE);
					m_binder.putLocalDate("UTILITY_MAILSORT_DATE", dateMailSort);
		          } catch (ParseException e)
		          {
		        	Log.error("UNABLE TO PARSE DATE IN UTILITYMAILSORT");
		        	throw new ServiceException("Cannot parse date for utility mailsort check");
		          }
				m_binder.putLocal("UTILITY_MAILSORT", UTILITY_MAILSORT);
				if (!hasValidationRecord)
				{
					fw.execute("qClientServices_InsertValidationRecord", m_binder);
					hasValidationRecord=true;
				}
				fw.execute("qClientServices_UpdateMailsortValidation", m_binder);
				AuthenticationScoreUtils.updateIdentityCheck(personId,fw);
			} else {
				m_binder.putLocal("mailsort_result", "PASS");
				m_binder.putLocal("result", "1");
				isPassMailsort=true;
				m_binder.putLocal("UTILITY_MAILSORT_DATE", UTILITY_MAILSORT_DATE);
				m_binder.putLocal("UTILITY_MAILSORT", UTILITY_MAILSORT);
				if (!hasValidationRecord)
				{
					fw.execute("qClientServices_InsertValidationRecord", m_binder);
					hasValidationRecord=true;
				}
				fw.execute("qClientServices_UpdateMailsortValidation", m_binder);
				Date expDate = AuthenticationScoreUtils.getNewExpiryDate();
				m_binder.putLocalDate("EXPIRY_DATE", expDate);
				fw.execute("qClientServices_SetExpiryDate_Authenticate", m_binder);	
				// clear any core data update flag
				CCLAUtils.addQueryIntParamToBinder(m_binder, "PERSON_ID", personId);
				fw.execute("qClientServices_ClearCoreDataAuthenticateRecord", m_binder);
				// recalculate score
				AuthenticationScoreUtils.updateIdentityCheck(personId,fw);
			}
		} catch (DataException e) {
			Log.error("Error whilst validating:" + e.getMessage());
			FullMailsortErrorMessage = FullMailsortErrorMessage + IdentityCheckGlobals.CCLA_VALIDATION_CANNOT_SAVE;
			
		} catch (SOAPFaultException se) {
			Log.error("Failed to call doValidateMailsortNumber", se);
			FullMailsortErrorMessage = FullMailsortErrorMessage + IdentityCheckGlobals.CCLA_VALIDATION_CANNOT_SAVE;
			throw new ServiceException(se);
		}
	}
	
	// Checks validity of passport number against passed data
	public void validatePassportNumber(FWFacade fw) throws ServiceException, RemoteException {
		
		String isLongNumberStr	= m_binder.getLocal("isLongNumber");
		Log.debug("isLongNumberStr is " + isLongNumberStr);
		// Should always be Yes. 'Short number' validation will only check the
		// portion of the passport number that appears before the <<< characters.
		boolean isLongNumber 	=  !StringUtils.stringIsBlank(isLongNumberStr);
		
		try {
			Log.debug("passing dob:" + DOB + ", PASSPORT_NUMBER:" + PASSPORT_NUMBER + ", GENDER:" + GENDER
					+ ",isLongNumber:" + isLongNumber);
			PassportNumberResults result = getValidatorPlusWS().doValidatePassport
			 (DOB, PASSPORT_NUMBER, GENDER, isLongNumber);
	
			// Overall result
			boolean isResult		= result.isResult();
			Log.debug("passport is " + isResult);
			
			if (!isResult) {
				
				m_binder.putLocal("result", "FAIL");
				m_binder.putLocal("result", "0");


				try {
					if (!hasValidationRecord)
					{
						fw.execute("qClientServices_InsertValidationRecord", m_binder);
						hasValidationRecord=true;
					}
					fw.execute("qClientServices_UpdatePassportValidation", m_binder);
					
					AuthenticationScoreUtils.updateIdentityCheck(personId,fw);
				} catch (DataException e) {
					FullPassportErrorMessage = FullPassportErrorMessage + IdentityCheckGlobals.CCLA_VALIDATION_CANNOT_SAVE;
					Log.error("Error whilst validating:" + e.getMessage());
					
				}
				
				// DOB check digit correct?
				boolean dobDigitTest 		= result.isDobDigitTest();
				// Does supplied DOB match the one in passport number?
				boolean dobTest				= result.isDobTest();
				
				boolean docNumDigitTest		= result.isDocumentNumberDigitTest();
				boolean expiryDateDigitTest	= result.isExpiryDateDigitTest();
				
				// Passport number check digit correct?
				boolean finalDigitTest		= result.isFinalDigitTest();
				
				// Does supplied gender match the one in passport number?
				boolean genderTest			= result.isGenderTest();
				
				// Does supplied country code match one held on file?
				boolean isoTest				= result.isIsoTest();
				
				// Personal number check digit correct?
				boolean personalNumberDigitTest	= result.isPersonalNumberDigitTest();
				
				String checks = 
				 dobDigitTest + "," + dobTest + "," + docNumDigitTest + "," + 
				 expiryDateDigitTest + "," + finalDigitTest + "," + genderTest + "," +
				 isoTest + "," + personalNumberDigitTest;
				Log.debug("passport check is " + checks);
				m_binder.putLocal("checkFlags", checks);
				
			} else {
				m_binder.putLocal("passport_result", "PASS");
				m_binder.putLocal("result", "1");
				isPassPassport=true;
				try {
					if (!hasValidationRecord)
					{
						fw.execute("qClientServices_InsertValidationRecord", m_binder);
						hasValidationRecord=true;
					}
					fw.execute("qClientServices_UpdatePassportValidation", m_binder);
					Date expDate = AuthenticationScoreUtils.getNewExpiryDate();
					m_binder.putLocalDate("EXPIRY_DATE", expDate);
					fw.execute("qClientServices_SetExpiryDate_Authenticate", m_binder);	
					// clear any core data update flag
					CCLAUtils.addQueryIntParamToBinder(m_binder, "PERSON_ID", personId);
					fw.execute("qClientServices_ClearCoreDataAuthenticateRecord", m_binder);
					// recalculate score
					AuthenticationScoreUtils.updateIdentityCheck(personId,fw);

				} catch (DataException e) {
					Log.error("Error whilst validating:" + e.getMessage());
					FullPassportErrorMessage = FullPassportErrorMessage + IdentityCheckGlobals.CCLA_VALIDATION_CANNOT_SAVE;
				}
			}
			
		} catch (SOAPFaultException se) {
			Log.error("Failed to call doValidatePassportNumber", se);
			FullPassportErrorMessage = FullPassportErrorMessage + IdentityCheckGlobals.CCLA_VALIDATION_CANNOT_SAVE;
			throw new ServiceException(se);
		}
	}
	
	// Checks validity of 12-digit micro-number on the driving license counterpart.
	// Appears to the right of the barcode on the first page.
	//
	// Currently unavailable due to IP claim from the DVLA
	public void validateMicroNumber() throws ServiceException, RemoteException {
		
		String dateOfIssue 		= m_binder.getLocal("dateOfIssue");
		String mailsortCode		= m_binder.getLocal("mailsortCode");
		String microNumber		= m_binder.getLocal("microNumber");
		String postCode			= m_binder.getLocal("postCode");
		
		try {
			MicronumberResults result = getValidatorPlusWS().doValidateMicronumber
			 (dateOfIssue, mailsortCode, microNumber, postCode);
	
			// Overall result
			boolean isResult		= result.isResult();
			
			if (!isResult) {
				
				m_binder.putLocal("result", "FAIL");
				
				// Passed mailsort code matches postcode at given date
				// of issue?
				boolean historicMailsortTest 	= result.isHistoricMailsortTest();
				// Micro-number is valid?
				boolean microNumberTest			= result.isMicronumberTest();
				
				String checks = 
				 historicMailsortTest + "," + microNumberTest;
				
				m_binder.putLocal("checkFlags", checks);
				
			} else {
				m_binder.putLocal("result", "PASS");
			}
			
		} catch (SOAPFaultException se) {
			Log.error("Failed to call doValidateMicronumber", se);
		
			// TODO
			// Detail obj contains an Error XML node.
			// This contains the following elements:
			// ErrorNumber, ErrorMessage, ErrorSource
			javax.xml.soap.Detail detail = se.getDetail();	
			
			throw new ServiceException(se);
		}
	}
	
		public void getValuesFromPerson(Person thisPerson) throws DataException
		{
			FIRST_NAME = StringUtils.stringIsBlank(thisPerson.getFirstName())?"":thisPerson.getFirstName().trim();
			MIDDLE_NAME = StringUtils.stringIsBlank(thisPerson.getMiddleName())?"":thisPerson.getMiddleName().trim();
			LAST_NAME = StringUtils.stringIsBlank(thisPerson.getLastName())?"":thisPerson.getLastName().trim();	
			GENDER_ID = thisPerson.getGender();
			Log.debug("GENDER_ID IS " + GENDER_ID);
			if (!StringUtils.stringIsBlank(GENDER_ID))
			{
			try {
				int genderInt = Integer.parseInt(GENDER_ID);
				Gender thisGender = Gender.getGender(genderInt);
				GENDER = thisGender.getName();
				GENDER = GENDER.toUpperCase();
			} catch (NumberFormatException e) {
				Log.debug("COULD NOT GET GENDER FOR PERSON:" + thisPerson.getPersonId());
			}	
			}
			Log.debug("GENDER is " + GENDER);
			TITLE_ID = Integer.toString(thisPerson.getTitleId());
			Log.debug("TITLE_ID IS " + TITLE_ID);
			if (!StringUtils.stringIsBlank(TITLE_ID))
			{
				int titleInt = Integer.parseInt(TITLE_ID);
				PersonTitle pTitle = PersonTitle.getCache().getCachedInstance(titleInt);
				TITLE = pTitle.getTitle();
			}
			Log.debug("TITLE IS " + TITLE);
			FULLNAME = getValidationFullName();		
			SimpleDateFormat dobFormat = new SimpleDateFormat("dd/MM/yyyy");
			try {
				DOB = dobFormat.format(thisPerson.getDateOfBirth());
			} catch (Exception e) {
				Log.error("Unable to format date of birth for :" + thisPerson.getFirstName());
				DOB = "";
			}
			DRIVING_LICENCE = PersonUtils.getBinderValue(m_binder,"DRIVING_LICENCE");
			PASSPORT_NUMBER = PersonUtils.getBinderValue(m_binder,"PASSPORT_NUMBER");
			UTILITY_MAILSORT = PersonUtils.getBinderValue(m_binder,"UTILITY_MAILSORT");
			UTILITY_MAILSORT_DATE = PersonUtils.getBinderValue(m_binder,"UTILITY_MAILSORT_DATE");	
			String crockFords = PersonUtils.getBinderValue(m_binder, "CROCKFORDS_ID");
			if (!StringUtils.stringIsBlank(crockFords))
			CROCKFORDS_ID = Integer.parseInt(crockFords);
			else
			CROCKFORDS_ID = 0;
			CROCKFORDS_VALUE = PersonUtils.getBinderValue(m_binder, "CROCKFORDS_VALUE");
			
		}
		
		/** Gets whether a validation record exists for the person,
		 * true or false and puts resultset into binder
		 * as results set rsValidationRecord
		 * 
		 *  
		 *  
		 * @throws DataException
		 */	
		public static DataResultSet  getValidationRecord(FWFacade fw, int personId)
		throws DataException
		{
			DataBinder binder = new DataBinder();
			Log.debug("IN GETVALIDATIONRECORD");
			boolean hasValidationRecord = false;
			binder.putLocal("PERSON_ID",Integer.toString(personId));
			DataResultSet rs = fw.createResultSet("qClientServices_GetValidationRecord", binder);
			if (!rs.isEmpty())
			{
			Log.debug("found validation record for:" +personId);
			hasValidationRecord=true;
			binder.addResultSet("rsValidationRecord", rs);
			}
			
			return rs;
			
		}
		
		public  void initValues(DataBinder binder, FWFacade facade) throws DataException, ServiceException
		{
			// get person data, data passed and existing validation data for that person
			personId = CCLAUtils.getBinderIntegerValue(binder, "PERSON_ID");
			if (personId == 0)
				throw new ServiceException("MISSING PERSON_ID");			
			Person thisPerson = Person.get(personId, facade);
			getValuesFromPerson(thisPerson);
			DataResultSet rs = getValidationRecord(facade, personId);
			if (!rs.isEmpty())
				hasValidationRecord=true;
		}
		
		public void doValidateLicence(FWFacade fw) 
		 throws DataException, ServiceException {
			try {
				Log.debug("**** DRIVING LICENCE **********");
				tryDrivingLicence = true;
				validateDrivingLicence(fw);
			} catch (RemoteException re) {
				
				Log.error("RemoteError in doValidate:" + re.getMessage());
				Log.error("with parameters" 
				 + DRIVING_LICENCE +"|" + DOB+"|" + FULLNAME +"|" +  GENDER);
				
				StringTokenizer st = new StringTokenizer(re.getMessage(),"|");
				int i=0;
				while (st.hasMoreTokens()){
					String token = st.nextToken();
					if (!token.equalsIgnoreCase("OK"))
						FullDrivingErrorMessage = FullDrivingErrorMessage 
						+ ExperianGlobals.validateDrivingLicenceParameters[i]
						+ ":" + token + " ";  
					i=i+1;
				}
				
				Log.error("full error is: " + FullDrivingErrorMessage);
				if (!hasValidationRecord)
				{
					fw.execute("qClientServices_InsertValidationRecord", m_binder);
					hasValidationRecord=true;
				}
				m_binder.putLocal("result", "");
				fw.execute("qClientServices_UpdateLicenceValidation", m_binder);
				
			} catch (ServiceException se) {
				Log.error("ServiceError in doValidate:" + se.getMessage());
				FullDrivingErrorMessage = "Driving Licence:" + 
				 IdentityCheckGlobals.CCLA_VALIDATION_SERVICE;
				
				throw se;
			} 			
		}
			
		public void doValidatePassport(FWFacade fw) throws DataException
		{
			try {
				Log.debug("**** PASSPORT **********");
				tryPassport = true;
				validatePassportNumber(fw);
				
			} catch (RemoteException re) {
				Log.error("RemoteError in doValidate:" + re.getMessage());
				StringTokenizer st = new StringTokenizer(re.getMessage(),"|");
				int i=0;
				while (st.hasMoreTokens()){
					String token = st.nextToken();
					if (!token.equalsIgnoreCase("OK"))
						FullPassportErrorMessage = FullPassportErrorMessage 
						+ com.ecs.ucm.ccla.experian.ExperianGlobals.validatePassportParameters[i]
						+ ":" + token + " ";  
					i=i+1;
				}
				if (!hasValidationRecord)
				{
					fw.execute("qClientServices_InsertValidationRecord", m_binder);
					hasValidationRecord=true;
				}
				m_binder.putLocal("result", "");
				fw.execute("qClientServices_UpdatePassportValidation", m_binder);
			} catch (ServiceException e) {
				Log.error(e.getMessage());
				FullPassportErrorMessage = "Passport:" + IdentityCheckGlobals.CCLA_VALIDATION_SERVICE; 
			}
		}
		
		public void doValidateMailsort(FWFacade fw, int personId) throws DataException
		{
			try {
				//need to get default postcode for person, assume this is experian one
				Vector<Contact> allContacts = Contact.getElementContacts(personId, fw);
				// returns all contact point rows
				// get the default ones for address
				Contact defaultContact = Contact.getDefaultContact(allContacts, Contact.ADDRESS);
				if (defaultContact == null)
				{
					FullMailsortErrorMessage = "Person is missing a default address, please add one to enable mailsort validation";
				} 
				else 
				{
					int addressId = defaultContact.getAddressId();
					Log.debug("addressId is " + addressId);
					Address defaultAddress = Address.get(addressId, fw);
					Log.debug("default address id is " + defaultAddress.getAddressId());
					String defaultPostCode = defaultAddress.getPostCode();
					Log.debug("defaultPostCode is " + defaultPostCode);
					if (!StringUtils.stringIsBlank(defaultPostCode))
					{
						tryMailsort=true;
						m_binder.putLocal("POSTCODE", defaultPostCode);
					}
				
				if (tryMailsort)
				{
				validateMailsortNumber(fw);
				} else {
					FullMailsortErrorMessage = "Default address for this person is missing or the address does not have a postcode, please add one to enable mailsort validation";
				}
				}
			} catch (RemoteException re) {
				Log.error("RemoteError in doValidate:" + re.getMessage());
				StringTokenizer st = new StringTokenizer(re.getMessage(),"|");
				int i=0;
				while (st.hasMoreTokens()){
					String token = st.nextToken();
					if (!token.equalsIgnoreCase("OK"))
						FullMailsortErrorMessage = FullMailsortErrorMessage 
						+ com.ecs.ucm.ccla.experian.ExperianGlobals.validateMailsortParameters[i]
						+ ":" + token + " ";  
					i=i+1;
				}
				if (!hasValidationRecord)
				{
					fw.execute("qClientServices_InsertValidationRecord", m_binder);
					hasValidationRecord=true;
				}
				m_binder.putLocal("result", "");
				fw.execute("qClientServices_UpdateMailsortValidation", m_binder);
				Log.error("full error is: " + FullMailsortErrorMessage);
			} catch (ServiceException e) {
				m_binder.putLocal("result", "");
				fw.execute("qClientServices_UpdateMailsortValidation", m_binder);				
				Log.error(e.getMessage());
				FullMailsortErrorMessage = "Utility Mailsort:" + IdentityCheckGlobals.CCLA_VALIDATION_SERVICE; 
			}
		}
}
