package com.ecs.stellent.ccla.clientservices;

import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import com.ecs.stellent.ccla.clientservices.campaign.CommunityFirstClientEnrolmentHandler;
import com.ecs.stellent.ccla.clientservices.form.CommunityFirstDonorSubscriptionFormHandler;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals;
import com.ecs.ucm.ccla.aurora.AuroraAccountHandler;
import com.ecs.ucm.ccla.aurora.AuroraAccountHandler.AccountValidationOutcome;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.AccountFlag;
import com.ecs.ucm.ccla.data.AccountFlagApplied;
import com.ecs.ucm.ccla.data.AuroraClient;
import com.ecs.ucm.ccla.data.Element;
import com.ecs.ucm.ccla.data.ElementAttribute;
import com.ecs.ucm.ccla.data.ElementAttributeApplied;
import com.ecs.ucm.ccla.data.ElementAttributeType;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Fund;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.Relation;
import com.ecs.ucm.ccla.data.RelationPropertyApplied;
import com.ecs.ucm.ccla.data.RelationType;
import com.ecs.ucm.ccla.data.ShareClass;
import com.ecs.ucm.ccla.data.ShareClassGroup;
import com.ecs.ucm.ccla.data.ShareClassOverride;
import com.ecs.ucm.ccla.data.SharedCols;
import com.ecs.ucm.ccla.data.ElementAttribute.SelectionType;
import com.ecs.ucm.ccla.data.SystemConfigVar;
import com.ecs.ucm.ccla.data.instruction.Instruction;
import com.ecs.ucm.ccla.data.subscription.Contribution;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.server.Service;

public class AccountService extends Service {
	
	/** Used to determine the Account data that is fetched.
	 * 
	 * @author Tom
	 *
	 */
	public static class AccountDataConfig {
		private boolean account;
		private boolean mandatedAccount;
		private boolean fund;
		private boolean owningOrg;
		private boolean shareClass;
		private boolean relatedPersons;
		private boolean relatedBankAccounts;
		private boolean flags;
		private boolean attribs;
		private boolean instructions;
		private boolean kyc;
		
		public AccountDataConfig(boolean account, boolean mandatedAccount, boolean fund,
				boolean owningOrg, boolean shareClass,
				boolean relatedPersons, boolean relatedBankAccounts,
				boolean flags, boolean attribs, boolean instructions, boolean kyc) {
			this.account = account;
			this.mandatedAccount = mandatedAccount;
			this.fund = fund;
			this.owningOrg = owningOrg;
			this.shareClass = shareClass;
			this.relatedPersons = relatedPersons;
			this.relatedBankAccounts = relatedBankAccounts;
			this.flags = flags;
			this.attribs = attribs;
			this.instructions = instructions;
			this.kyc = kyc;
		}
		
		/** Create a config instance with all data marked for fetching. */
		public AccountDataConfig() {
			this(true, true, true, true, true, true, true, true, true, true, true);
		}
		
		/** Create a config instance from a comma-separate list of fetch data elements.
		 * 
		 * @param fetchData
		 */
		public AccountDataConfig(String fetchData) {
			String[] fetchElements = fetchData.split(",");
			
			for (String fetchElement : fetchElements) {
				if (fetchElement.equals("account")) {
					this.account = true;
				} else if (fetchElement.equals("mandatedAccount")) {
					this.mandatedAccount = true;
				} else if (fetchElement.equals("fund")) {
					this.fund = true;	
				} else if (fetchElement.equals("owningOrg")) {
					this.owningOrg = true;
				} else if (fetchElement.equals("shareClass")) {
					this.shareClass = true;
				} else if (fetchElement.equals("relatedPersons")) {
					this.relatedPersons = true;
				} else if (fetchElement.equals("relatedBankAccounts")) {
					this.relatedBankAccounts = true;
				} else if (fetchElement.equals("flags")) {
					this.flags = true;
				} else if (fetchElement.equals("attribs")) {
					this.attribs = true;
				} else if (fetchElement.equals("instructions")) {
					this.instructions = true;
				} else if (fetchElement.equals("kyc")) {
					this.kyc = true;
				}
			}
		}

		public boolean isAccount() {
			return account;
		}

		public boolean isOwningOrg() {
			return owningOrg;
		}
		
		public boolean isShareClass() {
			return shareClass;
		}

		public boolean isRelatedPersons() {
			return relatedPersons;
		}

		public boolean isRelatedBankAccounts() {
			return relatedBankAccounts;
		}
		
		public boolean isFlags() {
			return flags;
		}

		public boolean isAttribs() {
			return attribs;
		}

		public boolean isInstructions() {
			return instructions;
		}

		public boolean isKyc() {
			return kyc;
		}

		public boolean isMandatedAccount() {
			return mandatedAccount;
		}

		public boolean isFund() {
			return fund;
		}
	}
	
	/** Service-accessible method, designed to fetch customized data for the Account
	 *  specified in the binder.
	 *  
	 *  The 'fetchElements' binder parameter is expected to contain a comma-separated
	 *  list of data elements to be fetched. See the AccountDataConfig inner class for
	 *  more details.
	 *  
	 *  If 'fetchElements' is emtpy/missing, all data elements are fetched.
	 *  
	 * @throws DataException 
	 * @throws ServiceException 
	 *  
	 */
	public void getAccountData() throws DataException, ServiceException {
		
		String fetchElements = m_binder.getLocal("fetchElements");
		AccountDataConfig dataConfig = null;
		
		if (StringUtils.stringIsBlank(fetchElements)) {
			dataConfig = new AccountDataConfig();
		} else {
			dataConfig = new AccountDataConfig(fetchElements);
		}
		
		boolean debug = !StringUtils.stringIsBlank(m_binder.getLocal("IsDebug"));
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		addAccountDataToBinder(m_binder, facade, dataConfig, debug);
	}
	
	/** Adds a custom selection of Account data to the binder, as specified by the 
	 *  passed dataConfig instance.
	 *  
	 *  The required binder parameters for identifying the Account can be found in the
	 *  getAccountData method description.
	 *  
	 * @param binder
	 * @param facade
	 * @param dataConfig
	 * @throws DataException
	 * @throws ServiceException
	 */
	public static void addAccountDataToBinder
	 (DataBinder binder, FWFacade facade, AccountDataConfig dataConfig, boolean debug) 
	 throws DataException, ServiceException {
		
		long startTime = System.currentTimeMillis();
		
		DataResultSet rsAccount = getAccountData(binder, true, facade);
		Account account = Account.get(rsAccount);
		
		if (debug)
			debugTiming(startTime, "addAccountDataToBinder - Account fetch");
		
		// Load in static Relation/Property lists if any relation data is required
		if (dataConfig.isRelatedPersons() || dataConfig.isRelatedBankAccounts()) {
			
			binder.addResultSet("rsElementRelationTypeNames", facade.createResultSet
			 ("qClientServices_GetElementRelationNames", binder));
			
			binder.addResultSet("rsRelationProperties", facade.createResultSet
			 ("qClientServices_GetRelationProperties", binder));
		}
		
		if (dataConfig.isAccount()) {
			// Add basic account data as ResultSet
			binder.addResultSet("rsAccount", Account.formatResultSet(rsAccount));
		}
		
		if (dataConfig.isMandatedAccount()) {
			// If the account has a 'mandated account' set, attempt to resolve
			// the string version of the account number.
			Integer mandatedAccId = account.getMandatedAccId();

			if (mandatedAccId != null) {
				Account mandatedAccount = Account.get(mandatedAccId, facade);
				
				if (mandatedAccount == null)
					throw new ServiceException
					 ("Unable to find mandated account with ID: " 
					  + mandatedAccId);
				
				String mandatedAccNumStr = mandatedAccount.getAccNumExt();
				binder.putLocal("MANDATED_ACCOUNT_ID_DISPLAY", mandatedAccNumStr);
			}
		}
		
		if (dataConfig.isRelatedPersons()) {
			long taskStartTime = System.currentTimeMillis();
			
			// Fetch related Persons data
			long queryStartTime = taskStartTime;
			
			DataResultSet rsRelatedPersons = 
			Relation.getRelatedPersonsData
			  (account.getAccountId(), ElementType.ACCOUNT, facade);
			
			binder.addResultSet("rsRelatedPersons", rsRelatedPersons);
			
			if (debug)
				debugTiming(queryStartTime,
				 "addAccountDataToBinder - rsRelatedPersons");
			
			// Fetch related Person data
			queryStartTime = System.currentTimeMillis();
			
			RelationType personAccountRelType = 
			 RelationType.getCache().getCachedInstance(RelationType.PERSON_ACCOUNT);
			
			DataResultSet rsPersonRelations =
			 Relation.getRelationData(null, account.getAccountId(), 
			 personAccountRelType, null, facade);
			
			binder.addResultSet("rsPersonRelations", rsPersonRelations);
			
			if (debug)
				debugTiming(queryStartTime,
				 "addAccountDataToBinder - rsPersonRelations");
			
			queryStartTime = System.currentTimeMillis();
			
			// Add Relation Map (assists in UI rendering)
			DataResultSet rsPersonRelationMap = 
			 Relation.getRelationMap(ElementType.PERSON, null, account.getAccountId(), 
			 personAccountRelType, null, facade);
			
			binder.addResultSet("rsPersonRelationMap", rsPersonRelationMap);
			
			// Add applied properties
			DataResultSet rsPersonRelationProperties =
			 RelationPropertyApplied.getByRelationsData(null, 
			 account.getAccountId(), personAccountRelType, null, facade);
			
			binder.addResultSet
			 ("rsPersonRelationProperties", rsPersonRelationProperties);
			
			if (debug)
				debugTiming(queryStartTime,
				 "addAccountDataToBinder - rsPersonRelationMap");
			
			if (debug)
				debugTiming(taskStartTime,
				 "addAccountDataToBinder - Related Persons fetch");
		}
		
		if (dataConfig.isRelatedBankAccounts()) {
			long taskStartTime = System.currentTimeMillis();
			
			// Fetch related Bank Account data
			DataResultSet rsRelatedBankAccounts = Relation.getRelatedBankAccountsData
			 (account.getAccountId(), facade);
			
			binder.addResultSet
			 ("rsRelatedBankAccounts", rsRelatedBankAccounts);
			
			RelationType accountBankAccountRelType = 
			 RelationType.getCache().getCachedInstance(RelationType.ACCOUNT_BANKACCOUNT);
			
			DataResultSet rsBankAccountRelations = Relation.getRelationData
			 (account.getAccountId(), null, accountBankAccountRelType, null, facade);
			
			binder.addResultSet
			 ("rsBankAccountRelations", rsBankAccountRelations);
			
			// Add Relation Map (assists in UI rendering)
			DataResultSet rsBankAccountRelationMap = 
			 Relation.getRelationMap(ElementType.BANK_ACCOUNT, 
			 account.getAccountId(), null, accountBankAccountRelType, null, facade);
			
			binder.addResultSet("rsBankAccountRelationMap", rsBankAccountRelationMap);
			
			DataResultSet rsBankAccountRelationProperties = 
			 RelationPropertyApplied.getByRelationsData(account.getAccountId(), 
			 null, accountBankAccountRelType, null, facade);
			
			binder.addResultSet
			 ("rsBankAccountRelationProperties", rsBankAccountRelationProperties);
			
			if (debug)
				debugTiming(taskStartTime,
				 "addAccountDataToBinder - Related Bank Accounts fetch");
		}
		
		if (dataConfig.isShareClass()) {
			long taskStartTime = System.currentTimeMillis();
			
			addShareClassDataToBinder(account, binder, facade);
			
			if (debug)
				debugTiming(taskStartTime,
				 "addAccountDataToBinder - Share Class fetch");
		}
			
		if (dataConfig.isFlags()) {
			// Get the static list of account flags
			binder.addResultSet("rsAccountFlags", AccountFlag.getAll(facade));
			
			// Get the applied account flags
			DataResultSet rsAccountFlagApplied =
			 facade.createResultSet("qClientServices_GetAccountFlagApplied", binder);
			binder.addResultSet("rsAccountFlagApplied", rsAccountFlagApplied);
		}
		
		if (dataConfig.isInstructions()) {
			DataResultSet rsInstructions = Instruction.getExtendedDataByAccount
			 (account.getAccountId(), Globals.INSTRUCTIONS_MAX_RESULTS, facade);
			binder.addResultSet("rsInstructions", rsInstructions);
		}
		
		if (dataConfig.isFund()) {
			// Add Fund data
			DataResultSet rsFund = Fund.getData(account.getFundCode(), facade);
			binder.addResultSet("rsFund", rsFund);
		}
		
		Entity org = null;
		
		if (dataConfig.isOwningOrg()) {
			long taskStartTime = System.currentTimeMillis();
	
			// Fetch Entity/Client data
			
			long queryStartTime = taskStartTime;
			
			DataResultSet rsEntity = Entity.getData
			 (account.getOwnerOrganisationId(facade), facade);
			org = Entity.get(rsEntity);
			
			binder.addResultSet("rsEntity", rsEntity);

			if (debug)
				debugTiming(queryStartTime, "addAccountDataToBinder - rsEntity");
			
			queryStartTime = System.currentTimeMillis();
			
			// Fetch Aurora Client mapping
			DataResultSet rsAuroraClientMap = 
			 Entity.getAuroraClientMappingData(org.getOrganisationId(), facade);
			
			binder.addResultSet("rsAuroraClientMap", rsAuroraClientMap);
			
			if (debug)
				debugTiming(queryStartTime, "addAccountDataToBinder - rsAuroraClientMap");
			
			if (debug)
				debugTiming(taskStartTime, "addAccountDataToBinder - Org fetch");
		}
		
		if (dataConfig.isKyc()) {
			long taskStartTime = System.currentTimeMillis();
			
			// Add IVS check status data
			if (org == null) {
				// Must fetch Org now, if it wasn't fetched previously.
				DataResultSet rsEntity = Entity.getData
				 (account.getOwnerOrganisationId(facade), facade);
				org = Entity.get(rsEntity);
			}
			
			Account.IVSCheck ivsCheck = account.getIVSCheck(org, facade);
			
			ivsCheck.addToBinder(binder);
			
			if (debug)
				debugTiming(taskStartTime, "addAccountDataToBinder - IVS fetch");
		}
		
		if (dataConfig.isAttribs()) {
			long taskStartTime = System.currentTimeMillis();
			
			// Add element attributes associated with accounts.
			DataResultSet rsElementAttributes = ElementAttribute.getElementAttributesData
			 (ElementType.ACCOUNT, ElementAttribute.SelectionType.ALL, facade);
			
			binder.addResultSet("rsElementAttributes", rsElementAttributes);
			
			// Add applied element attributes for this account.
			DataResultSet rsElementAttributesApplied = ElementAttributeApplied.getAllData
			 (account.getAccountId(), null, false, facade);
			
			binder.addResultSet
			 ("rsElementAttributesApplied", rsElementAttributesApplied);
			
			if (debug)
				debugTiming(taskStartTime, "addAccountDataToBinder - Attribs fetch");
		}
		
		if (debug)
			debugTiming(startTime, "addAccountDataToBinder");
	}
	
	private static void debugTiming(long startTime, String description) {
		double elapsed = ((double)(System.currentTimeMillis() - startTime) / 1000D);
		String elapsedStr = CCLAUtils.DECIMAL_FORMAT.format(elapsed);
		
		Log.debug("Time taken: " + elapsedStr + "s, Task: " + description);
	}
	
	/** Checks if the passed Account has a mapped share class.
	 *  
	 *  If so, adds various ResultSets to the passed DataBinder, including a list of
	 *  available share classes for the Account's fund.
	 *  
	 * @param account
	 * @param binder
	 * @param facade
	 * @throws DataException
	 */
	private static void addShareClassDataToBinder
	 (Account account, DataBinder binder, FWFacade facade) throws DataException {
		
		// Does account found include share classes?
		String fundCode = account.getFundCode();
		DataResultSet rsSC = ShareClass.getEnabledShareClassesByFund(fundCode, facade);
		
		if (rsSC != null && !rsSC.isEmpty()) {
			BinderUtils.addParamToBinder(binder, "hasShareClass", "1");
			// get share class details
			Integer shareClassId = Account.getTrueShareClass
			 (account.getAccountId(), facade);
			
			if (shareClassId!=null)
				binder.addResultSet
				 ("rsShareClass", ShareClass.getData(shareClassId, facade));
			
			if (shareClassId != null) {
				ShareClassGroup group = ShareClassGroup.getByAccountId
				 (account.getAccountId(), facade);
				
				if (group!=null)
					binder.addResultSet("rsShareClassGroup", 
					 ShareClassGroup.getData(group.getGroupId(), facade));
				
				ShareClassOverride sco = ShareClassOverride.get
				 (account.getAccountId(), facade);
				
				if (sco != null) {
					binder.addResultSet("rsShareClassOverride", 
					 ShareClass.getData(sco.getShareClassId(), facade));
				}
			}
		}
	}
		
	/** Used by SPP to determine AML status of individual accounts.
	 *  
	 *  Places the following values in the binder:
	 *  -IVSStatusCode: 	Indicates whether IVS checks are passed, failed or unknown 
	 *  					on this account.
	 *  -IVSFailReason:		Detailed message indicating reason for IVS failure, if
	 *  					applicable.
	 *  -EntityCheckStatusCode:  Indicates whether Entity checks are passed/failed/
	 *  					unknown for the account owner.
	 *  -FailedEntityVerificationSources: If the above has a 'failed' status, this
	 *  					will be a comma-separated list of failed verification 
	 *  					sources for the Entity.
	 *  -RelationCheckStatusCode:	Indicates whether Authorizing Person relations 
	 *  					between the Entity and associated persons have been 
	 *  					verified.
	 *  -PutOnWatchList:	Indicates whether the account has an entry in the 
	 *  					ACCOUNT_FLAG_APPLIED table, which is marked as a WatchList
	 *  					Reason.
	 *  -WatchReason:		If PutOnWatchList is true, this will list all the applied
	 *  					Flag Reasons from REF_ACCOUNT_FLAG which are WatchList 
	 *  					Reasons.
	 *  -GenerateCSLetter:	Indicates whether the account flag ID 10 'Generate CS 
	 *  					letter' was enabled on this account.
	 *  -AMLTrackerOverride: If the client has the legacy AML Tracker verification
	 *  					attribute, and the account does NOT have the 'mandate
	 *  					received since 16th Oct 2010' attribute, this flag will be
	 *  					true, false otherwise.
	 *  
	 *  If the passed accountNumber is null/empty, but clientNumber and fundCode
	 *  are non-null, the system returns a special hard-coded IVS check indicating
	 *  that no account number was passed.
	 *  
	 *  
	 * @throws ServiceException 
	 * @throws DataException 
	 */
	public void getAccountAMLStatus() throws ServiceException, DataException {
		
		// Fetch external account number component values from binder
		String clientNumStr 		= m_binder.getLocal("clientNumber");
		String accountNumStr		= m_binder.getLocal("accountNumber");
		String fundCode				= m_binder.getLocal("fundCode");
		
		// Ensure required account parameters are non-null
		if (StringUtils.stringIsBlank(clientNumStr)
			||
			StringUtils.stringIsBlank(fundCode)) {
			throw new ServiceException("Account not fully specified: " +
			 "clientNumber=" + clientNumStr + ", accountNumber=" + 
			  accountNumStr + ", fundCode=" + fundCode);
		}
		
		FWFacade facade				= CCLAUtils.getFacade(m_workspace, true);
		Account.IVSCheck ivsCheck 	= null;
		
		Entity.EntityCheck entityCheck = null;

		boolean addToWatchList = false;
		String watchListReasons = "";
		
		boolean generateCSLetter = false;
		
		boolean amlTrackerOverride = false;
		
		if (StringUtils.stringIsBlank(accountNumStr)) {
			// Only the account number was missing. Return hard-coded IVS 
			// response indicating failure due to lack of account number.
			
			ivsCheck = Account.getEmptyIVSCheck();
			ivsCheck.failReason =  "No account number specified.";
			ivsCheck.statusCode = Account.IVSStatusCode.FAILED;
		
			// Attempt to resolve Organisation from client number/fund.
			Fund fund = Fund.getCache().getCachedInstance(fundCode);
			
			if (fund == null)
				throw new ServiceException("Invalid fund passed: " + fundCode);

			Entity entity = Entity.getEntityFromAuroraValues
			 (Integer.parseInt(clientNumStr), fund.getCompany().getCode(), facade);
			
			if (entity != null) {
				Log.debug("Resolved Organisation ID " + 
				 entity.getOrgAccountCode() + " from Client Number: " + 
				 clientNumStr + ", company: " + fund.getCompany());
				
				entityCheck = 
				 Entity.getEntityCheck(entity.getOrganisationId(), facade);
			}

		} else {
			// Account number present, run proper KYC check on the account.
			int clientNumber, accountNumber;
			
			try {
				clientNumber 		= Integer.parseInt(clientNumStr);
				accountNumber 		= Integer.parseInt(accountNumStr);
				
			} catch (NumberFormatException e) {
				String msg = "Passed values for clientNumber " + 
				 "and/or accountNumber were not numeric (" + clientNumStr + 
				 ", " + accountNumStr + ")";
				
				Log.error(msg);
				throw new ServiceException(msg);
			}
			
			String accountParams = "client no: " + clientNumber + 
			 ", acc no: " + accountNumber + ", fund code: " + fundCode;
			Log.debug("Fetching account (" + accountParams + ")");
			
			DataResultSet rsAccount  = Account.getAccountByAuroraValues
			 (clientNumber, accountNumber, fundCode, facade);
			
			Account account = Account.get(rsAccount);
			
			if (account == null) {
				String msg = "Unable to find corresponding account " +
				 "(" + accountParams + ")";
				
				Log.error(msg);
				throw new ServiceException(msg);
			}
			
			Log.debug("Found corresponding account: " + account.getAccNumExt());
			
			// Now determine the Entity check status for the account owner.
			Integer orgId = account.getOwnerOrganisationId(facade);
			Entity org = Entity.get(orgId, facade);
			
			// Determine account IVS status
			ivsCheck = account.getIVSCheck(org, facade);
		
			// Determine Entity Check status
			entityCheck = Entity.getEntityCheck(org.getOrganisationId(), facade);
			
			int accountId = account.getAccountId();
			
			// Fetch any applied account flags for this account.
			Vector<AccountFlag> accountFlags = 
			 AccountFlagApplied.get(accountId, facade);
			
			// Search for WatchList flag and Generate CS Letter flags.
			for (AccountFlag accountFlag : accountFlags) {
				if (accountFlag.isAddToWatchList()) {
					addToWatchList = true;
					
					if (watchListReasons.length() > 0)
						watchListReasons += ",";
					
					watchListReasons += accountFlag.getName();
				}
				
				if (accountFlag.getAccountFlagId() 
					== AccountFlag.Ids.GENERATE_CS_LETTER)
					generateCSLetter = true;
			}
			
			Log.debug("PutOnWatchList:" + addToWatchList);
			Log.debug("WatchReason:" + watchListReasons);
			Log.debug("GenerateCSLetter:" + generateCSLetter);
			
			// New AML Tracker Override flag.
			
			// Fetch the Legacy AML Tracker verification attribute from the client, if 
			// one exists.
			ElementAttributeApplied verifiedByAMLTracker = ElementAttributeApplied.get(
			 orgId, 
			 ElementAttribute.OrganisationAttributes.VERIFIED_BY_AML_TRACKER, 
			 facade
			);

			if (verifiedByAMLTracker != null && verifiedByAMLTracker.getStatus()) {
				// Client was verified on old tracker. Check for 'No mandate received
				// since Oct 2010' attribute against the account.
				
				ElementAttributeApplied noMandateReceived = 
				 ElementAttributeApplied.get(
				  accountId, 
				  ElementAttribute.AccountAttributes.NO_MANDATE_RECEIVED_SINCE_OCT_2010, 
				  facade
				 );
				
				if (noMandateReceived != null
					&& noMandateReceived.getStatus() != null 
					&& noMandateReceived.getStatus()) {
					Log.debug("Client was part of old AML tracker, and account has  " +
					 "not had a mandate since Oct 16th 2010. Passing AML Tracker " +
					 "Override flag");
					// Account has not had a mandate received since Oct 2010.
					amlTrackerOverride = true;
				}
			}
		}	
	
		Log.debug("Resolved Account AML status for Client Number: " + 
		 Integer.parseInt(clientNumStr) + ", Account Number: " + 
		 accountNumStr + ", Fund: " + fundCode);
		
		Log.debug("Adding IVSStatusCode: " + ivsCheck.statusCode.toString());
		m_binder.putLocal("IVSStatusCode", ivsCheck.statusCode.toString());
		
		if (ivsCheck.failReason != null) {
			Log.debug("Adding IVSFailReason: "+ ivsCheck.failReason);
			m_binder.putLocal("IVSFailReason", ivsCheck.failReason);
		}
		
		Log.debug("Adding Entity Check outcome: " + entityCheck.toString());
		entityCheck.addToBinder(m_binder);
		
		//TODO Remove or improve this as necessary in future
		m_binder.putLocal("PutOnWatchList", addToWatchList ? "1" : "0");
		m_binder.putLocal("WatchReason", watchListReasons);
		m_binder.putLocal("GenerateCSLetter", generateCSLetter ? "1" : "0");
		
		m_binder.putLocal("AMLTrackerOverride", amlTrackerOverride ? "1" : "0");
	}
	
	public static enum AccountFilter {
		ALL,
		FUND,
		FUND_TYPE
	}
	
	/** Fetches accounts owned by a particular Organisation.
	 *  
	 *  If a filterType parameter is present in the binder, this will dictate the
	 *  fetch query used to pull back the accounts. The available filterTypes are:
	 *  
	 *  ALL: 		fetches all accounts
	 *  FUND:		fetches accounts with a Fund Code matching the FUND_CODE parameter 
	 *  			in the binder
	 *  FUND_TYPE:	fetches accounts with a Fund Code and Account Type matching the 
	 *  			FUND_CODE parameter and ACCOUNT_TYPE_ID parameters in the binder
	 *  
	 *  See the enum AccountFilter.
	 *  
	 * @throws ServiceException
	 * @throws DataException
	 */
	public void getClientAccounts() throws ServiceException, DataException {
		
		String entityIdStr = m_binder.getLocal("ORGANISATION_ID");
		
		if (StringUtils.stringIsBlank(entityIdStr)) {
			throw new ServiceException("Unable to load Client accounts: " +
			 "no ORGANISATION_ID found.");
		}
		
		// Determine filter type.
		String filterTypeStr = m_binder.getLocal("filterType");
		AccountFilter filter = AccountFilter.ALL;
		
		if (!StringUtils.stringIsBlank(filterTypeStr))
			filter = AccountFilter.valueOf(filterTypeStr);
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		int entityId	= Integer.parseInt(entityIdStr);
		
		DataResultSet rsAccounts = null;
		
		if (filter == AccountFilter.ALL)
			rsAccounts = Account.getClientAccountsData(entityId, facade);
		else if (filter == AccountFilter.FUND) {
			String fundCode = m_binder.getLocal(Account.Cols.FUND_CODE);
			
			if (StringUtils.stringIsBlank(fundCode))
				throw new ServiceException("Unable to load Client accounts: Fund Code "+
				 "filter is missing");
			
			rsAccounts = Account.getClientAccountsDataByFund
			 (entityId, fundCode, facade);
		} else if (filter == AccountFilter.FUND_TYPE) {
			String fundCode = m_binder.getLocal(Account.Cols.FUND_CODE);
			Integer accountTypeId = CCLAUtils.getBinderIntegerValue
			 (m_binder, Account.Cols.ACCOUNT_TYPE);
			
			if (StringUtils.stringIsBlank(fundCode)
				||
				accountTypeId == null)
				throw new ServiceException("Unable to load Client accounts: Fund Code "+
				 "filter or Account Type filter is missing");
			
			rsAccounts = Account.getClientAccountsDataByFundType
			 (entityId, fundCode, accountTypeId, facade);
		}
			
		m_binder.addResultSet("rsAccounts", Account.formatResultSet(rsAccounts));
		
		// Add totals by account type. Currently this will include all accounts
		// and does not take into account the above filter options.
		DataResultSet rsAccountTotals = Account.getClientAccountTotalsData
		 (entityId, facade);
		
		m_binder.addResultSet("rsAccountTotals", rsAccountTotals);
	}
	
	/** Fetches accounts for a given Organisation, with the given Status ID and Fund
	 *  Code.
	 * @throws ServiceException 
	 * @throws DataException 
	 *  
	 */
	public void getClientAccountsByStatusAndFund() 
	 throws ServiceException, DataException {
		
		String entityIdStr 	= m_binder.getLocal("ORGANISATION_ID");
		String fundCode		= m_binder.getLocal("FUND_CODE");
		String statusId		= m_binder.getLocal("ACCOUNT_STATUS_ID");
		
		if (StringUtils.stringIsBlank(entityIdStr)) {
			throw new ServiceException("Unable to load Client accounts: " +
			 "no ORGANISATION_ID found.");
		}
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		int entityId	= Integer.parseInt(entityIdStr);
		
		throw new ServiceException("Not implemented yet!");
		
		/*
		DataResultSet rsAccounts = 
		 Account.getClientAccountsDataByStatusAndFund(entityId, facade);
		
		m_binder.addResultSet("rsAccounts", Account.formatResultSet(rsAccounts));
		*/
	}

	/** 
	 * updates the default bank accounts, adding and/or removing rows
	 * in the relation_property_applied table, depending on what
	 * is passed into binder
	 *  
	 * @return 
	 * 
	 * @throws ServiceException, DataException 
	 */		
	
	public void updateDefaultBankAccounts() throws ServiceException, DataException
	{
		throw new ServiceException("This service is closed for maintenance. " +
		 "Please come back soon.");
	}
	
	/** Used to set or remove the 'AML Check Override User' for a given 
	 *  account. If this is non-null, the account will always pass IVS 
	 *  checking.
	 *  
	 * @throws ServiceException 
	 * @throws DataException 
	 *  
	 */
	public void setAmlCheckOverrideUser() 
	 throws ServiceException, DataException {
		
		String accountIdStr = m_binder.getLocal("ACCOUNT_ID");
		
		if (StringUtils.stringIsBlank(accountIdStr)) {
			throw new ServiceException("Unable to set AML Check override " +
			 "user: Account ID missing"); 
		}
		
		int accountId		= Integer.parseInt(accountIdStr);
		String overrideUser = m_binder.getLocal("OVERRIDE_USER");
		
		FWFacade facade 	= CCLAUtils.getFacade(m_workspace, true);
		Account account		= Account.get(accountId, facade);
		
		Log.debug("Setting override user to " + overrideUser + 
		 " for account: " + account.getAccNumExt());
		
		account.setAmlCheckOverrideUser(overrideUser);
		account.persist(facade, m_userData.m_name);
	}
	
	/** Adds a new Account. Expects all fields to be present, including
	 *  a mapped Correspondent relation.
	 *  
	 * @throws ServiceException
	 * @throws DataException
	 */
	public void addAccount() throws ServiceException, DataException {
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		try {
			facade.beginTransaction();
			
			String username = m_userData.m_name;
			String strOrgID = m_binder.getLocal(Entity.Cols.ID);
			
			if (StringUtils.stringIsBlank(strOrgID))
				throw new ServiceException
				 ("Unable to create account: Missing Organisation ID");
			
			int orgID = Integer.parseInt(strOrgID);
			Entity org = Entity.get(orgID, facade);
			
			String orgName = org.getOrganisationName();
			m_binder.putLocal(Entity.Cols.ORGANISATION_NAME, orgName);
			
			String fundStr = m_binder.getLocal(SharedCols.FUND);
			
			if (StringUtils.stringIsBlank(fundStr))
				throw new ServiceException
				("Unable to create account: Missing Fund");
		
			Fund fund = Fund.getCache().getCachedInstance(fundStr);
			
			if (fund==null)
				throw new ServiceException
				("Unable to create account: Cannot find Fund with Fund Code:"+fundStr);
			
			// Check for existing Client Number mapping for the account's company
			AuroraClient auroraClient = 
			 Entity.getAuroraClientCompanyMapping
			(org.getOrganisationId(), fund.getCompany(), facade);
				
			if (auroraClient == null) {
				// No assigned Aurora Client Number for this Company yet. 
				
				throw new ServiceException("No Aurora Client entry for Company " +
				 fund.getCompany().getCode() + ". Ensure one has been added before " +
				 "creating an account belonging to this Company.");
			}
			
			// Check for a Donor ID. This must be linked to the new Account as an applied
			// Element Attribute later on.
			Integer donorId = CCLAUtils.getBinderIntegerValue(m_binder, "DONOR_ID");
			
			Account account = null;
			
			if (donorId != null) {
				// Must create a Comm First Donor Account instead.
				Element donorElem = Element.get(donorId, facade);
				
				// Resolve the donor element ID to a Person/Organisation instance.
				if (donorElem.getType().equals(ElementType.PERSON))
					donorElem = Person.get(donorId, facade);
				else
					donorElem = Entity.get(donorId, facade);
				
				account = SubscriptionUtils.createDonorAccount
				 (donorElem, org, fund, true, m_userData.m_name, facade);
				
			} else {
				// Create a standard account.
				account	= Account.add(m_binder, facade, m_userData.m_name);
			}
			
			// Update entries in ACCOUNT_FLAG_APPLIED
			updateAuroraFlags(account.getAccountId(), facade, m_binder, username);
			
			m_binder.putLocal("RedirectUrl", 
			 m_binder.getLocal("RedirectUrl") + account.getAccountId());
			
			facade.commitTransaction();
		} catch (Exception e) {
			facade.rollbackTransaction();
			String msg = "Failed to add account: " + e.getMessage();
			
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}
	
	/**
	 * This method will create a PI account for all orphaned PC fund accounts
	 * 
	 * @throws DataException 
	 * @throws ServiceException 
	 */
	public void createCompanionAccountsForOphanedPcAccounts() 
	 throws DataException, ServiceException {
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);

		DataBinder binder = new DataBinder();
		
		//Fetch ResultSet of orphaned accounts
		DataResultSet rsOrphanedPcAccounts = facade.createResultSet
		 ("qClientServices_GetOrphanedPcAccounts", binder);
		
		//If there are accounts returned create PI accounts for each one.
		if (!rsOrphanedPcAccounts.isEmpty()){
			try {
				facade.beginTransaction();
				
				do {
					Integer accountId = CCLAUtils.getResultSetIntegerValue
					 (rsOrphanedPcAccounts, "ACCOUNT_ID");
					
					Account.createCompanionAccount(accountId, Fund.FundCodes.PI.toString(), 
					 m_userData.m_name, facade);
					
				}  while (rsOrphanedPcAccounts.next());
				
				facade.commitTransaction();
				
			} catch (ServiceException e) {
				facade.rollbackTransaction();
				Log.info("Unable to create companion PI accounts",e);
				throw new ServiceException("Unable to create companion PI accounts: "+
						e.getMessage(),e);
			}	
		}
	}
	
	
	/** Fetches extended account data.
	 *  
	 *  This includes data from the CCLA_ACCOUNT table, Entity data
	 *  for all related Entities, Person data for all related persons, 
	 *  and bank account data/relationships.
	 *  
	 *  Also executes the AML check logic to determine whether the account
	 *  is AML compliant, and a fail reason if necessary.
	 *  
	 * @throws ServiceException 
	 * @throws DataException 
	 */
	public void getAccount() throws ServiceException, DataException {
		
		FWFacade facade 	= CCLAUtils.getFacade(m_workspace, true);
		AccountDataConfig config = new AccountDataConfig
		 (true, true, true, true, true, true, true, true, true, false, true);
		
		addAccountDataToBinder(m_binder, facade, config, false);
	}
	
	/** Updates an existing account.
	 *  
	 *  This update method works slightly differently in comparison to others.
	 *  It will only permit updates to a small subset of fields taken from 
	 *  the DataBinder (as defined by the env. variable: 
	 *   
	 *   CCLA_CS_UpdatableAccountFields 
	 *   
	 *  Any fields from this list are copied into a fresh binder before being
	 *  applied to the Account instance.
	 *  
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void updateAccount() throws DataException, ServiceException {
		
		String accountIdStr = m_binder.getLocal("ACCOUNT_ID");
		
		if (StringUtils.stringIsBlank(accountIdStr))
			throw new ServiceException("Unable to update account, ACCOUNT_ID " +
			 "missing");
		
		int accountId 	= Integer.parseInt(accountIdStr);
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		Account account = Account.get(accountId, facade);
		
		if (account == null)
			throw new ServiceException("Unable to update account, no account " +
			 "found with ID: " + accountId);
		
		account.setAttributes(m_binder);
		// user needed for audit table
		String username = m_userData.m_name;
		m_binder.putLocal("USER_ID", username);
		
		try {
			facade.beginTransaction();
			
			account.persist(facade, username);
			
			// Update applied account flags
			updateAuroraFlags(account.getAccountId(), facade, m_binder, username);
			
			// Update applied element attributes. 
			ElementAttributeApplied.updateElementAttributesApplied
			 (account.getAccountId(), ElementType.ACCOUNT, 
			 SelectionType.NON_VERIFY_ONLY, 
			 ElementAttributeType.getCache().getCachedInstance(
			  ElementAttributeType.MISC_ACCOUNT_DETAILS
			 ), 
			 m_binder, facade,  m_userData.m_name);		
			
			facade.commitTransaction();
			
		} catch (Exception e) {
			facade.rollbackTransaction();
			
			String msg = "Failed to update account: " + e.getMessage();
			
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
		
		Log.debug("Updated account ID: " + account.getAccountId());
	}
	
	/** Bulk-updates certain data fields on a passed group of Accounts.
	 * 
	 * @throws ServiceException 
	 * @throws DataException 
	 * 
	 */
	public void updateAccounts() throws ServiceException, DataException {
		
		String accountIdsStr = m_binder.getLocal("accountIds");
		
		if (StringUtils.stringIsBlank(accountIdsStr)) {
			throw new ServiceException("Unable to update accounts data, " +
			 "no accounts specified");
		}
		
		Vector<Integer> accountIds = CCLAUtils.getIntegerList(accountIdsStr);
		Log.debug("Batch-updating details for " + accountIds.size() + " accounts");
		
		// Fetch any data field values which will be applied to all selected accounts.
		
		// Just required sigs for now.
		Integer requiredSigs = CCLAUtils.getBinderIntegerValue
		 (m_binder, Account.Cols.REQ_SIGNATURES);
		
		boolean valuesForUpdate = false;
		
		if (requiredSigs != null) {
			Log.debug("Found a Required Sigs value to batch update: " + requiredSigs);
			valuesForUpdate = true;
		}
		
		if (!valuesForUpdate)
			throw new ServiceException("Unable to update accounts data, no fields " +
			 "set for batch update");
		
		FWFacade facade =  CCLAUtils.getFacade(m_workspace, true);
		
		// Update each account with selected values.
		try {
			facade.beginTransaction();
			
			for (int accountId : accountIds) {
				Account account = Account.get(accountId, facade);
				
				Log.debug("Updating data on account " + account.getAccountId());
				
				if (requiredSigs != null)
					account.setRequiredSignatures(requiredSigs);
				
				account.persist(facade, m_userData.m_name);
			}
			
			facade.commitTransaction();
		} catch (Exception e) {
			facade.rollbackTransaction();
			
			String msg = "Failed to batch-update accounts data: " + e.getMessage();
			
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
		
		Log.debug("Account batch-update completed");
	}
	
	/** Fetches account data from the given Account ID/Ext. Account Number in the
	 *  DataBinder (whichever one is found first).
	 *  
	 */
	public void getAccountBasic() throws DataException, ServiceException {
		
		FWFacade facade 		= CCLAUtils.getFacade(m_workspace, true);

		AccountDataConfig basicDataConfig = new AccountDataConfig
		 (true, true, false, false, true, true, true, true, false, false, false);
		
		addAccountDataToBinder(m_binder, facade, basicDataConfig, false);
	}
	
	/** Fetches account data from the given Account ID/Ext. Account Number in the
	 *  DataBinder (whichever one is found first).
	 *  
	 */
	public void getAccountOnlyBasic() throws DataException, ServiceException {
		
		FWFacade facade 		= CCLAUtils.getFacade(m_workspace, true);
		
		AccountDataConfig basicDataConfig = new AccountDataConfig
		 (true, true, false, false, false, false, false, false, false, false, false);
		
		addAccountDataToBinder(m_binder, facade, basicDataConfig, false);
	}	
	
	/** Fetches account data from either: 
	 *  - Account ID (e.g. 6543499)
	 *  - Ext. Account Number (e.g. 123001001C)
	 *  - Client Number/Account Number (e.g. 123001, 001C)
	 *  
	 *  (whichever one is found first).
	 *  
	 *  If the extendedData flag is set, the V_ACCOUNT_EXTENDED_CLIENT view is used to
	 *  fetch Account data as opposed to the raw ACCOUNT table.
	 *  
	 *  @throws ServiceException If neither value is found, or no corresponding 
	 *  						 account data is found.
	 */
	private static DataResultSet getAccountData(DataBinder binder, boolean extendedData,
	 FWFacade facade) throws DataException, ServiceException {
		
		Integer accountId	 	= CCLAUtils.getBinderIntegerValue
		 (binder, Account.Cols.ID); 
		String accNumExt 		= binder.getLocal("ACCNUMEXT");
		
		DataResultSet rsAccount = null;
		
		if (accountId != null) {
			if (extendedData)
				rsAccount = Account.getExtendedData(accountId, facade);
			else
				rsAccount = Account.getData(accountId, facade);
			
			if (rsAccount.isEmpty())
				throw new ServiceException("Unable to find account with Account ID: "
				 + accountId);
			
		} else if (!StringUtils.stringIsBlank(accNumExt)) {
			if (extendedData)
				rsAccount = Account.getExtendedData(accNumExt, facade);
			else
				rsAccount = Account.getData(accNumExt, facade);
			
			if (rsAccount.isEmpty())
				throw new ServiceException("Unable to find account with Ext. Acc. No: "
				 + accNumExt);
		
		} else {
			// Search by legacy Aurora values.
			String clientNumStr		= binder.getLocal("CLIENT_NUMBER");
			String accountNumStr	= binder.getLocal("ACCOUNT_NUMBER");
			
			if (StringUtils.stringIsBlank(clientNumStr)
				||
				StringUtils.stringIsBlank(accountNumStr)) {
				// Insufficient parameters to perform lookup.
				throw new ServiceException("Unable to fetch account info, "
				 + "Account ID/ext. account number missing");
			} else {	
				rsAccount = Account.getAccountByIndexingValues
				 (clientNumStr,accountNumStr,extendedData,facade);
			}
		}
		
		return rsAccount;
	}
	
	public static void updateAuroraFlags
	 (int accountId, FWFacade facade, DataBinder binder, String username) 
	 throws DataException {
		// flags in binder as flag_<ACCOUNT_FLAG_ID>
		// get all flags from reference table
		
		DataResultSet rsFlags = AccountFlag.getAll(facade);
				
		//loop through adding or removing them according to presence in binder
		do {
			int checkFlag = CCLAUtils.getResultSetIntegerValue
			 (rsFlags, "ACCOUNT_FLAG_ID");
			
			boolean setFlag = CCLAUtils.getBinderBoolValue
			 (binder, "flag_" + Integer.toString(checkFlag));
			
			if (setFlag)
				AccountFlagApplied.add
				 (accountId, checkFlag, new Date(), facade, username);
			else
				AccountFlagApplied.remove(accountId, checkFlag, facade, username);
			
		}  while (rsFlags.next());
	}
	
	/** Admin-only service method used for bulk addition/removal of Account Flags.
	 *  
	 *  A ResultSet is generated from the queryText, which is expected to contain
	 *  ACCOUNT_ID and ACCOUNT_FLAG_ID columns.
	 *  
	 *  The selected flag is then added/removed to each matched ACCOUNT_ID.
	 *  
	 * @throws DataException
	 */
	public void bulkAddOrRemoveAccountFlags() throws DataException {
		String sql = m_binder.getLocal("queryText");
		
		boolean isRemove = CCLAUtils.getBinderBoolValue(m_binder, "isRemove");
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		DataResultSet rs = facade.createResultSetSQL(sql);
		
		Log.debug("Executed Account query: " + sql);
		Log.debug("Returned " + rs.getNumRows() + " rows");
	
		Log.debug("Columns: ");
		
		for (int i=0; i<rs.getNumFields(); i++) {
			Log.debug(i + ": " + rs.getFieldName(i));
		}
		
		Date date = new Date();
		int added = 0, removed = 0;
		
		if (rs.first()) {
			do {
				Integer accountId = CCLAUtils.getResultSetIntegerValue
				 (rs, Account.Cols.ID);
				
				Integer flagId = CCLAUtils.getResultSetIntegerValue
				 (rs, AccountFlag.Cols.ID);
				
				if (accountId != null && flagId != null) {
					Log.debug((isRemove ? "Removing" : "Adding") + " Account Flag ID " +
					 flagId + ", Account ID " + accountId);
					
					AccountFlagApplied existingFlag = 
					 AccountFlagApplied.get(accountId, flagId, facade);
					
					if (isRemove) {
						if (existingFlag != null) {
							AccountFlagApplied.remove
							 (accountId, flagId, facade, Globals.Users.System);
							
							removed++;
							Log.debug("Removed flag from Account ID " + accountId);
						} else {
							Log.warn("Unable to remove flag from Account ID " + accountId + 
							 ", doesn't exist");
						}
					} else {
						if (existingFlag == null) {
							AccountFlagApplied.add
							 (accountId, flagId, date, facade, Globals.Users.System);
							
							added++;
							Log.debug("Added flag to Account ID " + accountId);
						} else {
							Log.warn("Unable to add flag to Account ID " + accountId + 
							 ", already exists");
						}
					}
				} else {
					if (accountId == null)
						Log.warn("ACCOUNT_ID not present/missing in ResultSet row");
					else if (flagId == null)
						Log.warn("ACCOUNT_FLAG_ID not present/missing in ResultSet row");
				}
				
				Log.debug("Completed " + (rs.getCurrentRow()+1) + "/" + rs.getNumRows());
				
			} while (rs.next());
			
			Log.debug("Finished. Added: " + added + ", Removed: " + removed + 
			 ", Total: " +  rs.getNumRows());
		}
	}

	/**
	 * This method obtains the ACCOUNT_ID and REQ_SIGS from the CLIENT_NUMBER and 
	 * ACCOUNT_NUMBER
	 * 
	 * CALLED BY SERVICE CCLA_CS_GET_ACCOUNT_ID_FROM_INDEXING_DATA
	 * @throws DataException 
	 * @throws Exception
	 */
	public void getAccountIDAndReqSigsFromIndexingData() 
	 throws ServiceException, DataException {
		
		String clientNumStr		= m_binder.getLocal("CLIENT_NUMBER");
		String accountNumStr	= m_binder.getLocal("ACCOUNT_NUMBER");
		
		if (StringUtils.stringIsBlank(clientNumStr)){
			throw new ServiceException
			 ("getAccountIdFromIndexingData: CLIENT_NUMBER not on binder.");
		}
		if (StringUtils.stringIsBlank(accountNumStr)){
			throw new ServiceException
			 ("getAccountIdFromIndexingData: accountNumStr not on binder.");
		}
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		DataResultSet rsAccount = Account.getAccountByIndexingValues
		 (clientNumStr,accountNumStr,false,facade);
		 
		Account acc = Account.get(rsAccount);
		
		BinderUtils.addIntParamToBinder
		 (m_binder, Account.Cols.ID, acc.getAccountId());
		BinderUtils.addIntParamToBinder
		 (m_binder, "REQ_SIGS", acc.getRequiredSignatures());
	}
	
	/** Gets the share class details for an account
	 * 
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void getShareClassDetails() throws DataException, ServiceException
	{
		FWFacade facade 		= CCLAUtils.getFacade(m_workspace, true);
		
		DataResultSet rsAccount = getAccountData(m_binder, false, facade);
		Account account = Account.get(rsAccount);
		
		// get share class details
		Integer shareClassId = Account.getTrueShareClass(account.getAccountId(), facade);
		if (shareClassId!=null)
		{
			m_binder.addResultSet("rsShareClass", ShareClass.getData(shareClassId, facade));
		}	
		if (shareClassId != null)
		{
			ShareClassGroup group = ShareClassGroup.getByAccountId(account.getAccountId(), facade);
			if (group!=null)
			{
				m_binder.addResultSet("rsShareClassGroup", ShareClassGroup.getData(group.getGroupId(), facade));
			}
			ShareClassOverride sco = ShareClassOverride.get(account.getAccountId(), facade);
			if (sco != null)
			{
				m_binder.addResultSet("rsShareClassOverride", ShareClass.getData(sco.getShareClassId(), facade));
			}
		}			
		// get available share classes for account fund		
		DataResultSet rsShareClass = ShareClass.getEnabledShareClassesByFund(account.getFundCode(), facade);
		
		m_binder.addResultSet("rsAvailableShareClasses", rsShareClass);
	}
	
	public void updateShareClassOverride() throws DataException, ServiceException
	{
		Log.debug("starting updateShareClassOverride");
		FWFacade facade 		= CCLAUtils.getFacade(m_workspace,true);	
		String username = m_userData.m_name;
		
		Integer accountId = BinderUtils.getBinderIntegerValue(m_binder, "ACCOUNT_ID");
		if (accountId == null)
			throw new ServiceException("Cannot find ACCOUNT_ID value");
		
		// first determine if share class override already exists for account
		ShareClassOverride sco = ShareClassOverride.get(accountId, facade);
		
		if (sco == null)
		{		
		ShareClassOverride newSco = ShareClassOverride.add(m_binder, facade, username);
		}
		else{
		// update existing override
		sco.setAttributes(m_binder);
		sco.setUserId(username);
		sco.persist(facade, username);
		}
	}
	
	/** 
	 * Method that checks a documents' xBankAccountNumber and xSortCode metadata fields
	 * to see if they match the nominated bank account details which are stored in the
	 * database.
	 * 
	 * Required binderParams
	 * 
	 *  - dID
	 *  - TYPE_OF_ACCOUNT - can be either INCOME OR WITHDRAWAL
	 *  
	 *  Called by CCLA_CS_CHECK_FOR_NOMINATED_BANK_ACCOUNT
	 * @throws Exception 
	 * @throws NumberFormatException 
	 * 
	 */
	public void isNominatedBankAccount() throws ServiceException {
		
		try {
			FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
			Account.isNominatedBankAccount(m_binder,facade);
			
		} catch (Exception e) {
			throw new ServiceException("Unable to check for the nominated account: "
			 + e.getMessage(),e);
		}
	}
	
	/** Used to add or remove a set of selected Account Flags from a given set of 
	 *  accounts.
	 *  
	 *  Used on the Organisation Bulk Update page.
	 *  
	 * @throws ServiceException
	 * @throws DataException 
	 */
	public void addOrRemoveAccountFlags() throws ServiceException, DataException {
		
		// First determine whether Account Flags will be created or removed.
		Relation.UpdateType updateType = Relation.UpdateType.ADD;
		
		if (!m_binder.getLocal("relationUpdateType").equals("add"))
			updateType = Relation.UpdateType.REMOVE;
		
		// Fetch and parse the list of common Element IDs.
		String accountIdsStr = m_binder.getLocal("accountIds");
		Vector<Integer> accountIds = CCLAUtils.getIntegerList(accountIdsStr);
		
		// Fetch all Account Flags
		Collection<AccountFlag> accountFlags = 
		 AccountFlag.getCache().getCache().values();
		
		Vector<Integer> selectedAccountFlagIds = new Vector<Integer>();
		
		// Determine which Account Flags were selected by the user and build a list
		// of the selected Account Flag IDs.
		for (AccountFlag accountFlag : accountFlags) {
			if (!StringUtils.stringIsBlank
				(m_binder.getLocal("flag_" + accountFlag.getAccountFlagId()))) {
				selectedAccountFlagIds.add(accountFlag.getAccountFlagId());
			}
		}
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		for (Integer accountId : accountIds) {
			AccountFlagApplied.addOrRemove
			 (accountId, selectedAccountFlagIds, updateType, facade, m_userData.m_name);
		}
	}
	
	/** Used to set particular Account data values across multiple accounts in one go.
	 * 
	 *  Used by the Entity Bulk Update page.
	 *  
	 *  At the current time, only supports updates to the 'Required Signatures' values.
	 *  
	 * @throws DataException 
	 * @throws ServiceException 
	 *  
	 */
	public void setMultipleAccountsData() throws DataException, ServiceException {
				
		// Fetch and parse the list of Account IDs.
		String accountIdsStr = m_binder.getLocal("accountIds");
		Vector<Integer> accountIds = CCLAUtils.getIntegerList(accountIdsStr);

		Log.debug("setMultipleAccountsData:: " + accountIds.size() +" selected accounts");
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		Integer requiredSignatures = CCLAUtils.getBinderIntegerValue
			(m_binder, Account.Cols.REQ_SIGNATURES);
		
		// Check we have something to udpate.
		if (requiredSignatures == null)
			throw new ServiceException("Unable to set multiple account data -"
			 + " no field values supplied");
		else {
			Log.debug("Setting num. req. signatories to " + requiredSignatures);
		}
		
		try {
			facade.beginTransaction();
			
			for (Integer accountId : accountIds) {
				Account account = Account.get(accountId, facade);
				
				if (requiredSignatures != null) {
					account.setRequiredSignatures(requiredSignatures);
				}
				
				// Don't bother validating.
				account.persist(facade, m_userData.m_name, false);
			}
			
			facade.commitTransaction();
		} catch (Exception e) {
			facade.rollbackTransaction();
			Log.error("Failed to update account data. " + e.getMessage(), e);
			throw new ServiceException("Failed to update account data. " + e.getMessage(), e);
		}
	}
	
	/**
	 * @UCM_Service CCLA_CS_ACCOUNT_INFO
	 * @throws ServiceException
	 * @throws DataException
	 */
	public void getAccountInfo() throws ServiceException, DataException{
		//5:qClientServices_GetElementRelationNames:rsElementRelationTypeNames::null
		//5:qClientServices_GetRelationProperties:rsRelationProperties::null
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		m_binder.addResultSet(
				"rsElementRelationTypeNames", 
				facade.createResultSet("qClientServices_GetElementRelationNames", m_binder)
		);
		m_binder.addResultSet(
				"rsRelationProperties", 
				facade.createResultSet("qClientServices_GetRelationProperties", m_binder)
		);
	}
	
	/**
	 * @UCM_Service CCLA_CS_ACCOUNT_NEW
	 * @throws ServiceException
	 * @throws DataException
	 */
	public void newAccount() throws ServiceException, DataException{
		//5:qClientServices_GetIncomeDistMethods:rsIncomeDistMethods::null
		//5:qClientServices_GetFunds:rsFunds::null
		//5:qClientServices_GetOrganisation:rsOrganisation::null
		//5:qClientServices_GetAuroraClientsByEntityId:rsAuroraClient::null
		//5:qClientServices_GetCompanyList:rsCompanyList::null
		//5:qClientServices_GetAllAccountFlags:rsAccountFlags::null
		//5:qClientServices_GetAllAgreementTypes:rsAgreementTypes::null
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		m_binder.addResultSet(
				"rsIncomeDistMethods", 
				facade.createResultSet("qClientServices_GetIncomeDistMethods", m_binder)
		);
		m_binder.addResultSet(
				"rsFunds", 
				facade.createResultSet("qClientServices_GetFunds", m_binder)
		);
		m_binder.addResultSet(
				"rsOrganisation", 
				facade.createResultSet("qClientServices_GetOrganisation", m_binder)
		);
		m_binder.addResultSet(
				"rsAuroraClient", 
				facade.createResultSet("qClientServices_GetAuroraClientsByEntityId", m_binder)
		);
		m_binder.addResultSet(
				"rsCompanyList", 
				facade.createResultSet("qClientServices_GetCompanyList", m_binder)
		);
		m_binder.addResultSet(
				"rsAccountFlags", 
				facade.createResultSet("qClientServices_GetAllAccountFlags", m_binder)
		);
		m_binder.addResultSet(
				"rsAgreementTypes", 
				facade.createResultSet("qClientServices_GetAllAgreementTypes", m_binder)
		);
	}
	
	/**
	 * @UCM_Service CCLA_CS_ACCOUNT_EDIT
	 * @throws ServiceException
	 * @throws DataException
	 */
	public void editAccount() throws ServiceException, DataException{
		//5:qClientServices_GetAccountStatuses:rsAccountStatuses::null
		//5:qClientServices_GetIncomeDistMethods:rsIncomeDistMethods::null
		//5:qClientServices_GetAllAgreementTypes:rsAgreementTypes::null	
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		m_binder.addResultSet(
				"rsAccountStatuses", 
				facade.createResultSet("qClientServices_GetAccountStatuses", m_binder)
		);
		m_binder.addResultSet(
				"rsIncomeDistMethods", 
				facade.createResultSet("qClientServices_GetIncomeDistMethods", m_binder)
		);	
		m_binder.addResultSet(
				"rsAgreementTypes", 
				facade.createResultSet("qClientServices_GetAllAgreementTypes", m_binder)
		);
	}
	
	
	/**
	 * @UCM_Service CCLA_CS_GET_ACCOUNT_ONLY_BASIC
	 * @throws ServiceException
	 * @throws DataException
	 */
	public void getAccountOnlyBasicResults() throws ServiceException, DataException{
		//5:qClientServices_GetElementRelationNames:rsElementRelationTypeNames::null
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		m_binder.addResultSet(
				"rsElementRelationTypeNames", 
				facade.createResultSet("qClientServices_GetElementRelationNames", m_binder)
		);
	}
	
	
	/**
	 * Validate an account setting basic on the aurora logic
	 * 
	 * if isPersist == false, then errors are added to the binder
	 * -- IF account data is ok, isValid=true set to binder
	 * -- IF account data is not ok, isValid=false, errorMessage=[error details] set to binder
	 * 
	 * if isPersist == true, then dataException is thrown
	 * 
	 * @throws ServiceException
	 * @throws DataException
	 */
	public void validationAuroraAccount() throws ServiceException, DataException 
	{
		//Flag to throws an data exception or add error message to binder
		//if this is set to true, data exception will be throws.
		boolean isPersist = CCLAUtils.getBinderBoolValue(m_binder, "IS_PERSIST"); 
		
		//Complete validation of all account info.
		//Should be used once all data is setup as it will validate bank accounts etc..
		boolean isValidateAll = CCLAUtils.getBinderBoolValue(m_binder, "IS_VALIDATE_ALL"); 
		boolean isAuroraAccount = CCLAUtils.getBinderBoolValue(m_binder, "IS_AURORA_ACCOUNT"); 
		
		String incomeDistributionMethod = m_binder.getLocal(Account.Cols.INC_DIST_METHOD);
		String fundCode = CCLAUtils.getBinderStringValue(m_binder, Account.Cols.FUND_CODE);
		Integer mandatedAccountId = CCLAUtils.getBinderIntegerValue(m_binder, com.ecs.ucm.ccla.data.Account.Cols.MANDATED_ACCOUNT);
		Integer accountId = CCLAUtils.getBinderIntegerValue(m_binder, com.ecs.ucm.ccla.data.Account.Cols.ID);
		
		boolean forceValidateAuroraAccounts = false;
		
		SystemConfigVar config = SystemConfigVar.getCache().getCachedInstance
		 (Globals.VALIDATE_AURORA_ACCOUNTS);
		
		if (config!=null && config.getBoolValue()!=null && config.getBoolValue()) 
			forceValidateAuroraAccounts = true;
		
		if (isAuroraAccount && forceValidateAuroraAccounts) {
			FWFacade facade = CCLAUtils.getFacade(true);
			
			Account account = null;
			
			if (accountId != null)
				account = Account.get(accountId, facade);
			
			AccountValidationOutcome outcome = 
				AuroraAccountHandler.validateAuroraAccount(
						account, 
						mandatedAccountId, 
						incomeDistributionMethod, 
						fundCode, 
						isPersist, 
						isValidateAll, 
						facade);
			
			CCLAUtils.addQueryBooleanParamToBinder
			 (m_binder, "isValid", outcome.isValid());
			CCLAUtils.addQueryParamToBinder
			 (m_binder, "errorMessage", outcome.getErrorMsg());
			
		} else {
			CCLAUtils.addQueryBooleanParamToBinder(m_binder, "isValid", true);
		}
	}
	
	/** Used to fetch info used when creating new 'Donor Accounts' against LCF 
	 *  Organisations. This is called before displaying the New Account page.
	 * @throws DataException 
	 */
	public void getNewDonorAccountData() throws DataException {
		
		// Find the Comm. First CF Account for the Organisation, if one exists.
		Integer orgId = CCLAUtils.getBinderIntegerValue(m_binder, Entity.Cols.ID);
		
		if (orgId == null)
			return;
		
		FWFacade facade = CCLAUtils.getFacade(true);
		
		Account lcfAccount = CommunityFirstClientEnrolmentHandler
		 .getDonationAccount(orgId, facade);
		
		// No need to go further if there is no CF account for the org, or the CF account
		// isn't open.
		if (lcfAccount == null || lcfAccount.getStatus() != Account.AccountStatus.OPEN)
			return;
		
		// Flag used to change the New Account UI.
		m_binder.putLocal("isDonorAccount", "1");
		
		Log.debug("Organisation has active Comm. First CF Account. " +
		 "Fetching available Donor details");
			
		// Add list of Org/Person donors
		ContributionService.addAvailableDonorsByOrganisationIdDataToBinder
		 (orgId, m_binder, facade);
	}
}
