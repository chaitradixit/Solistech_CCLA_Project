package com.ecs.stellent.ccla.clientservices;

import java.rmi.RemoteException;

import com.ecs.utils.Log;
import com.experian.webservice.Address;
import com.experian.webservice.AddressLineType;
import com.experian.webservice.EngineType;
import com.experian.webservice.PicklistEntryType;
import com.experian.webservice.ProWebLocator;
import com.experian.webservice.QACanSearch;
import com.experian.webservice.QAConfigType;
import com.experian.webservice.QAFault;
import com.experian.webservice.QAGetAddress;
import com.experian.webservice.QAPicklistType;
import com.experian.webservice.QASearch;
import com.experian.webservice.QASearchOk;
import com.experian.webservice.QASearchResult;
import com.experian.webservice.QASoapBindingStub;
import com.experian.webservice.SearchTerm;

import intradoc.common.ServiceException;
import intradoc.server.Service;
import intradoc.shared.SharedObjects;

public class ExperianWebServiceHandler extends Service {
	
	/** Country dataset passed to Experian lookup services */
	public static String COUNTRY_DATASET;
	public static String COUNTRY_DATASET_BACKUP;
	
	/** Layout used when fetching address data */
	public static String PRO_WEB_LAYOUT;
	
	/** Layout used when fetching authentication data */
	public static String AUTH_PRO_LAYOUT;
	
	/** Location of Experian config file */
	public static final String CONFIG_FILE_PATH = 
	 "\\\\10.15.4.12\\d$\\Program Files\\QAS\\Authenticate Pro Web 1.15\\Qawserve.ini";
	
	/** Engine used for single-line address lookup */
	public static EngineType SINGLE_LINE_SEARCH_ENGINE = 
	 new EngineType("Singleline");
	
	/** Engine used for identity authentication */
	public static EngineType AUTHENTICATE_ENGINE = 
	 new EngineType("Authenticate");
	
	/** Configuration object passed to Experian lookup services
	 *  Not currently used.
	 **/
	public static QAConfigType EXPERIAN_CONFIG = new QAConfigType(CONFIG_FILE_PATH, null);
	
	/** List of mandatory authentication parameters which must be
	 *  used in conjunction with the Authenticate engine
	 */
	public static final String[] authParams = {
		"CTRL_SEARCHCONSENT",
		"NAME_TITLE",
		
		"NAME_FORENAME",
		"NAME_SURNAME",
		"NAME_DATEOFBIRTH",
		
		"FLAT",
		"HOUSENAME",
		"HOUSENUMBER",
		"STREET",
		"DISTRICT",
		"TOWN",
		"COUNTY",
		"POSTCODE"
	};

	static {
		COUNTRY_DATASET = 
		 SharedObjects.getEnvironmentValue("EXPERIAN_CountryDataset");	
		
		PRO_WEB_LAYOUT = 
		 SharedObjects.getEnvironmentValue("EXPERIAN_ProWebLayout");
		
		AUTH_PRO_LAYOUT =
		 SharedObjects.getEnvironmentValue("EXPERIAN_AuthenticateProLayout");
		
		// Set attributes for engines
		SINGLE_LINE_SEARCH_ENGINE.setFlatten(true);
		AUTHENTICATE_ENGINE.setFlatten(false);
	}

	public void testCanSearch() throws ServiceException {
		if (canSearch())
			m_binder.putLocal("canSearch", "1");
	}
	
	
	/** Test if the search service will execute with the given configuration. 
	 * 
	 *  @return true if the search service is available 
	 * @throws ServiceException 
	 **/
	public static boolean canSearch() throws ServiceException {
		
		QACanSearch canSearch = new QACanSearch
		 (COUNTRY_DATASET, SINGLE_LINE_SEARCH_ENGINE, PRO_WEB_LAYOUT, null);
		
		try {
			QASearchOk canSearchResult = getExperianWS().doCanSearch(canSearch);			
			if (canSearchResult.getErrorCode() != null) {
				throw new QAFault(canSearchResult.getErrorCode(), 
				 canSearchResult.getErrorMessage());			
			} else {
				return canSearchResult.isIsOk();
			}
			
		} catch (QAFault e) {
			String msg = 
				"Error code: " + e.getErrorCode() 
				 + ", message: " + e.getErrorMessage();
			
			Log.error(msg);
			throw new ServiceException(msg);
		} catch (RemoteException e) {
			Log.error("Failed to call Experian web service", e);
			throw new ServiceException(e);
		}
	}
	
	/** Searches the dataset using the passed String. Expected to be
	 *  comma-delimited.
	 *  
	 *  TODO:
	 *  The returned picklist is added to the binder as a ResultSet.
	 *  
	 *  TODO:
	 *  Check if the picklist only contains a single entry with the 
	 *  isWarnInformation flag set. If this is true, there was an error
	 *  fetching the address matches. Display the warning/error message
	 *  instead of a result list.
	 *  
	 * @throws ServiceException
	 */
	public void searchAddress() throws ServiceException {
		
		canSearch();
		
		String searchStr = m_binder.getLocal("searchString");
		
		try {
			QASearch search = new QASearch
			 (COUNTRY_DATASET, SINGLE_LINE_SEARCH_ENGINE, PRO_WEB_LAYOUT, 
			  null, searchStr, null);
			
			QASearchResult results = getExperianWS().doSearch(search);
			
			QAPicklistType pickList = results.getQAPicklist();
			
			m_binder.putLocal("isLargePotential", 
			 Boolean.toString(pickList.isLargePotential()));
			
			m_binder.putLocal("pickListMoniker", pickList.getFullPicklistMoniker());
			
			if (pickList.getDisplayText() != null)
				m_binder.putLocal("displayText", pickList.getDisplayText());
			
			m_binder.putLocal("isAutoStepInSafe", 
			 Boolean.toString(pickList.isAutoStepinSafe()));
			
			PicklistEntryType[] entries = pickList.getPicklistEntry();
			
			m_binder.putLocal("numEntries", Integer.toString(entries.length));

			for (int i=0; i<entries.length; i++) {
				/** Is isWarnInformation set to true, check getPicklist() function
				 *  to see error/warning message
				 */
				
				m_binder.putLocal("Entry" + i, entries[i].getPartialAddress() + 
				 ", Moniker: " + entries[i].getMoniker() + ", warn? " + 
				 entries[i].isWarnInformation() + ", pickListValue: " + entries[i].getPicklist());
			}

		} catch (QAFault e) {
			String msg = 
				"QAFault: code: " + e.getErrorCode() 
				 + ", message: " + e.getErrorMessage();
			
			Log.error(msg);
			
			throw new ServiceException(msg);
		} catch (Exception e) {
			Log.error("Failed to call Experian web service", e);
			throw new ServiceException(e);
		}
	}
	
	/** Performs an authentication check for a given name and address.
	 *  Various response fields are placed into the binder.
	 *  
	 *  TODO: audit the response
	 *  
	 * @throws ServiceException
	 */
	public void authenticate() throws ServiceException {
		
		canSearch();
		
		// Only inserts a name field at the moment.
		String nameStr = m_binder.getLocal("name");
		
		SearchTerm[] searchTerms = new SearchTerm[authParams.length];
		
		// Build search terms list
		for (int i=0; i<authParams.length; i++) {
			SearchTerm searchTerm = new SearchTerm();
			searchTerm.setKey(authParams[i]);
			
			if (i == 0)
				searchTerm.set_value("Yes");
			else if (i == 1) {
				searchTerm.set_value(nameStr);
			} else
				searchTerm.set_value(null);
		
			searchTerms[i] = searchTerm;
		}
		
		try {
			QASearch search = new QASearch
			 (COUNTRY_DATASET, AUTHENTICATE_ENGINE, AUTH_PRO_LAYOUT, 
			  null, null, searchTerms);
			
			QASearchResult results = getExperianWS().doSearch(search);
			
			AddressLineType[] response = results.getQAAddress();
			
			if (response == null) {
				QAPicklistType pickList = results.getQAPicklist();
				
				PicklistEntryType[] entries = pickList.getPicklistEntry();
				
				m_binder.putLocal("numEntries", Integer.toString(entries.length));

				for (int i=0; i<entries.length; i++) {
					m_binder.putLocal("Entry" + i, entries[i].getPartialAddress() + 
					 ", Moniker: " + entries[i].getMoniker() + ", warn? " + 
					 entries[i].isWarnInformation() + ", pickListValue: " + entries[i].getPicklist());
				}
				
			} else {
			
				for (int i=0; i<response.length; i++) {
					m_binder.putLocal("Response" + i, response[i].getLabel() + 
					 ": " + response[i].getLine());
				}
				
			}

		} catch (QAFault e) {
			String msg = 
				"QAFault: code: " + e.getErrorCode() 
				 + ", message: " + e.getErrorMessage();
			
			Log.error(msg);
			
			throw new ServiceException(msg);
		} catch (Exception e) {
			Log.error("Failed to call Experian web service", e);
			throw new ServiceException(e);
		}
	}
	
	/** Returns an address for a given moniker. */
	public void getAddress() throws ServiceException {
		
		canSearch();
		
		String moniker = m_binder.getLocal("moniker");
		
		try {
			QAGetAddress getAddress = new QAGetAddress("experian", moniker, null);
			
			Address address = getExperianWS().doGetAddress(getAddress);		
			AddressLineType[] addressLines = address.getQAAddress();
			
			for (int i=0; i<addressLines.length; i++) {
				m_binder.putLocal("Line" + i, 
				 addressLines[i].getLabel() + ": " + addressLines[i].getLine());
			}
			
		} catch (QAFault e) {
			String msg = 
				"QAFault: code: " + e.getErrorCode() 
				 + ", message: " + e.getErrorMessage();
			
			Log.error(msg);
			
			throw new ServiceException(msg);
		} catch (Exception e) {
			Log.error("Failed to call Experian web service", e);
			throw new ServiceException(e);
		}
	}
	
	/** Returns an instance of the Aurora web service stub. */
	public static QASoapBindingStub getExperianWS() 
	 throws ServiceException {
		
		ProWebLocator locator 		= new ProWebLocator();
		QASoapBindingStub service 	= null;
		
		try {
			service = 
			 (QASoapBindingStub)locator.getQAPortType();
			
		} catch (javax.xml.rpc.ServiceException se) {
			Log.error("Failed to instantiate Experian Web Service binding stub", se);
			throw new ServiceException(se);
		}
		
		return service;
	}
	
}
