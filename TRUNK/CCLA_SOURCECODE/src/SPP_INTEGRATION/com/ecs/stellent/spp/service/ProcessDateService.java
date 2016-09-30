package com.ecs.stellent.spp.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.DateFormatter;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.server.Service;

/** Handler methods for the Process Date field.
 *  
 * @author Tom
 *
 */
public @Deprecated class ProcessDateService extends Service {
	
	/** Service method used for updating a given Process Date. 
	 * 
	 * @throws DataException 
	 * @throws ServiceException */
	public @Deprecated void updateProcessDate() 
	 throws DataException, ServiceException {
		
		// Process Date name
		String name = m_binder.getLocal("name");
		// Updated date/time value
		String processDate = m_binder.getLocal("processDate");
		
		if (StringUtils.stringIsBlank(name) || StringUtils.stringIsBlank(processDate)) {
			throw new ServiceException("Unable to update process date: " +
			 "date name/value missing");
		}
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace,false);
	
		Log.debug("Fetching process date entry: " + name);
		
		DataResultSet rsProcessDates = 
		 facade.createResultSet("qProcessDates", m_binder);
		
		// Determine the type of the process date. This is required to see how
		// to parse the date value supplied by the user
		String processDateType = null;
		
		if (!rsProcessDates.isEmpty()) {
			do {
				String thisName = rsProcessDates.getStringValueByName("NAME");
				
				if (name.equals(thisName)) {	
					processDateType = rsProcessDates.getStringValueByName("DATE_TYPE");
					break;
				}
				
			} while (rsProcessDates.next());
		}
		
		if (processDateType == null) {
			String msg = "Unable to find process date entry with name: " + name;
			
			Log.error(msg);
			throw new ServiceException(msg);
		}

		try {
			Date newDate = null;

			if (processDateType.equals(ProcessDateUtils.DateType.Date.toString())) {
				// Expect a date in format dd/mm/yyyy
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				
				newDate = dateFormat.parse(processDate);
			} else {
				// Expect a time in format HH:mm
				SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
				
				newDate = dateFormat.parse(processDate);
				
				// Use a calendar instance to normalize the date portion to
				// Jan 1st 2000.
				GregorianCalendar cal = new GregorianCalendar();
				cal.setTime(newDate);
				cal.set(2000, 0, 1);
				
				newDate = cal.getTime();
			}
			DataBinder binder = new DataBinder();
			binder.putLocal("name", name);
			binder.putLocal("processDate", DateFormatter.getTimeStamp(newDate));
			
			facade.execute("qUpdateProcessDate", binder);
			
			Log.debug("Updated process date '" + name + "' to " + 
			 DateFormatter.getTimeStamp(newDate));
			
		} catch (ParseException e) {
			Log.error("Failed to parse date " + processDate, e);
			throw new ServiceException("Incorrectly formatted date/time: " + 
			 processDate, e);
		}
		
		// Refresh the cache after changing any date.
		ProcessDateUtils.refreshProcessDateCache(facade);
	}
	
	
	/** 
	 * Service method used for clearing the process date. 
	 * 
	 * @throws DataException 
	 * @throws ServiceException */
	public @Deprecated void clearProcessDate() throws DataException, ServiceException 
	{
		// Process Date name
		String name = m_binder.getLocal("name");
		
		if (StringUtils.stringIsBlank(name)) {
			throw new ServiceException("Unable to clear process date: date name");
		}
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace,false);
		
		DataBinder binder = new DataBinder();
		binder.putLocal("name", name);
			
		facade.execute("qClearProcessDate", binder);
			
		Log.debug("cleared process date for '" + name + "'");
		
		// Refresh the cache after changing any date.
		ProcessDateUtils.refreshProcessDateCache(facade);
	}
	
	/** 
	 * Service method used for updating all Process Date Times.
	 * The DataType 'Time' will update all. 
	 * 
	 * @throws DataException 
	 * @throws ServiceException */
	public @Deprecated void updateAllProcessDateTimes() 
	 throws DataException, ServiceException {

		// Updated date/time value
		String processDate = m_binder.getLocal("processDate");
		
		if (StringUtils.stringIsBlank(processDate)) {
			throw new ServiceException("Unable to update process date: " +
			 "date value missing");
		}
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace,false);
		
		try {
			Date newDate = null;
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
				
			newDate = dateFormat.parse(processDate);
			
			// Use a calendar instance to normalize the date portion to
			// Jan 1st 2000.
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(newDate);
			cal.set(2000, 0, 1);			
			newDate = cal.getTime();
			
			DataBinder binder = new DataBinder();
			binder.putLocal("processDate", DateFormatter.getTimeStamp(newDate));
			
			facade.execute("qUpdateAllProcessDateTimes", binder);
			
			Log.debug("Updated all process date times to " + 
			 DateFormatter.getTimeStamp(newDate));
			
		} catch (ParseException e) {
			Log.error("Failed to parse date " + processDate, e);
			throw new ServiceException("Incorrectly formatted date/time: " + 
			 processDate, e);
		}
		
		// Refresh the cache after changing any date.
		ProcessDateUtils.refreshProcessDateCache(facade);
	}	
	
	
	/** Service-accessible method for fetching the details of the Process date
	 *  cache and adding them to the DataBinder. Used on the Process Date
	 *  Administration screen.
	 * @throws DataException 
	 * @throws ServiceException 
	 *  
	 */
	public @Deprecated void getProcessDateCacheDetails() 
	 throws DataException, ServiceException {
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace,false);
		ProcessDateUtils.checkProcessDateCache(facade);
		
		if (ProcessDateUtils.isCacheInitialized()) {
			m_binder.putLocal("PreRolloverDate", 
                DateFormatter.getTimeStamp(ProcessDateUtils.preRolloverDate));
			
			m_binder.putLocal("PostRolloverDate",
				DateFormatter.getTimeStamp(ProcessDateUtils.postRolloverDate)); 
			
			m_binder.putLocal("CacheLastUpdated",
				DateFormatter.getTimeStamp(ProcessDateUtils.lastUpdated));
			
			m_binder.putLocal(ProcessDateUtils.ROLLOVER_TIME_NAME, 
				DateFormatter.getTimeStamp(ProcessDateUtils.rolloverTime));
			
			m_binder.putLocal(ProcessDateUtils.PSDF_TIME_NAME,
				DateFormatter.getTimeStamp(ProcessDateUtils.psdfTime));
			
			m_binder.putLocal(ProcessDateUtils.DEPOSIT_TIME_NAME, 
				DateFormatter.getTimeStamp(ProcessDateUtils.depositTime));

			m_binder.putLocal(ProcessDateUtils.UNITISED_TIME_NAME, 
				DateFormatter.getTimeStamp(ProcessDateUtils.unitisedTime));
			
			
			// Compute the base process date, relative to the current time.
			if (ProcessDateUtils.sppPsdfCodesList.size()>0) {
				Date basePSDFProcessDate = ProcessDateUtils.getNextDealingDate
				(ProcessDateUtils.sppPsdfCodesList.get(0), new Date(), facade);
			
				m_binder.putLocal("basePsdfProcessDate", 
						DateFormatter.getTimeStamp(basePSDFProcessDate));
			}
			// Compute the base deposit process date, relative to the current time.
			if (ProcessDateUtils.sppDepositList.size()>0) {
				Date baseDepositProcessDate = ProcessDateUtils.getNextDealingDate
				(ProcessDateUtils.sppDepositList.get(0), new Date(), facade);
				
				m_binder.putLocal("baseDepositProcessDate", 
					 DateFormatter.getTimeStamp(baseDepositProcessDate));
			}
		
			
			if (ProcessDateUtils.sppUnitisedList.size()>0) {
				// Compute the base unitised date, relative to the current time.
				Date baseUnitisedProcessDate = ProcessDateUtils.getNextDealingDate
				 (ProcessDateUtils.sppUnitisedList.get(0), new Date(), facade);
				
				m_binder.putLocal("baseUnitisedProcessDate", 
						 DateFormatter.getTimeStamp(baseUnitisedProcessDate));
			}
			
			// Compute the base process date, relative to the current time.
			Date baseProcessDate = ProcessDateUtils.getNextDealingDate
			 (null, new Date(), facade);
			
			
			m_binder.putLocal("baseProcessDate", 
			 DateFormatter.getTimeStamp(baseProcessDate));
			
			/*
			if (currentTime.getTime() < ProcessDateUtils.rolloverTime.getTime()) {
				m_binder.putLocal("baseProcessDate", 
				 DateFormatter.getTimeStamp(ProcessDateUtils.preRolloverDate));
			} else {
				m_binder.putLocal("baseProcessDate", 
				 DateFormatter.getTimeStamp(ProcessDateUtils.postRolloverDate));
			}
			*/
			
		} else {
			m_binder.putLocal("CacheNotInitialized", "1");
			m_binder.putLocal("baseProcessDate", DateFormatter.getTimeStamp());
		}	
	}
	
	/** Forces a refresh of the process date cache.
	 * 
	 */
	public @Deprecated void refreshCache() throws DataException {
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace,false);
		
		ProcessDateUtils.lastUpdated = null;
		ProcessDateUtils.checkProcessDateCache(facade);
		
		
		
	}
	
	/** Service-accessible method for fetching the next dealing date for a
	 *  given Fund. Uses the current system date/time when comparing against 
	 *  the roll-over time.
	 *  
	 * @throws DataException
	 * @throws ServiceException
	 */
	public @Deprecated void getNextDealingDate() throws DataException, ServiceException {
		FWFacade facade 	= CCLAUtils.getFacade(m_workspace,false);
		String fundCode		= m_binder.getLocal("fundCode");
		
		Date dealingDate 	= ProcessDateUtils.getNextDealingDate
		 (fundCode, new Date(), facade);
		
		m_binder.putLocalDate("nextDealingDate", dealingDate);
	}
	

}