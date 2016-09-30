package com.ecs.ucm.ccla.data;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.shared.SharedObjects;

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.DateFormatter;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Models entries from the REF_WORK_GROUP table.
 *  
 *  This table contains a list of groups which users can be allocated to. It determines
 *  the pending work jobs they will be issued by the system.
 * 
 * @author Tom
 *
 */
public class WorkGroup {

	private int workGroupId;
	private String name;
	private String description;
	
	/** Name of UCM query used to fetch pending bundles for members of this group. */
	private String bundleQueryName;
	
	/** Comma-separated list of parameters which will be passed into the binder before
	 *  executing the above query, in the form 'name1=value1,name2=value2' */
	private String bundleQueryParams;
	
	/** Mapping generated from the bundleQueryParams string on construction.
	 * 
	 */
	private HashMap<String, String> queryParamMap;

	/** Default Work Group ID assigned to users who don't have their own work group
	 *  explicitly set in the USER_WORK_GROUP table.
	 */
	public static final int DEFAULT_WORK_GROUP_ID = 1;
	
	/** How many milliseconds old a bundle must be before it is returned by the
	 *  auto-fetch method. It is stored in an environment variable as a
	 *  number of minutes.
	 */
	public static final int MINIMUM_BUNDLE_AGE_MILLIS = 
	 SharedObjects.getEnvironmentInt("CCLA_minimumBundleAge", 0) * 1000 * 60;
	
	public static class Cols {
		public static final String ID = "WORK_GROUP_ID";
		public static final String NAME = "WORK_GROUP_NAME";
		public static final String DESCRIPTION = "WORK_GROUP_DESCRIPTION";
		public static final String BUNDLE_QUERY_NAME = "BUNDLE_QUERY_NAME";
		public static final String BUNDLE_QUERY_PARAMS = "BUNDLE_QUERY_PARAMS";
	}
	
	public WorkGroup(int workGroupId, String name, String description,
			String bundleQueryName, String bundleQueryParams) {
		this.workGroupId = workGroupId;
		this.name = name;
		this.description = description;
		this.bundleQueryName = bundleQueryName;
		this.bundleQueryParams = bundleQueryParams;
		
		this.queryParamMap = new HashMap<String, String>();
		
		if (!StringUtils.stringIsBlank(bundleQueryParams)) {
			// Build the query parameter mapping
			String[] nameValuePairs = bundleQueryParams.split(",");
			
			for (String nameValuePair : nameValuePairs) {
				String[] nameAndValue = nameValuePair.split("=");
				
				queryParamMap.put(nameAndValue[0], nameAndValue[1]);
			}
		}
	}
	
	public static Vector<WorkGroup> getAll(FWFacade facade) throws DataException {
		
		DataResultSet rs = facade.createResultSet
		 ("qCore_GetAllWorkGroups", new DataBinder());
		
		Vector<WorkGroup> workGroups = new Vector<WorkGroup>();
		
		if (rs.first()) {
			do {
				workGroups.add(get(rs));
			} while (rs.next());
		}
		
		return workGroups;
	}
	
	public static WorkGroup get(DataResultSet rs) throws DataException {
		if (rs.isEmpty())
			return null;
		
		return new WorkGroup(
			CCLAUtils.getResultSetIntegerValue(rs, Cols.ID),
			rs.getStringValueByName(Cols.NAME),
			rs.getStringValueByName(Cols.DESCRIPTION),
			rs.getStringValueByName(Cols.BUNDLE_QUERY_NAME),
			rs.getStringValueByName(Cols.BUNDLE_QUERY_PARAMS)
		);
	}
	
	/** Attempts to fetch a 'pending' bundle reference for this WorkGroup and user ID.
	 *  
	 * @param facade
	 * @return 	a ResultSet containing two columns: dDocName and xBundleRef. Returns
	 * 			emtpy ResultSet if there is no unlocked pending bundle.
	 * @throws DataException 
	 */
	public DataResultSet getNextPendingBundle(FWFacade facade, String userName) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		
		// Compute the 'embargo date' - this is a calculated date relative
		// to the current time. It is used to prevent new bundles being
		// returned, whose documents may not yet be processed.
		long currentTime = new Date().getTime();
		long embargoTime = currentTime - MINIMUM_BUNDLE_AGE_MILLIS;
		
		Date embargoDate = new Date(embargoTime);
		String embargoDateStr = DateFormatter.getTimeStamp(embargoDate);
		
		binder.putLocal("userId", userName);
		binder.putLocal("embargoDate", embargoDateStr);
		
		// Add custom query parameters for this work group, if any exist.
		if (!this.getQueryParamMap().isEmpty()) {
			for (String paramName : queryParamMap.keySet()) {
				binder.putLocal(paramName, queryParamMap.get(paramName));
			}
		}
		
		return facade.createResultSet(this.getBundleQueryName(), binder);
	}
	
	public static DataResultSet getNextPendingBundleForUser
	 (FWFacade facade, String userName) throws DataException { 
		
		UserWorkGroup userWorkGroup = 
		 UserWorkGroup.getCache().getCachedInstance(userName);
	
		WorkGroup workGroup;
		
		if (userWorkGroup == null) {
			workGroup = WorkGroup.getCache().getCachedInstance(DEFAULT_WORK_GROUP_ID);
		} else {
			workGroup = userWorkGroup.getWorkGroup();
		}
		
		return workGroup.getNextPendingBundle(facade, userName);
	}

	public int getWorkGroupId() {
		return workGroupId;
	}

	public void setWorkGroupId(int workGroupId) {
		this.workGroupId = workGroupId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBundleQueryName() {
		return bundleQueryName;
	}

	public void setBundleQueryName(String bundleQueryName) {
		this.bundleQueryName = bundleQueryName;
	}

	public String getBundleQueryParams() {
		return bundleQueryParams;
	}

	public void setBundleQueryParams(String bundleQueryParams) {
		this.bundleQueryParams = bundleQueryParams;
	}
	
	/*
	 *  Caching stuff
	 */
	private static Cache CACHE = new Cache();
	
	/** Cache of Work Group IDs to WorkGroup instances.
	 * 
	 * @return
	 */
	public static Cachable<Integer, WorkGroup> getCache() {
		return CACHE;
	}
	
	/** WorkGroup cache implementor */
	private static class Cache extends Cachable<Integer, WorkGroup> {

		public Cache() {
			super("Work Group");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<WorkGroup> workGroups = getAll(facade);
			
			HashMap<Integer, WorkGroup> newCache = 
			 new HashMap<Integer, WorkGroup>();
			
			for (WorkGroup workGroup : workGroups) {
				newCache.put(workGroup.getWorkGroupId(), workGroup);
			}
			
			this.CACHE_MAP = newCache;
		}
	}

	public HashMap<String, String> getQueryParamMap() {
		return queryParamMap;
	}
}
