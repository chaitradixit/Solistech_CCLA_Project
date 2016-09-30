package com.ecs.ucm.ccla.data;

import java.util.HashMap;
import java.util.Vector;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.shared.SharedObjects;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Represents a single entry from the REF_RELATION_NAMES table.
 *  
 *  All instances are cached on server startup - see CacheManager class
 *  
 * @author Tom
 *
 */
public class RelationName implements Persistable {

	/** Available relationships between Persons and Accounts.
	 *  
	 *  Should be kept in sync with the REF_RELATION_NAMES
	 *  table.
	 *  
	 * @author Tom
	 *
	 */
	public static class PersonAccountRelation {
		public static final int DEFAULT_RELATION = 
		 SharedObjects.getEnvironmentInt
		("CCLA_CS_DefaultPersonAccountRelationNameId", 0);
		
		public static final int CORRESPONDENT 	= 80;
		public static final int SIGNATORY 	= 84;
		public static final int ORD_TRUSTEE 	= 82;
		public static final int AUTH_PERSON 	= 81; /* Formerly AUTH_TRUSTEE */
	}

	/** Available relationships between Organisations and 
	 *  Accounts.
	 *  
	 *  Should be kept in sync with the REF_RELATION_NAMES
	 *  table.
	 *  
	 * @author Tom
	 *
	 */
	public static class OrganisationAccountRelation {
		public static final int OWNER 			= 20;
		public static final int BENIFICIARY 	= 21;
	}
	
	/** Available relationships between Organisations and
	 *  Persons.
	 *  
	 * @author Tom
	 *
	 */
	public static class OrganisationPersonRelation {
		public static final int DEFAULT_RELATION = 
		 SharedObjects.getEnvironmentInt
		 ("CCLA_CS_DefaultOrgPersonRelationNameId", 0);
		
		public static final int CORRESPONDENT 		= 1;
		public static final int ORD_SIGNATORY 		= 4;
		public static final int SIGNATORY 			= 5;
		public static final int AUTH_PERSON 		= 2;
		public static final int TRUSTEE 			= 3;
		public static final int ASSOCIATE			= 7;
		public static final int CRM					= 8;
		public static final int LISTED_ON_MANDATE 	= 9;
		public static final int PREV_CORRESPONDENT 	= 10;
		public static final int SECTION_151_OFFICER	= 11;
		public static final int EMPLOYEE			= 12;
		public static final int DONOR				= 13;
	}
	
	/** Available relationships between Accounts and Bank Accounts.
	 * 
	 * @author Tom
	 *
	 */
	public static class AccountBankAccountRelation {
		public static final int WITHDRAWAL		= 120;
		public static final int INCOME			= 121;
	}
	
	/** Available relationships between 2 Organisations.
	 * 
	 * @author Tom
	 *
	 */
	public static class OrganisationOrganisationRelation {
		public static final int DONOR 			= 140;
	}
	
	public static class Cols {
		public static final String ID 				= "RELATION_NAME_ID";
		public static final String RELATION 		= "RELATION";
		public static final String SHORT_NAME 		= "SHORT_NAME";
		public static final String IS_SINGLETON 	= "IS_SINGLETON";
	}
	
	private int relationNameId;
	private RelationType relationType;
	
	private String relation;
	private String shortName;
	private boolean isSingleton;
	
	public RelationName(int relationNameId, RelationType relationType, 
	 String relation, String shortName, boolean isSingleton) {
		
		this.relationNameId = relationNameId;
		this.relationType = relationType;
		this.relation = relation;
		this.shortName = shortName;
		this.isSingleton = isSingleton;
	}
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "RELATION_NAME_ID", this.getRelationNameId());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "RELATION_TYPE_ID", 
		 this.getRelationType().getRelationTypeId());	
		CCLAUtils.addQueryParamToBinder
		 (binder, "RELATION", this.getRelation());	
		CCLAUtils.addQueryParamToBinder
		 (binder, "SHORT_NAME", this.getShortName());	
		CCLAUtils.addQueryBooleanParamToBinder
		 (binder, "IS_SINGLETON", this.isSingleton());			
	}

	public void persist(FWFacade facade, String username) throws DataException {
		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);
		
		this.validate(facade);
		
		facade.execute("qClientServices_UpdateRelationName", binder);
		
	}

	public void setAttributes(DataBinder binder) throws DataException {
		this.setRelationNameId(CCLAUtils.getBinderIntegerValue
		 (binder, "RELATION_NAME_ID"));
		
		Integer relationTypeId = CCLAUtils.getBinderIntegerValue
		 (binder, "RELATION_TYPE_ID");
		
		if (relationTypeId != null) {
			this.setRelationType(
			 RelationType.getCache().getCachedInstance(relationTypeId));
		}
		
		this.setRelation(binder.getLocal("RELATION"));
		this.setShortName(binder.getLocal("SHORT_NAME"));
		this.setSingleton(CCLAUtils.getBinderBoolValue
		 (binder, "IS_SINGLETON"));		
	}

	public void validate(FWFacade facade) throws DataException {
		if (StringUtils.stringIsBlank(this.getRelation()))
			throw new DataException("Relation Name is missing");
		if (StringUtils.stringIsBlank(this.getShortName()))
			throw new DataException("Short Name is missing");
				
	}
	public static RelationName get(DataResultSet rsRelationName) throws DataException {
		
		Integer relationTypeId = CCLAUtils.getResultSetIntegerValue
		 (rsRelationName, "RELATION_TYPE_ID");
		
		RelationType relationType = null;
		
		if (relationTypeId != null) {
			relationType = RelationType.getCache().getCachedInstance(relationTypeId);
		}
		
		return new RelationName(
			CCLAUtils.getResultSetIntegerValue
			 (rsRelationName, "RELATION_NAME_ID"),				
			relationType,
			rsRelationName.getStringValueByName("RELATION"),
			rsRelationName.getStringValueByName("SHORT_NAME"),
			CCLAUtils.getResultSetBoolValue
			 (rsRelationName, "IS_SINGLETON")
		);
	}
	
	/**
	 *  Use CacheManager instead.
	
	public static RelationName get(int RelationNameId, FWFacade facade) throws DataException
	{
		DataResultSet rsRelationName = getData(RelationNameId, facade);
		
		if (rsRelationName.isEmpty())
			return null;
		
		return RelationName.get(rsRelationName);	
		
	}
	*/
	
	/** Fetches a single entry from the RELATION table
	 *  
	 * @param RelationTypeId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getData(int RelationNameId, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, "RELATION_NAME_ID", RelationNameId);
		
		DataResultSet rsRelationName = 
		 facade.createResultSet("qClientServices_GetRelationName", binder);
		
		return rsRelationName;
	}
	public int getRelationNameId() {
		return relationNameId;
	}

	public void setRelationNameId(int relationNameId) {
		this.relationNameId = relationNameId;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public boolean isSingleton() {
		return isSingleton;
	}

	public void setSingleton(boolean isSingleton) {
		this.isSingleton = isSingleton;
	}

	public RelationType getRelationType() {
		return this.relationType;
	}
	
	public void setRelationType(RelationType relationType) {
		this.relationType = relationType;
	}

	@Override
	public String toString() {
		return "RelationName [isSingleton=" + isSingleton + ", relation="
				+ relation + ", relationNameId=" + relationNameId
				+ ", relationType=" + relationType + ", shortName=" + shortName
				+ "]";
	}
	
	public boolean equals(RelationName relName) {
		return (this.getRelationNameId() == relName.getRelationNameId());
	}
	
	public static Vector<RelationName> getAll(FWFacade facade) throws DataException {
		
		DataResultSet rsNames = facade.createResultSet
		("qClientServices_GetElementRelationNames", new DataBinder());
		
		Vector<RelationName> relNames = new Vector<RelationName>();
		
		if (rsNames.first()) {
			do {
				relNames.add(get(rsNames));
			} while (rsNames.next());
		}
		
		return relNames;
	}
	
	/*
	 *  Caching stuff
	 */
	public static Cache CACHE = new Cache();
	
	public static Cachable<Integer, RelationName> getCache() {
		return CACHE;
	}
	
	/** RelationType cache implementor */
	public static class Cache extends Cachable<Integer, RelationName> {

		public Cache() {
			super("Relation Name");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<RelationName> relNames = RelationName.getAll(facade);
			
			HashMap<Integer, RelationName> newCache = 
			 new HashMap<Integer, RelationName>();
			
			for (RelationName relName : relNames) {
				newCache.put(relName.getRelationNameId(), relName);
			}
			
			this.CACHE_MAP = newCache;
		}
	}

	@Override
	public int hashCode() {
		// Primary key
		return this.relationNameId;
	}
}
