/**
 * 
 */
package com.ecs.ucm.ccla.filename;

import java.util.HashMap;

/** Supports custom value conversions.
 * 
 * @author Tom
 *
 */
public interface MetadataFieldConverter {
	
	public void convert(MetadataFieldMapping fieldMapping, String fileNameValue, 
	 HashMap<String, String> mapping);
}