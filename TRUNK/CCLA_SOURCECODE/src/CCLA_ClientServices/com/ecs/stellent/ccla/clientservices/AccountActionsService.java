package com.ecs.stellent.ccla.clientservices;

import java.util.Vector;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.AccountAction;
import com.ecs.ucm.ccla.data.AccountActionAudit;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

import com.ecs.utils.Log;
import intradoc.common.ServiceException;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.server.Service;

/** Methods and utilities for adding and updating account-based
 *  actions.
 *  
 *  These are stored in the CCLA_ACCOUNT_ACTIONS table. Generally
 *  new rows will be inserted by a UCM application such as Iris.
 *  
 *  Rows are consumed and updated by the SPP Workflow application
 *  after the account action has been applied or delegated to
 *  Aurora for processing.
 *  
 * @author Tom Marchant
 *
 */
public class AccountActionsService extends Service {
	
	/** Fetches a subset of rows from the CCLA_ACCOUNT_ACTIONS table.
	 *  
	 *  All results are filtered to only return non-suspended and
	 *  incomplete actions.
	 *  
	 *  The user-supplied filters are:
	 *  -action
	 *  -status
	 *  -fund 	(relates to either the source fund or dest fund field, 
	 *  		depending on the passed action)
	 *  -numRows
	 * @throws DataException 
	 */
	public void getPendingAccountActionsByStatus() throws DataException {
		
		String action = m_binder.getLocal("action");
		String fund	  = m_binder.getLocal("fund");
		
		if (action.equals("CLOSE")) {
			m_binder.putLocal("sourceFund", fund);
			m_binder.putLocal("destFund", "%");
		} else {
			m_binder.putLocal("destFund", fund);
			m_binder.putLocal("sourceFund", "%");
		}
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace);
		
		DataResultSet rsActions = 
		 facade.createResultSet("qClientServices_GetAccountActionsByStatus", 
		 m_binder);
		
		m_binder.addResultSet("rsAccountActions", rsActions);
	}
	
	/** Service method invoked by UpdateAccountAction web service,
	 *  used by SPP.
	 *  
	 *  Updates a single entry in the CCLA_ACCOUNT_ACTIONS table.
	 *  
	 * @throws ServiceException 
	 * @throws DataException 
	 *  
	 */
	public void updateAccountAction() throws ServiceException, DataException {
		
		String actionId		= m_binder.getLocal("accountActionId");
		String userId 		= m_binder.getLocal("userId");
		String status		= m_binder.getLocal("status");
		boolean completed	= 
		 CCLAUtils.getBinderBoolValue(m_binder, "completed");
		
		if (StringUtils.stringIsBlank(userId) ||
			StringUtils.stringIsBlank(status) ||
			StringUtils.stringIsBlank(actionId))
			throw new ServiceException("Unable to update account action: " +
			 "required parameter userId, status or actionId missing");
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace);
		
		AccountAction action = AccountAction.get(
		 CCLAUtils.getBinderIntegerValue(m_binder, "actionId"), facade);
		
		if (action == null) {
			String msg = "Unable to update account action: no action found with ID: " 
			 + actionId;
			
			Log.error(msg);
			throw new ServiceException(msg);
		}
		
		try {
			facade.beginTransaction();
			
			action.setUserId(userId);
			action.setCompleted(completed);
			action.setStatus(status);
			
			action.persist(facade);
			
			action.addAudit(AccountActionAudit.ActionType.UPDATE, 
			 "Updated: status=" + status + ", completed=" + completed, 
			 userId, facade);
			
			Log.debug("Updated Account Action: " + action + ", ID: " + 
			 action.getActionId());
			
			facade.commitTransaction();
			
		} catch (Exception e) {
			facade.rollbackTransaction();
			
			String msg = "Failed to update account action: " + e.toString();
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
			
		// TODO: replace with accountAction.persist()
		//facade.execute("qClientServices_UpdateAccountAction", m_binder);
		
		/*
		Vector<String> params = new Vector<String>();
		
		params.add(status);
		params.add(completed);
		
		AuditUtils.addAuditEntry("ACC-ACTIONS", "UPDATE-ACTION", 
		 actionId, "Account action " + actionId, userId, 
		 "Action status updated to '" + status + "'", params);
		 */
	}
	
	/** 
	 * Adds a new pending account action, based on passed fields in the
	 * binder. This also handles adding new entries to the associated
	 * banking details table, if applicable.
	 * 
	 * @throws ServiceException
	 * @throws DataException
	 */
	public void addAccountAction() throws ServiceException, DataException {
		
		String userId = m_userData.m_name;
		m_binder.putLocal("userId", userId);
		
		String action = m_binder.getLocal("action");
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace);
		
		// When performing an operation on an existing account, the user
		// will select the account from a drop-down list. The value will
		// be in the passed binder as sourceAccNumExt
		String accNumExt = m_binder.getLocal("sourceAccNumExt");
		
		if (!StringUtils.stringIsBlank(accNumExt)) {
			Log.debug("Found a source ext. account no. in binder: " +
			 accNumExt + ". Looking up associated account info.");
			
			// Lookup this account.
			Account account = Account.get(accNumExt, facade);
			
			if (account == null)
				throw new ServiceException("Couldn't find existing account: " 
				 + accNumExt);
			
			m_binder.putLocal("sourceAccAccountNum", Integer.toString(account.getAccountNumber()));
			m_binder.putLocal("sourceAccFund", account.getFundCode());
		}
		
		String destAccCompany		= m_binder.getLocal("destAccCompany");
		String destAccClientId		= m_binder.getLocal("destAccClientId");
		String destAccAccountNum	= m_binder.getLocal("destAccAccountNum");
		String destAccFund			= m_binder.getLocal("destAccFund");
		
		if (!StringUtils.stringIsBlank(destAccCompany) &&
			!StringUtils.stringIsBlank(destAccClientId) &&
			!StringUtils.stringIsBlank(destAccAccountNum) &&
			!StringUtils.stringIsBlank(destAccFund)) {
			
			// All destination fields are present - generate an external 
			// destination account number
			
			String clientId = CCLAUtils.padClientNumber(destAccClientId);
			String accNum	= null;
			
			// Pad CBF accounts with 3 digits, 4 for other companies
			if (destAccCompany.equals("CBF")) {
				accNum = CCLAUtils.padString(destAccAccountNum, '0', 3);
			} else {
				accNum = CCLAUtils.padString(destAccAccountNum, '0', 4);
			}
			
			String destAccNumExt = clientId + accNum + destAccFund;
			m_binder.putLocal("destAccNumExt", destAccNumExt);
			
			Log.debug("Generated external destination account number: " + destAccNumExt);
		}
		
		// Default initial account status
		m_binder.putLocal("status", "Pending");
		
		try {
			facade.beginTransaction();
			
			AccountAction accountAction = AccountAction.add(m_binder, facade);
			accountAction.addAudit(AccountActionAudit.ActionType.ADD, 
			 "Added", m_userData.m_name, facade);
			
			Log.debug("Added new Account Action: " + action + ", ID: " + 
			 accountAction.getActionId());
			
			facade.commitTransaction();
			
		} catch (Exception e) {
			facade.rollbackTransaction();
			
			String msg = "Failed to add new account action: " + e.toString();
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}
	
	/** Fetches a bounded set of unbatched, incomplete Account Actions.
	 *  
	 *  Three optional filter parameters can be supplied:
	 *  -action
	 *  -status
	 *  -fund
	 *  
	 *  The rowCount parameter determines the number of rows returned and
	 *  is required.
	 * 
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void getUnbatchedAccountActions() 
	 throws DataException, ServiceException {
		
		String action = m_binder.getLocal("action");
		String status = m_binder.getLocal("status");
		String fund   = m_binder.getLocal("fund");

		// Insert wildcard values for any missing parameters.
		if (StringUtils.stringIsBlank(action))
			m_binder.putLocal("action", "%");
		
		if (StringUtils.stringIsBlank(status))
			m_binder.putLocal("status", "%");
		
		if (StringUtils.stringIsBlank(fund))
			m_binder.putLocal("fund", "%");
		
		String rowCountStr = m_binder.getLocal("rowCount");
		
		if (StringUtils.stringIsBlank(rowCountStr)) {
			throw new ServiceException("Unable to fetch unbatched " +
			 "account actions: rowCount missing"); 
		}
		
		Log.debug("Fetching unbatched account actions: action=" + 
		 action + ", status=" + status + ",fund=" + fund);
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace);
		
		DataResultSet rsAccountActions = facade.createResultSet
		 ("qClientServices_GetUnbatchedAccountActions", m_binder);
		
		m_binder.addResultSet("rsAccountActions", rsAccountActions);
	}
	
	/** 
	 *  Updates the Aurora Batch ID values for a given list of Account Action
	 *  IDs. It can also update the batch error flag for the given set of
	 *  actions.
	 *  
	 *  This method is exposed via web service and called by SPP when the
	 *  account actions are batched in Aurora. If the Aurora batch fails to
	 *  execute, this method may be called again to set the batch error flag
	 *  on the actions.
	 *  
	 *  Required binder parameters:
	 *  
	 *  -accountActionIds:		comma-separated list of Account Action IDs.
	 *  -batchId:				Aurora Batch ID value
	 *  -hasBatchErrors:		0/1 flag indicating whether the Aurora batch
	 *  						has errors
	 * @throws ServiceException 
	 * @throws DataException 
	 *  
	 */
	public void updateAccountActionsBatchId() 
	 throws ServiceException, DataException {
		
		String accountActionIds 	= m_binder.getLocal("accountActionIds");
		String batchIdStr			= m_binder.getLocal("batchId");
		
		boolean batchErrors  = CCLAUtils.getBinderBoolValue
		 (m_binder, "hasBatchErrors");
		
		if (StringUtils.stringIsBlank(accountActionIds)
			||
			StringUtils.stringIsBlank(batchIdStr)) {
			
			throw new ServiceException("Unable to update Account Action " +
			 "batch IDs: accountActionIds/batchId values missing");
		}
		
		FWFacade facade 	= CCLAUtils.getFacade(m_workspace);
		
		String[] actionIds	= accountActionIds.split(",");
		Integer batchId		= Integer.parseInt(batchIdStr);
		
		Log.debug("Updating Aurora Batch ID: '" + batchId + "', " +
		 "error flag: '" + batchErrors + "' across the " +
		 "following account actions: " + accountActionIds);
		
		try {
			facade.beginTransaction();
			
			// Loop through each account action, applying new batch ID/
			// error flag.
			for (String actionIdStr : actionIds) {
				int actionId = Integer.parseInt(actionIdStr.trim());
						
				AccountAction action = AccountAction.get(actionId, facade);
				Log.debug("Updating Account Action " + action.getActionId());
				
				action.setAuroraBatchId(batchId);
				action.setBatchErrors(batchErrors);
				
				action.persist(facade);
				
				action.addAudit(AccountActionAudit.ActionType.BATCH, 
				 "Batched account action to Aurora Batch ID " + batchId, 
				 m_userData.m_name, facade);
			}
			
			facade.commitTransaction();
			
		} catch (Exception e) {
			facade.rollbackTransaction();
			
			String msg = "Failed to updated Account Action batch IDs: " 
			 + e.toString(); 
			
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}
	
	/** 
	 *  Updates the Auth. Status value for all account actions with the
	 *  given Batch ID.
	 *  
	 *  Required binder parameters:
	 *  
	 *  batchId:		the Aurora Batch ID to match against
	 *  authStatus:		the Auth. status value which will be applied
	 *  				(can be blank)
	 * @throws DataException 
	 *  
	 */
	public void updateAuthStatusByBatchId() throws ServiceException, DataException {
		
		String batchId 		= m_binder.getLocal("batchId");
		String authStatus 	= m_binder.getLocal("authStatus");
		
		if (StringUtils.stringIsBlank(batchId))
			throw new ServiceException("Unable to update Auth Status flags, " +
			 "Aurora Batch ID missing");
		
		Log.debug("Updating Auth Status flag to '" + authStatus + 
		 "' for batch ID: " + batchId);

		FWFacade facade = CCLAUtils.getFacade(m_workspace);
		
		try {
			Vector<AccountAction> accountActions = 
			 AccountAction.getAccountActionsByBatchId(batchId, facade);
			
			Log.debug("Found " + accountActions.size() +
			 " matching account actions.");
			
			facade.beginTransaction();
			
			for (AccountAction action : accountActions) {
				Log.debug("Updating Account Action " + action.getActionId() +
				 " Auth Status to " + authStatus);
				
				action.setAuthStatus(authStatus);
				action.persist(facade);
				
				action.addAudit(AccountActionAudit.ActionType.UPDATE_AUTH_STATUS, 
				 "Updated Auth Status to " + authStatus, m_userData.m_name, facade);
			}
		
			facade.commitTransaction();
			
		} catch (Exception e) {
			facade.rollbackTransaction();
			
			String msg = "Failed to updated Account Action Auth Status: " 
			 + e.toString(); 
				
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}
	
	/** 
	 *  Updates the Submitted For Auth. flag for all account actions 
	 *  with the given Batch ID.
	 *  
	 *  Required binder parameters:
	 *  
	 *  batchId:			the Aurora Batch ID to match against
	 *  submittedForAuth:	the Submitted For Auth flag which will be
	 *  					applied (blank = false)
	 *  
	 * @throws DataException 
	 */
	public void updateSubmittedForAuthByBatchId() 
	 throws DataException, ServiceException {
		
		String batchId = m_binder.getLocal("batchId");

		if (StringUtils.stringIsBlank(batchId))
			throw new ServiceException("Unable to update Submitted For Auth " +
			 "flags, Aurora Batch ID missing");
		
		boolean submittedForAuth = 
		 CCLAUtils.getBinderBoolValue(m_binder, "submittedForAuth");
		
		Log.debug("Updating Submitted For Auth flag to '" + submittedForAuth +
		 "' for batch ID: " + batchId);
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace);
		
		try {
			Vector<AccountAction> accountActions = 
			 AccountAction.getAccountActionsByBatchId(batchId, facade);
			
			Log.debug("Found " + accountActions.size() +
			 " matching account actions.");
			
			facade.beginTransaction();
			
			for (AccountAction action : accountActions) {
				Log.debug("Updating Account Action " + action.getActionId());
				
				action.setSubmittedForAuth(submittedForAuth);
				action.persist(facade);
				
				action.addAudit(AccountActionAudit.ActionType.UPDATE_SUB_FOR_AUTH, 
				 "Updated Submitted to Auth flag: " + submittedForAuth, 
				 m_userData.m_name, facade);
			}
		
			facade.commitTransaction();
			
		} catch (Exception e) {
			facade.rollbackTransaction();
			
			String msg = "Failed to updated Account Action Submitted For Auth: " 
			 + e.toString(); 
				
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}
	
	/** Sets/unsets the Suspended flag for a particular Account Action. 
	 *  
	 *  Required binder parameters:
	 *  
	 *  accountActionId:		Account Action ID to update
	 *  suspended:				the Suspended flag which will be
	 *  						applied (blank = false)
	 *  
	 * @throws ServiceException
	 * @throws DataException
	 */
	public void setAccountActionSuspended() 
	 throws ServiceException, DataException {
		
		String actionIdStr	= m_binder.getLocal("accountActionId");
		boolean suspend		= 
		 CCLAUtils.getBinderBoolValue(m_binder, "suspended");
		
		if (StringUtils.stringIsBlank(actionIdStr)) {
			throw new ServiceException("Unable to set suspended status: " +
			 "Account Action ID missing");
		}
		
		int actionId		= Integer.parseInt(actionIdStr);
		FWFacade facade 	= CCLAUtils.getFacade(m_workspace);
		
		try {
			facade.beginTransaction();
			
			Log.debug("Setting suspended flag to " + suspend + " for Account " +
			 "Action " + actionId);
			
			AccountAction action = AccountAction.get(actionId, facade);
			action.setSuspended(suspend);
			action.persist(facade);
			
			if (suspend)
				action.addAudit(AccountActionAudit.ActionType.SUSPEND, 
				 "Account action suspended", m_userData.m_name, facade);
			else
				action.addAudit(AccountActionAudit.ActionType.RESUME, 
				 "Account action resumed", m_userData.m_name, facade);
			
			facade.commitTransaction();
			
		} catch (Exception e) {
			facade.rollbackTransaction();
			
			String msg = "Failed to updated Account Action Suspended flag: " 
			 + e.toString(); 
				
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}
}
