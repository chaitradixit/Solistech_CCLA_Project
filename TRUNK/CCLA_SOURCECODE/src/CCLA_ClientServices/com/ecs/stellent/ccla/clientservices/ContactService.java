package com.ecs.stellent.ccla.clientservices;

import java.util.Iterator;
import java.util.Vector;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Address;
import com.ecs.ucm.ccla.data.AuthStatus;
import com.ecs.ucm.ccla.data.Contact;
import com.ecs.ucm.ccla.data.Element;
import com.ecs.ucm.ccla.data.IdentityCheck;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.SharedCols;
import com.ecs.ucm.ccla.experian.AuthenticationScoreUtils;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.server.Service;

/** Service methods for viewing and manipulating contacts and addresses */
public class ContactService extends Service {
	
	/** Service method for adding a new record to CCLA_CONTACT_MAP.
	 * 
	 * @throws DataException
	 * @throws ServiceException 
	 */
	public void addContact() throws DataException, ServiceException {

		String username = m_userData.m_name;
		
		Integer elementId = CCLAUtils.getBinderIntegerValue
		 (m_binder, "ELEMENT_ID");
		
		Integer submethodId = CCLAUtils.getBinderIntegerValue
		 (m_binder, "SUBMETHOD_ID");
		
		Integer relationId = CCLAUtils.getBinderIntegerValue
		 (m_binder, "RELATION_ID");
		
		Integer methodId = CCLAUtils.getBinderIntegerValue
		 (m_binder, "METHOD_ID");

		String value = m_binder.getLocal("VALUE");
		
		if (elementId == null)
			throw new ServiceException("Unable to create contact: " +
			 "element ID was missing.");
		
		if (submethodId == null)
			throw new ServiceException("Unable to create contact: " +
			 "submethod ID was missing.");
		
		if (StringUtils.stringIsBlank(value))
			throw new ServiceException("Unable to create contact: " +
			 "value was missing.");
			
		Integer addressId = 
		 CCLAUtils.getBinderIntegerValue(m_binder, "ADDRESS_ID");
		
		boolean isDefault =
			CCLAUtils.getBinderBoolValue(m_binder, "IS_DEFAULT");
		
		boolean isDubious =
			CCLAUtils.getBinderBoolValue(m_binder, "IS_DUBIOUS");
		
		boolean isExperian =
			CCLAUtils.getBinderBoolValue(m_binder, "IS_EXPERIAN");
		
		boolean doNotContact =
			CCLAUtils.getBinderBoolValue(m_binder, "DO_NOT_CONTACT_FLAG");
		
		Integer authStatusId = CCLAUtils.getBinderIntegerValue
		 (m_binder, "AUTH_STATUS_ID");
		
		AuthStatus authStatus = null;
		
		if (authStatusId != null)
			authStatus = AuthStatus.getCache().getCachedInstance(authStatusId);
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		try {
			facade.beginTransaction();
			
			if (methodId == Contact.ADDRESS && addressId == null) {
				// User is attempting to add a new address contact, with
				// new address data. Add the address first.
				
				if (!Address.addressDataExists(m_binder))
					throw new ServiceException
					 ("Required address data is missing. " +
					  "Ensure that a postcode is specified, plus one of " +
					  "the following: Flat/House Name/Number");
				
				// Create the new Address record
				Address address = Address.add(m_binder, facade, username);
				addressId = address.getAddressId();
			}
			
			// Create the new Contact record
			
			// TODO: set isDefault=true if this is the only linked contact point for
			// the contact method.
			
			Contact contact = 
			 Contact.add(elementId, relationId, submethodId, value, addressId, 
			 isDefault, isDubious, isExperian, doNotContact, authStatus, 
			 facade, username);
				
			facade.commitTransaction();
			
			Log.debug("Generated new contact with ID: " + contact.getContactId());
		} catch (DataException e) {
			facade.rollbackTransaction();
			String msg = "Unable to create contact: " + e.getMessage();
			
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}
	
	/** Makes a copy of an existing Contact Point (and its linked Address if applicable)
	 * 
	 * @throws DataException 
	 * @throws ServiceException 
	 * 
	 */
	public void copyContact() throws DataException, ServiceException {
		
		// The contact point/address data will be connected to an Element ID or Relation
		// ID, depending on which one is present.
		Integer elementId = CCLAUtils.getBinderIntegerValue
		 (m_binder, Element.Cols.ID);
		
		Integer relationId = CCLAUtils.getBinderIntegerValue
		 (m_binder, SharedCols.RELATION);

		Integer contactId = CCLAUtils.getBinderIntegerValue
		 (m_binder, Contact.Cols.ID);
		
		Integer subMethodId = CCLAUtils.getBinderIntegerValue
		 (m_binder, Contact.Cols.SUBMETHOD);
		
		if (contactId == null) {
			throw new ServiceException
			 ("Unable to copy existing contact: source contact missing");	
		}
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace);
		Contact contact = Contact.get(contactId, facade);
		
		try {
			facade.beginTransaction();
			
			Contact.copy(contact, elementId, relationId, 
			 subMethodId, facade, m_userData.m_name);
			
			facade.commitTransaction();
			
		} catch (Exception e) {
			facade.rollbackTransaction();
			
			String msg = "Failed to copy contact details: " + e.getMessage();
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}
	
	/** Service method for adding a new Contact entry with a new linked Address.
	 *  
	 *  Useful when adding new address data to existing Entities/Persons.
	 *  
	 * @throws DataException
	 * @throws ServiceException 
	 */
	public void addAddressContact() throws DataException, ServiceException {
		
		String username 		= m_userData.m_name;
		
		Integer elementId = CCLAUtils.getBinderIntegerValue
		 (m_binder, Element.Cols.ID);
		
		Integer relationId = CCLAUtils.getBinderIntegerValue
		 (m_binder, SharedCols.RELATION);
		
		int submethodId = CCLAUtils.getBinderIntegerValue
		 (m_binder, Contact.Cols.SUBMETHOD);

		if (!Address.addressDataExists(m_binder))
			throw new ServiceException
			 ("Required address data is missing. " +
			  "Ensure that a postcode is specified, plus one of " +
			  "the following: Flat/House Name/Number");
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		// Fetch existing Contacts for this element.
		//
		// This is used to determine if an address contact is already
		// mapped to this Element ID - if not, this address will be
		// set as the default.
		Vector<Contact> contacts = Contact.getElementContacts(elementId, facade);
		boolean setDefault = true;
		boolean setExperian = true;
		
		for (Contact contact:contacts) {
			if (contact.getMethodId() == Contact.ADDRESS)
			{
				setDefault = false;
				setExperian = false;
			}
		}
		
		try {
			facade.beginTransaction();
			
			// First create the new Address record
			Address address = Address.add(m_binder, facade, username);
			
			// Now create the contact.
			Contact contact = Contact.add(elementId, relationId, submethodId, null, 
			 address.getAddressId(), setDefault, false, setExperian, false, 
			 AuthStatus.getCache().getCachedInstance(Contact.DEFAULT_AUTH_STATUS_ID),
			 facade, username);
			
			Log.debug("Created new address contact with Contact ID: " + 
			 contact.getContactId() + ", Address ID: " + address.getAddressId());
			
			// Add CONTACT_ID to binder, this will be picked up from the JSON
			// response message.
			CCLAUtils.addQueryIntParamToBinder
			 (m_binder, "CONTACT_ID", contact.getContactId());
			
			m_binder.putLocal("saved", "1");
			
			String redirectUrl = m_binder.getLocal("RedirectUrl");
			if (!StringUtils.stringIsBlank(redirectUrl))
				m_binder.putLocal("RedirectUrl", redirectUrl + contact.getContactId());
			
			facade.commitTransaction();
		} catch (Exception e) {
			facade.rollbackTransaction();
			String msg = "Failed to create address contact: " + e.getMessage();
			
			Log.error(msg, e);
			throw new ServiceException(e);
		}
	}
	
	/** Updates an existing record in CCLA_CONTACT_MAP table.
	 *  
	 *  If the contact has an Address ID set, the binder is also
	 *  searched for address data. The corresponding Address record
	 *  will also be updated.
	 * 
	 * @throws DataException
	 * @throws ServiceException 
	 */
	public void updateContact() throws DataException, ServiceException {

		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		Contact contact = getContact(facade);
		String username = m_userData.m_name;
		
		Log.debug("Updating Contact ID " + contact.getContactId());
		
		try {
			facade.beginTransaction();
			
			// Apply updated values to Contact instance
			contact.setAttributes(m_binder);
			
			// user needed for audit table
	
			m_binder.putLocal("USER_ID", username);
			contact.persist(facade, username);
			
			if (contact.getAddressId() != null) {
				// This is an address contact. Check for address data
				if (!Address.addressDataExists(m_binder))
					throw new ServiceException
					 ("Required address data is missing. " +
					  "Ensure that a postcode is specified, plus one of " +
					  "the following: Flat/House Name/Number");
				
				// Update the associated Address
				Address address = Address.get(contact.getAddressId(), facade);
				address.setAttributes(m_binder);
				address.persist(facade, username);
				
				Log.debug("Address ID " + address.getAddressId() + 
				 " updated successfully.");
				
				// Check if a nominated Experian address was updated.
				if (contact.isExperian())
				{
					Log.debug("Experian address has changed");
					
					IdentityCheck idCheck = IdentityCheck.get
					 (contact.getElementId(), facade);
					
					if (idCheck != null) {
						// Apply the 'Core Data Changed' flag to the Person's ID check
						AuthenticationScoreUtils.setCoreDataChanged
						 (idCheck, facade, m_userData.m_name);
					}
				}
			}
			
			Log.debug("Contact ID " + contact.getContactId() + 
			 " updated successfully.");
			
			m_binder.putLocal("saved", "1");
			
			facade.commitTransaction();
			
		} catch (Exception e) {
			facade.rollbackTransaction();
			String msg = "Unable to update contact. " + e.getMessage();
			
			Log.error(msg, e);
			throw new ServiceException(msg);
		}
	}

	/** Updates all non-address Contacts for a given Element ID.
	 *  
	 * 
	 * @throws DataException
	 * @throws ServiceException 
	 */
	public void updateElementContacts() throws DataException, ServiceException {

		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		String username = m_userData.m_name;
		
		// Get all current contacts for the element
		int elementId = CCLAUtils.getBinderIntegerValue(m_binder, Element.Cols.ID);
		Vector<Contact> ContactList = Contact.getElementContacts(elementId, facade);
		
		try {
			facade.beginTransaction();
			
			// loop round contactList and do update if value and submethod exists
			
			for (Contact contact:ContactList) {
				if (contact.getMethodId() != Contact.ADDRESS)
				{
					if (!StringUtils.stringIsBlank(m_binder.getLocal
						("SUBMETHOD_ID_" + contact.getContactId()))) {
						
						contact.setSubmethodId(CCLAUtils.getBinderIntegerValue
						 (m_binder, "SUBMETHOD_ID_" + contact.getContactId()));
						contact.setValue(m_binder.getLocal
						 ("CONTACT_VALUE_" + contact.getContactId()));
	
						Integer authStatusId = CCLAUtils.getBinderIntegerValue
						 (m_binder, "AUTH_STATUS_ID_" + contact.getContactId());
						
						if (authStatusId != null)
							contact.setAuthStatus(
							 AuthStatus.getCache().getCachedInstance(authStatusId));	
						
						contact.persist(facade, username);
						
						Log.debug("Contact ID " + contact.getContactId() + 
						 " updated successfully.");
					}
					
				}
			}

			m_binder.putLocal("saved", "1");
			m_binder.putLocal(Element.Cols.ID, Integer.toString(elementId));
			
			facade.commitTransaction();
			
		} catch (Exception e) {
			facade.rollbackTransaction();
			String msg = "Unable to update contact. " + e.getMessage();
			
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}	
	
	
	/** Removes an existing Contact record.
	 * 
	 *  This will also delete the associated Address record if applicable (via DELETE 
	 *  CASCADE property of Contacts table)
	 *  
	 * @param binder
	 * @param fw
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void removeContact() throws DataException, ServiceException {
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		Contact contact = getContact(facade);
		String username = m_userData.m_name;
		
		try {
			Contact.remove(contact.getContactId(), facade, username);
			
			Log.debug("Contact ID " + contact.getContactId() +
			 " removed successfully.");
			
			if (contact.getAddressId() != null)
				Log.debug("Linked Address ID " + contact.getAddressId() + 
				 " removed with contact.");
			
		} catch (Exception e) {
			String msg = "Failed to remove contact. The contact may have been " +
			 "removed already, or it is linked to an enrolment etc.";
			
			Log.error(msg, e);
			throw new ServiceException(msg);
		}
	}
	
	/** Checks the binder for a CONTACT_ID. If found, a Contact instance
	 *  is acquired and returns.
	 *  
	 * @return
	 * @throws ServiceException if CONTACT_ID is missing, or the corresponding
	 *   						Contact record isn't found.
	 * @throws DataException
	 */
	private Contact getContact(FWFacade facade) throws ServiceException, DataException {
		String contactIdStr = m_binder.getLocal(Contact.Cols.ID);
		
		if (StringUtils.stringIsBlank(contactIdStr))
			throw new ServiceException("Unable to fetch contact data, " +
			 "contact ID was missing.");
		
		int contactId = Integer.parseInt(contactIdStr);
		Contact contact = Contact.get(contactId, facade);
		
		if (contact == null)
			throw new ServiceException("Unable to find contact data with ID: " + 
			 contactId);
		else
			return contact;
	}
	
	/** Updates the preference flags for a set of address belonging to
	 *  a single Person/Client.
	 *  
	 *  Sets the isDefault and isExperian flags for the given Contacts.
	 *  These flags are 'exclusive', i.e. if a Person has 3 mapped addresses,
	 *  only 1 address can have either of these flags set.
	 *  
	 *  In the majority of cases, the same address will have both flags
	 *  set.
	 * @throws ServiceException 
	 * @throws DataException 
	 */
	public void updateAddressPreferences() throws ServiceException, DataException {
		
		String elementIdStr 	= m_binder.getLocal("ELEMENT_ID");
	
		if (StringUtils.stringIsBlank(elementIdStr))
			throw new ServiceException(
			 "Unable to updates address preferences: " +
			 "ELEMENT_ID was missing.");
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		int elementId = Integer.parseInt(elementIdStr);
		
		String defaultAddressStr 	= m_binder.getLocal("defaultAddress");
		String experianAddressStr	= m_binder.getLocal("experianAddress");
		
		if (StringUtils.stringIsBlank(defaultAddressStr)
			||
			StringUtils.stringIsBlank(experianAddressStr)) {
			
			throw new ServiceException(
			 "Unable to updates address preferences: " +
			 "designated default/Experian addresses were missing.");
		}
		
		int newDefaultAddressId		= Integer.parseInt(defaultAddressStr);
		int newExperianAddressId	= Integer.parseInt(experianAddressStr);
		
		Contact newDefaultContact = null, newExperianContact = null;
		
		Vector<Contact> contacts = 
		 Contact.getElementContacts(elementId, facade);
		
		// flag to check if the experian preference has changed
		// if it has changed then the authentication for the person
		// needs to be removed
		boolean experianAddressUpdated = false;
		
		// Store any Contact Point instances that get their default flags unset here.
		Vector<Contact> removedDefaultContacts = new Vector<Contact>();

		try {
			facade.beginTransaction();
			// Start off by removing default flags.
			
			for (Contact contact : contacts) {
				if (contact.getMethodId() != Contact.ADDRESS)
					continue; // Ignore non-address contacts.
					
				if (contact.getContactId() != newDefaultAddressId) {
					if (contact.isDefault()) {
						// Previously-set default contact.
						contact.setDefault(false);
						removedDefaultContacts.add(contact);
					}
				} else {
					newDefaultContact = contact;
				}
			}
		
			for (Contact contact : removedDefaultContacts) {
				Log.debug("Removing default flag from Contact ID: " 
				 + contact.getContactId());
				contact.persist(facade, m_userData.m_name);
			}
			
			removedDefaultContacts.clear();
			
			// Now remove Experian flags.
			
			for (Contact contact : contacts) {
				if (contact.getMethodId() != Contact.ADDRESS)
					continue; // Ignore non-address contacts.
					
				if (contact.getContactId() != newExperianAddressId) {
					if (contact.isExperian()) {
						// Previously-set Experian contact.
						contact.setExperian(false);
						removedDefaultContacts.add(contact);
					}
				} else {
					newExperianContact = contact;
				}
			}
	
			for (Contact contact : removedDefaultContacts) {
				experianAddressUpdated = true;
				
				Log.debug("Removing Experian flag from Contact ID: " 
				 + contact.getContactId());
				contact.persist(facade, m_userData.m_name);
			}

			// if experian address changed invalidate authentication
			// and recalculate score
			if (experianAddressUpdated) {
				Log.debug("Nominated Experian contact address has changed");
				
				// check if this is a person
				Person person = Person.get(elementId, false, facade);
				
				if (person!=null) {
					IdentityCheck idCheck = IdentityCheck.get
					 (person.getPersonId(), facade);
					
					if (idCheck != null) {
						AuthenticationScoreUtils.setCoreDataChanged
						 (idCheck, facade, m_userData.m_name);
					}
				}
			}
			
			// Now set default contact.
			Log.debug("Applying Default flag to Contact ID: " 
			 + newDefaultContact.getContactId());
			newDefaultContact.setDefault(true);
			newDefaultContact.persist(facade, m_userData.m_name);
			
			// Now set Experian contact.
			Log.debug("Applying Experian flag to Contact ID: " 
			 + newExperianContact.getContactId());
			newExperianContact.setExperian(true);
			newExperianContact.persist(facade, m_userData.m_name);
			
			facade.commitTransaction();
			
		} catch (Exception e) {
			facade.rollbackTransaction();
			
			String msg =  "Unable to update address preferences: " +
			 e.getMessage();
			
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}
}
