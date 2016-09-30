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
import intradoc.data.DataResultSet;

import com.ecs.stellent.ccla.clientservices.campaign.DioLoanEnrolmentHandler;
import com.ecs.stellent.ccla.clientservices.campaign.SegregatedClientEnrolmentHandler;
import com.ecs.stellent.ccla.clientservices.spool.DioLoanRegistrationSpoolFileGenerator;
import com.ecs.stellent.ccla.clientservices.spool.SegregatedClientRegistrationSpoolFileGenerator;
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
import com.ecs.ucm.ccla.data.OrganisationCategory;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.Relation;
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

/** Registration Form handler for Segregated Clients.
 *  
 *  Client-centric form, as opposed to Account-centric.
 *  
 * @author Tom
 *
 */
public class SegregatedClientRegistrationFormHandler extends FormHandler {

	public SegregatedClientRegistrationFormHandler(Form form, String userId,
			FWFacade facade) {
		super(form, userId, facade);
		// TODO Auto-generated constructor stub
	}

	public SegregatedClientRegistrationFormHandler(String userId,
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
		
		// Fail if fund code != Seg Client fund code
		/*
		if (!account.getFundCode().equals(DioLoanEnrolmentHandler.DIOLOAN_FUND_CODE))
			throw new DataException("Unable to generate " + formType.getName() + 
			 ", fund code was " + account.getFundCode() + " (expected: " + 
			 DioLoanEnrolmentHandler.DIOLOAN_FUND_CODE + ")");
		
		
		// Fail if account type != 'Loan'
		if (account.getAccountType() != Account.AccountType.LOAN)
			throw new DataException("Unable to generate " + formType.getName() + 
			 ", not a loan account");
		*/
		
		// Run various checks to ensure we have minimal data to generate the form.
		
		CampaignEnrolment enrolment = CampaignEnrolment.get
		 (SegregatedClientEnrolmentHandler.CAMPAIGN_ID, org.getOrganisationId(), facade);
		
		if (enrolment == null)
			throw new DataException("Unable to generate " + formType.getName() + ", " +
			 org.getOrganisationName() + " is not enrolled to Seg Client campaign");
		
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
		
		/*
		if (enrolment != null) {
			String activityMessage = formType.getName() + 
			 " form ID " + form.getFormId() + " created and dispatched for print";
			int activityTypeId = CampaignActivityType.FORM_CREATION_ACTIVITY_ID;
			
			enrolment.addActivity(null, activityTypeId, activityMessage, 
			 null, facade, userId);
			
			// Update enrolment last updated fields
			enrolment.persist(facade, this.userId);
		}
		*/
		
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
		// TODO Auto-generated method stub
		return super.getDocMetaMapping();
	}
	
	/** Generates a single Seg. Client Registration Form for the passed Org.
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
		 (ApplicableEnrolmentAttribute.Ids.SEG_CLIENT_BANK_ACCOUNT);
		
		if (bankAccAttrib != null) {
			// Lookup the bank account using the attrib value.
			int bankAccountId = Integer.parseInt(bankAccAttrib.getValue());
			bankAccount = BankAccount.get(bankAccountId, facade);
		}
		
		int personId = enrolment.getPersonId();
		
		// Fetch contact person, with associated contact data
		Person contactPerson = Person.get(personId, true, facade);
		
		// Fetch assigned contact point, where the form will be addressed to. May
		// not necessarily match the nominated contact address for the above person!
		Contact contact = Contact.get(enrolment.getContactId(), facade);
		
		Company cclaCompany = Company.getCache().getCachedInstance(Company.CCLA);
		
		// Fetch AuroraClient mapping for this entity
		AuroraClient auroraClient = Entity.getAuroraClientCompanyMapping
		 (org.getOrganisationId(), cclaCompany, facade);
		
		if (auroraClient == null)
			throw new ServiceException("Unable to generate form: " +
			 "organisation had no Aurora Client Number set against Company: " + 
			 cclaCompany.getCode());

		Integer enrolmentId = 
		 enrolment != null ? enrolment.getCampaignEnrolmentId() : null;
		
		HashMap<RelationName, Vector<Person>> relatedOrgPersons = 
		 org.getRelatedPersons(true, facade);

		RelationName orgCorrRelName = RelationName.getCache().getCachedInstance
		 (RelationName.OrganisationPersonRelation.CORRESPONDENT);
		
		// Remove any other correspondents from the map, if present.
		Vector<Person> orgCorrs = relatedOrgPersons.get(orgCorrRelName);
		
		if (orgCorrs != null && orgCorrs.size() > 1) {
			// Ambiguous correspondent - more than 1 set.
			
			// Determine the explicitly nominated correspondent, if one exists.
			Person nominatedOrgCorr = Entity.getNominatedCorrespondent
			 (org.getOrganisationId(), true, facade);
			
			if (nominatedOrgCorr == null) {
				throw new ServiceException("Unable to determine the nominated correspondent "+
				 "for this organisation - ensure one relationship is set as Nominated");
			}
			
			Log.debug("Reducing list of mapped correspondents to single nom. corr:" +
			 nominatedOrgCorr.getFullName());
			
			// Replace the list of org correspondents with the single nominated one.
			orgCorrs.clear();
			orgCorrs.add(nominatedOrgCorr);
		}
		
		boolean emailIndemnityReceived = false;
		/*
		 AuroraClient.getNumClientDocsWithDocumentClass
		 (auroraClient, "EMAILINDEMNITY", facade) > 0;
		*/
		
		// Determine whether this bank account has already been validated for use by 
		// this organisation.
		boolean requireBankStatement = true; 

		// Fetch all client bank accounts which are linked to the nominated bank
		// account.
		/*
		Vector<Account> accountsWithSameBankAccount = 
		 Relation.getRelatedBankAccountAccounts
		 (nominatedBankAccount.getBankAccountId(), orgId, facade);
		
		Log.debug("Found " + accountsWithSameBankAccount.size() + " org accounts "+
		 "with designated bank account ID " + nominatedBankAccount.getBankAccountId());
		
		for (Account orgAccount : accountsWithSameBankAccount) {
			if (account.getAccountId() == orgAccount.getAccountId())
				continue;
			
			if (orgAccount.getAccountType() != Account.AccountType.LOAN) {
				// Found a relation between the nominated account and a non-loan
				// account. Assume it has been verified in the past.
				Log.debug("Found a link to non-loan account ID " 
				 + orgAccount.getAccountId() + 
				 ". Assuming bank account has already been verified");
				
				requireBankStatement = false;
				break;
			}
		}
		*/
		
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
			 contactPerson,
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
			 (fileName, cclaCompany.getCode(), outputStream);
			
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
		
		Company cclaCompany = Company.getCache().getCachedInstance(Company.CCLA);
		String clientIdStr = auroraClient.getPaddedClientNumber();
		
		SegregatedClientRegistrationForm regForm = new SegregatedClientRegistrationForm(
		 form.getFormId(), 
		 contactAddress, 
		 correspondent, 
		 org, 
		 auroraClient, 
		 orgIdentifiers, 
		 orgAttributes,
		 bankAccount, 
		 orgCategoryTree, 
		 relatedOrgPersons, 
		 emailIndemnityReceived,
		 enrolmentAttributes
		);
		
		String templatePath			= fileName;
		
		SpoolHeader spoolHeader 	= new SpoolHeader
		 (cclaCompany.getCode(), clientIdStr, 
		 userName, 1, form.getFormId(), org.getOrganisationId(), templatePath);

		SegregatedClientRegistrationSpoolFileGenerator spoolFileGenerator = 
		 new SegregatedClientRegistrationSpoolFileGenerator(spoolHeader, regForm);
				
		ByteArrayOutputStream outputStream = 
		 spoolFileGenerator.createSpoolFile();
		
		return outputStream;
	}
}
