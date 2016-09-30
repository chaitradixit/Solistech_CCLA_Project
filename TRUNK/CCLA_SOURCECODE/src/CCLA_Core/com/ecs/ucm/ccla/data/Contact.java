package com.ecs.ucm.ccla.data;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals.UCMFieldNames;
import com.ecs.ucm.ccla.data.audit.SDAudit;
import com.ecs.ucm.ccla.data.instruction.InstructionType;
import com.ecs.ucm.ccla.data.staticdataupdate.StaticDataUpdate;
import com.ecs.ucm.ccla.data.staticdataupdate.StaticDataUpdateApplied;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Models entries from the CONTACT_POINT table 
 * 
 * @author Tom
 *
 */
public class Contact implements Persistable {
	
	public static final int DEFAULT_AUTH_STATUS_ID = 1;
	
	// List of entries from the REF_CONTACT_METHOD table. This
	// must be kept in sync or externalised to point directly at the
	// table data.
	public static final int ADDRESS = 1;
	public static final int PHONE = 2;
	public static final int EMAIL = 3;
	public static final int WEB = 4;
	public static final int FAX = 5;
	
	// List of entries from the REF_CONTACT_SUBMETHOD table. This
	// must be kept in sync or externalised to point directly at the
	// table data.
	public static final int SUBMETHOD_PHONE_PERSONAL = 100;
	public static final int SUBMETHOD_PHONE_BUSINESS = 101;
	public static final int SUBMETHOD_PHONE_MOBILE = 102;
	public static final int SUBMETHOD_ADDRESS_BUSINESS = 5;
	public static final int SUBMETHOD_ADDRESS_HOME = 1;
	public static final int SUBMETHOD_ADDRESS_PREVIOUS = 3;
	public static final int SUBMETHOD_ADDRESS_LIMITED_COMPANY = 6;
	public static final int SUBMETHOD_EMAIL_BUSINESS = 201;
	public static final int SUBMETHOD_EMAIL_PERSONAL = 200;
	public static final int SUBMETHOD_WEBSITE_BUSINESS = 301;
	
	private int contactId;
	
	/* Referenced element ID */
	private Integer elementId;
	
	/* Referenced relation ID */
	private Integer relationId;
	
	// Stores the id of the sub method, e.g. 'Home phone number' (stored 
	// in the REF_CONTACT_SUBMETHOD table). This in turn points to a row 
	// in table REF_CONTACT_METHOD which will store the value of the method, 
	// e.g. 'Phone' 
	private int submethodId;
	private int methodId;
	
	private String value;
	
	private Integer addressId;
	private Address address = null;
	
	private boolean isDefault;
	private boolean isDubious;
	private boolean isExperian;
	private boolean doNotContact;
	
	private Date lastUpdated;
	
	private AuthStatus authStatus;
	
	public static class Cols {
		public static final String ID 			= "CONTACT_ID";
		public static final String SUBMETHOD 	= "SUBMETHOD_ID";
		public static final String IS_DEFAULT	= "IS_DEFAULT";
	}
	
	public Contact(DataBinder binder) throws DataException {
		this.setAttributes(binder);
	}
	
	public Contact(Integer contactId, Integer elementId,  Integer relationId, 
			int submethodId, String value, Integer addressId, Address address,
			boolean isDefault, boolean isDubious, boolean isExperian, 
			boolean doNotContact, Date lastUpdated, AuthStatus authStatus, 
			FWFacade facade) 
			throws DataException{
	
		this.contactId = contactId;
		
		this.elementId = elementId;
		this.relationId = relationId;

		this.submethodId = submethodId;
		this.methodId = getMethodIdFromSubmethodId(submethodId,facade);
		
		this.value = value;
		this.addressId = addressId;
		this.setAddress(address);
		
		this.isDefault = isDefault;
		this.isDubious = isDubious;
		this.isExperian = isExperian;
		this.doNotContact = doNotContact;
		
		this.lastUpdated = lastUpdated;
		
		this.authStatus = authStatus;
	}

	public void validate(FWFacade facade) throws DataException {
		
		if (this.getElementId() == null && this.getRelationId() == null)
			throw new DataException("No target element/relation ID");
		
		if (this.getElementId() != null && this.getRelationId() != null)
			throw new DataException("Ambiguous target - element and relation " +
			 "IDs present");
	}
	
	public static Contact add(DataBinder binder, FWFacade facade, String userName)
	 throws DataException {
		Contact contact = new Contact(binder);
		
		// Fetch new ID for this contact.
		String contactId = CCLAUtils.getNewKey("Contact", facade);
		contact.setContactId(Integer.parseInt(contactId));
		
		return add(contact, facade, userName);
	}
	
	/** Copies the given Contact (and linked Address) to the given Element ID or 
	 *  Relation ID. Either Element or Relation ID must be specified but not both.
	 *  
	 *  If the passed subMethodId is not null, it will overwrite the source Contact's
	 *  sub-method ID.
	 *  
	 * @param srcContact
	 * @param elementId
	 * @param relationId
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static Contact copy
	 (Contact srcContact, Integer elementId, Integer relationId, Integer subMethodId,
	 FWFacade facade, String userName) throws DataException {

		Address srcAddress = srcContact.getAddress();
		Address newAddress = null;
		
		if (srcAddress != null) {
			// Make a copy of the source Contact address.
			newAddress = Address.copy(srcAddress, facade, userName);
		}
		
		return add(
			elementId, 
			relationId, 
			subMethodId != null ? subMethodId : srcContact.getSubmethodId(),
			srcContact.getValue(),
			newAddress != null ? newAddress.getAddressId() : null,
			false,
			srcContact.isDubious(),
			false,
			false,
			srcContact.getAuthStatus(),
			facade,
			userName
		);
	}
	
	public static Contact add(Integer elementId, Integer relationId, int submethodId, 
	 String value, Integer addressId, boolean isDefault, boolean isDubious, 
	 boolean isExperian, boolean doNotContact, AuthStatus authStatus, 
	 FWFacade facade, String userName) 
	 throws DataException {

		// Fetch new ID for this contact.
		String contactId = CCLAUtils.getNewKey("Contact", facade);
		
		if (authStatus == null)
			authStatus = AuthStatus.getCache().getCachedInstance
			 (DEFAULT_AUTH_STATUS_ID);
		
		Contact contact = new Contact(
		 Integer.parseInt(contactId),
		 elementId,
		 relationId,
		 submethodId,
		 value,
	 	 addressId,
	 	 null,
	     isDefault,
	     isDubious,
	     isExperian,
	     doNotContact,
	     null,
	     authStatus,
	     facade);
		
		return add(contact, facade, userName);
	}
	
	/** Adds a fully-specified Contact instance to the database.
	 * 
	 * @param contact
	 * @param facade
	 * @param userName
	 * @return
	 * @throws DataException
	 */
	private static Contact add(Contact contact, FWFacade facade, String userName) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		contact.addFieldsToBinder(binder);
		
		contact.validate(facade);
		
		facade.execute("qClientServices_AddContact", binder);
		
		// Add audit record
		DataResultSet newData = Contact.getData(contact.getContactId(), facade);
		
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(contact.getContactId(), 
		 ElementType.SecondaryElementType.ContactPoint.toString());
		
		if (contact.getElementId() != null) {
			// Determine the element type this Contact Point was linked to.
			Element elem = Element.get(contact.getElementId(), facade);
			
			auditRelations.put(elem.getElementId(), elem.getType().getName());
			
			Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, userName, 
			 Globals.AuditActions.ADD.toString(), 
			 ElementType.SecondaryElementType.ContactPoint.toString(), 
			 null, newData, auditRelations);
		} else {
			// Assume Contact Point was linked to Relation
			auditRelations.put(contact.getRelationId(), 
			 ElementType.SecondaryElementType.Relation.toString());
			
			Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, userName, 
			 Globals.AuditActions.ADD.toString(), 
			 ElementType.SecondaryElementType.ContactPoint.toString(), 
			 null, newData, auditRelations);
		}
		
		return contact;
	}
	
	/** Removes an existing Contact record. This will also
	 *  delete the associated Address record if applicable, via the
	 *  ON DELETE CASCADE database property.
	 * 
	 * @param contactId
	 * @param facade
	 * @throws DataException 
	 */
	public static void remove(int contactId, FWFacade facade, 
	 String userName) throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "CONTACT_ID", contactId);
	
		Contact thisContact = Contact.get(contactId, facade);
		thisContact.addFieldsToBinder(binder);
		
		// Fetch current data for auditing before deletion
		DataResultSet oldData = Contact.getData(contactId, facade);
		// Determine the element type this Contact Point was linked to.
		int elementId = CCLAUtils.getResultSetIntegerValue
		 (oldData, "ELEMENT_ID");
		Element elem = Element.get(elementId, facade);
		
		facade.execute("qClientServices_RemoveContact", binder);

		// Add 'delete' audit record
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(contactId, 
		 ElementType.SecondaryElementType.ContactPoint.toString());
		auditRelations.put(elem.getElementId(), elem.getType().getName());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, userName, 
		 Globals.AuditActions.DELETE.toString(), 
		 ElementType.SecondaryElementType.ContactPoint.toString(),
		 oldData, null, auditRelations);
	}
	
	/** Fetches a set of Contact instances which correspond to a given element ID.
	 */  
	public static Vector<Contact> getElementContacts(int elementId, FWFacade facade) 
	 throws DataException {
		
		Vector<Contact> contacts = new Vector<Contact>();
		DataResultSet rsElementContacts = getElementContactsData(elementId, facade);
		
		if (!rsElementContacts.isEmpty()) {
			do {
				Contact contact = Contact.get(rsElementContacts,facade);
				contacts.add(contact);
			} while (rsElementContacts.next());
		}
			
		return contacts;
	}
	
	public static Vector<Contact> getRelationContacts(int relationId, FWFacade facade)
	 throws DataException {
		
		Vector<Contact> contacts = new Vector<Contact>();
		DataResultSet rsRelationContacts = getRelationContactsData(relationId, facade);
		
		if (!rsRelationContacts.isEmpty()) {
			do {
				Contact contact = Contact.get(rsRelationContacts,facade);
				contacts.add(contact);
			} while (rsRelationContacts.next());
		}
			
		return contacts;
	}
	
	/** Fetches a set of contact map entries, joined to the Address table,
	 *  which correspond to a given element ID.
	 *  
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getElementContactsData(int elementId, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(
		 binder, "ELEMENT_ID", elementId);
		
		DataResultSet rsElementContacts =
		 facade.createResultSet("qClientServices_GetElementContacts", binder);
		
		return rsElementContacts;
	}
	
	/** Fetches a set of contact map entries, joined to the Address table,
	 *  which correspond to a given element ID.
	 *  
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getRelationContactsData(int relationId, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(
		 binder, SharedCols.RELATION, relationId);
		
		DataResultSet rsElementContacts =
		 facade.createResultSet("qClientServices_GetRelationContacts", binder);
		
		return rsElementContacts;
	}
	
	/** Returns the default contact of the given type from the passed list of
	 *  Contact instances.
	 *  
	 *  If there are no contacts of the given type, the method returns null.
	 *  
	 *  If there are contacts of the given type, the method will return the
	 *  one flagged as 'default'. If none are marked as default, the first one
	 *  is returned.
	 *  
	 * @param contacts
	 * @param contactType
	 * @return
	 */
	public static Contact getDefaultContact
	 (Vector<Contact> contacts, int methodId) {
		
		return getDefaultContact(contacts, methodId, false);
	}
	
	/** Returns the default contact of the given type from the passed list of
	 *  Contact instances.
	 *  
	 *  If there are no contacts of the given type, the method returns null.
	 *  
	 *  If there are contacts of the given type, the method will return the
	 *  one flagged as 'default'. If none are marked as default, the first one
	 *  is returned, providing the strict flag is false.
	 *  
	 * @param contacts
	 * @param contactType
	 * @return
	 */
	public static Contact getDefaultContact
	 (Vector<Contact> contacts, int methodId, boolean strict) {
		
		Contact defaultContact = null;
		
		for (Contact contact:contacts) {
			if (contact.getMethodId() == methodId) {
				if (contact.isDefault())
					return contact; // default contact found.
				
				if (defaultContact == null && !strict)
					defaultContact = contact;
			}
		}
		
		return defaultContact;
	}
	
	/** Returns the contact point with the given method ID and the Experian flag set.
	 * 
	 * @param contacts
	 * @param methodId
	 * @return null, if no contact of the given method has the flag set
	 */
	public static Contact getExperianContact(Vector<Contact> contacts, int methodId) {
		
		for (Contact contact:contacts) {
			if (contact.getMethodId() == methodId) {
				if (contact.isExperian())
					return contact; // default contact found.
			}
		}
		
		return null;
	}
	
	public static Contact get(DataResultSet rsContact, FWFacade facade) 
	throws DataException {
		
		if (rsContact.isEmpty())
			return null;
		
		Integer addressId = CCLAUtils.getResultSetIntegerValue
		 (rsContact, "ADDRESS_ID");
		
		Address address = null;
		
		// Attempt to fetch linked Address data as well, if it exists
		if (addressId != null)
			address = Address.get(rsContact);

		
		return new Contact(
			CCLAUtils.getResultSetIntegerValue(rsContact, "CONTACT_ID"),
			
			CCLAUtils.getResultSetIntegerValue(rsContact, "ELEMENT_ID"),
			CCLAUtils.getResultSetIntegerValue(rsContact, "RELATION_ID"),
			
			CCLAUtils.getResultSetIntegerValue(rsContact, "SUBMETHOD_ID"),
			rsContact.getStringValueByName("VALUE"),
			addressId,
			address,
			CCLAUtils.getResultSetBoolValue(rsContact, "IS_DEFAULT"),
			CCLAUtils.getResultSetBoolValue(rsContact, "IS_DUBIOUS"),
			CCLAUtils.getResultSetBoolValue(rsContact, "IS_EXPERIAN"),
			CCLAUtils.getResultSetBoolValue(rsContact, "DO_NOT_CONTACT_FLAG"),
			rsContact.getDateValueByName("LAST_UPDATED"),
			AuthStatus.getCache().getCachedInstance(
			 CCLAUtils.getResultSetIntegerValue(rsContact, "AUTH_STATUS_ID")
			),
			facade
		);
		
	}
	
	public static Contact get(int contactId, FWFacade facade) throws DataException {
		
		DataResultSet rsContact = getData(contactId, facade);
		
		if (rsContact.isEmpty())
			return null;
		
		return Contact.get(rsContact,facade);
	}
	
	/** Fetches a single entry from the CCLA_CONTACT_MAP table, left-joined
	 *  to the CCLA_ADDRESS table.
	 *  
	 * @param contactId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getData(int contactId, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, "CONTACT_ID", contactId);
		
		DataResultSet rsContact = 
		 facade.createResultSet("qClientServices_GetContact", binder);
		
		return rsContact;
	}
	
	/**
	 * Returns the submethodId for the given method id.
	 * @param submethodId
	 * @return
	 */
	public int getMethodIdFromSubmethodId(int submethodId, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(
				binder, "SUBMETHOD_ID", submethodId);
		
		DataResultSet rsSubMethodId = facade.createResultSet(
				"qClientServices_GetContactMethodIdFromSubmethodId",binder);
		
		return Integer.parseInt(rsSubMethodId.getStringValueByName("METHOD_ID"));
	}
		
	
	public void setAttributes(DataBinder binder) throws DataException {
		
		this.setSubmethodId(
				CCLAUtils.getBinderIntegerValue(binder, "SUBMETHOD_ID"));
		
		this.setRelationId(
				CCLAUtils.getBinderIntegerValue(binder, "RELATION_ID"));
		
		this.setElementId(
				CCLAUtils.getBinderIntegerValue(binder, "ELEMENT_ID"));
		
		this.setValue(binder.getLocal("VALUE"));
		
		this.setAddressId(
				CCLAUtils.getBinderIntegerValue(binder, "ADDRESS_ID"));
		
		this.setDefault(
				CCLAUtils.getBinderBoolValue(binder, "IS_DEFAULT"));
		
		this.setDubious(
				CCLAUtils.getBinderBoolValue(binder, "IS_DUBIOUS"));
		
		this.setExperian(
				CCLAUtils.getBinderBoolValue(binder, "IS_EXPERIAN"));
		
		this.setDoNotContact(
				CCLAUtils.getBinderBoolValue(binder, "DO_NOT_CONTACT_FLAG"));
		
		this.setAuthStatus(
				AuthStatus.getCache().getCachedInstance(
						CCLAUtils.getBinderIntegerValue(binder, "AUTH_STATUS_ID"))
				);
	}
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		
		CCLAUtils.addQueryIntParamToBinder(
		 binder, "CONTACT_ID", this.getContactId());
		
		CCLAUtils.addQueryIntParamToBinder(
		 binder, "SUBMETHOD_ID", this.getSubmethodId());
		
		CCLAUtils.addQueryIntParamToBinder(
		 binder, "RELATION_ID", this.getRelationId());
		
		CCLAUtils.addQueryIntParamToBinder(
		 binder, "ELEMENT_ID", this.getElementId());

		CCLAUtils.addQueryParamToBinder(
		 binder, "VALUE", this.getValue());
	
		CCLAUtils.addQueryIntParamToBinder(
		 binder, "ADDRESS_ID", this.getAddressId());
	
		CCLAUtils.addQueryBooleanParamToBinder(
		 binder, "IS_DEFAULT", this.isDefault());
		
		CCLAUtils.addQueryBooleanParamToBinder(
		 binder, "IS_DUBIOUS", this.isDubious());
		
		CCLAUtils.addQueryBooleanParamToBinder(
		 binder, "IS_EXPERIAN", this.isExperian());
		
		CCLAUtils.addQueryBooleanParamToBinder(
		 binder, "DO_NOT_CONTACT_FLAG", this.doNotContact());
		
		CCLAUtils.addQueryIntParamToBinder(
		 binder, "AUTH_STATUS_ID", this.getAuthStatus().getAuthStatusId());
	}

	public void persist(FWFacade facade, String userName) throws DataException {
		this.validate(facade);
		
		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);
		
		// Fetch current data for auditing
		DataResultSet oldData = Contact.getData(this.getContactId(), facade);

		facade.execute("qClientServices_UpdateContact", binder);
		
		// Add audit record
		DataResultSet newData = Contact.getData(this.getContactId(), facade);
		
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(this.getContactId(), 
		 ElementType.SecondaryElementType.ContactPoint.toString());
		
		// Determine the element type this Contact Point was linked to.
		Element elem = Element.get(this.getElementId(), facade);
		
		auditRelations.put(elem.getElementId(), elem.getType().getName());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, userName, 
		 Globals.AuditActions.UPDATE.toString(), 
		 ElementType.SecondaryElementType.ContactPoint.toString(), 
		 oldData, newData, auditRelations);			
	}
	
	public int getContactId() {
		return contactId;
	}
	
	public void setContactId(int contactId) {
		this.contactId = contactId;
	}
	
	public int getSubmethodId() {
		return submethodId;
	}
	
	public void setSubmethodId(int submethodId) {
		this.submethodId = submethodId;
	}

	public int getMethodId() {
		return methodId;
	}

	public void setMethodId(int methodId) {
		this.methodId = methodId;
	}
	
	public Integer getRelationId() {
		return relationId;
	}
	
	public void setRelationId(Integer relationId) {
		this.relationId = relationId;
	}
	
	public Integer getElementId() {
		return elementId;
	}
	
	public void setElementId(Integer elementId) {
		this.elementId = elementId;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public Integer getAddressId() {
		return addressId;
	}
	
	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public boolean isDubious() {
		return isDubious;
	}

	public void setDubious(boolean isDubious) {
		this.isDubious = isDubious;
	}

	public boolean isExperian() {
		return isExperian;
	}

	public void setExperian(boolean isExperian) {
		this.isExperian = isExperian;
	}

	public boolean doNotContact() {
		return doNotContact;
	}

	public void setDoNotContact(boolean doNotContact) {
		this.doNotContact = doNotContact;
	}
	
	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	
	public String toString() {
		return 
			"Contact [addressId=" + addressId 
			+ ", contactId=" + contactId
			+ ", submethodId=" + submethodId 
			+ ", relationId=" + relationId  
			+ ", elementId=" + elementId 
			+ ", value=" + value 
			+ ", isDefault=" + isDefault
			+ ", isDubious=" + isDubious 
			+ ", isExperian=" + isExperian
			+ ", doNotContact=" + doNotContact
			+ ", lastUpdated=" + lastUpdated 
			+ "]";
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Address getAddress() {
		return address;
	}

	/** Returns the first Contact instance found that matches the given sub method.
	 * 
	 *  Returns null if none of the passed contacts have a matching sub method
	 *  
	 * @param contacts
	 * @param subMethodId
	 * @return
	 */
	public static Contact getFirstContactBySubMethod
	 (Vector<Contact> contacts, int subMethodId) {
		
		for (Contact contact : contacts) {
			if (contact.getSubmethodId() == subMethodId)
				return contact;
		}
		
		return null;
	}

	public void setAuthStatus(AuthStatus authStatus) {
		this.authStatus = authStatus;
	}

	public AuthStatus getAuthStatus() {
		return authStatus;
	}
	
	@Override
	public boolean equals(Object o) {
		return ((o instanceof Contact)
			&& ((Contact)o).getContactId() == this.getContactId());
	}

	/** Name of the binder value that will store an error message related to a contact
	 *  point fetch operation
	 */
	public static final String CONTACT_POINT_FETCH_ERROR = "CONTACT_POINT_FETCH_ERROR";
	
	/** Attempts to add a ResultSet of Contact Points to the binder (rsContactPoints)
	 *  belonging to the nominated Account correspondent.
	 * 
	 *  Called from BundleServices.panelValidationExtra() service method. First checks
	 *  if the passed Document Class (docClass) may yield Static Data Update 
	 *  instructions post validation (e.g. Mandates, Apps)
	 *  
	 *  If so, the document metadata is fetched and checked to see if an Account can
	 *  be resolved from the metadata values. 
	 *  
	 *  If so, the nominated Correspondent is resolved and their contact points are
	 *  added to the binder.
	 *  
	 *  The Correspondent's full name is also added to the binder as 
	 *  AccountCorrespondentName
	 *  
	 * @param mBinder
	 * @param facade
	 */
	public static void addContactPointsResultSetToBinder(
	 DataBinder binder, FWFacade facade) {
		
		try {
			String docClassStr     = binder.getLocal("docClass");
			
			if (StringUtils.stringIsBlank(docClassStr)) {
				return;
			}
			
			// Determine the Instruction Type from the Document Class name.
			InstructionType instrType = 
			 InstructionType.getNameCache().getCachedInstance(docClassStr);

			if (instrType == null)
				return;
			
			// Determine whether this instruction type may yield Static Data Updates.
			if (!StaticDataUpdateApplied
				.isInstructionGeneratingStaticDataInstructions(instrType))
				return;
			
			Log.debug("Found an Instruction Type that will yield Static Data Updates ("+
			 instrType.getName() + "). Checking for Account data against passed Document");

			Integer dID = CCLAUtils.getBinderIntegerValue(binder, UCMFieldNames.DocID);
			LWDocument lwDoc = new LWDocument(dID, true);
			
			String clientNumStr = lwDoc.getAttribute(UCMFieldNames.ClientNumber);
			String accountNumStr = lwDoc.getAttribute(UCMFieldNames.AccountNumber);
						
			if (StringUtils.stringIsBlank(clientNumStr) 
				||
				StringUtils.stringIsBlank(accountNumStr)) {
				return;
			}
			
			Account account = Account.get(Account.getAccountByIndexingValues
			 (clientNumStr, accountNumStr, false, facade));
			
			if (account != null)
				addNominatedCorrespondentContactPointsToBinder(account, binder, facade);
			
		} catch (Exception e) {
			Log.warn("Failed to populate binder with Contact Points: " 
			 + e.getMessage(), e);
		}
	}
	
	/** For the given account, the following things are added to the passed DataBinder,
	 *  providing the account has a unambiguous nominated correspondent:
	 *  
	 *  -AccountCorrespondentId: Person ID of the correspondent.
	 *  -AccountCorrespondentName: full name of the correspondent.
	 *  -rsContactPoints: all their associated Contact Points, includes address data.
	 * 
	 *  If the correspondent can't be resolve for the account, an error message is
	 *  added to the binder instead.
	 * 
	 * @param account
	 * @param binder
	 * @param facade
	 * @throws DataException
	 */
	public static void addNominatedCorrespondentContactPointsToBinder
	 (Account account, DataBinder binder, FWFacade facade) throws DataException {
		
		Person corr = 
		 Account.getNominatedCorrespondent(account.getAccountId(), false, facade);
		
		if (corr != null) {
			binder.putLocal("AccountCorrespondentId", Integer.toString
			 (corr.getPersonId()));
			binder.putLocal("AccountCorrespondentName", corr.getFullName());
			
			DataResultSet rsContactPoints = 
			 getElementContactsData(corr.getPersonId(), facade);
			
			binder.addResultSet("rsContactPoints", rsContactPoints);

		} else {
			binder.putLocal(CONTACT_POINT_FETCH_ERROR, 
			 "Unable to resolve nominated correspondent. Check the account relations.");
		}
	}
	
	/** Determines the last update time of the passed contact by inspection of audit
	 *  records.
	 * 
	 * @param contact
	 * @return
	 * @throws DataException 
	 */
	public static Date getLastUpdate(Contact contact, FWFacade facade)
	 throws DataException {
		
		SDAudit latestContactPointAuditEvent = SDAudit
		 .getLatestSDAuditByEventAndRelationId(
		  ElementType.SecondaryElementType.ContactPoint.toString(), 
		  contact.getContactId(), 
		  facade);
		
		if (latestContactPointAuditEvent != null)
			return latestContactPointAuditEvent.getAuditDate();
		else
			return null;
	}
}
