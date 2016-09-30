package com.ecs.ucm.ccla.data;

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

public class Address implements Persistable {

	private int addressId;
	
	private String flat;	  /* Flat number */
	private String houseName; /* Building or house name */
	private String houseNumber;
	
	private String street;
	private String district;
	private String city;
	private String county;
	private String country;
	
	private String postCode;
	
	private boolean qasValid;
	private Date lastUpdated;
	
	/** Stores maximum DB field lengths */
	public static class FieldLengths {
		public static final int HOUSE_NUMBER = 20;
	}
	
	public Address(DataBinder binder) throws DataException {
		this.setAttributes(binder);
	}
	
	public Address(int addressId, String flat, String houseName,
			String houseNumber, String street, String district, String city, String county,
			String country, String postCode, boolean qasValid, Date lastUpdated) {
		
		this.addressId = addressId;
		this.flat = flat;
		this.houseName = houseName;
		this.houseNumber = houseNumber;
		this.street = street;
		this.district = district;
		this.city = city;
		this.county = county;
		this.country = country;
		this.postCode = postCode;
		this.qasValid = qasValid;
		this.lastUpdated = lastUpdated;
	}
	
	public static Address add(DataBinder binder, FWFacade facade, String username) 
	 throws DataException {
		
		Address address = new Address(binder);
		
		int addressId = Integer.parseInt(
		 CCLAUtils.getNewKey("Address", facade));
		
		address.setAddressId(addressId);
		address.addFieldsToBinder(binder);
		
		address.validate(facade);
		
		facade.execute("qClientServices_CreateAddressRecord", binder);
		
		// Add audit record
		DataResultSet newData = Address.getData(address.getAddressId(), facade);
	
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(address.getAddressId(), 
		 ElementType.SecondaryElementType.Address.toString());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.ADD.toString(), 
		 ElementType.SecondaryElementType.Address.toString(), 
		 null, newData, auditRelations);
		return address;
	}
	
	/** Makes a copy of an existing Address.
	 * 
	 * @param address
	 * @param facade
	 * @param username
	 * @return
	 * @throws DataException 
	 */
	public static Address copy(Address address, FWFacade facade, String userName) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		address.addFieldsToBinder(binder);
		
		return add(binder, facade, userName);
	}
	
	/** Creates an Address instance from the current row values of
	 *  the passed ResultSet.
	 *  
	 * @param rsAddress
	 * @return
	 * @throws DataException 
	 */
	public static Address get(DataResultSet rsAddress) throws DataException {
		
		if (rsAddress.isEmpty())
			return null;
		
		// Look for aliased version of LAST_UPDATED column first - sometimes the Address
		// table data is joined against the Contact Point data so the column must be
		// aliased.
		Date addrLastUpdated = rsAddress.getDateValueByName("ADDRESS_LAST_UPDATED");
		
		if (addrLastUpdated == null)
			addrLastUpdated = rsAddress.getDateValueByName("LAST_UPDATED");		
				
		return new Address(
			CCLAUtils.getResultSetIntegerValue(rsAddress, "ADDRESS_ID"),
 
			CCLAUtils.getResultSetStringValue(rsAddress, "FLAT"),
			CCLAUtils.getResultSetStringValue(rsAddress, "HOUSENAME"),
			CCLAUtils.getResultSetStringValue(rsAddress, "HOUSENUMBER"),
			CCLAUtils.getResultSetStringValue(rsAddress, "STREET"),
			CCLAUtils.getResultSetStringValue(rsAddress, "DISTRICT"),
			CCLAUtils.getResultSetStringValue(rsAddress, "CITY"),
			CCLAUtils.getResultSetStringValue(rsAddress, "COUNTY"),
			CCLAUtils.getResultSetStringValue(rsAddress, "COUNTRY"),
			CCLAUtils.getResultSetStringValue(rsAddress, "POSTCODE"),
			
			CCLAUtils.getResultSetBoolValue(rsAddress, "QAS_VALID"),
			addrLastUpdated
		);
	}
	
	public static Address get(int addressId, FWFacade facade) throws DataException {
		
		DataResultSet rsAddress = getData(addressId, facade);
		
		if (rsAddress.isEmpty())
			return null;
		else
			return get(rsAddress);
	}	
		
	public static DataResultSet getData(int addressId, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(
		 binder, "ADDRESS_ID", addressId);
		
		DataResultSet rsAddress = 
		 facade.createResultSet("qClientServices_GetAddressRecord", binder);
		
		return rsAddress;
	}
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		
		CCLAUtils.addQueryIntParamToBinder(binder, "ADDRESS_ID", addressId);
		
		CCLAUtils.addQueryParamToBinder(binder, "FLAT", flat);
		CCLAUtils.addQueryParamToBinder(binder, "HOUSENAME", houseName);
		CCLAUtils.addQueryParamToBinder(binder, "HOUSENUMBER", houseNumber);
		
		CCLAUtils.addQueryParamToBinder(binder, "STREET", street);
		CCLAUtils.addQueryParamToBinder(binder, "DISTRICT", district);
		CCLAUtils.addQueryParamToBinder(binder, "CITY", city);
		CCLAUtils.addQueryParamToBinder(binder, "COUNTY", county);
		CCLAUtils.addQueryParamToBinder(binder, "COUNTRY", country);
		
		CCLAUtils.addQueryParamToBinder(binder, "POSTCODE", postCode);
		CCLAUtils.addQueryBooleanParamToBinder(binder, "QAS_VALID", qasValid);
		
	}

	public void persist(FWFacade facade, String username) throws DataException {
		
		this.validate(facade);
		
		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);
		
		DataResultSet beforeData = Address.getData(this.getAddressId(), facade);
		
		facade.execute("qClientServices_UpdateAddressRecord", binder);
	
		DataResultSet afterData = Address.getData(this.getAddressId(), facade);
		
		// Add audit record
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(this.getAddressId(), ElementType.SecondaryElementType.Address.toString());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.UPDATE.toString(), 
		 ElementType.SecondaryElementType.Address.toString(), 
		 beforeData, afterData, auditRelations);	}

	public void setAttributes(DataBinder binder) throws DataException {
		
		this.setFlat(binder.getLocal("FLAT"));
		this.setHouseName(binder.getLocal("HOUSENAME"));
		this.setHouseNumber(binder.getLocal("HOUSENUMBER"));
		
		this.setStreet(binder.getLocal("STREET"));
		this.setDistrict(binder.getLocal("DISTRICT"));
		this.setCity(binder.getLocal("CITY"));
		this.setCounty(binder.getLocal("COUNTY"));
		this.setCountry(binder.getLocal("COUNTRY"));
		this.setPostCode(binder.getLocal("POSTCODE"));
		
		this.setQasValid(
		 CCLAUtils.getBinderBoolValue(binder, "QAS_VALID"));
	}
	
	/** Checks if the given binder contains sufficient address
	 *  fields to create a new Address record.
	 *  
	 *  Current requirements:
	 *  -binder contains a postcode
	 *  -binder contains either house name or house number
	 *  
	 * @param binder
	 * @return
	 */
	public static boolean addressDataExists(DataBinder binder) {
		
		String postCode 	= binder.getLocal("POSTCODE");
		
		String flatNumber	= binder.getLocal("FLAT");
		String houseName	= binder.getLocal("HOUSENAME");
		String houseNumber	= binder.getLocal("HOUSENUMBER");
		
		if (!StringUtils.stringIsBlank(postCode)
			&&
			(!StringUtils.stringIsBlank(flatNumber)
			 ||
			 !StringUtils.stringIsBlank(houseName) 
			 || 
			 !StringUtils.stringIsBlank(houseNumber))) {

			return true;
		} else
			return false;
	}

	/** Checks if the given address (from addressId) contains sufficient address
	 *  fields to create a new Address record.
	 *  
	 *  Current requirements:
	 *  -binder contains a postcode
	 *  -binder contains either house name or house number
	 *  
	 * @param binder
	 * @return
	 * @throws DataException 
	 */
	public static boolean addressDataValid(int addressId, FWFacade facade) throws DataException {
		
		Address address = Address.get(addressId, facade);
		
		if (address == null)
			throw new DataException("Address ID " + addressId + " not found");
		
		return address.isDataValid();
	}
	
	public boolean isDataValid() {
		String postCode 	= this.getPostCode();
		
		String flatNumber	= this.getFlat();
		String houseName	= this.getHouseName();
		String houseNumber	= this.getHouseNumber();
		String street 		= this.getStreet();
		
		if (!StringUtils.stringIsBlank(postCode)
			&&
			(!StringUtils.stringIsBlank(flatNumber)
			 ||
			 !StringUtils.stringIsBlank(houseName) 
			 || 
			 !StringUtils.stringIsBlank(houseNumber)
			 ||
			 !StringUtils.stringIsBlank(street))) {

			return true;
		} else
			return false;
	}
	
	public int getAddressId() {
		return addressId;
	}

	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}

	public String getFlat() {
		return flat;
	}

	public void setFlat(String flat) {
		this.flat = flat;
	}

	public String getHouseName() {
		return houseName;
	}

	public void setHouseName(String houseName) {
		this.houseName = houseName;
	}

	public String getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getCountry() {
		return country;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getDistrict() {
		return district;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode.toUpperCase();
	}

	public boolean isQasValid() {
		return qasValid;
	}

	public void setQasValid(boolean qasValid) {
		this.qasValid = qasValid;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	@Override
	public String toString() {
		return "Address [addressId=" + addressId + ", city=" + city
				+ ", country=" + country + ", county=" + county + ", flat="
				+ flat + ", houseName=" + houseName + ", houseNumber="
				+ houseNumber + ", lastUpdated=" + lastUpdated + ", postCode="
				+ postCode + ", qasValid=" + qasValid + ", street=" + street
				+ "]";
	}

	public void validate(FWFacade facade) throws DataException {

		// Ensure the House Number field value isn't too long.
		if (this.getHouseNumber() != null 
			&&
			this.getHouseNumber().length() > FieldLengths.HOUSE_NUMBER)
			throw new DataException("House Number field is too long (max "
			 + FieldLengths.HOUSE_NUMBER + " characters)");
	}
	
	/** Used when specifying address elements. */
	public static enum AddressElement {
		FLAT,
		HOUSE_NUMBER,
		HOUSE_NAME,
		STREET,
		DISTRICT,
		CITY,
		COUNTY,
		COUNTRY,
		POSTCODE
	}
	
	public String getAddressElement(AddressElement elem) {
		if (elem.equals(AddressElement.FLAT))
			return this.flat;
		else if (elem.equals(AddressElement.HOUSE_NUMBER))
			return houseNumber;
		else if (elem.equals(AddressElement.HOUSE_NAME))
			return houseName;
		else if (elem.equals(AddressElement.STREET))
			return street;
		else if (elem.equals(AddressElement.DISTRICT))
			return district;
		else if (elem.equals(AddressElement.CITY))
			return city;
		else if (elem.equals(AddressElement.COUNTY))
			return county;
		else if (elem.equals(AddressElement.POSTCODE))
			return postCode;
		else
			return null;
	}
	
	/** Returns a Vector containing all non-empty/null address elements
	 *  specified in the passed Array.
	 *  
	 * @param elems
	 * @return
	 */
	public Vector<String> getNormalizedAddress(AddressElement[] elems) {
		
		Vector<String> v = new Vector<String>();
		
		for (int i=0; i<elems.length; i++) {
			String elem = getAddressElement(elems[i]);
			
			if (!StringUtils.stringIsBlank(elem))
				v.add(elem);
		}
		
		return v;
	}
	
	/** Returns a set of printable address lines for the given address.
	 *  
	 *  If house number and street are both non-null, they are combined into a single
	 *  string.
	 *  
	 * @param includePostcode
	 * @return
	 */
	public Vector<String> getPrintableAddress
	 (boolean includePostCode, boolean includeCountry) {
		Vector<String> v = new Vector<String>();
		
		if (this.getFlat() != null)
			v.add(this.getFlat());
		
		if (this.getHouseName() != null)
			v.add(this.getHouseName());
		
		if ((this.getHouseNumber() != null) && (this.getStreet() != null))
			v.add(this.getHouseNumber() + " " + this.getStreet());
		else {
			if (this.getHouseNumber() != null)
				v.add(this.getHouseNumber());
			
			if (this.getStreet() != null)
				v.add(this.getStreet());
		}
		
		if (this.getDistrict() != null)
			v.add(this.getDistrict());
		
		if (this.getCity() != null)
			v.add(this.getCity());
		
		if (this.getCounty() != null)
			v.add(this.getCounty());
		
		if (includePostCode && this.getPostCode() != null)
			v.add(this.getPostCode());
		
		if (includeCountry && this.getCountry() != null)
			v.add(this.getCountry());
		
		StringBuffer sb = new StringBuffer();
		sb.append("Generated printable address for Address ID: " + this.getAddressId() 
		 + "\n");
		
		for (String s : v) {
			sb.append(s + "\n");
		}
		
		Log.debug(sb.toString());
		
		return v;
	}
}
