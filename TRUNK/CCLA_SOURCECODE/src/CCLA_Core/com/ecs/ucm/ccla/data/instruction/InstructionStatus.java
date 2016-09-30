package com.ecs.ucm.ccla.data.instruction;

import java.util.HashMap;
import java.util.Vector;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CacheManager;
import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.ucm.ccla.data.Persistable;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.DataResultSetUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Models entries from the REF_INSTRUCTION_STATUS table.
 * 
 * @author Tom
 *
 */
public class InstructionStatus implements Persistable {
	
	public static InstructionStatus INDEXED = 
	 new InstructionStatus(5, "Indexed", null);
	
	public static InstructionStatus DUPLICATE = 
	 new InstructionStatus(7, "Duplicate", null);
	
	public static InstructionStatus ARCHIVED = 
	 new InstructionStatus(11, "Archived", null);
	
	public static InstructionStatus COMPLETED = 
	 new InstructionStatus(29, "Completed", null);

	public static InstructionStatus TERMINATED =
	 new InstructionStatus(32, "Terminated", null);
	
	public static InstructionStatus PENDING_STATIC_DATA_AUTHORISATION = 
		 new InstructionStatus(30, "Pending SDU Authorisation", null);
	
	/** Default Instruction Status applied to new Instructions.
	 * 
	 */
	public static final InstructionStatus DEFAULT_INSTRUCTION_STATUS = INDEXED;
	
	public static final class StatusID {
		public static final int READY_EOD_PROCESSING 					= 1;
		public static final int IN_EOD_PROCESSING 						= 2;
		public static final int FINISHED_EOD_PROCESSING 				= 3;
		public static final int FINISHED_EOD_CALCULATION 				= 4;
		public static final int INDEXED 								= 5;
		public static final int SUSPECTED_DUPLICATED 					= 6;
		public static final int DUPLICATE 								= 7;
		public static final int PASSED_DUPLICATE_CHECKS 				= 8;
		public static final int PASSED_VALIDATION_CHECKS 				= 9;
		public static final int RELEASED_TO_SPP 						= 10;
		public static final int ARCHIVED 								= 11;
		public static final int READY_FOR_VALIDATION 					= 12;
		public static final int FAILED_VALIDATION_CHECKS 				= 13;
		public static final int READY_FOR_SPP_RELEASE 					= 14;
		public static final int FAILED_SPP_RELEASE 						= 15;
		public static final int READY_FOR_PENDING_TRANSACTION_CHECK 	= 16;
		public static final int PASSED_PENDING_TRANSACTION_CHECK 		= 17;
		public static final int FAILED_PENDING_TRANSACTION_CHECK 		= 18;
		public static final int READY_FOR_ACHIVE_CHECKS 				= 19;
		public static final int PENDING_IAT_SWEEP 						= 20;
		public static final int PENDING_MATCH 							= 21;
		public static final int COMPLETED_IAT_SWEEP 					= 22;
		public static final int COMPLETED_MATCH 						= 23;
		public static final int RETURNED_FUND 							= 24;
		public static final int READY_FOR_STATIC_DATA_UPDATE			= 25;
		public static final int FAILED_STATIC_DATA_UPDATE				= 26;
		public static final int READY_FOR_STATIC_DATA_EXECUTION			= 27;
		public static final int FAILED_STATIC_DATA_EXECUTION			= 28;
		public static final int COMPLETED								= 29;
		public static final int PENDING_STATIC_DATA_AUTHORISATION		= 30;
		public static final int FAILED_STATIC_DATA_AUTHORISATION		= 31;
		public static final int TERMINATED								= 32;
		public static final int PENDING_STATIC_DATA_REAUTHORISATION		= 33;
		public static final int READY_FOR_STATIC_DATA_ROUTING			= 34;
		//Add new statuses here!!
		
	}
	
	public static final class Cols {
		public static final String ID 			= "INSTRUCTION_STATUS_ID";
		public static final String NAME 		= "INSTRUCTION_STATUS_NAME";
		public static final String DESCRIPTION	= "INSTRUCTION_STATUS_DESCRIPTION";
	}
	
	private int instructionStatusId;
	private String statusName;
	private String description;

	public InstructionStatus() {}
	
	public InstructionStatus(int instructionStatusId, String instructionStatusName, 
	 String instructionDescription)
	{
		this.instructionStatusId = instructionStatusId;
		this.statusName = instructionStatusName;
		this.description = instructionDescription;
	}

	/** Fetches all available InstructionStatus instances from the database.
	 * 
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Vector<InstructionStatus> getAll(FWFacade facade) 
	 throws DataException {
		
		DataResultSet rs = facade.createResultSet
		 ("qCore_GetAllInstructionStatuses", new DataBinder());
		
		Vector<InstructionStatus> statuses = new Vector<InstructionStatus>();
		
		if (rs.first()) {
			do {
				statuses.add(get(rs));
			} while (rs.next());
		}
		
		return statuses;
	}
	
	public static InstructionStatus get(int instructionStatusId, FWFacade facade) 
	 throws DataException {
		DataResultSet rs = getData(instructionStatusId, facade);
		return get(rs);
		
	}
	
	public static InstructionStatus get(DataResultSet rs) throws DataException {
		if (rs.isEmpty())
			return null;

		return new InstructionStatus(
			DataResultSetUtils.getResultSetIntegerValue
			 (rs, Cols.ID),
			DataResultSetUtils.getResultSetStringValue
			 (rs, Cols.NAME),
			DataResultSetUtils.getResultSetStringValue
			 (rs, Cols.DESCRIPTION)
		);
	}
	
	public static DataResultSet getData(int instructionStatusId, FWFacade facade) 
	 throws DataException {
		DataBinder binder = new DataBinder();
		
		BinderUtils.addIntParamToBinder
		 (binder, Cols.ID, instructionStatusId);
		
		throw new DataException("Not implemented");
		//DataResultSet rsComm = facade.createResultSet("", binder);
		//return rsComm;
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

	public int getInstructionStatusId() {
		return instructionStatusId;
	}

	public void setInstructionStatusId(int instructionStatusId) {
		this.instructionStatusId = instructionStatusId;
	}

	public String getInstructionStatusName() {
		return statusName;
	}

	public void setInstructionStatusName(String instructionStatusName) {
		this.statusName = instructionStatusName;
	}

	public String getInstructionDescription() {
		return description;
	}

	public void setInstructionDescription(String instructionDescription) {
		this.description = instructionDescription;
	}

	public String toString() {
		return "InstructionStatus[name=" + this.getInstructionStatusName() + ", " +
		 "description=" + this.getInstructionDescription() + "]";
	}
	
	/*
	 *  Caching stuff
	 */
	private static Cache CACHE = new Cache();
	
	public static Cachable<Integer, InstructionStatus> getCache() {
		return CACHE;
	}
	
	/** InstructionStatus cache implementor */
	public static class Cache extends Cachable<Integer, InstructionStatus> {

		public Cache() {
			super("Instruction Status");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<InstructionStatus> statuses = InstructionStatus.getAll(facade);
			
			HashMap<Integer, InstructionStatus> newCache = 
			 new HashMap<Integer, InstructionStatus>();
			
			for (InstructionStatus status : statuses) {
				newCache.put(status.getInstructionStatusId(), status);
			}
			
			this.CACHE_MAP = newCache;
		}
	}
}
