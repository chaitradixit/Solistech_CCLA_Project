package com.ecs.ucm.ccla.commproc.translation;

import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.data.DataType;
import com.ecs.ucm.ccla.data.instruction.InstructionDataFieldValue;
import com.ecs.ucm.ccla.data.instruction.UCMFieldTranslator;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Generic translator for UCM fields which accept Yes/No values.
 * 
 * @author Tom
 *
 */
public class YesNoValueToBoolHandler implements UCMFieldTranslator {

	public InstructionDataFieldValue getInstructionFieldValue
	 (LWDocument lwd, String ucmFieldName, FWFacade facade) throws DataException {
		
		try {
			String yesNoValue = lwd.getAttribute(ucmFieldName);
			return getBoolValue(yesNoValue);
			
		} catch (Exception e) {
			throw new DataException("Failed to resolve Yes/No value: " 
			 + e.getMessage(), e);
		}
	}
	
	public InstructionDataFieldValue getInstructionFieldValue
	 (DataResultSet rs, String ucmFieldName, FWFacade facade) throws DataException {
		
		String yesNoValue = rs.getStringValueByName(ucmFieldName);
		return getBoolValue(yesNoValue);
	}
	
	private static InstructionDataFieldValue getBoolValue(String yesNoValue) 
	 throws DataException {
		InstructionDataFieldValue fieldValue = new InstructionDataFieldValue
		 (DataType.BOOL);
		
		if (StringUtils.stringIsBlank(yesNoValue))
			fieldValue.setBoolValue(null);
		else
			fieldValue.setBoolValue(yesNoValue.equalsIgnoreCase("Yes"));
			
		return fieldValue;
	}
}