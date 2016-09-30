package com.ecs.ucm.ccla.filename.fieldconverter;

import java.util.HashMap;

import com.ecs.ucm.ccla.Globals.UCMFieldNames;
import com.ecs.ucm.ccla.filename.MetadataFieldConverter;
import com.ecs.ucm.ccla.filename.MetadataFieldMapping;
import com.ecs.utils.StringUtils;

/** Checks for a rawAmount value in the field mapping. If not present, do nothing.
 *  
 *  If present, comma characters are first stripped out.
 *  
 *  The pennies/fraction component can be delimited with a space or full-stop. If
 *  a space was used, this is replaced with a full-stop.
 *  
 *  The normalized amount figure is then added to the mapping as 'xUnits', if the
 *  present 'xCurrency' value is set to 'UNI'. Otherwise it is added to the mapping
 *  as 'xAmount'
 *  
 * @author Tom
 *
 */
public class AmountUnitsFieldConverter implements MetadataFieldConverter {

	public void convert(MetadataFieldMapping fieldMapping,
			String fileNameValue, HashMap<String, String> mapping) {
		
		String rawAmount = fileNameValue;
		String currency = mapping.get("Currency");
		
		if (StringUtils.stringIsBlank(rawAmount) 
			|| StringUtils.stringIsBlank(currency))
			return;
		
		// Strip out commas
		String normalizedAmount = rawAmount.replaceAll("\\,", "");
		// Replace space char with full-stop
		normalizedAmount = normalizedAmount.replaceAll("\\s", ".");
		
		if (currency.equals("UNI")) {
			// Must be a Unit value
			mapping.put(UCMFieldNames.Units, normalizedAmount);
		} else {
			// Assume monetary amount value
			mapping.put(UCMFieldNames.Amount, normalizedAmount);
		}
	}
	
}