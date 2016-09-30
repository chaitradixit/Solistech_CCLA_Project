package com.ecs.ucm.ccla.data;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Represents entries from the REF_GENDER table. */
public class Gender implements Persistable {
	private int genderId;
	private String name;
	
	// Hard-coded Gender data. Not expecting this to change.
	private static Gender MALE = new Gender(1, "Male");
	private static Gender FEMALE = new Gender(2, "Female");
	
	public static Gender getGender(int genderId) {
		if(genderId == MALE.getGenderId())
			return MALE;
		else if (genderId == FEMALE.getGenderId())
			return FEMALE;
		else
			return null;
	}
	
	public Gender(int genderId, String name) {
		this.genderId = genderId;
		this.setName(name);
	}

	public static Gender get(DataResultSet rs) throws DataException {
		return new Gender(
		 CCLAUtils.getResultSetIntegerValue(rs, "GENDER_ID"),
		 rs.getStringValueByName("GENDER_NAME")
		);
	}
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		// TODO Auto-generated method stub

	}

	public void persist(FWFacade facade, String username) throws DataException {
		// TODO Auto-generated method stub

	}

	public void setAttributes(DataBinder binder) throws DataException {
		// TODO Auto-generated method stub

	}

	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub

	}
	
	public void setGenderId(int genderId) {
		this.genderId = genderId;
	}

	public int getGenderId() {
		return genderId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
