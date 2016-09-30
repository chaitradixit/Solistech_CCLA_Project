package com.ecs.ucm.ccla.data;

import java.util.Date;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

public class Process implements Persistable {


	protected int processId;
	protected int campaignId;
	
	protected String status;
	protected Integer contactAddressId;
	
	protected Date started;
	protected Date lastAction;
	
	public Process(int processId, int campaignId,
					Integer contactAddressId, String status, 
					Date started, Date lastAction) {
		this.processId = processId;
		this.campaignId = campaignId;
		this.status = status;
		this.contactAddressId = contactAddressId;
		this.started = started;
		this.lastAction = lastAction;
	}

	public int getProcessId() {
		return processId;
	}

	public int getCampaignId() {
		return campaignId;
	}
	
	public void setProcessId(int processId) {
		this.processId = processId;
	}

	public void setCampaignId(int campaignId) {
		this.campaignId = campaignId;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public Date getStarted() {
		return started;
	}

	public void setStarted(Date started) {
		this.started = started;
	}

	public Date getLastAction() {
		return lastAction;
	}

	public void setLastAction(Date lastAction) {
		this.lastAction = lastAction;
	}
	
	public static DataResultSet getData(int processId, FWFacade facade)
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "PROCESS_ID", processId);
		
		DataResultSet rsProcess =
		 facade.createResultSet("qClientServices_GetProcess", binder);
		
		return rsProcess;
	}
	
	/** Returns a list of all processes tied to the given Organisation ID.
	 * 
	 * @param orgId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getOrganisationProcessData
	 (int orgId, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "ORGANISATION_ID", orgId);
		
		DataResultSet rsProcess =
		 facade.createResultSet("qClientServices_GetClientProcesses", binder);
		
		return rsProcess;
	}
	
	public static DataResultSet getActivityData(String processId, FWFacade facade)
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		binder.putLocal("PROCESS_ID", processId);
		
		DataResultSet rsProcessActivities =
		 facade.createResultSet("qClientServices_GetActivitiesByProcessId", binder);
		
		return rsProcessActivities;
	}
	
	/** Returns a set of Form instances which map to the given process. 
	 * @throws DataException 
	 * @throws ServiceException */
	public static UCMForm[] getForms(Process process, FWFacade facade) 
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
	
	/** Returns a set of entries from the CCLA_FORMS table which map to
	 *  the given process.
	 *  
	 * @param processId
	 * @param facade
	 * @return
	 * @throws DataException 
	 * @throws DataException 
	 */
	public static DataResultSet getFormData(Process process, FWFacade facade) 
     throws DataException {
		
		DataBinder binder = new DataBinder();
		binder.putLocal("PROCESS_ID", Integer.toString(process.getProcessId()));
		
		DataResultSet rsProcessForms =
		 facade.createResultSet("qClientServices_GetFormsByProcessId", binder);
		
		return rsProcessForms;
	}
	
	/** Returns a set of the latest Form instances which map to the given 
	 *  process. 
	 *  
	 *  'Latest' implies the most recently created form entries with distinct 
	 *  BASE_DOCNAME values.
	 *  
	 * @throws DataException 
	 * @throws ServiceException */
	public static UCMForm[] getLatestForms(Process process, FWFacade facade) 
	 throws DataException, ServiceException {
		DataResultSet rsProcessForms = getLatestFormData(process, facade);
		
		if (rsProcessForms == null || rsProcessForms.isEmpty())
			return null;
		
		UCMForm[] forms = new UCMForm[rsProcessForms.getNumRows()];
		
		do {
			forms[rsProcessForms.getCurrentRow()] = UCMForm.get(rsProcessForms);
		} while (rsProcessForms.next());
		
		return forms;
	}
	
	/** Returns a set of entries from the CCLA_FORMS table which map to
	 *  the given process.
	 *  
	 * @param processId
	 * @param facade
	 * @return
	 * @throws DataException 
	 * @throws DataException 
	 */
	public static DataResultSet getLatestFormData(Process process, FWFacade facade) 
     throws DataException {
		
		DataBinder binder = new DataBinder();
		binder.putLocal("PROCESS_ID", Integer.toString(process.getProcessId()));
		
		DataResultSet rsProcessForms =
		 facade.createResultSet("qClientServices_GetLatestFormsByProcessId", binder);
		
		return rsProcessForms;
	}

	public void addFieldsToBinder(DataBinder binder) {
		CCLAUtils.addQueryIntParamToBinder(
		 binder, "PROCESS_ID", this.getProcessId());
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "CAMPAIGN_ID", campaignId);
		
		CCLAUtils.addQueryParamToBinder
		 (binder, "PROCESS_STATUS", this.getStatus());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "CONTACT_ADDRESS_ID", this.getContactAddressId());
		CCLAUtils.addQueryDateParamToBinder
		 (binder, "LAST_ACTION", lastAction);
	
		CCLAUtils.addQueryIntParamToBinder(binder, "PERSON_ID", null);	
		CCLAUtils.addQueryIntParamToBinder(binder, "ORGANISATION_ID", null);
	}
	
	public void persist(FWFacade facade, String username) throws DataException {
		throw new DataException("Can't persist a base Process instance. " +
		 "You must persist one of its subclass instances instead.");
	}
	
	public void setAttributes(DataBinder binder) throws DataException {
		this.setStatus(binder.getLocal("PROCESS_STATUS"));
		this.setContactAddressId(BinderUtils.getBinderIntegerValue(binder, "CONTACT_ADDRESS_ID"));
	}

	public void validate(FWFacade facade) throws DataException {
		if (this.getStatus() == null)
			throw new DataException("Process status missing");
	}

	public Integer getContactAddressId() {
		return contactAddressId;
	}

	public void setContactAddressId(Integer contactAddressId) {
		this.contactAddressId = contactAddressId;
	}


}
