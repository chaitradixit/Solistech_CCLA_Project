package com.ecs.ucm.ccla.transactions.services;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.ShareClassGroup;
import com.ecs.ucm.ccla.data.ShareClassGroupApplied;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ServiceException;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.server.Service;

public class ShareClassGroupService extends Service {


	public void getShareClassGroup() throws ServiceException, DataException
	{
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		Integer groupId = CCLAUtils.getBinderIntegerValue(m_binder, "GROUP_ID");
		if (groupId == null || groupId == 0)
			throw new ServiceException("Cannot find group id");
		try {
			facade.beginTransaction();
			String username = m_userData.m_name;
			// get basic data on share class group
			DataResultSet rsShareClassGroup = ShareClassGroup.getData(groupId, facade);
			
			// get the accounts in this share class group
			DataResultSet rsShareClassGroupAccounts = ShareClassGroup.getShareClassGroupAccounts(groupId, facade);
			
			m_binder.addResultSet("rsShareClassGroup", rsShareClassGroup);
			if (rsShareClassGroupAccounts != null)
				m_binder.addResultSet("rsShareClassGroupAccounts", rsShareClassGroupAccounts);
			facade.commitTransaction();
		} catch (NumberFormatException e) {
			String msg = "Unable to create Share Class group:" + e.getMessage();
			Log.error(msg, e);		
			facade.rollbackTransaction();
			throw new ServiceException(msg, e);
		} catch (DataException e) {
			String msg = "Unable to create Share Class group:" + e.getMessage();
			Log.error(msg, e);		
			facade.rollbackTransaction();
			throw new ServiceException(msg, e);
		}	
	}	
	
	public void createShareClassGroup() throws ServiceException, DataException
	{
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		try {
			facade.beginTransaction();
			String username = m_userData.m_name;
			ShareClassGroup scg = ShareClassGroup.add(m_binder, facade, username);
			Log.debug("Created share class group with id:" + scg.getGroupId());
			CCLAUtils.appendToBinderParam(m_binder, "RedirectUrl", Integer.toString(scg.getGroupId()));
			facade.commitTransaction();
		} catch (NumberFormatException e) {
			String msg = "Unable to create Share Class group:" + e.getMessage();
			Log.error(msg, e);		
			facade.rollbackTransaction();
			throw new ServiceException(msg, e);
		} catch (DataException e) {
			String msg = "Unable to create Share Class group:" + e.getMessage();
			Log.error(msg, e);		
			facade.rollbackTransaction();
			throw new ServiceException(msg, e);
		}	
	}

	public void updateShareClassGroup() throws ServiceException, DataException
	{
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		Integer groupId = CCLAUtils.getBinderIntegerValue(m_binder, "GROUP_ID");
		if (groupId == null || groupId == 0)
			throw new ServiceException("Cannot find group id");
		try {
			facade.beginTransaction();
			String username = m_userData.m_name;
			ShareClassGroup scg = ShareClassGroup.get(groupId, facade);
			scg.setAttributes(m_binder);
			scg.setLastUpdatedBy(username);
			scg.persist(facade, username);
			facade.commitTransaction();
			CCLAUtils.appendToBinderParam(m_binder, "RedirectUrl", Integer.toString(scg.getGroupId()));
		} catch (NumberFormatException e) {
			String msg = "Unable to update Share Class group:" + e.getMessage();
			Log.error(msg, e);		
			facade.rollbackTransaction();
			throw new ServiceException(msg, e);
		} catch (DataException e) {
			String msg = "Unable to update Share Class group:" + e.getMessage();
			Log.error(msg, e);		
			facade.rollbackTransaction();
			throw new ServiceException(msg, e);
		}	
	}	
	
	public void addAccountToGroup() throws DataException, ServiceException
	{
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);	
		Integer groupId = CCLAUtils.getBinderIntegerValue(m_binder, "GROUP_ID");
		Integer accountId = CCLAUtils.getBinderIntegerValue(m_binder, "ACCOUNT_ID");
		if (accountId == null || groupId == null)
			throw new ServiceException("Could not find account or group id, cannot add account to group");		
		try {
			facade.beginTransaction();
			// first check if it link already exists or if account is already added to another group
			// if added to group then return error message, if link exists do nothing
			ShareClassGroupApplied checkGroup = ShareClassGroupApplied.getByAccountId(accountId, facade);
			if (checkGroup != null)
			{
				if (checkGroup.getGroupId() != groupId)
				{
					String error = "Cannot add this account to this share class group, it already has a share class group";
					Log.debug(error);
					CCLAUtils.appendToBinderParam(m_binder, "RedirectUrl", "&hasError=" + error);
				} else
				{
					CCLAUtils.appendToBinderParam(m_binder, "RedirectUrl", "&IsUpdated=1");
				}
			} else {
			// can only add an account if it has the same fund_code as the share class group	
			Account account = Account.get(accountId, facade);
			ShareClassGroup scg = ShareClassGroup.get(groupId, facade);
				if (account.getFundCode().equalsIgnoreCase(scg.getFundCode()))
					{
					String username = m_userData.m_name;
					ShareClassGroupApplied scga = ShareClassGroupApplied.add(m_binder, facade, username);
					CCLAUtils.appendToBinderParam(m_binder, "RedirectUrl", "&IsUpdated=1");
					}
				else
					{
					String error = "Can only add accounts within fund " + scg.getFundCode() + " to this group";
					Log.debug(error);
					CCLAUtils.appendToBinderParam(m_binder, "RedirectUrl", "&hasError=" + error);
					}
			}
			facade.commitTransaction();
			
		} catch (NumberFormatException e) {
			String msg = "Unable to create Share Class group:" + e.getMessage();
			Log.error(msg, e);		
			facade.rollbackTransaction();
			throw new ServiceException(msg, e);
		}			
	}

	
	public void removeAccountFromGroup() throws ServiceException, DataException
	{
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		Integer groupId = CCLAUtils.getBinderIntegerValue(m_binder, "GROUP_ID");
		Integer accountId = CCLAUtils.getBinderIntegerValue(m_binder, "ACCOUNT_ID");
		if (groupId == null || accountId == null)
			throw new ServiceException("Cannot find group id or accountId");
		try {
			facade.beginTransaction();
			String username = m_userData.m_name;
			ShareClassGroupApplied scga = ShareClassGroupApplied.getByAccountIdGroupId(accountId, groupId, facade);
			ShareClassGroupApplied.delete(scga.getGroupAppliedId(), facade, username);
			facade.commitTransaction();
			CCLAUtils.appendToBinderParam(m_binder, "RedirectUrl", "&IsUpdated=1");
		} catch (NumberFormatException e) {
			String msg = "Unable to delete Share Class group applied:" + e.getMessage();
			Log.error(msg, e);		
			facade.rollbackTransaction();
			throw new ServiceException(msg, e);
		} catch (DataException e) {
			String msg = "Unable to delete Share Class group applied:" + e.getMessage();
			Log.error(msg, e);		
			facade.rollbackTransaction();
			throw new ServiceException(msg, e);
		}	
	}
	
	public void getAllShareClassGroups() throws ServiceException, DataException
	{
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		try {
			DataResultSet rsShareClassGroups = facade.createResultSet("qCore_GetShareClassGroups", m_binder);
			m_binder.addResultSet("rsShareClassGroups", rsShareClassGroups);
		} catch (DataException e) {
			String msg = "Unable to get all Share Class groups:" + e.getMessage();
			Log.error(msg, e);		
			throw new ServiceException(msg, e);
		}	
	}			
}
