package com.ecs.stellent.ccla.clientservices.filter;

import java.util.HashMap;

import intradoc.common.ExecutionContext;
import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.Workspace;
import intradoc.shared.FilterImplementor;

import com.ecs.ucm.ccla.Globals.UCMFieldNames;
import com.ecs.ucm.ccla.data.Globals;
import com.ecs.ucm.ccla.filename.MetadataFromString;
import com.ecs.ucm.ccla.utils.DateUtils;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.LWDocument;

public class CheckinFilter implements FilterImplementor {
	
	/** Called via checkin filter event validateCheckinData
	 * 
	 *  Used to auto-complete metadata fields based on saved form data, if
	 *  a xFormId value is present in the binder.
	 *  
	 */
	public int doFilter(Workspace workspace, DataBinder binder, ExecutionContext exc)
	 throws DataException, ServiceException {
		
		// Ensure this is checkin service
		String idcService = binder.getLocal("IdcService");
		if (StringUtils.stringIsBlank(idcService) || idcService.indexOf("CHECKIN") == -1)
			return FilterImplementor.CONTINUE;

		long startTime = System.currentTimeMillis();
		
		Log.debug("CCLA_ClientServices CheckinFilter started");
		
		if (Globals.USE_STORAGE_RULES) 
		{
			if (StringUtils.stringIsBlank(binder.getLocal(UCMFieldNames.StorageRule)) &&
					StringUtils.stringIsBlank(binder.getLocal(UCMFieldNames.FolderName))) 
			{
				binder.putLocal(UCMFieldNames.StorageRule, Globals.STORAGE_RULE_NAME);
				binder.putLocal(UCMFieldNames.FolderName, DateUtils.getYearAndMonthStr());			
			}
		} 
		
		String docName = binder.getLocal(UCMFieldNames.DocName);
		boolean isNewDoc = false;
		
		if (!StringUtils.stringIsBlank(docName)) {
			try {
				LWDocument lwd = new LWDocument(docName, true);
			} catch (Exception e) {
				isNewDoc = true;
			}
		}
		
		if (isNewDoc) {
			// Determine whether or not we can apply metadata auto-fill from the 
			// filename data
			HashMap<String, String> autoFillMetadata = 
			 MetadataFromString.extractFromProperties(binder.getLocalData());
			
			if (autoFillMetadata != null) {
				for (String key : autoFillMetadata.keySet()) {
					binder.putLocal(key, autoFillMetadata.get(key));
				}
			}
		}
		
		// If we have a xFormId then update binder with form data
		String formIdStr = binder.getLocal("xFormId");
			
		if (StringUtils.stringIsBlank(formIdStr))
			return FilterImplementor.CONTINUE;
		
		Log.debug("Item being checked in with form ID: " + formIdStr + 
		 ". Will perform lookup post-checkin.");
		
		binder.putLocal("csFormCheckedIn", "1");
		
		long endTime = System.currentTimeMillis();
		
		Log.debug("ClientServices CheckinFilter completed, dDocName: "
		 + binder.getLocal(UCMFieldNames.DocName) + ", time taken: " + 
		 (endTime-startTime)/1000D + "s");

		return FilterImplementor.CONTINUE;
	}
}