package com.ecs.ucm.ccla.data;

import java.util.HashMap;
import java.util.Vector;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.CacheManager;
import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Represents entries from the REF_RELATION_TYPES table in the CCLA 
 *  tablespace. These are all cached at startup - see the CacheManager class
 *  
 * @author Tom
 *
 */
public class RelationType implements Persistable {

	/* Element relation type IDs. Expected to match IDs used in 
	 * REF_RELATION_TYPES table. */
	
	public static final int ORG_PERSON = 1;
	public static final int ORG_ACCOUNT = 2;
	public static final int PERSON_ACCOUNT = 20;
	public static final int ACCOUNT_BANKACCOUNT = 40;
	// Mutual relations
	public static final int ORG_ORG = 4;
	public static final int PERSON_PERSON = 22;
	public static final int ACCOUNT_ACCOUNT = 41;
	
	private int relationTypeId;
	private String relationLabel;
	private ElementType element1Type;
	private ElementType element2Type;
	
	public static class Cols {
		public static final String ID = "RELATION_TYPE_ID";
		public static final String LABEL = "RELATION_LABEL";
	}
	
	public RelationType(int elementRelationTypeId,  String relationLabel,
	 ElementType element1Type, ElementType element2Type) {
		
		this.relationTypeId = elementRelationTypeId;
		this.relationLabel = relationLabel;
		this.setElement1Type(element1Type);
		this.setElement2Type(element2Type);
	}	
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "RELATION_TYPE_ID", this.getRelationTypeId());	
		CCLAUtils.addQueryParamToBinder
		 (binder, "RELATION_LABEL", this.getRelationLabel());	
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "ELEMENT_TYPE_ID_1", this.getElement1Type().getElementTypeId());	
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "ELEMENT_TYPE_ID_2", this.getElement2Type().getElementTypeId());		
		
	}
	public void persist(FWFacade facade, String username) throws DataException {
		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);		
		this.validate(facade);
		
		facade.execute("qClientServices_UpdateRelationType", binder);
	}
	
	public void setAttributes(DataBinder binder) throws DataException {

		this.setRelationTypeId(CCLAUtils.getBinderIntegerValue
		 (binder, "RELATION_TYPE_ID"));				
		this.setRelationLabel(binder.getLocal("RELATION_LABEL"));
		
		this.setElement1Type(
		 ElementType.getCache().getCachedInstance(
		  CCLAUtils.getBinderIntegerValue
		  (binder, "ELEMENT_TYPE_ID_1")
		 ));
		
		this.setElement1Type(
		 ElementType.getCache().getCachedInstance(
		  CCLAUtils.getBinderIntegerValue
		  (binder, "ELEMENT_TYPE_ID_2")
		 ));	
		
	}
	public void validate(FWFacade facade) throws DataException {
		if (StringUtils.stringIsBlank(this.getRelationLabel()))
			throw new DataException("Element Label is missing");
		
	}
	
	public static RelationType get(DataResultSet rsRelationType) throws DataException {
	
		return new RelationType(
			CCLAUtils.getResultSetIntegerValue
			 (rsRelationType, "RELATION_TYPE_ID"),
			rsRelationType.getStringValueByName("RELATION_LABEL"),
			
			ElementType.getCache().getCachedInstance(
			 CCLAUtils.getResultSetIntegerValue
			 (rsRelationType, "ELEMENT_TYPE_ID_1")
			),
			ElementType.getCache().getCachedInstance(
			 CCLAUtils.getResultSetIntegerValue
			 (rsRelationType, "ELEMENT_TYPE_ID_2")
			)
		);
	}
	
	/** Fetches a single entry from the REF_RELATION_TYPES table
	 *  
	 * @param RelationTypeId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getData(int RelationTypeId, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "RELATION_TYPE_ID", RelationTypeId);
		
		DataResultSet rsRelationType = 
		 facade.createResultSet("qClientServices_GetRelationType", binder);
		
		return rsRelationType;
	}
	
	public static Vector<RelationType> getAll(FWFacade facade) throws DataException {
		
		// Fetch entire CCLA_FUNDS table from db
		DataResultSet rsTypes = facade.createResultSet
		("qClientServices_GetRelationTypes", new DataBinder());
		
		Vector<RelationType> relTypes = new Vector<RelationType>();
		
		if (rsTypes.first()) {
			do {
				relTypes.add(get(rsTypes));
			} while (rsTypes.next());
		}
		
		return relTypes;
	}
	
	public int getRelationTypeId() {
		return relationTypeId;
	}
	public void setRelationTypeId(int relationTypeId) {
		this.relationTypeId = relationTypeId;
	}
	public String getRelationLabel() {
		return relationLabel;
	}
	public void setRelationLabel(String elementLabel) {
		this.relationLabel = elementLabel;
	}

	public void setElement1Type(ElementType element1Type) {
		this.element1Type = element1Type;
	}

	public ElementType getElement1Type() {
		return element1Type;
	}

	public void setElement2Type(ElementType element2Type) {
		this.element2Type = element2Type;
	}

	public ElementType getElement2Type() {
		return element2Type;
	}
	
	public boolean equals(RelationType relationType) {
		return (this.getRelationTypeId() == relationType.getRelationTypeId());
	}
	
	public String toString() {
		return this.getRelationLabel();
	}

	public static RelationType getRelationTypeFromBinder(DataBinder binder) 
	 throws DataException {
		
		String elemType1Str			= binder.getLocal("ELEMENT_TYPE_ID_1");
		String elemType2Str			= binder.getLocal("ELEMENT_TYPE_ID_2");
		
		if (StringUtils.stringIsBlank(elemType1Str)
			||
			StringUtils.stringIsBlank(elemType2Str)) {
			
			throw new DataException
			 ("Unable to resolve relation type, " +
			 "ELEMENT_TYPE_ID_1/ELEMENT_TYPE_ID_2 missing");
		}
		
		int elemType1Id 			= Integer.parseInt(elemType1Str);
		int elemType2Id				= Integer.parseInt(elemType2Str);
		
		ElementType elemType1		= 
		 ElementType.getCache().getCachedInstance(elemType1Id);
		ElementType elemType2		= 
		 ElementType.getCache().getCachedInstance(elemType2Id);
		
		// Determine relation type from passed ELEMENT_TYPE_ID_1, 
		// ELEMENT_TYPE_ID_2
		RelationType relationType = RelationType.getRelationType(elemType1,elemType2);
		
		return relationType;
	}
	
	/** Fetches a set of cached RelationNames with the given Relation Type.
	 * 
	 * @param relationTypeId
	 * @return
	 */
	public static Vector<RelationName> getRelationNames(int relationTypeId) {
		
		Vector<RelationName> relNames = new Vector<RelationName>();
		
		for (RelationName relName : RelationName.getCache().getCache().values()) {
			// Search for matching relation type IDs
			if (relName.getRelationType().getRelationTypeId() 
				== 
				relationTypeId) {
				relNames.add(relName);
			}
		}
		
		return relNames;
	}
	
	/*
	 *  Caching stuff
	 */
	private static Cache CACHE = new Cache();
	
	public static Cachable<Integer, RelationType> getCache() {
		return CACHE;
	}
	
	/** Returns the corresponding Relation Type for the two given
	 *  element types.
	 *  
	 *  This doesn't care about the ordering of the element types.
	 *  
	 * @param elementType1
	 * @param elementType2
	 * @return
	 * @throws DataException 
	 */
	public static RelationType getRelationType
	 (ElementType elementType1, ElementType elementType2) throws DataException {
		
		if (!CACHE.isInitialized())
			throw new DataException(CACHE.getCacheName() + " cache not initialized");
		
		for (RelationType relationType : CACHE.getCache().values()) {
			if ((relationType.getElement1Type().equals(elementType1)
				&&
				relationType.getElement2Type().equals(elementType2))
				||
				(relationType.getElement1Type().equals(elementType2)
				&&
				relationType.getElement2Type().equals(elementType1)))
				return relationType;
		}
		
		Log.warn("No matching Relation Type for " + 
		 elementType1.toString() + ", " + elementType2.toString());
		
		return null;
	}
	
	/** RelationType cache implementor */
	private static class Cache extends Cachable<Integer, RelationType> {

		public Cache() {
			super("Relation Type");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<RelationType> relTypes = RelationType.getAll(facade);
			
			HashMap<Integer, RelationType> newCache = 
			 new HashMap<Integer, RelationType>();
			
			for (RelationType relType : relTypes) {
				newCache.put(relType.getRelationTypeId(), relType);
			}
			
			this.CACHE_MAP = newCache;
		}
	}
}

