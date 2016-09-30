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

/** Models entries from the REF_INSTRUCTION_ACTION table.
 * 
 * @author Tom
 *
 */
public class InstructionAction {
	
	public static class Cols {
		public static final String ID = "INSTRUCTION_ACTION_ID";
		public static final String LABEL = "INSTRUCTION_ACTION_LABEL";
	}
	
	public static final InstructionAction RETRY = new InstructionAction(1, "Retry");
	
	public static final InstructionAction PASS 	= new InstructionAction(2, "Pass");
	public static final InstructionAction FAIL 	= new InstructionAction(3, "Fail");
	
	public static final InstructionAction APPROVE 	= new InstructionAction(4, "Approve");
	public static final InstructionAction REJECT 	= new InstructionAction(5, "Reject");
	
	public static final InstructionAction CANCEL 	= new InstructionAction(6, "Cancel");
	public static final InstructionAction SUSPEND   = new InstructionAction(7, "Suspend");
	
	public static final InstructionAction TERMINATE = new InstructionAction(8, "Terminate");
	
	public static final InstructionAction RESUBMIT	= new InstructionAction(9, "Resubmit");
	
	private int actionId;
	private String label;

	public InstructionAction(int actionId, String label) {
		this.actionId = actionId;
		this.label = label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	public String getLabel() {
		return label;
	}
	public void setActionId(int actionId) {
		this.actionId = actionId;
	}
	public int getActionId() {
		return actionId;
	}
	
	public boolean equals(InstructionAction action) {
		return this.getActionId() == action.getActionId();
	}
	
	public static Vector<InstructionAction> getAll(FWFacade facade) 
	 throws DataException {
		Vector<InstructionAction> actions = new Vector<InstructionAction>();
		
		DataResultSet rs = facade.createResultSet("qCommProc_GetAllInstructionActions",
		 new DataBinder());
		
		if (rs.first()) {
			do {
				actions.add(get(rs));
			} while (rs.next());
		}
		
		return actions;
	}
	
	public static InstructionAction get(DataResultSet rs) throws DataException {
		if (rs.isEmpty())
			return null;
		
		return new InstructionAction(
			DataResultSetUtils.getResultSetIntegerValue(rs, Cols.ID),
			DataResultSetUtils.getResultSetStringValue(rs, Cols.LABEL)
		);
	}
	
	/*
	 *  Caching stuff
	 */
	private static Cache CACHE = new Cache();
	
	public static Cachable<Integer, InstructionAction> getCache() {
		return CACHE;
	}
	
	/** Instruction Action cache implementor */
	private static class Cache extends Cachable<Integer, InstructionAction> {

		public Cache() {
			super("Instruction Action");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<InstructionAction> actions = InstructionAction.getAll(facade);
			
			HashMap<Integer, InstructionAction> newCache = 
			 new HashMap<Integer, InstructionAction>();
			
			for (InstructionAction action : actions) {
				newCache.put(action.getActionId(), action);
			}
			
			this.CACHE_MAP = newCache;
		}
	}
}
