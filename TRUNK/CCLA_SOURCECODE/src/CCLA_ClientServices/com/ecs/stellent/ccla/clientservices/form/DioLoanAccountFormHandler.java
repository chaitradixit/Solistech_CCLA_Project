package com.ecs.stellent.ccla.clientservices.form;

import intradoc.common.ServiceException;
import intradoc.data.DataException;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import com.ecs.stellent.ccla.clientservices.campaign.DioLoanEnrolmentHandler;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals.UCMFieldNames;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.AuroraClient;
import com.ecs.ucm.ccla.data.Company;
import com.ecs.ucm.ccla.data.Contact;
import com.ecs.ucm.ccla.data.Element;
import com.ecs.ucm.ccla.data.ElementAttribute;
import com.ecs.ucm.ccla.data.ElementAttributeApplied;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Fund;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.RelationName;
import com.ecs.ucm.ccla.data.campaign.CampaignEnrolment;
import com.ecs.ucm.ccla.data.campaign.FundInvestmentIntention;
import com.ecs.ucm.ccla.data.form.Form;
import com.ecs.ucm.ccla.data.form.FormElementApplied;
import com.ecs.ucm.ccla.data.form.FormType;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Handles generation of PSDF forms.
 *  
 *  Document handler functions for returned forms are taken care of by the superclass
 *  AccountFormHandler.
 * 
 * @author Tom
 *
 */
public class DioLoanAccountFormHandler extends AccountFormHandler {

	public DioLoanAccountFormHandler(Form form, String userId, FWFacade facade) {
		super(form, userId, facade);
	}
	
	public DioLoanAccountFormHandler(String userId, FWFacade facade) {
		super(userId, facade);
	}

	/** Generates forms for existing Dio Loan accounts.
	 *  
	 *  The passed Element is expected to be a D account (type of 'Loan'), belonging to 
	 *  a client who is enrolled to the Dio Loan campaign.
	 *  
	 *  The 'Dio Loan Resolution Received' attribute must also be set for the client.
	 * 
	 * @throws DataException 
	 * @throws ServiceException 
	 * @throws DataException 
	 * @throws ServiceException 
	 * 
	 */
	public Form generateForm(FormType formType, Element element)
	 throws ServiceException, DataException {
		
		Account account = Account.get(element.getElementId(), facade);
		
		if (account == null)
			throw new DataException("Unable to generate account form: no account " + 
			 "found with ID " + element.getElementId());
		
		// Fail if fund code != 'D'
		if (!account.getFundCode().equals(DioLoanEnrolmentHandler.DIOLOAN_FUND_CODE))
			throw new DataException("Unable to generate " + formType.getName() + 
			 " form, fund code was " + account.getFundCode() + " (expected: " + 
			 DioLoanEnrolmentHandler.DIOLOAN_FUND_CODE + ")");
		
		// Fail if account type != 'Loan'
		if (account.getAccountType() != Account.AccountType.LOAN)
			throw new DataException("Unable to generate " + formType.getName() + 
			 " form, not a loan account");
		
		// Fetch data elements required to generate form
		Integer orgId = account.getOwnerOrganisationId(facade);
		Entity org = Entity.get(orgId, facade);
		
		// Check for 'Dio Loan Resolution received' attribute
		ElementAttributeApplied resolutionRecvd = ElementAttributeApplied.get
		 (org.getOrganisationId(), 
		 ElementAttribute.OrganisationAttributes.DIO_LOAN_RESOLUTION_RECEIVED, facade);
		
		if (resolutionRecvd == null || !resolutionRecvd.getStatus()) {
			throw new DataException("Unable to generate " + formType.getName() + 
			 " form, the client's Dio Loan Resolution document must be attached to " +
			 "their organisation record first");
		}
		
		AuroraClient auroraClient = Entity.getAuroraClientCompanyMapping
		 (orgId, Company.getCache().getCachedInstance(Company.CBF), facade);
		
		if (auroraClient == null) {
			throw new DataException("Unable to generate " + formType.getName() + 
			 " form, no CBF Aurora Client set for Organisation");
		}
		
		// Fetch RelationName -> Person mapping for account, with Contact details
		// pre-loaded.
		HashMap<RelationName, Vector<Person>> relatedAccountPersons = 
		 account.getRelatedPersons(true, facade);
		
		// Search for existing PSDF Campaign enrolment. Fail if the organisation isn't
		// enrolled.
		CampaignEnrolment enrolment = CampaignEnrolment.get
		 (DioLoanEnrolmentHandler.CAMPAIGN_ID, orgId, facade);
		
		if (enrolment == null) {
			throw new DataException("Unable to generate account form: organisation " +
			 "not enrolled to Dio Loan campaign");
		}
		
		if (!enrolment.isContactable())
			throw new DataException("Unable to generate account form: no assigned " +
			 "person and/or contact address");
		
		Contact postalAddressContact = Contact.get(enrolment.getContactId(), facade);
		Person correspondent = Person.get(enrolment.getPersonId(), true, facade);
		
		// Fetch investment intention, i.e. the requested loan amount
		FundInvestmentIntention intention = FundInvestmentIntention.getByAccountId
		 (account.getAccountId(), facade);
		
		if (!(intention != null 
			&& intention.getCash() != null && intention.getCash() > 0f))
			throw new DataException("Unable to generate account form: no loan amount " +
			 "set. Ensure the account has a non-zero loan amount specified.");
		
		// Check the loan amount doesn't specify pence - must be a whole number in
		// pounds.
		if (intention.getCash() - intention.getCash().longValue() != 0)
			throw new DataException("Unable to generate account form: the loan " +
			 "amount must be a whole number expressed in pounds sterling.");
		
		this.form = DioLoanFormUtils.createAccountForm
		 (enrolment, org, auroraClient, 
		 postalAddressContact, 
		 correspondent, 
		 formType, account, intention,
		 relatedAccountPersons, true, userId, facade);
		
		return this.form;
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
			// Nothing we can do here - no mapped account!
			Log.error("Unable to apply doc metadata mapping to Form ID: " 
			 + form.getFormId() + ", type: " + form.getFormType().getName() + ", "
			 + "no mapped account");
			
		} else if (accounts.size() == 1) {
			Account account = accounts.get(0);

			String accNumberStr = account.getAccountNumberString();
			
			AuroraClient auroraClient = 
			 Entity.getAuroraClientCompanyMapping
			 (form.getOrganisationId(), 
			 Company.getCache().getCachedInstance(Company.CBF),
			 facade);
					
			Fund fund = Fund.getCache().getCachedInstance(account.getFundCode());
			
			map.put(UCMFieldNames.AccountNumber, accNumberStr);
			map.put(UCMFieldNames.Fund, account.getFundCode());
			map.put(UCMFieldNames.Company, fund.getCompany().getCode());
			
			// Ensure Client Number is padded correctly for the Dio Loan company
			map.put(UCMFieldNames.ClientNumber, CCLAUtils.padClientNumber(
			 Integer.toString(auroraClient.getClientNumber()), 
			 fund.getCompany()));
			
			// If this is a draw-down form, the cash amount field can also be
			// populated.
			FundInvestmentIntention intention = 
			 FundInvestmentIntention.getByAccountId(account.getAccountId(), facade);
			
			if (intention != null && intention.getCash() != null) {
				map.put(UCMFieldNames.Amount, 
				 CCLAUtils.PLAIN_NUMBER_FORMAT.format(intention.getCash()));
			}
			
		} else {
			// Nothing we can do here - more than 1 mapped account!
			Log.error("Unable to apply doc metadata mapping to Form ID: " 
			 + form.getFormId() + ", type: " + form.getFormType().getName() + ", "
			 + "too many mapped accounts (" + accounts.size() + ")");
		}
		
		return map;
	}
}
