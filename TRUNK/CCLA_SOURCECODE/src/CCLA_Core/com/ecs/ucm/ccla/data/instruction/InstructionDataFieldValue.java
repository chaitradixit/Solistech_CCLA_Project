package com.ecs.ucm.ccla.data.instruction;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;


import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.DataType;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.DateFormatter;

/** Used for storing a single Applied Instruction Data value. Represents a sub-set
 *  of fields from the INSTRUCTION_DATA_APPLIED table.
 * 
 *  Each InstructionDataApplied instance will have one of these as a member var. It is
 *  used for handling conversions of specific field types into their raw String values,
 *  and storing them in the correct format in the DB.
 *  
 *  There is no setRawValue() function, the rawValue is set automatically when any of
 *  the data type-specific setters are called (e.g. setDateValue). 
 *  
 *  You can use the setValue() method on an InstructionDataApplied instance, which will 
 *  automatically type the raw String value for you, based on the Instruction Data type.
 *  
 * @author Tom
 *
 */
public class InstructionDataFieldValue implements Comparable<InstructionDataFieldValue> {
	
	private String 	rawValue;
	
	private String 	stringValue;
	private Date 	dateValue;
	private BigDecimal bigDecimalValue;
	private Integer intValue;
	private Boolean boolValue;
	
	private DataType dataType;
	
	protected InstructionDataFieldValue() {}
	
	public InstructionDataFieldValue(DataType dataType) throws DataException {
		this(null, null, null, null, null, null, dataType);
	}
	
	public InstructionDataFieldValue(String rawValue, String stringValue,
	 Date dateValue, BigDecimal bigDecimalValue, Integer intValue, Boolean boolValue, 
	 DataType dataType) throws DataException {
		this.rawValue = rawValue;
		this.stringValue = stringValue;
		this.dateValue = dateValue;
		this.bigDecimalValue = bigDecimalValue;
		this.intValue = intValue;
		this.boolValue = boolValue;
		
		if (dataType == null)
			throw new DataException("Data Type not specified");
		
		this.dataType = dataType;
	}
	
	/** Simple constructor - performs a cast of the passed Object value to its appropriate
	 *  DataType.
	 *  
	 *  Will fail with a ClassCastException if the value parameter isn't the expected
	 *  object type.
	 *  
	 * @param dataType
	 * @param value
	 * @throws DataException
	 */
	public InstructionDataFieldValue(DataType dataType, Object value) 
	 throws DataException {
		
		if (dataType == null)
			throw new DataException("Data Type not specified");

		this.dataType = dataType;
		
		if (value != null) {
			if (dataType.equals(DataType.STRING))
				this.setStringValue((String)value);
			else if (dataType.equals(DataType.INT))
				this.setIntValue((Integer)value);
			else if (dataType.equals(DataType.BOOL))
				this.setBoolValue((Boolean)value);
			else if (dataType.equals(DataType.FLOAT))
				this.setBigDecimalValue((BigDecimal)value);
			else if (dataType.equals(DataType.DATE))
				this.setDateValue((Date)value);
		}
	}
	
	/** Returns an InstructionDataFieldValue instance from the given DataResultSet,
	 *  expected to contain entries from the INSTRUCTION_DATA_APPLIED table.
	 *  
	 * @param rs
	 * @return
	 * @throws DataException
	 */
	public static InstructionDataFieldValue get(DataResultSet rs, DataType type) 
	 throws DataException {
		
		BigDecimal floatValue = null;
		Integer intValue = null;
		Boolean boolValue = null;
		
		if (type.equals(DataType.FLOAT))
			floatValue = CCLAUtils.getResultSetBigDecimalValue
			 (rs, "INSTRUCTION_NUM_VALUE");
		else if (type.equals(DataType.INT))
			intValue = CCLAUtils.getResultSetIntegerValue
			 (rs, "INSTRUCTION_NUM_VALUE");
		else if (type.equals(DataType.BOOL))
			boolValue = CCLAUtils.getResultSetBoolValueAllowNull
			 (rs, "INSTRUCTION_NUM_VALUE");
		
		return new InstructionDataFieldValue(
			CCLAUtils.getResultSetStringValue(rs, "INSTRUCTION_VALUE"),
			CCLAUtils.getResultSetStringValue(rs, "INSTRUCTION_STRING_VALUE"),
			rs.getDateValueByName("INSTRUCTION_DATE_VALUE"),
			floatValue,
			intValue,
			boolValue,
			type
		);
	}
	
	public void addFieldsToBinder(DataBinder binder) {
		CCLAUtils.addQueryParamToBinder
		 (binder, "INSTRUCTION_VALUE", this.getRawValue());
		CCLAUtils.addQueryParamToBinder
		 (binder, "INSTRUCTION_STRING_VALUE", this.getStringValue());
		
		// The NUM_VALUE field is overloaded with 3 data types. These must be
		// cast correctly before being posted as SQL parameters.
		if (this.getDataType().equals(DataType.INT))
			CCLAUtils.addQueryIntParamToBinder
			 (binder, "INSTRUCTION_NUM_VALUE", this.getIntValue());
		else if (this.getDataType().equals(DataType.BOOL))
			CCLAUtils.addQueryBooleanParamToBinderAllowNull
			 (binder, "INSTRUCTION_NUM_VALUE", this.getBoolValue());
		else if (this.getDataType().equals(DataType.FLOAT))
			CCLAUtils.addQueryBigDecimalParamToBinder
			 (binder, "INSTRUCTION_NUM_VALUE", this.getBigDecimalValue());
		else
			CCLAUtils.addQueryParamToBinder
			 (binder, "INSTRUCTION_NUM_VALUE", null);
		
		CCLAUtils.addQueryDateParamToBinder
		 (binder, "INSTRUCTION_DATE_VALUE", this.getDateValue());
	}
	
	/** Casts the raw String value into the appropriate data type.
	 * 
	 * @param value
	 * @throws DataException
	 */
	public void setValue(String value) throws DataException {
		
		if (dataType == null)
			throw new DataException("Missing Data Type");
		
		if (StringUtils.stringIsBlank(value)) {
			// No need to cast data types etc. if the value is null/empty.
			this.clearAllValues();
			return;
		}
		
		if (dataType.equals(DataType.STRING)) {
			this.setStringValue(value);
		
		} else if (dataType.equals(DataType.DATE)) {
			try {
				this.setDateValue(
			     DateFormatter.getSystemSimpleDateFormat().parse(value));
			} catch (ParseException e) {
				throw new DataException("Unable to parse date: " + value, e);
			}
	
		} else if (dataType.equals(DataType.FLOAT)) {
			this.setBigDecimalValue(new BigDecimal(value));
		
		} else if (dataType.equals(DataType.INT)) {
			try {
				this.setIntValue(Integer.parseInt(value));
			} catch (NumberFormatException nfe) {
				throw new DataException("Unable to int: " + value, nfe);				
			}
			
		} else if (dataType.equals(DataType.BOOL)) {
			if (value.equals("0") || value.equalsIgnoreCase("N")) {
				this.setBoolValue(false); // False
			} else {
				this.setBoolValue(true); // True
			}
				
		} else 
			throw new DataException("Unknown Data Type");
	}
	
	private void clearAllValues() {
		this.stringValue = null;
		this.dateValue = null;
		
		this.bigDecimalValue = null;
		this.intValue = null;
		this.boolValue = null;
		
		this.rawValue = null;
	}
	
	/**
	 * 
	 * @return true if the raw value is empty/null.
	 */
	public boolean isEmpty() {
		return StringUtils.stringIsBlank(this.getRawValue());
	}
	
	public String getRawValue() {
		return rawValue;
	}
	
	public String getStringValue() {
		return stringValue;
	}
	public void setStringValue(String stringValue) throws DataException {
		if (!this.dataType.equals(DataType.STRING))
			throw new DataException("Illegal data type set request expecting string, got:"+this.getDataType().getName());
		
		this.stringValue = stringValue;
		
		this.rawValue = stringValue;
	}
	
	public Date getDateValue() {
		return dateValue;
	}
	
	public void setDateValue(Date dateValue) throws DataException {
		if (!this.dataType.equals(DataType.DATE))
			throw new DataException("Illegal data type set request expecting date, got:"+this.getDataType().getName());
		
		this.dateValue = dateValue;
		
		if (dateValue == null)
			this.rawValue = null;
		else
			this.rawValue = DateFormatter.getTimeStamp(dateValue);
	}
	
	public BigDecimal getBigDecimalValue() {
		return bigDecimalValue;
	}
	
	public void setBigDecimalValue(BigDecimal bigDecimalValue) throws DataException {
		if (!this.dataType.equals(DataType.FLOAT))
			throw new DataException("Illegal data type set request expecting float, got:"+this.getDataType().getName());
		
		this.bigDecimalValue = bigDecimalValue;
		
		if (bigDecimalValue == null)
			this.rawValue = null;
		else	
			this.rawValue = bigDecimalValue.toPlainString();
	}

	public void setIntValue(Integer intValue) throws DataException {
		if (!this.dataType.equals(DataType.INT))
			throw new DataException("Illegal data type set request expecting int, got:"+this.getDataType().getName());
		
		this.intValue = intValue;
		
		if (intValue == null)
			this.rawValue = null;
		else
			this.rawValue = Integer.toString(intValue);
	}

	public Integer getIntValue() {
		return this.intValue;
	}

	public void setBoolValue(Boolean boolValue) throws DataException {
		if (!this.dataType.equals(DataType.BOOL))
			throw new DataException("Illegal data type set request expecting bool, got:"+this.getDataType().getName());
		
		this.boolValue = boolValue;
		
		if (boolValue == null)
			this.rawValue = null;
		else if (boolValue)
			this.rawValue = "1";
		else
			this.rawValue = "0";
	}

	public Boolean getBoolValue() {
		return this.boolValue;
	}
	
	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public DataType getDataType() {
		return dataType;
	}
	
	public String toString() {
		return ("FieldValue[raw=" + this.getRawValue() + 
				", string=" + this.getStringValue() +
				", float=" + this.getBigDecimalValue() + 
				", int=" + this.getIntValue() + 
				", bool=" + this.getBoolValue() +
				", date=" + this.getDateValue() + "]");
	}

	public boolean equals(InstructionDataFieldValue compareValue) {
		
		if ((this.isEmpty() && !compareValue.isEmpty()) || 
				(!this.isEmpty() && compareValue.isEmpty()))
			return false;
		
		return ((this.isEmpty() && compareValue.isEmpty())				
				||
				(this.getRawValue().equals(compareValue.getRawValue()))
			   );
	}
	
	/** Compares against another InstructionDataFieldValue.
	 *  
	 *  Prior to calling this function, you must ensure that the data types of the
	 *  two DataFieldValues match and one or both of them isn't empty.
	 * 
	 */
	public int compareTo(InstructionDataFieldValue compareValue) {
		
		if (this.equals(compareValue))
			return 0;
		else if (this.getDataType().equals(DataType.FLOAT))
			return this.getBigDecimalValue().compareTo(compareValue.getBigDecimalValue());
		else if (this.getDataType().equals(DataType.INT))
			return this.getIntValue().compareTo(compareValue.getIntValue());
		else if (this.getDataType().equals(DataType.DATE))
			return this.getDateValue().compareTo(compareValue.getDateValue());
		else
			return 0;
	}
}
