package com.ecs.ucm.ccla.data;

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Models entries from the USER_WORK_GROUP table.
 *  
 *  Contains mappings between user names and WorkGroup IDs. Currently, a user name
 *  can only be mapped to a single WorkGroup ID.
 * 
 * @author Tom
 *
 */
public class UserWorkGroup implements Persistable {

	private int userWorkGroupId;
	private String userName;
	private WorkGroup workGroup;
	private Date lastUpdated;
	
	public static class Cols {
		public static final String ID = "USER_WORK_GROUP_ID";
	}
	
	public UserWorkGroup(int userWorkGroupId, String userName, WorkGroup workGroup, 
	 Date lastUpdated) {
		this.userWorkGroupId = userWorkGroupId;
		this.userName = userName;
		this.workGroup = workGroup;
		this.lastUpdated = lastUpdated;
	}
	
	/** Adds or updates the mapping for the given username.
	 * 
	 * @param userName
	 * @param workGroupId
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static UserWorkGroup addOrUpdate
	 (String userName, int workGroupId, FWFacade facade, String user) 
	 throws DataException {
		
		// Check for an existing mapped group for this user.
		UserWorkGroup userWorkGroup = getByUser(userName, facade);

		if (userWorkGroup != null) {
			
			if (userWorkGroup.getWorkGroup().getWorkGroupId() != workGroupId) {
				// Update existing user mapping
				userWorkGroup.setWorkGroup(
					WorkGroup.getCache().getCachedInstance(workGroupId)
				);
				userWorkGroup.persist(facade, user);
			}
			
		} else {
			// Add new user mapping
			userWorkGroup = add(userName, workGroupId, facade, user);
		}
		
		return userWorkGroup;
	}
	
	public static UserWorkGroup add(String userName, int workGroupId, 
	 FWFacade facade, String user) throws DataException {
		
		UserWorkGroup userWorkGroup = new UserWorkGroup(
			0,
			userName, 
			WorkGroup.getCache().getCachedInstance(workGroupId), 
			null
		);
		
		DataBinder binder = new DataBinder();
		
		userWorkGroup.addFieldsToBinder(binder);
		
		facade.execute("qCore_AddUserWorkGroup", binder);
		return getByUser(userName, facade);
	}
	
	public static UserWorkGroup get(int userWorkGroupId, FWFacade facade)
	 throws DataException {
		return get(getData(userWorkGroupId, facade));
	}
	
	public static UserWorkGroup getByUser(String userName, FWFacade facade)
	 throws DataException {
		return get(getDataByUser(userName, facade));
	}
	
	public static UserWorkGroup get(DataResultSet rs) throws DataException {
		if (rs.isEmpty())
			return null;
		
		return new UserWorkGroup(
			CCLAUtils.getResultSetIntegerValue(rs, Cols.ID),
			rs.getStringValueByName(SharedCols.USER),
			WorkGroup.getCache().getCachedInstance(
				CCLAUtils.getResultSetIntegerValue(rs, WorkGroup.Cols.ID)),
			rs.getDateValueByName(SharedCols.LAST_UPDATED)
		);
	}
	
	public static DataResultSet getData(int userWorkGroupId, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.ID, userWorkGroupId);
		
		return facade.createResultSet("qCore_GetUserWorkGroup", binder);
	}
	
	public static DataResultSet getDataByUser(String userName, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		binder.putLocal(SharedCols.USER, userName);
		
		return facade.createResultSet("qCore_GetUserWorkGroupByUser", binder);
	}
	
	/** Removes an existing mapping, if one exists for the given username.
	 * 
	 * @param userName
	 * @param facade
	 * @throws DataException 
	 */
	public static void remove(String userName, FWFacade facade) throws DataException {

		DataBinder binder = new DataBinder();
		binder.putLocal(SharedCols.USER, userName);
		
		facade.execute("qCore_RemoveUserWorkGroup", binder);
	}
	
	public static Vector<UserWorkGroup> getAll(FWFacade facade) throws DataException {
		
		DataResultSet rs = facade.createResultSet
		 ("qCore_GetAllUserWorkGroups", new DataBinder());
		
		Vector<UserWorkGroup> userWorkGroups = new Vector<UserWorkGroup>();
		
		if (rs.first()) {
			do {
				userWorkGroups.add(get(rs));
			} while (rs.next());
		}
		
		return userWorkGroups;
	}

	public void addFieldsToBinder(DataBinder binder) throws DataException {
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.ID, this.getUserWorkGroupId());
		
		binder.putLocal(SharedCols.USER, this.getUserName());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, WorkGroup.Cols.ID, this.getWorkGroup().getWorkGroupId());
	}

	public void persist(FWFacade facade, String username) throws DataException {
		
		DataBinder binder = new DataBinder();
		
		this.validate(facade);
		this.addFieldsToBinder(binder);
		
		facade.execute("qCore_UpdateUserWorkGroup", binder);
	}

	public void setAttributes(DataBinder binder) throws DataException {
		throw new DataException("Not implemented");
	}

	public void validate(FWFacade facade) throws DataException {
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public WorkGroup getWorkGroup() {
		return workGroup;
	}

	public void setWorkGroup(WorkGroup workGroup) {
		this.workGroup = workGroup;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}
	
	/*
	 *  Caching stuff
	 */
	private static Cache CACHE = new Cache();
	
	/** Cache of User IDs to UserWorkGroup instances.
	 * 
	 * @return
	 */
	public static Cachable<String, UserWorkGroup> getCache() {
		return CACHE;
	}
	
	public void setUserWorkGroupId(int userWorkGroupId) {
		this.userWorkGroupId = userWorkGroupId;
	}

	public int getUserWorkGroupId() {
		return userWorkGroupId;
	}

	/** WorkGroup cache implementor */
	private static class Cache extends Cachable<String, UserWorkGroup> {

		public Cache() {
			super("User ID -> User Work Group");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<UserWorkGroup> userWorkGroups = getAll(facade);
			
			HashMap<String, UserWorkGroup> newCache = 
			 new HashMap<String, UserWorkGroup>();
			
			for (UserWorkGroup userWorkGroup : userWorkGroups) {
				newCache.put(userWorkGroup.getUserName(), userWorkGroup);
			}
			
			this.CACHE_MAP = newCache;
		}
	}
}
