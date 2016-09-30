package com.ecs.stellent.ccla.clientservices.form;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.AuroraClient;
import com.ecs.ucm.ccla.data.ClientProcess;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Fund;
import com.ecs.ucm.ccla.data.UCMForm;
import com.ecs.ucm.ccla.data.form.UCMFormHandler;
import com.ecs.utils.Log;
import intradoc.common.ServiceException;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.ecs.stellent.spp.service.InstructionDocServices;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Used to handle pre-filled Mandate forms for COIF/CBF
 *  clients.
 * 
 **/
public class MandateFormHandler extends UCMFormHandler {
	
	static final String MAND_DOC_CLASS = "AUTOMAND";
	
	public MandateFormHandler(UCMForm form, String userId, FWFacade facade) {
		Log.debug("Initialized Mandate Form Handler");
		
		this.form = form;
		this.userId = userId;
		this.facade = facade;
	}
	
	/** Actions to perform when the form is initially 
	 *  checked in.
	 *  
	 *  By default, this method will set the returned flag
	 *  and returned date on the form data.
	 * @throws Exception 
	 */
	public void doPostCheckinActions(String docName) 
	 throws Exception {
		
		super.doPostCheckinActions(docName);
		
		form.setAdditionalData(true);
		form.persist(facade, null);
		
		// TM: Removed calls to code below, which fetches and instantiates
		// a set of Accounts related to this form. Won't work until heavy
		// refactoring is done.
		// For the time being, just print the expected mapped accounts to
		// the log.
		DataResultSet rsAccounts = form.getAccountData(facade);
		Log.debug("Found " + rsAccounts.getNumRows() + " linked with Form ID: " 
		 + form.getFormId());
		
		if (rsAccounts.first()) {
			do {
				Log.debug("Account: " + rsAccounts.getStringValueByName("ACCNUMEXT"));
			} while (rsAccounts.next());
		}
			
		// Fetch list of accounts which were displayed on the form.
		Vector<Account> accounts = form.getAccounts(facade);
		
		/*
		 * TODO: Get this working again.
		 */ 
		if (accounts.isEmpty()) {
			Log.warn("No accounts associated with mandate form: " 
			 + form.getFormId());
		} else {
			if (accounts.size() == 1) {
				// Simple case: 1 account listed on Mandate form.
				// No child documents required
				
			} else {
				// Complex case: multiple accounts listed on Mandate form.
				// Add a pending child document entry for each listed account
				
				// First, delete any existing child documents for this item
				InstructionDocServices.deletePendingChildDocs(docName, facade);
				
				for (Account account:accounts) {
					Fund fund = Fund.getCache().getCachedInstance(account.getFundCode());
					
					AuroraClient auroraClient = Entity.getAuroraClientCompanyMapping
					 (form.getOrganisationId(), fund.getCompany(), facade);
							
					// Determine account number string, i.e. the padded account
					// number and fund code.
					String accNumberStr = account.getAccountNumberString();

					InstructionDocServices.addPendingChildDoc(
					 docName, MAND_DOC_CLASS, accNumberStr, null, 
					 CCLAUtils.padClientNumber
					  (Integer.toString(auroraClient.getClientNumber())), 
					 account.getFundCode(), null, facade, this.userId);
				}
			}
		}
		
		// Check the parent process. If all the latest outstanding
		// forms have been returned, update the process status to 
		// 'Forms returned'
		if (form.getProcessId() != null) {
			Log.debug("Checking if latest forms have been returned for " +
			 "process: " + form.getProcessId());
			
			ClientProcess process	= ClientProcess.get
			 (form.getProcessId(), facade);
			
			UCMForm[] forms = ClientProcess.getLatestForms(process, facade);
			
			if (forms != null) {
				Log.debug("Found " + forms.length + " process forms");
				
				boolean formsOutstanding 	= false;
				boolean formReturned 		= false;
				
				for (int i=0; i<forms.length; i++) {
					Log.debug("Form " + forms[i].getFormId() + " returned? " 
					 + forms[i].isReturned());
					
					if (!forms[i].isReturned())
						formsOutstanding = true;
					else
						formReturned = true;
				}
				
				if (!formsOutstanding) {
					Log.debug("All latest process forms marked as Returned. " +
					 "Updating process status to 'Forms returned'");
					
					process.setStatus("Forms returned");
					process.persist(facade, null);
				} else if (formReturned) {
					Log.debug("Some forms still haven't been returned. " + 
					 "Leaving process status as-is.");
							
					// "Updated process status to 'Some forms returned'");
					
					//process.setStatus("Some forms returned");
					//process.persist(facade);
				} else {
					Log.debug("None of the latest forms were returned.");
				}
				
			} else {
				Log.error("Unable to find any forms for process.");
			}
		}
	}
	
	/** Returns the doc meta field mapping which should be applied
	 *  to content items bearing the form ID.
	 *  
	 * @return
	 * @throws DataException 
	 */
	public Hashtable<String, String> getDocMetaMapping() throws DataException {
		
		Hashtable<String, String> map = super.getDocMetaMapping();
		
		// Set document class, based on form sub-type
		if (form.getSubType() != null && 
			form.getSubType().equals("Single account")) {
			map.put("xDocumentClass", MAND_DOC_CLASS);
			
			// If this mandate form only listed one account, account
			// meta-data fields can be pre-filled.
			Vector<Account> accounts = form.getAccounts(facade);
			
			if (!accounts.isEmpty()) {
				Account account = accounts.get(0);
			
				String accNumberStr = account.getAccountNumberString();
				
				map.put("xAccountNumber", accNumberStr);
				map.put("xFund", account.getFundCode());
			}
			
		} else {
			// This form lists multiple accounts. Mark the base item
			// as a MULTIDOC. Child items will be added via the
			// doPostCheckinActions method.
			map.put("xDocumentClass", "MULTIDOC");
			map.put("xMultiDoc", "Yes");
		}
		
		// Set AML metadata flag for all mandates
		map.put("xAMLDocument", "Yes");
		
		return map;
	}
}
