package com.ecs.ucm.ccla.data.subscription;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.NumberUtils;
import com.ecs.ucm.ccla.data.Audit;
import com.ecs.ucm.ccla.data.EnhancedPersistable;
import com.ecs.ucm.ccla.data.Globals;
import com.ecs.ucm.ccla.data.SharedCols;
import com.ecs.ucm.ccla.data.Globals.AuditActions;
import com.ecs.ucm.ccla.data.product.Product;
import com.ecs.ucm.ccla.data.subscription.Contribution.Cols;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.embedded.FWFacade;

/** 
 *  @deprecated since the removal of TTLA allocations from the Community First scheme.
 *  Should be preserved for legacy purposes, but no new code should be accessing this DAO.
 * 
 *  Models entries in the CONTRIBUTION_TTLA_ALLOCATION table.
 * 
 *  Mapping between Contributions and Top-Tier Local Authority Organisations, with a 
 *  given percentage rate.
 *  
 *  Expected and actual Government Match amounts are also included here.
 *  
 *  It is expected that the sum of percentages for any set of allocations belonging to
 *  the same Contribution will always add up to 100, otherwise the allocation is invalid.
 *  
 * @author Tom
 *
 */
public class ContributionTTLAAllocation extends EnhancedPersistable {

	private int contributionId;
	private Integer ttlaOrgId;
	private Integer productId;
	
	private BigDecimal allocationPercent;
	private BigDecimal allocationAmount;
	
	// Government match-related fields
	private Integer govMatchLimitId;
	private Integer govMatchRateId;
	
	private BigDecimal govRecoveryAmountExpected;
	private BigDecimal govRecoveryAmountActual;

	private Date startDate;
	private Date endDate;
	
	/** Number of decimal places that Income Allocation Percentages will be rounded/
	 *  stored as.
	 */
	public static final int INCOME_ALLOCATION_PERCENT_PRECISION = 10;
	
	public static class Cols {
		public static final String ID = "CONTRIB_TTLA_ALLOCATION_ID";
		public static final String TTLA_ORG_ID = "TTLA_ORG_ID";
		
		public static final String INCOME_ALLOCATION_PERCENT = "INCOME_ALLOCATION_PERCENT";
		public static final String INCOME_ALLOCATION_AMOUNT = "INCOME_ALLOCATION_AMOUNT";
		
		public static final String GOV_RECOVERY_AMT_EXPECTED = "GOV_RECOVERY_AMT_EXPECTED";
		public static final String GOV_RECOVERY_AMT_ACTUAL = "GOV_RECOVERY_AMT_ACTUAL";
	}
	
	public static class Queries {
		public static final String ADD = "qCore_AddContributionTTLAAllocation";
		public static final String UPDATE = "qCore_UpdateContributionTTLAAllocation";
		public static final String GET = "qCore_GetContributionTTLAAllocation";
		public static final String REMOVE = "qCore_RemoveContributionTTLAAllocation";
		
		public static final String GET_ALL_BY_CONTRIBUTION_ID = 
		 "qCore_GetAllContributionTTLAAllocationsByContributionId";
		//public static final String GET_ALL_BY_SUBSCRIPTION_ID = 
		// "qCore_GetAllContributionTTLAAllocationsByContributionId";
	}
	
	public static ContributionTTLAAllocation add(
	 int contributionId, Integer ttlaOrgId, Integer productId,
	 BigDecimal percent, BigDecimal amount, 
	 Integer govMatchLimitId, Integer govMatchRateId,
	 BigDecimal govRecoveryAmountExpected, BigDecimal govRecoveryAmountActual,
	 Date startDate, Date endDate,
	 FWFacade facade, String userName)
	 throws DataException {
		
		ContributionTTLAAllocation alloc = new ContributionTTLAAllocation
		 (null, null, null, userName, contributionId, ttlaOrgId, productId,
		 percent, amount,
		 govMatchLimitId, govMatchRateId, 
		 govRecoveryAmountExpected, govRecoveryAmountActual,
		 startDate, endDate);
		
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
		
	public ContributionTTLAAllocation(Integer id, Date dateAdded, Date lastUpdated,
	 String lastUpdatedBy, 
	 int contributionId, Integer ttlaOrgId, Integer productId,
	 BigDecimal percent, BigDecimal amount,
	 Integer govMatchLimitId, Integer govMatchRateId,
	 BigDecimal govRecoveryAmountExpected, BigDecimal govRecoveryAmountActual,
	 Date startDate, Date endDate) {
		super(id, dateAdded, lastUpdated, lastUpdatedBy);
		
		this.contributionId = contributionId;
		this.ttlaOrgId = ttlaOrgId;
		this.productId = productId;
		
		this.allocationPercent = percent;
		this.allocationAmount = amount;
		
		this.govMatchLimitId = govMatchLimitId;
		this.govMatchRateId = govMatchRateId;
		this.govRecoveryAmountExpected = govRecoveryAmountExpected;
		this.govRecoveryAmountActual = govRecoveryAmountActual;
		
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	
	public ContributionTTLAAllocation(int contributionId, 
	 Integer ttlaOrgId, Integer productId,
	 BigDecimal percent, BigDecimal amount, 
	 Integer govMatchLimitId, Integer govMatchRateId, 
	 BigDecimal govRecoveryAmountExpected, BigDecimal govRecoveryAmountActual,
	 Date startDate, Date endDate) {
		
		super(null, null, null, null);
		
		this.contributionId = contributionId;
		this.ttlaOrgId = ttlaOrgId;
		this.productId = productId;
		this.allocationPercent = percent;
		this.allocationAmount = amount;
		
		this.govMatchLimitId = govMatchLimitId;
		this.govMatchRateId = govMatchRateId;
		this.govRecoveryAmountExpected = govRecoveryAmountExpected;
		this.govRecoveryAmountActual = govRecoveryAmountActual;
		
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public static ContributionTTLAAllocation get(int id, FWFacade facade) 
	 throws DataException {
		
		DataResultSet rs = getData(id, facade);
		
		if (rs.first())
			return get(rs);
		else
			return null;
	}
	
	public static ContributionTTLAAllocation get(DataResultSet rs) throws DataException {
		
		return new ContributionTTLAAllocation(
			CCLAUtils.getResultSetIntegerValue(rs, Cols.ID),
			rs.getDateValueByName(SharedCols.DATE_ADDED),
			rs.getDateValueByName(SharedCols.LAST_UPDATED),
			rs.getStringValueByName(SharedCols.LAST_UPDATED_BY),
			CCLAUtils.getResultSetIntegerValue(rs, Contribution.Cols.ID),
			CCLAUtils.getResultSetIntegerValue(rs, Cols.TTLA_ORG_ID),
			CCLAUtils.getResultSetIntegerValue(rs, Product.Cols.ID),
			CCLAUtils.getResultSetBigDecimalValue(rs, Cols.INCOME_ALLOCATION_PERCENT),
			CCLAUtils.getResultSetBigDecimalValue(rs, Cols.INCOME_ALLOCATION_AMOUNT),
			CCLAUtils.getResultSetIntegerValue(rs, TTLAGovMatchLimit.Cols.ID),
			CCLAUtils.getResultSetIntegerValue(rs, GovMatchRate.Cols.ID),
			CCLAUtils.getResultSetBigDecimalValue(rs, Cols.GOV_RECOVERY_AMT_EXPECTED),
			CCLAUtils.getResultSetBigDecimalValue(rs, Cols.GOV_RECOVERY_AMT_ACTUAL),
			rs.getDateValueByName(SharedCols.START_DATE),
			rs.getDateValueByName(SharedCols.END_DATE)
		);
	}
	
	public static DataResultSet getData(int id, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.ID, id);
		
		return facade.createResultSet(Queries.GET, binder);
	}

	@Override
	public String getSequenceName() {
		// TODO Auto-generated method stub
		return "SEQ_CONTRIB_TTLA_ALLOCATION";
	}

	public int getContributionId() {
		return contributionId;
	}

	public void setContributionId(int contributionId) {
		this.contributionId = contributionId;
	}

	public Integer getTTLAOrgId() {
		return ttlaOrgId;
	}

	public void setTTLAOrgId(Integer ttlaOrgId) {
		this.ttlaOrgId = ttlaOrgId;
	}

	public BigDecimal getAllocationPercent() {
		return allocationPercent;
	}

	public void setAllocationPercent(BigDecimal incomeAllocationPercent) {
		this.allocationPercent = incomeAllocationPercent;
	}

	@Override
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		super.addFieldsToBinder(binder);
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.ID, this.getId());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Contribution.Cols.ID, this.getContributionId());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.TTLA_ORG_ID, this.getTTLAOrgId());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Product.Cols.ID, this.getProductId());
		
		CCLAUtils.addQueryBigDecimalParamToBinder
		 (binder, Cols.INCOME_ALLOCATION_PERCENT, this.getAllocationPercent());
		CCLAUtils.addQueryBigDecimalParamToBinder
		 (binder, Cols.INCOME_ALLOCATION_AMOUNT, this.getAllocationAmount());
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, TTLAGovMatchLimit.Cols.ID, this.getGovMatchLimitId());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, GovMatchRate.Cols.ID, this.getGovMatchRateId());
		
		CCLAUtils.addQueryBigDecimalParamToBinder
		 (binder, Cols.GOV_RECOVERY_AMT_EXPECTED, 
		 this.getGovRecoveryAmountExpected());
		CCLAUtils.addQueryBigDecimalParamToBinder
		 (binder, Cols.GOV_RECOVERY_AMT_ACTUAL, 
		 this.getGovRecoveryAmountActual());
		
		CCLAUtils.addQueryDateParamToBinder
		 (binder, SharedCols.START_DATE, this.getStartDate());
		CCLAUtils.addQueryDateParamToBinder
		 (binder, SharedCols.END_DATE, this.getEndDate());
	}

	@Override
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
	
	public void remove(FWFacade facade, String username) throws DataException {
		
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
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.DELETE.toString(), this.getEntityName(),
		 rsBeforeData, null, auditRelations);
	}

	@Override
	public void setAttributes(DataBinder binder) throws DataException {
		super.setAttributes(binder);
		
		this.setContributionId
		 (CCLAUtils.getBinderIntegerValue(binder, Contribution.Cols.ID));
		this.setTTLAOrgId
		 (CCLAUtils.getBinderIntegerValue(binder, Cols.TTLA_ORG_ID));
		this.setProductId(CCLAUtils.getBinderIntegerValue(binder, Product.Cols.ID));
		
		this.setAllocationPercent
		 (CCLAUtils.getBinderBigDecimalValue(binder, Cols.INCOME_ALLOCATION_PERCENT));
		this.setAllocationAmount
		 (CCLAUtils.getBinderBigDecimalValue(binder, Cols.INCOME_ALLOCATION_AMOUNT));
		
		this.setGovMatchLimitId(
		 CCLAUtils.getBinderIntegerValue(binder, TTLAGovMatchLimit.Cols.ID));
		this.setGovMatchRateId(
		 CCLAUtils.getBinderIntegerValue(binder, GovMatchRate.Cols.ID));
		
		this.setGovRecoveryAmountExpected
		 (CCLAUtils.getBinderBigDecimalValue(binder, Cols.GOV_RECOVERY_AMT_EXPECTED));
		this.setGovRecoveryAmountActual
		 (CCLAUtils.getBinderBigDecimalValue(binder, Cols.GOV_RECOVERY_AMT_ACTUAL));
		
		this.setStartDate(CCLAUtils.getBinderDateValue
				 (binder, SharedCols.START_DATE));
		this.setEndDate(CCLAUtils.getBinderDateValue
				 (binder, SharedCols.END_DATE));
	}

	@Override
	/** Checks the Allocation Percentage does not exceed 100%.
	 * 
	 */
	public void validate(FWFacade facade) throws DataException {
		super.validate(facade);
		
		if (this.getAllocationPercent() != null
			&& this.getAllocationPercent().compareTo(new BigDecimal(100L)) > 0)
			throw new DataException("Allocation Percentage cannot exceed 100%");
		
		if (this.getAllocationPercent() != null) {
			// Apply rounding to percentage figure, if one is supplied
			this.setAllocationPercent(this.getAllocationPercent()
			 .setScale(INCOME_ALLOCATION_PERCENT_PRECISION, RoundingMode.HALF_UP));
		}
	}

	/** Fetches all TTLA Allocations for a given Contribution ID.
	 * 
	 * @param donationId
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static Vector<ContributionTTLAAllocation> getAllByContributionId(
	 Integer donationId, FWFacade facade) throws DataException {

		DataResultSet rs = getAllDataByContributionId(donationId, facade);
		
		Vector<ContributionTTLAAllocation> allocs = 
		 new Vector<ContributionTTLAAllocation>();
		
		if (rs.first()) {
			do {
				allocs.add(get(rs));
			} while (rs.next());
		}
		
		return allocs;
	}

	/** Fetches all TTLA Allocations for a given Contribution ID.
	 * 
	 * @param donationId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getAllDataByContributionId(Integer donationId,
	 FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, Contribution.Cols.ID, donationId);
		
		return facade.createResultSet
		 (Queries.GET_ALL_BY_CONTRIBUTION_ID, binder);
	}

	/** Validates the set of TTLA allocations against the given Contribution.
	 *  
	 *  The basic check must ensure that all percentage allocations with non-null 
	 *  amounts add up to 100, and all cash allocations add up to the Contribution 
	 *  Amount i.e. 100% of the contribution amount is allocated to TTLAs.
	 *  
	 *  TODO: verify cash amount total, only percentage total is checked currently!
	 *  
	 *  When running in strict mode, validation will fail if there aren't any 
	 *  allocations. Government Match Rate ID and Expected Recovery Amounts must also
	 *  be set against each TTLA Allocation.
	 *  
	 *  In non-strict mode, the TTLA allocations must satisfy one of the following
	 *  criteria: 
	 *  - all null amounts
	 *  - all adding up to 100%.
	 *  
	 * @param contribution
	 * @param testAllocs
	 * @param strict
	 * @throws DataException 
	 */
	public static void validate(Contribution contribution,
	 Vector<ContributionTTLAAllocation> testAllocs, boolean strict) 
	 throws DataException {
		
		Log.debug("Validating TTLA allocations for Contribution ID "
		 + contribution.getId());
		
		if (testAllocs.isEmpty()) {
			// No TTLA allocations.
			
			if (strict) {
				String msg = "No TTLA allocations specified. " +
				 "100% of the contribution amount (" 
				 + contribution.getAmount().toPlainString() + ") must be specified";
				
				Log.error(msg);
				throw new DataException(msg);
			} else {
				Log.debug("Skipping validation checks: no TTLA allocations specified");
				return;
			}
		}
		
		BigDecimal totalAllocationPercent = BigDecimal.ZERO;
		BigDecimal targetAllocationPercent = NumberUtils.ONE_HUNDRED;
		
		BigDecimal totalAllocationAmount = BigDecimal.ZERO;
		BigDecimal targetAllocationAmount = contribution.getAmount();
		
		int nonEmptyAllocations = 0;
		
		for (ContributionTTLAAllocation testAlloc : testAllocs) {
			if (testAlloc.getAllocationPercent() != null
				&& testAlloc.getAllocationPercent().compareTo(BigDecimal.ZERO) != 0) {
				nonEmptyAllocations++;
				totalAllocationPercent = 
				 totalAllocationPercent.add(testAlloc.getAllocationPercent());
			}
			
			if (testAlloc.getAllocationAmount() != null) {
				totalAllocationAmount = 
					totalAllocationAmount.add(testAlloc.getAllocationAmount());
			}
		}
		
		if (nonEmptyAllocations == 0) {
			if (strict) {
				String msg = "No TTLA allocations assigned";
				
				Log.error(msg);
				throw new DataException(msg);
			} else {
				Log.debug("Skipping validation checks: no non-empty TTLA allocations " +
				 "specified");
				return;
			}
		}
		
		// Ensure that all TTLA allocations have a non-null/non-zero value set, when
		// running in strict mode.
		if (strict) { 
			if (nonEmptyAllocations < testAllocs.size()) {
				String msg = "One or more of the TTLA Allocations has an empty/zero " +
				 "allocation amount. Ensure all selected TTLA Allocations " +
				 "have an allocation amount set";
				
				Log.error(msg);
				throw new DataException(msg);
			}
		}
		
		// Ensure total TTLA allocation amount matches the contribution amount
		if (strict) {
			if (totalAllocationAmount.compareTo(targetAllocationAmount) != 0) {
				String msg = "TTLA allocations for Contribution ID " 
				+ contribution.getId() + " do not add up to the Contribution amount " 
				+ "of " + targetAllocationAmount.toPlainString();
				
				Log.error(msg);
				throw new DataException(msg);
			}
		}
		
		Log.debug("Applying rounding to total percentage. Original value: " 
		 + totalAllocationPercent.toPlainString() + ", rounded value: " +
		 totalAllocationPercent.setScale(2, RoundingMode.HALF_UP));
		
		totalAllocationPercent = totalAllocationPercent.setScale
		 (2, RoundingMode.HALF_UP);
		
		Log.debug("Added up " + nonEmptyAllocations + " non-empty TTLA allocations. " +
		 "Comparing sum of allocations (" + totalAllocationPercent.toPlainString() + 
		 ") to target (" + targetAllocationPercent.toPlainString() + ")");
		
		int comparisonResult = totalAllocationPercent.compareTo(targetAllocationPercent);
		
		BigDecimal difference = targetAllocationPercent.subtract(
		 totalAllocationPercent).abs();
		
		if (comparisonResult < 0) {
			String msg = "Sum of allocations is less than 100% " +
			 "(difference: " + difference.toPlainString() + ")";
			
			Log.error(msg);
			throw new DataException(msg);
			
		} else if (comparisonResult > 0) {
			String msg = "Sum of allocations is greater than 100% " +
			 "(difference: " + difference.toPlainString() + ")";
			
			Log.error(msg);
			throw new DataException(msg);
		}
		
		Log.debug("Sum of allocations added up to 100%");
		
		// Now check each TTLA Allocation for required fields (when running in strict
		// mode)
		if (strict) {
			for (ContributionTTLAAllocation testAlloc : testAllocs) {
				if (testAlloc.getGovMatchRateId() == null) {
					// Government Match Rate is missing!
					String msg = "Government Match Rate is missing from at least one " +
					 "TTLA Allocation";
					
					Log.error(msg);
					throw new DataException(msg);
				}
				
				if (testAlloc.getGovRecoveryAmountExpected() == null) {
					// Expected Government Recovery Amount is missing!
					String msg = "Expected Government Recovery Amount is missing from "+
					 "at least one TTLA Allocation";
					
					Log.error(msg);
					throw new DataException(msg);
				}
			}
		}
		
		// TODO: ensure TTLA allocations do not exceed percentile limits (if any?)
		
		Log.debug("TTLA allocations validated successfully");
	}
	
	public static void main(String[] args) {
		BigDecimal total = new BigDecimal("98000");
		BigDecimal percentage = new BigDecimal("31.1183673");
		
		BigDecimal r = total.divide(NumberUtils.ONE_HUNDRED).multiply(percentage);
		System.out.println(r);
		
		final DecimalFormat CURRENCY_FORMAT = 
			new DecimalFormat("#,###,###,###.00");
	
		System.out.println(CURRENCY_FORMAT.format(r));
	}

	public void setAllocationAmount(BigDecimal amount) {
		this.allocationAmount = amount;
	}

	public BigDecimal getAllocationAmount() {
		return allocationAmount;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setGovMatchLimitId(Integer govMatchLimitId) {
		this.govMatchLimitId = govMatchLimitId;
	}

	public Integer getGovMatchLimitId() {
		return govMatchLimitId;
	}

	public void setGovRecoveryAmountExpected(BigDecimal govRecoveryAmountExpected) {
		this.govRecoveryAmountExpected = govRecoveryAmountExpected;
	}

	public BigDecimal getGovRecoveryAmountExpected() {
		return govRecoveryAmountExpected;
	}

	public void setGovRecoveryAmountActual(BigDecimal govRecoveryAmountActual) {
		this.govRecoveryAmountActual = govRecoveryAmountActual;
	}

	public BigDecimal getGovRecoveryAmountActual() {
		return govRecoveryAmountActual;
	}

	public void setGovMatchRateId(Integer govMatchRateId) {
		this.govMatchRateId = govMatchRateId;
	}

	public Integer getGovMatchRateId() {
		return govMatchRateId;
	}

	/** Returns true if any of the passed parameters differ from the instance 
	 *  attributes */
	public boolean hasChanged(
			BigDecimal ttlaAllocPercent,
			BigDecimal ttlaAllocAmount, 
			Integer productId,
			Integer govMatchRateId, 
			Integer govMatchLimitId,
			BigDecimal govRecoveryAmountExpected,
			BigDecimal govRecoveryAmountActual) {
		
		return (
			!CCLAUtils.bigDecimalsMatch(this.getAllocationPercent(), ttlaAllocPercent)
			||
			!CCLAUtils.bigDecimalsMatch(this.getAllocationAmount(), ttlaAllocAmount)
			||
			!CCLAUtils.integersMatch(this.getProductId(), productId)
			||
			!CCLAUtils.integersMatch(this.getGovMatchRateId(), govMatchRateId)
			||
			!CCLAUtils.integersMatch(this.getGovMatchLimitId(), govMatchLimitId)
			||
			!CCLAUtils.bigDecimalsMatch
			 (this.getGovRecoveryAmountExpected(), govRecoveryAmountExpected)
			||
			!CCLAUtils.bigDecimalsMatch
			 (this.getGovRecoveryAmountActual(), govRecoveryAmountActual)
		);
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
}
