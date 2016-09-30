package com.ecs.ucm.ccla.data.audit;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.util.Date;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Element;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.Relation;
import com.ecs.ucm.ccla.data.RelationName;
import com.ecs.ucm.ccla.data.RelationProperty;
import com.ecs.ucm.ccla.data.RelationType;
import com.ecs.utils.stellent.embedded.FWFacade;

public class SDAudit {
	
	public static class Cols {
		public static final String AUDITID = "AUDITID";
		public static final String AUDIT_DATE = "AUDIT_DATE";
		public static final String ACTION = "ACTION";
		public static final String EVENT = "EVENT";
		public static final String USERID = "USERID";
	}
	
	public enum AuditAction {
		ADD,
		UPDATE,
		DELETE
	}
	
	private int auditId;
	private Date auditDate;
	private AuditAction action;
	private String event;
	private String userId;
	
	public SDAudit(int auditId, Date auditDate, AuditAction action,
			String event, String userId) {
		super();
		this.auditId = auditId;
		this.auditDate = auditDate;
		this.action = action;
		this.event = event;
		this.userId = userId;
	}

	/** Fetches the most recent SDAudit entry for the given Event Type and Relation ID
	 * 
	 * @return
	 * @throws DataException 
	 */
	public static SDAudit getLatestSDAuditByEventAndRelationId
	 (String eventName, int relationId, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, SDAuditRelation.Cols.RELATIONID, relationId);
		CCLAUtils.addQueryParamToBinder
		 (binder, SDAudit.Cols.EVENT, eventName);
		CCLAUtils.addQueryParamToBinder
		 (binder, SDAuditRelation.Cols.RELATIONTYPE, eventName);

		DataResultSet rs = 
		 facade.createResultSet("qCore_GetLatestSDAuditByEventAndRelationId", binder);
		
		return get(rs);
	}
	
	public static SDAudit get(DataResultSet rs) throws DataException {
		
		if (rs.isEmpty())
			return null;
		
		return new SDAudit(
			CCLAUtils.getResultSetIntegerValue(rs, Cols.AUDITID),
			rs.getDateValueByName(Cols.AUDIT_DATE),
			AuditAction.valueOf(rs.getStringValueByName(Cols.ACTION)),
			rs.getStringValueByName(Cols.EVENT),
			rs.getStringValueByName(Cols.USERID)
		);
	}

	/** Returns the latest Audit event for the addition or removal of a relation with
	 *  the given Relation Name ID and associated Element ID.
	 *  
	 *  So if the element ID corresponded to 'Org ABC' and relation name corresponded to
	 *  'Signatory' this would return the latest time a Signatory relation was added or
	 *  removed against Org ABC.
	 *  
	 *  Warning: Extremely slow query, can take minutes to run due to increasing size
	 *  of SDAUDIT_DATA table. TODO need a way of caching this information for faster
	 *  access.
	 *  
	 * @param elementId
	 * @param relName
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static SDAudit getLatestRelationChangeByElementAndRelationName
	 (int elementId, RelationName relName, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryParamToBinder
		 (binder, SDAudit.Cols.EVENT, ElementType.SecondaryElementType.Relation.toString());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Element.Cols.ID, elementId);
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, RelationName.Cols.ID, relName.getRelationNameId());
		
		// Infer whether we need to target ELEMENT_ID1 or ELEMENT_ID2
		String elementIdCol = null;
		
		Element elem = Element.get(elementId, facade);

		if (relName.getRelationType().getElement1Type().equals(elem.getType()))
			elementIdCol = Relation.Cols.ELEMENT_ID1;
		else
			elementIdCol = Relation.Cols.ELEMENT_ID2;
		
		CCLAUtils.addQueryParamToBinder(binder, "ELEMENT_ID_COL", elementIdCol);
		
		DataResultSet rs = facade.createResultSet
		 ("qCore_GetLatestRelationChangeByElementAndRelationName", binder);
		
		return get(rs);
	}
	
	/** Returns the latest Audit event for the removal of an Applied Relation Property
	 *  with the given Relation Property ID, against the given Element ID.
	 *  
	 *  Useful for determining the last time a nominated property on a withdrawal/income 
	 *  bank account relation was removed.
	 *  
	 * @param elementId
	 * @param relName
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static SDAudit getLatestRelationPropertyDeletionByElementId
	 (int elementId, int relationPropertyId, FWFacade facade) throws DataException {
	
		Element elem = Element.get(elementId, facade);
		
		if (elem == null)
			return null; // Maybe should throw error here, we'll play it safe and 
						// return nothing instead.
		
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Element.Cols.ID, elementId);
		CCLAUtils.addQueryParamToBinder
		 (binder, SDAuditRelation.Cols.RELATIONTYPE, elem.getType().getName());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, RelationProperty.Cols.ID, relationPropertyId);
		
		DataResultSet rs = facade.createResultSet
		 ("qCore_GetLatestRelationPropertyDeletionByElementId", binder);
		
		return get(rs);
	}
	
	public int getAuditId() {
		return auditId;
	}

	public Date getAuditDate() {
		return auditDate;
	}

	public AuditAction getAction() {
		return action;
	}

	public String getEvent() {
		return event;
	}

	public String getUserId() {
		return userId;
	}
}
