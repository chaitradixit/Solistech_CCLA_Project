package com.ecs.ucm.ccla.aurora;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.PersonTitle;

/** Used when extracting potential Person signatory details from an Aurora record.
 *  
 * @author tm
 *
 */
public class CandidatePersonSignatory extends AuroraSignatory {
	
	/** Used to store a reference to an existing Person that may be a duplicate of this
	 *  candidate.
	 */
	private Person duplicatePerson;
	
	/** Determines whether or not the name actually looks like a person's name, via
	 *  RegExp match.
	 */
	private boolean looksLikeAPersonName;
	
	/** Only relevant when duplicatePerson is set. True if the existing duplicate person
	 *  already has a signatory relation against the DB entity.
	 */
	private boolean existingRelation = false;
	
	/** Used to store composite sections of the extracted name string.
	 * 
	 *  Only applicable where looksLikeAPersonName is true. Some may still even be null
	 *  in this case!
	 **/
	private String title;
	private String firstName;
	private String middleNames;
	private String lastName;
	
	public CandidatePersonSignatory(String name, String postcode,
			String telephone) {
		super(name, postcode, telephone);
		
		Matcher m = AuroraSignatories.getPersonNameMatcher(name);
		this.looksLikeAPersonName = m.matches();
		
		if (m.matches()) {
			extractCompositeNameFields(m);
		}
	}
	
	private void extractCompositeNameFields(Matcher m) {
		this.title = 		m.group(1) == null ? null : m.group(1).trim();
		this.firstName = 	m.group(2) == null ? null : m.group(2).trim();
		this.middleNames = 	m.group(3) == null ? null : m.group(3).trim();
		this.lastName = 	m.group(5) == null ? null : m.group(5).trim();
	}

	public Person getDuplicatePerson() {
		return duplicatePerson;
	}

	public void setDuplicatePerson(Person duplicatePerson) {
		this.duplicatePerson = duplicatePerson;
	}

	public boolean isLooksLikeAPersonName() {
		return looksLikeAPersonName;
	}

	public boolean isExistingRelation() {
		return existingRelation;
	}

	public void setExistingRelation(boolean existingRelation) {
		this.existingRelation = existingRelation;
	}

	public String getTitle() {
		return title;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getMiddleNames() {
		return middleNames;
	}

	public String getLastName() {
		return lastName;
	}
}
