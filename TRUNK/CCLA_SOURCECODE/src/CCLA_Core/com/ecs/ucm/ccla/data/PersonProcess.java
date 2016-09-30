package com.ecs.ucm.ccla.data;

import java.util.Date;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.utils.stellent.DateFormatter;
import com.ecs.utils.stellent.embedded.FWFacade;

public class PersonProcess extends Process {
	
	private int personId;
	
	public PersonProcess(int processId, int campaignId, 
			Integer contactAddressId, String status,
			int personId, Date started, Date lastAction) {

		super(processId, campaignId, contactAddressId, 
			  status, started, lastAction);
		
		this.personId = personId;
	}
	
	/** Adds a new Process to the database and returns the new instance. */
	public static PersonProcess add(int campaignId, String status,
	 String userName, int personId, Date started, FWFacade facade) 
	 throws DataException {
		
		// Fetch new ID for this activity.
		String processId = CCLAUtils.getNewKey("Process", facade);
		
		PersonProcess process = new PersonProcess(Integer.parseInt(processId),  
		 campaignId, null, status, personId, started, started);
		
		DataBinder binder = new DataBinder();
		process.addFieldsToBinder(binder);
		
		facade.execute("qClientServices_AddProcess", binder);
		
		return process;
	}
	
	public void addFieldsToBinder(DataBinder binder) {
		super.addFieldsToBinder(binder);
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "PERSON_ID", personId);	
	}
	
	/** Instantiates a ClientProcess instance from the passed ResultSet.
	 *  The ResultSet is expected to have column names which match those
	 *  used in the CCLA_PROCESSES table.
	 *  
	 * @param rsProcess
	 * @return
	 * @throws ServiceException 
	 * @throws DataException 
	 * @throws NumberFormatException 
	 * @throws NumberFormatException 
	 */
	public static PersonProcess get(DataResultSet rsProcess) 
	 throws DataException {
		
		if (rsProcess == null || rsProcess.isEmpty())
			return null;
		
		PersonProcess process = new PersonProcess(
			Integer.parseInt(rsProcess.getStringValueByName("PROCESS_ID")),
			Integer.parseInt(rsProcess.getStringValueByName("CAMPAIGN_ID")),
			Integer.parseInt(rsProcess.getStringValueByName("CONTACT_ADDRESS_ID")),
			rsProcess.getStringValueByName("PROCESS_STATUS"),
			CCLAUtils.getResultSetIntegerValue(rsProcess, "PERSON_ID"),
			rsProcess.getDateValueByName("PROCESS_DATE"),
			rsProcess.getDateValueByName("LAST_ACTION")
		);
		
		return process;
	}
	
	/** Instantiates a ClientProcess instance using data which corresponds to
	 *  the passed processId.
	 *  
	 * @param processId
	 * @param facade
	 * @return
	 * @throws DataException
	 * @throws ServiceException 
	 */
	public static PersonProcess get(Integer processId, FWFacade facade) 
	 throws DataException, ServiceException {
		
		DataResultSet rsProcess = getData(processId, facade);
		
		if (rsProcess == null || rsProcess.isEmpty())
			return null;
		
		PersonProcess process = get(rsProcess);
		return process;
	}
	
	/** Adds a new activity specific to this process.
	 * 
	 *  Returns the generated activity ID.
	 * 
	 * @param userId
	 * @param activityType
	 * @param activityAction
	 * @param activityOutcome
	 * @param note
	 * @param isComm
	 * @param confirmTypes
	 * @param confirmOther
	 * @param facade
	 * @throws DataException
	 * @throws ServiceException
	 */
	public int addActivity(String userId, Integer clientId,
	 String activityType, String activityAction, 
	 String note, boolean isError, 
	 FWFacade facade) 
	
	 throws DataException, ServiceException {
		
		Activity activity = CCLAUtils.addActivity(
		 this.getProcessId(), this.getPersonId(), null, clientId, 
		 userId, activityType, activityAction, note, isError, facade);
		
		// Update process last action time.
		this.setLastAction(new Date());
		this.persist(facade, null);
		
		return activity.getActivityId();
	}
	
	public void persist(FWFacade facade, String username) throws DataException {
		
		DataBinder binder = new DataBinder();
		addFieldsToBinder(binder);
		
		facade.execute("qClientServices_UpdateProcess", binder);
	}
	
	public Integer getPersonId() {
		return personId;
	}
	
	public void setPersonId(Integer personId) {
		this.personId = personId;
	}
	
	public String getStatus() {
		return status;
	}

	public Date getStarted() {
		return started;
	}

	public Date getLastAction() {
		return lastAction;
	}

	public void setCampaignId(int campaignId) {
		this.campaignId = campaignId;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setStarted(Date started) {
		this.started = started;
	}

	public void setLastAction(Date lastAction) {
		this.lastAction = lastAction;
	}
}