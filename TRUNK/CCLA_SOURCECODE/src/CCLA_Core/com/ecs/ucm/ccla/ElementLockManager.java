package com.ecs.ucm.ccla;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.shared.SharedObjects;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;

import com.ecs.ucm.ccla.data.Globals;
import com.ecs.ucm.ccla.data.SystemConfigVar;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.DateFormatter;


/**
 * Responsible for storing and maintaining an singleton Element Lock list.
 * 
 * When the database attempts to modify Elements in a transaction, or modify Relations
 * between them, we need to keep a record of which elements are being modified. If 
 * another database call is attempting to modify the same element, it will be postponed 
 * or aborted until the current transaction is complete.
 * 
 * This helps to avoid DB deadlocking caused when 2 long-running transactions are
 * operating on an overlapping set of Element Relations.
 * 
 * @author PAULT
 *
 */
public class ElementLockManager{

	private static ElementLockManager elementLockManager = null;
	
	/** Mapping of Element IDs and ElementLock instances */
	private static HashMap<Integer,ElementLock> LOCKED_ELEMENTS = 
	 new HashMap<Integer, ElementLock>();
	/** Mapping of Lock ID and ElementLock instances */
	private static HashMap<BigInteger,ElementLock> ELEMENT_LOCKS =
	 new HashMap<BigInteger, ElementLock>();
	
	/** Mapping between blocked/sleeping thread names and the Lock ID which is blocking 
	 *  them.
	 */
	private static HashMap<String, ElementLock> WAITING_THREADS =
	 new HashMap<String, ElementLock>();
	
	/** Records the total time spent waiting to acquire Element Locks. */
	private static BigInteger TOTAL_SLEEP_TIME_MILLIS = BigInteger.ZERO;
	
	/** Records the total number of collisions when attempting to acquire a lock.
	 *  A single lock request may collide multiple times until it is granted.
	 */
	private static BigInteger TOTAL_COLLISIONS = BigInteger.ZERO;
	
	/** Records the total number of 'fatal' collisions, where the lock request is
	 *  explicitly denied - either due to MAX_SLEEP_TIME being reached, or the
	 *  RAISE_ERROR_ON_COLLISION flag being set.
	 */
	private static BigInteger TOTAL_FATAL_COLLISIONS = BigInteger.ZERO;
	
	/** Stores the value of the enabled flag since the last time it was fetched, via
	 *  a call is isElementLockingEnabled().
	 *  
	 *  This is used to detect a transition in the enabled state, re-initializing the
	 *  state of the lock list where applicable.
	 * 
	 */
	private static boolean enabled = false;
	
	/** Sequence number used as a key ID used for identifying ElementLock instances in
	 *  the list.
	 * 
	 */
	private static BigInteger NEXT_LOCK_ID = BigInteger.ZERO;
	
	/** Determines whether a lock collision will immediately raise an exception back
	 *  to the interface, instead of sleeping and retrying the lock.
	 *  
	 *  Defaults to False. May be overwritten by a call to reloadSleepConfiguration()
	 */
	private static boolean RAISE_ERROR_ON_COLLISION = false;
	
	/** Time (in ms) to sleep if a collision occurs, before the lock is re-attempted.
	 * 
	 *  Defaults to 3 seconds. May be overwritten by a call to reloadSleepConfiguration()
	 */
	private static Integer SLEEP_TIME = 3000;
	
	/** Maximum time (in ms) to sleep and retry when a collision occurs. An exception is 
	 * always raised if the request is unable to acquire the lock within this period.
	 * 
	 * Defaults to 60 seconds. May be overwritten by a call to reloadSleepConfiguration()
	 * 
	 */
	private static Integer MAX_SLEEP_TIME = 60000;
	
	protected ElementLockManager() {
	}
	
	public static ElementLockManager getElementLockManager() {
	    if(elementLockManager == null) {
	    	elementLockManager = new ElementLockManager();
	    }
	    return elementLockManager;
	}
	
	/** Returns the state of the System Config Var that determines whether Element
	 *  Locking functionality will be used or not.
	 *  
	 *  Should always be called internally before adding/checking locks.
	 *  
	 * @return
	 * @throws DataException 
	 */
	public static synchronized boolean isElementLockingEnabled() throws DataException {
		SystemConfigVar isEnabledVar = SystemConfigVar.getCache().getCachedInstance
		 ("ElementLocking_IsEnabled");
		
		boolean isNowEnabled = isEnabledVar != null && isEnabledVar.getBoolValue();
		
		if (isNowEnabled != enabled) {
			// Enabled state has changed.
			if (isNowEnabled) {
				// Disabled -> Enabled. Nothing to really do here.
			} else {
				// Enabled -> Disabled. Clear down the lock listings.
				LOCKED_ELEMENTS.clear();
				ELEMENT_LOCKS.clear();
				
				// Reset counters.
				TOTAL_SLEEP_TIME_MILLIS = BigInteger.ZERO;
				TOTAL_COLLISIONS 		= BigInteger.ZERO;
				TOTAL_FATAL_COLLISIONS 	= BigInteger.ZERO;
			}
		}
		
		enabled = isNowEnabled;
		return enabled;
	}
	
	/**
	 * Removes the ElementLock instance with the given ID, unlocking all the elements
	 * it contained.
	 * 
	 */
	public synchronized void removeElementLock(BigInteger lockId) throws DataException {
		Log.info("Removing all element locks with lockId: "+lockId);
		
		if (!isElementLockingEnabled()) {
			Log.info("Element locking is disabled, exiting");
			return;
		}
		
		ElementLock elementLock = ELEMENT_LOCKS.get(lockId);
		Log.info("Found " + elementLock.getElementIds().size() +
		 " elements against this lock");
		
		//Remove all elements from LOCKED_ELEMENTS
		if (elementLock!=null && elementLock.ElementIds!=null) {
			for (Integer elementId : elementLock.ElementIds) {
				Log.info("removing element ["+elementId+"] from LOCKED_ELEMENTS");
				LOCKED_ELEMENTS.remove(elementId);
			}
			
			//Now remove the lock object from the ELEMENT_LOCKS
			Log.info("Finally removing the ElementLock instance with lockId: " + lockId);
			ELEMENT_LOCKS.remove(lockId);
		}
	}
	
	/**
	 * Takes a list of Element IDs and attempts to add them to a lock.
	 * 
	 * Depending on the raise error/sleep settings, this method may block for a period
	 * of time waiting for currently-locked Element IDs to be freed up again.
	 * 
	 * If there is any contention, and the RAISE_ERROR_ON_COLLISION flag is set, an error 
	 * is thrown immediately, instead of blocking.
	 * 
	 * Otherwise, in the case of contention, the thread will sleep for an interval of 
	 * SLEEP_TIME before re-checking for contention. This will continue until MAX_SLEEP
	 * _TIME is reached - at this point an time-out error is thrown.
	 * 
	 * @param user
	 * @param context Optional information about the context of these locked elements,
	 * 				  e.g. which method requested it 
	 * @param elementIds 
	 * @throws ServiceException 
	 * @throws DataException 
	 * @throws InterruptedException 
	 */
	public BigInteger addElementsToLock(String user, 
	 String context, Vector<Integer> elementIds) 
	 throws ServiceException, DataException {
		
		Log.info(user + " is attempting to lock elements: "+ elementIds.toString() + 
				" from context: " + context);
		
		if(!isElementLockingEnabled()){
			Log.info("Element locking is disabled, exiting");
			return null;
		}
		
		boolean elementAlreadyLocked = false;
		
		// Elapsed sleep time.
		Integer SLEPT_FOR_MILLIS = 0; 
		Integer attempts = 0;
		
		do {
			attempts++;
			
			// Check if any of the passed elements are locked.
			Log.debug("Testing for contention against " + LOCKED_ELEMENTS.size() + 
			 " locked Elements (attempt " + attempts + ")");
			
			elementAlreadyLocked = false;
			
			for (Integer elementId : elementIds) {
				if(isElementLocked(elementId)) {
					elementAlreadyLocked = true;
					
					// Add a mapping to waiting thread set
					ElementLock lock = LOCKED_ELEMENTS.get(elementId);
					WAITING_THREADS.put(Thread.currentThread().getName(), lock);
					
					break;
				}
			}
			
			if (elementAlreadyLocked) {
				TOTAL_COLLISIONS = TOTAL_COLLISIONS.add(BigInteger.ONE);
				
				// Reload all System Config Vars relating to Error Raising/Sleep Times.
				reloadSleepConfiguration();
				
				if (RAISE_ERROR_ON_COLLISION) {
					// We're done.
					TOTAL_FATAL_COLLISIONS = TOTAL_FATAL_COLLISIONS.add(BigInteger.ONE);
					
					Log.info("Element locking contention - raising error");
					throw new ServiceException("The elements you are trying to edit are "+
					 "currently being edited by someone else, please try again in a few" +
					 " minutes");
				}
				
				// Check that max sleep time hasn't been reached
				if (SLEPT_FOR_MILLIS.compareTo(MAX_SLEEP_TIME) >= 0) {
					TOTAL_FATAL_COLLISIONS = TOTAL_FATAL_COLLISIONS.add(BigInteger.ONE);
					
					Log.info("Maximum sleep time exceeded (" + SLEPT_FOR_MILLIS + "ms)"
					 + " - raising error");
					
					throw new ServiceException("Timed out waiting for access to " +
					 "elements. Another large update job may be in progress. If not, " +
					 "please contact an administrator");
				}
				
				// Sleep instead.
				try {
					Log.info("Sleeping thread for " + SLEEP_TIME + "ms");
					
					Thread.sleep(SLEEP_TIME);
					SLEPT_FOR_MILLIS += SLEEP_TIME;
					
					// Ugly..
					TOTAL_SLEEP_TIME_MILLIS = TOTAL_SLEEP_TIME_MILLIS.add
					 (new BigInteger(Long.toString(SLEEP_TIME)));
					
				} catch (InterruptedException e) {
					String err = "Error thrown whilst sleeping thread: " + e.getMessage();
					Log.info(err);
					e.printStackTrace();
					throw new ServiceException(err);
				}
			}
		
		} while (elementAlreadyLocked);
		
		// Remove mapping to waiting thread set, if one even exists.
		if (WAITING_THREADS.containsKey(Thread.currentThread().getName()))
			WAITING_THREADS.remove(Thread.currentThread().getName());
		
		
		// None of the elements we are trying to add are already locked, lock them now.
		ElementLock elementLock = createElementLock(user, context, elementIds);

		return elementLock.getLockId();
	}
	
	/** Called internally after the passed Element IDs are determined to be available for
	 *  locking.
	 *  
	 *  Creates a new ElementLock instance, adds it to the ELEMENT_LOCKS map, and adds 
	 *  the passed list of Element IDs to the LOCKED_ELEMENTS map.
	 * 
	 */
	private synchronized ElementLock createElementLock
	 (String user, String context, Vector<Integer> elementIds) {
		BigInteger lockId = getNextLockId();
		Date lockDate = new Date();
		
		Log.debug("Creating Element Lock for " + elementIds.size() + 
		 " elements, Lock ID: " + lockId);
	
		ElementLock elementLock = new ElementLock
		 (user, lockDate, lockId, context, elementIds);
				
		for (Integer elementId : elementIds) {
			Log.info("Locking element: " + elementId);
			LOCKED_ELEMENTS.put(elementId, elementLock);
		}
	
		ELEMENT_LOCKS.put(lockId, elementLock);
		return elementLock;
	}
	
	private synchronized void reloadSleepConfiguration() throws DataException {
		SystemConfigVar raiseErrorVar = SystemConfigVar.getCache().getCachedInstance
		 ("ElementLocking_RaiseErrorOnCollision");
		
		if (raiseErrorVar != null && raiseErrorVar.getBoolValue() != null)
			RAISE_ERROR_ON_COLLISION = raiseErrorVar.getBoolValue();
		
		SystemConfigVar sleepTimeVar = SystemConfigVar.getCache().getCachedInstance
		 ("ElementLocking_SleepTime");
		
		if (sleepTimeVar != null && sleepTimeVar.getIntValue() != null)
			SLEEP_TIME = sleepTimeVar.getIntValue();
		
		SystemConfigVar maxSleepTimeVar = SystemConfigVar.getCache().getCachedInstance
		 ("ElementLocking_MaxSleepTime");
		
		if (maxSleepTimeVar != null && maxSleepTimeVar.getIntValue() != null)
			MAX_SLEEP_TIME = maxSleepTimeVar.getIntValue();
		
		Log.debug("Reloaded sleep configuration. " +
		 "Raise Error on Collision? " + RAISE_ERROR_ON_COLLISION + ", " +
		 "Sleep Time: " + SLEEP_TIME + ", " +
		 "Max Sleep Time: " + MAX_SLEEP_TIME);
	}
	
	/**
	 * Takes elementId and checks the LOCKED_ELEMENTS to see if it is already locked
	 * @param elementId
	 */
	public boolean isElementLocked(Integer elementId){
		if(LOCKED_ELEMENTS.containsKey(elementId)){
			Log.info("Element: " + elementId + " is already locked");
			return true;
		}
		return false;
	}
	
	public void clearElementLocks(){
		ELEMENT_LOCKS.clear();
		LOCKED_ELEMENTS.clear();
	}
	
	
	//Returns a unique lockId number used to create ElementLock objects
	private BigInteger getNextLockId() {
		NEXT_LOCK_ID = NEXT_LOCK_ID.add(BigInteger.ONE);
		return NEXT_LOCK_ID;
	}
	
	/**
	 * Converts the element locks to a dataresultset
	 * 
	 * @return
	 */
	public DataResultSet getElementLockResultSet() {
		
		DataResultSet rsElementLocks = new DataResultSet(
			new String[] {
				"LOCKED_BY_USER","LOCK_DATE","LOCK_ID","CONTEXT",
				"ELEMENT_IDS", "THREAD_NAME", "WAITING_THREADS" 
			}
		);
		
		for (Entry<BigInteger, ElementLock> elementLock : ELEMENT_LOCKS.entrySet()) {
			Vector<String> v = elementLock.getValue().getDataVector();
			Log.info("Adding to result set: element lock info: "+v.toString());
			rsElementLocks.addRow(v);
		}
		
		return rsElementLocks;
	}
	
	/** Adds counters etc. to the passed binder
	 * 
	 */
	public void addElementLockDataToBinder(DataBinder binder) {
		binder.putLocal("TOTAL_SLEEP_TIME_MILLIS", TOTAL_SLEEP_TIME_MILLIS.toString());
		binder.putLocal("TOTAL_COLLISIONS", TOTAL_COLLISIONS.toString());
		binder.putLocal("TOTAL_FATAL_COLLISIONS", TOTAL_FATAL_COLLISIONS.toString());
	
		binder.putLocal("TOTAL_LOCKS", NEXT_LOCK_ID.toString());
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "TOTAL_WAITING_THREADS", WAITING_THREADS.size());
	}
	
	/*
	 * Stores details for a set of locked elements
	 */
	private class ElementLock {
		
		String user; 
		Date lockDate;
		BigInteger lockId;
		String context;
		String threadName;
		Vector<Integer> ElementIds;
			
		public ElementLock(String user, Date lockDate, BigInteger lockId,
				String context, Vector<Integer> elementIds) {
			super();
			this.user = user;
			this.lockDate = lockDate;
			this.lockId = lockId;
			this.context = context;
			this.threadName = Thread.currentThread().getName();
			ElementIds = elementIds;
		}

		public String getUser() {
			return user;
		}

		public Date getLockDate() {
			return lockDate;
		}

		public BigInteger getLockId() {
			return lockId;
		}

		public String getContext() {
			return context;
		}

		public Vector<Integer> getElementIds() {
			return ElementIds;
		}

		
		public String getThreadName() {
			return threadName;
		}
		
		public Vector<String> getDataVector(){
			
			Vector<String> v = new Vector<String>();
			v.add(getUser());
			v.add(DateFormatter.getTimeStamp(getLockDate()));
			v.add(getLockId().toString());
			v.add(getContext());
			v.add(getElementIds().toString());
			v.add(getThreadName());
			v.add(getWaitingThreads().toString());
			
			return v;
		}
		
		/** Returns a list of all Thread names currently being blocked by this lock.
		 * 
		 * @return
		 */
		public Vector<String> getWaitingThreads() {
			Vector<String> waitingThreads = new Vector<String>();
			
			Set<Map.Entry<String, ElementLock>> waitingThreadEntries = 
			 WAITING_THREADS.entrySet();
			
			for (Map.Entry<String, ElementLock> waitingThread : waitingThreadEntries) {
				if (waitingThread.getValue().equals(this)) {
					// Found a waiting thread
					waitingThreads.add(waitingThread.getKey());
				}
			}
			
			return waitingThreads;
		}
		
		public boolean equals(ElementLock lock) {
			return lock.getLockId().compareTo(this.getLockId()) == 0;
		}
	}
	
}

