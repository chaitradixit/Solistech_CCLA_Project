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

public class DioLoanRegistrationForm extends AccountRegistrationForm {

	protected boolean requireBankStatement;
	
	public DioLoanRegistrationForm(int formId, Contact contactAddress,
			Person correspondent, Entity entity, AuroraClient auroraClient,
			HashMap<Integer, String> orgIdentifiers,
			HashMap<Integer, ElementAttributeApplied> orgAttributes,
			Account account,
			HashMap<Integer, ElementAttributeApplied> accountAttributes,
			Account mandatedAccount, BankAccount bankAccount,
			BankAccount payAwayBankAccount, Vector<String> orgCategoryTree,
			HashMap<RelationName, Vector<Person>> relatedAccountPersons,
			boolean emailIndemnityReceived,
			boolean requireBankStatement) {
		super(formId, contactAddress, correspondent, entity, auroraClient,
				orgIdentifiers, orgAttributes, account, accountAttributes,
				mandatedAccount, bankAccount, payAwayBankAccount, orgCategoryTree,
				relatedAccountPersons, emailIndemnityReceived);

		this.requireBankStatement = requireBankStatement;
	}

	public boolean isRequireBankStatement() {
		return requireBankStatement;
	}

}
