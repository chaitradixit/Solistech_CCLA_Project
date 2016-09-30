package com.ecs.ucm.ccla.aurora;

/** Wrapper for a single Aurora Signatory entry, as stored against Aurora Client and
 *  Account records.
 *  
 * @author tm
 *
 */
public class AuroraSignatory {
	
	private final String name;
	private final String postcode;
	private final String telephone;
	
	public AuroraSignatory(String name, String postcode, String telephone) {
		super();
		this.name = name;
		this.postcode = postcode;
		this.telephone = telephone;
	}

	public String getName() {
		return name;
	}

	public String getPostcode() {
		return postcode;
	}

	public String getTelephone() {
		return telephone;
	}
}
