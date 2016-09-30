package com.ecs.ucm.ccla.aurora.compare;

import intradoc.data.DataException;

import java.util.Calendar;
import java.util.Date;

import com.ecs.ucm.ccla.data.SystemConfigVar;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.DateFormatter;

public class ComparisonUtils {

	/** Calculates the Static Data Update limit date from the passed base date.
	 *  
	 *  Returns a truncated version of the date which is equivalent to the base date,
	 *  subtracted by the number stored in the config var 'SDU_AmendmentLimitDays'
	 * 
	 *  Should be used when determining which Field Groups have been updated since
	 *  a Static Data Update source instruction (e.g. MAND) was indexed/created.
	 * 
	 * @param baseDate
	 * @return
	 * @throws DataException 
	 */
	public static Date getAmendmentLimitDate(Date baseDate) throws DataException {
		int numDays = 7;
		
		SystemConfigVar daysVar = SystemConfigVar.getCache().getCachedInstance
		 ("SDU_AmendmentLimitDays");
		
		if (daysVar == null)
			Log.warn("Unable to find config var SDU_AmendmentLimitDays. " +
			 "Using default amendment limit days of " + numDays);
		else
			numDays = daysVar.getIntValue();
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(baseDate);
		
		// Subtract no. of days from base date.
		cal.add(Calendar.DAY_OF_YEAR, -numDays);
		
		// Truncate to midnight.
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		
		Log.debug("Computed amendment limit date of " + 
		 DateFormatter.getTimeStamp(cal.getTime()) + 
		 " from base date: " +  DateFormatter.getTimeStamp(baseDate));
		
		return cal.getTime();
	}
}
