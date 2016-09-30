package com.ecs.stellent.spp.filter;

import com.ecs.stellent.spp.data.SPPJobProfile;
import com.ecs.stellent.spp.service.BundlePriorityUtils;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ExecutionContext;
import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.Workspace;
import intradoc.shared.FilterImplementor;

/** Hooks into the extraBeforeCacheLoadInit filter.
 *  
 *  Performs startup tasks required by SPP_INTEGRATION component:
 *  
 *  1. Initialise Job Priority cache
 *  2. Start up the BundlePriorityThread
 */
public class StartupFilter implements FilterImplementor {

	public int doFilter
	 (Workspace workspace, DataBinder binder, ExecutionContext exc)
	 throws DataException, ServiceException {
		
		FWFacade facade = CCLAUtils.getFacade(workspace,false);
		
		// Initialise static WorkflowProfile instances.
		SPPJobProfile.init();
		
		// Initialise the Job Priority Rules cache.
		BundlePriorityUtils.refreshJobPriorityRuleCache(facade);

		return FilterImplementor.CONTINUE;
	}
	
}
