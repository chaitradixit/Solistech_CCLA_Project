package com.ecs.ucm.ccla.data;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.shared.SharedObjects;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Globals;
import com.ecs.ucm.ccla.Globals.UCMFieldNames;
import com.ecs.ucm.ccla.aurora.AuroraAccountHandler;
import com.ecs.ucm.ccla.aurora.compare.AuroraEntitySource;
import com.ecs.ucm.ccla.experian.AuthenticationScoreUtils;

import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.DataResultSetUtils;
import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.embedded.FWFacade;


/** Represents an entry from the CCLA_ACCOUNT table.
 * 
 * @author Tom
 *
 */
public class Account extends Element implements Persistable, Comparable<Account>, AuroraEntitySource {

	/** Return codes used when requesting account AML status.
	 * 
	 * @author Tom
	 */
	public static enum IVSStatusCode {
		PASSED,
		FAILED,
		UNKNOWN,
		NOT_IMPLEMENTED
	}
	
	/** Account Status values, as found in the REF_ACCOUNT_STATUS table.
	 *  
	 * @author Tom
	 *
	 */
	public static final class AccountStatus {
		public static final int OPEN = 1;
		public static final int CLOS = 2;
		public static final int FROZ = 3;
		public static final int PEND = 4;
		public static final int TEMP = 5;
	}
	
	/** Account Type values, as found in the REF_ACCOUNT_TYPE table.
	 * 
	 * @author Tom
	 *
	 */
	public static final class AccountType {
		public static final int UNITIZED 	= 1;
		public static final int DEPOSIT 	= 2;
		public static final int LOAN 		= 3;
		public static final int COMM_FIRST	= 4;
	}
	
	/** Loan Type values, as found in the REF_LOAN_TYPE table.
	 * 
	 * @author Tom
	 *
	 */
	public static final class LoanType {
		public static final int INTERST_ONLY 	= 1;
		public static final int REPAYMENT 		= 2;
	}
	
	/** Agreement Type values, as found in REF_AGREEMENT_TYPE table.
	 * 
	 * @author Tom
	 *
	 */
	public static final class AgreementType {
		public static final int SCHEME_PARTICULAR 	= 1;
		public static final int TRIPARTITE 			= 2;
		public static final int SEGREGATED 			= 3;
	}
	
	public static final int DEFAULT_AGREEMENT_TYPE = AgreementType.SCHEME_PARTICULAR;
	
	/** Income Distribution Methods values, as found in the REF_INCOME_DIST_METHOD
	 *  table.
	 *  
	 * @author Tom
	 *
	 */
	public static enum IncomeDistMethod {
		PAYA,
		REIN,
		RETN,
		TXRI
	}
	
	/** Response object used when fetching an Account's IVS status.
	 *  <br/>
	 *  The status code indicates the overall IVS result, either PASSED 
	 *  or FAILED. This depends on the IVS status of specific persons
	 *  who are linked to the account. 
	 *  <br/>
	 *  The passReason will indicate a special case for passing the check, e.g. a
	 *  Nominee client skips all IVS checks. Won't always be present.
	 *  <br/>
	 *  The failReason will indicate the reason for an IVS failure, if applicable.
	 *  <br/>
	 *  The override flag is set if the check passed due to an override attribute being
	 *  set.
	 *  
	 * @author Tom
	 *
	 */
	public static class IVSCheck {
		public IVSStatusCode statusCode;
		
		public String passReason;
		public String failReason;
		
		public boolean override;
		
		public void addToBinder(DataBinder binder) {
			binder.putLocal("IVSCheckStatus", this.statusCode.toString());
			
			if (this.failReason != null)
				binder.putLocal("IVSFailReason",  this.failReason);
			
			if (this.passReason != null)
				binder.putLocal("IVSPassReason",  this.passReason);
			
			CCLAUtils.addQueryBooleanParamToBinder(binder, "IVSOverride", override);
		}
	}
	
	/** If this flag is true, the services/methods used to fetch the nominated 
	 *  withdrawal/income bank account will only return a result if the bank account
	 *  relation was explicitly marked as 'nominated'.
	 *  
	 *  If false, and there is no bank account explicitly marked as 'nominated', the
	 *  first withdrawal/income relation will be treated as the nominated bank account,
	 *  if the relation exists.
	 */
	public static final boolean USE_STRICT_NOMINATED_BANK_ACCOUNT = 
	 !StringUtils.stringIsBlank(SharedObjects.getEnvironmentValue
	 ("CCLA_UseStrictNominatedBankAccount"));
	
	/** Person-Account relations which must be IVS-checked to determine
	 *  whether the Account is IVS-passed or not.
	 *  
	 *  TODO: externalise!
	 */
	public static class RequiredIVSRelations {
		// Default relation checks.
		public static int[] DEFAULT_REQ_IVS_RELATIONS = new int[] {
			RelationName.PersonAccountRelation.AUTH_PERSON,
		};
		
		// PSDF Fund relation checks.
		public static int[] PSDF_REQ_IVS_RELATIONS = new int[] {
			RelationName.PersonAccountRelation.AUTH_PERSON,
		};
	}
	
	public static class Cols {
		public static final String ID 				= "ACCOUNT_ID";
		public static final String ACCOUNTNUMBER 	= "ACCOUNTNUMBER";
		public static final String FUND_CODE		= "FUND_CODE";
		public static final String ACCNUMEXT		= "ACCNUMEXT";
		public static final String STATUS 			= "ACCOUNT_STATUS_ID";
		public static final String SUBTITLE 		= "SUBTITLE";
		public static final String MANDATED_ACCOUNT = "MANDATED_ACCOUNT_ID";
		public static final String AURORA_ACCOUNT	= "AURORA_ACCOUNT";
		public static final String INC_DIST_METHOD  = "INCOME_DISTRIBUTION_METHOD";
		public static final String REQ_SIGNATURES	= "REQ_SIGNATURES";
		public static final String ACCOUNT_TYPE		= "ACCOUNT_TYPE_ID";
		public static final String LOAN_TYPE		= "LOAN_TYPE_ID";
		public static final String LOAN_TERM		= "LOAN_TERM";
		public static final String AML_CHECK_OVERRIDE_USER = "AML_CHECK_OVERRIDE_USER";
		public static final String IS_EXCLUSIVE		= "IS_EXCLUSIVE";
		public static final String DATE_OPENED		= "DATE_OPENED";
		public static final String SHARE_CLASS		= "SHARE_CLASS_ID";
		public static final String AGREEMENT_TYPE   = "AGREEMENT_TYPE_ID";
		public static final String ACC_ACCOUNT_CODE = "ACC_ACCOUNT_CODE";
	}
	
	/** Field index for the CASH column */
	public static int CASH_FIELD_INDEX = -1;
	/** Field index for the UNITS column */
	public static int UNITS_FIELD_INDEX = -1;

	private int accountId;
	private boolean auroraAccount;
	private int accountNum;
	
	private String fundCode;
	private String accNumExt;
	private String subtitle;
	private int statusId;
	private Date dateOpened;
	private String shareClass;
	private Integer requiredSignatures;
	private String incomeDistMethod;
	private Integer mandatedAccId;
	private boolean exclusiveBankAccount;
	private String amlCheckOverrideUser;

	private Integer accountType;
	
	private Integer loanType;
	private Integer loanTerm;
	
	private Integer agreementType;
	
	private String accAccountCode;
	
	private Date lastUpdated;
	
	public Account() {}
	
	public Account(int accountId, Integer mandatedAccId, boolean auroraAccount, 
			int statusId, int accountNumber, String fundCode, 
			String accNumExt, String subtitle, 
			String shareClass, Integer requiredSignatures, String incomeDistMethod, 
			boolean exclusiveBankAccount, String amlCheckOverrideUser,
			Integer accountType, Integer loanType, Integer loanTerm,
			Integer agreementType, String accAccountCode,
			Date dateOpened, Date lastUpdated) {
		this.accountId = accountId;
		this.auroraAccount = auroraAccount;
		this.accountNum = accountNumber;
		this.fundCode = fundCode;
		this.accNumExt = accNumExt;
		this.subtitle = subtitle;
		this.statusId = statusId;
		this.dateOpened = dateOpened;
		this.shareClass = shareClass;
		this.requiredSignatures = requiredSignatures;
		this.incomeDistMethod = incomeDistMethod;
		this.mandatedAccId = mandatedAccId;
		this.exclusiveBankAccount = exclusiveBankAccount;
		this.amlCheckOverrideUser = amlCheckOverrideUser;
		this.accountType = accountType;
		this.loanType = loanType;
		this.loanTerm = loanTerm;
		this.agreementType = agreementType;
		this.lastUpdated = lastUpdated;
	}
	
	/** Creates a new Account and stores it in the database.
	 * 
	 *  The 'owner' Organisation-Account relationship is also created here.
	 *  
	 *  If a corrId is passed, this is expected to be a Person ID and it will
	 *  be used to create the 'correspondent' Person-Account relationship.
	 *  
	 *  This method should be wrapped in a transaction block.
	 * @throws ServiceException 
	 *  
	 */
	public static Account add(Entity org, AuroraClient auroraClient, 
		boolean auroraAccount, Fund fund, 
		Integer accountNum, String subtitle, int accountStatusId, int reqSignatures, 
		String incomeDistMethod, boolean isExclusiveBankAccount, Integer mandatedAccountId,
		Integer accountType, Integer loanType, Integer loanTerm, Integer agreementType,
		Integer corrId, FWFacade facade, String userName) 
		throws DataException, ServiceException {
		
		if (accountType == null) {
			// If the account type isn't specified, derive it from the fund's Type Code.
			if (fund.getTypeCode().getFundTypeCodeId() 
				== FundTypeCode.FUND_TYPECODE_ID_DEPOSIT)
				accountType = AccountType.DEPOSIT;
			else
				accountType = AccountType.UNITIZED;
		}
		
		// if accountNum = null, acquire the next available account number
		if (accountNum == null)
			accountNum = getNextAccountNumberForOrgAndFund
			 (org.getOrganisationId(), facade, fund.getFundCode(), accountType);
		
		//check to see if an account already exists, if it does throw exception
		if (!Account.getDataByOrg
				(org.getOrganisationId(), accountNum, fund.getFundCode(), facade)
				.isEmpty()) {
			throw new ServiceException("An account with account number " +
			 accountNum + " and fund " + fund.getFundCode() + " already exists");
		}
		
		// Create a new Element for this account.
		Element element = Element.add(ElementType.ACCOUNT, null, facade, userName);
		
		// Generate an external account number
		String accNumExt = generateExternalAccountNumber
		 (org, accountNum, fund.getFundCode(), facade);
		
		Account account = new Account(
		 element.getElementId(), 
		 mandatedAccountId, 
		 auroraAccount, 
		 accountStatusId, 
		 accountNum, 
		 fund.getFundCode(),
		 accNumExt,
		 subtitle,
		 null,
		 reqSignatures,
		 incomeDistMethod,
		 isExclusiveBankAccount,
		 null,
		 accountType,
		 loanType,
		 loanTerm,
		 agreementType,
		 null,
		 null,
		 null
		);
		
		Log.debug("Adding new Account ID " + element.getElementId());
		Log.debug(account.toString());
		
		account.validate(facade);
		
		// Prepare DataBinder with all the Account fields
		DataBinder binder = new DataBinder();
		account.addFieldsToBinder(binder);

		//If the account being created is a PC account, we need to create a 
		//mirrored account but with a PI fund code.
		facade.execute("qClientServices_AddAccount", binder);

		// Add the Org-Account 'owner' relationship.
		RelationName ownerRelation = RelationName.getCache().getCachedInstance
		 (RelationName.OrganisationAccountRelation.OWNER);
		
		Relation.add(org.getOrganisationId(), account.getAccountId(), 
		 ownerRelation, facade, userName);
		
		if (corrId != null) {
			// Add the Person-Account 'correspondent' relationship.
			RelationName corrRelation = RelationName.getCache().getCachedInstance
			 (RelationName.PersonAccountRelation.CORRESPONDENT);
			
			Relation.add(corrId, account.getAccountId(), 
			 corrRelation, facade, userName);
		}
		
		// Add audit record
		DataResultSet newData = Account.getData(account.getAccountId(), facade);

		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(account.getAccountId(), ElementType.ACCOUNT.getName());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, userName, 
		 Globals.AuditActions.ADD.toString(), 
		 ElementType.ACCOUNT.getName(), 
		 null, newData, auditRelations);
		
		// Generate companion PI account for any PC account that is created.
		if (fund.getFundCode().equals(Fund.FundCodes.PC.toString())){
			createCompanionAccount(account.accountId,Fund.FundCodes.PI.toString(),
					userName, facade);
		}
		
		return account;
	}

	/**
	 * Method which creates a clone account of the given accountId except
	 * with a different fund code. 
	 * Also sets the mandatedAccountId value of the new account to the original 
	 * accountId value
	 * 
	 * @param accountId - id of the account to clone
	 * @param fundCode - fund code for the new account
	 * @param orgId - organisation id 
	 * @param facade - facade object
	 * @throws DataException 
	 * @throws ServiceException 
	 */
	public static void createCompanionAccount(int accountId, String fundCode, 
			String userName, FWFacade facade) throws DataException, ServiceException {
		
		Log.debug("Generating Companion Account with Fund Code " + fundCode + " for " +
		 "Account ID " + accountId);
	
		Account companionAccount = Account.get(accountId, facade);
		
		Log.debug("Source Account: " + companionAccount.toString());
		
		//change the account details 
		companionAccount.setFundCode(fundCode);
		companionAccount.setMandatedAccId(accountId);

		DataBinder binder = new DataBinder();
		companionAccount.addFieldsToBinder(binder);
		
		//ORGANISATION_ID needed on binder 
		CCLAUtils.addQueryIntParamToBinder(binder, Entity.Cols.ID,
		 companionAccount.getOwnerOrganisationId(facade));
		
		//add the account to the database
		companionAccount = Account.add(binder, facade, userName);

		Account originalAccount = Account.get(accountId, facade);
		originalAccount.setMandatedAccId(companionAccount.getAccountId());
		originalAccount.persist(facade, userName);
	}

	/**
	 * 
	 *  Creates a new Account, plus the Relation between the Account and owning
	 *  Organisation.
	 *  
	 *  This method should be wrapped in a transaction block.
	 *  
	 *  The following fields are expected in the binder:
	 *  
	 *  -ORGANISATION_ID
	 *  -CLIENT_NUMBER/COMPANY_ID: if present, a new ClientAuroraMap is generated
	 *  -FUND_CODE
	 *  -AURORA_ACCOUNT: 0/1
	 *  -SUBTITLE
	 *  -ACCOUNTNUMBER: optional, will be auto-generated if not present
	 *  -ACCOUNT_STATUS_ID
	 *  -REQ_SIGNATURES
	 *  -INCOME_DISTRIBUTION_METHOD
	 *  -MANDATED_ACCOUNT_ID: optional
	 *  -IS_EXCLUSIVE: 0/1
	 *  -AGREEMENT_TYPE_ID: mandatory
	 *  
	 * @param binder
	 * @param facade
	 * @param username
	 * @return
	 * @throws DataException
	 * @throws ServiceException
	 * 
	 * 
	 */
	public static Account add(DataBinder binder, FWFacade facade, String username) 
	 throws DataException, ServiceException {
				
		// Create a 'blank' Account instance to begin with.
		Account account = new Account();
		
		String strOrgID = binder.getLocal(Entity.Cols.ID);
		if (StringUtils.stringIsBlank(strOrgID))
			throw new ServiceException ("Missing Organisation ID.");
			
		int orgID = Integer.parseInt(strOrgID);
		Entity org = Entity.get(orgID, facade);
		
		String orgName = org.getOrganisationName();
		binder.putLocal(Entity.Cols.ID, orgName);
		
		// Apply all account attributes using the binder values directly.
		account.setAttributes(binder, false);

		String fundCode = account.getFundCode();			
		
		Integer accountNumber =  CCLAUtils.getBinderIntegerValue
		 (binder, Cols.ACCOUNTNUMBER);

		Fund f = Fund.get(fundCode, facade);
		
		Vector<AuroraClient> clients = Entity.getAuroraClientMapping(orgID, facade);
		AuroraClient client = null;
		
		if (!clients.isEmpty())
			client = clients.get(0);
		
		// Fetch all Entity Correspondents, assign the first one as the
		// account correspondent if applicable.
		Vector<Relation> corrRelations = Relation.getRelations
		 (org.getOrganisationId(), null, null, RelationName.getCache().getCachedInstance(
		  RelationName.OrganisationPersonRelation.CORRESPONDENT), facade);
		Integer corrId = null;
		if (corrRelations.isEmpty())
			throw new ServiceException
			 ("Cannot create an account without a correspondent. " +
			  "Ensure the client has a correspondent set.");
		else
			corrId = corrRelations.get(0).getElementId2();
		
		return Account.add(org, client, account.auroraAccount, 
			f, accountNumber, account.getSubtitle(), 
			account.statusId, account.requiredSignatures, 
			account.incomeDistMethod, account.isExclusiveBankAccount(),
			account.mandatedAccId, account.accountType, 
			account.loanType, account.loanTerm, account.agreementType,
			corrId, facade, username);
	}
	
	/** Creates an External Account Number (ACCNUMEXT) for the given account.
	 *  
	 * @return
	 * @throws DataException 
	 */
	private static String generateExternalAccountNumber
	 (Entity org, int accountNumber, String fundCode, FWFacade facade) 
	 throws DataException {

		Fund fund = Fund.getCache().getCachedInstance(fundCode);
		
		AuroraClient auroraClient = Entity.getAuroraClientCompanyMapping
		 (org.getOrganisationId(), fund.getCompany(), facade);
		
		if (auroraClient == null) {
			throw new DataException("Unable to generate Ext. Account Number. Owning " +
			 "Organisation does not have a " + fund.getCompany().getCode() + 
			 " Client link configured");
		}
		
		return AuroraAccountHandler.buildAccNumExt
		 (fundCode, auroraClient, accountNumber);
		
		/* Old method:
		
		 Doesn't have any real bearing on the ACCNUMEXT values imported/created
	 *  by Aurora. So this doesn't have much importance in its current state.
	 *  
	 *  The current format is:
	 *  -Aurora Client Number/Org ID padded to 8 digits
	 *  -Account Number padded to 8 digits
	 *  -Fund Code
		
		if (!auroraClients.isEmpty()) {
			// Aurora Client No. mapping found.
			AuroraClient auroraClient = auroraClients.get(0);
			
			accNumExt = CCLAUtils.padString
			 (Integer.toString(auroraClient.getClientNumber()), '0', 8);
		
		} else {
			// Use padded Org ID instead.
			accNumExt = CCLAUtils.padString
			 (Integer.toString(org.getOrganisationId()), '0', 8);
		}
		
		// Add padded account number
		accNumExt += CCLAUtils.padString
		 (Integer.toString(accountNumber), '0', 8);
		
		// Add fund code
		accNumExt += fundCode;
		
		Log.debug("calculated accnumext is " + accNumExt);
		return accNumExt;
		*/
	}
	
	/** Returns a list of Account instances for every entry in the
	 *  passed ResultSet, where the ResultSet is expected to contain
	 *  rows from the ACCOUNT_EXTENDED view.
	 *  
	 * @param rsAccounts
	 * @return
	 * @throws NumberFormatException
	 * @throws DataException
	 */
	public static Vector<Account> getAll(DataResultSet rsAccounts) 
	 throws NumberFormatException, DataException {
		
		Vector<Account> accounts = new Vector<Account>();
		
		if (rsAccounts.first()) {
			do {
				accounts.add(get(rsAccounts));
			} while (rsAccounts.next());
		}
		
		return accounts;
	}
	
	public static Account get(DataResultSet rsAccount) 
	 throws NumberFormatException, DataException {
		
		if (rsAccount.isEmpty())
			return null;
		
		return new Account(
			CCLAUtils.getResultSetIntegerValue(rsAccount, Cols.ID),
			CCLAUtils.getResultSetIntegerValue(rsAccount, Cols.MANDATED_ACCOUNT),
			CCLAUtils.getResultSetBoolValue(rsAccount, Cols.AURORA_ACCOUNT),
			CCLAUtils.getResultSetIntegerValue(rsAccount, Cols.STATUS),
			CCLAUtils.getResultSetIntegerValue(rsAccount, Cols.ACCOUNTNUMBER),
			rsAccount.getStringValueByName(Cols.FUND_CODE),
			rsAccount.getStringValueByName(Cols.ACCNUMEXT),
			rsAccount.getStringValueByName(Cols.SUBTITLE),
			rsAccount.getStringValueByName(Cols.SHARE_CLASS),
			CCLAUtils.getResultSetIntegerValue(rsAccount, Cols.REQ_SIGNATURES),
			rsAccount.getStringValueByName(Cols.INC_DIST_METHOD),
			CCLAUtils.getResultSetBoolValue(rsAccount, Cols.IS_EXCLUSIVE),
			rsAccount.getStringValueByName(Cols.AML_CHECK_OVERRIDE_USER),
			CCLAUtils.getResultSetIntegerValue(rsAccount, Cols.ACCOUNT_TYPE),
			CCLAUtils.getResultSetIntegerValue(rsAccount, Cols.LOAN_TYPE),
			CCLAUtils.getResultSetIntegerValue(rsAccount, Cols.LOAN_TERM),
			CCLAUtils.getResultSetIntegerValue(rsAccount, Cols.AGREEMENT_TYPE),
			rsAccount.getStringValueByName(Cols.ACC_ACCOUNT_CODE),
			rsAccount.getDateValueByName(Cols.DATE_OPENED),
			rsAccount.getDateValueByName(SharedCols.LAST_UPDATED)
		);
	}
	
	/** Fetches an Account instance by its Element ID value.
	 *  
	 * @param accNumExt
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Account get(int accountId, FWFacade facade) 
	 throws DataException {
		
		DataResultSet rsAccount = getData(accountId, facade);
		return get(rsAccount);
	}
	
	/** Fetches raw account data by its Element ID.
	 * 
	 * @param accNumExt
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getData(int accountId, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.ID, accountId);
		
		return facade.createResultSet
		 ("qClientServices_GetAccount", binder);
	}
	
	/** Fetches extended account data from the V_ACCOUNT_EXTENDED_CLIENT view 
	 *  by its Element ID.
	 * 
	 * @param accNumExt
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getExtendedData(int accountId, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.ID, accountId);
		
		return facade.createResultSet
		 ("qClientServices_GetAccountExtended", binder);
	}
	
	/** Fetches an Account instance by External Account Number value.
	 *  
	 * @param accNumExt
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Account get(String accNumExt, FWFacade facade) 
	 throws DataException {
		
		DataResultSet rsAccount = getData(accNumExt, facade);
		return get(rsAccount);
	}
	
	/**
	 * Fetches account DataResultSet by Organisation Id,
	 * Account Number and Fund Code.
	 * @param orgId
	 * @param accountNum
	 * @param fundCode
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getDataByOrg
	(int orgId, int accountNum, String fundCode, 
			  FWFacade facade) throws DataException {
	
		DataBinder binder = new DataBinder();

		CCLAUtils.addQueryIntParamToBinder(binder, Entity.Cols.ID, orgId);
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.ID, accountNum);
		CCLAUtils.addQueryParamToBinder(binder, Cols.FUND_CODE, fundCode);
		
		return facade.createResultSet("qCore_GetAccountByOrg", binder);
	}
	
	/** Fetches an ResultSet of raw Account data by Aurora Client Number,
	 *  Account Number and Fund Code.
	 *  
	 * @param clientNum
	 * @param accountNum
	 * @param fundCode
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getDataByAuroraValues
	 (int clientNum, int accountNum, String fundCode, boolean extendedData,
	  FWFacade facade) throws DataException {
		
		Fund fund = Fund.getCache().getCachedInstance(fundCode);
		
		if (fund == null) {
			throw new DataException("No Fund found with code " + 
			 fundCode);
		}
		
		Entity entity = Entity.getEntityIdByAuroraClientNumberAndFund
		 (clientNum, fundCode, facade);
		
		if (entity == null) {
			throw new DataException("No matching Organisation with " +
			 "Aurora Client Number: " + clientNum + ", FundCode: " + 
			 fundCode);
		}
		
		return getDataByValues(entity.getOrganisationId(), accountNum, fundCode, 
		 extendedData, facade);
	}
	
	/** Fetches an ResultSet of raw Account data by Organisation ID,
	 *  Account Number and Fund Code.
	 *  
	 * @param clientNum
	 * @param accountNum
	 * @param fundCode
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getDataByValues
	 (int orgId, int accountNum, String fundCode, boolean extendedData,
	  FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();

		CCLAUtils.addQueryIntParamToBinder
		 (binder, Entity.Cols.ID, orgId);
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "ACCOUNT_NUMBER", accountNum);
		binder.putLocal("FUND_CODE", fundCode);
		
		if (extendedData)
			return facade.createResultSet
			 ("qClientServices_GetAccountByValuesExtended", binder);
		else
			return facade.createResultSet
			 ("qClientServices_GetAccountByValues", binder);
	}
	
	/** Fetches raw account data by External Account Number value.
	 * 
	 * @param accNumExt
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getData(String accNumExt, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		binder.putLocal(Cols.ACCNUMEXT, accNumExt);
		
		return facade.createResultSet
		 ("qClientServices_GetAccountByAccNumExt", binder);
	}
	
	/** Fetches exteneded account data from the V_ACCOUNT_EXTENDED_CLIENT view,
	 *  by External Account Number value.
	 * 
	 * @param accNumExt
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getExtendedData(String accNumExt, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		binder.putLocal(Cols.ACCNUMEXT, accNumExt);
		
		return facade.createResultSet
		 ("qClientServices_GetAccountByAccNumExtExtended", binder);
	}
	
	
	/** Fetches account data by fund code and cfAccountType attribute value.
	 * Use ElementAttributeApplied.CFAccTypeAttrValue string values.
	 * 
	 * @param fundCode
	 * @param cfAccountType
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getCFAccountData(String fundCode, String cfAccountType, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		binder.putLocal(Account.Cols.FUND_CODE, fundCode);
		binder.putLocal(ElementAttributeApplied.Cols.ATTRIBUTE_VALUE, cfAccountType);
		return facade.createResultSet
		 ("qCore_GetCFAccountByFundAndAccType", binder);
	}
	
	/** Fetches account data by fund code and cfAccountType attribute value.
	 * Use ElementAttributeApplied.CFAccTypeAttrValue string values.
	 * 
	 * @param fundCode
	 * @param cfAccountType
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Account getCFAccount(String fundCode, String cfAccountType, FWFacade facade) 
	 throws DataException {
		
		DataResultSet rsAccount = getCFAccountData(fundCode, cfAccountType, facade);
		return get(rsAccount);
	}
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {

		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.ID, this.getAccountId());
		
		CCLAUtils.addQueryBooleanParamToBinder
		 (binder, Cols.AURORA_ACCOUNT, this.isAuroraAccount());
		
		CCLAUtils.addQueryIntParamToBinder(binder,
		 Cols.ACCOUNTNUMBER, this.getAccountNumber());
		
		CCLAUtils.addQueryParamToBinder
		 (binder, Cols.FUND_CODE, this.getFundCode());
		
		CCLAUtils.addQueryParamToBinder
		 (binder, Cols.ACCNUMEXT, this.getAccNumExt());
		
		CCLAUtils.addQueryParamToBinder
		 (binder, Cols.SUBTITLE, this.getSubtitle());
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.STATUS, this.getStatus());
		
		CCLAUtils.addQueryDateParamToBinder(binder,
		 Cols.DATE_OPENED, this.getDateOpened());
		
		CCLAUtils.addQueryParamToBinder
		 (binder, Cols.SHARE_CLASS, this.getShareClass());
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.REQ_SIGNATURES, this.getRequiredSignatures());
		
		CCLAUtils.addQueryParamToBinder
		 (binder, Cols.INC_DIST_METHOD, this.getIncomeDistMethod());
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.MANDATED_ACCOUNT, this.getMandatedAccId());
		
		CCLAUtils.addQueryBooleanParamToBinder
		 (binder, Cols.IS_EXCLUSIVE, this.isExclusiveBankAccount());
		
		CCLAUtils.addQueryParamToBinder
		 (binder, Cols.AML_CHECK_OVERRIDE_USER, this.getAmlCheckOverrideUser());
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.ACCOUNT_TYPE, this.getAccountType());
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.LOAN_TYPE, this.getLoanType());
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.LOAN_TERM, this.getLoanTerm());
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.AGREEMENT_TYPE, this.getAgreementType());
		
		CCLAUtils.addQueryParamToBinder
		 (binder, Cols.ACC_ACCOUNT_CODE, this.getAccAccountCode());
	}

	public void persist(FWFacade facade, String username) throws DataException {
		persist(facade, username, true);
	}
	
	public void persist(FWFacade facade, String username, boolean validate) throws DataException {
		if (validate)
			this.validate(facade);
		
		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);
		DataResultSet beforeData = Account.getData(this.getAccountId(), facade);
		
		facade.execute("qClientServices_UpdateAccount", binder);
		
		DataResultSet afterData = Account.getData(this.getAccountId(), facade);

		// Add audit record
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(this.getAccountId(), ElementType.ACCOUNT.getName());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.UPDATE.toString(), 
		 ElementType.ACCOUNT.getName(), 
		 beforeData, afterData, auditRelations);
	}

	/** Sets Account attributes with the restrict flag set. */
	public void setAttributes(DataBinder binder) throws DataException {
		setAttributes(binder, true);
	}
	
	/** Sets Account attributes, based on values from the passed DataBinder.
	 *  
	 *  If the restrict flag is set, only a subset of attributes will be set.
	 *  This prevents 'static' fields (such as ext. acc. number) being updated
	 *  by users.
	 *  
	 *  All standard updates coming from the UI should be using this restricted
	 *  method.
	 *  
	 * @param binder
	 * @param restrict
	 * @throws DataException
	 */
	public void setAttributes(DataBinder binder, boolean restrict) 
	 throws DataException {
		
		if (!restrict) {
			// Restricted fields
			if (!StringUtils.stringIsBlank(binder.getLocal(Cols.FUND_CODE)))	
				this.setFundCode(binder.getLocal(Cols.FUND_CODE));
			
			this.setAmlCheckOverrideUser(binder.getLocal(Cols.AML_CHECK_OVERRIDE_USER));
		}
		
		// Freely-updatable fields
		this.setAuroraAccount(
		 CCLAUtils.getBinderBoolValue(binder, Cols.AURORA_ACCOUNT));
		
		this.setSubtitle(binder.getLocal(Cols.SUBTITLE));
		
		if (!StringUtils.stringIsBlank(binder.getLocal(Cols.STATUS)))	
			this.setStatus(CCLAUtils.getBinderIntegerValue(binder, Cols.STATUS));

		this.setRequiredSignatures(
		 CCLAUtils.getBinderIntegerValue(binder, Cols.REQ_SIGNATURES));
		
		this.setIncomeDistMethod(binder.getLocal(Cols.INC_DIST_METHOD));
		
		this.setMandatedAccId(
		 CCLAUtils.getBinderIntegerValue(binder, Cols.MANDATED_ACCOUNT));
		
		this.setExclusiveBankAccount(
		 CCLAUtils.getBinderBoolValue(binder, Cols.IS_EXCLUSIVE));
		
		this.setAccountType(
			CCLAUtils.getBinderIntegerValue(binder, Cols.ACCOUNT_TYPE));
		
		this.setLoanType(
			CCLAUtils.getBinderIntegerValue(binder, Cols.LOAN_TYPE));
		this.setLoanTerm(
			CCLAUtils.getBinderIntegerValue(binder, Cols.LOAN_TERM));
		
		this.setAgreementType(
			CCLAUtils.getBinderIntegerValue(binder, Cols.AGREEMENT_TYPE));
		
		this.setAccAccountCode(binder.getLocal(Cols.ACC_ACCOUNT_CODE));
	}

	public void validate(FWFacade facade) throws DataException {
		
		// If the Fund's Income Typecode ID is 2 (i.e. Accumulation Fund), force the
		// Income Dist Method to REIN.
		if (!StringUtils.stringIsBlank(this.getFundCode())) {
			Fund fund = Fund.getCache().getCachedInstance(this.getFundCode());
			
			if (fund.getIncomeTypeCode().getIncomeTypeCodeId() 
				== FundIncomeTypeCode.Ids.ACCUMULATION) {
				this.setIncomeDistMethod(IncomeDistMethod.REIN.toString());
			}
		}
		
		// If account type is 'Loan', ensure that both loan fields are non-null.
		if (this.getAccountType() == AccountType.LOAN) {
			if (this.getLoanType() == null || this.getLoanTerm() == null)
				throw new DataException
				 ("Loan account is missing Loan Type/Loan Term values");
		}
		
		boolean forceValidateAuroraAccounts = false;
		SystemConfigVar config = SystemConfigVar.getCache().getCachedInstance
		 (com.ecs.ucm.ccla.Globals.VALIDATE_AURORA_ACCOUNTS);
		
		if (config!=null && config.getBoolValue()!=null && config.getBoolValue()) 
			forceValidateAuroraAccounts = true;
		
		if (this.isAuroraAccount() && forceValidateAuroraAccounts)
			AuroraAccountHandler.validateAuroraAccount(this, true, false, facade);
	}
	
	/** Returns a ResultSet containing the number of accounts linked to the given
	 *  person, grouped by Account Type.
	 *  
	 * @param entityId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getPersonAccountTotalsData
	 (int personId, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "parentElementId", personId);
		
		return facade.createResultSet
		 ("qClientServices_GetRelatedAccountTotalsByType", binder);
	}
	
	/** Returns a ResultSet containing the number of accounts belonging to the given
	 *  org, grouped by Account Type.
	 *  
	 * @param entityId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getClientAccountTotalsData
	 (int entityId, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "parentElementId", entityId);
		
		return facade.createResultSet
		 ("qClientServices_GetRelatedAccountTotalsByType", binder);
	}
	
	/** Fetches account data for the given Client/Entity. */
	public static DataResultSet getClientAccountsData
	 (int entityId, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "parentElementId", entityId);
		
		return facade.createResultSet
		 ("qClientServices_GetRelatedAccounts", binder);
	}

	/** Fetches account data for the given Client/Entity. */
	public static DataResultSet getClientAccountsDataByStatus
	 (int entityId, int accountStatusId, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "parentElementId", entityId);
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.STATUS, accountStatusId);
		
		return facade.createResultSet
		 ("qClientServices_GetRelatedAccountsByStatus", binder);
	}	
	
	/** Fetches account data for the given Client/Entity with status and fund as passed. 
	 **/
	public static DataResultSet getClientAccountsDataByFund
	 (int entityId, String fundCode, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "parentElementId", entityId);
		CCLAUtils.addQueryParamToBinder
		 (binder, Cols.FUND_CODE, fundCode);		
		
		return facade.createResultSet
		 ("qClientServices_GetRelatedAccountsByFund", binder);
	}
	
	/** Fetches account data for the given Client/Entity with status and fund as passed. 
	 **/
	public static DataResultSet getClientAccountsDataByStatusFund
	 (int entityId, int accountStatusId, String fundCode, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "parentElementId", entityId);
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.STATUS, accountStatusId);
		CCLAUtils.addQueryParamToBinder
		 (binder, Cols.FUND_CODE, fundCode);		
		
		return facade.createResultSet
		 ("qClientServices_GetRelatedAccountsByStatusFund", binder);
	}
	
	/** Fetches account data for the given Client/Entity with fund as passed. 
	 * 
	 **/
	public static DataResultSet getClientAccountsDataByFundType
	 (int entityId, String fundCode, FWFacade facade)
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "parentElementId", entityId);
		CCLAUtils.addQueryParamToBinder
		 (binder, Cols.FUND_CODE, fundCode);

		return facade.createResultSet
		 ("qClientServices_GetRelatedAccountsByFund", binder);
	}
	
	/** Fetches account data for the given Client/Entity with fund and account 
	 *  type as passed. 
	 * 
	 **/
	public static DataResultSet getClientAccountsDataByFundType
	 (int entityId, String fundCode, int accountTypeId, FWFacade facade)
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "parentElementId", entityId);
		CCLAUtils.addQueryParamToBinder
		 (binder, Cols.FUND_CODE, fundCode);
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.ACCOUNT_TYPE, accountTypeId);		
		
		DataResultSet rs = facade.createResultSet
		 ("qClientServices_GetRelatedAccountsByFundType", binder);
		
		Log.debug("Fetched Accounts for Org ID: " + entityId + ", Fund Code: " + 
		 fundCode + ", Account Type ID: " + accountTypeId + ". Found " + rs.getNumRows() 
		 + " results");
		
		return rs;
	}
	
	/** Fetches account data for the given Client/Entity. */
	public static DataResultSet getOpenClientAccountsData
	 (int entityId, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Entity.Cols.ID, entityId);
		
		return facade.createResultSet
		 ("qClientServices_GetOpenClientAccounts", binder);
	}	
	
	
	/** Fetches account data for the given Client/Entity. */
	public static DataResultSet getOpenAccountsDataByFund
	 (String fundCode, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryParamToBinder
		 (binder, Cols.FUND_CODE, fundCode);
		
		return facade.createResultSet
		 ("qClientServices_GetOpenAccountsByFund", binder);
	}		
	
	
	/** Fetches data for a single account which maps to the given
	 *  Aurora Client Number, account number and fund code.
	 *  
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getAccountByAuroraValues
	 (int clientNumber, int accountNumber, String fundCode,
	  FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "clientNumber", clientNumber);
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "accountNumber", accountNumber);
		
		binder.putLocal("fundCode", fundCode);
		
		DataResultSet rsAccount = facade.createResultSet
		 ("qClientServices_GetAccountByAuroraValues", binder);
		
		return rsAccount;
	}
	
	/** Takes a ResultSet of data from the CCLA_ACCOUNT table and formats
	 *  the cash/units values to include minimum decimal places, commas
	 *  etc.
	 *  
	 * @param rsAccounts
	 * @throws DataException 
	 */
	public static DataResultSet formatResultSet(DataResultSet rsAccounts) 
	 throws DataException {
		
		if (rsAccounts.isEmpty())
			return rsAccounts;
		
		rsAccounts.first();
		
		int cashFieldIndex = -1, unitsFieldIndex = -1;
		
		// Work out positions of cash/units fields.
		for (int i=0; i<rsAccounts.getNumFields(); i++) {
			String thisField = rsAccounts.getFieldName(i);
			
			if (thisField.equals("ACC_CASH"))
				cashFieldIndex = i;
			else if (thisField.equals("ACC_UNITS"))
				unitsFieldIndex = i;
		}
		
		DecimalFormat cashFormat = new DecimalFormat();
		cashFormat.setMinimumFractionDigits(2);
		
		DecimalFormat unitsFormat = new DecimalFormat();
		unitsFormat.setMinimumFractionDigits(2);
		
		do {
			String cashValueStr = rsAccounts.getStringValue(cashFieldIndex);
			if(!StringUtils.stringIsBlank(cashValueStr)) {
				Double cashValue	= Double.parseDouble(cashValueStr);

				rsAccounts.setCurrentValue
					(cashFieldIndex, cashFormat.format(cashValue));
			} else {
				rsAccounts.setCurrentValue(cashFieldIndex, "");
			}
			
			String unitsValueStr = rsAccounts.getStringValue(unitsFieldIndex);
			if(!StringUtils.stringIsBlank(unitsValueStr)){
				Double unitsValue		= Double.parseDouble(unitsValueStr);
				
				rsAccounts.setCurrentValue
				 (unitsFieldIndex, unitsFormat.format(unitsValue));
			}else{
				rsAccounts.setCurrentValue(unitsFieldIndex, "");
			}
		} while (rsAccounts.next());
		
		rsAccounts.first();
		return rsAccounts;
	}
	
	/** Returns the raw data from the Element Relations table for all 
	 *  Person-Account relations relating to the passed list of Accounts.
	 *  
	 * @param accounts
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getPersonRelationsData
	 (Vector<Account> accounts, FWFacade facade) 
	 throws DataException {
		
		Log.debug("Fetching all Account-Person relations for " 
		 + accounts.size() + " accounts");
		
		DataResultSet rsPersonRelations = null;
		
		for (int i=0; i<accounts.size(); i++) {
			
			DataResultSet rsAccountPersonRelations = 
			 accounts.get(i).getPersonRelationsData(facade);
			
			Log.debug("Found " + rsAccountPersonRelations.getNumRows() + 
			 " person relations for account " + accounts.get(i).getAccNumExt());
			
			if (rsPersonRelations == null) {
				rsPersonRelations = new DataResultSet();
				rsPersonRelations.copy(rsAccountPersonRelations);
			} else {
				
				if (rsAccountPersonRelations.first()) {
					do {
						rsPersonRelations.addRow(
						 rsAccountPersonRelations.getCurrentRowValues());
					} while (rsAccountPersonRelations.next());
				}
			}
		}
		
		Log.debug("Returning ResultSet of " + rsPersonRelations.getNumRows() 
		 + " Account-Person relations");
		
		return rsPersonRelations;
	}
	
	/** See getRelatedPersons(boolean, FWFacade) method for description.
	 *  
	 *  Fetches related persons without their associated Contact details.
	 *  
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public HashMap<RelationName, Vector<Person>> getRelatedPersons(FWFacade facade) 
	 throws DataException {
		return getRelatedPersons(false, facade);
	}
	
	/** Returns a mapping of relation names to lists of Person instances
	 *  for this account.
	 *  
	 *  e.g. Correspondent 	-> Person A
	 *  	 Signatory		-> Person A, Person B, Person C
	 *  	 Trustee		-> Person C, Person D
	 *  
	 *  The same Person may appear more than once across the returned Person 
	 *  lists, e.g. they are a Correspondent and Signatory for the account.
	 *  
	 *  If getContacts is true, the Person instances are loaded with their associated
	 *  Contact details available via their getContacts() method.
	 *  
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public HashMap<RelationName, Vector<Person>> getRelatedPersons
	 (boolean getContacts, FWFacade facade) 
	 throws DataException {
		RelationType relType = RelationType.getCache().getCachedInstance
		 (RelationType.PERSON_ACCOUNT);
		
		Vector<Relation> relations =
		 Relation.getRelations(null, this.getAccountId(), relType, null, facade);
		
		HashMap<RelationName, Vector<Person>> relatedPersons = 
		 new HashMap<RelationName, Vector<Person>>();
		
		HashMap<Integer, Person> persons = new HashMap<Integer, Person>();
		
		for (Relation relation: relations) {			
			RelationName relationName = relation.getRelationName();
			
			int personId = relation.getElementId1();
			Person person = null;
			
			if (persons.containsKey(personId))
				person = persons.get(personId); // Already fetched this Person record.
			else {
				// Must fetch new Person record from DB.
				person = Person.get(relation.getElementId1(), getContacts, facade);
				persons.put(person.getPersonId(), person);
			}
			
			if (relatedPersons.containsKey(relationName)) {
				Vector<Person> relPersons = relatedPersons.get(relationName);
				relPersons.add(person);
				
			} else {
				Vector<Person> relPersons = new Vector<Person>();
				relPersons.add(person);
				
				relatedPersons.put(relationName, relPersons);
			}
		}

		return relatedPersons;
	}
	
	/** Returns a mapping between correspondent Person IDs and accounts
	 *  for the given list of accounts.
	 *  
	 *  Specialised function used when building Mandate forms etc.
	 *  
	 * @param accounts
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static HashMap<Integer, Vector<Account>> 
	 getCorrespondentAccountMap(Vector<Account> accounts, FWFacade facade) 
	 throws DataException {
		
		HashMap<Integer, Vector<Account>> corrAccountMap = 
		 new HashMap<Integer, Vector<Account>>();
		
		RelationType relType = RelationType.getCache().getCachedInstance
		 (RelationType.PERSON_ACCOUNT);
		
		for (Account account:accounts) {
			 // Fetch the correspondent relationship for this account.
			RelationName relName = 
			 RelationName.getCache().getCachedInstance
			 (RelationName.PersonAccountRelation.CORRESPONDENT);
			
			DataResultSet rsRelationData = Relation.getRelationData
			 (null, account.getAccountId(), relType, relName, facade);
			 
			 if (rsRelationData.isEmpty())
				 throw new DataException("No Correspondent found for account: " 
				  + account.getAccNumExt());
			 
			 // Correspondent's Person ID will be stored in the ELEMENT_ID1 column
			 // in the returned ResultSet.
			 Integer thisCorrId = CCLAUtils.getResultSetIntegerValue
			  (rsRelationData, "ELEMENT_ID1");
			 
			 if (corrAccountMap.containsKey(thisCorrId)) {
				 Vector<Account> corrAccounts = corrAccountMap.get(thisCorrId);
				 
				 // Append to existing account list for this correspondent
				 corrAccounts.add(account);
				 
			 } else {
				 // Create new account list for this correspondent
				 Vector<Account> corrAccounts = new Vector<Account>();
				 corrAccounts.add(account);
				 
				 corrAccountMap.put(thisCorrId, corrAccounts);
			 }
			 
		 }
		
		return corrAccountMap;
	}
	
	/** Returns the raw data from the Element Relations table for all 
	 *  Person-Account relations for this account.
	 *  
	 * @param accounts
	 * @return
	 * @throws DataException 
	 */
	public DataResultSet getPersonRelationsData(FWFacade facade) 
	 throws DataException {
		
		// Fetch all Person-Account relations from the Relations
		// table for this account.
		RelationType relType = 
		 RelationType.getCache().getCachedInstance(RelationType.PERSON_ACCOUNT);
		
		return Relation.getRelationData(null, this.getAccountId(), 
		 relType, null, facade);
	}
	
	/** Returns a set of Account instances for each row of the
	 *  passed ResultSet.
	 *  
	 * @param rsAccounts
	 * @return
	 * @throws NumberFormatException
	 * @throws DataException
	 */
	public static Vector<Account> getAccounts(DataResultSet rsAccounts) 
	 throws NumberFormatException, DataException {
		
		Vector<Account> accounts = new Vector<Account>();

		if (rsAccounts.first()) {
			do {
				accounts.add(get(rsAccounts));
			} while (rsAccounts.next());
		}
		
		return accounts;
	}
	
	/** Fetches related bank account data for a given Account.
	 *  
	 *  This includes the relation data for each bank account, i.e.
	 *  whether it is nominated for withdrawal/income.
	 *  
	 * @param accountId
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getBankAccountRelationsData
	 (int accountId, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.ID, accountId);
		
		return facade.createResultSet(
		 "qClientServices_GetBankAccountRelationsByAccount", binder);
	}
	

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	
	public boolean isAuroraAccount() {
		return auroraAccount;
	}

	public void setAuroraAccount(boolean auroraAccount) {
		this.auroraAccount = auroraAccount;
	}

	public int getAccountNumber() {
		return accountNum;
	}

	public void setAccountNumber(int accountNumber) {
		this.accountNum = accountNumber;
	}

	public String getFundCode() {
		return fundCode;
	}

	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}

	public String getAccNumExt() {
		return accNumExt;
	}

	public void setAccNumExt(String accNumExt) {
		this.accNumExt = accNumExt;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public int getStatus() {
		return statusId;
	}

	public void setStatus(int statusId) {
		this.statusId = statusId;
	}

	public Date getDateOpened() {
		return dateOpened;
	}

	public void setDateOpened(Date dateOpened) {
		this.dateOpened = dateOpened;
	}

	public String getShareClass() {
		return shareClass;
	}

	public void setShareClass(String shareClass) {
		this.shareClass = shareClass;
	}

	public Integer getRequiredSignatures() {
		return requiredSignatures;
	}

	public void setRequiredSignatures(Integer requiredSignatures) {
		this.requiredSignatures = requiredSignatures;
	}

	public String getIncomeDistMethod() {
		return incomeDistMethod;
	}

	public void setIncomeDistMethod(String incomeDistMethod) {
		this.incomeDistMethod = incomeDistMethod;
	}

	public Integer getMandatedAccId() {
		return mandatedAccId;
	}

	public void setMandatedAccId(Integer mandatedAccId) {
		this.mandatedAccId = mandatedAccId;
	}

	public boolean isExclusiveBankAccount() {
		return exclusiveBankAccount;
	}

	public void setExclusiveBankAccount(boolean exclusiveBankAccount) {
		this.exclusiveBankAccount = exclusiveBankAccount;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	
	/** Returns a single AuroraClient instance for the given Account.
	 *  <br/>
	 *  A call is first made to find all mapped Aurora Client IDs for
	 *  the accounts' owning Entity ID. If there is no matches, the
	 *  method returns null.
	 *  <br/>
	 *  If there is 1 mapped Aurora Client ID for the owning Entity, 
	 *  this is returned immediately.
	 *  <br/>
	 *  In the more complex case, multiple mapped Aurora Client IDs
	 *  may be found. These must be checked in turn against the passed
	 *  external account number to see which one matches to the given
	 *  Account.
	 * 
	 * @param accNumExt
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static AuroraClient getAuroraClient
	 (String accNumExt, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		binder.putLocal("ACCNUMEXT", accNumExt);
		
		DataResultSet rsAuroraClientMap = 
		 facade.createResultSet(
		  "qClientServices_GetAuroraClientsByAccNumExt", binder);
		
		if (rsAuroraClientMap.first()) {
			
			if (rsAuroraClientMap.getNumRows() == 1) {
				AuroraClient auroraClient = 
				 AuroraClient.get(rsAuroraClientMap);
				
				Log.debug("Found single Aurora Client for acc. num. ext " 
				 + accNumExt + ": " + auroraClient.toString());
			}
			// Ambiguous case: more than one Aurora client
			// mapped to the accounts' owning Entity. Cross-check
			// each Aurora Client Number against the external
			// account number prefix.
			Log.debug("Found multiple Aurora Clients for acc. num. ext "
			 + accNumExt);
			
			do {
				AuroraClient auroraClient = 
				 AuroraClient.get(rsAuroraClientMap);
				
				String paddedClientNumber = 
				 CCLAUtils.padClientNumber
				 (Integer.toString(auroraClient.getClientNumber()));

				// Check if this client number is a prefix of the
				// passed external acc. number.
				if (accNumExt.startsWith(paddedClientNumber)) {
					Log.debug("Found number with matching prefix: " +
					 auroraClient.toString());
					
					return auroraClient;
				} else {
					Log.debug("Found number without matching prefix: " +
					 auroraClient.toString());
				}
				
			} while (rsAuroraClientMap.next());
		}
		
		Log.debug("No matching Aurora Clients for acc. num. ext: " + 
		 accNumExt);
		
		return null;
	}
	
	/** 
	 *  @deprecated works fine for any accounts imported from Aurora originally, but
	 *  won't work with newly-generated accounts, because the AccNumExts are incorrect.
	 *  <br/>
	 *  Returns the padded account number string using a substring
	 *  of the external account number. This ensures that account numbers
	 *  are always padded correctly, as they can be of arbitrary length!
	 *  <br/>
	 *  The clientNumber parameter should correspond the the Aurora
	 *  Client Number which this account corresponds to.
	 *  
	 * @return
	 */
	public String getAccountNumberString(int clientNumber) {
		
		String paddedClientId = CCLAUtils.padClientNumber(
		 Integer.toString(clientNumber));
		
		return this.getAccNumExt().substring(paddedClientId.length());
	}
	
	/** Returns the legacy-format Account Number string.
	 *  
	 *  This is the Account Number suffixed with the Fund Code. 
	 * 
	 *  The zero-padding length for the Account Number is determined by the Company that
	 *  owns the account's Fund.
	 * 
	 *  Currently, all CBF accounts are padded to a minimum of 3 characters, all other
	 *  company accounts are padded to a minimum of 4 characters.
	 * 
	 * @return
	 * @throws DataException 
	 */
	public String getAccountNumberString() throws DataException {
		Fund fund = Fund.getCache().getCachedInstance(this.getFundCode());
		return getAccountNumberStringWithPadding(fund.getCompany());
	}
	
	/**
	 * Returns the Account Number (zero-padded to 8 characters) suffixed with the Fund
	 * Code.
	 * <br/>
	 * This is the 'new' format for Account Number/Fund Code strings, but isn't 
	 * compatible with Aurora/Workflow yet. It should only be used for display purposes
	 * in UCM screens.
	 * 
	 * @return
	 */
	public String getExtendedAccountNumberString() {		
		return StringUtils.padString(
				String.valueOf(this.getAccountNumber()), 
				Globals.ACCOUNT_NUMBER_PADDED_CHAR,
				Globals.ACCOUNT_NUMBER_PADDED_LENGTH)+
				(this.getFundCode()==null?"":this.getFundCode());
	}
	
	/** Returns the Account Number and Fund Code, with the Account Number padded with
	 *  the given number of zeros.
	 *  
	 *  e.g. 	passing in 4 would yield '0001C' or '0003AB'
	 *  		passing in 3 would yield '001C' or '003AB'
	 *  
	 * @param padding
	 * @return
	 */
	private String getAccountNumberStringWithPadding(int padding) {
		return StringUtils.padString(
				String.valueOf(this.getAccountNumber()), 
				Globals.ACCOUNT_NUMBER_PADDED_CHAR,
				padding)
				+
				this.getFundCode();
	}
	
	/** Returns the Account Number and Fund Code, with the Account Number padded to
	 *  differing lengths depending on the passed Company.
	 *  
	 *  CBF is padded to 3 digits, everyone else 4.
	 *  
	 * @param company
	 * @return
	 * @throws DataException 
	 */
	public String getAccountNumberStringWithPadding(Company company) 
	 throws DataException {
		
		return CCLAUtils.getPaddedAccountNumber
		 (this.getAccountNumber(), 
		 Fund.getCache().getCachedInstance(this.getFundCode()));
	}
	
	/** Determines whether the passed Aurora client number matches the
	 *  external account number prefix.
	 *  
	 * @param clientNumber
	 * @return true if the given padded clientNumber is a prefix of the
	 * 				account's external account number.
	 */
	public boolean isAccountClientNumber(int clientNumber) {
		String paddedClientNumber = CCLAUtils.padClientNumber(
		 Integer.toString(clientNumber));
		
		return this.getAccNumExt().startsWith(paddedClientNumber);
	}
	
	public void setAmlCheckOverrideUser(String amlCheckOverrideUser) {
		this.amlCheckOverrideUser = amlCheckOverrideUser;
	}

	public String getAmlCheckOverrideUser() {
		return amlCheckOverrideUser;
	}
	
	public static IVSCheck getEmptyIVSCheck() 
	 throws DataException {
		IVSCheck ivsCheck = new IVSCheck();
		return ivsCheck;
	}
	
	/** Determines the IVS check result for this account.
	 *  <br/>
	 *  The first thing it does is check for an applied IVS Check Override attribute
	 *  on the account. If present, returns true immediately with the 'override' flag
	 *  set in the IVSCheck instance.
	 *  <br/>
	 *  This involves fetching all related persons tied to this account and
	 *  checking their relationships against a list. This will result in a
	 *  distinct list of Persons who must have passed Identity Checking to
	 *  allow a PASSED result to be returned.
	 *  <br/>
	 *  If one or more related Persons have failed, a FAILED result is returned, along 
	 *  with a reason for the failure. If no related Persons are available for checking,
	 *  i.e. the relationships are not present, this will also yield a FAILED result.
	 *  <br/>
	 *  Otherwise, the account must be linked to a mixture of 1 or more Persons with
	 *  an Unknown identity check status and 0 or more Persons with a Passed check
	 *  status. In this case, an UNKNOWN result is returned.
	 *  <br/>
	 *  Hard-code special KYC requirements for now:
	 *  
	 *  -PC accounts only require person PEP checks, no need to check correspondent
	 *   status
	 *  -Orgs with 'Nominee' category don't require ANY person checks.
	 * 
	 * @param org 	the account's owning Organisation. Should be resolved prior to
	 * 				calling the method, or it will fail immediately.
	 * @return
	 * @throws DataException 
	 */
	public IVSCheck getIVSCheck(Entity org, FWFacade facade) 
	 throws DataException {
		
		Log.debug("Determining IVS Check Status for account ID: " 
		 + this.getAccountId());
		
		if (org == null)
			throw new DataException("Unable to resolve IVS Check result: " +
			 "passed Org was missing");
		
		IVSCheck ivsCheck = new IVSCheck();
		
		// Check for an override attribute against the account.
		Vector<ElementAttributeApplied> ivsOverrides = ElementAttributeApplied.getAll
		 (this.getAccountId(), 
		 ElementAttributeType.getCache().getCachedInstance
		  (ElementAttributeType.ACCOUNT_IVS_CHECKING), 
		  true, facade);
		
		if (!ivsOverrides.isEmpty()) {
			// At least 1 override attribute set. Return a positive check result.
			Log.debug("Account has at least one override flag set. Returning positive "
			 + "check result");
			
			ivsCheck.statusCode = IVSStatusCode.PASSED;
			ivsCheck.passReason = "IVS Check Override enabled";
			ivsCheck.override 	= true;
			
			return ivsCheck;
		}
		
		if (org.getCategoryId() != null 
			&& 
			(org.getCategoryId() == OrganisationCategory.CategoryIds.NOMINEE)) {
			
			Log.debug("Owning Organisation is a Nominee client. Skipping IVS Checks");
			
			ivsCheck.statusCode = IVSStatusCode.PASSED;
			ivsCheck.passReason = "Client is a Nominee, no IVS checking required";
			
			return ivsCheck;
		}
		
		// Create HashMap to store list of persons requiring IVS check
		HashMap<Integer,Person> reqPersons = new HashMap<Integer,Person>();
		
		HashMap<RelationName, Vector<Person>> relPersons = 
		 this.getRelatedPersons(facade);
		
		// Determine which relations to check.
		int[] checkRelations = null;
		
		// Flag indicating whether persons are checked for PEP risk ONLY. Applies
		// to PC/PI PSDF funds currently.
		boolean pepCheckOnly = false;
		
		if (this.getFundCode().equals(Fund.FundCodes.PC.toString())
			||
			this.getFundCode().equals(Fund.FundCodes.PI.toString())) {
			// PSIC Fund Codes currently use a special relation check list.
			checkRelations = RequiredIVSRelations.PSDF_REQ_IVS_RELATIONS;
			pepCheckOnly = true;
			
		} else {
			// All other accounts use the default relations list for now.
			checkRelations = RequiredIVSRelations.DEFAULT_REQ_IVS_RELATIONS;
		}
		
		// Collect list of unique related Persons with relationships to account that
		// require IVS check.
		for (RelationName relationName : relPersons.keySet()) {
			for (int i=0; i<checkRelations.length; i++) {
				// Check if this relationship type must be checked as part of
				// IVS
				if (relationName.getRelationNameId() == checkRelations[i]) {
					Vector<Person> persons = relPersons.get(relationName);
					
					Log.debug("Found " + persons.size() + 
					 " Persons with relationship: " 
					 + relationName.getRelation() + " who must be IVS checked.");
					
					for (Person person : persons) {
						// Add all persons to the required list, if they aren't
						// already there.
						if (!reqPersons.containsKey(person.getPersonId())) {
							reqPersons.put(person.getPersonId(), person);
						}
					}
				}
			}
		}
		
		Log.debug("Found " + reqPersons.size() + " total related persons " +
		 "who must be IVS checked");
		
		// New update logic 07/09/12, if there are no people satisfying the criteria, 
		//then return AMBER as opposed to FAILED
		if (reqPersons.isEmpty()) {
			Log.debug("No related persons to check, returing amber result");
			
			ivsCheck.statusCode = IVSStatusCode.UNKNOWN;
			ivsCheck.failReason = "No authorising persons linked to the account. " +
			 "There must be at least one specified.";
//			ivsCheck.failReason = "";
			return ivsCheck;
		}
		
		Vector<Person> unknownPersons 	= new Vector<Person>();
		Vector<Person> failedPersons 	= new Vector<Person>();
		
		// Determine ID Check Status for each required person.
		for (Person person : reqPersons.values()) {
			Log.debug("Checking person IVS status: " + person.getFullName());
			int idCheckResult = IdentityCheck.FAILED;
			
			// Fetch all Person IVS checking attributes.
			Vector<ElementAttributeApplied> personAttrs = ElementAttributeApplied.getAll
			 (person.getPersonId(), 
			 ElementAttributeType.getCache().getCachedInstance
			  (ElementAttributeType.PERSON_IVS_CHECKING),
			 false, facade);
			
			// Relevant Person check attributes
			ElementAttributeApplied idChecked = null, 
									amlChecked = null, 
									pepChecked = null,
									accountOverride = null,
									pepOverride = null;
			
			// Scoop up relevant applied Attributes for this person, where applicable
			for (ElementAttributeApplied personAttr : personAttrs) {
				
				// We are only interested in boolean attributes
				if (personAttr.getStatus() != null) {
					switch (personAttr.getAttributeId()) {
						case ElementAttribute.PersonAttributes.IDENTITY_CHECKED : {
							// 'Identity Checked' attribute
							idChecked = personAttr;
							break;
						}
						case ElementAttribute.PersonAttributes.PEP_CHECKED : {
							// 'PEP Checked' attribute
							pepChecked = personAttr;
							break;
						}
						case ElementAttribute.PersonAttributes.ACCOUNT_IVS_OVERRIDE : {
							// Check for special Account-level ID Check override
							accountOverride = personAttr;
							break;
						}
						case ElementAttribute.PersonAttributes.PEP_CHECK_OVERRIDE : {
							// Check for special Account-level PEP Check override
							pepOverride = personAttr;
							break;
						}
						case ElementAttribute.PersonAttributes.AML_TRACKER_CHECKED : {
							// Check for legacy AML Tracker check attribute 
							
							if (AuthenticationScoreUtils.AML_TRACKER_CHECK_ENABLED)
								amlChecked = personAttr;
						}
						
						default : { break; }
					}
				}
			}
			
			
			if ((idChecked != null && idChecked.getStatus())
				||
				(amlChecked != null && amlChecked.getStatus())
				||
				(accountOverride != null && accountOverride.getStatus())) {
				// Any of these flags indicate an immediate pass.
				idCheckResult = IdentityCheck.PASSED;
				
			} else if (pepCheckOnly) {
				// Check for PEP-only attributes, if applicable
				
				if (pepOverride != null && pepOverride.getStatus()) {
					// PEP override is set
					idCheckResult = IdentityCheck.PASSED;
				} else if (pepChecked != null) {
					// Check state of PEP-Checked attribute
					if (pepChecked.getStatus()) {
						idCheckResult = IdentityCheck.PASSED;
					} else {
						idCheckResult = IdentityCheck.FAILED;
					}
				} else {
					idCheckResult = IdentityCheck.UNCHECKED;
				}
				
			} else if (idChecked != null && !idChecked.getStatus()) {
				// An explicit ID check fail has been recorded
				idCheckResult = IdentityCheck.FAILED;
				
			} else if (idChecked == null) {
				// No ID-Check attribute set, assume unchecked.
				idCheckResult = IdentityCheck.UNCHECKED;
				
			// Get the old-fashioned identity check result	
			} else {
				idCheckResult = AuthenticationScoreUtils.getIdentityCheckResult
				 (person.getPersonId(), facade);
			}
			
			Log.debug("Outcome for Person ID: " + person.getPersonId() + ", Name: " +
			 person.getFullName() +
			 "idCheckResult: " + idCheckResult + ", " +
			 "idChecked: " + idChecked + ", " +
			 "amlChecked: " + amlChecked + ", " +
			 "pepChecked: " + pepChecked + ", " +
			 "accountOverride: " + accountOverride + ", " +
			 "pepOverride: " + pepOverride);
			
			if (idCheckResult == IdentityCheck.FAILED) {
				failedPersons.add(person);
			} else if (idCheckResult == IdentityCheck.UNCHECKED) {
				unknownPersons.add(person);
			}
		}
		
		Log.debug("Out of " + reqPersons.size() + ", " + failedPersons.size() + 
		 " have failed IVS and " + unknownPersons.size() + " have unknown IVS status");
		
		if (!failedPersons.isEmpty()) {
			ivsCheck.statusCode = IVSStatusCode.FAILED;
			String failReason = "";
			
			Log.debug("1 or more persons with failed IVS status - " +
			 "resolved Account IVS check as " + ivsCheck.statusCode.toString());
			
			if (failedPersons.size() > 0) {
				for (Person person : failedPersons) {
					if (failReason.length() > 0)
						failReason += ", ";
					
					failReason += person.getSalutation();
				}
				
				failReason = failedPersons.size() + 
				 "/" + reqPersons.size() + " " +
				 "person(s) failed IVS checking: " + failReason;
			}
			
			ivsCheck.failReason = failReason;
			
		} else if (!unknownPersons.isEmpty()) {
			ivsCheck.statusCode = IVSStatusCode.UNKNOWN;
			String failReason = "";
			
			Log.debug("1 or more persons with unknown IVS status - " +
			 "resolved Account IVS check as " + ivsCheck.statusCode.toString());
			
			// Build the fail reason - list of unknown persons
			if (unknownPersons.size() > 0) {
				for (Person person : unknownPersons) {
					if (failReason.length() > 0)
						failReason += ", ";
					
					failReason += person.getSalutation();
				}
				
				failReason = unknownPersons.size() + 
				 "/" + reqPersons.size() + " " +
				 "person(s) haven't been IVS checked: " + failReason;
			}
			
			ivsCheck.failReason = failReason;
			
		} else {
			ivsCheck.statusCode = IVSStatusCode.PASSED;
			ivsCheck.failReason = null;
			
			Log.debug("No persons with failed/unknown IVS status - " +
			 "resolved Account IVS check as " + ivsCheck.statusCode.toString());
			
			// If it passes, remove the AML check override user, if applicable.
			if (!StringUtils.stringIsBlank(this.getAmlCheckOverrideUser())) {
				Log.debug("Removing previous AML check override user: " +
				 this.getAmlCheckOverrideUser());
				
				this.setAmlCheckOverrideUser(null);
				this.persist(facade, com.ecs.ucm.ccla.Globals.Users.System);
			}
		}
		
		return ivsCheck;
	}

	@Override
	public int getElementId() {
		return this.getAccountId();
	}
	
	@Override
	public ElementType getType() {
		return ElementType.ACCOUNT;
	}
	
	/**
	 * Returns a organisation id for a given account id.
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public Integer getOwnerOrganisationId(FWFacade facade) throws DataException {
		return getOwnerOrganisationId(this.getAccountId(), facade);
	}
	
	public static Integer getOwnerOrganisationId(int accountId, FWFacade facade) 
	 throws DataException {
		
		RelationName relName = 
		 RelationName.getCache().getCachedInstance
		 (RelationName.OrganisationAccountRelation.OWNER);
		
		Vector<Relation> existingRelations = 
		 Relation.getRelations(null, accountId, null, relName, facade);
		
		if(existingRelations.size() > 0){
			return existingRelations.get(0).getElementId1();
		} else {
			return null;
		}
	}
	
	/** Fetches the next available Account Number for the given Organisation
	 *  and Fund.
	 *  
	 *  This should always be used for supplying Account Numbers when creating 
	 *  new Accounts.
	 *  
	 * @param elementId
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static int getNextAccountNumberForOrgAndFund
	 (int organisationId, FWFacade facade, String fundcode, int accountType) 
	 throws DataException {
		
		if (accountType == AccountType.LOAN) {
			// Loan accounts are currently allocated out from a single global sequence.
			int loanAccNum = 
			 Integer.parseInt(CCLAUtils.getNewKey("LoanAccountNumber", facade));
			
			Log.debug("Fetched next Loan Account Number in sequence: " + loanAccNum);
			return loanAccNum;
		} else {
			DataBinder binder = new DataBinder();
			CCLAUtils.addQueryIntParamToBinder
			 (binder, Entity.Cols.ID, organisationId);
			CCLAUtils.addQueryParamToBinder
			 (binder, Cols.FUND_CODE, fundcode);
			CCLAUtils.addQueryIntParamToBinder
			 (binder, Cols.ACCOUNT_TYPE, accountType);
			
			DataResultSet rsMaxAccountNumber =
			 facade.createResultSet
			  ("qClientServices_GetMaxAccountNumberForOrgAndFundAndAccountType", 
			  binder);
			
			Integer maxNo = CCLAUtils.getResultSetIntegerValue
			 (rsMaxAccountNumber, "MAX_ACCOUNTNUMBER");
				
			if (maxNo == null) {
				// No existing accounts for this Org/Fund.
				return 1;
			} else {
				return (maxNo + 1);
			}
		}
	}	
	
	/** Fetches the highest account numbers per Fund/Account Type for the given 
	 *  Organisation.
	 *  
	 *  Should be used when allocating 'special' account numbers, which must be
	 *  reserved across the entire org/fund holdings, e.g. Comm. First Donor Accounts.
	 *  
	 * @param orgId
	 * @param facade
	 * @return a ResultSet with 3 columns: FUND_CODE, ACCOUNT_TYPE_ID, MAX_ACCOUNTNUMBER
	 * @throws DataException
	 */
	public static DataResultSet getMaxAccountNumbersForOrg
	 (int orgId, FWFacade facade) throws DataException {
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Entity.Cols.ID, orgId);
		
		DataResultSet rsMaxAccountNumbers =
		 facade.createResultSet
		  ("qClientServices_GetMaxAccountNumbersForOrg", binder);
		
		return rsMaxAccountNumbers;
	}
	
	/** Fetches a the unit value of an account
	 *  
	 * @param elementId
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static String getAccountUnits(int accountId, FWFacade facade)
	  throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.ID, accountId);

		DataResultSet rsUnits =
		 facade.createResultSet
		  ("qClientServices_GetAccountValue", binder);
		
		if (rsUnits.isEmpty())
		{
			return null;
		} else 
		{
			do {
				String units = DataResultSetUtils.getResultSetStringValue(rsUnits, "ACC_UNITS");
				return units;
			} while (rsUnits.next()) ;
		}
	}

	/** Fetches a the cash value of an account
	 *  
	 * @param elementId
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static String getAccountCash(int accountId, FWFacade facade)
	  throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.ID, accountId);

		DataResultSet rsUnits =
		 facade.createResultSet
		  ("qClientServices_GetAccountValue", binder);
		
		if (rsUnits.isEmpty())
		{
			return null;
		} else 
		{
			do {
				String units = DataResultSetUtils.getResultSetStringValue(rsUnits, "ACC_CASH");
				return units;
			} while (rsUnits.next()) ;
		}
	}	

	/**
	 * @deprecated use new getAccountByIndexingValues() method instead.
	 * 
	 * @param clientNumStr
	 * @param accountNumStr
	 * @param facade
	 * @return
	 * @throws ServiceException
	 * @throws DataException
	 */
	public static DataResultSet getAccountByIndexingValues(String clientNumStr,
			 String accountNumStr, FWFacade facade) 
			 throws ServiceException, DataException {
		
		return getAccountByIndexingValues(clientNumStr, accountNumStr, false, facade);
	}
	
	/**
	 * Returns a ResultSet of Account data for the account with the
	 * given Aurora Client Number and Account Number.
	 * 
	 * The Account Number is expected to be suffixed with the Fund code, i.e.
	 * '0001C' 
	 * 
	 * @param clientNumStr
	 * @param accountNumStr
	 * @param facade
	 * @return rsAccount
	 * @throws ServiceException if the corresponding account isn't found, or
	 * 			the request values are malformed
	 * @throws DataException
	 */
	public static DataResultSet getAccountByIndexingValues(String clientNumStr,
	 String accountNumStr, boolean extendedData, FWFacade facade) 
	 throws ServiceException, DataException {
		
		DataResultSet rsAccount = null;
		
		// Get up to the last 3 A-Z characters of account number
		// i.e. the Fund Code
		String fundCode = CCLAUtils.getSuffixChars
		 (accountNumStr);
	
		if (fundCode.length() == 0) {
			throw new ServiceException("Account number '" + 
			 accountNumStr + "' did not contain a Fund Code. " +
			 "Unable to resolve Account.");
		}
		
		String numStr = accountNumStr.substring
		 (0, accountNumStr.length() - fundCode.length());
		
		Integer accountNum = null;
		
		try {
			accountNum = Integer.parseInt(numStr);
		} catch (NumberFormatException ne) {
			throw new ServiceException("Expected numeric prefix " +
			"in account number '" + accountNumStr + 
			"'. Unable to resolve Account.");
		}
		
		int clientNum = Integer.parseInt(clientNumStr);
		
		rsAccount = Account.getDataByAuroraValues
		 (clientNum, accountNum, fundCode, extendedData, facade);
		
		if (rsAccount.isEmpty())
			throw new ServiceException("Unable to find account " +
			 "with Client Number: " + clientNum + 
			 ", Account Number: " + accountNum + ", Fund: " +
			 fundCode);
		
		return rsAccount;
	}
	
	/**
	 * Returns a ResultSet of Account data for the account belonging to the given
	 * Organisation ID, with the given Account Number.
	 * 
	 * The Account Number is expected to be suffixed with the Fund code, i.e.
	 * '0001C' 
	 * 
	 * @param clientNumStr
	 * @param accountNumStr
	 * @param facade
	 * @return rsAccount
	 * @throws ServiceException 
	 * @throws ServiceException if the corresponding account isn't found, or
	 * 			the request values are malformed
	 * @throws DataException 
	 * @throws DataException
	 */
	public static Account getAccountByIndexingValues(int orgId, String accountNumStr, 
	 boolean extendedData, FWFacade facade) throws ServiceException, DataException {
		
		// Get up to the last 3 A-Z characters of account number
		// i.e. the Fund Code
		String fundCode = CCLAUtils.getSuffixChars
		 (accountNumStr);
	
		if (fundCode.length() == 0) {
			throw new ServiceException("Account number '" + 
			 accountNumStr + "' did not contain a Fund Code. " +
			 "Unable to resolve Account.");
		}
		
		String numStr = accountNumStr.substring
		 (0, accountNumStr.length() - fundCode.length());
		
		Integer accountNum = null;
		
		try {
			accountNum = Integer.parseInt(numStr);
		} catch (NumberFormatException ne) {
			throw new ServiceException("Expected numeric prefix " +
			"in account number '" + accountNumStr + 
			"'. Unable to resolve Account.");
		}
		
		DataResultSet rsAccount = Account.getDataByValues
		 (orgId, accountNum, fundCode, extendedData, facade);
		
		if (rsAccount.isEmpty())
			throw new ServiceException("Unable to find Organisation account " +
			 "with Account Number: " + accountNum + ", Fund: " +
			 fundCode);
		
		return get(rsAccount);
	}
	
	/**
	 * This method will find out if an account already  has a value in the ACCOUNT_VALUE_AUDIT
	 * table for a particular day
	 * @throws DataException 
	*/
	public static boolean hasAccountValueAudit(int accountId, Date accountAuditDate, 
	 FWFacade facade) throws DataException {
		boolean hasAudit = true;
		DataBinder binder = new DataBinder();
		BinderUtils.addIntParamToBinder(binder, Cols.ID, accountId);
		BinderUtils.addDateParamToBinder(binder, "ACCOUNT_VALUE_DATE", accountAuditDate);
		
		DataResultSet rsAccValAudit = facade.createResultSet
		 ("qTransactions_GetHasAccountAudit", binder);
		
		if (rsAccValAudit.isEmpty())
			return false;
		else 
			return hasAudit;
	}
	
	/** Returns the share class that is being applied to an account.  This can be retrieved from
	 * the share class group an account belongs to (if it is overridden), the share class override for
	 * the individual account, or the account itself.
	 * 
	 * @param accountId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Integer getTrueShareClass(int accountId, FWFacade facade) 
	throws DataException
	{
		Integer shareClassId = null;
		Account account = Account.get(accountId, facade);
		// get share class group account belongs to
		// get share class group if it exists
		ShareClassGroup group = ShareClassGroup.getByAccountId(accountId, facade);
		if (group != null)
		{
			if (group.isOverridden())
				shareClassId = group.getShareClassId();		
		}		
		
		if (shareClassId == null)
		{
		// get share class override
		ShareClassOverride sco = ShareClassOverride.get(accountId, facade);
			if (sco!=null)
			{
				shareClassId = sco.getShareClassId();
			}
		}
		
		// get share class applied to account
		if (shareClassId == null)
		{
			String accountShareClass = account.getShareClass();
			if (!StringUtils.stringIsBlank(accountShareClass))
				shareClassId = Integer.parseInt(accountShareClass);
		}
		
		return shareClassId;		
	}
	
	/**
	 *  Returns the nominated withdrawal/income Bank Account for the given Account ID.
	 *  
	 *  RelationName MUST be either:
	 *   
	 *   -RelationName.AccountBankAccountRelation.INCOME
	 *   -RelationName.AccountBankAccountRelation.WITHDRWAL
	 *   
	 * @param incomeOrWithdrawal
	 * @return
	 * @throws DataException 
	 */
	public static BankAccount getNominatedBankAccount
	 (int accountId, RelationName incomeOrWithdrawal, FWFacade facade)
	 throws DataException {

		// if relationName is not income or withdrawal throw an error for extra safety!
		if (!(incomeOrWithdrawal.getRelationNameId()== 
			RelationName.AccountBankAccountRelation.INCOME
			||
			incomeOrWithdrawal.getRelationNameId()== 
				RelationName.AccountBankAccountRelation.WITHDRAWAL)) {
			throw new DataException("RelationName: " + incomeOrWithdrawal.getRelation()
			 + " is not valid");
		}
		
		// Found a matching Account. Fetch all linked withdrawal/income Bank Accounts
		Vector<Relation> relatedBankAccounts = Relation.getRelations
		 (accountId, null, 
		 RelationType.getCache().getCachedInstance
		  (RelationType.ACCOUNT_BANKACCOUNT), 
		  incomeOrWithdrawal, facade);
		
		Log.debug("Found " + relatedBankAccounts.size() + 
		 " related " + incomeOrWithdrawal.getRelation() + " Bank Accounts");
			
		return getNominatedBankAccount(relatedBankAccounts, facade);
	}
	
	/** Returns a Bank Account instance which is flagged as 'nominated' in the passed
	 *  ResultSet. 
	 *  <br/>
	 *  The ResultSet must have been sourced from Relation.getAccountBankAccountsData(),
	 *  as this method returns a ResultSet with the IS_NOMINATED_ACCOUNT column set.
	 *  Either the nominated withdrawl or nominated income bank account is returned,
	 *  depending on the parameters fed into the previous method.
	 *  
	 * @param relatedBankAccounts
	 * @return
	 * @throws DataException 
	 */
	public static BankAccount getNominatedBankAccount
	 (DataResultSet rsBankAccounts) throws DataException {
		
		if (rsBankAccounts.first()) {
			do {
				boolean isNominated = CCLAUtils.getResultSetBoolValue
				 (rsBankAccounts, "IS_NOMINATED_ACCOUNT");
				
				if (isNominated) {
					// Found the nominated bank acc.
					return BankAccount.get(rsBankAccounts);
				}
				
			} while (rsBankAccounts.next());
		}
		
		// No nominated bank account.
		return null;
	}
		
	/**
	 * Returns the nominated bank account from the given list of bank account relations.
	 * <br/>
	 * This involves fetching the mapped Relation Properties for all the passed bank
	 * account relations, then stepping through to look for the 'default' property.
	 * If no such property is found, and 'strict' mode is on (see the flag 
	 * USE_STRICT_NOMINATED_BANK_ACCOUNT) the method returns null. If no property is
	 * found and 'strict' mode is off, the first relation is assumed to be the nominated
	 * account.
	 * <br/>
	 * The relatedBankAccounts list must feature only Withdrawal relations or Income
	 * relations for a single account, but not both.
	 *
	 * @param relatedBankAccounts
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static BankAccount getNominatedBankAccount(
	 Vector<Relation> relatedBankAccounts, FWFacade facade) 
	 throws DataException {
		
		Integer nominatedBankAccountId = null;

		// Return the default withdrawal account, or the first one
		// if a default withdrawal doesn't exist.
		if(relatedBankAccounts.size()>=1) {
			//get the relation properties for all the bank accounts
			HashMap<Integer, Vector<RelationPropertyApplied>> hm = 
				Relation.getRelationProperties(relatedBankAccounts, facade);
	    	Log.info("found relation Properties: "+hm.size());

			Iterator<Map.Entry<Integer, Vector<RelationPropertyApplied>>> it = 
				hm.entrySet().iterator();
			Integer relId = null;
			
			// Loop the relation properties
		    while (it.hasNext()) {
		        Map.Entry<Integer, Vector<RelationPropertyApplied>> pairs = 
		        	(Map.Entry<Integer, Vector<RelationPropertyApplied>>)it.next();
		        
		        Vector<RelationPropertyApplied> relProps = pairs.getValue();
		        //loop the relationPropertiesapplied to the relationproperty
		        for (RelationPropertyApplied relPropApplied : relProps) {
		        	
		        	Log.info("found relation properties applied:" + 
		        		relPropApplied.getPropertyValue()+" for property: "+
		        		relProps.toString());	
		        	
		        	if (relPropApplied.getRelationProperty().getProperty().getPropertyId() 
		        		== 
		        		Property.Ids.DEFAULT){
		        		relId = relPropApplied.getRelationId();
		        		break;
		        	}
				}
		    }
		  
			 // Found a nominated account 
		     if (relId != null){
			   for (Relation relBankAccount : relatedBankAccounts) {
				 if(relId==relBankAccount.getRelationId()){
					nominatedBankAccountId = relBankAccount.getElementId2();
					break;
				 } 
		 	   }
		     }
		}
		
		// If we aren't in strict mode, and no nominated account has been found, just
		// treat the first matched relation as the nominated account.
		if (!USE_STRICT_NOMINATED_BANK_ACCOUNT && (nominatedBankAccountId == null)) {
			for (Relation relBankAccount : relatedBankAccounts) {
				nominatedBankAccountId = relBankAccount.getElementId2();
				break;
			}
		}

		Log.debug("nominatedBankAccountId :"+(nominatedBankAccountId==null?"":nominatedBankAccountId));
		
		if (nominatedBankAccountId == null) {
			return null;
		} else {
			return BankAccount.get(nominatedBankAccountId, facade);
		}
	}
	
	/**
	 * This method will determine whether the bank account details on the given UCM 
	 * document is the nominated bank account for the CCLA account specified by the
	 * document metadata.
	 * 
	 * Required Binder Values
	 *  - dID
	 *  - TYPE_OF_ACCOUNT (either withdrawal or income)
	 * 
	 * Various flags are placed in the binder which indicate whether there was enough
	 * data to run the check (non-null account/bank account metadata values) and whether
	 * the bank details correspond to the nominated account.
	 * 
	 * @param binder
	 * @param facade
	 * @throws DataException 
	 * @throws Exception
	 */
	public static void isNominatedBankAccount(DataBinder binder, FWFacade facade) 
	 throws Exception {
		
		// use dID to obtain BankAccNum and SortCode stored on the document
		Integer dID = CCLAUtils.getBinderIntegerValue(binder, "dID");
		if (dID == null)
			throw new ServiceException("dID must be on the binder");
		
		String typeOfAccount = binder.getLocal("TYPE_OF_ACCOUNT");
		if (typeOfAccount == null)
			throw new ServiceException("TYPE_OF_ACCOUNT must be on the binder");
		
		// TODO: Ensure Dual Index data fetched here.
		LWDocument lwd = new LWDocument(dID, true);
		
		// fetch required fields
		String clientNumStr = lwd.getAttribute(com.ecs.ucm.ccla.Globals.UCMFieldNames.ClientNumber);
		String accountNumStr = lwd.getAttribute(com.ecs.ucm.ccla.Globals.UCMFieldNames.AccountNumber);			
		
		String docBankAccNum = lwd.getAttribute(com.ecs.ucm.ccla.Globals.UCMFieldNames.BankAccountNumber);
		String docSortCode = lwd.getAttribute(com.ecs.ucm.ccla.Globals.UCMFieldNames.SortCode);

		Log.info("clientNumStr: "+clientNumStr + " | accountNumStr: "+accountNumStr +
				" | docBankAccNum: "+docBankAccNum +" | docSortCode: "+docSortCode);
		
		// If all the values have been set
		if (!StringUtils.stringIsBlank(clientNumStr) 
				&& !StringUtils.stringIsBlank(accountNumStr)
				&& !StringUtils.stringIsBlank(docBankAccNum) 
				&& !StringUtils.stringIsBlank(docSortCode)){
		
			binder.putLocal("ENOUGH_DETAILS_FOR_CHECK","1");

			// Try to create a bank account object from the entered details
			Vector<BankAccount> docBankAccounts = 
			 BankAccount.getAllBySortCodeAndAccountNumber
			 (docBankAccNum, docSortCode, facade);
			
			// FAIL if docBankAccount = null
			if (docBankAccounts.isEmpty()) {
				binder.putLocal("NON_EXISTENT_BANK_ACCOUNT","1");
				return;
			}
							
			//Fetch account details
			Account account = Account.get(
			 Account.getAccountByIndexingValues
			 (clientNumStr,  accountNumStr, false, facade));
			
			RelationName accountType = null;
			if(typeOfAccount.equals("WITHDRAWAL")){
				accountType = RelationName.getCache().getCachedInstance(
						RelationName.AccountBankAccountRelation.WITHDRAWAL);
			} else if (typeOfAccount.equals("INCOME")){
				accountType = RelationName.getCache().getCachedInstance(
						RelationName.AccountBankAccountRelation.INCOME);
			}
			
			// Fetch all related withdrawal and income bank accounts, with the
			// nominated withdrawal or income account marked explicitly, if it exists.
			DataResultSet rsBankAccounts = Relation.getAccountBankAccountsData
			 (account.getAccountId(), accountType, facade);
			
			// Get the nominated bank account from the previous list of bank accounts
			// (may be null)
			BankAccount nomBankAcc = Account.getNominatedBankAccount(rsBankAccounts);	

			 if(nomBankAcc == null) {
				 binder.putLocal("NO_NOMINATED_BANK_ACCOUNT_FOUND","1");
			 } else {
				 // Loop through all matched bank accounts (with same sort code/acc no)
				 // If the nominated bank account is the same as the docAccount return 
				 // a pass
				 for (BankAccount docBankAccount : docBankAccounts) {
					 if (nomBankAcc.getBankAccountId() 
						== docBankAccount.getBankAccountId()) { 
						binder.putLocal("IS_NOMINATED_BANK_ACCOUNT","1");
						return;
					 }
				 }
			}
			 
			// The indexed Bank Account details don't match to the nominated bank 
			// account. Check if they match to any of the other linked bank accounts
			// (withdrawal or income)
			boolean isRelatedBankAccount = false;
			
			if (rsBankAccounts.first()) {
				do {
					int thisBankAccountId = CCLAUtils.getResultSetIntegerValue
					 (rsBankAccounts, "BANK_ACCOUNT_ID");
					
					 for (BankAccount docBankAccount : docBankAccounts) {
						 if (thisBankAccountId == docBankAccount.getBankAccountId()) { 
							isRelatedBankAccount = true;
							break;
						 }
					 }
					
				} while (rsBankAccounts.next());
			}
			
			if (isRelatedBankAccount) {
				binder.putLocal("IS_RELATED_BANK_ACCOUNT","1");
			} else {
				// The bank account exists in the database but it does not belong to
				// this account - return error flag
				binder.putLocal("NON_RELATED_BANK_ACCOUNT","1");
			}

		} else {
			// there isn't enough data on the document to check the bank account info
			binder.putLocal("ENOUGH_DETAILS_FOR_CHECK","0");
		}
	}

	/**
	 * Method just checks to see if the required binder params are present 
	 * for the nominated bank account method
	 * @param binder
	 * @return
	 */
	public static boolean checkBinderReqParamsForNomBankAcc(DataBinder binder) {
		if (StringUtils.stringIsBlank(binder.getLocal(UCMFieldNames.DocID))|| 
				StringUtils.stringIsBlank(binder.getLocal("TYPE_OF_ACCOUNT"))){
			Log.info("checkBinderReqParamsForNomBankAcc: returning false");
			return false;
		} else {
			Log.info("checkBinderReqParamsForNomBankAcc: returning true");
			return true;
		}
	}
	
	/** Returns a list of Person instances who have a direct Correspondent relationship
	 *  to the Account.
	 *  
	 *  Returns an empty list if no Correspondents set.
	 * 
	 * @param getContacts whether Contact details are loaded into the Person instances
	 * @return
	 * @throws DataException 
	 */
	public static Vector<Person> getCorrespondents
	 (int accountId, boolean getContacts, FWFacade facade) throws DataException {
		
		Vector<Person> correspondents = new Vector<Person>();
		
		// Fetch all mapped Org -> Person Correspondent relations.
		Vector<Relation> corrRelations = Relation.getRelations
		 (null, accountId, null, 
		 RelationName.getCache().getCachedInstance(
		  RelationName.PersonAccountRelation.CORRESPONDENT
		 ), facade);
		
		for (Relation corrRel : corrRelations) {
			Person thisCorr = Person.get(corrRel.getElementId1(), getContacts, facade);
			correspondents.add(thisCorr);
		}
		
		return correspondents;
	}
	
	/** Fetches the account's nominated Correspondent.
	 * 
	 *  If there are no set Correspondent relations, the method returns null.
	 * 
	 *  If there is only one Correspondent relation, this person is returned.
	 *  
	 *  In the case of multiple Correspondent relations, each relation is checked for
	 *  the explicit 'default/nominated' property. Providing a relation with this
	 *  property is found, this associated person is returned.
	 *  
	 *  If there are multiple correspondents and none have the nominated property, the
	 *  method returns null.
	 * 
	 * @param accountId
	 * @param getContacts
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Person getNominatedCorrespondent
	 (int accountId, boolean getContacts, FWFacade facade) 
	 throws DataException {
		
		RelationName corrRelName =  RelationName.getCache().getCachedInstance(
		 RelationName.PersonAccountRelation.CORRESPONDENT
		);
		
		// Fetch all mapped Org -> Person Correspondent relations.
		Vector<Relation> corrRelations = Relation.getRelations
		 (null, accountId, null, corrRelName, facade);
		
		if (corrRelations.isEmpty()) {
			Log.debug("No correspondent relations for Account ID " + accountId);
			return null;
			
		} else if (corrRelations.size() == 1) {
			Log.debug("Single correspondent found for Account ID " + accountId);
			return Person.get
			 (corrRelations.get(0).getElementId1(), getContacts, facade); 
			
		} else {
			Log.debug("Multiple correspondents found for Account ID " + accountId + 
			 ". Searching for Default/Nominated relation rroperty ");
			
			// Fetch all applied relation properties for the correspondent relations
			// to this account.
			DataResultSet rsRelPropAppl = RelationPropertyApplied.getByRelationsData
			 (null, accountId, null, corrRelName, facade);
			
			Vector<RelationPropertyApplied> relPropsAppl = 
			 RelationPropertyApplied.getAll(rsRelPropAppl);
			
			for (RelationPropertyApplied relPropAppl : relPropsAppl) {
				if (relPropAppl.getRelationProperty().getProperty().getPropertyId()
					==
					Property.Ids.DEFAULT) {
					
					for (Relation corrRel : corrRelations) {
						if (corrRel.getRelationId() == relPropAppl.getRelationId()) {
							Log.debug("Found explicitly nominated correspondent with " +
							 "Person ID " + corrRel.getElementId1());
							return Person.get
							 (corrRel.getElementId1(), getContacts, facade); 
						}
					}
				}
			}
			
			Log.debug("No explicitly nominated correspondent found");
			return null;
		}
	}	
		
	public Vector<Person> getCorrespondents(boolean getContacts, FWFacade facade) 
	 throws DataException {
		
		return getCorrespondents(this.getAccountId(), getContacts, facade);
	}

	public Integer getAccountType() {
		return accountType;
	}

	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}

	public Integer getLoanType() {
		return loanType;
	}

	public void setLoanType(Integer loanType) {
		this.loanType = loanType;
	}

	public Integer getLoanTerm() {
		return loanTerm;
	}

	public void setLoanTerm(Integer loanTerm) {
		this.loanTerm = loanTerm;
	}

	public void setAgreementType(Integer agreementType) {
		this.agreementType = agreementType;
	}

	public Integer getAgreementType() {
		return agreementType;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Account [accNumExt=");
		builder.append(accNumExt);
		builder.append("\n, accountId=");
		builder.append(accountId);
		builder.append("\n, accountNum=");
		builder.append(accountNum);
		builder.append("\n, accountType=");
		builder.append(accountType);
		builder.append("\n, agreementType=");
		builder.append(agreementType);
		builder.append("\n, amlCheckOverrideUser=");
		builder.append(amlCheckOverrideUser);
		builder.append("\n, auroraAccount=");
		builder.append(auroraAccount);
		builder.append("\n, dateOpened=");
		builder.append(dateOpened);
		builder.append("\n, exclusiveBankAccount=");
		builder.append(exclusiveBankAccount);
		builder.append("\n, fundCode=");
		builder.append(fundCode);
		builder.append("\n, incomeDistMethod=");
		builder.append(incomeDistMethod);
		builder.append("\n, lastUpdated=");
		builder.append(lastUpdated);
		builder.append("\n, loanTerm=");
		builder.append(loanTerm);
		builder.append("\n, loanType=");
		builder.append(loanType);
		builder.append("\n, mandatedAccId=");
		builder.append(mandatedAccId);
		builder.append("\n, requiredSignatures=");
		builder.append(requiredSignatures);
		builder.append("\n, shareClass=");
		builder.append(shareClass);
		builder.append("\n, statusId=");
		builder.append(statusId);
		builder.append("\n, subtitle=");
		builder.append(subtitle);
		builder.append("]");
		return builder.toString();
	}
	
	/**
	 * Generates the aurora accnumext based on the exact logic used by Kainos.
	 * 
	 * Requires constituent Client Number fields set.
	 * 
	 * Deprecated in favour of AuroraAccountHandler.buildAccNumExt()
	 * 
	 * @param acc
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public @Deprecated static String generateAccNumExt(Account acc, FWFacade facade) 
	 throws DataException {
		
		// Determine Fund
		Fund fund = Fund.getCache().getCachedInstance(acc.getFundCode());
		if (fund==null) {
			String msg = "Unable to generate Ext. Account Number, cannot find " +
			 "Fund for account";
			Log.error(msg);
			throw new DataException(msg);	
		}
		
		// Determine Company by the account's Fund
		Company company = fund.getCompany();
		if (company==null) {
			String msg = "Unable to generate Ext. Account Number, cannot find " +
			 "Company for account";
			Log.error(msg);
			throw new DataException(msg);	
		}
		
		// Find Aurora Client mapping for this Organisation
		Integer ownerOrgId = acc.getOwnerOrganisationId(facade);
		
		if (ownerOrgId == null) {
			String msg = "Unable to generate Ext. Account Number, owning Organisation " +
			 "is not set";
			Log.error(msg);
			throw new DataException(msg);
		}
		
		AuroraClient auroraClient = Entity.getAuroraClientCompanyMapping
		 (ownerOrgId, company, facade);
		
		if (auroraClient==null) {
			String msg = "Unable to generate Ext. Account Number, " + company.getCode() + 
			 "Client link not set against owning Organisation";
			Log.error(msg);
			throw new DataException(msg);
			
		}
		
		String accNumExt = null;
		
		String clientNumber = "";
		if (auroraClient.getClientNumber()!=null)
			clientNumber = auroraClient.getClientNumber().toString();
		
		String subDivisionCode = "";	
		if (auroraClient.getSubdivisionCode()!=null)
			subDivisionCode = auroraClient.getSubdivisionCode().toString();
		
		String contributorTypeCode = "";
		if (auroraClient.getContributorTypeCode()!=null)
			contributorTypeCode = auroraClient.getContributorTypeCode().toString();
		
		String contributorCode = "";
		if (auroraClient.getContributorCode()!=null)
			subDivisionCode = auroraClient.getContributorCode().toString();

		String accountNumber = String.valueOf(acc.getAccountNumber());
		String fundCode = fund.getFundCode();
		
		switch (company.getCompanyId()) 
		{
			case Company.CBF:
				accNumExt = StringUtils.padString(contributorTypeCode, '0', 1) + 
							StringUtils.padString(subDivisionCode, '0', 2) +
							StringUtils.padString(contributorCode, '0', 3) +
							StringUtils.padString(accountNumber, '0', 3) +
							fundCode;	
				break;
			case Company.LAMIT:
				accNumExt = StringUtils.padString(fundCode, '0', 2) +
							StringUtils.padString(clientNumber, '0', 5) +
							StringUtils.padString(accountNumber, '0', 2);
				break;
			case Company.PSIC:
				accNumExt = StringUtils.padString(clientNumber, '0', 6) +
							StringUtils.padString(accountNumber, '0', 4) +
							fundCode;
				break;
			default:
				//COIF and other.
				accNumExt = StringUtils.padString(clientNumber, '0', 5) +
							StringUtils.padString(accountNumber, '0', 4) +
							fundCode;
				break;
				
		}
		return accNumExt;
	}

	public void setAccAccountCode(String accAccountCode) {
		this.accAccountCode = accAccountCode;
	}

	public String getAccAccountCode() {
		return accAccountCode;
	}

	/** Compares two accounts for sorting purposes. Sorts by Account Type, Fund Code 
	 *  and then Account Number. 
	 *  
	 *  Only relevant when comparing accounts belonging to the same organisation!
	 */
	public int compareTo(Account account) {
		
		int accountTypeComparison = this.getAccountType().compareTo
		 (account.getAccountType());
		
		if (accountTypeComparison != 0)
			return accountTypeComparison;
		
		int fundCodeComparison = (this.getFundCode().compareTo(account.getFundCode()));
		
		if (fundCodeComparison != 0)
			return fundCodeComparison;
		else {
			if (this.getAccountNumber() > account.getAccountNumber())
				return 1;
			else if (this.getAccountNumber() < account.getAccountNumber())
				return -1;
			else
				return 0;
		}
	}

	public DataResultSet getAuroraCompaniesData(FWFacade facade)
			throws DataException {
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, Account.Cols.ID, this.getAccountId());
		
		return facade.createResultSet("qCore_GetAuroraCompaniesByAccountId", binder);
	}

	/** Only ever one Company for an Account - look it up via the Fund Code. */
	public Vector<Company> getAuroraCompanies(FWFacade facade)
			throws DataException {
		// TODO Auto-generated method stub
		
		Vector<Company> companies = new Vector<Company>();
		
		// If it isn't an Aurora Account, then there are no mapped Companies!
		if (this.isAuroraAccount()) {
			Company accCompany = Fund.getCache().getCachedInstance(
			 this.getFundCode()).getCompany();
			
			companies.add(accCompany);
		}
		
		return companies;
	}

	public String getId() {
		// TODO Auto-generated method stub
		return Integer.toString(this.getAccountId());
	}

	public String getComparisonLabel() {
		// TODO Auto-generated method stub
		return "Central DB Account/Aurora Account";
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.getAccNumExt();
	}
}
