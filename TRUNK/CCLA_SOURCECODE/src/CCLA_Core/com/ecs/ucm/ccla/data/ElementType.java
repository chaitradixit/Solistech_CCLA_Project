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

/** Models entries from the REF_ELEMENT_TYPE table. 
 *
 **/
public class ElementType {
	
	private int elementTypeId;
	private String name; /* Name of the element type, e.g. 'Person' */
	
	/* All element types are hard-coded here for easy reference. Should match 
	 * with REF_ELEMENT_TYPE table exactly (apart from the first 'Element' entry, this
	 * is here for auditing purposes only)
	 * 
	 */
	public static final ElementType ELEMENT			= new ElementType(0, "Element");
	public static final ElementType ORGANISATION 	= new ElementType(1, "Organisation");
	public static final ElementType PERSON 			= new ElementType(2, "Person");;
	public static final ElementType ACCOUNT 		= new ElementType(3, "Account");
	public static final ElementType BANK_ACCOUNT 	= new ElementType(4, "BankAccount");

	public static final class Cols {
		public static final String ID 	= "ELEMENT_TYPE_ID";
		public static final String NAME = "ELEMENT_TYPE_NAME";
	}
	
	/** 'Secondary' element types aren't currently listed in the 
	 * REF_ELEMENT_TYPE table, because they don't refer to the shared 
	 * ELEMENT_ID key sequence. The names are enumerated here for
	 * auditing purposes.
	 * 
	 */
	public static enum SecondaryElementType {
	    Relation, Address, ContactPoint, 
	    ClientAuroraMap, PersonAuroraMap, 
	    AccountFlag,
	    ElementAttributeApplied,
	    ElementIdentifierApplied,
	    RelationPropertyApplied,
	    ShareClass,
	    ShareClassOverride,
	    Document,
	    ShareClassGroup, 
	    ShareClassGroupApplied,
	    Campaign, CampaignEnrolment, CampaignActivity, Form,
	    FundInvestmentIntention, Comm, CommGroup, Instruction,
	    EnrolmentAttributeApplied, FundEOD
	}
	
	public ElementType(int elementTypeId, String name) {
		this.elementTypeId = elementTypeId;
		this.name = name;
	}
	
	public static Vector<ElementType> getAll(FWFacade facade) throws DataException {
		DataResultSet rs = facade.createResultSet
		 ("qCore_GetAllElementTypes", new DataBinder());
		
		Vector<ElementType> elemTypes = new Vector<ElementType>();
		
		if (!rs.isEmpty()) {
			do {
				elemTypes.add(get(rs));
			} while (rs.next());
		}
		
		return elemTypes;
	}
	
	public static ElementType get(DataResultSet rsType) throws DataException {
		return new ElementType(
			CCLAUtils.getResultSetIntegerValue
			 (rsType, Cols.ID),
			 rsType.getStringValueByName(Cols.NAME)
		);
	}
	
	public void setElementTypeId(int elementTypeId) {
		this.elementTypeId = elementTypeId;
	}
	public int getElementTypeId() {
		return elementTypeId;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	
	public boolean equals(ElementType elementType) {
		return (this.getElementTypeId() == elementType.getElementTypeId());
	}
	
	public String toString() {
		return this.getName();
	}
	
	/*
	 *  Caching stuff
	 */
	private static Cache CACHE = new Cache();
	
	public static Cachable<Integer, ElementType> getCache() {
		return CACHE;
	}
	
	/** ElementType cache implementor */
	private static class Cache extends Cachable<Integer, ElementType> {

		public Cache() {
			super("Element Type");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<ElementType> elemTypes = ElementType.getAll(facade);
			
			HashMap<Integer, ElementType> newCache = 
			 new HashMap<Integer, ElementType>();
			
			for (ElementType elemType : elemTypes) {
				newCache.put(elemType.getElementTypeId(), elemType);
			}
			
			this.CACHE_MAP = newCache;
		}
	}
}
