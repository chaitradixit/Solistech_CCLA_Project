package com.ecs.ucm.ccla.data.campaign;

import java.util.Vector;

import com.ecs.utils.stellent.embedded.FWFacade;


import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

public interface IEnrolmentHandler {

	/**
	 * Add new enrolment
	 * @param organisationId
	 * @param personId
	 * @param initialStatus
	 * @param exclude
	 * @return
	 * @throws DataException
	 * @throws ServiceException
	 */
	public CampaignEnrolment addNewEnrolment(int organisationId, Integer personId, boolean exclude) 
	 	throws DataException, ServiceException; 
	
	
	/**
	 * Applies an action to the campaign enrolment and updates the status.
	 * @param enrolment
	 * @param action
	 * @param note
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void applyAction(CampaignEnrolment enrolment, CampaignEnrolmentAction action, String note) 
		throws DataException, ServiceException;

	/**
	 * Applies an custom action to the campaign enrolment and updates the status.
	 * @param enrolment
	 * @param actionId
	 * @param note
	 * @param extraParams
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void applyCustomAction(CampaignEnrolment enrolment, int actionId, String note, DataBinder extraParams) 
		throws DataException, ServiceException;
//	public void applyCustomAction(CampaignEnrolment enrolment, int actionId, String note) 
//	throws DataException, ServiceException;


	
	/**
	 * 
	 * @param enrolment
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void cancelEnrolment(CampaignEnrolment enrolment) 
		throws DataException, ServiceException; 
	
	/**
	 * Return a dataresult set of actions that can be applied.
	 * DataResultSet ENROLMENT_ACTION_ID, ENROLMENT_ACTION_NAME, ENROLMENT_ACTION_DESCRIPTION
	 * @param process
	 * @return
	 * @throws DataException
	 * @throws ServiceException 
	 */
	public DataResultSet getAvailableActionsResultSet(CampaignEnrolment enrolment)
		throws DataException, ServiceException; 
	
	/**
	 * Returns a list of campaign enrolmentAction
	 * @param process
	 * @return
	 * @throws DataException
	 * @throws ServiceException
	 */
	public Vector<CampaignEnrolmentAction> getAvailableActions(CampaignEnrolment enrolment)
	 	throws DataException, ServiceException; 
	
	/** For any campaigns that involve investment intentions, this method should be
	 *  implemented to allow intentions to be checked with respect to the campaign.
	 *  
	 *  For instance: if the total cash available to the campaign (or one of its 
	 *  enrolled clients) is limited, implementation of this method would capture
	 *  this fact and raise an exception.
	 *  
	 *  This is currently called in the validate() method of FundInvestmentIntention.
	 *  
	 * @param intention
	 * @return
	 */
	public void validateIntention(FundInvestmentIntention intention) 
	 throws DataException, ServiceException;
	
	/** Appends custom data for the Campaign Enrolment to the passed DataBinder.
	 * 
	 *  This should be used when building Enrolment Info screens to ensure any custom
	 *  data fields/ResultSets are present in the DataBinder.
	 * 
	 * @param enrolment
	 * @param binder
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void addEnrolmentDataToBinder(CampaignEnrolment enrolment, DataBinder binder) 
	 throws DataException, ServiceException;
}