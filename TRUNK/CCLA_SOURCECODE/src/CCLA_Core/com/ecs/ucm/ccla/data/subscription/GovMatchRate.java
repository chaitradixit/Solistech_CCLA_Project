package com.ecs.ucm.ccla.data.subscription;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

/** Wrapper for REF_GOV_MATCH_RATE table.
 * 
 * @author Tom
 *
 */
public class GovMatchRate {
	
	public static class Cols {
		public static final String ID = "GOV_MATCH_RATE_ID";
		public static final String PERCENTAGE = "GOV_MATCH_PERCENTAGE";
	}
	
	public static class Queries {
		public static final String GET_ALL = "qCore_GetAllGovMatchRates";
		public static final String GET_ALL_BY_SUBSCRIPTION_TYPE =
		 "qCore_GetAllGovMatchRatesByContributionType";
	}
	
	private int id;
	private BigDecimal percentage;
	
	public GovMatchRate(int id, BigDecimal percentage) {
		this.id = id;
		this.percentage = percentage;
	}
	
	private static final BigDecimal ONE_HUNDRED = new BigDecimal(100);
	
	public BigDecimal getGovMatchAmount(BigDecimal amount) {
		BigDecimal govMatchAmount = null;
		
		if (this.getPercentage().compareTo(ONE_HUNDRED) == 0)
			govMatchAmount = new BigDecimal(amount.doubleValue());
		else {
			govMatchAmount = amount.divide(ONE_HUNDRED)
			 .multiply(this.getPercentage());
		}
		
		govMatchAmount = govMatchAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
		return govMatchAmount;
	}
	
	public static void main (String[] args) {
		GovMatchRate rate = new GovMatchRate(1, new BigDecimal(50));
		System.out.println(rate.getGovMatchAmount(new BigDecimal(108.33)));
	}

	public static List<GovMatchRate> getAll(FWFacade facade) throws DataException {
		List<GovMatchRate> govMatchRates = new ArrayList<GovMatchRate>();
		
		DataResultSet rs = getAllData(facade);
		
		if (rs.first()) {
			do {
				govMatchRates.add(get(rs));
			} while (rs.next());
		}
		
		return govMatchRates;
	}
	
	public static GovMatchRate getById(int id, FWFacade facade) throws DataException {
		List<GovMatchRate> rates = getAll(facade);
		
		for (GovMatchRate rate : rates) {
			if (rate.getId() == id) {
				return rate;
			}
		}
		
		return null;
	}
	
	public static GovMatchRate get(DataResultSet rs) throws DataException {
		return new GovMatchRate(
			CCLAUtils.getResultSetIntegerValue(rs, Cols.ID),
			CCLAUtils.getResultSetBigDecimalValue(rs, Cols.PERCENTAGE)
		);
	}
	
	public static DataResultSet getAllData(FWFacade facade) throws DataException {
		return facade.createResultSet(Queries.GET_ALL, new DataBinder());
	}
	
	public static DataResultSet getAllDataByContributionType
	 (int contributionTypeId, FWFacade facade) throws DataException {
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, ContributionType.Cols.ID, contributionTypeId);
		
		return facade.createResultSet
		 (Queries.GET_ALL_BY_SUBSCRIPTION_TYPE, binder);
	}

	public BigDecimal getPercentage() {
		return percentage;
	}

	public int getId() {
		return id;
	}
}
