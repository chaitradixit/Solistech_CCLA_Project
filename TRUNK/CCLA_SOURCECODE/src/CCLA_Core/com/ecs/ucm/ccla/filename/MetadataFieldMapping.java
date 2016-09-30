/**
 * 
 */
package com.ecs.ucm.ccla.filename;

/** Converter configuration for a single substring value to UCM field.
 * 
 *  A set of these are linked to a single MetadataMappingProfile instance.
 *  
 *  Models entries in the MFS_FIELD_MAPPING table
 *  
 * @author Tom
 *
 */
public class MetadataFieldMapping {
	
	private String name;
	
	/** RegExp for this particular field. Used to determine whether the expected 
	 *  substring of a source string matches the expected field format.
	 * 
	 */
	private String matchPattern;
	
	/** Custom converter class, used for more complex value translation. If this isn't
	 *  specified, the source substring value is copied as-is without modification.
	 */
	private Class<? extends MetadataFieldConverter> converterClass;
	
	/** Target field name for the mapped value. */
	private String ucmFieldName;
	
	public static class Cols {
		public static final String ID = "MFS_FIELDMAP_ID";
		public static final String NAME = "MFS_FIELDMAP_NAME";
		public static final String UCM_FIELD_NAME = "MFS_FIELDMAP_UCMFIELDNAME";
		public static final String REGEXP = "MFS_FIELDMAP_REGEXP";
		public static final String CONVERTER_CLASS = "MFS_FIELDMAP_CONVERTERCLASS";
	}
	
	public MetadataFieldMapping(String name, String ucmFieldName, 
			String matchPattern,
			Class<? extends MetadataFieldConverter> converterClass) {
		this.name = name;
		this.matchPattern = matchPattern;
		this.converterClass = converterClass;
		this.ucmFieldName = ucmFieldName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMatchPattern() {
		return matchPattern;
	}

	public void setMatchPattern(String matchPattern) {
		this.matchPattern = matchPattern;
	}

	public Class<?> getConverterClass() {
		return converterClass;
	}

	public void setConverterClass(Class<? extends MetadataFieldConverter> converterClass) {
		this.converterClass = converterClass;
	}

	public String getUcmFieldName() {
		return ucmFieldName;
	}

	public void setUcmFieldName(String ucmFieldName) {
		this.ucmFieldName = ucmFieldName;
	}
}