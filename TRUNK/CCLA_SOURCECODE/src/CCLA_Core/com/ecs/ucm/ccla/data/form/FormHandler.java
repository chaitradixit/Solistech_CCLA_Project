package com.ecs.ucm.ccla.data.form;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals;
import com.ecs.ucm.ccla.Globals.UCMFieldNames;
import com.ecs.ucm.ccla.data.AuroraClient;
import com.ecs.ucm.ccla.data.DocumentClass;
import com.ecs.ucm.ccla.data.Element;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.campaign.CampaignActivityType;
import com.ecs.ucm.ccla.data.campaign.CampaignEnrolment;
import com.ecs.ucm.ccla.data.form.Form.FormStatus;
import com.ecs.ucm.ccla.data.subscription.Subscription;
import com.ecs.utils.Log;

import intradoc.common.ParseStringException;
import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.ecs.utils.stellent.DateFormatter;
import com.ecs.utils.stellent.embedded.FWFacade;

/** 
 *  Deals with external actions related to a particular form. Also used to generate new
 *  Form instances.
 *  
 *  Each Form Type can refer to a custom subclass of FormHandler.
 *  
 *  The important methods are:
 *  
 *  -generateForm()
 *  -doPostCheckinActions()
 *  -getDocMetaMapping()
 * 
 * @author Tom Marchant
 *
 */
public abstract class FormHandler {
	
	protected Form form;
	protected String userId;
	protected FWFacade facade;

	/** Stores form messages during execution of form handler methods.
	 *  
	 *  These are inserted to a DataBinder via the addFormMessagesToBinder
	 *  method.
	 */
	protected Vector<FormMessage> messages = new Vector<FormMessage>();
	
	/** Stores the rolling validity level during execution of form handler methods. */
	private ValidityLevel validityLevel = ValidityLevel.VALID;
	
	/** Sets the validity level, only if it is higher than the current level!
	 *
	 * @param level new level to set.
	 */
	protected void setValidityLevel(ValidityLevel level) {
		if (this.validityLevel.rank < level.rank)
			this.validityLevel = level;
	}
	
	protected ValidityLevel getValidatyLevel() {
		return this.validityLevel;
	}
	
	public enum ValidityLevel {
		VALID (1),
		WARNING (2),
		ERROR (3);
		
		public int rank;
		
		ValidityLevel(int rank) {
			this.rank = rank;
		}
	}
	
	protected FormHandler() {}
	
	/** Add values for these strings to the DataBinder via addExtraBinderParams, to
	 *  display a custom URL in the Iris indexing interface.
	 *  
	 * @author tm
	 *
	 */
	public static final class FormLinkBinderStrings {
		public static final String URL = "CUSTOM_FORM_LINK_URL";
		public static final String LABEL = "CUSTOM_FORM_LINK_LABEL";
	}
	
	
	/** Available return message types that will be displayed on the Iris indexing
	 *  interface.
	 */
	public static enum MessageType {
		INFO (1, ValidityLevel.VALID, "Info"),
		WARNING (2, ValidityLevel.WARNING, "Warning"),
		ERROR (3, ValidityLevel.ERROR, "Error");
		
		/** Default message header if one isn't supplied. */
		public String defaultHeader;
		/** Associated validity level for this message type. */
		public ValidityLevel validityLevel;
		public int rank;
		
		MessageType(int rank, ValidityLevel validityLevel, String defaultHeader) {
			this.rank = rank;
			this.validityLevel = validityLevel;
			this.defaultHeader = defaultHeader;
		}
	}
	
	/** Wrapper class for a Form Message that will be returned and displayed on the
	 *  indexing interface. */
	public static class FormMessage {
		MessageType type;
		String header;
		String body;
		
		public FormMessage(MessageType type, String header, String body) {
			this.type = type;
			this.header = header;
			this.body = body;
		}
		
		public String toString() {
			return "\nFormMessage[Type=" + this.type.toString() + "]" +
			 "\nHeader: " + this.header +
			 "\nBody: " + this.body;
		}
		
		/** Adds the message parts to the DataBinder, providing there isn't a higher-
		 *  ranked message already in there!
		 *  
		 *  Will overwrite a lower-ranked or similar-ranked message.
		 *  
		 * @param binder
		 * @return
		 */
		/*
		public boolean addToBinder(DataBinder binder) {
			
			String currTypeStr = binder.getLocal(FormMessageStrings.TYPE);
			if (currTypeStr != null) {
				MessageType currType = MessageType.valueOf(currTypeStr);
				
				if (currType != null && currType.rank >= this.type.rank) {
					// Similar/Higher-ranked message already in binder!
					return false;
				}
			}
			
			binder.putLocal(FormMessageStrings.TYPE, this.type.toString());
			binder.putLocal(FormMessageStrings.HEADER, 
			 this.header != null ? this.header : this.type.defaultHeader);
			
			if (this.body != null)
				binder.putLocal(FormMessageStrings.BODY, body);
			
			return true;
		}
		*/
	}
	
	/** Used to support multiple form messages.
	 * 
	 *  Not used yet - only 1 returned message is supported.
	 *  
	 * @param messages
	 * @return
	 */
	private DataResultSet createFormMessageResultSet() {
		DataResultSet rs = new DataResultSet(FORM_MESSAGE_RS_COLUMNS);
		
		Log.debug("Adding " + messages.size() + " form messages to DataBinder");
		
		for (FormMessage message : messages) {
			Vector<String> v = new Vector<String>();
			
			v.add(message.type.toString());
			v.add(message.header != null ? message.header : message.type.defaultHeader);
			v.add(message.body);
		
			rs.addRow(v);
		}
		
		return rs;
	}
	
	/** Creates a ResultSet of stored form messages and adds to the passed DataBinder
	 *  as 'rsFormMessages'
	 *  
	 *  Also adds the FORM_VALIDITY_LEVEL key. This will determine the overall validity
	 *  of the form ID with respect to the document and determine how the Form ID field
	 *  will be displayed.
	 *  
	 *  Should be executed AFTER calls to getDocMetaMapping, addExtraBinderParams.
	 */
	public void addFormMessagesToBinder(DataBinder binder) {
		
		DataResultSet rs = createFormMessageResultSet();
		binder.addResultSet("rsFormMessages", rs);
		
		binder.putLocal("FORM_VALIDITY_LEVEL", validityLevel.toString());
	}
	
	/** Enqueues a form message, which will be added to the DataBinder via 
	 *  addFormMessagesToBinder.
	 *  
	 *  The form Validity Level may become more severe, depending on the message type
	 *  and current validity level.
	 *  
	 * @param message
	 */
	protected void enqueueFormMessage(FormMessage message) {
		Log.debug("Enqueueing new form message: " + message.toString());
		this.messages.add(message);
		setValidityLevel(message.type.validityLevel);
	}
	
	/** The DataBinder supports a single Form Message within these key strings. */
	protected final class FormMessageStrings {
		public static final String TYPE = "FORM_MESSAGE_TYPE";
		public static final String HEADER = "FORM_MESSAGE_HEADER";
		public static final String BODY = "FORM_MESSAGE_BODY";
	}
	
	/** Column names used to build the message container ResultSet. */
	protected final String[] FORM_MESSAGE_RS_COLUMNS = new String[] {
		"MSG_TYPE",
		"MSG_HEADER",
		"MSG_BODY"
	};
	
	/** Used when generating new form instances.
	 * 
	 * @param userId
	 * @param facade
	 */
	public FormHandler(String userId, FWFacade facade) {
		Log.debug("Initialized default Form Handler");
		
		this.userId = userId;
		this.facade = facade;
	}
	
	public FormHandler(Form form, String userId, FWFacade facade) {
		Log.debug("Initialized default Form Handler");
		
		this.form = form;
		this.userId = userId;
		this.facade = facade;
	}
	
	public abstract Form generateForm(FormType formType, Element element, Integer investmentId) 
	 throws ServiceException, DataException;

	public Form generateForm(FormType formType, Element element)
			throws ServiceException, DataException {
		return generateForm(formType, element, null);
	}
	
	/**
	 *   This will be executed once for each form instance, after it
	 *   has been returned to CCLA and scanned/faxed into UCM. 
	 *   
	 *   By default, it is used to update form data to indicate when 
	 *   it was returned and the dDocName of the scanned content item.
	 *   
	 *   Some validation checks will take place here, i.e. checking if the form is
	 *   in a Cancelled state, or the deadline date has been exceeded.
	 *   
	 *   An activity log is also added to the associated Client Services
	 *   campaign enrolment, where applicable, indicating the form was returned.
	 *   
	 *   WARNING: this method is called from a update filter event. Be 
	 *   wary to catch any non-critical errors so the calling action does
	 *   not fail.
	 *   
	 * @throws Exception 
	 */
	public void doPostCheckinActions(int docId) 
	 throws Exception {
		
		Log.debug("Performing standard post-checkin actions for Form ID " 
		 + form.getFormId());
		
		boolean doEnrolmentAudit = true;
		
		// Check if this form has been Cancelled first.
		if (form.getFormStatusId() == FormStatus.CANCELLED.id) {
			String err = "Form ID " + form.getFormId() + " has been cancelled";
			Log.warn(err);
			
			doEnrolmentAudit = false;
		}
		
		// Check if this form has exceeded its deadline date.
		if (form.getReturnDeadlineDate() != null
			&& form.getReturnDeadlineDate().before(new Date())) {
			String err = "Form ID " + form.getFormId() + 
			 " has been returned after its deadline date";
			Log.warn(err);
			
			//doEnrolmentAudit = false;
		}
		
		String docGuid = null;
		try {
			docGuid = CCLAUtils.getDocGuidFromDid(docId);
		} catch (Exception e) {
			String err = "FormHandler: doPostCheckinActions: unable to set docGuid: " + 
			 e.getMessage();
			Log.error(err);
			throw new ServiceException(err);
		}
		
		form.setRetDocGuid(docGuid);
		
		form.setFormStatusId(FormStatus.RETURNED.id);
		form.setDateReturned(new Date());
		
		form.persist(facade, Globals.Users.System);
		
		if (doEnrolmentAudit)
			addFormReturnedActivityLogToEnrolment();
	}
	
	/** Add audit message against the campaign enrolment, if this is a singleton
	 * form type.
	 */
	protected void addFormReturnedActivityLogToEnrolment() throws DataException {
		
		if (form.getFormType().isSingleton() && form.getCampaignEnrolmentId() != null) {
			Log.debug("Adding 'Form returned' activity log to enrolment ID: " 
			 + form.getCampaignEnrolmentId());
			
			CampaignEnrolment enrolment = CampaignEnrolment.get
			 (form.getCampaignEnrolmentId(), facade);
			
			if (enrolment == null) {
				Log.warn("No matching enrolment found! " +
				 "Unable to append activty log.");
				return;
			} else {
				try {
					enrolment.addActivity(null, 
					 CampaignActivityType.FORM_RETURNED_ACTIVITY_ID, 
					 "Form ID " + form.getFormId() + " returned", null, 
					 facade, Globals.Users.System);
					
				} catch (Exception e) {
					Log.error("Failed to append activity log", e);
				}
			}	
		}
	}
	
	/** Executed after a document with the given Form ID has passed initial Indexing
	 *  step.
	 *  
	 *  Sets the Form Status to 'Indexed'
	 *  
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void doPostIndexActions() throws DataException, ServiceException {
		Log.debug("Performing post-index actions on form ID " + form.getFormId());
		
		if (!form.getFormType().isSingleton())
			return;
		
		if (form.getFormStatusId() == Form.FormStatus.CANCELLED.id)
			return;
		
		form.setFormStatusId(Form.FormStatus.INDEXED.id);
		form.persist(facade, userId);
	}
	
	/** Executed after a document with the given Form ID has passed Validation and ready
	 *  for consumption by the Instruction Register.
	 *  
	 *  If the form is at a Cancelled state, abort immediately.
	 *  
	 *  Sets the status to Validated or Invalidated, depending on the IS_VALID flag.
	 *  
	 */
	public void doPostValidateActions() throws DataException, ServiceException {
		Log.debug("Performing post-validate actions on form ID " + form.getFormId());
		
		if (!form.getFormType().isSingleton())
			return;
		
		if (form.getFormStatusId() == Form.FormStatus.CANCELLED.id)
			return;
		
		if (form.isValid() != null && !form.isValid()) {
			form.setFormStatusId(Form.FormStatus.INVALIDATED.id);
		} else if (form.isValid() == null || form.isValid()) {
			form.setFormStatusId(Form.FormStatus.VALIDATED.id);
			
			// Explicitly set IS_VALID flag to 'true'
			form.setValid(true);
		}
		
		// If Subscription ID present, set the parent subscription's 
		// CLIENT_CONFIRMED flag
		// TODO: update to support amendment forms.
		if (form.getSubscriptionId() != null) {
			Subscription subscription = 
			 Subscription.get(form.getSubscriptionId(), facade);
			
			if (subscription.getStatusId() 
				== Subscription.SubscriptionStatusIds.CANCELLED) {
				// Subscription has been cancelled, don't touch it.
			} else {
				if (form.isValid()) {
					Log.debug("Updating Form's Subscription status to Client Confirmed");
					subscription.setClientConfirmed(true);
					subscription.setStatusId
					 (Subscription.SubscriptionStatusIds.CLIENT_CONFIRMED);
					
				} else {
					Log.debug("Updating Form's Subscription status to Client " +
					 "Confirmation Invalid");
					subscription.setClientConfirmed(false);
					subscription.setStatusId
					 (Subscription.SubscriptionStatusIds.CLIENT_CONFIRMATION_INVALID);
				}
				
				subscription.persist(facade, userId);
			}
		}
			
		form.persist(facade, userId);
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
		
		// Add 'isSingleton' flag to map if the form has singleton form type
		if (form.getFormType().isSingleton())
			map.put(FormType.Cols.SINGLETON, "1");
		
		// Set the xDocumentClass/Instruction Type
		if (form.getFormType().getRetInstructionType() != null)
			map.put(UCMFieldNames.DocClass, 
			 form.getFormType().getRetInstructionType().getName());
		
		// Add the xFormId field
		map.put(UCMFieldNames.FormID, Integer.toString(form.getFormId()));
		
		// Add the Subscription ID value, if applicable
		if (form.getSubscriptionId() != null)
			map.put(Subscription.Cols.ID, form.getSubscriptionId().toString());
		
		Entity org = null;
		
		if (form.getOrganisationId() != null) {
			org = Entity.get(form.getOrganisationId(), facade);
		} else if (form.getCampaignEnrolmentId() != null) {
			CampaignEnrolment enrolment = CampaignEnrolment.get
			 (form.getCampaignEnrolmentId(), facade);
			
			if (enrolment.getOrganisationId() != null) {
				// Lookup client data, if applicable
				org =  Entity.get(enrolment.getOrganisationId(), facade);
			}
		}
		
		if (org != null) {
			Vector<AuroraClient> auroraClients = 
			 Entity.getAuroraClientMapping(org.getOrganisationId(), facade);
			
			if (!auroraClients.isEmpty()) {
				// Just add data from the first matched AuroraClient.
				addClientDocMeta(org, auroraClients.get(0), map);
			}
		}
		
		Date docDate = null;
		
		// Add the xDocumentDate field
		if (form.getDateReturned() != null)
			docDate = form.getDateReturned();
		else
			docDate = new Date();

		try {
			map.put(UCMFieldNames.DocumentDate, 
			 DateFormatter.getTimeStamp("dd/MM/yyyy", docDate));
		} catch (ParseStringException e) {}
		
		// Check if this form has been Cancelled.
		if (form.getFormStatusId() == FormStatus.CANCELLED.id) {
			String err = "Form ID " + form.getFormId() + " has been cancelled. " +
			 "Setting doc class to CONDINS";
			
			Log.warn(err);
			
			map.put(UCMFieldNames.DocClass, DocumentClass.Classes.CONDINS);
			map.put(UCMFieldNames.Comments, "Form ID " + form.getFormId() + 
			 " has been cancelled");
			
			FormMessage message = new FormMessage(
			 MessageType.WARNING, 
			 "Form Cancelled",
			 "Form has been marked as cancelled"
			);
			enqueueFormMessage(message);
		}
		
		// Check if this form has exceeded its deadline date.
		if (form.getReturnDeadlineDate() != null) {
			Date formReturnedDate = form.getDateReturned();
			
			if (formReturnedDate == null)
				formReturnedDate = new Date(); // Assume form has just been checked in!
			
			if (form.getReturnDeadlineDate().before(formReturnedDate)) {
				String err = "Form ID " + form.getFormId() + 
				 " has been returned after its deadline date. " +
				 "Setting doc class to CONDINS";
				
				Log.warn(err);
				
				map.put(UCMFieldNames.DocClass, DocumentClass.Classes.CONDINS);
				map.put(UCMFieldNames.Comments, "Form ID " + form.getFormId() + 
				 " returned after its deadline date (" + 
				 DateFormatter.getTimeStamp(form.getReturnDeadlineDate()) + ")");
				
				FormMessage message = new FormMessage(
				 MessageType.WARNING, 
				 "Deadline exceeded",
				 "Form has been returned after its deadline " +
				 "date of " + 
				 DateFormatter.getTimeStamp(form.getReturnDeadlineDate())
				);
				enqueueFormMessage(message);
			}
		}
		
		return map;
	}
	
	/** Called in addition to getDocMetaMapping when fetching form details to display
	 *  in the UI, particularly when indexing returned form documents.
	 *  
	 *  Use this hook to add custom binder parameters that don't necessarily fit in the
	 *  getDocMetaMapping ResultSet.
	 *  
	 *  If you want to add a 'custom form link' to the Iris UI, add the required
	 *  parameters here (CUSTOM_FORM_LINK_URL, CUSTOM_FORM_LINK_LABEL)
	 *  
	 *  Custom messages are also supported - see FormMessage.addToBinder(). Currently
	 *  only 1 return message is supported.
	 *  
	 * @param binder
	 */
	public void addExtraBinderParams(DataBinder binder) {
	}
	
	protected static void addClientDocMeta
	 (Entity org, AuroraClient auroraClient, Hashtable<String, String> map) {
		
		if (auroraClient != null) {
			map.put(UCMFieldNames.Company, auroraClient.getCompany().getCode());
			map.put(UCMFieldNames.ClientNumber, 
			 CCLAUtils.padClientNumber(
			 Integer.toString(auroraClient.getClientNumber()), 
			 auroraClient.getCompany()));
			
			if (org != null) {
				map.put(UCMFieldNames.ClientName, org.getOrganisationName());
				map.put(UCMFieldNames.OrgAccountCode, org.getOrgAccountCode());
			} else {
				map.put(UCMFieldNames.ClientName, "");
				map.put(UCMFieldNames.OrgAccountCode, "");
			}
			
		} else {
			map.put(UCMFieldNames.Company, "");
			map.put(UCMFieldNames.ClientNumber, "");
			map.put(UCMFieldNames.ClientName, "");
			map.put(UCMFieldNames.OrgAccountCode, "");
		}
	}
	
	private static Class<?>[] CONSTRUCTOR_PARAMS = null;
	private static Class<?>[] FORM_CONSTRUCTOR_PARAMS = null;
	
	static {
		CONSTRUCTOR_PARAMS = new Class[2];
		
		CONSTRUCTOR_PARAMS[0] = String.class;
		CONSTRUCTOR_PARAMS[1] = FWFacade.class;
		
		FORM_CONSTRUCTOR_PARAMS = new Class[3];
		
		FORM_CONSTRUCTOR_PARAMS[0] = Form.class;
		FORM_CONSTRUCTOR_PARAMS[1] = String.class;
		FORM_CONSTRUCTOR_PARAMS[2] = FWFacade.class;
	}
	
	/** Fetches default FormHandler constructor params. Used for instantiating
	 *  subclasses using reflection.
	 *  
	 * @return
	 */
	public static Class<?>[] getConstructorParams() {
		return CONSTRUCTOR_PARAMS;
	}
	
	/** Fetches FormHandler constructor params for existing Form. Used for instantiating
	 *  subclasses using reflection.
	 *  
	 * @return
	 */
	public static Class<?>[] getFormConstructorParams() {
		return FORM_CONSTRUCTOR_PARAMS;
	}
}
