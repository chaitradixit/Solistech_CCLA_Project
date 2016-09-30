package com.ecs.stellent.ccla.clientservices;

import java.util.Date;

import com.ecs.stellent.ccla.clientservices.processhandler.ClientProcessHandler;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Campaign;
import com.ecs.ucm.ccla.data.ClientProcess;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.DateFormatter;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.server.Service;

public class ProcessActionService extends Service {
	
	static ProcessActionQueueMonitor queueMonitor = null;
	
	static {
		queueMonitor = new ProcessActionQueueMonitor
		 (ProcessActionQueueMonitor.DEFAULT_SLEEP_INTERVAL);
	}
	
	/** Adds the selected set of items to the process action queue.
	 *  The passed ResultSet is expected to have a PROCESS_ID column,
	 *  this is used to extract process IDs. 
	 *  
	 * @return number of items added to queue
	 * @throws DataException */
	public static int addItemsToQueue
	 (DataResultSet items, String action, FWFacade facade) 
	 throws DataException {
		
		Log.debug("Attempting to add " + items.getNumRows() + 
		 " processes to the queue with action: " + action);
		
		int itemCount = 0;
		
		do {
			String processId = items.getStringValueByName("PROCESS_ID");
			
			if (StringUtils.stringIsBlank(processId)) {
				Log.debug("Couldn't find process ID in row: " + items.getCurrentRow());
				continue;
			}
			
			String processActionId = 
			 CCLAUtils.getNewKey("ProcessAction", facade);
			
			DataBinder binder = new DataBinder();
			binder.putLocal("processActionId", processActionId);
			binder.putLocal("processId", processId);
			binder.putLocal("action", action);
			binder.putLocal("completed", "0");
			binder.putLocal("completeDate", "");
			binder.putLocal("errorMsg", "");
			binder.putLocal("dateAdded", DateFormatter.getTimeStamp());
			
			facade.execute("qClientServices_AddProcessAction", binder);
			
			itemCount++;
			
		} while (items.next());
		
		return itemCount;
	}
	
	private static void updateQueuedItem
	 (String processActionId, String errorMsg, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		binder.putLocal("completed", "1");
		binder.putLocal("processActionId", processActionId);
		binder.putLocal("errorMsg", errorMsg);
		binder.putLocal("completedDate", DateFormatter.getTimeStamp());
		
		facade.execute("qClientServices_UpdateProcessAction", binder);
	}
	
	public static boolean actionNextQueuedItem(FWFacade facade) 
	 throws DataException {
		
		DataResultSet rsNextItem = facade.createResultSet
		 ("qClientServices_GetNextQueuedProcessAction", new DataBinder());
		
		if (rsNextItem.isEmpty()) {
			Log.debug("No queued process actions remain.");
			return false;
		}
		
		String processActionId	= rsNextItem.getStringValueByName("PROCESS_ACTION_ID");
		
		String processId		= rsNextItem.getStringValueByName("PROCESS_ID");
		String action			= rsNextItem.getStringValueByName("ACTION");
		
		String errorMsg			= "";
		
		try {
			ClientProcess process 	= ClientProcess.get(Integer.parseInt(processId), facade);
			
			if (process == null)
				throw new DataException("Process not found");
			
			Campaign campaign		= Campaign.get(process.getCampaignId(), facade);
			
			if (campaign == null)
				throw new DataException("Campaign not found");
			
			CampaignHandler	cHandler = new CampaignHandler
			 (campaign, "Batch Actions Queue", facade);
			
			String note = null;
			
			ClientProcessHandler pHandler = cHandler.createClientProcessHandler();
			pHandler.applyAction(process, action, note);
			
		} catch (Exception e) {
			Log.error("Error actioning queued item: " + processActionId, e);
			errorMsg			= e.toString();
			
			if (errorMsg.length() > 200)
				errorMsg = errorMsg.substring(0, 200);
		}
		
		updateQueuedItem(processActionId, errorMsg, facade);
		return true;
	}
	
	public void setQueueMonitorEnabled() {
		boolean enable = !StringUtils.stringIsBlank(m_binder.getLocal("enable"));
		
		String sleepIntervalStr = m_binder.getLocal("sleepInterval");
		float sleepInterval		= ProcessActionQueueMonitor.DEFAULT_SLEEP_INTERVAL;
		
		if (!StringUtils.stringIsBlank(sleepIntervalStr))
			sleepInterval = Float.parseFloat(sleepIntervalStr);
			
		if (queueMonitor == null)
			queueMonitor = new ProcessActionQueueMonitor(sleepInterval);
		
		if (enable) {
			queueMonitor.setEnabled(true);
		
			if (!queueMonitor.isAlive()) {
				queueMonitor = new ProcessActionQueueMonitor(sleepInterval);
				queueMonitor.start();
			}
			
		} else {
			queueMonitor.setEnabled(false);
		}
	}
	
	public void getQueueMonitorStatus() {
		String status;
		
		if (queueMonitor == null)
			status = "Uninitialized";
		else {
			Date lastCheck = queueMonitor.getLastCheck();
			float sleepInterval = queueMonitor.getSleepInterval();
			
			m_binder.putLocal("sleepInterval", Float.toString(sleepInterval));
			
			if (lastCheck != null)
				m_binder.putLocal("lastCheckTime", 
				DateFormatter.getTimeStamp(lastCheck));
		
			if (queueMonitor.isAlive())
				status = "Running";
			else
				status = "Stopped";
		}
		
		m_binder.putLocal("queueMonitorStatus", status);
	}
	
	/** Adds process items fetched by the passed SQL statement. 
	 *  Rather dangerous as it executes custom SQL provided by the
	 *  user!
	 *  
	 * */
	public void addQueryItemsToQueue() throws DataException {
		String sqlQuery = m_binder.getLocal("sqlQuery");
		String action	= m_binder.getLocal("action");
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace);
		
		DataResultSet rsItems = facade.createResultSetSQL(sqlQuery);
		int itemCount = addItemsToQueue(rsItems, action, facade);
		
		m_binder.putLocal("itemCount", Integer.toString(itemCount));
	}
}
