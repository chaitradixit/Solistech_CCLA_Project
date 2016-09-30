package com.ecs.stellent.ccla.clientservices;

//import java.util.HashMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;

import com.ecs.stellent.ccla.clientservices.campaign.CommunityFirstClientEnrolmentHandler;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.aurora.AuroraAccountHandler;
import com.ecs.ucm.ccla.commproc.InstructionUtils;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.Account.IncomeDistMethod;
import com.ecs.ucm.ccla.data.comm.Comm;
import com.ecs.ucm.ccla.data.comm.CommGroup;
import com.ecs.ucm.ccla.data.comm.CommSource;
import com.ecs.ucm.ccla.data.comm.CommType;
import com.ecs.ucm.ccla.data.instruction.Instruction;
import com.ecs.ucm.ccla.data.instruction.InstructionData;
import com.ecs.ucm.ccla.data.instruction.InstructionDataApplied;
import com.ecs.ucm.ccla.data.instruction.InstructionDataFieldValue;
import com.ecs.ucm.ccla.data.instruction.InstructionStatus;
import com.ecs.ucm.ccla.data.instruction.InstructionType;
import com.ecs.ucm.ccla.data.subscription.Contribution;
import com.ecs.ucm.ccla.data.subscription.ContributionAssetAllocation;
import com.ecs.ucm.ccla.data.subscription.Subscription;
import com.ecs.ucm.ccla.data.subscription.SubscriptionAssetAllocation;
import com.ecs.ucm.ccla.data.AuroraClient;
import com.ecs.ucm.ccla.data.BankAccount;
import com.ecs.ucm.ccla.data.Company;
import com.ecs.ucm.ccla.data.Element;
import com.ecs.ucm.ccla.data.ElementAttribute;
import com.ecs.ucm.ccla.data.ElementAttributeApplied;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Fund;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.Property;
import com.ecs.ucm.ccla.data.Relation;
import com.ecs.ucm.ccla.data.RelationName;
import com.ecs.ucm.ccla.data.RelationPropertyApplied;
import com.ecs.ucm.ccla.data.RelationType;
import com.ecs.ucm.ccla.data.SystemConfigVar;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
//import com.ecs.utils.StringUtils;
import intradoc.shared.SharedObjects;

public class SubscriptionUtils {

	public static final String CF_PRINCIPAL_FUND =
		SharedObjects.getEnvironmentValue("CF_PRINCIPAL_FUND");
	
	public static final String CF_DEPOSIT_FUND =
		SharedObjects.getEnvironmentValue("CF_DEPOSIT_FUND");
	
	/** Whether or not a default deposit account is generated for every donor in the
	 *  one-off generateDonorAccounts function, regardless of the list of holdings
	 *  passed.
	 */
	public static final boolean AUTO_GENERATE_DONOR_DEPOSIT_ACCOUNT = true;
	
	/** One-off function for generating donor fund accounts
	 * 
	 * @param donorHoldings mapping of LCF orgs to their donor elements (can be Person or 
	 * 						Org) against a list of their required fund holdings.
	 * @param facade
	 * @throws DataException 
	 * @throws ServiceException 
	 */
	public static void generateDonorAccounts
	 (LinkedHashMap<Entity, LinkedHashMap<Element, List<Fund>>> lcfDonorHoldings, 
	 String userName, FWFacade facade) throws DataException, ServiceException {
		
		Log.debug("Generating Donor Accounts for " + lcfDonorHoldings.size() + 
		 " LCF Orgs");
		
		int lcfCount = 0;
	
		int newAccounts = 0;
		int existingAccounts = 0;
		
		Fund depositFund = Fund.getCache().getCachedInstance
		 (CommunityFirstClientEnrolmentHandler.CDF_DEPOSIT_FUNDCODE.toString());
		
		// Loop through LCF orgs.
		for (Entity lcfOrg : lcfDonorHoldings.keySet()) {
			lcfCount++;

			HashMap<Element, List<Fund>> lcfDonors = lcfDonorHoldings.get(lcfOrg);

			Log.debug("LCF " + lcfCount + "/" + lcfDonorHoldings.size() + " (" + 
			 lcfOrg.getOrgAccountCode() + "): " + lcfOrg.getOrganisationName() + ", " +
			 lcfDonors.size() + " associated donors");
			 
			int donorCount = 0;
			
			// Loop through LCF donors.
			for (Element donor : lcfDonors.keySet()) {
				donorCount++;
				List<Fund> donorHoldings = lcfDonors.get(donor);
				
				Log.debug("======================");
				Log.debug("LCF " + lcfCount + "/" + lcfDonorHoldings.size() + " (" + 
				 lcfOrg.getOrgAccountCode() + "): " + lcfOrg.getOrganisationName() + ", " +
				 lcfDonors.size() + " associated donors");
				
				String logPrefix = "Donor " + donorCount + "/" + lcfDonors.size();
				
				if (donor instanceof Person) {
					Person donorPerson = Person.get(donor.getElementId(), facade);
					Log.debug(logPrefix + " (Person): " + donorPerson.getFullName());
				} else {
					Entity donorOrg = Entity.get(donor.getElementId(), facade);
					Log.debug(logPrefix + " (Org): " + donorOrg.getOrganisationName());
				}

				if (AUTO_GENERATE_DONOR_DEPOSIT_ACCOUNT) {
					// Always create a deposit account.
					if (!donorHoldings.contains(depositFund)) {
						Log.debug("Deposit Fund not passed." +
						 " Adding to the list of donor holdings.");
						donorHoldings.add(depositFund);
					}
				}
				
				boolean strict = false;
				
				// Sort the list so the accounts are generated in predictable order.
				Collections.sort(donorHoldings);
				
				// Loop through donor holdings, generating 1 account per holding, with the
				// LCF set as the owner.
				for (Fund fund : donorHoldings) {
					
					// Check if the donor account already exists.
					Account existingAccount = getDonorAccount(
						lcfOrg.getOrganisationId(), 
						donor.getElementId(), 
						fund, 
						facade
					);
					
					if (existingAccount == null) {
						Log.debug("Generating Donor Account for fund " + fund.getFundCode());
						Account donorAccount  = createDonorAccount
						 (donor, lcfOrg, fund, strict, userName, facade);
						
						newAccounts++;
					} else {
						Log.debug(" Donor Account for fund " + fund.getFundCode() 
						 + " already existed");
						existingAccounts++;
					}
				}
			}
		}
		
		Log.debug("Donor Account generation complete. Created " + newAccounts + 
		 " new Donor accounts, " + existingAccounts + " already existed.");
	}
	
	/** Creates a single Community First Donor Account, linked to the given donor/LCF org
	 *  and Fund.
	 *  
	 * @param donor			must be a Person or Entity instance.
	 * @param lcfOrg
	 * @param fund
	 * @param strict		setting to true will invoke an Exception if the donor account
	 * 						already exists, otherwise a warning is printed to the log 
	 * 						instead.
	 * @param facade
	 * @throws ServiceException 
	 * @throws DataException 
	 */
	public static Account createDonorAccount
	 (Element donor, Entity lcfOrg, Fund fund, boolean strict, 
	 String userName, FWFacade facade)
	 throws DataException, ServiceException {
		
		// Check if the donor account already exists.
		Account existingAccount = getDonorAccount
		 (lcfOrg.getOrganisationId(), donor.getElementId(), fund, facade);
		
		if (existingAccount != null) {
			String msg = "Donor Account for Donor ID " 
			 + donor.getElementId() + ", LCF Org: " + lcfOrg.getOrgAccountCode() + 
			 ", Fund: " + fund.getFundCode() + " already exists (" + 
			 existingAccount.getAccountNumberString() + ")";
			
			if (strict)
				throw new ServiceException(msg);
			else {
				Log.warn(msg);
				return existingAccount;
			}
		}
		
		// Fetch Aurora client instance for this LCF.
		AuroraClient lcfAuroraClient = Entity.getAuroraClientCompanyMapping(
			lcfOrg.getOrganisationId(), 
			Company.getCache().getCachedInstance(Company.COIF), 
			facade
		);
		
		// Fetch the LCF's Comm. First account (Fund Code CF)
		Account lcfAccount = CommunityFirstClientEnrolmentHandler.getDonationAccount
		 (lcfOrg.getOrganisationId(), facade);
		
		if (lcfAccount == null) {
			throw new ServiceException("No CF Account found for Organisation: " 
			 + lcfOrg.getOrgAccountCode());
		}
		
		// Fail if the LCF Account isn't open - shouldn't be any donor accounts created
		// in this case, we've probably picked the wrong Org.
		if (lcfAccount.getStatus() != Account.AccountStatus.OPEN) {
			throw new ServiceException("Source CF Account for Organisation: " 
			 + lcfOrg.getOrgAccountCode() + " is closed/inactive. Please check the " +
			 "donor account is being created for the active LCF Organisation.");
		}
		
		Person corr = Account.getNominatedCorrespondent
		 (lcfAccount.getAccountId(), false, facade);
		
		if (corr == null)
			throw new ServiceException("Correspondent for source account ID " 
			 + lcfAccount.getAccountId() + " not found");
		
		Integer accountNum = getAccountNumberForLCFDonor
		 (lcfOrg.getOrganisationId(), donor.getElementId(), facade);
		
		String subtitle = null;
		
		//String incomeDistMethod = IncomeDistMethod.PAYA.toString();
		//Integer mandatedAccountId = null;
		
		// Set the inc. dist. method and mandated account ID to match the CF account.
		String incomeDistMethod = lcfAccount.getIncomeDistMethod();
		Integer mandatedAccountId = lcfAccount.getMandatedAccId();
		
		Integer corrId = corr.getPersonId();
		Integer reqSigs = lcfAccount.getRequiredSignatures();
		
		RelationName donorRelName;
		
		// Determine Account subtitle to use
		if (donor instanceof Person) {
			//subtitle = ((Person)donor).getAccountCode() 
			//			+ " - " + ((Person)donor).getFullName();
			
			subtitle = ((Person)donor).getFullName();
			
			donorRelName = RelationName.getCache().getCachedInstance
			 (RelationName.OrganisationPersonRelation.DONOR);
			
		} else {
			//subtitle = ((Entity)donor).getOrgAccountCode()
			//			+ " - " + ((Entity)donor).getOrganisationName();
			
			subtitle = ((Entity)donor).getOrganisationName();
			
			donorRelName = RelationName.getCache().getCachedInstance
			 (RelationName.OrganisationOrganisationRelation.DONOR);
		}
		
		if (subtitle.length() > 64)
			subtitle = subtitle.substring(0, 64);

		// This takes care of the Element/Account DB entities, plus the Correspondent
		// relation.
		Account account = Account.add(
			lcfOrg, 
			lcfAuroraClient, 
			true, 
			fund, 
			accountNum, 
			subtitle, 
			Account.AccountStatus.OPEN, 
			reqSigs, 
			incomeDistMethod, 
			false, 
			mandatedAccountId, 
			null, 
			null, 
			null, 
			Account.AgreementType.SCHEME_PARTICULAR, 
			corrId, 
			facade, 
			userName
		);
	
		Log.debug("Created Account for Fund " + fund.getFundCode() + ", ID " 
		 + account.getAccountId());
		
		// Set the Donor ID as an applied element attribute, so we can trace this account 
		// back to the donor in future.
		ElementAttributeApplied.add(
			account.getAccountId(), 
			ElementAttribute.AccountAttributes.DONOR_ELEMENT_ID, 
			true, 
			Integer.toString(donor.getElementId()), 
			userName, 
			facade, 
			null
		);
		
		// Now add any other required relations, e.g. bank details. These are generally
		// copied from the LCF Org, or their CF account.
		RelationName corrRelName = RelationName.getCache().getCachedInstance
		 (RelationName.PersonAccountRelation.CORRESPONDENT);
		
		// First mark the correspondent relation as Default/Nominated.
		// ======================
		Vector<Relation> corrRelations = Relation.getRelations(
			corr.getPersonId(), 
			account.getAccountId(), 
			null, 
			corrRelName, 
			facade
		);
		
		RelationPropertyApplied.add(
			Property.getCache().getCachedInstance(Property.Ids.DEFAULT), 
			corrRelations.get(0), 
			null, 
			true, 
			facade, 
			userName
		);
		
		// Do Nominated Bank Accounts
		// ==================
		
		// Fetch the Nominated Withdrawal Bank Account from the source account. Fail
		// if one doesn't exist.
		
		// Set the bank account as the Nominated Withrawal/Income account - ignoring any
		// other bank accounts linked to the source account.
		
		RelationName withdrawalRelation = RelationName.getCache().getCachedInstance
		 (RelationName.AccountBankAccountRelation.WITHDRAWAL);
		RelationName incomeRelation = RelationName.getCache().getCachedInstance
		 (RelationName.AccountBankAccountRelation.INCOME);
		
		DataResultSet withdrawalBankAccounts = Relation.getAccountBankAccountsData(
			lcfAccount.getAccountId(), 
			withdrawalRelation, 
			facade
		);
		
		// Get the nominated withdrawal account from the previous ResultSet 
		//(must actually be marked as nominated in the DB)
		BankAccount nomWithdrawalBankAcc = Account.getNominatedBankAccount
		 (withdrawalBankAccounts);
		
		if (nomWithdrawalBankAcc == null) {
			throw new ServiceException("Nominated Withdrawal Bank Account for Account ID " 
			 + lcfAccount.getAccountId() + " not found");
		}
		
		// Add Withdrawal Account-Bank Account relationship
		Relation withdrawalRel = Relation.add(
			account.getAccountId(), 
			nomWithdrawalBankAcc.getBankAccountId(), 
			withdrawalRelation, 
			facade, 
			userName
		);
		
		// Add Default/Nominated Flag to the new bank account relationship
		RelationPropertyApplied.add(
			Property.getCache().getCachedInstance(Property.Ids.DEFAULT), 
			withdrawalRel, 
			null, 
			true, 
			facade, 
			userName
		);
		
		// Add Income Account-Bank Account relationship
		Relation incomeRel = Relation.add(
			account.getAccountId(), 
			nomWithdrawalBankAcc.getBankAccountId(), 
			incomeRelation, 
			facade, 
			userName
		);
		
		// Add Default/Nominated Flag to the new bank account relationship
		RelationPropertyApplied.add(
			Property.getCache().getCachedInstance(Property.Ids.DEFAULT), 
			incomeRel, 
			null, 
			true, 
			facade, 
			userName
		);
		
		Log.debug("Added nominated withdrawal/income bank account: "
		 + nomWithdrawalBankAcc.toString());
		
		// Do all related Persons
		// ======================
		// Nominated correspondent will already be there, other persons won't be.
		// Copy all person relations from the source lcfAccount
		
		Vector<Relation> personAccRelations = Relation.getRelations(
			null, 
			lcfAccount.getAccountId(), 
			RelationType.getCache().getCachedInstance(RelationType.PERSON_ACCOUNT), 
			null, facade);
		
		for (Relation r : personAccRelations) {
			
			if (r.getRelationName().equals(corrRelName)
				&& r.getElementId1() == corr.getPersonId())
				continue; // Correspondent relation, we already have this, move along.
			
			Relation.add(
				r.getElementId1(), 
				account.getAccountId(), 
				r.getRelationName(), 
				facade, 
				userName
			);
		}
		
		// Set the Donor relationship between the Org/Person donor and the LCF Org,
		// if it doesn't already exist.
		// ====================
		
		// The ordering of Donor ID / LCF Org ID depends on the relationship type we
		// use, i.e. whether the Donor is a Person or Organisation.
		Integer elemId1 = (donor instanceof Person) ? lcfOrg.getOrganisationId() : donor.getElementId();
		Integer elemId2 = (donor instanceof Person) ? donor.getElementId() : lcfOrg.getOrganisationId();
		
		Vector<Relation> donorRelations = Relation.getRelations(
			elemId1, 
			elemId2, 
			null, 
			donorRelName, 
			facade
		);
		
		if (donorRelations.isEmpty()) {
			Relation.add(
				elemId1, 
				elemId2,
				donorRelName,
				facade, 
				userName
			);
		}
		
		return account;
	}
	
	/** Returns the donor's fund holding under the given LCF Org ID, null if it doesn't
	 *  exist.
	 *  
	 * @param lcfOrgId
	 * @param donorId
	 * @param fund
	 * @return
	 * @throws DataException 
	 */
	public static Account getDonorAccount
	 (int lcfOrgId, int donorId, Fund fund, FWFacade facade) throws DataException {
		
		List<Account> lcfDonorAccounts = getLCFDonorAccounts(lcfOrgId, donorId, facade);

		for (Account account : lcfDonorAccounts) {
			// Found an existing donor account for this LCF/Donor.
			// Check to see if the Fund Code matches.
			if (account.getFundCode().equals(fund.getFundCode()))
				return account;
		}
		
		// No account for this LCF/Donor/Fund.
		return null;
	}
	
	/** Returns a list of all Donor Accounts belonging to a particular LCF and Donor.
	 * 
	 * @param lcfOrgId
	 * @param donorId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static List<Account> getLCFDonorAccounts
	 (int lcfOrgId, int donorId, FWFacade facade) throws DataException {
		
		// Search for any existing accounts this donor is tied to.
		// The donor's Element ID will be set as a particular Element Attribute value
		// against these accounts.
		ElementAttribute donorElementAttr = ElementAttribute.getCache().getCachedInstance
		 (ElementAttribute.AccountAttributes.DONOR_ELEMENT_ID);
		
		List<ElementAttributeApplied> attrs = ElementAttributeApplied.getByValue
		 (donorElementAttr, Integer.toString(donorId), facade);
		
		List<Account> accounts = new ArrayList<Account>();
		
		// Step through all these, seeing if the account's owning Organisation matches
		// the passed LCF Org ID.
		for (ElementAttributeApplied attr : attrs) {
			if (Account.getOwnerOrganisationId(attr.getElementId(), facade) 
				== lcfOrgId) {
				// Found an existing donor account for this LCF/Donor.
				accounts.add(Account.get(attr.getElementId(), facade));
			}
		}
		
		return accounts;
	}
	
	private static final String[] DONOR_ACCOUNT_RESULTSET_COLS = new String[] {
		"ACCOUNT_ID",
		"FUND_CODE",
		"ACCNUMEXT"
	};
	
	/** Returns a ResultSet of basic Donor Accounts data, belonging to a particular LCF 
	 *  and Donor.
	 * 
	 * @param lcfOrgId
	 * @param donorId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getLCFDonorAccountsData
	 (int lcfOrgId, int donorId, FWFacade facade) throws DataException {
		
		List<Account> accounts = getLCFDonorAccounts(lcfOrgId, donorId, facade);
		DataResultSet rsAccounts = new DataResultSet(DONOR_ACCOUNT_RESULTSET_COLS);
		
		for (Account account : accounts) {
			Vector<String> row = new Vector<String>();
			row.add(Integer.toString(account.getAccountId()));
			row.add(account.getFundCode());
			row.add(account.getAccNumExt());
		
			rsAccounts.addRow(row);
		}
		
		return rsAccounts;
	}
	
	/** 
	 * Must be queried when creating new donor accounts, to determine the Account Number
	 * to use.
	 * 
	 * If the donor already has accounts under the given LCF, the Account Number is copied
	 * from the first of these accounts.
	 * 
	 * If the donor has no accounts under the LCF, the next highest unique Account Number
	 * is fetched instead.
	 * 
	 * @return
	 * @throws DataException 
	 */
	private static int getAccountNumberForLCFDonor
	 (int lcfOrgId, int donorId, FWFacade facade) throws DataException {
		
		// Search for any existing accounts this donor is tied to.
		List<Account> lcfDonorAccounts = getLCFDonorAccounts(lcfOrgId, donorId, facade);
		
		if (!lcfDonorAccounts.isEmpty()) {
			// At least 1 donor account exists for the LCF. Return its Account Number.
			return lcfDonorAccounts.get(0).getAccountNumber();
		}
		
		// Donor doesn't have any existing donor accounts under this LCF.
		// Determine the next available account number index to use. It must be unique
		// across ALL the LCF's accounts.
		DataResultSet rsMaxAccountNumbers = Account.getMaxAccountNumbersForOrg
		 (lcfOrgId, facade);
		
		int newAccountNumber = 1;
		
		if (rsMaxAccountNumbers.first()) {
			do {
				Integer accountTypeId = CCLAUtils.getResultSetIntegerValue
				 (rsMaxAccountNumbers, Account.Cols.ACCOUNT_TYPE);
				
				if (accountTypeId == Account.AccountType.DEPOSIT
					||
					accountTypeId == Account.AccountType.UNITIZED) {
					Integer maxNumber = CCLAUtils.getResultSetIntegerValue
					 (rsMaxAccountNumbers, "MAX_ACCOUNTNUMBER");
					
					if (maxNumber >= newAccountNumber)
						newAccountNumber = maxNumber+1;	
				}
				
			} while (rsMaxAccountNumbers.next());
		}
		
		// Guaranteed to be the next-highest unused account number across all Deposit and
		// Unitized accounts belonging to the LCF.
		return newAccountNumber;
	}
	
	/** Generates any missing Donor accounts required to fulfill payments against this
	 *  Subscription.
	 *  
	 *  A Deposit Fund account is always generated for each Donor, even if there is no
	 *  asset allocation to the Deposit Fund.
	 *  
	 *  Donor Account creations are run in non-strict mode, so there is no error if the
	 *  account already exists.
	 *  
	 * @param subscriptionId
	 * @param binder			optional parameter - if specified, the RedirectUrl is
	 * 							modified at the end of the service to add counter 
	 * 							values to display on the screen.
	 * @param facade
	 * @throws DataException 
	 * @throws ServiceException 
	 */
	public static void createDonorAccountsForSubscription
	 (Integer subscriptionId, DataBinder binder, String userName, FWFacade facade) 
	 throws DataException, ServiceException {
		
		Log.debug("Generating Donor Accounts for Subscription ID: " + subscriptionId);
		
		Vector<Contribution> contributions = Contribution.getAllBySubscriptionId
		 (subscriptionId, facade);

		if (contributions.isEmpty()) {
			throw new ServiceException("Unable to create required Donor Accounts: " +
			 "subscription has no donors selected");
		}
		
		// The benefactor ID is the LCF Org ID in this case.
		Entity org = Entity.get(contributions.get(0).getBenefactorId(), facade);
		
		Fund depositFund = Fund.getCache().getCachedInstance
		 (CommunityFirstClientEnrolmentHandler.CDF_DEPOSIT_FUNDCODE.toString());
		
		// Build a list of Donor Accounts as they are fetched/generated.
		List<Account> donorAccounts = new ArrayList<Account>();
		
		int numFailedAccounts = 0;
		
		for (Contribution contribution : contributions) {
			// Contributor = Donor.
			Integer donorId = contribution.getContributorId();
			Element donorElem = Element.get(donorId, facade);
			
			// Fetch their asset allocations.
			Vector<ContributionAssetAllocation> assetAllocs = ContributionAssetAllocation
			 .getAllByContributiontId(contribution.getId(), facade);
			
			if (donorElem.getType().equals(ElementType.PERSON))
				donorElem = Person.get(donorId, facade);
			else if (donorElem.getType().equals(ElementType.ORGANISATION))
				donorElem = Entity.get(donorId, facade);
			else
				throw new ServiceException("Invalid Donor ID: " + donorId);
			
			boolean depositFundUsed = false;
	
			for (ContributionAssetAllocation assetAlloc : assetAllocs) {
				Fund fund = Fund.getCache().getCachedInstance(assetAlloc.getFundCode());
				
				try {
					Account account = createDonorAccount
					 (donorElem, org, fund, false, userName, facade);
					
					donorAccounts.add(account);
				} catch (Exception e) {
					// Some accounts may fail to generate, due to invalid
					// configuration on the CF account. Ensure these are reported on.
					Log.error(e.getMessage(), e);
					numFailedAccounts++;				
				}
				
				if (fund.equals(depositFund))
					depositFundUsed = true;
			}
			
			if (!depositFundUsed) {
				// Force creation of a Deposit Fund account for this donor.
				try {
					Account account = createDonorAccount
						(donorElem, org, depositFund, false, userName, facade);
					donorAccounts.add(account);
				} catch (Exception e) {
					// Some accounts may fail to generate, due to invalid
					// configuration on the CF account. Ensure these are reported on.
					Log.error(e.getMessage(), e);
					numFailedAccounts++;				
				}
			}
		}
		
		Log.debug("Fetched/created " + donorAccounts.size() + 
		 " required donor accounts for this Subscription.");
		
		// Dispatch CREATE_ACCOUNT Static Data Update instructions for each account, 
		// where each of the following is true:
		// - No existing CREATE_ACCOUNT instruction for the account
		// - Account doesn't exist in Aurora
		int numSDUInstructions = 0;
		for (Account account : donorAccounts) {
			Instruction instr = 
			 createSDUInstructionForDonorAccount(org, account, facade, userName);
			
			if (instr != null)
				numSDUInstructions++;
		}
		
		
		if (binder != null) {
			CCLAUtils.appendToBinderParam(binder, "RedirectUrl", "&numAccounts=" 
			 + donorAccounts.size() + "&numFailedAccounts=" + numFailedAccounts
			 + "&numSDUInstructions=" + numSDUInstructions 
			 + "#donorContributions");
		}
	}
	
	/** Generates a CREATE_ACCOUNT Static Data Update instruction for the passed Donor
	 *  Account, if there isn't already a pending instruction, and the account doesn't
	 *  already exist in Aurora.
	 *  
	 * @throws DataException 
	 */
	private static Instruction createSDUInstructionForDonorAccount
	 (Entity lcfOrg, Account account, FWFacade facade, String userName) 
	 throws DataException {
		
		Log.debug("Checking Aurora status of Donor Account " + account.getAccNumExt() 
		 + ", ID: " + account.getAccountId());

		// Will always be COIF.
		Company accountCompany = Fund.getCache().getCachedInstance
		 (account.getFundCode()).getCompany();

		// Search for existing CREATE_ACCOUNT instruction.
		InstructionType createAccountInstrType = InstructionType.getIdCache()
		 .getCachedInstance(InstructionType.Ids.CREATE_ACCOUNT);
		
		Integer createAccountInstrId = 
		 InstructionUtils.getLatestSDUInstructionIdByValues(
			createAccountInstrType, 
			null, accountCompany.getCompanyId(), 
			lcfOrg.getOrganisationId(), 
			account.getAccountId(), 
			null, 
			facade
		);
		
		if (createAccountInstrId != null) {
			Log.debug("Pending CREATE_ACCOUNT instruction found. " +
			 "No need to generate another.");
			return null;
		}
		
		boolean accountExistsInAurora = false;
		
		// Aurora check is conditional, based on the config var switch below.
		// Should be switched off in dev.
		SystemConfigVar checkAuroraForDonorAccountsVar = 
		 SystemConfigVar.getCache().getCachedInstance
		 ("CommunityFirst_CheckAuroraForDonorAccounts");
		
		boolean checkAuroraForDonorAccounts = 
		 checkAuroraForDonorAccountsVar != null 
		 && checkAuroraForDonorAccountsVar.getBoolValue();
		
		if (checkAuroraForDonorAccounts) {
			AuroraAccountHandler auroraAccountHandler = new AuroraAccountHandler();
			
			// Secondary check: maybe the account exists in Aurora already.
			Log.debug("Checking Aurora for presence of Account " 
		     + account.getAccNumExt() 
			 + ", ID: " + account.getAccountId());

			if (auroraAccountHandler.getExistingAuroraEntity
				(account, accountCompany, facade) != null) {
				Log.debug("Aurora account exists.");
				accountExistsInAurora = true;
			} else {
				Log.debug("Aurora account not found.");
			}
		}
		
		if (!accountExistsInAurora) {
			Log.debug("Generating CREATE_ACCOUNT SDU Instruction");
			// No existing CREATE_ACCOUNT instruction. Generate one now.
			CommGroup commGroup = CommGroup.add(null, null, facade, userName);
			
			Comm comm = Comm.add(
				CommSource.SYSTEM, 
				CommType.USER, 
				null, 
				lcfOrg.getOrganisationId(), 
				new Date(), 
				userName, 
				null, 
				null, 
				commGroup.getCommGroupId(), 
				facade, 
				userName
			);
			
			Instruction instr = Instruction.add(
				comm.getCommId(), 
				InstructionStatus.PENDING_STATIC_DATA_AUTHORISATION,
				createAccountInstrType, 
				null, 
				null, 
				new Date(),
				userName, 
				facade
			);
			
			// Add required data fields.
			// ===============
			InstructionDataApplied.addOrUpdate(
				instr, 
				InstructionData.getCache().getCachedInstance
				 (InstructionData.Fields.COMPANY_ID), 
				accountCompany.getCompanyId(),
				facade, 
				userName
			);
			
			InstructionDataApplied.addOrUpdate(
				instr, 
				InstructionData.getCache().getCachedInstance
				 (InstructionData.Fields.ORGANISATION_ID), 
				 lcfOrg.getOrganisationId(),
				facade, 
				userName
			);
			
			InstructionDataApplied.addOrUpdate(
				instr, 
				InstructionData.getCache().getCachedInstance
				 (InstructionData.Fields.SOURCE_ACCOUNT_ID), 
				account.getAccountId(),
				facade, 
				userName
			);
			
			InstructionDataApplied.addOrUpdate(
				instr, 
				InstructionData.getCache().getCachedInstance
				 (InstructionData.Fields.INSTRUCTION_COMMENTS), 
				"Community First Donor Account. Instruction generated " +
				 "due to investment preferences on Donor Subscription Form",
				facade, 
				userName
			);
			
			Log.debug("Generated CREATE_ACCOUNT SDU Instruction with ID " 
			 + instr.getInstructionId());
			
			return instr;
		}
		
		return null;
	}
}
