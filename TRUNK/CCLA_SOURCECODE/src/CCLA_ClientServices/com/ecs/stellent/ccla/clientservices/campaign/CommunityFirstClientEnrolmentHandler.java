package com.ecs.stellent.ccla.clientservices.campaign;

import java.util.Vector;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.shared.SharedObjects;

import com.ecs.stellent.ccla.clientservices.campaign.DioLoanEnrolmentHandler.CustomActionIds;
import com.ecs.stellent.ccla.clientservices.form.CommunityFirstClientInfoFormHandler;
import com.ecs.stellent.ccla.clientservices.form.CommunityFirstOnBoardingCoveringLetterFormHandler;
import com.ecs.stellent.ccla.clientservices.form.SegregatedClientRegistrationFormHandler;
import com.ecs.stellent.ccla.clientservices.spool.CommunityFirstOnBoardingCoveringLetterSpoolFileGenerator;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.ElementAttributeApplied;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Fund;
import com.ecs.ucm.ccla.data.Account.AccountType;
import com.ecs.ucm.ccla.data.Fund.FundCodes;
import com.ecs.ucm.ccla.data.campaign.Campaign;
import com.ecs.ucm.ccla.data.campaign.CampaignActivityType;
import com.ecs.ucm.ccla.data.campaign.CampaignEnrolment;
import com.ecs.ucm.ccla.data.campaign.CampaignEnrolmentAction;
import com.ecs.ucm.ccla.data.campaign.CampaignMessages;
import com.ecs.ucm.ccla.data.campaign.FundInvestmentIntention;
import com.ecs.ucm.ccla.data.form.Form;
import com.ecs.ucm.ccla.data.form.FormType;
import com.ecs.ucm.ccla.data.subscription.Contribution;
import com.ecs.ucm.ccla.data.subscription.Subscription;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.LWFacade;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Handles Community First client enrolments
 * 
 * @author Tom
 *
 */
public class CommunityFirstClientEnrolmentHandler extends BaseEnrolmentHandler {
	
	public static final int CAMPAIGN_ID = 13;
	
	public static final String COMPANY_CODE = "COIF";
	public static final String SPOOL_FILE_CAMPAIGN_NAME = "Community First";
	
	public static final FundCodes CDF_DEPOSIT_FUNDCODE = Fund.FundCodes.C;
	
	/* Special Fund Code used for LCF Donation Accounts. No transactions are issued
	 * against these accounts - they are only used for linking/relational purposes */
	public static final String DONATION_ACCOUNT_FUND_CODE = Fund.FundCodes.CF.toString();
	
	/** ORGANISATION_ID of the Community Development Fund Organisation. Will differ
	 *  between environments.
	 */
	public static final Integer CDF_ORGANISATION_ID = Integer.parseInt
	 (SharedObjects.getEnvironmentValue("CCLA_CS_CommunityDevelopmentFundOrgId"));
	
	/** Returns the Community First Account Type attribute value associated with the
	 *  given Contribution Type ID.
	 *  
	 *  Used to select the correct CDF fund account for a given subscription type.
	 *  
	 * @param contributionTypeId
	 * @return
	 * @throws DataException 
	 */
	public static String getAccountType(int contributionTypeId) throws DataException {
		if (contributionTypeId 
			== Subscription.SubscriptionTypeIds.ENDOWMENT_ELIGIBLE_FOR_GOV_MATCH)
			return ElementAttributeApplied.CFAccTypeAttrValue.ENDOWMENT;
		else if (contributionTypeId 
			== Subscription.SubscriptionTypeIds.ENDOWMENT_NOT_ELIGIBLE_FOR_GOV_MATCH)
			return ElementAttributeApplied.CFAccTypeAttrValue.ENDOWMENT;
		else if (contributionTypeId 
			== Subscription.SubscriptionTypeIds.GIFT_AID_FOR_GRANTS_AND_SOCIAL_INVESTMENTS)
			return ElementAttributeApplied.CFAccTypeAttrValue.GIFT_AID;
		else
			throw new DataException("Unknown Community First Account Type for " +
			 "Subscription Type ID " + contributionTypeId);
	}
	
	/** Returns the CDF deposit account for the given subscription, based on the type
	 *  of its first contribution (we assume all Comm First subscription contributions
	 *  are of the same type!)
	 * 
	 * @param subscription
	 * @param facade
	 * @return
	 * @throws ServiceException 
	 * @throws DataException 
	 */
	public static Account getDepositAccount
	 (Subscription subscription, FWFacade facade) throws DataException {
		
		Vector<Contribution> contribs = Contribution.getAllBySubscriptionId
		 (subscription.getId(), facade);
		
		if (contribs.isEmpty())
			throw new DataException("Unable to resolve CDF deposit account for " +
			 "Subscription ID " + subscription.getId() + ", no contributions found");
		
		String commFirstAccountType = getAccountType
		 (contribs.get(0).getContributionTypeId());
		
		return Account.getCFAccount
		 (CDF_DEPOSIT_FUNDCODE.toString(), commFirstAccountType, facade);
	}
	
	/** Subtitle set against the single Community First account for the enrolled
	 *  Organisation.
	 *  
	 */
	public static final String COMM_FIRST_ACCOUNT_SUBTITLE = 
	 "Community First Donation Account";
	
	static class CustomActionIds {
		public static final int CREATE_COMM_FIRST_DONATION_ACCOUNT = 1; 
		public static final int CREATE_ONBOARDING_COVERING_LETTER = 2;
	}
	
	public CommunityFirstClientEnrolmentHandler(Campaign campaign, String username,
	 FWFacade facade) {
		super(campaign, username, facade);
	}

	public void applyAction(CampaignEnrolment enrolment,
	 CampaignEnrolmentAction action, String note) throws DataException,
	 ServiceException {

		if (action.getActionId() == CampaignEnrolmentAction.GENERATE_FORM_ACTION) {
			// Generate a Community First application/registration form.
			
			CommunityFirstClientInfoFormHandler formHandler = 
			 new CommunityFirstClientInfoFormHandler(this.username, this.facade);
			
			FormType formType = FormType.getCache().getCachedInstance
			 (FormType.COMMUNITY_FIRST_CLIENT_INFO);
			
			Entity org = Entity.get(enrolment.getOrganisationId(), facade);
			
			Form form = formHandler.generateForm(formType, org);
			
		} else if (action.getActionId() 
					== CampaignEnrolmentAction.GENERATE_ONBOARDING_COVERING_LETTER) {
			
			// Generate a Community First covering letter.
			CommunityFirstOnBoardingCoveringLetterFormHandler formHandler = 
			 new CommunityFirstOnBoardingCoveringLetterFormHandler
			 (this.username, this.facade);
			
			FormType formType = FormType.getCache().getCachedInstance
			 (FormType.COMMUNITY_FIRST_ONBOARDING_COVERING_LETTER);
			
			Entity org = Entity.get(enrolment.getOrganisationId(), facade);
			Form form = formHandler.generateForm(formType, org);
			
		} else {
			throw new ServiceException("Unknown enrolment action ID: "
			 + action.getActionId());
		}

	}

	public void applyCustomAction(CampaignEnrolment enrolment, int actionId,
	 String note, DataBinder extraParams) throws DataException,
	 ServiceException {
		if (actionId == CustomActionIds.CREATE_COMM_FIRST_DONATION_ACCOUNT) {
			addDonationAccount(enrolment, this.facade, this.username);
			
			enrolment.addActivity(
				enrolment.getPersonId(), 
				CampaignActivityType.ACCOUNT_CREATION_ACTIVITY_ID, 
				"Donation Account created", 
				note, 
				facade, 
				username);		
		} else {
			throw new ServiceException("Unbound Custom Action ID: " + actionId);
		}
	}

	public void validateIntention(FundInvestmentIntention intention,
			FWFacade facade) throws DataException, ServiceException {
		// TODO Auto-generated method stub
	}

	/** Generates the single Community First donation Account for this Organisation.
	 * 
	 *  Fails if the donation account already exists. 
	 *  
	 * @return
	 * @throws ServiceException 
	 * @throws DataException 
	 */
	private static Account addDonationAccount
	 (CampaignEnrolment enrolment, FWFacade facade, String userName) 
	 throws DataException, ServiceException {
			
		// Check if the account already exists.
		Account account = getDonationAccount(enrolment.getOrganisationId(), facade);
		
		if (account != null) {
			throw new DataException("Unable to create donation account: " +
			 "one already exists");
		}
		
		Entity org = Entity.get(enrolment.getOrganisationId(), facade);
		
		int requiredSigs = 2;
		
		account = Account.add(org, null, false, Fund.getCache().getCachedInstance
		 (DONATION_ACCOUNT_FUND_CODE), null, 
		 COMM_FIRST_ACCOUNT_SUBTITLE, Account.AccountStatus.OPEN, requiredSigs, 
		 Account.IncomeDistMethod.RETN.toString(), false, null, 
		 Account.AccountType.COMM_FIRST, 
		 null, null, Account.AgreementType.SCHEME_PARTICULAR, null, facade, userName);  
		
		return account;
	}
	
	/** Fetches the single Donation account for this Organisation, if one exists.
	 * 
	 * @param orgId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Account getDonationAccount(int orgId, FWFacade facade) 
	 throws DataException {
		Vector<Account> commFirstAccounts = Account.getAll(
		 Account.getClientAccountsDataByFundType
		 (orgId, DONATION_ACCOUNT_FUND_CODE, Account.AccountType.COMM_FIRST, facade));
	
		if (!commFirstAccounts.isEmpty()) {
			return commFirstAccounts.get(0);
		} else {
			return null;
		}
	}
	
	/** Returns a list of Funds that have the 'eligible for Community First' flag set.
	 *  
	 * @return
	 * @throws DataException 
	 */
	public static Vector<Fund> getAllFundsEligibleForCommunityFirst(FWFacade facade)
	 throws DataException {
		
		Vector<Fund> commFirstFunds = new Vector<Fund>();
		
		DataResultSet rsFunds = facade.createResultSet
		 ("qCore_GetAllFundsEligibleForCommunityFirst", new DataBinder());
		
		if (rsFunds.first()) {
			do {
				commFirstFunds.add(Fund.get(rsFunds));
			} while (rsFunds.next());
		}
		
		return commFirstFunds;
	}
	
	/** Returns a ResultSet of Funds from the cache that have the 'eligible for 
	 *  Community First' flag set.
	 *  
	 * @return
	 */
	public static DataResultSet getAllFundsDataEligibleForCommunityFirst
	 (FWFacade facade) throws DataException {
		return facade.createResultSet
		 ("qCore_GetAllFundsEligibleForCommunityFirst", new DataBinder());
	}
	
	/** Returns a list of Organisation IDs that are considered to be Top-Tier Local
	 *  Authorities (TTLAs)
	 *  
	 * @return
	 * @throws DataException 
	 */
	public static Vector<Integer> getAllTTLAOrganisationIds(FWFacade facade) 
	 throws DataException {
		
		DataResultSet rs = getAllTTLAOrganisationsData(facade);
		Vector<Integer> orgIds = new Vector<Integer>();
		
		if (rs.first()) {
			do {
				orgIds.add(CCLAUtils.getResultSetIntegerValue(rs, Entity.Cols.ID));
			} while (rs.next());
		}
		
		return orgIds;
	}
	
	/** Returns a ResultSet of Organisations that are considered to be Top-Tier Local
	 *  Authorities (TTLAs)
	 *  
	 *  The returned ResultSet is guaranteed to contain the ORGANISATION_ID, 
	 *  ORG_ACCOUNT_CODE and ORGANISATION_NAME.
	 *  
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getAllTTLAOrganisationsData(FWFacade facade) 
	 throws DataException {
		
		return facade.createResultSet
		 ("qCore_GetAllTTLAOrganisations", new DataBinder());
	}

	/** Adds the Donation Account data to the binder, if it exists, as 
	 *  rsDonationAccount.
	 * 
	 *  If a Donation Account is found, any mapped Investments for this account are
	 *  added to the binder, as rsInvestments.
	 * 
	 */
	public void addEnrolmentDataToBinder(CampaignEnrolment enrolment,
	 DataBinder binder) throws DataException, ServiceException {
		
		Log.debug("Adding custom Comm First enrolment data to Binder");
		
		DataResultSet rsCommFirstAccounts = Account.getClientAccountsDataByFundType
		 (enrolment.getOrganisationId(), DONATION_ACCOUNT_FUND_CODE, 
		 AccountType.COMM_FIRST, facade);

		binder.addResultSet("rsDonationAccount", rsCommFirstAccounts);
		
		Log.debug("Found " + rsCommFirstAccounts.getNumRows() + " Donation Accounts");
		
		if (rsCommFirstAccounts.getNumRows() > 1)
			throw new DataException("Multiple Donation accounts found. A Community " +
			 "First client should only have one Donation account");
		
		Account donationAccount = Account.get(rsCommFirstAccounts);
		
		if (donationAccount != null) {
			DataResultSet rsSubscriptions = 
			 Subscription.getAllDataByAccountId(donationAccount.getAccountId(), facade);
		
			binder.addResultSet("rsSubscriptions", rsSubscriptions);
			
			Log.debug("Found " + rsSubscriptions.getNumRows() + " Investments");
			
		}
	}

	public void validateIntention(FundInvestmentIntention intention)
			throws DataException, ServiceException {
		// TODO Auto-generated method stub
		
	}
}
