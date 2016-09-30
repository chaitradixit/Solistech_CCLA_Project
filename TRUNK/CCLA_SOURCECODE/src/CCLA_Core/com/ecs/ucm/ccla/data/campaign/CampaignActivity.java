package com.ecs.ucm.ccla.data.campaign;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.util.Date;
import java.util.HashMap;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Audit;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.Globals;
import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.DataResultSetUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/**
 * Models CAMPAIGN_ACTIVITY Table
 * @author Cam
 *
 */
public class CampaignActivity {

	/* ************************ Constants *********************** */
	//BINDER AND DB COLUMN
	public static final String ACTIVITY_ID 		= "CAMPAIGN_ACTIVITY_ID";
	public static final String ENROLMENT_ID		= "CAMPAIGN_ENROLMENT_ID";
	public static final String PERSON_ID		= "PERSON_ID"; 
	public static final String TYPE_ID			= "CAMPAIGN_ACTIVITY_TYPE_ID";
	public static final String DESCRIPTION		= "ACTIVITY_DESCRIPTION";
	public static final String DATE_ADDED 		= "DATE_ADDED";
	public static final String LAST_UPDATED_BY	= "LAST_UPDATED_BY";
	public static final String NOTE_ID			= "NOTE_ID";
	
	//QUERY
	private static final String ADD_QUERY_NAME = "qCore_AddCampaignActivity";
	private static final String GET_BY_ID_QUERY_NAME = "qCore_GetCampaignActivityById";
	private static final String GET_BY_ENROLMENT_ID_QUERY_NAME = "qCore_GetCampaignActivitiesByEnrolmentId";
	
	
	/* ************************ Properties *********************** */
	private int campaignActivityId;
	private int enrolmentId;
	private Integer personId;
	private Integer typeId;
	private Integer noteId;
	private String description;
	private String lastUpdatedBy;
	private Date dateAdded;
	
	/**
	 * Constructor
	 * @param binder
	 * @throws DataException
	 */
	public CampaignActivity(DataBinder binder) throws DataException {
		this.setAttributes(binder);
	}
	
	/**
	 * Constructor
	 * @param campaignActivityId
	 * @param enrolmentId
	 * @param personId
	 * @param typeId
	 * @param description
	 * @param dateAdded
	 * @param lastUpdatedBy
	 * @param noteId
	 */
	public CampaignActivity(int campaignActivityId, int enrolmentId, Integer personId,
			Integer typeId, String description, Date dateAdded, String lastUpdatedBy,
			Integer noteId) {
		this.campaignActivityId = campaignActivityId;
		this.enrolmentId = enrolmentId;
		this.personId = personId;
		this.typeId = typeId;
		this.description = description;
		this.dateAdded = dateAdded;
		this.lastUpdatedBy = lastUpdatedBy;
		this.noteId = noteId;
	}
	
	// TODO: complete
	public static CampaignActivity add(FWFacade facade) 
	 throws DataException {
		
		int campaignActivityId = Integer.parseInt(
		 CCLAUtils.getNewKey("CampaignActivity", facade));
		
		return null;
	}
	
	public void setCampaignActivityId(int campaignActivityId) {
		this.campaignActivityId = campaignActivityId;
	}
	public int getCampaignActivityId() {
		return campaignActivityId;
	}
	public void setEnrolmentId(int enrolmentId) {
		this.enrolmentId = enrolmentId;
	}
	public int getEnrolmentId() {
		return enrolmentId;
	}
	public void setPersonId(Integer personId) {
		this.personId = personId;
	}
	public Integer getPersonId() {
		return personId;
	}
	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}
	public Integer getTypeId() {
		return typeId;
	}
	public void setNoteId(Integer noteId) {
		this.noteId = noteId;
	}
	public Integer getNoteId() {
		return noteId;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription() {
		return description;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}
	public Date getDateAdded() {
		return dateAdded;
	}
	
	/**
	 * Adds a campaign activity to the database
	 * @param binder
	 * @param username
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static CampaignActivity add(DataBinder binder, String username, FWFacade facade) 
	throws DataException {
		CampaignActivity activity = new CampaignActivity(binder);
		return CampaignActivity.add(activity, username, facade);
	}
	
	
	/**
	 * Add activity to the database
	 * @param activity
	 * @param username
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static CampaignActivity add
	(CampaignActivity activity, String username, FWFacade facade) throws DataException {
		
		if (activity.getCampaignActivityId()==0) {
			activity.setCampaignActivityId(
					Integer.parseInt(
							CCLAUtils.getNewKey("CampaignActivity", facade)));
		}
		
		activity.setLastUpdatedBy(username);
		DataBinder binder = new DataBinder();
		activity.validate(facade);		
		activity.addFieldsToBinder(binder);
		
		facade.execute(ADD_QUERY_NAME, binder);
		
		DataResultSet afterData = CampaignActivity.getData
		 (activity.getCampaignActivityId(), facade);
		
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(activity.getCampaignActivityId(), 
		 ElementType.SecondaryElementType.CampaignActivity.toString());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.ADD.toString(), 
		 ElementType.SecondaryElementType.CampaignActivity.toString(), 
		 null, afterData, auditRelations);	
		
		// Touch the CampaignEnrolment instance so the 'last updated' date is changed
		CampaignEnrolment enrolment = CampaignEnrolment.get
		 (activity.getEnrolmentId(), facade);
		
		enrolment.persist(facade, username);
		
		return CampaignActivity.get(activity.getCampaignActivityId(), facade);
	}
	
	/**
	 * 
	 * @param binder
	 * @throws DataException
	 */
	public void addFieldsToBinder(DataBinder binder) throws DataException 
	{
		BinderUtils.addIntParamToBinder(binder, ACTIVITY_ID, this.getCampaignActivityId());
		BinderUtils.addIntParamToBinder(binder, ENROLMENT_ID, this.getEnrolmentId());
		BinderUtils.addIntParamToBinder(binder, PERSON_ID, this.getPersonId());
		BinderUtils.addIntParamToBinder(binder, TYPE_ID, this.getTypeId());
		BinderUtils.addIntParamToBinder(binder, NOTE_ID, this.getNoteId());
		BinderUtils.addParamToBinder(binder, DESCRIPTION, this.getDescription());
		BinderUtils.addDateParamToBinder(binder, DATE_ADDED, this.getDateAdded());
		BinderUtils.addParamToBinder(binder, LAST_UPDATED_BY, this.getLastUpdatedBy());
	}	
	
	/**
	 * Populates CampaignActivity object with values from the DataBinder.
	 * @param binder
	 * @throws DataException
	 */
	public void setAttributes(DataBinder binder) throws DataException 
	{
		Integer campaignActivityId = BinderUtils.getBinderIntegerValue(binder, ACTIVITY_ID);
		if (campaignActivityId!=null)
			this.setCampaignActivityId(campaignActivityId);
		
		Integer enrolmentId = BinderUtils.getBinderIntegerValue(binder, ENROLMENT_ID);
		if (enrolmentId!=null)
			this.setEnrolmentId(enrolmentId);
		
		this.setPersonId(BinderUtils.getBinderIntegerValue(binder, PERSON_ID));
		this.setTypeId(BinderUtils.getBinderIntegerValue(binder, TYPE_ID));
		this.setNoteId(BinderUtils.getBinderIntegerValue(binder, NOTE_ID));
		this.setDescription(BinderUtils.getBinderStringValue(binder, DESCRIPTION));
		this.setDateAdded(BinderUtils.getBinderDateValue(binder, DATE_ADDED));
		this.setLastUpdatedBy(BinderUtils.getBinderStringValue(binder, LAST_UPDATED_BY));			
	}	
	
	/**
	 * Validate the data
	 */
	public void validate(FWFacade facade) throws DataException {
		//TODO
	}	
	
	/**
	 * Get Campaign Activity
	 * @param activityId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static CampaignActivity get(int activityId, FWFacade facade) 
	 throws DataException {
		DataResultSet rs = getData(activityId, facade);
		return get(rs);
	}
	
	/**
	 * 
	 * @param activityId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getData(int activityId, FWFacade facade) 
	 throws DataException {
		DataBinder binder = new DataBinder();
		
		BinderUtils.addIntParamToBinder(binder, ACTIVITY_ID, activityId);
		DataResultSet rsCampaignActivity = facade.createResultSet
		 (GET_BY_ID_QUERY_NAME, binder);
		
		return rsCampaignActivity;
	}	
	
	/**
	 * Gets a campaignactivty from the DataResultSet
	 * @param rs
	 * @return
	 * @throws DataException
	 */
	public static CampaignActivity get(DataResultSet rs) throws DataException {
		
		if (rs.isEmpty())
			return null;
		
		return new CampaignActivity(
			DataResultSetUtils.getResultSetIntegerValue(rs, ACTIVITY_ID),
			DataResultSetUtils.getResultSetIntegerValue(rs, ENROLMENT_ID),
			DataResultSetUtils.getResultSetIntegerValue(rs, PERSON_ID),
			DataResultSetUtils.getResultSetIntegerValue(rs, TYPE_ID),
			DataResultSetUtils.getResultSetStringValue(rs, DESCRIPTION),
			rs.getDateValueByName(DATE_ADDED),
			DataResultSetUtils.getResultSetStringValue(rs, LAST_UPDATED_BY),
			DataResultSetUtils.getResultSetIntegerValue(rs, NOTE_ID)		
		);
	}	
	
	/**
	 * Get the activity data for this campaign enrolment
	 * @param processId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getActivityData(int campaignEnrolmentId, FWFacade facade)
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		BinderUtils.addIntParamToBinder(binder, ENROLMENT_ID, campaignEnrolmentId);
		
		DataResultSet rsProcessActivities = facade.createResultSet
		 (GET_BY_ENROLMENT_ID_QUERY_NAME, binder);
		
		return rsProcessActivities;
	}	
}
