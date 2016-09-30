package com.ecs.stellent.ccla.clientservices;

import java.util.Vector;

import com.ecs.stellent.ccla.clientservices.processhandler.ClientProcessHandler;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Campaign;
import com.ecs.ucm.ccla.data.ClientProcess;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.server.Service;

/** @deprecated uses older Campaign schema. See EnrolmentService class for equivalent
 *  new services.
 * 
 * @author tm
 *
 */
public class CampaignService extends Service {
	
	/** Adds a single client to a given campaign.
	 * 
	 *  If the useExisting flag is set in the DataBinder, an initial check
	 *  is made to see if the client is already enrolled to the given
	 *  campaign. If so, the existing Process ID is taken and added to the
	 *  URL, without trying to create a new campaign process.
	 * 
	 * @throws ServiceException 
	 * @throws DataException 
	 * 
	 */
	public void addClientToCampaign() throws DataException, ServiceException {
		
		String campaignIdStr = m_binder.getLocal("CAMPAIGN_ID");
	
		String entityIdStr 	= m_binder.getLocal("ORGANISATION_ID");
		String personIdStr	= m_binder.getLocal("PERSON_ID");
		
		if (StringUtils.stringIsBlank(campaignIdStr)
			||
			StringUtils.stringIsBlank(entityIdStr))
			throw new ServiceException("Unable to start client campaign, " +
			 "Campaign ID/Entity ID missing");
		
		Log.debug("Starting campaign ID " + campaignIdStr + 
		 " for entity: " + entityIdStr);
		
		int entityId		= Integer.parseInt(entityIdStr);
		int campaignId		= Integer.parseInt(campaignIdStr);
		
		Integer personId	= null;
		if (!StringUtils.stringIsBlank(personIdStr))
			personId = Integer.parseInt(personIdStr);
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		// Fetch campaign instance first.
		Campaign campaign = Campaign.get(Integer.parseInt(campaignIdStr), facade);
		
		if (campaign == null)
			throw new ServiceException
			 ("No campaign found with ID: " + campaignIdStr);
		
		// Check if the client is already enrolled to this campaign.
		ClientProcess process = null;
		process = ClientProcess.get(entityId, campaignId, facade);
		
		if (process != null) {
			// Client is already enrolled to this campaign.
			boolean useExisting = CCLAUtils.getBinderBoolValue
			 (m_binder, "useExisting");
			
			if (!useExisting)
				throw new ServiceException("Entity ID " + entityId + 
				 " is already enrolled to Campaign ID " + campaignId); 
			
		} else {
			// Create new Client Process.
			CampaignHandler campaignHandler = 
			 new CampaignHandler(campaign, m_userData.m_name, facade);
			
			ClientProcessHandler processHandler = 
			 campaignHandler.createClientProcessHandler();
			
			process = processHandler.addNewProcess
			(entityId, personId, ClientProcessHandler.MANUAL_CREATION, 
			 processHandler.getInitialStatus(), false);
		}
		
		// Append process ID to RedirectUrl
		String redirectUrl = m_binder.getLocal("RedirectUrl");
		if (!StringUtils.stringIsBlank(redirectUrl)) {
			redirectUrl += process.getProcessId();
			
			m_binder.putLocal("RedirectUrl", redirectUrl);
		}
	}
	
	/** Adds a specific list of clients to a given campaign. 
	 *  
	 *  campaignId and clientList must be present in the binder.
	 *  clientList is expected to be a comma-separated list of valid
	 *  ORGANISATION_IDs.
	 *  
	 * @throws ServiceException 
	 * @throws DataException 
	 *  
	 */
	public void addSelectedClientsToCampaign() 
	 throws ServiceException, DataException {
		
		String campaignId 	= m_binder.getLocal("campaignId");
		
		// Comma-separated list of client IDs and their respective companies 
		// for campaign submission
		String entityListStr	= m_binder.getLocal("entityList");
		
		boolean exclude	= !StringUtils.stringIsBlank(
		 m_binder.getLocal("exclude"));
		
		if (StringUtils.stringIsBlank(campaignId) ||
			StringUtils.stringIsBlank(entityListStr)) {
			
			throw new ServiceException("Unable to add entities to campaign: " +
			 "campaignId or entityIdList is missing from binder.");
		}
		
		String userName = m_userData.m_name;
		
		String[] entityList 	= entityListStr.split(",");
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		DataResultSet rsClients = new DataResultSet(
		 new String[] {"ORGANISATION_ID"});
		
		// Loop through split client string and build ResultSet
		for (int i=0; i<entityList.length; i++) {
			String entityId 		= entityList[i];

			Vector<String> v = new Vector<String>();
			v.add(entityId);
			
			rsClients.addRow(v);
		}
		
		DataResultSet processResults = 
		 addClientsToCampaign(campaignId, rsClients, userName,
		 ClientProcessHandler.MANUAL_CREATION, exclude, facade);
	
		// Add results to binder
		m_binder.addResultSet("rsProcessResults", processResults);
		// Add flag to prevent potential infinite loops in calling page
		m_binder.putLocal("processStarted", "1");
	}
	
	/** 
	 *  @deprecated part of old Campaign schema.
	 *  
	 *  Adds a ResultSet of clients to a given campaign. 
	 * 
	 *  The ResultSet is expected to contain an ORGANISATION_ID column.
     *
	 * @return a DataResultSet containing the list of submitted
	 * 		   clients and whether or not they failed campaign
	 * 		   submission.
	 * @throws ServiceException 
	 * @throws DataException 
	 *  
	 */
	private DataResultSet addClientsToCampaign(String campaignId, 
	 DataResultSet rsClients, String userName, int createMethod, 
	 boolean exclude, FWFacade facade) 
	 throws ServiceException, DataException {
		
		Log.debug("Triggering new campaign processes for " 
		 + rsClients.getNumRows() + " clients");
		
		if (StringUtils.stringIsBlank(campaignId))
			throw new ServiceException("No campaignId was supplied, " +
			 "unable to add clients to campaign.");
		
		// Fetch campaign instance first.
		Campaign campaign = Campaign.get(Integer.parseInt(campaignId), facade);
		
		if (campaign == null)
			throw new ServiceException("No campaign found with ID: " + campaignId);
		
		CampaignHandler campaignHandler = 
		 new CampaignHandler(campaign, userName, facade);
		
		ClientProcessHandler processHandler = 
		 campaignHandler.createClientProcessHandler();
		
		// Records outcome for each triggered process
		DataResultSet processResults = new DataResultSet(
		 new String[] {"ORGANISATION_ID", "PROCESS_STARTED", "FAIL_REASON"});
		
		int newProcessCount = 0;
		
		// Loop through all clients in the ResultSet and kick off 
		// a new process for each one.
		do {
			boolean clientFailed 	= false;
			String failReason		= null;
			
			String entityIdStr		= rsClients.getStringValueByName("ORGANISATION_ID");
			
			if (StringUtils.stringIsBlank(entityIdStr)) {
				clientFailed = true;
				failReason = "Client Entity ID missing";
				continue;
				
			}
			
			int entityId			= Integer.parseInt(entityIdStr);
			Entity entity 			= Entity.get(entityId, facade);
			
			if (entity == null) {
				clientFailed = true;
				failReason = "Client did not exist";
				continue;
			}
			
			try {
				processHandler.addNewProcess
				(entityId, null, createMethod, processHandler.getInitialStatus(), exclude);
			} catch (Exception e) {
				Log.error("Failed to created campaign process", e);
				clientFailed 	= true;
				failReason 		= "Failed to create campaign process";
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
			
		} while (rsClients.next());
		
		Log.debug("Successfully started " + newProcessCount + "/" 
		 + rsClients.getNumRows() + " new client processes.");
		
		return processResults;
	}
	
	/** Adds a ResultSet of persons to a given campaign. 
	 * 
	 *  The ResultSet is expected to contain 1 columns:
	 *  -PERSON_ID
	 * 
	 * @return a DataResultSet containing the list of submitted
	 * 		   persons and whether or not they failed campaign
	 * 		   submission.
	 * @throws ServiceException 
	 * @throws DataException 
	 *  
	 */
	private DataResultSet addPersonsToCampaign(String campaignId, 
	 DataResultSet rsPersons, String userName, int createMethod, 
	 boolean exclude, FWFacade facade) 
	 throws ServiceException, DataException {
		
		Log.debug("Triggering new campaign processes for " 
		 + rsPersons.getNumRows() + " persons");
		
		if (StringUtils.stringIsBlank(campaignId))
			throw new ServiceException("No campaignId was supplied, " +
			 "unable to add persons to campaign.");
		
		// Fetch campaign instance first.
		Campaign campaign = Campaign.get(Integer.parseInt(campaignId), facade);
		
		if (campaign == null)
			throw new ServiceException("No campaign found with ID: " + campaignId);
		
		CampaignHandler campaignHandler = 
		 new CampaignHandler(campaign, userName, facade);
		
		PersonProcessHandler processHandler = 
		 campaignHandler.createPersonProcessHandler();
		
		// Records outcome for each triggered process
		DataResultSet processResults = new DataResultSet(
		 new String[] {"PERSON_ID", "PROCESS_STARTED", "FAIL_REASON"});
		
		int newProcessCount = 0;
		
		// Loop through all clients in the ResultSet and kick off 
		// a new process for each one.
		do {
			boolean personFailed 	= false;
			String failReason		= null;
			
			String personIdStr		= rsPersons.getStringValueByName("PERSON_ID");
			int personId			= Integer.parseInt(personIdStr);
			
			Person person 			= Person.get(personId, false, facade);
			
			if (person == null) {
				personFailed = true;
				failReason = "Person did not exist";
			
			} else {
				try {
					processHandler.addNewProcess
					(personId, createMethod, exclude);
				} catch (Exception e) {
					Log.error("Failed to created campaign process", e);
					personFailed 	= true;
					failReason 		= "Failed to create campaign process";
				}
			}
			
			Vector<String> v = new Vector<String>();
			v.add(personIdStr);
			
			if (!personFailed) {
				newProcessCount++;
				
				v.add("Y");
				v.add("");
			} else {
				v.add("N");
				v.add(failReason);
			}
			
			processResults.addRow(v);
		} while (rsPersons.next());
		
		Log.debug("Successfully started " + newProcessCount + "/" 
		 + rsPersons.getNumRows() + " new person processes.");
		
		return processResults;
	}
	
	/** 
	 *  @deprecated part of old Campaign schema.
	 * 
	 *  Service method for adding a set of clients to a given campaign,
	 *  as specified by a custom SQL query string. This service method
	 *  is dangerous and should be protected using the Admin switch on
	 *  any service which invokes it.
	 *  
	 *  A campaignId must also be present in the binder, this determines
	 *  which campaign will be initialized.
	 *  
	 *  
	 *  
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void addClientsToCampaign() throws DataException, ServiceException {
		String campaignId 	= m_binder.getLocal("campaignId");
		String sqlQuery 	= m_binder.getLocal("sqlQuery");
			
		FWFacade facade		= CCLAUtils.getFacade(m_workspace, true);
		
		DataResultSet rsClients = facade.createResultSetSQL(sqlQuery);
		
		Log.debug("Adding clients to campaign: " + campaignId + " using SQL:\n" +
		 sqlQuery);
		
		DataResultSet triggeredClients =
		 addClientsToCampaign(campaignId, rsClients, m_userData.m_name, 
		  ClientProcessHandler.AUTO_CREATION, false, facade);
		
		m_binder.putLocal("clientCount", Integer.toString(
		 triggeredClients.getNumRows()));
		
		int started = 0;
		int failed = 0;
		
		if (!triggeredClients.isEmpty()) {
			do {
				String processStarted = 
				 triggeredClients.getStringValueByName("PROCESS_STARTED");
				
				if (processStarted.equals("Y"))
					started++;
				else
					failed++;
				
			} while (triggeredClients.next());
		}
		
		m_binder.putLocal("processStartedCount", Integer.toString(started));
		m_binder.putLocal("processFailedCount", Integer.toString(failed));
		
	}
	
	/** Service method for adding a set of persons to a given campaign,
	 *  as specified by a custom SQL query string. This service method
	 *  is dangerous and should be protected using the Admin switch on
	 *  any service which invokes it.
	 *  
	 *  A campaignId must also be present in the binder, this determines
	 *  which campaign will be initialized.
	 *  
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void addPersonsToCampaign() throws DataException, ServiceException {
		String campaignId 	= m_binder.getLocal("campaignId");
		String sqlQuery 	= m_binder.getLocal("sqlQuery");
			
		FWFacade facade		= CCLAUtils.getFacade(m_workspace, true);
		
		DataResultSet rsClients = facade.createResultSetSQL(sqlQuery);
		
		Log.debug("Adding persons to campaign: " + campaignId + " using SQL:\n" +
		 sqlQuery);
		
		DataResultSet triggeredPersons =
		 addClientsToCampaign(campaignId, rsClients, m_userData.m_name, 
		  ClientProcessHandler.AUTO_CREATION, false, facade);
		
		m_binder.putLocal("personCount", Integer.toString(
		 triggeredPersons.getNumRows()));
		
		int started = 0;
		int failed = 0;
		
		if (!triggeredPersons.isEmpty()) {
			do {
				String processStarted = 
				 triggeredPersons.getStringValueByName("PROCESS_STARTED");
				
				if (processStarted.equals("Y"))
					started++;
				else
					failed++;
				
			} while (triggeredPersons.next());
		}
		
		m_binder.putLocal("processStartedCount", Integer.toString(started));
		m_binder.putLocal("processFailedCount", Integer.toString(failed));
		
	}
	
	public void applyProcessAction() throws DataException, ServiceException {
		
		Integer processId 	= CCLAUtils.getBinderIntegerValue
		 (m_binder, "PROCESS_ID");
		
		String action		= m_binder.getLocal("action");
		String note			= m_binder.getLocal("NOTE");
		
		FWFacade facade		= CCLAUtils.getFacade(m_workspace, true);
		
		ClientProcess process = ClientProcess.get(processId, facade);
		Campaign campaign	= Campaign.get(process.getCampaignId(), facade);
		
		CampaignHandler campaignHandler = 
		 new CampaignHandler(campaign, m_userData.m_name, facade);
		
		campaignHandler.createClientProcessHandler().applyAction
		 (process, action, note);
	}
	
	/** Adds a note to an existing process. */
	public void addNote() throws DataException, ServiceException {
		
		Integer processId 	= CCLAUtils.getBinderIntegerValue
		 (m_binder, "PROCESS_ID");

		String note			= m_binder.getLocal("NOTE");
		
		if (StringUtils.stringIsBlank(note))
			throw new ServiceException("Note body was empty");
		
		FWFacade facade			= CCLAUtils.getFacade(m_workspace, true);
		ClientProcess process 	= ClientProcess.get(processId, facade);
		
		try {
			facade.beginTransaction();
			
			process.addActivity(m_userData.m_name, null, "Note", null, 
			 note, false, facade);
		
			facade.commitTransaction();
		} catch (Exception e) {
			facade.rollbackTransaction();
			String msg = "Failed to add process note: " + e.getMessage();
			
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}	
	
	/**
	 * Add CCLA_CAMPAIGNS resultset to binder
	 * @throws ServiceException
	 * @throws DataException
	 */
	public void getCampaignResultSet() throws ServiceException, DataException {
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		DataBinder binder = new DataBinder();
		DataResultSet rsLegacyCampaign = 
			facade.createResultSet("qClientServices_GetCampaigns", binder);
		
		m_binder.addResultSet("rsLegacyCampaign", rsLegacyCampaign);
	}
}
