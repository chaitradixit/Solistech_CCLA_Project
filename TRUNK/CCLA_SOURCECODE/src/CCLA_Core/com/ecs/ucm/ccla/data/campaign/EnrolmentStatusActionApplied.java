package com.ecs.ucm.ccla.data.campaign;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.util.HashMap;
import java.util.Vector;

import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.DataResultSetUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/**
 * MODELS the ENROLMENT_STATUS_ACTION_APPL Table
 * @author Cam
 *
 */
public class EnrolmentStatusActionApplied {

	public static final String CAMPAIGN_ID 		= "CAMPAIGN_ID";
	public static final String ENROL_STATUS_ID	= "ENROLMENT_STATUS_ID";
	public static final String ENROL_ACTION_ID	= "ENROLMENT_ACTION_ID";
	
	private static final String GET_ALL_QUERY_NAME = "qCore_GetAllEnrolmentStatusActionApplied";
	private static final String ADD_QUERY_NAME = "qCore_AddEnrolmentStatusActionApplied";
	private static final String GET_BY_ID_QUERY_NAME = "qCore_GetEnrolmentStatusActionAppliedById";
	
	private int campaignId;
	private int enrolmentStatusId;
	private int enrolmentActionId;
	
	public EnrolmentStatusActionApplied(int campaignId, 
			int enrolmentStatusId, int enrolmentActionId) {
		this.campaignId = campaignId;
		this.enrolmentStatusId = enrolmentStatusId;
		this.enrolmentActionId = enrolmentActionId;
	}
	
	public EnrolmentStatusActionApplied(DataBinder binder) throws DataException {
		this.setAttributes(binder);
	}
	
	public void setCampaignId(int campaignId) {
		this.campaignId = campaignId;
	}
	public int getCampaignId() {
		return campaignId;
	}
	public void setEnrolmentStatusId(int enrolmentStatusId) {
		this.enrolmentStatusId = enrolmentStatusId;
	}
	public int getEnrolmentStatusId() {
		return enrolmentStatusId;
	}
	public void setEnrolmentActionId(int enrolmentActionId) {
		this.enrolmentActionId = enrolmentActionId;
	}
	public int getEnrolmentActionId() {
		return enrolmentActionId;
	}
	
	public static EnrolmentStatusActionApplied add(EnrolmentStatusActionApplied enrolmentStatusActionApp, FWFacade facade) throws DataException 
	{
		enrolmentStatusActionApp.validate(facade);
		
		DataBinder binder = new DataBinder();
		enrolmentStatusActionApp.addFieldsToBinder(binder);
		facade.execute(ADD_QUERY_NAME, binder);
		
		return EnrolmentStatusActionApplied.get(enrolmentStatusActionApp.getCampaignId(), 
				enrolmentStatusActionApp.getEnrolmentStatusId(), enrolmentStatusActionApp.getEnrolmentActionId(), facade);
	}
	
	
	public static EnrolmentStatusActionApplied add(DataBinder binder, String username, FWFacade facade) 
	throws DataException {
		EnrolmentStatusActionApplied enrolmentStatusActionApplied = new EnrolmentStatusActionApplied(binder);
		return EnrolmentStatusActionApplied.add(enrolmentStatusActionApplied, facade);
	}
	
	/**
	 * 
	 * @param campaignId
	 * @param enrolmentStatusId
	 * @param enrolmentActionId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static EnrolmentStatusActionApplied get(int campaignId, int enrolmentStatusId, 
			int enrolmentActionId, FWFacade facade) throws DataException {
		DataResultSet rs = getData(campaignId, enrolmentStatusId, enrolmentActionId, facade);
		return get(rs);
	}
	
	public static DataResultSet getData(int campaignId, int enrolmentStatusId, 
			int enrolmentActionId, FWFacade facade) throws DataException {
		DataBinder binder = new DataBinder();
		
		BinderUtils.addIntParamToBinder(binder, CAMPAIGN_ID, campaignId);
		BinderUtils.addIntParamToBinder(binder, ENROL_STATUS_ID, enrolmentStatusId);
		BinderUtils.addIntParamToBinder(binder, ENROL_ACTION_ID, enrolmentActionId);
		DataResultSet rsEnrolmentStatusActionApplied = facade.createResultSet
		 (GET_BY_ID_QUERY_NAME, binder);
		
		return rsEnrolmentStatusActionApplied;
	}	
	
	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub
		
	}
		
	/* ************************ Caching ************************************** */	
	private static Cache CACHE = new Cache();
	
	/** Mapping between Campaign IDs and a mapping of Campaign Enrolment Statuses ->
	 *  Available Actions.
	 *  
	 * @return
	 */
	public static Cachable<Integer, EnrolmentStatusMap> getCache() {
		return CACHE;
	}
	
	/** EnrolmentStatusActionApplied cache implementor */
	private static class Cache extends Cachable<Integer, EnrolmentStatusMap> {

		public Cache() {
			super("Enrolment Status Action Applied");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<EnrolmentStatusActionApplied> actions = 
			 EnrolmentStatusActionApplied.getAll(facade);
			
			HashMap<Integer,EnrolmentStatusMap> newCache = 
			 new HashMap<Integer,EnrolmentStatusMap>();
			
			
			for (EnrolmentStatusActionApplied action : actions) {
				EnrolmentStatusMap enrolmentStatusMap = null;
				
				if (newCache.containsKey(action.getCampaignId())) {
					
					enrolmentStatusMap = (EnrolmentStatusMap)newCache.get(action.getCampaignId());
					
					if (enrolmentStatusMap.containsStatusId(action.getEnrolmentStatusId())) {
						if (!enrolmentStatusMap.containsActionIdForStatusId(action.getEnrolmentStatusId(), action.getEnrolmentActionId()))
						{
							HashMap<Integer, Vector<Integer>> statusActionMap = enrolmentStatusMap.getStatusActionMap();
							
							Vector<Integer> actionVec =  statusActionMap.get(action.getEnrolmentStatusId());
							actionVec.add(action.getEnrolmentActionId());
							
							statusActionMap.put(action.getEnrolmentStatusId(), actionVec);
							
							enrolmentStatusMap.setStatusActionMap(statusActionMap);
							newCache.put(action.getCampaignId(), enrolmentStatusMap);
						} else {
							Log.warn("Already contains statusId:"+action.getEnrolmentStatusId()+
									" and ActionId:"+action.getEnrolmentActionId()+
									" for CampaignId:"+action.getCampaignId());
						}
					} else {
						Vector<Integer> actionVec = new Vector<Integer>();
						actionVec.add(action.getEnrolmentActionId());
						
						HashMap<Integer, Vector<Integer>> statusActionMap = enrolmentStatusMap.getStatusActionMap();
						statusActionMap.put(action.getEnrolmentStatusId(), actionVec);
						
						enrolmentStatusMap.setStatusActionMap(statusActionMap);
						newCache.put(action.getCampaignId(), enrolmentStatusMap);
					}
					
				} else {
					enrolmentStatusMap = new EnrolmentStatusMap(); 
					enrolmentStatusMap.setCampaignId(action.getCampaignId());
					
					Vector<Integer> actionVec = new Vector<Integer>();
					actionVec.add(action.getEnrolmentActionId());
					
					HashMap<Integer,Vector<Integer>> statusActionMap = new HashMap<Integer,Vector<Integer>>();
					statusActionMap.put(action.getEnrolmentStatusId(), actionVec);
					
					enrolmentStatusMap.setStatusActionMap(statusActionMap);
					newCache.put(action.getCampaignId(), enrolmentStatusMap);
				}
			}
			
			this.CACHE_MAP = newCache;
		}
	}	
	
	
	public static EnrolmentStatusActionApplied get(DataResultSet rs) throws DataException {
		if (rs.isEmpty())
			return null;
		
		return new EnrolmentStatusActionApplied(
			DataResultSetUtils.getResultSetIntegerValue(rs, CAMPAIGN_ID),
			DataResultSetUtils.getResultSetIntegerValue(rs, ENROL_STATUS_ID),
			DataResultSetUtils.getResultSetIntegerValue(rs, ENROL_ACTION_ID)
		);
	}	
	
	/**
	 * Get Vector of EnrolmentStatusActionApplied (all entries in the table)
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Vector<EnrolmentStatusActionApplied> getAll(FWFacade facade) throws DataException {
		Vector<EnrolmentStatusActionApplied> appliedVec = new Vector<EnrolmentStatusActionApplied>();
		
		DataResultSet rs = facade.createResultSet
		 (GET_ALL_QUERY_NAME, new DataBinder());
		
		if (rs.first()) {
			do {
				appliedVec.add(get(rs));
			} while (rs.next());
		}
		return appliedVec;
	}	
	
	
	/* ***************** Inner Wrapper Class ***************** */
	public static class EnrolmentStatusMap {
		public int campaignId;
		public HashMap<Integer, Vector<Integer>> statusActionMap
			= new HashMap<Integer, Vector<Integer>>();
	
		public EnrolmentStatusMap() {}
		
		public Vector<Integer> getActions(int statusId) {
			return statusActionMap.get(statusId);
		}
		
		public boolean containsStatusId(int statusId) {
			return statusActionMap.containsKey(statusId);
		}
		
		public boolean containsActionIdForStatusId(int statusId, int actionId) {
			return statusActionMap.containsKey(statusId) 
			&& ((Vector<Integer>)statusActionMap.get(statusId)).contains(actionId);
		}
		
		public HashMap<Integer, Vector<Integer>> getStatusActionMap() {
			return statusActionMap;
		}
		
		public void setStatusActionMap(HashMap<Integer, Vector<Integer>> statusActionMap) {
			this.statusActionMap = statusActionMap;
		}
		
		public void setCampaignId(int campaignId) { this.campaignId = campaignId; }
		public int getCampaignId() { return campaignId; }
		
		/**
		 * String representation of the object.
		 */
		public String toString() {
			final StringBuffer sb = new StringBuffer();
			sb.append("CampaignId: "+this.getCampaignId());
			sb.append(" [ ");
			
			for (Integer statusId: this.getStatusActionMap().keySet()) {
				Vector<Integer> actionVec = this.getStatusActionMap().get(statusId);
				sb.append("StatusId: "+statusId);
				sb.append(" [ ActionId: ");
				int count=0;
				for (Integer actionId: actionVec) {
					
					if (count!=0)
						sb.append(",");
					sb.append(actionId);
					count++;
				}
				sb.append(" ] ");
			}
			sb.append(" ]");
			return sb.toString();
		}
	}

	/**
	 * 
	 * @param binder
	 * @throws DataException
	 */
	public void setAttributes(DataBinder binder) throws DataException {
		this.setCampaignId(BinderUtils.getBinderIntegerValue(binder, CAMPAIGN_ID));
		this.setEnrolmentStatusId(BinderUtils.getBinderIntegerValue(binder, ENROL_STATUS_ID));
		this.setEnrolmentActionId(BinderUtils.getBinderIntegerValue(binder, ENROL_ACTION_ID));
	}

	/**
	 * 
	 * @param binder
	 * @throws DataException
	 */
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		BinderUtils.addIntParamToBinder(binder, CAMPAIGN_ID, this.getCampaignId());
		BinderUtils.addIntParamToBinder(binder, ENROL_STATUS_ID, this.getEnrolmentStatusId());
		BinderUtils.addIntParamToBinder(binder, ENROL_ACTION_ID, this.getEnrolmentActionId());

	}

}
