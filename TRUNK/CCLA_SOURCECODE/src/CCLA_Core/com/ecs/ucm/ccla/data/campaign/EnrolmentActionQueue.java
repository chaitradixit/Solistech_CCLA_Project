package com.ecs.ucm.ccla.data.campaign;

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.ucm.ccla.data.Persistable;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.DataResultSetUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/**
 * MODELS the ENROLMENT_ACTION_QUEUE Table
 * @author Cam
 *
 */
public class EnrolmentActionQueue implements Persistable {

	//BINDER AND TABLE COLUMN NAMES
	public static final String QUEUE_ID			= "ENROLMENT_ACTION_QUEUE_ID";
	public static final String ENROLMENT_ID 	= "CAMPAIGN_ENROLMENT_ID";
	public static final String DATE_ADDED		= "DATE_ADDED";
	public static final String DATE_EXECUTED	= "DATE_EXECUTED";
	public static final String IS_COMPLETED		= "IS_COMPLETED";
	public static final String ERROR_MSG		= "ERROR_MESSAGE";
	public static final String ACTION_ID		= "ENROLMENT_ACTION_ID";
	
	private static final String GET_ALL_QUERY_NAME = "qCore_GetAllEnrolmentActionQueue";
	private static final String ADD_QUERY_NAME = "qCore_AddEnrolmentActionQueue";
	private static final String UPDATE_QUERY_NAME = "qCore_UpdateEnrolmentActionQueue";
	private static final String GET_BY_ID_QUERY_NAME = "qCore_GetEnrolmentActionQueue";
	
	private int enrolmentActionQueueId;
	private Integer campaignEnrolmentId;
	private Date dateAdded;
	private Date dateExecuted;
	private boolean isCompleted;
	private String errorMessage;
	private int enrolmentActionId;
	
	public EnrolmentActionQueue(int enrolmentActionQueueId,
			Integer campaignEnrolmentId, Date dateAdded, Date dateExecuted,
			boolean isCompleted, String errorMessage, int enrolmentActionId) {
		this.enrolmentActionQueueId = enrolmentActionQueueId;
		this.campaignEnrolmentId = campaignEnrolmentId;
		this.dateAdded = dateAdded;
		this.dateExecuted = dateExecuted;
		this.isCompleted = isCompleted;
		this.errorMessage = errorMessage;
		this.enrolmentActionId = enrolmentActionId;
	}

	public EnrolmentActionQueue(DataBinder binder) throws DataException {
		this.setAttributes(binder);
	}
	
	public void setAttributes(DataBinder binder) throws DataException {
		this.setEnrolmentActionQueueId(BinderUtils.getBinderIntegerValue(binder, QUEUE_ID));
		this.setCampaignEnrolmentId(BinderUtils.getBinderIntegerValue(binder, ENROLMENT_ID));
		this.setDateAdded(BinderUtils.getBinderDateValue(binder, DATE_ADDED));
		this.setDateExecuted(BinderUtils.getBinderDateValue(binder, DATE_EXECUTED));
		this.setCompleted(BinderUtils.getBinderBoolValue(binder, IS_COMPLETED));
		this.setEnrolmentActionId(BinderUtils.getBinderIntegerValue(binder, ACTION_ID));
		this.setErrorMessage(BinderUtils.getBinderStringValue(binder, ERROR_MSG));
	}

	public void addFieldsToBinder(DataBinder binder) throws DataException {
		BinderUtils.addIntParamToBinder(binder, QUEUE_ID, this.getEnrolmentActionQueueId());
		BinderUtils.addIntParamToBinder(binder, ENROLMENT_ID, this.getCampaignEnrolmentId());
		BinderUtils.addDateParamToBinder(binder, DATE_ADDED, this.getDateAdded());
		BinderUtils.addDateParamToBinder(binder, DATE_EXECUTED ,this.getDateExecuted());
		BinderUtils.addBooleanParamToBinder(binder, IS_COMPLETED, this.isCompleted());
		BinderUtils.addIntParamToBinder(binder, ACTION_ID, this.getEnrolmentActionId());
		BinderUtils.addParamToBinder(binder, ERROR_MSG, this.getErrorMessage());
	}

	public void persist(FWFacade facade, String username) throws DataException {
		this.validate(facade);
		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);
		facade.execute(UPDATE_QUERY_NAME, binder);
		
		//TODO add audit logging!
	}

	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub
		
	}

	public void setEnrolmentActionQueueId(int enrolmentActionQueueId) {
		this.enrolmentActionQueueId = enrolmentActionQueueId;
	}

	public int getEnrolmentActionQueueId() {
		return enrolmentActionQueueId;
	}

	public void setCampaignEnrolmentId(Integer campaignEnrolmentId) {
		this.campaignEnrolmentId = campaignEnrolmentId;
	}

	public Integer getCampaignEnrolmentId() {
		return campaignEnrolmentId;
	}

	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}

	public Date getDateAdded() {
		return dateAdded;
	}

	public void setDateExecuted(Date dateExecuted) {
		this.dateExecuted = dateExecuted;
	}

	public Date getDateExecuted() {
		return dateExecuted;
	}

	public void setCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}

	public boolean isCompleted() {
		return isCompleted;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setEnrolmentActionId(int enrolmentActionId) {
		this.enrolmentActionId = enrolmentActionId;
	}

	public int getEnrolmentActionId() {
		return enrolmentActionId;
	}

	
	/**
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Vector<EnrolmentActionQueue> getAll(FWFacade facade) throws DataException {
		Vector<EnrolmentActionQueue> queueVec = new Vector<EnrolmentActionQueue>();
		
		DataResultSet rs = facade.createResultSet
		 (GET_ALL_QUERY_NAME, new DataBinder());
		
		if (rs.first()) {
			do {
				queueVec.add(get(rs));
			} while (rs.next());
		}
		return queueVec;
	}	
	
	
	
	public static EnrolmentActionQueue get(DataResultSet rs) throws DataException {
		if (rs.isEmpty())
			return null;
		
		return new EnrolmentActionQueue(
			DataResultSetUtils.getResultSetIntegerValue(rs, QUEUE_ID),
			DataResultSetUtils.getResultSetIntegerValue(rs, ENROLMENT_ID),
			rs.getDateValueByName(DATE_ADDED),
			rs.getDateValueByName(DATE_EXECUTED),
			DataResultSetUtils.getResultSetBoolValue(rs, IS_COMPLETED),
			DataResultSetUtils.getResultSetStringValue(rs, ERROR_MSG),
			DataResultSetUtils.getResultSetIntegerValue(rs, ACTION_ID)
		);
	}	
	
	/**
	 * 
	 * @param enrolmentActionQueue
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static EnrolmentActionQueue add(EnrolmentActionQueue enrolmentActionQueue, FWFacade facade) throws DataException 
	{
		if (enrolmentActionQueue.getEnrolmentActionQueueId()==0) {
			enrolmentActionQueue.setEnrolmentActionQueueId(
					Integer.parseInt(
							CCLAUtils.getNewKey("EnrolmentActionQueue", facade)));
		}
		
		enrolmentActionQueue.validate(facade);
		DataBinder binder = new DataBinder();
		enrolmentActionQueue.addFieldsToBinder(binder);
		facade.execute(ADD_QUERY_NAME, binder);
		
		return EnrolmentActionQueue.get(enrolmentActionQueue.getEnrolmentActionQueueId(), facade);
		//TODO add audit logging
	}	
	
	/**
	 * 
	 * @param binder
	 * @param username
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static EnrolmentActionQueue add(DataBinder binder, String username, FWFacade facade) 
	throws DataException {
		EnrolmentActionQueue enrolmentActionQueue = new EnrolmentActionQueue(binder);
		return EnrolmentActionQueue.add(enrolmentActionQueue, facade);
	}
	
	/**
	 * 
	 * @param enrolmentActionQueueId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static EnrolmentActionQueue get(int enrolmentActionQueueId, FWFacade facade) 
	 throws DataException {
		DataResultSet rs = getData(enrolmentActionQueueId, facade);
		return get(rs);
	}
	
	/**
	 * 
	 * @param enrolmentActionQueueId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getData(int enrolmentActionQueueId, FWFacade facade) 
	 throws DataException {
		DataBinder binder = new DataBinder();
		
		BinderUtils.addIntParamToBinder(binder, QUEUE_ID, enrolmentActionQueueId);
		DataResultSet rsEnrolmentActionQueue = facade.createResultSet
		 (GET_BY_ID_QUERY_NAME, binder);
		
		return rsEnrolmentActionQueue;
	}	
	
	/* *************************** Cache **************************** */ 
	/*
	 *  Caching stuff
	 */
	private static Cache CACHE = new Cache();
	
	public static Cachable<Integer, EnrolmentActionQueue> getCache() {
		return CACHE;
	}
	
	/** EnrolmentActionQueue cache implementor */
	private static class Cache extends Cachable<Integer, EnrolmentActionQueue> {

		public Cache() {
			super("Enrolment Action Queue");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<EnrolmentActionQueue> actionQueue = EnrolmentActionQueue.getAll(facade);
			
			HashMap<Integer, EnrolmentActionQueue> newCache = 
			 new HashMap<Integer, EnrolmentActionQueue>();
			
			for (EnrolmentActionQueue enrolmentActionQueue : actionQueue) {
				newCache.put(enrolmentActionQueue.getEnrolmentActionQueueId(), enrolmentActionQueue);
			}
			
			this.CACHE_MAP = newCache;
		}
	}	
	
	
}
