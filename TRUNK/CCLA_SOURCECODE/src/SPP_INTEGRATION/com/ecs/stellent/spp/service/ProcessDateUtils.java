package com.ecs.stellent.spp.service;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.shared.SharedObjects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.ecs.ucm.ccla.data.DocumentClass;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.DateFormatter;
import com.ecs.utils.stellent.embedded.FWFacade;

public @Deprecated class ProcessDateUtils {

	/* Core Process Date names, should be a mirror of names used in the
	 * CCLA_PROCESS_DATES table.
	 * 
	 */
	public static final String ROLLOVER_TIME_NAME		= "RolloverTime";
	public static final String UNITISED_TIME_NAME		= "UnitisedTime";
	public static final String PSDF_TIME_NAME			= "PSDFTime";
	public static final String DEPOSIT_TIME_NAME		= "DepositTime";
	public static final String ROLLBACK_TIME_NAME		= "RollbackTime";	
	public static final String ROLLBACK_DATE_NAME		= "RollbackDate";


	protected static List<String> sppPsdfCodesList = null;
	protected static List<String> sppDepositList = null;
	protected static List<String> sppUnitisedList = null;
	
	/** Indicates whether a particular Process Date is scoped to a particular
	 *  date or time of day.
	 *  
	 * @author Tom
	 *
	 */
	public static enum DateType {
		Date,
		Time
	}
	
	/** Determines when the base process date/roll-over time was 
	 *  last updated. */
	public static Date lastUpdated = null; 
	
	/** used to cache the roll-over time. */
	public static Date rolloverTime = null;
	/** used to cache the psdf time. */
	public static Date psdfTime = null;
	/** used to cache the unitised time. */
	public static Date unitisedTime = null;
	/** used to cache the deposit time. */
	public static Date depositTime = null;
	/** used to cache the rollbackTime */
	public static Date rollbackTime = null;
	/** used to cache the rollbackDate */
	public static Date rollbackDate = null;
	
	/** used to cache the overrideTime */
	public static Date overrideTime = null;
	/** used to cache the overrideDate */
	public static Date overrideDate = null;
	
	
	/** Base processing date used for items that arrive before the roll-over
	 *  time has passed.
	 */
	public static Date preRolloverDate = null;
	
	/** Base processing date used for items that arrive after the roll-over
	 *  time has passed.
	 */
	public static Date postRolloverDate = null;
	
	/** Number of milliseconds that the process date cache is valid for. */
	public static long EXPIRY_TIME_MILLIS = 
	 (long)SharedObjects.getEnvironmentInt("SPP_ProcessDateCacheExpiry", 60)
	 * 1000 * 60;
	
	/** Check if the cache is not initialized, expired, or the rollover
	 * time has exceeded. If any condition is true, refresh the date 
	 * cache. 
	 *
	 **/
	public @Deprecated static synchronized void checkProcessDateCache(FWFacade facade) 
	 throws DataException {
		
		long currentTime = System.currentTimeMillis();
		
		if (!isCacheInitialized() ||
			(currentTime - lastUpdated.getTime() < EXPIRY_TIME_MILLIS)) {
			
			refreshProcessDateCache(facade);
		}
	}
	
	/** Recalculates the rollover time and pre-/post-rollover process dates.
	 *  
	 * @param facade
	 * @throws DataException
	 */
	public @Deprecated static void refreshProcessDateCache(FWFacade facade) 
	 throws DataException {
		
		Log.debug("Refreshing Process Date cache...");
		

		DataBinder binder = new DataBinder();
		DataResultSet rsProcessDates = 
		 facade.createResultSet("qProcessDates", binder);
		
		// Fetch the roll-over time from the database.
		if (!rsProcessDates.isEmpty()) {
			do {
				String thisName = rsProcessDates.getStringValueByName("NAME");
				
				if (thisName.equals(ROLLOVER_TIME_NAME)) {
					rolloverTime = rsProcessDates.getDateValueByName("PROCESS_DATE");
				} else if (thisName.equals(UNITISED_TIME_NAME)) {
					unitisedTime = rsProcessDates.getDateValueByName("PROCESS_DATE");
				} else if (thisName.equals(PSDF_TIME_NAME)) {
					psdfTime = rsProcessDates.getDateValueByName("PROCESS_DATE");
				} else if (thisName.equals(DEPOSIT_TIME_NAME)) {
					depositTime = rsProcessDates.getDateValueByName("PROCESS_DATE");					
				} else if (thisName.equals(ROLLBACK_TIME_NAME)) {
					rollbackTime = rsProcessDates.getDateValueByName("PROCESS_DATE");					
				} else if (thisName.equals(ROLLBACK_DATE_NAME)) {
					rollbackDate = rsProcessDates.getDateValueByName("PROCESS_DATE");					
				} 	
			} while (rsProcessDates.next());
		}
		
		if (rolloverTime == null || unitisedTime == null 
				|| psdfTime == null || depositTime == null) {
			
			if (rolloverTime==null) {
				throw new DataException("Unable to calculate Process Date: " +
				"roll-over time entry missing");
			} else if (unitisedTime==null) {
				throw new DataException("Unable to calculate Process Date: " +
				"unitised time entry missing");
			} else if (depositTime==null) {
				throw new DataException("Unable to calculate Process Date: " +
				"deposit time entry missing");
			} else if (psdfTime==null) {
				throw new DataException("Unable to calculate Process Date: " +
				"psdf time entry missing");
			}
		} else {
			// First calculate the times.
			String sppPsdfCodes = SharedObjects.getEnvironmentValue("SPP_PSDF_CODE");
			String sppDepositCodes = SharedObjects.getEnvironmentValue("SPP_DEPOSIT_CODES");
			String sppUnitisedCodes = SharedObjects.getEnvironmentValue("SPP_UNITISED_CODES");
			
			if (!StringUtils.stringIsBlank(sppPsdfCodes)) {
				String[] psdfCodes = sppPsdfCodes.split(",");
				sppPsdfCodesList = Arrays.asList(psdfCodes);
			} else 
				sppPsdfCodesList = new ArrayList<String>();
			
			if (!StringUtils.stringIsBlank(sppDepositCodes)) {
				String[] depositCodes = sppDepositCodes.split(",");
				sppDepositList = Arrays.asList(depositCodes);
			} else 
				sppDepositList = new ArrayList<String>();
			
			if (!StringUtils.stringIsBlank(sppUnitisedCodes)) {
				String[] unitisedCodes = sppUnitisedCodes.split(",");
				sppUnitisedList = Arrays.asList(unitisedCodes);
			} else 
				sppUnitisedList = new ArrayList<String>();
			
			
			rolloverTime 		= getTodaysDateWithSetTime(rolloverTime);
			psdfTime			= getTodaysDateWithSetTime(psdfTime);
			depositTime			= getTodaysDateWithSetTime(depositTime);
			unitisedTime		= getTodaysDateWithSetTime(unitisedTime);
			
			if (rollbackTime!=null)
				rollbackTime = getTodaysDateWithSetTime(rollbackTime);
			
			
			// Create a date instance set to midnight of the current day.
			GregorianCalendar currentDate = new GregorianCalendar();;
			
			if (rollbackDate!=null) {
				currentDate.setTime(rollbackDate);
			} 

			currentDate.set(Calendar.HOUR_OF_DAY, 0);
			currentDate.set(Calendar.MINUTE, 0);
			currentDate.set(Calendar.SECOND, 0);
			currentDate.set(Calendar.MILLISECOND, 0);
			
			
			preRolloverDate = getWorkingDay(currentDate.getTime());
			
			currentDate.add(Calendar.DAY_OF_YEAR, 1);
			postRolloverDate = getWorkingDay(currentDate.getTime());
			
		}
		
		
		
		Log.debug("Process date cache refreshed.");
		
		Log.debug("Rollover Time: " + 
		 DateFormatter.getTimeStamp(rolloverTime));
		Log.debug("PSDF Time: " + 
				 DateFormatter.getTimeStamp(psdfTime));
		Log.debug("Unitised Time: " + 
				 DateFormatter.getTimeStamp(unitisedTime));
		Log.debug("Deposit Time: " + 
				 DateFormatter.getTimeStamp(depositTime));
								
		Log.debug("Pre-Rollover Date: " + 
		 DateFormatter.getTimeStamp(preRolloverDate));
		Log.debug("Post-Rollover Date: " + 
		 DateFormatter.getTimeStamp(postRolloverDate));
		
		lastUpdated = new Date();
	}
	

	public @Deprecated static Date getWorkingDay(Date baseDate) throws DataException {
		
		GregorianCalendar newProcessDate = new GregorianCalendar();
		newProcessDate.setTime(baseDate);
		
		// Determine the next available weekday.
		while ((newProcessDate.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
			   ||
			   (newProcessDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)) {
			newProcessDate.add(Calendar.DAY_OF_YEAR, 1);
		}
		
		// TODO: Account for holidays
		
		// Ensure new process date is a discrete date set at midnight.
		newProcessDate.set(Calendar.HOUR_OF_DAY, 0);
		newProcessDate.set(Calendar.MINUTE, 0);
		newProcessDate.set(Calendar.SECOND, 0);
		newProcessDate.set(Calendar.MILLISECOND, 0);
		
		Date workingDate = newProcessDate.getTime();
		return workingDate;
	}	
	
	/**
	 * 
	 * @param time
	 * @return
	 */
	public @Deprecated static Date getTodaysDateWithSetTime(Date time) 
	{
		// Construct a Calendar from the passed date. This is used to
		// extract the required hour/minute values.
		GregorianCalendar calTime = new GregorianCalendar();
		calTime.setTime(time);
		
		GregorianCalendar today = new GregorianCalendar();
		
		// Copy over time fields
		today.set(Calendar.HOUR_OF_DAY, calTime.get(Calendar.HOUR_OF_DAY));
		today.set(Calendar.MINUTE, calTime.get(Calendar.MINUTE));
		today.set(Calendar.SECOND, calTime.get(Calendar.SECOND));
		today.set(Calendar.MILLISECOND, calTime.get(Calendar.MILLISECOND));
		
		return today.getTime();
	}
	
	/** Fetches the next dealing date for a given Fund. If the passed Fund
	 *  Code is null/empty, the base process date is passed instead.
	 *  
	 *  The passed release date is compared against the base processing
	 *  date
	 * 
	 * @param fundCode
	 * @param facade
	 * @return
	 * @throws ServiceException 
	 * @throws DataException 
	 */
	public @Deprecated static Date getNextDealingDate
	 (String fundCode, Date releaseDate, FWFacade facade) 
	 throws ServiceException, DataException {
		
		checkProcessDateCache(facade);
		
		Date baseProcessDate = null;
		
		if (rollbackTime!=null)
			releaseDate = rollbackTime;
			
		if (releaseDate.getTime() < getProcessingTime(fundCode)) {
			baseProcessDate = preRolloverDate;
		} else {
			baseProcessDate = postRolloverDate;
		}
		
		if (StringUtils.stringIsBlank(fundCode))
			return baseProcessDate;
		
		DataBinder binder	= new DataBinder();
		binder.putLocal("baseProcessDate", 
		 DateFormatter.getTimeStamp(baseProcessDate));
		binder.putLocal("fundCode", fundCode);
		
		// Execute a query which will return a single-row, single-column
		// ResultSet, containing the next dealing date for the given fund.
		DataResultSet rsNextDealingDate = 
		 facade.createResultSet("qGetNextDealingDate", binder);
		
		if (rsNextDealingDate.isEmpty()) {
			Log.warn("No next dealing date found for fund: " + fundCode);
			return null;
		} else {
			return rsNextDealingDate.getDateValueByName("DEALING_DATE");
		}
	}
	
	/** Determines the dealing date for the passed Content Item. The ResultSet
	 *  is expected to contain a join of columns from the Revisions and DocMeta
	 *  tables.
	 *  
	 *  If the Content Item has a non-null xProcessDate value, the method 
	 *  returns null to indicate it has already been set, and shouldn't be
	 *  touched.
	 *  
	 *  Otherwise, the Process Date is calculated based on the item's xFund
	 *  value (if applicable) and its release date. If the document has a
	 *  non-transaction Document Class, the base processing date is returned
	 *  and the fund code won't be considered.
	 *  
	 * @param rsContentItem
	 * @param facade
	 * @return
	 * @throws ServiceException
	 * @throws DataException
	 */
	public @Deprecated static Date getDealingDate(DataResultSet rsContentItem, 
	 FWFacade facade) throws ServiceException, DataException {

		String processDate 	= rsContentItem.getStringValueByName
		 ("xProcessDate");
		
		if (StringUtils.stringIsBlank(processDate)) {
			Date releaseDate = 
			 rsContentItem.getDateValueByName("dInDate");
			
			String docClass	= rsContentItem.getStringValueByName
			 ("xDocumentClass");
			
			DocumentClass documentClass = null;
			
			if (!StringUtils.stringIsBlank(docClass))
				documentClass = DocumentClass.getCache().getCachedInstance(docClass);
			
			
			Date dealingDate = null;
			
			if (documentClass!=null && documentClass.isSubmitToSpp()) {
				// Document Class is not a transaction type, just use the base
				// process date.
				
				dealingDate = ProcessDateUtils.getNextDealingDate
				 (null, releaseDate, facade);
				
			} else {
				// Document Class is a transaction type, use the Fund value to
				// calculate the process date where applicable.
				
				String fundCode	= rsContentItem.getStringValueByName
				 ("xFund");
			
				dealingDate = ProcessDateUtils.getNextDealingDate
				 (fundCode, releaseDate, facade);
			}
			
			return dealingDate;
			
		} else
			return null; // xProcessDate already set on this item
	}
	
	public @Deprecated static boolean isCacheInitialized() {
		return (lastUpdated != null);
	}

	
	
	private @Deprecated static long getProcessingTime(String fundCode) {		
		if (!StringUtils.stringIsBlank(fundCode)) {
			if (sppDepositList.contains(fundCode)) {
				return depositTime.getTime();
			} else if (sppPsdfCodesList.contains(fundCode)) {
				return psdfTime.getTime();
			} else if (sppUnitisedList.contains(fundCode)){
				return unitisedTime.getTime();
			} else {
				return rolloverTime.getTime();				
			}
		} 		
		//default return rolloverTime
		return rolloverTime.getTime();
	}
	

	
}
