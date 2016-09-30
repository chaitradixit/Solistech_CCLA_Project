package com.ecs.stellent.spp.filter;

import intradoc.common.ExecutionContext;
import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.Workspace;
import intradoc.shared.FilterImplementor;

import com.ecs.ucm.ccla.Globals.UCMFieldNames;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.LWDocument;

/** Linked to validateStandard filter event.
 *  
 *  Triggered before standard UCM validation takes place.
 *  
 * checkUpdateDocInfo
 * 
 */
public class PreUpdateFilter implements FilterImplementor {
	
	/**Called when updating doc info 
	 * 
	 * Performs the following operations:
	 * 	
	 *  1. 	Check for isSPP flag. If detected, it means the DOC_INFO has occurred via
	 *  	a webservice from SPP. Ensure dDocType hasn't been changed by running 
	 *  	DOC_INFO call first.
	 * 
	 * 	2. Adds createPrimaryMetaFile flag to binder if the document type
	 *     is ChildDocument. Ensures ChildDocument items will always
	 *     update/checkin successfully.
	 */
	public int doFilter(Workspace workspace, DataBinder binder, ExecutionContext exc)
	 throws DataException, ServiceException {
		
		String docType = binder.getLocal("dDocType");
		
		//DJ 14/11/09
		String isSPPStr = binder.getLocal("isSPP");
		
		boolean isSPP = (isSPPStr != null && isSPPStr.length() > 0);
		
		long startTime = System.currentTimeMillis();
		
		Log.debug("SPP_Integration PreUpdateFilter started");
		
		if(isSPP){
			Log.info("Checkin called from SPP");
			
			try {
				int dID = Integer.parseInt(binder.getLocal("dID"));
				
				LWDocument lwd = new LWDocument(dID, true);
				
				String dDocName = lwd.getAttribute("dDocName");
				
				String origDocType = lwd.getAttribute("dDocType");
				
				if(!docType.equals(origDocType)){
					Log.warn("dDocType for " + dDocName + " has changed from original " +
							"dDocType:" + origDocType + " to: " + docType +
							"Reverting dDocType to original." +
							"");
					//Do we want to revert the orig doc type automatically? Commenting out 
					//for now for safety.
					binder.putLocal("dDocType", origDocType);
					docType = origDocType;
				}
			} catch (Exception e) {
				Log.error("Unable to instantiate content item:" + e.getMessage(), e);
			}
		}
		//DJ END
		
		boolean isChildDoc = (docType!= null && docType.equals("ChildDocument"));
		
		// If this is a ChildDocument, it won't have any content of its own.
		// Add the createPrimaryMetaFile flag to the binder to ensure it
		// will checkin/update successfully, regardless of source.
		if (isChildDoc) {
			Log.debug("ChildDocument update detected - " +
			 "adding createPrimaryMetaFile flag to binder");
			binder.putLocal("createPrimaryMetaFile", "1");
		}
		
		long endTime = System.currentTimeMillis();
		
		Log.debug("SPP_Integration PreUpdateFilter completed, dDocName: "
		 + binder.getLocal(UCMFieldNames.DocName) + ", time taken: " + 
		 (endTime-startTime)/1000D + "s");
			
		return FilterImplementor.CONTINUE;
	}
}
