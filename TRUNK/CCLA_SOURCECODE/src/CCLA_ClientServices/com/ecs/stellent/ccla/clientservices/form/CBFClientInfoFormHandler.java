package com.ecs.stellent.ccla.clientservices.form;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;

import intradoc.common.ParseStringException;
import intradoc.common.ServiceException;
import intradoc.data.DataException;

import com.ecs.stellent.ccla.clientservices.campaign.CBFClientConfirmationEnrolmentHandler;
import com.ecs.stellent.ccla.clientservices.spool.CBFClientInfoSpoolFileGenerator;
import com.ecs.stellent.ccla.clientservices.spool.CommunityFirstClientInformationSpoolFileGenerator;
import com.ecs.stellent.spp.service.InstructionDocServices;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals.UCMFieldNames;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.AuroraClient;
import com.ecs.ucm.ccla.data.BankAccount;
import com.ecs.ucm.ccla.data.Company;
import com.ecs.ucm.ccla.data.Contact;
import com.ecs.ucm.ccla.data.DocumentClass;
import com.ecs.ucm.ccla.data.Element;
import com.ecs.ucm.ccla.data.ElementAttributeApplied;
import com.ecs.ucm.ccla.data.ElementIdentifierApplied;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Fund;
import com.ecs.ucm.ccla.data.OrganisationCategory;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.Relation;
import com.ecs.ucm.ccla.data.RelationName;
import com.ecs.ucm.ccla.data.RelationType;
import com.ecs.ucm.ccla.data.SystemConfigVar;
import com.ecs.ucm.ccla.data.campaign.ApplicableEnrolmentAttribute;
import com.ecs.ucm.ccla.data.campaign.CampaignActivityType;
import com.ecs.ucm.ccla.data.campaign.CampaignEnrolment;
import com.ecs.ucm.ccla.data.campaign.EnrolmentAttribute;
import com.ecs.ucm.ccla.data.campaign.EnrolmentAttributeApplied;
import com.ecs.ucm.ccla.data.form.Form;
import com.ecs.ucm.ccla.data.form.FormElementApplied;
import com.ecs.ucm.ccla.data.form.FormHandler;
import com.ecs.ucm.ccla.data.form.FormType;
import com.ecs.ucm.ccla.data.form.FormUtils;
import com.ecs.ucm.ccla.data.form.Form.FormStatus;
import com.ecs.ucm.ccla.data.form.spool.SpoolHeader;
import com.ecs.ucm.ccla.data.subscription.Subscription;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.DateFormatter;
import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Handles creation and return of CBF Client Information forms.
 * 
 *  For a given CBF Client, a subset of their accounts will be eligible for AML chasing.
 *  For this subset, a form must be generated for each set of these accounts with a
 *  common correspondent.
 *  
 *  So, each generated form is specific to a client AND correspondent, and lists 
 *  multiple accounts and bank accounts.
 *  
 * @author Tom
 *
 */
public class CBFClientInfoFormHandler extends FormHandler {
	
	/** Used when generating new form instances.
	 * 
	 * @param userId
	 * @param facade
	 */
	public CBFClientInfoFormHandler(String userId, FWFacade facade) {
		Log.debug("Initialized CBFClientInfoFormHandler");
		
		this.userId = userId;
		this.facade = facade;
	}
	
	public CBFClientInfoFormHandler(Form form, String userId, FWFacade facade) {
		Log.debug("Initialized CBFClientInfoFormHandler");
		
		this.form = form;
		this.userId = userId;
		this.facade = facade;
	}
	
	/** Due to there being multiple accounts per form/client, it isn't possible to use
	 *  this method.
	 * 
	 */
	public Form generateForm(FormType formType, Element element,
			Integer investmentId) throws ServiceException, DataException {
		
		throw new ServiceException(formType.getName() + 
		 " form cannot be generated in this way. Use the 'Generate Forms' " +
		 "enrolment action instead");
	}

	@Override
	public Form generateForm(FormType formType, Element element)
			throws ServiceException, DataException {
		return generateForm(formType, element, null);
	}
	
	@Override
	/** 
	 *  Create child-doc entries for each account listed on the Client Info form.
	 */
	public void doPostCheckinActions(int docId) throws Exception {
		super.doPostCheckinActions(docId);
		
		// Fetch list of accounts which were displayed on the form.
		Vector<Account> accounts = FormElementApplied.getFormAccounts
		 (this.form.getFormId(), ElementType.ACCOUNT, facade);
		
		Log.debug("Doing post-checkin for CBF Client Info form. Found " 
		 + accounts.size() + " associated accounts");
		
		if (accounts.isEmpty()) {
			Log.warn("No accounts associated with mandate form: " 
			 + form.getFormId());
		} else {
			if (accounts.size() == 1) {
				// Simple case: 1 account listed on Mandate form.
				// No child documents required
				
			} else {
				// Complex case: multiple accounts listed on Mandate form.
				// Add a pending child document entry for each listed account
				
				// Sort the accounts first, so they are generated in the same order
				// as they appear on the form!
				Collections.sort(accounts);
				
				// First, delete any existing child documents for this item
				LWDocument lwd = new LWDocument(docId, true);
				String docName = lwd.getDocName();
				
				FWFacade ucmFacade = CCLAUtils.getFacade(false);
				
				InstructionDocServices.deletePendingChildDocs(docName, ucmFacade);
				
				for (Account account:accounts) {
					// Determine account number string, i.e. the padded account
					// number and fund code.
					String accNumberStr = account.getAccountNumberString();
					
					Fund fund = Fund.getCache().getCachedInstance(account.getFundCode());
					
					AuroraClient auroraClient = Entity.getAuroraClientCompanyMapping
					 (form.getOrganisationId(), fund.getCompany(), facade);

					String clientNumStr = auroraClient != null 
					 ? auroraClient.getPaddedClientNumber() : null;
					
					InstructionDocServices.addPendingChildDoc(
					 lwd.getDocName(), DocumentClass.Classes.AUTOMAND, accNumberStr, null, 
					  clientNumStr, 
					 account.getFundCode(), null, ucmFacade, this.userId);
				}
			}
		}
	}

	@Override
	public void doPostValidateActions() throws DataException, ServiceException {
		// TODO Auto-generated method stub
		super.doPostValidateActions();
	}

	@Override
	public Hashtable<String, String> getDocMetaMapping() throws DataException {
		
		Hashtable<String, String> map = super.getDocMetaMapping();
		
		// Populate Account info, if applicable
		Vector<Account> accounts = FormElementApplied.getFormAccounts
		 (this.form.getFormId(), ElementType.ACCOUNT, facade);
		
		Log.debug("Doing getDocMetaMapping for CBF Client Info form. Found " 
		 + accounts.size() + " associated accounts");
		
		if (accounts.isEmpty()) {
			Log.warn("No accounts associated with mandate form: " 
			 + form.getFormId());
		} else if (accounts.size() == 1) {
			// Populate account number field
			String accNumberStr = accounts.get(0).getAccountNumberString();
			map.put(UCMFieldNames.AccountNumber, accNumberStr);
		} else {
			// If more than 1 mapped Account, change doc class to MULTIDOC.
			map.put(UCMFieldNames.DocClass, DocumentClass.Classes.MULTIDOC);
			map.put(UCMFieldNames.MultiDoc, "Yes");
		}
		
		return map;
	}

	/** Generates all required CBF Client Info forms for the given Organisation.
	 * 
	 * @param formType
	 * @param org
	 * @param print
	 * @param facade
	 * @param userName
	 * @return
	 * @throws DataException
	 * @throws ServiceException
	 * @throws IOException
	 */
	public static Vector<Form> generateForms
	 (FormType formType, Entity org, boolean print, FWFacade facade, String userName) 
	 throws DataException, ServiceException {
		Vector<Form> forms = new Vector<Form>();
		
		Log.debug("Generating CBF Client Info forms for org: " + 
		 org.getOrgAccountCode());
		
		// Fetch their CBF Client Info enrolment record
		CampaignEnrolment enrolment = CampaignEnrolment.get
		 (CBFClientConfirmationEnrolmentHandler.CAMPAIGN_ID, 
		 org.getOrganisationId(), facade);
		
		if (enrolment == null)
			throw new ServiceException("Unable to generate forms - organisation has " +
			 "not been enrolled to required campaign!");
		
		// Account fetching
		// ----------------
		// Accounts to print on the form will be a selected sub-set of the client's
		// CBF accounts.
		// The selection is stored as applied Enrolment Attributes.
		Vector<EnrolmentAttributeApplied> accountAttribs = 
		 EnrolmentAttributeApplied.getAllAttrByTypeAndEnrolmentId
		 (enrolment.getCampaignEnrolmentId(), 
		 ApplicableEnrolmentAttribute.Ids.CBF_CLIENT_CONF_ACCOUNT, facade);
		
		Log.debug("Found " + accountAttribs.size() + " selected accounts");
		
		if (accountAttribs.isEmpty())
			throw new ServiceException
			 ("No accounts have been selected for AML checking. Ensure that at least " +
			 "one account has been selected and saved on the Enrolment Info page.");

		Vector<Account> accounts = new Vector<Account>();
		
		for (EnrolmentAttributeApplied enrolmentAttrib : accountAttribs) {
			Integer accountId = Integer.parseInt(enrolmentAttrib.getValue());
			
			accounts.add(Account.get(accountId, facade));
		}
		
		// Build mapping of correspondent Person ID -> accounts
		HashMap<Integer, Vector<Account>> corrAccountMap =
		 Account.getCorrespondentAccountMap(accounts, facade);
		
		// Fetch CBF Aurora Client map
		Company cbfCompany = Company.getCache().getCachedInstance(Company.CBF);
		AuroraClient auroraClient = Entity.getAuroraClientCompanyMapping
		 (org.getOrganisationId(), cbfCompany, facade);
		
		if (auroraClient == null)
			throw new ServiceException("Unable to generate forms - organisation has " +
			 "has no CBF Aurora Client link!");
		
		// Org Details fetching
		// ---------------
		
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
		
		HashMap<Integer, EnrolmentAttributeApplied> enrolmentAttributes = 
			 EnrolmentAttributeApplied.getMapping(enrolmentAttribsApplied);
		
		// Extra Form Attributes
		// ---------------------
		
		// Build extraAttributes list.
		HashMap<String, Object> extraAttributes = new HashMap<String, Object>();
		
		// Add isConfirmation flag, depending on form type.
		Boolean isConfirmation = Boolean.FALSE;
		
		if (formType.getFormTypeId() == FormType.CBF_CLIENT_CONFIRMATION)
			isConfirmation = Boolean.TRUE;
		
		extraAttributes.put
		 (CBFClientInfoSpoolFileGenerator.Attributes.IS_CONFIRMATION, isConfirmation);
		
		// Check for an 'override' form deadline date in sys config vars. If one isn't
		// present, calculate one dynamically.
		SystemConfigVar overrideFormDeadlineDate = SystemConfigVar.getCache()
		 .getCachedInstance("CBFClientInfo_FormDeadlineDate");
		
		Date deadlineDate = null;
		
		if (overrideFormDeadlineDate != null 
			&& overrideFormDeadlineDate.getDateValue() != null) {
			deadlineDate = overrideFormDeadlineDate.getDateValue();
			Log.debug("Using overriden fixed form deadline date: " + deadlineDate);
		} else {
			// Add deadline date, depending on current date + 1 month, rounded up to last
			// day of the month.
			GregorianCalendar deadlineDateCal = new GregorianCalendar();
			deadlineDateCal.add(Calendar.MONTH, 2);
			deadlineDateCal.set(Calendar.DAY_OF_MONTH, 1);
			
			deadlineDate = deadlineDateCal.getTime();
			Log.debug("Calculated form deadline date as: " + deadlineDate);
		}
		
		extraAttributes.put
		 (CBFClientInfoSpoolFileGenerator.Attributes.DEADLINE_DATE, deadlineDate);
		
		boolean populatePersonSections = false;
		
		SystemConfigVar populatePersonSectionsVar = SystemConfigVar.getCache()
		 .getCachedInstance("CBFClientInfo_PopulatePersonSections");
		
		if (populatePersonSectionsVar != null 
			&& populatePersonSectionsVar.getBoolValue() != null)
			populatePersonSections = populatePersonSectionsVar.getBoolValue();
		
		// --------------
		
		// Now loop through each account correspondent, generating a separate form
		// for each.
		for (Integer corrId : corrAccountMap.keySet()) {
			
			Person corr = Person.get(corrId, true, facade);
			Vector<Account> corrAccounts = corrAccountMap.get(corrId);
			
			if (corrAccounts.size() == 0)
				continue;
			
			ByteArrayOutputStream outputStream = null;
			Form form = null;

			try {
				// Generate a new Form entry in the DB.
				form = Form.add(
					formType,
					enrolment.getCampaignEnrolmentId(),
					null,
					corrId,
					null,
					org.getOrganisationId(),
					null,
					facade,
					userName	
				);
				
				// Determine Correspondent postal address
				Contact contact = 
				 Contact.getDefaultContact(corr.getContacts(), Contact.ADDRESS);
				
				if (contact == null)
					throw new ServiceException("Account correspondent " 
					 + corr.getFullName() + " has no nominated address set");
				
				// Fetching Bank Accounts
				// ------------
				// Fetch all linked bank accounts.
				Vector<BankAccount> corrBankAccounts = new Vector<BankAccount>();
	
				Vector<Relation> corrBankAccountRelations = 
				 Relation.getAll(Relation.getMultipleRelationsData
				 (corrAccounts, new Vector<Element>(), 
				 RelationType.getCache().getCachedInstance
				  (RelationType.ACCOUNT_BANKACCOUNT), 
				 null, facade));
				
				HashSet<Integer> bankAccountIds = new HashSet<Integer>();
				
				// Build list of unique related Bank Account IDs
				for (Relation rel : corrBankAccountRelations) {
					if (!bankAccountIds.contains(rel.getElementId2()))
						bankAccountIds.add(rel.getElementId2());
				}
				
				// Fetch the related Bank Account instances from DB
				for (Integer bankAccountId : bankAccountIds)
					corrBankAccounts.add(BankAccount.get(bankAccountId, facade));
	
				// ------------
				
				// Fetching Persons to print on form
				// ------------
				// If the form should have names pre-populated, determine the names
				// that should be added now.
				HashMap<RelationName, Vector<Person>> commonRelPersons = null;
				
				// Fetch only related persons who have a common relation across ALL
				// accounts.
				if (populatePersonSections)
					commonRelPersons = 
					 Relation.getCommonPersonAccountRelations(corrAccounts, facade);
				
				// ------------
				
				// Form Attributes
				// ------------
				// Link the accounts to the generated form
				for (Account acc : corrAccounts)
					FormElementApplied.add(form.getFormId(), acc.getAccountId(), facade);
				
				// Link the bank accounts to the generated form
				for (BankAccount bankAccount : corrBankAccounts)
					FormElementApplied.add
					 (form.getFormId(), bankAccount.getBankAccountId(), facade);
				
				// ------------
				
				String fileName = FormUtils.getSpoolFileName
				 (form, org, auroraClient);
				
				outputStream = createInfoSpoolFile(
				 form, 
				 fileName,
				 contact,
				 corr,
				 org,
				 orgAttributes,
				 orgIdentifiers,
				 orgCategoryTree,
				 auroraClient,  
				 commonRelPersons,
				 false,
				 false,
				 enrolmentAttributes,
				 extraAttributes,
				 corrAccounts,
				 corrBankAccounts,
				 userName
				);
				
				// Generate a new file in the temporary location
				File spoolFile = FormUtils.createTempSpoolFile
				 (fileName, cbfCompany.getCode(), outputStream);
				
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
				enrolment.addActivity(corrId, 
				 CampaignActivityType.FORM_CREATION_ACTIVITY_ID,
				 formType.getName() + " form " + form.getFormId() + 
				 " for " + corr.getSalutation() + 
				 " created and dispatched for print", null, facade, userName);
				
			} catch (Exception e) {
				throw new ServiceException(e.getMessage(), e);
			}
		}
		
		return forms;
	}
	
	private static ByteArrayOutputStream createInfoSpoolFile(
	 Form form, 
	 String fileName,
	 Contact contactAddress,
	 Person correspondent,
	 Entity org,
	 HashMap<Integer, ElementAttributeApplied> orgAttributes,
	 HashMap<Integer, String> orgIdentifiers,
	 Vector<String> orgCategoryTree,
	 AuroraClient auroraClient,
	 HashMap<RelationName, Vector<Person>> commonRelPersons,
	 boolean emailIndemnityReceived,
	 boolean requireBankStatement,
	 HashMap<Integer, EnrolmentAttributeApplied> enrolmentAttributes,
	 HashMap<String, ? extends Object> extraAttributes,
	 Vector<Account> accounts,
	 Vector<BankAccount> bankAccounts,
	 String userName) throws IOException, DataException, ServiceException {
		
		Log.debug("Creating Spool File for form type: " 
		 + form.getFormType().getName() +
		 ", org: " + org.getOrganisationId());
		
		Company cbfCompany = Company.getCache().getCachedInstance(Company.CBF);
		String clientIdStr = auroraClient.getPaddedClientNumber();
		
		ClientInfoForm infoForm = new ClientInfoForm(
		 form.getFormId(), 
		 contactAddress, 
		 correspondent, 
		 org, 
		 auroraClient, 
		 orgIdentifiers, 
		 orgAttributes,
		 null,
		 null, 
		 orgCategoryTree, 
		 commonRelPersons, 
		 emailIndemnityReceived,
		 enrolmentAttributes,
		 extraAttributes,
		 accounts,
		 bankAccounts
		);
		
		String templatePath			= fileName;
		
		SpoolHeader spoolHeader 	= new SpoolHeader
		 (cbfCompany.getCode(), clientIdStr, 
		 userName, 1, form.getFormId(), org.getOrganisationId(), templatePath);

		CBFClientInfoSpoolFileGenerator spoolFileGenerator = 
		 new CBFClientInfoSpoolFileGenerator(spoolHeader, infoForm);
				
		ByteArrayOutputStream outputStream = 
		 spoolFileGenerator.createSpoolFile();
		
		return outputStream;
	}
}
