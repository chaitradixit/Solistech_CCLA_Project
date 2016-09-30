package com.ecs.stellent.ccla.clientservices.form;

import intradoc.common.ServiceException;
import intradoc.data.DataException;

import java.util.HashMap;
import java.util.Vector;

import com.ecs.stellent.ccla.clientservices.campaign.PSDFEnrolmentHandler;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.AuroraClient;
import com.ecs.ucm.ccla.data.Contact;
import com.ecs.ucm.ccla.data.Element;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.RelationName;
import com.ecs.ucm.ccla.data.campaign.Campaign;
import com.ecs.ucm.ccla.data.campaign.CampaignEnrolment;
import com.ecs.ucm.ccla.data.form.Form;
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
public class PSDFAccountFormHandler extends AccountFormHandler {

	public PSDFAccountFormHandler(Form form, String userId, FWFacade facade) {
		super(form, userId, facade);
	}
	
	public PSDFAccountFormHandler(String userId, FWFacade facade) {
		super(userId, facade);
	}

	/** Generates forms for existing PSDF accounts.
	 *  
	 *  The passed Element is expected to be a PC account, belonging to a client who
	 *  is enrolled to the PSDF Registration campaign.
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
		
		// Fail if fund code != 'PC'
		if (!account.getFundCode().equals(PSDFEnrolmentHandler.PSDF_FUND_CODE))
			throw new DataException("Unable to generate PSDF account form, fund code " +
			 "was " + account.getFundCode() + " (expected: " + 
			 PSDFEnrolmentHandler.PSDF_FUND_CODE + ")");
		
		// Fetch data elements required to generate form
		Integer orgId = account.getOwnerOrganisationId(facade);
		Entity org = Entity.get(orgId, facade);
		
		Vector<AuroraClient> auroraClients = 
		 Entity.getAuroraClientMapping(orgId, facade);
		
		if (auroraClients.isEmpty()) {
			throw new DataException("Unable to generate PSDF account form, no " +
			 "Client Number set");
		}
		
		Account mandatedAccount = Account.get(account.getMandatedAccId(), facade);
		
		// Fetch RelationName -> Person mapping for account, with Contact details
		// pre-loaded.
		HashMap<RelationName, Vector<Person>> relatedAccountPersons = 
		 account.getRelatedPersons(true, facade);
		
		// Search for existing PSDF Campaign enrolment. Fail if the organisation isn't
		// enrolled.
		CampaignEnrolment enrolment = CampaignEnrolment.get
		 (PSDFEnrolmentHandler.CAMPAIGN_ID, orgId, facade);
		
		if (enrolment == null) {
			throw new DataException("Unable to generate account form: organisation " +
			 "not enrolled to PSDF Registration campaign");
		}
		
		if (!enrolment.isContactable())
			throw new DataException("Unable to generate account form: no assigned " +
			 "person and/or contact address");
		
		Contact postalAddressContact = Contact.get(enrolment.getContactId(), facade);
		Person correspondent = Person.get(enrolment.getPersonId(), true, facade);
		
		this.form = PSDFFormUtils.createWelcomePackForm
		 (enrolment, org, auroraClients.get(0), 
		 postalAddressContact, 
		 correspondent, 
		 formType, account, mandatedAccount,
		 relatedAccountPersons, true, userId, facade);
		
		return this.form;
	}
}
