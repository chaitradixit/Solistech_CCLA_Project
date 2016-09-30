package com.ecs.ucm.ccla.data.subscription;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.NumberUtils;
import com.ecs.ucm.ccla.NumberUtils.AmountPercentPair;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.Audit;
import com.ecs.ucm.ccla.data.EnhancedPersistable;
import com.ecs.ucm.ccla.data.Fund;
import com.ecs.ucm.ccla.data.Globals;
import com.ecs.ucm.ccla.data.SharedCols;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Models entries in the CONTRIBUTION_ASSET_ALLOCATION table.
 *  
 *  Links Contributions to Assets (either Fund or Account at the moment), with a given 
 *  amount and equivalent percentage.
 *  
 *  Government match rates/amounts can also be attached to each allocation.
 *  
 * @author Tom
 *
 */
public class ContributionAssetAllocation extends EnhancedPersistable {

	private int contributionId;
	private String fundCode;
	private Integer accountId;
	
	private BigDecimal amount;
	private BigDecimal percent;
	
	private Date startDate;
	private Date endDate;
	
	// Government match-related fields
	private BigDecimal govRecoveryAmountExpected;
	private BigDecimal govRecoveryAmountActual;
	
	private Integer govMatchRateId;
	
	public static class Cols {
		public static final String ID = "CONTRIB_ASSET_ALLOCATION_ID";
		public static final String CONTRIBUTION_AMOUNT = "CONTRIBUTION_AMOUNT";
		public static final String CONTRIBUTION_PERCENT = "CONTRIBUTION_PERCENT";
		
		public static final String GOV_RECOVERY_AMT_EXPECTED = "GOV_RECOVERY_AMT_EXPECTED";
		public static final String GOV_RECOVERY_AMT_ACTUAL = "GOV_RECOVERY_AMT_ACTUAL";
	}
	
	public static class Queries {
		public static final String ADD = "qCore_AddContributionAssetAllocation";
		public static final String UPDATE = "qCore_UpdateContributionAssetAllocation";
		public static final String REMOVE = "qCore_RemoveContributionAssetAllocation";
		public static final String GET = "qCore_GetContributionAssetAllocation";
		
		public static final String GET_ALL_BY_CONTRIBUTION_ID = 
		 "qCore_GetAllContributionAssetAllocationsByContributionId";
		public static final String GET_ALL_BY_SUBSCRIPTION_ID = 
		 "qCore_GetAllContributionAssetAllocationsBySubscriptionId";
		public static final String GET_BY_CONTRIBUTION_AND_ASSET_IDS = 
		 "qCore_GetContributionAssetAllocationByValues";
	}
	
	/** Number of decimal places that Income Allocation Percentages will be rounded/
	 *  stored as.
	 */
	public static final int CONTRIBUTION_PERCENT_PRECISION = 10;
	
	/** After crunching relative percentage values for a group asset allocations, their
	 *  total may not actually add up to 100%. Allow the total to be off by this amount
	 *  and tweak one of the percentages after to ensure 100% total.
	 */
	public static final double TOTAL_PERCENTAGE_TOLERANCE = 0.0001;
	
	/** After crunching relative amount values for a group asset allocations, their
	 *  total may not actually add up to the contribution amount. Allow the total to be 
	 *  off by this amount and tweak one of the amounts after to ensure 100% total.
	 */
	public static final double TOTAL_AMOUNT_TOLERANCE = 0.1;
	
	public static ContributionAssetAllocation add
	 (int contributionId, String fundCode, Integer accountId, 
	 BigDecimal amount, BigDecimal percent, 
	 Date startDate, Date endDate,
	 BigDecimal govRecoveryAmountExpected, BigDecimal govRecoveryAmountActual,
	 Integer govMatchRateId,
	 FWFacade facade, String userName) throws DataException {
		
		ContributionAssetAllocation alloc = new ContributionAssetAllocation
		 (null, null, null, userName, contributionId, 
		 fundCode, accountId, amount, percent, startDate, endDate,
		 govRecoveryAmountExpected, govRecoveryAmountActual, govMatchRateId);
		
		int newId = alloc.getNewKey(facade);
		alloc.setId(newId);
		
		alloc.validate(facade);
		
		DataBinder binder = new DataBinder();
		alloc.addFieldsToBinder(binder);
		
		facade.execute(Queries.ADD, binder);
		
		// Audit the Add action
		
		// Link to the new Allocation ID and Contribution ID
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(newId, alloc.getEntityName());
		auditRelations.put(contributionId, Contribution.class.getSimpleName());
		
		DataResultSet rsNewData = getData(newId, facade);
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, userName, 
		 Globals.AuditActions.ADD.toString(), alloc.getEntityName(),
		 null, rsNewData, auditRelations);
		
		return get(rsNewData);
	}
	
	public ContributionAssetAllocation(Integer id, Date dateAdded,
			Date lastUpdated, String lastUpdatedBy,
			int contributionId, String fundCode, Integer accountId,
			BigDecimal amount, BigDecimal percent,
			Date startDate, Date endDate,
			BigDecimal govRecoveryAmountExpected, BigDecimal govRecoveryAmountActual,
			Integer govMatchRateId) {
		
		super(id, dateAdded, lastUpdated, lastUpdatedBy);
		
		this.setContributionId(contributionId);
		
		this.fundCode = fundCode;
		this.accountId = accountId;
		
		this.amount = amount;
		this.percent = percent;
		
		this.startDate = startDate;
		this.endDate = endDate;
		
		this.govRecoveryAmountExpected = govRecoveryAmountExpected;
		this.govRecoveryAmountActual = govRecoveryAmountActual;
		
		this.govMatchRateId = govMatchRateId;
	}
	
	/** Basic constructor that should only be used when building 'test' instances
	 *  when validating sets of allocations.
	 *  
	 *  Following a successful validation against a set of contribution allocations, the
	 *  percentages, amounts and gov. match values will be used later to build or update
	 *  ContributionAssetAllocation instances, which are then persisted to the database.
	 *  
	 * @param contributionId
	 * @param fundCode
	 * @param amount
	 */
	public ContributionAssetAllocation
	 (int contributionId, String fundCode, Integer accountId, 
	 BigDecimal amount, BigDecimal percent,
	 BigDecimal govMatchExpected, BigDecimal govMatchActual, Integer govMatchRateId) {
		super(null, null, null, null);
		
		this.setContributionId(contributionId);
		this.accountId = accountId;
		this.fundCode = fundCode;
		this.amount = amount;
		this.percent = percent;
		
		this.govRecoveryAmountExpected = govMatchExpected;
		this.govRecoveryAmountActual = govMatchActual;
		
		this.govMatchRateId = govMatchRateId;
	}
	
	public static ContributionAssetAllocation get(int id, FWFacade facade) 
	 throws DataException {
		
		DataResultSet rs = getData(id, facade);
		
		if (rs.first())
			return get(rs);
		else
			return null;
	}
	
	/** Fetches a matching Contribution Fund Allocation for the given Contribution ID
	 *  and Fund/Account, if one exists.
	 *  
	 * @param contributionId
	 * @param fund
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static ContributionAssetAllocation getByValues
	 (int contributionId, Fund fund, Integer accountId, FWFacade facade) 
	 throws DataException {
		
		DataResultSet rs = getDataByValues(contributionId, fund, accountId, facade);
		
		if (rs.first())
			return get(rs);
		else
			return null;
	}
	
	public static DataResultSet getDataByValues(int contributionId,
	 Fund fund, Integer accountId, FWFacade facade) throws DataException {
		
		if (fund != null && accountId != null) {
			throw new DataException("Unable to fetch Contribution Asset Allocation " +
			 "by values - Fund and Account ID passed. Must be one or the other");
		}
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Contribution.Cols.ID, contributionId);
		
		CCLAUtils.addQueryParamToBinder
		 (binder, SharedCols.FUND, fund == null ? null : fund.getFundCode());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Account.Cols.ID, accountId);
		
		return facade.createResultSet
		 (Queries.GET_BY_CONTRIBUTION_AND_ASSET_IDS, binder);
	}

	/** Returns the set of fund allocations for the given Contribution ID.
	 * 
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getAllDataByContributionId
	 (int contributionId, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, Contribution.Cols.ID, contributionId);
		
		return facade.createResultSet
		 (Queries.GET_ALL_BY_CONTRIBUTION_ID, binder);
	}
	
	/** Returns the set of fund allocations for the given Contribution ID.
	 * 
	 * @return
	 * @throws DataException 
	 */
	public static Vector<ContributionAssetAllocation> getAllByContributiontId
	 (int contributionId, FWFacade facade) throws DataException {
		
		DataResultSet rs = getAllDataByContributionId(contributionId, facade);
		Vector<ContributionAssetAllocation> allocs = 
		 new Vector<ContributionAssetAllocation>();
		
		if (rs.first()) {
			do {
				allocs.add(get(rs));
			} while (rs.next());
		}
		
		return allocs;
	}
	
	/** Returns the set of fund allocations for all Contributions belonging to the 
	 *  given Subscription ID.
	 * 
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getAllDataBySubscriptionId
	 (int subscriptionId, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Subscription.Cols.ID, subscriptionId);
		
		return facade.createResultSet
		 (Queries.GET_ALL_BY_SUBSCRIPTION_ID, binder);
	}
	
	/** Returns the set of fund allocations for all Contributions belonging to the 
	 *  given Subscription ID.
	 * 
	 * @return
	 * @throws DataException 
	 */
	public static Vector<ContributionAssetAllocation> getAllBySubscriptiontId
	 (int subscriptionId, FWFacade facade) throws DataException {
		
		DataResultSet rs = getAllDataBySubscriptionId(subscriptionId, facade);
		Vector<ContributionAssetAllocation> allocs = 
		 new Vector<ContributionAssetAllocation>();
		
		if (rs.first()) {
			do {
				allocs.add(get(rs));
			} while (rs.next());
		}
		
		return allocs;
	}
	
	public static ContributionAssetAllocation get(DataResultSet rs) throws DataException {
		
		return new ContributionAssetAllocation(
			CCLAUtils.getResultSetIntegerValue(rs, Cols.ID),
			rs.getDateValueByName(SharedCols.DATE_ADDED),
			rs.getDateValueByName(SharedCols.LAST_UPDATED),
			rs.getStringValueByName(SharedCols.LAST_UPDATED_BY),
			
			CCLAUtils.getResultSetIntegerValue(rs, Contribution.Cols.ID),
			
			rs.getStringValueByName(SharedCols.FUND),
			CCLAUtils.getResultSetIntegerValue(rs, Account.Cols.ID),
			
			CCLAUtils.getResultSetBigDecimalValue(rs, Cols.CONTRIBUTION_AMOUNT),
			CCLAUtils.getResultSetBigDecimalValue(rs, Cols.CONTRIBUTION_PERCENT),
			
			rs.getDateValueByName(SharedCols.START_DATE),
			rs.getDateValueByName(SharedCols.END_DATE),
			
			CCLAUtils.getResultSetBigDecimalValue(rs, Cols.GOV_RECOVERY_AMT_EXPECTED),
			CCLAUtils.getResultSetBigDecimalValue(rs, Cols.GOV_RECOVERY_AMT_ACTUAL),
			
			CCLAUtils.getResultSetIntegerValue(rs, GovMatchRate.Cols.ID)
		);
	}
	
	public static DataResultSet getData(int id, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.ID, id);
		
		return facade.createResultSet(Queries.GET, binder);
	}

	public void addFieldsToBinder(DataBinder binder) throws DataException {
		super.addFieldsToBinder(binder);
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.ID, this.getId());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Contribution.Cols.ID, this.getContributionId());
		
		CCLAUtils.addQueryParamToBinder
		 (binder, SharedCols.FUND, this.getFundCode());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Account.Cols.ID, this.getAccountId());
		
		CCLAUtils.addQueryBigDecimalParamToBinder
		 (binder, Cols.CONTRIBUTION_AMOUNT, this.getAmount());
		CCLAUtils.addQueryBigDecimalParamToBinder
		 (binder, Cols.CONTRIBUTION_PERCENT, this.getPercent());
		
		CCLAUtils.addQueryDateParamToBinder
		 (binder, SharedCols.START_DATE, this.getStartDate());
		CCLAUtils.addQueryDateParamToBinder
		 (binder, SharedCols.END_DATE, this.getEndDate());
		
		CCLAUtils.addQueryBigDecimalParamToBinder
		 (binder, Cols.GOV_RECOVERY_AMT_EXPECTED, this.getGovRecoveryAmountExpected());
		CCLAUtils.addQueryBigDecimalParamToBinder
		 (binder, Cols.GOV_RECOVERY_AMT_ACTUAL, this.getGovRecoveryAmountActual());
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, GovMatchRate.Cols.ID, this.getGovMatchRateId());
	}

	public void persist(FWFacade facade, String username) throws DataException {
		super.persist(facade, username);
		
		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);
		
		DataResultSet rsBeforeData = getData(this.getId(), facade);
		facade.execute(Queries.UPDATE, binder);
		DataResultSet rsAfterData = getData(this.getId(), facade);
		
		// Audit the Update action
		
		// Link to the Allocation ID and Contribution ID
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(this.getId(), this.getEntityName());
		auditRelations.put(this.getContributionId(), Contribution.class.getSimpleName());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.UPDATE.toString(), this.getEntityName(),
		 rsBeforeData, rsAfterData, auditRelations);
	}
	
	public void remove(FWFacade facade, String userName) throws DataException {
		
		DataResultSet rsBeforeData = getData(this.getId(), facade);
		
		if (rsBeforeData.isEmpty()) {
			throw new DataException("Unable to remove " +
			 this.getEntityName() + ", not found in the database");
		}
		
		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);
		
		facade.execute(Queries.REMOVE, binder);
		
		// Audit the Remove action
		
		// Link to the Allocation ID and Contribution ID
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(this.getId(), this.getEntityName());
		auditRelations.put(this.getContributionId(), Contribution.class.getSimpleName());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, userName, 
		 Globals.AuditActions.DELETE.toString(), this.getEntityName(),
		 rsBeforeData, null, auditRelations);
	}

	public void setAttributes(DataBinder binder) throws DataException {
		super.setAttributes(binder);
		
		this.setContributionId(CCLAUtils.getBinderIntegerValue
		 (binder, Contribution.Cols.ID));
		
		this.setFundCode(binder.getLocal(SharedCols.FUND));
		this.setAccountId(CCLAUtils.getBinderIntegerValue
		 (binder, Account.Cols.ID));
		
		this.setAmount(CCLAUtils.getBinderBigDecimalValue
		 (binder, Cols.CONTRIBUTION_AMOUNT));
		this.setPercent(CCLAUtils.getBinderBigDecimalValue
		 (binder, Cols.CONTRIBUTION_PERCENT));
		
		this.setStartDate(CCLAUtils.getBinderDateValue
				 (binder, SharedCols.START_DATE));
		this.setEndDate(CCLAUtils.getBinderDateValue
				 (binder, SharedCols.END_DATE));
	}

	public void validate(FWFacade facade) throws DataException {
		super.validate(facade);
		
		/*
		// Ensure the amount has a scale of 4 decimal places.
		if (this.getAmount() != null) {
			this.setAmount(amount.setScale(4, RoundingMode.HALF_UP));
			Log.debug("Scaled Contribution Fund Allocation amount to 4 decimal " +
			 "places: " + amount.toPlainString());
		}
		*/
	}
	
	/** Validates the set of asset allocations against the given Contribution i.e. the
	 *  sum of contribution allocations add up to the contribution amount.
	 *  
	 *  Tidys up small differences in the amounts/percentages to ensure all asset
	 *  allocations match the contribution amount exactly.
	 *  
	 *  Running in 'strict' mode is only necessary after the form has been returned and
	 *  is supposed to ensure that the Contribution has been fully allocated across a
	 *  set of assets.
	 *  
	 * @param contribution
	 * @param assetAllocations 	a set of allocations, with the percentage AND amount values 
	 * 							set.
	 * @param strict 
	 * @throws DataException
	 */
	public static void validate(Contribution contribution, 
	 Vector<ContributionAssetAllocation> assetAllocations, boolean strict) 
	 throws DataException {
		
		Log.debug("Validating asset allocations for Contribution ID " 
		 + contribution.getId());
		
		if (contribution.getAmount() == null) {
			if (strict) {
				String msg = "Contribution amount not specified";
				
				Log.error(msg);
				throw new DataException(msg);
			} else {
				Log.debug("Skipping validation checks: contribution did not have an " +
				 "amount specified");
				return;
			}
		}
		
		if (assetAllocations.isEmpty()) {
			if (strict) {
				String msg = "No asset allocations specified";
				
				Log.error(msg);
				throw new DataException(msg);
			} else {
				Log.debug("Skipping validation checks: no allocations specified");
				return;
			}
		}
		
		BigDecimal totalAllocationPercent = BigDecimal.ZERO;
		
		// Add up all allocation percentages, ensure they sum up to 100.
		for (ContributionAssetAllocation alloc : assetAllocations)
			totalAllocationPercent = totalAllocationPercent.add(alloc.getPercent());
		
		if (totalAllocationPercent.compareTo(new BigDecimal(100)) != 0) {
			// Total didn't match 100. It may be ever so slightly off, check the 
			// difference and see if its within tolerance.
			BigDecimal diff = totalAllocationPercent.subtract(new BigDecimal(100));
			
			Log.debug("Total allocation percentage ( " 
			 + totalAllocationPercent.toPlainString() + ") did not match 100. " 
			 + "Difference:" + diff.toPlainString());
			 
			if (Math.abs(diff.doubleValue()) > TOTAL_PERCENTAGE_TOLERANCE) {
				String msg = "Sum of allocation percentages is not 100% " +
				 " (sum was: " + totalAllocationPercent.toPlainString() + ")";
				
				Log.error(msg);
				throw new DataException(msg);
			} else {
				// Difference was within tolerance. We'll adjust one of the percentage
				// values by the difference, to ensure they all add up to 100.
				Log.debug("Difference was within tolerance." +
				 " Adjusting percentage value to remove this difference");
				
				BigDecimal currentPercent = assetAllocations.get(0).getPercent();
				BigDecimal adjustedPercent = currentPercent.subtract(diff);
				
				Log.debug("Adjusting first allocation percentage " 
				+ currentPercent.toPlainString() + " to " + adjustedPercent.toPlainString());
				
				assetAllocations.get(0).setPercent(adjustedPercent);
			}
		}
		
		// Add up all allocation amounts, ensure they match the contribution amount.
		BigDecimal totalAllocationAmount = BigDecimal.ZERO;
		
		for (ContributionAssetAllocation alloc : assetAllocations)
			totalAllocationAmount = totalAllocationAmount.add(alloc.getAmount());
		
		BigDecimal targetAllocationAmount = contribution.getAmount();
		
		Log.debug("Added up " + assetAllocations.size() + " fund allocations. Comparing " +
		 "sum of allocations (" + totalAllocationAmount.toPlainString() + ") to " +
		 "total contribution amount (" + targetAllocationAmount.toPlainString() + ")");
		
		if (totalAllocationAmount.compareTo(targetAllocationAmount) != 0) {
			BigDecimal diff = totalAllocationAmount.subtract
			 (targetAllocationAmount);
			
			if (Math.abs(diff.doubleValue()) > TOTAL_AMOUNT_TOLERANCE) {
				// Difference exceeds tolerance.
				String msg = "Sum of allocations differs from " +
				 "contribution amount (difference: " + diff.toPlainString() + ")";
				
				Log.error(msg);
				throw new DataException(msg);
			} else {
				// Difference was within tolerance. We'll adjust one of the amount
				// values by the difference, to ensure they all add up to 100.
				Log.debug("Difference was within tolerance." +
				 " Adjusting amount value to remove this difference");
				
				BigDecimal currentAmount = assetAllocations.get(0).getAmount();
				BigDecimal adjustedAmount = currentAmount.subtract(diff);
				
				Log.debug("Adjusting first allocation amount " 
				+ currentAmount.toPlainString() + " to " + adjustedAmount.toPlainString());
				
				assetAllocations.get(0).setAmount(adjustedAmount);
			}
		}
		
		Log.debug("Sum of allocations matched subscription amount");
		
		// TODO: ensure fund allocations do not exceed percentile limits?
		
		Log.debug("Fund allocations validated successfully");
	}
	
	/** Removes all existing Contribution Fund Allocations linked to the given
	 *  Subscription and Fund.
	 *  
	 * @param subscriptionFundAlloc
	 * @param facade
	 * @param userName
	 * @throws DataException 
	 */
	public static void removeContributionFundAllocations
	 (int subscriptionId, Fund fund, FWFacade facade, String userName) 
	 throws DataException {
		
		Log.debug("Removing all Contribution Fund Allocations to Fund Code " 
		 + fund.getFundCode() + " linked to Subscription ID "
		 + subscriptionId);
		
		// Fetch all associated Contribution Fund Allocations for this Subscription
		Vector<ContributionAssetAllocation> contributionFundAllocs =
		 ContributionAssetAllocation.getAllBySubscriptiontId
		 (subscriptionId, facade);
		
		for (ContributionAssetAllocation alloc : contributionFundAllocs) {
			if (alloc.getFundCode().equals(fund.getFundCode())) {
				alloc.remove(facade, userName);
			}
		}
	}
	
	/** 
	 * @deprecated bulk-update of Contribution Asset allocations is no longer applicable.
	 * Each Contribution must have its asset allocations set individually. See 
	 * ContributionService.updateContributionAssetAllocations()
	 * 
	 * Updates all Contribution Fund Allocations for the given Subscription ID.
	 * 
	 * @param subscriptionId
	 * @param subscriptionAllocs 	total allocations per Fund for the subscription
	 * @param contributions			existing Contributions
	 * @param availableFunds		all funds that can be potentially invested in.
	 * @param facade
	 * @param userName
	 * @throws DataException 
	 */
	public static void updateContributionFundAllocations
	 (int subscriptionId, Vector<SubscriptionAssetAllocation> subscriptionAllocs,
	 Vector<Contribution> contributions,
	 Vector<Fund> availableFunds,
	 FWFacade facade, String userName) 
	 throws DataException {
		
		Log.debug("Updating all Contribution Fund Allocations linked to Subscription " +
		 "ID " + subscriptionId);
		
		// Fetch all associated Contribution Fund Allocations for this Subscription
		Vector<ContributionAssetAllocation> contributionFundAllocs =
		 ContributionAssetAllocation.getAllBySubscriptiontId
		 (subscriptionId, facade);
		
		Log.debug("Found " + contributionFundAllocs.size() + " existing " +
		 "Contribution Fund Allocations");
		
		//HashMap<Contribution, BigDecimal> contributionAllocationShares =
		// Contribution.getContributionShareMapping(contributions);
		
		// Step through each Fund available for allocation
		for (Fund availableFund : availableFunds) {
			SubscriptionAssetAllocation newSubscriptionAlloc = null;
			
			// Check if this Fund has been allocated to
			for (SubscriptionAssetAllocation subscriptionAlloc : subscriptionAllocs) {
				if (subscriptionAlloc.getFund().equals(availableFund)) {
					// Found an allocation to this Fund.
					newSubscriptionAlloc = subscriptionAlloc;
					break;
				}
			}
			
			if (newSubscriptionAlloc == null) {
				// No allocation to this Fund. Ensure all existing Contribution
				// allocations have been removed.
				
				removeContributionFundAllocations
				 (subscriptionId, availableFund, facade, userName);
				
			} else {
				// Allocation to this Fund. Split it across a set of Contribution
				// allocations.
				
				addOrUpdateContributionFundAllocations
				 (contributions, newSubscriptionAlloc, facade, userName);	
			}
		}
	}
	
	/** 
	 * Adds/updates/removes Contribution Fund Allocations in the database. 
	 * 
	 * The list of passed ContributionAssetAllocations must have been validated 
	 * previously, with respect to their parent Contribution instance. Allocation amounts
	 * and percentages (including Gov. Match data) is copied from the passed list and used
	 * to update or create new Allocations in the database.
	 * 
	 * @param contributionAllocationShares
	 * @param subscriptionAlloc
	 * @param facade
	 * @param userName
	 * @throws DataException 
	 */
	public static void addOrUpdateContributionFundAllocations(
	 Contribution contribution,
	 Vector<ContributionAssetAllocation> capturedAllocations,
	 Vector<Fund> availableFunds,
	 FWFacade facade, String userName) throws DataException {

		Log.debug("Adding/updating Contribution Fund Allocations for Contribution ID "
		 + contribution.getId());
		
		// Fetch existing Asset Allocations for this Contribution.
		Vector<ContributionAssetAllocation> existingAllocations = 
		 getAllByContributiontId(contribution.getId(), facade);
		
		Log.debug("Captured " + capturedAllocations.size() + " allocations from user, " +
		 existingAllocations.size() + " already exist in DB");
		
		// Loop through available Funds for allocation.
		for (Fund fund : availableFunds) {
			Log.debug("Checking allocation against Fund " + fund.getFundCode());
			
			// Find existing allocation, if applicable.
			ContributionAssetAllocation existingAllocation = null;
			
			for (ContributionAssetAllocation alloc : existingAllocations) {
				if (alloc.getFundCode().equals(fund.getFundCode())) {
					existingAllocation = alloc; // Found an existing Allocation in DB.
					break;
				}
			}
			
			// Fund new allocation, if applicable.
			ContributionAssetAllocation capturedAllocation = null;
			
			for (ContributionAssetAllocation alloc : capturedAllocations) {
				if (alloc.getFundCode().equals(fund.getFundCode())) {
					capturedAllocation = alloc; // Found data for a captured allocation.
					break;
				}
			}
			
			// OK now figure out if we need to add, remove, update (or none of these!)
			if (existingAllocation == null && capturedAllocation == null) {
				Log.debug("No allocation");
				continue;
			} else if (existingAllocation != null && capturedAllocation == null) {
				// Remove existing allocation.
				Log.debug("Removing existing allocation");
				existingAllocation.remove(facade, userName);
			} else if (existingAllocation == null) {
				// Add new allocation.
				Log.debug("Adding new allocation");
				ContributionAssetAllocation.add(
					contribution.getId(), 
					fund.getFundCode(), 
					null, 
					capturedAllocation.getAmount(), 
					capturedAllocation.getPercent(), 
					null, 
					null, 
					capturedAllocation.getGovRecoveryAmountExpected(), 
					capturedAllocation.getGovRecoveryAmountActual(), 
					capturedAllocation.getGovMatchRateId(), 
					facade, 
					userName
				);
				
			} else {
				// Update existing allocation.
				Log.debug("Updating existing allocation");
				existingAllocation.setAmount(capturedAllocation.getAmount());
				existingAllocation.setPercent(capturedAllocation.getPercent());
				
				existingAllocation.setGovMatchRateId(capturedAllocation.getGovMatchRateId());
				existingAllocation.setGovRecoveryAmountExpected
				 (capturedAllocation.getGovRecoveryAmountExpected());
				existingAllocation.setGovRecoveryAmountActual
				 (capturedAllocation.getGovRecoveryAmountActual());
				
				existingAllocation.persist(facade, userName);
			}
		}
		
	}
	
	/** 
	 * @deprecated bulk-update of Contribution Asset allocations is no longer applicable.
	 * Each Contribution must have its asset allocations set individually. See 
	 * ContributionService.updateContributionAssetAllocations()
	 * 
	 * Adds/updates Contribution Fund Allocations for the single Fund/Amount specified 
	 * in the SubscriptionAssetAllocation instance.
	 * 
	 * @param contributionAllocationShares
	 * @param subscriptionAlloc
	 * @param facade
	 * @param userName
	 * @throws DataException 
	 */
	private static void addOrUpdateContributionFundAllocations(
	 Vector<Contribution> contributions,
	 SubscriptionAssetAllocation subscriptionAlloc,
	 FWFacade facade, String userName) throws DataException {
		
		// Fail if the total allocation is null/zero
		if (subscriptionAlloc == null 
			|| subscriptionAlloc.getAmount().compareTo(BigDecimal.ZERO) == 0) {
			throw new DataException("Unable to add/update Contribution Fund " +
			 "Allocations: passed total allocation was null/zero");
		}

		// Total amount to be allocated amongst contributions
		BigDecimal totalAmount = subscriptionAlloc.getAmount();
		
		Log.debug("Adding/updating Contribution Fund Allocations for Fund " 
		 + subscriptionAlloc.getFund().getFundCode() + ", Total allocation amount=" 
		 + totalAmount.toPlainString());
		
		// The loop below won't update the DB yet, just populate this hashmap with the
		// required contribution allocations.
		HashMap<Contribution, AmountPercentPair> contribAssetAllocs = 
		 new HashMap<Contribution, AmountPercentPair>();
		
		// Store the cumulative total of allocation amounts as we calculate each one.
		// Use this later to compare against the desired total amount.
		BigDecimal totalContribAssetAllocationAmount = BigDecimal.ZERO;
		
		for (Contribution contribution : contributions) {
			
			if (contribution.getAmount() == null 
				|| contribution.getAmount().compareTo(BigDecimal.ZERO) == 0) {
				Log.debug("Can't allocate any assets to Contribution ID "
				+ contribution.getContributorId() + ", no contribution amount set.");
				continue;
			}
			
			BigDecimal contributionShare = contribution.getPercent().divide
			 (NumberUtils.ONE_HUNDRED);
			
			BigDecimal assetAllocAmount = contributionShare.multiply(totalAmount);

			Log.debug("Contribution ID " + contribution.getContributorId() + " had " +
			 "total contribution amount: " + contribution.getAmount().toPlainString() +
			 ", subscription share: " + contributionShare.toPlainString());

			BigDecimal assetAllocPercent = contributionShare.multiply
			 (NumberUtils.ONE_HUNDRED);
			
			Log.debug("Calculated (pre-rounding) asset allocation amount: " 
			 + assetAllocAmount.toPlainString() + ", percent: " 
			 + assetAllocPercent.toPlainString());
			
			// Apply scaling to calculated amount/percent.
			assetAllocAmount = assetAllocAmount.setScale(2, RoundingMode.HALF_UP);
			
			assetAllocPercent = assetAllocPercent.setScale
			 (ContributionAssetAllocation.CONTRIBUTION_PERCENT_PRECISION, 
			 RoundingMode.HALF_UP);

			Log.debug("Calculated (post-rounding) asset allocation amount: " 
			 + assetAllocAmount.toPlainString() + ", percent: " 
			 + assetAllocPercent.toPlainString());
			
			AmountPercentPair amountPercent = new AmountPercentPair
			 (assetAllocAmount, assetAllocPercent);
			
			contribAssetAllocs.put(contribution, amountPercent);
			
			totalContribAssetAllocationAmount =
			 totalContribAssetAllocationAmount.add(assetAllocAmount);
		}
		
		// OK - all amounts/percentages have been calculated for all contributions with
		// respect to this asset.
		
		// Make sure the rounded amounts still add up to the subscription-level 
		// allocation amount - if not, we'll tweak the first subscription amount to
		// ensure the sum of all contribution asset allocation amounts match the
		// desired subscription asset allocation amount.
		
		Log.debug("Finished calculating contribution asset allocations for fund: " 
		 + subscriptionAlloc.getFund().getFundCode());
		
		BigDecimal diff = subscriptionAlloc.getAmount().subtract
		 (totalContribAssetAllocationAmount);
		
		Log.debug("Sum of calculated asset allocations: " + 
		 totalContribAssetAllocationAmount.toPlainString() + 
		 ", difference from desired total: " + diff.toPlainString());
		
		if (diff.compareTo(BigDecimal.ZERO) != 0) {
			// Oh dear - discrepancy between calculated amount and desired (due to
			// cumulative rounding errors). Fix this now by tweaking the first
			// contribution asset allocation amount.
			for (Contribution contribution : contribAssetAllocs.keySet()) {
				AmountPercentPair amountPair = contribAssetAllocs.get(contribution);

				BigDecimal adjustedAmount = amountPair.getAmount().add(diff);
				
				Log.debug("Adjusted asset allocation amount for Contribution ID " 
				 + contribution.getId() + " from " + amountPair.getAmount() + " to "
				 + adjustedAmount.toPlainString() + " to account for discrepancy");
				
				// Replace the AmountPercentPair instance in the mapping.
				AmountPercentPair newAmountPair = new AmountPercentPair
				 (adjustedAmount, amountPair.getPercent());
				
				contribAssetAllocs.put(contribution, newAmountPair);
				
				break;
			}
		}
		
		Log.debug("Adding/updating " + contribAssetAllocs.size() + 
		 " asset allocations for fund: " + subscriptionAlloc.getFund().getFundCode());
		
		// Finally, apply the updates to the DB.
		for (Contribution contribution : contribAssetAllocs.keySet()) {
			AmountPercentPair amountPair = contribAssetAllocs.get(contribution);
			
			addUpdateOrRemoveContributionFundAllocation
			 (contribution, subscriptionAlloc.getFund(), null,
			 amountPair.getAmount(), amountPair.getPercent(), facade, userName);
		}
	}
	
	/** Used to add, update or remove an existing Contribution Fund Allocation for the
	 *  passed Contribution/Fund.
	 *  
	 *  If the passed amount is zero/null, any existing Fund Allocation will be removed.
	 *  
	 *  @deprecated since letting users define contribution asset allocations.
	 *  
	 * @param contribution
	 * @param contributionAllocationAmount
	 * @param facade
	 * @param userName
	 * @throws DataException 
	 */
	private static void addUpdateOrRemoveContributionFundAllocation(
	 Contribution contribution, Fund fund, Integer accountId, 
	 BigDecimal amount, BigDecimal percent,
	 FWFacade facade, String userName) throws DataException {
		
		// Fetch exist allocation, if one exists.
		ContributionAssetAllocation existingAlloc =
		 ContributionAssetAllocation.getByValues
		 (contribution.getId(), fund, accountId, facade);
		
		boolean removeAlloc = false;
		
		String fundCode = (fund == null) ? null : fund.getFundCode();
		
		if (amount == null || amount.compareTo(BigDecimal.ZERO) == 0)
			removeAlloc = true;
		
		// Describes the allocation attributes, used in logging messages
		String contribFundAllocStr = "Contribution ID " +
		 contribution.getId() + ", Fund " + fundCode + ", Account " + accountId +
		 ", Amount=" + amount.toPlainString() + ", Percent=" + percent.toPlainString();
		
		if (existingAlloc != null) {
			// Do something with the existing allocation.
			
			if (removeAlloc) {
				// Remove existing allocation.
				Log.debug("Removing Contribution Fund Allocation for " 
				 + contribFundAllocStr);
				
				existingAlloc.remove(facade, userName);	
			} else {
				// Update existing allocation, if the amount changed.
				if ((existingAlloc.getAmount() == null)
					||
					(existingAlloc.getAmount().compareTo(amount) != 0)) {
					// Apply changes to existing amount.
					Log.debug("Updating Contribution Fund Allocation for "
					 + contribFundAllocStr);
					
					existingAlloc.setAmount(amount);
					existingAlloc.persist(facade, userName);
				} else {
					Log.debug("Skipping update of Contribution Fund Allocation for "
					 + contribFundAllocStr + " (no change in amount)");
				}
			}
		} else { 
			if (removeAlloc) {
				// No existing allocation, and none required now, so do nothing.
				Log.debug("No need for Contribution Fund Allocation for " +
				 "Contribution ID " + contribution.getId() + ", Fund=" 
				 + fund.getFundCode());
				
			} else {
				Log.debug("Adding Contribution Fund Allocation for Contribution ID " +
				 contribution.getId() + ", Fund=" + fund.getFundCode() + ", Amount=" +
				 amount.toPlainString());
				
				// Add new allocation
				add(contribution.getId(), fund.getFundCode(), accountId, 
				 amount, percent, null, null, null, null, null, facade, userName);
			}
		}
	}

	@Override
	public String getSequenceName() {
		return "SEQ_CONTRIB_FUND_ALLOCATION";
	}

	public String getFundCode() {
		return fundCode;
	}

	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public void setContributionId(int contributionId) {
		this.contributionId = contributionId;
	}

	public int getContributionId() {
		return contributionId;
	}

	public static void updateContributionFundAllocations(
	 Subscription subscription, Vector<Fund> availableFunds,
	 FWFacade facade, String userName) throws DataException {
		
		// Fetch existing Subscription-level allocations (if any)
		Vector<SubscriptionAssetAllocation> allocs =
		 SubscriptionAssetAllocation.getAllBySubscriptionId
		 (subscription.getId(), facade);
		
		// Now validate all the allocations we just fetched before continuing.
		// Do a non-strict validation here.
		SubscriptionAssetAllocation.validate(subscription, allocs, false);
		
		// If we got this far, the subscription/allocation amounts have been 
		// validated successfully. Now apply changes to the database.
		Vector<Contribution> contributions = Contribution.getAllBySubscriptionId
		 (subscription.getId(), facade);
		
		updateContributionFundAllocations
		 (subscription.getId(), allocs, contributions, availableFunds,
		 facade, userName);
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public BigDecimal getPercent() {
		return percent;
	}

	public void setPercent(BigDecimal percent) {
		this.percent = percent;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getGovMatchRateId() {
		return govMatchRateId;
	}

	public void setGovMatchRateId(Integer govMatchRateId) {
		this.govMatchRateId = govMatchRateId;
	}

	public BigDecimal getGovRecoveryAmountExpected() {
		return govRecoveryAmountExpected;
	}

	public void setGovRecoveryAmountExpected(BigDecimal govRecoveryAmountExpected) {
		this.govRecoveryAmountExpected = govRecoveryAmountExpected;
	}

	public BigDecimal getGovRecoveryAmountActual() {
		return govRecoveryAmountActual;
	}

	public void setGovRecoveryAmountActual(BigDecimal govRecoveryAmountActual) {
		this.govRecoveryAmountActual = govRecoveryAmountActual;
	}
}
