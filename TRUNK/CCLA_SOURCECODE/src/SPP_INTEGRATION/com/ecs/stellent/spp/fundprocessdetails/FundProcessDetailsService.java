package com.ecs.stellent.spp.fundprocessdetails;

import java.util.Date;
import java.util.StringTokenizer;


import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals;
import com.ecs.ucm.ccla.data.Company;
import com.ecs.ucm.ccla.data.FundTypeCode;
import com.ecs.ucm.ccla.utils.DateUtils;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.DateFormatter;
import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.server.Service;

public class FundProcessDetailsService extends Service {
	
	private static final int USER_ENTERED_PROCESS_DATE = 0;
	private static final int SYSTEM_CALCULATED_PROCESS_DATE = 1;
	
	
	/** 
	 * Forces a refresh of the process date cache.
	 * 
	 */
	public void refreshCache() throws DataException {
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace,true);
		
		FundProcessDetailsManager.lastUpdated = null;
		FundProcessDetailsManager.fpdLastUpdated = null;
		FundProcessDetailsManager.checkProcessDetailsCache(facade);
	}

	
	public void getProcessDateFromJobId() throws DataException, ServiceException {
		FWFacade facade 	= CCLAUtils.getFacade(m_workspace,true);
		FWFacade ucmFacade 	= CCLAUtils.getFacade(m_workspace);
		
		
		String fundCode		= m_binder.getLocal("fundCode");
		String destFundCode		= m_binder.getLocal("destFundCode");
		String ucmJobId 	= m_binder.getLocal("ucmJobId");
		String documentClass 	= m_binder.getLocal("documentClass");
		
		if (StringUtils.stringIsBlank(ucmJobId)) {
			throw new DataException("ucmJobId is blank.");
		}

		if (StringUtils.stringIsBlank(documentClass)) {
			throw new DataException("documentClass is blank.");
		}
		
		
		DataBinder binder = new DataBinder();
		binder.putLocal("jobId", ucmJobId);
		DataResultSet rsResult = ucmFacade.createResultSet("qGetDocumentStatusForJobId", binder);
		
		if (rsResult==null || rsResult.isEmpty()) {
			throw new DataException("cannot find job with xJobId:"+ucmJobId);
		} 		
		
		boolean found=false;
		Date releaseDate = null;
		String source = null;
		
		Log.debug(documentClass+ " : "+rsResult.getStringValueByName("xDocumentClass"));
		do {
			if (documentClass.equalsIgnoreCase(rsResult.getStringValueByName("xDocumentClass"))) 
			{								
				releaseDate = rsResult.getDateValueByName("dInDate");
				source = rsResult.getStringValueByName("xSource");
				found=true;
			}			
		} while (rsResult.next() && !found);
		
		if (found) {
			Date dealingDate = FundProcessDetailsManager.getDealingDate(fundCode, destFundCode, documentClass, null, false, source, releaseDate, false, facade);
			if (dealingDate!=null) {
				m_binder.putLocal("processDate", DateUtils.formatddsMMsyyyyspHHcmm(dealingDate));
				m_binder.putLocalDate("nextDealingDate", dealingDate);
			} else 
				throw new DataException("Cannot calculate process date");			
				
		} else {
			throw new DataException("no matching xJobId:"+ucmJobId);			
		}
	}
	
	/** 
	 * Service-accessible method for fetching the next dealing date for a
	 * given Fund. Uses the current system date/time when comparing against 
	 * the roll-over time.
	 *  
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void getNextDealingDate() throws DataException, ServiceException 
	{
		FWFacade facade 	= CCLAUtils.getFacade(m_workspace,true);
		FundProcessDetails.prepareBinderWithNextDealingDate(m_binder, facade);

	}

	
	
	
	/**
	 * 
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void getMismatchProcessDatesForBundle() throws DataException, ServiceException 
	{
		String xBundleRef = m_binder.getLocal(Globals.UCMFieldNames.BundleRef);
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace);
		
		DataBinder binder = new DataBinder();
		binder.putLocal(Globals.UCMFieldNames.BundleRef, xBundleRef);
		
		DataResultSet ProcessDateMismatch = 
			facade.createResultSet("QGetDocumentProcessDateMismatch", binder); 
		
		if (ProcessDateMismatch!=null && !ProcessDateMismatch.isEmpty())
			m_binder.addResultSet("rsProcessDateMismatch", ProcessDateMismatch);
		
		DataResultSet ProcessDatesForResetBundles = 
			facade.createResultSet("QGetDocumentProcessDateForResetBundle", binder); 
		
		if (ProcessDatesForResetBundles!=null && !ProcessDatesForResetBundles.isEmpty())
			m_binder.addResultSet("rsProcessDatesForResetBundles", ProcessDatesForResetBundles);
		
	}

	
	
	public void updateProcessDatesForBundle() throws DataException, ServiceException 
	{
		String docNameListStr = m_binder.getLocal("docNameList");
		
		if (!StringUtils.stringIsBlank(docNameListStr)) 
		{
			StringTokenizer st = new StringTokenizer(docNameListStr,",");
			
			String docName = null;
			LWDocument lwDoc = null;
			String dateValue = null;
			
			while (st.hasMoreTokens()) 
			{
				docName = st.nextToken();	
				
				if (!StringUtils.stringIsBlank(docName)) 
				{
					Integer choice = CCLAUtils.getBinderIntegerValue(m_binder,docName+"_Outcome");
					
					if (choice!=null) 
					{
						switch (choice) {
						
							case USER_ENTERED_PROCESS_DATE:
								//no need to do anything.
								break;					
							case SYSTEM_CALCULATED_PROCESS_DATE:
								try 
								{
									lwDoc = new LWDocument(docName, true);
									dateValue = lwDoc.getAttribute(Globals.UCMFieldNames.OriginalProcessDate);
							
									if (!StringUtils.stringIsBlank(dateValue)) {
										lwDoc.setAttribute(Globals.UCMFieldNames.ProcessDate, dateValue);
										lwDoc.update();
									} else {
										Log.error("Cannot set system calculated date, originalProcessDate is empty");
										throw new ServiceException("Cannot set system calculated date, originalProcessDate is empty");
									}
								} catch (Exception e) {
									Log.error("Cannot update process date "+e.getMessage());
									throw new ServiceException(e.getMessage());
								}
								break;	
							default:
								Log.error("Cannot find the choice for docName :"+docName);
								throw new ServiceException("Cannot find the choice for docName :"+docName);								
						}
					} else {
						Log.error("Cannot find the choice for docName :"+docName);
						throw new ServiceException("Cannot find the choice for docName :"+docName);
					}
				} else {
					Log.info("docName is blank, skipping");
				}
			} 
		}
	}	
	
	/******************************************************************************************/
	/******************************************************************************************/
	/******************************************************************************************/

	/** 
	 * Service method used for updating all Process Details Rollover Times.
	 * 
	 * @throws DataException 
	 * @throws ServiceException 
	 * */
	public void updateAllProcessDetailsRolloverTimes() throws DataException, ServiceException 
	{
		// Updated date/time value
		String setting = m_binder.getLocal("setting");
		
		if (StringUtils.stringIsBlank(setting)) {
			throw new ServiceException("Unable to update process details time: rolloverTime value is missing");
		}

		if (DateUtils.parseHHcmm(setting)==null) {
			throw new ServiceException("Unable to update process details time: rolloverTime format is invalid");			
		}
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace,true);
		
		DataBinder binder = new DataBinder();
		binder.putLocal("rolloverTime", setting);
		facade.execute("qUpdateAllProcessDetailsRolloverTimes", binder);
		
		FundProcessDetailsManager.refreshFundProcessDetailsCache(facade);
	}		

	
	/** 
	 * Service method used for updating all Process Details override Times.
	 * 
	 * @throws DataException 
	 * @throws ServiceException 
	 * */
	public void updateAllProcessDetailsOverrideDate() throws DataException, ServiceException 
	{
		// Updated date/time value
		String setting = m_binder.getLocal("setting");
		
		if (StringUtils.stringIsBlank(setting)) {
			throw new ServiceException("Unable to update process details time: override date value is missing");
		}

		if (DateUtils.parseddsMMsyyyyspHHcmm(setting)==null) {
			throw new ServiceException("Unable to update process details time: override date format is invalid");			
		}
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace,true);
		
		DataBinder binder = new DataBinder();
		binder.putLocalDate("overrideDate", DateUtils.parseddsMMsyyyyspHHcmm(setting));
		facade.execute("qUpdateAllProcessDetailsOverrideDate", binder);
		
		FundProcessDetailsManager.refreshFundProcessDetailsCache(facade);
	}			
	
	
	/** 
	 * Service method used for updating all Process Details override Times.
	 * 
	 * @throws DataException 
	 * @throws ServiceException 
	 * */
	public void clearAllProcessDetailsOverrideDate() throws DataException, ServiceException 
	{
		FWFacade facade = CCLAUtils.getFacade(m_workspace,true);
		
		DataBinder binder = new DataBinder();
		facade.execute("qClearAllProcessDetailsOverrideDate", binder);
		
		FundProcessDetailsManager.refreshFundProcessDetailsCache(facade);
	}			
	
	/** 
	 * Service method used for updating roll-over times
	 * for a particular fund code.
	 * 
	 * @throws DataException 
	 * @throws ServiceException 
	 * */
	public void updateProcessDetailsRolloverTimeByFundCode() throws DataException, ServiceException 
	{
		// Updated date/time value
		String rolloverTime = m_binder.getLocal("rolloverTime");
		String fundCode = m_binder.getLocal("fundCode");
		
		if (StringUtils.stringIsBlank(rolloverTime)) {
			throw new ServiceException("Unable to update process details rollover time: rolloverTime value is missing");
		}

		if (DateUtils.parseHHcmm(rolloverTime)==null) {
			throw new ServiceException("Unable to update process details rollover time: rollover time format is invalid");			
		}
	
		if (StringUtils.stringIsBlank(fundCode)) {
			throw new ServiceException("Unable to update process details rollover time: fundCode is missing");
		}
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace,true);
		
		DataBinder binder = new DataBinder();
		binder.putLocal("rolloverTime", rolloverTime);
		binder.putLocal("fundCode", fundCode);
		
		facade.execute("qProcessDetailsRolloverTimeByFundCode", binder);
		
		// Refresh the cache after changing any date.
		FundProcessDetailsManager.refreshProcessDetailsCache(facade);
	}		
	

	/** 
	 * Service method used for updating rollover times
	 * for a particular fund code.
	 * 
	 * @throws DataException 
	 * @throws ServiceException 
	 * 
	 * */
	public void updateProcessDetailsRolloverTimeByFundType() throws DataException, ServiceException 
	{
		// Updated date/time value
		String rolloverTime = m_binder.getLocal("setting");
		String name = m_binder.getLocal("name");
		
		if (StringUtils.stringIsBlank(rolloverTime)) {
			throw new ServiceException("Unable to update process details rollover time: rolloverTime value is missing");
		}

		if (DateUtils.parseHHcmm(rolloverTime)==null) {
			throw new ServiceException("Unable to update process details rollover time: rollover time format is invalid");			
		}
	
		if (StringUtils.stringIsBlank(name)) {
			throw new ServiceException("Unable to update process details rollover time: name is missing");
		}
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace,true);
		
		String fundType = "0";
		if (name.equals(FundProcessDetailsManager.DEPOSIT_TIME_NAME))
			fundType = String.valueOf(FundTypeCode.FUND_TYPECODE_ID_DEPOSIT);
		else if (name.equals(FundProcessDetailsManager.PSDF_TIME_NAME))
			fundType =  String.valueOf(FundTypeCode.FUND_TYPECODE_ID_PSDF);
		else if (name.equals(FundProcessDetailsManager.UNITISED_TIME_NAME))
			fundType =  String.valueOf(FundTypeCode.FUND_TYPECODE_ID_UNITIZED);
		else 
			fundType =  String.valueOf(FundTypeCode.FUND_TYPECODE_ID_UNDEFINED);
			
		
		DataBinder binder = new DataBinder();
		binder.putLocal("rolloverTime", rolloverTime);
		binder.putLocal("fundType", fundType);
		
		facade.execute("qUpdateProcessDetailsRolloverTimeByFundType", binder);
		
		FundProcessDetailsManager.refreshFundProcessDetailsCache(facade);
	}		

	/** 
	 * Service method used for updating rollover times
	 * for a particular fund code.
	 * 
	 * @throws DataException 
	 * @throws ServiceException 
	 * 
	 * */
	public void clearProcessDetailsRolloverTimeByFundType() throws DataException, ServiceException 
	{
		// Updated date/time value
		String fundType = m_binder.getLocal("fundType");
	
		if (StringUtils.stringIsBlank(fundType)) {
			throw new ServiceException("Unable to update process details rollover time: fundType is missing");
		}
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace,true);
		
		DataBinder binder = new DataBinder();
		binder.putLocal("fundCode", fundType);
		
		facade.execute("qClearProcessDetailsRolloverTimeByFundCode", binder);
		
		FundProcessDetailsManager.refreshFundProcessDetailsCache(facade);
		

	}		
	
	
	public @Deprecated void clearSystemConfigSetting() throws DataException, ServiceException {
		// Process Date name
		String name = m_binder.getLocal("name");
		
		if (StringUtils.stringIsBlank(name)) {
			throw new ServiceException("Unable to clear system config setting, name is empty");
		}
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace,true);
		
		DataBinder binder = new DataBinder();
		binder.putLocal("name", name);
			
		facade.execute("qClearSystemConfigSetting", binder);
			
		Log.debug("cleared system config setting for '" + name + "'");
		// Refresh the cache after changing any date.
		//cascade down
		if (name.equals(FundProcessDetailsManager.OVERRIDE_DATE_NAME)) {
			clearAllProcessDetailsOverrideDate();
		}
		
		FundProcessDetailsManager.refreshProcessDetailsCache(facade);
		 

	}

	public void clearAllProcessDetailsRolloverTimes() throws DataException, ServiceException {
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace,true);
		
		DataBinder binder = new DataBinder();
			
		facade.execute("qClearAllProcessDetailsRolloverTimes", binder);
			
		Log.debug("cleared all fund process details rolloverTime");
		// Refresh the cache after changing any date.
		FundProcessDetailsManager.refreshFundProcessDetailsCache(facade);	
	}	
	
	/**
	 * @throws DataException
	 * @throws ServiceException
	 */
	public @Deprecated void updateSystemConfigSetting() throws DataException, ServiceException {
		// Process Date name
		String name = m_binder.getLocal("name");
		String setting = m_binder.getLocal("setting");
		
		Log.debug("UpdateSystemConfigSetting name="+name+", setting="+setting);
		
		if (StringUtils.stringIsBlank(name)) {
			throw new ServiceException("Unable to update system config, name is empty");		
		}
		
		if (StringUtils.stringIsBlank(setting)) {
			throw new ServiceException("Unable to update system config setting is empty");		
		}
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace,true);
		
		DataBinder binder = new DataBinder();
		binder.putLocal("name", name);
		binder.putLocal("setting", setting);
		
		facade.execute("qUpdateSystemConfigSetting", binder);
			
		Log.debug("update system config setting for '" + name + "'");
		

		//Cascade downwards.
		if (name.equals(FundProcessDetailsManager.DEPOSIT_TIME_NAME) ||
				name.equals(FundProcessDetailsManager.UNITISED_TIME_NAME) ||
				name.equals(FundProcessDetailsManager.PSDF_TIME_NAME)) {
			updateProcessDetailsRolloverTimeByFundType();
		} else if (name.equals(FundProcessDetailsManager.OVERRIDE_DATE_NAME)) {
			updateAllProcessDetailsOverrideDate();
		}
		
		// Refresh the cache after changing any date.
		FundProcessDetailsManager.refreshProcessDetailsCache(facade);
		
	}
	
	/** Service-accessible method for fetching the details of the Process date
	 *  cache and adding them to the DataBinder. Used on the Process Date
	 *  Administration screen.
	 * @throws DataException 
	 * @throws ServiceException 
	 *  
	 */
	public void getProcessDetailsCacheDetails() throws DataException, ServiceException 
	{
		FWFacade facade = CCLAUtils.getFacade(m_workspace,true);
		
		FundProcessDetailsManager.checkProcessDetailsCache(facade);

		if (FundProcessDetailsManager.isCacheInitialized()) {
/*
			m_binder.putLocal("PreRolloverDate", 
                DateFormatter.getTimeStamp(FundProcessDetailsManager.preRolloverDate));
			
			m_binder.putLocal("PostRolloverDate",
				DateFormatter.getTimeStamp(FundProcessDetailsManager.postRolloverDate)); 
*/			
			m_binder.putLocal("CacheLastUpdated",
				DateFormatter.getTimeStamp(FundProcessDetailsManager.lastUpdated));
			
			m_binder.putLocal(FundProcessDetailsManager.ROLLOVER_TIME_NAME, 
				DateFormatter.getTimeStamp(FundProcessDetailsManager.rolloverTime));
			
			m_binder.putLocal(FundProcessDetailsManager.PSDF_TIME_NAME,
				DateFormatter.getTimeStamp(FundProcessDetailsManager.psdfTime));
			
			m_binder.putLocal(FundProcessDetailsManager.DEPOSIT_TIME_NAME, 
				DateFormatter.getTimeStamp(FundProcessDetailsManager.depositTime));

			m_binder.putLocal(FundProcessDetailsManager.UNITISED_TIME_NAME, 
				DateFormatter.getTimeStamp(FundProcessDetailsManager.unitisedTime));
			
			// Compute the base process date, relative to the current time.
			Date baseProcessDate = 
				FundProcessDetailsManager.getNextDealingDate(null, new Date(), false, false, null, false, facade);
			m_binder.putLocal("baseProcessDate", DateFormatter.getTimeStamp(baseProcessDate));			
		
			//compute the base process date for each of the fund types, relative to current time.
			boolean foundDeposit = false;

			boolean foundPsdf = false;
			boolean foundUnitised = false;
			boolean foundUnitisedCBF = false;
			boolean foundUnitisedCOIF = false;
			
			// Fetch entire CCLA_FUNDS table from db
			DataResultSet rsFunds = facade.createResultSet("qClientServices_GetFunds", new DataBinder());
			
			if(rsFunds!=null && !rsFunds.isEmpty()) {
				
				int fundTypeCodeID = FundTypeCode.FUND_TYPECODE_ID_UNDEFINED;
				int companyID = Company.UNKNOWN;
				
				String fundCode = null;
				do {
					
					try {
						fundTypeCodeID = Integer.parseInt(rsFunds.getStringValueByName("FUND_TYPECODE_ID"));
					} catch (NumberFormatException nfe) {
						Log.error("Cannot parse fundcodeTypeID "+nfe.getMessage());
						fundTypeCodeID = FundTypeCode.FUND_TYPECODE_ID_UNDEFINED;
					}

					try {
						companyID = Integer.parseInt(rsFunds.getStringValueByName("COMPANY_ID"));
					} catch (NumberFormatException nfe) {
						Log.error("Cannot parse fundcodeTypeID "+nfe.getMessage());
						companyID = Company.UNKNOWN;
					}
					
					fundCode = rsFunds.getStringValueByName("FUND_CODE");

					switch(fundTypeCodeID) {
						case FundTypeCode.FUND_TYPECODE_ID_DEPOSIT:
							if (!foundDeposit) {
								Date baseDepositDate = 
									FundProcessDetailsManager.getNextDealingDate(fundCode, new Date(), false, false, null, false, facade);
								
								if (baseDepositDate!=null) {
									m_binder.putLocal("baseDepositDate", DateFormatter.getTimeStamp(baseDepositDate));			
									foundDeposit = true;
								}
							}
							break;
						case FundTypeCode.FUND_TYPECODE_ID_PSDF:
							if (!foundPsdf) {
								Date basePsdfDate = 
									FundProcessDetailsManager.getNextDealingDate(fundCode, new Date(), false, false, null, false, facade);
								
								if (basePsdfDate!=null) {
									m_binder.putLocal("basePsdfDate", DateFormatter.getTimeStamp(basePsdfDate));			
									foundPsdf = true;
								}
							}
							break;
						case FundTypeCode.FUND_TYPECODE_ID_UNITIZED:
							if (companyID==Company.COIF && !foundUnitisedCOIF) {
								Date baseUnitisedCoifDate = 
									FundProcessDetailsManager.getNextDealingDate(fundCode, new Date(), false, false, null, false, facade);
								
								if (baseUnitisedCoifDate!=null) {
									m_binder.putLocal("baseUnitisedDateCoif", DateFormatter.getTimeStamp(baseUnitisedCoifDate));			
									foundUnitisedCOIF = true;
								}
							} else if (companyID==Company.CBF && !foundUnitisedCBF) {
								Date baseUnitisedCbfDate = 
									FundProcessDetailsManager.getNextDealingDate(fundCode, new Date(), false, false, null, false, facade);
								
								if (baseUnitisedCbfDate!=null) {
									m_binder.putLocal("baseUnitisedDateCbf", DateFormatter.getTimeStamp(baseUnitisedCbfDate));			
									foundUnitisedCBF = true;
								}
							} else {
								if (!foundUnitised) {
									Date baseUnitisedDate = 
										FundProcessDetailsManager.getNextDealingDate(fundCode, new Date(), false, false, null, false, facade);
									
									if (baseUnitisedDate!=null) {
										m_binder.putLocal("baseUnitisedDate", DateFormatter.getTimeStamp(baseUnitisedDate));			
										foundUnitised = true;
									}
								}
							}
							break;
						default:
							break;							
					}
				} while (rsFunds.next());
			}	
		} else {
			m_binder.putLocal("CacheNotInitialized", "1");
			m_binder.putLocal("baseProcessDate", DateFormatter.getTimeStamp());
		}	
	}
}
