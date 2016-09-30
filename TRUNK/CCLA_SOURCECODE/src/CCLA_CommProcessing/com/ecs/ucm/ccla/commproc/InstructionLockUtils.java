package com.ecs.ucm.ccla.commproc;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.SharedCols;
import com.ecs.ucm.ccla.data.instruction.InstructionLock;
import com.ecs.ucm.ccla.data.instruction.InstructionProcessApplied;
import com.ecs.ucm.ccla.data.instruction.InstructionStatus;
import com.ecs.ucm.ccla.data.instruction.RoutingModule;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Static methods for handling Instruction Locking by the Routing Modules.
 * 
 * @author Tom
 *
 */
public class InstructionLockUtils {
	
	/** Adds a new lock, or resets the date/suspended status on an existing lock, 
	 *  providing the existing lock is against the passed module.
	 * 
	 * @param instructionId
	 * @param moduleId
	 * @param suspended
	 * @param facade
	 * @param userName
	 * @throws DataException
	 */
	public static void lockInstruction
	 (int instructionId, RoutingModule module, 
	 FWFacade facade, String userName) throws DataException {
		
		InstructionLock currentLock = InstructionLock.get(instructionId, facade);
		
		if (currentLock == null)
			InstructionLock.add(instructionId, module, facade, userName);
		else {
			if (!currentLock.getModule().equals(module)) {
				String msg = "Unable to lock instruction, currently locked " +
				 "by " + currentLock.getModule().getName() + " module";
				 
				Log.error(msg);
				throw new DataException(msg);
			}
			
			currentLock.setSuspended(false);
			currentLock.persist(facade, userName);
		}
	}
	
	/** Sets the suspended status on an existing lock.
	 *  
	 *  The passed module must already have the lock.
	 *  
	 * @param instructionId
	 * @param module
	 * @throws DataException 	if no lock exists, or the existing lock is against a
	 * 							different module.
	 */
	public static void suspendInstruction(int instructionId, RoutingModule module,
	 FWFacade facade, String userName) throws DataException {
		
		InstructionLock currentLock = InstructionLock.get(instructionId, facade);
		
		if (currentLock == null) {
			String msg = "Unable to suspend instruction, no lock in place";
			 
			Log.error(msg);
			throw new DataException(msg);
			
		} else {
			if (!currentLock.getModule().equals(module)) {
				String msg = "Unable to suspend instruction, currently locked " +
				 "by " + currentLock.getModule().getName() + " module";
				 
				Log.error(msg);
				throw new DataException(msg);
			}
			
			currentLock.setSuspended(true);
			currentLock.persist(facade, userName);
		}
	}
	
	/** Unlocks the given instruction, providing it has a lock against the passed
	 *  module ID, and it doesn't have an active Instruction Process.
	 *  
	 * @param instructionId
	 * @param module
	 * @param facade
	 * @param userName
	 * @throws DataException 
	 * @throws DataException 	if the lock doesn't exist, or its locked against a 
	 * 							different module
	 */
	public static void unlockInstruction
	 (int instructionId, RoutingModule module, FWFacade facade, String userName) 
	 throws DataException {
		
		InstructionLock instrLock = InstructionLock.get(instructionId, facade);		
		
		if (instrLock.getModule().equals(module)) {
			if (InstructionProcessApplied.hasActiveProcess(instructionId, facade)) {
				String msg = "Unable to unlock instruction, has an active process";
				 
				Log.error(msg);
				throw new DataException(msg);
			}
			
			InstructionLock.remove(instructionId, facade, userName);
		} else {
			String msg = "Unable to unlock instruction, currently locked " +
			 "by " + instrLock.getModule().getName() + " module";
			 
			Log.error(msg);
			throw new DataException(msg);
		}
	}
	
	/** Fetches the existing Instruction Lock, if applicable.
	 * 
	 * @param instructionId
	 * @param facade
	 * @return null if no lock exists
	 * @throws DataException
	 */
	public static InstructionLock getLock
	 (int instructionId, FWFacade facade) throws DataException {
		return InstructionLock.get(instructionId, facade);
	}
	
	/** Removes all locks against Instructions with the given module's Listen Status ID.
	 * 
	 *  Used for routing module clean-up, when module processing is terminated 
	 *  prematurely.
	 *  
	 *  Instructions with an active Instruction Process are filtered by the fetch query.
	 *  
	 * @param instructionStatusId
	 * @param facade
	 * @throws DataException 
	 */
	public static void removeModuleInstructionLocks
	 (RoutingModule module, FWFacade facade, String userName) 
	 throws DataException {
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, InstructionStatus.Cols.ID, 
		 module.getListenStatus().getInstructionStatusId());
		
		DataResultSet rsLockedInstructionIds = facade.createResultSet
		 ("qCommProcess_GetLockedInstructionIdsByStatusId", binder);
		
		if (rsLockedInstructionIds.first()) {
			Log.debug("Removing Instruction Locks on "
			 + rsLockedInstructionIds.getNumRows() + " instructions");
			
			do {
				int instructionId = CCLAUtils.getResultSetIntegerValue
				 (rsLockedInstructionIds, SharedCols.INSTRUCTION);
				
				unlockInstruction(instructionId, module, facade, userName);
				
			} while (rsLockedInstructionIds.next());
		}
	}
}
