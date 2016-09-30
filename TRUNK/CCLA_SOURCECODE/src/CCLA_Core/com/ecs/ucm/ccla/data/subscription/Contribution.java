package com.ecs.ucm.ccla.data.subscription;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.NumberUtils;
import com.ecs.ucm.ccla.data.Audit;
import com.ecs.ucm.ccla.data.EnhancedPersistable;
import com.ecs.ucm.ccla.data.Globals;
import com.ecs.ucm.ccla.data.RetailPriceIndex;
import com.ecs.ucm.ccla.data.SharedCols;
import com.ecs.ucm.ccla.data.Globals.AuditActions;
import com.ecs.ucm.ccla.data.campaign.Campaign;
import com.ecs.ucm.ccla.data.form.Form;
import com.ecs.ucm.ccla.data.product.Product;
import com.ecs.ucm.ccla.data.subscription.ContributionAssetAllocation.Cols;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Models entries in the CONTRIBUTION_INTENTION table.
 *  
 *  A Subscription is made up of a series of Contributions, with each Contribution 
 *  having an associated Donor (Element ID relating to an Organisation/Person record)
 * 
 * @author Tom
 *
 */
public class Contribution extends EnhancedPersistable {
	
	/** Parent Subscription ID */
	private int subscriptionId;
	
	/** ELEMENT_ID value for the contributor. Will map to either a Person or 
	 *  Organisation ID 
	 **/
	private Integer contributorId;
	/** ELEMENT_ID value for the benefactor. Will map to either a Person or 
	 *  Organisation ID 
	 */
	private Integer benefactorId;
	
	private Integer contributionTypeId;
	
	/** Contribution-specific Payment Reference. */
	private String paymentRef;
	
	private BigDecimal amount;
	private BigDecimal percent;
	
	private Integer retailPriceIndexId;

	private Integer matchedContributionId; 

	private Integer productId;
	private Integer campaignId;
	private Integer formId;
	
	private Date dateLatestCashProcessed;
	private Date dateCompleted;
	
	private Date startDate;
	private Date endDate;
	
	public static class Cols {
		public static final String ID = "CONTRIBUTION_ID";
		public static final String CONTRIBUTOR_ID = "CONTRIBUTOR_ID";
		public static final String BENEFACTOR_ID = "BENEFACTOR_ID";
		public static final String PAYMENT_REF = "CONTRIBUTION_PAYMENT_REF";
		public static final String CONTRIBUTION_AMOUNT = "CONTRIBUTION_AMOUNT";
		public static final String CONTRIBUTION_PERCENT = "CONTRIBUTION_PERCENT";
		public static final String MATCHED_CONTRIBUTION_ID = "MATCHED_CONTRIBUTION_ID";
		
		public static final String DATE_LATEST_CASH_PROCESSED = "DATE_LATEST_CASH_PROCESSED";
		public static final String DATE_COMPLETED = "DATE_COMPLETED";
	}
	
	public static class Queries {
		public static final String ADD = "qCore_AddContribution";
		public static final String UPDATE = "qCore_UpdateContribution";
		public static final String GET = "qCore_GetContribution";
		public static final String REMOVE = "qCore_RemoveContribution";
		
		public static final String GET_ALL_BY_SUBSCRIPTION_ID = 
		 "qCore_GetAllContributionsBySubscriptionId";
		
		/** Includes extra aggregation columns */
		public static final String GET_ALL_BY_SUBSCRIPTION_ID_EXTENDED = 
		 "qCore_GetAllContributionsBySubscriptionIdExtended";
	}
	
	
	/** Number of decimal places that Income Allocation Percentages will be rounded/
	 *  stored as.
	 */
	public static final int CONTRIBUTION_PERCENT_PRECISION = 10;
	
	/** Person/Org Account Code prefixes, used when generating anonymous Donors.
	 * 
	 * @author Tom
	 *
	 */
	public static class DonorAccountCodePrefixes {
		public static final String PERSON = "DONP";
		public static final String ORG = "DONO";
	}
	
	public Contribution(Integer id, Date dateAdded, Date lastUpdated,
			String lastUpdatedBy, int subscriptionId, Integer donorId,
			Integer benefactorId, Integer contributionTypeId, String paymentRef, 
			BigDecimal amount, BigDecimal percent,
			Integer retailPriceIndexId,
			Integer matchedContributionId, 
			Integer productId, Integer campaignId, Integer formId, 
			Date dateLatestCashProcessed, Date dateCompleted,
			Date startDate, Date endDate) {
		super(id, dateAdded, lastUpdated, lastUpdatedBy);
		
		this.subscriptionId = subscriptionId;
		
		this.contributorId = donorId;
		this.benefactorId = benefactorId;
		
		this.contributionTypeId = contributionTypeId;
		this.paymentRef = paymentRef;
		this.amount = amount;
		this.percent = percent;
		this.retailPriceIndexId = retailPriceIndexId;
		
		this.matchedContributionId = matchedContributionId;
		
		this.productId = productId;
		this.campaignId = campaignId;
		this.formId = formId;
		
		this.dateLatestCashProcessed = dateLatestCashProcessed;
		this.dateCompleted = dateCompleted;
		
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public static Contribution add(int subscriptionId, Integer donorId,
			Integer benefactorId, Integer contributionTypeId, String paymentRef, 
			BigDecimal amount, BigDecimal percent,
			Integer retailPriceIndexId,
			Integer matchedContributionId, 
			Integer productId, Integer campaignId, Integer formId, 
			Date dateLatestCashProcessed, Date dateCompleted,
			Date startDate, Date endDate,
			FWFacade facade, String userName) throws DataException {
		
		Contribution contribution = new Contribution
		 (null, null, null, userName, subscriptionId, 
		 donorId, benefactorId, 
		 contributionTypeId, paymentRef, amount, percent, retailPriceIndexId,  
		 matchedContributionId, productId, campaignId, formId, 
		 dateLatestCashProcessed, dateCompleted,
		 startDate, endDate);
	
		int newId = contribution.getNewKey(facade);
		contribution.setId(newId);
		
		DataBinder binder = new DataBinder();
		contribution.addFieldsToBinder(binder);
		
		facade.execute(Queries.ADD, binder);
		
		// Audit the Add action
		
		// Link to the new Contribution ID, Donor ID and Investment ID
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(contribution.getId(), contribution.getEntityName());
		auditRelations.put(contribution.getContributorId(), "Donor");
		auditRelations.put(contribution.getSubscriptionId(), Subscription.class.getSimpleName());
		
		DataResultSet rsNewData = getData(newId, facade);
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, userName, 
		 Globals.AuditActions.ADD.toString(), contribution.getEntityName(),
		 null, rsNewData, auditRelations);
		
		return get(rsNewData);
	}
	
	public static Contribution add(DataBinder binder, FWFacade facade, String userName) 
	 throws DataException {
		
		Contribution contribution = new Contribution(userName);
		contribution.setAttributes(binder);
		
		return add(
			contribution.getSubscriptionId(),
			contribution.getContributorId(),
			contribution.getBenefactorId(),
			contribution.getContributionTypeId(),
			contribution.getPaymentRef(),
			contribution.getAmount(),
			contribution.getPercent(),
			contribution.getRetailPriceIndexId(),
			contribution.getMatchedContributionId(),
			contribution.getProductId(),
			contribution.getCampaignId(),
			contribution.getFormId(),
			contribution.getDateLatestCashProcessed(),
			contribution.getDateCompleted(),
			contribution.getStartDate(),
			contribution.getEndDate(),
			facade,
			userName
		);
	}
	
	public Contribution(String userName) {
		super(null, null, null, userName);
	}
	
	/** Constructor for test Contribution instances, used for validation.
	 * 
	 * @param donorId
	 * @param amount
	 */
	public Contribution(int contributorId, BigDecimal amount) {
		super(null, null, null, null);
		
		this.contributorId = contributorId;
		this.amount = amount;
	}
	
	public static Contribution get(int id, FWFacade facade) 
	 throws DataException {
		
		DataResultSet rs = getData(id, facade);
		
		if (rs.first())
			return get(rs);
		else
			return null;
	}
	
	public static Contribution get(DataResultSet rs) throws DataException {
		
		return new Contribution(
			CCLAUtils.getResultSetIntegerValue(rs, Cols.ID),
			rs.getDateValueByName(SharedCols.DATE_ADDED),
			rs.getDateValueByName(SharedCols.LAST_UPDATED),
			rs.getStringValueByName(SharedCols.LAST_UPDATED_BY),
			
			CCLAUtils.getResultSetIntegerValue(rs, Subscription.Cols.ID),
			CCLAUtils.getResultSetIntegerValue(rs, Cols.CONTRIBUTOR_ID),
			CCLAUtils.getResultSetIntegerValue(rs, Cols.BENEFACTOR_ID),
			CCLAUtils.getResultSetIntegerValue(rs, ContributionType.Cols.ID),
			rs.getStringValueByName(Cols.PAYMENT_REF),
			CCLAUtils.getResultSetBigDecimalValue(rs, Cols.CONTRIBUTION_AMOUNT),
			CCLAUtils.getResultSetBigDecimalValue(rs, Cols.CONTRIBUTION_PERCENT),
			CCLAUtils.getResultSetIntegerValue(rs, RetailPriceIndex.Cols.ID),
			
			CCLAUtils.getResultSetIntegerValue(rs, Cols.MATCHED_CONTRIBUTION_ID),
			
			CCLAUtils.getResultSetIntegerValue(rs, Product.Cols.ID),
			CCLAUtils.getResultSetIntegerValue(rs, Campaign.Cols.CAMPAIGN_ID),
			CCLAUtils.getResultSetIntegerValue(rs, Form.Cols.ID),
			
			rs.getDateValueByName(Cols.DATE_LATEST_CASH_PROCESSED),
			rs.getDateValueByName(Cols.DATE_COMPLETED),
			rs.getDateValueByName(SharedCols.START_DATE),
			rs.getDateValueByName(SharedCols.END_DATE)
		);
	}
	
	public static DataResultSet getData(int id, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.ID, id);
		
		return facade.createResultSet(Queries.GET, binder);
	}
	
	/** Returns a ResultSet containing all donations linked to the given investment ID
	 * 
	 * @param investmentId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getAllDataBySubscriptionId
	 (int subscriptionId, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, Subscription.Cols.ID, subscriptionId);
		
		return facade.createResultSet(Queries.GET_ALL_BY_SUBSCRIPTION_ID, binder);
	}
	
	/** Returns a ResultSet containing all donations linked to the given investment ID,
	 *  along with extra aggregation columns that describe the mapped TTLA Allocations
	 *  for each Contribution.
	 * 
	 * @param investmentId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getAllDataBySubscriptionIdExtended
	 (int investmentId, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, Subscription.Cols.ID, investmentId);
		
		return facade.createResultSet
		 (Queries.GET_ALL_BY_SUBSCRIPTION_ID_EXTENDED, binder);
	}
	
	/** Returns a list containing all Contributions linked to the given investment ID
	 * 
	 * @param investmentId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Vector<Contribution> getAllBySubscriptionId
	 (int subscriptionId, FWFacade facade) throws DataException {
		
		DataResultSet rs = getAllDataBySubscriptionId(subscriptionId, facade);
		Vector<Contribution> donations = new Vector<Contribution>();
		
		if (rs.first()) {
			do {
				donations.add(get(rs));
			} while (rs.next());
		}
		
		return donations;
	}
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		super.addFieldsToBinder(binder);
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.ID, this.getId());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Subscription.Cols.ID, this.getSubscriptionId());
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.CONTRIBUTOR_ID, this.getContributorId());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.BENEFACTOR_ID, this.getBenefactorId());
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, ContributionType.Cols.ID, this.getContributionTypeId());
		CCLAUtils.addQueryParamToBinder
		 (binder, Cols.PAYMENT_REF, this.getPaymentRef());
		CCLAUtils.addQueryBigDecimalParamToBinder
		 (binder, Cols.CONTRIBUTION_AMOUNT, this.getAmount());
		CCLAUtils.addQueryBigDecimalParamToBinder
		 (binder, Cols.CONTRIBUTION_PERCENT, this.getPercent());
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, RetailPriceIndex.Cols.ID, this.getRetailPriceIndexId());
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.MATCHED_CONTRIBUTION_ID, this.getMatchedContributionId());
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Campaign.Cols.CAMPAIGN_ID, this.getCampaignId());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Product.Cols.ID, this.getProductId());
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Form.Cols.ID, this.getFormId());
		
		CCLAUtils.addQueryDateParamToBinder
		 (binder, Cols.DATE_LATEST_CASH_PROCESSED, this.getDateLatestCashProcessed());
		CCLAUtils.addQueryDateParamToBinder
		 (binder, Cols.DATE_COMPLETED, this.getDateCompleted());
		
		CCLAUtils.addQueryDateParamToBinder
		 (binder, SharedCols.START_DATE, this.getStartDate());
		CCLAUtils.addQueryDateParamToBinder
		 (binder, SharedCols.END_DATE, this.getEndDate());
	}

	public void persist(FWFacade facade, String username) throws DataException {
		super.persist(facade, username);
		
		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);
		
		DataResultSet rsBeforeData = getData(this.getId(), facade);
		facade.execute(Queries.UPDATE, binder);
		DataResultSet rsAfterData = getData(this.getId(), facade);
		
		// Audit the Update action
		
		// Link to the Contribution ID, Donor ID and Investment ID
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(this.getId(), this.getEntityName());
		auditRelations.put(this.getContributorId(), "Donor");
		auditRelations.put(this.getSubscriptionId(), Subscription.class.getSimpleName());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.UPDATE.toString(), this.getEntityName(),
		 rsBeforeData, rsAfterData, auditRelations);
	}

	public void remove(FWFacade facade, String userName) throws DataException {
		DataResultSet rsBeforeData = getData(this.getId(), facade);
		
		Log.debug("Removing Contribution ID " + this.getId() + " entry");
		
		if (rsBeforeData.isEmpty()) {
			throw new DataException("Unable to remove " +
			 this.getEntityName() + ", not found in the database");
		}
		
		// First delete any associated TTLA Allocations.
		Vector<ContributionTTLAAllocation> ttlaAllocs = ContributionTTLAAllocation
		 .getAllByContributionId(this.getId(), facade);
		
		if (!ttlaAllocs.isEmpty()) {
			Log.debug("Found " + ttlaAllocs.size() + " TTLA Allocations. " +
			 "Removing these first.");
		
			for (ContributionTTLAAllocation alloc : ttlaAllocs) {
				alloc.remove(facade, userName);
			}
		}
		
		// Now delete any associated Fund Allocations.
		Vector<ContributionAssetAllocation> fundAllocs = ContributionAssetAllocation
		 .getAllByContributiontId(this.getId(), facade);
		
		if (!fundAllocs.isEmpty()) {
			Log.debug("Found " + fundAllocs.size() + " Fund Allocations. " +
			 "Removing these first.");
		
			for (ContributionAssetAllocation alloc : fundAllocs) {
				alloc.remove(facade, userName);
			}
		}
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.ID, this.getId());
		
		facade.execute(Queries.REMOVE, binder);
		
		// Audit the Remove action
		
		// Link to the Contribution ID, Donor ID and Investment ID
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(this.getId(), this.getEntityName());
		auditRelations.put(this.getContributorId(), "Donor");
		auditRelations.put(this.getSubscriptionId(), Subscription.class.getSimpleName());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, userName, 
		 Globals.AuditActions.DELETE.toString(), this.getEntityName(),
		 rsBeforeData, null, auditRelations);
	}
	
	public void setAttributes(DataBinder binder) throws DataException {
		super.setAttributes(binder);

		this.setSubscriptionId(CCLAUtils.getBinderIntegerValue
		 (binder, Subscription.Cols.ID));
		
		this.setDonorId(CCLAUtils.getBinderIntegerValue
		 (binder, Cols.CONTRIBUTOR_ID));
		this.setBenefactorId(CCLAUtils.getBinderIntegerValue
		 (binder, Cols.BENEFACTOR_ID));

		this.setContributionTypeId(CCLAUtils.getBinderIntegerValue
		 (binder, ContributionType.Cols.ID));
		this.setPaymentRef(CCLAUtils.getBinderStringValue(binder, Cols.PAYMENT_REF));
		this.setAmount(CCLAUtils.getBinderBigDecimalValue
		 (binder, Cols.CONTRIBUTION_AMOUNT));
		this.setPercent(CCLAUtils.getBinderBigDecimalValue
		 (binder, Cols.CONTRIBUTION_PERCENT));
		
		this.setRetailPriceIndexId(CCLAUtils.getBinderIntegerValue
		 (binder, RetailPriceIndex.Cols.ID));
		this.setMatchedContributionId(CCLAUtils.getBinderIntegerValue
		 (binder, Cols.MATCHED_CONTRIBUTION_ID));
		
		this.setProductId(CCLAUtils.getBinderIntegerValue
		 (binder, Product.Cols.ID));
		this.setCampaignId(CCLAUtils.getBinderIntegerValue
		 (binder, Campaign.Cols.CAMPAIGN_ID));
		this.setFormId(CCLAUtils.getBinderIntegerValue
		 (binder, Form.Cols.ID));
		
		this.setDateLatestCashProcessed
		 (CCLAUtils.getBinderDateValue(binder, Cols.DATE_LATEST_CASH_PROCESSED));
		this.setDateCompleted
		 (CCLAUtils.getBinderDateValue(binder, Cols.DATE_COMPLETED));		
		
		this.setStartDate(CCLAUtils.getBinderDateValue
				 (binder, SharedCols.START_DATE));
		this.setEndDate(CCLAUtils.getBinderDateValue
				 (binder, SharedCols.END_DATE));
	}

	public void validate(FWFacade facade) throws DataException {
		super.validate(facade);
	}
	
	/** Returns the total of all Contribution Amounts.
	 *  
	 *  Will always return zero as opposed to null, even if there are no Contributions, 
	 *  or none have amounts set.
	 * 
	 *  Always ensures the number is returned with 2-decimal precision. 
	 * 
	 * @param contributions
	 * @return
	 */
	public static BigDecimal getTotalContributionAmount
	 (Vector<Contribution> contributions) {
		
		BigDecimal totalContributionAmount = BigDecimal.ZERO;
		
		for (Contribution contribution : contributions) {
			BigDecimal thisAmount = contribution.getAmount();
		
			if (thisAmount != null) {
				totalContributionAmount = totalContributionAmount.add(thisAmount);
			}
		}
		
		totalContributionAmount = totalContributionAmount.setScale
		 (2, RoundingMode.HALF_UP);
		
		Log.debug("Calculating new Subscription Amount. Added up " + contributions.size() +
		 " donations, total amount was: " + totalContributionAmount);
		
		return totalContributionAmount;
	}
	
	/** Returns a mapping between Contributions and their relative share (expressed as
	 *  a number between 0 and 1)
	 *  
	 *  If a Contribution has no Amount set, its share will be zero. This ensures that
	 *  all Contributions will have a share set.
	 *  
	 * @param contributions
	 * @return
	 */
	public static HashMap<Contribution, BigDecimal> getContributionShareMapping
	 (Vector<Contribution> contributions) {
		
		HashMap<Contribution, BigDecimal> shares = 
		 new HashMap<Contribution, BigDecimal>();
		
		BigDecimal totalContributionAmount = Contribution.getTotalContributionAmount
		 (contributions);
		
		Log.debug("Calculating relative share between " + contributions.size() + 
		 " Contributions, total amount: " + totalContributionAmount.toPlainString());
		
		for (Contribution contribution : contributions) {
			BigDecimal share = BigDecimal.ZERO;
			
			if (contribution.getAmount() != null
				&& totalContributionAmount.compareTo(BigDecimal.ZERO) > 0)
				share = contribution.getAmount().divide
				 (totalContributionAmount, 
				 ContributionAssetAllocation.CONTRIBUTION_PERCENT_PRECISION+2);
			
			shares.put(contribution, share);
		}
	
		for (Map.Entry<Contribution, BigDecimal> entry : shares.entrySet()) {
			String thisAmount = entry.getKey().getAmount() != null ? 
			 entry.getKey().getAmount().toPlainString() : null;
			
			Log.debug("Contribution ID " + entry.getKey().getId() + ": Amount=" 
			 + thisAmount + ", Share=" + entry.getValue().toPlainString());
		}
		
		return shares;
	}

	@Override
	public String getSequenceName() {
		return "SEQ_CONTRIBUTION";
	}

	public int getContributorId() {
		return contributorId;
	}

	public void setContributorId(int contributorId) {
		this.contributorId = contributorId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	/** Test if the instance attributes differ from the passed values.
	 * 
	 * @param amount
	 * @return
	 */
	public boolean hasChanged(
		BigDecimal amount, 
		BigDecimal percent,
		Integer contributionTypeId,
		Integer retailPriceIndexId,
		Integer productId,
		Integer campaignId,
		Integer formId) {
		
		return (
			!CCLAUtils.bigDecimalsMatch(this.getAmount(), amount)
			||
			!CCLAUtils.bigDecimalsMatch(this.getPercent(), percent)
			||
			!CCLAUtils.integersMatch(this.getContributionTypeId(), contributionTypeId)
			||
			!CCLAUtils.integersMatch(this.getRetailPriceIndexId(), retailPriceIndexId)
			||
			!CCLAUtils.integersMatch(this.getProductId(), productId)
			||
			!CCLAUtils.integersMatch(this.getCampaignId(), campaignId)
			||
			!CCLAUtils.integersMatch(this.getFormId(), formId)
		);
	}

	public void setSubscriptionId(int subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public int getSubscriptionId() {
		return subscriptionId;
	}

	public void setDateLatestCashProcessed(Date dateLatestCashProcessed) {
		this.dateLatestCashProcessed = dateLatestCashProcessed;
	}

	public Date getDateLatestCashProcessed() {
		return dateLatestCashProcessed;
	}

	public Integer getMatchedContributionId() {
		return matchedContributionId;
	}

	public void setMatchedContributionId(Integer matchedContributionId) {
		this.matchedContributionId = matchedContributionId;
	}

	public Integer getBenefactorId() {
		return benefactorId;
	}

	public void setBenefactorId(Integer benefactorId) {
		this.benefactorId = benefactorId;
	}

	public String getPaymentRef() {
		return paymentRef;
	}

	public void setPaymentRef(String paymentRef) {
		this.paymentRef = paymentRef;
	}

	public Integer getRetailPriceIndexId() {
		return retailPriceIndexId;
	}

	public void setRetailPriceIndexId(Integer retailPriceIndexId) {
		this.retailPriceIndexId = retailPriceIndexId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Integer campaignId) {
		this.campaignId = campaignId;
	}

	public Integer getFormId() {
		return formId;
	}

	public void setFormId(Integer formId) {
		this.formId = formId;
	}

	public Date getDateCompleted() {
		return dateCompleted;
	}

	public void setDateCompleted(Date dateCompleted) {
		this.dateCompleted = dateCompleted;
	}

	public void setDonorId(Integer donorId) {
		this.contributorId = donorId;
	}

	public void setContributionTypeId(Integer contributionTypeId) {
		this.contributionTypeId = contributionTypeId;
	}

	public Integer getContributionTypeId() {
		return contributionTypeId;
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

	public void setPercent(BigDecimal percent) {
		this.percent = percent;
	}

	public BigDecimal getPercent() {
		return percent;
	}
	
	/** Calculates and sets the relative percentages for the passed set of 
	 *  Contributions.
	 *   
	 * @param testContributions
	 * @throws DataException 
	 * @throws DataException if any contribution is passed with a zero amount. These
	 * 						should be filtered out of the passed set beforehand.
	 */
	public static void calculateAndSetPercentages(
	 Vector<Contribution> contributions) throws DataException {

		// First determine the total amount.
		BigDecimal totalAmount = getTotalContributionAmount(contributions);
		
		if (totalAmount.compareTo(BigDecimal.ZERO) == 0) {
			// Contribution set is empty, or only contains null/zero amounts.
			// Return all contributions with null percentages.
			return;
		}
		
		BigDecimal totalPercent = BigDecimal.ZERO;
		
		for (Contribution contrib : contributions) {
			if (contrib.getAmount() != null) {
				BigDecimal percent = null;
				
				if (contrib.getAmount().compareTo(BigDecimal.ZERO) == 0) {
					throw new DataException("Unable to calculate contribution " + 
					 "percentages: contribution passed with zero amount");
				} else {
					percent = NumberUtils.convertCashAmountToPercentage
					 (contrib.getAmount(), totalAmount, CONTRIBUTION_PERCENT_PRECISION);
				}
				
				contrib.setPercent(percent);
				totalPercent = totalPercent.add(percent);
			}
		}
		
		// Check that cumulative percentages add up to 100.
		Log.debug("Calculated percentages for " + contributions.size() + 
		 " contributions, sum of percentages was: " + totalPercent.toPlainString());
		
		if (totalPercent.compareTo(NumberUtils.ONE_HUNDRED) != 0) {
			// There must have been a rounding error. Amend this by adding the 
			// difference to the first non-null contribution percentage.
			BigDecimal diff = NumberUtils.ONE_HUNDRED.subtract(totalPercent);
			
			Log.debug("Difference was: " + diff.toPlainString() + 
			  ". Amending the first percentage to ensure percentages add to 100");
			
			for (Contribution contrib : contributions) {
				if (contrib.getAmount() != null) {
					BigDecimal percentAmended = contrib.getPercent().add(diff);
					
					Log.debug("Amending contributor ID " + contrib.getContributorId() + 
					 " percentage from " + contrib.getPercent().toPlainString() + 
					 " to: " + percentAmended.toPlainString());
					
					contrib.setPercent(percentAmended);
					
					break;
				}
			}
		}
	}
	
	public boolean equals(Object o) {
		if (o != null && (o instanceof Contribution))
			return ((Contribution)o).getId().equals(this.getId());
		else
			return false;
	}

	@Override
	public int hashCode() {
		return this.getId();
	}
	
	
}