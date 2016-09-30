package com.ecs.ucm.ccla.data;

import java.util.HashMap;
import java.util.Vector;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

/** Models entries from the REF_DATA_SOURCE table. */
public class DataSource {
	
	private int dataSourceId;
	private String name;
	
	public static final class Cols {
		public static final String ID 	= "DATA_SOURCE_ID";
		public static final String NAME = "DATA_SOURCE_NAME";
	}
	
	public static final class Ids {
		public static final int AURORA 	= 1;
		public static final int USER 	= 2;
		public static final int AURORA_SIGNATORIES = 6;
	}
	
	public DataSource(int dataSourceId, String name) {
		this.dataSourceId = dataSourceId;
		this.name = name;
	}
	
	public static Vector<DataSource> getAll(FWFacade facade) throws DataException {
		DataResultSet rs = facade.createResultSet
		 ("qCore_GetAllDataSources", new DataBinder());
		
		Vector<DataSource> sources = new Vector<DataSource>();
		
		if (rs.first()) {
			do {
				sources.add(get(rs));
			} while (rs.next());
		}
		
		return sources;
	}
	
	public static DataSource get(DataResultSet rs) throws DataException {
		return new DataSource(
			CCLAUtils.getResultSetIntegerValue(rs, Cols.ID),
			rs.getStringValueByName(Cols.NAME)
		);
	}
	
	public void setDataSourceId(int dataSourceId) {
		this.dataSourceId = dataSourceId;
	}
	public int getDataSourceId() {
		return dataSourceId;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	
	/*
	 *  Caching stuff
	 */
	private static Cache CACHE = new Cache();
	
	public static Cachable<Integer, DataSource> getCache() {
		return CACHE;
	}
	
	/** DataSource cache implementor.
	 *  
	 **/
	private static class Cache extends Cachable<Integer, DataSource> {

		public Cache() {
			super("Data Source");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<DataSource> objs = DataSource.getAll(facade);
			HashMap<Integer, DataSource> newCache = new HashMap<Integer, DataSource>();
			
			for (DataSource obj : objs) {
				newCache.put(obj.getDataSourceId(), obj);
			}
			
			this.CACHE_MAP = newCache;
		}
	}
}
