package com.ecs.stellent.spp.service;

import idcbean.data.LWDataBinder;
import intradoc.common.ExecutionContext;
import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.Workspace;
import intradoc.server.Service;
import intradoc.shared.FilterImplementor;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.ecs.utils.Log;
import com.ecs.utils.stellent.LWFacade;

/** Used to cache aurora data every 5 minutes.
 *  
 *  Should be bound to the checkScheduledEvents filter (currently unused)
 *  
 *  DJ 14/11/09: I'm 99% sure this class is not used. Stopped mid development due to 
 *  requirement changes. 
 * 
 * @author Darren Johnson
 *
 */
public class AuroraCacheServices extends Service implements FilterImplementor{
	
	protected DataBinder m_binder;
	
	static boolean cacheComplete = false;
	
	private LWFacade facade = new LWFacade();
	
	//Filter than runs at midnight and refreshes the Aurora cache
	/*public int doFilter(Workspace arg0, DataBinder binder, ExecutionContext arg2) throws DataException, ServiceException {
		Log.info("CCLA SOW6: RUNNING AUTOMATED ACTIVITIES >>");
		
		doAuroraCacheEvent();
		
		Log.info("CCLA SOW6: FINISHED AUTOMATED ACTIVITIES <<");
		return 0;
	}*/
	public int doFilter(Workspace ws, DataBinder binder, ExecutionContext cxt) throws ServiceException{ 
	
	    m_binder = binder;

	    Log.debug("Cache 5 Minute Run...");

    	//Set to true to run every 5 minutes
    	boolean doCacheNow = Boolean.valueOf(
				m_binder.getEnvironmentValue("SPP_INT_AURORA_CACHING_TEST_MODE")).booleanValue();
    	
		GregorianCalendar newCal = new GregorianCalendar();
		int hour = newCal.get(Calendar.HOUR_OF_DAY);
		int minute = newCal.get(Calendar.MINUTE);

		String[] timeToSend = m_binder.getEnvironmentValue("SPP_INT_AURORA_CACHING_TIMETORUN").split(":");
		int hourToSend = Integer.parseInt(timeToSend[0]);
		int minuteToSend = Integer.parseInt(timeToSend[1]);
		
		Log.debug("Time to cache: " + hourToSend + ":" + minuteToSend);
		Log.debug("Time now: " + hour + ":" + minute);
		
		//If the time is to send it now (or later) and no cache has occured yet, do the cache. Otherwise, 
		//if its after the time and the cache has been done, reset the boolean.
		if((hour == hourToSend && minute >= minuteToSend) && !cacheComplete){
			Log.debug("It's time to cache");
			cacheComplete = true;	
			doCacheNow = true;
		}else if(hour > hourToSend && cacheComplete){
			cacheComplete = false;
		}
		
    	if (doCacheNow) {		    
			Log.info("CCLA SOW6: RUNNING AUTOMATED ACTIVITIES >>");
			doAuroraCacheEvent();
			Log.info("CCLA SOW6: FINISHED AUTOMATED ACTIVITIES <<");
	    }
	    
	    return 0;
	}
	
	private void doAuroraCacheEvent() throws ServiceException {
		try{
			boolean enableCaching = Boolean.valueOf(
					m_binder.getEnvironmentValue("SPP_INT_AURORA_CACHING_ENABLED")).booleanValue();
			
			if(enableCaching){
				refreshAuroraCache();
			}else{
				Log.info("CCLA SOW6: Aurora Caching not enabled...");
			}
			
		}catch (Exception e) {
			Log.error("UNABLE TO UPDATE AURORA CACHE: " +e.getMessage());
			throw new ServiceException("UNABLE TO UPDATE AURORA CACHE: " +e.getMessage());
		}	
	}

	public void refreshAuroraCache() throws Exception{
		//Get aurora cache
		
		//If cache:
		Log.info("CCLA SOW6: Refreshing Aurora Cache...");
		
		//Delete Aurora cache 
		LWDataBinder qDeleteCache = new LWDataBinder();
		qDeleteCache.putLocal("IdcService", "SPP_INT_Q_DELETE_AURORA_CACHE");
		
		facade.executeService(qDeleteCache);
		
		LWDataBinder qInsertData = new LWDataBinder();
		qInsertData.putLocal("IdcService", "SPP_INT_Q_INSERT_AURORA_CACHE");
		
		for(int i=0; i<5; i++){
			qInsertData.putLocal("client", "Google");
			qInsertData.putLocal("account", "G"+(i+1));
			facade.executeService(qInsertData);
		}
		//End if
	}
	
}
