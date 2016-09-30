package com.ecs.ucm.ccla.data;

import java.util.HashMap;
import java.util.Vector;

import com.ecs.utils.Log;
import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.ucm.ccla.data.instruction.InstructionDataFieldValue;
import com.ecs.utils.stellent.DataResultSetUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

/** Represents an entry from the REF_INSTRUCTION_CONDITION_OP table.
 * 
 *  Each available operator is hard-coded as static references in the class
 *  for easy access.
 *  
 * @author Tom
 *
 */
public class Operator {
	
	private int operatorId;
	private String name;
	private String symbol;
	
	public static final Operator EQUALS 		
	 = new Operator(1, "equals", "=");
	public static final Operator NOT_EQUALS 	
	 = new Operator(2, "does not equal", "!=");
	
	public static final Operator GREATER_THAN 	
	 = new Operator(3, "greater than", ">");
	public static final Operator LESS_THAN 		
	 = new Operator(4, "less than", "<");
	
	public static final Operator GREATER_THAN_OR_EQ 
	 = new Operator(5, "greater than or equal", ">=");
	public static final Operator LESS_THAN_OR_EQ 	
	 = new Operator(6, "less than or equal", "<=");
	
	public Operator(int operatorId, String name, String symbol) {
		super();
		this.operatorId = operatorId;
		this.name = name;
		this.setSymbol(symbol);
	}
	
	public static Vector<Operator> getAll(FWFacade facade) 
	 throws DataException {
		
		Vector<Operator> conditions = new Vector<Operator>();
		
		DataResultSet rs = facade.createResultSet
		 ("qCore_GetAllInstructionConditionOP", new DataBinder());
		
		if (rs.first()) {
			do {
				conditions.add(get(rs));
			} while (rs.next());
		}
		
		return conditions;
	}
	
	public static Operator get(DataResultSet rs) throws DataException {
		return new Operator(
			DataResultSetUtils.getResultSetIntegerValue(rs, "OPERATOR_ID"),
			DataResultSetUtils.getResultSetStringValue(rs, "OPERATOR_NAME"),
			DataResultSetUtils.getResultSetStringValue(rs, "OPERATOR_SYMBOL")
		);
	}
	
	public boolean equals(Operator op) {
		return (op.getOperatorId() == this.getOperatorId());
	}
	
	public void setOperatorId(int operatorId) {
		this.operatorId = operatorId;
	}
	public int getOperatorId() {
		return operatorId;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	
	/** String operand evaluation */
	public boolean eval(InstructionDataFieldValue instrValue, 
	 InstructionDataFieldValue checkValue) throws DataException {
		
		Log.debug("Eval method called");
		
		if (instrValue==null || checkValue==null)
			return false;
		
		// Check for 1 empty value
		if ((instrValue.isEmpty() && !checkValue.isEmpty())
			||
			(!instrValue.isEmpty() && checkValue.isEmpty()))
			return false;
		
		if (!instrValue.getDataType().equals(checkValue.getDataType())) {
			String msg = "Unable to perform data comparison, data type " +
			 "mismatch (" + instrValue.getDataType().getName() + 
			 ", " + checkValue.getDataType().getName();
			
			Log.error(msg);
			throw new DataException(msg);
		}
		
		if (this.equals(EQUALS))
			return instrValue.equals(checkValue);
		
		if (this.equals(NOT_EQUALS))
			return !instrValue.equals(checkValue);
		
		if (this.equals(GREATER_THAN))
			return instrValue.compareTo(checkValue) > 0;
			
		if (this.equals(LESS_THAN))
			return instrValue.compareTo(checkValue) < 0;
		
		if (this.equals(GREATER_THAN_OR_EQ))
			return instrValue.compareTo(checkValue) >= 0;
			
		if (this.equals(LESS_THAN_OR_EQ))
			return instrValue.compareTo(checkValue) <= 0;
		
		Log.error("Unsupported comparison operator "+this.getName()+":"+this.getOperatorId());
		throw new DataException("Unsupported comparison operator: " + this.getName());
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getSymbol() {
		return symbol;
	}
	
	/*
	 *  Caching stuff
	 */
	private static Cache CACHE = new Cache();
	
	public static Cachable<Integer, Operator> getCache() {
		return CACHE;
	}
	
	/** Condition Operator cache implementor */
	private static class Cache extends Cachable<Integer, Operator> {

		public Cache() {
			super("Condition Operator");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<Operator> ops = Operator.getAll(facade);
			
			HashMap<Integer, Operator> newCache = 
			 new HashMap<Integer, Operator>();
			
			for (Operator op : ops) {
				newCache.put(op.getOperatorId(), op);
			}
			
			this.CACHE_MAP = newCache;
		}
	}
}
