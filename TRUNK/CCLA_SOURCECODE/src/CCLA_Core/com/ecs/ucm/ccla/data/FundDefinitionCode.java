package com.ecs.ucm.ccla.data;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

public class FundDefinitionCode implements Persistable {
	
	/* **************** Constants ******************** */
	//DefCode_ID
	//Should match the database
	public static final int FUND_DEFCODE_ID_UNDEFINED 		= 0;
	public static final int FUND_DEFCODE_ID_DEPOSIT 		= 1;
	public static final int FUND_DEFCODE_ID_INVESTMENT 		= 2;
	public static final int FUND_DEFCODE_ID_FIXED_INTEREST 	= 3;
	public static final int FUND_DEFCODE_ID_GLOBAL_EQUITY 	= 4;
	public static final int FUND_DEFCODE_ID_PROPERTY 		= 5;
	public static final int FUND_DEFCODE_ID_UK_EQUITY 		= 6;
	public static final int FUND_DEFCODE_ID_ETHICAL 		= 7;
	public static final int FUND_DEFCODE_ID_PSDF 			= 8;
	
	//Column names
	private static final String COL_DEFCODE_ID		= "FUND_DEFCODE_ID";
	private static final String COL_DEFCODE			= "FUND_DEFCODE";
	private static final String COL_DEFCODE_DESC	= "DEFCODE_DESC";
	
	
	/* **************** Properties ******************* */
	private int fundDefCodeId = FUND_DEFCODE_ID_UNDEFINED; //Default Value 
	private String fundDefCode = null;
	private String defCodeDescription = null;
	
	/* **************** Constructor ****************** */
	/**
	 * Constructor for fundDefCode
	 * @param fundDefCodeID
	 * @param fundDefCode
	 * @param DefCodeDescription
	 */
	public FundDefinitionCode(int fundDefCodeId, String fundDefCode,
			String defCodeDescription) {
		super();
		this.fundDefCodeId = fundDefCodeId;
		this.fundDefCode = fundDefCode;
		this.defCodeDescription = defCodeDescription;
	}
	
	/* **************** Methods ********************* */
	/**
	 * Getting for fundDefCodeId
	 * @return int (fundDefCodeId)
	 */
	public int getFundDefCodeId() {
		return fundDefCodeId;
	}
	
	/**
	 * Setter for fundDefCodeId
	 * @param fundDefCodeId
	 */
	public void setFundDefCodeId(int fundDefCodeId) {
		this.fundDefCodeId = fundDefCodeId;
	}
	
	/**
	 * Getter for fundDefCode
	 * @return String (fundDefCode)
	 */
	public String getFundDefCode() {
		return fundDefCode;
	}
	
	/**
	 * Setter for fundDefCode
	 * @param fundDefCode
	 */
	public void setFundDefCode(String fundDefCode) {
		this.fundDefCode = fundDefCode;
	}
	
	/**
	 * Getter for DefCodeDescription
	 * @return String (DefCodeDescription)
	 */
	public String getDefCodeDescription() {
		return defCodeDescription;
	}
	
	/**
	 * Setter for DefCodeDescription
	 * @param DefCodeDescription
	 */
	public void setDefCodeDescription(String defCodeDescription) {
		this.defCodeDescription = defCodeDescription;
	}

	
	/**
	 * Static Getter for Object based on resultSet
	 * @param rsfundDefCode (DataResultSet)
	 * @return FundDefinitonCode 
	 */
	public static FundDefinitionCode get(DataResultSet rsfundDefCode) throws DataException {
		
		FundDefinitionCode obj = null;
		
		if (rsfundDefCode==null || rsfundDefCode.isEmpty())
			return obj;
		
		obj = new FundDefinitionCode( 
			CCLAUtils.getResultSetIntegerValue(rsfundDefCode, COL_DEFCODE_ID),
			rsfundDefCode.getStringValueByName(COL_DEFCODE),
			rsfundDefCode.getStringValueByName(COL_DEFCODE_DESC)
		);
		
		return obj;
	}
		
	/**
	 * Outputs all the properties to a string
	 * @return String
	 */
	public String toString() {
		String s = new String();
		s += "[[fundDefCodeId " + fundDefCodeId + "]: fundDefCode =" +fundDefCode+ 
		", defCodeDescription=" +defCodeDescription +"]";
		
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
		if (this.getFundDefCodeId()==FUND_DEFCODE_ID_UNDEFINED)
			throw new DataException("fundDefCodeID is not set");
		
		if (StringUtils.stringIsBlank(this.getFundDefCode())) {
			throw new DataException("fundDefCode is blank");
		}
	}
	
}
