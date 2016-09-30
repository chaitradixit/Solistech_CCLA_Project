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
 * Models entries from the REF_CAMPAIGN_ENROLMENT_STATUS table.
 * @author Cam
 */
public class CampaignEnrolmentStatus {

	/* **************** Constants **************** */
	//BINDER AND DB COLUMN
	public static final String ID = "ENROLMENT_STATUS_ID";
	public static final String NAME = "ENROLMENT_STATUS_NAME";
	public static final String DESCRIPTION = "STATUS_DESCRIPTION";
	
	//List of status that the campaign enrolment can take, 
	//mirrors the ENROLMENT_STATUS_ID column in the database. 
	public static final int ENROL_STATUS 					= 1;
	public static final int EXCLUDE_STATUS 					= 2;
	public static final int INTERESTED_STATUS				= 3;
	public static final int NOT_INTERESTED_STATUS			= 4;
	public static final int NO_RESPONSE_STATUS				= 5;
	public static final int UNDECIDED_STATUS				= 6;
	public static final int AWAITING_ELIGIBILITY_STATUS		= 7;
	public static final int INELIGIBLE_STATUS				= 8;
	public static final int INTENTIONS_CONFIRMED_STATUS		= 9;
	public static final int FORM_GENERATED_STATUS			= 10;
	public static final int FORM_PRINTED_STATUS				= 11;
	public static final int FORM_DISPATCHED_STATUS			= 12;
	public static final int FORM_RETURNED_STATUS			= 13;
	public static final int COMPLETED_STATUS				= 14;
	public static final int ISSUE_WITH_RETURNED_FORM_STATUS	= 15;
	public static final int INTENTION_NOT_CONFIRMED_STATUS	= 16;
	public static final int AWAITING_RESPONSE_STATUS		= 17;
	
	public static final int[] AVAILABLE_STATUSES = {
		ENROL_STATUS, EXCLUDE_STATUS, INTERESTED_STATUS, 
		NOT_INTERESTED_STATUS, NO_RESPONSE_STATUS, UNDECIDED_STATUS,
		AWAITING_ELIGIBILITY_STATUS, INELIGIBLE_STATUS, INTENTIONS_CONFIRMED_STATUS,
		INELIGIBLE_STATUS, INTENTIONS_CONFIRMED_STATUS, FORM_GENERATED_STATUS,
		FORM_PRINTED_STATUS, FORM_DISPATCHED_STATUS, FORM_RETURNED_STATUS,
		COMPLETED_STATUS, ISSUE_WITH_RETURNED_FORM_STATUS, INTENTION_NOT_CONFIRMED_STATUS,
		AWAITING_RESPONSE_STATUS
	};
	
	
	//QUERIES
	private static final String GET_ALL_QUERY_NAME = "qCore_GetAllCampaignEnrolmentStatus";
	
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
	public CampaignEnrolmentStatus(int statusId, String name, String description) {
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
	 * Gets a Vector of CampaignEnrolmentStatus.
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Vector<CampaignEnrolmentStatus> getAll(FWFacade facade) throws DataException {
		Vector<CampaignEnrolmentStatus> statuses = new Vector<CampaignEnrolmentStatus>();
		
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
	 * Returns a CampaignEnrolmentStatus object from the DataResultSet or null if empty
	 * @param rs
	 * @return
	 * @throws DataException
	 */
	public static CampaignEnrolmentStatus get(DataResultSet rs) throws DataException {
		if (rs.isEmpty())
			return null;
		
		return new CampaignEnrolmentStatus(
			DataResultSetUtils.getResultSetIntegerValue(rs, ID),
			DataResultSetUtils.getResultSetStringValue(rs, NAME),
			DataResultSetUtils.getResultSetStringValue(rs, DESCRIPTION)
		);
	}
	
	/* ************************ Caching ************************************** */	
	private static Cache CACHE = new Cache();
	
	public static Cachable<Integer, CampaignEnrolmentStatus> getCache() {
		return CACHE;
	}
	
	/** CampaignEnrolmentStatus cache implementor */
	private static class Cache extends Cachable<Integer, CampaignEnrolmentStatus> {

		public Cache() {
			super("Campaign Enrolment Status");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<CampaignEnrolmentStatus> statuses = CampaignEnrolmentStatus.getAll(facade);
			
			HashMap<Integer, CampaignEnrolmentStatus> newCache = 
			 new HashMap<Integer, CampaignEnrolmentStatus>();
			
			for (CampaignEnrolmentStatus status : statuses) {
				newCache.put(status.getStatusId(), status);
			}
			
			this.CACHE_MAP = newCache;
		}
	}
}
