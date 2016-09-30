package com.ecs.ucm.ccla.data;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

public class FundStatus implements Persistable {
	
	/* **************** Constants ******************** */
	public static final int FUND_STATUS_OPEN 	= 1;
	public static final int FUND_STATUS_CLOSED 	= 2;
	public static final int FUND_STATUS_PENDING = 3;
	
	//Column names
	public final static String COL_FUND_STATUS_ID 	= "FUND_STATUS_ID";
	public final static String COL_FUND_STATUS_NAME = "FUNS_STATUS_NAME";
		
	/* **************** Properties ******************* */
	private int statusId = -1;
	private String statusName = null;
	
	/* **************** Constructor ****************** */
	/**
	 * Constructor for FundStatus
	 * @param statusId
	 * @param statusCode
	 */
	public FundStatus(int statusId, String statusName) {
		super();
		this.statusId = statusId;
		this.statusName = statusName;
	}	
	

	/* **************** Methods ********************* */

	/**
	 * Setter for statusId
	 * @param statusId
	 */
	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	/**
	 * Getter for statusID
	 * @return int (statusId)
	 */
	public int getStatusId() {
		return statusId;
	}

	/**
	 * Setter for statusName
	 * @param statusName
	 */
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	/**
	 * Getter for statusName 
	 * @return String(statusName)
	 */
	public String getStatusName() {
		return statusName;
	}
	
	/**
	 * Static Getter for Object based on resultSet
	 * @param rsFundStatus (DataResultSet)
	 * @return FundStatus
	 */
	public static FundStatus get(DataResultSet rsFundStatus) throws DataException {
		
		FundStatus obj = null;
		
		if (rsFundStatus==null || rsFundStatus.isEmpty())
			return obj;
		
		obj = new FundStatus( 
			CCLAUtils.getResultSetIntegerValue(rsFundStatus, COL_FUND_STATUS_ID),
			rsFundStatus.getStringValueByName(COL_FUND_STATUS_NAME)
		);
		
		return obj;
	}
	
	/**
	 * Outputs all the properties to a string
	 * @return String
	 */
	public String toString() {
		String s = new String();
		s += "[[FundStatus " + statusId + "]: statusName =" +statusName+"]";
		
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
