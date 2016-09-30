package com.ecs.ucm.ccla.data.instruction;

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.ucm.ccla.data.Persistable;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.DataResultSetUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/**
 * Models INSTR_RULE_PRIORITY_APPLIED Table
 * @author Cam
 *
 */

public class InstructionRulePriorityApplied implements Persistable 
{
	/* ******* Constants ******* */	
	//Binder values
	public static final String INSTRUCTION_RULE_ID 		= "INSTRUCTION_RULE_ID";
	public static final String INSTRUCTION_CONDITION_ID = "INSTRUCTION_CONDITION_ID";
	public static final String PRIORITY 				= "PRIORITY";
	public static final String LAST_UPDATED 			= "LAST_UPDATED";
	public static final String LAST_UPDATED_BY 			= "LAST_UPDATED_BY";
	public static final String DATE_ADDED				= "DATE_ADDED";
	
	//Queries
	public static final String ADD_QUERY_NAME 			= "qCore_AddInstructionRulePriorityApplied";
	public static final String UPDATE_QUERY_NAME 		= "qCore_UpdateInstructionRulePriorityApplied";
	public static final String UPDATE_BASE_QUERY_NAME 	= "qCore_UpdateBaseInstructionRulePriorityApplied";
	public static final String REMOVE_QUERY_NAME 		= "qCore_RemoveInstructionRulePriorityApplied";
	public static final String GET_ALL_QUERY_NAME 		= "qCore_GetAllInstructionRulePriorityApplied";
	public static final String GET_BY_IDS_QUERY_NAME 	= "qCore_GetInstructionRulePriorityAppliedByFK";
	
	
	/* ******* Properties ******* */
	private int instrRuleId;
	private Integer instrCondId;
	private int priority;
	private Date lastUpdated;
	private String lastUpdatedBy;
	private Date dateAdded;
	/**
	 * Constructor
	 * @param instrRuleId
	 * @param instrCondId
	 * @param priority
	 * @param lastUpdated
	 * @param lastUpdatedBy
	 */
	public InstructionRulePriorityApplied(int instrRuleId, Integer instrCondId, 
			int priority, Date lastUpdated, String lastUpdatedBy, Date dateAdded) {
		this.instrRuleId = instrRuleId;
		this.instrCondId = instrCondId;
		this.priority = priority;
		this.lastUpdated = lastUpdated;
		this.lastUpdatedBy = lastUpdatedBy;
		this.dateAdded = dateAdded;
	}
	
	/**
	 * Constructor
	 * @param binder
	 * @throws DataException
	 */
	public InstructionRulePriorityApplied(DataBinder binder) throws DataException {
		this.setAttributes(binder);
	}
	
	
	public int getInstrRuleId() { return instrRuleId; }
	public void setInstrRuleId(int instrRuleId) { this.instrRuleId = instrRuleId; }
	
	public Integer getInstrCondId() { return instrCondId; }
	public void setInstrCondId(Integer instrCondId) { this.instrCondId = instrCondId; }
	
	public int getPriority() { return priority; }
	public void setPriority(int priority) { this.priority = priority; } 
	
	public Date getLastUpdated() { return lastUpdated; }
	public void setLastUpdated(Date lastUpdated) { this.lastUpdated = lastUpdated; }
	
	public String getLastUpdatedBy() { return lastUpdatedBy; }
	public void setLastUpdatedBy(String lastUpdatedBy) { this.lastUpdatedBy = lastUpdatedBy; }
	
	public void setDateAdded(Date dateAdded) { this.dateAdded = dateAdded; }
	public Date getDateAdded() { return dateAdded; }		
	
	/* ******************** Persistable Interface ********************** */
	public void setAttributes(DataBinder binder) throws DataException 
	{
		this.setInstrRuleId(BinderUtils.getBinderIntegerValue(binder,INSTRUCTION_RULE_ID));
		this.setInstrCondId(BinderUtils.getBinderIntegerValue(binder, INSTRUCTION_CONDITION_ID));
		this.setPriority(BinderUtils.getBinderIntegerValue(binder, PRIORITY));
		this.setLastUpdatedBy(BinderUtils.getBinderStringValue(binder, LAST_UPDATED_BY));
		this.setLastUpdated(BinderUtils.getBinderDateValue(binder, LAST_UPDATED));
		this.setDateAdded(BinderUtils.getBinderDateValue(binder, DATE_ADDED));
		
	}

	public void addFieldsToBinder(DataBinder binder) throws DataException 
	{
		BinderUtils.addIntParamToBinder(binder, INSTRUCTION_RULE_ID, this.getInstrRuleId());
		BinderUtils.addIntParamToBinder(binder, INSTRUCTION_CONDITION_ID, this.getInstrCondId());
		BinderUtils.addIntParamToBinder(binder, PRIORITY, this.getPriority());
		BinderUtils.addDateParamToBinder(binder, LAST_UPDATED, this.getLastUpdated());
		BinderUtils.addParamToBinder(binder, LAST_UPDATED_BY, this.getLastUpdatedBy());
		BinderUtils.addDateParamToBinder(binder, DATE_ADDED, this.getDateAdded());
	}

	public void persist(FWFacade facade, String username) throws DataException {	
		this.validate(facade);
		this.setLastUpdatedBy(username);
		DataBinder binder = new DataBinder();		
		this.addFieldsToBinder(binder);
		
		if (this.getInstrCondId()==null)
			facade.execute(UPDATE_BASE_QUERY_NAME, binder);	
		else
			facade.execute(UPDATE_QUERY_NAME, binder);	
 
	}

	
	public static void remove(int getInstrRuleId, int instrCondId, FWFacade facade) throws DataException {	
		DataBinder binder = new DataBinder();		
		BinderUtils.addIntParamToBinder(binder, INSTRUCTION_RULE_ID, getInstrRuleId);
		BinderUtils.addIntParamToBinder(binder, INSTRUCTION_CONDITION_ID, instrCondId);
		facade.execute(REMOVE_QUERY_NAME, binder);	
	}
	
	
	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub

	}
	/* ******************* End Persistable Interface ********************** */

	/* *************************** Cache **************************** */ 
	/*
	 *  Caching stuff
	 */
	private static Cache CACHE = new Cache();
	
	public static Cachable<Integer, Vector<InstructionRulePriorityApplied>> getCache() {
		return CACHE;
	}
	
	/** Condition Operator cache implementor */
	private static class Cache extends Cachable<Integer, Vector<InstructionRulePriorityApplied>> {

		public Cache() {
			super("InstructionRulePriorityApplied");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<InstructionRulePriorityApplied> rulePriorityApplied = 
				InstructionRulePriorityApplied.getAll(facade);
			
			HashMap<Integer, Vector<InstructionRulePriorityApplied>> newCache = 
			 new HashMap<Integer, Vector<InstructionRulePriorityApplied>>();
			
			for (InstructionRulePriorityApplied instrRulePriorityApp : rulePriorityApplied) {
				
				int ruleId = instrRulePriorityApp.getInstrRuleId();
				
				if (newCache.containsKey(ruleId)) {
					newCache.get(ruleId).add(instrRulePriorityApp);
				} else {
					Vector<InstructionRulePriorityApplied> v = 
					 new Vector<InstructionRulePriorityApplied>();
					
					v.add(instrRulePriorityApp);
					newCache.put(ruleId, v);
				}
			}
			
			this.CACHE_MAP = newCache;
		}
	}
	/* *************************** End Cache **************************** */	
	
	/**
	 * Create an InstructionRulePriorityApplied Object from the DataResultSet
	 * @param rs
	 * @return
	 * @throws DataException
	 */
	public static InstructionRulePriorityApplied get(DataResultSet rs) throws DataException 
	{
		if (rs.isEmpty())
			return null;

		return new InstructionRulePriorityApplied(
			DataResultSetUtils.getResultSetIntegerValue(rs, INSTRUCTION_RULE_ID),
			DataResultSetUtils.getResultSetIntegerValue(rs, INSTRUCTION_CONDITION_ID),
			DataResultSetUtils.getResultSetIntegerValue(rs, PRIORITY),
			rs.getDateValueByName(LAST_UPDATED),
			DataResultSetUtils.getResultSetStringValue(rs, LAST_UPDATED_BY),	
			rs.getDateValueByName(DATE_ADDED)
		);
	}	
	
	
	/**
	 * Returns an InstructionRulePriorityApplied from the resultset
	 * @param rs
	 * @return
	 * @throws DataException
	 */
	public static InstructionRulePriorityApplied get(int instructionRuleId, Integer instructionConditionId, FWFacade facade) throws DataException {

		DataResultSet rs = getData(instructionRuleId, instructionConditionId, facade);
		return get(rs);
	}			

	
	public static DataResultSet getData(int instructionRuleId, Integer instructionConditionId, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();

		BinderUtils.addIntParamToBinder(binder, INSTRUCTION_RULE_ID, instructionRuleId);
		BinderUtils.addIntParamToBinder(binder, INSTRUCTION_CONDITION_ID, instructionConditionId);
		DataResultSet rsData = facade.createResultSet
		 (GET_BY_IDS_QUERY_NAME, binder);
		
		return rsData;
	}	
	
	/**
	 * Get a vector of all InstructionRulePriorityApplied objects.
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Vector<InstructionRulePriorityApplied> getAll(FWFacade facade) throws DataException {
		
		Vector<InstructionRulePriorityApplied> rulesConditionApplied = 
			new Vector<InstructionRulePriorityApplied>();
		
		DataResultSet rs = facade.createResultSet
		 (GET_ALL_QUERY_NAME, new DataBinder());
		
		if (rs.first()) {
			do {
				rulesConditionApplied.add(get(rs));
			} while (rs.next());
		}		
		return rulesConditionApplied;
	}

	/**
	 * Add an InstructionRulePriorityApplied to the database
	 * @param rulePriorityApplied
	 * @param username
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static InstructionRulePriorityApplied add(InstructionRulePriorityApplied rulePriorityApplied, String username, FWFacade facade) 
	throws DataException {
		
		rulePriorityApplied.setLastUpdatedBy(username);
		rulePriorityApplied.validate(facade);
		
		DataBinder binder = new DataBinder();
		rulePriorityApplied.addFieldsToBinder(binder);
		facade.execute(ADD_QUERY_NAME, binder);
		
		return rulePriorityApplied;
	}	
	
	/**
	 * Add an InstructionRulePriorityApplied to the database
	 * @param binder
	 * @param username
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static InstructionRulePriorityApplied add(DataBinder binder, String username, FWFacade facade) 
	throws DataException {
		InstructionRulePriorityApplied ruleConditionApplied = new InstructionRulePriorityApplied(binder);
		return InstructionRulePriorityApplied.add(ruleConditionApplied, username, facade);
	}

}
