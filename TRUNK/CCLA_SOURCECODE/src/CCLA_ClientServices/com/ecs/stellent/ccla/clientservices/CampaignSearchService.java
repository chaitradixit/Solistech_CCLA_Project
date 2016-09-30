package com.ecs.stellent.ccla.clientservices;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.server.Service;


public class CampaignSearchService extends Service {

	//private static final int NUMBER_OF_SEARCH_RESULTS = 20;
	
	/* Specifies the type of operator */
	private static final int OPERATOR_EQUALS 				= 0; //default
	private static final int OPERATOR_LESS_THAN_EQUALS 		= 1;
	private static final int OPERATOR_GREATER_THAN_EQUALS 	= 2;
	private static final int OPERATOR_BETWEEN 				= 3;
	
	
	/* Specifies the return type of the search i.e organisation or person */
	private static final int SEARCH_TYPE_ENTITY				= 0;
	private static final int SEARCH_TYPE_PERSON				= 1;	
	private static final String ALL_STRING					= "ALL";
	private static final String NONE_STRING					= "-";
	private static final String FORM_PARAMS_REGION 			= "region";
	private static final String FORM_PARAMS_POSTCODE 		= "postcode";
	private static final String FORM_PARAMS_COMPANY 		= "company";
	private static final String FORM_PARAMS_AMOUNT_A 		= "priceAmount";
	private static final String FORM_PARAMS_AMOUNT_B 		= "priceAmountExt";
	private static final String FORM_PARAMS_OPERATOR 		= "operator";
	private static final String FORM_PARAMS_SHOW_REGION 	= "show_region";
	private static final String FORM_PARAMS_SHOW_POSTCODE 	= "show_postcode";
	private static final String FORM_PARAMS_SHOW_COMPANY 	= "show_company";
	private static final String FORM_PARAMS_SEARCH_PARAMS 	= "search_params";
	
	private static final String BASIC_PARAM_ORGANISATION_ID 	= "ORGANISATION_ID";
	private static final String BASIC_PARAM_ORGANISATION_NAME 	= "ORGANISATION_NAME";
	private static final String BASIC_PARAM_PERSON_ID 			= "PERSON_ID";
	private static final String BASIC_PARAM_PERSON_NAME 		= "PERSON_NAME";
	private static final String BASIC_PARAM_CATEGORY_ID 		= "CATEGORY_ID";
	private static final String BASIC_PARAM_COMPANY 			= "COMPANIES";
	private static final String BASIC_PARAM_AMOUNT 				= "AMOUNT";
	private static final String BASIC_PARAM_AMOUNTEXT 			= "AMOUNTEXT";
	private static final String BASIC_PARAM_OPERATOR 			= "OPERATOR";
	
//rem out for now.	
//	private static final String SQL_SUM_AMOUNT_SELECT_CLAUSE = 
//			"SUM(CASE "+
//				"WHEN ((V3.TOT_LONGTERM_ONLY IS NULL) OR (V3.TOT_LONGTERM_ONLY BETWEEN 0 AND 100000)) <<COMPANY_COND>> THEN 1 "+
//			  	"ELSE 0 "+
//			"END) AS BETWEEN_0_100K<<COMPANY>>, "+
//			"SUM(CASE "+
//			  	"WHEN (V3.TOT_LONGTERM_ONLY BETWEEN 100000 AND 250000) <<COMPANY_COND>> THEN 1 "+
//			  	"ELSE 0 "+
//			"END) AS BETWEEN_100K_250K<<COMPANY>>, "+
//			"SUM(CASE " +
//			  	"WHEN (V3.TOT_LONGTERM_ONLY BETWEEN 250000 AND 1000000) <<COMPANY_COND>> THEN 1 "+
//			  	"ELSE 0 "+
//			"END) AS BETWEEN_250K_1M<<COMPANY>>, "+
//			"SUM(CASE "+ 
//			  	"WHEN (V3.TOT_LONGTERM_ONLY BETWEEN 1000000 AND 5000000) <<COMPANY_COND>> THEN 1 "+
//			  	"ELSE 0 "+
//			"END) AS BETWEEN_1M_5M<<COMPANY>>, "+
//			"SUM(CASE "+
//			  	"WHEN (V3.TOT_LONGTERM_ONLY BETWEEN 5000000 AND 10000000) <<COMPANY_COND>> THEN 1 "+
//			  	"ELSE 0 "+
//			"END) AS BETWEEN_5M_10M<<COMPANY>>, "+
//			"SUM(CASE "+
//			  	"WHEN (V3.TOT_LONGTERM_ONLY BETWEEN 10000000 AND 25000000) <<COMPANY_COND>> THEN 1 "+
//			  	"ELSE 0 "+
//			"END) AS BETWEEN_10M_25M<<COMPANY>>, "+
//			"SUM(CASE "+
//			  	"WHEN (V3.TOT_LONGTERM_ONLY >25000000) <<COMPANY_COND>> THEN 1 "+
//			  	"ELSE 0 "+
//			"END) AS OVER_25M<<COMPANY>> ";
//	
//	public void getSearchResults() throws DataException 
//	{
//		FWFacade facade = CCLAUtils.getFacade(m_workspace);
//
//		//These are a list of comma separated entries
//		String region = m_binder.getLocal(FORM_PARAMS_REGION);
//		String postcode = m_binder.getLocal(FORM_PARAMS_POSTCODE);
//		String company = m_binder.getLocal(FORM_PARAMS_COMPANY);
//		
//		boolean showRegion = CCLAUtils.getBinderBoolValue(m_binder, FORM_PARAMS_SHOW_REGION);
//		boolean showPostcode = CCLAUtils.getBinderBoolValue(m_binder, FORM_PARAMS_SHOW_POSTCODE);
//		boolean showCompany = CCLAUtils.getBinderBoolValue(m_binder, FORM_PARAMS_SHOW_COMPANY);
//		
//		float priceA = 0;
//		try {
//			priceA = CCLAUtils.getBinderFloatValue(m_binder, FORM_PARAMS_AMOUNT_A);
//		} catch (Exception e) {
//			priceA = 0;
//		}
//		
//		float priceB = 0;
//		try {
//			CCLAUtils.getBinderFloatValue(m_binder, FORM_PARAMS_AMOUNT_B);
//		} catch (Exception e) {
//			priceB = 0;
//		}
//			
//		int operator = OPERATOR_EQUALS;
//		try {
//			operator = CCLAUtils.getBinderIntegerValue(m_binder, FORM_PARAMS_OPERATOR);
//		} catch (Exception e) {
//			operator = OPERATOR_EQUALS;
//		}
//		
//		Log.debug("showCompany = "+m_binder.getLocal(FORM_PARAMS_SHOW_COMPANY)+", "+showCompany);
//		
//		if (StringUtils.stringIsBlank(company))
//			return;
//		
//		String query = getQuery(region, showRegion, postcode, showPostcode, company, showCompany, 
//				priceA, priceB, operator);
//		
//		
//		//Organisation/Client Data
//		Log.debug("query:" + query);	
//
//		
//		DataResultSet rsSearchResults = facade.createResultSetSQL(query);
//		if (!rsSearchResults.isEmpty()) {
//			m_binder.addResultSet("rsSearchResults", rsSearchResults);		
//		}	
//		
//		Log.debug("returned rsSearchResults with rows:" + rsSearchResults.getNumRows());	
//	}
//	
//	
//	public void getSearchResultsL2() throws DataException 
//	{
//		String searchParams = m_binder.getLocal(FORM_PARAMS_SEARCH_PARAMS);
//		
//		if (StringUtils.stringIsBlank(searchParams)) {
//			Log.error("searchParams is blank");
//			throw new DataException("searchParams is blank");
//		}
//		
//		String query = getL2Query(searchParams);
//		
//		//Organisation/Client Data
//		Log.debug("query:" + query);	
//
//		FWFacade facade = CCLAUtils.getFacade(m_workspace);
//		
//		DataResultSet rsL2SearchResults = facade.createResultSetSQL(query);
//		if (!rsL2SearchResults.isEmpty()) {
//			m_binder.addResultSet("rsL2SearchResults", rsL2SearchResults);		
//		}	
//		
//		Log.debug("returned rsL2SearchResults with rows:" + rsL2SearchResults.getNumRows());
//	}
//	
//	/**
//	 * Get the actual search result
//	 * @param searchParams
//	 * @return
//	 */
//	private String getL2Query(String searchParams) 
//	{
//		final StringBuffer sb = new StringBuffer(20);
//		boolean requiresAND = false;
//		
//		String[] params = searchParams.split("\\|");
//		
//		sb.append("SELECT V2.REGION, V2.POSTCODE, V2.POST_TOWN, V2.CHARITY_REF, V2.COMPANY_CODE, V2.ORGANISATION_NAME, ");
//		sb.append("V3.* FROM CCLA_BASE_CAMP_SEARCH2_VIEW V2 ");
//		sb.append("LEFT JOIN CCLA_BASE_CAMP_SEARCH3_VIEW V3 ON (V2.ORGANISATION_ID = V3.ORGANISATION_ID) ");
//		sb.append("WHERE ");
//		
//		//Query will always been in the form
//		//[Region]|[PostCode]|[Company]|[AmountA]|[AmountB] 		
//		
//		//Region
//		if (!StringUtils.stringIsBlank(params[0])) {
//			sb.append(getCSSQL(params[0], "V2.REGION=", null, true));
//			requiresAND = true;
//		}
//		
//		//Postcode
//		if (!StringUtils.stringIsBlank(params[1])) {
//			if (requiresAND) {
//				sb.append(" AND ");
//				requiresAND = false;
//			}
//			sb.append(getCSSQL(params[1], "V2.SHORT_POSTCODE=", null, true));
//			requiresAND = true;
//
//		}
//		
//		//Company
//		if (!StringUtils.stringIsBlank(params[2])) {
//			if (requiresAND) {
//				sb.append(" AND ");
//				requiresAND = false;
//			}
//			sb.append(getCSSQL(params[2], "V2.COMPANY_CODE=", null, true));
//			requiresAND = true;			
//		}
//		
//		//Amounts
//		if (!StringUtils.stringIsBlank(params[3]) && !StringUtils.stringIsBlank(params[4])) {
//			
//			if (!params[3].equals("ALL") && !params[4].equals("ALL")) {
//				if (requiresAND) {
//					sb.append(" AND ");
//					requiresAND = false;
//				}
//				
//				//use the between statement				
//				sb.append("(V3.TOT_LONGTERM_ONLY BETWEEN "+params[3]+" AND "+params[4]+")");
//			}
//			else if (params[4].equals("ALL") && !params[3].equals("ALL")) {
//				if (requiresAND) {
//					sb.append(" AND ");
//					requiresAND = false;
//				}
//				
//				//use greater than
//				sb.append("(V3.LONGTERM_ONLY>"+params[3]+")");				
//			} 
//		}
//		return sb.toString();
//	}
//	
//	/**
//	 * 
//	 * @param region
//	 * @param showRegion
//	 * @param postcode
//	 * @param showPostcode
//	 * @param company
//	 * @param showCompany
//	 * @return
//	 */
//	private String getQuery(String region, boolean showRegion, 
//			String postcode, boolean showPostcode, 
//			String company, boolean showCompany,
//			float priceA, float priceB, int operator) {
//			
//		
//		boolean appendCommaForField = false;
//		
//		final StringBuffer sb = new StringBuffer(20);
//		sb.append("SELECT ");
//		
//		if (showRegion) {
//			sb.append("V2.REGION ");
//			appendCommaForField = true;
//		}
//		
//		if (showPostcode) {
//			if (appendCommaForField) {
//				sb.append(", ");
//				appendCommaForField = false;
//
//			}
//			sb.append("V2.SHORT_POSTCODE, V2.POST_TOWN ");
//			appendCommaForField = true;
//		}
//
//
//		if (!StringUtils.stringIsBlank(company)) {
//			if (appendCommaForField) {
//				sb.append(", ");
//				appendCommaForField = false;
//			}
//
//			if (showCompany) {
//				
//				sb.append(getCompanyTotalsSQL(company));
//				sb.append(", ");
//				sb.append(getIndCompanyTotalsSQL(company));		
//
//			} else {
//				sb.append(getCompanyTotalsSQL(company));
//			}
//			
//			sb.append(", ");
//		}
//		
//		//Sum amounts and from 
//		sb.append(getCompanyAmountSQL(company, showCompany));		
//		sb.append(" ");
//		
//		sb.append(getTableClause());
//		sb.append(" ");
//		
//		//Where 
//		sb.append("WHERE ");
//		
//		//company is mandatory for searches to work
//		sb.append(getCSSQL(company,  "V2.IS_", "=1", false));
//		
//		if (!StringUtils.stringIsBlank(postcode)) {
//			sb.append(" AND ");
//			sb.append(getCSSQL(postcode,  "V2.SHORT_POSTCODE=", null, true));
//		}
//		if (!StringUtils.stringIsBlank(region)) {
//			sb.append(" AND");					
//			sb.append(getCSSQL(region,  "V2.REGION=", null, true));
//		}
//
//		if (priceA!=0) {
//			switch(operator) {
//				case OPERATOR_EQUALS:
//					sb.append(" AND (V3.TOT_LONGTERM_ONLY="+priceA+")");
//					break;
//				case OPERATOR_LESS_THAN_EQUALS:
//					sb.append(" AND (V3.TOT_LONGTERM_ONLY<="+priceA+") ");
//					break;
//				case OPERATOR_GREATER_THAN_EQUALS:
//					sb.append(" AND (V3.TOT_LONGTERM_ONLY>="+priceA+") ");					
//					break;
//				case OPERATOR_BETWEEN:
//					if (priceB!=0) {
//						sb.append(" AND (V3.TOT_LONGTERM_ONLY BETWEEN "+priceA+"+ AND "+priceB+") ");
//					}
//					break;
//				default:
//					break;
//			}
//		}
//		
//		sb.append(" ");
//		//Group by
//		if (showRegion || showPostcode) {
//			sb.append("GROUP BY ");
//			
//			if (showRegion) {
//				sb.append("V2.REGION");
//				appendCommaForField = true;
//			}
//			
//			if (showPostcode) {
//				if (appendCommaForField) {
//					sb.append(", ");
//					appendCommaForField = false;
//				}
//				sb.append("V2.SHORT_POSTCODE, V2.POST_TOWN");
//			}
//		}
//		
//		sb.append(" ");
//		//OrderBy
//		if (showRegion || showPostcode) {
//			sb.append("ORDER BY ");
//			
//			if (showRegion) {
//				sb.append("V2.REGION");
//				appendCommaForField = true;
//			}
//			
//			if (showPostcode) {
//				if (appendCommaForField) {
//					sb.append(", ");
//					appendCommaForField = false;
//				}
//				sb.append("V2.SHORT_POSTCODE");			
//			}
//		}
//		return sb.toString();
//		
//	}
//	
//	public String getTableClause() {
//		return "FROM CCLA_BASE_CAMP_SEARCH2_VIEW V2 "+
//			   "LEFT JOIN CCLA_BASE_CAMP_SEARCH3_VIEW V3 ON (V2.ORGANISATION_ID = V3.ORGANISATION_ID) ";
//	}
//
//	private String getCompanyTotalsSQL(String companyStr) 
//	{	
//		final StringBuffer sb = new StringBuffer(20);
//		
//		if (!StringUtils.stringIsBlank(companyStr)) {
//			String[] companiesStr = companyStr.split(",");
//			
//			sb.append("SUM(");
//			for (int i=0; i<companiesStr.length; i++) {
//				
//				if (i!=0) {
//					sb.append("+");
//				}
//				sb.append("V2.IS_"+companiesStr[i]);				
//
//			}
//			sb.append(") AS TOTAL");
//			
//		}		
//		return sb.toString();		
//	}
//	
//	private String getIndCompanyTotalsSQL(String companyStr) 
//	{	
//		final StringBuffer sb = new StringBuffer(20);
//		
//		if (!StringUtils.stringIsBlank(companyStr)) {
//			String[] companiesStr = companyStr.split(",");
//			
//			for (int i=0; i<companiesStr.length; i++) {
//				
//				if (i!=0) {
//					sb.append(", ");
//				}
//				sb.append("SUM(V2.IS_"+companiesStr[i]+") AS TOTAL_"+companiesStr[i]);				
//
//			}
//		}		
//		return sb.toString();		
//	}
//	
//	private String getCompanyAmountSQL(String companyStr, boolean showCompany) 
//	{	
//		final StringBuffer sb = new StringBuffer(50);
//		if (!StringUtils.stringIsBlank(companyStr) && showCompany) 
//		{
//			String[] companiesStr = companyStr.split(",");
//			
//			for (int i=0; i<companiesStr.length; i++) 
//			{
//				if (i!=0)
//					sb.append(", ");
//				
//				String sql = StringUtils.replaceInString(SQL_SUM_AMOUNT_SELECT_CLAUSE, "<<COMPANY>>", "_"+companiesStr[i]);
//				sql = StringUtils.replaceInString(sql, "<<COMPANY_COND>>", "AND IS_"+companiesStr[i]+"=1");
//				sb.append(sql);
//			}
//			
//			sb.append(", ");
//			
//			//Also append a total QUERY
//			String sql = StringUtils.replaceInString(SQL_SUM_AMOUNT_SELECT_CLAUSE, "<<COMPANY>>", "");
//			sql = StringUtils.replaceInString(sql, "<<COMPANY_COND>>", "AND "+getCSSQL(companyStr, "IS_", "=1", false));
//			sb.append(sql);
//		
//		} else {
//			String sql = StringUtils.replaceInString(SQL_SUM_AMOUNT_SELECT_CLAUSE, "<<COMPANY>>", "");
//			sql = StringUtils.replaceInString(sql, "<<COMPANY_COND>>", "");			
//			sb.append(sql);
//		}
//		return sb.toString();
//	}
//
	private String getCSSQL(String commaStr, String prefix, String postfix, boolean addQuotes) 
	{
		final StringBuffer sb = new StringBuffer(20);
		if (!StringUtils.stringIsBlank(commaStr)) {
			String[] indStr = commaStr.split(",");
			
			if (indStr.length>0)
				sb.append(" (");
			
			for (int i=0; i<indStr.length; i++) {
				
				if (i!=0) {
					sb.append(" OR ");
				}
				sb.append((StringUtils.stringIsBlank(prefix)?"":prefix));
				if (addQuotes)
					sb.append("'");
				sb.append(indStr[i]);
				if (addQuotes)
					sb.append("'");
				sb.append((StringUtils.stringIsBlank(postfix)?"":postfix));				
			}
			
			if (indStr.length>0)
				sb.append(") ");
		}
		return sb.toString();
	}
	
/*****************************************************************************************************************/	
	
	/**
	 * Basic Search
	 * 
	 */
	public void getBasicCampaignSearchResults() throws DataException, ServiceException 
	{		
		String query = getBasicQuery();
		
		if (StringUtils.stringIsBlank(query)) {
			throw new DataException("search query is null.");
		}
		
		Log.debug("Basic Search:"+query);
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace);
		
		DataBinder binder = new DataBinder();
		DataResultSet rsSearchResults = facade.createResultSetSQL(query);
		
		if (rsSearchResults!=null && !rsSearchResults.isEmpty())
			m_binder.addResultSet("rsSearchResults", rsSearchResults);
		else 
			Log.debug("no results found!!");
	}	

	/**
	 * 
	 * @return
	 * @throws DataException
	 */
	private String getBasicQuery() throws DataException 
	{
		
		String organisationName = 
			BinderUtils.getBinderStringValue(m_binder, BASIC_PARAM_ORGANISATION_NAME);
				
		String companies = 
			BinderUtils.getBinderStringValue(m_binder, BASIC_PARAM_COMPANY);
		
		Integer organisationId =
			BinderUtils.getBinderIntegerValue(m_binder, BASIC_PARAM_ORGANISATION_ID);
		
		Integer personId =
			BinderUtils.getBinderIntegerValue(m_binder, BASIC_PARAM_PERSON_ID);
		
		Integer categoryId = 
			BinderUtils.getBinderIntegerValue(m_binder, BASIC_PARAM_CATEGORY_ID);
		
		int operator =
			BinderUtils.getBinderIntegerValue(m_binder, BASIC_PARAM_OPERATOR);
		
		Float amount = 
			BinderUtils.getBinderFloatValue(m_binder, BASIC_PARAM_AMOUNT);
		
		Float amountExt =
			BinderUtils.getBinderFloatValue(m_binder, BASIC_PARAM_AMOUNTEXT);
		
		String personName = 
			BinderUtils.getBinderStringValue(m_binder, BASIC_PARAM_PERSON_NAME);

		if (StringUtils.stringIsBlank(companies) && StringUtils.stringIsBlank(organisationName)
				&& StringUtils.stringIsBlank(personName) && organisationId==null && personId==null
				&& categoryId==null && amount==null && amountExt==null) {
			throw new DataException("Cannot search, no parameters specified.");
		}
			
		final StringBuffer sb = new StringBuffer(20);
		sb.append("SELECT * FROM (");
		sb.append("SELECT COMPANY_CODE, ORGANISATION_ID, ORGANISATION_NAME, NUM_ACCOUNTS, CASH FROM V_ORG_ACC_PER WHERE ");

		boolean includeAND = false;
		//either person or organisation search
		
		if (!StringUtils.stringIsBlank(personName) || personId!=null) {
			//do person search 
			
			if (!StringUtils.stringIsBlank(personName)) {
				sb.append("FULL_NAME like '%").append(personName.toUpperCase()).append("%'");
				includeAND = true;
			}
			
			if (personId!=null) {
				if (includeAND)
					sb.append(" AND ");
				sb.append("PERSON_ID=").append(personId.intValue());
				includeAND=true;
			}
		} 
		else 
		{
			//do organisation search
			
			//organisation Name
			if (!StringUtils.stringIsBlank(organisationName)) {
				sb.append("ORGANISATION_NAME LIKE '%").append(organisationName.toUpperCase()).append("%'");
				includeAND = true;
			}
			
			//organisation ID
			if (organisationId!=null) {
				if (includeAND)
					sb.append(" AND ");
				
				sb.append("ORGANISATION_ID=").append(organisationId);
				includeAND = true;
			}
			
			if (categoryId!=null) {
				if (includeAND)
					sb.append(" AND ");
				
				sb.append("(CATEGORY_ID IN (");
				sb.append("SELECT CATEGORY_ID FROM REF_ORG_CATEGORY ");
				sb.append("START WITH PARENT_CATEGORY_ID=").append(categoryId);
				sb.append("CONNECT BY PRIOR CATEGORY_ID=PARENT_CATEGORY_ID) OR ");
				sb.append("CATEGORY_ID=").append(categoryId).append(")");
				includeAND=true;
			}
			
			
			if (!StringUtils.stringIsBlank(companies)) {
				if (includeAND)
					sb.append(" AND ");
				//company is mandatory for searches to work
				sb.append(getCSSQL(companies,  "COMPANY_CODE=", null, true));
				includeAND = true;
			}
			
			//Operator and amounts
			if (amount!=null) {
				
				switch(operator) {
					case OPERATOR_EQUALS:
						if (includeAND)
							sb.append(" AND ");
						
						sb.append(" AND (CASH="+amount+")");
						break;
					case OPERATOR_LESS_THAN_EQUALS:
						if (includeAND)
							sb.append(" AND ");
						
						sb.append(" AND (CASH<="+amount+") ");
						break;
					case OPERATOR_GREATER_THAN_EQUALS:
						if (includeAND)
							sb.append(" AND ");
						
						sb.append("(CASH>="+amount+")");
						
						break;
					case OPERATOR_BETWEEN:
						if (amountExt!=null) {
							if (includeAND)
								sb.append(" AND ");
							
							sb.append("(CASH BETWEEN "+amount+"+ AND "+amountExt+") ");
						}
						break;
					default:
						break;
				}
			}
		
		}
		
		sb.append(" GROUP BY COMPANY_CODE, ORGANISATION_ID, ORGANISATION_NAME, NUM_ACCOUNTS, CASH");
		sb.append(") WHERE ROWNUM<=100");
		return sb.toString();
	}
}

		//Getting organisation with correspondant name
		/*
SELECT R.RELATION_NAME_ID, R.ELEMENT_ID1, R.ELEMENT_ID2, O.ORGANISATION_NAME, P.FULL_NAME, RRN.RELATION, RRT.RELATION_LABEL FROM RELATIONS R 
RIGHT JOIN ORGANISATION O ON (R.ELEMENT_ID1 = O.ORGANISATION_ID) 
RIGHT JOIN PERSON P ON (R.ELEMENT_ID2 = P.PERSON_ID)
LEFT JOIN REF_RELATION_NAMES RRN ON (R.RELATION_NAME_ID = RRN.RELATION_NAME_ID)
LEFT JOIN REF_RELATION_TYPES RRT ON (RRN.RELATION_TYPE_ID = RRT.RELATION_TYPE_ID AND RRT.RELATION_TYPE_ID=1) 
WHERE R.RELATION_NAME_ID=1;	

		*/
		
		//Getting child categories
		/*
SELECT CATEGORY_ID
      FROM REF_ORG_CATEGORY
      START WITH PARENT_CATEGORY_ID =205
      CONNECT BY PRIOR CATEGORY_ID = PARENT_CATEGORY_ID
      ORDER SIBLINGS BY CATEGORY_ID
		*/
		
	
	
	
	//Organisation - Account totals
	/*
SELECT
RC.COMPANY_CODE, 
O.CATEGORY_ID, O.ORGANISATION_ID, O.ORGANISATION_NAME, 
COUNT(ACC_DETAILS.ACCOUNT_ID) AS NUM_ACCOUNTS, 
SUM(
  CASE WHEN ACC_DETAILS.TOTAL_CASH IS NULL THEN 0 
  ELSE ACC_DETAILS.TOTAL_CASH
END
) AS CASH
FROM RELATIONS R 
RIGHT JOIN ORGANISATION O ON (R.ELEMENT_ID1 = O.ORGANISATION_ID) 
LEFT JOIN REF_RELATION_NAMES RRN ON (R.RELATION_NAME_ID = RRN.RELATION_NAME_ID)
LEFT JOIN REF_RELATION_TYPES RRT ON (RRN.RELATION_TYPE_ID = RRT.RELATION_TYPE_ID)
LEFT JOIN CLIENT_AURORA_MAP CAM ON (O.ORGANISATION_ID = CAM.ORGANISATION_ID)
LEFT JOIN REF_COMPANY RC ON (CAM.COMPANY_ID = RC.COMPANY_ID)
LEFT JOIN
(SELECT 
  ACC.ACCOUNT_ID, 
  SUM(AV.ACC_CASH) AS TOTAL_CASH, 
FROM ACCOUNT ACC
LEFT JOIN ACCOUNT_VALUE AV ON (ACC.ACCOUNT_ID = AV.ACCOUNT_ID)
GROUP BY ACC.ACCOUNT_ID) ACC_DETAILS ON (R.ELEMENT_ID2 = ACC_DETAILS.ACCOUNT_ID) 
GROUP BY RC.COMPANY_CODE, O.CATEGORY_ID, O.ORGANISATION_ID, O.ORGANISATION_NAME
	*/
	


