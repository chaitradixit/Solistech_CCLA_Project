package com.ecs.ucm.ccla.data;

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

public class ElementIdentifierApplied implements Persistable {
	
	private int identifierId;
	private String identifierValue;
	private int elementId;
	
	private Date dateAdded;
	private Date lastUpdated;
	private String lastUpdatedBy;
	
	/**
	 * An OrgReference models a single row from the ORG_REFERENCES table. The
	 * row is a reference to a unique ID for an organisation, e.g. CRN.
	 * @param identifierId
	 * @param identifierValue
	 * @param elementId
	 */
	public ElementIdentifierApplied(int identifierId, String identifierValue, 
	 int elementId, Date dateAdded, Date lastUpdated, String lastUpdatedBy) {
		this.identifierId 		= identifierId;
		this.identifierValue 	= identifierValue;
		this.elementId 			= elementId;
		this.dateAdded			= dateAdded;
		this.lastUpdated		= lastUpdated;
		this.lastUpdatedBy		= lastUpdatedBy;
	}
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		CCLAUtils.addQueryIntParamToBinder(
				binder, "ELEMENT_IDENTIFIER_ID", this.identifierId);
		
		CCLAUtils.addQueryParamToBinder(
				binder, "IDENTIFIER_VALUE", this.identifierValue);
		
		CCLAUtils.addQueryIntParamToBinder(
				binder, "ELEMENT_ID", elementId);
		
		CCLAUtils.addQueryDateParamToBinder(binder, "DATE_ADDED", dateAdded);
		CCLAUtils.addQueryDateParamToBinder(binder, "LAST_UPDATED", lastUpdated);
		CCLAUtils.addQueryParamToBinder(binder, "LAST_UPDATED_BY", lastUpdatedBy);
	}

	/**
	 * If the value passed is empty, the corresponding row will be deleted
	 * from the table, otherwise an Update will be performed on that table.
	 */
	public void persist(FWFacade facade, String username) throws DataException {		
		
		this.validate(facade);
		
		DataResultSet beforeData = 
		 getData(this.getElementId(), this.getIdentifierId(), facade);
		
		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);
		
		facade.execute("qClientServices_UpdateElementIdentifierApplied", 
		 binder);
		
		DataResultSet afterData = 
		 getData(this.getElementId(), this.getIdentifierId(), facade);

		// Audit against ELEMENT_ID
		ElementType elemType = Element.get(this.getElementId(), facade).getType();
		
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(this.getElementId(), elemType.getName());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.UPDATE.toString(), 
		 ElementType.SecondaryElementType.ElementIdentifierApplied.toString(), 
		 beforeData, afterData, auditRelations);
	}
	
	/**
	 * Fetches the corresponding applied Element Identifier, if it exists.
	 *  
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static ElementIdentifierApplied get(int elementId, int identifierId, 
	 FWFacade facade) throws DataException{
		
		return get(getData(elementId, identifierId, facade));
	}
	
	public static DataResultSet getData(int elementId, int identifierId, 
	 FWFacade facade) throws DataException{
		
		ElementIdentifierApplied elemIdAppl = 
		 new ElementIdentifierApplied
		 (identifierId, null, elementId, null, null, null);
		
		DataBinder binder = new DataBinder();
		elemIdAppl.addFieldsToBinder(binder);
		
		DataResultSet rsElementIdentifierApplied = facade.createResultSet(
		 "qClientServices_GetElementIdentifierApplied", binder);
		
		return rsElementIdentifierApplied;
	}
			
	
	public static ElementIdentifierApplied get(DataResultSet rs) 
	 throws DataException {
		
		if (rs.isEmpty())
			return null;
		
		return new ElementIdentifierApplied(
			CCLAUtils.getResultSetIntegerValue
			 (rs, "ELEMENT_IDENTIFIER_ID"),
			rs.getStringValueByName("IDENTIFIER_VALUE"),
			CCLAUtils.getResultSetIntegerValue
			 (rs, "ELEMENT_ID"),
			rs.getDateValueByName("DATE_ADDED"),
			rs.getDateValueByName("LAST_UPDATED"),
			rs.getStringValueByName("LAST_UPDATED_BY")
		);
	}
	
	/**
	 *  Deletes corresponding applied Element Identifier.
	 *  
	 * 
	 * @param facade
	 * @throws DataException
	 */
	public static void remove
	 (ElementIdentifierApplied elemIdAppl, FWFacade facade, String userName) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		
		DataResultSet oldData = 
		 getData(elemIdAppl.getElementId(), elemIdAppl.getIdentifierId(), facade);
		
		elemIdAppl.addFieldsToBinder(binder);
		facade.execute("qClientServices_DeleteElementIdentifierApplied", binder);
		
		// Audit against ELEMENT_ID
		ElementType elemType = Element.get(elemIdAppl.getElementId(), facade).getType();
		
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(elemIdAppl.getElementId(), elemType.getName());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, userName, 
		 Globals.AuditActions.DELETE.toString(), 
		 ElementType.SecondaryElementType.ElementIdentifierApplied.toString(), 
		 oldData, null, auditRelations);
	}
	
	/**
	 * Adds this reference to the table
	 * 
	 * @param facade
	 * @throws DataException
	 */
	public static ElementIdentifierApplied add
	 (ElementIdentifierApplied elemIdAppl, FWFacade facade, String userName) 
	 throws DataException {
		
		elemIdAppl.validate(facade);
		
		DataBinder binder = new DataBinder();
		elemIdAppl.addFieldsToBinder(binder);

		facade.execute("qClientServices_AddElementIdentifierApplied", 
		 binder);
		
		// Add audit record, linked to the given Element ID. 
		DataResultSet newData = 
		 getData(elemIdAppl.getElementId(), elemIdAppl.getIdentifierId(), facade);
		
		// Determine ElementType of the Element ID
		ElementType elemType = Element.get(elemIdAppl.getElementId(), facade).getType();
		
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(elemIdAppl.getElementId(), elemType.getName());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, userName, 
		 Globals.AuditActions.ADD.toString(), 
		 ElementType.SecondaryElementType.ElementIdentifierApplied.toString(), 
		 null, newData, auditRelations);
		
		return get(newData);
	}

	public void setAttributes(DataBinder binder) throws DataException {
		throw new DataException("Not implemented");	
	}

	public void validate(FWFacade facade) throws DataException {
		if (StringUtils.stringIsBlank(this.getIdentifierValue()))
			throw new DataException("Unable to set Element Identifier " +
			 "to a blank value");
	}
	
	public int getIdentifierId() {
		return identifierId;
	}

	public void setIdentifierId(int identifierId) {
		this.identifierId = identifierId;
	}

	public String getIdentifierValue() {
		return identifierValue;
	}

	public void setIdentifierValue(String identifierValue) {
		this.identifierValue = identifierValue;
	}

	public int getElementId() {
		return elementId;
	}

	public void setElementId(int elementId) {
		this.elementId = elementId;
	}

	/**
	 * Adds/updates/deletes entries in the ELEMENT_IDENTIFIERS_APPLIED table
	 * for the given Element ID.
	 * 
	 * The passed binder is expected to contain variables in the form:
	 *  
	 *  ELEMENT_IDENTIFIER_<Element Identifier ID>
	 * 
	 * @param isAdd
	 * @param entityId
	 * @throws ServiceException
	 * @throws DataException 
	 */
	public static void updateElementIdentifiersApplied
	 (int elementId, ElementType elemType, DataBinder binder, 
	 FWFacade facade, String userName) 
	 throws ServiceException, DataException {
	
		Log.debug("Updating applied Element Identifiers for Element ID: " + 
		 elementId);
		
		// Fetch available element identifiers for this Element ID.
		DataResultSet rsIdentifiers = 
		 ElementIdentifier.getIdentifiersByElementTypeData
		 (elemType, facade);
		
		Vector<ElementIdentifier> elemIds = 
		 ElementIdentifier.getAll(rsIdentifiers);
		
		// Fetch existing applied element identifiers for this Element ID.
		Vector<ElementIdentifierApplied> idsApplied = 
		 getElementIdentifiersApplied(elementId, facade);
		
		Log.debug("Found " + idsApplied.size() + " existing identifiers, " 
		 + elemIds.size() + " available for this element type.");
		
		// Loop through all available element identifiers
		for (ElementIdentifier elemId : elemIds) {
				
			// Check if this element identifier has a value set.
			String identifierValue = 
			 binder.getLocal("ELEMENT_IDENTIFIER_" + elemId.getElementIdentifierId());
			
			ElementIdentifierApplied elemIdApplied = null;
			
			// Check if the element has this identifier already set.
			for (ElementIdentifierApplied idApplied : idsApplied) {
				if (idApplied.getIdentifierId() == elemId.getElementIdentifierId()) {
					elemIdApplied = idApplied;
				}
			}
			
			if (StringUtils.stringIsBlank(identifierValue)) {
				if (elemIdApplied!=null) {
					// Existing value has been deleted from this element.
					Log.debug("Removing identifier with ID: " + 
					 elemId.getElementIdentifierId());
					
					remove(elemIdApplied, facade, userName);
				}
			} else {
				if (elemIdApplied!=null) {
					if (!elemIdApplied.getIdentifierValue().equals(identifierValue)) {
						// Existing value has been updated
						Log.debug("Updating identifier with ID: " + 
						 elemId.getElementIdentifierId());
					
						elemIdApplied.setIdentifierValue(identifierValue);
						elemIdApplied.setLastUpdatedBy(userName);
						
						elemIdApplied.persist(facade, userName);
					}
				} else {
					// New identifier.
					Log.debug("Adding identifier with ID: " + 
					 elemId.getElementIdentifierId());
					
					elemIdApplied = new ElementIdentifierApplied(
					 elemId.getElementIdentifierId(), identifierValue, 
					 elementId, null, null, userName);
					
					add(elemIdApplied, facade, userName);
				}
			}
		}
	}

	/**
	 * Fetches all rows from the ELEMENT_IDENTIFIERS_APPLIED table for the
	 * given Element ID, i.e. all identifiers and their respective values
	 * associated with the element.
	 * 
	 * @param facade
	 * @return
	 * @throws DataException 
	 * @throws ServiceException
	 * @throws DataException
	 */
	public static DataResultSet getElementIdentifiersAppliedData
	 (int elementId, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "ELEMENT_ID", elementId);
		
		return facade.createResultSet
		 ("qClientServices_GetElementIdentifiersApplied", binder);
	}
	
	public static Vector<ElementIdentifierApplied> 
	 getElementIdentifiersApplied(int elementId, FWFacade facade) 
	 throws DataException {
		
		DataResultSet rs = getElementIdentifiersAppliedData
		 (elementId, facade);
		
		Vector<ElementIdentifierApplied> idsApplied = 
		 new Vector<ElementIdentifierApplied>();
		
		if (rs.first()) {
			do {
				idsApplied.add(get(rs));
			} while (rs.next());
		}
		
		return idsApplied;
	}

	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}

	public Date getDateAdded() {
		return dateAdded;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
}
