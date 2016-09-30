package com.ecs.ucm.ccla.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.xml.rpc.ServiceException;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.CacheManager;
import com.ecs.ucm.ccla.data.ElementAttribute.SelectionType;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Models entries from the ELEMENT_ATTRIBUTE_APPLIED table.
 * 
 *  Each entry represents a connection between an Element and Attribute.
 * 
 * @author Tom
 *
 */
public class ElementAttributeApplied implements Persistable {

	public static class CFAccTypeAttrValue {
		public static final String INCOME 			= "Income";
		public static final String GOVERNMENT_MATCH = "Government Match";
		public static final String GIFT_AID 		= "Gift Aid";
		public static final String ENDOWMENT 		= "Endowment";
	}
	

	public static class Cols {
		public static final String ELEMENT_ID = "ELEMENT_ID";
		public static final String ELEMENT_ATTRIBUTE_ID = "ELEMENT_ATTRIBUTE_ID";
		public static final String DATE_ADDED = "DATE_ADDED";
		public static final String ATTRIBUTE_STATUS = "ATTRIBUTE_STATUS";
		public static final String LAST_UPDATED = "LAST_UPDATED";
		public static final String LAST_UPDATED_BY = "LAST_UPDATED_BY";
		public static final String SUPPORTING_DOC_ID = "SUPPORTING_DOC_ID";
		public static final String ATTRIBUTE_VALUE = "ATTRIBUTE_VALUE";
	}
	
	private int elementId;
	private int attributeId;
	
	private Boolean status;
	private String attrValue;
	private String docGuid;

	private Date dateAdded;
	private Date lastUpdated;
	private String lastUpdatedBy;
	
	
	public ElementAttributeApplied(int elementId, int attributeId,
			Boolean status, String attrValue, 
			Date dateAdded, Date lastUpdated, String lastUpdatedBy, String docGuid) {
		this.elementId = elementId;
		this.attributeId = attributeId;
		this.status = status;
		this.dateAdded = dateAdded;
		this.lastUpdated = lastUpdated;
		this.lastUpdatedBy = lastUpdatedBy;
		this.attrValue = attrValue;
		this.docGuid = docGuid;
	}
	
	/** Adds a new ElementAttributeApplied to the database.
	 * 
	 * @param elementId
	 * @param attributeId
	 * @param status
	 * @param userId
	 * @throws DataException 
	 * @throws ServiceException 
	 */
	public static void add(int elementId, int attributeId, Boolean status, String attrValue,
			String userId, FWFacade facade, String docGuid) throws DataException {
		
		ElementAttributeApplied attribAppl = new ElementAttributeApplied(
			elementId,
			attributeId,
			status,
			attrValue,
			null,
			null,
			userId,
			docGuid
		);
		
		DataBinder binder = new DataBinder();
		attribAppl.addFieldsToBinder(binder);
		
		facade.execute("qClientServices_AddElementAttributeApplied", binder);

		// Add audit record, linked to the given Element ID. 
		// Fetch newly-added ElementAttributeApplied data.
		DataResultSet newData = ElementAttributeApplied.getData
		 (elementId, attributeId, facade);
	
		// Determine ElementType of passed Element ID
		ElementType elemType = Element.get(elementId, facade).getType();
		
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(elementId, elemType.getName());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, userId, 
		 Globals.AuditActions.ADD.toString(), 
		 ElementType.SecondaryElementType.ElementAttributeApplied.toString(), 
		 null, newData, auditRelations);
	}
	
	public void remove(String userName, FWFacade facade) throws DataException {
		remove(this.getElementId(), this.getAttributeId(), userName, facade);
	}
	
	/** Removes the given ElementAttributeApplied. Throws error if it doesn't
	 *  exist.
	 *  
	 * @param elementId
	 * @param attributeId
	 * @param status
	 * @param userId
	 * @param facade
	 * @throws DataException
	 */
	public static void remove(int elementId, int attributeId, String userId, 
	 FWFacade facade) throws DataException {
		
		DataResultSet beforeData = getData(elementId, attributeId, facade);
		ElementAttributeApplied attribAppl = get(beforeData);
		
		if (attribAppl == null)
			throw new DataException("Unable to remove applied attribute (not found)");
		
		// Add audit record, linked to the given Element ID. 
	
		// Determine ElementType of the Element ID
		ElementType elemType = Element.get(elementId, facade).getType();
		
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(elementId, elemType.getName());
		
		// Remove the table row
		DataBinder binder = new DataBinder();
		attribAppl.addFieldsToBinder(binder);
		
		facade.execute("qClientServices_DeleteElementAttributeApplied", binder);
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, userId, 
		 Globals.AuditActions.DELETE.toString(), 
		 ElementType.SecondaryElementType.ElementAttributeApplied.toString(), 
		 beforeData, null, auditRelations);
	}
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "ELEMENT_ID", this.getElementId());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "ELEMENT_ATTRIBUTE_ID", this.getAttributeId());
		
		CCLAUtils.addQueryBooleanParamToBinderAllowNull
		 (binder, "ATTRIBUTE_STATUS", this.getStatus());
		
		CCLAUtils.addQueryParamToBinder(binder, "ATTRIBUTE_VALUE", this.getAttrValue());
		CCLAUtils.addQueryParamToBinder(binder, "DOC_GUID", this.getDocGuid());
				
		binder.putLocal("LAST_UPDATED_BY", this.getLastUpdatedBy());
		CCLAUtils.addQueryDateParamToBinder(binder, "DATE_ADDED", this.getDateAdded());
	}
	
	/** Returns a single ElementAttributeApplied, if one exists for the given
	 *  Element ID and Attribute ID.
	 *  
	 * @param elementId
	 * @param attributeId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static ElementAttributeApplied get
	 (int elementId, int attributeId, FWFacade facade) throws DataException {
		
		DataResultSet rs = getData(elementId, attributeId, facade);
		return get(rs);
	}
	
	/** Returns a mapping of ATTRIBUTE_IDs to their mapped attributes in the given
	 *  set.
	 *  
	 * @param attribsApplied
	 * @return
	 */
	public static HashMap<Integer, ElementAttributeApplied> 
	 getMapping(Vector<ElementAttributeApplied> attribsApplied) {
		
		HashMap<Integer, ElementAttributeApplied> mapping = 
		 new HashMap<Integer, ElementAttributeApplied>();
		
		for (ElementAttributeApplied attribApplied : attribsApplied) {
			mapping.put(attribApplied.getAttributeId(), attribApplied);
		}
		
		return mapping;
	}
	
	/** Fetches all attributes mapped to the given element ID.
	 *  
	 *  If verifiedSourceOnly is set, only attributes with a Verification Source ID
	 *  will be returned.
	 * 
	 * @param elementId
	 * @param selType
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static Vector<ElementAttributeApplied> getAll
	 (int elementId, boolean verifiedSourceOnly, FWFacade facade) throws DataException {
		
		DataResultSet rs = getAllData(elementId, null, verifiedSourceOnly, facade);
		return getAll(rs);
	}
	
	/** Fetches all attributes mapped to the given element ID, with the given
	 *  attribute type.
	 *  
	 *  If verifiedSourceOnly is set, only attributes with a Verification Source ID
	 *  will be returned.
	 * 
	 * @param elementId
	 * @param selType
	 * @param facade
	 * @return
	 * @throws DataException 
	 * @throws DataException 
	 */
	public static Vector<ElementAttributeApplied> getAll
	 (int elementId, ElementAttributeType attribType, boolean verifiedSourceOnly,
	 FWFacade facade) throws DataException {
		
		DataResultSet rs = getAllData
		 (elementId, attribType, verifiedSourceOnly, facade);
		
		return getAll(rs);
	}
	
	public static Vector<ElementAttributeApplied> getAll(DataResultSet rs) 
	 throws DataException {
		
		Vector<ElementAttributeApplied> attribsApplied = 
			 new Vector<ElementAttributeApplied>();
		
		if (rs.first()) {
			do {
				attribsApplied.add(get(rs));
			} while (rs.next());
		}
		
		return attribsApplied;
	}
	
	/** Fetches all applied Element Attributes for the given Element ID and Attribute 
	 *  Type.
	 *  
	 *  Attribute Type can be null, this will return all applied attributes regardless
	 *  of type.
	 *  
	 *  If verifiedSourceOnly flag is set, applied attributes are only returned if their
	 *  attribute has a Verification Source set.
	 *  
	 * @param elementId
	 * @param attribType
	 * @param verifiedSourceOnly
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getAllData
	 (int elementId, ElementAttributeType attribType, boolean verifiedSourceOnly, 
	 FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder(binder, "ELEMENT_ID", elementId);
		
		if (attribType == null) {
			if (!verifiedSourceOnly)
				return facade.createResultSet
				 ("qClientServices_GetElementAttributesApplied", binder);
			else
				return facade.createResultSet
				 ("qClientServices_GetVerifiedSourceElementAttributesApplied", binder);
		
		} else {
			CCLAUtils.addQueryIntParamToBinder
			 (binder, "ATTRIBUTE_TYPE_ID", attribType.getElementAttributeTypeId());
			
			if (!verifiedSourceOnly)
				return facade.createResultSet
				 ("qClientServices_GetElementAttributesByAttributeTypeApplied", binder);
			else
				return facade.createResultSet
				 ("qClientServices_GetVerifiedSourceElementAttributesByAttributeTypeApplied", binder);
		}
	}

	public static ElementAttributeApplied get(DataResultSet rs) throws DataException {
		
		if (rs.isEmpty())
			return null;
		
		return new ElementAttributeApplied(
		 CCLAUtils.getResultSetIntegerValue(rs, "ELEMENT_ID"),
		 CCLAUtils.getResultSetIntegerValue(rs, "ELEMENT_ATTRIBUTE_ID"),	
		 CCLAUtils.getResultSetBoolValue(rs, "ATTRIBUTE_STATUS"),
		 rs.getStringValueByName("ATTRIBUTE_VALUE"),
		 rs.getDateValueByName("DATE_ADDED"),
		 rs.getDateValueByName("LAST_UPDATED"),
		 rs.getStringValueByName("LAST_UPDATED_BY"),
		 rs.getStringValueByName("DOC_GUID")
		);
	}
	
	/** Fetches a DataResultSet containing a single applied attribute for the given 
	 *  Element ID and Attribute ID, if one exists.
	 * 
	 * @param elementId
	 * @param attributeId
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getData(int elementId, int attributeId, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder(binder, "ELEMENT_ID", elementId);
		CCLAUtils.addQueryIntParamToBinder(binder, "ELEMENT_ATTRIBUTE_ID", attributeId);
		
		return facade.createResultSet
		 ("qClientServices_GetElementAttributeApplied", binder);
	}

	public void persist(FWFacade facade, String username) throws DataException {
		
		this.validate(facade);
		
		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);
		
		DataResultSet beforeData = getData
		 (this.getElementId(), this.getAttributeId(), facade);
		
		facade.execute("qClientServices_UpdateElementAttributeApplied", binder);
		
		// Add audit record, linked to the given Element ID. 
		DataResultSet afterData = getData
		 (this.getElementId(), this.getAttributeId(), facade);
		
		// Determine ElementType of the Element ID
		ElementType elemType = Element.get(this.getElementId(), facade).getType();
		
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(elementId, elemType.getName());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.UPDATE.toString(), 
		 ElementType.SecondaryElementType.ElementAttributeApplied.toString(), 
		 beforeData, afterData, auditRelations);
	}

	public void setAttributes(DataBinder binder) throws DataException {
		// TODO Auto-generated method stub
		throw new DataException("Not implemented");
	}

	public void validate(FWFacade facade) throws DataException {
		if (StringUtils.stringIsBlank(this.getLastUpdatedBy()))
			throw new DataException("Update user name missing");
		
		if (this.getDocGuid() == null) {
			// Check if a supporting doc was required for this attribute.
			
			ElementAttribute attrib = ElementAttribute.getCache().getCachedInstance(
			 this.getAttributeId()
			);
			
			if (attrib.getVerificationSourceId() != null) {
				// Fetch Verification Source from cache
				VerificationSource verSource = 
				 VerificationSource.getCache().getCachedInstance
				 (attrib.getVerificationSourceId());
				
				// Throw exception if the Verification Source requires a supporting
				// document. This is also protected via a database trigger.
				if (verSource.isSupportingDocRequired())
					throw new DataException("Unable to apply attribute with " +
					 "verification source '" + verSource.getSourceName() + "', " +
					 "supporting document link is required");
			}
		}
	}

	public int getElementId() {
		return elementId;
	}

	public void setElementId(int elementId) {
		this.elementId = elementId;
	}

	public int getAttributeId() {
		return attributeId;
	}

	public void setAttributeId(int attributeId) {
		this.attributeId = attributeId;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Date getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}
	
	/** Updates the mapping between a single element and its associated attributes.
	 *  
	 *  Applied attribute values will only be updated for the given element type,
	 *  selection type and attribute type.
	 *  
	 *  The binder is expected to contain name-value pairs in one of the following 
	 *  forms for each selectable attribute:
	 *  
	 *  BOOL attributes:
	 *  
	 *  Null/Yes/No option list:
	 *  -elemAttribStatus_<attribute ID>=0/1
	 *  
	 *  True/False checkboxes:
	 *  -elemAttribTrue_<attribute ID>=on
	 *  -elemAttribFalse_<attribute ID>=on
	 *  
	 *  STRING/INT/FLOAT attributes:
	 *  -ELEMENT_ATTRIBUTE_<attribute ID>=<value>
	 *  
	 *  If none are present/non-null for an available attribute, the element does not 
	 *  have the attribute set, and it will be removed if it was previously set.
	 *	
	 *  This method WILL NOT touch any attributes where the setByUser flag is false.
	 *
	 * @param elementId
	 * @param elemType
	 * @param verifyOnly if set to true, only the Verification Attribute types are
	 * 					 modified, i.e. Attributes with a non-null Verification Source.
	 * @param binder
	 * @param facade
	 * @param mName
	 * @throws DataException 
	 */
	public static void updateElementAttributesApplied(int elementId,
	 ElementType elemType, SelectionType selType, ElementAttributeType attribType,
	 DataBinder binder, FWFacade facade, String userName) throws DataException {

		Log.debug("Updating applied Element Attributes for Element ID: " + 
		 elementId);
				
		// Fetch available element attributes with the given element/selection type
		Vector<ElementAttribute> attribs = ElementAttribute.getElementAttributes
		 (elemType, selType, facade);
							
		// Fetch all existing applied element attributes for this Element ID.
		Vector<ElementAttributeApplied> attribsApplied = 
		 getAll(elementId, false, facade);
				
		Log.debug("Found " + attribsApplied.size() + " existing attributes, " 
		 + attribs.size() + " available for this selection type.");
				
		// Loop through all available element attributes
		for (ElementAttribute attrib : attribs) {
			
			if (!attrib.isSetByUser())
				continue; // ignore this attribute, it isn't controlled by the user
						  // so won't be batch-updated with the others.
			
			if (attribType != null && !attrib.getAttributeType().equals(attribType))
				continue; // ignore this attribute, it doesn't have the right type.
				
			// Determine the applied status of this attribute. Will be either
			// True, False or Null.
			Boolean newAttribAppliedStatus = null;
			
			// Certain BOOL attribute forms will use a 3-value option list to allow
			// users to set their state. The option list will have the name:
			// elemAttribStatus_<Attribute ID>
			//
			// It will either have a null value, 0 or 1.
			newAttribAppliedStatus = CCLAUtils.getBinderBoolValueAllowNull(binder, 
			 "elemAttribStatus_" + attrib.getAttributeId());
			
			// Other BOOL attribute forms will use two checkboxes to model True/False
			// of an applied attribute.
			if (!StringUtils.stringIsBlank(
				binder.getLocal("elemAttribTrue_" + attrib.getAttributeId()))) {
				newAttribAppliedStatus = true;
			} else if (!StringUtils.stringIsBlank(
				binder.getLocal("elemAttribFalse_" + attrib.getAttributeId()))) {
				newAttribAppliedStatus = false;
			}
			
			// check for attribute value
			String attrValue = binder.getLocal
			 ("ELEMENT_ATTRIBUTE_" + attrib.getAttributeId());

			// Check if a Document ID (dID) has been attached to the attribute.
			String docGuid = binder.getLocal("elemAttribSupportingDocGuid_" + 
			 attrib.getAttributeId());			
			
			boolean foundAttrib = false;
			
			// Check if the element has this attribute already set.
			for (ElementAttributeApplied attribApplied : attribsApplied) {
				if (attribApplied.getAttributeId() == attrib.getAttributeId()) {
					// Found the previously-set attribute.
					foundAttrib = true;
					
					if (newAttribAppliedStatus == null && attrValue == null) {
						// Previously-stored attribute must be removed.
						ElementAttributeApplied.remove(elementId, 
						 attrib.getAttributeId(), userName, facade);
					
						// See if the newly-supplied status/doc ID differs from the 
						// old one.
					} else if (attribApplied.hasChanged
							   (newAttribAppliedStatus, docGuid, attrValue)) {	
						// Data mismatch - update is required.
						attribApplied.setAttrValue(attrValue);
						attribApplied.setStatus(newAttribAppliedStatus);
						attribApplied.setDocGuid(docGuid);
						attribApplied.setLastUpdatedBy(userName);
						
						attribApplied.persist(facade, userName);
					}
				}
			}
			
			if (!foundAttrib 
				&& 
				(newAttribAppliedStatus != null 
				|| 
				!StringUtils.stringIsBlank(attrValue))) {
				// New applied attribute.
				Log.debug("Adding new applied attribute with values: " 
				 + elementId + "," + attrib.getAttributeId() + ","
				 + newAttribAppliedStatus + "," + attrValue + "," + docGuid + 
				 "," + userName);
				
				ElementAttributeApplied.add(elementId, attrib.getAttributeId(), 
				 newAttribAppliedStatus, attrValue, userName, facade, docGuid);
			}
		}
	}

	public String getDocGuid() {
		return docGuid;
	}

	public void setDocGuid(String docGuid) {
		this.docGuid = docGuid;
	}

	
	/** Determines whether this instance has a different status/supporting doc ID or value
	 *  to the ones passed.
	 *  
	 * @param status
	 * @param supportingDocId
	 * @return
	 */
	public boolean hasChanged(Boolean status, String docGuid, String attrValue) {
		
		// Check for null/non-null boolean status value
		if ((this.getStatus() == null && status != null)
			||
			(this.getStatus() != null && status == null))
			return true;
		
		// Check for mismatched boolean status value
		if (this.getStatus() != null && status != null 
			&& 
			(!this.getStatus().equals(status)))
			return true;
		
		// Check for null/non-null attribute values
		if ((StringUtils.stringIsBlank(this.getAttrValue())
			&& !StringUtils.stringIsBlank(attrValue))
			||
			(!StringUtils.stringIsBlank(this.getAttrValue()) 
			&& StringUtils.stringIsBlank(attrValue)))
			return true;
		
		// Check for mismatched attribute values
		if (!StringUtils.stringIsBlank(this.getAttrValue())
			&& 
			!StringUtils.stringIsBlank(attrValue)
			&&
			!this.getAttrValue().equals(attrValue))
			return true;
		
		
		// Check for null/non-null supporting doc IDs
		if ((this.getDocGuid() == null && docGuid != null)
			||
			(this.getDocGuid() != null && docGuid == null))
			return true;
		

		// Check for mismatched supporting doc IDS
		if (this.getDocGuid() != null
			&&
			docGuid != null
			&&
			!this.getDocGuid().equals(docGuid))
			return true;
		
		return false;
	}

	public String getAttrValue() {
		return attrValue;
	}

	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}

	/**
	 * Adds or removes a single applied Element Attribute.
	 *  
	 * If the attribute already exists, and isAdd=true, nothing happens
	 * If the attribute didn't exist, and isAdd=false, nothing happens
	 * 
	 * @param personId
	 * @param attrID
	 * @param isAdd 		determines whether to add or remove. true = add.
	 * @param facade
	 * @throws DataException
	 */
	public static void addOrRemoveSingle
	 (int elementId, int attribId, boolean isAdd, FWFacade facade) throws DataException {
		
		// Check if the attribute already exists.
		ElementAttributeApplied attribAppl = get(elementId, attribId, facade);
		
		ElementAttribute attrib = 
		 ElementAttribute.getCache().getCachedInstance(attribId);
		
		if (isAdd) {
			if (attribAppl==null) {
				Log.debug("Adding ElementAttribute '" + attrib.getName() + 
				 "' for  Element ID " + elementId);

				add(elementId, attribId, true, null, com.ecs.ucm.ccla.Globals.Users.System, facade, null);
			} else {
				Log.debug("ElementAttribute '" + attrib.getName() + 
				 "' for  Element ID " + elementId + " already exists, no need to add");		
			}
		} else {
			if (attribAppl!=null) {
				Log.debug("Removing ElementAttribute '" + attrib.getName() + 
				 "' for  Element ID " + elementId);
				
				remove(elementId, attribId, com.ecs.ucm.ccla.Globals.Users.System, facade);
			} else {
				Log.debug("ElementAttribute '" + attrib.getName() + 
				 "' for  Element ID " + elementId + " not present, no need to remove");
			}
		}
	}
	
	/**
	 * Adds a single applied attribute with the given status/value.
	 *  
	 * If the applied attribute already exists it will be updated.
	 * 
	 * @param elementId
	 * @param attribId
	 * @param attribStatus
	 * @param facade
	 * @throws DataException 
	 * @throws DataException
	 */
	public static void addOrUpdateSingle
	 (int elementId, int attribId, Boolean attribStatus, 
	 String attribValue, FWFacade facade) throws DataException {
		
		// Check if the attribute already exists.
		ElementAttributeApplied attribAppl = get(elementId, attribId, facade);
		
		ElementAttribute attrib = 
		 ElementAttribute.getCache().getCachedInstance(attribId);
		
		// If this is a BOOL attribute, ensure the attribStatus param is non-null.
		if (attrib.getDataType().equals(DataType.BOOL) && attribStatus == null)
			throw new DataException("Unable to set null attribute status for " +
			 "attribute: " + attrib.getName());
		
		if (attribAppl != null) {
			// Update existing attribute.
			Log.debug("Updating ElementAttribute '" + attrib.getName() + 
			"' for  Element ID " + elementId + 
			" (status=" + attribStatus + ", value=" + attribValue + ")");
			
			attribAppl.setStatus(attribStatus);
			attribAppl.setAttrValue(attribValue);
			
			attribAppl.persist(facade, com.ecs.ucm.ccla.Globals.Users.System);
			
		} else {
			// Add new applied attribute.
			Log.debug("Adding ElementAttribute '" + attrib.getName() + 
			"' for  Element ID " + elementId + 
			" (status=" + attribStatus + ", value=" + attribValue + ")");
			add(elementId, attribId, attribStatus, attribValue, com.ecs.ucm.ccla.Globals.Users.System, facade, null);
		}
	}
	
	
	@Override
	public String toString() {
		return "ElementAttributeApplied [attrValue=" + attrValue
				+ ", attributeId=" + attributeId + ", dateAdded=" + dateAdded
				+ ", elementId=" + elementId + ", lastUpdated=" + lastUpdated
				+ ", lastUpdatedBy=" + lastUpdatedBy + ", status=" + status
				+ ", docGuid=" + docGuid + "]";
	}
	
	/** Fetches all applied element attributes of the given Attribute type and value.
	 *
	 * @param attributeId
	 * @param value
	 * @return
	 * @throws DataException 
	 */
	public static List<ElementAttributeApplied> getByValue
	 (ElementAttribute elementAttribute, String value, FWFacade facade) 
	 throws DataException {
		List<ElementAttributeApplied> attrs = new ArrayList<ElementAttributeApplied>();
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, ElementAttribute.Cols.ID, elementAttribute.getAttributeId());
		CCLAUtils.addQueryParamToBinder
		 (binder, ElementAttributeApplied.Cols.ATTRIBUTE_VALUE, value);
		
		DataResultSet rs = facade.createResultSet
		 ("qCore_GetElementAttributesAppliedByValue", binder);
		
		if (rs.first()) {
			do
				attrs.add(ElementAttributeApplied.get(rs));
			while (rs.next());
		}
		
		return attrs;
	}
}
