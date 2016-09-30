package com.ecs.stellent.ccla.clientservices.processhandler;

import java.util.Date;
import java.util.Vector;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.data.Activity;
import com.ecs.ucm.ccla.data.Campaign;
import com.ecs.ucm.ccla.data.ClientProcess;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.DateFormatter;
import com.ecs.utils.stellent.embedded.FWFacade;

public class ClientProcessHandler {
	
	protected static final String DEFAULT_INITIAL_STATUS = "Pending";
	protected static final String EXCLUDED_STATUS = "Excluded";
	
	/** Process creation methods */
	public static final int AUTO_CREATION = 0;
	public static final int MANUAL_CREATION = 1;
	
	protected Campaign campaign;

	protected FWFacade facade;
	protected String userName;
	
	/** Mapping of current status to permitted actions */
	protected String[][] statusActions = new String[][] {
		{"*",		"Cancel"}
	};
	
	private ClientProcessHandler() {}
	
	public ClientProcessHandler(Campaign campaign, String userName, FWFacade facade) {
		this.campaign = campaign;
		this.userName = userName;
		this.facade   = facade;
	}
	
	public String getInitialStatus() {
		return DEFAULT_INITIAL_STATUS;
	}
	
	/** Adds a new client process, for the given Entity ID.
	 *  
	 * @param entityId
	 * @param createMethod how the process was created (manually/automatically)
	 * @param initialStatus optional status for new Process.
	 * @param exclude whether to start the process in an excluded state
	 * @return the newly-created ClientProcess instance
	 * @throws DataException
	 * @throws ServiceException
	 */
	public ClientProcess addNewProcess(int entityId, Integer personId,
	 int createMethod, String initialStatus, boolean exclude) 
	 throws DataException, ServiceException {
		
		String create = "Creating new "; 
		
		if (exclude) {
			create 	= "Excluding ";
		}
		
		Log.debug(create + this.campaign.getName() + 
		 " campaign for entity/client " + entityId);
				
		try {
			facade.beginTransaction();
			
			String status; 
			
			if (initialStatus != null)
				status = initialStatus;
			else
				status = DEFAULT_INITIAL_STATUS;
			
			if (exclude)
				status =  EXCLUDED_STATUS;
			
			Date curDate = new Date();
			
			ClientProcess process = 
			 ClientProcess.add(this.campaign.getCampaignId(), status, 
			 entityId, this.userName, curDate, personId, facade);
			
			String processType 	= "Enrollment";
			String actionVerb	= "enrolled";
			
			if (exclude) {
				processType		= "Exclusion";
				actionVerb		= "excluded";
			}
			
			String method		= "manually";
			if (createMethod == AUTO_CREATION)
				method			= "automatically";
			
			String action 		= "Client " + actionVerb + " " + method;
			
			process.addActivity(
			 this.userName, personId, processType, action, null, 
			 false, facade);
			
			facade.commitTransaction();
			
			return process;
			
		} catch (ServiceException e) {
			facade.rollbackTransaction();
			throw new ServiceException(e);
		}
	}

	/** Applies a given action to the member process.
	 * 
	 *  Default behaviour simply updates the process status value.
	 *  
	 * @param action
	 * @throws DataException 
	 * @throws ServiceException 
	 */
	public void applyAction(ClientProcess process, String action, String note) 
	 throws DataException, ServiceException {
		
		Log.debug("Default Client Process Handler: applying action: " + action);
		
		if (action.equals("Exclude")) {
			setExcludedAddActivity(process, true, note);
			return;
		} else if (action.equals("Enroll")) {
			setExcludedAddActivity(process, false, note);
			return;
		}
		
		setStatus(process, action);
		
		process.addActivity(userName, null, "Status update", 
		 "Status updated to " + action, note, false, facade);
	}
	
	protected void setStatus(ClientProcess process, String status) 
	 throws DataException, ServiceException {
		
		Log.debug("Setting status for process: " 
		+ process.getProcessId() + ", last action: " + process.getLastAction());
		
		process.setStatus(status);
		process.persist(facade, null);
	}
	
	/** Sets the excluded status for this client process and
	 * adds a generic type/description
	 *  
	 * 
	 * @param process
	 * @param excluded
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void setExcludedAddActivity(ClientProcess process, boolean excluded, String note) 
	 throws DataException, ServiceException {
		
		Log.debug("Setting excluded=" + excluded + " for process: " 
		 + process.getProcessId());
		
		String curStatus = process.getStatus();
		
		if (excluded && !curStatus.equals(EXCLUDED_STATUS)) {
			setStatus(process, EXCLUDED_STATUS);
			
			process.addActivity(userName, null, "Exclusion",
			 "Client excluded from campaign", note, false, facade);
			
		} else if (curStatus.equals(EXCLUDED_STATUS)) {
			// TODO: map to corresponding initial status for the given
			// process type.
			setStatus(process, DEFAULT_INITIAL_STATUS);
			
			process.addActivity(userName, null, "Enrolment", 
			 "Client enrolled to campaign", note, false, facade);
		}
	}
	/** Sets the excluded status for this client process.
	 *  
	 *  
	 * 
	 * @param process
	 * @param excluded
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void setExcluded(ClientProcess process, boolean excluded, String note) 
	 throws DataException, ServiceException {
		
		Log.debug("Setting excluded=" + excluded + " for process: " 
		 + process.getProcessId());
		
		String curStatus = process.getStatus();
		
		if (excluded && !curStatus.equals(EXCLUDED_STATUS)) {
			setStatus(process, EXCLUDED_STATUS);			
			
		} else if (curStatus.equals(EXCLUDED_STATUS)) {
			// TODO: map to corresponding initial status for the given
			// process type.
			setStatus(process, DEFAULT_INITIAL_STATUS);			
		}
	}
	
	/** Returns a list of the available process actions, based on the
	 *  current process status.
	 *  
	 * @param process
	 * @return
	 */
	public Vector<String> getAvailableActions(ClientProcess process) {
		String currentStatus = process.getStatus();
		
		Vector<String> actionList = new Vector<String>();
		
		for (int i=0; i<statusActions.length; i++) {
			String checkStatus = statusActions[i][0];
			
			if (checkStatus.equals("*") || checkStatus.equals(currentStatus)) {
				for (int j=1; j<statusActions[i].length; j++)
					actionList.add(statusActions[i][j]);
			}
		}
		
		return actionList;
	}
	
	/** Returns a list of the available process actions as a single-column
	 *  ResultSet.
	 *  
	 * @param process
	 * @return
	 */
	public DataResultSet getAvailableActionsResultSet(ClientProcess process) {
		
		Vector<String> actionList = getAvailableActions(process);
		
		DataResultSet rsActions = new DataResultSet(new String[] {
			"ACTION"
		});
		
		
		for (int i=0; i<actionList.size(); i++) {
			Vector<String> v = new Vector<String>();
			v.add(actionList.get(i));
			
			rsActions.addRow(v);
		}

		return rsActions;
	}
	
	/** Checks if the passed action is a permitted transition for
	 *  the passed ClientProcess.
	 *  
	 * @param process
	 * @param action
	 * @return
	 */
	public boolean isValidAction(ClientProcess process, String action) {
		
		Vector<String> availActions = getAvailableActions(process);
		
		for (int i=0; i<availActions.size(); i++) {
			if (action.equals(availActions.get(i)))
				return true;
		}
		
		return false;
	}
	
	/** Cancels an existing process. This will delete all associated
	 *  activities and forms, as well as the process entry itself. 
	 * @throws DataException 
	 * @throws ServiceException */
	public void cancelProcess(ClientProcess process)
	 throws DataException, ServiceException {
		
		Log.debug("Deleting process: " + process.getProcessId());
		
		DataBinder binder = new DataBinder();
		binder.putLocal("PROCESS_ID", Integer.toString(process.getProcessId()));
		
		try {
			facade.beginTransaction();
			
			// Delete associated activities first
			facade.execute("qClientServices_DeleteActivitiesByProcessId", binder);
			
			// Deleting all process forms will auto-delete any entries in the
			// CCLA_FORM_ACCOUNT_MAP table.
			facade.execute("qClientServices_DeleteFormsByProcessId", binder);
			
			// Finally, delete the process entry itself
			facade.execute("qClientServices_DeleteProcess", binder);
			
			Log.debug("Sucessfully deleted process: " + process.getProcessId());
		
			facade.commitTransaction();
			
		} catch (Exception e) {
			facade.rollbackTransaction();
			String msg = "Failed to cancel process: " + e.getMessage();
		
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}
}
