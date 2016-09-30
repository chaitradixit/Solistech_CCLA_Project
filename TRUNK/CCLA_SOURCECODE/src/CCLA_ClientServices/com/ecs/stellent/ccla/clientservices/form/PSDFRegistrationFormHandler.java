package com.ecs.stellent.ccla.clientservices.form;

import com.ecs.stellent.ccla.clientservices.campaign.PSDFEnrolmentHandler;
import com.ecs.ucm.ccla.Globals;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.AuroraClient;
import com.ecs.ucm.ccla.data.Company;
import com.ecs.ucm.ccla.data.Contact;
import com.ecs.ucm.ccla.data.Element;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Fund;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.campaign.CampaignActivityType;
import com.ecs.ucm.ccla.data.campaign.CampaignEnrolment;
import com.ecs.ucm.ccla.data.campaign.CampaignEnrolmentStatus;
import com.ecs.ucm.ccla.data.form.Form;
import com.ecs.ucm.ccla.data.form.FormElementApplied;
import com.ecs.ucm.ccla.data.form.FormHandler;
import com.ecs.ucm.ccla.data.form.FormType;
import com.ecs.ucm.ccla.data.form.FormUtils;
import com.ecs.utils.Log;

import intradoc.common.ServiceException;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.ecs.utils.stellent.embedded.FWFacade;

/** Used to handle pre-filled PSDF Public Sector Fund registration forms.
 *  
 *  
 **/
public class PSDFRegistrationFormHandler extends FormHandler {
	
	public PSDFRegistrationFormHandler(Form form, String userId, FWFacade facade) {
		Log.debug("Initialized PSDF Reg. Form Handler");
		
		this.form = form;
		this.userId = userId;
		this.facade = facade;
	}
	
	public PSDFRegistrationFormHandler(String userId, FWFacade facade) {
		Log.debug("Initialized PSDF Reg. Form Handler");

		this.userId = userId;
		this.facade = facade;
	}
	
	/** Actions to perform when the form is initially 
	 *  checked in.
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
				
				// Fetch the mandated PI account, update the status of that as well.
				if (account.getMandatedAccId() != null) {
					Account incomeAccount = Account.get
					 (account.getMandatedAccId(), facade);
					
					if (incomeAccount.getStatus() == Account.AccountStatus.TEMP) {
						Log.debug("Updating status of mandated account ID " + 
						 account.getAccountId() + " to OPEN");
						incomeAccount.setStatus(Account.AccountStatus.OPEN);
						incomeAccount.persist(facade, Globals.Users.System);
					}
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
			map.put("xCompany", "PSIC");
			map.put("xFund", PSDFEnrolmentHandler.PSDF_FUND_CODE);
			
		} else if (accounts.size() == 1) {
			// If this mandate form only listed one account, account meta-data fields 
			//can be pre-filled.
			Account account = accounts.get(0);

			String accNumberStr = account.getAccountNumberString();
			
			Fund fund = Fund.getCache().getCachedInstance(account.getFundCode());
			
			map.put("xAccountNumber", accNumberStr);
			map.put("xFund", account.getFundCode());
			map.put("xCompany", fund.getCompany().getCode());
			
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
		
		// Fetch corresponding PSIC account.
		Account account = Account.get(element.getElementId(), facade);
		
		if (account == null)
			throw new DataException("Unable to generate PSDF Reg. form: no account " + 
			 "found with ID " + element.getElementId());
		
		// Fail if fund code != 'PC'
		if (!account.getFundCode().equals(PSDFEnrolmentHandler.PSDF_FUND_CODE))
			throw new DataException("Unable to generate PSDF Reg. form, fund code " +
			 "was " + account.getFundCode() + " (expected: " + 
			 PSDFEnrolmentHandler.PSDF_FUND_CODE + ")");
		
		// Fetch data elements required to generate form
		Integer orgId = account.getOwnerOrganisationId(facade);
		Entity org = Entity.get(orgId, true, facade);
		
		// Check they are enrolled to PSIC campaign.
		CampaignEnrolment enrolment = CampaignEnrolment.get
		 (PSDFEnrolmentHandler.CAMPAIGN_ID, orgId, facade);

		if (enrolment == null)
			throw new DataException("Unable to generate PSDF Reg. form, client " +
			 "was not enrolled to PSDF campaign");
		
		Integer enrolmentId 	= enrolment.getCampaignEnrolmentId();
		Integer personId 		= enrolment.getPersonId();
		Integer contactId 		= enrolment.getContactId();
		
		if (personId == null)
			throw new DataException("Unable to generate PSDF Reg. form, no campaign" +
			 " correspondent selected");
			
		// Fetch campaign correspondent, with associated contact data
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
		
		Company psicCompany = Company.getCache().getCachedInstance(Company.PSIC);
		AuroraClient auroraClient = 
		 Entity.getAuroraClientCompanyMapping(orgId, psicCompany, facade);
	
		if (auroraClient == null) {
			throw new DataException("Unable to generate PSDF account form, no " +
			 "PSIC Client link set");
		}
		
		// Generate a new Form entry in the DB.
		Form form = Form.add(
			formType,
			enrolmentId,
			null,
			personId,
			null,
			org.getOrganisationId(),
			null,
			this.facade,
			this.userId	
		);
		
		// Link the account to the generated form
		FormElementApplied.add
		 (form.getFormId(), account.getAccountId(), this.facade);
		
		String fileName = FormUtils.getSpoolFileName
		 (form, org, auroraClient);
		try {
			ByteArrayOutputStream outputStream = PSDFFormUtils.createPSDFRegistrationSpoolFile
			 (enrolment, form, fileName, org, auroraClient, contact, person, 
			  account, this.userId, this.facade);
			
			// Generate a new file in the temporary location
			File spoolFile = FormUtils.createTempSpoolFile
			 (fileName, "PSIC", outputStream);
			
			// Update form data to indicate it was generated
			//form.setFormStatusId(Form.FormStatus.GENERATED.id);
			//form.setDateGenerated(new Date());
			
			Log.debug("Generated temp spool file: " + spoolFile.getName() + 
			 ". Dispatching for print.");
			
			boolean print = true;
			
			if (print) {
				FormUtils.printForm(spoolFile.getAbsolutePath());
				
				// Update form data to indicate it was printed
				form.setFormStatusId(Form.FormStatus.PRINTED.id);
				form.setDatePrinted(new Date());
			} else {
				// Set generated status
				form.setFormStatusId(Form.FormStatus.GENERATED.id);
			}
			
			form.persist(this.facade, this.userId);
			
			// Add activity log to Client Services process, indicating form
			// was generated.
			enrolment.addActivity(personId, 
			 CampaignActivityType.FORM_CREATION_ACTIVITY_ID,
			 "PSDF Registration form " + form.getFormId() + 
			 " created and dispatched for print", null, this.facade, this.userId); 	
			
		} catch (Exception e) {
			String msg = "Failed to generate PSDF Reg. spool file: "
					 + e.getMessage();
			Log.error(msg, e);
			throw new ServiceException(msg);
		}

		return form;
	}
}
