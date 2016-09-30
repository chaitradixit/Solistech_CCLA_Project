package com.ecs.stellent.ccla.clientservices.form;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.shared.SharedObjects;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import com.aurora.webservice.Account;
import com.aurora.webservice.Correspondent;
import com.ecs.stellent.ccla.clientservices.spool.FundTransferSpoolFileGenerator;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.aurora.AuroraWebServiceHandler;
import com.ecs.ucm.ccla.data.form.FormPrinter;
import com.ecs.ucm.ccla.data.form.spool.SpoolHeader;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.embedded.FWFacade;

public class FundTransferFormUtils {
	
	/** Creates and checks in the Ethical Fund form. This requires creation
	 *  of the spool file and associated Create!Form PDF - these are checked
	 *  in as the primary/web renditions respectively. Currently the 
	 *  Create!Form PDF rendition is unavailable.
	 *  
	 *  A processId must be present in the binder, this is assumed to relate
	 *  to a Client Services process ID. The associated process will be looked
	 *  up to determine the company/correspondent ID combo required to build
	 *  the form.
	 *  
	 * @throws ServiceException
	 */
	public static void checkinEthicalFundForm(DataBinder m_binder, FWFacade facade, 
	 String userName) 
	 throws ServiceException, DataException {
	
		ByteArrayOutputStream outputStream = null;
		
		String processId		= m_binder.getLocal("processId");
		
		String correspondentId	= null;
		String company			= null;
		
		DataResultSet rsProcess =
		 facade.createResultSet("qClientServices_GetProcess", m_binder);
		
		correspondentId 	= rsProcess.getStringValueByName("PERSON_ID");
		company				= rsProcess.getStringValueByName("COMPANY");
		
		m_binder.putLocal("personId", correspondentId);
		m_binder.putLocal("correspondentId", correspondentId);
		m_binder.putLocal("company", company);
		
		String clientId		= m_binder.getLocal("clientId");
		
		Log.debug("Checking in Ethical Fund transfer form for correspondent: " 
		 + correspondentId);
		
		File spoolFile = null;
		
		try {
			String docName = "CS_" + processId;
			m_binder.putLocal("formDocName", docName);
			
			outputStream = createEthicalFundSpoolFile(m_binder, facade, userName);
			
			spoolFile = new File(correspondentId + "_spool.txt");

			FileOutputStream fileOutputStream = new FileOutputStream(spoolFile);
			fileOutputStream.write(outputStream.toByteArray());
			
			LWDocument lwDoc 	= null;
			boolean upRevision	= false;
			
			try {
				lwDoc 		= new LWDocument(docName, true);
				upRevision	= true;
				
				lwDoc.checkout();
				
			} catch (Exception e) {
				// Document doesn't exist yet.
				lwDoc = new LWDocument();
				lwDoc.setDocName("CS_" + processId);
			}
				
			// Set default attributes
			lwDoc.setTitle("Ethical Fund Transfer - " + correspondentId);
			lwDoc.setSecurityGroup("Public");
			lwDoc.setDocType("Document");
			lwDoc.setAuthor(userName);
			
			// Set custom attributes
			lwDoc.setAttribute("xDocumentClass", "FUNDSPLIT");
			lwDoc.setAttribute("xCompany", company);
			lwDoc.setAttribute("xCorrespondentCode", correspondentId);
			
			if (!StringUtils.stringIsBlank(processId))
				lwDoc.setAttribute("xParentId", processId);
			
			lwDoc.checkin(spoolFile);
			
			Log.debug("Checked in Ethical Fund transfer form with dDocName: " 
			 + docName);
			
			m_binder.putLocal("formDocName", docName);
			
			if (!StringUtils.stringIsBlank(processId)) {
				// Add activity log to Client Services process, indicating form
				// was generated.
				/*
				addOrUpdateActivity(processId, null,
				 userName, "Form Generation", 
				 "Fund Transfer confirmation created @DOCNAME:" + docName + "@", 
				 null, clientId, null, null, facade);
				*/
			}
			
		} catch (Exception e) {
			Log.error("Failed to checkin ethical fund form", e);
			
			if (!StringUtils.stringIsBlank(processId)) {
				// Add activity log to Client Services process, indicating form
				// was generated.
				/*
				addOrUpdateActivity(processId, null, 
				 userName, "Form Generation", 
				 "Failed to generate Fund Transfer confirmation", 
				 null, clientId, null, null, facade);
				 */
			}
			
			throw new ServiceException(e);
		} finally {
			if (spoolFile != null)
				spoolFile.delete();
		}
	}
	
	/** Generates the Create!Form spool file for a correspondent's Ethical
	 *  Fund Transfer forms. This is returned as a ByteArrayOutputStream.
	 *  
	 *  This requires several operations:
	 *  
	 *  1. Fetch correspondent data using Aurora web service.
	 *  2. Build spool file header wrapper object
	 *  3. Fetch chosen account transfers
	 *  4. Fetch full correspondent account information
	 *  5. Build list of account transfer information for spool file
	 *  6. Create the spool file
	 *  
	 *  The following items must be present in the binder:
	 *  
	 *  1. processId
	 *  2. correspondentId
	 *  3. company
	 * @throws DataException 
	 */
	public static ByteArrayOutputStream createEthicalFundSpoolFile
	 (DataBinder m_binder, FWFacade facade, String userName) 
	 throws ServiceException, DataException {
		
		Log.debug("Creating Fund Transfer Form");
		
		/*
		String processId		= m_binder.getLocal("processId");
		
		String correspondentId 	= m_binder.getLocal("correspondentId");
		String company		   	= m_binder.getLocal("company");

		if (StringUtils.stringIsBlank(processId) |
			StringUtils.stringIsBlank(correspondentId) |
			StringUtils.stringIsBlank(company))
			throw new ServiceException("Unable to create Transfer form: " +
			 "processId/correspondentId/company missing from binder.");
		
		int corrId				= Integer.parseInt(correspondentId);
		
		Log.debug("Company: " + company + ", correspondent ID: " + corrId);
		
		Correspondent corr =
		 AuroraWebServiceHandler.getCorrespondentByCorrespondentCode(m_binder);
		
		if (corr == null)
			throw new ServiceException("Unable to create Transfer Form: " +
			 "correspondent data was null.");
			
		String templatePath			= correspondentId + "_spool.txt";
		String docName				= m_binder.getLocal("formDocName");
		
		SpoolHeader spoolHeader 	= new SpoolHeader
		 (company, null, userName, 0, null, null, templatePath);

		FundTransferSpoolFileGenerator spoolFileGenerator = 
		 new FundTransferSpoolFileGenerator
		 (spoolHeader, docName, Integer.parseInt(processId), corr);
				
		// Now retrieve all chosen account transfers.
		// First fetch the correspondent process ID.
		String processName 	= m_binder.getLocal("processName");
		Log.debug("Fetching correspondent account transfers for process name: " +
		 processName);
		
		DataResultSet rsFundTransfers =
		 facade.createResultSet
		 ("qClientServices_GetFundTransfersByProcessId",m_binder);
		
		Log.debug("Found " + rsFundTransfers.getNumRows() + 
		 " existing fund transfers.");
		
		if (rsFundTransfers.isEmpty())
			throw new ServiceException("No transfers set for this client.");
		
		// Fetch all account information for this correspondent.
		Account[] accounts = 
		 AuroraWebServiceHandler.getAccountsByCorrespondentCode(m_binder);
		
		Log.debug("Found " + rsFundTransfers.getNumRows() + 
		 " correspondent accounts.");
		
		for (int i=0; i<accounts.length; i++) {
			Log.debug("Account " + i + ": " + 
			 accounts[i].getAccountNumberExternal().trim());
		}

		// Loop through fund transfers. Ignore any 'no action' transfers
		// and add others to transfer list.
		Vector fundTransfers = new Vector();
		
		// Used to store distinct list of client numbers
		HashSet clientSet		= new HashSet();
		
		do {
			String transferType = 
			 rsFundTransfers.getStringValueByName("TRANSFER_TYPE");
			
			if (transferType.equals("none"))
				continue;
			
			String clientId			= rsFundTransfers.getStringValueByName("CLIENT_ID");
			clientId				= CCLAUtils.padClientNumber(clientId);
			
			String accNumberExt 	= clientId +
			 rsFundTransfers.getStringValueByName("ACCOUNT_NUMBER");
			
			String fundCode 		= rsFundTransfers.getStringValueByName("FUND");
			
			Log.debug("Adding account transfer: type=" + transferType + 
			 ", clientId=" + clientId + ", accNumberExt=" + accNumberExt + 
			 ", fund=" + fundCode);
			
			double numberOfUnits 	= 0;
			String accountName		= null;
			
			// Fetch full info for this transfer account
			Account thisAccount = AuroraWebServiceHandler.
			 getAccountByAccountNumberExternal(accounts, accNumberExt);
			
			accountName			= thisAccount.getSubtitle();
			
			if (transferType.equals("amount")) {
				// Account marked for specific amount of units to transfer
				numberOfUnits = Double.parseDouble(
				 rsFundTransfers.getStringValueByName("TRANSFER_AMOUNT"));
			} else if (transferType.equals("undecided")) {
				// Account marked for client decision (i.e. leave form entry blank)
				numberOfUnits = 0;
				
			} else {
				// Account marked for transfer of all units.
				// Determine how many units are in the account
				
				if (thisAccount == null)
					throw new ServiceException(
					 "Unable to find correspondent account: " + accNumberExt);
				
				numberOfUnits 	= thisAccount.getUnits();
			}
				
			FundTransferSpoolFileGenerator.FundTransferSpool transfer =
			 spoolFileGenerator.createFundTransferSpool();
			
			transfer.clientId			= clientId;
			
			transfer.accountName		= accountName;
			
			transfer.accountNumberExt 	= accNumberExt;
			transfer.transType			= transferType;
			
			transfer.numberOfUnits		= numberOfUnits;
			
			fundTransfers.add(transfer);
			clientSet.add(clientId);
			
		} while (rsFundTransfers.next());
		
		if (fundTransfers.isEmpty())
			throw new ServiceException("No eligible transfers to display on form.");
		
		// Determine whether or not to print AML cover letter. Loop through
		// list of clients with eligible accounts and check AML status for each.
		boolean printAmlLetter = false;
		
		Iterator clientIter = clientSet.iterator();
		Log.debug("Determining AML status of " + clientSet.size() + 
		 " associated clients");

		while (clientIter.hasNext() && !printAmlLetter) {
			String thisClientId		= (String)clientIter.next();
			
			DataResultSet drs 	= CCLAUtils.getClientAMLStatus(thisClientId);
			
			if (!drs.isEmpty()) {
				String amlStatus	= drs.getStringValue(0);
				
				if (amlStatus.equals("0")) {
					Log.debug("Found a client requiring AML check: " + 
					 thisClientId);
					printAmlLetter = true;
				}
			}
		}
		
		if (!printAmlLetter)
			Log.debug("No associated clients require AML check");
		
		
//		ByteArrayOutputStream outputStream =
//		 FundTransferSpoolFileGenerator.createFundTransferSpool
//		 (header, true, printAmlLetter, fundTransfers, m_workspace);
//		
		
		spoolFileGenerator.setTransfers(fundTransfers);
		// Set covering letter AML notification
		spoolFileGenerator.setAmlNotify(printAmlLetter);
		
		*/
		
		ByteArrayOutputStream outputStream = null;
		// spoolFileGenerator.createSpoolFile();
		
		return outputStream;
	}
	
	
	/* @deprecated needs to be refactored to use printForm service method.
	 * 
	 * Takes the dDocName for an item which should have a CreateForm
	 * spool file as its primary rendition. This file is passed to
	 * the Aurora Form printer device (specified in an env. var)
	 * 
	 * If a fileName value is present in the binder, the service will
	 * attempt to print this file directly.
	 * 
	 * A processId must be in the binder in order to log the print
	 * action in the Client Services activity log.
	 */
	public void printEthicalFundForm(DataBinder m_binder, FWFacade facade) 
     throws ServiceException, DataException {
		
		// Check if printing is enabled via env. variable
		boolean enablePrinting = !StringUtils.stringIsBlank(
		 SharedObjects.getEnvironmentValue("AURORA_EnableFormPrinting"));
		
		if (!enablePrinting)
			throw new ServiceException("Form printing is currently disabled.");
		
		String docName = m_binder.getLocal("formDocName");
		File spoolFile = null;
		
		boolean hasFile = !StringUtils.stringIsBlank(m_binder.getLocal("fileName"));
		
		if (!hasFile) {
			Log.debug("Printing form with dDocName: " + docName);
		} else {
			Log.debug("Printing form from file: " + m_binder.getLocal("fileName"));
		}
		
		String processId	= m_binder.getLocal("processId");
		String clientId		= m_binder.getLocal("clientId");
		
		String fileName = null;

		try {
			String auroraFormPrinter = 
			 SharedObjects.getEnvironmentValue("AURORA_FormPrinterAddress");
			
			if (hasFile)
				fileName = m_binder.getLocal("fileName");
			else {
				String spoolFileTempAddress =
				 SharedObjects.getEnvironmentValue("AURORA_SpoolFileTempAddress");
				
				File spoolFileAddress = new File(spoolFileTempAddress);
				
				LWDocument lwDoc = new LWDocument(docName, true);
				
				// Fetch primary content and output to temp file
				ByteArrayOutputStream outputStream = lwDoc.getLatestContent();
				
				spoolFile 	= File.createTempFile(docName + "_spool", ".txt", spoolFileAddress);
				FileOutputStream fileOutputStream = new FileOutputStream(spoolFile);
				
				outputStream.writeTo(fileOutputStream);
				fileName 	= spoolFile.getAbsolutePath();
			}
			
			Log.debug("Printing spool file " + fileName + " via CreateForm device: " 
			 + auroraFormPrinter);
			
			// Delegate execution of print command to separate Thread
			FormPrinter formPrinter = new FormPrinter(auroraFormPrinter, fileName);
			formPrinter.start();
			
			if (!StringUtils.stringIsBlank(processId)) {
				// Add activity log to Client Services process, indicating form
				// was printed.
				/*
				addOrUpdateActivity(processId, null, m_userData.m_name, 
				 "Form Print", "Fund Transfer confirmation printed", 
				 null, clientId, null, null, facade);
				*/
				// Set sent date for all printed forms in CCLA_FORM_MAP
				//Form.setSentDate(m_workspace, processId, new Date());
			}
			
		} catch (Exception e) {
			Log.error("Unable to print ethical fund form: " + e,e);
			throw new ServiceException("Unable to print ethical fund form: " + e, e);
		} finally {
			
			// Delete the temp spool file
			if (spoolFile != null) {
				//Log.debug("Deleting spool file: " + fileName);
				//spoolFile.delete();
			}
		}
	}
}
