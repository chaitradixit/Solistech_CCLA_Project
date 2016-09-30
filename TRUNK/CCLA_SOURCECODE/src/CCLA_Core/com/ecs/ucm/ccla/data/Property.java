package com.ecs.ucm.ccla.data;

import java.util.HashMap;
import java.util.Vector;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.ucm.ccla.data.ElementAttribute.SelectionType;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Models entries from the REF_PROPERTY table.
 * 
 * @author Tom
 *
 */
public class Property implements Persistable {
	
	private int propertyId;
	
	private String name;
	private String description;
	
	private Integer verificationSourceId;
	private boolean setByUser;
	
	private PropertyType propertyType;
	private boolean singleton;
	
	/** Types of Property. Describes what data is stored against applied Properties.
	 */
	public static enum PropertyType {
		STRING, // PROPERTY_VALUE must be set, PROPERTY_STATUS will always be 1
		BOOL, 	// PROPERTY_STATUS can be 0 or 1
		FLAG	// PROPERTY_STATUS can only be 1
	}
	
	public static class Cols {
		public static final String ID = "PROPERTY_ID";
	}
	
	public static class Ids {
		public static final int DEFAULT = 1;
		public static final int VERIFIED_BY_AML_TRACKER = 14;
	}

	public Property(int propertyId, String name, String description,
			Integer verificationSourceId, boolean setByUser, 
			PropertyType propertyType, boolean singleton) {
		
		this.propertyId = propertyId;
		this.name = name;
		this.description = description;
		this.verificationSourceId = verificationSourceId;
		this.setByUser = setByUser;
		this.setPropertyType(propertyType);
		this.setSingleton(singleton);
	}
	
	public static Property get(DataResultSet rs) throws DataException {
		
		if (rs.isEmpty())
			return null;
			
		return new Property(
			CCLAUtils.getResultSetIntegerValue(rs, Cols.ID),
			rs.getStringValueByName("PROPERTY_NAME"),
			rs.getStringValueByName("PROPERTY_DESCRIPTION"),
			CCLAUtils.getResultSetIntegerValue(rs, "VERIFICATION_SOURCE_ID"),
			CCLAUtils.getResultSetBoolValue(rs, "SET_BY_USER"),
			PropertyType.valueOf(rs.getStringValueByName("PROPERTY_TYPE")),
			CCLAUtils.getResultSetBoolValue(rs, "IS_SINGLETON")
		);
	}
	
	public static Property get(int propertyId, FWFacade facade) throws DataException {
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.ID, propertyId);
		
		return get(facade.createResultSet("qClientServices_GetProperty", binder));
	}
	
	public static Vector<Property> getAll(DataResultSet rs) 
	 throws DataException {
		
		Vector<Property> relProps = new Vector<Property>();
		
		if (rs.first()) {
			do {
				relProps.add(get(rs));
			} while (rs.next());
		}
		
		return relProps;
	}
	
	public static DataResultSet getAllData(FWFacade facade) throws DataException {
		return facade.createResultSet
		 ("qClientServices_GetAllProperties", new DataBinder());
	}
	
	public static Vector<Property> getAvailableProperties
	 (RelationName relName, FWFacade facade) throws DataException {
		
		DataResultSet rs = getAvailablePropertiesData(relName, facade);
		return getAll(rs);
	}
	
	/** Fetches a ResultSet of all Relation Properties which are available for use
	 *  with the given Relation Name.
	 *  
	 * @param relType
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getAvailablePropertiesData
	 (RelationName relName, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, RelationName.Cols.ID, relName.getRelationNameId());
		
		return facade.createResultSet
		 ("qClientServices_GetAvailableRelationProperties", binder);
	}
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		// TODO Auto-generated method stub
		throw new DataException("Not implemented");
		
	}

	public void persist(FWFacade facade, String username) throws DataException {
		// TODO Auto-generated method stub
		throw new DataException("Not implemented");

	}

	public void setAttributes(DataBinder binder) throws DataException {
		// TODO Auto-generated method stub
		throw new DataException("Not implemented");
	}

	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub
		throw new DataException("Not implemented");
	}

	public int getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(int propertyId) {
		this.propertyId = propertyId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getVerificationSourceId() {
		return verificationSourceId;
	}

	public void setVerificationSourceId(Integer verificationSourceId) {
		this.verificationSourceId = verificationSourceId;
	}

	public boolean isSetByUser() {
		return setByUser;
	}

	public void setSetByUser(boolean setByUser) {
		this.setByUser = setByUser;
	}

	public void setPropertyType(PropertyType propertyType) {
		this.propertyType = propertyType;
	}

	public PropertyType getPropertyType() {
		return propertyType;
	}

	public void setSingleton(boolean singleton) {
		this.singleton = singleton;
	}

	public boolean isSingleton() {
		return singleton;
	}
	
	/** Determines whether this property is a Verification Property (i.e. is it linked
	 *  to a Verification Source)
	 *  
	 * @return
	 */
	public boolean isVerificationProperty() {
		return this.getVerificationSourceId() != null;
	}
	
	/*
	 *  Caching stuff
	 */
	private static Cache CACHE = new Cache();
	
	/** Cache of Element Attribute ID to ElementAttribute instances.
	 * 
	 * @return
	 */
	public static Cachable<Integer, Property> getCache() {
		return CACHE;
	}
	
	/** Property cache implementor */
	private static class Cache extends Cachable<Integer, Property> {

		public Cache() {
			super("Property (used against Relations)");
		}
		
		public void doRebuild(FWFacade facade) throws DataException {
			Vector<Property> props = 
			 getAll(getAllData(facade));
			
			HashMap<Integer, Property> newCache = 
			 new HashMap<Integer, Property>();
			
			for (Property prop : props) {
				newCache.put(prop.getPropertyId(), prop);
			}
			
			this.CACHE_MAP = newCache;
		}
	}
}
