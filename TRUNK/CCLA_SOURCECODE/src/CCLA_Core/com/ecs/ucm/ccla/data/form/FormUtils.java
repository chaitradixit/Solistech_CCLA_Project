package com.ecs.ucm.ccla.data.form;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.shared.SharedObjects;

import com.aurora.webservice.Client;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.aurora.AuroraWebServiceHandler;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.AuroraClient;
import com.ecs.ucm.ccla.data.ClientProcess;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.UCMForm;
import com.ecs.utils.ClassLoaderUtils;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.LWDocument;
import com.ecs.utils.stellent.embedded.FWFacade;

public class FormUtils {
	
	static final String SPOOL_FILE_TEMP_ADDRESS =
	 SharedObjects.getEnvironmentValue("AURORA_SpoolFileTempAddress");
	
	static final Class<UCMFormHandler> DEFAULT_FORM_HANDLER_CLASS =
	 UCMFormHandler.class;
	
	/** Looks for entries in the CCLA_FORM_CONFIG table which match
	 *  the given form's type/sub-type.
	 *  
	 *  Returns the 'best fit', i.e. the config which matches the passed
	 *  form type and sub-type.
	 *  
	 * @throws DataException 
	 *  
	 */
	public static FormConfig getFormConfig(UCMForm form, FWFacade facade) 
	 throws DataException {
		
		String type 	= form.getType();
		String subType 	= form.getSubType();
		
		DataBinder binder = new DataBinder();
		binder.putLocal("formType", type);
		
		DataResultSet rsConfigs = 
		 facade.createResultSet("qClientServices_GetFormConfigs", binder);
		
		String docClass			= null;
		String formHandlerName 	= null;
		
		Class<UCMFormHandler> handlerClass = null;
		
		if (!rsConfigs.isEmpty())  {
			// Loop through all form configs for the form type. Choose the most
			// specific handler class found.
			do {
				String thisSubType = rsConfigs.getStringValueByName("FORM_SUBTYPE");
				
				if (formHandlerName == null && 
					StringUtils.stringIsBlank(thisSubType)) {
					docClass = rsConfigs.getStringValueByName("DOC_CLASS");
					formHandlerName = 
					 rsConfigs.getStringValueByName("FORM_HANDLER_CLASS");
				
				} else if (subType != null && thisSubType.equals(subType)) {
					// Matching form sub-type config.
					docClass = rsConfigs.getStringValueByName("DOC_CLASS");
					formHandlerName = 
					 rsConfigs.getStringValueByName("FORM_HANDLER_CLASS");
				}
				
			} while (rsConfigs.next());
		}
		
		try {
			if (formHandlerName != null)
				handlerClass = (Class<UCMFormHandler>)
					ClassLoaderUtils.getComponentClassLoader().loadClass(formHandlerName);			
		} catch (ClassNotFoundException e) {
			throw new DataException("Unable to load custom form " +
			 "handler class: " + formHandlerName, e);
		}
		
		FormConfig thisConfig = new FormConfig(handlerClass, docClass);
		
		return thisConfig;
	}
	
	/**
	 *  If a non-null handlerClass is passed, an instance is constructed 
	 *  and returned.
	 *  
	 *  If no custom class is passed, a default FormHandler instance is
	 *  returned instead.
	 *  
	 * @param form
	 * @param facade
	 * @return
	 * @throws DataException
	 * @throws ServiceException
	 */
	public static UCMFormHandler getFormHandler
	 (UCMForm form, Class<UCMFormHandler> handlerClass, String userId, FWFacade facade)
	
	 throws DataException, ServiceException {
	
		if (handlerClass == null)
			handlerClass = DEFAULT_FORM_HANDLER_CLASS;
		
		// FormHandler constructor parameters and values
		Class<?>[] params = new Class[] {
			UCMForm.class, String.class, FWFacade.class
		};
		
		Object[] values = new Object[] {
			form, userId, facade
		};
		
		try {
			Constructor constructor = 
			 handlerClass.getConstructor(params);
			
			UCMFormHandler formHandler = 
			 (UCMFormHandler)constructor.newInstance(values);
			
			return formHandler;
			
		} catch (Exception e) {
			throw new ServiceException("Unable to initialize form " +
			 "handler class: " + handlerClass.getName(), e);
		}
	}
	
	/** Determines whether or not a form exists for the given combination
	 *  of process ID, form type, client and person. If so, its
	 *  unique ID is returned. Returns null otherwise.
	 *  
	 *  @deprecated this isn't required now, as new form revisions are always
	 *  			generated.
	 *  
	 * @param processId
	 * @param formType
	 * @param clientId
	 * @param correspondentId
	 * @param facade
	 * @return
	 */
	public static Integer getFormId(String processId, 
	 String formType, String clientId, String personId,
	 FWFacade facade) throws ServiceException, DataException {
		
		DataBinder binder = new DataBinder();
		
		if (StringUtils.stringIsBlank(processId)) {
			throw new ServiceException("Can't search for form " +
			 "without a processId.");
		}
		
		binder.putLocal("processId", processId);
		
		CCLAUtils.addQueryParamToBinder(binder, "formType", formType);
		CCLAUtils.addQueryParamToBinder(binder, "clientId", clientId);
		CCLAUtils.addQueryParamToBinder(binder, "personId", personId);
		
		Log.debug("Searching for existing form with process ID: " + processId +
		 ", type: " + formType + ", clientId: " + clientId + 
		 ", personId: " + personId);
		
		DataResultSet rs =
		 facade.createResultSet("qClientServices_GetFormMap", binder);
		
		if (!rs.isEmpty()) {
			Log.debug("Found existing form with ID: " + 
			 rs.getStringValueByName("FORM_ID"));
			
			return new Integer(rs.getStringValueByName("FORM_ID"));
		} else {
			Log.debug("No existing form found.");
			
			return null;
		}
	}
	
	/** Adds the metadata field mapping for the given form into the binder.
	 *  
	 *  The first set of field values come directly from the Form instance,
	 *  i.e. data from the CCLA_FORMS table. This includes client and person
	 *  IDs.
	 *  
	 *  The second set of field values comes from performing lookups on the
	 *  above data, i.e. client name from the client ID.
	 *  
	 *  The third set of field values are more static and based on the form
	 *  type/sub-type. This includes values such as the document class.
	 * 
	 * @param form
	 * @param binder
	 * @throws ServiceException
	 * @throws DataException 
	 */
	public static void addFormDataToBinder(UCMForm form, DataBinder binder, 
	 FWFacade facade) throws ServiceException, DataException {
		
		if (form == null)
			throw new ServiceException("Unable to add form data to binder: " +
			 "form instance was null.");
		
		Entity entity		= Entity.get(form.getOrganisationId(), facade);
		
		Vector<AuroraClient> auroraClients =
		 Entity.getAuroraClientMapping(form.getOrganisationId(), facade);
		
		if (auroraClients.isEmpty())
			throw new ServiceException("Unable to add form data to binder: " +
			 "no Aurora Client Number/Company for Entity ID " + form.getOrganisationId());
		
		// TODO catch instances where multiple auroraClients exist
		AuroraClient auroraClient = auroraClients.get(0);
		
		//CCLAClient client = form.getClient(facade);
		
		CCLAUtils.addQueryParamToBinder(binder, 
		 "xCompany", auroraClient.getCompany().getCode());
		
		CCLAUtils.addQueryIntParamToBinder(binder, 
		 "xClientNumber", auroraClient.getClientNumber());
				
		CCLAUtils.addQueryIntParamToBinder(binder, 
		 "xCorrespondentId", form.getPersonId());
		
		CCLAUtils.addQueryParamToBinder(binder, 
		 "xClientName", entity.getOrganisationName());
	}
	
	/** Checks in a new generated form. 
	 * 
	 *  Returns the dID of the generated form.
	 * 
	 * @throws ServiceException */
	public static int checkinForm(UCMForm form, String docTitle, 
	 String fileName, String userName, Hashtable attributes,
	 ByteArrayOutputStream outputStream, FWFacade facade) throws ServiceException {
		
		File spoolFile = null;
		int dID;
		
		String docName = form.getBaseDocName();
		
		try {
			spoolFile = new File(fileName);

			FileOutputStream fileOutputStream = new FileOutputStream(spoolFile);
			fileOutputStream.write(outputStream.toByteArray());
			
			LWDocument lwDoc 	= null;
			boolean upRevision	= false;
			
			try {
				lwDoc 		= new LWDocument(docName, true);
				upRevision	= true;
				
				lwDoc.checkout();
				
			} catch (Exception e) {
				// Document doesn't exist yet.
				lwDoc = new LWDocument();
				lwDoc.useDatabase();
				
				lwDoc.setDocName(docName);
			}
				
			// Set default attributes
			lwDoc.setTitle(docTitle);
			lwDoc.setSecurityGroup("Public");
			lwDoc.setDocType("Document");
			lwDoc.setAuthor(userName);
			
			// Set custom attributes
			lwDoc.setAttributes(attributes);

			// Check-in the form content. This will return the dID of the
			// generated form, if this is a new revision.
			String dIDStr = lwDoc.checkin(spoolFile);
			
			if (upRevision)
				dID = Integer.parseInt(dIDStr);
			else
				dID = Integer.parseInt(lwDoc.getId());
			
			String newRevision = "";
			if (upRevision)
				newRevision = "new revision of ";
			
			Log.debug("Checked in " + newRevision + "form with dDocName: " 
			 + docName + " [dID=" + dID + "]");
			
			// Update form entry in CCLA_FORMS table
			form.setBaseDID(new Integer(dID));
			
			form.setGeneratedDate(new Date());
			form.setGenerated(true);
			
			form.setStatus("Generated");
			form.persist(facade, null);
			
		} catch (Exception e) {
			Log.error("Failed to checkin form with dDocName: " + docName, e);
			throw new ServiceException(e);
		}
		
		return dID;
	}
	
	/** Used to set a mapping between the given form ID and a set
	 *  of accounts.
	 *  
	 *  Any existing maps for the given ID are deleted first.
	 *  
	 *  Passing a null/empty account set will just remove any existing
	 *  maps.
	 *  
	 * @param formId
	 * @param accounts
	 */
	public static void setFormAccountMapping
	 (int formId, Vector<Account> accounts, FWFacade facade)
	 throws DataException {
		DataBinder binder = new DataBinder();
		
		if (accounts == null || accounts.size() == 0)
			Log.debug("Removing form account mapping: form ID=" + formId);
		else
			Log.debug("Setting form account mapping: form ID=" + formId + 
			 ", " + accounts.size() + " accounts");
		
		// Remove existing form-account maps
		CCLAUtils.addQueryIntParamToBinder(binder, "FORM_ID", formId);
		facade.execute("qClientServices_RemoveFormAccountMaps", binder);
		
		if (accounts == null || accounts.size() == 0)
			return;
		
		for (Account account:accounts) {
			binder.putLocal("ACCNUMEXT", account.getAccNumExt());
			facade.execute("qClientServices_AddFormAccountMap", binder);
		}
	}
	
	/** Takes a spool file output stream  and its associated file name (generally
	 *  the target content ID for the generated form) and returns a uniquely-named
	 *  File instance, created in the temporary spool file location.
	 *  
	 *  If the passed Company string is non-null, spool files will be placed in a
	 *  sub-directory with the Company's name.
	 *  
	 * @param fileName
	 * @param outputStream
	 * @return
	 * @throws IOException
	 */
	public static File createTempSpoolFile
	 (String fileName, String company, ByteArrayOutputStream outputStream) 
	 throws IOException {
	
		String directoryStr = SPOOL_FILE_TEMP_ADDRESS + 
		 (company != null ? company + File.separator : "");
		
		File directoryFile = new File(directoryStr);
		Log.debug("FormUtils.CreateSpoolFile in directory :"+directoryStr+", exists:"+
				directoryFile.exists());
		
		File spoolFile 	= File.createTempFile
		 (fileName + "_", ".txt", directoryFile);
		
		FileOutputStream fileOutputStream = new FileOutputStream(spoolFile);
		fileOutputStream.write(outputStream.toByteArray());
		
		fileOutputStream.close();
		
		return spoolFile;
	}
	
	/** Takes a formId parameter, which should map to an entry in the CCLA_FORMS
	 * table. The associated Form instance is retrieved, which contains the
	 * form dDocName. 
	 * 
	 * The primary file for the content item (i.e. the spool file)
	 * is then copied to a special temporary directory. The resulting file name
	 * for the temp file is passed to the printForm(fileName) function.
	 * 
	 * The Company string is used to determine the subdirectory in the spool file
	 * output folder where the spool file will be placed. If null, files will be placed
	 * in the root directory.
	 * 
	 */
	public static void printForm
	 (int formId, String company, String userName, FWFacade facade) 
	 throws ServiceException, DataException {
		
		// Check if printing is enabled via env. variable
		boolean enablePrinting = !StringUtils.stringIsBlank(
		 SharedObjects.getEnvironmentValue("AURORA_EnableFormPrinting"));
		
		if (!enablePrinting)
			throw new ServiceException("Form printing is currently disabled.");
		
		UCMForm form = UCMForm.get(formId, facade);
		
		if (form == null)
			throw new ServiceException("Form with ID:" + formId + " not found.");
		
		File spoolFile = null;
		
		try {
			String docName 		= form.getBaseDocName();
			Integer entityId	= form.getOrganisationId();
			Integer personId	= form.getPersonId();
			
			String formType		= form.getType();
			String fileName 	= null;
			
			File spoolFileAddress = new File(SPOOL_FILE_TEMP_ADDRESS +
			 (company != null ? company + File.separator : ""));
			
			LWDocument lwDoc = new LWDocument(docName, true);
			
			// Fetch primary content and output to temp file
			ByteArrayOutputStream outputStream = lwDoc.getLatestContent();
			
			spoolFile 	= File.createTempFile
			 (docName + "_spool", ".txt", spoolFileAddress);
			
			FileOutputStream fileOutputStream = new FileOutputStream(spoolFile);
			
			// Retrieve full path to the temp file
			outputStream.writeTo(fileOutputStream);
			fileName 	= spoolFile.getAbsolutePath();
	
			printForm(fileName);
				
			Integer processId	= form.getProcessId();
			
			if (processId != null) {
				// Add activity log to Client Services process, indicating form
				// was printed.
				ClientProcess process = ClientProcess.get(processId, facade);
				
				process.addActivity(userName, personId, "Form Print", 
				 formType + " form " + formId + " printed", null, 
				 false, facade);
				
				// Set sent date for all printed forms in CCLA_FORMS
				form.setPrintDate(new Date());
				form.setPrinted(true);
				form.setStatus("Printed");
				
				form.persist(facade, null);
			}
		
		} catch (Exception e) {
			Log.error("Unable to print form: " + e,e);
			throw new ServiceException("Unable to print form: " + e, e);
		}
	}
	
	/** Prints a CreateForm form, using a spool file with the given file 
	 *  name.
	 *  
	 *  The print job itself takes place in a delegate process.
	 * 
	 * @param fileName
	 */
	public static void printForm(String fileName) {
		
		String auroraFormPrinter = 
		 SharedObjects.getEnvironmentValue("AURORA_FormPrinterAddress");

		Log.debug("Printing spool file " + fileName + " via CreateForm device: " 
		 + auroraFormPrinter);
		
		// Delegate execution of print command to separate Thread
		FormPrinter formPrinter = new FormPrinter(auroraFormPrinter, fileName);
		formPrinter.start();
	}
	
	public static String getSpoolFileName
	 (Form form, Entity org, AuroraClient auroraClient) {
		
		return form.getFormType().getShortName() 
		 + "_" +
		 form.getFormId()
		 + "_" +
		 org.getOrganisationId()
		 + "_" +
		 auroraClient.getPaddedClientNumber();
	}
}
