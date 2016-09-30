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

/** Models entries in INSTR_RULE_CONDITION_APPLIED.
 *  
 *  Each entry indicates a mapping between an Instruction Rule and an Instruction 
 *  Condition.
 * 
 * @author Tom
 *
 */
public class InstructionRuleConditionApplied implements Persistable {

	/* ***************** Constants ***************************** */
	//BINDER VALUES
	private final static String INSTRUCTION_RULE_ID 		= "INSTRUCTION_RULE_ID";  
	private final static String INSTRUCTION_CONDITION_ID 	= "INSTRUCTION_CONDITION_ID";  
	private final static String USER_ID 					= "USER_ID";  
	private final static String DATE_ADDED 					= "DATE_ADDED";  
	private final static String LAST_UPDATED 				= "LAST_UPDATED";  
	
	/* ***************** Properties **************************** */
	private int instructionRuleId;
	private int instructionConditionId;
	private String userId;
	private Date dateAdded;
	private Date lastUpdated;
	
	
	public InstructionRuleConditionApplied(int instructionRuleId, 
			int instructionConditionId, 
			String userId, Date dateAdded, Date lastUpdated) throws DataException {
		this.instructionRuleId = instructionRuleId;
		this.instructionConditionId = instructionConditionId;
		this.userId = userId;
		this.dateAdded = dateAdded;
		this.lastUpdated = lastUpdated;
	}
	
	public InstructionRuleConditionApplied(DataBinder binder) throws DataException {
		this.setAttributes(binder);
	}
	
	public void setInstructionConditionId(int instructionConditionId) { 
		this.instructionConditionId = instructionConditionId; 
	}

	public int getInstructionConditionId() { return instructionConditionId; }
	
	public void setInstructionRuleId(int instructionRuleId) { 
		this.instructionRuleId = instructionRuleId; 
	}
	
	public int getInstructionRuleId() { return instructionRuleId; }

	public void setUserId(String userId) { this.userId = userId; }
	public String getUserId() { return userId; }
	
	public void setDateAdded(Date dateAdded) { this.dateAdded = dateAdded; }
	public Date getDateAdded() { return dateAdded; }
	
	public void setLastUpdated(Date lastUpdated) { this.lastUpdated = lastUpdated; }
	public Date getLastUpdated() { return lastUpdated; }
		
	/* *********************** Persistable Interface ************************ */	
	
	public void setAttributes(DataBinder binder) throws DataException {
		
		this.setInstructionConditionId(BinderUtils.getBinderIntegerValue(binder, INSTRUCTION_CONDITION_ID));
		this.setInstructionRuleId(BinderUtils.getBinderIntegerValue(binder, INSTRUCTION_RULE_ID));
		this.setLastUpdated(BinderUtils.getBinderDateValue(binder, LAST_UPDATED));
		this.setDateAdded(BinderUtils.getBinderDateValue(binder, DATE_ADDED));
		this.setUserId(BinderUtils.getBinderStringValue(binder, USER_ID));
		
	}

	public void addFieldsToBinder(DataBinder binder) throws DataException 
	{
		BinderUtils.addIntParamToBinder(binder, INSTRUCTION_CONDITION_ID, this.getInstructionConditionId());
		BinderUtils.addIntParamToBinder(binder, INSTRUCTION_RULE_ID, this.getInstructionRuleId());
		BinderUtils.addDateParamToBinder(binder, DATE_ADDED, this.getDateAdded());
		BinderUtils.addDateParamToBinder(binder, LAST_UPDATED, this.getLastUpdated());
		BinderUtils.addParamToBinder(binder, USER_ID, this.getUserId());
	}

	
	public void persist(FWFacade facade, String username) throws DataException {
		this.validate(facade);
		this.setUserId(username);
		DataBinder binder = new DataBinder();		
		this.addFieldsToBinder(binder);
		facade.execute("qCore_UpdateInstructionRuleConditionApplied", binder);	
	}

	public void validate(FWFacade facade) throws DataException {
		
	}
	
	/* ******************** End Persistable Interface ************************ */
	
	/* *************************** Cache **************************** */ 
	/*
	 *  Caching stuff
	 */
	private static Cache CACHE = new Cache();
	
	public static Cachable<Integer, Vector<InstructionRuleConditionApplied>> getCache() {
		return CACHE;
	}
	
	/** Condition Operator cache implementor */
	private static class Cache extends Cachable<Integer, Vector<InstructionRuleConditionApplied>> {

		public Cache() {
			super("InstructionRuleConditionApplied");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<InstructionRuleConditionApplied> ruleConditionApplied = 
				InstructionRuleConditionApplied.getAll(facade);
			
			HashMap<Integer, Vector<InstructionRuleConditionApplied>> newCache = 
			 new HashMap<Integer, Vector<InstructionRuleConditionApplied>>();
			
			for (InstructionRuleConditionApplied instrRuleCondApp : ruleConditionApplied) {
				
				int ruleId = instrRuleCondApp.getInstructionRuleId();
				
				if (newCache.containsKey(ruleId)) {
					newCache.get(ruleId).add(instrRuleCondApp);
				} else {
					Vector<InstructionRuleConditionApplied> v = 
					 new Vector<InstructionRuleConditionApplied>();
					
					v.add(instrRuleCondApp);
					newCache.put(ruleId, v);
				}
			}
			
			this.CACHE_MAP = newCache;
		}
	}
	/* *************************** End Cache **************************** */	
	

	
	public static Vector<InstructionRuleConditionApplied> getAll(FWFacade facade) throws DataException {
		Vector<InstructionRuleConditionApplied> rulesConditionApplied = new Vector<InstructionRuleConditionApplied>();
		
		DataResultSet rs = facade.createResultSet
		 ("qCore_GetAllInstructionRuleConditionApplied", new DataBinder());
		
		if (rs.first()) {
			do {
				rulesConditionApplied.add(get(rs));
			} while (rs.next());
		}
		
		return rulesConditionApplied;
	}
		
	/**
	 * Returns an InstructionRuleConditionApplied from the resultset
	 * @param rs
	 * @return
	 * @throws DataException
	 */
	public static InstructionRuleConditionApplied get(DataResultSet rs) throws DataException {
		
		if (rs.isEmpty())
			return null;
		
		return new InstructionRuleConditionApplied(
			DataResultSetUtils.getResultSetIntegerValue(rs, INSTRUCTION_RULE_ID),
			DataResultSetUtils.getResultSetIntegerValue(rs, INSTRUCTION_CONDITION_ID),
			DataResultSetUtils.getResultSetStringValue(rs, USER_ID),
			rs.getDateValueByName(DATE_ADDED),
			rs.getDateValueByName(LAST_UPDATED)
		);
	}	
	

	/**
	 * Returns an InstructionRuleConditionApplied from the resultset
	 * @param rs
	 * @return
	 * @throws DataException
	 */
	public static InstructionRuleConditionApplied get(int instructionRuleId, int instructionConditionId, FWFacade facade) throws DataException {

		DataResultSet rs = getData(instructionRuleId, instructionConditionId, facade);
		return get(rs);
	}		
	
	/**
	 * 
	 * @param ruleId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getData(int instructionRuleId, int instructionConditionId, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();

		BinderUtils.addIntParamToBinder(binder, INSTRUCTION_RULE_ID, instructionRuleId);
		BinderUtils.addIntParamToBinder(binder, INSTRUCTION_CONDITION_ID, instructionConditionId);
		DataResultSet rsData = facade.createResultSet
		 ("qCore_GetInstructionRuleConditionAppliedByFK", binder);
		
		return rsData;
	}
	
	
	/**
	 * Add an instruction rule condition applied
	 * @param ruleConditionApplied
	 * @param username
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static InstructionRuleConditionApplied add(InstructionRuleConditionApplied ruleConditionApplied, String username, FWFacade facade) 
	throws DataException {
		
		ruleConditionApplied.setUserId(username);
		ruleConditionApplied.validate(facade);
		
		DataBinder binder = new DataBinder();
		ruleConditionApplied.addFieldsToBinder(binder);
		facade.execute("qCore_AddInstructionRuleConditionApplied", binder);
		
		return ruleConditionApplied;
	}	
	
	/**
	 * Add an instruction rule condition applied
	 * @param binder
	 * @param username
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static InstructionRuleConditionApplied add(DataBinder binder, String username, FWFacade facade) 
	throws DataException {
		InstructionRuleConditionApplied ruleConditionApplied = new InstructionRuleConditionApplied(binder);
		return InstructionRuleConditionApplied.add(ruleConditionApplied, username, facade);
	}
}
