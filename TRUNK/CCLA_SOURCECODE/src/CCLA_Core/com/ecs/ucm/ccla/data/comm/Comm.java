package com.ecs.ucm.ccla.data.comm;

import java.util.Date;
import java.util.HashMap;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Audit;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.Globals;
import com.ecs.ucm.ccla.data.Persistable;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.SharedCols;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Models entries in the COMM table.
 * 
 * @author Tom
 *
 */
public class Comm implements Persistable {
	
	public static final CommStatus DEFAULT_COMM_STATUS = CommStatus.READY;
	
	private int commId;
	
	private CommSource source;
	private CommType type;
	private CommStatus status;
	
	private Integer personId;
	private Integer organisationId;

	private Date datedAdded;
	private String createdBy;
	
	private String docGuid;
	private Integer interactionId;
	
	private Integer groupId;
	
	
	public Comm(int commId, CommSource source, CommType type,
			CommStatus status, Integer personId, Integer organisationId,
			Date datedAdded, String createdBy, String docGuid,
			Integer interactionId, Integer groupId) {
		this.commId = commId;
		this.source = source;
		this.type = type;
		this.status = status;
		this.personId = personId;
		this.organisationId = organisationId;
		this.datedAdded = datedAdded;
		this.createdBy = createdBy;
		this.docGuid = docGuid;
		this.interactionId = interactionId;
		this.groupId = groupId;
	}
	
	/** Creates a new Comm entry in the database.
	 * 
	 *  Source, type, dateAdded and createdBy are all required fields.
	 *  
	 *  The dateAdded date should correspond to the original creation date of the
	 *  source document/interaction. 
	 *  
	 * @param source
	 * @param type
	 * @param personId
	 * @param organisationId
	 * @param datedAdded
	 * @param createdBy
	 * @param docGuid
	 * @param interactionId
	 * @param groupId
	 * @param facade
	 * @param userName
	 * @return
	 * @throws NumberFormatException
	 * @throws DataException
	 */
	public static Comm add(CommSource source, CommType type,
			Integer personId, Integer organisationId,
			Date dateAdded, String createdBy, String docGuid,
			Integer interactionId, Integer groupId, FWFacade facade, String userName) 
	 		throws NumberFormatException, DataException {
		
		int newCommId = Integer.parseInt(CCLAUtils.getNewKey("Comm", facade));
		
		Comm comm = new Comm(newCommId, source, type, DEFAULT_COMM_STATUS, 
		 personId, organisationId, dateAdded, createdBy, docGuid, interactionId,
		 groupId);
		
		comm.validate(facade);
		
		DataBinder binder = new DataBinder();
		comm.addFieldsToBinder(binder);
		
		facade.execute("qCore_AddComm", binder);
		
		// Add audit record
		DataResultSet newData = Comm.getData(newCommId, facade);
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(newCommId, ElementType.SecondaryElementType.Comm.toString());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, userName, 
		 Globals.AuditActions.ADD.toString(), 
		 ElementType.SecondaryElementType.Comm.toString(), 
		 null, newData, auditRelations);
		
		return get(newCommId, facade);
	}
	
	public static Comm get(int commId, FWFacade facade) throws DataException {
		return get(getData(commId, facade));
	}
	
	public static DataResultSet getData(int commId, FWFacade facade) 
	 throws DataException {
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder(binder, SharedCols.COMM, commId);
		return facade.createResultSet("qCore_GetComm", binder);
	}
	
	/** Returns any matching Comm entries for the given UCM dID.
	 * 
	 * @param docId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getDataByDocGuid(String docGuid, FWFacade facade) 
	 throws DataException {
		DataBinder binder = new DataBinder();
		
		//TODO should it pass in DocGuid or convert from DID)
		CCLAUtils.addQueryParamToBinder(binder, SharedCols.GUID, docGuid);
		return facade.createResultSet("qCore_GetCommByDocGuid", binder);
	}
	
	/** Returns any matching Comm entries for the given UCM dID and Organisation ID.
	 * 
	 * @param docId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getDataByDocGuidAndOrgId
	 (String docGuid, Integer orgId, FWFacade facade) throws DataException {
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryParamToBinder(binder, SharedCols.GUID, docGuid);
		CCLAUtils.addQueryIntParamToBinder(binder, SharedCols.ORG, orgId);
		
		return facade.createResultSet("qCore_GetCommByDocGuidAndOrgId", binder);
	}
	
	
	public static Comm get(DataResultSet rs) throws DataException {
		if (rs.isEmpty())
			return null;
		
		return new Comm(
			CCLAUtils.getResultSetIntegerValue(rs, SharedCols.COMM),
			
			CommSource.getCache().getCachedInstance(
				CCLAUtils.getResultSetIntegerValue(rs, "COMM_SOURCE_ID")	
			),
			CommType.getCache().getCachedInstance(
				CCLAUtils.getResultSetIntegerValue(rs, "COMM_TYPE_ID")	
			),
			CommStatus.getCache().getCachedInstance(
				CCLAUtils.getResultSetIntegerValue(rs, "COMM_STATUS_ID")	
			),
			
			CCLAUtils.getResultSetIntegerValue(rs, SharedCols.PERSON),
			CCLAUtils.getResultSetIntegerValue(rs, SharedCols.ORG),
			
			rs.getDateValueByName(SharedCols.DATE_ADDED),
			rs.getStringValueByName("CREATED_BY"),
			
			CCLAUtils.getResultSetStringValue(rs, SharedCols.GUID),
			CCLAUtils.getResultSetIntegerValue(rs, "INTERACTION_ID"),
			
			CCLAUtils.getResultSetIntegerValue(rs, "COMM_GROUP_ID")
		);
	}
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {

		CCLAUtils.addQueryIntParamToBinder(binder, SharedCols.COMM, this.getCommId());
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "COMM_SOURCE_ID", this.getSource().getCommSourceId());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "COMM_TYPE_ID", this.getType().getCommTypeId());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "COMM_STATUS_ID", this.getStatus().getCommStatusId());
		CCLAUtils.addQueryDateParamToBinder(
				binder, "DATE_ADDED", this.getDatedAdded());
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, SharedCols.PERSON, this.getPersonId());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, SharedCols.ORG, this.getOrganisationId());
		
		CCLAUtils.addQueryParamToBinder
		 (binder, SharedCols.GUID, this.getDocGuid());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "INTERACTION_ID", this.getInteractionId());
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "COMM_GROUP_ID", this.getGroupId());
		
		CCLAUtils.addQueryParamToBinder
		 (binder, "CREATED_BY", this.getCreatedBy());
	}

	public void persist(FWFacade facade, String username) throws DataException {

		this.validate(facade);
		
		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);
		
		DataResultSet beforeData = Comm.getData(this.getCommId(), facade);
		
		facade.execute("qCore_UpdateComm", binder);
		
		// Update: audit
		DataResultSet newData = Comm.getData(this.getCommId(), facade);
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(this.getCommId(), ElementType.SecondaryElementType.Comm.toString());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.UPDATE.toString(), 
		 ElementType.SecondaryElementType.Comm.toString(), 
		 beforeData, newData, auditRelations);
		
	}

	public void setAttributes(DataBinder binder) throws DataException {
		throw new DataException("Not implemented");

	}

	public void validate(FWFacade facade) throws DataException {

		if (this.getSource() == null)
			throw new DataException("Communication Source missing");
		
		if (this.getType() == null)
			throw new DataException("Communication Type missing");
		
		if (this.getStatus() == null)
			throw new DataException("Communication Status missing");
		
		if (this.getDatedAdded() == null)
			throw new DataException("Communication Date missing");
	}

	public int getCommId() {
		return commId;
	}

	public void setCommId(int commId) {
		this.commId = commId;
	}

	public CommSource getSource() {
		return source;
	}

	public void setSource(CommSource source) {
		this.source = source;
	}

	public CommType getType() {
		return type;
	}

	public void setType(CommType type) {
		this.type = type;
	}

	public CommStatus getStatus() {
		return status;
	}

	public void setStatus(CommStatus status) {
		this.status = status;
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

	public Date getDatedAdded() {
		return datedAdded;
	}

	public void setDatedAdded(Date datedAdded) {
		this.datedAdded = datedAdded;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getDocGuid() {
		return docGuid;
	}

	public void setDocGuid(String docGuid) {
		this.docGuid = docGuid;
	}

	public Integer getInteractionId() {
		return interactionId;
	}

	public void setInteractionId(Integer interactionId) {
		this.interactionId = interactionId;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	@Override
	public String toString() {
		return "Comm [commId=" + commId + ", createdBy=" + createdBy
				+ ", docGuid=" + docGuid + ", groupId=" + groupId
				+ ", interactionId=" + interactionId 
				+ ", organisationId=" + organisationId 
				+ ", source=" + source
				+ ", status=" + status
				+ ", type=" + type + "]";
	}
	
	public boolean equals(Comm comm) {
		return (this.getCommId() == comm.getCommId());
	}
}
