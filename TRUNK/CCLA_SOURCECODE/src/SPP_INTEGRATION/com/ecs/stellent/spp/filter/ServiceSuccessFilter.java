package com.ecs.stellent.spp.filter;

import java.util.Date;

import com.ecs.stellent.iris.batch.BatchDocumentServices;
import com.ecs.stellent.spp.service.BundlePriorityUtils;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals.UCMFieldNames;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ExecutionContext;
import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.data.Workspace;
import intradoc.shared.FilterImplementor;

public class ServiceSuccessFilter implements FilterImplementor {
	
	/** Called via checkin filter event onEndServiceRequestActions
	 * 
	 *  Used to apply post-processing actions after a bundle is checked in.
	 *  
	 */
	public int doFilter(Workspace workspace, DataBinder binder, ExecutionContext exc)
	 throws DataException, ServiceException {
		
		// Check for special flag indicating that post-processing actions are
		// required for the form tagged to this document.
		boolean doBundlePostCheckin = 
		 !StringUtils.stringIsBlank(binder.getLocal("csBundleDoPostCheckinActions"));
		
		// Check for special flag indicating that the bundle priority must be 
		// refreshed.
		boolean refreshBundlePriority = 
		 !StringUtils.stringIsBlank(binder.getLocal("refreshBundlePriority"));
		
		if (!(doBundlePostCheckin || refreshBundlePriority))
			return FilterImplementor.CONTINUE;
		
		long startTime = System.currentTimeMillis();
		
		Log.debug("SPP_Integration ServiceSuccessFilter started");
		
		if (doBundlePostCheckin) {
			// There are no more bundle post-check-in actions. The commented-out code
			// below used to update the priority of bundles after they were checked
			// in. This functionality has now been added to the UpdateFilter instead.
			
			/*
			String bundleDocName = binder.getLocal("dDocName");
			
			try {
				LWDocument bundleDoc = new LWDocument(bundleDocName, true);
				
				DataResultSet rsBatchItems = BatchDocumentServices.getBatchItems
				 (bundleDoc.getAttribute("xBundleRef"), 
				 SppIntegrationUtils.getFacade(workspace), false);
				
				Log.debug("Found " + rsBatchItems.getNumRows() + " batch items" + 
				" with bundle ref: " + bundleDoc.getAttribute("xBundleRef"));
						
				int priority = BundlePriorityUtils.getPriority(bundleDoc, rsBatchItems);
				
				Log.debug("Assigning priority of " + priority + " to bundle: " 
				 + bundleDocName);
				
				bundleDoc.setAttribute("xPriority", Integer.toString(priority));
				bundleDoc.update();
				
			} catch (Exception e) {
				Log.error("Failed to assign bundle priority for: " + bundleDocName, e);
			}
			*/
			
			// Clear the bundle post check-in flag.
			binder.putLocal("csBundleDoPostCheckinActions", "");
		
		} else if (refreshBundlePriority) {
			
			String bundleRef = binder.getLocal("xBundleRef");

			if (!StringUtils.stringIsBlank(bundleRef)) {
				Log.debug("Refreshing priority for bundle " + bundleRef 
				 + " post-update");
				
				try {
					FWFacade facade = CCLAUtils.getFacade(workspace,false);
					LWDocument bundleDoc = 
					 BatchDocumentServices.getParentBatchDoc(bundleRef, facade);
					
					DataResultSet rsBatchItems = BatchDocumentServices.getBatchItems
					 (bundleRef, facade, false);
					
					Log.debug("Found " + rsBatchItems.getNumRows() + " batch items" + 
					" with bundle ref: " + bundleRef);
					
					BundlePriorityUtils.updateBundlePriority(bundleDoc, rsBatchItems);
	
				} catch (Exception e) {
					Log.error("Failed to assign bundle priority for: " + bundleRef, e);
				}
			
			} else {
				Log.warn("Unable to refresh bundle priority: xBundleRef missing");
			}
			
			// Clear the refresh priority flag.
			binder.putLocal("refreshBundlePriority", "");
		}

		long endTime = System.currentTimeMillis();

		Log.debug("SPP_Integration ServiceSuccessFilter completed, dDocName: "
		 + binder.getLocal(UCMFieldNames.DocName) + ", time taken: " + 
		 (endTime-startTime)/1000D + "s");
		
		return FilterImplementor.CONTINUE;
	}

}
