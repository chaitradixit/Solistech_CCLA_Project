package com.ecs.stellent.spp.dependantdocs;

import idcbean.data.LWResultSet;
import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.data.ResultSet;
import intradoc.data.ResultSetUtils;
import intradoc.data.Workspace;
import intradoc.server.Service;
import intradoc.shared.SharedObjects;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import com.ecs.stellent.auditlog.AuditUtils;
import com.ecs.stellent.iris.batch.BatchDocumentServices;
import com.ecs.stellent.spp.service.SppIntegrationUtils;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals.UCMFieldNames;
import com.ecs.ucm.ccla.data.DocumentClass;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.DateFormatter;
import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.embedded.FWFacade;

public class DependantDocServices extends Service{
	
	DependantDocUtils dependantDocUtils = new DependantDocUtils();
	
	private final String APP_DOCTYPE = "APP";
	
	/**
	 * Sets the passed root document to be the root document for all the 
	 * csv passed dependant documents.
	 * 
	 * @throws ServiceException
	 */
	public void setDependantDocsForRootDoc() throws ServiceException{
		Log.debug("setDependantDocsForRootDoc >>");
		
		String rootDocNameStr 		= m_binder.getLocal("rootDocName");
		String dependantDocNamesStr = m_binder.getLocal("dependantDocNames");
		String isSupportingStr 		= m_binder.getLocal("isSupporting");		
		
		if(StringUtils.stringIsBlank(rootDocNameStr)){
			Log.error("Parameter rootDocName not found");
			throw new ServiceException("Parameter rootDocName not found");
			
		}else if(StringUtils.stringIsBlank(dependantDocNamesStr)){
			Log.error("Parameter dependantDocNames not found");
			throw new ServiceException("Parameter dependantDocNames not found");
			
		}else if(StringUtils.stringIsBlank(isSupportingStr)){
			Log.error("Parameter isSupporting not found");
			throw new ServiceException("Parameter isSupporting not found");
		
		}
		
		String[] dependantDocNames = StringUtils.stringToArray(dependantDocNamesStr);
		
		boolean isSupporting = isSupportingStr.equals("1");
		
		String msg = "";
		
		//If isSPP flag is found, and the date of document is old (i.e. before
		//the new supporting doc system went live) - we need to populate the table
		//with supporting documents so it's using the new system to reconcile supporting
		//documents
		String isSpp = m_binder.getLocal("isSPP");
Log.debug("*****isSPP:" + isSpp);
		if(!StringUtils.stringIsBlank(isSpp)){
			initDependantDocsForRootDoc(rootDocNameStr);
		}
		
		try{
			dependantDocUtils.setDependantDocsForRootDoc(
					m_workspace, rootDocNameStr, dependantDocNames, isSupporting);
			msg = "Updated dependant docs for root doc: " + rootDocNameStr;
		}catch(ServiceException se){
			msg = "Unable to update dependant docs for root doc: " + se.getMessage();
			throw new ServiceException(se);
		}finally{
			
			
			//Audit the web service call
			//Param1: name of the root document
			//Param2: the dependant documents (csv)
			//Param3: whether the call was made from SPP or not (1/0)
			
			Vector params = new Vector();
			params.add(rootDocNameStr);
			params.add(dependantDocNamesStr);
			
			if(!StringUtils.stringIsBlank(isSpp)){
				params.add("1");
				Log.debug("Method called via SPP web service call");
			}else{
				params.add("0");
			}
			
			AuditUtils.addAuditEntry("DEPENDANT_DOC_WEBSERVICE", "SET_ROOT_FOR_DEPS", 
					rootDocNameStr,
					 "Set dependant docs for root doc", 
					 m_userData.m_name,
					 msg,
					 params);
		}
		Log.debug("setDependantDocsForRootDoc <<");
	}
	

	/**
	 * Sets the passed dependant document to have a dependancy on all the 
	 * csv passed root documents.
	 * 
	 * @throws ServiceException
	 */
	public void setDependantDocForRootDocs() throws ServiceException{
		Log.debug("setDependantDocForRootDocs >>");
		
		String rootDocNamesStr 		= m_binder.getLocal("rootDocNames");
		String dependantDocNameStr 	= m_binder.getLocal("dependantDocName");
		String isSupportingStr 		= m_binder.getLocal("isSupporting");
		
		if(StringUtils.stringIsBlank(rootDocNamesStr)){
			Log.error("Parameter rootDocNames not found");
			throw new ServiceException("Parameter rootDocNames not found");
			
		}else if(StringUtils.stringIsBlank(dependantDocNameStr)){
			Log.error("Parameter dependantDocName not found");
			throw new ServiceException("Parameter dependantDocName not found");
			
		}else if(StringUtils.stringIsBlank(isSupportingStr)){
			Log.error("Parameter isSupporting not found");
			throw new ServiceException("Parameter isSupporting not found");
		
		}
		
		String[] rootDocNames = StringUtils.stringToArray(rootDocNamesStr);
		
		boolean isSupporting = isSupportingStr.equals("1");
		
		//If isSPP flag is found, and the date of document is old (i.e. before
		//the new supporting doc system went live) - we need to populate the table
		//with supporting documents so it's using the new system to reconcile supporting
		//documents
		String isSpp = m_binder.getLocal("isSPP");
Log.debug("*****isSPP:" + isSpp);
		if(!StringUtils.stringIsBlank(isSpp)){
			initDependantDocsForRootDocs(rootDocNames);
		}
		
		String msg = "";
		
		try{
			dependantDocUtils.setDependantDocForRootDocs(
					m_workspace, rootDocNames, dependantDocNameStr, isSupporting);
			msg = "Updated dependant doc for root docs: " + rootDocNamesStr;
		}catch(ServiceException se){
			msg = "Unable to update dependant doc for root docs: " + se.getMessage();
			throw new ServiceException(se);
		}finally{
			
			
			//Audit the web service call
			//Param1: name of the root documents (csv)
			//Param2: the dependant document docname
			//Param3: whether the call was made from SPP or not (1/0)
			
			Vector params = new Vector();
			params.add(rootDocNamesStr);
			params.add(dependantDocNameStr);
			
			if(!StringUtils.stringIsBlank(isSpp)){
				params.add("1");
				Log.debug("Method called via SPP web service call");
			}else{
				params.add("0");
			}
			
			AuditUtils.addAuditEntry("DEPENDANT_DOC_WEBSERVICE", "SET_DEP_FOR_ROOTS", 
					rootDocNamesStr,
					 "Set dependant doc for root docs", 
					 m_userData.m_name,
					 msg,
					 params);
		}
		
		Log.debug("setDependantDocForRootDocs <<");
	}
	
	/**
	 * Called only via SPP web service calls
	 * 
	 * Try and get dependant docs from TDEPENDANTDOCS table. If no entries are
	 * found, use the old method of returning all non MAND/APP documents as 
	 * supporting.
	 * 
	 * @throws ServiceException
	 * @throws DataException 
	 */
	public void getDependantDocsForRootDoc() throws ServiceException, DataException{
		Log.debug("getDependantDocsForRootDoc >>");
		
		String rootDocName 		= m_binder.getLocal("rootDocName");
		String workflowId 		= m_binder.getLocal("workflowId");
		String isSupportingStr 	= m_binder.getLocal("isSupporting");
		FWFacade ucmFacade = CCLAUtils.getFacade(m_workspace);
		if(StringUtils.stringIsBlank(isSupportingStr)){
			Log.error("Parameter isSupporting not found");
			throw new ServiceException("Parameter isSupporting not found");
			
		}else if(StringUtils.stringIsBlank(workflowId) 
				&& StringUtils.stringIsBlank(rootDocName) ){
			Log.error("Parameters workflowId and rootDocName not found");
			throw new ServiceException("Parameters workflowId and rootDocName not found");
		}
		
		DataResultSet rsDependantDocs = null;
		
		if(!StringUtils.stringIsBlank(rootDocName)){
			rsDependantDocs = dependantDocUtils.getDependantDocsForRootDoc(m_workspace, 
					rootDocName, isSupportingStr.equals("1"));
		}
		
		//Build a queryString to execute. If we need to use the old method, pass
		//the same queryString that would have been passed using the old method.
		//Otherwise, construct a new queryString for the dependant docs of the
		//TDEPENDENTDOCS table and return them in the same way.
		
		//The base of the query should always have the following env value query
		String queryString = SharedObjects.getEnvironmentValue("SPP_INT_GET_CASE_DOCS_QUERY");
		
		if(rsDependantDocs != null && !rsDependantDocs.isEmpty()){
			Log.debug("Return supporting documents - using NEW method");
			
			//Use new method to return supporting docs
			queryString += " <AND> (";
						
			do {
				String dDocName = ResultSetUtils.getValue(rsDependantDocs, "DEP_DOCNAME");
				
				if(rsDependantDocs.getCurrentRow() != 0){
					queryString += " <OR> ";
				}
				
				queryString += " dDocName <MATCHES> `" + dDocName + "`";
				
			}while (rsDependantDocs.next());
			
			queryString += ")";
		}else{
			Log.debug("Return supporting documents - using OLD method");
			//Use old method to return supporting docs
			
			//TEMPORARY FIX BECAUSE SPP IS NOT ALWAYS PASSING WORKFLOWID AS IT SHOULD BE
			if(StringUtils.stringIsBlank(workflowId)){
				//Try and get the workflowID from the rootDocName
				try{
					LWDocument lwd = new LWDocument(rootDocName, true);
					workflowId = lwd.getAttribute("xBatchNumber");
				}catch(Exception e){
					String msg = "Parameter workflowId not found and unable to resolve from " +
					"rootDocName " + rootDocName + ":" + e.getMessage();
					Log.error(msg);
					throw new ServiceException(msg);
				}
			}
			
			queryString += " <AND> xBatchNumber = `" + workflowId + "`";
		}
		
		SppIntegrationUtils.executeCustomSearch(queryString, m_binder, ucmFacade);
		
		Log.debug("getDependantDocsForRootDoc <<");
	}
	
	
	/**
	 * Remove a table reference for a dependant documents dependancy on
	 * a root document.
	 * 
	 * @throws ServiceException
	 */
	public void deleteDependantDocForRootDoc()  throws ServiceException{
		Log.debug("deleteDependantDocForRootDoc >>");	
		
		String rootDocNameStr 		= m_binder.getLocal("rootDocName");
		String dependantDocNameStr 	= m_binder.getLocal("dependantDocName");
		String isSupportingStr 		= m_binder.getLocal("isSupporting");
		
		if(StringUtils.stringIsBlank(rootDocNameStr)){
			Log.error("Parameter rootDocName not found");
			throw new ServiceException("Parameter rootDocName not found");
			
		}else if(StringUtils.stringIsBlank(dependantDocNameStr)){
			Log.error("Parameter dependantDocName not found");
			throw new ServiceException("Parameter dependantDocName not found");
			
		}else if(StringUtils.stringIsBlank(isSupportingStr)){
			Log.error("Parameter isSupporting not found");
			throw new ServiceException("Parameter isSupporting not found");
		
		}
		
		boolean isSupporting = isSupportingStr.equals("1");
		
		dependantDocUtils.deleteDependantDocForRootDoc(
				m_workspace, rootDocNameStr, dependantDocNameStr, isSupporting);
		
		Log.debug("deleteDependantDocForRootDoc <<");
	}
	
	/**
	 * Called only from UCM.
	 * 
	 * Sets dependant documents for an envelope, if the envelope contains a MAND 
	 * or an APP. Set all the other documents (which are not duplicate and can be 
	 * supporting documents) to be supporting documents of the APPs/MANDs.
	 * 
	 * @throws ServiceException
	 * @throws DataException 
	 */
	public void initDependantDocsForEnvelope() throws ServiceException, DataException{
		Log.debug("initDependantDocsForEnvelope >>");
		long startTime = System.currentTimeMillis();
		
		String bundleRef 		= m_binder.getLocal("bundleRef");
		String isSupportingStr 	= m_binder.getLocal("isSupporting");
		
		boolean performInit = true;
		String performInitStr 	= m_binder.getLocal("performInit");
		if(!StringUtils.stringIsBlank(performInitStr) && performInitStr.equals("0"))
			performInit=false;
		
		if(StringUtils.stringIsBlank(bundleRef)){
			Log.error("Parameter bundleRef not found");
			throw new ServiceException("Parameter bundleRef not found");
			
		}else if(StringUtils.stringIsBlank(isSupportingStr)){
			Log.error("Parameter isSupporting not found");
			throw new ServiceException("Parameter isSupporting not found");
		
		}
		
		boolean isSupporting = (isSupportingStr.equals("1"));
		
		ResultSet batchItems 		= m_binder.getResultSet("rsBatchItems");
		DataResultSet rsBatchItems 	= null;
		
		if (batchItems != null) {
			rsBatchItems = new DataResultSet();
			rsBatchItems.copy(batchItems);
		} else {
			try{
				rsBatchItems = BatchDocumentServices.getBatchItems(bundleRef,m_workspace);
			}catch(DataException de){
				String errMsg = "Unable to retrieve batch items for bundleRef " + bundleRef 
					+ ":" + de.getMessage();
				Log.error(errMsg, de);
				throw new ServiceException(errMsg);
			}
		}
		
		rsBatchItems.first();
		
		List<String> mandAndAppList = new ArrayList<String>();
		
		// If any other documents have been previously uploaded as a supporting doc
		// (without the bundle refs set), the method below will fetch them and
		// append them to the ResultSet.
		rsBatchItems = addExtraneousSupportingDocs(rsBatchItems, m_workspace);
		rsBatchItems.first();
		
		//Add APPs and MANDs in the bundle to a list
		do{
			String docClassStr = ResultSetUtils.getValue
			 (rsBatchItems, UCMFieldNames.DocClass);
			
			Log.debug("Found a doc with class " + docClassStr);
			
			if(!StringUtils.stringIsBlank(docClassStr)) {
				DocumentClass docClass = DocumentClass.getCache().getCachedInstance
				 (docClassStr);
			
				if (docClass.isMandate()) {
				
					String dDocName = ResultSetUtils.getValue
					 (rsBatchItems,UCMFieldNames.DocName);
					
					Log.debug("+Marking as mandate/app");
					mandAndAppList.add(dDocName);
				}
			}
			
		} while (rsBatchItems.next());
		
		rsBatchItems.first();
		Log.debug("Found " + mandAndAppList.size() + " MAND/APP items.");
		
		//If the bundle contains (more than one MAND or more than one APP) or
		//(an APP and a MAND) there is ambiguity within the bundle because we 
		//dont know which supporting documents correspondent to which APP/MAND
		boolean ambiguous = false;
		
		if(mandAndAppList.size() > 1) {
			m_binder.putLocal("bundleContainsAmbiguity", "1");
			ambiguous = true;
		}
			
		// You can use performInit set to 0 to only use this service to put the 
		// bundleContainsAmbiguity flag in the binder
		if(performInit){
			
			/*
			 * 
			 * HashMap docClassMap = null;
			if(mandAndAppList.size() > 0)
				docClassMap = SppIntegrationServices.getDocClassMappingForSupportingDocs(m_binder);
			*/
			
			long supptStartTime = System.currentTimeMillis();
			
			//For each MAND/APP in the bundle, add the other bundle documents as supporting 
			//documents of it
			for (int i=0; i<mandAndAppList.size(); i++) {
				String rootDocName = (String)mandAndAppList.get(i);
				
				Log.debug("Marking supporting documents for root doc " + rootDocName);
			
				rsBatchItems.first();
				
				do {
					String isDuplicateStr = ResultSetUtils.getValue(rsBatchItems, "xStatus");
					boolean isDuplicate = 
						(!StringUtils.stringIsBlank(isDuplicateStr) 
						 && isDuplicateStr.equals("Duplicate"));  
					
					String dependantDocName = ResultSetUtils.getValue(rsBatchItems, "dDocName");
					String documentClass 	= ResultSetUtils.getValue(rsBatchItems, "xDocumentClass");
					
					DocumentClass docClass = null;
					
					if (!StringUtils.stringIsBlank(documentClass))
						docClass = DocumentClass.getCache().getCachedInstance(documentClass);
					
					
					// If bundle doc is not a MAND/APP and not a duplicate, set it to be a 
					// dependant/supporting doc based on its type.
					if(!mandAndAppList.contains(dependantDocName) && !isDuplicate) {
						boolean isSupportingDoc = docClass!=null && docClass.isSupporting();
						boolean isMultiDoc		= docClass!=null && docClass.isMultiDoc();
						
						try {
							
							if (isSupportingDoc) {
								// Mark as supporting document (table only)
								Log.debug(dependantDocName + " is a supporting document of " + rootDocName);
								
								dependantDocUtils.setDependantDocForRootDoc(m_workspace, rootDocName, 
								 dependantDocName, true);
								
							} else if (!isMultiDoc) {
								// Mark as dependant document (table + metadata field), if there
								// is only 1 MAND/APP in the bundle.
								
								Log.debug(dependantDocName + " is dependant on " + rootDocName + ", doing nothing.");
								
								/*
								dependantDocUtils.setDependantDocForRootDoc(m_workspace, rootDocName, 
								 dependantDocName, false);
								
								LWDocument lwDoc = new LWDocument(dependantDocName);
								lwDoc.setAttribute("xDependantDocName", rootDocName);
								*/
							}
						} catch (Exception e) {
							Log.error("Unable to set dependancy for dependantDoc: " + dependantDocName
									+ " to rootDoc: " + rootDocName + ": " + e.getMessage(), e);
						}
					}
				} while (rsBatchItems.next());
			}
			
			Log.debug("Time taken to mark supporting docs: " +
			 ((System.currentTimeMillis() - supptStartTime)/1000D) + " seconds");
		}
		
		Log.debug("initDependantDocsForEnvelope << bundle: " + bundleRef + ", time taken: " +
		 ((System.currentTimeMillis() - startTime)/1000D) + " seconds");
	}
	
	/**
	 * Scans the given rsBatchItems ResultSet for a non-zero workflow ID. If one is found,
	 * this is fed into a query which searches for all items with a matching workflow ID
	 * and a null bundle ref.
	 * 
	 * @param bundleRef
	 */
	private DataResultSet addExtraneousSupportingDocs(DataResultSet rsBatchItems,
			Workspace workspace) {
		
		Log.debug("addExtraneousSupportingDocs >>");
		long startTime = System.currentTimeMillis();
		
		//Get the workflow id for the envelope
		String workflowId = null;
		
		do {
			String docWorkflowId = ResultSetUtils.getValue(rsBatchItems, "xBatchNumber");
			if(!StringUtils.stringIsBlank(docWorkflowId) && !docWorkflowId.equals("0")){
				workflowId = docWorkflowId;
				break;
			}
		} while (rsBatchItems.next());
		
		if (workflowId == null) {
			Log.debug("Unable to look up extraneous docs for bundle, no workflow ID was set.");
			return rsBatchItems;
		}
		
		Log.debug("Looking up extraneous documents for workflowId: " + workflowId);
		
		/*
		String sql = "";

		sql = "SELECT * FROM Revisions " +
			"INNER JOIN DocMeta ON Revisions.dID = DocMeta.dID " +
			"WHERE (Revisions.dRevRank = 0) " +
			"AND (NOT (Revisions.dStatus = 'DELETED'))" +  
			"AND (NOT (Revisions.dStatus = 'EXPIRED')) " +
			"AND ((Revisions.dDocType = 'Document') OR (Revisions.dDocType = 'ChildDocument'))" +
			"AND ((DocMeta.xBundleRef IS NULL) AND (DocMeta.xBatchNumber = '" + workflowId + "'))";
		*/
			
		// Fetch all items with given workflow ID set. DJ: User upload, is supporting doc type?
		try{
			DataBinder binder = new DataBinder();
			binder.putLocal("workflowId", workflowId);
			ResultSet rs = workspace.createResultSet("qGetDocsByWorkflowId", binder);
			
			DataResultSet rsExtraneousItems = new DataResultSet();
			rsExtraneousItems.copy(rs);
			
			Log.debug("Got " + rsExtraneousItems.getNumRows() + " extraneous items.");
			
			if(!rsExtraneousItems.isEmpty()){
				do {
					rsBatchItems.addRow(rsExtraneousItems.getCurrentRowValues());
				} while(rsExtraneousItems.next());	
			}
			
			Log.debug("addExtraneousSupportingDocs << " +
			 "wfID: " + workflowId + ", error: 0, extraneous items: " + rsExtraneousItems.getNumRows() + 
			 ", time taken: " + (System.currentTimeMillis()/startTime)/1000D + "s");
			
			return rsBatchItems;
			
		} catch (Exception e) {
			Log.error("Unable to run query/perform merge :" + e.getMessage());
			Log.debug("addExtraneousSupportingDocs << " +
			 "wfID: " + workflowId + ", error: 1, extraneous items: 0 " + 
			 ", time taken: " + (System.currentTimeMillis()/startTime)/1000D + "s");
			
			return rsBatchItems;
		}
	}


	/**
	 * Called from SPP only.
	 * 
	 * Provides a way to check in mutliple supporting documents with the same PDF for multiple root 
	 * documents. This services assumes a document has already been checked in which will hold the 
	 * PDF of the supporting doc (i.e. the PDF document is just a place holder and has no pertinant
	 * doc meta associated with it).
	 * 
	 * A number of ChildDocuments will be checked in that point to the above PDF document. The CSV 
	 * metadata passed in will be set for each ChildDocument. The ChildDocuments will also be marked
	 * as supporting documents of the passed rootDocName.
	 * 
	 * csvClientIds/csvAccountNumbers are optional parameters. This is due to APP items, which
	 * may not have either client IDs or account numbers assigned to them.
	 * 
	 * Test Url: ?IdcService=CCLA_DEP_DOCS_CREATE_DEPENDANT_DOCS_FOR_ROOT_DOCS_WS&pdfDocName=PDF_DOCNAME
	 * &csvRootDocs=r1,r2&csvEnvelopeIds=e1,e2&csvCompanyNames=c1,c2&csvClientIds=cl1,cl2
	 * &csvAccountNumbers=ac1,ac2&isSupporting=1&documentClass=DEPCHK
	 */
	public void createDependantDocsForRootDocs() throws ServiceException{
		
		Log.debug("createDependantDocsForRootDocs >>");
		
		String pdfDocName 			= m_binder.getLocal("pdfDocName");
		String csvRootDocs 			= m_binder.getLocal("csvRootDocs");
		String csvWorkflowIds 		= m_binder.getLocal("csvEnvelopeIds");
		String csvCompanyNames 		= m_binder.getLocal("csvCompanyNames");
		String csvClientIds 		= m_binder.getLocal("csvClientIds");
		String csvAccountNumbers 	= m_binder.getLocal("csvAccountNumbers");
		String isSupportingStr 		= m_binder.getLocal("isSupporting");
		String documentClass		= m_binder.getLocal("documentClass");

		if(StringUtils.stringIsBlank(pdfDocName)){
			Log.error("Parameter pdfDocName not found");
			throw new ServiceException("Parameter pdfDocName not found");
			
		}else if(StringUtils.stringIsBlank(csvRootDocs)){
			Log.error("Parameter csvRootDocs not found");
			throw new ServiceException("Parameter csvRootDocs not found");
			
		}else if(StringUtils.stringIsBlank(csvWorkflowIds)){
			Log.error("Parameter csvEnvelopeIds not found");
			throw new ServiceException("Parameter csvEnvelopeIds not found");
			
		}else if(StringUtils.stringIsBlank(csvCompanyNames)){
			Log.error("Parameter csvCompanyNames not found");
			throw new ServiceException("Parameter csvCompanyNames not found");
		
		// TM: Made the second 2 optional to allow attachment of supporting docs
		// to mandate items.
		/*
		}else if(StringUtils.stringIsBlank(csvClientIds)){
			Log.error("Parameter csvClientIds not found");
			throw new ServiceException("Parameter csvClientIds not found");
			
		}else if(StringUtils.stringIsBlank(csvAccountNumbers)){
			Log.error("Parameter csvAccountNumbers not found");
			throw new ServiceException("Parameter csvAccountNumbers not found");
		*/
			
		}else if(StringUtils.stringIsBlank(isSupportingStr)){
			Log.error("Parameter isSupporting not found");
			throw new ServiceException("Parameter isSupporting not found");
			
		}else if(StringUtils.stringIsBlank(documentClass)){
			Log.error("Parameter documentClass not found");
			throw new ServiceException("Parameter documentClass not found");
			
		}
		
		//Dont use StringUtils to split as we need to retain the empty csv values
		//and not trim them. The -1 ensures that the last comma with a blank value
		//will not be ignored
		
		String[] rootDocs 			= csvRootDocs.split(",", -1);
		String[] workflowIds 		= csvWorkflowIds.split(",", -1);
		String[] companyNames 		= csvCompanyNames.split(",", -1);
		
		String[] clientIds = null;
		String[] accountNumbers = null;
		
		if (!StringUtils.stringIsBlank(csvClientIds))
			clientIds 			= csvClientIds.split(",", -1);
		
		if (!StringUtils.stringIsBlank(csvAccountNumbers))
			accountNumbers 	= csvAccountNumbers.split(",", -1);
		
		initDependantDocsForRootDocs(rootDocs);
		
		Log.debug("Checking in " + rootDocs.length + " ChildDocuemnts");
		
		Log.debug("csvRootDocs: '" + csvRootDocs + "':" + rootDocs.length);
		Log.debug("csvWorkflowIds: '" + csvWorkflowIds + "':" + workflowIds.length);
		Log.debug("csvCompanyNames: '" + csvCompanyNames + "':" + companyNames.length);
		
		if (clientIds != null)
			Log.debug("csvClientIds: '" + csvClientIds + "':" + clientIds.length);
		else
			Log.debug("csvClientIds: not supplied");
		
		if (accountNumbers != null)
			Log.debug("csvAccountNumbers: '" + csvAccountNumbers + "':" + accountNumbers.length);
		else
			Log.debug("csvAccountNumbers: not supplied");
		
		//Check in a metadata only ChildDocument content item linked to the passed pdf 
		for(int i=0; i<rootDocs.length; i++){
			String dDocTitle = workflowIds[i] + "-FromSPP-" + documentClass  + "_SubDoc" + (i+1);
			
			String childDocName = "";
			
			try{
				LWDocument lwd = new LWDocument();
				//Standard params
				lwd.setAttribute("dDocType", 		"ChildDocument");
				lwd.setAttribute("dDocTitle", 		dDocTitle);
				lwd.setAttribute("dSecurityGroup", 	"Public");
				lwd.setAttribute("dDocAuthor", 		"sysadmin");
				
				//Custom params
				lwd.setAttribute("createPrimaryMetaFile", 	"true");
				lwd.setAttribute("xSource", 				"User Upload");
				lwd.setAttribute("xBatchNumber",			workflowIds[i]);
				lwd.setAttribute("xCompany",				companyNames[i]);
				
				if (clientIds != null)
					lwd.setAttribute("xClientNumber",			clientIds[i]);
				
				if (accountNumbers != null)
					lwd.setAttribute("xAccountNumber",			accountNumbers[i]);
				
				lwd.setAttribute("xParentDocName", 			pdfDocName);
				lwd.setAttribute("xDocumentClass", 			documentClass);
			
				childDocName = lwd.checkin(null);
				
				Log.debug("Succesfully checked in ChildDocument " + i + 
				 " (" + childDocName + ")," + " marking it as supporting...");
				
				dependantDocUtils.setDependantDocForRootDoc(m_workspace, 
						rootDocs[i], childDocName, isSupportingStr.equals("1"));
				
			}catch(Exception e){
				String errMsg = "Unable to checkin or mark as supporting - ChildDocument " 
					+ i + "(dDocName:" + childDocName + "): " + e.getMessage();
				Log.error(errMsg, e);
				throw new ServiceException(errMsg, e);
			}
		}
		
		Log.debug("createDependantDocsForRootDocs <<");
	}
	
	private void initDependantDocsForRootDoc(String rootDocNames) {
		String[] csvRootDocs = StringUtils.stringToArray(rootDocNames);
		initDependantDocsForRootDocs(csvRootDocs);
	}
	
	/**
	 * Will initialise the TDEPENDANTDOCS table for the passed doc names. If they
	 * were checked in before a certain date (the new supporting doc system's go
	 * live date).
	 * 
	 * @param dependantDocNames
	 */
	private void initDependantDocsForRootDocs(String[] rootDocNames) {
		Log.debug("initDependantDocsForRootDocs >>");
		
		ArrayList bundleRefList = new ArrayList();
		SimpleDateFormat sdf = DateFormatter.getSystemSimpleDateFormat();
		Date newSystemLiveDateStr = null;
		
		try{
			newSystemLiveDateStr = sdf.parse("11/12/2009 00:00");
		}catch(Exception e){
			Log.error("Unable to parse date: " + e.getMessage());
		}
		
		for(int i=0; i<rootDocNames.length; i++){
			try{
				LWDocument lwd = new LWDocument(rootDocNames[i], true);
				
				Date inDate = sdf.parse(lwd.getAttribute("dInDate"));
				
				String bundleRef = lwd.getAttribute("xBundleRef");
				
				if(inDate.before(newSystemLiveDateStr) && !StringUtils.stringIsBlank(bundleRef)){
					bundleRefList.add(bundleRef);
				}
				
			}catch(Exception e){
				Log.error("Unable to instantiate document: " + e.getMessage());
			}
		}
		
		//Create a HashSet which allows no duplicates
		HashSet hashSet = new HashSet(bundleRefList);

		//Assign the HashSet to a new ArrayList
		ArrayList bundleRefListWithoutDuplicates = new ArrayList(hashSet) ;
		
		m_binder.putLocal("isSupporting", "1");
		
		if(bundleRefListWithoutDuplicates.size() > 0){
			
			try{
				FWFacade fwf = CCLAUtils.getFacade(m_workspace,false);
				DataResultSet drs = fwf.createResultSet("QDocClasses", m_binder);
				m_binder.addResultSet("rsDocClasses", drs);
			}catch(Exception e){
				Log.error("Unable to create FWFacade: " + e.getMessage());
			}
			
			for(int i=0; i<bundleRefListWithoutDuplicates.size(); i++){
				String bundleRef = (String) bundleRefListWithoutDuplicates.get(i);
				
				m_binder.putLocal("bundleRef", bundleRef);
				
				try {					
					initDependantDocsForEnvelope();
				} catch (Exception e) {
					Log.error("Unable to init documents for envelope " + bundleRef
							+": " + e.getMessage(), e);
				}
				
			}
		}
		
		Log.debug("initDependantDocsForRootDocs <<");
	}
}
