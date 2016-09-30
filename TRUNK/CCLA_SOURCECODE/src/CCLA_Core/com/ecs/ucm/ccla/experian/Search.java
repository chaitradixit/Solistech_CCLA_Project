package com.ecs.ucm.ccla.experian;

import intradoc.common.ServiceException;
import intradoc.server.Service;

import java.rmi.RemoteException;

import com.ecs.utils.Log;
import com.experian.webservice.ProWebLocator;
import com.experian.webservice.QACanSearch;
import com.experian.webservice.QAFault;
import com.experian.webservice.QASearchOk;
import com.experian.webservice.QASoapBindingStub;

public class Search extends Service {

	/** Test if the search service will execute with the given configuration. 
	 * 
	 *  @return true if the search service is available 
	 * @throws ServiceException 
	 **/
	protected  boolean canSearch() {
		
		Log.debug("starting canSearch");
		QACanSearch canSearch = new QACanSearch
		 (ExperianGlobals.COUNTRY_DATASET, ExperianGlobals.SINGLE_LINE_SEARCH_ENGINE_FLATTENED, ExperianGlobals.PRO_WEB_LAYOUT, null);
		
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
			String ErrorMessage=ExperianGlobals.CCLA_CANNOT_SEARCH_ERROR_MSG;	
			String ErrorDetail = e.getErrorMessage();
			String redirectURL = m_binder.getLocal("RedirectUrl");
			redirectURL = redirectURL + "&ErrorMessage=" + ErrorMessage + "&ErrorDetail=" + ErrorDetail;
			m_binder.putLocal("RedirectUrl", redirectURL);
			return false;
		} catch (RemoteException e) {
			Log.error("Failed to call Experian web service", e);
			String ErrorMessage=ExperianGlobals.CCLA_SERVICE_CALL_FAILED_ERROR_MSG;	
			String ErrorDetail = "Could not connect to the Experian Service.";
			String redirectURL = m_binder.getLocal("RedirectUrl");
			redirectURL = redirectURL + "&ErrorMessage=" + ErrorMessage + "&ErrorDetail=" + ErrorDetail;
			m_binder.putLocal("RedirectUrl", redirectURL);
			return false;
	} catch (ServiceException e) {
		Log.error("Failed to call Experian web service", e);
		String ErrorMessage=ExperianGlobals.CCLA_SERVICE_CALL_FAILED_ERROR_MSG;	
		String ErrorDetail = e.getMessage();
		String redirectURL = m_binder.getLocal("RedirectUrl");
		redirectURL = redirectURL + "&ErrorMessage=" + ErrorMessage + "&ErrorDetail=" + ErrorDetail;
		m_binder.putLocal("RedirectUrl", redirectURL);
		return false;
	}
	}

	/** Returns an instance of the Aurora web service stub. */
	public QASoapBindingStub getExperianWS() 
	 throws ServiceException {
		
		ProWebLocator locator 		= new ProWebLocator();
		QASoapBindingStub service 	= null;
		
		try {
			service = 
			 (QASoapBindingStub)locator.getQAPortType();
			
		} catch (javax.xml.rpc.ServiceException se) {
			Log.error("Failed to instantiate Experian Web Service binding stub", se);
			m_binder.putLocal("ErrorMessage", "Failed to instantiate Experian Web Service binding stub");
			throw new ServiceException(se);
		}
		
		return service;
	}
}
