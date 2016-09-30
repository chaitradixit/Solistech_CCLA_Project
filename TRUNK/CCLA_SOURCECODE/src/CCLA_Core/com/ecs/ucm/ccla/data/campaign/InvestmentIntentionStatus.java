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
 * Models entries from the REF_INVESTMENT_INTENTION_STATUS table.
 * @author Cam
 */
public class InvestmentIntentionStatus {

	/* **************** Constants **************** */
	//BINDER AND DB COLUMN
	public static final String ID = "INVINTENTSTATUS_ID";
	public static final String NAME = "INVINTENTSTATUS_NAME";
	public static final String DESCRIPTION = "INVINTENTSTATUS_DESCRIPTION";
	
	
	public static final int UNCONFIRMED_STATUS_ID 			= 1;
	public static final int UNDECIDED_STATUS_ID 			= 2;
	public static final int ZERO_PERCENT_STATUS_ID 			= 3;
	public static final int TWENTY_FIVE_PERCENT_STATUS_ID 	= 4;
	public static final int FIFTY_PERCENTAGE_STATUS_ID 		= 5;
	public static final int ONE_HUNDRED_PERCENT_STATUS_ID 	= 6;
	
	
	//QUERIES
	private static final String GET_ALL_QUERY_NAME = "qCore_GetAllInvestmentIntentionStatus";
	
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
	public InvestmentIntentionStatus(int statusId, String name, String description) {
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
	 * Gets a Vector of InvestmentIntentionStatus.
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Vector<InvestmentIntentionStatus> getAll(FWFacade facade) throws DataException {
		Vector<InvestmentIntentionStatus> statuses = new Vector<InvestmentIntentionStatus>();
		
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
	public static InvestmentIntentionStatus get(DataResultSet rs) throws DataException {
		if (rs.isEmpty())
			return null;
		
		return new InvestmentIntentionStatus(
			DataResultSetUtils.getResultSetIntegerValue(rs, ID),
			DataResultSetUtils.getResultSetStringValue(rs, NAME),
			DataResultSetUtils.getResultSetStringValue(rs, DESCRIPTION)
		);
	}
	
	/* ************************ Caching ************************************** */	
	private static Cache CACHE = new Cache();
	
	public static Cachable<Integer, InvestmentIntentionStatus> getCache() {
		return CACHE;
	}
	
	/** InvestmentIntentionStatus cache implementor */
	private static class Cache extends Cachable<Integer, InvestmentIntentionStatus> {

		public Cache() {
			super("Investment Intention Status");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<InvestmentIntentionStatus> statuses = InvestmentIntentionStatus.getAll(facade);
			
			HashMap<Integer, InvestmentIntentionStatus> newCache = 
			 new HashMap<Integer, InvestmentIntentionStatus>();
			
			for (InvestmentIntentionStatus status : statuses) {
				newCache.put(status.getStatusId(), status);
			}
			
			this.CACHE_MAP = newCache;
		}
	}
}
