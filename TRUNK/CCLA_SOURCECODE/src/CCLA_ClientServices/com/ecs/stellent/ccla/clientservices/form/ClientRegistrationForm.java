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
import com.ecs.ucm.ccla.data.campaign.EnrolmentAttributeApplied;

/** Container object for all common data fields that will appear on pre-populated 
 *  client application forms.
 *  
 *  Capable of storing data for a single client.
 * 
 *  It is expected that subclasses will be used to store specialized pieces of data
 *  which only relate to a single campaign/fund etc.
 *  
 * @author Tom
 *
 */
public class ClientRegistrationForm {
	
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
	
	protected Account account;
	
	/** The nominated Bank Account which will be displayed on the form (may be null) */
	protected BankAccount bankAccount;
	
	/** Org Category hierarchy **/
	protected Vector<String> orgCategoryTree;
	
	/** All persons related to the Organisation. Must have contact details pre-loaded.
	 * 
	 */
	protected HashMap<RelationName, Vector<Person>> relatedOrgPersons;
	
	/** Determines whether or not an Email Indemnity has been received for this client
	 *  in the past.
	 */
	protected boolean emailIndemnityReceived;
	
	/** Mapping of Applicable Enrolment Attribute IDs to applied enrolment attributes. 
	 **/
	protected HashMap<Integer, EnrolmentAttributeApplied> enrolmentAttributes;

	/** Minimum number of Authorising Person entries that will be output on the form.
	 * 
	 */
	protected Integer minAuthPersons = null;

	/** Minimum number of Signatory entries that will be output on the form.
	 * 
	 */
	protected Integer minSignatories = null;
	
	/** Misc extra attributes to link to the reg. form. */
	protected HashMap<String, ? extends Object> extraAttributes = 
	 new HashMap<String, Object>();
	
	public ClientRegistrationForm(int formId, Contact contactAddress,
			Person correspondent, Entity entity, AuroraClient auroraClient,
			HashMap<Integer, String> orgIdentifiers,
			HashMap<Integer, ElementAttributeApplied> orgAttributes,
			Account account,
			BankAccount bankAccount, Vector<String> orgCategoryTree,
			HashMap<RelationName, Vector<Person>> relatedOrgPersons,
			boolean emailIndemnityReceived,
			HashMap<Integer, EnrolmentAttributeApplied> enrolmentAttributes,
			HashMap<String, ? extends Object> extraAttributes) {
		super();
		this.formId = formId;
		this.contactAddress = contactAddress;
		this.correspondent = correspondent;
		this.entity = entity;
		this.auroraClient = auroraClient;
		this.orgIdentifiers = orgIdentifiers;
		this.orgAttributes = orgAttributes;
		this.account = account;
		this.bankAccount = bankAccount;
		this.orgCategoryTree = orgCategoryTree;
		this.relatedOrgPersons = relatedOrgPersons;
		this.emailIndemnityReceived = emailIndemnityReceived;
		this.enrolmentAttributes = enrolmentAttributes;
		this.extraAttributes = extraAttributes;
	}

	public int getFormId() {
		return formId;
	}

	public Person getCorrespondent() {
		return correspondent;
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

	public Vector<String> getOrgCategoryTree() {
		return orgCategoryTree;
	}

	public void setOrgCategoryTree(Vector<String> orgCategoryTree) {
		this.orgCategoryTree = orgCategoryTree;
	}

	public void setRelatedOrgPersons
	 (HashMap<RelationName, Vector<Person>> relatedOrgPersons) {
		this.relatedOrgPersons = relatedOrgPersons;
	}

	public HashMap<RelationName, Vector<Person>> getRelatedOrgPersons() {
		return relatedOrgPersons;
	}

	public void setEmailIndemnityReceived(boolean emailIndemnityReceived) {
		this.emailIndemnityReceived = emailIndemnityReceived;
	}

	public boolean isEmailIndemnityReceived() {
		return emailIndemnityReceived;
	}

	public HashMap<Integer, ElementAttributeApplied> getOrgAttributes() {
		return orgAttributes;
	}
	
	public HashMap<Integer, EnrolmentAttributeApplied> getEnrolmentAttributes() {
		return enrolmentAttributes;
	}

	public void setEnrolmentAttributes(
			HashMap<Integer, EnrolmentAttributeApplied> enrolmentAttributes) {
		this.enrolmentAttributes = enrolmentAttributes;
	}

	public void setOrgAttributes(
			HashMap<Integer, ElementAttributeApplied> orgAttributes) {
		this.orgAttributes = orgAttributes;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public HashMap<String, ? extends Object> getExtraAttributes() {
		return extraAttributes;
	}
}
