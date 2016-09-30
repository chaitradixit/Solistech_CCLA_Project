package com.ecs.ucm.ccla.data.instruction;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.util.HashMap;
import java.util.Vector;

import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.DataResultSetUtils;
import com.ecs.utils.stellent.embedded.FWFacade;


/**
 * MODELS REF_INSTR_AUDIT_ACTION TABLE
 * @author Cam
 *
 */

public class InstructionAuditAction {

	/* **************** Constants **************** */
	//BINDER AND DB COLUMN
	public static final String ID = "INSTR_AUDIT_ACTION_ID";
	public static final String NAME = "INSTR_AUDIT_ACTION_NAME";
	public static final String DESCRIPTION = "DESCRIPTION";
	
	
	//values representing db values.
	
	/* Used when triggering an SPP job for an instruction. */
	public static final int ACTION_START_WORKFLOW 		= 1;
	
	/* Generic module transition actions */
	public static final int ACTION_EXIT_MODULE 			= 2;
	public static final int ACTION_ENTERED_MODULE 		= 3;
	public static final int ACTION_SUSPENDED 			= 4;
	public static final int ACTION_CREATED 				= 5;
	public static final int ACTION_APPLIED 				= 6;
	public static final int ACTION_SKIPPED 				= 7;
	public static final int ACTION_PASSED 				= 8;
	public static final int ACTION_FAILED 				= 9;
	
	public static final int ACTION_STATUS_UPDATE		= 10;
	
	/* Static Data Update actions */
	public static final int ACTION_SDU_AUTHORISED		= 11;
	public static final int ACTION_SDU_NOT_AUTHORISED	= 12;
	public static final int ACTION_SDU_AURORA_CREATE	= 13;
	public static final int ACTION_SDU_AURORA_UPDATE	= 14;
	public static final int ACTION_SDU_RESET_AUTHORISATION = 18;
	
	public static final int ACTION_TERMINATED			= 15;
	
	/* Used when triggering an SPP notification job based on an instruction. */
	public static final int ACTION_START_NOTIFICATION	= 16;
	
	public static final int ACTION_RESUBMITTED			= 17;
	
	//Cache Name
	public static final String CLASS_NAME = "Instruction Audit Action";
	
	//QUERIES
	private static final String GET_ALL_QUERY_NAME = "qCore_GetAllInstructionAuditAction";
	
	/* **************** Properties **************** */
	private int actionId;
	private String name;
	private String description;
	
	/* ********************** Constructor ************************* */
	/**
	 * Constructor
	 * @param actionId
	 * @param name
	 * @param description
	 */
	public InstructionAuditAction(int actionId, String name, String description) {
		this.setActionId(actionId);
		this.setName(name);
		this.setDescription(description);
	}

	public void setActionId(int actionId) {
		this.actionId = actionId;
	}

	public int getActionId() {
		return actionId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}	
	
	/**
	 * Gets a Vector of InstructionAuditAction.
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Vector<InstructionAuditAction> getAll(FWFacade facade) throws DataException {
		Vector<InstructionAuditAction> actions = new Vector<InstructionAuditAction>();
		
		DataResultSet rs = facade.createResultSet
		 (GET_ALL_QUERY_NAME, new DataBinder());
		
		if (rs.first()) {
			do {
				actions.add(get(rs));
			} while (rs.next());
		}
		return actions;
	}
	
	/**
	 * Returns a InstructionAuditAction object from the DataResultSet or null if empty
	 * @param rs
	 * @return
	 * @throws DataException
	 */
	public static InstructionAuditAction get(DataResultSet rs) throws DataException {
		if (rs.isEmpty())
			return null;
		
		return new InstructionAuditAction(
			DataResultSetUtils.getResultSetIntegerValue(rs, ID),
			DataResultSetUtils.getResultSetStringValue(rs, NAME),
			DataResultSetUtils.getResultSetStringValue(rs, DESCRIPTION)
		);
	}
	
	/* ************************ Caching ************************************** */	
	private static Cache CACHE = new Cache();
	
	public static Cachable<Integer, InstructionAuditAction> getCache() {
		return CACHE;
	}
	
	/** InstructionAuditAction cache implementor */
	private static class Cache extends Cachable<Integer, InstructionAuditAction> {

		public Cache() {
			super(CLASS_NAME);
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<InstructionAuditAction> actions = InstructionAuditAction.getAll(facade);
			
			HashMap<Integer, InstructionAuditAction> newCache = 
			 new HashMap<Integer, InstructionAuditAction>();
			
			for (InstructionAuditAction action : actions) {
				newCache.put(action.getActionId(), action);
			}
			
			this.CACHE_MAP = newCache;
		}
	}	
}
