package com.ecs.ucm.ccla.transactions.utils;


	import intradoc.common.*;
	import intradoc.conversion.*;
	import intradoc.data.*;
	import intradoc.shared.*;
	import intradoc.server.*;
	import intradoc.server.script.*;

	import java.util.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;


	/**
	 * This class demonstrates how to create custom IdocScript functions. These
	 * include variable names that should be evaluated, variables that are either 
	 * true or false, as well as new kinds of functions.
	 */
	public class TransactionIdocScriptFunctions extends ScriptExtensionsAdaptor
	{
		public TransactionIdocScriptFunctions()
		{
			// this is a list of all the custom variable names that can be evaluated
			m_variableTable = new String[] {
				// these variables should be evaluated, and a string should be returned
				"UppercaseUserName", "DayOfWeek",

				// these variables should be evaluated as true or false
				"TodaysDateIsEven", "HasAdminRole"
			};

			// this is the definition table for the variables.  The first integer is
			// an id used in the switch statement in evaluateVariable(...) below, the second
			// integer simply shows that the variable should be evaluated as a boolean
			m_variableDefinitionTable = new int[][]
			{
				{0, 0}, // UppercaseUserName
				{1, 0}, // DayOfWeek
				{2, 1}, // TodaysDateIsEven
				{3, 1}  // HasAdminRole		
			};

			// this is a list of the functions that can be called with the custom code
			m_functionTable = new String[] {"uuShaHash", "factorial", "log", "strMin", "formatCurrency", "formatNumber", "roundNumber"};

			// Configuration data for functions.  This list must align with the "m_functionTable"
			// list.  In order the values are "id number", "Number of arguments", "First argument type",
			// "Second argument type", "Return Type".  Return type has the following possible
			// values: 0 generic object (such as strings) 1 boolean 2 integer 3 double.
			// The value "-1" means the value is unspecified.
			m_functionDefinitionTable = new int[][]
			{
				{0, 1, GrammarElement.STRING_VAL, -1, -1, 0}, // uuShaHash
				{1, 1, GrammarElement.INTEGER_VAL, -1, -1, 2}, // factorial
				{2, 1, GrammarElement.STRING_VAL, -1, -1, -1}, // log
				{3, 2, GrammarElement.STRING_VAL, GrammarElement.STRING_VAL, -1, 0}, // strMin
				{4, 1, GrammarElement.STRING_VAL,-1, -1,  0}, // formatCurrency
				{5, 2, GrammarElement.STRING_VAL,GrammarElement.STRING_VAL, -1, 0}, // formatNumber
				{6, 3, GrammarElement.STRING_VAL,GrammarElement.INTEGER_VAL, GrammarElement.INTEGER_VAL, 0}, // formatNumber
			};
		}


		/**
		 * This is where the custom IdocScript function is evaluated.
		 */
		public boolean evaluateFunction(ScriptInfo info, Object[] args, ExecutionContext context)
			throws ServiceException
		{
			/**
			 * This code below is optimized for speed, not clarity.  Do not modify
			 * the code below when making new IdocScript functions.  It is needed to
			 * prepare the necessary variables for the evaluation and return of the
			 * custom IdocScript functions.  Only customize the switch statement below.
			 */
			int config[] = (int[])info.m_entry;
			String function = info.m_key;
			
			int nargs = args.length - 1;
			int allowedParams = config[1];
			if (allowedParams >= 0 && allowedParams != nargs)
			{
				String msg = LocaleUtils.encodeMessage("csScriptEvalNotEnoughArgs", 
					null, function, ""+allowedParams);
				throw new IllegalArgumentException(msg);
			}
			
			String msg = LocaleUtils.encodeMessage("csScriptMustBeInService", 
				null, function, "Service");
			Service service = ScriptExtensionUtils.getService(context, msg);
			DataBinder binder = service.getBinder();
			
			UserData userData = (UserData)context.getCachedObject("UserData");
			if (userData == null)
			{
				msg = LocaleUtils.encodeMessage("csUserDataNotAvailable", null, function);
				throw new ServiceException(msg);
			}
			
			// Do some initial conversion of arguments.  Choices of what initial conversions to make
			// are based on frequency of usage.  If a function uses nontypical parameters it will
			// have to do its own conversion.
			String sArg1 = null;
			String sArg2 = null;
			String sArg3 = null;
			long lArg1 = 0;
			long lArg2 = 0;
			long lArg3 = 0;
			if (nargs > 0)
			{
				if (config[2] == GrammarElement.STRING_VAL)
				{
					sArg1 = ScriptUtils.getDisplayString(args[0], context);
				}
				else if (config[2] == GrammarElement.INTEGER_VAL)
				{
					lArg1 = ScriptUtils.getLongVal(args[0], context);
				}
					
			}
			if (nargs > 1)
			{
				if (config[3] == GrammarElement.STRING_VAL)
				{
					sArg2 = ScriptUtils.getDisplayString(args[1], context);
				}
				else if (config[3] == GrammarElement.INTEGER_VAL)
				{
					lArg2 = ScriptUtils.getLongVal(args[1], context);
				}
			}
			if (nargs > 2)
			{
				if (config[4] == GrammarElement.STRING_VAL)
				{
					sArg3 = ScriptUtils.getDisplayString(args[2], context);
				}
				else if (config[4] == GrammarElement.INTEGER_VAL)
				{
					lArg3 = ScriptUtils.getLongVal(args[2], context);
				}
			}


			/**
			 * Here is where the custom code should go. The case values coincide
			 * with the "id values" in m_functionDefinitionTable. Perform the
			 * calculations here, and place the result into ONE of the result
			 * variables declared below.  Use 'sArg1' and 'sArg2' for the first and
			 * second String arguments for the function (if they exist).  Likewise use
			 * 'lArg1' and 'lArg2' for the first and second long integer arguments.
			 */
			boolean bResult = false;  // Used for functions that return a boolean.
			int iResult = 0; // Used for functions that return an integer.
			double dResult = 0.0;  // Used for functions that return a double.
			Object oResult = null; // Used for functions that return an object (string).
			switch (config[0])
			{
			case 0:		// uuShaHash

				// this will create a uuencoded version of a sha1 hash of the
				// string value that they pass

				// create a new sha1 object
				Sha1 sha = new Sha1();

				// obtain a buffer of the string argument
				byte dataBuf[] = sArg1.getBytes();

				// compute the sha1 digest
				sha.computeDigest(dataBuf);

				// obtain a uuencoded version of the hash for the final result
				oResult = DataConversion.uuencode(sha.m_digestBits, 0,
											   sha.m_digestBits.length);
				break;

			case 1:		// factorial

				// If the integer is negative, we wish to return an error
				// string, and not an integer.
				if (lArg1 < 0)
				{
					// print the error message to the display
					System.out.println("Cannot perform a factorial " +
						"function on the negative number '" + lArg1 + "'");

					// set the result to an error string
					oResult = "Error";

					// using the code from below, we will compute the return
					// object using the result variables, but will force the
					// function to calculate a string return type by passing a
					// '0' as the first parameter, instead of config[4], which
					// comes from the m_functionDefinitionTable defined above
					args[nargs] = ScriptExtensionUtils.computeReturnObject(
						0, bResult, iResult, dResult, oResult);

					// exit the function here, rather than below
					return true;
				}


				// otherwise, perform a simple factorial algorithm on iResult
				iResult = 1;
				for (int i=1; i <= lArg1; i++)
					iResult = iResult * i;

				break;

			case 2:		// log

				// this function will log a string to the console output so
				// a developer can debug IdocScript.  The console can be viewed
				// from the 'View Server Output' link on the Admin Server. It
				// can also be viewed if the server is started from the console
				// as opposed to being run as an NT Service. This function is
				// handy for debugging IdocScript pages.
				try
				{
					// if the key they pass is '#LocalData', dump everything!
					if (sArg1.equals("#LocalData"))
					{
						String name, value;
						
						// obtain a list of all the name value-pairs in the local data
						java.util.Enumeration e = binder.getLocalData().keys();
						System.out.println("\nLocalData:");
						
						// loop over the list of names, and print out the name-value
						// pairs on a new line. 
						while(e.hasMoreElements())
						{
							name = (String)e.nextElement();
							value = binder.getLocal(name);
							if (value != null && value.length() > 0)
								System.out.println("  " + name + "=" + value);
						}
					}
					else
					{
						// obtain a PageMerger object from the service object
						// obtained above. This PageMerger object can be used
						// anywhere to evaluate IdocScript based on the data
						// in a DataBinder. This PageMerger object was initialized
						// with the data in the service call, and also contains
						// all the values set in IdocScript on the template page
						// for the response.
						PageMerger pm = service.getPageMerger();
						
						// the first string argument (sArg1) is a String containing 
						// IdocScript which can be evaluated.	The PageMerger object
						// evaluates it, and returns some plain text, or even a
						// chunk of html
						String str = pm.evaluateScript(sArg1);
						
						// print this value to the standard output.
						System.out.println(str);
					}
				}
				catch (java.io.IOException ioe)
				{
					ioe.printStackTrace();
				}

				break;

			case 3:		// strMin

				// this will look at the 2 strings passed, and return the one that
				// occurs first in alphabetical order.

				// compare the two string lexicographically, and case insensitively
				int result = sArg1.toLowerCase().compareTo(sArg2.toLowerCase());

				// set the result value to the one that occurs first, or the first
				// entry if they are lexigraphically equivalent
				if (result <= 0)
					oResult = sArg1;
				else
					oResult = sArg2;

				break;

			case 4:		// formatCurrency

				String format = sArg1;
				//com.ecs.utils.Log.debug("format is " + format);
				DecimalFormat twoPlaces = new DecimalFormat("#,###,###,##0.00");
				Double number = Double.valueOf(format);
				if (format.equalsIgnoreCase("0"))
					oResult = "0.00";
				else					
					oResult = twoPlaces.format(number);
				//oResult = "test";

				break;
	

			case 5:		// formatNumber

				String no = sArg1;
				String decPlaces = sArg2;
				//com.ecs.utils.Log.debug("no is " + no);
				//com.ecs.utils.Log.debug("decPlaces is " + decPlaces);
				if (!(com.ecs.utils.StringUtils.stringIsBlank(sArg2)))
				{
				Integer n = Integer.parseInt(sArg2);
				String zero = "0";
			    final StringBuilder sb = new StringBuilder();
			    for(int i = 0; i < n; i++) {
			        sb.append(zero);
			    }
			    DecimalFormat decPlace = new DecimalFormat("#,###,###,##0." + sb.toString());

				Double myNo = Double.valueOf(no);
				if (no.equalsIgnoreCase("0"))
					oResult = "0.00";
				else					
					oResult = decPlace.format(myNo);
				//oResult = "test";
				}
				else
					oResult = sArg1;
				break;				

			case 6:		// roundNumber

				String formatNo = sArg1;
				int decimals = (int) lArg2;
				int roundRule = (int) lArg3; 
			
				//com.ecs.utils.Log.debug("formatNo is " + formatNo);
				//com.ecs.utils.Log.debug("decPlaces is " + decPlaces);
				if (formatNo.equalsIgnoreCase("0"))
					oResult = "0";
				else
				{
				if (!(com.ecs.utils.StringUtils.stringIsBlank(formatNo)))
				{				
				BigDecimal formatNumber = new BigDecimal(formatNo);
				int decimal = 6;
				int round = BigDecimal.ROUND_HALF_UP;

				BigDecimal roundResult = formatNumber.setScale(decimals, roundRule);
				//com.ecs.utils.Log.debug(formatNumber + " rounded to " + roundResult.toString());
				oResult = roundResult.toString();
				}
				else
				{
					oResult = sArg1;
				}
				}

				break;	
				
			default:
				return false;
			}


			/**
			 * Do not alter code below here
			 */
			args[nargs] = ScriptExtensionUtils.computeReturnObject(config[5],
				bResult, iResult, dResult, oResult);

			// Handled function.
			return true;
		}


		/**
		 * This is where the custom IdocScript variable is evaluated.
		 */
		public boolean evaluateValue(ScriptInfo info, boolean[] bVal, String[] sVal,
			ExecutionContext context, boolean isConditional)
			throws ServiceException
		{
			/**
			 * This code, like the beginning block of code in evaluateFunction,
			 * is required for preparing the data for evaluation.  It should not
			 * be altered.   Only customize the switch statement below.
			 */
			int config[] = (int[])info.m_entry;
			String key = info.m_key;
			if ((context instanceof Service) == false)
			{
				// Some variables will evaluate trivially instead of throwing an exception.
				if (config[1] == 1)
				{
					bVal[0] = false;
					sVal[0] = "";
					return true;
				}
				throw new ServiceException("Script variable " + key + " must have be evaluated in " +
					"context of a Service object.");
			}
			Service service = (Service)context;
			DataBinder binder = service.getBinder();
			UserData userData = (UserData)context.getCachedObject("UserData");
			if (userData == null)
			{
				throw new ServiceException("Script variable " + key + " must have user data context.");
			}


			/**
			 * Here is where the custom code should go. The case values coincide
			 * with the "id values" in m_variableDefinitionTable. Perform the
			 * calculations here, and place the result into ONE of the result
			 * variables declared below: either a string, OR a boolean.
			 */
			boolean bResult = false;
			String sResult = null;
			switch (config[0])
			{
			case  0:		// UppercaseUserName

				// obtain the value for dFullName from the current user data
				sResult = userData.getProperty("dFullName");

				// set it to upper case
				sResult = sResult.toUpperCase();

				break;

			case  1:		// DayOfWeek

				// prepare a simple date format object to display the day of week
				SimpleDateFormat dayFrmt = new SimpleDateFormat("E");

				// set the timezone
				dayFrmt.setTimeZone(TimeZone.getDefault());

				// use the format object to display the day of week for this date
				sResult = dayFrmt.format(new Date());

				break;

			case  2:		// TodaysDateIsEven

				// prepare a simple date format object to display the day of month
				SimpleDateFormat dateFrmt = new SimpleDateFormat("d");

				// set the timezone
				dateFrmt.setTimeZone(TimeZone.getDefault());

				// use the format object to display the day of month for this date
				String dateNumString = dateFrmt.format(new Date());

				// convert it into an integer
				int dateNum = Integer.valueOf(dateNumString).intValue();

				// if its even, change bResult from the default 'false' to 'true'
				if ( (dateNum % 2) == 0)
					bResult = true;

				break;

			case  3:		// HasAdminRole

				// obtain the list of roles for the user
				Vector roles = userData.getAttributes("role");

				// loop through the roles, and look for "admin"
				for (int i=0; i<roles.size(); i++)
				{
					UserAttribInfo uaInfo = (UserAttribInfo)roles.elementAt(i);
					if (uaInfo.m_attribName.equals("admin"))
					{
						// if found, set the result to true
						bResult = true;
						break;
					}
				}
				break;
				
			default:
				return false;
			}


			/**
			 * Do not alter code below here
			 */
			if (config[1] == 0)
			{
				if (isConditional)
				{
					bVal[0] = (sResult != null && sResult.length() > 0);
				}
				else
				{
					sVal[0] = sResult;
				}
			}
			else
			{
				if (isConditional)
				{
					bVal[0] = bResult;
				}
				else
				{
					sVal[0] = (bResult) ? "1" : "0";
				}
			}

			return true;
		}
	}

