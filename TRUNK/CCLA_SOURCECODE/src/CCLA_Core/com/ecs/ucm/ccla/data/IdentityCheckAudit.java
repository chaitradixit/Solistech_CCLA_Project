package com.ecs.ucm.ccla.data;

import intradoc.data.DataBinder;
import intradoc.data.DataException;

import java.util.Date;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Models entries in the IDENTITY_CHECK_AUDIT table.
 *  
 *  Used to audit authentication/validation requests which use the Experian web
 *  services.
 *  
 *  Audit entries can only be added, and never updated or removed.
 *  
 *  No fetch methods exist currently as there's no need for them.
 *  
 * @author Tom
 *
 */
public class IdentityCheckAudit implements Persistable {
	
	private int auditCheckId;
	private Integer personId;
	private CheckType checkType;
	private String userId;
	private Date checkDate;
	private String checkData;
	private String decision;
	private String refNo;
	
	public enum CheckType {
		/** Experian Authenticate Pro check */
		Experian,
		/** Experian Validator Plus check */
		Validate,
		/** CCLA internal validation check */
		CCLA,
		/** Manual check */
		Manual
	}
	
	public static class Cols {
		public static final String ID 			= "AUDIT_CHECK_ID";
		public static final String CHECK_TYPE 	= "CHECK_TYPE";
		public static final String USER_ID 		= "USER_ID";
		public static final String CHECK_DATE 	= "CHECK_DATE";
		public static final String CHECK_DATA 	= "CHECK_DATA";
		public static final String DECISION 	= "DECISION";
		public static final String REF_NO 		= "REF_NO";
	}
	
	/** Adds a new Identity Check Audit.
	 * 
	 * @param personId
	 * @param checkType
	 * @param userId
	 * @param checkData
	 * @param decision
	 * @param refNo
	 * @param facade
	 * @throws DataException 
	 * @throws NumberFormatException 
	 */
	public static void add(Integer personId, CheckType checkType, String userId, 
	 String checkData, String decision, String refNo, FWFacade facade)
	 throws DataException {
		
		int auditCheckId = Integer.parseInt(
		 CCLAUtils.getNewKey("IdentityCheckAudit", facade));
		
		IdentityCheckAudit identityCheckAudit = new IdentityCheckAudit(
			auditCheckId,
			personId,
			checkType,
			userId,
			new Date(),
			checkData,
			decision,
			refNo
		);
		
		identityCheckAudit.validate(facade);
		
		DataBinder binder = new DataBinder();
		identityCheckAudit.addFieldsToBinder(binder);
		
		facade.execute("qClientServices_AddIdentityCheckAudit", binder);
	}
	
	public IdentityCheckAudit(int auditCheckId, Integer personId,
			CheckType checkType, String userId, Date checkDate,
			String checkData, String decision, String refNo) {
		this.auditCheckId = auditCheckId;
		this.personId = personId;
		this.checkType = checkType;
		this.userId = userId;
		this.checkDate = checkDate;
		this.checkData = checkData;
		this.decision = decision;
		this.refNo = refNo;
	}

	public void addFieldsToBinder(DataBinder binder) throws DataException {

		CCLAUtils.addQueryIntParamToBinder(binder, Cols.ID, this.getAuditCheckId());
		CCLAUtils.addQueryIntParamToBinder(binder, SharedCols.PERSON, this.getPersonId());
		
		CCLAUtils.addQueryParamToBinder(binder, Cols.CHECK_TYPE, this.getCheckType().toString());
		CCLAUtils.addQueryParamToBinder(binder, Cols.USER_ID, this.getUserId());
		CCLAUtils.addQueryDateParamToBinder(binder, Cols.CHECK_DATE, this.getCheckDate());
		CCLAUtils.addQueryParamToBinder(binder, Cols.CHECK_DATA, this.getCheckData());
		CCLAUtils.addQueryParamToBinder(binder, Cols.DECISION, this.getDecision());
		CCLAUtils.addQueryParamToBinder(binder, Cols.REF_NO, this.getRefNo());
	}

	public void persist(FWFacade facade, String username) throws DataException {
		throw new DataException("Not implemented");
	}

	public void setAttributes(DataBinder binder) throws DataException {
		throw new DataException("Not implemented");
	}

	public void validate(FWFacade facade) throws DataException {
		if (this.getCheckType() == null)
			throw new DataException("Check Type missing");
		
		if (this.getUserId() == null)
			throw new DataException("User ID missing");
		
		if (this.getPersonId() == null)
			throw new DataException("Person ID missing");
	}

	public int getAuditCheckId() {
		return auditCheckId;
	}

	public void setAuditCheckId(int auditCheckId) {
		this.auditCheckId = auditCheckId;
	}

	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

	public CheckType getCheckType() {
		return checkType;
	}

	public void setCheckType(CheckType checkType) {
		this.checkType = checkType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public String getCheckData() {
		return checkData;
	}

	public void setCheckData(String checkData) {
		this.checkData = checkData;
	}

	public String getDecision() {
		return decision;
	}

	public void setDecision(String decision) {
		this.decision = decision;
	}

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}
}
