package com.ecs.stellent.ccla.clientservices.campaign;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.text.MessageFormat;
import java.util.Vector;

import com.ecs.ucm.ccla.data.campaign.Campaign;
import com.ecs.ucm.ccla.data.campaign.CampaignActivityType;
import com.ecs.ucm.ccla.data.campaign.CampaignEnrolment;
import com.ecs.ucm.ccla.data.campaign.CampaignEnrolmentAction;
import com.ecs.ucm.ccla.data.campaign.CampaignEnrolmentStatus;
import com.ecs.ucm.ccla.data.campaign.CampaignMessages;
import com.ecs.ucm.ccla.data.campaign.CampaignSubjectStatus;
import com.ecs.ucm.ccla.data.campaign.EnrolmentStatusActionApplied;
import com.ecs.ucm.ccla.data.campaign.IEnrolmentHandler;
import com.ecs.ucm.ccla.data.campaign.EnrolmentStatusActionApplied.EnrolmentStatusMap;
import com.ecs.utils.Log;

import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

public abstract class BaseEnrolmentHandler implements IEnrolmentHandler {

	public static final String QUERY_DELETE_CAMPAIGN_ENROLMENT_BY_ID 			= "";
	public static final String QUERY_DELETE_CAMPAIGN_ACTIVITY_BY_ENROLMENT_ID 	= "";
	public static final String QUERY_DELETE_FORM_BY_ENROLMENT_ID = "";
	
	public static String[] AVAIL_ACTIONS_RESULTSET_COLS = new String[] {
		CampaignEnrolmentAction.ID, 
		CampaignEnrolmentAction.NAME, 
		CampaignEnrolmentAction.DESCRIPTION
	};
	
	Campaign campaign = null;
	CampaignEnrolment enrolment = null;
	FWFacade facade = null;
	String username = null;
	
	/**
	 * Define the constructor that must be used.
	 */
	public BaseEnrolmentHandler(Campaign campaign, String username, FWFacade facade) {
		this.campaign = campaign;
		this.username = username;
		this.facade = facade;
	}
	

	public void cancelEnrolment(CampaignEnrolment enrolment)
			throws DataException, ServiceException 
	{
		Log.debug("Deleting CampaignEnrolment: " + enrolment.getCampaignEnrolmentId());
		
		DataBinder binder = new DataBinder();
		
		BinderUtils.addIntParamToBinder(binder, "CAMPAIGN_ENROLMENT_ID", 
		 enrolment.getCampaignEnrolmentId());
		
		try {
			facade.beginTransaction();
			
			//TODO delete notes???
			
			// Delete associated activities first
			facade.execute(QUERY_DELETE_CAMPAIGN_ACTIVITY_BY_ENROLMENT_ID, binder);
			
			// Deleting all forms
			facade.execute(QUERY_DELETE_FORM_BY_ENROLMENT_ID, binder);
			
			// Finally, delete the enrolment entry itself
			facade.execute(QUERY_DELETE_CAMPAIGN_ENROLMENT_BY_ID, binder);
			
			Log.debug("Sucessfully deleted CampaignEnrolment: " + 
			 enrolment.getCampaignEnrolmentId());
		
			facade.commitTransaction();
			
		} catch (Exception e) {
			facade.rollbackTransaction();
			String msg = "Failed to cancel CampaignEnrolment: " + e.getMessage();
		
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}		

	
	public DataResultSet getAvailableActionsResultSet(CampaignEnrolment enrolment) 
		throws DataException, ServiceException 
	{
		EnrolmentStatusMap statusMap = EnrolmentStatusActionApplied.getCache().
		 getCachedInstance(campaign.getCampaignId());

		Vector<Integer> actions = null;
		if (statusMap!=null) {
			actions = statusMap.getActions(enrolment.getEnrolmentStatusId());
		}
		
		DataResultSet rsActions = new DataResultSet(AVAIL_ACTIONS_RESULTSET_COLS);

		if (actions==null || actions.isEmpty())
			return rsActions;
		
		CampaignEnrolmentAction enrolmentAction = null;
		for (Integer actionId: actions)
		{
			if (actionId!=null && actionId.intValue()!=0) 
			{
				enrolmentAction = CampaignEnrolmentAction.getCache().
				 getCachedInstance(actionId);
				
				if (enrolmentAction!=null) 
				{
					Vector<String> v = new Vector<String>();
					v.add(String.valueOf(enrolmentAction.getActionId()));
					v.add(enrolmentAction.getName());
					v.add(enrolmentAction.getDescription());
					rsActions.addRow(v);
				}
			}
		}
		
		return rsActions;
	}

	public Vector<CampaignEnrolmentAction> getAvailableActions
	 (CampaignEnrolment enrolment) throws DataException, ServiceException {
		
		EnrolmentStatusMap statusMap = EnrolmentStatusActionApplied.getCache().
		 getCachedInstance(campaign.getCampaignId());

		Vector<CampaignEnrolmentAction> enrolmentActions = 
		 new Vector<CampaignEnrolmentAction>();
		
		Vector<Integer> actions = null;
		
		if (statusMap!=null) {
			actions = statusMap.getActions(enrolment.getEnrolmentStatusId());
		}
		
		if (actions!=null && !actions.isEmpty()) 
		{
			CampaignEnrolmentAction enrolmentAction = null;
			for (Integer actionId: actions)
			{
				if (actionId!=null && actionId.intValue()!=0) 
				{
					enrolmentAction = CampaignEnrolmentAction.getCache().
					 getCachedInstance(actionId);
					
					if (enrolmentAction!=null) 
						enrolmentActions.add(enrolmentAction);
				}
			}
		}
		
		return enrolmentActions;
	}
	
	/**
	 * Adds an note and activity to the database.
	 * @param enrolment
	 * @param PersonId
	 * @param activityTypeId
	 * @param activityDesc
	 * @param username
	 */
	protected void addEnrolmentActivity(CampaignEnrolment enrolment, 
			Integer personId, Integer activityTypeId, String activityDesc, 
			String username, String noteMsg) throws DataException {
		
		CampaignEnrolment.addActivity(enrolment, personId, activityTypeId, 
		 activityDesc, noteMsg, facade, username);
	}
	
	/**
	 * 
	 * @param message
	 * @param params
	 */
	protected static String getMessage(String message, Object[] params) {
		return MessageFormat.format(message, params);
	}


	public CampaignEnrolment addNewEnrolment(int organisationId,
			Integer personId, boolean exclude) throws DataException,
			ServiceException {
		
		try {
			facade.beginTransaction();
			
			//Add an entry to the CampaignEnrolment table
			int enrolmentStatusId = campaign.getDefaultEnrolmentStatusId();
			int activityTypeId = CampaignActivityType.ENROLMENT_ACTIVITY_ID;
			String activityMessage = getMessage
			 (CampaignMessages.ENROL_MSG, new Object[] {campaign.getName()});
			
			
			if (exclude) {
				enrolmentStatusId = CampaignEnrolmentStatus.EXCLUDE_STATUS;
				activityTypeId = CampaignActivityType.EXCLUSION_ACTIVITY_ID;
				activityMessage = getMessage
				 (CampaignMessages.EXCLUDE_MSG, new Object[] {campaign.getName()});
			}
				
			
			Log.debug("campaign:"+campaign.getCampaignId()+", enrolmentStatusId:"
			 +enrolmentStatusId+", activityTypeId:"+activityTypeId);
			
			CampaignEnrolment enrol = 
				new CampaignEnrolment(
						0, campaign.getCampaignId(), personId, 
						organisationId, enrolmentStatusId, 
						null, null, null, username, 
						CampaignSubjectStatus.UNDECIDED_STATUS_ID);
			
			enrol = CampaignEnrolment.add(enrol, facade, username);
		
			//Add an entry to the CampaignActivity table
			this.addEnrolmentActivity(enrol, personId, activityTypeId, 
			 activityMessage, username, null);
			
			facade.commitTransaction();
			return enrol;
		} catch (Exception e) {
			facade.rollbackTransaction();
			throw new ServiceException(e);
		}
	}
}
