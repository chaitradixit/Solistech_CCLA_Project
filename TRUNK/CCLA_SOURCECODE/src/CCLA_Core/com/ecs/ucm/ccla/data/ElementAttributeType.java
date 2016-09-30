package com.ecs.ucm.ccla.data;

import java.util.HashMap;
import java.util.Vector;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.embedded.FWFacade;

public class ElementAttributeType implements Persistable {
	
	private int elementAttributeTypeId;
	private String name;
	private String description;
	
	public class Cols {
		public static final String ID 			= "ELEMENT_ATTRIBUTE_TYPE_ID";
		public static final String NAME 		= "ELEMENT_ATTRIBUTE_NAME";
		public static final String DESCRIPTION 	= "ELEMENT_ATTRIBUTE_TYPE_DESC";
	}
	
	/* Hard-coded references to existing ELEMENT_ATTRIBUTE_TYPE_IDs. Use these to fetch
	 *  Element Attribute Types from the cache.
	 */
	public static final int ENTITY_VERIFICATION 		= 1;
	public static final int RELATIONSHIP_VERIFICATION 	= 2;
	public static final int ACCOUNT_IVS_CHECKING		= 3;
	public static final int MARKETING_DETAILS			= 4;
	public static final int MISC_ACCOUNT_DETAILS		= 5;
	public static final int MISC_ORG_DETAILS			= 6;
	public static final int PERSON_IVS_CHECKING			= 7;
	
	public ElementAttributeType(int elementAttributeTypeId, String name,
			String description) {
		this.elementAttributeTypeId = elementAttributeTypeId;
		this.name = name;
		this.description = description;
	}
	
	public static ElementAttributeType get(DataResultSet rs) throws DataException {
		return new ElementAttributeType(
			CCLAUtils.getResultSetIntegerValue(rs, Cols.ID),
			CCLAUtils.getResultSetStringValue(rs, Cols.NAME),
			CCLAUtils.getResultSetStringValue(rs, Cols.DESCRIPTION)
		);
	}
	
	public static Vector<ElementAttributeType> getAll(FWFacade facade) 
	 throws DataException {
		
		Vector<ElementAttributeType> elemAttribTypes = 
		 new Vector<ElementAttributeType>();
		
		DataResultSet rs = facade.createResultSet
		 ("qCore_GetElementAttributeTypes", new DataBinder());
		
		if (rs.first()) {
			do {
				elemAttribTypes.add(get(rs));
			} while (rs.next());
		}
		
		return elemAttribTypes;
	}
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		// TODO Auto-generated method stub

	}

	public void persist(FWFacade facade, String username) throws DataException {
		// TODO Auto-generated method stub

	}

	public void setAttributes(DataBinder binder) throws DataException {
		// TODO Auto-generated method stub

	}

	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub

	}

	public int getElementAttributeTypeId() {
		return elementAttributeTypeId;
	}

	public void setElementAttributeTypeId(int elementAttributeTypeId) {
		this.elementAttributeTypeId = elementAttributeTypeId;
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
	
	public boolean equals(ElementAttributeType attribType) {
		return (this.getElementAttributeTypeId() 
				== 
				attribType.getElementAttributeTypeId());
	}

	/*
	 *  Caching stuff
	 */
	private static Cache CACHE = new Cache();
	
	public static Cachable<Integer, ElementAttributeType> getCache() {
		return CACHE;
	}
	
	/** ElementAttributeType cache implementor */
	private static class Cache extends Cachable<Integer, ElementAttributeType> {

		public Cache() {
			super("Element Attribute Type");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<ElementAttributeType> types = ElementAttributeType.getAll(facade);
			
			HashMap<Integer, ElementAttributeType> newCache = 
			 new HashMap<Integer, ElementAttributeType>();
			
			for (ElementAttributeType type : types) {
				newCache.put(type.getElementAttributeTypeId(), type);
			}
			
			this.CACHE_MAP = newCache;
		}
	}
}
