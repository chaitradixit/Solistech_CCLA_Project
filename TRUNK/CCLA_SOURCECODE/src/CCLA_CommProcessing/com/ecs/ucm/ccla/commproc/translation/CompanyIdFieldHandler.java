package com.ecs.ucm.ccla.commproc.translation;

import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.Globals.UCMFieldNames;
import com.ecs.ucm.ccla.data.Company;
import com.ecs.ucm.ccla.data.DataType;
import com.ecs.ucm.ccla.data.instruction.InstructionDataFieldValue;
import com.ecs.ucm.ccla.data.instruction.UCMFieldTranslator;
import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.embedded.FWFacade;

public class CompanyIdFieldHandler implements UCMFieldTranslator {

	public InstructionDataFieldValue getInstructionFieldValue(LWDocument lwd,
			String ucmFieldName, FWFacade facade) throws DataException 
	{
		String companyStr;

		try {
			companyStr = lwd.getAttribute(UCMFieldNames.Company); 
			return getCompanyId(companyStr, facade);
		} catch (Exception e) {
			throw new DataException("Unable to translate field: " + e.getMessage(), e);
		}
	}

	public InstructionDataFieldValue getInstructionFieldValue(DataResultSet rs,
			String ucmFieldName, FWFacade facade) throws DataException 
	{
		String companyStr;

		try {
			companyStr = rs.getStringValueByName(UCMFieldNames.Company); 
			return getCompanyId(companyStr, facade);
		} catch (Exception e) {
			throw new DataException("Unable to translate field: " + e.getMessage(), e);
		}
	}

	
	private static InstructionDataFieldValue getCompanyId(String companyStr, FWFacade facade) 
	throws DataException {
	
		Integer companyId = null;
		
		Company company = Company.getNameCache().getCachedInstance(companyStr);
		
		if (company!=null)
			companyId = company.getCompanyId();
		
		InstructionDataFieldValue fieldValue = 
			new InstructionDataFieldValue(DataType.INT);
		fieldValue.setIntValue(companyId);
		
		return fieldValue;
	}
}
