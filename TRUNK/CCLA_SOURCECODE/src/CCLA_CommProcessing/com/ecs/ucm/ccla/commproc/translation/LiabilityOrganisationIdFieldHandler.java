package com.ecs.ucm.ccla.commproc.translation;

import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.Globals.UCMFieldNames;
import com.ecs.ucm.ccla.data.DataType;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.instruction.InstructionDataFieldValue;
import com.ecs.ucm.ccla.data.instruction.UCMFieldTranslator;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.embedded.FWFacade;

public class LiabilityOrganisationIdFieldHandler implements UCMFieldTranslator {

	public InstructionDataFieldValue getInstructionFieldValue
	 (LWDocument lwd, String ucmFieldName, FWFacade facade) throws DataException {
		
		try {
			String orgAccountCode = 
			 lwd.getAttribute(UCMFieldNames.DestinationOrgAccountCode);
		
			if (!StringUtils.stringIsBlank(orgAccountCode)) {
				return getFromOrgCode(orgAccountCode, facade);
			} else {
				// No Dest. Org Code present, return empty field value
				return new InstructionDataFieldValue(DataType.INT);
			}
			
		} catch (Exception e) {
			throw new DataException("Unable to translate field: " + e.getMessage(), e);
		}
	}

	public InstructionDataFieldValue getInstructionFieldValue
	 (DataResultSet rs, String ucmFieldName, FWFacade facade) throws DataException {
		
		try {
			String orgAccountCode = 
			 rs.getStringValueByName(UCMFieldNames.DestinationOrgAccountCode);
			
			if (!StringUtils.stringIsBlank(orgAccountCode)) {
				return getFromOrgCode(orgAccountCode, facade);
			} else {
				// No Dest. Org Code present, return empty field value
				return new InstructionDataFieldValue(DataType.INT);
			}
	 	} catch (Exception e) {
			throw new DataException("Unable to translate field: " + e.getMessage(), e);
		}
	}
	
	private static InstructionDataFieldValue getFromOrgCode
	 (String orgCode, FWFacade facade) throws DataException {
		Entity org = Entity.getEntityByOrgAccountCode(orgCode, facade);
		
		if (org != null) {
			Integer orgId = org.getOrganisationId();
			
			InstructionDataFieldValue fieldValue = new InstructionDataFieldValue
			 (DataType.INT);
			fieldValue.setIntValue(orgId);
			
			return fieldValue;
		} else {
			throw new DataException("No Organisation found with Org Code: " + orgCode);
		}
	}
}
