package com.ecs.stellent.ccla.clientservices.campaign;

import java.util.Vector;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;

import com.ecs.stellent.ccla.clientservices.AccountIntentionService;
import com.ecs.stellent.ccla.clientservices.form.PSDFFormUtils;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.Address;
import com.ecs.ucm.ccla.data.AuroraClient;
import com.ecs.ucm.ccla.data.Company;
import com.ecs.ucm.ccla.data.Contact;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Fund;
import com.ecs.ucm.ccla.data.SharedCols;
import com.ecs.ucm.ccla.data.campaign.Campaign;
import com.ecs.ucm.ccla.data.campaign.CampaignActivityType;
import com.ecs.ucm.ccla.data.campaign.CampaignEnrolment;
import com.ecs.ucm.ccla.data.campaign.CampaignEnrolmentAction;
import com.ecs.ucm.ccla.data.campaign.CampaignEnrolmentStatus;
import com.ecs.ucm.ccla.data.campaign.CampaignMessages;
import com.ecs.ucm.ccla.data.campaign.CampaignSubjectStatus;
import com.ecs.ucm.ccla.data.campaign.FundInvestmentIntention;
import com.ecs.ucm.ccla.data.form.Form;
import com.ecs.ucm.ccla.data.form.FormType;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

public class PSDFEnrolmentHandler extends BaseEnrolmentHandler {

	public static final int 	CAMPAIGN_ID = 10;
	public static final String 	PSDF_FUND_CODE = Fund.FundCodes.PC.toString();
	
	/** IDs used for custom enrolment actions. */
	public static class CustomActionIds {
		public static final int GENERATE_TEMP_ACCOUNT = 1;
	}

	public static final String COMPANY_CODE = "PSIC";
	
	public PSDFEnrolmentHandler(Campaign campaign, String username, FWFacade facade) {
		super(campaign, username, facade);
	}

	public void applyAction(CampaignEnrolment enrolment, CampaignEnrolmentAction action, String note) 
	throws DataException, ServiceException 
	{ 	
		Log.debug("PSDF Enrolment Handler: applying action: " + action);

		Integer activityId = null;
		String activityMessage = null;
		int enrolStatusId = 0;
		
		if (action.getActionId()==CampaignEnrolmentAction.EXCLUDE_ACTION) {
			enrolment.setEnrolmentStatusId(CampaignEnrolmentStatus.EXCLUDE_STATUS);
			enrolment.persist(facade, username);
			activityId = CampaignActivityType.EXCLUSION_ACTIVITY_ID;
			activityMessage = getMessage(CampaignMessages.EXCLUDE_MSG, new Object[] {campaign.getName()});
			this.addEnrolmentActivity(enrolment, enrolment.getPersonId(), activityId, activityMessage, username, note);
			return;
		} else if (action.getActionId()==CampaignEnrolmentAction.ENROL_ACTION) {
			enrolment.setEnrolmentStatusId(campaign.getDefaultEnrolmentStatusId());
			enrolment.persist(facade, username);
			activityId = CampaignActivityType.ENROLMENT_ACTIVITY_ID;
			activityMessage = getMessage(CampaignMessages.ENROL_MSG, new Object[] {campaign.getName()});
			this.addEnrolmentActivity(enrolment, enrolment.getPersonId(), activityId, activityMessage, username, note);
			return; 
		}
		
		
		if (action.getActionId()==CampaignEnrolmentAction.MARK_INTEREST_ACTION	||
				action.getActionId()==CampaignEnrolmentAction.MARK_NOT_INTEREST_ACTION ||
				action.getActionId()==CampaignEnrolmentAction.MARK_UNDECIDED_ACTION ||
				action.getActionId()==CampaignEnrolmentAction.AWAIT_MORE_INFO_ACTION ||
				action.getActionId()==CampaignEnrolmentAction.MARK_INELIGIBLE ||
				action.getActionId()==CampaignEnrolmentAction.MARK_COMPLETED_ACTION ||
				action.getActionId()==CampaignEnrolmentAction.MARK_AWAIT_ELIGIBILITY_ACTION ||
				action.getActionId()==CampaignEnrolmentAction.MARK_NO_RESPONSE_ACTION) 
		{
			
			switch (action.getActionId()) {
				case CampaignEnrolmentAction.MARK_INTEREST_ACTION:
					enrolStatusId = CampaignEnrolmentStatus.INTERESTED_STATUS;
					break;
				case CampaignEnrolmentAction.MARK_NOT_INTEREST_ACTION:
					enrolStatusId = CampaignEnrolmentStatus.NOT_INTERESTED_STATUS;
					break;
				case CampaignEnrolmentAction.MARK_UNDECIDED_ACTION:
					enrolStatusId = CampaignEnrolmentStatus.UNDECIDED_STATUS;					
					break;
				case CampaignEnrolmentAction.AWAIT_MORE_INFO_ACTION:
					enrolStatusId = CampaignEnrolmentStatus.AWAITING_RESPONSE_STATUS;					
					break;
				case CampaignEnrolmentAction.MARK_INELIGIBLE:
					enrolStatusId = CampaignEnrolmentStatus.INELIGIBLE_STATUS;					
					break;
				case CampaignEnrolmentAction.MARK_COMPLETED_ACTION:
					enrolStatusId = CampaignEnrolmentStatus.COMPLETED_STATUS;					
					break;
				case CampaignEnrolmentAction.MARK_AWAIT_ELIGIBILITY_ACTION:
					enrolStatusId = CampaignEnrolmentStatus.AWAITING_ELIGIBILITY_STATUS;					
					break;
				case CampaignEnrolmentAction.MARK_NO_RESPONSE_ACTION:
					enrolStatusId = CampaignEnrolmentStatus.NO_RESPONSE_STATUS;					
					break;
				default:
					//should never get to this state!
					Log.debug("PSDF Enrolment Handler: unknown action: " + action.getActionId()+", "+action.getName());					
					return;
			}
			
			enrolment.setEnrolmentStatusId(enrolStatusId);
			enrolment.persist(facade, username);

			CampaignEnrolmentStatus status = CampaignEnrolmentStatus.getCache().getCachedInstance(enrolStatusId);
			String currentStatusDesc = null;
			
			if (status!=null)
				currentStatusDesc = status.getName();
			else
				currentStatusDesc = "N/A";
			
			activityMessage = 
			 getMessage(CampaignMessages.STATUS_CHANGED_MSG, 
			 new Object[] {currentStatusDesc, campaign.getName()});
			
			this.addEnrolmentActivity(enrolment, enrolment.getPersonId(), CampaignActivityType.STATUS_UPDATE_ACTIVITY_ID, activityMessage, username, note);
			return;
			
		} else if (action.getActionId()==CampaignEnrolmentAction.GENERATE_FORM_ACTION ||
				action.getActionId()==CampaignEnrolmentAction.REGENERATE_FORM_ACTION ||
				action.getActionId()==CampaignEnrolmentAction.GENERATE_LA_FORM_ACTION ||
				action.getActionId()==CampaignEnrolmentAction.GENERATE_NON_LA_FORM_ACTION ||
				action.getActionId()==CampaignEnrolmentAction.REGENERATE_NON_LA_FORM_ACTION ||
				action.getActionId()==CampaignEnrolmentAction.REGENERATE_LA_FORM_ACTION) {
			
			Log.debug("Generating application forms for EnrolmentID: "
			 + enrolment.getCampaignEnrolmentId());
			
			// Run various checks to ensure we have minimal data to generate the form.
			Integer organisationId = enrolment.getOrganisationId();
			Entity entity = Entity.get(organisationId, facade);
			if (entity == null)
				throw new ServiceException("Missing Organisation from enrolment id: " 
				 + enrolment.getCampaignEnrolmentId());
			
			Integer contactId = enrolment.getContactId();
			
			if (contactId == null)
				throw new ServiceException("No assigned postal address");
			
			Contact contact = Contact.get(contactId, facade);
			
			boolean isAddressValid = Address.addressDataValid
			 (contact.getAddressId(), facade);
			
			if (!isAddressValid)
				throw new ServiceException("Assigned postal address is not valid, " +
				 "ensure it has a number/street/postcode set");
			
			Integer categoryId = entity.getCategoryId();
			if (categoryId == null)
				throw new ServiceException("Organisation Category not specified");
			
			// At this point all pending accounts should be created. We
			// are now in a position to generate the Reg. Form
			int formTypeId = FormType.PSDF_LA; //default
			if (action.getActionId()==CampaignEnrolmentAction.GENERATE_NON_LA_FORM_ACTION ||
					action.getActionId()==CampaignEnrolmentAction.REGENERATE_NON_LA_FORM_ACTION) {
				formTypeId = FormType.PSDF_NON_LA;
			}
			
			// Determine FormType to generate.
			FormType formType = FormType.getCache().getCachedInstance(formTypeId);
			
			Vector<Form> forms = PSDFFormUtils.createRegistrationForms
			 (enrolment, formType, null, false, true, this.facade, this.username);
			
			/*
			switch (action.getActionId()) {
				case CampaignEnrolmentAction.GENERATE_FORM_ACTION:
					enrolStatusId = CampaignEnrolmentStatus.FORM_GENERATED_STATUS;
					activityMessage = CampaignMessages.FORM_GENERATED_MSG;
					activityId = CampaignActivityType.FORM_CREATION_ACTIVITY_ID;
					break;
				case CampaignEnrolmentAction.REGENERATE_FORM_ACTION:
					enrolStatusId = CampaignEnrolmentStatus.FORM_GENERATED_STATUS;
					activityMessage = CampaignMessages.FORM_REGENERATED_MSG;
					activityId = CampaignActivityType.REGENERATE_FORM_ACTIVITY_ID;
					break;
				default:
					//should never get to this state!
					Log.debug("PSDF Enrolment Handler: unknown action: " 
					 + action.getActionId()+", "+action.getName());					
					return;
			}
			*/
			
			enrolment.setEnrolmentStatusId
			 (CampaignEnrolmentStatus.FORM_GENERATED_STATUS);
			
			enrolment.persist(facade, this.username);
			
			/*
			this.addEnrolmentActivity(
					enrolment, 
					enrolment.getPersonId(), 
					activityId, 
					getMessage(activityMessage, new Object[] {campaign.getName()}), 
					this.username, 
					note);
			return;
			*/
			
		} else if (action.getActionId()==CampaignEnrolmentAction.FORM_DISPATCHED_ACTION ||
				action.getActionId()==CampaignEnrolmentAction.FORM_RETURNED_ACTION ||
				action.getActionId()==CampaignEnrolmentAction.ISSUE_RETURNED_FORMED_ACTION) {
			
			switch (action.getActionId()) {
				case CampaignEnrolmentAction.FORM_DISPATCHED_ACTION:
					enrolStatusId = CampaignEnrolmentStatus.FORM_DISPATCHED_STATUS;
					activityMessage = CampaignMessages.FORM_DISPATCHED_MSG;
					activityId = CampaignActivityType.FORM_DISPATCHED_ACTIVITY_ID;
					break;
				case CampaignEnrolmentAction.FORM_RETURNED_ACTION:
					enrolStatusId = CampaignEnrolmentStatus.FORM_RETURNED_STATUS;
					activityMessage = CampaignMessages.FORM_RETURNED_MSG;
					activityId = CampaignActivityType.FORM_RETURNED_ACTIVITY_ID;
					break;
				case CampaignEnrolmentAction.ISSUE_RETURNED_FORMED_ACTION:
					enrolStatusId = CampaignEnrolmentStatus.ISSUE_WITH_RETURNED_FORM_STATUS;
					activityMessage = CampaignMessages.ISSUE_WITH_FORM_RETURN_MSG;
					activityId = CampaignActivityType.ISSUE_WITH_RETURN_FORM_ACTIVITY_ID;
					break;
				default:
					//should never get to this state!
					Log.debug("PSDF Enrolment Handler: unknown action: " + action.getActionId()+", "+action.getName());					
					return;
			}
			
			enrolment.setEnrolmentStatusId(enrolStatusId);
			enrolment.persist(facade, username);

			activityMessage = getMessage(activityMessage, new Object[] {campaign.getName()});
			
			this.addEnrolmentActivity(enrolment, enrolment.getPersonId(), activityId, activityMessage, username, note);
			return;			
		}
	}
	
	public void applyCustomAction(CampaignEnrolment enrolment, int actionId,
			String note, DataBinder extraParams) throws DataException, ServiceException 
	{
		if (actionId == CustomActionIds.GENERATE_TEMP_ACCOUNT) 
		{
			// Ensure the Organisation has an associated PSIC AuroraClient reference.
			Company pSICCompany = Company.getCache().getCachedInstance(Company.PSIC);
			
			AuroraClient pSICClient = Entity.getAuroraClientCompanyMapping
			 (enrolment.getOrganisationId(), pSICCompany, facade);
			
			if (pSICClient == null) {
				throw new ServiceException("Ensure the Organisation has an Aurora " +
				 "PSIC Client set before continuing");
			}			

			// get info needed to open an account
			Campaign campaign = Campaign.get(enrolment.getCampaignId(), facade);
			// get fund code
			Fund fund = Fund.get(PSDF_FUND_CODE, facade);
			Entity organisation = Entity.get(enrolment.getOrganisationId(), facade);
			int corrId = enrolment.getPersonId();
			String accountSubTitle = organisation.getOrganisationName();
			if (accountSubTitle.length() > 64)
				accountSubTitle = accountSubTitle.substring(0, 64);
			
			try {
				facade.beginTransaction();
				
				String incDistMethod = null;
				
				if (extraParams!=null) { 
					// Grab the default Income Dist. Method from binder.
					incDistMethod = extraParams.getLocal
					 (Account.Cols.INC_DIST_METHOD);
					
					if (StringUtils.stringIsBlank(incDistMethod))
						throw new ServiceException("Unable to generate Account " +
						 "Intention: Income Distribution Method not specified");
				}
				
				Account account = Account.add(organisation, null, true, fund, null, 
				 accountSubTitle, Account.AccountStatus.TEMP, 1, 
				 incDistMethod, false, null, null, null, null,
				 Account.DEFAULT_AGREEMENT_TYPE,
				 corrId, facade, username);
				
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
						username);		
				
				
				if (extraParams!=null) { 
					
					Float cash = CCLAUtils.getBinderFloatValue
					 (extraParams,SharedCols.CASH);
					Float units = CCLAUtils.getBinderFloatValue
					 (extraParams,SharedCols.UNITS);
					
					Integer intentionStatusId = CCLAUtils.getBinderIntegerValue
					 (extraParams,
					 FundInvestmentIntention.Cols.INVESTMENT_INTENTION_STATUS_ID);
					
					if (intentionStatusId!=null) {
						// Create the account intention and account intention activity 
						// as well					
						AccountIntentionService.addAccountIntention(fund.getFundCode(), 
							enrolment.getOrganisationId(), 
							enrolment.getPersonId(), cash , units, 
							enrolment.getCampaignId(), 
							intentionStatusId, facade, username, account.getAccountId(), 
							null, 
							enrolment.getCampaignEnrolmentId());
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

	public void validateIntention(FundInvestmentIntention intention, FWFacade facade)
	 throws DataException, ServiceException {
		return;
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
