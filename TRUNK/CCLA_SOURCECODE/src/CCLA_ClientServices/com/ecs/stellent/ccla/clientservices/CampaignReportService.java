package com.ecs.stellent.ccla.clientservices;

import java.text.DecimalFormat;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals;
import com.ecs.ucm.ccla.data.SharedCols;
import com.ecs.ucm.ccla.data.campaign.Campaign;

import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.server.Service;

public class CampaignReportService extends Service {
	
	/** Fetches total number of clients, grouped by Client Status, for the given
	 *  Campaign ID in the binder.
	 * 
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void getCampaignSummary() throws DataException, ServiceException {
	
		FWFacade fw = CCLAUtils.getFacade(m_workspace, true);
		String strCampaignID = m_binder.getLocal("CAMPAIGN_ID");
		
		if (StringUtils.stringIsBlank(strCampaignID))
			throw new ServiceException("MISSING CAMPAIGN ID");
		
		int campaignId = Integer.parseInt(strCampaignID);
		
		DataResultSet rsCampaign = Campaign.getData(campaignId, fw);
		
		if (rsCampaign.isEmpty())
			throw new ServiceException("No campaign found with ID: " + campaignId);
		
		m_binder.addResultSet("rsCampaign", rsCampaign);
		
		Campaign campaign = Campaign.get(rsCampaign);
		
		// Fetch totals by Subject Status
		DataResultSet rsEnrolmentTotalsBySubjectStatus = 
		 getEnrolmentTotalsBySubjectStatus(campaignId, fw);
		
		m_binder.addResultSet
		 ("rsEnrolmentTotalsBySubjectStatus", rsEnrolmentTotalsBySubjectStatus);
		
		// If a FUND_CODE is present in the URL/binder, fetch the account capital
		// and intentions for the fund.
		String fundCode = m_binder.getLocal("FUND_CODE");
		
		if (!StringUtils.stringIsBlank(fundCode)) {
			// Fetch account capital for the Fund
			DataResultSet rsAccountCapitalByFund =
			 getAccountCapitalByFund(fundCode, fw);
			
			m_binder.addResultSet
			 ("rsAccountCapitalByFund", rsAccountCapitalByFund);
			
			// Fetch pending account intentions for the Fund. This will always exclude
			// any intentions for accounts fetched by the previous query.
			DataResultSet rsPendingAccountIntentionsByFund =
			 getPendingAccountIntentionsByFund(campaignId, fundCode, fw);
			
			m_binder.addResultSet
			 ("rsPendingAccountIntentionsByFund", rsPendingAccountIntentionsByFund);
		}
	}
	
	/** Fetches total enrolled subjects for a given campaign, grouped by subject status
	 * 
	 * @param campaignId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getEnrolmentTotalsBySubjectStatus
	 (int campaignId, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder(binder, SharedCols.CAMPAIGN, campaignId);
		
		return facade.createResultSet
		 ("qClientServices_GetEnrolmentTotalsBySubjectStatus", binder);
	}
	
	/** Fetches total no. of accounts, total cash amount and total unit amount for all
	 *  CCLA Accounts with the given Fund Code, which have had their balance figures 
	 *  refreshed at least once in the past.
	 *  
	 * @param fundCode
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getAccountCapitalByFund
	 (String fundCode, FWFacade facade) throws DataException {
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryParamToBinder(binder, SharedCols.FUND, fundCode);
		
		return facade.createResultSet
		 ("qClientServices_GetAccountCapitalByFund", binder);
	}

	/** Fetches all 'pending' account intentions belonging to the given campaign and
	 *  fund, grouped by intention status/confidence level.
	 *  
	 *  This query excludes any intentions who's linked accounts now have an active
	 *  balance.
	 *  
	 * @param campaignId
	 * @param fundCode
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getPendingAccountIntentionsByFund
	 (int campaignId, String fundCode, FWFacade facade) throws DataException {
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder(binder, SharedCols.CAMPAIGN, campaignId);
		CCLAUtils.addQueryParamToBinder(binder, SharedCols.FUND, fundCode);
		
		return facade.createResultSet
		 ("qClientServices_GetPendingAccountIntentionsByFund", binder);
	}
	
	/** Cam's method? Not sure if this is needed now.
	 * 
	 * @param CampaignID
	 * @param fw
	 * @param binder
	 * @param Filtered
	 * @param rsName
	 * @throws DataException
	 */
	public void getAccountIntentions(int CampaignID, FWFacade fw, DataBinder binder, 
	 boolean Filtered, String rsName) throws DataException {
		DataResultSet rsIntentionTotals = fw.createResultSet
		 ("qClientServices_GetProcessIntentionTotals", binder);
		
		DecimalFormat unitFormat = new DecimalFormat();
		unitFormat.setMinimumFractionDigits(0);
		
		if (!rsIntentionTotals.isEmpty())
		{
			// need to get the index for the TOTAL field
			int NameFieldIndex = 0;
			// total units
			Double TotalUnits = null;
			String totalUnitsFormatted = "";
			for (int j=0;j<rsIntentionTotals.getNumFields();j++)
			{
				if (rsIntentionTotals.getFieldName(j).equalsIgnoreCase("TOTAL"))
					NameFieldIndex = j;
			}		
			for (int i=0;i<rsIntentionTotals.getNumRows();i++)
			{
				rsIntentionTotals.setCurrentRow(i);
				String total = rsIntentionTotals.getStringValueByName("TOTAL");
				
				if (StringUtils.stringIsBlank(total))
					total = "0";
					
				Double unitD = Double.parseDouble(total);
				if (TotalUnits!=null)			
					TotalUnits = TotalUnits + unitD.doubleValue();
				else
					TotalUnits = unitD.doubleValue();
				String unitFormatted = unitFormat.format(unitD);
				Log.debug("formatted units: " + unitFormatted);
				rsIntentionTotals.setCurrentValue(NameFieldIndex, unitFormatted);
				
//				if (Filtered)
//				{
//					String processStatus = rsIntentionTotals.getStringValueByName("PROCESS_STATUS");
//					if (processStatus.equalsIgnoreCase("Undecided") || processStatus.equalsIgnoreCase("Not interested")
//							| processStatus.equalsIgnoreCase("Excluded"))
//					{
//						Log.debug("***deleting row for status:" + processStatus);
//						rsIntentionTotals.deleteCurrentRow();
//						TotalUnits = TotalUnits - unitD.doubleValue();
//						i = i-1;
//					} 
//				}
				totalUnitsFormatted = unitFormat.format(TotalUnits);
			}
			binder.putLocal(rsName + "_TOTAL", totalUnitsFormatted);
			binder.addResultSet(rsName, rsIntentionTotals);
		}
	}	
}
