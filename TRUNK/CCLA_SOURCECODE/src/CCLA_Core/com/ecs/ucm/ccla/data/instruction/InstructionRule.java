package com.ecs.ucm.ccla.data.instruction;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.ucm.ccla.data.Persistable;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.DataResultSetUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Represents an entry from the INSTRUCTION_RULE table.
 *  
 *  A rule is defined by its unique name and mapping against
 *  a series of InstructionCondition instances.
 *  
 * @author Tom
 *
 */
public class InstructionRule implements Persistable{

	private int ruleId;
	private int ruleTypeId;
	private String name;
	
	private Date dateAdded;
	private String addedBy;

	//BINDER VALUES
	public static final String RULE_ID = "INSTRUCTION_RULE_ID";
	public static final String RULE_TYPE_ID = "RULE_TYPE_ID";
	public static final String ADDED_BY = "ADDED_BY";
	public static final String DATE_ADDED = "DATE_ADDED";
	public static final String RULE_NAME = "RULE_NAME"; 
	
	/**
	 * Rule types
	 */
	public static final int RULE_TYPE_AND = 0;
	public static final int RULE_TYPE_OR = 1;
	
	/**
	 * Constructor for InstructionRule
	 * @param ruleId
	 * @param name
	 * @param dateAdded
	 * @param addedBy
	 * @param ruleTypeId
	 * @throws DataException
	 */
	public InstructionRule(int ruleId, String name, Date dateAdded, String addedBy, int ruleTypeId) 
	throws DataException 
	{
		this.ruleId = ruleId;
		this.name = name;
		this.dateAdded = dateAdded;
		this.ruleTypeId = ruleTypeId;
		this.addedBy = addedBy;
	}
	
	/**
	 * Construction for InstructionRule
	 * @param binder
	 * @throws DataException
	 */
	public InstructionRule(DataBinder binder) throws DataException {
		this.setAttributes(binder);
	}
	
	/**
	 * Gets a Single Instruction Rule 
	 * @param ruleId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static InstructionRule get(int ruleId, FWFacade facade) 
	 throws DataException {
		DataResultSet rs = getData(ruleId, facade);
		return get(rs);
	}

	/**
	 * Gets a Single Instruction Rule 
	 * @param ruleName
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static InstructionRule get(String ruleName, FWFacade facade) 
	 throws DataException {
		DataResultSet rs = getData(ruleName, facade);
		return get(rs);
	}	

	
	/** 
	 * Fetches ResultSet for a single Instruction Rule by Id 
	 * This includes any priority associated with the main rule.
	 * @param ruleId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getExtendedData(int ruleId, FWFacade facade) 
	 throws DataException {
		DataBinder binder = new DataBinder();
		
		BinderUtils.addIntParamToBinder(binder, RULE_ID, ruleId);
		DataResultSet rsInstructionRule = facade.createResultSet
		 ("qCore_GetInstructionRuleWithPriorityById", binder);
		
		return rsInstructionRule;
	}	
	
	/** 
	 * Fetches ResultSet for a single Instruction Rule by Id.
	 * @param ruleId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getData(int ruleId, FWFacade facade) 
	 throws DataException {
		DataBinder binder = new DataBinder();
		
		BinderUtils.addIntParamToBinder(binder, RULE_ID, ruleId);
		DataResultSet rsInstructionRule = facade.createResultSet
		 ("qCore_GetInstructionRuleById", binder);
		
		return rsInstructionRule;
	}
	
	/**
	 * Fetches ResultSet for a single Instruction Rule by name.
	 * @param ruleName
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getData(String ruleName, FWFacade facade) 
	 throws DataException {
		DataBinder binder = new DataBinder();
		
		BinderUtils.addParamToBinder(binder, RULE_NAME, ruleName);
		DataResultSet rsInstructionRule = facade.createResultSet
		 ("qCore_GetInstructionRuleByName", binder);
		
		return rsInstructionRule;
	}
	
	
	/**
	 * Returns an InstructionRule from the resultset
	 * @param rs
	 * @return
	 * @throws DataException
	 */
	public static InstructionRule get(DataResultSet rs) throws DataException {
		
		if (rs.isEmpty())
			return null;
		
		return new InstructionRule(
			DataResultSetUtils.getResultSetIntegerValue(rs, RULE_ID),
			DataResultSetUtils.getResultSetStringValue(rs, RULE_NAME),
			rs.getDateValueByName(DATE_ADDED),
			DataResultSetUtils.getResultSetStringValue(rs, ADDED_BY),
			DataResultSetUtils.getResultSetIntegerValue(rs, RULE_TYPE_ID)		
		);
	}
	
	public static Vector<InstructionRule> getAll(FWFacade facade) throws DataException {
		Vector<InstructionRule> rules = new Vector<InstructionRule>();
		
		DataResultSet rs = facade.createResultSet
		 ("qCore_GetAllInstructionRules", new DataBinder());
		
		if (rs.first()) {
			do {
				rules.add(get(rs));
			} while (rs.next());
		}
		
		return rules;
	}
	
	/** Determines whether the passed Instruction passes all conditions
	 *  for this rule.
	 *  
	 * @param comm
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public boolean eval(Instruction instr) throws DataException {
		
		Log.debug("Evaluating Instruction "+instr.getInstructionId()+", against rule "+this.getRuleId());
		boolean success = false;
		
		Vector<InstructionRuleConditionApplied> conditionApplied = 
			InstructionRuleConditionApplied.getCache().getCachedInstance(this.getRuleId());

		//default behaviour if not conditions are applied.				
		if (conditionApplied==null || conditionApplied.isEmpty()) {
			Log.debug("Not found any conditions, returning true");
			return true;
		}
		
		Log.debug("found some conditions, evaluating conditions");
		
		for (InstructionRuleConditionApplied applied: conditionApplied) {
			
			InstructionCondition condition = InstructionCondition.getCache().getCachedInstance(applied.getInstructionConditionId());
			
			if (condition!=null) {
				Log.debug("Found Condition, "+condition.getConditionId());
				
				success = condition.eval(instr);
				
				if (this.getRuleTypeId()==RULE_TYPE_AND) {
					if (!success)
						return success;
				} else if (this.getRuleTypeId()==RULE_TYPE_OR) {
					if (success)
						return success;
				}
			} else {
				Log.debug("Not Found Condition, "+applied.getInstructionConditionId());
				return false;
			}
		}
		
		return true;
	}
	
	public int getRuleId() {
		return ruleId;
	}
	public void setRuleId(int ruleId) {
		this.ruleId = ruleId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Date getDateAdded() {
		return dateAdded;
	}
	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}
	
	public void setRuleTypeId(int ruleTypeId) {
		this.ruleTypeId = ruleTypeId;
	}
	
	public int getRuleTypeId() {
		return ruleTypeId;
	}
	
	public void setAddedBy(String addedBy) {
		this.addedBy = addedBy;
	}
	
	public String getAddedBy() {
		return addedBy;
	}
		
	/* *************************** Cache **************************** */ 
	/*
	 *  Caching stuff
	 */
	private static Cache CACHE = new Cache();
	
	public static Cachable<Integer, InstructionRule> getCache() {
		return CACHE;
	}
	
	/** Condition Operator cache implementor */
	private static class Cache extends Cachable<Integer, InstructionRule> {

		public Cache() {
			super("Instruction Rule");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<InstructionRule> rules = InstructionRule.getAll(facade);
			
			HashMap<Integer, InstructionRule> newCache = 
			 new HashMap<Integer, InstructionRule>();
			
			for (InstructionRule rule : rules) {
				newCache.put(rule.getRuleId(), rule);
			}
			
			this.CACHE_MAP = newCache;
		}
	}

	
	/* *************************** End Cache **************************** */

	
	
	/* ******************** Persistable Interface ************************ */
	
	/**
	 * Use the binder values to populate this object.  
	 */
	public void setAttributes(DataBinder binder) throws DataException 
	{
		Integer ruleId = BinderUtils.getBinderIntegerValue(binder, RULE_ID);
		
		if (ruleId!=null)
			this.setRuleId(ruleId);
		
		this.setRuleTypeId(BinderUtils.getBinderIntegerValue(binder, RULE_TYPE_ID));
		this.setDateAdded(BinderUtils.getBinderDateValue(binder, DATE_ADDED));
		this.setName(BinderUtils.getBinderStringValue(binder, RULE_NAME));	
		this.setAddedBy(BinderUtils.getBinderStringValue(binder, ADDED_BY));
	}

	/**
	 * Use the object values to populate to the binder.
	 */
	public void addFieldsToBinder(DataBinder binder) throws DataException 
	{
		BinderUtils.addIntParamToBinder(binder, RULE_ID, this.getRuleId());
		BinderUtils.addIntParamToBinder(binder, RULE_TYPE_ID, this.getRuleTypeId());
		BinderUtils.addDateParamToBinder(binder, DATE_ADDED, this.getDateAdded());
		BinderUtils.addParamToBinder(binder, RULE_NAME, this.getName());
		BinderUtils.addParamToBinder(binder, ADDED_BY, this.getAddedBy());		
	}

	/**
	 * Persist the object into the database
	 */
	public void persist(FWFacade facade, String username) throws DataException 
	{
		this.validate(facade);
		this.setAddedBy(username);
		DataBinder binder = new DataBinder();		
		this.addFieldsToBinder(binder);
		
		facade.execute("qCore_UpdateInstructionRule", binder);	
	}

	/**
	 * Validate the data
	 */
	public void validate(FWFacade facade) throws DataException {
		//TODO
	}
	/* ******************** End Persistable Interface ************************ */
	
	/**
	 * Adds a new InstructionRule to the database
	 * @param rule
	 * @param username
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static InstructionRule add(InstructionRule rule, String username, FWFacade facade) 
	throws DataException {
		
		int ruleId = 0;
		
		// Get new ID value if required
		if (rule.getRuleId()==0) {
			ruleId = Integer.parseInt(
			 CCLAUtils.getNewKey("InstructionRule", facade));
			rule.setRuleId(ruleId);
		}
		
		rule.setAddedBy(username);
		rule.validate(facade);
		
		DataBinder binder = new DataBinder();
		rule.addFieldsToBinder(binder);
		facade.execute("qCore_AddInstructionRule", binder);
		
		return InstructionRule.get(ruleId, facade);
	}
	
	/**
	 * Adds a new InstructionRule to the database
	 * @param binder
	 * @param username
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static InstructionRule add(DataBinder binder, String username, FWFacade facade) 
	throws DataException {
		InstructionRule rule = new InstructionRule(binder);
		return InstructionRule.add(rule, username, facade);
	}
	
}
