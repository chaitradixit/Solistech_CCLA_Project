package com.ecs.ucm.ccla.data;

import java.util.Date;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

/** Wrapper class for an activity associated with a particular
 *  Client Services process.
 * 
 * @author Tom Marchant
 *
 */
public class Activity implements Persistable {
	
	private int activityId;
	
	private Integer processId;
	private Integer personId;
	private Integer organisationId;
	private Integer interactionId;
	
	private String userId;
	private Date date;
	private String type;
	private String action;
	
	private Integer noteId;
	private boolean error;
	
	public Activity(DataBinder binder) throws DataException {
		this.setAttributes(binder);
	}
	
	public Activity(int activityId, Integer processId, Integer personId,
			Integer interactionId, Integer orgId, String userId, Date date, 
			String type,String action, Integer noteId, boolean isError) {
		super();
		this.activityId = activityId;
		this.processId = processId;
		this.interactionId = interactionId;
		this.personId = personId;
		this.organisationId = orgId;
		this.userId = userId;
		this.date = date;
		this.type = type;
		this.action = action;
		this.noteId = noteId;
		this.error = isError;
		
	}
	
	public static Activity get(DataResultSet rsActivity) 
	 throws NumberFormatException, DataException {
		
		if (rsActivity.isEmpty())
			return null;
		
		return new Activity(
		 Integer.parseInt(rsActivity.getStringValueByName("ACTIVITY_ID")), 
		 CCLAUtils.getResultSetIntegerValue(rsActivity,"PROCESS_ID"),
		 CCLAUtils.getResultSetIntegerValue(rsActivity,"PERSON_ID"),
		 CCLAUtils.getResultSetIntegerValue(rsActivity,"INTERACTION_ID"),
		 CCLAUtils.getResultSetIntegerValue(rsActivity,"ORGANISATION_ID"),
		 rsActivity.getStringValueByName("USER_ID"),
		 rsActivity.getDateValueByName("ACTIVITY_DATE"),
		 rsActivity.getStringValueByName("ACTIVITY_TYPE"),
		 rsActivity.getStringValueByName("ACTIVITY_ACTION"),
		 CCLAUtils.getResultSetIntegerValue(rsActivity,"NOTE_ID"),
		 CCLAUtils.getResultSetBoolValue
		 (rsActivity, "ERROR")
		);
		
	}
	
	public static Activity get(int activityId, FWFacade facade) 
	 throws DataException, NumberFormatException, ServiceException {
		
		DataResultSet rsActivity = getData(activityId, facade);
		return get(rsActivity);
	}

	public static DataResultSet getData(int activityId, FWFacade facade) 
	 throws DataException {
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "ACTIVITY_ID", activityId);
		
		DataResultSet rsActivity =
		 facade.createResultSet("qClientServices_GetActivity", binder);
		
		return rsActivity;
	}
	
	/** Fetches the most recent Activity records for the given Organisation.
	 *  
	 *  The number of rows returned is specified by the numRows parameter.
	 *  
	 * @param entityId
	 * @param numRows
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getOrganisationActivitiesData
	 (int entityId, int numRows, FWFacade facade)
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "ORGANISATION_ID", entityId);
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "numRows", numRows);
		
		DataResultSet rsActivity =
		 facade.createResultSet("qClientServices_GetClientActivities", binder);
		
		return rsActivity;
	}
	
	/** Fetches the most recent Activity records for the given Person.
	 *  
	 *  The number of rows returned is specified by the numRows parameter.
	 *  
	 * @param entityId
	 * @param numRows
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getPersonActivitiesData
	 (int personId, int numRows, FWFacade facade)
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "PERSON_ID", personId);
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "numRows", numRows);
		
		DataResultSet rsActivity =
		 facade.createResultSet("qClientServices_GetPersonActivities", binder);
		
		return rsActivity;
	}
	
	/** Fetches the most recent Interaction records for the given Interaction.
	 *  
	 *  The number of rows returned is specified by the numRows parameter.
	 *  
	 * @param entityId
	 * @param numRows
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getInteractionActivitiesData
	 (int interactionId, int numRows, FWFacade facade)
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "INTERACTION_ID", interactionId);
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "numRows", numRows);
		
		DataResultSet rsActivity =
		 facade.createResultSet("qClientServices_GetInteractionActivities", binder);
		
		return rsActivity;
	}
	
	/** Adds a new activity to the database. Expects all required fields
	 *  to be present in the passed DataBinder.
	 *  
	 * @param binder
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Activity add(DataBinder binder, FWFacade facade) 
	 throws DataException {
		
		// Fetch new ID for this activity.
		String activityId = CCLAUtils.getNewKey("Activity", facade);
		
		Activity activity = new Activity(binder);
		activity.setActivityId(Integer.parseInt(activityId));
		
		activity.validate(null);
		activity.addFieldsToBinder(binder);
		
		facade.execute("qClientServices_AddActivity", binder);
		return activity;
	}
	
	/** Adds a new activity to the database.
	 *  
	 *  @return the new Activity instance.
	 **/
	public static Activity add(Integer processId, Integer personId,
	 Integer interactionId, Integer entityId, String userId, String type,
	 String action, Integer noteId, boolean isError, FWFacade facade)
	 throws DataException {
		
		// Fetch new ID for this activity.
		String activityId = CCLAUtils.getNewKey("Activity", facade);
		
		Activity activity = new Activity(Integer.parseInt(activityId), 
		 processId, personId, interactionId, entityId, userId, 
		 null, type, action, noteId, isError);
		
		activity.validate(facade);
		
		DataBinder binder = new DataBinder();
		activity.addFieldsToBinder(binder);

		facade.execute("qClientServices_AddActivity", binder);
		return activity;
	}
	
	/** Persists changes made to the activity instance to the database */
	public void persist(FWFacade facade, String username) throws DataException {
		this.validate(null);
		
		DataBinder binder = new DataBinder();
		addFieldsToBinder(binder);
		
		facade.execute("qClientServices_UpdateActivity", binder);
	}
	
	public void addFieldsToBinder(DataBinder binder) {
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "ACTIVITY_ID",activityId);
		
		CCLAUtils.addQueryIntParamToBinder(binder, "PROCESS_ID", processId);
		CCLAUtils.addQueryIntParamToBinder(binder, "PERSON_ID", personId);
		CCLAUtils.addQueryIntParamToBinder(binder, "INTERACTION_ID", interactionId);
		CCLAUtils.addQueryIntParamToBinder(binder, "ORGANISATION_ID", organisationId);
		CCLAUtils.addQueryParamToBinder(binder, "USER_ID", userId);

		CCLAUtils.addQueryParamToBinder(binder, "ACTIVITY_TYPE", type);
		CCLAUtils.addQueryParamToBinder(binder, "ACTIVITY_ACTION", action);

		CCLAUtils.addQueryIntParamToBinder(binder, "NOTE_ID", noteId);
		
		CCLAUtils.addQueryBooleanParamToBinder(binder, "ERROR", error);
	}
	
	public boolean isOrphan() {
		return (processId == null);
	}

	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}
	
	public Integer getProcessId() {
		return processId;
	}

	public void setProcessId(Integer processId) {
		this.processId = processId;
	}

	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Integer getNoteId() {
		return noteId;
	}

	public void setNoteId(Integer noteId) {
		this.noteId = noteId;
	}

	public int getActivityId() {
		return activityId;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer orgId) {
		this.organisationId = orgId;
	}

	public void setAttributes(DataBinder binder) throws DataException {

		this.setProcessId(
		 CCLAUtils.getBinderIntegerValue(binder, "PROCESS_ID"));
		
		this.setPersonId(
		 CCLAUtils.getBinderIntegerValue(binder, "PERSON_ID"));
		this.setOrganisationId(
		 CCLAUtils.getBinderIntegerValue(binder, "ORGANISATION_ID"));
		
		this.setUserId(binder.getLocal("USER_ID"));
		this.setType(binder.getLocal("ACTIVITY_TYPE"));
		this.setAction(binder.getLocal("ACTIVITY_ACTION"));
		
		this.setNoteId(
		 CCLAUtils.getBinderIntegerValue(binder, "NOTE_ID"));
		this.setError(
		 CCLAUtils.getBinderBoolValue(binder, "ERROR"));
	}

	public void validate(FWFacade facade) throws DataException {

		if (this.getType() == null)
			throw new DataException("Activity type missing");
		
		if (this.getPersonId() == null 
			&& 
			this.getOrganisationId() == null
			&&
			this.getProcessId() == null)
			throw new DataException
			 ("Process, person and entity references missing");
		
		if (this.getUserId() == null)
			throw new DataException("User name missing");
	}
}
