package com.ecs.stellent.ccla.clientservices.form;

import java.util.HashMap;
import java.util.Vector;

import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.BankAccount;
import com.ecs.ucm.ccla.data.Contact;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.RelationName;
import com.ecs.ucm.ccla.data.form.Form;

/** Generic wrapper for data objects required for a bank account form type,
 *  e.g. Additional Bank Account form.
 *  
 * @author Tom
 *
 */
public class BankAccountForm extends AccountForm {
	
	private BankAccount bankAccount;
	private BankAccount payAwayBankAccount;
	
	public BankAccountForm(Form form, Person correspondent,
			Contact postalAddress, Entity organisation, 
			Account account, Account mandatedAccount,
			HashMap<RelationName, Vector<Person>> relatedAccountPersons,
			boolean emailIndemnityReceived,
			BankAccount bankAccount, BankAccount payAwayBankAccount) {
		
		super(form, correspondent, postalAddress, organisation, 
		 account, null, mandatedAccount,
		 relatedAccountPersons, emailIndemnityReceived);

		this.bankAccount = bankAccount;
		this.payAwayBankAccount = payAwayBankAccount;
	}

	public void setBankAccount(BankAccount bankAccount) {
		this.bankAccount = bankAccount;
	}

	public BankAccount getBankAccount() {
		return bankAccount;
	}

	public void setPayAwayBankAccount(BankAccount payAwayBankAccount) {
		this.payAwayBankAccount = payAwayBankAccount;
	}

	public BankAccount getPayAwayBankAccount() {
		return payAwayBankAccount;
	}

}
