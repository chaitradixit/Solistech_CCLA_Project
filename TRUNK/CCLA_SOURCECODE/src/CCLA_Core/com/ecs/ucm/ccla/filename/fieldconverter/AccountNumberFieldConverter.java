package com.ecs.ucm.ccla.filename.fieldconverter;

import java.util.HashMap;

import com.ecs.ucm.ccla.Globals.UCMFieldNames;
import com.ecs.ucm.ccla.filename.MetadataFieldConverter;
import com.ecs.ucm.ccla.filename.MetadataFieldMapping;
import com.ecs.utils.StringUtils;

/** Concatenates the passed numeric account number with the Fund Code present in
 *  the mapping.
 *  
 * @author Tom
 *
 */
public class AccountNumberFieldConverter 
 implements MetadataFieldConverter {

	public void convert(
			MetadataFieldMapping fieldMapping, String fileNameValue,
			HashMap<String, String> mapping) {
		String accNumber = fileNameValue;
		String fundCode = mapping.get(UCMFieldNames.Fund);
		
		if (!StringUtils.stringIsBlank(accNumber) 
			&& !StringUtils.stringIsBlank(fundCode)) {
		
			String accNumberStr = accNumber + fundCode;
			mapping.put(fieldMapping.getUcmFieldName(), accNumberStr);
		}
	}
}