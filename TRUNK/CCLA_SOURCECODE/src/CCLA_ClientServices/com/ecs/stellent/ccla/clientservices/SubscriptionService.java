package com.ecs.stellent.ccla.clientservices;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.server.Service;
import intradoc.shared.SharedObjects;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;

import com.ecs.stellent.ccla.clientservices.EntityService.OrganisationDataConfig;
import com.ecs.stellent.ccla.clientservices.campaign.CommunityFirstClientEnrolmentHandler;
import com.ecs.stellent.ccla.signature.data.InstructionSignature;
import com.ecs.stellent.spp.service.InstructionDocServices;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals;
import com.ecs.ucm.ccla.NumberUtils;
import com.ecs.ucm.ccla.Globals.UCMFieldNames;
import com.ecs.ucm.ccla.NumberUtils.AmountPercentPair;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.AuroraClient;
import com.ecs.ucm.ccla.data.Company;
import com.ecs.ucm.ccla.data.DataSource;
import com.ecs.ucm.ccla.data.Element;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Fund;
import com.ecs.ucm.ccla.data.OrganisationCategory;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.PersonTitle;
import com.ecs.ucm.ccla.data.Relation;
import com.ecs.ucm.ccla.data.RelationName;
import com.ecs.ucm.ccla.data.form.Form;
import com.ecs.ucm.ccla.data.form.FormHandler;
import com.ecs.ucm.ccla.data.form.FormType;
import com.ecs.ucm.ccla.data.instruction.InstructionType;
import com.ecs.ucm.ccla.data.product.ApplicableProductAssetInvestment;
import com.ecs.ucm.ccla.data.subscription.Contribution;
import com.ecs.ucm.ccla.data.subscription.ContributionAssetAllocation;
import com.ecs.ucm.ccla.data.subscription.ContributionTTLAAllocation;
import com.ecs.ucm.ccla.data.subscription.ContributionType;
import com.ecs.ucm.ccla.data.subscription.Subscription;
import com.ecs.ucm.ccla.data.subscription.SubscriptionAssetAllocation;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.DateFormatter;
import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Service methods for Subscriptions and their allocated Contributions.
 * 
 *  By 'subscription' in this context we mean:
 *  
 *  The action of making or agreeing to make an advance payment in order to receive or 
 *  participate in something.
 * 
 * @author Tom
 *
 */
public class SubscriptionService extends Service {
	
	
	/** Adds a new Subscription. 
	 * 
	 * @throws DataException 
	 * @throws DataException 
	 **/
	public void addSubscription() throws ServiceException, DataException {
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace,true);
		
		try {
			facade.beginTransaction();
			
			Subscription newSubscription = 
			 Subscription.add(m_binder, facade, m_userData.m_name);
			
			// Append new Primary Key so the Info page loads correctly.
			CCLAUtils.appendToBinderParam
			 (m_binder, "RedirectUrl", "&" + Subscription.Cols.ID + 
			 "=" + newSubscription.getId());
			
			facade.commitTransaction();
			
		} catch (Exception e) {
			facade.rollbackTransaction();
			
			String msg = "Failed to add new investment: " + e.getMessage();
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}
	
	public void updateSubscription() throws DataException, ServiceException {
	
		FWFacade facade = CCLAUtils.getFacade(m_workspace,true);
		
		try {
			Integer investmentId = CCLAUtils.getBinderIntegerValue
			 (m_binder, Subscription.Cols.ID);
			
			if (investmentId == null)
				throw new DataException("Subscription ID missing");
			
			Subscription subscription = Subscription.get(investmentId, facade);
			
			if (subscription == null)
				throw new DataException("Subscription ID " + investmentId +
				 " is invalid");
			
			facade.beginTransaction();
			
			subscription.setAttributes(m_binder);
			subscription.persist(facade, m_userData.m_name);
			
			facade.commitTransaction();
			
		} catch (Exception e) {
			facade.rollbackTransaction();
			
			String msg = "Failed to update investment: " + e.getMessage();
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}
	
	/** Marks a subscription as Cancelled. 
	 * 
	 * @throws DataException 
	 * @throws ServiceException 
	 * @throws DataException */
	public void cancelSubscription() throws ServiceException, DataException {
		FWFacade facade = CCLAUtils.getFacade(m_workspace,true);
		
		try {
			
			Integer subscriptionId = CCLAUtils.getBinderIntegerValue
			 (m_binder, Subscription.Cols.ID);
			
			if (subscriptionId == null)
				throw new DataException("Subscription ID missing");
			
			Subscription subscription = Subscription.get(subscriptionId, facade);
			
			if (subscription == null)
				throw new DataException("Subscription ID " + subscriptionId +
				 " is invalid");
			
			facade.beginTransaction();
			
			subscription.setStatusId(Subscription.SubscriptionStatusIds.CANCELLED);
			subscription.persist(facade, m_userData.m_name);
			
			facade.commitTransaction();
			
		} catch (Exception e) {
			facade.rollbackTransaction();
			throw new ServiceException("Unable to cancel Subscription. " 
			 + e.getMessage());
		}
	}
	
	/**
	 *  Adds a new anonymous donor to a given organisation (either an Organisation or 
	 *  Person).
	 *  
	 *  Requires an ORGANISATION_ID (Org who the anonymous donor will be connected to),
	 *  an ELEMENT_TYPE_ID (will relate to either Org or Person element type,
	 *  determines what kind of donor we are going to create)
	 *  
	 * @throws DataException 
	 * @throws ServiceException 
	 *  
	 */
	public void addAnonymousDonor() throws DataException, ServiceException {
		
		Integer recipientOrgId = 
		 CCLAUtils.getBinderIntegerValue(m_binder, Entity.Cols.ID);
		
		Integer donorElementTypeId = CCLAUtils.getBinderIntegerValue
		 (m_binder, ElementType.Cols.ID);
	
		FWFacade facade = CCLAUtils.getFacade(m_workspace,true);
		
		try {
			facade.beginTransaction();
			
			if (recipientOrgId == null)
				throw new DataException(Entity.Cols.ID + " missing");	
			if (donorElementTypeId == null)
				throw new DataException(ElementType.Cols.ID + " missing");
			
			ElementType elementType = ElementType.getCache().getCachedInstance
			 (donorElementTypeId);
			if (elementType == null)
				throw new DataException("unknown Element Type ID: " + 
				 donorElementTypeId);
			
			Log.debug("Creating new Anonymous Donor of type: " + elementType.getName() +
			" for Organisation ID: " + recipientOrgId);
			
			// Prep variables for creating the Relationship between the Person/Org and
			// the Org they are a donor for.
			Integer relElementId1 = null, relElementId2 = null;
			RelationName relName = null;
			
			if (elementType.equals(ElementType.PERSON)) {
				// Need to generate a new Anonymous Person Donor.
				
				// First figure out how many there are already, so we can suffix the
				// Person Name with a number
				relName = RelationName.getCache().getCachedInstance
				 (RelationName.OrganisationPersonRelation.DONOR);
				
				Vector<Integer> relNameIds = new Vector<Integer>();
				relNameIds.add(relName.getRelationNameId());
				
				Vector<Person> donorPersons =
				 Relation.getRelatedPersons(recipientOrgId, ElementType.ORGANISATION, 
				 relNameIds, facade);
				
				int numAnonDonors = 0;
				
				// Determine how many anonymous Person donors exist by adding up all
				// Persons with the Donor relation and the 'Anonymous' title.
				for (Person donorPerson : donorPersons) {
					if (donorPerson.getTitleId() != null
						&& donorPerson.getTitleId().equals
						(PersonTitle.Ids.ANONYMOUS)) {
						numAnonDonors++;
					}
				}
				
				Log.debug("Found " + numAnonDonors + 
				 " existing Anonymous Person Donors");
				
				DataBinder binder = new DataBinder();
				
				// Add DONP Account Code prefix
				binder.putLocal(Person.PERSON_ACCOUNT_CODE_PREFIX,
				 Contribution.DonorAccountCodePrefixes.PERSON);
				
				// Add 'Anonymous' title
				CCLAUtils.addQueryIntParamToBinder
				 (binder, PersonTitle.Cols.ID, PersonTitle.Ids.ANONYMOUS);
				
				// Add name elements
				binder.putLocal(Person.Cols.FIRST_NAME, "Individual");
				binder.putLocal(Person.Cols.LAST_NAME, 
				 "Donor (" + (numAnonDonors+1) + ")");
				binder.putLocal(Person.Cols.SALUTATION, "Anonymous " +
				 "Donor (" + (numAnonDonors+1) + ")");
				
				// Add 'User' Source ID
				CCLAUtils.addQueryIntParamToBinder(binder, DataSource.Cols.ID, 
				 DataSource.Ids.USER);
				
				Person person = Person.add(binder, facade, m_userData.m_name);
				
				Log.debug("Created new Anonymous Person Donor: " 
				 + person.getFullName());
				
				relElementId1 = recipientOrgId;
				relElementId2 = person.getPersonId();
				
			} else if (elementType.equals(ElementType.ORGANISATION)) {
				// Need to generate a new Anonymous Org Donor.
				
				// First figure out how many there are already, so we can suffix the
				// Org Name with a number
				relName = RelationName.getCache().getCachedInstance
				 (RelationName.OrganisationOrganisationRelation.DONOR);
				
				Vector<Integer> relNameIds = new Vector<Integer>();
				relNameIds.add(relName.getRelationNameId());
				
				Vector<Entity> donorOrgs =
				 Relation.getRelatedOrgs(null, recipientOrgId, relNameIds, facade);
				
				int numAnonDonors = 0;
				
				for (Entity donorOrg : donorOrgs) {
					if (donorOrg.getCategoryId() != null
						&& donorOrg.getCategoryId().equals
						(OrganisationCategory.CategoryIds.ANONYMOUS_DONOR)) {
						numAnonDonors++;
					}
				}
				
				Log.debug("Found " + numAnonDonors + " existing Anonymous Org Donors");
				
				DataBinder binder = new DataBinder();
				
				// Add DONO Account Code prefix
				binder.putLocal(Entity.ORG_ACCOUNT_CODE_PREFIX, 
				 Contribution.DonorAccountCodePrefixes.ORG);
				
				// Add Client Number suppression
				CCLAUtils.addQueryBooleanParamToBinder
				 (binder, Entity.SKIP_AURORA_CLIENT_NUMBER_GENERATION, true);
				
				// Add name
				String orgName = "Anonymous Organisation Donor (" 
				 + (numAnonDonors+1) + ")";
				binder.putLocal(Entity.Cols.ORGANISATION_NAME, orgName);
				
				// Add Org Category
				CCLAUtils.addQueryIntParamToBinder(binder, Entity.Cols.CATEGORY_ID,
				 OrganisationCategory.CategoryIds.ANONYMOUS_DONOR);
				
				// Add 'User' Source ID
				CCLAUtils.addQueryIntParamToBinder(binder, DataSource.Cols.ID, 
				 DataSource.Ids.USER);
				
				Entity org = Entity.add(binder, facade, m_userData.m_name);
				
				Log.debug("Created new Anonymous Org Donor: " + orgName);
				
				relElementId1 = org.getOrganisationId();
				relElementId2 = recipientOrgId;
				
			} else {
				throw new DataException("can't create Donor with Element Type: " +
				 elementType.getName());
			}
			
			// Now create the relationship between the new Element and the Organisation
			// they are donating to.
			Relation.add(relElementId1, relElementId2, relName, 
			 facade, m_userData.m_name);
			
			facade.commitTransaction();
			
		} catch (Exception e) {
			facade.rollbackTransaction();
			
			throw new ServiceException("Unable to create anonymous donor: " 
			 + e.getMessage(), e);
		}
	}
	
	/** 
	 *  @deprecated Contribution Fund Allocations can't be updated in bulk any more - users
	 *  must specify how each Contribution is allocated manually.
	 * 
	 *  Batch-updates all Contribution Fund Allocations for a given Subscription.
	 *  
	 *  The chosen allocation amounts must be distributed evenly against all 
	 *  Contributions.
	 *  
	 * @throws DataException 
	 * @throws ServiceException 
	 */
	public void updateSubscriptionFundAllocations() 
	 throws DataException, ServiceException {
	
		FWFacade facade = CCLAUtils.getFacade(m_workspace,true);
		
		try {
			Integer subscriptionId = CCLAUtils.getBinderIntegerValue
			 (m_binder, Subscription.Cols.ID);
			
			if (subscriptionId == null)
				throw new DataException("Subscription ID missing");
			
			Log.debug("Updating fund allocations for Subscription ID " + subscriptionId);
			
			// Check this is a valid Subscription ID before continuing
			Subscription subscription = Subscription.get(subscriptionId, facade);
			
			if (subscription == null)
				throw new DataException("Subscription ID " + subscriptionId +
				 " is invalid");
		
			// Ensure Subscription Amount is up-to-date before continuing.
			BigDecimal totalContributionAmount = Subscription.updateSubscriptionAmount
			 (subscription, facade, m_userData.m_name);
			
			// Fetch existing Subscription-level allocations (if any)
			//Vector<SubscriptionAssetAllocation> allocs =
			// SubscriptionAssetAllocation.getAllBySubscriptionId(subscriptionId, facade);
			
			// Fetch all Funds that can be allocated to
			Vector<Fund> availableFunds = CommunityFirstClientEnrolmentHandler
			 .getAllFundsEligibleForCommunityFirst(facade);
			
			// First fetch the latest allocations submitted by the user. Before
			// persisting any changes to the DB, we need to validate all the allocations
			// as a group.
			Vector<SubscriptionAssetAllocation> testAllocs = 
			 new Vector<SubscriptionAssetAllocation>();
		
			for (Fund fund : availableFunds) {
				// Check the binder for a mapped amount for this Fund.
				BigDecimal fundAmount = CCLAUtils.getBinderBigDecimalValue
				 (m_binder, SubscriptionAssetAllocation.Cols.ASSET_AMOUNT
				 + "_" + fund.getFundCode());
				
				// Check the binder for a mapped percentage for this Fund.
				BigDecimal fundPercent = CCLAUtils.getBinderBigDecimalValue
				 (m_binder, SubscriptionAssetAllocation.Cols.ASSET_PERCENT
				 + "_" + fund.getFundCode());
				
				// Treat a zero-valued Fund Allocation amount to be the same as null
				if (fundAmount != null && fundAmount.compareTo(BigDecimal.ZERO) == 0)
					fundAmount = null;
				
				// Calculate amount/percent, whichever one is missing.
				AmountPercentPair amountPercent = new AmountPercentPair
				 (fundAmount, fundPercent);
				
				amountPercent = NumberUtils.computePercentageOrAmount
				 (amountPercent, subscription.getSubscriptionAmount(), 
				 ContributionAssetAllocation.CONTRIBUTION_PERCENT_PRECISION);
				
				fundAmount = amountPercent.getAmount();
				fundPercent = amountPercent.getPercent();
				
				if (fundAmount != null) {
					// Add the allocation 
					SubscriptionAssetAllocation subscriptionAllocs = 
					 new SubscriptionAssetAllocation(
						subscription.getId(),
						fund,
						null,
						fundAmount,
						fundPercent
					);
					
					testAllocs.add(subscriptionAllocs);
				}
			}
			
			if (!testAllocs.isEmpty()
				&&
				(totalContributionAmount == null 
				|| totalContributionAmount.compareTo(BigDecimal.ZERO) == 0)) {
				// At least one non-zero Fund Allocation has been set, but there are
				// no contribution amounts set. Bail out as we can't divide up the
				// Fund allocations across the contributions.
				throw new ServiceException("Unable to update Fund allocations: ensure "
				 + " at least 1 contribution has an amount set");
			}
			
			// Now validate all the allocations we just fetched before continuing.
			// Do a non-strict validation here.
			SubscriptionAssetAllocation.validate(subscription, testAllocs, false);
			
			// If we got this far, the subscription/allocation amounts have been 
			// validated successfully. Now apply changes to the database.
			Vector<Contribution> contributions = Contribution.getAllBySubscriptionId
			 (subscriptionId, facade);
			
			facade.beginTransaction();
			
			ContributionAssetAllocation.updateContributionFundAllocations
			 (subscriptionId, testAllocs, contributions, availableFunds, 
			 facade, m_userData.m_name);
			
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
	
	/** Adds a ResultSet of linked Subscriptions for the ACCOUNT_ID value in the binder.
	 * 
	 * @throws DataException
	 */
	public void getSubscriptionsByAccountId() throws DataException {
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace,true);
		
		int accountId = CCLAUtils.getBinderIntegerValue(m_binder, Account.Cols.ID);
		
		m_binder.addResultSet("rsSubscriptions",
		 Subscription.getAllDataByAccountId(accountId, facade));
	}
	
	public void getSubscriptionInfo() throws DataException, ServiceException {		
		Integer subscriptionId = CCLAUtils.getBinderIntegerValue
		 (m_binder, Subscription.Cols.ID);
	
		FWFacade facade = CCLAUtils.getFacade(m_workspace,true);
		
		if (subscriptionId == null)
			throw new DataException("Subscription ID not found");
		
		addSubscriptionAndContributionDetailsToBinder
		 (subscriptionId, m_binder, facade);
	}
	
	/** Adds investment details to the binder, for a given Subscription ID.
	 *  
	 *  Includes the actual Subscription data, Subscription Fund Allocations and 
	 *  Contributions.
	 *  
	 * @param subscriptionId
	 * @param binder
	 * @param facade
	 * @throws DataException 
	 * @throws ServiceException 
	 * @throws ServiceException 
	 */
	public static void addSubscriptionAndContributionDetailsToBinder
	 (int subscriptionId, DataBinder binder, FWFacade facade) 
	 throws DataException, ServiceException {
		
		// Add Subscription data
		DataResultSet rsSubscription = Subscription.getData(subscriptionId, facade);
		binder.addResultSet("rsSubscription", rsSubscription);
		
		if (rsSubscription.isEmpty())
			throw new DataException("Subscription data for ID " + subscriptionId + 
			 " not found");
		
		Subscription subscription = Subscription.get(rsSubscription);
		
		Integer productId = subscription.getProductId();
		
		if (productId == null) {
			throw new DataException("Unable to load Subscription info: " +
			 "no Product ID set");
		}
		
		// Add Account data linked to Subscription record
		binder.addResultSet("rsAccount", Account.getData
		 (subscription.getAccountId(), facade));

		Integer orgId = Account.getOwnerOrganisationId
		 (subscription.getAccountId(), facade);
		
		// Add the above account's owning Organisation data
		OrganisationDataConfig orgDataConfig = new OrganisationDataConfig
		 (true, false, true, false, false, false, false, false, 
		 true, false, true, false);
		
		EntityService.addEntityDataToBinder
		 (binder, orgId, facade, orgDataConfig, false);
		
		//binder.addResultSet("rsOrganisation", Entity.getData(orgId, facade));
		
		// Add Fund Allocations data
		DataResultSet rsSubscriptionFundAllocations = 
		 SubscriptionAssetAllocation.getAllDataBySubscriptionId(subscriptionId, facade);
		
		binder.addResultSet
		 ("rsSubscriptionFundAllocations", rsSubscriptionFundAllocations);
		
		// Add details for the latest generated form, if there is one.
		DataResultSet rsForm = Form.getLatestFormDataBySubscriptionId(subscriptionId, facade);
		binder.addResultSet("rsForm", rsForm);
		
		// Add available Funds for investment allocation
		DataResultSet rsFunds = ApplicableProductAssetInvestment
		 .getApplicableFundsForInvestmentByProductData(productId, facade);
		
		// Apply custom re-ordering for funds list.
		rsFunds = Subscription.getSortedFundsData(productId, rsFunds);
		binder.addResultSet("rsFunds", rsFunds);
		
		// Add Contributions data
		DataResultSet rsContributions =
		 Contribution.getAllDataBySubscriptionIdExtended(subscriptionId, facade);
		binder.addResultSet("rsContributions", rsContributions);
		
		// Add Contribution Types list for the passed Product ID
		DataResultSet rsContributionTypes = 
		 ContributionType.getAllDataByProduct(productId, facade);
		
		binder.addResultSet("rsContributionTypes", rsContributionTypes);
		
		// Add available Donors data
		ContributionService.addAvailableDonorsByOrganisationIdDataToBinder
		 (orgId, binder, facade);
	}
	
	/** Adds ResultSets to the binder used for displaying Donors and their
	 *  Contributions, for a given Subscription ID.
	 *  
	 * @throws DataException
	 * @throws ServiceException 
	 */
	public void getDonorsAndContributionsInfo() throws DataException, ServiceException {
		
		Integer subscriptionId = CCLAUtils.getBinderIntegerValue
		 (m_binder, Subscription.Cols.ID);
		
		if (subscriptionId == null)
			throw new ServiceException("Subscription ID missing");
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace,true);
		
		Subscription subscription = Subscription.get(subscriptionId, facade);
		
		if (subscription == null)
			throw new DataException("Subscription data for ID " + subscriptionId + 
			 " not found");
		
		// Add Contributions data. Includes some aggregate columns for their TTLA
		// allocations.
		DataResultSet rsContributions =
		 Contribution.getAllDataBySubscriptionIdExtended(subscriptionId, facade);
		m_binder.addResultSet("rsContributions", rsContributions);
		
		Integer orgId = Account.getOwnerOrganisationId
		 (subscription.getAccountId(), facade);
		
		// Add available Donors data
		ContributionService.addAvailableDonorsByOrganisationIdDataToBinder
		 (orgId, m_binder, facade);
	}
	
	/**
	 * @deprecated Replace with Instruction creation instead.
	 * 
	 * Generates a set of Child Documents, using the returned form Document as their
	 * parent.
	 * 
	 * The generated Child Documents are a group of deposit/transfer instructions which
	 * match the Fund investment allocations for the given Subscription.
	 * 
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void generateChildDocuments() throws DataException, ServiceException {
		
		String userName = m_userData.m_name;
		//ChildDocument DocumentClass to use.
		int childTransferDocType = SharedObjects.getEnvironmentInt
		 ("CF_Child_Transfer_DocType", InstructionType.Ids.BUYDF);
		int childDepositDocType = SharedObjects.getEnvironmentInt
		 ("CF_Child_Deposit_DocType", InstructionType.Ids.DEPBNK);
		
		String childTransferDocTypeName = InstructionType.getIdCache().getCachedInstance
		 (childTransferDocType).getName();
		String childDepositDocTypeName = InstructionType.getIdCache().getCachedInstance
		 (childDepositDocType).getName();
		
		FWFacade ucmFacade = CCLAUtils.getFacade(m_workspace);
		FWFacade cdbFacade = CCLAUtils.getFacade(m_workspace, true);
		
		LWDocument parentDoc = null;
		String bundleRef = null;
		String parentDocname = null;
		
		Integer subscriptionId = CCLAUtils.getBinderIntegerValue
		 (m_binder, Subscription.Cols.ID);

		if (subscriptionId==null)
			throw new DataException(Subscription.Cols.ID + " missing");
		
		Subscription subscription = Subscription.get(subscriptionId, cdbFacade);
		
		if (subscription==null)
			throw new DataException("Subscription ID " + subscriptionId + " not found");

		// Fetch the Form record associated with the Subscription ID which has a 
		// returned doc reference against it.
		Form form = 
			Form.getFormByTypeAndSubscriptionWithReturnedDoc(
					FormType.COMMUNITY_FIRST_DONOR_SUBSCRIPTION, 
					subscriptionId, 
					cdbFacade);
		
		if (form==null) {
			throw new DataException("Subscription Form does not appear to have been " +
			 "returned yet");
		}

		String retDocGuid = form.getRetDocGuid();

		try {
			// Fetch LWDocument instance linked to the Form ID
			parentDoc = CCLAUtils.getLatestLwdFromDocGuid(retDocGuid);
			
			if (parentDoc==null) {
				throw new DataException("Unable to find Subscription Form document " +
				 "with GUID: " + retDocGuid);
			}
			
			bundleRef = parentDoc.getAttribute(Globals.UCMFieldNames.BundleRef);
			parentDocname = parentDoc.getAttribute(Globals.UCMFieldNames.DocName);
			
		} catch (Exception e) {
			throw new DataException(e.getMessage(), e);
		}
		
		if (StringUtils.stringIsBlank(bundleRef) 
			|| StringUtils.stringIsBlank(parentDocname)) {
			throw new DataException("Unable to generate transfer instructions: " +
			 Globals.UCMFieldNames.BundleRef + " or " +
			 Globals.UCMFieldNames.DocName + " not found for document GUID: " + 
			 retDocGuid);
		}

		//Need to check if the signatures are valid.
		Account subAcc = Account.get(subscription.getAccountId(), cdbFacade);
	
		if (!checkSignaturesValid(subAcc, retDocGuid, cdbFacade)) {
			String errMsg = "Signature check has not been completed against the " +
			 "returned Subscription form.";
			Log.error(errMsg);
			throw new DataException(errMsg);
		}
		
		Vector<LWDocument> existingDocsVec = new Vector<LWDocument>();
		DataResultSet rsChildDocs = new DataResultSet(new String[] {
			"CLASS", "ACCOUNT", "AMOUNT", "CLIENTNO",
			"FUND", "UNITS", "DESTACCNUMEXT", "DESTFUND"
		});
		
		//Check donations and TTLA allocation.
		Vector<Contribution> contributions = Contribution.getAllBySubscriptionId
		 (subscriptionId, cdbFacade);
	
		if (contributions.isEmpty()) {
			String msg = 
				 "No contributions specified. There must be at least one specified " +
				 "before continuing";
				
				Log.error(msg);
				throw new DataException(msg);
		}
		
		// Check each Contribution instance is valid.
		for (Contribution contrib: contributions) {

			BigDecimal amount = contrib.getAmount();
			
			if ((amount==null || amount.compareTo(BigDecimal.ZERO)==0)) {
				String msg = 
				 "One or more of the contributions has an empty/zero Contribution " +
				 "Amount. Ensure these have been completed before continuing";
				
				Log.error(msg);
				throw new DataException(msg);
			}
			
			// Validate the set of TTLA allocations for this Contribution, in strict
			// mode. This ensures each Contribution has 100% TTLA allocation.
			Vector<ContributionTTLAAllocation> ttlaAllocs = ContributionTTLAAllocation
			 .getAllByContributionId(contrib.getId(), cdbFacade);
			
			ContributionTTLAAllocation.validate(contrib, ttlaAllocs, true);	
		}			
		
		//Check subscription fund allocation.
		Vector<SubscriptionAssetAllocation> fundAllocationVec = 
			SubscriptionAssetAllocation.getAllBySubscriptionId(subscriptionId, cdbFacade);
		
		SubscriptionAssetAllocation.validate(subscription, fundAllocationVec, true);
		
		//Need to always added a deposit subscription for childdoc generation
		boolean containDeposit = false;
		for (SubscriptionAssetAllocation fundAlloc: fundAllocationVec) {
			if (fundAlloc.getFund().getFundCode().equals(SubscriptionUtils.CF_DEPOSIT_FUND)) {
				fundAlloc.setAmount(subscription.getSubscriptionAmount());
				containDeposit = true;
			}
		}
		
		//manually add deposit subscription if it doesn't exist
		if (!containDeposit) {
			SubscriptionAssetAllocation fundAlloc = new SubscriptionAssetAllocation(
				subscription.getId(), 
				Fund.getCache().getCachedInstance(SubscriptionUtils.CF_DEPOSIT_FUND), 
				null,
				subscription.getSubscriptionAmount(),
				NumberUtils.ONE_HUNDRED
			);
			
			fundAllocationVec.add(fundAlloc);
		}
		
		// Determine contribution type
		Integer contributionTypeId = contributions.get(0).getContributionTypeId();
		
		//Get the CDF Deposit Fund Account
		String accountType = CommunityFirstClientEnrolmentHandler.getAccountType
		 (contributionTypeId);
		Log.debug("Got CDF Deposit Account Type :"+accountType+
		 " using SubscriptionTypeId :"+contributionTypeId);
		
		if (StringUtils.stringIsBlank(accountType))
			throw new DataException("Subscription account type not known: "
			 +contributionTypeId);
		
		Account cdfDepositAcc = Account.getCFAccount
		 (SubscriptionUtils.CF_DEPOSIT_FUND, accountType, cdbFacade);
		
		if (cdfDepositAcc==null) {
			String msg = "Cannot find CDF Deposit account with fund:"+
			SubscriptionUtils.CF_DEPOSIT_FUND+", type:"+accountType;
			
			Log.error(msg);
			throw new DataException(msg);						
		}
		
		Integer principalOrgId = cdfDepositAcc.getOwnerOrganisationId(cdbFacade);
		
		if (principalOrgId==null) {
			String msg = "No owner organisation for CDF Deposit Acc :"
			 +cdfDepositAcc.getAccountId();
			Log.error(msg);
			throw new DataException(msg);
		}
		
		// Assume always COIF for now(!)
		AuroraClient principalAuroraClient = Entity.getAuroraClientCompanyMapping
		 (principalOrgId, Company.getCache().getCachedInstance(Company.COIF), cdbFacade);
		
		if (principalAuroraClient==null) {
			String msg = "No AuroraClient for orgId :"+principalOrgId;
			Log.error(msg);
			throw new DataException(msg);			
		}
		
		//String childDocTypeName = childDepositDocTypeName;
		
		//Generic child document binder. Only thing that changes is the fund and account data.
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryParamToBinder(binder, Globals.UCMFieldNames.ParentDocName, parentDocname);
		CCLAUtils.addQueryParamToBinder(binder, Globals.UCMFieldNames.BundleRef, bundleRef);
		CCLAUtils.addQueryParamToBinder(binder, Globals.UCMFieldNames.Fund, cdfDepositAcc.getFundCode());
		//CCLAUtils.addQueryParamToBinder(binder, Globals.UCMFieldNames.ClientNumber, principalAuroraClient.getPaddedClientNumber());
		//CCLAUtils.addQueryParamToBinder(binder, Globals.UCMFieldNames.AccountNumber, cdfDepositAcc.getAccountNumberString());		
		
		//Loop through all fund allocations and create transfer childDocuments.
//		List<String> fundOrderList = 
//			SharedObjects.getEnvValueAsList("CF_FUND_INVESTMENT_ALLOCATION_ORDER");
//		
		
		for (SubscriptionAssetAllocation fundAllocation: fundAllocationVec) 
		{
			BigDecimal fundAmount = fundAllocation.getAmount();
			String fund = fundAllocation.getFund().getFundCode();
			
			boolean isDepositFund = SubscriptionUtils.CF_DEPOSIT_FUND.equals(fund);
			
			boolean foundExisting = false;			
			Log.debug("---- Subscription Allocations: fund="+(fund!=null?fund:"N/A")+
					", Amount="+(fundAmount!=null?fundAmount.toPlainString():"N/A"));
			
			if (!StringUtils.stringIsBlank(fund) && fundAmount.compareTo(BigDecimal.ZERO)!=0) {				
				Account acc = Account.getCFAccount(fund, accountType, cdbFacade);
				
				if (acc==null) {
					Log.error("Cannot find CF account with fund:"+fund+", type:"+accountType);
					throw new DataException("Cannot find CF account with fund:"+fund+", type:"+accountType);						
				}
				
				//Default query
				String query = "qClientServices_GetCFPrincipalFundChildDocuments";
				
				//Append data to binder
				if (!isDepositFund) {
					//Set default docType
					CCLAUtils.addQueryParamToBinder(binder, Globals.UCMFieldNames.DocClass, childTransferDocTypeName);
					CCLAUtils.addQueryParamToBinder(binder, Globals.UCMFieldNames.DestinationFund, acc.getFundCode());
					CCLAUtils.addQueryParamToBinder(binder, Globals.UCMFieldNames.DestinationAccount, acc.getAccNumExt());		
					query = "qClientServices_GetCFDestinationFundChildDocuments";
				} else {
					CCLAUtils.addQueryParamToBinder(binder, Globals.UCMFieldNames.DocClass, childDepositDocTypeName);		
				}
				
				//check if exist
				DataResultSet rsExistingEntries = 
					ucmFacade.createResultSet(query, binder);
				
				//Possible existing childDocument found. Check the amount to see if it is the same.
				if (rsExistingEntries!=null && !rsExistingEntries.isEmpty()) {
					foundExisting = true;

					//do update if there is only a single entry.
					//if amounts are different, then do the update otherwise ignore.
					BigDecimal existingAmount = CCLAUtils.getResultSetBigDecimalValue(rsExistingEntries, Globals.UCMFieldNames.Amount); 
					String existingDocname = CCLAUtils.getResultSetStringValue(rsExistingEntries, Globals.UCMFieldNames.DocName);
					String existingAccountNumber = CCLAUtils.getResultSetStringValue(rsExistingEntries, Globals.UCMFieldNames.AccountNumber);
					String existingClientNumber = CCLAUtils.getResultSetStringValue(rsExistingEntries, Globals.UCMFieldNames.ClientNumber);
					
					if (existingAmount.compareTo(fundAmount)!=0 || 
							!existingAccountNumber.equals(cdfDepositAcc.getAccountNumberString()) ||
							!existingClientNumber.equals(principalAuroraClient.getPaddedClientNumber()))
					{
						try {
							LWDocument existingDoc = new LWDocument(existingDocname, true);
							existingDoc.setAttribute(Globals.UCMFieldNames.Amount, fundAmount.toString());
							existingDoc.setAttribute(Globals.UCMFieldNames.AccountNumber, cdfDepositAcc.getAccountNumberString());
							existingDoc.setAttribute(Globals.UCMFieldNames.ClientNumber, principalAuroraClient.getPaddedClientNumber());

							if (!isDepositFund) {
								existingDoc.setAttribute(Globals.UCMFieldNames.DestinationFund, acc.getFundCode());
								existingDoc.setAttribute(Globals.UCMFieldNames.DestinationAccount, acc.getAccNumExt());	
							}
							existingDocsVec.add(existingDoc);

							Log.debug("Found existing ChildDoc with docname:"+existingDocname+
									", Updating parentDocname:"+parentDocname+
									", docClass:"+CCLAUtils.getBinderStringValue(binder, Globals.UCMFieldNames.DocClass)+
									", clientNo:"+principalAuroraClient.getPaddedClientNumber()+
									", accountNo:"+cdfDepositAcc.getAccountNumberString()+
									", fund:"+fund+
									", amount:"+fundAmount.toString()+
									", destAccNo:"+(StringUtils.stringIsBlank(acc.getAccNumExt())?"":acc.getAccNumExt())+
									", destFund:"+(StringUtils.stringIsBlank(acc.getFundCode())?"":acc.getFundCode()));
						} catch (Exception e) {
							Log.error(e.getMessage(), e);
							throw new DataException(e.getMessage(), e);
						}
					} else {
						Log.debug("Found existing ChildDoc with docname:"+existingDocname+
								", but not updating as it is the same!");
					}
				} 
				
				//Cannot find an existing child doc, creating a new one.
				if (!foundExisting) {
					//int orgId = acc.getOwnerOrganisationId(cdbFacade);
					//String clientNo = Entity.getAuroraClient(orgId, cdbFacade).getPaddedClientNumber();
					
					//create new entry
					//"CLASS", "ACCOUNT", "AMOUNT", "CLIENTNO",
					//"FUND", "UNITS", "DESTACCNUMEXT", "DESTFUND"
					Vector<String> newEntry = new Vector<String>();
					newEntry.add(CCLAUtils.getBinderStringValue(binder, Globals.UCMFieldNames.DocClass)); //CLASS
					newEntry.add(cdfDepositAcc.getAccountNumberString()); //ACCOUNT
					newEntry.add(fundAmount.toString()); //AMOUNT						
					newEntry.add(principalAuroraClient.getPaddedClientNumber()); //CLIENTNO
					newEntry.add(fund); //FUND
					newEntry.add(""); //UNITS
					newEntry.add(acc.getAccNumExt()); //DESTACCNUMEXT
					newEntry.add(acc.getFundCode()); //DESTFUND
					rsChildDocs.addRow(newEntry);
					
					Log.debug("Creating New ChildDoc with following instructions:- "+
							"parentDocname:"+parentDocname+
							", docClass:"+CCLAUtils.getBinderStringValue(binder, Globals.UCMFieldNames.DocClass)+
							", clientNo:"+principalAuroraClient.getPaddedClientNumber()+
							", accountNo:"+cdfDepositAcc.getAccountNumberString()+
							", fund:"+fund+
							", amount:"+fundAmount.toString()+
							", destAccNo:"+(StringUtils.stringIsBlank(acc.getAccNumExt())?"":acc.getAccNumExt())+
							", destFund:"+(StringUtils.stringIsBlank(acc.getFundCode())?"":acc.getFundCode()));
				}
			}
		}
		
		//Clean up any left over instructions.
		DataResultSet rsCurrChildDocs = ucmFacade.createResultSet("qClientServices_GetCFChildDocuments", binder);
		Vector<String> expiredDocnameVec = new Vector<String>();
		
		if (rsCurrChildDocs!=null && !rsCurrChildDocs.isEmpty()) 
		{
			do {
				boolean isExpired = true;
				String currFund = CCLAUtils.getResultSetStringValue(rsCurrChildDocs, Globals.UCMFieldNames.Fund);
				String currDestFund = CCLAUtils.getResultSetStringValue(rsCurrChildDocs, Globals.UCMFieldNames.DestinationFund);
				String currDocClass = CCLAUtils.getResultSetStringValue(rsCurrChildDocs, Globals.UCMFieldNames.DocClass);
				String currDocname = CCLAUtils.getResultSetStringValue(rsCurrChildDocs, Globals.UCMFieldNames.DocName);
				
				for (SubscriptionAssetAllocation fundAllocation: fundAllocationVec) {
					if (currDocClass.equals(childTransferDocTypeName)) {
						if (fundAllocation.getFund().getFundCode().equals(currDestFund))
							isExpired = false;
					} else if (currDocClass.equals(childDepositDocTypeName)) {
						if (fundAllocation.getFund().getFundCode().equals(currFund))					
							isExpired = false;
					}
				}
				
				if (isExpired) {
					Log.debug("Found expired ChildDoc: DocName:"+currDocname+
							", DocClass:"+currDocClass+
							", Fund:"+currFund+
							", DestFund:"+(StringUtils.stringIsBlank(currDestFund)?"N/A":currDestFund));
					expiredDocnameVec.add(currDocname);
				}
			} while (rsCurrChildDocs.next());
		}
		//update all changes
		try {
			
			//Update all existing documents that needs changing.
			if (!existingDocsVec.isEmpty()) {
				for (LWDocument lwDoc:existingDocsVec) {
					lwDoc.update();
				}
			}
			
			//Check in any newly created child documents.
			if (!rsChildDocs.isEmpty()) {
				int newCount = InstructionDocServices.checkinChildDocs(parentDocname, rsChildDocs, m_workspace, m_userData.m_name);
				CCLAUtils.addQueryIntParamToBinder(m_binder, "newlyCreated", newCount);
			}
			
			//expire any child documents that is not in used.
			if (!expiredDocnameVec.isEmpty()) {
				for (String expiredDocname: expiredDocnameVec) {
					LWDocument lwDoc = new LWDocument(expiredDocname, true);
					lwDoc.setAttribute("dOutDate",DateFormatter.getTimeStamp());
					Log.debug("+Attempting to update dOutDate on :" + expiredDocname + "...");
					lwDoc.update();
				}
			}
			
			//Finally update all signatures			
			//Current signatures for returned document.
			Vector<InstructionSignature> parentSigVec = InstructionSignature.getAllByDocGuid(retDocGuid, cdbFacade);
			
			rsCurrChildDocs = ucmFacade.createResultSet("qClientServices_GetCFChildDocuments", binder);
			if (rsCurrChildDocs!=null && !rsCurrChildDocs.isEmpty()) {
				do {
					//String docName = CCLAUtils.getResultSetStringValue(rsCurrChildDocs, Globals.UCMFieldNames.DocName);
					int docId = CCLAUtils.getResultSetIntegerValue(rsCurrChildDocs, Globals.UCMFieldNames.DocID);
					
					LWDocument lwDoc = new LWDocument(docId, true);
					String docGuid = CCLAUtils.getDocGuidFromLwd(lwDoc);
					Vector<InstructionSignature> childSigVec = InstructionSignature.getAllByDocGuid(docGuid, cdbFacade);
					
					addUpdateOrRemoveSignatures(docGuid, docId, childSigVec, parentSigVec, userName, cdbFacade);
					
					//finally update lwd
					lwDoc.setAttribute(UCMFieldNames.SignaturesValid, "1");
					lwDoc.update();
					
				} while (rsCurrChildDocs.next());
			
			}
			
		} catch (Exception e) {
			Log.error(e.getMessage(), e);
			throw new DataException(e.getMessage(), e);
		}
		
		// Append form ID to RedirectUrl
		String redirectUrl = m_binder.getLocal("RedirectUrl");		
		if (!StringUtils.stringIsBlank(redirectUrl)) 
		{
			if (form != null)
				redirectUrl += "&generatedChildDoc=1";
			m_binder.putLocal("RedirectUrl", redirectUrl);
		}
	}

	
	/**
	 * Generates a Donor Subscription form for the Subscription.
	 *  
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void generateDonorSubscriptionForm() throws DataException, ServiceException 
	{
		boolean useLegacyForm = CCLAUtils.getBinderBoolValue(m_binder, "legacyMode");
		
		Integer formTypeId = useLegacyForm ? 
		 FormType.COMMUNITY_FIRST_DONOR_TTLA_SUBSCRIPTION
		 :
		 FormType.COMMUNITY_FIRST_DONOR_SUBSCRIPTION;
		
		Log.debug("Generating " + (useLegacyForm ? "Legacy" : "") 
		 + " Donor Subscription Form");
		
		FormType formType = FormType.getCache().getCachedInstance(formTypeId);
		
		if (formType==null)
			throw new DataException
			 ("Cannot generate Donor Subscription Form, Cannot find FormType with id:"
			 +formTypeId);
	
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		FormHandler formHandler = formType.getHandlerInstance(m_userData.m_name, facade);
		
		if (formHandler==null) 
			throw new DataException("Cannot generate Donor Subscription Form: " +
			 "cannot get Form Handler Instance for formTypeId:"+formTypeId);
		
		Integer subscriptionId = CCLAUtils.getBinderIntegerValue
		 (m_binder, Subscription.Cols.ID);
		
		if (subscriptionId==null) 
			throw new DataException("Cannot generate Donor Subscription Form: " +
			 Subscription.Cols.ID + " is null");

		Subscription subscription = Subscription.get(subscriptionId, facade);
		
		if (subscription==null)
			throw new DataException("Cannot generate Donor Subscription Form: " + 
			 "cannot find Subscription with id: "+subscriptionId);
		
		Integer orgId = Account.getOwnerOrganisationId
		 (subscription.getAccountId(), facade);
		
		if (orgId==null)
			throw new DataException("Cannot generate Donor Subscription Form: " +
			 "cannot find owner Organisation with AccId: "+subscription.getAccountId());
		
		Entity org = Entity.get(orgId, facade);
		
		if (org==null)
			throw new DataException("Cannot generate Donor Subscription Form: " +
			 "cannot find Organisation with id: "+orgId);
		
		try {
			facade.beginTransaction();
			
			Form form = formHandler.generateForm(formType, org, subscriptionId);
			
			facade.commitTransaction();
			
		} catch (Exception e) {
			facade.rollbackTransaction();
			
			String msg = "Failed to generate Donor Subscription form: " 
			 + e.getMessage();
		
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}
	
	/**
	 * Utility method for checking
	 * @param account
	 * @param guid
	 * @return
	 * @throws ValidationException
	 */
	private static boolean checkSignaturesValid(Account account, String docGuid, FWFacade facade) 
	throws DataException {
		Integer reqSigs = account.getRequiredSignatures();
		
		DataResultSet rsData = 
			InstructionSignature.getAllDataByDocGuid(docGuid, facade);
		
		Integer dataRows = ((rsData!=null && !rsData.isEmpty())?rsData.getNumRows():null);
		
		if (reqSigs!=null && dataRows!=null) {
			if (dataRows.intValue()>=reqSigs.intValue())
				return true; 
			else
				return false;
	    } else if (reqSigs==null && dataRows!=null) {
	    	return true;
	    } else if (reqSigs!=null && dataRows==null) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Add/Update or Remove Signatures for a document based on the parent document signatures.
	 * Ensures that all the document signatures are the same as the parent document signature
	 * @param docGuid
	 * @param docId 
	 * @param rsDocumentSigs
	 * @param rsParentSigs
	 * @param userName
	 * @param facade
	 * @throws DataException
	 */
	private static void addUpdateOrRemoveSignatures(String docGuid, int docId,  
			Vector<InstructionSignature> rsDocumentSigs, Vector<InstructionSignature> rsParentSigs, String userName, FWFacade facade) throws DataException {
		
		if ((rsDocumentSigs==null || rsDocumentSigs.isEmpty()) && 
				(rsParentSigs==null || rsParentSigs.isEmpty())) {
			//All empty, no need to do anything.
			return;
		} else if ((rsDocumentSigs==null || rsDocumentSigs.isEmpty()) && 
				(rsParentSigs!=null && !rsParentSigs.isEmpty())) {
			//Current Document Signature is empty, add all parent signatures.
			for (InstructionSignature sig: rsParentSigs) {
				InstructionSignature.addByDocGuid(docGuid, sig.getPersonId(), facade, userName);
			}			
		} else if ((rsDocumentSigs!=null && !rsDocumentSigs.isEmpty()) &&
				(rsParentSigs==null || rsParentSigs.isEmpty())) {		
			//Parent Signatures are all empty, delete current document signatures
			for (InstructionSignature sig: rsDocumentSigs) {
				InstructionSignature.removeByDocGuid(sig.getDocGuid(), sig.getPersonId(), facade, userName);
			}
		} else {
			//Compare the Document Signature against the Parent and delete or add depending on differences.
			Vector<Integer> toAddVec = new Vector<Integer>();
			Vector<Integer> toDelVec = new Vector<Integer>();
			
			boolean containsParent = false;
			for (InstructionSignature docSig: rsDocumentSigs) 
			{
				containsParent = false;
				for (InstructionSignature parentSig: rsParentSigs) {
					if (parentSig.getPersonId() == docSig.getPersonId()) {
						containsParent = true;
					}
				}
				
				if (!containsParent) {
					toDelVec.add(docSig.getPersonId());
				}		
			}			
			
			//Now loop the otherway around and add
			boolean containsChild = false;
			for (InstructionSignature parentSig: rsParentSigs) 
			{
				containsChild = false;
				for (InstructionSignature docSig: rsDocumentSigs) {
					if (parentSig.getPersonId() == docSig.getPersonId()) {
						containsChild = true;
					}
				}
				
				if (!containsChild) {
					toAddVec.add(parentSig.getPersonId());
				}		
			}			
			
			if (!toAddVec.isEmpty()) {
				for (Integer personId: toAddVec) {
					InstructionSignature.addByDocGuid(docGuid, personId, facade, userName);
				}
			}
			
			if (!toDelVec.isEmpty()) {
				for (Integer personId: toDelVec) {
					InstructionSignature.removeByDocGuid(docGuid, personId, facade, userName);
				}
			}
		}
	}
	
	/**
	 * @UCM_Service CCLA_CS_SUBSCRIPTION_NEW
	 * @throws ServiceException
	 * @throws DataException
	 */
	public void newSubscription() throws ServiceException, DataException{
		
		//5:qCore_GetAllSubscriptionTypes:rsSubscriptionTypes::null
		//5:qCore_GetAllSubscriptionStatuses:rsSubscriptionStatuses::null
		//5:qCore_GetAllRetailPriceIndexes:rsRetailPriceIndexes::null
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		m_binder.addResultSet(
				"rsSubscriptionTypes", 
				facade.createResultSet("qCore_GetAllSubscriptionTypes", new DataBinder())
		);
		
		m_binder.addResultSet(
				"rsSubscriptionStatuses", 
				facade.createResultSet("qCore_GetAllSubscriptionStatuses", new DataBinder())
		);
		
		m_binder.addResultSet(
				"rsRetailPriceIndexes", 
				facade.createResultSet("qCore_GetAllRetailPriceIndexes", new DataBinder())
		);
	}
	
	/** Service method used to generate any missing Donor Accounts on demand, based on the 
	 *  investment preferences of the passed Subscription.
	 *  
	 *  Bear in mind these are created automatically upon generation of the Subscription
	 *  Form - this just provides a way for the user to pre-empt this process, or create
	 *  extra accounts if the donor changes their investment decisions on the returned
	 *  form.
	 *  
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void createDonorAccountsForSubscription() 
	 throws DataException, ServiceException {
		
		FWFacade facade = CCLAUtils.getFacade(true);
		
		Integer subscriptionId = CCLAUtils.getBinderIntegerValue
		 (m_binder, Subscription.Cols.ID);
		
		if (subscriptionId==null) 
			throw new DataException("Cannot generate Donor Accounts: " +
			 Subscription.Cols.ID + " is null");

		SubscriptionUtils.createDonorAccountsForSubscription
		 (subscriptionId, m_binder, m_userData.m_name, facade);
	}
	
	/** One-off function for creating accounts for all donor holdings. 
	 *  
	 *  The list of accounts to generate is sourced from a view called 
	 *  V_DONOR_ACCOUNT_TEMP, which is expected to contain the following columns:
	 *  
	 *  LCF_ORG_ID
	 *  DONOR_ID
	 *  FUND_CODE
	 * @throws DataException 
	 * @throws ServiceException 
	 *  
	 **/
	public void generateDonorAccounts() throws DataException, ServiceException {
		
		final String LOADER_TABLE_NAME = "V_DONOR_ACCOUNT_TEMP";
		
		LinkedHashMap<Entity, LinkedHashMap<Element, List<Fund>>> lcfDonorHoldings = 
		 new LinkedHashMap<Entity, LinkedHashMap<Element, List<Fund>>>();
		
		FWFacade facade = CCLAUtils.getFacade(true);
		boolean commit = CCLAUtils.getBinderBoolValue(m_binder, "commit");
		
		DataResultSet rs = facade.createResultSetSQL
		 ("SELECT * FROM " + LOADER_TABLE_NAME);
		
		if (rs.first()) {
			do {
				Integer lcfOrgId = CCLAUtils.getResultSetIntegerValue(rs, "LCF_ORG_ID");
				Integer donorId = CCLAUtils.getResultSetIntegerValue(rs, "DONOR_ID");
				String fundCode = rs.getStringValueByName(Fund.Cols.FUND_CODE);
				
				if (lcfOrgId == null || donorId == null || fundCode == null)
					throw new ServiceException("Row in loader table " 
					 + LOADER_TABLE_NAME + " had a missing value");
				
				// Resolve the LCF Org.
				Entity lcfOrg = Entity.get(lcfOrgId, facade);
				// Resolve the donor. Must be a Person or Organisation.
				Element donorElem = Element.get(donorId, facade);
				// Resolve the Fund.
				Fund fund = Fund.getCache().getCachedInstance(fundCode);
				
				// Fetch the existing list of donors for this LCF, if applicable.
				LinkedHashMap<Element, List<Fund>> lcfDonors = lcfDonorHoldings.get(lcfOrg);
				
				if (lcfDonors == null) {
					lcfDonors = new LinkedHashMap<Element, List<Fund>>();
					lcfDonorHoldings.put(lcfOrg, lcfDonors);
				}
				
				if (donorElem.getType().equals(ElementType.PERSON))
					donorElem = Person.get(donorId, facade);
				else if (donorElem.getType().equals(ElementType.ORGANISATION))
					donorElem = Entity.get(donorId, facade);
				else
					throw new ServiceException("Loader table contained an invalid " +
					 "Donor ID: " + donorId);
				
				List<Fund> donorHoldings = lcfDonors.get(donorElem);
				
				if (donorHoldings == null) {
					donorHoldings = new ArrayList<Fund>();
					lcfDonors.put(donorElem, donorHoldings);
				}
				
				if (donorHoldings.contains(fund)) {
					// Already a mapping for this LCF/Donor/Fund. Fail.
					throw new ServiceException("Loader table contained a duplicate " +
					 "mapping for LCF Org ID: " + lcfOrgId + ", Donor ID: " + donorId 
					 + ", Fund: " + fundCode);
				}
				
				donorHoldings.add(fund);
				
			} while (rs.next());
		}
		
		try {
			facade.beginTransaction();
			
			SubscriptionUtils.generateDonorAccounts
			 (lcfDonorHoldings, m_userData.m_name, facade);
			
			if (commit) {
				facade.commitTransaction();
				Log.debug("Changes committed");
			} else {
				Log.debug("Test Mode active - rolling back DB changes");
				facade.rollbackTransaction();
			}
			
		} catch (Exception e) {
			facade.rollbackTransaction();
			
			String msg = "Failed to generate Donor accounts: " + e.getMessage();
			
			Log.error(msg, e);
			throw new ServiceException(msg);
		}
	}
}
