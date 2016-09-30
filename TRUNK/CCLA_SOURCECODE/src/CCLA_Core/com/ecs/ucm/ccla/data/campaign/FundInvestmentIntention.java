package com.ecs.ucm.ccla.data.campaign;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.Audit;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Fund;
import com.ecs.ucm.ccla.data.Globals;
import com.ecs.ucm.ccla.data.Note;
import com.ecs.ucm.ccla.data.Persistable;
import com.ecs.ucm.ccla.data.SharedCols;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Represents a single entry from the FUND_INVESTMENT_INTENTION table.
 *  
 *  Each Investment Intention is used to store a pledge made by a Person/Organisation
 *  to invest money into a particular CCLA fund.
 *  
 *  Their pledged amount is specified in either Cash or Units.
 *  
 * @author Tom
 *
 */
public class FundInvestmentIntention implements Persistable {
	
	private int investmentIntentionId;
	
	private Fund fund;
	private Integer organisationId;
	private Integer personId;
	
	private Float cash;
	private Float units;
	
	private Date dateAdded;
	private Date lastUpdated;
	private String userId;
	private String lastUpdatedBy;
	private Integer accountId;

	private Integer campaignId;
	private Integer investmentIntentionStatusId;
	

	public static class Cols {
		public static final String ID = "INVESTMENT_INTENTION_ID";
		public static final String INVESTMENT_INTENTION_STATUS_ID = "INVINTENTSTATUS_ID";
	}
	
	public static final String ADD_INTENTION_QUERY = "qCore_AddInvestmentIntention";
	public static final String UPDATE_INTENTION_QUERY = "qCore_UpdateInvestmentIntention";
	public static final String DELETE_INTENTION_QUERY = "qCore_DeleteInvestmentIntention";
	public static final String GET_INTENTION_BY_ID_QUERY = "qCore_GetInvestmentIntentionById";
	
	public static final String GET_INTENTION_BY_ACCOUNT_ID_QUERY = 
	 "qCore_GetInvestmentIntentionByAccountId";
	
	public static final String GET_INTENTIONS_BY_ORG_AND_CAMPAIGN_ID_QUERY = 
		 "qCore_GetInvestmentIntentionsByOrgAndCampaignId";
	
	public FundInvestmentIntention(int investmentIntentionId, Fund fund,
		Integer organisationId, Integer personId, Float cash, Float units,
		Date dateAdded, Date lastUpdated, String userId,
		String lastUpdatedBy, Integer campaignId, 
		Integer investmentIntentionStatusId, Integer accountId) {
		
		this.investmentIntentionId = investmentIntentionId;
		
		this.fund = fund;
		this.organisationId = organisationId;
		this.personId = personId;
		
		this.cash = cash;
		this.units = units;
		
		this.dateAdded = dateAdded;
		this.lastUpdated = lastUpdated;
		this.userId = userId;
		this.lastUpdatedBy = lastUpdatedBy;
		
		this.campaignId = campaignId;
		this.investmentIntentionStatusId = investmentIntentionStatusId;
		this.accountId = accountId;
	}

	/** 
	 * Fetches ResultSet for a single FundInvestmentIntention by Id.
	 * @param fundInvestmentIntentionId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getData(int fundInvestmentIntentionId, FWFacade facade) 
	 throws DataException 
	 {
		DataBinder binder = new DataBinder();
		BinderUtils.addIntParamToBinder(binder, Cols.ID, fundInvestmentIntentionId);
		DataResultSet rsIntention = facade.createResultSet(GET_INTENTION_BY_ID_QUERY, binder);
		return rsIntention;
	}	
	
	public static FundInvestmentIntention add(Fund fund, Integer orgId, Integer personId, 
	 Float cash, Float units, Integer campaignId, Integer investmentIntentionStatusId, Integer accountId, FWFacade facade, String userName) 
	 throws DataException {
		
		int investmentIntentionId = Integer.parseInt(
		 CCLAUtils.getNewKey("InvestmentIntention", facade));
		
		FundInvestmentIntention newIntention = new FundInvestmentIntention(
			investmentIntentionId,
			fund,
			orgId,
			personId,
			cash,
			units,
			null,
			null,
			userName,
			userName,
			campaignId,
			investmentIntentionStatusId,
			accountId
		);
		
		newIntention.validate(facade);
		
		DataBinder binder = new DataBinder();
		newIntention.addFieldsToBinder(binder);
		
		facade.execute(ADD_INTENTION_QUERY, binder);
	
		return get(investmentIntentionId, facade);
	}
	
	public static FundInvestmentIntention get(int investmentIntentionId, FWFacade facade) 
	throws DataException
	{
		DataResultSet rs = getData(investmentIntentionId, facade);
		return get(rs);
	}
	
	/** Fetch an Investment Intention for the given Account ID, if one exists.
	 *  
	 *  Returns null if there is no logged intention for the account.
	 *  
	 * @param accountId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static FundInvestmentIntention getByAccountId
	 (int accountId, FWFacade facade) throws DataException {
		DataResultSet rs = getDataByAccountId(accountId, facade);
		return get(rs);
	}
	
	public static DataResultSet getDataByAccountId
	 (int accountId, FWFacade facade) throws DataException {
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, Account.Cols.ID, accountId);
		
		return facade.createResultSet(GET_INTENTION_BY_ACCOUNT_ID_QUERY, binder);
	}
	
	/** Fetches the set of all investment intentions for the given organisation and
	 *  campaign.
	 *  
	 * @param orgId
	 * @param campaignId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Vector<FundInvestmentIntention> getByOrgAndCampaignId
	(int orgId, int campaignId, FWFacade facade) throws DataException {
		DataResultSet rs = getDataByOrgAndCampaignId(orgId, campaignId, facade);
		
		Vector<FundInvestmentIntention> intentions = 
		 new Vector<FundInvestmentIntention>();
		
		if (rs.first()) {
			do {
				intentions.add(get(rs));
			} while (rs.next());
		}
		
		return intentions;
	}
	
	public static DataResultSet getDataByOrgAndCampaignId
	 (int orgId, int campaignId, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Entity.Cols.ID, orgId);
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Campaign.Cols.CAMPAIGN_ID, campaignId);
		
		return facade.createResultSet
		 (GET_INTENTIONS_BY_ORG_AND_CAMPAIGN_ID_QUERY, binder);
	}
	
	public static FundInvestmentIntention get(DataResultSet rs) throws DataException {
		
		if (rs.isEmpty())
			return null;
		
		return new FundInvestmentIntention(
			CCLAUtils.getResultSetIntegerValue(rs, Cols.ID),
			Fund.getCache().getCachedInstance(
				rs.getStringValueByName(SharedCols.FUND)
			),
			CCLAUtils.getResultSetIntegerValue(rs, SharedCols.ORG),
			CCLAUtils.getResultSetIntegerValue(rs, SharedCols.PERSON),
			CCLAUtils.getResultSetFloatValue(rs, SharedCols.CASH),
			CCLAUtils.getResultSetFloatValue(rs, SharedCols.UNITS),
			rs.getDateValueByName(SharedCols.DATE_ADDED),
			rs.getDateValueByName(SharedCols.LAST_UPDATED),
			rs.getStringValueByName(SharedCols.USER),
			rs.getStringValueByName(SharedCols.LAST_UPDATED_BY),
			CCLAUtils.getResultSetIntegerValue(rs, SharedCols.CAMPAIGN),
			CCLAUtils.getResultSetIntegerValue(rs, Cols.INVESTMENT_INTENTION_STATUS_ID),
			CCLAUtils.getResultSetIntegerValue(rs, SharedCols.ACCOUNT)
			
		);
	}
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.ID, this.getInvestmentIntentionId());
		CCLAUtils.addQueryParamToBinder(binder, SharedCols.FUND, this.getFund().getFundCode());
		CCLAUtils.addQueryIntParamToBinder(binder, SharedCols.ORG, this.getOrganisationId());
		CCLAUtils.addQueryIntParamToBinder(binder, SharedCols.PERSON, this.getPersonId());
		CCLAUtils.addQueryFloatParamToBinder(binder, SharedCols.CASH, this.getCash());
		CCLAUtils.addQueryFloatParamToBinder(binder, SharedCols.UNITS, this.getUnits());
		CCLAUtils.addQueryParamToBinder(binder, SharedCols.USER, this.getUserId());
		CCLAUtils.addQueryParamToBinder(binder, SharedCols.LAST_UPDATED_BY, this.getLastUpdatedBy());
		CCLAUtils.addQueryIntParamToBinder(binder, SharedCols.CAMPAIGN, this.getCampaignId());
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.INVESTMENT_INTENTION_STATUS_ID, this.getInvestmentIntentionStatusId());
		CCLAUtils.addQueryIntParamToBinder(binder, SharedCols.ACCOUNT, this.getAccountId());

	}
	
	public void persist(FWFacade facade, String username) throws DataException 
	{
		this.validate(facade);
		
		this.setLastUpdatedBy(username);
		DataBinder binder = new DataBinder();		
		this.addFieldsToBinder(binder);
		
		DataResultSet beforeData = 
			FundInvestmentIntention.getData(this.getInvestmentIntentionId(), facade);
		
		facade.execute(UPDATE_INTENTION_QUERY, binder);	
		
		DataResultSet afterData = 
			FundInvestmentIntention.getData(this.getInvestmentIntentionId(), facade);
		
		// Add audit record
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(this.getInvestmentIntentionId(), ElementType.SecondaryElementType.FundInvestmentIntention.toString());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.UPDATE.toString(), 
		 ElementType.SecondaryElementType.FundInvestmentIntention.toString(), 
		 beforeData, afterData, auditRelations);	
		
	}

	public void setAttributes(DataBinder binder) throws DataException 
	{
		Integer campaignId = BinderUtils.getBinderIntegerValue(binder, SharedCols.CAMPAIGN); 
		if (campaignId!=null)
			this.setCampaignId(campaignId);
		
		Fund fund = Fund.getCache().getCachedInstance(BinderUtils.getBinderStringValue(binder, SharedCols.FUND));
		this.setFund(fund);
		
		this.setCash(BinderUtils.getBinderFloatValue(binder, SharedCols.CASH));
		this.setUnits(BinderUtils.getBinderFloatValue(binder, SharedCols.UNITS));
		this.setDateAdded(BinderUtils.getBinderDateValue(binder, SharedCols.DATE_ADDED));
		this.setInvestmentIntentionId(BinderUtils.getBinderIntegerValue(binder, Cols.ID));
		this.setInvestmentIntentionStatusId(BinderUtils.getBinderIntegerValue(binder, Cols.INVESTMENT_INTENTION_STATUS_ID));
		this.setOrganisationId(BinderUtils.getBinderIntegerValue(binder, SharedCols.ORG));
		this.setPersonId(BinderUtils.getBinderIntegerValue(binder, SharedCols.PERSON));
		this.setLastUpdatedBy(BinderUtils.getBinderStringValue(binder, SharedCols.LAST_UPDATED_BY));
		this.setUserId(BinderUtils.getBinderStringValue(binder,SharedCols.USER));
		this.setLastUpdated(BinderUtils.getBinderDateValue(binder, SharedCols.LAST_UPDATED));
		this.setAccountId(BinderUtils.getBinderIntegerValue(binder, SharedCols.ACCOUNT));
	}

	public void validate(FWFacade facade) throws DataException {
		
		// Ensure that the investment intention is valid.
		
		if (this.getCampaignId() != null && this.getOrganisationId() != null) {
			// Fetch campaign instance first.
			Campaign campaign = Campaign.getCache().getCachedInstance
			 (this.getCampaignId());
			
			try {
			
				if (campaign == null)
					throw new ServiceException
					 ("No campaign found with ID: " + this.getCampaignId());		
				
				IEnrolmentHandler enrolmentHandler = 
				 campaign.getEnrolmentHandlerInstance
				 (com.ecs.ucm.ccla.Globals.Users.System, facade);
				
				enrolmentHandler.validateIntention(this);
				
			} catch (Exception e) {
				throw new DataException("Failed to validate investment intention: "
				 + e.getMessage(), e);
			}
		}
	}
	
	public CampaignActivity addActivity(int campaignEnrolmentId, Integer activityTypeId, 
	 String noteMsg, FWFacade facade, String userName) throws DataException {
		
		String activityDesc = "";
		
		InvestmentIntentionStatus status = 
		 InvestmentIntentionStatus.getCache().getCachedInstance
		 (this.getInvestmentIntentionStatusId());
		
		Object[] params = new Object[] {
			(this.getUnits()!= null ? 
			 CCLAUtils.PLAIN_NUMBER_FORMAT.format(this.getUnits()):"unspecified"),
			 (status!=null?status.getName():"N/A")
		};
		
		switch (activityTypeId) {
			case CampaignActivityType.ADD_INTENTION_ACTIVITY_ID:
				activityDesc = MessageFormat.format(CampaignMessages.INTENTION_ADDED, params);
				break;
			case CampaignActivityType.UPDATE_INTENTION_ACTIVITY_ID:
				activityDesc = MessageFormat.format(CampaignMessages.INTENTION_UPDATED, params);				
				break;
			case CampaignActivityType.REMOVE_INTENTION_ACTIVITY_ID:
				activityDesc = MessageFormat.format(CampaignMessages.INTENTION_DELETED, params);
				break;
			default:
				throw new DataException("ActivityTypeID not supported for FundInvestmentIntention");				
		}
		
		return this.addActivity(campaignEnrolmentId, 
				this.getPersonId(), activityTypeId, activityDesc, 
				noteMsg, facade, userName);
	}
	/**
	 * Adds a new Campaign Activity to the database, tied to the given 
	 * Campaign Enrolment instance. The noteMsg string is optional.
	 * 
	 * @param enrolment
	 * @param PersonId
	 * @param activityTypeId
	 * @param activityDesc
	 * @param username
	 */
	private CampaignActivity addActivity
	 (int campaignEnrolmentId, Integer personId, 
	 Integer activityTypeId, String activityDesc, String noteMsg, FWFacade facade, 
	 String username) throws DataException {
	
		Integer noteId = null;
		if (!StringUtils.stringIsBlank(noteMsg)) {
			Note note = Note.add(noteMsg, username, facade);
			noteId = note.getNoteId();
		}
		
		CampaignActivity activity = new CampaignActivity(
					0, campaignEnrolmentId, 
					personId, activityTypeId, activityDesc, null, 
					username, noteId);
		return CampaignActivity.add(activity, username, facade);
	}	
	

	public int getInvestmentIntentionId() {
		return investmentIntentionId;
	}

	public void setInvestmentIntentionId(int investmentIntentionId) {
		this.investmentIntentionId = investmentIntentionId;
	}

	public Fund getFund() {
		return fund;
	}

	public void setFund(Fund fund) {
		this.fund = fund;
	}

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}

	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

	public Float getCash() {
		return cash;
	}

	public void setCash(Float cash) {
		this.cash = cash;
	}

	public Float getUnits() {
		return units;
	}

	public void setUnits(Float units) {
		this.units = units;
	}

	public Date getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public void setCampaignId(Integer campaignId) {
		this.campaignId = campaignId;
	}

	public Integer getCampaignId() {
		return campaignId;
	}

	public Integer getInvestmentIntentionStatusId() {
		return investmentIntentionStatusId;
	}

	public void setInvestmentIntentionStatusId(Integer investmentIntentionStatusId) {
		this.investmentIntentionStatusId = investmentIntentionStatusId;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}
}
