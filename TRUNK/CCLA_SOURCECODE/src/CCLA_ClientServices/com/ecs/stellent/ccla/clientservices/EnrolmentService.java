package com.ecs.stellent.ccla.clientservices;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.server.Service;

import java.util.Vector;

import com.ecs.stellent.ccla.clientservices.EntityService.OrganisationDataConfig;
import com.ecs.stellent.ccla.clientservices.form.PSDFFormUtils;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.Address;
import com.ecs.ucm.ccla.data.BankAccount;
import com.ecs.ucm.ccla.data.Contact;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Note;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.SharedCols;
import com.ecs.ucm.ccla.data.campaign.ApplicableEnrolmentAttribute;
import com.ecs.ucm.ccla.data.campaign.Campaign;
import com.ecs.ucm.ccla.data.campaign.CampaignActivity;
import com.ecs.ucm.ccla.data.campaign.CampaignActivityType;
import com.ecs.ucm.ccla.data.campaign.CampaignEnrolment;
import com.ecs.ucm.ccla.data.campaign.CampaignEnrolmentAction;
import com.ecs.ucm.ccla.data.campaign.CampaignMessages;
import com.ecs.ucm.ccla.data.campaign.CampaignSubjectStatus;
import com.ecs.ucm.ccla.data.campaign.EnrolmentActionQueue;
import com.ecs.ucm.ccla.data.campaign.EnrolmentAttribute;
import com.ecs.ucm.ccla.data.campaign.EnrolmentAttributeApplied;
import com.ecs.ucm.ccla.data.campaign.IEnrolmentHandler;
import com.ecs.ucm.ccla.data.form.Form;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Handles user interactions with the CAMPAIGN_ENROLMENT table.
 * 
 * @author Tom
 *
 */
public class EnrolmentService extends Service {
	
	/** Fetches info for the given Enrolment ID and places it in the DataBinder.
	 *  
	 *  Designed as a replacement to ProcessService.getProcess()
	 *  
	 * @throws DataException 
	 */
	public void getEnrolmentInfo() throws ServiceException, DataException {
		
		Integer campaignEnrolmentId = CCLAUtils.getBinderIntegerValue
		 (m_binder, "CAMPAIGN_ENROLMENT_ID");
		
		if (campaignEnrolmentId == null) {
			throw new ServiceException("CAMPAIGN_ENROLMENT_ID missing");
		}
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);

		// Fetch Enrolment data
		DataResultSet rsCampaignEnrolment = 
		 CampaignEnrolment.getDataExtended(campaignEnrolmentId, facade);
		
		if (rsCampaignEnrolment==null || rsCampaignEnrolment.isEmpty()) 
			throw new ServiceException
			 ("Cannot find CampaignEnrolment with id "+campaignEnrolmentId);
		
		m_binder.addResultSet("rsCampaignEnrolment", rsCampaignEnrolment);

		// create a campaign enrolment object from the resultset
		CampaignEnrolment enrolment = CampaignEnrolment.get(rsCampaignEnrolment);
		
		Integer campaignEnrolmentStatusId = enrolment.getEnrolmentStatusId();
		Log.info("campaignEnrolmentStatusId: "+campaignEnrolmentStatusId);
		//if campaignEnrolmentStatusId empty throw an error
		if (campaignEnrolmentStatusId == null) {
			throw new ServiceException("campaignEnrolmentStatusId missing");
		}
		
		// Fetch the campaign data and add it to the binder	
		DataResultSet rsCampaign = Campaign.getData(enrolment.getCampaignId(), facade);
		
		if (rsCampaign==null || rsCampaign.isEmpty()) 
			throw new ServiceException
			 ("Cannot find Campaign with id " + enrolment.getCampaignId());
		
		Campaign c = Campaign.get(rsCampaign);
		m_binder.addResultSet("rsCampaign", rsCampaign);	
		
		//Fetch available actions for current status
		IEnrolmentHandler enrolmentHandler = c.getEnrolmentHandlerInstance
		 (m_userData.m_name, facade);
		
		m_binder.addResultSet
		 ("rsActions", enrolmentHandler.getAvailableActionsResultSet(enrolment));
		
		if (enrolment.getOrganisationId()!=null) {
			OrganisationDataConfig dataConfig = new OrganisationDataConfig
			 (true, true, true, true, true, false, false, false, true, false, true, false);
			
			EntityService.addEntityDataToBinder
			 (m_binder, enrolment.getOrganisationId(), facade, dataConfig, false);
		}

		// Fetch applicable Attributes for Campaign
		DataResultSet rsApplicableEnrolmentAttributes = 
		 ApplicableEnrolmentAttribute.getDataByCampaignId
		 (enrolment.getCampaignId(), facade);
		
		m_binder.addResultSet
		 ("rsApplicableEnrolmentAttributes", rsApplicableEnrolmentAttributes);
		
		// Fetch applied Attributes for Enrolment
		DataResultSet rsEnrolmentAttributesApplied = 
		 EnrolmentAttributeApplied.getDataByEnrolmentId
		 (enrolment.getCampaignEnrolmentId(), facade);
			
		m_binder.addResultSet
		 ("rsEnrolmentAttributesApplied", rsEnrolmentAttributesApplied);
			
		// Load person info (includes their address)
		if (enrolment.getPersonId() != null) {
			int personId = enrolment.getPersonId();
			Log.info("personId: "+ personId);

			DataResultSet rsPerson = Person.getData(personId, facade);
			m_binder.addResultSet("rsProcessPerson", rsPerson);
			
			if (enrolment.getContactId()!=null){
			Contact contact = Contact.get(enrolment.getContactId(), facade);		
			// check if the contact address of the enrolment is valid (ie has postcode 
			// and one of flat, housename, housenumber)
			// this is needed to determine if form can be sent out
				Integer contactAddress = contact.getAddressId();
				Log.info("contactAddress:" +contactAddress);
				if (contactAddress != null)
				{
					boolean isAddressValid = Address.addressDataValid(contactAddress, facade);
					 if (isAddressValid)
						 m_binder.putLocal("CONTACT_ADDRESS_VALID", "1");
				}
		
			}
		}
			
		// get CampaignActivities
		DataResultSet rsActivities = CampaignActivity.getActivityData(
				campaignEnrolmentId, facade);

		m_binder.addResultSet("rsCampaignActivities", rsActivities);

		// call query for intention statuses.
		DataResultSet rsCampaignSubjectStatuses = facade.createResultSet(
				CampaignSubjectStatus.GET_ALL_QUERY_NAME, new DataBinder());
		m_binder.addResultSet("rsCampaignSubjectStatuses",
				rsCampaignSubjectStatuses);

		// call query for related person addresses.
		DataBinder qBinder = new DataBinder();
		qBinder.putLocal("ORGANISATION_ID", enrolment.getOrganisationId()
				.toString());

		DataResultSet rsPersonAddresses = facade.createResultSet(
				"qClientServices_getRelatedPersonAddresses", qBinder);
		m_binder.addResultSet("rsPersonAddresses", rsPersonAddresses);

		// call query for related Organisation addresses.
		DataResultSet rsOrgAddresses = facade.createResultSet(
				"qClientServices_getOrganisationAddresses", qBinder);
		m_binder.addResultSet("rsOrgAddresses", rsOrgAddresses);

		// get forms
		DataResultSet rsForms = CampaignEnrolment
				.getFormData(enrolment, facade);
		m_binder.addResultSet("rsProcessForms", rsForms);
		
		// Finally, add Campaign/Enrolment-specific data to the binder.
		enrolmentHandler.addEnrolmentDataToBinder(enrolment, m_binder);
	}
	
	/**
	 * add select clients to campaign
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void addSelectedClientsToCampaign() throws DataException, ServiceException {
		
		Integer campaignId = BinderUtils.getBinderIntegerValue(m_binder, 
		 Campaign.Cols.CAMPAIGN_ID);
		
		if (campaignId==null)
			throw new ServiceException("CampaignId not specified");

		String orgListStr = m_binder.getLocal("entityList");
		if (StringUtils.stringIsBlank(orgListStr)) {
			throw new ServiceException("No entities specified, Cannot Enrol to campaign");
		}
		
		boolean exclude	= !StringUtils.stringIsBlank(m_binder.getLocal("exclude"));
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		String[] entityList = orgListStr.split(",");

		DataResultSet rsClients = new DataResultSet(
		 new String[] {"ORGANISATION_ID"});
		
		// Loop through split client string and build ResultSet
		for (int i=0; i<entityList.length; i++) {
			String entityId = entityList[i];
			Vector<String> v = new Vector<String>();
			v.add(entityId);	
			rsClients.addRow(v);
		}
		
		DataResultSet rsResults = 
			addOrgsToCampaign(campaignId, rsClients, m_userData.m_name, exclude, facade);
		
		m_binder.addResultSet("rsProcessResults", rsResults);
	}
	
	/**
	 * method to add a organisation to a campaign
	 * @param campaignId
	 * @param rsOrgs
	 * @param username
	 * @param exclude
	 * @param facade
	 * @return
	 * @throws DataException
	 * @throws ServiceException
	 */
	private DataResultSet addOrgsToCampaign(int campaignId, DataResultSet rsOrgs, 
	 String username, boolean exclude, FWFacade facade) throws DataException, ServiceException 
	{	
		Campaign campaign = Campaign.getCache().getCachedInstance(campaignId);
		if (campaign == null)
			throw new ServiceException("No campaign found with ID: " + campaignId);
		
		IEnrolmentHandler enrolmenthandler = campaign.getEnrolmentHandlerInstance
		 (m_userData.m_name, facade);
		
		Log.debug("Triggering new campaign processes for " 
		 + rsOrgs.getNumRows() + " clients");

		// Records outcome for each triggered process
		DataResultSet processResults = 
			new DataResultSet(
					new String[] {"ORGANISATION_ID", "ENROLLED", "FAIL_REASON"});
		
		int newProcessCount = 0;
		
		// Loop through all clients in the ResultSet and kick off 
		// a new enrolment for each one.
		do {
			boolean clientFailed = false;
			String failReason = null;
			
			String entityIdStr = rsOrgs.getStringValueByName("ORGANISATION_ID");
			
			if (StringUtils.stringIsBlank(entityIdStr)) {
				clientFailed = true;
				failReason = "Client Entity ID missing";
				continue;				
			}
			
			int entityId = Integer.parseInt(entityIdStr);
			Entity entity = Entity.get(entityId, facade);
			
			if (entity == null) {
				clientFailed = true;
				failReason = "Client did not exist";
				continue;
			}
			
			try {
				enrolmenthandler.addNewEnrolment(entityId, null, exclude);
			} catch (Exception e) {
				Log.error("Failed to create campaign Enrolment for Organisation:"+entityId, e);
				clientFailed = true;
				failReason = "Failed to create campaign Enrolment for Organisation "+entityId;
			}
			
			Vector<String> v = new Vector<String>();
			v.add(Integer.toString(entityId));
			
			if (!clientFailed) {
				newProcessCount++;
				v.add("Y");
				v.add("");
			} else {
				v.add("N");
				v.add(failReason);
			}			
			processResults.addRow(v);
			
		} while (rsOrgs.next());
		
		Log.debug("Successfully started " + newProcessCount + "/" 
		 + rsOrgs.getNumRows() + " new Campaign Enrolments");
		
		return processResults;
	}	
	
	/**
	 * Adds Organisations to Campaign using SQL fetch.
	 * 
	 * SQL results should contain ORGANISATION_ID column.
	 * 
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void addOrgsToCampaign() throws DataException, ServiceException 
	{
		Integer campaignId 	= BinderUtils.getBinderIntegerValue
		 (m_binder, Campaign.Cols.CAMPAIGN_ID);
		
		if (campaignId==null)
			throw new ServiceException("CampaignId not specified");
		
		String sqlQuery = m_binder.getLocal("queryText");			
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);		
		DataResultSet rsOrgs = facade.createResultSetSQL(sqlQuery);
		Log.debug("Adding clients to campaign: " + campaignId + " using SQL:\n" +
		 sqlQuery);
		
		DataResultSet triggeredClients =
		 addOrgsToCampaign(campaignId, rsOrgs, m_userData.m_name, false, facade);
		
		m_binder.putLocal("clientCount", Integer.toString(triggeredClients.getNumRows()));
		
		int started = 0;
		int failed = 0;
		
		if (!triggeredClients.isEmpty()) {
			do {
				String processStarted = 
				 triggeredClients.getStringValueByName("ENROLLED");
				
				if (processStarted.equals("Y"))
					started++;
				else
					failed++;
				
			} while (triggeredClients.next());
		}
		
		m_binder.putLocal("processStartedCount", Integer.toString(started));
		m_binder.putLocal("processFailedCount", Integer.toString(failed));		
	}
	
	
	public void addClientToCampaign() throws DataException, ServiceException {
		
		Integer campaignId 	= BinderUtils.getBinderIntegerValue
		 (m_binder, Campaign.Cols.CAMPAIGN_ID);
		
		if (campaignId==null)
			throw new ServiceException("CampaignId not specified");
		
		Integer organisationId = BinderUtils.getBinderIntegerValue(m_binder, SharedCols.ORG);
		if (organisationId==null) {
			throw new ServiceException("organisationId not specified");
		}
		
		Integer personId = BinderUtils.getBinderIntegerValue(m_binder, SharedCols.PERSON);

		
		Log.debug("Starting campaign ID " + campaignId + " for entity: " + organisationId);
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		// Fetch campaign instance first.
		Campaign campaign = Campaign.getCache().getCachedInstance(campaignId);
		
		if (campaign == null)
			throw new ServiceException
			 ("No campaign found with ID: " + campaignId);
		
		// Check if the client is already enrolled to this campaign.
		CampaignEnrolment enrolment = CampaignEnrolment.get(campaignId, organisationId, facade);
		
		if (enrolment != null) {
			// Client is already enrolled to this campaign.
			boolean useExisting = CCLAUtils.getBinderBoolValue(m_binder, "useExisting");
			
			if (!useExisting)
				throw new ServiceException("Organisation ID " + organisationId + 
				 " is already enrolled to Campaign ID " + campaignId); 
			
		} else {
			// Create new Client Process.
			IEnrolmentHandler enrolmentHandler = campaign.getEnrolmentHandlerInstance(m_userData.m_name, facade);
			try {
				enrolment = enrolmentHandler.addNewEnrolment(organisationId, personId, false);
			} catch (Exception e) {
				String msg = "Failed to create campaign Enrolment for Organisation ID:"
				 +organisationId;
				
				Log.error(msg, e);
				throw new ServiceException(msg, e);
			}
		}
		
		// Append process ID to RedirectUrl
		String redirectUrl = m_binder.getLocal("RedirectUrl");
		if (!StringUtils.stringIsBlank(redirectUrl)) {
			redirectUrl += enrolment.getCampaignEnrolmentId();
			
			m_binder.putLocal("RedirectUrl", redirectUrl);
		}
	}	
	
	/**
	 * Method that will apply a submitted action
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void applyEnrolmentAction() throws DataException, ServiceException {
		
		Integer campaignEnrolmentId = CCLAUtils.getBinderIntegerValue
		 (m_binder, "CAMPAIGN_ENROLMENT_ID");
		
		if (campaignEnrolmentId == null) {
			throw new ServiceException("CAMPAIGN_ENROLMENT_ID missing");
		}
		
		Integer actionId	=  CCLAUtils.getBinderIntegerValue(m_binder, "ACTION_ID");
		if (actionId == null)
			throw new ServiceException("ACTION_ID is missing");
		
		// Determine if this is a custom action
		boolean isCustom	= CCLAUtils.getBinderBoolValue(m_binder, "isCustomAction");
		
		String note			= m_binder.getLocal(Note.NOTE_CONTENT);
		
		FWFacade facade		= CCLAUtils.getFacade(m_workspace, true);
		CampaignEnrolment ce = CampaignEnrolment.get(campaignEnrolmentId, facade);
			
		// Fetch campaign instance first.
		Campaign campaign = Campaign.getCache().getCachedInstance(ce.getCampaignId());
		
		if (campaign == null)
			throw new ServiceException
			 ("No campaign found with ID: " + ce.getCampaignId());		
		
		IEnrolmentHandler enrolmentHandler = campaign.getEnrolmentHandlerInstance
		 (m_userData.m_name, facade);
		
		if (isCustom) {
			enrolmentHandler.applyCustomAction(ce, actionId, note, m_binder);
		} else {	
			// Lookup matching EnrolmentAction from the table cache
			CampaignEnrolmentAction enrolmentAction = 
			 CampaignEnrolmentAction.getCache().getCachedInstance(actionId);
			
			if (enrolmentAction == null)
				throw new ServiceException("Unknown enrolment action ID: " + actionId);
			
			enrolmentHandler.applyAction(ce, enrolmentAction, note);
		}
	}

	/** Updates the recipient for a particular campaign enrolment.
	 * 
	 * @throws ServiceException 
	 * @throws DataException 
	 *  
	 */
	public void updateCampaignEnrolmentRecipient() throws ServiceException, DataException {
		
		Integer campaignEnrolmentId = CCLAUtils.getBinderIntegerValue
		 (m_binder, CampaignEnrolment.CAMPAIGN_ENROLMENT_ID);
		
		if (campaignEnrolmentId == null) {
			throw new ServiceException("CAMPAIGN_ENROLMENT_ID missing");
		}
		
		Integer personId = CCLAUtils.getBinderIntegerValue
		 (m_binder, SharedCols.PERSON);	
		Integer contactId = BinderUtils.getBinderIntegerValue(m_binder,CampaignEnrolment.CONTACT_ID);
		Integer campaignSubjectStatus = BinderUtils.getBinderIntegerValue(m_binder,"CAMPAIGN_SUBJECT_STATUS");

		Log.info("updating enrolment correspondent details with:" +
				" personId:"+personId +
				" contactId: "+contactId + 
				" campaignSubjectStatus:"+campaignSubjectStatus);
		
		FWFacade facade 		= CCLAUtils.getFacade(m_workspace, true);
		
		//set person id, contact address id and subjectStatusId
		CampaignEnrolment ce = CampaignEnrolment.get(campaignEnrolmentId, facade);
		ce.setPersonId(personId);
		ce.setContactId(contactId);
		ce.setSubjectStatusId(campaignSubjectStatus);
		ce.persist(facade, m_userData.m_name);
		
		// now add activity description
		try {
			facade.beginTransaction();
			
			String enrolmentAction = 
			 "Recipient updated to default correspondent with addressId:" + contactId;
			
			if (personId != null) {
				Person person		= Person.get(personId, facade);
				enrolmentAction 	 	= "Recipient updated to " + 
				 person.getFullName() + " with addressId " + contactId;
			}
					
			// This will add the activity to the campaign activities.
			ce.addActivity(personId,
					CampaignActivityType.CORRESPONDENT_DETAILS_UPDATED_ID,
					CampaignMessages.CORRESPONDENT_DETAILS_UPDATED_MSG,
					null,
					facade,m_userData.m_name);
			
			facade.commitTransaction();
		} catch (Exception e) {
			facade.rollbackTransaction();
			throw new ServiceException("Failed to update enrolment recipient", e);
		}
	}
	
	/** Updates the set of all mapped Enrolment attributes for a given Enrolment ID.
	 * 
	 *  Will remove any previously-set attributes that are passed in with missing/null 
	 *  values.
	 * 
	 *  To update a single mapped Enrolment attribute, call updateEnrolmentAttribute()
	 *  instead.
	 * 
	 * @throws DataException 
	 * @throws ServiceException 
	 * 
	 */
	public void updateEnrolmentAttributes() throws DataException, ServiceException {
		
		Integer campaignEnrolmentId = CCLAUtils.getBinderIntegerValue
		 (m_binder, CampaignEnrolment.CAMPAIGN_ENROLMENT_ID);
		
		if(campaignEnrolmentId==null)
			throw new ServiceException("Campaign Enrolment Id is empty");
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		CampaignEnrolment enrolment = CampaignEnrolment.get
		 (campaignEnrolmentId, facade);
		
		if (enrolment == null)
			throw new ServiceException("No Enrolment found with ID: " 
			 + campaignEnrolmentId);
		
		// Fetch all applicable attributes for this Campaign ID.
		Vector<ApplicableEnrolmentAttribute> applAttribs = 
		 ApplicableEnrolmentAttribute.getCampaignCache().getCachedInstance
		 (enrolment.getCampaignId());
		
		// Fetch previously-applied attribute values for this Enrolment ID
		Vector<EnrolmentAttributeApplied> attribs = 
		 EnrolmentAttributeApplied.getByEnrolmentId(campaignEnrolmentId, facade);
		
		try {
			facade.beginTransaction();
			
			for (ApplicableEnrolmentAttribute applAttrib : applAttribs) {
				
				EnrolmentAttributeApplied thisAttrib = null;
				
				// Check for a previously-applied attribute
				for (EnrolmentAttributeApplied attrib : attribs) {
					if (attrib.getApplicableEnrolmentAttribute().getId()
						== applAttrib.getId())
						thisAttrib = attrib;
				}
				
				// Fetch value for this attribute from the binder.
				String newAttribValue = m_binder.getLocal
				 (EnrolmentAttributeApplied.Cols.VALUE + "_" + applAttrib.getId());
				
				if (!StringUtils.stringIsBlank(newAttribValue) 
					&& (thisAttrib == null)) {
					
					// Attrib value is specified and attribute doesn't already exist.
					// Add now
					thisAttrib = EnrolmentAttributeApplied.add
					 (applAttrib.getId(), campaignEnrolmentId, newAttribValue, 
					 facade, m_userData.m_name);
				
				} else if (!StringUtils.stringIsBlank(newAttribValue) 
					&& (thisAttrib != null)) {
					
					// Attrib value is specified and attribute already exists.
					// Update existing, providing the value changed.
					
					if (!newAttribValue.equals(thisAttrib.getValue())) {
						thisAttrib.setValue(newAttribValue);
						thisAttrib.persist(facade, m_userData.m_name);
					}
				} else if (StringUtils.stringIsBlank(newAttribValue) 
					&& thisAttrib != null) {
					
					// Attrib value is not specified and attribute already exists.
					// Remove existing
					EnrolmentAttributeApplied.remove
					 (thisAttrib.getId(), m_userData.m_name, facade);
				}
			}
			
			facade.commitTransaction();
			
		} catch (Exception e) {
			facade.rollbackTransaction();
			
			String msg = "Failed to update Enrolment Attributes: " + e.getMessage();
			Log.error(msg, e);
			throw new ServiceException(msg, e);	
		}
	}
	
	/** Adds/updates/removes a single Enrolment attribute.
	 * 
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void updateEnrolmentAttribute() throws DataException, ServiceException {
		
		Integer campaignEnrolmentId = CCLAUtils.getBinderIntegerValue
		 (m_binder, CampaignEnrolment.CAMPAIGN_ENROLMENT_ID);
		
		if(campaignEnrolmentId==null)
			throw new ServiceException("Campaign Enrolment Id is empty");
		
		Integer applEnrolmentAttrId = CCLAUtils.getBinderIntegerValue
		 (m_binder, ApplicableEnrolmentAttribute.Cols.ID);
		
		if(applEnrolmentAttrId==null)
			throw new ServiceException("Applicable Enrolment Id is empty");
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);	
		
		String newAttribValue 		= m_binder.getLocal
		 (EnrolmentAttributeApplied.Cols.VALUE);
		
		Log.debug("Updating Enrolment Attribute value, " + 
		 ApplicableEnrolmentAttribute.Cols.ID + "=" + applEnrolmentAttrId + ", " +
		 CampaignEnrolment.CAMPAIGN_ENROLMENT_ID + "=" + campaignEnrolmentId);
		
		CampaignEnrolment enrolment = CampaignEnrolment.get
		 (campaignEnrolmentId, facade);
		
		if (enrolment == null)
			throw new ServiceException("No Enrolment found with ID: " 
			 + campaignEnrolmentId);
		
		// Fetch previously-applied attribute values for this Enrolment ID
		Vector<EnrolmentAttributeApplied> attribs = 
		 EnrolmentAttributeApplied.getByEnrolmentId(campaignEnrolmentId, facade);
		
		// Determine whether this attribute is already set against this enrolment or
		// not.
		EnrolmentAttributeApplied currentAttribAppl = null;
		
		for (EnrolmentAttributeApplied attrib : attribs) {
			if (attrib.getApplicableEnrolmentAttribute().getId() == applEnrolmentAttrId)
				currentAttribAppl = attrib;
		}
		
		Log.debug("Current attribute value? " + 
		 ((currentAttribAppl != null) ? currentAttribAppl.getValue() : null));
		
		try {
			facade.beginTransaction();
			
			if (currentAttribAppl != null) {
				// Attribute already mapped, either update or remove it.
				
				if (StringUtils.stringIsBlank(newAttribValue)) {
					Log.debug("Removing mapped attribute value");
					
					// Value now empty - remove.
					EnrolmentAttributeApplied.remove
					 (currentAttribAppl.getId(), m_userData.m_name, facade);
					
				} else if (!newAttribValue.equals(currentAttribAppl.getValue())) {
					Log.debug("Updating mapped attribute value to: " + newAttribValue);
					
					currentAttribAppl.setValue(newAttribValue);
					currentAttribAppl.persist(facade, m_userData.m_name);
					
				} else {
					// Attribute value hasn't changed - do nothing.
					Log.debug("Mapped attribute value is unchanged, doing nothing");
				}
			
			} else {
				// Attribute isn't already mapped. Either add a new one, or do nothing
				// if the passed value is blank.
				
				if (StringUtils.stringIsBlank(newAttribValue)) {
				
				} else {
					// Attrib value is specified and attribute doesn't already exist.
					// Add now
					Log.debug("Adding new attribute value: " + newAttribValue);
					
					currentAttribAppl = EnrolmentAttributeApplied.add
					 (applEnrolmentAttrId, campaignEnrolmentId, newAttribValue, 
					 facade, m_userData.m_name);
				}
			}
			
			facade.commitTransaction();
		} catch (Exception e) {
			facade.rollbackTransaction();
			
			String msg = "Failed to update Enrolment Attribute: " + e.getMessage();
			Log.error(msg, e);
			throw new ServiceException(msg, e);	
		}
	}
	
	
	
	
	/** Adds a note to an existing process. */
	public void addNote() throws DataException, ServiceException {
		
		Integer campaignEnrolmentId = 
			CCLAUtils.getBinderIntegerValue(m_binder, CampaignEnrolment.CAMPAIGN_ENROLMENT_ID);

		if(campaignEnrolmentId==null)
			throw new ServiceException("Campaign Enrolment Id is empty");
		
		String note	= m_binder.getLocal(Note.NOTE_CONTENT);
		
		if (StringUtils.stringIsBlank(note))
			throw new ServiceException("Note body was empty");
		
		FWFacade facade	= CCLAUtils.getFacade(m_workspace, true);
		
		//set person id, contact address id and subjectStatusId
		CampaignEnrolment ce = CampaignEnrolment.get(campaignEnrolmentId, facade);
		
		try {
			facade.beginTransaction();
			ce.addActivity(ce.getPersonId(), CampaignActivityType.NOTE_ACTIVITY_ID, 
					CampaignMessages.NOTE_MSG, note, facade, m_userData.m_name);
			
			facade.commitTransaction();
		} catch (Exception e) {
			facade.rollbackTransaction();
			String msg = "Failed to add process note: " + e.getMessage();
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}
	
	
	public void getFormInfo() throws DataException, ServiceException {
		
		Integer formId = CCLAUtils.getBinderIntegerValue
		 (m_binder, SharedCols.FORM);
		
		if (formId == null)
			throw new ServiceException("formId parameter not found");
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		DataResultSet rsFormExtendedData = 
			Form.getExtendedData(formId, facade);
		
		if (rsFormExtendedData == null || rsFormExtendedData.isEmpty())
			throw new ServiceException("Form with ID " + 
			 formId + " not found");
		
		m_binder.addResultSet("rsForm", rsFormExtendedData);

		
//		DataResultSet rsAccounts = form.getAccountData(facade);
//		m_binder.addResultSet("rsFormAccounts", rsAccounts);
	}
	
	/**
	 * Creates a PSDF 'welcome pack' for the given enrolment's client.
	 * 
	 * If an ACCOUNT_ID is present in the binder, a single welcome pack is generated
	 * for that account. If it isn't present, a weclome pack is generated for every
	 * PC account at PEND status.
	 * 
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void generatePSDFWelcomePack() throws DataException, ServiceException {
		Integer enrolmentId = CCLAUtils.getBinderIntegerValue
		 (m_binder, CampaignEnrolment.CAMPAIGN_ENROLMENT_ID);
		
		Integer accountId = CCLAUtils.getBinderIntegerValue
		 (m_binder, SharedCols.ACCOUNT);
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		CampaignEnrolment enrolment = CampaignEnrolment.get(enrolmentId, facade);
		Account account 			= null;
		
		if (accountId != null)
			account = Account.get(accountId, facade);
		
		PSDFFormUtils.createWelcomePack
		 (enrolment, account, false, true, facade, m_userData.m_name);
	}
	
	/**
	 * Creates a PSDF 'Additional Account form' for the given account.
	 * 
	 * If an ACCOUNT_ID is present in the binder, a single welcome pack is generated
	 * for that account. If it isn't present, a weclome pack is generated for every
	 * PC account at PEND status.
	 * 
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void generatePSDFAdditionalAccountForm() 
	 throws DataException, ServiceException {
		
		Integer enrolmentId = CCLAUtils.getBinderIntegerValue
		 (m_binder, CampaignEnrolment.CAMPAIGN_ENROLMENT_ID);
		
		Integer accountId = CCLAUtils.getBinderIntegerValue
		 (m_binder, SharedCols.ACCOUNT);
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		CampaignEnrolment enrolment = CampaignEnrolment.get(enrolmentId, facade);
		Account account 			= null;
		
		if (accountId == null)
			throw new ServiceException("Must specify an ACCOUNT_ID to generate an " +
			 "Additional Account form.");
			
		account = Account.get(accountId, facade);
		
		PSDFFormUtils.createAdditionalAccountForm
		 (enrolment, account, false, true, facade, m_userData.m_name);
	}
	
	/**
	 * Creates a PSDF 'Additional Withdrawal Bank Account form' for the given account.
	 * 
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void generatePSDFAdditionalBankAccountForm() 
	 throws DataException, ServiceException {
		
		Integer enrolmentId = CCLAUtils.getBinderIntegerValue
		 (m_binder, CampaignEnrolment.CAMPAIGN_ENROLMENT_ID);
		
		Integer accountId = CCLAUtils.getBinderIntegerValue
		 (m_binder, SharedCols.ACCOUNT);
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		CampaignEnrolment enrolment = CampaignEnrolment.get(enrolmentId, facade);
		Account account 			= null;
		
		if (accountId == null)
			throw new ServiceException("Must specify an ACCOUNT_ID to generate an " +
			 "Additional Account form.");
			
		account = Account.get(accountId, facade);
		
		PSDFFormUtils.createAdditionalBankAccountForm
		 (enrolment, account, null, null, 
		 false, true, facade, m_userData.m_name);
	}
	
	/**
	 * Creates a PSDF 'Change of Dividend Payment form' for the given account.
	 * 
	 * If a BANK_ACCOUNT_ID is present in the binder, this will be the bank account that
	 * is printed on the form. If it isn't present, no bank account data will be printed
	 * 
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void generatePSDFChangeOfDividendPaymentForm()
	 throws DataException, ServiceException {
		
		Integer enrolmentId = CCLAUtils.getBinderIntegerValue
		 (m_binder, CampaignEnrolment.CAMPAIGN_ENROLMENT_ID);
		
		Integer accountId = CCLAUtils.getBinderIntegerValue
		 (m_binder, SharedCols.ACCOUNT);
		
		Integer bankAccountId = CCLAUtils.getBinderIntegerValue
		 (m_binder, SharedCols.BANK_ACCOUNT);
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace,true);
		
		CampaignEnrolment enrolment = CampaignEnrolment.get(enrolmentId, facade);
		Account account 			= null;
		
		BankAccount bankAccount = null;
		BankAccount payAwayBankAccount = null;
		
		if (bankAccountId != null)
			bankAccount = BankAccount.get(bankAccountId, facade);
		
		if (accountId == null)
			throw new ServiceException("Must specify an ACCOUNT_ID to generate a " +
			 "Change of Dividend Payment form.");
			
		account = Account.get(accountId, facade);
		
		PSDFFormUtils.createChangeOfDividendPaymentForm
		 (enrolment, account, bankAccount, payAwayBankAccount, 
		 false, true, facade, m_userData.m_name);
	}
	
	/** Service method for fetching extra data specific to enrolment attributes.
	 *  
	 *  Only fetches mapped Bank Account data for now.
	 *  
	 * @throws DataException 
	 * @throws ServiceException 
	 */
	public void getClientEnrolmentAttributeInfo() 
	 throws DataException, ServiceException {
		
		Integer campaignEnrolmentId = CCLAUtils.getBinderIntegerValue
		 (m_binder, "CAMPAIGN_ENROLMENT_ID");
		
		// Determine whether enrolment attributes should be reloaded.
		boolean isAjaxRefresh = CCLAUtils.getBinderBoolValue(m_binder, "isAjax");
		
		if (campaignEnrolmentId == null) {
			throw new ServiceException("CAMPAIGN_ENROLMENT_ID missing");
		}
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		// Fetch applied Attributes for Enrolment
		DataResultSet rsEnrolmentAttributesApplied = 
		 EnrolmentAttributeApplied.getDataByEnrolmentId
		 (campaignEnrolmentId, facade);
		
		if (isAjaxRefresh) {
			m_binder.addResultSet
			 ("rsEnrolmentAttributesApplied", rsEnrolmentAttributesApplied);
		}
		
		Vector<EnrolmentAttributeApplied> attribsAppl = 
		 EnrolmentAttributeApplied.getAll(rsEnrolmentAttributesApplied);
		
		for (EnrolmentAttributeApplied attribAppl : attribsAppl) {
			
			EnrolmentAttribute attrib = 
			 attribAppl.getApplicableEnrolmentAttribute().getAttribute();
			
			// Look for a mapped Bank Account ID. If found, load associated Bank Account
			// data into binder
			if (attrib.getId() == EnrolmentAttribute.Ids.BANK_ACCOUNT) {
				DataResultSet rsBankAccount = BankAccount.getData
				 (Integer.parseInt(attribAppl.getValue()), facade);
				
				m_binder.addResultSet("rsRelatedBankAccounts", rsBankAccount);
			}
		}
	}
	
	/** Applies the same Action to a group of CAMPAIGN_ENROLMENT_ID values, as matched
	 *  by a user-suppied query string
	 *  
	 * @throws DataException 
	 * @throws ServiceException 
	 */
	public void applyBulkEnrolmentAction() throws DataException, ServiceException {
		
		Integer actionId = CCLAUtils.getBinderIntegerValue
		 (m_binder, CampaignEnrolmentAction.ID);
		
		CampaignEnrolmentAction action = CampaignEnrolmentAction.getCache()
		 .getCachedInstance(actionId);
		
		String sql = m_binder.getLocal("queryText");
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		DataResultSet rsEnrolments = facade.createResultSetSQL(sql);
		
		Log.debug("Matched " + rsEnrolments.getNumRows() + 
		 " results from supplied query text: " + sql);
		
		if (rsEnrolments.first()) {
			do {
				int enrolmentId = CCLAUtils.getResultSetIntegerValue
				 (rsEnrolments, CampaignEnrolment.CAMPAIGN_ENROLMENT_ID);
				
				try {
					CampaignEnrolment enrolment = CampaignEnrolment.get
					 (enrolmentId, facade);
					
					Campaign campaign = Campaign.get(enrolment.getCampaignId(), facade);
					
					IEnrolmentHandler handler = 
					 campaign.getEnrolmentHandlerInstance(m_userData.m_name, facade);
					
					handler.applyAction(enrolment, action, null);
					
					Log.debug("Successfully applied action " + action.getName() + 
					 " to Enrolment ID " + enrolmentId);
					
				} catch (Exception e) {
					Log.error("Failed to execute action " + action.getName() +
					 " on Enrolment ID " + enrolmentId);
				}
				
			} while (rsEnrolments.next());
		}
	}
	
	/**
	 * @UCM_Service CCLA_CS_ENROLMENT_LISTING
	 * @throws ServiceException
	 * @throws DataException
	 */
	public void getEnrolmentListing() throws ServiceException, DataException{
		//5:qCore_GetAllCampaignSubjectStatus:rsCampaignSubjectStatuses::null
		//5:qCore_GetAllCampaignEnrolmentStatus:rsCampaignEnrolmentStatuses::null
		//5:qCore_GetAllCampaigns:rsCampaigns::null
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		m_binder.addResultSet(
				"rsCampaignSubjectStatuses", 
				facade.createResultSet("qCore_GetAllCampaignSubjectStatus", m_binder)
		);
		m_binder.addResultSet(
				"rsCampaignEnrolmentStatuses", 
				facade.createResultSet("qCore_GetAllCampaignEnrolmentStatus", m_binder)
		);
		m_binder.addResultSet(
				"rsCampaigns", 
				facade.createResultSet("qCore_GetAllCampaigns", m_binder)
		);
	}
}
