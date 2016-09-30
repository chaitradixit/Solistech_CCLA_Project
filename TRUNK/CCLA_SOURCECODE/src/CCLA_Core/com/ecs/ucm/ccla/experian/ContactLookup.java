package com.ecs.ucm.ccla.experian;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Globals;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.json.JsonUtils;

public class ContactLookup extends AddressLookup {

	
    public boolean doOrgCodeSearch = false;
    public boolean doPersonCodeSearch = false;
    public boolean doExtUniqueIDSearch = false;
    
    public boolean doExtIDSearch1 = false;
    public boolean doExtIDSearch2 = false;
    public boolean doExtIDSearch3 = false;
    public boolean doExtIDSearch4 = false;
    public boolean doExtIDSearch5 = false;
    
    public boolean doCampaignSearch = false;
    public boolean doClientNoSearch = false;
    public boolean doAccountNoSearch = false;
    public boolean doFundSearch = false;
    public boolean doCorrNoSearch = false;
    public boolean doPersonNameSearch = false;
    public boolean doOrgNameSearch = false;
    public boolean doAddressSearch = false;
    public boolean doPostcodeSearch = false;
    public boolean doQASTreeSearch = false;
    public boolean doEmailSearch = false;
    
    private String OrgCodeSearch = "";
    private String PersonCodeSearch = "";
    private String ExtUniqueIDSearch = "";
    private String ExtIDSearch1 = "";
    private String ExtIDSearch2 = "";
    private String ExtIDSearch3 = "";
    private String ExtIDSearch4 = "";
    private String ExtIDSearch5 = "";
    private String CampaignSearch = "";
    private String ClientNoSearch = "";
    private String AccountNoSearch = "";
    private String FundSearch = "";
    private String CorrNoSearch = "";
    private String PersonNameSearch = "";
    private String OrgNameSearch = ""; 
    private String HouseNameSearch = "";
    private String PostcodeSearch = "";
    private String EmailSearch = "";
    
    // list of person_ids to return
    private HashSet personHS = new HashSet();
    // list of client ids to return
    private HashSet clientHS = new HashSet();
    // list of account ids to return
    private HashSet accountHS = new HashSet();

    private String strComma = "";
    private String csList = "";
    
    // these determine whether or not search results page should show the results for each type
    private boolean showResultsPeople = false;
    private boolean showResultsOrgs = false;
    private boolean showResultsAccounts=false;
    private boolean showQASResults=false;   
    private String QASError = "";
    
    private String searchType = "";
    // these are used only if the searchType is set
    boolean doOrgSearch = false;
    boolean doPersonSearch = false;
    boolean doAccountSearch = false;
    boolean doAddressOnlySearch = false;
    
    // if passed as true this will attempt to return people/org, people/account and org/account
    // links
    boolean returnLinkedData = false;
 
    

    /**
     * Searches the dataset using the passed String. Expected to be
     * comma-delimited.
     * 
     * 
     * 
     * @throws ServiceException
     */
    public void findContact() throws ServiceException {
	getSearchParameters();
	try {
	    FWFacade fw = CCLAUtils.getFacade(this.m_workspace, true);
	    boolean doneQAS = false;
	    if (doAddressOnlySearch)
	    {
	    	// do QAS search
		    try {
				if (doPostcodeSearch || doAddressSearch)
				{
					showQASResults=true;
					executeQASSearch();
					doneQAS=true;
				}
			} catch (ServiceException se) {
				QASError = ContactLookupGlobals.CONTACT_LOOKUP_QAS_DOWN_ERROR_MSG;
				Log.error("CANNOT SEARCH ON QAS: " + se.getMessage());
			}
	    }

	    if (doPersonSearch)
	    {
	    	// do QAS search
		    try {
				if (doPostcodeSearch || doAddressSearch)
				{
					showQASResults=true;
					executeQASSearch();
					doneQAS=true;
				}
			} catch (ServiceException se) {
				QASError = ContactLookupGlobals.CONTACT_LOOKUP_QAS_DOWN_ERROR_MSG;
				Log.error("CANNOT SEARCH ON QAS: " + se.getMessage());
			}
		    // find people
		    findPeople(fw);	
		    // return people 
		    returnPersonResults(fw);				
	    }

	    if (doOrgSearch)
	    {
		    try {
				if (!doneQAS && (doPostcodeSearch || doAddressSearch))
				{
					showQASResults=true;
					executeQASSearch();
					doneQAS=true;
				}
			} catch (ServiceException se) {
				QASError = ContactLookupGlobals.CONTACT_LOOKUP_QAS_DOWN_ERROR_MSG;
				Log.error("CANNOT SEARCH ON QAS: " + se.getMessage());
			}
		
		    // find orgs
		    findOrgs(fw);	
		    // return org results
		    returnOrgResults(fw);
		}

	    if (doAccountSearch)
		{	
	    	findAccounts(fw);
		    // return account results
			returnAccountResults(fw);	    	
		}
   
	} catch (DataException e) {
	    Log.error(e.toString());
	    // log any errors
	    String ErrorMessage = "Cannot run search";
	    m_binder.putLocal("SearchErrorMessage", ErrorMessage);
	} 

    // log which results should be returned
    if (showResultsPeople)
    	CCLAUtils.addQueryBooleanParamToBinder(m_binder, "showResultsPeople", showResultsPeople);
    if (showResultsOrgs)
    	CCLAUtils.addQueryBooleanParamToBinder(m_binder, "showResultsOrgs", showResultsOrgs);
    if (showResultsAccounts)
    	CCLAUtils.addQueryBooleanParamToBinder(m_binder, "showResultsAccounts", showResultsAccounts);     
    if (showQASResults)
    	CCLAUtils.addQueryBooleanParamToBinder(m_binder, "showQASResults", showQASResults);    
    m_binder.putLocal("QASError", QASError);
	
    }

    private void findAccounts(FWFacade fw) throws DataException, ServiceException
    {
    	Log.debug("search using findAccount method");
    	boolean doSearch = false;
    	String SearchAccountQuery ="";  
    	String strAND = "";
    	String strWHERE = " WHERE ";    	
    	if (doOrgCodeSearch && doAccountNoSearch)
    	{
    		AccountNoSearch = CCLAUtils.padString(AccountNoSearch, '0', 8);
    		SearchAccountQuery = ContactLookupGlobals.CONTACT_ACCOUNT_NEW_QRY_START  + " INNER JOIN ("
    		+  ContactLookupGlobals.CONTACT_ORG_QRY_BASE + strWHERE + 
    		ContactLookupGlobals.CONTACT_UNIQUE_ID_ORG_QRY  + "'%" + OrgCodeSearch.toUpperCase() + "%' )" + 
    		ContactLookupGlobals.CONTACT_ACCOUNT_NEW_QRY_JOIN + strWHERE+
    		ContactLookupGlobals.CONTACT_ACCOUNT_CODE_QRY + "'" + AccountNoSearch + "'";
    		strAND = " AND ";
    		strWHERE = "";
    		doSearch=true;
    	}
    	if (doClientNoSearch && doAccountNoSearch)
    	{
    		// old style client number and accnumext
    		SearchAccountQuery = ContactLookupGlobals.CONTACT_ACCOUNT_CLIENT_OLD_QRY_START 
    		+ " INNER JOIN ( " + ContactLookupGlobals.CONTACT_ACCOUNT_CLIENT_OLD_QRY_JOIN
    		+ ClientNoSearch + ") " + ContactLookupGlobals.CONTACT_ACCOUNT_CLIENT_OLD_QRY_JOIN_ON
    		+ " WHERE " + ContactLookupGlobals.CONTACT_ACCOUNT_CLIENT_OLD_QRY_WHERE + "'" 
    		+ AccountNoSearch.toUpperCase()	+ "'";
    		strAND = " AND ";
    		strWHERE = "";
    		doSearch=true;
    	}
    	if ((doClientNoSearch || doOrgCodeSearch) && doFundSearch)
    	{
    		if (doAccountNoSearch)
    		{
    		Log.debug("in doAccountNoSearch");
    		// in this case just append the fund code on the end
    		SearchAccountQuery = SearchAccountQuery + strAND + strWHERE + ContactLookupGlobals.CONTACT_ACCOUNT_FUND_QUERY
    		+ " '" + FundSearch.toUpperCase() + "' ";
    		doSearch=true;
    		} 
    		else
    		{
    			Log.debug("NOT in doAccountNoSearch");
    			// create query from the start
    			if (doOrgCodeSearch)
    			{
	        		SearchAccountQuery = ContactLookupGlobals.CONTACT_ACCOUNT_NEW_QRY_START  + " INNER JOIN ("
	        		+  ContactLookupGlobals.CONTACT_ORG_QRY_BASE + " WHERE " + 
	        		ContactLookupGlobals.CONTACT_UNIQUE_ID_ORG_QRY  + "'%" + OrgCodeSearch.toUpperCase() + "%' )" 
	        		+ ContactLookupGlobals.CONTACT_ACCOUNT_NEW_QRY_JOIN + 
	        		strAND + strWHERE + ContactLookupGlobals.CONTACT_ACCOUNT_FUND_QUERY + " '" + FundSearch.toUpperCase() + "'";
	        		doSearch=true;
	    		} else if (doClientNoSearch)
    			{
	        		// old style client number and accnumext
	        		SearchAccountQuery = ContactLookupGlobals.CONTACT_ACCOUNT_CLIENT_OLD_QRY_START 
	        		+ " INNER JOIN ( " + ContactLookupGlobals.CONTACT_ACCOUNT_CLIENT_OLD_QRY_JOIN
	        		+ ClientNoSearch + ") " + ContactLookupGlobals.CONTACT_ACCOUNT_CLIENT_OLD_QRY_JOIN_ON +
	           		strAND + strWHERE 
	           		+ ContactLookupGlobals.CONTACT_ACCOUNT_FUND_QUERY + " '" + FundSearch.toUpperCase() + "'";   	     
	        		strAND = " AND ";
	        		strWHERE = "";
	        		doSearch=true;	
    			}
    		}
    	}    	
    	if (doSearch && !StringUtils.stringIsBlank(SearchAccountQuery))
    	{
    		SearchAccountQuery = SearchAccountQuery + ContactLookupGlobals.CONTACT_LOOKUP_ROW_LIMIT;
    		Log.debug("SearchAccountQuery:" + SearchAccountQuery);
    		getAccountResultSet(fw, SearchAccountQuery);
    		showResultsAccounts = true;
    	}
    }
    private void findPeople(FWFacade fw) throws DataException, ServiceException 
    {
    	Log.debug("search using findPeople method");
    	
    	String strAND = "";
    	boolean doSearch = false;
    	String SearchPersonQuery ="";  
    	String strWHERE = "";
    	if (doCorrNoSearch)
    	{
    		// In this case have to do exact match on corr ID therefore do not do any other searches
    		SearchPersonQuery = ContactLookupGlobals.CONTACT_CORRRESPONDENT_QRY + CorrNoSearch;
    		doSearch = true;
    		showResultsPeople = true;
    	} 
    	else 
    	{

	    	if (doPersonCodeSearch)
	    	{
	    		SearchPersonQuery =  ContactLookupGlobals.CONTACT_UNIQUE_ID_PERSON_QRY
	    		+ "'%" + PersonCodeSearch.toUpperCase() +"%'";
	    		strAND = " AND ";
	    		strWHERE = " WHERE ";
	    		doSearch = true;
	    	}
	    	
	    	if (doPersonNameSearch)
	    	{
	    		SearchPersonQuery = SearchPersonQuery +
	    		strAND + ContactLookupGlobals.CONTACT_NAME_PERSON_QRY + "'%" + PersonNameSearch.toUpperCase() + "%'";
	    		strAND = " AND ";
	    		strWHERE = " WHERE ";
	    		doSearch = true;
	    	}
	    	
	    	if (doPostcodeSearch && !doAddressSearch)
	    	{
	    		SearchPersonQuery = SearchPersonQuery + strAND + 
				ContactLookupGlobals.CONTACT_PERSON_ADDRESS_QRY_IN + "(" + 
				ContactLookupGlobals.CONTACT_PERSON_ADDRESS_QRY_BASE + 
				ContactLookupGlobals.CONTACT_ADDRESS_POSTCODE_QRY + "'" + PostcodeSearch.toUpperCase() + "%')";
				//ContactLookupGlobals.CONTACT_PERSON_ADDRESS_QRY_JOIN + ")";
		    	
				strAND = " AND ";
		    	strWHERE = " WHERE ";
		    	doSearch = true;
	    	}    
	    	
	    	if (doAddressSearch)
	    	{
		    	SearchPersonQuery = SearchPersonQuery + strAND + 
				ContactLookupGlobals.CONTACT_PERSON_ADDRESS_QRY_IN + "(" + 
				ContactLookupGlobals.CONTACT_PERSON_ADDRESS_QRY_BASE + 
				ContactLookupGlobals.CONTACT_ADDRESS_POSTCODE_QRY + "'" + PostcodeSearch.toUpperCase() + "%'" +
				" AND (" + ContactLookupGlobals.CONTACT_ADDRESS_FLAT_QRY + "'%" + HouseNameSearch.toUpperCase() + "%'" +
				" OR " + ContactLookupGlobals.CONTACT_ADDRESS_HOUSENAME_QRY + "'%" + HouseNameSearch.toUpperCase() + "%'" +
				" OR " + ContactLookupGlobals.CONTACT_ADDRESS_HOUSENUMBER_QRY + "'%" + HouseNameSearch.toUpperCase() + "%'" +
				" ))";
				//ContactLookupGlobals.CONTACT_PERSON_ADDRESS_QRY_JOIN + ")";
		    	strAND = " AND ";
		    	strWHERE = " WHERE ";
		    	doSearch = true;
	    	}  
	    	
	    	if (doEmailSearch) 
	    	{
		    	SearchPersonQuery = SearchPersonQuery + strAND + 
				ContactLookupGlobals.CONTACT_PERSON_EMAIL_QRY_IN + "(" + 
				ContactLookupGlobals.CONTACT_PERSON_EMAIL_QRY_BASE + 
				ContactLookupGlobals.CONTACT_PERSON_EMAIL_QRY + "'" + EmailSearch + "%')";
		    	strAND = " AND ";
		    	strWHERE = " WHERE ";
		    	doSearch = true;	    		
	    	}
    	}
    	if (doSearch && !StringUtils.stringIsBlank(SearchPersonQuery))
    	{
    		SearchPersonQuery = ContactLookupGlobals.CONTACT_PERSON_QRY_BASE + strWHERE 
    	    + SearchPersonQuery + ContactLookupGlobals.CONTACT_LOOKUP_ROW_LIMIT;
    		Log.debug("CCLAUniqueIDSearchPersonQuery:" + SearchPersonQuery);
    		getPersonResultSet(fw, SearchPersonQuery);
    		showResultsPeople = true;
    	}

    }
 
    private void findOrgs(FWFacade fw) throws DataException 
    {
    	Log.debug("search using findOrg method");
    	String strAND = "";
    	boolean doSearch = false;
    	String SearchOrgQuery ="";  
    	String strWHERE = "";
    	if (doExtIDSearch1 || doExtIDSearch2 || doExtIDSearch3 || doExtIDSearch4 || doExtIDSearch5)
    	{
    		SearchOrgQuery 
		    =  ContactLookupGlobals.CONTACT_ORG_IDENTIFIER_QRY ;
    		String strExtAND = "";
    		String strExtWhere = " WHERE ";
    		
    		if (doExtIDSearch1)
    		{
	    		SearchOrgQuery 
			    =  SearchOrgQuery + strExtAND 
			    +  strExtWhere + ContactLookupGlobals.CONTACT_ORG_IDENTIFIER_VALUE_WHERE_CLAUSE 
			    + "'" + ExtIDSearch1.toUpperCase() +"'"  + " AND " 
			    + ContactLookupGlobals.CONTACT_ORG_IDENTIFIER_WHERE_CLAUSE + "1";
	    		strExtAND = " AND ";
	    		strExtWhere = "" ;
    		}
    		if (doExtIDSearch2)
    		{
	    		SearchOrgQuery 
			    =  SearchOrgQuery + strExtAND 
			    	+ strExtWhere + ContactLookupGlobals.CONTACT_ORG_IDENTIFIER_VALUE_WHERE_CLAUSE 
			    + "'" + ExtIDSearch2.toUpperCase() +"'"  + " AND " 
			    + ContactLookupGlobals.CONTACT_ORG_IDENTIFIER_WHERE_CLAUSE + "2";
	    		strExtAND = " AND ";
	    		strExtWhere = "" ;
    		}  	
    		if (doExtIDSearch3)
    		{
	    		SearchOrgQuery 
			    =  SearchOrgQuery + strExtAND 
			    	+ strExtWhere + ContactLookupGlobals.CONTACT_ORG_IDENTIFIER_VALUE_WHERE_CLAUSE 
			    + "'" + ExtIDSearch3.toUpperCase() +"'"  + " AND " 
			    + ContactLookupGlobals.CONTACT_ORG_IDENTIFIER_WHERE_CLAUSE + "3";
	    		strExtAND = " AND ";
	    		strExtWhere = "" ;
    		}  
    		if (doExtIDSearch4)
    		{
	    		SearchOrgQuery 
			    =  SearchOrgQuery + strExtAND
			    	+ strExtWhere + ContactLookupGlobals.CONTACT_ORG_IDENTIFIER_VALUE_WHERE_CLAUSE 
			    + "'" + ExtIDSearch4.toUpperCase() +"'"  + " AND " 
			    + ContactLookupGlobals.CONTACT_ORG_IDENTIFIER_WHERE_CLAUSE + "4";
	    		strExtAND = " AND ";
	    		strExtWhere = "" ;
    		}      	
    		if (doExtIDSearch5)
    		{
	    		SearchOrgQuery 
			    =  SearchOrgQuery + strExtAND
			    	+ strExtWhere + ContactLookupGlobals.CONTACT_ORG_IDENTIFIER_VALUE_WHERE_CLAUSE 
			    + "'" + ExtIDSearch5.toUpperCase() +"'"  + " AND " 
			    + ContactLookupGlobals.CONTACT_ORG_IDENTIFIER_WHERE_CLAUSE + "5";
	    		strExtAND = " AND ";
	    		strExtWhere = "" ;
    		}    
    		
       		SearchOrgQuery = SearchOrgQuery + ContactLookupGlobals.CONTACT_LOOKUP_ROW_LIMIT;
       		Log.debug("CCLAUniqueIDSearchOrgQuery:" + SearchOrgQuery);  	
       		getOrgResultSet(fw, SearchOrgQuery); 
    		showResultsOrgs = true;
    	} 
    	else if (doClientNoSearch)
    	{
    		SearchOrgQuery 
		    =  ContactLookupGlobals.CONTACT_CLIENT_NUMBER_QRY
		    + ClientNoSearch + ContactLookupGlobals.CONTACT_LOOKUP_ROW_LIMIT ;
    		Log.debug("CCLAUniqueIDSearchOrgQuery:" + SearchOrgQuery);
    		getOrgResultSet(fw, SearchOrgQuery); 
    		showResultsOrgs = true;
    	} 
    	else 
    	{
	    	if (doOrgCodeSearch)
	    	{
	    		SearchOrgQuery 
			    =  ContactLookupGlobals.CONTACT_UNIQUE_ID_ORG_QRY
			    + "'%" + OrgCodeSearch.toUpperCase() +"%'";
			    strAND = " AND ";
			    strWHERE = " WHERE ";
			    doSearch = true;
	    	}
	    	if (doPostcodeSearch && !doAddressSearch)
	    	{
		    	SearchOrgQuery = SearchOrgQuery + strAND + 
				ContactLookupGlobals.CONTACT_ORG_ADDRESS_QRY_IN + "(" + 
				ContactLookupGlobals.CONTACT_ORG_ADDRESS_QRY_BASE + 
				ContactLookupGlobals.CONTACT_ADDRESS_POSTCODE_QRY + "'" + PostcodeSearch.toUpperCase() + "%')";
				//ContactLookupGlobals.CONTACT_ORG_ADDRESS_QRY_JOIN + ")";
		    	strAND = " AND ";
		    	strWHERE = " WHERE ";
		    	doSearch = true;
	    	}    
	    	if (doAddressSearch)
	    	{
		    	SearchOrgQuery = SearchOrgQuery + strAND + 
				ContactLookupGlobals.CONTACT_ORG_ADDRESS_QRY_IN + "(" + 
				ContactLookupGlobals.CONTACT_ORG_ADDRESS_QRY_BASE + 
				ContactLookupGlobals.CONTACT_ADDRESS_POSTCODE_QRY + "'" + PostcodeSearch.toUpperCase() + "%'" +
				" AND (" + ContactLookupGlobals.CONTACT_ADDRESS_FLAT_QRY + "'%" + HouseNameSearch.toUpperCase() + "%'" +
				" OR " + ContactLookupGlobals.CONTACT_ADDRESS_HOUSENAME_QRY + "'%" + HouseNameSearch.toUpperCase() + "%'" +
				" OR " + ContactLookupGlobals.CONTACT_ADDRESS_HOUSENUMBER_QRY + "'%" + HouseNameSearch.toUpperCase() + "%'" +
				" )) ";
				//ContactLookupGlobals.CONTACT_ORG_ADDRESS_QRY_JOIN + ")";
		    	strAND = " AND ";
		    	strWHERE = " WHERE ";
		    	doSearch = true;
	    	}  	    	
    	}
    	if (doOrgNameSearch)
    	{
    		if (doSearch)
    		{
    			SearchOrgQuery = SearchOrgQuery + " AND " + ContactLookupGlobals.CONTACT_NAME_ORG_QRY +
    			"'%" + OrgNameSearch.toUpperCase() + "%'";
    		} else 
    		{
    			SearchOrgQuery = " WHERE " + ContactLookupGlobals.CONTACT_NAME_ORG_QRY +
    			"'%" + OrgNameSearch.toUpperCase() + "%'";	
    			doSearch=true;
    		}
    	
    	}
    	if (doSearch && !StringUtils.stringIsBlank(SearchOrgQuery))
    	{
    		SearchOrgQuery = ContactLookupGlobals.CONTACT_ORG_QRY_BASE + strWHERE 
    	    + SearchOrgQuery + ContactLookupGlobals.CONTACT_LOOKUP_ROW_LIMIT;
    		Log.debug("CCLAUniqueIDSearchOrgQuery:" + SearchOrgQuery);
    		getOrgResultSet(fw, SearchOrgQuery);
    		showResultsOrgs = true;
    	}
    } 
 
    /**
     * runs the query passed to it and then puts the results (always just an
     * account id) into the accountHS hashset
     * 
     * @return void
     * 
     * @throws DataException
     */
    private void getAccountResultSet(FWFacade fw, String queryName) throws DataException {

	DataResultSet rs = fw.createResultSetSQL(queryName);
	
	if (rs.isEmpty()) {
	    Log.debug("in getOrgResultSet record not found for account");
	} else {
	    for (int i = 0; i < rs.getNumRows(); i++) {
		rs.setCurrentRow(i);
		String accountID = rs.getStringValue(0);
		accountHS.add(accountID);
	    }
	    Log.debug("record has been found for at least one account");
	}
    } 
    
    /**
     * runs the query passed to it and then puts the results (always just a
     * person id) into the personHS hashset
     * 
     * @return void
     * 
     * @throws DataException
     */
    private void getOrgResultSet(FWFacade fw, String queryName) throws DataException {

	//DataResultSet rs = fw.createResultSet(queryName, m_binder);
	DataResultSet rs = fw.createResultSetSQL(queryName);
	
	if (rs.isEmpty()) {
	    Log.debug("in getOrgResultSet record not found for organisation");
	} else {
	    for (int i = 0; i < rs.getNumRows(); i++) {
		rs.setCurrentRow(i);
		String clientID = rs.getStringValue(0);
		clientHS.add(clientID);
	    }
	    Log.debug("record has been found for at least one organisation");
	}
    } 
    
    private void returnAccountResults(FWFacade fw) throws DataException
    {
    	if (!accountHS.isEmpty()) {
    	    Iterator accountI = accountHS.iterator();
    	    while (accountI.hasNext()) {
    		csList = csList + strComma + accountI.next();
    		strComma = ",";
    	    }
    	    m_binder.putLocal("ACCOUNT_ID_LIST", csList);
    	    DataResultSet existingRS = fw.createResultSet("qClientServices_GetLookupAccount_Detail", m_binder);

    	    if (existingRS.isEmpty()) {
    		Log.debug("in contact record not found for account");
    	    } else {
    		m_binder.addResultSet("rsAccounts", existingRS);
    	    }
    	}
    }
     
    
    private void returnPersonResults(FWFacade fw) throws DataException
    {
    	if (!personHS.isEmpty()) {
    	    Iterator contactI = personHS.iterator();
    	    while (contactI.hasNext()) {
    		csList = csList + strComma + contactI.next();
    		strComma = ",";
    	    }
    	    m_binder.putLocal("PERSON_ID_LIST", csList);
    	    DataResultSet existingRS = fw.createResultSet("qClientServices_GetLookupPerson_Detail", m_binder);

    	    if (existingRS.isEmpty()) {
    		Log.debug("in contact record not found for person");
    	    } else {
    		m_binder.addResultSet("rsExisting", existingRS);
    	    }
    	}
    }
 
    private void returnOrgResults(FWFacade fw) throws DataException
    {
    	if (!clientHS.isEmpty()) {
    	    Iterator clientI = clientHS.iterator();
    	    strComma = "";
    	    csList = "";
    	    while (clientI.hasNext()) {
    		csList = csList + strComma + clientI.next();
    		strComma = ",";
    	    }
    	    m_binder.putLocal("CLIENT_ID_LIST", csList);
    	    //fw = CCLAUtils.getFacade(this.m_workspace);
    	    DataResultSet existingClientRS = fw.createResultSet("qClientServices_GetLookupEntity_Detail", m_binder);
    	    if (existingClientRS.isEmpty()) {
    		Log.debug("in contact record not found for org");
    	    } else {
    		m_binder.addResultSet("rsExistingClient", existingClientRS);
    	    }
    	}
    }
    	  
    
    /**
     * runs the query passed to it and then puts the results (always just a
     * person id) into the personHS hashset
     * 
     * @return void
     * 
     * @throws DataException
     */
    private void getPersonResultSet(FWFacade fw, String queryName) throws DataException {

	//DataResultSet rs = fw.createResultSet(queryName, m_binder);
	DataResultSet rs = fw.createResultSetSQL(queryName);
	
	if (rs.isEmpty()) {
	    Log.debug("in personresults record not found for person");
	} else {
	    for (int i = 0; i < rs.getNumRows(); i++) {
		rs.setCurrentRow(i);
		String personID = rs.getStringValue(0);
		personHS.add(personID);
	    }
	    Log.debug("record has been found for at least one person");
	}
    }
 
    public void getSearchParameters() {
    	  
    //******** search type **********//
    //********* if these are passed then only search on that type **********//
    searchType = m_binder.getLocal("searchScope");
    if (!StringUtils.stringIsBlank(searchType))
    {
    	if (searchType.equalsIgnoreCase("Person"))
    		doPersonSearch = true;
    	if (searchType.equalsIgnoreCase("Organisation"))
    		doOrgSearch = true;
    	if (searchType.equalsIgnoreCase("Account"))
    		doAccountSearch = true;  
    	if (searchType.equalsIgnoreCase("Address"))
    		doAddressOnlySearch = true;
    	// if searchScope of all is returned then just ignore the scope (should not be used anymore
    	// but could be there because of legacy code)
    	if (searchType.equalsIgnoreCase("all"))
    		searchType = "";
    }
    Log.debug("searchType is " + searchType);

    // ******* UNIQUE IDENTIFIERS **********	
    OrgCodeSearch = m_binder.getLocal("ORGANISATION_CODE");
    if (!StringUtils.stringIsBlank(OrgCodeSearch)) {
    	OrgCodeSearch = OrgCodeSearch.trim();
    	doOrgCodeSearch = true;
    }
    PersonCodeSearch = m_binder.getLocal("PERSON_CODE");
    if (!StringUtils.stringIsBlank(PersonCodeSearch)) {
    	PersonCodeSearch = PersonCodeSearch.trim();
    	doPersonCodeSearch = true;
    }
    CampaignSearch = m_binder.getLocal("CAMPAIGN_NO");
    if (!StringUtils.stringIsBlank(CampaignSearch)) {
    	CampaignSearch = CampaignSearch.trim();
    	doCampaignSearch = true;
    }
    
    //*********** ORG EXTERNAL IDENTIFIERS ***********//
    ExtIDSearch1= m_binder.getLocal("EXT_IDENTIFIER_1");
    if (!StringUtils.stringIsBlank(ExtIDSearch1))
    	doExtIDSearch1 = true; 
    ExtIDSearch2= m_binder.getLocal("EXT_IDENTIFIER_2");
    if (!StringUtils.stringIsBlank(ExtIDSearch2))
    	doExtIDSearch2 = true; 
    ExtIDSearch3= m_binder.getLocal("EXT_IDENTIFIER_3");
    if (!StringUtils.stringIsBlank(ExtIDSearch3))
    	doExtIDSearch3 = true; 
    ExtIDSearch4= m_binder.getLocal("EXT_IDENTIFIER_4");
    if (!StringUtils.stringIsBlank(ExtIDSearch4))
    	doExtIDSearch4 = true; 
    ExtIDSearch5= m_binder.getLocal("EXT_IDENTIFIER_5");
    if (!StringUtils.stringIsBlank(ExtIDSearch5))
    	doExtIDSearch5 = true;     
    
    // ******* EXISTING ORGANISATIONS **********	
    ClientNoSearch = m_binder.getLocal("CLIENT_NO");
    if (!StringUtils.stringIsBlank(ClientNoSearch))
    	doClientNoSearch = true;
    AccountNoSearch = m_binder.getLocal("ACCOUNT_NO");
    if (!StringUtils.stringIsBlank(AccountNoSearch))
    	doAccountNoSearch = true;
    FundSearch = m_binder.getLocal("FUND");
    if (!StringUtils.stringIsBlank(FundSearch)) {
    	FundSearch = FundSearch.trim();
    	doFundSearch = true;
    }
    CorrNoSearch = m_binder.getLocal("CORR_ID");
    if (!StringUtils.stringIsBlank(CorrNoSearch))
    	doCorrNoSearch = true;
    
    // ******* NAME/ADDRESS SEARCHES **********	
    PersonNameSearch = m_binder.getLocal("PERSON_NAME");
    if (!StringUtils.stringIsBlank(PersonNameSearch))
    	doPersonNameSearch = true;
    OrgNameSearch = m_binder.getLocal("ORG_NAME");
    if (!StringUtils.stringIsBlank(OrgNameSearch))
    	doOrgNameSearch = true;
    HouseNameSearch = m_binder.getLocal("HOUSE_NAMENUMBER");
    PostcodeSearch = m_binder.getLocal("POSTCODE");
    if (!StringUtils.stringIsBlank(HouseNameSearch) && !StringUtils.stringIsBlank(PostcodeSearch))
    	doAddressSearch = true;
    if (!StringUtils.stringIsBlank(PostcodeSearch) && !doAddressSearch)
    	doPostcodeSearch = true;
    
    if (!StringUtils.stringIsBlank(m_binder.getLocal("QASTreeSearch")))
    	doQASTreeSearch = true;
    
    EmailSearch = m_binder.getLocal("EMAIL");
    if (!StringUtils.stringIsBlank(EmailSearch))
    	doEmailSearch = true;
    
    // Figure out what kind of search to do - some of this logic should already be on the page
    // in the disabled/enabled search fields so this is a second check
    // person search
    if (doPersonSearch || (doPersonCodeSearch || doPersonNameSearch || doCorrNoSearch || ((doAddressSearch || doPostcodeSearch || doEmailSearch) && !doOrgNameSearch  && !doOrgSearch)))
    {
    	doOrgSearch = false;
    	doPersonSearch = true;
    	doAccountSearch = false;
    }
    // org search
    if (!doPersonSearch  && !doPersonNameSearch && !doEmailSearch && (doOrgSearch || ((doOrgCodeSearch || doExtIDSearch1 || doExtIDSearch2 || doExtIDSearch3 
    		|| doExtIDSearch4 || doExtIDSearch5  || doOrgNameSearch || 
    		doAddressSearch || doPostcodeSearch)
    		|| (doClientNoSearch && !(doAccountNoSearch || doFundSearch || doAccountSearch)))))
    {
    	doOrgSearch = true;
    	doPersonSearch = false;
    	doAccountSearch = false;
    }
    // account search
    if (!doPersonSearch && !doOrgSearch && !doEmailSearch && (doAccountSearch || (doAccountNoSearch || doFundSearch)))
    {
    	doOrgSearch = false;
    	doPersonSearch = false;
    	doAccountSearch = true;	
    }
    // first exception is if it set to explicitly only search on address
    if (doAddressOnlySearch)
    {
    	doOrgSearch = false;
    	doPersonSearch = false;
    	doAccountSearch = false;
    }
    // second exception is if only address fields are passed then search on person and org
    // if searchscope is not explicitly set
    if (!searchType.equalsIgnoreCase("Person") && !searchType.equalsIgnoreCase("Organisation"))
    {
	    if (!doAddressOnlySearch && (doPostcodeSearch || doAddressSearch) && !(doPersonNameSearch || doOrgNameSearch))
	    {
	    	doOrgSearch = true;
	    	doPersonSearch = true;
	    	doAccountSearch = false;
	    }
    }
    
    Log.debug("doPersonSearch is " + doPersonSearch);
    Log.debug("doOrgSearch is " + doOrgSearch);
    Log.debug("doAccountSearch is " + doAccountSearch);
    Log.debug("doAddressOnlySearch is " + doAddressOnlySearch);
    }

    /**
     * Fetches both Entity and Person results for the given search parameters
     * and scope.
     * 
     * Any matching records will be fetched as ResultSets. These ResultSets are
     * added to a separate DataBinder instance. At the very end of the method,
     * this binder is serialized to a JSON formatted string. This string is then
     * added to the local DataBinder.
     * 
     * This has to be done, rather than simply calling the service with IsJson=1
     * because some of the core ResultSet data (e.g. the UserAttribInfo
     * ResultSet) will occassionally 'break' the JavaScript JSON parsing on the
     * client end.
     * 
     */
    public void doJSONContactLookup() throws ServiceException, DataException {
	String searchScope = m_binder.getLocal("searchScope");
	Log.debug("searchscope is " + searchScope);

	FWFacade fw = CCLAUtils.getFacade(this.m_workspace, true);
	DataBinder jsonBinder = new DataBinder();

	if (searchScope.equalsIgnoreCase("all") || searchScope.equalsIgnoreCase("person")) {
	    String searchNameString = m_binder.getLocal("SEARCH_NAME");

	    DataResultSet rsExistingPerson = getPersonResultsStripped(fw, searchNameString);

	    Log.debug("running query qClientServices_GetLookupPerson_Name with value LAST_NAME:"
		    + m_binder.getLocal("LAST_NAME") + "and FULL_NAME: " + m_binder.getLocal("FULL_NAME"));

	    // Add returned person records to new DataBinder. Serialize this
	    // into
	    // JSON format and add to the local DataBinder as a single String
	    // value.

	    if (rsExistingPerson != null)
		jsonBinder.addResultSet("rsExistingPerson", rsExistingPerson);
	}

	if (searchScope.equalsIgnoreCase("all") || searchScope.equalsIgnoreCase("client") || searchScope.equalsIgnoreCase("entity")) {
	    String searchNameString = m_binder.getLocal("SEARCH_NAME");
	    String wildcardsearchNameString = "%" + searchNameString.toUpperCase() + "%";

	    Log.debug("name search is " + wildcardsearchNameString);
	    m_binder.putLocal("CLIENT_NAME", wildcardsearchNameString);

	    Log.debug("calling qClientServices_GetLookupEntity_Name with CLIENT_NAME:" + m_binder.getLocal("CLIENT_NAME"));

	    DataResultSet rsClients = getEntityResultsStripped(fw, "qClientServices_GetLookupEntity_Name");

	    if (rsClients != null)
		jsonBinder.addResultSet("rsExistingClient", rsClients);
	}

	// Serialize the binder containing the Contact Lookup ResultSets into
	// JSON format and add to the local DataBinder as a single String value.
	String jsonData = JsonUtils.binderToJsonString(jsonBinder);
	m_binder.putLocal("jsonData", jsonData);
    }

    public void doTestJSONContactLookup() throws IOException {
	String jsonData = getJSONTestData();
	m_binder.putLocal("jsonData", jsonData);
    }

    public static String getJSONTestData() throws IOException {

	String path = "C:\\ORACLE\\ucm\\server\\test.json";

	StringBuffer fileData = new StringBuffer(1000);

	BufferedReader reader = new BufferedReader(new FileReader(path));

	char[] buf = new char[1024];
	int numRead = 0;
	while ((numRead = reader.read(buf)) != -1) {
	    fileData.append(buf, 0, numRead);
	}
	reader.close();
	return fileData.toString();
    }

    /**
     * May return null. Fix.
     * 
     * @param fw
     * @param queryString
     * @return
     * @throws DataException
     */
    private DataResultSet getEntityResultsStripped(FWFacade fw, String queryName) throws DataException {

	CCLAUtils.addQueryIntParamToBinder(m_binder, "numRows", Globals.AUTOSEARCH_ENTITY_NUMROWS);

	DataResultSet rs = fw.createResultSet(queryName, m_binder);

	if (rs.isEmpty()) {
	    Log.debug("in getClientResultsStripped record not found for client");
	    return null;
	}

	Log.debug("record has been found for person:");
	for (int i = 0; i < rs.getNumRows(); i++) {
	    rs.setCurrentRow(i);
	    clientHS.add(rs.getStringValue(0));
	}
	Iterator clientI = clientHS.iterator();
	strComma = "";
	csList = "";
	while (clientI.hasNext()) {
	    csList = csList + strComma + clientI.next();
	    strComma = ",";
	}

	Log.debug("csList is " + csList);
	m_binder.putLocal("CLIENT_ID_LIST", csList);

	DataResultSet rsExistingClients = fw.createResultSet("qClientServices_GetLookupEntity_Detail", m_binder);

	Vector<String> myV = new Vector<String>();

	if (rsExistingClients.isEmpty()) {
	    Log.debug("in rsExistingClients record not found for client");

	} else {
	    int numFields = rsExistingClients.getNumFields();
	    for (int j = 0; j < numFields; j++) {
		String fieldName = rsExistingClients.getFieldName(j);
		myV.add(fieldName);
	    }
	    String newArray[] = new String[myV.size()];
	    myV.toArray(newArray);

	    Vector<String> checkFields = new Vector<String>();
	    checkFields.add("NAME");

	    CCLAUtils.removeResultSetValueChars(rsExistingClients, Globals.BAD_JSON_CHARS, checkFields);

	    m_binder.addResultSet("rsExistingClient", rsExistingClients);
	}

	return rsExistingClients;
    }

    /**
     * May return null. Fix.
     * 
     * @param fw
     * @param queryString
     * @return
     * @throws DataException
     */
    private DataResultSet getPersonResultsStripped(FWFacade fw, String queryString) throws DataException 
    {
		String queryName = "qClientServices_GetLookupPerson_Name";
		boolean doPostcodeSearch = false;
		boolean doNameSearch = true;
		String secondName = "";
		String postcode = "";
		// the string coming from person can be made up of a comma separated
		// list of
		// second name and postcode, eg Dent, GU1 2DU
		// or just the second name
	
		// does the searchstring have a comma in it?
		int commaPos = queryString.indexOf(",");
		Log.debug("commaPos is " + commaPos);
		if (commaPos >= 0) {
		    doNameSearch = false;
		    if (commaPos == 0) {
			// in this case only postcode is presumed to be present
			postcode = queryString.substring(1).trim();
			doPostcodeSearch = true;
			doNameSearch = false;
			Log.debug("postcode is " + postcode);
		    }
		    if (commaPos >= 0) {
			secondName = queryString.substring(0, commaPos).trim();
			Log.debug("secondname is " + secondName);
			postcode = queryString.substring(commaPos + 1).trim();
			Log.debug("postcode is " + postcode);
			if (secondName.length() > 2)
			    doNameSearch = true;
			if (postcode.length() > 2)
			    doPostcodeSearch = true;
		    }
		    // need to get name and postcode separately
		    // and change queryName
		} else {
		    secondName = queryString;
		}
		String wildcardsearchPostcodeString = "%" + postcode.toUpperCase() + "%";
		String wildcardsearchNameString = "%" + secondName.toUpperCase() + "%";
		Log.debug("name search is " + wildcardsearchNameString);
		m_binder.putLocal("LAST_NAME", wildcardsearchNameString);
		m_binder.putLocal("FULL_NAME", wildcardsearchNameString);
		if (doPostcodeSearch)
		    m_binder.putLocal("POSTCODE", wildcardsearchPostcodeString);
		if (doNameSearch && doPostcodeSearch) {
		    // set queryname
		    queryName = "qClientServices_GetLookupPerson_PostcodeName";
		} else if (doNameSearch && !doPostcodeSearch) {
		    queryName = "qClientServices_GetLookupPerson_Name";
		} else if (!doNameSearch && doPostcodeSearch) {
		    queryName = "qClientServices_GetLookupPerson_Postcode";
		} else {
		    Log.error("Cannot find search terms in getPersonResultsStripped");
		}
	
		CCLAUtils.addQueryIntParamToBinder(m_binder, "numRows", Globals.AUTOSEARCH_PERSON_NUMROWS);
	
		DataResultSet rs = fw.createResultSet(queryName, m_binder);
		if (rs.isEmpty()) {
		    Log.debug("in getClientResultsStripped record not found for person");
		    return null;
		}
	
		Log.debug("record has been found for person:");
		for (int i = 0; i < rs.getNumRows(); i++) {
		    rs.setCurrentRow(i);
		    personHS.add(rs.getStringValue(0));
		}
	
		Iterator personI = personHS.iterator();
		strComma = "";
		csList = "";
		while (personI.hasNext()) {
		    csList = csList + strComma + personI.next();
		    strComma = ",";
		}
		Log.debug("csList is " + csList);
		m_binder.putLocal("PERSON_ID_LIST", csList);
	
		DataResultSet rsExistingPerson = fw.createResultSet("qClientServices_GetLookupPerson_Detail", m_binder);
	
		Vector myV = new Vector();
		if (rsExistingPerson.isEmpty()) {
		    Log.debug("in existingClientRS record not found for person");
		} else {
		    int numFields = rsExistingPerson.getNumFields();
		    
		    for (int j = 0; j < numFields; j++) {
		    	String fieldName = rsExistingPerson.getFieldName(j);
		    	myV.add(fieldName);
		    }
		    
		    String newArray[] = new String[myV.size()];
		    myV.toArray(newArray);
		    DataResultSet myRS = new DataResultSet(newArray);
	
		    Vector<String> checkFields = new Vector<String>();
		    checkFields.add("FIRST_NAME");
		    checkFields.add("LAST_NAME");
		    checkFields.add("FULL_NAME");
	
		    CCLAUtils.removeResultSetValueChars(rsExistingPerson, Globals.BAD_JSON_CHARS, checkFields);
	
		    m_binder.addResultSet("rsExistingPerson", rsExistingPerson);
		}
	
		return rsExistingPerson;
    }
    
    
    private void executeQASSearch() throws ServiceException {
    	// Search over QAS for address
    	Log.debug("SEARCHING over QAS");
    	String searchStringQAS = PostcodeSearch;
    	
    	if (!StringUtils.stringIsBlank(HouseNameSearch)) {
    		searchStringQAS = HouseNameSearch + "," + PostcodeSearch;
    	}
    	
    	Log.debug("searchStringQAS:" + searchStringQAS);
    	m_binder.putLocal("overrideString", searchStringQAS);
    	
    	if (doQASTreeSearch)
    		searchAddressTree();
    	else
    		searchAddress();
    }
}
