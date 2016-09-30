package com.ecs.stellent.ccla.clientservices.filter;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals.UCMFieldNames;
import com.ecs.ucm.ccla.data.UCMForm;
import com.ecs.ucm.ccla.data.form.Form;
import com.ecs.ucm.ccla.data.form.FormConfig;
import com.ecs.ucm.ccla.data.form.FormHandler;
import com.ecs.ucm.ccla.data.form.FormUtils;
import com.ecs.ucm.ccla.data.form.UCMFormHandler;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ExecutionContext;
import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.Workspace;
import intradoc.shared.FilterImplementor;

public class ServiceSuccessFilter implements FilterImplementor {
	
	/** Called via checkin filter event onEndServiceRequestActions
	 * 
	 *  Used to apply post-processing actions after a Client Services
	 *  form is checked in.
	 *  
	 */
	public int doFilter(Workspace workspace, DataBinder binder, ExecutionContext exc)
	 throws DataException, ServiceException {
		
		// Check for special flag indicating that post-processing actions are
		// required for the form tagged to this document.
		String doFormPostCheckin = binder.getLocal("csFormDoPostCheckinActions");
		if (!(doFormPostCheckin != null && doFormPostCheckin.equals("1")))
			return FilterImplementor.CONTINUE;
		
		long startTime = System.currentTimeMillis();
		
		String formIdStr 	= binder.getLocal(UCMFieldNames.FormID);
		Integer formId		= null;
		
		String docName 		= binder.getLocal(UCMFieldNames.DocName);
		Integer docId		= CCLAUtils.getBinderIntegerValue
								(binder, UCMFieldNames.DocID);
		
		Log.debug("CCLA_ClientServices ServiceSuccessFilter started");
		
		if (StringUtils.stringIsBlank(formIdStr)) {
			Log.warn("doFormPostCheckin set, but no xFormId exists in the binder");
			return FilterImplementor.CONTINUE;
		}
		
		if (StringUtils.stringIsBlank(docName)) {
			Log.warn("doFormPostCheckin set, but no dDocName exists in the binder");
			return FilterImplementor.CONTINUE;
		}
		
		Log.debug("doFormPostCheckin flag set. Performing post-checkin actions.");
		
		try {
			formId = CCLAUtils.getBinderIntegerValue
			 (binder, "xFormId");
			
		} catch (DataException e) {
			Log.warn("Item checked in with non-numeric form ID: '" + formIdStr 
			 + "', unable to apply post-checkin actions.");
			
			return FilterImplementor.CONTINUE;
		}
		
		FWFacade facade = null;
		
		try {
			facade = CCLAUtils.getFacade(workspace, true);
		} catch (DataException e) {
			Log.error("Unable to create Facade for form data lookup",e);
			return FilterImplementor.CONTINUE;
		}
		
		String userId = binder.getLocal("dUser");
		
		// Fetch the associated form instance and update the
		// returned date, returned docName and status
		
		boolean foundForm = false;
		
		Form form = Form.get(formId, facade);
		
		if (form != null) {
			foundForm = true;

			FormHandler handler = form.getFormType().getHandlerInstance
			 (form, userId, facade);
			
			try {
				Log.debug("Applying post checkin actions for form ID: " + formId + 
				 ", document: " + docName);
				
				handler.doPostCheckinActions(docId);
			} catch (Exception e) {
				Log.error("Failed to apply form post-checkin actions.", e);
				return FilterImplementor.CONTINUE;
			}
		}
		
		if (!foundForm) {
			// Check legacy form table
			UCMForm ucmForm = UCMForm.get(formId, facade);
			
			if (ucmForm != null) {
				FormConfig config 		= FormUtils.getFormConfig(ucmForm, facade);
				UCMFormHandler formHandler	= FormUtils.getFormHandler
				 (ucmForm, config.getHandlerClass(), userId, facade);
				
				try {
					Log.debug("Applying post checkin actions for form ID: " + formId + 
					 ", document: " + docName);
					formHandler.doPostCheckinActions(docName);
				} catch (Exception e) {
					Log.error("Failed to apply form post-checkin actions.", e);
					return FilterImplementor.CONTINUE;
				}
			}
		}
		
		// Reset form check-in flag
		binder.putLocal("csFormDoPostCheckinActions", "");
		
		long endTime = System.currentTimeMillis();
		
		Log.debug("ClientServices ServiceSuccessFilter completed, dDocName: "
		 + binder.getLocal(UCMFieldNames.DocName) + ", time taken: " + 
		 (endTime-startTime)/1000D + "s");
		
		return FilterImplementor.CONTINUE;
	}

}
