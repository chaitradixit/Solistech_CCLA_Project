package com.ecs.stellent.ccla.clientservices.processhandler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.stellent.ccla.clientservices.AMLUtils;
import com.ecs.stellent.ccla.clientservices.form.MandateFormUtils;
import com.ecs.ucm.ccla.data.Campaign;
import com.ecs.ucm.ccla.data.ClientProcess;
import com.ecs.ucm.ccla.data.UCMForm;
import com.ecs.ucm.ccla.data.form.FormUtils;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.embedded.FWFacade;

public class AMLProcessHandler extends ClientProcessHandler {

	/** Mapping of current status to permitted actions */
	protected String[][] statusActions = new String[][] {
		{"*", 					"Cancel",
								"BAU form returned", 
								"Updated mandate returned",
								"Checks completed"},
								
		{"Pending", 			"Generate and print form(s)"},
		
		{"Forms printed",		"Regenerate and print form(s)",
								"Forms returned"},
		
		{"BAU form returned", 	"Generate and print form(s)",
								"Forms returned"},
		
		{"AML check not required", 	"Generate and print form(s)"},
		
		{"Checks completed",	"Regenerate and print form(s)",
								"Forms returned"},
		
		{"Some forms returned", "Forms printed"}
	};
	
	public AMLProcessHandler(Campaign campaign, String userName, FWFacade facade) {
		super(campaign, userName, facade);
	}
	
	public ClientProcess addNewProcess(int entityId,
	 int createMethod, String initialStatus, boolean exclude) 
	 throws DataException, ServiceException {
		
		return super.addNewProcess(entityId, null, createMethod, null, exclude);
	}
	
	/** Returns a list of the available process actions, based on the
	 *  current process status.
	 *  
	 * @param process
	 * @return
	 */
	public Vector<String> getAvailableActions(ClientProcess process) {
		String currentStatus = process.getStatus();
		
		Vector<String> actionList = new Vector<String>();
		
		for (int i=0; i<statusActions.length; i++) {
			String checkStatus = statusActions[i][0];

			if (checkStatus.equals("*") || checkStatus.equals(currentStatus)) {
				for (int j=1; j<statusActions[i].length; j++) {
					actionList.add(statusActions[i][j]);
				}
			}
		}
		
		return actionList;
	}
	
	public void applyAction(ClientProcess process, String action, String note) 
	 throws ServiceException, DataException {
		
		Log.debug("AML Process Handler: applying action: " + action);
		
		if (action.equals("Exclude")) {
			setExcludedAddActivity(process, true, note);
			return;
		} else if (action.equals("Enroll")) {
			setExcludedAddActivity(process, false, note);
			return;
		}
		
		if (!isValidAction(process, action))
			throw new ServiceException("Unable to apply action '" + action + 
			 "' to process ID " + process.getProcessId() + ": not permitted");
	
		if (action.equals("Cancel")) {
			cancelProcess(process);
		
		} else if (action.equals("Forms returned")) {		
			
			setStatus(process, "Forms returned");
			
			process.addActivity(userName, null, 
			 "Forms returned",
			 "All outstanding forms returned", 
			 note, false, facade);
			
		} else if (action.equals("Checks completed")) {	
			// This action can only be executed if all AML accounts
			// have been marked as being 'complete'
			if (!AMLUtils.isAMLAccountsCompleted
				(process.getOrganisationId(), facade)) {
				throw new ServiceException("All AML accounts must be marked " +
				 "as 'complete' before this action can take place.");
			}
			
			setStatus(process, "Checks completed");
			
			process.addActivity(userName, null, 
			 "Checks completed",
			 "AML checks completed for this client", 
			 note, false, facade);
			
		} else if (action.equals("AML check not required")) {
			
			setStatus(process, "AML check not required");
			
			process.addActivity(userName, null, 
			 "Checks not required",
			 "Client marked as not requiring AML check", 
			 note, false, facade);
			
		} else if (action.equals("BAU form returned")) {	
			
			setStatus(process, "BAU form returned");
			
			process.addActivity(userName, null, 
			 "BAU form return",
			 "BAU mandate form returned", 
			 note, false, facade);
		
		} else if (action.equals("Updated mandate returned")) {	
			
			setStatus(process, "Updated mandate returned");
			
			process.addActivity(userName, null, 
			 "Updated mandate return",
			 "Updated mandate form returned", 
			 note, false, facade);
			
		} else if (action.equals("Generate form(s)") || action.equals("Regenerate form(s)")) {
			MandateFormUtils.checkinMandateForms(
			 process.getProcessId(), this.facade, this.userName);
			
			setStatus(process, "Forms generated");
		} else if (action.equals("Print form(s)") || action.equals("Reprint form(s)")) {
			
			// Print off all the latest forms.
			UCMForm[] forms = ClientProcess.getForms(process, facade);
			
			HashMap<String, Integer> latestForms = new HashMap<String, Integer>();
			
			for (int i=0; i<forms.length; i++) {
				String formDocName = forms[i].getBaseDocName();
				
				if (latestForms.containsKey(formDocName)) {
					// do nothing if this form document is already collected
				} else {
					latestForms.put(formDocName, new Integer(forms[i].getFormId()));
				}
			}
			
			Iterator<Integer> formIds = latestForms.values().iterator();
			
			while (formIds.hasNext()) {
				FormUtils.printForm(formIds.next(), null, this.userName, this.facade);
			}
			
			setStatus(process, "Forms printed");
		} else if (action.equals("Generate and print form(s)") || 
				   action.equals("Regenerate and print form(s)")) {
			
			// Generate and print all required forms (do not check in spool file)
			MandateFormUtils.createAndPrintMandateForms
			 (process, facade, userName);
			
			setStatus(process, "Forms printed");
		
		} else if (action.equals("Forms printed")) {
			
			setStatus(process, "Forms printed");
			
			process.addActivity(userName, null, 
			 "Status update",
			 "Status updated to Forms Printed", 
			 note, false, facade);
			
		} else {
			throw new ServiceException("Unsupported action: " + action);
		}
	}
}
