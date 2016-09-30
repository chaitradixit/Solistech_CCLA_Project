package com.ecs.ucm.ccla.data.form;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.Audit;
import com.ecs.ucm.ccla.data.Contact;
import com.ecs.ucm.ccla.data.Element;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Globals;
import com.ecs.ucm.ccla.data.Persistable;
import com.ecs.ucm.ccla.data.SharedCols;
import com.ecs.ucm.ccla.data.ElementType.SecondaryElementType;
import com.ecs.ucm.ccla.data.Globals.AuditActions;
import com.ecs.ucm.ccla.data.subscription.Subscription;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Models entries in the FORM table.
 * 
 * @author Tom
 *
 */
public class Form implements Persistable{
	
	@Override
	public String toString() {
		return "Form [campaignActivityId=" + campaignActivityId
				+ ", campaignEnrolmentId=" + campaignEnrolmentId
				+ ", dateAdded=" + dateAdded + ", dateGenerated="
				+ dateGenerated + ", datePrinted=" + datePrinted
				+ ", dateReturned=" + dateReturned + ", dateUploaded="
				+ dateUploaded + ", formId=" + formId + ", formStatusId="
				+ formStatusId + ", formType=" + formType + ", genDocGuid="
				+ genDocGuid + ", lastUpdated=" + lastUpdated
				+ ", organisationId=" + organisationId + ", personId="
				+ personId + ", retDocGuid=" + retDocGuid + ", subscriptionId="
				+ subscriptionId + ", userId=" + userId + "]";
	}

	private int formId;
	private FormType formType;
	private int formStatusId;
	private Integer campaignEnrolmentId;
	private Integer campaignActivityId;
	private Integer personId;
	private String genDocGuid;
	private String retDocGuid;
	private Integer subscriptionId;
	private Date dateAdded;
	private Date dateGenerated;
	private Date dateUploaded;
	private Date datePrinted;
	private Date dateReturned;
	private Date lastUpdated;
	private String userId;
	private Integer organisationId;
	private Boolean valid;
	private Date returnDeadlineDate;
	private Date calculationDate;

	public static enum FormStatus {
		NEW(1),
		GENERATED(2),
		UPLOADED(3),
		PRINTED(4),
		DISPATCHED(5),
		RETURNED(6),
		CANCELLED(7),
		OLD(8),
		VALIDATED(9),
		INVALIDATED(10),
		INDEXED(11);
		
		public final int id;
		
		private FormStatus(int id) {
			this.id = id;
		}
	}
	
	public static final FormStatus DEFAULT_STATUS = FormStatus.NEW;
	
	public static class Cols {
		public static final String ID				= "FORM_ID";
		public static final String FORM_TYPE 		= "FORM_TYPE_ID";
		public static final String FORM_STATUS 		= "FORM_STATUS_ID";
		
		public static final String GEN_DOC 			= "GEN_DOC_GUID";
		public static final String RET_DOC 			= "RET_DOC_GUID";
		
		public static final String DATE_GENERATED 	= "DATE_GENERATED";
		public static final String DATE_UPLOADED 	= "DATE_UPLOADED";
		public static final String DATE_PRINTED		= "DATE_PRINTED";
		public static final String DATE_RETURNED	= "DATE_RETURNED";
		
		public static final String IS_VALID			= "IS_VALID";
		
		public static final String RETURN_DEADLINE_DATE = "RETURN_DEADLINE_DATE";
		public static final String CALCULATION_DATE = "CALCULATION_DATE";
	}

	public Form(int formId, FormType formType, Integer formStatusId, 
			Integer campaignEnrolmentId, Integer campaignActivityId, 
			Integer personId, String genDocGuid, String retDocGuid,
			Date dateAdded, Date dateGenerated, Date dateUploaded, Date datePrinted,
			Date dateReturned, Date lastUpdated, String userId, 
			Integer organisationId, Integer subscriptionId, Boolean valid,
			Date returnDeadlineDate, Date calculationDate) {
		this.formId = formId;
		this.formType = formType;
		this.formStatusId = formStatusId;
		this.campaignEnrolmentId = campaignEnrolmentId;
		this.campaignActivityId = campaignActivityId;
		this.personId = personId;
		this.setGenDocGuid(genDocGuid);
		this.setRetDocGuid(retDocGuid);
		this.dateAdded = dateAdded;
		this.dateGenerated = dateGenerated;
		this.dateUploaded = dateUploaded;
		this.datePrinted = datePrinted;
		this.dateReturned = dateReturned;
		this.lastUpdated = lastUpdated;
		this.userId = userId;
		this.setOrganisationId(organisationId);
		this.subscriptionId = subscriptionId;
		this.valid = valid;
		this.returnDeadlineDate = returnDeadlineDate;
		this.calculationDate = calculationDate;
		
		Log.debug("Constructing Form with Org ID: " + this.organisationId + 
		 ", passed Org ID: " + organisationId);
	}

	public Form(DataBinder binder) throws DataException {
		this.setAttributes(binder);
	}
	
	/** Adds a new Form.
	 * 
	 * @param formTypeId
	 * @param campaignEnrolmentId
	 * @param campaignActivityId
	 * @param personId
	 * @param facade
	 * @param username
	 * @return
	 * @throws DataException 
	 * @throws NumberFormatException 
	 */
	public static Form add(FormType formType, Integer campaignEnrolmentId,
	 Integer campaignActivityId, Integer personId, String genDocGuid, 
	 Integer organisationId, Integer investmentId,
	 FWFacade facade, String username) throws DataException {
		
		int newFormId = Integer.parseInt(CCLAUtils.getNewKey("Form", facade));
		
		FormStatus status = DEFAULT_STATUS;
		Date genDate	  = null;
		
		if (genDocGuid != null) {	
			// If a Generated Doc GUID is passed, we know the form is already generated.
			status = FormStatus.GENERATED;
			genDate = new Date();
		}
		
		Form form = new Form(newFormId, formType, status.id, 
		 campaignEnrolmentId, campaignActivityId, personId, genDocGuid, null, null, 
		 genDate, null, null, null, null, username, organisationId, investmentId, 
		 null, null, null);
		
		DataBinder binder = new DataBinder();
		form.addFieldsToBinder(binder);
		
		facade.execute("qCore_AddForm", binder);
		
		// Add audit record
		DataResultSet afterData = Form.getData(newFormId, facade);
		
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(newFormId, ElementType.SecondaryElementType.Form.toString());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.ADD.toString(), 
		 ElementType.SecondaryElementType.Form.toString(), 
		 null, afterData, auditRelations);	
		
		return get(afterData);
	}
	
	public static Form get(int formId, FWFacade facade) throws DataException {
		return get(getData(formId, facade));
	}
	
	public static Form get(DataResultSet rs) throws DataException {
		if (rs.isEmpty())
			return null;

		Log.debug("Form get: Org ID = " + 
		 CCLAUtils.getResultSetIntegerValue(rs, SharedCols.ORG));
				
		return new Form(
			CCLAUtils.getResultSetIntegerValue(
					rs, SharedCols.FORM),
					FormType.getCache().getCachedInstance(
							CCLAUtils.getResultSetIntegerValue(rs, Cols.FORM_TYPE)
					),
			CCLAUtils.getResultSetIntegerValue(rs, Cols.FORM_STATUS),
			
			CCLAUtils.getResultSetIntegerValue(rs, SharedCols.CAMPAIGN_ENROLMENT),
			CCLAUtils.getResultSetIntegerValue(rs, SharedCols.CAMPAIGN_ACTIVITY),
			CCLAUtils.getResultSetIntegerValue(rs, SharedCols.PERSON),
			
			CCLAUtils.getResultSetStringValue(rs, Cols.GEN_DOC),
			CCLAUtils.getResultSetStringValue(rs, Cols.RET_DOC),
			rs.getDateValueByName(SharedCols.DATE_ADDED),
			rs.getDateValueByName(Cols.DATE_GENERATED),
			rs.getDateValueByName(Cols.DATE_UPLOADED),
			rs.getDateValueByName(Cols.DATE_PRINTED),
			rs.getDateValueByName(Cols.DATE_RETURNED),
			rs.getDateValueByName(SharedCols.LAST_UPDATED),
			rs.getStringValueByName(SharedCols.USER),
			
			CCLAUtils.getResultSetIntegerValue(rs, SharedCols.ORG),
			CCLAUtils.getResultSetIntegerValue(rs, Subscription.Cols.ID),
			CCLAUtils.getResultSetBoolValueAllowNull(rs, Cols.IS_VALID),
			
			rs.getDateValueByName(Cols.RETURN_DEADLINE_DATE),
			rs.getDateValueByName(Cols.CALCULATION_DATE)
		);
	}
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		CCLAUtils.addQueryIntParamToBinder(binder, SharedCols.FORM, this.getFormId());
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.FORM_TYPE, this.getFormType().getFormTypeId());
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.FORM_STATUS, this.getFormStatusId());
		CCLAUtils.addQueryIntParamToBinder(binder, SharedCols.CAMPAIGN_ENROLMENT, this.getCampaignEnrolmentId());
		CCLAUtils.addQueryIntParamToBinder(binder, SharedCols.CAMPAIGN_ACTIVITY, this.getCampaignActivityId());
		CCLAUtils.addQueryIntParamToBinder(binder, SharedCols.PERSON, this.getPersonId());
		CCLAUtils.addQueryParamToBinder(binder, Cols.GEN_DOC, this.getGenDocGuid());
		CCLAUtils.addQueryParamToBinder(binder, Cols.RET_DOC, this.getRetDocGuid());
		CCLAUtils.addQueryDateParamToBinder(binder, SharedCols.DATE_ADDED, this.getDateAdded());
		CCLAUtils.addQueryDateParamToBinder(binder, Cols.DATE_GENERATED, this.getDateGenerated());
		CCLAUtils.addQueryDateParamToBinder(binder, Cols.DATE_UPLOADED, this.getDateUploaded());
		CCLAUtils.addQueryDateParamToBinder(binder, Cols.DATE_PRINTED, this.getDatePrinted());
		CCLAUtils.addQueryDateParamToBinder(binder, Cols.DATE_RETURNED, this.getDateReturned());
		CCLAUtils.addQueryDateParamToBinder(binder, SharedCols.LAST_UPDATED, this.getLastUpdated());
		CCLAUtils.addQueryParamToBinder(binder, SharedCols.USER, this.getUserId());
		CCLAUtils.addQueryIntParamToBinder(binder, SharedCols.ORG, this.getOrganisationId());
		CCLAUtils.addQueryIntParamToBinder(binder, Subscription.Cols.ID, this.getSubscriptionId());
		CCLAUtils.addQueryBooleanParamToBinderAllowNull
		 (binder, Cols.IS_VALID, this.isValid());
		CCLAUtils.addQueryDateParamToBinder
		 (binder, Cols.RETURN_DEADLINE_DATE, this.getReturnDeadlineDate());
		CCLAUtils.addQueryDateParamToBinder
		 (binder, Cols.CALCULATION_DATE, this.getCalculationDate());
	}
	
	/** Fetches enrolment data by its enrolment ID.
	 * 
	 * @param accNumExt
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getData(int formId, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.ID, formId);
		return (facade.createResultSet("qCore_GetForm", binder));	
	}
	
	/**
	 * Gets the form extended data, includes status and type names
	 * @param formId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getExtendedData(int formId, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.ID, formId);
		return (facade.createResultSet("qCore_GetFormsExtended", binder));	
	}
		
	/**
	 * Get the latest form by form type id and investment with an returned document
	 * @param formTypeId
	 * @param investmentId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Form getFormByTypeAndSubscriptionWithReturnedDoc(int formTypeId, 
			int subscriptionId, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, Form.Cols.FORM_TYPE, formTypeId);
		CCLAUtils.addQueryIntParamToBinder(binder, Subscription.Cols.ID, subscriptionId);
		return get(facade.createResultSet("qCore_GetFormByTypeAndSubscriptionWithReturnedDoc", binder));	
	}
	
	/** Returns the newest/latest Form instance with the given Subscription ID.
	 *  
	 *  This is considered to be the only 'valid' form for a given subscription - any
	 *  other (older) forms are not guaranteed to hold the same data held on the
	 *  linked Subscription.
	 * 
	 *  Returns null if there are no forms with the passed Subscription ID.
	 *  
	 * @param subscriptionId
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static Form getLatestFormBySubscriptionId
	 (int subscriptionId, FWFacade facade) throws DataException {

		return get(getLatestFormDataBySubscriptionId(subscriptionId, facade));	
	}
	
	/** Returns the newest/latest Form ResultSet with the given Subscription ID.
	 *  
	 *  This is considered to be the only 'valid' form for a given subscription - any
	 *  other (older) forms are not guaranteed to hold the same data held on the
	 *  linked Subscription.
	 * 
	 *  Returns null if there are no forms with the passed Subscription ID.
	 *  
	 * @param subscriptionId
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getLatestFormDataBySubscriptionId
	 (int subscriptionId, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Subscription.Cols.ID, subscriptionId);
		return facade.createResultSet
		 ("qCore_GetLatestFormBySubscriptionId", binder);	
	}
	
	
	/** Returns the newest/latest Form instance of the given type, belonging to the given
	 *  Organisation, with the given Calculation Date.
	 *  
	 *  This is considered to be the only 'valid' form for a given form type/org/date
	 *  combination - any other (older) forms are not guaranteed to hold the most up-to-
	 *  date information.
	 *  
	 *  The passed calculation date is truncated when comparing with other form 
	 *  calculation dates (also truncated)
	 * 
	 *  Returns null if there are no forms that match the passed parameters.
	 *  
	 * @param subscriptionId
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static Form getLatestFormByTypeOrgAndCalculationDate
	 (int formTypeId, int orgId, Date calculationDate, FWFacade facade) 
	 throws DataException {

		return get(getLatestFormDataByTypeOrgAndCalculationDate
		 (formTypeId, orgId, calculationDate, facade));	
	}
	
	/** Returns the newest/latest Form ResultSet of the given type, belonging to the given
	 *  Organisation, with the given Calculation Date.
	 *  
	 *  This is considered to be the only 'valid' form for a given form type/org/date
	 *  combination - any other (older) forms are not guaranteed to hold the most up-to-
	 *  date information.
	 *  
	 *  The passed calculation date is truncated when comparing with other form 
	 *  calculation dates (also truncated)
	 * 
	 *  Returns null if there are no forms that match the passed parameters.
	 *  
	 * @param subscriptionId
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getLatestFormDataByTypeOrgAndCalculationDate
	 (int formTypeId, int orgId, Date calculationDate, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, FormType.Cols.ID, formTypeId);
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Entity.Cols.ID, orgId);
		CCLAUtils.addQueryDateParamToBinder
		 (binder, Form.Cols.CALCULATION_DATE, calculationDate);
		
		return facade.createResultSet
		 ("qCore_GetLatestFormDataByTypeOrgAndCalculationDate", binder);	
	}
	
	
	public void persist(FWFacade facade, String username) throws DataException {
		this.validate(facade);
				
		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);
		
		DataResultSet beforeData = Form.getData(this.getFormId(), facade);
		
		facade.execute("qCore_UpdateForm", binder);
		
		DataResultSet afterData = Form.getData(this.getFormId(), facade);
		
		// Add audit record
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(this.getFormId(), ElementType.SecondaryElementType.Form.toString());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.UPDATE.toString(), 
		 ElementType.SecondaryElementType.Form.toString(), 
		 beforeData, afterData, auditRelations);
	}
	
	public void setAttributes(DataBinder binder) throws DataException {
	
	}
	
	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub
		
	}

	public int getFormId() {
		return formId;
	}

	public void setFormId(int formId) {
		this.formId = formId;
	}

	public FormType getFormType() {
		return formType;
	}

	public void setFormType(FormType formType) {
		this.formType = formType;
	}

	public Integer getFormStatusId() {
		return formStatusId;
	}

	public void setFormStatusId(Integer formStatusId) {
		this.formStatusId = formStatusId;
	}

	public Integer getCampaignEnrolmentId() {
		return campaignEnrolmentId;
	}

	public void setCampaignEnrolmentId(Integer campaignEnrolmentId) {
		this.campaignEnrolmentId = campaignEnrolmentId;
	}

	public Integer getCampaignActivityId() {
		return campaignActivityId;
	}

	public void setCampaignActivityId(Integer campaignActivityId) {
		this.campaignActivityId = campaignActivityId;
	}

	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

	public void setGenDocGuid(String genDocGuid) {
		this.genDocGuid = genDocGuid;
	}

	public String getGenDocGuid() {
		return genDocGuid;
	}

	public void setRetDocGuid(String retDocGuid) {
		this.retDocGuid = retDocGuid;
	}

	public String getRetDocGuid() {
		return retDocGuid;
	}

	public Date getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}

	public Date getDateGenerated() {
		return dateGenerated;
	}

	public void setDateGenerated(Date dateGenerated) {
		this.dateGenerated = dateGenerated;
	}

	public Date getDateUploaded() {
		return dateUploaded;
	}

	public void setDateUploaded(Date dateUploaded) {
		this.dateUploaded = dateUploaded;
	}

	public Date getDatePrinted() {
		return datePrinted;
	}

	public void setDatePrinted(Date datePrinted) {
		this.datePrinted = datePrinted;
	}

	public Date getDateReturned() {
		return dateReturned;
	}

	public void setDateReturned(Date dateReturned) {
		this.dateReturned = dateReturned;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}

	public Integer getOrganisationId() {
		return organisationId;
	}
	
	public Integer getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(Integer investmentId) {
		this.subscriptionId = investmentId;
	}

	public static Vector<Contact> getContacts(int formId, FWFacade facade)
	 throws DataException {
		return FormContactApplied.getByFormId(formId, facade);
	}
	
	public FormHandler getHandlerInstance(String userId, FWFacade facade)
	 throws DataException {
		return this.getFormType().getHandlerInstance(this, userId, facade);
	}

	public Boolean isValid() {
		return valid;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
	}

	
	public Date getReturnDeadlineDate() {
		return returnDeadlineDate;
	}

	public void setReturnDeadlineDate(Date returnDeadlineDate) {
		this.returnDeadlineDate = returnDeadlineDate;
	}
	
	public Date getCalculationDate() {
		return calculationDate;
	}

	public void setCalculationDate(Date calculationDate) {
		this.calculationDate = calculationDate;
	}

}
