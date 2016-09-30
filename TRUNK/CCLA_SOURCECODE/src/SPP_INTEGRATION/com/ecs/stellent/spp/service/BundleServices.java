package com.ecs.stellent.spp.service;

import idcbean.data.LWDataBinder;
import idcbean.data.LWResultSet;
import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.data.ResultSet;
import intradoc.data.ResultSetUtils;
import intradoc.server.Service;
import intradoc.shared.SharedObjects;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import com.ecs.stellent.auditlog.AuditUtils;
import com.ecs.stellent.iris.ItemLockUtils;
import com.ecs.stellent.iris.batch.BatchDocumentServices;
import com.ecs.stellent.iris.batch.dualindex.DualIndexUtils;
import com.ecs.stellent.spp.fundprocessdetails.FundProcessDetails;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals;
import com.ecs.ucm.ccla.Globals.UCMDocTypes;
import com.ecs.ucm.ccla.Globals.UCMFieldNames;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.Contact;
import com.ecs.ucm.ccla.data.SystemConfigVar;
import com.ecs.ucm.ccla.data.form.Form;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.DateFormatter;
import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.LWFacade;
import com.ecs.utils.stellent.embedded.FWFacade;

public class BundleServices extends Service {
	
	/** Caches the day range used to check for duplicate
	 *  items.
	 */
	public static String OFFSET_DAYS = null;
	/** Caches the millisecond range used to check for duplicate
	 *  items.
	 */
	public static long OFFSET_MILLIS = 0;
	
	/** Determines whether bundles are immediately locked after being
	 *  returned by the getNextPendingBundle() method.
	 * 
	 */
	public static final boolean PREEMPTIVE_AUTOFETCH_LOCK =
	 !StringUtils.stringIsBlank(SharedObjects.getEnvironmentValue
	 ("CCLA_preemptiveAutoFetchLock"));
	
	
	/** How many milliseconds old a bundle must be before it is returned by the
	 *  auto-fetch method. It is stored in an environment variable as a
	 *  number of minutes.
	 */
	public static final int MINIMUM_BUNDLE_AGE_MILLIS = 
	 SharedObjects.getEnvironmentInt("CCLA_minimumBundleAge", 0) * 1000 * 60;
	
	/** Used to store a list of Mandate-only bundle usernames, derived from an
	 *  entry in the SYSTEM_CONFIG_VAR table. 
	 *  
	 *  Built on first use.
	 *  
	 *  When any user featured in this list requests the next pending bundle,
	 *  an alternate query is used that will only fetch bundles which contain
	 *  Mandates.
	 */
	public static Vector<String> MANDATE_BUNDLE_USERS = new Vector<String>();
	
	/** Stores all cached bundle count ResultSets. */
	private static Vector<BundleCountResultSet> CACHED_RESULTSETS;
	
	//SystemConfigVar name contain cs instruction type. i.e BUYBNK,DEPBNK
	private static final String INSTR_ALLOWED_PENDING_TRANSACTION_CHECKS = "allowPendTransCheckInstr";
	
	// System Config Var names.
	public static final class ConfigVarNames {
		public static final String OVERRIDE_FLAG_BUNDLE_USERS 
		 = "DocValidation_OverrideFlagBundleUsers";
	}
	
	static {
		CACHED_RESULTSETS = new Vector<BundleCountResultSet>();
		
		CACHED_RESULTSETS.add(new BundleCountResultSet("Status"));
		CACHED_RESULTSETS.add(new BundleCountResultSet("ScanUser"));
		CACHED_RESULTSETS.add(new BundleCountResultSet("Source"));
		CACHED_RESULTSETS.add(new BundleCountResultSet("Priority"));
	}
	
	private static class BundleCountResultSet {
		private String name;
		private DataResultSet resultSet;
		
		public BundleCountResultSet(String name) {
			this.name = name;
		}
		
		public void setResultSet(DataResultSet resultSet) {
			this.resultSet = resultSet;
		}
		
		public DataResultSet getResultSet() {
			return resultSet;
		}

		public String getName() {
			return name;
		}
	}

	/** Cache of completed bundle counts for the current day */
	private static int NUM_BUNDLES_COMPLETED = 0;
	
	/** Last time the Bundle Count ResultSets were cached. */
	private static Date BUNDLE_COUNTS_LAST_UPDATED = null;
	
	/** No. of milliseconds that the cached Bundle Count resultSets are valid for.
	 *  
	 *  Configurable via an env. variable (see below)
	 **/
	public static int BUNDLE_COUNT_REFRESH_TIME_MILLIS = 120000;
	
	/** Determines whether the previous ResultSet fetch used the more complex queries
	 *  that include 'completed' bundle counts.
	 */
	private static boolean INCLUDED_COMPLETED_COUNTS = false;
	
	static {
		int refreshTimeInSeconds = SharedObjects.getEnvironmentInt
		 ("CCLA_bundleCountRefreshTime", 120);
		
		BUNDLE_COUNT_REFRESH_TIME_MILLIS = refreshTimeInSeconds * 1000;
		
		INCLUDED_COMPLETED_COUNTS = !StringUtils.stringIsBlank(
		 SharedObjects.getEnvironmentValue("CCLA_includeCompletedBundleCounts"));
	}

	/** Called in custom checkin method used for manual upload of
	 *  documents in CCLA Iris implementation.
	 *  
	 *  To ensure such items always end up in unique envelopes,
	 *  an envelope ID is generated, if one is not found in the binder.
	 *  
	 *  If the xSource field value is 'Fax':
	 *   ID is prefixed with an 'f'
	 *  
	 *  If the xSource field value is 'User Upload'
	 *   ID is prefixed with an 'u'
	 *   
	 *  otherwise it is prefixed with an 'e' as normal.
	 */
	public void checkForEnvelopeId() throws DataException, Exception {
		
		String bundleRef = m_binder.getLocal("xBundleRef");
		
		if (StringUtils.stringIsBlank(bundleRef)) {
			// No supplied envelope ID, generate one now.
			String source = m_binder.getLocal("xSource"); 
			FWFacade facade = CCLAUtils.getFacade(m_workspace,true);
			
			bundleRef = getNewBundleRef(source, facade);
			m_binder.putLocal("xBundleRef", bundleRef);
		}
	}
	
	/** Fetches a new bundle ref, generally used to identify a new
	 *  document bundle in Iris.
	 *  
	 *  The number itself comes from a DB sequence. This number is
	 *  then prefixed by a letter based on the document source value.
	 *  
	 *  E.g. 'f' is used to prefix all fax documents.
	 *  
	 * @param source
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static String getNewBundleRef(String source, FWFacade facade) 
	 throws DataException {
		
		String bundleRef = SppIntegrationUtils.getNextEnvelopeId(facade);
		
		if (source == null) 
			bundleRef = "e" + bundleRef;
		else if (source.equals("Fax"))
			bundleRef = "f" + bundleRef;
		else if (source.equals("User Upload"))
			bundleRef = "u" + bundleRef;
		else if (source.equals("Bank"))
			bundleRef = "b" + bundleRef;
		else if (source.equals("Email"))
			bundleRef = "m" + bundleRef;
		else
			bundleRef = "e" + bundleRef;
		
		Log.debug("Generated new bundle ref: '" + bundleRef + 
		 "' (source = " + source + ")");
		
		return bundleRef;
	}
	
	/**	All bundle items will be expired (i.e. dOutDate set) and their single 
	 * 	bundle content item will have it's xStatus field set to 'Deleted'. 
	 * 
	 * 	Marking a bundle as 'Deleted' - this involves updating its xStatus
	 *  value to 'Deleted'. This will remove it from the Scanned Envelopes
	 *  queue and eventually release it automatically from the CCLA_MailHandling
	 *  workflow.
	 */
	public void deleteBundle() throws DataException, Exception {
		
		String bundleRef = m_binder.getLocal("bundleRef");
		Log.debug("Deleting bundle with ref: " + bundleRef);
		
		if(StringUtils.stringIsBlank(bundleRef))
			throw new ServiceException("Supplied bundle ref was missing: " +
			 "no items were deleted.");
		else if (bundleRef.equals("0"))
			throw new ServiceException("Bundle ref was 0, preventing deletion.");
		
		// Fetch all bundle items and mark them as expired
		DataResultSet batchItems =
		 BatchDocumentServices.getBatchItems(bundleRef, m_workspace);
		
		Log.debug("Found " + batchItems.getNumRows() + " batch items.");
		int numExpired = 0;
		
		if (batchItems.first()) {
			do {
				try {
					String itemDocName = ResultSetUtils.getValue(batchItems,"dDocName");
					
					Log.debug("Marking item " + itemDocName + " as expired...");
					
					LWDocument itemDoc = new LWDocument(itemDocName, true);
					itemDoc.setAttribute("dOutDate", DateFormatter.getTimeStamp());
					
					itemDoc.update();
					
					Log.debug("Done.");
					numExpired++;
					
				} catch (Exception e) {
					Log.error("Failed to mark item for expiry",e);
				}
				
			} while (batchItems.next());
		}
		
		//Get the bundle item content item (one file that represents the bundle in
		//Iris)
		DataResultSet bundleItemData =
		 BatchDocumentServices.getParentBatchItem(bundleRef,
		 BatchDocumentServices.getFWFacade(m_workspace));
		
		// Update status value on bundle item
		String bundleDocName 	= ResultSetUtils.getValue(bundleItemData,"dDocName");
		String bundleTitle		= ResultSetUtils.getValue(bundleItemData, "dDocTitle");
		LWDocument bundleDoc 	= new LWDocument(bundleDocName, true);
		
		bundleDoc.setAttribute("xStatus", "Deleted");
		bundleDoc.update();
		
		// Add audit entry for bundle deletion
		// Params:
		// 1. bundle ref
		// 2. number of items expired
		Vector params = new Vector();
		params.add(bundleRef);
		params.add(Integer.toString(numExpired));
		
		AuditUtils.addAuditEntry("IRIS", "DEL-BUNDLE", 
		 bundleDocName, 
		 bundleTitle, 
		 m_userData.m_name,
		 "Bundle marked as deleted, " + numExpired + " of " 
		 	+ batchItems.getNumRows() + " items expired.",
		 params);
	}
	
	/** 
	 *  Sets the xStatus value for a given item.
	 * @throws DataException 
	 *  
	 */
	public void updateItemStatus() throws ServiceException, DataException {
		String docName 		= m_binder.getLocal("docName");
		String newStatus	= m_binder.getLocal("newStatus");
		
		updateItemStatus
		 (docName, newStatus, CCLAUtils.getFacade(m_workspace), m_userData.m_name);
	}
	
	/** 
	 *  Unflags all docnames passed to it in comma separated list
	 * @throws Exception 
	 *  
	 *  
	 */
	public void unflagMultipleBundles() throws Exception {
		
		String docNameListStr 		= m_binder.getLocal("updateList");
		String[] docNameList 		= docNameListStr.split(",");
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace,false);
		
		// loop round docNameList to get each doc and unflag
		for (int i=0; i<docNameList.length; i++) {
			String docNameStr 		= docNameList[i];
			unflagBundle(docNameStr, m_userData.m_name, facade, false);
		}
	}
	
	/** Method that runs on a scheduled execution cycle. Automatically unflags all
	 *  currently-flagged bundles, if the flag time is more than a certain age, 
	 *  specified by the numDays paramter. This can be a fraction, so 0.5 = 12 hours,
	 *  2 = 2 days/48 hours.
	 *  
	 *  Adds an audit note to these bundles to indicate they were automatically 
	 *  unflagged due to their age.
	 *  
	 *  By default, the method will unflag all bundles which were flagged more than
	 *  24 hours ago.
	 * @throws DataException 
	 * @throws ServiceException 
	 */
	public static void unflagAgedBundles(FWFacade facade, float numDays, boolean useTransaction) 
	 throws DataException, ServiceException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryFloatParamToBinder(binder, "numDays", numDays);
		
		DataResultSet rsAgedFlaggedBundles = facade.createResultSet
		 ("QGetAgedFlaggedBundles", binder);
		
		Log.debug("Found " + rsAgedFlaggedBundles.getNumRows() + " flagged bundles, " +
		 "which were flagged more than " + CCLAUtils.PLAIN_NUMBER_FORMAT.format(numDays) 
		 + " days ago");
		
		if (rsAgedFlaggedBundles.first()) {
			do {
				String bundleDocName =  rsAgedFlaggedBundles.getStringValueByName
				 (Globals.UCMFieldNames.DocName);
				String bundleRef =  rsAgedFlaggedBundles.getStringValueByName
				 (Globals.UCMFieldNames.BundleRef);
				
				int flagReasonId = CCLAUtils.getResultSetIntegerValue
				 (rsAgedFlaggedBundles, "FLAG_REASON_ID");
				String flagReason = rsAgedFlaggedBundles.getStringValueByName
				 ("FLAG_REASON");
				
				Log.debug("Unflagging bundle " + bundleRef + "(" + bundleDocName + 
				 "), flag reason: " + flagReason);
				
				BundlePriorityServices.addUnflagBundlesBoost(bundleRef, facade);
				
				if (unflagBundle(bundleDocName, Globals.Users.System, facade, useTransaction)) {
					// Add audit note to bundle.
					AuditUtils.addAuditEntry("IRIS", 
					 "AUTO-UNFLAG-BUNDLE", bundleDocName, 
					 "Envelope: " + bundleRef, Globals.Users.System, 
					 "Flagged bundle was automatically unflagged", new Vector());
				}
				
			} while (rsAgedFlaggedBundles.next());
		}
	}
	
	/** Unflags the given bundle item, providing it is at Flagged status, and the user
	 *  didn't cause the flagging in the first place.
	 * 
	 *  The bundle priority is also refreshed at this point.
	 * 
	 * @param bundleDocName
	 * @param facade
	 * @return true if the bundle was unflagged
	 * @throws DataException
	 */
	public static boolean unflagBundle
	 (String bundleDocName, String userName, FWFacade facade, boolean useTransaction)
	 throws ServiceException {
		
		try {
			LWDocument bundleDoc 	= new LWDocument(bundleDocName, true);
			
			if (!useTransaction)
				bundleDoc.setUseTransaction(useTransaction);
			
			// Ensure bundle item is at 'Flagged' status before continuing
			String xStatus = bundleDoc.getAttribute(Globals.UCMFieldNames.Status);
			Log.debug("Bundle " + bundleDocName + " had status: " + xStatus);
			if (xStatus.equals("Flagged")) {
				
				// Ensure current user isn't listed in the xDocumentAuthor field - this
				// is used to store the name of the user who caused the bundle to be
				// flagged.
				String lastFlagUser = bundleDoc.getAttribute
				 (Globals.UCMFieldNames.DocumentAuthor);
				
				Log.debug("Last Flag User=" + lastFlagUser);
				
				if (!StringUtils.stringIsBlank(lastFlagUser)
					&& lastFlagUser.equals(userName)) {
					// Found a match between current user and previous flag user.
					// Check they aren't in the 'override users' list - if not the unflag
					// action will be thrown an error.
					Log.debug("Current User matches Last Flag User!" +
					 " Checking override list");
					
					boolean isOverrideUser = isFlagOverrideUser(userName);
					
					if (!isOverrideUser) {
						// Prevent unflagging - user caused the bundle to be flagged!
						throw new ServiceException("You aren't permitted to unflag this " + 
						"bundle - you caused it to be flagged in the first place. " +
						"Please ask another user to do this.");
					}
				}
				
				String wfStepName = SppIntegrationUtils.getDocumentWorkflowStepName
				 (bundleDocName, facade);
				
				if (wfStepName == null) {
					Log.debug("setting status to completed");
					bundleDoc.setAttribute(Globals.UCMFieldNames.Status, "Completed");
				} else {
					bundleDoc.setAttribute(Globals.UCMFieldNames.Status, wfStepName);
					Log.debug("Setting bundle status to " + wfStepName);
				}
				
				// Re-evaluate priority
				DataResultSet rsBatchItems = BatchDocumentServices.getBatchItems
				 (bundleDoc.getAttribute(Globals.UCMFieldNames.BundleRef), facade);
				
				int bundlePriority = BundlePriorityUtils.getPriority
				 (bundleDoc, rsBatchItems);
				
				Log.debug("Evaluated new bundle priority: " + bundlePriority);
				bundleDoc.setAttribute("xPriority", Integer.toString(bundlePriority));
				
				bundleDoc.update();
				
				// Add audit entry
				// Add audit note to bundle.
				AuditUtils.addAuditEntry("IRIS", 
				 "UNFLAG-BUNDLE", bundleDocName, 
				 "Envelope: " + bundleDoc.getAttribute(Globals.UCMFieldNames.BundleRef), 
				 userName, 
				 "Bundle un-flagged by " + userName, new Vector());
				
				Log.debug("Bundle unflagged successfully");
				return true;
			} else {
				Log.debug("Skipping unflag action: bundle was not at Flagged status");
				return false;
			}
			
		} catch (Exception e) {
			String msg = e.getMessage();
			
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}
	
	public static void updateItemStatus
	 (String docName, String newStatus, FWFacade facade, String userId) 
	 throws ServiceException {
		
		Log.debug("Setting new status value for " + docName + ": " + newStatus);
		
		try {
			LWDocument lwDoc = new LWDocument(docName, true);
			
			boolean isBundle = (lwDoc.getAttribute(UCMFieldNames.DocType)
			 .equals("Bundle"));
			
			// Prevent update on Bundle items with a status of 'Completed' - these should
			// essentially be read-only. The UI will prevent any such update calls, but
			// a stale page or accidental resubmission could still invoke such an invalid
			// request.
			if (isBundle
				&& lwDoc.getAttribute(UCMFieldNames.Status).equals("Completed")) {
				Log.warn("Invalid bundle status update call. Bundle " + docName + " is " +
				 "already at Completed status. Skipping status update");
				return;
			}
			
			boolean doUpdate = true;
			lwDoc.setAttribute(UCMFieldNames.Status, newStatus);
			
			if (BundlePriorityUtils.REFRESH_PRIORITY_ON_BUNDLE_STATUS_UPDATE) {
				// Refresh bundle priority when a bundle status is updated.
				if (isBundle) {
					String batchRef = lwDoc.getAttribute(UCMFieldNames.BundleRef);
					
					if (!StringUtils.stringIsBlank(batchRef)) {
						Log.debug("Refreshing Bundle Priority due to status update");
						
						doUpdate = !BundlePriorityUtils.updateBundlePriority(lwDoc, 
						 BatchDocumentServices.getBatchItems(batchRef, facade));
					}
				}
			}
			
			if (doUpdate) // Update the doc metadata, unless it was updated above
				lwDoc.update();
			
			// Add audit entry to log status change
			AuditUtils.addAuditEntry("IRIS", "UPDATE-ITEM-STATUS", 
			 docName, lwDoc.getAttribute("dDocTitle"), userId, 
			 "Item status updated to '" + newStatus + "'", new Vector());
			
		} catch (Exception e) {
			Log.error("Unable to update item status", e);
			throw new ServiceException("Unable to update item status: " 
			 + e.toString(), e);	
		}
	}
	
	/** Sets the xPriority value for a given bundle.
	 * 
	 * @throws ServiceException
	 */
	public static boolean updateItemPriority
	 (String docName, String newPriority, String userId) {
		
		Log.debug("Setting new priority value for " + docName + ": " + newPriority);
		
		try {
			LWDocument lwDoc = new LWDocument(docName, true);
			String oldPriority = lwDoc.getAttribute("xPriority");
			
			lwDoc.setAttribute("xPriority", newPriority);
			lwDoc.update();
			
			String msg = null;
			Vector params = new Vector();
			
			if (StringUtils.stringIsBlank(oldPriority))
				msg = "Priority manually updated to " + newPriority;
			else {
				msg = "Priority manually updated from " + oldPriority
				 	   +  " to " + newPriority;
				params.add(oldPriority);
			}
			
			// Add audit entry to log status change
			AuditUtils.addAuditEntry("IRIS", "UPDATE-ITEM-PRIORITY", 
			 docName, lwDoc.getAttribute("dDocTitle"), userId, msg,
			 params);
			
			return true;
		} catch (Exception e) {
			Log.error("Unable to update item priority", e);
			return false;	
		}
	}
	
	/** Checks a given Iris bundle for duplicate items.
	 *  
	 *  Requires the following present in the binder:
	 *  -rsBatchItems				ResultSet of all batch items
	 *  or
	 *  -bundleRef					bundle ref for the batch
	 *  
	 *  Adds the following to the binder:
	 *  -numDuplicatesFound: 		number of items in the bundle which
	 *  							are potential duplicates
	 *  -csvDuplicatedBatchItems: 	comma-separated list of bundle item
	 *  							dDocNames which are potential duplicates
	 *  
	 *  -rsDuplicates_<dDocName>:	0 or more ResultSets holding the matched items
	 *  							for each potential duplicate in the bundle
	 * @throws DataException 
	 * @throws ServiceException 
	 */
	public void checkForDuplicateItems() throws DataException, ServiceException {
		
		long startTime = System.currentTimeMillis();
		Log.debug("checkForDuplicateItems >>");
		
		DataResultSet batchItems = null;
		
		// Check for existing rsBatchItems ResultSet in the binder
		ResultSet rsBatchItems = m_binder.getResultSet("rsBatchItems");
		
		String bundleRef = m_binder.getLocal("bundleRef");
		FWFacade facade = CCLAUtils.getFacade(m_workspace,false);
		
		if (rsBatchItems == null) {
			// Fetch batch items
			if (StringUtils.stringIsBlank(bundleRef))
				throw new ServiceException("No batch items or bundleRef present, " +
				 "unable to check for duplicates.");
			
			batchItems =
			 BatchDocumentServices.getBatchItems(bundleRef, facade);
			m_binder.addResultSet("rsBatchItems", batchItems);
			
			Log.debug("Fetched batch items for bundle: " + bundleRef + ". Took " + 
			 (System.currentTimeMillis() - startTime)/1000D + " seconds.");
			
		} else {
			batchItems = new DataResultSet();
			batchItems.copy(rsBatchItems);
		}
		
		int numDuplicates = 0;
		int numChecks = 0;
		String duplicateItemsList = "";
		
		// Loop through all items in the batch and record any duplicates found.
		do {
			String docName 			= batchItems.getStringValueByName("dDocName");
			
			// Fetch all fields used to check for matches
			String clientNumber 	= batchItems.getStringValueByName
			 (UCMFieldNames.ClientNumber);
			String accountNumber	= batchItems.getStringValueByName
			 (UCMFieldNames.AccountNumber);
			String amount			= batchItems.getStringValueByName
			 (UCMFieldNames.Amount);
			String units			= batchItems.getStringValueByName
			 (UCMFieldNames.Units);
			
			String status			= batchItems.getStringValueByName
			 (UCMFieldNames.Status);
			
			if (StringUtils.stringIsBlank(clientNumber) 
				||
				StringUtils.stringIsBlank(accountNumber) 
				||
				(StringUtils.stringIsBlank(amount) && StringUtils.stringIsBlank(units))
				) {
				Log.debug("Item " + docName + " skipping duplicate check.");
			} else {
				Log.debug("Checking item " + docName + " for duplicates.");
				
				DataBinder qBinder		= new DataBinder();
				qBinder.putLocal("docName", docName);
				qBinder.putLocal("clientNumber", clientNumber);
				qBinder.putLocal("accountNumber", accountNumber);
				
				CCLAUtils.addQueryParamToBinder(qBinder, "amount", amount);
				CCLAUtils.addQueryParamToBinder(qBinder, "units", units);
				
				// Add offset date
				Date inDate 			= batchItems.getDateValueByName("dInDate");
				qBinder.putLocalDate("startDate", getDuplicateOffsetDate(inDate));
				
				// Execute duplicate check query (found in CCLA_MailHandling component)
				DataResultSet rsDuplicates =
				 facade.createResultSet("QGetDuplicates", qBinder);
				
				numChecks++;
				
				if (!rsDuplicates.isEmpty()) {
					// Potential duplicates found for this bundle item.
					numDuplicates++;
					duplicateItemsList += "," + docName + ",";
					
					Log.debug("Found " + rsDuplicates.getNumRows() + 
					 " potential duplicates.");
					
					m_binder.addResultSet("rsDuplicates_" + docName, rsDuplicates);
				} else {
					Log.debug("No duplicate candidates.");
				}
			}
			
		} while (batchItems.next());
		
		m_binder.putLocal("numDuplicatesFound", numDuplicates + "");
		m_binder.putLocal("csvDuplicatedBatchItems", duplicateItemsList);
		
		double elapsedTime = (System.currentTimeMillis() - startTime)/1000D;
		Log.debug("checkForDuplicateItems << bundle: " + bundleRef + 
		 ", no. checks: " + numChecks + 
		 ", time taken: " + Double.toString(elapsedTime) + "s");
	}
	
	/** Computes the offset date for the given date, used when looking
	 *  for duplicate items.
	 *  
	 *  Returns a date which is set to x number of days before the given
	 *  date, where x corresponds to the env. var CCLA_duplicateDayCheckRange.
	 *  
	 * @param itemDate
	 * @return
	 */
	private static Date getDuplicateOffsetDate(Date itemDate) {
		
		if (OFFSET_DAYS == null) {
			// Cache the offset constants
			OFFSET_DAYS = 
			 SharedObjects.getEnvironmentValue("CCLA_duplicateDayCheckRange");
			
			int offsetDays 	= Integer.parseInt(OFFSET_DAYS);
			OFFSET_MILLIS	= 86400000 * offsetDays;
			
			Log.debug("Evaluating duplicate offset constants: " 
			 + offsetDays + " days, " + OFFSET_MILLIS + " milliseconds");
		}

		Date offsetDate = new Date(itemDate.getTime() - OFFSET_MILLIS);
		//Log.debug("Orig date: " + itemDate.toString() + 
		// ", duplicate offset date: " + offsetDate.toString());
		return offsetDate;
	}
	
	/**
	 * Reviews the choices made for found duplicate bundle content items, 
	 * and updates their status accordingly.
	 * 
	 * Duplicate checking is now performed twice per document bundle. This
	 * method must be capable of detecting 'duplicate disagreements', i.e. if
	 * the first user marks a document as a duplicate, but the second one 
	 * doesn't.
	 * 
	 * If one or more documents are in 'disagreement', the last one detected is
	 * placed into the binder as duplicateDisagreementDocName. This is picked
	 * up by an AJAX callback method on the duplicate checking page, and forces
	 * the bundle to be flagged rather than submitted to SPP.
	 * 
	 * PARAMS: 
	 * csvDuplicatedBundleItems - the comma separated list of bundle item doc
	 * 			names that are duplicates.
	 * 
	 * duplicate_action_[DDOCNAME] - the choice made for that content item (the 
	 * 			dDocName will be stored in csvDuplicatedBundleItems). If 
	 * 			duplicate_action_[DDOCNAME]=1 it means mark it as a duplicate, 
	 * 			otherwise proceed.
	 * 
	 */
	public void updateDuplicateItems() throws ServiceException{
		Log.debug("updateDuplicateItems() >>");
		
		String bundleRef 		= m_binder.getLocal("envelopeId");
		String bundleDocName 	= m_binder.getLocal("bundleDocName");
		
		String[] duplicatedDocs = StringUtils.stringToArray(
				m_binder.getLocal("csvDuplicatedBundleItems"));
		
		Log.debug("Warned about " + duplicatedDocs.length + 
		 " potentially duplicated documents (" + 
		 m_binder.getLocal("csvDuplicatedBundleItems") + ") for envelope " 
		 + bundleRef);
		
		try {
			LWDocument bundleDoc = new LWDocument(bundleDocName, true);
			String bundleStatus = bundleDoc.getAttribute("xStatus");
			
			// Loop through all suspected duplicate bundle items which were displayed to 
			// the user.
			for(int i=0; i<duplicatedDocs.length; i++) {
				String suspectedDuplicateDocName =  duplicatedDocs[i];
				
				String actionStr = m_binder.getLocal("duplicate_action_" + 
				 suspectedDuplicateDocName);
				
				// Determine whether the user opted to mark this suspected duplicate
				// as a duplicate, or ignore it.
				boolean markAsDuplicate = (actionStr != null && actionStr.equals("1"));
				Log.info(suspectedDuplicateDocName + " mark as duplicate: " + markAsDuplicate);
				
				String duplicatesOfStr = m_binder.getLocal("csvBundleItemDuplicateOf_" 
						+ suspectedDuplicateDocName);
				
				String[] duplicatesOf = StringUtils.stringToArray(duplicatesOfStr);
	
				// Prepare audit entry for duplicate marking/ignoring
				String auditMessage = null;
				String auditAction = null;			
				
				// Audit Params:
				// 1. bundle ref
				// 2. The dDocName of the item being marked as a duplicate
				// 3-6: The potential documents it is a duplicate of
				Vector<String> params = new Vector<String>();
				params.add(bundleRef);
				params.add(duplicatedDocs[i]);
	
				//Add up to 4 more parameters for each potential duplicated document
				int maxRemainingParams=4;
				int counter=0;
				
				if(duplicatesOf != null){
					for(int j=0; j<duplicatesOf.length; j ++){
						if(counter < maxRemainingParams) 
							params.add(duplicatesOf[j]);
						
						counter++;
					}
				}
				
				LWDocument lwd 		= new LWDocument(suspectedDuplicateDocName, true);
				String docStatus 	= lwd.getAttribute("xStatus");
				
				if (markAsDuplicate) {
					// User opted to flag this item as a duplicate.
	
					// TODO
					// Check if we are at the Validation step, and it is not currently
					// marked as a duplicate - this would indicate a disagreement between 
					// indexers.
					if (bundleStatus.equals("Validation")
						&& !docStatus.equals("Duplicate")) {
						Log.debug("User opted to mark an item as a Duplicate, " +
						 "which was previously ignored as Duplicate.");

						m_binder.putLocal("duplicateDisagreementDocName", 
						 suspectedDuplicateDocName);
					}
					
					// Set the xComments value on this item to indicate it was
					// tagged as a duplicate.
					lwd.setAttribute("xComments", "Duplicate of:" + duplicatesOfStr + ".");
					lwd.setAttribute("xStatus", "Duplicate");
					lwd.update();
					
					auditMessage = "Set " + suspectedDuplicateDocName + " as a duplicate of: " + 
					 duplicatesOfStr;
					
					auditAction = "DUPLICATE-ST-UPD";
					
					Log.debug("Successfully set " + suspectedDuplicateDocName + 
					 " xStatus to 'Duplicate' of: " + duplicatesOfStr);

				} else {
					// User has chosen to ignore this suspected duplicate item.
					// Check if it was previously marked as a duplicate - this would
					// indicate a disagreement between indexers.

					if (docStatus.equals("Duplicate")) {
						Log.debug("User opted to ignore a potential duplicate item, " +
						 "which was previously marked as Duplicate.");

						m_binder.putLocal("duplicateDisagreementDocName", 
						 suspectedDuplicateDocName);
					}
					
					// Set the item status to match that of the parent bundle, if it
					// doesn't match already.

					if (docStatus == null || !docStatus.equals(bundleStatus)) {
						Log.debug("Updating status of ignored duplicate to: "
						 + bundleStatus);
						
						lwd.setAttribute("xStatus", bundleStatus);
						lwd.update();
					}
					
					auditAction = "DUPLICATE-IGNORE";
					
					auditMessage = "Ignored warning for " + suspectedDuplicateDocName 
					 + " as a duplicate of: " + duplicatesOfStr;
				}
				
				// Audit the decision for each suspected duplicate separately.
				AuditUtils.addAuditEntry("IRIS", auditAction, 
			     bundleDocName,
				 bundleDoc.getTitle(),
				 m_userData.m_name,
				 auditMessage,
				 params);
			}
			
			m_binder.putLocal("duplicateError", "0");
			
		} catch (Exception e) {
			Log.error("Error updating bundle document duplicate status", e);
			m_binder.putLocal("duplicateError", "1");
		}
		
		Log.debug("updateDuplicateItems() <<");
	}
	
	public void testSearchResultsTiming() throws DataException {
		int numQueries = 50;
		
		String numQueriesParam = m_binder.getLocal("numQueries");
		if (!StringUtils.stringIsBlank(numQueriesParam))
			numQueries = Integer.parseInt(numQueriesParam);
		
		Log.debug("testSearchResultsTiming >>");
		Log.debug("Fetching " + numQueries + " envelope IDs for checking.");
		
		String bundleRefQuery = "SELECT * FROM ( " +
				  "SELECT dm.xBundleRef, dm.dID FROM DocMeta dm " +
				  "INNER JOIN Revisions r ON (dm.dID = r.dID) " +
				  "WHERE (xBundleRef IS NOT NULL) " +
				  "AND (r.dDocType = 'Bundle') " +
				  "AND (r.dRevRank = 0) " +
				  "ORDER BY dm.dID DESC " +
				  ") WHERE ROWNUM <= " + numQueries;
		
		DataResultSet rsBundleRefs = null;
		
		try {
			FWFacade facade = CCLAUtils.getFacade(m_workspace,false);
			rsBundleRefs = facade.createResultSetSQL(bundleRefQuery);
			
			Log.debug("Found " + rsBundleRefs.getNumRows() + " bundle refs");
			
		} catch (Exception e) {
			Log.error("Failed to instantiate FWFacade", e);
			throw new DataException(e.toString());
		}
		
		LWDataBinder caseDocs = new LWDataBinder();
		
		long netStartTime = System.currentTimeMillis();
		
		do {
			String thisBundleRef = rsBundleRefs.getStringValueByName("xBundleRef");
			
			String queryString = 
			 "(dDocType <matches> `Document` <or> dDocType <matches> `ChildDocument`) " + 
			 "<and> xBundleRef <matches> `" + thisBundleRef + "`";
			
			caseDocs.putLocal("QueryText", queryString);
			caseDocs.putLocal("SortField", "dInDate");
			caseDocs.putLocal("SortOrder", "DESC");
			caseDocs.putLocal("ResultCount", "100");
			caseDocs.putLocal("IdcService", "GET_SEARCH_RESULTS");

			LWFacade facade = new LWFacade();

			try {
				long startTime = System.currentTimeMillis();
				
				//Execute the search and populate the search results in the searchResults result set.
				LWResultSet searchResults = facade.executeService(caseDocs, "SearchResults");
				
				Log.debug("GetSearchResults << bundleRef: " + thisBundleRef + ", results: " + 
				 searchResults.getNumRows() + ", time taken: " +
				 (System.currentTimeMillis() - startTime)/1000D + "s");
				
			} catch (Exception e) {
				Log.error("Failed to fetch search results for bundle " 
				 + thisBundleRef + ": " + e.toString());
			}
			
		} while (rsBundleRefs.next());
		
		double timeTaken 	= (System.currentTimeMillis() - netStartTime)/1000D;
		double avgQueryTime	= timeTaken/(rsBundleRefs.getNumRows() + 0L);
		Log.debug("testSearchResultsTiming << time taken: " +
		 timeTaken + "s, average query time: " + avgQueryTime + "s");
	}
	
	/** Fetches cached ResultSets providing aggregate totals of active Iris bundles.
	 * 
	 *  To reduce strain on the DB, the ResultSets are cached for a set period of time
	 *  before they are re-queried. If the forceRefresh flag is passed, the ResultSets
	 *  will always be re-queried.
	 *  
	 *  If the includeCompletedCounts flag is passed, the more complex fetch queries
	 *  are used, which feature 2 'count' columns: one for active bundles and one for
	 *  bundles completed on the current day. Currently, these queries are significantly
	 *  slower than the standard ones.
	 *  
	 * @throws DataException 
	 * 
	 */
	public void getActiveBundleCounts() throws DataException {
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace);
		
		boolean forceRefresh = CCLAUtils.getBinderBoolValue
		 (m_binder, "forceRefresh");
		
		Boolean includeCompletedCounts = CCLAUtils.getBinderBoolValueAllowNull
		 (m_binder, "includeCompletedCounts");
		
		if (includeCompletedCounts == null)
			includeCompletedCounts = INCLUDED_COMPLETED_COUNTS;
		
		boolean refreshed = 
		 checkActiveBundleCounts(facade, forceRefresh, includeCompletedCounts);
		
		for (BundleCountResultSet bcResultSet : CACHED_RESULTSETS) {
			String rsName = "rsBundleCountsBy" + bcResultSet.getName();
			
			if (bcResultSet.getResultSet() != null)
				m_binder.addResultSet
				 (rsName, bcResultSet.getResultSet());
		}
		
		CCLAUtils.addQueryIntParamToBinder
		 (m_binder, "numBundlesCompleted", NUM_BUNDLES_COMPLETED);
		
		if (INCLUDED_COMPLETED_COUNTS)
			m_binder.putLocal("includedCompletedCounts", "1");
		
		if (BUNDLE_COUNTS_LAST_UPDATED != null)
			m_binder.putLocalDate
			 ("bundleCountsLastUpdated", BUNDLE_COUNTS_LAST_UPDATED);
		
		CCLAUtils.addQueryIntParamToBinder
		 (m_binder, "bundleCountRefreshTime", BUNDLE_COUNT_REFRESH_TIME_MILLIS);
		
		if (refreshed) {
			// If we just reloaded the bundle counts from the DB, client will auto-
			// refresh after the full time period has expired
			CCLAUtils.addQueryIntParamToBinder
			 (m_binder, "clientRefreshTime", BUNDLE_COUNT_REFRESH_TIME_MILLIS);
			
		} else if (BUNDLE_COUNTS_LAST_UPDATED != null) {
			// Determine auto-refresh time for client browser.
			
			// Computed as:
			// [refresh time period] - ([last refresh time] - [refresh time in millis])
			long clientRefreshTime = 
			 BUNDLE_COUNT_REFRESH_TIME_MILLIS -
			 (System.currentTimeMillis() - BUNDLE_COUNTS_LAST_UPDATED.getTime());
			
			// Round down to nearest minute
			clientRefreshTime -= clientRefreshTime % 60000;
			
			// Add on a minute, so it will never refresh before the refresh period has
			// been reached.
			clientRefreshTime += 60000;
			
			CCLAUtils.addQueryIntParamToBinder
			 (m_binder, "clientRefreshTime", (int)clientRefreshTime);
		}
	}
	
	/** Checks the validity of the cached bundle count ResultSets and refreshes them
	 *  from the DB if neccessary.
	 * 
	 * @param facade
	 * @return true if the bundle counts were refreshed from the DB
	 * @throws DataException 
	 */
	private static synchronized boolean checkActiveBundleCounts
	 (FWFacade facade, boolean forceRefresh, boolean includeCompletedCounts) 
	 throws DataException {
		
		// Check the last cache update time.
		if (forceRefresh
			||
			BUNDLE_COUNTS_LAST_UPDATED == null
			||
			includeCompletedCounts != INCLUDED_COMPLETED_COUNTS
			||
			((BUNDLE_COUNTS_LAST_UPDATED.getTime() + BUNDLE_COUNT_REFRESH_TIME_MILLIS)
			< new Date().getTime())) {
			// Caches require update.
			
			DataBinder binder = new DataBinder();
			
			for (BundleCountResultSet bcResultSet : CACHED_RESULTSETS) {
				String queryName;
				
				if (includeCompletedCounts)
					queryName = "QGetBundleTotalsBy" + bcResultSet.getName();
				else
					queryName = "QGetActiveBundleTotalsBy" + bcResultSet.getName();
				
				bcResultSet.setResultSet(facade.createResultSet(queryName, binder));
			}
			
			Calendar cal = new GregorianCalendar();
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			
			NUM_BUNDLES_COMPLETED = getWorkflowActionCount
			 ("Approve", "Validation", "CCLA_MailHandling", cal.getTime(), facade);
			
			BUNDLE_COUNTS_LAST_UPDATED = new Date();
			INCLUDED_COMPLETED_COUNTS = includeCompletedCounts;
			
			return true;
		}
		
		return false;
	}
	
	/** Returns the total number of UCM workflow actions at the given step, since the
	 *  given date.
	 *  
	 * @param action either 'Approve' or 'Reject'
	 * @param wfStepName
	 * @param actionDate
	 * @return
	 * @throws DataException 
	 * @throws NumberFormatException 
	 */
	public static int getWorkflowActionCount
	 (String wfAction, String wfStepName, String wfName, 
	 Date actionDate, FWFacade facade) 
	 throws DataException {
		DataBinder binder = new DataBinder();
		
		binder.putLocal("wfAction", wfAction);
		binder.putLocal("wfStepName", wfStepName);
		binder.putLocal("wfName", wfName);
		binder.putLocalDate("wfActionDate", actionDate);
		
		return Integer.parseInt(
		 facade.createResultSet("QGetWFActionTotals", binder).getStringValue(0));
	}
	
	/** Used to reset 'Completed' mail bundles containing Mandate items which
	 *  are still sat in the Parking Lot.
	 *  <br/>
	 *  Two queries are used to fetch the corresponding bundle items. The 
	 *  'basic' fetch query will only return bundles that contain a single
	 *  Mandate. This query is used if the 'basicMandatesOnly' flag is present
	 *  in the binder.
	 *  <br/>
	 *  The other query will return bundles containing an arbitrary number of
	 *  Mandates.
	 *  <br/>
	 *  Each fetched Bundle item is checked out/checked in, triggering the
	 *  CCLA_MailHandling workflow again and forcing the bundle to appear back
	 *  in the Scanned Envelopes listing.
	 *  <br/>
	 *  One parameter is required: numBundles. This determines how many Mandate 
	 *  bundles will be re-introduced to the queue.
	 *  
	 * @throws Exception 
	 */
	public void resetParkingLotBundles() throws Exception {
		String numBundles = m_binder.getLocal("numBundles");
		
		if (StringUtils.stringIsBlank(numBundles)) {
			throw new ServiceException("Unable to reset Parking Lot bundles, " +
			 "number of bundles not specified.");
		}
		
		String clientNumberPrefix = m_binder.getLocal("clientNumberPrefix");
		String clientNumber = "%";
		
		if (!StringUtils.stringIsBlank(clientNumberPrefix))
			clientNumber = clientNumberPrefix + clientNumber;
		
		m_binder.putLocal("clientNumber", clientNumber);
		
		boolean basicMandatesOnly = !StringUtils.stringIsBlank(
			m_binder.getLocal("basicMandatesOnly")
		);
		
		Log.debug("Attempting to reset " + numBundles + " Parking Lot " +
		 "bundles back to the Scanned Envelopes queue...");
		
		Log.debug("ClientNumber filter: " + clientNumber);
		
		if (basicMandatesOnly)
			Log.debug("Only 'basic' mandate bundles will be reset.");
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace,false);
		
		int resetCount = 
			BundleServices.resetParkingLotBundles(numBundles, 
					clientNumberPrefix, basicMandatesOnly, m_userData.m_name, facade);
		
		String redirectUrl = m_binder.getLocal("RedirectUrl");
		
		if (!StringUtils.stringIsBlank(redirectUrl)) {
			redirectUrl += "&resetCount=" + resetCount;
			m_binder.putLocal("RedirectUrl", redirectUrl);
		}
	}
	
	
	public static int resetParkingLotBundles(String numBundles, 
			String clientNumberPrefix, boolean basicMandatesOnly,
			String userName, FWFacade facade) throws ServiceException, DataException {
		
		if (StringUtils.stringIsBlank(numBundles)) {
			throw new ServiceException("Unable to reset Parking Lot bundles, " +
			 "number of bundles not specified.");
		}
		
		String clientNumber = "%";
		
		if (!StringUtils.stringIsBlank(clientNumberPrefix))
			clientNumber = clientNumberPrefix + clientNumber;
		
		
		Log.debug("Attempting to reset " + numBundles + " Parking Lot " +
		 "bundles back to the Scanned Envelopes queue...");
		
		Log.debug("ClientNumber filter: " + clientNumber);
		
		if (basicMandatesOnly)
			Log.debug("Only 'basic' mandate bundles will be reset.");
		
		DataResultSet rsParkingLotBundles = null;
		
		DataBinder binder = new DataBinder();
		binder.getLocal("clientNumberPrefix");
		
		binder.putLocal("numBundles", numBundles);
		binder.putLocal("clientNumber", clientNumber);
		
		if (basicMandatesOnly)
			rsParkingLotBundles = 
			facade.createResultSet("QGetBasicParkingLotBundles", binder);
		else
			rsParkingLotBundles =
			 facade.createResultSet("QGetParkingLotBundles", binder);
		
		Log.debug("Found " + rsParkingLotBundles.getNumRows() + "/" + 
		 numBundles + " bundles.");

		int resetCount = 0;
		
		if (rsParkingLotBundles.isEmpty())
			return resetCount;
		
		// Step through each matched Bundle document. Check it out and
		// check it back in to trigger the CCLA_MailHandling workflow
		// again.
		do {
			String docName = 
			 rsParkingLotBundles.getStringValueByName("dDocName");
			String bundleRef = 
			 rsParkingLotBundles.getStringValueByName("xBundleRef");
			
			Log.debug("Resetting bundle item " + docName + 
			 " (xBundleRef=" + bundleRef + ")...");
			

			try {
				LWDocument bundleDoc = new LWDocument(docName, true);
			
				bundleDoc.checkout();
				// Check-in the bundle, preserving the old release date.
				bundleDoc.checkin(null, null, true, false);
			
				Log.debug("Bundle checked out/checked in successfully.");
				
				resetCount++;
				
				// Add audit entry to log resubmission
				AuditUtils.addAuditEntry("IRIS", "RESET-BUNDLE", 
				 docName, bundleDoc.getAttribute("dDocTitle"), userName, 
				 "Parking Lot bundle resubmitted to Scanned Envelopes queue", 
				 new Vector());
					
			} catch (Exception e) {
				String msg = "Error checking in/out document with docName "+docName+", "+e.getMessage();
				Log.error(msg, e);
				throw new ServiceException(msg, e);
			}

			
			// Now each Mandate must be pushed out of the VerifyMandate workflow.
			// This is done by simply updating the xStatus value to 'Pending',
			// the change is picked up in the Update event of the workflow.
			DataResultSet rsBatchItems = 
			 BatchDocumentServices.getBatchItems(bundleRef, facade);
			
			Log.debug("Setting status of bundle items to 'Pending'");
			
			if (rsBatchItems.isEmpty()) {
				Log.warn("No bundle documents found!");
			} else {
				do {
					String bundleItemDocName = 
					 rsBatchItems.getStringValueByName("dDocName");
					
					Log.debug("Updating status of document " + bundleItemDocName);
					try {
						LWDocument lwDoc = new LWDocument(bundleItemDocName, true);
						lwDoc.setAttribute("xStatus", "Pending");
					
						lwDoc.update();
					
						
						// Add audit entry to log doc resubmission
						AuditUtils.addAuditEntry("IRIS", "RESET-PL-DOC", 
								bundleItemDocName, lwDoc.getAttribute("dDocTitle"), 
								userName, 
								"Item moved back to Scanned Envelopes queue", 
								new Vector());
					} catch (Exception e) {
						String msg = "Error updating document with docName "+bundleItemDocName+", "+e.getMessage();
						Log.error(msg, e);
						throw new ServiceException(msg, e);	
					}
				} while (rsBatchItems.next());
			
				Log.debug("Updated status of " + rsBatchItems.getNumRows() + 
				 " bundle items");
			}
			
		} while (rsParkingLotBundles.next());
		
		return resetCount;
	}	
	
	/** Returns the last flag user for the bundle with the passed dDocName. If the
	 *  flag user matches the current user name, the override list is queried to check
	 *  whether the user's name belongs in it. The outcome of this is passed back in
	 *  the binder var isFlagOverrideUser
	 *  
	 *  THe outcome of these checks is always passed back as 'userEligibleToProcess'
	 *  
	 * @throws ServiceException
	 */
	public void getLastFlagUser() throws ServiceException {
		String docName = m_binder.getLocal("dDocName");
		
		if (StringUtils.stringIsBlank(docName)) {
			throw new ServiceException("Unable to retrieve last flag user - " +
			 "dDocName not present");
		}
		try {
			boolean eligibleToProcess = false;
			
			LWDocument bundleDoc = new LWDocument(docName, true);
			String lastFlagUser = 
			 bundleDoc.getAttribute(Globals.UCMFieldNames.DocumentAuthor);
			
			m_binder.putLocal("lastFlagUser", lastFlagUser);
			
			if (!StringUtils.stringIsBlank(lastFlagUser) 
				&& (lastFlagUser.equals(m_userData.m_name))) {
				// Current user was the last flag user. Check if they are in the override
				// list.
				boolean isOverrideUser = isFlagOverrideUser(m_userData.m_name);
				
				CCLAUtils.addQueryBooleanParamToBinder(m_binder,
				 "isFlagOverrideUser", isOverrideUser);
				
				eligibleToProcess = isOverrideUser;
			} else
				eligibleToProcess = true;
			
			CCLAUtils.addQueryBooleanParamToBinder
			 (m_binder, "userEligibleToProcess", eligibleToProcess);
			
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	/** Determines whether the passed user name is listed in the 'Flag Override Users'
	 *  System Config Var. If so, they are still permitted to unflag and process bundles
	 *  they have previously flagged.
	 *  
	 * @param userName
	 * @return
	 * @throws DataException
	 */
	private static boolean isFlagOverrideUser(String userName) throws DataException {
		SystemConfigVar overrideUsersVar = SystemConfigVar.getCache().getCachedInstance
		 (ConfigVarNames.OVERRIDE_FLAG_BUNDLE_USERS);
		
		if (overrideUsersVar == null)
			return false;
		
		if (overrideUsersVar.getStringValue() != null) {
			String[] overrideUserNames = overrideUsersVar.getStringValue().split(",");
			
			for (String overrideUser : overrideUserNames) {
				if (overrideUser.equals(userName))
					return true;
			}
		}
		
		return false;
	}
	
	/** Marks a given bundle as 'Flagged'. This will log a special audit
	 *  action which contains various parameters specific to the Flag action.
	 *  
	 *  Audit action parameters:
	 *  
	 *  LACTION: 	FLAG-BUNDLE
	 *  LREF:		Bundle dDocName
	 *  LPARAM1:	Flag Reason ID
	 *  LPARAM2:	Flag reason String
	 *  LPARAM3:	Custom comment included (boolean, empty = false)
	 *  LPARAM4:	Selected bundle document dDocName
	 *  LPARAM5:	Selected bundle document class (xDocumentClass)
	 *  LPARAM6:	Document audit ID, relates to an entry in the DOCMETA_AUDIT 
	 *  			table
	 *  
	 * @throws Exception 
	 *  
	 */
	public void flagBundle() throws Exception {
		
		// Bundle document fields
		String bundleDocName 	= m_binder.getLocal("bundleDocName");
		String bundleRef		= m_binder.getLocal("bundleRef");
		
		if (StringUtils.stringIsBlank(bundleDocName)
			||
			StringUtils.stringIsBlank(bundleRef)) {
			throw new ServiceException("Unable to flag bundle, bundle data" +
			 "missing.");
		}
		
		// Selected bundle item Content ID.
		String selDocName	 	= m_binder.getLocal("selDocName");
		
		// Reason ID for flagging. Corresponds to an entry in CCLA_FLAG_REASONS
		// table.
		String reasonIdStr		= m_binder.getLocal("FLAG_REASON_ID");
		
		if (StringUtils.stringIsBlank(reasonIdStr)) {
			throw new ServiceException("Unable to flag bundle, " +
			 "no reason given.");
		}
		
		
		FWFacade facade 		= CCLAUtils.getFacade(m_workspace,false);
		LWDocument bundleDoc 	= new LWDocument(bundleDocName, true);
		
		// Update the flag user
		bundleDoc.setAttribute(Globals.UCMFieldNames.DocumentAuthor, m_userData.m_name);
		bundleDoc.update();
		
		// Update the item status
		updateItemStatus(bundleDocName, "Flagged", facade, m_userData.m_name);
		
		String flagReason		= getFlagReason
		(Integer.parseInt(reasonIdStr), facade);
		
		String flagComment		= m_binder.getLocal("FLAG_COMMENT");
		
		// If the user has supplied their own comment, use this as the audit
		// message. Otherwise just add a generic message with the flag
		// reason included.
		String auditMessage = null;
		boolean hasCustomComment = false;
		
		if (!StringUtils.stringIsBlank(flagComment)) {
			auditMessage = flagComment;
			hasCustomComment = true;
		} else {
			auditMessage = "Bundle flagged: " + flagReason;
		}
		
		Vector<String> params	= new Vector<String>();
		params.add(reasonIdStr);
		params.add(flagReason);
		
		if (hasCustomComment) {
			params.add("1");
		} else {
			params.add("");
		}

		if (!StringUtils.stringIsBlank(selDocName)) {
			LWDocument selDoc = new LWDocument(selDocName, true);
			params.add(selDocName);
			
			String docClass = selDoc.getAttribute("xDocumentClass");
			
			if (docClass != null)
				params.add(docClass);
			
			// Add document metadata snapshot.
			int docMetaAuditId = addDocMetaAudit(selDoc, "Snapshot", facade);
			
			params.add(Integer.toString(docMetaAuditId));
		}
		
		AuditUtils.addAuditEntry("IRIS", "FLAG-BUNDLE", bundleDocName, 
		 bundleDoc.getTitle(), m_userData.m_name, auditMessage, params);
		
		// Add audit entry to notify flag user is now prevented from doing stuff
		AuditUtils.addAuditEntry("IRIS", "FLAG-USER-NOTIFY", 
		 bundleDoc.getDocName(), bundleDoc.getAttribute("dDocTitle"), m_userData.m_name, 
		 "User " + m_userData.m_name + " caused the bundle to be flagged. " +
		 "They won't be allowed to unflag or approve the bundle", new Vector());
	}
	
	public void unflagBundle() throws ServiceException, DataException {
		String bundleDocName 	= m_binder.getLocal("bundleDocName");
		
		unflagBundle(bundleDocName, m_userData.m_name, CCLAUtils.getFacade(false), true);
	}
	
	/** Adds a 'snap shot' of the given documents' metadata to the 
	 *  DOCMETA_AUDIT table.
	 * 
	 * @return the generated DocMeta Audit ID (DOCMETA_AUDIT_ID value)
	 * @throws Exception 
	 */
	public static int addDocMetaAudit
	 (LWDocument lwDoc, String action, FWFacade facade) throws Exception {
		
		if (lwDoc == null)
			throw new DataException("Unable to audit docmeta: " +
			 "LWDocument was null");
		
		Hashtable<String, String> attribs = lwDoc.getAttributes();
		
		// Create a new DataBinder and fill it with all available metadata
		// name-value pairs from the LWDocument.
		DataBinder binder = new DataBinder();
		
		for (Map.Entry<String, String> entry : attribs.entrySet()) {
			binder.putLocal(entry.getKey(), entry.getValue());
		}
		
		// Fetch a new Audit ID.
		DataResultSet rsAuditId = 
		 facade.createResultSet("QGetNextDocMetaAuditId", binder);
		
		int auditId = Integer.parseInt(rsAuditId.getStringValue(0));
		binder.putLocal("DOCMETA_AUDIT_ID", Integer.toString(auditId));
		
		// Add the Action.
		binder.putLocal("ACTION", action);
		
		// Add the new docmeta snapshot.
		facade.execute("QAddDocMetaAudit", binder);
		
		Log.debug("Added Document Metadata snapshot for " 
		 + lwDoc.getAttribute("dDocName") + ", Audit ID: " + auditId);
		
		return auditId;
	}
	
	/** Returns a Flag Reason, corresponding to the given Reason ID in the
	 *  CCLA_FLAG_REASONS table.
	 *  
	 * @param reasonId
	 * @return
	 * @throws DataException 
	 */
	public static String getFlagReason(int reasonId, FWFacade facade)
	 throws DataException {
		DataBinder binder = new DataBinder();
		binder.putLocal("FLAG_REASON_ID", Integer.toString(reasonId));
		
		DataResultSet rsFlagReason = facade.createResultSet
		 ("QGetFlagReason", binder);
		
		return rsFlagReason.getStringValue(0);
	}
	
	/** Fetches the total number of Mandate items held in the Parking Lot,
	 *  plus the total number of mail bundles these Mandates correspond to.
	 *  <br/> 
	 *  Used in the summary page, before resetting Parking Lot bundles using
	 *  the resetParkingLotBundles method.
	 * @throws DataException 
	 *  
	 */
	public void getParkingLotBundleCounts() throws DataException {
		
		DataBinder binder = new DataBinder();
		// Use a massive number
		binder.putLocal("numBundles", "10000000");
		binder.putLocal("clientNumber", "%");
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace,false);
		
		DataResultSet rsParkingLotBundles =
		 facade.createResultSet("QGetParkingLotBundles", binder);
		
		m_binder.putLocal("parkingLotBundleCount", 
		 Integer.toString(rsParkingLotBundles.getNumRows()));
		
		DataResultSet rsParkingLotDocumentCount =
		 facade.createResultSet("QGetParkingLotDocumentCount", binder);
		
		m_binder.putLocal("parkingLotDocumentCount", 
		 rsParkingLotDocumentCount.getStringValue(0));
		
		DataResultSet rsBasicParkingLotBundles =
		 facade.createResultSet("QGetBasicParkingLotBundles", binder);
		
		m_binder.putLocal("basicParkingLotBundleCount", 
		 Integer.toString(rsBasicParkingLotBundles.getNumRows()));
		
		DataResultSet rsParkingLotClientCount =
		 facade.createResultSet("QGetParkingLotClientCount", binder);
			
		m_binder.putLocal("parkingLotClientCount",
		 rsParkingLotClientCount.getStringValue(0));
		
		DataResultSet rsParkingLotAccountCount =
		 facade.createResultSet("QGetParkingLotAccountCount", binder);
		
		m_binder.putLocal("parkingLotAccountCount",
		 rsParkingLotAccountCount.getStringValue(0));
	}
	
	/** Up-revisions the given set of Bundle content items. This will force
	 *  them to re-enter the CCLA_MailHandling workflow.
	 *  
	 *  The passed sqlQuery should return a ResultSet containing a DOCNAME
	 *  column.  
	 *  
	 *  Non-bundle items will be skipped.
	 *  
	 *  WARNING: doesn't work correctly, items get stuck in Review state on
	 *  checkin.
	 *  
	 * @throws Exception 
	 *  
	 */
	public void resetBundles() throws Exception {
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace,false);
		String sqlQuery = m_binder.getLocal("sqlQuery");
		
		DataResultSet rsItems = facade.createResultSetSQL(sqlQuery);
		
		Log.debug("Found " + rsItems.getNumRows() + 
		 " items for up-revisioning.");
		
		if (!rsItems.first())
			return;
		
		int upRevisionCount = 0;
		
		do {
			String docName = rsItems.getStringValueByName("DOCNAME");
			Log.debug("Attempting up-revision for " + docName);
			
			try {
				LWDocument thisDoc = new LWDocument(docName, true);
				if (!thisDoc.getAttribute("dDocType").equals("Bundle")) {
					Log.debug("Item didn't have dDocType of bundle - " +
					 "skipping checkout/checkin.");
				} else {
					thisDoc.checkout();

					// Use special check-in method which prevents release
					// date being updated.
					thisDoc.checkin(null, null, true, false);
					
					upRevisionCount++;
					Log.debug("Up-revision successful.");
				}
				
			} catch (Exception e) {
				Log.error(e.toString(), e);
			}
				
		} while (rsItems.next());
		
		Log.debug("Bundle reset complete: up-revisioned " + 
		 upRevisionCount + "/" + rsItems.getNumRows() + " matched items");
	}
	
	
	/**
	 * Performs a check for all the docs in a bundle that are either transactions or
	 * 'mandate' types.
	 * 
	 * Searches for any pending documents which have the same client number/company as
	 * any of the bundle documents matched above.
	 * 
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void checkForAllPendingDocsInBundle() throws DataException, ServiceException {
			
		String bundleRef = m_binder.getLocal("bundleRef");
		
		Log.debug("Started: Checks For All Pending Docs In Bundle for bundle ref: "
		 + bundleRef);
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace,false);
		
		DataBinder qBinder = new DataBinder();
		qBinder.putLocal("bundleRef", bundleRef);
		
		DataResultSet rsAllPendBundle =
		 facade.createResultSet("QGetAllPendingDocsForClientCompanyForBundle", qBinder);
		
		m_binder.putLocal("hasPending", rsAllPendBundle.isEmpty()?"0":"1"); 
		m_binder.addResultSet("rsAllPendBundle", rsAllPendBundle);
		
		Log.debug("Finished: Checks For All Pending Docs In Bundle: returned " 
		 +rsAllPendBundle.getNumRows() + " matched items for bundle ref: " + bundleRef);
	}
	
	/**
	 * This check is currently executed by AJAX request from the Iris interface
	 * when a document is displayed or updated, and it has certain data fields set.
	 * 
	 * It checks for any pending documents with the same client number/company with a
	 * 'mandate' doc class.
	 * 
	 * Adds a single flag to the binder 'hasPending' which indicates whether or not
	 * there is 1 or more pending documents with the same client number/company.
	 * 
	 * @throws DataException 
	 * @throws ServiceException
	 */
	public void checkForPendingDocsForClientCompany() throws DataException {
		
		String bundleRef 	= m_binder.getLocal("bundleRef");
		String clientNum 	= m_binder.getLocal("clientNum");
		String company 		= m_binder.getLocal("company");
		
		Log.debug("Started: Checks For All Pending Client Docs for bundle ref: " 
		 + bundleRef + ", clientNum: " + clientNum + ", company: " + company);
		
		if (StringUtils.stringIsBlank(bundleRef)
			||
			StringUtils.stringIsBlank(clientNum)
			||
			StringUtils.stringIsBlank(company)) {
			
			Log.debug("Not enough information to run check.");
			m_binder.putLocal("hasPending", "0");
			return;
		}
			
		FWFacade facade = CCLAUtils.getFacade(m_workspace,false);
		
		DataBinder qBinder = new DataBinder();
		
		qBinder.putLocal("bundleRef", bundleRef);
		qBinder.putLocal("clientNum", clientNum);
		qBinder.putLocal("company", company);
		
		DataResultSet rsAllPendClientDocs =
		 facade.createResultSet("QGetAllPendingDocsForClientCompany", qBinder);
		
		m_binder.putLocal("hasPending", rsAllPendClientDocs.isEmpty()?"0":"1"); 
	}
	
	/** Service method for adding dual index items for a bundle.
	 *  
	 *  Fails if the bundle is not at Validation step of workflow.
	 *  
	 *  Matches the service method in the IrisBatchDocuments component, apart
	 *  from the added check to ensure the bundle item is currently in the
	 *  Validation step.
	 *  
	 * @throws ServiceException
	 */
	public void addBundleDualIndexItems() throws ServiceException {
		
		String docName 			= m_binder.getLocal("dDocName");
		String bundleRef 		= m_binder.getLocal("bundleRef");
		
		Log.debug("Adding bundle Dual Index items for " + bundleRef +
		 " via service method");
		
		try {
			FWFacade facade			= CCLAUtils.getFacade(m_workspace,false);
			LWDocument bundleDoc 	= new LWDocument(docName, true);
			
			// Ensure the bundle item is in the 'Validation' workflow
			// step.
			String wfStepName = SppIntegrationUtils.
			 getDocumentWorkflowStepName(docName, facade);
			
			if (wfStepName == null || !wfStepName.equals("Validation")) {
				throw new ServiceException("Unable to add dual index items: " +
				 "bundle " + bundleRef + " not at Validation step.");
			}
			
			DataResultSet rsBatchItems = 
			 BatchDocumentServices.getBatchItems(bundleRef, facade);
			
			DualIndexUtils.addBundleDualIndexItems
			 (bundleDoc, rsBatchItems, true, false, facade);
			
			AuditUtils.addAuditEntry("IRIS", "DI-BUNDLE-RESET", docName, 
			 bundleDoc.getTitle(), m_userData.m_name, "Dual Indexing data reset", 
			 new Vector<String>());
			
		} catch (Exception e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}
	
	
	/**
	 * Resets the duplicate documents in a bundle to the current workflow state 
	 * @throws ServiceException
	 */
	public void resetDuplicate() throws ServiceException 
	{
		Log.debug("--- START resetDuplicate ---");

		String bundleRef = m_binder.getLocal("bundleRef");
		String dDocName = m_binder.getLocal("dDocName");
		
		try {
			FWFacade facade = CCLAUtils.getFacade(m_workspace,false);
			LWDocument bundleDoc = new LWDocument(dDocName, true);
			
			if (StringUtils.stringIsBlank(bundleRef)) {
				throw new ServiceException("resetDuplicate(), bundleRef is empty");
			}

			if (StringUtils.stringIsBlank(dDocName)) {
				throw new ServiceException("resetDuplicate(), dDocName is empty");
			}
			
			String workflowStepName = 
				SppIntegrationUtils.getDocumentWorkflowStepName(dDocName, facade);
			
			
			if (StringUtils.stringIsBlank(workflowStepName)) {
				throw new ServiceException("resetDuplicate(), workflowStepName is empty");
			}
			
			DataResultSet rsBatchItems = 
				 BatchDocumentServices.getBatchItems(bundleRef, facade);
			
			String xStatus;
			String docName;
			String newXStatus;
			boolean resetDuplicates = false;
			
			if (workflowStepName.equals("EnterDetails")) {
				newXStatus = "Pending";
			} else 
				newXStatus = workflowStepName;
			
			if (rsBatchItems.first()) {
				do {
					xStatus = rsBatchItems.getStringValueByName("xStatus");
					docName = rsBatchItems.getStringValueByName("dDocName");
					
					if (!StringUtils.stringIsBlank(docName) 
							&& !StringUtils.stringIsBlank(xStatus) 
							&& xStatus.equals("Duplicate")) 
					{
						try 
						{
							LWDocument document = new LWDocument(docName, true);
							
							document.setAttribute("xStatus", newXStatus);
							document.update();
						
							Log.debug("resetDuplicate() reset Duplicate xStatus for bundleref:"+bundleRef+", " +
									"docName:"+docName+" to "+newXStatus);
							
							String msg = "Reset duplicate status of " + docName;
							Vector<String> params = new Vector<String>();
							params.add(docName);
							
							AuditUtils.addAuditEntry("IRIS", "DUP-BUNDLE-RESET", dDocName, 
							 bundleDoc.getTitle(), m_userData.m_name, msg, params);
							
							if (!resetDuplicates)
								resetDuplicates = true;
						
						} catch (Exception e) {
							Log.error("resetDuplicate()...failed to reset duplicate status for bundleRef:"
									+bundleRef+", dDocName:"+dDocName);
							throw new ServiceException(e.getMessage(), e);
						}
					}
				} while (rsBatchItems.next());
			}
			
			if (!resetDuplicates)
				Log.debug("resetDuplicate()  bundleref:"+bundleRef+" contains no documents to reset");
				
		} catch (Exception e) {
			throw new ServiceException(e.getMessage(), e);
		}

		Log.debug("--- END resetDuplicate ---");
	}
	
	/** Fetches a ResultSet of all UCM document data which are potential
	 *  duplicates of the item with the passed field values.
	 *  
	 *  If any of the required checking fields are empty/null (client no,
	 *  account no, amount), no check is performed and a null ResultSet is 
	 *  returned.
	 *  
	 *  Not yet used.
	 *  
	 * @param docName
	 * @param itemDate dInDate of the document
	 * @param clientNumber e.g. 77564
	 * @param accountNumStr e.g. '0023D'
	 * @param amount e.g. 1100.45
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getSuspectedDuplicateItemsData
	 (String docName, Date itemDate, 
	  Integer clientNumber, String accountNumStr, String amount, String units,
	  FWFacade facade) throws DataException {
		
		if (clientNumber == null 
			||
			StringUtils.stringIsBlank(accountNumStr) 
			||
			(StringUtils.stringIsBlank(amount) && StringUtils.stringIsBlank(units))) {
			
			Log.debug("Item " + docName + " skipping duplicate check.");
			return null;
			
		} else {
			Log.debug("Checking item " + docName + " for duplicates.");
			
			DataBinder qBinder		= new DataBinder();
			
			qBinder.putLocal("docName", docName);
			BinderUtils.addIntParamToBinder(qBinder, "clientNumber", clientNumber);
			qBinder.putLocal("accountNumber", accountNumStr);
			qBinder.putLocal("amount", amount);
			qBinder.putLocal("units", amount);
			
			// Add offset date
			qBinder.putLocalDate("startDate", getDuplicateOffsetDate(itemDate));
			
			// Execute duplicate check query (found in CCLA_MailHandling component)
			DataResultSet rsDuplicates =
			 facade.createResultSet("QGetDuplicates", qBinder);
			
			return rsDuplicates;
		}
	}
	
	/**
	 * Service method called by CCLA_PANEL_VALIDATION_EXTRA
	 * 
	 * Is called using jquery once the doc info panel has loaded in iris. 
	 * 
	 * Use this method to hook in extra validation checks and data loads.
	 * 
	 * Currently using two validations methods: 
	 *  - Process Date Validation
	 *  - Nominated Bank Account Validation
	 *  
	 * @throws Exception 
	 */
	public void panelValidationExtra() throws ServiceException {
		
		try {
			FWFacade cdbFacade = CCLAUtils.getFacade(m_workspace, true);
			
			//Make sure there is enough information on the binder first
			if (FundProcessDetails.checkBinderReqParamsForDealingDate(m_binder)){
				//prepare the binder for the processDate validation
				FundProcessDetails.prepareBinderWithNextDealingDate(m_binder, cdbFacade);
			}
			
			//Make sure there is enough information on the binder first
			if (Account.checkBinderReqParamsForNomBankAcc(m_binder)){
				//prepare the binder for bank detail validation
				Account.isNominatedBankAccount(m_binder,cdbFacade);
			}
			
			// Add account correspondent contact points.
			Contact.addContactPointsResultSetToBinder(m_binder, cdbFacade);
			
			// Add rsForm ResultSet, if a valid xFormId value is present in the binder.
			Integer formId = CCLAUtils.getBinderIntegerValue
			 (m_binder, Form.Cols.ID);
			
			if (formId != null) {
				DataResultSet rsForm = Form.getData(formId, cdbFacade);
				
				if (!rsForm.isEmpty())
					m_binder.addResultSet("rsForm", rsForm);
			}
			
		} catch (Exception e) {
			throw new ServiceException("Data validation/lookup failed: "
			 + e.getMessage(),e);
		}
	}
	
	/**
	 * Service call to get a list of result set of pending preadvice/dicondins 
	 * for the bundle doc or an individual doc.
	 * @throws ServiceException
	 */
	public void getPossiblePreadviceDicondins() throws ServiceException  
	{
		String docName = m_binder.getLocal(Globals.UCMFieldNames.DocName);
		
		if (StringUtils.stringIsBlank(docName)) {
			throw new ServiceException("dDocName or docname is empty");
		}
		
		try 
		{
			//Main Document being passed
			LWDocument mainDoc = null;
			
			//New result set for storing the pending documents
			DataResultSet rsDocsWithPossiblePendingTrans = 
				 new DataResultSet(new String[] {
						 Globals.UCMFieldNames.DocName,
						 Globals.UCMFieldNames.DocClass,									 
				 });
			
			FWFacade facade = CCLAUtils.getFacade(m_workspace,false);
			mainDoc = new LWDocument(docName, true);
			
			String docType = mainDoc.getAttribute(Globals.UCMFieldNames.DocType);
			
			if (StringUtils.stringIsBlank(docType))
				throw new ServiceException("Cannot find pending transactions, docType unknown for "+docName);
			
			
			if (docType.equals("Bundle")) {
				String batchRef = mainDoc.getAttribute(Globals.UCMFieldNames.BundleRef);
	
				// Fetch all batch items
				DataResultSet batchItems = 
					BatchDocumentServices.getBatchItems(batchRef, facade);
	
				do  {
					String dDocName = batchItems.getStringValueByName(Globals.UCMFieldNames.DocName);
					String docClassStr = batchItems.getStringValueByName(Globals.UCMFieldNames.DocClass);
					
					if (!allowedPendingTransactionChecks(docClassStr))
						return;
					
					String clientNumStr = batchItems.getStringValueByName(Globals.UCMFieldNames.ClientNumber);
					String accNumStr = batchItems.getStringValueByName(Globals.UCMFieldNames.AccountNumber);
					String amountStr = batchItems.getStringValueByName(Globals.UCMFieldNames.Amount);
					String companyStr = batchItems.getStringValueByName(Globals.UCMFieldNames.Company);
					
					DataResultSet results = 
						getPossiblePendingPreadviceDicondins(clientNumStr, accNumStr, amountStr, companyStr, facade);
					
					if (results!=null && !results.isEmpty()) {
						m_binder.addResultSet("rsPendingTransRef_"+dDocName, results);
						
						Vector<String> docNameVec = new Vector<String>();
						docNameVec.add(dDocName);
						docNameVec.add(docClassStr);
						rsDocsWithPossiblePendingTrans.addRow(docNameVec);
					}
					
				} while (batchItems.next());
				
				if (!rsDocsWithPossiblePendingTrans.isEmpty())
					m_binder.addResultSet("rsDocsWithPossiblePendingTrans", rsDocsWithPossiblePendingTrans);
			} else {

				
				String docClassStr = mainDoc.getAttribute(Globals.UCMFieldNames.DocClass);
				//treat as individual doc
				if (!allowedPendingTransactionChecks(docClassStr))
					return;
				
				String clientNumStr = mainDoc.getAttribute(Globals.UCMFieldNames.ClientNumber);
				String accNumStr = mainDoc.getAttribute(Globals.UCMFieldNames.AccountNumber);
				String amountStr = mainDoc.getAttribute(Globals.UCMFieldNames.Amount);
				String companyStr = mainDoc.getAttribute(Globals.UCMFieldNames.Company);

				
				DataResultSet results = 
					getPossiblePendingPreadviceDicondins(clientNumStr, accNumStr, amountStr, companyStr, facade);
				
				if (results!=null && !results.isEmpty()) {
					m_binder.addResultSet("rsPendingTransRef_"+docName, results);
					Vector<String> docNameVec = new Vector<String>();
					docNameVec.add(docName);
					docNameVec.add(docClassStr);
					rsDocsWithPossiblePendingTrans.addRow(docNameVec);
					m_binder.addResultSet("rsDocsWithPossiblePendingTrans", rsDocsWithPossiblePendingTrans);
				}
			}
		} catch (Exception e) {
			Log.error("Cannot get possible predadvice or dicondins", e);
			throw new ServiceException("Cannot get possible predadvice or dicondins: "
					+e.getMessage());
		}
	}
	
	
	/**
	 * Gets a resultset of possible pending preadvice or dicondins based on the data.
	 * @param clientNumStr
	 * @param accountNumStr
	 * @param amountStr
	 * @param company
	 * @param facade
	 * @return
	 * @throws ServiceException
	 * @throws DataException
	 */
	public static DataResultSet getPossiblePendingPreadviceDicondins(
			String clientNumStr, String accountNumStr, String amountStr, 
			String company, FWFacade facade) 
	throws ServiceException, DataException 
	{
		if (StringUtils.stringIsBlank(clientNumStr) && StringUtils.stringIsBlank(accountNumStr) 
				&& StringUtils.stringIsBlank(company))
		{
			Log.error("Cannot lookup pending preadvice/dicondins: clientNum,accNum, amount and company are blank");
			throw new ServiceException ("Cannot lookup pending preadvice/dicondins: clientNum,accNum, " +
					"amount and company are blank");
		}
		
		// Get up to the last 3 A-Z characters of account number
		// i.e. the Fund Code
		String fundCode = CCLAUtils.getSuffixChars
		 (accountNumStr);
	
		if (fundCode.length() > 0) {
			String numStr = accountNumStr.substring
			 (0, accountNumStr.length() - fundCode.length());
			
			Integer accountNum = null;
			
			try {
				accountNum = Integer.parseInt(numStr);
			} catch (NumberFormatException ne) {
				throw new ServiceException("Expected numeric prefix " +
				"in account number '" + accountNumStr + 
				"'. Unable to resolve Account.");
			}
			
			Integer clientNum = null;
			
			try { 
				clientNum = Integer.parseInt(clientNumStr);
			} catch (NumberFormatException nfe) {
				clientNum = null;
			}
		
			DataBinder binder = new DataBinder();
			CCLAUtils.addQueryIntParamToBinder(binder, "CLIENT_NUMBER", clientNum);
			CCLAUtils.addQueryIntParamToBinder(binder, "ACCOUNTNUMBER", accountNum);
			CCLAUtils.addQueryParamToBinder(binder, "FUND_CODE", fundCode);
			CCLAUtils.addQueryParamToBinder(binder, "CASH", amountStr);
			CCLAUtils.addQueryParamToBinder(binder, "COMPANY_CODE", company);
			return facade.createResultSet("QGetPendingPreadviceDicondins", binder);
		} else {
			return null;
		}
	}
	
	
	private boolean allowedPendingTransactionChecks(String docClass) 
	{
		try {			
			SystemConfigVar configVar = SystemConfigVar.getCache().getCachedInstance(INSTR_ALLOWED_PENDING_TRANSACTION_CHECKS);
			
			//If the config var doesn't exist than don't allow checks.
			if (configVar==null || 
					StringUtils.stringIsBlank(configVar.getStringValue()) ||
					StringUtils.stringIsBlank(docClass)) {
				return false;
			}
			
			if (configVar.getStringValue().equals("ALL"))
				return true;
			
			if (configVar.getStringValue().indexOf(docClass)!=-1) {
				return true;
		 	} else {
		 		return false;
			}
		} catch (Exception e) {
			Log.error("Error checking for pending transaction checks",e);
			return false;
		}
	}
}
