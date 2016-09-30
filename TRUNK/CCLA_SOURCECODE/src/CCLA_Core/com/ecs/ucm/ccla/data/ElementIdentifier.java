package com.ecs.ucm.ccla.data;

import java.util.Vector;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.CacheManager;
import com.ecs.utils.stellent.embedded.FWFacade;

public class ElementIdentifier {
	
	public static final int CHARITY_REF = 1;
	public static final int ONS_NUMBER = 2;
	public static final int COMPANY_REG_NUMBER = 3;
	public static final int CROCKFORDS = 4;
	public static final int HMRC_NUMBER = 5;
	public static final int LOCAL_AUTH_CODE = 7;
	
	private int elementIdentifierId;
	private ElementType elementType;
	private String name;
	
	public ElementIdentifier(int elementIdentifierId, ElementType elementType,
	 String name) {
		this.elementIdentifierId = elementIdentifierId;
		this.elementType = elementType;
		this.name = name;
	}
	
	public static ElementIdentifier get(DataResultSet rs) throws DataException {
		if (rs.isEmpty())
			return null;
		
		return new ElementIdentifier(
			CCLAUtils.getResultSetIntegerValue
			 (rs, "ELEMENT_IDENTIFIER_ID"),
			 ElementType.getCache().getCachedInstance(
			 CCLAUtils.getResultSetIntegerValue
			 (rs, "ELEMENT_TYPE_ID")),
			rs.getStringValueByName("IDENTIFIER_NAME")
		);
	}
	
	/**
	 * Fetches all rows from the REF_ELEMENT_IDENTIFIERS table
	 * 
	 * @param facade
	 * @return
	 * @throws ServiceException
	 * @throws DataException
	 */
	public static DataResultSet getAllIdentifiersData(FWFacade facade) 
		throws DataException {
		
		DataBinder binder = new DataBinder();
		return facade.createResultSet("qClientServices_GetElementIdentifiers", binder);
	}
	
	/**
	 * Fetches all rows from the REF_ELEMENT_IDENTIFIERS table for
	 * the given Element Type.
	 * 
	 * @param facade
	 * @return
	 * @throws ServiceException
	 * @throws DataException
	 */
	public static DataResultSet getIdentifiersByElementTypeData
	 (ElementType type, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "ELEMENT_TYPE_ID", type.getElementTypeId());
		
		return facade.createResultSet
		 ("qClientServices_GetElementIdentifiersByType", binder);
	}
	
	/** Returns a set of ElementIdentifier instances from the given
	 *  ResultSet of Element Identifiers.
	 *  
	 * @param rs
	 * @return
	 * @throws DataException
	 */
	public static Vector<ElementIdentifier> getAll(DataResultSet rs) 
	 throws DataException {
		
		Vector<ElementIdentifier> elemIds = new Vector<ElementIdentifier>();
		
		if (rs.first()) {
			do {
				elemIds.add(get(rs));
			} while (rs.next());
		}
		
		return elemIds;
	}
	
	public int getElementIdentifierId() {
		return elementIdentifierId;
	}

	public void setElementIdentifierId(int elementIdentifierId) {
		this.elementIdentifierId = elementIdentifierId;
	}

	public ElementType getElementType() {
		return elementType;
	}

	public void setElementType(ElementType elementType) {
		this.elementType = elementType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
