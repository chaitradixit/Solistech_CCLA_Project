package com.ecs.stellent.ccla.clientservices;

import java.util.Hashtable;
import java.util.Vector;

import com.ecs.stellent.ccla.clientservices.form.FundTransferFormUtils;
import com.ecs.stellent.ccla.clientservices.form.MandateFormUtils;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals;
import com.ecs.ucm.ccla.Globals.UCMFieldNames;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.ClientProcess;
import com.ecs.ucm.ccla.data.Element;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.UCMForm;
import com.ecs.ucm.ccla.data.campaign.CampaignActivityType;
import com.ecs.ucm.ccla.data.campaign.CampaignEnrolment;
import com.ecs.ucm.ccla.data.form.Form;
import com.ecs.ucm.ccla.data.form.FormConfig;
import com.ecs.ucm.ccla.data.form.FormHandler;
import com.ecs.ucm.ccla.data.form.FormType;
import com.ecs.ucm.ccla.data.form.FormUtils;
import com.ecs.ucm.ccla.data.form.UCMFormHandler;
import com.ecs.ucm.ccla.data.form.Form.FormStatus;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.embedded.FWFacade;

import intradoc.common.ServiceException;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.server.Service;

/** Service methods relating to auto-generated form data and their
 *  associated content.
 *  
 * @author Tom Marchant
 *
 */
public class FormService extends Service {
	
	/** For a given formId, fetches the associated row from the CCLA_FORMS
	 *  table and adds to the binder as rsForm. 
	 *  
	 *  Any associated accounts are fetched from the CCLA_FORM_ACCOUNT_MAP 
	 *  table and added to the binder as rsFormAccounts.
	 *  
	 *  @throws DataException		if the passed formId is non-numeric
	 *  @throws ServiceException 	if the associated form does not exist
	 */
	public void getFormInfo() throws DataException, ServiceException {
	
		Integer formId = CCLAUtils.getBinderIntegerValue
		 (m_binder, "formId");
		
		if (formId == null)
			throw new ServiceException("formId parameter not found");
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		UCMForm form = UCMForm.get(formId, facade);
		
		if (form == null)
			throw new ServiceException("Form with ID " + 
			 formId + " not found");
		
		m_binder.addResultSet("rsForm", form.getData());
		
		DataResultSet rsAccounts = form.getAccountData(facade);
		m_binder.addResultSet("rsFormAccounts", rsAccounts);
	}
	
	/** Blanks out the Returned Doc ID (RET_DOC_GUID) and Return Date on the given
	 *  form, so it can be indexed against a different document.
	 *  
	 *  The Form ID field value is also removed from the associated document, if present.
	 * 
	 * @throws DataException 
	 * @throws ServiceException if the form didn't have a Returned Doc ID set.
	 */
	public void unlinkFormDocument() throws DataException, ServiceException {
		
		Integer formId = CCLAUtils.getBinderIntegerValue(m_binder, Form.Cols.ID);
		
		if (formId == null) {
			throw new DataException("Unable to unlink form doc: " + Form.Cols.ID +
			 " missing");
		}
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		Form form = Form.get(formId, facade);
		
		String retDocGUID = form.getRetDocGuid();
		
		if (retDocGUID == null)
			throw new ServiceException("Unable to unlink form doc: " +
			 "no Returned Doc ID set");
		
		Log.debug("Removing Returned Doc ID from Form ID: " + form.getFormId() + ". " +
		 "Currently set to " + retDocGUID);
		
		form.setRetDocGuid(null);
		form.setDateReturned(null);
		
		try {
			LWDocument lwDoc = CCLAUtils.getLatestLwdFromDocGuid(retDocGUID);
			
			if (lwDoc != null) {
				String docFormIdStr = lwDoc.getAttribute(UCMFieldNames.FormID);
				
				Log.debug("Found form ID value of " + docFormIdStr + " against the " +
				 "returned form document");
				
				if (docFormIdStr != null) {
					Integer docFormId = new Integer(docFormIdStr);
					
					if (docFormId == form.getFormId()) {
						// Matching Form IDs. Remove from document.
						Log.debug("Matched the Form ID - removing from document");
						lwDoc.setAttribute(UCMFieldNames.FormID, "");
						lwDoc.update();
					}
				}
			} else {
				Log.warn("Unable to find correspondent document for GUID: " + retDocGUID);
			}
			
		} catch (Exception e) {
			String msg = "Failed to access/update content item with GUID: " + retDocGUID
			 + ": " + e.getMessage();
			
			Log.error(msg,e);
			throw new ServiceException(msg, e);
		}
		
		Log.debug("Successfully unlinked Returned Doc ID from Form ID "
		 + form.getFormId());
		
		form.persist(facade, m_userData.m_name);
	}
	
	/** Service method for setting the status value for a Form record.
	 * 
	 * @throws DataException 
	 */
	public void updateFormStatus() throws DataException {
		
		Integer formId = CCLAUtils.getBinderIntegerValue(m_binder, Form.Cols.ID);
		Integer newStatusId = CCLAUtils.getBinderIntegerValue
		 (m_binder, Form.Cols.FORM_STATUS);
		
		if (formId == null || newStatusId == null) {
			throw new DataException("Unable to update form status: " + Form.Cols.ID +
			 " or " + Form.Cols.FORM_STATUS + " missing");
		}
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		Form form = Form.get(formId, facade);
		
		Log.debug("Setting Form ID " + form.getFormId() + " status to Status ID " +
				 newStatusId);
		
		if (form.getFormStatusId().compareTo(newStatusId) == 0) {
			Log.debug("No change in status value, skipping update.");
			return;
		}		
		
		try {
			facade.beginTransaction();
			
			form.setFormStatusId(newStatusId);
			form.persist(facade, m_userData.m_name);
			
			// Add audit entry to Enrolment
			if (form.getCampaignEnrolmentId() != null) {
				CampaignEnrolment enrolment = CampaignEnrolment.get
				 (form.getCampaignEnrolmentId(), facade);
				
				if (newStatusId == Form.FormStatus.CANCELLED.id) {
					// Form has been cancelled.
					enrolment.addActivity(null, 
					 CampaignActivityType.FORM_CANCELLED_ID, 
					 "Form ID " + form.getFormId() + " marked as Cancelled", null, 
					 facade, m_userData.m_name);
				} else {
					// Generic form update message.
					enrolment.addActivity(null, 
					 CampaignActivityType.FORM_STATUS_UDPATE_ID, 
					 "Form ID " + form.getFormId() + " status updated (Status ID " 
					 + newStatusId + ")", null, 
					 facade, m_userData.m_name);
				}
			}
				
			facade.commitTransaction();
			
		} catch (Exception e) {
			facade.rollbackTransaction();
			
			String msg = "Failed to update form status: " + e.getMessage();
			Log.error(msg, e);
			throw new DataException(msg, e);
		}
	}
	
	/** Legacy method - updates the status value for an old UCM form.
	 *  
	 *  Adds an activity log to the parent process, if applicable.
	 *  
	 * @throws ServiceException 
	 * @throws DataException 
	 */
	public void updateUCMFormStatus() throws ServiceException, DataException {
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		Integer formId = null;
		
		try {
			formId = CCLAUtils.getBinderIntegerValue(m_binder, "formId");
		} catch (DataException e) {
			throw new ServiceException("Invalid form ID - must be numeric");
		}
		
		UCMForm form = UCMForm.get(formId, facade);
			
		if (form == null)
			throw new ServiceException("No form found with ID: " + formId);
		
		String status = m_binder.getLocal("formStatus");
		
		if (StringUtils.stringIsBlank(status))
			throw new ServiceException("Passed form status was empty/null");
		
		try {
			facade.beginTransaction();
			
			form.setStatus(status);
			// user needed for audit table
			String username = m_userData.m_name;
			m_binder.putLocal("USER_ID", username);			
			form.persist(facade, username);
			
			// Now add activity log indicating status change.
			Integer processId = form.getProcessId();
		
			if (processId != null) {
				ClientProcess process = ClientProcess.get(processId, facade);
					
				process.addActivity(m_userData.m_name, form.getPersonId(), "Form Update", 
				 "Form status updated to '" + status + "'", null, false, facade);
			}
			
			facade.commitTransaction();
		} catch (Exception e) {
			facade.rollbackTransaction();
			String msg = "Failed to update form status";
		
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}
	
	/** Updates the IS_VALID flag field for a given FORM_ID.
	 *  
	 *  User will check a 'formInvalid' checkbox to mark the form as invalid.
	 *  
	 * @throws ServiceException 
	 * @throws DataException 
	 */
	public void updateFormValidityState() throws ServiceException, DataException {
		
		Integer formId = CCLAUtils.getBinderIntegerValue(m_binder, Form.Cols.ID);
		boolean formInvalid = CCLAUtils.getBinderBoolValue(m_binder, "formInvalid");
		
		if (formId == null)
			throw new ServiceException("No Form ID passed");
		
		FWFacade facade = CCLAUtils.getFacade(true);
		Form form = Form.get(formId, facade);
		
		if (form == null)
			throw new ServiceException("No Form found with ID " + formId);
		
		form.setValid(!formInvalid);
		form.persist(facade, m_userData.m_name);
	}
	
	/** Updates the Validity/Required Updates flags for the passed formId (legacy forms
	 *  only!)
	 *  
	 * @throws ServiceException 
	 * @throws DataException 
	 */
	public void updateFormDataFlags() throws ServiceException, DataException {
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		Integer formId = null;
		
		try {
			formId = CCLAUtils.getBinderIntegerValue(m_binder, "formId");
		} catch (DataException e) {
			throw new ServiceException("Invalid form ID - must be numeric");
		}
		
		UCMForm form = UCMForm.get(formId, facade);
		
		if (form == null)
			throw new ServiceException("No form found with ID: " + formId);
		
		try {
			facade.beginTransaction();
			
			// Validity is essentially stored as a boolean string (0/1), although
			// it can also be null.
			String validity = m_binder.getLocal("valid");
			Boolean valid 	= null;
			
			if (!StringUtils.stringIsBlank(validity))
				valid = (validity.equals("1"));
		
			form.setValid(valid);
			
			String invalidReason = m_binder.getLocal("invalidReason");
			form.setInvalidReason(invalidReason);
			
			// Required Update flags are all stored as boolean strings
			boolean additionalData = !StringUtils.stringIsBlank(
			 m_binder.getLocal("additionalData"));
			
			boolean staticDataChange = !StringUtils.stringIsBlank(
			 m_binder.getLocal("staticDataChange"));
			
			boolean noChanges = !StringUtils.stringIsBlank(
			 m_binder.getLocal("noChanges"));
			
			form.setAdditionalData(additionalData);
			form.setStaticDataChange(staticDataChange);
			form.setNoChanges(noChanges);
			
			form.persist(facade, null);
			
			// Now add activity log indicating status change.
			Integer processId = form.getProcessId();
			
			if (processId != null) {
				ClientProcess process = ClientProcess.get(processId, facade);
				
				process.addActivity(m_userData.m_name, form.getPersonId(), "Form Update", 
				 "Form " + form.getFormId() + " updated", "Valid=" + valid + 
				 "\nNo Changes=" + noChanges + "\nAdditional Data=" + additionalData + 
				 "\nStatic Data Change=" + staticDataChange, false, facade);
			}
			
			facade.commitTransaction();
			
		} catch (Exception e) {
			facade.rollbackTransaction();
			
			String msg = "Failed to update form data flags: " + e.getMessage();
			
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}
	
	/** Service method for fetching form data suitable for metadata 
	 *  auto-completion.
     *  
     *  Generally called via AJAX to auto-complete field values
     *  on the fly.
     *  
     *  Similar to the older getFormData() method, except this one
     *  performs extra checks to ensure the form ID isn't already
     *  being used on another document.
     *  
	 * @throws ServiceException
	 * @throws DataException
	 * @throws ServiceException 
	 */
	public void getDocMetaByFormId() throws DataException, ServiceException {
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		Integer formId = null;
		
		try {
			formId = CCLAUtils.getBinderIntegerValue(m_binder, "formId");
		} catch (DataException e) {
			throw new ServiceException("Invalid form ID - must be numeric");
		}
		
		String docName 	= m_binder.getLocal("docName");
		
		if (StringUtils.stringIsBlank(docName))
			throw new ServiceException
			 ("Unable to lookup form data - docName required.");
		
		String docIdStr	= m_binder.getLocal("docId");
		
		if (StringUtils.stringIsBlank(docIdStr))
			throw new ServiceException
			 ("Unable to lookup form data - docId required.");
		
		int docId = Integer.parseInt(docIdStr);
		
		// Check new forms table first.
		boolean foundForm = getDocMetaFromFormId(formId, docId, facade);
		
		if (!foundForm) // Check legacy forms table.
			foundForm = getDocMetaFromLegacyFormId(formId, docName, facade);
		
		if (!foundForm)
			throw new ServiceException("No form data found for ID " + formId);
	}
	
	/** Attempt to fetch doc meta from the new FORM table.
	 * 
	 * @param formId
	 * @param docName
	 * @param facade
	 * @return
	 * @throws DataException
	 * @throws ServiceException
	 */
	private boolean getDocMetaFromFormId(int formId, int docId, FWFacade facade)
	 throws DataException, ServiceException {
		
		DataResultSet rsForm = Form.getData(formId, facade);
		Form form  = Form.get(rsForm);
		
		if (form == null)
			return false;
		
		m_binder.addResultSet("rsForm", rsForm);
		
		boolean singleton = form.getFormType().isSingleton();
		CCLAUtils.addQueryBooleanParamToBinder(m_binder, "formIsSingleton", singleton);
		
		if (singleton) {
			// If this is a 'singleton' form type, throw an error if it has already
			// been assigned against a different document.
			String docGUID = CCLAUtils.getDocGuidFromDid(docId);
			
			if (form.getRetDocGuid() != null && !form.getRetDocGuid().equals(docGUID)) {
				// Determine the previously-returned form's Doc GUID for the error
				// message
				throw new ServiceException("This form ID has already been allocated to " +
				 form.getRetDocGuid());
			}
		}
		
		FormHandler handler = form.getFormType().getHandlerInstance
		 (form, m_userData.m_name, facade);
		
		// Fetch doc meta mapping and add to DataBinder as ResultSet
		Hashtable<String, String> docMeta = handler.getDocMetaMapping();
		
		DataResultSet rs = CCLAUtils.getResultSetFromMap(docMeta);
		m_binder.addResultSet("rsFormMeta", rs);
		
		handler.addExtraBinderParams(m_binder);
		handler.addFormMessagesToBinder(m_binder);
		
		return true;
	}
	
	/** Attempt to fetch doc meta from the legacy CCLA_FORM table.
	 * 
	 * @param formId
	 * @param docName
	 * @param facade
	 * @return
	 * @throws DataException
	 * @throws ServiceException
	 */
	private boolean getDocMetaFromLegacyFormId
	 (int formId, String docName, FWFacade facade)
	 throws DataException, ServiceException {
	
		UCMForm form = UCMForm.get(formId, facade);
		
		if (form == null) {
			return false;
		}	
		
		if (!StringUtils.stringIsBlank(form.getReturnedDocName()) &&
			!form.getReturnedDocName().equals(docName)) {
		
			throw new ServiceException("This form ID has already been allocated to " +
			 form.getReturnedDocName());
		}
		
		FormConfig config	= FormUtils.getFormConfig(form, facade);
		
		UCMFormHandler handler = FormUtils.getFormHandler
		 (form, config.getHandlerClass(), m_userData.m_name, facade);
		
		// Fetch doc meta mapping and add to DataBinder as ResultSet
		Hashtable<String, String> docMeta = handler.getDocMetaMapping();
		
		DataResultSet rs = CCLAUtils.getResultSetFromMap(docMeta);
		m_binder.addResultSet("rsFormMeta", rs);
		
		return true;
	}
	
	/** Prints a CreateForm form, using spool file content associated
	 *  with the formId in the binder.
	 *  
	 * @throws ServiceException if formId is not present
	 * @throws DataException
	 */
	public void printForm() throws ServiceException, DataException {
		
		String formIdStr = m_binder.getLocal("formId");
		
		if (StringUtils.stringIsBlank(formIdStr)) {
			// Look for direct filename instead.
			String fileName	 = m_binder.getLocal("fileName");
			
			if (!StringUtils.stringIsBlank(fileName)) {
				FormUtils.printForm(fileName);
			} else{
				throw new ServiceException("Unable to print form: " +
				 "formId and fileName not present.");
			}
		} else {
			int formId = Integer.parseInt(formIdStr);
			
			FormUtils.printForm(formId, null, m_userData.m_name, 
			 CCLAUtils.getFacade(m_workspace, true));
		}
	}
	
	/** Prints a set of CreateForm forms from a single spool file. The spool
	 *  file will be the primary file for the docName present in the binder.
	 *  
	 * @throws ServiceException if formId is not present
	 * @throws DataException
	 */
	public void printForms() throws ServiceException, DataException {
		
		String docName = m_binder.getLocal("docName");
		
		throw new ServiceException("Not implemented yet.");
	}
	
	/** Creates and checks in pre-filled Mandate forms for a given client. 
	 *  This requires creation of spool files and associated Create!Form PDF 
	 *  - these are checked in as the primary/web renditions respectively. 
	 *  
	 *  Currently the Create!Form PDF rendition is unavailable.
	 *  
	 *  If a form already exists with the derived content ID, it will be up-
	 *  revisioned.
	 *  
	 *  A processId must be present in the binder, this is assumed to relate
	 *  to a Client Services process ID. The associated process will be looked
	 *  up to determine the company/correspondent ID combo required to build
	 *  the form.
	 *  
	 * @throws ServiceException
	 */
	public void checkinMandateForms() throws ServiceException, 
	 DataException {
		
		String processId		= m_binder.getLocal("processId");
		
		if (StringUtils.stringIsBlank(processId))
			throw new ServiceException("Unable to checkin form: " +
			 "processId missing.");

		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		MandateFormUtils.checkinMandateForms(Integer.parseInt(processId), 
		 facade, m_userData.m_name);
	}
	
	/** Checks in a new Ethical Fund form for the given processId and prints
	 *  it.
	 */
	public void createAndPrintEthicalFundForm() throws ServiceException {
		
		String processId = m_binder.getLocal("processId");
		
		try {
			FundTransferFormUtils.checkinEthicalFundForm(m_binder, 
			 CCLAUtils.getFacade(m_workspace, true), m_userData.m_name);
			//printEthicalFundForm();
		} catch (Exception e) {
			String msg = "Failed to create/print Ethical Fund form for " +
			 "process ID: " + processId;
			
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}
	
	
	/** Service method for fetching a list of available Form Types that can be generated
	 *  for the given ELEMENT_ID in the binder.
	 *  
	 * @throws ServiceException 
	 * @throws DataException 
	 * @throws NumberFormatException 
	 *  
	 */
	public void getAvailableElementFormTypes() throws ServiceException, DataException {
		
		String elementIdStr = m_binder.getLocal("ELEMENT_ID");
		
		if (StringUtils.stringIsBlank(elementIdStr))
			throw new ServiceException("ELEMENT_ID missing");
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		DataResultSet rsElement = Element.getData
		 (Integer.parseInt(elementIdStr), facade);
		
		Element elem = Element.get(rsElement);
		
		if (elem == null)
			throw new ServiceException("No Element found with ID " + elementIdStr);
		
		m_binder.addResultSet("rsElement", rsElement);
		m_binder.putLocal(ElementType.Cols.NAME, elem.getType().getName());
		
		// If this is an Account element, add details for the owning Org element as well
		if (elem.getType().equals(ElementType.ACCOUNT)) {
			int orgId = Account.getOwnerOrganisationId(elem.getElementId(), facade);
			CCLAUtils.addQueryIntParamToBinder(m_binder, Entity.Cols.ID, orgId);
		}
		
		/*
		DataResultSet rsFormTypes = FormType.getDataByElementType
		 (elem.getType(), facade);
		
		
		m_binder.addResultSet("rsFormTypes", rsFormTypes);
		*/
	}
	
	/** Generates a set of forms for the given Form Type IDs and Element ID.
	 * 
	 * @throws DataException 
	 * @throws ServiceException 
	 * 
	 */
	public void generateFormsByElementId() throws DataException, ServiceException {
		
		Integer elemId 			= CCLAUtils.getBinderIntegerValue
		 (m_binder, Element.Cols.ID);
		String formTypeIdList	= m_binder.getLocal("formTypeIds");
		
		if (elemId == null || formTypeIdList == null)
			throw new DataException("Unable to generate form: " + Element.Cols.ID + 
			 " or formTypeIds missing");
		
		// Convert CSV into vector of form type IDs
		Vector<Integer> formTypeIds = CCLAUtils.getIntegerList(formTypeIdList);
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		Log.debug("Found " + formTypeIds.size() + " form type IDs");
		
		int generatedForms = 0;
		
		try {
			facade.beginTransaction();
			
			for (Integer formTypeId : formTypeIds) {	
				FormType formType = FormType.getCache().getCachedInstance(formTypeId);
				Element element = Element.get(elemId, facade);
				
				Log.debug("Generating form type '" + formType.getName() + 
				 "' for element ID: " + elemId);
				
				FormHandler frmHandler = formType.getHandlerInstance
				 (m_userData.m_name, facade);
			
				frmHandler.generateForm(formType, element);
			
				Log.debug("Successfully generated form type '" + formType.getName() + 
				 "' for element ID: " + elemId);
				
				generatedForms++;
			}
			
			facade.commitTransaction();

			CCLAUtils.appendToBinderParam(m_binder, "RedirectUrl", 
			 "&numFormsGenerated=" + generatedForms);
			
		} catch (Exception e) {
			facade.rollbackTransaction();
			
			String msg = "Failed to generate forms: " + e.getMessage();
			
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}
	
	public void bulkGenerateAndPrintForms() throws DataException {
		Integer formTypeId = CCLAUtils.getBinderIntegerValue
		 (m_binder, FormType.Cols.ID);
		
		FormType formType = FormType.getCache().getCachedInstance(formTypeId);
		
		Log.debug("Bulk generating forms of type " + formType.getName());
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
	
		String sql = m_binder.getLocal("queryText");
		DataResultSet rsElements = facade.createResultSetSQL(sql);
		
		Log.debug("Matched " + rsElements.getNumRows() + 
		 " results from supplied query text: " + sql);
		
		if (rsElements.first()) {
			do {
				Integer elementId = CCLAUtils.getResultSetIntegerValue
				 (rsElements, Element.Cols.ID);
				
				try {
					FormHandler formHandler =
					 formType.getHandlerInstance(m_userData.m_name, facade);
					
					Element elem = Element.get(elementId, facade);
					
					if (elem != null)
						formHandler.generateForm(formType, elem);
					
				} catch (Exception e) {
					Log.error("Failed to generate form for Element ID: " + elementId, e);
				}
			} while (rsElements.next());
		}
	}
}
