package com.ecs.stellent.ccla.clientservices;

import com.ecs.stellent.ccla.clientservices.EntityService.OrganisationDataConfig;
import com.ecs.stellent.ccla.clientservices.processhandler.ClientProcessHandler;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Address;
import com.ecs.ucm.ccla.data.Campaign;
import com.ecs.ucm.ccla.data.ClientProcess;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.Process;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.server.Service;

/** Service methods for dealing with Process data */
public class ProcessService extends Service {
	
	/** Fetches all data relevant to a given Process and adds to the
	 *  binder as ResultSets.
	 *  
	 *  This can include basic Entity/Person ResultSets.
	 * @throws ServiceException
	 * @throws DataException
	 */
	public void getProcess() throws ServiceException, DataException {
		String processId = m_binder.getLocal("PROCESS_ID");
		
		if (StringUtils.stringIsBlank(processId))
			throw new ServiceException("Unable to load process info: " +
			 "processId missing.");
		
		Log.debug("Fetching process by ID: " + processId);
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		DataResultSet rsProcess = Process.getData(
		 Integer.parseInt(processId), facade);
		
		if (rsProcess.isEmpty())
			throw new ServiceException("No process found with ID: "
			 + processId);
		
		m_binder.addResultSet("rsProcess", rsProcess);
			
		String campaignId	= rsProcess.getStringValueByName("CAMPAIGN_ID");
		Campaign campaign = Campaign.get(Integer.parseInt(campaignId), facade);
		
		CampaignHandler cHandler = 
		 new CampaignHandler(campaign, m_userData.m_name, facade);
		
		ClientProcessHandler processHandler = cHandler.createClientProcessHandler();
		ClientProcess process				= ClientProcess.get(rsProcess);

		// get if the contact address of the process is valid (ie has postcode and one of flat, housename, housenumber)
		// this is needed to determine if form can be sent out
		Integer contactAddress = process.getContactAddressId();
		if (contactAddress != null)
		{
			boolean isAddressValid = Address.addressDataValid(contactAddress, facade);
			 if (isAddressValid)
				 m_binder.putLocal("CONTACT_ADDRESS_VALID", "1");
		}
		
		DataResultSet rsActions = 
		 processHandler.getAvailableActionsResultSet(process);
		
		m_binder.addResultSet("rsActions", rsActions);
		
		String entityIdStr = rsProcess.getStringValueByName("ORGANISATION_ID");

		if (!StringUtils.stringIsBlank(entityIdStr)) {
			int entityId = (Integer.parseInt(entityIdStr));
			
			// Load entity info
			OrganisationDataConfig dataConfig = new OrganisationDataConfig
			 (true, true, true, true, false, false, false, false, true, false, true, false);
			
			EntityService.addEntityDataToBinder
			 (m_binder, entityId, facade, dataConfig, false);
		}
		
		String personIdStr	= rsProcess.getStringValueByName("PERSON_ID");
		
		if (!StringUtils.stringIsBlank(personIdStr)) {
			// Load person info (includes their address)
			DataResultSet rsPerson = Person.getData
			 (Integer.parseInt(personIdStr), facade);
			
			m_binder.addResultSet("rsProcessPerson", rsPerson);
		}
		
		// Add any process activities
		DataResultSet rsProcessActivities = 
		 ClientProcess.getActivityData(processId, facade);
		
		m_binder.addResultSet("rsActivities", rsProcessActivities);
		
		// Add any generated forms
		DataResultSet rsForms = 
		 ClientProcess.getFormData(process, facade);
		
		m_binder.addResultSet("rsProcessForms", rsForms);
	}
	
	/** Updates the recipient for a particular Client process.
	 * 
	 * @throws ServiceException 
	 * @throws DataException 
	 *  
	 */
	public void updateProcessRecipient() throws ServiceException, DataException {
		
		String processIdStr = m_binder.getLocal("PROCESS_ID");
		String personIdStr  = m_binder.getLocal("PERSON_ID");
		Integer contactAddressId = BinderUtils.getBinderIntegerValue(m_binder,"CONTACT_ADDRESS_ID");
		
		if (StringUtils.stringIsBlank(processIdStr))
			throw new ServiceException("Unable to update Process contact address, " +
			 "PROCESS_ID missing");
		
		int processId		= Integer.parseInt(processIdStr);
		Integer personId    = null;
		
		if (!StringUtils.stringIsBlank(personIdStr))
			personId = Integer.parseInt(personIdStr);
		
		FWFacade facade 		= CCLAUtils.getFacade(m_workspace, true);
		ClientProcess process	= ClientProcess.get(processId, facade);
	
		if (process == null)
			throw new ServiceException("Unable to update Process recipient, " +
			 "no Process found with ID " + processId);
		
		process.setPersonId(personId);
		process.setContactAddressId(contactAddressId);
		
		try {
			facade.beginTransaction();
			
			String processAction = 
			 "Recipient updated to default correspondent with addressId:" + contactAddressId;
			
			if (personId != null) {
				Person person		= Person.get(personId, facade);
				processAction 	 	= "Recipient updated to " + 
				 person.getFullName() + " with addressId " + contactAddressId;
			}
					
			// This will apply the Person ID update on the Process entry.
			process.addActivity(m_userData.m_name, 
			 personId, "Recipient Update", processAction, 
			 null, false, facade);
			
			facade.commitTransaction();
		} catch (Exception e) {
			facade.rollbackTransaction();
			throw new ServiceException("Failed to update process recipient", e);
		}
	}
	
	/**
	 * @UCM_Service CCLA_CS_GET_PROCESSES_BY_PERSON
	 * @throws ServiceException
	 * @throws DataException
	 */
	public void getProcessesByPerson() throws ServiceException, DataException{
		//5:qClientServices_GetProcessesByPerson:rsPersonProcesses::null
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		m_binder.addResultSet(
				"rsPersonProcesses", 
				facade.createResultSet("qClientServices_GetProcessesByPerson", m_binder)
		);
	}
	
	/**
	 * @UCM_Service CCLA_CS_GET_PROCESSES_BY_STATUS
	 * @throws ServiceException
	 * @throws DataException
	 */
	public void getProcessesByStatus() throws ServiceException, DataException{
		//5:qClientServices_GetProcessesByStatus:rsProcesses::null
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		m_binder.addResultSet(
				"rsProcesses", 
				facade.createResultSet("qClientServices_GetProcessesByStatus", m_binder)
		);
	}
	
	/**
	 * @UCM_Service CCLA_CS_UPDATE_PROCESS_STATUS
	 * @throws ServiceException
	 * @throws DataException
	 */
	public void updateProcessStatus() throws ServiceException, DataException{
		//2:qClientServices_UpdateProcessStatus:::null
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		facade.execute("qClientServices_UpdateProcessStatus", m_binder);
	}
}
