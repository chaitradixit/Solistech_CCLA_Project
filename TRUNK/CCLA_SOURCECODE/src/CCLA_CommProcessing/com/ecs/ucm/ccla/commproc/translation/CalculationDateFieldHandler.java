package com.ecs.ucm.ccla.commproc.translation;

import java.util.Date;

import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.Globals.UCMFieldNames;
import com.ecs.ucm.ccla.data.DataType;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.form.Form;
import com.ecs.ucm.ccla.data.instruction.InstructionDataFieldValue;
import com.ecs.ucm.ccla.data.instruction.UCMFieldTranslator;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.embedded.FWFacade;

public class CalculationDateFieldHandler implements UCMFieldTranslator {

	public InstructionDataFieldValue getInstructionFieldValue
	 (LWDocument lwd, String ucmFieldName, FWFacade facade) throws DataException {
		
		try {
			String formIdStr = 
			 lwd.getAttribute(UCMFieldNames.FormID);

			if (!StringUtils.stringIsBlank(formIdStr)) {
				Integer formId = Integer.parseInt(formIdStr);
				
				return getFromFormId(formId, facade);
			} else {
				// No Form ID present, return empty field value
				return new InstructionDataFieldValue(DataType.DATE);
			}
			
		} catch (Exception e) {
			throw new DataException("Unable to translate field: " + e.getMessage(), e);
		}
	}

	public InstructionDataFieldValue getInstructionFieldValue
	 (DataResultSet rs, String ucmFieldName, FWFacade facade) throws DataException {
		
		try {
			String formIdStr = 
			 rs.getStringValueByName(UCMFieldNames.FormID);
			
			if (!StringUtils.stringIsBlank(formIdStr)) {
				Integer formId = Integer.parseInt(formIdStr);
				return getFromFormId(formId, facade);
			} else {
				// No Dest. Org Code present, return empty field value
				return new InstructionDataFieldValue(DataType.DATE);
			}
	 	} catch (Exception e) {
			throw new DataException("Unable to translate field: " + e.getMessage(), e);
		}
	}
	
	private static InstructionDataFieldValue getFromFormId
	 (Integer formId, FWFacade facade) throws DataException {
		Form form = Form.get(formId, facade);
		
		if (form != null) {
			Date calculationDate = form.getCalculationDate();
			
			if (calculationDate == null) {
				throw new DataException("Form ID " + formId +
				 " has no Calculation Date set");
			}
			
			InstructionDataFieldValue fieldValue = new InstructionDataFieldValue
			 (DataType.DATE);
			fieldValue.setDateValue(calculationDate);
			
			return fieldValue;
		} else {
			throw new DataException("No Form found with ID: " + formId);
		}
	}
}
