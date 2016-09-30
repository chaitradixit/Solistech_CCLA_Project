package com.ecs.stellent.ccla.signature.data;

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Audit;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.Globals;
import com.ecs.ucm.ccla.data.Persistable;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Models entries from the COMM_SIGNATURE table.
 *  
 *  This maps instruction IDs/UCM Document IDs to Person IDs. Each mapping indicates the
 *  person's signature was found on the instruction/document.
 * 
 * @author Tom
 *
 */
public class InstructionSignature implements Persistable {
	
	public static final String AUDIT_LABEL = "InstructionSignature";
	
	private int instructionSignatureId;
	
	private String docGuid;
	private Integer commId;
	private int personId;
	
	private MatchType matchType;
	private String userId;
	private Date dateAdded;
	
	public static enum MatchType {
		Match
	}
	
	/** Adds a new Instruction Signature for the given dDocName.
	 * 
	 * @param docName
	 * @param personId
	 * @param facade
	 * @param userName
	 * @throws DataException 
	 */
	public static void addByDocGuid(String docGuid, int personId, 
	 FWFacade facade, String userName) throws DataException {
		
		/* Create a new instance with an ID of zero. The actual ID will be pulled off a 
		 *  sequence in the 'add' query.
		 */
		InstructionSignature newSig = new InstructionSignature(0, null, 
		 personId, MatchType.Match, userName, null, docGuid);
		
		DataBinder binder = new DataBinder();
		newSig.addFieldsToBinder(binder);
		
		facade.execute("qSignatureChecking_AddApprovedSigForDoc", binder);

		// Add audit record
		DataResultSet newData = InstructionSignature.getDataByDocGuid
		 (docGuid, personId, facade);
		
		newSig = get(newData);
		
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(newSig.getInstructionSignatureId(), AUDIT_LABEL);

		//UNABLE TO AUDIT DOC_GUID AS IT IS A STRING
		//auditRelations.put(newSig.get(),
		//ElementType.SecondaryElementType.Document.toString());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, userName, 
		 Globals.AuditActions.ADD.toString(), 
		 AUDIT_LABEL, 
		 null, newData, auditRelations);
	}
	
	/** Removes an Instruction Signature for the given dDocName.
	 * 
	 * @param docName
	 * @param personId
	 * @param facade
	 * @param userName
	 * @throws DataException 
	 */
	public static void removeByDocGuid(String docGuid, int personId, 
	 FWFacade facade, String userName) throws DataException {
		
		DataResultSet oldData = getDataByDocGuid(docGuid, personId, facade); 
		InstructionSignature delSig = get(oldData);
		
		if (delSig == null)
			throw new DataException("Unable to remove doc signature (doesn't exist)");
		
		DataBinder binder = new DataBinder();
		delSig.addFieldsToBinder(binder);
		
		facade.execute("qSignatureChecking_DeleteApprovedSigForDoc", binder);

		// Add audit record
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(delSig.getInstructionSignatureId(), AUDIT_LABEL);
		
		//UNABLE TO AUDIT DOC_GUID AS IT IS A STRING
		//auditRelations.put(delSig.getDoc(),
		//ElementType.SecondaryElementType.Document.toString());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, userName, 
		 Globals.AuditActions.DELETE.toString(), 
		 AUDIT_LABEL, 
		 oldData, null, auditRelations);
	}
	
	
	
	
	public static InstructionSignature get(DataResultSet rs) throws DataException {
		if (rs.isEmpty())
			return null;
		
		return new InstructionSignature(
			CCLAUtils.getResultSetIntegerValue(rs, "COMM_SIGNATURE_ID"),
			CCLAUtils.getResultSetIntegerValue(rs, "COMM_ID"),
			CCLAUtils.getResultSetIntegerValue(rs, "PERSON_ID"),
			MatchType.valueOf(
				rs.getStringValueByName("MATCH_TYPE")
			),
			rs.getStringValueByName("USER_ID"),
			rs.getDateValueByName("DATE_ADDED"),
			rs.getStringValueByName("DOC_GUID")
		);
	}
	
	/** Fetches a single entry for the given dDocName and Person ID.
	 *  
	 *  Will be empty if no such mapping exists.
	 * 
	 * @param docName
	 * @param personId
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getDataByDocGuid(String docGuid, int personId, 
	 FWFacade facade) throws DataException {
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryParamToBinder(binder,"DOC_GUID", docGuid);
		CCLAUtils.addQueryIntParamToBinder(binder, "PERSON_ID", personId);
		
		return facade.createResultSet
		 ("qSignatureChecking_GetApprovedSigForDoc", binder);
	}
	
	/** Fetches all mapped Instruction Signature instances for the given doc ID.
	 * 
	 * @return
	 * @throws DataException 
	 */
	public static Vector<InstructionSignature> getAllByDocGuid
	 (String docGuid, FWFacade facade) throws DataException {
		
		Vector<InstructionSignature> sigs = new Vector<InstructionSignature>();
		DataResultSet rs = getAllDataByDocGuid(docGuid, facade);
		
		if (rs.first()) {
			do {
				sigs.add(get(rs));
			} while (rs.next());
		}
		
		return sigs;
	} 
	
	/** Fetches all mapped Instruction Signature data for the given doc ID.
	 * 
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getAllDataByDocGuid(String docGuid, FWFacade facade) 
	 throws DataException {
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryParamToBinder(binder, "DOC_GUID", docGuid);
		
		return facade.createResultSet
		 ("qSignatureChecking_GetApprovedSigsForDoc", binder);
	}
	
	public InstructionSignature(int instructionSignatureId,
			Integer commId, int personId, MatchType matchType, String userId,
			Date dateAdded, String docGuid) {
		this.instructionSignatureId = instructionSignatureId;
		this.commId = commId;
		this.personId = personId;
		this.matchType = matchType;
		this.userId = userId;
		this.dateAdded = dateAdded;
		this.docGuid = docGuid;
	}
	
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		// TODO Auto-generated method stub
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "COMM_SIGNATURE_ID", this.getInstructionSignatureId());
		CCLAUtils.addQueryIntParamToBinder(binder, "COMM_ID", this.getCommId());
		CCLAUtils.addQueryIntParamToBinder(binder, "PERSON_ID", this.getPersonId());
		CCLAUtils.addQueryParamToBinder(binder, "MATCH_TYPE", this.getMatchType().toString());
		CCLAUtils.addQueryParamToBinder(binder, "USER_ID", this.getUserId());
		CCLAUtils.addQueryDateParamToBinder(binder, "DATE_ADDED", this.getDateAdded());
		CCLAUtils.addQueryParamToBinder(binder, "DOC_GUID", this.getDocGuid());
		
	}

	public String getDocGuid() {
		return docGuid;
	}

	public void setDocGuid(String docGuid) {
		this.docGuid = docGuid;
	}

	public void persist(FWFacade facade, String username) throws DataException {
		throw new DataException("Not implemented");
	}

	public void setAttributes(DataBinder binder) throws DataException {
		throw new DataException("Not implemented");
	}

	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub
	}

	public int getInstructionSignatureId() {
		return instructionSignatureId;
	}

	public void setInstructionSignatureId(int instructionSignatureId) {
		this.instructionSignatureId = instructionSignatureId;
	}

	public Integer getCommId() {
		return commId;
	}

	public void setCommId(Integer commId) {
		this.commId = commId;
	}

	public int getPersonId() {
		return personId;
	}

	public void setPersonId(int personId) {
		this.personId = personId;
	}

	public MatchType getMatchType() {
		return matchType;
	}

	public void setMatchType(MatchType matchType) {
		this.matchType = matchType;
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

}
