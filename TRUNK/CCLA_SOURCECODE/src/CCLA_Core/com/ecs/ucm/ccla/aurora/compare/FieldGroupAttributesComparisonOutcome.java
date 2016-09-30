package com.ecs.ucm.ccla.aurora.compare;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import intradoc.data.DataException;
import intradoc.data.DataResultSet;

/** Wrapper for comparison outcome between two FieldGroupAttributes, and the data the 
 *  comparison is based on
 * 
 * @author tm
 */
public class FieldGroupAttributesComparisonOutcome<A> {
	
	private FieldGroupConfig fieldGroupConfig;
	
	private FieldGroupAttributes<A> attributes1;
	private FieldGroupAttributes<A> attributes2;
	
	private Vector<String> mismatchedFields;
	
	/** Builds and evaluates the comparison outcome between the 2 FieldGroups. */
	public FieldGroupAttributesComparisonOutcome
	 (FieldGroupConfig fieldGroupConfig, 
	 FieldGroupAttributes<A> fieldGroup1, FieldGroupAttributes<A> fieldGroup2) 
	 throws DataException {
		this.fieldGroupConfig = fieldGroupConfig;
		
		this.attributes1 = fieldGroup1;
		this.attributes2 = fieldGroup2;
		
		this.mismatchedFields = fieldGroup1.getMismatches(fieldGroup2);
	}
	
	protected FieldGroupAttributesComparisonOutcome() {}
	
	/** Returns the list of mismatched field names. Can be empty */
	public Vector<String> getMismatchedFields() {
		return mismatchedFields;
	}
	
	/** Returns true if the mismatched field name list is empty, i.e. both field groups 
	 *  have matching values.
	 * 
	 * @return
	 */
	public boolean getOutcome() {
		return mismatchedFields.isEmpty();
	}

	public HashMap<String,String> getFieldGroup1Attributes() {
		return attributes1.getFieldValues();
	}

	public HashMap<String,String> getFieldGroup2Attributes() {
		return attributes2.getFieldValues();
	}
	
	private String getMatchLineOutput(String fieldName) {
		return fieldName + " [" + getFieldGroup1Attributes().get(fieldName) + 
		 "|" + getFieldGroup2Attributes().get(fieldName) + "] .." + 
		!getMismatchedFields().contains(fieldName);
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("Field Group Comparison Outcome: Group Name=" + 
		 this.getFieldGroupConfig().getName() + ", Field Count=" + 
		 this.getFieldGroup1Attributes().size() + 
		 ", match outcome=" + this.getOutcome());
		
		sb.append("\n");
		
		int line = 1;
		
		for (String fieldName : this.getFieldGroup1Attributes().keySet()) {
			sb.append("\t" + line + ": " + getMatchLineOutput(fieldName) + "\n");
			line++;
		}
		
		return sb.toString();
	}

	public FieldGroupConfig getFieldGroupConfig() {
		return fieldGroupConfig;
	}

	public void setFieldGroupConfig(FieldGroupConfig fieldGroupConfig) {
		this.fieldGroupConfig = fieldGroupConfig;
	}
	
	/** Returns a ResultSet with each row containing a Field Name and it's 2 associated 
	 *  values, and whether or not it is considered to be mismatched.
	 *  
	 * @return
	 */
	public DataResultSet getComparisonResultSet() {
		DataResultSet rs = new DataResultSet(
			new String[] {
				"FIELD_NAME", 
				"VALUE_1", 
				"VALUE_2",
				"IS_MISMATCHED"
			}
		);
		
		for (String fieldName : getFieldGroup1Attributes().keySet()) {
			Vector<String> v = new Vector<String>();
			
			v.add(fieldName);
			
			String value1 = getFieldGroup1Attributes().get(fieldName);
			String value2 = getFieldGroup2Attributes().get(fieldName);
			
			v.add(value1);
			v.add(value2);
			
			v.add(getMismatchedFields().contains(fieldName) ? "1" : "0");
			
			rs.addRow(v);
		}
		
		return rs;
	}
}
