package com.ecs.ucm.ccla.customIdoc;

import java.util.Date;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals.UCMFieldNames;
import com.ecs.ucm.ccla.data.SystemConfigVar;
import com.ecs.ucm.ccla.utils.DocumentUtils;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.DataResultSetUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ExecutionContext;
import intradoc.common.GrammarElement;
import intradoc.common.LocaleUtils;
import intradoc.common.ScriptInfo;
import intradoc.common.ScriptUtils;
import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataResultSet;
import intradoc.data.ResultSetUtils;
import intradoc.server.Service;
import intradoc.server.script.ScriptExtensionUtils;
import intradoc.shared.UserData;

/** Custom IdocScript functions used in the CCLA environment.
 *  
 * @author tm
 *
 */
public class CustomIdocFunction extends intradoc.common.ScriptExtensionsAdaptor {

	public CustomIdocFunction ()
	{
		// this is a list of the functions that can be called with the custom code
		m_functionTable = (new String[] {
			"getPdfDocUrl", 				// 0
			"irisGetAliasMembers",			// 1
			"getSystemConfigVar"			// 2
		});

		// Configuration data for functions.  This list must align with the "m_functionTable"
		// list.  In order the values are: 
		// "id number", 
		// "Number of arguments", 
		// "First argument type",
		// "Second argument type", 
		// "Return Type".  
		// Return type has the following possible
		// values: 0 generic object (such as strings) 1 boolean 2 integer 3 double.
		// The value "-1" means the value is unspecified.
		m_functionDefinitionTable = new int[][]
		{
			{0, 0, -1, -1, 	GrammarElement.STRING_VAL}, // getPdfDocUrl
			{1, 0, -1, -1, 	GrammarElement.STRING_VAL}, // irisGetAliasMembers
			{2, 1, GrammarElement.STRING_VAL, -1, 	GrammarElement.STRING_VAL}, // getSystemConfigVar
		};
	}

	public boolean evaluateFunction(ScriptInfo info, Object[] args, ExecutionContext context) 
	throws ServiceException {

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
		
		if (allowedParams >= 0 && allowedParams != nargs) {
			String msg = LocaleUtils.encodeMessage("csScriptEvalNotEnoughArgs", 
				null, function, ""+allowedParams);
			throw new IllegalArgumentException(msg);
		}
		
		String msg = LocaleUtils.encodeMessage("csScriptMustBeInService", 
			null, function, "Service");
		Service service = ScriptExtensionUtils.getService(context, msg);
		DataBinder binder = service.getBinder();
		
		UserData userData = (UserData)context.getCachedObject("UserData");
		if (userData == null) {
			msg = LocaleUtils.encodeMessage("csUserDataNotAvailable", null, function);
			throw new ServiceException(msg);
		}
		
		// Do some initial conversion of arguments.  Choices of what initial conversions to make
		// are based on frequency of usage.  If a function uses nontypical parameters it will
		// have to do its own conversion.
		String sArg1 = null, sArg2 = null;
		long lArg1, lArg2, lArg3 = 0;
		Date dArg1, dArg2;
		
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
			else if (config[2] == GrammarElement.DATE_VAL)
			{
				dArg1 = ScriptUtils.getDateVal(args[0], context);
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
			else if (config[2] == GrammarElement.DATE_VAL)
			{
				dArg2 = ScriptUtils.getDateVal(args[1], context);
			}
		}
		
		boolean bResult = false;  // Used for functions that return a boolean.
		int iResult = 0; // Used for functions that return an integer.
		double dResult = 0.0;  // Used for functions that return a double.
		Object oResult = null; // Used for functions that return an object (string).
		
		/**
		 * Here is where the custom code should go. The case values coincide
		 * with the "id values" in m_functionDefinitionTable. Perform the
		 * calculations here, and place the result into ONE of the result
		 * variables declared below.  Use 'sArg1' and 'sArg2' for the first and
		 * second String arguments for the function (if they exist).  Likewise use
		 * 'lArg1' and 'lArg2' for the first and second long integer arguments.
		 */

		switch (config[0])
		{
			case 0:		//getPdfDocUrl
				try {
					String dDocType = binder.getActiveValue
					 (UCMFieldNames.DocType);

					String dSecurityGroup = binder.getActiveValue
					 (UCMFieldNames.SecurityGroup);
					String dDocAccount = binder.getActiveValue
					 (UCMFieldNames.DocAccount);
					String dDocName = binder.getActiveValue
					 (UCMFieldNames.DocName);
					String parentDocName = binder.getActiveValue
					 (UCMFieldNames.ParentDocName);
					String pdfDocName = binder.getActiveValue
					 (UCMFieldNames.PdfDocName);
					String folderName = binder.getActiveValue
					 (UCMFieldNames.FolderName);
					
					FWFacade ucmFacade = CCLAUtils.getFacade(service.getWorkspace());
					
					oResult = DocumentUtils.generateDocUrl
					 (dSecurityGroup, dDocAccount, dDocType, dDocName, parentDocName, 
					 pdfDocName, folderName, ucmFacade);
					
				} catch (Exception e) {
					Log.error("Failed to generate PDF Doc URL: " + e.getMessage(), e);
					return false;
				}
				break;
			case 1: // irisGetAliasMembers
				try{
					String aliasName = binder.getActiveValue("aliasName");
					if (StringUtils.stringIsBlank(aliasName)){
						throw new Exception("aliasName is empty");
					}
					FWFacade ucmFacade = CCLAUtils.getFacade(service.getWorkspace());
					DataResultSet rsAliasMembers =
						ucmFacade.createResultSet("qIrisCore_AliasMembers", binder);
					
					String aliasUsersList = "";
					
					if (!rsAliasMembers.isEmpty() && rsAliasMembers.first()){
						do {
							if(aliasUsersList.length()>0)
								aliasUsersList += ",";
							
							aliasUsersList += 
								DataResultSetUtils.getResultSetStringValue
								 (rsAliasMembers, "dUserName");
							
						}while (rsAliasMembers.next());
					}
					
					oResult = aliasUsersList;
				} catch (Exception e) {
					Log.error("Unable to get alias members: " + e.getMessage(), e);
					return false;
				}
			
				break;
			case 2: // getSystemConfigVar
				try{
					if (sArg1 != null) {
						String configVarName = sArg1;

						SystemConfigVar cfgVar = 
						 SystemConfigVar.getCache().getCachedInstance(configVarName);
						
						String cfgVarValue = null;
						
						if (cfgVar != null) {
							cfgVarValue = cfgVar.getValue();
						}
							
						if (cfgVarValue != null)
							oResult = cfgVarValue;
						else
							oResult = "";
					}
				} catch (Exception e) {
					return false;
				}
				break;
			default:
				return false;
		}
		
		/**
		 * Do not alter code below here
		 */
		args[nargs] = ScriptExtensionUtils.computeReturnObject(config[4],
			bResult, iResult, dResult, oResult);

		// Handled function.
		return true;
		
		
//		try {
//			service = ScriptExtensionUtils.getService(context,"DocService");
//			intradoc.data.Workspace ws = service.getWorkspace();
//			binder = service.getBinder();
//		
//			userData = (UserData)context.getCachedObject("UserData");
//		
//			binder.putLocal("xMyProperty","Value here");
//			binder.putLocal("xComments","Comments here");
//		
//		}
//		catch(Exception exp) {
//			//log or something
//		}
//		return true;
	}	
}
