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
 * Models entries from the REF_CAMPAIGN_SUBJECT_STATUS table.
 * @author Cam
 */
public class CampaignSubjectStatus {

	/* **************** Constants **************** */
	//BINDER AND DB COLUMN
	public static final String ID = "CAMPSUBJECTSTATUS_ID";
	public static final String NAME = "CAMPSUBJECTSTATUS_NAME";
	public static final String DESCRIPTION = "CAMPSUBJECTSTATUS_DESCRIPTION";
	
	//Statuses, mirrors the id column
	public static final int UNDECIDED_STATUS_ID 		= 1;
	public static final int INTERESTED_STATUS_ID 		= 2;
	public static final int NOT_INTERESTED_STATUS_ID 	= 3;
	
	
	//QUERIES
	public static final String GET_ALL_QUERY_NAME = "qCore_GetAllCampaignSubjectStatus";
	
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
	public CampaignSubjectStatus(int statusId, String name, String description) {
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
	 * Gets a Vector of CampaignSubjectStatus.
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Vector<CampaignSubjectStatus> getAll(FWFacade facade) throws DataException {
		Vector<CampaignSubjectStatus> statuses = new Vector<CampaignSubjectStatus>();
		
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
	 * Returns a CampaignSubjectStatus object from the DataResultSet or null if empty
	 * @param rs
	 * @return
	 * @throws DataException
	 */
	public static CampaignSubjectStatus get(DataResultSet rs) throws DataException {
		if (rs.isEmpty())
			return null;
		
		return new CampaignSubjectStatus(
			DataResultSetUtils.getResultSetIntegerValue(rs, ID),
			DataResultSetUtils.getResultSetStringValue(rs, NAME),
			DataResultSetUtils.getResultSetStringValue(rs, DESCRIPTION)
		);
	}
	
	/* ************************ Caching ************************************** */	
	private static Cache CACHE = new Cache();
	
	public static Cachable<Integer, CampaignSubjectStatus> getCache() {
		return CACHE;
	}
	
	/** CampaignSubjectStatus cache implementor */
	private static class Cache extends Cachable<Integer, CampaignSubjectStatus> {

		public Cache() {
			super("Campaign Subject Status");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<CampaignSubjectStatus> statuses = CampaignSubjectStatus.getAll(facade);
			
			HashMap<Integer, CampaignSubjectStatus> newCache = 
			 new HashMap<Integer, CampaignSubjectStatus>();
			
			for (CampaignSubjectStatus status : statuses) {
				newCache.put(status.getStatusId(), status);
			}
			
			this.CACHE_MAP = newCache;
		}
	}
}
