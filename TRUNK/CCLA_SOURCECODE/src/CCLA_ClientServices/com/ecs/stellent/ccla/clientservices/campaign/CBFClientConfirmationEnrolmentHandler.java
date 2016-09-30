package com.ecs.stellent.ccla.clientservices.campaign;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.ecs.stellent.ccla.clientservices.form.CBFClientInfoFormHandler;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.AuroraClient;
import com.ecs.ucm.ccla.data.Company;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.campaign.ApplicableEnrolmentAttribute;
import com.ecs.ucm.ccla.data.campaign.Campaign;
import com.ecs.ucm.ccla.data.campaign.CampaignActivityType;
import com.ecs.ucm.ccla.data.campaign.CampaignEnrolment;
import com.ecs.ucm.ccla.data.campaign.CampaignEnrolmentAction;
import com.ecs.ucm.ccla.data.campaign.CampaignMessages;
import com.ecs.ucm.ccla.data.campaign.EnrolmentAttributeApplied;
import com.ecs.ucm.ccla.data.campaign.FundInvestmentIntention;
import com.ecs.ucm.ccla.data.form.FormType;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

public class CBFClientConfirmationEnrolmentHandler extends BaseEnrolmentHandler {

	public static final int CAMPAIGN_ID = 14;	
	public static final String COMPANY_CODE = "CBF";
	
	//Custom
	static class CustomActionIds {
		public static final int ADD_SELECTED_ACCOUNTS 	= 1; 
		public static final int RESET_ACCOUNTS 			= 2;
	}

	public static String SPOOL_FILE_CAMPAIGN_NAME = "Generic";
	
	public CBFClientConfirmationEnrolmentHandler(Campaign campaign,
			String username, FWFacade facade) {
		super(campaign, username, facade);
		// TODO Auto-generated constructor stub
	}

	public void applyAction(CampaignEnrolment enrolment,
			CampaignEnrolmentAction action, String note) throws DataException,
			ServiceException {

		if (action.getActionId() 
			== CampaignEnrolmentAction.GENERATE_INFORMATION_FORM) {
			// Generate a CBF Client Information Form.
			Entity org = Entity.get(enrolment.getOrganisationId(), true, facade);
			
			FormType formType = 
			 FormType.getCache().getCachedInstance(FormType.CBF_CLIENT_INFO);
			
			CBFClientInfoFormHandler.generateForms
			 (formType, org, true, this.facade, this.username);
			
		} else if (action.getActionId() 
			== CampaignEnrolmentAction.GENERATE_CONFIRMATION_FORM) {
			// Generate a CBF Client Confirmation Form.
			Entity org = Entity.get(enrolment.getOrganisationId(), true, facade);
			
			FormType formType = 
			 FormType.getCache().getCachedInstance(FormType.CBF_CLIENT_CONFIRMATION);
			
			CBFClientInfoFormHandler.generateForms
			 (formType, org, true, this.facade, this.username);
			
		} else {
			throw new ServiceException("Unknown enrolment action ID: "
			 + action.getActionId());
		}		
	}

	public void applyCustomAction(CampaignEnrolment enrolment, int actionId,
			String note, DataBinder extraParams) throws DataException,
			ServiceException {
		
		
		Integer activityId = null;
		String activityMessage = null;
		
		if (actionId == CustomActionIds.ADD_SELECTED_ACCOUNTS) {
			//add account id as attributes to the enrolment, 
			//this will delete existing account id attrs and be replaced with whatever is on the form.	
			
			//Delete all existing ones
			EnrolmentAttributeApplied.removeAllAttrByTypeAndEnrolmentId(
					enrolment.getCampaignEnrolmentId(),
					ApplicableEnrolmentAttribute.Ids.CBF_CLIENT_CONF_ACCOUNT, 
					facade);
			
			String commaSepValues = CCLAUtils.getBinderStringValue(extraParams, "CUSTOM_VALUE");
			if (!StringUtils.stringIsBlank(commaSepValues)) {
				EnrolmentAttributeApplied.addAllAttrByTypeAndEnrolmentId(
						enrolment.getCampaignEnrolmentId(), 
						ApplicableEnrolmentAttribute.Ids.CBF_CLIENT_CONF_ACCOUNT, 
						commaSepValues, username, facade);
			}
			
			activityId = CampaignActivityType.ACCOUNT_SELECTED_ID;
			activityMessage = CampaignMessages.ACCOUNT_SELECTED_MSG;
			
		} else if (actionId == CustomActionIds.RESET_ACCOUNTS) {
			//reset the account id attributes as to the default value
			//i.e. based on stephen gibbons query, any account without mandate docs.
			
			EnrolmentAttributeApplied.removeAllAttrByTypeAndEnrolmentId(
					enrolment.getCampaignEnrolmentId(),
					ApplicableEnrolmentAttribute.Ids.CBF_CLIENT_CONF_ACCOUNT, 
					facade);

			activityId = CampaignActivityType.ACCOUNT_RESETED_ID;
			activityMessage = CampaignMessages.ACCOUNT_RESETED_MSG;

		} else {
			throw new ServiceException("Unknown custom enrolment action ID: "
					 + actionId);
		}
		
		//Add an activity.
		if (activityId!=null && !StringUtils.stringIsBlank(activityMessage))
			this.addEnrolmentActivity(enrolment, enrolment.getPersonId(), activityId, activityMessage, username, note);

	}

	public void validateIntention(FundInvestmentIntention intention)
			throws DataException, ServiceException {
		// TODO Auto-generated method stub
	}


	public void addEnrolmentDataToBinder(CampaignEnrolment enrolment,
			DataBinder binder) throws DataException, ServiceException {
		
		FWFacade cdbFacade = CCLAUtils.getFacade(true);
		FWFacade ucmFacade = CCLAUtils.getFacade(false);
		
		Vector<EnrolmentAttributeApplied> appliedVec = 
			EnrolmentAttributeApplied.getAllAttrByTypeAndEnrolmentId(
				enrolment.getCampaignEnrolmentId(), 
				ApplicableEnrolmentAttribute.Ids.CBF_CLIENT_CONF_ACCOUNT, 
				cdbFacade);
		
		if (appliedVec!=null && !appliedVec.isEmpty()) {
			// A subset of accounts has already been selected/saved against this
			// enrolment. Add to binder.
			CCLAUtils.addQueryBooleanParamToBinder(binder, "hasSelectedAccount", true);
			
			for (EnrolmentAttributeApplied enrol: appliedVec) {
				// Add each selected Account ID to the binder as a local var
				CCLAUtils.addQueryParamToBinder
				 (binder, "Sel_"+enrol.getValue(), enrol.getValue());
			}
		}
		
		int entityId = enrolment.getOrganisationId();
		
		// Hard-code to CBF
		Company company = Company.getCache().getCachedInstance(Company.CBF);
		
		// Fetch non-zero account balances from the special temp table.
		/*
		DataResultSet rsAccountBalances = facade.createResultSet
		 ("qClientServices_GetTempAuroraNonZeroBalances", binder);
		
		binder.addResultSet("rsNonZeroAccountBalances", rsAccountBalances);
		*/
		
		// Fetch ALL accounts to display on the page.
		DataResultSet rsAccounts = Account.getClientAccountsData
		 (entityId, cdbFacade);
		
		if (rsAccounts==null || rsAccounts.isEmpty()) {
			return;
		}
		
		AuroraClient auroraClient = Entity.getAuroraClientCompanyMapping
		 (entityId, company, cdbFacade);
		
		if (auroraClient.getClientNumber()==null)
			throw new ServiceException("Client Number is null for Organisation "+entityId);
		
		DataBinder sbinder = new DataBinder();
		CCLAUtils.addQueryParamToBinder(sbinder, "clientNumber", String.valueOf(auroraClient.getClientNumber()));
		
		List<String> mandateList = new ArrayList<String>();
		
		DataResultSet rsMandateDocForAccounts = 
			ucmFacade.createResultSet("qClientServices_GetClientAccountMandatedDocs", sbinder);
		
		//If cannot find any mandates with client number, then just return.  
		if (rsMandateDocForAccounts==null || rsMandateDocForAccounts.isEmpty()) {
			Log.debug("Cannot find any account mandates for clientNumber "+auroraClient.getClientNumber());
		} else { 		
			//Populate the hashmap with valid mandate documents
			do {			
				Integer cn = CCLAUtils.getResultSetIntegerValue(rsMandateDocForAccounts, "CLIENT_NUMBER");
				String fund = CCLAUtils.getResultSetStringValue(rsMandateDocForAccounts, "DOC_FUNDCODE");
				Integer num = CCLAUtils.getResultSetIntegerValue(rsMandateDocForAccounts, "DOC_ACCOUNTNUMBER");
					
				if (cn!=null && num!=null && !StringUtils.stringIsBlank(fund)) {
					String value = cn+"|"+num+"|"+fund;
					Log.debug("Adding Doc with the following value:"+value);
					mandateList.add(value);
				} else {
					Log.debug("Ignoring Doc with ClientNumber: "+(cn==null?"null":String.valueOf(cn))+
							", AccountNum: "+(num==null?"null":String.valueOf(num))+
							", Fund: "+(StringUtils.stringIsBlank(fund)?"null":fund));
				}
			} while (rsMandateDocForAccounts.next());		
		}
		
		//now check the accounts against the possible mandates and generate an not matched list.
		if (rsAccounts.first()) {
			do {
				int accountId = CCLAUtils.getResultSetIntegerValue(rsAccounts, Account.Cols.ID);
				String fund = CCLAUtils.getResultSetStringValue(rsAccounts, Account.Cols.FUND_CODE);
				int num = CCLAUtils.getResultSetIntegerValue(rsAccounts, Account.Cols.ACCOUNTNUMBER);
				
				String lookup = auroraClient.getClientNumber()+"|"+num+"|"+fund;
				if (!mandateList.contains(lookup)) {
					Log.debug("Adding No Mandate Document for Account "+accountId+" to binder");
					CCLAUtils.addQueryIntParamToBinder(binder, "NoMand_"+accountId, accountId);
				} else {
					Log.debug("Found Mandate Document for Account "+accountId+", not adding to binder");
				}
				
			} while (rsAccounts.next());
		}		
	}

}
