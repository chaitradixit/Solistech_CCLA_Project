package com.ecs.ucm.ccla.data.subscription;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.math.BigDecimal;
import java.util.Vector;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.Fund;
import com.ecs.ucm.ccla.data.SharedCols;
import com.ecs.ucm.ccla.data.Account.Cols;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.embedded.FWFacade;

/** 
 *  @deprecated use separate ContributionAssetAllocations instead.
 * 
 *  Wrapper class for Subscriptions and their aggregated Contribution Asset Allocations. 
 *  Doesn't actually exist as an individual database entity, but very useful when
 *  building forms etc that require the Asset Allocations expressed in terms of a 
 *  total Subscription amount, as opposed to individual contributions.
 *  
 * @author Tom
 *
 */
public class SubscriptionAssetAllocation {
	
	private int subscriptionId;
	
	private Fund fund;
	private Integer accountId;
	
	private BigDecimal amount; /** Should always be cast by SQL query with 2 decimal
								   places */
	private BigDecimal percent;
	
	public static class Cols {
		public static final String ASSET_AMOUNT = "ASSET_AMOUNT";
		public static final String ASSET_PERCENT = "ASSET_PERCENT";
	}
	
	public static class Queries {
		public static final String GET_ALL_BY_SUBSCRIPTION_ID = 
		 "qCore_GetTotalAssetAllocationsBySubscriptionId";
	}

	public SubscriptionAssetAllocation(int subscriptionId, Fund fund, Integer accountId,
			BigDecimal amount, BigDecimal percent) {
		super();
		this.subscriptionId = subscriptionId;
		this.fund = fund;
		this.amount = amount;
		this.setPercent(percent);
	}
	
	public static Vector<SubscriptionAssetAllocation> getAllBySubscriptionId
	 (int subscriptionId, FWFacade facade) throws DataException {
		
		DataResultSet rs = getAllDataBySubscriptionId(subscriptionId, facade);
		Vector<SubscriptionAssetAllocation> v = new Vector<SubscriptionAssetAllocation>();
		
		if (rs.first()) {
			do {
				v.add(get(rs));
			} while (rs.next());
		}
		
		return v;
	}
	
	public static DataResultSet getAllDataBySubscriptionId
	 (int subscriptionId, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Subscription.Cols.ID, subscriptionId);
		
		return facade.createResultSet
		 (Queries.GET_ALL_BY_SUBSCRIPTION_ID, binder);
	}

	public static SubscriptionAssetAllocation get(DataResultSet rs) 
	 throws DataException {
		
		if (rs.isEmpty())
			return null;
		
		return new SubscriptionAssetAllocation(
			CCLAUtils.getResultSetIntegerValue(rs, Subscription.Cols.ID),
			Fund.getCache().getCachedInstance(rs.getStringValueByName(SharedCols.FUND)),
			CCLAUtils.getResultSetIntegerValue(rs, Account.Cols.ID),
			CCLAUtils.getResultSetBigDecimalValue(rs, Cols.ASSET_AMOUNT),
			CCLAUtils.getResultSetBigDecimalValue(rs, Cols.ASSET_PERCENT)
		);
	}
	
	public int getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(int subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public Fund getFund() {
		return fund;
	}

	public void setFund(Fund fund) {
		this.fund = fund;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	/** Validates the passed set of subscription-level fund allocations against the 
	 *  passed Subscription.
	 *  
	 *  Makes the assumption that the Subscription Amount has already been set to the
	 *  total of the associated Contribution amounts.
	 *  
	 *  If the Subscription doesn't have an amount specified, and we are running in non-
	 *  strict mode, the method returns immediately.
	 *  
	 *  If there are no allocations specified, and we are running in non-strict mode,
	 *  the method returns immediately.
	 *  
	 *  The basic check ensures that all allocations add up to the total investment
	 *  amount.
	 * 
	 * @param subscription
	 * @param testAllocs list of fund allocations with non-null amounts
	 * @param strict if false, the check will pass if there is an investment amount set
	 * 				but no allocations. If true, both the investment amount AND 
	 * 				allocations must be set and matching. Strict validation should be
	 * 				applied after the form/money is provided by the client.
	 * @throws DataException if validation fails
	 */
	public static void validate(Subscription subscription, 
	 Vector<SubscriptionAssetAllocation> testAllocs, boolean strict) 
	 throws DataException {
		
		Log.debug("Validating fund allocations for Subscription ID " 
		 + subscription.getId());
		
		if (subscription.getSubscriptionAmount() == null) {
			if (strict) {
				String msg = "Subscription amount not specified";
				
				Log.error(msg);
				throw new DataException(msg);
			} else {
				Log.debug("Skipping validation checks: susbcription did not have an " +
				 "amount specified");
				return;
			}
		}
		
		if (testAllocs.isEmpty()) {
			if (strict) {
				String msg = "No asset allocations specified";
				
				Log.error(msg);
				throw new DataException(msg);
			} else {
				Log.debug("Skipping validation checks: no allocations specified");
				return;
			}
		}
		
		BigDecimal totalAllocationAmount = getTotalSubscriptionFundAllocation
		 (testAllocs);
		
		Log.debug("Added up " + testAllocs.size() + " fund allocations. Comparing " +
		 "sum of allocations (" + totalAllocationAmount.toPlainString() + ") to " +
		 "total subscription amount (" + subscription.getSubscriptionAmount()
		 .toPlainString() + ")");
		
		int comparisonResult = totalAllocationAmount.compareTo
		 (subscription.getSubscriptionAmount());
		
		BigDecimal difference = subscription.getSubscriptionAmount().subtract(
		 totalAllocationAmount).abs();
		
		if (comparisonResult < 0) {
			String msg = "Sum of allocations is less than total " +
			 "subscription amount (difference: " + difference.toPlainString() + ")";
			
			Log.error(msg);
			throw new DataException(msg);
			
		} else if (comparisonResult > 0) {
			String msg = "Sum of allocations is greater than total " +
			 "subscription amount (difference: " + difference.toPlainString() + ")";
			
			Log.error(msg);
			throw new DataException(msg);
		}
		
		Log.debug("Sum of allocations matched subscription amount");
		
		// TODO: ensure fund allocations do not exceed percentile limits?
		
		Log.debug("Fund allocations validated successfully");
	}
	
	/** Adds up all Contribution Fund Allocations for a given Subscription ID and 
	 *  returns the total.
	 *  
	 *  Will always return zero as opposed to null, if there are no allocations.
	 * 
	 * @param subscriptionId
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static BigDecimal getTotalSubscriptionFundAllocation(
			Integer subscriptionId, FWFacade facade) throws DataException {

		Vector<SubscriptionAssetAllocation> allocs = getAllBySubscriptionId
		 (subscriptionId, facade);

		return getTotalSubscriptionFundAllocation(allocs);
	}
	
	public static BigDecimal getTotalSubscriptionFundAllocation
	 (Vector<SubscriptionAssetAllocation> allocs) {
		
		BigDecimal totalAllocationAmount = BigDecimal.ZERO;
		
		// Add up all Fund allocations
		for (SubscriptionAssetAllocation alloc : allocs) {
			if (alloc.getAmount() != null)
				totalAllocationAmount = totalAllocationAmount.add(alloc.getAmount());
		}
		
		return totalAllocationAmount;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setPercent(BigDecimal percent) {
		this.percent = percent;
	}

	public BigDecimal getPercent() {
		return percent;
	}
}
