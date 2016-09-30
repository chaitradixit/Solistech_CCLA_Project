package com.ecs.ucm.ccla.commproc;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.provider.Providers;
import intradoc.server.FileService;
import intradoc.server.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals;
import com.ecs.ucm.ccla.Globals.UCMFieldNames;
import com.ecs.ucm.ccla.commproc.RoutingModuleManager.ModuleExecutionStatus;
import com.ecs.ucm.ccla.commproc.events.IInstructionEvent;
import com.ecs.ucm.ccla.commproc.events.InstructionEvent;
import com.ecs.ucm.ccla.commproc.events.TransactionReferenceEvent;
import com.ecs.ucm.ccla.data.SharedCols;
import com.ecs.ucm.ccla.data.comm.CommGroup;
import com.ecs.ucm.ccla.data.instruction.Instruction;
import com.ecs.ucm.ccla.data.instruction.InstructionAction;
import com.ecs.ucm.ccla.data.instruction.InstructionProcess;
import com.ecs.ucm.ccla.data.instruction.InstructionProcessApplied;
import com.ecs.ucm.ccla.data.instruction.RoutingModule;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.DataResultSetUtils;
import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.embedded.FWFacade;

public class CommProcService extends FileService {
	
	public void addDocumentBundle() throws DataException, ServiceException {
		String bundleRef 	= m_binder.getLocal("bundleRef");
		boolean persist  	= CCLAUtils.getBinderBoolValue(m_binder, "persist");
		
		//boolean append   	= CCLAUtils.getBinderBoolValue(m_binder, "append");
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace,true);
		
		Vector<Instruction> instructions = null;
		
		try {
			// Determine if bundle has already been converted. If true, run in append
			// mode.
			boolean append = CommGroup.getByUCMBatchRef(bundleRef, facade) != null;
			
			if (persist)
				facade.beginTransaction();
			
			instructions = 
			 CommUtils.addDocumentBundle
			 (bundleRef, persist, append, facade, m_userData.m_name);
			
			m_binder.putLocal("bundleTranslated", "1");
			
			if (!persist) {
				DataResultSet rsInstructions = 
				 Instruction.getSimpleResultSet(instructions);
				
				m_binder.addResultSet("rsInstructions", rsInstructions);
			}
			
			if (persist)
				facade.commitTransaction();
		
		} catch (BundleTranslateException bte) {
			
			bte.addFieldsToBinder(m_binder);
			
		} catch (Exception e) {
			String msg = "Error adding doc bundle into Comm/Instruction Registers: " +
			 e.getMessage();
			
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}

	/** Used to execute actions against a given Instruction Process.
	 *  
	 *  This will generally remove the suspended state of the instruction.
	 * @throws ServiceException 
	 * @throws DataException 
	 */
	public void applyProcessAction() throws ServiceException, DataException {
		
		Integer processApplId = CCLAUtils.getBinderIntegerValue
		 (m_binder, "INSTR_PROCESS_APPLIED_ID");
		
		Integer instructionActionId = CCLAUtils.getBinderIntegerValue
		 (m_binder, "INSTRUCTION_ACTION_ID");
		
		if (processApplId == null || instructionActionId == null)
			throw new ServiceException("Unable to apply Instruction Process action: " +
			 "Process ID/Action ID missing");
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		InstructionProcessApplied processAppl = InstructionProcessApplied.get
		 (processApplId, facade);
		
		if (processAppl == null)
			throw new ServiceException("Unable to apply Instruction Process action: " +
			 "Process ID " + processApplId + " not found");
		
		InstructionAction action = 
		 InstructionAction.getCache().getCachedInstance(instructionActionId);
		
		if (action == null)
			throw new ServiceException("Unable to apply Instruction Process action: " +
			 "Action ID " + instructionActionId + " invalid");
		
		try {
			facade.beginTransaction();
				
			InstructionProcessUtils.applyAction
			 (processAppl, action, facade, m_userData.m_name);
			
			facade.commitTransaction();
			
		} catch (Exception e) {
			facade.rollbackTransaction();
			
			String msg = "Failed to apply Instruction Process action: " 
			 + e.getMessage();
			
			Log.error(msg, e);
			
			throw new ServiceException(msg, e);
		}
	}
	
	/** Fetches information about the current state of the Routing Module Manager.
	 * 
	 */
	public void getRoutingModuleManagerState() {
		RoutingModuleManager manager = RoutingModuleManager.getManager();
		
		CCLAUtils.addQueryBooleanParamToBinder
		(m_binder, "managerIsPaused", manager.isPaused());
		
		CCLAUtils.addQueryIntParamToBinder
		(m_binder, "moduleCount", manager.getModuleCount());
		
		CCLAUtils.addQueryDateParamToBinder
		(m_binder, "activeSince", manager.getDateActive());
		
		CCLAUtils.addQueryIntParamToBinder
		(m_binder, "numCycles", (int)manager.getNumCycles());
		
		CCLAUtils.addQueryDateParamToBinder
		(m_binder, "lastCycle", manager.getLastCycle());
		
		ModuleExecutionStatus execStatus = manager.getModuleExecutionStatus();
		
		// Information on current Module Execution Status.
		if (execStatus != null) {
			CCLAUtils.addQueryParamToBinder
			(m_binder, "currentModule", execStatus.getCurrentModule().getName());
			CCLAUtils.addQueryParamToBinder
			(m_binder, "moduleExecStatus", execStatus.getExecutionStatus().toString());
			CCLAUtils.addQueryIntParamToBinder
			(m_binder, "numFetchedInstructions", execStatus.getNumFetchedInstructions());
			CCLAUtils.addQueryIntParamToBinder
			(m_binder, "numProcessedInstructions", execStatus.getNumProcessedInstructions());
			
			CCLAUtils.addQueryParamToBinder(m_binder, "elapsedModuleExecutionTime", 
			 manager.getElapsedModuleProcessingExecutionTime());
		}
		
		// Config Vars
		CCLAUtils.addQueryIntParamToBinder
		 (m_binder, RoutingModuleManager.ConfigVars.INTER_CYCLE_REST_PERIOD, 
	     RoutingModuleManager.INTER_CYCLE_REST_PERIOD);
		CCLAUtils.addQueryIntParamToBinder
		 (m_binder, RoutingModuleManager.ConfigVars.INTER_MODULE_REST_PERIOD, 
	     RoutingModuleManager.INTER_MODULE_REST_PERIOD);
		CCLAUtils.addQueryIntParamToBinder
		 (m_binder, RoutingModuleManager.ConfigVars.NUM_INSTRUCTIONS_TO_PROCESS, 
	     RoutingModuleManager.NUM_INSTRUCTIONS_TO_PROCESS);
		CCLAUtils.addQueryIntParamToBinder
		 (m_binder, RoutingModuleManager.ConfigVars.MODULE_EXECUTION_TIMEOUT_PERIOD, 
	     RoutingModuleManager.MODULE_EXECUTION_TIMEOUT_PERIOD);
	}
	
	
	/** Used to pause/unpause the Routing Module Manager thread.
	 * 
	 */
	public void setRoutingModuleManagerState() {
		RoutingModuleManager manager = RoutingModuleManager.getManager();
		
		boolean managerIsPaused = CCLAUtils.getBinderBoolValue
		 (m_binder, "managerIsPaused");
		
		manager.setPaused(managerIsPaused);
	}
	
	/** Sub-service method used for resolving a dDocName value to feed into a GET_FILE
	 *  service call.
	 *  
	 *  Requires either an INSTRUCTION_ID 
	 *  or a GUID (value is either [DOCNAME] or [DOCNAME]:[REVISION])
	 *  
	 * @throws Exception 
	 *  
	 */
	public void resolveGetFileParams() throws Exception {
		
		Integer instructionId = CCLAUtils.getBinderIntegerValue
		 (m_binder, SharedCols.INSTRUCTION);

		String guid = CCLAUtils.getBinderStringValue
		 (m_binder, "GUID");
		
		String origDocName = m_binder.getLocal(SharedCols.UCM_DOC_NAME);

		//Main params to set into binder
		String docName = null;
		Integer revisionId = null;
		
		
		//Main lookup is guid.
		if (!StringUtils.stringIsBlank(guid)) {	
			
			String[] values = guid.split(":");
			if (values.length==1) {
				docName = values[0];
			} else if (values.length>1) {
				docName = values[0];
				String revisionStr = values[1];
				try {
					revisionId = Integer.parseInt(revisionStr);
				} catch (NumberFormatException nfe) {
					throw new DataException("RevisionId is not known");
				}

			}
			
			//verify the docName
			docName = CCLAUtils.getContentDocName(docName);
			
		} else if (instructionId != null) {
			FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
			
			LWDocument commDoc = 
			 CCLAUtils.getInstructionContentDocument(instructionId, facade);
			
			docName = commDoc.getDocName();
		} else if (!StringUtils.stringIsBlank(origDocName)) {
			docName = CCLAUtils.getContentDocName(origDocName);
			
		} else {
			String msg = "Not enough parameters to resolve document URL";
			
			Log.error(msg);
			throw new ServiceException(msg);
		}
		
		m_binder.putLocal(UCMFieldNames.DocName, docName);
		
		// Resolve Revision ID
		if (revisionId==null)
			revisionId = CCLAUtils.getBinderIntegerValue(m_binder, SharedCols.UCM_REVISION_ID);
		
		if (revisionId == null) {
			m_binder.putLocal("RevisionSelectionMethod", "LatestReleased");
		} else {
			// Revision ID passed, we need to lookup corresponding dID.
			// TODO
			CCLAUtils.addQueryIntParamToBinder
			 (m_binder, UCMFieldNames.RevisionId, revisionId);
			
			m_binder.putLocal("RevisionSelectionMethod", "LatestReleased");
		}
		
		// Static parameters
		// ------------------
		
		// noSaveAs - prevents download prompt appearing to user.
		m_binder.putLocal("noSaveAs", "1");
		
		// Rendition - always web
		m_binder.putLocal("Rendition", "Web");
	}
	
	/**
	 * Service Method to update a given modules details
	 * Required Binder Params: 
	 * 	- MODULE_ID
	 * 
	 * @throws ServiceException 
	 */
	public void updateModuleDetails() throws ServiceException {
		
		FWFacade facade;
		Integer routingModuleId = null;
		
		try {
			routingModuleId = CCLAUtils.getBinderIntegerValue(m_binder, "MODULE_ID");
			if (routingModuleId==null)
				throw new ServiceException("MODULE_ID is needed");
			
			facade = CCLAUtils.getFacade(m_workspace, true);
			RoutingModule rm = RoutingModule.get(routingModuleId,facade);
			rm.setAttributes(m_binder);
			rm.persist(facade, m_userData.m_name);
			
		} catch (DataException e) {
			Log.error("Unable to update the routing module: "+
					routingModuleId+" "+e.getMessage(),e);
			throw new ServiceException("Unable to update the routing module " +
					routingModuleId+" "+e.getMessage(),e);
		}
		
	}
	
	
	/**
	 * 
	 * @throws ServiceException
	 */
	public void retryAllWithState() throws ServiceException{
		try {
			Integer state = CCLAUtils.getBinderIntegerValue(m_binder, "STATE");
			//TODO: apply retry processes for all values with given state
			
		} catch (DataException e) {
			Log.error("Unable to retry all items with given state: "
					+e.getMessage(),e);
			throw new ServiceException("Unable to retry all items with given state: "
					+e.getMessage(),e);
			
		}
	}
	
	
	/**
	 * Service to generate know events to be fired off to each routing module.
	 * @throws ServiceException
	 */
	public void generateEventAndFire() throws ServiceException 
	{
		String eventName = m_binder.getLocal("eventName");
		
		if (StringUtils.stringIsBlank(eventName)) {
			throw new ServiceException("eventName is empty");
		}
		
		try {
			IInstructionEvent event = null;
			String userId = this.m_userData.m_name;
			InstructionEventGenerator generator = InstructionEventGenerator.getInstance();
			
			FWFacade facade = CCLAUtils.getFacade(m_workspace);
			Integer instrId = CCLAUtils.getBinderIntegerValue(m_binder, SharedCols.INSTRUCTION);
			eventName  = eventName.intern();
			
			if (eventName==InstructionEvent.CLASS_NAME) {
				generator.triggerInstructionEvent(instrId, userId, facade);
			} else if (eventName==TransactionReferenceEvent.CLASS_NAME) {				
				String transRef = m_binder.getLocal(Globals.UCMFieldNames.TransactionRef);
				generator.triggerTransactionRefEvent(instrId, userId, facade, transRef);
			} else {
				throw new ServiceException("eventName not known :"+eventName);			
			}
		} catch (DataException de) {
			Log.error("Cannot fire event:"+de.getMessage(), de);
			throw new ServiceException("Cannot fire event:"+de.getMessage(), de);
		}
	}
	
	/**
	 * FIXME query qCommProc_GetInstructionDocsForClass refers to old INSTRUCTION_DOC_ID
	 * field, needs to be refactored to remove this and joins to Revisions/DocMeta.
	 * 
	 * This method will get all the documents with an specific class xDocumentClass value
	 * and perform the lwd.update() method to push them back through the system. This is needed
	 * as the metadata mapping for invoices is now ready, but older invoices may not have had 
	 * been converted correctly. 
	 * 
	 * Required Binder Params
	 * - DOC_CLASS
	 * - TO_DATE
	 * - FROM_DATE
	 * 
	 * Optional Binder Params 
	 * - getCSV - if this is present a CSV file will be returned with the failed docs
	 * 
	 * Return Binder Params
	 * - TOTAL_NUM_DOCS - total number of docs returned from the query
	 * - NUM_DOCS_UPDATED - total number of docs successfully updated		
	 * - rsFailedDocs - resultset of docs failed, with error msg and metadata
	 * 
	 */
	public void resubmitInstructionDocs() throws ServiceException{
				
		try {
			FWFacade f = CCLAUtils.getFacade(m_workspace);
			DataResultSet invoiceDocs = 
				f.createResultSet("qCommProc_GetInstructionDocsForClass", m_binder);
			Log.info("found: " +invoiceDocs.getNumRows()+" rows");	
			if (invoiceDocs.first()) { 
				
				String[] colsNeeded = {"URL", "ERROR_MSG", "dDocName","dId","xIndexer","dInDate",
						"xAmount","xUnits","xFund", "xClientServicesRef","xDestinationFund", 
						"xDestinationFund","xProcessDate"};
				
				DataResultSet rsFailedDocs = new DataResultSet(colsNeeded);
				int numDocsUpdated = 0; 
				
				do {
					String dDocName = 
						DataResultSetUtils.getResultSetStringValue(invoiceDocs, "dDocName");
					
					LWDocument lwd = null;
					try {
						lwd = new LWDocument(dDocName, true);
						Log.info("Updating document: " + dDocName);
						lwd.update();
						numDocsUpdated++;
					}  catch (Exception e) {
						//If unable to update document, add it to the failedDocs Vector
						Log.info("unable to update document: "+ dDocName +" ["+e.getMessage()+"]");							
						if (lwd!=null){
							try {
								Vector<String> sdfg = new Vector<String>();
								v.add("http://ccla-ap14/ucm/idcplg?IdcService=DOC_APPROVAL&dDocName="+lwd.getDocName());
								v.add(e.getMessage());
								for (int i = 2; i < colsNeeded.length; i++) {
									v.add(lwd.getAttribute(colsNeeded[i]));	
								} 
								rsFailedDocs.addRow(v);
							} catch (Exception e1) {
								throw new ServiceException(
										"unable to get lwd information: " + e.getMessage());
							}
						} else {
							Log.info("LWD object is null");
						}
					
					}						
				} while (invoiceDocs.next());
				

				//Add report data to the binder
				m_binder.putLocal("TOTAL_NUM_DOCS", Integer.toString(invoiceDocs.getNumRows()));
				m_binder.addResultSet("rsFailedDocs", rsFailedDocs);
				m_binder.putLocal("NUM_DOCS_UPDATED", Integer.toString(numDocsUpdated));		
				
				String isCSV = m_binder.getLocal("getCSV");
				
				//return file docs CSV file
				if (!rsFailedDocs.isEmpty() && !StringUtils.stringIsBlank(isCSV)){
					
					String tempDir = DataBinder.getTemporaryDirectory();
					String fileName = "";
					try {
						DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
						Date fromDate = (Date)formatter.parse(m_binder.getLocal("FROM_DATE"));  
						Date toDate = (Date)formatter.parse(m_binder.getLocal("TO_DATE"));  
						SimpleDateFormat fmt = new SimpleDateFormat("ddMMyyyy-HHmmss");
						
	                    fileName = "failedDocs_" 
	                    	+ m_binder.getLocal("DOCUMENT_CLASS")
	                    	+ "_" + fmt.format(fromDate)
	                    	+ "_" + fmt.format(toDate)
	                    	+ ".csv";
	                    
	                    File csvFile = new File(tempDir + "/" + fileName); 
	                    StringBuffer sb = CCLAUtils.convertToCSV(rsFailedDocs, null);
	                    
	                    FileWriter fw;
						fw = new FileWriter(csvFile);
						fw.write(sb.toString());
		                fw.close();
					} catch (IOException e) {
						e.printStackTrace();
						throw new ServiceException("unable to create csv file: " + e.getMessage());
					} catch (ParseException e) {
						e.printStackTrace();
						throw new ServiceException("unable to parse dates: " + e.getMessage());
					}
                   
                          
                    m_binder.addTempFile(tempDir + "/" + fileName);

                    this.setSendFile(true);
                    this.setFile(tempDir + "/" + fileName);
                    this.setDownloadName(fileName);

                    this.setDownloadFormat("text/csv");
                    this.m_binder.putLocal("dExtension", "csv");

				}
				
			}
		} catch (DataException e) {
			String err = "unable to execute query qCommProc_GetInvoiceDocs: "+e.getMessage();
			Log.info(err);
			throw new ServiceException(err, e);
		}
		
	}
	
	/** Used to bulk-apply a given process action to instructions suspended to a given
	 *  Routing Module.
	 *  
	 * @throws DataException 
	 *  
	 */
	public void applyBulkProcessAction() throws DataException {
		
		Integer instructionActionId = CCLAUtils.getBinderIntegerValue
		 (m_binder, InstructionAction.Cols.ID);
		
		Integer instructionProcessId = CCLAUtils.getBinderIntegerValue
		 (m_binder, InstructionProcess.Cols.ID);
		
		Integer routingModuleId = CCLAUtils.getBinderIntegerValue
		 (m_binder, RoutingModule.Cols.ID);
		
		RoutingModule module = RoutingModule.getCache().getCachedInstance
		 (routingModuleId);
		InstructionProcess process = InstructionProcess.getCache().getCachedInstance
		 (instructionProcessId);
		InstructionAction action = InstructionAction.getCache().getCachedInstance
		 (instructionActionId);
		
		Log.debug("Fetching active Instruction Processes for all suspended " +
		 "instructions locked to Module '" + module.getName() + "' with Process '" +
		 process.getName() + "'");
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		Vector<InstructionProcessApplied> instrProcessApplied =
		 InstructionProcessApplied.getByProcessIdAndModuleId
		 (instructionProcessId, routingModuleId, facade);
		
		Log.debug("Found " + instrProcessApplied.size() + " matching processes. " +
		 "Applying action '" + action.getLabel() + "'");
		
		int successCount = 0;
		
		for (InstructionProcessApplied proc : instrProcessApplied) {
			try {
				facade.beginTransaction();
				
				InstructionProcessUtils.applyAction
				 (proc, action, facade, m_userData.m_name);
				
				facade.commitTransaction();
				
				successCount++;
				
			} catch (Exception e) {
				facade.rollbackTransaction();
				
				String msg = "Failed to apply Instruction Process action: " 
				 + e.getMessage();
				
				Log.error(msg, e);
			}
		}
		
		Log.debug("Bulk process action execution complete. " 
		 + successCount + "/" + instrProcessApplied.size() + " successful");
	}
	
	/** Test service method to see what happens when spawned threads use FWFacade 
	 *  connections which aren't terminated properly.
	 */
	public void testThreadedDBConnections() {
		
		Thread t1 = new Thread() {
			public void run() {
				
				// Instantiate new SystemDatabase facade
				FWFacade facade = null;
				try {
					facade = CCLAUtils.getFacade(false);
				} catch (DataException e) {
					// TODO Auto-generated catch block
					Log.error("Failed to create test facade: " + e.getMessage(), e);
				}

				
				while (true) {
					try {
						Log.debug("Sleeping thread " + this.getName());
						Thread.sleep(5000);
						
						facade.createResultSetSQL("SELECT 1 FROM DUAL");
						Log.debug("Thread " + this.getName() + " facade is doing fine");
						
						// Simulate a DB connection reset somehow??

					} catch (Exception e) {
						Log.error("Thread error: " + e.getMessage(), e);
					}
				}
			}
		};
		
		t1.setName("TestFacadeThread1");
		t1.start();
		
		Thread t2 = new Thread() {
			public void run() {
					
				while (true) {

					// Instantiate new SystemDatabase facade
					FWFacade facade = null;
					
					try {
						Log.debug("Sleeping thread " + this.getName());
						Thread.sleep(5000);
						
						
						try {
							facade = CCLAUtils.getFacade(false);
						} catch (DataException e) {
							// TODO Auto-generated catch block
							Log.error("Failed to create test facade: " + e.getMessage(), e);
						}
						
						facade.createResultSetSQL("SELECT 1 FROM DUAL");
						Log.debug("Thread " + this.getName() + " facade is doing fine");
						
						// Simulate a DB connection reset somehow??
						
					} catch (Exception e) {
						Log.error("Thread error: " + e.getMessage(), e);
					} finally {
						if (facade != null)
							facade.releaseConnection();
					}
				}
			}
		};
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		t2.setName("TestFacadeThread2");
		t2.start();
	}
}
