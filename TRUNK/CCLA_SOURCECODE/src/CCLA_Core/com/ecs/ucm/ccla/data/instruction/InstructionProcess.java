package com.ecs.ucm.ccla.data.instruction;

import java.util.HashMap;
import java.util.Vector;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.ucm.ccla.data.Persistable;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.DataResultSetUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Models entries from the REF_INSTRUCTION_PROCESS table.
 * 
 * @author Tom
 *
 */
public class InstructionProcess implements Persistable {
	
	private int instructionProcessId;
	private String name;
	private String description;

	private String[] userGroups;
	private String idocInclude;
	
	public static class Cols {
		public static final String ID = "INSTRUCTION_PROCESS_ID";
	}
	
	public static class ProcessIds {
		public static final int DEFAULT_SUSPEND_PROCESS 		= 1;
		public static final int SPP_RELEASE_FAILURE 			= 2;
		public static final int SDU_AURORA_EXECUTION_FAILURE 	= 3;
	}
	
	public InstructionProcess(int instructionProcessId, String name,
			String description, String[] userGroups, String idocInclude) {
		this.instructionProcessId = instructionProcessId;
		this.name = name;
		this.description = description;
		this.userGroups = userGroups;
		this.idocInclude = idocInclude;
	}
	
	public static Vector<InstructionProcess> getAll(FWFacade facade) 
	 throws DataException {
		
		DataResultSet rs = facade.createResultSet
		 ("qCommProc_GetAllInstructionProcesses", new DataBinder());
		
		Vector<InstructionProcess> procs = new Vector<InstructionProcess>();
		
		if (rs.first()) {
			do {
				procs.add(get(rs));
			} while (rs.next());
		}
		
		return procs;
	}
	
	public static InstructionProcess get(DataResultSet rs) throws DataException {
		if (rs.isEmpty())
			return null;
		
		String[] userGroups = null;
		String userGroupsStr = DataResultSetUtils.getResultSetStringValue
		 (rs, "USER_GROUPS");
		
		if (userGroupsStr != null)
			userGroups = userGroupsStr.split(",");
		else
			userGroups = new String[0];
		
		return new InstructionProcess(
			DataResultSetUtils.getResultSetIntegerValue(rs, Cols.ID),
			DataResultSetUtils.getResultSetStringValue(rs, "PROCESS_NAME"),
			DataResultSetUtils.getResultSetStringValue(rs, "PROCESS_DESCRIPTION"),
			userGroups,
			DataResultSetUtils.getResultSetStringValue(rs, "IDOC_INCLUDE")
		);
	}
	
	/** Returns a list of available Instruction Actions for this process from the
	 *  cache.
	 *  
	 * @return
	 * @throws DataException
	 */
	public Vector<InstructionActionApplied> getAvailableActions() throws DataException {
		return InstructionActionApplied.getCache().getCachedInstance(
		 this.getInstructionProcessId()
		);
	}
	
	/** Returns a ResultSet of available Instruction Actions for this process from the
	 *  DB.
	 *  
	 * @return
	 * @throws DataException
	 */
	public DataResultSet getAvailableActionsData(FWFacade facade) 
	 throws DataException {

		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.ID, 
		 this.getInstructionProcessId());
		
		return facade.createResultSet
		 ("qCommProc_GetInstructionProcessActionApplied", binder);
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String[] getUserGroups() {
		return userGroups;
	}

	public void setUserGroups(String[] userGroups) {
		this.userGroups = userGroups;
	}

	public String getIdocInclude() {
		return idocInclude;
	}

	public void setIdocInclude(String idocInclude) {
		this.idocInclude = idocInclude;
	}
	
	/*
	 *  Caching stuff
	 */
	private static Cache CACHE = new Cache();
	
	public static Cachable<Integer, InstructionProcess> getCache() {
		return CACHE;
	}
	
	/** Instruction Process cache implementor */
	private static class Cache extends Cachable<Integer, InstructionProcess> {

		public Cache() {
			super("Instruction Process");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<InstructionProcess> procs = InstructionProcess.getAll(facade);
			
			HashMap<Integer, InstructionProcess> newCache = 
			 new HashMap<Integer, InstructionProcess>();
			
			for (InstructionProcess process : procs) {
				newCache.put(process.getInstructionProcessId(), process);
			}
			
			this.CACHE_MAP = newCache;
		}
	}

}
