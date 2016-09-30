package com.ecs.stellent.ccla.clientservices;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Element;
import com.ecs.ucm.ccla.data.ElementAttribute;
import com.ecs.ucm.ccla.data.ElementAttributeApplied;
import com.ecs.ucm.ccla.data.ElementAttributeType;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ServiceException;
import intradoc.data.DataException;
import intradoc.server.Service;

public class ElementAttributeService extends Service {
	
	/** Updates a set of applied Element attributes for a given Element ID.
	 * 
	 *  Requires the following in the binder:
	 *  -selectionType
	 *  -attributeTypeId
	 *  
	 * @throws DataException 
	 * @throws ServiceException 
	 * @throws DataException 
	 * 
	 */
	public void updateElementAttributesApplied()
	 throws ServiceException, DataException {
		
		String elemIdStr = m_binder.getLocal("ELEMENT_ID");
		
		if (StringUtils.stringIsBlank(elemIdStr)) {
			throw new ServiceException("Unable to update applied attributes:" +
			 " ELEMENT_ID not found");
		}
		
		// Determines which attributes are to be updated.
		// See ElementAttribute.SelectionType enumerator.
		String selTypeStr  = m_binder.getLocal("selectionType");
		
		ElementAttribute.SelectionType selType = null;
		
		if (!StringUtils.stringIsBlank(selTypeStr)) {
			selType = ElementAttribute.SelectionType.valueOf(selTypeStr);
		}
		
		if (selType == null)
			throw new ServiceException("Unable to update applied attributes: "
			 + "unknown/missing attribute selection type: " + selTypeStr);
		
		// Determines what type of attributes are to be updated. 
		Integer attribTypeId = CCLAUtils.getBinderIntegerValue
		 (m_binder, "attributeTypeId");
		
		if (attribTypeId == null)
			throw new ServiceException("Unable to update applied attributes: " +
			 " unknown/missing attribute type");
		
		ElementAttributeType attribType = ElementAttributeType.getCache()
		 .getCachedInstance(attribTypeId);
		
		if (attribType == null)
			throw new ServiceException("Unable to update applied attributes: " + 
			 "unknown/missing attribute type: " + attribTypeId);
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		Element element = Element.get(Integer.parseInt(elemIdStr), facade);
		
		Log.debug("Updating applied attributes for Element ID " + 
		 element.getElementId() + ", selection type: " + selType.toString());
		
		try {
			facade.beginTransaction();
			
			// Update applied element attributes
			ElementAttributeApplied.updateElementAttributesApplied
			 (element.getElementId(), element.getType(), 
			 selType, attribType, m_binder, facade, m_userData.m_name);
			
			facade.commitTransaction();
		} catch (Exception e) {
			facade.rollbackTransaction();
			
			String msg = "Failed to update applied attributes for Element ID " 
			 + element.getElementId() + ": " + e.getMessage();
			
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}
	
}
