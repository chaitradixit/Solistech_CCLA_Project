package com.ecs.ucm.ccla.data.instruction;

import java.util.Vector;

import com.ecs.ucm.ccla.data.DataType;

import intradoc.data.DataException;

/** Wrapper for Instruction Data/Value pairs.
 * 
 *  No equivalent entity in the DB for these, they are used to store pieces of 
 *  Instruction Data (and their values) independently of any particular Instruction 
 *  Type.
 * 
 * @author Tom
 *
 */
public class InstructionDataValue {
	
	private InstructionData instructionData;
	private InstructionDataFieldValue instructionDataFieldValue;
	
	/** Creates an InstructionDataValue for the given field, with a blank value.
	 * 
	 * @param instructionData
	 * @throws DataException 
	 */
	public InstructionDataValue(InstructionData instructionData) throws DataException {
		this.instructionData = instructionData;
		
		this.instructionDataFieldValue = new InstructionDataFieldValue
		 (instructionData.getDataType());
	}
	
	/** Creates an InstructionDataValue for the given field, with the given String
	 *  value set.
	 *  
	 * @param instructionData
	 * @param rawValue
	 * @throws DataException
	 */
	public InstructionDataValue(InstructionData instructionData, String rawValue) 
	 throws DataException {
		this.instructionData = instructionData;
		
		try {
			this.instructionDataFieldValue = new InstructionDataFieldValue
			(instructionData.getDataType());

			this.instructionDataFieldValue.setValue(rawValue);
		} catch (DataException de) {
			throw new DataException("Cannot set instruction data for "+instructionData.getName()+
					", due to "+de.getMessage());
		}
	}
	
	/** Creates an InstructionDataValue for the given field, with the given Int
	 *  value set.
	 *  
	 * @param instructionData
	 * @param rawValue
	 * @throws DataException
	 */
	public InstructionDataValue(InstructionData instructionData, Integer intValue) 
	 throws DataException {
		this.instructionData = instructionData;
		
		if (!instructionData.getDataType().equals(DataType.INT))
			throw new DataException("Incorrect INT datatype used for Data Field " +
			 instructionData.getName() + " (expected " 
			 + instructionData.getDataType().getName() + ")");
			
		this.instructionDataFieldValue = new InstructionDataFieldValue
		 (instructionData.getDataType());
		
		this.instructionDataFieldValue.setIntValue(intValue);
	}
	
	public InstructionDataValue(InstructionData instructionData,
			InstructionDataFieldValue instructionDataFieldValue) {
		this.instructionData = instructionData;
		this.instructionDataFieldValue = instructionDataFieldValue;
	}
	
	public InstructionData getInstructionData() {
		return instructionData;
	}
	public void setInstructionData(InstructionData instructionData) {
		this.instructionData = instructionData;
	}
	public InstructionDataFieldValue getInstructionDataFieldValue() {
		return instructionDataFieldValue;
	}
	public void setInstructionDataFieldValue(
			InstructionDataFieldValue instructionDataFieldValue) {
		this.instructionDataFieldValue = instructionDataFieldValue;
	}
	
	/** Converts a generic set of Instruction Data Values into a set of Applied Data
	 *  Values for the given Instruction.
	 *  
	 *  If any of the passed data fields are not applicable for the passed instruction's
	 *  type, they will be ignored.
	 * 
	 *  For opposite conversion to convertToDataValues()
	 *  
	 * @param instr
	 * @param dataValues
	 * @return
	 * @throws DataException
	 */
	public static Vector<InstructionDataApplied> convertToAppliedData
	 (Instruction instr, Vector<InstructionDataValue> dataValues) throws DataException {
		
		Vector<InstructionDataApplied> dataApplied = 
		 new Vector<InstructionDataApplied>();
		
		for (InstructionDataValue dataValue : dataValues) {

			InstructionDataApplied instrDataAppl = convertToAppliedData
			 (instr, dataValue);
			
			if (instrDataAppl != null) // ignore data fields which aren't valid for this
										// instuction type
				dataApplied.add(instrDataAppl);
		}
		
		return dataApplied;
	}
	
	/** Convert an instruction-specific set of applied Data Values into a generic set
	 *  of Instruction Data Values.
	 *  
	 *  For opposite conversion to convertToAppliedData()
	 *  
	 * @param instrDataApplied
	 * @return
	 */
	public static Vector<InstructionDataValue> convertToDataValues
	 (Vector<InstructionDataApplied> instrDataApplied) {
		
		Vector<InstructionDataValue> dataValues
		 = new Vector<InstructionDataValue>();
		
		for (InstructionDataApplied dataApplied : instrDataApplied) {

			InstructionDataValue instrDataValue = new InstructionDataValue(
			 dataApplied.getApplicableInstructionData().getInstructionData(), 
			 dataApplied.getDataFieldValue());

			dataValues.add(instrDataValue);
		}
		
		return dataValues;
	}
		
	/** Converts a single abstract InstructionDataValue instance to an 
	 *  InstructionDataApplied instance for the given Instruction.
	 *  
	 * @param instr
	 * @param dataValue
	 * @return 	null, if the Instruction Data field isn't applicable for the given
	 * 			Instruction's Type.	
	 * @throws DataException 
	 */
	public static InstructionDataApplied convertToAppliedData
	 (Instruction instr, InstructionDataValue dataValue) throws DataException {
		
		ApplicableInstructionData applInstrData = ApplicableInstructionData
		 .getApplicableInstructionDataById(
			instr.getType().getInstructionTypeId(), 
			dataValue.getInstructionData().getInstructionDataId());
		
		if (applInstrData != null) {
			return new InstructionDataApplied
			 (instr.getInstructionId(), applInstrData, 
			 dataValue.getInstructionDataFieldValue());
		} else {
			return null;
		}
	}
}
