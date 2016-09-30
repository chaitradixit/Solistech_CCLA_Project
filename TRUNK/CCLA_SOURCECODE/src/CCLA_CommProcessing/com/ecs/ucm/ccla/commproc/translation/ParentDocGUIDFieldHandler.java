package com.ecs.ucm.ccla.commproc.translation;

import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals.UCMFieldNames;
import com.ecs.ucm.ccla.data.DataType;
import com.ecs.ucm.ccla.data.instruction.InstructionDataFieldValue;
import com.ecs.ucm.ccla.data.instruction.UCMFieldTranslator;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Determines the parent document reference for the passed document.
 *  
 *  The xParentDocName field is checked first, and the corresponding Doc GUID is built
 *  from this, if it exists.
 *  
 *  If the xParentDocName field is blank, it is assumed this document is its own
 *  parent, and the Doc GUID is built from the passed document attributes.
 *  
 *  Currently used as a grouping field for invoices. A set of related invoice line items 
 *  (INV documents) will share the same parent Doc GUID.
 *  
 * @author Tom
 *
 */
public class ParentDocGUIDFieldHandler implements UCMFieldTranslator {

	public InstructionDataFieldValue getInstructionFieldValue(LWDocument lwd,
			String ucmFieldName, FWFacade facade) throws DataException {

		try {
			Integer dID = Integer.parseInt(lwd.getId());
			String parentDocName = lwd.getAttribute(UCMFieldNames.ParentDocName);
			
			return getParentDocGUID(dID, parentDocName, facade);
		} catch (Exception e) {
			throw new DataException("Unable to extract " + UCMFieldNames.ParentDocName +
			 "attribute: " + e.getMessage(), e);
		}
	}
	
	public InstructionDataFieldValue getInstructionFieldValue(DataResultSet rs,
			String ucmFieldName, FWFacade facade) throws DataException {

		Integer dID = CCLAUtils.getResultSetIntegerValue(rs, UCMFieldNames.DocID);
		String parentDocName = rs.getStringValueByName(UCMFieldNames.ParentDocName);
		
		return getParentDocGUID(dID, parentDocName, facade);
	}
	
	private static InstructionDataFieldValue 
	 getParentDocGUID(Integer docId, String parentDocName, FWFacade facade) 
	 throws DataException {
		
		InstructionDataFieldValue fieldValue = new InstructionDataFieldValue
		 (DataType.STRING);
		
		String invoiceDocGUID = null;
		
		if (!StringUtils.stringIsBlank(parentDocName)) {
			// Invoice doc correspondents to the parent doc reference.
			invoiceDocGUID = CCLAUtils.getLatestDocGuidFromDocName(parentDocName);
		} else {
			// No parent doc ref - assume this is the invoice doc.
			invoiceDocGUID = CCLAUtils.getDocGuidFromDid(docId);
		}
			
		fieldValue.setStringValue(invoiceDocGUID);
		return fieldValue;
	}
}