package com.ecs.stellent.ccla.clientservices.campaign;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;

import com.ecs.stellent.ccla.clientservices.campaign.DioLoanEnrolmentHandler.CustomActionIds;
import com.ecs.stellent.ccla.clientservices.form.SegregatedClientRegistrationFormHandler;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Fund;
import com.ecs.ucm.ccla.data.campaign.Campaign;
import com.ecs.ucm.ccla.data.campaign.CampaignEnrolment;
import com.ecs.ucm.ccla.data.campaign.CampaignEnrolmentAction;
import com.ecs.ucm.ccla.data.campaign.FundInvestmentIntention;
import com.ecs.ucm.ccla.data.form.Form;
import com.ecs.ucm.ccla.data.form.FormType;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Handles segregated client enrolments AKA Big Local.
 * 
 * @author Tom
 *
 */
public class SegregatedClientEnrolmentHandler extends BaseEnrolmentHandler {
	
	public static final int CAMPAIGN_ID = 12;
	
	public static final String SEG_CLIENT_FUND_CODE = null;
	public static final String COMPANY_CODE = "CCLA";
	
	public SegregatedClientEnrolmentHandler(Campaign campaign, String username,
	 FWFacade facade) {
		super(campaign, username, facade);
	}

	public void applyAction(CampaignEnrolment enrolment,
	 CampaignEnrolmentAction action, String note) throws DataException,
	 ServiceException {

		if (action.getActionId() == CampaignEnrolmentAction.GENERATE_FORM_ACTION) {
			// Generate a Seg Client application/registration form.
			
			SegregatedClientRegistrationFormHandler formHandler = 
			 new SegregatedClientRegistrationFormHandler(this.username, this.facade);
			
			FormType formType = FormType.getCache().getCachedInstance
			 (FormType.SEG_CLIENT_APP);
			
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

	}

	public void validateIntention(FundInvestmentIntention intention,
			FWFacade facade) throws DataException, ServiceException {
		// TODO Auto-generated method stub
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
