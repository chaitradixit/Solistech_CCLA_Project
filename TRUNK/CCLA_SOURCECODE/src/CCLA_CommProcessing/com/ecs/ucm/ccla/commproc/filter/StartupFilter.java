package com.ecs.ucm.ccla.commproc.filter;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.CacheManager;
import com.ecs.ucm.ccla.commproc.RoutingModuleManager;
import com.ecs.ucm.ccla.data.Operator;
import com.ecs.ucm.ccla.data.comm.CommSource;
import com.ecs.ucm.ccla.data.comm.CommStatus;
import com.ecs.ucm.ccla.data.comm.CommType;
import com.ecs.ucm.ccla.data.instruction.InstructionCondition;
import com.ecs.ucm.ccla.data.instruction.InstructionRule;
import com.ecs.ucm.ccla.data.instruction.InstructionRuleConditionApplied;
import com.ecs.ucm.ccla.data.instruction.UCMMetadataTranslation;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ExecutionContext;
import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.Workspace;
import intradoc.shared.FilterImplementor;

/** Hooks into the extraBeforeCacheLoadInit filter.
 *  
 *  Performs startup tasks required by CCLA_CommProcessing component:
 *  
 *  1. Initialize the Routing Module Manager thread
 *  
 *  This filter must be executed AFTER the CCLA_Core startup filter, as it relies on
 *  the Cachables that are stored in CCLA_Core.
 *  
 */
public class StartupFilter implements FilterImplementor {

	public int doFilter(Workspace ws, DataBinder binder, ExecutionContext exc)
	 throws DataException, ServiceException {
		
		FWFacade facade = null;
		
		try {			
			facade = CCLAUtils.getFacade(ws, true);
			//04/04/12 Lazy caching.
			// Load comm/instruction data caches
			// Communication data caches
			CacheManager.addCacheAndInit(CommSource.getCache(), facade);
			CacheManager.addCacheAndInit(CommType.getCache(), facade);
			CacheManager.addCacheAndInit(CommStatus.getCache(), facade);
			// Misc data caches
			CacheManager.addCacheAndInit(Operator.getCache(), facade);
			
			// Instruction data caches
			CacheManager.addCacheAndInit(UCMMetadataTranslation.getCache(), facade);
			CacheManager.addCacheAndInit(UCMMetadataTranslation.getUCMFieldCache(), facade);						
			CacheManager.addCacheAndInit(InstructionRule.getCache(), facade);
			CacheManager.addCacheAndInit(InstructionCondition.getCache(), facade);
			CacheManager.addCacheAndInit(InstructionRuleConditionApplied.getCache(), facade);
			
			// Initialize the singleton RoutingModuleManager
			RoutingModuleManager routingModuleManager = new RoutingModuleManager();
			routingModuleManager.init(facade);
			
			// Kick off its thread
			Thread thread = new Thread(routingModuleManager);
			thread.setName("RoutingModuleManager");
			
			thread.start();
			
		} catch (Exception e) {
			String msg = "Failed to initialize Routing Module Manager";
			
			Log.error(msg, e);
			throw new DataException(msg, e);
		} finally {
			if (facade != null)
				facade.releaseConnection();
		}
		
		return FilterImplementor.CONTINUE;
	}

}
