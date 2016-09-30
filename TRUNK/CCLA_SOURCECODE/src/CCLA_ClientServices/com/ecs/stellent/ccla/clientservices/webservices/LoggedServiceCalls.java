package com.ecs.stellent.ccla.clientservices.webservices;

import intradoc.common.ServiceException;
import intradoc.data.DataResultSet;
import intradoc.server.Service;

import java.util.Vector;

import com.ecs.stellent.auditlog.AuditUtils;
import com.ecs.stellent.ccla.clientservices.ClientServicesService;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.ClientProcess;
import com.ecs.ucm.ccla.data.UCMForm;
import com.ecs.ucm.ccla.data.PersonProcess;
import com.ecs.ucm.ccla.data.form.FormUtils;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

public class LoggedServiceCalls extends Service{

	/**
	 * Executes query qClientServices_UpdateFundTransferTo and logs calls to it
	 * 
	 * Example test url:
	 * http://ccla-uat-ap14/ucm/idcplg?IdcService=CCLA_CS_UPDATE_FUND_TRANSFER_TO
	 * &transferId=387&toAccount=0003TA&toFund=AA&jobId=501&auroraStatus=FROZ
	 */
	public void updateFundTransferTo() throws ServiceException{
		String msg = "";
		
		String transferId 	= m_binder.getLocal("transferId");
		
		try{
			Log.debug("CS web service called (CCLA_CS_UDPATE_FUND_TRANSFER_TO) >>");
			Log.debug("updateFundTransferTo() method called");
			
			Log.debug("toAccount: '" 	+ m_binder.getLocal("toAccount") + "'");
			Log.debug("toFund: '" 		+ m_binder.getLocal("toFund") + "'");
			Log.debug("jobId: '" 		+ m_binder.getLocal("jobId") + "'");
			Log.debug("auroraStatus: '" + m_binder.getLocal("auroraStatus") + "'");
			Log.debug("transferId: '" 	+ transferId + "'");
			
			FWFacade facade = CCLAUtils.getFacade(m_workspace,true);
			facade.execute("qClientServices_UpdateFundTransferTo", m_binder);		
			msg = "Updated Fund transfer (" + transferId + ") via web service call"; 
		}catch(Exception e){
			msg = "Error executing web service call: " + e.getMessage();
			Log.error(msg, e);
			throw new ServiceException(msg);
			
		}finally{
			//Audit the web service call
			//Pass in toAccount and toFund params for extra auditing 
			Vector params = new Vector();
			params.add(m_binder.getLocal("toAccount"));
			params.add(m_binder.getLocal("toFund"));
			
			try{
				AuditUtils.addAuditEntry("CLIENT_SERVICES_WEBSERVICE", "UPD-FND-TR-TO", 
					 transferId,
					 "Fund transfer to", 
					 m_userData.m_name,
					 msg,
					 params);
			}catch (ServiceException se){
				Log.error("Unable to audit updateFundTransferTo call: " 
						+ se.getMessage()
						+"Audit entry: UPD-FND-TR-TO - " + msg
						, se);
			}
			
			Log.debug("Finished CCLA_CS_UDPATE_FUND_TRANSFER_TO <<");
		}
	}
	
	/**
	 * Executes query qClientServices_UpdateFundTransferBatchStatus and logs calls to it
	 * 
	 * Example test url:
	 * http://ccla-uat-ap14/ucm/idcplg?IdcService=CCLA_CS_UPDATE_FUND_TRANSFER_BATCH_STATUS
	 * &transferId=387&batchedInAurora=1
	 */
	public void updateFundTransferBatchStatus() throws ServiceException{
		String msg = "";
		
		String transferId 	= m_binder.getLocal("transferId");
		String batchStatus 	= m_binder.getLocal("batchedInAurora");
		
		try{
			Log.debug("CS web service called (CCLA_CS_UDPATE_FUND_TRANSFER_BATCH_STATUS) >>");
			Log.debug("updateFundTransferBatchStatus() method called");
			
			Log.debug("batchedInAurora: '" 	+ batchStatus + "'");
			Log.debug("transferId: '" 		+ transferId + "'");
			
			FWFacade facade = CCLAUtils.getFacade(m_workspace,true);
			facade.execute("qClientServices_UpdateFundTransferBatchStatus", m_binder);		
			msg = "Updated fund transfer status via web service call"; 
		}catch(Exception e){
			msg = "Error executing web service call: " + e.getMessage();
			Log.error(msg, e);
			throw new ServiceException(msg);
			
		}finally{
			//Audit the web service call
			//Pass in toAccount and toFund params for extra auditing 
			Vector params = new Vector();
			params.add(batchStatus);
			
			try{
				AuditUtils.addAuditEntry("CLIENT_SERVICES_WEBSERVICE", "UPD-FND-TR-B-ST", 
					 transferId,
					 "Fund transfer batch status", 
					 m_userData.m_name,
					 msg,
					 params);
			}catch (ServiceException se){
				Log.error("Unable to audit updateFundTransferBatchStatus call: " 
						+ se.getMessage()
						+"Audit entry: UPD-FND-TR-B-ST - " + msg
						, se);
			}
		
			
			Log.debug("Finished CCLA_CS_UDPATE_FUND_TRANSFER_BATCH_STATUS <<");
		}
	}
	
	/**
	 * Executes query qClientServices_SetFundTransfersConfirmed and logs calls to it
	 * 
	 * Example test url:
	 * http://ccla-uat-ap14/ucm/idcplg?IdcService=CCLA_CS_MARK_FUND_TRANSFERS_CLIENT_CONFIRMED
	 * &formId=501
	 * 
	 * Should update CS_FUND_TRANSFERS rows with PROCESS_ID=132 and set CLIENT_CONFIRMED=1
	 */
	public void updateFundTransferClientConfirmed() throws ServiceException{
		String msg = "";
		
		String formId 		= m_binder.getLocal("formId");
		String pClientId	= m_binder.getLocal("clientId");
		 
		try{
			Log.debug("CS web service called (CCLA_CS_MARK_FUND_TRANSFERS_CLIENT_CONFIRMED) >>");
			Log.debug("updateFundTransferClientConfirmed() method called");
			
			Log.debug("formId: '" 	+ formId + "'");
			Log.debug("clientId: '" + pClientId + "'");
			
			FWFacade facade = CCLAUtils.getFacade(m_workspace,true);
			facade.execute("qClientServices_SetFundTransfersConfirmedByClientId", m_binder);		
			msg = "Marked form with formId " + formId + " and clientId " + pClientId + " as confirmed"; 
		}catch(Exception e){
			msg = "Error executing web service call: " + e.getMessage();
			Log.error(msg, e);
			throw new ServiceException(msg);
			
		}finally{
			//Audit the web service call
			//Pass in toAccount and toFund params for extra auditing 
			Vector params = new Vector();
			params.add(formId);
			
			try{
				AuditUtils.addAuditEntry("CLIENT_SERVICES_WEBSERVICE", "UPD-FND-TR-CONF", 
					formId,
					 "Fund transfer confirmed", 
					 m_userData.m_name,
					 msg,
					 params);
			}catch (ServiceException se){
				Log.error("Unable to audit updateFundTransferClientConfirmed call: " 
						+ se.getMessage()
						+"Audit entry: UPD-FND-TR-CONF - " + msg
						, se);
			}
			
			String clientId = "";
			
			try {
				UCMForm form = UCMForm.get(Integer.parseInt(formId), CCLAUtils.getFacade(m_workspace,true));
				
				if(form.getProcessId() != null) {
					FWFacade facade = CCLAUtils.getFacade(m_workspace);
					
					PersonProcess process = PersonProcess.get(form.getProcessId(), facade);
					
					process.addActivity(m_userData.m_name, process.getPersonId(), 
					 "Form Back and Confirmed", "CLIENTCONF received and signature verified", 
					 null, false, facade);
				}
			}catch(Exception e){
				Log.debug("Unable to write CLIENT_CONFIRM=1 client services exchange row " +
						"for client " + clientId);
			}
			
			Log.debug("Finished CCLA_CS_MARK_FUND_TRANSFERS_CLIENT_CONFIRMED <<");
		}
	}
	
	/** Executes an update query on the CS_FUND_TRANSFERS table for a given
	 *  comma-separated list of transfer IDs.
	 *  
	 *  The query will set the AURORA_BATCH_ID and HAS_BATCH_ERRORS values for
	 *  all associated rows.
	 */
	public void updateFundTransfersAuroraBatchId() throws ServiceException {
		
		String batchId = m_binder.getLocal("batchId");
		String hasBatchErrors = m_binder.getLocal("hasBatchErrors");
		String transferIds = m_binder.getLocal("transferIds");
		
		String[] idList = transferIds.split(",");
		
		Log.debug("Updating Aurora Batch IDs and Error Flags for " 
		 + idList.length + " transfers [" + transferIds + "]");
		Log.debug("Aurora Batch ID: " + batchId + ", Error Flag: " + hasBatchErrors);
		 
		if (StringUtils.stringIsBlank(batchId))
			throw new ServiceException("Unable to set Aurora Batch IDs/error flag: " +
			 "no Aurora Batch ID was passed.");
		
		// Use false flag by default if none specified
		if (StringUtils.stringIsBlank(hasBatchErrors))
			hasBatchErrors = "0";
		
		if (StringUtils.stringIsBlank(transferIds)) {
			Log.warn("Unable to set Aurora Batch IDs/error flag: " +
			 "no transfer IDs were passed.");
			return;
		}
		
		// Build update statement
		String query = "UPDATE CS_FUND_TRANSFERS " +
		"SET AURORA_BATCH_ID='" + batchId + "', HAS_BATCH_ERRORS='" 
		+ hasBatchErrors + "' " +
		"WHERE TRANSFER_ID IN (" + transferIds + ")"; 
		
		try {
			FWFacade facade = CCLAUtils.getFacade(m_workspace,true);
			facade.executeSQL(query);
			
			Vector params = new Vector();
			params.add(batchId);
			params.add(hasBatchErrors);
			
			// Audit each row that was updated
			for (int i=0; i<idList.length; i++) {
				AuditUtils.addAuditEntry("CLIENT_SERVICES_WEBSERVICE", 
				 "UPD-FND-TR-AURORA-BATCH-ID", 
				 idList[i],
				 "Transfer ID: " + idList[i],
				 m_userData.m_name,
				 "Batch ID and error flag updated for transfer ID: " 
				 + idList[i],
				 params);
			}
			
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	/** Updates the SUBMITTED_FOR_AUTH flag for all rows in the 
	 *  CS_FUND_TRANSFERS table where the AURORA_BATCH_ID matches the
	 *  passed batchId in the binder.
	 *  
	 *  If a true flag is passed, the AUTH_STATUS values are also updated
	 *  to 3.
	 *  
	 * @throws ServiceException
	 */
	public void updateSubmittedForAuthByBatchId() throws ServiceException {
	
		String batchId = m_binder.getLocal("batchId");
		String submittedForAuth = m_binder.getLocal("submittedForAuth");
		
		Log.debug("Updating Submitted For Auth flag for batch ID: " + batchId);
		
		if (StringUtils.stringIsBlank(submittedForAuth))
			submittedForAuth = "0";
		
		boolean updateStatus = false;
		
		// Automatically update the auth status value if a true flag
		// is passed
		if (submittedForAuth.equals("1")) {
			updateStatus = true;
			m_binder.putLocal("authStatus", "3");
		}
		
		try {
			FWFacade facade = CCLAUtils.getFacade(m_workspace,true);
			
			if (updateStatus) {
				facade.execute("qClientServices_UpdateSubmittedForAuthAndAuthStatusByBatchId", 
				 m_binder);
			} else {
				facade.execute("qClientServices_UpdateSubmittedForAuthByBatchId", 
				 m_binder);
			}
			
			Vector params = new Vector();
			params.add(submittedForAuth);
			
			String msg = "Submitted For Auth flag updated to " 
			 + submittedForAuth + " for Aurora Batch ID: " + batchId;
			
			if (updateStatus) {
				params.add("3");
				msg += " (Auth Status auto-updated to 3)";
			} else
				params.add("");

			AuditUtils.addAuditEntry("CLIENT_SERVICES_WEBSERVICE", 
			 "UPD-FND-TRS-SUB-AUTH-BY-BATCH-ID", 
			 batchId,
			 "Aurora Batch ID: " + batchId,
			 m_userData.m_name,
			 msg,
			 params);
			
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	/** Updates the AUTH_STATUS value for all rows in CS_FUND_TRANSFERS where
	 *  the AURORA_BATCH_ID value matches the passed batchId.
	 *  
	 */
	public void updateAuthStatusByBatchId() throws ServiceException {
		
		String batchId = m_binder.getLocal("batchId");
		String authStatus = m_binder.getLocal("authStatus");
		
		Log.debug("Updating Auth Status flag to '" + authStatus + 
		 "' for batch ID: " + batchId);
		
		if (StringUtils.stringIsBlank(batchId) || StringUtils.stringIsBlank(authStatus))
			throw new ServiceException("Unable to update Auth Status flags, " +
			 "no batch ID/auth status passed");
		
		try {
			FWFacade facade = CCLAUtils.getFacade(m_workspace,true);
			facade.execute("qClientServices_UpdateAuthStatusByBatchId", m_binder);
			
			Vector params = new Vector();
			params.add(authStatus);
			
			String msg = "Auth Status updated to " + authStatus + 
			 " for Aurora Batch ID: " + batchId;
			
			AuditUtils.addAuditEntry("CLIENT_SERVICES_WEBSERVICE", 
			 "UPD-FND-TRS-AUTH-STATUS-BY-BATCH-ID", 
			 batchId,
			 "Aurora Batch ID: " + batchId,
			 m_userData.m_name,
			 msg,
			 params);
			
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	/** Suspends a transfer in the CS_FUND_TRANSFERS table 
	 *  by the given transfer ID.
	 * 
	 *  This involves setting the IS_SUSPENDED flag to true and
	 *  resetting various Aurora batching flags:
	 *  -AURORA_BATCH_ID
	 *  -HAS_BATCH_ERRORS
	 *  -IS_SUBMITTED_FOR_AUTH
	 *  -AUTH_STATUS
	 *  
	 */
	public void suspendTransfer() throws ServiceException {
		
		String transferId = m_binder.getLocal("transferId");
		
		Log.debug("Suspending transfer ID: " + transferId);
		
		if (StringUtils.stringIsBlank(transferId))
			throw new ServiceException("Unable to suspend transfer, " +
			 "no transfer ID passed");
		
		// Required query params
		m_binder.putLocal("isSuspended", "1");
		m_binder.putLocal("emptyString", "");
		m_binder.putLocal("resetFlag", "0");
		
		try {
			FWFacade facade = CCLAUtils.getFacade(m_workspace,true);
			facade.execute("qClientServices_SuspendTransfer", m_binder);
			
			Vector params = new Vector();
			
			String msg = "Suspended transfer with ID: " + transferId + 
			 " (Aurora batching flags reset)";
			
			AuditUtils.addAuditEntry("CLIENT_SERVICES_WEBSERVICE", 
			 "SUSPEND-TRANSFER", 
			 transferId,
			 "Transfer ID: " + transferId,
			 m_userData.m_name,
			 msg,
			 params);
			
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Executes a query that updates the BATCHED_IN_AURORA flag for the given csv
	 * list of transfer IDs. Sets values to 1 or 0.
	 * 
	 * Intended for use after a set of account transfers have been batched in Aurora 
	 * for transfer.
	 * 
	 * Example test url:
	 * http://ccla-uat-ap14/ucm/idcplg?IdcService=CCLA_CS_UPDATE_FUND_TRANSFER_LIST_BATCH_STATUS
	 * &transferIds=388,390,391&batchedInAurora=1
	 * 
	 * The above should update CS_FUND_TRANSFERS rows with relevant transfer ids with
	 * a BATCHED_IN_AURORA status of 1.
	 */
	public void updateFundTransferListBatchStatus() throws ServiceException{
		String msg = "";
		
		String csvTransferIds 	= m_binder.getLocal("transferIds");
		String batchedInAurora 	= m_binder.getLocal("batchedInAurora");
		
		if(StringUtils.stringIsBlank(csvTransferIds)){
			String exMsg = "Unable to perform query, parameter 'transferIds' is missing.";
			Log.error(exMsg);
			throw new ServiceException(exMsg);
		}else if(StringUtils.stringIsBlank(batchedInAurora)){
			String exMsg = "Unable to perform query, parameter 'batchStatus' is missing.";
			Log.error(exMsg);
			throw new ServiceException(exMsg);
		}			
		
		try{
			Log.debug("CS web service called (CCLA_CS_UPDATE_FUND_TRANSFER_LIST_BATCH_STATUS) >>");
			Log.debug("updateFundTransferListBatchStatus() method called");
			
			Log.debug("transferIds: '" 		+ csvTransferIds + "'");
			Log.debug("batchedInAurora: '" 	+ batchedInAurora + "'");
			
			String sql = "UPDATE CS_FUND_TRANSFERS";
			sql += " SET BATCHED_IN_AURORA='" + batchedInAurora + "'";
			sql += " WHERE TRANSFER_ID IN (" + csvTransferIds + ")";
			
			Log.debug("Executing sql: " + sql);
			
			m_workspace.executeSQL(sql);

			msg = "Updated transfer ids"; 
		}catch(Exception e){
			msg = "Error executing web service call: " + e.getMessage();
			Log.error(msg, e);
			throw new ServiceException(msg);
			
		}finally{
			//Audit the web service call
			//Pass in toAccount and toFund params for extra auditing 
			Vector params = new Vector();
			params.add(csvTransferIds);
			params.add(batchedInAurora);
			
			try{
				AuditUtils.addAuditEntry("CLIENT_SERVICES_WEBSERVICE", "UPD-FND-TR-LST", 
					csvTransferIds,
					 "Fund transfer list update", 
					 m_userData.m_name,
					 msg,
					 params);
			}catch (ServiceException se){
				Log.error("Unable to audit updateFundTransferListBatchStatus call: " 
						+ se.getMessage()
						+ "Audit entry: UPD-FND-TR-CONF - " + msg
						, se);
			}
			
			Log.debug("Finished CCLA_CS_UPDATE_FUND_TRANSFER_LIST_BATCH_STATUS <<");
		}
	}
	
}
 