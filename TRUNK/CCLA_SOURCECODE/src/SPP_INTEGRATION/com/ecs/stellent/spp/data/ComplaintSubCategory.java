package com.ecs.stellent.spp.data;

import java.util.Vector;

/** Represents an ID-value pair. These are returned by the SPP Workflow web
 *  service getComplaintSubCategories.
 *  
 * @author Tom
 *
 */
public class ComplaintSubCategory {
	
	private int id;
	private String name;
	
	public ComplaintSubCategory(int id, String name) {
		this.setId(id);
		this.setName(name);
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public Vector<String> getResultSetRowValues() {
		Vector<String> v = new Vector<String>();
		
		v.add(Integer.toString(this.id));
		v.add(this.name);
		
		return v;
	}
}
