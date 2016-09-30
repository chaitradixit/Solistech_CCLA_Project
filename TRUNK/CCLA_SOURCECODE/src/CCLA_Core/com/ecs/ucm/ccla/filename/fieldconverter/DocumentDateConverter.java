package com.ecs.ucm.ccla.filename.fieldconverter;

import intradoc.common.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import com.ecs.ucm.ccla.filename.MetadataFieldConverter;
import com.ecs.ucm.ccla.filename.MetadataFieldMapping;

/** Converts a date in the format YYYYMMDD to dd/MM/yyyy
 * 
 * @author Tom
 *
 */
public class DocumentDateConverter implements MetadataFieldConverter {

	private static SimpleDateFormat SOURCE_DATE_FORMAT = 
	 new SimpleDateFormat("yyyyMMdd");
	
	private static SimpleDateFormat DESTINATION_DATE_FORMAT = 
	 new SimpleDateFormat("dd/MM/yyyy");
	
	public void convert(MetadataFieldMapping fieldMapping,
			String fileNameValue, HashMap<String, String> mapping) {
		
		try {
			Date date = SOURCE_DATE_FORMAT.parse(fileNameValue);
			
			mapping.put(fieldMapping.getUcmFieldName(), 
			 DESTINATION_DATE_FORMAT.format(date));
			
		} catch (ParseException e) {
			Log.warn("Unable to parse filename date: " + fileNameValue);
		}
		
	}
	
}