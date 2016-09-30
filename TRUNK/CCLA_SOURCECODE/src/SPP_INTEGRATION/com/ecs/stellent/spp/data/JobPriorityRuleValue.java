package com.ecs.stellent.spp.data;

import java.util.Date;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.SharedCols;
import com.ecs.ucm.ccla.utils.DateUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Models single entries from the JOB_PRIORITY_RULE_VALUES table.
 * 
 * @author Tom
 *
 */
public class JobPriorityRuleValue {
	
	//DB column names
	public static final String RULE_ID 			= "RULE_ID";
	public static final String RULE_VALUE_ID 	= "RULE_VALUE_ID";
	public static final String FIELD_VALUE 		= "FIELD_VALUE";
	public static final String PRIORITY 		= "PRIORITY";
	public static final String DATE_ADDED 		= SharedCols.DATE_ADDED;
	public static final String NUM_VALID_DAYS	= "NUM_VALID_DAYS";
	
	
	private int ruleValueId;
	private int ruleId;
	
	private String fieldValue;
	private int priority;
	
	private Date dateAdded;
	private Integer numValidDays;
	


	public JobPriorityRuleValue(int ruleValueId, int ruleId, String fieldValue,
			int priority, Date dateAdded, Integer numValidDays) {
		this.ruleValueId = ruleValueId;
		this.ruleId = ruleId;
		this.fieldValue = fieldValue;
		this.priority = priority;
		this.dateAdded = dateAdded; 
		this.numValidDays = numValidDays;
	}
	
	public static JobPriorityRuleValue get(DataResultSet rs) throws DataException {

		return new JobPriorityRuleValue(
				CCLAUtils.getResultSetIntegerValue(rs, RULE_VALUE_ID),
				CCLAUtils.getResultSetIntegerValue(rs, RULE_ID),
				CCLAUtils.getResultSetStringValue(rs, FIELD_VALUE),
				CCLAUtils.getResultSetIntegerValue(rs, PRIORITY),		
				rs.getDateValueByName(DATE_ADDED),		
				CCLAUtils.getResultSetIntegerValue(rs, NUM_VALID_DAYS)			
		);
	}
	
	public void addFieldsToBinder(DataBinder binder) {
		CCLAUtils.addQueryIntParamToBinder(binder, RULE_VALUE_ID, this.getRuleValueId());
		CCLAUtils.addQueryIntParamToBinder(binder, RULE_ID, this.getRuleId());
		CCLAUtils.addQueryParamToBinder(binder, FIELD_VALUE, this.getFieldValue());
		CCLAUtils.addQueryIntParamToBinder(binder, PRIORITY, this.getPriority());
		CCLAUtils.addQueryDateParamToBinder(binder, DATE_ADDED, this.getDateAdded());
		CCLAUtils.addQueryIntParamToBinder(binder, NUM_VALID_DAYS, this.getNumValidDays());
	}
	
	public void persist(FWFacade facade) throws DataException {
		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);
		facade.execute("QUpdateJobPriorityRuleValue", binder);
	}
	
	public static DataResultSet getAllJobPriorityRuleValuesData(FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		return facade.createResultSet("QGetAllJobPriorityRuleValues", binder);
	}
	
	/** Adds a new Job Priority Rule Value. The primary key RULE_VALUE_ID
	 *  is derived using a database trigger on row insert.
	 *  
	 * @param ruleId
	 * @param fieldValue
	 * @param priority
	 * @param facade
	 * @throws DataException
	 */
	public static void add(int ruleId, String fieldValue, int priority, Integer numValidDays,
	 FWFacade facade) throws DataException 
	 {
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder(binder, RULE_ID, ruleId);
		CCLAUtils.addQueryIntParamToBinder(binder, PRIORITY, priority);
		CCLAUtils.addQueryParamToBinder(binder, FIELD_VALUE, fieldValue);
		CCLAUtils.addQueryIntParamToBinder(binder, NUM_VALID_DAYS, numValidDays);
		
		facade.execute("QAddJobPriorityRuleValue", binder);
	}
	
	/** Removes an existing Rule Value. 
	 *
	 **/
	public static void remove(int ruleValueId, FWFacade facade) 
	 throws DataException 
	 {
		DataBinder binder = new DataBinder();		
		CCLAUtils.addQueryIntParamToBinder(binder, RULE_VALUE_ID, ruleValueId);
		facade.execute("QRemoveJobPriorityRuleValue", binder);
	}

	public int getRuleValueId() {
		return ruleValueId;
	}

	public void setRuleValueId(int ruleValueId) {
		this.ruleValueId = ruleValueId;
	}

	public int getRuleId() {
		return ruleId;
	}

	public void setRuleId(int ruleId) {
		this.ruleId = ruleId;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}

	public Date getDateAdded() {
		return dateAdded;
	}
	
	public Integer getNumValidDays() {
		return numValidDays;
	}

	public void setNumValidDays(Integer numValidDays) {
		this.numValidDays = numValidDays;
	}
	
	/**
	 * Determines whether this rule value has expired or not.
	 * @return
	 */
	public boolean isExpired() {
		if (this.getNumValidDays()==null)
			return false;
		
		Date expiryDate = DateUtils.addDays(
							this.getDateAdded(), 
							this.getNumValidDays());
		
		return (DateUtils.compareDates(DateUtils.getNow(), expiryDate)>0);
	}
}
