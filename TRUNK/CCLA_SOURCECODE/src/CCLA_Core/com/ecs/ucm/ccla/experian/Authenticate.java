package com.ecs.ucm.ccla.experian;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.shared.SharedObjects;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.IdentityCheckAudit;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.PersonTitle;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;
import com.experian.webservice.AddressLineType;
import com.experian.webservice.PicklistEntryType;
import com.experian.webservice.QAFault;
import com.experian.webservice.QAPicklistType;
import com.experian.webservice.QASearch;
import com.experian.webservice.QASearchResult;
import com.experian.webservice.SearchTerm;
import com.experian.webservice.VerifyLevelType;

public class Authenticate extends Search {
	
	/** Whether or not the Experian Authenticate web service is actually executed. If
	 *  true, the service isn't executed and some dummy test data is returned instead.
	 */
	private static final boolean LOCAL_TEST_MODE = !StringUtils.stringIsBlank
	 (SharedObjects.getEnvironmentValue("EXPERIAN_Authenticate_LocalTestMode"));
	
	/** Performs an authentication check for a given name and address.
	 *  Various response fields are placed into the binder.
	 *  
	 *  TODO: audit the response
	 *  
	 * @throws ServiceException
	 */
	
	public String auditText = "";
	private String riskConditions = "";
	
	//  boolean value if the search is  retry
	boolean isRetry = false;
	
	// these are the address values passed to experian
	private String flatName = "";
	private String houseName = "";
	private String houseNo = "";
	private String streetName = "";
	private String district = "";
	private String town = "";
	private String county = "";
	private String postcode = "";
	
	public void doAuthenticate() throws ServiceException {

		if (LOCAL_TEST_MODE || canSearch())
		{
			Log.debug("canSearch is true");
		try {
			FWFacade fw = CCLAUtils.getFacade(this.m_workspace, true);
			String Person_ID = m_binder.getLocal("PERSON_ID");
			int personId = Integer.parseInt(Person_ID);
			
			SearchTerm[] searchTerms = new SearchTerm[ExperianGlobals.authParams.length];
			getAuthenticateParameters(searchTerms, personId, fw);
			this.checkAuthenticate(searchTerms, fw, Person_ID);
			String dec = m_binder.getLocal("DECISION");
			String refNo = m_binder.getLocal("REF_NO");
			if (StringUtils.stringIsBlank(dec))
				dec = "";
			if (StringUtils.stringIsBlank(refNo))
				refNo = "";
			
			/*
			AuthenticateAuditRecord.insertAuthenticateAuditRecord("Experian", Person_ID,
					auditText, dec, refNo, m_userData.m_name,
					m_binder, fw);
			*/
			
			IdentityCheckAudit.add(Integer.parseInt(Person_ID), 
			 IdentityCheckAudit.CheckType.Experian, 
			 m_userData.m_name, auditText, dec, refNo, fw);
					
			AuthenticationScoreUtils.updateIdentityCheck(personId,fw);
			
			// if NA00 plus all flat, housenumber and housename all passed
			// then need to try again without housename
			if (dec.equalsIgnoreCase(ExperianGlobals.FAILURE_CODE))
			{
				if (flatName.length()>0 && houseName.length()>0 && houseNo.length()>0)
				{
					searchTerms[6].set_value("");
					Log.debug("searchTerms6:" + searchTerms[6]);
					this.checkAuthenticate(searchTerms, fw, Person_ID);
					dec = m_binder.getLocal("DECISION");
					refNo = m_binder.getLocal("REF_NO");
					if (StringUtils.stringIsBlank(dec))
						dec = "";
					if (StringUtils.stringIsBlank(refNo))
						refNo = "";
					
					/*
					AuthenticateAuditRecord.insertAuthenticateAuditRecord("Experian", Person_ID,
							auditText, dec, refNo, m_userData.m_name,
							m_binder, fw);
					*/
					
					IdentityCheckAudit.add(Integer.parseInt(Person_ID), 
					 IdentityCheckAudit.CheckType.Experian, 
					 m_userData.m_name, auditText, dec, refNo, fw);
					
					AuthenticationScoreUtils.updateIdentityCheck(personId,fw);
				}
			}
			
			//Append the isCompact flag onto the redirect URL if flag is preset
			String isCompact = m_binder.getLocal("isCompact");
			
			if(!StringUtils.stringIsBlank(isCompact)){
				m_binder.putLocal("RedirectUrl", 
						m_binder.getLocal("RedirectUrl") + "&isCompact=" + isCompact);
			}
			
		} catch (QAFault e) {
			String msg = 
				"QAFault: code: " + e.getErrorCode() 
				 + ", message: " + e.getErrorMessage();			
			Log.error(msg, e);
			Log.error("QAFault Message: " + e.getMessage());
			Log.error("QAFault Reason: " + e.getFaultReason());
			
			String ErrorMessage = IdentityCheckGlobals.CCLA_AUTHENTICATION_FAILED_MSG;
			String redirectURL = m_binder.getLocal("RedirectUrl");
			redirectURL = redirectURL + "&ErrorMessage=" + ErrorMessage;
			m_binder.putLocal("RedirectUrl", redirectURL);
		} catch (Exception e) {
			Log.error("Failed to call Experian web service", e);
			
			String ErrorMessage = IdentityCheckGlobals.CCLA_AUTHENTICATION_FAILED_SERVICE_MSG;
			String redirectURL = m_binder.getLocal("RedirectUrl");
			redirectURL = redirectURL + "&ErrorMessage=" + ErrorMessage;
			m_binder.putLocal("RedirectUrl", redirectURL);
		}
		}
	}
	
	/** @deprecated authentication checks can't be overriden like this any more. */
	public void overrideAuthentication()
	{
		String ErrorMessage ="";
		String reasonStr = m_binder.getLocal("REASON");
		String personID = m_binder.getLocal("PERSON_ID");
		
		int personId = Integer.parseInt(personID);
		//String userName = m_userData.m_name;
		
		Log.debug("overriding authentication");
		Log.debug("by person:" + m_userData.m_name);
		Log.debug("reason is:" + m_binder.getLocal("REASON"));
		
		if (reasonStr.length() > 10) {
			try {
				FWFacade fw = CCLAUtils.getFacade(this.m_workspace, true);
				DataResultSet rs = fw.createResultSet("qClientServices_GetAuthenticateRecord", m_binder);
				if (rs.isEmpty())
				{
				//need to insert new record
					fw.execute("qClientServices_InsertAuthenticateRecord", m_binder);
					Log.debug("created record for person:" + personID);	
				} 
				// in this case update record
				m_binder.putLocal("DECISION_TEXT", reasonStr);
				fw.execute("qClientServices_UpdateOverrideAuthenticateRecord", m_binder);
				Log.debug("update record for person:" + personID);
				
				
				AuthenticationScoreUtils.updateIdentityCheck(personId,fw);
				
				IdentityCheckAudit.add(Integer.parseInt(personID), 
				 IdentityCheckAudit.CheckType.Manual, 
				 m_userData.m_name, "manual override", reasonStr, null, fw);
				
				/*
				AuthenticateAuditRecord.insertAuthenticateAuditRecord("Manual", personID,
						, reasonStr, "" , m_userData.m_name,
						m_binder, fw);
				*/
				
			} catch (DataException e) {
				Log.debug("Error override authentication:" + e.getMessage());
				ErrorMessage = IdentityCheckGlobals.CCLA_AUTHENTICATION_FAILED_MSG;
				String redirectURL = m_binder.getLocal("RedirectUrl");
				redirectURL = redirectURL + "&ErrorMessage=" + ErrorMessage;
				m_binder.putLocal("RedirectUrl", redirectURL);
			}
		} else {
			Log.error("MISSING REASON FOR OVERRIDE AUTHENTCATION:" + personID);
			ErrorMessage = "MISSING REASON FOR OVERRIDE AUTHENTCATION";
			String redirectURL = m_binder.getLocal("RedirectUrl");
			redirectURL = redirectURL + "&ErrorMessage=" + ErrorMessage;
			m_binder.putLocal("RedirectUrl", redirectURL);
		}
		
		m_binder.putLocal("RedirectUrl", 
		 "?IdcService=CCLA_CS_PERSON_EDIT&PERSON_ID=" + personID + "&ErrorMessage=" + ErrorMessage);	
	}
	
	public SearchTerm[] getAuthenticateParameters(SearchTerm[] searchTerms, int Person_ID, FWFacade fw)
	throws DataException
	{
		SearchTerm searchTerm = new SearchTerm();
		Person mypeep = Person.get(Person_ID, fw);
		// Get the parameters from the form.
		Integer titleInt = mypeep.getTitleId();
		PersonTitle pTitle = PersonTitle.getCache().getCachedInstance(titleInt);
		String titleStr = pTitle.getTitle();
		Log.debug("PASSING:" + titleStr);
		String nameStr = m_binder.getLocal("FIRST_NAME");
		Log.debug("PASSING:" + nameStr);
		String surnameStr = m_binder.getLocal("LAST_NAME");
		Log.debug("PASSING:" + surnameStr);
		Date mydate = mypeep.getDateOfBirth();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");	
		String dobStr = df.format(mydate);
		Log.debug("PASSING:" + dobStr);
		
		DataResultSet rs = getPersonExperianAddress(Person_ID, fw, m_binder);
		if (rs.isEmpty())
		{
		Log.error("Cannot authenticate person: CANNOT FIND EXPERIAN ADDRESS FOR PERSON:" + Person_ID);
		String ErrorMessage = IdentityCheckGlobals.CCLA_AUTHENTICATION_MISSING_ADDRESS_MSG;
		String redirectURL = m_binder.getLocal("RedirectUrl");
		redirectURL = redirectURL + "&ErrorMessage=" + ErrorMessage;
		m_binder.putLocal("RedirectUrl", redirectURL);
		} else 
		{
			Log.debug("rs is not empty");
			for(int i = 0; i < rs.getNumRows(); i++)
			{
				rs.setCurrentRow(i);
				flatName = rs.getStringValueByName("FLAT");
				if (flatName==null)
					flatName="";
				Log.debug("PASSING:" + flatName);
				houseName = rs.getStringValueByName("HOUSENAME");
				if (houseName == null)
					houseName = "";
				Log.debug("PASSING:" + houseName);
				houseNo = rs.getStringValueByName("HOUSENUMBER");
				if (houseNo == null)
					houseNo = "";
				Log.debug("PASSING:" + houseNo);
				streetName = rs.getStringValueByName("STREET");
				if (streetName == null)
					streetName = "";
				Log.debug("PASSING:" + streetName);
				district = rs.getStringValueByName("DISTRICT");
				if (district == null)
					district = "";
				Log.debug("PASSING:" + district);				
				town = rs.getStringValueByName("CITY");
				if (town == null)
					town = "";
				Log.debug("PASSING:" + town);	
				county = rs.getStringValueByName("COUNTY");
				if (county == null)
					county = "";
				Log.debug("PASSING:" + county);	
				postcode = rs.getStringValueByName("POSTCODE");
				if (postcode == null)
					postcode = "";
				Log.debug("PASSING:" + postcode);					
			}


		auditText = titleStr + "|" + nameStr + "|" + surnameStr + "|" + dobStr + "|" +
		flatName + "|" + houseName + "|" + houseNo + "|" + streetName + "|" + district + "|" +
		town + "|" + county + "|" + postcode;
		Log.debug("auditText:" + auditText);
		// Build search terms list
		for (int i=0; i<ExperianGlobals.authParams.length; i++) {
			searchTerm = new SearchTerm();
			searchTerm.setKey(ExperianGlobals.authParams[i]);			
			if (i == 0)
				searchTerm.set_value("Yes");
			else if (i == 1) {
				searchTerm.set_value(titleStr);
			}
			else if (i == 2) {
				searchTerm.set_value(nameStr);
			}
			else if (i == 3) {
				searchTerm.set_value(surnameStr);
			}
			else if (i == 4) {
				searchTerm.set_value(dobStr);
			}
			else if (i == 5) {
				searchTerm.set_value(flatName);
			}
			else if (i == 6) {
				searchTerm.set_value(houseName);				
			}
			else if (i == 7) {
				searchTerm.set_value(houseNo);				
			}
			else if (i == 8) {
				searchTerm.set_value(streetName);				
			}
			else if (i == 9) {
				searchTerm.set_value(district);				
			}
			else if (i == 10) {
				searchTerm.set_value(town);				
			}
			else if (i == 11) {
				searchTerm.set_value(county);				
			}			
			else if (i == 12) {
				searchTerm.set_value(postcode);				
			}			
			else
				searchTerm.set_value(null);
		
			searchTerms[i] = searchTerm;
			Log.debug("for number " + i + "set " + searchTerm);
			Log.debug("Globals.authParams.length is " + ExperianGlobals.authParams.length);
		}
		}
		return searchTerms;
	}
	
	public void addCustomResultstoBinder(DataBinder binder, String isAuthenticated, String DecisionText, String Decision
			, String RiskConditions, int AuthenticationIndex, String RiskConditionsText, String RefNo)
	{
		// IS_AUTHENTICATED
		String authIndex = Integer.toString(AuthenticationIndex);
		CCLAUtils.addQueryParamToBinder(binder, "IS_AUTHENTICATED", isAuthenticated);
		// DECISION_TEXT
		CCLAUtils.addQueryParamToBinder(binder, "DECISION_TEXT", DecisionText);
		// DECISION
		CCLAUtils.addQueryParamToBinder(binder, "DECISION", Decision);
		// RISK_CONDITIONS
		CCLAUtils.addQueryParamToBinder(binder, "RISK_CONDITIONS", RiskConditions);
		// AUTHENTICATION_INDEX
		CCLAUtils.addQueryParamToBinder(binder, "AUTHENTICATION_INDEX", authIndex);
		// RISK_CONDITION_TEXT
		CCLAUtils.addQueryParamToBinder(binder, "RISK_CONDITIONS_TEXT", RiskConditionsText);
		// REF_NO
		CCLAUtils.addQueryParamToBinder(binder, "REF_NO", RefNo);
	}
	
	public void addResultsToBinder(DataBinder m_binder,AddressLineType[] response){
		
		m_binder.putLocal("IS_AUTHENTICATED", "0");
		
		for (int i=0; i<response.length; i++) {
			String Label = response[i].getLabel();
			String Line = response[i].getLine();
			if (Label.equalsIgnoreCase("Authenticate Decision Text "))
			{
				m_binder.putLocal("DECISION_TEXT", Line);
			} 
			else if (Label.equalsIgnoreCase("Authenticate Decision"))
			{
				m_binder.putLocal("DECISION", Line);
				if (Line.equalsIgnoreCase(ExperianGlobals.SUCCESS_CODE))
					m_binder.putLocal("IS_AUTHENTICATED", "1");
			} 
			else if (Label.equalsIgnoreCase("Authenticate High Risk Policy rule(s)"))
			{
				m_binder.putLocal("RISK_CONDITIONS", Line);
				riskConditions=Line;
				Log.debug("riskConditions:" + riskConditions);
			} 
			else if (Label.equalsIgnoreCase("Authenticate Authentication index"))
			{
				m_binder.putLocal("AUTHENTICATION_INDEX", Line);
			} 
			else if (Label.equalsIgnoreCase("Authenticate High Risk Policy rule text"))
			{
				m_binder.putLocal("RISK_CONDITIONS_TEXT", Line);
			} 
			else if (Label.equalsIgnoreCase("Authenticate Experian Reference Number"))
			{
				m_binder.putLocal("REF_NO", Line);
			} 	
			
			Log.debug("Response" + i, Label + 
					 ": " + Line);
			Label ="";
			Line ="";
		}	
	}
	
	public static void addAuthenticationRecord(DataBinder binder, FWFacade fw) throws DataException
	{
		DataResultSet rs = fw.createResultSet("qClientServices_GetAuthenticateRecord", binder);
		if (rs.isEmpty())
		{
			binder.putLocalDate("EXPIRY_DATE", AuthenticationScoreUtils.getNewExpiryDate());
			fw.execute("qClientServices_InsertAuthenticateRecord", binder);
		} else {
			binder.putLocalDate("EXPIRY_DATE", AuthenticationScoreUtils.getNewExpiryDate());
			fw.execute("qClientServices_UpdateAuthenticateRecord", binder);
		}	
	}
	
	public void checkAuthenticate(SearchTerm[] searchTerms,  FWFacade fw, String Person_ID) 
	 throws DataException, QAFault, RemoteException, ServiceException {
		
		QASearchResult results = null;
		AddressLineType[] response = null;
		
		if (LOCAL_TEST_MODE) {
			// Return dummy data.
			Log.debug("Running Experian check service in Test Mode");
			results = getDummySearchResults();
			response = getDummyResponse();
			
			Log.debug("Generated dummy response: " + response + ", results: " + results);
			
		} else {
			QASearch search = new QASearch
			 (ExperianGlobals.COUNTRY_DATASET, ExperianGlobals.AUTHENTICATE_ENGINE, ExperianGlobals.AUTH_PRO_LAYOUT, 
			  null, null, searchTerms);
			
			results = getExperianWS().doSearch(search);
			response = results.getQAAddress();
		}
		
		if (response == null) {
			QAPicklistType pickList = results.getQAPicklist();				
			PicklistEntryType[] entries = pickList.getPicklistEntry();				
			m_binder.putLocal("numEntries", Integer.toString(entries.length));
			for (int i=0; i<entries.length; i++) {
				Log.debug("Entry" + i, entries[i].getPartialAddress() + 
				 ", Moniker: " + entries[i].getMoniker() + ", warn? " + 
				 entries[i].isWarnInformation() + ", pickListValue: " + entries[i].getPicklist());
				Log.error("unable to run authenticate for person " + Person_ID);
				Log.error("error is:" + entries[i].getPicklist());
				String ErrorMessage = IdentityCheckGlobals.CCLA_AUTHENTICATION_FAILED_MSG;
				String ErrorDetail = entries[i].getPicklist();
				if (ErrorDetail.indexOf("does not uniquely identify an address")>-1)
					ErrorDetail = ErrorDetail + IdentityCheckGlobals.CCLA_AUTHENTICATION_MISSING_ADDRESS_DETAIL + IdentityCheckGlobals.CCLA_AUTHENTICATION_MISSING_ADDRESS_MSG;
				if (ErrorDetail.indexOf("no search results present")>-1)
				{
					ErrorDetail = ErrorDetail + ". <strong>" + IdentityCheckGlobals.CCLA_AUTHENTICATION_INCOMPLETE_ADDRESS + "</strong>";
					//add a referral code and log as failure
					addCustomResultstoBinder(m_binder,"0", "", "RA00", "U999", 0, "Missing or incomplete address/name", "");
					addAuthenticationRecord(m_binder, fw);
					
					/*
					AuthenticateAuditRecord.insertAuthenticateAuditRecord("Experian", Person_ID,
							auditText, "RA00", "", m_userData.m_name,
							m_binder, fw);
					*/
					
					IdentityCheckAudit.add(Integer.parseInt(Person_ID), 
					 IdentityCheckAudit.CheckType.Experian, 
					 m_userData.m_name, auditText, "RA00", null, fw);
					
				}
				String redirectURL = m_binder.getLocal("RedirectUrl");
				redirectURL = redirectURL + "&ErrorMessage=" + ErrorMessage + "&ErrorDetail=" + ErrorDetail;
				m_binder.putLocal("RedirectUrl", redirectURL);
			}								
		} else {				
			// Add results to binder
			addResultsToBinder(m_binder,response);
			addAuthenticationRecord(m_binder, fw);
			m_binder.putLocal("RedirectUrl", "?IdcService=CCLA_CS_PERSON_EDIT&PERSON_ID=" + Person_ID);			
		}						
	}
	
	/** Used to generate dummy Experian Authenticate response objects.
	 * 
	 */
	private static AddressLineType[] getDummyResponse() {
		AddressLineType[] response = new AddressLineType[6];
		
		response[0] = new AddressLineType(
			"Authenticate Decision Text ", 
			"User has been authenticated via dummy test response", 
			null, null, false, false
		);
		
		response[1] = new AddressLineType(
			"Authenticate Decision", 
			ExperianGlobals.SUCCESS_CODE, 
			null, null, false, false
		);
		
		response[2] = new AddressLineType(
			"Authenticate Authentication index", 
			"80", 
			null, null, false, false
		);
		
		response[3] = new AddressLineType(
			"Authenticate Experian Reference Number", 
			"TESTREF0123456789", 
			null, null, false, false
		);
		
		response[4] = new AddressLineType(
			"Authenticate High Risk Policy rule(s)", 
			"", 
			null, null, false, false
		);
			
		response[5] = new AddressLineType(
			"Authenticate High Risk Policy rule text", 
			"", 
			null, null, false, false
		);
		
		return response;
	}
	
	/** Used to generate dummy Experian Authenticate response objects.
	 * 
	 */
	private static QASearchResult getDummySearchResults() {
		
		return new QASearchResult(
			new QAPicklistType(), 
			new AddressLineType[0], 
			VerifyLevelType.Verified
		);
	}
	
	/** Gets a resultset of the experian address contact map entry for a persson
	 *  
	 * @return a results set the contact map entry for a person's experian address
	 *  
	 * @throws DataException
	 */
	public static DataResultSet getPersonExperianAddress(int Person_ID, FWFacade fw, DataBinder binder)
	throws DataException
	{	
		binder.putLocal("PERSON_ID", Integer.toString(Person_ID));
		DataResultSet rs = fw.createResultSet("qClientServices_GetPersonExperianAddress", binder);	
		return rs;
	}	
	
}
