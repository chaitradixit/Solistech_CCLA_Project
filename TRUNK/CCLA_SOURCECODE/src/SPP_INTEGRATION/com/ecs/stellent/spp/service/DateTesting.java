package com.ecs.stellent.spp.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.ecs.utils.Log;

public class DateTesting {
	
	public static void main (String[] args) throws ParseException {
		
		SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
		Date date = f.parse("01/08/2010");

		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		
		System.out.println(cal.getTime().toString());
		System.out.println(cal.getTimeZone().inDaylightTime(cal.getTime()));
		
		
		
		if (cal.get(Calendar.DST_OFFSET) > 0) {
			// We are in BST (British Summer Time). Add an extra hour
			// to counter the UTC date transition
			cal.add(Calendar.HOUR_OF_DAY, 1);
			
			date = cal.getTime();
			
			System.out.println(date);
		} else {
			// We are in GMT. No time edits required.
		}
	}
	
}
