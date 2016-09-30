package com.ecs.ucm.ccla.aurora;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.shared.SharedObjects;

import java.util.Calendar;
import java.util.Vector;

import com.aurora.webservice.AccountStatus;
import com.aurora.webservice.Client;
import com.aurora.webservice.IncomeDistributionMethods;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals;
import com.ecs.ucm.ccla.NumberToWords;
import com.ecs.ucm.ccla.aurora.compare.AccountFieldSet;
import com.ecs.ucm.ccla.aurora.compare.AuroraFieldSet;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.Account.IncomeDistMethod;
import com.ecs.ucm.ccla.data.AuroraClient;
import com.ecs.ucm.ccla.data.AuroraCorrespondent;
import com.ecs.ucm.ccla.data.BankAccount;
import com.ecs.ucm.ccla.data.Company;
import com.ecs.ucm.ccla.data.ElementAttribute;
import com.ecs.ucm.ccla.data.ElementAttributeApplied;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Fund;
import com.ecs.ucm.ccla.data.FundIncomeTypeCode;
import com.ecs.ucm.ccla.data.FundTypeCode;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.RelationName;
import com.ecs.ucm.ccla.data.SystemConfigVar;
import com.ecs.ucm.ccla.utils.ShareClassUtils;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/**
 * Helper class for Aurora Account instances
 * 
 * @author tm
 *
 */
public class AuroraAccountHandler extends AuroraEntityHandler
 <com.aurora.webservice.Account, Account> {
	
	/** Whether or not an Account must have a Withdrawal Bank Account before it can
	 *  be created/updated in Aurora
	 */
	public static final boolean REQUIRE_ACCOUNT_WITHDRAWAL_BANK_ACCOUNT =
	 SharedObjects.getEnvValueAsBoolean
	 ("CCLA_CommProc_RequireAuroraAccountWithdrawalBankAccount", false);
	
	/** Whether or not an Account must have an Income Bank Account before it can
	 *  be created/updated in Aurora
	 */
	public static final boolean REQUIRE_ACCOUNT_INCOME_BANK_ACCOUNT =
	 SharedObjects.getEnvValueAsBoolean
	 ("CCLA_CommProc_RequireAuroraAccountIncomeBankAccount", false);
	
	private static final String PSDF_FUND_CODE = 
		SharedObjects.getEnvironmentValue("PSDF_FUND_CODE");
	
	/** Maximum length of the Aurora Account Subtitle field.
	 *  
	 *  If a Central DB Account Subtitle exceeds this length, it is truncated before
	 *  being sent to Aurora
	 */
	public static final int MAX_SUBTITLE_LENGTH = 40;
	
	public static final int MAX_AURORA_BANK_ACC_NAME_LENGTH =
		SharedObjects.getEnvironmentInt("CCLA_CommProc_MaxAuroraBankAccountNameLength", 50);
	
	/** Converts an Inc Dist. Method code to its equivalent Aurora object.
	 * 
	 * @param incDistMethod
	 * @return
	 * @throws DataException 
	 */
	public static IncomeDistributionMethods getIncomeDistributionMethod
	 (String incDistMethod) throws DataException {
		
		if (incDistMethod.equals(IncomeDistMethod.PAYA.toString()))
			return IncomeDistributionMethods.PAYA;
		else if (incDistMethod.equals(IncomeDistMethod.REIN.toString()))
			return IncomeDistributionMethods.REIN;
		else if (incDistMethod.equals(IncomeDistMethod.RETN.toString()))
			return IncomeDistributionMethods.RETN;
		else if (incDistMethod.equals(IncomeDistMethod.TXRI.toString()))
			return IncomeDistributionMethods.TXRI;
		else
			throw new DataException("Unknown Income Dist. Method code: " + 
			 incDistMethod);
	}
	
	private static AccountStatus getAccountStatus(int accountStatusId) 
	 throws DataException {
		
		switch (accountStatusId) {
			case com.ecs.ucm.ccla.data.Account.AccountStatus.OPEN : {
				return AccountStatus.OPEN;
			}
			
			case com.ecs.ucm.ccla.data.Account.AccountStatus.CLOS : {
				return AccountStatus.CLOS;
			}
				
			case com.ecs.ucm.ccla.data.Account.AccountStatus.FROZ : {
				return AccountStatus.FROZ;
			}
			
			default : {
				throw new DataException("Unable to determine account status. Ensure " +
				 "it is either OPEN, CLOS or FROZ before applying to Aurora");
			}
		}
		
	}
	
	@Override
	public com.aurora.webservice.Account buildAuroraEntityInstance(
			Account account, Company company, FWFacade facade)
			throws DataException {
		
		validateDBInstance(account, company, facade);
		
		// Determine Fund
		Fund fund = Fund.getCache().getCachedInstance
		 (account.getFundCode());
		
		// Find Aurora Client mapping for this Organisation
		Integer ownerOrgId = account.getOwnerOrganisationId(facade);
		
		if (ownerOrgId == null) {
			String msg = "no owning Organisation set";
			
			Log.error(msg);
			throw new DataException(msg);
		}
		
		AuroraClient clMap = 
		 Entity.getAuroraClientCompanyMapping(ownerOrgId, company, facade);
		
		if (clMap == null) {
			// Fail if no Company mapping
			throw new DataException("no Aurora Client preferences set for " +
			 "Company " + company.getCode());
		}
		
		if (clMap.getContributorTypeCode() == null) {
			// Fail if the Contributor Type doesn't have a value set - this indicates
			// that the AuroraClient mapping hasn't been sync'ed to Aurora yet
			throw new DataException("Aurora Client preferences for " +
			 "Company " + company.getCode() + " must be synchronised with Aurora first");
		}
		
		// Fetch all mapped Account -> Person Correspondent relations.
		Person person = com.ecs.ucm.ccla.data.Account.getNominatedCorrespondent
		 (account.getAccountId(), false, facade);
		
		// Fail if no correspondent set
		if (person==null) {
			String msg = "No Correspondent/Nominated Correspondent set";
			
			Log.error(msg);
			throw new DataException(msg);
		}
		
		Integer corrPersonId = person.getPersonId();
		AuroraCorrespondent corrMap = null;
		
		// Fetch Aurora Correspondent mappings for Correspondent's Person ID
		Vector<AuroraCorrespondent> corrsMap = 
		 AuroraCorrespondent.getCorrespondentsByPersonId(corrPersonId, facade);
		
		corrMap = AuroraCorrespondent.getByCompany(corrsMap, company);
		
		// Fail if correspondent doesn't have Aurora Correspondent mapping
		if (corrMap == null) {
			String msg = "No mapped Correspondent preferences for " + 
			 "Company: " + company.getCode();
			
			Log.error(msg);
			throw new DataException(msg);
		} else if (corrMap.getCorrId() == null) {
			String msg = "Correspondent preferences for Company " + company.getCode() +
			 " not synchronized with Aurora. Ensure the Correspondent record has been" +
			 " created in Aurora first";
			
			Log.error(msg);
			throw new DataException(msg);
		}
		
		// Fetch payment (banking) details
		// ===============================
		// Fetch all related withdrawal and income bank accounts, with the
		// nominated withdrawal or income account marked explicitly, if it exists.
		
		RelationName withdrawalRel = RelationName.getCache().getCachedInstance(
		 RelationName.AccountBankAccountRelation.WITHDRAWAL
		);
		
		RelationName incomeRel= RelationName.getCache().getCachedInstance(
		 RelationName.AccountBankAccountRelation.INCOME
		);
		
		BankAccount nomWithdrawalAcc =
		 com.ecs.ucm.ccla.data.Account.getNominatedBankAccount
		 (account.getAccountId(), withdrawalRel, facade);
		
		BankAccount nomIncomeAcc =
		 com.ecs.ucm.ccla.data.Account.getNominatedBankAccount
		 (account.getAccountId(), incomeRel, facade);
		
		// Fetch mandated account, if it exists.
		com.ecs.ucm.ccla.data.Account mandatedAccount = null;
		
		if (account.getMandatedAccId() != null) {
			mandatedAccount = com.ecs.ucm.ccla.data.Account.get
			 (account.getMandatedAccId(), facade);
		}
		
		// Now create the new Account instance
		com.aurora.webservice.Account newAccount = new com.aurora.webservice.Account();
		setDefaultValues(newAccount);
		
		// Set signatories.
		Vector<Person> sigs = AuroraSignatories.getPersonSignatoryList
		 (account, facade);
		
		Vector<AuroraSignatory> auroraSigs = AuroraSignatories.getSignatories(sigs);
		AuroraSignatories.applyToAuroraEntity(auroraSigs, newAccount);
		
		int numReqSigs = account.getRequiredSignatures();
		String multiSigsInfo = "";
		
		newAccount.setMultipleSignaturesIndicator((numReqSigs > 1));
		
		// No. of required signatures is stored as the actual word in Aurora for whatever
		// reason, i.e. 5 = "five"
		if (numReqSigs > 1)
			multiSigsInfo = NumberToWords.convert(numReqSigs);
	
		newAccount.setMultipleSignaturesInformation(multiSigsInfo);
		
		newAccount.setClientNumber(clMap.getClientNumber());
		newAccount.setAccountNumber(account.getAccountNumber());
		newAccount.setFundCode(account.getFundCode());
		
		newAccount.setContributorCode(clMap.getContributorCode());
		newAccount.setContributorTypeCode(clMap.getContributorTypeCode());
		newAccount.setSubDivisionCode(clMap.getSubdivisionCode());
		
		newAccount.setStatus(getAccountStatus(account.getStatus()));
		
		// Corr. code
		newAccount.setCorrespondentCode(corrMap.getCorrId());
		
		// Ensure Account subtitle doesn't exceed max Aurora length.
		String trimmedSubtitle = "";
		
		if (account.getSubtitle() != null)
			trimmedSubtitle = account.getSubtitle();
		
		if (trimmedSubtitle.length() > MAX_SUBTITLE_LENGTH)
			trimmedSubtitle = trimmedSubtitle.substring(0, MAX_SUBTITLE_LENGTH);
		
		newAccount.setSubtitle(trimmedSubtitle);
		
		newAccount.setIncomeDistributionMethod
		 (getIncomeDistributionMethod(account.getIncomeDistMethod()));

		if (mandatedAccount == null) {	
			// Always set the Mandated Company field, even if there is no Mandated Account 
			// set.
			newAccount.setMandatedCompany(company.getCode());
		} else {
			// Mandated Account set. Determine its AccNumExt using the Aurora web 
			// service. This account may be owned by a different client, in a different
			// Company, so have to look up all the data fresh.
			
			// Determine Fund
			Fund mandatedfund = Fund.getCache().getCachedInstance
			 (mandatedAccount.getFundCode());
			
			// Determine Company by the account's Fund
			Company mandatedCompany = mandatedfund.getCompany();
			
			// Find Aurora Client mapping for this Organisation
			Integer mandatedOwnerOrgId = mandatedAccount.getOwnerOrganisationId(facade);
			
			if (mandatedOwnerOrgId == null) {
				String msg = "No owning Organisation set for Mandated Account";
				
				Log.error(msg);
				throw new DataException(msg);
			}
			
			AuroraClient mandatedClMap = 
			 Entity.getAuroraClientCompanyMapping(mandatedOwnerOrgId, company, facade);
			
			if (mandatedClMap == null) {
				// Fail if no Company mapping
				throw new DataException("No Aurora Client preferences set for " +
				 "Mandated Organisation's Company " + mandatedCompany.getCode());
			}
			
			try {
				String mandatedAccNumExt = getAuroraAccNumExt
				 (mandatedfund.getFundCode(), mandatedClMap, 
				 mandatedAccount.getAccountNumber());
						
				newAccount.setMandatedAccount(mandatedAccNumExt);
				newAccount.setMandatedCompany(mandatedCompany.getCode());
				
			} catch (Exception e) {
				String msg = "Failed to resolve mandated AccNumExt for " +
				 "mandated Account ID " + mandatedAccount.getAccountId() + ": " + 
				 e.getMessage();
				
				Log.error(msg, e);
				throw new DataException(msg, e);
			}
		}
			
		// Withdrawal Banking details
		if (nomWithdrawalAcc != null) {
			Log.debug("Withdrawal ID :"+nomWithdrawalAcc.getBankAccountId());			
			Log.debug("Withdrawal Name :"+nomWithdrawalAcc.getAccountName());
			Log.debug("Withdrawal AccNo :"+nomWithdrawalAcc.getAccountNumber());			
			Log.debug("Withdrawal SortCode :"+nomWithdrawalAcc.getSortCode());			
			
			if (StringUtils.stringIsBlank(nomWithdrawalAcc.getShortName())) {
				throw new DataException("Nominated Withdrawal Bank Account does not " +
				 "have a Short Name set");
			}
			
			newAccount.setBankAccountNameWithdrawal
			 (nomWithdrawalAcc.getAccountName() != null ? 
			 CCLAUtils.truncateString(
			 nomWithdrawalAcc.getAccountName(),
			 AuroraAccountHandler.MAX_AURORA_BANK_ACC_NAME_LENGTH) : "");
			newAccount.setBankAccountNumberWithdrawal(
			 Integer.parseInt(nomWithdrawalAcc.getAccountNumber()));
			newAccount.setBankSortCodeWithdrawal(
			 Integer.parseInt(nomWithdrawalAcc.getSortCode()));
			newAccount.setAccountShortNameOrBuildingSocietyReferenceWithdrawal
			 (nomWithdrawalAcc.getShortName() != null ? nomWithdrawalAcc.getShortName() : "");
			
		} else if (REQUIRE_ACCOUNT_WITHDRAWAL_BANK_ACCOUNT) {
			throw new DataException("Account must have a nominated withdrawal " +
			 "Bank Account set");
		}
		
		// Income Banking details
		if (nomIncomeAcc != null) {
			Log.debug("Income ID :"+nomIncomeAcc.getBankAccountId());			
			Log.debug("Income Name :"+nomIncomeAcc.getAccountName());
			Log.debug("Income AccNo :"+nomIncomeAcc.getAccountNumber());			
			Log.debug("Income SortCode :"+nomIncomeAcc.getSortCode());			
			
			if (StringUtils.stringIsBlank(nomIncomeAcc.getShortName())) {
				throw new DataException("Nominated Income Bank Account does not " +
				 "have a Short Name set");
			}
			
			newAccount.setBankAccountNameIncome
			 (nomIncomeAcc.getAccountName() != null ? 
			 CCLAUtils.truncateString(
			 nomIncomeAcc.getAccountName(), 
			 AuroraAccountHandler.MAX_AURORA_BANK_ACC_NAME_LENGTH) : "");
			
			newAccount.setBankAccountNumberIncome(
			 Integer.parseInt(nomIncomeAcc.getAccountNumber()));
			
			newAccount.setBankSortCodeIncome(
			 Integer.parseInt(nomIncomeAcc.getSortCode()));
			
			newAccount.setAccountShortNameOrBuildingSocietyReferenceIncome
			 (nomIncomeAcc.getShortName() != null ? nomIncomeAcc.getShortName() : "");
			
		} else if (REQUIRE_ACCOUNT_INCOME_BANK_ACCOUNT) {
			throw new DataException("Account must have a nominated income " +
			 "Bank Account set");
		}
		
		
		Calendar dateOpenedCal = Calendar.getInstance();
		dateOpenedCal.setTime(account.getLastUpdated());
		
		Calendar lastUpdatedCal = Calendar.getInstance();
		lastUpdatedCal.setTime(account.getLastUpdated());

		// Set these date values, although they won't be picked up by Aurora.
		newAccount.setDateOpened(dateOpenedCal);
		newAccount.setLastAmendmentDate(lastUpdatedCal);
		
		// AccNumExt (must be calculated by Aurora web service)
		try {
			String accNumExt = getAuroraAccNumExt
			 (account.getFundCode(), clMap, account.getAccountNumber());
			
			newAccount.setAccountNumberExternal(accNumExt);
		} catch (Exception e) {
			throw new DataException(e.getMessage(), e);
		}
		
		// Set fields based on Element Attribute values
		Vector<ElementAttributeApplied> accAttribs = ElementAttributeApplied.getAll
		 (account.getAccountId(), false, facade);
		
		boolean standingTransactions = false;
		boolean internalAccount = false;
		
		for (ElementAttributeApplied accAttrib : accAttribs) {
			
			// Standing Transactions attribute.
			if (accAttrib.getAttributeId() 
				== ElementAttribute.AccountAttributes.STANDING_TRANSACTIONS) {
				standingTransactions = accAttrib.getStatus();
			}
			
			// Internal Account attribute.
			if (accAttrib.getAttributeId() 
				== ElementAttribute.AccountAttributes.INTERNAL_ACCOUNT) {
				internalAccount = accAttrib.getStatus();
			}
		}
		
		// Apply Element Attributes to Aurora Account instance
		newAccount.setStandingTransactionsIndicator(standingTransactions);
		newAccount.setIsExternalAccount(!internalAccount);
		
		//Set the Share Class ID for PC accounts.
		if (fund.getFundCode().equals(PSDF_FUND_CODE)) {
			
			try {
				Integer shareClassId = 0;
				
				if (!StringUtils.stringIsBlank(account.getShareClass())) {
					shareClassId = Integer.parseInt(account.getShareClass());
				}
				
				if (shareClassId==0) {
					shareClassId = ShareClassUtils.getExpectedShareClass
					 (account.getAccountId(), null, facade);
				}
				
				if (shareClassId!=null && shareClassId!=0)
					newAccount.setShareClassCode(shareClassId);
				else 
					throw new DataException
					 ("Cannot find ShareClassId for account:"+account.getAccountId());
			} catch (NumberFormatException nfe) {
				Log.error("Cannot set ShareClass Id "+account.getShareClass());
			}
		}
		
		Log.debug("Generated Aurora Account Instance");
		return newAccount;
	}
	
	/** Calls an Aurora web service that builds an AccNumExt using the values of the
	 *  passed Account instance as parameters.
	 *  
	 *  The corresponding Account doesn't have to exist in Aurora to get a response.
	 *  
	 * @return
	 * @throws DataException 
	 */
	public static String getAuroraAccNumExt
	 (String fundCode, AuroraClient clMap, int accountNumber) throws DataException {
		try {
			if (AuroraWebServiceHandler.TEST_MODE) {
				return buildAccNumExt(fundCode, clMap, accountNumber);
			}
			
			Company company = Fund.getCache().getCachedInstance(fundCode).getCompany();
			
			Log.debug("Fetching AccNumExt value from Aurora. Company=" 
			 + company.getCode() + ", FundCode=" + fundCode + ", ClientNumber=" 
			 + clMap.getClientNumber() + ", AccountNumber=" + accountNumber);
			
			String accNumExt = AuroraWebServiceHandler.getAuroraWS()
			 .getPendingAccountNumberExternalByClientNumberAndAccountNumber
			 (company.getCode(), fundCode, clMap.getClientNumber(), 
			 accountNumber);
		
			if (accNumExt != null)
				accNumExt = accNumExt.trim();
			
			Log.debug("Aurora returned AccNumExt: " + accNumExt);
			
			return accNumExt;
			
		} catch (Exception e) {
			String msg = "Failed to fetch AccNumExt from Aurora: " + e.getMessage();
			Log.error(msg);
			throw new DataException(msg);
		}
	}
	
	/** Builds an Acc Num Ext value locally, based on known padding rules.
	 *  
	 * @param fundCode
	 * @param clientNumber
	 * @param accountNumber
	 * @return
	 * @throws DataException
	 */
	public static String buildAccNumExt
	 (String fundCode, AuroraClient clMap, int accountNumber) throws DataException {
		Company company = clMap.getCompany(); 
		
		Log.debug("Building AccNumExt value. Company=" 
		 + company.getCode() + ", FundCode=" + fundCode + ", ClientNumber=" 
		 + clMap.getClientNumber() + ", AccountNumber=" + accountNumber);
		
		String accNumExt = null;
		
		if (company.getCompanyId() == Company.LAMIT) {
			// LAMIT is weird, has Fund Code at the beginning.
			// [Fund Code][Padded Client Number][Padded Account Number]
			
			accNumExt = 
				fundCode + 
				clMap.getPaddedClientNumber() +
				CCLAUtils.padString
				 (Integer.toString(accountNumber), '0', company.getAccountNumberPadding());
			
		} else {
			// [Padded Client Number][Padded Account Number][Fund Code]
			accNumExt = 
				clMap.getPaddedClientNumber() + 
				CCLAUtils.padString
				 (Integer.toString(accountNumber), '0', company.getAccountNumberPadding()) +
				fundCode;
		}
		
		Log.debug("Built local AccNumExt value: " + accNumExt);
		return accNumExt;
	}
	
	@Override
	protected void setDefaultValues(com.aurora.webservice.Account account)
			throws DataException {
		account.setIVSReferenceNumber("");
		account.setMandateLetterSentDate("");
		account.setMandateLetterSentIndicator("");
		account.setMultipleSignaturesInformation("");
		account.setMandatedCompany("");
		account.setMandatedAccount("");
		account.setIVSCFID("");
	}

	@Override
	public void validateDBInstance(Account dbEntity, Company company,
	 FWFacade facade) throws DataException {
		
		boolean forceValidateAuroraAccounts = false;
		SystemConfigVar config = SystemConfigVar.getCache().getCachedInstance
		 (com.ecs.ucm.ccla.Globals.VALIDATE_AURORA_ACCOUNTS);
		
		if (config!=null && config.getBoolValue()!=null && config.getBoolValue()) 
			forceValidateAuroraAccounts = true;
		
		if (forceValidateAuroraAccounts)
			validateAuroraAccount(dbEntity, true, true, facade);
	}

	@Override
	public void validateAuroraInstance(
			com.aurora.webservice.Account auroraEntity, Company company,
			FWFacade facade) throws DataException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void updateAuroraEntity(Account dbEntity, Company company,
			Vector<String> fieldGroupNames, FWFacade facade)
			throws DataException, ServiceException {
		super.updateAuroraEntity(dbEntity, company, fieldGroupNames, facade);
		
		// Update local data
		com.aurora.webservice.Account auroraAccount = getExistingAuroraEntity
		 (dbEntity, company, facade);
		
		updateEntityFromAuroraData(dbEntity, auroraAccount, company, facade);
	}
	
	/** Post-Aurora creation/update hook.
	 *  
	 *  Sets the Date Opened and AccNumExt Account values to match Aurora.
	 * 
	 * @param account
	 * @param auroraAccount
	 * @param company
	 * @param facade
	 * @throws DataException
	 */
	private void updateEntityFromAuroraData
	 (Account account, com.aurora.webservice.Account auroraAccount, 
	 Company company, FWFacade facade) throws DataException {
		
		// Set the AccNumExt to match Aurora.
		String auroraAccNumExt = auroraAccount.getAccountNumberExternal().trim();
		
		Log.debug("Setting local DateOpened to match Aurora: " 
		 + auroraAccount.getDateOpened());
		account.setDateOpened(auroraAccount.getDateOpened().getTime());
		
		if (!auroraAccNumExt.equals(account.getAccNumExt())) {
			Log.debug("Updating local AccNumExt to match Aurora: " + auroraAccNumExt);
			account.setAccNumExt(auroraAccNumExt);
		}
		
		account.persist(facade, Globals.Users.System);
	}

	@Override
	public com.aurora.webservice.Account getExistingAuroraEntity(
			Account dbEntity, Company company, FWFacade facade)
			throws DataException {
		Log.debug("Attempting to fetch existing Aurora Account for Account ID: "
		 + dbEntity.getAccountId() + ", Company: " + company.getCode());
		
		//validateDBInstance(dbEntity, company, facade);
		
		Integer orgId = Account.getOwnerOrganisationId(dbEntity.getAccountId(), facade);
		
		if (orgId == null) {
			String msg = "No owner Organisation set for Account ID " + 
			 dbEntity.getAccountId();
			
			Log.error(msg);
			throw new DataException(msg);
		}
		
		AuroraClient clMap = Entity.getAuroraClientCompanyMapping(orgId, company, facade);
		
		if (clMap == null) {
			String msg = "No Aurora " + company.getCode() + " Client link set against " +
			 "Account's owner Organisation ID " + orgId;
			
			Log.error(msg);
			throw new DataException(msg);
		}

		com.aurora.webservice.Account existingAccount = null;

		if (AuroraWebServiceHandler.TEST_MODE) {
			Log.debug("Running in Web Service test mode.");
			SystemConfigVar testAccountId = SystemConfigVar.getCache().getCachedInstance
			 ("SDU_TestAccountId");
			
			if (testAccountId != null && testAccountId.getIntValue() != null) {
				Log.debug("Building test Aurora account instance from Org ID " 
				 + testAccountId.getIntValue());
				
				existingAccount = buildAuroraEntityInstance
				 (Account.get(testAccountId.getIntValue(), facade), company, facade);
			} else {
				Log.debug("No test Account ID found - returning null");
			}
		} else {
			try {
				String accNumExt = getAuroraAccNumExt(
					dbEntity.getFundCode(), 
					clMap, 
					dbEntity.getAccountNumber()
				);
				
				existingAccount = AuroraWebServiceHandler.getAuroraWS()
				 .getAccountByAccountNumberExternal(company.getCode(), accNumExt);
			
			} catch (Exception e) {
				// Assume error was thrown by Aurora, as Account did not exist.
				Log.debug("Failed to fetch existing Aurora Account: " + e.getMessage());
			}
		}
		
		if (existingAccount != null)
			Log.debug("Found existing Aurora Account with AccNumExt " 
			 + existingAccount.getAccountNumberExternal());
		else
			Log.debug("No existing Aurora Account found");
			
		return existingAccount;
	}

	
	/** Adds the Central DB entity to Aurora via web service call, providing it doesn't
	 *  exist there already.
	 *  
	 *  Overriden here to amend the Aurora account instance just before creation, to 
	 *  handle circular mandated account references.
	 *  
	 * @param entity
	 * @param company
	 * @param facade
	 * @throws ServiceException 
	 */
	@Override
	public void addAuroraEntity(Account account, Company company, FWFacade facade)
	 throws DataException, ServiceException {
		com.aurora.webservice.Account existingAuroraEntity = 
		 getExistingAuroraEntity(account, company, facade);
		
		if (existingAuroraEntity != null) {
			String msg = "Unable to add new Aurora entity - already exists in Aurora";
			Log.error(msg);
			throw new DataException(msg);
		}
	
		com.aurora.webservice.Account auroraEntity = 
		 buildAuroraEntityInstance(account, company, facade);
		
		validateAuroraInstance(auroraEntity, company, facade);
		
		// Override line here.
		amendMandatedAccountDetails(account, auroraEntity, facade);
		
		addToAurora(auroraEntity, company, facade);
		
		// Update local data
		com.aurora.webservice.Account auroraAccount = getExistingAuroraEntity
		 (account, company, facade);
		
		updateEntityFromAuroraData(account, auroraAccount, company, facade);
	}
	
	@Override
	protected Object addToAurora(com.aurora.webservice.Account auroraEntity,
			Company company, FWFacade facade) throws DataException {
		// Call Aurora Web Service to add the client.
		Log.debug("Creating Aurora Account: \n" + auroraEntity.toString());

		try {
			if (AuroraWebServiceHandler.TEST_MODE) {
				Log.debug("Web Service Test Mode: skipping call, returning null");
				return null;
			} else {
				String accNumExt = AuroraWebServiceHandler.getAuroraWS().createAccount
				 (company.getCode(), auroraEntity, false);
	
				return accNumExt;
			}
			
		} catch (Exception e) {
			String msg = "Failed to add new Aurora account: " + e.getMessage();
			
			Log.error(msg, e);
			throw new DataException(msg, e);
		}
	}

	@Override
	protected Object updateInAurora(com.aurora.webservice.Account auroraEntity,
			Company company, FWFacade facade) throws DataException {
		
		if (AuroraWebServiceHandler.TEST_MODE) {
			Log.debug("Web Service Test Mode: skipping call, returning true");
			return true;
		} else {
			try {
				return AuroraWebServiceHandler.getAuroraWS().amendAccount
				 (company.getCode(), auroraEntity);
				
			} catch (Exception e) {
				String msg = "Failed to update Aurora account: " + e.getMessage();
				
				Log.error(msg, e);
				throw new DataException(msg, e);
			}
		}
	}
	
	/** 
	 *  Must be called before adding a new account to Aurora.
	 * 
	 *  Checks that the designated mandated account, if set, actually exists in Aurora. 
	 *  If not, the Mandated Account reference must be removed from the account instance 
	 *  and some other details changed where necessary to ensure Aurora validation checks
	 *  pass and the account is created successfully.
	 *  
	 *  It is expected that a subsequent update-account instruction will 'fix' the data
	 *  and set the mandated account correctly.
	 *  
	 *  This shenanigans is required due to some account pairs referencing each other via
	 *  their mandated accounts (e.g. PSIC PC/PI account pairs)
	 * 
	 * @param auroraEntity
	 * @param facade
	 */
	protected void amendMandatedAccountDetails
	 (Account account, com.aurora.webservice.Account auroraAccount, FWFacade facade)
	 throws DataException {
		
		if (account.getMandatedAccId() == null)
			return; // Nothing needs to be done.
		
		Log.debug("amendMandatedAccountDetails::Account ID=" + account.getAccountId() +
		 ", Mandated Account ID=" + account.getMandatedAccId());
		
		// Fetch the Mandated Account.
		Account mandatedAccount = Account.get(account.getMandatedAccId(), facade);
		Vector<Company> companies = mandatedAccount.getAuroraCompanies(facade);
		
		if (companies.isEmpty()) {
			throw new DataException("Mandated Account doesn't appear to be an " +
			 "Aurora account");
		}
		
		com.aurora.webservice.Account mandatedAuroraAccount = getExistingAuroraEntity
		 (mandatedAccount, companies.get(0), facade);
			
		if (mandatedAuroraAccount != null)
			return; // Mandated Account already exists in Aurora, no need to touch it
		
		
		// OK, so the mandated account doesn't exist in Aurora yet. We'll need
		// to assume it will be created later.
		
		// To ensure this account creation still goes through, the mandated account
		// reference must be removed for now.
		
		Log.debug("Clearing Mandated Account reference, as mandated account does " +
		 "not exist in Aurora yet.");
		auroraAccount.setMandatedAccount("");
		
		//If the account income distribution is set to re-invest, set it to PAYA
		if (account.getIncomeDistMethod().equals
			(Account.IncomeDistMethod.REIN.toString())) {
			Log.warn("Changing Inc. Dist. Method to PAYA from REIN");
			
			auroraAccount.setIncomeDistributionMethod(getIncomeDistributionMethod(
			 Account.IncomeDistMethod.PAYA.toString()));

			// Set some dummy income bank account details if it has not been specified
			if (auroraAccount.getBankSortCodeIncome()==0 || 
				auroraAccount.getBankAccountNumberIncome()==0) {	
				Log.debug("Income bank details missing. Checking for nominated " +
				 "income bank account");
				
				// Try using the mandated account income details (if it exists)
				RelationName incomeRel= RelationName.getCache().getCachedInstance(
				 RelationName.AccountBankAccountRelation.INCOME);
						
				BankAccount nomIncomeAcc = Account.getNominatedBankAccount(
				 mandatedAccount.getAccountId(), incomeRel, facade);
				
				if (nomIncomeAcc!=null && 
					!StringUtils.stringIsBlank(nomIncomeAcc.getSortCode()) &&
					!StringUtils.stringIsBlank(nomIncomeAcc.getAccountNumber())) {
					
					Log.debug("Setting income bank details based on nominated " +
					 "Income bank account");
					
					auroraAccount.setBankSortCodeIncome(
							Integer.parseInt(nomIncomeAcc.getSortCode()));
					auroraAccount.setBankAccountNumberIncome(
							Integer.parseInt(nomIncomeAcc.getAccountNumber()));
				} 

				
				//If it is still empty, use dummy details.
				if ((auroraAccount.getBankSortCodeIncome()==0 || 
					auroraAccount.getBankAccountNumberIncome()==0)) {
					Log.debug("Setting income bank account details based on global "
					 + "defaults");
					
					SystemConfigVar dummyAccountNumber = 
					 SystemConfigVar.getCache().getCachedInstance(
					 Globals.CAP_INCOME_BANK_ACCOUNT_NUMBER+"_"+account.getFundCode());
					
					SystemConfigVar dummyAccountSortCode = 
					 SystemConfigVar.getCache().getCachedInstance(
					 Globals.CAP_INCOME_BANK_ACCOUNT_SORTCODE+"_"+account.getFundCode());
					
					if (dummyAccountSortCode!=null 
						&& dummyAccountSortCode.getIntValue()!=null
						&& dummyAccountNumber!=null 
						&& dummyAccountNumber.getIntValue()!=null) {
						
						Log.debug("Global defaults found: " + 
						 dummyAccountSortCode.getIntValue() + "-" +
						 dummyAccountNumber.getIntValue());
						
						auroraAccount.setBankSortCodeIncome
						 (dummyAccountSortCode.getIntValue());
						auroraAccount.setBankAccountNumberIncome
						 (dummyAccountNumber.getIntValue());
						auroraAccount
						 .setAccountShortNameOrBuildingSocietyReferenceIncome
						 ("Capital Income Acc "+account.getFundCode());
						
					} else {
						String errorMsg = "Cannot find default capital income " +
						 "account details. Please contact the Administrator.";
						
						Log.error(errorMsg);
						throw new DataException(errorMsg);
					}	
				}
			}
		}
	}

	@Override
	public AuroraFieldSet<Account, com.aurora.webservice.Account> getAuroraFieldSet(
			com.aurora.webservice.Account auroraEntity) throws DataException {
		return new AccountFieldSet(auroraEntity);
	}

	public static class AccountValidationOutcome {
		private boolean valid;
		private String errorMsg;
		
		public AccountValidationOutcome(boolean valid, String errorMsg) {
			this.valid = valid;
			this.errorMsg = errorMsg;
		}

		public boolean isValid() {
			return valid;
		}

		public String getErrorMsg() {
			return errorMsg;
		}
	}
	
	/** Returns the boolean value of the Sys Config Var with the given name. If the
	 *  config var doesn't exist, or its value is null, the passed defaultValue is
	 *  returned.
	 *  
	 * @param name
	 * @param defaultValue
	 * @return
	 * @throws DataException
	 */
	private static boolean getConfigFlag(String name, boolean defaultValue)
	 throws DataException {
		SystemConfigVar cfg = SystemConfigVar.getCache().getCachedInstance
		 (name);
		
		if (cfg == null || cfg.getBoolValue() == null)
			return defaultValue;
		else
			return cfg.getBoolValue();
	}
	
	/**
	 * Aurora Account Validation Utility. Based on validateAccount webservice logic from 
	 * Aurora
	 * 
	 * if throwErrorOnFail flag is set to true, a DataException is thrown if the data
	 * fails validation checks. If false, the method will always return an outcome
	 * wrapper object, with an error message if applicable.
	 * 
	 * if isValidateAll is set to true, nominated bankaccount are checked as well. This 
	 * requires the account to actually exist in the central database as it checks the 
	 * relations and accounts. 
	 * 
	 * @param account					existing Account instance, must be set
	 * 									if isValidateAll flag is set.
	 * @param mandatedAccountId
	 * @param incomeDistributionMethod
	 * @param fundCode
	 * @param throwErrorOnFail
	 * @param isValidateAll 
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static AccountValidationOutcome validateAuroraAccount
	 (Account account, Integer mandatedAccountId, String incomeDistributionMethod, 
	 String fundCode, boolean throwErrorOnFail, boolean isValidateAll, FWFacade facade)
	 throws DataException {

		boolean isValid = true;
		String errorMessage = null;
		
		Fund fund = null;
		Fund mandatedFund = null;
		FundTypeCode fundTypeCode = null;
		FundIncomeTypeCode fundIncomeTypeCode = null;
		
		Log.debug("Validating account -- AccountID=" +
		 (account != null ? account.getAccountId() : "[none]") +
		 ", mandatedAccountId="+mandatedAccountId+", incomeDistributionMethod="
		 +incomeDistributionMethod+", fundCode=" + fundCode + 
		 ", throwErrorOnFail=" + throwErrorOnFail + ", isValidateAll="  + isValidateAll);		
		
		// Fetch checking flags.
		boolean checkAllowReinvestmentToSameAccount = getConfigFlag
		 ("AV_CheckAllowReinvestmentToSameAccount", true);
		
		Log.debug("Check flags: checkAllowReinvestmentToSameAccount=" 
		 + checkAllowReinvestmentToSameAccount);
	
		try {
			/********************************************************************************************/
			/********************************* Validation of Base Data  *********************************/
			/********************************************************************************************/
			
			// Check Account Status is valid.
			if (account != null && isValidateAll)
				getAccountStatus(account.getStatus());
			
			//0. First make sure the passed Account instance is non-null if we are 
			// performing full validation checks.
			if (isValid && isValidateAll && account == null) {
				isValid = false;
				errorMessage = 
				 "Cannot perform full Account validation, passed Account is missing/null";
			}
			
			//1. Income Distribution Method must not be empty
			if (isValid && StringUtils.stringIsBlank(incomeDistributionMethod)) {
				isValid = false;
				errorMessage = "No income distribution method selected.";	
			}
			
			//2. Check Fund and related fund attributes
			if (isValid && StringUtils.stringIsBlank(fundCode)) {
				isValid = false;
				errorMessage = "Fund code missing. Please select a fund code.";
			}
			
			if (isValid) {
				fund = Fund.getCache().getCachedInstance(fundCode);
				if (fund==null) {
					isValid = false;
					errorMessage = "Fund Code "+fundCode+" is invalid";
				}
			}
	
			// Type code is either UNI (Unitised), DEP (Deposit) or PSIC (Public Sector Fund)
			// NB in Aurora PSIC funds is set up as UNI.
			if (isValid) {
				fundTypeCode = fund.getTypeCode();
				if (fundTypeCode==null) {
					isValid = false;
					errorMessage = "Fund doesn't have a type code set, fund Code: "+fundCode+".";
				}
			}
			
			//Income type code is either INC (Income) or ACC (Accumulation)
			if (isValid) {
				fundIncomeTypeCode = fund.getIncomeTypeCode();
				if (fundIncomeTypeCode==null) {
					isValid = false;
					errorMessage = "Fund doesn't have a income type code, fund Code: "+fundCode+".";
				}
			}
			
			/********************************************************************************************/
			/****************** Validation of Income Distribution Method Against Fund *******************/
			/********************************************************************************************/
			//Check Deposit Account
			
			//Deposit Account should only allow PAYA, RETN, TXRI
			//Unitised Account should only allow PAYA, TXRI, REIN
			
			if (isValid) {
				if (incomeDistributionMethod.equals(IncomeDistMethod.PAYA.toString())
						&& !fund.isAllowPAYA()) {
					isValid = false;
					errorMessage = "Income Distribution Method PAYA is not valid for fund "+fundCode+".";
				} else if (incomeDistributionMethod.equals(IncomeDistMethod.RETN.toString())
						&& !fund.isAllowRETN()) {
					isValid = false;
					errorMessage = "Income Distribution Method RETN is not valid for fund "+fundCode+".";
				} else if (incomeDistributionMethod.equals(IncomeDistMethod.TXRI.toString())
						&& !fund.isAllowTXRI()) {
					isValid = false;
					errorMessage = "Income Distribution Method TXRI is not valid for fund "+fundCode+".";
				} else if (incomeDistributionMethod.equals(IncomeDistMethod.REIN.toString())
						&& !fund.isAllowREIN()) {
					isValid = false;
					errorMessage = "Income Distribution Method REIN is not valid for fund "+fundCode+".";
				}
			}
			
			/********************************************************************************************/
			/********************* Validation of Bank Accounts Against PAYA  ****************************/
			/********************************************************************************************/
			if (isValid) {
				if (incomeDistributionMethod.equals(IncomeDistMethod.PAYA.toString())) {
					
					//Full Validation
					if (isValid && isValidateAll) {

						if (isValid) {
							RelationName incomeRel= 
							 RelationName.getCache().getCachedInstance(
							 RelationName.AccountBankAccountRelation.INCOME);
							
							BankAccount nomIncomeAcc = Account.getNominatedBankAccount
							 (account.getAccountId(), incomeRel, facade);
							
							if (nomIncomeAcc==null) {
								isValid = false;
								errorMessage = "Nominated Income Bank Account must " +
								 "be set when using Pay-Away (PAYA) Income Distribution Method";
							} else {
								try {
									nomIncomeAcc.validate(facade);
								} catch (DataException de) {
									isValid = false;
									errorMessage = 
									 "Nominated Income Bank Account failed validation " +
									 "checks:"+de.getMessage();
								}
								
								if (isValid) {
									// Check for a short name.
									if (StringUtils.stringIsBlank
										(nomIncomeAcc.getShortName())) {
										isValid = false;
										errorMessage = "Nominated Income Bank Account does not " +
										"have a Short Name set";
									}
								}
							}
						}
					}
				} else if (incomeDistributionMethod.equals(IncomeDistMethod.REIN.toString()) ||
						incomeDistributionMethod.equals(IncomeDistMethod.RETN.toString()) ||
						incomeDistributionMethod.equals(IncomeDistMethod.TXRI.toString())) {

					//Full Validation
					if (isValid && isValidateAll) {
						if (isValid) {
							RelationName incomeRel= 
							 RelationName.getCache().getCachedInstance(
							 RelationName.AccountBankAccountRelation.INCOME);
							
							BankAccount nomIncomeAcc = Account.getNominatedBankAccount(
										account.getAccountId(), incomeRel, facade);
							
							if (nomIncomeAcc!=null) {
								isValid = false;
								errorMessage = "Nominated Income Bank Account must " +
								 "NOT be set when using this Income Distribution Method";
							}
						}
					}					
					
					// Mandated Account checks
					if (isValid && mandatedAccountId == null) {
						if (fund.getCompany()==null) {
							isValid = false;
							errorMessage = "Mandated Company must be populated for " +
							 "selected Income Distribution Method. Please ensure that " +
							 "Fund has a company set.";							
						}
						
						if (checkAllowReinvestmentToSameAccount) {
							if (fund.getTypeCode().getFundTypeCodeId()==FundTypeCode.FUND_TYPECODE_ID_PSDF) {
								if (isValid && !fund.isAllowSameMandatedAccountFund() && isValidateAll){
									//Temp fix for PSDF account due to the nature of how it is setup.
									isValid = false;
									errorMessage = "Fund "+fund.getFundCode()+
									 " does not allow reinvestment back into same Account " +
									 "for Fund - please specify a Mandated Account.";		
									
								}
							} else {
								if (isValid && !fund.isAllowSameMandatedAccountFund()){			
									isValid = false;
									errorMessage = "Fund "+fund.getFundCode()+" does not " +
									 "allow reinvestment back into same Account - please " +
									 "specify a Mandated Account.";
								}
							}
						}
						
						if (isValid && incomeDistributionMethod.equals
							(Account.IncomeDistMethod.TXRI.toString())) {
							isValid = false;
                            errorMessage = "Mandated Account must be populated for" +
                             " selected Income Distribution Method (TXRI)";
						}
						
					} else if (isValid && mandatedAccountId != null) {
						//Get mandated Account
						Account mandatedAccount = Account.get(mandatedAccountId, facade);
						if (mandatedAccount==null) {
							isValid = false;
							errorMessage = "Cannot find mandated account with id: "
							 +mandatedAccountId;
						}						
						
						//Check mandated fund setup.
						if (isValid) {
							if (StringUtils.stringIsBlank(mandatedAccount.getFundCode())) {
								isValid = false;
								errorMessage = "Mandated Account "
								 +mandatedAccountId+" does not have a fund code";
							} else {
								mandatedFund = Fund.getCache().getCachedInstance
								 (mandatedAccount.getFundCode());
								
								if (mandatedFund==null) {
									isValid = false;
									errorMessage = "Cannot find fund for mandated account" +
									 " with id "+mandatedAccountId
									 +", and fund "+mandatedAccount.getFundCode();
								} else {
									if (mandatedFund.getTypeCode()==null) {
										isValid = false;
										errorMessage = "Mandated Fund doesn't have a" +
										 " type code, mandated fund Code: "
										 +mandatedFund.getTypeCode()+".";
									} 
								}
								
								if (mandatedFund.getCompany()==null) {
									isValid = false;
									errorMessage = "Mandated Company must be populated" +
									 " for selected Income Distribution Method. " +
									 "Please ensure that mandated fund has a company.";							
								}
							}
						}
						
						//Check income distribution methods against mandated account fund type code.
						if (isValid) {
							if ((incomeDistributionMethod.equals(IncomeDistMethod.RETN.toString()) ||
								incomeDistributionMethod.equals(IncomeDistMethod.TXRI.toString())) 
								&& 
								mandatedFund.getTypeCode().getFundTypeCodeId()
								!=FundTypeCode.FUND_TYPECODE_ID_DEPOSIT) {
								
								isValid = false;
								errorMessage = "Mandated Account must be a Deposit type" +
								 " account for this income distribution method."; 
							
							} else if (incomeDistributionMethod.equals(IncomeDistMethod.REIN.toString()) 
								&& 
								(mandatedFund.getTypeCode().getFundTypeCodeId()!=FundTypeCode.FUND_TYPECODE_ID_UNITIZED 
								&&
								mandatedFund.getTypeCode().getFundTypeCodeId()!=FundTypeCode.FUND_TYPECODE_ID_PSDF)) {
								isValid = false;
								errorMessage = "Mandated Account must be a Unitized/PSIC" +
								 " type account for this income distribution method."; 							
							}
						}
						
						//PSIC Validation Logic
						if (isValid 
							&& 
							incomeDistributionMethod.equals(IncomeDistMethod.REIN.toString())) {
							// Non PSIC accounts, account and mandated account must 
							// belong to the same fund code 
							
							if (fund.getCompany().getCompanyId()==Company.PSIC) {
									
								if (fundTypeCode.getFundTypeCodeId()!=FundTypeCode.FUND_TYPECODE_ID_PSDF 
									&& mandatedFund.getTypeCode().getFundTypeCodeId()!=FundTypeCode.FUND_TYPECODE_ID_PSDF) {
									isValid = false;
									errorMessage =  "An Account in a share class " +
									 "fund must reinvest in a fund that reinvests its" +
									 " capital balance.";
								}
								
								if (isValid && mandatedFund.getFundCode().equals(fund.getFundCode())) {
									isValid = false;
									errorMessage = "An Account in a fund that reinvests" +
									 " it capital balance cannot reinvest back into the same fund.";
								}
							}
						}
						
						//Mandated Fund and Fund checks.
						if (isValid) {
							if (fund.getFundCode().equals(mandatedFund.getFundCode()) 
								&&
								!fund.isAllowSameMandatedAccountFund()) {
								isValid = false;
								errorMessage = "Fund "+fund.getFundCode()+
								 " does not allow reinvestment into another account in the same fund.";
							} 
							else if (!fund.getFundCode().equals(mandatedFund.getFundCode()) 
								&& 
								incomeDistributionMethod.equals(IncomeDistMethod.REIN.toString()) &&
								!fund.isAllowREINMandatedAccountInOtherFunds()) {
								isValid = false;
								errorMessage = "Fund "+fund.getFundCode()+
								 " does not allow reinvestment into an account in the another fund.";
							} 
							else if (!fund.getFundCode().equals(mandatedFund.getFundCode())) {
								String key = fund.getFundCode()+"|"+mandatedFund.getFundCode();
								String value = Fund.getInterFundCache().getCachedInstance(key);
								
								if (StringUtils.stringIsBlank(value)) {
									isValid = false;
									errorMessage = "Inter-Fund Transfer between fund "
									 +fund.getFundCode()+" - "+mandatedFund.getFundCode()
									 +" is invalid.";
								}
							}
						}
					}
				}
				
				//Check withdrawal bank account if populated
				if (isValid && isValidateAll) {
					RelationName withdrawalRel= 
						RelationName.getCache().getCachedInstance(
							RelationName.AccountBankAccountRelation.WITHDRAWAL);
					
					BankAccount nomWithdrawalAcc = Account.getNominatedBankAccount(
					 account.getAccountId(), withdrawalRel, facade);
					
					if (nomWithdrawalAcc==null && REQUIRE_ACCOUNT_WITHDRAWAL_BANK_ACCOUNT) {
						isValid = false;
						errorMessage = "Nominated Withdrawal Bank Account must be populated.";
					} else {							
						if (nomWithdrawalAcc!=null) { 
							try {
								nomWithdrawalAcc.validate(facade);
							} catch (DataException de) {
								isValid = false;
								errorMessage = 
								 "Nominated Withdrawal Bank Account failed validation: "
								 +de.getMessage();
							}
							
							if (isValid) {
								// Check for a short name.
								if (StringUtils.stringIsBlank
									(nomWithdrawalAcc.getShortName())) {
									isValid = false;
									errorMessage = "Nominated Withdrawal Bank Account does not " +
									"have a Short Name set";
								}
							}
						}
					}
				}
			}
		} catch (DataException de) {
			isValid = false;
			errorMessage = de.getMessage();
		}
		
		Log.debug("Completed Validating account -- AccountID=" +
		 (account != null ? account.getAccountId() : "[none]") +
		 ", isValid="+isValid+", errorMessage="
		 +(StringUtils.stringIsBlank(errorMessage)?"":errorMessage));		
		
		AccountValidationOutcome outcome = new AccountValidationOutcome
		 (isValid, errorMessage);
		
		if (!throwErrorOnFail || isValid) {
			return outcome;
		} else {
			throw new DataException(errorMessage);
		}
	}
	
	public static AccountValidationOutcome validateAuroraAccount
	 (Account account, boolean throwErrorOnFail, boolean isValidateAll, 
	 FWFacade facade) throws DataException {
		
		Integer mandatedAccountId = account.getMandatedAccId();
		String incomeDistributionMethod = account.getIncomeDistMethod();
		String fundCode = account.getFundCode();

		return AuroraAccountHandler.validateAuroraAccount
		 (account, mandatedAccountId, incomeDistributionMethod, fundCode, throwErrorOnFail, 
		 isValidateAll, facade);
	}
}
