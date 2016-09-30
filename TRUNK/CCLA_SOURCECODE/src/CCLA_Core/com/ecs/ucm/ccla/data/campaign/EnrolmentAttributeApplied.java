package com.ecs.ucm.ccla.data.campaign;

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
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Models entries from the ENROLMENT_ATTRIBUTE_APPLIED table.
 * 
 * @author Tom
 *
 */
public class EnrolmentAttributeApplied implements Persistable {
	
	public static class Cols {
		public static final String ID 				= "ENROLATTRAPPL_ID";
		public static final String APPLICABLE_ENROLMENT_ATTRIBUTE_ID 	
													= "APPLENROLATTR_ID";
		public static final String CAMPAIGN_ENROLMENT_ID 
													= "CAMPAIGN_ENROLMENT_ID";
		public static final String VALUE 			= "ATTRIBUTE_VALUE";
	}
	
	private int id;
	private ApplicableEnrolmentAttribute applicableEnrolmentAttribute;
	private int campaignEnrolmentId;
	private String value;
	
	public EnrolmentAttributeApplied(int id,
	 ApplicableEnrolmentAttribute applicableEnrolmentAttribute, 
	 int campaignEnrolmentId, String value) {
		this.id = id;
		this.applicableEnrolmentAttribute = applicableEnrolmentAttribute;
		this.setCampaignEnrolmentId(campaignEnrolmentId);
		this.value = value;
	}
	
	public static EnrolmentAttributeApplied add
	 (int applicableEnrolmentAttributeId,
	 int campaignEnrolmentId, String value, 
     FWFacade facade, String userName) throws NumberFormatException, DataException {
		
		DataBinder binder = new DataBinder();
		
		int newId = Integer.parseInt(
		 CCLAUtils.getNewKey("EnrolmentAttributeApplied", facade));
		
		EnrolmentAttributeApplied attribAppl = new EnrolmentAttributeApplied
		 (newId,
		 ApplicableEnrolmentAttribute.getCache().getCachedInstance
		  (applicableEnrolmentAttributeId), 
		 campaignEnrolmentId, value);
		
		attribAppl.addFieldsToBinder(binder);
		
		facade.execute("qCore_AddEnrolmentAttributeApplied", binder);
		
		DataResultSet afterData = getData(newId, facade);
		
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put
		 (newId, ElementType.SecondaryElementType.EnrolmentAttributeApplied.toString());
		auditRelations.put
		 (attribAppl.getCampaignEnrolmentId(), 
		 ElementType.SecondaryElementType.CampaignEnrolment.toString());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, userName, 
		 Globals.AuditActions.UPDATE.toString(), 
		 ElementType.SecondaryElementType.ElementIdentifierApplied.toString(), 
		 null, afterData, auditRelations);
		
		return attribAppl;
	}
	
	public static void remove(int id, String userName, FWFacade facade)
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.ID, id);

		DataResultSet beforeData = getData(id, facade);
		
		if (beforeData.isEmpty())
			throw new DataException("Unable to remove Applied Enrolment Attribute " +
			 "with ID: " + id + " (not found)");
		
		EnrolmentAttributeApplied attribAppl = get(beforeData);
		
		facade.execute("qCore_RemoveEnrolmentAttributeApplied", binder);
		
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		
		auditRelations.put
		 (id, ElementType.SecondaryElementType.EnrolmentAttributeApplied.toString());
		
		auditRelations.put
		 (attribAppl.getCampaignEnrolmentId(), 
		 ElementType.SecondaryElementType.CampaignEnrolment.toString());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, userName, 
		 Globals.AuditActions.DELETE.toString(), 
		 ElementType.SecondaryElementType.ElementIdentifierApplied.toString(), 
		 beforeData, null, auditRelations);
	}
	
	public static EnrolmentAttributeApplied get
	 (int id, FWFacade facade) throws DataException {
		
		DataResultSet rs = getData(id, facade);
		
		if (rs.isEmpty())
			return null;
		else 
			return get(rs);
	}
	
	public static Vector<EnrolmentAttributeApplied> getByEnrolmentId
	 (int campaignEnrolmentId, FWFacade facade) throws DataException {
		
		return getAll(getDataByEnrolmentId(campaignEnrolmentId, facade));
	}
	
	public static DataResultSet getDataByEnrolmentId
	 (int campaignEnrolmentId, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.CAMPAIGN_ENROLMENT_ID, campaignEnrolmentId);
		
		return facade.createResultSet
		 ("qCore_GetEnrolmentAttributeAppliedByEnrolmentId", binder);
	}
	
	public static Vector<EnrolmentAttributeApplied> 
	 getAll(DataResultSet rs) throws DataException {
		
		Vector<EnrolmentAttributeApplied> attribsApplied = 
		 new Vector<EnrolmentAttributeApplied>();
		
		if (rs.first()) {
			do {
				attribsApplied.add(get(rs));
			} while (rs.next());
		}
		
		return attribsApplied;
	}
	
	public static DataResultSet getData(int id, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.ID, id);
		
		return facade.createResultSet("qCore_GetEnrolmentAttributeApplied", binder);
	}
	
	public static EnrolmentAttributeApplied get(DataResultSet rs) throws DataException {
		return new EnrolmentAttributeApplied(
			CCLAUtils.getResultSetIntegerValue(rs, Cols.ID),
			ApplicableEnrolmentAttribute.getCache().getCachedInstance(
			 CCLAUtils.getResultSetIntegerValue
			 (rs, Cols.APPLICABLE_ENROLMENT_ATTRIBUTE_ID)
			),
			CCLAUtils.getResultSetIntegerValue
			 (rs, Cols.CAMPAIGN_ENROLMENT_ID),
			rs.getStringValueByName(Cols.VALUE)
		);
	}
	

	public void addFieldsToBinder(DataBinder binder) throws DataException {
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.ID, this.getId());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.APPLICABLE_ENROLMENT_ATTRIBUTE_ID, 
		 this.getApplicableEnrolmentAttribute().getId());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.CAMPAIGN_ENROLMENT_ID, 
		 this.getCampaignEnrolmentId());
		CCLAUtils.addQueryParamToBinder(binder, Cols.VALUE, this.getValue());
	}

	public void persist(FWFacade facade, String username) throws DataException {

		this.validate(facade);
		
		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);
		
		facade.execute("qCore_UpdateEnrolmentAttributeApplied", binder);
	
		// TODO audit
	}

	public void setAttributes(DataBinder binder) throws DataException {
		/*
		this.setApplicableEnrolmentAttributeId(
		 CCLAUtils.getBinderIntegerValue(binder, 
		 Cols.APPLICABLE_ENROLMENT_ATTRIBUTE_ID));
		*/
		
		this.setValue(binder.getLocal(Cols.VALUE));
	}

	public void validate(FWFacade facade) throws DataException {
		if (StringUtils.stringIsBlank(this.getValue()))
			throw new DataException("Value cannot be empty");	
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public void setCampaignEnrolmentId(int campaignEnrolmentId) {
		this.campaignEnrolmentId = campaignEnrolmentId;
	}
	public int getCampaignEnrolmentId() {
		return campaignEnrolmentId;
	}
	public void setApplicableEnrolmentAttribute(
			ApplicableEnrolmentAttribute applicableEnrolmentAttribute) {
		this.applicableEnrolmentAttribute = applicableEnrolmentAttribute;
	}
	public ApplicableEnrolmentAttribute getApplicableEnrolmentAttribute() {
		return applicableEnrolmentAttribute;
	}
	
	/** Returns a mapping of Applicable Enrolment Attribute IDs to each applied
	 *  enrolment attribute.
	 *  
	 * @param enrolmentAttributes
	 * @return
	 */
	public static HashMap<Integer, EnrolmentAttributeApplied> getMapping(
			Vector<EnrolmentAttributeApplied> enrolmentAttributes) {
		
		HashMap<Integer, EnrolmentAttributeApplied> mapping = new
		 HashMap<Integer, EnrolmentAttributeApplied>();
		
		for (EnrolmentAttributeApplied attribAppl : enrolmentAttributes) {
			mapping.put(attribAppl.getApplicableEnrolmentAttribute().getId(),
			 attribAppl);
		}
		
		return mapping;
	}
	

	public static Vector<EnrolmentAttributeApplied> getAllAttrByTypeAndEnrolmentId(int enrolmentId, int applicableAttrId, FWFacade facade) 
	throws DataException {
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.APPLICABLE_ENROLMENT_ATTRIBUTE_ID, applicableAttrId);
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.CAMPAIGN_ENROLMENT_ID, enrolmentId);
		
		DataResultSet rs = facade.createResultSet("qCore_GetEnrolmentAttrByTypeAndEnrolment", binder);
		
		Vector<EnrolmentAttributeApplied> attribsApplied = 
			 new Vector<EnrolmentAttributeApplied>();
			
			if (rs.first()) {
				do {
					attribsApplied.add(get(rs));
				} while (rs.next());
			}
			
		return attribsApplied;
	}

	
	public static void removeAllAttrByTypeAndEnrolmentId(int enrolmentId, int applicableAttrId, FWFacade facade)
	throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.APPLICABLE_ENROLMENT_ATTRIBUTE_ID, applicableAttrId);
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.CAMPAIGN_ENROLMENT_ID, enrolmentId);
		
		facade.execute("qCore_DeleteEnrolmentAttrByTypeAndEnrolment", binder);
		//TODO Add audit entries
	}
	
	/**
	 * Add a bunch of values that are comma separated.
	 * @param enrolmentId
	 * @param applicableAttrId
	 * @param comaSepValues
	 * @param facade
	 * @throws DataException
	 */
	public static void addAllAttrByTypeAndEnrolmentId(int enrolmentId, int applicableAttrId, String commaSepValues, String userName, FWFacade facade)
	throws DataException {
			
		if (!StringUtils.stringIsBlank(commaSepValues)) {
			String[] values = commaSepValues.split(",");
			
			for (int i=0; i<values.length; i++) {
				EnrolmentAttributeApplied.add(applicableAttrId, enrolmentId, values[i], facade, userName);
			}
		}	
	}
}
