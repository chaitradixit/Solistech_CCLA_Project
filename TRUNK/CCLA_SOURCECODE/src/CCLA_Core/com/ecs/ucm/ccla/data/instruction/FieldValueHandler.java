package com.ecs.ucm.ccla.data.instruction;

import intradoc.data.DataException;

import com.ecs.utils.stellent.embedded.FWFacade;

/** Used to calculate dynamic Instruction Data field values.
 *  
 *  Subclasses are tied to Instruction Data entries in the database.
 * 
 * @author Tom
 *
 */
public abstract class FieldValueHandler {

	/** The Instruction Data field this FieldValueHandler is mapped to. */
	protected InstructionData instructionData;
	
	public FieldValueHandler() {}
	
	public void setInstructionData(InstructionData instructionData) {
		this.instructionData = instructionData;
	}
	
	/** Returns the value of the associated field, for the given
	 *  Communication instance.
	 *  
	 * @param comm
	 * @param facade
	 * @return
	 */
	public abstract InstructionDataFieldValue getValue
	 (Instruction instr, FWFacade facade) throws DataException;
}
