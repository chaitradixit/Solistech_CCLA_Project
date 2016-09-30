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

/** Container object for all data fields that will appear on pre-populated PSDF
 *  application forms.
 *  
 *  Echoes back previously-captured data and intentions for opening a single account
 *  in the PSIC Public Sector Fund.
 * 
 * @author Tom
 *
 */
public class PSDFRegistrationForm extends AccountRegistrationForm {
	
	/** Determines whether the organisation is considered to be a Local Authority
	 *  or not.
	 */
	protected boolean isLocalAuthority;
	
	public PSDFRegistrationForm(int formId,
			Contact contactAddress,
			Person correspondent, Entity entity, AuroraClient auroraClient,
			HashMap<Integer, String> orgIdentifiers, 
			Account account, 
			HashMap<Integer, ElementAttributeApplied> accountAttributes,
			Account mandatedAccount,
			BankAccount bankAccount, BankAccount payAwayBankAccount,
			Vector<String> orgCategoryTree,
			HashMap<RelationName, Vector<Person>> relatedAccountPersons,
			boolean emailIndemnityReceived,
			boolean isLocalAuthority) {
		
		super(formId, contactAddress, correspondent, entity, auroraClient, 
		 orgIdentifiers, null, account, accountAttributes, mandatedAccount, 
		 bankAccount, payAwayBankAccount, 
		 orgCategoryTree, relatedAccountPersons, emailIndemnityReceived);
		
		this.isLocalAuthority = isLocalAuthority;
	}

	public void setLocalAuthority(boolean isLocalAuthority) {
		this.isLocalAuthority = isLocalAuthority;
	}

	public boolean isLocalAuthority() {
		return isLocalAuthority;
	}
}
