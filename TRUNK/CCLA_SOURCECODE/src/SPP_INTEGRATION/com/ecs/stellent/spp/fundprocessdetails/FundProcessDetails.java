package com.ecs.stellent.spp.fundprocessdetails;

import java.util.Date;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.utils.DateUtils;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

public class FundProcessDetails{

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
		
		int idToUse = 0;
		try {
			idToUse = Integer.parseInt(rs.getStringValueByName(PROCESS_DETAILS_COL));
		} catch (NumberFormatException nfe) {
			throw new DataException(nfe.getMessage());
		}
		
		return new FundProcessDetails(
				idToUse,
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
	
	
	
	/**
	 * Method that gets the next dealing date from the information on the binder.
	 * It then compares it with the userProcessDate and decides which processDateComparision
	 * to put on the binder. 0 before, 1 same, 2 after 
	 * 
	 * Required Binder Params
	 * 
	 *  - fundCode
	 *  - docClass
	 * 
	 * @param binder
	 * @param facade
	 * @throws DataException
	 * @throws ServiceException
	 */
	public static void prepareBinderWithNextDealingDate(DataBinder binder, FWFacade facade) throws DataException, ServiceException{
		
		
		String fundCode		= binder.getLocal("fundCode");
		String destFundCode = binder.getLocal("destFundCode");
		String source 		= binder.getLocal("source");
		String docClass     = binder.getLocal("docClass");
		Date userProcessDate = CCLAUtils.getBinderDateValue(binder, "userProcessDate");
		Date toCompareDate = DateUtils.parseddsMMsyyyyspHHcmm(binder.getLocal("dateToUse"));
		
		if (StringUtils.stringIsBlank(fundCode))
			throw new DataException("fundCode is blank.");
		
		boolean forceOriginal = new Boolean(binder.getLocal("forceOriginal")).booleanValue();
		boolean isAmendment = new Boolean(binder.getLocal("isAmendment")).booleanValue();
		
		if (toCompareDate==null) { toCompareDate = DateUtils.getNow(); }
		if (docClass==null) { docClass = "APP"; }
		
		Log.debug("fundCode="+fundCode
				+", destFundCode="+(destFundCode==null?"null":destFundCode)
				+", Source="+(source==null?"null":source)
				+", DocumentClass="+(docClass==null?"null":docClass)
				+", UserProcessDate="+(userProcessDate==null?"null":userProcessDate.toString())				
			);
		
		Date dealingDate = FundProcessDetailsManager.getDealingDate(fundCode, destFundCode, docClass, null, isAmendment, source, toCompareDate, forceOriginal, facade);
		
		//Additional information
		if (userProcessDate!=null) 
		{
			int val = DateUtils.compareDates(userProcessDate, dealingDate);
		
			if (val<0) {
				//User entered process date is before the calculated process date
				CCLAUtils.addQueryIntParamToBinder(binder, "processDateComparision", 0);
			} else if (val==0) {
				//User entered process date is the same as the calculated process date
				CCLAUtils.addQueryIntParamToBinder(binder, "processDateComparision", 1);				
			} else {
				//User entered process date is after the calculated process date
				CCLAUtils.addQueryIntParamToBinder(binder, "processDateComparision", 2);
			}
		}
		
		binder.putLocal("processDate", DateUtils.formatddsMMsyyyyspHHcmm(dealingDate));
		binder.putLocalDate("nextDealingDate", dealingDate);
		binder.putLocal("fundCode", fundCode);
		binder.putLocal("dateToUse", DateUtils.formatddsMMsyyyyspHHcmm(toCompareDate));
		BinderUtils.addBooleanParamToBinder(binder, "isAmendment", isAmendment);
		
	}
	
	/**
	 * This method will simply check for required params on a given binder
	 * If any are empty return false, otherwise return true
	 * @param binder
	 * @return
	 */
	public static boolean checkBinderReqParamsForDealingDate(DataBinder binder){
				
		if (StringUtils.stringIsBlank(binder.getLocal("fundCode")) || 
				StringUtils.stringIsBlank(binder.getLocal("docClass"))){
			Log.info("checkBinderReqParamsForDealingDate: returning false");
			return false;
		} else {
			Log.info("checkBinderReqParamsForDealingDate: returning true");
			return true;
		}
	}
}
