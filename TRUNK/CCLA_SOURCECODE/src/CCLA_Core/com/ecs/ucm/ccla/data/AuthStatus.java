package com.ecs.ucm.ccla.data;

import java.util.HashMap;
import java.util.Vector;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Models entries in the REF_AUTH_STATUS table.
 * 
 * @author Tom
 *
 */
public class AuthStatus {
	
	private int authStatusId;
	private String name;

	public static class Cols {
		public static final String ID = 	"AUTH_STATUS_ID";
		public static final String NAME = 	"AUTH_STATUS_NAME";
	}
	
	public static class Ids {
		public static final int UNAUTHORISED = 1;
		public static final int PENDING_AUTHORISATION = 2;
		public static final int AUTHORISED = 3;
	}
	
	public AuthStatus(int authStatusId, String name) {
		this.authStatusId = authStatusId;
		this.name = name;
	}
	
	public static DataResultSet getAllData(FWFacade facade) throws DataException {
		return facade.createResultSet("qCore_GetAllAuthStatuses", new DataBinder());
	}
	
	public static Vector<AuthStatus> getAll(FWFacade facade) throws DataException {
		Vector<AuthStatus> v = new Vector<AuthStatus>();
		
		DataResultSet rs = getAllData(facade);
		
		if (rs.first()) {
			do {
				v.add(get(rs));
			} while (rs.next());
		}
		
		return v;
	}
	
	public static AuthStatus get(DataResultSet rs) throws DataException {
		return new AuthStatus(
			CCLAUtils.getResultSetIntegerValue(rs, Cols.ID),
			rs.getStringValueByName(Cols.NAME)
		);
	}

	public void setAuthStatusId(int authStatusId) {
		this.authStatusId = authStatusId;
	}

	public int getAuthStatusId() {
		return authStatusId;
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
	
	public static Cachable<Integer, AuthStatus> getCache() {
		return CACHE;
	}
	
	/** AuthStatus cache implementor.
	 *  
	 **/
	private static class Cache extends Cachable<Integer, AuthStatus> {

		public Cache() {
			super("Auth Status");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<AuthStatus> objs = getAll(facade);
			HashMap<Integer, AuthStatus> newCache = new HashMap<Integer, AuthStatus>();
	
			for (AuthStatus obj : objs) {
				newCache.put(obj.getAuthStatusId(), obj);
			}
			
			this.CACHE_MAP = newCache;
		}
	}
}
