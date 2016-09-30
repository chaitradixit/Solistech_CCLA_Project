package com.ecs.ucm.ccla.data;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

public class FundTypeCode implements Persistable {
	
	/* **************** Constants ******************** */
	//TypeCode_ID
	public static final int FUND_TYPECODE_ID_UNDEFINED 	= 0;
	public static final int FUND_TYPECODE_ID_UNITIZED 	= 1;
	public static final int FUND_TYPECODE_ID_DEPOSIT 	= 2;
	public static final int FUND_TYPECODE_ID_PSDF 		= 3;
	
	
	//Column names
	private static final String COL_TYPECODE_ID		= "FUND_TYPECODE_ID";
	private static final String COL_TYPECODE		= "FUND_TYPECODE";
	private static final String COL_TYPECODE_DESC	= "TYPECODE_DESC";
	
	
	/* **************** Properties ******************* */
	private int fundTypeCodeId = FUND_TYPECODE_ID_UNDEFINED; //Default Value 
	private String fundTypeCode = null;
	private String typeCodeDescription = null;
	
	/* **************** Constructor ****************** */
	/**
	 * Constructor for FundTypeCode
	 * @param fundTypeCodeID
	 * @param fundTypeCode
	 * @param typeCodeDescription
	 */
	public FundTypeCode(int fundTypeCodeId, String fundTypeCode,
			String typeCodeDescription) {
		super();
		this.fundTypeCodeId = fundTypeCodeId;
		this.fundTypeCode = fundTypeCode;
		this.typeCodeDescription = typeCodeDescription;
	}
	
	/* **************** Methods ********************* */
	/**
	 * Getting for fundTypeCodeId
	 * @return int (fundTypeCodeId)
	 */
	public int getFundTypeCodeId() {
		return fundTypeCodeId;
	}
	
	/**
	 * Setter for fundTypeCodeId
	 * @param fundTypeCodeId
	 */
	public void setFundTypeCodeId(int fundTypeCodeId) {
		this.fundTypeCodeId = fundTypeCodeId;
	}
	
	/**
	 * Getter for FundTypeCode
	 * @return String (fundTypeCode)
	 */
	public String getFundTypeCode() {
		return fundTypeCode;
	}
	
	/**
	 * Setter for fundTypeCode
	 * @param fundTypeCode
	 */
	public void setFundTypeCode(String fundTypeCode) {
		this.fundTypeCode = fundTypeCode;
	}
	
	/**
	 * Getter for TypeCodeDescription
	 * @return String (typeCodeDescription)
	 */
	public String getTypeCodeDescription() {
		return typeCodeDescription;
	}
	
	/**
	 * Setter for typeCodeDescription
	 * @param typeCodeDescription
	 */
	public void setTypeCodeDescription(String typeCodeDescription) {
		this.typeCodeDescription = typeCodeDescription;
	}

	
	/**
	 * Static Getter for Object based on resultSet
	 * @param rsFundTypeCode (DataResultSet)
	 * @return FundTypeCode 
	 */
	public static FundTypeCode get(DataResultSet rsFundTypeCode) throws DataException {
		
		FundTypeCode obj = null;
		
		if (rsFundTypeCode==null || rsFundTypeCode.isEmpty())
			return obj;
		
		obj = new FundTypeCode( 
			CCLAUtils.getResultSetIntegerValue(rsFundTypeCode, COL_TYPECODE_ID),
			rsFundTypeCode.getStringValueByName(COL_TYPECODE),
			rsFundTypeCode.getStringValueByName(COL_TYPECODE_DESC)
		);
		
		return obj;
	}
		
	/**
	 * Outputs all the properties to a string
	 * @return String
	 */
	public String toString() {
		String s = new String();
		s += "[[FundTypeCode " + fundTypeCodeId + "]: fundTypeCode =" +fundTypeCode+ 
		", typeCodeDescription=" +typeCodeDescription +"]";
		
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
		if (this.getFundTypeCodeId()==FUND_TYPECODE_ID_UNDEFINED)
			throw new DataException("fundTypeCodeID is not set");
		
		if (StringUtils.stringIsBlank(this.getFundTypeCode())) {
			throw new DataException("fundTypeCode is blank");
		}
	}
	
}
