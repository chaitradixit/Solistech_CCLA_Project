package com.ecs.ucm.ccla.data.instruction;

import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.embedded.FWFacade;

public interface UCMFieldTranslator {
	
	/** Attempts to resolve the Instruction Data field value using the LWDocument
	 *  attributes as input.
	 *  
	 *  Must always return an InstructionDataFieldValue instance, even if it is
	 *  empty/no value set.
	 *  
	 * @param lwd
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public InstructionDataFieldValue getInstructionFieldValue
	 (LWDocument lwd, String ucmFieldName, FWFacade facade) throws DataException;
	
	/** Attempts to resolve the Instruction Data field value using the DataResultSet
	 *  current row values as input.
	 *  
	 *  Must always return an InstructionDataFieldValue instance, even if it is
	 *  empty/no value set.
	 *  
	 *  The DataResultSet is expected to be a join of the DocMeta and Revisions tables.
	 *  
	 * @param lwd
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public InstructionDataFieldValue getInstructionFieldValue
	 (DataResultSet rs, String ucmFieldName, FWFacade facade) throws DataException;
}
