package com.ecs.stellent.ccla.clientservices;

import java.util.Vector;

import com.ecs.stellent.ccla.clientservices.form.FundTransferFormUtils;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.ClientProcess;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.DateFormatter;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.data.ResultSet;
import intradoc.data.ResultSetUtils;
import intradoc.server.Service;

/** Service methods used by CCLA_ClientServices component. */
public class ClientServicesService extends Service {
	
	/** Used by AML database provider. Requires a client number
	 *  to be appended. */
	public static final String GET_CORRESPONDENT_RESPONDED_AML_QUERY =
	 "SELECT Correspondent_Responded AS Responded " +
	 "FROM AML_CheckList " +
	 "WHERE (Client_Number = ";
	
	/** Called when the user clicks the Next button on the Set Fund
	 *  Transfers page.
	 *  
	 *  Checks for a 'back' action. If found, take the user back to
	 *  to Step 2. Otherwise do nothing.
	 *  
	 * @throws ServiceException
	 */
	public void confirmFundTransfers() throws ServiceException, Exception {
		
		String submitType = m_binder.getLocal("submitType");
		
		if (!StringUtils.stringIsBlank(submitType) &&
			submitType.equalsIgnoreCase("back")) {
			// User clicked Previous/Back button.
			// Load previous screen template (step 3)	
			this.m_serviceData.m_htmlPage = "CCLA_CS_CLIENT_LOOKUP";
			
			// Ensure process/activity data loaded on postback
			getProcess();
			getActivity();
			
			return;
		}
	}
	
	/** Called when the user clicks the Submit button on the Confirm
	 *  Fund Transfers page.
	 *  
	 *  First checks for a 'back' action. If this was an actual submission,
	 *  all selected account data must be lifted from the binder and
	 *  persisted.
	 *  
	 * @throws ServiceException
	 */
	public void submitFundTransfers() throws ServiceException, Exception {
		
		String submitType = m_binder.getLocal("submitType");
		
		if (!StringUtils.stringIsBlank(submitType) &&
			submitType.equalsIgnoreCase("back")) {
			// User clicked Previous/Back button.
			// Load previous screen template (step 3)
			this.m_serviceData.m_htmlPage = "CCLA_CS_SET_FUND_TRANSFERS";
			
			// Ensure process/activity data loaded on postback
			getProcess();
			getActivity();
			
			return;
		} else {
			// User clicked Submit button. Persist transfer info
			int updatedTransfers = setFundTransfers();
			
			String totalTransfers = m_binder.getLocal("totalTransfers");
			
			// Now perform a redirect to the confirmation page
			String redirectUrl = m_binder.getLocal("redirectOnSuccessUrl");
			redirectUrl += "&updatedTransfers=" + updatedTransfers + 
			 "&totalTransfers=" + totalTransfers;
			
			m_binder.putLocal("RedirectUrl", redirectUrl);
			prepareRedirect();
		}
	}
	
	/** Called when a correspondent process is required. This method will first
	 *  check for an existing correspondent process with the given correspondent 
	 *  ID, company and process name. 
	 *  
	 *  If there is no existing process, a new one is generated.
	 *  @deprecated
	 */
	public void getOrAddPersonProcess() throws DataException, Exception {
		
		String personId 	= m_binder.getLocal("personId");
		String company		= m_binder.getLocal("company");
		String campaignId	= m_binder.getLocal("campaignId");
		
		Log.debug("Checking for existing person process with campaign ID:" 
		 + campaignId + "', person ID=" + personId + ", company=" + company);
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace,true);
		
		DataResultSet personProcess =
		 facade.createResultSet("qClientServices_GetPersonProcess", m_binder);
		
		if (personProcess.isEmpty()) {
			// No correspondent process exists yet, fail.
			throw new ServiceException("No process exists for this person and campaign. " +
			 "Enroll them to the campaign first.");
			
			// No correspondent process exists yet, create one now.
			/*
			// Fetch new primary key
			String newProcessId = ClientServicesUtils.getNewKey("ClientProcess", facade);
			Log.debug("Creating new person process with ID: " + newProcessId);
			
			m_binder.putLocal("newProcessId", newProcessId);
			
			String curDate = DateFormatter.getTimeStamp();
			
			m_binder.putLocal("curDate", curDate);
			m_binder.putLocal("processStatus", "Open");
			
			ClientServicesUtils.addQueryParamToBinder(m_binder, "clientId", m_binder.getLocal("clientId"));
			
			facade.execute("qClientServices_AddPersonProcess", m_binder);
			
			// Now we can fetch the process ResultSet.
			personProcess =
			 facade.createResultSet("qClientServices_GetPersonProcess", m_binder);
			*/
		} else {
			Log.debug("Found existing person process with ID: " + 
			 personProcess.getStringValueByName("PROCESS_ID"));
		}
		
		String processId = personProcess.getStringValueByName("PROCESS_ID");
		m_binder.putLocal("processId", processId);
		
		m_binder.addResultSet("rsProcess", personProcess);
	}
	
	/** Fetches a process by its process ID.
	 * 
	 *  @throws ServiceException if processId not present in binder,
	 *  		or no matching process found. 
	 * 
	 * @deprecated
	 */
	public void getProcess() throws ServiceException, DataException {
		
		String processId = m_binder.getLocal("processId");
		Log.debug("Fetching process by ID: " + processId);
		
		if (StringUtils.stringIsBlank(processId))
			throw new ServiceException("Unable to fetch process: " +
			 "processId missing from binder.");
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace);
		
		DataResultSet rsProcess = ClientProcess.getData(
		 Integer.parseInt(processId), facade);
		
		if (rsProcess.isEmpty())
			throw new ServiceException("No process exists with ID: " + 
			 processId);
		
		m_binder.addResultSet("rsProcess", rsProcess);
	}
	
	/** Fetches existing data for a single activity, by the
	 *  activity ID.
	 * 
	 * @throws ServiceException if correspondentId missing from binder, or
	 * 							no matching activity found in database
	 * @throws Exception
	 */
	public void getActivity() throws ServiceException, Exception {
		String activityId = m_binder.getLocal("activityId");
		
		Log.debug("Fetching activity with ID: " + activityId);
		
		if (StringUtils.stringIsBlank(activityId))
			throw new ServiceException("Unable to fetch activity, " +
			 "activityId missing from binder.");
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace,true);
		
		DataResultSet corrExchange =
		 facade.createResultSet("qClientServices_GetActivity", m_binder);
		
		if (corrExchange.isEmpty())
			throw new ServiceException("Unable to fetch activity, " +
			 "no activity with ID " + activityId + " found.");
		
		m_binder.addResultSet("rsActivity", corrExchange);
	}
	
	/** Fetches all existing activities for a given person ID.
	 *  
	 *  Adds them to the binder as a ResultSet called 
	 *  rsCorrespondentExchanges_<correspondentId>
	 *  
	 * @throws ServiceException if correspondentId missing from binder
	 */
	public void getActivitiesByPerson() throws ServiceException, Exception {
		String personId = m_binder.getLocal("personId");
		
		Log.debug("Fetching existing activities for person: " + personId);
		
		if (StringUtils.stringIsBlank(personId))
			throw new ServiceException("Unable to fetch activities, " +
			 "personId missing from binder.");
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace,true);
		
		DataResultSet corrExchanges =
		 facade.createResultSet("qClientServices_GetActivitiesByPersonId", m_binder);
		
		m_binder.addResultSet("rsPersonActivities", corrExchanges);
	}
	
	/** Service method used to fetch all accounts associated with 
	 *  the given correspondent. Requires a correspondentId and
	 *  company in the binder.
	 */
	public void getCorrespondentAccounts() throws ServiceException, Exception {
		
		String corrId 	= m_binder.getLocal("correspondentId");
		String company	= m_binder.getLocal("company");
		
		DataResultSet rsCorrAccounts = getCorrespondentAccounts(company, corrId);
		m_binder.addResultSet("rsCorrespondentAccounts", rsCorrAccounts);
	}
	
	/** Dummy data method, prior to implementation of Aurora web services.
	 * 
	 *  Fetches a list of fake accounts.
	 * 
	 * @throws ServiceException
	 * @throws Exception
	 */
	private static DataResultSet getCorrespondentAccounts
	 (String company, String correspondentId) 
	 throws ServiceException, Exception {
		
		// Test code.
		String[] cols = new String[] {
			"clientNumber","accountNumber","fund","accountType","numUnits","price"
		};
		
		DataResultSet corrAccounts = new DataResultSet(cols);
		
		String[][] testData = new String[][] {
			{"101001","001T","T","DEP","452","34.2"},
			{"101001","002T","T","DEP","27","17.3"},
			{"101001","002R","R","DEP","62","60.2"},
			{"103212","001T","T","DEP","452","12.9"},
			{"140042","006R","R","DEP","22","38.1"}
		};
		
		for (int i=0; i<testData.length; i++) {
			String[] thisAcc 	= testData[i];
			Vector values 		= new Vector();
			
			for (int j=0; j<thisAcc.length; j++) {
				String thisVal = thisAcc[j];
				values.add(thisVal);
			}
			
			corrAccounts.addRow(values);
		}
		
		return corrAccounts;
	}
	
	/** Fetches all existing client activity data for a process ID.
	 * 
	 */
	public void getActivitiesByProcess() throws ServiceException,
	 Exception {
		String processId = m_binder.getLocal("processId");
		
		Log.debug("Fetching all activities with process ID: " 
		 + processId);
		
		if (StringUtils.stringIsBlank(processId))
			throw new ServiceException("Unable to fetch activities, " +
			 "processId missing from binder.");
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace,true);
		
		DataResultSet rsActivities =
		 facade.createResultSet
		 ("qClientServices_GetActivitiesByProcessId", m_binder);
		
		Log.debug("Found " + rsActivities.getNumRows() + " activities");
		
		m_binder.addResultSet("rsProcessActivities", rsActivities);
	}
	
	/** Persists any fund transfers which the user has opted for.
	 * 	
	 *  Adds the number of updated/added transfers to the binder as
	 *  updatedTransfers. The total number of transfers is added as
	 *  totalTransfers.
	 *  
	 *  @return the number of persisted transfers.
	 */
	public int setFundTransfers() throws ServiceException, Exception {
		
		String processId 		= m_binder.getLocal("processId");
		String activityId 		= m_binder.getLocal("activityId");
		String personId 		= m_binder.getLocal("personId");
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace,true);
		
		// Retrieve list of full account numbers
		String accountNumberList	= m_binder.getLocal("fullAccountNumberList");
		
		if (StringUtils.stringIsBlank(accountNumberList)) {
			Log.debug("No valid accounts for correspondent - " +
			 "no transfer data will be stored.");
			
			m_binder.putLocal("updatedTransfers", "0");
			m_binder.putLocal("totalTransfers", "0");
			
			return 0;
		}
			
		// Delete any existing account transfers for this correspondent
		/*
		Log.debug("Deleting any existing transfers for process ID: " + processId);
		
		facade.execute("qClientServices_DeleteFundTransfersByProcessId", 
		 m_binder);
		*/
		
		String[] accountList = accountNumberList.split(",");
		
		// Fetch the existing transfers ResultSet from binder
		// (should be there before calling this method)
		ResultSet existingFundTransfers =
		 m_binder.getResultSet("rsExistingTransfers");
		
		DataResultSet rsExistingFundTransfers = new DataResultSet();
		rsExistingFundTransfers.copy(existingFundTransfers);
		
		m_binder.putLocal("totalTransfers", 
		 Integer.toString(rsExistingFundTransfers.getNumRows()));
		
		Log.debug("Attempting to store " + accountList.length + " fund transfers " +
		 "for process ID: " + processId);
		
		int transfers = 0;
		
		boolean accountsAddedOrUpdated = false;
		
		for (int i=0; i<accountList.length; i++) {
			String thisAccount = accountList[i];
			
			String company  = m_binder.getLocal("company_" + thisAccount);
			String clientId = m_binder.getLocal("clientId_" + thisAccount);
			
			String accountNumber = m_binder.getLocal("accountNumber_" + thisAccount);
			String fund			 = m_binder.getLocal("fund_" + thisAccount);
			
			String transferType  	= m_binder.getLocal("transferType_" + thisAccount);
			String transferAmount	= m_binder.getLocal("transferAmount_" + thisAccount);
			String accountStatus	= m_binder.getLocal("accountStatus_" + thisAccount);
			
			Log.debug("***************************************** accountStatus:" + accountStatus);
			
			boolean addedOrUpdated = addOrUpdateFundTransfer(processId, activityId, 
			 clientId, company, personId, accountNumber, fund,
			 transferType, transferAmount, accountStatus,
			 facade, rsExistingFundTransfers);
			
			if (addedOrUpdated) {
				accountsAddedOrUpdated = true;
				transfers++;
			}
		}
		
		// Add number of updated transfers to DataBinder
		m_binder.putLocal("updatedTransfers", Integer.toString(transfers));
		
		// Update the activity action to indicate fund transfers were updated
		if (accountsAddedOrUpdated) {
			/*
			TODO: replace with call to activity.persist()
			setActivityAction
			 (activityId, m_binder.getLocal("activityAction"), m_workspace);
			 */
		}
		
		return transfers;
	}
	
	/** Adds a new fund transfer entry if one doesn't exist for the given
	 *  process ID, company and account number, or updates the existing
	 *  one.
	 * 
	 *  If the account already exists, and the new data doesn't differ
	 *  from the old data, no change is made and the method returns false.
	 *  
	 * @param processId
	 * @param company
	 * @param accountNumber
	 * @param transferType
	 * @param transferAmount
	 * @param facade
	 * @param existingFundTransfers 	set of existing fund transfers
	 * 									for this process ID
	 * @return whether or not account values were updated
	 * 
	 * @throws ServiceException
	 * @throws Exception
	 */
	private static boolean addOrUpdateFundTransfer
	 (String processId, String activityId,
	  String clientId, String company, String personId,
	  String accountNumber, String fund, String transferType, 
	  String transferAmount, String accountStatus,
	  FWFacade facade, DataResultSet existingFundTransfers)
	 throws ServiceException, Exception {
		
		int existingTransferRow = -1;
		DataBinder binder = new DataBinder();
		
		Log.debug("Adding/updating fund transfer: " + 
		 "\nprocessId=" + processId +
		 "\nactivityId=" + activityId +
		 "\nclientId=" + clientId +
		 "\ncompany=" + company +
		 "\npersonId=" + personId +
		 "\naccount no=" + accountNumber +
		 "\nfund=" + fund + 
		 "\ntransfer type=" + transferType +
		 "\ntransferAmount=" + transferAmount +
		 "\naccountStatus=" + accountStatus);
		
		
		// Add query data to binder.
		binder.putLocal("processId", processId);
		binder.putLocal("activityId", activityId);
		binder.putLocal("personId", personId);
		binder.putLocal("clientId", CCLAUtils.padClientNumber(clientId));
		binder.putLocal("company", company);
		binder.putLocal("accountNumber", accountNumber);
		binder.putLocal("fund", fund);
		
		if(!StringUtils.stringIsBlank(accountStatus))
			binder.putLocal("accountStatus", accountStatus);
		else
			binder.putLocal("accountStatus", "");
		
		binder.putLocal("transferType", transferType);
		binder.putLocal("transferAmount", transferAmount);
		
		// Add null values for Aurora input fields
		binder.putLocal("toAccount", "");
		binder.putLocal("toFund", "");
		binder.putLocal("auroraStatus", "");
		binder.putLocal("jobId", "");
		binder.putLocal("falseFlag", "0");
		binder.putLocal("emptyString", "");
		
		// Search through existing transfers to see if this transfer
		// has already been registered.
		if (!existingFundTransfers.isEmpty()) {
			existingFundTransfers.first();
			
			do {
				String thisClient = 
				 ResultSetUtils.getValue(existingFundTransfers, "CLIENT_ID");
				
				String thisAccountNumber =
				 ResultSetUtils.getValue(existingFundTransfers, "ACCOUNT_NUMBER");
				
				// Check if this fund transfer matches the passed one
				if (clientId.equals(thisClient) 
					&& accountNumber.equals(thisAccountNumber)) {
					existingTransferRow = 
					 existingFundTransfers.getCurrentRow();
		
					break;
				}
				
			} while (existingFundTransfers.next());
		}
		
		if (existingTransferRow == -1) {
			// No transfer for this account exists yet, create one now.
			
			// Fetch new primary key
			String newTransferId = CCLAUtils.getNewKey("FundTransfer", facade);
			Log.debug("Creating new fund transfer with ID: " + newTransferId);
			
			binder.putLocal("newTransferId", newTransferId);
			binder.putLocal("curDate", DateFormatter.getTimeStamp());
			
			facade.execute("qClientServices_AddFundTransfer", binder);
			
			return true;
			
		} else {
			// Retrieve data for existing fund transfer
			existingFundTransfers.setCurrentRow(existingTransferRow);
			
			String thisTransferId = 
			 existingFundTransfers.getStringValueByName("TRANSFER_ID");
			
			Log.debug("Found existing fund transfer with ID: " + 
			 thisTransferId);
			
			String thisTransferType = 
			 existingFundTransfers.getStringValueByName("TRANSFER_TYPE");
			
			String thisTransferAmount = 
			 existingFundTransfers.getStringValueByName("TRANSFER_AMOUNT");
			
			String thisAccountStatus = 
				 existingFundTransfers.getStringValueByName("ACCOUNT_STATUS");
			
			if (thisTransferType.equals(transferType) &&
				thisTransferAmount.equals(transferAmount) &&
				thisAccountStatus.equals(accountStatus)) {
				Log.debug("Transfer type/amount/account status unchange, no update required.");
				return false;
			} else {
				binder.putLocal("transferId", thisTransferId);
				facade.execute("qClientServices_UpdateFundTransfer", binder);
				
				Log.debug("Updated existing transfer.");
				return true;
			}
		}
	}
	
	/** Fetches all existing fund transfers for a given process ID.
	 * 
	 */
	public void getFundTransfersByProcessId() throws ServiceException, Exception {
		
		String processId = m_binder.getLocal("processId");
		
		if (StringUtils.stringIsBlank(processId))
			throw new ServiceException("Unable to fetch fund transfers: " +
			"process ID was missing");
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace,true);
		DataResultSet rsFundTransfers =
		 facade.createResultSet
		 ("qClientServices_GetFundTransfersByProcessId",m_binder);
		
		Log.debug("Found " + rsFundTransfers.getNumRows() + 
		 " existing fund transfers for process ID: " + processId);
		
		m_binder.addResultSet("rsFundTransfers", rsFundTransfers);
		
		// If any existing fund transfers were found, extract the
		// activity data which applied these transfers.
		if (!rsFundTransfers.isEmpty()) {
			String activityId = 
			 ResultSetUtils.getValue(rsFundTransfers, "ACTIVITY_ID");
			
			DataBinder activityBinder = new DataBinder();
			activityBinder.putLocal("activityId", activityId);
			
			DataResultSet fundTransferActivity = 
			facade.createResultSet("qClientServices_GetActivity", 
			 activityBinder);
			
			m_binder.addResultSet("rsFundTransferActivity", fundTransferActivity);
		}
	}
	
	/** Adds a correspondent question to the database. */
	public void addCorrespondentQuestion() throws Exception {
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace,true);
		String newId = CCLAUtils.getNewKey("CorrespondentQuestion", facade);
		
		m_binder.putLocal("newQuestionId", newId);
		m_binder.putLocal("userId", m_userData.m_name);
		
		m_binder.putLocal("curDate", DateFormatter.getTimeStamp());
		
		// Check for required insert values. If they don't exist yet,
		// use special null values instead.
		String clientId 	= m_binder.getLocal("clientId");
		String processId	= m_binder.getLocal("processId");
		String corrId		= m_binder.getLocal("personId");
		
		String questionAnswer	= m_binder.getLocal("questionAnswer");
		
		if (StringUtils.stringIsBlank(processId))
			m_binder.putLocal("processId", "-1");
		
		if (StringUtils.stringIsBlank(clientId))
			m_binder.putLocal("clientId", "-1");
		
		if (StringUtils.stringIsBlank(corrId))
			m_binder.putLocal("correpondentId", "-1");
		
		facade.execute("qClientServices_AddCorrespondentQuestion", m_binder);
	}
	
	/** Called after a Fund Transfer activity is signed off.
	 * If the activity outcome is Completed, the Fund Transfer form
	 * is automatically generated. 
	 * 
	 * The associated correspondent process status is then updated to 
	 * "Pending account creation"
	 */
	public void checkForTransferCompletion() 
	 throws ServiceException, DataException, Exception {
		
		String activityOutcome = m_binder.getLocal("activityOutcome");
		
		// This flag indicates the correspondent has no transfers set.
		// This will prevent the form being generated.
		boolean noTransfers = !StringUtils.stringIsBlank
		 (m_binder.getLocal("noTransfers"));
		
		if (activityOutcome.equals("Completed") && !noTransfers) {
			FundTransferFormUtils.checkinEthicalFundForm(m_binder, 
			 CCLAUtils.getFacade(m_workspace), m_userData.m_name);
			
			// Check for print form flag
			boolean printForm = 
			 !StringUtils.stringIsBlank(m_binder.getLocal("printForm"));
			
			// Add the new form dDocName to the redirect URL
			String docName = m_binder.getLocal("formDocName");

			String redirectUrl 	= m_binder.getLocal("RedirectUrl");
			redirectUrl 		+= "&formDocName=" + docName; 
			
			m_binder.putLocal("processStatus", "Account creation pending");
			
			// TODO: Replace with process.persist()
			//updateCorrespondentProcessStatus();
			
			/*
			if (printForm)
				printEthicalFundForm();
			*/
		}
	}
	
	/**
	 * Adds or updates a note in the CS_FUND_TRANSFERS_NOTES table for a
	 * particular transfer id.
	 */
	public void addOrUpdateFundTransferNote() throws ServiceException{
		String transferId  = "";
		String note = "";
		
		try{
			transferId = m_binder.getLocal("transferId");
			note = m_binder.getLocal("note");
			
			DataBinder binder = new DataBinder();
			binder.putLocal("transferId", 	transferId);
			binder.putLocal("note", 		note);
			
			FWFacade facade = CCLAUtils.getFacade(m_workspace,true);
			
			try{
				facade.execute("qClientServices_InsertFundTransferNote", binder);
				Log.debug("Added note to transferId " + transferId);
			}catch(Exception e){
				Log.warn("Unable to perform qClientServices_InsertFundTransferNote, " +
						"attempting update");
				
				//Try update instead
				facade.execute("qClientServices_UpdateFundTransferNote", binder);
			}
			
		}catch(Exception e){
			Log.error("Unable to add note for transferId (" + transferId + "):" 
					+ e.getMessage(), e);
			throw new ServiceException("Unable to add note: " + e.getMessage(), e);
		}
	}
}
