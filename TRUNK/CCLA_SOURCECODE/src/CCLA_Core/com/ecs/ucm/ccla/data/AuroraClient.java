package com.ecs.ucm.ccla.data;

import java.util.Date;
import java.util.HashMap;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.shared.SharedObjects;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Wrapper class for storing Client Number/Company
 *  used by the Aurora database, mapped against the Entity/Clients
 *  in the central DB.
 *  
 * @author Tom
 *
 */
public class AuroraClient implements Persistable {
	
	private int mapId; 			/* CLIENT_AURORA_MAP_ID value */
	private int organisationId; /* ORGANISATION_ID (element ID) value */
	
	private int clientNumber; 	/* Aurora Client Number */
	private Company company;		/* Aurora Company */
	
	private Integer contributorTypeCode;
	private Integer subdivisionCode;
	private Integer contributorCode;
	
	private boolean incomeDistReportIndicator;
	private boolean incomeDistReportPaperIndicator;
	private Integer numDepositStatements;
	private Integer numUnitisedStatements;
	
	private Integer statementMonths1;
	private Integer statementMonths2;
	private Integer statementMonths3;
	private Integer statementMonths4;
	
	private boolean statementMonthsAll;
	
	private boolean bulkDepositStatementIndicator;
	private boolean depositWithdrawBooksIndicator;
	
	private boolean autoStationaryOrderedIndicator;
	private boolean quarterlyReportClientIndicator;
	
	private String quarterlyReportManagerInitials;
	
	private Integer marketingGroupId;
	private Integer marketingSubGroupId;
	private Integer accountGroupId;
	
	private Date lastUpdated;
	private String lastUpdatedBy;
	
	public static final class ContributorTypeCodes {
		public static final int DIOCESAN_AUTHORITY             	= 1;
		public static final int CATHEDRAL_AUTHORITY            	= 2;
		public static final int CENTRAL_CHURCH_ORGANIZATION    	= 3;
		public static final int THEOLOGICAL_COLLEGE            	= 4;
		public static final int CHURCH_COLLEGE_OF_EDUCATION    	= 5;
		public static final int PAROCHIAL_AUTHORITY            	= 6;
		public static final int MISCELLANEOUS                  	= 7;
		public static final int OFFICIAL_CUSTODIAN_FOR_CHRTIES 	= 8;
	}
	
	/** Upper limit for an Aurora Client Number. */
	public static final int MAX_AURORA_CLIENT_NUMBER = 999999;
	
	/** Determines whether an Organisation Account Code suffix will always be updated
	 *  to match an allocated Client Number.
	 */
	public static final boolean UPDATE_ORG_ACCOUNT_CODE = 
	 (!StringUtils.stringIsBlank(SharedObjects.getEnvironmentValue
	 ("CCLA_UpdateOrgAccountCodeToMatchClientNumber")));
	
	/** Whether or not the Kainos Client Number fetch function is used to obtain new
	 *  Client Numbers, as opposed to the Central DB sequence.
	 * 
	 */
	public static final boolean FETCH_CLIENT_NUMBER_BY_FUNCTION =
	 (!StringUtils.stringIsBlank(SharedObjects.getEnvironmentValue
	 ("CCLA_FetchAuroraClientNumberByFunction")));
	
	/** (Optional) owner of the Client Number Fetch pacakge/function. If present this
	 *  will be added as a prefix to the function call.
	 */
	public static final String CLIENT_NUMBER_FETCH_FUNCTION_OWNER_NAME = 
	 SharedObjects.getEnvironmentValue
	 ("CCLA_ClientNumberFetchFunctionOwnerName");
			
	public static class Cols {
		public static final String ID = "CLIENT_AURORA_MAP_ID";
		public static final String CLIENT_NUMBER = "CLIENT_NUMBER";
		public static final String COMPANY = "COMPANY_ID";
		
		public static final String CONTRIBUTOR_TYPE_CODE	= "CONTRIBUTER_TYPE_CODE";
		public static final String SUBDIVISION_CODE 		= "SUBDIVISION_CODE";
		public static final String CONTRIBUTER_CODE 		= "CONTRIBUTER_CODE";
		
		public static final String INCOME_DIST_REPORT_IND	= "INCOME_DIST_REPORT_IND";
		public static final String INCOME_DIST_REPORT_PAPER_IND 
		 = "INCOME_DIST_REPORT_PAPER_IND";
		
		public static final String DEPOSIT_NO_OF_STATEMENTS 
		 = "DEPOSIT_NO_OF_STATEMENTS";
		public static final String UNITISED_NO_OF_STATEMENTS 
		 = "UNITISED_NO_OF_STATEMENTS";
		
		public static final String STATEMENTS_MONTH_1 = "STATEMENTS_MONTH_1";
		public static final String STATEMENTS_MONTH_2 = "STATEMENTS_MONTH_2";
		public static final String STATEMENTS_MONTH_3 = "STATEMENTS_MONTH_3";
		public static final String STATEMENTS_MONTH_4 = "STATEMENTS_MONTH_4";
		
		public static final String STATEMENTS_MONTH_ALL = "STATEMENTS_MONTH_ALL";
		
		public static final String BULK_DEPOSIT_STATEMENT_IND
		 = "BULK_DEPOSIT_STATEMENT_IND";
		public static final String DEPOSIT_WITHDRAW_BOOKS_IND 
		 = "DEPOSIT_WITHDRAW_BOOKS_IND";
		public static final String AUTO_STATIONARY_ORDERED_IND
		 = "AUTO_STATIONARY_ORDERED_IND";
		
		public static final String QUARTERLY_RPT_CLIENT_IND
		 = "QUARTERLY_RPT_CLIENT_IND";
		public static final String QUARTERLY_RPT_MANAGER_INITS
		 = "QUARTERLY_RPT_MANAGER_INITS";
		
		public static final String MARKETING_GROUP_ID = "MARKETING_GROUP_ID";
		public static final String MARKETING_SUBGROUP_ID = "MARKETING_SUBGROUP_ID";
		public static final String ACCOUNT_GROUP_ID = "ACCOUNT_GROUP_ID";
	}
	
	public static AuroraClient add
	 (DataBinder binder, FWFacade facade, String userName) throws DataException {
		
		AuroraClient auroraClient = new AuroraClient();
		auroraClient.setAttributes(binder);
		
		return add(auroraClient, facade, userName);
	}
	
	public static AuroraClient add
	 (AuroraClient auroraClient, FWFacade facade, String userName) throws DataException{
		
		Log.debug("Adding new AuroraClient mapping (" 
		 + auroraClient.getClientNumber() + ", " 
		 + auroraClient.getCompany().getCode() + ")");
				
		int mapId = Integer.parseInt(
		 CCLAUtils.getNewKey("ClientAuroraMap", facade));	
		
		auroraClient.setMapId(mapId);
		auroraClient.setLastUpdatedBy(userName);
		
		auroraClient.validate(facade);
		
		DataBinder binder = new DataBinder();
		auroraClient.addFieldsToBinder(binder);
		
		facade.execute("qClientServices_AddClientAuroraMap", binder);
		
		// Add audit record
		DataResultSet newData = AuroraClient.getData(mapId, facade);
		
		HashMap<Integer, String> auditRelations = 
		 new HashMap<Integer, String>();
		
		auditRelations.put
		 (mapId, ElementType.SecondaryElementType.ClientAuroraMap.toString());
		auditRelations.put
		 (auroraClient.getOrganisationId(), ElementType.ORGANISATION.getName());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, userName, 
		 Globals.AuditActions.ADD.toString(), 
		 ElementType.SecondaryElementType.ClientAuroraMap.toString(), 
		 null, newData, auditRelations);
		
		if (UPDATE_ORG_ACCOUNT_CODE) {
			// Ensure the Org Account Code has been updated to match the Client Number.
			Entity org = Entity.get(auroraClient.getOrganisationId(), facade);
			
			int orgCodeSuffix = 
			 CCLAUtils.getAccountCodeNumberSuffix(org.getOrgAccountCode());
			
			Log.debug("Checking existing Org Account Code suffix " + orgCodeSuffix +
			 " against newly-allocated Client Number: " + 
			 auroraClient.getClientNumber());
			
			if (orgCodeSuffix != auroraClient.getClientNumber()) {
				Log.debug("Existing Org Account Code " + org.getOrgAccountCode() +
				" numeric suffix did not match allocated Client Number (" + 
				auroraClient.getClientNumber() + "). Updating now.");
				
				String orgCodePrefix = org.getOrgAccountCode().substring(0, 4);
				String newOrgCode = orgCodePrefix + CCLAUtils.padString
				  (auroraClient.getClientNumber().toString(), '0', 8);
				
				Log.debug("Seting new Org Account Code: " + newOrgCode);
				
				// Run the update as System user.
				org.setOrgAccountCode(newOrgCode);
				org.persist(facade, com.ecs.ucm.ccla.Globals.Users.System);
			} else {
				Log.debug("No Account Code update required");
			}
		}
		
		return get(newData);
	}
	
	/** Adds a new Organisation-Aurora Client mapping.
	 * 
	 * @param orgId
	 * @param clientNumber
	 * @param company
	 * @return
	 * @throws DataException 
	 * @throws NumberFormatException 
	 */
	public static AuroraClient add(int orgId, Company company, int clientNumber,
		int contributorTypeCode,
		int subdivisionCode, int contributorCode,
		boolean incomeDistReportIndicator,
		boolean incomeDistReportPaperIndicator,
		Integer numDepositStatements, Integer numUnitisedStatements,
		Integer statementMonths1, Integer statementMonths2,
		Integer statementMonths3, Integer statementMonths4,
		boolean statementMonthsAll, boolean bulkDepositStatementIndicator,
		boolean depositWithdrawBooksIndicator,
		boolean autoStationaryOrderedIndicator,
		boolean quarterlyReportClientIndicator,
		String quarterlyReportManagerInitials, Integer marketingGroupId,
		Integer marketingSubGroupId, Integer accountGroupId,
		FWFacade facade, String userName) throws DataException{
		
		AuroraClient auroraClient = new AuroraClient
		 (0, orgId, company, clientNumber, contributorTypeCode, subdivisionCode, 
		 contributorCode, incomeDistReportIndicator, incomeDistReportPaperIndicator, 
		 numDepositStatements, numUnitisedStatements, 
		 statementMonths1, statementMonths2, statementMonths3, statementMonths4,
		 statementMonthsAll, 
		 bulkDepositStatementIndicator, depositWithdrawBooksIndicator,
		 autoStationaryOrderedIndicator, 
		 quarterlyReportClientIndicator, quarterlyReportManagerInitials,
		 marketingGroupId, marketingSubGroupId, accountGroupId, 
		 null, userName);
		
		return add(auroraClient, facade, userName);
	}
	
	/** Removes an existing Aurora Client mapping.
	 * 
	 * @param mapId
	 * @throws DataException 
	 */
	public static void remove(int mapId, FWFacade facade, String userName) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.ID, mapId);
		
		DataResultSet remData = AuroraClient.getData(mapId, facade);
		AuroraClient remClient = get(remData);
		
		if (remClient == null)
			throw new DataException
			 ("Unable to remove Aurora Client: mapping not found");
		
		facade.execute("qClientServices_DeleteClientAuroraMap", binder);
		
		// Add audit record
		HashMap<Integer, String> auditRelations = 
		 new HashMap<Integer, String>();
		
		auditRelations.put
		 (mapId, ElementType.SecondaryElementType.ClientAuroraMap.toString());
		auditRelations.put
		 (remClient.getOrganisationId(), ElementType.ORGANISATION.getName());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, userName, 
		 Globals.AuditActions.DELETE.toString(), 
		 ElementType.SecondaryElementType.ClientAuroraMap.toString(), 
		 remData, null, auditRelations);
	}
	
	public static boolean isValidClientNumber(int clientNumber) {
		return (clientNumber <= MAX_AURORA_CLIENT_NUMBER);
	}
	
	public static AuroraClient get(int mapId, FWFacade facade)
	 throws DataException {
		
		DataResultSet rsAuroraClient = getData(mapId, facade);
		return get(rsAuroraClient);
	}
	
	public static DataResultSet getData(int mapId, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.ID, mapId);
		
		return facade.createResultSet
		 ("qClientServices_GetAuroraClient", binder);
	}
	
	public static AuroraClient get(DataResultSet rs) throws DataException {
		if (rs.isEmpty())
			return null;
		
		int companyId = CCLAUtils.getResultSetIntegerValue
		 (rs, Cols.COMPANY);
		
		Company company = 
		 Company.getCache().getCachedInstance(companyId);
		
		return new AuroraClient(
			CCLAUtils.getResultSetIntegerValue(rs, Cols.ID),
			CCLAUtils.getResultSetIntegerValue(rs, SharedCols.ORG),
			company,
			CCLAUtils.getResultSetIntegerValue(rs, Cols.CLIENT_NUMBER),
			CCLAUtils.getResultSetIntegerValue(rs, Cols.CONTRIBUTOR_TYPE_CODE),
			CCLAUtils.getResultSetIntegerValue(rs, Cols.SUBDIVISION_CODE),
			CCLAUtils.getResultSetIntegerValue(rs, Cols.CONTRIBUTER_CODE),
			CCLAUtils.getResultSetBoolValue(rs, Cols.INCOME_DIST_REPORT_IND),
			CCLAUtils.getResultSetBoolValue(rs, Cols.INCOME_DIST_REPORT_PAPER_IND),
			CCLAUtils.getResultSetIntegerValue(rs, Cols.DEPOSIT_NO_OF_STATEMENTS),
			CCLAUtils.getResultSetIntegerValue(rs, Cols.UNITISED_NO_OF_STATEMENTS),
			CCLAUtils.getResultSetIntegerValue(rs, Cols.STATEMENTS_MONTH_1),
			CCLAUtils.getResultSetIntegerValue(rs, Cols.STATEMENTS_MONTH_2),
			CCLAUtils.getResultSetIntegerValue(rs, Cols.STATEMENTS_MONTH_3),
			CCLAUtils.getResultSetIntegerValue(rs, Cols.STATEMENTS_MONTH_4),
			CCLAUtils.getResultSetBoolValue(rs, Cols.STATEMENTS_MONTH_ALL),
			CCLAUtils.getResultSetBoolValue(rs, Cols.BULK_DEPOSIT_STATEMENT_IND),
			CCLAUtils.getResultSetBoolValue(rs, Cols.DEPOSIT_WITHDRAW_BOOKS_IND),
			CCLAUtils.getResultSetBoolValue(rs, Cols.AUTO_STATIONARY_ORDERED_IND),
			CCLAUtils.getResultSetBoolValue(rs, Cols.QUARTERLY_RPT_CLIENT_IND),
			rs.getStringValueByName(Cols.QUARTERLY_RPT_MANAGER_INITS),
			CCLAUtils.getResultSetIntegerValue(rs, Cols.MARKETING_GROUP_ID),
			CCLAUtils.getResultSetIntegerValue(rs, Cols.MARKETING_SUBGROUP_ID),
			CCLAUtils.getResultSetIntegerValue(rs, Cols.ACCOUNT_GROUP_ID),
			rs.getDateValueByName(SharedCols.LAST_UPDATED),
			rs.getStringValueByName(SharedCols.LAST_UPDATED_BY)
		);
	}
	
	/** Fetches a new unique Client Number which is guaranteed to be unique in the
	 *  Aurora Client Number space.
	 *  
	 *  The current sequence has a limited range and will throw a DB error once it
	 *  reaches 70000.
	 *  
	 * @param facade
	 * @return
	 * @throws DataException 
	 * @throws NumberFormatException 
	 */
	public static int getNewClientNumber(FWFacade facade) 
	 throws DataException {
		
		Integer num = null;
		
		if (FETCH_CLIENT_NUMBER_BY_FUNCTION) {
			Log.debug("Fetching new Client Number via function call");
			num = CCLAUtils.getResultSetIntegerValue(
			 facade.createResultSetSQL(getClientNumberFetchSQL()), "NEW_CLIENT_NUMBER");
		} else {
			Log.debug("Fetching new Client Number via sequence");
			num = Integer.parseInt(CCLAUtils.getNewKey("AuroraClientNumber", facade));
		}
		
		Log.debug("Fetched Client Number: " + num);
		return num;
	}

	/** Builds and returns the SELECT statement used to fetch new Client Numbers via the
	 *  Kainos DB function.
	 *  
	 * @return
	 */
	private static String getClientNumberFetchSQL() {
		return "SELECT " + 
		
		// Add owner name prefix, if specified
		 (!StringUtils.stringIsBlank(CLIENT_NUMBER_FETCH_FUNCTION_OWNER_NAME)
		 ? (CLIENT_NUMBER_FETCH_FUNCTION_OWNER_NAME + ".") : "") +
		 
		 " pkg_client_numbers.get_new_client_number() " +
		 "AS NEW_CLIENT_NUMBER FROM DUAL";
	}
	
	public AuroraClient() {}
	
	public AuroraClient
	 (int mapId, int orgId, Company company, int clientNumber,
		Integer contributorTypeCode,
		Integer subdivisionCode, Integer contributorCode,
		boolean incomeDistReportIndicator,
		boolean incomeDistReportPaperIndicator,
		Integer numDepositStatements, Integer numUnitisedStatements,
		Integer statementMonths1, Integer statementMonths2,
		Integer statementMonths3, Integer statementMonths4,
		boolean statementMonthsAll, boolean bulkDepositStatementIndicator,
		boolean depositWithdrawBooksIndicator,
		boolean autoStationaryOrderedIndicator,
		boolean quarterlyReportClientIndicator,
		String quarterlyReportManagerInitials, Integer marketingGroupId,
		Integer marketingSubGroupId, Integer accountGroupId,
		Date lastUpdated, String lastUpdatedBy) {
		
		this.setMapId(mapId);
		this.setOrganisationId(orgId);
		this.setClientNumber(clientNumber);
		this.setCompany(company);
		
		this.contributorTypeCode = contributorTypeCode;
		this.subdivisionCode = subdivisionCode;
		this.contributorCode = contributorCode;
		
		this.incomeDistReportIndicator = incomeDistReportIndicator;
		this.incomeDistReportPaperIndicator = incomeDistReportPaperIndicator;
		this.numDepositStatements = numDepositStatements;
		this.numUnitisedStatements = numUnitisedStatements;
		
		this.statementMonths1 = statementMonths1;
		this.statementMonths2 = statementMonths2;
		this.statementMonths3 = statementMonths3;
		this.statementMonths4 = statementMonths4;
		this.statementMonthsAll = statementMonthsAll;
		
		this.bulkDepositStatementIndicator = bulkDepositStatementIndicator;
		this.depositWithdrawBooksIndicator = depositWithdrawBooksIndicator;
		this.autoStationaryOrderedIndicator = autoStationaryOrderedIndicator;
		this.quarterlyReportClientIndicator = quarterlyReportClientIndicator;
		this.quarterlyReportManagerInitials = quarterlyReportManagerInitials;
		
		this.marketingGroupId = marketingGroupId;
		this.marketingSubGroupId = marketingSubGroupId;
		this.accountGroupId = accountGroupId;
		
		this.lastUpdated = lastUpdated;
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public void setClientNumber(int clientNumber) {
		this.clientNumber = clientNumber;
	}

	public Integer getClientNumber() {
		return clientNumber;
	}
	
	public String getPaddedClientNumber() {
		 return CCLAUtils.padClientNumber
		  (Integer.toString(this.clientNumber), this.company);
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public int getMapId() {
		return mapId;
	}

	public int getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(int organisationId) {
		this.organisationId = organisationId;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public Company getCompany() {
		return company;
	}

	public void addFieldsToBinder(DataBinder binder) throws DataException {

		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.ID, this.getMapId());
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, SharedCols.ORG, this.getOrganisationId());
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.COMPANY, this.getCompany().getCompanyId());
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.CLIENT_NUMBER, this.getClientNumber());
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.CONTRIBUTOR_TYPE_CODE, this.getContributorTypeCode());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.SUBDIVISION_CODE, this.getSubdivisionCode());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.CONTRIBUTER_CODE, this.getContributorCode());
		
		CCLAUtils.addQueryBooleanParamToBinder
		 (binder, Cols.INCOME_DIST_REPORT_IND, this.isIncomeDistReportIndicator());
		CCLAUtils.addQueryBooleanParamToBinder
		 (binder, Cols.INCOME_DIST_REPORT_PAPER_IND, this.isIncomeDistReportPaperIndicator());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.DEPOSIT_NO_OF_STATEMENTS, this.getNumDepositStatements());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.UNITISED_NO_OF_STATEMENTS, this.getNumUnitisedStatements());
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.STATEMENTS_MONTH_1, this.getStatementMonths1());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.STATEMENTS_MONTH_2, this.getStatementMonths2());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.STATEMENTS_MONTH_3, this.getStatementMonths3());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.STATEMENTS_MONTH_4, this.getStatementMonths4());
		
		CCLAUtils.addQueryBooleanParamToBinder
		 (binder, Cols.STATEMENTS_MONTH_ALL, this.isStatementMonthsAll());
		
		CCLAUtils.addQueryBooleanParamToBinder
		 (binder, Cols.BULK_DEPOSIT_STATEMENT_IND, this.isBulkDepositStatementIndicator());
		CCLAUtils.addQueryBooleanParamToBinder
		 (binder, Cols.DEPOSIT_WITHDRAW_BOOKS_IND, this.isDepositWithdrawBooksIndicator());
		CCLAUtils.addQueryBooleanParamToBinder
		 (binder, Cols.AUTO_STATIONARY_ORDERED_IND, this.isAutoStationaryOrderedIndicator());
		CCLAUtils.addQueryBooleanParamToBinder
		 (binder, Cols.QUARTERLY_RPT_CLIENT_IND, this.isQuarterlyReportClientIndicator());
		
		CCLAUtils.addQueryParamToBinder
		 (binder, Cols.QUARTERLY_RPT_MANAGER_INITS, this.getQuarterlyReportManagerInitials());
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.MARKETING_GROUP_ID, this.getMarketingGroupId());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.MARKETING_SUBGROUP_ID, this.getMarketingSubGroupId());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.ACCOUNT_GROUP_ID, this.getAccountGroupId());
		
		CCLAUtils.addQueryParamToBinder
		 (binder, SharedCols.LAST_UPDATED_BY, this.getLastUpdatedBy());
	}

	public void persist(FWFacade facade, String username) throws DataException {

		this.setLastUpdatedBy(username);
		this.validate(facade);
		
		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);
		
		// Add audit record
		DataResultSet beforeData = AuroraClient.getData(this.getMapId(), facade);

		facade.execute("qClientServices_UpdateClientAuroraMap", binder);
		
		// Add audit record
		DataResultSet afterData = AuroraClient.getData(this.getMapId(), facade);
		
		HashMap<Integer, String> auditRelations = 
		 new HashMap<Integer, String>();
		
		auditRelations.put
		 (mapId, ElementType.SecondaryElementType.ClientAuroraMap.toString());
		auditRelations.put
		 (this.getOrganisationId(), ElementType.ORGANISATION.getName());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.UPDATE.toString(), 
		 ElementType.SecondaryElementType.ClientAuroraMap.toString(), 
		 beforeData, afterData, auditRelations);
	}

	public void setAttributes(DataBinder binder) throws DataException {
		
		this.setOrganisationId(CCLAUtils.getBinderIntegerValue
		 (binder, SharedCols.ORG));
		this.setClientNumber(CCLAUtils.getBinderIntegerValue
		 (binder, Cols.CLIENT_NUMBER));
		
		this.setCompany(Company.getCache().getCachedInstance(
		 CCLAUtils.getBinderIntegerValue(binder, Cols.COMPANY)));
		
		this.setContributorCode
		 (CCLAUtils.getBinderIntegerValue(binder, Cols.CONTRIBUTER_CODE));
		this.setContributorTypeCode
		 (CCLAUtils.getBinderIntegerValue(binder, Cols.CONTRIBUTOR_TYPE_CODE));
		this.setSubdivisionCode
		 (CCLAUtils.getBinderIntegerValue(binder, Cols.SUBDIVISION_CODE));
		
		this.setIncomeDistReportIndicator(
		 CCLAUtils.getBinderBoolValue(binder, Cols.INCOME_DIST_REPORT_IND));
		this.setIncomeDistReportPaperIndicator(
		 CCLAUtils.getBinderBoolValue(binder, Cols.INCOME_DIST_REPORT_PAPER_IND));
		
		this.setNumDepositStatements
		 (CCLAUtils.getBinderIntegerValue(binder, Cols.DEPOSIT_NO_OF_STATEMENTS));
		this.setNumUnitisedStatements
		 (CCLAUtils.getBinderIntegerValue(binder, Cols.UNITISED_NO_OF_STATEMENTS));
		
		this.setStatementMonths1
		 (CCLAUtils.getBinderIntegerValue(binder, Cols.STATEMENTS_MONTH_1));
		this.setStatementMonths2
		 (CCLAUtils.getBinderIntegerValue(binder, Cols.STATEMENTS_MONTH_2));
		this.setStatementMonths3
		 (CCLAUtils.getBinderIntegerValue(binder, Cols.STATEMENTS_MONTH_3));
		this.setStatementMonths4
		 (CCLAUtils.getBinderIntegerValue(binder, Cols.STATEMENTS_MONTH_4));
		
		this.setStatementMonthsAll
		 (CCLAUtils.getBinderBoolValue(binder, Cols.STATEMENTS_MONTH_ALL));
				
		this.setBulkDepositStatementIndicator
		 (CCLAUtils.getBinderBoolValue(binder, Cols.BULK_DEPOSIT_STATEMENT_IND));
		this.setDepositWithdrawBooksIndicator
		 (CCLAUtils.getBinderBoolValue(binder, Cols.DEPOSIT_WITHDRAW_BOOKS_IND));
		this.setAutoStationaryOrderedIndicator
		 (CCLAUtils.getBinderBoolValue(binder, Cols.AUTO_STATIONARY_ORDERED_IND));
		
		this.setQuarterlyReportClientIndicator
		 (CCLAUtils.getBinderBoolValue(binder, Cols.QUARTERLY_RPT_CLIENT_IND));
		this.setQuarterlyReportManagerInitials
		 (binder.getLocal(Cols.QUARTERLY_RPT_MANAGER_INITS));
							
		this.setMarketingGroupId
		 (CCLAUtils.getBinderIntegerValue(binder, Cols.MARKETING_GROUP_ID));
		this.setMarketingSubGroupId
		 (CCLAUtils.getBinderIntegerValue(binder, Cols.MARKETING_SUBGROUP_ID));
		this.setAccountGroupId
		 (CCLAUtils.getBinderIntegerValue(binder, Cols.ACCOUNT_GROUP_ID));
	}

	public void validate(FWFacade facade) throws DataException {
		if (!isValidClientNumber(this.clientNumber))
			throw new DataException("Client Number " + this.clientNumber + " is too " +
			 "large");
	}
	
	/** Fetches the total number of UCM content items with the given document class,
	 *  for the Client Number/Company combination specified by the AuroraClient 
	 *  instance.
	 *  
	 * @param client
	 * @param docClass
	 * @return
	 * @throws DataException 
	 */
	public static int getNumClientDocsWithDocumentClass
	 (AuroraClient auroraClient, String docClass, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		
		binder.putLocal(com.ecs.ucm.ccla.Globals.UCMFieldNames.DocClass, docClass);
		
		binder.putLocal(com.ecs.ucm.ccla.Globals.UCMFieldNames.ClientNumber, 
		 auroraClient.getPaddedClientNumber());
		binder.putLocal(com.ecs.ucm.ccla.Globals.UCMFieldNames.Company, 
		 auroraClient.getCompany().getCode());
		
		DataResultSet rsDocCount = facade.createResultSet
		 ("qClientServices_GetNumClientDocsWithType", binder);
		
		return CCLAUtils.getResultSetIntegerValue(rsDocCount, "NUM_DOCS");
	}
	
	/** Fetches Aurora companies associated with the passed Org ID via the Aurora
	 *  Client Mapping table.
	 *  
	 * @param orgId
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getAuroraCompaniesDataByOrgId
	 (int orgId, FWFacade facade) throws DataException {
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, Entity.Cols.ID, orgId);
		
		return facade.createResultSet("qCore_GetAuroraCompaniesByOrgId", binder);
	}

	public Integer getContributorTypeCode() {
		return contributorTypeCode;
	}

	public void setContributorTypeCode(Integer contributorTypeCode) {
		this.contributorTypeCode = contributorTypeCode;
	}

	public Integer getSubdivisionCode() {
		return subdivisionCode;
	}

	public void setSubdivisionCode(Integer subdivisionCode) {
		this.subdivisionCode = subdivisionCode;
	}

	public Integer getContributorCode() {
		return contributorCode;
	}

	public void setContributorCode(Integer contributorCode) {
		this.contributorCode = contributorCode;
	}

	public boolean isIncomeDistReportIndicator() {
		return incomeDistReportIndicator;
	}

	public void setIncomeDistReportIndicator(boolean incomeDistReportIndicator) {
		this.incomeDistReportIndicator = incomeDistReportIndicator;
	}

	public boolean isIncomeDistReportPaperIndicator() {
		return incomeDistReportPaperIndicator;
	}

	public void setIncomeDistReportPaperIndicator(
			boolean incomeDistReportPaperIndicator) {
		this.incomeDistReportPaperIndicator = incomeDistReportPaperIndicator;
	}

	public Integer getNumDepositStatements() {
		return numDepositStatements;
	}

	public void setNumDepositStatements(Integer numDepositStatements) {
		this.numDepositStatements = numDepositStatements;
	}

	public Integer getNumUnitisedStatements() {
		return numUnitisedStatements;
	}

	public void setNumUnitisedStatements(Integer numUnitisedStatements) {
		this.numUnitisedStatements = numUnitisedStatements;
	}

	public Integer getStatementMonths1() {
		return statementMonths1;
	}

	public void setStatementMonths1(Integer statementMonths1) {
		this.statementMonths1 = statementMonths1;
	}

	public Integer getStatementMonths2() {
		return statementMonths2;
	}

	public void setStatementMonths2(Integer statementMonths2) {
		this.statementMonths2 = statementMonths2;
	}

	public Integer getStatementMonths3() {
		return statementMonths3;
	}

	public void setStatementMonths3(Integer statementMonths3) {
		this.statementMonths3 = statementMonths3;
	}

	public Integer getStatementMonths4() {
		return statementMonths4;
	}

	public void setStatementMonths4(Integer statementMonths4) {
		this.statementMonths4 = statementMonths4;
	}

	public boolean isStatementMonthsAll() {
		return statementMonthsAll;
	}

	public void setStatementMonthsAll(boolean statementMonthsAll) {
		this.statementMonthsAll = statementMonthsAll;
	}

	public boolean isBulkDepositStatementIndicator() {
		return bulkDepositStatementIndicator;
	}

	public void setBulkDepositStatementIndicator(
			boolean bulkDepositStatementIndicator) {
		this.bulkDepositStatementIndicator = bulkDepositStatementIndicator;
	}

	public boolean isDepositWithdrawBooksIndicator() {
		return depositWithdrawBooksIndicator;
	}

	public void setDepositWithdrawBooksIndicator(
			boolean depositWithdrawBooksIndicator) {
		this.depositWithdrawBooksIndicator = depositWithdrawBooksIndicator;
	}

	public boolean isAutoStationaryOrderedIndicator() {
		return autoStationaryOrderedIndicator;
	}

	public void setAutoStationaryOrderedIndicator(
			boolean autoStationaryOrderedIndicator) {
		this.autoStationaryOrderedIndicator = autoStationaryOrderedIndicator;
	}

	public boolean isQuarterlyReportClientIndicator() {
		return quarterlyReportClientIndicator;
	}

	public void setQuarterlyReportClientIndicator(
			boolean quarterlyReportClientIndicator) {
		this.quarterlyReportClientIndicator = quarterlyReportClientIndicator;
	}

	public String getQuarterlyReportManagerInitials() {
		return quarterlyReportManagerInitials;
	}

	public void setQuarterlyReportManagerInitials(
			String quarterlyReportManagerInitials) {
		this.quarterlyReportManagerInitials = quarterlyReportManagerInitials;
	}

	public Integer getMarketingGroupId() {
		return marketingGroupId;
	}

	public void setMarketingGroupId(Integer marketingGroupId) {
		this.marketingGroupId = marketingGroupId;
	}

	public Integer getMarketingSubGroupId() {
		return marketingSubGroupId;
	}

	public void setMarketingSubGroupId(Integer marketingSubGroupId) {
		this.marketingSubGroupId = marketingSubGroupId;
	}

	public Integer getAccountGroupId() {
		return accountGroupId;
	}

	public void setAccountGroupId(Integer accountGroupId) {
		this.accountGroupId = accountGroupId;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
}
