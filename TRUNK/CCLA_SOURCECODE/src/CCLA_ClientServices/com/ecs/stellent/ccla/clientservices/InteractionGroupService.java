package com.ecs.stellent.ccla.clientservices;


import java.util.HashSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.InteractionGroup;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.server.Service;

public class InteractionGroupService extends Service {
	
	public void updateInteractionsGroup()
		throws ServiceException, DataException
	{
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		String userId = m_userData.m_name;
		try {
			facade.beginTransaction();
			updateLinks(facade,m_binder,userId);		
		facade.commitTransaction();
		}
		catch (Exception e) {
			facade.rollbackTransaction();
			
			String msg = "Failed to add new interaction: " + e.toString();
			
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	
	}
	

	
	public static void updateLinks(FWFacade facade, DataBinder binder, String userId) 
	throws DataException, ServiceException
	{
	// get all possible links (on personId and organisationId)
	// and figure out if the checkbox is checked or not
	String pID = binder.getLocal("PERSON_ID");
	String oID = binder.getLocal("ORGANISATION_ID");
	
	if (( StringUtils.stringIsBlank(pID)))
		throw new ServiceException("MISSING PERSON ID");

	String queryName = "qClientServices_getInteractionsByPersonOrg";
	
	if (StringUtils.stringIsBlank(oID))
		queryName = "qClientServices_getInteractionsByPerson";
	

	DataResultSet rsInteractions = facade.createResultSet(queryName, binder);
	String existingInteraction = binder.getLocal("INTERACTION_ID");
	if (StringUtils.stringIsBlank(existingInteraction))
		throw new ServiceException("Missing interaction id for link");
	int interactionExistingId = Integer.parseInt(existingInteraction);
	if (!rsInteractions.isEmpty())
	{
		HashSet<Integer> removeList = new HashSet<Integer>();
		HashSet<Integer> addList = new HashSet<Integer>();
		do {
			// get the InteractionGroup
			Log.debug("looking for group");
			String addId = rsInteractions.getStringValueByName("INTERACTION_ID");
			// skip over if interaction ids match
			if (!addId.equalsIgnoreCase(existingInteraction))
			{
				boolean addLink = CCLAUtils.getBinderBoolValue(binder, "chk_doLink_" + addId);
				int interactionAddId = Integer.parseInt(addId);
				Log.debug("add link for " + addId  + " is "+ addLink);
				
				if (addLink)
				{
					// only add link if they don't already have one
					if (!InteractionGroup.sharesGroup(interactionAddId, interactionExistingId, facade))
						addList.add(interactionAddId);
				} else {
					//Need to store all remove ids as have to decide how to remove them
					//only remove if they already share a group
					if (InteractionGroup.sharesGroup(interactionAddId, interactionExistingId, facade))
						removeList.add(interactionAddId);				
				}
			}
			
		} while (rsInteractions.next());	
		if (!removeList.isEmpty())
		{
			// remove link
			InteractionGroup.removeLinks(removeList, interactionExistingId, userId, facade);	
		}
		if (!addList.isEmpty())
		{
			// remove link
			InteractionGroup.addLinks(addList, interactionExistingId, userId, facade);	
		}		
	}
	}
}
