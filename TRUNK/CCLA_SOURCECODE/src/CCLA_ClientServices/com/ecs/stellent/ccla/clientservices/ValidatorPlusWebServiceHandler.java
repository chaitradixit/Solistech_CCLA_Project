package com.ecs.stellent.ccla.clientservices;

import java.net.URL;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import javax.xml.rpc.soap.SOAPFaultException;
import javax.xml.soap.Detail;
import javax.xml.soap.SOAPElement;

import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.experian.validator.webservice.DrivingLicenceResults;
import com.experian.validator.webservice.MailsortResults;
import com.experian.validator.webservice.MicronumberResults;
import com.experian.validator.webservice.PassportNumberResults;
import com.experian.validator.webservice.ValidatorPlusServiceLocator;
import com.experian.validator.webservice.ValidatorPlusServiceSoap_BindingStub;

import intradoc.common.ServiceException;
import intradoc.server.Service;
import intradoc.shared.SharedObjects;

public class ValidatorPlusWebServiceHandler extends Service {

	/** Format required for all date request strings */
	public static final SimpleDateFormat REQUEST_DATE_FORMAT = 
	 new SimpleDateFormat("dd/MM/yy");
	
	public static final String MALE = "MALE";
	public static final String FEMALE = "FEMALE";
	
	/** Returns an instance of the Aurora web service stub. */
	public static ValidatorPlusServiceSoap_BindingStub getValidatorPlusWS() 
	 throws ServiceException {
	
		ValidatorPlusServiceLocator locator = new ValidatorPlusServiceLocator();
		
		// Get the Validator Plus web service address
		String validatorPlusAddress = 
		 SharedObjects.getEnvironmentValue("EXPERIAN_ValidatorPlusWebServiceAddress");
		
		Log.info("Validator Plus: Connecting to " + validatorPlusAddress);
		locator.setValidatorPlusServiceSoap12EndpointAddress(validatorPlusAddress);
		
		ValidatorPlusServiceSoap_BindingStub service 	= null;
		
		try {
			URL portUrl = new URL(validatorPlusAddress);
			//return locator.getValidatorPlusServiceSoap(portUrl);
			
			service = (ValidatorPlusServiceSoap_BindingStub)
			 locator.getValidatorPlusServiceSoap(portUrl);
			
		} catch (Exception e) {
			Log.error
			 ("Failed to instantiate Validator Plus Web Service binding stub", e);
			throw new ServiceException(e);
		}
		
		return service;
	}
	
	/**
	 *  Validates the driving license number against passed inputs.
	 *  Does not perform an external lookup of any sort.
	 * 
	 * @throws RemoteException
	 * @throws ServiceException
	 */
	public void validateDrivingLicence() throws RemoteException, ServiceException {
		
		String licenseNumber 	= m_binder.getLocal("licenseNumber");
		String dateOfBirth   	= m_binder.getLocal("dateOfBirth");
		
		// Name as it appears on the driving license
		// Don't worry about title portion (not required)
		String name				= m_binder.getLocal("name");
		
		// MALE/FEMALE
		String sex				= m_binder.getLocal("sex");
		
		try {
			DrivingLicenceResults result = getValidatorPlusWS().doValidateDrivingLicence
			 (licenseNumber, dateOfBirth, name, sex);
	
			// Overall result
			boolean isResult		= result.isResult();
			
			if (!isResult) {
				
				m_binder.putLocal("result", "FAIL");
				
				boolean isDobTest 		= result.isDobTest();
				boolean isInitialsTest 	= result.isInitialsTest();
				boolean isSurnameTest	= result.isSurnameTest();
				
				m_binder.putLocal("dobTest", Boolean.toString(isDobTest));
				m_binder.putLocal("initialsTest", Boolean.toString(isInitialsTest));
				m_binder.putLocal("surnameTest", Boolean.toString(isSurnameTest));
				
			} else {
				m_binder.putLocal("result", "PASS");
			}
			
		} catch (SOAPFaultException se) {
			Log.error("Failed to call doValidateDrivingLicense", se);
		
			// TODO
			// Detail obj contains an Error XML node.
			// This contains the following elements:
			// ErrorNumber, ErrorMessage, ErrorSource
			Detail detail = se.getDetail();	
			
			/*
			Log.debug("SOAPFailException details: ");
			Log.debug("FaultActor: " + se.getFaultActor());
			Log.debug("FaultString: " + se.getFaultString());
			Log.debug("FaultCode: " + se.getFaultCode());
			Log.debug("Detail: " + se.getDetail());
			
			Iterator<SOAPElement> i = detail.getChildElements();
			
			while (i.hasNext()) {
				SOAPElement childElem = i.next();
				
				Log.debug("Detail child elem: ");
				
				Log.debug("Name: " + childElem.getNodeName());
				Log.debug("Value: " + childElem.getNodeValue());
			}
			*/
			
			throw new ServiceException(se);
		}
	}
	
	// Performs lookup against database of mailsort codes.
	//
	// Functions as an address check.
	public void validateMailsortNumber() throws RemoteException, ServiceException {
		
		// Date on the bill.
		String dateOfIssue 	= m_binder.getLocal("dateOfIssue");
		
		// Mailsort code
		// Always 5 digits, printed around the address.
		String mailsortCode = m_binder.getLocal("mailsortCode"); 
		
		// Postcode (not fussy about formatting)
		String postCode 	= m_binder.getLocal("postCode");
		
		try {
			MailsortResults result = getValidatorPlusWS().doValidateMailsort
			 (dateOfIssue, mailsortCode, postCode);
	
			// Overall result
			boolean isResult		= result.isResult();
			
			if (!isResult) {				
				m_binder.putLocal("result", "FAIL");				
				// Essentially the same as isResult
				boolean isFullMatch = result.isFullMatchTest();				
				// Checks if the mail sort code exists in the database
				boolean isMailSortRangeTest	= result.isMailsortRangeTest();
				
				m_binder.putLocal("fullMatchTest", Boolean.toString(isFullMatch));
				m_binder.putLocal("mailSortRangeTest", Boolean.toString(isMailSortRangeTest));
				
			} else {
				m_binder.putLocal("result", "PASS");
			}
			
		} catch (SOAPFaultException se) {
			Log.error("Failed to call doValidateMailsortNumber", se);
		
			// TODO
			// Detail obj contains an Error XML node.
			// This contains the following elements:
			// ErrorNumber, ErrorMessage, ErrorSource
			javax.xml.soap.Detail detail = se.getDetail();	
			
			throw new ServiceException(se);
		}
	}
	
	// Checks validity of passport number against passed data
	public void validatePassportNumber() throws ServiceException, RemoteException {
		
		String dateOfBirth 		= m_binder.getLocal("dateOfBirth");
		String passportNumber	= m_binder.getLocal("passportNumber");
		String sex				= m_binder.getLocal("sex");
		String isLongNumberStr	= m_binder.getLocal("isLongNumber");
	
		// Should always be Yes. 'Short number' validation will only check the
		// portion of the passport number that appears before the <<< characters.
		boolean isLongNumber 	=  !StringUtils.stringIsBlank(isLongNumberStr);
		
		try {
			PassportNumberResults result = getValidatorPlusWS().doValidatePassport
			 (dateOfBirth, passportNumber, sex, isLongNumber);
	
			// Overall result
			boolean isResult		= result.isResult();
			
			if (!isResult) {
				
				m_binder.putLocal("result", "FAIL");
				
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
				
				m_binder.putLocal("checkFlags", checks);
				
			} else {
				m_binder.putLocal("result", "PASS");
			}
			
		} catch (SOAPFaultException se) {
			Log.error("Failed to call doValidatePassportNumber", se);
		
			// TODO
			// Detail obj contains an Error XML node.
			// This contains the following elements:
			// ErrorNumber, ErrorMessage, ErrorSource
			javax.xml.soap.Detail detail = se.getDetail();	
			
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
}
