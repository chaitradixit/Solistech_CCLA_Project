package com.ecs.stellent.ccla.clientservices;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.server.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import com.ecs.stellent.ccla.clientservices.campaign.CommunityFirstClientEnrolmentHandler;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.NumberUtils;
import com.ecs.ucm.ccla.NumberUtils.AmountPercentPair;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.Element;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Fund;
import com.ecs.ucm.ccla.data.Relation;
import com.ecs.ucm.ccla.data.RelationName;
import com.ecs.ucm.ccla.data.RetailPriceIndex;
import com.ecs.ucm.ccla.data.SystemConfigVar;
import com.ecs.ucm.ccla.data.campaign.Campaign;
import com.ecs.ucm.ccla.data.form.Form;
import com.ecs.ucm.ccla.data.product.ApplicableProductAssetInvestment;
import com.ecs.ucm.ccla.data.product.Product;
import com.ecs.ucm.ccla.data.subscription.Contribution;
import com.ecs.ucm.ccla.data.subscription.ContributionAssetAllocation;
import com.ecs.ucm.ccla.data.subscription.ContributionTTLAAllocation;
import com.ecs.ucm.ccla.data.subscription.ContributionType;
import com.ecs.ucm.ccla.data.subscription.GovMatchRate;
import com.ecs.ucm.ccla.data.subscription.Subscription;
import com.ecs.ucm.ccla.data.subscription.SubscriptionAssetAllocation;
import com.ecs.ucm.ccla.data.subscription.TTLAGovMatchLimit;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Service handlers for managing Donors, Contributions and their investment allocations
 * 
 * @author Tom
 *
 */
public class ContributionService extends Service {
	
	public void addContribution() throws DataException, ServiceException {
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace,true);
		
		try {
			facade.beginTransaction();
			
			Contribution.add(m_binder, facade, m_userData.m_name);
			
			facade.commitTransaction();
			
		} catch (Exception e) {
			facade.rollbackTransaction();
			
			String msg = "Failed to add new donation: " + e.getMessage();
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}
	
	/** Adds information for a given Contribution and its associated TTLA/Asset 
	 *  Allocations to the binder.
	 *  
	 * @throws DataException 
	 *  
	 */
	public void getContributionInfo() throws DataException {
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace,true);
		
		Integer donationId = CCLAUtils.getBinderIntegerValue
		 (m_binder, Contribution.Cols.ID);
		
		if (donationId == null)
			throw new DataException("Contribution ID missing");
		
		DataResultSet rsContribution = Contribution.getData(donationId, facade);
		m_binder.addResultSet("rsContribution", rsContribution);
		
		Contribution contribution = Contribution.get(rsContribution);
		
		// Fetch list of applicable government match rates
		DataResultSet rsGovMatchRates = GovMatchRate.getAllDataByContributionType
		 (contribution.getContributionTypeId(), facade);
		m_binder.addResultSet("rsGovMatchRates", rsGovMatchRates);
		
		// Fetch asset allocations
		DataResultSet rsContributionAssetAllocations = 
		 ContributionAssetAllocation.getAllDataByContributionId(donationId, facade);
		
		m_binder.addResultSet
		 ("rsContributionAssetAllocations", rsContributionAssetAllocations);
		
		// Add available Funds for investment allocation
		DataResultSet rsFunds = ApplicableProductAssetInvestment
		 .getApplicableFundsForInvestmentByProductData(contribution.getProductId(), facade);

		// Apply custom re-ordering for funds list.
		rsFunds = Subscription.getSortedFundsData(contribution.getProductId(), rsFunds);
		m_binder.addResultSet("rsFunds", rsFunds);
		
		// Donor Accounts.
		DataResultSet rsDonorAccounts = SubscriptionUtils.getLCFDonorAccountsData
		 (contribution.getBenefactorId(), contribution.getContributorId(), facade);
		
		m_binder.addResultSet("rsDonorAccounts", rsDonorAccounts);
		
		// Legacy stuff for fetching TTLA allocations. Preserved here so that original
		// screens will still display older data correctly
		// ----
		// Fetch existing TTLA allocations (if any)
		DataResultSet rsTTLAAllocations = 
		 ContributionTTLAAllocation.getAllDataByContributionId(donationId, facade);
		m_binder.addResultSet("rsTTLAAllocations", rsTTLAAllocations);
		
		// Fetch all TTLA Org IDs that can be allocated to
		DataResultSet rsTTLAOrganisations = CommunityFirstClientEnrolmentHandler
		 .getAllTTLAOrganisationsData(facade);
		m_binder.addResultSet("rsTTLAOrganisations", rsTTLAOrganisations);
	}
	
	/** Currently unused */
	public void updateContribution() throws DataException, ServiceException {
	
		FWFacade facade = CCLAUtils.getFacade(m_workspace,true);
		
		try {
			Integer donationId = CCLAUtils.getBinderIntegerValue
			 (m_binder, Contribution.Cols.ID);
			
			if (donationId == null)
				throw new DataException("Contribution ID missing");
			
			Contribution contribution = Contribution.get(donationId, facade);
			
			if (contribution == null)
				throw new DataException("Contribution ID " + donationId +
				 " is invalid");
			
			facade.beginTransaction();
			
			contribution.setAttributes(m_binder);
			contribution.persist(facade, m_userData.m_name);
			
			facade.commitTransaction();
			
		} catch (Exception e) {
			facade.rollbackTransaction();
			
			String msg = "Failed to update donation: " + e.getMessage();
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}
	
	/** Updates the set of Contribution Asset Allocations associated with a single 
	 *  Contribution.
	 *  
	 * @throws DataException 
	 * @throws ServiceException 
	 * 
	 */
	public void updateContributionAssetAllocations() 
	 throws DataException, ServiceException {
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace,true);
		
		try {
			Integer contributionId = CCLAUtils.getBinderIntegerValue
			 (m_binder, Contribution.Cols.ID);
			
			if (contributionId == null)
				throw new DataException("Contribution ID missing");
			
			Contribution contribution = Contribution.get(contributionId, facade);
			
			if (contribution == null)
				throw new DataException("Contribution ID " + contributionId +
				 " is invalid");
			
			// Determine the selected Gov Match Rate in the header of the form. This will
			// be applied to all the allocations.
			Integer govMatchRateId = CCLAUtils.getBinderIntegerValue
			 (m_binder, GovMatchRate.Cols.ID);
			
			// Fetch all Funds that can be allocated to
			Vector<Fund> availableFunds = CommunityFirstClientEnrolmentHandler
			 .getAllFundsEligibleForCommunityFirst(facade);
			
			// First fetch the latest allocations submitted by the user. Before
			// persisting any changes to the DB, we need to validate all the allocations
			// as a group.
			Vector<ContributionAssetAllocation> capturedAllocations = 
			 new Vector<ContributionAssetAllocation>();
		
			for (Fund fund : availableFunds) {
				// Check the binder for a mapped amount for this Fund.
				BigDecimal fundAmount = CCLAUtils.getBinderBigDecimalValue
				 (m_binder, ContributionAssetAllocation.Cols.CONTRIBUTION_AMOUNT
				 + "_" + fund.getFundCode());
				
				// Check the binder for a mapped percentage for this Fund.
				BigDecimal fundPercent = CCLAUtils.getBinderBigDecimalValue
				 (m_binder, ContributionAssetAllocation.Cols.CONTRIBUTION_PERCENT
				 + "_" + fund.getFundCode());
				
				// Treat a zero-valued Fund Allocation amount to be the same as null
				if (fundAmount != null && fundAmount.compareTo(BigDecimal.ZERO) == 0)
					fundAmount = null;
				
				BigDecimal totalCash = contribution.getAmount();
				
				// Calculate amount/percent, whichever one is missing.
				AmountPercentPair amountPercent = new AmountPercentPair
				 (fundAmount, fundPercent);
				
				amountPercent = NumberUtils.computePercentageOrAmount
				 (amountPercent, totalCash, 
				 ContributionAssetAllocation.CONTRIBUTION_PERCENT_PRECISION);
				
				fundAmount = amountPercent.getAmount();
				fundPercent = amountPercent.getPercent();
				
				if (fundAmount != null) {
					// Fetch Gov Match values.
					
					BigDecimal govMatchExpected = CCLAUtils.getBinderBigDecimalValue
					 (m_binder, ContributionAssetAllocation.Cols.GOV_RECOVERY_AMT_EXPECTED
					 + "_" + fund.getFundCode());
					
					BigDecimal govMatchActual = CCLAUtils.getBinderBigDecimalValue
					 (m_binder, ContributionAssetAllocation.Cols.GOV_RECOVERY_AMT_ACTUAL
					 + "_" + fund.getFundCode());
					
					// Add the allocation 
					ContributionAssetAllocation subscriptionAllocs = 
					 new ContributionAssetAllocation(
						contribution.getId(),
						fund.getFundCode(),
						null,
						fundAmount,
						fundPercent,
						govMatchExpected,
						govMatchActual,
						govMatchRateId
					);
					
					capturedAllocations.add(subscriptionAllocs);
				}
			}
			
			// Now validate all the allocations we just fetched before continuing.
			// Do a non-strict validation here.
			ContributionAssetAllocation.validate(contribution, capturedAllocations, false);
			
			// Check whether Expected Gov Match rates are calculated automatically,
			// regardless of what the user entered.
			SystemConfigVar calcGovMatchVar = SystemConfigVar.getCache().getCachedInstance
			 ("CommunityFirst_CalculateExpectedGovMatchAmounts");
	
			boolean calculateGovMatchAmounts = 
			 (calcGovMatchVar != null && calcGovMatchVar.getBoolValue());
			
			if (calculateGovMatchAmounts && govMatchRateId != null) {
				GovMatchRate govMatchRate = GovMatchRate.getById(govMatchRateId, facade);

				for (ContributionAssetAllocation alloc : capturedAllocations) {
					// Calculate this match value, based on the chosen Gov. Match
					// Rate. Will overwrite any gov. match value captured from the
					// binder previously.
					alloc.setGovRecoveryAmountExpected(govMatchRate.getGovMatchAmount
					 (alloc.getAmount()));
				}
			}

			// If we got this far, the allocation amounts have been 
			// validated successfully. Now apply changes to the database.
			facade.beginTransaction();
	
			ContributionAssetAllocation.addOrUpdateContributionFundAllocations
			 (contribution, capturedAllocations, availableFunds, facade, m_userData.m_name);
			
			facade.commitTransaction();
			
			Log.debug("Successfully updated fund allocations for Contribution ID " 
			 + contribution.getId());
			
		} catch (Exception e) {
			facade.rollbackTransaction();
			
			String msg = "Failed to update donation: " + e.getMessage();
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}
	
	/** 
	 * @deprecated since retiring TTLAs from the Community First scheme.
	 * 
	 * Batch-updates all TTLA Allocations for a given Contribution.
	 * 
	 *  Requires the following 'header' values in the binder, which will be added
	 *  against each TTLA allocation record:
	 *  
	 *  -GOV_MATCH_RATE_ID
	 *  -PRODUCT_ID
	 * 
	 *  Searches the binder for various parameters for each TTLA, where XXX is the TTLA 
	 *  Org ID:
	 *  
	 *  -TTLA_ORG_ID_XXX
	 *  -INCOME_ALLOCATION_PERCENT_XXX or INCOME_ALLOCATION_AMOUNT_XXX
	 *  -GOV_RECOVERY_AMT_EXPECTED_XXX
	 *  -GOV_RECOVERY_AMT_ACTUAL_XXX
	 *  -GOV_MATCH_LIMIT_ID_XXX
	 * 
	 *  If the first one is present, the user has selected this TTLA and the allocation
	 *  to the TTLA must be added/updated in the DB.
	 * 
	 *  Either percentage or amount values will be passed in but not both. This is 
	 *  determined by the valueType binder parameter.
	 * 
	 * @throws DataException 
	 * @throws ServiceException 
	 */
	public void updateContributionTTLAAllocations() 
	 throws DataException, ServiceException {
	
		FWFacade facade = CCLAUtils.getFacade(m_workspace,true);
		
		try {
			Integer contributionId = CCLAUtils.getBinderIntegerValue
			 (m_binder, Contribution.Cols.ID);
			
			if (contributionId == null)
				throw new DataException("Contribution ID missing");
			
			Log.debug("Updating TTLA allocations for Contribution ID " + contributionId);
			
			// Check this is a valid Contribution ID before continuing
			Contribution donation = Contribution.get(contributionId, facade);
			
			if (donation == null)
				throw new DataException("Contribution ID " + contributionId +
				 " is invalid");
			
			// Fetch header values, that will be set against each allocation.
			
			Integer govMatchRateId = CCLAUtils.getBinderIntegerValue
			 (m_binder, GovMatchRate.Cols.ID);
			
			Integer productId = CCLAUtils.getBinderIntegerValue
			 (m_binder, Product.Cols.ID); 
			
			if (govMatchRateId == null || productId == null) {
				throw new DataException("Gov Match Rate ID or Product ID missing");
			}
			
			// Fetch existing allocations (if any)
			Vector<ContributionTTLAAllocation> allocs =
			 ContributionTTLAAllocation.getAllByContributionId(contributionId, facade);
			
			// Fetch all TTLA Org IDs that can be allocated to
			Vector<Integer> availableOrgIds = CommunityFirstClientEnrolmentHandler
			 .getAllTTLAOrganisationIds(facade);
			
			// Add the 'null' Org entry, to pick up TBC allocation if present.
			availableOrgIds.add(null);
			
			// First fetch the latest allocations submitted by the user. Before
			// persisting any changes to the DB, we need to validate all the allocations
			// as a group.
			Vector<ContributionTTLAAllocation> testAllocs = 
			 new Vector<ContributionTTLAAllocation>();
		
			//Check the value type. Determines whether percentage or amount values
			//will be used to compute allocations.
			String valueType = CCLAUtils.getBinderStringValue(m_binder, "valueType");
			
			boolean isPercentage = true;
			
			if (valueType.equalsIgnoreCase("amount"))
				isPercentage=false;
	
			BigDecimal totalAmount = donation.getAmount();
			// Treat a zero-valued Allocation amount to be the same as null
			if (totalAmount != null && totalAmount.equals(BigDecimal.ZERO))
				totalAmount = null;

			for (Integer orgId : availableOrgIds) {
				String orgIdStr = orgId != null ? orgId.toString() : "";
				
				// Check the binder for the flag for this Org.
				// Will be in the form TTLA_ORG_ID_XXX, where XXX = the TTLA's
				// Organisation ID.
				boolean ttlaSelected =  CCLAUtils.getBinderBoolValue
				 (m_binder, ContributionTTLAAllocation.Cols.TTLA_ORG_ID + "_" + orgIdStr);
				
				BigDecimal ttlaAllocPercent = null, ttlaAllocAmount = null;
				
				if (ttlaSelected) {
					// Check the binder for a mapped allocation percent for this Org.
					ttlaAllocPercent = CCLAUtils.getBinderBigDecimalValue
					 (m_binder, ContributionTTLAAllocation.Cols.INCOME_ALLOCATION_PERCENT  
					 + "_" + orgIdStr);
	
					// Check the binder for a mapped allocation amount for this Org.
					ttlaAllocAmount = CCLAUtils.getBinderBigDecimalValue
					 (m_binder, ContributionTTLAAllocation.Cols.INCOME_ALLOCATION_AMOUNT  
					 + "_" + orgIdStr);
					
					AmountPercentPair amountPercent = new AmountPercentPair
					 (ttlaAllocAmount, ttlaAllocPercent);
					
					// Calculate percent/amount.
					amountPercent = NumberUtils.computePercentageOrAmount
					 (amountPercent, totalAmount,
					 ContributionTTLAAllocation.INCOME_ALLOCATION_PERCENT_PRECISION);
					
					// Replace refs with calculated values
					ttlaAllocAmount = amountPercent.getAmount();
					ttlaAllocPercent = amountPercent.getPercent();
					
					// Add the allocation, with minimal data required for a non-strict
					// validation check.

					ContributionTTLAAllocation testAlloc = 
					 new ContributionTTLAAllocation(
						contributionId,
						orgId,
						productId,
						ttlaAllocPercent,
						ttlaAllocAmount,
						null,
						govMatchRateId,
						null,
						null,
						null,
						null
					);
					
					testAllocs.add(testAlloc);
				}
			}
			
			// Now validate all the allocations we just fetched before continuing.
			// Do a non-strict validation here.
			ContributionTTLAAllocation.validate(donation, testAllocs, false);
			
			// If we got this far, the TTLA allocation amounts have been validated
			// successfully for this donation. Now apply changes to the database.
			
			facade.beginTransaction();
			
			for (Integer orgId : availableOrgIds) {
				String orgIdStr = orgId != null ? orgId.toString() : "";
				
				// Check the binder for the selected flag for this TTLA.
				//
				// Will be in the form TTLA_ORG_ID_XXX, where XXX = the TTLA's
				// Organisation ID.
				boolean ttlaSelected =  CCLAUtils.getBinderBoolValue
				 (m_binder, ContributionTTLAAllocation.Cols.TTLA_ORG_ID + "_" + orgIdStr);
				
				BigDecimal ttlaAllocPercent = null, ttlaAllocAmount = null;
				
				Integer 	govMatchLimitId = null;
				
				BigDecimal 	govRecoveryAmountExpected = null, 
							govRecoveryAmountActual = null;
				
				if (ttlaSelected) {
					// Check the binder for a mapped allocation percent for this Org.
					ttlaAllocPercent = CCLAUtils.getBinderBigDecimalValue
					 (m_binder, ContributionTTLAAllocation.Cols.INCOME_ALLOCATION_PERCENT  
					 + "_" + orgIdStr);
	
					// Check the binder for a mapped allocation amount for this Org.
					ttlaAllocAmount = CCLAUtils.getBinderBigDecimalValue
					 (m_binder, ContributionTTLAAllocation.Cols.INCOME_ALLOCATION_AMOUNT  
					 + "_" + orgIdStr);

					AmountPercentPair amountPercent = new AmountPercentPair
					 (ttlaAllocAmount, ttlaAllocPercent);
					
					// Calculate percent/amount.
					amountPercent = NumberUtils.computePercentageOrAmount
					 (amountPercent, totalAmount,
					 ContributionTTLAAllocation.INCOME_ALLOCATION_PERCENT_PRECISION);
					
					// Replace refs with calculated values
					ttlaAllocAmount = amountPercent.getAmount();
					ttlaAllocPercent = amountPercent.getPercent();
					
					// Fetch other TTLA allocation attributes
					
					/*
					 * Below values are currently set the same for all allocations
					productId = CCLAUtils.getBinderIntegerValue
					 (m_binder, Product.Cols.ID + "_" + orgIdStr);
					
					govMatchRateId = CCLAUtils.getBinderIntegerValue
					 (m_binder, GovMatchRate.Cols.ID + "_" + orgIdStr);
					*/
					
					govMatchLimitId = CCLAUtils.getBinderIntegerValue
					 (m_binder, TTLAGovMatchLimit.Cols.ID + "_" + orgIdStr);
					
					govRecoveryAmountExpected = CCLAUtils.getBinderBigDecimalValue
					 (m_binder, 
					 ContributionTTLAAllocation.Cols.GOV_RECOVERY_AMT_EXPECTED + "_" 
					 + orgIdStr);
					
					govRecoveryAmountActual = CCLAUtils.getBinderBigDecimalValue
					 (m_binder, 
					 ContributionTTLAAllocation.Cols.GOV_RECOVERY_AMT_ACTUAL + "_" 
					 + orgIdStr);
				}
				
				// Did we allocate to this TTLA Org already?
				ContributionTTLAAllocation existingAlloc = null;
				
				for (ContributionTTLAAllocation alloc : allocs) {
					if (CCLAUtils.integersMatch(alloc.getTTLAOrgId(), orgId)) {
						existingAlloc = alloc;
						break;
					}
				}
				
				if (existingAlloc != null) {
					if (!ttlaSelected) {
						// Previous allocation has now been removed.
						Log.debug("Removing existing allocation to Org ID " + 
						 (orgId == null ? "[none]" : orgId));
						
						existingAlloc.remove(facade, m_userData.m_name);
					} else {
						// Previous allocation percentage/amount has possibly been 
						// updated.
						if (existingAlloc.hasChanged(
							ttlaAllocPercent, 
							ttlaAllocAmount,
							productId,
							govMatchRateId,
							govMatchLimitId,
							govRecoveryAmountExpected,
							govRecoveryAmountActual)) {
							// Yep, its been updated.
							
							Log.debug("Updating TTLA Org ID " +
							 (orgId == null ? "[none]" : orgId) + " allocation: " +
							 "Amount: " + (ttlaAllocAmount == null ? 	
								"[none]" : ttlaAllocAmount.toPlainString()) + 
							
							 ", Percent: " + (ttlaAllocPercent == null ?
								"[none]" : ttlaAllocPercent.toPlainString()));
							
							existingAlloc.setAllocationAmount(ttlaAllocAmount);
							existingAlloc.setAllocationPercent(ttlaAllocPercent);
							
							existingAlloc.setGovMatchRateId(govMatchRateId);
							existingAlloc.setGovMatchLimitId(govMatchLimitId);
							
							existingAlloc.setProductId(productId);
							
							existingAlloc.setGovRecoveryAmountExpected
							 (govRecoveryAmountExpected);
							existingAlloc.setGovRecoveryAmountActual
							 (govRecoveryAmountActual);
							
							existingAlloc.persist(facade, m_userData.m_name);
							
						} else {
							// No update, do nothing
							Log.debug("No update to TTLA Org ID " +
							 (orgId == null ? "[none]" : orgId)); 
						}
					}
					
				} else if (ttlaSelected) {
					// Add a new allocation
					Log.debug("Adding TTLA Org ID " +
					 (orgId == null ? "[none]" : orgId) + " allocation: " +
					 "Amount: " + (ttlaAllocAmount == null ? 	
						"[none]" : ttlaAllocAmount.toPlainString()) + 
					
					 ", Percent: " + (ttlaAllocPercent == null ?
						"[none]" : ttlaAllocPercent.toPlainString()));
					
					ContributionTTLAAllocation.add(
						contributionId, 
						orgId, 
						productId,
						ttlaAllocPercent, 
						ttlaAllocAmount,
						govMatchLimitId,
						govMatchRateId,
						govRecoveryAmountExpected,
						govRecoveryAmountActual,
						null, null,
						facade, m_userData.m_name
					);
				}	
			}
			
			facade.commitTransaction();
			
			Log.debug("Successfully updated TTLA allocations for Contribution ID " 
			 + contributionId);
			
		} catch (Exception e) {
			facade.rollbackTransaction();
			
			String msg = "Failed to update donation TTLA allocations: " 
			 + e.getMessage();
			
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}
	
	/**
	 *  Batch-updates all Contributions for a given Subscription.
	 *  
	 *  The associated Subscription Amount is updated after to reflect the sum of all
	 *  donations.
	 *  
	 * @throws DataException 
	 * @throws ServiceException 
	 */
	public void updateSubscriptionContributions() 
	 throws DataException, ServiceException {
	
		FWFacade facade = CCLAUtils.getFacade(m_workspace,true);
		
		Integer subscriptionId = CCLAUtils.getBinderIntegerValue
		 (m_binder, Subscription.Cols.ID);
		
		if (subscriptionId == null)
			throw new DataException("Subscription ID missing");
		
		Log.debug("Updating contributions for Subscription ID " + subscriptionId);
		
		// Check this is a valid Subscription ID before continuing
		Subscription subscription = Subscription.get(subscriptionId, facade);
		
		if (subscription == null)
			throw new DataException("Subscription ID " + subscriptionId +
			 " is invalid");
		
		// Fetch 'header' values out the binder which don't yet change on a 
		// per-contribution basis (although they are stored at the Contribution
		// level)
		Integer productId = CCLAUtils.getBinderIntegerValue
		 (m_binder, Product.Cols.ID);
		Integer campaignId = CCLAUtils.getBinderIntegerValue
		 (m_binder, Campaign.Cols.CAMPAIGN_ID);
		Integer formId = CCLAUtils.getBinderIntegerValue
		 (m_binder, Form.Cols.ID);
		Integer contributionTypeId = CCLAUtils.getBinderIntegerValue
		 (m_binder, ContributionType.Cols.ID);
		Integer benefactorId = CCLAUtils.getBinderIntegerValue
		 (m_binder, Contribution.Cols.BENEFACTOR_ID);
		Integer retailPriceIndexId = CCLAUtils.getBinderIntegerValue
		 (m_binder, RetailPriceIndex.Cols.ID);
		
		Integer govMatchRateId = CCLAUtils.getBinderIntegerValue
		 (m_binder, GovMatchRate.Cols.ID);
		
		if (productId == null 
			|| campaignId == null 
			|| contributionTypeId == null
			|| benefactorId == null) {
			throw new DataException("Unable to update contributions: Product ID " +
			 ", Campaign ID, Contribution Type ID or Benefactor ID is missing");
		}
		
		// Fetch existing contributions (if any)
		Vector<Contribution> contributions =
		 Contribution.getAllBySubscriptionId(subscriptionId, facade);
		
		// Determine owner of the account this contribution is linked to.
		Integer orgId = 
		 Account.getOwnerOrganisationId(subscription.getAccountId(), facade);
		
		// Fetch all contributor Elements that can be allocated to
		Vector<Element> availableContributors = getAvailableDonorsByOrganisationId
		 (orgId, facade);
		
		// First fetch the latest allocations submitted by the user. Before
		// persisting any changes to the DB, we need to validate all the allocations
		// as a group, and calculate the percentages.
		Vector<Contribution> testContributions = new Vector<Contribution>();
	
		for (Element contributor : availableContributors) {
			// Check the binder for a flag indicating this donor has been selected
			boolean contributorSelected = CCLAUtils.getBinderBoolValue(m_binder,
			 Contribution.Cols.CONTRIBUTOR_ID + "_" + contributor.getElementId());
			
			if (contributorSelected) {
				// Donor was selected. Fetch other donation values from the binder.
				BigDecimal contributionAmount = CCLAUtils.getBinderBigDecimalValue
				 (m_binder, Contribution.Cols.CONTRIBUTION_AMOUNT
				 + "_" + contributor.getElementId());
				
				// Treat a zero-valued Contribution amount to be the same as null
				// during testing.
				if (contributionAmount != null 
					&& contributionAmount.equals(BigDecimal.ZERO))
					contributorSelected = false;
				
				if (contributorSelected) {
					// Add the test donation instance
					Contribution testContribution = new Contribution
					 (contributor.getElementId(), contributionAmount);
					
					testContributions.add(testContribution);
				}
			}
		}
		
		// Calculate relative percentages for the test contributions.
		Contribution.calculateAndSetPercentages(testContributions);

		// Now validate all the contributions we just fetched before continuing.
		// Do a non-strict validation here.
		// TODO? Even required?
		// Contribution.validate(investment, testContributions, false);
		
		try {
			// If we got this far, the donation amounts have been validated
			// successfully. Now apply changes to the database.
			
			facade.beginTransaction();
			
			for (Element contributor : availableContributors) {
				// Check the binder for a flag indicating this donor has been selected
				boolean contributorSelected = CCLAUtils.getBinderBoolValue(m_binder,
				 Contribution.Cols.CONTRIBUTOR_ID + "_" + contributor.getElementId());
				
				BigDecimal contributionAmount = null,
						contributionPercent = null;
				
				Date 	dateLatestCashProcessed = null, 
						dateCompleted = null;
				
				if (contributorSelected) {
					// Contributor was selected. Fetch other contribution values from
					// the binder.
					
					// Fetch the previously-calculated percentage from the test
					// set of contributions.
					for (Contribution testContrib : testContributions) {
						if (testContrib.getContributorId() 
							== contributor.getElementId()) {
							contributionPercent = testContrib.getPercent();
							break;
						}
					}
					
					contributionAmount = CCLAUtils.getBinderBigDecimalValue
					 (m_binder, Contribution.Cols.CONTRIBUTION_AMOUNT
					 + "_" + contributor.getElementId());
					
					// Don't store zero contribution amounts, treat them as null.
					if (contributionAmount != null 
						&& contributionAmount.compareTo(BigDecimal.ZERO) == 0)
						contributionAmount = null;
					
					/*
					 * Fields below are set in bulk against all contributions at the
					 * moment.
					benefactorId = CCLAUtils.getBinderIntegerValue
					 (m_binder, Contribution.Cols.BENEFACTOR_ID
					 + "_" + contributor.getElementId());
					
					contributionTypeId = CCLAUtils.getBinderIntegerValue
					 (m_binder, ContributionType.Cols.ID
					 + "_" + contributor.getElementId());
					
					retailPriceIndexId = CCLAUtils.getBinderIntegerValue
					 (m_binder, RetailPriceIndex.Cols.ID
					 + "_" + contributor.getElementId());
					 */
				}
				
				// Did we allocate to this Donor already?
				Contribution existingContribution = null;
				
				for (Contribution contribution : contributions) {
					if (CCLAUtils.integersMatch(
							contribution.getContributorId(),
							contributor.getElementId()
						)) {
						existingContribution = contribution;
						break;
					}
				}
				
				if (existingContribution != null) {
					if (!contributorSelected) {
						// Previous contribution has now been removed.
						Log.debug("Removing existing contribution for contributor ID " +
						 contributor.getElementId());
						
						existingContribution.remove(facade, m_userData.m_name);
						
					} else {
						// Previous contribution has possibly been updated.
						if (existingContribution.hasChanged(
								contributionAmount,
								contributionPercent,
								contributionTypeId,
								retailPriceIndexId,
								productId,
								campaignId,
								formId)) {
							// Yep, its been updated.
							Log.debug("Updating contribution for contributor ID " +
							 contributor.getElementId());
							
							existingContribution.setAmount(contributionAmount);
							existingContribution.setPercent(contributionPercent);
							existingContribution.setContributionTypeId(contributionTypeId);
							existingContribution.setRetailPriceIndexId(retailPriceIndexId);
							existingContribution.setProductId(productId);
							existingContribution.setCampaignId(campaignId);
							existingContribution.setFormId(formId);
							
							existingContribution.persist(facade, m_userData.m_name);
						} else {
							Log.debug("No contribution update required for " +
							 "contributor ID " + contributor.getElementId());
						}
					}
					
				} else if (contributorSelected) {
					// Add a new donation
					Log.debug("Adding contribution for contributor ID " 
					 + contributor.getElementId());
					
					Contribution.add(
						subscriptionId, 
						contributor.getElementId(),
						benefactorId,
						contributionTypeId,
						null,
						contributionAmount,
						contributionPercent,
						retailPriceIndexId,
						null,
						productId,
						campaignId,
						formId,
						dateLatestCashProcessed, 
						dateCompleted,
						null,
						null,
						facade, m_userData.m_name
					);
				}	
			}
			
			// Update the parent Subscription Amount
			BigDecimal totalContributionAmount = Subscription.updateSubscriptionAmount
			 (subscription, facade, m_userData.m_name);
			
			// Recalculate the asset distributions, if applicable.
			
			/*
			 * TODO: requires scaling existing asset allocations by the existing
			 * subscription-level percentages.
			 * 
			// Fetch all Funds that can be allocated to
			Vector<Fund> availableFunds = CommunityFirstClientEnrolmentHandler
			 .getAllFundsEligibleForCommunityFirst(facade);
			
			ContributionAssetAllocation.updateContributionFundAllocations
			 (subscription, availableFunds, facade, m_userData.m_name);
			*/
	
			facade.commitTransaction();
			
			Log.debug("Successfully updated fund allocations for Subscription ID " 
			 + subscriptionId);
			
		} catch (Exception e) {
			facade.rollbackTransaction();
			
			String msg = "Failed to update investment fund allocations: " 
			 + e.getMessage();
			
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}
	
	/** Fetches all Person/Org Element instances that have the 'Donor' relationship
	 *  to the given Organisation ID.
	 *  
	 * @param orgId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Vector<Element> getAvailableDonorsByOrganisationId
	 (int orgId, FWFacade facade) throws DataException {
		
		Log.debug("Fetching donors for Org ID: " + orgId);
		
		Vector<Relation> orgDonorRelations = Relation.getRelations
		 (null, orgId, null, RelationName.getCache().getCachedInstance(
		 RelationName.OrganisationOrganisationRelation.DONOR), 
		 facade);
		
		Log.debug("Found " + orgDonorRelations.size() + " Org Donors");
		
		Vector<Relation> personDonorRelations = Relation.getRelations
		 (orgId, null, null, RelationName.getCache().getCachedInstance(
		 RelationName.OrganisationPersonRelation.DONOR),  
		 facade);
		
		Log.debug("Found " + personDonorRelations.size() + " Person Donors");
		
		Vector<Integer> elementIds = new Vector<Integer>();
		
		for (Relation rel : orgDonorRelations)
			elementIds.add(rel.getElementId1());
		
		for (Relation rel : personDonorRelations)
			elementIds.add(rel.getElementId2());
		
		return Element.getAll(elementIds, facade);
	}
	
	/** Adds 4 ResultSets to the binder:
	 * 
	 *  -rsRelatedPersons: all unique Persons who are related to the given Organisation
	 *  				ID
	 *  -rsPersonDonorRelations: list of Org-Person Donor relations for the given
	 *  				Organisation ID
	 *  -rsRelatedOrgs: all unique Orgs who are related to the given Organisation
	 *  				ID
	 *  -rsOrgDonorRelations: list of Org-Org Donor relations for the given
	 *  				Organisation ID
	 *  
	 * @throws DataException 
	 */
	public static void addAvailableDonorsByOrganisationIdDataToBinder
	 (int orgId, DataBinder binder, FWFacade facade) throws DataException {
		
		Vector<Integer> orgPersonRelationNameIds = new Vector<Integer>();
		orgPersonRelationNameIds.add(RelationName.OrganisationPersonRelation.DONOR);
		
		DataResultSet rsRelatedPersons =
		 Relation.getRelatedPersonsData(orgId, ElementType.ORGANISATION, 
		 orgPersonRelationNameIds, facade);
		
		binder.addResultSet("rsRelatedPersons", rsRelatedPersons);
		
		DataResultSet rsPersonDonorRelations = Relation.getRelationData
		 (orgId, null, null, RelationName.getCache().getCachedInstance(
		 RelationName.OrganisationPersonRelation.DONOR), facade);
		
		binder.addResultSet("rsPersonDonorRelations", rsPersonDonorRelations);
		
		Vector<Integer> orgOrgRelationNameIds = new Vector<Integer>();
		orgOrgRelationNameIds.add(RelationName.OrganisationOrganisationRelation.DONOR);
		
		DataResultSet rsRelatedOrgs =
		 Relation.getRelatedOrgsData(null, orgId, orgOrgRelationNameIds, facade);
		
		binder.addResultSet("rsRelatedOrgs", rsRelatedOrgs);
			
		DataResultSet rsOrgDonorRelations = Relation.getRelationData
		 (null, orgId, null, RelationName.getCache().getCachedInstance(
		 RelationName.OrganisationOrganisationRelation.DONOR), facade);
			
		binder.addResultSet("rsOrgDonorRelations", rsOrgDonorRelations);
	}
}
