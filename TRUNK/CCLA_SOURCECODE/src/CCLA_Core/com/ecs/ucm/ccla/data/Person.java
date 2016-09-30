package com.ecs.ucm.ccla.data;

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import org.apache.commons.lang.WordUtils;

import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.shared.SharedObjects;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.CacheManager;
import com.ecs.ucm.ccla.aurora.compare.AuroraEntitySource;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

public class Person extends Element 
 implements Persistable, AuroraEntitySource, Comparable<Person> {
	
	public static boolean DO_CAPITALISATION = SharedObjects
	.getEnvironmentValue("CCLA_CS_NAME_CAPITALISATION").equals("1");	
	
	public static final String PERSON_ACCOUNT_CODE_PREFIX = 
	 "PERSON_ACCOUNT_CODE_PREFIX";
	
	/** 4-character prefix used when generating Person Account Codes from 
	 *  Aurora identifiers.
	 */
	public static final String AURORA_PERSON_ACCOUNT_CODE_PREFIX = "AURP";
	
	public static final class Cols {
		public static final String ID 						= "PERSON_ID";
		public static final String PERSON_ACCOUNT_CODE		= "PERSON_ACCOUNT_CODE";
		public static final String SALUTATION 				= "SALUTATION";
		public static final String TITLE_ID					= "TITLE_ID";
		public static final String FIRST_NAME				= "FIRST_NAME";
		public static final String MIDDLE_NAME				= "MIDDLE_NAME";
		public static final String LAST_NAME				= "LAST_NAME";
		public static final String FULL_NAME				= "FULL_NAME";	
		public static final String GENDER_ID				= "GENDER_ID";
		public static final String QUALIFICATIONS			= "QUALIFICATIONS";
		public static final String JOB_TITLE				= "JOB_TITLE";
		public static final String DATE_OF_BIRTH			= "DATE_OF_BIRTH";
		public static final String CONSENT_TO_AUTH			= "CONSENT_TO_AUTH";
		public static final String IS_DECEASED				= "IS_DECEASED";
		public static final String LAST_UPDATED				= "LAST_UPDATED";
		public static final String DATE_ADDED				= "DATE_ADDED";
		public static final String HEARD_ABOUT_CCLA			= "HEARD_ABOUT_CCLA";
		
		public static final String TYPE						= "TYPE";
	}
	
	private int personId;
	private String accountCode; /* Displayable Person ID */

	private String salutation;
	private Integer titleId;
	private String firstName;
	private String middleName;
	private String lastName;
	private String fullName;
	
	private String qualifications;
	private String jobTitle;
	
	private Date dateOfBirth;
	private String gender;
	
	private String personType;
	private Boolean consentToAuth;
	private boolean deceased;
	
	private Date lastUpdated;
	
	private Vector<Contact> contacts = null;
	
	private DataResultSet data;
	
	public static final class PersonIdentityCheck {
		
		/** Determines general ID check status of person */
		private Boolean idChecked = null;
		/** Determines PEP-only check status of person */
		private Boolean pepChecked = null;
		
		/** Whether or not this person has the account-level override applied.
		 */
		private boolean accountCheckOverride = false;
		
		/** Whether or not this person has the account-level PEP-only override applied.
		 * 
		 */
		private boolean pepCheckOverride = false;
		
		public PersonIdentityCheck() {
		}

		public void setPepChecked(Boolean pepChecked) {
			this.pepChecked = pepChecked;
		}
		public Boolean getPepChecked() {
			return pepChecked;
		}
		public void setIdChecked(Boolean idChecked) {
			this.idChecked = idChecked;
		}
		public Boolean getIdChecked() {
			return idChecked;
		}

		public void setAccountCheckOverride(boolean accountCheckOverride) {
			this.accountCheckOverride = accountCheckOverride;
		}

		public boolean isAccountCheckOverride() {
			return accountCheckOverride;
		}

		public void setPepCheckOverride(boolean pepCheckOverride) {
			this.pepCheckOverride = pepCheckOverride;
		}

		public boolean isPepCheckOverride() {
			return pepCheckOverride;
		}

		@Override
		public String toString() {
			return "PersonIdentityCheck [accountCheckOverride="
					+ accountCheckOverride + ", idChecked=" + idChecked
					+ ", pepCheckOverride=" + pepCheckOverride
					+ ", pepChecked=" + pepChecked + "]";
		}
	}
	
	public Person(Element element, int personId, String accountCode, String salutation,
			Integer titleId, String firstName, String middleName,
			String lastName, String fullName, 
			String qualifications, String jobTitle,
			Date dateOfBirth, String gender, String personType,
			Boolean consentToAuth, boolean deceased,
			Date lastUpdated, DataResultSet data) {
		
		super(element);
		
		this.personId = personId;
		this.accountCode = accountCode;
		
		this.salutation = salutation;
		this.titleId = titleId;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.fullName = fullName;
		
		this.qualifications = qualifications;
		this.jobTitle = jobTitle;
		
		this.dateOfBirth = dateOfBirth;
		this.gender = gender;
		this.personType = personType;
		this.consentToAuth = consentToAuth;
		this.deceased = deceased;
		
		this.lastUpdated = lastUpdated;
		this.data = data;
	}
	
	/** Creates a new Person instance using any passed fields from
	 *  the binder. Will not set the PERSON_ID.
	 *  
	 * @param binder
	 * @throws DataException
	 */
	public Person(DataBinder binder) throws DataException {
		this.setAttributes(binder);
	}
	
	/** Returns a person for the given person ID.
	 * 
	 *  Does not fetch contact information.
	 * 
	 * @param clientId
	 * @param company
	 */
	public static Person get(int personId, FWFacade facade)
	 throws DataException {
		
		return get(personId, false, facade);
	}
	
	/** Returns a person for the given person ID.
	 *  
	 *  Setting the getContacts parameter to true will prefetch all contact
	 *  data associated with the person, accessible via the person.getContacts()
	 *  method.
	 *  
	 * @param clientId
	 * @param company
	 */
	public static Person get(int personId, boolean getContacts, FWFacade facade)
	 throws DataException {
		
		DataResultSet rsPerson =
		 getData(personId, facade);
		
		Person person = get(rsPerson);
		
		if (person != null && getContacts) {
			person.setContacts(Contact.getElementContacts(personId, facade));
			
			/*
			Log.debug("Fetched person " + person.getSalutation() + " with " + 
			 person.getContacts().size() + " contact details:");
			
			for (Contact contact:person.getContacts()) {
				Log.debug(contact.toString());
				
				if (contact.getAddress() != null)
					Log.debug(" Address: " + contact.getAddress().toString());
			}
			*/
		}
		
		return person;
	}
	
	public static Person get(DataResultSet rsPerson) throws DataException {
		if (rsPerson.isEmpty())
			return null;
		
		Element element = Element.get(rsPerson);
		
		Person person = new Person(
		 element,
		 CCLAUtils.getResultSetIntegerValue
		  (rsPerson, Cols.ID),
		 rsPerson.getStringValueByName(Cols.PERSON_ACCOUNT_CODE),
		 rsPerson.getStringValueByName(Cols.SALUTATION),
		 CCLAUtils.getResultSetIntegerValue
		  (rsPerson, Cols.TITLE_ID),			 
		 rsPerson.getStringValueByName(Cols.FIRST_NAME),
		 rsPerson.getStringValueByName(Cols.MIDDLE_NAME),
		 rsPerson.getStringValueByName(Cols.LAST_NAME),
		 rsPerson.getStringValueByName(Cols.FULL_NAME),
		 rsPerson.getStringValueByName(Cols.QUALIFICATIONS),
		 rsPerson.getStringValueByName(Cols.JOB_TITLE),
		 rsPerson.getDateValueByName(Cols.DATE_OF_BIRTH),
		 rsPerson.getStringValueByName(Cols.GENDER_ID),
		 rsPerson.getStringValueByName(Cols.TYPE),
		 CCLAUtils.getResultSetBoolValueAllowNull(rsPerson, Cols.CONSENT_TO_AUTH),
		 CCLAUtils.getResultSetBoolValue(rsPerson, Cols.IS_DECEASED),
		 rsPerson.getDateValueByName(Cols.LAST_UPDATED),
		 rsPerson
		);
			
		return person;
	}
	
	/** Returns a row from the PERSON table for the given person 
	 *  ID.
	 * 
	 * @param personID
	 * @param facade
	 */
	public static DataResultSet getData(int personId, FWFacade facade)
	 throws DataException {
		DataBinder binder = new DataBinder();
		binder.putLocal(Cols.ID, Integer.toString(personId));
		
		DataResultSet rs =
		 facade.createResultSet("qClientServices_GetPersonDetails", binder);
		
		if (!rs.isEmpty()) {
			return rs;
		} else {
			Log.debug("No person found for person ID:" + personId);
			return rs;
		}
	}
	
	public void persist(FWFacade facade, String username) throws DataException {
		this.validate(null);
		
		DataBinder binder = new DataBinder();
		this.addFieldsToBinder(binder);
		
		DataResultSet beforeData = Person.getData(this.getPersonId(), facade);
		
		facade.execute("qClientServices_UpdatePersonRecord", binder);
		
		DataResultSet afterData = Person.getData(this.getPersonId(), facade);
		
		// Add audit record
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(this.getPersonId(), ElementType.PERSON.getName());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.UPDATE.toString(), 
		 ElementType.PERSON.getName(), 
		 beforeData, afterData, auditRelations);
	}
	
	public void addFieldsToBinder(DataBinder binder) {

		CCLAUtils.addQueryIntParamToBinder(binder, Cols.ID, personId);
		CCLAUtils.addQueryParamToBinder(binder, Cols.PERSON_ACCOUNT_CODE, accountCode);
		CCLAUtils.addQueryParamToBinder(binder, Cols.SALUTATION, salutation);
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.TITLE_ID, titleId);
		CCLAUtils.addQueryParamToBinder(binder, Cols.FIRST_NAME, firstName);
		CCLAUtils.addQueryParamToBinder(binder, Cols.MIDDLE_NAME, middleName);
		CCLAUtils.addQueryParamToBinder(binder, Cols.LAST_NAME, lastName);
		CCLAUtils.addQueryParamToBinder(binder, Cols.FULL_NAME, fullName);
		CCLAUtils.addQueryParamToBinder(binder, Cols.QUALIFICATIONS, qualifications);
		CCLAUtils.addQueryParamToBinder(binder, Cols.JOB_TITLE, jobTitle);
		CCLAUtils.addQueryDateParamToBinder(binder,Cols.DATE_OF_BIRTH,dateOfBirth);
		CCLAUtils.addQueryParamToBinder(binder, Cols.GENDER_ID, gender);
		CCLAUtils.addQueryParamToBinder(binder, Cols.TYPE, personType);

		CCLAUtils.addQueryBooleanParamToBinderAllowNull(binder, 
		 Cols.CONSENT_TO_AUTH, consentToAuth);
		CCLAUtils.addQueryBooleanParamToBinder(binder, 
		 Cols.IS_DECEASED, deceased);
	}
	
	/** Sets all Person attributes to match those in the given DataBinder.
	 *  
	 *  Empty/missing attributes will have their associated instance values
	 *  set to null.
	 *  
	 * @param binder
	 * @throws DataException 
	 */
	public void setAttributes(DataBinder binder) throws DataException {
		
		this.setAccountCode(binder.getLocal(Cols.PERSON_ACCOUNT_CODE));
		
		this.setSalutation(binder.getLocal(Cols.SALUTATION));
		this.setTitle(CCLAUtils.getBinderIntegerValue(binder, Cols.TITLE_ID));
		this.setFirstName(binder.getLocal(Cols.FIRST_NAME));
		this.setMiddleName(binder.getLocal(Cols.MIDDLE_NAME));
		this.setLastName(binder.getLocal(Cols.LAST_NAME));
		String FULLNAME = "";
		if (!StringUtils.stringIsBlank(binder.getLocal(Cols.TITLE_ID)))
		{
		try {
			String titleIDstr = binder.getLocal(Cols.TITLE_ID);
			int titleID = Integer.parseInt(titleIDstr);
			PersonTitle pTitle = PersonTitle.getCache().getCachedInstance(titleID);
			String titleName = pTitle.getTitle();
			if (!StringUtils.stringIsBlank(titleName))
				FULLNAME = FULLNAME + titleName + " ";
		} catch (NumberFormatException e) {
			// not too important so don't stop calculating full name
			Log.error(e.getMessage());
		}
		}
		// should always have first and second name
		FULLNAME = FULLNAME + binder.getLocal(Cols.FIRST_NAME) + " ";
		if (!StringUtils.stringIsBlank(binder.getLocal(Cols.MIDDLE_NAME)))
				FULLNAME = FULLNAME + binder.getLocal(Cols.MIDDLE_NAME) + " ";
		FULLNAME = FULLNAME + binder.getLocal(Cols.LAST_NAME);
		this.setFullName(FULLNAME);
		
		this.setQualifications(binder.getLocal(Cols.QUALIFICATIONS));
		this.setJobTitle(binder.getLocal(Cols.JOB_TITLE));
		
		this.setDateOfBirth(CCLAUtils.getBinderDateValue
		 (binder, Cols.DATE_OF_BIRTH));
		this.setGender(binder.getLocal(Cols.GENDER_ID));
		
		this.setPersonType(binder.getLocal(Cols.TYPE));
		this.setConsentToAuth(
		 CCLAUtils.getBinderBoolValueAllowNull(binder, Cols.CONSENT_TO_AUTH));
		
		this.setDeceased(
		 CCLAUtils.getBinderBoolValue(binder, Cols.IS_DECEASED));
	}
	
	/** Adds a new Person instance, using any fields from the
	 *  passed DataBinder. Will deal with any missing fields.
	 *  
	 *  Requires an initial Element object to reserve an Element ID.
	 *  
	 *  If the passed DataBinder contains CORR_ID and COMPANY values,
	 *  a new Person Aurora Mapping is also created for this Person.
	 *  This will affect how the Person Account Code is generated.
	 *  
	 * @param binder
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public static Person add(DataBinder binder, FWFacade facade, String username) 
	 throws DataException {
		
		boolean doNotContactFlag = CCLAUtils.getBinderBoolValue(binder,"DO_NOT_CONTACT_FLAG");
		Integer preferredContactMethod = CCLAUtils.getBinderIntegerValue(binder,"PREFERRED_CONTACTMETHOD");
		Element element = Element.add
		 (ElementType.PERSON, null, doNotContactFlag, preferredContactMethod, null,
		 facade, username);
		
		// Check for an Aurora Company. This is used to create a new Person Aurora 
		// mapping.
		Integer corrId		= CCLAUtils.getBinderIntegerValue
		 (binder, AuroraCorrespondent.Cols.CORR);
		Integer companyId	= CCLAUtils.getBinderIntegerValue
		 (binder, AuroraCorrespondent.Cols.COMPANY);

		Company company = null;
		
		if (companyId != null) {
			// Sufficient data to create an Aurora Correspondent mapping.	
			company	= Company.getCache().getCachedInstance(companyId);
		}
		
		Person person = new Person(binder);
		person.setPersonId(element.getElementId());
		
		// Look for a fixed Person Account Code prefix in the passed binder.
		String codePrefix = binder.getLocal(PERSON_ACCOUNT_CODE_PREFIX);
		
		String accountCode = calculateAccountCode(person, corrId, company, codePrefix);
		person.setAccountCode(accountCode);
		
		person.validate(facade);
		
		person.addFieldsToBinder(binder);
		facade.execute("qClientServices_CreatePersonRecord", binder);
		
		Log.debug("Added new Person record with ID " + person.getPersonId());
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, SharedCols.PERSON, person.getPersonId());
		
		if (company != null) {
			// Create a new Aurora Correspondent mapping.
			AuroraCorrespondent corr = 
			 AuroraCorrespondent.add(binder, facade, username);
			
			Log.debug("Linked to Aurora Correspondent with Map ID: " + corr.getMapId());
		}
		
		// Add audit record
		DataResultSet newData = Person.getData(person.getPersonId(), facade);
		
		HashMap<Integer, String> auditRelations = new HashMap<Integer, String>();
		auditRelations.put(person.getPersonId(), ElementType.PERSON.getName());
		
		Audit.addAudit(facade, Globals.STATIC_DATA_AUDIT_PROFILE, username, 
		 Globals.AuditActions.ADD.toString(), 
		 ElementType.PERSON.getName(), 
		 null, newData, auditRelations);
		
		return person;
	}
	
	public DataResultSet getAccountData(FWFacade facade) throws DataException {
		DataBinder binder = new DataBinder();
		binder.putLocal(Cols.ID, Integer.toString(this.getPersonId()));
		
		DataResultSet rsAccounts = 
		 facade.createResultSet("qClientServices_GetPersonAccounts", binder);
		
		return rsAccounts;
	}
	
	public DataResultSet getPersonsData
	 (Vector<Integer> personIds, FWFacade facade) throws DataException {
		
		String personIdList = "";
		
		for (Integer i:personIds) {
			if (personIdList.length() > 0)
				personIdList += ",";
			
			personIdList += i.intValue();
		}
		
		DataBinder binder = new DataBinder();
		binder.putLocal("personIdList", personIdList);
		
		return facade.createResultSet
		 ("qClientServices_GetPersons", binder);
	}
	
	/** Returns the Person's name.
	 *  
	 *  First checks for a non-null secondName. If non-null, the split-up
	 *  name portions are concatenated to give the person's full name.
	 *  
	 *  Otherwise, the fullName field is returned. This will only be non-
	 *  null for legacy Aurora records which haven't been cleaned up yet.
	 * 
	 * 
	 * @return
	 * @throws DataException 
	 */
	@Override
	public String getName() {
		return this.getFullName();
	}
	
	public DataResultSet getData() {
		return data;	
	}

	public int getPersonId() {
		return personId;
	}

	public void setPersonId(int personId) {
		this.personId = personId;
	}

	public String getSalutation() {
		return salutation;
	}

	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}

	public Integer getTitleId() {
		return titleId;
	}

	public void setTitle(Integer titleId) {
		this.titleId = titleId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = CCLAUtils.capitaliseName(firstName);
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = CCLAUtils.capitaliseName(middleName);
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = CCLAUtils.capitaliseName(lastName);
	}

	/** Fetches a 'complete' rendition of the Person's name.
	 
	  Legacy records may only have the Full Name field set and not the composite
	  Title/firstName/lastName fields.
	  
	  The middleName field is not included in the returned output.
	 * @throws DataException 
	  
	*/
	public String getCompleteName() throws DataException {
		if (lastName == null || lastName.length()==0)
			return fullName;
		else {
			PersonTitle title = null;
			
			if (titleId != null)
				title = PersonTitle.getCache().getCachedInstance(titleId);
			
			// Returns <Title + " " + firstName + " " + lastName>
			return (title != null ? title.getTitle() + " " : null)
					+
					(!StringUtils.stringIsBlank(firstName) ? firstName + " " : "")
					+
					lastName;
		}
	}
	
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getQualifications() {
		return qualifications;
	}

	public void setQualifications(String qualifications) {
		this.qualifications = qualifications;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPersonType() {
		return personType;
	}

	public void setPersonType(String type) {
		this.personType = type;
	}

	public Date getDateLastUpdated() {
		return lastUpdated;
	}

	public void setDateLastUpdated(Date dateLastUpdated) {
		this.lastUpdated = dateLastUpdated;
	}

	public void setData(DataResultSet data) {
		this.data = data;
	}

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	public Boolean isConsentToAuth() {
		return consentToAuth;
	}

	public void setConsentToAuth(Boolean consentToAuth) {
		this.consentToAuth = consentToAuth;
	}

	public void validate(FWFacade facade) throws DataException {
		// Perform trimming of name elements here.
		
		if (this.getFirstName() != null)
			this.setFirstName(this.getFirstName().trim());
		
		if (this.getMiddleName() != null)
			this.setMiddleName(this.getMiddleName().trim());
		
		if (this.getLastName() != null)
			this.setLastName(this.getLastName().trim());
		
		if (this.getFullName() != null)
			this.setFullName(this.getFullName().trim());
		
		if (this.getSalutation() != null)
			this.setSalutation(this.getSalutation().trim());
	}

	public void setContacts(Vector<Contact> contacts) {
		this.contacts = contacts;
	}

	public Vector<Contact> getContacts() {
		return contacts;
	}

	@Override
	public String toString() {
		return "Person [accountCode=" + accountCode + ", consentToAuth="
				+ consentToAuth + ", contacts=" + contacts + ", data=" + data
				+ ", dateOfBirth=" + dateOfBirth + ", firstName=" + firstName
				+ ", fullName=" + fullName + ", gender=" + gender
				+ ", jobTitle=" + jobTitle
				+ ", lastUpdated=" + lastUpdated + ", middleName=" + middleName
				+ ", personId=" + personId + ", qualifications="
				+ qualifications + ", salutation=" + salutation
				+ ", lastName=" + lastName + ", titleId=" + titleId + ", type="
				+ personType + "]";
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public boolean isDeceased() {
		return deceased;
	}

	public void setDeceased(boolean deceased) {
		this.deceased = deceased;
	}

	@Override
	public int getElementId() {
		return this.getPersonId();
	}
	
	@Override
	public ElementType getType() {
		return ElementType.PERSON;
	}
	
	/** Caculates the Person Account Code, a unique display ID for the Person.
	 *  
	 *  The method used to generate the code is dependent on the passed 
	 *  parameters.
	 *  
	 *  If the codePrefix parameter is non-empty/null:
	 *  
	 *  The first 4 characters of the codePrefix is used as the prefix. Throws exception
	 *  if the given prefix is less than 4 characters long.
	 *  
	 *  The numeric portion is the Person ID, zero-padded to 8 digits.
	 *  
	 *  Else if corrId and company are NOT null:
	 *  
	 *  The code is prefixed by AURP, followed by the passed Correspondent ID
	 *  padded to 8 characters.
	 *  
	 *  Otherwise:
	 *  
	 *  The prefix is the first 3 characters of second name (padded with X's 
	 *  if less than 3), the first initial of first name, and then the person id (padded 
	 *  with 0's at the front if less than 8 digits) to make a string of 12 
	 *  characters
	 * 
	 * @return
	 * @throws DataException 
	 * @throws DataException 
	 */
	public static String calculateAccountCode
	 (Person person, Integer corrId, Company company, String codePrefix) 
	 throws DataException {
		
		String accountCodePrefix 	= null;
		String accountCodeSuffix 	= null;
		
		String numberStr		 	= null;
		
		if (!StringUtils.stringIsBlank(codePrefix)) {
			if (codePrefix.length() < 4)
				throw new DataException("Unable to build Person Account Code, passed " +
				 "code prefix was less than 4 characters (" + codePrefix + ")");
			
			accountCodePrefix 	= codePrefix.substring(0, 4).toUpperCase();
			numberStr = Integer.toString(person.getPersonId());
		
		} else if (corrId != null && company != null) {
			// Use the Aurora identifiers to build the Account Code.
			accountCodePrefix = AURORA_PERSON_ACCOUNT_CODE_PREFIX;
			numberStr = Integer.toString(corrId);
			
		} else {
			// Build the Account Code using the person's name and Element ID.
			
			String SName 		= person.getLastName();
			String FName 		= person.getFirstName();
			Integer personId 	= person.getPersonId();
			
			Log.debug
			 ("SName:" + SName + " FName:" + FName + " personId:" + personId);
		
			if (StringUtils.stringIsBlank(SName) 
				|| 
				StringUtils.stringIsBlank(FName) 
				|| 
				personId == null) 
				throw new DataException("Cannot create person account code, " +
				 "missing first name, last name or person id");
			
			SName = SName.toUpperCase();
			// strip out all spaces and apostrophes
			
			SName = SName.replaceAll("'", "");
			SName = SName.replaceAll(" ", "");
			SName = SName.replaceAll("-", "");
			
			StringBuffer buffer = new StringBuffer();
			
			if (SName.length()>=3) {
				buffer.append(SName.substring(0, 3));
			} else {
				// Very short surname. Pad with X's.
				buffer.append(SName);
				
				while (buffer.length() < 3) {
					buffer.append("X"); // pad surname with X's
				}
			}
			
			// Add first letter of first name.
			buffer.append(FName.substring(0,1).toUpperCase());
			accountCodePrefix = buffer.toString();
			
			numberStr = Integer.toString(personId);
		}
		
		accountCodeSuffix = CCLAUtils.padString
		 (numberStr, '0', 8);

		String accountCode = accountCodePrefix + accountCodeSuffix;
		
		Log.debug("Calculated Account Code as " + accountCode);
		
		return accountCode;
	}
	
	
	public static PersonIdentityCheck getIdentityCheckFromAttributes
	 (Vector<ElementAttributeApplied> personAttrs) {
		
		PersonIdentityCheck personIdCheck = new PersonIdentityCheck();
		
		// Relevant Person check attributes
		ElementAttributeApplied idChecked = null, 
								amlChecked = null, 
								pepChecked = null,
								accountOverride = null,
								pepOverride = null;
		
		// Scoop up relevant applied Attributes for this person, where applicable
		for (ElementAttributeApplied personAttr : personAttrs) {	
			// We are only interested in boolean attributes
			if (personAttr.getStatus() != null) {
				switch (personAttr.getAttributeId()) {
					case ElementAttribute.PersonAttributes.IDENTITY_CHECKED : {
						// 'Identity Checked' attribute
						idChecked = personAttr;
						personIdCheck.setIdChecked(personAttr.getStatus());
						break;
					}
					case ElementAttribute.PersonAttributes.AML_TRACKER_CHECKED : {
						// 'AML Tracker Checked' attribute
						amlChecked = personAttr;
						personIdCheck.setIdChecked(personAttr.getStatus());
						break;
					}
					case ElementAttribute.PersonAttributes.PEP_CHECKED : {
						// 'PEP Checked' attribute
						pepChecked = personAttr;
						break;
					}
					case ElementAttribute.PersonAttributes.PEP_CHECK_OVERRIDE : {
						// 'PEP Override' attribute
						pepOverride = personAttr;
						break;
					}
					case ElementAttribute.PersonAttributes.ACCOUNT_IVS_OVERRIDE : {
						// Account-level ID Check override
						accountOverride = personAttr;
						break;
					}
				}
			}
		}
		
		if (idChecked == null && amlChecked == null) {
			personIdCheck.setIdChecked(null);
		} else if ((idChecked != null && idChecked.getStatus())
			||
			(amlChecked != null && amlChecked.getStatus())) {
			personIdCheck.setIdChecked(true);
		} else if ((idChecked != null && !idChecked.getStatus())
				||
				(amlChecked != null && !amlChecked.getStatus())) {
			personIdCheck.setIdChecked(false);
		}
		
		if (pepChecked != null) {
			personIdCheck.setPepChecked(pepChecked.getStatus());
		}
		
		personIdCheck.setAccountCheckOverride
		 (accountOverride != null && accountOverride.getStatus());
		
		personIdCheck.setPepCheckOverride
		 (pepOverride != null && pepOverride.getStatus());
		
		return personIdCheck;
	}
	
	@Override
	public boolean equals(Object person) {
		return (person instanceof Person
				&&
				this.getPersonId() == ((Person)person).getPersonId());
	}
	
	/**
	 * This value cannot be set and returns the title, 
	 * initials of first and second (if exist) name and last name.
	 * 
	 * If the title field isn't set, just return the full name as-is.
	 * 
	 * @return
	 */
	public String getAbbrevatedFullName() {
		
		String abbrevatedName = "";
		
		if (this.getTitleId() == null) {
			// No title set - assume this is an imported Aurora name.
			// Just return the full name 'as-is'
			return this.getFullName();
		}
		
		if (this.getTitleId()!=null) {
			try {
				PersonTitle pTitle = PersonTitle.getCache().getCachedInstance(this.getTitleId());
				String titleName = pTitle.getTitle();
				if (!StringUtils.stringIsBlank(titleName))
					abbrevatedName = abbrevatedName + titleName + " ";
			} catch (Exception e) {
				// not too important so don't stop calculating full name
				Log.error(e.getMessage());
			}
		}
		
		abbrevatedName += (StringUtils.stringIsBlank(this.getFirstName())?"":(this.getFirstName().substring(0,1)+" ")); 
		abbrevatedName += (StringUtils.stringIsBlank(this.getMiddleName())?"":(this.getMiddleName().substring(0,1)+" ")); 
		abbrevatedName += (StringUtils.stringIsBlank(this.getLastName())?"":this.getLastName());
		// should always have first and second name
		return abbrevatedName;
	}
	
	/** Returns the numeric suffix of the 12-character Person Account Code.
	 * 
	 * @return
	 * @throws DataException 
	 */
	public int getAccountCodeNumber() throws DataException {
		
		if (StringUtils.stringIsBlank(this.getAccountCode()))
			throw new DataException("Unable to extract numeric portion of Person " +
			 "Account Code: code is missing/empty");
		
		return Integer.parseInt(this.getAccountCode().substring(4));
	}
	
	public static void main (String[] args) throws DataException {
		Person person = new Person(new DataBinder());
		person.setFirstName("T");
		person.setLastName("M");
		person.setPersonId(1234);
		
		System.out.println(calculateAccountCode(person, null, null, null));
		
		System.out.println(calculateAccountCode
		 (null, 1234, new Company(1, "CBF", "XXX", "XXX", 6, 3), null ));
	}
	
	/** Compares alphabetically on the Person's name.
	 * 
	 *  If Last Name is set, this will be used to run the comparison
	 *  
	 *  If Last Name isn't set (can be the case on old imported records), the last string 
	 *  token of the Full Name is assumed to the Last Name instead.
	 */
	public int compareTo(Person p) {
		String myLastName = null;
		String theirLastName = null;
		
		if (!StringUtils.stringIsBlank(this.getLastName())) {
			myLastName = this.getLastName();
		} else {
			String[] splitFullName =  this.getFullName().split(" ");
			myLastName = splitFullName[splitFullName.length-1];
		}
		
		if (!StringUtils.stringIsBlank(p.getLastName())) {
			theirLastName = p.getLastName();
		} else {
			String[] splitFullName =  p.getFullName().split(" ");
			theirLastName = splitFullName[splitFullName.length-1];
		}
		
		return myLastName.compareTo(theirLastName);
	}

	public DataResultSet getAuroraCompaniesData(FWFacade cdbFacade) throws DataException {
		// Fetch Aurora company links associated with person
		return AuroraCorrespondent.getAuroraCompaniesDataByPersonId
		 (this.getPersonId(), cdbFacade);
	}
	
	public Vector<Company> getAuroraCompanies(FWFacade cdbFacade) throws DataException {
		// Fetch Aurora company links associated with person
		DataResultSet rsCompanies = getAuroraCompaniesData(cdbFacade);
		return Company.getAll(rsCompanies);
	}

	public String getId() {
		return Integer.toString(this.getPersonId());
	}
	
	public String getComparisonLabel() {
		return "Central DB Person/Aurora Correspondent";
	}
	
	/** Convenience method for creating a new Person record (plus associated contacts)
	 *  from Signatory data imported from Aurora.
	 *  
	 * @param sigName
	 * @param sigTel
	 * @param sigPostcode
	 * @param facade
	 * @param m_name
	 * @return
	 * @throws DataException 
	 */
	public static Person addFromSignatoryData(
		int titleId,
		String firstName,
		String middleNames,
		String lastName,
		String sigTel,
		String sigPostcode, 
		FWFacade facade, String userName) throws DataException {
		
		DataBinder binder = new DataBinder();
		
		PersonTitle title = PersonTitle.getCache().getCachedInstance(titleId);
		
		String sigFullName = title.getTitle() +
		 (StringUtils.stringIsBlank(firstName) ? "" : " " + firstName) +
		 (StringUtils.stringIsBlank(middleNames) ? "" : " " + middleNames) +
		 " " + lastName;
		
		String salutation = title.getTitle() + " " + lastName;
		
		CCLAUtils.addQueryIntParamToBinder(binder, Cols.TITLE_ID, titleId);
		
		if (firstName != null)
			binder.putLocal(Cols.FIRST_NAME, firstName);
		if (middleNames != null)
			binder.putLocal(Cols.MIDDLE_NAME, middleNames);
		
		binder.putLocal(Cols.LAST_NAME, lastName);
		
		binder.putLocal(Cols.FULL_NAME, sigFullName);
		binder.putLocal(Cols.SALUTATION, salutation);
	
		Log.debug("Adding new Person from sig data: " + sigFullName);
		
		CCLAUtils.addQueryIntParamToBinder
		 (binder, Element.Cols.SOURCE_ID, DataSource.Ids.AURORA_SIGNATORIES);
		
		Person p = add(binder, facade, userName);
		
		if (!StringUtils.stringIsBlank(sigTel)) {
			Log.debug("Adding contact telephone no: " + sigTel);
			// Add the telephone number.
			Contact.add(p.getPersonId(), null, Contact.SUBMETHOD_PHONE_PERSONAL, sigTel,
			 null, true, false, false, false, null, facade, userName);
		}
		
		if (!StringUtils.stringIsBlank(sigPostcode)) {
			Log.debug("Adding contact address with post-code: " + sigPostcode);
			// Add the address (postcode only!)
			DataBinder addrBinder = new DataBinder();
			CCLAUtils.addQueryParamToBinder(addrBinder, "POSTCODE", sigPostcode);
			Address addr = Address.add(addrBinder, facade, userName);
			
			Contact.add(p.getPersonId(), null, Contact.SUBMETHOD_ADDRESS_HOME, null,
			 addr.getAddressId(), true, true, true, false, null, facade, userName);
		}
		
		return p;
	}
}
