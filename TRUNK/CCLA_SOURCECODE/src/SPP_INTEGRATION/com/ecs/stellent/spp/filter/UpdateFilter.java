package com.ecs.stellent.spp.filter;

import intradoc.common.ExecutionContext;
import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.Workspace;
import intradoc.shared.FilterImplementor;
import intradoc.shared.SharedObjects;

import java.text.DecimalFormat;

import com.ecs.stellent.spp.service.BundlePriorityUtils;
import com.ecs.stellent.spp.service.SppIntegrationUtils;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals.UCMFieldNames;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;

/** Contains a metadata update filter, used for cleansing CCLA data.
 * 
 * @author Tom Marchant
 *
 */
public class UpdateFilter implements FilterImplementor {

	/** Filter binding: postValidateCheckinData.
	 * 
	 *  Called after standard UCM validation has occurred.
	 * 
	 *  Checks for an env. flag, if present it will force a refresh of a bundle's 
	 *  Priority value if the xDocumentClass value has been updated.
	 *  
	 *  The method can only know if the Document Class was updated if the 
	 *  xDocumentClass_OLD field value is present in the binder. This will only be
	 *  present on update calls originating from Iris.
	 *  
	 *  If a Doc Class update was detected, a flag called refreshBundlePriority is
	 *  dropped into the binder, which will get picked up by the ServiceSuccessFilter
	 *  and update the bundle priority.
	 *  
	 */
	public int doFilter(Workspace arg0, DataBinder binder, ExecutionContext arg2) 
	 throws DataException, ServiceException {
		
		if (BundlePriorityUtils.REFRESH_PRIORITY_ON_DOC_CLASS_UPDATE) {
			long startTime = System.currentTimeMillis();
			
			Log.debug("SPP_Integration UpdateFilter started");
			
			// Check for an updated Document Class. If so, the bundle priority must be
			// re-evaluated in the ServiceSuccessFilter.
			
			// In order to do the difference check, there must be an xDocumentClass_OLD
			// value in the binder. This is always present on update requests coming
			// from the Iris Doc Update panel. If the document class wasn't set when
			// the document was displayed in this panel, the _OLD field will be set to
			// '[NONE]'
			String oldDocClass = binder.getLocal("xDocumentClass_OLD");
			String newDocClass = binder.getLocal("xDocumentClass");
			
			if (!StringUtils.stringIsBlank(oldDocClass)) {
				
				Log.debug("Checking for Doc Class update. " +
				 "OldDocClass=" + oldDocClass + ", newDocClass=" + newDocClass);
				
				// Check for 2 conditions:
				// (Old Doc Class = [NONE] AND New Doc Class = non-empty)
				// OR
				// (Old Doc Class != [NONE] AND Old Doc Class != New Doc Class)
				if ((oldDocClass.equals("[NONE]") 
					&& 
					!StringUtils.stringIsBlank(newDocClass))
					||
					(!oldDocClass.equals("[NONE]")
					&&
					!oldDocClass.equals(newDocClass))) {
					// Document class has been updated in Iris.
					Log.debug("Document class has been updated. " +
					 "Refreshing bundle priority on service completion.");
					binder.putLocal("refreshBundlePriority", "1");
				} else {
					Log.debug("No update detected.");
				}
			}
			
			long endTime = System.currentTimeMillis();
			
			Log.debug("SPP_Integration UpdateFilter completed, dDocName: "
			 + binder.getLocal(UCMFieldNames.DocName) + ", time taken: " + 
			 (endTime-startTime)/1000D + "s");
		}
		
		return FilterImplementor.CONTINUE;
	}
}
