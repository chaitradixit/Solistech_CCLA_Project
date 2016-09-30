package com.ecs.stellent.spp.service;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.data.ResultSetUtils;
import intradoc.server.Service;
import intradoc.shared.SharedObjects;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import com.ecs.stellent.auditlog.AuditUtils;
import com.ecs.stellent.iris.batch.BatchDocumentServices;
import com.ecs.stellent.spp.data.SPPJobProfile;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.DocumentClass;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.embedded.FWFacade;

public class SppIntegrationServices extends Service {
	
	/** Determines whether or not the Process Date field is calculated and
	 *  applied to processed bundle documents.
	 *  
	 */
	public static final boolean APPLY_DOCUMENT_PROCESS_DATE =
	 !StringUtils.stringIsBlank
	 (SharedObjects.getEnvironmentValue("SPP_ApplyDocumentProcessDate"));

	public static final boolean KEEP_ORIGINAL_DATES_FOR_CLONE_DOCUMENTS =
		 !StringUtils.stringIsBlank
		 (SharedObjects.getEnvironmentValue("SPP_KeepOriginalDatesForCloneDocuments"));
	
	public static enum BundleStatus {
		EnterDetails,
		Validation,
		Completed
	}
	
	/** Triggers jobs for the set of items returned by the given query.
	 *  
	 *  Admin-only service method.
	 * 
	 * @throws DataException
	 */
	public void triggerBatchSppWorkflow() throws DataException {
		String sql = m_binder.getLocal("query");
		boolean checkForDate = !StringUtils.stringIsBlank
		 (m_binder.getLocal("checkForDate"));
		
		FWFacade ucmFacade = CCLAUtils.getFacade(m_workspace, false);
		DataResultSet rsDocs = ucmFacade.createResultSetSQL(sql);
		
		SppIntegrationUtils.triggerSppWorkflow(rsDocs, ucmFacade, checkForDate);
	}
	
	public void createNewCaseDocument() throws Exception {
		Log.info("SPP_WSDL: CHECKING IN NEW DOCUMENT...");
		SPPJobProfile workflowProfile = SPPJobProfile.JOB_PROFILE;
		
		//Get fields and mapping
		String[] splitUcmMetadata = workflowProfile.getUcmVarNames();
		String[] splitSppMetadata = workflowProfile.getSppVarNames();

		//Get environmental variables
		String dDocType 		= m_binder.getEnvironmentValue("SPP_INT_CASE_REF_DOCTYPE");
		String dSecurityGroup 	= m_binder.getEnvironmentValue("SPP_INT_CASE_REF_SECURITYGROUP");
		
		// xBatchNumber
		String caseRefField 	= m_binder.getEnvironmentValue("SPP_INT_CASE_REF_FIELDNAME");
		
		//Get Binder variables
		String dDocTitle 	= m_binder.getLocal("title");
		String dDocAuthor 	= m_binder.getLocal("author");
		String caseRef 		= m_binder.getLocal("caseRef");

		//Get the original doc name from binder, e.g. mydoc.doc
		String docFilename = m_binder.getLocal("content");
		String[] docFilenameSplit = docFilename.split("\\\\");
		docFilename = docFilenameSplit[docFilenameSplit.length -1];
		
		//Create a new document filename
		String newDocFilename = DataBinder.getTemporaryDirectory() + docFilename;
		
		LWDocument lwDoc = new LWDocument();
		
		//Set hard coded doc metadata, and other non-extra props params
		lwDoc.setTitle(dDocTitle);
		lwDoc.setAuthor(dDocAuthor);
		lwDoc.setAttribute("dDocType", dDocType);
		lwDoc.setAttribute("dSecurityGroup", dSecurityGroup);
		lwDoc.setAttribute("dOriginalName", docFilename);
		lwDoc.setAttribute(caseRefField, caseRef);
		
		for(int i=0; i<splitUcmMetadata.length; i++) {
			String thisSPPField = splitSppMetadata[i];
			String thisUCMField = splitUcmMetadata[i];
			
			String thisValue = m_binder.getLocal(thisSPPField);
			
			if (thisValue != null) {
				Log.debug("Setting metadata field " + thisUCMField + "=" + thisValue);
				lwDoc.setAttribute(thisUCMField, thisValue);
			}
		}
		
		File doc = new File(m_binder.getLocal("content:path"));

		boolean renameSuccess = doc.renameTo(new File(newDocFilename));
		if(!renameSuccess) 
			Log.info("SPP_WSDL: Unable to rename file from " + doc.getName() 
			 +" to " +newDocFilename);
		
		File docToCheckIn = new File(newDocFilename);
		
		Log.info("SPP_WSDL: Checking in '"+docToCheckIn.getName()
		 +"' of size: "+docToCheckIn.length()/1024+"kb");
		
		String dDocName = lwDoc.checkin(docToCheckIn);
		
		boolean delSuccess =docToCheckIn.delete();
		if(!delSuccess) 
			Log.info("SPP_WSDL: Unable to delete file" + 
			 newDocFilename + " from " + DataBinder.getTemporaryDirectory());
		
		//if (SppIntegrationUtils.SEND_CONTENT_LINKS_WITH_JOB_DATA) {
			//Set the URL to return
			String rtnUrl = "";
			
			String serverHostName = m_binder.getEnvironmentValue("HttpServerAddress");
			String relWebRoot = m_binder.getEnvironmentValue("HttpRelativeWebRoot");			
			rtnUrl = "http://" + serverHostName + relWebRoot + "idcplg?" +
			 "IdcService=CCLA_CP_GET_FILE&GUID=" + dDocName;
			
			m_binder.putLocal("URL", rtnUrl);
		//}
		
		m_binder.putLocal("dDocName", dDocName);
		
		//Return extraResults
		DataResultSet extraResults = new DataResultSet(splitSppMetadata);
		
		Vector v = new Vector();

		Log.info("SPP_WSDL: Found "+ splitUcmMetadata.length + " custom fields");
		
		for(int i=0; i<splitUcmMetadata.length; i++){
			String field = "";
			field = m_binder.getLocal(splitUcmMetadata[i]);
			v.add(field);	
			Log.info("SPP_WSDL: Populating field " + splitUcmMetadata[i] + " with "+ field);
		}
		
		extraResults.addRow(v);
		m_binder.addResultSet("extraResults", extraResults);
		
		Log.info("SPP_WSDL: ...FININISHED CHECK IN");
	} 

	public void getCaseDocumentList() throws DataException {
		Log.info("SPP_WSDL: PERFORMING SEARCH...");
		
		SPPJobProfile workflowProfile = SPPJobProfile.JOB_PROFILE;
		
		//Get fields and mapping
		String[] splitUcmMetadata = workflowProfile.getUcmVarNames();
		String[] splitSppMetadata = workflowProfile.getSppVarNames();
		
		String queryString 	= m_binder.getEnvironmentValue("SPP_INT_GET_CASE_DOCS_QUERY");
		String caseRefField = m_binder.getEnvironmentValue("SPP_INT_CASE_REF_FIELDNAME");
		
		String caseRef = m_binder.getLocal("caseRef");
		
		if(caseRef != null && caseRef.length() > 0)
			queryString = "" + queryString + " <AND> " + caseRefField + " >= `" + caseRef + "`"
										   + " <AND> " + caseRefField + " <= `" + caseRef + "`";		
		
		for(int i=0; i<splitUcmMetadata.length; i++){
			try{
				String field = m_binder.getLocal(splitSppMetadata[i]);
				
				if(field != null && field.length() > 0){
					String docDates = m_binder.getEnvironmentValue("SPP_INT_DATE_FIELDS");
					String[] splitDocDates = docDates.split(",");
					boolean docIsADate = false;
					
					for(int j=0; j<splitDocDates.length; j++){
						if(splitSppMetadata[i].equalsIgnoreCase(splitDocDates[j])){
							docIsADate=true;
							break;
						}
					}
					
					if(docIsADate){
						queryString += " <AND> " + splitUcmMetadata[i] + " >= `"  + m_binder.getLocal(splitSppMetadata[i]) + "`";
					}else{
						queryString += " <AND> " + splitUcmMetadata[i] + " <MATCHES> `"  + m_binder.getLocal(splitSppMetadata[i]) + "`";
					}
				}
					
			}catch(NullPointerException e){
				//DO NOTHING
			}
		}
		
		try{
			String passedQueryString = m_binder.getLocal("extraQueryText");
			
			if(passedQueryString != null && passedQueryString.length() >0)
				queryString += "<AND> " + passedQueryString;
		} catch (NullPointerException e){
			//Do nothing
		}
		
		Log.info("SPP_WSDL: Running query: " + queryString);
		
		FWFacade ucmFacade = CCLAUtils.getFacade(m_workspace, false);
		SppIntegrationUtils.executeCustomSearch(queryString, m_binder, ucmFacade);
	}
	
	/** Service method for manual checkout/checkin.
	 * 
	 * @throws ServiceException
	 */
	public void checkoutCheckin() throws ServiceException {
		
		String docName = m_binder.getLocal("dDocName");
		
		try {
			LWDocument lwDoc = new LWDocument(docName, true);
			
			// Attempt to retreive workflow ID off associated envelope.
			FWFacade facade =  CCLAUtils.getFacade(m_workspace, false);
			DataResultSet batchItem =
			 BatchDocumentServices.getParentBatchItem(lwDoc.getAttribute("xBundleRef"), facade);
			String wfId = batchItem.getStringValueByName("xBatchNumber");
			
			if (StringUtils.stringIsBlank(wfId) || wfId.equals("0"))
				throw new ServiceException("workflow ID not found on parent envelope.");
			
			checkoutCheckinMAND(lwDoc, wfId);
			
		} catch (Exception e) {
			throw new ServiceException("Failed checkout/checkin: " + e,e);
		}
		
	}
	
	/** Used for checking out/checking in MAND items to
	 *  force them into the VerifyMandate queue.
	 *  
	 * @param lwDoc the MAND item
	 * @param newWorkflowId the new workflow ID to assign to the MAND
	 */
	private void checkoutCheckinMAND(LWDocument lwDoc, String newWorkflowId)
	 throws Exception {
		
		File tempFile = null;
		boolean success = false;
		
		String docName = null, docTitle = null;
		
		try {
			docName 	= lwDoc.getDocName();
			docTitle 	= lwDoc.getAttribute("dDocTitle");
			
			String origName = lwDoc.getAttribute("dOriginalName");
			Log.debug("Resolved original name of MAND: " + origName);
			
			// Copy the current MAND content into a temp file
			ByteArrayOutputStream baos = lwDoc.getLatestContent();
			
			tempFile = new File(origName);
			
			FileOutputStream fois = new FileOutputStream(tempFile);
			fois.write(baos.toByteArray());
			
			lwDoc.checkout();
	
		    if (lwDoc != null) {
		    	lwDoc.setAuthor(m_userData.m_name);
		    	
		    	if (newWorkflowId != null)
		    		lwDoc.setAttribute("xBatchNumber",newWorkflowId);
		    }	
		    	
		    lwDoc.checkin(tempFile, false);
		    
		    Log.debug("Successfully checked out and checked in " +
		     "MAND item.");
		    
		    success = true;
		    
		} catch (Exception e) {
			Log.error("Failed to check out and check in " +
		     "MAND item.",e);
			
		} finally {
			if (tempFile != null) tempFile.delete();
			
			// Audit MAND item checkout/checkin.
			// param1: SUCCESS/FAIL
			Vector params = new Vector();
			String msg;
			
			if (docName != null & docTitle != null) {
				if (success) {
					params.add("SUCCESS");
					msg = "MAND document checked out/checked in";
				} else {
					params.add("FAIL");
					msg = "MAND document failed checkout/checkin";
				}
				
				AuditUtils.addAuditEntry("IRIS", "MAND-RESUBMIT", 
										 docName, 
										 docTitle, 
										 m_userData.m_name,
										 msg,
										 params);
			}
		}
	}
	
	/** Used when performing single-item submissions. Checks if the given
	 *  item (dDocName in binder) already has a workflow ID (xBatchNumber)
	 *  set.
	 *  
	 *  If not, we check for a parent bundle item and see if that has a
	 *  workflow ID. If this exists, copy this workfow ID onto the item.
	 *  
	 *  If the parent bundle item does not have a workflow ID set, 
	 *  or is not found, create a new workflow ID.
	 */
	public void checkItemWorkflowId() throws ServiceException {
		
		try {
			String docName = m_binder.getLocal("dDocName");
			
			Log.info("SPP_WSDL: CHECKING FOR PRESENCE OF WORKFLOW ID ON " + 
			 docName);
			
			LWDocument lwd = new LWDocument(docName, true);
			
			docName = lwd.getDocName();
			String workflowId = lwd.getAttribute("xBatchNumber");
			
			Log.info("SPP_WSDL: workflow ID was " + workflowId);
			
			if (StringUtils.stringIsBlank(workflowId) || workflowId.equals("0")) {
				// Workflow ID missing for this item
				String batchIdField = SharedObjects.getEnvironmentValue("Iris_batchIdField"); 
				String batchRef = lwd.getAttribute(batchIdField);
				
				FWFacade ucmFacade = CCLAUtils.getFacade(m_workspace, false);
				
				boolean hasParentBatchItem = false;
				boolean addedWorkflowId = false;
				
				if (!StringUtils.stringIsBlank(batchRef)) {
					// Lookup bundle item to see if it holds a workflow ID
					Log.info("SPP_WSDL: searching for parent batch item with ref: " + batchRef);
					
					DataResultSet batchItemData = 
					 BatchDocumentServices.getParentBatchItem(batchRef, ucmFacade);
					
					if (batchItemData == null)
						Log.info("SPP_WSDL: No parent batch item found.");
					else {
						hasParentBatchItem = true;
						
						String batchItemDocName = 
						 ResultSetUtils.getValue(batchItemData, "dDocName");
						
						String batchWorkflowId =
						 ResultSetUtils.getValue(batchItemData, "xBatchNumber");
						
						if (StringUtils.stringIsBlank(batchWorkflowId) || 
							batchWorkflowId.equals("0")) {
							// No workflow ID set on parent batch item
							
							Log.info("SPP_WSDL: parent batch item found, " +
							 "but no workflow ID set.");
							
						} else {
							// Workflow ID set on parent batch item, use this one.
							
							Log.info("SPP_WSDL: Found an existing workflow ID on " +
							 "parent batch item  (" + batchWorkflowId + ")");
							lwd.setAttribute("xBatchNumber", batchWorkflowId);
							lwd.update();
							
							addedWorkflowId = true;
							
							Log.info("SPP_WSDL: Successfully applied workflow ID.");
						}
					}	
				}
				
				if (!addedWorkflowId) {
					// Generate new workflow ID.
					String newWorkflowId = SppIntegrationUtils.getNextWorkflowId(CCLAUtils.getFacade(m_workspace,true));
					Log.debug("SPP_WSDL: Generated new workflow ID for item: " + newWorkflowId);
					
					lwd.setAttribute("xBatchNumber", newWorkflowId);
					lwd.update();
					
					addedWorkflowId = true;
					
					Log.info("SPP_WSDL: Successfully applied workflow ID.");
				}
			}
		} catch (Exception e) {
			Log.error("Failed to check/acquire workflow ID for item",e);
			throw new ServiceException("Failed to check/acquire workflow ID for item: " 
			 + e.getMessage());
		}
	}
	
	/** Dispatches a single item to SPP. Expects a dDocName present 
	 *  in the binder.
	 * 
	 *  By default, the user will be redirected to the item's DOC_INFO
	 *  page if the submission is successful. This behaviour can be
	 *  overriden by adding a noRedirectOnSuccess flag to the binder
	 *  before calling this method.
	 *  
	 *  An audit entry is added, indicating whether the submission 
	 *  was successful or not.
	 * @throws DataException 
	 *  
	 **/
	public void triggerSppWorkflow() throws ServiceException, DataException {
		String docName = m_binder.getLocal("dDocName");
		
		if (StringUtils.stringIsBlank(docName))
			throw new ServiceException("Unable to trigger SPP workflow: " +
			 " no dDocName passed");
		
		FWFacade cdbFacade =  CCLAUtils.getFacade(m_workspace, true);
		
		SppIntegrationUtils.triggerSppWorkflow
		 (docName, m_userData.m_name, m_binder, 
		  SppIntegrationUtils.SINGLE_SUBMISSION, 
		  cdbFacade,
		  SPPJobProfile.JOB_PROFILE);
	}
	
	public void addJobNote() {
		
		String jobId = m_binder.getLocal("jobId");
		
		Vector notes = new Vector();
		notes.add(m_binder.getLocal("note"));
		
		String sessionId = m_binder.getLocal("sessionId");
		
		SppIntegrationUtils.addJobNotes(sessionId, jobId, notes);
		
	}
	
	public void getLookupData() throws ServiceException {
		String categoryIdStr 	= m_binder.getLocal("categoryId");
		short categoryId		= Short.parseShort(categoryIdStr);
		
		DataResultSet rsLookupData =
		 SppIntegrationUtils.getLookupDataResultSet(categoryId);
		
		m_binder.addResultSet("rsLookupData", rsLookupData);
	}
	
	public void getComplaintCategories() throws ServiceException {
	
		DataResultSet rsComplaintCategories =
		 SppIntegrationUtils.getComplaintCategoriesResultSet();
		
		m_binder.addResultSet("rsComplaintCategories", 
		 rsComplaintCategories);
	}
	
	public void getComplaintSubCategories() throws ServiceException {
		String categoryIdStr 	= m_binder.getLocal("categoryId");
		int categoryId			= Integer.parseInt(categoryIdStr);
		
		DataResultSet rsComplaintSubCategories =
		 SppIntegrationUtils.getComplaintSubCategoriesResultSet(categoryId);
		
		m_binder.addResultSet("rsComplaintSubCategories", 
		 rsComplaintSubCategories);
	}
	
	public void testWorkflowJobServices() {
		String wfId = m_binder.getLocal("wfId");
		SppIntegrationUtils.testWorkflowJobServices(wfId);
	}
}
	