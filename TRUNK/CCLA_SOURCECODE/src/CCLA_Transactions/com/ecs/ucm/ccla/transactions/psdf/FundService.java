package com.ecs.ucm.ccla.transactions.psdf;

import java.util.Date;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.transactions.FundPrice;
import com.ecs.ucm.ccla.transactions.FundPriceApplied;
import com.ecs.ucm.ccla.transactions.globals.TransactionGlobals;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.server.Service;

public class FundService extends Service {
	
	
	public void getLatestPrice() throws ServiceException
	{
		try {
			String fundCode = TransactionGlobals.PSDF_FUND_CODE;
			DataBinder binder = new DataBinder();
			CCLAUtils.addQueryParamToBinder(binder, "FUND_CODE", fundCode);
			FWFacade facade = CCLAUtils.getFacade(m_workspace, true);			
			DataResultSet rsPrice = FundPrice.getDailyData(fundCode, facade);
			if (!rsPrice.isEmpty())
				m_binder.addResultSet("rsPrice", rsPrice);				
			
		} catch (DataException e) {
			String msg = "Unable to get unit price: " + e.getMessage();
			Log.error(msg, e);		
			throw new ServiceException(msg, e);
		}
	}

	public void updatePrice() throws ServiceException, DataException
	{
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);		
		String username = m_userData.m_name;
		
		try {
			facade.beginTransaction();
			String fundCode = m_binder.getLocal("FUND_CODE");
			String bid = m_binder.getLocal("BID");
			String basic = m_binder.getLocal("BASIC");
			String offer = m_binder.getLocal("OFFER");

			Date runDate = CCLAUtils.getBinderDateValue(m_binder, "END_DATE");
			
			Log.debug("bid, basic offer are " + bid + ", " + basic + ", " + offer);

			FundPrice fp = FundPrice.add(fundCode, new Date(), bid, basic, offer, username,  facade);
			
			int fundPriceId =  fp.getFundPriceId();	
			int dealTypeId = CCLAUtils.getBinderIntegerValue(m_binder, "DEAL_TYPE_ID");
			// set the fund price applied for today to added value
			FundPriceApplied fpa = null;
			
			if (runDate!=null) {
				fpa = FundPriceApplied.getFundPriceAppliedByFundAndDate(TransactionGlobals.PSDF_FUND_CODE, runDate, facade);
				
				if (fpa!=null) {
					fpa.setDealTypeId(dealTypeId);
					fpa.setFundPriceId(fundPriceId);
					fpa.persist(facade, username);
				} else {
					fpa = FundPriceApplied.add(fundPriceId, dealTypeId, runDate, username, facade);					
				}	
			} else {
				if (FundPriceApplied.getToday(TransactionGlobals.PSDF_FUND_CODE, facade)!=null) {
					fpa = FundPriceApplied.getToday(TransactionGlobals.PSDF_FUND_CODE, facade);
					fpa.setDealTypeId(dealTypeId);
					fpa.setFundPriceId(fundPriceId);
					fpa.persist(facade, username);
				} else {
					fpa = FundPriceApplied.add(fundPriceId, dealTypeId, new Date(), username, facade);
				} 		
			}
			
			Log.debug("FundPriceApplied: id:"+fpa.getFundPriceAppliedId()+
					", fpId:"+fpa.getFundPriceId()+", date:"+fpa.getPriceDate());
			
			facade.commitTransaction();
		} catch (DataException e) {
			facade.rollbackTransaction();
			String msg = "Unable to get unit price: " + e.getMessage();
			Log.error(msg, e);		
			throw new ServiceException(msg, e);
		}
	}	
}
