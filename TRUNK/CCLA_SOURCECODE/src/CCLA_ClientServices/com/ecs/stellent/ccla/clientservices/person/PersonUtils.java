package com.ecs.stellent.ccla.clientservices.person;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.shared.SharedObjects;

import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.DateFormatter;
import com.experian.webservice.EngineType;
import com.experian.webservice.QAConfigType;

public class PersonUtils {
	
	public static String getBinderValue(DataBinder binder, String Name){
	
		String returnStr = "";		
		if (binder.getLocal(Name) != null)
			returnStr = binder.getLocal(Name);
		
		return returnStr;
	}
	
	/** compares value of parameter name in binder with column of a
	 * DataResultSet and returns true if they match
	 * or false if they do not match
	 * 
	 */

	public static boolean compareBinderWithResultSet(DataBinder binder, DataResultSet rs, String parameterName ){
		
		boolean retValue = false;
		String binderValue = binder.getLocal(parameterName);
		String rsValue = "";
		for(int i = 0; i < rs.getNumRows(); i++)
		{
			rs.setCurrentRow(i);
			rsValue=rs.getStringValueByName(parameterName);
		}
		Log.debug("COMPARING " + rsValue + " with " + binderValue);
		if (binderValue.equals(rsValue))
		{
			retValue = true;
		}
		return retValue;
	}
	
	/* Grabs the separate date, month and year values from the DataBinder
	 * and attempts some basic validation, before combining them in the binder
	 * as a valid date string.
	 * 
	 * @throws DataException if an invalid Date is present in the Binder.
	 */
	public static Date getDateOfBirth(DataBinder binder) throws DataException {	
		
		String day = binder.getLocal("DOB_DAY");
		Log.debug("day is " + day);
		String month = binder.getLocal("DOB_MONTH");
		Log.debug("month is " + month);
		String year = binder.getLocal("DOB_YEAR");
		Log.debug("year is " + year);
		
		// First check for all 3 date fields being empty.
		if (StringUtils.stringIsBlank(day) 
			&& 
			StringUtils.stringIsBlank(month) 
			&& 
			StringUtils.stringIsBlank(year)) {
			
			// User has not attempted to input a date, or removed the
			//existing one.
			// Return a null Date value.
			return null;
			
			//ClientServicesUtils.addQueryDateParamToBinder(binder, "DOB", null);
			
		} else if  (StringUtils.stringIsBlank(day) 
			||
			StringUtils.stringIsBlank(month) 
			||
			StringUtils.stringIsBlank(year)) {
			// Now check for a single empty field - this indicates an
			// invalid date.
			
			String msg = "Invalid date of birth entered: " +
			 "day, month or year value is missing.";
			
			Log.error(msg);
			throw new DataException(msg);	
			
		} else {
			// Attempt to parse the full date string. Append hours/minutes
			// to ensure it matches the expected UCM date format.
			String fullDate = day + "/" + month + "/" + year + " 00:00";
			Log.debug("Attempting to parse date: " + fullDate);
			
			try {
				Date dateOfBirth = 
				 DateFormatter.getSystemSimpleDateFormat().parse(fullDate);
				
				Log.debug("Successfully parsed date: " + fullDate);
				return dateOfBirth;
			
			} catch (ParseException e) {
				String msg = "Unable to parse date of birth: " + fullDate;
				
				Log.error(msg);
				throw new DataException(msg);
			}
		}
	}
	
	/** Splits up date so it can be put into day/month/year fields
	 *  
	 */		
	public static void splitDateOfBirth (Date dob, DataBinder binder) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dob);
		Log.debug("dob is " + DateFormatter.getTimeStamp(dob));
		Log.debug("formatted dob is " + DateFormatter.getTimeStamp(cal.getTime()));
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH) + 1;
		int year = cal.get(Calendar.YEAR);
		
		Log.debug(day + "/" + month + "/" + year);
		String fullDOB = Integer.toString(day) + "/" + 
		 Integer.toString(month) + "/" + Integer.toString(year);
		Log.debug("fullDOB is " + fullDOB);
		
		binder.putLocal("DOB_DAY", Integer.toString(day));
		binder.putLocal("DOB_MONTH", Integer.toString(month));
		binder.putLocal("DOB_YEAR", Integer.toString(year));
	}
	
}
