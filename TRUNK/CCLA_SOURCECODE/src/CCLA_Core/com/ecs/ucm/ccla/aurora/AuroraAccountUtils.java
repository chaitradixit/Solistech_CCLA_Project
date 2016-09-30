package com.ecs.ucm.ccla.aurora;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Vector;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.shared.SharedObjects;

import com.aurora.webservice.Account;
import com.aurora.webservice.AccountStatus;
import com.aurora.webservice.Client;
import com.aurora.webservice.IncomeDistributionMethods;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals;
import com.ecs.ucm.ccla.aurora.AuroraAccountHandler.AccountValidationOutcome;
import com.ecs.ucm.ccla.data.AccountValue;
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
import com.ecs.ucm.ccla.data.Account.IncomeDistMethod;
import com.ecs.ucm.ccla.data.SystemConfigVar;
import com.ecs.ucm.ccla.utils.ObjectUtils;
import com.ecs.ucm.ccla.utils.ShareClassUtils;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Helper methods for interfacing with the Aurora Account web services
 * 
 *  Superseded by AuroraAccountHandler
 *  
 * @author Tom
 *
 */
public @Deprecated class AuroraAccountUtils {
	
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
	
	/** Returns the corresponding Aurora account, null otherwise. Should be used to
	 *  check for presence of accounts in Aurora
	 * 
	 * @param account
	 * @param facade
	 * @return
	 * @throws ServiceException
	 * @throws DataException 
	 * @throws RemoteException 
	 */
	public static Account getAccount
	 (com.ecs.ucm.ccla.data.Account account, FWFacade facade) 
	 throws ServiceException, DataException {
		
		Log.debug("Attempting to fetch corresponding Aurora account for Account ID " 
		 + account.getAccountId());
		
		Account existingAccount = null;
		
		// Determine Fund
		Fund fund = Fund.getCache().getCachedInstance
		 (account.getFundCode());
		
		Integer ownerOrgId = account.getOwnerOrganisationId(facade);
		AuroraClient clMap = Entity.getAuroraClientCompanyMapping
		 (ownerOrgId, fund.getCompany(), facade);
			
		if (clMap == null) {
			// Fail if no Company mapping
			throw new ServiceException("no Aurora Client preferences set for Company " 
			 + fund.getCompany().getCode());
		}
			
		// Calculate the AccNumExt and set it against the new Aurora Account and
		// the base CCLA Account.
		Account testAccount = getAccountInstance(account, facade);
		String accNumExt = null;
		
		try {
			accNumExt = getAuroraAccNumExt(testAccount, clMap);
		} catch (Exception e) {
			Log.debug(e.getMessage(), e);
			throw new ServiceException(e.getMessage(), e);
		}
		
		try {
			existingAccount = 
			 AuroraWebServiceHandler.getAuroraWS().getAccountByAccountNumberExternal
			  (clMap.getCompany().getCode(), accNumExt);
			
			Log.debug("Found Aurora account: "
			 + existingAccount.getAccountNumberExternal());
			
		} catch (Exception e) {
			// Assume error was thrown by Aurora, as Account did not exist.
			Log.error("getAccountByAccountNumberExternal - error: " 
			 + e.getMessage(), e);
		}
		
		return existingAccount;
	}
	
	/** Adds a new Aurora Account record, based on the passed CCLA Account instance.
	 * 
	 * @param account
	 * @param facade
	 * @throws ServiceException 
	 */
	public static void addAccount
	 (com.ecs.ucm.ccla.data.Account account, FWFacade facade) throws ServiceException {
		
		Log.debug("Adding new Aurora Account for Account ID " 
		 + account.getAccountId());
		
		if (!account.isAuroraAccount()) {
			throw new ServiceException("Account:"+account.getAccountId()+", is not " +
			 "an aurora account");
		}
		
		try {
			Fund fund = Fund.getCache().getCachedInstance(account.getFundCode());
			
			Integer ownerOrgId = account.getOwnerOrganisationId(facade);
			AuroraClient clMap = Entity.getAuroraClientCompanyMapping
			 (ownerOrgId, fund.getCompany(), facade);
			
			if (clMap == null) {
				throw new ServiceException("no UCM Aurora Client Number set for " +
				 "Organisation ID " + ownerOrgId + 
				 ", Company " + fund.getCompany().getCode()+", Please check the Organisation Record.");
			}
			
			// Set account to OPEN status (change is persisted providing that the
			// Aurora web service succeeds)
			account.setStatus(com.ecs.ucm.ccla.data.Account.AccountStatus.OPEN);
			// Set the Date Opened to current date (change is persisted providing that 
			// the Aurora web service succeeds)
			account.setDateOpened(new Date());
			
			AuroraAccountHandler.validateAuroraAccount(account, true, true, facade);
			
			// Generate Aurora Account directly from CCLA Account data
			Account newAccount = getAccountInstance(account, facade);
			
			// Ensure Aurora Account with this Client/Fund/Number doesn't already exist
			Account existingAccount = getAccount(account, facade);
			
			if (existingAccount != null) {
				throw new ServiceException("account record with Client Number: " + 
				 clMap.getClientNumber() + ", Fund: " +  account.getFundCode() + 
				 ", Account Number: " + account.getAccountNumber() + " already exists");
			}

//			boolean requireMandateUpdate = false;
			if (account.getMandatedAccId() != null) {				
				com.ecs.ucm.ccla.data.Account mandatedAccount = 
					com.ecs.ucm.ccla.data.Account.get(account.getMandatedAccId(), facade);
				Account mandatedAuroraAccount = 
					AuroraAccountUtils.getAccount(mandatedAccount, facade);
				
				
				
				//Aurora Logic 
				if (mandatedAuroraAccount==null) {
					Log.warn("Clearing mandated acc field, as mandated account does not exist in Aurora, MandatedAcc:"
							+account.getMandatedAccId()+" for account:"+account.getAccountId());
					newAccount.setMandatedAccount("");
					
					//If the account income distribution is set to re-invest, set it to PAYA
					if (account.getIncomeDistMethod().equals(com.ecs.ucm.ccla.data.Account.IncomeDistMethod.REIN.toString())) {
						Log.warn("Changing income distribution method to PAYA from REIN, as mandated account does not exist in Aurora for account:"
								+account.getAccountId());
						newAccount.setIncomeDistributionMethod(
							getIncomeDistributionMethod(
									com.ecs.ucm.ccla.data.Account.IncomeDistMethod.PAYA.toString()));

						//Set some dummy income bank account details if it has not been specified
						if (newAccount.getBankSortCodeIncome()==0 || 
								newAccount.getBankAccountNumberIncome()==0) 
						{	
							//Try using the mandated account income details (if it exists)
							if (mandatedAccount!=null) 
							{
								RelationName incomeRel= RelationName.getCache().getCachedInstance(
										 RelationName.AccountBankAccountRelation.INCOME);
										
								BankAccount nomIncomeAcc = 
									com.ecs.ucm.ccla.data.Account.getNominatedBankAccount(
											mandatedAccount.getAccountId(), incomeRel, facade);
								
								if (nomIncomeAcc!=null && 
										!StringUtils.stringIsBlank(nomIncomeAcc.getSortCode()) &&
										!StringUtils.stringIsBlank(nomIncomeAcc.getAccountNumber())) 
								{
									newAccount.setBankSortCodeIncome(
											Integer.parseInt(nomIncomeAcc.getSortCode()));
									newAccount.setBankAccountNumberIncome(
											Integer.parseInt(nomIncomeAcc.getAccountNumber()));
								} 
							} 
							
							//If it is still empty, use dummy details.
							if (newAccount.getBankSortCodeIncome()==0 || 
									newAccount.getBankAccountNumberIncome()==0) {
								SystemConfigVar dummyAccountNumber = 
									SystemConfigVar.getCache().getCachedInstance(
											Globals.CAP_INCOME_BANK_ACCOUNT_NUMBER+"_"+account.getFundCode());
								
								SystemConfigVar dummyAccountSortCode = 
									SystemConfigVar.getCache().getCachedInstance(
											Globals.CAP_INCOME_BANK_ACCOUNT_SORTCODE+"_"+account.getFundCode());
								
								if (dummyAccountSortCode!=null && dummyAccountSortCode.getIntValue()!=null
									&& dummyAccountNumber!=null && dummyAccountNumber.getIntValue()!=null) {
									newAccount.setBankSortCodeIncome(dummyAccountSortCode.getIntValue());
									newAccount.setBankAccountNumberIncome(dummyAccountNumber.getIntValue());
									newAccount.setAccountShortNameOrBuildingSocietyReferenceIncome("Capital Income Acc "+account.getFundCode());
								} else {
									String errorMsg = "Cannot find default capital income account details. Please contact the Administrator.";
									//Log.error(errorMsg);
									throw new ServiceException(errorMsg);
								}	
							}
						}
					}
				}
			}
//					if (mandatedAccount.getMandatedAccId()!=null && 
//							mandatedAccount.getMandatedAccId().equals(account.getAccountId())) {
//						requireMandateUpdate = true;
//					}
//					mandatedAccount = addOrUpdateMandatedAccount(mandatedAccount, true, facade);
//				}
//			}
			
			Log.debug("Creating Aurora Account: \n" + newAccount.toString());
			AuroraWebServiceHandler.getAuroraWS().createAccount
			 (clMap.getCompany().getCode(), newAccount, false);
			
			// Persist changes to base Account object.
			account.persist(facade, Globals.Users.System);
			
			//finally update the mandate account with the correct mandate info.
//			if (requireMandateUpdate) {
//				mandatedAccount = addOrUpdateMandatedAccount(mandatedAccount, false, facade);
//			}
//			
//			if (mandatedAccount!=null)
//				mandatedAccount.persist(facade, Globals.Users.System);
			
			Log.debug("Successfully added new Aurora Account for Account ID " 
			 + account.getAccountId());
			
			// TODO audit
			
		} catch (Exception e) {
			String msg = "Failed to add new Aurora account: " + e.getMessage();
			
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}
	
	
	/**
	 * Adds a mandate account.
	 * @param account
	 * @param facade
	 * @throws ServiceException
	 */
	private static com.ecs.ucm.ccla.data.Account addOrUpdateMandatedAccount
	 (com.ecs.ucm.ccla.data.Account account, boolean clearMandatedAccount, FWFacade facade) throws ServiceException {
	
		// Ensure Aurora Account with this Client/Fund/Number doesn't already exist
		try {
			Fund fund = Fund.getCache().getCachedInstance(account.getFundCode());
			
			Integer ownerOrgId = account.getOwnerOrganisationId(facade);
			AuroraClient clMap = Entity.getAuroraClientCompanyMapping
			 (ownerOrgId, fund.getCompany(), facade);
			
			if (clMap == null) {
				throw new ServiceException("no Aurora Client Number set for " +
				 "Organisation ID " + ownerOrgId + 
				 ", Company " + fund.getCompany().getCode());
			}
			
			Account newMandateAccount = getAccountInstance(account, facade);

			//Clear the mandate info if required. 
			//Will possibly update the mandate account.
			if (clearMandatedAccount) {
				newMandateAccount.setMandatedAccount("");
			}

			//check if this exist
			Account existingMandateAccount = getAccount(account, facade);
			
			if (existingMandateAccount==null) {
				
				// Set account to OPEN status (change is persisted providing that the
				// Aurora web service succeeds)
				account.setStatus(com.ecs.ucm.ccla.data.Account.AccountStatus.OPEN);
				// Set the Date Opened to current date (change is persisted providing that 
				// the Aurora web service succeeds)
				account.setDateOpened(new Date());
				
				Log.debug("Creating Mandated Aurora Account: \n" + 
				 newMandateAccount.toString());
				
				AuroraWebServiceHandler.getAuroraWS().createAccount
				(clMap.getCompany().getCode(), newMandateAccount, false);
			} else {
				
				Log.debug("Updating Mandated Aurora Account: \n" + 
				 newMandateAccount.toString());
				
				updateAccountInstance(existingMandateAccount, newMandateAccount);
				// Apply update in Aurora
				AuroraWebServiceHandler.getAuroraWS().amendAccount
				(clMap.getCompany().getCode(), existingMandateAccount);
			}
		} catch (Exception e) {
			String msg = "Failed to add/update Mandate Aurora account: " + e.getMessage();
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
		
		return account;
	}
	
	/** Updates an existing Aurora Account record, based on the passed CCLA Account
	 *  instance.
	 *  
	 * @param account
	 * @param facade
	 * @throws ServiceException 
	 */
	public static void updateAccount
	 (com.ecs.ucm.ccla.data.Account account, FWFacade facade) throws ServiceException {

		if (!account.isAuroraAccount()) {
			throw new ServiceException
			 ("Account ID "+account.getAccountId()+" is not an Aurora account");
		}
		try {
			
			Log.debug("Updating Aurora Account for Account ID " 
			 + ((account==null)?"Null":account.getAccountId()));

			AuroraAccountHandler.validateAuroraAccount(account, true, true, facade);
			
			Integer ownerOrgId = account.getOwnerOrganisationId(facade);
			
			if (ownerOrgId==null) {
				Log.error("no ownerOrgId for account" +account.getAccountId());
				throw new ServiceException("no ownerOrgId for account" +account.getAccountId());
			}
			
			Fund fund = Fund.getCache().getCachedInstance(account.getFundCode());
			
			AuroraClient clMap = Entity.getAuroraClientCompanyMapping
			 (ownerOrgId, fund.getCompany(), facade);
			
			if (clMap == null) {
				String msg = "no Aurora Client set for " +
				 "Organisation ID " + ownerOrgId + ", Company: " 
				 + fund.getCompany().getCode();
				
				Log.error(msg);
				throw new ServiceException(msg);
			}
			
			Account newAccount = getAccountInstance(account, facade);
			String accNumExt = getAuroraAccNumExt(newAccount, clMap);
			
			newAccount.setAccountNumberExternal(accNumExt);
			account.setAccNumExt(accNumExt);
			
			// Ensure Aurora Account with this Client/Fund/Number already exists
			Account existingAccount = getAccount(account, facade);
				
			if (existingAccount == null) {
				String msg = "Aurora account with Client Number: " + 
				 clMap.getClientNumber() + ", Fund: " +  account.getFundCode() + 
				 ", Account Number: " + account.getAccountNumber() + " not found";
				
				Log.error(msg);
				throw new ServiceException(msg);
			}
			
			
			if (account.getMandatedAccId() != null) {				
				com.ecs.ucm.ccla.data.Account mandatedAccount = 
					com.ecs.ucm.ccla.data.Account.get(account.getMandatedAccId(), facade);
				Account mandatedAuroraAccount = 
					AuroraAccountUtils.getAccount(mandatedAccount, facade);
				if (mandatedAuroraAccount==null) {
					Log.warn("Clearing mandated acc field, as mandated account does not exist in Aurora, MandatedAcc:"
							+account.getMandatedAccId()+" for account:"+account.getAccountId());
					newAccount.setMandatedAccount("");
					
					//If the account income distribution is set to re-invest, set it to PAYA
					if (account.getIncomeDistMethod().equals(com.ecs.ucm.ccla.data.Account.IncomeDistMethod.REIN.toString())) {
						Log.warn("Changing income distribution method to PAYA from REIN, as mandated account does not exist in Aurora for account:"
								+account.getAccountId());
						newAccount.setIncomeDistributionMethod(
							getIncomeDistributionMethod(
									com.ecs.ucm.ccla.data.Account.IncomeDistMethod.PAYA.toString()));
					}
				}
			}

			
			// Copy over updatable fields
			updateAccountInstance(existingAccount, newAccount);

			// Apply update in Aurora
			Log.debug("Amending Aurora Account: \n" + existingAccount.toString());
			AuroraWebServiceHandler.getAuroraWS().amendAccount
			 (clMap.getCompany().getCode(), existingAccount);

			// Fetch the freshly-updated account from Aurora
			Account auroraAccount = AuroraWebServiceHandler.getAuroraWS().
			 getAccountByAccountNumberExternal(fund.getCompany().getCode(), accNumExt);
			
			if (auroraAccount == null) {
				String msg = "Aurora account updated successfully, but unable to fetch "
				 + "back from Aurora (ext. acc. no: " + accNumExt;
				
				Log.error(msg);
				throw new ServiceException(msg);
			}
			
			// Set the date opened
			account.setDateOpened(auroraAccount.getDateOpened().getTime());

			// Apply changes to local Account record
			account.persist(facade, Globals.Users.System);
			
//			if (mandatedAccount!=null)
//				mandatedAccount.persist(facade, Globals.Users.System);
			
			Log.debug("Successfully updated Aurora Account for Account ID " 
			 + account.getAccountId());
					
			// TODO Audit successful/failed call
			
		} catch (Exception e) {
			String msg = "Failed to update Aurora account: " + e.getMessage();
			
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
		
	}
	
	/** Generates a new Aurora Account instance from the passed CCLA Account. Does not
	 *  call any Aurora web services to actually create/update the Account record.
	 *  
	 * @param account
	 * @param facade
	 * @return
	 * @throws ServiceException 
	 * @throws DataException 
	 * @throws ServiceException 
	 * @throws DataException 
	 */
	public static Account getAccountInstance
	 (com.ecs.ucm.ccla.data.Account account, FWFacade facade) 
	 throws ServiceException, DataException {
		
		// Determine Fund
		Fund fund = Fund.getCache().getCachedInstance
		 (account.getFundCode());
		
		// Determine Company by the account's Fund
		Company company = fund.getCompany();
		
		// Find Aurora Client mapping for this Organisation
		Integer ownerOrgId = account.getOwnerOrganisationId(facade);
		
		if (ownerOrgId == null) {
			String msg = "no owning Organisation set";
			
			Log.error(msg);
			throw new ServiceException(msg);
		}
		
		AuroraClient clMap = 
		 Entity.getAuroraClientCompanyMapping(ownerOrgId, company, facade);
		
		if (clMap == null) {
			// Fail if no Company mapping
			throw new ServiceException("no Aurora Client preferences set for " +
			 "Company " + company.getCode());
		}
		
		if (clMap.getContributorTypeCode() == null) {
			// Fail if the Contributor Type doesn't have a value set - this indicates
			// that the AuroraClient mapping hasn't been sync'ed to Aurora yet
			throw new ServiceException("Aurora Client preferences for " +
			 "Company " + company.getCode() + " must be synchronised with Aurora first");
		}
		
		// Fetch all mapped Account -> Person Correspondent relations.
		Person person = com.ecs.ucm.ccla.data.Account.getNominatedCorrespondent
		 (account.getAccountId(), false, facade);
		
		// Fail if no correspondent set
		if (person==null) {
			String msg = "no Correspondent/Nominated Correspondent set";
			
			Log.error(msg);
			throw new ServiceException(msg);
		}
		
		Integer corrPersonId = person.getPersonId();
		AuroraCorrespondent corrMap = null;
		
		// Fetch Aurora Correspondent mappings for Correspondent's Person ID
		Vector<AuroraCorrespondent> corrsMap = 
		 AuroraCorrespondent.getCorrespondentsByPersonId(corrPersonId, facade);
		
		corrMap = AuroraCorrespondent.getByCompany(corrsMap, company);
		
		// Fail if correspondent doesn't have Aurora Correspondent mapping
		if (corrMap == null) {
			String msg = "no mapped Correspondent preferences for " + 
			 "Company: " + company.getCode();
			
			Log.error(msg);
			throw new ServiceException(msg);
		} else if (corrMap.getCorrId() == null) {
			String msg = "Correspondent preferences for Company " + company.getCode() +
			 " not synchronized with Aurora. Ensure the Correspondent record has been" +
			 " created in Aurora first";
			
			Log.error(msg);
			throw new ServiceException(msg);
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
		Account newAccount = new Account();
		setDefaultValues(newAccount);
		
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
			// Always set the Mandated Company field, if there is no Mandated Account 
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
				String msg = "no owning Organisation set for mandated Account";
				
				Log.error(msg);
				throw new ServiceException(msg);
			}
			
			AuroraClient mandatedClMap = 
			 Entity.getAuroraClientCompanyMapping(mandatedOwnerOrgId, company, facade);
			
			if (mandatedClMap == null) {
				// Fail if no Company mapping
				throw new ServiceException("no Aurora Client preferences set for " +
				 "mandated Organisation's Company " + mandatedCompany.getCode());
			}
			
			try {
				String mandatedAccNumExt = AuroraWebServiceHandler.getAuroraWS().
				getPendingAccountNumberExternalByClientNumberAndAccountNumber(
				 mandatedCompany.getCode(), 
				 mandatedAccount.getFundCode(), 
				 mandatedClMap.getClientNumber(), 
				 mandatedAccount.getAccountNumber());
				
				newAccount.setMandatedAccount(mandatedAccNumExt);
				newAccount.setMandatedCompany(mandatedCompany.getCode());
				
			} catch (Exception e) {
				String msg = "Failed to resolve mandated AccNumExt for " +
				 "mandated Account ID " + mandatedAccount.getAccountId() + ": " + 
				 e.getMessage();
				
				Log.error(msg, e);
				throw new ServiceException(msg, e);
			}
		}
			
		// Withdrawal Banking details
		if (nomWithdrawalAcc != null) {
			Log.debug("Withdrawal ID :"+nomWithdrawalAcc.getBankAccountId());			
			Log.debug("Withdrawal Name :"+nomWithdrawalAcc.getAccountName());
			Log.debug("Withdrawal AccNo :"+nomWithdrawalAcc.getAccountNumber());			
			Log.debug("Withdrawal SortCode :"+nomWithdrawalAcc.getSortCode());			
			
			if (StringUtils.stringIsBlank(nomWithdrawalAcc.getShortName())) {
				throw new ServiceException("Nominated Withdrawal Bank Account does not " +
				 "have a Short Name set");
			}
			
			newAccount.setBankAccountNameWithdrawal
			 (nomWithdrawalAcc.getAccountName() != null ? 
					 CCLAUtils.truncateString(
							 nomWithdrawalAcc.getAccountName(),
							 AuroraAccountUtils.MAX_AURORA_BANK_ACC_NAME_LENGTH) : "");
			newAccount.setBankAccountNumberWithdrawal(
			 Integer.parseInt(nomWithdrawalAcc.getAccountNumber()));
			newAccount.setBankSortCodeWithdrawal(
			 Integer.parseInt(nomWithdrawalAcc.getSortCode()));
			newAccount.setAccountShortNameOrBuildingSocietyReferenceWithdrawal
			 (nomWithdrawalAcc.getShortName() != null ? nomWithdrawalAcc.getShortName() : "");
			
		} else if (REQUIRE_ACCOUNT_WITHDRAWAL_BANK_ACCOUNT) {
			throw new ServiceException("Account must have a nominated withdrawal " +
			 "Bank Account set");
		}
		
		// Income Banking details
		if (nomIncomeAcc != null) {
			Log.debug("Income ID :"+nomIncomeAcc.getBankAccountId());			
			Log.debug("Income Name :"+nomIncomeAcc.getAccountName());
			Log.debug("Income AccNo :"+nomIncomeAcc.getAccountNumber());			
			Log.debug("Income SortCode :"+nomIncomeAcc.getSortCode());			
			
			if (StringUtils.stringIsBlank(nomIncomeAcc.getShortName())) {
				throw new ServiceException("Nominated Income Bank Account does not " +
				 "have a Short Name set");
			}
			
			newAccount.setBankAccountNameIncome
			 (nomIncomeAcc.getAccountName() != null ? 
					 CCLAUtils.truncateString(
							 nomIncomeAcc.getAccountName(), 
							 AuroraAccountUtils.MAX_AURORA_BANK_ACC_NAME_LENGTH) : "");
			
			newAccount.setBankAccountNumberIncome(
			 Integer.parseInt(nomIncomeAcc.getAccountNumber()));
			
			newAccount.setBankSortCodeIncome(
			 Integer.parseInt(nomIncomeAcc.getSortCode()));
			
			newAccount.setAccountShortNameOrBuildingSocietyReferenceIncome
			 (nomIncomeAcc.getShortName() != null ? nomIncomeAcc.getShortName() : "");
			
		} else if (REQUIRE_ACCOUNT_INCOME_BANK_ACCOUNT) {
			throw new ServiceException("Account must have a nominated income " +
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
			String accNumExt = getAuroraAccNumExt(newAccount, clMap);
			newAccount.setAccountNumberExternal(accNumExt);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage(), e);
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
		
		
		//Set the Shared Class ID for PC accounts.
		if (fund.getFundCode().equals(PSDF_FUND_CODE)) {
			
			try {
				Integer shareClassId = 0;
				
				if (!StringUtils.stringIsBlank(account.getShareClass())) {
					shareClassId = Integer.parseInt(account.getShareClass());
				}
				
				if (shareClassId==0) {
					shareClassId = ShareClassUtils.getExpectedShareClass(account.getAccountId(), null, facade);
				}
				
				if (shareClassId!=null && shareClassId!=0)
					newAccount.setShareClassCode(shareClassId);
				else 
					throw new DataException("Cannot find ShareClassId for account:"+account.getAccountId());
			} catch (NumberFormatException nfe) {
				Log.error("Cannot set ShareClass Id "+account.getShareClass());
			}
		}
		
		Log.debug("Generated Aurora Account Instance " + newAccount.toString());
		return newAccount;
	}
	
	/** Copies over select field values from the newAccount instance to the account
	 *  instance.
	 *  
	 *  Should be used when updating existing Aurora accounts.
	 *  
	 * @param account
	 * @param newAccount
	 */
	private static void updateAccountInstance(Account account, Account newAccount) {
		
		account.setStatus(newAccount.getStatus());
		account.setAccountNumberExternal(newAccount.getAccountNumberExternal());
		
		// Corr. code
		account.setCorrespondentCode(newAccount.getCorrespondentCode());
		
		account.setSubtitle(newAccount.getSubtitle());
		
		account.setIncomeDistributionMethod(newAccount.getIncomeDistributionMethod());
		account.setMandatedAccount(newAccount.getMandatedAccount());
		account.setMandatedCompany(newAccount.getMandatedCompany());
		
		account.setIsExternalAccount(newAccount.isIsExternalAccount());
		account.setStandingTransactionsIndicator
		 (newAccount.isStandingTransactionsIndicator());
		
		// Withdrawal Banking details
		account.setBankAccountNameWithdrawal(
				newAccount.getBankAccountNameWithdrawal()!=null?
						CCLAUtils.truncateString(
								newAccount.getBankAccountNameWithdrawal(), 
								AuroraAccountUtils.MAX_AURORA_BANK_ACC_NAME_LENGTH):"");
		
		account.setBankAccountNumberWithdrawal
		 (newAccount.getBankAccountNumberWithdrawal());
		account.setBankSortCodeWithdrawal
		 (newAccount.getBankSortCodeWithdrawal());
		account.setAccountShortNameOrBuildingSocietyReferenceWithdrawal
		 (newAccount.getAccountShortNameOrBuildingSocietyReferenceWithdrawal());
		
		// Income Banking details
		account.setBankAccountNameIncome(
				newAccount.getBankAccountNameIncome()!=null?
						CCLAUtils.truncateString(
								newAccount.getBankAccountNameIncome(), 
								AuroraAccountUtils.MAX_AURORA_BANK_ACC_NAME_LENGTH):"");

		account.setBankAccountNumberIncome
		 (newAccount.getBankAccountNumberIncome());
		account.setBankSortCodeIncome
		 (newAccount.getBankSortCodeIncome());
		account.setAccountShortNameOrBuildingSocietyReferenceIncome
		 (newAccount.getAccountShortNameOrBuildingSocietyReferenceIncome());
	}
	
	/** Converts an Inc Dist. Method code to its equivalent Aurora object.
	 * 
	 * @param incDistMethod
	 * @return
	 * @throws DataException 
	 */
	private static IncomeDistributionMethods getIncomeDistributionMethod
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
			throw new DataException("unknown Income Dist Method code: " + 
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
				throw new DataException("unable to determine account status. Ensure " +
				 "it is either OPEN, CLOS or FROZ before applying to Aurora");
			}
		}
		
	}
	
	/** Fetches the Account's AccNumExt value, as calculated by Aurora.
	 * 
	 * @param account
	 * @param auroraClient
	 * @return
	 * @throws ServiceException 
	 * @throws RemoteException 
	 * @throws Exception
	 */
	private static String getAuroraAccNumExt
	 (Account account, AuroraClient auroraClient) 
	 throws RemoteException, ServiceException {
		return AuroraWebServiceHandler.getAuroraWS().
			getPendingAccountNumberExternalByClientNumberAndAccountNumber(
					auroraClient.getCompany().getCode(), 
					account.getFundCode(), 
					auroraClient.getClientNumber(), 
					account.getAccountNumber());
	}
	
	
	private static void setDefaultValues(Account account) {
		account.setAdditionalSignatoryName1("");
		account.setAdditionalSignatoryPostcode1("");
		account.setAdditionalSignatoryTelephone1("");

		account.setAdditionalSignatoryName2("");
		account.setAdditionalSignatoryPostcode2("");
		account.setAdditionalSignatoryTelephone2("");
		
		account.setAdditionalSignatoryName3("");
		account.setAdditionalSignatoryPostcode3("");
		account.setAdditionalSignatoryTelephone3("");
		
		account.setAdditionalSignatoryName4("");
		account.setAdditionalSignatoryPostcode4("");
		account.setAdditionalSignatoryTelephone4("");
		
		account.setAdditionalSignatoryName5("");
		account.setAdditionalSignatoryPostcode5("");
		account.setAdditionalSignatoryTelephone5("");
		
		account.setAdditionalSignatoryName6("");
		account.setAdditionalSignatoryPostcode6("");
		account.setAdditionalSignatoryTelephone6("");
		
		account.setAdditionalSignatoryName7("");
		account.setAdditionalSignatoryPostcode7("");
		account.setAdditionalSignatoryTelephone7("");
		
		account.setAdditionalSignatoryName8("");
		account.setAdditionalSignatoryPostcode8("");
		account.setAdditionalSignatoryTelephone8("");
		
		account.setAdditionalSignatoryName9("");
		account.setAdditionalSignatoryPostcode9("");
		account.setAdditionalSignatoryTelephone9("");
		
		account.setAdditionalSignatoryName10("");
		account.setAdditionalSignatoryPostcode10("");
		account.setAdditionalSignatoryTelephone10("");
		
		account.setIVSReferenceNumber("");
		account.setMandateLetterSentDate("");
		account.setMandateLetterSentIndicator("");
		account.setMultipleSignaturesInformation("");
		account.setMandatedCompany("");
		account.setMandatedAccount("");
		account.setIVSCFID("");
	}
	
	
	/**
	 * Aurora Account Validation Utility. Based on validateAccount webservice logic from 
	 * Aurora
	 * 
	 * if throwErrorOnFail flag is set to true, a DataException is thrown if the data
	 * fails validation checks.
	 * 
	 * if isValidateAll is set to true, nominated bankaccount are checked as well. This 
	 * required an account to be created in the central database as it checks the 
	 * relations and accounts. 
	 * 
	 * @param accountId
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
	 (Integer accountId, Integer mandatedAccountId, String incomeDistributionMethod, 
	 String fundCode, boolean throwErrorOnFail, boolean isValidateAll, FWFacade facade)
	 throws DataException {

		boolean isValid = true;
		String errorMessage = null;

		com.ecs.ucm.ccla.data.Account account = null;
		Fund fund = null;
		Fund mandatedFund = null;
		FundTypeCode fundTypeCode = null;
		FundIncomeTypeCode fundIncomeTypeCode = null;

		
		try {
			/********************************************************************************************/
			/********************************* Validation of Base Data  *********************************/
			/********************************************************************************************/
			
			//1. Income Distribution Method must not be empty
			if (StringUtils.stringIsBlank(incomeDistributionMethod)) {
				isValid = false;
				errorMessage = "Account, No income distribution method selected.";	
			}
			
	
			//2. Check Fund and related fund attributes
			if (isValid) {
				if (StringUtils.stringIsBlank(fundCode)) {
					isValid = false;
					errorMessage = "Account, Fund code is empty. Please select a fund code.";
				}
			}
			
			if (isValid) {
				fund = Fund.getCache().getCachedInstance(fundCode);
				if (fund==null) {
					isValid = false;
					errorMessage = "Account, Cannot find fund with fund code "+fundCode+".";
				}
			}
	
			//Type code is either UNI (Unitised), DEP (Deposit) or PSIC (Public Sector Fund)
			//NB in Aurora PSIC funds is set up as UNI.
			if (isValid) {
				fundTypeCode = fund.getTypeCode();
				if (fundTypeCode==null) {
					isValid = false;
					errorMessage = "Fund doesn't have a type code, fund Code: "+fundCode+".";
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
				if (incomeDistributionMethod.equals(com.ecs.ucm.ccla.data.Account.IncomeDistMethod.PAYA.toString())
						&& !fund.isAllowPAYA()) {
					isValid = false;
					errorMessage = "Income Distribution Method PAYA is not valid for fund "+fundCode+".";
				} else if (incomeDistributionMethod.equals(com.ecs.ucm.ccla.data.Account.IncomeDistMethod.RETN.toString())
						&& !fund.isAllowRETN()) {
					isValid = false;
					errorMessage = "Income Distribution Method RETN is not valid for fund "+fundCode+".";
				} else if (incomeDistributionMethod.equals(com.ecs.ucm.ccla.data.Account.IncomeDistMethod.TXRI.toString())
						&& !fund.isAllowTXRI()) {
					isValid = false;
					errorMessage = "Income Distribution Method TXRI is not valid for fund "+fundCode+".";
				} else if (incomeDistributionMethod.equals(com.ecs.ucm.ccla.data.Account.IncomeDistMethod.REIN.toString())
						&& !fund.isAllowREIN()) {
					isValid = false;
					errorMessage = "Income Distribution Method REIN is not valid for fund "+fundCode+".";
				}
			}
			
			/********************************************************************************************/
			/********************* Validation of Bank Accounts Against PAYA  ****************************/
			/********************************************************************************************/
			if (isValid) {
				if (incomeDistributionMethod.equals(com.ecs.ucm.ccla.data.Account.IncomeDistMethod.PAYA.toString())) {
					
					//Full Validation
					if (isValidateAll) {
						if (accountId==null || accountId==0) {
							isValid = false;
							errorMessage = "Cannot perform full validation, Account ID is null.";
						}
						
						if (isValid) {
							account = com.ecs.ucm.ccla.data.Account.get(accountId, facade);
							
							if (account==null) {
								isValid = false;
								errorMessage = "Cannot perform full validation, cannot find account with id "+accountId+".";
							}
							
							if (isValid) {
								RelationName incomeRel= 
									RelationName.getCache().getCachedInstance(
											RelationName.AccountBankAccountRelation.INCOME);
								
								BankAccount nomIncomeAcc =
									com.ecs.ucm.ccla.data.Account.getNominatedBankAccount(
											account.getAccountId(), incomeRel, facade);
								
								if (nomIncomeAcc==null) {
									isValid = false;
									errorMessage = "Nominated Bank Account (Income) must be populated for IncomeDistributionMethods.PAYA.";
								} else {
									try {
										nomIncomeAcc.validate(facade);
									} catch (DataException de) {
										isValid = false;
										errorMessage = "Nominated Income Account Validation Error :"+de.getMessage();
									}
								}
							}
						}
					}
				} else if (incomeDistributionMethod.equals(com.ecs.ucm.ccla.data.Account.IncomeDistMethod.REIN.toString()) ||
						incomeDistributionMethod.equals(com.ecs.ucm.ccla.data.Account.IncomeDistMethod.RETN.toString()) ||
						incomeDistributionMethod.equals(com.ecs.ucm.ccla.data.Account.IncomeDistMethod.TXRI.toString())) {

					//Full Validation
					if (isValidateAll) {
						if (accountId==null || accountId==0) {
							isValid = false;
							errorMessage = "Cannot perform full validation, Account ID is null.";
						}
						
						if (isValid) {
							account = com.ecs.ucm.ccla.data.Account.get(accountId, facade);
							
							if (account==null) {
								isValid = false;
								errorMessage = "Cannot perform full validation, cannot find account with id "+accountId+".";
							}
							
							if (isValid) {
								RelationName incomeRel= 
									RelationName.getCache().getCachedInstance(
											RelationName.AccountBankAccountRelation.INCOME);
								
								BankAccount nomIncomeAcc =
									com.ecs.ucm.ccla.data.Account.getNominatedBankAccount(
											account.getAccountId(), incomeRel, facade);
								
								if (nomIncomeAcc!=null) {
									isValid = false;
									errorMessage = "Nominated Bank Account (Income) must be not be populated for this IncomeDistributionMethods.";
								}
							}
						}
					}					
					
					
					if (mandatedAccountId == null) {
						
						if (fund.getCompany()==null) {
							isValid = false;
							errorMessage = "Mandated Company must be populated for selected Income Distribution Method. - Please ensure that fund has a company.";							
						}
						
						//Temp fix for PSDF account due to the nature of how it is setup.
						if (fund.getTypeCode().getFundTypeCodeId()==FundTypeCode.FUND_TYPECODE_ID_PSDF) {
							if (isValid && !fund.isAllowSameMandatedAccountFund() && isValidateAll){
								isValid = false;
								errorMessage = "Fund "+fund.getFundCode()+" does not allow reinvestment back into same Account for Fund - Please specify Mandated Account.";								
							}
						} else {
							if (isValid && !fund.isAllowSameMandatedAccountFund()){			
								isValid = false;
								errorMessage = "Fund "+fund.getFundCode()+" does not allow reinvestment back into same Account - Please specify Mandated Account.";
							}
						}
						
						if (isValid && incomeDistributionMethod.equals(com.ecs.ucm.ccla.data.Account.IncomeDistMethod.TXRI.toString())) {
							isValid = false;
                            errorMessage = "Mandated Account must be populated for selected Income Distribution Method TXRI.";
						}						
					} else {
						
						//Get mandated Account
						com.ecs.ucm.ccla.data.Account mandatedAccount = 
							com.ecs.ucm.ccla.data.Account.get(mandatedAccountId, facade);
						if (mandatedAccount==null) {
							isValid = false;
							errorMessage = "Cannot find mandated account with id :"+mandatedAccountId+".";
						}						
						
						
						//Check mandated fund setup.
						if (isValid) {
							if (StringUtils.stringIsBlank(mandatedAccount.getFundCode())) {
								isValid = false;
								errorMessage = "Mandated Account "+mandatedAccountId+" does not have a fund code.";
							} else {
								mandatedFund = Fund.getCache().getCachedInstance(mandatedAccount.getFundCode());
								if (mandatedFund==null) {
									isValid = false;
									errorMessage = "Cannot find fund for mandated account with id "+mandatedAccountId
										+", and fund "+mandatedAccount.getFundCode()+".";
								} else {
									if (mandatedFund.getTypeCode()==null) {
										isValid = false;
										errorMessage = "Mandated Fund doesn't have a type code, mandated fund Code: "+mandatedFund.getTypeCode()+".";
									} 
								}
								
								if (mandatedFund.getCompany()==null) {
									isValid = false;
									errorMessage = "Mandated Company must be populated for selected Income Distribution Method. - Please ensure that mandated fund has a company.";							
								}
							}
						}
						
						//Check income distribution methods against mandated account fund type code.
						if (isValid) {
							if ((incomeDistributionMethod.equals(com.ecs.ucm.ccla.data.Account.IncomeDistMethod.RETN.toString()) ||
									incomeDistributionMethod.equals(com.ecs.ucm.ccla.data.Account.IncomeDistMethod.TXRI.toString())) && 
									mandatedFund.getTypeCode().getFundTypeCodeId()!=FundTypeCode.FUND_TYPECODE_ID_DEPOSIT) {
								isValid = false;
								errorMessage = "Mandated Account must be a Deposit type account for this income distribution method."; 
							} else if (incomeDistributionMethod.equals(com.ecs.ucm.ccla.data.Account.IncomeDistMethod.REIN.toString()) && 
									(mandatedFund.getTypeCode().getFundTypeCodeId()!=FundTypeCode.FUND_TYPECODE_ID_UNITIZED &&
									mandatedFund.getTypeCode().getFundTypeCodeId()!=FundTypeCode.FUND_TYPECODE_ID_PSDF)) {
								isValid = false;
								errorMessage = "Mandated Account must be a Unitized/PSIC type account for this income distribution method."; 							
							}
						}
						
						
						//PSIC Validation Logic
						if (isValid) {
							if (incomeDistributionMethod.equals(com.ecs.ucm.ccla.data.Account.IncomeDistMethod.REIN.toString())) {
								//Non PSIC accounts, account and mandated account must belong to the same fund code 
								if (fund.getCompany().getCompanyId()==Company.PSIC) {
										
									if (fundTypeCode.getFundTypeCodeId()!=FundTypeCode.FUND_TYPECODE_ID_PSDF 
											&& mandatedFund.getTypeCode().getFundTypeCodeId()!=FundTypeCode.FUND_TYPECODE_ID_PSDF) {
										isValid = false;
										errorMessage =  "An Account in a share class fund must reinvest in a fund that reinvests its capital balance.";
									}
									
									if (isValid && mandatedFund.getFundCode().equals(fund.getFundCode())) {
										isValid = false;
										errorMessage = "An Account in a fund that reinvests it capital balance cannot reinvest back into the same fund.";
									}
								}
							}
						}
						
						//Mandated Fund and Fund checks.
						if (isValid) {
							if (fund.getFundCode().equals(mandatedFund.getFundCode()) &&
									!fund.isAllowSameMandatedAccountFund()) {
								isValid = false;
								errorMessage = "Fund "+fund.getFundCode()+" does not allow reinvestment into another account in the same fund.";
							} 
							else if (!fund.getFundCode().equals(mandatedFund.getFundCode()) && 
										incomeDistributionMethod.equals(com.ecs.ucm.ccla.data.Account.IncomeDistMethod.REIN.toString()) &&
											!fund.isAllowREINMandatedAccountInOtherFunds()) 
							{
								isValid = false;
								errorMessage = "Fund "+fund.getFundCode()+" does not allow reinvestment into an account in the another fund.";
							} 
							else if (!fund.getFundCode().equals(mandatedFund.getFundCode())) 
							{
								String key = fund.getFundCode()+"|"+mandatedFund.getFundCode();
								String value = Fund.getInterFundCache().getCachedInstance(key);
								
								if (StringUtils.stringIsBlank(value)) {
									isValid = false;
									errorMessage = "Inter-Fund Transfer between fund "+fund.getFundCode()+" - "+mandatedFund.getFundCode()+" is invalid.";
								}
							}
						}
					}
				}
				
				//Check withdrawal bank account if populated
				if (isValidateAll) {
					
					if (isValid) {
						RelationName withdrawalRel= 
							RelationName.getCache().getCachedInstance(
								RelationName.AccountBankAccountRelation.WITHDRAWAL);
						
						BankAccount nomWithdrawalAcc =
							com.ecs.ucm.ccla.data.Account.getNominatedBankAccount(
									account.getAccountId(), withdrawalRel, facade);
						
						if (nomWithdrawalAcc==null && REQUIRE_ACCOUNT_WITHDRAWAL_BANK_ACCOUNT) {
							isValid = false;
							errorMessage = "Nominated Bank Account (Withdrawal) must be populated.";
						} else {							
							if (nomWithdrawalAcc!=null) { 
								try {
									nomWithdrawalAcc.validate(facade);
								} catch (DataException de) {
									isValid = false;
									errorMessage = "Nominated Withdrawal Account Validation Error :"+de.getMessage();
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
		
		Log.debug("Completed Validating account -- AccountID:"+accountId+
				", isValid="+isValid+", errorMessage="+(StringUtils.stringIsBlank(errorMessage)?"":errorMessage));		
		
		Log.debug("Completed Validating account -- AccountID:"+accountId+
				", isValid="+isValid+", errorMessage="+(StringUtils.stringIsBlank(errorMessage)?"":errorMessage));		
		
		AccountValidationOutcome outcome = new AccountValidationOutcome
		 (isValid, errorMessage);
		
		if (!throwErrorOnFail || isValid) {
			return outcome;
		} else {
			throw new DataException(errorMessage);
		}
	}
	
	public static AccountValidationOutcome validateAuroraAccount
	 (com.ecs.ucm.ccla.data.Account account, boolean isPersist, boolean isValidateAll, 
	 FWFacade facade) throws DataException {
			
		Integer mandatedAccountId = account.getMandatedAccId();
		String incomeDistributionMethod = account.getIncomeDistMethod();
		String fundCode = account.getFundCode();

		return AuroraAccountHandler.validateAuroraAccount
		 (account, mandatedAccountId, incomeDistributionMethod, fundCode, isPersist, 
		 isValidateAll, facade);
	}
}
