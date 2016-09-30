package com.ecs.stellent.ccla.clientservices.form;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.Address;
import com.ecs.ucm.ccla.data.BankAccount;
import com.ecs.ucm.ccla.data.Contact;
import com.ecs.ucm.ccla.data.Element;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Fund;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.subscription.Contribution;
import com.ecs.ucm.ccla.data.subscription.ContributionAssetAllocation;
import com.ecs.ucm.ccla.data.subscription.ContributionTTLAAllocation;
import com.ecs.ucm.ccla.data.subscription.Subscription;
import com.ecs.ucm.ccla.data.subscription.SubscriptionAssetAllocation;

/** Wrapper class for storing all data required to generate a subscription form.
 *  
 *  Includes support for contributions, TTLA Allocations, Fund Allocations
 * 
 * @author Tom
 *
 */
public class SubscriptionForm {
	
	//form id of the form.
	protected int formId;
	
	// Subscription
	protected Subscription subscription;
	
	// List of contributions
	protected Vector<Contribution> contributions;
	
	// Mapping of Contributions and their Asset Allocations.
	protected HashMap<Contribution, List<ContributionAssetAllocation>> contributionAssetAllocations;
	
	// Mapping of Donor Element IDs and their Donation Accounts.
	protected HashMap<Integer, List<Account>> donorAccounts;
	
	// Legacy data objects containing TTLA details and Subscription-level Fund allocations
	// =================
	//HashMap of DonationTTLAAllocation objects for the donations, could be empty.
	protected HashMap<Integer, Vector<ContributionTTLAAllocation>> ttlaAllocationMap;
	//HashMap of Investment Fund Allocation.
	protected HashMap<Fund, SubscriptionAssetAllocation> fundAllocationMap;
	//Sorted list of ttlaAllocationVec.
	protected Vector<Entity> ttlaEntityVec;
	// =================
	
	//HashMap of donorId and elements
	protected HashMap<Integer, Element> donorMap;

	//Correspondent
	protected Person correspondent;
	
	//Organisation
	protected Entity organisation;
	
	//Contact 
	protected Contact contact;
	
	//Account for the investment.
	protected Account donationAccount;
	
	//Nominated Bank Account of the investment.
	protected BankAccount nominatedBankAccount;
	
	//Minimum number of Directors or Controllers entries that will be printed on the form
	protected Integer minDirectorsOrControllers = null;
	
	//Minimum number of Signatory entries that will be output on the form.
	protected Integer minSignatories = null;
	
	public SubscriptionForm(int formId, Entity organisation, Person correspondent, Contact contact, 
			Account donationAccount, BankAccount nominatedBankAccount, Integer minDirectorsOrControllers,
			Integer minSignatories, 
			Subscription subscription, 
			HashMap<Contribution, List<ContributionAssetAllocation>> contributionAssetAllocations,
			HashMap<Integer, List<Account>> donorAccounts,
			HashMap<Integer, Vector<ContributionTTLAAllocation>> ttlaAllocationMap,
			Vector<Contribution> donationVec,  
			HashMap<Fund, SubscriptionAssetAllocation> fundAllocationMap,
			HashMap<Integer, Element> donorMap, Vector<Entity> ttlaEntityVec) {
		this.formId = formId;
		this.organisation = organisation;
		this.correspondent = correspondent;
		this.contact = contact;
		this.donationAccount = donationAccount;
		this.nominatedBankAccount = nominatedBankAccount;
		this.minDirectorsOrControllers = minDirectorsOrControllers;
		this.minSignatories = minSignatories;
		this.subscription = subscription;
		this.contributionAssetAllocations = contributionAssetAllocations;
		this.donorAccounts = donorAccounts;
		this.ttlaAllocationMap = ttlaAllocationMap;
		this.contributions = donationVec;
		this.fundAllocationMap = fundAllocationMap;
		this.donorMap = donorMap;
		this.ttlaEntityVec = ttlaEntityVec;
	}

	public int getFormId() {
		return formId;
	}

	public void setFormId(int formId) {
		this.formId = formId;
	}

	public Vector<Contribution> getContributions() {
		return contributions;
	}

	public void setContributions(Vector<Contribution> contributions) {
		this.contributions = contributions;
	}

	public Subscription getSubscription() {
		return subscription;
	}

	public void setSubscription(Subscription investment) {
		this.subscription = investment;
	}

	public Person getCorrespondent() {
		return correspondent;
	}

	public void setCorrespondent(Person correspondent) {
		this.correspondent = correspondent;
	}

	public Entity getOrganisation() {
		return organisation;
	}

	public void setOrganisation(Entity organisation) {
		this.organisation = organisation;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public Account getDonationAccount() {
		return donationAccount;
	}

	public void setDonationAccount(Account donationAccount) {
		this.donationAccount = donationAccount;
	}

	public BankAccount getNominatedBankAccount() {
		return nominatedBankAccount;
	}

	public void setNominatedBankAccount(BankAccount nominatedBankAccount) {
		this.nominatedBankAccount = nominatedBankAccount;
	}

	public Integer getMinDirectorsOrControllers() {
		return minDirectorsOrControllers;
	}

	public void setMinDirectorsOrControllers(Integer minDirectorsOrControllers) {
		this.minDirectorsOrControllers = minDirectorsOrControllers;
	}

	public Integer getMinSignatories() {
		return minSignatories;
	}

	public void setMinSignatories(Integer minSignatories) {
		this.minSignatories = minSignatories;
	}

	public HashMap<Fund, SubscriptionAssetAllocation> getFundAllocationMap() {
		return fundAllocationMap;
	}

	public void setFundAllocationMap(
	 HashMap<Fund, SubscriptionAssetAllocation> fundAllocationMap) {
		this.fundAllocationMap = fundAllocationMap;
	}


	public void setTtlaAllocationMap(
			HashMap<Integer, Vector<ContributionTTLAAllocation>> ttlaAllocationMap) {
		this.ttlaAllocationMap = ttlaAllocationMap;
	}

	public HashMap<Integer, Vector<ContributionTTLAAllocation>> getTtlaAllocationMap() {
		return ttlaAllocationMap;
	}

	public HashMap<Integer, Element> getDonorMap() {
		return donorMap;
	}

	public void setDonorMap(HashMap<Integer, Element> donorMap) {
		this.donorMap = donorMap;
	}

	public Vector<Entity> getTtlaEntityVec() {
		return ttlaEntityVec;
	}

	public void setTtlaEntityVec(Vector<Entity> ttlaEntityVec) {
		this.ttlaEntityVec = ttlaEntityVec;
	}

	public HashMap<Contribution, List<ContributionAssetAllocation>>
	 getContributionAssetAllocations() {
		return contributionAssetAllocations;
	}

	public HashMap<Integer, List<Account>> getDonorAccounts() {
		return donorAccounts;
	}
}
