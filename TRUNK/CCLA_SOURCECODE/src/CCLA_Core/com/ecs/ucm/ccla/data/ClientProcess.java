package com.ecs.ucm.ccla.data;

import java.util.Date;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.DataResultSetUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

public class ClientProcess extends Process implements Persistable {

	private Integer organisationId;
	private Integer personId;
	
	public ClientProcess(int processId, int campaignId, 
			Integer contactAddressId, String status,
			Integer orgId, Integer personId, Date started, Date lastAction) {
		
		super(processId, campaignId, contactAddressId, 
			  status, started, lastAction);
		this.organisationId = orgId;
		this.personId = personId;
	}
	
	/** Adds a new Process to the database and returns the new instance. */
	public static ClientProcess add(int campaignId, String status,
	 Integer orgId, String userName, Date started, 
	 Integer personId, FWFacade facade) throws DataException {
		
		// Fetch new ID for this activity.
		String processId = CCLAUtils.getNewKey("Process", facade);
		
		ClientProcess process = new ClientProcess(Integer.parseInt(processId), 
		 campaignId, null, status, orgId, personId, started, started);
		
		DataBinder binder = new DataBinder();
		process.addFieldsToBinder(binder);
		
		facade.execute("qClientServices_AddProcess", binder);
		return process;
	}
	
	public void addFieldsToBinder(DataBinder binder) {
		super.addFieldsToBinder(binder);

		CCLAUtils.addQueryIntParamToBinder
		 (binder, "ORGANISATION_ID", this.getOrganisationId());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "PERSON_ID", this.getPersonId());
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
	public static ClientProcess get(DataResultSet rsProcess) 
	 throws DataException {
		
		if (rsProcess.isEmpty())
			return null;
		
		ClientProcess process = new ClientProcess(
			Integer.parseInt(rsProcess.getStringValueByName("PROCESS_ID")),
			Integer.parseInt(rsProcess.getStringValueByName("CAMPAIGN_ID")),
			DataResultSetUtils.getResultSetIntegerValue(rsProcess, "CONTACT_ADDRESS_ID"),
			rsProcess.getStringValueByName("PROCESS_STATUS"),
			CCLAUtils.getResultSetIntegerValue
			 (rsProcess, "ORGANISATION_ID"),
				CCLAUtils.getResultSetIntegerValue
				 (rsProcess, "PERSON_ID"),			 
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
	public static ClientProcess get(Integer processId, FWFacade facade) 
	 throws DataException, ServiceException {
		
		DataResultSet rsProcess = getData(processId, facade);
		
		if (rsProcess == null || rsProcess.isEmpty())
			return null;
		
		ClientProcess process = get(rsProcess);
		return process;
	}
	
	/** Attempts to find an existing Client Process with the given Organisation
	 *  ID and Campaign ID.
	 *  
	 *  This is generally used to determine whether a client is already
	 *  enrolled to a particular campaign.
	 *  
	 * @param orgId
	 * @param campaignId
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static ClientProcess get
	 (int orgId, int campaignId, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "ORGANISATION_ID", orgId);
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "CAMPAIGN_ID", campaignId);
		
		DataResultSet rsProcess = facade.createResultSet
		 ("qClientServices_GetClientProcessByValues", binder);
		
		return get(rsProcess);
	}
	
	public static DataResultSet getData(Integer processId, FWFacade facade)
	 throws DataException {

		return Process.getData(processId, facade);
	}
	
	public static DataResultSet getActivityData(String processId, FWFacade facade)
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		binder.putLocal("PROCESS_ID", processId);
		
		DataResultSet rsProcessActivities = facade.createResultSet
		 ("qClientServices_GetActivitiesByProcessId", binder);
		
		return rsProcessActivities;
	}
	
	/** Returns a set of Form instances which map to the given process. 
	 * @throws DataException 
	 * @throws ServiceException */
	public static UCMForm[] getForms(ClientProcess process, FWFacade facade) 
	 throws DataException, ServiceException {
		DataResultSet rsProcessForms = getFormData(process, facade);
		
		if (rsProcessForms == null || rsProcessForms.isEmpty())
			return null;
		
		UCMForm[] forms = new UCMForm[rsProcessForms.getNumRows()];
		
		do {
			forms[rsProcessForms.getCurrentRow()] = UCMForm.get(rsProcessForms);
		} while (rsProcessForms.next());
		
		return forms;
	}
	
	public void setAttributes(DataBinder binder) throws DataException {
		super.setAttributes(binder);
	}
	
	/** Adds a new activity specific to this process.
	 * 
	 *  Returns the generated activity ID.
	 * 
	 * @param userId
	 * @param personId
	 * @param activityType
	 * @param activityAction
	 * @param note
	 * @param isError
	 * @param facade
	 * @throws DataException
	 * @throws ServiceException
	 */
	public int addActivity(String userId, Integer personId,
	 String activityType, String activityAction, 
	 String note, boolean isError, 
	 FWFacade facade) 
	
	 throws DataException, ServiceException {
		
		Activity activity = CCLAUtils.addActivity(
		 this.getProcessId(), this.getPersonId(), null, this.getOrganisationId(), 
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
	
	public Date getStarted() {
		return started;
	}

	public Date getLastAction() {
		return lastAction;
	}

	public void setStarted(Date started) {
		this.started = started;
	}

	public void setLastAction(Date lastAction) {
		this.lastAction = lastAction;
	}

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer orgId) {
		this.organisationId = orgId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

	public Integer getPersonId() {
		return personId;
	}
}