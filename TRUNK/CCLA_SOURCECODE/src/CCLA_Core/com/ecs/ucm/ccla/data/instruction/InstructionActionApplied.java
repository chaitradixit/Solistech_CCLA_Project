package com.ecs.ucm.ccla.data.instruction;

import java.util.HashMap;
import java.util.Vector;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.ucm.ccla.data.Persistable;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.DataResultSetUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Models entries from the INSTR_PROCESS_ACTION_APPLIED table, i.e. the available
 *  Instruction Actions for each Instruction Process.
 * 
 * @author Tom
 *
 */
public class InstructionActionApplied implements Persistable {

	private int instructionProcessId;
	private int instructionActionId;

	private String label;
	private String descrption;
	
	public InstructionActionApplied(int instructionProcessId,
			int instructionActionId, String label, String descrption) {
		this.instructionProcessId = instructionProcessId;
		this.setInstructionActionId(instructionActionId);
		this.label = label;
		this.descrption = descrption;
	}
	
	public static InstructionActionApplied get(DataResultSet rs) throws DataException {
		if (rs.isEmpty())
			return null;
		
		return new InstructionActionApplied(
			DataResultSetUtils.getResultSetIntegerValue(rs, "INSTRUCTION_PROCESS_ID"),
			DataResultSetUtils.getResultSetIntegerValue(rs, "INSTRUCTION_ACTION_ID"),
			
			DataResultSetUtils.getResultSetStringValue(rs, "APPLIED_ACTION_LABEL"),
			DataResultSetUtils.getResultSetStringValue(rs, "APPLIED_ACTION_DESCRIPTION")
		);
	}
	
	public static Vector<InstructionActionApplied> getAll(FWFacade facade)
	 throws DataException {
		Vector<InstructionActionApplied> objs = new Vector<InstructionActionApplied>();
		
		DataResultSet rs = facade.createResultSet
		 ("qCommProc_GetAllInstructionActionApplied", new DataBinder());
		
		if (rs.first()) {
			do {
				objs.add(get(rs));
			} while (rs.next());
		}
		
		return objs;
	}
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		// TODO Auto-generated method stub

	}

	public void persist(FWFacade facade, String username) throws DataException {
		// TODO Auto-generated method stub

	}

	public void setAttributes(DataBinder binder) throws DataException {
		// TODO Auto-generated method stub

	}

	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub

	}

	public int getInstructionProcessId() {
		return instructionProcessId;
	}

	public void setInstructionProcessId(int instructionProcessId) {
		this.instructionProcessId = instructionProcessId;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDescrption() {
		return descrption;
	}

	public void setDescrption(String descrption) {
		this.descrption = descrption;
	}

	public void setInstructionActionId(int instructionActionId) {
		this.instructionActionId = instructionActionId;
	}

	public int getInstructionActionId() {
		return instructionActionId;
	}
	
	/*
	 *  Caching stuff
	 */
	private static Cache CACHE = new Cache();
	
	/** Fetches a mapping of Instruction Process IDs -> list of available Instruction
	 *  Actions.
	 *  
	 * @return
	 */
	public static Cachable<Integer, Vector<InstructionActionApplied>> getCache() {
		return CACHE;
	}
	
	/** Instruction Process cache implementor */
	private static class Cache 
	 extends Cachable<Integer, Vector<InstructionActionApplied>> {

		public Cache() {
			super("Instruction Action Applied");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<InstructionActionApplied> availActions = 
			 InstructionActionApplied.getAll(facade);
			
			HashMap<Integer, Vector<InstructionActionApplied>> newCache = 
			 new HashMap<Integer, Vector<InstructionActionApplied>>();
			
			for (InstructionActionApplied availAction : availActions) {
				
				int thisProcessId = availAction.getInstructionProcessId();
				
				if (newCache.containsKey(thisProcessId)) {
					newCache.get(thisProcessId).add(availAction);
				} else {
					Vector<InstructionActionApplied> v = 
					 new Vector<InstructionActionApplied>();
					
					v.add(availAction);
					newCache.put(thisProcessId, v);
				}
			}
			
			this.CACHE_MAP = newCache;
		}
	}


}
