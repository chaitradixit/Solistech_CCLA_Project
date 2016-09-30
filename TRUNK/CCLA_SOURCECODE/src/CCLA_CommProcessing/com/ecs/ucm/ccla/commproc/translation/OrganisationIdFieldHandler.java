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

/** Converts UCM account metadata fields into an Organisation ID.
 * 
 * @author Tom
 *
 */
public class OrganisationIdFieldHandler implements UCMFieldTranslator {

	public InstructionDataFieldValue getInstructionFieldValue
	 (LWDocument lwd, String ucmFieldName, FWFacade facade) throws DataException {
		
		String orgCodeStr, clientNumStr, companyStr;
		
		try {
			orgCodeStr		= lwd.getAttribute(UCMFieldNames.OrgAccountCode);
			clientNumStr 	= lwd.getAttribute(UCMFieldNames.ClientNumber);
			companyStr 		= lwd.getAttribute(UCMFieldNames.Company); 
			
			return getOrganisationId(orgCodeStr, clientNumStr, companyStr, facade);
		} catch (Exception e) {
			throw new DataException("Unable to translate field: " + e.getMessage(), e);
		}
	}

	public InstructionDataFieldValue getInstructionFieldValue
	 (DataResultSet rs, String ucmFieldName, FWFacade facade) throws DataException {
		
		try {
			String orgCodeStr = rs.getStringValueByName(UCMFieldNames.OrgAccountCode);
			String clientNumStr = rs.getStringValueByName(UCMFieldNames.ClientNumber);
			String companyStr 	= rs.getStringValueByName(UCMFieldNames.Company); 
			
			return getOrganisationId(orgCodeStr, clientNumStr, companyStr, facade);
	 	} catch (Exception e) {
			throw new DataException("Unable to translate field: " + e.getMessage(), e);
		}
	}
	
	public static InstructionDataFieldValue getOrganisationId
	 (String orgCodeStr, String clientNumStr, String companyStr, FWFacade facade) 
	 throws DataException {
		
		if (StringUtils.stringIsBlank(orgCodeStr) && 
			StringUtils.stringIsBlank(clientNumStr)) {
			// No Client Number present, return empty field value
			return new InstructionDataFieldValue(DataType.INT);
		}
		
		// If enough values are present, the Organisation record will be looked up from
		// both Org Code and Aurora Client Number/Company fields.
		
		// If Org IDs can be resolved from both identitiers, the resultant Org IDs are
		// compared for equality. A mismatch indicates that the document data is
		// invalid (i.e. Org Acc Code doesn't correspond to the Client Number/Company)
		Entity orgCodeEntity = null, clientNumberEntity = null;
		
		if (!StringUtils.stringIsBlank(orgCodeStr)) {
			// Org Code is present - use this to lookup Org record
			orgCodeEntity = Entity.getEntityByOrgAccountCode(orgCodeStr, facade);
			
			if (orgCodeEntity == null) {
				throw new DataException("Invalid Organisation data, no matching " +
				 "Organisation with Org Account Code: " + orgCodeStr);
			}
		}
		
		if (!StringUtils.stringIsBlank(clientNumStr)) {
			// Use Client Number/Company to perform Org lookup.
			int clientNum = Integer.parseInt(clientNumStr);
			clientNumberEntity = Entity.getEntityFromAuroraValues
			 (clientNum, companyStr, facade);
			
			if (clientNumberEntity == null) {
				throw new DataException("Invalid Organisation data, no matching " +
				 "Organisation for Client No: " + clientNumStr + ", Company: " + 
				 companyStr);
			}
		}
		
		if (orgCodeEntity != null && clientNumberEntity != null) {
			if (orgCodeEntity.getOrganisationId() 
				!= clientNumberEntity.getOrganisationId()) {
				// Uh-oh, data mismatch. Fail.
				throw new DataException("Invalid Organisation data, Org Account Code " +
				 "correspondents to Organisation '" 
				 + orgCodeEntity.getOrganisationName() + "', Client Number/Company " +
				 "corresponds to Organisation '"
				 + clientNumberEntity.getOrganisationName() + "'. Ensure all Org " +
				 "identifiers map to the same Organisation.");
			}
		}
		
		Integer entityId = null;
		
		if (orgCodeEntity != null)
			entityId = orgCodeEntity.getOrganisationId();
		else
			entityId = clientNumberEntity.getOrganisationId();
		
		InstructionDataFieldValue fieldValue = new InstructionDataFieldValue
		 (DataType.INT);
		fieldValue.setIntValue(entityId);
		
		return fieldValue;
	}
}
