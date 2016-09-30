package com.ecs.stellent.ccla.clientservices;

import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import com.ecs.stellent.ccla.clientservices.EntityService.OrganisationDataConfig;
import com.ecs.stellent.spp.data.QueryJobData;
import com.ecs.stellent.spp.service.BundlePriorityServices;
import com.ecs.stellent.spp.service.SppIntegrationUtils;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.commproc.InteractionTranslator;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.AuroraClient;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Interaction;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.Relation;
import com.ecs.ucm.ccla.data.instruction.Instruction;
import com.ecs.ucm.ccla.data.instruction.InstructionDataApplied;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.data.Workspace;
import intradoc.server.Service;
import intradoc.shared.SharedObjects;

public class InteractionService extends Service {
	
	/** List of Query Cause IDs (sourced from SPP) which will trigger the client 
	 *  query boost behaviour after submitting an Interaction
	 */
	public static int[] CLIENT_BOOST_QUERY_CAUSE_IDS;
	
	static {
		// Load up the int array of Cause IDs from the env. var
		String clientBoostQueryCauseIds = SharedObjects.getEnvironmentValue(
		 "CCLA_CS_ClientBoostQueryCauseIds"
		);
		
		if (!StringUtils.stringIsBlank(clientBoostQueryCauseIds)) {
			String[] idStr = clientBoostQueryCauseIds.split(",");
			
			CLIENT_BOOST_QUERY_CAUSE_IDS = new int[idStr.length];
			
			for (int i=0; i<idStr.length; i++) {
				CLIENT_BOOST_QUERY_CAUSE_IDS[i] = Integer.parseInt(idStr[i]);
			}
		} else {
			CLIENT_BOOST_QUERY_CAUSE_IDS = null;
		}
	}
	
	/** Adds a new entry to the CCLA_INTERACTION table.
	 * 
	 *  Optionally creates an SPP Query job off the back of the interaction,
	 *  if the user ticked the 'Query' checkbox.
	 * 
	 * @throws DataException
	 * @throws ServiceException 
	 */
	public void addInteraction() throws DataException, ServiceException {
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		try {
			facade.beginTransaction();
			
			m_binder.putLocal("USER_ID", m_userData.m_name);
			Interaction interaction = Interaction.add(m_binder, facade);

			if (interaction.isQuery()) 
			{
				addInstructionForInteraction(interaction, m_binder,
				 m_userData.m_name, facade);
				// User has logged this interaction and chosen to kick off a 
				// Query job. Start a corresponding SPP CLIENTQUERY job using
				// details from the logged interaction.
				
				//If the query is mandate backlog, we need to boost the priority of 
				//the mandates for this client.				
				String causeIdStr = m_binder.getLocal("causeId");
				
				if (!StringUtils.stringIsBlank(causeIdStr)) {
					int causeId = Integer.parseInt(causeIdStr);
					applyBoostRule(interaction, causeId, m_userData.m_name, 
					 facade, m_workspace);
				}			
			}
			
			// update any links to this interaction if they have been added
			CCLAUtils.addQueryIntParamToBinder
			 (m_binder, "INTERACTION_ID", interaction.getInteractionId());
			
			InteractionGroupService.updateLinks(facade, m_binder, m_userData.m_name);
			
			// If complete interaction then need to close any selected related 
			// interactions as well also need to remove follow up info from interactions 
			// that are closed and set 'CLOSED_BY' column to be this interaction id
			if (interaction.getOutcomeId() == Interaction.InteractionOutcome.COMPLETED)
			{
				CCLAUtils.addQueryIntParamToBinder
				 (m_binder, "INTERACTION_ID", interaction.getInteractionId());
				
				InteractionService.closeLinks(facade, m_binder, m_userData.m_name);	
			}
			
			facade.commitTransaction();
					
			String redirectUrl = m_binder.getLocal("RedirectUrl");
			
			if (!StringUtils.stringIsBlank(redirectUrl)) {
				redirectUrl +=  Integer.toString(interaction.getInteractionId());
				m_binder.putLocal("RedirectUrl", redirectUrl);	
			}
			
			
		} catch (Exception e) {
			facade.rollbackTransaction();
			
			String msg = "Failed to add new interaction: " + e.toString();
			
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}
	
	/** Determines whether or not to apply a boost rule before applying one, if
	 *  applicable.
	 *  
	 *  TODO: Shouldn't need to pass Workspace here. BundlePriorityServices methods
	 *  need refactoring to use FWFacade param instead.
	 *  
	 * @param interaction
	 * @param causeId
	 * @param userName
	 * @param facade
	 * @param workspace
	 * @throws DataException
	 * @throws ServiceException
	 */
	private static void applyBoostRule
	 (Interaction interaction, Integer causeId, 
	 String userName, FWFacade facade, Workspace workspace) 
	 throws DataException, ServiceException {
		
		if (!BundlePriorityServices.BOOST_RULE_ENABLED)
			return;
		
		if (CLIENT_BOOST_QUERY_CAUSE_IDS == null) {
			Log.warn("Unable to apply Client Query Boost check, no Cause IDs found.");
			return;
		}
		
		if (causeId == null) {
			Log.warn("Unable to apply Client Query Boost check, Cause ID was missing");
			return;
		}
		
		if (interaction.getOrganisationId() == null) {
			Log.info("Unable to apply Client Query Boost check, no Organisation ID");
			return;
		}
		
		boolean boostCause = false;
		
		// Check if the passed Cause IDs matches one of the Boost ones.
		for (int thisCauseId : CLIENT_BOOST_QUERY_CAUSE_IDS) {
			if (thisCauseId == causeId)
				boostCause = true;
		}
		
		if (!boostCause) {
			Log.info("Cause ID " + causeId + " didn't match to any of the boost cause "+
			 "IDs. No boost rule will be applied");
			return;
		}
		
		Vector<AuroraClient> auroraClients = 
		 Entity.getAuroraClientMapping(interaction.getOrganisationId(), facade);
	
		if (auroraClients.size() > 0) {
			// Pick the first Aurora Client for now.
			AuroraClient auroraClient = auroraClients.get(0);
			
			Log.debug("Adding boost rule for Organisation ID: " 
			 + interaction.getOrganisationId() + ", Aurora Client ID: " 
			 + auroraClient.getClientNumber());
			
			// TODO: refactor methods to take FWFacade param
			if (BundlePriorityServices.addBoostRule
				(auroraClient.getPaddedClientNumber(), workspace)) {
				BundlePriorityServices.refreshBoostForClientQuery
				 (auroraClient.getPaddedClientNumber(), 
				 userName, workspace);
			}
			
		} else {
			Log.info("Cannot boost bundles, no Aurora Client for " +
			 "Organisation ID "+ interaction.getOrganisationId());
		}
	}
	
	/** 
	 *  Adds a new Instruction instance to the register.
	 *  
	 *  Currently, only a CLIENTQUERY instruction can be generated off the back of an
	 *  interaction.
	 *  
	 * @throws ServiceException 
	 * @throws ParseException 
	 **/
	private static void addInstructionForInteraction
	 (Interaction interaction, DataBinder binder, 
	  String userName, FWFacade facade) 
	 throws DataException, ServiceException, ParseException {
		
		Integer jobId = null;
		
		// Fetch the Aurora client number/company for this Entity.
		if (interaction.getOrganisationId() != null) {
			Vector<AuroraClient> auroraClients = 
			 Entity.getAuroraClientMapping
			  (interaction.getOrganisationId(), facade);
		
			if (auroraClients.size() > 0) {
				// Pick the first Aurora Client for now.
				AuroraClient auroraClient = auroraClients.get(0);
				
				String company 		= auroraClient.getCompany().getCode();
				String clientNumber	= auroraClient.getPaddedClientNumber();
				String fundCode		= null;
				String accountNumber = null;

				if (interaction.getAccountId() != null) {
					Account account = 
						Account.get(interaction.getAccountId(), facade);
					
					accountNumber = account.getAccountNumberString();
					fundCode = account.getFundCode();
				} else if (!StringUtils.stringIsBlank(interaction.getFundCode())) {
					fundCode = interaction.getFundCode();
				}
				
				if (SppIntegrationUtils.SET_JOB_ID) {
					//Set the Job ID for each job that starts in SPP
					jobId = CCLAUtils.getNextSppJobId(facade);
				}
				
				
				// Build the query job data object. This will contain all required
				// parameters for starting a query job.
				QueryJobData queryJobData = new QueryJobData();
				queryJobData.setAttributes(binder);
				
				queryJobData.createdBy = userName;
				
				queryJobData.date = new Date();

				queryJobData.company = company;
				queryJobData.clientNumber = clientNumber;
				queryJobData.accountNumber = accountNumber;
				queryJobData.fundCode = fundCode;
				
				queryJobData.ucmJobId = jobId;
				//This will add an instruction to the instruction register 
				//and the modules will submit the job to SPP 
				
				//This will translate the queryJobData into the instruction for processing
				InteractionTranslator translator = 
					new InteractionTranslator(
							interaction.getInteractionId(), 
							queryJobData, 
							true, 
							facade,
							userName);
				
				Instruction instr = translator.translate();
				
			} else {
				Log.error("Unable to trigger SPP Query job, no Aurora " +
				 "Client Numbers found for Entity ID: " + 
				 interaction.getOrganisationId());
			}
		} else {
			Log.error("Unable to trigger SPP Query job, no Entity " +
			 "ID specified with interaction.");
		}
	
		return;
	}
	
	public void updateInteraction()
	throws ServiceException, DataException
	{
		String interactionId = m_binder.getLocal("INTERACTION_ID");
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		Interaction interaction = Interaction.get(Integer.parseInt(interactionId), facade);
		interaction.setAttributes(m_binder);
		interaction.persist(facade,  m_userData.m_name);
		
	}
	
	public void getInteraction()
	throws DataException, ServiceException
	{
		// get interaction information
		String strInteraction = m_binder.getLocal("INTERACTION_ID");
		
		if (!StringUtils.stringIsBlank(strInteraction))
		{
			FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
			int interactionId = Integer.parseInt(strInteraction);
			Interaction interaction = Interaction.get(interactionId, facade);

			if (interaction != null)
			{
				interaction.addFieldsToBinder(m_binder);
				// get the note if any exist
				DataResultSet rsInteractionNotes = interaction.getNotesData(interactionId, facade);
				if (!rsInteractionNotes.isEmpty())
					m_binder.addResultSet("rsInteractionNotes", rsInteractionNotes);
			
				//Add additional params to binder
				addExtraDataToBinder(interactionId, m_binder, facade);	
			}
		}
		// get the person and organisation info
		this.getInteractionSubjectInfo();

		
	
	}
	
	/** Used to load basic information associated with the passed Entity ID
	 *  and Person ID. One or both pieces of data can be missing from the
	 *  binder.
	 *  
	 * @throws DataException 
	 * @throws ServiceException 
	 *  
	 */
	public void getInteractionSubjectInfo() 
	 throws DataException, ServiceException {
		
		String entityIdStr = m_binder.getLocal("ORGANISATION_ID");
		String personIdStr	= m_binder.getLocal("PERSON_ID");
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		// Fetch Entity data, if applicable
		if (!StringUtils.stringIsBlank(entityIdStr)) {
			int entityId = Integer.parseInt(entityIdStr);
			
			OrganisationDataConfig dataConfig = new OrganisationDataConfig
			 (true, true, true, true, true, true, false, false, true, false, true, false);
			
			EntityService.
			 addEntityDataToBinder(m_binder, entityId, facade, dataConfig, false);

		} else if(!StringUtils.stringIsBlank(personIdStr))
		{
			int personId = Integer.parseInt(personIdStr);
			
			DataResultSet rsAccounts = Relation.getRelatedAccountsData
			 (personId, ElementType.PERSON, facade);

			m_binder.addResultSet("rsAccounts", rsAccounts);	
		}
		
		// Fetch Person data, if applicable
		if (!StringUtils.stringIsBlank(personIdStr)) {
			int personId = Integer.parseInt(personIdStr);
			
			PersonService.addPersonDataToBinder(personId, facade, m_binder);
		}
		
		//Add additional params to binder
		addExtraDataToBinder(null, m_binder, facade);	
	}
	
	
	
	public static void closeLinks(FWFacade facade, DataBinder binder, String userId) throws DataException, ServiceException
	{
		// get all possible links (on personId and organisationId)
		// and figure out if the checkbox is checked or not
		String pID = binder.getLocal("PERSON_ID");
		String oID = binder.getLocal("ORGANISATION_ID");
		if (( StringUtils.stringIsBlank(pID)))
			throw new ServiceException("MISSING PERSON ID");
		String queryName = "qClientServices_getInteractionsByPersonOrg";
		if (StringUtils.stringIsBlank(oID))
			queryName = "qClientServices_getInteractionsByPerson";

		DataResultSet rsInteractions = facade.createResultSet(queryName, binder);
		String existingInteraction = binder.getLocal("INTERACTION_ID");
		if (StringUtils.stringIsBlank(existingInteraction))
			throw new ServiceException("Missing interaction id for closure");
		int interactionId = Integer.parseInt(existingInteraction);
		if (!rsInteractions.isEmpty())
		{
	
			HashSet<Integer> closeList = new HashSet<Integer>();
			do {
				// get the InteractionGroup
				String closeId = rsInteractions.getStringValueByName("INTERACTION_ID");
				// skip over if interaction ids match
				if (!closeId.equalsIgnoreCase(existingInteraction))
				{
				boolean addLink = CCLAUtils.getBinderBoolValue(binder, "chk_doLink_" + closeId);
				int interactionCloseId = Integer.parseInt(closeId);
				Log.debug("close " + closeId  + " is "+ addLink);
				if (addLink)
				{
						closeList.add(interactionCloseId);
				} 

				}
				
			} while (rsInteractions.next());	

			if (!closeList.isEmpty())
			{
				// remove link
				closeInteractions(closeList, interactionId, userId, facade);	
			}		
		}	
	}
	
	public static void closeInteractions(HashSet<Integer> closeList, int interactionId, String userId, FWFacade facade)
	throws DataException
	{
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, "CLOSED_BY", interactionId);
    		Iterator<Integer> closeI = closeList.iterator();
    	    String strComma = "";
    	    String clsList = "";
    	    while (closeI.hasNext()) {
    	    	clsList = clsList + strComma + closeI.next();
    		strComma = ",";
    	    }
    	    Log.debug("closelist is :" + clsList);
    	CCLAUtils.addQueryParamToBinder(binder, "CLOSE_LIST", clsList);
    	facade.execute("qClientServices_CompleteInteractions", binder);
	}
	
	
	private static void addExtraDataToBinder(Integer interactionId, DataBinder binder, FWFacade facade) throws DataException {
		
		DataBinder dataBinder = new DataBinder();
		if (interactionId!=null) {
			CCLAUtils.addQueryIntParamToBinder(dataBinder, "INTERACTION_ID", interactionId);
		
			DataResultSet rsGroupedInteractions = 
				facade.createResultSet("qClientServices_GetGroupedInteractions", dataBinder);
			
			binder.addResultSet("rsGroupedInteractions", rsGroupedInteractions);
		}
 		
		DataResultSet rsFunds = 
			facade.createResultSet("qClientServices_GetFunds", dataBinder);
		
		DataResultSet rsInteractionSubjects = 
			facade.createResultSet("qClientServices_GetInteractionSubjects", dataBinder);
		
		DataResultSet rsOutcomes = 
			facade.createResultSet("qClientServices_getInteractionOutcomes", dataBinder);
		
		DataResultSet rsOutcomeTypes = 
			facade.createResultSet("qClientServices_getInteractionOutcomeTypes", dataBinder);
		
		
		binder.addResultSet("rsFunds", rsFunds);
		binder.addResultSet("rsInteractionSubjects", rsInteractionSubjects);
		binder.addResultSet("rsOutcomes", rsOutcomes);
		binder.addResultSet("rsOutcomeTypes", rsOutcomeTypes);
		
	}
	
}
