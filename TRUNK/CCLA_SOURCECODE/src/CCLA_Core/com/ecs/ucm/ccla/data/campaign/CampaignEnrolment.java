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
import com.ecs.ucm.ccla.data.Note;
import com.ecs.ucm.ccla.data.Persistable;
import com.ecs.ucm.ccla.data.SharedCols;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.DataResultSetUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

public class CampaignEnrolment implements Persistable{
	
	public static final String CAMPAIGN_ENROLMENT_ID 	= "CAMPAIGN_ENROLMENT_ID";
	public static final String CAMPAIGN_ID 				= "CAMPAIGN_ID";
	public static final String ENROLMENT_STATUS_ID		= "ENROLMENT_STATUS_ID";
	public static final String CONTACT_ID				= "CONTACT_ID";
	public static final String SUBJECT_STATUS_ID		= "CAMPSUBJECTSTATUS_ID";
	
	private int campaignEnrolmentId;
	private int campaignId;
	private Integer personId;
	private Integer organisationId;
	private int enrolmentStatusId;
	private Integer contactId;
	private Date dateAdded;
	private Date lastUpdated;
	private String lastUpdatedBy;
	private int subjectStatusId;

	public int getSubjectStatusId() {
		return subjectStatusId;
	}

	public void setSubjectStatusId(int subjectStatusId) {
		this.subjectStatusId = subjectStatusId;
	}

	public CampaignEnrolment(int campaignEnrolmentId, int campaignId, Integer personId, Integer organisationId,
			int enrolmentStatusId, Integer contactId, Date dateAdded, Date lastUpdated, String lastUpdatedBy, int subjectStatusId)
	{
		this.campaignEnrolmentId = campaignEnrolmentId;
		this.campaignId = campaignId;
		this.personId = personId;
		this.organisationId = organisationId;
		this.enrolmentStatusId = enrolmentStatusId;
		this.contactId = contactId;
		this.dateAdded = dateAdded;
		this.lastUpdated = lastUpdated;
		this.lastUpdatedBy = lastUpdatedBy;
		this.subjectStatusId = subjectStatusId;
	}
	
	public CampaignEnrolment(DataBinder binder) throws DataException
	{
		this.setAttributes(binder);
	}
	
	/** Creates a new CampaignEnrolment entity in the DB.
	 *  
	 *  Checks first to ensure the enrolment is valid, i.e. the passed Org isn't already
	 *  enrolled to the Campaign.
	 * 
	 * @param enrolment
	 * @param facade
	 * @param username
	 * @return
	 * @throws NumberFormatException
	 * @throws DataException
	 */
	public static CampaignEnrolment add(CampaignEnrolment enrolment, FWFacade facade, String username) 
	throws NumberFormatException, DataException
	{
		CampaignEnrolment existingEnrolment = get
		 (enrolment.getCampaignId(), enrolment.getOrganisationId(), facade);
		
		if (existingEnrolment != null)
			throw new DataException("Organisation ID " + enrolment.getOrganisationId() 
			 + " already enrolled to campaign");
		
		int campaignEnrolmentId = 
			 Integer.parseInt(
			 CCLAUtils.getNewKey("CampaignEnrolment", facade));
			
		enrolment.setCampaignEnrolmentId(campaignEnrolmentId);
		enrolment.setLastUpdatedBy(username);
		DataBinder binder = new DataBinder();
		enrolment.addFieldsToBinder(binder);
		facade.execute("qCore_AddCampaignEnrolment", binder);
		
		// Add audit record
		DataResultSet afterData = CampaignEnrolment.getData(campaignEnrolmentId, facade);
		
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(campaignEnrolmentId, ElementType.SecondaryElementType.CampaignEnrolment.toString());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.ADD.toString(), 
		 ElementType.SecondaryElementType.CampaignEnrolment.toString(), 
		 null, afterData, auditRelations);	
		
		return enrolment;
	}
	
	public static CampaignEnrolment add(DataBinder binder, FWFacade facade, String username) 
	throws NumberFormatException, DataException
	{
		CampaignEnrolment enrolment = new CampaignEnrolment(binder);
		return CampaignEnrolment.add(enrolment, facade, username);
	}
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		CCLAUtils.addQueryIntParamToBinder(binder, CAMPAIGN_ENROLMENT_ID, this.getCampaignEnrolmentId());
		CCLAUtils.addQueryIntParamToBinder(binder, CAMPAIGN_ID, this.getCampaignId());
		CCLAUtils.addQueryIntParamToBinder(binder, SharedCols.PERSON, this.getPersonId());
		CCLAUtils.addQueryIntParamToBinder(binder, SharedCols.ORG, this.getOrganisationId());
		CCLAUtils.addQueryIntParamToBinder(binder, ENROLMENT_STATUS_ID, this.getEnrolmentStatusId());
		CCLAUtils.addQueryIntParamToBinder(binder, CONTACT_ID, this.getContactId());
		CCLAUtils.addQueryDateParamToBinder(binder, SharedCols.DATE_ADDED, this.getDateAdded());
		CCLAUtils.addQueryDateParamToBinder(binder, SharedCols.LAST_UPDATED, this.getLastUpdated());
		CCLAUtils.addQueryParamToBinder(binder, SharedCols.LAST_UPDATED_BY, this.getLastUpdatedBy());
		CCLAUtils.addQueryIntParamToBinder(binder, SUBJECT_STATUS_ID, this.getSubjectStatusId());		
	}
	
	/**
	 * Fetches a campaignEnrolment object by campaignEnrolmentId
	 * @param campaignEnrolmentId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static CampaignEnrolment get(int campaignEnrolmentId, FWFacade facade) 
	throws DataException {
		DataResultSet rs = getData(campaignEnrolmentId, facade);
		return get(rs);
	}

	/**
	 * Fetches a campaignEnrolment object by campaignId and organisationId
	 * @param campaignId
	 * @param organisationId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static CampaignEnrolment get(int campaignId, int organisationId, FWFacade facade) 
	throws DataException {
		DataResultSet rs = getData(campaignId, organisationId, facade);
		return get(rs);
	}
	
	/** Fetches enrolment data by its enrolment ID.
	 * 
	 * @param accNumExt
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getData(int campaignEnrolmentId, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, CAMPAIGN_ENROLMENT_ID, campaignEnrolmentId);
		return (facade.createResultSet("qCore_GetClientEnrolment", binder));
	
	}
	
	/** Fetches enrolment data by its enrolment ID.
	 *  Now joined to the REF_CAMPAIGN_ENROLMENT_STATUS table 
	 * 
	 * @param accNumExt
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getDataExtended(int campaignEnrolmentId, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, CAMPAIGN_ENROLMENT_ID, campaignEnrolmentId);
		return (facade.createResultSet("qCore_GetClientEnrolmentExtended", binder));
	
	}
	
	/**
	 * Fetches the form data for this client
	 * @param enrolment
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getFormData(CampaignEnrolment enrolment, FWFacade facade) 
	throws DataException {
		
		DataBinder binder = new DataBinder();
		binder.putLocal(SharedCols.CAMPAIGN_ENROLMENT, Integer.toString(enrolment.getCampaignEnrolmentId()));
		
		DataResultSet rsEnrolmentForms =
		 facade.createResultSet("qCore_GetFormsByCampaignEnrolmentId", binder);
		
		return rsEnrolmentForms;
	}
	
	/** Returns a list of all campaign enrolments for a given client.
	 *  
	 *  The query joins the matched rows from the CAMPAIGN_ENROLMENT table to:
	 *   -CAMPAIGN
	 *   -REF_CAMPAIGN_ENROLMENT_STATUS
	 *  
	 * @param orgId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getDataExtendedByOrg(int orgId, FWFacade facade) 
	 throws DataException {
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, SharedCols.ORG, orgId);
		
		return facade.createResultSet
		 ("qCore_GetCampaignEnrolmentExtendedByOrg", binder);
	}

	/** Fetches enrolment data by its enrolment ID and campaignId.
	 * 
	 * @param accNumExt
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getData(int campaignId, int organisationId, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, CAMPAIGN_ID, campaignId);
		CCLAUtils.addQueryIntParamToBinder(binder, SharedCols.ORG, organisationId);

		return (facade.createResultSet("qCore_GetClientEnrolmentByIds", binder));
	
	}
	
	
	public static CampaignEnrolment get(DataResultSet rs) throws DataException 
	{
		if (rs.isEmpty())
			return null;
		
		return new CampaignEnrolment(
				DataResultSetUtils.getResultSetIntegerValue(rs, CAMPAIGN_ENROLMENT_ID),
				DataResultSetUtils.getResultSetIntegerValue(rs, CAMPAIGN_ID),
				DataResultSetUtils.getResultSetIntegerValue(rs, SharedCols.PERSON),
				DataResultSetUtils.getResultSetIntegerValue(rs, SharedCols.ORG),
				DataResultSetUtils.getResultSetIntegerValue(rs, ENROLMENT_STATUS_ID),
				DataResultSetUtils.getResultSetIntegerValue(rs, CONTACT_ID),
				rs.getDateValueByName(SharedCols.DATE_ADDED),
				rs.getDateValueByName(SharedCols.LAST_UPDATED),
				DataResultSetUtils.getResultSetStringValue(rs, SharedCols.LAST_UPDATED_BY),
				DataResultSetUtils.getResultSetIntegerValue(rs, SUBJECT_STATUS_ID)
		);
	}	
	
	
	public void persist(FWFacade facade, String username) throws DataException {
		this.validate(facade);
				
		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);
		DataResultSet beforeData = CampaignEnrolment.getData(campaignEnrolmentId, facade);
		
		facade.execute("qCore_UpdateEnrolment", binder);
		
		DataResultSet afterData = CampaignEnrolment.getData(campaignEnrolmentId, facade);
		
		// Add audit record
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(this.getCampaignEnrolmentId(), 
		 ElementType.SecondaryElementType.CampaignEnrolment.toString());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.UPDATE.toString(), 
		 ElementType.SecondaryElementType.CampaignEnrolment.toString(), 
		 beforeData, afterData, auditRelations);
		
	}
	public void setAttributes(DataBinder binder) throws DataException {
		this.setCampaignId(CCLAUtils.getBinderIntegerValue(binder, CAMPAIGN_ID));
		this.setPersonId(CCLAUtils.getBinderIntegerValue(binder, SharedCols.PERSON));
		this.setOrganisationId(CCLAUtils.getBinderIntegerValue(binder, SharedCols.ORG));
		this.setEnrolmentStatusId(CCLAUtils.getBinderIntegerValue(binder, ENROLMENT_STATUS_ID));
		this.setContactId(CCLAUtils.getBinderIntegerValue(binder, CONTACT_ID));
		this.setLastUpdatedBy(binder.getLocal(SharedCols.LAST_UPDATED_BY));
		this.setSubjectStatusId(CCLAUtils.getBinderIntegerValue(binder, SUBJECT_STATUS_ID));
	}
	
	public CampaignActivity addActivity(Integer personId, 
	 Integer activityTypeId, String activityDesc, String noteMsg, FWFacade facade, 
	 String username) throws DataException {

		return addActivity(this, personId, activityTypeId, activityDesc, noteMsg,
		 facade, username);
	}
	
	/**
	 * Adds a new Campaign Activity to the database, tied to the given 
	 * Campaign Enrolment instance. The noteMsg string is optional.
	 * 
	 * @param enrolment
	 * @param PersonId
	 * @param activityTypeId
	 * @param activityDesc
	 * @param username
	 */
	public static CampaignActivity addActivity
	 (CampaignEnrolment enrolment, Integer personId, 
	 Integer activityTypeId, String activityDesc, String noteMsg, FWFacade facade, 
	 String username) throws DataException {
	
		Integer noteId = null;
		if (!StringUtils.stringIsBlank(noteMsg)) {
			Note note = Note.add(noteMsg, username, facade);
			noteId = note.getNoteId();
		}
		
		CampaignActivity activity = new CampaignActivity(
					0, enrolment.getCampaignEnrolmentId(), 
					personId, activityTypeId, activityDesc, null, 
					username, noteId);
		return CampaignActivity.add(activity, username, facade);
	}
	
	
	
	
	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub
		
	}

	
	public int getCampaignEnrolmentId() {
		return campaignEnrolmentId;
	}
	public void setCampaignEnrolmentId(int campaignEnrolmentId) {
		this.campaignEnrolmentId = campaignEnrolmentId;
	}
	public int getCampaignId() {
		return campaignId;
	}
	public void setCampaignId(int campaignId) {
		this.campaignId = campaignId;
	}
	public Integer getPersonId() {
		return personId;
	}
	public void setPersonId(Integer personId) {
		this.personId = personId;
	}
	public Integer getOrganisationId() {
		return organisationId;
	}
	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}

	public Integer getContactId() {
		return contactId;
	}
	public void setContactId(Integer contactId) {
		this.contactId = contactId;
	}
	public Date getDateAdded() {
		return dateAdded;
	}
	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}
	public Date getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public int getEnrolmentStatusId() {
		return enrolmentStatusId;
	}

	public void setEnrolmentStatusId(int enrolmentStatusId) {
		this.enrolmentStatusId = enrolmentStatusId;
	}
	
	/** Returns true if the enrolment has both a correspondent and contact point
	 *  assigned.
	 *  
	 * @return
	 */
	public boolean isContactable() {
		return (getPersonId() != null) && (getContactId() != null);
	}
}
