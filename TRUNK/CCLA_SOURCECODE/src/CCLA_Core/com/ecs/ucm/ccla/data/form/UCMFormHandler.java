package com.ecs.ucm.ccla.data.form;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.AuroraClient;
import com.ecs.ucm.ccla.data.ClientProcess;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.UCMForm;
import com.ecs.utils.Log;

import intradoc.common.ParseStringException;
import intradoc.data.DataException;

import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.ecs.utils.stellent.DateFormatter;
import com.ecs.utils.stellent.embedded.FWFacade;

/** 
 *  @deprecated Old Form Handler designed for use with UCMForm instances. 
 *  Form Handlers working with the new schema should use the FormHandler class instead.
 *  
 *  Deals with external actions related to a particular form.
 *  
 *  Each form type/subtype can refer to a custom subclass of
 *  FormHandler.
 *  
 *  The two important methods are:
 *  
 *  -doPostCheckinActions()
 *  -getDocMetaMapping()
 * 
 * @author Tom Marchant
 *
 */
public class UCMFormHandler {
	
	protected UCMForm form;
	protected String userId;
	protected FWFacade facade;
	
	protected UCMFormHandler() {}
	
	public UCMFormHandler(UCMForm form, String userId, FWFacade facade) {
		Log.debug("Initialized default Form Handler");
		
		this.form = form;
		this.userId = userId;
		this.facade = facade;
	}
	
	/**
	 *   This will be executed once for each form instance, after it
	 *   has been returned to CCLA and scanned/faxed into UCM. 
	 *   
	 *   By default, it is used to update form data to indicate when 
	 *   it was returned and the dDocName of the scanned content item.
	 *   
	 *   An activity log is also added to the associated Client Services
	 *   process, indicating the form was returned.
	 *   
	 *   WARNING: this method is called from a update filter event. Be 
	 *   wary to catch any non-critical errors so the calling action does
	 *   not fail.
	 *   
	 * @throws Exception 
	 */
	public void doPostCheckinActions(String docName) 
	 throws Exception {
		
		form.setReturnedDocName(docName);

		form.setStatus("Returned");
		form.setReturnDate(new Date());
		form.setReturned(true);
		
		form.persist(facade, null);
		
		if (form.getProcessId() != null) {
			Log.debug("Adding 'form returned' activity log to process: " 
			 + form.getProcessId());
			
			ClientProcess process = ClientProcess.get
			 (form.getProcessId(), facade);
			
			if (process == null) {
				Log.warn("No matching process found! " +
				 "Unable to append activty log.");
				return;
			} else {
				try {
					process.addActivity(userId, form.getPersonId(), "Form Return", 
					 "Form " + form.getFormId() + " returned", null, 
					 false, facade);
				} catch (Exception e) {
					Log.error("Failed to append activity log", e);
				}
			}	
		}
	}
	
	/** Returns the doc meta field mapping which should be applied
	 *  to content items bearing the form ID.
	 *  
	 *  This is called automatically when checking in a content item
	 *  with an unused form ID. It can also be called via user action
	 *  on the Iris indexing screen.
	 *  
	 *  WARNING: this method is called from a update filter event. Be 
	 *   wary to catch any non-critical errors so the calling action does
	 *   not fail.
	 *  
	 * @return
	 * @throws DataException 
	 */
	public Hashtable<String, String> getDocMetaMapping() throws DataException {
		
		Hashtable<String, String> map = new Hashtable<String, String>();
		
		map.put("xFormId", Integer.toString(form.getFormId()));
		
		// Lookup client data, if applicable
		Entity org 					= form.getOrganisation(facade);
		
		Vector<AuroraClient> auroraClients = 
		 Entity.getAuroraClientMapping(org.getOrganisationId(), facade);
		
		if (!auroraClients.isEmpty()) {
			// Just add data from the first matched AuroraClient.
			addClientDocMeta(org, auroraClients.get(0), map);
		}
		
		// Add correspondent ID if present (not relevant)
		/*
		Integer personId =  form.getPersonId();
		
		if (personId != null)
			map.put("xCorrespondentId", personId.toString());
		else
			map.put("xCorrespondentId", "");
		*/
		
		Date docDate = null;
		
		if (form.getReturnDate() != null) 
			docDate = form.getReturnDate();
		else
			docDate = new Date();

		try {
			map.put("xDocumentDate", 
			 DateFormatter.getTimeStamp("dd/MM/yyyy", docDate));
		} catch (ParseStringException e) {}
		
		return map;
	}
	
	protected static void addClientDocMeta
	 (Entity org, AuroraClient auroraClient, Hashtable<String, String> map) {
		
		if (auroraClient != null) {
			map.put("xCompany", auroraClient.getCompany().getCode());
			map.put("xClientNumber", 
			 CCLAUtils.padClientNumber(
			 Integer.toString(auroraClient.getClientNumber())));
			
			if (org != null)
				map.put("xClientName", org.getOrganisationName());
			else
				map.put("xClientName", "");
			
		} else {
			map.put("xCompany", "");
			map.put("xClientNumber", "");
			map.put("xClientName", "");
		}
	}
}
