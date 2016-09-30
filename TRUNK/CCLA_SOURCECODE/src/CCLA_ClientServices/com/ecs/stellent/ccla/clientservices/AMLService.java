package com.ecs.stellent.ccla.clientservices;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Vector;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.ClientProcess;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ServiceException;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.data.ResultSetUtils;
import intradoc.server.Service;

public class AMLService extends Service {
	
	/** Calculates various totals based on the CCLA_AML_CHECKLIST
	 *  table copied over from the legacy AML system.
	 *  
	 */
	public void getAMLSummary() throws DataException {
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		Log.debug("AML Summary: Fetching all legacy AML data");
		
		DataResultSet rsAMLData = facade.createResultSetSQL
		 ("SELECT * FROM CCLA_AML_CHECKLIST");
		
		Log.debug("AML Summary: Found " + rsAMLData.getNumRows() +
		 " client AML entries.");
		
		DataResultSet rsAMLTotals = getAMLFlagTotals(rsAMLData);
		m_binder.addResultSet("rsAMLTotals", rsAMLTotals);		
	}
	
	/** Fetches extended AML information for a particular client. 
	 * 
	 * @throws DataException 
	 * @throws ServiceException */
	public void getClientAMLInfo() throws DataException, ServiceException {
		
		String entityIdStr	= m_binder.getLocal("ORGANISATION_ID");
		
		if (StringUtils.stringIsBlank(entityIdStr))
			throw new ServiceException(
			 "Unable to retreive client AML info, " +
			 "ORGANISATION_ID was not specified.");
		
		int entityId		= Integer.parseInt(entityIdStr);
		FWFacade facade 	= CCLAUtils.getFacade(m_workspace, true);
		
		DataResultSet rsAMLInfo = facade.createResultSet(
		 "qClientServices_GetAMLChecklistInfo", m_binder);
		
		m_binder.addResultSet("rsAMLInfo", rsAMLInfo);
		
		m_binder.putLocal("parentElementId",m_binder.getLocal("ORGANISATION_ID"));
		
		// Fetch full account list
		DataResultSet rsAccounts = 
		 facade.createResultSet("qClientServices_GetRelatedAccounts", m_binder);
		
		m_binder.addResultSet("rsAccounts", rsAccounts);
		
		if (rsAccounts != null && !rsAccounts.isEmpty()) {
			Vector<Account> accounts = Account.getAccounts(rsAccounts);
			
			DataResultSet rsAccountPersonRelations = 
			 Account.getPersonRelationsData(accounts, facade);
	
			m_binder.addResultSet
			 ("rsAccountPersonRelations", rsAccountPersonRelations);
			
			/*
			DataResultSet rsCorrespondents = 
			 CCLAAccount.getCorrespondentData(accounts, facade);
			
			m_binder.addResultSet("rsAccountCorrespondents", rsCorrespondents);
			*/
		}
		
		// Fetch accounts eligible for AML checking
		DataResultSet rsAMLAccounts = 
		 AMLUtils.getAMLAccounts(entityId, facade);
		
		ResultSetUtils.sortResultSet(rsAMLAccounts, new String[] {
			"ACC_FUNDCODE", "ACC_ACCOUNTNUMBER"
		});
		
		m_binder.addResultSet("rsAMLAccounts", rsAMLAccounts);
		
		DataResultSet rsAmlChecked = AMLUtils.getAMLAccountsStatus
		 (entityId, facade);
		
		m_binder.addResultSet("rsAmlChecked", rsAmlChecked);
	}
	
	/** Adds/removes entries from the CCLA_ACCOUNTS_AML_STATUS table.
	 *  
	 *  This is a temporary table, used to flag accounts involved in the
	 *  AML campaign as having completed their AML checks.
	 * 
	 *  If an account features in this table, it has been flagged as complete.
	 * 
	 * @throws ServiceException
	 * @throws DataException
	 */
	public void updateAccountsAMLStatus() throws ServiceException, DataException {
		
		String processId	= m_binder.getLocal("PROCESS_ID");
		String entityId		= m_binder.getLocal("ORGANISATION_ID");
		
		if (StringUtils.stringIsBlank(processId) || 
			StringUtils.stringIsBlank(entityId)) {
			throw new ServiceException("Unable to update account AML status: " +
			 "process ID/entity ID not specified");
		}
		
		String accountList 	= m_binder.getLocal("accounts"); 
		Log.debug("Updating account AML status for Entity ID: " + entityId + 
		 ", accounts: " + accountList);
		
		String[] accounts = null;
		
		if (!StringUtils.stringIsBlank(accountList)) {
			accounts	= accountList.split(",");
		} else {
			accounts	= new String[0];
		}
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		try {
			facade.beginTransaction();
			
			ClientProcess process = ClientProcess.get
			 (Integer.parseInt(processId), facade);
			
			if (process == null)
				throw new ServiceException("Unable to find process with ID: " + 
				 processId);
			
			// Fetch list of previously checked accounts. Compare this
			// with submitted list of checked accounts.
			DataResultSet rsAmlAccounts =
			 facade.createResultSet("qClientServices_GetAccountsAMLStatus", 
			 m_binder);
			
			// Search for: 
			// -previously checked accounts which weren't in the
			//  submitted list. These need to be removed.
			if (!rsAmlAccounts.isEmpty()) {
				do {
					String rsAccNum = rsAmlAccounts.getStringValueByName("ACCNUMEXT");
					boolean accountExists = false;
					
					for (int i=0; i<accounts.length && !accountExists; i++) {
						if (rsAccNum.equals(accounts[i])) {
							// Account already exists in list of checked accounts,
							// no update required here.
							accountExists = true;
						}
					}
					
					if (!accountExists) {
						// Previously-checked account was not passed in submitted list,
						// remove it from the list of checked accounts.
						m_binder.putLocal("ACCNUMEXT", rsAccNum);
						facade.execute("qClientServices_DeleteAccountAMLStatus", m_binder);
						
						process.addActivity(m_userData.m_name, null, 
						 "Account AML status updated", "AML checks for " + rsAccNum + 
						 " marked as incomplete", 
						 null, false, facade);
					}
						
				} while (rsAmlAccounts.next());
			}
			
			// Search for:
			// -submitted accounts which aren't in the stored list
			//  of checked accounts. These need to be added.
			for (int i=0; i<accounts.length; i++) {
				boolean accountExists = false;
				rsAmlAccounts.first();
				
				if (!rsAmlAccounts.isEmpty()) {
					do {
						String rsAccNum = rsAmlAccounts.getStringValueByName("ACCNUMEXT");
						if (rsAccNum.equals(accounts[i])) {
							// Account already exists in list of checked accounts,
							// no update required here.
							accountExists = true;
						}
							
					} while (rsAmlAccounts.next() && !accountExists);
				}
			
				if (!accountExists) {
					// Account has not been added to checked list yet, add it now.
					m_binder.putLocal("ACCNUMEXT", accounts[i]);
					facade.execute("qClientServices_AddAccountAMLStatus", m_binder);
					
					process.addActivity(m_userData.m_name, null, 
					 "Account AML status updated", "AML checks for " + accounts[i] + 
					 " marked as complete", 
					 null, false, facade);
				}
			}
			
			facade.commitTransaction();
			
		} catch (Exception e) {
			facade.rollbackTransaction();
			String msg = "Failed to update account AML status";
			
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}
	
	public void getExcludedAMLSummary() throws DataException {
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		Log.debug("Excluded AML Summary: Fetching all AML clients not " +
		 "enrolled in new campaign");
		
		// Fetches all clients 
		DataResultSet rsAMLData = facade.createResultSetSQL
		 ("SELECT * FROM V_CLIENTS_AML_CAMPAIGN");
		
		Log.debug("AML Summary: Found " + rsAMLData.getNumRows() +
		 " excluded AML clients.");
		
		DataResultSet rsLegacyAMLTotals = getAMLFlagTotals(rsAMLData);
		m_binder.addResultSet("rsLegacyAMLTotals", rsLegacyAMLTotals);
		
		DataResultSet rsLegacyAMLStatusTotals = getAMLStatusTotals(rsAMLData);
		m_binder.addResultSet("rsLegacyAMLStatusTotals", rsLegacyAMLStatusTotals);
	}
	
	/** Takes a raw ResultSet derived from the CCLA_AML_CHECKLIST table
	 *  and computes a list of totals for various flag fields.
	 *  
	 * @param rsAMLData
	 * @return ResultSet containing summary values
	 */
	private static DataResultSet getAMLFlagTotals(DataResultSet rsAMLData) {
		
		int completed 	= 0;
		int responded	= 0;
		int bauOut 		= 0;
		int bauIn		= 0;
		int onHold		= 0;
		int qrs			= 0;
		int ivs			= 0;
		int closed		= 0;
		int numInactive = 0;
		
		if (rsAMLData.first() && !rsAMLData.isEmpty()) {
			do {
				if (rsAMLData.getStringValueByName
					("CORRESPONDENT_RESPONDED").equals("1"))
					completed++;
				
				String respTypeStr = rsAMLData.getStringValueByName
				 ("CORRESPONDENT_RESPONSE_TYPE");
				
				int respType = 0;
				
				if (!StringUtils.stringIsBlank(respTypeStr))
					respType = Integer.parseInt(respTypeStr);
				
				if (respType > 0)
					responded++;
				
				if (rsAMLData.getStringValueByName
					("CORRESPONDENT_OVERRIDE").equals("1"))
					bauIn++;
				
				if (rsAMLData.getStringValueByName
					("CORRESPONDENT_REQUESTED").equals("1"))
					bauOut++;
				
				if (rsAMLData.getStringValueByName
					("CORRESPONDENT_ONHOLD").equals("1"))
					onHold++;
				
				if (rsAMLData.getStringValueByName
					("CORRESPONDENT_QRS_CLIENT").equals("1"))
					qrs++;
				
				if (rsAMLData.getStringValueByName
					("CORRESPONDENT_IVS_PRE1207").equals("1"))
					ivs++;
				
				if (rsAMLData.getStringValueByName
					("CORRESPONDENT_ACCOUNT_CLOSED").equals("1"))
					closed++;
				
				String numAMLAccounts = rsAMLData.getStringValueByName
				 ("NUM_AML_ACCOUNTS");
				
				if (!StringUtils.stringIsBlank(numAMLAccounts) &&
					numAMLAccounts.equals("0")) {
					numInactive++;
				}
				
			} while (rsAMLData.next());
		}
			
		// Add all counts to a new ResultSet
		DataResultSet rsAMLTotals = new DataResultSet(
		 new String[] {"LABEL","COUNT"});
		
		String[] labels = new String[] {
			"Completed", "Responded", "BAU out", "BAU in", "On hold", "QRS clients", 
			"IVS checked pre-Dec 07", "Accounts marked for closure", "AML accounts closed", 
			"Total AML clients"
		};
		
		int[] values = new int[] {
			completed, responded, bauOut, bauIn, onHold, qrs, ivs, closed, numInactive, 
			rsAMLData.getNumRows()
		};
		
		for (int i=0; i<labels.length; i++) {
			Vector<String> v = new Vector<String>();
		
			v.add(labels[i]);
			v.add(Integer.toString(values[i]));
			rsAMLTotals.addRow(v);
		}
		
		return rsAMLTotals;
	}
	
	/** Determines a status for each row of the passed legacy AML data.
	 * 
	 * @param rsAMLData
	 * @return
	 */
	private static DataResultSet getAMLStatusTotals(DataResultSet rsAMLData) {
		
		// Status counters
		int completed			= 0;
		int returned			= 0;
		int excluded			= 0;
		int formsSent			= 0;
		int markedForClosure	= 0;
		int inactive			= 0;
		int pending				= 0;
		
		if (rsAMLData.first() && !rsAMLData.isEmpty()) {
			do {
				boolean isCompleted	= false;
				boolean responded	= false;
				boolean bauIn		= false;
				boolean bauOut		= false;
				boolean onHold		= false;
				boolean qrs			= false;
				boolean ivs			= false;
				boolean closed		= false;
				boolean isInactive	= false;
				
				isCompleted = rsAMLData.getStringValueByName
				 ("CORRESPONDENT_RESPONDED").equals("1");
				
				String respTypeStr = rsAMLData.getStringValueByName
				 ("CORRESPONDENT_RESPONSE_TYPE");
				
				int respType = 0;
				
				if (!StringUtils.stringIsBlank(respTypeStr))
					respType = Integer.parseInt(respTypeStr);
				
				if (respType > 0)
					responded = true;

				bauIn =rsAMLData.getStringValueByName
				 ("CORRESPONDENT_OVERRIDE").equals("1");
				
				bauOut = rsAMLData.getStringValueByName
				 ("CORRESPONDENT_REQUESTED").equals("1");
				
				onHold = rsAMLData.getStringValueByName
				 ("CORRESPONDENT_ONHOLD").equals("1");
				
				qrs = rsAMLData.getStringValueByName
				 ("CORRESPONDENT_QRS_CLIENT").equals("1");
				
				ivs = rsAMLData.getStringValueByName
				 ("CORRESPONDENT_IVS_PRE1207").equals("1");
				
				closed = rsAMLData.getStringValueByName
				 ("CORRESPONDENT_ACCOUNT_CLOSED").equals("1");
				
				String numAMLAccounts = rsAMLData.getStringValueByName
				 ("NUM_AML_ACCOUNTS");
				
				isInactive = !StringUtils.stringIsBlank(numAMLAccounts) &&
				 numAMLAccounts.equals("0");
				
				// Conditional logic for determining legacy AML client
				// status
				if (isCompleted) {
					completed++;
				} else if (responded || bauIn) {
					returned++;
				} else if (isInactive) {
					inactive++;
				} else if (onHold || qrs) {
					excluded++;
				} else if (bauOut) {
					formsSent++;
				} else if (closed) {
					markedForClosure++;
				} else {
					pending++;
				}
				
			} while (rsAMLData.next());
		}
		
		// Add all counts to a new ResultSet
		DataResultSet rsAMLTotals = new DataResultSet(
		 new String[] {"LABEL","COUNT","PERCENTAGE"});
		
		String[] labels = new String[] {
			"Completed", "Returned", "AML accounts closed", "Excluded",
			"Forms sent", "Accounts marked for closure", "Pending",
			"Total"
		};
		
		int[] values = new int[] {
			completed, returned, inactive, excluded, formsSent, 
			markedForClosure, pending, rsAMLData.getNumRows()
		};
		
		NumberFormat pcFormat = DecimalFormat.getPercentInstance();
		pcFormat.setMaximumFractionDigits(1);
		
		for (int i=0; i<labels.length; i++) {
			Vector<String> v = new Vector<String>();
		
			v.add(labels[i]);
			v.add(Integer.toString(values[i]));
			
			// Compute percentage of total
			float percentage = (float)values[i] / (float)rsAMLData.getNumRows();
			v.add(pcFormat.format(percentage));
			
			rsAMLTotals.addRow(v);
		}
		
		return rsAMLTotals;
	}
	
	public static void main(String[] args) {
		
		NumberFormat pcFormat = DecimalFormat.getPercentInstance();
		
		int x = 257;
		int y = 600;
		
		// Compute percentage of total
		float percentage =  ((float)x / (float)y);
		System.out.println(pcFormat.format(percentage));
		
	}
}
