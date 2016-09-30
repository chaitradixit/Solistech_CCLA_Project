package com.ecs.ucm.ccla.data.comm;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.util.HashMap;
import java.util.Vector;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Models entries from the REF_COMM_TYPE table.
 * 
 * @author Tom
 *
 */
public class CommType {
	
	public static final CommType DOCUMENT = new CommType(1, "Document");
	public static final CommType PHONE_CALL = new CommType(2, "Phone Call");
	public static final CommType USER = new CommType(3, "User");
	public static final CommType INTERACTION = new CommType(4, "Interaction");
	public static final CommType ENDOFDAY = new CommType(5, "End of Day");
	public static final CommType COMM_FIRST_TRANS_FEED = new CommType
	 (6, "Comm First Transaction Feed");
	public static final CommType TRANSACTION_BATCH = new CommType(7, "Transaction Batch");
	
	private int commTypeId;
	private String name;
	
	public CommType(int commTypeId, String name) {
		this.commTypeId = commTypeId;
		this.name = name;
	}
	
	public static Vector<CommType> getAll(FWFacade facade) throws DataException {
		Vector<CommType> types = new Vector<CommType>();
		
		DataResultSet rs = facade.createResultSet
		 ("qCore_GetAllCommTypes", new DataBinder());
		
		if (rs.first()) {
			do {
				types.add(get(rs));
			} while (rs.next());
		}
		return types;
	}
	
	public static CommType get(DataResultSet rs) throws DataException {
		if (rs.isEmpty())
			return null;
		
		return new CommType(
			CCLAUtils.getResultSetIntegerValue(rs, "COMM_TYPE_ID"),
			rs.getStringValueByName("COMM_TYPE_NAME")
		);
	}

	public void setCommTypeId(int commTypeId) {
		this.commTypeId = commTypeId;
	}
	public int getCommTypeId() {
		return commTypeId;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	
	public String toString() {
		return getName();
	}
	
	/*
	 *  Caching stuff
	 */
	private static Cache CACHE = new Cache();
	
	public static Cachable<Integer, CommType> getCache() {
		return CACHE;
	}
	
	/** CommType cache implementor */
	private static class Cache extends Cachable<Integer, CommType> {

		public Cache() {
			super("Comm Type");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<CommType> commTypes = CommType.getAll(facade);
			
			HashMap<Integer, CommType> newCache = 
			 new HashMap<Integer, CommType>();
			
			for (CommType status : commTypes) {
				newCache.put(status.getCommTypeId(), status);
			}
			
			this.CACHE_MAP = newCache;
		}
	}
	
	/**
	 * Overridden equals
	 */
	public boolean equals(Object obj) {
		
		if (!(obj instanceof CommType))
			return false;
		
		return (this.getCommTypeId()==((CommType)obj).getCommTypeId() &&
				this.getName().equals(((CommType)obj).getName()));
	}
}
