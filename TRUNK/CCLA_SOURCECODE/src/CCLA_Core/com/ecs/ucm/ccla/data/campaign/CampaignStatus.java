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
 * Models entries from the REF_CAMPAIGN_STATUS table.
 * @author Cam
 */
public class CampaignStatus {

	/* **************** Constants **************** */
	//BINDER AND DB COLUMN
	public static final String ID = "CAMPAIGN_STATUS_ID";
	public static final String NAME = "CAMPAIGN_STATUS_NAME";
	public static final String DESCRIPTION = "STATUS_DESCRIPTION";
	
	//QUERIES
	private static final String GET_ALL_QUERY_NAME = "qCore_GetAllCampaignStatus";
	
	/* **************** Properties **************** */
	private int statusId;
	private String name;
	private String description;
	
	/* ********************** Constructor ************************* */
	/**
	 * Constructor
	 * @param statusId
	 * @param name
	 * @param description
	 */
	public CampaignStatus(int statusId, String name, String description) {
		this.statusId = statusId;
		this.name = name;
		this.description = description;
	}
	
	/* ************************* Methods ************************* */
	public int getStatusId() { return statusId; }
	public void setStatusId(int statusId) { this.statusId = statusId; }
	
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }

	/**
	 * Gets a Vector of CampaignStatus.
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Vector<CampaignStatus> getAll(FWFacade facade) throws DataException {
		Vector<CampaignStatus> statuses = new Vector<CampaignStatus>();
		
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
	 * Returns a CampaignStatus object from the DataResultSet or null if empty
	 * @param rs
	 * @return
	 * @throws DataException
	 */
	public static CampaignStatus get(DataResultSet rs) throws DataException {
		if (rs.isEmpty())
			return null;
		
		return new CampaignStatus(
			DataResultSetUtils.getResultSetIntegerValue(rs, ID),
			DataResultSetUtils.getResultSetStringValue(rs, NAME),
			DataResultSetUtils.getResultSetStringValue(rs, DESCRIPTION)
		);
	}
	
	/* ************************ Caching ************************************** */	
	private static Cache CACHE = new Cache();
	
	public static Cachable<Integer, CampaignStatus> getCache() {
		return CACHE;
	}
	
	/** CommStatus cache implementor */
	private static class Cache extends Cachable<Integer, CampaignStatus> {

		public Cache() {
			super("Campaign Status");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<CampaignStatus> statuses = CampaignStatus.getAll(facade);
			
			HashMap<Integer, CampaignStatus> newCache = 
			 new HashMap<Integer, CampaignStatus>();
			
			for (CampaignStatus status : statuses) {
				newCache.put(status.getStatusId(), status);
			}
			
			this.CACHE_MAP = newCache;
		}
	}
}
