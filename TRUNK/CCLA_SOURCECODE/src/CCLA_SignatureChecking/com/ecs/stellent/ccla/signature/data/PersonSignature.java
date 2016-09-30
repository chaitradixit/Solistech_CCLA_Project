package com.ecs.stellent.ccla.signature.data;

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.shared.SharedObjects;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Audit;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.Globals;
import com.ecs.ucm.ccla.data.Persistable;
import com.ecs.ucm.ccla.data.Relation;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Represents entries in the PERSON_SIGNATURE table.
 * 
 * @author Tom
 *
 */
public class PersonSignature implements Persistable {

	public static final String AUDIT_LABEL = "PersonSignature";
	
	/* Maximum allowed dimensions for signature images. */
	public static Integer MAX_IMAGE_HEIGHT = 
	 SharedObjects.getEnvironmentInt("CCLA_SC_MaxSigHeight", 0);
	
	public static Integer MAX_IMAGE_WIDTH = 
	 SharedObjects.getEnvironmentInt("CCLA_SC_MaxSigWidth", 0);
	
	/** Determines whether Source Doc ID must be specified when a signature image is
	 *  captured/updated.
	 */
	public static boolean REQUIRE_SOURCE_DOC_ID =
	 !StringUtils.stringIsBlank(
	 SharedObjects.getEnvironmentValue("CCLA_SC_RequireSourceDocId"));
	
	/** Determines whether signatures images can only be captured from non-transaction
	 *  document classes.
	 * 
	 */
	public static boolean PREVENT_TRANSACTION_CAPTURE =
	 !StringUtils.stringIsBlank(
	 SharedObjects.getEnvironmentValue("CCLA_SC_PreventTransactionCapture"));
	
	private int personId;
	private String sigDocGuid;
	private String sourceDocGuid;
	private String userId;
	
	/** The originating dID where the signature was apparently captured from.
	 *  Won't be present on older person signature entries.
	 */
	
	private Date dateAdded;
	private Date lastUpdated;

	/** Adds a new PersonSignature entry to the database.
	 * 
	 * @param personId
	 * @param docName
	 * @param userId
	 * @param facade
	 * @return
	 * @throws DataException
	 * @throws ServiceException 
	 */
	public static PersonSignature add(int personId, String docName, Integer sourceDocId,
	 String userId, FWFacade facade) throws DataException, ServiceException {
		Log.info("Person Signature: add >>");
		
		String sourceDocGuid = null;
		String sigDocGuid = null;

		try {
			sourceDocGuid = CCLAUtils.getDocGuidFromDid(sourceDocId);
			sigDocGuid = CCLAUtils.getDocGuidFromRawValues(docName, 1);
		} catch (Exception e) {
			String err = "add personSignature: unable to get sourceDocGuid: " + e.getMessage();
			Log.error(err);
			throw new ServiceException(err);
		}
		
		
		
		// Create new PersonSignature instance. Date values can be null, they
		// will be set by the database on insert.
		PersonSignature personSignature = new PersonSignature(
			personId,
			sigDocGuid,
			userId,
			null,
			null, 
			sourceDocGuid
		);
		
		DataBinder binder = new DataBinder();
		personSignature.addFieldsToBinder(binder);
		personSignature.validate(facade);
		
		// If there is already a mapped signature, a database error will be thrown.
		facade.execute("qSignatureChecking_AddPersonSignature", binder);
		
		// Add audit record
		DataResultSet newData = PersonSignature.getData
		 (personSignature.getPersonId(), facade);
		
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(personSignature.getPersonId(), ElementType.PERSON.getName());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, userId, 
		 Globals.AuditActions.ADD.toString(), 
		 AUDIT_LABEL, 
		 null, newData, auditRelations);
		
		return get(newData);
	}
	
	/** Updates an existing PersonSignature in the database.
	 * 
	 * @param personId
	 * @param userId
	 * @param facade
	 * @throws DataException 
	 * @throws ServiceException 
	 */
	public static void update
	 (int personId, Integer sourceDocId, String userId, FWFacade facade) 
	 throws DataException, ServiceException {
		
		Log.info("Person Signature: update >>");
		
		PersonSignature signature = get(personId, facade);
		
		if (signature == null)
			throw new DataException("No Signature found in database for " +
			 "Person ID: " + personId);
		
		signature.setUserId(userId);
		
		try {
			signature.setSouceDocGuid(CCLAUtils.getDocGuidFromDid(sourceDocId));
		} catch (Exception e) {
			String err = "update personSignature: unable to set docGuid: " + e.getMessage();
			Log.error(err);
			throw new ServiceException(err);
		}
		signature.persist(facade, userId);
	}
	
	/** Removes an existing PersonSignature entry.
	 * 
	 * @param personId
	 * @param facade
	 * @throws DataException 
	 */
	public static void remove(int personId, FWFacade facade, String userName) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, "PERSON_ID", personId);
	
		DataResultSet oldData = getData(personId, facade);
		
		facade.execute("qSignatureChecking_RemovePersonSignature", binder);
		
		// Add audit record
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(personId, ElementType.PERSON.getName());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, userName, 
		 Globals.AuditActions.DELETE.toString(), 
		 AUDIT_LABEL, 
		 oldData, null, auditRelations);
	}
	
	/**
	 * Returns a PersonSignature instance for the given Person ID. If no mapped
	 * Person Signature exists, returns null.
	 * 
	 * @param personId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static PersonSignature get(int personId, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, "PERSON_ID", personId);
		
		DataResultSet rsPersonSignature = facade.createResultSet
		 ("qSignatureChecking_GetPersonSignature", binder);
		
		return get(rsPersonSignature);
	}
	
	/** Returns a corresponding entry from the PERSON_SIGNATURE table for the given
	 *  PERSON_ID.
	 *  
	 * @param personId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getData(int personId, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, "PERSON_ID", personId);
		
		return facade.createResultSet
		 ("qSignatureChecking_GetPersonSignature", binder);
	}
	
	public static PersonSignature get(DataResultSet rs) throws DataException {
		if (rs.isEmpty())
			return null;
		
		return new PersonSignature(
			CCLAUtils.getResultSetIntegerValue(rs, "PERSON_ID"),
			rs.getStringValueByName("SIG_DOC_GUID"),
			rs.getStringValueByName("USER_ID"),
			rs.getDateValueByName("DATE_ADDED"),
			rs.getDateValueByName("LAST_UPDATED"),
			rs.getStringValueByName("SOURCE_DOC_GUID")
		);
	}
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		CCLAUtils.addQueryIntParamToBinder(binder, "PERSON_ID", this.getPersonId());
		CCLAUtils.addQueryParamToBinder(binder,"SIG_DOC_GUID", this.getSigDocGuid());
		CCLAUtils.addQueryParamToBinder(binder, "SOURCE_DOC_GUID", this.getSourceDocGuid());
		CCLAUtils.addQueryParamToBinder(binder,"USER_ID", this.getUserId());
	}

	public void persist(FWFacade facade, String username) throws DataException {
		DataBinder binder = new DataBinder();
		
		this.addFieldsToBinder(binder);
		this.validate(facade);
		
		DataResultSet oldData = getData(this.getPersonId(), facade);
		
		Log.debug("Updating: " + this.toString());
		
		facade.execute("qSignatureChecking_UpdatePersonSignature", binder);
		
		// Add audit record
		DataResultSet newData = getData(this.getPersonId(), facade);
		
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(this.getPersonId(), ElementType.PERSON.getName());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.UPDATE.toString(), 
		 AUDIT_LABEL, 
		 oldData, newData, auditRelations);
	}

	public void setAttributes(DataBinder binder) throws DataException {
		throw new DataException("Not implemented");
	}

	public void validate(FWFacade facade) throws DataException {
		if (REQUIRE_SOURCE_DOC_ID && this.getSourceDocGuid() == null) {
			throw new DataException("Source Doc GUID missing");
		}
	}

	public int getPersonId() {
		return personId;
	}


	public void setPersonId(int personId) {
		this.personId = personId;
	}



	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
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

	public void setSouceDocGuid(String docGuid) {
		this.sourceDocGuid = docGuid;
	}

	public String getSourceDocGuid() {
		return sourceDocGuid;
	}

	public void setSigDocGuid(String sigDocGuid) {
		this.sigDocGuid = sigDocGuid;
	}

	public String getSigDocGuid() {
		return sigDocGuid;
	}
	

	public PersonSignature(int personId, String sigDocGuid, String userId, 
	 Date dateAdded, Date lastUpdated, String sourceDocGuid) {
		this.personId = personId;
		this.sigDocGuid = sigDocGuid;
		this.userId = userId;
		this.dateAdded = dateAdded;
		this.lastUpdated = lastUpdated;
		this.sourceDocGuid = sourceDocGuid;
	}


	@Override
	public String toString() {
		return "PersonSignature [dateAdded=" + dateAdded + ", lastUpdated="
				+ lastUpdated + ", personId=" + personId + ", sigDocGuid="
				+ sigDocGuid + ", sourceDocGuid=" + sourceDocGuid + ", userId="
				+ userId + "]";
	}


}
