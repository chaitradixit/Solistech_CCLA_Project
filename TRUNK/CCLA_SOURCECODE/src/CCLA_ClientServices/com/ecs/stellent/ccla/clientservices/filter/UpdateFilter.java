package com.ecs.stellent.ccla.clientservices.filter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.ecs.stellent.spp.fundprocessdetails.FundProcessDetailsManager;
import com.ecs.stellent.spp.fundprocessdetails.FundProcessDetailsService;
import com.ecs.stellent.spp.service.SppIntegrationUtils;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals;
import com.ecs.ucm.ccla.data.Company;
import com.ecs.ucm.ccla.data.SystemConfigVar;
import com.ecs.ucm.ccla.data.UCMForm;
import com.ecs.ucm.ccla.data.Fund;
import com.ecs.ucm.ccla.data.form.Form;
import com.ecs.ucm.ccla.data.form.FormConfig;
import com.ecs.ucm.ccla.data.form.FormHandler;
import com.ecs.ucm.ccla.data.form.FormUtils;
import com.ecs.ucm.ccla.data.form.UCMFormHandler;
import com.ecs.ucm.ccla.data.instruction.InstructionType;
import com.ecs.ucm.ccla.data.instruction.TransactionType;
import com.ecs.ucm.ccla.data.subscription.Contribution;
import com.ecs.ucm.ccla.data.subscription.Subscription;
import com.ecs.ucm.ccla.Globals.UCMFieldNames;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.DateFormatter;
import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ExecutionContext;
import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.data.Workspace;
import intradoc.shared.FilterImplementor;
import intradoc.shared.SharedObjects;

public class UpdateFilter implements FilterImplementor {
	
	/** Names of SystemConfigVars which control behaviour of the UpdateFilter.
	 * 
	 * @author Tom
	 *
	 */
	public static class ConfigVars {
		public static final String AUTOCOMPLETE_DOC_DATE 		=
		 "DocMeta_AutoCompleteDocDate";
		public static final String AUTOPAD_DOC_CLIENT_NUMBER	= 
		 "DocMeta_AutoPadClientNumber";
		public static final String AUTOPAD_DOC_ACCOUNT_NUMBER	= 
		 "DocMeta_AutoPadAccountNumber";
		public static final String STRICT_PAYMENT_REF_VALIDATION = 
		 "DocMeta_StrictPaymentRefValidation";
		public static final String PAYMENT_REF_CHECK_ON_DOC_UDPATE =
		 "DocMeta_PaymentRefCheckOnDocUpdate";
	}
	
	/** List of Document Classes considered to be Invoices. Metadata fields are used
	 *  differently for Invoices.
	 */
	public static String[] INVOICE_DOC_CLASSES; 
	
	static {
		String csvInvDocClasses = SharedObjects.getEnvironmentValue
		 ("CCLA_CS_InvoiceDocClasses");
	
		if (csvInvDocClasses != null)
			INVOICE_DOC_CLASSES = csvInvDocClasses.split(",");
		else
			INVOICE_DOC_CLASSES = new String[0];
	}
	
	/** Filter binding: postValidateCheckinData.
	 * 
	 *  Called after standard UCM validation has occurred.
	 *  
	 *  Immediately returns if the dDocType isn't 'Document' or 'ChildDocument'
	 *  
	 *  Performs various actions:
	 *  
	 *  1. Client account numbers forced to upper-case
	 *     a.) Checks if account number value is present
	 *     b.) If so, force the value to upper-case 
	 *  
	 *  2. Fund value auto-completion
	 *     a.) Strip the last character from the acc. no. and check if its
	 *     	   alphabetic.
	 *     b.) If it is, store it as the Fund value, replacing the previous Fund value
	 *         if there was one.
	 *  
	 *  3. Fund value forced to upper-case
	 *     
	 *  4. Padding decimal places to amount/unit values
	 *     a.) Check if xAmount value is non-null, numeric and lacking a decimal
	 *         place.
	 *     b.) If true, pad the value with 2 decimal places.
	 *     c.) Repeat for xUnits
	 *    
	 *  6. Company field auto-completion/Client Number sanitization
	 *     a.) Check if xCompany field is blank, but Fund field has a value
	 *     b.) If true, lookup the company associated with the Fund code
	 *         in the CCLA_FUNDS table.
	 *     c.) If a value is found, store it as the xCompany value.
	 *     d.) Strip and re-pad the Client Number so it has the appropriate zero-
	 *         padding for the Fund's Company.
	 *  
	 *  5. Validates the xFormId value if present, and pre-fills metadata
	 *     associated with the form.
	 *     
	 *  6. If the IRIS_DOC_PANEL_SAVE_REQUEST flag is present in the binder, a Process
	 *     Date will be re-calculated for the document and saved in the 
	 *     xOriginalProcessDate. This is then copied into the xProcessDate field, if the
	 *     current field value is null/empty.
	 *     
	 *  7. Document Date field auto-completion. If no value is present in xDocumentDate,
	 *     the documents creation date (dInDate) is copied in here.  
	 *     
	 *  8. Payment Ref checking and Subscription/Contribution updating. If the 
	 *     xPaymentRef field is non-null, its value is checked against existing Payment
	 *     Ref values in the Subscription table.
	 *     
	 *     If a match is found, the associated Subscription amount is cross-checked
	 *     against the xAmount field value to see if they match. If so, the associated
	 *     Subscription and Contribution 'Date Cash Processed' dates are updated.
	 *     
	 *     If the amounts differ, an exception is thrown.
	 *     
	 */
	public int doFilter(Workspace workspace, DataBinder binder, ExecutionContext exc)
	 throws DataException, ServiceException {

		long startTime = System.currentTimeMillis();
		
		FWFacade facade = null;
		
		String docType 			= binder.getLocal(UCMFieldNames.DocType);
		boolean isChildDoc = (docType!= null && docType.equals("ChildDocument"));
		
		// We are only interested in Document/ChildDocument doc types for the
		// rest of the method.
		if (StringUtils.stringIsBlank(docType) || 
			(!docType.equals("Document") && !isChildDoc))
			return FilterImplementor.CONTINUE;
		
		Log.debug("CCLA_ClientServices UpdateFilter started");
		
		String formId 			= binder.getLocal(UCMFieldNames.FormID);
		String docClass			= binder.getLocal(UCMFieldNames.DocClass);
		String fund				= binder.getLocal(UCMFieldNames.Fund);
		String accountNumber	= binder.getLocal(UCMFieldNames.AccountNumber);
		String clientNumber		= binder.getLocal(UCMFieldNames.ClientNumber);
				
		String accountNumberSuffix = null;

		/** Check for a delay period (in milliseconds)
		 * 
		 */
		Integer delayPeriod 	= CCLAUtils.getBinderIntegerValue
		 (binder, "updateDelayMillis");
		
		if (delayPeriod != null) {
			Log.debug("Delay Period value found in binder. Sleeping thread for " 
			 + delayPeriod + " milliseconds");
			
			try {
				Thread.sleep(delayPeriod.longValue());
			} catch (InterruptedException e) {
				Log.debug("Update rest interrupted: " + e.getMessage(), e);
			}
			
			Log.debug("Waking up");
		}
		
		// Force account number to uppercase
		if (!StringUtils.stringIsBlank(accountNumber)) {
			accountNumber = accountNumber.trim().toUpperCase();
			binder.putLocal(UCMFieldNames.AccountNumber, accountNumber);
			
			// Get up to the last 3 A-Z characters of account number, i.e. the fund code
			accountNumberSuffix = CCLAUtils.getSuffixChars(accountNumber);
		}
		
		if (!StringUtils.stringIsBlank(accountNumber)) {
			// Obtain the Fund Code from the Account Number
			
			if (accountNumberSuffix.length() > 0)
				fund = accountNumberSuffix;
			
			SystemConfigVar applyPadding = SystemConfigVar.getCache().getCachedInstance
			 (ConfigVars.AUTOPAD_DOC_ACCOUNT_NUMBER);
			
			if (applyPadding != null && applyPadding.getBoolValue() != null
				&& applyPadding.getBoolValue()) {
				// Fix zero-padding on the Account Number.
				
				long subStartTime = System.currentTimeMillis();

				// Extract the numeric prefix
				String accountNumberPrefix = accountNumber.substring
				 (0, accountNumber.length() - accountNumberSuffix.length());
				
				if (accountNumberPrefix.length() > 0) {
					try {
						int accNum = Integer.parseInt(accountNumberPrefix);
						
						if (fund != null) {
							Fund fundObj = Fund.getCache().getCachedInstance(fund);
							
							if (fundObj != null) {
								accountNumber = CCLAUtils.getPaddedAccountNumber
								 (accNum, fundObj);
								
								binder.putLocal
								 (UCMFieldNames.AccountNumber, accountNumber);
							}
						}
						
					} catch (NumberFormatException e) {
						// Silent error - don't throw.
						Log.debug("Unable to auto-pad Account Number, failed to " +
						 "parse numeric prefix");
					}
				}
				
				long subEndTime = System.currentTimeMillis();
				
				Log.debug("ClientServices UpdateFilter [Autopad Account Number] "
				 + "completed, dDocName: "
				 + binder.getLocal(UCMFieldNames.DocName) + ", time taken: " + 
				 (subEndTime-subStartTime)/1000D + "s");
			}
		}
		
		// Force Fund value to uppercase
		if (!StringUtils.stringIsBlank(fund)) {
			fund = fund.toUpperCase();
			binder.putLocal(UCMFieldNames.Fund, fund);
		}
		
		// Destination Account fields capitalization/validation
		// ====================================================
		String destFund 			= binder.getLocal(UCMFieldNames.DestinationFund);
		String destAccountNumber 	= binder.getLocal(UCMFieldNames.DestinationAccount);
			
		if (destAccountNumber!=null) {
			destAccountNumber = destAccountNumber.toUpperCase();
			
			binder.putLocal
			 (UCMFieldNames.DestinationAccount, destAccountNumber.toUpperCase());
			
			// Get up to the last 3 A-Z characters of dest. account number, 
			// i.e. the fund code
			String destAccountNumberSuffix = CCLAUtils.getSuffixChars(destAccountNumber);
			
			// Replace destination fund with the dest. account number suffix.
			if (destAccountNumberSuffix.length() > 0) {
				destFund = destAccountNumberSuffix;
			}
		}
		
		if (destFund!=null) 
			binder.putLocal
			 (UCMFieldNames.DestinationFund, destFund.toUpperCase());
		
		long amountUnitsStartTime = System.currentTimeMillis();
		
		// Amount field sanitising
		// =======================
		String amount = binder.getLocal(UCMFieldNames.Amount);
		String formattedAmount = null;
		
		if (!StringUtils.stringIsBlank(amount)) {
			formattedAmount = CCLAUtils.convertToDecimal(amount);
			
			if (formattedAmount != null) {
				binder.putLocal(UCMFieldNames.Amount, formattedAmount);
			}
		}
		
		// Unit field sanitising
		// =====================
		// Only apply if the document isn't an invoice type.
		if (!isInvoice(docClass)) {
			String units = binder.getLocal(UCMFieldNames.Units);
			String formattedUnits = null;
			
			if (!StringUtils.stringIsBlank(units)) {
				formattedUnits = CCLAUtils.convertToDecimal(units);
				
				if (formattedUnits != null) {
					binder.putLocal(UCMFieldNames.Units, formattedUnits);
				}
			}
		}
		
		long amountUnitsEndTime = System.currentTimeMillis();
		
		Log.debug("ClientServices UpdateFilter [Amount/Unit Field Sanitization] "
				 + "completed, dDocName: "
				 + binder.getLocal(UCMFieldNames.DocName) + ", time taken: " + 
				 (amountUnitsEndTime-amountUnitsStartTime)/1000D + "s");
	
		// Company field auto-completion
		// =============================
		addCompanyAndClientNumberToBinder
		 (fund, clientNumber, binder);
		
		// Form ID resolution
		// =============================
		if (!StringUtils.stringIsBlank(formId)) {
			
			long subStartTime = System.currentTimeMillis();
			
			if (facade == null)
				facade = CCLAUtils.getFacade(workspace,true);
			
			// Form ID check and metadata auto-completion
			// ==========================================
			checkFormId(formId, facade, binder);
			
			long subEndTime = System.currentTimeMillis();
			
			Log.debug("ClientServices UpdateFilter [Form ID Check] completed, dDocName: "
			 + binder.getLocal(UCMFieldNames.DocName) + ", time taken: " + 
			 (subEndTime-subStartTime)/1000D + "s");
		}
		
		if (!StringUtils.stringIsBlank(binder.getLocal("IRIS_DOC_PANEL_SAVE_REQUEST"))){

			// Calculate System Process Date
			// ==============================
			Date workflowDate = CCLAUtils.getBinderDateValue
			 (binder, Globals.UCMFieldNames.WorkflowDate);
			
			if (workflowDate==null) {
				long subStartTime = System.currentTimeMillis();
				
				if (facade == null)
					facade = CCLAUtils.getFacade(workspace,true);
				
				Date systemProcessDate = getSystemCalculatedProcessDate(binder, facade);
				if (systemProcessDate!=null) {
					Log.debug("Setting OriginalProcessDate to "+systemProcessDate.toString());
					CCLAUtils.addQueryDateParamToBinder(binder, Globals.UCMFieldNames.OriginalProcessDate, systemProcessDate);
					//binder.putLocalDate(Globals.UCMFieldNames.OriginalProcessDate, systemProcessDate);
					//Now set the xProcessDate field if it is blank
					if (StringUtils.stringIsBlank(binder.getLocal(Globals.UCMFieldNames.ProcessDate))){
						CCLAUtils.addQueryDateParamToBinder(binder, Globals.UCMFieldNames.ProcessDate, systemProcessDate);						
						//binder.putLocalDate(Globals.UCMFieldNames.ProcessDate, systemProcessDate);
					} 
				} else {
					Log.debug("Cannot set OriginalProcessDate");
				}
				
				
				long subEndTime = System.currentTimeMillis();
				
				Log.debug("ClientServices UpdateFilter [Get Process Date] "
				 + "completed, dDocName: "
				 + binder.getLocal(UCMFieldNames.DocName) + ", time taken: " + 
				 (subEndTime-subStartTime)/1000D + "s");
			}
		}
		
		// Document Date auto-completion
		// =============================
		if (StringUtils.stringIsBlank(binder.getLocal(UCMFieldNames.DocumentDate))) {
			SystemConfigVar autoCompleteDate = SystemConfigVar.getCache()
			 .getCachedInstance(ConfigVars.AUTOCOMPLETE_DOC_DATE);
			
			if (autoCompleteDate != null && autoCompleteDate.getBoolValue() != null
				&& autoCompleteDate.getBoolValue()) {
				Log.debug("Auto-completing " + UCMFieldNames.DocumentDate + " value");
				
				long subStartTime = System.currentTimeMillis();
				
				String creationDate = binder.getLocal(UCMFieldNames.DocInDate);
				
				if (StringUtils.stringIsBlank(creationDate)) {
					// No dInDate present - must be new checkin
					creationDate = DateFormatter.getTimeStamp();
					
					Log.debug("No " + UCMFieldNames.DocInDate + " value present, " +
					 "using new timestamp: " + creationDate);
				} else {
					Log.debug(UCMFieldNames.DocInDate + " value present, " +
					 "using this: " + creationDate);
				}
				
				binder.putLocal(UCMFieldNames.DocumentDate, creationDate);
				
				long subEndTime = System.currentTimeMillis();
				
				Log.debug("ClientServices UpdateFilter [AutoComplete Date] "
				 + "completed, dDocName: "
				 + binder.getLocal(UCMFieldNames.DocName) + ", time taken: " + 
				 (subEndTime-subStartTime)/1000D + "s");
			}
		}
		
		// Payment Ref checking
		// ====================
		SystemConfigVar PAYMENT_REF_CHECK_ON_DOC_UDPATE = SystemConfigVar.getCache()
		 .getCachedInstance(ConfigVars.PAYMENT_REF_CHECK_ON_DOC_UDPATE);
		
		if (PAYMENT_REF_CHECK_ON_DOC_UDPATE != null
			&&
			PAYMENT_REF_CHECK_ON_DOC_UDPATE.getBoolValue()) {
			
			String paymentRef = binder.getLocal(UCMFieldNames.PaymentRef);
			
			if (!StringUtils.stringIsBlank(paymentRef)) {
				paymentRef = paymentRef.trim();
				String amountStr = binder.getLocal(UCMFieldNames.Amount);
				
				if (facade == null)
					facade = CCLAUtils.getFacade(workspace, true);
				
				Date processDate = CCLAUtils.getBinderDateValue
				 (binder, UCMFieldNames.ProcessDate);
				
				checkPaymentRef(paymentRef, docClass, amountStr, processDate, facade);
			}
		}
		
		long endTime = System.currentTimeMillis();
		
		Log.debug("ClientServices UpdateFilter completed, dDocName: "
		 + binder.getLocal(UCMFieldNames.DocName) + ", time taken: " + 
		 (endTime-startTime)/1000D + "s");
		
		return FilterImplementor.CONTINUE;
	}
	
	/** Checks that the account number and fund field values correspond to each other.
	 * 
	 *  e.g. 	an account number of '0001C' and fund of 'C' is valid
	 *  		an account number of '0001D' and fund of 'C' is invalid.
	 *  
	 * @param fund
	 * @param accountNumber
	 * @throws ServiceException if there is a mismatch between the values.
	 */
	public static void checkFundAndAccountNumber(String fund, String accountNumber) 
	 throws ServiceException {
		
		if (!StringUtils.stringIsBlank(fund) 
			|| !StringUtils.stringIsBlank(accountNumber)) {
			// Not enough data for check.
			return;
		}
		
	}
	
	/** Adds an xCompany value to the binder, providing that the current
	 *  xCompany binder value is null and a non-null Fund value is present.
	 *  
	 *  Providing a Company is resolved above, the method will also attempt
	 *  to 'fix' the Client Number value entered by the user. This involves casting
	 *  it to an int then back to a string to remove leading zeroes, then re-padding
	 *  with the appropriate no. of zeros for the give Company.
	 *  
	 * @param fundCode
	 * @param facade
	 * @param binder
	 * @throws DataException
	 * @throws ServiceException
	 */
	public static void addCompanyAndClientNumberToBinder
	 (String fundCode, String clientNumber, DataBinder binder)
	 throws DataException, ServiceException {
		
		long subStartTime = System.currentTimeMillis();
		
		Company company = null;
		
		String companyCode = binder.getLocal(UCMFieldNames.Company);
		
		if (StringUtils.stringIsBlank(companyCode)) {
			if (!StringUtils.stringIsBlank(fundCode)) {
				Log.debug("Attempting to complete xCompany field for Fund: " + fundCode);
				
				Fund fund = Fund.getCache().getCachedInstance(fundCode);
				
				if (fund == null) {
					Log.debug("No matching Fund entry found for " + fundCode);
					return;
				}
				
				company = fund.getCompany();
				
				Log.debug("Adding company value to binder: " + company.getCode());
				binder.putLocal(UCMFieldNames.Company, company.getCode());
			
			} else {
				Log.debug("Unable to complete Company code, no Fund Code specified");
				return;
			}
			
		} else { 
			company = Company.getNameCache().getCachedInstance(companyCode);
		}
		
		if (!StringUtils.stringIsBlank(clientNumber)
			&&
			company != null) {
			
			SystemConfigVar applyPadding = SystemConfigVar.getCache().getCachedInstance
			 (ConfigVars.AUTOPAD_DOC_CLIENT_NUMBER);
			
			if (applyPadding != null && applyPadding.getBoolValue() != null
				&& applyPadding.getBoolValue()) {
				// Ensure minimum padding digits, per company
				
				try {
					// Strip all zeros from xClientNumber by casting to int and
					// back to string again.
					String strippedClientNumber = 
					 Integer.toString(Integer.parseInt(clientNumber));
					
					// Now re-add appropriate padding.
					strippedClientNumber =
					 CCLAUtils.padClientNumber(strippedClientNumber, company);
					
					if (!strippedClientNumber.equals(clientNumber)) {
						Log.debug("Adding stripped/re-padded client number to binder: " 
						 + strippedClientNumber);
						binder.putLocal
						 (UCMFieldNames.ClientNumber, strippedClientNumber);
					}
					
				} catch (NumberFormatException e) {
					Log.debug("Failed to parse Client Number " + clientNumber + 
					 ". Unable to apply auto-padding");
				}
			}
		}
	
		long subEndTime = System.currentTimeMillis();
		
		Log.debug("ClientServices UpdateFilter [Add Company/Client Number] "
		 + "completed, dDocName: "
		 + binder.getLocal(UCMFieldNames.DocName) + ", time taken: " + 
		 (subEndTime-subStartTime)/1000D + "s");
	}
	
	private static void checkFormId(String formIdStr, FWFacade facade, DataBinder binder)
	 throws DataException, ServiceException {
		
		if (StringUtils.stringIsBlank(formIdStr))
			return;
		
		Integer formId 			= null;
		
		// Basic validation: first check if the formId is a valid integer.
		try {
			formId = CCLAUtils.getBinderIntegerValue(binder, UCMFieldNames.FormID);
		} catch (Exception e) {
			Log.warn("Non-numeric form ID found in xFormId field. Removing value.");
			binder.putLocal(UCMFieldNames.FormID, "");
			
			return;
		}
		
		String service = binder.getLocal("IdcService");
		boolean isCheckin = false;
		
		if (!StringUtils.stringIsBlank(service))
			isCheckin = service.indexOf("CHECKIN") >= 0;
			
		// Check new forms table
		boolean foundForm = checkForFormData(formId, isCheckin, binder, facade);
		
		if (!foundForm) {
			// Check legacy forms table
			foundForm = checkForLegacyFormData(formId, isCheckin, binder, facade);
		}
		
		if (!foundForm) {
			Log.debug("No matching form data for ID: " + formId + 
			 ". Removing from metadata.");
			binder.putLocal(UCMFieldNames.FormID, "");
			
			return;
		}
	}
	
	private static boolean checkForFormData
	 (int formId, boolean isCheckin, DataBinder binder, FWFacade facade) 
	 throws DataException, ServiceException {
		
		// Check new forms table
		Form form = Form.get(formId, facade);
		
		if (form == null) {
			Log.debug("No matching form data for ID: " + formId);
			return false;
		}
		
		Log.debug("Found corresponding form data for ID: " + formId);
		
		String returnedDocGuid 	= form.getRetDocGuid();
		
		// Fetch document ID fields from binder.
		Integer docId 			= CCLAUtils.getBinderIntegerValue
		 (binder, UCMFieldNames.DocID);
		Integer revisionId 		= CCLAUtils.getBinderIntegerValue
		 (binder, UCMFieldNames.RevisionId);
		String docName 			= binder.getLocal(UCMFieldNames.DocName);
		
		// Try to build Doc GUID to compare against form table, providing that:
		// - this isn't a checkin service (doc refs won't be available to build GUID)
		// - the form is singleton type
		// - the form already has a returned doc reference attached
		String docGuid			= null;
		
		boolean isSingleton		= form.getFormType().isSingleton();
		
		if (!isCheckin && isSingleton && (returnedDocGuid != null)) {
			if (!StringUtils.stringIsBlank(docName) && revisionId != null) {
				// dDocName AND dRevisionId present in binder, can build the Doc GUID
				// directly from these.
				Log.debug("Building Doc GUID from raw values");
				docGuid = CCLAUtils.getDocGuidFromRawValues(docName, revisionId);
			} else if (docId != null) {
				// dDocName present but no dRevisionId. Try and resolve Doc GUID by building
				// a LWDocument instance. Will fail if this is a brand new check-in.
				Log.debug("Attempting to resolve Doc GUID by building LWDocument instance");
				try {
					// Attempt to fetch
					docGuid = CCLAUtils.getDocGuidFromDid(docId);
				} catch (DataException e) {
					// Assume brand new checkin.
					String err = "Couldn't resolve LWDocument: " + e.getMessage(); 
					Log.error(err); 
					 
					//throw new ServiceException(err);
				}
			}
			
			if (docGuid == null) {
				// Nothing we can do now :(
				String msg = "Not enough parameters to build Doc GUID value to check " +
				 "Form ID " + formId + " against";
				Log.error(msg);
				throw new ServiceException(msg);
			}
			
			Log.debug("Resolved Doc GUID: " + docGuid);
		}
		
		if (isSingleton && returnedDocGuid != null) {
			// This singleton form already has a 'returned Doc ID' (dID) set. This may
			// be a potential duplicate.
			
			// Check if this is a new checkin, and the passed dID is either
			// blank, or doesn't match the one assigned to the form.
			if (isCheckin 
				&& 
				(docGuid != null)
				&&
				(docGuid.equals(returnedDocGuid))) {
				Log.debug("Appears to be a CHECKIN service. Passed Doc GUID " +
				docGuid + " matches the one already tagged to form - " +
				 "preserving form ID.");
				
				return true;
			} else if (isCheckin || docGuid == null) {
				Log.debug("No dID present/appears to be a CHECKIN service.");
				Log.debug("Form ID " + formId + " already assigned to doc GUID " + 
				 returnedDocGuid + ", removing from item metadata.");
				
				binder.putLocal(UCMFieldNames.FormID, "");
				return true;
				
				// Throw error if form GUID already in use.
			} else if (!returnedDocGuid.equals(docGuid)) {
				String msg = "Form ID " + formId + 
				 " already assigned to item docGuid " + returnedDocGuid;
				
				Log.error(msg);
				throw new ServiceException(msg);
			}
		}
		
		// Form ID has passed all validation checks. Now check if this document
		// was previously assigned to the form data itself. If not, perform
		// initial post-checkin tasks.
		boolean doPostCheckinActions = false;
		
		if (isSingleton) {
			// Easy check for singleton form types - compare the Returned Doc ID value
			// in the forms table to the current document.
			Log.info("returnedDocGuid: "+ returnedDocGuid + " docGuid: "+docGuid);
			
			if ((returnedDocGuid != null) && returnedDocGuid.equals(docGuid)) {
				Log.debug("Form ID " + formId + " was already assigned to " + docId + 
				 ". No further action required.");
			} else {
				doPostCheckinActions = true;
			}
			
		} else {
			// Non-singleton forms: check the FormID value currently assigned to the
			// document. If it doesn't match the newly assigned Form ID, do post-checkin
			// tasks.
			FWFacade ucmFacade = CCLAUtils.getFacade(false);
			
			DataResultSet rsDocMeta = CCLAUtils.getUCMDocMeta(docId, ucmFacade);
			Integer currentFormId = null;
			
			// If doc hasn't been checked in yet, we can't check the currently-assigned
			// form ID!
			if (rsDocMeta != null && !rsDocMeta.isEmpty()) {
				currentFormId = CCLAUtils.getResultSetIntegerValue
				 (rsDocMeta, UCMFieldNames.FormID);
			}
			
			if (currentFormId == null || !currentFormId.equals(formId))
				doPostCheckinActions = true;
			else
				Log.debug("Form ID " + formId + " was already assigned to " 
				 + returnedDocGuid + ". No further action required.");
		}
		
		if (doPostCheckinActions) {
			Log.debug("Adding form metadata to binder");
			
			String userName = binder.getLocal("dUser");
			
			FormHandler handler = form.getFormType().getHandlerInstance
			 (form, userName, facade);
				
			Hashtable<String, String> docMeta = handler.getDocMetaMapping();
			CCLAUtils.addMappingToBinder(docMeta, binder);
			
			Log.debug("Adding form post-checkin hook to binder");
			binder.putLocal("csFormDoPostCheckinActions", "1");
		}
			
		return true;
	}
	
	/** Checks for a matching Form ID in the old CCLA_FORM table.
	 * 
	 * @param formId
	 * @param isCheckin
	 * @param binder
	 * @param facade
	 * @return true if the Form ID was resolved to an ID in the legacy table
	 * @throws DataException
	 * @throws ServiceException
	 */
	private static boolean checkForLegacyFormData
	 (int formId, boolean isCheckin, DataBinder binder, FWFacade facade) 
	 throws DataException, ServiceException {
		
		// Check legacy forms table
		UCMForm ucmForm = UCMForm.get(formId, facade);
		
		if (ucmForm == null) {
			Log.debug("No matching legacy form data for ID: " + formId);
			return false;
		}
		
		String docType = binder.getLocal(UCMFieldNames.DocType);
		
		// Put in a horrible hack thing here to ensure old Ethical Fund Transfer form
		// IDs on ChildDocuments dont cause any trouble during archiving process.
		if (ucmForm.getType().equals("EthicalFundTransfer_Confirm")
			&& docType != null && docType.equals("ChildDocument")) {
			Log.debug("Detected an old Ethical Fund Transfer form ID on a child " +
			 "document, terminating form handler actions");
			return true; // return true so xFormId doesn't get deleted.
		}	
		
		Log.debug("Found corresponding legacy form data for ID: " + formId);
		
		String returnedDocName 	= ucmForm.getReturnedDocName();
		String docName 			= binder.getLocal(UCMFieldNames.DocName);
		
		if (!StringUtils.stringIsBlank(returnedDocName)) {
			// This form already has a 'returned docname' set. This may
			// be a potential duplicate.
			
			// Check if this is a new checkin, and the passed dDocName is either
			// blank, or doesn't match the one assigned to the form.
			if (isCheckin 
				&& 
				!StringUtils.stringIsBlank(docName)
				&&
				(docName.equals(returnedDocName))) {
				Log.debug("Appears to be a CHECKIN service. Passed dDocName " +
				 docName + " matches the one already tagged to form - " +
				 "preserving form ID.");
				
			} else if (isCheckin || StringUtils.stringIsBlank(docName)) {
				Log.debug("No docName present/appears to be a CHECKIN service.");
				Log.debug("Form ID " + formId + " already assigned to item " + 
				 ucmForm.getReturnedDocName() + ", removing from item metadata.");
				
				binder.putLocal(UCMFieldNames.FormID, "");
			} else {
				// Throw error if form ID already in use.
				if (!returnedDocName.equals(docName)) {
					Log.error("Form ID " + formId + 
					 " already assigned to item " + ucmForm.getReturnedDocName());
					
					throw new ServiceException("Form ID " + formId + 
					 " has already been allocated to item: " + 
					 ucmForm.getReturnedDocName());
				}
			}
		}
		
		// Form ID has passed all validation checks. Now check if this document
		// was previously assigned to the form data itself. If not, perform
		// initial post-checkin tasks.
		if (!StringUtils.stringIsBlank(docName) && returnedDocName.equals(docName)) {
			Log.debug("Form ID " + formId + " was already assigned to " + docName + 
			 ". No further action required.");
		} else {
			Log.debug("Adding form metadata to binder");
			
			FormConfig config 		= FormUtils.getFormConfig(ucmForm, facade);
			UCMFormHandler handler		= FormUtils.getFormHandler
			 (ucmForm, config.getHandlerClass(), binder.getLocal("UserName"), facade);
				
			Hashtable<String, String> docMeta = handler.getDocMetaMapping();
			CCLAUtils.addMappingToBinder(docMeta, binder);
			
			Log.debug("Adding form post-checkin hook to binder");
			binder.putLocal("csFormDoPostCheckinActions", "1");
		}
		
		return true;
	}
	
	
	private static Date getSystemCalculatedProcessDate(DataBinder binder, FWFacade facade) 
	throws DataException, ServiceException
	{
		String fundCode = binder.getLocal(Globals.UCMFieldNames.Fund);
		String destFundCode = binder.getLocal(Globals.UCMFieldNames.DestinationFund);
		String docClass = binder.getLocal(Globals.UCMFieldNames.DocClass);
		String source = binder.getLocal(Globals.UCMFieldNames.Source);
		Date dInDate = CCLAUtils.getBinderDateValue(binder, Globals.UCMFieldNames.DocInDate);
		
		if (dInDate==null) {
			throw new ServiceException("Error, cannot find dInDate for document");
		}
		
		Log.debug(
				"fundCode:"+(fundCode==null?"null":fundCode)+
				", destFundCode: "+(destFundCode==null?"null":destFundCode)+
				", docClass: "+(docClass==null?"null":docClass)+
				", source: "+(source==null?"null":source)+
				", dInDate:"+(dInDate==null?"null":dInDate.toString()));
		return FundProcessDetailsManager.getDealingDate(fundCode, destFundCode, docClass, 
				null, false, source, dInDate, false, facade);
	}
	
	private static boolean isInvoice(String docClass) {
		if (StringUtils.stringIsBlank(docClass))
			return false;
		
		for (String invDocClass : INVOICE_DOC_CLASSES) {
			if (docClass.equals(invDocClass))
				return true;
		}
		
		return false;
	}
	
	/** Checks the corresponding Payment Ref against its Subscription (if applicable).
	 *  
	 *  Before going any further, confirm this is a 'Deposit' type instruction, e.g.
	 *  DEPBNK.
	 *   
	 *  If the Subscription is at a valid status, and the passed cash amount matches
	 *  the total Subscription amount, the status of the Subscription is automatically
	 *  set to 'Cash Received'
	 *  
	 * @param paymentRef
	 * @param amountStr
	 * @param processDate
	 * @param facade
	 * @throws DataException
	 * @throws ServiceException
	 */
	private void checkPaymentRef
	 (String paymentRef, String docClass, String amountStr, Date processDate, 
	 FWFacade facade) 
	 throws DataException, ServiceException {

		// Ensure document class is a deposit type.
		if (StringUtils.stringIsBlank(docClass))
			return;
		
		InstructionType instrType = InstructionType.getNameCache().getCachedInstance
		 (docClass);
		
		boolean validInstrType = false;
		
		if (instrType != null
			&&
			(
				instrType.getTransactionType() != null
				&&
				instrType.getTransactionType().equals(TransactionType.BUY)
			)) {
			validInstrType = true;
		}
		
		if (!validInstrType)
			return;
		
		Log.debug("Checking passed Payment Ref '" + paymentRef + "'");
		
		// Search for a Subscription with the given Payment Ref.
		Subscription subscription = Subscription.getByPaymentRef
		 (paymentRef, facade);
		
		if (subscription != null) {
			Log.debug("Found a matching Subscription (ID " + subscription.getId() + 
			 ") with Payment Ref: " + paymentRef);
			
			if (subscription.getStatusId()
				== Subscription.SubscriptionStatusIds.CANCELLED
				||
				subscription.getStatusId() 
				== Subscription.SubscriptionStatusIds.COMPLETED) {
				Log.debug("Subscription had invalid status for receipt of cash " +
				" (possibly Cancelled/Completed?)");
				
			} else if (!StringUtils.stringIsBlank(amountStr)) {
				// Cross-check total Subscription amount against the xAmount value.
				BigDecimal cashAmount = null;
				
				try {
					cashAmount = new BigDecimal(amountStr);
				} catch (Exception e) {
					String msg = "Expected a numeric value in the " +
					 "Amount field, got " + amountStr + " instead";
					
					Log.error(msg, e);
					throw new ServiceException(msg);
				}
				
				// Determines whether this payment amount is a 100% match for the
				// subscription amount.
				boolean paymentMatchesSubscriptionAmount = false;
				
				if (cashAmount.compareTo(subscription.getSubscriptionAmount()) 
					== 0) {
					// Exact match.
					Log.debug("Cash Amount matches Subscription Amount exactly");
					
					paymentMatchesSubscriptionAmount = true;
				} else {
					// Amount did not match total subscription amount
					String msg = "Cash Amount did not match Subscription Amount, " +
					 "expected " + subscription.getSubscriptionAmount() + " " +
					 "(Subscription ID " + subscription.getId() + "). " +
					 "Assuming partial payment";
					
					// Check for strict validation switch. 
					SystemConfigVar strictPaymentRefValidation = 
					 SystemConfigVar.getCache().getCachedInstance
					 (ConfigVars.STRICT_PAYMENT_REF_VALIDATION);
					
					if (strictPaymentRefValidation != null && 
					 strictPaymentRefValidation.getBoolValue()) {	
						Log.error(msg);
						throw new ServiceException(msg);
					} else {
						Log.warn(msg);
					}
				}
				
				// See if the subscription record needs updating. Requires a 
				// Process Date set.
				if (processDate != null) {
					// Whether or not the subscription Date Latest Cash Received
					// field should be updated.
					boolean applyLatestCashReceivedDate = false;
					
					// Only update the subscription status to 'Cash Received' after
					// 100% of the subscription amount has been paid.
					boolean applySubscriptionStatusUpdate = 
					 paymentMatchesSubscriptionAmount;
					
					if (subscription.getDateLatestCashProcessed() == null) {
						
						Log.debug("Current Cash Process Date is empty, " +
						 "populating with document process date of " + processDate);	
						
						applyLatestCashReceivedDate = true;
	
					} else if (subscription.getDateLatestCashProcessed().compareTo
						(processDate) != 0) {
						
						Log.debug("Current Latest Cash Processed date on " +
						 "subscription differs from document process date of " 
						 + processDate + ". Updating to reflect new date");
						
						applyLatestCashReceivedDate = true;
					}
					
					if (applyLatestCashReceivedDate
						|| applySubscriptionStatusUpdate) {
						// Subscription record will now be updated.
						
						if (applyLatestCashReceivedDate)
							subscription.setDateLatestCashProcessed(processDate);
						
						if (applySubscriptionStatusUpdate)
							subscription.setStatusId
							 (Subscription.SubscriptionStatusIds.CASH_RECEIVED);
						
						subscription.persist(facade, Globals.Users.System);
						
						if (applyLatestCashReceivedDate) {
							// Update all Contribution Date Latest Cash Processed 
							// values.
							Vector<Contribution> contribs = Contribution
							 .getAllBySubscriptionId(subscription.getId(), facade);
							
							Log.debug("Setting Latest Cash Process Date against " + 
							 contribs.size() + " associated contributions");
							
							for (Contribution contrib : contribs) {
								contrib.setDateLatestCashProcessed(processDate);
								contrib.persist(facade, Globals.Users.System);
							}
						}
					}
				}
			} else {
				Log.debug("Couldn't check Payment Ref, Amount field was empty");
			}
		}
	}
}
