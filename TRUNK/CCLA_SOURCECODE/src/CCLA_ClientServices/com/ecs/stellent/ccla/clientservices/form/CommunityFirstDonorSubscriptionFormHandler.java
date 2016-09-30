package com.ecs.stellent.ccla.clientservices.form;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import intradoc.common.ServiceException;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.shared.SharedObjects;

import com.ecs.stellent.ccla.clientservices.SubscriptionUtils;
import com.ecs.stellent.ccla.clientservices.campaign.CommunityFirstClientEnrolmentHandler;
import com.ecs.stellent.ccla.clientservices.spool.CommunityFirstDonorSubscriptionSpoolFileGenerator;
import com.ecs.stellent.ccla.clientservices.spool.CommunityFirstDonorTTLASubscriptionSpoolFileGenerator;
import com.ecs.stellent.spp.service.InstructionDocServices;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals;
import com.ecs.ucm.ccla.Globals.UCMFieldNames;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.Address;
import com.ecs.ucm.ccla.data.AuroraClient;
import com.ecs.ucm.ccla.data.BankAccount;
import com.ecs.ucm.ccla.data.Company;
import com.ecs.ucm.ccla.data.Contact;
import com.ecs.ucm.ccla.data.DocumentClass;
import com.ecs.ucm.ccla.data.Element;
import com.ecs.ucm.ccla.data.ElementAttributeApplied;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Fund;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.campaign.CampaignActivityType;
import com.ecs.ucm.ccla.data.campaign.CampaignEnrolment;
import com.ecs.ucm.ccla.data.form.Form;
import com.ecs.ucm.ccla.data.form.FormElementApplied;
import com.ecs.ucm.ccla.data.form.FormHandler;
import com.ecs.ucm.ccla.data.form.FormType;
import com.ecs.ucm.ccla.data.form.FormUtils;
import com.ecs.ucm.ccla.data.form.Form.FormStatus;
import com.ecs.ucm.ccla.data.form.spool.SpoolFileGenerator;
import com.ecs.ucm.ccla.data.form.spool.SpoolHeader;
import com.ecs.ucm.ccla.data.instruction.InstructionType;
import com.ecs.ucm.ccla.data.subscription.Contribution;
import com.ecs.ucm.ccla.data.subscription.ContributionAssetAllocation;
import com.ecs.ucm.ccla.data.subscription.ContributionTTLAAllocation;
import com.ecs.ucm.ccla.data.subscription.Subscription;
import com.ecs.ucm.ccla.data.subscription.SubscriptionAssetAllocation;
import com.ecs.ucm.ccla.data.subscription.Subscription.SubscriptionStatusIds;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.embedded.FWFacade;

public class CommunityFirstDonorSubscriptionFormHandler extends AccountFormHandler {


	public CommunityFirstDonorSubscriptionFormHandler(Form form, String userId,
			FWFacade facade) {
		super(form, userId, facade);
	}

	public CommunityFirstDonorSubscriptionFormHandler(String userId,
			FWFacade facade) {
		super(userId, facade);
	}
	
	/** Generate just the DEPBNK ChildDocument for now.
	 * 
	 */
	public void doPostCheckinActions(int docId) throws Exception {
		
		super.doPostCheckinActions(docId);
		
		Log.debug("Performing custom post-checkin actions for " 
		 + form.getFormType() + " form");
		
		// Determine if the subscription is in a valid state.
		Subscription subscription = null;
		
		if (form.getSubscriptionId() == null) {
			Log.error("No subscription ID tied to form ID " + form.getFormId() + ". " +
			 "Unable to continue with post-checkin actions");
			return;
		} else {
			subscription = Subscription.get(form.getSubscriptionId(), facade);
			
			if (subscription.getStatusId() 
				== Subscription.SubscriptionStatusIds.CANCELLED) {
				// Subscription is cancelled - abort here without updating subscription
				// record etc.
				Log.error("Subscription form has status of Cancelled. " +
				 "Aborting post-checkin actions.");
				return;
			}
		}
		
		// Now check if this is the latest form for the Subscription ID.
		Form latestSubscriptionForm = 
		 Form.getLatestFormBySubscriptionId(form.getSubscriptionId(), facade);
		
		if (latestSubscriptionForm != null) {
			if (latestSubscriptionForm.getFormId() != this.form.getFormId()) {
				// Older form has been returned! Abort!!
				Log.warn("Older form for Subscription ID " 
				 + form.getSubscriptionId() + " detected! Latest form has ID " 
				 + latestSubscriptionForm.getFormId() 
				 + ". Aborting post-checkin actions.");
				return;
			}
		} else {
			// Weird - no latest form found with Subscription ID. There must have been
			// at least one (i.e. the current form in context) so this is a fatal
			// state.
			throw new ServiceException("Unable to determine latest Form ID for " +
			 "Subscription ID " + form.getSubscriptionId());
		}
		
		String docGuid = null;
		try {
			docGuid = CCLAUtils.getDocGuidFromDid(docId);
		} catch (Exception e) {
			String err = "FormHandler: doPostCheckinActions: unable to set docGuid: " + 
			 e.getMessage();
			Log.error(err);
			throw new ServiceException(err);
		}
		

		// If we got this far, the form ID is valid for the linked Subscription.
		
		// Update the Subscription Status to 'Form returned'
		subscription.setStatusId(Subscription.SubscriptionStatusIds.FORM_RETURNED);
		subscription.setDateFormReceived(latestSubscriptionForm.getDateReturned());
		subscription.persist(facade, Globals.Users.System);
		
		/*
		LWDocument formDoc = new LWDocument(docId, true);
		
		// Remove all pending ChildDocuments first.
		InstructionDocServices.deletePendingChildDocs(formDoc.getDocName(), facade);

		
		// Fetch the CDF Organisation instance.
		Entity cdfOrg = Entity.get
		 (CommunityFirstClientEnrolmentHandler.CDF_ORGANISATION_ID, facade);
		
		AuroraClient auroraClient = Entity.getAuroraClient
		 (cdfOrg.getOrganisationId(), facade);
		
		Vector<Contribution> contributions = Contribution.getAllBySubscriptionId
		 (subscription.getId(), facade);
		
		// Determines the Subscription account
		
		if (contributions.isEmpty()) {
			String msg = "Unable to continue with post-checkin actions: subscription " +
			 "had no associated contributions.";
			
			Log.error(msg);
			return;
		}
		
		int contributionTypeId = contributions.get(0).getContributionTypeId();
		
		String communityFirstAccountType = CommunityFirstClientEnrolmentHandler
		 .getAccountType(contributionTypeId);
		
		Account account = Account.getCFAccount
		 (Fund.FundCodes.C.toString(), communityFirstAccountType, facade);
		
		if (account == null) {
			Log.error("No Community First Account found with Fund: " + 
			 Fund.FundCodes.C.toString() + ", type: " + communityFirstAccountType);
			return;
		}

		String parentDocname = null;
		try {
			LWDocument parentDoc = CCLAUtils.getLatestLwdFromDocGuid(form.getRetDocGuid());
			parentDocname = parentDoc.getAttribute(Globals.UCMFieldNames.DocName);
		} catch (Exception e) {
			throw new DataException(e.getMessage(), e);
		}
		
		DataResultSet rsChildDocs = new DataResultSet(new String[] {
			"CLASS", "ACCOUNT", "AMOUNT", "CLIENTNO",
			"FUND", "UNITS", "DESTACCNUMEXT", "DESTFUND"
		});
		
		// Instruction Type used for the initial Deposit instruction
		InstructionType depositInstrType =  InstructionType.getIdCache()
		 .getCachedInstance(SharedObjects.getEnvironmentInt
		 ("CF_Child_Deposit_DocType", InstructionType.Ids.DEPBNK));
		
		String childDepositDocTypeName = depositInstrType.getName();

		Vector<String> newEntry = new Vector<String>();
		newEntry.add(childDepositDocTypeName); //CLASS
		newEntry.add(account.getAccountNumberString()); //ACCOUNT
		newEntry.add(subscription.getSubscriptionAmount().toPlainString()); //AMOUNT						
		newEntry.add(auroraClient.getPaddedClientNumber()); //CLIENTNO
		newEntry.add(SubscriptionUtils.CF_DEPOSIT_FUND); //FUND
		newEntry.add(""); //UNITS
		newEntry.add(""); //DESTACCNUMEXT
		newEntry.add(""); //DESTFUND
		rsChildDocs.addRow(newEntry);
		
		if (!rsChildDocs.isEmpty()) {
			int newCount = InstructionDocServices.checkinChildDocs
			 (parentDocname, rsChildDocs, facade, userId);
		}
		*/
	}
	
	@Override
	public void doPostValidateActions() throws DataException, ServiceException {
		super.doPostValidateActions();
	}

	@Override
	public Hashtable<String, String> getDocMetaMapping() throws DataException {
		
		Hashtable<String, String> map = super.getDocMetaMapping();
		
		Subscription subscription = Subscription.get(form.getSubscriptionId(), facade);
		
		if (subscription.getStatusId() 
			== Subscription.SubscriptionStatusIds.CANCELLED) {
			// Subscription has been cancelled. Mark this as CONDINS with explanation
			// in the Comments field.
			
			Log.warn("Form for cancelled Subscription ID " 
			 + form.getSubscriptionId() + " detected!");
					
			map.put(UCMFieldNames.DocClass, DocumentClass.Classes.CONDINS);
			// Add comment
			map.put(UCMFieldNames.Comments, "Subscription Form invalid. " +
			 "Subscription ID " + form.getSubscriptionId() + " has been cancelled");
			
			// Add isMultiDoc = "No"
			map.put(UCMFieldNames.MultiDoc, "No");
			
			return map;
		}
		
		// Check if this form has been Cancelled.
		if (form.getFormStatusId() == FormStatus.CANCELLED.id) {
			// Abort here.
			return map;
		}
		
		// Check if this is the latest form for the Subscription ID.
		Form latestSubscriptionForm = 
		 Form.getLatestFormBySubscriptionId(form.getSubscriptionId(), facade);
		
		if (latestSubscriptionForm != null
			&&
			latestSubscriptionForm.isValid() != null 
			&&
			!latestSubscriptionForm.isValid()) {
			// Form already manually marked as invalid!
			Log.debug("Form ID " + latestSubscriptionForm.getFormId() + 
			 " has been manually marked as Invalid.");
			
			map.put(UCMFieldNames.DocClass, DocumentClass.Classes.CONDINS);
			
		} else if (latestSubscriptionForm != null) {
			if (latestSubscriptionForm.getFormId() != this.form.getFormId()) {
				// Older form has been returned!
				
				// We'll index as a CONDINS here instead, with an explanation in the
				// xComments field.
				Log.warn("Older form for Subscription ID " 
				 + form.getSubscriptionId() + " detected! Latest form has ID " 
				 + latestSubscriptionForm.getFormId() 
				 + ". Indexing form as CONDINS instead.");
				
				map.put(UCMFieldNames.DocClass, DocumentClass.Classes.CONDINS);
				// Add comment
				map.put(UCMFieldNames.Comments, "Subscription Form invalid. " +
				 "Newer form exists for Subscription ID " + form.getSubscriptionId() + 
				 " with Form ID " + latestSubscriptionForm.getFormId());
				
			} else {
				// Latest form has been returned.
				
				// Always index as PREADVICE.
				map.put(UCMFieldNames.DocClass, DocumentClass.Classes.PREADVICE);
				
				// Add Subscription amount
				map.put(UCMFieldNames.Amount, CCLAUtils.DECIMAL_FORMAT.format(
				 subscription.getSubscriptionAmount()));
				
				// Below stuff will populate Client/Account fields with CDF details
				/*
				// Add CDF client number and appropriate deposit account
				Entity cdfOrg = Entity.get
				 (CommunityFirstClientEnrolmentHandler.CDF_ORGANISATION_ID, facade);
				
				AuroraClient cdfAuroraClient = Entity.getAuroraClientCompanyMapping
				 (CommunityFirstClientEnrolmentHandler.CDF_ORGANISATION_ID, 
				 Company.getNameCache().getCachedInstance(
				  CommunityFirstClientEnrolmentHandler.COMPANY_CODE), 
				 facade);
				
				map.put(UCMFieldNames.ClientName, cdfOrg.getOrganisationName());
				map.put(UCMFieldNames.OrgAccountCode, cdfOrg.getOrgAccountCode());
				
				map.put(UCMFieldNames.Company, 
				 CommunityFirstClientEnrolmentHandler.COMPANY_CODE);
				map.put(UCMFieldNames.ClientNumber, 
				 cdfAuroraClient.getPaddedClientNumber());
				
				// Determine the required CDF Deposit account.
				Account cdfDepositAccount = CommunityFirstClientEnrolmentHandler
				 .getDepositAccount(subscription, facade);
				
				map.put(UCMFieldNames.AccountNumber, 
				 cdfDepositAccount.getAccountNumberString());
				*/
				
				// Below stuff will populate Client/Account fields with LCF details,
				// as opposed to CDF details above.
				/*
				Integer lcfOrgId = Account.getOwnerOrganisationId
				 (subscription.getAccountId(), facade);
				
				Entity lcfOrg = Entity.get(lcfOrgId, facade);
				
				map.put(UCMFieldNames.ClientName, lcfOrg.getOrganisationName());
				map.put(UCMFieldNames.OrgAccountCode, lcfOrg.getOrgAccountCode());
				
				// Blank out the account number to avoid confusion.
				map.put(UCMFieldNames.AccountNumber, "");
				*/
				
				// Add Payment Ref
				map.put(UCMFieldNames.PaymentRef, subscription.getPaymentRef());
			}
		} else {
			// Weird - no latest form found with Subscription ID. There must have been
			// at least one (i.e. the current form in context). Ignore the error here,
			// will throw an error during postCheckinActions instead.
			Log.warn("Unable to determine latest Form ID for " +
			 "Subscription ID " + form.getSubscriptionId());
		}
		
		return map;
	}

	@Override
	public Form generateForm(FormType formType, Element element, Integer investmentId) 
	throws ServiceException, DataException 
	{
		//1. Check Investments.
		if (investmentId==null) 
			throw new DataException("Unable to generate " + formType.getName() + 
					 ": investmentId is not specified");
			
		Subscription investment = Subscription.get(investmentId, facade);
		if (investment==null)
			throw new DataException("Unable to generate " + formType.getName() + 
					 ": no investment with ID " + investmentId);
		
		//2. Check Organisation
		Entity org = Entity.get(element.getElementId(), true, facade);
		
		if (org == null)
			throw new DataException("Unable to generate " + formType.getName() + 
			 ": no org found with ID " + element.getElementId());
		
		Log.debug("Generating " + formType.getName() + " form for Org ID: " 
		 + org.getOrganisationId());
		
		// Run various checks to ensure we have minimal data to generate the form.
		
		//3. Check Campaign Enrolment. 
		CampaignEnrolment enrolment = CampaignEnrolment.get
		 (CommunityFirstClientEnrolmentHandler.CAMPAIGN_ID, org.getOrganisationId(), facade);
		
		if (enrolment == null)
			throw new DataException("Unable to generate " + formType.getName() + ", " +
			 org.getOrganisationName() + " is not enrolled to Community First campaign");
		
		//4. Check Person Details
		if (enrolment.getPersonId()==null)
			throw new DataException("Unable to generate " + formType.getName() + ", " +
					 "No correspondent for this enrolment");
		
		Person correspondent = Person.get(enrolment.getPersonId(), facade);
		if (correspondent==null)
			throw new DataException("Unable to generate " + formType.getName() + ", " +
			 "No correspondent with id:"+enrolment.getPersonId());
	
		//5. Check Contact Details
		if (!enrolment.isContactable())
			throw new DataException("Unable to generate " + formType.getName() + ", " +
			 "campaign enrolment does not have a correspondent/address set");
	
		Contact contact = Contact.get(enrolment.getContactId(), facade);
	
		boolean isAddressValid = Address.addressDataValid
		 (contact.getAddressId(), facade);
		
		if (!isAddressValid)
			throw new ServiceException("Assigned postal address is not valid, " +
			 "ensure it has a number/street/postcode set");

		
		return createDonorSubscriptionForm(org, investment, 
				contact, correspondent, formType, enrolment, true,
				facade, userId);
	}
	
	/**
	 * Generate the Donor Subscription Form
	 * 
	 * @param org
	 * @param subscription
	 * @param contact
	 * @param correspondent
	 * @param formType
	 * @param enrolment
	 * @param facade
	 * @param userId
	 * @return
	 * @throws DataException
	 * @throws ServiceException
	 */
	public static Form createDonorSubscriptionForm(Entity org, Subscription subscription, 
			Contact contact, Person correspondent, FormType formType, 
			CampaignEnrolment enrolment, boolean print, FWFacade facade, 
			String userId) throws DataException, ServiceException {
		
		// Determine whether we are generating a legacy-style form with TTLA details.
		// This will affect the data we need to collect and the spool file generator used.
		boolean useLegacyForm = 
		 (formType.getFormTypeId() == FormType.COMMUNITY_FIRST_DONOR_TTLA_SUBSCRIPTION);
		
		//Use default COIF company
		Company coifCompany = Company.getCache().getCachedInstance(Company.COIF);
		
		//Get Aurora COIF Client link
		AuroraClient auroraClient = Entity.getAuroraClientCompanyMapping
		 (org.getOrganisationId(), coifCompany, facade);

		if (auroraClient == null) {
			throw new ServiceException("Unable to generate Donor Subscription Form: " +
			 "no COIF Client number set against Organisation " + org.getOrgAccountCode());
		}
		
		//Get all selected donors
		Vector<Contribution> contributions = 
		 Contribution.getAllBySubscriptionId(subscription.getId(), facade);
		
		//Fail if there are no allocated donors/contributions
		if (contributions.isEmpty())
			throw new ServiceException("Unable to create " + formType.getName() + 
			 " form, there must be at least 1 Donor selected");
		
		// Create a map of Contribution -> List<ContributionAssetAllocation>
		HashMap<Contribution, List<ContributionAssetAllocation>> 
	     contributionAssetAllocations = 
	     new HashMap<Contribution, List<ContributionAssetAllocation>>();
		
		for (Contribution contribution : contributions) {
			// Fail if a contribution doesn't have an amount set.
			if (contribution.getAmount() == null) {
				throw new ServiceException("Unable to create " + formType.getName() + 
				 " form, one of the selected donations doesn't have an amount set." +
				 " De-select this donor or set a donation amount.");
			}
			
			Vector<ContributionAssetAllocation> assetAllocs =
			 ContributionAssetAllocation.getAllByContributiontId
			 (contribution.getId(), facade);
			
			if (!assetAllocs.isEmpty()) {
				// Add up all asset allocations for this contribution and confirm they
				// match the contribution amount.
				BigDecimal totalAssetAllocation = BigDecimal.ZERO;
				
				for (ContributionAssetAllocation assetAlloc : assetAllocs) {
					totalAssetAllocation = totalAssetAllocation.add
					 (assetAlloc.getAmount());
				}
				
				if (totalAssetAllocation.compareTo(contribution.getAmount()) != 0) {
					throw new ServiceException("Unable to create " + formType.getName() + 
					 " form, one of the selected donations has asset allocations that" +
					 " don't match the donation amount of " +
					 contribution.getAmount().toPlainString() + ". Remove these asset" +
					  " allocations, or ensure they match the donation amount.");
				}
			}
			
			contributionAssetAllocations.put(contribution, assetAllocs);
		}
		
		// Donation (CF Fund) Account
		Account account = Account.get(subscription.getAccountId(), facade);
		
		// Create a map of Donor Element ID -> Element
		HashMap<Integer, Element> donorMap = new HashMap<Integer, Element>();
		
		for (Contribution contribution: contributions) {
			Element element = Element.get(contribution.getContributorId(), facade);
			if (element.getType().equals(ElementType.ORGANISATION)) {
				Entity entity = Entity.get(element.getElementId(), facade);
				if (entity!=null) 
					donorMap.put(element.getElementId(), entity);
			} else if (element.getType().equals(ElementType.PERSON)) {
				Person person = Person.get(element.getElementId(), facade);
				if (person!=null)
					donorMap.put(element.getElementId(), person);
			}
		}
		
		// Donor Account Generation 
		// ==============
		// Create required Donor Accounts and Aurora CREATE_ACCOUNT instructions, if they
		// don't already exist.
		SubscriptionUtils.createDonorAccountsForSubscription
		 (subscription.getId(), null, userId, facade);

		// ==============
		
		// Build list of Donor Accounts.
		HashMap<Integer, List<Account>> donorAccounts = 
		 new HashMap<Integer, List<Account>>();
		
		for (Contribution contribution: contributions) {
			Integer donorId = contribution.getContributorId();
			
			donorAccounts.put(donorId, SubscriptionUtils.getLCFDonorAccounts
			 (org.getOrganisationId(), donorId, facade));
		}
		
		HashMap<Integer, Vector<ContributionTTLAAllocation>> donationTTLAMap = null;
		Vector<ContributionTTLAAllocation> ttlaAllocs = null;
		HashMap<Fund, SubscriptionAssetAllocation> fundAllocationMap = null;
		Vector<Entity> ttlaEntityVec = null;
		
		if (useLegacyForm) {
			// Old stuff required for creation of legacy TTLA form
			
			// Create a map of Contribution ID -> Vector<DonationTTLAAllocation>
			donationTTLAMap = new HashMap<Integer, Vector<ContributionTTLAAllocation>>();

			for (Contribution contribution: contributions) {
				ttlaAllocs = ContributionTTLAAllocation.getAllByContributionId
				 (contribution.getId(), facade);
	
				if (!ttlaAllocs.isEmpty())
					donationTTLAMap.put(contribution.getId(), ttlaAllocs);
			}
			
			//Create a hashmap for all the fund allocation
			fundAllocationMap = new HashMap<Fund, SubscriptionAssetAllocation>();
			
			Vector<SubscriptionAssetAllocation> fundAllocs = SubscriptionAssetAllocation
			 .getAllBySubscriptionId(subscription.getId(), facade);
			
			//Sanity check to ensure the total subscription amount and total fund allocation
			//amount match.
			BigDecimal totalFundAllocationAmount = SubscriptionAssetAllocation
			 .getTotalSubscriptionFundAllocation(fundAllocs);
			
			if ((totalFundAllocationAmount.compareTo(BigDecimal.ZERO) != 0)
				&&
				(subscription.getSubscriptionAmount().compareTo(totalFundAllocationAmount)
				!= 0)) {
				throw new ServiceException("Unable to create " + formType.getName() + 
				 " form, total fund allocation amount (" + 
				 totalFundAllocationAmount.toPlainString() + ") does not match the total " +
				 "subscription amount (" + subscription.getSubscriptionAmount() + ")");
			}
			
			ttlaEntityVec = Entity.getAllByCategoryId
			 (SharedObjects.getEnvironmentInt("CF_TTLA_CATEGORY_ID", 1056), facade);

			for (SubscriptionAssetAllocation allocation: fundAllocs) {
				fundAllocationMap.put(allocation.getFund(), allocation);
			}
		}

		ByteArrayOutputStream outputStream = null;
		Form form = null;
		
		try {
			// Set the previous form to 'Old' status, if one exists.
			Form prevForm = 
			 Form.getLatestFormBySubscriptionId(subscription.getId(), facade);
			
			if (prevForm != null 
				&& (prevForm.getFormStatusId() != FormStatus.CANCELLED.id)) {
				Log.debug("Setting previous Subscription form ID " +
				 prevForm.getFormId() + " to 'Old' status");
				
				prevForm.setFormStatusId(FormStatus.OLD.id);
				prevForm.persist(facade, userId);
			}
			
			// Generate a new Form entry in the DB.
			form = Form.add(
				formType,
				enrolment.getCampaignEnrolmentId(),
				null, //Campaign ActivityId
				correspondent.getPersonId(),
				null, //genDocId
				org.getOrganisationId(),
				subscription.getId(),
				facade,
				userId	
			);
			
			// Link the account to the generated form
			FormElementApplied.add
			 (form.getFormId(), account.getAccountId(), facade);
			
			String fileName = FormUtils.getSpoolFileName(form, org, auroraClient);

			outputStream = createSubscriptionSpoolFile(form, fileName, donationTTLAMap, 
			 contributions, contributionAssetAllocations, donorAccounts, 
			 fundAllocationMap, org, 
			 correspondent, contact, enrolment, subscription, 
			 auroraClient, coifCompany, userId, account, null, donorMap, ttlaEntityVec);
			
			// Generate a new file in the temporary location
			File spoolFile = FormUtils.createTempSpoolFile
			 (fileName, coifCompany.getCode(), outputStream);
			
			form.setDateGenerated(new Date());
			
			Log.debug("Generated temp spool file: " + spoolFile.getName() + 
			 ". Dispatching for print.");
			
			if (print) {
				FormUtils.printForm(spoolFile.getAbsolutePath());
				
				// Update form data to indicate it was printed
				form.setFormStatusId(Form.FormStatus.PRINTED.id);
				form.setDatePrinted(new Date());
			} else {
				// Set generated status
				form.setFormStatusId(Form.FormStatus.GENERATED.id);
			}
			
			form.persist(facade, userId);
			
			// Add activity log to campaign enrolment, indicating form
			// was generated.
			enrolment.addActivity(correspondent.getPersonId(), 
			 CampaignActivityType.FORM_CREATION_ACTIVITY_ID,
			 formType.getName() + " form " + form.getFormId() + 
			 " created and dispatched for print", null, facade, userId); 

			// Update the Subscription Status to 'Form generated'
			subscription.setStatusId(Subscription.SubscriptionStatusIds.FORM_GENERATED);

			//Clear the date form returned due to the newly created form.
			subscription.setDateFormReceived(null);
			
			subscription.persist(facade, Globals.Users.System);
			
			// Update the Form ID field on the contribution records
			for (Contribution contrib : contributions) {
				contrib.setFormId(form.getFormId());
				contrib.persist(facade, userId);
			}
			
		} catch (Exception e) {
			Log.error("Failed to generate/print " + formType.getName() + " form", e);
			
			// Add activity log to Client Services process, indicating form
			// failed to generate.
			enrolment.addActivity(correspondent.getPersonId(), 
			 CampaignActivityType.FORM_CREATION_ACTIVITY_ID,
			 "Failed to generate " + formType.getName() + " form", 
			 null, facade, userId); 
			
			throw new ServiceException(e);
		}
		
		return form;
	}
	
	
	private static ByteArrayOutputStream createSubscriptionSpoolFile(
			Form form, String fileName, 
			HashMap<Integer, Vector<ContributionTTLAAllocation>> donationTTLAMap, 
			Vector<Contribution> contributions, 
			HashMap<Contribution, List<ContributionAssetAllocation>> contributionAssetAllocations,
			HashMap<Integer, List<Account>> donorAccounts,
			HashMap<Fund, SubscriptionAssetAllocation> fundAllocationMap, 
			Entity org, Person correspondent, 
			Contact contact, CampaignEnrolment enrolment, Subscription investment, 
			AuroraClient auroraClient, Company company, String userId, Account account, 
			BankAccount bankAccount, HashMap<Integer, Element> donorMap, Vector<Entity> ttlaEntityVec) 
			
			throws IOException, DataException, ServiceException{
		
		//1. Create the wrapper object containing all data needed to run off a spool file.
		SubscriptionForm subscriptionForm = new SubscriptionForm(
			form.getFormId(), org, correspondent, contact, account, bankAccount, 
			null, null, 
			investment, 
			contributionAssetAllocations, 
			donorAccounts,
			donationTTLAMap, 
			contributions, 
			fundAllocationMap, 
			donorMap, 
			ttlaEntityVec
		);
		
		//2. Create SpoolHeader
		String templatePath = fileName;
		
		SpoolHeader spoolHeader = new SpoolHeader
		 (company.getCode(), auroraClient.getPaddedClientNumber(), 
		 userId, 1, form.getFormId(), org.getOrganisationId(), templatePath);
		
		//3. Instantiate spoolfileGenerator
		SpoolFileGenerator spoolFileGenerator = null;
		
		if (form.getFormType().getFormTypeId() 
			== FormType.COMMUNITY_FIRST_DONOR_TTLA_SUBSCRIPTION) {
			// Generate legacy style form with TTLA information
			spoolFileGenerator = new CommunityFirstDonorTTLASubscriptionSpoolFileGenerator
			 (spoolHeader, subscriptionForm);
		} else {
			// Generate new style form
			spoolFileGenerator = new CommunityFirstDonorSubscriptionSpoolFileGenerator
			 (spoolHeader, subscriptionForm);
		}
				
		//4. Create outputstream from spoolfile and return.
		ByteArrayOutputStream outputStream = 
		 spoolFileGenerator.createSpoolFile();
		
		return outputStream;
	}
}
