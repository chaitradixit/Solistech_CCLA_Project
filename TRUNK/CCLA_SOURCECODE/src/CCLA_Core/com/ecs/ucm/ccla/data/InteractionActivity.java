package com.ecs.ucm.ccla.data;

import java.util.Date;

import com.ecs.ucm.ccla.CCLAUtils;

import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

/** Wrapper class for an activity associated with a particular
 *  Interactions.
 *  Models the INTERACTION_ACTIVITY table.
 *
 */
public class InteractionActivity implements Persistable {
	
	public static class Cols {
		public static final String ACTIVITY_ID = "ACTIVITY_ID";
		public static final String PERSON_ID = "PERSON_ID";
		public static final String INTERACTION_ID = "INTERACTION_ID";
		public static final String ORGANISATION_ID = "ORGANISATION_ID";
		public static final String USER_ID = "USER_ID";
		public static final String ACTIVITY_DATE = "ACTIVITY_DATE";
		public static final String ACTIVITY_TYPE = "ACTIVITY_TYPE";
		public static final String ACTIVITY_ACTION = "ACTIVITY_ACTION";
		public static final String NOTE_ID = "NOTE_ID";
		public static final String ERROR = "ERROR";
	}
	
	public static class Queries {
		public static final String GET_QUERY = "qClientServices_getInteractionActivity";
		public static final String ADD_QUERY = "qClientServices_AddInteractionActivity";
		public static final String UPDATE_QUERY = "qClientServices_UpdateInteractionActivity";
		
	}
	
	private int activityId;
	private Integer personId;
	private Integer organisationId;
	private Integer interactionId;
	private String userId;
	private Date date;
	private String type;
	private String action;
	private Integer noteId;
	private boolean error;
	
	
	public InteractionActivity(DataBinder binder) throws DataException {
		this.setAttributes(binder);
	}
	
	public InteractionActivity(int activityId, Integer personId,
			Integer interactionId, Integer orgId, String userId, Date date, 
			String type,String action, Integer noteId, boolean isError) {
		super();
		this.activityId = activityId;
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
	
	public static InteractionActivity get(DataResultSet rsActivity) 
	 throws NumberFormatException, DataException {
		
		if (rsActivity.isEmpty())
			return null;
		
		return new InteractionActivity(
		 Integer.parseInt(rsActivity.getStringValueByName(Cols.ACTIVITY_ID)), 
		 CCLAUtils.getResultSetIntegerValue(rsActivity,Cols.PERSON_ID),
		 CCLAUtils.getResultSetIntegerValue(rsActivity,Cols.INTERACTION_ID),
		 CCLAUtils.getResultSetIntegerValue(rsActivity,Cols.ORGANISATION_ID),
		 rsActivity.getStringValueByName(Cols.USER_ID),
		 rsActivity.getDateValueByName(Cols.ACTIVITY_DATE),
		 rsActivity.getStringValueByName(Cols.ACTIVITY_TYPE),
		 rsActivity.getStringValueByName(Cols.ACTIVITY_ACTION),
		 CCLAUtils.getResultSetIntegerValue(rsActivity,Cols.NOTE_ID),
		 CCLAUtils.getResultSetBoolValue(rsActivity, Cols.ERROR)
		);
		
	}
	
	public static InteractionActivity get(int activityId, FWFacade facade) 
	 throws DataException, NumberFormatException, ServiceException {
		
		DataResultSet rsActivity = getData(activityId, facade);
		return get(rsActivity);
	}

	public static DataResultSet getData(int activityId, FWFacade facade) 
	 throws DataException {
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.ACTIVITY_ID, activityId);
		
		DataResultSet rsActivity =
		 facade.createResultSet(Queries.GET_QUERY, binder);
		
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
	public static InteractionActivity add(DataBinder binder, FWFacade facade) 
	 throws DataException {
		
		// Fetch new ID for this activity.
		String activityId = CCLAUtils.getNewKey("Activity", facade);
		
		InteractionActivity activity = new InteractionActivity(binder);
		activity.setActivityId(Integer.parseInt(activityId));
		
		activity.validate(null);
		activity.addFieldsToBinder(binder);
		
		facade.execute(Queries.ADD_QUERY, binder);
		return activity;
	}
	
	/** Adds a new activity to the database.
	 *  
	 *  @return the new Activity instance.
	 **/
	public static InteractionActivity add(Integer personId,
	 Integer interactionId, Integer entityId, String userId, String type,
	 String action, Integer noteId, boolean isError, FWFacade facade)
	 throws DataException {
		
		// Fetch new ID for this activity.
		String activityId = CCLAUtils.getNewKey("Activity", facade);
		
		InteractionActivity activity = new InteractionActivity(
				Integer.parseInt(activityId), personId, interactionId, 
				entityId, userId, 
		 null, type, action, noteId, isError);
		
		activity.validate(facade);
		
		DataBinder binder = new DataBinder();
		activity.addFieldsToBinder(binder);

		facade.execute(Queries.ADD_QUERY, binder);
		return activity;
	}
	
	/** Persists changes made to the activity instance to the database */
	public void persist(FWFacade facade, String username) throws DataException {
		this.validate(null);
		
		DataBinder binder = new DataBinder();
		addFieldsToBinder(binder);
		
		facade.execute(Queries.UPDATE_QUERY, binder);
	}
	
	public void addFieldsToBinder(DataBinder binder) {
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.ACTIVITY_ID,activityId);
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.PERSON_ID, personId);
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.INTERACTION_ID, interactionId);
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.ORGANISATION_ID, organisationId);
		CCLAUtils.addQueryParamToBinder(binder, Cols.USER_ID, userId);
		CCLAUtils.addQueryParamToBinder(binder, Cols.ACTIVITY_TYPE, type);
		CCLAUtils.addQueryParamToBinder(binder, Cols.ACTIVITY_ACTION, action);
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.NOTE_ID, noteId);
		CCLAUtils.addQueryBooleanParamToBinder(binder, Cols.ERROR, error);
	}
	

	public void setActivityId(int activityId) {
		this.activityId = activityId;
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

	public Integer getInteractionId() {
		return interactionId;
	}

	public void setInteractionId(Integer interactionId) {
		this.interactionId = interactionId;
	}
	
	public void setAttributes(DataBinder binder) throws DataException 
	{
		this.setPersonId(CCLAUtils.getBinderIntegerValue(binder, Cols.PERSON_ID));
		this.setOrganisationId(CCLAUtils.getBinderIntegerValue(binder, Cols.ORGANISATION_ID));
		this.setUserId(binder.getLocal(Cols.USER_ID));
		this.setType(binder.getLocal(Cols.ACTIVITY_TYPE));
		this.setAction(binder.getLocal(Cols.ACTIVITY_ACTION));
		this.setNoteId(CCLAUtils.getBinderIntegerValue(binder, Cols.NOTE_ID));
		this.setError(CCLAUtils.getBinderBoolValue(binder, Cols.ERROR));
	}

	public void validate(FWFacade facade) throws DataException {

		if (this.getType() == null)
			throw new DataException("Activity type missing");
		
		if (this.getPersonId() == null 
			&& 
			this.getOrganisationId() == null
			&&
			this.getInteractionId() == null)
			throw new DataException
			 ("Interaction, Person and entity references missing");
		
		if (this.getUserId() == null)
			throw new DataException("User name missing");
	}
}
