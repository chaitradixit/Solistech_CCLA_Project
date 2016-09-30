package com.ecs.ucm.ccla.utils;


import intradoc.data.DataException;

import java.util.Iterator;
import java.util.Vector;

import com.ecs.ucm.ccla.data.instruction.Instruction;
import com.ecs.ucm.ccla.data.instruction.InstructionCondition;
import com.ecs.ucm.ccla.data.instruction.InstructionRule;
import com.ecs.ucm.ccla.data.instruction.InstructionRulePriorityApplied;

import com.ecs.utils.Log;

public class PriorityCalculationUtils {	
	
	private static final int DEFAULT_PRIORITY = 0;
	
	/**
	 * Gets the priority for the instruction.
	 * @param instr
	 * @return
	 */
	public static int getPriority(Instruction instruction) 
	{
		int priority = DEFAULT_PRIORITY;
		int defaultRulePriority = DEFAULT_PRIORITY; // the priority to use if there are no conditions
		boolean hasConditions = false;
		
		if (instruction==null)
			return priority;
		
		Log.debug("Calculating Priority for Instruction :"+instruction.getInstructionId());
		
		if (!InstructionRule.getCache().isInitialized()) {
			Log.error("Cache is not initialised");
			return priority;
		} 
		else
		{
			//get all instruction rules for the instruction			
			for (Iterator<InstructionRule> rules = InstructionRule.getCache().getValuesIterator(); rules.hasNext();) 				
			{	
				InstructionRule instrRule = rules.next();
				
				Log.debug("PriorityCalc instrRuleId="+instrRule.getRuleId());
				defaultRulePriority = DEFAULT_PRIORITY;
				hasConditions = false;
				
				Vector<InstructionRulePriorityApplied> priorityApplied = null;
				
				try {
					priorityApplied = InstructionRulePriorityApplied.getCache().getCachedInstance(instrRule.getRuleId());
				
				} catch (DataException de) {
					Log.error("Cache is not initialised, return 0 for priority ignoring priorities");
				}
				
				
				//default behaviour if not conditions are applied.				
				if (priorityApplied!=null && !priorityApplied.isEmpty()) 
				{	
					Log.debug("PriorityCalc priority applied is not null for "+instrRule.getRuleId());
					boolean matches = false;
					
					try {
						matches = instrRule.eval(instruction);
					} catch (DataException de) {
						Log.error("Cannot evaluate InstructionRule "+de.getMessage());
					}
					
					if (matches) {
						for (InstructionRulePriorityApplied applied: priorityApplied) 
						{			
							//No conditions
							if (applied.getInstrCondId()==null) {
								Log.debug("Default PriorityCondition for rule"+applied.getInstrRuleId()+", matched, set default priority: "+applied.getPriority());
								defaultRulePriority = applied.getPriority();
							} else {
								try {	
									InstructionCondition condition = InstructionCondition.getCache().getCachedInstance(applied.getInstrCondId());
									if (condition.eval(instruction)) {
										Log.debug("PriorityCondition "+condition.getConditionId()+", matched, adding priority "+applied.getPriority());
										priority+=applied.getPriority();
									} else {
										Log.debug("PriorityCondition "+condition.getConditionId()+", not matched");
									}
									hasConditions = true;
								} catch (DataException de) {
									Log.error("Cannot calculate priority for this condition "+de.getMessage());
								}
							}
						}
						
						//If there are no conditions, then append the default priority.
						if (!hasConditions) {
							Log.debug("no PriorityCondition for rule "+instrRule.getRuleId()+", adding default priority: "+defaultRulePriority);
							
							priority+= defaultRulePriority;
						}
					}
				} else {
					Log.debug("PriorityCalc priorityApplied is null for instrRule "+instrRule.getRuleId());
					
				}
			}
			
			//Return the combined total
			return priority;
		}
	}
}
