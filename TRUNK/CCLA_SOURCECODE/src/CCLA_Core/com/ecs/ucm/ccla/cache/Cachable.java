package com.ecs.ucm.ccla.cache;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import intradoc.data.DataException;
import intradoc.shared.SharedObjects;

import com.ecs.ucm.ccla.CacheManager;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Used to implement caching functionality on Persistable object implementations.
 *  
 *  For a Persistable that you want to cache, an inner class should be created that
 *  extends this class, where K is your cache key (e.g. the unique ID) and V
 *  is the Persistable's class.
 *  
 *  To expose it to the CacheManager, so it will actually get built on server startup,
 *  you must add a reference to the Cachable instance in the CacheManager.init() method.
 * 
 * @author Tom
 *
 * @param <K> cache key, usually an Integer
 * @param <V> cache value, usually a Persistable implementation
 */
public abstract class Cachable<K,V> {
	
	protected HashMap<K,V> CACHE_MAP;
	protected String CACHE_NAME;
	
	/** This must be set on the first buildCache call. */
	protected boolean INITIALIZED = false;
	
	/** Last time the cache was rebuilt. */
	protected Date LAST_REFRESHED = null;
	
	/** Connection Name */
	//Default is to use the CCLA POOL Id.
	//If value is left blank, it will use the UCM facade.
	protected String CONNECTION_NAME = 
		SharedObjects.getEnvironmentValue("CCLA_CORE_DatabaseConnectionPoolIds");;
	
		
	/** Creates a new Cachable with the given name. The name should be unique across
	 *  the application.
	 *  
	 * @param name
	 */
	public Cachable(String name) {
		this.CACHE_NAME = name;
	}
	
	/** Public method for rebuilding the cache.
	 * 
	 * @param facade
	 * @throws DataException
	 */
	public void buildCache(FWFacade facade) throws DataException {
		Log.debug("Building " + this.getCacheName() + " Cache...");
		
		
		doRebuild(facade);
		
		Log.debug("Cached " + CACHE_MAP.size() + " items.");
		
		this.LAST_REFRESHED = new Date();
		this.setInitialized(true);
	}
	
	
	/** Fetches the raw database entries for this cache type and stores them in the
	 *  map.
	 *  
	 *  Implementations of this method must ensure that the CACHE_MAP member variable
	 *  is never cleared, in case a fetch call occurs during a rebuild. The cache
	 *  map should be rebuilt using a temporary variable and then assigned to the 
	 *  CACHE_MAP variable at the end.
	 *
	 * @param facade
	 * @throws DataException
	 */
	protected abstract void doRebuild(FWFacade facade) throws DataException;
	
	/** Returns the raw cache mapping.
	 * 
	 * @return
	 */
	public HashMap<K,V> getCache() {
		return CACHE_MAP;
	}
	
	/** Prints all key-value pairs from the cache, using their toString() methods.
	 * 
	 */
	public void debugCache() {
		CacheManager.debugCache(this);
	}
	
	public String getCacheName() {
		return CACHE_NAME;
	}
	
	
	
	/** Returns an mapped entry for the given key in the cache.
	 * 
	 * @param key
	 * @return null if there is no mapping for the key
	 * @throws DataException if the cache hasn't been initialized yet
	 */
	public V getCachedInstance(K key) throws DataException {
		if (!isInitialized())
			throw new DataException(this.getCacheName() + 
			 " Cache not initialized");
		
		return this.getCache().get(key);
	}

	public boolean isInitialized() {
		return INITIALIZED;
	}

	public void setInitialized(boolean initialized) {
		INITIALIZED = initialized;
	}
	
	public Date getLastRefreshed() {
		return LAST_REFRESHED;
	}
	
	public boolean isEmpty() {
		return (getCache()==null || getCache().isEmpty());
	}
	
	public Iterator<V> getValuesIterator() {
		if (getCache()!=null) {
			return getCache().values().iterator();
		} else {
			return Collections.<V>emptyList().iterator();
		}
	}

	/**
	 * Gets the poolId to use. 
	 * @return
	 */
	public String getConnectionName() {
		return CONNECTION_NAME;
	}

	/**
	 * Sets the pool id to use for this cache.
	 * If this value is set to null, it will use the default ucm connector.
	 * Default value is the environmental var: CCLA_CORE_DatabaseConnectionPoolIds
	 * @param connectionName
	 */
	public void setConnectionName(String connectionName) {
		CONNECTION_NAME = connectionName;
	}
}
