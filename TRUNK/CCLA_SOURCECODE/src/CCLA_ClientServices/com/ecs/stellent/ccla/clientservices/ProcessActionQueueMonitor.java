package com.ecs.stellent.ccla.clientservices;

import java.util.Date;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.embedded.FWFacade;

public class ProcessActionQueueMonitor extends Thread {
	
	static float DEFAULT_SLEEP_INTERVAL = 30;
	
	float sleepInterval 	= DEFAULT_SLEEP_INTERVAL;
	boolean enabled   		= true;
	Date lastCheck			= null;
	
	public ProcessActionQueueMonitor(float sleepInterval) {
		super();
		
		this.setDaemon(true);
		this.sleepInterval = sleepInterval;
	}
	
	public void run() {
		super.run();
		
		Log.debug("Starting Process Action Queue monitor thread");
		
		int sleepTime = 1000;
		
		if (sleepInterval < 1)
			sleepTime = Math.round(sleepInterval * 1000);
		
		Log.debug("Resolved sleeping time as " + sleepTime + "ms");
		
		while (enabled) {
			int intervalTicks = 0;
			
			while (enabled && (intervalTicks < sleepInterval)) {
				try {
					intervalTicks++;
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {}
			}
			
			if (enabled) {
				try {
					lastCheck = new Date();
					
					//FWFacade facade = new FWFacade("SystemDatabase");
					FWFacade facade = CCLAUtils.getFacade(true);
					boolean actioned = ProcessActionService.actionNextQueuedItem(facade);
					
					facade.releaseConnection();
					
					if (!actioned)
						enabled = false;
					
				} catch (Exception e) {
					Log.error("Process action queue monitor failed: " + e.toString());
					enabled = false;
				}
			}
		}
		
		Log.debug("Process Action Queue monitor thread was stopped");
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public Date getLastCheck() {
		return lastCheck;
	}
	
	public float getSleepInterval() {
		return sleepInterval;
	}
}
