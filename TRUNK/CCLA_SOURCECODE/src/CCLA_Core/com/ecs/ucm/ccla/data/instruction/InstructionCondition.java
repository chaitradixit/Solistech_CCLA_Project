package com.ecs.ucm.ccla.data.instruction;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.ucm.ccla.data.DataType;
import com.ecs.ucm.ccla.data.Operator;
import com.ecs.ucm.ccla.data.Persistable;
import com.ecs.ucm.ccla.utils.DateUtils;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.DataResultSetUtils;
import com.ecs.utils.stellent.DateFormatter;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Models entries from the INSTRUCTION_CONDITION table.
 *  
 *  Each Condition consists of a Condition Field, a Condition Operator and
 *  a check value/Condition Field.
 * 
 * @author Tom
 *
 */
public class InstructionCondition implements Persistable {
	
	//Binder fields
	public static final String INSTRUCTION_CONDITION_ID = "INSTRUCTION_CONDITION_ID";
	public static final String INSTRUCTION_DATA_ID = "INSTRUCTION_DATA_ID";
	public static final String OPERATOR_ID = "OPERATOR_ID";
	public static final String CHECK_VALUE = "CHECK_VALUE";
	public static final String CHECK_INSTRUCTION_DATA_ID = "CHECK_INSTRUCTION_DATA_ID";
	public static final String DESCRIPTION = "DESCRIPTION";
	public static final String LAST_UPDATED = "LAST_UPDATED";
	public static final String LAST_UPDATED_BY = "LAST_UPDATED_BY";
	
	private int conditionId;
	private String description;
	private String lastUpdatedBy;
	private Date lastUpdated;
	
	/** Field value to check against. */
	private InstructionData dataField;
	private Operator operator;
	
	/** Static check value. */
	private String checkValue;
	
	/** Dynamic check value. */
	private InstructionData checkDataField;
	
	/**
	 * Constructor for InstructionCondition
	 * @param conditionId
	 * @param dataField
	 * @param operator
	 * @param checkValue
	 * @param checkDataField
	 * @param description
	 * @param lastUpdatedBy
	 * @param lastUpdated
	 */
	public InstructionCondition(int conditionId, InstructionData dataField, 
			Operator operator, String checkValue, InstructionData checkDataField,
			String description, String lastUpdatedBy, Date lastUpdated) 
	throws DataException{
		super();
		this.conditionId = conditionId;
		this.setDataField(dataField);
		this.operator = operator;
		this.checkValue = checkValue;
		this.setCheckDataField(checkDataField);
		this.description = description;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdated = lastUpdated;
	}
	
	/**
	 * Constructor for InstructionCondition
	 * @param binder
	 * @throws DataException
	 */
	public InstructionCondition(DataBinder binder) throws DataException {
		this.setAttributes(binder);
	}
				
	public static Vector<InstructionCondition> getAll(FWFacade facade) 
	 throws DataException {
		
		Vector<InstructionCondition> conditions = new Vector<InstructionCondition>();
		
		DataResultSet rs = facade.createResultSet
		 ("qCore_GetAllInstructionConditions", new DataBinder());
		
		if (rs.first()) {
			do {
				conditions.add(get(rs));
			} while (rs.next());
		}
		
		return conditions;
	}
	
	/** Evaluates the Condition instance against the given Instruction.
	 * 
	 * @param comm
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public boolean eval(Instruction instr) throws DataException {
		
		// Fetch the field value. It could be missing/null.
		InstructionDataFieldValue instructionValue = 
		 instr.getDataApplied(this.getDataField().getInstructionDataId());

		InstructionDataFieldValue checkValue = null;
		
		if (this.getCheckDataField() != null) {
			// Must check the Comm Field value against a dynamically
			// calculated value.
			checkValue = instr.getDataApplied
			 (this.getCheckDataField().getInstructionDataId());
			
		} else {
			// Check the Comm Field value against a hardcoded value. Use the data type
			// from the data field to cast it correctly.
			
			//Special handling for dates
			if (this.getDataField().getDataType().equals(DataType.DATE)) 
			{
				//Try converting to normal date
				Date comparableDate = null;

				try {
					comparableDate = 
						DateFormatter.getSystemSimpleDateFormat().parse(
								this.getCheckValue());
				} catch (ParseException e) { 
					//Exception will be thrown later on.
				}
				
				//Try to use the number to work out the date, special rule for dates.
				if (comparableDate==null) 
				{
					try {
						
						float numDays = Float.parseFloat(this.getCheckValue());
						comparableDate = DateUtils.getDateFromDate(instructionValue.getDateValue(), numDays);
						if (comparableDate!=null) {
							instructionValue = new InstructionDataFieldValue(DataType.DATE);
							instructionValue.setDateValue(DateUtils.getNow());							
						}
					} catch (NumberFormatException nfe) { 
						//Exception will be thrown later on.
					}
					
				}
				
				if (comparableDate==null) {
					Log.error("-- Unable to parse Date "+this.getCheckValue());
					throw new DataException("Unable to parse date: " + this.getCheckValue());
				} 
				
				checkValue = new InstructionDataFieldValue
				(this.getDataField().getDataType());
			
				checkValue.setDateValue(comparableDate);
				
			} else {
				checkValue = new InstructionDataFieldValue
				(this.getDataField().getDataType());
			
				checkValue.setValue(this.getCheckValue());
			}
		}
		
		// Evaluate.
		// Must cast to appropriate field data type.
		return this.getOperator().eval(instructionValue, checkValue);
	}

	public int getConditionId() {
		return conditionId;
	}

	public void setConditionId(int conditionId) {
		this.conditionId = conditionId;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public String getCheckValue() {
		return checkValue;
	}

	public void setCheckValue(String checkValue) {
		this.checkValue = checkValue;
	}

	public void setDataField(InstructionData dataField) {
		this.dataField = dataField;
	}

	public InstructionData getDataField() {
		return dataField;
	}

	public void setCheckDataField(InstructionData checkDataField) {
		this.checkDataField = checkDataField;
	}

	public InstructionData getCheckDataField() {
		return checkDataField;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	
	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	
	public Date getLastUpdated() {
		return lastUpdated;
	}
	
	/*
	 *  Caching stuff
	 */
	private static Cache CACHE = new Cache();
	
	public static Cachable<Integer, InstructionCondition> getCache() {
		return CACHE;
	}
	
	/** InstructionCondition cache implementor */
	private static class Cache extends Cachable<Integer, InstructionCondition> {

		public Cache() {
			super("Instruction Condition");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<InstructionCondition> conds = InstructionCondition.getAll(facade);
			
			HashMap<Integer, InstructionCondition> newCache = 
			 new HashMap<Integer, InstructionCondition>();
			
			for (InstructionCondition cond : conds) {
				newCache.put(cond.getConditionId(), cond);
			}
			
			this.CACHE_MAP = newCache;
		}
	}

	/**************************** Persistable Interface ********************************/
	
	public void setAttributes(DataBinder binder) throws DataException 
	{		
		Integer conditionId = BinderUtils.getBinderIntegerValue(binder, INSTRUCTION_CONDITION_ID);
		if (conditionId!=null)
			this.setConditionId(conditionId);
		
		this.setCheckValue(BinderUtils.getBinderStringValue(binder, CHECK_VALUE));
		
		InstructionData instructionData = 
			InstructionData.getCache().getCachedInstance(
				BinderUtils.getBinderIntegerValue(binder, INSTRUCTION_DATA_ID));
		this.setDataField(instructionData);
		
		Operator operator = 
			Operator.getCache().getCachedInstance(
				BinderUtils.getBinderIntegerValue(binder, OPERATOR_ID));
		this.setOperator(operator);
		
		InstructionData checkInstructionData = 
			InstructionData.getCache().getCachedInstance(
				BinderUtils.getBinderIntegerValue(binder, CHECK_INSTRUCTION_DATA_ID));
		this.setCheckDataField(checkInstructionData);
		
		this.setDescription(BinderUtils.getBinderStringValue(binder, DESCRIPTION));
		this.setLastUpdatedBy(BinderUtils.getBinderStringValue(binder, LAST_UPDATED_BY));
		this.setLastUpdated(BinderUtils.getBinderDateValue(binder, LAST_UPDATED));
	}

	public void addFieldsToBinder(DataBinder binder) throws DataException 
	{
		BinderUtils.addIntParamToBinder(binder, INSTRUCTION_CONDITION_ID, this.getConditionId());
		BinderUtils.addIntParamToBinder(binder, OPERATOR_ID, this.getOperator().getOperatorId());
		BinderUtils.addParamToBinder(binder, CHECK_VALUE, this.getCheckValue());
		
		if (this.getCheckDataField()!=null)
			BinderUtils.addIntParamToBinder(binder, CHECK_INSTRUCTION_DATA_ID, this.getCheckDataField().getInstructionDataId());
		else {
			BinderUtils.addIntParamToBinder(binder, CHECK_INSTRUCTION_DATA_ID, null);			
		}
		
		BinderUtils.addIntParamToBinder(binder, INSTRUCTION_DATA_ID, this.getDataField().getInstructionDataId());		 		
		BinderUtils.addParamToBinder(binder, DESCRIPTION, this.getDescription());
		BinderUtils.addParamToBinder(binder, LAST_UPDATED_BY, this.getLastUpdatedBy());
		BinderUtils.addDateParamToBinder(binder, LAST_UPDATED, this.getLastUpdated());
	}

	public void persist(FWFacade facade, String username) throws DataException {
		this.validate(facade);
		this.setLastUpdatedBy(username);
		DataBinder binder = new DataBinder();		
		this.addFieldsToBinder(binder);
		facade.execute("qCore_UpdateInstructionCondition", binder);	
		
	}

	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub
		
	}

	/**************************** End Persistable Interface ********************************/
		

	/**
	 * Add an InstructionCondition to the database
	 * @param condition
	 * @param username
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static InstructionCondition add(InstructionCondition condition, String username, FWFacade facade) 
	throws DataException {
		
		int conditionId = 0;
		
		// Get new ID value if required
		if (condition.getConditionId()==0) {
			conditionId = Integer.parseInt(
			 CCLAUtils.getNewKey("InstructionCondition", facade));
			condition.setConditionId(conditionId);
		}
		
		condition.setLastUpdatedBy(username);
		condition.validate(facade);
		
		DataBinder binder = new DataBinder();
		condition.addFieldsToBinder(binder);
		facade.execute("qCore_AddInstructionCondition", binder);
		
		return InstructionCondition.get(conditionId, facade);
	}	
	
	/**
	 * Add an InstructionCondition to the Database
	 * @param binder
	 * @param username
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static InstructionCondition add(DataBinder binder, String username, FWFacade facade) 
	 throws DataException 
	 {
		InstructionCondition condition = new InstructionCondition(binder);
		return add(condition, username, facade);
	}

	/**
	 * Get an InstructionCondition for a given id
	 * @param conditionId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static InstructionCondition get(int conditionId, FWFacade facade) 
	 throws DataException {
		DataResultSet rs = getData(conditionId, facade);
		return get(rs);
	}
	
	/**
	 * Get the instructionCondition resultset by id
	 * @param conditionId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getData(int conditionId, FWFacade facade) 
	 throws DataException {
		DataBinder binder = new DataBinder();
		
		BinderUtils.addIntParamToBinder(binder, INSTRUCTION_CONDITION_ID, conditionId);
		DataResultSet rs = facade.createResultSet
		 ("qCore_GetInstructionConditionById", binder);
		
		return rs;
	}
	
	/**
	 * Create an InstructionCondition from the resultset.
	 * @param rs
	 * @return 
	 * @throws DataException
	 */
	public static InstructionCondition get(DataResultSet rs) throws DataException {
		if (rs.isEmpty())
			return null;
		
		return new InstructionCondition(
			DataResultSetUtils.getResultSetIntegerValue(rs, INSTRUCTION_CONDITION_ID),
			InstructionData.getCache().getCachedInstance(
				DataResultSetUtils.getResultSetIntegerValue(rs, INSTRUCTION_DATA_ID)
			),
			
			Operator.getCache().getCachedInstance(
				DataResultSetUtils.getResultSetIntegerValue(rs, OPERATOR_ID)
			),
			
			DataResultSetUtils.getResultSetStringValue(rs, CHECK_VALUE),
			InstructionData.getCache().getCachedInstance(
				DataResultSetUtils.getResultSetIntegerValue
				(rs, CHECK_INSTRUCTION_DATA_ID)),
			DataResultSetUtils.getResultSetStringValue(rs, DESCRIPTION),
			DataResultSetUtils.getResultSetStringValue(rs, LAST_UPDATED_BY),
			rs.getDateValueByName(LAST_UPDATED)
		);
	}
	
	
}
