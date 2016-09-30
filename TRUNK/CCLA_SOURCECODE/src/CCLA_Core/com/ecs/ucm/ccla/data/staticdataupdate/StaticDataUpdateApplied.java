package com.ecs.ucm.ccla.data.staticdataupdate;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.ucm.ccla.data.instruction.InstructionType;
import com.ecs.utils.stellent.embedded.FWFacade;

public class StaticDataUpdateApplied {


	/** Database Columns **/
	public static final class Cols {
		public static final String INSTRUCTION_TYPE_ID 	= "INSTRUCTION_TYPE_ID";
		public static final String EXECUTION_ORDER 		= "EXECUTION_ORDER";
		public static final String SDU_ID 				= "SDU_ID";
		public static final String ALLOW_CREATE 		= "ALLOW_CREATE";
		public static final String ALLOW_UPDATE 		= "ALLOW_UPDATE";
	}
	
	/** Queries to use **/
	public static final class Queries {
		public static final String GET_ALL_QUERY = "qCore_GetAllSDUApplied";
	}

	/** Properties **/
	private int instrTypeId;
	private int sduId;
	private int executionOrder;
	private boolean allowUpdate;
	private boolean allowCreate;
	
	
	public StaticDataUpdateApplied(int instrTypeId, int sduId,
			int executionOrder, boolean allowUpdate, boolean allowCreate) {
		super();
		this.instrTypeId = instrTypeId;
		this.sduId = sduId;
		this.executionOrder = executionOrder;
		this.allowUpdate = allowUpdate;
		this.allowCreate = allowCreate;
	}
	
	
	public int getInstrTypeId() {
		return instrTypeId;
	}
	public void setInstrTypeId(int instrTypeId) {
		this.instrTypeId = instrTypeId;
	}
	public int getSduId() {
		return sduId;
	}
	public void setSduId(int sduId) {
		this.sduId = sduId;
	}
	public int getExecutionOrder() {
		return executionOrder;
	}
	public void setExecutionOrder(int executionOrder) {
		this.executionOrder = executionOrder;
	}
	public boolean isAllowUpdate() {
		return allowUpdate;
	}
	public void setAllowUpdate(boolean allowUpdate) {
		this.allowUpdate = allowUpdate;
	}
	public boolean isAllowCreate() {
		return allowCreate;
	}
	public void setAllowCreate(boolean allowCreate) {
		this.allowCreate = allowCreate;
	}

	
	/**
	 * Gets a Vector of StaticDataUpdateApplied.
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Vector<StaticDataUpdateApplied> getAll(FWFacade facade) throws DataException {
		Vector<StaticDataUpdateApplied> stdVec = new Vector<StaticDataUpdateApplied>();
		
		DataResultSet rs = facade.createResultSet
		 (Queries.GET_ALL_QUERY, new DataBinder());
		
		if (rs.first()) {
			do {
				stdVec.add(get(rs));
			} while (rs.next());
		}
		return stdVec;
	}
	
	/**
	 * Returns a StaticDataUpdateApplied object from the DataResultSet or null if empty
	 * @param rs
	 * @return
	 * @throws DataException
	 */
	public static StaticDataUpdateApplied get(DataResultSet rs) throws DataException {
		if (rs.isEmpty())
			return null;
		
		return new StaticDataUpdateApplied(
			CCLAUtils.getResultSetIntegerValue(rs, Cols.INSTRUCTION_TYPE_ID),	
			CCLAUtils.getResultSetIntegerValue(rs, Cols.SDU_ID),
			CCLAUtils.getResultSetIntegerValue(rs, Cols.EXECUTION_ORDER),
			CCLAUtils.getResultSetBoolValue(rs, Cols.ALLOW_CREATE),
			CCLAUtils.getResultSetBoolValue(rs, Cols.ALLOW_UPDATE)
		);
	}	
	
	/** Awkwardly named method which determines whether the passed Instruction Type may
	 *  yield Static Data Update instructions, i.e. does it have any SDU Instructions
	 *  applied against it in the table cache.
	 *  
	 *  So it will return true for Mandate, App etc.
	 *  
	 * @param instrType
	 * @return
	 */
	public static boolean isInstructionGeneratingStaticDataInstructions
	 (InstructionType instrType) {
		Collection<StaticDataUpdateApplied> sduas = 
		 StaticDataUpdateApplied.getCache().getCache().values();

		// Determine whether the current Doc Class/Instruction Type will yield any
		// Static Data Updates
		for (StaticDataUpdateApplied sdua : sduas) {
			if (sdua.getInstrTypeId() == instrType.getInstructionTypeId()) {
				return true;
			}
		}
		
		return false;
	}
	
	/* ************************ Caching ************************************** */	
	private static Cache CACHE = new Cache();
	
	public static Cachable<String, StaticDataUpdateApplied> getCache() {
		return CACHE;
	}
	
	/** StaticDataUpdateApplied cache implementor */
	private static class Cache extends Cachable<String, StaticDataUpdateApplied> {

		public Cache() {
			super("Static Data Update Applied");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<StaticDataUpdateApplied> stdAppliedVec = StaticDataUpdateApplied.getAll(facade);
			
			HashMap<String, StaticDataUpdateApplied> newCache = 
			 new HashMap<String, StaticDataUpdateApplied>();
			
			for (StaticDataUpdateApplied stdApplied : stdAppliedVec) {
				newCache.put(stdApplied.getInstrTypeId()+"|"+stdApplied.getSduId(), stdApplied);
			}
			this.CACHE_MAP = newCache;
		}
	}		
	
	
	/* ************************ Caching ************************************** */	
	private static IdCache IdCACHE = new IdCache();
	
	public static Cachable<Integer, Vector<StaticDataUpdateApplied>> getIdCache() {
		return IdCACHE;
	}
	
	/** StaticDataUpdateApplied cache implementor */
	private static class IdCache extends Cachable<Integer, Vector<StaticDataUpdateApplied>> {

		public IdCache() {
			super("Static Data Update Applied Id");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<StaticDataUpdateApplied> stdAppliedVec = StaticDataUpdateApplied.getAll(facade);
			
			HashMap<Integer, Vector<StaticDataUpdateApplied>> newCache = 
			 new HashMap<Integer, Vector<StaticDataUpdateApplied>>();
			
			for (StaticDataUpdateApplied stdApplied : stdAppliedVec) {
				
				Vector<StaticDataUpdateApplied> appliedVec;
				if (newCache.containsKey(stdApplied.getInstrTypeId())) {
					appliedVec = newCache.get(stdApplied.getInstrTypeId());
				} else {
					appliedVec = new Vector<StaticDataUpdateApplied>();
				}
				appliedVec.add(stdApplied);
				newCache.put(stdApplied.getInstrTypeId(), appliedVec);
			}
			this.CACHE_MAP = newCache;
		}
	}			
}
