package com.ecs.stellent.ccla.clientservices;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.server.Service;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.ElementLockManager;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.Contact;
import com.ecs.ucm.ccla.data.Element;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.Property;
import com.ecs.ucm.ccla.data.Relation;
import com.ecs.ucm.ccla.data.RelationName;
import com.ecs.ucm.ccla.data.RelationProperty;
import com.ecs.ucm.ccla.data.RelationPropertyApplied;
import com.ecs.ucm.ccla.data.RelationType;
import com.ecs.ucm.ccla.data.SharedCols;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

public class RelationService extends Service {
	
	/** Connects two elements with a given relation.
	 *  
	 *  Requires PARENT_ELEMENT_ID and a RELATED_ELEMENT_ID in the binder
	 *  (these map to ELEMENT_ID1, ELEMENT_ID2 columns in the relations
	 *  table respectively). Also requires RELATION_TYPE_ID and RELATIONSHIP.
	 *  
	 * @throws ServiceException 
	 * @throws DataException 
	 *  
	 */
	public void addRelation() throws ServiceException, DataException {
		
		String parentIdStr		= m_binder.getLocal("PARENT_ELEMENT_ID");
		String relatedIdStr 	= m_binder.getLocal("RELATED_ELEMENT_ID");
		
		String relationNameIdStr	= m_binder.getLocal("RELATION_NAME_ID");
		
		String username = m_userData.m_name;
		
		if (StringUtils.stringIsBlank(parentIdStr)
			||
			StringUtils.stringIsBlank(relatedIdStr)
			||
			StringUtils.stringIsBlank(relationNameIdStr))
			throw new ServiceException("Unable to add Element " + 
			 "relation, PARENT_ELEMENT_ID, RELATED_ELEMENT_ID or " +
			 "RELATION_NAME_ID missing:" + parentIdStr + "," + relatedIdStr +
			 "," + relationNameIdStr);
		
		int parentId 		= Integer.parseInt(parentIdStr); // Maps to ELEMENT_ID1
		int relatedId 		= Integer.parseInt(relatedIdStr); // Maps to ELEMENT_ID2
		
		int relationNameId	= Integer.parseInt(relationNameIdStr);
		RelationName relName = 
		 RelationName.getCache().getCachedInstance(relationNameId);
		
		// Ensure the relation name exists.
		if (relName == null)
			throw new DataException("No matching relation name for ID " 
			 + relationNameId);
		
		FWFacade facade 	= CCLAUtils.getFacade(m_workspace, true);
		
		Log.debug("Creating relation '" + relName.getRelation() + 
		 "' between parent element " + parentId + 
		 ", related element " + relatedId);
		
		if (Relation.relationExists(parentId, relatedId, relName, facade)) {
			Log.error("Relation already exists!");
			throw new ServiceException(("Relation between elements already exists."));
			
		} else {
			// Add elementIds to the Element Lock map
			ElementLockManager elementLockManager = 
			 ElementLockManager.getElementLockManager();
			
			Vector<Integer> allElementIds = new Vector<Integer>();
			allElementIds.add(parentId);
			allElementIds.add(relatedId);
			
			// Acquire lock and block until we have it!
			BigInteger	lockId = elementLockManager.addElementsToLock(
			 m_userData.m_name, "method: addRelation", allElementIds);
			
			try {
				Relation elemRelation = 
				 Relation.add(parentId, relatedId, relName, facade, username);
				
				CCLAUtils.addQueryIntParamToBinder
				 (m_binder, "newRelationId", elemRelation.getRelationId());
				Log.debug("Relation created.");
				
				CCLAUtils.appendToBinderParam
				 (m_binder, "RedirectUrl", 
				  Integer.toString(elemRelation.getRelationId()));
				
			} finally {
				// Remove the ElementLock, if it exists.
				if (lockId != null)
					elementLockManager.removeElementLock(lockId);
			}
		}
	}
	
	/** Fetches information for a single Relation and places it in the binder.
	 * 
	 * @throws ServiceException 
	 * @throws DataException 
	 * 
	 */
	public void getRelationInfo() throws ServiceException, DataException {
		
		String relIdStr = m_binder.getLocal(Relation.Cols.ID);
		
		if (StringUtils.stringIsBlank(relIdStr))
			throw new ServiceException("Unable to fetch Relation info: RELATION_ID " +
			"missing");
		
		int relId		= Integer.parseInt(relIdStr);
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		// Add general Relation data
		DataResultSet rsRelation = Relation.getExtendedData(relId, facade);
		m_binder.addResultSet("rsRelation", rsRelation);
		
		Relation rel = Relation.get(rsRelation);
		
		// Add available Properties
		DataResultSet rsAvailableRelationProperties =
		 Property.getAvailablePropertiesData(rel.getRelationName(), facade);
		
		m_binder.addResultSet
		 ("rsAvailableRelationProperties", rsAvailableRelationProperties);
		
		// Add Applied Properties
		DataResultSet rsRelationPropertiesApplied = 
		 RelationPropertyApplied.getByRelationData(relId, facade);
		m_binder.addResultSet
		 ("rsRelationPropertiesApplied", rsRelationPropertiesApplied);
		
		// Add element type names
		m_binder.putLocal("ELEMENT_TYPE_ID_1_NAME", 
		 rel.getRelationName().getRelationType().getElement1Type().getName());
		m_binder.putLocal("ELEMENT_TYPE_ID_2_NAME", 
		 rel.getRelationName().getRelationType().getElement2Type().getName());
		
		// Add relation contacts
		DataResultSet rsRelationContacts = Contact.getRelationContactsData
		 (rel.getRelationId(), facade);
		
		m_binder.addResultSet("rsRelationContacts", rsRelationContacts);
		
		// Add contact data for related elements.
		DataResultSet rsElementId1Contacts = 
		 Contact.getElementContactsData(rel.getElementId1(), facade);
		DataResultSet rsElementId2Contacts = 
		 Contact.getElementContactsData(rel.getElementId2(), facade);
		
		m_binder.addResultSet("rsElementId1Contacts", rsElementId1Contacts);
		m_binder.addResultSet("rsElementId2Contacts", rsElementId2Contacts);
	}
	
	/** Fetches the relations between 2 elements and adds them to the
	 *  DataBinder. Generally used to prep a page before calling the
	 *  updateRelations method.
	 *  
	 *  Requires PARENT_ELEMENT_ID and a RELATED_ELEMENT_ID in the binder
	 *  (these map to ELEMENT_ID1, ELEMENT_ID2 columns in the relations
	 *  table respectively).
	 *  
	 * @throws ServiceException 
	 * @throws DataException 
	 */
	public void getRelations() throws ServiceException, DataException {
		
		String parentIdStr		= m_binder.getLocal("PARENT_ELEMENT_ID");
		String relatedIdStr 	= m_binder.getLocal("RELATED_ELEMENT_ID");

		if (StringUtils.stringIsBlank(parentIdStr)
			||
			StringUtils.stringIsBlank(relatedIdStr))
			throw new ServiceException("Unable to fetch Element " + 
			 "relations, PARENT_ELEMENT_ID or RELATED_ELEMENT_ID missing");
		
		int parentId 	= Integer.parseInt(parentIdStr); // Maps to ELEMENT_ID1
		int relatedId 	= Integer.parseInt(relatedIdStr); // Maps to ELEMENT_ID2
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);

		DataResultSet rsRelations = 
		 Relation.getRelationData(parentId, relatedId, null, 
		  null, facade);
		
		m_binder.addResultSet("rsRelations", rsRelations);
	}
	

	/** Updates the relations between a single element and a set of other
	 *  homogeneous elements, e.g. all Person relations to a single 
	 *  Organisation.
	 *  
	 *  Required in the binder:
	 *	-ELEMENT_ID: determines the 'common' element ID being updated. E.g. 
	 *   when updating the relationships between a single Organisation and all
	 *   related persons, this would correspond to the Organisation ID.
	 *  -ELEMENT_TYPE_ID_1, ELEMENT_TYPE_ID_2: Element Type IDs - implies the 
	 *   type of relationship that will be updated between the passed elements.
	 *  
	 *  Relationships are updated based on the value of: 
	 *  
	 *   rel_<related element ID>_<relation name ID>
	 *  
	 *  parameters in the binder. Related Element IDs are determined by 
	 *  fetching all existing related elements for the common element ID.
	 *  
	 *  If a particular value like the one above is null/missing, the
	 *  existing relation will be removed if applicable. If a particular
	 *  one is non-null, the new relation will be added if applicable.
	 *  
	 *  This will potentially result in rows being added/removed to the
	 *  RELATIONS table.
	 *  
	 * @throws ServiceException 
	 * @throws DataException 
	 */
	public void updateAllRelations() throws ServiceException, DataException {
		
		FWFacade facade 			= CCLAUtils.getFacade(m_workspace, true);
		String username 			= m_userData.m_name;
		
		// Gather vars out the binder. Only Set 1 or Set 2 vars need to be present.
		// Set 1:
		// ======
		Integer elementId			= CCLAUtils.getBinderIntegerValue
		 (m_binder, Element.Cols.ID);
		Integer elemType1Id			= CCLAUtils.getBinderIntegerValue
		 (m_binder, "ELEMENT_TYPE_ID_1");
		Integer elemType2Id			= CCLAUtils.getBinderIntegerValue
		 (m_binder, "ELEMENT_TYPE_ID_2");
		
		// Set 2:
		// ======
		Integer relTypeId			= CCLAUtils.getBinderIntegerValue
		 (m_binder, RelationType.Cols.ID);
		Integer elementId1			= CCLAUtils.getBinderIntegerValue
		 (m_binder, Relation.Cols.ELEMENT_ID1);
		Integer elementId2			= CCLAUtils.getBinderIntegerValue
		 (m_binder, Relation.Cols.ELEMENT_ID2);
		
		RelationType relationType = null;
		ElementType elemType1 = null, elemType2 = null;
		
		ElementType commonElementType = null, relatedElementType = null;
		
		// Whether or not the 'common' Element ID refers to ELEMENT_ID1 or ELEMENT_ID2
		// in the Relation.
		Relation.RelationElementId commonRelationElementId = null;

		// First check for ELEMENT_ID1/ELEMENT_ID2 and RELATION_TYPE_ID variables
		if ((elementId1 != null || elementId2 != null)
			&& relTypeId != null) {
			
			// Fail if we can't resolve the single element ID we are updating relations 
			// against.
			if (elementId1 != null && elementId2 != null)
				throw new ServiceException("Unable to update element relations, " +
				 "ELEMENT_ID1 and ELEMENT_ID2 values present");
			
			relationType = RelationType.getCache().getCachedInstance(relTypeId);
			
			// Determine common Element ID to update against
			elementId = elementId1 != null ? elementId1 : elementId2;
			
			// Derive Element Types from Relation Type
			elemType1 = relationType.getElement1Type();
			elemType2 = relationType.getElement2Type();
			
			// Determine common/related Element Type.
			commonElementType = Element.get(elementId, facade).getType();
			
			if (elementId1 != null) 
				commonRelationElementId = Relation.RelationElementId.ELEMENT_ID1;
			else
				commonRelationElementId = Relation.RelationElementId.ELEMENT_ID2;
			
		// Now instead check for ELEMENT_TYPE_ID_1/ELEMENT_TYPE_ID_2 and ELEMENT_ID 
		// variables
		} else {
			if (elementId == null)
				throw new ServiceException
				 ("Unable to update element relations, missing ELEMENT_ID");
	
			if (elemType1Id == null || elemType2Id == null)
				throw new ServiceException
				 ("Unable to update element relations, " +
				 "ELEMENT_TYPE_ID_1/ELEMENT_TYPE_ID_2 missing");
			
			elemType1 = ElementType.getCache().getCachedInstance(elemType1Id);
			elemType2 = ElementType.getCache().getCachedInstance(elemType2Id);
				
			// Determine relation type from passed Element Types
			relationType = RelationType.getRelationType(elemType1,elemType2);
			
			// Determine the element type of the 'common' element between all
			// relations to be updated.
			commonElementType = Element.get(elementId, facade).getType();
			
			// Does the common element appear as ELEMENT_ID1 or ID2 in the
			// relations data? Once we know this, existing relations data can
			// be fetched.
			if (relationType.getElement1Type().equals(commonElementType))
				commonRelationElementId = Relation.RelationElementId.ELEMENT_ID1;
			else
				commonRelationElementId = Relation.RelationElementId.ELEMENT_ID2;
		}

		if (elemType1 == null || elemType2 == null)
			throw new ServiceException("Unable to resolve Element Types");
		
		// ===============
		// We've got all the starting bits/config together now to fetch available
		// Relation Names and existing Relationships.
		// ===============
		
		// Fetch the available relation names for the derived relation type.
		Vector<RelationName> relationNames = 
		 RelationType.getRelationNames(relationType.getRelationTypeId());

		Log.debug("Updating " + relationType.getRelationLabel() + 
		 " relations, common element is " + commonElementType.toString());
		
		// Fetch existing Relationships.
		Vector<Relation> existingRelations;
		
		if (commonRelationElementId == Relation.RelationElementId.ELEMENT_ID1) {
			existingRelations = Relation.getRelations
			 (elementId, null, relationType, null, facade);
			
			relatedElementType = elemType2;
		} else {
			existingRelations = Relation.getRelations
			 (null, elementId, relationType, null, facade);
		
			relatedElementType = elemType1;
		}
		
		Log.debug("Found " + existingRelations.size() + " existing relations.");

		// Extract all unique related element IDs. This will determine the
		// names of all checkbox values that need to be checked to see whether
		// relations have been added/removed.
		HashSet<Integer> relatedElementIds = new HashSet<Integer>();
		
		for (Relation existingRelation : existingRelations) {	
			if (commonRelationElementId == Relation.RelationElementId.ELEMENT_ID1)
				relatedElementIds.add(existingRelation.getElementId2());
			else
				relatedElementIds.add(existingRelation.getElementId1());
		}
		
		Log.debug("Existing relations consist of " + relatedElementIds.size() + 
		 " unique " + relatedElementType.toString() + " elements");
		
		// Add common Element ID and all related Element IDs to the Element Lock map
		ElementLockManager elementLockManager = 
		 ElementLockManager.getElementLockManager();
		
		Vector<Integer> allElementIds = new Vector<Integer>();
		allElementIds.addAll(relatedElementIds);
		allElementIds.add(elementId);
		
		BigInteger lockId = null;
		
		try {
			lockId = elementLockManager.addElementsToLock(
			 m_userData.m_name, 
			 "Method: updateAllRelations, common Element ID: " + elementId, 
			 allElementIds);
		
			facade.beginTransaction();
			
			// Now step through all unique related elements and determine the value
			// for each relation checkbox.
			for (Integer relatedElementId : relatedElementIds) {
				Log.debug("Updating relations for " + 
				 relatedElementType.toString() + " ID " + relatedElementId);
				
				// Step through all available relation names - each one will have
				// an associated checkbox.
				for (RelationName thisRelationName : relationNames) {
					
					boolean hasRelation = CCLAUtils.getBinderBoolValue
					 (m_binder, "relElement_" + relatedElementId + "_" + 
					 thisRelationName.getRelationNameId());
					
					// Check if this relation already exists.
					Relation checkRelation;
					
					// Build the Relation instance which will be checked.
					if (relationType.getElement1Type() == commonElementType) {
						checkRelation = new Relation(0, thisRelationName, 
						 elementId, relatedElementId, null, null, username);
					} else {
						checkRelation = new Relation(0, thisRelationName, 
						 relatedElementId, elementId, null, null, username);
					}
					
					Integer existingRelationId = 
					 Relation.getExistingRelation(existingRelations, checkRelation);
					
					boolean relationExists = (existingRelationId != null);
					
					if (!relationExists && hasRelation) {
						// Add the relation, as it didn't already exist.
						Log.debug("Adding new relation: " + thisRelationName.getRelation());
						
						if (relationType.getElement1Type() == commonElementType) {
							Relation.add(elementId, relatedElementId, 
							 thisRelationName, facade, username);
						} else {
							Relation.add(relatedElementId, elementId,
							 thisRelationName, facade, username);
						}
					
					} else if (relationExists && !hasRelation) {
						// Remove the relation, as it existed previously.
						
						Log.debug("Removing relation: " + thisRelationName.getRelation());
						Relation.remove(existingRelationId, facade, username);
					} else {
						//Log.debug("Ignoring relation: " + thisRelationName.getRelation() + 
						// " (no change)");
					}
				}
			}
			
			facade.commitTransaction();
		
		} catch (Exception e) {
			facade.rollbackTransaction();
			
			String msg = "Failed to update relations: " + e.getMessage();
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		} finally {
			// Remove the ElementLock, if it exists.
			  if (lockId != null) elementLockManager.removeElementLock(lockId);
			 
		}
	}
	
	/** Updates the relations (and properties) between 2 homogenous groups of elements, 
	 *  e.g. all the relations between a particular set of persons and a particular set
	 *  of accounts.
	 *  
	 *  Used to execute actions on the Organisation Bulk Update page. 
	 *  
	 *  This method is not as flexible as updateAllRelations, which is capable
	 *  of adding and removing relationships simultaneously.
	 *  
	 *  Instead, this method will either add particular relations between the
	 *  two element sets, or remove particular relations. 
	 *  
	 *  It is also capable of running in 'Property Update' mode, which is capable of
	 *  adding, updating and removing applied Relation Properties (and adding relations 
	 *  where required). This is used for setting Nominated Withdrawal/Income accounts
	 *  and Nominated Correspondents.
	 *  
	 *  Required parameters in the binder:
	 *  
	 *  -ELEMENT_TYPE_ID_1/ID_2: the Element Type IDs which correspond to the 
	 *   two Element Types whose relations will be updated.
	 *  
	 *  -commonElementIds: list of 'common' element IDs. Any relation updates
	 *   picked up by this method will be applied to all elements in this list.
	 *   
	 *  -relatedElementIds: list of related element IDs. The method will search
	 *   for any selected relation names against these IDs and perform updates
	 *   as required. 
	 *  
	 *  -relationUpdateType: determines whether relations will be added/removed.
	 *   If not set to 'add', existing relations are removed.
	 *  
	 *  Extra parameters required to run Relation Update mode:
	 *  
	 *  -PROPERTY_ID: the Property ID to add/remove from the matched relations
	 *  
	 *  -PROPERTY_STATUS: the Property Status to set against applied Properties
	 *  -PROPERTY_VALUE: the Property Value to set against applied Properties (optional)
	 *  
	 * @throws ServiceException 
	 * @throws DataException 
	 */
	public void addOrRemoveRelations() throws ServiceException, DataException {
		
		Log.debug("RelationService:addOrRemoveRelations:begin");
		
		String userName = m_userData.m_name;
		
		// First determine whether relationships will be created or removed.
		Relation.UpdateType updateType = Relation.UpdateType.ADD;
		
		if (!m_binder.getLocal("relationUpdateType").equals("add"))
			updateType = Relation.UpdateType.REMOVE;
		
		// Fetch and parse the list of common Element IDs.
		String commonElementIdStr = m_binder.getLocal("commonElementIds");
		Vector<Integer> commonElementIds = 
		 CCLAUtils.getIntegerList(commonElementIdStr);
		
		// Fetch and parse the list of related Element IDs.
		String relatedElementIdStr = m_binder.getLocal("relatedElementIds");
		
		Vector<Integer> relatedElementIds = 
		 CCLAUtils.getIntegerList(relatedElementIdStr);

		if (commonElementIds.size() == 0 || relatedElementIds.size() == 0) {
			throw new ServiceException
			 ("No elements selected to apply relation updates.");
		}
		
		// Add elementId and relatedElementIds to the Element Lock map
		ElementLockManager elementLockManager = 
		 ElementLockManager.getElementLockManager();
		
		Vector<Integer> allElementIds = new Vector<Integer>();
		allElementIds.addAll(commonElementIds);
		allElementIds.addAll(relatedElementIds);
		
		// Acquire lock and block until we have it!
		BigInteger lockId = elementLockManager.addElementsToLock(
		 m_userData.m_name, "method: addOrRemoveRelations", allElementIds);

		// Determine if this is a 'single add' of one person/org/account 
		// (relatedElementIds?)
		// this is used by eg when you click 'add person' on the bulk update screen
		// the relationship should be passed as RELATIONSHIP
		boolean singleAdd = false;
		if (!StringUtils.stringIsBlank(m_binder.getLocal("singleAdd")))
			singleAdd = true;
		
		Integer propertyId = CCLAUtils.getBinderIntegerValue
		 (m_binder, Property.Cols.ID);
		
		Log.debug("singleAdd is " + singleAdd);		
		
		if (singleAdd) {
			// the relation name id must be passed in binder as RELATIONSHIP
			String relationship = m_binder.getLocal("RELATIONSHIP");
			if (StringUtils.stringIsBlank(relationship))
				throw new ServiceException("Missing relation name");
			// spoof the value in the binder so it gets picked up later on
			Vector<Integer> relationshipIds = 
				 CCLAUtils.getIntegerList(relationship);
			
			
			for (Integer relationshipId : relationshipIds) {
				String addRelationship = "relElement_" + relatedElementIds.get(0) + "_" + 
				 relationshipId;
				m_binder.putLocal(addRelationship, "on");	
			}
	
		}

		RelationType relationType = RelationType.getRelationTypeFromBinder
		 (m_binder);
		
		if (relationType == null)
			throw new ServiceException("No matching relation type found.");
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		boolean propertyUpdate = false;
		
		// Property update-specific fields.
		Property property = null;
		Boolean propertyStatus = null;
		String propertyValue = null;
		Vector<Element> commonElements = null;
		
		
		// What exactly are we updating? Relationships or Properties.
		String updateItem = "relationships";
		
		if (propertyId != null) {
			property = Property.getCache().getCachedInstance(propertyId);
			
			if (property != null) {
				Log.debug("Found passed Property ID " + propertyId + ": " + 
				 property.getName());
				
				propertyUpdate = true;
				updateItem = "properties";
				
				// Fetch required Property Status and Property Value from the binder.
				propertyStatus = CCLAUtils.getBinderBoolValueAllowNull
				 (m_binder, RelationPropertyApplied.Cols.PROPERTY_STATUS);
				
				if (propertyStatus == null)
					throw new ServiceException
					 ("Property ID was passed, but Property Status missing");
				
				propertyValue = m_binder.getLocal
				 (RelationPropertyApplied.Cols.PROPERTY_VALUE);
				
				// Build list of common Element instances.
				commonElements = Element.getAll(commonElementIds, facade);
				
			} else {
				throw new ServiceException("Invalid Property ID passed: " + propertyId);
			}
		}
		
		// Fetch the available relation names for this relation type.
		Vector<RelationName> relationNames = 
		 RelationType.getRelationNames(relationType.getRelationTypeId());
		
		Log.debug("Updating " + updateItem + " across " + commonElementIds.size() 
		 + " common elements, " + relatedElementIds.size() + " related element" 
		 + " IDs available. Relation type is " + relationType.getRelationLabel());
		
		Log.debug("Bulk relation update type is: " + updateType.toString());
		
		// Prep as much data as possible before we enter the (potentially long) 
		// transaction block.
		Vector<Element> relatedElements = new Vector<Element>();
		
		// Build list of related Element instances
		for (Integer relatedElementId : relatedElementIds)
			relatedElements.add(Element.get(relatedElementId, facade));
		
		// Determine which relations were marked for addition/removal
		CheckedRelations checkedRelations = new CheckedRelations();
		
		for (Element relatedElement : relatedElements) {
			int relatedElementId = relatedElement.getElementId();
			
			Log.debug("Searching for checked relations on element ID: " + 
			 relatedElement.getElementId());
			
			for (RelationName relName : relationNames) {
				// Was this particular relation name marked for 
				// addition/removal?
				boolean markedRelation = !StringUtils.stringIsBlank
				 (m_binder.getLocal("relElement_" + relatedElementId + "_" + 
				 relName.getRelationNameId()));
				
				if (markedRelation) {
					Log.debug("Relation Name " + relName.getRelation() + 
					 " was marked for addition/removal.");
					
					checkedRelations.add(relatedElementId, relName.getRelationNameId());
				}
			}
		}
		
		Log.debug("Found " + checkedRelations.getNumChecked() + " checked relations");
		int updateCount = 0;
		
		try {
			// Step through available list of related Element IDs and look for
			// checked relation boxes.
			for (Element relatedElement : relatedElements) {
				int relatedElementId = relatedElement.getElementId();
	
				Vector<Integer> checkedElementRelations = 
				 checkedRelations.getCheckedRelations(relatedElementId);
				
				if (checkedElementRelations != null && 
					!checkedElementRelations.isEmpty()) {
					
					Log.debug("Updating " + checkedElementRelations.size() + " " +
					 updateItem + " against related element ID: " + relatedElementId);
					
					try {
						// Execute the updates in small transaction blocks.
						facade.beginTransaction();
						
						int tranUpdateCount = 0;
						
						for (RelationName relName : relationNames) {
							// Was this particular relation name marked for 
							// addition/removal?
							if (checkedRelations.isChecked
								(relatedElementId, relName.getRelationNameId())) {
								
								RelationProperty relProperty = null;
								
								if (propertyUpdate) {
									// Determine the RelationProperty for this RelationName
									relProperty = RelationProperty.get
									 (relName.getRelationNameId(), propertyId, facade);
									
									// Fail if no such RelationProperty
									if (relProperty == null)
										throw new ServiceException
										 ("Invalid Relation Name '" +
										 relName.getRelation() + "' (ID " + 
										 relName.getRelationNameId() + 
										 ") for Property ID: " + property.getPropertyId());
								}
								
								updateCount++;
								tranUpdateCount++;
								
								Log.debug("Relation Name " + relName.getRelation() + 
								 " was marked for addition/removal ("  + updateCount + 
								 "/" + checkedRelations.getNumChecked() + ")");
								
								// When in property update mode, we never want to remove
								// existing relationships, only add them as required.
								if (!propertyUpdate
									||
									updateType.equals(Relation.UpdateType.ADD)) {
									Relation.addOrRemoveAll(commonElementIds, relatedElement, 
									 relName, updateType, facade, userName);
								}
								
								if (propertyUpdate) {
									// Add/update/remove property as requested
									RelationPropertyApplied.addUpdateOrRemoveAll
									 (commonElements, relatedElement, relName,
									 relProperty, updateType, propertyValue, propertyStatus, 
									 facade, userName);
								}
							}
						}
						
						Log.debug("Committing " + tranUpdateCount + 
						 " batched relation updates");
						
						facade.commitTransaction();
						
					} catch (Exception e) {
						facade.rollbackTransaction();
						
						Log.error("Failed to bulk add/remove relations", e);
						throw new ServiceException(e);
					}
				}
			}
			
		} finally {
			// Remove the ElementLock, if it exists.
			if (lockId != null)
				elementLockManager.removeElementLock(lockId);
		}
		
		Log.debug("Batch Update completed");
		Log.debug("RelationService:addOrRemoveRelations:end");
	}
	
	/** Stores a mapping of relation names that were checked, for a given set of
	 *  homogenous element IDs.
	 *  
	 * @author Tom
	 *
	 */
	public static class CheckedRelations {
		private HashMap<Integer, Vector<Integer>> checkedRelationMap;
		private int count;
		
		public CheckedRelations() {
			this.checkedRelationMap = new HashMap<Integer, Vector<Integer>>();
			this.count = 0;
		}
		
		public void add(int elementId, int relationNameId) {
			Vector<Integer> checkedElementRelations = checkedRelationMap.get(elementId);
			
			if (checkedElementRelations == null) {
				checkedElementRelations = new Vector<Integer>();
				checkedRelationMap.put(elementId, checkedElementRelations);
			}
			
			checkedElementRelations.add(relationNameId);
			count++;
		}
		
		public boolean isChecked(int elementId, int relationNameId) {
			Vector<Integer> checkedElementRelations = checkedRelationMap.get(elementId);
			
			if (checkedElementRelations == null)
				return false;
			else 
				return (checkedElementRelations.contains(relationNameId));
		}
		
		public Vector<Integer> getCheckedRelations(int elementId) {
			return  checkedRelationMap.get(elementId);
		}
		
		public int getNumChecked() {
			return count;
		}
	}

	/** @deprecated by addOrRemoveRelations
	 * 
	 *  This is identical to the updateAllRelations, except that:
	 *  
	 *  b) each call will either be adding or removing relationships - 
	 *     only one is done at a time
	 *  
	 *  This was used by the Bulk Update views.
	 * 
	 * @throws ServiceException 
	 * @throws DataException 
	 */
	public void updateBulkRelations(int parentId, int relatedId) 
	 throws ServiceException, DataException {
		
		String parentIdStr	= "";
		String relatedIdStr = "";
		
		FWFacade facade 	= CCLAUtils.getFacade(m_workspace, true);
		DataResultSet rsRelatedPersons 	= null;
		
		String username 	= m_userData.m_name;
		
		if (parentId >	0)
			 parentIdStr	= m_binder.getLocal("PARENT_ELEMENT_ID");
		if (relatedId>0)
			 relatedIdStr = m_binder.getLocal("RELATED_ELEMENT_ID");
		
		String relationTypeIdStr	= m_binder.getLocal("RELATION_TYPE_ID");
		String relationAction 		= m_binder.getLocal("ACTION");
		
		if (StringUtils.stringIsBlank(relationAction))
			throw new ServiceException
			("Unable to update element relations, MISSING ACTION");
		
		if (StringUtils.stringIsBlank(relationTypeIdStr))
			throw new ServiceException
			 ("Unable to update element relations, MISSING RELATION_TYPE_ID");
		
		// Maps to RELATION_TYPE_ID
		int relationTypeId	= Integer.parseInt(relationTypeIdStr);
		
		Log.debug("parentIdStr:" + parentIdStr);
		Log.debug("relationTypeIdStr:" + relationTypeIdStr);

		// Fetch the available relation names for this relation type.
		Vector<RelationName> relationNames = 
		 RelationType.getRelationNames(relationTypeId);
		
		RelationType relationType = 
		 RelationType.getCache().getCachedInstance(relationTypeId);
			
		if (relationType == null)
			throw new ServiceException("No matching relation type with ID: " +
			 relationTypeId);
		
		// if this is 1 then need parent element id and
		// get related persons from entity
		if (relationType.getRelationTypeId() == RelationType.ORG_PERSON) {
			if (parentId==0  && StringUtils.stringIsBlank(parentIdStr))
				throw new ServiceException
				 ("Unable to update element relations, MISSING PARENT_ELEMENT_ID");	
			
			if (parentId==0)
				parentId 		= Integer.parseInt(parentIdStr); 
			
			rsRelatedPersons 	= Relation.getRelatedPersonsData
			 (parentId, ElementType.ORGANISATION, facade);
		}
		
		// if it is two then expect related element id
		// and need to use getRelatedAccountsData
		if (relationType.getRelationTypeId() == RelationType.PERSON_ACCOUNT)
		{
			if (relatedId==0 && StringUtils.stringIsBlank(relatedIdStr))
				throw new ServiceException
				 ("Unable to update element relations, MISSING RELATED_ELEMENT_ID");	
			if (relatedId==0)
				relatedId 	= Integer.parseInt(relatedIdStr); 
			
			rsRelatedPersons = Relation.getRelatedEntityAccountPersonsData
			 (CCLAUtils.getBinderIntegerValue
			  (m_binder, "ORGANISATION_ID"), facade);
		}
		
		try {
			facade.beginTransaction();
		
			if (rsRelatedPersons.first()) {
				do {
					String personId = rsRelatedPersons.getStringValueByName("PERSON_ID");
					
					if (relationType.getRelationTypeId() == RelationType.ORG_PERSON)
						relatedId = Integer.parseInt(personId);
					if (relationType.getRelationTypeId() == RelationType.PERSON_ACCOUNT)
						parentId = Integer.parseInt(personId);
					
					relationType = 
					 RelationType.getCache().getCachedInstance(relationTypeId);
					
					// Fetch the existing relationships for this person.
					Vector<Relation> existingRelations = Relation.getRelations
					 (parentId, relatedId, relationType, null, facade);
	
					for (RelationName thisRelation : relationNames) {

						// Fetch the corresponding relationship checkbox value, to determine
						// if the relationship has been flagged or not.
						boolean relationSelected = CCLAUtils.getBinderBoolValue
						 (m_binder, "relPerson_" + personId + "_" + thisRelation);
						
						if (relationSelected) {
							// Check if this relation already exists.
							boolean relationExists = false;
							Iterator<Relation> j = existingRelations.iterator();
							
							Integer existingRelationId = null;
							
							for (Relation existingRelation : existingRelations) {
								if (existingRelation.getElementId2() == relatedId
									&&
									existingRelation.equals(thisRelation)) {
									
									// Existing relationship found.
									existingRelationId = existingRelation.getRelationId();
									relationExists = true;
									
									break;
								}
							}
						
							if (!relationExists && 
								relationAction.equalsIgnoreCase("Add")) {								
								// Add the relation, if it didn't already exist.
								Log.debug("Adding new relation: " + thisRelation);
								Relation.add(parentId, relatedId, thisRelation,
								 facade, username);
								
							} else if (relationExists && 
								relationAction.equalsIgnoreCase("Remove")) {
								// Remove the relation, if it existed previously.
								Log.debug("Removing relation: " + thisRelation);
								Relation.remove(existingRelationId, facade, username);
								
							} else {
								Log.debug("Ignoring relation: " + thisRelation + 
								 " (no change)");
							}
						}
					}

				} while (rsRelatedPersons.next());
				
				facade.commitTransaction();
			}
			
		} catch (DataException e) {
			facade.rollbackTransaction();
			
			String msg = "Failed to update Client-Person relations: " + e.getMessage();
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}	
	}		
	
	/** Fetches 3 ResultSet of Person relationships for a given ELEMENT_ID.
	 *  
	 *  rsRelatedPersons contains unique entries from the Persons table for
	 *  all related Persons.
	 *  
	 *  rsPersonRelations contains the relationship data.
	 *  
	 *  rsPersonRelationMap contains the relationship data in a format more
	 *  suited for display.
	 *  
	 *  This method works for both Entity-Person and Person-Account relations.
	 *  
	 * @throws DataException 
	 * @throws NumberFormatException 
	 * @throws ServiceException 
	 */
	public void getRelatedPersons() throws DataException, ServiceException {
		
		Integer elementId = CCLAUtils.getBinderIntegerValue
		 (m_binder, SharedCols.ELEMENT);
		
		if (elementId == null) {
			throw new ServiceException("Unable to fetch related persons, " +
			 SharedCols.ELEMENT + " was missing");
		}
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		// Determine the element type first. If this is an Account, we need
		// to call a different query to fetch the related persons.
		Element element = Element.get(elementId, facade);
		
		// Now determine the Relation Type required to fetch the relation
		// data.
		RelationType relType = RelationType.getRelationType
		 (element.getType(), ElementType.PERSON);
		
		DataResultSet rsPersonRelations = null;
		
		DataResultSet rsRelatedPersons = Relation.getRelatedPersonsData
		 (elementId, element.getType(), facade);
		
		if (relType.getElement1Type().equals(ElementType.PERSON))
			rsPersonRelations = Relation.getRelationData
			 (null, elementId, relType, null, facade);
		else
			rsPersonRelations = Relation.getRelationData
			 (elementId, null, relType, null, facade);
		
		m_binder.addResultSet("rsRelatedPersons", rsRelatedPersons);
		m_binder.addResultSet("rsPersonRelations", rsPersonRelations);

		// Add Relation Map (assists in UI rendering)
		DataResultSet rsPersonRelationMap = null;
		
		if (relType.getElement1Type().equals(ElementType.PERSON))
			rsPersonRelationMap = Relation.getRelationMap(ElementType.PERSON, 
			 null, elementId, relType, null, facade);
		else
			rsPersonRelationMap = Relation.getRelationMap(ElementType.PERSON, 
			 elementId, null, relType, null, facade);
		
		m_binder.addResultSet("rsPersonRelationMap", rsPersonRelationMap);
		
		// Add applied properties
		DataResultSet rsPersonRelationProperties = null;
		
		if (relType.getElement1Type().equals(ElementType.PERSON))
			rsPersonRelationProperties = 
			 RelationPropertyApplied.getByRelationsData(null, 
			 elementId, relType, null, facade);
		else
			rsPersonRelationProperties = 
			 RelationPropertyApplied.getByRelationsData(elementId, 
			 null, relType, null, facade);
		
		m_binder.addResultSet
		 ("rsPersonRelationProperties", rsPersonRelationProperties);
	}
	
	/** Fetches 3 ResultSets of BankAccount relationships for a given ELEMENT_ID.
	 *  
	 *  
	 * @throws DataException 
	 * @throws NumberFormatException 
	 * @throws ServiceException 
	 */
	public void getRelatedBankAccounts() throws DataException, ServiceException {
		
		String elementIdStr = m_binder.getLocal("ELEMENT_ID");
		
		if (StringUtils.stringIsBlank(elementIdStr)) {
			throw new ServiceException("Unable to fetch related bank accounts, " +
			 "ELEMENT_ID was missing");
		}
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		int elementId	= Integer.parseInt(elementIdStr);
		
		// Determine the element type first.
		Element element = Element.get(elementId, facade);
		
		// Now determine the Relation Type required to fetch the relation
		// data.
		RelationType relType = RelationType.getRelationType
		 (element.getType(), ElementType.BANK_ACCOUNT);
		
		DataResultSet rsBankAccountRelations = null;
		
		DataResultSet rsRelatedBankAccounts = Relation.getRelatedBankAccountsData
		(element.getElementId(), facade);
		
		if (relType.getElement1Type().equals(ElementType.ACCOUNT))
			rsBankAccountRelations = Relation.getRelationData
			 (elementId, null, relType, null, facade);
		else // Not used yet. BANK_ACCOUNTs always appear as ELEMENT_ID2 in Relations
			rsBankAccountRelations = Relation.getRelationData
			 (elementId, null, relType, null, facade);
		
		m_binder.addResultSet("rsRelatedBankAccounts", rsRelatedBankAccounts);
		m_binder.addResultSet("rsBankAccountRelations", rsBankAccountRelations);
		
		// Add Relation Map (assists in UI rendering)
		DataResultSet rsBankAccountRelationMap = null;
		
		rsBankAccountRelationMap = Relation.getRelationMap(ElementType.BANK_ACCOUNT, 
		 elementId, null, relType, null, facade);
		
		m_binder.addResultSet("rsBankAccountRelationMap", rsBankAccountRelationMap);
		
		
		DataResultSet rsBankAccountRelationProperties = 
		 RelationPropertyApplied.getByRelationsData
		 (elementId, null, relType, null, facade);
		
		m_binder.addResultSet
		 ("rsBankAccountRelationProperties", rsBankAccountRelationProperties);
	}
	
	/** Returns similar output to getRelatedPersons, except this fetches all
	 *  Person relationships for all accounts belonging to a particular entity.
	 *  
	 *  Uses out-dated Relation fetch method.
	 *  
	 * @throws DataException
	 * @throws ServiceException
	 */
	@Deprecated
	public void getRelatedEntityAccountPersons() 
	 throws DataException, ServiceException {
		
		String entityIdStr = m_binder.getLocal("ORGANISATION_ID");
		
		if (StringUtils.stringIsBlank(entityIdStr)) {
			throw new ServiceException("Unable to fetch related entity " +
			 "account persons, ORGANISATION_ID was missing");
		}
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		int entityId	= Integer.parseInt(entityIdStr);
		
		DataResultSet rsRelatedPersons = null;
		DataResultSet rsPersonRelations = null;
		
		// Person-Account relation
		rsRelatedPersons = Relation.getRelatedEntityAccountPersonsData
		 (entityId, facade);
		
		Vector<Account> accounts = Account.getAccounts(
		 Account.getClientAccountsData(entityId, facade));
		
		Vector<Element> elements = new Vector<Element>();
		
		for (Account account : accounts) {
			Element elem = new Element
			 (account.getElementId(), account.getType());
			
			elements.add(elem);
		}
		RelationType relType = RelationType.getCache().getCachedInstance
		(RelationType.PERSON_ACCOUNT);
		
		rsPersonRelations = Relation.getMultipleRelationsData
		 (null, elements, relType, null, facade);
		
		m_binder.addResultSet("rsRelatedPersons", rsRelatedPersons);
		m_binder.addResultSet("rsPersonRelations", rsPersonRelations);
	}
	
	/** Fetches 4 ResultSet of Entity relationships for a given 
	 *  ELEMENT_ID.
	 *  
	 *  Used for Entity-Person relationships, but should work for any relationship type
	 *  that involves Organisations (CHECK!)
	 *  
	 * @throws ServiceException 
	 *  
	 * @throws DataException 
	 * @throws NumberFormatException 
	 * @throws ServiceException 
	 * @throws DataException 
	 */
	public void getRelatedEntities() throws ServiceException, DataException {
		
		String elementIdStr = m_binder.getLocal("ELEMENT_ID");
		
		if (StringUtils.stringIsBlank(elementIdStr)) {
			throw new ServiceException("Unable to fetch related entities, " +
			 "ELEMENT_ID was missing");
		}
		
		int elementId = Integer.parseInt(elementIdStr);
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		// Determine the element type first.
		Element element = Element.get(elementId, facade);
		
		// Now determine the Relation Type required to fetch the relation
		// data.
		RelationType relType = RelationType.getRelationType
		 (element.getType(), ElementType.ORGANISATION);
		
		DataResultSet rsRelatedEntities = Relation.getRelatedOrgsData
		 (elementId, facade);

		DataResultSet rsEntityRelations = Relation.getRelationData
		 (null, elementId, null, null, facade);
		
		m_binder.addResultSet("rsRelatedEntities", rsRelatedEntities);
		m_binder.addResultSet("rsEntityRelations", rsEntityRelations);
		
		// Add Relation Map (assists in UI rendering)
		DataResultSet rsEntityRelationMap = null;
		
		if (relType.getElement1Type().equals(ElementType.ORGANISATION))
			rsEntityRelationMap = Relation.getRelationMap(ElementType.ORGANISATION, 
			 null, elementId, relType, null, facade);
		else
			rsEntityRelationMap = Relation.getRelationMap(ElementType.ORGANISATION, 
			 elementId, null, relType, null, facade);
		
		m_binder.addResultSet("rsEntityRelationMap", rsEntityRelationMap);
		
		// Add applied properties
		DataResultSet rsEntityRelationProperties = null;
		
		if (relType.getElement1Type().equals(ElementType.ORGANISATION))
			rsEntityRelationProperties = RelationPropertyApplied.getByRelationsData
			 (null, elementId, relType, null, facade);
		else
			rsEntityRelationProperties = RelationPropertyApplied.getByRelationsData
			 (elementId, null, relType, null, facade);
		
		m_binder.addResultSet
		 ("rsEntityRelationProperties", rsEntityRelationProperties);	
	}

	/** @deprecated by EntityService.getEntityBulkUpdate
	 * 
	 * Fetches entity data, suitable for use on a bulk update screen.

	 * @throws ServiceException if no matching entity is found in the DB
	 * @throws DataException 
	 */
	public void doEntityBulkUpdate() throws ServiceException, DataException {

		// if this is passed then only need to update one person with 
		// relationship to multiple accounts
		boolean isSingleAdd = false;

		String singleAdd = m_binder.getLocal("singleAdd");
		if (!StringUtils.stringIsBlank(singleAdd))
			isSingleAdd = true;
		Log.debug("isSingleAdd is " + isSingleAdd);

		// need to get the list of accounts for the entity
		//DataResultSet rsAccounts = 
		//	Account.getClientAccountsData(entityId, facade);
		//Log.debug("number of accounts is " + rsAccounts.getNumRows());
		
		// Fetch and parse the list of selected Account IDs on which to
		// perform the bulk update.
		String selectedAccountIds = m_binder.getLocal("selectedAccountIds");
		Vector<Integer> accountIds = 
		 CCLAUtils.getIntegerList(selectedAccountIds);
		
		Log.debug("Updating relationships across " + accountIds.size() 
		 + " accounts");
		
		for (Integer accountId : accountIds) {
			Log.debug("Updating account: " + accountId);
			
			if (isSingleAdd) {
				Log.debug("calling addRelation");
				CCLAUtils.addQueryIntParamToBinder
				 (m_binder, "RELATED_ELEMENT_ID", accountId);
				
				this.addRelation(); 
				
			} else {
				this.updateBulkRelations(0, accountId);	
			}
		}
	}
	
	/** 
	 *  TODO: refactor to use similar methods to updateAllRelations.
	 *  
	 * Switches two people in a particular role.
	 * 
	 * Expects removePerson as the person_id of the person to remove from a role
	 * PARENT_ELEMENT_ID as the person_id of the person to add to the role, RELATION_TYPE_ID as the
	 * integer type of relation, RELATIONSHIP as the
	 *
	 * @throws ServiceException if no matching entity is found in the DB
	 * @throws DataException 
	 */
	public void switchRelatedPersonBulk() throws ServiceException, DataException
	{
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		String removePerson = m_binder.getLocal("removePerson");
		if (StringUtils.stringIsBlank(removePerson))
			throw new ServiceException ("Cannot switch people: missing removePerson value");
		String parentId = m_binder.getLocal("PARENT_ELEMENT_ID");
		if (StringUtils.stringIsBlank(parentId))
			throw new ServiceException ("Cannot switch people: missing parentId value");
		String relationType = m_binder.getLocal("RELATION_TYPE_ID");
		if (StringUtils.stringIsBlank(relationType))
			throw new ServiceException ("Cannot switch people: missing relationType value");
		String entity = m_binder.getLocal("ORGANISATION_ID");
		if (StringUtils.stringIsBlank(entity))
			throw new ServiceException ("Cannot switch people: missing entity value");
		int entityId = Integer.parseInt(entity);
		DataResultSet rsAccounts = 
			Account.getClientAccountsData(entityId, facade);
		Log.debug("number of accounts is " + rsAccounts.getNumRows());
		do 
		{
			String accountId = rsAccounts.getStringValueByName("ACCOUNT_ID");
			boolean hasRelation = CCLAUtils.getBinderBoolValue
			(m_binder, "chk_" + accountId);
			if (!StringUtils.stringIsBlank(accountId) && hasRelation)
			{
				Log.debug("matched " + accountId);
				int accId = Integer.parseInt(accountId);
				switchPersonsRoles(Integer.parseInt(removePerson),
				 Integer.parseInt(parentId), Integer.parseInt(relationType),
				 accId, facade, m_binder);
			}
		} while (rsAccounts.next());		
	}
	
	public void switchPersonsRoles(int removePerson, int addPerson, 
	 int relationTypeId, int relatedElementId,
	 FWFacade facade, DataBinder binder)
	 throws ServiceException, DataException {
		
		String username = m_userData.m_name;
		// remove old relationship
		
		Vector<Relation> existingRelationsRemove = Relation.getRelations
		 (removePerson, relatedElementId, null, null, facade);
		// Fetch the available relation names for this relation type.
		
		RelationType relType = 
	     RelationType.getCache().getCachedInstance
		 (relationTypeId);
		
		// Fetch the available relation names for this relation type.
		Vector<RelationName> relationNames = 
		 RelationType.getRelationNames(relationTypeId);
		
		// Check if this relation exists.
		boolean oldRelationExists = false;
	
		Integer removeExistingRelationId = null;

		try {
			facade.beginTransaction();
			
			for (RelationName thisRelationName : relationNames) {	
				Log.debug("looking for role:" + thisRelationName);
				
				for (Relation existingRelation : existingRelationsRemove) {	
					
					if (existingRelation.getElementId2() == relatedElementId
						&&
						existingRelation.getRelationName().equals(thisRelationName)) {
						
						// Existing relationship found.
						Log.debug("found relationship:" +  thisRelationName.getRelation());
						removeExistingRelationId = existingRelation.getRelationId();
						oldRelationExists = true;
						
						if (oldRelationExists) {
							// remove relationship
							Log.debug("removing relationship:" + thisRelationName.getRelation());
							Relation.remove(removeExistingRelationId, facade, username);
							// add relationship
							// Fetch the existing relationships for this person.
							
							Vector<Relation> existingRelations = Relation.getRelations
							 (addPerson, relatedElementId, relType, null, facade);
							
							// Check if this relation already exists.
							boolean relationExists = false;
							Iterator<Relation> j = existingRelations.iterator();
							
							Integer existingRelationId = null;
							
							while (j.hasNext()) {
								Relation newexistingRelation = j.next();
								
								if (newexistingRelation.getElementId2() == relatedElementId
									&&
									newexistingRelation.getRelationName().equals(thisRelationName)) {
									
									// Existing relationship found.
									existingRelationId = newexistingRelation.getRelationId();
									relationExists = true;
									
									break;
								}
							}
							if (!relationExists)
							{
								Relation.add(addPerson, relatedElementId,
								 thisRelationName, facade, username);
								
								Log.debug("added relationship:" + thisRelationName.getRelation());
							}
						}
					}
				}
			}
			facade.commitTransaction();

		} catch (Exception e) {
			facade.rollbackTransaction();
			Log.debug("Error whilst switching people");
			
			throw new ServiceException(
			 "There was an error whilst switching the people: " + e.getMessage());
		}
	}
	
	/**
	 * Gets persons relations to accounts that are both related to organisation
	 * @throws ServiceException
	 * @throws DataException
	 */
	public void getAllRelatedEntityAccountPersons() throws ServiceException, DataException {
		
		String orgIdStr = m_binder.getLocal("ORGANISATION_ID");
		
		if (StringUtils.stringIsBlank(orgIdStr)) {
			throw new ServiceException("Unable to fetch only related entity acount person, " +
			 "ORGANISATION_ID was missing");
		}
		
		int orgId = Integer.parseInt(orgIdStr);
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);

		DataResultSet rsAllRelatedEntityAccountPersons = null;
		
		rsAllRelatedEntityAccountPersons = 
				Relation.getAllRelatedEntityAccountPersonsData(orgId, facade);

		
		m_binder.addResultSet("rsAllRelatedEntityAccountPersons", rsAllRelatedEntityAccountPersons);
	}
	
	/** Updates the user-defined PropertyApplied objects for a given Relation.
	 * @throws ServiceException 
	 * @throws DataException 
	 * 
	 */
	public void updateAppliedRelationProperties() 
	 throws ServiceException, DataException {
		
		Integer relationId = CCLAUtils.getBinderIntegerValue(m_binder, "RELATION_ID");
		
		if (relationId == null)
			throw new ServiceException("Unable to update relation properties, " +
			 "RELATION_ID missing");
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		Relation relation = Relation.get(relationId, facade);
		
		if (relation == null)
			throw new ServiceException("Unable to update relation properties, " +
			 "relation ID " + relationId + " not found");
		
		try {
			facade.beginTransaction();
			
			RelationPropertyApplied.updateRelationPropertiesApplied
			 (relation, m_binder, facade, m_userData.m_name);
			
			facade.commitTransaction();
			
		} catch (Exception e) {
			facade.rollbackTransaction();
			
			String msg = "Unable to update relation properties";
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}
}
