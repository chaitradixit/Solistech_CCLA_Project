package com.ecs.ucm.ccla.data.staticdataupdate;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.util.HashMap;
import java.util.Vector;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.utils.stellent.embedded.FWFacade;

public class StaticDataUpdate {

	/** Database Columns **/
	public static final class Cols {
		public static final String SDU_ID 				= "SDU_ID";
		public static final String SDU_NAME 			= "SDU_NAME";
		public static final String CREATE_INSTR_TYPE_ID = "CREATE_INSTR_TYPE_ID";
		public static final String UPDATE_INSTR_TYPE_ID = "UPDATE_INSTR_TYPE_ID";	
	}
	
	/** Queries to use **/
	public static final class Queries {
		public static final String GET_ALL_QUERY = "qCore_GetAllSDU";
	}
	
	/** Quick Id lookup, should reflect the DB table. **/
	public static final class Ids {
		public static final int AURORA_CORRESPONDENT 	= 1;
		public static final int AURORA_CLIENT 			= 2;
		public static final int AURORA_ACCOUNT 			= 3;
	}
	
	/** Properties **/
	private int id;
	private String name;
	private Integer createInstrTypeId;
	private Integer updateInstrTypeId;
	
	
	public StaticDataUpdate(int id, String name, Integer createInstrTypeId, Integer updateInstrTypeId) {
		this.id = id;
		this.name = name;
		this.createInstrTypeId = createInstrTypeId;
		this.updateInstrTypeId = updateInstrTypeId;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getCreateInstrTypeId() {
		return createInstrTypeId;
	}
	public void setCreateInstrTypeId(Integer createInstrTypeId) {
		this.createInstrTypeId = createInstrTypeId;
	}
	public Integer getUpdateInstrTypeId() {
		return updateInstrTypeId;
	}
	public void setUpdateInstrTypeId(Integer updateInstrTypeId) {
		this.updateInstrTypeId = updateInstrTypeId;
	}
	
	
	/**
	 * Gets a Vector of StaticDataUpdate.
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Vector<StaticDataUpdate> getAll(FWFacade facade) throws DataException {
		Vector<StaticDataUpdate> stdVec = new Vector<StaticDataUpdate>();
		
		DataResultSet rs = facade.createResultSet
		 (Queries.GET_ALL_QUERY, new DataBinder());
		
		if (rs.first()) {
			do {
				stdVec.add(get(rs));
			} while (rs.next());
		}
		return stdVec;
	}
	
	/**
	 * Returns a StaticDataUpdate object from the DataResultSet or null if empty
	 * @param rs
	 * @return
	 * @throws DataException
	 */
	public static StaticDataUpdate get(DataResultSet rs) throws DataException {
		if (rs.isEmpty())
			return null;
		
		return new StaticDataUpdate(
			CCLAUtils.getResultSetIntegerValue(rs, Cols.SDU_ID),
			CCLAUtils.getResultSetStringValue(rs, Cols.SDU_NAME),
			CCLAUtils.getResultSetIntegerValue(rs, Cols.CREATE_INSTR_TYPE_ID),
			CCLAUtils.getResultSetIntegerValue(rs, Cols.UPDATE_INSTR_TYPE_ID)
		);
	}
	
	/* ************************ Caching ************************************** */	
	private static Cache CACHE = new Cache();
	
	public static Cachable<Integer, StaticDataUpdate> getCache() {
		return CACHE;
	}
	
	/** StaticDataUpdate cache implementor */
	private static class Cache extends Cachable<Integer, StaticDataUpdate> {

		public Cache() {
			super("Static Data Update");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<StaticDataUpdate> stdVec = StaticDataUpdate.getAll(facade);
			
			HashMap<Integer, StaticDataUpdate> newCache = 
			 new HashMap<Integer, StaticDataUpdate>();
			
			for (StaticDataUpdate std : stdVec) {
				newCache.put(std.getId(), std);
			}
			
			this.CACHE_MAP = newCache;
		}
	}	
	
}
