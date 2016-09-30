package com.ecs.ucm.ccla.data.instruction;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.DataResultSetUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Represents an entry from the IRMODULE_INSTR_RULE_APPLIED table.
 * 
 * @author Tom
 *
 */
public class ModuleRuleApplied {
	
	private int moduleRuleAppliedId;
	
	private int moduleId;
	private InstructionRule instructionRule;
	
	private RoutingModule.RuleType ruleType;
	private InstructionProcess failProcess;
	private Integer evaluationOrder;
	
	private Date dateAdded;

	public ModuleRuleApplied(int moduleRuleAppliedId, int moduleId,
	 InstructionRule instructionRule, RoutingModule.RuleType ruleType, 
	 InstructionProcess failProcess, Integer evaluationOrder,
	 Date dateAdded) {
		this.moduleRuleAppliedId = moduleRuleAppliedId;
		this.moduleId = moduleId;
		this.setInstructionRule(instructionRule);
		this.ruleType = ruleType;
		this.failProcess = failProcess;
		this.evaluationOrder = evaluationOrder;
		this.dateAdded = dateAdded;
	}
	
	public static Vector<ModuleRuleApplied> getAll(FWFacade facade) 
	 throws DataException {
		
		Vector<ModuleRuleApplied> rulesApplied = new Vector<ModuleRuleApplied>();
		
		DataResultSet rs = facade.createResultSet
		 ("qCommProc_GetAllModuleInstructionRulesApplied", new DataBinder());
		
		if (rs.first()) {
			do {
				rulesApplied.add(get(rs));
			} while (rs.next());
		}
		
		return rulesApplied;
	}
	
	public static ModuleRuleApplied get(DataResultSet rs) throws DataException {
		if (rs.isEmpty())
			return null;
		
		InstructionProcess failProcess = null;
		Integer failProcessId = DataResultSetUtils.getResultSetIntegerValue
		 (rs, "INSTRUCTION_PROCESS_ID");
		
		if (failProcessId != null)
			failProcess = InstructionProcess.getCache().getCachedInstance
			 (failProcessId);
		
		return new ModuleRuleApplied(
			DataResultSetUtils.getResultSetIntegerValue(rs, "IRMODULE_INSTR_RULE_ID"),
			DataResultSetUtils.getResultSetIntegerValue(rs, "MODULE_ID"),
			
			InstructionRule.getCache().getCachedInstance(
			 DataResultSetUtils.getResultSetIntegerValue(rs, "INSTRUCTION_RULE_ID")
			),
			
			RoutingModule.RuleType.valueOf(
			 DataResultSetUtils.getResultSetStringValue(rs, "MODULE_RULE_TYPE")
			),
			
			failProcess,
			DataResultSetUtils.getResultSetIntegerValue(rs, "EVALUATION_ORDER"),
			
			rs.getDateValueByName("DATE_ADDED")
		);
	}

	public int getModuleRuleAppliedId() {
		return moduleRuleAppliedId;
	}

	public void setModuleRuleAppliedId(int moduleRuleAppliedId) {
		this.moduleRuleAppliedId = moduleRuleAppliedId;
	}

	public int getModuleId() {
		return moduleId;
	}

	public void setModuleId(int moduleId) {
		this.moduleId = moduleId;
	}

	public RoutingModule.RuleType getRuleType() {
		return ruleType;
	}

	public void setRuleType(RoutingModule.RuleType ruleType) {
		this.ruleType = ruleType;
	}

	public Date getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}
	
	/*
	 *  Caching stuff
	 */
	private static Cache CACHE = new Cache();
	
	public static Cachable<Integer, Vector<ModuleRuleApplied>> getCache() {
		return CACHE;
	}
	
	public void setInstructionRule(InstructionRule instructionRule) {
		this.instructionRule = instructionRule;
	}

	public InstructionRule getInstructionRule() {
		return instructionRule;
	}

	public void setInstructionProcess(InstructionProcess instructionProcess) {
		this.failProcess = instructionProcess;
	}

	public InstructionProcess getInstructionProcess() {
		return failProcess;
	}

	public void setEvaluationOrder(Integer evaluationOrder) {
		this.evaluationOrder = evaluationOrder;
	}

	public Integer getEvaluationOrder() {
		return evaluationOrder;
	}

	/** ModuleRuleApplied cache implementor.
	 *  
	 *  Maps Module IDs against sets of mapped Instruction Rules.
	 *  */
	private static class Cache extends Cachable<Integer, Vector<ModuleRuleApplied>> {

		public Cache() {
			super("Module Rules Applied");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<ModuleRuleApplied> rulesApplied = ModuleRuleApplied.getAll(facade);
			
			HashMap<Integer, Vector<ModuleRuleApplied>> newCache = 
			 new HashMap<Integer, Vector<ModuleRuleApplied>>();
			
			for (ModuleRuleApplied ruleApplied : rulesApplied) {
				if (newCache.containsKey(ruleApplied.getModuleId())) {
					Vector<ModuleRuleApplied> v = 
					 newCache.get(ruleApplied.getModuleId());
					
					v.add(ruleApplied);
				} else {
					Vector<ModuleRuleApplied> v = new Vector<ModuleRuleApplied>();
					v.add(ruleApplied);
					
					newCache.put(ruleApplied.getModuleId(), v);
				}
			}
			
			this.CACHE_MAP = newCache;
		}
	}
}
