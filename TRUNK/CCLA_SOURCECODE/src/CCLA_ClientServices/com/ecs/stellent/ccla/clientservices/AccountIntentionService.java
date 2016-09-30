package com.ecs.stellent.ccla.clientservices;

import java.text.DecimalFormat;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Fund;
import com.ecs.ucm.ccla.data.SharedCols;
import com.ecs.ucm.ccla.data.campaign.Campaign;
import com.ecs.ucm.ccla.data.campaign.CampaignActivityType;
import com.ecs.ucm.ccla.data.campaign.CampaignEnrolment;
import com.ecs.ucm.ccla.data.campaign.FundInvestmentIntention;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.BinderUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ServiceException;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.server.Service;

/** Service handlers for Account Intention CRUD methods.
 * 
 *  @deprecated remove any useful stuff to FundInvestmentIntention DAO instead, this
 *  class shouldn't exist.
 *  
 * @author Tom
 *
 */
public class AccountIntentionService extends Service {
	

	/** Adds a new account intention.
	 * 
	 * @throws ServiceException 
	 * @throws DataException
	 **/
	public void addAcountIntention() throws ServiceException, DataException {	
		
		String fundCode = BinderUtils.getBinderStringValue(m_binder, SharedCols.FUND);
		Integer personId = BinderUtils.getBinderIntegerValue(m_binder, SharedCols.PERSON);
		Integer organisationId = BinderUtils.getBinderIntegerValue(m_binder, SharedCols.ORG);
		Integer enrolmentId = BinderUtils.getBinderIntegerValue(m_binder, CampaignEnrolment.CAMPAIGN_ENROLMENT_ID);
		Integer campaignId = BinderUtils.getBinderIntegerValue(m_binder, Campaign.Cols.CAMPAIGN_ID);
		Integer accountId = BinderUtils.getBinderIntegerValue(m_binder, SharedCols.ACCOUNT);
		Float units = BinderUtils.getBinderFloatValue(m_binder, SharedCols.UNITS);
		Integer intentionStatusId = BinderUtils.getBinderIntegerValue(m_binder, FundInvestmentIntention.Cols.INVESTMENT_INTENTION_STATUS_ID);
		String noteMsg = BinderUtils.getBinderStringValue(m_binder, "NOTE");
		

		String userId = m_userData.m_name;
		BinderUtils.addParamToBinder(m_binder, SharedCols.USER, userId);
		Float cash = null;

		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		FundInvestmentIntention intention = addAccountIntention
		 (fundCode, organisationId, personId, cash, units, campaignId, 
		 intentionStatusId, facade, userId, accountId, noteMsg, enrolmentId);
		
		m_binder.putLocal(FundInvestmentIntention.Cols.INVESTMENT_INTENTION_STATUS_ID, 
		 Integer.toString(intention.getInvestmentIntentionId()));		
		
	}
	
	
	public static FundInvestmentIntention addAccountIntention
	 (String fundCode, Integer organisationId, Integer personId, 
	 Float cash, Float units, Integer campaignId, Integer intentionStatusId, FWFacade facade, 
	 String userId, Integer accountId, String noteMsg, Integer enrolmentId) 
	 throws ServiceException, DataException {

		if (StringUtils.stringIsBlank(fundCode))
			throw new ServiceException("Missing Fund Code");
		
		Fund fund = Fund.getCache().getCachedInstance(fundCode);	
		
		if (fund==null)
			throw new ServiceException("Cannot find fund with fund code :"+fundCode);

		if (personId==null) 
			throw new ServiceException("Missing Person Id");
		
		if (organisationId==null) 
			throw new ServiceException("Missing Organisation Id");
		
		if (enrolmentId==null) 
			throw new ServiceException("Missing Campaign Enrolment Id");

		if (campaignId==null)
			throw new ServiceException("Missing Campaign Id");

		
		if (intentionStatusId==null)
			throw new ServiceException("Missing Fund Investment Status Id");
		
		if (accountId==null)
			throw new ServiceException("Missing Account Id");
		
		
		FundInvestmentIntention intention = 
			FundInvestmentIntention.add(fund, organisationId, personId, cash, units, campaignId, intentionStatusId,  accountId , facade, userId);
		
		
		intention.addActivity(enrolmentId, CampaignActivityType.ADD_INTENTION_ACTIVITY_ID, noteMsg, facade, userId);
		
		return intention;
	}
	
	/** gets account intentions for a particular fund and entity
	 * 
	 * @throws ServiceException 
	 * @throws DataException
	 **/
	public void getFundAccountIntentionByEntity() throws ServiceException, DataException {
		Log.debug("IN getFundAccountIntentionByEntity");
		

		String fundCode = m_binder.getLocal(SharedCols.FUND);
		String ORGANISATION_ID = m_binder.getLocal(SharedCols.ORG);
		 if (fundCode == null || fundCode.length()==0 
				 || ORGANISATION_ID ==null || ORGANISATION_ID.length()==0)
			 throw new ServiceException("Missing FundCode or Entity Id");

		FWFacade fw = CCLAUtils.getFacade(this.m_workspace, true);
		DataResultSet existingIntentions = fw.createResultSet("qCore_GetFundInvestmentIntentionByEntity", m_binder);
		
		if (existingIntentions.isEmpty())
			Log.debug("no results");
		// need to get the index for the units field
		int NameFieldIndex = 0;
		
		for (int j=0;j<existingIntentions.getNumFields();j++) {
			if (existingIntentions.getFieldName(j).equalsIgnoreCase(SharedCols.UNITS))
				NameFieldIndex = j;
		}
		
		Log.debug("NameFieldIndex is " + NameFieldIndex);
		for (int i=0;i<existingIntentions.getNumRows();i++)
		{
			existingIntentions.setCurrentRow(i);
			String units = existingIntentions.getStringValueByName(SharedCols.UNITS);
			
			if (!StringUtils.stringIsBlank(units)) {
				DecimalFormat unitFormat = new DecimalFormat();
				unitFormat.setMinimumFractionDigits(0);
				Double unitD = Double.parseDouble(units);
				String unitFormatted = unitFormat.format(unitD);;
				Log.debug("formatted units is " + unitFormatted);
				existingIntentions.setCurrentValue(NameFieldIndex, unitFormatted);
			}
		}		
		m_binder.addResultSet("rsIntentions", existingIntentions);
		
		Log.debug("returned rsIntentions with rows:" + existingIntentions.getNumRows());		
	}
	
	/** gets account intentions for a particular fund and entity
	 * 
	 * @throws ServiceException 
	 * @throws DataException
	 **/
	public void getFundAccountIntention() throws ServiceException, DataException 
	{
		Log.debug("IN getFundAccountIntention");
		
		FWFacade fw = CCLAUtils.getFacade(this.m_workspace, true);
		String orgId = m_binder.getLocal(SharedCols.ORG);
		String investmentId = m_binder.getLocal(FundInvestmentIntention.Cols.ID);
		
		if (orgId ==null || orgId.length()==0 ||
				investmentId==null || investmentId.length()==0)
			 throw new ServiceException("Missing Investment Intention Id or Entity Id");
		
		DataResultSet rsIntentionDetail = fw.createResultSet("qCore_GetFundInvestmentIntention", m_binder);
		m_binder.addResultSet("rsIntentionDetail", rsIntentionDetail);
		Log.debug("returned rsIntentionDetail with rows:" + rsIntentionDetail.getNumRows());		
	}

	/** removes an account intention
	 * 
	 * @throws ServiceException 
	 * @throws DataException
	 **/
	public void deleteFundAccountIntention() throws ServiceException, DataException {
		Log.debug("IN deleteFundAccountIntention");
		Integer intentionId = BinderUtils.getBinderIntegerValue(m_binder, FundInvestmentIntention.Cols.ID);
		Integer enrolmentId = BinderUtils.getBinderIntegerValue(m_binder, CampaignEnrolment.CAMPAIGN_ENROLMENT_ID);
		String noteMsg = BinderUtils.getBinderStringValue(m_binder, "NOTE");

		if (intentionId==null)
			throw new ServiceException("Missing Fund Investment Intention Id");

		if (enrolmentId==null) 
			throw new ServiceException("Missing Campaign Enrolment Id");

		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);		
		FundInvestmentIntention intention = FundInvestmentIntention.get(intentionId, facade);
		
		if (intention==null) 
			throw new ServiceException("Cannot find Fund Investment Intention with id:"+intentionId);
		
		String userId = m_userData.m_name;

		facade.execute(FundInvestmentIntention.DELETE_INTENTION_QUERY, m_binder);
		intention.addActivity(enrolmentId, CampaignActivityType.REMOVE_INTENTION_ACTIVITY_ID, noteMsg, facade, userId);

	}

	/** updates account intentions
	 * 
	 * @throws ServiceException 
	 * @throws DataException
	 **/
	public void updateFundAccountIntention() throws ServiceException, DataException {
		Log.debug("IN updateFundAccountIntention");
		
		Integer intentionId = BinderUtils.getBinderIntegerValue
		 (m_binder, FundInvestmentIntention.Cols.ID);
		Integer enrolmentId = BinderUtils.getBinderIntegerValue
		 (m_binder, CampaignEnrolment.CAMPAIGN_ENROLMENT_ID);
		
		String noteMsg = BinderUtils.getBinderStringValue(m_binder, "NOTE");

		if (intentionId==null)
			throw new ServiceException("Missing Fund Investment Intention Id");

		if (enrolmentId==null) 
			throw new ServiceException("Missing Campaign Enrolment Id");
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);		
		FundInvestmentIntention intention = FundInvestmentIntention.get
		 (intentionId, facade);
		
		if (intention==null) 
			throw new ServiceException
			 ("Cannot find Fund Investment Intention with id:"+intentionId);
		
		String userId = m_userData.m_name;
		intention.setAttributes(m_binder);
		intention.persist(facade, userId);
		
		intention.addActivity
		 (enrolmentId, CampaignActivityType.UPDATE_INTENTION_ACTIVITY_ID, 
		 noteMsg, facade, userId);
	}
}
