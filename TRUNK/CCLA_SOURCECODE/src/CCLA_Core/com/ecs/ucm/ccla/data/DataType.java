package com.ecs.ucm.ccla.data;

import java.util.HashMap;
import java.util.Vector;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CacheManager;
import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.DataResultSetUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Models entries from the REF_DATA_TYPE table.
 * 
 *  These need to be referenced directly when storing/parsing instruction data values,
 *  sothat each available data type is coded as a static member of this class.
 *  
 * @author Tom
 *
 */
public class DataType implements Persistable {
	
	private String name;
	private String description;
	
	public static final DataType STRING = 	new DataType("STRING", "Alphanumeric data");
	public static final DataType DATE = 	new DataType("DATE", "Dates and times");
	public static final DataType BOOL = 	new DataType("BOOL", "Flags (0/1)");
	public static final DataType INT = 		new DataType("INT", "Whole numbers");
	public static final DataType FLOAT = 	new DataType("FLOAT", "Decimal numbers");
	
	public DataType(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	/** Checks for a matching datatype.
	 * 
	 * @param dataType
	 * @return
	 */
	public boolean equals(DataType dataType) {
		return this.getName().equals(dataType.getName());
	}
	
	public static Vector<DataType> getAll(FWFacade facade) throws DataException {
		DataResultSet rs = facade.createResultSet
		 ("qCore_GetAllDataTypes", new DataBinder());
		
		Vector<DataType> dataTypes = new Vector<DataType>();
		
		if (rs.first()) {
			do {
				dataTypes.add(get(rs));
			} while (rs.next());
		}
		
		return dataTypes;
	}
	
	public static DataType get(DataResultSet rs) {
		if (rs.isEmpty())
			return null;
		
		return new DataType(
			DataResultSetUtils.getResultSetStringValue(rs, "DATA_TYPE_NAME"),
			DataResultSetUtils.getResultSetStringValue(rs, "DATA_TYPE_DESCRIPTION")
		);
	}
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		// TODO Auto-generated method stub
		
	}

	public void persist(FWFacade facade, String username) throws DataException {
		// TODO Auto-generated method stub
		
	}

	public void setAttributes(DataBinder binder) throws DataException {
		// TODO Auto-generated method stub
		
	}

	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub
		
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
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
	
	public static Cachable<String, DataType> getCache() {
		return CACHE;
	}
	
	/** DataType cache implementor.
	 *  
	 *  Maps DataType names against DataType instances
	 *  
	 **/
	private static class Cache extends Cachable<String, DataType> {

		public Cache() {
			super("Data Type");
		}
		
		public void doRebuild(FWFacade facade) throws DataException {
			Vector<DataType> dataTypes = DataType.getAll(facade);
			
			HashMap<String, DataType> newCache = new HashMap<String, DataType>();
			
			for (DataType dataType : dataTypes) {
				newCache.put(dataType.getName(), dataType);
			}
			
			this.CACHE_MAP = newCache;
		}
	}
}
