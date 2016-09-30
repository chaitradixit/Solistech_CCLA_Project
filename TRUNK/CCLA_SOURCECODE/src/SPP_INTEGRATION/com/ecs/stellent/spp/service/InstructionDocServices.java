package com.ecs.stellent.spp.service;

import idcbean.data.LWDataBinder;
import idcbean.data.LWResultSet;
import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.data.ResultSet;
import intradoc.data.Workspace;
import intradoc.server.Service;
import intradoc.shared.SharedObjects;

import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import com.ecs.stellent.auditlog.AuditUtils;
import com.ecs.stellent.iris.batch.BatchDocumentServices;
import com.ecs.stellent.spp.data.SPPJobProfile;
import com.ecs.stellent.spp.fundprocessdetails.FundProcessDetailsManager;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals;
import com.ecs.ucm.ccla.Globals.UCMDocTypes;
import com.ecs.ucm.ccla.Globals.UCMFieldNames;
import com.ecs.ucm.ccla.data.DocumentClass;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.DateFormatter;
import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.LWFacade;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Service methods and utilities for handling of instruction/child
 *  documents.
 *  
 *  Instruction document: Usually a banking instruction of some kind that requires an SPP
 *  job to be started for it. Can only be added to docs with xDocumentClass of APPs/MANDS(?)
 *  
 *  Multidoc - only added to content with an xDocumentClass value which is listed in the
 *  environment var CCLA_multiDocClasses.
 *  
 * @author Tom Marchant
 *
 */
public class InstructionDocServices extends Service{
	
	private static boolean SYNCHRONOUS_CHILD_DOC_CHECKIN = false;
	
	/** Cached array of meta data fields which are copied from parent
	 *  to child item, when the child item is checked in.
	 */
	private static String[] INHERITED_CHECKIN_PARENT_META = null;
	
	/** Cached array of meta data fields which are copied from parent
	 *  to child items, when the parent item is updated.
	 */
	private static String[] INHERITED_UPDATE_PARENT_META = null;
	
	private LWFacade facade = new LWFacade();
	
	/** Persists the instruction/multi doc metadata from the popup window to UCM
	 *  custom table tWithInstructionDocs.
	 *  
	 *  TM: added a fix to allow the user to delete all existing instruction
	 *  rows.
	 *  
	 *  1. Construct insert SQL statement to update the tWithInstructionDocs
	 *     table.
	 *  2. Delete any existing rows that may contain old metadata for docs.
	 *  3. Insert new rows with new metadata. 
	 * 
	 * @throws ServiceException
	 * @throws DataException 
	 */
	public void writeInstructionDocs() throws ServiceException, DataException{
		int numDocs = Integer.parseInt(m_binder.getLocal("rowTotal"));
		String parentDocName = m_binder.getLocal("dDocName");
		
		Log.info("CCLA SOW6: Persisting instruction rows for " + parentDocName +
		 " (found " + numDocs + " rows)");
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, false);
		
		//Construct insert SQL statement to update the tWithInstructionDocs table.
		String sql = "";
		
		// Delete all existing pending child docs first.
		deletePendingChildDocs(parentDocName, facade);
		
		for(int i=1; i<=numDocs;i++){
			String docClass = m_binder.getLocal("docClass" + i);
			
			if (StringUtils.stringIsBlank(docClass))
				continue; // Ignore rows with no Doc Class value set.
		
			addPendingChildDoc(
				parentDocName, 
				docClass, 
				m_binder.getLocal("docAccount" + i), 
				m_binder.getLocal("docAmount" + i),
				m_binder.getLocal("docClientNo" + i), 
				m_binder.getLocal("docFund" + i), 
				m_binder.getLocal("docUnits" + i), 
				facade, m_userData.m_name
			);
			
			/*
			if(m_binder.getLocal("docClass" + i).length() > 0){
				sql += "INTO TWITHINSTRUCTIONDOCS(DDOCNAME,CLASS,ACCOUNT,AMOUNT,CLIENTNO,FUND,UNITS) VALUES ";
				sql += "(";
				sql += "'" + m_binder.getLocal("dDocName") + "'";
				sql += ",'" + m_binder.getLocal("docClass" + i) + "'";
				sql += ",'" + m_binder.getLocal("docAccount" + i) + "'";
				sql += ",'" + m_binder.getLocal("docAmount" + i) + "'";
				sql += ",'" + m_binder.getLocal("docClientNo" + i) + "'";
				sql += ",'" + m_binder.getLocal("docFund" + i) + "'";
				sql += ",'" + m_binder.getLocal("docUnits" + i) + "'";
				sql += ")";
			}
			*/
		}

		try {

		} catch (Exception e) {
			Log.error("Unable to insert pending Child Docs: + " + e.getMessage(), e);
			throw new ServiceException(e);
		}
	}
	
	/** Adds a single entry to the pending ChildDocuments docs table (PENDING_CHILDDOCS,
	 *  previously called TWITHINSTRUCTIONDOCS)
	 *  
	 *  Key is automatically set.
	 *  
	 *  No entries are deleted.
	 *  
	 * @param baseDocName
	 * @param docClass
	 * @param accNum
	 * @param amount
	 * @param clientId
	 * @param fund
	 * @param units
	 * @param facade
	 * @throws DataException
	 */
	public static void addPendingChildDoc
	 (String baseDocName, String docClass, String accNum, 
	  String amount, String clientId, String fund, String units, 
	  FWFacade facade, String userName) 
	  throws DataException {
		
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryParamToBinder(binder, "parentDocName", baseDocName);
		CCLAUtils.addQueryParamToBinder(binder, "docClass", docClass);
		CCLAUtils.addQueryParamToBinder(binder, "accNum", accNum);
		CCLAUtils.addQueryParamToBinder(binder, "amount", amount);
		CCLAUtils.addQueryParamToBinder(binder, "clientId", clientId);
		CCLAUtils.addQueryParamToBinder(binder, "fund", fund);
		CCLAUtils.addQueryParamToBinder(binder, "units", units);
		CCLAUtils.addQueryParamToBinder(binder, "addedBy", userName);
		
		facade.execute("qAddPendingChildDoc", binder);
	}
	
	/** Sets a pending child doc row to completed state.
	 * 
	 * @param id
	 * @param genDocName
	 * @throws DataException 
	 */
	public static void setPendingChildDocGenerated
	 (int id, String genDocName, FWFacade facade) throws DataException {
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder(binder, "id", id);
		CCLAUtils.addQueryParamToBinder(binder, "genDocName", genDocName);
		
		facade.execute("qSetPendingChildDocGenerated", binder);
	}
	
	/** For a given fund transfer item (e.g. CLIENTCONF), this method
	 *  will fetch all associated fund transfers from the CS_FUND_TRANSFERS
	 *  table and check them in as ChildDocuments storing account information.
	 *  
	 * @throws ServiceException
	 */
	public void checkinTransferDocs(String docName, String formId, String clientNum,
			Workspace workspace) throws ServiceException {
		
		Log.debug("Checking in transfer docs for formId:" + formId + " and clientNumber: "
				+clientNum);
		
		FWFacade facade = null;
		DataResultSet rsTransfers = null;
		
		try {
			facade = CCLAUtils.getFacade(m_workspace,false);
			
			rsTransfers = getFundTransfersByFormId(facade, formId, clientNum);
			
			if (rsTransfers.isEmpty()) {
				Log.warn("No transfers found for form ID: " + formId +
				 ". No child documents will be checked in.");
				return;
			}
			
			Log.debug("Found " + rsTransfers.getNumRows() + " transfers");
			
			int checkinCounter = 0;
			
			LWDocument parentDoc = new LWDocument(docName, true);
			
			do {
				checkinCounter++;
				LWDataBinder checkInBinder = new LWDataBinder();
				
				String docClass		= parentDoc.getAttribute("xDocumentClass");
				
				//Get custom fields specified by indexer
				String clientId 	= rsTransfers.getStringValueByName("CLIENT_ID");
				String accNumber 	= rsTransfers.getStringValueByName("ACCOUNT_NUMBER");
				String fund 		= rsTransfers.getStringValueByName("FUND");
				
				String transferType		= rsTransfers.getStringValueByName("TRANSFER_TYPE");
				String transferAmount 	= rsTransfers.getStringValueByName("TRANSFER_AMOUNT");
				
				checkInBinder.putLocal("xClientNumber", padClientNumber(clientId));
				checkInBinder.putLocal("xAccountNumber", accNumber);
				checkInBinder.putLocal("xFund", fund);
				
				checkInBinder.putLocal("xDocumentClass", docClass);
				
				checkInBinder.putLocal("xComments", "Transfer type: " + transferType +
				 "\nProcess ID: " + rsTransfers.getStringValueByName("PROCESS_ID") +
				 "\nTransfer ID: " + rsTransfers.getStringValueByName("TRANSFER_ID"));
				
				if (!StringUtils.stringIsBlank(transferAmount))
					checkInBinder.putLocal("xUnits", transferAmount);
				
				checkInBinder.putLocal("xFormId", formId);
				checkInBinder.putLocal("xBatchNumber", formId);
				
				//String envId = parentDoc.getAttribute("xBundleRef");
				//checkInBinder.putLocal("xBundleRef", envId);
				
				// TM: added document class to sub-doc's title.
				checkInBinder.putLocal("dDocTitle", parentDoc.getAttribute("dDocTitle")
				 + "_" + docClass + "_Transfer_"+checkinCounter);
				
				checkInBinder.putLocal("createAlternateMetaFile", "false");
				checkInBinder.putLocal("createPrimaryMetaFile", "true");
				checkInBinder.putLocal("primaryFile", "");
				checkInBinder.putLocal("dSecurityGroup", "Public");
				checkInBinder.putLocal("dDocType", "ChildDocument");
				checkInBinder.putLocal("xParentDocName", docName);
				checkInBinder.putLocal("xPdfDocName", 
						SppIntegrationUtils.getPdfDocName(parentDoc));
				
				// Add 'archived' status to prevent these items appearing
				// as errors in the content reports.
				checkInBinder.putLocal("xStatus", "Archived");
				
				checkInBinder.putLocal("IdcService", "CHECKIN_NEW");
				
				try {
					//Execute the checkin binder for the child doc
					LWDataBinder checkinBinder = this.facade.executeService(checkInBinder);
					Log.info("CCLA SOW6: Checked in child document " + checkinCounter + 
					 " (" + checkinBinder.getLocal("dDocName") + ")");

				} catch (Exception e) {
					Log.error("Failed to checkin transfer ChildDocument: " + e, e);
				}
			} while (rsTransfers.next());
			
			Log.debug("Added " + checkinCounter + " transfer ChildDocuments.");
			
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	/** Used to pad client numbers with length < 5 with extra zeros,
	 *  so they always appear as 5 characters in length.
	 *  
	 *  Copy of method in CCLA_ClientServices, cant reference that
	 *  project due to circular reference.
	 *  
	 * @param clientNumber
	 * @return
	 */
	public static String padClientNumber(String clientNumber) {
		
		if (clientNumber.length() < 5) {
			String paddedClientNumber = new String(clientNumber);
			
			int padLength = 5 - clientNumber.length();
			
			for (int i=0; i<padLength; i++)
				paddedClientNumber = "0" + paddedClientNumber;
			
			return paddedClientNumber;
			
		} else {
			return clientNumber;
		}
	}
	
	/** Fetches a set of fund transfers which relate to a given form ID.
	 *  
	 *  Should be in CCLA_ClientServices codebase but this causes a 
	 *  cyclical reference between the 2 projects.
	 *  
	 * @param facade
	 * @return list of fund transfers
	 */
	public static DataResultSet getFundTransfersByFormId
	 (FWFacade facade, String formId, String clientId) throws DataException {
		
		DataBinder binder = new DataBinder();
		binder.putLocal("formId", formId);
		binder.putLocal("clientId", clientId);
		
		// Query resides in CCLA_ClientServices component
		DataResultSet transfers =
		 facade.createResultSet("qClientServices_GetFundTransfersByFormIdAndClientId", 
		 binder);
		
		return transfers;
	}
	
	public static int checkinChildDocs
	 (String docName, DataResultSet rsChildDocs, Workspace ws, String userName) 
	 throws Exception {
		FWFacade facade = CCLAUtils.getFacade(ws);
		return checkinChildDocs(docName, rsChildDocs, facade, userName);
	}
	
	/** Checks in the child documents specified in the given instructionRows ResultSet.
	 *  The passed docName value should correspond to the parent item.
	 *  
	 *  As each new ChildDocument is successfully checked in, the corresponding pending
	 *  child doc record is updated to show its been generated.
	 *  
	 *  The group check-in will be audited as a single action.
	 *  
	 * @param parentDocName
	 * @param rsChildDocs
	 * @param userName
	 * @return number of successfully checked-in child documents
	 * @throws ServiceException
	 */
	public static int checkinChildDocs
	(String parentDocName, DataResultSet rsChildDocs, FWFacade ucmFacade, String userName) 
	throws Exception {
		
		Log.debug("checkinChildDocs: " + parentDocName);
		
		int docCheckinCount = 0; // No. of successful child doc checkins for this document
		
		// Determines whether each child document is checked in synchronously
		// (i.e. wait for each new content item to be released before continuing).
		// If false, the items are checked in asynchronously and the method will block
		// at the end until all items are released.
		boolean synchronousCheckin = !StringUtils.stringIsBlank(
		 SharedObjects.getEnvironmentValue("CCLA_synchronousChildDocCheckin"));

		//If this document has child (instruction/mutli) documents
		if (rsChildDocs != null && rsChildDocs.getNumRows() > 0) {
			
			FWFacade cdbFacade = CCLAUtils.getFacade(true);
			
			Log.info("CCLA: Checking in " + rsChildDocs.getNumRows() + 
			 " pending child documents for " + parentDocName);
			
			// Stores dDocNames for the new check-ins.
			Vector<String> childDocNames = new Vector<String>();
			
			//Get the DOC_INFO for the parent item
			LWDocument parentDoc = new LWDocument(parentDocName, true);
			String pdfDocName = SppIntegrationUtils.getPdfDocName(parentDoc);
			
			// Pull these field values out for auditing later
			String parentDocTitle 	= parentDoc.getTitle();
			String withInstr		= parentDoc.getAttribute("xWithInstruction");
			
			boolean isInstructionDocs = 
			 (!StringUtils.stringIsBlank(withInstr) && withInstr.equals("Yes"));

			int childDocCounter = 0; // stores current row number of child docs ResultSet
			
			/*For each child document - 
			 * 1. Get the custom fields, i.e. class, account and amount.
			 * 2. Create a new LWDocument instance for each child doc, with a reference to 
			 *    it's parent.
			 * 3. Check it in.
			 */
			do {
				// Fetch the Pending Child Doc ID for this record. It may not exist
				// if the passed ResultSet was generated in code.
				Integer pendingChildDocId = CCLAUtils.getResultSetIntegerValue
				 (rsChildDocs, "PENDING_CHILDDOC_ID");
				
				childDocCounter++;
				LWDocument childDoc = new LWDocument();
				childDoc.useDatabase();
				
				//Get custom fields specified by indexer
				String docClass 		= rsChildDocs.getStringValueByName("CLASS");
				String clientAccNumber 	= rsChildDocs.getStringValueByName("ACCOUNT");
				String amount 			= rsChildDocs.getStringValueByName("AMOUNT");
				String clientNo 		= rsChildDocs.getStringValueByName("CLIENTNO");
				String fund 			= rsChildDocs.getStringValueByName("FUND");
				String units 			= rsChildDocs.getStringValueByName("UNITS");
				
				String destAcc 			= rsChildDocs.getStringValueByName("DESTACCNUMEXT");
				String destFund 		= rsChildDocs.getStringValueByName("DESTFUND");
				
				// Need to grab the source from the parent item later (if available)
				// to use in the Process Date calculation
				String source = null;
				
				DocumentClass documentClass = null;
				
				if (!StringUtils.stringIsBlank(docClass))
					documentClass = DocumentClass.getCache().getCachedInstance(docClass);
				
				// Build the child document title.
				String childDocTitle = parentDocTitle
				 + "_" + docClass + "_SubDoc"+childDocCounter;
				
				// Check it doesn't exceed maximum permitted length
				if (childDocTitle.length() > 80) {
					// Remove the parentDocTitle
					childDocTitle = docClass + "_SubDoc"+childDocCounter;
				}
				
				childDoc.setAttribute("dDocTitle", childDocTitle);

				childDoc.setAttribute("createAlternateMetaFile", "false");
				childDoc.setAttribute("createPrimaryMetaFile", "true");
				childDoc.setAttribute("primaryFile", "");
				childDoc.setAttribute(UCMFieldNames.DocType, UCMDocTypes.ChildDocument);
				childDoc.setAttribute(UCMFieldNames.DocAuthor, userName);
				childDoc.setAttribute(UCMFieldNames.ParentDocName, parentDocName);
				
				// Only set the DependantDocName field if we are creating 'Instruction'
				// docs (as opposed to 'Multi-Docs' and this isn't a Supporting Doc
				// class.
				if (documentClass!=null 
					&& !documentClass.isSupporting() 
					&& isInstructionDocs) 
					childDoc.setAttribute(UCMFieldNames.DependantDocName, parentDocName);
				
				childDoc.setAttribute(UCMFieldNames.PdfDocName, pdfDocName);

				// Load fields that the child documents inherit from the parent document.
				if (INHERITED_CHECKIN_PARENT_META == null) {
					// Fetch and cache the list of fields to be inherited on checkin.
					// (Can be empty/null)
					String fieldList = 
					 SharedObjects.getEnvironmentValue("SPP_INT_CHILD_META_INHERITED");
					
					if (StringUtils.stringIsBlank(fieldList))
						INHERITED_CHECKIN_PARENT_META = new String[0];
					else
						INHERITED_CHECKIN_PARENT_META = StringUtils.stringToArray(fieldList);
				}
				
				//Set child document metadata inherited from parent
				for (int k=0; k<INHERITED_CHECKIN_PARENT_META.length; k++) {
					childDoc.setAttribute(INHERITED_CHECKIN_PARENT_META[k], 
					 parentDoc.getAttribute(INHERITED_CHECKIN_PARENT_META[k]));
					
					if (INHERITED_CHECKIN_PARENT_META[k].equals(UCMFieldNames.Source))
						source = parentDoc.getAttribute
						 (INHERITED_CHECKIN_PARENT_META[k]);
				}
				
				// If the client acc number is non-null and the fund value is null, 
				// extract the fund code if possible
				if (!StringUtils.stringIsBlank(clientAccNumber) 
					&& StringUtils.stringIsBlank(fund)) {
					
					// Get up to the last 3 A-Z characters of account number
					String accountNoSuffix = SppIntegrationUtils.getSuffixChars(clientAccNumber);
				
					if (accountNoSuffix.length() > 0) {						
						childDoc.setAttribute(Globals.UCMFieldNames.Fund, accountNoSuffix);
						Log.info("Setting xFund field for child doc to " + accountNoSuffix + "" +
						 " (account number " + clientAccNumber + ")");
					} else {
						Log.info("xFund field not set for account number " + clientAccNumber);
					}
				}
				
				childDoc.setAttribute(Globals.UCMFieldNames.DocClass, docClass);
				childDoc.setAttribute(Globals.UCMFieldNames.AccountNumber, clientAccNumber);
				childDoc.setAttribute(Globals.UCMFieldNames.Amount, amount);
				childDoc.setAttribute(Globals.UCMFieldNames.ClientNumber, clientNo);
				childDoc.setAttribute(Globals.UCMFieldNames.Fund, fund);
				childDoc.setAttribute(Globals.UCMFieldNames.Units, units);
				
				//Added destination fund and destination account.
				if (!StringUtils.stringIsBlank(destFund))
					childDoc.setAttribute(Globals.UCMFieldNames.DestinationFund, destFund);
				
				if (!StringUtils.stringIsBlank(destAcc))
					childDoc.setAttribute(Globals.UCMFieldNames.DestinationAccount, destAcc);
				
				// Calculate a process date
				Date processDate = FundProcessDetailsManager.getDealingDate(
					fund, 
					destFund, 
					docClass, 
					null, 
					false, 
					source, 
					new Date(), 
					false, 
					cdbFacade
				);
				
				if (processDate != null) {
					String processDateStr = DateFormatter.getTimeStamp(processDate);
					
					childDoc.setAttribute(UCMFieldNames.OriginalProcessDate, 
					 processDateStr);
					childDoc.setAttribute(UCMFieldNames.ProcessDate, 
					 processDateStr);
				}
				
				try {
					// Execute the checkin action on the LWDocument instance
					String childDocName = childDoc.checkin(null, synchronousCheckin);
					
					// Update the Pending Child Doc record to show its been generated
					if (pendingChildDocId != null)
						setPendingChildDocGenerated
						 (pendingChildDocId, childDocName, ucmFacade);
					
					// Save a list of all new dDocName values, these are queried at the
					// end to ensure all child documents have completed check-in before
					// continuing.
					childDocNames.add(childDocName);
					
					Log.info("CCLA: Checked in child document " + childDocName);
					docCheckinCount++;
					
				} catch(Exception e) {
					Log.error("CCLA: Unable to check in child document " + childDocCounter + 
					 ": " + e.getMessage(), e);
					
					throw new ServiceException("Unable to check in child document: " 
					 + e.getMessage(), e);
				}
				
			} while (rsChildDocs.next());

			// Remove child doc rows from table now that all child docs have been 
			// checked in.
			if (ucmFacade!=null)
				deletePendingChildDocs(parentDocName, ucmFacade);
			
			// Audit child doc checkins for this single item.
			Vector<String> params = new Vector<String>();
			params.add(Integer.toString(docCheckinCount));
			
			String auditId = null;
			String auditMsg = null;

			// If the parent document of the child docs has xWithInstruction=Yes 
			// DJ: I think this means that the child docs are all Instruction docs 
			// also?
			if (isInstructionDocs && docCheckinCount > 0) {
				auditId = "CHECKIN-INSTR-DOCS";
				auditMsg = docCheckinCount + " instruction documents created";
			} else if(docCheckinCount > 0) {
				auditId = "CHECKIN-MULTI-DOCS";
				auditMsg = docCheckinCount + " multi documents created";
			}
			
			if(auditId != null && auditMsg != null){
			AuditUtils.addAuditEntry("IRIS", auditId, 
					parentDocName, 
					parentDocTitle, 
					userName,
					auditMsg,
					params);
			}
			
			// If asynchronous checkins were used, the method must block until
			// all items are released.
			if (!synchronousCheckin) {
				
				// Build a comma-separated, delimited list of the new dDocNames
				// for use in the SQL IN clause.
				StringBuffer docNames = new StringBuffer();
				
				int numDocs = childDocNames.size();
				
				for (int i=0; i<numDocs; i++) {
					if (docNames.length() > 0)
						docNames.append(",");
					docNames.append("'" + (String)childDocNames.get(i) + "'");
				}
				
				Log.debug("Checking database for readiness of " + numDocs + 
				 " new content items.");
				
				String sql = "SELECT dReleaseState FROM Revisions r " +
				 "INNER JOIN DocMeta dm ON (r.dID = dm.dID) " +
				 "WHERE r.dDocName IN (" + docNames + ") " +
				 "AND r.dReleaseState = 'Y'";
				
				Log.debug("Used query: " + sql);
				
				int numAttempts = 0;
				
				do {
					Thread.sleep(2000);
					
					numAttempts++;
					Log.debug("Attempt #" + numAttempts + "...");
					DataResultSet rs = ucmFacade.createResultSetSQL(sql);
					
					if (rs.getNumRows() >= numDocs) {
						Log.debug("All child content items released! Required " 
						 + numAttempts + " attempts.");
						break;
					} else {
						Log.debug(rs.getNumRows() + "/" + numDocs + " items ready.");
						
						if (numAttempts >= 20) {
							Log.debug("Max attempts exceeded. Continuing anyway.");
							break;
						}
					}
					
				} while (true);
			}

		} else {
			Log.debug("CCLA SOW6: No child documents found for " + parentDocName);
		}
		
		return docCheckinCount;
	}
	
	/** Service-accessible method for static checkinChildDocs. Kept for legacy
	 *  purposes.
	 *  
	 * @throws ServiceException
	 */
	public void checkinWithInstructionDocs() throws ServiceException, DataException {
		String docName = m_binder.getLocal("docName");
		
		DataResultSet rsChildDocs = getPendingChildDocs(docName, m_workspace);
		
		try {
			checkinChildDocs(docName, rsChildDocs, m_workspace, m_userData.m_name);
		} catch (Exception e) {
			Log.error("Failed checking in child docs", e);
			throw new ServiceException(e);
		}
	}
	
	/** Iterates through envelope content items, as defined by a bundleRef variable in the
	 *  binder. If an item has their xWithInstruction or xMultiDoc field set to "Yes",
	 *  their accompanying child documents must be created and checked in.
	 *	
	 *	DJ: Different from checkinWithInstructionDocs() which check's in child docs for a single
	 *  content item. This method loops through content items in an envelope and where
	 *  appropriate checks in child docs for relevant envelope document.
	 *  
	 *  A running total of successful checkins are counted for the whole bundle. This
	 *  figure is then saved as a special audit entry.
	 *  
	 * @throws Exception
	 */
	public void checkInChildDocsForEnvelope() throws ServiceException, DataException {	
		String bundleRef = m_binder.getLocal("bundleRef");
		
		Log.info("checkInChildDocsForEnvelope: " + bundleRef + " >>");
		long startTime = System.currentTimeMillis();
		
		DataResultSet rsBatchItems = 
		 BatchDocumentServices.getBatchItems(bundleRef, m_workspace);
		
		int totalChildDocCheckins = 0;
		
		if (rsBatchItems != null && rsBatchItems.getNumRows() > 0) {
			Log.info("CCLA: Got " + rsBatchItems.getNumRows() + " results");
			
			do {
				//Get vars for this content item in the envelope
				String withInstruction = 
				 rsBatchItems.getStringValueByName("xWithInstruction");
				String multiDoc = 
				 rsBatchItems.getStringValueByName("xMultiDoc");
				String docName = 
				 rsBatchItems.getStringValueByName("dDocName");
				
				Log.info("CCLA: xWithInstruction for " + docName + " is " + withInstruction);
				Log.info("CCLA: xMultiDoc for " + docName + " is " + multiDoc);
				
				// If the content item in the envelope is either Multi-doc or With Instruction,
				// get the child docs and (rsWithInstructionDocs) and check in the child docs
				if (withInstruction.equalsIgnoreCase("Yes") || 
					multiDoc.equalsIgnoreCase("Yes")) {
					
					DataResultSet rsChildDocs = getPendingChildDocs(docName, m_workspace);
					
					Log.info("CCLA: " + docName + " has " + rsChildDocs.getNumRows() 
					 + " pending child docs.");
					
					if (rsChildDocs.getNumRows() > 0) {
						// If there are child docs for this envelope item, check them in
						// and remove their rows from the child doc table.
						try {
							int childDocCheckinCount = checkinChildDocs
							 (docName, rsChildDocs, m_workspace, m_userData.m_name);
							
							totalChildDocCheckins += childDocCheckinCount;
					
						} catch (Exception e) {
							Log.error("Failed checking in child docs", e);
							throw new ServiceException(e);
						}
					}
				}
				
			} while (rsBatchItems.next());
			
			if (totalChildDocCheckins > 0) {
				String checkinCount = Integer.toString(totalChildDocCheckins);
				
				// Audit batch instruction doc checkin.
				// param1: total child docs generated
				// param2: total child docs submitted to SPP (no longer applicable here)
				
				/*
				String sppSubmitCount = m_binder.getLocal("childDocSppSubmitCount");
				if (StringUtils.stringIsBlank(sppSubmitCount))
					sppSubmitCount = "0";
				*/
				
				Vector params = new Vector();
				params.add(checkinCount);
				
				// Fetch data for parent batch item, for use in auditing
				DataResultSet parentItem = 
				 BatchDocumentServices.getParentBatchItem(bundleRef,
				 BatchDocumentServices.getFWFacade(m_workspace));
				
				AuditUtils.addAuditEntry("IRIS", "CHECKIN-BUNDLE-CHILD-DOCS", 
										 parentItem.getStringValueByName("dDocName"), 
										 parentItem.getStringValueByName("dDocTitle"), 
										 m_userData.m_name,
										 checkinCount + 
										 " instruction/multi documents created",
										 params);
			}
			
			m_binder.putLocal("totalChildDocCheckins", totalChildDocCheckins + "");
			
			Log.info("checkInChildDocsForEnvelope << bundle: " + bundleRef + 
			 ", no. checkins: " + totalChildDocCheckins + 
			 ", time taken: " + ((System.currentTimeMillis()-startTime)/1000D) + 
			 "s");
			
		} else {
			Log.info("Bundle was empty.");
		}
	}
	
	/** Fetches all rows in the pending instruction documents table for the given
	 *  parent doc name, which are yet to generated Child Documents.
	 *  
	 * @param docName
	 * @param ws
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getPendingChildDocs(String docName, Workspace ws)
	 throws DataException {
		DataBinder binder = new DataBinder();
		
		binder.putLocal("parentDocName", docName);
		ResultSet rs = 
		 ws.createResultSet("qGetPendingChildDocs", binder);
		
		DataResultSet rsChildDocs = new DataResultSet();
		rsChildDocs.copy(rs);
		
		return rsChildDocs;
	}
	
	/** Deletes all instruction entries for the given parent dDocName, that haven't
	 *  generated Child Documents yet. Called before adding/updating the existing set
	 *  of pending Child Documents.
	 *  
	 * @param docName
	 * @param ws
	 * @throws Exception
	 */
	public static void deletePendingChildDocs(String docName, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		binder.putLocal("parentDocName", docName);
		
		facade.execute("qDelPendingChildDocs", binder);
	}
	
	/** Cascades metadata updates from parent items down to any existing
	 *  child documents.
	 *  
	 *  This service method is appended to the UPDATE_DOCINFO core services,
	 *  so it will always be executed for every document update.
	 *  
	 *  The item being updated must have its xWithInstruction flag set to
	 *  "Yes" before the method checks for the presence of any child docs.
	 *  
	 *  Only those fields specified by the SPP_INT_CHILD_META_INHERITED_ON_UPDATE
	 *  environment variable will be updated.
	 *  
	 * @throws ServiceException
	 */
	public void propagateChildDocInfo() throws ServiceException {

		//DJ: Is this right? I thought xWithInstruction=Yes was set on child docs also.
		//Wouldn't technically matter 
		String isParentDoc = m_binder.getLocal("xWithInstruction");

		if(isParentDoc != null && isParentDoc.equalsIgnoreCase("Yes")){
			String docName = m_binder.getLocal("dDocName");

			if (INHERITED_UPDATE_PARENT_META == null) {
				// Fetch and cache the list of fields to be inherited.
				// (Can be empty/null)
				
				String fieldList = 
				 m_binder.getEnvironmentValue("SPP_INT_CHILD_META_INHERITED_ON_UPDATE");
				
				if (StringUtils.stringIsBlank(fieldList))
					INHERITED_UPDATE_PARENT_META = new String[0];
				else
					INHERITED_UPDATE_PARENT_META = StringUtils.stringToArray(fieldList);
			} 
			
			if (INHERITED_UPDATE_PARENT_META.length == 0) {
				Log.info("Parent->Child update propagation: " +
				 "No metadata fields set for inherited update.");
				return;
			}
				
			try {
				Log.info("CCLA: Propagating update metadata for " 
				 + docName + " to child documents.");
				
				LWDataBinder searchBinder = new LWDataBinder();
				
				//DJ: Should query below have (<and> dDoctype <matches> `Document`) in it?
				searchBinder.putLocal("QueryText", "xParentDocName <matches> `" + docName + "`");
				searchBinder.putLocal("ResultCount", "200");
				searchBinder.putLocal("IdcService", "GET_SEARCH_RESULTS");
				
				LWResultSet rsSearchResults = facade.executeService(searchBinder, "SearchResults");
				
				if (rsSearchResults != null && rsSearchResults.getNumRows() > 0) {
					Log.info("CCLA: Query \"xParentDocName <matches> `" + docName + "`\" found " 
							+ rsSearchResults.getNumRows() + " child documents.");

					do{
						String childDocName = 
						 rsSearchResults.getStringValue(rsSearchResults.getFieldIndex("dDocName"));
						
						LWDocument lwChildDoc = new LWDocument(childDocName, true);
						
						for(int i=0; i<INHERITED_UPDATE_PARENT_META.length; i++){
							lwChildDoc.setAttribute(INHERITED_UPDATE_PARENT_META[i], 
							 m_binder.getLocal(INHERITED_UPDATE_PARENT_META[i]));
						}
						
						Log.info("CCLA SOW6: Updating metadata of child doc " + childDocName + "...");
						lwChildDoc.update();
						
					} while (rsSearchResults.next());
					
					Log.info("CCLA SOW6: Finished propagating metadata of child documents");
				}else{
					Log.info("CCLA SOW6: No child docs found");
				}
			}catch(Exception e){
				Log.error("Unable to propagate metadata to children: "+e.getMessage(),e);
				throw new ServiceException(e);
			}
					
		}
	}
	
	/** Web service method used by SPP. Used to generate a new ChildDocument
	 *  and immediately trigger an SPP job for it.
	 *  
	 *  Expects documentId and fileTitle parameters in the binder.
	 *  
	 *  The documentId will translate to a dID of a supporting document in UCM.
	 *  The new item will share the content of this item.
	 *  
	 * @throws ServiceException 
	 */
	public void createNewJob() throws ServiceException {
		
		String documentId 	= m_binder.getLocal("documentId");
		String fileTitle	= m_binder.getLocal("fileTitle");
		
		ResultSet metaData 	= m_binder.getResultSet("metaData");
		
		if (StringUtils.stringIsBlank(documentId)) {
			throw new ServiceException("documentId was empty/missing, " +
			 "unable to create new item/job.");
		}
		
		int dID = 0;
		
		try {
			dID = Integer.parseInt(documentId);
		} catch (NumberFormatException e) {
			throw new ServiceException("Passed documentId '" + documentId + 
			 "' was non-numeric.");
		}
		
		try {
			Log.debug("Adding new ChildDocument via SPP web service. " +
			 "Parent dID: " + dID); 
			
			LWDocument parentDoc = new LWDocument(dID, true);
			String docType = parentDoc.getAttribute("dDocType");
			
			String pdfDocName 		= parentDoc.getDocName();
			String parentDocName 	= parentDoc.getDocName();
			
			String wfId				= parentDoc.getAttribute("xBatchNumber");
			
			// If the passed supporting document is a ChildDocument itself,
			// we need to fetch the content info from its metadata fields.
			if (docType.equals("ChildDocument")) {
				pdfDocName = parentDoc.getAttribute("xPdfDocName");
				
				if (StringUtils.stringIsBlank(pdfDocName))
					pdfDocName = parentDoc.getAttribute("xParentDocName");
			}
			
			LWDocument newDoc = new LWDocument();
				
			// Set all passed attribs in the binder
			Hashtable table = new Hashtable();
			Properties localData = m_binder.getLocalData();
			
			Iterator i = localData.entrySet().iterator();
			String[] ignoreValues = new String[] {
			 "dDocName", "dID", "dRevisionsID", "dRevLabel", 
			 "dFormat", "dOriginalName", "dExtension", 
			 "xWorkflowDate", "xJobId"
			};
			
			while (i.hasNext()) {
				Map.Entry entry = (Map.Entry)i.next();
				
				String key 		= (String)entry.getKey();
				String value	= (String)entry.getValue();
				
				boolean ignoreKey = false;
				
				for (int j=0; j<ignoreValues.length;j++) {
					if (key.equals(ignoreValues[j])) {
						Log.debug("Ignoring binder key: " + key);
						ignoreKey = true;
						break;
					}
				}
				
				if (!ignoreKey) {
					Log.debug("Adding binder name-value pair to " + 
					 "LWDocument attributes: " + key + "=" + value);
				
					newDoc.setAttribute(key, value);
				}
			}
			
			// Set required attributes
			newDoc.setTitle(fileTitle);
			newDoc.setSecurityGroup("Public");
			newDoc.setAttribute("dDocAuthor", m_userData.m_name);
			newDoc.setAttribute("dDocType", "ChildDocument");
			
			// Set content link attributes
			newDoc.setAttribute("xParentDocName", parentDocName);
			newDoc.setAttribute("xPdfDocName", pdfDocName);
			
			if (StringUtils.stringIsBlank(wfId) || wfId.equals("0")) {
				// Fetch new workflow ID
				wfId = SppIntegrationUtils.getNextWorkflowId(CCLAUtils.getFacade(m_workspace,true));
				Log.debug("Fetched new workflow ID: " + wfId);
			} else
				Log.debug("Using existing parent workflow ID: " + wfId);
			
			newDoc.setAttribute("xBatchNumber", wfId);
			
			String newDocName = newDoc.checkin(null, true);
			
			Log.debug("New ChildDocument checked in: " + newDocName 
			 + ". Workflow ID: " + wfId + ". Triggering SPP workflow.");
			
			m_binder.putLocal("noRedirectOnSuccess", "1");
			SppIntegrationUtils.triggerSppWorkflow(newDocName, "SPP", 
			 m_binder, SppIntegrationUtils.SINGLE_SUBMISSION, CCLAUtils.getFacade(m_workspace,true),
			 SPPJobProfile.JOB_PROFILE);
			
			Log.debug("Workflow trigger successful: " + newDocName);
			
			m_binder.putLocal("workflowId", wfId);
			
			LWFacade lwFacade = new LWFacade("sysadmin");
			
			
			
		} catch (Exception e) {
			throw new ServiceException("Failed to create new job/item: " 
			 + e.getMessage(), e);
		}
	}
}
