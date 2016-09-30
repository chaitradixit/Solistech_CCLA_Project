package com.ecs.stellent.ccla.clientservices.form;

import intradoc.common.ServiceException;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import com.ecs.stellent.ccla.clientservices.campaign.PSDFEnrolmentHandler;
import com.ecs.stellent.ccla.clientservices.spool.PSDFAccountSpoolFileGenerator;
import com.ecs.stellent.ccla.clientservices.spool.PSDFAdditionalAccountSpoolFileGenerator;
import com.ecs.stellent.ccla.clientservices.spool.PSDFAdditionalBankAccountSpoolFileGenerator;
import com.ecs.stellent.ccla.clientservices.spool.PSDFChangeOfDividendPaymentSpoolFileGenerator;
import com.ecs.stellent.ccla.clientservices.spool.PSDFRegistrationSpoolFileGenerator;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.AuroraClient;
import com.ecs.ucm.ccla.data.BankAccount;
import com.ecs.ucm.ccla.data.Company;
import com.ecs.ucm.ccla.data.Contact;
import com.ecs.ucm.ccla.data.ElementIdentifierApplied;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Fund;
import com.ecs.ucm.ccla.data.OrganisationCategory;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.Relation;
import com.ecs.ucm.ccla.data.RelationName;
import com.ecs.ucm.ccla.data.campaign.CampaignActivityType;
import com.ecs.ucm.ccla.data.campaign.CampaignEnrolment;
import com.ecs.ucm.ccla.data.form.Form;
import com.ecs.ucm.ccla.data.form.FormElementApplied;
import com.ecs.ucm.ccla.data.form.FormType;
import com.ecs.ucm.ccla.data.form.FormUtils;
import com.ecs.ucm.ccla.data.form.spool.SpoolHeader;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Methods used for generation of the following PSDF forms:
 * 
 *   -Application (LA, Non-LA) 
 *   -Additional account
 *   -Change of Dividend Payment
 *   -Subscription
 *   -Redemption
 *   -Cancellation
 *   
 * @author Tom
 *
 */
public class PSDFFormUtils {
	
	/** Generates pre-filled PSDF Reg. Form spool files and places them in the
	 *  temporary spool directory. 
	 *  
	 *  One spool file is generated for each PC account belonging to the client which is
	 *  at 'TEMP' status, unless the passed account parameter is non-null. If account
	 *  is non-null, a single reg form is generated for that given account.
	 *  
	 *  Two types of reg. form can be generated from this method:
	 *  -LA: Designed for Local Authorities
	 *  -Non-LA: For everyone else.
	 *  The generated type is determined by the FormType parameter.
	 *  
	 *  If the print flag is set, the file is sent to the Aurora Forms printer.
	 *  
	 *  If the checkin flag is set, the spool file is checked into UCM.
	 *  
	 * @param processId
	 * @param facade
	 * @param userName
	 * @throws ServiceException
	 * @throws DataException
	 */
	public static Vector<Form> createRegistrationForms
	 (CampaignEnrolment enrolment, FormType formType, Account account,
	 boolean checkin, boolean print, FWFacade facade, String userName) 
	 throws ServiceException, DataException {
		
		if (enrolment == null)
			throw new ServiceException("Unable to generate PSDF Reg. form(s): " +
			 "passed CampaignEnrolment was null");
		
		int enrolmentId				= enrolment.getCampaignEnrolmentId();
		
		Integer orgId				= enrolment.getOrganisationId();
		Integer personId 			= enrolment.getPersonId();
		Integer contactId 			= enrolment.getContactId();
		
		Log.debug("Generating PSDF Reg. form(s) for Campaign Enrolment: " 
		 + enrolmentId + ", entity: " + orgId);
		
		// Fetch entity, with associated contact data
		Entity entity = Entity.get(orgId, true, facade);
		
		// Fetch person, with associated contact data
		Person person = Person.get(personId, true, facade);
		
		// Fetch assigned contact point
		Contact contact = Contact.get(contactId, facade);
		if (contact.getAddress() == null) {
			throw new ServiceException
			 ("Assigned Contact Point did not have an address set");
		} else {
			Log.debug("Assigned postal address for form: " + 
			 contact.getAddress().toString());
		}
		
		if (entity == null)
			throw new ServiceException("Unable to generate form: " +
			 "could not find associated organisation with ID: " + orgId);
		
		// Fetch AuroraClient mapping for this entity
		Company psicCompany = Company.getCache().getCachedInstance(Company.PSIC);
		AuroraClient auroraClient = Entity.getAuroraClientCompanyMapping
		 (orgId, psicCompany, facade);

		if (auroraClient == null)
			throw new ServiceException("Unable to generate form: " +
			 "organisation had no PSIC Aurora Client Number set");
		
		Vector<Account> accounts = null;
		
		if (account == null) {
			// Fetch all PSDF accounts that are linked to the campaign fund code (ie 'PC'),
			// organisation and are in 'TEMP' status.
			// One form is created per account.
			DataResultSet rsAccounts = 
			 Account.getClientAccountsDataByStatusFund(orgId, Account.AccountStatus.TEMP, 
			 Fund.FundCodes.PC.toString(), facade);		
	
			Log.debug("Found " + rsAccounts.getNumRows() +
			 " template " + PSDFEnrolmentHandler.PSDF_FUND_CODE + 
			 " accounts for Organisation");
			
			if (rsAccounts.isEmpty()) {
				throw new ServiceException("No template " 
				 + PSDFEnrolmentHandler.PSDF_FUND_CODE + " accounts for Organisation");
			}
			
			accounts = Account.getAll(rsAccounts);
		} else {
			// Generate form for single passed account.
			accounts = new Vector<Account>();
			accounts.add(account);
		}
		
		Vector<Form> generatedForms = new Vector<Form>();
		
		// Loop through each account, generating a Reg. form for each.
		for (Account thisAccount : accounts)  {
			
			ByteArrayOutputStream outputStream = null;

			try {
				// Determine form type
				
				Integer orgCategory = entity.getCategoryId();
				// TODO figure out if this is an LA category/subcategory
						
				// Generate a new Form entry in the DB.
				Form form = Form.add(
					formType,
					enrolmentId,
					null,
					personId,
					null,
					entity.getOrganisationId(),
					null,
					facade,
					userName	
				);
				
				// Link the account to the generated form
				FormElementApplied.add
				 (form.getFormId(), thisAccount.getAccountId(), facade);
				
				String fileName = FormUtils.getSpoolFileName
				 (form, entity, auroraClient);
				
				outputStream = createPSDFRegistrationSpoolFile
				 (enrolment, form, fileName, entity, auroraClient, contact, person, 
				  thisAccount, userName, facade);
				
				// Generate a new file in the temporary location
				File spoolFile = FormUtils.createTempSpoolFile
				 (fileName, "PSIC", outputStream);
				
				// Update form data to indicate it was generated
				//form.setFormStatusId(Form.FormStatus.GENERATED.id);
				//form.setDateGenerated(new Date());
				
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
				
				// Add activity log to Client Services process, indicating form
				// was generated.
				
				enrolment.addActivity(personId, 
				 CampaignActivityType.FORM_CREATION_ACTIVITY_ID,
				 "PSDF Registration form " + form.getFormId() + 
				 " created and dispatched for print", null, facade, userName); 
				
				generatedForms.add(form);
				
			} catch (Exception e) {
				Log.error("Failed to generate/print PSDF Reg. form", e);
				
				// Add activity log to Client Services process, indicating form
				// failed to generate.
				
				enrolment.addActivity(personId, 
				 CampaignActivityType.FORM_CREATION_ACTIVITY_ID,
				 "Failed to generate PSDF Registration form", null, facade, userName); 
				
				throw new ServiceException(e);
			}
		}
		
		return generatedForms;
	}
	
	/** Generates pre-filled PSDF Change of Dividend Payment form, for the given 
	 *  account.
	 *  
	 *  
	 * @param processId
	 * @param facade
	 * @param userName
	 * @throws ServiceException
	 * @throws DataException
	 */
	public static Form createChangeOfDividendPaymentForm
	 (CampaignEnrolment enrolment, Account account, 
	 BankAccount bankAccount, BankAccount payAwayBankAccount,
	 boolean checkin, boolean print, FWFacade facade, String userName) 
	 throws ServiceException, DataException {
		
		// Determine form type
		FormType formType = FormType.getCache().getCachedInstance
		 (FormType.PSDF_CHANGE_OF_DIVIDEND_PAYMENT);
		
		if (enrolment == null)
			throw new ServiceException
			 ("Unable to generate " + formType.getName() + ": " +
			 "passed CampaignEnrolment was null");
		
		int enrolmentId				= enrolment.getCampaignEnrolmentId();
		
		Integer orgId				= enrolment.getOrganisationId();
		Integer personId 			= enrolment.getPersonId();
		Integer contactId 			= enrolment.getContactId();
		
		Log.debug("Generating " + formType.getName() + " for Campaign Enrolment: " 
		 + enrolmentId + ", entity: " + orgId);
		
		// Fetch entity, with associated contact data
		Entity entity = Entity.get(orgId, true, facade);
		
		// Fetch person, with associated contact data
		Person person = Person.get(personId, true, facade);
		
		// Fetch assigned contact point
		Contact contact = Contact.get(contactId, facade);
		if (contact.getAddress() == null) {
			throw new ServiceException
			 ("Assigned Contact Point did not have an address set");
		} else {
			Log.debug("Assigned postal address for form: " + 
			 contact.getAddress().toString());
		}
		
		if (entity == null)
			throw new ServiceException("Unable to generate form: " +
			 "could not find associated organisation with ID: " + orgId);
		
		// Fetch AuroraClient mapping for this entity
		Company psicCompany = Company.getCache().getCachedInstance(Company.PSIC);
		AuroraClient auroraClient = Entity.getAuroraClientCompanyMapping
		 (orgId, psicCompany, facade);

		if (auroraClient == null)
			throw new ServiceException("Unable to generate form: " +
			 "organisation had no PSIC Aurora Client Number set");
		
		ByteArrayOutputStream outputStream = null;

		try {
			// Generate a new Form entry in the DB.
			Form form = Form.add(
				formType,
				enrolmentId,
				null,
				personId,
				null,
				entity.getOrganisationId(),
				null,
				facade,
				userName	
			);
			
			// Link the account to the generated form
			FormElementApplied.add
			 (form.getFormId(), account.getAccountId(), facade);
			
			// Link the bank account to the generated form, if it was supplied
			// Currently nowhere to output this information to the form anyway, so it
			// should always be null.
			if (bankAccount != null) {
				FormElementApplied.add
				 (form.getFormId(), bankAccount.getBankAccountId(), facade);
			}
			
			String fileName = FormUtils.getSpoolFileName
			 (form, entity, auroraClient);
			
			outputStream = createPSDFChangeOfDividendPaymentSpoolFile
			 (enrolment, form, fileName, entity, auroraClient, contact, person, 
			  account, bankAccount, userName, facade);
			
			// Generate a new file in the temporary location
			File spoolFile = FormUtils.createTempSpoolFile
			 (fileName, PSDFEnrolmentHandler.COMPANY_CODE, outputStream);
			
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
			
			// Add activity log to Client Services process, indicating form
			// was generated.
			
			enrolment.addActivity(personId, 
			 CampaignActivityType.FORM_CREATION_ACTIVITY_ID,
			 formType.getName() + " " + form.getFormId() + 
			 " created and dispatched for print", null, facade, userName); 
			
			return form;
			
		} catch (Exception e) {
			Log.error("Failed to generate/print " + formType.getName(), e);
			
			// Add activity log to Client Services process, indicating form
			// failed to generate.
			
			enrolment.addActivity(personId, 
			 CampaignActivityType.FORM_CREATION_ACTIVITY_ID,
			 "Failed to generate " + formType.getName(), null, facade, userName); 
			
			throw new ServiceException(e);
		}
		
	}
	
	/** Generates pre-filled PSDF Additional Withdrawal Bank Account form, for the given 
	 *  account.
	 *  
	 * @param processId
	 * @param facade
	 * @param userName
	 * @throws ServiceException
	 * @throws DataException
	 */
	public static Form createAdditionalBankAccountForm
	 (CampaignEnrolment enrolment, Account account, 
	 BankAccount bankAccount, BankAccount payAwayBankAccount,
	 boolean checkin, boolean print, FWFacade facade, String userName) 
	 throws ServiceException, DataException {
		
		// Determine form type
		FormType formType = FormType.getCache().getCachedInstance
		 (FormType.PSDF_ADDITIONAL_WITHDRAWAL_BANK_ACCOUNT);
		
		if (enrolment == null)
			throw new ServiceException
			 ("Unable to generate " + formType.getName() + ": " +
			 "passed CampaignEnrolment was null");
		
		int enrolmentId				= enrolment.getCampaignEnrolmentId();
		
		Integer orgId				= enrolment.getOrganisationId();
		Integer personId 			= enrolment.getPersonId();
		Integer contactId 			= enrolment.getContactId();
		
		Log.debug("Generating " + formType.getName() + " for Campaign Enrolment: " 
		 + enrolmentId + ", entity: " + orgId);
		
		// Fetch entity, with associated contact data
		Entity entity = Entity.get(orgId, true, facade);
		
		// Fetch person, with associated contact data
		Person person = Person.get(personId, true, facade);
		
		// Fetch assigned contact point
		Contact contact = Contact.get(contactId, facade);
		if (contact.getAddress() == null) {
			throw new ServiceException
			 ("Assigned Contact Point did not have an address set");
		} else {
			Log.debug("Assigned postal address for form: " + 
			 contact.getAddress().toString());
		}
		
		if (entity == null)
			throw new ServiceException("Unable to generate form: " +
			 "could not find associated organisation with ID: " + orgId);
		
		// Fetch AuroraClient mapping for this entity
		Company psicCompany = Company.getCache().getCachedInstance(Company.PSIC);
		AuroraClient auroraClient = Entity.getAuroraClientCompanyMapping
		 (orgId, psicCompany, facade);

		if (auroraClient == null)
			throw new ServiceException("Unable to generate form: " +
			 "organisation had no PSIC Aurora Client Number set");
		
		ByteArrayOutputStream outputStream = null;

		try {
			// Generate a new Form entry in the DB.
			Form form = Form.add(
				formType,
				enrolmentId,
				null,
				personId,
				null,
				entity.getOrganisationId(),
				null,
				facade,
				userName	
			);
			
			// Link the account to the generated form
			FormElementApplied.add
			 (form.getFormId(), account.getAccountId(), facade);
			
			// Link the bank account to the generated form, if it was supplied
			if (bankAccount != null) {
				FormElementApplied.add
				 (form.getFormId(), bankAccount.getBankAccountId(), facade);
			}
			
			// Link the pay-away bank account to the generated form, if it was supplied
			if (payAwayBankAccount != null) {
				FormElementApplied.add
				 (form.getFormId(), payAwayBankAccount.getBankAccountId(), facade);
			}
			
			String fileName = FormUtils.getSpoolFileName
			 (form, entity, auroraClient);
			
			outputStream = createPSDFAdditionalBankAccountSpoolFile
			 (enrolment, form, fileName, entity, auroraClient, contact, person, 
			  account, bankAccount, userName, facade);
			
			// Generate a new file in the temporary location
			File spoolFile = FormUtils.createTempSpoolFile
			 (fileName, PSDFEnrolmentHandler.COMPANY_CODE, outputStream);
			
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
			
			// Add activity log to Client Services process, indicating form
			// was generated.
			
			enrolment.addActivity(personId, 
			 CampaignActivityType.FORM_CREATION_ACTIVITY_ID,
			 formType.getName() + " " + form.getFormId() + 
			 " created and dispatched for print", null, facade, userName); 
			
			return form;
			
		} catch (Exception e) {
			Log.error("Failed to generate/print " + formType.getName(), e);
			
			// Add activity log to Client Services process, indicating form
			// failed to generate.
			
			enrolment.addActivity(personId, 
			 CampaignActivityType.FORM_CREATION_ACTIVITY_ID,
			 "Failed to generate " + formType.getName(), null, facade, userName); 
			
			throw new ServiceException(e);
		}
	}
	
	/** Generates pre-filled PSDF Additional Account opening form, for the given 
	 *  account.
	 * 
	 * @param processId
	 * @param facade
	 * @param userName
	 * @throws ServiceException
	 * @throws DataException
	 */
	public static Form createAdditionalAccountForm
	 (CampaignEnrolment enrolment, Account account,
	 boolean checkin, boolean print, FWFacade facade, String userName) 
	 throws ServiceException, DataException {
		
		// Determine form type
		FormType formType = FormType.getCache().getCachedInstance
		 (FormType.PSDF_ADDITIONAL_ACCOUNT);
		
		if (enrolment == null)
			throw new ServiceException
			 ("Unable to generate " + formType.getName() + ": " +
			 "passed CampaignEnrolment was null");
		
		int enrolmentId				= enrolment.getCampaignEnrolmentId();
		
		Integer orgId				= enrolment.getOrganisationId();
		Integer personId 			= enrolment.getPersonId();
		Integer contactId 			= enrolment.getContactId();
		
		Log.debug("Generating " + formType.getName() + " for Campaign Enrolment: " 
		 + enrolmentId + ", entity: " + orgId);
		
		// Fetch entity, with associated contact data
		Entity entity = Entity.get(orgId, true, facade);
		
		// Fetch person, with associated contact data
		Person person = Person.get(personId, true, facade);
		
		// Fetch assigned contact point
		Contact contact = Contact.get(contactId, facade);
		if (contact.getAddress() == null) {
			throw new ServiceException
			 ("Assigned Contact Point did not have an address set");
		} else {
			Log.debug("Assigned postal address for form: " + 
			 contact.getAddress().toString());
		}
		
		if (entity == null)
			throw new ServiceException("Unable to generate form: " +
			 "could not find associated organisation with ID: " + orgId);
		
		// Fetch AuroraClient mapping for this entity
		Company psicCompany = Company.getCache().getCachedInstance(Company.PSIC);
		AuroraClient auroraClient = Entity.getAuroraClientCompanyMapping
		 (orgId, psicCompany, facade);

		if (auroraClient == null)
			throw new ServiceException("Unable to generate form: " +
			 "organisation had no PSIC Aurora Client Number set");
		
		ByteArrayOutputStream outputStream = null;

		try {
			// Generate a new Form entry in the DB.
			Form form = Form.add(
				formType,
				enrolmentId,
				null,
				personId,
				null,
				entity.getOrganisationId(),
				null,
				facade,
				userName	
			);
			
			// Link the account to the generated form
			FormElementApplied.add
			 (form.getFormId(), account.getAccountId(), facade);
			
			String fileName = FormUtils.getSpoolFileName
			 (form, entity, auroraClient);
			
			outputStream = createPSDFAdditionalAccountSpoolFile
			 (enrolment, form, fileName, entity, auroraClient, contact, person, 
			  account, userName, facade);
			
			// Generate a new file in the temporary location
			File spoolFile = FormUtils.createTempSpoolFile
			 (fileName, PSDFEnrolmentHandler.COMPANY_CODE, outputStream);
			
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
			
			// Add activity log to Client Services process, indicating form
			// was generated.
			
			enrolment.addActivity(personId, 
			 CampaignActivityType.FORM_CREATION_ACTIVITY_ID,
			 formType.getName() + " " + form.getFormId() + 
			 " created and dispatched for print", null, facade, userName); 
			
			return form;
			
		} catch (Exception e) {
			Log.error("Failed to generate/print " + formType.getName(), e);
			
			// Add activity log to Client Services process, indicating form
			// failed to generate.
			
			enrolment.addActivity(personId, 
			 CampaignActivityType.FORM_CREATION_ACTIVITY_ID,
			 "Failed to generate " + formType.getName(), null, facade, userName); 
			
			throw new ServiceException(e);
		}
	}
	
	/** Generates the Create!Form spool file for a PSDF Reg form, mapped
	 *  to a single client/correspondent and a single PC account. 
	 *  
	 *  This is returned as a ByteArrayOutputStream.
	 *  
	 *  Most of the work here is extracting the necessary database records (e.g.
	 *  phone numbers, website addresses) so a PSDFRegistrationForm instance can be
	 *  generated.
	 *  
	 *  This is then handed off to the PSDFSpoolFileGenerator which will yield the
	 *  ByteArrayOutputStream.
	 *  
	 */
	public static ByteArrayOutputStream createPSDFRegistrationSpoolFile(
	 CampaignEnrolment enrolment, Form form, String fileName,
	 Entity entity, AuroraClient auroraClient, 
	 Contact contact,
	 Person correspondent, 
	 Account account, 
	 String userName, FWFacade facade)
	 throws ServiceException, DataException, IOException {
		
		Log.debug("Creating Spool File for form type: " + form.getFormType().getName() +
		 ", account: " + account.getExtendedAccountNumberString());
		
		Company psicCompany = Company.getCache().getCachedInstance(Company.PSIC);
		
		int clientId 	= auroraClient.getClientNumber();
		String clientIdStr = CCLAUtils.padClientNumber
		 (Integer.toString(clientId), psicCompany);
		
		boolean isLocalAuthority = 
		 form.getFormType().getFormTypeId() == (FormType.PSDF_LA);
		
		PSDFRegistrationForm regForm = getPSDFRegistrationForm
		 (form, isLocalAuthority, fileName, entity, auroraClient, contact, 
		 correspondent, account, userName, facade);
		
		String templatePath			= fileName;
		
		SpoolHeader spoolHeader 	= new SpoolHeader
		 (psicCompany.getCode(), clientIdStr, 
		 userName, 1, form.getFormId(), entity.getOrganisationId(), templatePath);

		PSDFRegistrationSpoolFileGenerator spoolFileGenerator = 
		 new PSDFRegistrationSpoolFileGenerator(
		  spoolHeader, fileName, enrolment.getCampaignEnrolmentId(), regForm);
				
		ByteArrayOutputStream outputStream = 
		 spoolFileGenerator.createSpoolFile();
		
		return outputStream;
	}
	
	/** Generates the Create!Form spool file for a PSDF Additional Account form, mapped
	 *  to a single client/correspondent and a single PC account.
	 *  
	 *  This is returned as a ByteArrayOutputStream.
	 *  
	 *  This is then handed off to the PSDFSpoolFileGenerator which will yield the
	 *  ByteArrayOutputStream.
	 *  
	 */
	private static ByteArrayOutputStream createPSDFAdditionalAccountSpoolFile(
	 CampaignEnrolment enrolment, Form form, String fileName,
	 Entity entity, AuroraClient auroraClient, 
	 Contact contact,
	 Person correspondent, 
	 Account account, 
	 String userName, FWFacade facade)
	 throws ServiceException, DataException, IOException {
		
		Log.debug("Creating Spool File for form type: " + form.getFormType().getName() +
		 ", account: " + account.getExtendedAccountNumberString());
		
		Company psicCompany = Company.getCache().getCachedInstance(Company.PSIC);
		
		int clientId 	= auroraClient.getClientNumber();
		String clientIdStr = CCLAUtils.padClientNumber
		 (Integer.toString(clientId), psicCompany);
		
		PSDFRegistrationForm regForm = getPSDFRegistrationForm
		 (form, false, fileName, entity, auroraClient, contact, correspondent, account, 
		 userName, facade);
		
		String templatePath			= fileName;
		
		SpoolHeader spoolHeader 	= new SpoolHeader
		 (psicCompany.getCode(), clientIdStr, 
		 userName, 1, form.getFormId(), entity.getOrganisationId(), templatePath);

		PSDFAdditionalAccountSpoolFileGenerator spoolFileGenerator = 
		 new PSDFAdditionalAccountSpoolFileGenerator(
		  spoolHeader, fileName, enrolment.getCampaignEnrolmentId(), regForm);
				
		ByteArrayOutputStream outputStream = 
		 spoolFileGenerator.createSpoolFile();
		
		return outputStream;
	}
	
	/** Generates the Create!Form spool file for a PSDF Change of Dividend Payment form, 
	 *  mapped to a single client/correspondent and a single PC account, and optionally
	 *  a specific Bank Account.
	 *  
	 *  This is returned as a ByteArrayOutputStream.
	 *  
	 *  This is then handed off to the PSDFSpoolFileGenerator which will yield the
	 *  ByteArrayOutputStream.
	 *  
	 */
	private static ByteArrayOutputStream createPSDFChangeOfDividendPaymentSpoolFile(
	 CampaignEnrolment enrolment, Form form, String fileName,
	 Entity entity, AuroraClient auroraClient, 
	 Contact contact,
	 Person correspondent, 
	 Account account,
	 BankAccount bankAccount,
	 String userName, FWFacade facade)
	 throws ServiceException, DataException, IOException {
		
		Log.debug("Creating Spool File for form type: " + form.getFormType().getName() +
		 ", account: " + account.getExtendedAccountNumberString());
		
		Company psicCompany = Company.getCache().getCachedInstance(Company.PSIC);
		
		int clientId 	= auroraClient.getClientNumber();
		String clientIdStr = CCLAUtils.padClientNumber
		 (Integer.toString(clientId), psicCompany);
		
		PSDFRegistrationForm regForm = getPSDFRegistrationForm
		 (form, false, fileName, entity, auroraClient, contact, correspondent, account, 
		 userName, facade);
		
		// Ensure the registration form instance is mapped to the specific passed
		// Bank Account, rather than the default/nominated bank account.
		regForm.setBankAccount(bankAccount);
		
		String templatePath			= fileName;
		
		SpoolHeader spoolHeader 	= new SpoolHeader
		 (psicCompany.getCode(), clientIdStr, 
		 userName, 1, form.getFormId(), entity.getOrganisationId(), templatePath);

		PSDFChangeOfDividendPaymentSpoolFileGenerator spoolFileGenerator = 
		 new PSDFChangeOfDividendPaymentSpoolFileGenerator(
		  spoolHeader, fileName, enrolment.getCampaignEnrolmentId(), regForm);
				
		ByteArrayOutputStream outputStream = 
		 spoolFileGenerator.createSpoolFile();
		
		return outputStream;
	}
	
	/** Generates the Create!Form spool file for a PSDF Change of Dividend Payment form, 
	 *  mapped to a single client/correspondent and a single PC account, and optionally
	 *  a specific Bank Account.
	 *  
	 *  This is returned as a ByteArrayOutputStream.
	 *  
	 *  This is then handed off to the PSDFSpoolFileGenerator which will yield the
	 *  ByteArrayOutputStream.
	 *  
	 */
	private static ByteArrayOutputStream createPSDFAdditionalBankAccountSpoolFile(
	 CampaignEnrolment enrolment, Form form, String fileName,
	 Entity entity, AuroraClient auroraClient, 
	 Contact contact,
	 Person correspondent, 
	 Account account,
	 BankAccount bankAccount,
	 String userName, FWFacade facade)
	 throws ServiceException, DataException, IOException {
		
		Log.debug("Creating Spool File for form type: " + form.getFormType().getName() +
		 ", account: " + account.getExtendedAccountNumberString());
		
		Company psicCompany = Company.getCache().getCachedInstance(Company.PSIC);
		
		int clientId 	= auroraClient.getClientNumber();
		String clientIdStr = CCLAUtils.padClientNumber
		 (Integer.toString(clientId), psicCompany);
		
		PSDFRegistrationForm regForm = getPSDFRegistrationForm
		 (form, false, fileName, entity, auroraClient, contact, correspondent, account, 
		 userName, facade);
		
		String templatePath			= fileName;
		
		SpoolHeader spoolHeader 	= new SpoolHeader
		 (psicCompany.getCode(), clientIdStr, 
		 userName, 1, form.getFormId(), entity.getOrganisationId(), templatePath);

		PSDFAdditionalBankAccountSpoolFileGenerator spoolFileGenerator = 
		 new PSDFAdditionalBankAccountSpoolFileGenerator(
		  spoolHeader, fileName, enrolment.getCampaignEnrolmentId(), regForm);
				
		ByteArrayOutputStream outputStream = 
		 spoolFileGenerator.createSpoolFile();
		
		return outputStream;
	}
	
	/** Constructs the Reg. Form wrapper object - contains all the data required to
	 *  generate a Reg/Additional Account spool file.
	 *  
	 *  Most of the work here is extracting the neccessary database records (e.g.
	 *  phone numbers, website addresses) so a PSDFRegistrationForm instance can be
	 *  generated.
	 *  
	 * @param form
	 * @param fileName
	 * @param entity
	 * @param auroraClient
	 * @param contact
	 * @param correspondent
	 * @param account
	 * @param userName
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	private static PSDFRegistrationForm getPSDFRegistrationForm
	 (Form form, boolean isLocalAuthority, String fileName,
	 Entity entity, AuroraClient auroraClient, 
	 Contact contact,
	 Person correspondent, 
	 Account account,
	 String userName, FWFacade facade) throws DataException {
		
		int clientId 	= auroraClient.getClientNumber();
		String company	= auroraClient.getCompany().getCode();
		
		Log.debug("Aurora Client: " + clientId + ", Company: " + company 
		 + ", Person ID: " + correspondent.getPersonId());

		// Fetch the mandated PI account. Throw error if it isn't set
		Integer mandatedAccountId = account.getMandatedAccId(); 
		
		if (mandatedAccountId == null)
			throw new DataException("Unable to generate form, PC account did " +
			 "not have mandated PI account set");
		
		Account mandatedAccount = Account.get(mandatedAccountId, facade);
		
		int formId = form.getFormId();
		
		// Get the Org Category tree
		Vector<String> orgCategoryTree = OrganisationCategory.getCategoryTree
		 (entity.getCategoryId(), facade);
		
		// Create HashMap to store ELEMENT_IDENTIFIER_ID -> Identifier Values.
		// This is used to store Charity Reg Numbers, HMRC Numbers etc.
		HashMap<Integer, String> orgIdentifiers = new HashMap<Integer, String>();
		
		// Fetch mapped Identifiers for Organisation
		Vector<ElementIdentifierApplied> identsApplied = 
		 ElementIdentifierApplied.getElementIdentifiersApplied
		 (entity.getOrganisationId(), facade);
		
		for (ElementIdentifierApplied eIdA : identsApplied)
			orgIdentifiers.put(eIdA.getIdentifierId(), eIdA.getIdentifierValue());
	
		RelationName withdrawalRelation = RelationName.getCache().getCachedInstance(
			RelationName.AccountBankAccountRelation.WITHDRAWAL
		);
		
		BankAccount nominatedBankAccount = null;
		
		// Grab first withdrawal bank account for the PC account
		DataResultSet pcBankAccounts = 
		 Relation.getRelatedBankAccountsWithRelationData
		 (account.getAccountId(), withdrawalRelation, facade);
		
		Log.debug("Found " + pcBankAccounts.getNumRows() + 
		 " related withdrawal Bank Accounts for PC account");
		
		if (pcBankAccounts.first()) {
			// First account will be the nominated one, if one of them is marked
			// as nominated
			nominatedBankAccount = BankAccount.get(pcBankAccounts);
		}
		
		BankAccount payAwayBankAccount = null;
		
		// Grab first withdrawal bank account for the PC account
		DataResultSet piBankAccounts = 
		 Relation.getRelatedBankAccountsWithRelationData
		 (mandatedAccount.getAccountId(), withdrawalRelation, facade);
		
		Log.debug("Found " + piBankAccounts.getNumRows() + 
		 " related withdrawal Bank Accounts for PI account");
		
		if (piBankAccounts.first()) {
			// First account will be the nominated one, if one of them is marked
			// as nominated
			payAwayBankAccount = BankAccount.get(piBankAccounts);
		}
		
		// Fetch RelationName -> Person mapping for account, with Contact details
		// pre-loaded.
		HashMap<RelationName, Vector<Person>> relatedAccountPersons = 
		 account.getRelatedPersons(true, facade);
		
		FWFacade ucmFacade = CCLAUtils.getFacade(false);
		boolean emailIndemnityReceived = 
		 AuroraClient.getNumClientDocsWithDocumentClass
		 (auroraClient, "EMAILINDEMNITY", ucmFacade) > 0;
		
		PSDFRegistrationForm regForm = new PSDFRegistrationForm
		 (formId, 
		 contact, correspondent, entity, auroraClient, orgIdentifiers, 
		 account, null, mandatedAccount, 
		 nominatedBankAccount, payAwayBankAccount, 
		 orgCategoryTree, relatedAccountPersons, emailIndemnityReceived,
		 isLocalAuthority);
		
		return regForm;
	}
	
	/** Generates PSDF Subscription, Redemption and cancellation forms for a PSDF
	 *  client.
	 *  
	 *  If the passed account param is null, the 3 forms are generated for all active
	 *  PC accounts belonging to the client.
	 *  
	 *  If account is non-null, a welcome pack is generated for this account only.
	 * 
	 * @throws ServiceException 
	 * @throws DataException 
	 * @throws IOException 
	 * 
	 */
	public static void createWelcomePack(CampaignEnrolment enrolment, 
	 Account account, boolean checkin, boolean print, 
	 FWFacade facade, String userName)
	 throws ServiceException, DataException {
		
		if (enrolment == null)
			throw new ServiceException(
			 "Unable to generate PSDF Welcome Pack forms: " +
			 "passed CampaignEnrolment was null");
		
		int enrolmentId				= enrolment.getCampaignEnrolmentId();
		
		Integer orgId				= enrolment.getOrganisationId();
		Integer personId 			= enrolment.getPersonId();
		Integer contactId 			= enrolment.getContactId();
		
		Log.debug("Generating PSDF Welcome Pack forms for Campaign Enrolment: " 
		 + enrolmentId + ", org ID: " + orgId);
		
		Vector<Account> accounts = null;
		
		if (account == null) {
			Log.debug("No Account specified, will generate welcome packs for every " +
			 "active PC account");
			
			// Fetch all PSDF accounts that are linked to the campaign fund code 
			// (ie 'PC'), organisation and are in 'PEND' status.
			// One welcome pack is created per account.
			DataResultSet rsAccounts = 
			 Account.getClientAccountsDataByStatusFund(orgId, 
			 Account.AccountStatus.PEND, Fund.FundCodes.PC.toString(), facade);		
			
			if (rsAccounts.isEmpty()) {
				String msg = "Unable to generate PSDF Welcome Pack: no active PC " +
				 "accounts for this client";
				
				Log.error(msg);
				throw new ServiceException(msg);
			}
			
			Log.debug("Found " + rsAccounts.getNumRows() +
			 " active " + PSDFEnrolmentHandler.PSDF_FUND_CODE + 
			 " accounts for Organisation");
			
		} else {
			accounts = new Vector<Account>();
			accounts.add(account);
		}
		
		// Fetch entity, with associated contact data
		Entity org = Entity.get(orgId, true, facade);
		
		// Fetch person, with associated contact data
		Person correspondent = Person.get(personId, true, facade);
		
		// Fetch assigned contact point
		Contact postalAddress = Contact.get(contactId, facade);
		
		if (postalAddress.getAddress() == null) {
			throw new ServiceException
			 ("Assigned Contact Point did not have an address set");
		} else {
			Log.debug("Assigned postal address for form: " + 
			 postalAddress.getAddress().toString());
		}
		
		if (org == null)
			throw new ServiceException("Unable to generate forms: " +
			 "could not find associated organisation with ID: " + orgId);
		
		// Fetch AuroraClient mapping for this entity
		Company psicCompany = Company.getCache().getCachedInstance(Company.PSIC);
		AuroraClient auroraClient = Entity.getAuroraClientCompanyMapping
		 (orgId, psicCompany, facade);

		if (auroraClient == null)
			throw new ServiceException("Unable to generate form: " +
			 "organisation had no PSIC Aurora Client Number set");
		
		Vector<FormType> welcomePackFormTypes = new Vector<FormType>();
		
		welcomePackFormTypes.add(FormType.getCache().getCachedInstance(
		 FormType.PSDF_SUBSCRIPTION)
		);
		welcomePackFormTypes.add(FormType.getCache().getCachedInstance(
		 FormType.PSDF_REDEMPTION)
		);
		welcomePackFormTypes.add(FormType.getCache().getCachedInstance(
		 FormType.PSDF_CANCELLATION)
		);
		
		Vector<Form> generatedForms = new Vector<Form>();
		
		for (Account acc : accounts) {
			// Fetch the mandated PI account. Throw error if it isn't set
			Integer mandatedAccountId = acc.getMandatedAccId(); 
			
			if (mandatedAccountId == null)
				throw new ServiceException("Unable to generate form, PC account did " +
				 "not have mandated PI account set");
			
			Account mandatedAccount = Account.get(acc.getMandatedAccId(), facade);
			
			// Fetch RelationName -> Person mapping for account, with Contact details
			// pre-loaded.
			HashMap<RelationName, Vector<Person>> relatedAccountPersons = 
			 acc.getRelatedPersons(true, facade);
			
			for (FormType formType : welcomePackFormTypes) {
							
				Form form = createWelcomePackForm
				 (enrolment, org, auroraClient, postalAddress, correspondent, 
				 formType, acc, mandatedAccount,
				 relatedAccountPersons, print, userName, facade);
				
				generatedForms.add(form);
			}
			
			// Add activity log to Client Services process, indicating welcome pack was
			// generated for the account.
			enrolment.addActivity(correspondent.getPersonId(), 
			 CampaignActivityType.FORM_CREATION_ACTIVITY_ID,
			 "Welcome pack generated for account " 
			 + acc.getExtendedAccountNumberString(), 
			 null, facade, userName); 
		}
	}
	
	/** Generates a single entry in the FORM table for one of the PSDF Welcome Pack
	 *  forms.
	 *  
	 * @param enrolment
	 * @param org
	 * @param auroraClient
	 * @param postalAddress
	 * @param correspondent
	 * @param formType
	 * @param account
	 * @param print
	 * @param userName
	 * @param facade
	 * @return
	 * @throws ServiceException
	 * @throws DataException
	 * @throws IOException
	 */
	public static Form createWelcomePackForm(
	 CampaignEnrolment enrolment,
	 Entity org, AuroraClient auroraClient, 
	 Contact postalAddress,
	 Person correspondent, 
	 FormType formType,
	 Account account,
	 Account mandatedAccount,
	 HashMap<RelationName, Vector<Person>> relatedAccountPersons,
	 boolean print,
	 String userName, FWFacade facade)
	 throws ServiceException, DataException {
		
		ByteArrayOutputStream outputStream = null;
		
		try {
			Log.debug("Adding form with Org ID: " + org.getOrganisationId());
			
			// Generate a new Form entry in the DB.
			Form form = Form.add(
				formType,
				(enrolment != null ? enrolment.getCampaignEnrolmentId() : null), 
				null,
				correspondent.getPersonId(),
				null,
				org.getOrganisationId(),
				null,
				facade,
				userName	
			);
			
			Log.debug("Generated form with Org ID: " + form.getOrganisationId());
			
			// Link the account to the generated form
			FormElementApplied.add
			 (form.getFormId(), account.getAccountId(), facade);
			
			String fileName = FormUtils.getSpoolFileName
			 (form, org, auroraClient);
			
			FWFacade ucmFacade = CCLAUtils.getFacade(false);
			boolean emailIndemnityReceived = 
			 AuroraClient.getNumClientDocsWithDocumentClass
			 (auroraClient, "EMAILINDEMNITY", ucmFacade) > 0;
			
			outputStream = createPSDFAccountSpoolFile
			 (enrolment, form, fileName, org, auroraClient, postalAddress, 
		     correspondent, account, mandatedAccount, 
		     relatedAccountPersons, emailIndemnityReceived, 
		     userName, facade);
			
			// Generate a new file in the temporary location
			File spoolFile = FormUtils.createTempSpoolFile
			 (fileName, PSDFEnrolmentHandler.COMPANY_CODE, outputStream);
			
			Log.debug("Generated temp spool file: " + spoolFile.getName());
			
			if (print) {
				Log.debug("Dispatching for print");
				
				FormUtils.printForm(spoolFile.getAbsolutePath());
				
				// Update form data to indicate it was printed
				form.setFormStatusId(Form.FormStatus.PRINTED.id);
				form.setDatePrinted(new Date());
				
			} else {
				// Set generated status
				form.setFormStatusId(Form.FormStatus.GENERATED.id);
			}
			
			form.persist(facade, userName);
			
			// Add activity log to Client Services process, indicating form
			// was generated.
			if (enrolment != null) {
				enrolment.addActivity(correspondent.getPersonId(), 
				 CampaignActivityType.FORM_CREATION_ACTIVITY_ID,
				 form.getFormType().getName() + " for account " + 
				 account.getExtendedAccountNumberString() +
				 " generated with ID " + form.getFormId() +
				 (print ? " and dispatched for print" : ""), 
				 null, facade, userName); 
			}
			
			return form;
			
		} catch (Exception e) {
			String msg = "Failed to generate/print Welcome Pack form for account: " +
			 account.getExtendedAccountNumberString();
			
			Log.error(msg, e);	
			throw new ServiceException(msg, e);
		}
	}
	
	/** Generates the Create!Form spool file for a PSDF account form, mapped
	 *  to a single client/correspondent and a single PC account they own. 
	 *  
	 *  This is returned as a ByteArrayOutputStream.
	 *  
	 *  Depending on the form type specified by the Form instance, this method will
	 *  create a spool file for a Subscription, Redemption or Cancellation form.
	 *  
	 *  Most of the work here is extracting the neccessary database records (e.g.
	 *  phone numbers, website addresses) so a PSDFAccountForm instance can be
	 *  generated.
	 *  
	 *  This is then handed off to the PSDFAccountSpoolFileGenerator which will yield 
	 *  the ByteArrayOutputStream.
	 *  
	 */
	public static ByteArrayOutputStream createPSDFAccountSpoolFile(
	 CampaignEnrolment enrolment, Form form, String fileName,
	 Entity org, AuroraClient auroraClient, 
	 Contact postalAddress,
	 Person correspondent, 
	 Account account,
	 Account mandatedAccount,
	 HashMap<RelationName, Vector<Person>> relatedAccountPersons,
	 boolean emailIndemnityReceived,
	 String userName, FWFacade facade)
	 throws ServiceException, DataException, IOException {
		
		Log.debug("Creating Spool File for form type: " + form.getFormType().getName());
		
		int clientId 	= auroraClient.getClientNumber();
		String company	= auroraClient.getCompany().getCode();
		
		Log.debug("Aurora Client: " + clientId + ", Company: " + company 
		 + ", Person ID: " + correspondent.getPersonId());
		
		AccountForm accountForm = new AccountForm(form, correspondent, 
		 postalAddress, org, account, null, mandatedAccount, 
		 relatedAccountPersons, emailIndemnityReceived);
		
		String templatePath			= fileName;
		
		SpoolHeader spoolHeader 	= new SpoolHeader
		 ("PSIC", CCLAUtils.padClientNumber(Integer.toString(clientId)), 
		 userName, 1, form.getFormId(), org.getOrganisationId(), templatePath);

		PSDFAccountSpoolFileGenerator spoolFileGenerator = 
		 new PSDFAccountSpoolFileGenerator(
		  spoolHeader, fileName, 
		  (enrolment != null ? enrolment.getCampaignEnrolmentId() : null), accountForm);
				
		ByteArrayOutputStream outputStream = 
		 spoolFileGenerator.createSpoolFile();
		
		return outputStream;
	}
}
