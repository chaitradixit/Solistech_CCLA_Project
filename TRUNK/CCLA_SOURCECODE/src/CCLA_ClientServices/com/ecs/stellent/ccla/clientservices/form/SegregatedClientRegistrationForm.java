package com.ecs.stellent.ccla.clientservices.form;

import java.util.HashMap;
import java.util.Vector;

import com.ecs.ucm.ccla.data.AuroraClient;
import com.ecs.ucm.ccla.data.BankAccount;
import com.ecs.ucm.ccla.data.Contact;
import com.ecs.ucm.ccla.data.ElementAttributeApplied;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.RelationName;
import com.ecs.ucm.ccla.data.campaign.EnrolmentAttributeApplied;

public class SegregatedClientRegistrationForm extends ClientRegistrationForm {

	public SegregatedClientRegistrationForm(int formId, Contact contactAddress,
			Person correspondent, Entity entity, AuroraClient auroraClient,
			HashMap<Integer, String> orgIdentifiers,
			HashMap<Integer, ElementAttributeApplied> orgAttributes,
			BankAccount bankAccount, Vector<String> orgCategoryTree,
			HashMap<RelationName, Vector<Person>> relatedOrgPersons,
			boolean emailIndemnityReceived,
			HashMap<Integer, EnrolmentAttributeApplied> enrolmentAttributes) {

		super(formId, contactAddress, correspondent, entity, auroraClient,
				orgIdentifiers, orgAttributes, null, bankAccount, orgCategoryTree,
				relatedOrgPersons, emailIndemnityReceived, enrolmentAttributes, null);
		// TODO Auto-generated constructor stub
	}

}
