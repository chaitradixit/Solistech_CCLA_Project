package com.ecs.ucm.ccla.filter;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.CacheManager;
import com.ecs.ucm.ccla.data.Audit;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.DateFormatter;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ExecutionContext;
import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.Workspace;
import intradoc.shared.FilterImplementor;

/** Hooks into the extraBeforeCacheLoadInit filter.
 *  
 *  Performs startup tasks required by CCLA_ClientServices component:
 *  
 *  1. Create Fund cache
 *  2. Initialises Audit
 *  
 */
public class StartupFilter implements FilterImplementor {

	public int doFilter(Workspace ws, DataBinder binder, ExecutionContext exc)
	 throws DataException, ServiceException {

		try {
			FWFacade facade = CCLAUtils.getFacade(ws, true);
			
			// 04/04/12 This will be lazy cached, loaded on demand.
			// Initialize all Cachables
			CacheManager.rebuildAll(ws);
			
			// Initialize static data audit tables
			Audit.initAudit(facade, "SDAUDIT");
			
		} catch (Exception e) {
			String msg = "Failed to build caches at server startup";
			
			Log.error(msg, e);
			throw new DataException(msg, e);
		}
		
		return FilterImplementor.CONTINUE;
	}

}
