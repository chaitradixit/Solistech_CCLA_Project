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

/** Models entries from the REF_COMM_STATUS table.
 *  
 *  
 * @author Tom
 *
 */
public class CommStatus {
	
	/** Default Ready status. */
	public static final CommStatus READY = new CommStatus(1, "Ready");
	
	private int commStatusId;
	private String name;
	
	public CommStatus(int commStatusId, String name) {
		this.commStatusId = commStatusId;
		this.name = name;
	}
	
	public static Vector<CommStatus> getAll(FWFacade facade) throws DataException {
		Vector<CommStatus> statuses = new Vector<CommStatus>();
		
		DataResultSet rs = facade.createResultSet
		 ("qCore_GetAllCommStatuses", new DataBinder());
		
		if (rs.first()) {
			do {
				statuses.add(get(rs));
			} while (rs.next());
		}
		return statuses;
	}
	
	public static CommStatus get(DataResultSet rs) throws DataException {
		if (rs.isEmpty())
			return null;
		
		return new CommStatus(
			CCLAUtils.getResultSetIntegerValue(rs, "COMM_STATUS_ID"),
			rs.getStringValueByName("COMM_STATUS_NAME")
		);
	}
	
	public void setCommStatusId(int commStatusId) {
		this.commStatusId = commStatusId;
	}
	public int getCommStatusId() {
		return commStatusId;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	
	public boolean equals(CommStatus commStatus) {
		return (this.getCommStatusId() == commStatus.getCommStatusId());
	}
	
	public String toString() {
		return getName();
	}
	
	/*
	 *  Caching stuff
	 */
	private static Cache CACHE = new Cache();
	
	public static Cachable<Integer, CommStatus> getCache() {
		return CACHE;
	}
	
	/** CommStatus cache implementor */
	private static class Cache extends Cachable<Integer, CommStatus> {

		public Cache() {
			super("Comm Status");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<CommStatus> commSources = CommStatus.getAll(facade);
			
			HashMap<Integer, CommStatus> newCache = 
			 new HashMap<Integer, CommStatus>();
			
			for (CommStatus status : commSources) {
				newCache.put(status.getCommStatusId(), status);
			}
			
			this.CACHE_MAP = newCache;
		}
	}
}
