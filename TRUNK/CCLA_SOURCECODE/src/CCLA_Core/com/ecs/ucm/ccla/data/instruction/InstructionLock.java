package com.ecs.ucm.ccla.data.instruction;

import java.util.Date;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.data.Persistable;
import com.ecs.ucm.ccla.data.instruction.Instruction;
import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.DataResultSetUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Models entries from the INSTRUCTION_LOCK table.
 *  
 *  This tracks the instructions currently 'owned' by each routing module.
 *  
 *  The table has a constraint to ensure that each instruction can only have 1 lock
 *  at a time.
 *  
 *  The suspended flag will be set by a routing module if an instruction cannot proceed
 *  from that module until a user decision is made.
 * 
 * @author Tom
 *
 */
public class InstructionLock implements Persistable {
	
	private int instructionId;

	private RoutingModule module;
	private Date lockDate;
	
	private boolean suspended;
	
	public InstructionLock(int instructionId, RoutingModule module, Date lockDate,
	 boolean suspended) {
		this.instructionId = instructionId;
		this.module = module;
		this.lockDate = lockDate;
		this.suspended = suspended;
	}
	
	/** Adds a new Instruction Lock.
	 *  
	 * 
	 * @param instructionId
	 * @param module
	 * @param facade
	 * @param userName
	 * @throws DataException if a lock already exists for this instruction (DB error)
	 */
	public static void add(int instructionId, RoutingModule module,
	 FWFacade facade, String userName) throws DataException {
		
		InstructionLock newLock = new InstructionLock
		 (instructionId, module, new Date(), false);
		
		DataBinder binder = new DataBinder();
		newLock.addFieldsToBinder(binder);
		
		facade.execute("qCommProc_AddInstructionLock", binder);
	}
	
	public static InstructionLock get(DataResultSet rs) throws DataException {
		if (rs.isEmpty())
			return null;
		
		return new InstructionLock(
			DataResultSetUtils.getResultSetIntegerValue(rs, "INSTRUCTION_ID"),
			RoutingModule.getCache().getCachedInstance(
					DataResultSetUtils.getResultSetIntegerValue(rs, "MODULE_ID")
			),
			rs.getDateValueByName("LOCK_DATE"),
			DataResultSetUtils.getResultSetBoolValue(rs, "IS_SUSPENDED")
		);
	}
	
	public static InstructionLock get(int instructionId, FWFacade facade) 
	 throws DataException {
		return get(getData(instructionId, facade));
	}
	
	public static DataResultSet getData(int instructionId, FWFacade facade) 
	 throws DataException {
		DataBinder binder = new DataBinder();
		
		BinderUtils.addIntParamToBinder
		 (binder, "INSTRUCTION_ID", instructionId);
		
		return facade.createResultSet("qCommProc_GetInstructionLock", binder);
	}
	
	/** Removes an existing Instruction Lock, if applicable.
	 * 
	 *  This should not be called directly by Routing Module Handlers - instead they
	 *  should use InstructionLockUtils.removeLock() method which performs extra
	 *  validation checks.
	 * 
	 * @param instructionId
	 * @param facade
	 * @param userName
	 * @throws DataException
	 */
	public static void remove(int instructionId, FWFacade facade, String userName) 
	 throws DataException {

		DataBinder binder = new DataBinder();
		BinderUtils.addIntParamToBinder
		 (binder, "INSTRUCTION_ID", instructionId);
		
		facade.execute("qCommProc_RemoveInstructionLock", binder);
	}
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {

		BinderUtils.addIntParamToBinder
		 (binder, "INSTRUCTION_ID", this.getInstructionId());
		BinderUtils.addIntParamToBinder
		 (binder, "MODULE_ID", this.getModule().getModuleId());

		BinderUtils.addDateParamToBinder
		 (binder, "LOCK_DATE", this.getLockDate());
		BinderUtils.addBooleanParamToBinder
		 (binder, "IS_SUSPENDED", this.isSuspended());
	}

	public void persist(FWFacade facade, String username) throws DataException {

		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);
		
		facade.execute("qCommProc_UpdateInstructionLock", binder);
	}

	public void setAttributes(DataBinder binder) throws DataException {
		throw new DataException("Not implemented");

	}

	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub

	}

	public int getInstructionId() {
		return instructionId;
	}

	public void setInstructionId(int instructionId) {
		this.instructionId = instructionId;
	}

	public RoutingModule getModule() {
		return module;
	}

	public void setModule(RoutingModule module) {
		this.module = module;
	}

	public Date getLockDate() {
		return lockDate;
	}

	public void setLockDate(Date lockDate) {
		this.lockDate = lockDate;
	}

	public boolean isSuspended() {
		return suspended;
	}

	public void setSuspended(boolean suspended) {
		this.suspended = suspended;
	}
}
