package com.ecs.ucm.ccla.utils;

import intradoc.data.DataException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.ecs.ucm.ccla.cache.HolidayDateUtils;


public class DateUtils {

	private static ThreadLocal<Calendar> calendars = new ThreadLocal<Calendar>();
	
	/**
	 * Gets the current date
	 * @return Date
	 */
	public static Date getNow() {
		return new Date();
	}

	/**
	 * Gets a date that we deem as beginning of time.
	 * @return Date
	 */
	public static Date beginningOfTime() {
		return getDate(1970, 1, 1);
	}

	/**
	 * Create a date base on the parameters
	 * @param year
	 * @param month
	 * @param day
	 * @return Date
	 */
	public static Date getDate(int year, int month, int day) {
		return getDate(year, month, day, 0, 0, 0);
	}

	/**
	 * Create a date based on the parameters
	 * @param year
	 * @param month
	 * @param day
	 * @param hour
	 * @param minute
	 * @param seconds
	 * @return Date
	 */
	public static Date getDate(int year, int month, int day, int hour, int minute, int seconds) {
		Calendar calendar = (Calendar) calendars.get();
		if (calendar == null) {
			calendar = Calendar.getInstance();
			calendars.set(calendar);
		}
		calendar.set(year, month-1, day, hour, minute, seconds);
		calendar.clear(Calendar.MILLISECOND);
		return calendar.getTime();
	
	}	
	
	/**
	 * Rounds a given date to the beginning of the day
	 * @param date
	 * @return Date
	 */
	public static Date roundToBeginningOfDay(Date date) {
		Calendar calendar = (Calendar) calendars.get();
		if (calendar == null) {
			calendar = Calendar.getInstance();
			calendars.set(calendar);
		}
		
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(Calendar.DAY_OF_MONTH, 0);
		return calendar.getTime();
	}	
	
	/**
	 * Rounds a given date to the end of the day
	 * @param date
	 * @return Date
	 */
	public static Date roundToEndOfDay(Date date) {
		Calendar calendar = (Calendar) calendars.get();
		if (calendar == null) {
			calendar = Calendar.getInstance();
			calendars.set(calendar);
		}
		
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		calendar.add(Calendar.DAY_OF_MONTH, 0);
		return calendar.getTime();
	}	
	
	/**
	 * Parse a string HH:mm and returns a date obj
	 * @param string
	 * @return Date
	 */
	public static Date parseHHcmm(String s) {
		try {
			SimpleDateFormat simpledateformat = new SimpleDateFormat();
			simpledateformat.setLenient(false);
			simpledateformat.applyPattern("HH:mm");
			return simpledateformat.parse(s);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Formats a date to HH:mm String format 
	 * @param date
	 * @return formatted string in the format HH:mm
	 */
	public static String formatHHcmm(Date date) {
		SimpleDateFormat simpledateformat = new SimpleDateFormat();
		simpledateformat.applyPattern("HH:mm");
		return simpledateformat.format(date);
	}	

	
	/**
	 * Parse a string dd/MM/yyyy and returns a date obj
	 * @param string
	 * @return Date
	 */
	public static Date parseddsMMsyyyy(String s) {
		try {
			SimpleDateFormat simpledateformat = new SimpleDateFormat();
			simpledateformat.setLenient(false);
			simpledateformat.applyPattern("dd/MM/yyyy");
			return simpledateformat.parse(s);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Formats a date to dd/MM/yyyy String format 
	 * @param date
	 * @return formatted string in the format dd/MM/yyyy
	 */
	public static String formatddsMMsyyyy(Date date) {
		
		if (date==null)
			return null;
		
		SimpleDateFormat simpledateformat = new SimpleDateFormat();
		simpledateformat.applyPattern("dd/MM/yyyy");
		return simpledateformat.format(date);
	}	

	/**
	 * Formats a date to yyyy-MM String format 
	 * @param date
	 * @return formatted string in the format yyyy-MM
	 */
	public static String formatyyyyMM(Date date) {
		
		if (date==null)
			return null;
		
		SimpleDateFormat simpledateformat = new SimpleDateFormat();
		simpledateformat.applyPattern("yyyy-MM");
		return simpledateformat.format(date);
	}	
	
	
	/**
	 * Formats a date to dd/MM/yy String format 
	 * @param date
	 * @return formatted string in the format dd/MM/yy
	 */
	public static String formatddsMMsyy(Date date) {
		
		if (date==null)
			return null;
		
		SimpleDateFormat simpledateformat = new SimpleDateFormat();
		simpledateformat.applyPattern("dd/MM/yy");
		return simpledateformat.format(date);
	}	
	
	/**
	 * Parse a string 'dd/MM/yyyy HH:mm' and returns a date obj
	 * @param string
	 * @return Date
	 */
	public static Date parseddsMMsyyyyspHHcmm(String s) {
		try {
			SimpleDateFormat simpledateformat = new SimpleDateFormat();
			simpledateformat.setLenient(false);
			simpledateformat.applyPattern("dd/MM/yyyy HH:mm");
			return simpledateformat.parse(s);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Formats a date to dd/MM/yyyy HH:mm String format 
	 * @param date
	 * @return formatted string in the format HH:mm
	 */
	public static String formatddsMMsyyyyspHHcmm(Date date) {
		
		if (date==null)
			return null;
		
		SimpleDateFormat simpledateformat = new SimpleDateFormat();
		simpledateformat.applyPattern("dd/MM/yyyy HH:mm");
		return simpledateformat.format(date);
	}	

	/**
	 * Add int number of days to date object 
	 * @param date
	 * @param i no. of days
	 * @return
	 */
	public static Date addDays(Date date, int i) {
		Calendar calendar = (Calendar) calendars.get();
		if (calendar == null) {
			calendar = Calendar.getInstance();
			calendars.set(calendar);
		}
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, i);
		return calendar.getTime();
	}
	
	/**
	 * Add int hours to current date object
	 * @param date
	 * @param i
	 * @return
	 */
	public static Date addHours(Date date, int i) {
		Calendar calendar = (Calendar) calendars.get();
		if (calendar == null) {
			calendar = Calendar.getInstance();
			calendars.set(calendar);
		}
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, i);
		return calendar.getTime();
	}

	/**
	 * 
	 * @param time
	 * @return
	 */
	public static Date getTodaysDateWithSetTime(Date time) 
	{
		// Construct a Calendar from the passed date. This is used to
		// extract the required hour/minute values.
		Calendar calTime = (Calendar) calendars.get();
		if (calTime == null) {
			calTime = Calendar.getInstance();
			calendars.set(calTime);
		}
		calTime.setTime(time);
		
		Calendar today = Calendar.getInstance();
		
		// Copy over time fields
		today.set(Calendar.HOUR_OF_DAY, calTime.get(Calendar.HOUR_OF_DAY));
		today.set(Calendar.MINUTE, calTime.get(Calendar.MINUTE));
		today.set(Calendar.SECOND, calTime.get(Calendar.SECOND));
		today.set(Calendar.MILLISECOND, calTime.get(Calendar.MILLISECOND));
		
		return today.getTime();
	}


	/**
	 * 
	 * @param time
	 * @return
	 */
	public static Date setDateWithTime(Date dateToUse, Date time) 
	{
		Calendar calendar = Calendar.getInstance();
		Calendar calendar1 = Calendar.getInstance();
		calendar.setTime(dateToUse);
		calendar1.setTime(time);
		
		// Copy over time fields
		calendar.set(Calendar.HOUR_OF_DAY, calendar1.get(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar1.get(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar1.get(Calendar.SECOND));
		calendar.set(Calendar.MILLISECOND, calendar1.get(Calendar.MILLISECOND));
		
		return calendar.getTime();
	}
	
	/**
	 * Gets the current or next available working date
	 * (i.e not holiday, weekends)
	 * @param baseDate
	 * @return
	 * @throws DataException
	 */
	public static Date getWorkingDay(Date baseDate) {
		
		return getWorkingDay(baseDate,0, false);
	}	
	
	/**
	 * Gets the current or previous working date 
	 * (i.e not holiday, weekends)
	 * @param baseDate
	 * @return
	 */
	public static Date getPreviousWorkingDay(Date baseDate) {
		return getWorkingDay(baseDate, 0, true);
	}
	
	
	
	/**
	 * Gets the previous/next offset working day 
	 * @param baseDate
	 * @param workingDateOffset
	 * @param isPrevious
	 * @return
	 * @throws DataException
	 */
	public static Date getWorkingDay(Date baseDate, int workingDateOffset, boolean isPrevious) {
		
		Calendar newProcessDate = (Calendar) calendars.get();
		if (newProcessDate == null) {
			newProcessDate = Calendar.getInstance();
			calendars.set(newProcessDate);
		}
		
		newProcessDate.setTime(baseDate);

		// Determine the next available weekday.
		for (int i=0; i<=workingDateOffset; i++) {
			if (i!=0)
				newProcessDate.add(Calendar.DAY_OF_YEAR, (isPrevious)?-1:1);

			while ((newProcessDate.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
					|| (newProcessDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
					|| HolidayDateUtils.isHoliday(newProcessDate.getTime())) {

					newProcessDate.add(Calendar.DAY_OF_YEAR, (isPrevious)?-1:1);
			}
		}
		
		// Ensure new process date is a discrete date set at midnight.
		newProcessDate.set(Calendar.HOUR_OF_DAY, 0);
		newProcessDate.set(Calendar.MINUTE, 0);
		newProcessDate.set(Calendar.SECOND, 0);
		newProcessDate.set(Calendar.MILLISECOND, 0);
		
		Date workingDate = newProcessDate.getTime();
		return workingDate;
	}	


	/**
	 * Compares time of one date object to another, ignoring the date
	 * if -ve int is returned, date1 is before date2
	 * if 0 int is return, date1 is equal to date2
	 * if +ve int, date1 is after date2

	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int compareTimes(Date date1, Date date2) {
		Calendar calendar = Calendar.getInstance();
		Calendar calendar1 = Calendar.getInstance();
		calendar.setTime(date1);
		calendar1.setTime(date2);
		int i = calendar.get(Calendar.HOUR_OF_DAY) - calendar1.get(Calendar.HOUR_OF_DAY);
		if (i == 0)
			i = calendar.get(Calendar.MINUTE) - calendar1.get(Calendar.MINUTE);
		if (i == 0)
			i = calendar.get(Calendar.SECOND) - calendar1.get(Calendar.SECOND);
		return i;
	}


	/**
	 * Compares date one date object to another, ignoring the time.
	 * if -ve int is returned, date1 is before date2
	 * if 0 int is return, date1 is equal to date2
	 * if +ve int, date1 is after date2
	 * @param date1
	 * @param date2
	 * @return int
	 */
	public static int compareDates(Date date1, Date date2) {
		Calendar calendar = Calendar.getInstance();
		Calendar calendar1 = Calendar.getInstance();
		calendar.setTime(date1);
		calendar1.setTime(date2);
		int i = calendar.get(Calendar.YEAR) - calendar1.get(Calendar.YEAR);
		if (i == 0)
			i = calendar.get(Calendar.MONTH) - calendar1.get(Calendar.MONTH);
		if (i == 0)
			i = calendar.get(Calendar.DATE) - calendar1.get(Calendar.DATE);
		return i;
	}

	/**
	 * Compares date one date object to another, ignoring the time.
	 * if -ve int is returned, date1 is before date2
	 * if 0 int is return, date1 is equal to date2
	 * if +ve int, date1 is after date2
	 * @param date1
	 * @param date2
	 * @return int
	 */
	public static int compareDay(Date date1, Date date2) {
		Calendar calendar = Calendar.getInstance();
		Calendar calendar1 = Calendar.getInstance();
		calendar.setTime(date1);
		calendar1.setTime(date2);
		return calendar.get(Calendar.DATE) - calendar1.get(Calendar.DATE);
	}
	
	
	/**
	 * Round to the next closest day for the current date object, 
	 * if the date is on the current day, then this is returned
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date roundToNextDayOfWeek(Date date, int day) {
		Calendar calendar = (Calendar) calendars.get();
		if (calendar == null) {
			calendar = Calendar.getInstance();
			calendars.set(calendar);
		}
		calendar.setTime(date);
		
		// Determine the next available weekday.
		while ((calendar.get(Calendar.DAY_OF_WEEK)!=day)) {			   

			calendar.add(Calendar.DAY_OF_YEAR, 1);
		}
		return calendar.getTime();
	}
	
	/**
	 * Round to the next closest day for the current date object, 
	 * if the date is on the current day, then this is returned
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date roundToPreviousDayOfWeek(Date date, int day) {
		Calendar calendar = (Calendar) calendars.get();
		if (calendar == null) {
			calendar = Calendar.getInstance();
			calendars.set(calendar);
		}
		calendar.setTime(date);
		
		// Determine the earlier available weekday.
		while ((calendar.get(Calendar.DAY_OF_WEEK)!=day)) {			   
			calendar.add(Calendar.DAY_OF_YEAR, -1);
		}
		return calendar.getTime();
	}
	
	/**
	 * Get the day of week for date object
	 * @param date
	 * @return
	 */
	public static int getDayOfWeek(Date date) {
		Calendar calendar = (Calendar) calendars.get();
		if (calendar == null) {
			calendar = Calendar.getInstance();
			calendars.set(calendar);
		}
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}
	
	/**
	 * Get the day of week for date object
	 * @param date
	 * @return
	 */
	public static int getYear(Date date) {
		Calendar calendar = (Calendar) calendars.get();
		if (calendar == null) {
			calendar = Calendar.getInstance();
			calendars.set(calendar);
		}
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}
		
	public static String getYearAndMonthStr() {
		return formatyyyyMM(DateUtils.getNow());
	}
	
	
	
	
	/**
	 * Get the day of week for date object
	 * @param date
	 * @return
	 */
	public static String getYearAndQuarterStr() {
		Calendar calendar = (Calendar) calendars.get();
		if (calendar == null) {
			calendar = Calendar.getInstance();
			calendars.set(calendar);
		}
		calendar.setTime(DateUtils.getNow());
		int month = calendar.get(Calendar.MONTH);
		int qrt = 0;
		
		switch (month) {
			case Calendar.JANUARY:
			case Calendar.FEBRUARY:
			case Calendar.MARCH:
				qrt=1;
				break;
			case Calendar.APRIL:
			case Calendar.MAY:
			case Calendar.JUNE:
				qrt=2;
				break;
			case Calendar.JULY:
			case Calendar.AUGUST:
			case Calendar.SEPTEMBER:
				qrt=3;
				break;
			case Calendar.OCTOBER:
			case Calendar.NOVEMBER:
			case Calendar.DECEMBER:
				qrt=4;
				break;
		}
		
		return calendar.get(Calendar.YEAR)+"-"+qrt;
	}

	
	
	/**
	 * Compare the date and times between 2 date objects.
	 * if -ve int is returned, date1 is before date2
	 * if 0 int is return, date1 is equal to date2
	 * if +ve int, date1 is after date2
	 * @param dat1
	 * @param date2
	 * @return
	 */
	public static int compareDateTimes(Date date1, Date date2) {
		int i = compareDates(date1, date2);
		if (i == 0)
			i = compareTimes(date1, date2);
		return i;
	}
	
	
	/**
	 * Rounds a to end of month
	 * @param date
	 * @return Date
	 */
	public static Date roundToLastDayOfMonth(Date date) {
		Calendar calendar = (Calendar) calendars.get();
		if (calendar == null) {
			calendar = Calendar.getInstance();
			calendars.set(calendar);
		}
		
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();
	}	
	
	/**
	 * 
	 * @param date
	 * @param date1
	 * @return
	 */
	public static int differenceInDays(Date date, Date date1) {
		Calendar calendar = Calendar.getInstance();
		Calendar calendar1 = Calendar.getInstance();
		calendar.setTime(date);
		calendar1.setTime(date1);
		if (calendar.get(Calendar.YEAR) == calendar1.get(Calendar.YEAR))
			return calendar.get(Calendar.DAY_OF_YEAR) - calendar1.get(Calendar.DAY_OF_YEAR);
		if (calendar.get(Calendar.YEAR) > calendar1.get(Calendar.YEAR)) {
			int i = calendar.get(Calendar.DAY_OF_YEAR);
			calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 1);
			calendar.set(Calendar.MONTH, 11);
			calendar.set(Calendar.DAY_OF_MONTH, 31);
			for (; calendar.get(Calendar.YEAR) != calendar1.get(Calendar.YEAR); calendar.set(Calendar.YEAR,
					calendar.get(Calendar.YEAR) - 1))
				i += calendar.get(Calendar.DAY_OF_YEAR);

			i += calendar.get(Calendar.DAY_OF_YEAR) - calendar1.get(Calendar.DAY_OF_YEAR);
			return i;
		} else {
			return -differenceInDays(date1, date);
		}
	}	

	public static void main(String[] args) {
		Date date = DateUtils.getNow();
		Date date1 = DateUtils.getDate(2011, 5, 11);
		Date date2 = DateUtils.getDate(2010, 5, 13);
		System.out.println("overHere "+DateUtils.differenceInDays(date, date1));
		System.out.println("overHere "+DateUtils.differenceInDays(date, date2));
		
		System.out.println(DateUtils.getYearAndMonthStr());
		System.out.println(DateUtils.formatyyyyMM(DateUtils.getDate(2010, 5, 13)));
	}
	
	public static Date getDateFromNow(float numDays) {
		return getDateFromDate(DateUtils.getNow(), numDays);
	}

	public static Date getDateFromDate(Date date, float numDays) {
		
		if (date==null)
			return null;
		
		double d = numDays*86400000;
		long time = (long)(date.getTime() + d);		
		return new Date(time);
	}

	/**Compare 2 dates so see if they fall on the same day
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameDay(Date date1, Date date2)
	{
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(date1);
		cal2.setTime(date2);
		boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
		                  cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
		return sameDay;
	}
}
