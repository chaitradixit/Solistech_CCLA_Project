package com.ecs.ucm.ccla.data;

import java.util.HashMap;
import java.util.Vector;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Models entries from the REF_ELEMENT_ATTRIBUTES table
 * 
 * @author Tom
 *
 */
public class ElementAttribute implements Persistable {

	private int attributeId;
	private String name;
	private String description;
	
	private ElementType elementType;
	private Integer verificationSourceId;
	private Integer dependantIdentifierId;
	
	private String[] userGroups;
	
	/** Indicates whether or not this attribute can be set freely by a user. */
	private boolean setByUser;
	
	private DataType dataType;
	private ElementAttributeType attributeType;
	
	public static class Cols {
		public static final String ID = "ELEMENT_ATTRIBUTE_ID";
	}
	
	public static class PersonAttributes {		
		public static final int IDENTITY_CHECKED = 7;
		public static final int PEP_CHECKED = 8;
		public static final int PEP_CHECK_OVERRIDE = 37;
		public static final int ACCOUNT_IVS_OVERRIDE = 39;
		public static final int AML_TRACKER_CHECKED = 50;
	}
	
	public static class AccountAttributes {		
		public static final int PSDF_SEED_FUND = 35;
		public static final int PSDF_EARLY_INVESTOR = 36;
		public static final int INTERNAL_ACCOUNT = 40;
		public static final int USE_CURRENT_SIG_LIST = 47;
		public static final int NO_MANDATE_RECEIVED_SINCE_OCT_2010 = 49;
		public static final int STANDING_TRANSACTIONS = 51;
		public static final int CF_COLLECTION_ACCOUNT_TYPE = 63;
		
		public static final int DONOR_ELEMENT_ID = 65;
	}
	

	public static class OrganisationAttributes {
		public static final int EMAIL_INDEMNITY_REQUESTED = 45;
		public static final int EMAIL_INDEMNITY_RECEIVED = 46;
		
		public static final int DIO_LOAN_RESOLUTION_RECEIVED = 48;
		
		public static final int FINANCIAL_YEAR_END = 54;
		public static final int ACCOUNTS_SUBJECT_TO_AUDIT = 55;
		public static final int LATEST_FINANCIAL_ACCOUNTS_URL = 56;
		public static final int BALANCE_SHEET_AT_LEAST_20M_EUROS = 57;
		public static final int NET_TURNOVER_AT_LEAST_40M_EUROS = 58;
		public static final int FUND_OWNERSHIP_AT_LEAST_2M_EUROS = 59;		
		public static final int REG_AS_LIMITED_COMPANY = 60;		
		public static final int LIMITED_COMPANY_NAME = 61;
		
		// Verification attributes
		public static final int VERIFIED_BY_AML_TRACKER = 11;
		
		public static final int LIABLE = 64; 
	}
	
	/** Filter parameter used when fetching lists of Atttributes.
	 *
	 *  ALL: fetches all attributes.
	 *  NON_VERIFY_ONLY: fetches all attributes which do not have a Verification Source
	 *  set
	 *  VERIFY_ONLY: fetches all attributes which have a Verification Source set					
	 *
	 * @author Tom
	 *
	 */
	public static enum SelectionType {
		ALL,
		NON_VERIFY_ONLY,
		VERIFY_ONLY,
	}
	
	public ElementAttribute(int attributeId, String name, String description,
			ElementType elementType, Integer verificationSourceId,
			Integer dependantIdentifierId, boolean setByUser, String[] userGroups,
			DataType dataType, ElementAttributeType attributeType) {
		this.attributeId = attributeId;
		this.name = name;
		this.description = description;
		this.elementType = elementType;
		this.verificationSourceId = verificationSourceId;
		this.dependantIdentifierId = dependantIdentifierId;
		this.setByUser = setByUser;
		this.dataType = dataType;
		this.attributeType = attributeType;
	}
	
	/** Returns a ResultSet containing all available element attributes for the given 
	 *  Element Type and selection criteria.
	 * 
	 *  If the passed Element Type is null, attributes are fetched for all Element 
	 *  Types.
	 * 
	 * @param facade
	 * @return
	 * @throws DataException 
	 * @throws DataException
	 */
	public static DataResultSet getElementAttributesData(ElementType elemType, 
	 SelectionType selType, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		
		if (elemType == null 
			&& 
			(selType == null || selType.equals(SelectionType.ALL))) {
			// Fetch every single Element Attribute.
			return facade.createResultSet
			 ("qClientServices_GetElementAttributes", binder);
		}
		
		// Element Type-specific queries
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "ELEMENT_TYPE_ID", elemType.getElementTypeId());
		
		if (selType == null || selType.equals(SelectionType.ALL))
			// Fetch all attributes for the given Element Type.
			return facade.createResultSet
			 ("qClientServices_GetElementAttributesWithElementType", binder);
		else if (selType.equals(SelectionType.VERIFY_ONLY)) 
			return facade.createResultSet
			 ("qClientServices_GetElementAttributesWithVerificationSource", binder);
		else if (selType.equals(SelectionType.NON_VERIFY_ONLY))
			return facade.createResultSet
			 ("qClientServices_GetElementAttributesWithoutVerificationSource", binder);
		else 
			throw new DataException("Invalid selection type");
	}
	
	/** Returns a list of ElementAttributes containing all available element attributes 
	 *  for the given Element Type and selection criteria.
	 * 
	 * @param facade
	 * @return
	 * @throws DataException 
	 * @throws DataException
	 */
	public static Vector<ElementAttribute> getElementAttributes(ElementType elemType, 
	 SelectionType selType, FWFacade facade) 
	 throws DataException {
		
		DataResultSet rs = getElementAttributesData
		 (elemType, selType, facade);
		
		return getAll(rs);
	}
	
	private static Vector<ElementAttribute> getAll(DataResultSet rs) 
	 throws DataException {
		
		Vector<ElementAttribute> elemAttribs = new Vector<ElementAttribute>();
		
		if (rs.first()) {
			do {
				elemAttribs.add(get(rs));
			} while (rs.next());
		}
		
		return elemAttribs;
	}
	
	public static ElementAttribute get(DataResultSet rs) throws DataException {
		
		if (rs.isEmpty())
			return null;
		
		String[] userGroups = null;
		String userGroupsStr = rs.getStringValueByName("USER_GROUPS");
		
		if (!StringUtils.stringIsBlank(userGroupsStr))
			userGroups = userGroupsStr.split(",");
			
		return new ElementAttribute(
		 CCLAUtils.getResultSetIntegerValue
		  (rs, Cols.ID),
		  
		 rs.getStringValueByName("ELEMENT_ATTRIBUTE_NAME"),
		 rs.getStringValueByName("ELEMENT_ATTRIBUTE_DESCRIPTION"),
		 
		 ElementType.getCache().getCachedInstance
		  (CCLAUtils.getResultSetIntegerValue(rs, "ELEMENT_TYPE_ID")),
		  
		 CCLAUtils.getResultSetIntegerValue(rs, "VERIFICATION_SOURCE_ID"),
		  
		 CCLAUtils.getResultSetIntegerValue(rs, "ELEMENT_IDENTIFIER_ID"),
		 CCLAUtils.getResultSetBoolValue(rs, "SET_BY_USER"),
		 
		 userGroups,
		 
		 DataType.getCache().getCachedInstance(
		  CCLAUtils.getResultSetStringValue(rs, "ELEMENT_ATTRIBUTE_DATA_TYPE")),
		  
		 ElementAttributeType.getCache().getCachedInstance(
		  CCLAUtils.getResultSetIntegerValue(rs, ElementAttributeType.Cols.ID))
		);
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

	}

	public int getAttributeId() {
		return attributeId;
	}

	public void setAttributeId(int attributeId) {
		this.attributeId = attributeId;
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

	public ElementType getElementType() {
		return elementType;
	}

	public void setElementType(ElementType elementType) {
		this.elementType = elementType;
	}

	public Integer getDependantIdentifierId() {
		return dependantIdentifierId;
	}

	public void setDependantIdentifierId(Integer dependantIdentifierId) {
		this.dependantIdentifierId = dependantIdentifierId;
	}

	public boolean isSetByUser() {
		return setByUser;
	}

	public void setSetByUser(boolean setByUser) {
		this.setByUser = setByUser;
	}

	public void setVerificationSourceId(Integer verificationSourceId) {
		this.verificationSourceId = verificationSourceId;
	}

	public Integer getVerificationSourceId() {
		return verificationSourceId;
	}

	public void setUserGroups(String[] userGroups) {
		this.userGroups = userGroups;
	}

	public String[] getUserGroups() {
		return userGroups;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public DataType getDataType() {
		return dataType;
	}

	public void setAttributeType(ElementAttributeType attributetType) {
		this.attributeType = attributetType;
	}

	public ElementAttributeType getAttributeType() {
		return attributeType;
	}
	
	/*
	 *  Caching stuff
	 */
	private static Cache CACHE = new Cache();
	
	/** Cache of Element Attribute ID to ElementAttribute instances.
	 * 
	 * @return
	 */
	public static Cachable<Integer, ElementAttribute> getCache() {
		return CACHE;
	}
	
	/** ElementType cache implementor */
	private static class Cache extends Cachable<Integer, ElementAttribute> {

		public Cache() {
			super("Element Attribute");
		}
		
		public void doRebuild(FWFacade facade) throws DataException {
			Vector<ElementAttribute> attribs = 
			 getElementAttributes(null, SelectionType.ALL, facade);
			
			HashMap<Integer, ElementAttribute> newCache = 
			 new HashMap<Integer, ElementAttribute>();
			
			for (ElementAttribute attrib : attribs) {
				newCache.put(attrib.getAttributeId(), attrib);
			}
			
			this.CACHE_MAP = newCache;
		}
	}
}
