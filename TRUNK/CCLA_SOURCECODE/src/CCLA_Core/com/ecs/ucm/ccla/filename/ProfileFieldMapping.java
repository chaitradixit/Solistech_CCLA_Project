package com.ecs.ucm.ccla.filename;

import com.ecs.utils.stellent.DataResultSetUtils;

import intradoc.data.DataException;
import intradoc.data.DataResultSet;

/** A single mapping between a Metadata Mapping Profile and a Metadata Field Mapping.
 * 
 *  Used to attach field positioning and execution orders to the individual field
 *  mappings.
 * 
 *  Models entries in the MFS_PROFILE_FIELD_MAPPING table
 *  
 * @author Tom
 *
 */
public class ProfileFieldMapping {
	
	private int profileId;
	private MetadataFieldMapping fieldMapping;
	
	/** Position of this field in the source string.
	 *  
	 *  Will be null if the particular field isn't expected to appear anywhere in the
	 *  String (i.e. if its a product of other converted field values)
	 **/
	private Integer position;
	
	/** Order that the custom field converter class will be executed, with respect to
	 *  other field converter classes in the profile. They will be loaded in ascending
	 *  execution order.
	 *  
	 *  For Metadata Field Mappings without a custom converter class, this field is
	 *  irrelevant and expected to be null.
	 */
	private Integer executionOrder;
	
	public static class Cols {
		public static final String POSITION = "FIELD_POSITION";
		public static final String EVAL_ORDER = "EVAL_ORDER";
	}
	
	public ProfileFieldMapping(int profileId,
	 MetadataFieldMapping fieldMapping, Integer position, Integer executionOrder) {
		this.profileId = profileId;
		this.fieldMapping = fieldMapping;
		this.position = position;
		this.executionOrder = executionOrder;
	}
	
	public static ProfileFieldMapping get(DataResultSet rs) 
	 throws DataException {
		
		return null;
		
		/*
		return new ProfileFieldMapping(
			DataResultSetUtils.getResultSetIntegerValue(rs, MetadataMappingProfile.Cols.ID),
			DataResultSetUtils.getResultSetIntegerValue(rs, MetadataFieldMapping.Cols.ID),
			DataResultSetUtils.getResultSetIntegerValue(rs, Cols.POSITION),
			DataResultSetUtils.getResultSetIntegerValue(rs, Cols.EVAL_ORDER)
		);
		*/
	}
	
	public int getProfileId() {
		return profileId;
	}
	public MetadataFieldMapping getFieldMapping() {
		return fieldMapping;
	}
	public Integer getPosition() {
		return position;
	}
	public Integer getExecutionOrder() {
		return executionOrder;
	}
}
