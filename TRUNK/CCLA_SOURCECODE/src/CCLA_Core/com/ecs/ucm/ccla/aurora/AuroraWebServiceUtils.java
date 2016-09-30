package com.ecs.ucm.ccla.aurora;


import java.util.Vector;

import com.aurora.webservice.Address;
import com.ecs.ucm.ccla.data.Contact;
import com.ecs.utils.StringUtils;

/** Generic helper methods for interfacing with Aurora web services.
 * 
 * @author Tom
 *
 */
public class AuroraWebServiceUtils {
	
	/** Returns an Aurora Address instance, as used in the Client and Correspondent
	 *  Aurora objects.
	 *  
	 *  The Aurora Address object contains both postal address data and telephone/fax/
	 *  email/website details.
	 *  
	 * @param elementContacts 	list of all Contact instances linked to the Element
	 * 							that requires the Aurora Address instance.
	 * @return
	 */
	public static Address getAddressInstance(Vector<Contact> elementContacts) {
		
		Contact addressContact = 
		 Contact.getDefaultContact(elementContacts, Contact.ADDRESS, true);
			
		Contact phoneContact = 
		 Contact.getDefaultContact(elementContacts, Contact.PHONE, false);
		
		Contact emailContact = 
		 Contact.getDefaultContact(elementContacts, Contact.EMAIL, false);
			
		Contact faxContact = 
		 Contact.getDefaultContact(elementContacts, Contact.FAX, false);
				
		Contact webContact = 
		 Contact.getDefaultContact(elementContacts, Contact.WEB, false);
		
		if (addressContact != null) {
			com.ecs.ucm.ccla.data.Address address = addressContact.getAddress();
			
			// Postal address data available.
			return new Address(
				address.getFlat() !=null ? address.getFlat() : "" ,
				address.getHouseName() !=null ? address.getHouseName() : "",
				getStreet(address) !=null ? getStreet(address) : "",
				address.getDistrict() !=null ? address.getDistrict(): "",
				address.getCity() !=null ? address.getCity() : "",
				address.getPostCode() !=null ? address.getPostCode() : "",
				address.getCountry() !=null ? address.getCountry() : "",
				address.getCounty() !=null ? address.getCounty() : "",
				phoneContact != null ? phoneContact.getValue() : "",
				faxContact != null ? faxContact.getValue() : "",
				emailContact != null ? emailContact.getValue() : "",
				webContact != null ? webContact.getValue() : ""
			);
			
		} else {
			// No postal address data to return.
			return new Address(
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				"",
				phoneContact != null ? phoneContact.getValue() : "",
				faxContact != null ? faxContact.getValue() : "",
				emailContact != null ? emailContact.getValue() : "",
				webContact != null ? webContact.getValue() : null
			);
		}
	}
	
	/** Returns a combined version of the 'street' portion of a CCLA Address.
	 *  
	 *  If the passed CCLA Address contains a House Number string, this is prepended to 
	 *  the Street string. Otherwise, just the Street string is returned. 
	 *  
	 * @param address
	 * @return
	 */
	private static String getStreet(com.ecs.ucm.ccla.data.Address address) {
		
		if (!StringUtils.stringIsBlank(address.getStreet())) {
			return (!StringUtils.stringIsBlank(address.getHouseNumber()) ?
					address.getHouseNumber() + " " : "") + address.getStreet();
		} else {
			return "";
		}
	}
}
