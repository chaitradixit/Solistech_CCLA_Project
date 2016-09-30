package com.ecs.stellent.ccla.clientservices.campaign;

import java.text.NumberFormat;
import java.util.Vector;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.shared.SharedObjects;

import com.ecs.stellent.ccla.clientservices.AccountIntentionService;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Fund;
import com.ecs.ucm.ccla.data.Relation;
import com.ecs.ucm.ccla.data.RelationName;
import com.ecs.ucm.ccla.data.SharedCols;
import com.ecs.ucm.ccla.data.campaign.Campaign;
import com.ecs.ucm.ccla.data.campaign.CampaignActivityType;
import com.ecs.ucm.ccla.data.campaign.CampaignEnrolment;
import com.ecs.ucm.ccla.data.campaign.CampaignEnrolmentAction;
import com.ecs.ucm.ccla.data.campaign.CampaignEnrolmentStatus;
import com.ecs.ucm.ccla.data.campaign.CampaignMessages;
import com.ecs.ucm.ccla.data.campaign.CampaignSubjectStatus;
import com.ecs.ucm.ccla.data.campaign.FundInvestmentIntention;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

public class DioLoanEnrolmentHandler extends BaseEnrolmentHandler {
	
	public static final int CAMPAIGN_ID = 11;
	public static final String DIOLOAN_FUND_CODE = Fund.FundCodes.D.toString();
	public static final String COMPANY_CODE = "CBF";
	
	public static final float MAXIMUM_CLIENT_LOAN_AMOUNT = 
	 (float)SharedObjects.getEnvironmentInt("CCLA_CS_DioLoan_MaxLoanPerDiocese", 0);
	
	/** IDs used for custom enrolment actions. */
	public static class CustomActionIds {
		public static final int GENERATE_LOAN_ACCOUNT = 1;
	}
	
	public DioLoanEnrolmentHandler(Campaign campaign, String username,
			FWFacade facade) {
		super(campaign, username, facade);
	}

	public void applyAction(CampaignEnrolment enrolment,
			CampaignEnrolmentAction action, String note) throws DataException,
			ServiceException {
		// TODO Auto-generated method stub

	}

	public void applyCustomAction(CampaignEnrolment enrolment, int actionId,
			String note, DataBinder extraParams) throws DataException,
			ServiceException {
		
		if (actionId == CustomActionIds.GENERATE_LOAN_ACCOUNT) {
			// Generate a new Loan account at TEMP status.
			
			Entity org = Entity.get(enrolment.getOrganisationId(), facade);
			Fund fund  = Fund.getCache().getCachedInstance(DIOLOAN_FUND_CODE);
			
			try {
				facade.beginTransaction();
				
				String incDistMethod = null;
				Integer loanType = null, loanTerm = null;
				
				if (extraParams!=null) { 
					// Grab the default Income Dist. Method from binder.
					incDistMethod = extraParams.getLocal
					 (Account.Cols.INC_DIST_METHOD);
					
					if (StringUtils.stringIsBlank(incDistMethod))
						throw new ServiceException("Unable to generate Loan Account: " +
						 "Income Distribution Method not specified");
					
					loanType = CCLAUtils.getBinderIntegerValue
					 (extraParams, Account.Cols.LOAN_TYPE);
					
					loanTerm = CCLAUtils.getBinderIntegerValue
					 (extraParams, Account.Cols.LOAN_TERM);
					
					if (loanType == null
						||
						loanTerm == null) {
						throw new ServiceException("Unable to generate Loan Account: " +
						 "Loan Type or Loan Term not specified");
					}
				}
				
				String accSubtitle = org.getOrganisationName();
				
				Account account = Account.add(org, null, false, fund, null, accSubtitle, 
				 Account.AccountStatus.TEMP, 2, incDistMethod, false, null, 
				 Account.AccountType.LOAN, loanType, loanTerm, 
				 Account.DEFAULT_AGREEMENT_TYPE,
				 enrolment.getPersonId(), facade, username);
				
				// Add Auth Persons and Signatories from Client level.
				
				// Fetch auth persons.
				Vector<Relation> authPersons = Relation.getRelations
				 (org.getOrganisationId(), 
				 null, 
				 null, 
				 RelationName.getCache().getCachedInstance(
				  RelationName.OrganisationPersonRelation.AUTH_PERSON), 
				 facade);
				
				// Add auth person relations to account.
				RelationName accountAuthPersonRel =  
				 RelationName.getCache().getCachedInstance(
				 RelationName.PersonAccountRelation.AUTH_PERSON); 
				
				for (Relation r : authPersons) {
					Relation.add(
					 r.getElementId2(), 
					 account.getAccountId(), 
					 accountAuthPersonRel, facade, Globals.Users.System);
				}
				
				// Fetch sigs.
				Vector<Relation> sigs = Relation.getRelations
				 (org.getOrganisationId(), 
				 null, 
				 null, 
				 RelationName.getCache().getCachedInstance(
				  RelationName.OrganisationPersonRelation.SIGNATORY), 
				 facade);
				
				// Add sig relations to account.
				RelationName accountSigRel =  
				 RelationName.getCache().getCachedInstance(
				 RelationName.PersonAccountRelation.SIGNATORY); 
				
				for (Relation r : sigs) {
					Relation.add(
					 r.getElementId2(), 
					 account.getAccountId(), 
					 accountSigRel, facade, Globals.Users.System);
				}
				
				// Add activity indicating the new account was generated
				enrolment.addActivity(
					enrolment.getPersonId(), 
					CampaignActivityType.ACCOUNT_CREATION_ACTIVITY_ID, 
					getMessage(
						CampaignMessages.ACCOUNT_TEMPLATE_CREATE_MSG, 
						new Object[] {
							account.getExtendedAccountNumberString(), 
							campaign.getName()
						} 
					), 
					note, 
					facade, 
					username
				);		
				
				
				if (extraParams!=null) { 
					// Add the loan intention
					
					Float cash = CCLAUtils.getBinderFloatValue
					 (extraParams,SharedCols.CASH);
					Float units = CCLAUtils.getBinderFloatValue
					 (extraParams,SharedCols.UNITS);
					
					Integer intentionStatusId = CCLAUtils.getBinderIntegerValue
					 (extraParams,
					 FundInvestmentIntention.Cols.INVESTMENT_INTENTION_STATUS_ID);
					
					if (intentionStatusId!=null) {
						//Create the account intention and account intention activity as well					
						AccountIntentionService.addAccountIntention(fund.getFundCode(), 
							enrolment.getOrganisationId(), 
							enrolment.getPersonId(), cash , units, enrolment.getCampaignId(), 
							intentionStatusId, facade, username, account.getAccountId(), null, 
							enrolment.getCampaignEnrolmentId()
						);
					}
				}
				
				facade.commitTransaction();
				
			} catch (Exception e) {
				facade.rollbackTransaction();
				Log.error("failed to create account:" + e.getMessage(),e);
				throw new ServiceException("failed to create account:"+
						e.getMessage(),e);
			}
		}
	}
	
	/** Ensure that an individual client's total requested loan amount does not exceed
	 *  £1m.
	 *  
	 */
	public void validateIntention(FundInvestmentIntention intention, FWFacade facade)
	 throws DataException, ServiceException {

		if (intention.getAccountId() != null && intention.getOrganisationId() != null) {

			// Fetch all existing loan 
			Vector<FundInvestmentIntention> intentions = 
			 FundInvestmentIntention.getByOrgAndCampaignId
			 (intention.getOrganisationId(), this.campaign.getCampaignId(), facade);
			
			float totalLoanAmount = 
			 intention.getCash() != null ? intention.getCash() : 0;
			
			for (FundInvestmentIntention loanIntention : intentions) {
				if (loanIntention.getInvestmentIntentionId() 
					== intention.getInvestmentIntentionId()) {
					// Don't count the intention we are currently comparing.
				} else if (loanIntention.getCash() != null) {
					// Sum up other intentions
					totalLoanAmount += loanIntention.getCash();
				}
			}
			
			if (totalLoanAmount > MAXIMUM_CLIENT_LOAN_AMOUNT) {
				NumberFormat fmt = NumberFormat.getInstance();
				
				fmt.setMaximumFractionDigits(0);
				fmt.setGroupingUsed(true);
				
				throw new ServiceException("Unable to set intended loan amount - " +
				 "this brings the total requested loan amount above the client's " +
				 "limit of " + fmt.format
				 (MAXIMUM_CLIENT_LOAN_AMOUNT) + " (" + 
				 fmt.format(totalLoanAmount) + ")");
			}
		}
	}

	public void addEnrolmentDataToBinder(CampaignEnrolment enrolment,
			DataBinder binder) throws DataException, ServiceException {
		// TODO Auto-generated method stub
		
	}

	public void validateIntention(FundInvestmentIntention intention)
			throws DataException, ServiceException {
		// TODO Auto-generated method stub
		
	}

}
