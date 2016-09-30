package com.ecs.ucm.ccla.data;

import java.util.Date;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

public class FundProcessDetails implements Persistable{

	/******************* Constants ****************************/
	public static final String PROCESS_DETAILS_COL	= "PROCESS_DETAILS";
	public static final String FUND_CODE_COL		= "FUND_CODE";
	public static final String ROLLOVER_TIME_COL	= "ROLLOVER_TIME";
	public static final String OVERRIDE_DATE_COL	= "OVERRIDE_DATE";
	public static final String DESCRIPTION_COL 		= "PROCESS_DETAILS_DESCRIPTION";
	
	/******************* Properties ***************************/
	private int id				   	= 0;
	private String rolloverTimeStr 	= null;
	private Date overrideDate		= null;
	private String description		= null;
	private String fundCode			= null;
	
	
	/***************************************************************/
	/********************* Constructors ****************************/ 
	/***************************************************************/		
	public FundProcessDetails() {
	}
	
	public FundProcessDetails(int id, String rolloverTimeStr, Date overrideDate, 
			String description, String fundCode) 
	{
		this.id = id;
		this.rolloverTimeStr = rolloverTimeStr;
		this.overrideDate = overrideDate;
		this.description = description;
		this.fundCode = fundCode;
	}
	
	/***************************************************************/
	/************************** Methods ****************************/ 
	/***************************************************************/		
	public int getId() {
		return id;
	}
	
	public String getFundCode() { return fundCode; }
	public void setFundCode(String fundCode) { this.fundCode = fundCode; }
	
	public String getRolloverTimeStr() { return rolloverTimeStr; }
	public void setRolloverTimeStr(String rolloverTimeStr) throws DataException {
		this.rolloverTimeStr = rolloverTimeStr;
	}
	
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	
	public void setOverrideDate(Date overrideDate) throws DataException {
		this.overrideDate = overrideDate;
	}
	public Date getOverrideDate() { return overrideDate; }
	
	
	public static DataResultSet getData(String fundCode, FWFacade facade) 
	throws DataException {
		DataBinder binder = new DataBinder();
		binder.putLocal("fundCode", fundCode);
		
		return facade.createResultSet
		 ("qClientServices_GetAllProcessDetailsByFundCode", binder);	
	}
	
	
	public static FundProcessDetails get(DataResultSet rs) 
	throws DataException {
		
		if (rs==null || rs.isEmpty())
			return null;
		
		return new FundProcessDetails(
				CCLAUtils.getResultSetIntegerValue(rs, PROCESS_DETAILS_COL),
				rs.getStringValueByName(ROLLOVER_TIME_COL),
				rs.getDateValueByName(OVERRIDE_DATE_COL),
				rs.getStringValueByName(DESCRIPTION_COL),
				rs.getStringValueByName(FUND_CODE_COL)
				);
	}
	
	public static FundProcessDetails get(String fundCode, FWFacade facade) 
	throws DataException {
		
		DataResultSet dataResultSet = getData(fundCode, facade);
		return get(dataResultSet);
	}
	
	/***************************************************************/
	/****************** Persistable Interface **********************/ 
	/***************************************************************/	
	public void setAttributes(DataBinder binder) throws DataException {
		//Does nothing		
	}

	public void addFieldsToBinder(DataBinder binder) throws DataException {
		// TODO Auto-generated method stub
		
	}

	public void persist(FWFacade facade, String username) throws DataException {
		
	}

	public void validate(FWFacade facade) throws DataException {
		
	}

}
