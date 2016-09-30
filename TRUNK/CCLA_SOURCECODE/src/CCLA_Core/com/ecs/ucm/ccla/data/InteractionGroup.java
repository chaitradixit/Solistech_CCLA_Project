package com.ecs.ucm.ccla.data;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.DateFormatter;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Represents records in the CCLA_INTERACTION_GROUP table.
 * 
 * @author Tom
 *
 */
public class InteractionGroup implements Persistable {
	private int groupId;
	private int interactionId;
	private String userId;
	private Date lastUpdated;
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		binder.putLocal("GROUP_ID", Integer.toString(
				 this.getGroupId()));
		binder.putLocal("INTERACTION_ID", Integer.toString(
				 this.getInteractionId()));
		binder.putLocal("USER_ID", this.getUserId());
		CCLAUtils.addQueryDateParamToBinder
		 (binder, "LAST_UPDATED", this.getLastUpdated());		
		
	}

	public void persist(FWFacade facade, String username) throws DataException {
		this.validate(facade);
		
		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);
		
		facade.execute("qClientServices_UpdateInteractionGroup", binder);
	}

	public void setAttributes(DataBinder binder) throws DataException {
		this.setGroupId(CCLAUtils.getBinderIntegerValue(binder, "GROUP_ID"));
		this.setInteractionId(CCLAUtils.getBinderIntegerValue(binder,"INTERACTION_ID"));
	}

	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub
		
	}
	
	public InteractionGroup(int groupId, int interactionId, String userId, Date lastUpdated)
	{
		this.groupId = groupId;
		this.interactionId = interactionId;
		this.userId = userId;
		this.lastUpdated = lastUpdated;
	}
	
	public static DataResultSet getData(int interactionId, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		binder.putLocal("INTERACTION_ID", Integer.toString(interactionId));
		
		DataResultSet rsInteraction = facade.createResultSet
		 ("qClientServices_GetInteractionGroupByInteraction", binder);
		
		return rsInteraction;
	}
	
	/*Gets the interaction group for a particular interaction
	 * 
	 * 
	 * 
	 */
	public static InteractionGroup get(int interactionId, FWFacade facade) 
	 throws DataException {
		
		DataResultSet rsInteraction = getData(interactionId, facade);
		
		if (!rsInteraction.isEmpty())
			return get(rsInteraction);
		else
			return null;
	}
	
	/*Compares interaction group for both interactions to see if they match
	 * returns true if they share the same group
	 * 
	 * 
	 */
	public static boolean sharesGroup(int interactionId1,int interactionId2, FWFacade facade) 
	 throws DataException {
		boolean hasMatch = false;
		InteractionGroup group1 = get(interactionId1, facade);
		if (group1 != null)
		{
			int groupId1 = group1.getGroupId();
			InteractionGroup group2 = get(interactionId2, facade);
			if (group2 != null)
			{
				int groupId2 = group2.getGroupId();
				if (groupId1 == groupId2)
					hasMatch=true;
			}
		}
		
		
	
		
		return hasMatch;
	}	
	
	public static InteractionGroup get(DataResultSet rsInteraction) 
	 throws DataException {
		

		InteractionGroup interactionGroup = new InteractionGroup(

			CCLAUtils.getResultSetIntegerValue(rsInteraction, "GROUP_ID"),
			CCLAUtils.getResultSetIntegerValue(rsInteraction, "INTERACTION_ID"),
			rsInteraction.getStringValueByName("USER_ID"),
			rsInteraction.getDateValueByName("LAST_UPDATED")
		);
		
		return interactionGroup;
	}
	
	
	public static InteractionGroup add(int interactionAddId, 
			int interactionExistingId, String userId,  FWFacade facade) throws DataException
	{
		int groupId = 0;
		InteractionGroup interactionGroup = null;		
		Date lastUpdated = new Date();
		boolean doUpdate = false;
		DataBinder binder = new DataBinder();
		
		// find if any group already assigned for existing interaction or add interaction
		if (InteractionGroup.get(interactionExistingId, facade) != null)
		{
			Log.debug("found interaction group for existing interaction:" + interactionExistingId);
			// get group code of existing interaction
			InteractionGroup ig = InteractionGroup.get(interactionExistingId, facade);
			groupId = ig.getGroupId();
			// check to see if additional one has group id too
			if (InteractionGroup.get(interactionAddId, facade)==null)
			{
			Log.debug("NOT found interaction group for add interaction:" + interactionAddId);
			interactionGroup = new InteractionGroup(groupId, interactionAddId, userId, lastUpdated);
			addGroup(interactionGroup, binder, facade);
			} 
			else
			{
			// overwrite existing group with new one
			Log.debug("found existing interaction group, overwriting old group with new");
			interactionGroup = InteractionGroup.get(interactionAddId, facade);
			interactionGroup.addFieldsToBinder(binder);
			interactionGroup.setGroupId(groupId);
			interactionGroup.persist(facade, userId);
			}

		} else if (InteractionGroup.get(interactionAddId, facade) != null)
		{
			// get group code of addition interaction
			InteractionGroup ig = InteractionGroup.get(interactionAddId, facade);
			groupId = ig.getGroupId();
			// check to see if existing one has group id too
			if (InteractionGroup.get(interactionExistingId, facade)==null)
			{
			Log.debug("this existing interaction does not have group so using group from add interaction");
			interactionGroup = new InteractionGroup(groupId, interactionExistingId, userId, lastUpdated);
			addGroup(interactionGroup, binder, facade);
			} 
			else
			{
			// overwrite additional group with new one
			Log.debug("found additional interaction group, overwriting old group with new");
			interactionGroup = InteractionGroup.get(interactionExistingId, facade);
			interactionGroup.addFieldsToBinder(binder);
			interactionGroup.setGroupId(groupId);
			interactionGroup.persist(facade, userId);
			}

		} else
		{
			// need to add two new groups, one for each interaction
			// get next number for groups - they share this value
			String group = CCLAUtils.getNewKey("InteractionGroup", facade);
			groupId = Integer.parseInt(group);
			InteractionGroup interactionGroupAdd = new InteractionGroup(groupId, interactionAddId, userId, lastUpdated);
			addGroup(interactionGroupAdd, binder, facade);
			InteractionGroup interactionGroupExisting = new InteractionGroup(groupId, interactionExistingId, userId, lastUpdated);
			addGroup(interactionGroupExisting, binder, facade);
		}
		return interactionGroup;
	}
	
	public static void addLinks(HashSet addIds,int interactionExistingId, String userId,  FWFacade facade) 
	throws DataException
	{
		Iterator addI = addIds.iterator();
	    while (addI.hasNext()) {
    		int interactionAddId = (Integer) addI.next();
    		add(interactionAddId, interactionExistingId, userId, facade);
    		Log.debug("added " + interactionAddId);
    	    }			
	}
	
	public static void removeLinks(HashSet removeIds, 
			int interactionExistingId, String userId,  FWFacade facade) throws DataException
			{
			DataBinder binder = new DataBinder();
				// get the size of the remove list and compare it to how many interactions are in the group
			int removeSize = removeIds.size();
			InteractionGroup existingGroup = InteractionGroup.get(interactionExistingId, facade);
			int groupId = existingGroup.getGroupId();
			// get all members of group
			CCLAUtils.addQueryIntParamToBinder(binder, "INTERACTION_ID", interactionExistingId);
			DataResultSet groupMembers = facade.createResultSet("qClientServices_GetGroupedInteractions", binder);
			int groupSize = groupMembers.getNumRows()-1;
			// if the groupsize equals the remove size (plus one for the existing interaction)
			// then simply remove the group from existing interaction
			// otherwise remove interactions from group one by one
			Log.debug("removeSize is " + removeSize + " and groupSize is " + groupSize);
			if (removeSize != 0 && removeSize == groupSize)
			{
				remove(interactionExistingId, facade);
				Log.debug("removed group from existing " + interactionExistingId);
			} else
			{
				Iterator removeI = removeIds.iterator();
	    	    while (removeI.hasNext()) {
	        		int interactionRemoveId = (Integer) removeI.next();
	        		remove(interactionRemoveId, facade);
	        		Log.debug("removed " + interactionRemoveId);
	        	    }				
			}
					
			}
	
	public static void remove(int interactionId, FWFacade facade) throws DataException
	{
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, "REMOVE_INTERACTION_ID", interactionId);
		Log.debug("removing link for " + interactionId);
		facade.execute("qClientServices_DeleteInteractionGroupForInteraction", binder);
	}
	
	public static void addGroup(InteractionGroup interactionGroup, DataBinder binder, FWFacade facade) 
		throws DataException
	{
		interactionGroup.addFieldsToBinder(binder);
		interactionGroup.validate(facade);
		facade.execute("qClientServices_AddInteractionGroup", binder);
		Log.debug("Added or amended new interaction group: " + interactionGroup.getGroupId());
	}
	
	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public int getInteractionId() {
		return interactionId;
	}

	public void setInteractionId(int interactionId) {
		this.interactionId = interactionId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	
}
