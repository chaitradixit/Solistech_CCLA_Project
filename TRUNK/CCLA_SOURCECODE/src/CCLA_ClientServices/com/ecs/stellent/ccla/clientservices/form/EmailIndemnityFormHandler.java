package com.ecs.stellent.ccla.clientservices.form;

import intradoc.common.ServiceException;
import intradoc.data.DataException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Vector;

import com.ecs.stellent.ccla.clientservices.campaign.CommunityFirstClientEnrolmentHandler;
import com.ecs.stellent.ccla.clientservices.spool.EmailIndemnitySpoolFileGenerator;
import com.ecs.ucm.ccla.Globals.Users;
import com.ecs.ucm.ccla.data.AuroraClient;
import com.ecs.ucm.ccla.data.AuthStatus;
import com.ecs.ucm.ccla.data.Company;
import com.ecs.ucm.ccla.data.Contact;
import com.ecs.ucm.ccla.data.Element;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.campaign.CampaignEnrolment;
import com.ecs.ucm.ccla.data.form.Form;
import com.ecs.ucm.ccla.data.form.FormContactApplied;
import com.ecs.ucm.ccla.data.form.FormHandler;
import com.ecs.ucm.ccla.data.form.FormType;
import com.ecs.ucm.ccla.data.form.FormUtils;
import com.ecs.ucm.ccla.data.form.spool.SpoolHeader;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.embedded.FWFacade;

public class EmailIndemnityFormHandler extends FormHandler {

	public EmailIndemnityFormHandler(Form form, String userId, FWFacade facade) {
		super(form, userId, facade);
	}

	public EmailIndemnityFormHandler(String userId, FWFacade facade) {
		super(userId, facade);
	}

	@Override
	public Form generateForm(FormType formType, Element element, Integer investmentId)
	 throws ServiceException, DataException {
		
		Entity org = Entity.get(element.getElementId(), facade);
		
		if (org == null)
			throw new DataException("Unable to generate " + formType.getName() + 
			 " form: no organisation found with ID " + element.getElementId());
		
		// Fetch the first mapped Aurora Client we can find. Company isn't important
		Vector<AuroraClient> auroraClients = 
		 Entity.getAuroraClientMapping(org.getOrganisationId(), facade);
		
		if (auroraClients.isEmpty())
			throw new ServiceException("No mapped Aurora Client Number");
		
		AuroraClient auroraClient = auroraClients.get(0);
		
		// No way to determine number of required signatories at Organisation level.
		// Default to max allowed (3)
		int numSignatories = 3;
		
		Vector<Contact> orgContacts = 
		 Contact.getElementContacts(org.getOrganisationId(), facade);
		
		Vector<Contact> orgEmailContacts = new Vector<Contact>();
		
		// Filter list of all contacts to yield a list of Email contacts that
		// are pending authorisation.
		for (Contact contact : orgContacts) {
			if (contact.getMethodId() == Contact.EMAIL
				&& contact.getAuthStatus().getAuthStatusId() 
				== AuthStatus.Ids.PENDING_AUTHORISATION) {
				orgEmailContacts.add(contact);
			}
		}
		
		ByteArrayOutputStream outputStream = null;
		
		try {
			// Generate a new Form entry in the DB.
			Form form = Form.add(
				formType,
				null, 
				null,
				null,
				null,
				org.getOrganisationId(),
				null,
				facade,
				this.userId	
			);
			
			// Link the contacts to the generated form
			
			for (Contact emailContact : orgEmailContacts) {
				FormContactApplied.add
				 (form.getFormId(), emailContact.getContactId(), facade);
			}
			
			String fileName = FormUtils.getSpoolFileName
			 (form, org, auroraClient);
			
			// A campaign parameter can be optionally specified
			String campaign = null;
			
			if (formType.getFormTypeId() ==
				FormType.EMAIL_INDEMNITY_COMMUNITY_FIRST) {
				campaign = CommunityFirstClientEnrolmentHandler.SPOOL_FILE_CAMPAIGN_NAME;
			}
			
			outputStream = createSpoolFile(null, form, fileName, org, auroraClient, 
			 orgEmailContacts, numSignatories, campaign, this.userId, this.facade);
			
			// Generate a new file in the temporary location
			File spoolFile = FormUtils.createTempSpoolFile
			 (fileName, auroraClient.getCompany().getCode(), outputStream);
			
			Log.debug("Generated temp spool file: " + spoolFile.getName());
			Log.debug("Dispatching for print");
			
			FormUtils.printForm(spoolFile.getAbsolutePath());
			
			// Update form data to indicate it was printed
			form.setFormStatusId(Form.FormStatus.PRINTED.id);
			form.setDatePrinted(new Date());
				
			form.persist(facade, userId);
			
			return form;
			
		} catch (Exception e) {
			String msg = "Failed to generate/print " + formType.getName() + 
			 " form for Organisation ID " + org.getOrganisationId();

			Log.error(msg, e);	
			throw new ServiceException(msg, e);
		}
	}
	
	public static ByteArrayOutputStream createSpoolFile(
	 CampaignEnrolment enrolment, Form form, String fileName,
	 Entity org, AuroraClient auroraClient, 
	 Vector<Contact> emailAddresses,
	 int numSignatories,
	 String campaign,
	 String userName, FWFacade facade)
	 throws ServiceException, DataException, IOException {
		
		Log.debug("Creating Spool File for form type: " + form.getFormType().getName());
		
		EmailIndemnityForm emailIndemnityForm = new EmailIndemnityForm
		 (form, org, auroraClient, emailAddresses, numSignatories, campaign);
		
		String templatePath			= fileName;
		
		if (campaign.equals
			(CommunityFirstClientEnrolmentHandler.SPOOL_FILE_CAMPAIGN_NAME)) {
			// Always force COIF for this campaign type
			auroraClient.setCompany(Company.getCache().getCachedInstance(Company.COIF));
		}
		
		SpoolHeader spoolHeader 	= new SpoolHeader
		 (auroraClient.getCompany().getCode(), 
		 auroraClient.getPaddedClientNumber(), 
		 userName, 1, form.getFormId(), org.getOrganisationId(), templatePath);

		EmailIndemnitySpoolFileGenerator spoolFileGenerator = 
		 new EmailIndemnitySpoolFileGenerator(spoolHeader, emailIndemnityForm);
		
		ByteArrayOutputStream outputStream = 
		 spoolFileGenerator.createSpoolFile();
		
		return outputStream;
	}

	@Override
	public void doPostValidateActions() throws DataException, ServiceException {
		super.doPostValidateActions();
		
		if (form.isValid()) {
			// Mark any email addresses linked to this Form ID as being 'authorised'
			Vector<Contact> contacts = Form.getContacts(this.form.getFormId(), facade);
			
			Log.debug("Found " + contacts.size() + " contacts mapped to " 
			 + form.getFormType().getName() + " form. Setting Auth Statuses to Authorised");
			
			for (Contact emailContact : contacts) {
				Log.debug("Updating email contact ID " + emailContact.getContactId());
				
				emailContact.setAuthStatus(
				 AuthStatus.getCache().getCachedInstance(AuthStatus.Ids.AUTHORISED)
				);
				
				emailContact.persist(facade, Users.System);
			}
		}
	}
}
