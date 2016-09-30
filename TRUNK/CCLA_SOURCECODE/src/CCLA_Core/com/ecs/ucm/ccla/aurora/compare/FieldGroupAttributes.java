package com.ecs.ucm.ccla.aurora.compare;

import intradoc.data.DataException;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import com.aurora.webservice.Address;
import com.ecs.utils.StringUtils;

/** Wrapper for the name-value pairs and their associated comparison/population methods
 *  that correspond to a particular FieldGroup.
 * 
 * @author tm
 * @param <A> Aurora Entity type
 */
public abstract class FieldGroupAttributes<A> {
	
	/** Linked collection used to preserve ordering. */
	protected LinkedHashMap<String, String> fieldValues;
	
	public LinkedHashMap<String, String> getFieldValues() {
		return fieldValues;
	}
	
	public FieldGroupAttributes() {
		this.fieldValues = new LinkedHashMap<String, String>();
	}
	
	protected void addFieldValue(String name, String value) throws DataException {
		if (this.fieldValues.containsKey(name)) {
			throw new DataException
			 ("Field Value with name '" + name + "' already exists");
		}
		this.fieldValues.put(name, value);
	}
	
	public String getFieldValue(String fieldName) throws DataException {
		if (!this.fieldValues.containsKey(fieldName)) {
			throw new DataException
			 ("Field Value with name '" + fieldName + "' not found");
		}
		
		return this.fieldValues.get(fieldName);
	}
	
	/** Implement this to apply the stored attribute values to the passed
	 *  Aurora entity. 
	 *  
	 * @param auroraEntity
	 * @throws DataException
	 */
	public abstract void applyValuesToEntity(A auroraEntity) throws DataException;
	
	/** Implement this to capture and store attribute values from the passed Aurora entity.
	 * 
	 * @param auroraEntity
	 * @throws DataException
	 */
	protected abstract void setValuesFromEntity(A auroraEntity) throws DataException;
	
	/** Compares field values in the current instance against those in the passed 
	 *  instance, returns a list of the mismatched field names (if any)
	 *  
	 *  The passed field group must have an identical structure (i.e. same list of field 
	 *  names)
	 *  
	 * @throws DataException 
	 **/
	public Vector<String> getMismatches(FieldGroupAttributes<A> fieldGroupValues) 
	 throws DataException {
		
		if (this.getFieldValues().size() != fieldGroupValues.getFieldValues().size())
			throw new DataException("Unable to compare field groups - " +
			 "mismatched number of fields");
		
		Vector<String> mismatchedFieldNames = new Vector<String>();
		
		for (Map.Entry<String, String> fieldValue : this.getFieldValues().entrySet()) {
			String fieldName = fieldValue.getKey();
			
			if (!fieldGroupValues.getFieldValues().containsKey(fieldName)) {
				throw new DataException("Unable to compare field groups - " +
				 "couldn't find any entry for field name '" + fieldName + "'");
			}
			
			String thisFieldValue = fieldValue.getValue();
			String otherFieldValue = fieldGroupValues.getFieldValues().get(fieldName);
			
			// Trim both values.
			if (thisFieldValue != null)
				thisFieldValue = thisFieldValue.trim();
			if (otherFieldValue != null)
				otherFieldValue = otherFieldValue.trim();
			
			// Check for both being null/empty strings first.
			if (StringUtils.stringIsBlank(thisFieldValue)
				&&
				StringUtils.stringIsBlank(otherFieldValue)) {
				continue;
			}
			
			if (thisFieldValue == null || otherFieldValue == null) {
				// One or the other is null
				mismatchedFieldNames.add(fieldName);
			} else if (!thisFieldValue.equalsIgnoreCase(otherFieldValue)) {
				// Do case-insensitive string match.
				mismatchedFieldNames.add(fieldName);
			}
		}
		
		return mismatchedFieldNames;
	}
	
	/* ===============
		
	   Helper methods for getting/setting attributes
	   
	   ===============
	*/
	
	/** Adds all address lines from the com.aurora.webservice.Address instance.
	 *  
	 *  Safe if the passed Address is null.
	 *  
	 * @param addr
	 * @throws DataException 
	 */
	protected void addAddressData(Address addr) throws DataException {
		this.addFieldValue("Organisation", addr == null ? null : addr.getOrganisation());
		this.addFieldValue("Building", addr == null ? null : addr.getBuilding());
		this.addFieldValue("Street", addr == null ? null : addr.getStreet());
		this.addFieldValue("Locality", addr == null ? null : addr.getLocality());
		this.addFieldValue("Town", addr == null ? null : addr.getTown());
		this.addFieldValue("County", addr == null ? null : addr.getCounty());
		this.addFieldValue("Postcode", addr == null ? null : addr.getPostCode());
		this.addFieldValue("Country", addr == null ? null : addr.getCountry());
	}
	
	/** Adds other contact data from the com.aurora.webservice.Address instance, 
	 *  including phone/fax numbers
	 *  
	 *  Safe if the passed Address is null.
	 *  
	 * @param fieldGroup
	 * @param addr
	 * @throws DataException 
	 */
	public void addOtherContactData(Address addr) throws DataException {
		this.addFieldValue("Telephone", addr == null ? null : addr.getTelephone());
		this.addFieldValue("Fax", addr == null ? null : addr.getFax());
		this.addFieldValue("Email", addr == null ? null : addr.getEmail());
		this.addFieldValue("WebsiteAddress", addr == null ? null : addr.getWebsiteAddress());
	}
	
	/** Applies address field values to the passed Address instance. 
	 * @throws DataException */
	protected void applyAddressData(Address addr) throws DataException {
		addr.setOrganisation(this.getFieldValue("Organisation"));
		addr.setBuilding(this.getFieldValue("Building"));
		addr.setStreet(this.getFieldValue("Street"));
		addr.setLocality(this.getFieldValue("Locality"));
		addr.setTown(this.getFieldValue("Town"));
		addr.setCounty(this.getFieldValue("County"));
		addr.setPostCode(this.getFieldValue("Postcode"));
		addr.setCountry(this.getFieldValue("Country"));
	}
	
	/** Applies other contact detail field values to the passed Address instance. 
	 * @throws DataException */
	protected void applyOtherContactData(Address addr) throws DataException {
		addr.setTelephone(this.getFieldValue("Telephone"));
		addr.setFax(this.getFieldValue("Fax"));
		addr.setEmail(this.getFieldValue("Email"));
		addr.setWebsiteAddress(this.getFieldValue("WebsiteAddress"));
	}
	
}
