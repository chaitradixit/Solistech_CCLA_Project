package com.ecs.ucm.ccla.data;

import java.util.Vector;

import intradoc.data.DataResultSet;

/** Temporary wrapper class to store data for a single signatory.
 *  
 *  This will be replaced by Person instances.
 * 
 * @author Tom Marchant
 *
 */
public class Signatory {
	
	private String fullName;
	private String telephone;
	private String postCode;
	
	public Signatory(String fullName, String telephone, String postCode) {
		super();
		this.fullName = fullName;
		this.telephone = telephone;
		this.postCode = postCode;
	}
	
	public static Vector<Signatory> get(DataResultSet rsSignatories) {
		Vector<Signatory> sigs = new Vector<Signatory>();
		
		if (rsSignatories == null || rsSignatories.isEmpty())
			return sigs;
		
		rsSignatories.first();

		do {
			sigs.add(
			 new Signatory(
				rsSignatories.getStringValueByName("SIG_FULLNAME"),
				rsSignatories.getStringValueByName("SIG_TEL"),
				rsSignatories.getStringValueByName("SIG_POSTCODE")
			));
			
		} while (rsSignatories.next());
		
		return sigs;
	}

	public String getFullName() {
		return fullName;
	}

	public String getTelephone() {
		return telephone;
	}

	public String getPostCode() {
		return postCode;
	}
}
