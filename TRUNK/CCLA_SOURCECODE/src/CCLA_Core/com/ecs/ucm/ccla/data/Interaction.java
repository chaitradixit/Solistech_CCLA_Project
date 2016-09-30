package com.ecs.ucm.ccla.data;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.util.Date;
import java.util.Vector;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Represents records in the INTERACTION table.
 * 
 * @author Tom
 *
 */
public class Interaction implements Persistable {
	
	private int interactionId;
	
	private String user;
	private Date date;

	private Integer organisationId;
	private Integer personId;
	private Integer accountId;
	
	private String confirmTypes;
	private String confirmOther;
	
	private String type;
	private Integer subjectId;
	private Integer categoryId;
	
	private boolean query;
	private boolean complaint;
	private boolean breach;
	private boolean error;
	
	private Integer campaignId;
	private String jobId;
	
	private Integer outcomeId;
	private Integer outcomeType;
	private String outcomeText;
	private String status;
	
	private String assignee;
	private Date deadline;
	
	private Date lastUpdated;
	private String fundcode;
	
	public static class InteractionOutcome {
		public static final int COMPLETED = 1;
		public static final int WAITING_ON_CORR_CALLBACK = 2;
		public static final int CORR_CALLBACK_REQUIRED = 3;
		public static final int FURTHER_ACTION_REQUIRED = 4;
		public static final int DISCONNECTED = 5;
	}
	
	public Interaction(int interactionId, String user, Date date,
			Integer organisationId, Integer personId, Integer accountId,
			String confirmTypes, String confirmOther, 
			String type, Integer subjectId, Integer categoryId,
			boolean query, boolean complaint, boolean breach, boolean error,
			Integer campaignId, String jobId, 
			 String status, String assignee,
			Date deadline, Date lastUpdated, String fundcode, Integer outcomeId, Integer outcomeType, String outcomeText) {
		this.interactionId = interactionId;
		this.user = user;
		this.date = date;
		
		this.organisationId = organisationId;
		this.personId = personId;
		this.accountId = accountId;

		this.confirmTypes = confirmTypes;
		this.confirmOther = confirmOther;
		this.type = type;
		this.subjectId = subjectId;
		this.categoryId = categoryId;
		
		this.query = query;
		this.complaint = complaint;
		this.breach = breach;
		this.error = error;
		
		this.campaignId = campaignId;
		this.jobId = jobId;
		
		this.outcomeId = outcomeId;
		this.outcomeType = outcomeType;
		this.outcomeText = outcomeText;
		this.status = status;
		
		this.assignee = assignee;
		this.deadline = deadline;
		this.lastUpdated = lastUpdated;
		this.fundcode = fundcode;
	}
	
	public static Interaction get(int interactionId, FWFacade facade) 
	 throws DataException {
		
		DataResultSet rsInteraction = getData(interactionId, facade);
		
		if (!rsInteraction.isEmpty())
			return get(rsInteraction);
		else
			return null;
	}
	
	public static DataResultSet getData(int interactionId, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		binder.putLocal("INTERACTION_ID", Integer.toString(interactionId));
		
		DataResultSet rsInteraction = facade.createResultSet
		 ("qClientServices_GetInteraction", binder);
		
		return rsInteraction;
	}
	
	public static Interaction get(DataResultSet rsInteraction) 
	 throws DataException {
		
		int id = Integer.parseInt(
		 rsInteraction.getStringValueByName("INTERACTION_ID"));
		
		Interaction interaction = new Interaction(
			id,
			rsInteraction.getStringValueByName("USER_ID"),
			rsInteraction.getDateValueByName("INTERACTION_DATE"),
			CCLAUtils.getResultSetIntegerValue(rsInteraction, "ORGANISATION_ID"),
			CCLAUtils.getResultSetIntegerValue(rsInteraction, "PERSON_ID"),
			CCLAUtils.getResultSetIntegerValue(rsInteraction, "ACCOUNT_ID"),
			
			rsInteraction.getStringValueByName("CONFIRM_TYPES"),
			rsInteraction.getStringValueByName("CONFIRM_OTHER"),
			rsInteraction.getStringValueByName("INTERACTION_TYPE"),
			CCLAUtils.getResultSetIntegerValue(rsInteraction,"SUBJECT_ID"),
			CCLAUtils.getResultSetIntegerValue(rsInteraction,"CATEGORY_ID"),
			
			CCLAUtils.getResultSetBoolValue(rsInteraction, "IS_QUERY"),
			CCLAUtils.getResultSetBoolValue(rsInteraction, "IS_COMPLAINT"),
			CCLAUtils.getResultSetBoolValue(rsInteraction, "IS_BREACH"),
			CCLAUtils.getResultSetBoolValue(rsInteraction, "IS_ERROR"),
			
			CCLAUtils.getResultSetIntegerValue(rsInteraction, "CAMPAIGN_ID"),
			rsInteraction.getStringValueByName("JOB_ID"),
			
			rsInteraction.getStringValueByName("INTERACTION_STATUS"),
			rsInteraction.getStringValueByName("ASSIGNEE"),
			rsInteraction.getDateValueByName("DEADLINE"),
			rsInteraction.getDateValueByName("LAST_UPDATED"),
			rsInteraction.getStringValueByName("FUND_CODE"),
			CCLAUtils.getResultSetIntegerValue(rsInteraction,"OUTCOME_ID"),
			CCLAUtils.getResultSetIntegerValue(rsInteraction,"OUTCOME_TYPE_ID"),
			rsInteraction.getStringValueByName("OUTCOME_TEXT")
		);
		
		return interaction;
	}
	
	public static Interaction add(DataBinder binder, FWFacade facade) 
	 throws DataException {
		
		return add(
		 binder.getLocal("USER_ID"),
		 CCLAUtils.getBinderIntegerValue(binder, "ORGANISATION_ID"),
		 CCLAUtils.getBinderIntegerValue(binder, "PERSON_ID"),
		 CCLAUtils.getBinderIntegerValue(binder, "ACCOUNT_ID"),
		 binder.getLocal("CONFIRM_TYPES"),
		 binder.getLocal("CONFIRM_OTHER"),
		 binder.getLocal("INTERACTION_TYPE"),
		 CCLAUtils.getBinderIntegerValue(binder, "SUBJECT_ID"),
		 CCLAUtils.getBinderIntegerValue(binder, "CATEGORY_ID"),
		 CCLAUtils.getBinderBoolValue(binder, "IS_QUERY"),
		 CCLAUtils.getBinderBoolValue(binder, "IS_COMPLAINT"),
		 CCLAUtils.getBinderBoolValue(binder, "IS_BREACH"),
		 CCLAUtils.getBinderBoolValue(binder, "IS_ERROR"),
		 CCLAUtils.getBinderIntegerValue(binder, "CAMPAIGN_ID"),
		 binder.getLocal("JOB_ID"),
		 binder.getLocal("INTERACTION_STATUS"),
		 binder.getLocal("ASSIGNEE"),
		 CCLAUtils.getBinderDateValue(binder, "DEADLINE"),
		 binder.getLocal("NOTE"),
		 binder.getLocal("FUND_CODE"),
		 CCLAUtils.getBinderIntegerValue(binder, "OUTCOME_ID"),
		 CCLAUtils.getBinderIntegerValue(binder, "OUTCOME_TYPE_ID"),
		 binder.getLocal("OUTCOME_TEXT"),
		 facade
		);
	}
	
	/** Adds a new interaction to the database.
	 *  
	 *  @return the new Activity instance.
	 **/
	public static Interaction add(
		String user,
		Integer organisationId, Integer personId, Integer accountId,
		String confirmTypes, String confirmOther, 
		String type, 
		Integer subjectId, Integer categoryId,
		boolean query, boolean complaint, boolean breach, boolean error,
		Integer campaignId, String jobId,
		String status, String assignee, Date deadline,
		String note, String fundcode, Integer outcomeId, Integer outcomeType, String outcomeText,
		FWFacade facade)
	 throws DataException {
		
		// Fetch new ID for this interaction.
		String interactionId = CCLAUtils.getNewKey("Interaction", facade);
		
		Date curDate = new Date();
		
		Interaction interaction = new Interaction(
			Integer.parseInt(interactionId), 
			user,
			curDate,
			organisationId, personId, accountId,
			confirmTypes, confirmOther, 
			type, subjectId, categoryId,
			query, complaint, breach, error,
			campaignId, jobId,  
			status, assignee, deadline, curDate, fundcode,
			outcomeId, outcomeType, outcomeText
		);
		
		DataBinder binder = new DataBinder();
		interaction.addFieldsToBinder(binder);
		
		binder.putLocal("newInteractionId", Integer.toString(
		 interaction.getInteractionId()));
		
		interaction.validate(facade);
		
		facade.execute("qClientServices_AddInteraction", binder);
		Log.debug("Added new interaction: " + interactionId);
		
		// If a note string was supplied, add it now in a separate
		// table.
		if (!StringUtils.stringIsBlank(note))
			interaction.addNote(note, interaction.getUser(), facade);
		
		return interaction;
	}
	
	/** Attaches an Activity Note to the interaction instance.
	 *  
	 *  This requires a new mapping entry in the CCLA_NOTE_MAPPING
	 *  table, as well as the NOTES table
	 *  
	 * @param note
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public int addNote(String message, String userId, FWFacade facade) 
	 throws DataException {
		
		// Create the new Note first.
		//UCMNote note = UCMNote.add(message, userId, facade);
		Note note = Note.add(message, userId, facade);
		
		if (note != null) {
			// Now create the Activity Note
			
			/*
			 * Old method, pre-Activity
			 
			return NoteMapping.add(
			 this.getInteractionId(), null, note.getNoteId(), facade);
			*/
			
			InteractionActivity activity = 
				InteractionActivity.add(personId, interactionId, organisationId, 
			 userId, "Note", null, note.getNoteId(), false, facade);
			
			return activity.getActivityId();
		
		} else {
			throw new DataException("Failed to add note to interaction: " 
			 + this.getInteractionId());
		}
	}

	/** Returns dataresultset of ccla_notes if note exists for a interaction
	 * 
	 *  
	 * @param interactionId int
	 * @param facade
	 * @param binder
	 * @return DataResultSet
	 * @throws DataException 
	 */
	public DataResultSet getNote(int interactionId, FWFacade facade) 
	 throws DataException {
		DataBinder binder = new DataBinder();
		binder.putLocal("INTERACTION_ID",Integer.toString(interactionId));
		DataResultSet rsNote = facade.createResultSet("qClientServices_GetNoteByInteraction", binder);
		if (!rsNote.isEmpty())
		{
			return rsNote;
		} else
		{
			return null;
		}
	}
	
	public int addActivity(String activityType, String action, 
	 String userId, FWFacade facade) throws DataException {
		
		InteractionActivity activity = 
			InteractionActivity.add(personId, interactionId, organisationId, 
		 userId, activityType, action, null, false, facade);
		
		return activity.getActivityId();
	}

	/** @deprecated notes are now attached directly to Activity items.
	 * 
	 * @param interactionId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Vector<NoteMapping> getNotes
	 (int interactionId, FWFacade facade) throws DataException {
		
		Vector<NoteMapping> notes = new Vector<NoteMapping>();
		
		DataResultSet rsNotes = getNotesData(interactionId, facade);
		
		if (!rsNotes.isEmpty()) {
			do {
				notes.add(NoteMapping.get(rsNotes));	
			} while (rsNotes.next());
		}
		
		return notes;
	}
	
	/** Fetches a ResultSet of all notes data connected to the interaction
	 *  ID.
	 *  
	 * @param interactionId
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getNotesData
	 (int interactionId, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "INTERACTION_ID", interactionId);
		
		return facade.createResultSet
		 ("qClientServices_GetInteractionNotes", binder);
	}
	
	public void persist(FWFacade facade, String username) throws DataException {
		
		this.validate(facade);
		
		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);
		
		facade.execute("qClientServices_UpdateInteraction", binder);
		Log.debug("Persisted interaction: " + this.getInteractionId());
	}
	
	public void addFieldsToBinder(DataBinder binder) {
		
		binder.putLocal("INTERACTION_ID", Integer.toString(
		 this.getInteractionId()));
		
		binder.putLocal("USER_ID", this.getUser());
		binder.putLocalDate("INTERACTION_DATE", this.getDate());
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "ORGANISATION_ID", this.getOrganisationId());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "PERSON_ID", this.getPersonId());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "ACCOUNT_ID", this.getAccountId());
		
		CCLAUtils.addQueryParamToBinder
		 (binder, "CONFIRM_TYPES", this.getConfirmTypes());
		CCLAUtils.addQueryParamToBinder
		 (binder, "CONFIRM_OTHER", this.getConfirmOther());
		
		CCLAUtils.addQueryParamToBinder
		 (binder, "INTERACTION_TYPE", this.getType());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "SUBJECT_ID", this.getSubjectId());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "CATEGORY_ID", this.getCategoryId());

		CCLAUtils.addQueryBooleanParamToBinder
		 (binder, "IS_QUERY", this.isQuery());
		CCLAUtils.addQueryBooleanParamToBinder
		 (binder, "IS_COMPLAINT", this.isComplaint());
		CCLAUtils.addQueryBooleanParamToBinder
		 (binder, "IS_BREACH", this.isBreach());
		CCLAUtils.addQueryBooleanParamToBinder
		 (binder, "IS_ERROR", this.isError());
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "CAMPAIGN_ID", this.getCampaignId());
		CCLAUtils.addQueryParamToBinder
		 (binder, "JOB_ID", this.getJobId());
		
		CCLAUtils.addQueryParamToBinder
		 (binder, "INTERACTION_STATUS", this.getStatus());
		CCLAUtils.addQueryParamToBinder
		 (binder, "ASSIGNEE", this.getAssignee());
		CCLAUtils.addQueryDateParamToBinder
		 (binder, "DEADLINE", this.getDeadline());
		
		CCLAUtils.addQueryDateParamToBinder
		 (binder, "LAST_UPDATED", this.getLastUpdated());
		CCLAUtils.addQueryParamToBinder(binder, "FUND_CODE", this.getFundCode());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "OUTCOME_ID", this.getOutcomeId());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "OUTCOME_TYPE_ID", this.getOutcomeType());
		CCLAUtils.addQueryParamToBinder(binder, "OUTCOME_TEXT", this.getOutcomeText());
	}

	public int getInteractionId() {
		return interactionId;
	}

	public void setInteractionId(int interactionId) {
		this.interactionId = interactionId;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

	public String getConfirmTypes() {
		return confirmTypes;
	}

	public void setConfirmTypes(String confirmTypes) {
		this.confirmTypes = confirmTypes;
	}

	public String getConfirmOther() {
		return confirmOther;
	}

	public void setConfirmOther(String confirmOther) {
		this.confirmOther = confirmOther;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Integer campaignId) {
		this.campaignId = campaignId;
	}

	public Integer getOutcomeId() {
		return outcomeId;
	}

	public void setOutcomeId(Integer outcomeId) {
		this.outcomeId = outcomeId;
	}

	public Integer getOutcomeType() {
		return outcomeType;
	}

	public void setOutcomeType(Integer outcomeType) {
		this.outcomeType = outcomeType;
	}	
	public String getOutcomeText() {
		return outcomeText;
	}

	public void setOutcomeText(String outcomeText) {
		this.outcomeText = outcomeText;
	}		
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public boolean isQuery() {
		return query;
	}

	public void setQuery(boolean query) {
		this.query = query;
	}

	public boolean isComplaint() {
		return complaint;
	}

	public void setComplaint(boolean complaint) {
		this.complaint = complaint;
	}

	public boolean isBreach() {
		return breach;
	}

	public void setBreach(boolean breach) {
		this.breach = breach;
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

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}

	public Integer getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public void setAttributes(DataBinder binder) throws DataException {
		
		this.setType(binder.getLocal("INTERACTION_TYPE"));
		this.setSubjectId(CCLAUtils.getBinderIntegerValue(binder, "SUBJECT_ID"));
		this.setCategoryId(CCLAUtils.getBinderIntegerValue(binder, "CATEGORY_ID"));
		this.setOutcomeId(CCLAUtils.getBinderIntegerValue(binder, "OUTCOME_ID"));
		this.setOutcomeType(CCLAUtils.getBinderIntegerValue(binder, "OUTCOME_TYPE_ID"));
		this.setOutcomeText(binder.getLocal("OUTCOME_TEXT"));
		this.setAssignee(binder.getLocal("ASSIGNEE"));
		this.setDeadline(CCLAUtils.getBinderDateValue(binder, "DEADLINE"));
	}

	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub
		
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getJobId() {
		return jobId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public Integer getAccountId() {
		return accountId;
	}
	
	public void setFundCode(String fundcode) {
		this.fundcode = fundcode;
	}

	public String getFundCode() {
		return fundcode;
	}	
}
