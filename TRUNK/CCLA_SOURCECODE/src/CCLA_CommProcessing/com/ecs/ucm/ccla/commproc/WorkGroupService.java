package com.ecs.ucm.ccla.commproc;

import java.util.Vector;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.SharedCols;
import com.ecs.ucm.ccla.data.UserWorkGroup;
import com.ecs.ucm.ccla.data.WorkGroup;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.data.DataException;
import intradoc.server.Service;

public class WorkGroupService extends Service {
	
	/** Adds (or updates) a User ID -> Work Group mapping to the DB.
	 * 
	 * @throws DataException 
	 * 
	 */
	public void addUserWorkGroup() throws DataException {
		
		String userId	= m_binder.getLocal(SharedCols.USER);
		int workGroupId = CCLAUtils.getBinderIntegerValue(m_binder, WorkGroup.Cols.ID);
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		UserWorkGroup.addOrUpdate(userId, workGroupId, facade, m_userData.m_name);
		
		// Refresh user work group cache to reflect the change.
		UserWorkGroup.getCache().buildCache(facade);
	}
	
	/** Updates all existing User ID -> Work Group mappings.
	 *  
	 *  Expects binder values with names in the form:
	 *  
	 *  WORK_GROUP_ID_<user work group ID>
	 *  
	 *  Where <user work group ID> matches to an existing mapping entry ID in the
	 *  USER_WORK_GROUP table.
	 *  
	 * @throws DataException 
	 */
	public void updateUserWorkGroups() throws DataException {
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		Vector<UserWorkGroup> userWorkGroups = UserWorkGroup.getAll(facade);
		
		for (UserWorkGroup userWorkGroup : userWorkGroups) {
			Integer workGroupId = CCLAUtils.getBinderIntegerValue
			 (m_binder, WorkGroup.Cols.ID + "_" + userWorkGroup.getUserWorkGroupId());
			
			if (workGroupId != null) {
				Log.debug("Updating Work Group for user: " + userWorkGroup.getUserName()
				 + " to " + workGroupId);
				
				UserWorkGroup.addOrUpdate
				 (userWorkGroup.getUserName(), workGroupId, facade, m_userData.m_name);
			} else {
				Log.warn("Unable to find Work Group ID for user: " + 
				 userWorkGroup.getUserName());
			}
		}
		
		// Refresh user work group cache to reflect the change.
		UserWorkGroup.getCache().buildCache(facade);
	}
	
}
