package com.ecs.ucm.ccla.data;

import java.util.Date;
import java.util.Vector;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/**
 * Class that models the CCLA_FORMS database table.
 * 
 * @author gs
 *
 */
public class UCMForm implements Persistable {
	
	/** Status value assigned to all new forms. */
	public static final String NEW_FORM_STATUS = "Processing";
	
	private int formId;
	
	private String type;
	private String subType;
	
	private Integer processId;
	
	private Integer organisationId;
	private AuroraClient auroraClient; 
	
	private Integer personId;
	
	private String baseDocName;
	private Integer baseDID;
	
	private boolean printed;
	private boolean generated;
	private boolean returned;
	
	private Date createDate;
	private Date generatedDate;
	private Date printDate;
	private Date returnDate;
	
	private String status;
	
	private String returnedDocName;
	
	private boolean staticDataChange;
	private boolean additionalData;
	private boolean noChanges;
	
	// Use object type here, as the 'valid' parameter is initially null before
	// being manually set by a user.
	private Boolean valid;
	
	private String invalidReason;
	
	private Date lastUpdated;
	
	private DataResultSet data;
	
	/** Shortened constructor, used for creating new Form records. */
	public UCMForm(int formId, String type, String subType, 
		Integer processId, Integer orgId, Integer personId,
		AuroraClient auroraClient,
		String baseDocName, Integer baseDID,
		DataResultSet data) {
		
		this(formId, type, subType, processId, 
		 orgId, auroraClient, personId,
		 baseDocName, baseDID,
		 false, false, false, null, null, 
		 null, null, null, null, 
		 false, false, false, null, null, null, data);
	}
	
	/** Fully-qualified constructor. */
	public UCMForm(int formId, String type, String subType, 
		Integer processId, Integer orgId, AuroraClient auroraClient,
		Integer personId, 
		String baseDocName, Integer baseDID,
		boolean printed, boolean generated, boolean returned,
		String returnedDocName, String formStatus, 
		Date createDate, Date printDate, Date generatedDate, Date returnDate,
		boolean staticDataChange, boolean additionalData, boolean noChanges,
		Boolean valid, String invalidReason, Date lastUpdated,
		DataResultSet data) {

		this.formId = formId;
		this.type = type;
		this.subType = subType;
		
		this.processId = processId;
		this.organisationId = orgId;
		this.setAuroraClient(auroraClient);
		this.personId = personId;
		
		this.baseDocName = baseDocName;
		this.baseDID = baseDID;
		
		this.printed = printed;
		this.generated = generated;
		this.returned = returned;
		
		this.createDate	= createDate;
		this.generatedDate = generatedDate;
		this.printDate = printDate;
		this.returnDate = returnDate;
		
		this.returnedDocName = returnedDocName;
		this.status = formStatus;
		
		this.staticDataChange = staticDataChange;
		this.additionalData    = additionalData;
		this.noChanges = noChanges;
		this.valid = valid;
		
		this.invalidReason = invalidReason;
		
		this.lastUpdated = lastUpdated;
		this.data = data;
	}
	
	/**
	 * Creates a new ID to use on generated forms. This
	 * is used to link a document to a combination of process ID, 
	 * form type, Org ID, Person ID and dDocName.
	 * 
	 * All mapped fields are optional, apart from process ID.
	 * 
	 * @param processId
	 * @param formType
	 * @param orgId
	 * @param auroraClient
	 * @param personId
	 * @param workspace
	 * @return the new form document ID
	 * 
	 * @throws ServiceException if no processId specified
	 */
	public static UCMForm add(Integer processId, String type, String subType,
	 Integer orgId,  AuroraClient auroraClient, Integer personId,
	 String baseDocName, Integer baseId,
	 FWFacade facade) 
	 throws ServiceException, DataException {

		// Generate new Form ID. Use workflow ID sequence
		int formId = Integer.parseInt(
		 CCLAUtils.getNextEnvelopeId(facade));
		
		UCMForm form = new UCMForm(formId, type, subType, 
		 processId, orgId, personId, auroraClient,  
		 baseDocName, baseId, null);
		
		form.setStatus(NEW_FORM_STATUS);
		form.validate(facade);
		
		DataBinder binder = new DataBinder();
		form.addFieldsToBinder(binder);
		
		facade.execute("qClientServices_AddForm", binder);
		
		Log.debug(
			"Inserted new form with id '" + formId + "' [processId=" +
			processId + ",orgtId=" + orgId + ",personId=" +
			personId + ",baseDocName=" + baseDocName);
		
		return get(formId, facade);
	}
	
	/** Creates a Form instance, from existing form data with the
	 *  passed formId. If no form data is found, returns null.
	 *  
	 * @param formId
	 * @param facade
	 * @return
	 * @throws DataException
	 * @throws ServiceException 
	 */
	public static UCMForm get(int formId, FWFacade facade) 
	 throws DataException, ServiceException {

		DataResultSet rsForm = getData(formId, facade);
		
		if (rsForm.isEmpty()) {
			Log.debug("No existing form found for formId:" + formId);
			return null;
		}
		
		UCMForm form = get(rsForm);
		return form;
	}
	
	/** Creates a Form instance from the active row of the passed ResultSet.
	 * 
	 *  Expects column names in the same format found in the CCLA_FORM
	 *  table, left-joined to the CCLA_CLIENT_AURORA_MAP table.
	 *  
	 * @param rsForm
	 * @return
	 * @throws ServiceException 
	 * @throws DataException 
	 */
	public static UCMForm get(DataResultSet rsForm) throws DataException {
		
		if (rsForm.isEmpty())
			return null;
		
		// First check for Aurora Client mapping fields.
		AuroraClient auroraClient = null;
		String mapIdStr = rsForm.getStringValueByName("CLIENT_AURORA_MAP_ID");
		
		if (!StringUtils.stringIsBlank(mapIdStr)) {
			 auroraClient = AuroraClient.get(rsForm);
		}
		
		Boolean valid = null;
		
		if (!StringUtils.stringIsBlank(rsForm.getStringValueByName("VALID")))
			valid = CCLAUtils.getResultSetBoolValue(rsForm, "VALID");
		
		UCMForm form = new UCMForm(
		 CCLAUtils.getResultSetIntegerValue(rsForm, "FORM_ID"), 
		 rsForm.getStringValueByName("FORM_TYPE"),
		 rsForm.getStringValueByName("FORM_SUBTYPE"), 
		 CCLAUtils.getResultSetIntegerValue(rsForm, "PROCESS_ID"), 
		 CCLAUtils.getResultSetIntegerValue(rsForm, "ORGANISATION_ID"),
		 auroraClient,
		 CCLAUtils.getResultSetIntegerValue(rsForm, "PERSON_ID"), 
		 rsForm.getStringValueByName("BASE_DOCNAME"), 
		 CCLAUtils.getResultSetIntegerValue(rsForm, "BASE_ID"),
		 CCLAUtils.getResultSetBoolValue(rsForm, "PRINTED"), 
		 CCLAUtils.getResultSetBoolValue(rsForm, "GENERATED"), 
		 CCLAUtils.getResultSetBoolValue(rsForm, "RETURNED"),
		 rsForm.getStringValueByName("RETURNED_DOCNAME"),
		 rsForm.getStringValueByName("FORM_STATUS"), 
		 rsForm.getDateValueByName("CREATE_DATE"), 
		 rsForm.getDateValueByName("PRINT_DATE"), 
		 rsForm.getDateValueByName("GEN_DATE"), 
		 rsForm.getDateValueByName("RETURN_DATE"),
		 CCLAUtils.getResultSetBoolValue(rsForm, "STATIC_DATA_CHANGE"),
		 CCLAUtils.getResultSetBoolValue(rsForm, "ADDITIONAL_DATA"),
		 CCLAUtils.getResultSetBoolValue(rsForm, "NO_CHANGES"),
		 valid,
		 rsForm.getStringValueByName("INVALID_REASON"),
		 rsForm.getDateValueByName("LAST_UPDATED"),
		 rsForm
		 );
		
		return form;
	}
	
	public static DataResultSet getData(int formId, FWFacade facade)
	 throws DataException {
		DataBinder binder = new DataBinder();
		binder.putLocal("FORM_ID", Integer.toString(formId));
		
		DataResultSet rs =
		 facade.createResultSet("qClientServices_GetFormById", binder);
		
		return rs;
	}
	
	/** Updates the associated DB data for this form. 
	 * 
	 * @throws DataException */
	public void persist(FWFacade facade, String username) throws DataException {
		this.validate(facade);
		
		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);
		
		facade.execute("qClientServices_UpdateForm", binder);
	}
	
	/** Attempts to fetch Organisation data using the client ID/company
	 *  associated with this form. If either field is missing, the
	 *  method returns null.
	 *  
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public Entity getOrganisation(FWFacade facade) throws DataException {

		if (this.getOrganisationId() == null)
			return null;
		
		return Entity.get(this.getOrganisationId(), facade);
	}
	
	/** Fetches all associated accounts for this form, as specified by the
	 *  CCLA_FORM_ACCOUNT_MAP table.
	 *  
	 *  If no associated accounts exist, an empty Vector is returned.
	 *  
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public Vector<Account> getAccounts(FWFacade facade) throws DataException {
		Vector<Account> accounts = new Vector<Account>();
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "FORM_ID", this.getFormId());
		
		// Fetch a list of external account numbers related to this form
		DataResultSet rsAccounts = this.getAccountData(facade);
		
		if (!rsAccounts.isEmpty()) {
			do {
				String thisAccNumExt = 
				 rsAccounts.getStringValueByName("ACCNUMEXT");
				Account account = Account.get(thisAccNumExt, facade);
				
				if (account != null)
					accounts.add(account);
				
			} while (rsAccounts.next());	
		}
		
		return accounts;
	}
	
	/** Fetches raw data for all associated accounts for this form, 
	 *  as specified by the CCLA_FORM_ACCOUNT_MAP table.
	 *  
	 *  If no associated accounts exist, an empty Vector is returned.
	 *  
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public DataResultSet getAccountData(FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		binder.putLocal("FORM_ID", Integer.toString(this.getFormId()));
		
		// Fetch a list of external account numbers related to this form
		DataResultSet rsAccounts =
		 facade.createResultSet("qClientServices_GetFormAccounts", binder);
		
		return rsAccounts;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public Integer getProcessId() {
		return processId;
	}

	public void setProcessId(Integer processId) {
		this.processId = processId;
	}

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer orgId) {
		this.organisationId = orgId;
	}

	public Integer getPersonId() {
		return personId;
	}

	public void setPersonId(Integer personId) {
		this.personId = personId;
	}

	public String getBaseDocName() {
		return baseDocName;
	}

	public void setBaseDocName(String baseDocName) {
		this.baseDocName = baseDocName;
	}

	public Integer getBaseDID() {
		return baseDID;
	}

	public void setBaseDID(Integer baseDID) {
		this.baseDID = baseDID;
	}

	public boolean isPrinted() {
		return printed;
	}

	public void setPrinted(boolean printed) {
		this.printed = printed;
	}

	public boolean isGenerated() {
		return generated;
	}

	public void setGenerated(boolean generated) {
		this.generated = generated;
	}

	public boolean isReturned() {
		return returned;
	}

	public void setReturned(boolean returned) {
		this.returned = returned;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getGeneratedDate() {
		return generatedDate;
	}

	public void setGeneratedDate(Date generatedDate) {
		this.generatedDate = generatedDate;
	}

	public Date getPrintDate() {
		return printDate;
	}

	public void setPrintDate(Date printDate) {
		this.printDate = printDate;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String formStatus) {
		this.status = formStatus;
	}

	public String getReturnedDocName() {
		return returnedDocName;
	}

	public void setReturnedDocName(String returnedDocName) {
		this.returnedDocName = returnedDocName;
	}

	public int getFormId() {
		return formId;
	}

	public DataResultSet getData() {
		return data;
	}

	public void setData(DataResultSet data) {
		this.data = data;
	}

	public boolean isStaticDataChange() {
		return staticDataChange;
	}

	public void setStaticDataChange(boolean staticDataChange) {
		this.staticDataChange = staticDataChange;
	}

	public boolean isAdditionalData() {
		return additionalData;
	}

	public void setAdditionalData(boolean additionalData) {
		this.additionalData = additionalData;
	}

	public Boolean getValid() {
		return valid;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
	}

	public boolean isNoChanges() {
		return noChanges;
	}

	public void setNoChanges(boolean noChanges) {
		this.noChanges = noChanges;
	}

	public String getInvalidReason() {
		return invalidReason;
	}

	public void setInvalidReason(String invalidReason) {
		this.invalidReason = invalidReason;
	}

	public void addFieldsToBinder(DataBinder binder) throws DataException {
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "FORM_ID", this.getFormId());

		CCLAUtils.addQueryParamToBinder
		 (binder, "FORM_TYPE", this.getType());
		
		CCLAUtils.addQueryParamToBinder
		 (binder, "FORM_SUBTYPE", this.getSubType());
		 
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "PROCESS_ID", this.getProcessId());
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "ORGANISATION_ID", this.getOrganisationId());

		if (this.getAuroraClient() != null) {
			CCLAUtils.addQueryIntParamToBinder
			 (binder, "CLIENT_AURORA_MAP_ID", this.getAuroraClient().getMapId());
		} else {
			CCLAUtils.addQueryIntParamToBinder
			 (binder, "CLIENT_AURORA_MAP_ID", null);
		}
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "PERSON_ID", this.getPersonId());

		CCLAUtils.addQueryParamToBinder
		 (binder, "BASE_DOCNAME", this.getBaseDocName());
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "BASE_ID", this.getBaseDID());
		
		CCLAUtils.addQueryBooleanParamToBinder
		 (binder, "PRINTED", this.isPrinted());
		
		CCLAUtils.addQueryBooleanParamToBinder
		 (binder, "GENERATED", this.isGenerated());
		
		CCLAUtils.addQueryBooleanParamToBinder
		 (binder, "RETURNED", this.isReturned());
		
		CCLAUtils.addQueryParamToBinder
		 (binder, "RETURNED_DOCNAME", this.getReturnedDocName());
		
		CCLAUtils.addQueryParamToBinder
		 (binder, "FORM_STATUS", this.getStatus());
		
		CCLAUtils.addQueryDateParamToBinder
		 (binder, "CREATE_DATE", this.getCreateDate());
		
		CCLAUtils.addQueryDateParamToBinder
		 (binder, "GEN_DATE", this.getGeneratedDate());
		
		CCLAUtils.addQueryDateParamToBinder
		 (binder, "PRINT_DATE", this.getPrintDate());
		
		CCLAUtils.addQueryDateParamToBinder
		 (binder, "RETURN_DATE", this.getReturnDate());
		
		CCLAUtils.addQueryBooleanParamToBinder
		 (binder, "STATIC_DATA_CHANGE", this.isStaticDataChange());
		
		CCLAUtils.addQueryBooleanParamToBinder
		 (binder, "ADDITIONAL_DATA", this.isAdditionalData());
		
		CCLAUtils.addQueryBooleanParamToBinder
		 (binder, "NO_CHANGES", this.isNoChanges());
		
		if (this.getValid() == null) {
			CCLAUtils.addQueryParamToBinder
			 (binder, "VALID", null);
		} else {
			CCLAUtils.addQueryBooleanParamToBinder
			 (binder, "VALID", this.getValid().booleanValue());
		}
		
		CCLAUtils.addQueryParamToBinder
		 (binder, "INVALID_REASON", this.getInvalidReason());
		
		CCLAUtils.addQueryDateParamToBinder
		 (binder, "LAST_UPDATED", this.getLastUpdated());
	}

	/** Not implemented here, as forms are never created/updated directly
	 *  from Binder contents.
	 *  
	 */
	public void setAttributes(DataBinder binder) throws DataException {
		throw new DataException("Not implemented.");
	}

	public void validate(FWFacade facade) throws DataException {
		if (this.getProcessId() == null)
			throw new DataException("Process ID not specified");
	}

	public void setAuroraClient(AuroraClient auroraClient) {
		this.auroraClient = auroraClient;
	}

	public AuroraClient getAuroraClient() {
		return auroraClient;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}
}
