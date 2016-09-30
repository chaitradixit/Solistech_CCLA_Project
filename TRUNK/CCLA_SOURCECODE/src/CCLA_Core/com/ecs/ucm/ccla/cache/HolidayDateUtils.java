package com.ecs.ucm.ccla.cache;

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.utils.DateUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

public class HolidayDateUtils {

	/** Rebuilt when the internal cache is rebuilt.
	 * 
	 *  Stores all public holiday dates for the current year.
	 */
	private static Vector<Date> ANNUAL_HOLIDAY_DATES = null;
	
	/**
	 * 
	 * @param dateToCompare
	 * @return
	 */
	public static boolean isHoliday(Date dateToCompare) 
	{
		boolean isHoliday = false;
		
		if (ANNUAL_HOLIDAY_DATES != null) {
			for (Date holiday: ANNUAL_HOLIDAY_DATES) {
				if (DateUtils.compareDates(holiday, dateToCompare)==0) {
					isHoliday=true;
					break;
				}
			}
		}
		return isHoliday;
	}
	
	/*
	 *  Caching stuff. Not formally used yet.
	 */
	private static Cache CACHE = new Cache();
	
	public static Cachable<Integer, Vector<Date>> getCache() {
		return CACHE;
	}
	
	/** Year -> Holidays Dates */
	private static class Cache extends Cachable<Integer, Vector<Date>> {

		public Cache() {
			super("Holiday Date");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {

			HashMap<Integer, Vector<Date>> newCache = 
			 new HashMap<Integer, Vector<Date>>();
			
			//only cache this years holidays
			int year = DateUtils.getYear(DateUtils.getNow());
			
			//StartDate 1/1
			Date startDate = DateUtils.getDate(year, 1, 1);
			//EndDate 31/12
			Date endDate = DateUtils.getDate(year, 12, 31);

			DataBinder binder = new DataBinder();
			binder.putLocalDate("startDate", startDate);
			binder.putLocalDate("endDate", endDate);
			
			DataResultSet results = facade.createResultSet("qGetPublicHoliday", binder);
			
			ANNUAL_HOLIDAY_DATES = new Vector<Date>();
			
			if (results!=null && !results.isEmpty()) {
				do {
					Date holiday = results.getDateValueByName("HOLIDAY_DATE");
					ANNUAL_HOLIDAY_DATES.add(holiday);
				} while (results.next());
			}
			
			newCache.put(year, ANNUAL_HOLIDAY_DATES);
			
			this.CACHE_MAP = newCache;
		}
	}

}
