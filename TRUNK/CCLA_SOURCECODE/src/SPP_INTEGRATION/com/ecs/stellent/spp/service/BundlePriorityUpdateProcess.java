package com.ecs.stellent.spp.service;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.shared.SharedObjects;

import java.util.Date;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.CacheManager;
import com.ecs.ucm.ccla.data.SystemConfigVar;
import com.ecs.ucm.ccla.utils.DateUtils;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.embedded.FWFacade;

/** @deprecated Replaced with ScheduledEventFilter.
 * 
 * @author Tom
 *
 */
public class BundlePriorityUpdateProcess implements Runnable {

	//Time in ms to wait, default is 60000 (i.e. 60 secs)
	private static final long WAIT_TIME = 		
		SharedObjects.getEnvironmentInt("BUNDLE_PRIORITY_UPDATE_PROCESS_WAIT_TIME", 60000);

	private static final String START_TIME = 		
		SharedObjects.getEnvironmentValue("BUNDLE_PRIORITY_UPDATE_PROCESS_START_TIME");

	private static final String END_TIME = 		
		SharedObjects.getEnvironmentValue("BUNDLE_PRIORITY_UPDATE_PROCESS_END_TIME");
	
//	private static final int INTERVAL_TYPE = 		
//		SharedObjects.getEnvironmentInt("BUNDLE_PRIORITY_UPDATE_PROCESS_INTERVAL_TYPE", 0);
	
	
	private static final String START_TIME_CONFIG_VAR_NAME = "BundleProcessStartTime";
	private static final String END_TIME_CONFIG_VAR_NAME = "BundleProcessEndTime";	
	
	//Last date process did some work.
	private Date lastRunDate = null; 
	
	//Flag to indicate whether the process should do any work.
	private boolean allowRun  = true;
	// Flag to indiciate whether the process is doing any work.
	private boolean busy = false;
	
	public void run() 
	{	
		while (allowRun) 
		{		
			
			if (shouldRun() && !isBusy()) {
				doWork();
			} 

			try {
				Thread.sleep(WAIT_TIME);
			} catch (InterruptedException ie) {
				Log.warn("interrupted exception "+ie.getMessage());
			}
		}
		

	}

	/**
	 * initialising the process 
	 */
	public void init() {
		
	}

	
	private boolean shouldRun() 
	{	
		try {
			if (lastRunDate!=null) {
				//if date is before now, then run the process
				if (DateUtils.compareDates(lastRunDate, DateUtils.getNow())<0) {
					return checkTimes();
				}
				return false;
			} else {
				return checkTimes();
			}
		} catch (DataException de) {
			Log.error("Error Running Job",de);
			return false;
		}
		
	}
	
	private boolean checkTimes() throws DataException {
		
		SystemConfigVar startTimeVar = SystemConfigVar.getCache().getCachedInstance(START_TIME_CONFIG_VAR_NAME);
		SystemConfigVar endTimeVar = SystemConfigVar.getCache().getCachedInstance(END_TIME_CONFIG_VAR_NAME);

		Date startTime = null;
		Date endTime = null;
		
		if (startTimeVar!=null && endTimeVar!=null) {
			startTime = DateUtils.parseHHcmm(startTimeVar.getStringValue());
			endTime = DateUtils.parseHHcmm(endTimeVar.getStringValue());			
		} 
		
		if (startTime==null) {
			startTime = DateUtils.parseHHcmm(START_TIME);
		}
		
		if (endTime==null) {
			endTime = DateUtils.parseHHcmm(END_TIME);
		}
		
		if (startTime!=null && endTime!=null) {
			Date currentDate = DateUtils.getNow();
			if (DateUtils.compareTimes(currentDate, startTime)>=0 
					&& DateUtils.compareTimes(currentDate, endTime)<=0) {
				
				return true;
			}
		} 
		
		return false;
	}
	
	public void setAllowRun(boolean allowRun) {
		this.allowRun = allowRun;
	}
	
	public boolean getAllowRun() {
		return allowRun;
	}
	
	public void setBusy(boolean busy) {
		this.busy = busy;
	}
	
	public boolean isBusy() {
		return busy;
	}
	
	public void doWork() 
	{	
		Log.debug("START PROCESS: Refreshing all bundle priorities");
		
		busy = true;
		FWFacade facade = null;
		
		try {
			facade = CCLAUtils.getFacade(false);
		} catch (Exception e) {
			Log.error("Unable to create handler for SystemDatabase: " 
			 + e.getMessage(), e);
			busy = false;
		}
		
		if (facade!=null) {
			try {
				facade.beginTransaction();
				DataResultSet rsAllBundles =
				 facade.createResultSet("QGetAllBundleDocuments", new DataBinder());
	
				int updateCount = 0;
				int totalCount = 0;
			
//				for (int i=0; i<rsAllBundles.getNumFields(); i++) {
//					Log.debug(rsAllBundles.getFieldName(i));
//				}
				
				BundlePriorityServices.updatePriorityForDataResultSet(rsAllBundles, updateCount, totalCount, "dDocName" , "xBundleRef", facade);
				Log.debug("END PROCESS: Refreshing all bundle priorities");	
				facade.commitTransaction();
				
			} catch (DataException de) {
				facade.rollbackTransaction();
				Log.error("Cannot refresh all bundle priorities "+de.getMessage());	
			}			
			facade.releaseConnection();
			lastRunDate = DateUtils.getNow();
			busy = false;
		}		
	}
}
