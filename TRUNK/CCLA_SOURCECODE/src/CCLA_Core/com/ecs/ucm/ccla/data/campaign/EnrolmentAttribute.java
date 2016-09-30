package com.ecs.ucm.ccla.data.campaign;

import java.util.HashMap;
import java.util.Vector;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

/** Models entries from the REF_ENROLMENT_ATTRIBUTE table.
 *  
 * @author Tom
 *
 */
public class EnrolmentAttribute {
	
	public static class Ids {
		public static final int BANK_ACCOUNT 			= 1;
		public static final int NUMBER_OF_SIGNATURES 	= 2;
		public static final int MIN_AUTH_PERSONS 		= 3;
		public static final int MIN_SIGNATORIES 		= 4;
		public static final int ACCOUNT 				= 5;
		
	}
	
	public static class Cols {
		public static final String ID = "ENROLMENT_ATTRIBUTE_ID";
		public static final String NAME = "ENROLMENT_ATTRIBUTE_NAME";
	}
	
	private int id;
	private String name;
	
	public EnrolmentAttribute(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public static EnrolmentAttribute get(DataResultSet rs) throws DataException {
		if (rs.isEmpty())
			return null;
		
		return new EnrolmentAttribute(
			CCLAUtils.getResultSetIntegerValue(rs, Cols.ID),
			rs.getStringValueByName(Cols.NAME)
		);
	}

	public static Vector<EnrolmentAttribute> getAll(FWFacade facade) 
	 throws DataException {
		
		DataResultSet rs = facade.createResultSet
		 ("qCore_GetAllEnrolmentAttributes", new DataBinder());
		
		Vector<EnrolmentAttribute> v = new Vector<EnrolmentAttribute>();
		
		if (rs.first()) {
			do {
				v.add(get(rs));
			} while (rs.next());
		}
		
		return v;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	/*
	 *  Caching stuff
	 */
	private static Cache CACHE = new Cache();
	
	/** Cache of Campaign IDs to Vectors of ApplicableEnrolmentAttributes.
	 * 
	 * @return
	 */
	public static Cachable<Integer, EnrolmentAttribute> getCache() {
		return CACHE;
	}
	
	/** ElementType cache implementor */
	private static class Cache extends Cachable<Integer, EnrolmentAttribute> {

		public Cache() {
			super("Enrolment Attribute");
		}
		
		public void doRebuild(FWFacade facade) throws DataException {
			Vector<EnrolmentAttribute> attribs = 
			 getAll(facade);
			
			HashMap<Integer, EnrolmentAttribute> newCache = 
			 new HashMap<Integer, EnrolmentAttribute>();
			
			for (EnrolmentAttribute attrib : attribs) {
				newCache.put(attrib.getId(), attrib);
			}
			
			this.CACHE_MAP = newCache;
		}
	}
}
