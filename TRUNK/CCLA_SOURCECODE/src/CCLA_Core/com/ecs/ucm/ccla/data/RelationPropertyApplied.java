package com.ecs.ucm.ccla.data;

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.RelationProperty.SingletonScope;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

public class RelationPropertyApplied implements Persistable {
	
	private int propertyAppliedId;
	
	private RelationProperty relationProperty; /* Maps to a particular Property ID */
	private int relationId;

	private Date dateAdded;
	private Date lastUpdated;
	private String lastUpdatedBy;
	
	private String propertyValue;
	private boolean propertyStatus;

	public static class Cols {
		public static final String ID 				= "PROPERTY_APPLIED_ID";
		public static final String PROPERTY_VALUE 	= "PROPERTY_VALUE";
		public static final String PROPERTY_STATUS 	= "PROPERTY_STATUS";
	}
	
	public RelationPropertyApplied(int propertyAppliedId, 
			RelationProperty relationProperty, int relationId, 
			Date dateAdded, Date lastUpdated, String lastUpdatedBy, 
			String propertyValue, boolean propertyStatus) {
		this.propertyAppliedId = propertyAppliedId;
		
		this.relationProperty = relationProperty;
		this.relationId = relationId;
		
		this.dateAdded = dateAdded;
		this.lastUpdated = lastUpdated;
		this.lastUpdatedBy = lastUpdatedBy;
		
		this.propertyValue = propertyValue;
		this.propertyStatus = propertyStatus;
	}
	
	public static RelationPropertyApplied add(Property property, 
	 Relation relation, String propertyValue, boolean propertyStatus, 
	 FWFacade facade, String username) 
	 throws DataException {
		
		// Determine the Relation Property which maps the passed Property to the passed
		// Relation Name.
		RelationProperty relationProperty = 
		 RelationProperty.get(relation.getRelationName().getRelationNameId(), 
		 property.getPropertyId(), facade);
		
		// If the return RelationProperty is null, there is no such mapping between
		// the Relation Name and Property.
		if (relationProperty == null)
			throw new DataException("Invalid property '" + property.getName() + "' " +
			 "for Relation '" + relation.getRelationName().getRelation() + "'");
	
		return add(relationProperty, relation, propertyValue, 
		 propertyStatus, facade, username);
	}
	
	/** 
	 *  Add property applied object with the passed parameters
	 *  
	 *  If an object already exists with the same relation id and property id 
	 *  (relation property id) then it does not add a new one
	 *  
	 * @return PropertyApplied
	 * 
	 * @throws DataException 
	 */
	public static RelationPropertyApplied add(RelationProperty relationProperty, 
	 Relation relation, String propertyValue, boolean propertyStatus, 
	 FWFacade facade, String username) 
	 throws DataException {
		
		int propertyAppliedId = Integer.parseInt(
		 CCLAUtils.getNewKey("PropertyApplied", facade));	
		
		RelationPropertyApplied propApplied = new RelationPropertyApplied(
			propertyAppliedId,
			relationProperty, 
			relation.getRelationId(), 
			null, 
			null,
			username,
			propertyValue,
			propertyStatus);
		
		DataBinder binder = new DataBinder();
		propApplied.addFieldsToBinder(binder);
		facade.execute("qClientServices_AddPropertyApplied", binder);
		
		// Fetch newly-added RelationPropertyApplied data for audit.
		DataResultSet newData = RelationPropertyApplied.getData
		 (propertyAppliedId, facade);

		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.ADD.toString(), 
		 ElementType.SecondaryElementType.RelationPropertyApplied.toString(), 
		 null, newData, Relation.getAuditRelations(relation));
		
		return propApplied;
	}
	
	/** 
	 *  Removes property applied object with the passed parameters
	 *  
	 * 
	 *  
	 * @return 
	 * 
	 * @throws DataException 
	 */	
	public static void remove(RelationPropertyApplied appliedProperty, 
	 FWFacade facade, String username) throws DataException {
		
		// Fetch current data for audit purposes
		DataResultSet oldData = RelationPropertyApplied.getData
		 (appliedProperty.getPropertyAppliedId(), facade);
		
		// Fetch relation instance for audit purposes
		Relation relation = Relation.get(appliedProperty.getRelationId(), facade);
		
		DataBinder binder = new DataBinder();
		appliedProperty.addFieldsToBinder(binder);
		facade.execute("qClientServices_DeletePropertyApplied", binder);
		
		// Audit
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.DELETE.toString(), 
		 ElementType.SecondaryElementType.RelationPropertyApplied.toString(), 
		 oldData, null, Relation.getAuditRelations(relation));
	}
	
	public static void remove(Relation relation, RelationProperty relationProperty, 
	 FWFacade facade, String username) throws DataException {
		
		RelationPropertyApplied existingProp = 
		 RelationPropertyApplied.getByRelationAndRelationProperty
		 (relation, relationProperty, facade);
		
		if (existingProp == null)
			throw new DataException("Unable to remove relation: not found");
		
		remove(existingProp, facade, username);
	}
	
	public static Vector<RelationPropertyApplied> getByRelation
	 (int relationId, FWFacade facade) throws DataException {
		
		DataResultSet rs = getByRelationData(relationId, facade);
		return getAll(rs);
	}
	
	/** Returns all Applied Properties for a single given Relation.
	 *  
	 *  The target query is joined to the REF_PROPERTY and REF_VERIFICATION_SOURCE
	 *  tables.
	 *  
	 * @param relationId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getByRelationData(int relationId, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, Relation.Cols.ID, relationId);
		
		return facade.createResultSet
		 ("qClientServices_GetPropertyAppliedByRelationId", binder);
	}
	
	/** Fetches all applied relation properties for all relations matching the given
	 *  criteria.
	 *  
	 * @param elementId1List
	 * @param elementId2List
	 * @param relationType
	 * @param relationName
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getByRelationsData
	 (Integer elementId1, Integer elementId2, 
	 RelationType relationType, RelationName relationName, FWFacade facade) 
	 throws DataException {
		
		Vector<Element> elementId1List = new Vector<Element>();
		Vector<Element> elementId2List = new Vector<Element>();
		
		if (elementId1 != null)
			elementId1List.add(new Element(elementId1, null));
		
		if (elementId2 != null)
			elementId2List.add(new Element(elementId2, null));
		
		return getByRelationsData(elementId1List, elementId2List,
		 relationType, relationName, facade);
	}
	
	/** Fetches all applied relation properties for all relations matching the given
	 *  criteria.
	 *  
	 * @param elementId1List
	 * @param elementId2List
	 * @param relationType
	 * @param relationName
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getByRelationsData
	 (Vector<Element> elementId1List, Vector<Element> elementId2List, 
	 RelationType relationType, RelationName relationName, FWFacade facade) 
	 throws DataException {
		
		String baseQuery = "SELECT relPropAppl.RELATION_ID, " +
		 "relPropAppl.*, relProp.*, prop.*, verSource.* " +
		 "FROM RELATION_PROPERTY_APPLIED relPropAppl " +
		 "INNER JOIN REF_RELATION_PROPERTY relProp ON " +
		 "(relPropAppl.RELATION_PROPERTY_ID = relProp.RELATION_PROPERTY_ID) " +
		 "INNER JOIN REF_PROPERTY prop ON " +
		 "(relProp.property_id = prop.property_id) " +
		 "LEFT JOIN REF_VERIFICATION_SOURCE verSource ON " +
		 "(prop.VERIFICATION_SOURCE_ID = verSource.VERIFICATION_SOURCE_ID) " +
		 "WHERE relpropappl.relation_id IN (";
		
		String relQuery = Relation.getRelationQueryText(Relation.Cols.ID, false, null,
		 elementId1List, elementId2List, relationType, relationName, facade);
		
		StringBuffer query = new StringBuffer(baseQuery);
		query.append(relQuery).append(")");
	
		return facade.createResultSetSQL(query.toString());
	}
	
	/** Returns a mapping of matched Relation IDs to their associated Properties.
	 * 
	 *  The mapping will only contain matched  Relation IDs that have 1 or more
	 *  associated Properties set.
	 * 
	 * @param elementId1List
	 * @param elementId2List
	 * @param relationType
	 * @param relationName
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static HashMap<Integer, Vector<RelationPropertyApplied>> 
	 getRelationPropertyMapping
	 (Vector<Element> elementId1List, Vector<Element> elementId2List, 
	 RelationType relationType, RelationName relationName, FWFacade facade) 
	 throws DataException {
		
		DataResultSet rs = getByRelationsData
		 (elementId1List, elementId2List, relationType, relationName, facade);
		
		HashMap<Integer, Vector<RelationPropertyApplied>> relPropertyMap = 
		 new HashMap<Integer, Vector<RelationPropertyApplied>>();
		
		if (rs.first()) {
			do {
				Integer thisRelationId = CCLAUtils.getResultSetIntegerValue
				 (rs, Relation.Cols.ID);
				
				Vector<RelationPropertyApplied> relPropAppl = relPropertyMap.get
				 (thisRelationId);
				
				if (relPropAppl == null) {
					relPropAppl = new Vector<RelationPropertyApplied>();
					relPropertyMap.put(thisRelationId, relPropAppl);
				}
				
				relPropAppl.add(RelationPropertyApplied.get(rs));
				
			} while (rs.next());
		}
		
		return relPropertyMap;
	}
	
	/** 
	 *  Gets the property applied object with the passed Relation ID and 
	 *  Relation Property ID 
	 *  
	 * @return PropertyApplied or null if object does not exist
	 * 
	 * @throws DataException 
	 */	
	
	public static RelationPropertyApplied getByRelationAndRelationProperty
	 (Relation relation, RelationProperty relationProperty, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Relation.Cols.ID, relation.getRelationId());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, RelationProperty.Cols.ID, relationProperty.getRelationPropertyId());
		
		DataResultSet rsPropertyApplied = 
		 facade.createResultSet("qClientServices_GetPropertyAppliedByValues", binder);
		
		return get(rsPropertyApplied);	 			
	}
	
	/** 
	 *  Returns whether a property applied object exists for the passed
	 *  Relation and RelationProperty 
	 *  
	 * @return true if it exists, false if not
	 * 
	 * @throws DataException 
	 */	
	public static boolean hasPropertyApplied
	 (Relation relation, RelationProperty relationProperty, 
	 FWFacade facade) throws DataException {
		
		if (getByRelationAndRelationProperty(relation, relationProperty, facade) 
			!= null)
			return true;
		else
			return false;
	}
	
	public static RelationPropertyApplied get(int propertyAppliedId, FWFacade facade) 
	 throws DataException {
		
		return get(getData(propertyAppliedId, facade));
	}
	
	public static DataResultSet getData(int propertyAppliedId, FWFacade facade) 
	 throws DataException {
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.ID, propertyAppliedId);
		
		DataResultSet rsPropertyApplied = facade.createResultSet
		 ("qClientServices_GetPropertyApplied", binder);
		return rsPropertyApplied;
	}

	/** Returns a RelationPropertyApplied instance.
	 *  
	 *  The ResultSet must contain all columns from the RELATION_PROPERTY_APPLIED and
	 *  REF_RELATION_PROPERTY tables.
	 * 
	 * @param rs
	 * @return
	 * @throws DataException
	 */
	public static RelationPropertyApplied get(DataResultSet rs) throws DataException {
		
		if (rs.isEmpty())
			return null;
		
		return new RelationPropertyApplied(
			CCLAUtils.getResultSetIntegerValue(rs, Cols.ID),
			RelationProperty.get(rs),
			CCLAUtils.getResultSetIntegerValue(rs, Relation.Cols.ID),
			rs.getDateValueByName(SharedCols.DATE_ADDED),
			rs.getDateValueByName(SharedCols.LAST_UPDATED),
			rs.getStringValueByName(SharedCols.LAST_UPDATED_BY),
			rs.getStringValueByName(Cols.PROPERTY_VALUE),
			CCLAUtils.getResultSetBoolValue(rs, Cols.PROPERTY_STATUS)
		);		
	}
	
	public static Vector<RelationPropertyApplied> getAll(DataResultSet rs) 
	 throws DataException {
		
		Vector<RelationPropertyApplied> relProps = 
		 new Vector<RelationPropertyApplied>();
		
		if (rs.first()) {
			do {
				relProps.add(get(rs));
			} while (rs.next());
		}
		
		return relProps;
	}
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.ID, this.getPropertyAppliedId());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, RelationProperty.Cols.ID, 
		 this.getRelationProperty().getRelationPropertyId());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Relation.Cols.ID, this.getRelationId());
		CCLAUtils.addQueryDateParamToBinder
		 (binder, SharedCols.DATE_ADDED, this.getDateAdded());
		CCLAUtils.addQueryDateParamToBinder
		 (binder, SharedCols.LAST_UPDATED, this.getLastUpdated());
		CCLAUtils.addQueryParamToBinder
		 (binder, SharedCols.LAST_UPDATED_BY, this.getLastUpdatedBy());
		CCLAUtils.addQueryParamToBinder
		 (binder, Cols.PROPERTY_VALUE, this.getPropertyValue());
		CCLAUtils.addQueryBooleanParamToBinder
		 (binder, Cols.PROPERTY_STATUS, this.getPropertyStatus());
	}

	public void persist(FWFacade facade, String username) throws DataException {
		
		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);
		this.validate(facade);
		
		Relation relation = Relation.get(this.getRelationId(), facade);
		
		DataResultSet beforeData = RelationPropertyApplied.getData
		 (this.getPropertyAppliedId(), facade);
		
		facade.execute("qClientServices_UpdatePropertyApplied", binder);
		
		DataResultSet afterData = RelationPropertyApplied.getData
		 (this.getPropertyAppliedId(), facade);
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.UPDATE.toString(), 
		 ElementType.SecondaryElementType.RelationPropertyApplied.toString(), 
		 beforeData, afterData, Relation.getAuditRelations(relation));
	}

	public void setAttributes(DataBinder binder) throws DataException {
		throw new DataException("Not implemented");
	}

	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub

	}

	public int getPropertyAppliedId() {
		return propertyAppliedId;
	}

	public void setPropertyAppliedId(int propertyAppliedId) {
		this.propertyAppliedId = propertyAppliedId;
	}

	public int getRelationId() {
		return relationId;
	}

	public void setRelationId(int relationId) {
		this.relationId = relationId;
	}

	public Date getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}

	public String getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	
	/** Updates all the mapped relation properties for a given Relation.
	 * 
	 *  Relation Properties will be in the DataBinder in 4 forms, depending on the
	 *  Property Type:
	 *  
	 *  -BOOL (verification sources)
	 *  relationPropertyFalse_<property ID>=on
	 *  relationPropertyTrue_<property ID>=on
	 *  
	 *  -BOOL (non-verification sources)
	 *  relationPropertyStatus_<property ID>=[null/0/1]
	 *  
	 *  -FLAG
	 *  relationPropertyTrue_<property ID>=on
	 *  
	 *  -STRING
	 *  relationPropertyValue_<property ID>=xxx
	 * 
	 *  The first 2 represent the Property Status and are mutually exclusive.
	 *  The third one is the property value. If none of the 3 variables are present,
	 *  then the property hasn't been mapped (or the existing mapping has been removed)
	 *  
	 *  Properties which aren't editable by users (SET_BY_USER flag) will not be
	 *  updated in this method.
	 *  
	 * @param relation
	 * @param binder
	 * @param facade
	 * @param userName
	 * @throws DataException 
	 */
	public static void updateRelationPropertiesApplied
	 (Relation relation, DataBinder binder, FWFacade facade, String userName) 
	 throws DataException {
		
		Log.debug("Updated applied user properties for " + 
		 relation.getRelationName().getRelation() + " ID " + relation.getRelationId());
		
		// Fetch all available properties for the relation type
		Vector<Property> availableProps = 
		 Property.getAvailableProperties(relation.getRelationName(), facade);
		
		// Fetch existing applied properties for the relation
		Vector<RelationPropertyApplied> appliedProps = 
		 RelationPropertyApplied.getByRelation(relation.getRelationId(), facade);
		
		for (Property availableProp : availableProps) {
			int propertyId = availableProp.getPropertyId();
			
			if (!availableProp.isSetByUser())
				continue; // ignore properties that can't be set by the user.
			
			// See if this property has been set in the binder and if so, determine its
			// status.
			Boolean propertyStatus = null; 
			
			if (!StringUtils.stringIsBlank(
				binder.getLocal("relationPropertyTrue_" + propertyId)))
				propertyStatus = true;
			else if (!StringUtils.stringIsBlank(
				binder.getLocal("relationPropertyFalse_" + propertyId)))
				propertyStatus = false;
			
			// Look for a property value, if the Property Type is 'STRING'
			String propertyValue = null;
			
			if (availableProp.getPropertyType() == Property.PropertyType.STRING)
				propertyValue = binder.getLocal("relationPropertyValue_" + propertyId);
			
			if (propertyStatus == null && propertyValue == null)
				propertyStatus = CCLAUtils.getBinderBoolValueAllowNull
				(binder, "relationPropertyStatus_" + propertyId);
			
			// The property is considered to be 'set' if either the status or value
			// is present.
			boolean hasProperty = 
			 (propertyStatus != null || !StringUtils.stringIsBlank(propertyValue));
			
			if (hasProperty && (propertyStatus == null))
				propertyStatus = true; 	// default to True property status if there
										// is a non-null property value.
			
			// Check if the property was already applied in the DB
			boolean foundProperty = false;
			
			for (RelationPropertyApplied appliedProp : appliedProps) {
				if (appliedProp.getRelationProperty().getProperty().getPropertyId() 
					== propertyId) {
					// Propery was previously set.
					foundProperty = true;
					
					if (!hasProperty) {
						// Previously-set property must be removed.
						Log.debug("Removing applied property: " + 
						 availableProp.getName());
						
						RelationPropertyApplied.remove(appliedProp, facade, userName);
					} else {
						// Check if the previously-set property has had its status 
						// or value changed
						if (appliedProp.hasChanged(propertyStatus, propertyValue)) {
							
							// Update existing applied property.
							appliedProp.setPropertyStatus(propertyStatus);
							appliedProp.setPropertyValue(propertyValue);
							appliedProp.setLastUpdatedBy(userName);
							
							appliedProp.persist(facade, userName);
						}
					}
					
					break;
				}
			}
			
			if (!foundProperty && hasProperty) {
				// Property was not previously set, add it now.
				Log.debug("Adding applied property: " + 
				 availableProp.getName() + ", status=" + propertyStatus + 
				 ", value=" + propertyValue);
				
				if (availableProp.isSingleton()) {
					// Singleton property being applied. Remove any other occurrences of
					// this applied property, based on the singleton scope
					Log.debug("Property was singleton - removing matching properties");
					removeAll(relation, availableProp, facade);
				}
				
				RelationPropertyApplied.add
				 (availableProp, relation, propertyValue, propertyStatus, 
				 facade, userName);
			}
		}
	}

	public void setPropertyStatus(boolean propertyStatus) {
		this.propertyStatus = propertyStatus;
	}

	public boolean getPropertyStatus() {
		return propertyStatus;
	}
	
	/** Determines whether the instance's status or value are different to the passed
	 *  parameters.
	 *  
	 * @param status
	 * @param value
	 * @return
	 */
	public boolean hasChanged(boolean status, String value) {
		
		if (status != this.getPropertyStatus())
			return true;
		
		// Ensure comparison strings aren't null before doing comparison check
		String curValue = "";
		String newValue = "";
		
		if (this.getPropertyValue() != null)
			curValue = this.getPropertyValue();
		
		if (value != null)
			newValue = value;
		
		return !(curValue.equals(newValue));
	}

	public void setRelationProperty(RelationProperty relationProperty) {
		this.relationProperty = relationProperty;
	}

	public RelationProperty getRelationProperty() {
		return relationProperty;
	}
	
	/** This method is used to support 'singleton' relation properties.
	 * 
	 *  When a 'singleton' relation property P is applied to a Relation R, any other
	 *  instances of P belonging to similar instances of R must be removed by calling 
	 *  this method.
	 *  
	 *  When I say 'similar instances', the matching method used to find similar 
	 *  instances is dependent on the 'singleton scope' for the given relation property
	 *  P. Possible singleton scope values:
	 *  
	 *  - ELEMENT_ID1: remove all instances of R with matching ELEMENT_ID1 and 
	 *    RelationName
	 *  - ELEMENT_ID2: remove all instances of R with matching ELEMENT_ID2 and 
	 *    RelationName
	 *  - BOTH: remove all instances of R for both the criteria above.
	 *  
	 *  TODO: replace with a PL/SQL trigger instead.
	 *  
	 * @param relation
	 * @param property
	 * @param facade
	 * @throws DataException
	 */
	public static void removeAll
	 (Relation relation, Property property, FWFacade facade) throws DataException {
		
		// First, lookup the corresponding RelationProperty for the given Relation Name
		// and Property.
		RelationProperty relProp = RelationProperty.get
		 (relation.getRelationName().getRelationNameId(),
		 property.getPropertyId(), facade);
		
		if (relProp == null)
			throw new DataException("Unable to find matching Relation Property for " +
			 "Relation Name: " + relation.getRelationName().getRelation() + 
			 ", Property: " + property.getName());
		else if (relProp.getSingletonScopeId() == null)
			throw new DataException("Unable to remove Relation Properties for " +
			 "Relation Name: " + relation.getRelationName().getRelation() + 
			 ", Property: " + property.getName() + ", no Singleton Scope specified");
		
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "ELEMENT_ID1", relation.getElementId1());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "ELEMENT_ID2", relation.getElementId2());
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "RELATION_PROPERTY_ID", relProp.getRelationPropertyId());
		
		// Remove any applied relation properties linked to ELEMENT_ID1
		if (relProp.getSingletonScopeId() == SingletonScope.ELEMENT_ID1
			||
			relProp.getSingletonScopeId() == SingletonScope.BOTH)
		facade.execute("qClientServices_DeletePropertiesAppliedByElementId1AndRelation",
		 binder);
		
		// Remove any applied relation properties linked to ELEMENT_ID2
		if (relProp.getSingletonScopeId() == SingletonScope.ELEMENT_ID2
			||
			relProp.getSingletonScopeId() == SingletonScope.BOTH)
		facade.execute("qClientServices_DeletePropertiesAppliedByElementId2AndRelation",
		 binder);
		
		Log.debug("Removed all existing relation properties for Relation Name: " + 
		 relation.getRelationName().getRelation() +
		 ", ElementID1: " + relation.getElementId1() + 
		 ", ElementID2: " + relation.getElementId2() + 
		 ", Property: " + property.getName());
	}

	/** Bulk-editing of a single applied Property against the set of relationships of
	 *  the passed RelationName type between the common Elements list and the 
	 *  related Element.
	 *  
	 *  If Relation.UpdateType = ADD, the passed property will be applied to the set of 
	 *  matched Relations. If the property is found to already exist, it will be updated 
	 *  if the passed status/value differ.
	 *  
	 *  If Relation.UpdateType = REMOVE, the passed property will be removed from the 
	 *  set of matched Relations.
	 *  
	 * @param commonElementIds
	 * @param relatedElement
	 * @param relName
	 * @param relProperty
	 * @param updateType
	 * @param propertyValue2
	 * @param propertyStatus2
	 * @param facade
	 * @param userName
	 * 
	 * @throws DataException 
	 */
	public static void addUpdateOrRemoveAll(Vector<Element> commonElements,
		Element relatedElement, RelationName relName, RelationProperty relProperty,
		Relation.UpdateType updateType,
		String propertyValue, Boolean propertyStatus, FWFacade facade,
		String userName) throws DataException {
		
		Property property = relProperty.getProperty();
		
		Log.debug("RelationPropertyApplied:addUpdateOrRemoveAll:begin");
		Log.debug("Relation Property ID: " + relProperty.getRelationPropertyId());
		Log.debug("Property ID: " + property.getPropertyId() + ", name: " + 
		 property.getName());
		Log.debug("Relation Name ID: " + relName.getRelationNameId() + ", name: " +
		 relName.getRelation());
		
		Vector<Element> elementId1List = new Vector<Element>();
		Vector<Element> elementId2List = new Vector<Element>();
		
		RelationType relType = relName.getRelationType();
		
		if (relType.getElement1Type().equals(relatedElement.getType())) {
			elementId1List.add(relatedElement);
			elementId2List = commonElements;
		} else {
			elementId1List = commonElements;
			elementId2List.add(relatedElement);
		}
		
		// Fetch the target set of Relations
		Vector<Relation> relations = Relation.getAll(
		 Relation.getMultipleRelationsData
		 (elementId1List, elementId2List, null, relName, facade)
		);
		
		Log.debug("Matched " + relations.size() + 
		 " relations to potentially apply property update");
		
		// Fetch any previously-applied properties for the above Relations
		// TODO: this list may become 'stale' due to the removeAll() call below. May
		// need to re-query this list following a call to that method.
		HashMap<Integer, Vector<RelationPropertyApplied>> relPropertyMap = 
		 getRelationPropertyMapping(elementId1List, elementId2List, 
		 relType, relName, facade);
		
		Log.debug(relPropertyMap.size() + 
		 " of the relations have at least 1 Property already set");
		
		int addCount = 0;
		int removeCount = 0;
		int updateCount = 0;
		
		// Add/update properties
		for (Relation relation : relations) {
			Vector<RelationPropertyApplied> existingProps = relPropertyMap.get
			 (relation.getRelationId());
		
			RelationPropertyApplied existingProp = null;
			
			if (existingProps != null) {
				for (RelationPropertyApplied relProp : existingProps) {
					if (relProp.getRelationProperty().equals(relProperty)) {
						existingProp = relProp;
						break;
					}
				}
			}
			
			if (updateType == Relation.UpdateType.ADD) {
				if (existingProp != null) {
					// Property already exists. May need to update it, if the status/
					// value is different.
					if (existingProp.hasChanged(propertyStatus, propertyValue)) {
						// Has indeed changed. Set passed values and update
						
						Log.debug("Property already applied to Relation ID " 
						 + relation.getRelationId() + ", but status/value needs update");
						
						existingProp.setPropertyStatus(propertyStatus);
						existingProp.setPropertyValue(propertyValue);
						existingProp.setLastUpdatedBy(userName);
						
						existingProp.persist(facade, userName);
						
						updateCount++;
					} else {
						// No change to existing property.
						Log.debug("Property already applied to Relation ID " 
						 + relation.getRelationId() + ", no update required");
					}
					
				} else {
					Log.debug("Adding applied Property to Relation ID " 
					 + relation.getRelationId());
					
					if (property.isSingleton()) {
						// Singleton property being applied. Remove any other occurrences of
						// this applied property, based on the singleton scope
						Log.debug("Property was singleton - removing matching properties");
						removeAll(relation, property, facade);
					}
					
					// Property doesn't exist. Add one now.
					existingProp = RelationPropertyApplied.add
					 (property, relation, propertyValue, propertyStatus, 
					 facade, userName);
					
					addCount++;
				}
				
			} else if (updateType == Relation.UpdateType.REMOVE) {
				
				if (existingProp != null) {
					Log.debug("Removing applied Property from Relation ID " 
					 + relation.getRelationId());
					
					RelationPropertyApplied.remove(existingProp, facade, userName);
					removeCount++;
				}	
			}
		}
		
		Log.debug("Add/Update/Remove Relation Properties completed. Adds: "
		 + addCount + ", Updates: " + updateCount + ", Removes: " + removeCount);
		
		Log.debug("RelationPropertyApplied:addUpdateOrRemoveAll:end");
	}
}
