package com.ecs.ucm.ccla;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import java.util.regex.Pattern;

import oracle.jdbc.pool.OracleConnectionCacheManager;
import oracle.jdbc.pool.OracleDataSource;

import org.apache.commons.lang.WordUtils;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.data.ResultSetUtils;
import intradoc.data.Workspace;
import intradoc.provider.Provider;
import intradoc.provider.Providers;
import intradoc.shared.SharedObjects;

import com.ecs.ucm.ccla.Globals.UCMDocTypes;
import com.ecs.ucm.ccla.Globals.UCMFieldNames;
import com.ecs.ucm.ccla.data.Activity;
import com.ecs.ucm.ccla.data.Company;
import com.ecs.ucm.ccla.data.Fund;
import com.ecs.ucm.ccla.data.Note;

import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.comm.Comm;
import com.ecs.ucm.ccla.data.instruction.Instruction;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.DateFormatter;
import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.LWFacade;
import com.ecs.utils.stellent.embedded.FWFacade;

public class CCLAUtils {

	private static int MAX_FUND_CODE_LENGTH = 4;

	/** Determines whether NT Domains are stripped off the start of usernames coming
	 *  from external sources (i.e. web service calls from Workflow)
	 */
	public static boolean STRIP_NT_DOMAIN_FROM_USERNAMES = true;
	
	/** List of available database connection pool IDs, all configured to point at
	 *  Central Database via UCMADMIN user.
	 *  
	 */
	private static Vector<String> DB_CONNECTION_POOL_IDS;
	
	/** Initialise all static env vars. 
	 * 
	 *  Wrapped in a try-catch to prevent failures when testing methods locally.
	 *  
	 */
	static {
		try {
			MAX_FUND_CODE_LENGTH = SharedObjects.getEnvironmentInt
			 ("CCLA_CORE_Max_Fund_Code_Length", 4);
			
			STRIP_NT_DOMAIN_FROM_USERNAMES = 
			 !StringUtils.stringIsBlank(
			 SharedObjects.getEnvironmentValue("CCLA_stripNTDomainFromUsernames"));
			
			DB_CONNECTION_POOL_IDS = new Vector<String>();
			
			String connPoolIdStr = SharedObjects.getEnvironmentValue
			 ("CCLA_CORE_DatabaseConnectionPoolIds");
			
			if (connPoolIdStr != null) {
				String[] ids = connPoolIdStr.split(",");
				
				for (String id : ids) {
					DB_CONNECTION_POOL_IDS.add(id);
				}
			}
		} catch (Exception e) {
			// This is quite serious error when running in UCM context.
			Log.error("Failed to set CCLAUtils static vars!", e);
		}
	}
	
	/** RegExp pattern for the 12-character Account Codes used for Person 
	 *  and Organisation records.
	 *  
	 *  Enforces 4 capital alpha, 8 numeric characters.
	 */
	public static Pattern ACCOUNT_CODE_PATTERN = Pattern.compile("^[A-Z]{4}\\d{8}$");
	
	/** 'Plain' NumberFormat used for formatting Floats, Doubles etc. to ensure
	 *  that exponents and comma separators aren't ever used.
	 */
	public static final NumberFormat PLAIN_NUMBER_FORMAT;
	
	/** Used when converting decimal strings to fixed-format decimal values, with
	 *  2 decimal places.
	 */
	public static final DecimalFormat DECIMAL_FORMAT;
	
	static {
		PLAIN_NUMBER_FORMAT = NumberFormat.getInstance();
		PLAIN_NUMBER_FORMAT.setGroupingUsed(false);
		
		DECIMAL_FORMAT = new DecimalFormat("0.00");
	}
	
	/** Fetches the next workflow ID (formerly envelope ID, stored in 
	 * 	the xBatchNumber field of an item before it is submitted to SPP.
	 * 
	 *  SPP still refer to this field value as the Envelope ID. In UCM it
	 *  is more of a workflow batch ID.
	 *  
	 * @param workspace
	 * @return
	 */
	public static String getNextEnvelopeId(FWFacade facade) 
	 throws DataException {
		
		DataResultSet rs = facade.createResultSet
		 ("qGetNextEnvelopeId", new DataBinder());
		
		if (rs == null || rs.isEmpty() 
			|| 
			StringUtils.stringIsBlank(rs.getStringValue(0)))
			throw new DataException("Unable to retrieve new envelope ID");
		
		return rs.getStringValue(0);
	}
	
	public static String getNextWorkflowId(FWFacade facade) throws DataException {
		
		DataResultSet rs = 
		 facade.createResultSet("qGetNextWorkflowId", new DataBinder());
		
		if (rs == null || rs.isEmpty() 
			|| 
			StringUtils.stringIsBlank(rs.getStringValue(0)))
			throw new DataException("Unable to fetch new workflow ID");
		
		return rs.getStringValue(0);
	}
	
	/** Fetches the next job ID, which will be stored in the xJobId field.
	 *  Any item that starts a job in SPP should get a unique job id, and
	 *  it should be passed to SPP.
	 *   
	 * @return
	 * @throws DataException 
	 * @throws DataException 
	 */
	public static int getNextSppJobId(FWFacade facade) throws DataException {
		
		DataResultSet rs = 
		 facade.createResultSet("qGetNextJobId", new DataBinder());
			
		if (rs == null || rs.isEmpty() 
			|| 
			StringUtils.stringIsBlank(rs.getStringValue(0)))
			throw new DataException("Unable to fetch new SPP Job ID");
		
		return Integer.parseInt(rs.getStringValue(0));
	}
	
	/**
	 * Will return the last 3 or 2 or 1 A-Z characters of the passed account number
	 * where present.
	 * 
	 * If null account is passed, an empty fund is returned.
	 * 
	 * If the last four characters (or more) of the passed account number is 
	 * passed, an empty fund will be returned, as this many chars isn't supported.
	 * 
	 * @param accountNo
	 * @return
	 */
	public static String getSuffixChars(String accountNo)
	{
		if(accountNo == null) 
			return "";
		
		String[] array = new String[MAX_FUND_CODE_LENGTH];

		for (int z=array.length;z>=1;z--) {
			if (accountNo.length()>=z) {
				array[z-1] = accountNo.substring(accountNo.length() -z, accountNo.length()-(z-1));				
			} else {
				array[z-1] = "";
			}
		}
		
		Fund fund = null;
		boolean found = false;
		int x = MAX_FUND_CODE_LENGTH-1;
		
		do {	
			String possibleFundCode = "";
			for (int i=x; i>=0; i--) {
				possibleFundCode+=array[i];
			}
		
			try {
				fund = Fund.getCache().getCachedInstance(possibleFundCode);

				if (fund!=null) {
					found = true;
				}
			} catch (DataException de) {
				Log.error(de.getMessage(), de);
			}
			
			x--;
		} while (x>=0 && !found);
		
		
		if (fund!=null) {
			return fund.getFundCode();
		} 
		return "";
	}
	
	/**
	 * Returns true if passed string is an A-Z character.
	 * 
	 * @param character
	 * @return
	 */
	private static boolean isAlphaChar(String character){
		return character.matches("^[A-Z]$");
	}
	
	
	/** @deprecated
	 * 
	 *  replace with query to AML checklist
	 * 
	 * @param clientId
	 * @return
	 */
	public static DataResultSet getClientAMLStatus(String clientId) {
		
		Workspace amlWorkspace = null;
		DataResultSet emptyDrs = new DataResultSet(new String[] {"Responded"});
		
		try {
			Provider odcProvider = Providers.getProvider("AML");	
			if (odcProvider != null)
				amlWorkspace = (Workspace)odcProvider.getProvider();
			else {
				Log.warn("Unable to retrieve AML provider!");
				//throw new ServiceException("Unable to retrieve AML provider!");
				return emptyDrs;
			}
			
			if (amlWorkspace == null) {
				Log.warn("Unable to retrieve AML workspace instance!");
				//throw new ServiceException("Unable to retrieve AuroraCOIF workspace instance!");
			} else {
				
				//String query = new String(GET_CORRESPONDENT_RESPONDED_AML_QUERY);
				String query = "";
				query += clientId + ")";
				
				//Log.debug("Calling AML provider with query: " + query);
				//ResultSet rs = amlWorkspace.createResultSetSQL(query);
				
				DataResultSet drs = new DataResultSet();
				//drs.copy(rs);
				
				return drs;
			}
		} catch (Exception e) {
			Log.warn("Unable to access AML DB provider. Returning empty ResultSet");
		}
		
		return emptyDrs;
	}
	
	/** Used to pad client numbers with length < 5 with extra zeros,
	 *  so they always appear as 5 characters in length.
	 *  
	 * @param clientNumber
	 * @return
	 */
	public static String padClientNumber(String clientNumber) {
		return padString(clientNumber, '0', 5);
	}
	
	/** Pads bank account numbers, so they are always 8 digits
	 *  long.
	 *  
	 * @param padded account number
	 * @return
	 */
	public static String padBankAccountNumber(String accountNo) {
		return padString(accountNo, '0', 8);
	}
	
	/** Pads sort codes, so they are always 6 digits long.
	 *  
	 * @param padded sort code
	 * @return
	 */
	public static String padSortCode(String sortCode) {
		return padString(sortCode, '0', 6);
	}
	
	/** Left-pads the passed string with the given character, up to
	 *  the given length.
	 *  
	 *  If the passed string already matches or exceeds the passed
	 *  length, it is returned unchanged.
	 *  
	 * @param string
	 * @param padChar
	 * @param length
	 * @return
	 */
	public static String padString(String string, char padChar, int length) {
		return padString(string, padChar, length, PadType.LEFT);
	}
	
	public enum PadType {
		LEFT,
		RIGHT
	}
	
	/** Left- or right-pads the passed string with the given character, up to
	 *  the given length.
	 *  
	 *  If the passed string already matches or exceeds the passed
	 *  length, it is returned unchanged.
	 *  
	 * @param string
	 * @param padChar
	 * @param length
	 * @return
	 */
	public static String padString(String string, char padChar, int length, PadType type) {
		
		if (string.length() < length) {
			StringBuilder paddedString = new StringBuilder();
			
			int padLength = length - string.length();
			
			for (int i=0; i<padLength; i++)
				paddedString.append(padChar);
			
			if (type == PadType.LEFT)
				paddedString.append(string);
			else
				paddedString.insert(0, string);
			
			return paddedString.toString();
			
		} else
			return string;
	}
	
	/** Returns the string suffix used for pretty-formatted dates etc.
	 *  
	 *  Where d is the last digit of the passed integer i:
	 * 
	 *  So 	d=1 returns 'st'
	 *  	d=2 returns 'nd'
	 *  	d=3 returns 'rd'
	 *  	others return 'th'
	 *  
	 * @param i
	 * @return
	 */
	public static String getNumericSuffix(int i) {
		String s = Integer.toString(i);
		String lastDigit = s.substring(s.length()-1);
		
		if (lastDigit.equals("1"))
			return "st";
		else if (lastDigit.equals("2"))
			return "nd";
		else if (lastDigit.equals("3"))
			return "rd";
		else
			return "th";
	}
	
	public static void main(String[] args) throws DataException {
		
		DataResultSet rs = new DataResultSet(new String[] {
			"ID", "NAME"	
		});
		
		Vector row1 = new Vector();
		row1.add("12345");
		row1.add("Tom's M,,arc/hant");
		
		rs.addRow(row1);
		
		Vector<String> fields = new Vector<String>();
		fields.add("NAME");
		
		removeResultSetValueChars(rs, new String[] { ",", "'" }, fields);
		
		System.out.println("NAME value: " + rs.getStringValueByName("NAME"));
		
		String s = "Tom's acco\"unt name";
		
		escapeForOracle(s);
		
		String accCode = "ABCD00001234";
		
		System.out.println("Acc Code valid? " + isAccountCodeValid(accCode, true));
		System.out.println("Acc Code num? " + getAccountCodeNumberSuffix(accCode));

	}
	
	/** Adds the given String to the binder. If the passed String
	 *  is null, an empty string is added instead. This ensures
	 *  that any queries requiring this parameter will always execute.
	 *  
	 * @param binder
	 * @param param
	 */
	public static void addQueryParamToBinder
	 (DataBinder binder, String name, String value) {
		
		if (StringUtils.stringIsBlank(value))
			binder.putLocal(name, "");
		else
			binder.putLocal(name, value);
	}
	
	/** Adds the given Integer to the binder. If the passed Integer
	 *  is null, an empty string is added instead. This ensures
	 *  that any queries requiring this parameter will always execute.
	 *  
	 * @param binder
	 * @param param
	 */
	public static void addQueryIntParamToBinder
	 (DataBinder binder, String name, Integer value) {
		
		if (value == null)
			binder.putLocal(name, "");
		else
			binder.putLocal(name, value.toString());
	}
	
	public static void addQueryFloatParamToBinder
	 (DataBinder binder, String name, Float value) {
		
		if (value == null)
			binder.putLocal(name, "");
		else
			binder.putLocal(name, PLAIN_NUMBER_FORMAT.format(value));
	}

	public static void addQueryBigDecimalParamToBinder
	 (DataBinder binder, String name, BigDecimal value) {
		
		if (value == null)
			binder.putLocal(name, "");
		else {
			binder.putLocal(name, value.toPlainString());
		}
		
	}	
	
	/** Adds the given Date to the binder. The Date is converted
	 *  to a String using the DateFormatter functions, before being
	 *  delegated to the String version of this method.
	 *  
	 * @param binder
	 * @param param
	 */
	public static void addQueryDateParamToBinder
	 (DataBinder binder, String name, Date value) {
		
		if (value == null)
			binder.putLocal(name, "");
		else {
			
			
			addQueryParamToBinder(binder, name, 
			 DateFormatter.getTimeStamp(value));
		}
	}
	
	/** Attempts to fetch a date string from the binder and covert
	 *  it into a java.util.Date instance using the DateFormatter
	 *  functions.
	 *  
	 * @param binder
	 * @param name
	 * @return
	 * @throws ParseException 
	 * @throws DataException 
	 */
	public static Date getBinderDateValue
	 (DataBinder binder, String name) throws DataException {
		
		String dateStr = binder.getLocal(name);
		boolean isSimple = true;
		
		if (!StringUtils.stringIsBlank(dateStr)) {
			try {
				if (!dateStr.contains(" ")) {
					// No space in the date string, append hh:mm.
					dateStr += " 00:00";
				}
				//String dateFormat = binder.getLocal("blDateFormat");
				
				// if the datestr starts with YYYY-MM-DD then use the DateFormatter.decode date func
				if (dateStr.length()>4 && dateStr.charAt(4)=='-')
					isSimple = false;
				
				//When operating with the context of the LWDocuments, 
				//the ridc libraries will convert and alter the date formats and date strings.
				//Need to check for the dateformat in the check and parse accordingly.
				if (isSimple)
					return DateFormatter.getSystemSimpleDateFormat().parse(dateStr);					
				else 
					return DateFormatter.decodeDate(dateStr).getTime();

			} catch (ParseException e) {
				String errorMsg = "Expected a date value for '" 
					 + dateStr + "', failed to parse date. IsSimpleDateFormat: "+isSimple+",  " + e.toString(); 
				Log.error(errorMsg);
				throw new DataException(errorMsg, e);
			}
		} else
			return null;
	}
	
	/** Adds the given boolean to the binder. If the boolean
	 *  is true, a "1" is added to the binder, "0" otherwise.
	 *  
	 * @param binder
	 * @param param
	 */
	public static void addQueryBooleanParamToBinder
	 (DataBinder binder, String name, boolean value) {
		
		if (value)
			binder.putLocal(name, "1");
		else
			binder.putLocal(name, "0");
	}
	
	/** Adds the given boolean to the binder. 
	 * 
	 * If the boolean is null, adds an empty string to the binder.
	 * 
	 * If the boolean is true, a "1" is added to the binder, 
	 * "0" otherwise.
	 *  
	 * @param binder
	 * @param param
	 */
	public static void addQueryBooleanParamToBinderAllowNull
	 (DataBinder binder, String name, Boolean value) {
		
		if (value == null)
			binder.putLocal(name, "");
		else if (value)
			binder.putLocal(name, "1");
		else
			binder.putLocal(name, "0");
	}

	/** Creates an Integer instance using the named value
	 *  from the current row of the passed ResultSet.
	 *  
	 * @param rs
	 * @param name
	 * @return
	 * @throws ServiceException if the ResultSet value is non-empty,
	 * 							but not a valid integer
	 */
	public static Integer getResultSetIntegerValue
	 (DataResultSet rs, String name) throws DataException {
		
		String valueStr = rs.getStringValueByName(name);
		Integer value   = null;
		
		if (!StringUtils.stringIsBlank(valueStr)) {
			try {
				value = new Integer(valueStr);
			} catch (NumberFormatException e) {
				String msg = "Expected an integer for '" 
				 + name + "', got value: '" + valueStr + "'";
				
				Log.error(msg);
				throw new DataException(msg);
			}
		}
		
		return value;
	}
	
	/** Creates an Integer instance using the named value
	 *  from the current row of the passed ResultSet.
	 *  
	 * @param binder
	 * @param name
	 * @return
	 * @throws ServiceException if the Binder value is non-empty,
	 * 							but not a valid integer
	 */
	public static Integer getBinderIntegerValue
	 (DataBinder binder, String name) throws DataException {
		
		String valStr = binder.getLocal(name);
		
		if (!StringUtils.stringIsBlank(valStr)) {
			try {
				return Integer.parseInt(valStr);
			} catch (NumberFormatException e) {
				String msg = "Expected an integer for '" 
				 + name + "', got value: '" + valStr + "'";
				
				Log.error(msg);
				throw new DataException(msg);
			}
		} else
			return null;
	}
	
	public static Float getBinderFloatValue
	 (DataBinder binder, String name) throws DataException {
		
		String valStr = binder.getLocal(name);
		
		if (!StringUtils.stringIsBlank(valStr)) {
			try {
				return Float.parseFloat(valStr);
			} catch (NumberFormatException e) {
				String msg = "Expected a float for '" 
				 + name + "', got value: '" + valStr + "'";
				
				Log.error(msg);
				throw new DataException(msg);
			}
		} else
			return null;
	}

	
	public static BigDecimal getBinderBigDecimalValue
	 (DataBinder binder, String name) throws DataException {
		
		String valStr = binder.getLocal(name);
		
		if (!StringUtils.stringIsBlank(valStr)) {
			try {
				return new BigDecimal(valStr);
			} catch (NumberFormatException e) {
				String msg = "Expected a bigdecimal for '" 
				 + name + "', got value: '" + valStr + "'";
				
				Log.error(msg);
				throw new DataException(msg);
			}
		} else
			return null;
	}
	
	
	public static String getBinderStringValue(DataBinder binder, String name) {
		
		String value = binder.getLocal(name);
		
		if (StringUtils.stringIsBlank(value))
			return null;
		else
			return value;
	}
	
	/** Creates an Float instance using the named value
	 *  from the current row of the passed ResultSet.
	 *  
	 * @param rs
	 * @param name
	 * @return
	 * @throws ServiceException if the ResultSet value is non-empty,
	 * 							but not a valid float
	 */
	public static Float getResultSetFloatValue
	 (DataResultSet rs, String name) throws DataException {
		
		String valueStr = rs.getStringValueByName(name);
		Float value   = null;
		
		if (!StringUtils.stringIsBlank(valueStr)) {
			try {
				value = new Float(valueStr);
			} catch (NumberFormatException e) {
				String msg = "Expected a float for '" 
				 + name + "', got value: '" + valueStr + "'";
				
				Log.error(msg);
				throw new DataException(msg);
			}
		}
		
		return value;
	}
	
	/** Creates an Float instance using the named value
	 *  from the current row of the passed ResultSet.
	 *  
	 * @param rs
	 * @param name
	 * @return
	 * @throws ServiceException if the ResultSet value is non-empty,
	 * 							but not a valid float
	 */
	public static Double getResultSetDoubleValue
	 (DataResultSet rs, String name) throws DataException {
		
		String valueStr = rs.getStringValueByName(name);
		Double value   = null;
		
		if (!StringUtils.stringIsBlank(valueStr)) {
			try {
				value = new Double(valueStr);
			} catch (NumberFormatException e) {
				String msg = "Expected a double for '" 
				 + name + "', got value: '" + valueStr + "'";
				
				Log.error(msg);
				throw new DataException(msg);
			}
		}
		
		return value;
	}
	
	/** Returns a boolean using the named value from the current row 
	 *  of the passed ResultSet. If the ResultSet value is '1', returns
	 *  true, otherwise returns false.
	 *  
	 * @param rs
	 * @param name
	 * @return
	 */
	public static boolean getResultSetBoolValue
	 (DataResultSet rs, String name) {
		
		String valueStr = rs.getStringValueByName(name);
		return (!StringUtils.stringIsBlank(valueStr) 
				&& 
				!valueStr.equals("0")
				&&
				!valueStr.equals("N"));
	}
	
	/** Returns a Boolean using the named value from the current row 
	 *  of the passed ResultSet. 
	 *  
	 *  If the value is empty/missing, return null.
	 *  
	 *  If the ResultSet value is '1', returns true, else return false.
	 *  
	 * @param rs
	 * @param name
	 * @return
	 */
	public static Boolean getResultSetBoolValueAllowNull
	 (DataResultSet rs, String name) {
		
		String valueStr = rs.getStringValueByName(name);
		
		if (StringUtils.stringIsBlank(valueStr))
			return null;
		else
			return (!valueStr.equals("0") &&
					!valueStr.equals("N"));
	}
	
	/** Returns a boolean using the named value from the current row 
	 *  of the passed ResultSet. If the ResultSet value is non-empty 
	 *  and not '0', returns true, else false.
	 *  
	 * @param rs
	 * @param name
	 * @return
	 */
	public static boolean getBinderBoolValue
	 (DataBinder binder, String name) {
		
		String valueStr = binder.getLocal(name);
		return (!StringUtils.stringIsBlank(valueStr) && !valueStr.equals("0"));
	}
	
	/** Returns a boolean using the named value from the current row 
	 *  of the passed ResultSet. If the ResultSet value is '1', returns
	 *  true, otherwise returns false.
	 *  
	 * @param rs
	 * @param name
	 * @return
	 */
	public static Boolean getBinderBoolValueAllowNull
	 (DataBinder binder, String name) {
		
		String valueStr = binder.getLocal(name);
		
		if (StringUtils.stringIsBlank(valueStr))
			return null;
		else
			return !valueStr.equals("0");
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
		 ("qClientServices_GetNext" + idLabel + "Id", new DataBinder());
		
		String nextVal = rsNextVal.getStringValue(0);
		return nextVal;
	}
	
	/** Creates a new Facade, using a new Workspace instance generated from the 
	 *  default SystemDatabase provider.
	 *  
	 * @return
	 * @throws DataException
	 */
	public static FWFacade getFacade() throws DataException {
		return getFacade(false);
	}
	
	public static FWFacade getFacade(boolean usePooling) throws DataException {
		Provider provider = Providers.getProvider("SystemDatabase");
		
		if (provider != null) {
			Workspace ws = (Workspace)provider.getProvider();
			return getFacade(ws, usePooling);
		} else {
			throw new DataException("Unable to create provider for SystemDatabase");
		}
	}

	public static FWFacade getFacade(String poolId) throws DataException {
		Provider provider = Providers.getProvider("SystemDatabase");
		
		if (provider != null) {
			Workspace ws = (Workspace)provider.getProvider();
			if (StringUtils.stringIsBlank(poolId)) {
				return getFacade(ws);				
			} else {
				return getFacade(ws, poolId);
			}
		} else {
			throw new DataException("Unable to create provider for SystemDatabase");
		}
	}	
	
	
	/** Creates a new FWFacade using the passed Workspace instance.
	 * 
	 * @param workspace
	 * @return
	 * @throws DataException
	 */
	public static FWFacade getFacade(Workspace workspace) throws DataException {
		try {
			FWFacade facade = new FWFacade(workspace);
			return facade;
		} catch (Exception e) {
			Log.error("Unable to instantiate FWFacade", e);
			throw new DataException("Unable to instantiate FWFacade: " + e, e);
		}
	}
	
	private static boolean firstRun = false;
	
	public static FWFacade getFacade(Workspace workspace, boolean usePooling) 
	 throws DataException {
		try {
			FWFacade facade;
			
			if (usePooling) {// use custom Connection pooling
				
				if (firstRun) {
					// These settings are typically configured in JNDI
				    // so they a implementation specific
				    OracleDataSource ds = new OracleDataSource();
				   // ds.setDriverType("thin");
				   // ds.setServerName("localhost");
				   // ds.setPortNumber(1521);
				    
				    ds.setURL(SharedObjects.getEnvironmentValue
						     ("ECS_CONN_POOL_STRING_CCLA_POOL1"));
				   // ds.setName("orcldbucm11g"); // sid
				    ds.setUser(SharedObjects.getEnvironmentValue
						     ("ECS_CONN_POOL_USER_CCLA_POOL1"));
				    ds.setPassword(SharedObjects.getEnvironmentValue
						     ("ECS_CONN_POOL_PWD_CCLA_POOL1"));
	
				    ds.setConnectionCachingEnabled(true);
				    
					OracleConnectionCacheManager connMgr = OracleConnectionCacheManager
					 .getConnectionCacheManagerInstance();
				    
				    connMgr.createCache("CCLA_POOL1", ds, new Properties());
				    
				    Connection conn = ds.getConnection();
				    
				    CallableStatement st = conn.prepareCall("SELECT * FROM REF_ELEMENT_TYPE");
				    Log.debug(Boolean.toString(st.execute()));  
				    
				    firstRun = false;
				}
			    
				facade = new FWFacade(workspace, getDBConnectionPoolId()); 	
			}
			else 			// use standard DB connector and transaction mechanism
				facade = new FWFacade(workspace); 	
				
			
				//facade.beginTransaction();
			return facade;
		} catch (Exception e) {
			String msg = "Unable to instantiate " + (usePooling ? "pooled " : " ")  + 
			 "FWFacade";
			
			Log.error(msg, e);
			throw new DataException(msg, e);
		}
	}
	
	/**
	 * Gets a facade for a specific poolId
	 * @param workspace
	 * @param poolId
	 * @return
	 * @throws DataException
	 */
	public static FWFacade getFacade(Workspace workspace, String poolId) throws DataException {
		try {
			FWFacade facade;
			
			if (!StringUtils.stringIsBlank(poolId)) 
				facade = new FWFacade(workspace, poolId); 	
			else 			// use standard DB connector and transaction mechanism
				facade = new FWFacade(workspace); 	
			
			return facade;
		} catch (Exception e) {
			String msg = "Unable to instantiate " + (!StringUtils.stringIsBlank(poolId) ? poolId : " ")  + 
				"FWFacade";
			Log.error(msg, e);
			throw new DataException(msg, e);
		}
	}
	
	/** Fetches an available connection pool ID.
	 * 
	 * @return
	 */
	public static String getDBConnectionPoolId() {
		return DB_CONNECTION_POOL_IDS.get(0);
	}
	
	public static void addMappingToBinder
	 (Hashtable<String, String> map, DataBinder binder) {
		
		Iterator<Map.Entry <String, String>> i = map.entrySet().iterator();
		
		while (i.hasNext()) {
			Map.Entry <String, String> entry = i.next();
			binder.putLocal(entry.getKey(), entry.getValue());
		}
		
		Log.debug("Added " + map.size() + " field-value pairs to binder");
	}
	
	public static DataResultSet getResultSetFromMap
	 (Hashtable<String, String> map) {
		
		DataResultSet rs = new DataResultSet(new String[] {"NAME", "VALUE"});
		Iterator<Map.Entry<String, String>> i = map.entrySet().iterator();
		
		while (i.hasNext()) {
			Map.Entry<String, String> entry = i.next();
			
			Vector v = new Vector();
			v.add(entry.getKey());
			v.add(entry.getValue());
			
			rs.addRow(v);
		}
		
		return rs;
	}
	
	/** Takes the given ResultSet and removes all occurences of
	 *  Strings in the chars array from the list of specified fields.
	 *  
	 *  If the fields list is null, the chars are removed from every
	 *  field/column in the ResultSet (TODO)
	 *  
	 * @param rs
	 * @param chars
	 * @param fields
	 * @return
	 * @throws DataException 
	 */
	public static void removeResultSetValueChars
	 (DataResultSet rs, String[] chars, Vector<String> fields) 
	 throws DataException {
		
		if (rs.isEmpty())
			return;
		
		// Create the regexp. This is all the strings in the passed chars array,
		// delimited by the pipe character.
		String charPatternStr = "";
		
		for (int i=0; i<chars.length; i++) {
			if (charPatternStr.length() > 0)
				charPatternStr += "|";
			
			charPatternStr += chars[i];
		}
		
		Pattern charPattern = Pattern.compile(charPatternStr);
		
		HashMap<String, Integer> fieldIndexes = null;
		
		if (fields != null) {
			fieldIndexes = new HashMap<String, Integer>();
			
			// Work out column index positions for all fields in the list.
			for (String thisField:fields) {
				for (int i=0; i<rs.getNumFields(); i++) {
					String thisColumn = rs.getFieldName(i);
					
					if (thisColumn.equals(thisField))
						fieldIndexes.put(thisField, i);
				}
			}
		}
		
		rs.first();
		
		// Now remove characters from the ResultSet values, one row at a time.
		do {
			if (fields != null) {
				for (String thisField:fieldIndexes.keySet()) {
					
					String thisValue = rs.getStringValueByName(thisField);
					
					if (!StringUtils.stringIsBlank(thisValue)) {
						// Execute the regular expression which removes all
						// character occurrences
						thisValue = charPattern.matcher(thisValue).replaceAll("");
	
						// Update the ResultSet value.
						rs.setCurrentValue(fieldIndexes.get(thisField), thisValue);
					}
				}
				
				
			}
		} while (rs.next());
		
		rs.first();
	}
	
	/** Ensures all special characters in the given string are escaped
	 *  before being passed as String values to an Oracle query.
	 *  
	 * @param string
	 * @return
	 */
	public static String escapeForOracle(String string) {

		Pattern charPattern = Pattern.compile("'");
		
		String escapedString = 
		 charPattern.matcher(string).replaceAll("'$0");
		
		System.out.println(escapedString);
		
		return escapedString;
	}
	
	/** Shaves off occurrences of the passed character from the start of
	 *  the passed String. This is useful for trimming off leading zeroes
	 *  from account numbers/sort codes, for instance.
	 *  
	 *  It is recommended that the trim() function is executed on the
	 *  passed String beforehand.
	 *  
	 * @param string
	 * @param trimChar
	 * @return the trimmed String
	 */
	public static String trimLeadingChars(String string, char trimChar) {
		int startingIndex = 0;
		
		while (startingIndex<string.length() 
				 && string.charAt(startingIndex) == trimChar) {
			startingIndex++;
		}

		return string.substring(startingIndex, string.length());
	}
	
	/** Appends the given appendValue String to the binder parameter
	 *  specified by the name String.
	 *  
	 *  This is useful for appending new IDs etc. to the RedirectUrl
	 *  parameter, for instance.
	 *  
	 *  If the value isn't present in the Binder to start with, no
	 *  changes are applied.
	 *  
	 * @param name
	 * @param appendValue
	 * @return true, if the value was present in the binder and got
	 * 		   appended.	
	 */
	public static boolean appendToBinderParam
	 (DataBinder binder, String name, String appendValue) {
		
		String value = binder.getLocal(name);
		
		if (StringUtils.stringIsBlank(value))
			return false;
		else {
			binder.putLocal(name, value + appendValue);
			return true;
		}
	}
	
	/** Takes a comma-separated String of integers and returns them in
	 *  a Vector format, cast to Integers.
	 *  
	 *  If the passed String is blank/null an empty Vector is returned.
	 *  
	 * @param integerList
	 * @throws NumberFormatException if any of the list items don't
	 * 		   cast to Integers, i.e. non-numeric.
	 * @return
	 */
	public static Vector<Integer> getIntegerList(String integerList)
	 throws NumberFormatException {
		
		Vector<Integer> list = new Vector<Integer>();
		
		if (!StringUtils.stringIsBlank(integerList)) {
			String[] integers = integerList.split(",");
			
			for (String i : integers) {
				if (!StringUtils.stringIsBlank(i)) {
					Integer thisInt = new Integer(i);
					list.add(thisInt);
				}
			}
		}
		
		return list;
	}
	
	/** Utility method for adding a new Activity entry, mapped to
	 *  the supplied Process/Person/Entity.
	 *  
	 *  If a non-null message String is passed, a Note entry will
	 *  also be created and tagged to the new Activity.
	 * 
	 *  WARNING: the record creation methods are not performed within
	 *  a transaction block. It is strongly recommended that the
	 *  calling code is executed inside a transaction, if you are
	 *  passing a non-null message String (as this will do 2 SQL
	 *  inserts)
	 * 
	 * @param processId
	 * @param personId
	 * @param interactionId
	 * @param entityId
	 * @param userId
	 * @param type
	 * @param action
	 * @param note
	 * @param isError
	 * @param facade
	 *  
	 * @throws ServiceException 
	 * @throws DataException 
	 */
	public static Activity addActivity
	 (Integer processId, Integer personId, Integer interactionId, 
	  Integer entityId, String userId, String type, String action, 
	  String message, boolean isError, FWFacade facade) 
		throws DataException {
		
		Integer noteId = null;
			
		if (!StringUtils.stringIsBlank(message)) {
			// Note message was passed - add a new Note entry.
			Note note = Note.add(message, userId, facade);
			noteId = note.getNoteId();
		}
		
		Activity activity = Activity.add(processId, personId, 
			interactionId, entityId,userId, type, action, noteId, 
			isError, facade);

		return activity;
	}
	
	
	
	public static String capitaliseName(String phrase)
	{
		if (StringUtils.stringIsBlank(phrase))
			return phrase;
		
		if (Person.DO_CAPITALISATION)
		{
			phrase = WordUtils.capitalizeFully(phrase);
			phrase = WordUtils.capitalize(phrase, new char[]{'\''});
			phrase = WordUtils.capitalize(phrase, new char[]{'-'});
			phrase = WordUtils.capitalize(phrase, new char[]{'.'});
			
			// search for "Mc" and capitalize next letter
			int index = phrase.indexOf("Mc");
			if (index>=0)
			{
			    char[] chars = phrase.toCharArray();
	
			    for (int i = 0; i + 1 < chars.length; i++)
			    {
			        if (i == index+2)
			        {                    
			            chars[i] = Character.toUpperCase(chars[i]);
			        }
			    }
			    phrase =  new String(chars);
		
			}
		}
		return phrase;
	}
	
	/** Returns the string value of the given column in the ResultSet.
	 *  
	 *  If the ResultSet column value is an empty string, the method returns null.
	 *  
	 * @param rs
	 * @param name
	 * @return
	 */
	public static String getResultSetStringValue
	 (DataResultSet rs, String name) {
		String s = rs.getStringValueByName(name);
		
		if (StringUtils.stringIsBlank(s))
			return null;
		else 
			return s;
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
				String msg = "Expected an bigdecimal for '" 
				 + name + "', got value: '" + valueStr + "'";
				
				Log.error(msg);
				throw new DataException(msg);
			}
		}
		
		return value;
	}
	
	/** Returns a truncated version of the passed web URL string, i.e. with the
	 *  http:// removed if applicable.
	 *  
	 *  A null input simply returns null back.
	 *  
	 * @param url
	 * @return
	 */
	public static String truncateWebUrl(String url) {
		if (url == null)
			return null;
		
		return StringUtils.replaceInString(url, "http://", "");
	}
	
	/** Checks if the passed string is a valid Person/Org Account Code.
	 *  
	 *  Must be 4 capital alpha, 8 numeric characters.
	 * 
	 *  If the throwError flag is set, a DataException is thrown if the code doesn't
	 *  match the regexp.
	 * 
	 * @return
	 * @throws DataException 
	 */
	public static boolean isAccountCodeValid(String accountCode, boolean throwError) 
	 throws DataException {
		
		boolean isValid = ACCOUNT_CODE_PATTERN.matcher(accountCode).matches();
	
		if (isValid)
			return true;
		else {
			if (throwError)
				throw new DataException
				 ("Account Code '" + accountCode + "' is invalid");
			else return false;
		}
	}
	
	/** For a given Person/Org Account Code, e.g. ABCD00001234, returns the numeric
	 *  suffix as an integer.
	 *  
	 *  In the previous example this would return 1234.
	 *  
	 * @return
	 * @throws DataException 
	 */
	public static int getAccountCodeNumberSuffix(String accountCode) 
	 throws DataException {
		
		// Check code validity first.
		isAccountCodeValid(accountCode, true);
			
		String numberStr = accountCode.substring(4, 12);
		return Integer.parseInt(numberStr);
	}
	
	/** Checks the two strings for equality, using the stringIsBlank function.
	 *  
	 *  This means that empty strings are considered equal to null strings.
	 * 
	 * @param s1
	 * @param s2
	 * @return true if both Strings are null/empty or matching, false otherwise
	 */
	public static boolean stringEquals(String s1, String s2) {
		
		return 	(StringUtils.stringIsBlank(s1) && StringUtils.stringIsBlank(s2))
				||
				(s1 != null && s2 != null && s1.equals(s2));
	}
	
	/** Removes any space characters for the passed string. Useful when displaying
	 *  telephone/fax numbers on forms.
	 *  
	 * @param s
	 * @return
	 */
	public static String stripSpaceCharacters(String s) {
		return StringUtils.replaceInString(s, " ", "");
	}

	public static DataResultSet getUCMDocMeta(int docId, FWFacade ucmFacade) 
	 throws DataException {
	
		DataBinder binder = new DataBinder();
		addQueryIntParamToBinder(binder, "DOC_ID", docId);
		
		return ucmFacade.createResultSet("qCommProc_GetUCMDocMeta", binder);
	}

	/** Pads the given client number string with the appropriate number of leading
	 *  zeros.
	 *  
	 * @param string
	 * @param company
	 * @return
	 */
	public static String padClientNumber(String clientNumber, Company company) {
		return CCLAUtils.padString(clientNumber, '0', company.getClientNumberPadding());
	}
	
	/** Returns the correct padded version of the given account number/fund code, with 
	 *  the appropriate number of leading zeros.
	 *  
	 * @param string
	 * @param company
	 * @return
	 */
	public static String getPaddedAccountNumber
	 (int accountNumber, Fund fund) {
		return CCLAUtils.padString
		 (Integer.toString(accountNumber), '0', 
		 fund.getCompany().getAccountNumberPadding()) + fund.getFundCode();
	}
	
	/** Used to convert decimal Strings to decimal format. Will successfully convert
	 *  any string and return it in an 0.00 format, providing that the
	 *  string can be initially parsed as a double type.
	 *  
	 *  If the passed String s is null/blank, the method returns null.
	 *  If the passed String cannot be parsed/formatted, the method returns null.
	 *  
	 * @param s
	 * @return
	 * @throws ServiceException 
	 */
	public static String convertToDecimal(String s) throws ServiceException {
		
		String decimalStr = null;
		
		if (StringUtils.stringIsBlank(s))
			return null;
		
		try {
			double d = Double.parseDouble(s);

			decimalStr = DECIMAL_FORMAT.format(d);
			//Log.debug("Converted string to decimal: '" + s + "' -> " + decimalStr);
			
		} catch (NumberFormatException e) {
			String msg = "Unable to convert string to decimal, " +
			 "not a valid decimal [" + s + "] "; 
			
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
		
		return decimalStr;
	}
	
	/** Converts the passed ResultSet into a CSV file.
	 *  
	 *  If columnLabelMap is non-null, it is used to choose which column names in the 
	 *  ResultSet are selected for display, and the label they are given in the CSV
	 *  output. It also dictates the order that columns are displayed in.
	 *  
	 * @param rs
	 * @return
	 */
	public static StringBuffer convertToCSV
	 (DataResultSet rs, LinkedHashMap<String, String> columnLabelMap) {
		
		StringBuffer sb = new StringBuffer();
		
		// Used to build an ordered mapping of selected ResultSet column indexes and
		// their output labels.
		LinkedHashMap<Integer, String> selectedColumns = 
		 new LinkedHashMap<Integer, String>();

		if (columnLabelMap == null) {
			// Add all columns as selected, with their original names
			for (int i=0; i<rs.getNumFields(); i++) {
				selectedColumns.put(i, rs.getFieldName(i));
			}
			
		} else {
			
			for (String columnName : columnLabelMap.keySet()) {
				for (int i=0; i<rs.getNumFields(); i++) {
					if (rs.getFieldName(i).equals(columnName)) {
						// Found a matching column in the ResultSet.
						String columnLabel = columnLabelMap.get(columnName);
						
						// Add mapping for column index and custom label
						selectedColumns.put(i, columnLabel);
					}	
				}
			}
		}
		
		// Write column names first.
		int colCounter = 0;
		
		for (Integer selCol : selectedColumns.keySet()) {
			if (colCounter > 0)
					sb.append(",");
				
			sb.append(selectedColumns.get(selCol));
		
			colCounter++;
		}

		sb.append("\n");
		
		// Now output data for the selected columns from each row in turn
		if (rs.first()) {
			do {
				colCounter = 0;
				
				for (Integer selCol : selectedColumns.keySet()) {
					if (colCounter>0)
						sb.append(",");
					
					sb.append("\"" + rs.getStringValue(selCol) + "\"");
					colCounter++;
				}
				
				sb.append("\n");
			} while (rs.next());
		}
		
		return sb;
	}
	
	/** Formats Active Directory userNames so it will match to an entry in the 
	 *  UCMADMIN.USERS table.
 	 *  
 	 *  If an NT domain is detected in the name (i.e. it contains a \ character), and 
 	 *  the remove domain flag is set, the domain will be stripped off.
 	 *  
	 *  Requires changing the username portion to lowercase, if the userName string has
	 *  a backslash character in it.
	 *  
	 *  So CCLA\adminTM becomes 'CCLA\admintm'
	 *  
	 *  User names without the backslash character are returned unaffected.
	 */  
	public static String normalizeUserName(String userName) {
		
		String formattedName 	= new String(userName);
		int slashPosition 		= userName.indexOf("\\");
		
		if (slashPosition > -1) {
			Log.debug("Fixing UserName case. Original name string: " + userName);
			
			formattedName =
			 userName.substring(0, slashPosition+1) 
			 +
			 (userName.substring(slashPosition+1).toLowerCase());
			
			if (STRIP_NT_DOMAIN_FROM_USERNAMES) {
				Log.debug("Stripping NT domain from UserName");
				formattedName = formattedName.substring(slashPosition+1);
			}
				
			Log.debug("Formatted UserName to: " + formattedName);
		}
		
		return formattedName;
	}
	
	/** Returns the dDocName where the content resides for the given dDocName. 
	 *  
	 *  If the passed dDocName belongs to a content item of type 'Document', the
	 *  passed dDocName is returned as-is.
	 *  
	 *  If the passed dDocName belongs to a content item of type 'ChildDocument', the
	 *  xPdfDocName value is returned, if it exists. If no xPdfDocName value is found,
	 *  the xParentDocName value is inspected in a similar way to the original dDocName,
	 *  until we find a content item of type 'Document', or an xPdfDocName value is
	 *  found.
	 *  
	 * @param docName
	 * @return
	 * @throws ServiceException 
	 */
	public static String getContentDocName(String docName) throws ServiceException {

		int MAX_LOOKUPS = 3;
		
		try {
			String thisDocName = new String(docName);
			String contentDocName = null;
			
			int lookups = 0;
			
			// Loop up via ParentDocNames until we find the content dDocName.
			while (contentDocName == null) {
				LWDocument lwDoc = new LWDocument(thisDocName, true);
				
				String docType = lwDoc.getAttribute(UCMFieldNames.DocType);
				
				if (docType.equals(UCMDocTypes.ChildDocument)) {
					String pdfDocName = lwDoc.getAttribute(UCMFieldNames.PdfDocName);
					
					if (!StringUtils.stringIsBlank(pdfDocName)) {
						// We found a xPdfDocName value - we're done.
						contentDocName = pdfDocName;
					} else {
						// Need to go up one level and check again.
						thisDocName = lwDoc.getAttribute(UCMFieldNames.ParentDocName);
					}
				} else {
					contentDocName = thisDocName;
				}
				
				lookups++;
				
				// Loop limiter
				if (lookups > MAX_LOOKUPS) {
					throw new Exception("Too many parent document lookups");
				}
			}
			
			return contentDocName;
			
		} catch (Exception e) {
			String msg = "Failed to resolve document reference: " 
			 + e.getMessage();
			
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}
	
	/** Returns the LWDocument instance associated with an Instruction's parent Comm's
	 *  document reference.
	 *  
	 *  If the parent Comm doesn't have a document reference, an error is thrown.
	 *  
	 * @param instructionId
	 * @param facade
	 * @return
	 * @throws DataException
	 * @throws ServiceException
	 */
	public static LWDocument getInstructionContentDocument
	 (int instructionId, FWFacade facade) throws DataException, ServiceException {
		
		Instruction instr = Instruction.get(instructionId, facade);
		Comm comm = Comm.get(instr.getCommId(), facade);
		
		String docGuid = comm.getDocGuid();
		
		if (docGuid != null) {
			try {
				return getLatestLwdFromDocGuid(docGuid);

			} catch (Exception e) {
				throw new ServiceException(e);
			}
		} else {
			throw new ServiceException
			 ("Unable to resolve Instruction content document reference: " +
			  "missing value");
		}
	}

	/** Generates a string of comma-separated Integers from the passed list of Integers.
	 *  
	 *  Useful when building values for an SQL query IN() clause.
	 *  
	 * @param elementIds
	 * @return
	 */
	public static String getCSVFromIntegerList(Vector<Integer> integerList) {

		StringBuffer csv = new StringBuffer();
		
		for (Integer integer : integerList) {
			if (csv.length() > 0)
				csv.append(",");
		
			csv.append(integer);
		}
		
		return csv.toString();
	}
	
	/** Builds a comma-separated String by concatenating all non-empty/null Strings in 
	 *  the passed list.
	 *  
	 * @param strings
	 * @param includeSpaceDelimiter	Whether or not spaces are included after each comma.
	 * @return
	 */
	public static String getCSVFromStringList
	 (Vector<String> strings, boolean includeSpaceDelimiter) {
		return getCSVFromStringList(strings, includeSpaceDelimiter, false);
	}
	
	/** Builds a comma-separated String by concatenating all non-empty/null Strings in 
	 *  the passed list.
	 *  
	 * @param strings
	 * @param includeSpaceDelimiter	Whether or not spaces are included after each comma.
	 * @return
	 */
	public static String getCSVFromStringList(Vector<String> strings,
	 boolean includeSpaceDelimiter, boolean includeQuotes) {
		StringBuffer sb = new StringBuffer();
		
		for (String s : strings) {
			if (s == null || s.length() == 0)
				continue; // discard null strings
				
			if (sb.length() > 0)
				sb.append("," + (includeSpaceDelimiter ? " " : ""));
			
			String strToAppend = s.trim();
			
			if (includeQuotes)
				sb.append("'").append(strToAppend).append("'");
			else
				sb.append(strToAppend);
		}
		
		return sb.toString();
	}
	
	/** Extracts a list of trimmed strings from the comma-separated list.
	 *  
	 *  Zero-length strings won't be added to the list.
	 *  
	 *  So a String like 'abc, , ab,,d,'
	 *  Would yield a list ['abc','ab','d']
	 *  
	 * @param stringCSV
	 * @return
	 */
	public static Vector<String> getStringListFromCSV(String stringCSV) {
		Vector<String> strings = new Vector<String>();
		String[] splitString = stringCSV.split(",");
		
		for (String s : splitString) {
			s = s.trim();
			
			if (s.length() > 0)
				strings.add(s);
		}
		
		return strings;
	}

	//************* DOC_GUID UTIL METHODS ********************//
	
	
	/**
	 * Generate a DOC_GUID from the given LWD. 
	 * DOC_GUID is constructed from the dDocName + ":" + dRevision, i.e: CCLA_1234:2
	 * 
	 * @param lwd
	 * @return
	 * @throws Exception
	 */
	public static String getDocGuidFromLwd(LWDocument lwd) throws Exception{
		
		String revIdStr = null;
		
		try {
			revIdStr = lwd.getAttribute("dRevisionId");
		} catch (Exception e) {
			throw new ServiceException("Unable to generate docGuid: " +
			 "can't fetch dRevisionId value", e);
		}
			
		if (StringUtils.stringIsBlank(revIdStr))
			throw new ServiceException("Unable to generate docGuid as revId is empty");
		
		Integer revId = Integer.parseInt(revIdStr);
		String docGuid = getDocGuidFromRawValues(lwd.getDocName(),revId);
		return docGuid;
	}
	
	/**
	 * Takes a dId and generates a DocGuid value
	 * @param dId
	 * @return
	 * @throws Exception
	 */
	public static String getDocGuidFromDid(int dId) throws DataException {
		//TODO: refactor to use a dedicated query to improve efficiency
		try {
			LWDocument lwd = new LWDocument(dId, true);
			return getDocGuidFromLwd(lwd);
		} catch (Exception e) {
			String msg = "Unable to instantiate LWDocument for dID " + dId;
			
			Log.error(msg, e);
			throw new DataException(msg, e);
		}
	}
	
	/**
	 * Takes the raw values for dDocName and dId and creates the DocGuid value
	 * @param dDocName
	 * @param revId
	 * @return
	 */
	public static String getDocGuidFromRawValues(String dDocName, Integer revId){
		String docGuid = dDocName + ":" + revId;
		Log.debug("Returning DOC_GUID: "+docGuid);
		return docGuid;
	}
	
	/** Returns a DocGuid value containing the latest Revision ID for the given 
	 *  dDocName.
	 * @param dDocName
	 * @return
	 * @throws DataException 
	 */
	public static String getLatestDocGuidFromDocName(String dDocName)
	 throws DataException {
		
		try {
			LWDocument lwd = new LWDocument(dDocName, true);

			Integer revId = Integer.parseInt
			 (lwd.getAttribute(UCMFieldNames.RevisionId));
			
			String docGUID = dDocName + ":" + revId;
			
			Log.debug("Returning DOC_GUID: " + docGUID);
			return docGUID;
			
		} catch (Exception e) {
			throw new DataException("Failed to construct latest Doc GUID from " +
			 "dDocName: " + dDocName + ", " + e.getMessage(), e);
		}
	}
	
	/**
	 * Takes a docGuid, extracts the docName and returns the latest version as a 
	 * LWDocument object
	 * @param docGuid
	 * @return
	 * @throws Exception
	 */
	public static LWDocument getLatestLwdFromDocGuid(String docGuid) throws Exception{	
		if (StringUtils.stringIsBlank(docGuid))
			throw new ServiceException("getLatestLwdFromDocGuid: docGuid is empty");
	
		String docName = docGuid.substring(0, docGuid.indexOf(":"));
		return new LWDocument(docName,true);
	}
	
	
	//TODO: finish method to obtain lwd with specific revision
	public static LWDocument getLwdByRevisionFromDocGuid(String docGuid) throws Exception{
	
		if (StringUtils.stringIsBlank(docGuid))
			throw new ServiceException("getLatestLwdFromDocGuid: docGuid is empty");
			
		String docName = docGuid.substring(0, docGuid.indexOf(":"));
		String revId = docGuid.substring(docGuid.indexOf(":"), docGuid.length());
		
		//TODO get by revision id. 
		//Not critical at this stage but need to re-factor query: QitemDocInfoDocname
		//and LWDocuument and ContentItem in ECS_BaseUtils
		LWDocument lwDoc = new LWDocument(docName, true); 
		return lwDoc;
		
		//throw new ServiceException("METHOD NOT IMPLEMENTED");
		//GET LWD FROM DOCNAME AND REVISION
	}
	
	/** Determines whether 2 BigDecimals match. Copes with either/both being null.
	 *
	 * @param bd1
	 * @param bd2
	 * @return
	 */
	public static boolean bigDecimalsMatch(BigDecimal bd1, BigDecimal bd2) {
		if (bd1 == null && bd2 == null)
			return true;
		else if (bd1 == null || bd2 == null)
			return false;
		else
			return (bd1.compareTo(bd2) == 0);
	}
	
	/** Determines whether 2 Integers match. Copes with either/both being null.
	 *  
	 * @param i1
	 * @param i2
	 * @return
	 */
	public static boolean integersMatch(Integer i1, Integer i2) {
		if (i1 == null && i2 == null)
			return true;
		else if (i1 == null || i2 == null)
			return false;
		else
			return (i1.compareTo(i2) == 0);
	}
	
	/** Determines whether 2 Strings match. Copes with either/both being null.
	 *  
	 * @param i1
	 * @param i2
	 * @return
	 */
	public static boolean stringsMatch(String s1, String s2) {
		if (s1 == null && s2 == null)
			return true;
		else if (s1 == null || s2 == null)
			return false;
		else
			return (s1.equals(s2));
	}
	
	/**
	 * Truncates a string to the max length if i it is larger than the max length.
	 * This method will trim the initial string before comparing.  
	 * 
	 * @param s1
	 * @param maxlength
	 * @return
	 */
	public static String truncateString(String s1, int maxlength) {
		if (s1==null) {
			return null;
		} else {
			s1 = s1.trim();
			if (s1.length()>maxlength)
				return s1.substring(0, maxlength);
			else
				return s1;
		}
	}
	
	/** Returns the latest date from the set, or null if the set is empty.
	 * 
	 * @param dates
	 * @return
	 */
	public static Date getLatestDate(Vector<Date> dates) {
		Date latestDate = null;
		
		for (Date date : dates) {
			if (latestDate == null || (date.after(latestDate)))
				latestDate = date;
		}
		
		return latestDate;
	}
}
