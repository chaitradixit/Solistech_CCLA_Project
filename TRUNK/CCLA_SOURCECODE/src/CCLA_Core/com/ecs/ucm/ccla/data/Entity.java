package com.ecs.ucm.ccla.data;

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.data.ResultSet;
import intradoc.shared.SharedObjects;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.aurora.compare.AuroraEntitySource;
import com.ecs.ucm.ccla.cache.Cachable;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Represents an entry in the CCLA.ORGANISATION table, i.e. a 
 *  charity/organisation/authority known to CCLA.
 *  
 * @author Tom
 *
 */
public class Entity extends Element implements Persistable, AuroraEntitySource {
	
	/** Special flag value, if passed into the DataBinder on the add() method, will
	 *  not auto-generate a new Aurora Client Number regardless of other configuration.
	 */
	public static final String SKIP_AURORA_CLIENT_NUMBER_GENERATION = 
	 "SKIP_AURORA_CLIENT_NUMBER_GENERATION";
	
	public static final String ORG_ACCOUNT_CODE_PREFIX = "ORG_ACCOUNT_CODE_PREFIX";
	
	/** Whether or not all Relation Checks return PASSED, regardless of
	 *  the state of Relations.
	 */
	public static final boolean RELATION_CHECK_GLOBAL_OVERRIDE = 
	 !StringUtils.stringIsBlank(SharedObjects.getEnvironmentValue
	 ("CCLA_CS_GlobalRelationCheckOverride"));

	/** Whether or not an Unknown Relation Check will return a PASSED outcome.
	 */
	public static final boolean RELATION_CHECK_TREAT_UNKNOWN_AS_PASSED = 
	 !StringUtils.stringIsBlank(SharedObjects.getEnvironmentValue
	 ("CCLA_CS_TreatUnknownRelationCheckAsPassed"));
	
	/** Wrapper class for Client Number/Company pairs, generally used to create an
	 *  AuroraClient instance.
	 *  
	 * @author Tom
	 *
	 */
	public static class ClientNumber {
		private int clientNumber;
		private Company company;

		private int contributerCode;
		private int contributorTypeCode;
		private int subdivisionCode;
		
		public ClientNumber(int clientNumber, Company company) {
			this.clientNumber = clientNumber;
			this.company = company;
			
			// For centrally-generated Client Numbers, the Cont. Type Code and
			// Sub-division Code are always 0, Cont. Type Code matches the full 
			// Client Number.
			this.contributerCode = clientNumber;
			this.contributorTypeCode = 0;
			this.subdivisionCode = 0;
		}

		public int getClientNumber() {
			return clientNumber;
		}

		public Company getCompany() {
			return company;
		}
		
		public int getContributerCode() {
			return contributerCode;
		}

		public int getContributorTypeCode() {
			return contributorTypeCode;
		}

		public int getSubdivisionCode() {
			return subdivisionCode;
		}
		
		public void addFieldsToBinder(DataBinder binder) {
			CCLAUtils.addQueryIntParamToBinder
			 (binder, AuroraClient.Cols.CLIENT_NUMBER, this.getClientNumber());
			
			CCLAUtils.addQueryIntParamToBinder
			 (binder, AuroraClient.Cols.COMPANY, 
			 this.getCompany().getCompanyId());
			
			CCLAUtils.addQueryIntParamToBinder
			 (binder, AuroraClient.Cols.CONTRIBUTER_CODE, this.getContributerCode());
			CCLAUtils.addQueryIntParamToBinder
			 (binder, AuroraClient.Cols.CONTRIBUTOR_TYPE_CODE, 
			 this.getContributorTypeCode());
			CCLAUtils.addQueryIntParamToBinder
			 (binder, AuroraClient.Cols.SUBDIVISION_CODE, 
			 this.getSubdivisionCode());
		}
	}
	
	/** Return codes used when requesting entity check/verification status.
	 * 
	 * @author Tom
	 *
	 */
	public static enum VerificationCheckStatusCode {
		PASSED,
		FAILED,
		UNKNOWN,
		NOT_IMPLEMENTED
	}
	
	/** Wrapper class for storing Entity Verification Check outcomes.
	 * 
	 * @author Tom
	 *
	 */
	public static class VerificationCheckStatus {
		
		private VerificationCheckStatusCode statusCode;
		
		private boolean override;
		private String failReason;
		
		public VerificationCheckStatus(
				VerificationCheckStatusCode statusCode, boolean override,
				String failReason) {
			this.statusCode = statusCode;
			this.override = override;
			this.failReason = failReason;
		}

		public VerificationCheckStatusCode getStatusCode() {
			return statusCode;
		}

		public boolean isOverride() {
			return override;
		}

		public String getFailReason() {
			return failReason;
		}
		
		public void addToBinder(DataBinder binder) {
			binder.putLocal("EntityCheckStatusCode", 
			 this.statusCode.toString());
			
			if (this.failReason != null)
				binder.putLocal("EntityCheckFailReason",  
				 this.failReason);
			
			CCLAUtils.addQueryBooleanParamToBinder
			 (binder, "EntityCheckOverride", override);
		}

		@Override
		public String toString() {
			return "VerificationCheckStatus [failReason=" + failReason
					+ "\\n, override=" + override + "\\n, statusCode="
					+ statusCode + "]";
		}
	}
	

	/** Return codes used for Entity Relation check outcomes. */
	public static enum RelationCheckStatusCode {
		PASSED,
		FAILED,
		UNKNOWN
	}
	
	/** Wrapper class for storing Relation Check outcomes. 
	 *
	 **/
	public static class RelationCheckStatus {
		
		private RelationCheckStatusCode statusCode;
		
		private boolean override;
		private String failReason;
		
		public RelationCheckStatus(RelationCheckStatusCode statusCode,
				boolean override, String failReason) {
			this.statusCode = statusCode;
			this.override = override;
			this.failReason = failReason;
		}

		public RelationCheckStatusCode getStatusCode() {
			return statusCode;
		}

		public boolean isOverride() {
			return override;
		}

		public String getFailReason() {
			return failReason;
		}
		
		public void addToBinder(DataBinder binder) {
			binder.putLocal("RelationCheckStatusCode", 
			 this.statusCode.toString());
			
			if (this.failReason != null)
				binder.putLocal("RelationCheckFailReason",  
				 this.failReason);
			
			CCLAUtils.addQueryBooleanParamToBinder
			 (binder, "RelationCheckOverride", override);
		}

		@Override
		public String toString() {
			return "RelationCheckStatus [failReason=" + failReason
					+ "\\n, override=" + override + "\\n, statusCode="
					+ statusCode + "]";
		}
	}
	
	/** Wrapper class for all KYC check results relating to an Entity/Organisation.
	 *  
	 * @author Tom
	 *
	 */
	public static class EntityCheck {
		
		private VerificationCheckStatus verificationCheck;
		private RelationCheckStatus relationCheck;
		
		public EntityCheck(
		 VerificationCheckStatus verificationCheck, 
		 RelationCheckStatus relationCheck) {
			
			this.verificationCheck = verificationCheck;
			this.relationCheck = relationCheck;
		}
		
		public void addToBinder(DataBinder binder) {
			this.verificationCheck.addToBinder(binder);
			this.relationCheck.addToBinder(binder);
		}
		
		public String toString() {
			return "EntityCheck: \n\n" + 
			 this.verificationCheck.toString() 
			 + "\n" +
			 this.relationCheck.toString();
		}

		public VerificationCheckStatus getVerificationCheck() {
			return verificationCheck;
		}

		public RelationCheckStatus getRelationCheck() {
			return relationCheck;
		}
	}
	
	public static EntityCheck getEntityCheck(int entityId, FWFacade facade) 
	 throws DataException {
		
		// Determine Entity Verification/Check status
		VerificationCheckStatus verificationCheckStatus = 
		 Entity.getVerificationStatus(entityId, facade);
		
		// Determine Relation Check status
		RelationCheckStatus relationCheckStatus = 
		 Entity.getRelationCheckStatus(entityId, facade);
		
		return new EntityCheck(verificationCheckStatus, relationCheckStatus);
	}
	
	public static class Cols {
		public static final String ID = "ORGANISATION_ID";
		public static final String ORG_ACCOUNT_CODE = "ORG_ACCOUNT_CODE";
		public static final String INCORPORATION_COUNTRY = "INCORPORATION_COUNTRY";
		public static final String BUSINESS_DOMICILE = "BUSINESS_DOMICILE";
		public static final String CATEGORY_ID = "CATEGORY_ID";
		public static final String ORGANISATION_NAME = "ORGANISATION_NAME";
		public static final String DATE_ENROLLED = "DATE_ENROLLED";
		public static final String LAST_UPDATED = "LAST_UPDATED";
		public static final String DATE_ADDED = "DATE_ADDED";
		public static final String GROSS_DISTRIBUTION_NUMBER = "GROSS_DISTRIBUTION_NUMBER";
		public static final String CLASSIFICATION_ID = "CLASSIFICATION_ID"; 
		public static final String GSS_LOCATION_ID = "GSS_LOCATION_ID";
	}

	/** Determines whether Entity Check outcomes are determined by Verified Source
	 *  attributes connected to the Organisation, as opposed to the legacy
	 *  CCLA_ENTITY_CHECK table.
	 */
	public static boolean ENTITY_VERIFICATION_ENABLED = false;
	
	/** Determines whether Aurora Client Number mapping must always be generated for 
	 *  new Organisation records.
	 */
	public static boolean FORCE_CLIENT_NUMBER_GENERATION = false;
	
	static {
		String verEnabledStr = SharedObjects.getEnvironmentValue
		 ("CCLA_CS_EnableEntityVerification");
		
		if (!StringUtils.stringIsBlank(verEnabledStr) && !verEnabledStr.equals("0"))
			ENTITY_VERIFICATION_ENABLED = true;
		
		String forceClientNumStr = SharedObjects.getEnvironmentValue
		 ("CCLA_CS_ForceClientNumberGeneration");
		
		if (!StringUtils.stringIsBlank(forceClientNumStr) && !forceClientNumStr.equals("0"))
			FORCE_CLIENT_NUMBER_GENERATION = true;
	}
	
	private int organisationId;
	
	private String organisationName;
	private Integer categoryId;
	private Integer classificationId;
	
	/** 4-alpha, 8 numeric unique identifier code for Organisation */
	private String orgAccountCode;
	private Integer incorporationCountryId;
	private Integer businessDomicileId;
	private String grossDistributionNumber;
	
	private Date dateEnrolled;
	private Date dateAdded;
	private Date lastUpdated;
	
	private Integer gssLocationId;
	
	
	private Vector<Contact> contacts = null;

	
	public Entity(DataBinder binder) throws DataException {
		this.setAttributes(binder);
	}

	public Entity(int organisationId, String orgAccountCode, 
			String organisationName, Integer categoryId, 
			Integer classificationId, Integer incorporationCountryId, Integer businessDomicileId, 
			String grossDistributionNumber, 
			Date dateEnrolled, Date dateAdded, Date lastUpdated,
			Integer gssLocationId) throws DataException {
		
		super(organisationId, ElementType.ORGANISATION);
		
		this.organisationId = organisationId;
		this.orgAccountCode = orgAccountCode;
		
		this.organisationName = organisationName;
		
		this.categoryId = categoryId;
		this.classificationId = classificationId;
		this.incorporationCountryId = incorporationCountryId;
		this.businessDomicileId = businessDomicileId;
		this.grossDistributionNumber = grossDistributionNumber;
		
		this.dateEnrolled = dateEnrolled;
		this.dateAdded = dateAdded;
		this.lastUpdated = lastUpdated;
		
		this.gssLocationId = gssLocationId;
	}

	public static Entity get(DataResultSet rsEntity) throws DataException {
		
		if (rsEntity.isEmpty())
			return null;
		
		return new Entity(
			CCLAUtils.getResultSetIntegerValue(rsEntity, Cols.ID),
			rsEntity.getStringValueByName(Cols.ORG_ACCOUNT_CODE),
			rsEntity.getStringValueByName(Cols.ORGANISATION_NAME),
			CCLAUtils.getResultSetIntegerValue(rsEntity, Cols.CATEGORY_ID),
			CCLAUtils.getResultSetIntegerValue(rsEntity, Cols.CLASSIFICATION_ID),
			CCLAUtils.getResultSetIntegerValue(rsEntity, Cols.INCORPORATION_COUNTRY),
			CCLAUtils.getResultSetIntegerValue(rsEntity, Cols.BUSINESS_DOMICILE),
			rsEntity.getStringValueByName(Cols.GROSS_DISTRIBUTION_NUMBER),
			rsEntity.getDateValueByName(Cols.DATE_ENROLLED),
			rsEntity.getDateValueByName(Cols.DATE_ADDED),
			rsEntity.getDateValueByName(Cols.LAST_UPDATED),
			CCLAUtils.getResultSetIntegerValue(rsEntity, Cols.GSS_LOCATION_ID)
		);
	}
	
	/** Fetches an Entity with the given ID.
	 * 
	 * 	Setting the getContacts parameter to true will prefetch all contact
	 *  data associated with the entity, accessible via the entity.getContacts()
	 *  method.
	 */
	public static Entity get(int organisationId, boolean getContacts, FWFacade facade) 
	 throws DataException {
		
		DataResultSet rsEntity = getData(organisationId, facade);
		
		if (rsEntity.isEmpty())
			return null;
		
		Entity entity = get(rsEntity);
		if (entity != null && getContacts) {
			entity.setContacts(Contact.getElementContacts(organisationId, facade));
		}
		
		return entity;
	}
	
	
	public static Vector<Entity> getAllByCategoryId(int categoryId, FWFacade facade) 
	throws DataException {
		
		DataResultSet rsEntities = getDataByCategoryId(categoryId, facade);
		Vector<Entity> entityVec = new Vector<Entity>();
			
		if (rsEntities.first()) {
			do {
				entityVec.add(get(rsEntities));
			} while (rsEntities.next());
		}
		
		return entityVec;
	}
	
	public static DataResultSet getDataByCategoryId(int categoryId, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.CATEGORY_ID, categoryId);
		
		DataResultSet rsEntity = 
		 facade.createResultSet("qCore_GetAllEntitiesByCategoryId", binder);
		
		return rsEntity;
	}
	
	
	public static Entity getEntityByOrgAccountCode(String orgAccountCode, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryParamToBinder
		 (binder, Cols.ORG_ACCOUNT_CODE, orgAccountCode.trim());
		
		DataResultSet rsEntity = 
		 facade.createResultSet("qClientServices_GetEntityByOrgAccountCode", binder);
		if (rsEntity.getNumRows() == 1)
			return get(rsEntity);
		
		return null;
	}
	
	
	public static Entity get(int organisationId, FWFacade facade) 
	 throws DataException {
		
		return get(organisationId, false, facade);
	}
	
	/** Returns a single matching row from the ORGANISATION table.
	 * 
	 * @param organisationId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getData(int organisationId, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.ID, organisationId);
		
		DataResultSet rsEntity = 
		 facade.createResultSet("qClientServices_GetEntity", binder);
		
		return rsEntity;
	}
	
	/** Returns a single matching row from the ORGANISATION table, plus the selected
	 *  Category Name, if there is one.
	 *  
	 * @param organisationId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getExtendedData(int organisationId, FWFacade facade) 
	 throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.ID, organisationId);
		
		DataResultSet rsEntity = 
		 facade.createResultSet("qClientServices_GetEntityExtended", binder);
		
		return rsEntity;
	}
	
	/** Adds a new Organisation entry to the database.
	 *  
	 *  An AuroraClient mapping will also be generated, providing COMPANY_ID and
	 *  CLIENT_NUMBER values are passed in. 
	 *  
	 *  The ORG_ACCOUNT_CODE is also generated here.
	 *  
	 *  Calls to this method must be wrapped in a transaction block.
	 *  
	 * @param binder
	 * @param facade
	 * @param username
	 * @return
	 * @throws DataException
	 * @throws ServiceException
	 */
	public static Entity add(DataBinder binder, FWFacade facade, String username) 
	 throws DataException, ServiceException {
		
		Element element = Element.add(ElementType.ORGANISATION, null, facade, username);
		Entity entity = new Entity(binder);
		
		entity.setOrganisationId(element.getElementId());
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.ID, entity.getOrganisationId());
		
		// Extract Client Number/Company from binder
		ClientNumber clientNumber = getAuroraClientDataFromBinder(binder, facade);
		
		// Fail immediately if Client Number couldn't be extracted above.
		if (FORCE_CLIENT_NUMBER_GENERATION 
			&& clientNumber == null) {
			
			// Check for special override flag, this will still allow orgs to be
			// generated without applying a Client/Company.
			if (!CCLAUtils.getBinderBoolValue
				(binder, SKIP_AURORA_CLIENT_NUMBER_GENERATION))
				throw new ServiceException("Client Number was not generated - unable " +
				 "to add new Organisation");
		}
		
		// Check the binder for a prefix for the ORG_ACCOUNT_CODE. If one isn't
		// specified, its built from the Org Name instead.
		String orgAccountCodePrefix = binder.getLocal(ORG_ACCOUNT_CODE_PREFIX);
		
		if (StringUtils.stringIsBlank(orgAccountCodePrefix))
			orgAccountCodePrefix = entity.getOrganisationName();
		else
			Log.debug("Found an " + ORG_ACCOUNT_CODE_PREFIX + ": " 
			 + orgAccountCodePrefix);
		
		String orgAccountCode 	= null;	
		
		if (clientNumber != null) {
			Log.debug("Generating Org Account Code using Client Number");
			orgAccountCode = generateOrgAccountCode
			 (orgAccountCodePrefix, clientNumber.getClientNumber());
		} else {
			Log.debug("Generating Org Account Code using Organisation ID");
			orgAccountCode = generateOrgAccountCode
			 (orgAccountCodePrefix, element.getElementId());
		}	
		
		Log.debug("New Org Account Code: " + orgAccountCode);
		entity.setOrgAccountCode(orgAccountCode);
		
		entity.validate(facade);
		
		entity.addFieldsToBinder(binder);
		facade.execute("qClientServices_CreateEntityRecord", binder);
		
		if (clientNumber != null) {
			clientNumber.addFieldsToBinder(binder);
			
			// Add an Aurora Client Mapping if fields are present.
			AuroraClient.add(binder, facade, username);	
		}

		// Add audit record
		DataResultSet newData = Entity.getData(entity.getOrganisationId(), facade);
	
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(element.getElementId(), ElementType.ORGANISATION.getName());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.ADD.toString(), 
		 ElementType.ORGANISATION.getName(), 
		 null, newData, auditRelations);
		
		return entity;
	}
	
	/** Checks the binder for a CLIENT_NUMBER/COMPANY_ID combination.
	 * 
	 *  If a valid Company can't be resolved in the binder, returns null, otherwise:
	 * 
	 *  If the generateClientNumber flag is present in the binder, a new Client Number 
	 *  will be created from the existing ORG_ACCOUNT_CODE or the Client Number
	 *  sequence, in absence of an existing CLIENT_NUMBER in the binder.
	 *  
	 *  If neither can be resolved, returns null.
	 *  
	 * @param binder
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static ClientNumber getAuroraClientDataFromBinder
	 (DataBinder binder, FWFacade facade) throws DataException {
		
		// Check for Aurora identifiers. These will have an impact on how the
		// Account Code is generated, and whether or not a Client-Aurora mapping
		// entry is generated.
		String clientNumStr = binder.getLocal("CLIENT_NUMBER");
		String companyIdStr = binder.getLocal("COMPANY_ID");
		
		Integer clientNum 	= null;
		Company company 	= null;
		
		if (!StringUtils.stringIsBlank(clientNumStr))
			clientNum = Integer.parseInt(clientNumStr);
		
		if (!StringUtils.stringIsBlank(companyIdStr))
			company = Company.getCache().getCachedInstance(
						Integer.parseInt(companyIdStr));
		
		/* Abandon early if Company isn't specified.
		 */
		if (company == null)
			return null;
		
		// Check for 'generateClientNumber' flag. If the flag is set, and a 
		// CLIENT_NUMBER isn't present in the binder, a new Client Number should be 
		// created from one of the below:
		//
		// -the existing ORG_ACCOUNT_CODE, if the numeric suffix is a valid Aurora
		//  Client Number, otherwise
		// -the Client Number sequence.
		boolean generateClientNumber = !StringUtils.stringIsBlank
		 (binder.getLocal("generateClientNumber"));
		
		String orgAccountCode = binder.getLocal(Cols.ORG_ACCOUNT_CODE);
		
		if (!StringUtils.stringIsBlank(orgAccountCode)) {
			// Check if the existing ORG_ACCOUNT_CODE has a valid Client Number suffix.
			int accCodeNum = CCLAUtils.getAccountCodeNumberSuffix(orgAccountCode);
			
			if (AuroraClient.isValidClientNumber(accCodeNum)) {
				Log.debug("The numeric portion of the Org Account Code appear to be " +
				 "a valid Aurora Client Number (" + accCodeNum + "). Using this " +
				 "number to generate Aurora Client mapping");
				
				clientNum = accCodeNum;
			}
		}
		
		if (generateClientNumber && clientNum == null) {
			clientNum = AuroraClient.getNewClientNumber(facade);
			Log.debug("Generated new Client Number from sequence: " + clientNum);
		}
		
		// First see if only one is specified. This is an incomplete
		// mapping and an error must be thrown.
		if ((clientNum != null && company == null)
			||
			(clientNum == null && company != null)) {
			
			throw new DataException("Unable to create/update Organisation: " +
			 "Client Number/Company was missing. You must " +
			 "specify both or neither.");	
		}
		
		if (clientNum != null && company != null)
			return new ClientNumber(clientNum, company);
		else
			return null;
	}
	
	public void addFieldsToBinder(DataBinder binder) throws DataException {
		CCLAUtils.addQueryIntParamToBinder(
				binder, Cols.ID, this.getOrganisationId());
		
		CCLAUtils.addQueryParamToBinder(
				binder, Cols.ORG_ACCOUNT_CODE, this.getOrgAccountCode());
		
		CCLAUtils.addQueryParamToBinder(binder, 
				Cols.ORGANISATION_NAME, this.getOrganisationName());
		
		CCLAUtils.addQueryIntParamToBinder(
				binder, Cols.CATEGORY_ID, this.getCategoryId());
		
		CCLAUtils.addQueryIntParamToBinder(
				binder, Cols.CLASSIFICATION_ID, this.getClassificationId());	
		
		CCLAUtils.addQueryIntParamToBinder(
				binder, Cols.INCORPORATION_COUNTRY, this.getIncorporationCountryId());
		
		CCLAUtils.addQueryIntParamToBinder(
				binder, Cols.BUSINESS_DOMICILE, this.getBusinessDomicileId());
				
		CCLAUtils.addQueryParamToBinder(binder, 
				Cols.GROSS_DISTRIBUTION_NUMBER, this.getGrossDistributionNumber());
		
		CCLAUtils.addQueryDateParamToBinder(binder, 
				Cols.DATE_ENROLLED, this.getDateEnrolled());
		
		CCLAUtils.addQueryIntParamToBinder(
				binder, Cols.GSS_LOCATION_ID, this.getGssLocationId());
	}

	public void persist(FWFacade facade, String username) throws DataException {
		
		this.validate(null);
		
		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);
		
		DataResultSet beforeData = Entity.getData(this.getOrganisationId(), facade);
		
		facade.execute("qClientServices_UpdateEntity", binder);
		DataResultSet afterData = Entity.getData(this.getOrganisationId(), facade);
		
		Entity entity = Entity.get(afterData);

		// Add audit record
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(this.getOrganisationId(), ElementType.ORGANISATION.getName());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.UPDATE.toString(), 
		 ElementType.ORGANISATION.getName(), 
		 beforeData, afterData, auditRelations);
	}
	
	public void setAttributes(DataBinder binder) throws DataException {		

		this.setOrganisationName(binder.getLocal(Cols.ORGANISATION_NAME));
		
		this.setCategoryId(
				CCLAUtils.getBinderIntegerValue(binder, Cols.CATEGORY_ID));
		
		this.setClassificationId(
				CCLAUtils.getBinderIntegerValue(binder, Cols.CLASSIFICATION_ID));	
	
		this.setIncorporationCountryId(
				CCLAUtils.getBinderIntegerValue(binder, Cols.INCORPORATION_COUNTRY));
		
		this.setBusinessDomicileId(
				CCLAUtils.getBinderIntegerValue(binder, Cols.BUSINESS_DOMICILE));
		
		this.setGrossDistributionNumber(binder.getLocal(Cols.GROSS_DISTRIBUTION_NUMBER));
		
		this.setDateEnrolled(
				CCLAUtils.getBinderDateValue(binder, Cols.DATE_ENROLLED));
		
		this.setGssLocationId(
				CCLAUtils.getBinderIntegerValue(binder, Cols.GSS_LOCATION_ID));
	}
	
	/** Returns a list of Person instances who have a direct Correspondent relationship
	 *  to the Organisation.
	 *  
	 *  Returns an empty list if no Correspondents set.
	 * 
	 * @param getContacts whether Contact details are loaded into the Person instances
	 * @return
	 * @throws DataException 
	 */
	public static Vector<Person> getCorrespondents
	 (int orgId, boolean getContacts, FWFacade facade) throws DataException {
		
		Vector<Person> correspondents = new Vector<Person>();
		
		// Fetch all mapped Org -> Person Correspondent relations.
		Vector<Relation> corrRelations = Relation.getRelations
		 (orgId, null, null, 
		 RelationName.getCache().getCachedInstance(
		  RelationName.OrganisationPersonRelation.CORRESPONDENT
		 ), facade);
		
		for (Relation corrRel : corrRelations) {
			Person thisCorr = Person.get(corrRel.getElementId2(), getContacts, facade);
			correspondents.add(thisCorr);
		}
		
		return correspondents;
	}
	
	public Vector<Person> getCorrespondents(boolean getContacts, FWFacade facade) 
	 throws DataException {
		
		return getCorrespondents(this.getOrganisationId(), getContacts, facade);
	}
	
	public int getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(int organisationId) {
		this.organisationId = organisationId;
	}

	public String getOrganisationName() {
		return organisationName;
	}

	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getClassificationId() {
		return classificationId;
	}

	public void setClassificationId(Integer classificationId) {
		this.classificationId = classificationId;
	}

	public Date getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getOrgAccountCode() {
		return orgAccountCode;
	}

	public void setOrgAccountCode(String orgAccountCode) {
		this.orgAccountCode = orgAccountCode;
	}

	public Integer getIncorporationCountryId() {
		return incorporationCountryId;
	}

	public void setIncorporationCountryId(Integer incorporationCountryId) {
		this.incorporationCountryId = incorporationCountryId;
	}

	public Integer getBusinessDomicileId() {
		return businessDomicileId;
	}

	public void setBusinessDomicileId(Integer businessDomicileId) {
		this.businessDomicileId = businessDomicileId;
	}

	public String getGrossDistributionNumber() {
		return grossDistributionNumber;
	}

	public void setGrossDistributionNumber(String grossDistributionNumber) {
		this.grossDistributionNumber = grossDistributionNumber;
	}

	public Date getDateEnrolled() {
		return dateEnrolled;
	}

	public void setDateEnrolled(Date dateEnrolled) {
		this.dateEnrolled = dateEnrolled;
	}

	public void validate(FWFacade facade) throws DataException {
		if (this.getOrgAccountCode() == null)
			throw new DataException("ORG_ACCOUNT_CODE is missing");
		
		// Check Account Code validaty
		CCLAUtils.isAccountCodeValid(this.getOrgAccountCode(), true);
		
		if (StringUtils.stringIsBlank(this.getOrganisationName()))
			throw new DataException("ORGANISATION_NAME is missing");
	}
	
	/** Attempts to resolve an Entity from the given Aurora Client Number/Company, using
	 *  entries from the CLIENT_AURORA_MAP table.
	 * 
	 *  All rows matching the given Client Number will be fetched from the table
	 *  initially. If there is just one match, the corresponding ORGANISATION_ID is 
	 *  used.
	 *  
	 *  If there is more than one match, i.e. same Client Number across multiple
	 *  Companies, the passed Company parameter is used for disambiguation.
	 * 
	 * @param clientNumber
	 * @param company
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Entity getEntityFromAuroraValues
	 (int clientNumber, String company, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "CLIENT_NUMBER", clientNumber);
		
		DataResultSet rsEntities = facade.createResultSet
		 ("qCore_GetEntitiesByClientNumber", binder);
		
		if (rsEntities.isEmpty())
			return null; // No matching Organisations for this Client Number.
		
		if (rsEntities.getNumRows() == 1)
			return get(rsEntities);
		
		Log.debug("Client Number " + clientNumber + " is ambiguous - more than one " +
		 "mapped Organisation ID.");
		
		if (StringUtils.stringIsBlank(company)) {
			String msg = "Unable to resolve Organisation for Client Number " 
			 + CCLAUtils.padClientNumber(Integer.toString(clientNumber)) + 
			 ", a Fund or Company is required for disambiguation";
			
			Log.error(msg);
			throw new DataException(msg);
			
		} else {
			// Loop through returned Aurora Client Number maps and attempt to match
			// against the passed Company code.
			do {
				String thisCompanyCode = 
				 rsEntities.getStringValueByName("COMPANY_CODE");
				
				if (thisCompanyCode.equals(company)) {
					Log.debug("Found a matching Company mapping for " + company);
					return get(rsEntities);
				}
				
			} while (rsEntities.next());
			
			// Still no match! Check for accounts linked to the Client Number.
			// TODO
			// Entity.getOrganisationCompaniesData(clientNumber, facade);
			
			String msg = "Unable to resolve Organisation for Client Number " 
			 + CCLAUtils.padClientNumber(Integer.toString(clientNumber)) + 
			 ", Company " + company;
			
			Log.error(msg);
			throw new DataException(msg);
		}
	}
	
	
	public static Entity getEntityIdByAuroraClientNumberAndFund
		(int clientNumber, String fundCode, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, "CLIENT_NUMBER", clientNumber);
		
		CCLAUtils.addQueryParamToBinder(binder, "FUND_CODE", fundCode);
		
		DataResultSet rsEntityIds = facade.createResultSet
		 ("qCore_GetEntityIdByClientNumberAndFund", binder);
		
		if (rsEntityIds==null || rsEntityIds.isEmpty())
			return null; // No matching Organisations for this Client Number.
		
		
		if (rsEntityIds.getNumRows() == 1) {
			int orgId = CCLAUtils.getResultSetIntegerValue(rsEntityIds, Cols.ID);
			return Entity.get(orgId, facade);
		} else {
			throw new DataException("No matching Organisation with " +
					 "Aurora Client Number: " + clientNumber + ", FundCode: " + 
					 fundCode);
		}
	}
	
	/** Fetches a set of mapped Aurora Client Number/Company
	 *  entries, which map to the corresponding Client records
	 *  in the Aurora database for this particular Entity.
	 *  
	 * @param facade
	 * @return
	 * @throws DataException 
	 * @throws DataException 
	 */
	public static DataResultSet getAuroraClientMappingData
	 (int organisationId, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.ID, organisationId);
		
		DataResultSet rsClientAuroraMap = facade.createResultSet
		 ("qClientServices_GetAuroraClientsByEntityId", binder);
		
		return rsClientAuroraMap;
	}
	
	/** Returns a unique set of entries from the REF_COMPANY table.
	 * 
	 *  The returned companies must own at least one of the Organisation's accounts.
	 *  
	 *  Examples:
	 *  1. an Organisation with no accounts will have nothing returned here.
	 *  2. an Organisation with only COIF accounts will have the COIF company data
	 *     returned.
	 *  3. an Organisation with CBF and PSIC accounts will have the CBF and PSIC company
	 *     data returned.
	 *  
	 * @param organisationId
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getAccountCompaniesData
	 (int organisationId, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.ID, organisationId);
		
		return facade.createResultSet
		 ("qClientServices_GetAccountCompaniesForOrganisation", binder);
	}
	
	/** Returns similar data to getAccountCompaniesData, but includes the actual mapped
	 *  company in the CLIENT_AURORA_MAP table.
	 *  
	 *  So this query will always return at least 1 match, providing they have a mapped
	 *  Aurora Client Number, even if their Organisation has no accounts set up.
	 *  
	 * @param organisationId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static DataResultSet getOrganisationCompaniesData
	 (int organisationId, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.ID, organisationId);
		
		return facade.createResultSet
		 ("qClientServices_GetCompaniesForOrganisation", binder);
	}
	
	/** Returns a set of AuroraClient instances which correspond
	 *  to the given Entity ID.
	 *  
	 * @param organisationId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Vector<AuroraClient> getAuroraClientMapping
	 (int organisationId, FWFacade facade) throws DataException {
		
		Vector<AuroraClient> auroraClients = new Vector<AuroraClient>();
		
		// Fetch raw data
		DataResultSet rsAuroraClientMap = 
		 getAuroraClientMappingData(organisationId, facade);
		
		if (rsAuroraClientMap.isEmpty())
			return auroraClients;
		
		do {
			// Create AuroraClient instances from ResultSet row values
			AuroraClient auroraClient = AuroraClient.get(rsAuroraClientMap);
			
			auroraClients.add(auroraClient);
		} while (rsAuroraClientMap.next());
		
		return auroraClients;
	}
	
	/** Fetches an AuroraClient mapping for a particular Company.
	 * 
	 * @param organisationId
	 * @param facade
	 * @return null, if no such AuroraClient mapping for the Org/Company
	 * @throws DataException
	 */
	public static AuroraClient getAuroraClientCompanyMapping
	 (int organisationId, Company company, FWFacade facade) throws DataException {
		Vector<AuroraClient> auroraClients = getAuroraClientMapping
		 (organisationId, facade);
		
		for (AuroraClient client : auroraClients) {
			if (client.getCompany().getCompanyId() == company.getCompanyId())
				return client;
		}
		
		return null;
	}
	
	/** 
	 * @deprecated 
	 * 
	 * Returns a single mapped AuroraClient for the given Organisation. If there is
	 * more than one found in the DB, the first one is returned.
	 * 
	 * Be extremely wary of using this method now. If an Org has multiple
	 * AuroraClient mappings, against different Client Numbers, this method will simply
	 * return the first one it encounters, which may be incorrect in the given 
	 * circumstance. It is strongly recommended you use getAuroraClientCompanyMapping
	 * instead.
	 * 
	 * If none are found an exception is thrown.
	 * 
	 * @param organisationId
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static AuroraClient getAuroraClient
	 (int organisationId, FWFacade facade) throws DataException {
		
		Vector<AuroraClient> auroraClients = 
		 getAuroraClientMapping(organisationId, facade);
		
		if (auroraClients.size() == 0) {
			// Fail if Organisation doesn't have any mapped Aurora 
			// Client numbers
			throw new DataException("Organisation ID " + 
			 organisationId + " does not have a mapped " +
			 "Aurora Client");
		} else if (auroraClients.size() > 1) {
			
			return auroraClients.get(0);
			
			/*
			// Fail if Organisation has more than 1 mapped Aurora 
			// Client number
			throw new DataException("Organisation ID " + 
			 organisationId + " has more than 1 mapped " +
			 "Aurora Client");
			 */
		}
	
		return auroraClients.get(0);
	}

	public void setContacts(Vector<Contact> contacts) {
		this.contacts = contacts;
	}

	public Vector<Contact> getContacts() {
		return contacts;
	}

	@Override
	public int getElementId() {
		return this.getOrganisationId();
	}

	@Override
	public ElementType getType() {
		return ElementType.ORGANISATION;
	}
	
	/** Temporary method for fetching any of the given organisation's accounts
	 *  from the CCLA_QRS_ACCOUNTS table.
	 *  
	 *  The ResultSet only contains columns from the CCLA_QRS_ACCOUNTS table.
	 *  
	 *  This method is currently used to determine whether the given 
	 *  organisation is a 'QRS client' or not (i.e. they have at least 1 QRS
	 *  account).
	 *  
	 * @param orgId
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static DataResultSet getOrganisationQRSAccountsData
	 (int orgId, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Cols.ID, orgId);
		
		return facade.createResultSet
		 ("qClientServices_GetOrganisationQRSAccounts", binder);
	}
	
	/** New method for generating Organisation Account Codes.
	 *  
	 *  Requires a non-null Organisation Name and an Aurora Client Number. The Client
	 *  Number must be linked to the given Organisation, and must be a valid Aurora
	 *  Client Number.
	 *  
	 *  The Org Account Code is generated in the form:
	 * 
	 * a. First four letters of the passed Organisation name, excluding the
	 * 	  characters in the CCLA_CS_OrgNameRemovableStrings environment variable 
	 *    (plus commas). If the trimmed name is less than 4 characters long, it is
	 *    padded with X's.
	 * 
	 * b. The passed Client Number, left-padded to 8 digits with zeros.
	 * 
	 * @param orgName		Name of the organisation
	 * @param clientNumber	Aurora Client Number
	 * @return
	 * @throws DataException 
	 */
	private static String generateOrgAccountCode(String orgName, int clientNumber) 
	 throws DataException {
		
		if (StringUtils.stringIsBlank(orgName))
			throw new DataException("Unable to generate Org Account Code: " +
			 "Organisation Name missing");
		
		/*
		if (!AuroraClient.isValidClientNumber(clientNumber))
			throw new DataException("Unable to generate Org Account Code: " +
			 "Client Number " + clientNumber + " is too large");
		*/
		
		String[] removableStrings = StringUtils.stringToArray(
		 SharedObjects.getEnvironmentValue("CCLA_CS_OrgNameRemovableStrings"));
		String clientNumStr = Integer.toString(clientNumber);
		
		orgName = orgName.toUpperCase();
		
		for(int i=0; i<removableStrings.length; i++){
			orgName = orgName.replace
			 (removableStrings[i].toUpperCase(), "");
		}
		
		//Also remove commas
		orgName = orgName.replace(",","");
		orgName = orgName.trim();
		
		int alphaPrefixLength = 4;
		int numericSuffixLength = 8;
		
		//If the organisation name length is less than 4, pad with X's
		while (orgName.length() < alphaPrefixLength) {
			orgName += "X";
		}
		
		// Alpha prefix
		String alphaPrefix = orgName.substring(0,alphaPrefixLength);
		
		// Padded numeric portion
		String numericPostfix = CCLAUtils.padString
		 (clientNumStr, '0', numericSuffixLength);
		
		return (alphaPrefix + numericPostfix);
	}
	
	/** Returns whether or not the Entity has been verified against an external source.
	 *  
	 *  If the ENTITY_VERIFICATION_ENABLED flag isn't set, the legacy entity check
	 *  table will be used to determine the check/verification status.
	 *  
	 *  Otherwise the new method will be used.
	 *  
	 *  This involves fetching all Attributes with Verification Sources which have 
	 *  been linked to the given Organisation/Entity.
	 *  
	 *  -If there are no such attributes, the UNKNOWN check status is returned.
	 *  -If all such attributes have False statuses, the FAILED check status is 
	 *   returned, along with a list of the failed sources.
	 *  -If one or more such attributes have a True status, the PASSED check status is
	 *   returned.
	 *  
	 * @param orgId
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static VerificationCheckStatus getVerificationStatus
	 (int orgId, FWFacade facade) throws DataException {
		
		if (!ENTITY_VERIFICATION_ENABLED) {
			// Use legacy entity check data source
			throw new DataException("Legacy Entity Checking no longer supported");
		}

		DataBinder binder = new DataBinder();
		CCLAUtils.addQueryIntParamToBinder(binder, Element.Cols.ID, orgId);
		
		Vector<ElementAttributeApplied> attribsApplied = 
		 ElementAttributeApplied.getAll(orgId, true, facade);
		
		if (attribsApplied.isEmpty()) {
			// No verification attributes set. Return 'unknown' outcome
			return new VerificationCheckStatus
			 (VerificationCheckStatusCode.UNKNOWN, false, null);
		}
		
		boolean override = false;
		boolean hasPassedVerification = false;
		String failedSources = null;
		
		// Search for Entity Verification attributes which have been applied to this
		// Organisation.
		for (ElementAttributeApplied attribApplied : attribsApplied) {
			ElementAttribute thisAttrib = ElementAttribute.getCache().getCachedInstance
			 (attribApplied.getAttributeId());

			if ((thisAttrib.getAttributeType().getElementAttributeTypeId()
				== ElementAttributeType.ENTITY_VERIFICATION)) {

				// Determine the Verification Source
				int verSourceId = thisAttrib.getVerificationSourceId();
				VerificationSource verSource = VerificationSource.getCache()
				 .getCachedInstance(verSourceId);
				
				if (verSource.isOverride())
					override = true;
				
				if (attribApplied.getStatus()) {
					// Org has at least one verified attribute set.
					hasPassedVerification = true;
				} else {
					// Failed verification source.
					if (failedSources != null)
						failedSources += ", " + verSource.getSourceName();
					else
						failedSources = verSource.getSourceName();
				}
			}
		}
		
		if (hasPassedVerification) {
			return new VerificationCheckStatus
			 (VerificationCheckStatusCode.PASSED, override, null);
		} else {
			return new VerificationCheckStatus
			 (VerificationCheckStatusCode.FAILED, override, 
			 "Failed to verify against the following sources: " + failedSources);
		}
	}
	
	/** Returns a code of PASSED if all Authorising Person relations for this 
	 *  Organisation have been marked as verified, or there is a relation check override 
	 *  in place for this Organisation, or the RELATION_CHECK_GLOBAL_OVERRIDE flag is
	 *  set.
	 *  
	 *  Returns a code of FAILED if there are no overrides set and 1 or more Authorising
	 *  Person relations have been explicitly marked as unverified.
	 *  
	 *  Returns UNKNOWN/PASSED otherwise, depending on the 
	 *  RELATION_CHECK_TREAT_UNKNOWN_AS_PASSED flag. An 'unknown' state captures either
	 *  of the following conditions:
	 *  
	 *  - No Authorsing Person relationships set
	 *  - 1 or more unchecked Authorising Person relationships, and no failed ones.
	 *  
	 * @return
	 * @throws DataException 
	 */
	public static RelationCheckStatus getRelationCheckStatus(int orgId, FWFacade facade) 
	 throws DataException {
		
		Log.debug("Checking relation verification status for Org ID: " + orgId);
		
		if (RELATION_CHECK_GLOBAL_OVERRIDE) {
			Log.debug("Global Relation Check override is set. Returning PASSED");
			return new RelationCheckStatus(RelationCheckStatusCode.PASSED, true, null);
		}
		
		// Check for an existing relation check override first.
		// Fetch all applied Relationship Verification attributes for this Organisation.
		// All of these represent an override on the Relation Check currently, so if
		// 1 or more are present, the Relation Check has been overriden.
		Vector<ElementAttributeApplied> overrides = ElementAttributeApplied.getAll
		 (orgId, ElementAttributeType.getCache().getCachedInstance(
				 ElementAttributeType.RELATIONSHIP_VERIFICATION), true, facade);
		
		if (!overrides.isEmpty()) {
			Log.debug("At least 1 Relation Check override has been set");
			return new RelationCheckStatus(RelationCheckStatusCode.PASSED, true, null);
		}
		
		RelationType orgPersonRelationType = 
		 RelationType.getCache().getCachedInstance(RelationType.ORG_PERSON);
		
		RelationName authPersonRelationName = RelationName.getCache().getCachedInstance(
		 RelationName.OrganisationPersonRelation.AUTH_PERSON
		);
		
		// Fetch all Authorizing Person relations data for the given entity.
		DataResultSet rsAuthPersonRelations = Relation.getRelationData
		 (orgId, null, orgPersonRelationType, authPersonRelationName, facade);
		
		int numAuthPersonRelations = rsAuthPersonRelations.getNumRows();
		
		Vector<Person> failedPersons = new Vector<Person>();
		Vector<Person> unknownPersons = new Vector<Person>();
		
		if (rsAuthPersonRelations.first()) {
			do {
				Boolean isVerified = null;
				
				Integer numVerifiedSources = CCLAUtils.getResultSetIntegerValue
				 (rsAuthPersonRelations, Relation.Cols.NUM_VERIFIED_SOURCES);
				
				Integer numUnverifiedSources = CCLAUtils.getResultSetIntegerValue
				 (rsAuthPersonRelations, Relation.Cols.NUM_UNVERIFIED_SOURCES);
				
				if (numVerifiedSources != null && numVerifiedSources > 0) {
					isVerified = true;
				} else if (numUnverifiedSources != null && numUnverifiedSources > 0) {
					isVerified = false;
				}
				
				// If isVerified == null, this relation had no Verified Properties
				// attached, indicating an unknown check status. 
				//
				// If false, this relation has 1 or more negative Verified 
				// Properties set (and no positives), indicating a failed check status.
				if (isVerified == null || !isVerified) {
					Integer personId = CCLAUtils.getResultSetIntegerValue
					 (rsAuthPersonRelations, Relation.Cols.ELEMENT_ID2);
					
					Person person = Person.get(personId, facade);
					
					if (isVerified == null)
						unknownPersons.add(person);
					else if (!isVerified)
						failedPersons.add(person);
				}
				
			} while (rsAuthPersonRelations.next());
		}
		
		Log.debug("Out of " + rsAuthPersonRelations.getNumRows() + 
		 " authorising Persons, " +
		 unknownPersons.size() + " have an unknown check status and " + 
		 failedPersons.size() + " have a failed check status");
		
		// Determine which status code represents an 'Unknown' status.
		// There is a global override switch available that will treat all unknown
		// statuses as PASSED (see below)
		RelationCheckStatusCode unknownStatusCode;
		
		// If the below flag is set, then the Unknown status is returned as PASSED
		if (RELATION_CHECK_TREAT_UNKNOWN_AS_PASSED)
			unknownStatusCode = RelationCheckStatusCode.PASSED;
		else
			unknownStatusCode = RelationCheckStatusCode.UNKNOWN;
		
		// Finally, determine the Relation Check outcome from what we've gathered
		// above
		
		if (numAuthPersonRelations == 0) {
			// No authorising person relations present. Return Unknown status
			
			return new RelationCheckStatus
			 (unknownStatusCode, false, "No authorising persons assigned");
			
		} else if (!failedPersons.isEmpty()) {
			// At least 1 auth person was negatively verified. Return FAILED status
			
			String failedPersonsStr = "";
			
			for (Person person : failedPersons) {
				if (failedPersonsStr.length() > 0)
					failedPersonsStr += ", ";
				
				failedPersonsStr += person.getSalutation();
			}
			
			return new RelationCheckStatus
			 (RelationCheckStatusCode.FAILED, false, 
			  failedPersons.size() + "/" + numAuthPersonRelations + 
			  " Auth. Person relationships have been marked as unverified: " +
			  failedPersonsStr);
		
		} else if (!unknownPersons.isEmpty()) {
			// At least 1 auth person has unknown status.
			
			String unknownPersonsStr = "";
			
			for (Person person : unknownPersons) {
				if (unknownPersonsStr.length() > 0)
					unknownPersonsStr += ", ";
				
				unknownPersonsStr += person.getSalutation();
			}

			return new RelationCheckStatus
			 (unknownStatusCode, false, 
			 unknownPersons.size() + "/" + numAuthPersonRelations + 
			  " Auth. Person relationships have an unknown verification status: " +
			  unknownPersonsStr);
	
		} else {
			// If neither of the previous cases fired, assume all relations have a
			// positive verification status, return PASSED.
			return new RelationCheckStatus(RelationCheckStatusCode.PASSED, false, null);
		}
	}
	
	/** Fetches the organisation's nominated Correspondent.
	 * 
	 *  If there are no set Correspondent relations, the method returns null.
	 * 
	 *  If there is only one Correspondent relation, this person is returned.
	 *  
	 *  In the case of multiple Correspondent relations, each relation is checked for
	 *  the explicit 'default/nominated' property. Providing a relation with this
	 *  property is found, this associated person is returned.
	 *  
	 *  If there are multiple correspondents and none have the nominated property, the
	 *  method returns null.
	 * 
	 * @param accountId
	 * @param getContacts
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Person getNominatedCorrespondent
	 (int orgId, boolean getContacts, FWFacade facade) 
	 throws DataException {
		
		RelationName corrRelName =  RelationName.getCache().getCachedInstance(
		 RelationName.OrganisationPersonRelation.CORRESPONDENT
		);
		
		// Fetch all mapped Org -> Person Correspondent relations.
		Vector<Relation> corrRelations = Relation.getRelations
		 (orgId, null, null, corrRelName, facade);
		
		if (corrRelations.isEmpty()) {
			Log.debug("No correspondent relations for Org ID " + orgId);
			return null;
			
		} else if (corrRelations.size() == 1) {
			Log.debug("Single correspondent found for Org ID " + orgId);
			return Person.get
			 (corrRelations.get(0).getElementId2(), getContacts, facade); 
			
		} else {
			Log.debug("Multiple correspondents found for Org ID " + orgId + 
			 ". Searching for Default/Nominated relation rroperty ");
			
			// Fetch all applied relation properties for the correspondent relations
			// to this account.
			DataResultSet rsRelPropAppl = RelationPropertyApplied.getByRelationsData
			 (orgId, null, null, corrRelName, facade);
			
			Vector<RelationPropertyApplied> relPropsAppl = 
			 RelationPropertyApplied.getAll(rsRelPropAppl);
			
			for (RelationPropertyApplied relPropAppl : relPropsAppl) {
				if (relPropAppl.getRelationProperty().getProperty().getPropertyId()
					==
					Property.Ids.DEFAULT) {
					
					for (Relation corrRel : corrRelations) {
						if (corrRel.getRelationId() == relPropAppl.getRelationId()) {
							Log.debug("Found explicitly nominated correspondent with " +
							 "Person ID " + corrRel.getElementId2());
							return Person.get
							 (corrRel.getElementId2(), getContacts, facade); 
						}
					}
				}
			}
			
			Log.debug("No explicitly nominated correspondent found");
			return null;
		}
	}
	
	/** Returns a mapping of relation names to lists of Person instances
	 *  for this org.
	 *  
	 *  e.g. Correspondent 	-> Person A
	 *  	 Signatory		-> Person A, Person B, Person C
	 *  	 Trustee		-> Person C, Person D
	 *  
	 *  The same Person may appear more than once across the returned Person 
	 *  lists, e.g. they are a Correspondent and Signatory for the org.
	 *  
	 *  If getContacts is true, the Person instances are loaded with their associated
	 *  Contact details available via their getContacts() method.
	 *  
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public HashMap<RelationName, Vector<Person>> getRelatedPersons
	 (boolean getContacts, FWFacade facade) 
	 throws DataException {
		RelationType relType = RelationType.getCache().getCachedInstance
		 (RelationType.ORG_PERSON);
		
		Vector<Relation> relations =
		 Relation.getRelations(this.getOrganisationId(), null, relType, null, facade);
		
		HashMap<RelationName, Vector<Person>> relatedPersons = 
		 new HashMap<RelationName, Vector<Person>>();
		
		HashMap<Integer, Person> persons = new HashMap<Integer, Person>();
		
		for (Relation relation: relations) {			
			RelationName relationName = relation.getRelationName();
			
			int personId = relation.getElementId1();
			Person person = null;
			
			if (persons.containsKey(personId))
				person = persons.get(personId); // Already fetched this Person record.
			else {
				// Must fetch new Person record from DB.
				person = Person.get(relation.getElementId2(), getContacts, facade);
				persons.put(person.getPersonId(), person);
			}
			
			if (relatedPersons.containsKey(relationName)) {
				Vector<Person> relPersons = relatedPersons.get(relationName);
				relPersons.add(person);
				
			} else {
				Vector<Person> relPersons = new Vector<Person>();
				relPersons.add(person);
				
				relatedPersons.put(relationName, relPersons);
			}
		}

		return relatedPersons;
	}

	public void setGssLocationId(Integer gssLocationId) {
		this.gssLocationId = gssLocationId;
	}

	public Integer getGssLocationId() {
		return gssLocationId;
	}
	

	/* ************************ Caching ************************************** */	
	private static Cache CACHE = new Cache();
	
	public static Cachable<Integer, Entity> getTTLACache() {
		return CACHE;
	}
	
	/** StaticDataUpdateApplied cache implementor */
	private static class Cache extends Cachable<Integer, Entity> {

		public Cache() {
			super("TTLA Organisation");
		}
		
		protected void doRebuild(FWFacade facade) throws DataException {
			
			HashMap<Integer, Entity> newCache = new HashMap<Integer, Entity>();	
			Integer ttlaCategoryId = SharedObjects.getEnvironmentInt("CF_TTLA_CATEGORY_ID", 0);
			
			if (ttlaCategoryId!=0) {
					Vector<Entity> entityVec = Entity.getAllByCategoryId(ttlaCategoryId, facade);
				
				for (Entity entity : entityVec) {
					newCache.put(entity.getOrganisationId(), entity);
				}
			}
			this.CACHE_MAP = newCache;
		}
	}

	/** Fetches a ResultSet of all Organisations with the given applied attribute set
	 *  to the given status.
	 *  
	 * @param elementAttributeId
	 * @param status
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static ResultSet getOrgsWithAttributeStatus(int elementAttributeId,
	 boolean status, FWFacade facade) throws DataException {
		
		DataBinder binder = new DataBinder();
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, ElementAttribute.Cols.ID, elementAttributeId);
		CCLAUtils.addQueryBooleanParamToBinder
		 (binder, ElementAttributeApplied.Cols.ATTRIBUTE_STATUS, status);
		
		return facade.createResultSet
		 ("qCore_GetOrgsWithAttributeStatus", binder);
	}

	public DataResultSet getAuroraCompaniesData(FWFacade facade)
			throws DataException {
		return AuroraClient.getAuroraCompaniesDataByOrgId
		 (this.getOrganisationId(), facade);
	}
	
	public Vector<Company> getAuroraCompanies(FWFacade facade)
	 throws DataException {
		DataResultSet rsCompanies = getAuroraCompaniesData(facade);
		return Company.getAll(rsCompanies);
	}

	public String getId() {
		return Integer.toString(this.getOrganisationId());
	}
	
	public String getComparisonLabel() {
		return "Central DB Organisation/Aurora Client";
	}

	@Override
	public int hashCode() {
		return this.getOrganisationId();
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.getOrganisationName();
	}	
}
