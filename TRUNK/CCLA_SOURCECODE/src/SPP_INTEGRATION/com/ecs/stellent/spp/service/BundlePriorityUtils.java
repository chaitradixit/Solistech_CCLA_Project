package com.ecs.stellent.spp.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.shared.SharedObjects;

import com.ecs.stellent.spp.data.JobPriorityRule;
import com.ecs.stellent.spp.data.JobPriorityRuleValue;
import com.ecs.ucm.ccla.Globals;
import com.ecs.ucm.ccla.Globals.UCMFieldNames;
import com.ecs.ucm.ccla.data.DocumentClass;
import com.ecs.ucm.ccla.data.SystemConfigVar;
import com.ecs.ucm.ccla.utils.DateUtils;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Utility methods for supporting priority allocation on bundle items.
 *  
 *  Each bundle priority is stored as a number against the bundle content
 *  item (xPriority meta-data field). These numbers determine the ordering
 *  when auto-fetching bundles for user processing, with higher priority
 *  numbers being presented before lower numbers.
 *  
 *  The field is updated 'on-demand', i.e. when the bundle item is initially
 *  generated and when it is pushed through processing steps in the 
 *  CCLA_MailHandling workflow.
 *  
 *  
 * @author Tom
 *
 */
public class BundlePriorityUtils {

	//Special boost for either item or bundle data. These matches the System Config Var type names.
	//If these are set and matches the document, 
	//then the existing rules are are ignored.
	//
	private static final String BUNDLE_DATA_FIXED_PRIORITY_TYPE = "BundleDataPriority";
	private static final String ITEM_DATA_FIXED_PRIORITY_TYPE = "ItemDataPriority";
	
	// Determines whether bundles have their priority calculated during checkin.
	public static final boolean REFRESH_PRIORITY_ON_BUNDLE_CHECKIN = 
	 SharedObjects.getEnvValueAsBoolean
	 ("CCLA_refreshBundlePriorityOnBundleCheckin", false);
	
	// Determines whether bundles have their priority calculated when one of their
	// child items has their document class updated
	public static final boolean REFRESH_PRIORITY_ON_DOC_CLASS_UPDATE = 
	 SharedObjects.getEnvValueAsBoolean
	 ("CCLA_refreshBundlePriorityOnDocClassUpdate", false);
	
	// Determines whether bundles have their priority calculated during checkin.
	public static final boolean REFRESH_PRIORITY_ON_BUNDLE_STATUS_UPDATE = 
	 SharedObjects.getEnvValueAsBoolean
	 ("CCLA_refreshBundlePriorityOnBundleStatusUpdate", false);
	
	//minimum number of days in which to boost the priority
	//for a mandate
	public static final int AUTO_MANDATE_BOOST_DAY_LIMIT = 
		SharedObjects.getEnvironmentInt("AUTO_MANDATE_BOOST_DAY_LIMIT", 8);
	
	//minimum number of days in which to boost the priority
	//for a mandate
	public static final int AUTO_MANDATE_BOOST_PRIORITY = 
		SharedObjects.getEnvironmentInt("AUTO_MANDATE_BOOST_PRIORITY", 10);
		
	public static final boolean AUTO_MANDATE_BOOST_ENABLED =
		SharedObjects.getEnvValueAsBoolean("AUTO_MANDATE_BOOST_ENABLED", false);

	public static final List AUTO_MANDATE_BOOST_STATUS_LIST =
		SharedObjects.getEnvValueAsList("AUTO_MANDATE_BOOST_STATUS_LIST");

	public static final List AUTO_MANDATE_BOOST_SCAN_USER_LIST =
		SharedObjects.getEnvValueAsList("AUTO_MANDATE_BOOST_SCAN_USER_LIST");

	
	/** Default priority if the bundle didn't match against any priority
	 *  rules.
	 */
	public static final int DEFAULT_PRIORITY = 0;
	
	/** Cache of all Job Priority Rules, and their associated Rule Values. */
	private static Vector<JobPriorityRule> jobPriorityRules;
	
	/** Determines if the rules are currently 'in use' i.e. being evaluated.
	 *  The rule cache will not refresh while the rules are being used.
	 */
	private static boolean rulesInUse = false;
	
	/** Updates the priority on the passed LWDocument instance, providing it is
	 *  different to the current xPriority value.
	 *  
	 * @param bundleDoc
	 * @param rsBatchItems
	 * @return 	whether or not the document actually got updated with a new priority
	 * 			value
	 * @throws Exception 
	 */
	public static boolean updateBundlePriority
	 (LWDocument bundleDoc, DataResultSet rsBatchItems) throws Exception {
		
		int newPriority = getPriority(bundleDoc, rsBatchItems);
		
		String currentPriorityStr = bundleDoc.getAttribute("xPriority");
		boolean update = false;
		
		if (!StringUtils.stringIsBlank(currentPriorityStr)) {
			int currentPriority = Integer.parseInt(currentPriorityStr);
			update = (newPriority != currentPriority);
			
		} else {
			update = true;
		}
		
		if (update) {
			Log.debug("Updating Bundle Priority for " + 
			 bundleDoc.getAttribute(UCMFieldNames.BundleRef) + " to " + newPriority);
			bundleDoc.setAttribute("xPriority", Integer.toString(newPriority));
			bundleDoc.update();
		} else {
			Log.debug("Skipping Bundle Priority update for " + 
			 bundleDoc.getAttribute(UCMFieldNames.BundleRef) + " to " + newPriority +
			 " (unchanged)");
		}
		
		return update;
	}

	/** Determines the priority for the passed bundle item, and its associated
	 *  documents, by scanning through the Job Priority rules.
	 *  
	 * @param bundleDoc
	 * @param rsBatchItems
	 * @return
	 * @throws ServiceException
	 */
	public static int getPriority(LWDocument bundleDoc, DataResultSet rsBatchItems)
	throws ServiceException 
	{	
		return calculatePriority(null, bundleDoc, rsBatchItems);	
	}
		
	/** Returns the calculated priority for the given bundle item, based on
	 *  the metadata assigned to the bundle item itself and its child items.
	 * 
	 * @param bundleDoc
	 * @param rsBatchItems
	 * @return
	 * @throws ServiceException 
	 */
	public static int getPriority_OLD
	 (LWDocument bundleDoc, DataResultSet rsBatchItems)
	 throws ServiceException {
		
		try {
			//String status 	= bundleDoc.getAttribute("xStatus");
			
			String scanUser = bundleDoc.getAttribute(Globals.UCMFieldNames.ScanUser);
			String source 	= bundleDoc.getAttribute(Globals.UCMFieldNames.Source);
			
			if (!StringUtils.stringIsBlank(source)) {
				if (source.equals("Email"))
					return 3;
				else if (source.equals("Fax"))
					return 2;
				else if (source.equals("Scanned for Iris") 
						 &&
						 scanUser != null
						 &&
						 scanUser.equals("SCANUSER1")) {
					return 1;
				}
			}
			
			return DEFAULT_PRIORITY;
			
		} catch (Exception e) {
			throw new ServiceException
			 ("Failed to assign priority to bundle item", e);
		}
	}
	
	/** Evaluates a single JobPriorityRule instance against the given 
	 *  bundle/batch (parent) document, and a set of bundle/batch (child)
	 *  items.
	 *  
	 * @param rule
	 * @param bundleDoc
	 * @param rsBatchItems
	 * @return
	 * @throws Exception 
	 */
	private static int evaluateRule
	 (JobPriorityRule rule, LWDocument bundleDoc, DataBinder bundleBinder, DataResultSet rsBatchItems) 
	 throws Exception {
		
		int priority = DEFAULT_PRIORITY;
		
		//1. First check if the rules apply to this bundle status.
		//For backwards compatibility, if appliesToBundleValuesStr is blank, this will match all statuses
		if (bundleDoc!=null || bundleBinder!=null) 
		{
			String status = null;
			
			if (bundleDoc!=null)
				status = bundleDoc.getAttribute(Globals.UCMFieldNames.Status);
			else if (bundleBinder!=null) 
				status = bundleBinder.getLocal(Globals.UCMFieldNames.Status);
				
			if (!StringUtils.stringIsBlank(status)) 
			{
				if (!rule.containsAppliesToStatus(status)) 
				{
					Log.debug("Not found matching bundle status:"+status+" for rule, returning priority 0");
					return priority;
				}
			}
		}
		
		//2. Evaluate the rest of the rule priorites as normal
		if (rule.getRuleType().equals(JobPriorityRule.RuleType.BundleData)) {
			// This rule must be evaluated against the Bundle document 
			// meta-data.
			
			String fieldValue = null;
				
			if (bundleDoc!=null)
				fieldValue = bundleDoc.getAttribute(rule.getFieldName());
			else if (bundleBinder!=null) 
				fieldValue = bundleBinder.getLocal(rule.getFieldName());
			
			Vector<JobPriorityRuleValue> ruleValues = rule.getRuleValues();
			
			if (fieldValue == null || 
				ruleValues == null || ruleValues.isEmpty())
				return priority;
			
			for (JobPriorityRuleValue ruleValue : ruleValues) {
				
				
				if (!ruleValue.isExpired() && ruleValue.getFieldValue().equals(fieldValue)) {
					// Found a matching rule value.
					
					priority = ruleValue.getPriority();
					
					Log.debug("Found a matching priority rule value against " +
					 "Bundle field: " + rule.getFieldName() + "='" + 
					 fieldValue + "' (" +  priority + " " + (rule.isAppendPriority()?"Append":"")+")");
					
					return priority;
				}
			}
		} else if (rule.getRuleType().equals(JobPriorityRule.RuleType.ItemData)) {
			// This rule must be evaluated against the metadata of each passed
			// Bundle item, if applicable.
			
			if (rsBatchItems == null || !rsBatchItems.first()) {
				Log.warn("Unable to evaluate Bundle Documents priority rule: " +
				 "passed batch items ResultSet was empty/missing.");
				return priority;
			}
			
			Vector<JobPriorityRuleValue> ruleValues = rule.getRuleValues();
			
			if (ruleValues == null || ruleValues.isEmpty())
				return priority;
			
			// Loop through each batch item, keeping a record of the highest
			// priority found.
			do {
				int itemPriority = 0;
				
				String fieldValue = rsBatchItems.getStringValueByName
				 (rule.getFieldName());
				
				if (fieldValue == null)
					continue; // skip to next
				
				for (JobPriorityRuleValue ruleValue : ruleValues) {
					if (!ruleValue.isExpired() && ruleValue.getFieldValue().equals(fieldValue)) {
						// Found a matching rule value.
						
						itemPriority = ruleValue.getPriority();
						
						Log.debug("Found a matching priority rule value " +
						 "against Bundle item '" + 
						 rsBatchItems.getStringValueByName(Globals.UCMFieldNames.DocName) + 
						 "' field: " + rule.getFieldName() 
						 + "='" + fieldValue + "' (" +  itemPriority + ")");
						
						if (itemPriority > priority)
							priority = itemPriority;
					}
				}
				
			} while (rsBatchItems.next());
			
			rsBatchItems.first();
		}
		
		// No rule matches found.
		return priority;
	}

	
	private static int evaluateAutoMandateRule(String xStatus, String scanUser, DataResultSet rsBatchItems) {
	Log.debug("Performing AUTO-MANDATE-BOOST checks");
		
		int priority = DEFAULT_PRIORITY;
		
		//1. First check if the rules apply to this bundle status.
		//For backwards compatibility, if appliesToBundleValuesStr is blank, this will match all statuses
			
		if (!StringUtils.stringIsBlank(xStatus)) 
		{
			if (!AUTO_MANDATE_BOOST_STATUS_LIST.contains(xStatus)) 
			{
				Log.debug("AUTO MANDATE BOOST. Not founding matching bundle status for rule, returning priority 0");
				return priority;
			}
		} else
			return priority;
		

		boolean isMandate = false;
		boolean isScanUser = false;
		Date dInDate = null;
		String docClassValue = null;
		do {
			dInDate = rsBatchItems.getDateValueByName(Globals.UCMFieldNames.DocInDate);
			docClassValue = rsBatchItems.getStringValueByName(Globals.UCMFieldNames.DocClass);



//			Log.debug("dInDate is "+(dInDate==null?"null":"not null")+
//					", xDocumentClass is "+(StringUtils.stringIsBlank(docClassValue)?"null":"not null")+
//					", xScanUser is "+(StringUtils.stringIsBlank(scanUser)?"null":"not null"));
			
			if (dInDate==null || 
					StringUtils.stringIsBlank(docClassValue) || 
						StringUtils.stringIsBlank(scanUser)) {
				continue; // skip to next
			}
			
			try {				
				DocumentClass documentClass = 
					DocumentClass.getCache().getCachedInstance(docClassValue);	
				
				isMandate = documentClass.isMandate();
			} catch (DataException de) {
				Log.error("Cannot determine if docClass is mandate "+docClassValue+", exception="+de.getMessage());
				isMandate = false;
			}
			
			if (AUTO_MANDATE_BOOST_SCAN_USER_LIST!=null && !AUTO_MANDATE_BOOST_SCAN_USER_LIST.isEmpty()) {
				isScanUser = AUTO_MANDATE_BOOST_SCAN_USER_LIST.contains(scanUser);
			} else {
				isScanUser = true;
			}
			
//			Log.debug("isMandate="+isMandate+", isScanUser="+isScanUser+", dInDate="+dInDate.toString()+", xDocumentClass="+docClassValue+", xScanUser="+scanUser);
		
			if (isMandate && isScanUser) 
			{
				Date calInDate = DateUtils.getWorkingDay(dInDate, AUTO_MANDATE_BOOST_DAY_LIMIT, false);
				Log.debug("IS MANDATE: "+docClassValue+", dInDate="+dInDate.toString()+", calc="+calInDate.toString()+", scanuser="+scanUser);
				if (DateUtils.compareDates(DateUtils.getNow(), calInDate)>=0) {
					priority = AUTO_MANDATE_BOOST_PRIORITY;
					break;
				} 
			} 
			 
		} while (rsBatchItems.next());
		
		rsBatchItems.first();
			
		return priority;
	}
	
	/**
	 * Special Rule to check for mandates that are older than 8 working days
	 * assign priority of 10
	 * use the dInDate for the comparison.
	 * Loop through each batch item, if found, then break out as the priority are fixed.
	 */
	private static int evaluateAutoMandateRule(LWDocument bundleDoc, DataBinder bundleBinder, DataResultSet rsBatchItems) {
		
		String xStatus = null;
		String scanUser = null;

		try {
			if (bundleDoc!=null) {
				xStatus = bundleDoc.getAttribute(Globals.UCMFieldNames.Status);		
			} else if (bundleBinder!=null) {
				xStatus = bundleBinder.getLocal(Globals.UCMFieldNames.Status);		
			}
		} catch (Exception e) {
			Log.error("Cannot get xStatus for bundle item, not calculating AutoMandateBoost");
			xStatus = null;
		}
			
		try {
			if (bundleDoc!=null) {
				scanUser = bundleDoc.getAttribute(Globals.UCMFieldNames.ScanUser);	
			} else if (bundleBinder!=null) {
				scanUser = bundleBinder.getLocal(Globals.UCMFieldNames.ScanUser);		
			}
 
		} catch (Exception e) {
			Log.error("cannot get xScanUser attribute from bundle item "+e.getMessage());
			scanUser = null;
		}
		
		return evaluateAutoMandateRule(xStatus, scanUser, rsBatchItems);
	}
	
	public static void setJobPriorityRules(Vector<JobPriorityRule> jobPriorityRules) {
		BundlePriorityUtils.jobPriorityRules = jobPriorityRules;
	}

	public static Vector<JobPriorityRule> getJobPriorityRules() {
		return jobPriorityRules;
	}
	
	/** Rebuilds the Job Priority Rules cache. Should be called on server
	 *  startup and whenever the underlying Rules data is changed.
	 *  
	 * @param facade
	 * @throws DataException 
	 * @throws ServiceException 
	 */
	public static synchronized void refreshJobPriorityRuleCache(FWFacade facade) 
	 throws DataException, ServiceException {
		
		Log.debug("Refreshing Job Priority Rule/Value cache...");
		
		// Block for a period of time if the rules are currently 'in use'
		if (rulesInUse) {
			int retries = 0;
			
			while (rulesInUse && (retries < 20)) {
				try {
					Log.debug("Waiting for Rules Cache to become available...");
					Thread.sleep(500);
				} catch (InterruptedException e) {}
				
				retries++;
			}
			
			if (retries >= 20)
				throw new ServiceException("Unable to refresh Job Priority " + 
				 "Rule/Value cache: lock wait timeout exceeeded.");
		}

		// Fetch rule/values from database tables
		DataResultSet rsPriorityRules = 
		 JobPriorityRule.getAllJobPriorityRulesData(facade);
	
		DataResultSet rsPriorityRuleValues =
		 JobPriorityRuleValue.getAllJobPriorityRuleValuesData(facade);
		
		Vector<JobPriorityRule> newRules = 
		 new Vector<JobPriorityRule>();
		
		// Temporary HashMap used to store a mapping between Rule IDs
		// and fetched Rule Values
		HashMap<Integer, Vector<JobPriorityRuleValue>> ruleValuesMap =
		 new HashMap<Integer, Vector<JobPriorityRuleValue>>();
		
		if (rsPriorityRules.isEmpty()) {
			Log.warn("No Job Priority Rules found!");
			
			// Set empty rules Vector
			setJobPriorityRules(newRules);
			return;
		} else {
			
			// First create the temporary Rule-Value mapping.
			if (rsPriorityRuleValues.first()) {
				do {
					JobPriorityRuleValue ruleValue = 
					 JobPriorityRuleValue.get(rsPriorityRuleValues);
					
					int thisRuleId = ruleValue.getRuleId();
					
					if (ruleValuesMap.containsKey(thisRuleId)) {
						Vector<JobPriorityRuleValue> ruleValues = 
						 ruleValuesMap.get(thisRuleId);
						
						ruleValues.add(ruleValue);
					} else {
						Vector<JobPriorityRuleValue> ruleValues = 
						 new Vector<JobPriorityRuleValue>();
						
						ruleValues.add(ruleValue);
						ruleValuesMap.put(thisRuleId, ruleValues);
					}
					
				} while (rsPriorityRuleValues.next());
				
			} else {
				Log.warn("No Job Priority Rule Values found!");
			}
			
			// Now instantiate the JobPriorityRuleValue instances.
			do {
				JobPriorityRule priorityRule = 
				 JobPriorityRule.get(rsPriorityRules);
				
				int ruleId = priorityRule.getRuleId();
				
				if (ruleValuesMap.containsKey(ruleId)) {
					Vector<JobPriorityRuleValue> ruleValues = 
					 ruleValuesMap.get(ruleId);
					
					priorityRule.setRuleValues(ruleValues);
					
					Log.debug("Cached Rule '" + priorityRule.getName() + "' "
					 + "(" + ruleValues.size() + " associated rule values)");
				} else {
					Log.debug("Cached Rule '" + priorityRule.getName() + "' "
					 + "(no associated rule values)");
				}
				
				newRules.add(priorityRule);
				
			} while (rsPriorityRules.next());

			setJobPriorityRules(newRules);
			
			Log.debug("Cached " + rsPriorityRules.getNumRows() + 
			 " job priority rules.");
		}
	}
	
	
	/**
	 * Evaluates the fixed bundle priority system rules
	 * Returns null if no rule matches
	 * @param bundleDoc
	 * @return
	 */
	private static Integer evaluateFixedBundlePriority(LWDocument bundleDoc, DataBinder bundleBinder) 
	{
		Integer priority = null;
		
		try {
			Vector<SystemConfigVar> fixedBundleDataPriorities = 
				SystemConfigVar.getTypeCache().getCachedInstance(BUNDLE_DATA_FIXED_PRIORITY_TYPE);
					
			String priorityMapping = null;
			String dataField = null;
			String dataValue = null;
			Integer fixedPriority = null;
			
			if (fixedBundleDataPriorities!=null) {
				for (SystemConfigVar sysConfigVar: fixedBundleDataPriorities) 
				{
					priorityMapping = sysConfigVar.getStringValue();
					if (!StringUtils.stringIsBlank(priorityMapping)) 
					{
						StringTokenizer st = new StringTokenizer(priorityMapping,":");
						
						if (st.countTokens()==3) 
						{			
							try {
								dataField = st.nextToken();
								dataValue = st.nextToken();
								fixedPriority = Integer.parseInt(st.nextToken());
							} catch(NumberFormatException nfe) {
								dataField = null;
								dataValue = null;
								fixedPriority = null;
							}
							
							if (fixedPriority!=null && 
									!StringUtils.stringIsBlank(dataField) &&
									!StringUtils.stringIsBlank(dataValue)) 
							{
								String docValue = null; 
								
								try { 
									if (bundleDoc!=null)
										docValue = bundleDoc.getAttribute(dataField);
									else if (bundleBinder!=null)
										docValue = bundleBinder.getLocal(dataField);
									
								} catch (Exception e) {
									Log.error("Cannot get "+dataField+" for bundleDoc, not calculating fixed bundle priority");
									docValue = null;
								}
								
								if (!StringUtils.stringIsBlank(docValue) && 
										docValue.equals(dataValue)) {
									
									Log.debug("Found Matching Fix Bundle Priority, field:"
											+dataField+", value:"+dataValue+", priority:"+fixedPriority);
									
									if (priority==null || priority<fixedPriority)
										priority = fixedPriority;
								}	
							} else {
								Log.error("Cannot evaluate fixed priority rule, one");
							}							
						} else 
							Log.error("Invalid fixed priority string: "+priorityMapping);
					}
				}
			}
		} catch (DataException de) {
			Log.error("Error matching fixed priorities", de);				
		}
		return priority;
	}
	
	/**
	 * Evaluates the fixed item priority system rules
	 * Returns null if no rule matches
	 * @param bundleDoc
	 * @return
	 */
	private static Integer evaluateFixedItemDataPriority(DataResultSet rsBatchItems) 
	{
		Integer priority = null;
		
		try {
			Vector<SystemConfigVar> fixedItemDataPriorities = 
					SystemConfigVar.getTypeCache().getCachedInstance(ITEM_DATA_FIXED_PRIORITY_TYPE);
		
			String priorityMapping = null;
			String dataField = null;
			String dataValue = null;
			
			if (fixedItemDataPriorities!=null) 
			{
				do 
				{
					Integer fixedPriority = null;
					
					for (SystemConfigVar sysConfigVar: fixedItemDataPriorities) 
					{
						priorityMapping = sysConfigVar.getStringValue();
						if (!StringUtils.stringIsBlank(priorityMapping)) 
						{
							StringTokenizer st = new StringTokenizer(priorityMapping,":");
							
							if (st.countTokens()==3) 
							{			
								try {
									dataField = st.nextToken();
									dataValue = st.nextToken();
									fixedPriority = Integer.parseInt(st.nextToken());
								} catch(NumberFormatException nfe) {
									dataField = null;
									dataValue = null;
									fixedPriority = null;
								}
								
								if (fixedPriority!=null && 
										!StringUtils.stringIsBlank(dataField) &&
										!StringUtils.stringIsBlank(dataValue)) 
								{
									String docValue = null; 
									
									try { 
										docValue = rsBatchItems.getStringValueByName(dataField);
									} catch (Exception e) {
										Log.error("Cannot get "+dataField+" for bundleDoc, not calculating fixed bundle priority");
										docValue = null;
									}
									
									if (!StringUtils.stringIsBlank(docValue) && 
											docValue.equals(dataValue)) {
										
										Log.debug("Found Matching Fix Item Priority, field:"
												+dataField+", value:"+dataValue+", priority:"+fixedPriority);
										
										if (priority==null || priority<fixedPriority)
											priority = fixedPriority;
									}	
								} else {
									Log.error("Cannot evaluate fixed priority rule, one");
								}							
							} else 
								Log.error("Invalid fixed priority string: "+priorityMapping);
						}
					}
				} while (rsBatchItems.next());
			}
		} catch (DataException de) {
			Log.error("Error matching fixed priorities", de);				
		}
		return priority;
	}	
	
	/** Determines the priority for the passed bundle binder, and its associated
	 *  documents, by scanning through the Job Priority rules.
	 *  
	 * @param bundleDoc
	 * @param rsBatchItems
	 * @return
	 * @throws ServiceException
	 */
	public static int getPriority(DataBinder bundleBinder, DataResultSet rsBatchItems)
	throws ServiceException 
	{
		return calculatePriority(bundleBinder, null, rsBatchItems);
	}
	
	
	/**
	 * Use either the DataBinder or LWDocument or the bundle document and batch items
	 * @param bundleBinder
	 * @param bundleDoc
	 * @param rsBatchItems
	 * @return
	 * @throws ServiceException
	 */
	private static int calculatePriority(DataBinder bundleBinder, LWDocument bundleDoc, DataResultSet rsBatchItems)
	throws ServiceException {
		try {
			rulesInUse 		= true; // Lock out the cached Rules
			int priority 	= DEFAULT_PRIORITY;
			boolean isFixPriority = false;
			
			//First check the fixed bundle priority rules
			Integer fixedBundlePriority = 
				evaluateFixedBundlePriority(bundleDoc, bundleBinder);
			if (fixedBundlePriority!=null && fixedBundlePriority>priority) {
				priority = fixedBundlePriority;
				isFixPriority = true;
			}
			
			//Check the fix item priority rules
			Integer fixedItemPriority = evaluateFixedItemDataPriority(rsBatchItems);

			if (fixedItemPriority!=null && fixedItemPriority>priority) {
				priority = fixedItemPriority;
				isFixPriority = true;
			}

			//If contains fix priority rules, do not evaluate using the system rules.
			if (!isFixPriority) 
			{
				Vector<Integer> appendPriorityVec = new Vector<Integer>(); 
	
				for (JobPriorityRule rule: jobPriorityRules) {
					if (rule.isEnabled()) {
						int rulePriority = evaluateRule
						 (rule, bundleDoc, bundleBinder, rsBatchItems);
						
						if (rule.isAppendPriority() && rulePriority!=0) {
							appendPriorityVec.add(rulePriority);
						} else {
							if (rulePriority > priority)
								priority = rulePriority;
						}
					}
				}
				
				if (AUTO_MANDATE_BOOST_ENABLED) {
					int rulePriority = evaluateAutoMandateRule(bundleDoc, bundleBinder, rsBatchItems);
					if (rulePriority > priority)
						priority = rulePriority;
				} 				
				
				//Append priority to rules.
				for (int appendPriority: appendPriorityVec) {
					Log.debug("-- Appending priority: "+appendPriority);
					priority+=appendPriority;
				}
			}
			
			// Return highest-matched priority.
			rulesInUse = false;
			
			return priority;
			
		} catch (Exception e) {
			throw new ServiceException
			 ("Failed to assign priority to bundle item :"+e.getMessage(), e);
		} finally {
			rulesInUse = false;
		}
	}
}
