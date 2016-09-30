package com.ecs.stellent.spp.filter;

import java.util.Date;

import com.ecs.stellent.spp.service.BundlePriorityServices;
import com.ecs.stellent.spp.service.BundleServices;
import com.ecs.stellent.spp.service.SppIntegrationUtils;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals;
import com.ecs.ucm.ccla.data.SystemConfigVar;
import com.ecs.ucm.ccla.utils.DateUtils;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ExecutionContext;
import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.data.Workspace;
import intradoc.shared.FilterImplementor;
import intradoc.shared.SharedObjects;

public class ScheduledEventFilter implements FilterImplementor {

	//Refresh Priorities Variables
	
	private static final String START_TIME = 		
		SharedObjects.getEnvironmentValue("BUNDLE_PRIORITY_UPDATE_PROCESS_START_TIME");
	private static final String END_TIME = 		
		SharedObjects.getEnvironmentValue("BUNDLE_PRIORITY_UPDATE_PROCESS_END_TIME");		
	private static final String START_TIME_CONFIG_VAR_NAME = "BundleProcessStartTime";
	private static final String END_TIME_CONFIG_VAR_NAME = "BundleProcessEndTime";	
	private static final String BUNDLE_PRIORITY_REFRESH_LAST_RUN_DATE = "BundlePriorityRefreshLastRunDate";	
	
	
	//Unflag Bundle Variables
	private static final String UNFLAGGED_START_TIME = 		
		SharedObjects.getEnvironmentValue("UNFLAGGED_BUNDLE_PRIORITY_UPDATE_PROCESS_START_TIME");
	private static final String UNFLAGGED_END_TIME = 		
		SharedObjects.getEnvironmentValue("UNFLAGGED_BUNDLE_PRIORITY_UPDATE_PROCESS_END_TIME");
	private static final String UNFLAGGED_BUNDLE_AGE = 		
		SharedObjects.getEnvironmentValue("UNFLAGGED_BUNDLE_AGE");	

	private static final String UNFLAGGED_START_TIME_CONFIG_VAR_NAME = "UnflagBundleProcessStartTime";
	private static final String UNFLAGGED_END_TIME_CONFIG_VAR_NAME = "UnflagBundleProcessEndTime";	
	private static final String UNFLAGGED_BUNDLE_AGE_CONFIG_VAR_NAME = "UnflagBundleAge";	
	private static final String UNFLAGGED_BUNDLE_ENABLED = "UnflagBundleEnable";
	private static final String UNFLAGGED_BUNDLE_LAST_RUN_DATE = "UnflagBundleLastRunDate";
	
	
	//Date properties
	private static Date lastUnflaggedRunDate = null; 
	private static Date lastRunDate = null;
	private static boolean busy = false;
	
	//Interval type
	private static final int INTERVAL_DAILY 	= 1;
	private static final int INTERVAL_HOURLY 	= 2;
	private static final int INTERVAL_WEEKLY 	= 3;
	
	
	public int doFilter(Workspace workspace, DataBinder dataBinder, 
			ExecutionContext executionContext)
			throws DataException, ServiceException {
		
		//This runs between 03:00 - 04:00
		SystemConfigVar startTimeVar = 
			SystemConfigVar.getCache().getCachedInstance(START_TIME_CONFIG_VAR_NAME);
		SystemConfigVar endTimeVar = 
			SystemConfigVar.getCache().getCachedInstance(END_TIME_CONFIG_VAR_NAME);
		
		SystemConfigVar lastRunVar = null;
		//get it from the SystemConfigVar if null
		if (lastRunDate==null) {
			lastRunVar = 
				SystemConfigVar.getCache().getCachedInstance(BUNDLE_PRIORITY_REFRESH_LAST_RUN_DATE);
			if (lastRunVar!=null && lastRunVar.getDateValue()!=null) {
				lastRunDate = lastRunVar.getDateValue();
			}
		}
		
		Date startTime = null;
		Date endTime = null;
		
		if (startTimeVar!=null && endTimeVar!=null) {
			startTime = DateUtils.parseHHcmm(startTimeVar.getStringValue());
			endTime = DateUtils.parseHHcmm(endTimeVar.getStringValue());			
		} 
		
		if (startTime==null)
			startTime = DateUtils.parseHHcmm(START_TIME);
		
		if (endTime==null)
			endTime = DateUtils.parseHHcmm(END_TIME);
		
		
		if (!busy && shouldRun(INTERVAL_DAILY, startTime, endTime, lastRunDate)) {
			FWFacade facade = CCLAUtils.getFacade(workspace,false);
			doRefreshPriorities(facade);
		} 
		
		
		//This runs between 06:00 - 23:00
		SystemConfigVar unflagEnabled = SystemConfigVar.getCache().getCachedInstance(UNFLAGGED_BUNDLE_ENABLED);
		
		if (unflagEnabled!=null && 
				unflagEnabled.getBoolValue()!=null && 
				unflagEnabled.getBoolValue().equals(true)) {

			startTimeVar = SystemConfigVar.getCache().getCachedInstance(UNFLAGGED_START_TIME_CONFIG_VAR_NAME);
			endTimeVar = SystemConfigVar.getCache().getCachedInstance(UNFLAGGED_END_TIME_CONFIG_VAR_NAME);
			startTime = null;
			endTime = null;

			if (lastUnflaggedRunDate==null) {
				lastRunVar = 
					SystemConfigVar.getCache().getCachedInstance(UNFLAGGED_BUNDLE_LAST_RUN_DATE);
				if (lastRunVar!=null && lastRunVar.getDateValue()!=null) {
					lastUnflaggedRunDate = lastRunVar.getDateValue();
				}
			}			
			
			if (startTimeVar!=null && endTimeVar!=null) {
				startTime = DateUtils.parseHHcmm(startTimeVar.getStringValue());
				endTime = DateUtils.parseHHcmm(endTimeVar.getStringValue());			
			} 
			
			if (startTime==null)
				startTime = DateUtils.parseHHcmm(UNFLAGGED_START_TIME);
			
			if (endTime==null) 
				endTime = DateUtils.parseHHcmm(UNFLAGGED_END_TIME);
			
			if (!busy && shouldRun(INTERVAL_HOURLY, startTime, endTime, lastUnflaggedRunDate)) {
				FWFacade facade = CCLAUtils.getFacade(workspace,false);
				doUnflagBundles(facade);
			} 		
		}
		return FilterImplementor.CONTINUE;
	}

	

	private boolean shouldRun(int intervalType, Date startTime, Date endTime, Date lastRanDate) 
	{
		if (startTime==null || endTime==null)
			return false;
		
		if (lastRanDate==null)
			return isAllowedTime(startTime, endTime);
		
		
		boolean allowed = false;
			
		switch (intervalType) {
			case INTERVAL_DAILY:
				if (DateUtils.compareDates(lastRanDate, DateUtils.getNow())<0) {
					allowed = isAllowedTime(startTime, endTime);
				} 
				break;
			case INTERVAL_HOURLY:
				if (DateUtils.getNow().getTime() - lastRanDate.getTime()>(3600000)) {
					allowed = isAllowedTime(startTime, endTime);
				}
				break;
			case INTERVAL_WEEKLY:
				if (DateUtils.compareDates(DateUtils.addDays(lastRanDate,7), DateUtils.getNow())<0) {
					allowed = isAllowedTime(startTime, endTime);
				} 
				break;
			default:
				Log.error("Unknown interval type, not running");
				allowed = false;
				break;
		}
		
		return allowed;
		
	}
	
	private boolean isAllowedTime(Date startTime, Date endTime) {
		if (startTime!=null && endTime!=null) {
			Date currentDate = DateUtils.getNow();
			if (DateUtils.compareTimes(currentDate, startTime)>=0 
					&& DateUtils.compareTimes(currentDate, endTime)<=0) {
				return true;
			} 		
		} 
		return false;
	}
	
		
	
	public void doUnflagBundles(FWFacade facade) {
		Log.debug("START PROCESS: Unflagged bundle priorities");
		busy = true;
		
		Float age = null;
		//check if the age are valid
		try {
			SystemConfigVar configVar = SystemConfigVar.getCache().getCachedInstance(UNFLAGGED_BUNDLE_AGE_CONFIG_VAR_NAME);
			
			if (configVar!=null)
				age = configVar.getFloatValue();
		} catch (Exception de) { 
			Log.error("error with SystemConfigVar", de);
			age=null;
		}
		
		if (age==null) {
			if (!StringUtils.stringIsBlank(UNFLAGGED_BUNDLE_AGE)) {
				try {
					age = Float.parseFloat(UNFLAGGED_BUNDLE_AGE);
				} catch (NumberFormatException nfe) {
					Log.error("Cannot convert unflagged bundle age to float: "+nfe.getMessage());
				}
			}
		}
		
		if (age==null) {
			Log.debug("Age is blank. Not unflagging bundles");
			Log.debug("END PROCESS: finished Unflagged bundle priorities");	
			busy = false;
			return;
		}

		if (facade!=null) {
			try {
		
				Log.debug("-- Calling BundleServices.unflagAgedBundles...");
				BundleServices.unflagAgedBundles(facade, age, false);

			} catch (Exception e) {

				Log.error("Cannot refresh all unflag priorities "+e.getMessage());	
			} finally {		
				lastUnflaggedRunDate = DateUtils.getNow();
				busy = false;
				try {
					SystemConfigVar configVar = 
						SystemConfigVar.getCache().getCachedInstance(UNFLAGGED_BUNDLE_LAST_RUN_DATE);
					if (configVar!=null) {
						configVar.setDateValue(lastUnflaggedRunDate);
						configVar.persist(CCLAUtils.getFacade(true), Globals.Users.System);
					}
				} catch (Exception e) {
					Log.info("Error updating "+UNFLAGGED_BUNDLE_LAST_RUN_DATE+" :"+e.getMessage(), e);
				}
			}
		} else {
			busy = false;
		}
		Log.debug("END PROCESS: finished Unflagged bundle priorities");	
	}	
	
	public void doRefreshPriorities(FWFacade facade) 
	{	
		Log.debug("START PROCESS: Refreshing all bundle priorities");		
		busy = true;
		
		if (facade!=null) {
			try {
				//facade.beginTransaction();
				DataResultSet rsAllBundles =
				 facade.createResultSet("QGetAllBundleDocuments", new DataBinder());
	
				int updateCount = 0;
				int totalCount = 0;
			
				BundlePriorityServices.updatePriorityForDataResultSet(rsAllBundles, updateCount, totalCount, "dDocName" , "xBundleRef", facade);
				Log.debug("END PROCESS: Refreshing all bundle priorities");	
				//facade.commitTransaction();
				
			} catch (DataException de) {
				//facade.rollbackTransaction();
				Log.error("Cannot refresh all bundle priorities "+de.getMessage());	
			} finally {		
				//facade.releaseConnection();
				lastRunDate = DateUtils.getNow();
				busy = false;				
				try {
					SystemConfigVar configVar = 
						SystemConfigVar.getCache().getCachedInstance(BUNDLE_PRIORITY_REFRESH_LAST_RUN_DATE);
					if (configVar!=null) {
						configVar.setDateValue(lastRunDate);
						configVar.persist(CCLAUtils.getFacade(true), Globals.Users.System);
					}
				} catch (Exception e) {
					Log.info("Error updating "+BUNDLE_PRIORITY_REFRESH_LAST_RUN_DATE+" :"+e.getMessage(), e);
				}
			}
		} else {
			busy = false;
		}
	}	
}
