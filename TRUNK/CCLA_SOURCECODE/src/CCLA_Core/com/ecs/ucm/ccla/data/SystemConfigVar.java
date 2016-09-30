package com.ecs.ucm.ccla.data;

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.cache.Cachable;

import com.ecs.ucm.ccla.utils.DateUtils;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.DateFormatter;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Models entries from the SYSTEM_CONFIG_VAR table.
 *  
 *  This table is used to store global configuration values, which can be
 *  changed immediately by administrators or high-level users.
 *  
 *  These are all cached on server startup, see the CacheManager class.
 *  
 * @author Tom
 *
 */
public class SystemConfigVar implements Persistable {
	
	public enum DataType {
		STRING, INT, FLOAT, DATE, BOOL
	}
	
	private String name;
	private String description;
	private DataType dataType;
	private String type;
	
	
	private String stringValue;
	private Integer intValue;
	private Float floatValue;
	private Date dateValue;
	private Boolean boolValue;
	
	private Date lastUpdated;
	private String lastUpdatedBy;
	
	/** Creates an empty SystemConfigVar without a value set.
	 * 
	 * @param name
	 * @param description
	 * @param dataType
	 */
	public SystemConfigVar(String name, String description, DataType dataType, String type) {
		this(name, description, dataType, type, null, null, null, null, null, null, null);
	}
	
	public SystemConfigVar(String name, String description, DataType dataType, String type,
			String stringValue, Integer intValue, Float floatValue, Date dateValue,
			Boolean boolValue, Date lastUpdated, String lastUpdatedBy) {
		this.name = name;
		this.description = description;
		this.dataType = dataType;
		this.type = type;
		this.stringValue = stringValue;
		this.intValue = intValue;
		this.floatValue = floatValue;
		this.dateValue = dateValue;
		this.boolValue = boolValue;
		
		this.lastUpdated = lastUpdated;
		this.lastUpdatedBy = lastUpdatedBy;
	}
	
	public static SystemConfigVar get(String name, FWFacade facade) 
	 throws DataException {
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryParamToBinder(binder, "CONFIG_VAR_NAME", name);
		
		DataResultSet rs =
		 facade.createResultSet("qClientServices_GetSystemConfigVar", binder);
		
		return get(rs);
	}
	
	public static Vector<SystemConfigVar> getAll(FWFacade facade) 
	 throws DataException {
		
		Vector<SystemConfigVar> configValues = new Vector<SystemConfigVar>();
		
		DataResultSet rs =
		 facade.createResultSet("qClientServices_GetSystemConfigVars", 
		 new DataBinder());
		
		if (rs.first()) {
			do {
				configValues.add(get(rs));
			} while (rs.next());
		}
			
		return configValues;
	}
	
	public static SystemConfigVar get(DataResultSet rs) throws DataException {
		return new SystemConfigVar(
			rs.getStringValueByName("CONFIG_VAR_NAME"),
			rs.getStringValueByName("CONFIG_VAR_DESCRIPTION"),
			DataType.valueOf(rs.getStringValueByName("CONFIG_VAR_DATA_TYPE")),
			rs.getStringValueByName("CONFIG_VAR_TYPE"),
			rs.getStringValueByName("STRING_VALUE"),
			CCLAUtils.getResultSetIntegerValue(rs, "INT_VALUE"),
			CCLAUtils.getResultSetFloatValue(rs, "FLOAT_VALUE"),
			rs.getDateValueByName("DATE_VALUE"),
			CCLAUtils.getResultSetBoolValueAllowNull(rs, "BOOL_VALUE"),
			rs.getDateValueByName("LAST_UPDATED"),
			rs.getStringValueByName("LAST_UPDATED_BY")
		);
	}
	
	/** Adds a new SystemConfigValue to the database. 
	 * 
	 *  Name must be unique.
	 * 
	 * @param name
	 * @param type
	 * @param value
	 * @param facade
	 * @param userName
	 * @return
	 * @throws DataException
	 */
	public static SystemConfigVar add(String name, String description, 
	 DataType dataType, String type, Object value, 
	 FWFacade facade, String userName) throws DataException {
		
		SystemConfigVar configValue = new SystemConfigVar(name, description, dataType, type);
		configValue.validate(facade);
		
		configValue.setValue(value);
		configValue.setLastUpdatedBy(userName);

		DataBinder binder = new DataBinder();
		configValue.addFieldsToBinder(binder);
		
		facade.execute("qClientServices_AddSystemConfigVar", binder);
		return configValue;
	}
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		
		binder.putLocal("CONFIG_VAR_NAME", this.getName());
		CCLAUtils.addQueryParamToBinder(binder, "CONFIG_VAR_DESCRIPTION", this.getDescription());
		
		binder.putLocal("CONFIG_VAR_DATA_TYPE", this.getDataType().toString());
		binder.putLocal("CONFIG_VAR_TYPE", this.getType());
		
		CCLAUtils.addQueryParamToBinder(binder, "STRING_VALUE", this.getStringValue());
		CCLAUtils.addQueryIntParamToBinder(binder, "INT_VALUE", this.getIntValue());
		CCLAUtils.addQueryFloatParamToBinder(binder, "FLOAT_VALUE", this.getFloatValue());
		CCLAUtils.addQueryDateParamToBinder(binder, "DATE_VALUE", this.getDateValue());
		CCLAUtils.addQueryBooleanParamToBinderAllowNull
		 (binder, "BOOL_VALUE", this.getBoolValue());
		
		binder.putLocal("LAST_UPDATED_BY", this.getLastUpdatedBy());
	}

	public void persist(FWFacade facade, String username) throws DataException {
		this.setLastUpdatedBy(username);
		this.validate(facade);
		
		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);
		
		facade.execute("qClientServices_UpdateSystemConfigVar", binder);
	}

	public void setAttributes(DataBinder binder) throws DataException {
		
		this.setName(binder.getLocal("CONFIG_VAR_NAME"));
		this.setDescription(binder.getLocal("CONFIG_VAR_DESCRIPTION"));
		this.setDataType(SystemConfigVar.DataType.valueOf(binder.getLocal("CONFIG_VAR_DATA_TYPE")));
		this.setType(binder.getLocal("CONFIG_VAR_TYPE"));
		
		if (this.getDataType()!=null) {
			switch (this.getDataType()) {
				case BOOL:
					this.setBoolValue(CCLAUtils.getBinderBoolValue(binder, "BOOL_VALUE"));						
					break;
				case DATE:
					this.setDateValue(CCLAUtils.getBinderDateValue(binder, "DATE_VALUE"));
					break;
				case STRING:
					this.setStringValue(binder.getLocal("STRING_VALUE"));
					break;
				case INT:
					this.setIntValue(CCLAUtils.getBinderIntegerValue(binder, "INT_VALUE"));
					break;
				case FLOAT:
					this.setFloatValue(CCLAUtils.getBinderFloatValue(binder, "FLOAT_VALUE"));
					break;
				default:
					throw new DataException("Unknown datatype found.");
			}
		}
	}

	public void validate(FWFacade facade) throws DataException {
		if (StringUtils.stringIsBlank(this.getName()))
			throw new DataException("System config name missing");
		
		if (this.getDataType() == null)
			throw new DataException("Data Type missing");
	}
	
	/** Returns the String representation of the config var, regardless of its type.
	 *  
	 *  -Dates are returned formatted to the current locale string
	 *  -Bools are returned as either 0 or 1
	 *  
	 * @return
	 */
	public String getValue() {
		if (this.getDataType().equals(DataType.STRING)) {
			return this.getStringValue();
		} else if (this.getDataType().equals(DataType.DATE)) {
			if (this.getDateValue() == null)
				return null;
			else
				return DateFormatter.getTimeStamp(this.getDateValue());
		} else if (this.getDataType().equals(DataType.BOOL)) {
			if (this.getBoolValue() == null || !this.getBoolValue())
				return "0";
			else
				return "1";
		} else if (this.getDataType().equals(DataType.INT)) {
			if (this.getIntValue() != null)
				return this.getIntValue().toString();
			else
				return null;
		} else if (this.getDataType().equals(DataType.FLOAT)) {
			if (this.getFloatValue() != null)
				return this.getFloatValue().toString();
			else
				return null;
		} else {
			return null;
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public Integer getIntValue() {
		return intValue;
	}

	public void setIntValue(Integer intValue) {
		this.intValue = intValue;
	}

	public Date getDateValue() {
		return dateValue;
	}

	public void setDateValue(Date dateValue) {
		this.dateValue = dateValue;
	}

	public Boolean getBoolValue() {
		return boolValue;
	}

	public void setBoolValue(Boolean boolValue) {
		this.boolValue = boolValue;
	}
	
	/** Generic set-value method. Throws ClassCastException if a mismatched data
	 *  type is passed in.
	 *  
	 * @param obj
	 */
	public void setValue(Object obj) {
		if (this.getDataType().equals(DataType.STRING))
			this.setStringValue((String)obj);
		else if (this.getDataType().equals(DataType.INT))
			this.setIntValue((Integer)obj);
		else if (this.getDataType().equals(DataType.FLOAT))
			this.setFloatValue((Float)obj);
		else if (this.getDataType().equals(DataType.DATE))
			this.setDateValue((Date)obj);
		else if (this.getDataType().equals(DataType.BOOL))
			this.setBoolValue((Boolean)obj);
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setFloatValue(Float floatValue) {
		this.floatValue = floatValue;
	}

	public Float getFloatValue() {
		return floatValue;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	/*
	 *  Caching stuff
	 */
	private static Cache CACHE = new Cache();
	
	public static Cachable<String, SystemConfigVar> getCache() {
		return CACHE;
	}
	
	/** InstructionCondition cache implementor */
	private static class Cache extends Cachable<String, SystemConfigVar> {

		public Cache() {
			super("System Config Var");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<SystemConfigVar> vars = SystemConfigVar.getAll(facade);
			
			HashMap<String, SystemConfigVar> newCache = 
			 new HashMap<String, SystemConfigVar>();
			
			for (SystemConfigVar systemConfigVar : vars) {
				newCache.put(systemConfigVar.getName(), systemConfigVar);
			}
			
			this.CACHE_MAP = newCache;
		}
	}

	
	private static TypeCache TYPE_CACHE = new TypeCache();
	
	public static Cachable<String, Vector<SystemConfigVar>> getTypeCache() {
		return TYPE_CACHE;
	}
	

	/** InstructionType cache implementor.
	 *  
	 *  Maps Instruction Type IDs againsts Instruction Type instances.
	 *  
	 *  */
	private static class TypeCache extends Cachable<String, Vector<SystemConfigVar>> {
		public TypeCache() {
			super("System Config Var Type");
		}

		@Override
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<SystemConfigVar> vars = SystemConfigVar.getAll(facade);
			
			HashMap<String, Vector<SystemConfigVar>> newCache = 
			 new HashMap<String, Vector<SystemConfigVar>>();

			
			for (SystemConfigVar systemConfigVar : vars) 
			{
				Vector<SystemConfigVar> varVec = null;
				
				if (!StringUtils.stringIsBlank(systemConfigVar.getType())) {
					if (newCache.get(systemConfigVar.getType())!=null) 
					{
						varVec = newCache.get(systemConfigVar.getType());
					} else {
						varVec = new Vector<SystemConfigVar>();
					}
					varVec.add(systemConfigVar);
					newCache.put(systemConfigVar.getType(), varVec);
				} else {
					Log.debug("SystemConfigVar type is empty for "+
					 systemConfigVar.getName());
				}
			}
			
			this.CACHE_MAP = newCache;
		}
	}
}
