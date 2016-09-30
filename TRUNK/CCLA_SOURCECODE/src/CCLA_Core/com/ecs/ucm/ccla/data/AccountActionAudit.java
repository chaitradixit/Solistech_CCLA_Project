package com.ecs.ucm.ccla.data;

import intradoc.data.DataBinder;
import intradoc.data.DataException;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

public class AccountActionAudit implements Persistable {

	public enum ActionType {
		ADD,
		UPDATE,
		BATCH,
		RESUME,
		SUSPEND,
		UPDATE_AUTH_STATUS,
		UPDATE_SUB_FOR_AUTH
	}
	
	public static void add
	 (int accountActionId, ActionType type, String description, 
	  String userId, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "ACCOUNT_ACTION_ID", accountActionId);
		
		binder.putLocal("ACTION", type.toString());
		binder.putLocal("DESCRIPTION", description);
		
		binder.putLocal("USER_ID", userId);
		
		facade.execute("qClientServices_AddAccountActionAudit", binder);
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
