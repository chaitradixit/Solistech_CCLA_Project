package com.ecs.stellent.ccla.clientservices.form;

import java.util.HashMap;
import java.util.Vector;

import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.AuroraClient;
import com.ecs.ucm.ccla.data.BankAccount;
import com.ecs.ucm.ccla.data.Contact;
import com.ecs.ucm.ccla.data.ElementAttributeApplied;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.RelationName;
import com.ecs.ucm.ccla.data.campaign.EnrolmentAttributeApplied;

/** Used for forms that refer to a set of accounts, instead of just one. 
 **/
public class ClientInfoForm extends ClientRegistrationForm {

	protected Vector<Account> accounts;
	protected Vector<BankAccount> bankAccounts;
	
	public ClientInfoForm(int formId, Contact contactAddress,
			Person correspondent, Entity entity, AuroraClient auroraClient,
			HashMap<Integer, String> orgIdentifiers,
			HashMap<Integer, ElementAttributeApplied> orgAttributes,
			Account account, BankAccount bankAccount,
			Vector<String> orgCategoryTree,
			HashMap<RelationName, Vector<Person>> relatedOrgPersons,
			boolean emailIndemnityReceived,
			HashMap<Integer, EnrolmentAttributeApplied> enrolmentAttributes,
			HashMap<String, ? extends Object> extraAttributes,
			Vector<Account> accounts,
			Vector<BankAccount> bankAccounts) {
		super(formId, contactAddress, correspondent, entity, auroraClient,
				orgIdentifiers, orgAttributes, account, bankAccount, orgCategoryTree,
				relatedOrgPersons, emailIndemnityReceived, enrolmentAttributes,
				extraAttributes);
		
		this.accounts = accounts;
		this.bankAccounts = bankAccounts;
	}

	public Vector<Account> getAccounts() {
		return accounts;
	}

	public Vector<BankAccount> getBankAccounts() {
		return bankAccounts;
	}
}
