package com.ecs.stellent.ccla.clientservices.form;

import java.util.HashMap;
import java.util.Vector;

import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.AuroraClient;
import com.ecs.ucm.ccla.data.BankAccount;
import com.ecs.ucm.ccla.data.Contact;
import com.ecs.ucm.ccla.data.ElementAttribute;
import com.ecs.ucm.ccla.data.ElementAttributeApplied;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.RelationName;

/** Container object for all common data fields that will appear on pre-populated 
 *  client/account application forms.
 *  
 *  Capable of storing data for a single account.
 * 
 *  It is expected that subclasses will be used to store specialized pieces of data
 *  which only relate to a single campaign/fund etc.
 *  
 * @author Tom
 *
 */
public class AccountRegistrationForm {
	
	protected int formId;
	
	/** Delivery address for the form. Could be an email or postal address */
	protected Contact 		contactAddress;
	
	/** Nominated correspondent - must have Contact mapping preloaded. The form will
	 *  be addressed to this person. */
	protected Person 			correspondent;
	
	/** Organisation - must have Contact mapping preloaded */
	protected Entity 			entity;
	protected AuroraClient 	auroraClient;
	
	/** Mapping of IDENTIFIER_IDs to their mapped values for the organistion. */
	protected HashMap<Integer, String> orgIdentifiers;
	
	/** Mapping of ATTRIBUTE_IDs to their mapped Attribute objects for the organisation
	 *  
	 */
	protected HashMap<Integer, ElementAttributeApplied> orgAttributes;
	
	/** The Account which will be displayed on the form. */
	protected Account account;
	
	/** Mapping of ATTRIBUTE_IDs to their mapped Attribute objects for the account
	 *  
	 */
	protected HashMap<Integer, ElementAttributeApplied> accountAttributes;
	
	/** The mandated account, if applicable */
	protected Account mandatedAccount;
	
	/** The nominated Bank Account which will be displayed on the form (may be null) */
	protected BankAccount bankAccount;
	
	/** The pay-away Bank Account. Only applicable for accounts with distribution 
	 *  preference of 'pay away income'  */
	protected BankAccount payAwayBankAccount;
	
	/** Org Category hierarchy **/
	protected Vector<String> orgCategoryTree;
	
	protected HashMap<RelationName, Vector<Person>> relatedAccountPersons;
	
	/** Determines whether or not an Email Indemnity has been received for this client
	 *  in the past.
	 */
	protected boolean emailIndemnityReceived;
	
	public AccountRegistrationForm(int formId,
			Contact contactAddress,
			Person correspondent, Entity entity, AuroraClient auroraClient,
			HashMap<Integer, String> orgIdentifiers, 
			HashMap<Integer, ElementAttributeApplied> orgAttributes,
			Account account, 
			HashMap<Integer, ElementAttributeApplied> accountAttributes,
			Account mandatedAccount,
			BankAccount bankAccount, BankAccount payAwayBankAccount,
			Vector<String> orgCategoryTree,
			HashMap<RelationName, Vector<Person>> relatedAccountPersons,
			boolean emailIndemnityReceived) {
		
		this.formId = formId;
		this.contactAddress = contactAddress;
		this.correspondent = correspondent;
		this.entity = entity;
		this.auroraClient = auroraClient;
		this.orgIdentifiers = orgIdentifiers;
		this.orgAttributes = orgAttributes;
		this.account = account;
		this.accountAttributes = accountAttributes;
		this.mandatedAccount = mandatedAccount;
		this.bankAccount = bankAccount;
		this.payAwayBankAccount = payAwayBankAccount;
		this.orgCategoryTree = orgCategoryTree;
		this.relatedAccountPersons = relatedAccountPersons;
		this.emailIndemnityReceived = emailIndemnityReceived;
	}

	public int getFormId() {
		return formId;
	}

	public Person getCorrespondent() {
		return correspondent;
	}

	public Account getAccount() {
		return account;
	}

	public Contact getContactAddress() {
		return contactAddress;
	}

	public void setContactAddress(Contact contactAddress) {
		this.contactAddress = contactAddress;
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public AuroraClient getAuroraClient() {
		return auroraClient;
	}

	public void setAuroraClient(AuroraClient auroraClient) {
		this.auroraClient = auroraClient;
	}

	public HashMap<Integer, String> getOrgIdentifiers() {
		return orgIdentifiers;
	}

	public void setOrgIdentifiers(HashMap<Integer, String> orgIdentifiers) {
		this.orgIdentifiers = orgIdentifiers;
	}

	public BankAccount getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(BankAccount bankAccount) {
		this.bankAccount = bankAccount;
	}

	public void setFormId(int formId) {
		this.formId = formId;
	}

	public void setCorrespondent(Person correspondent) {
		this.correspondent = correspondent;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Vector<String> getOrgCategoryTree() {
		return orgCategoryTree;
	}

	public void setOrgCategoryTree(Vector<String> orgCategoryTree) {
		this.orgCategoryTree = orgCategoryTree;
	}

	public void setPayAwayBankAccount(BankAccount payAwayBankAccount) {
		this.payAwayBankAccount = payAwayBankAccount;
	}

	public BankAccount getPayAwayBankAccount() {
		return payAwayBankAccount;
	}

	public void setRelatedAccountPersons
	 (HashMap<RelationName, Vector<Person>> relatedAccountPersons) {
		this.relatedAccountPersons = relatedAccountPersons;
	}

	public HashMap<RelationName, Vector<Person>> getRelatedAccountPersons() {
		return relatedAccountPersons;
	}

	public void setEmailIndemnityReceived(boolean emailIndemnityReceived) {
		this.emailIndemnityReceived = emailIndemnityReceived;
	}

	public boolean isEmailIndemnityReceived() {
		return emailIndemnityReceived;
	}

	public void setMandatedAccount(Account mandatedAccount) {
		this.mandatedAccount = mandatedAccount;
	}

	public Account getMandatedAccount() {
		return mandatedAccount;
	}

	public HashMap<Integer, ElementAttributeApplied> getOrgAttributes() {
		return orgAttributes;
	}

	public HashMap<Integer, ElementAttributeApplied> getAccountAttributes() {
		return accountAttributes;
	}
}
