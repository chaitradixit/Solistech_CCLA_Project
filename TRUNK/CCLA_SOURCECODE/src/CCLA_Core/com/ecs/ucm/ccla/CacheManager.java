package com.ecs.ucm.ccla;

import intradoc.common.ServiceException;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.data.Workspace;
import intradoc.server.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.ucm.ccla.cache.HolidayDateUtils;
import com.ecs.ucm.ccla.data.AccountFlag;
import com.ecs.ucm.ccla.data.AuthStatus;
import com.ecs.ucm.ccla.data.Company;
import com.ecs.ucm.ccla.data.DataSource;
import com.ecs.ucm.ccla.data.DataType;
import com.ecs.ucm.ccla.data.DocumentClass;
import com.ecs.ucm.ccla.data.ElementAttribute;
import com.ecs.ucm.ccla.data.ElementAttributeType;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Fund;
import com.ecs.ucm.ccla.data.HelpMapping;
import com.ecs.ucm.ccla.data.IdentityCheckScore;
import com.ecs.ucm.ccla.data.PersonTitle;
import com.ecs.ucm.ccla.data.Property;
import com.ecs.ucm.ccla.data.RelationName;
import com.ecs.ucm.ccla.data.RelationNameSync;
import com.ecs.ucm.ccla.data.RelationType;
import com.ecs.ucm.ccla.data.SystemConfigVar;
import com.ecs.ucm.ccla.data.UserWorkGroup;
import com.ecs.ucm.ccla.data.VerificationSource;
import com.ecs.ucm.ccla.data.WorkGroup;
import com.ecs.ucm.ccla.data.campaign.ApplicableEnrolmentAttribute;
import com.ecs.ucm.ccla.data.campaign.Campaign;
import com.ecs.ucm.ccla.data.campaign.CampaignEnrolmentAction;
import com.ecs.ucm.ccla.data.campaign.CampaignEnrolmentStatus;
import com.ecs.ucm.ccla.data.campaign.CampaignStatus;
import com.ecs.ucm.ccla.data.campaign.CampaignSubjectStatus;
import com.ecs.ucm.ccla.data.campaign.EnrolmentAttribute;
import com.ecs.ucm.ccla.data.campaign.EnrolmentStatusActionApplied;
import com.ecs.ucm.ccla.data.campaign.InvestmentIntentionStatus;
import com.ecs.ucm.ccla.data.form.FormType;
import com.ecs.ucm.ccla.data.instruction.ApplicableInstructionData;
import com.ecs.ucm.ccla.data.instruction.InstructionData;
import com.ecs.ucm.ccla.data.instruction.InstructionRulePriorityApplied;
import com.ecs.ucm.ccla.data.instruction.InstructionStatus;
import com.ecs.ucm.ccla.data.instruction.InstructionType;
import com.ecs.ucm.ccla.data.instruction.TransactionType;
import com.ecs.ucm.ccla.data.staticdataupdate.StaticDataUpdate;
import com.ecs.ucm.ccla.data.staticdataupdate.StaticDataUpdateApplied;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.DateFormatter;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Stores static instances of all required Cache objects.
 *  
 *  Contains service method for initialising or refreshing all
 *  caches.
 *  
 * @author Tom
 *
 */
public class CacheManager extends Service {
	
	/** initialise flag **/
	private static boolean initialized = false;
	
	/** flag to indicate whether the default cache has been rebuilt **/
	private static boolean rebuildAll = false;
	
	/** Mapping of Cachable names against their instances.
	 * 
	 *  Updated in step with the cachables Vector.
	 * 
	 *  Used to fetch/update particular Cachable instances.
	 */
	private static HashMap<String, Cachable<?,?>> cachablesMap = 
	 new HashMap<String, Cachable<?,?>>();
	
	
	/** Mapping of poolId against the cachable instances
	 * 
	 */
	private static HashMap<String,Vector<Cachable<?,?>>> poolCacheMap = 
		new HashMap<String,Vector<Cachable<?,?>>>();	
	
	
	/** List of Cachables.
	 * 
	 *  Updated in step with the cachableMap HashMap.
	 * 
	 *  When building/rebuilding the caches, each Cachable instance is built in the
	 *  order they occur in this list.
	 */
	private static Vector<Cachable<?,?>> cachables = new Vector<Cachable<?,?>>();
	
	/** Adds a new Cachable instance to the collection. This will expose it in the
	 *  rebuildAll and rebuildCache methods.
	 *  
	 *  The name of the cache must be unique in the collection or an error is thrown.
	 *  
	 *  If the Cachable is dependent on another, you must ensure the base Cachable is
	 *  added first.
	 *  
	 * @param cache
	 * @throws DataException
	 */
	public static void addCache(Cachable<?,?> cache) throws DataException {
//		Log.debug("Adding Cachable: " + cache.getCacheName());
		
		if (cachablesMap.containsKey(cache.getCacheName()))
			throw new DataException("Cache with name " + cache.getCacheName() +
			 " is already loaded");
		
		cachables.add(cache);
		cachablesMap.put(cache.getCacheName(), cache);
			
		Vector<Cachable<?,?>> vec = null; 
		
		boolean isNew = false;
		if (poolCacheMap.containsKey(cache.getConnectionName())) {
			vec = poolCacheMap.get(cache.getConnectionName());
		} else {
			vec = new Vector<Cachable<?,?>>();
			isNew = true;
		}
		
//		Log.debug("isNew:"+isNew+", "+cache.getCacheName()+" with Connector "+
//				(StringUtils.stringIsBlank(cache.getConnectionName())?"Default UCM":cache.getConnectionName()));
		vec.add(cache);
		poolCacheMap.put(cache.getConnectionName(), vec);
	}
	
	/** Adds a new Cachable instance to the collection and initializes it. 
	 *  
	 *  This will expose it in the rebuildAll and rebuildCache methods.
	 *  
	 *  The name of the cache must be unique in the collection or an error is thrown.
	 *  
	 *  If the Cachable is dependent on another, you must ensure the base Cachable is
	 *  added first.
	 */
	public static synchronized void addCacheAndInit(Cachable<?,?> cache, FWFacade facade) 
	 throws DataException {
		
		addCache(cache);	
		
		if (facade==null) {
			facade = CCLAUtils.getFacade(cache.getConnectionName());
		}
		
		cache.buildCache(facade);
	}
	
	/** Fetch a single Cachable instance by its name.
	 * 
	 * @param name
	 * @return null if there is no matching Cachable
	 */
	public static Cachable<?,?> getCache(String name) {
		return cachablesMap.get(name);
	}
	
	
	/** Rebuilds a single Cachable and cache instance.
	 * 
	 * @param cacheName
	 * @param facade
	 * @throws ServiceException
	 * @throws DataException 
	 */
	public static void rebuildCache(String cacheName, FWFacade facade) 
	 throws ServiceException, DataException {
		
		//Always look at the cachable references first
		Cachable<?,?> cachable = getCache(cacheName);
		
		if (cachable!=null)
			cachable.buildCache(facade);
		else
			throw new ServiceException("Unable to rebuild " + cacheName + 
			 " cache (not found)");
	}
	
	/** Runs only once, on the first call to rebuildAll(). Fills the Cachables 
	 *  collection but does not actually build any caches.
	 *  
	 *  Add order is very important here - ensure that any dependant Cachables are
	 *  loaded after their dependants.
	 *  
	 * @throws DataException 
	 */
	public static void init() throws DataException {
//		Log.debug("Loading in Cachables...");
//		
		// Core reference data caches
		addCache(DataSource.getCache());
		addCache(DataType.getCache());
		
		// Element data caches
		addCache(ElementType.getCache());
		
		// Element attribs/identifiers
		addCache(AuthStatus.getCache());
		addCache(ElementAttributeType.getCache());
		addCache(ElementAttribute.getCache());
		addCache(VerificationSource.getCache());
		
		// Person caches
		addCache(PersonTitle.getCache());
		addCache(IdentityCheckScore.getCache());
		addCache(IdentityCheckScore.getConditionCache());
		
		// Account caches
		addCache(AccountFlag.getCache());
		
		// Relation data caches
		addCache(RelationType.getCache());
		addCache(RelationName.getCache());
		addCache(RelationNameSync.getID1Cache());
		
		// Relation Property caches
		addCache(Property.getCache());
		
		// Aurora-related
		addCache(Company.getCache());
		addCache(Company.getNameCache());
		
		addCache(Fund.getCache());
		addCache(Fund.getInterFundCache());

		
		// Date caches
		addCache(HolidayDateUtils.getCache());
		
		// Instruction data caches
		addCache(TransactionType.getCache());
		addCache(InstructionStatus.getCache());
		addCache(InstructionData.getCache());
		addCache(InstructionType.getIdCache());
		addCache(InstructionType.getNameCache());
		
		// Document data caches
		addCache(DocumentClass.getCache());
		
		//addCache(UCMMetadataTranslation.getCache());
		addCache(ApplicableInstructionData.getCache());
		addCache(ApplicableInstructionData.getInstructionTypeCache());
		
		// Campaigns
		addCache(Campaign.getCache());
		addCache(CampaignStatus.getCache());
		addCache(CampaignEnrolmentStatus.getCache());
		addCache(CampaignEnrolmentAction.getCache());
		addCache(EnrolmentStatusActionApplied.getCache());
		addCache(CampaignSubjectStatus.getCache());
		addCache(InvestmentIntentionStatus.getCache());
		
		addCache(EnrolmentAttribute.getCache());
		addCache(ApplicableEnrolmentAttribute.getCache());
		
		// Forms
		addCache(FormType.getCache());
		
		// SystemConfigVar
		addCache(SystemConfigVar.getCache());
		addCache(SystemConfigVar.getTypeCache());
		
		//Instruction Priority Rules Applied
		//N.B Instruction Rule and Instruction Conditions are initiate
		addCache(InstructionRulePriorityApplied.getCache());
		
		// Work Group caches
		addCache(WorkGroup.getCache());
		addCache(UserWorkGroup.getCache());
		
		//HelpMapping
		addCache(HelpMapping.getCache());
		
		//StaticDataUpdate
		addCache(StaticDataUpdate.getCache());
		addCache(StaticDataUpdateApplied.getCache());
		addCache(StaticDataUpdateApplied.getIdCache());
		
		//TTLA Organisation cache
		addCache(Entity.getTTLACache());
	}
	
	/** Initialises/refreshes all caches.
	 * 
	 * @param facade
	 * @throws DataException 
	 */
	public static synchronized void rebuildAll(Workspace ws) 
	 throws DataException {
		
		if (!initialized) {
			init();
			initialized = true;
		}

		// Build all registered Cachable instances, in the order they were originally
		// added.
		Log.debug("Refreshing " + cachables.size() + " new caches...");
		
		//Store all cache with poolId and vector of cachable objects
		for (String poolId: poolCacheMap.keySet()) {
			
			FWFacade facade = null;
			if (ws!=null)
				facade = CCLAUtils.getFacade(ws, poolId);
			else 
				facade = CCLAUtils.getFacade(poolId);
				
			Log.debug("Using facade with pool id: "+
					(StringUtils.stringIsBlank(poolId)?"default UCM":poolId));
			
			for (Cachable<?,?> cachable : poolCacheMap.get(poolId)) {
				Log.debug("buildCache :"+cachable.getCacheName());
				cachable.buildCache(facade);
			}	
		}
		
		//set the rebuild all flag.
		rebuildAll = true;
	}
	
	/** Service-accessible method for forcing manual refresh of all cached
	 *  data sets.
	 * @throws DataException 
	 */
	public void refreshAllCaches() throws DataException {
		Log.debug("Manual Cache Refresh requested");
		rebuildAll(m_workspace);
	}
	
	/** Refreshes a single cache with the given name. Must be one of the 'new'
	 *  caches.
	 * 
	 */
	public void refreshCache() throws DataException, ServiceException {
		String cacheName = m_binder.getLocal("cacheName");
		
		Cachable<?,?> c = getCache(cacheName);
		
		if (c == null) {
			throw new ServiceException("No cache found with name: " + cacheName);
		}
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, c.getConnectionName());
		c.buildCache(facade);
		facade = null;
		Log.debug("Refreshed '" + cacheName + "' cache, " + c.getCache().size() 
		 + " entries cached");
	}
	
	/** Prints out the key value pairs from the passed Cachable implementor.
	 * 
	 * @param <K>
	 * @param <V>
	 * @param cachable
	 */
	public static <K,V> void debugCache(Cachable<K,V> cachable) {
		
		if (cachable.getCache() == null) {
			Log.debug(cachable.getCacheName() + " Cache not initialized");
			return;
		}
		
		Log.debug("Displaying " + cachable.getCache().size() + 
		 " items from the " + cachable.getCacheName() + " Cache:");
		
		for (Map.Entry<K, V> cachedItem : cachable.getCache().entrySet()) {
			Log.debug(	cachedItem.getKey().toString() + 
						" = " + 
						cachedItem.getValue().toString());
		}
	}
	
	/** Service method for fetching and displaying all Cachables.
	 * 
	 * @throws ServiceException 
	 * 
	 */
	public void getCachables() throws ServiceException {
		
		String[] cols = new String[] { 
			"NAME", "IS_INITIALIZED", "RESULTSET_ID", 
			"ITEM_COUNT", "SIZE", "LAST_REFRESHED"
		};
		
		DataResultSet rsCachables = new DataResultSet(cols);
		
		int i = 0;
		
		try {
			for (Cachable<?,?> c : cachables) {	
				Vector<String> v = new Vector<String>();
					
				v.add(c.getCacheName());
				v.add(c.isInitialized() ? "1" : "0");
				v.add(Integer.toString(i));
				v.add(Integer.toString(c.getCache().size()));
				v.add("");
				
				if (c.getLastRefreshed() != null)
					v.add(DateFormatter.getTimeStamp(c.getLastRefreshed()));
				else
					v.add("");
				
				rsCachables.addRow(v);
			}
			
			m_binder.addResultSet("rsCachables", rsCachables);
		
		} catch (Exception e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}
	
	/** Service method for outputting the contents of a single Cachable instance.
	 * 
	 * @throws ServiceException
	 */
	public void debugCache() throws ServiceException {
		String cacheName = m_binder.getLocal("cacheName");
		
		Cachable<?,?> c = getCache(cacheName);
		
		if (c == null)
			throw new ServiceException("No cache found with name: " + cacheName);
		
		c.debugCache();
	}
	
	/** Returns the approx size of a Java obect in bytes
	 * 
	 * @param o
	 * @return
	 * @throws IOException
	 */
	private static int sizeOf(Object o) throws IOException {
		ByteArrayOutputStream byteObject = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteObject);
		objectOutputStream.writeObject(o);
		
		objectOutputStream.flush();
		objectOutputStream.close();
		byteObject.close();

		return byteObject.toByteArray().length;
	}

	public static boolean isRebuildAll() {
		return rebuildAll;
	}

}
