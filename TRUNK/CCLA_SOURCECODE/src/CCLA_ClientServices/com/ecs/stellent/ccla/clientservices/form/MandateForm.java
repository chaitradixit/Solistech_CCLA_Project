package com.ecs.stellent.ccla.clientservices.form;

import intradoc.common.ServiceException;

import java.util.Vector;


import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.utils.stellent.embedded.FWFacade;

public class MandateForm {
	
	/** Hard limit for the number of accounts which can be displayed on a 
	 *  single form.
	 */
	public static final int ACCOUNT_LIST_LIMIT = 20;
	
	private int formId;
	private Person correspondent;
	
	/** List of CCLAAccount instances which will be displayed on the form. */
	private Vector<Account> accounts;
	
	/** Determines whether banking details are displayed or not. Currently
	 *  suppressed for complex cases. */
	private boolean displayBankingDetails;
	
	private Vector<Person> signatories;
	
	/** Container for income/withdrawal payment preferences */
	//private PaymentPrefs paymentPrefs			= null;
	
	public MandateForm(int formId, Entity entity, 
	 Person correspondent, Vector<Account> accounts,
	 boolean displayBankingDetails,
	 Vector<Person> signatories, FWFacade facade) throws ServiceException {
		this.formId = formId;
		this.correspondent = correspondent;
		this.accounts = accounts;
		this.displayBankingDetails = displayBankingDetails;
		this.signatories = signatories;
	}
	
	public enum PaymentPref { 
		PAY_TO_THIS_ACCOUNT, 
		PAY_TO_DIFF_DEP_FUND_ACCOUNT, 
		PAY_TO_BANK_ACCOUNT,
		PAY_DIVIDENDS_TO_DEP_FUND // for unitized funds only
	};

	public int getFormId() {
		return formId;
	}

	public Person getCorrespondent() {
		return correspondent;
	}

	public Vector<Account> getAccounts() {
		return accounts;
	}
	
	public Vector<Person> getSignatories() {
		return signatories;
	}

	public boolean isDisplayBankingDetails() {
		return displayBankingDetails;
	}
}
