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

import com.ecs.stellent.ccla.clientservices.campaign.DioLoanEnrolmentHandler;
import com.ecs.stellent.ccla.clientservices.spool.DioLoanAccountSpoolFileGenerator;
import com.ecs.stellent.ccla.clientservices.spool.DioLoanRegistrationSpoolFileGenerator;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.AuroraClient;
import com.ecs.ucm.ccla.data.BankAccount;
import com.ecs.ucm.ccla.data.Company;
import com.ecs.ucm.ccla.data.Contact;
import com.ecs.ucm.ccla.data.Element;
import com.ecs.ucm.ccla.data.ElementAttribute;
import com.ecs.ucm.ccla.data.ElementAttributeApplied;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.Relation;
import com.ecs.ucm.ccla.data.RelationName;
import com.ecs.ucm.ccla.data.RelationType;
import com.ecs.ucm.ccla.data.campaign.CampaignActivityType;
import com.ecs.ucm.ccla.data.campaign.CampaignEnrolment;
import com.ecs.ucm.ccla.data.campaign.FundInvestmentIntention;
import com.ecs.ucm.ccla.data.form.Form;
import com.ecs.ucm.ccla.data.form.FormElementApplied;
import com.ecs.ucm.ccla.data.form.FormType;
import com.ecs.ucm.ccla.data.form.FormUtils;
import com.ecs.ucm.ccla.data.form.spool.SpoolHeader;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.embedded.FWFacade;

public class DioLoanFormUtils {
	
	/** Generates a single Dio. Loan Registration Form for the passed Account.
	 *  
	 *  This includes adding a new entry to the FORM table, linking the passed Account
	 *  to this form and optionally checking in/printing the form.
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
	 Account account,
	 FormType formType,
	 CampaignEnrolment enrolment, 
	 boolean checkin, boolean print,
	 FWFacade facade, String userName) throws DataException, ServiceException {
		
		// Fetch account attributes
		HashMap<Integer, ElementAttributeApplied> accountAttributes = 
		 ElementAttributeApplied.getMapping(
		 ElementAttributeApplied.getAll(account.getAccountId(), false, facade));
		
		int orgId 	= account.getOwnerOrganisationId(facade);
		
		// Fetch entity, with associated contact data
		Entity org 	= Entity.get(orgId, true, facade);
		
		// Fetch entity attributes
		HashMap<Integer, ElementAttributeApplied> orgAttributes = 
		 ElementAttributeApplied.getMapping(
		 ElementAttributeApplied.getAll(org.getOrganisationId(), false, facade));
		
		int personId = enrolment.getPersonId();
		
		// Fetch person, with associated contact data
		Person person = Person.get(personId, true, facade);
		
		// Fetch assigned contact point
		Contact contact = Contact.get(enrolment.getContactId(), facade);

		// Fetch AuroraClient CBF mapping for this entity
		Company cbfCompany = Company.getCache().getCachedInstance(Company.CBF);
		AuroraClient auroraClient = Entity.getAuroraClientCompanyMapping
		 (orgId, cbfCompany, facade);

		if (auroraClient == null)
			throw new ServiceException("Unable to generate form: " +
			 "organisation had no CBF Aurora Client Number set");

		Integer enrolmentId = 
		 enrolment != null ? enrolment.getCampaignEnrolmentId() : null;
		
		// Grab first withdrawal bank account for the account
		BankAccount nominatedBankAccount = null;
		 
		RelationName withdrawalRelation = RelationName.getCache().getCachedInstance(
		 RelationName.AccountBankAccountRelation.WITHDRAWAL
		);
				
		DataResultSet dioLoanBankAccounts = 
		 Relation.getRelatedBankAccountsWithRelationData
		 (account.getAccountId(), withdrawalRelation, facade);
		
		Log.debug("Found " + dioLoanBankAccounts.getNumRows() + 
		 " related withdrawal Bank Accounts for DioLoan account");
		
		if (dioLoanBankAccounts.first()) {
			// First account will be the nominated one, if one of them is marked
			// as nominated
			nominatedBankAccount = BankAccount.get(dioLoanBankAccounts);
		}
		
		if (nominatedBankAccount == null) {
			throw new ServiceException("Unable to generate form: " +
			 "no bank account specified");
		}
		
		HashMap<RelationName, Vector<Person>> relatedAccountPersons = 
		 account.getRelatedPersons(true, facade);
		
		FWFacade ucmFacade = CCLAUtils.getFacade(false);
		boolean emailIndemnityReceived = 
		 AuroraClient.getNumClientDocsWithDocumentClass
		 (auroraClient, "EMAILINDEMNITY", ucmFacade) > 0;
		
		// Determine whether this bank account has already been linked an existing
		// non-loan account.
		boolean requireBankStatement = true; 

		// Fetch all client bank accounts which are linked to the nominated bank
		// account.
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
			
			// Link the account to the generated form
			FormElementApplied.add
			 (form.getFormId(), account.getAccountId(), facade);
			
			String fileName = FormUtils.getSpoolFileName
			 (form, org, auroraClient);
			
			outputStream = createRegistrationSpoolFile(
			 form, 
			 fileName,
			 contact,
			 person,
			 org,
			 orgAttributes,
			 auroraClient,  
			 account,
			 accountAttributes,
			 nominatedBankAccount,
			 relatedAccountPersons,
			 emailIndemnityReceived,
			 requireBankStatement,
			 userName
			);
			
			// Generate a new file in the temporary location
			File spoolFile = FormUtils.createTempSpoolFile
			 (fileName, "CBF", outputStream);
			
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
	 AuroraClient auroraClient,
	 Account account,
	 HashMap<Integer, ElementAttributeApplied> accountAttributes,
	 BankAccount bankAccount,
	 HashMap<RelationName, Vector<Person>> relatedAccountPersons,
	 boolean emailIndemnityReceived,
	 boolean requireBankStatement,
	 String userName) throws IOException, DataException, ServiceException {
		
		Log.debug("Creating Spool File for form type: " 
		 + form.getFormType().getName() +
		 ", account: " + account.getAccountId());
		
		Company cbfCompany = Company.getCache().getCachedInstance(Company.CBF);
		String clientIdStr = auroraClient.getPaddedClientNumber();
		
		DioLoanRegistrationForm regForm = new DioLoanRegistrationForm(
		 form.getFormId(), 
		 contactAddress, 
		 correspondent, 
		 org, 
		 auroraClient, 
		 null, 
		 orgAttributes,
		 account, 
		 accountAttributes,
		 null, 
		 bankAccount, 
		 null, 
		 null, 
		 relatedAccountPersons, 
		 emailIndemnityReceived,
		 requireBankStatement
		);
		
		String templatePath			= fileName;
		
		SpoolHeader spoolHeader 	= new SpoolHeader
		 (cbfCompany.getCode(), clientIdStr, 
		 userName, 1, form.getFormId(), org.getOrganisationId(), templatePath);

		DioLoanRegistrationSpoolFileGenerator spoolFileGenerator = 
		 new DioLoanRegistrationSpoolFileGenerator(spoolHeader, regForm);
				
		ByteArrayOutputStream outputStream = 
		 spoolFileGenerator.createSpoolFile();
		
		return outputStream;
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
	public static Form createAccountForm(
	 CampaignEnrolment enrolment,
	 Entity org, AuroraClient auroraClient, 
	 Contact postalAddress,
	 Person correspondent, 
	 FormType formType,
	 Account account,
	 FundInvestmentIntention intention,
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
			
			outputStream = createDioLoanAccountSpoolFile
			 (enrolment, form, fileName, org, auroraClient, postalAddress, 
		     correspondent, account, intention, relatedAccountPersons, 
		     emailIndemnityReceived, userName, facade);
			
			// Generate a new file in the temporary location
			File spoolFile = FormUtils.createTempSpoolFile
			 (fileName, DioLoanEnrolmentHandler.COMPANY_CODE, outputStream);
			
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
			String msg = "Failed to generate/print form for account: " +
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
	public static ByteArrayOutputStream createDioLoanAccountSpoolFile(
	 CampaignEnrolment enrolment, Form form, String fileName,
	 Entity org, AuroraClient auroraClient, 
	 Contact postalAddress,
	 Person correspondent, 
	 Account account,
	 FundInvestmentIntention intention,
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
		 postalAddress, org, account, intention, null, 
		 relatedAccountPersons, emailIndemnityReceived);
		
		String templatePath			= fileName;
		
		SpoolHeader spoolHeader 	= new SpoolHeader
		 (DioLoanEnrolmentHandler.COMPANY_CODE, 
		 CCLAUtils.padClientNumber(Integer.toString(clientId)), 
		 userName, 1, form.getFormId(), org.getOrganisationId(), templatePath);

		DioLoanAccountSpoolFileGenerator spoolFileGenerator = 
		 new DioLoanAccountSpoolFileGenerator(
		  spoolHeader, fileName, 
		  (enrolment != null ? enrolment.getCampaignEnrolmentId() : null), accountForm);
				
		ByteArrayOutputStream outputStream = 
		 spoolFileGenerator.createSpoolFile();
		
		return outputStream;
	}
}
