package com.ecs.stellent.ccla.clientservices.form;

import com.ecs.stellent.ccla.clientservices.campaign.DioLoanEnrolmentHandler;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals;
import com.ecs.ucm.ccla.Globals.UCMFieldNames;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.Address;
import com.ecs.ucm.ccla.data.AuroraClient;
import com.ecs.ucm.ccla.data.Contact;
import com.ecs.ucm.ccla.data.Element;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Fund;
import com.ecs.ucm.ccla.data.campaign.CampaignActivity;
import com.ecs.ucm.ccla.data.campaign.CampaignActivityType;
import com.ecs.ucm.ccla.data.campaign.CampaignEnrolment;
import com.ecs.ucm.ccla.data.campaign.CampaignEnrolmentAction;
import com.ecs.ucm.ccla.data.campaign.CampaignEnrolmentStatus;
import com.ecs.ucm.ccla.data.campaign.CampaignMessages;
import com.ecs.ucm.ccla.data.form.Form;
import com.ecs.ucm.ccla.data.form.FormElementApplied;
import com.ecs.ucm.ccla.data.form.FormHandler;
import com.ecs.ucm.ccla.data.form.FormType;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;

import intradoc.common.ServiceException;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.util.Hashtable;
import java.util.Vector;

import com.ecs.utils.stellent.embedded.FWFacade;

/** Used to handle pre-filled Diocesan Loan registration forms.
 *  
 *  
 **/
public class DioLoanRegistrationFormHandler extends FormHandler {
	
	
	public DioLoanRegistrationFormHandler(String userId, FWFacade facade) {
		Log.debug("Initialized Diocesan Loan Reg. Form Handler");
		
		this.userId = userId;
		this.facade = facade;
	}
	
	public DioLoanRegistrationFormHandler(Form form, String userId, FWFacade facade) {
		Log.debug("Initialized Diocesan Loan Reg. Form Handler");
		
		this.form = form;
		this.userId = userId;
		this.facade = facade;
	}
	
	/** Actions to perform when the form is initially checked in.
	 *  
	 *  Updates the mapped account(s) from a TEMP status to an OPEN status.
	 *  
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void doPostCheckinActions(int docId) 
	 throws Exception {
		
		super.doPostCheckinActions(docId);
		
		form.persist(facade, userId);
		
		// Fetch Organisation/Aurora Client Number
		CampaignEnrolment enrolment = null;
		Entity org = null;
		
		if (form.getCampaignEnrolmentId() != null) {
			enrolment = CampaignEnrolment.get(form.getCampaignEnrolmentId(), facade);
			
			if (enrolment.getOrganisationId() != null) {
				// Lookup client data, if applicable
				org = Entity.get(enrolment.getOrganisationId(), facade);
			}
		}
		
		// Determine list of Accounts which are tagged to the form
		
		// Fetch mapped accounts.
		Vector<Account> accounts = (Vector<Account>)
		 FormElementApplied.getFormElementsAppliedByType
		 (form.getFormId(), ElementType.ACCOUNT, facade);
		
		Log.debug("Found " + accounts.size() + " mapped accounts");
		
		if (accounts.isEmpty()) {
			Log.warn("No accounts associated with mandate form: " 
			 + form.getFormId());
		} else {
			// Update all account statuses to OPEN, if they are currently at TEMP 
			// status
			for (Account account : accounts) {
				if (account.getStatus() == Account.AccountStatus.TEMP) {
					Log.debug("Updating status of account ID " + account.getAccountId()
					 + " to OPEN");
					account.setStatus(Account.AccountStatus.OPEN);
					account.persist(facade, Globals.Users.System);
				}
			}
		}
		
		// If no PC accounts for the client are at 'TEMP' status, we will assume all
		// forms have been returned.
		if (form.getCampaignEnrolmentId() != null) {
			Log.debug("Checking if all client PC accounts are no longer at TEMP " +
			 "status");
			
			DataResultSet rsTempPCAccounts = Account.getClientAccountsDataByStatusFund
			 (org.getOrganisationId(), Account.AccountStatus.TEMP, 
			 Fund.FundCodes.PC.toString(), facade);
			
			if (rsTempPCAccounts.isEmpty()) {
				Log.debug("No PC accounts at TEMP status. " +
				 "Updating enrolment status to 'Forms returned'");
				
				enrolment.setEnrolmentStatusId
				 (CampaignEnrolmentStatus.FORM_RETURNED_STATUS);
				
				enrolment.persist(facade, Globals.Users.System);
				
			} else {
				Log.debug(rsTempPCAccounts.getNumRows() + 
				 " PC accounts still at TEMP status. Leaving enrolment status as-is");
			}
		}
	}
	
	/** Returns the doc meta field mapping which should be applied
	 *  to content items bearing the form ID.
	 *  
	 * @return
	 * @throws DataException 
	 */
	@SuppressWarnings("unchecked")
	public Hashtable<String, String> getDocMetaMapping() throws DataException {
		
		Hashtable<String, String> map = super.getDocMetaMapping();
		
		// Fetch mapped accounts.
		Vector<Account> accounts = (Vector<Account>)
		 FormElementApplied.getFormElementsAppliedByType
		 (form.getFormId(), ElementType.ACCOUNT, facade);
		
		Log.debug("Found " + accounts.size() + " mapped accounts");
		
		if (accounts.size() == 0) {
			// Assume this is one of the 'blank' application forms. Add Company/Fund 
			// Code only.
			map.put("xCompany", DioLoanEnrolmentHandler.COMPANY_CODE);
			map.put("xFund", DioLoanEnrolmentHandler.DIOLOAN_FUND_CODE);
			
		} else if (accounts.size() == 1) {
			// If this mandate form only listed one account, account meta-data fields 
			//can be pre-filled.
			Account account = accounts.get(0);

			String accNumberStr = account.getAccountNumberString();
			
			Fund fund = Fund.getCache().getCachedInstance(account.getFundCode());
			
			map.put("xAccountNumber", accNumberStr);
			map.put("xFund", account.getFundCode());
			map.put("xCompany", fund.getCompany().getCode());
			
			AuroraClient auroraClient = Entity.getAuroraClientCompanyMapping
			 (form.getOrganisationId(), fund.getCompany(), facade);
					
			// Ensure Client Number is padded correctly for the Dio Loan company
			map.put("xClientNumber", CCLAUtils.padClientNumber(
			 Integer.toString(auroraClient.getClientNumber()), 
			 fund.getCompany()));
			
		} else {
			// This form lists multiple accounts. Mark the base item
			// as a MULTIDOC. Child items will be added via the
			// doPostCheckinActions method.
			map.put("xDocumentClass", "MULTIDOC");
			map.put("xMultiDoc", "Yes");
		}
		
		// Set AML metadata flag for all mandates
		map.put("xAMLDocument", "Yes");
		
		return map;
	}

	@Override
	public Form generateForm(FormType formType, Element element, Integer investmentId)
			throws ServiceException, DataException {
		
		Account account = Account.get(element.getElementId(), facade);
		
		if (account == null)
			throw new DataException("Unable to generate " + formType.getName() + 
			 ": no account found with ID " + element.getElementId());
		
		// Fail if fund code != Dio Loan fund code
		if (!account.getFundCode().equals(DioLoanEnrolmentHandler.DIOLOAN_FUND_CODE))
			throw new DataException("Unable to generate " + formType.getName() + 
			 ", fund code was " + account.getFundCode() + " (expected: " + 
			 DioLoanEnrolmentHandler.DIOLOAN_FUND_CODE + ")");
		
		// Fail if account type != 'Loan'
		if (account.getAccountType() != Account.AccountType.LOAN)
			throw new DataException("Unable to generate " + formType.getName() + 
			 ", not a loan account");
		
		Log.debug("Generating " + formType.getName() + " for account ID: " 
		 + element.getElementId());
		
		// Run various checks to ensure we have minimal data to generate the form.
		
		// Ensure account has a subtitle set.
		if (StringUtils.stringIsBlank(account.getSubtitle()))
			throw new DataException("Unable to generate " + formType.getName() + ", " +
			 "no account subtitle specified");
		
		Integer orgId 	= account.getOwnerOrganisationId(facade);
		Entity org 		= Entity.get(orgId, facade);
		
		CampaignEnrolment enrolment = CampaignEnrolment.get
		 (DioLoanEnrolmentHandler.CAMPAIGN_ID, orgId, facade);
		
		if (enrolment == null)
			throw new DataException("Unable to generate " + formType.getName() + ", " +
			 org.getOrganisationName() + " is not enrolled to Diocesan Loan campaign");
		
		if (!enrolment.isContactable())
			throw new DataException("Unable to generate " + formType.getName() + ", " +
			 "campaign enrolment does not have a correspondent/address set");
		
		Contact contact = Contact.get(enrolment.getContactId(), facade);
	
		boolean isAddressValid = Address.addressDataValid
		 (contact.getAddressId(), facade);
		
		if (!isAddressValid)
			throw new ServiceException("Assigned postal address is not valid, " +
			 "ensure it has a number/street/postcode set");

		Form form = DioLoanFormUtils.createRegistrationForm
		 (account, formType, enrolment, false, true, facade, userId);
		
		/*
		if (enrolment != null) {
			int enrolStatusId = CampaignEnrolmentStatus.FORM_GENERATED_STATUS;
			
			String activityMessage = formType.getName() + 
			 " form ID " + form.getFormId() + " created and dispatched for print";
			int activityTypeId = CampaignActivityType.FORM_CREATION_ACTIVITY_ID;
			
			enrolment.addActivity(null, activityTypeId, activityMessage, 
			 null, facade, userId);
			
			// Update enrolment status
			enrolment.setEnrolmentStatusId(enrolStatusId);
			enrolment.persist(facade, this.userId);
		}
		*/
		
		return form;
	}
}
