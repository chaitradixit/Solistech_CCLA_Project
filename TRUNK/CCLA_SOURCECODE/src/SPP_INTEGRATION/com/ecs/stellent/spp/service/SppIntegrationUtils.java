package com.ecs.stellent.spp.service;

import idcbean.data.LWDataBinder;
import idcbean.data.LWResultSet;
import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.data.Workspace;
import intradoc.shared.SharedObjects;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.TimeZone;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.axis.client.Call;
import org.apache.axis.transport.http.CommonsHTTPSender;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ecs.stellent.auditlog.AuditUtils;
import com.ecs.stellent.spp.JobProperties6;
import com.ecs.stellent.spp.LogonDetails;
import com.ecs.stellent.spp.Notes;
import com.ecs.stellent.spp.PlatformManagerWsLocator;
import com.ecs.stellent.spp.PlatformManagerWsSoap_PortType;
import com.ecs.stellent.spp.Variable;
import com.ecs.stellent.spp.WorkflowLocator;
import com.ecs.stellent.spp.WorkflowSoap_PortType;
import com.ecs.stellent.spp.data.ComplaintCategory;
import com.ecs.stellent.spp.data.ComplaintSubCategory;
import com.ecs.stellent.spp.data.LookupData;
import com.ecs.stellent.spp.data.QueryJobData;
import com.ecs.stellent.spp.data.WorkflowJobResponse;
import com.ecs.stellent.spp.data.SPPJobProfile;
import com.ecs.stellent.spp.data.SPPJobProfile.JobVariable;
import com.ecs.stellent.spp.data.SPPJobProfile.VariableDataTypes;
import com.ecs.stellent.spp.fundprocessdetails.FundProcessDetailsManager;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals;
import com.ecs.ucm.ccla.Globals.UCMFieldNames;
import com.ecs.ucm.ccla.data.DocumentClass;
import com.ecs.ucm.ccla.utils.DateUtils;
import com.ecs.ucm.ccla.utils.DocumentUtils;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.DateFormatter;
import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.LWFacade;
import com.ecs.utils.stellent.embedded.FWFacade;

public class SppIntegrationUtils {
	
	/** Passed to SPP web service call, used to identify UCM job submissions. */
	public static final String SPP_SESSION_ID = "CF804840115111D48C6100104B71BD07";
	
	/** Passed as the Logon Type value when calling SPP logon web service 
	 *  (indicates Internet-based login)
	 */
	public static final short SPP_INTERNET_LOGON_TYPE = 7;
	
	/** Username used when calling the SPP logon web service. */
	public static final String SPP_LOGON_USER = "UCM";
	
	// Enums for SPP submission types.
	/** Submission type for single items, generally done manually. */
	public static final int SINGLE_SUBMISSION = 0;
	/** Submission type for batch items, e.g. Iris document bundle. */
	public static final int BATCH_SUBMISSION = 1;

	public static final boolean SET_JOB_ID = 
		SharedObjects.getEnvironmentValue("SPP_INT_ASSIGN_JOB_ID").equals("1");
	
	public static final boolean APPLY_DATE_TIME_OFFSET =
	 !StringUtils.stringIsBlank(SharedObjects.getEnvironmentValue(
	  "SPP_APPLY_DATE_TIME_OFFSET"));
	
	public static final boolean SEND_CONTENT_LINKS_WITH_JOB_DATA =
	 !StringUtils.stringIsBlank(SharedObjects.getEnvironmentValue(
	  "SPP_SEND_CONTENT_LINKS_WITH_JOB_DATA"));
	
	
	public static final boolean SPP_FAKE_WORKFLOW_RESPONSE = 
		SharedObjects.getEnvValueAsBoolean("SPP_FAKE_WORKFLOW_RESPONSE", false);
	
	/** Special date used when null/empty date values are passed to SPP.
	 *  
	 *  Should correspond to 1st Jan 1901.
	 *  
	 */
	public static Date NULL_SPP_DATE;
	
	static {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeZone(TimeZone.getTimeZone("UTC"));
		cal.set(1901, 0, 1, 0, 0, 0);
		
		NULL_SPP_DATE = cal.getTime();
	}
	
	/** NTLM username used for web service authentication */
	public static final String WORKFLOW_AUTH_USERNAME = 
	 SharedObjects.getEnvironmentValue("AURORA_WEBSERVICE_AUTH_USERNAME");
	
	/** NTLM password used for web service authentication */
	public static final String WORKFLOW_AUTH_PASSWORD =
	 SharedObjects.getEnvironmentValue("AURORA_WEBSERVICE_AUTH_PASSWORD");
	
	/** Determines whether mandate items which are submitted in bundles without
	 *  transaction items are checked out/checked in on bundle SPP submission.
	 *  
	 *  Performing the checkout/checkin operation will potentially trigger the
	 *  VerifyMandate (Parking Lot) workflow for these documents.
	 */
	public static final boolean UPREVISION_ORPHAN_MANDS_ON_SUBMISSION =
	 !StringUtils.stringIsBlank(SharedObjects.getEnvironmentValue(
	  "SPP_UPREVISION_ORPHAN_MANDS_ON_SUBMISSION"));
		
	/** Determines whether supporting document types that are bundled with
	 *  mandates are not submitted to SPP.
	 */
	public static final boolean SUPPRESS_MAND_SUPPORTING_DOC_SUBMISSION =
	 !StringUtils.stringIsBlank(SharedObjects.getEnvironmentValue(
	  "SPP_SUPPRESS_MAND_SUPPORTING_DOC_SUBMISSION"));
	
	/** Determines whether supporting document types that are bundled with
	 *  applications are not submitted to SPP.
	 */
	public static final boolean SUPPRESS_APP_SUPPORTING_DOC_SUBMISSION =
	 !StringUtils.stringIsBlank(SharedObjects.getEnvironmentValue(
	  "SPP_SUPPRESS_APP_SUPPORTING_DOC_SUBMISSION"));
	
	/** List of document types which are valid for SPP release. Other document
	 * types are prevented from release.
	 */
	public static String[] SPP_JOBTYPES = null;
	
	/** List of document classes which will use their xFormId value as a workflow
	 *  ID (only applicable if their xFormId value is non-null)
	 */
	public static String[] SPP_FORM_ID_DOC_CLASSES = null;
	
	static {
		String jobTypes = SharedObjects.getEnvironmentValue("SPP_INT_UCM_DOCTYPES");
		
		if (!StringUtils.stringIsBlank(jobTypes))
			SPP_JOBTYPES = jobTypes.split(",");
		
		String formIdDocClasses = 
		 SharedObjects.getEnvironmentValue("SPP_FORM_ID_DOC_CLASSES");
		
		if (!StringUtils.stringIsBlank(formIdDocClasses))
			SPP_FORM_ID_DOC_CLASSES = formIdDocClasses.split(",");
	}
	
	/** Fetches the current UCM workflow step name for the given item.
	 *  
	 *  Returns null if the given item is not in a UCM workflow.
	 *  
	 * @param docName
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static String getDocumentWorkflowStepName
	 (String docName, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		
		binder.putLocal("DOCNAME", docName);
		DataResultSet rsWfStepName = facade.createResultSet
		 ("QgetWorkflowStepName", binder);
		
		if (rsWfStepName.isEmpty())
			return null;

		return rsWfStepName.getStringValue(0);
	}

	
	/** Fetches the next workflow ID (formerly envelope ID, stored in 
	 * 	the xBatchNumber field of an item before it is submitted to SPP.
	 *  SPP still refer to this field value as the envelope ID. In UCM it
	 *  is more of a workflow batch ID.
	 *  
	 * @param workspace
	 * @return
	 */
	public static String getNextEnvelopeId(Workspace workspace) throws DataException {

		return getNextEnvelopeId(CCLAUtils.getFacade(workspace, true));
	}
	
	/** Fetches the next workflow ID (formerly envelope ID, stored in 
	 * 	the xBatchNumber field of an item before it is submitted to SPP.
	 *  SPP still refer to this field value as the envelope ID. In UCM it
	 *  is more of a workflow batch ID.
	 *  
	 * @param workspace
	 * @return
	 */
	public static String getNextEnvelopeId(FWFacade cdbFacade) 
	 throws DataException {
		
		DataResultSet rs = cdbFacade.createResultSet
		 ("qGetNextEnvelopeId", new DataBinder());
		
		if (rs == null || rs.isEmpty() 
			|| 
			StringUtils.stringIsBlank(rs.getStringValue(0)))
			throw new DataException("Unable to retrieve new envelope ID");
		
		return rs.getStringValue(0);
	}
	
	/** Fetches the next workflow ID (formerly envelope ID), stored in
	 *  the xBatchNumber field of an item before it is submitted to SPP.
	 *  
	 *  TODO: sort out the underlying queries and sequences.
	 *  This method should use it's own database sequence, and not the
	 *  same one that the envelope ID is pulled from. 
	 *  
	 * @param workspace
	 * @return
	 */
	public static String getNextWorkflowId(Workspace workspace) throws DataException {

		return getNextWorkflowId(CCLAUtils.getFacade(workspace, true));
	}
	
	public static String getNextWorkflowId(FWFacade cdbFacade) throws DataException {
		
		DataResultSet rs = 
		 cdbFacade.createResultSet("qGetNextWorkflowId", new DataBinder());
		
		if (rs == null || rs.isEmpty() 
			|| 
			StringUtils.stringIsBlank(rs.getStringValue(0)))
			throw new DataException("Unable to fetch new workflow ID");
		
		return rs.getStringValue(0);
	}
	
	/** Fetches the next job ID, which will be stored in the xJobId field.
	 * Any document that starts a job in SPP should get a unique job id, and
	 * it should be passed to SPP.
	 *  
	 * @deprecated use equivalent method in CCLA_Core CCLAUtils class instead. 
	 *  
	 * @return
	 * @throws DataException 
	 */
	public static String getNextJobId(FWFacade facade) throws DataException{

		DataResultSet rs = 
		 facade.createResultSet("qGetNextJobId", new DataBinder());
		
		if (rs == null || rs.isEmpty() 
			|| 
			StringUtils.stringIsBlank(rs.getStringValue(0)))
			throw new DataException("Unable to fetch new Job ID");
		
		return rs.getStringValue(0);
	}
	
	/** Calls the full triggerSppWorkflow method with useTransaction=true.
	 * 
	 * @param docName
	 * @param userName
	 * @param binder
	 * @param submissionType
	 * @param facade
	 * @param workflowProfile
	 * @return
	 * @throws ServiceException
	 */
	public static Date triggerSppWorkflow
	 (String docName, String userName, DataBinder binder, 
	  int submissionType, FWFacade facade, SPPJobProfile workflowProfile)
	  throws ServiceException {
		
		return triggerSppWorkflow
		 (docName, true, userName, binder, submissionType, facade, workflowProfile);
	}
	
	/** Dispatches a single UCM content item to SPP.
	 * 
	 *  By default, the user will be redirected to the item's DOC_INFO
	 *  page if the submission is successful. This behaviour can be
	 *  overriden by adding a noRedirectOnSuccess flag to the binder
	 *  before calling this method.
	 *  
	 *  An audit entry is added, indicating whether the submission 
	 *  was successful or not.
	 *  
	 *  The xProcessDate on the content item is calculated and set if the current value
	 *  is empty/null.
	 *  
	 *  The xWorkflowDate on the content item is updated to reflect the current time
	 *  if the trigger was successful.
	 *  
	 *  The xJobId field will be set to a unique sequence value if the SET_JOB_ID
	 *  flag is set.
	 *  
	 *  The xStatus value will also be updated to one of the following:
	 *  -Released to SPP
	 *  -Failed SPP release
	 *  
	 *  @param 		useTransaction 	determines whether or not LWDocument.update() calls
	 *  							are executed in their own transaction block. This
	 *  							should always be true unless the workflow trigger
	 *  							is occurring within an outer transaction block.
	 *  
	 *  @param		binder			can be passed in empty. Used to update counter
	 *  							values and set RedirectUrls.
	 *  
	 *  @return 	the workflow submission date, if the trigger was successful,
	 *  			null otherwise.
	 *  @throws 	ServiceException if the xBatchNumber is missing from the document
	 **/
	public static Date triggerSppWorkflow
	 (String docName, boolean useTransaction, String userName, DataBinder binder, 
	  int submissionType, FWFacade cdbFacade, SPPJobProfile workflowProfile)
	
	  throws ServiceException {
		
		// We'll need a UCM Facade later on. Create one now
		FWFacade ucmFacade = null;
		try {
			ucmFacade = CCLAUtils.getFacade(false);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		
		String docTitle = null;
		Date wfDate = null;
		
		// Error flags
		boolean missingWorkflowId 	= false;
		boolean validDocType		= false;
		
		boolean isDuplicate = false;
		
		long startTime = System.currentTimeMillis();
		LWDocument lwd = null;
		
		try {
			// TM: check for noRedirectOnSuccess flag in binder.
			// If found, don't add DOC_INFO RedirectUrl to binder.
			boolean redirectToDocInfo = 
			 StringUtils.stringIsBlank(binder.getLocal("noRedirectOnSuccess"));
			
			Log.info("SPP_WSDL: TRIGGERING SPP WORKFLOW FOR " + docName + 
			 " VIA STATIC FUNCTION");
			
			lwd = new LWDocument(docName, true);
			lwd.setUseTransaction(useTransaction);
			
			docName = lwd.getDocName();
			docTitle = lwd.getAttribute(UCMFieldNames.DocTitle);
			
			String docType	= lwd.getAttribute(UCMFieldNames.DocType);
			
			// Ensure this item has a valid SPP doc type.
			if (SPP_JOBTYPES != null) {
				for (int i=0; i<SPP_JOBTYPES.length; i++) {
					if (docType.equals(SPP_JOBTYPES[i]))
						validDocType = true;
				}
			} else
				validDocType = true;
			
			if (!validDocType) {
				String msg = "Item did not have a valid SPP doc type: " + docType;
				
				Log.error(msg);
				throw new ServiceException(msg);
			}
			
			// TM: Check if this is a multidoc type before proceeding. If this
			// is a multi-doc, it should not trigger an SPP workflow, so return 
			// immediately.
			String isMultiDoc 	= lwd.getAttribute(UCMFieldNames.MultiDoc);
			String docClass		= lwd.getAttribute(UCMFieldNames.DocClass);
			
			if (!StringUtils.stringIsBlank(docClass) 
				&& 
				(docClass.equals(DocumentClass.Classes.MULTIDOC)
				||
				docClass.equals(DocumentClass.Classes.MULTIINV))) {
				Log.info(docName + " is a multi-doc. Skipping SPP submission.");
				
				if (redirectToDocInfo)
					addDocInfoRedirectUrl(binder, lwd.getId(), "&wfStarted=false&multiDoc=true");
				
				return null;
			}
			
			//CL: Check for the processDate and populate it.
			String processDateStr = lwd.getAttribute(UCMFieldNames.ProcessDate);
			String origProcessDateStr = lwd.getAttribute(UCMFieldNames.OriginalProcessDate);
			
			Date dInDate = null;
			if (StringUtils.stringIsBlank(origProcessDateStr)) {
				dInDate = DateFormatter.getSystemSimpleDateFormat().parse(
						 lwd.getAttribute(UCMFieldNames.DocInDate));
				
				Log.debug("OrigProcessDate is empty, attempting to populate");
				Date origProcessDate = FundProcessDetailsManager.getDealingDate(
						lwd.getAttribute(UCMFieldNames.Fund), 
						lwd.getAttribute(UCMFieldNames.DestinationFund), 
						docClass, 
						null,
						false,
						lwd.getAttribute(UCMFieldNames.Source), 
						dInDate,
						true,
						cdbFacade);
				
				lwd.setAttribute(UCMFieldNames.OriginalProcessDate, 
				 DateFormatter.getTimeStamp(origProcessDate));
			}
			
			if (StringUtils.stringIsBlank(processDateStr)) {
				Log.debug("ProcessDate is empty, attempting to populate");
				Date processDate = FundProcessDetailsManager.getDealingDate(
						lwd.getAttribute(UCMFieldNames.Fund), 
						lwd.getAttribute(UCMFieldNames.DestinationFund), 
						docClass, 
						processDateStr,
						false,
						lwd.getAttribute(UCMFieldNames.Source), 
						null,
						false,
						cdbFacade);
				
				Log.debug("Calculated Process Date of " + processDate + 
				 " using document date: " + DateUtils.getNow());
				
				lwd.setAttribute(UCMFieldNames.ProcessDate, 
				 DateFormatter.getTimeStamp(processDate));
			}
			
			// TM: Check if this item has a workflow ID set. If not, throw an
			// Exception.
			String workflowId = lwd.getAttribute(UCMFieldNames.BatchNumber);
			
			if (StringUtils.stringIsBlank(workflowId) || workflowId.equals("0")) {
				missingWorkflowId = true;
				throw new Exception("Item did not have workflow ID set (xBatchNumber)");
			}
			
			// Set the UCM Job ID value against the document.
			if (SET_JOB_ID)
				lwd.setAttribute(UCMFieldNames.JobID, 
				 Integer.toString(CCLAUtils.getNextSppJobId(cdbFacade)));
			
			// Get all required SPP variables from the content item, 
			// e.g. title, account num etc
			Variable[] initParams = getSppVariables
			 (lwd.getAttributes(), workflowProfile, ucmFacade);
			
			// Calls the SPP service with the generated variable array.
			WorkflowJobResponse response = 
			 triggerSppJob(initParams, workflowProfile);

			Log.info("SPP_WSDL: ...FINISHED TRIGGERING SPP WORKFLOW");
			
			wfDate = new Date();	    
		    String now = 
		     DateFormatter.getSystemSimpleDateFormat().format(wfDate);
		    
		    // Child documents have no content - ensure createPrimaryMetaFile
		    // flag is set before performing update.
		    // TODO: no longer required, flag added by PreUpdateFilter filter class
		    if(lwd.getAttribute(UCMFieldNames.DocType).equals("ChildDocument"))
		    	lwd.setAttribute("createPrimaryMetaFile", "1");
		    
		    // Set the workflow date on the item
		   
			lwd.setAttribute(UCMFieldNames.WorkflowDate, now);
			
			//Added code that checks if a job is being manually started for a Duplicate. 
			//If it is, it should no longer have an xStatus of 'Duplicate'. Set it to 
			//'Duplicate_JobStarted' for auditing purposes.
			if(lwd.getAttribute(UCMFieldNames.Status).equals("Duplicate")){
				isDuplicate = true;
				Log.info("Duplicate item being submitted to SPP.");
			}
			
			lwd.setAttribute(UCMFieldNames.Status,"Released to SPP");
			lwd.update();
			
			// If need to redirect
			if (redirectToDocInfo)
				addDocInfoRedirectUrl(binder, lwd.getId(), "&wfStarted=true");
			
			// TM: added this to store running total of submitted docs in the binder.
			// Useful when doing a batch submission. 
			// Either set it to 1 if not present, or increment it if an ongoing count
			// is present.
			String SPPSubmitCount = binder.getLocal("SPPSubmitCount");
			
			if (StringUtils.stringIsBlank(SPPSubmitCount))
				binder.putLocal("SPPSubmitCount", "1");
			else {
				int submitCount = Integer.parseInt(SPPSubmitCount);
				submitCount++;
				
				binder.putLocal("SPPSubmitCount", Integer.toString(submitCount));
			}

			// Try and audit the submit to SPP
			if (docName != null) {
				// Audit single item SPP submission.
				// param1: workflow ID used for submission
				// param2: outcome of submission (SUCCESS/FAIL)
				// param3: submission type (integer)
				Vector<String> params = new Vector<String>();
				params.add("");
				params.add("SUCCESS");
				params.add(Integer.toString(submissionType));
				params.add(response.getReturnId());
				if (isDuplicate)
					params.add("1");
				
				String msg = "Document submitted to SPP";
				if (isDuplicate) {
					msg += " (duplicate)";
				}
				
				AuditUtils.addAuditEntry("IRIS", "SPP-MAN-SUBMIT", 
				 docName, 
				 docTitle, 
				 userName,
				 msg,
				 params);
				
			} else {
				Log.warn("Unable to add audit entry for SPP submission, " +
				 "docName not present.");
			}
			
		} catch (Exception e) {
			Log.error("Unable to trigger workflow: " + e.getMessage(),e);
			
			if (docName != null) {				
				// Audit failed submission
				Vector<String> params = new Vector<String>();
				params.add("");
				params.add("FAIL");
				params.add(Integer.toString(submissionType));
				if (isDuplicate)
					params.add("1");
				
				String msg = "Document failed submission to SPP";
				
				if (!validDocType)
					msg += " - invalid type";
				else if (missingWorkflowId)
					msg += " - workflow ID missing";
				
				AuditUtils.addAuditEntry("IRIS", "SPP-MAN-SUBMIT", 
				 docName, 
				 docTitle, 
				 userName,
				 msg,
				 params);
				
				if (lwd != null) {
					lwd.setAttribute(UCMFieldNames.Status,"Failed SPP release");
					try {
						lwd.update();
					} catch (Exception ex) {
						Log.error("Unable to set Failed xStatus value on item.");
					}
				}
				
			} else {
				Log.warn("Unable to add audit entry for failed SPP submission, " +
				 "docName not present.");
			}
			
			throw new ServiceException("Unable to start a workflow for this document: "
			 + e.getMessage(),e);
		}
		
		long endTime = System.currentTimeMillis();
		Log.info("SPP_WSDL: FINISHED TRIGGERING SPP WORKFLOW FOR " + docName + 
		 " VIA STATIC FUNCTION.");
		Log.debug("triggerSppWorkflow << docName: " + docName + 
		 ", time taken: " + (endTime-startTime)/1000D + "s");
		Log.info("-----------------------------");
		
		return wfDate;
	}
	
	/** Dispatches the set of passed items to SPP. The ResultSet must 
	 *  contain a dDocName column.
	 *  
	 *  If the checkForDate parameter is true, the passed ResultSet is
	 *  checked for an xWorkflowDate value. If this is non-null, the
	 *  item will not be dispatched to workflow.
	 *  
	 *  The item will not be dispatched if it doesn't have a workflow
	 *  ID (xBatchNumber) present.
	 *  
	 * @throws DataException 
	 *  
	 **/
	public static void triggerSppWorkflow
	 (DataResultSet docs, FWFacade facade, boolean checkForDate) {
		
		Log.debug("Triggering SPP workflow on " + docs.getNumRows() + " items");
		int successCount = 0;
		int suppressCount = 0;
		
		if (docs.first()) {
			
			DataBinder binder = new DataBinder();
			binder.putLocal("noRedirectOnSuccess", "1");
			
			do {
				String docName = docs.getStringValueByName("dDocName");
				Log.debug("Attempting to trigger workflow on: " + docName);
				
				try {
					boolean suppress = false;
					
					if (StringUtils.stringIsBlank(docName))
						throw new ServiceException("dDocName was empty/missing");
					
					if (checkForDate) {
						String wfDate = docs.getStringValueByName("xWorkflowDate");
						
						if (!StringUtils.stringIsBlank(wfDate)) {
							Log.debug("Found a workflow date, skipping this item.");
							suppress = true;
							suppressCount++;
						}
					}
					
					if (!suppress) {
						SppIntegrationUtils.triggerSppWorkflow
						 (docName, "SPP_BatchTrigger", binder, 
						  SppIntegrationUtils.SINGLE_SUBMISSION, 
						  facade, SPPJobProfile.JOB_PROFILE);
						
						Thread.sleep(100);
						successCount++;
					}
					
				} catch (Exception e) {
					Log.error("Failed to trigger workflow on item: " + docName, e);
				}
				
			} while (docs.next());
			
			Log.debug("Finished triggering SPP jobs." +
			 "\nTotal: " + docs.getNumRows() +
			 "\nSucceeded: " + successCount +
			 "\nSuppressed: " + suppressCount);
			
		} else {
			Log.error("No documents in passed ResultSet");
		}
	}
	
	/** Triggers an SPP job with the given parameters. 
	 *  
	 * @throws ServiceException 
	 **/
	public static WorkflowJobResponse triggerSppJob(Variable[] initParams, 
	 SPPJobProfile workflowProfile) 
	 throws ServiceException {
		
		
		Log.debug((SPP_FAKE_WORKFLOW_RESPONSE?"FAKING_WORKFLOW ":"")+"Triggering the following SPP Job:\n" + 
				"Profile: '" + workflowProfile.getProfileName() +"'\n"+
				"Map Name: '" + workflowProfile.getMapName() + "'\n" +
				initParams.length + " params: \n" + Variable.arrayToString(initParams));
		
		try {
			
			String returnId = null;
			
			//faking spp workflow submissions 
			if (SPP_FAKE_WORKFLOW_RESPONSE) {
				returnId = String.valueOf(new Date().getTime());
			} else {
				returnId = getPlatformManagerWsPort().createNewJobUsingMapName
			     (SPP_SESSION_ID,workflowProfile.getMapName(),initParams);
			}
			Integer jobId = null;
			
			/*
			if (SET_JOB_ID)
				//Set the Job ID for each job that starts in SPP
				jobId = CCLAUtils.getNextSppJobId(facade);
			*/
			
			WorkflowJobResponse response = new WorkflowJobResponse(jobId, returnId);
			Log.debug("Successfully triggered SPP job: " + response.toString());
			
			return response;
			
		} catch (Exception e) {
			Log.error("Unable to trigger job: " + e.getMessage(), e);
			throw new ServiceException(e);
		}
	}
	
	/** Triggers an SPP query job for the given client/account, with the
	 *  given username/comment attached. 
	 *  
	 * @param company
	 * @param clientNumber
	 * @param accountNumber
	 * @return
	 * @throws Exception 
	 * @throws ServiceException 
	 * @throws DataException 
	 */
	public static WorkflowJobResponse triggerSppQueryJob
	 (QueryJobData queryJobData, String userName, FWFacade ucmFacade) 
		throws ServiceException, DataException {
		
		Log.debug("Triggering SPP CLIENTQUERY job");
		Log.debug(queryJobData.toString());
		
		Hashtable<String, String> attributes = 
		 queryJobData.getAttributes();
		
		return triggerSppJob(
				getSppVariables(attributes, SPPJobProfile.QUERY_PROFILE, ucmFacade),
				SPPJobProfile.QUERY_PROFILE);
	}
	
	/** Constructs and returns an SPP Variable array from the given set of name-value
	 *  attributes. The attributes are expected to use UCM metadata field names.
	 *  
	 *  This variable array will be submitted to the SPP job-creation web service.
	 *  
	 *  The UCM - SPP field mapping is acquired from comma-separated environment 
	 *  variables.
	 *  
	 * @param lwd 	an instantiated LWDocument instance for the item you wish to
	 * 				capture SPP variable data.
	 * @return
	 */
	private static Variable[] getSppVariables(Hashtable<String, String> attributes,
			SPPJobProfile workflowProfile, FWFacade ucmFacade) throws DataException {
		
		Variable[] initParams = 
		 new Variable[workflowProfile.getNumVariables()];
		
		//Set URL param from binder
		int varIndex = 0;
		
		String docClass = attributes.get(UCMFieldNames.DocClass);
		boolean isInvoice = false;
		String[] blankifyUCMInvoiceFields = null;
		
		if (docClass != null && docClass.equals(DocumentClass.Classes.INV)) {
			isInvoice = true;
			
			// Oh dear, an invoice. Build the list of UCM fields that must be blanked
			// out in the job variables. This is due to invoices using certain UCM
			// fields for other purposes, e.g. Fund, Units.
			String blankifyUCMInvoiceFieldsStr = SharedObjects.getEnvironmentValue
			 ("CCLA_CommProc_Invoices_BlankValuesOnWorkflowSubmission");
			
			Log.debug("Invoice item detected! The following UCM field values will be " + 
			 "sent through as empty strings: " + blankifyUCMInvoiceFieldsStr);
			
			if (!StringUtils.stringIsBlank(blankifyUCMInvoiceFieldsStr)) {
				blankifyUCMInvoiceFields = blankifyUCMInvoiceFieldsStr.split(",");
			}
		}
		
		for (JobVariable jobVariable : workflowProfile.getJobVariables()) {
			
			Variable initParam 	= jobVariable.getEmptyVariableInstance();
			
			String ucmVarName	= jobVariable.getUcmVarName();
			String ucmVarValue  = attributes.get(ucmVarName);
			
			boolean blankifyField = false;
			
			if (isInvoice && blankifyUCMInvoiceFields != null) {
				// Determine whether or not to blank out this field.
				for (String ucmField : blankifyUCMInvoiceFields) {
					if (ucmVarName.equals(ucmField)) {
						blankifyField = true;
						break;
					}
				}
			}
			
			if (jobVariable.getSppVarName().equals("pFILENAME")) {
				// Add the FILENAME parameter for the associated content item.
				// Not all workflow jobs will have a content item - check for 
				// presence of dDocName value first.
				
				// This link-building method won't work in 11g systems - you don't have
				// the same control over the direct PDF URLS.
				// To prevent spurious/incorrect URLs being passed, a new config var
				// has been introduced which will prevent them being generated at all.
				// In future, workflow should issue calls to the CCLA_CP_GET_FILE
				// service to acquire content instead.
				if (SEND_CONTENT_LINKS_WITH_JOB_DATA) {
					String docName = attributes.get(UCMFieldNames.DocName);
					
					if (!StringUtils.stringIsBlank(docName)) {
						String docUrl = DocumentUtils.generateDocUrl(
						 attributes.get(UCMFieldNames.SecurityGroup), 
						 attributes.get(UCMFieldNames.DocAccount), 
						 attributes.get(UCMFieldNames.DocType), 
						 attributes.get(UCMFieldNames.DocName), 
						 attributes.get(UCMFieldNames.ParentDocName), 
						 attributes.get(UCMFieldNames.PdfDocName), 
						 attributes.get(UCMFieldNames.FolderName), 
						 ucmFacade);
						//Log.info("SPP_WSDL: Setting Doc URL to: " + docUrl);
						
						initParam.setValue(docUrl);
					} else {
						//Log.info("SPP_WSDL: Skipping Doc URL, no dDocName found");
						initParam.setValue(null);
					}
					
				} else {
					initParam.setValue(null);
				}
				
			} else if (jobVariable.getSppVarName().equals("pUCM_GUID")) {
				// Add the Doc GUID parameter for the associated content item.
				String docName = attributes.get(UCMFieldNames.DocName);
				String docGUID = "";
				
				try {
					LWDocument lwd = new LWDocument(docName, true);
					docGUID = CCLAUtils.getDocGuidFromLwd(lwd);
					
				} catch (Exception e) {
					Log.error("Failed to fetch Doc GUID for docName: " + docName);
				}
				
				initParam.setValue(docGUID);
			
			} else if (blankifyField) {
				// Stupid hack/workaround to ensure Invoice items are processed.
				// TODO: fix this so invoice fields are passed correctly or not at all.
				initParam.setValue("");
				
			} else {	
				if (jobVariable.getSppVarDataType() == VariableDataTypes.SHORT){					
					//PARAM NEEDS TO BE A SHORT
					Short paramShort = new Short(Short.parseShort(ucmVarValue));
					
					initParam.setValue(paramShort);

				} else if (jobVariable.getSppVarDataType() == VariableDataTypes.LONG) {
					//PARAM NEEDS TO BE A LONG
					Long paramLong = null;
					
					if (!StringUtils.stringIsBlank(ucmVarValue)) {
						paramLong = new Long(Long.parseLong(ucmVarValue));
					}
					
					initParam.setValue(paramLong);
				
				} else if (jobVariable.getSppVarDataType() == VariableDataTypes.FLOAT) {
					//PARAM NEEDS TO BE A FLOAT
					Float paramFloat = new Float(Float.parseFloat(ucmVarValue));
					initParam.setValue(paramFloat);
					
				} else if (jobVariable.getSppVarDataType() == VariableDataTypes.DOUBLE) {
					//PARAM NEEDS TO BE A DOUBLE
					Double paramDouble = new Double(Double.parseDouble(ucmVarValue));
					initParam.setValue(paramDouble);
					
				} else if (jobVariable.getSppVarDataType() == VariableDataTypes.CURRENCY) {
					//PARAM NEEDS TO BE A CURRENCY
					//Double is best match?
					Double paramDouble = new Double(Double.parseDouble(ucmVarValue));
					initParam.setValue(paramDouble);
				
				} else if (jobVariable.getSppVarDataType() == VariableDataTypes.DATE) {
					//PARAM NEEDS TO BE A DATE
					String dateStr = ucmVarValue;
					Date date = null;
					
					if (!StringUtils.stringIsBlank(dateStr)) {
				
						try {
							date = 
							 DateFormatter.getSystemSimpleDateFormat().parse(dateStr);
							
							date = getSppDate(date);
							initParam.setValue(date);
							
						} catch (ParseException pe) {
							Log.warn("Failed to parse date '" + dateStr + "'");
						}
					}
					
					if (date == null) {
						//initParam.setValue(new Date());
						initParam.setValue(null);
					}
					
				} else if (jobVariable.getSppVarDataType() == VariableDataTypes.BOOLEAN) {
					//PARAM NEEDS TO BE A BOOLEAN
					String boolStr = ucmVarValue;
					
					Boolean paramBoolean = !StringUtils.stringIsBlank(boolStr) &&
					 !boolStr.equals("N") && !boolStr.equals("No")
					 && !boolStr.equals("0") 
					 && !boolStr.equals("False");
						
					initParam.setValue(paramBoolean);
					
				} else if (jobVariable.getSppVarDataType() == VariableDataTypes.DECIMAL) {	
					//PARAM NEEDS TO BE A DECIMAL
					//Float is best match?
					Float paramFloat = new Float(Float.parseFloat(ucmVarValue));
					initParam.setValue(paramFloat);
					
				} else if (jobVariable.getSppVarDataType() == VariableDataTypes.BYTE) {	
					//PARAM NEEDS TO BE A BYTE
					Byte paramByte = new Byte(ucmVarValue);
					initParam.setValue(paramByte);
					
				} else {
					// Assume String type
					initParam.setValue(ucmVarValue);
				}
			}
			
			initParams[varIndex++] = initParam;
		}
		
		return initParams;
	}
	
	/*  
	 *  Used to translating standard Date values before they are sent as SPP Variable
	 *  objects.
	 * 
	 *  This fixes a timezone issue which arises during the BST period. Dates are
	 *  always passed to SPP in UTC format. SPP/DotNet reads these date values
	 *  literally and doesnt apply timezone information. During BST, this means
	 *  all times are displayed 1 hour earlier than they really are.
	 *  
	 *  To get round these, we detect if the passed date is during BST. If so, an extra
	 *  hour is added to ensure this still confirms to GMT.
	 */
	public static Date getSppDate(Date date) {
		
		if (APPLY_DATE_TIME_OFFSET) {
			Calendar cal = new GregorianCalendar();
			cal.setTime(date);
			
			if (cal.getTimeZone().inDaylightTime(cal.getTime())) {
				// Date is in BST (British Summer Time). Add an 
				// extra hour to counter the UTC date transition
				cal.add(Calendar.HOUR_OF_DAY, 1);
				
				Log.debug("(BST) Changed date from " + date.toString() + 
				 " to: " + cal.getTime().toString());
				
				date = cal.getTime();
			} else {
				// We are in GMT. No time edits required.
			}
		}
		
		return date;
	}
	
	/** Instantiates the SPP web service port used to execute
	 *  the SPP web service.
	 *  
	 *  Use web service call below as black box. Returns a unique
	 *  SPP id just for the triggered job.
	 *  
	 * @return
	 * @throws MalformedURLException
	 * @throws javax.xml.rpc.ServiceException
	 */
	private static PlatformManagerWsSoap_PortType getPlatformManagerWsPort()
	 throws MalformedURLException, javax.xml.rpc.ServiceException {
		
		PlatformManagerWsLocator service = new PlatformManagerWsLocator();
		
		// Get the SPP web service address
		String sppAddress = 
		 SharedObjects.getEnvironmentValue("SPP_INT_SPP_ADDRESS");
		
		Log.info("SPP_WSDL: Connecting to " + sppAddress);
		
		//Get the SPP Machine address from env
		service.setPlatformManagerWsSoap12EndpointAddress(sppAddress);
		
		URL portUrl = new URL(sppAddress);
		return service.getPlatformManagerWsSoap(portUrl);
	}
	
	/** Instantiates the SPP web service port used to execute
	 *  the SPP web service.
	 *  
	 *  Use web service call below as black box. Returns a unique
	 *  SPP id just for the triggered job.
	 *  
	 * @return
	 * @throws MalformedURLException
	 * @throws javax.xml.rpc.ServiceException
	 */
	private static WorkflowSoap_PortType getWorkflowWsServicePort()
	 throws MalformedURLException, javax.xml.rpc.ServiceException {
		
		WorkflowLocator service = new WorkflowLocator();
		
		// Get the SPP web service address
		String sppAddress = 
		 SharedObjects.getEnvironmentValue("SPP_INT_WORKFLOW_ADDRESS");
		
		Log.info("SPP_WSDL: Connecting to " + sppAddress);
		
		//Get the SPP Machine address from env
		service.setWorkflowSoap12EndpointAddress(sppAddress);
		
		URL portUrl = new URL(sppAddress);
		return service.getWorkflowSoap(portUrl);
	}
	
	/** Returns an org.w3c.dom.Document instance for the given XML string.
	 * 
	 * @param xmlData
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	private static Document getDocument(String xmlData) 
	 throws ParserConfigurationException, SAXException, IOException {
		// Get Document Builder Factory
		DocumentBuilderFactory factory =
		DocumentBuilderFactory.newInstance();

		// Turn on validation, and turn off namespaces
		factory.setValidating(false);
		factory.setNamespaceAware(false);

		// Obtain a document builder object
		DocumentBuilder builder = factory.newDocumentBuilder();
		
		ByteArrayInputStream is = 
		 new ByteArrayInputStream(xmlData.getBytes());
		
		return builder.parse(is);
	}
	
	public static void refreshComplaintCategoriesCache(FWFacade facade) 
	 throws DataException, ServiceException {
		
		Vector<ComplaintCategory> categories = getComplaintCategories();
		
		try {
			facade.beginTransaction();
			
			for (ComplaintCategory category : categories) {
				facade.executeSQL("INSERT INTO CCLA_COMPLAINT_CATEGORIES " +
				 "VALUES(" + category.getId() + ",'" + category.getName() + "')");
				
				Vector<ComplaintSubCategory> subCategories = 
				 getComplaintSubCategories(category.getId());
				
				for (ComplaintSubCategory subCategory : subCategories) {
					facade.executeSQL("INSERT INTO CCLA_COMPLAINT_SUBCATEGORIES " +
					 "VALUES(" + subCategory.getId() + "," + category.getId() + 
					 ",'" + category.getName() + "')");		
				}
			}
			
			facade.commitTransaction();
			
		} catch (Exception e) {
			facade.rollbackTransaction();
			throw new ServiceException(e.getMessage(), e);
		}
	}
	
	/** Fetches a list of complaint categories - these are displayed on the
	 *  Record Interaction page in UCM.
	 *  
	 * @throws javax.xml.rpc.ServiceException 
	 * @throws MalformedURLException 
	 * @throws RemoteException 
	 * @throws RemoteException 
	 * @throws ServiceException 
	 */
	public static Vector<ComplaintCategory> getComplaintCategories() 
	 throws ServiceException {
		
		try {
			WorkflowSoap_PortType workflowWs = getWorkflowWsServicePort();
			String complaintCategories = workflowWs.getComplaintCategories();	
			
			// The lookup data is returned as raw XML. Turn this into something
			// useful.
			
			//		<ComplaintCategoryResultSet>
			//			<ComplaintCategory>
			//				<ComplaintCategoryID>1</ComplaintCategoryID>
			//				<CategoryName>Indexing</CategoryName>
			//			</ComplaintCategory>	
			
			Document document = getDocument(complaintCategories);
			NodeList categoryNodes = document.getFirstChild().getChildNodes();
			
			Log.debug("Found " + categoryNodes.getLength() + 
			 " ComplantCategory nodes");
			
			Vector<ComplaintCategory> categoryList = new Vector<ComplaintCategory>();
			
			for (int i=0; i<categoryNodes.getLength(); i++) {
				Node lookupDataNode = categoryNodes.item(i);
				
				String thisId = 
				 lookupDataNode.getFirstChild().getFirstChild().getNodeValue();
				
				String thisValue =
				 lookupDataNode.getLastChild().getFirstChild().getNodeValue();
				
				//Log.debug("Adding complaint category: " + thisId + ", " + thisValue);
				
				categoryList.add(
					new ComplaintCategory(Integer.parseInt(thisId), thisValue)
				);
			}
			
			return categoryList;		
		
	 	} catch (Exception e) {
			String msg = "Failed to get lookup data via SPP web service: " + 
			 e.getMessage();
			
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	
	}
	
	/** Fetches a list of complaint sub-categories for a given complaint
	 *  category.
	 *  
	 * @throws javax.xml.rpc.ServiceException 
	 * @throws MalformedURLException 
	 * @throws RemoteException 
	 * @throws RemoteException 
	 * @throws ServiceException 
	 */
	public static Vector<ComplaintSubCategory> getComplaintSubCategories(int categoryId) 
	 throws ServiceException {
		
		try {
			WorkflowSoap_PortType workflowWs = getWorkflowWsServicePort();
			String complaintSubCategories = 
			 workflowWs.getComplaintSubCategories(categoryId);
			
			Document document = getDocument(complaintSubCategories);
			NodeList categoryNodes = document.getFirstChild().getChildNodes();
			
			Log.debug("Found " + categoryNodes.getLength() + 
			 " ComplantCategory nodes");
			
			Vector<ComplaintSubCategory> subCategoryList = 
			 new Vector<ComplaintSubCategory>();
			
			for (int i=0; i<categoryNodes.getLength(); i++) {
				Node lookupDataNode = categoryNodes.item(i);
				
				String thisId = 
				 lookupDataNode.getFirstChild().getFirstChild().getNodeValue();
				
				String thisValue =
				 lookupDataNode.getLastChild().getFirstChild().getNodeValue();
				
				//Log.debug("Adding complaint sub-category: " + thisId + ", " + thisValue);
				
				subCategoryList.add(
					new ComplaintSubCategory(Integer.parseInt(thisId), thisValue)
				);
			}
			
			return subCategoryList;		
			
		} catch (Exception e) {
			String msg = "Failed to get lookup data via SPP web service: " + 
			 e.getMessage();
			
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}	
	}
	
	/** Fetches a list of 'lookup data' from SPP, based on the passed
	 *  lookup category ID.
	 *  
	 * @param lookupCategoryId
	 * @return
	 * @throws ServiceException
	 */
	public static Vector<LookupData> getLookupData(short lookupCategoryId) 
	 throws ServiceException {
		
		try {
			WorkflowSoap_PortType workflowWs = getWorkflowWsServicePort();
			String lookupData = workflowWs.getLookupData(lookupCategoryId);
			
			Log.debug("Found lookup data with category ID: " + lookupCategoryId);
			Log.debug(lookupData);
			
			// The lookup data is returned as raw XML. Turn this into something
			// useful.
			
			Document document = getDocument(lookupData);
			NodeList lookupDataNodes = document.getFirstChild().getChildNodes();
			
			Log.debug("Found " + lookupDataNodes.getLength() + 
			 " LookupData nodes");
			
			Vector<LookupData> lookupDataList = new Vector<LookupData>();
			
			for (int i=0; i<lookupDataNodes.getLength(); i++) {
				Node lookupDataNode = lookupDataNodes.item(i);
				
				String thisId = 
				 lookupDataNode.getFirstChild().getFirstChild().getNodeValue();
				
				String thisValue =
				 lookupDataNode.getLastChild().getFirstChild().getNodeValue();
				
				lookupDataList.add(
					new LookupData(Integer.parseInt(thisId), thisValue)
				);
			}
			
			return lookupDataList;
			
		} catch (Exception e) {
			String msg = "Failed to get lookup data via SPP web service: " + 
			 e.getMessage();
			
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}
	
	public static DataResultSet getLookupDataResultSet(short lookupCategoryId) 
	 throws ServiceException {
		
		Vector<LookupData> lookupDataList = getLookupData(lookupCategoryId);
		
		DataResultSet rsLookupData = new DataResultSet(
			new String[] { "LOOKUP_ID", "LOOKUP_VALUE" }	
		);
		
		for (LookupData lookupData : lookupDataList) {
			Vector<String> values = new Vector<String>();
			
			values.add(Integer.toString(lookupData.getId()));
			values.add(lookupData.getValue());
			
			rsLookupData.addRow(values);
		}
		
		return rsLookupData;
	}
	
	public static DataResultSet getComplaintCategoriesResultSet() 
	 throws ServiceException {
		
		Vector<ComplaintCategory> categoryList = getComplaintCategories();
		
		DataResultSet rsComplaintCategories = new DataResultSet(
			new String[] { 	"COMPLAINT_CATEGORY_ID", 
							"COMPLAINT_CATEGORY_NAME" }	
		);
		
		for (ComplaintCategory category : categoryList) {
			Vector<String> values = category.getResultSetRowValues();	
			rsComplaintCategories.addRow(values);
		}
		
		return rsComplaintCategories;
	}
	
	public static DataResultSet getComplaintSubCategoriesResultSet
	 (int categoryId) throws ServiceException {
		
		Vector<ComplaintSubCategory> subCategoryList = 
		 getComplaintSubCategories(categoryId);
		
		DataResultSet rsComplaintSubCategories = new DataResultSet(
			new String[] { 	"COMPLAINT_SUBCATEGORY_ID", 
							"COMPLAINT_SUBCATEGORY_NAME" }	
		);
		
		for (ComplaintSubCategory category : subCategoryList) {
			Vector<String> values = category.getResultSetRowValues();	
			rsComplaintSubCategories.addRow(values);
		}
		
		return rsComplaintSubCategories;
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
	public static String getSuffixChars(String accountNo){
		
		if(accountNo == null) 
			return "";
		
		String suffixLast 		= "";
		String suffixSecondLast = "";
		String suffixThirdLast 	= "";
		String suffixFourthLast = "";
		
		if(accountNo.length() >= 4)
			suffixFourthLast = accountNo.substring(accountNo.length() -4, accountNo.length()-3);
			
		if(accountNo.length() >= 3)
			suffixThirdLast = accountNo.substring(accountNo.length() -3, accountNo.length()-2);
		
		if(accountNo.length() >= 2)
			suffixSecondLast = accountNo.substring(accountNo.length() -2, accountNo.length()-1);
		
		if(accountNo.length() >= 1)
			suffixLast = accountNo.substring(accountNo.length() -1, accountNo.length());
		
		//If the last four characters are A-Z ones
		if(isAlphaChar(suffixFourthLast) && isAlphaChar(suffixThirdLast) 
			&& isAlphaChar(suffixSecondLast) && isAlphaChar(suffixLast)){
			
			Log.error("Account number " + accountNo + " ends with four or more Fund characters." +
					" This is an unsupported number of characters, returning an empty fund.");
			return "";
		}else{	
			if(isAlphaChar(suffixThirdLast) && isAlphaChar(suffixSecondLast) && isAlphaChar(suffixLast))
				return suffixThirdLast + suffixSecondLast + suffixLast;
			else if(isAlphaChar(suffixSecondLast) && isAlphaChar(suffixLast))
				return suffixSecondLast + suffixLast;
			else if(isAlphaChar(suffixLast))
				return suffixLast;
			else	
				return "";
		}
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
	
	/**
	 * Executes a custom search with the passed query text and puts the resulting
	 * result sets in the binder. Formerly part of the getCaseDocumentList() method
	 *  
	 * @param queryString
	 * @param m_binder
	 * @throws DataException 
	 */
	public static void executeCustomSearch(String queryString, DataBinder m_binder, FWFacade ucmFacade) 
	 throws DataException {
		Log.debug("executeCustomSearch >>");
		Log.debug("Running query: " + queryString);
		
		String HTTP_HOST = m_binder.getEnvironmentValue("HTTP_HOST");
		
		SPPJobProfile workflowProfile = SPPJobProfile.JOB_PROFILE;
		
		//Get fields and mapping
		String[] splitUcmMetadata = workflowProfile.getUcmVarNames();
		String[] splitSppMetadata = workflowProfile.getSppVarNames();
		
		String[] cols = {"title","author","URL"};
		DataResultSet rsCaseDocuments = new DataResultSet(cols);
		DataResultSet extraResults =  null;
		LWDataBinder caseDocs = new LWDataBinder();
		
		caseDocs.putLocal("QueryText", queryString);
		caseDocs.putLocal("SortField", "dInDate");
		caseDocs.putLocal("SortOrder", "DESC");
		caseDocs.putLocal("ResultCount", "2048");
		caseDocs.putLocal("IdcService", "GET_SEARCH_RESULTS");

		LWFacade lwFacade = new LWFacade();

		try {

			//Execute the search and populate the search results in the searchResults result set.
			LWResultSet searchResults = lwFacade.executeService(caseDocs, "SearchResults");

			extraResults = new DataResultSet(splitSppMetadata);

			//Extract our values into a new result set rsCaseDocuments
			if (searchResults != null && searchResults.getNumRows() > 0) {
				do {
					//Add standard results
					Vector v = new Vector();
					
					String dDocName 		= searchResults.getStringValue(searchResults.getFieldIndex("dDocName"));
					String dSecurityGroup 	= searchResults.getStringValue(searchResults.getFieldIndex("dSecurityGroup"));
					String dDocAccount 		= searchResults.getStringValue(searchResults.getFieldIndex("dDocAccount"));
					String dDocType 		= searchResults.getStringValue(searchResults.getFieldIndex("dDocType"));
					String dDocTitle 		= searchResults.getStringValue(searchResults.getFieldIndex("dDocTitle"));
					String dDocAuthor 		= searchResults.getStringValue(searchResults.getFieldIndex("dDocAuthor"));
					
					String xParentDocName	= searchResults.getStringValue(searchResults.getFieldIndex("xParentDocName"));
					String xPdfDocName		= searchResults.getStringValue(searchResults.getFieldIndex("xPdfDocName"));
					String xFolderName		= searchResults.getStringValue(searchResults.getFieldIndex("xFolderName"));
					
					String url = searchResults.getStringValue(searchResults.getFieldIndex("URL"));
					
					if(dDocType.equals("ChildDocument")){
						url = DocumentUtils.generateDocUrl(
								dSecurityGroup, dDocAccount, dDocType, dDocName, xParentDocName, xPdfDocName, xFolderName, ucmFacade);
						Log.info("Generated URL for ChildDocument " + dDocName + ": " + url);
					}else{
						url = "http://" + HTTP_HOST + url;
					}
					
					v.add(dDocTitle);
					v.add(dDocAuthor);
					v.add(url.toLowerCase());
					
					rsCaseDocuments.addRow(v);
					
					//Add extra results
					Vector vExtraProps = new Vector();
					
					for(int i=0; i<splitSppMetadata.length; i++){
						
							String field = "";
							
							try{
								field = searchResults.getStringValue(searchResults.getFieldIndex(splitUcmMetadata[i]));
							}catch(Exception e){
								Log.error("Unable to populate search results field "+ splitUcmMetadata[i]);
							}
							
							vExtraProps.add(field);	
							//Log.info("Populating search results field "+ splitUcmMetadata[i] + " with:" + field);
						
					}
					
					extraResults.addRow(vExtraProps);
				} while (searchResults.next());
			}

			Log.info("SPP_WSDL: Returning " + rsCaseDocuments.getNumRows() + " documents");

			m_binder.addResultSet("CaseResultSet", rsCaseDocuments);
			m_binder.addResultSet("ExtraResults", extraResults);
			Log.info("SPP_WSDL: ...FINISHED PERFORMING SEARCH");
		}catch (Exception e){
			Log.info("SPP_WSDL: Unable to perform search: " + e.getMessage(), e);
		}
		
		Log.debug("executeCustomSearch <<");
	}
	
	/**
	 * Required when ChildDocuments give birth ChildrenDocuemnts of their own.
	 * The pdf reference needs to be taken as the xPdfDocName if available, and
	 * shouldn't simply point to the parent (the parent ChildDocument) content 
	 * as this will display the metadata only file.
	 *  
	 * @param lwdParent
	 * @return
	 */
	public static String getPdfDocName(LWDocument lwdParent){
		String dDocName = "";
		
		try{
			dDocName = lwdParent.getDocName();
			String pdfReference 	= lwdParent.getAttribute("xPdfDocName");
			String parentReference 	= lwdParent.getAttribute("xParentDocName");
			
			if(!StringUtils.stringIsBlank(pdfReference)){
				return pdfReference;
			}else{
				return parentReference;
			}
		}catch(Exception e){
			Log.error("getPdfDocName >> Error getting pdf docname from document " + 
					dDocName + ":" + e.getMessage());
			return "";
		}
	}
	
	public static String getPdfDocName(String dDocName){
		try{
			return getPdfDocName(new LWDocument(dDocName, true));
		}catch(Exception e){
			Log.error("getPdfDocName >> Error instantiating document " + 
					dDocName + ":" + e.getMessage());
			return "";
		}
	}
	
	/** Adds the UCM DOC_INFO page to the given binder as the RedirectUrl.
	 *  The passed params string should be a list of URL paramaters, e.g.
	 *  
	 *  &abc=1&def=2
	 *  
	 * @param binder
	 * @param params
	 */
	private static void addDocInfoRedirectUrl
	 (DataBinder binder, String dID, String params) {
		
		binder.putLocal("RedirectUrl", "idcplg?IdcService=DOC_INFO&dID=" 
		 + dID + params);
	}
	
	/** Fetches any user notes which were appended to a particular
	 *  bundle item in the Iris interface.
	 *  
	 *  Only the message portion of notes are returned - user/date
	 *  information is dropped here.
	 *  
	 * @param bundleRef
	 * @param docName
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static Vector getBundleItemNotes
	 (String bundleRef, String docName, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		binder.putLocal("bundleRef", bundleRef);
		binder.putLocal("docName", docName);
		
		DataResultSet rs =
		 facade.createResultSet("qGetBundleItemNotes", binder);
		
		if (!rs.isEmpty()) {
			Vector v = new Vector();
			
			do {
				String message = rs.getStringValue(0);
				Log.debug("Found a bundle item note: '" + message + "'");
				
				v.add(message);
			} while (rs.next());
			
			return v;
			
		} else
			return null;
	}
	
	/** Adds job notes to existing SPP jobs, via SPP web service.
	 * 
	 * @param jobId GUID generated by SPP for each new job
	 * @param notes list of string messages to be passed to SPP as notes
	 */
	public static void addJobNotes(String sessionId, String jobId, Vector notes) {
		
		Log.debug("Calling addNote SPP web service for " + notes.size() 
		 + " notes, session ID: " + sessionId + ", jobId: " + jobId);
		
		Iterator i = notes.iterator();
		
		while (i.hasNext()) {
			String note = (String)i.next();
			
			try {
				getPlatformManagerWsPort().addNote(sessionId, jobId, note);
			} catch (Exception e) {
				Log.error("Failed to call addNote SPP web service: "
				 + "jobId: " + jobId + ", note: '" + note + "':"
				 + e.getMessage(), e);
			}
		}
	}
	
	public static void testWorkflowJobServices(String wfId) {
		
		try {
			Vector<LookupData> lookupData = getLookupData((short) 4);
			Log.debug("Found " + lookupData.size() + " bits of lookup data.");
			
			String jobId = getWorkflowWsServicePort().getJobID(wfId);
			
			getComplaintCategories();
			getComplaintSubCategories(1);
			
			Log.debug("Found SPP Job ID: " + jobId);
			
			Log.debug("Adding test note");
			
			// Login with special 'UCM' account
			LogonDetails logonDetails = 
			 getPlatformManagerWsPort().logon
			 (SPP_LOGON_USER, SPP_INTERNET_LOGON_TYPE, true);
			
			getPlatformManagerWsPort().addNote
			 (logonDetails.getSessionId(), jobId, "A test note");

			Notes[] notes = 
			 getPlatformManagerWsPort().getNotes
			 (logonDetails.getSessionId(), jobId);
			
			Log.debug("Found " + notes.length + " notes for Job.");
			
			for (int i=0; i<notes.length; i++) {
				Log.debug("Note " + i);
				Log.debug("Creator: " + notes[i].getCreator());
				Log.debug("Date: " + DateFormatter.getTimeStamp(
				 notes[i].getCreationDate().getTime()));
				Log.debug("Message: " + notes[i].getNote());
			}
			
			JobProperties6 jobProps = getPlatformManagerWsPort().
			 getJobProps6(logonDetails.getSessionId(), jobId, false);
			
			Log.debug("Job props6 status: " + jobProps.getJobStatus());
			Log.debug("Job props6 state: " + jobProps.getJobState());
			
		} catch (Exception e) {
			Log.error("Failed to call SPP web service: "
			 + "wfId: " + wfId + ": " + e.getMessage(), e);
		}
	}
	
	/** Used to set NTLM authentication credentials on Aurora
	 *  web service requests.
	 *  
	 *  Referenced in the createCall() method in the AuroraWSSoap
	 *  class.
	 *  
	 * @param call
	 */
	public static void setWorkflowWSCredentials(Call call) {
		
        // TM: use HttpClient for NTLM auth.
        CommonsHTTPSender reqConnectionHandler = new CommonsHTTPSender();
        CommonsHTTPSender respConnectionHandler = new CommonsHTTPSender();
        
        call.setClientHandlers(reqConnectionHandler,respConnectionHandler);
		
		call.setUsername(WORKFLOW_AUTH_USERNAME);
		call.setPassword(WORKFLOW_AUTH_PASSWORD);
		
		Log.debug("Setting web service login user: " + call.getUsername());
	}
}
