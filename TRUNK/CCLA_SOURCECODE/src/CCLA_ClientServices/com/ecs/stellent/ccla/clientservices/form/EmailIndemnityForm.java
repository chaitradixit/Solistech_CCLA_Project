package com.ecs.stellent.ccla.clientservices.form;

import java.util.Vector;

import com.ecs.ucm.ccla.data.AuroraClient;
import com.ecs.ucm.ccla.data.Contact;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.form.Form;

public class EmailIndemnityForm {
	
	/** Form data object. */
	private Form form;
	
	private Entity organisation;
	private AuroraClient auroraClient;
	
	/** List of email addresses that will be printed (max of 3) */
	private Vector<Contact> emailAddresses;
	
	/** Number of authorising signature sections that will be printed */
	private int numSignatories;
	
	private String campaign;

	public EmailIndemnityForm(Form form, Entity organisation,
			AuroraClient auroraClient, Vector<Contact> emailAddresses,
			int numSignatories, String campaign) {
		this.form = form;
		this.organisation = organisation;
		this.auroraClient = auroraClient;
		this.emailAddresses = emailAddresses;
		this.numSignatories = numSignatories;
		this.setCampaign(campaign);
	}
	
	public Form getForm() {
		return form;
	}
	
	public int getFormId() {
		return form.getFormId();
	}

	public Entity getOrganisation() {
		return organisation;
	}

	public Vector<Contact> getEmailAddresses() {
		return emailAddresses;
	}

	public int getNumSignatories() {
		return numSignatories;
	}

	public AuroraClient getAuroraClient() {
		return auroraClient;
	}

	public void setCampaign(String campaign) {
		this.campaign = campaign;
	}

	public String getCampaign() {
		return campaign;
	}
}
