package com.ecs.stellent.ccla.clientservices.form;

import java.util.HashMap;
import java.util.Vector;

import com.ecs.ucm.ccla.data.Contact;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.RelationName;
import com.ecs.ucm.ccla.data.campaign.FundInvestmentIntention;
import com.ecs.ucm.ccla.data.form.Form;

/** Generic wrapper for data objects required for a single-account form type,
 *  e.g. Subscription, Redemption, Cancellation forms.
 * 
 * @author Tom
 *
 */
public class AccountForm {
	
	/** Form data object. */
	private Form form;
	
	/** Used for the name that appears above the address. */
	private Person correspondent;
	/** The postal address generally displayed on the first page of the form. */
	private Contact postalAddress;
	private Entity organisation;
	private Account account;
	
	/** The mandated account for the above account, if applicable */
	private Account mandatedAccount;
	
	/** Persons and their relations to the given acccount. */
	private HashMap<RelationName, Vector<Person>> relatedAccountPersons;
	
	/** Stores an investment intention for the account, if applicable */
	private FundInvestmentIntention intention;
	
	/** Determines whether or not an Email Indemnity has been received for this client
	 *  in the past.
	 */
	private boolean emailIndemnityReceived;
	
	public AccountForm(Form form, Person correspondent, Contact postalAddress,
	 Entity organisation, Account account, FundInvestmentIntention intention,
	 Account mandatedAccount,
	 HashMap<RelationName, Vector<Person>> relatedAccountPersons, 
	 boolean emailIndemnityReceived) {
		
		this.form = form;
		
		this.correspondent = correspondent;
		this.postalAddress = postalAddress;
		this.organisation = organisation;
		this.account = account;
		this.intention = intention;
		this.mandatedAccount = mandatedAccount;
		this.relatedAccountPersons = relatedAccountPersons;
		
		this.setEmailIndemnityReceived(emailIndemnityReceived);
	}

	public Entity getOrganisation() {
		return organisation;
	}

	public Account getAccount() {
		return account;
	}
	
	public Form getForm() {
		return form;
	}
	
	public Person getCorrespondent() {
		return correspondent;
	}

	public Contact getPostalAddress() {
		return postalAddress;
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

	public void setIntention(FundInvestmentIntention intention) {
		this.intention = intention;
	}

	public FundInvestmentIntention getIntention() {
		return intention;
	}	
}
