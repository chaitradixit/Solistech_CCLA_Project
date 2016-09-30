package com.ecs.ucm.ccla.data;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

public class FundIncomeTypeCode implements Persistable {


	/* **************** Constants ******************** */
	public static final class Ids {
		public static final int INCOME = 1;
		public static final int ACCUMULATION = 2;
	}
	
	//Column names	
	public static final String COL_INCOME_TYPECODE_ID 		= "INCOME_TYPECODE_ID";
	public static final String COL_INCOME_TYPECODE 			= "INCOME_TYPECODE";
	public static final String COL_INCOMECODE_DESCRIPTION 	= "INCOMECODE_DESC";
		
	/* **************** Properties ******************* */
	private int incomeTypeCodeId = 0;
	private String incomeTypeCode = null;
	private String incomeCodeDescription = null;
	
	
	/* **************** Constructor ****************** */
	public FundIncomeTypeCode(int incomeTypeCodeId, String incomeTypeCode, String incomeCodeDescription) {
		super();
		this.incomeTypeCodeId = incomeTypeCodeId;
		this.incomeTypeCode = incomeTypeCode;
		this.incomeCodeDescription = incomeCodeDescription;
	}	
	
	/* **************** Methods ********************* */
	/**
	 * Getter for incomeTypeCodeId
	 * @return int (incomeTypeCodeId)
	 */
	public int getIncomeTypeCodeId() {
		return incomeTypeCodeId;
	}
	
	/**
	 * Setter for incomeTypeCodeId
	 * @param incomeTypeCodeId
	 */
	public void setIncomeTypeCodeId(int incomeTypeCodeId) {
		this.incomeTypeCodeId = incomeTypeCodeId;
	}
	
	/**
	 * Getter for incomeTypeCode
	 * @return String (incomeTypeCode)
	 */
	public String getIncomeTypeCode() {
		return incomeTypeCode;
	}
	
	/**
	 * Setter for incomeTypeCode
	 * @param incomeTypeCode
	 */
	public void setIncomeTypeCode(String incomeTypeCode) {
		this.incomeTypeCode = incomeTypeCode;
	}
	
	/**
	 * Getter for incomeCodeDescrption
	 * @return String incomeCodeDescription
	 */
	public String getIncomeCodeDescription() {
		return incomeCodeDescription;
	}
	
	/**
	 * Setter for incomeCodeDescription
	 * @param incomeCodeDescription
	 */
	public void setIncomeCodeDescription(String incomeCodeDescription) {
		this.incomeCodeDescription = incomeCodeDescription;
	}
	
	/**
	 * Static Getter for Object based on resultSet
	 * @param rsFundIncomeTypeCode (DataResultSet)
	 * @return FundIncomeTypeCode
	 */
	public static FundIncomeTypeCode get(DataResultSet rsFundIncomeTypeCode) throws DataException {
		
		FundIncomeTypeCode obj = null;
		
		if (rsFundIncomeTypeCode==null || rsFundIncomeTypeCode.isEmpty())
			return obj;
		
		obj = new FundIncomeTypeCode( 
			CCLAUtils.getResultSetIntegerValue(rsFundIncomeTypeCode, COL_INCOME_TYPECODE_ID),
			rsFundIncomeTypeCode.getStringValueByName(COL_INCOME_TYPECODE),
			rsFundIncomeTypeCode.getStringValueByName(COL_INCOMECODE_DESCRIPTION)
		);
		
		return obj;
	}

	/**
	 * Outputs all the properties to a string
	 * @return String
	 */
	public String toString() {
		String s = new String();
		s += "[[FundIncomeTypeCode " + incomeTypeCodeId + "]: incomeTypeCode =" +incomeTypeCode+ 
		", incomeCodeDescription=" +incomeCodeDescription +"]";
		
		return s;
	}	
	
	/* ************** Persistable Interface ****************** */
	
	public void setAttributes(DataBinder binder) throws DataException {
		// TODO Auto-generated method stub
		
	}

	public void addFieldsToBinder(DataBinder binder) throws DataException {
		// TODO Auto-generated method stub
		
	}

	public void persist(FWFacade facade, String username) throws DataException {
		// TODO Auto-generated method stub
		
	}

	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub
	}	
}
