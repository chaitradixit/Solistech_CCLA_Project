package com.ecs.ucm.ccla.data;

import java.util.HashMap;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Represents a single entry from the REF_RELATION_NAMES_SYNC table.
 *  
 *  All instances are cached on server startup - see CacheManager class
 *  
 * @author kd
 *
 */
public class RelationNameSync implements Persistable {

	public static final String COPY_RELATION 	= "Copy";
	public static final String DELETE_RELATION 	= "Delete";
	
	public int getRelationNameSyncId() {
		return relationNameSyncId;
	}

	public void setRelationNameSyncId(int relationNameSyncId) {
		this.relationNameSyncId = relationNameSyncId;
	}

	public int getRelationNameId1() {
		return relationNameId1;
	}

	public void setRelationNameId1(int relationNameId1) {
		this.relationNameId1 = relationNameId1;
	}

	public int getRelationNameId2() {
		return relationNameId2;
	}

	public void setRelationNameId2(int relationNameId2) {
		this.relationNameId2 = relationNameId2;
	}

	public String getSyncType() {
		return syncType;
	}

	public void setSyncType(String syncType) {
		this.syncType = syncType;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}


	private int relationNameSyncId;
	private int relationNameId1;
	private int relationNameId2;
	private String syncType;
	private String action;
	
	
	public RelationNameSync(int relationNameSyncId, int relationNameId1, 
	 int relationNameId2, String syncType, String action) {
		
		this.relationNameSyncId = relationNameSyncId;
		this.relationNameId1 = relationNameId1;
		this.relationNameId2 = relationNameId2;
		this.syncType = syncType;
		this.action = action;
	}
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "RELATION_NAME_SYNCHID", this.getRelationNameSyncId());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "RELATION_NAME_ID1", 
		 this.getRelationNameId1());	
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "RELATION_NAME_ID2", 
		 this.getRelationNameId2());
		CCLAUtils.addQueryParamToBinder
		 (binder, "SYNC_TYPE", this.getSyncType());	
		CCLAUtils.addQueryParamToBinder
		 (binder, "ACTION", this.getAction());				
	}

	public void persist(FWFacade facade, String username) throws DataException {
	// TODO
		
	}

	public void setAttributes(DataBinder binder) throws DataException {
		//TODO	
	}

	public void validate(FWFacade facade) throws DataException {
		//TODO
				
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
	
	public static RelationNameSync get(DataResultSet rsRelationNameSync) throws DataException {
		
		
		return new RelationNameSync(
			CCLAUtils.getResultSetIntegerValue
			 (rsRelationNameSync, "RELATION_NAME_SYNC_ID"),	
				CCLAUtils.getResultSetIntegerValue
				 (rsRelationNameSync, "RELATION_NAME_ID1"),	
					CCLAUtils.getResultSetIntegerValue
					 (rsRelationNameSync, "RELATION_NAME_ID2"),					 
			rsRelationNameSync.getStringValueByName("SYNC_TYPE"),
			rsRelationNameSync.getStringValueByName("ACTION")
		);
	}
	
	
	/** Fetches a single entry from the RELATION table
	 *  
	 * @param RelationTypeId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getData(int RelationNameSyncId, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, "RELATION_NAME_SYNC_ID", RelationNameSyncId);
		
		DataResultSet rsRelationName = 
		 facade.createResultSet("qClientServices_GetRelationNameSync", binder);
		
		return rsRelationName;
	}

	
	/*
	 *  Caching stuff
	 */
	private static ID1Cache ID1_CACHE = new ID1Cache();
	
	public static Cachable<Integer, RelationNameSync> getID1Cache() {
		return ID1_CACHE;
	}
	
	/** Relation Name ID 1 cache implementor */
	private static class ID1Cache extends Cachable<Integer, RelationNameSync> {

		public ID1Cache() {
			super("ID1 -> Relation Name Sync");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			HashMap<Integer, RelationNameSync> newCache = 
			 new HashMap<Integer, RelationNameSync>();
			
			DataResultSet rsNamesSync = facade.createResultSet
			("qClientServices_GetRelationNamesSync", new DataBinder());
			
			if (rsNamesSync.first()) {
				do {
					RelationNameSync thisNameSync = RelationNameSync.get(rsNamesSync);
					newCache.put(thisNameSync.getRelationNameId1(), thisNameSync);
					
				} while (rsNamesSync.next());
			}
			
			this.CACHE_MAP = newCache;
		}
	}


}
