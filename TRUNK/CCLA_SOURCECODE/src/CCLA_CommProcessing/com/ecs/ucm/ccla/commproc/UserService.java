package com.ecs.ucm.ccla.commproc;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.server.Service;

public class UserService  extends Service {

	private static final String USER_COL = "DNAME";
	
	public void addNewUser() throws DataException {
		
		String username	= m_binder.getLocal(USER_COL);
		if (StringUtils.stringIsBlank(username)) {
			throw new DataException("DNAME is blank, cannot add user.");
		}
		
		DataBinder binder = new DataBinder();
		binder.putLocal(USER_COL, username);
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		DataResultSet rsUser = facade.createResultSet("qCommProc_GetCDBUser", binder);
		
		if (rsUser==null || rsUser.isEmpty())
			facade.execute("qCommProc_AddCDBUser", m_binder);
		else 
			throw new DataException("User "+username+" already added");
		
	}

	public void deleteUser() throws DataException 
	{
		String username	= m_binder.getLocal(USER_COL);

		if (StringUtils.stringIsBlank(username)) {
			throw new DataException("DNAME is blank, cannot delete user.");
		}
		
		DataBinder binder = new DataBinder();
		binder.putLocal(USER_COL, username);
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		facade.execute("qCommProc_DeleteCDBUser", binder);
	}
}
