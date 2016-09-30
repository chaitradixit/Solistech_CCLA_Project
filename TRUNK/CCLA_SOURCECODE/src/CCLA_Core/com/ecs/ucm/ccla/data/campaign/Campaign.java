package com.ecs.ucm.ccla.data.campaign;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.ucm.ccla.data.Audit;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.Globals;
import com.ecs.ucm.ccla.data.Persistable;
import com.ecs.utils.ClassLoaderUtils;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.DataResultSetUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/**
 * MODELS CAMPAIGN TABLE
 * @author Cam
 *
 */

public class Campaign implements Persistable{

	//BINDER VALUES AND DB COLUMN
	public static final class Cols {
		public static final String CAMPAIGN_ID = "CAMPAIGN_ID";
		public static final String NAME = "CAMPAIGN_NAME";
		public static final String DESCRIPTION = "CAMPAIGN_DESCRIPTION";
		public static final String OWNER = "CAMPAIGN_OWNER";
		public static final String DATE_ADDED = "DATE_ADDED";
		public static final String LAST_UPDATED = "LAST_UPDATED"; 
		public static final String ENROL_START_DATE = "ENROLMENT_START_DATE";
		public static final String ENROL_END_DATE = "ENROLMENT_END_DATE";
		public static final String START_DATE = "START_DATE";
		public static final String END_DATE = "END_DATE";
		public static final String STATUS_ID = "CAMPAIGN_STATUS_ID";
		public static final String HANDLER_CLASS = "PROCESS_HANDLER_CLASS";
		public static final String LAST_UPDATED_BY = "LAST_UPDATED_BY";
		public static final String IDOC_INCLUDE	= "IDOC_INCLUDE_INFIX";
		public static final String DEFAULT_ENROLMENT_STATUS_ID = 
		 "DEFAULT_ENROLMENT_STATUS_ID";
	}
	
	//QUERY NAMES
	private static final String ADD_QUERY_NAME = "qCore_AddCampaign";
	private static final String UPDATE_QUERY_NAME = "qCore_UpdateCampaign";
	private static final String GET_ALL_QUERY_NAME = "qCore_GetAllCampaigns";
	private static final String GET_BY_ID_QUERY_NAME = "qCore_GetCampaignById";
	
	
	/* ****************** Properties ********************* */
	private int id; //campaign id
	private String name; //campaign name
	private String description; //campaign description
	private String owner; //campaign owner
	private Date enrolStartDate; //enrolment start date
	private Date enrolEndDate; //enrolment end date
	private Date startDate; //campaign start date
	private Date endDate; //campaign end date
	private String processHandlerClass; //process handler class
	private String idocInclude; //idoc include file.
	private Date dateAdded; //date added to database
	private Date lastUpdated; //date campaign was last updated
	private String lastUpdatedBy; //user that last updated the campaign.
	private int statusId; //campaign status id
	private int defaultEnrolmentStatusId; //default enrolment status id
	/* ***************************** Constructors ****************************** */
	/**
	 * Construction
	 * @param id
	 * @param name
	 * @param description
	 * @param owner
	 * @param enrolStartDate
	 * @param enrolEndDate
	 * @param startDate
	 * @param endDate
	 * @param statusId
	 * @param processHandlerClass
	 * @param idocInclude
	 * @param dateAdded
	 * @param lastUpdated
	 * @param lastUpdatedBy
	 */
	public Campaign(int id, String name, String description, String owner, 
			Date enrolStartDate, Date enrolEndDate, Date startDate, Date endDate,
			int statusId, String processHandlerClass, String idocInclude, 
			Date dateAdded, Date lastUpdated, String lastUpdatedBy, int defaultEnrolmentStatusId) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.owner = owner;
		this.enrolStartDate = enrolStartDate;
		this.enrolEndDate = enrolEndDate;
		this.startDate = startDate;
		this.endDate = endDate;
		this.statusId = statusId;
		this.processHandlerClass = processHandlerClass;
		this.idocInclude = idocInclude;
		this.dateAdded = dateAdded;
		this.lastUpdated = lastUpdated;
		this.lastUpdatedBy = lastUpdatedBy;
		this.defaultEnrolmentStatusId = defaultEnrolmentStatusId;
	}
	
	/**
	 * Constructor
	 * @param binder
	 * @throws DataException
	 */
	public Campaign(DataBinder binder) throws DataException {
		this.setAttributes(binder);
	}
	
	
	/* ****************** Methods ******************** */
	
	public int getCampaignId() { return id; }
	public void setCampaignId(int id) { this.id = id; }
	
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	
	public String getOwner() { return owner; }
	public void setOwner(String owner) { this.owner = owner; }
	
	public Date getEnrolmentStartDate() { return enrolStartDate; }
	public void setEnrolmentStartDate(Date enrolStartDate) { this.enrolStartDate = enrolStartDate; }

	public Date getEnrolmentEndDate() { return enrolEndDate; }
	public void setEnrolmentEndDate(Date enrolEndDate) { this.enrolStartDate = enrolEndDate; }

	public Date getStartDate() { return startDate; }
	public void setStartDate(Date startDate) { this.startDate = startDate; }

	public Date getEndDate() { return endDate; }
	public void setEndDate(Date endDate) { this.endDate = endDate; }
	
	public String getProcessHandlerClass() { return processHandlerClass; }
	public void setProcessHandlerClass(String processHandlerClass) { this.processHandlerClass = processHandlerClass; }
	
	public String getIdocInclude() { return idocInclude; }
	public void setIdocInclude(String idocInclude) { this.idocInclude = idocInclude; }

	public Date getDateAdded() { return dateAdded; }
	public void setDateAdded(Date dateAdded) { this.dateAdded = dateAdded; }

	public Date getLastUpdated() { return lastUpdated; }
	public void setLastUpdated(Date lastUpdated) { this.lastUpdated = lastUpdated; }

	public String getLastUpdatedBy() { return lastUpdatedBy; }
	public void setLastUpdatedBy(String lastUpdatedBy) { this.lastUpdatedBy = lastUpdatedBy; }
	
	public int getStatusId() { return statusId; }
	public void setStatusId(int statusId) { this.statusId = statusId; }

	public int getDefaultEnrolmentStatusId() { return defaultEnrolmentStatusId; }
	public void setDefaultEnrolmentStatusId(int defaultEnrolmentStatusId) { this.defaultEnrolmentStatusId = defaultEnrolmentStatusId; }
	/**
	 * Gets a Vector<Campaign> of all the campaigns
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Vector<Campaign> getAll(FWFacade facade) throws DataException 
	{
		Vector<Campaign> campaigns = new Vector<Campaign>();
		
		DataResultSet rs = facade.createResultSet
		 (GET_ALL_QUERY_NAME, new DataBinder());
		
		if (rs.first()) {
			do {
				campaigns.add(get(rs));
			} while (rs.next());
		}	
		return campaigns;
	}
	
	/**
	 * Gets a Campaign Object for the DataResultSet
	 * @param rs
	 * @return
	 * @throws DataException
	 */
	public static Campaign get(DataResultSet rs) throws DataException {
		if (rs.isEmpty())
			return null;
		
		return new Campaign(
				DataResultSetUtils.getResultSetIntegerValue(rs, Cols.CAMPAIGN_ID),
				DataResultSetUtils.getResultSetStringValue(rs, Cols.NAME),
				DataResultSetUtils.getResultSetStringValue(rs, Cols.DESCRIPTION),
				DataResultSetUtils.getResultSetStringValue(rs, Cols.OWNER),
				rs.getDateValueByName(Cols.ENROL_START_DATE),
				rs.getDateValueByName(Cols.ENROL_END_DATE),
				rs.getDateValueByName(Cols.START_DATE),
				rs.getDateValueByName(Cols.END_DATE),
				DataResultSetUtils.getResultSetIntegerValue(rs, Cols.STATUS_ID),				
				DataResultSetUtils.getResultSetStringValue(rs, Cols.HANDLER_CLASS),
				DataResultSetUtils.getResultSetStringValue(rs, Cols.IDOC_INCLUDE),
				rs.getDateValueByName(Cols.DATE_ADDED),
				rs.getDateValueByName(Cols.LAST_UPDATED),
				DataResultSetUtils.getResultSetStringValue(rs, Cols.LAST_UPDATED_BY),
				DataResultSetUtils.getResultSetIntegerValue(rs, Cols.DEFAULT_ENROLMENT_STATUS_ID)
				);
	}
	
	/**
	 * Adds a new Campaign to the database
	 * @param binder
	 * @param username
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Campaign add(DataBinder binder, String username, FWFacade facade) 
	throws DataException {
		Campaign rule = new Campaign(binder);
		return Campaign.add(rule, username, facade);
	}
	
	/**
	 * Add a campaign to the database
	 * @param campaign
	 * @param username
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Campaign add(Campaign campaign, String username, FWFacade facade) throws DataException 
	{
		if (campaign.getCampaignId()==0) {
			campaign.setCampaignId(
					Integer.parseInt(
							CCLAUtils.getNewKey("Campaign", facade)));
		}
		
		campaign.setLastUpdatedBy(username);
		campaign.validate(facade);
		
		DataBinder binder = new DataBinder();
		campaign.addFieldsToBinder(binder);
		facade.execute(ADD_QUERY_NAME, binder);
		
		
		// Add audit record
		DataResultSet afterData = CampaignEnrolment.getData(campaign.getCampaignId(), facade);
		
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(campaign.getCampaignId(), ElementType.SecondaryElementType.Campaign.toString());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.ADD.toString(), 
		 ElementType.SecondaryElementType.Campaign.toString(), 
		 null, afterData, auditRelations);	
		
		return Campaign.get(campaign.getCampaignId(), facade);
	}
	
	/**
	 * Gets a single campaign from the campaignId 
	 * @param campaignId
	 * @param facade
	 * @return Campaign, or null if not found.
	 * @throws DataException
	 */
	public static Campaign get(int campaignId, FWFacade facade) 
	 throws DataException {
		DataResultSet rs = getData(campaignId, facade);
		return get(rs);
	}
	
	/** 
	 * Fetches ResultSet for a single Campaign by Id.
	 * @param campaignId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getData(int campaignId, FWFacade facade) 
	 throws DataException {
		DataBinder binder = new DataBinder();
		
		BinderUtils.addIntParamToBinder(binder, Cols.CAMPAIGN_ID, campaignId);
		DataResultSet rsCampaign = facade.createResultSet
		 (GET_BY_ID_QUERY_NAME, binder);
		
		return rsCampaign;
	}	
	
	/* *************************** Cache **************************** */ 
	/*
	 *  Caching stuff
	 */
	private static Cache CACHE = new Cache();
	
	public static Cachable<Integer, Campaign> getCache() {
		return CACHE;
	}
	
	/** Condition Operator cache implementor */
	private static class Cache extends Cachable<Integer, Campaign> {

		public Cache() {
			super("Campaigns");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			Vector<Campaign> campaigns = Campaign.getAll(facade);
			
			HashMap<Integer, Campaign> newCache = 
			 new HashMap<Integer, Campaign>();
			
			for (Campaign campaign : campaigns) {
				newCache.put(campaign.getCampaignId(), campaign);
			}
			
			this.CACHE_MAP = newCache;
		}
	}

	/* ******************** Persistable Interface ************************ */
	
	public void setAttributes(DataBinder binder) throws DataException 
	{
		
		Integer campaignId = BinderUtils.getBinderIntegerValue(binder, Cols.CAMPAIGN_ID);
		if (campaignId!=null)
			this.setCampaignId(id);
		
		this.setName(BinderUtils.getBinderStringValue(binder, Cols.NAME));
		this.setDescription(BinderUtils.getBinderStringValue(binder, Cols.DESCRIPTION));
		this.setOwner(BinderUtils.getBinderStringValue(binder, Cols.OWNER));		
		this.setEnrolmentStartDate(BinderUtils.getBinderDateValue(binder, Cols.ENROL_START_DATE));
		this.setEnrolmentEndDate(BinderUtils.getBinderDateValue(binder, Cols.ENROL_END_DATE));
		this.setStartDate(BinderUtils.getBinderDateValue(binder, Cols.START_DATE));
		this.setEndDate(BinderUtils.getBinderDateValue(binder, Cols.END_DATE));
		
		Integer statusId = BinderUtils.getBinderIntegerValue(binder, Cols.STATUS_ID);
		if (statusId!=null)
			this.setStatusId(statusId);
			
		this.setProcessHandlerClass(BinderUtils.getBinderStringValue(binder, Cols.HANDLER_CLASS));		
		this.setIdocInclude(BinderUtils.getBinderStringValue(binder, Cols.IDOC_INCLUDE));		
		this.setDateAdded(BinderUtils.getBinderDateValue(binder, Cols.DATE_ADDED));
		this.setLastUpdated(BinderUtils.getBinderDateValue(binder, Cols.LAST_UPDATED));
		this.setLastUpdatedBy(BinderUtils.getBinderStringValue(binder, Cols.LAST_UPDATED_BY));
		this.setDefaultEnrolmentStatusId(BinderUtils.getBinderIntegerValue(binder, Cols.DEFAULT_ENROLMENT_STATUS_ID));
	}


	public void addFieldsToBinder(DataBinder binder) throws DataException 
	{
		BinderUtils.addIntParamToBinder(binder, Cols.CAMPAIGN_ID, this.getCampaignId());
		BinderUtils.addParamToBinder(binder, Cols.NAME, this.getName());
		BinderUtils.addParamToBinder(binder, Cols.DESCRIPTION, this.getDescription());
		BinderUtils.addParamToBinder(binder, Cols.OWNER, this.getOwner());
		BinderUtils.addDateParamToBinder(binder, Cols.ENROL_START_DATE, this.getEnrolmentStartDate());
		BinderUtils.addDateParamToBinder(binder, Cols.ENROL_END_DATE, this.getEnrolmentEndDate());
		BinderUtils.addDateParamToBinder(binder, Cols.START_DATE, this.getStartDate());
		BinderUtils.addDateParamToBinder(binder, Cols.END_DATE, this.getEndDate());
		BinderUtils.addIntParamToBinder(binder, Cols.STATUS_ID, this.getStatusId());
		BinderUtils.addParamToBinder(binder, Cols.HANDLER_CLASS, this.getProcessHandlerClass());
		BinderUtils.addParamToBinder(binder, Cols.IDOC_INCLUDE, this.getIdocInclude());
		BinderUtils.addDateParamToBinder(binder, Cols.DATE_ADDED, this.getDateAdded());
		BinderUtils.addDateParamToBinder(binder, Cols.LAST_UPDATED, this.getLastUpdated());
		BinderUtils.addParamToBinder(binder, Cols.LAST_UPDATED_BY, this.getLastUpdatedBy());
		BinderUtils.addIntParamToBinder(binder, Cols.DEFAULT_ENROLMENT_STATUS_ID, this.getDefaultEnrolmentStatusId());
		
	}

	public void persist(FWFacade facade, String username) throws DataException {
		this.validate(facade);
		this.setLastUpdatedBy(username);
		DataBinder binder = new DataBinder();		
		this.addFieldsToBinder(binder);
		
		DataResultSet beforeData = Campaign.getData(this.getCampaignId(), facade);
		
		facade.execute(UPDATE_QUERY_NAME, binder);	
		
		DataResultSet afterData = Campaign.getData(this.getCampaignId(), facade);
		
		// Add audit record
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(this.getCampaignId(), ElementType.SecondaryElementType.Campaign.toString());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.UPDATE.toString(), 
		 ElementType.SecondaryElementType.Campaign.toString(), 
		 beforeData, afterData, auditRelations);		
	}


	public void validate(FWFacade facade) throws DataException {
		// TODO Auto-generated method stub
		
	}
	
	
	/**
	 * Returns a fresh instance of the Enrolment handler class!
	 * @return
	 * @throws ServiceException
	 */
	public IEnrolmentHandler getEnrolmentHandlerInstance(String userName, FWFacade facade) throws ServiceException{
		
		String className = this.processHandlerClass;
		Class handlerClass = null;

		if (StringUtils.stringIsBlank(className)) {
			Log.info("Using default client process handler");
			throw new ServiceException("process handler class missing");
		} else {
			Log.info("Loading client process handler: " + className);
			try {
				handlerClass = 
					ClassLoaderUtils.getComponentClassLoader().loadClass(className);
				
				Class[] params = 
					new Class[] {Campaign.class, String.class, FWFacade.class };
					
				Object[] values = 
					new Object[] {this, userName, facade};
					
				Constructor constructor = handlerClass.getConstructor(params);
					
				return (IEnrolmentHandler)constructor.newInstance(values);

			} catch (Exception e) {
				Log.error("unable to load process handler "+e.getMessage(), e);
				throw new ServiceException("unable to load process handler "+e.getMessage());
			}
		}
	}
	
	
}
