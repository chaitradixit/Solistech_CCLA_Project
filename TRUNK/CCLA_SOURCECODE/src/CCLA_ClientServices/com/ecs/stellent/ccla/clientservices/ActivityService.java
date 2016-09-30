package com.ecs.stellent.ccla.clientservices;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Activity;
import com.ecs.ucm.ccla.data.UCMNote;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ServiceException;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.server.Service;

public class ActivityService extends Service {
	
	/** Service method for adding a new activity entry, which doesn't 
	 *  have a parent process.
	 *  
	 *  E.g. note relating to entity/client.
	 *  
	 *  If a NOTE value is present in the DataBinder, this will be tagged
	 *  to the Activity as a new Note.
	 *  
	 * @throws DataException 
	 * @throws ServiceException 
	 */
	public void addActivity() throws DataException, ServiceException {
		
		String entityIdStr 		= m_binder.getLocal("ORGANISATION_ID");
		String personIdStr		= m_binder.getLocal("PERSON_ID");
		String interactionIdStr	= m_binder.getLocal("INTERACTION_ID");
		
		if (StringUtils.stringIsBlank(entityIdStr) 
			&& StringUtils.stringIsBlank(personIdStr)
			&& StringUtils.stringIsBlank(interactionIdStr))
			throw new ServiceException("Unable to add activity: " +
			 "entity/person/interaction ID not found");
		
		String message = m_binder.getLocal("NOTE");

		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		try {
			facade.beginTransaction();
			
			if (!StringUtils.stringIsBlank(message)) {
				// Add a new Note for this activity.
				UCMNote note = UCMNote.add(message, m_userData.m_name, facade);

				// Add the new Note ID to the binder, so it gets picked
				// up on Activity creation.
				CCLAUtils.addQueryIntParamToBinder
				 (m_binder, "NOTE_ID", note.getNoteId());
			}
			
			m_binder.putLocal("USER_ID", m_userData.m_name);
			Activity activity = Activity.add(m_binder, facade);
			
			facade.commitTransaction();
			
			Log.debug("Added orphan activity with ID: " + 
			 activity.getActivityId() + ", type: " + activity.getType());
					
		} catch (Exception e) {
			Log.error("Error adding orphan activity", e);
			facade.rollbackTransaction();
			
			throw new ServiceException(e);
		}
	}
	
	/** Adds a new Activity note. This is a special type of Activity
	 *  used purely for tagging a note to a process/person/entity.
	 * 
	 * 
	 * @param processId
	 * @param clientId
	 * @param company
	 * @param person
	 * @param note
	 * @param facade
	 * @throws ServiceException 
	 * @throws DataException 
	 */
	public static void addActivityNote
	 (Integer processId, Integer entityId, Integer personId, 
	 Integer interactionId, String message, String userId, FWFacade facade) 
	
	  throws DataException, ServiceException {
		
		try {
			facade.beginTransaction();
			
			if (processId != null)
				Log.debug("Adding process note for process: " + processId);
			else if (entityId != null)
				Log.debug("Adding entity note for entity: " + entityId);
			else if (personId != null)
				Log.debug("Adding person note for person: " + personId);
			else if (interactionId != null)
				Log.debug("Adding interaction note for interaction: " + interactionId);
			else {
				Log.error("Not enough parameters supplied to create activity note!");
				throw new DataException("Process/client/person ID missing.");
			}
			
			Log.debug("Note content: " + message);
			UCMNote note = UCMNote.add(message, userId, facade);

			Activity activity = Activity.add
			 (processId, personId, interactionId, entityId, userId, 
					 "Note",null, note.getNoteId(), false, facade);
			
			Log.debug("Added new Activity Note: " + activity.getActivityId());
			
			facade.commitTransaction();
			
		} catch (DataException e) {
			facade.rollbackTransaction();
			
			Log.error("Error adding activity note", e);
			throw new ServiceException(e);
		}
	}
	
	/** Adds an 'orphan' activity note to the given person/client. This
	 *  activity entry won't be linked to a particular process.
	 * 
	 *  Requires either clientId/company or personId values present
	 *  in the binder.
	 *  
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void addActivityNote() throws DataException, ServiceException {
		
		String entityIdStr		= m_binder.getLocal("ORGANISATION_ID");
		String personIdStr		= m_binder.getLocal("PERSON_ID");
		String interactionIdStr	= m_binder.getLocal("INTERACTION_ID");
		
		if (StringUtils.stringIsBlank(entityIdStr) && 
				StringUtils.stringIsBlank(personIdStr) && 
				StringUtils.stringIsBlank(interactionIdStr))
			throw new ServiceException("Unable to add activity note: " +
			 "entity/person/interaction ID missing.");
		
		Integer entityId = null, personId = null, interactionId = null;
		
		if (!StringUtils.stringIsBlank(entityIdStr))
			entityId  = new Integer(entityIdStr);
		
		if (!StringUtils.stringIsBlank(personIdStr))
			personId  = new Integer(personIdStr);
		
		if (!StringUtils.stringIsBlank(interactionIdStr))
			interactionId  = new Integer(interactionIdStr);
		
		String note	= m_binder.getLocal("NOTE");
		
		addActivityNote(null, entityId, personId, interactionId, 
		 note, m_userData.m_name, CCLAUtils.getFacade(m_workspace));
	}
	
	public void getEntityActivities() throws DataException, ServiceException {
		String entityIdStr = m_binder.getLocal("ORGANISATION_ID");
		
		if (StringUtils.stringIsBlank(entityIdStr)) {
			throw new ServiceException("Unable to fetch Entity activites: " +
			 "no ORGANISATION_ID found.");
		}
		
		int entityId = Integer.parseInt(entityIdStr);
		FWFacade facade = CCLAUtils.getFacade(m_workspace);
		
		DataResultSet rsActivities = 
		 Activity.getOrganisationActivitiesData(entityId, 10, facade);
	
		m_binder.addResultSet("rsActivities", rsActivities);
	}
	
	public void getPersonActivities() throws DataException, ServiceException {
		String personIdStr = m_binder.getLocal("PERSON_ID");
		
		if (StringUtils.stringIsBlank(personIdStr)) {
			throw new ServiceException("Unable to fetch person activites: " +
			 "no PERSON_ID found.");
		}
		
		int personId = Integer.parseInt(personIdStr);
		FWFacade facade = CCLAUtils.getFacade(m_workspace);
		
		DataResultSet rsActivities = 
		 Activity.getPersonActivitiesData(personId, 10, facade);
	
		m_binder.addResultSet("rsActivities", rsActivities);
	}

	public void getInteractionActivities() throws DataException, ServiceException {
		String interactionIdStr = m_binder.getLocal("INTERACTION_ID");
		
		if (StringUtils.stringIsBlank(interactionIdStr)) {
			throw new ServiceException("Unable to fetch interaction activites: " +
			 "no INTERACTION_ID found.");
		}
		
		int interactionId = Integer.parseInt(interactionIdStr);
		FWFacade facade = CCLAUtils.getFacade(m_workspace);
		
		DataResultSet rsActivities = 
		 Activity.getInteractionActivitiesData(interactionId, 50, facade);
	
		m_binder.addResultSet("rsActivities", rsActivities);
	}
}
