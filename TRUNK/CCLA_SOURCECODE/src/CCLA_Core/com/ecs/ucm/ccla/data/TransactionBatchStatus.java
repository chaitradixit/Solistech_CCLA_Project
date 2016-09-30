package com.ecs.ucm.ccla.data;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.util.HashMap;
import java.util.Vector;

import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.utils.stellent.DataResultSetUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

public class TransactionBatchStatus {
	/* **************** Constants **************** */
	//BINDER AND DB COLUMN
	public static final String ID = "TRANS_BATCH_STATUS_ID";
	public static final String NAME = "STATUS_NAME";
	
	public static final class Statuses {
		public static final int PENDING_STATUS 		= 0;
		public static final int PROCESSED_STATUS 	= 1;
		public static final int COMPLETED_STATUS 	= 2;
		public static final int ERROR_STATUS 		= 3;	
	}
	
	//QUERIES
	private static final String GET_ALL_QUERY_NAME = "qCore_GetAllTransactionBatchStatus";
	
	/* **************** Properties **************** */
	private int statusId;
	private String name;

	
	/* ********************** Constructor ************************* */
	/**
	 * Constructor
	 * @param statusId
	 * @param name
	 */
	public TransactionBatchStatus(int statusId, String name) {
		this.statusId = statusId;
		this.name = name;
	}
	
	/* ************************* Methods ************************* */
	public int getStatusId() { return statusId; }
	public void setStatusId(int statusId) { this.statusId = statusId; }
	
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	

	/**
	 * Gets a Vector of TransBatchStatus.
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Vector<TransactionBatchStatus> getAll(FWFacade facade) throws DataException {
		Vector<TransactionBatchStatus> statuses = new Vector<TransactionBatchStatus>();
		
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
	public static TransactionBatchStatus get(DataResultSet rs) throws DataException {
		if (rs.isEmpty())
			return null;
		
		return new TransactionBatchStatus(
			DataResultSetUtils.getResultSetIntegerValue(rs, ID),
			DataResultSetUtils.getResultSetStringValue(rs, NAME)
		);
	}
	
	/* ************************ Caching ************************************** */	
	private static Cache CACHE = new Cache();
	
	public static Cachable<Integer, TransactionBatchStatus> getCache() {
		return CACHE;
	}
	
	/** CommStatus cache implementor */
	private static class Cache extends Cachable<Integer, TransactionBatchStatus> {

		public Cache() {
			super("Transaction Batch Status");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<TransactionBatchStatus> statuses = TransactionBatchStatus.getAll(facade);
			
			HashMap<Integer, TransactionBatchStatus> newCache = 
			 new HashMap<Integer, TransactionBatchStatus>();
			
			for (TransactionBatchStatus status : statuses) {
				newCache.put(status.getStatusId(), status);
			}
			
			this.CACHE_MAP = newCache;
		}
	}
	
}
