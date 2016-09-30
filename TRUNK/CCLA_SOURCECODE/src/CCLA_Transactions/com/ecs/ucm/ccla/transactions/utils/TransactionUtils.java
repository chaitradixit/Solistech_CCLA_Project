package com.ecs.ucm.ccla.transactions.utils;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.data.ResultSetUtils;
import intradoc.data.Workspace;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.transactions.globals.TransactionGlobals;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

public class TransactionUtils {

	
	private static DecimalFormat PSDFDecimalFormat = null; 
	private static final String psdfDecimalFormatStr = "0.##########";
	
	/** Creates an Long instance using the named value
	 *  from the current row of the passed ResultSet.
	 *  
	 * @param rs
	 * @param name
	 * @return
	 * @throws ServiceException if the ResultSet value is non-empty,
	 * 							but not a valid integer
	 */
	public static long getResultSetLongValue
	 (DataResultSet rs, String name) throws DataException {
		
		String valueStr = ResultSetUtils.getValue(rs, name);
		Log.debug("value is " + valueStr);
		Long value = null;
		
		if (!StringUtils.stringIsBlank(valueStr)) {
			try {
				value = Long.parseLong(valueStr);
			} catch (NumberFormatException e) {
				String msg = "Expected an integer for '" 
				 + name + "', got value: '" + name + "'";
				
				Log.error(msg);
				throw new DataException(msg);
			}
		}
		
		return value;
	}
	
	/** Creates an BigDecimal instance using the named value
	 *  from the current row of the passed ResultSet.
	 *  
	 * @param rs
	 * @param name
	 * @return
	 * @throws ServiceException if the ResultSet value is non-empty,
	 * 							but not a valid integer
	 */
	public static BigDecimal getResultSetBigDecimalValue
	 (DataResultSet rs, String name) throws DataException {
		
		String valueStr = ResultSetUtils.getValue(rs, name);
		BigDecimal value = null;
		
		if (!StringUtils.stringIsBlank(valueStr)) {
			try {
				value = new BigDecimal(valueStr);
			} catch (NumberFormatException e) {
				String msg = "Expected an integer for '" 
				 + name + "', got value: '" + name + "'";
				
				Log.error(msg);
				throw new DataException(msg);
			}
		}
		
		return value;
	}
	
	/** Adds the given Integer to the binder. If the passed Integer
	 *  is null, an empty string is added instead. This ensures
	 *  that any queries requiring this parameter will always execute.
	 *  
	 * @param binder
	 * @param param
	 */ 
	public static void addLongParamToBinder
	 (DataBinder binder, String name, Long value) {
		
		if (value == null)
			binder.putLocal(name, "");
		else
			binder.putLocal(name, value.toString());
	}
	
	/** Adds the given Integer to the binder. If the passed Integer
	 *  is null, an empty string is added instead. This ensures
	 *  that any queries requiring this parameter will always execute.
	 *  
	 * @param binder
	 * @param param
	 */ 
	public static void addBigDecimalParamToBinder
	 (DataBinder binder, String name, BigDecimal value) {
		
		if (value == null)
			binder.putLocal(name, "");
		else
			binder.putLocal(name, value.toString());
	}

	/** Creates a long instance using the named value
	 *  from the current row of the passed ResultSet.
	 *  
	 *  Returns null if given DataBinder value is empty/missing.
	 *  
	 * @param binder
	 * @param name
	 * @return
	 * @throws ServiceException if the Binder value is non-empty,
	 * 							but not a valid integer
	 */
	public static BigDecimal getBinderBigDecimalValue
	 (DataBinder binder, String name) throws DataException {
		
		String valStr = binder.getLocal(name);
		
		if (!StringUtils.stringIsBlank(valStr)) {
			try {
				return new BigDecimal(valStr);
			} catch (NumberFormatException e) {
				String msg = "Expected a long for '" 
				 + name + "', got value: '" + valStr + "'";
				
				Log.error(msg);
				throw new DataException(msg);
			}
		} else
			return null;
	}	
	
	/** Creates a long instance using the named value
	 *  from the current row of the passed ResultSet.
	 *  
	 *  Returns null if given DataBinder value is empty/missing.
	 *  
	 * @param binder
	 * @param name
	 * @return
	 * @throws ServiceException if the Binder value is non-empty,
	 * 							but not a valid integer
	 */
	public static Long getBinderLongValue
	 (DataBinder binder, String name) throws DataException {
		
		String valStr = binder.getLocal(name);
		
		if (!StringUtils.stringIsBlank(valStr)) {
			try {
				return Long.parseLong(valStr);
			} catch (NumberFormatException e) {
				String msg = "Expected a long for '" 
				 + name + "', got value: '" + valStr + "'";
				
				Log.error(msg);
				throw new DataException(msg);
			}
		} else
			return null;
	}
	
	

	
	/** Fetches the next primary key for the given ID label.
	 * 
	 *  The passed FWFacade instance must have been initialised
	 *  with a workspace instance to enable database calls.
	 *  
	 *  Available ID labels: ClientProcess, Activity, 
	 *  FundTransfer, ProcessNote, ClientQuestion
	 *  
	 * @param idLabel
	 */
	public static String getNewKey(String idLabel, FWFacade facade)
	 throws DataException {
	
		// Fetch new process ID using dynamically-built query name.
		DataResultSet rsNextVal = facade.createResultSet
		 ("qTransactions_GetNext" + idLabel + "Id", new DataBinder());
		
		String nextVal = rsNextVal.getStringValue(0);
		return nextVal;
	}
	
	/** Returns a string representation of a number padded to two decimal places
	 * 
	 * @param currency
	 * @return
	 */
	public static String getFormattedCurrency(String currency)
	{
		DecimalFormat twoPlaces = new DecimalFormat("#,###,###,###.00");
		Double number = Double.valueOf(currency);
		return twoPlaces.format(number);
	}
	
	
	/** Returns a string representation of a number with commas to separate 
	 * thousands
	 * 
	 * @param currency
	 * @return
	 */
	public static String getFormattedInteger(String format)
	{
		DecimalFormat twoPlaces = new DecimalFormat("#,###,###,###");
		Double number = Double.valueOf(format);
		return twoPlaces.format(number);
	}	
	
	/**Compare 2 dates so see if they fall on the same day
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameDay(Date date1, Date date2)
	{
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(date1);
		cal2.setTime(date2);
		boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
		                  cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
		return sameDay;
	}
	
    /**
     * Gets dataresultset of share classes for a particular fund where the is_enabled flag is 1
     * ORDERED BY SHARE CLASS ID
     * 
     * @return DataResultSet of share classes, or null if none exist for fund
     * @throws DataException
     */		
	public static DataResultSet getEnabledShareClassesByFundOrdered(String fundCode, FWFacade facade) 
	throws DataException
	{
		DataBinder binder = new DataBinder();
		BinderUtils.addParamToBinder(binder, "FUND_CODE", fundCode);
		DataResultSet rsShareClasses = facade.createResultSet("qTransactions_GetEnabledShareClassbyFundOrdered", binder);
		
		if (!rsShareClasses.isEmpty())
			return rsShareClasses;
		else
			return null;
	}
	
	
	public static DecimalFormat getPSDFDecimalFormat() {
		
		if (PSDFDecimalFormat!=null)
			return PSDFDecimalFormat;
		else {
			if (StringUtils.stringIsBlank(TransactionGlobals.PSDF_DECIMAL_FORMAT_STR)) {
					PSDFDecimalFormat = new DecimalFormat(TransactionGlobals.PSDF_DECIMAL_FORMAT_STR);
			} else {
				PSDFDecimalFormat = new DecimalFormat(psdfDecimalFormatStr);
			}	
		}
		return PSDFDecimalFormat;
	}
}
