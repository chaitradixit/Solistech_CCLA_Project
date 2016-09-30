package com.ecs.ucm.ccla.data.campaign;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.util.HashMap;
import java.util.Vector;

import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.DataResultSetUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** 
 * Models entries from the REF_CAMPAIGN_ACTIVITY_TYPE table.
 * @author Cam
 */
public class CampaignActivityType {

	/* **************** Constants **************** */
	//BINDER AND DB COLUMN
	public static final String ID = "CAMPAIGN_ACTIVITY_TYPE_ID";
	public static final String NAME = "CAMPAIGN_ACTIVITY_NAME";
	public static final String DESCRIPTION = "CAMPAIGN_ACTIVITY_DESCRIPTION";
	
	public static final int ENROLMENT_ACTIVITY_ID			= 100000;
	public static final int EXCLUSION_ACTIVITY_ID			= 100001;
	public static final int NOTE_ACTIVITY_ID				= 100002;
	public static final int STATUS_UPDATE_ACTIVITY_ID		= 100003;
	public static final int ACCOUNT_CREATION_ACTIVITY_ID	= 100004;
	public static final int ADD_INTENTION_ACTIVITY_ID		= 100005;
	public static final int UPDATE_INTENTION_ACTIVITY_ID	= 100006;
	public static final int REMOVE_INTENTION_ACTIVITY_ID	= 100007;
	public static final int FORM_CREATION_ACTIVITY_ID		= 100008;
	public static final int FORM_PRINTED_ACTIVITY_ID		= 100009;
	public static final int FORM_RETURNED_ACTIVITY_ID		= 100010;
	public static final int CORRESPONDENT_DETAILS_UPDATED_ID = 100011;
	public static final int FORM_DISPATCHED_ACTIVITY_ID		= 100012;
	public static final int ISSUE_WITH_RETURN_FORM_ACTIVITY_ID = 100013;
	public static final int REGENERATE_FORM_ACTIVITY_ID		= 100014;
	public static final int FORM_STATUS_UDPATE_ID			= 100015;
	public static final int FORM_CANCELLED_ID				= 100016;
	public static final int ACCOUNT_SELECTED_ID				= 100017;
	public static final int ACCOUNT_RESETED_ID				= 100018;
	
	
	public static final int[] AVAILABLE_ACTIVITIES = new int[] {
		ENROLMENT_ACTIVITY_ID, EXCLUSION_ACTIVITY_ID, NOTE_ACTIVITY_ID,
		STATUS_UPDATE_ACTIVITY_ID, ACCOUNT_CREATION_ACTIVITY_ID, ADD_INTENTION_ACTIVITY_ID,
		UPDATE_INTENTION_ACTIVITY_ID, REMOVE_INTENTION_ACTIVITY_ID, FORM_CREATION_ACTIVITY_ID,
		FORM_PRINTED_ACTIVITY_ID, FORM_RETURNED_ACTIVITY_ID,CORRESPONDENT_DETAILS_UPDATED_ID,
		FORM_DISPATCHED_ACTIVITY_ID, ISSUE_WITH_RETURN_FORM_ACTIVITY_ID, REGENERATE_FORM_ACTIVITY_ID,
		FORM_STATUS_UDPATE_ID, FORM_CANCELLED_ID
	};
	
	//QUERIES
	private static final String GET_ALL_QUERY_NAME = "qCore_GetAllCampaignActivityType";
	
	/* **************** Properties **************** */
	private int typeId;
	private String name;
	private String description;
	
	/* ********************** Constructor ************************* */
	/**
	 * Constructor
	 * @param statusId
	 * @param name
	 * @param description
	 */
	public CampaignActivityType(int typeId, String name, String description) {
		this.typeId = typeId;
		this.name = name;
		this.description = description;
	}
	
	/* ************************* Methods ************************* */
	public int getTypeId() { return typeId; }
	public void setTypeId(int typeId) { this.typeId = typeId; }
	
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }

	/**
	 * Gets a Vector of CampaignActivityType.
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Vector<CampaignActivityType> getAll(FWFacade facade) throws DataException {
		Vector<CampaignActivityType> statuses = new Vector<CampaignActivityType>();
		
		DataResultSet rs = facade.createResultSet
		 (GET_ALL_QUERY_NAME, new DataBinder());
		
		if (rs.first()) {
			do {
				statuses.add(get(rs));
			} while (rs.next());
		}
		return statuses;
	}
	
	/**
	 * Returns a CampaignActivityType object from the DataResultSet or null if empty
	 * @param rs
	 * @return
	 * @throws DataException
	 */
	public static CampaignActivityType get(DataResultSet rs) throws DataException {
		if (rs.isEmpty())
			return null;
		
		return new CampaignActivityType(
			DataResultSetUtils.getResultSetIntegerValue(rs, ID),
			DataResultSetUtils.getResultSetStringValue(rs, NAME),
			DataResultSetUtils.getResultSetStringValue(rs, DESCRIPTION)
		);
	}
	
	/* ************************ Caching ************************************** */	
	private static Cache CACHE = new Cache();
	
	public static Cachable<Integer, CampaignActivityType> getCache() {
		return CACHE;
	}
	
	/** CampaignActivityType cache implementor */
	private static class Cache extends Cachable<Integer, CampaignActivityType> {

		public Cache() {
			super("Campaign Activity Type");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<CampaignActivityType> types = CampaignActivityType.getAll(facade);
			
			HashMap<Integer, CampaignActivityType> newCache = 
			 new HashMap<Integer, CampaignActivityType>();
			
			for (CampaignActivityType type : types) {
				newCache.put(type.getTypeId(), type);
			}
			
			this.CACHE_MAP = newCache;
		}
	}
}
