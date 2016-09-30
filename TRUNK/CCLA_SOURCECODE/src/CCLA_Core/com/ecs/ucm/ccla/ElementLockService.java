package com.ecs.ucm.ccla;

import java.math.BigInteger;
import java.util.Random;
import java.util.Vector;

import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;

import intradoc.common.ServiceException;
import intradoc.data.DataException;
import intradoc.server.Service;

/** TODO: doesn't really belong in data package. Move to proper service package 
 *  elsewhere - perhaps ClientServices component?
 *  
 * @author Tom
 *
 */
public class ElementLockService extends Service{

	/**
	 * Simple Service class that provides access to the ElementLockManager
	 */
	
	/*
	 * Clear all ElementLocks
	 */
	public void clearElementLocks(){
		Log.info("clearElementLocks >>");
		ElementLockManager elm = ElementLockManager.getElementLockManager();
		elm.clearElementLocks();
	}
	
	/*
	 * Get all element lock info and resultset to the binder
	 */
	public void getElementLockInfo(){
		Log.info("getElementLockInfo >>");
		ElementLockManager elm = ElementLockManager.getElementLockManager();		
		m_binder.addResultSet("RS_ELEMENT_LOCKS", elm.getElementLockResultSet());
		
		elm.addElementLockDataToBinder(m_binder);
	}
	
	/*
	 * Removes a specific element lock
	 * 
	 * Required Binder Param
	 * - lockId - id of the ElementLock to remove
	 */
	public void removeElementLock() throws ServiceException, DataException {
		String lockId = m_binder.getLocal("lockId");
		
		if (StringUtils.stringIsBlank(lockId))
			throw new ServiceException("lockId must be on the binder");
		
		ElementLockManager elm = ElementLockManager.getElementLockManager();
		elm.removeElementLock(new BigInteger(lockId));	
	}
	
	/*
	 * Adds test ElementLocks to the ElementLockManager 
	 */
	public void addTestElementLock() throws ServiceException{
		Log.info("addTestElementLock");
		
		String context = m_binder.getLocal("context");
		String elementId = m_binder.getLocal("elementId");
		if (StringUtils.stringIsBlank(context)) {
			context = "addTestElementLock";
		}
		
		Vector<Integer> elementIdVector = new Vector<Integer>();
	    
		if (StringUtils.stringIsBlank(elementId)){
			Random randomGenerator = new Random();
			elementIdVector.add(new Integer(randomGenerator.nextInt(10000)));
		} else {
			elementIdVector.add(Integer.parseInt(elementId));
		}
		
		ElementLockManager elm = ElementLockManager.getElementLockManager();		
		
		try {
			elm.addElementsToLock(m_userData.m_name, context, elementIdVector);
		} catch (Exception e) {
			Log.info("unable to addTestElementLock: " + e.getMessage());
			e.printStackTrace();
			throw new ServiceException("unable to addTestElementLock: " + e.getMessage());
		}
	}
	
}
