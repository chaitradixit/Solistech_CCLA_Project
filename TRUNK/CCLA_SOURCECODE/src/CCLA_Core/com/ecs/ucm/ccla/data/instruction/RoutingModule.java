package com.ecs.ucm.ccla.data.instruction;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.ucm.ccla.commproc.modulehandler.AbstractModuleHandler;
import com.ecs.ucm.ccla.data.Persistable;
import com.ecs.utils.ClassLoaderUtils;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.DataResultSetUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Represents an entry from the INSTR_ROUTING_MODULE table.
 *  
 *  Each instance only stores the module ID, name, various InstructionStatuses and
 *  a handler class reference.
 *  
 *  If the handler class is non-null, a single instance of it is used for executing the 
 *  logic at each module step.
 *  
 *  If the handler class is null, the default RoutingRoutingHandler is used for 
 *  execution.
 * 
 * @author Tom
 *
 */
public class RoutingModule implements Persistable {
	
	private int moduleId;
	private String name;
	
	private InstructionStatus listenStatus;
	private InstructionStatus skipStatus;
	private InstructionStatus entryStatus;
	private InstructionStatus passStatus;
	private InstructionStatus failStatus;
	
	private boolean enabled;
	private AbstractModuleHandler moduleHandler;
	
	private Date lastUpdated;
	
	private Integer executionOrder;

	/** The available Rule Types, applied to CommRule instances */
	public static enum RuleType {
		Accept, Check
	}
	
	private HashMap<RuleType, Vector<ModuleRuleApplied>> rules;
	
	public static class Cols {
		public static final String ID = "MODULE_ID";
	}
	
	public RoutingModule(int moduleId, String name,
			InstructionStatus listenStatus, InstructionStatus skipStatus,
			InstructionStatus entryStatus, InstructionStatus passStatus,
			InstructionStatus failStatus, boolean enabled,
			AbstractModuleHandler moduleHandler, Date lastUpdated,
			Integer executionOrder,
			HashMap<RuleType, Vector<InstructionRule>> rules) throws DataException {
		this.moduleId = moduleId;
		this.name = name;
		this.listenStatus = listenStatus;
		this.skipStatus = skipStatus;
		this.entryStatus = entryStatus;
		this.passStatus = passStatus;
		this.failStatus = failStatus;
		this.enabled = enabled;
		this.moduleHandler = moduleHandler;
		this.lastUpdated = lastUpdated;
		this.executionOrder = executionOrder;
		
		this.moduleHandler.setRoutingModule(this);
		
		this.rules = new HashMap<RuleType, Vector<ModuleRuleApplied>>();
		
		// Fetch Rule mapping from cache
		Vector<ModuleRuleApplied> rulesApplied = 
		 ModuleRuleApplied.getCache().getCachedInstance(moduleId);
		
		if (rulesApplied == null)
			rulesApplied = new Vector<ModuleRuleApplied>();
		else {
			for (ModuleRuleApplied ruleApplied : rulesApplied) {
				RuleType ruleType = ruleApplied.getRuleType();
				
				InstructionRule rule = ruleApplied.getInstructionRule();
				
				if (rules.containsKey(ruleType)) {
					Vector<InstructionRule> v = rules.get(ruleType);
					v.add(rule);
				} else {
					Vector<InstructionRule> v = new Vector<InstructionRule>();
					v.add(rule);
					
					rules.put(ruleType, v);
				}
			}
		}
	}

	public int getModuleId() {
		return moduleId;
	}

	public void setModuleId(int moduleId) {
		this.moduleId = moduleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public void setRules(HashMap<RuleType, Vector<ModuleRuleApplied>> rules) {
		this.rules = rules;
	}

	public HashMap<RuleType, Vector<ModuleRuleApplied>> getRules() {
		return rules;
	}
	
	public Vector<ModuleRuleApplied> getRulesByType(RuleType ruleType) {
		Vector<ModuleRuleApplied> rulesByType = rules.get(ruleType);
		return rulesByType;
	}
	
	@SuppressWarnings("unchecked")
	public static RoutingModule get(DataResultSet rs) throws DataException {
		
		if (rs.isEmpty())
			return null;
		
		Integer listenStatusId 	= DataResultSetUtils.getResultSetIntegerValue
		 (rs, "LISTEN_STATUS_ID");
		Integer skipStatusId 	= DataResultSetUtils.getResultSetIntegerValue
		 (rs, "SKIP_STATUS_ID");
		Integer entryStatusId 	= DataResultSetUtils.getResultSetIntegerValue
		 (rs, "ENTRY_STATUS_ID");
		Integer passStatusId 	= DataResultSetUtils.getResultSetIntegerValue
		 (rs, "PASS_STATUS_ID");
		Integer failStatusId 	= DataResultSetUtils.getResultSetIntegerValue
		 (rs, "FAIL_STATUS_ID");

		AbstractModuleHandler moduleHandler = null;
		
		String handlerClassName = 
		 DataResultSetUtils.getResultSetStringValue(rs, "HANDLER_CLASS");

		if (!StringUtils.stringIsBlank(handlerClassName)) {
			try {
				// Attempt to load the Class from its name.
				Class<? extends AbstractModuleHandler> moduleHandlerClass =  
				 (Class<? extends AbstractModuleHandler>)
				 ClassLoaderUtils.getComponentClassLoader().loadClass(handlerClassName);
				
				moduleHandler = moduleHandlerClass.newInstance();
				
			} catch (Exception e) {
				String msg = "Failed to load Routing Module handler class: " + 
				handlerClassName + ". Ensure the Java class is visible.";
				
				Log.error(msg, e);
				throw new DataException(msg, e);
			}
		} else {
			throw new DataException("No Module Handler Class set for module: " 
			 + DataResultSetUtils.getResultSetStringValue(rs, "MODULE_NAME"));
		}
		
		return new RoutingModule(
			DataResultSetUtils.getResultSetIntegerValue(rs, Cols.ID),
			DataResultSetUtils.getResultSetStringValue(rs, "MODULE_NAME"),
			getStatusAllowNull(listenStatusId),
			getStatusAllowNull(skipStatusId),
			getStatusAllowNull(entryStatusId),
			getStatusAllowNull(passStatusId),
			getStatusAllowNull(failStatusId),
			DataResultSetUtils.getResultSetBoolValue(rs, "IS_ENABLED"),
			moduleHandler,
			rs.getDateValueByName("LAST_UPDATED"),
			DataResultSetUtils.getResultSetIntegerValue(rs, "EXECUTION_ORDER"),
			null
		);
	}
	
	/**
	 * Fetches a routing module object based on a routingModuleId 
	 * @param routingModuleId
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static RoutingModule get(Integer routingModuleId, FWFacade facade) 
			throws DataException {
		DataResultSet rsRoutingModule = getData(routingModuleId, facade);
		return get(rsRoutingModule);

	}
	
	/**
	 * Executes the query to obtain the data from the database from a given
	 * routing moduleId
	 * @param routingModuleId
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	private static DataResultSet getData(Integer routingModuleId,
			FWFacade facade) throws DataException {

		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.ID, routingModuleId);
		
		return facade.createResultSet
		("qCommProc_GetModuleInfo", binder);
		
	}

	/** Helper method for fetching cached Instruction Statuses from the various Status
	 *  ID values in this object.
	 *  
	 * @param statusId
	 * @return
	 * @throws DataException 
	 */
	private static InstructionStatus getStatusAllowNull(Integer statusId) 
	 throws DataException {
		
		if (statusId == null)
			return null;
		
		return InstructionStatus.getCache().getCachedInstance(statusId);
	}
	
	private static Integer getStatusIntegerAllowNull(InstructionStatus status) 
	 throws DataException {
		
		if (status == null)
			return null;
		
		return status.getInstructionStatusId();
	}
	
	public static DataResultSet getAllData(FWFacade facade) throws DataException {
		DataBinder binder = new DataBinder();
		
		return facade.createResultSet("qCommProc_GetAllRoutingModules", binder);
	}
	
	/** Fetches all RoutingModule instances, i.e. one for every row in the 
	 *  COMM_ROUTING_MODULE table.
	 *  
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static Vector<RoutingModule> getAll(FWFacade facade) throws DataException {
		Vector<RoutingModule> modules = new Vector<RoutingModule>();
		
		DataResultSet rsModules = getAllData(facade);
		
		if (rsModules.first()) {
			do {
				modules.add(get(rsModules));
			} while (rsModules.next());
		}
		
		return modules;
	}
	
	/** Fetches a list of all pending Instructions which are eligible for this
	 *  RoutingModule, i.e. Instructions with a status matching the module's accept
	 *  status.
	 *  
	 * @param maxInstructionCount limit for the total number of instructions that will
	 * 							  be fetched
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public Vector<Instruction> getPendingInstructions
	 (int maxInstructionCount, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		BinderUtils.addIntParamToBinder
		 (binder, "INSTRUCTION_STATUS_ID", 
		 this.getListenStatus().getInstructionStatusId());
		
		BinderUtils.addIntParamToBinder
			(binder,"NUM_INSTRUCTIONS_TO_PROCESS", maxInstructionCount);
		
		DataResultSet rsPendingInstrs = facade.createResultSet
		 ("qCommProc_GetPendingInstructionsByStatusId", binder);
		
		Vector<Instruction> pendingInstrs = new Vector<Instruction>();
		
		if (rsPendingInstrs.first()) {
			do {
				pendingInstrs.add(Instruction.get(rsPendingInstrs));
			} while (rsPendingInstrs.next());
		}
		
		return pendingInstrs;
	}


	
	/*
	 *  Caching stuff
	 */
	private static Cache CACHE = new Cache();
	
	public static Cachable<Integer, RoutingModule> getCache() {
		return CACHE;
	}
	
	public void setExecutionOrder(Integer executionOrder) {
		this.executionOrder = executionOrder;
	}

	public Integer getExecutionOrder() {
		if(executionOrder==null){
			return null; 
		}
		return executionOrder;
	}

	/** Condition Operator cache implementor */
	private static class Cache extends Cachable<Integer, RoutingModule> {

		public Cache() {
			super("Instruction Routing Module");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<RoutingModule> modules = RoutingModule.getAll(facade);
			
			HashMap<Integer, RoutingModule> newCache = 
			 new HashMap<Integer, RoutingModule>();
			
			for (RoutingModule module : modules) {
				newCache.put(module.getModuleId(), module);
			}
			
			this.CACHE_MAP = newCache;
		}
	}

	public void addFieldsToBinder(DataBinder binder) throws DataException {
		
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.ID, this.getModuleId());
		CCLAUtils.addQueryParamToBinder(binder, "MODULE_NAME", this.getName());
		
		CCLAUtils.addQueryIntParamToBinder(binder, "LISTEN_STATUS_ID", 
				getStatusIntegerAllowNull(this.getListenStatus()));

		CCLAUtils.addQueryIntParamToBinder(binder, "SKIP_STATUS_ID", 
				getStatusIntegerAllowNull(this.getSkipStatus()));
		
		CCLAUtils.addQueryIntParamToBinder(binder, "ENTRY_STATUS_ID",
				getStatusIntegerAllowNull(this.getEntryStatus()));
		
		CCLAUtils.addQueryIntParamToBinder(binder, "PASS_STATUS_ID",
				getStatusIntegerAllowNull(this.getPassStatus()));

		CCLAUtils.addQueryIntParamToBinder(binder, "FAIL_STATUS_ID", 
				getStatusIntegerAllowNull(this.getFailStatus()));
		
		CCLAUtils.addQueryBooleanParamToBinder(binder, "IS_ENABLED", this.isEnabled());
		
		CCLAUtils.addQueryParamToBinder
		 (binder, "HANDLER_CLASS", this.moduleHandler.getClass().getName());
		
		CCLAUtils.addQueryIntParamToBinder(binder, "EXECUTION_ORDER", this.getExecutionOrder());
		
	}

	
	/** TODO: write query and refresh Cache!!
	 * 
	 */
	public void persist(FWFacade facade, String username) throws DataException {
		DataBinder binder = new DataBinder();
		
		this.validate(facade);
		this.addFieldsToBinder(binder);
		
		facade.execute("qCommProc_UpdateRoutingModule", binder);
		
		//Rebuild the Routing Module cache
		RoutingModule.getCache().buildCache(facade);
	}  

	/**
	 * sets the attribute of a Routing Module from the given databinder
	 * 
	 * @param DataBinder binder
	 */
	@SuppressWarnings("unchecked")
	public void setAttributes(DataBinder binder) throws DataException {
		
		this.setModuleId(CCLAUtils.getBinderIntegerValue(binder, Cols.ID));
		
		this.setName(binder.getLocal("MODULE_NAME"));
		
		InstructionStatus listenStatus = 
			getStatusAllowNull(CCLAUtils.getBinderIntegerValue(binder, "LISTEN_STATUS_ID"));
		if(listenStatus!=null)
			this.setListenStatus(listenStatus);

		InstructionStatus skipStatus = 
			getStatusAllowNull(CCLAUtils.getBinderIntegerValue(binder, "SKIP_STATUS_ID"));
		this.setSkipStatus(skipStatus);
		
		InstructionStatus entryStatus = 
			getStatusAllowNull(CCLAUtils.getBinderIntegerValue(binder, "ENTRY_STATUS_ID"));
			this.setEntryStatus(entryStatus);

		InstructionStatus passStatus = 
			getStatusAllowNull(CCLAUtils.getBinderIntegerValue(binder, "PASS_STATUS_ID"));
			this.setPassStatus(passStatus);
		
		InstructionStatus failStatus = 
			getStatusAllowNull(CCLAUtils.getBinderIntegerValue(binder, "FAIL_STATUS_ID"));

		this.setFailStatus(failStatus);	
		
		this.setEnabled(CCLAUtils.getBinderBoolValue(binder, "IS_ENABLED"));
		
		String handlerClassName = binder.getLocal("HANDLER_CLASS");
		
		if (!StringUtils.stringIsBlank(handlerClassName)) {
			try {
				// Attempt to load the Class from its name.
				Class<? extends AbstractModuleHandler> moduleHandlerClass =  
				 (Class<? extends AbstractModuleHandler>)
				 ClassLoaderUtils.getComponentClassLoader().loadClass(handlerClassName);
				
				moduleHandler = moduleHandlerClass.newInstance();
				
			} catch (Exception e) {
				String msg = "Failed to load Routing Module handler class: " + 
				handlerClassName + ". Ensure the Java class is visible.";
				
				Log.error(msg, e);
				throw new DataException(msg, e);
			}
		} else {
			throw new DataException("No Module Handler Class set for module: " 
			 + this.getName());
		}
		
		this.setExecutionOrder(
				CCLAUtils.getBinderIntegerValue(binder, "EXECUTION_ORDER"));
		
	}
	

	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub
		
	}

	
	public InstructionStatus getListenStatus() {
		return listenStatus;
	}

	public void setListenStatus(InstructionStatus listenStatus) {
		this.listenStatus = listenStatus;
	}

	public InstructionStatus getSkipStatus() {
		return skipStatus;
	}

	public void setSkipStatus(InstructionStatus skipStatus) {
		this.skipStatus = skipStatus;
	}

	public InstructionStatus getEntryStatus() {
		return entryStatus;
	}

	public void setEntryStatus(InstructionStatus entryStatus) {
		this.entryStatus = entryStatus;
	}

	public InstructionStatus getPassStatus() {
		return passStatus;
	}

	public void setPassStatus(InstructionStatus passStatus) {
		this.passStatus = passStatus;
	}

	public InstructionStatus getFailStatus() {
		return failStatus;
	}

	public void setFailStatus(InstructionStatus failStatus) {
		this.failStatus = failStatus;
	}

	public void setModuleHandler(AbstractModuleHandler moduleHandler) {
		this.moduleHandler = moduleHandler;
	}

	public AbstractModuleHandler getModuleHandler() {
		return moduleHandler;
	}
	
	public boolean equals(RoutingModule module) {
		return this.getModuleId() == module.getModuleId();
	}
	
}
