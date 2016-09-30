package com.ecs.ucm.ccla.data;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Vector;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.shared.SharedObjects;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.CacheManager;
import com.ecs.ucm.ccla.data.ElementType.SecondaryElementType;
import com.ecs.ucm.ccla.experian.AuthenticationScoreUtils;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Models entries from the RELATIONS table in the CCLA tablespace.
 * 
 *  Reliant on the RelationName/RelationTypeCache instances.
 * 
 * @author Tom
 *
 */
public class Relation implements Persistable {
	
	private int relationId;
	private RelationName relationName;
	
	private Integer elementId1;
	private Integer elementId2;

	private Date relationDate;
	private Date lastUpdated;
	private String lastUpdatedBy;
	
	// Test flag used for delaying add/remove actions by a given time period.
	public static boolean DELAY_ADD_AND_REMOVE_OPERATIONS = !StringUtils.stringIsBlank
	 (SharedObjects.getEnvironmentValue("CCLA_CS_DelayAddAndRemoveOperations"));
	
	public static boolean LOG_QUERIES = !StringUtils.stringIsBlank
	 (SharedObjects.getEnvironmentValue("CCLA_CS_LogRelationQueries"));
	
	public static class Cols {
		public static final String ID = "RELATION_ID";
		public static final String ELEMENT_ID1 = "ELEMENT_ID1";
		public static final String ELEMENT_ID2 = "ELEMENT_ID2";
		
		public static final String DATE = "RELATION_DATE";
		
		// Special columns returned in aggregate queries
		public static final String NUM_VERIFIED_SOURCES = "NUM_VERIFIED_SOURCES";
		public static final String NUM_UNVERIFIED_SOURCES = "NUM_UNVERIFIED_SOURCES";
	}
	
	public static enum UpdateType {
		ADD, REMOVE
	}
	
	/** Used when identifying which 'side' of an element relationship we are referring
	 *  to in a given instance.
	 *  
	 * @author Tom
	 *
	 */
	public static enum RelationElementId {
		ELEMENT_ID1,
		ELEMENT_ID2
	}
	
	private static final String VERIFIED_SOURCE_APPEND_SQL =
	 "LEFT JOIN (SELECT RELATION_ID AS VER_RELATION_ID, " +
	 "COUNT(*) AS NUM_VERIFIED_SOURCES " +
	 "FROM RELATION_PROPERTY_APPLIED relPropAppl " +
	 "INNER JOIN REF_RELATION_PROPERTY relProp ON " + 
	 "(relpropappl.PROPERTY_STATUS = 1 AND " +
	 "relpropappl.relation_property_id = relprop.relation_property_id) " +
     "INNER JOIN REF_PROPERTY prop ON (relProp.property_id = prop.property_id) " +
     "WHERE prop.VERIFICATION_SOURCE_ID IS NOT NULL " +
     "GROUP BY RELATION_ID) verRelProps ON " +
     "(rel.RELATION_ID = verRelProps.VER_RELATION_ID) " +
     
     "LEFT JOIN (SELECT RELATION_ID AS UNVER_RELATION_ID, " +
	 "COUNT(*) AS NUM_UNVERIFIED_SOURCES " +
	 "FROM RELATION_PROPERTY_APPLIED relPropAppl " +
	 "INNER JOIN REF_RELATION_PROPERTY relProp ON " + 
	 "(relpropappl.PROPERTY_STATUS = 0 AND " +
	 "relpropappl.relation_property_id = relprop.relation_property_id) " +
     "INNER JOIN REF_PROPERTY prop ON (relProp.property_id = prop.property_id) " +
     "WHERE prop.VERIFICATION_SOURCE_ID IS NOT NULL " +
     "GROUP BY RELATION_ID) unverRelProps ON " +
     "(rel.RELATION_ID = unverRelProps.UNVER_RELATION_ID) ";
	
	public Relation(int relationId, RelationName relationName,
		Integer elementId1, Integer elementId2,
		Date relationDate, Date lastUpdated, String lastUpdatedBy) 
		throws DataException {

		this.relationId = relationId;
		this.elementId1 = elementId1;
		this.elementId2 = elementId2;
		
		this.relationName = relationName;
		
		this.relationDate = relationDate;
		this.lastUpdated = lastUpdated;
		this.lastUpdatedBy = lastUpdatedBy;
	}
	
	public static Relation get(int relationId, FWFacade facade) 
	 throws DataException {
		
		DataResultSet rsRelation = getData(relationId, facade);
		return get(rsRelation);
	}
	
	public static Relation get(DataResultSet rs) throws DataException {
		
		if (rs.isEmpty())
			return null;
		
		Integer relationNameId = 
		 CCLAUtils.getResultSetIntegerValue(rs, "RELATION_NAME_ID");
		
		RelationName relationName = null;
		
		if (relationNameId != null) {
			relationName = RelationName.getCache().getCachedInstance(relationNameId);
		}
		
		return new Relation(
		 CCLAUtils.getResultSetIntegerValue(rs, Cols.ID),
		 relationName,
		 CCLAUtils.getResultSetIntegerValue(rs, Cols.ELEMENT_ID1),
		 CCLAUtils.getResultSetIntegerValue(rs, Cols.ELEMENT_ID2),
		 rs.getDateValueByName(Cols.DATE),
		 rs.getDateValueByName(SharedCols.LAST_UPDATED),
		 rs.getStringValueByName(SharedCols.LAST_UPDATED_BY)
		);
	}
	
	/** Fetches data for a single relation.
	 *  
	 * @param relationId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getData(int relationId, FWFacade facade) 
	 throws DataException {
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "RELATION_ID", relationId);
		
		return facade.createResultSet
		 ("qClientServices_GetRelation", binder);
	}
	
	/** Fetches data for a single relation.
	 *  
	 *  The target query joins to the REF_RELATION_NAMES and REF_RELATION_TYPES tables.
	 * 
	 * @param relationId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getExtendedData(int relationId, FWFacade facade) 
	 throws DataException {
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "RELATION_ID", relationId);
		
		return facade.createResultSet
		 ("qClientServices_GetRelationExtended", binder);
	}
	
	/** Determines whether a particular relationship already exists. 
	 * 
	 * @param elementId1
	 * @param elementId2
	 * @param relationTypeId
	 * @param relationship
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static boolean relationExists
	 (int elementId1, int elementId2, 
	  RelationName relName, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.ELEMENT_ID1, elementId1);
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.ELEMENT_ID2, elementId2);

		CCLAUtils.addQueryIntParamToBinder
		 (binder, "RELATION_NAME_ID", relName.getRelationNameId());
		
		DataResultSet rsRelation = 
		 facade.createResultSet("qClientServices_GetRelationByValues", binder);
		
		return !rsRelation.isEmpty();
	}
	
	/** Constructs a Relation ID mapping used by the Audit method.
	 *  
	 *  Contains 3 entries:
	 *  
	 *  <Relation ID>  = Relation
	 *  <Element ID 1> = <Element ID 1 type>
	 *  <Element ID 2> = <Element ID 2 type>
	 * 
	 * 
	 */
	public static HashMap<Integer, String> getAuditRelations(Relation relation) {
		
		String elemType1 = 
		 relation.getRelationName().getRelationType().getElement1Type().getName();
		String elemType2 = 
		 relation.getRelationName().getRelationType().getElement2Type().getName();
		
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		
		auditRelations.put(relation.getRelationId(), 
		 ElementType.SecondaryElementType.Relation.toString());
		
		auditRelations.put(relation.getElementId1(), elemType1);
		auditRelations.put(relation.getElementId2(), elemType2);
		
		return auditRelations;
	}
	
	/** Adds a new Element Relation with the given parameters, and performs
	 *  any required cascading relation updates.
	 *  
	 * @param elementId1
	 * @param elementId2
	 * @param relationTypeId
	 * @param relationship
	 * @param relationFlags
	 * @param relationDate
	 * @param lastUpdated
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Relation add(int elementId1, int elementId2,
	 RelationName relationName,
	 FWFacade facade, String username) throws DataException {

		// this will add any synched relations via the REF_RELATION_NAMES_SYNC table
		addOrRemoveSyncRelation
		 (elementId1, elementId2, relationName, "Add", facade, username);
		
		// original relation to add
		return addSingle(elementId1, elementId2, 
				 relationName, facade, username);
	}
	
	/** Sleep for a given delay period, used to test element locking.
	 * 
	 * @throws DataException
	 */
	private static void delay() throws DataException {
		
		if (DELAY_ADD_AND_REMOVE_OPERATIONS) {
			SystemConfigVar delayPeriodVar = 
			 SystemConfigVar.getCache().getCachedInstance
			 ("Relations_AddAndRemoveDelayPeriod");
			
			// Default delay period.
			int delayPeriod = 3000;
			
			if (delayPeriodVar != null) {
				delayPeriod = delayPeriodVar.getIntValue();
			}
			
			try {
				Log.debug("Thread " + Thread.currentThread().getName() + 
				 " Artificial sleep on add/remove relation for " 
				 + delayPeriod + "ms...");
				Thread.sleep(delayPeriod);
				Log.debug("Thread " + Thread.currentThread().getName()  + " waking up!");
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/** Adds a single Element Relation.
	 * 
	 * @param elementId1
	 * @param elementId2
	 * @param relationName
	 * @param facade
	 * @param username
	 * @return
	 * @throws DataException
	 */
	private static Relation addSingle(int elementId1, int elementId2,
	 RelationName relationName,
	 FWFacade facade, String username) throws DataException {
		
		delay();
		
		int relationId = Integer.parseInt(
		 CCLAUtils.getNewKey("ElementRelation", facade));
		
		Relation relation = new Relation(
		 relationId,
		 relationName,
		 elementId1,
		 elementId2,
		 null,
		 null,
		 username
		);
		
		relation.validate(facade);
		
		DataBinder binder = new DataBinder();
		relation.addFieldsToBinder(binder);
		
		facade.execute("qClientServices_AddRelation", binder);

		// Add audit record
		DataResultSet newData = Relation.getData(relation.getRelationId(), facade);
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.ADD.toString(), 
		 ElementType.SecondaryElementType.Relation.toString(), 
		 null, newData, getAuditRelations(relation));
	
		return relation;
	}
	
	/** Removes an existing relation, and performs any required cascading
	 *  relation updates.
	 * 
	 * @param relationId
	 * @param facade
	 * @param username
	 * @throws DataException
	 */
	public static void remove
	 (int relationId, FWFacade facade, String username) throws DataException {
		
		delay();
		
		Relation relation = Relation.get(relationId, facade);
		// get the relationName
		RelationName relationName = relation.getRelationName();
		int elementId1 = relation.getElementId1();
		int elementId2 = relation.getElementId2();
		
		addOrRemoveSyncRelation(elementId1, elementId2, relationName, "Remove", facade, username);

		removeSingle(relationId, facade, username);
	}

	/** Removes a single existing relation.
	 * 
	 * @param relationId
	 * @param facade
	 * @param username
	 * @throws DataException
	 */
	private static void removeSingle
	 (int relationId, FWFacade facade, String username) throws DataException {
	
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "RELATION_ID", relationId);
		
		// Fetch the existing relation to perform audit action afterwards.
		Relation relation = get(relationId, facade);
		
		if (relation == null)
			throw new DataException
			 ("Relation ID " + relationId  + " not found.");
		
		DataBinder auditBinder = new DataBinder();
		relation.addFieldsToBinder(auditBinder);
		
		// remove any applied relation properties
		Vector<RelationPropertyApplied> relProps = 
		 RelationPropertyApplied.getByRelation(relationId, facade);
		
		if (!relProps.isEmpty()) {
			for (RelationPropertyApplied relProp : relProps)
				RelationPropertyApplied.remove(relProp, facade, username);
		}
		
		DataResultSet beforeData = Relation.getData(relation.getRelationId(), facade);
		
		// Remove the relation.
		facade.execute("qClientServices_RemoveRelation", binder);
		
		// Create Audit Record
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.DELETE.toString(), 
		 ElementType.SecondaryElementType.Relation.toString(), 
		 beforeData, null, getAuditRelations(relation));
	}	
		
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "RELATION_ID", this.getRelationId());
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "RELATION_NAME_ID", this.getRelationName().getRelationNameId());
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "ELEMENT_ID1", this.getElementId1());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "ELEMENT_ID2", this.getElementId2());
		
		CCLAUtils.addQueryParamToBinder(binder, "LAST_UPDATED_BY", 
		 this.getLastUpdatedBy());
	}

	public void persist(FWFacade facade, String username) throws DataException {
		this.validate(null);
		
		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);
		
		DataResultSet beforeData = Relation.getData(this.getRelationId(), facade);
		
		facade.execute("qClientServices_UpdateRelation", binder);
		
		DataResultSet afterData = Relation.getData(this.getRelationId(), facade);
	
		// Add audit record
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.UPDATE.toString(), 
		 com.ecs.ucm.ccla.data.ElementType.SecondaryElementType.Relation.toString(), 
		 beforeData, afterData, getAuditRelations(this));	
	}

	public void setAttributes(DataBinder binder) throws DataException {
		
		this.setElementId1(CCLAUtils.getBinderIntegerValue
		 (binder, "ELEMENT_ID1"));
		this.setElementId2(CCLAUtils.getBinderIntegerValue
		 (binder, "ELEMENT_ID2"));

		Integer relationNameId = CCLAUtils.getBinderIntegerValue
		 (binder, "RELATION_NAME_ID");
			
		RelationName thisRelationName = null;
		
		if (relationNameId != null) {
			thisRelationName = RelationName.getCache().getCachedInstance
			 (relationNameId);
		}
		
		this.setRelationName(thisRelationName);
	}
	
	/** Perform basic validation checks. If one fails, a DataException
	 *  is thrown.
	 *  
	 * @throws DataException
	 */
	public void validate(FWFacade facade) throws DataException {		
		
		if (relationName == null)
			throw new DataException("No relation name ID specified");
		
		if (elementId1 == null || elementId2 == null)
			throw new DataException("Relation element IDs are missing");
		
		if (elementId1 == elementId2)
			throw new DataException(
			 "Can't have a relationship between the same element");
		
		if (lastUpdatedBy == null)
			throw new DataException("Last Updated By field value is missing");
	}

	public int getRelationId() {
		return relationId;
	}

	public void setRelationId(int relationId) {
		this.relationId = relationId;
	}

	public int getElementId1() {
		return elementId1;
	}

	public void setElementId1(int elementId1) {
		this.elementId1 = elementId1;
	}

	public int getElementId2() {
		return elementId2;
	}

	public void setElementId2(int elementId2) {
		this.elementId2 = elementId2;
	}

	public Date getRelationDate() {
		return relationDate;
	}

	public void setRelationDate(Date relationDate) {
		this.relationDate = relationDate;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	
	/** Fetches ElementRelation instances derived from the passed
	 *  parameters. See the getRelationData for more information
	 *  on the parameters.
	 *  
	 * @param elementId1
	 * @param elementId2
	 * @param relationTypeId
	 * @param relationship
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Vector<Relation> getRelations
	 (Integer elementId1, Integer elementId2, RelationType relationType, 
	  RelationName relationName, FWFacade facade) throws DataException {
		
		DataResultSet rsRelations = getRelationData(elementId1, 
		 elementId2, relationType, relationName, facade);
		
		Vector<Relation> elementRelations = 
		 new Vector<Relation>();
		
		if (rsRelations.first()) {
			do {
				Relation relation = Relation.get(rsRelations);
				elementRelations.add(relation);
				
			} while (rsRelations.next());
		}
		
		return elementRelations;
	}
		
	
	/** Fetches raw data from the Element Relations table for the
	 *  corresponding elementId1/elementId2/relationTypeId values.
	 *  
	 *  Either the elementId1 or elementId2 parameter must always be
	 *  specified to allow the query to execute. Other fields are
	 *  optional.
	 *  
	 * @param elementId1
	 * @param elementId2
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getRelationData
	 (Integer elementId1, Integer elementId2, RelationType relationType, 
	  RelationName relationName, FWFacade facade) throws DataException {

		Vector<Element> elementId1List = new Vector<Element>();
		Vector<Element> elementId2List = new Vector<Element>();
		
		if (elementId1 != null)
			elementId1List.add(new Element(elementId1, null));
		
		if (elementId2 != null)
			elementId2List.add(new Element(elementId2, null));
		
		return getMultipleRelationsData(elementId1List, elementId2List, 
		 relationType, relationName, facade);
	}
	
	/** Fetches raw data from the Element Relations table for the
	 *  corresponding elementId1/elementId2/relationTypeId/relationNameId 
	 *  values.
	 *  
	 *  Either the elementId1List or elementId2List parameter must 
	 *  always be non-empty to allow the query to execute. Other fields 
	 *  are optional.
	 *  
	 * @param elementId1List
	 * @param elementId2List
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getMultipleRelationsData
	 (Vector<? extends Element> elementId1List, 
	  Vector<? extends Element> elementId2List, 
	  RelationType relationType, RelationName relationName, FWFacade facade) 
	  throws DataException {
		
		if (elementId1List.size() > 1000)
			throw new DataException
			 ("Unable to fetch multiple relations - too many elements (" + 
			 elementId1List.size() + ")");
		
		if (elementId2List.size() > 1000)
			throw new DataException
			 ("Unable to fetch multiple relations - too many elements (" + 
			 elementId2List.size() + ")");
		
		String query = getRelationQueryText("*", true, "rel.RELATION_NAME_ID",
		 elementId1List, elementId2List, relationType, relationName, facade);
		
		if (LOG_QUERIES)
			Log.debug("Executing relation fetch query: " + query);
		
		return facade.createResultSetSQL(query);
	}
	
	public static Vector<Relation> getAll
	 (DataResultSet rsRelations) throws DataException {
		
		Vector<Relation> relations = new Vector<Relation>();
		
		if (rsRelations.first()) {
			do {
				relations.add(get(rsRelations));
			} while (rsRelations.next());
		}
		
		return relations;
	}
	
	/** Builds a query to fetch relation data with the given criteria.
	 * 
	 * @param selParams
	 * @param elementId1List
	 * @param elementId2List
	 * @param relationType
	 * @param relationName
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static String getRelationQueryText
	 (String selParams, boolean includeVerifiedSource, String orderByCol,
	 Vector<? extends Element> elementId1List, 
	 Vector<? extends Element> elementId2List, 
	 RelationType relationType, RelationName relationName, FWFacade facade) 
	  throws DataException {
		
		String baseQuery = "SELECT " + selParams + " FROM RELATIONS rel " +
		"INNER JOIN REF_RELATION_NAMES relname " +
		"ON (rel.RELATION_NAME_ID = relname.RELATION_NAME_ID) ";

		if (includeVerifiedSource)
			baseQuery += Relation.VERIFIED_SOURCE_APPEND_SQL;
		
		baseQuery += " WHERE ";
		
		// Ensure that the Element ID lists have at least 1 item
		// between them.
		if ((elementId1List == null || elementId1List.isEmpty())
			 && 
			(elementId2List == null || elementId2List.isEmpty())) {
			throw new DataException
			 ("Not enough parameters to fetch relation data.");
		}
		
		StringBuffer queryText = new StringBuffer();
		
		if (elementId1List != null && elementId1List.size() > 0) {
			queryText.append("rel.ELEMENT_ID1 IN (");
			boolean firstElem = true;
			
			for (Element element : elementId1List) {
				if (firstElem)
					firstElem = false;
				else
					queryText.append(",");
					
				queryText.append(element.getElementId());
			}
			
			queryText.append(") ");
		}
			
		if (elementId2List != null && elementId2List.size() > 0) {
			if (queryText.length() > 0) queryText.append(" AND ");
			
			queryText.append("rel.ELEMENT_ID2 IN (");
			boolean firstElem = true;
			
			for (Element element : elementId2List) {
				if (firstElem)
					firstElem = false;
				else
					queryText.append(",");
					
				queryText.append(element.getElementId());
			}
			
			queryText.append(") ");
		}
		
		if (relationType!=null) {
			if (queryText.length() > 0) queryText.append(" AND ");
			
			queryText.append("relname.RELATION_TYPE_ID=" + 
			 relationType.getRelationTypeId());
		}
		
		if (relationName != null) {
			if (queryText.length() > 0) queryText.append(" AND ");
			
			queryText.append("relname.RELATION_NAME_ID=" + 
			 relationName.getRelationNameId() + "");	
		}
		
		if (!StringUtils.stringIsBlank(orderByCol))
			queryText.append(" ORDER BY " + orderByCol);
		
		return baseQuery + queryText.toString();
	}
	
	/** Fetches a ResultSet of all unique entries from the Persons table
	 *  which are related to the given Element ID.
	 *  
	 * @param elementId
	 * @param elemType the Element Type of the passed Element ID
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getRelatedPersonsData
	 (int elementId, ElementType elemType, FWFacade facade) 
	 throws DataException {
		return getRelatedPersonsData(elementId, elemType, null, facade);
	}
	
	/** Fetches a ResultSet of all unique entries from the Persons table
	 *  which are related to the given Element ID.
	 *  
	 *  If the passed relationNameIds list is non-null, the returned relations will
	 *  be limited to those with a Relation Name ID in the list. If the list is null,
	 *  all relation names are returned.
	 *  
	 * @param elementId
	 * @param elemType the Element Type of the passed Element ID
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getRelatedPersonsData
	 (int elementId, ElementType elemType, Vector<Integer> relationNameIds,
	 FWFacade facade) throws DataException {

		// Determine the required relation type.
		RelationType relType = RelationType.getRelationType
		 (elemType, ElementType.PERSON);
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "elementId", elementId);
		
		if (relationNameIds != null) {
			binder.putLocal
			 ("relationNameIds", CCLAUtils.getCSVFromIntegerList(relationNameIds));
		}
		
		DataResultSet rsRelatedPersons = null;
		
		if (relType.getElement1Type().equals(ElementType.PERSON)) {
			if (relationNameIds != null) {
				rsRelatedPersons = facade.createResultSet
				 ("qClientServices_GetUniqueRelatedParentPersonsWithRelationNames", 
				 binder);
			} else {
				rsRelatedPersons = facade.createResultSet
				 ("qClientServices_GetUniqueRelatedParentPersons", binder);
			}
		} else {
			if (relationNameIds != null) {
				rsRelatedPersons = facade.createResultSet
				 ("qClientServices_GetUniqueRelatedChildPersonsWithRelationNames", 
				 binder);
			} else {
				rsRelatedPersons = facade.createResultSet
				 ("qClientServices_GetUniqueRelatedChildPersons", binder);
			}
		}
		
		// Ensure returned Identity Check results are up-to-date.
		AuthenticationScoreUtils.refreshIdentityCheckResults
		 (rsRelatedPersons, facade);
		
		return rsRelatedPersons;
	}
	
	/** Fetches a ResultSet of all unique entries from the Persons table
	 *  which are related to the given Element ID.
	 *  
	 *  If the passed relationNameIds list is non-null, the returned relations will
	 *  be limited to those with a Relation Name ID in the list. If the list is null,
	 *  all relation names are returned.
	 *  
	 * @param elementId
	 * @param elemType the Element Type of the passed Element ID
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static Vector<Person> getRelatedPersons(int elementId, ElementType elemType, 
	 Vector<Integer> relationNameIds, FWFacade facade) throws DataException {
		
		Vector<Person> persons = new Vector<Person>();
		DataResultSet rs = getRelatedPersonsData
		 (elementId, elemType, relationNameIds, facade);
		
		if (rs.first()) {
			do {
				persons.add(Person.get(rs));
			} while (rs.next());
		}
		
		return persons;
	}	
		
	/** Fetches a ResultSet of all unique entries from the Persons table
	 *  who have a relationship to the passed Organisation ID, or one of the 
	 *  Organisation's accounts.
	 *  
	 *  The ResultSet contains all columns from the Person table, Element table, plus 
	 *  their associated SIG_DOC_GUID value from the Person-Signature table, if present.
	 *  
	 * @param entityId
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getRelatedEntityAccountPersonsData
	 (int entityId, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, Entity.Cols.ID, entityId);

		DataResultSet rsRelatedPersons = facade.createResultSet
		 ("qClientServices_GetEntityAccountPersons", binder);
		
		return rsRelatedPersons;
	}

	/***
	 * Gets Result set containing person account relationship (both belonging) to organisation
	 * @param organisationId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getAllRelatedEntityAccountPersonsData
	 (int organisationId, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "organisationId", organisationId);
		
		return facade.createResultSet
		  ("qClientServices_GetAllRelatedEntityAccountPersons", binder);
		
	}
	
	
	/** Fetches a ResultSet of all unique entries from the Accounts table
	 *  for all accounts related to the given Element ID.
	 *  
	 *  
	 * @param elementId
	 * @param elemType the ElementType for the passed elementId
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getRelatedAccountsData
	 (int elementId, ElementType elemType, FWFacade facade) 
	 throws DataException {

		// Determine the required relation type.
		RelationType relType = RelationType.getRelationType
		 (elemType, ElementType.ACCOUNT);
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "elementId", elementId);
		
		DataResultSet rsRelatedAccounts = null;
		
		if (relType.getElement1Type().equals(ElementType.ACCOUNT))
			rsRelatedAccounts = facade.createResultSet
			 ("qClientServices_GetUniqueRelatedParentAccounts", binder);
		else
			rsRelatedAccounts = facade.createResultSet
			 ("qClientServices_GetUniqueRelatedChildAccounts", binder);
		
		return rsRelatedAccounts;
	}
	
	/** Fetches all Bank Accounts related to a given Organisation ID. The query must
	 *  link into the Relation table twice - once to fetch all related Accounts and then
	 *  again to fetch all unique related bank accounts.
	 *  
	 * @throws DataException 
	 * 
	 */
	public static Vector<BankAccount> getRelatedClientBankAccounts
	 (int orgId, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder(binder, Entity.Cols.ID, orgId);
		
		DataResultSet rsBankAccounts = facade.createResultSet
		 ("qClientServices_GetUniqueRelatedClientBankAccounts", binder);
		
		Vector<BankAccount> bankAccounts = new Vector<BankAccount>();
		
		if (rsBankAccounts.first()) {
			do {
				bankAccounts.add(BankAccount.get(rsBankAccounts));
			} while (rsBankAccounts.next());
		}
		
		return bankAccounts;
	}
	
	/** Fetches all unique Accounts which are linked to the given bank account,
     *  belonging to the given Organisation ID.
	 *  
	 * @param orgId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Vector<Account> getRelatedBankAccountAccounts
	 (int bankAccountId, int orgId, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder(binder, Entity.Cols.ID, orgId);
		CCLAUtils.addQueryIntParamToBinder
		 (binder, SharedCols.BANK_ACCOUNT, bankAccountId);
		
		DataResultSet rsAccounts = facade.createResultSet
		 ("qClientServices_GetUniqueRelatedBankAccountAccounts", binder);

		return Account.getAll(rsAccounts);
	}
	
	/** Fetches a ResultSet of all unique entries from the Bank Accounts 
	 *  table where the Bank Account ID appears in the ELEMENT_ID2 column, 
	 *  and the given parentElementId appears in the ELEMENT_ID1 column.
	 *  <br/>
	 *  This should be used when fetching Entity-Account and Person-Account 
	 *  relationships.
	 *  
	 * @param elementId
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getRelatedBankAccountsData
	 (int parentElementId, FWFacade facade) throws DataException {
		
		return getRelatedBankAccountsWithRelationData(parentElementId, null, facade);
	}
	
	/** Fetches a ResultSet of all unique entries from the Bank Accounts table where the 
	 *  Bank Account has a withdrawal and/or income relationship to the given Account 
	 *  ID.
	 *  <br/>
	 *  It features an extra column 'IS_NOMINATED_ACCOUNT'. This will be set to 1 for
	 *  the nominated withdrawal/income bank account, and null for all other bank
	 *  accounts. The relName parameter is used to specify whether you want the flag
	 *  to return the nominated withdrawal or nominated income bank account.
	 *  <br/>
	 *  The ResultSet is ordered so the nominated account is always listed first, if one
	 *  exists.
	 *  
	 * @param elementId
	 * @param facade
	 * @return
	 * @throws DataException 
	 * @throws DataException 
	 */
	public static DataResultSet getAccountBankAccountsData
	 (int accountId, RelationName nominatedRelName, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, SharedCols.ACCOUNT, accountId);
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "nominatedRelationNameId", nominatedRelName.getRelationNameId());
		
		return facade.createResultSet
		 ("qClientServices_GetAccountBankAccountsWithNominatedAccountFlag", binder);
	}
	
	/** Fetches a ResultSet of all unique entries from the Bank Accounts 
	 *  table where the Bank Account ID appears in the ELEMENT_ID2 column, 
	 *  and the given parentElementId appears in the ELEMENT_ID1 column.
	 *  <br/>
	 *  If RelationName is specified, only Bank Accounts with the given relationship
	 *  to the parent element are fetched. This should be used if you need a list of
	 *  all 'withdrawal' bank accounts for a particular account, for example.
	 *  
	 *  The query used here (qClientServices_GetUniqueRelatedBankAccountsWithRelation)
	 *  is rather special, as it will sort the list of unique bank accounts with the
	 *  default/nominated account always appearing first.
	 *  <br/>
	 *  This should be used when fetching Entity-Account and Person-Account 
	 *  relationships.
	 *  
	 * @param elementId
	 * @param relName
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getRelatedBankAccountsWithRelationData
	 (int parentElementId, RelationName relName, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "parentElementId", parentElementId);
		
		DataResultSet rsRelatedAccounts = null;
		
		if (relName != null) {
			CCLAUtils.addQueryIntParamToBinder
			 (binder, "RELATION_NAME_ID", relName.getRelationNameId());
			
			rsRelatedAccounts = facade.createResultSet
			 ("qClientServices_GetUniqueRelatedBankAccountsWithRelation", binder);
		} else {
			rsRelatedAccounts = facade.createResultSet
			 ("qClientServices_GetUniqueRelatedBankAccounts", binder);
		}
		
		return rsRelatedAccounts;
	}
	
	/** Fetches a zero or single-row ResultSet, containing data for the nominated
	 *  Bank Account for the given Account ID. 
	 *  
	 *  The relName parameter determines whether the nominated withdrawal or income
	 *  bank account will be fetched.
	 *  
	 *  A 'nominated bank account' must have a relation to the Account, which has the
	 *  singleton 'Default' property associated with it.
	 *  
	 *  If there is no nominated account for the passed relation, an empty ResultSet is
	 *  returned.
	 *  
	 * @param orgId
	 * @param relName
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getNominatedBankAccountData
	 (int accountId, RelationName relName, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, SharedCols.ACCOUNT, accountId);
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, SharedCols.RELATION_NAME, relName.getRelationNameId());
		
		DataResultSet rsNominatedBankAccount =
		 facade.createResultSet
		  ("qClientServices_GetNominatedBankAccount", binder);
		
		return rsNominatedBankAccount;
	}

	/** Fetches a ResultSet of all unique entries from the Organisation table
	 *  where the Entity ID appears in the ELEMENT_ID1 column, and the
	 *  given childElementId appears in the ELEMENT_ID2 column.
	 *  <br/>
	 *  This should be used when fetching Entity-Person and Entity-Account 
	 *  relationships.
	 *  
	 * @param elementId
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getRelatedOrgsData
	 (int childElementId, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "childElementId", childElementId);
		
		DataResultSet rsRelatedEntities =
		 facade.createResultSet
		  ("qClientServices_GetUniqueRelatedEntities", binder);
		
		return rsRelatedEntities;
	}
	
	/** Fetches a ResultSet of all unique entries from the Organisation table who
	 *  have parent/child relationships to the passed element ID.
	 *  
	 *  Either elementId1 must be specified, or elementId2, but not both. This is used
	 *  to determine which relation element ID field will form the base of the check
	 *  (ELEMENT_ID1 or ELEMENT_ID2 fields)
	 *  
	 * @param elementId1
	 * @param elementId2
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getRelatedOrgsData
	 (Integer elementId1, Integer elementId2, FWFacade facade) throws DataException {
		
		return getRelatedOrgsData(elementId1, elementId2, null, facade);
	}
	
	/** Fetches a ResultSet of all unique entries from the Organisation table who
	 *  have parent/child relationships to the passed element ID.
	 *  
	 *  Either elementId1 must be specified, or elementId2, but not both. This is used
	 *  to determine which relation element ID field will form the base of the check
	 *  (ELEMENT_ID1 or ELEMENT_ID2 fields)
	 *  
	 *  If the passed relationNameIds list is non-null, the returned relations will
	 *  be limited to those with a Relation Name ID in the list. If the list is null,
	 *  all relation names are returned.
	 *  
	 * @param elementId1
	 * @param elementId2
	 * @param relationNameIds
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getRelatedOrgsData
	 (Integer elementId1, Integer elementId2, 
	 Vector<Integer> relationNameIds, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();

		if (elementId1 != null && elementId2 != null) {
			throw new DataException("Unable to fetch related Organisations - base " +
			 "Element ID is ambiguous");
		}
		
		Integer elementId = (elementId1 != null ? elementId1 : elementId2);
		
		if (elementId == null)
			throw new DataException("Unable to fetch related Organisations - no base " +
			 "Element ID specified");
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "elementId", elementId);
		
		if (relationNameIds != null) {
			binder.putLocal
			 ("relationNameIds", CCLAUtils.getCSVFromIntegerList(relationNameIds));
		}
		
		DataResultSet rsRelatedOrgs = null;
		
		if (elementId1 == null)
			if (relationNameIds != null) {
				rsRelatedOrgs = facade.createResultSet
				 ("qClientServices_GetUniqueRelatedParentOrgsWithRelationNames", binder);
			} else {
				rsRelatedOrgs = facade.createResultSet
				 ("qClientServices_GetUniqueRelatedParentOrgs", binder);
			}
		else
			if (relationNameIds != null) {
				rsRelatedOrgs = facade.createResultSet
				 ("qClientServices_GetUniqueRelatedChildOrgsWithRelationNames", binder);
			} else {
				rsRelatedOrgs = facade.createResultSet
				 ("qClientServices_GetUniqueRelatedChildOrgs", binder);
			}
		
		return rsRelatedOrgs;
	}
	
	/** Fetches a list of all unique entries from the Organisation table who
	 *  have parent/child relationships to the passed element ID.
	 *  
	 *  Either elementId1 must be specified, or elementId2, but not both. This is used
	 *  to determine which relation element ID field will form the base of the check
	 *  (ELEMENT_ID1 or ELEMENT_ID2 fields)
	 *  
	 *  If the passed relationNameIds list is non-null, the returned relations will
	 *  be limited to those with a Relation Name ID in the list. If the list is null,
	 *  all relation names are returned.
	 *  
	 * @param elementId1
	 * @param elementId2
	 * @param relationNameIds
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static Vector<Entity> getRelatedOrgs
	 (Integer elementId1, Integer elementId2, 
	 Vector<Integer> relationNameIds, FWFacade facade) throws DataException {
		
		Vector<Entity> orgs = new Vector<Entity>();
		
		DataResultSet rsOrgs = getRelatedOrgsData(elementId1, elementId2, 
		 relationNameIds, facade);
		
		if (rsOrgs.first()) {
			do {
				orgs.add(Entity.get(rsOrgs));
			} while (rsOrgs.next());
		}
		
		return orgs;
	}
	
	/** Fetches a ResultSet of the Element Relations table joined
	 *  to the Persons table, for the given Person ID.
	 *  
	 *  This will pick up Person-Person relationships only - the
	 *  1st and 2nd Element ID relation columns are checked for the
	 *  given Person ID.
	 **/
	public static DataResultSet getMutualPersonRelations
	 (int personId, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "personId", personId);
		
		DataResultSet rsRelatedPersons =
		 facade.createResultSet
		 ("qClientServices_GetMutualPersonRelations", binder);
		
		return rsRelatedPersons;
	}
	
	/** Fetches a ResultSet of the Element Relations table joined
	 *  to the Entity/Clients tables, for the given Entity ID.
	 *  
	 *  This will pick up Entity-Entity relationships only - the
	 *  1st and 2nd Element ID relation columns are checked for the
	 *  given Entity ID.
	 *  
	 **/
	public static DataResultSet getMutualEntityRelations
	 (int entityId, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "entityId", entityId);
		
		DataResultSet rsRelatedEntities =
		 facade.createResultSet
		 ("qClientServices_GetMutualEntityRelations", binder);
		
		return rsRelatedEntities;
	}
	
	/** Checks the passed list of relations for the given checkRelation.
	 *  It is assumed that the passed list of relations will correspond
	 *  to existing (persisted) relations with valid Relation ID values,
	 *  whereas the checkRelation instance will be created on-to-fly
	 *  purely to run the check.
	 *  
	 *  If it exists, the ID of the matched relation from the list is
	 *  returned.
	 *  
	 *  If no matching relation is found, returns null.
	 *  
	 * @param relations
	 * @param checkRelation
	 * @return
	 */
	public static Integer getExistingRelation
	 (Vector<Relation> relations, Relation checkRelation) {
		
		for (Relation thisRelation: relations) {
			if (thisRelation.equals(checkRelation)) {
				return thisRelation.getRelationId();
			}
		}
		
		return null;
	}
	
	/** Returns true if the given Relation has matching Element IDs
	 *  and Element Name.
	 *  
	 * @param relation
	 */
	public boolean equals(Relation relation) {
		
		return (this.getElementId1() == relation.getElementId1())
				&&
			   (this.getElementId2() == relation.getElementId2())
				&&
			   (this.getRelationName().equals(relation.getRelationName()));
	}

	public void setRelationName(RelationName relationName) {
		this.relationName = relationName;
	}

	public RelationName getRelationName() {
		return relationName;
	}
	
	/** Adds or removes a set of similar Relation Names between a single
	 *  'related element' and a set of 'common elements'.
	 *  
	 *  E.g. if the relatedElement corresponded to Person X, the
	 *  commonElementIds list corresponded to a list of accounts, the
	 *  RelationName was 'Trustee', and the UpdateType was 'ADD', then
	 *  a Trustee Relation will be created between Person X and all the
	 *  listed accounts.
	 *  
	 *  The method will account for existing relationships, so a new relation
	 *  will not be created between 2 elements if it already existed.
	 *  
	 * @param commonElementIds
	 * @param relatedElement
	 * @param relName
	 * @param updateType
	 * @param facade
	 * @param userName
	 * @throws DataException
	 */
	public static void addOrRemoveAll(Vector<Integer> commonElementIds, 
	 Element relatedElement, RelationName relName, 
	 Relation.UpdateType updateType,
	 FWFacade facade, String userName) throws DataException {
		
		RelationType relType = relName.getRelationType();
		
		Log.debug("Adding/removing " + relType.getRelationLabel() + 
		 " relation '" + relName.getRelation() + "', related element ID=" +
		 relatedElement.getElementId() + ", no. of common elements=" +
		 commonElementIds.size());
		
		// Determine whether the related Element type is the 'parent'
		// type for the given relation, i.e. does it appear in the
		// ELEMENT_ID1 column.
		boolean relatedElementTypeIsParent = false;
		
		if (relType.getElement1Type().equals(relatedElement.getType()))
			relatedElementTypeIsParent = true;
		else if (!relType.getElement2Type().equals(relatedElement.getType())) {
			
			// Fail if the related element type isn't compatible with the passed
			// Relation Name.
			throw new DataException("Unable to find related element type " + 
			 relatedElement.getType() + " in the Relation Type: " + 
			 relType.getRelationLabel());
		}
		
		// Fetch all existing relations for the given related element ID and
		// relation name.
		Vector<Relation> existingRelations = null;
		
		if (relatedElementTypeIsParent) {
			existingRelations = Relation.getRelations
			 (relatedElement.getElementId(), null, null, relName, facade);
		} else {
			existingRelations = Relation.getRelations
			 (null, relatedElement.getElementId(), null, relName, facade);;
		}
		
		// Step through the full list of common element IDs and add/remove
		// relations as required.
		for (Integer commonElementId : commonElementIds) {
			
			// Build the Relation instance to be checked against.
			Relation checkRelation = null;
			
			if (relatedElementTypeIsParent) {
				checkRelation = new Relation(0, relName, 
				 relatedElement.getElementId(), commonElementId, null, null, userName);
			} else {
				checkRelation = new Relation(0, relName, 
				 commonElementId, relatedElement.getElementId(), null, null, userName);
			}
			
			// Determine whether the relation already exists between the
			// two elements.
			Integer existingRelationId = 
			 Relation.getExistingRelation(existingRelations, checkRelation);
			
			if (updateType.equals(UpdateType.ADD) 
				&& 
				(existingRelationId == null)) {

				// Add a new relation.
				int elemId1, elemId2;
				
				if (relatedElementTypeIsParent) {
					elemId1 = relatedElement.getElementId();
					elemId2 = commonElementId;
				} else {
					elemId2 = relatedElement.getElementId();
					elemId1 = commonElementId;
				}
				
				Relation.add(elemId1, elemId2, relName,
				 facade, userName);
				
			} else if (updateType.equals(UpdateType.REMOVE) 
				&& 
				(existingRelationId != null)) {
				
				// Remove the exisitng relation.
				
				Relation.remove
				 (existingRelationId, facade, userName);
			}
		}
	}
	
	public static void addOrRemoveSyncRelation(int elementId1, int elementId2,
			 RelationName relationName, String addOrRemove,
			 FWFacade facade, String username) throws DataException {

		// get relation name id and see if it's in the sync table
		int relationNameId = relationName.getRelationNameId();
		RelationNameSync thisSync = RelationNameSync.getID1Cache().getCachedInstance
		 (relationNameId);
		
		if (thisSync!=null && thisSync.getAction().equalsIgnoreCase(addOrRemove)) {
			Log.debug(addOrRemove + " RELATION NAME SYNC FOUND FOR " + relationNameId);
			RelationType relType = relationName.getRelationType();

			if (relType.getRelationTypeId() == RelationType.PERSON_ACCOUNT)
			{
				Log.debug("RELATION TYPE IS PERSON_ACCOUNT");
				// elementId1 is person, elementId2 is account
				// get the relation to add/remove
				int newRelationNameId = thisSync.getRelationNameId2();
				RelationName newRelName = 
				 RelationName.getCache().getCachedInstance(newRelationNameId);
				RelationType newRelType = newRelName.getRelationType();
				if (newRelType.getRelationTypeId() == RelationType.ORG_PERSON)
				{
					// Need to find orgs for account
					Log.debug("Looking for orgs for account:" + elementId2);
					Account account = Account.get(elementId2, facade);
					int ownerId = account.getOwnerOrganisationId(facade);
					// this is the org id to create the person_org relation with
					Log.debug("creating org_person relationship");
					boolean relExists = relationExists(ownerId, elementId1, newRelName, facade);
					if (thisSync.getSyncType().equalsIgnoreCase(RelationNameSync.COPY_RELATION) && !relExists)
						addSingle(ownerId, elementId1, newRelName, facade, username);
					if (thisSync.getSyncType().equalsIgnoreCase(RelationNameSync.DELETE_RELATION) && relExists)
					{
						Relation deleteRel = get(Relation.getRelationData(ownerId, elementId2, null, newRelName, facade));
						removeSingle(deleteRel.getRelationId(), facade, username);
					}
											
				}
				
			} else if (relType.getRelationTypeId() == RelationType.ORG_PERSON)
			{
				Log.debug("RELATION TYPE IS ORG_PERSON");	
				// elementId1 is org, elementId2 is person
				// get the relation to add/remove
				int newRelationNameId = thisSync.getRelationNameId2();
				RelationName newRelName = 
				 RelationName.getCache().getCachedInstance(newRelationNameId);
				
				RelationType newRelType = newRelName.getRelationType();
				if (newRelType.getRelationTypeId() == RelationType.PERSON_ACCOUNT)
				{
					// NEED TO GET ACCOUNTS FOR ORGANISATION
					DataResultSet rsAccounts = getRelatedAccountsData(elementId1,ElementType.ORGANISATION, facade);
					if (!rsAccounts.isEmpty())
					{
						do {
							Account account = Account.get(CCLAUtils.getResultSetIntegerValue(rsAccounts, "ACCOUNT_ID"), facade);
							int accountId = account.getAccountId();
							boolean relExists = relationExists(elementId2, accountId, newRelName, facade);
							if (thisSync.getSyncType().equalsIgnoreCase(RelationNameSync.COPY_RELATION) && !relExists)
							{
								addSingle(elementId2, accountId, newRelName, facade, username);
							} else if (thisSync.getSyncType().equalsIgnoreCase(RelationNameSync.DELETE_RELATION) && relExists)
							{
								Relation deleteRel = get(Relation.getRelationData(elementId2, accountId, null, newRelName, facade));
								removeSingle(deleteRel.getRelationId(), facade, username);
							}
							
						} while (rsAccounts.next());	
					}
				}


			}
		}
	}
	
	/** See other getRelationMap method for explanation.
	 * 
	 * @param elementId1List
	 * @param elementId2List
	 * @param relationType
	 * @param relationName
	 * @param facades
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getRelationMap
	 (ElementType relElementType, 
	 Integer elementId1, Integer elementId2, 
	 RelationType relationType, RelationName relationName, FWFacade facade) 
	 throws DataException {
		
		Vector<Element> elementId1List = new Vector<Element>();
		Vector<Element> elementId2List = new Vector<Element>();
		
		if (elementId1 != null)
			elementId1List.add(new Element(elementId1, null));
		
		if (elementId2 != null)
			elementId2List.add(new Element(elementId2, null));
		
		return getRelationMap(relElementType, elementId1List, 
		 elementId2List, relationType, relationName, facade);
	}
	
	/** Fetches a 3-column ResultSet of transformed Relation data. This is used when
	 *  displaying the 2D relationship grids in the UI.
	 *  
	 *  The 3 columns are:
	 *  
	 *  REL_MAP: <element ID>_<relation name ID>
	 *  REL_ID: <relation ID>
	 *  NUM_VER_SOURCES: <no. of verified relation sources>
	 *  
	 *  The REL_MAP column is designed for fast look-up using the IDOC function
	 *  rsFindRowPrimary. The Element ID in the first part of the string will 
	 *  correspond to either the ELEMENT_ID1 column or the ELEMENT_ID2 one, this 
	 *  depends on the passed relElementType parameter.
	 *  
	 *  The other columns are used when rendering the relation 'tick' on the screen.
	 *  
	 * @param elementId1List
	 * @param elementId2List
	 * @param relationType
	 * @param relationName
	 * @param facades
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getRelationMap
	 (ElementType relElementType, 
	 Vector<? extends Element> elementId1List, Vector<? extends Element> elementId2List, 
	 RelationType relationType, RelationName relationName, FWFacade facade) 
	 throws DataException {
		
		if (relElementType == null || relationType == null)
			throw new DataException("Related Element Type and Relation Type must " +
			 "be specified to create Relation Map");
		
		// Determine which Element ID column to use in the select clause.
		String elementIdColumn = null; 
		
		if (relationType.getElement1Type().equals(relElementType))
			elementIdColumn = "ELEMENT_ID1";
		else
			elementIdColumn = "ELEMENT_ID2";
				
		String selectClause =
		 "rel." + elementIdColumn + " || '_' || rel.RELATION_NAME_ID AS REL_MAP, " +
		 "rel.RELATION_ID AS REL_ID, " +
		 "verRelProps.NUM_VERIFIED_SOURCES AS NUM_VER_SOURCES, " +
		 "unverRelProps.NUM_UNVERIFIED_SOURCES AS NUM_UNVER_SOURCES ";
		
		String query = getRelationQueryText(selectClause, true, null,
		 elementId1List, elementId2List, relationType, relationName, facade);
		
		//Log.debug("Getting relationship with query: " + query);
		
		return facade.createResultSetSQL(query);
	}
	
	/** Returns a list of Relation Names mapped against lists of Person who have that
	 *  relationship across all the passed accounts.

	 *  Returns joined rows from REF_RELATION_NAMES and PERSON tables.
	 *  
	 * @param accounts
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static HashMap<RelationName, Vector<Person>> 
	 getCommonPersonAccountRelations(Vector<Account> accounts, FWFacade facade) 
	 throws DataException {
		
		HashMap<RelationName, Vector<Person>> commonRels = 
		 new HashMap<RelationName, Vector<Person>>();
		
		Log.debug("Fetching common person relations across " 
		 + accounts.size() + " accounts");
		
		DataResultSet rs = getCommonPersonAccountRelationsData(accounts, facade);
		
		// Used to cache Person instances as we come across them.
		HashMap<Integer, Person> persons = new HashMap<Integer, Person>();
		
		if (rs.first()) {
			do {
				Integer relNameId = CCLAUtils.getResultSetIntegerValue
				 (rs, RelationName.Cols.ID);
				
				Integer personId = CCLAUtils.getResultSetIntegerValue
				 (rs, Person.Cols.ID);
				
				RelationName relName = RelationName.getCache().getCachedInstance
				 (relNameId);
				
				Person person = null;
				
				if (persons.containsKey(personId)) {
					person = persons.get(personId);
				} else {
					person = Person.get(personId, true, facade);
					persons.put(personId, person);
				}
				
				Vector<Person> relPersons = null;
				
				if (commonRels.containsKey(relName)) {
					relPersons = commonRels.get(relName);
				} else {
					relPersons = new Vector<Person>();
					commonRels.put(relName, relPersons);
				}
				
				relPersons.add(person);
				
			} while (rs.next());
		}
		
		Log.debug("Found " + commonRels.size() + " common relations");
		
		for (RelationName relName : commonRels.keySet()) {
			Log.debug("Relation " + relName.getRelation() + " has " 
			 + commonRels.get(relName).size() + " common persons");
		}
		
		return commonRels;
	}
	
	/** Returns a list of Persons and their common relations across the passed set of
	 *  accounts.
	 *  
	 *  So if Person X has the Signatory relation across all passed accounts, a row
	 *  will be present in the results with his Person record and the Signatory Relation
	 *  Name record.
	 *  
	 *  Returns joined rows from REF_RELATION_NAMES and PERSON tables.
	 *  
	 * @param accounts
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getCommonPersonAccountRelationsData
	 (Vector<Account> accounts, FWFacade facade) throws DataException {
		DataBinder binder = new DataBinder();
		
		Vector<Integer> accountIds = new Vector<Integer>();
		
		for (Account acc : accounts) {
			accountIds.add(acc.getAccountId());
		}
		
		String accountIdsStr = CCLAUtils.getCSVFromIntegerList(accountIds);
		binder.putLocal("accountIds",accountIdsStr);
		
		return
		 facade.createResultSet("qCore_GetCommonPersonAccountRelations", binder);
	}
	
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	
	/** 
	 * Returns a mapping of Relation IDs -> Applied Relation Properties for the given 
	 * set of Relations.
	 * 
	 * @param relations
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static HashMap<Integer, Vector<RelationPropertyApplied>> 
	 getRelationProperties
	 (Vector<Relation> relations, FWFacade facade) throws DataException {
	
		HashMap<Integer, Vector<RelationPropertyApplied>> relPropMap =
		 new HashMap<Integer, Vector<RelationPropertyApplied>>();
		
		// Build comma-separated list of Relation IDs
		StringBuffer relationIds = new StringBuffer();
		
		for (Relation rel : relations) {
			if (relationIds.length() > 0)
				relationIds.append(",");
			
			relationIds.append(rel.getRelationId());
		}
		
		DataBinder binder = new DataBinder();
		binder.putLocal("RELATION_IDS", relationIds.toString());
		
		DataResultSet rsRelationProperties = facade.createResultSet
		 ("qCore_GetRelationPropertiesAppliedByRelationIds", binder);
		
		Log.info("Found "+ rsRelationProperties.getNumRows() + " relation properties");
		
		if (!rsRelationProperties.isEmpty()) {
			do {
				Integer relId = CCLAUtils.getResultSetIntegerValue
				 (rsRelationProperties, SharedCols.RELATION);
				
				RelationPropertyApplied relProp = 
				 RelationPropertyApplied.get(rsRelationProperties);
				
				if (relPropMap.containsKey(relId)) {
					Vector<RelationPropertyApplied> relProps = relPropMap.get(relId);
					relProps.add(relProp);
				} else {
					Vector<RelationPropertyApplied> relProps = 
					 new Vector<RelationPropertyApplied>();
					relProps.add(relProp);
					relPropMap.put(relId, relProps);
				}
				
			} while (rsRelationProperties.next());
		}
		Log.info("relPropMap size: "+relPropMap.size());
		return relPropMap;
	}
}
