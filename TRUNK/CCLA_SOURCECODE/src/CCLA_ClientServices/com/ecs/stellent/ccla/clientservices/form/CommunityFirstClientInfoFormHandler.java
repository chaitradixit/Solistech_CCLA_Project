package com.ecs.stellent.ccla.clientservices.form;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import intradoc.common.ServiceException;
import intradoc.data.DataException;

import com.ecs.stellent.ccla.clientservices.campaign.CommunityFirstClientEnrolmentHandler;
import com.ecs.stellent.ccla.clientservices.spool.CommunityFirstClientInformationSpoolFileGenerator;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.Address;
import com.ecs.ucm.ccla.data.AuroraClient;
import com.ecs.ucm.ccla.data.BankAccount;
import com.ecs.ucm.ccla.data.Company;
import com.ecs.ucm.ccla.data.Contact;
import com.ecs.ucm.ccla.data.Element;
import com.ecs.ucm.ccla.data.ElementAttributeApplied;
import com.ecs.ucm.ccla.data.ElementIdentifierApplied;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Fund;
import com.ecs.ucm.ccla.data.OrganisationCategory;
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
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Client Info form handler for Community First Clients.
 *  
 *  Client-centric form, as opposed to Account-centric.
 *  
 * @author Tom
 *
 */
public class CommunityFirstClientInfoFormHandler extends FormHandler {

	public CommunityFirstClientInfoFormHandler(Form form, String userId,
			FWFacade facade) {
		super(form, userId, facade);
		// TODO Auto-generated constructor stub
	}

	public CommunityFirstClientInfoFormHandler(String userId,
			FWFacade facade) {
		super(userId, facade);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Form generateForm(FormType formType, Element element, Integer investmentId)
	 throws ServiceException, DataException {
		
		Entity org = Entity.get(element.getElementId(), true, facade);
		
		if (org == null)
			throw new DataException("Unable to generate " + formType.getName() + 
			 ": no org found with ID " + element.getElementId());
		
		Log.debug("Generating " + formType.getName() + " form for Org ID: " 
		 + org.getOrganisationId());
		
		// Run various checks to ensure we have minimal data to generate the form.
		
		CampaignEnrolment enrolment = CampaignEnrolment.get
		 (CommunityFirstClientEnrolmentHandler.CAMPAIGN_ID, org.getOrganisationId(), facade);
		
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

		Form form = createRegistrationForm
		 (org, formType, enrolment, false, true, facade, userId);
		
		return form;
	}
	
	@Override
	public void doPostCheckinActions(int docId) throws Exception {
		// TODO Auto-generated method stub
		super.doPostCheckinActions(docId);
	}

	@Override
	public void doPostValidateActions() throws DataException, ServiceException {
		// TODO Auto-generated method stub
		super.doPostValidateActions();
	}

	@Override
	public Hashtable<String, String> getDocMetaMapping() throws DataException {
		
		Hashtable<String, String> map = super.getDocMetaMapping();
		
		// Search for the Community First Donation account, if this Org has one.
		Account account = 
		 CommunityFirstClientEnrolmentHandler.getDonationAccount
		 (this.form.getOrganisationId(), this.facade);
		
		if (account != null)
			AccountFormHandler.addAccountDocMeta(map, account);
		
		return map;
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
	public static Form createRegistrationForm(
	 Entity org,
	 FormType formType,
	 CampaignEnrolment enrolment, 
	 boolean checkin, boolean print,
	 FWFacade facade, String userName) throws DataException, ServiceException {

		// Fetch org attributes
		HashMap<Integer, ElementAttributeApplied> orgAttributes = 
		 ElementAttributeApplied.getMapping(
		 ElementAttributeApplied.getAll(org.getOrganisationId(), false, facade));
		
		// Create HashMap to store ELEMENT_IDENTIFIER_ID -> Identifier Values.
		// This is used to store Charity Reg Numbers, HMRC Numbers etc.
		HashMap<Integer, String> orgIdentifiers = new HashMap<Integer, String>();
		
		// Fetch mapped Identifiers for Organisation
		Vector<ElementIdentifierApplied> identsApplied = 
		 ElementIdentifierApplied.getElementIdentifiersApplied
		 (org.getOrganisationId(), facade);
		
		for (ElementIdentifierApplied eIdA : identsApplied)
			orgIdentifiers.put(eIdA.getIdentifierId(), eIdA.getIdentifierValue());
		
		// Get the Org Category tree
		Vector<String> orgCategoryTree = null;
		
		if (org.getCategoryId() != null)
			orgCategoryTree = OrganisationCategory.getCategoryTree
			 (org.getCategoryId(), facade);
		
		// Fetch Enrolment attributes
		Vector<EnrolmentAttributeApplied> enrolmentAttribsApplied = 
		 EnrolmentAttributeApplied.getByEnrolmentId
		 (enrolment.getCampaignEnrolmentId(), facade);
		
		BankAccount bankAccount = null;
		
		HashMap<Integer, EnrolmentAttributeApplied> enrolmentAttributes = 
		 EnrolmentAttributeApplied.getMapping(enrolmentAttribsApplied);
		
		// Fetch the 'Bank Account' enrolment attribute, if it exists.
		EnrolmentAttributeApplied bankAccAttrib = enrolmentAttributes.get
		 (ApplicableEnrolmentAttribute.Ids.COMM_FIRST_BANK_ACCOUNT);
		
		if (bankAccAttrib != null) {
			// Lookup the bank account using the attrib value.
			int bankAccountId = Integer.parseInt(bankAccAttrib.getValue());
			bankAccount = BankAccount.get(bankAccountId, facade);
		}
		
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
		
		// Don't actually fetch the related persons for now.
		
		HashMap<RelationName, Vector<Person>> relatedOrgPersons = 
		 new HashMap<RelationName, Vector<Person>>();	
		// org.getRelatedPersons(true, facade);
		
		boolean emailIndemnityReceived = false;
		
		// Determine whether this bank account has already been validated for use by 
		// this organisation.
		boolean requireBankStatement = true; 

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
			
			outputStream = createRegistrationSpoolFile(
			 form, 
			 fileName,
			 contact,
			 person,
			 org,
			 orgAttributes,
			 orgIdentifiers,
			 orgCategoryTree,
			 auroraClient,  
			 bankAccount,
			 relatedOrgPersons,
			 emailIndemnityReceived,
			 requireBankStatement,
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
	
	private static ByteArrayOutputStream createRegistrationSpoolFile(
	 Form form, 
	 String fileName,
	 Contact contactAddress,
	 Person correspondent,
	 Entity org,
	 HashMap<Integer, ElementAttributeApplied> orgAttributes,
	 HashMap<Integer, String> orgIdentifiers,
	 Vector<String> orgCategoryTree,
	 AuroraClient auroraClient,
	 BankAccount bankAccount,
	 HashMap<RelationName, Vector<Person>> relatedOrgPersons,
	 boolean emailIndemnityReceived,
	 boolean requireBankStatement,
	 HashMap<Integer, EnrolmentAttributeApplied> enrolmentAttributes,
	 String userName) throws IOException, DataException, ServiceException {
		
		Log.debug("Creating Spool File for form type: " 
		 + form.getFormType().getName() +
		 ", org: " + org.getOrganisationId());
		
		Company coifCompany = Company.getCache().getCachedInstance(Company.COIF);
		String clientIdStr = auroraClient.getPaddedClientNumber();
		
		ClientRegistrationForm regForm = new ClientRegistrationForm(
		 form.getFormId(), 
		 contactAddress, 
		 correspondent, 
		 org, 
		 auroraClient, 
		 orgIdentifiers, 
		 orgAttributes,
		 null,
		 bankAccount, 
		 orgCategoryTree, 
		 relatedOrgPersons, 
		 emailIndemnityReceived,
		 enrolmentAttributes,
		 null
		);
		
		String templatePath			= fileName;
		
		SpoolHeader spoolHeader 	= new SpoolHeader
		 (coifCompany.getCode(), clientIdStr, 
		 userName, 1, form.getFormId(), org.getOrganisationId(), templatePath);

		CommunityFirstClientInformationSpoolFileGenerator spoolFileGenerator = 
		 new CommunityFirstClientInformationSpoolFileGenerator(spoolHeader, regForm);
				
		ByteArrayOutputStream outputStream = 
		 spoolFileGenerator.createSpoolFile();
		
		return outputStream;
	}
}
