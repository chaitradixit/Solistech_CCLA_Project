package com.ecs.stellent.ccla.clientservices;

import java.util.StringTokenizer;
import java.util.Vector;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.instruction.InstructionCondition;
import com.ecs.ucm.ccla.data.instruction.InstructionRule;
import com.ecs.ucm.ccla.data.instruction.InstructionRuleConditionApplied;
import com.ecs.ucm.ccla.data.instruction.InstructionRulePriorityApplied;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.server.Service;

public class InstructionRuleService extends Service {
	
	/**
	 * Adds a new Instruction Rule to the database.
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void addInstructionRule() throws DataException, ServiceException {

		//Rule name checks
		String ruleName = m_binder.getLocal(InstructionRule.RULE_NAME);
		if (StringUtils.stringIsBlank(ruleName))
			throw new DataException("Cannot add InstructionRule, rule name is not defined.");
		
		//Rule type checks
		Integer ruleType = CCLAUtils.getBinderIntegerValue(m_binder, InstructionRule.RULE_TYPE_ID); 
		if (ruleType==null)
			throw new DataException("Cannot add InstructionRule, rule type is not defined.");
			
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		InstructionRule rule = InstructionRule.get(ruleName, facade);
		
		if (rule!=null)
			throw new DataException("Cannot add InstructionRule, name is already used.");			
		
		try {
			Integer priority = CCLAUtils.getBinderIntegerValue(m_binder, InstructionRulePriorityApplied.PRIORITY);
			
			facade.beginTransaction();
			rule = InstructionRule.add(m_binder, m_userData.m_name, facade);			

			if (rule!=null && priority!=null) {
				DataBinder binder = new DataBinder();
				CCLAUtils.addQueryIntParamToBinder(binder,  InstructionRulePriorityApplied.PRIORITY, priority);
				CCLAUtils.addQueryIntParamToBinder(binder,  InstructionRule.RULE_ID, rule.getRuleId());
				InstructionRulePriorityApplied.add(binder, m_userData.m_name, facade);
			}
			
			facade.commitTransaction();
			
			// Append the new rule ID to RedirectUrl
			String redirectUrl = m_binder.getLocal("RedirectUrl");
			
			if (!StringUtils.stringIsBlank(redirectUrl)) {
				redirectUrl += rule.getRuleId();
				
				m_binder.putLocal("RedirectUrl", redirectUrl);
			}

			//refresh the instruction cache
			InstructionRule.getCache().buildCache(facade);

			if (priority!=null) {
				//rebuild the cache
				InstructionRulePriorityApplied.getCache().buildCache(facade);
			}
		
		} catch (DataException e) {
			facade.rollbackTransaction();
			Log.error("Error adding Instruction Rule", e);
			throw new ServiceException(e);
		}
	}

	
	/**
	 * Adds a new Instruction Condition to the database.
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void addInstructionCondition() throws DataException, ServiceException 
	{
		//Data Id checks
		Integer dataId = CCLAUtils.getBinderIntegerValue(m_binder, InstructionCondition.INSTRUCTION_DATA_ID);
		if (dataId==null)
			throw new DataException("Cannot Add Condition, dataId not defined");
		
		//Operator checks
		Integer operatorId = CCLAUtils.getBinderIntegerValue(m_binder, InstructionCondition.OPERATOR_ID);
		if (operatorId==null)
			throw new DataException("Cannot Add Condition, operator not defined");
		
		//Check Values checks
		Integer checkDataId = CCLAUtils.getBinderIntegerValue(m_binder, InstructionCondition.CHECK_INSTRUCTION_DATA_ID);
		String checkValue = m_binder.getLocal(InstructionCondition.CHECK_VALUE); 
		if (checkDataId==null && StringUtils.stringIsBlank(checkValue)) 
			throw new DataException("Cannot Add Condition, operator not defined");
		
		//Description checks
		String description = m_binder.getLocal(InstructionCondition.DESCRIPTION); 
		if (StringUtils.stringIsBlank(description))
			throw new DataException("Cannot Add Condition, operator not defined");

		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		try {
			facade.beginTransaction();	
			
			//Add an instruction condition
			InstructionCondition condition = InstructionCondition.add(m_binder, m_userData.m_name, facade);
			
			Integer ruleId = CCLAUtils.getBinderIntegerValue(m_binder, InstructionRule.RULE_ID);
			
			
			DataBinder binder = new DataBinder();
			
			if (ruleId!=null)
				CCLAUtils.addQueryIntParamToBinder(binder, InstructionRule.RULE_ID, ruleId);				
			
			if (condition!=null)
				CCLAUtils.addQueryIntParamToBinder(binder, InstructionCondition.INSTRUCTION_CONDITION_ID, condition.getConditionId());
			
			//Add it to the rule condition applied
			if (ruleId!=null && condition!=null) {
				InstructionRuleConditionApplied.add(binder, m_userData.m_name, facade);
			}

			
			facade.commitTransaction();
			
			//refresh the caches
			InstructionRule.getCache().buildCache(facade);
			InstructionRuleConditionApplied.getCache().buildCache(facade);
			
		} catch (DataException e) {
			facade.rollbackTransaction();
			Log.error("Error adding instruction condition", e);
			throw new ServiceException(e);
		} 
	}
	
	
	/**
	 * Adds a new Instruction Condition to the database.
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void addInstructionConditionPriority() throws DataException, ServiceException 
	{
		//Data Id checks
		Integer dataId = CCLAUtils.getBinderIntegerValue(m_binder, InstructionCondition.INSTRUCTION_DATA_ID);
		if (dataId==null)
			throw new DataException("Cannot Add Condition, dataId not defined");
		
		//Operator checks
		Integer operatorId = CCLAUtils.getBinderIntegerValue(m_binder, InstructionCondition.OPERATOR_ID);
		if (operatorId==null)
			throw new DataException("Cannot Add Condition, operator not defined");
		
		//Check Values checks
		Integer checkDataId = CCLAUtils.getBinderIntegerValue(m_binder, InstructionCondition.CHECK_INSTRUCTION_DATA_ID);
		String checkValue = m_binder.getLocal(InstructionCondition.CHECK_VALUE); 
		if (checkDataId==null && StringUtils.stringIsBlank(checkValue)) 
			throw new DataException("Cannot Add Condition, operator not defined");
		
		//Description checks
		String description = m_binder.getLocal(InstructionCondition.DESCRIPTION); 
		if (StringUtils.stringIsBlank(description))
			throw new DataException("Cannot Add Condition, operator not defined");

		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		try {
			facade.beginTransaction();	
			
			//Add an instruction condition
			InstructionCondition condition = InstructionCondition.add(m_binder, m_userData.m_name, facade);
			Integer ruleId = CCLAUtils.getBinderIntegerValue(m_binder, InstructionRule.RULE_ID);
			Integer priority = CCLAUtils.getBinderIntegerValue(m_binder, InstructionRulePriorityApplied.PRIORITY);

			DataBinder binder = new DataBinder();
			
			if (ruleId!=null)
				CCLAUtils.addQueryIntParamToBinder(binder, InstructionRule.RULE_ID, ruleId);				
			
			if (condition!=null)
				CCLAUtils.addQueryIntParamToBinder(binder, InstructionCondition.INSTRUCTION_CONDITION_ID, condition.getConditionId());
			
			if (priority!=null)
				CCLAUtils.addQueryIntParamToBinder(binder, InstructionRulePriorityApplied.PRIORITY, priority);

			//Add priority if exist
			if (ruleId!=null && condition!=null && priority!=null) {
				InstructionRulePriorityApplied.add(binder, m_userData.m_name, facade);
			}				
			
			facade.commitTransaction();
			
			//refresh the caches
			InstructionRuleConditionApplied.getCache().buildCache(facade);
			InstructionRulePriorityApplied.getCache().buildCache(facade);
			
		} catch (DataException e) {
			facade.rollbackTransaction();
			Log.error("Error adding instruction condition", e);
			throw new ServiceException(e);
		} 
	}	
	
	/**
	 * Adds a new condition ID to the database for a rule.
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void addConditionIdsToRule() throws DataException, ServiceException {
		
		String conditions = m_binder.getLocal("INSTRUCTION_CONDITIONS");
		if (StringUtils.stringIsBlank(conditions)) {
			Log.error("cannot add conditions to rule, no condition ids found");
			throw new DataException("cannot add conditions to rule, no condition ids found");
		}
		
		Integer ruleId = CCLAUtils.getBinderIntegerValue(m_binder, InstructionRule.RULE_ID);
		if (ruleId==null) {
			throw new DataException("Cannot add conditions to rule, ruleId is not defined.");
		}
		
		StringTokenizer st = new StringTokenizer(conditions,",");
		
		int conditionId = 0;
		String conditionIdStr = null;
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		InstructionRuleConditionApplied irca = null;
		Exception e = null;
		
		while (st.hasMoreTokens()) {
			
			try {
				conditionIdStr = st.nextToken();
				conditionId = Integer.parseInt(conditionIdStr.trim());
				irca = InstructionRuleConditionApplied.get(ruleId, conditionId, facade);
				
				//only add the condition to the rule if it does not exist.
				if (irca==null) {
					irca = new InstructionRuleConditionApplied(ruleId, conditionId, null, null, null);
					InstructionRuleConditionApplied.add(irca, m_userData.m_name, facade);
				} 
			} catch (NumberFormatException nfe) {
				Log.error("Cannot parse condition, condition "+(conditionIdStr==null?"null":conditionIdStr));
				e=nfe;
				break;
			} catch (DataException de) {
				Log.error("cannot add conditionId to rule, DataException",de);
				e=de;
				break;
			}
		}
		
		if (e!=null) {
			facade.rollbackTransaction();
			throw new ServiceException("Cannot add condition to rule");
		} else {
			facade.commitTransaction();
			InstructionRuleConditionApplied.getCache().buildCache(facade);
		}
	}	
	
	
	/**
	 * Adds a new condition ID to the database for a rule.
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void addConditionIdsToRuleWithPriority() throws DataException, ServiceException {
		
		String conditions = m_binder.getLocal("INSTRUCTION_CONDITIONS");
		Integer priority = CCLAUtils.getBinderIntegerValue(m_binder, InstructionRulePriorityApplied.PRIORITY);
		
		if (priority==null) {
			Log.error("cannot add conditions with priority to rule, no priority found");
			throw new DataException("cannot add conditions with priority to rule, no priority found");
		}
		
		if (StringUtils.stringIsBlank(conditions)) {
			Log.error("cannot add conditions to rule, no condition ids found");
			throw new DataException("cannot add conditions to rule, no condition ids found");
		}
		
		
		Integer ruleId = CCLAUtils.getBinderIntegerValue(m_binder, InstructionRule.RULE_ID);
		if (ruleId==null) {
			throw new DataException("Cannot add conditions to rule, ruleId is not defined.");
		}
		
		StringTokenizer st = new StringTokenizer(conditions,",");
		
		Integer conditionId = null;
		String conditionIdStr = null;

		InstructionRulePriorityApplied irpa = null;
		Exception e = null;
		
		if (st.hasMoreTokens()) {
			
			FWFacade facade = CCLAUtils.getFacade(m_workspace, true);			
			facade.beginTransaction();
		
			while (st.hasMoreTokens()) {
				
				try {
					conditionIdStr = st.nextToken();
					conditionId = Integer.parseInt(conditionIdStr.trim());
					
					irpa = InstructionRulePriorityApplied.get(ruleId, conditionId, facade);
					
					//only add the condition to the rule if it does not exist.
					if (irpa==null) {
						irpa = new InstructionRulePriorityApplied(ruleId, conditionId, priority, null, null, null);
						InstructionRulePriorityApplied.add(irpa, m_userData.m_name, facade);
					} 
				} catch (NumberFormatException nfe) {
					Log.error("Cannot parse condition, condition "+(conditionIdStr==null?"null":conditionIdStr));
					e=nfe;
					break;
				} catch (DataException de) {
					Log.error("cannot add conditionId to rule, DataException",de);
					e=de;
					break;
				}
			}
			
			if (e!=null) {
				facade.rollbackTransaction();
				throw new ServiceException("Cannot add condition to rule");
			} else {
				facade.commitTransaction();
				InstructionRulePriorityApplied.getCache().buildCache(facade);
				InstructionRuleConditionApplied.getCache().buildCache(facade);
				
			}
		}
	}	
		
	
	/**
	 * Update the Instruction Rule
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void updateInstructionRule() throws DataException, ServiceException 
	{
		Integer ruleId = CCLAUtils.getBinderIntegerValue(m_binder, InstructionRule.RULE_ID);
		
		if (ruleId==null) {
			throw new DataException("Cannot Update InstructionRule, id is not defined.");
		}
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		try {
			facade.beginTransaction();			
			InstructionRule rule = InstructionRule.get(ruleId, facade);
			rule.setAttributes(m_binder);
			rule.persist(facade, m_userData.m_name);

			//refresh the cache
			InstructionRule.getCache().buildCache(facade);
			
			Integer priority = CCLAUtils.getBinderIntegerValue(m_binder, InstructionRulePriorityApplied.PRIORITY);
			
			//Add/Update Priority if this doesn't exist
			if (rule!=null && priority!=null) {
				DataBinder binder = new DataBinder();
				CCLAUtils.addQueryIntParamToBinder(binder,  InstructionRulePriorityApplied.PRIORITY, priority);
				CCLAUtils.addQueryIntParamToBinder(binder,  InstructionRule.RULE_ID, rule.getRuleId());
				CCLAUtils.addQueryIntParamToBinder(binder,  InstructionCondition.INSTRUCTION_CONDITION_ID, null);
				
				Vector<InstructionRulePriorityApplied> priorityListing = InstructionRulePriorityApplied.getCache().getCachedInstance(rule.getRuleId());
					
				boolean found = false;
				for (InstructionRulePriorityApplied priorityApplied: priorityListing) {
					if (priorityApplied.getInstrCondId()==null 
							&& priorityApplied.getInstrRuleId()==rule.getRuleId()) {
						priorityApplied.setPriority(priority);
						priorityApplied.persist(facade, m_userData.m_name);
						found = true;
					}
				}
				
				if (!found)
					InstructionRulePriorityApplied.add(binder, m_userData.m_name, facade);
			}

			facade.commitTransaction();
			
			//refresh the instruction cache
			InstructionRule.getCache().buildCache(facade);

			if (priority!=null) {
				//rebuild the cache
				InstructionRulePriorityApplied.getCache().buildCache(facade);
			}
			
		} catch (DataException de) {
			facade.rollbackTransaction();
			Log.error("Error updating instructionID", de);
			throw new ServiceException(de);
		}
	}

	
	/**
	 * This will not delete the condition from the database but just for the rule.
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void removeInstructionConditionFromRule() throws DataException, ServiceException {
		
		Integer ruleId = CCLAUtils.getBinderIntegerValue(m_binder, InstructionRule.RULE_ID);
		if (ruleId==null)
			throw new DataException("Cannot remove InstructionCondition, ruleId is not defined.");
		
		Integer conditionId = CCLAUtils.getBinderIntegerValue(m_binder, InstructionCondition.INSTRUCTION_CONDITION_ID);
		if (conditionId==null) 
			throw new DataException("Cannot remove InstructionCondition, conditionId is not defined.");
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		try {
			DataBinder binder = new DataBinder();
			CCLAUtils.addQueryIntParamToBinder(binder, InstructionRule.RULE_ID, ruleId);
			CCLAUtils.addQueryIntParamToBinder(binder, InstructionCondition.INSTRUCTION_CONDITION_ID, conditionId);
			
			facade.beginTransaction();			
			facade.execute("qCore_DeleteInstrRuleConditionsAppliedByRuleIdAndCondtionId", binder);

			facade.commitTransaction();
			
			//finally refresh the cache.
			InstructionRuleConditionApplied.getCache().buildCache(facade);
			
		} catch (DataException de) {
			facade.rollbackTransaction();
			Log.error("Error updating instructionID", de);
			throw new ServiceException(de);
		}
		
	}	

	/**
	 * Deletes an instruction rule and corresponding references 
	 * in the instr_rule_conditions_applied table
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void deleteInstructionRule() throws DataException, ServiceException 
	{
		Integer ruleId = CCLAUtils.getBinderIntegerValue(m_binder, InstructionRule.RULE_ID);
		
		if (ruleId==null) {
			throw new DataException("Cannot delete InstructionRule, id is not defined.");
		}
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		try {
			facade.beginTransaction();						
			DataBinder binder = new DataBinder();
			CCLAUtils.addQueryIntParamToBinder(binder, InstructionRule.RULE_ID, ruleId);
			facade.execute("qCore_DeleteInstructionRulePriorityByRuleId", binder);
			facade.execute("qCore_DeleteInstrRuleConditionsAppliedByRuleId", binder);	
			facade.execute("qCore_DeleteInstructionRuleByRuleId", binder);

			facade.commitTransaction();

			//refresh the cache and rule condition applied cache.
			InstructionRule.getCache().buildCache(facade);
			InstructionRuleConditionApplied.getCache().buildCache(facade);
		
			// Append the new rule ID to RedirectUrl
			String redirectUrl = m_binder.getLocal("RedirectUrl");
			Log.debug("Redirect URL = "+redirectUrl);
			
		} catch (DataException de) {
			facade.rollbackTransaction();
			Log.error("Error deleting instructionID",de);
			throw new ServiceException(de);
		}
	}		
	
	/**
	 * 
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void deleteCondition() throws DataException, ServiceException {
		//TODO
	}	
	/**
	 * Search for a list of rules
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void searchRules() throws DataException, ServiceException {
		
		//TODO
	}
	
	/**
	 * Search for a list of conditions
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void searchConditions() throws DataException, ServiceException 
	{	
		Integer dataFieldId = CCLAUtils.getBinderIntegerValue(m_binder, InstructionCondition.INSTRUCTION_DATA_ID);
		Integer checkDataFieldId = CCLAUtils.getBinderIntegerValue(m_binder, InstructionCondition.CHECK_INSTRUCTION_DATA_ID);
		Integer operatorId = CCLAUtils.getBinderIntegerValue(m_binder, InstructionCondition.OPERATOR_ID);
		
		String checkValue = m_binder.getLocal(InstructionCondition.CHECK_VALUE);
		String description = m_binder.getLocal(InstructionCondition.DESCRIPTION);
		
		if (dataFieldId==null && checkDataFieldId==null && operatorId==null
				&& StringUtils.stringIsBlank(checkValue) && StringUtils.stringIsBlank(description)) {
			Log.info("Cannot perform search, no search parameter specified.");
			//throw new DataException("Cannot perform search, no search parameter specified.");
		} else {		
			//Create a dynamic search query based on the form details
			final StringBuffer query =  new StringBuffer(20);
			boolean includeAND = false; 

			query.append("SELECT IC.* FROM INSTRUCTION_CONDITION IC WHERE ");
			
			if (dataFieldId!=null) {
				query.append("IC."+InstructionCondition.INSTRUCTION_DATA_ID+"="+dataFieldId.intValue());			
				includeAND = true;
			}
			
			if (operatorId!=null) {
				if (includeAND) 
					query.append(" AND ");
				
				query.append("IC."+InstructionCondition.OPERATOR_ID+"="+operatorId.intValue());
				includeAND=true;
			}
	
			if (checkDataFieldId!=null) {
				if (includeAND)
					query.append(" AND ");;
				
				query.append("IC."+InstructionCondition.CHECK_INSTRUCTION_DATA_ID+"="+checkDataFieldId);
				includeAND=true;			
			}
			
			if (!StringUtils.stringIsBlank(checkValue)) {
				if (includeAND)
					query.append(" AND ");
				
				query.append("IC."+InstructionCondition.CHECK_VALUE+"='"+checkValue+"'");
			}
			
			if (!StringUtils.stringIsBlank(description)) {
				if (includeAND)
					query.append(" AND ");
				
				query.append("IC."+InstructionCondition.DESCRIPTION+"='"+description+"'");
			}
			
			FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
			DataResultSet rsConditions = facade.createResultSetSQL(query.toString());
			
			if (rsConditions!=null && !rsConditions.isEmpty()) {
				m_binder.addResultSet("rsConditions", rsConditions);
			}
		}
	}
	
	/**
	 * Gets all the condition for the a specific ruleId
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void getAllConditionsForRules() throws DataException, ServiceException 
	{
		Integer ruleId = CCLAUtils.getBinderIntegerValue(m_binder, InstructionRule.RULE_ID);
		if (ruleId==null) {
			throw new DataException("Cannot show InstructionRule conditions, rule id is not defined.");
		}
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, InstructionRule.RULE_ID, ruleId);
		
		DataResultSet rsInstrConditions = 
			facade.createResultSet("qCore_GetAllInstructionConditionsForRuleId", binder);
		
		if (rsInstrConditions!=null && !rsInstrConditions.isEmpty()) {
			m_binder.addResultSet("rsInstrConditions", rsInstrConditions);
		}
	}

	
	/**
	 * Gets all the condition for the a specific ruleId
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void getAllPriorityConditionsForRules() throws DataException, ServiceException 
	{
		Integer ruleId = CCLAUtils.getBinderIntegerValue(m_binder, InstructionRule.RULE_ID);
		if (ruleId==null) {
			throw new DataException("Cannot show InstructionRule conditions, rule id is not defined.");
		}
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, InstructionRule.RULE_ID, ruleId);
		
		DataResultSet rsInstrPriorities = 
			facade.createResultSet("qCore_GetAllInstructionPrioritiesForRuleId", binder);
		
		if (rsInstrPriorities!=null && !rsInstrPriorities.isEmpty()) {
			m_binder.addResultSet("rsInstrPriorities", rsInstrPriorities);
		}
	}
	
	public void getInstructionRule() throws DataException, ServiceException 
	{
		Integer ruleId = CCLAUtils.getBinderIntegerValue(m_binder, InstructionRule.RULE_ID);
		if (ruleId==null) {
			throw new DataException("Cannot show InstructionRule, rule id is not defined.");
		}
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, InstructionRule.RULE_ID, ruleId);
		
		DataResultSet rsRule = 
			facade.createResultSet("qCore_GetInstructionRuleById", binder);
		
		if (rsRule!=null && !rsRule.isEmpty()) {
			m_binder.addResultSet("rsInstrRule", rsRule);
		} else
			Log.debug("Rule is empty");
	}
	
	public void getInstructionRuleWithPriority() throws DataException, ServiceException 
	{
		Integer ruleId = CCLAUtils.getBinderIntegerValue(m_binder, InstructionRule.RULE_ID);
		if (ruleId==null) {
			throw new DataException("Cannot show InstructionRule, rule id is not defined.");
		}
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, InstructionRule.RULE_ID, ruleId);
		
		DataResultSet rsRule = 
			facade.createResultSet("qCore_GetInstructionRuleWithPriorityById", binder);
		
		if (rsRule!=null && !rsRule.isEmpty()) {
			m_binder.addResultSet("rsInstrRule", rsRule);
		} else
			Log.debug("Rule is empty");
	}
	
	public void addInstructionRulePriority() throws DataException, ServiceException 
	{	
		Integer instrRuleId = 
			CCLAUtils.getBinderIntegerValue(m_binder, InstructionRulePriorityApplied.INSTRUCTION_RULE_ID);
		
		Integer instrConditionId = 
			CCLAUtils.getBinderIntegerValue(m_binder, InstructionRulePriorityApplied.INSTRUCTION_CONDITION_ID);
		
		
		Integer priority = 
			CCLAUtils.getBinderIntegerValue(m_binder, InstructionRulePriorityApplied.PRIORITY);
		
		if (instrRuleId==null) {
			throw new ServiceException("Missing instruction rule id");	
		} 

		if (priority==null) {
			throw new ServiceException("Missing priority value");	
		} 
		
		InstructionRulePriorityApplied applied = 
			new InstructionRulePriorityApplied(instrRuleId, 
					instrConditionId, priority , null, m_userData.m_name, null);
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		InstructionRulePriorityApplied.add(applied, m_userData.m_name, facade);
		
		//refresh the cache.
		InstructionRulePriorityApplied.getCache().buildCache(facade);
	}
	
	public void updateInstructionRulePriority() throws DataException, ServiceException 
	{
		Integer instrRuleId = 
			CCLAUtils.getBinderIntegerValue(m_binder, InstructionRulePriorityApplied.INSTRUCTION_RULE_ID);
		
		Integer instrConditionId = 
			CCLAUtils.getBinderIntegerValue(m_binder, InstructionRulePriorityApplied.INSTRUCTION_CONDITION_ID);
		
		Integer priority = 
			CCLAUtils.getBinderIntegerValue(m_binder, InstructionRulePriorityApplied.PRIORITY);
		
		if (instrRuleId==null) {
			throw new ServiceException("Missing instruction rule id");	
		} 

		if (priority==null) {
			throw new ServiceException("Missing priority value");	
		} 
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		InstructionRulePriorityApplied applied = 
			InstructionRulePriorityApplied.get(instrRuleId, instrConditionId, facade);
		
		if (applied==null)
			throw new ServiceException("Cannot find instruction rule priority applied");
		
		applied.setPriority(priority);
		
		applied.persist(facade, m_userData.m_name);

		//rebuild the cache
		InstructionRulePriorityApplied.getCache().buildCache(facade);

	}

	/**
	 * 
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void removeInstructionRulePriority() throws DataException, ServiceException 
	{
		Integer instrRuleId = 
			CCLAUtils.getBinderIntegerValue(m_binder, InstructionRulePriorityApplied.INSTRUCTION_RULE_ID);
		
		Integer instrConditionId = 
			CCLAUtils.getBinderIntegerValue(m_binder, InstructionRulePriorityApplied.INSTRUCTION_CONDITION_ID);
		
		
		if (instrRuleId==null) {
			throw new ServiceException("Missing instruction rule id");	
		} 
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		removeInstructionRulePriority(instrRuleId, instrConditionId, facade);
		InstructionRulePriorityApplied.getCache().buildCache(facade);
		
	}
	
	private void removeInstructionRulePriority(int ruleId, Integer ruleCondId, FWFacade facade) throws DataException, ServiceException {
		
		InstructionRulePriorityApplied applied = 
			InstructionRulePriorityApplied.get(ruleId, ruleCondId, facade);
		
		if (applied!=null) {
			InstructionRulePriorityApplied.remove(ruleId, ruleCondId, facade);	
			InstructionRulePriorityApplied.getCache().buildCache(facade);
		}
	}
}
