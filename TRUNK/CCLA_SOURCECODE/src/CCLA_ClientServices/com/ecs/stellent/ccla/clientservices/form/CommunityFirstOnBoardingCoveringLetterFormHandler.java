package com.ecs.stellent.ccla.clientservices.form;

import intradoc.common.ServiceException;
import intradoc.data.DataException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import com.ecs.stellent.ccla.clientservices.campaign.CommunityFirstClientEnrolmentHandler;
import com.ecs.stellent.ccla.clientservices.spool.CommunityFirstOnBoardingCoveringLetterSpoolFileGenerator;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.Address;
import com.ecs.ucm.ccla.data.AuroraClient;
import com.ecs.ucm.ccla.data.BankAccount;
import com.ecs.ucm.ccla.data.Company;
import com.ecs.ucm.ccla.data.Contact;
import com.ecs.ucm.ccla.data.Element;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.RelationName;
import com.ecs.ucm.ccla.data.campaign.ApplicableEnrolmentAttribute;
import com.ecs.ucm.ccla.data.campaign.CampaignActivityType;
import com.ecs.ucm.ccla.data.campaign.CampaignEnrolment;
import com.ecs.ucm.ccla.data.campaign.EnrolmentAttributeApplied;
import com.ecs.ucm.ccla.data.form.Form;
import com.ecs.ucm.ccla.data.form.FormElementApplied;
import com.ecs.ucm.ccla.data.form.FormHandler;
import com.ecs.ucm.ccla.data.form.FormType;
import com.ecs.ucm.ccla.data.form.FormUtils;
import com.ecs.ucm.ccla.data.form.spool.SpoolHeader;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Handler class for the Community First On-boarding Covering Letter.
 *  
 *  Displays correspondence address and a list of Authorising Persons for the
 *  Organisation.
 *  
 * @author Tom
 *
 */
public class CommunityFirstOnBoardingCoveringLetterFormHandler extends FormHandler {

	public CommunityFirstOnBoardingCoveringLetterFormHandler(String username,
			FWFacade facade) {
		this.userId = username;
		this.facade = facade;
	}
	
	public CommunityFirstOnBoardingCoveringLetterFormHandler
	 (Form form, String userId, FWFacade facade) {
		this.form = form;
		this.userId = userId;
		this.facade = facade;
	}

	@Override
	public Form generateForm(FormType formType, Element element,
	 Integer investmentId) throws ServiceException, DataException {
		
		Entity org = Entity.get(element.getElementId(), true, facade);
		
		if (org == null)
			throw new DataException("Unable to generate " + formType.getName() + 
			 ": no org found with ID " + element.getElementId());
		
		Log.debug("Generating " + formType.getName() + " form for Org ID: " 
		 + org.getOrganisationId());
		
		// Run various checks to ensure we have minimal data to generate the form.
		
		CampaignEnrolment enrolment = CampaignEnrolment.get
		 (CommunityFirstClientEnrolmentHandler.CAMPAIGN_ID, 
		 org.getOrganisationId(), facade);
		
		if (enrolment == null)
			throw new DataException("Unable to generate " + formType.getName() + ", " +
			 org.getOrganisationName() + " is not enrolled to Community First campaign");
		
		if (!enrolment.isContactable())
			throw new DataException("Unable to generate " + formType.getName() + ", " +
			 "campaign enrolment does not have a correspondent/address set");
		
		Contact contact = Contact.get(enrolment.getContactId(), facade);
	
		boolean isAddressValid = Address.addressDataValid
		 (contact.getAddressId(), facade);
		
		if (!isAddressValid)
			throw new ServiceException("Assigned postal address is not valid, " +
			 "ensure it has a number/street/postcode set");

		Form form = createCoveringLetterForm
		 (org, formType, enrolment, false, true, facade, userId);
		
		return form;
	}
	
	/** Generates a single Client Info Form for the passed Org.
	 *  
	 *  This includes adding a new entry to the FORM table and optionally checking 
	 *  in/printing the form.
	 *  
	 * @param account
	 * @param enrolment
	 * @param facade
	 * @param userName
	 * @return
	 * @throws DataException 
	 * @throws ServiceException 
	 */
	public static Form createCoveringLetterForm(
	 Entity org,
	 FormType formType,
	 CampaignEnrolment enrolment, 
	 boolean checkin, boolean print,
	 FWFacade facade, String userName) throws DataException, ServiceException {

		// Fetch Enrolment attributes
		Vector<EnrolmentAttributeApplied> enrolmentAttribsApplied = 
		 EnrolmentAttributeApplied.getByEnrolmentId
		 (enrolment.getCampaignEnrolmentId(), facade);
		
		HashMap<Integer, EnrolmentAttributeApplied> enrolmentAttributes = 
		 EnrolmentAttributeApplied.getMapping(enrolmentAttribsApplied);
		
		Account account = CommunityFirstClientEnrolmentHandler.getDonationAccount
		 (org.getOrganisationId(), facade);
		
		if (account == null)
			throw new ServiceException("Create the Community First Donation Account " +
			 "first before generating the on-boarding letter.");
		
		BankAccount bankAccount = Account.getNominatedBankAccount
		 (account.getAccountId(), RelationName.getCache().getCachedInstance(
		  RelationName.AccountBankAccountRelation.WITHDRAWAL), 
		 facade);
		
		int personId = enrolment.getPersonId();
		
		// Fetch person, with associated contact data
		Person person = Person.get(personId, true, facade);
		
		// Fetch assigned contact point
		Contact contact = Contact.get(enrolment.getContactId(), facade);
		
		Company coifCompany = Company.getCache().getCachedInstance(Company.COIF);
		
		// Fetch AuroraClient mapping for this entity
		AuroraClient auroraClient = Entity.getAuroraClientCompanyMapping
		 (org.getOrganisationId(), coifCompany, facade);
		
		if (auroraClient == null)
			throw new ServiceException("Unable to generate form: " +
			 "organisation had no Aurora Client Number set against Company: " + 
			 coifCompany.getCode());

		Integer enrolmentId = 
		 enrolment != null ? enrolment.getCampaignEnrolmentId() : null;
		
		// Fetch related Account persons. We don't need their contact details, only 
		// their names.
		HashMap<RelationName, Vector<Person>> relatedAccountPersons = 	
		 account.getRelatedPersons(false, facade);	
		
		ByteArrayOutputStream outputStream = null;
		Form form = null;
		
		try {
			// Generate a new Form entry in the DB.
			form = Form.add(
				formType,
				enrolmentId,
				null,
				personId,
				null,
				org.getOrganisationId(),
				null,
				facade,
				userName	
			);
			
			// Link the bank account to the generated form
			if (bankAccount != null) {
				FormElementApplied.add
				 (form.getFormId(), bankAccount.getBankAccountId(), facade);
			}
			
			String fileName = FormUtils.getSpoolFileName
			 (form, org, auroraClient);
			
			outputStream = createCoveringLetterSpoolFile(
			 form, 
			 fileName,
			 contact,
			 person,
			 org,
			 auroraClient,
			 account,
			 bankAccount,
			 relatedAccountPersons,
			 enrolmentAttributes,
			 userName
			);
			
			// Generate a new file in the temporary location
			File spoolFile = FormUtils.createTempSpoolFile
			 (fileName, coifCompany.getCode(), outputStream);
			
			form.setDateGenerated(new Date());
			
			Log.debug("Generated temp spool file: " + spoolFile.getName() + 
			 ". Dispatching for print.");
			
			if (print) {
				FormUtils.printForm(spoolFile.getAbsolutePath());
				
				// Update form data to indicate it was printed
				form.setFormStatusId(Form.FormStatus.PRINTED.id);
				form.setDatePrinted(new Date());
			} else {
				// Set generated status
				form.setFormStatusId(Form.FormStatus.GENERATED.id);
			}
			
			form.persist(facade, userName);
			
			// Add activity log to campaign enrolment, indicating form
			// was generated.
			enrolment.addActivity(personId, 
			 CampaignActivityType.FORM_CREATION_ACTIVITY_ID,
			 formType.getName() + " form " + form.getFormId() + 
			 " created and dispatched for print", null, facade, userName); 

		} catch (Exception e) {
			Log.error("Failed to generate/print " + formType.getName() + " form", e);
			
			// Add activity log to Client Services process, indicating form
			// failed to generate.
			enrolment.addActivity(personId, 
			 CampaignActivityType.FORM_CREATION_ACTIVITY_ID,
			 "Failed to generate " + formType.getName() + " form", 
			 null, facade, userName); 
			
			throw new ServiceException(e);
		}
		
		return form;
	}
	
	private static ByteArrayOutputStream createCoveringLetterSpoolFile(
	 Form form, 
	 String fileName,
	 Contact contactAddress,
	 Person correspondent,
	 Entity org,
	 AuroraClient auroraClient,
	 Account account,
	 BankAccount bankAccount,
	 HashMap<RelationName, Vector<Person>> relatedAccountPersons,
	 HashMap<Integer, EnrolmentAttributeApplied> enrolmentAttributes,
	 String userName) throws IOException, DataException, ServiceException {
		
		Log.debug("Creating Spool File for form type: " 
		 + form.getFormType().getName() +
		 ", org: " + org.getOrganisationId());
		
		Company coifCompany = Company.getCache().getCachedInstance(Company.COIF);
		String clientIdStr = auroraClient.getPaddedClientNumber();
		
		AccountRegistrationForm regForm = new AccountRegistrationForm(
		 form.getFormId(), 
		 contactAddress, 
		 correspondent, 
		 org, 
		 auroraClient, 
		 null, 
		 null,
		 account,
		 null,
		 null,
		 bankAccount, 
		 null, 
		 null,
		 relatedAccountPersons, 
		 false
		);
		
		String templatePath			= fileName;
		
		SpoolHeader spoolHeader 	= new SpoolHeader
		 (coifCompany.getCode(), clientIdStr, 
		 userName, 1, form.getFormId(), org.getOrganisationId(), templatePath);

		CommunityFirstOnBoardingCoveringLetterSpoolFileGenerator spoolFileGenerator = 
		 new CommunityFirstOnBoardingCoveringLetterSpoolFileGenerator(spoolHeader, regForm);
				
		ByteArrayOutputStream outputStream = 
		 spoolFileGenerator.createSpoolFile();
		
		return outputStream;
	}

}
