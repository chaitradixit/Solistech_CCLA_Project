package com.ecs.ucm.ccla.data;

import java.util.HashMap;
import java.util.Vector;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.CacheManager;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Models entries in the core ELEMENT table.
 * 
 * 
 */
public class Element implements Persistable {
	
	private int elementId;
	private ElementType type;
	
	private int preferredContactMethod;
	private boolean doNotContact;
	
	private Double kycScore;
	private DataSource dataSource;
	
	private String notes;
	
	/** Default preferred contact method (address) */
	public static final int DEFAULT_PREFERRED_CONTACT_METHOD = Contact.ADDRESS;
	
	/** Default preferred contact method (address) */
	public static final boolean DEFAULT_DO_NOT_CONTACT_FLAG = false;
	
	public static final class Cols {
		public static final String ID 						= "ELEMENT_ID";
		public static final String PREFERRED_CONTACTMETHOD 	= "PREFERRED_CONTACTMETHOD";
		public static final String KYC_SCORE				= "KYC_SCORE";
		public static final String DO_NOT_CONTACT_FLAG 		= "DO_NOT_CONTACT_FLAG";
		public static final String SOURCE_ID				= "SOURCE_ID";
		public static final String NOTES 					= "NOTES";
	}
	
	public Element() {
	}
	
	/** Creates a new Person instance using any passed fields from
	 *  the binder. Will not set the PERSON_ID.
	 *  
	 * @param binder
	 * @throws DataException
	 */
	public Element(DataBinder binder) throws DataException {
		this();
	}
	
	public Element(int entityId, ElementType type) {
		this(entityId, type, DEFAULT_PREFERRED_CONTACT_METHOD, 
		 false, null, null, null);
	}
	
	public Element(int entityId, ElementType type, 
	 int preferredContactMethod, boolean doNotContact, 
	 Double kycScore, DataSource dataSource, String notes) {
		
		this.elementId = entityId;
		this.type = type;
		
		this.preferredContactMethod = preferredContactMethod;
		this.doNotContact = doNotContact;
		
		this.setKycScore(kycScore);
		this.setDataSource(dataSource);
		
		this.notes = notes;
	}
	
	public Element(Element element) {
		this.elementId = element.getElementId();
		this.type = element.getType();
		
		this.preferredContactMethod = element.getPreferredContactMethod();
		this.doNotContact = element.isDoNotContact();
		
		this.setKycScore(element.getKycScore());
		this.setDataSource(element.getDataSource());
		
		this.notes = element.getNotes();
	}
	
	public static Element get(int elementId, FWFacade facade) 
	 throws DataException {
		
		DataResultSet rsElement = getData(elementId, facade);
		return get(rsElement);
	}
	
	public static Element get(DataResultSet rsElement) throws DataException {
		
		if (rsElement.isEmpty())
			return null;
		
		DataSource dataSource = null;
		
		Integer dataSourceId = CCLAUtils.getResultSetIntegerValue
		 (rsElement, DataSource.Cols.ID);

		if (dataSourceId != null)
			dataSource = DataSource.getCache().getCachedInstance(dataSourceId);
		
		return new Element(
			CCLAUtils.getResultSetIntegerValue(rsElement, Cols.ID),
			ElementType.getCache().getCachedInstance(
				CCLAUtils.getResultSetIntegerValue
				 (rsElement, ElementType.Cols.ID)
			),
			
			CCLAUtils.getResultSetIntegerValue
			 (rsElement, Cols.PREFERRED_CONTACTMETHOD),
			 CCLAUtils.getResultSetBoolValue
			  (rsElement, Cols.DO_NOT_CONTACT_FLAG),
			 
			CCLAUtils.getResultSetDoubleValue
			 (rsElement, Cols.KYC_SCORE),
			 
			dataSource,
			
			rsElement.getStringValueByName(Cols.NOTES)
		);
	}
	
	/** Returns a list of Element instances for the passed list of Element IDs.
	 *  
	 * @param elementIds
	 * @return
	 * @throws DataException 
	 */
	public static Vector<Element> getAll
	 (Vector<Integer> elementIds, FWFacade facade) throws DataException {
		
		DataResultSet rs = getAllData(elementIds, facade);
		Vector<Element> elements = new Vector<Element>();
		
		if (rs.first()) {
			do {
				elements.add(get(rs));
			} while (rs.next());
		}
		
		return elements;
	}
	
	public static DataResultSet getAllData
	 (Vector<Integer> elementIds, FWFacade facade) throws DataException {
		
		if (elementIds.isEmpty())
			throw new DataException("Unable to fetch Elements data: no Element IDs " +
			 "passed");
		
		DataBinder binder = new DataBinder();
		
		String csvElementIds = CCLAUtils.getCSVFromIntegerList(elementIds);
		binder.putLocal("ELEMENT_IDS", csvElementIds);
		
		return facade.createResultSet("qClientServices_GetElements", binder);
	}
	
	public static DataResultSet getData(int elementId, FWFacade facade) 
	 throws DataException {
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.ID, elementId);
		
		DataResultSet rsElement = 
		 facade.createResultSet("qClientServices_GetElement", binder);
		
		return rsElement;
	}
	
	/* Inserts a new Element entry of the given type into the database and 
	 * returns the equivalent Element instance.
	 */
	public static Element add(ElementType type, DataSource dataSource, 
	 FWFacade facade, String userName) throws DataException {
		return add(type, dataSource, false, null, null, facade, userName);
	}
 
	/* Inserts a new Element entry of the given type into the database and 
	 * returns the equivalent Element instance.
	 */
	public static Element add(ElementType type, DataSource dataSource, 
	 boolean doNotContactFlag, Integer preferredContactMethod, String notes, 
	 FWFacade facade, String userName) throws DataException {
		
		int elementId = Integer.parseInt(
		 CCLAUtils.getNewKey("Element", facade));
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.ID, elementId);

		CCLAUtils.addQueryIntParamToBinder
		 (binder, ElementType.Cols.ID, type.getElementTypeId());
		
		if (preferredContactMethod == null)
			preferredContactMethod = DEFAULT_PREFERRED_CONTACT_METHOD;
			
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.PREFERRED_CONTACTMETHOD, preferredContactMethod);
		
		CCLAUtils.addQueryBooleanParamToBinder
		 (binder, Cols.DO_NOT_CONTACT_FLAG, doNotContactFlag);
		
		Integer dataSourceId = null;
		
		if (dataSource != null)
			dataSourceId = dataSource.getDataSourceId();
		
		CCLAUtils.addQueryIntParamToBinder(binder, 
		 DataSource.Cols.ID, dataSourceId);
		
		CCLAUtils.addQueryParamToBinder(binder, Cols.NOTES, notes);
		
		facade.execute("qClientServices_AddElement", binder);
		
		DataResultSet newData 	= Element.getData(elementId, facade);
		Element element	 		= get(newData);
		
		// Add audit record
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(element.getElementId(), ElementType.ELEMENT.getName());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, userName, 
		 Globals.AuditActions.UPDATE.toString(), 
		 ElementType.ELEMENT.getName(), null, newData, auditRelations);			
		
		return element;
	}	
	
	public int getElementId() {
		return elementId;
	}

	public ElementType getType() {
		return type;
	}

	public void setPreferredContactMethod(int preferredContactMethod) {
		this.preferredContactMethod = preferredContactMethod;
	}

	public int getPreferredContactMethod() {
		return preferredContactMethod;
	}

	public void setDoNotContact(boolean doNotContact) {
		this.doNotContact = doNotContact;
	}

	public boolean isDoNotContact() {
		return doNotContact;
	}
	
	public Double getKycScore() {
		return kycScore;
	}
	
	public void setKycScore(Double kycScore) {
		this.kycScore = kycScore;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void addFieldsToBinder(DataBinder binder) throws DataException {
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.ID, this.getElementId());
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, ElementType.Cols.ID, this.getType().getElementTypeId());
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.PREFERRED_CONTACTMETHOD, this.getPreferredContactMethod());
		
		CCLAUtils.addQueryBooleanParamToBinder
		 (binder, Cols.DO_NOT_CONTACT_FLAG, this.isDoNotContact());
		
		CCLAUtils.addQueryParamToBinder(binder, Cols.NOTES, this.getNotes());
	}

	public void persist(FWFacade facade, String username) throws DataException {

		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);
		
		facade.execute("qClientServices_UpdateElement", binder);
	}

	public void setAttributes(DataBinder binder) throws DataException {

		this.setPreferredContactMethod(
		 CCLAUtils.getBinderIntegerValue
		 (binder, Cols.PREFERRED_CONTACTMETHOD));
		
		this.setDoNotContact(
		 CCLAUtils.getBinderBoolValue
		 (binder, Cols.DO_NOT_CONTACT_FLAG));
		
		this.setNotes(binder.getLocal(Cols.NOTES));
	}

	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub
		
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return this.getElementId();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (this == obj) return true;
		
		return (obj instanceof Element
			&&
			this.getElementId() == ((Element) obj).getElementId());
	}
	
	/** Returns the display name for this Element. Should be implemented by subclasses
	 *  to return something sensible. 
	 *  
	 * @return
	 */
	public String getName() {
		return "[Element]";
	}
}
