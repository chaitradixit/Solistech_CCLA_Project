package com.ecs.stellent.spp.fundprocessdetails;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.shared.SharedObjects;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

import com.ecs.ucm.ccla.CacheManager;
import com.ecs.ucm.ccla.data.Company;
import com.ecs.ucm.ccla.data.Fund;
import com.ecs.ucm.ccla.data.FundTypeCode;
import com.ecs.ucm.ccla.data.SystemConfigVar;
import com.ecs.ucm.ccla.data.instruction.InstructionType;
import com.ecs.ucm.ccla.utils.DateUtils;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.DateFormatter;
import com.ecs.utils.stellent.embedded.FWFacade;

public class FundProcessDetailsManager {

	/* Core ProcessDetailsNames, should be a mirror of names used in the
	 * SYSTEM_CONFIG table AND FUND_TYPECODE.
	 */
	public static final String ROLLOVER_TIME_NAME		= "RolloverTime";
	public static final String UNITISED_TIME_NAME		= "UnitisedTime";
	public static final String PSDF_TIME_NAME			= "PSDFTime";
	public static final String DEPOSIT_TIME_NAME		= "DepositTime";
	
	public static final String ROLLOVER_AMENDMENT_TIME_NAME		= "RolloverAmendmentTime";
	public static final String UNITISED_AMENDMENT_TIME_NAME		= "UnitisedAmendmentTime";
	public static final String PSDF_AMENDMENT_TIME_NAME			= "PSDFAmendmentTime";
	public static final String DEPOSIT_AMENDMENT_TIME_NAME		= "DepositAmendmentTime";
	
	public static final String FAX_TIME_NAME 				= "FaxTime";
	public static final String EMAIL_TIME_NAME 				= "EmailTime";
	public static final String SCAN_FOR_IRIS_TIME_NAME 		= "ScanForIrisTime";
	public static final String MANUAL_UPLOAD_TIME_NAME 		= "ManualUploadTime";
	public static final String FORM_GENERATOR_TIME_NAME 	= "FormGeneratorTime";
	public static final String STP_TIME_NAME				= "STPTime";
	
	public static final String SOURCE_FAX 				= "FaxSource";
	public static final String SOURCE_EMAIL 			= "EmailSource";
	public static final String SOURCE_SCAN_IRIS 		= "ScanIrisSource";
	public static final String SOURCE_MANUAL_UPLOAD 	= "ManualUploadSource";
	public static final String SOURCE_FORM_GENERATOR 	= "FormGeneratorSource";
	public static final String SOURCE_STP 				= "StpSource";
	
	public static final String VALIDATION_CUT_OFF_TIME	= "ValidationCutOffTime";
	
	public static final String[] SOURCES = 
		{SOURCE_FAX, SOURCE_EMAIL, SOURCE_SCAN_IRIS, 
		SOURCE_MANUAL_UPLOAD, SOURCE_FORM_GENERATOR, SOURCE_STP};
	
	public static HashMap<String, String> sourceNameMapping = null;
		
	public static final String OVERRIDE_DATE_NAME	= "OverrideDate";
	
	public static final String PROCESS_DATES_GROUP_NAME = "PROCESS_DATES";
	public static final String PROCESS_DATES_CONFIG_GROUP_NAME = "PROCESS_DATES_CONFIG";
	
	
	//Create a mapping
	private static HashMap<String,String> destFundMapping = null;
	static {
		String mappingStr = SharedObjects.getEnvironmentValue("SPP_FORCE_DESTFUND_CODE_FOR_PROCESS_DATES");
		
		if (!StringUtils.stringIsBlank(mappingStr)) {
			destFundMapping = new HashMap<String,String>();
			
			StringTokenizer st = new StringTokenizer(mappingStr,",");
			while (st.hasMoreTokens()) {
				String[] str = st.nextToken().split(":");
				if (str.length==2) {
					destFundMapping.put(str[0], str[1]);
				}
			}
		}
	};
	
	private static HashMap<String, FundProcessDetails> fundProcessDetailsCache = null;
	
	/** Determines when the base process date/roll-over time was 
	 *  last updated. */
	public static Date lastUpdated = null; 
	public static Date fpdLastUpdated = null; 
	
	/** used to cache the roll-over time. */
	public static Date rolloverTime = null;
	/** used to cache the psdf time. */
	public static Date psdfTime = null;
	/** used to cache the unitised time. */
	public static Date unitisedTime = null;
	/** used to cache the deposit time. */
	public static Date depositTime = null;	
	/** used to cache the overrideDate */
	public static Date overrideDate = null;
	
	/** used to cache the roll-over amendment time. */
	public static Date rolloverAmendmentTime = null;
	/** used to cache the psdf amendment time. */
	public static Date psdfAmendmentTime = null;
	/** used to cache the unitised amentment time. */
	public static Date unitisedAmendmentTime = null;
	/** used to cache the deposit amendment time. */
	public static Date depositAmendmentTime = null;	
	
	//Validation Cut-Off Time
	public static Date validationCutOffTime = null;
	
	//Source Times
	public static Date faxTime = null;
	public static Date emailTime = null;
	public static Date scanForIrisTime = null;
	public static Date manualUploadTime = null;
	public static Date formGeneratorTime = null;
	public static Date stpTime = null;
	
	
	/** cache expiry time before forcing a reload, default is 5 mins **/
	public final static long EXPIRY_TIME_MILLIS = 
	 (long)SharedObjects.getEnvironmentInt("SPP_ConfigSettingCacheExpiry", 5) * 1000 * 60;
	
	
	/** Base processing date used for items that arrive before the roll-over
	 *  time has passed.
	 */
	public static Date preRolloverDate = null;
	
	/** Base processing date used for items that arrive after the roll-over
	 *  time has passed.
	 */
	public static Date postRolloverDate = null;
	
	
	
	public static boolean isCacheInitialized() {
		return (lastUpdated!=null && fpdLastUpdated!=null);
	}
	
	/** Check if the cache is not initialized, expired, or the rollover
	 * time has exceeded. If any condition is true, refresh the date 
	 * cache. 
	 *
	 **/
	public static synchronized void checkProcessDetailsCache(FWFacade facade) 
	 throws DataException {
		
		long currentTime = System.currentTimeMillis();
		
		//First refresh the main times
		if (lastUpdated==null ||
			((currentTime - lastUpdated.getTime()) > EXPIRY_TIME_MILLIS)) {			
			refreshProcessDetailsCache(facade);
		}
		
		//Next Refresh Fund Process Details 
		if (fpdLastUpdated==null || 
			((currentTime - fpdLastUpdated.getTime()) > EXPIRY_TIME_MILLIS)) {			
			refreshFundProcessDetailsCache(facade);
		}
	}

	/** Fetches the next dealing date for a given Fund. If the passed Fund
	 *  Code is null/empty, the base process date is passed instead.
	 *  
	 *  The passed release date is compared against the base processing
	 *  date
	 * 
	 * @param fundCode
	 * @param facade
	 * @return
	 * @throws ServiceException 
	 * @throws DataException 
	 */
	public static Date getNextDealingDate(String fundCode, Date releaseDate, 
			boolean isAmendment, boolean useValidationTime, 
			String source, boolean forceOriginalDate, FWFacade facade) 
	 throws ServiceException, DataException {
		
		checkProcessDetailsCache(facade);
		Date baseProcessDate = null;
			
		baseProcessDate = getProcessingTime(fundCode, releaseDate, isAmendment, useValidationTime, source, forceOriginalDate, facade);
		
		if (StringUtils.stringIsBlank(fundCode))
			return baseProcessDate;
		DataBinder binder	= new DataBinder();
		binder.putLocal("baseProcessDate", DateFormatter.getTimeStamp(baseProcessDate));
		binder.putLocal("fundCode", fundCode);
		
		// Execute a query which will return a single-row, single-column
		// ResultSet, containing the next dealing date for the given fund.
		DataResultSet rsNextDealingDate = 
		 facade.createResultSet("qGetNextFundDealingDate", binder);
		
		if (rsNextDealingDate.isEmpty()) {
			Log.warn("No next dealing date found for fund: " + fundCode);
			// TM: Updated return to baseProcessDate here (used to be null)
			return baseProcessDate;
		} else {
			return rsNextDealingDate.getDateValueByName("DEALING_DATE");
		}
	}
	
	/** Determines the dealing date for the passed Content Item. The ResultSet
	 *  is expected to contain a join of columns from the Revisions and DocMeta
	 *  tables.
	 *  
	 *  If the Content Item has a non-null xProcessDate value, the method 
	 *  returns null to indicate it has already been set, and shouldn't be
	 *  touched.
	 *  
	 *  Otherwise, the Process Date is calculated based on the item's xFund
	 *  value (if applicable) and its release date. If the document has a
	 *  non-transaction Document Class, the base processing date is returned
	 *  and the fund code won't be considered.
	 *  
	 * @param rsContentItem
	 * @param facade
	 * @return
	 * @throws ServiceException
	 * @throws DataException
	 */
	public static Date getDealingDate(DataResultSet rsContentItem, 
			boolean isAmendment, Date releaseDate, boolean forceOriginalDate, FWFacade facade) 
	throws ServiceException, DataException 
	{
		String processDateStr = null;
		
		if (!forceOriginalDate)
			processDateStr 	= rsContentItem.getStringValueByName("xProcessDate");
		
		String docClass	= rsContentItem.getStringValueByName("xDocumentClass");
		String fundCode	= rsContentItem.getStringValueByName("xFund");
		String destFundCode	= rsContentItem.getStringValueByName("xDestinationFund");
		String source = rsContentItem.getStringValueByName("xSource");
		
		if (releaseDate==null)
			 releaseDate = rsContentItem.getDateValueByName("dInDate");

		return FundProcessDetailsManager.getDealingDate(
				fundCode, destFundCode, docClass, processDateStr, 
				isAmendment, source, releaseDate, forceOriginalDate,facade);
	}
	
	
	public static Date getDealingDate(String fundCode, String destFundCode, 
			String docClass, String processDateStr, boolean isAmendment, 
			String source, Date releaseDate, boolean forceOriginalDate, FWFacade facade) 
	throws ServiceException, DataException 
	{
		Log.debug("-- fundCode:"+(fundCode==null?"null":fundCode)+
				", docClass:"+(docClass==null?"null":docClass)+
				", source:"+(source==null?"null":source)+
				", releaseDate:"+(releaseDate==null?"null":releaseDate.toString()));
		
		
		if (StringUtils.stringIsBlank(processDateStr)) 
		{
			if (releaseDate==null)
				releaseDate = DateUtils.getNow();
			
			boolean useValidationTime = false;
			
			if (!forceOriginalDate) 
			{
				Date today = DateUtils.getNow();
				
				if (DateUtils.compareDates(releaseDate, today)<0) {
					releaseDate = today;
					useValidationTime = true;
					Log.debug("Pasted in time has already elasped, rolling to the current time.");
				}
			}
			
			Date dealingDate = null;
			boolean submitToSpp = false;
			InstructionType instrType = InstructionType.getNameCache().getCachedInstance(docClass);
			
			// TODO: Why 'is submit to SPP?' maybe isFinancialTransaction() would be
			// better
			if (instrType!=null && instrType.isSubmitToSpp()) {
				submitToSpp = true;
			}
			
			if (!submitToSpp) {
				// Document Class is not a transaction type, just use the base
				// process date.
				dealingDate = getNextDealingDate(null, releaseDate, isAmendment, false, source, false, facade);
			} else {
				// Document Class is a transaction type, use the Fund value to
				// calculate the process date where applicable.
				
				//Special conditions!
				//1. If the xDocumentClass is BUYDF, then use dest fund code (xDestinationFund)
				//2. If the xDocumentClass is TRANSOUT, then use dest fund code (xDestinationFund) 
				//   if the the fund code is V,W,N,P,62 
				fundCode = getFundCodeToUse(fundCode, destFundCode, docClass);
				dealingDate = getNextDealingDate(fundCode, releaseDate, isAmendment, useValidationTime, source, forceOriginalDate, facade);
			}
			return dealingDate;
		} else
			return null; // xProcessDate already set on this item
	}
		
	
	/**
	 * Returns the date to compare against, i.e. either current time or the override time if set
	 * @param fundCode
	 * @param releaseDate
	 * @return
	 */
	private static Date getComparableDate(String fundCode, Date releaseDate) {
		
		if (!StringUtils.stringIsBlank(fundCode)) {
			FundProcessDetails fpd = getFundProcessDetails(fundCode);
			if (fpd!=null && fpd.getOverrideDate()!=null) {
				releaseDate = fpd.getOverrideDate();
			} else {
				if (overrideDate!=null) {
					releaseDate = overrideDate;
				}	
			}
		} else {
			if (overrideDate!=null) {
				releaseDate = overrideDate;
			}
		}
		
		Log.debug((fundCode==null?"":fundCode)+", using comparableDate="+releaseDate.toString());
		return releaseDate;
	}
	
	/**
	 * Gets the processing time to determine whether to use the previous date or not.
	 * @param fundCode
	 * @param releaseDate
	 * @return
	 */
	public static Date getProcessingTime(String fundCode, Date releaseDate, boolean isAmendment, boolean useValidationTime, String source, boolean forceOriginalDate, FWFacade facade) throws DataException 
	{
		//ignore override time, just use the current time etc..
		if (!forceOriginalDate)
			releaseDate = getComparableDate(fundCode, releaseDate);

		Log.debug("Comparable Date is "+releaseDate.toString());
		
		Date toCompareAgainst = null;
		int typeCodeID = FundTypeCode.FUND_TYPECODE_ID_UNDEFINED;
		int companyID = Company.UNKNOWN;
		
		if (!StringUtils.stringIsBlank(fundCode)) {
			//First try to individual times if set.
			
			if (fundProcessDetailsCache!=null && !fundProcessDetailsCache.isEmpty() && !isAmendment) {
				FundProcessDetails fpd = (FundProcessDetails)fundProcessDetailsCache.get(fundCode);
				if (fpd!=null) {					
					try {
						toCompareAgainst = DateUtils.parseHHcmm(fpd.getRolloverTimeStr());
					} catch(Exception e) {
						Log.error("FundProcessDetails, error parsing time str for fund "+fundCode+", "+e.getMessage());
						//carry on to get the base times instead.
						toCompareAgainst=null;
					}
				} else {
					Log.warn("FundProcessDetails not found for code "+fundCode+", will attempt to use fund specific times");
				}
			} 
			
			if (toCompareAgainst==null) {
				Fund f = null;
				if ((f=Fund.getCache().getCachedInstance(fundCode))!=null) {			
					typeCodeID = f.getTypeCode().getFundTypeCodeId();
					companyID = f.getCompany().getCompanyId();
				}
				
				switch(typeCodeID) {
					case FundTypeCode.FUND_TYPECODE_ID_UNITIZED:
						toCompareAgainst = unitisedTime;
						
						if (isAmendment) {
							Date baseProcessDate = preRolloverDate;
							if (forceOriginalDate)
								baseProcessDate = DateUtils.getWorkingDay(DateUtils.roundToBeginningOfDay(releaseDate));
	
							if (companyID==Company.CBF || companyID==Company.COIF) {
								if (companyID==Company.CBF && 
										DateUtils.getDayOfWeek(baseProcessDate)==Calendar.TUESDAY)
								{
									toCompareAgainst = unitisedAmendmentTime;
								} else if (companyID==Company.COIF && 
										DateUtils.getDayOfWeek(baseProcessDate)==Calendar.THURSDAY) {
									toCompareAgainst = unitisedAmendmentTime;
								}
							}
						}
						break;
					case FundTypeCode.FUND_TYPECODE_ID_DEPOSIT:
						toCompareAgainst = (isAmendment)?depositAmendmentTime:depositTime;
						break;
					case FundTypeCode.FUND_TYPECODE_ID_PSDF:
						toCompareAgainst = (isAmendment)?psdfAmendmentTime:psdfTime;
						break;
					default:
						toCompareAgainst = (isAmendment)?rolloverAmendmentTime:rolloverTime;
						break;
				}
			}
		} else {
			toCompareAgainst = (isAmendment)?rolloverAmendmentTime:rolloverTime;
		}

		
		//compare against the source times if applicable and get the latest ones.
		Date sourceTime = null;
		if (!StringUtils.stringIsBlank(source) && sourceNameMapping.containsKey(source)) {
			String value = sourceNameMapping.get(source);
			if (!StringUtils.stringIsBlank(value)) {
				if (value.equals(SOURCE_EMAIL)) {
					sourceTime = emailTime;
				} else if (value.equals(SOURCE_FAX)) {
					sourceTime = faxTime;
				} else if (value.equals(SOURCE_FORM_GENERATOR)) {
					sourceTime = formGeneratorTime;
				} else if (value.equals(SOURCE_MANUAL_UPLOAD)) {
					sourceTime = manualUploadTime;
				} else if (value.equals(SOURCE_SCAN_IRIS)) {
					sourceTime = scanForIrisTime;
				} else if (value.equals(SOURCE_STP)) {
					sourceTime = stpTime;
				} else {
					Log.info("Unknown source "+value);
				}
				
				if (sourceTime!=null) {
					Log.debug("Found SourceTime:"+sourceTime.toString()+" for source:"+source);
				}
			} else {
				Log.debug("no source mapping found for sourceName "+source);
			}

		} else {
			Log.debug("no source is specified for calculating processDates");
		}
		
		//Determine which time is greater and use it to compare
		if (sourceTime!=null) {
			if (DateUtils.compareTimes(sourceTime, toCompareAgainst)>0) {
				Log.debug("SourceTime is greater than current toCompareAgainst time, using sourceTime.");
				toCompareAgainst = sourceTime;
			} else {
				Log.debug("toCompareAgainst time is greater than current source time, using toCompareAgainst time.");
			}
		}
		
		//Log.debug("Got toCompareAgainstTime after check 2 "+toCompareAgainst.toString());
		
		if (useValidationTime && validationCutOffTime!=null) 
		{

			if (DateUtils.compareTimes(validationCutOffTime, toCompareAgainst)>0) {
				Log.debug("validationCutOffTime is greater, using validationCutOffTime");
				toCompareAgainst = validationCutOffTime;
			} else {
				Log.debug("compareTime is greater, using compareTime");
			}
			
			if (typeCodeID==FundTypeCode.FUND_TYPECODE_ID_UNITIZED) {

				Date baseProcessDate = preRolloverDate;
				if (forceOriginalDate)
					baseProcessDate = DateUtils.getWorkingDay(DateUtils.roundToBeginningOfDay(releaseDate));

				
				if (companyID==Company.CBF || companyID==Company.COIF) {
					if (companyID==Company.CBF && 
							DateUtils.getDayOfWeek(baseProcessDate)==Calendar.TUESDAY)
					{
						toCompareAgainst = validationCutOffTime;
					} else if (companyID==Company.COIF && 
							DateUtils.getDayOfWeek(baseProcessDate)==Calendar.THURSDAY) {
						toCompareAgainst = validationCutOffTime;
					}
				}
			}
		}
		
		//Log.debug("Got toCompareAgainstTime after check 3 "+toCompareAgainst.toString());
		boolean unitisedCutOff = false;
		if (typeCodeID==FundTypeCode.FUND_TYPECODE_ID_UNITIZED) {
			if (companyID==Company.CBF || companyID==Company.COIF) {
				if (isAmendment || useValidationTime)
					unitisedCutOff = true;
			}
		}
		
		Log.debug("Fund="+(fundCode==null?"":fundCode)+", compareAgainst="+toCompareAgainst.toString()+", releaseDate="+releaseDate.toString()+", "+DateUtils.compareTimes(toCompareAgainst, releaseDate));
		
		if (DateUtils.compareTimes(toCompareAgainst, releaseDate)>0) {
			return getPreRolloverDate(fundCode, typeCodeID, companyID, forceOriginalDate, unitisedCutOff,releaseDate);
		} else {
			return getPostRolloverDate(fundCode, typeCodeID, companyID, forceOriginalDate, unitisedCutOff, releaseDate);
		}
	}
	
	private static Date getPreRolloverDate(String fundCode, int typeCodeID, int companyID, boolean forceOriginalDate, boolean useValidationTime, Date releaseDate) {
		
		Date baseProcessDate = preRolloverDate;
		
		if (forceOriginalDate)
			baseProcessDate = DateUtils.getWorkingDay(DateUtils.roundToBeginningOfDay(releaseDate));
			
		if (!StringUtils.stringIsBlank(fundCode)) {
			
			if (!forceOriginalDate) {
				FundProcessDetails fpd = getFundProcessDetails(fundCode);
				if (fpd!=null && fpd.getOverrideDate()!=null) { 
					baseProcessDate = DateUtils.getWorkingDay(fpd.getOverrideDate());
				}
			}
			
			if (typeCodeID==FundTypeCode.FUND_TYPECODE_ID_UNITIZED) {
				//extra checking for coif and cbf
				if (companyID==Company.CBF) {
					if (DateUtils.getDayOfWeek(baseProcessDate)==Calendar.TUESDAY && !useValidationTime) {
						baseProcessDate = DateUtils.roundToNextDayOfWeek(baseProcessDate, Calendar.WEDNESDAY);
					}
				} else if (companyID==Company.COIF) {
	
					if (DateUtils.getDayOfWeek(baseProcessDate)==Calendar.THURSDAY && !useValidationTime) {
						baseProcessDate = DateUtils.roundToNextDayOfWeek(baseProcessDate, Calendar.FRIDAY);
					}
				}
			}	
		}
		Log.debug("PRE: "+(fundCode==null?"":fundCode)+", companyID="
				+companyID+", typeCodeID="+typeCodeID+", BaseDate="+baseProcessDate.toString());
		
		return baseProcessDate;
	}
	
	private static Date getPostRolloverDate(String fundCode, int typeCodeID, int companyID, boolean forceOriginalDate, boolean useValidationTime, Date releaseDate) {
		
		Date baseProcessDate = postRolloverDate;
		Date preBaseProcessDate = preRolloverDate;
		
		if (forceOriginalDate) {
			baseProcessDate = DateUtils.getWorkingDay(DateUtils.roundToBeginningOfDay(DateUtils.addDays(releaseDate,1)));
			preBaseProcessDate = DateUtils.getWorkingDay(DateUtils.roundToBeginningOfDay(releaseDate));
		}
		
		if (!StringUtils.stringIsBlank(fundCode)) {
			
			if (!forceOriginalDate) {
				FundProcessDetails fpd = getFundProcessDetails(fundCode);
				if (fpd!=null && fpd.getOverrideDate()!=null) {
					baseProcessDate = DateUtils.getWorkingDay(DateUtils.addDays(fpd.getOverrideDate(),1));
				}
			}
			//extra checking for coif and cbf
			if (typeCodeID==FundTypeCode.FUND_TYPECODE_ID_UNITIZED) {
				if (companyID==Company.CBF) {
					if (DateUtils.getDayOfWeek(preBaseProcessDate)!=Calendar.FRIDAY &&
						DateUtils.getDayOfWeek(preBaseProcessDate)!=DateUtils.getDayOfWeek(baseProcessDate)) {
						if (DateUtils.getDayOfWeek(baseProcessDate)==Calendar.MONDAY ||
								DateUtils.getDayOfWeek(baseProcessDate)==Calendar.TUESDAY) {
							baseProcessDate = DateUtils.roundToNextDayOfWeek(baseProcessDate, Calendar.WEDNESDAY);
						}
					}
				} else if (companyID==Company.COIF) {
					if (DateUtils.getDayOfWeek(preBaseProcessDate)==Calendar.WEDNESDAY ||
							DateUtils.getDayOfWeek(preBaseProcessDate)==Calendar.THURSDAY) {
						baseProcessDate = DateUtils.roundToNextDayOfWeek(baseProcessDate, Calendar.FRIDAY);
					}
				}
			}
		}
		Log.debug("POS: "+(fundCode==null?"":fundCode)+", companyID="
				+companyID+", typeCodeID="+typeCodeID+", BaseDate="+baseProcessDate.toString());
		
		return baseProcessDate;
	}
	
	private static FundProcessDetails getFundProcessDetails(String fundCode) {
		if (fundProcessDetailsCache!=null && fundProcessDetailsCache.containsKey(fundCode)) {
			return (FundProcessDetails)fundProcessDetailsCache.get(fundCode);
		} else {
			return null;
		}
	}

	public static HashMap<String,FundProcessDetails> getFundProcessDetailsCache() {
		if (fundProcessDetailsCache!=null) {
			return fundProcessDetailsCache;
		} else {
			return null;
		}
	}
	
	/********************************************************************************************/
	/********************************************************************************************/
	
	
	/** Recalculates the rollover time and pre-/post-rollover process dates.
	 *  
	 * @param facade
	 * @throws DataException
	 */
	public static void refreshProcessDetailsCache(FWFacade facade) 
	 throws DataException {
		
		Log.debug("Refreshing Process Details cache...");

//		SystemConfigUtils.refreshSystemConfigCache(facade);
//		rolloverTime = DateUtils.parseHHcmm(SystemConfigUtils.getSystemConfigValue(ROLLOVER_TIME_NAME));
//		unitisedTime = DateUtils.parseHHcmm(SystemConfigUtils.getSystemConfigValue(UNITISED_TIME_NAME));
//		psdfTime = DateUtils.parseHHcmm(SystemConfigUtils.getSystemConfigValue(PSDF_TIME_NAME));
//		depositTime = DateUtils.parseHHcmm(SystemConfigUtils.getSystemConfigValue(DEPOSIT_TIME_NAME));
//		overrideDate = DateUtils.parseddsMMsyyyyspHHcmm(SystemConfigUtils.getSystemConfigValue(OVERRIDE_DATE_NAME));

		SystemConfigVar.getCache().buildCache(facade);	
		
		rolloverTime = DateUtils.parseHHcmm(SystemConfigVar.getCache().getCachedInstance(ROLLOVER_TIME_NAME).getStringValue());
		unitisedTime = DateUtils.parseHHcmm(SystemConfigVar.getCache().getCachedInstance(UNITISED_TIME_NAME).getStringValue());
		psdfTime = DateUtils.parseHHcmm(SystemConfigVar.getCache().getCachedInstance(PSDF_TIME_NAME).getStringValue());
		depositTime = DateUtils.parseHHcmm(SystemConfigVar.getCache().getCachedInstance(DEPOSIT_TIME_NAME).getStringValue());
		overrideDate = DateUtils.parseddsMMsyyyyspHHcmm(SystemConfigVar.getCache().getCachedInstance(OVERRIDE_DATE_NAME).getStringValue());
		
		SystemConfigVar configVar = null; 
		
		configVar = SystemConfigVar.getCache().getCachedInstance(VALIDATION_CUT_OFF_TIME);
		if (configVar !=null && !StringUtils.stringIsBlank(configVar.getStringValue())) {
			validationCutOffTime = DateUtils.parseHHcmm(configVar.getStringValue());	
		}
		
		if (configVar !=null && !StringUtils.stringIsBlank(configVar.getStringValue())) {
			rolloverAmendmentTime = DateUtils.parseHHcmm(configVar.getStringValue());
		}
		if (rolloverAmendmentTime==null)
			rolloverAmendmentTime = rolloverTime;
		
		configVar = SystemConfigVar.getCache().getCachedInstance(UNITISED_AMENDMENT_TIME_NAME);
		if (configVar !=null && !StringUtils.stringIsBlank(configVar.getStringValue())) {
			unitisedAmendmentTime = DateUtils.parseHHcmm(configVar.getStringValue());
		}
		if (unitisedAmendmentTime==null)
			unitisedAmendmentTime = unitisedTime;

		
		configVar = SystemConfigVar.getCache().getCachedInstance(PSDF_AMENDMENT_TIME_NAME);
		if (configVar !=null && !StringUtils.stringIsBlank(configVar.getStringValue())) {
			psdfAmendmentTime = DateUtils.parseHHcmm(configVar.getStringValue());
		}
		if (psdfAmendmentTime==null)
			psdfAmendmentTime = psdfTime;

		configVar = SystemConfigVar.getCache().getCachedInstance(DEPOSIT_AMENDMENT_TIME_NAME);
		if (configVar !=null && !StringUtils.stringIsBlank(configVar.getStringValue())) {
			depositAmendmentTime = DateUtils.parseHHcmm(configVar.getStringValue());
		}
		if (depositAmendmentTime==null)
			depositAmendmentTime = depositTime;
		
		configVar = SystemConfigVar.getCache().getCachedInstance(FAX_TIME_NAME);
		if (configVar!=null && !StringUtils.stringIsBlank(configVar.getStringValue())) {
			faxTime = DateUtils.parseHHcmm(configVar.getStringValue());
		}
			
		configVar = SystemConfigVar.getCache().getCachedInstance(STP_TIME_NAME);
		if (configVar!=null && !StringUtils.stringIsBlank(configVar.getStringValue())) {
			stpTime = DateUtils.parseHHcmm(configVar.getStringValue());
		}
		
		configVar = SystemConfigVar.getCache().getCachedInstance(SCAN_FOR_IRIS_TIME_NAME);
		if (configVar!=null && !StringUtils.stringIsBlank(configVar.getStringValue())) {
			scanForIrisTime = DateUtils.parseHHcmm(configVar.getStringValue());
		}
		
		configVar = SystemConfigVar.getCache().getCachedInstance(MANUAL_UPLOAD_TIME_NAME);
		if (configVar!=null && !StringUtils.stringIsBlank(configVar.getStringValue())) {
			manualUploadTime = DateUtils.parseHHcmm(configVar.getStringValue());
		}
		
		configVar = SystemConfigVar.getCache().getCachedInstance(FORM_GENERATOR_TIME_NAME);
		if (configVar!=null && !StringUtils.stringIsBlank(configVar.getStringValue())) {
			formGeneratorTime = DateUtils.parseHHcmm(configVar.getStringValue());
		}
		
		configVar = SystemConfigVar.getCache().getCachedInstance(EMAIL_TIME_NAME);
		if (configVar!=null && !StringUtils.stringIsBlank(configVar.getStringValue())) {
			emailTime = DateUtils.parseHHcmm(configVar.getStringValue());
		}
		
		
		if (unitisedTime==null || psdfTime==null || 
				depositTime==null || rolloverTime==null) 
		{
			if (rolloverTime==null) {
				throw new DataException("Unable to calculate Process Date: " +
				"rollover time entry missing");
			} else if (unitisedTime==null) {
				throw new DataException("Unable to calculate Process Date: " +
				"unitised time entry missing");
			} else if (depositTime==null) {
				throw new DataException("Unable to calculate Process Date: " +
				"deposit time entry missing");
			} else if (psdfTime==null) {
				throw new DataException("Unable to calculate Process Date: " +
				"psdf time entry missing");
			} 
		} else {
			
			rolloverTime	= DateUtils.getTodaysDateWithSetTime(rolloverTime);
			psdfTime		= DateUtils.getTodaysDateWithSetTime(psdfTime);
			depositTime		= DateUtils.getTodaysDateWithSetTime(depositTime);
			unitisedTime	= DateUtils.getTodaysDateWithSetTime(unitisedTime);
			
			rolloverAmendmentTime = DateUtils.getTodaysDateWithSetTime(rolloverAmendmentTime);
			psdfAmendmentTime = DateUtils.getTodaysDateWithSetTime(psdfAmendmentTime);
			depositAmendmentTime = DateUtils.getTodaysDateWithSetTime(depositAmendmentTime);
			unitisedAmendmentTime = DateUtils.getTodaysDateWithSetTime(unitisedAmendmentTime);
			
			if (validationCutOffTime!=null)
				validationCutOffTime = DateUtils.getTodaysDateWithSetTime(validationCutOffTime);
			
			if (faxTime!=null)
				faxTime = DateUtils.getTodaysDateWithSetTime(faxTime);
			
			if (stpTime!=null)
				stpTime = DateUtils.getTodaysDateWithSetTime(stpTime);
			
			if (scanForIrisTime!=null)
				scanForIrisTime = DateUtils.getTodaysDateWithSetTime(scanForIrisTime);
			
			if (manualUploadTime!=null)
				manualUploadTime = DateUtils.getTodaysDateWithSetTime(manualUploadTime);
			
			if (formGeneratorTime!=null)
				formGeneratorTime = DateUtils.getTodaysDateWithSetTime(formGeneratorTime);

			if (emailTime!=null)
				emailTime = DateUtils.getTodaysDateWithSetTime(emailTime);
			
			
			//set the sourceNames to the mappings
			sourceNameMapping = new HashMap<String,String>();
			String[] arrayStr = null;
			for (int i=0; i<=SOURCES.length-1; i++) 
			{
				configVar = SystemConfigVar.getCache().getCachedInstance(SOURCES[i]);
				if (configVar!=null && !StringUtils.stringIsBlank(configVar.getStringValue())) 
				{
					arrayStr = StringUtils.stringToArray(configVar.getStringValue());					
					for (int z=0; z<=arrayStr.length-1; z++) {
						sourceNameMapping.put(arrayStr[z], SOURCES[i]);
					}
				}				
			}
			
			// Create a date instance set to midnight of the current day.
			Date currentDate = null;
			
			if (overrideDate!=null) {
				currentDate = DateUtils.roundToBeginningOfDay(overrideDate);
			} else {
				currentDate = DateUtils.roundToBeginningOfDay(DateUtils.getNow());					
			}
			
			preRolloverDate 	= DateUtils.getWorkingDay(currentDate);			
			postRolloverDate 	= DateUtils.getWorkingDay(DateUtils.addDays(currentDate, 1));	
		}

		Log.debug("Process Details cache refreshed.");
		Log.debug("Rollover Time: "+DateFormatter.getTimeStamp(rolloverTime));		
		Log.debug("PSDF Time: "+DateFormatter.getTimeStamp(psdfTime));
		Log.debug("Unitised Time: "+DateFormatter.getTimeStamp(unitisedTime));
		Log.debug("Deposit Time: " +DateFormatter.getTimeStamp(depositTime));
		Log.debug("Pre-Rollover Date: "+DateFormatter.getTimeStamp(preRolloverDate));
		Log.debug("Post-Rollover Date: " +DateFormatter.getTimeStamp(postRolloverDate));

		Log.debug("Amendment Rollover Time: "+DateFormatter.getTimeStamp(rolloverAmendmentTime));		
		Log.debug("Amendment PSDF Time: "+DateFormatter.getTimeStamp(psdfAmendmentTime));
		Log.debug("Amendment Unitised Time: "+DateFormatter.getTimeStamp(unitisedAmendmentTime));
		Log.debug("Amendment Deposit Time: " +DateFormatter.getTimeStamp(depositAmendmentTime));		
		
		if (faxTime!=null) 
			Log.debug("Fax Time: "+DateFormatter.getTimeStamp(faxTime));		
		
		if (emailTime!=null)
			Log.debug("Email Time: "+DateFormatter.getTimeStamp(emailTime));
		
		if (stpTime!=null)
			Log.debug("STP Time: "+DateFormatter.getTimeStamp(stpTime));
		
		if (scanForIrisTime!=null)
			Log.debug("Scan Time: " +DateFormatter.getTimeStamp(scanForIrisTime));	
		
		if (manualUploadTime!=null)
			Log.debug("Manual Upload Time: " +DateFormatter.getTimeStamp(manualUploadTime));	
		
		if (formGeneratorTime!=null)
			Log.debug("Form Generator Time: " +DateFormatter.getTimeStamp(formGeneratorTime));	
		
		if (validationCutOffTime!=null)
			Log.debug("Validation Cut-Off Time: " +DateFormatter.getTimeStamp(validationCutOffTime));	
		
		lastUpdated = DateUtils.getNow();
	}	
	
	
	public static void refreshFundProcessDetailsCache(FWFacade facade) throws DataException {
		
		Log.debug("Refreshing Fund Process Details cache...");
		
		DataBinder binder = new DataBinder();
		
		DataResultSet rs = 
		 facade.createResultSet("qClientServices_GetAllProcessDetails", binder);
		
		if (fundProcessDetailsCache!=null)
			fundProcessDetailsCache.clear();
		else 
			fundProcessDetailsCache = new HashMap<String,FundProcessDetails>();	
		
		if (rs!=null && !rs.isEmpty()) {
			do {
				FundProcessDetails fpd = FundProcessDetails.get(rs);
				fundProcessDetailsCache.put(fpd.getFundCode(), fpd);		
			} while(rs.next());
		}		
		
		fpdLastUpdated = DateUtils.getNow();
	}	
	
	/**
	 * Get the fund code based on customer specific rules
	 * //1. If the xDocumentClass is BUYDF, then use dest fund code (xDestinationFund)
	 * //2. If the xDocumentClass is TRANSOUT, then use dest fund code (xDestinationFund) 
	 * //   if the the fund code is V,W,N,P,62
	 *  
	 * @param rsContentItem
	 * @return
	 */
	private static String getFundCodeToUse(DataResultSet rsContentItem) 
	{
		String fundCode	= rsContentItem.getStringValueByName("xFund");
		String docClass	= rsContentItem.getStringValueByName("xDocumentClass");
		String destFundCode	= rsContentItem.getStringValueByName("xDestinationFund");
		
		return getFundCodeToUse(fundCode, destFundCode, docClass);
	}
	
	/**
	 * Get the fund code based on customer specific rules
	 * //1. If the xDocumentClass is BUYDF, then use dest fund code (xDestinationFund)
	 * //2. If the xDocumentClass is TRANSOUT, then use dest fund code (xDestinationFund) 
	 * //   if the the fund code is V,W,N,P,62
	 *  
	 * @param rsContentItem
	 * @return
	 */
	private static String getFundCodeToUse(String fundCode, String destFundCode, String docClass) 
	{
		
		if (StringUtils.stringIsBlank(fundCode) || StringUtils.stringIsBlank(docClass))
			return fundCode;
		
		Fund f = null;
		
		try {
			if ((f=Fund.getCache().getCachedInstance(fundCode))!=null) 
			{
				if (destFundMapping!=null && !destFundMapping.isEmpty() && destFundMapping.containsKey(docClass)) 
				{
					String destFundDefinition = destFundMapping.get(docClass);
					
					if (destFundDefinition.equals("ALL") || f.getDefinitionCode().getFundDefCode().equals(destFundDefinition)) 
					{
						if (!StringUtils.stringIsBlank(destFundCode)) {						
							Log.debug("Using destinationFundCode for DocumentClass="+docClass+", DestFundCode="+destFundCode+", FundCode="+fundCode);
							fundCode = destFundCode;
						}
					} 
				}	
			}
		} catch (DataException de) {
			Log.error("Cache not initialised "+de.getMessage());
		}
		return fundCode;
	}	
}
