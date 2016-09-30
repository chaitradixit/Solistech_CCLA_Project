package com.ecs.ucm.ccla.data;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Models entries from the CCLA_IDENTITY_VALIDATION_CHECK table
 * 
 * @author Tom
 *
 */
public class IdentityValidationCheck implements Persistable {

	public static DataResultSet getData(int personId, FWFacade facade) 
	 throws DataException {
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, SharedCols.PERSON, personId);
	
		return facade.createResultSet
		 ("qClientServices_GetValidationRecord", binder);
	}
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		// TODO Auto-generated method stub
		
	}

	public void persist(FWFacade facade, String username) throws DataException {
		// TODO Auto-generated method stub
		
	}

	public void setAttributes(DataBinder binder) throws DataException {
		// TODO Auto-generated method stub
		
	}

	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub
		
	}

	
}
