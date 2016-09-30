package com.ecs.stellent.spp.service;

import java.util.Vector;

import com.ecs.stellent.iris.batch.BatchDocumentServices;
import com.ecs.stellent.spp.data.JobPriorityRule;
import com.ecs.stellent.spp.data.JobPriorityRuleValue;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.data.Workspace;
import intradoc.server.Service;
import intradoc.shared.SharedObjects;

public class BundlePriorityServices extends Service {
	
	//Mandate Boost Rule
	public static final String BOOST_RULE_NAME = 
		SharedObjects.getEnvironmentValue("SPP_RULES_BOOST_NAME");
	
	public static final int BOOST_RULE_PRIORITY = 
		SharedObjects.getEnvironmentInt("SPP_RULES_BOOST_PRIORITY", 50);

	public static final boolean BOOST_RULE_ENABLED =
		SharedObjects.getEnvValueAsBoolean("SPP_RULES_BOOST_ON_QUERY_MANDATE_BACK_LOG", false);

	public static final boolean BOOST_RULE_APPEND =
		SharedObjects.getEnvValueAsBoolean("SPP_RULES_BOOST_APPEND", false);

	
	public static final boolean BOOST_RULE_RELEASE_PARKING_LOT =
		SharedObjects.getEnvValueAsBoolean("SPP_RULES_BOOST_RELEASE_FROM_PARKING_LOT", false);
	
	
	public static final String BOOST_RULE_APPLIES_TO = 
		SharedObjects.getEnvironmentValue("SPP_RULES_BOOST_APPLIES_TO_BUNDLE_STATUS_VALUES");
	

	public static final String BOOST_RULE_NUM_VALID_DAYS = 
		SharedObjects.getEnvironmentValue("SPP_RULES_BOOST_NUM_VALID_DAYS");
	

	//Unflagged Boost Rule
	public static final String UNFLAGGED_BOOST_RULE_APPLIES_TO = 
		SharedObjects.getEnvironmentValue("SPP_UNFLAGGED_RULE_BOOST_APPLIES_TO");
	
	public static final boolean UNFLAGGED_BOOST_RULE_ENABLED =
		SharedObjects.getEnvValueAsBoolean("SPP_UNFLAGGED_RULE_BOOST_ENABLED", false);
	
	public static final boolean UNFLAGGED_BOOST_RULE_APPEND =
		SharedObjects.getEnvValueAsBoolean("SPP_UNFLAGGED_RULE_BOOST_APPEND", false);
	
	
	public static final String UNFLAGGED_BOOST_RULE_NAME = 
		SharedObjects.getEnvironmentValue("SPP_UNFLAGGED_RULE_BOOST_NAME");
	
	public static final int UNFLAGGED_BOOST_RULE_PRIORITY = 
		SharedObjects.getEnvironmentInt("SPP_UNFLAGGED_RULE_BOOST_PRIORITY", 50);
	
	public static final String UNFLAGGED_BOOST_RULE_NUM_VALID_DAYS = 
		SharedObjects.getEnvironmentValue("SPP_UNFLAGGED_RULE_BOOST_NUM_VALID_DAYS");
	
	
	
	/** Adds a new Job Priority Rule.
	 * 
	 * @throws ServiceException
	 * @throws DataException
	 */
	public void addRule() throws ServiceException, DataException {
	
		String ruleName 	= m_binder.getLocal("RULE_NAME");
		String ruleField 	= m_binder.getLocal("RULE_FIELD");
		String ruleTypeStr	= m_binder.getLocal("RULE_TYPE");
		String ruleOrderStr	= m_binder.getLocal("RULE_ORDER");
		boolean isAppendPriority = CCLAUtils.getBinderBoolValueAllowNull(m_binder, "IS_APPEND_PRIORITY");
		
		if (StringUtils.stringIsBlank(ruleName)
			||
			StringUtils.stringIsBlank(ruleField)) {
			throw new ServiceException("Unable to add Rule: " +
			 "Rule Name/Field was missing.");
		}
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace,false);
		int ruleOrder	= Integer.parseInt(ruleOrderStr);
		
		JobPriorityRule.RuleType ruleType = 
		 JobPriorityRule.RuleType.valueOf(ruleTypeStr);
		
		JobPriorityRule.add(ruleName, ruleField, ruleType, ruleOrder, isAppendPriority, facade);
		
		// Refresh the rules cache.
		BundlePriorityUtils.refreshJobPriorityRuleCache(facade);
	}
	
	/** Updates an existing Job Priority Rule.
	 * 
	 * @throws DataException 
	 * @throws ServiceException 
	 * 
	 */
	public void updateRule() throws DataException, ServiceException {
		String ruleIdStr  = m_binder.getLocal("RULE_ID");
		int ruleId		= Integer.parseInt(ruleIdStr);

		// Fetch the existing Rule from cache.
		Vector<JobPriorityRule> rules = 
		 BundlePriorityUtils.getJobPriorityRules();

		for (JobPriorityRule rule: rules) {
			if (rule.getRuleId() == ruleId) {
				
				FWFacade facade = CCLAUtils.getFacade(m_workspace,false);
				
				rule.setAttributes(m_binder);
				rule.persist(facade);
				
				// Refresh the rules cache.
				BundlePriorityUtils.refreshJobPriorityRuleCache(facade);
				
				break;
			}
		}
	}
	
	/** Adds a new Rule Value. 
	 * 
	 * @throws ServiceException 
	 * @throws DataException 
	 *
	 **/
	public void addRuleValue() throws ServiceException, DataException {
		
		Integer ruleId = 
			CCLAUtils.getBinderIntegerValue(m_binder, JobPriorityRuleValue.RULE_ID);
		
		String fieldValue 	= m_binder.getLocal(JobPriorityRuleValue.FIELD_VALUE);
		
		Integer priority = 
			CCLAUtils.getBinderIntegerValue(m_binder, JobPriorityRuleValue.PRIORITY);
		
		Integer numDaysValid = 
			CCLAUtils.getBinderIntegerValue(m_binder, JobPriorityRuleValue.NUM_VALID_DAYS);
		
		
		if (StringUtils.stringIsBlank(fieldValue) || priority==null) {
			throw new ServiceException("Unable to add Rule Value: " +
			 "Rule ID/Field Value/Priority was missing.");
		}
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace,false);
		
		JobPriorityRuleValue.add(ruleId, fieldValue, priority, numDaysValid, facade);
		
		// Refresh the rules cache.
		BundlePriorityUtils.refreshJobPriorityRuleCache(facade);
	}
	
	/** 
	 * Updates a set of Rule Values, relating to a single Rule ID. 
	 * @throws ServiceException 
	 * @throws DataException 
	 * 
	 */
	public void updateRuleValues() throws ServiceException, DataException 
	{
		Integer ruleId = 
			CCLAUtils.getBinderIntegerValue(m_binder, JobPriorityRuleValue.RULE_ID);
		
		if (ruleId==null)
			throw new ServiceException("Cannot update rule values, Missing RULE_ID");
		
		// Fetch set of existing Rule Values for this Rule.
		Vector<JobPriorityRule> rules = 
		 BundlePriorityUtils.getJobPriorityRules();
		
		Vector<JobPriorityRuleValue> ruleValues = null;
		
		for (JobPriorityRule rule: rules) {
			if (rule.getRuleId() == ruleId) {
				
				ruleValues = rule.getRuleValues();
				break;
			}
		}
		
		if (ruleValues == null)
			return;

		FWFacade facade = CCLAUtils.getFacade(m_workspace,false);
		
		try {
			facade.beginTransaction();
			
			boolean doUpdate = false;
			
			for (JobPriorityRuleValue ruleValue: ruleValues) 
			{
				doUpdate = false; //reset the update flag.
				
				Integer priority = 
					CCLAUtils.getBinderIntegerValue(m_binder, "PRIORITY_"+ruleValue.getRuleValueId());
				
				Integer numValidDays = 
					CCLAUtils.getBinderIntegerValue(m_binder, "NUMVALIDDAYS_"+ruleValue.getRuleValueId());
				
				
				if (priority==null) {
					Log.error("Unable to update Rule Values: priority for " +
					 "RULE_VALUE_ID " + ruleValue.getRuleValueId() + 
					 " missing");
					
					throw new ServiceException("Unable to update Rule " +
					 "Values: at least one Priority value was missing.");
				}
				
				if (priority != ruleValue.getPriority()) {
					ruleValue.setPriority(priority);
					doUpdate = true;
				}

				if (numValidDays!=ruleValue.getNumValidDays()) {
					ruleValue.setNumValidDays(numValidDays);
					doUpdate = true;					
				}
				
				if (doUpdate)
					ruleValue.persist(facade);
			}
			
			facade.commitTransaction();
		
			// Refresh the rules cache.
			BundlePriorityUtils.refreshJobPriorityRuleCache(facade);
			
		} catch (Exception e) {
			facade.rollbackTransaction();
			
			Log.error("Failed to update Rule Values: " + e.getMessage(), e);
			throw new ServiceException(e);
		}
	}
	
	/** Removes a Rule Value with the given ID.
	 * 
	 * @throws DataException 
	 * @throws ServiceException 
	 * 
	 */
	public void removeRuleValue() throws DataException, ServiceException 
	{
		Integer ruleValueId	= 
			CCLAUtils.getBinderIntegerValue(m_binder, JobPriorityRuleValue.RULE_VALUE_ID);
		
		if (ruleValueId==null) {
			throw new ServiceException("Missing RULE_VALUE_ID");
		}
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace,false);
		JobPriorityRuleValue.remove(ruleValueId, facade);
		// Refresh the rules cache.
		BundlePriorityUtils.refreshJobPriorityRuleCache(facade);
	}
	
	/** 
	 * Updates the priority value for every pending bundle.
	 *  
	 */
	public void refreshAllBundlePriorities() throws DataException
	{
		Log.debug("START: Refresh all bundle priorities");
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace,false);

		DataResultSet rsAllBundles =
			 facade.createResultSet("QGetAllBundleDocuments", new DataBinder());

		int updateCount = 0;
		int totalCount = 0;
		
		BundlePriorityServices.updatePriorityForDataResultSet
		 (rsAllBundles, updateCount, totalCount, "dDocName" , "xBundleRef", facade);
		
		m_binder.putLocal("IsRefreshComplete", "1");
		m_binder.putLocal("TotalNumPrioritiesToRefresh", String.valueOf(totalCount));
		m_binder.putLocal("PrioritiesRefreshed", String.valueOf(updateCount));

		Log.debug("END: Refresh all bundle priorities");			
	}	
	
	/**
	 * Add boost rule
	 * @param clientNumber
	 * @param workspace
	 * @return
	 * @throws ServiceException
	 * @throws DataException
	 */
	public static boolean addBoostRule(String clientNumber, Workspace workspace) 
	throws ServiceException, DataException 
	{
		
		boolean success = false;
		
		FWFacade facade = CCLAUtils.getFacade(workspace,false);
		DataBinder binder = null;
		
		Log.debug("--START of addBoostRule : clientNumer:"+clientNumber);
		
		
		//0. Check Client number first:
		if (StringUtils.stringIsBlank(clientNumber)) {
			Log.error("Not boosting client mandates, client number is blank");
			return success;
		}
		
		//1. Check if rule exist first, otherwise add it
		Vector<JobPriorityRule> vec = BundlePriorityUtils.getJobPriorityRules();
		JobPriorityRule rule = null;
		
		if (vec!=null) {
			for (int i=0; i<vec.size();i++) {
				rule = (JobPriorityRule)vec.get(i);
				if (rule.getName().equals(BOOST_RULE_NAME)) {
					break;
				} else {
					rule = null;
				}
			}
		} 
		
		// rule is empty, add new priority rule
		if (rule==null) 
		{
			JobPriorityRule.add(BOOST_RULE_NAME, "xClientNumber", 
					JobPriorityRule.RuleType.ItemData, 1, "1", BOOST_RULE_APPLIES_TO, 
					BOOST_RULE_APPEND, facade);

			binder =  new DataBinder();
			binder.putLocal("ruleName", BOOST_RULE_NAME);
			DataResultSet rsJobID =
				 facade.createResultSet("QGetJobPriorityRuleByName", binder);
			
			if (rsJobID==null)
				throw new DataException("Cannot create a new priority boost rule");
			else 
				rule = JobPriorityRule.get(rsJobID);				
		} 

		// 2. Check for values
		if (rule!=null) 
		{
			Vector<JobPriorityRuleValue> ruleValues = rule.getRuleValues();
			
			JobPriorityRuleValue ruleValue = null;
			
			if (ruleValues!=null) {				
				
				for (int i=0; i<ruleValues.size(); i++) 
				{
					ruleValue = ruleValues.get(i);
					if (ruleValue.getFieldValue().equals(clientNumber))
						break;
					else 
						ruleValue = null;					
				}
			}
			
			if (ruleValue==null) {
				
				Integer numValidDays = null;
				
				if (!StringUtils.stringIsBlank(BOOST_RULE_NUM_VALID_DAYS)){
					try {
						numValidDays = Integer.parseInt(BOOST_RULE_NUM_VALID_DAYS);
					} catch (NumberFormatException nfe) {
						Log.error("cannot convert the number of valid days "+BOOST_RULE_NUM_VALID_DAYS);						
					}
				}
				
				JobPriorityRuleValue.add(rule.getRuleId(), clientNumber, BOOST_RULE_PRIORITY, numValidDays, facade);
				Log.debug("--addBoostRule : adding ruleValue priority:"+BOOST_RULE_PRIORITY);
			} else {
				Log.debug("--addBoostRule : using existing ruleValue priority:"+ruleValue.getPriority());
			}
//			else {
//				ruleValue.setPriority(BOOST_RULE_PRIORITY);
//				ruleValue.persist(facade);
//			}
		}
		facade.commitTransaction();
		
		// 3. Refresh cache and re-evaluate priorities for client
		BundlePriorityUtils.refreshJobPriorityRuleCache(facade);

		Log.debug("--END of addBoostRule : clientNumer:"+clientNumber);
		return true;
	}
	
	/**
	 * Applies the refresh priority refresh for a particular clientnumber only and release their document from the parking lot.
	 * @param clientNumber
	 * @param username
	 * @param workspace
	 * @throws DataException
	 * @throws ServiceException
	 */
	public static void refreshBoostForClientQuery(String clientNumber, String username, Workspace workspace) throws DataException, ServiceException {
		
		FWFacade facade = CCLAUtils.getFacade(workspace,false);
		
		// 1. Reset any parking lot
		if (BOOST_RULE_RELEASE_PARKING_LOT)
			BundleServices.resetParkingLotBundles("100", clientNumber, false,  username, facade);
		
		// 2. Calculate bundles priorities
		DataBinder binder = new DataBinder();
			binder.putLocal("clientNumber", clientNumber);
			
		DataResultSet rsUniqueDocname =
			facade.createResultSet("qGetUniqueBundleDocnameForPendingValidationDocByClientNum", binder);

		int updateCount = 0;
		int totalCount = 0;
		
		BundlePriorityServices.updatePriorityForDataResultSet(rsUniqueDocname, updateCount, totalCount, "BUNDLE_DOCNAME" , "xBundleRef", facade);
		
	}
	
	
	/**
	 * Common method for updating bundles
	 * @param resultSet
	 * @param updateCount
	 * @param totalCount
	 * @param facade
	 * @return
	 */
	public static void updatePriorityForDataResultSet(DataResultSet resultSet, 
			int updateCount, int totalCount, String dDocNameFieldName, String xBundleRefFieldName, 
			FWFacade facade) throws DataException
	{
		if (resultSet==null || resultSet.isEmpty())
			return;

		String dDocName = null;
		String xBundleRef = null;
		
		do {
			totalCount++;
			
			dDocName = resultSet.getStringValueByName(dDocNameFieldName);
			xBundleRef = resultSet.getStringValueByName(xBundleRefFieldName);
			
			// Do additional column lookup. Sometimes the column is returned as 
			// xBundleRef, other times it is XBUNDLEREF
			if (StringUtils.stringIsBlank(xBundleRef))
				xBundleRef = resultSet.getStringValueByName
				 (xBundleRefFieldName.toUpperCase());
			
			if (!StringUtils.stringIsBlank(dDocName) 
				&& !StringUtils.stringIsBlank(xBundleRef)) {
				
				DataResultSet rsBatchItems = 
				 BatchDocumentServices.getBatchItems(xBundleRef , facade, false);
				
				try {
					// Re-evaluate priority.
					LWDocument bundleDoc = new LWDocument(dDocName, true);
	
					int priority = 
					 BundlePriorityUtils.getPriority(bundleDoc, rsBatchItems);
					
					// Compare against the current priority value. If it hasn't
					// changed, dont bother updating the doc metadata.
					Integer curPriority = null;
					
					String curPriorityStr = bundleDoc.getAttribute("xPriority");
					if (!StringUtils.stringIsBlank(curPriorityStr)) {
						curPriority = Integer.parseInt(curPriorityStr);
					}
					
					if (curPriority == null || !curPriority.equals(priority)) {
						Log.debug("Updating bundle priority to " + priority);
						
						bundleDoc.setAttribute("xPriority", Integer.toString(priority));
						bundleDoc.update();
					} else {
						Log.debug("Skipping bundle, priority value was unchanged (" 
						 + priority + ")");
					}
					
					updateCount++;
					
					Log.debug("Bundle priority update complete. Progress: " 
					 + updateCount + "/" + resultSet.getNumRows());		
					
				} catch (ServiceException se) {
					Log.error("ServiceException "+se.getMessage());
				} catch (Exception e) {
					Log.error("Exception "+e.getMessage());
				}
				
			} else {
				//Log a warning message if ddocname or xbundleref is null as this shouldn't happen!
				Log.warn("blank field for " +
				 "dDocName="+(StringUtils.stringIsBlank(dDocName)?"":dDocName) +
				 ", xBundleRef="+(StringUtils.stringIsBlank(xBundleRef)?"":xBundleRef));
						
			}
		} while (resultSet.next());
	}
	
	/**
	 * Add the envelope (bundleref) to the unflag bundle boost rule.
	 * This will create a new rule if it doesn't exist.
	 */
	public static void addUnflagBundlesBoost(String bundleRef, FWFacade facade) 
	throws DataException, ServiceException {
		
		if (!UNFLAGGED_BOOST_RULE_ENABLED) {
			Log.debug("Unflagged bundle boost rule not enabled, not boosting "+bundleRef);
			return;
		}
		
		//0. Check bundleRef value first:
		if (StringUtils.stringIsBlank(bundleRef)) {
			Log.error("Not boosting bundleRef, as bundleRef is blank");
			return;
		}
		
		//1. Check if rule exist first, otherwise add it
		Vector<JobPriorityRule> vec = BundlePriorityUtils.getJobPriorityRules();
		JobPriorityRule rule = null;
		DataBinder binder = null;

		
		if (vec!=null) {
			for (int i=0; i<vec.size();i++) {
				rule = (JobPriorityRule)vec.get(i);
				if (rule.getName().equals(UNFLAGGED_BOOST_RULE_NAME)) {
					break;
				} else {
					rule = null;
				}
			}
		} 
		
		// rule is empty, add new priority rule
		if (rule==null) 
		{
			JobPriorityRule.add(UNFLAGGED_BOOST_RULE_NAME, 
					Globals.UCMFieldNames.BundleRef, 
					JobPriorityRule.RuleType.BundleData, 
					1, 
					"1", 
					UNFLAGGED_BOOST_RULE_APPLIES_TO, 
					UNFLAGGED_BOOST_RULE_APPEND, 
					facade);

			binder =  new DataBinder();
			binder.putLocal("ruleName", UNFLAGGED_BOOST_RULE_NAME);
			DataResultSet rsJobID =
				 facade.createResultSet("QGetJobPriorityRuleByName", binder);
			
			if (rsJobID==null)
				throw new DataException("Cannot create a new priority boost rule");
			else 
				rule = JobPriorityRule.get(rsJobID);				
		} 
		// 2. Check for values
		if (rule!=null) 
		{
			Vector<JobPriorityRuleValue> ruleValues = rule.getRuleValues();
			
			JobPriorityRuleValue ruleValue = null;
			
			if (ruleValues!=null) {				
				
				for (int i=0; i<ruleValues.size(); i++) 
				{
					ruleValue = ruleValues.get(i);
					if (ruleValue.getFieldValue().equals(bundleRef))
						break;
					else 
						ruleValue = null;					
				}
			}
			
			if (ruleValue==null) {
				
				Integer numValidDays = null;
				
				if (!StringUtils.stringIsBlank(UNFLAGGED_BOOST_RULE_NUM_VALID_DAYS)){
					try {
						numValidDays = Integer.parseInt(UNFLAGGED_BOOST_RULE_NUM_VALID_DAYS);
					} catch (NumberFormatException nfe) {
						Log.error("cannot convert the number of valid days "+UNFLAGGED_BOOST_RULE_NUM_VALID_DAYS);						
					}
				}
				
				JobPriorityRuleValue.add(rule.getRuleId(), bundleRef, UNFLAGGED_BOOST_RULE_PRIORITY, numValidDays, facade);
				Log.debug("--addUnflaggedBoostRule : adding ruleValue priority:"+UNFLAGGED_BOOST_RULE_PRIORITY);
			} 
		}
		facade.commitTransaction();
		
		// 3. Refresh cache and re-evaluate priorities for client
		BundlePriorityUtils.refreshJobPriorityRuleCache(facade);

		Log.debug("--END of addUnflaggedBoostRule : xBundleRef:"+bundleRef);		
		
	}
}