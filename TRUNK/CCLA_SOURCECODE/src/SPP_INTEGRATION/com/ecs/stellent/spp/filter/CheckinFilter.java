package com.ecs.stellent.spp.filter;


import com.ecs.stellent.iris.batch.BatchDocumentServices;
import com.ecs.stellent.spp.service.BundlePriorityUtils;
import com.ecs.stellent.spp.service.BundleServices;

import com.ecs.stellent.spp.service.SppIntegrationUtils;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals.UCMFieldNames;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;

import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ExecutionContext;
import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.data.Workspace;
import intradoc.shared.FilterImplementor;
import intradoc.shared.SharedObjects;

public class CheckinFilter implements FilterImplementor {
	
	static String CHECKIN_STATUS = null;
	
	/** Called via checkin filter event validateCheckinData.
	 * 
	 *  1. Used to auto-populate xStatus field for newly-checked in
	 *     items of type Document/ChildDocument.
	 *  
	 *  2. Generates a new bundle reference if the 'addBundleRef'
	 *     flag is present in the binder.
	 */
	public int doFilter(Workspace workspace, DataBinder binder, ExecutionContext exc)
	 throws DataException, ServiceException {
		
		// Ensure this is checkin service (must contain the 'CHECKIN' substring
		// in the IdcService name)
		String idcService = binder.getLocal("IdcService");
		if (StringUtils.stringIsBlank(idcService) || idcService.indexOf("CHECKIN") == -1)
			return FilterImplementor.CONTINUE;
		
		long startTime = System.currentTimeMillis();
		
		Log.debug("SPP_Integration CheckinFilter started");
		
		String docType 		= binder.getLocal("dDocType");
		String status  		= binder.getLocal("xStatus");
		
		String checkinStatus = getCheckinStatus();
		
		boolean isBundleItem = !StringUtils.stringIsBlank(docType) &&
		 docType.equals("Bundle");
	
		boolean isSppItem = !StringUtils.stringIsBlank(docType) &&
		(docType.equals("Document") || docType.equals("ChildDocument"));
		
		// Only set the xStatus value if one is not already set
		// and the dDocType matches one of the valid SPP item
		// types.
		if (isSppItem && checkinStatus != null &&
			StringUtils.stringIsBlank(status)) {

			binder.putLocal("xStatus", checkinStatus);
		}
		
		// Attempt to set process date on this item, if it hasn't already
		// been set.
		// TM: this is now done when the bundle is pushed through workflow
		// steps.
		/*
		if (isSppItem && StringUtils.stringIsBlank(processDate)) {
			try {
				FWFacade facade = SppIntegrationUtils.getFacade(workspace);
				Date thisProcessDate = 
				 ProcessDateUtils.getProcessDate
				  (ProcessDateUtils.BASE_PROCESS_DATE_NAME, facade);
				
				if (thisProcessDate != null) {
					String dateStr = DateFormatter.getTimeStamp(thisProcessDate);
					Log.debug("Setting process date to: " + dateStr);
					
					binder.putLocal("xProcessDate", dateStr);
				}
				
			} catch (Exception e) {
				Log.error("Failed to lookup process date", e);
			}
		}
		*/
		
		// Only add a new bundle ref to the document metadata if a bundle
		// ref is not already present, and the 'addBundleRef' flag is present.
		boolean addBundleRef = !StringUtils.stringIsBlank(
		 binder.getLocal("addBundleRef"))
		 &&
		 StringUtils.stringIsBlank(binder.getLocal("xBundleRef"));
		
		FWFacade facade = null;
		
		if (addBundleRef || isBundleItem)
			facade = CCLAUtils.getFacade(workspace,false);
		
		if (addBundleRef) {
			Log.debug("Detected addBundleRef flag in binder, and no xBundleRef value. " +
			 "Attempting to generate new bundle ref");
			
			String source = binder.getLocal("xSource");
			
			try {
				String bundleRef = BundleServices.getNewBundleRef(source, CCLAUtils.getFacade(workspace, true));
				binder.putLocal("xBundleRef", bundleRef);
			} catch (DataException e) {
				Log.error("Failed to add new bundle ref: " + e.getMessage(), e);
			}
		}
		
		if (isBundleItem && BundlePriorityUtils.REFRESH_PRIORITY_ON_BUNDLE_CHECKIN) {
			if (!StringUtils.stringIsBlank(binder.getLocal("xBundleRef"))) {
				
				String bundleRef = binder.getLocal("xBundleRef");
				DataResultSet rsBatchItems = 
					BatchDocumentServices.getBatchItems(bundleRef, facade);
				
				Log.debug("Calculating bundle priority pre-checkin");
				
				int priority = BundlePriorityUtils.getPriority(binder, rsBatchItems);
				Log.debug("Assigning priority of " + priority + " to bundle: "+bundleRef);
				binder.putLocal("xPriority", Integer.toString(priority));
			}
			
			//No longer required
			//binder.putLocal("csBundleDoPostCheckinActions", "1");
		}		
		
		long endTime = System.currentTimeMillis();

		Log.debug("SPP_Integration CheckinFilter completed, dDocName: "
		 + binder.getLocal(UCMFieldNames.DocName) + ", time taken: " + 
		 (endTime-startTime)/1000D + "s");
		
		return FilterImplementor.CONTINUE;
	}
	
	/** Fetches the default check-in status value from an environment
	 *  variable.
	 *  
	 * @return
	 */
	private String getCheckinStatus() {
		
		if (CHECKIN_STATUS == null) {
			CHECKIN_STATUS = SharedObjects.getEnvironmentValue(
			 "CCLA_checkinItemStatus");
		
			if (CHECKIN_STATUS == null)
				Log.warn("Checkin Status is missing, unable to apply " +
				 "xStatus value to new content item");
		}
		
		return CHECKIN_STATUS;
	}
	
}
