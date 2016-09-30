/**
 * 
 */
package com.ecs.ucm.ccla.filename;

import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.DataResultSetUtils;

/** Stores a string->field mapping configuration.
 * 
 *  Models entries in the MFS_PROFILE table
 * 
 * @author Tom
 *
 */
public class MetadataMappingProfile {
	
	private int id;
	
	private String name;
	private String description;

	/** Names of the metadata fields which hold the source string for extraction and
	 *  checking. The first field containing a matching string value will be used for
	 *  extraction.
	 */
	private String[] sourceFields;
	
	/** Expected file extension. Can be null if this profile doesn't apply to filenames
	 **/
	private String fileExtension;
	
	/** Separator character used to delimit data values appearing in filenames 
	 * 
	 */
	private String fieldSeparatorChar;
	
	/** Raw list of field mappings for this profile.
	 *  
	 */
	private Vector<ProfileFieldMapping> fieldMappings;
	
	/** Subset of field mappings used for matching/capture against the source string.
	 *  
	 *  Ordered by their expected position in the source string.
	 **/ 
	private TreeMap<Integer, MetadataFieldMapping> matchFieldMappings;
	
	/** Subset of field mappings used for matching/capture against the source string.
	 *  
	 *  Ordered by their expected position in the source string.
	 **/ 
	private TreeMap<Integer, ProfileFieldMapping> customFieldMappings;
	
	/** RegExp pattern generated on instantiation, based on the passed list of field
	 *  mappings, separator char and file name extension.
	 */
	private Pattern matchPattern;
	
	private boolean initialized = false;
	
	public static class Cols {
		public static final String ID = "MFS_PROFILE_ID";
		public static final String NAME = "MFS_PROFILE_NAME";
		public static final String SOURCE_FIELD = "MFS_PROFILE_SOURCE_FIELD";
		public static final String DESCRIPTION = "MFS_PROFILE_DESCRIPTION";
		public static final String SEPARATOR_CHAR = "MFS_PROFILE_SEPARATOR_CHAR";
		public static final String FILE_EXTENSION = "MFS_PROFILE_FILE_EXTENSION";
	}
	
	public MetadataMappingProfile(int id, String name, String description,
			String sourceFields, String fileExtension,
			String fieldSeparatorChar) {
		this.id = id;
		this.name = name;
		this.description = description;
		
		this.sourceFields = sourceFields.split("\\|");

		this.fileExtension = fileExtension;
		this.fieldSeparatorChar = fieldSeparatorChar;
	}
	
	public static MetadataMappingProfile get(DataResultSet rs) throws DataException {
		return new MetadataMappingProfile(
			DataResultSetUtils.getResultSetIntegerValue(rs, Cols.ID),
			rs.getStringValueByName(Cols.NAME),
			rs.getStringValueByName(Cols.SOURCE_FIELD),
			rs.getStringValueByName(Cols.DESCRIPTION),
			rs.getStringValueByName(Cols.SEPARATOR_CHAR),
			rs.getStringValueByName(Cols.FILE_EXTENSION)
		);
	}
	
	/** Determines whether the passed string confirms to the format expected by this
	 *  mapping profile.
	 *  
	 * @param fileName
	 * @return
	 */
	public boolean matches(String string) {
		Matcher matcher = matchPattern.matcher(string);
		return matcher.matches();
	}
	
	public HashMap<String, String> getMapping(String string) 
	 throws DataException {
		
		String matchString = new String(string);
		
		Log.debug("Obtaining field mapping for source string '" + string + 
		 "' via Mapping Profile '" + this.getName() + "'");
		
		if (!initialized)
			throw new DataException("Metadata Mapping Profile '" + this.getName() +
			 "' not initialized");
		
		HashMap<String, String> mapping = new HashMap<String, String>();
		
		Matcher matcher = matchPattern.matcher(string);
		if (!matcher.matches()) {
			throw new DataException("Unable to derive metadata mapping: passed " +
			 "fileName does not match profile format");
		}
		
		// Filename confirms to match expression.
		
		
		if (this.fileExtension != null) {
			// Remove the filename extension and trim.
			matchString = string.substring
			 (0, string.length() - this.fileExtension.length() - 1).trim();
		}
		
		// Now split up the raw name by the separator char
		Vector<String> rawValues = MetadataFromString.extractChunksFromString
		 (matchString, this.fieldSeparatorChar);
		//String[] rawValues = matchString.split("\\" + this.fieldSeparatorChar);
		
		System.out.println("Split up source string. Yielded " + rawValues.size() + 
		 " pieces");
		
		// Check the number of pieces matches the number of fields we are mapping
		// to.
		if (rawValues.size() != matchFieldMappings.size()) {
			throw new DataException("String broke up into unexpected number of " +
			 "pieces (expected: " + matchFieldMappings.size() + ", actual: " +
			 rawValues.size() + ") for input string: " + string);
		}
		
		// Good stuff, now translate the raw values to the target metadata field
		// values.
		
		// Do easy ones first i.e. the ones that don't need special conversion.
		for (int i=0; i< rawValues.size(); i++) {
			String fileNameValue = rawValues.get(i);
			
			Integer fieldIndex = (i+1);
			
			if (matchFieldMappings.containsKey(fieldIndex)) {
				MetadataFieldMapping fieldMapping = matchFieldMappings.get(fieldIndex);
				
				if (fieldMapping.getConverterClass() == null) {
					// No complex conversion neccessary.
					mapping.put(fieldMapping.getUcmFieldName(), fileNameValue);
				}
			}
		}
		
		// Now do the hard ones that require custom conversion, in desired order of
		// execution
		for (Integer execOrder : customFieldMappings.keySet()) {
			ProfileFieldMapping profileFieldMapping = 
			 customFieldMappings.get(execOrder);
			
			MetadataFieldMapping fieldMapping = profileFieldMapping.getFieldMapping();
			
			String fileNameValue = null;
			
			// Custom fields may or may not specify a field position.
			Integer customFieldIndex = profileFieldMapping.getPosition();
			
			if (customFieldIndex != null)
				fileNameValue = rawValues.get(customFieldIndex-1);
			
			try {
				// Build an instance of the mapping's field converter
				MetadataFieldConverter fieldConverter =
				 (MetadataFieldConverter)fieldMapping.getConverterClass().newInstance();
				
				// Apply custom value conversion
				fieldConverter.convert(fieldMapping, fileNameValue, mapping);
				
			} catch (IllegalAccessException e) {
				throw new DataException
				("Unable to access field converter class: " 
				 + fieldMapping.getConverterClass().getSimpleName(), e);
				
			} catch (InstantiationException e) {
				throw new DataException
				("Unable to instantiate field converter class: " 
				 + fieldMapping.getConverterClass().getSimpleName(), e);
			}
		}
		
		Log.debug("Extracted " + mapping.size() + 
		 " name/value pairs from source string:");
		
		for (Map.Entry<String, String> fieldVal : mapping.entrySet()) {
			Log.debug(fieldVal.getKey() + "=" + fieldVal.getValue());
		}
		
		return mapping;
	}
	
	/** Sets the profile's field mapping arrays and builds the RegExp match string.
	 * 
	 * @param fieldMappings
	 * @throws DataException
	 */
	public void setFieldMappings(Vector<ProfileFieldMapping> fieldMappings) 
	 throws DataException {
		this.fieldMappings = fieldMappings;
		
		try {
			init();
		} catch (Exception e) {
			throw new DataException("Failed to build Metadata Field Mapping '" +
			 this.getName() + "': " + e.getMessage(), e);
		}
	}

	public Vector<ProfileFieldMapping> getFieldMappings() {
		return fieldMappings;
	}
	
	public Pattern getMatchPattern() {
		return this.matchPattern;
	}
	
	/** Builds the reg exp match pattern used to identify a matching filename for
	 *  this profile.
	 *  
	 *  Also builds the ordered tree sets matchFieldMappings and customFieldMappings.
	 *  
	 * @return
	 * @throws DataException 
	 */
	private void init() throws DataException {
		System.out.println("Initializing Metadata Mapping Profile '" + this.getName() + 
		 "'");
		
		StringBuffer pattern = new StringBuffer();
		
		// Prepare a mapping between string field indexes and their metadata mappings.
		matchFieldMappings = new TreeMap<Integer, MetadataFieldMapping>();
		
		for (ProfileFieldMapping fieldMapping : fieldMappings) {
			// Ignore any Profile Field Mappings that don't specify a position.
			if (fieldMapping.getPosition() != null) {
				// Make sure this field position doesn't already have a metadata mapping
				// allocated - fail in this case!
				if (matchFieldMappings.containsKey(fieldMapping.getPosition()))
					throw new DataException("Metadata mapping already allocated to " +
					 "field index " + fieldMapping.getPosition() + ". Ensure positions " 
					 + "are unique or empty");
				
				matchFieldMappings.put
				 (fieldMapping.getPosition(), fieldMapping.getFieldMapping());
			}
		}
		
		System.out.println("Captured " + matchFieldMappings.size() + 
		 " positional field mappings");
		
		// Now capture all custom field mappings, in the desired order of execution.
		customFieldMappings = new TreeMap<Integer, ProfileFieldMapping>();
		
		for (ProfileFieldMapping fieldMapping : fieldMappings) {
			MetadataFieldMapping thisFieldMapping = fieldMapping.getFieldMapping();
			
			if (thisFieldMapping.getConverterClass() != null) {
				if (fieldMapping.getExecutionOrder() == null) {
					// Fail if the custom mapping doesn't have an execution order
					// specified.
					throw new DataException("Custom metadata mapping '" + 
					 thisFieldMapping.getName() + "' does not have an execution order " +
					 "specified");
				}
				
				if (customFieldMappings.containsKey(fieldMapping.getExecutionOrder())) {
					// Fail if this execution order index has already been used
					throw new DataException("Metadata mapping already allocated to " +
					 "execution order " + fieldMapping.getExecutionOrder() + 
					 ". Ensure execution orders are unique");
				}
				
				customFieldMappings.put(fieldMapping.getExecutionOrder(), fieldMapping);
			}
		}
		
		System.out.println("Captured " + customFieldMappings.size() + 
		 " custom field mappings");
		
		// Now build the RegExp used to match against source Strings.
		for (Integer fieldIndex : matchFieldMappings.keySet()) {
			if (pattern.length() > 0) {
				// Append a separator char
				pattern.append(this.fieldSeparatorChar);
			}
			
			pattern.append(matchFieldMappings.get(fieldIndex).getMatchPattern());
		}
		
		if (!StringUtils.stringIsBlank(this.fileExtension)) {
			// Accept the file extension in upper and lowercase.
			pattern.append(".(" + this.fileExtension.toLowerCase() + 
							"|" 
							+ this.fileExtension.toUpperCase()+ ")");
		}
		
		System.out.println("Building match pattern from String: " + pattern);
		this.matchPattern = Pattern.compile(pattern.toString());
		
		this.initialized = true;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String[] getSourceFields() {
		return sourceFields;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public String getFieldSeparatorChar() {
		return fieldSeparatorChar;
	}
}