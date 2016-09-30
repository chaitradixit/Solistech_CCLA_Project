package com.ecs.stellent.spp.data;

/** Represents an ID-value pair. These are returned by the SPP Workflow web
 *  service getLookupData.
 *  
 * @author Tom
 *
 */
public class LookupData {
	
	public static final int LOOKUP_CATEGORY_QUERY_CAUSE = 3;
	
	private int id;
	private String value;
	
	public LookupData(int id, String value) {
		this.setId(id);
		this.setValue(value);
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
