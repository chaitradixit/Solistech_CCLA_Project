package com.ecs.ucm.ccla.aurora;

import idcbean.data.LWDataBinder;
import idcbean.data.LWResultSet;
import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.data.ResultSet;
import intradoc.data.Workspace;
import intradoc.provider.Provider;
import intradoc.provider.Providers;
import intradoc.server.Service;
import intradoc.shared.SharedObjects;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Vector;

import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.axis.client.Transport;
import org.apache.axis.transport.http.CommonsHTTPSender;
import org.apache.axis.transport.http.HTTPTransport;
import org.apache.axis.utils.Options;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.NTCredentials;

import com.aurora.webservice.Account;
import com.aurora.webservice.AccountStatus;
import com.aurora.webservice.Address;
import com.aurora.webservice.ArrayOfAccount;
import com.aurora.webservice.ArrayOfCorrespondent;
import com.aurora.webservice.ArrayOfFund;
import com.aurora.webservice.ArrayOfUnitPrice;
import com.aurora.webservice.ArrayOfUnitValuationPrice;
import com.aurora.webservice.AuroraWSLocator;
import com.aurora.webservice.AuroraWSSoap_BindingStub;
import com.aurora.webservice.BankDetails;
import com.aurora.webservice.Client;
import com.aurora.webservice.Correspondent;
import com.aurora.webservice.Fund;
import com.aurora.webservice.UnitPrice;
import com.aurora.webservice.UnitValuationPrice;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.DateFormatter;
import com.ecs.utils.stellent.LWFacade;

/** Handles requests and responses to Aurora web services.
 */
public class AuroraWebServiceHandler extends Service {
	
	/** Location of the Aurora Web Service ASMX/WSDL */
	public static final String AURORA_WS_URL = 
	 SharedObjects.getEnvironmentValue("AURORA_WebServiceEndpointURL");
	
	/** NTLM password used for web service authentication */
	public static final boolean AURORA_ENABLE_NTLM_AUTH =
	 !StringUtils.stringIsBlank(
	 SharedObjects.getEnvironmentValue("AURORA_EnableNTLMAuth"));
	
	/** NTLM username used for web service authentication */
	public static final String AURORA_AUTH_USERNAME = 
	 SharedObjects.getEnvironmentValue("AURORA_WEBSERVICE_AUTH_USERNAME");
	
	/** NTLM password used for web service authentication */
	public static final String AURORA_AUTH_PASSWORD =
	 SharedObjects.getEnvironmentValue("AURORA_WEBSERVICE_AUTH_PASSWORD");
	
	/** Determines whether the GetBankDetails service will ever be invoked. */
	public static final boolean LOOKUP_BANK_DETAILS =
	 (!StringUtils.stringIsBlank(
	  SharedObjects.getEnvironmentValue("AURORA_EnableBankDetailsLookup")));
	
	/** Maximum number of Aurora signatories that will be captured against Clients and
	 *  Accounts.
	 */
	public static final int MAXIMUM_AURORA_SIGNATORIES = 10;
	
	/** Message placed in the first Signatory Name field on an Aurora entity where the
	 *  total number of account signatories exceeds the maximum available for storage.
	 */
	public static final String SIGNATORY_LIST_OVERFLOW_MESSAGE 
	 = "See signature list held on file";
	
	/** If set, method calls to Aurora web services are diverted and return locally-
	 *  set data instead.
	 *  
	 *  Useful when testing services in dev environment where Aurora isn't available.
	 */
	public static final boolean TEST_MODE =
	 (!StringUtils.stringIsBlank(
	 SharedObjects.getEnvironmentValue("AURORA_WebServicesTestMode")));
	
	/** Determines whether the IsValidBankAccount service will be called when
	 *  checking bank account validitiy. This must be switched off in the 
	 *  test/UAT environment, as the external call made by Aurora to check 
	 *  bank details doesn't work here. */
	private static final boolean ENABLE_BANK_ACCOUNT_VALIDITY_CHECK =
	 (!StringUtils.stringIsBlank(
	  SharedObjects.getEnvironmentValue("AURORA_EnableBankAccountValidityCheck")));
	
	/** Query on our custom view which resides in the COIF database.
	 *  Will be deprecated once a getClientCorrespondents web service
	 *  is available. */
	public static final String ECS_COIF_CLIENT_CORRESPONDENT_QUERY =
	 "SELECT * FROM ECS_ClientCorrespondent WHERE AN_ContributorCode=";	
	
	/** Column names used in ResultSets to store address information 
	 *  retrieved from Aurora.
	 */
	public static final String[] AURORA_ADDRESS_FIELDS = {
		"organisation","building","street","town",
		"locality","county","country","postCode",
		"telephone","email","fax"
	};
	
	public static boolean CACHE_CORRESPONDENT_DATA = SharedObjects
		.getEnvironmentValue("CCLA_CS_CACHE_CORRESPONDENT_DATA").equals("1");
	
	public static boolean CACHE_CORRESPONDENT_ADDRESS_DATA = SharedObjects
	.getEnvironmentValue("CCLA_CS_CACHE_CORRESPONDENT_ADDRESS_DATA").equals("1");
	
	public static boolean RESOLVE_QRS_FLAG = SharedObjects
	.getEnvironmentValue("CCLA_CS_RESOLVE_QRS_FLAG_FOR_ACCOUNT").equals("1");
	
	
	/** Service method used to call any of the Aurora web services. 
	 * 
	 *  The binder must contain a methodName variable, this determines
	 *  which web service method is called. The corresponding arguments
	 *  for the method must also be present in the binder.
	 * 
	 * @throws ServiceException
	 */
	public void callAuroraWebService() throws ServiceException {
		
		String methodName = m_binder.getLocal("methodName");
		
		if (StringUtils.stringIsBlank(methodName))
			throw new ServiceException("Unable to call Aurora web service: " +
			 "methodName not present in binder.");
		
		if (methodName.equals("IsValidBankAccount")) {
			isValidBankAccount(m_binder);
			
		} else if (methodName.equals("GetAccount")) {
			getAccount(m_binder);
		
		} else if (methodName.equals("GetBankDetails")) {
			getBankDetails();
			
		} else if (methodName.equals("GetClientByClientNumber")) {
			//getClientByClientNumber(m_binder);
			
		} else if (methodName.equals("GetClientByAccountNumberExternal")) {
			getClientByAccountNumberExternal(m_binder);
			
		} else if (methodName.equals("GetAccountCorrespondentAddress")) {
			getAccountCorrespondentAddress(m_binder);
			
		} else if (methodName.equals("GetClientCorrespondentAddress")) {
			//getClientCorrespondentAddress(m_binder);
		
		} else if (methodName.equals("GetAccountsByCorrespondentCode")) {
			getAccountsByCorrespondentCode(m_binder);
	
		} else if (methodName.equals("GetCorrespondentByCorrespondentCode")) {
			//getCorrespondentByCorrespondentCode(m_binder);
		
		} else if (methodName.equals("GetAccountCorrespondentsByClientNumber")) {
			//getAccountCorrespondentsByClientNumber(m_binder);
		
		} else if (methodName.equals("GetFundByFundCode")) {
			getFundByFundCode(m_binder);
			
		} else if (methodName.equals("GetFunds")) {
			getFunds(m_binder);
		
		} else if (methodName.equals("GetUnitPrices")) {
			//getUnitPrices(m_binder);
		
		} else if (methodName.equals("GetLatestUnitPrices")) {
			getLatestUnitPrices(m_binder);
			
		} else if (methodName.equals("GetUnitValuationPrices")) {
			//getUnitValuationPrices(m_binder);
			
		} else {
			throw new ServiceException("Unknown Aurora web service method: " 
			 + methodName);
		}
	}
	
	/** Fetches a ResultSet of account data for the given company and
	 *  correspondent ID in the binder.
	 *  
	 */
	public static Account[] getAccountsByCorrespondentCode(DataBinder binder) 
	 throws ServiceException {
		String company 		= binder.getLocal("company");
		String corrStr		= binder.getLocal("correspondentId");
		String fundFilter	= binder.getLocal("fundFilter");
		
		boolean filterClosed = !StringUtils.stringIsBlank(
		 binder.getLocal("filterClosedAccounts"));
		
		boolean filterEmpty  = !StringUtils.stringIsBlank(
		 binder.getLocal("filterEmptyAccounts"));
		
		boolean showUnitPrices	= !StringUtils.stringIsBlank(
		 binder.getLocal("getUnitPrices"));
		
		int corrId = -1;
		
		try {
			corrId = Integer.parseInt(corrStr);
		} catch (NumberFormatException e) {
			String msg = "Correspondent ID '" + corrStr 
			 + "' was not a valid number.";
			
			Log.debug(msg);
			throw new ServiceException(msg);
		}
			
		Account[] accounts = null;
		
		try {			
			ArrayOfAccount arrayOfAccount = getAuroraWS().
			 getAccountsByCorrespondentCode(company, corrId);
			
			accounts = arrayOfAccount.getAccount();
			
			Log.debug("Found " + accounts.length + " accounts " +
			 "for company: " + company + ", correspondent ID: " + corrStr);
			
			String[] filter = null;
			
			if (!StringUtils.stringIsBlank(fundFilter)) {
				Log.debug("Filtering accounts to match fund codes: " 
				 + fundFilter);
				
				filter = fundFilter.split(",");
			}
			
			UnitPrice[] unitPrices = null;
			
			if (showUnitPrices) {
				// Fetch latest unit prices and pass to ResultSet
				// generator function - this will add extra data
				// to the ResultSet showing unit price and current value
				ArrayOfUnitPrice arrayOfUnitPrice = getAuroraWS().
				 getLatestUnitPrices(company);
				
				unitPrices = arrayOfUnitPrice.getUnitPrice();
			}
			
			DataResultSet rsCorrespondentAccounts =
			 getAccountResultSet(accounts, filter, 
			 filterClosed, filterEmpty, unitPrices);
			
			binder.addResultSet("rsCorrespondentAccounts",
			 rsCorrespondentAccounts);
			
		} catch (Exception e) {
			Log.error("Failed to get accounts by correspondent: " + e, e);
			throw new ServiceException(e);
		}
		
		if (accounts != null)
			Log.debug("Found " + accounts.length + " accounts");
		
		return accounts;
	}
	
	/*
	public static Correspondent getCorrespondentByCorrespondentCode(DataBinder binder) 
	 throws ServiceException {
		
		String company 	= binder.getLocal("company");
		String corrStr	= binder.getLocal("correspondentId");
		
		Log.debug("Parsing int: " + corrStr);
		
		int corrId = Integer.parseInt(corrStr);
		
		Correspondent corr = null;
		
		try {
			Log.debug("Calling GetCorrespondentByCorrespondentCode with company: " 
			 + company + ", corrId=" + corrId);
			
			corr = getAuroraWS().getCorrespondentByCorrespondentCode
			 (company, corrId);
			
			Log.debug("Found correspondent: " + corr.getName());
			
			Correspondent[] correspondent =
			 new Correspondent[] {corr};
			
			if(CACHE_CORRESPONDENT_DATA){
				updateCorrespondentCache(corr);
			}else{
				Log.debug("Skipping correspondent cache update.");
			}
			
			if(CACHE_CORRESPONDENT_ADDRESS_DATA){
				int addressId = getPersonAddressId(corr);
				
				if(addressId != 0)
					updateCorrespondentAddressCache(corr, addressId);
			}else{
				Log.debug("Skipping correspondent address cache update.");
			}
			
			DataResultSet rsCorrespondent =
			 getCorrespondentResultSet(correspondent);
			
			binder.addResultSet("rsCorrespondent", rsCorrespondent);
			
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
		return corr;
	}
	*/
	
	/** Returns bank name and branch name/address for the given
	 *  sort code.
	 *  
	 * @param sortCode
	 * @return
	 * @throws ServiceException
	 */
	public static BankDetails getBankDetails(String sortCode)
	 throws ServiceException {
		
		if (!LOOKUP_BANK_DETAILS) {
			Log.debug("Skipping Aurora bank details lookup.");
			return null;
		}
		
		try {
			BankDetails details = getAuroraWS().getBankDetails(sortCode);
			return details;
			
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	public void getBankDetails() throws ServiceException {
		
		String sortCode = m_binder.getLocal("sortCode");
		
		if (StringUtils.stringIsBlank(sortCode))
			throw new ServiceException("No sort code passed.");
		
		BankDetails details = getBankDetails(sortCode);
		
		m_binder.putLocal("branchTitle", details.getBranchTitle());
		m_binder.putLocal("bankName", details.getBankName());
	}
	
	/**
	 * Get the revelant address ID associated with the correspondent
	 * 
	 * @param corr
	 * @return
	 */
	/*
	private static int getPersonAddressId(Correspondent corr) {
		String corrId = corr.getCorrespondentCode();
		
		Log.debug("Fetching ADDRESS_ID for person: " + corrId);
		
		LWDataBinder binder = new LWDataBinder();
		binder.putLocal("IdcService", "CCLS_CS_GET_ADDRESS_ID_FOR_PERSON_ID");
		binder.putLocal("personId", corrId);
		
		LWFacade lwf = new LWFacade();
		
		try{
			LWDataBinder lwdb = lwf.executeService(binder);
			
			LWResultSet lwrs = lwdb.getResultSet("rsAddressId");
			
			if(!lwrs.isEmpty()){
				String addressId = getFieldValue(lwrs, "ADDRESS_ID");
				Log.debug("Returning addressId of " + addressId);
				return Integer.parseInt(addressId);
			}

		}catch(Exception e){
			Log.warn("Unable to resolve ADDRESS_ID:" + e.getMessage());
			return 0;
		}
		
		return 0;
	}
	*/

	/**
	 * Returns the requested paramter from the passed search results.
	 * 
	 * @param searchResults
	 * @param fieldName
	 * @return
	 */
	private static String getFieldValue(LWResultSet searchResults, String fieldName) {
		return searchResults.getStringValue(searchResults.getFieldIndex(fieldName));
	}
	
	/**
	 * Caches correspondent address data to CS_ADDRESS table
	 * @param address
	 */
	private static void updateCorrespondentAddressCache(Correspondent corr,
			int addressId) {
		Log.debug("Updating correspondent address cache >>");
		
		Address address = corr.getAddress();
		
		LWDataBinder binder = new LWDataBinder();
		
		binder.putLocal("IdcService", 		"CCLA_CS_INSERT_ADDRESS");
		binder.putLocal("addressId", 		Integer.toString(addressId));
		binder.putLocal("building", 		address.getBuilding());
		binder.putLocal("country", 			address.getCountry());
		binder.putLocal("county", 			address.getCounty());
		binder.putLocal("locality", 		address.getLocality());
		binder.putLocal("organisation", 	address.getOrganisation());
		binder.putLocal("postcode", 		address.getPostCode());
		binder.putLocal("street", 			address.getStreet());
		binder.putLocal("towncity", 		address.getTown());
		binder.putLocal("lastUpdated", 		DateFormatter.getTimeStamp());
		
		LWFacade lwf = new LWFacade();
		
		try{
			lwf.executeService(binder);
			Log.info("Created cache for ADDRESS_ID " 
					+ addressId + " <<");
		}catch(Exception e1){
			binder.putLocal("IdcService", "CCLA_CS_UPDATE_ADDRESS");
			
			try{
				lwf.executeService(binder);
			}catch(Exception e2){
				Log.warn("Unable to update address cache for " +
						"ADDRESS_ID " + addressId);
				//Don't throw any exceptions, as it should fail silently
			}
			
			Log.info("Updated address cache for ADDRESS_ID " 
					+ addressId + " <<");
		}
	}

	/** Fetches correspondents by client number.
	 * 
	 * @param binder
	 * @throws ServiceException
	 */
	/*
	public static void getAccountCorrespondentsByClientNumber(DataBinder binder) 
	 throws ServiceException {
		Log.debug("Calling GetAccountCorrespondentsByClientNumber...");
		
		String company 		= binder.getLocal("company");
		String clientNumber	= binder.getLocal("clientNumber");
		
		Correspondent[] correspondents = null;
		
		try {
			Log.debug("Calling GetAccountCorrespondentsByClientNumber with company: " 
			 + company + ", clientNumber=" + clientNumber);
			
			ArrayOfCorrespondent arrayOfCorrespondents = getAuroraWS().
			 getAccountsCorrespondentsByClientNumber(company, clientNumber);
			
			correspondents = arrayOfCorrespondents.getCorrespondent();
			
			Log.debug("Found " + correspondents.length + " correspondents.");
			
			binder.addResultSet("rsClientCorrespondents", 
			 getCorrespondentResultSet(correspondents));
			
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	*/
	
	/** Called via Aurora webservice.
	 *  
	 *  Determines whether a given sort code and account number
	 *  are valid together.
	 *  
	 *  Always returns false in test/UAT env.
	 * 
	 * @throws ServiceException
	 */
	public static void isValidBankAccount(DataBinder binder) 
	 throws ServiceException {
		
		String sortCode 	= binder.getLocal("sortCode");
		String accNumber	= binder.getLocal("accNumber");
		
		boolean isValid   = isValidBankAccount(sortCode, accNumber);
		
		Log.debug("isValidBankAccount: " + Boolean.toString(isValid));
		binder.putLocal("isValidBankAccount", Boolean.toString(isValid));
	}
	
	public static boolean isValidBankAccount
		(String sortCode, String accountNumber) throws ServiceException {
		
		Log.debug("Checking validity of Bank Account. Sort code: " + sortCode +
		 ", account no: " + accountNumber);
		
		if (!ENABLE_BANK_ACCOUNT_VALIDITY_CHECK) {
			Log.debug("Bank Account validity checking is switched off - " +
			 "returning true.");
			return true;
		}
		
		try {
			return
			 getAuroraWS().isValidBankAccount(sortCode, accountNumber);
			
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	/** Fetches client data via Aurora web service.
	 *  
	 *  Requires a company and external account number present in the binder.
	 * 
	 * @throws ServiceException
	 */
	public static void getClientByAccountNumberExternal(DataBinder binder) 
	 throws ServiceException {
		
		String company 		= binder.getLocal("company");
		String accNumber	= binder.getLocal("accNumberExternal");
		
		Client client		= null;
		
		try {
			client = getAuroraWS().getClientByAccountNumberExternal(company, accNumber);
			
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
		if (client != null) {
			Log.debug("getClientByAccountNumberExternal: returned client instance: " +
			 client.getName());
			
			addClientToBinder(client, binder);
			
		} else {
			Log.debug("getClientByAccountNumberExternal: returned null client for " +
			 "company " + company + ", acc no. " + accNumber);
		}
	}
	
	/** Fetches client details via Aurora web service.
	 * 
	 *  Given a company and client number, this method fetches the
	 *  client name, exemption number and address.
	 *  
	 * @throws ServiceException
	 */
	/*
	public static void getClientByClientNumber(DataBinder binder) throws ServiceException {
		
		String company 		= binder.getLocal("company");
		String clientNumber	= binder.getLocal("clientNumber");
		
		Client client		= null;
		
		try {
			client = getAuroraWS().getClientByClientNumber(company, clientNumber);
			
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
		if (client != null) {
			Log.debug("getClientByClientNumber: returned client instance: " +
			 client.getName());
			
			addClientToBinder(client, binder);
			
		} else {
			Log.debug("getClientByClientNumber: returned null client for " +
			 "company " + company + ", client no. " + clientNumber);
		}
	}
	*/
	
	/** Fetches account details via Aurora web service.
	 * 
	 *  Requires an extended account number in the binder, i.e.
	 *  "<client number><account number><fund>"
	 * 
	 * @throws ServiceException
	 */
	public static void getAccount(DataBinder binder) throws ServiceException {
		
		String company 		= binder.getLocal("company");
		String accNumber	= binder.getLocal("accNumberExternal"); 
		
		boolean showUnitPrices	= !StringUtils.stringIsBlank(
		 binder.getLocal("getUnitPrices"));
		
		Account account   = null;
		UnitPrice[] unitPrices = null;
		
		try {
			account = getAccountByAccountNumberExternal(company, accNumber);
			
			if (showUnitPrices) {
				// Fetch latest unit prices and pass to ResultSet
				// generator function - this will add extra data
				// to the ResultSet showing unit price and current value
				ArrayOfUnitPrice arrayOfUnitPrice = getAuroraWS().
				 getLatestUnitPrices(company);
				
				unitPrices = arrayOfUnitPrice.getUnitPrice();
			}
			
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
		if (account != null) {
			Log.debug("getAccount: returned account instance: " +
			 account.getAccountNumberExternal());
		
			Account[] accountArray = new Account[] {account};
			
			binder.addResultSet("rsAccount", 
			 getAccountResultSet(accountArray, null, false, false, unitPrices));
			
		} else {
			Log.debug("getAccount: returned null account for " +
			 "company " + company + ", ext. account no. " + accNumber);
		}
	}
	
	public static Account getAccountByAccountNumberExternal(String company, String accNumExt) 
	 throws ServiceException {
		
		try {
			Log.debug("Fetching account " + accNumExt + " via Aurora web service");
			Account account = getAuroraWS().getAccountByAccountNumberExternal(company, accNumExt);
			return account;
			
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	/** Fetches client data via Aurora web service.
	 * 
	 * @throws ServiceException
	 */
	public static void getAccountCorrespondentAddress(DataBinder binder) 
	 throws ServiceException {
		
		String company 		= binder.getLocal("company");
		String accNumber	= binder.getLocal("accNumberExternal");
		
		Address address   	= null;
		
		try {
			address = getAuroraWS().
			 getAccountCorrespondentAddress(company, accNumber);
			
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
		if (address != null) {
			Log.debug("getAccountCorrespondentAddress: " + address.toString());
			//addAddressToBinder(address, binder);
			
			DataResultSet rsAddress = getAddressResultSet(address);
			binder.addResultSet("rsAccountCorrespondentAddress", rsAddress);
		}
	}
	
	/** Fetches client data via Aurora web service.
	 * 
	 * @throws ServiceException
	 */
	/*
	public static void getClientCorrespondentAddress(DataBinder binder) 
	 throws ServiceException {
		
		String company 		= binder.getLocal("company");
		String clientNumber	= binder.getLocal("clientNumber");
		
		Address address   	= null;
		
		try {
			address = getAuroraWS().getClientCorrespondentAddress(company, clientNumber);
			
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
		if (address != null) {
			Log.debug("getClientCorrespondentAddress: " + address.toString());
			//addAddressToBinder(address, binder);
			
			DataResultSet rsAddress = getAddressResultSet(address);
			binder.addResultSet("rsClientCorrespondentAddress", rsAddress);
		}
	}
	*/
	
	/** Fetches the fund for the given company and fund code.
	 *  
	 *  Added to the binder as a ResultSet called rsFund.
	 * 
	 * @param binder
	 * @throws ServiceException
	 */
	public static void getFundByFundCode(DataBinder binder)
	 throws ServiceException {
		
		String company 		= binder.getLocal("company");
		String fundCode		= binder.getLocal("fundCode");
		
		Fund fund	   		= null;
		
		try {
			fund = getAuroraWS().getFundByFundCode(company, fundCode);
			
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
		if (fund != null) {
			Log.debug("getFundByFundCode: " + fund.getFundName());
			
			DataResultSet rsFund = getFundResultSet(new Fund[] {fund});
			binder.addResultSet("rsFund", rsFund);
		}
	}
	
	/** Fetches all funds for a given company and adds them to the
	 *  binder as rsFunds.
	 *  
	 * @param binder
	 * @throws ServiceException
	 */
	public static Fund[] getFunds(DataBinder binder) 
	 throws ServiceException {
		
		String company 		= binder.getLocal("company");
		
		Fund[] funds   		= null;
		
		try {
			ArrayOfFund arrayOfFunds = getAuroraWS().getFunds(company);
			funds = arrayOfFunds.getFund();
			
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
		if (funds != null) {
			Log.debug("getFunds for company: " + company + " returned " 
			 + funds.length + " funds.");
			
			DataResultSet rsFund = getFundResultSet(funds);
			binder.addResultSet("rsFunds", rsFund);
		}
		
		return funds;
	}
	
	/** Fetches unit prices for a given company and valuation point.
	 * 
	 * @throws ServiceException
	 */
	/*
	public static void getUnitPrices(DataBinder binder) throws ServiceException {
		
		String company 			= binder.getLocal("company");
		String valuationPoint	= binder.getLocal("valuationPoint");
		
		UnitPrice[] unitPrices	= null;
	
		try {
			ArrayOfUnitPrice arrayOfUnitPrices = 
			 getAuroraWS().getUnitPrices(company, valuationPoint);
			
			unitPrices = arrayOfUnitPrices.getUnitPrice();
			
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
		if (unitPrices != null) {
			Log.debug("getUnitPrices: found " + unitPrices.length 
			 + " unit prices.");
			
			DataResultSet rsUnitPrices = getUnitPriceResultSet(unitPrices);
			binder.addResultSet("rsUnitPrices", rsUnitPrices);
		}
	}
	*/
	
	public static UnitPrice[] getLatestUnitPrices(DataBinder binder) throws ServiceException {
		
		String company 			= binder.getLocal("company");
		
		UnitPrice[] unitPrices	= null;
	
		try {
			ArrayOfUnitPrice arrayOfUnitPrices = 
			 getAuroraWS().getLatestUnitPrices(company);
			
			unitPrices = arrayOfUnitPrices.getUnitPrice();
			
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
		if (unitPrices != null) {
			Log.debug("getLatestUnitPrices: found " + unitPrices.length 
			 + " unit prices.");
			
			DataResultSet rsUnitPrices = getUnitPriceResultSet(unitPrices);	
			binder.addResultSet("rsLatestUnitPrices", rsUnitPrices);
		}
		
		return unitPrices;
	}
	
	/*
	public static void getUnitValuationPrices(DataBinder binder) 
	 throws ServiceException {
		
		String company 			= binder.getLocal("company");
		String valuationPoint	= binder.getLocal("valuationPoint");
		
		UnitValuationPrice[] unitValuationPrices	= null;
	
		try {
			ArrayOfUnitValuationPrice arrayOfUnitValuationPrices = 
			 getAuroraWS().getUnitValuationPrices
			 (company, valuationPoint);
			
			unitValuationPrices = 
			 arrayOfUnitValuationPrices.getUnitValuationPrice();
			
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
		if (unitValuationPrices != null) {
			Log.debug("getLatestUnitPrices: found " + unitValuationPrices.length 
			 + " unit prices.");
			
			DataResultSet rsUnitPrices = 
			 getUnitValuationPriceResultSet(unitValuationPrices);	
			binder.addResultSet("rsUnitValuationPrices", rsUnitPrices);
		}
	}
	*/
	
	/** Generates a single-row ResultSet containing all data from
	 *  the given Address object, including telephone number.
	 * @param address
	 * @return
	 */
	private static DataResultSet getAddressResultSet(Address address) {
		
		String[] cols = new String[] {
			"Organisation","Building","Street","Town","Locality",
			"County","Country","PostCode","Telephone"
		};
		
		DataResultSet rsAddress = new DataResultSet(cols);
		
		Vector values = new Vector();
		
		values.add(address.getOrganisation());
		values.add(address.getBuilding());
		values.add(address.getStreet());
		values.add(address.getTown());
		values.add(address.getLocality());
		values.add(address.getCounty());
		values.add(address.getCountry());
		values.add(address.getPostCode());
		values.add(address.getTelephone());
		
		rsAddress.addRow(values);
		
		return rsAddress;
	}
	
	
	/** Adds a com.aurora.webservice.Address instance to the given DataBinder.
	 *  
	 *  @deprecated in favour of getAddressResultSet
	 *  
	 * @param address
	 * @param binder
	 * @return
	 */
	private static DataBinder addAddressToBinder(Address address, DataBinder binder) {
		
		if (address.getOrganisation() != null)
			binder.putLocal("addr_Organisation", address.getOrganisation());
		
		if (address.getBuilding() != null)
			binder.putLocal("addr_Building", address.getBuilding());
		
		if (address.getStreet() != null)
			binder.putLocal("addr_Street", address.getStreet());
		
		if (address.getTown() != null)
			binder.putLocal("addr_Town", address.getTown());
		
		if (address.getLocality() != null)
			binder.putLocal("addr_Locality", address.getLocality());
		
		if (address.getCounty() != null)
			binder.putLocal("addr_County", address.getCounty());
		
		if (address.getCountry() != null)
			binder.putLocal("addr_Country", address.getCountry());
		
		if (address.getPostCode() != null)
			binder.putLocal("addr_PostCode",address.getPostCode());
		
		return binder;
	}
	
	/** Adds data from the passed Client instance into the passed
	 *  DataBinder.
	 *  
	 * @param client
	 * @param binder
	 */
	private static DataBinder addClientToBinder(Client client, DataBinder binder) {
		
		Client[] clients = new Client[] {client};
		DataResultSet rsClient = getClientResultSet(clients);
		
		binder.addResultSet("rsClient", rsClient);
		
		return binder;
	}
	
	/** Returns an instance of the Aurora web service stub. 
	 * @throws MalformedURLException */
	public static AuroraWSSoap_BindingStub getAuroraWS() 
	 throws ServiceException {
	
		try {
			AuroraWSLocator locator = null;
			
			if (AURORA_ENABLE_NTLM_AUTH) {
				locator = new AuroraWSLocator(getEngineConfiguration());
			} else {
				locator = new AuroraWSLocator();
			}
			
			locator.setAuroraWSSoapEndpointAddress(AURORA_WS_URL);
			
			AuroraWSSoap_BindingStub service = null;
			
			service = 
			 (AuroraWSSoap_BindingStub)locator.getAuroraWSSoap();
			
			return service;
		} catch (Exception e) {
			String msg = "Failed to instantiate Aurora Web Service binding stub: " +
			 e.getMessage();
			
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
		
		
	}
	
	/** Used to set NTLM authentication credentials on Aurora
	 *  web service requests.
	 *  
	 *  Referenced in the createCall() method in the AuroraWSSoap
	 *  class.
	 *  
	 * @param call
	 */
	public static void setAuroraWSCredentials(Call call) {
		
		if (AURORA_ENABLE_NTLM_AUTH) {
	        // TM: use HttpClient for NTLM auth.
	        CommonsHTTPSender reqConnectionHandler = new CommonsHTTPSender();
	        CommonsHTTPSender respConnectionHandler = new CommonsHTTPSender();
	        
	        call.setClientHandlers(reqConnectionHandler,respConnectionHandler);
	        
			call.setUsername(AURORA_AUTH_USERNAME);
			call.setPassword(AURORA_AUTH_PASSWORD);
		}
	}
	
	public static Credentials getNTLMCredentials() {
		
		Credentials ntlmCredentials =
	     new NTCredentials(
	     AURORA_AUTH_USERNAME.substring(
	      "CCLA\\".length(), AURORA_AUTH_USERNAME.length()
	     ),
	     AURORA_AUTH_PASSWORD,"","CCLA");
	    
		//client.getState().setProxyCredentials(null,proxyCred);
		
		return ntlmCredentials;
	}
	
	/** Returns a list of correspondents for the clientNumber in
	 *  the binder.
	 *  
	 *  Currently uses our provider to query a custom view directly.
	 *  This will be replaced by a Kainos web service shortly.
	 */
	public static void getCorrespondentsForClient
	 (DataBinder binder, Workspace workspace) 
	 throws ServiceException, DataException {
		
		String clientNumStr = binder.getLocal("clientNumber");
		ResultSet rsClient 	= binder.getResultSet("rsClient");
		
		if (rsClient == null || rsClient.isEmpty()) {
			Log.debug("No rsClient ResultSet in binder, won't bother " +
			 "searching for correspondents.");
			return;
		}
		
		// Convert the client number into an Integer.
		int clientNum = Integer.parseInt(clientNumStr);
		
		Workspace coifWorkspace = getCOIFWorkspace();
		
		String query = ECS_COIF_CLIENT_CORRESPONDENT_QUERY;
		query += clientNum;
		
		Log.debug("Executing query on AuroraCOIF provider: " + query);
		
		ResultSet rs = coifWorkspace.createResultSetSQL(query);
		
		DataResultSet clientCorrespondents = new DataResultSet();
		clientCorrespondents.copy(rs);
		
		Log.debug("Found " + clientCorrespondents.getNumRows() + 
		 " correspondents.");
		
		binder.addResultSet("rsClientCorrespondents", 
		 clientCorrespondents);
	}
	
	/** Takes an array of correspondents and returns an 
	 *  equivalent ResultSet (includes address data)
	 *  
	 * @param correspondents
	 * @return
	 */
	public static DataResultSet getClientResultSet
	 (Client[] clients) {
		
		// Standard client fields
		String[] cols = new String[] {
			"clientNumber","name","exemptionNumber","exemptionType"
		};
		
		// Combine address fields with above array
		String[] allCols = 
		 new String[cols.length+AURORA_ADDRESS_FIELDS.length];

		System.arraycopy(cols, 0, allCols, 0, cols.length);
		System.arraycopy(AURORA_ADDRESS_FIELDS, 0, allCols, 
		 cols.length, AURORA_ADDRESS_FIELDS.length);
		
		DataResultSet rsCorrespondents = new DataResultSet(allCols);
		
		for (int i=0; i<clients.length; i++) {	
			Client client = clients[i];
			
			Vector v = new Vector();
			
			v.add(client.getClientNumber());
			v.add(client.getName());
			v.add(client.getExemptionNumber());
			v.add(client.getExemptionType().getValue());
			
			Address address = client.getAddress();
			addAddressFieldsToVector(address, v);
			
			rsCorrespondents.addRow(v);
		}
		
		// Log.debug("Created ResultSet containing " + 
		//  rsCorrespondents.getNumRows() + " correspondents.");
		
		return rsCorrespondents;
	}
	
	/** Takes an array of correspondents and returns an 
	 *  equivalent ResultSet (includes address data)
	 *  
	 * @param correspondents
	 * @return
	 */
	public static DataResultSet getCorrespondentResultSet
	(Correspondent[] correspondents) {

		// Standard correspondent fields
		String[] cols = new String[] {
			"code","typeCode","name","salutation",
		};

		// Combine address fields with above array
		String[] allCols = 
		 new String[cols.length+AURORA_ADDRESS_FIELDS.length];

		System.arraycopy(cols, 0, allCols, 0, cols.length);
		System.arraycopy(AURORA_ADDRESS_FIELDS, 0, allCols, 
		 cols.length, AURORA_ADDRESS_FIELDS.length);

		DataResultSet rsCorrespondents = new DataResultSet(allCols);

		for (int i=0; i<correspondents.length; i++) {	
			Correspondent correspondent = correspondents[i];

			Vector v = new Vector();

			v.add(correspondent.getCorrespondentCode());
			v.add(correspondent.getTypeCode());
			v.add(correspondent.getName());
			v.add(correspondent.getSalutation());

			Address address = correspondent.getAddress();
			addAddressFieldsToVector(address, v);

			rsCorrespondents.addRow(v);
		}

		// Log.debug("Created ResultSet containing " + 
		//  rsCorrespondents.getNumRows() + " correspondents.");

		return rsCorrespondents;
	}

	/** Takes an array of accounts and returns an equivalent ResultSet.
	 *  
	 *  Includes the account subtitle, status, number of units and cash
	 *  value.
	 *  
	 * @param accounts 		array of Account instances
	 * @param fundFilter 	array of Fund codes to filter by. If this is non-null,
	 * 						only accounts with matching fund codes will be
	 * 						added to the ResultSet.
	 * @param filterClosed	if true, no closed accounts are returned. If false,
	 * 						all accounts are returned regardless of status.
	 * @param filterEmpty	if true, no empty accounts are returned (i.e. no
	 * 						units)
	 * @param unitPrice		array of UnitPrices (optional)
	 * @return
	 */
	public static DataResultSet getAccountResultSet
	 (Account[] accounts, String[] fundFilter, 
	  boolean filterClosed, boolean filterEmpty, UnitPrice[] unitPrices) {
		
		int filterCount = 0;
		
		String[] cols = new String[] {
			"accountNumberExternal","accountNumber","clientNumber","correspondentCode",
			"subtitle",
			"fundCode","bankAccountNumberWithdrawal","bankSortCodeWithdrawal",
			"getBuildingSocietyNumberWithdrawal","dateOpened","status",
			"units","unitPrice","currentValue","cash","QRS","clientConfirmed","accStatus"
		};
		
		DataResultSet rsAccounts = new DataResultSet(cols);
		
		for (int i=0; i<accounts.length; i++) {	
			Account account = accounts[i];
			
			AccountStatus accStatus 	= account.getStatus();
			String status 				= accStatus.getValue();
			
			if (filterClosed && status.startsWith("CLOS")) {
				Log.debug("Filtered out closed account: " + 
				 account.getAccountNumberExternal());
				
				filterCount++;
				continue;
			}
			
			String fundCode = account.getFundCode();
			
			if (fundFilter != null) {
				boolean addAccount = false;
				
				for (int j=0; j<fundFilter.length; j++) {	
					if (fundFilter[j].equals(fundCode))
						addAccount = true;
				}
				
				if (!addAccount) {
					// Account fund code not present in fund filter list,
					// skip to next account in array.
					Log.debug("Filtered out account with fund: " + fundCode);
					
					filterCount++;
					continue;
				}
			}
			
			if (filterEmpty) {
				// Skip this account if it has no units.
				if (account.getUnits() == 0D) {
					Log.debug("Filtered empty account: " 
					 + account.getAccountNumberExternal());
					
					filterCount++;
					continue;
				}
			}
				
			
			Vector<String> v = new Vector<String>();
			
			String accNumExt = account.getAccountNumberExternal().trim();
			v.add(accNumExt);
			
			// Take the last 5 characters of the extended account
			// number to use as the account number.
			v.add(accNumExt.substring(5, accNumExt.length()));
			
			Integer clientNumber = account.getClientNumber();
			if (clientNumber == null)
				v.add("");
			else
				v.add(CCLAUtils.padClientNumber(Integer.toString(clientNumber)));
			
			v.add(Integer.toString(account.getCorrespondentCode()));
			
			v.add(account.getSubtitle());
			
			v.add(account.getFundCode());
			v.add(
			 CCLAUtils.padBankAccountNumber(
			  Integer.toString(account.getBankAccountNumberWithdrawal())));
			
			v.add(
			 CCLAUtils.padSortCode(		
			 Integer.toString(account.getBankSortCodeWithdrawal())));
			
			v.add(
			 CCLAUtils.padBankAccountNumber(
			 account.getAccountShortNameOrBuildingSocietyReferenceWithdrawal()));
			
			v.add(DateFormatter.getTimeStamp(account.getDateOpened().getTime()));
			v.add(account.getStatus().getValue());
			
			// Ensure unit value is returned as an int value, to 4 decimal places
			NumberFormat unitCountFormat = new DecimalFormat("0.00##");
			v.add(unitCountFormat.format(account.getUnits()));
			
			// Calculate Unit Value/Current Value using UnitPrice array, 
			// if present.
			// Ensure current value is formatted in currency style. Bear
			// in mind Unit Values are returned in pence not pounds.
			
			if (unitPrices != null) {
				BigDecimal unitPrice = null;
				
				for (int j=0; j<unitPrices.length; j++) {	
					if (unitPrices[j].getFundCode().equals(fundCode)) {
						unitPrice = unitPrices[j].getBasicPrice();
						break;
					}
				}
				
				if (unitPrice != null) {
					// Divide unit price by 100 to obtain price in pounds
					double dUnitPrice = unitPrice.doubleValue();
					dUnitPrice = dUnitPrice/100D;
					
					double currentValue = dUnitPrice*account.getUnits();
				
					v.add(NumberFormat.getCurrencyInstance().format(dUnitPrice));
					v.add(NumberFormat.getCurrencyInstance().format(currentValue));
				} else {
					v.add(""); v.add(""); // unit prices unavailable
				}
				
			} else {
				v.add(""); v.add(""); // unit prices unavailable
			}
			
			// Ensure cash amount is formatted in currency style
			String cash = NumberFormat.getCurrencyInstance().format(account.getCash());
			v.add(cash);
			
			//Add QRS flag 'Y' or 'N' or '?' for errors. QRS database requires the account
			//number to be in the form of client number concatenated with account #
			String qrsFlag = getQrsFlagForAccount(account.getAccountNumberExternal());
			v.add(qrsFlag);
			
			//Add flag for CLIENT_CONFIRMED and ACCOUNT_STATUS from CS_FUND_TRANSFERS table.
			/*
			LWResultSet rsFund = getFundRsForAccount("COIF", 
					account.getClientNumber(), accNumExt.substring(5, accNumExt.length()));
			*/
			
			String clientConfirmed = "";
			String accountStatus = "";
			
			/*
			if(rsFund != null && !rsFund.isEmpty()){
				clientConfirmed = rsFund.getStringValue("CLIENT_CONFIRMED");
				accountStatus 	= rsFund.getStringValue("ACCOUNT_STATUS");
			}
			*/
			 
			v.add(clientConfirmed);
			v.add(accountStatus);
			
			rsAccounts.addRow(v);
		}
		
		Log.debug("Created ResultSet containing " + 
		 rsAccounts.getNumRows() + " accounts (filtered out " + filterCount + ")");
		
		return rsAccounts;
	}
	
	/** @deprecated
	 * 
	 *  Calls deprecated service GET_FUND_TRANSFERS_BY_ACCOUNT
	 *  
	 * @param company
	 * @param clientId
	 * @param account
	 * @return
	 */
	private static LWResultSet getFundRsForAccount(String company, 
			String clientId, String account) {
		
		try{
			LWDataBinder binder = new LWDataBinder();
			binder.putLocal("IdcService", "CCLA_CS_GET_FUND_TRANSFERS_BY_ACCOUNT");
			binder.putLocal("company", company);
			binder.putLocal("clientId", clientId);
			binder.putLocal("accountNumber", account);
			
			LWFacade lwf = new LWFacade();
			
		
			LWResultSet lwrs = lwf.executeService(binder,"rsTransfers");
			
			return lwrs;
		}catch(Exception e){
			Log.error("Unable to retrive CLIENT_CONFIRMED field for account " 
					+ account + ":" + e.getMessage());
			return null;
		}		
	}

	private static String getQrsFlagForAccount(String accountNumber) {
		if(RESOLVE_QRS_FLAG){
			Log.debug("Getting QRS flag for account: '" + accountNumber + "' >>");
			
			Workspace qrsWorkspace = null;
			
			try{
				Provider qrsProvider = Providers.getProvider("QRS");	
				if (qrsProvider != null)
					qrsWorkspace = (Workspace)qrsProvider.getProvider();
				else {
					Log.warn("Unable to retrieve QRS provider");
					return "?";
				}
				
				if (qrsWorkspace == null) {
					Log.warn("Unable to retrieve QRS workspace instance");
				} else {
					//Got the provider
					String sql = "SELECT * FROM tblAccounts WHERE AccNumExt='" 
					+ accountNumber + "'";
					
					Log.debug("Calling QRS provider with query: " + sql);
					ResultSet rs = qrsWorkspace.createResultSetSQL(sql);
					
					String rtnStr = "N";
					
					if(!rs.isEmpty())
						rtnStr = "Y";
					
					Log.debug("Return QRS flag: " + rtnStr + " <<");
					
					return rtnStr;
				}
			} catch (Exception e) {
				Log.warn("Unable to access QRS DB provider. Returning empty QRS Flag");
			}
		}
		
		return "?";
	}
	
	/** Takes an array of unit prices and returns an equivalent ResultSet.
	 *  
	 * @param accounts
	 * @return
	 */
	public static DataResultSet getUnitPriceResultSet(UnitPrice[] unitPrices) {
		
		DataResultSet rsUnitPrices = new DataResultSet( new String[]
		 {"fundCode","status",
		  "basicPrice","bidPrice","offerPrice",
		  "userName","valuationPoint"});
		
		for (int i=0; i<unitPrices.length; i++) {
			UnitPrice unitPrice = unitPrices[i];
			
			Vector v = new Vector();
			
			v.add(unitPrice.getFundCode());
			v.add(unitPrice.getStatus());
			
			String basicPrice = 
			 NumberFormat.getCurrencyInstance().format(unitPrice.getBasicPrice());
			v.add(basicPrice);
			
			v.add(unitPrice.getBidPrice().toString());
			v.add(unitPrice.getOfferPrice().toString());
			v.add(unitPrice.getUserName());
			v.add(DateFormatter.getTimeStamp(
			 unitPrice.getValuationPoint().getTime()));
			
			rsUnitPrices.addRow(v);
		}
		
		return rsUnitPrices;
	}
	
	/** Takes an array of unit valuation prices and returns an 
	 *  equivalent ResultSet.
	 *  
	 * @param accounts
	 * @return
	 */
	public static DataResultSet getUnitValuationPriceResultSet
	 (UnitValuationPrice[] unitValuationPrices) {
		
		DataResultSet rsUnitPrices = new DataResultSet( new String[]
		 {"status",
		  "basicPrice","bidPrice","fundCode",
		  "offerPrice","userName","valuationPoint"});
		
		for (int i=0; i<unitValuationPrices.length; i++) {
			UnitValuationPrice unitPrice = unitValuationPrices[i];
			
			Vector v = new Vector();
			
			v.add(unitPrice.getBasicPrice());
			v.add(unitPrice.getBasicPrice().toString());
			v.add(unitPrice.getBidPrice().toString());
			v.add(unitPrice.getFundCode());
			v.add(unitPrice.getOfferPrice().toString());
			v.add(unitPrice.getUserName());
			v.add(DateFormatter.getTimeStamp(
			 unitPrice.getValuationPoint().getTime()));
			
			rsUnitPrices.addRow(v);
		}
		
		return rsUnitPrices;
	}
	
	/** Takes an array of funds and returns an equivalent ResultSet.
	 *  
	 * @param accounts
	 * @return
	 */
	public static DataResultSet getFundResultSet
	 (Fund[] funds) {
		
		DataResultSet rsUnitPrices = new DataResultSet( new String[]
		 {"fundCode","typeCode","fundName","incomeTypeCode",
		  "numberOfDecimalPlaceRounding","openingDate","status"});
		
		for (int i=0; i<funds.length; i++) {
			Fund fund = funds[i];
			
			Vector v = new Vector();
			
			v.add(fund.getFundCode());
			v.add(fund.getTypeCode());
			v.add(fund.getFundName());
			v.add(fund.getIncomeTypeCode());
			v.add(fund.getNumberOfDecimalPlaceRounding());
			v.add(fund.getOpeningDate());
			v.add(fund.getStatus());
			
			rsUnitPrices.addRow(v);
		}
		
		return rsUnitPrices;
	}
	
	private static Workspace getCOIFWorkspace() throws ServiceException {
		Workspace coifWorkspace = null;
		
		Provider odcProvider = Providers.getProvider("AuroraCOIF");	
		if (odcProvider != null)
			coifWorkspace = (Workspace)odcProvider.getProvider();
		else {
			Log.error("Unable to retrieve AuroraCOIF provider!");
			throw new ServiceException("Unable to retrieve AuroraCOIF provider!");
		}
		
		if (coifWorkspace == null) {
			Log.error("Unable to retrieve AuroraCOIF workspace instance!");
			throw new ServiceException("Unable to retrieve AuroraCOIF workspace instance!");
		}
		
		return coifWorkspace;
	}
	
	/** Test method, calling public weather web service */
	public static void main(String [] args) {
		
		try {
			String endpoint =
			 "http://www.deeptraining.com/webservices/weather.asmx";

			org.apache.axis.client.Service  service = new 
			 org.apache.axis.client.Service();
			Call     call    = (Call) service.createCall();

			call.setTargetEndpointAddress( new java.net.URL(endpoint) );
			call.setOperationName(new QName("http://litwinconsulting.com/webservices/", "WeatherSoap"));
			call.setSOAPActionURI("http://litwinconsulting.com/webservices/GetWeather");
			
			String ret = (String) call.invoke( new Object[] { "Hello!" } );

			System.out.println("Sent 'Hello!', got '" + ret + "'");
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}
	
	/** Adds all fields from the given Address instance to the given
	 *  Vector. Null/empty values are still added.
	 *  
	 *  Used when building ResultSets containing Aurora address information.
	 *  
	 * @param address
	 * @return the Vector with the added fields
	 */
	private static Vector addAddressFieldsToVector
	 (Address address, Vector v) {
		
		v.add(address.getOrganisation());
		v.add(address.getBuilding());
		v.add(address.getStreet());
		v.add(address.getTown());
		v.add(address.getLocality());
		v.add(address.getCounty());
		v.add(address.getCountry());
		v.add(address.getPostCode());
		v.add(address.getTelephone());
		v.add(address.getEmail());
		v.add(address.getFax());
		
		return v;
	}
	
	public static Account getAccountByAccountNumberExternal
	 (Account[] accounts, String accountNumberExternal) {
		for (int i=0; i<accounts.length; i++) {
			if (accounts[i].getAccountNumberExternal().trim().equals(accountNumberExternal))
				return accounts[i];
		}
		
		return null;
	}
	
	public static Fund getFundByFundCode(Fund[] funds, String fundCode) {
		for (int i=0; i<funds.length; i++) {
			if (funds[i].getFundCode().equals(fundCode))
				return funds[i];
		}
		
		return null;
	}
	
	public static UnitPrice getUnitPriceByFundCode(UnitPrice[] unitPrices, String fundCode) {
		for (int i=0; i<unitPrices.length; i++) {
			if (unitPrices[i].getFundCode().equals(fundCode))
				return unitPrices[i];
		}
		
		return null;
	}

	/** Fetches client name via Aurora web service, given a company and client number.
	 *  
	 *  @param company
	 *  @param clientNumber
	 *  @return
	 *  @throws ServiceException
	 */
	/*
	private String getClientName(String company, String clientNumber) throws ServiceException {
		
		// SOAP request envelope string
		String getClientNameReq =
			"<soap:Envelope xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' " +
						   "xmlns:xsd='http://www.w3.org/2001/XMLSchema' " +
						   "xmlns:soap='http://schemas.xmlsoap.org/soap/envelope/'>" +
				"<soap:Body>" +
				"<GetClientName xmlns='http://aurora/AuroraWS'>" +
					"<company>${company}</company>" +
					"<clientNumber>${clientNumber}</clientNumber>" +
				"</GetClientName>" +
				"</soap:Body>" +
			"</soap:Envelope>";
		
		// Substitute vars
		getClientNameReq = StringUtils.replaceInString(getClientNameReq, "${company}", company);
		getClientNameReq = StringUtils.replaceInString(getClientNameReq, "${clientNumber}", clientNumber);
		
		// Obtain byte array for SOAP request envelope
		byte[] data = getClientNameReq.getBytes();
		
		HttpURLConnection uc = null;
		URL url = null;
		
		try {
			url = new URL(AURORA_WS_URL);
			
		} catch (MalformedURLException e1) {
			Log.error("URL EXCEPTION",e1);
			e1.printStackTrace();
		}
		
		try {
			uc = (HttpURLConnection) url.openConnection();
	
			uc.setDoInput(true);
			uc.setDoOutput(true);
			uc.setUseCaches(false);
			
			// set request properties
			uc.setRequestMethod("POST");
			uc.setRequestProperty("Host", "aurora");
			uc.setRequestProperty("Content-Type","text/xml; charset=utf-8");
			uc.setRequestProperty("Content-Length", " " + data.length);
			uc.setRequestProperty("SOAPAction", "\"http://aurora/AuroraWS/GetClientName\"");
			
			uc.connect();
	
			DataOutputStream dos = new DataOutputStream(uc.getOutputStream());
			dos.write(data, 0, data.length);
			dos.flush();
			dos.close();
			
			BufferedReader br = new BufferedReader(
			 new InputStreamReader(uc.getInputStream()));
			
			String res = null;
			
			Log.debug("Printing Aurora web service response:");
			
			StringBuffer strBuff = new StringBuffer();
			
			while ((res = br.readLine()) != null) {
				strBuff.append(res);
				Log.debug(res);
			}
			
			Log.debug("End web service response.");
			br.close();
			
			return strBuff.toString();
			
		} catch (IOException e) {
			Log.error("IO Error calling Aurora web service.",e);
			
		} finally {
			if (uc != null) {
				uc.disconnect();
				Log.debug("Disconnected.");
			}
		}
		
		return null;
	}
	*/
	
	/** Builds an EngineConfiguration which uses NTLM authentication.
	 * 
	 */
    protected static org.apache.axis.EngineConfiguration getEngineConfiguration() {
    	
    	Log.debug("Building custom Engine Configuration");
    	
        java.lang.StringBuffer sb = new java.lang.StringBuffer();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
        sb.append("<deployment name=\"defaultClientConfig\"\r\n");
        sb.append("xmlns=\"http://xml.apache.org/axis/wsdd/\"\r\n");
        sb.append("xmlns:java=\"http://xml.apache.org/axis/wsdd/providers/java\">\r\n");
        sb.append("<transport name=\"http\" pivot=\"java:org.apache.axis.transport.http.CommonsHTTPSender\" />\r\n");
        sb.append("<transport name=\"local\" pivot=\"java:org.apache.axis.transport.local.LocalSender\" />\r\n");
        sb.append("<transport name=\"java\" pivot=\"java:org.apache.axis.transport.java.JavaSender\" />\r\n");
        sb.append("</deployment>\r\n");
        
        org.apache.axis.configuration.XMLStringProvider config = 
            new org.apache.axis.configuration.XMLStringProvider(sb.toString());
        return config;
    }
}
