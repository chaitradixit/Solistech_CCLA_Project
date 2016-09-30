package com.ecs.stellent.ccla.clientservices;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.server.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ecs.stellent.ccla.clientservices.person.PersonUtils;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.Address;
import com.ecs.ucm.ccla.data.AuroraCorrespondent;
import com.ecs.ucm.ccla.data.AuthStatus;
import com.ecs.ucm.ccla.data.Contact;
import com.ecs.ucm.ccla.data.Element;
import com.ecs.ucm.ccla.data.ElementAttribute;
import com.ecs.ucm.ccla.data.ElementAttributeApplied;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.IdentityCheck;
import com.ecs.ucm.ccla.data.IdentityValidationCheck;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.PersonTitle;
import com.ecs.ucm.ccla.data.Relation;
import com.ecs.ucm.ccla.data.RelationName;
import com.ecs.ucm.ccla.data.RelationPropertyApplied;
import com.ecs.ucm.ccla.data.RelationType;
import com.ecs.ucm.ccla.data.SharedCols;
import com.ecs.ucm.ccla.data.ElementAttribute.SelectionType;
import com.ecs.ucm.ccla.experian.AuthenticationScoreUtils;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

public class PersonService extends Service {
	
	/** Adds a new Person.
	 * 
	 * @throws ServiceException 
	 * @throws DataException
	 **/
	public void addPerson() throws ServiceException, DataException {
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);

		try {
			facade.beginTransaction();
			
			// First, create the new Person record. This will return
			// a Person instance with the assigned Person/Element ID.
			
			// Sort out the date of birth in the DataBinder
			Date dob = PersonUtils.getDateOfBirth(m_binder);
			CCLAUtils.addQueryDateParamToBinder
			 (m_binder, "DATE_OF_BIRTH", dob);

			Person person 	= Person.add(m_binder, facade, m_userData.m_name);
			int personId	= person.getPersonId();
			
			// Now create an Address record for the new entity, providing
			// there is sufficient address data.
			if (Address.addressDataExists(m_binder)) {
				//String addressLabel = m_binder.getLocal("newAddressLabel");
				
				Address address = Address.add(m_binder, facade, m_userData.m_name);
				int addressId	= address.getAddressId();
			
				int submethodId = CCLAUtils.getBinderIntegerValue
				 (m_binder, Contact.Cols.SUBMETHOD);
				
				boolean qasValid = !StringUtils.stringIsBlank
				 (m_binder.getLocal("QAS_VALID"));
				
				// Add the 'Home address' contact mapping and mark it as
				// 'default' and designated address for Experian checks
				Contact.add(personId, null, submethodId,
				 null, addressId, true, false, qasValid, false, null, 
				 facade, m_userData.m_name);
			}
			
			// Add common contact details, if user specified values
			String mobile = m_binder.getLocal("MOBILE");
			boolean hasDefaultPhone = false;
			if (!StringUtils.stringIsBlank(mobile))
			{
				Contact.add(personId, null, Contact.SUBMETHOD_PHONE_MOBILE,
				 mobile, null, true, false, false, false, null, facade, 
				 m_userData.m_name);
				hasDefaultPhone = true;
			}			
			String phone = m_binder.getLocal("PHONE");
			if (!StringUtils.stringIsBlank(phone)) {
				String phoneExt = m_binder.getLocal("PHONE_EXT");
				
				// Look for phone extension number
				if (!StringUtils.stringIsBlank(phoneExt)) {
					phone += " ext. " + phoneExt;
				}
				
				Contact.add(personId, null, Contact.SUBMETHOD_PHONE_BUSINESS,
				 phone, null, !hasDefaultPhone, false, false, false, null,
				 facade, m_userData.m_name);
			}
				
			String email =  m_binder.getLocal("EMAIL");
			if (!StringUtils.stringIsBlank(email))
				Contact.add(personId, null, Contact.SUBMETHOD_EMAIL_PERSONAL, 
				 email, null, true, false, false, false, null, facade, 
				 m_userData.m_name);
			
			String website =  m_binder.getLocal("WEBSITE");
			if (!StringUtils.stringIsBlank(website))
				Contact.add(personId, null, Contact.SUBMETHOD_WEBSITE_BUSINESS,
				 website, null, true, false, false, false, null, facade, 
				 m_userData.m_name);
			
			// A relationship between an existing Entity can be created
			// immediately for the new Person. This is determined by the
			// presence of the related Entity ID in the binder as
			// relatedEntityId.
			Integer relEntityId = CCLAUtils.getBinderIntegerValue
			 (m_binder, "relatedEntityId");
			
			Integer newRelationId = null;
			RelationName relName = null;
			
			Integer relationNameId = CCLAUtils.getBinderIntegerValue
			 (m_binder, "RELATION_NAME_ID");
			
			if (relationNameId != null)
				relName = 
				 RelationName.getCache().getCachedInstance(relationNameId);
			
			if (relEntityId != null) {
				Relation elemRelation = 
				 Relation.add(relEntityId, personId, 
				 relName,
				 facade, m_userData.m_name);
				
				newRelationId = elemRelation.getRelationId();
			}
			
			// A relationship between an existing Account can be created
			// immediately for the new Person. This is determined by the
			// presence of the related Account ID in the binder as
			// relatedAccountId.
			//
			// If new Account AND new Entity relations are both created here,
			// the 'newRelationId' added to the RedirectUrl will correspond
			// to the Person-Account relation.
			//
			// TODO: currently only supports one RELATION_NAME_ID parameter
			// in the binder for both generated relations.
			Integer relAccountId = CCLAUtils.getBinderIntegerValue
			 (m_binder, "relatedAccountId");
		
			if (relAccountId != null) {
				Relation elemRelation = 
				 Relation.add(personId, relAccountId,  
				 relName, facade, m_userData.m_name);
				
				newRelationId = elemRelation.getRelationId();
			}
			
			// If a Company value is present in the binder, attempt to create an
			// Aurora Correspondent as well.
			/*
			Integer companyId 	= CCLAUtils.getBinderIntegerValue
			 (m_binder, AuroraCorrespondent.Cols.COMPANY);
			
			if (companyId != null) {
				CCLAUtils.addQueryIntParamToBinder(m_binder, Person.Cols.ID, personId);
				
				AuroraCorrespondent.add(m_binder, facade, userName);
			}
			*/
			
			facade.commitTransaction();
			
			Log.debug("Successfully created new Person record " + personId);
			
			// Append new entity ID (and new relation ID, if applicable) to RedirectUrl
			String redirectUrl = m_binder.getLocal("RedirectUrl");
			
			if (!StringUtils.stringIsBlank(redirectUrl)) {
				redirectUrl += personId;
				
				if (newRelationId != null)
					redirectUrl += "&newRelationId=" + newRelationId;
				
				m_binder.putLocal("RedirectUrl", redirectUrl);
			}
				
		} catch (Exception e){
			String msg = "Unable to create new Person: " + e.getMessage();
			Log.error(msg, e);

			facade.rollbackTransaction();			
			throw new ServiceException(msg, e);
		}
	}
	
	public void getPersonExtended() throws DataException, ServiceException {
		String personIdStr = m_binder.getLocal("PERSON_ID");
		
		if (StringUtils.stringIsBlank(personIdStr)) {
			throw new ServiceException("Unable to load person record: " +
			 "no PERSON_ID found.");
		}
		
		Integer personId = Integer.parseInt(personIdStr);
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		addPersonDataToBinder(personId, facade, m_binder);
		
		// Account listings
		DataResultSet rsAccountTotals = Account.getPersonAccountTotalsData
		 (personId, facade);
		
		m_binder.addResultSet("rsAccountTotals", rsAccountTotals);
		
		DataResultSet rsAccounts = Relation.getRelatedAccountsData
		 (personId, ElementType.PERSON, facade);
		m_binder.addResultSet("rsAccounts", rsAccounts);
		
		// Campaigns
		DataResultSet rsProcesses = getProcessData(facade);
		m_binder.addResultSet("rsProcesses", rsProcesses);
	}	
	
	
	public void getPerson() throws DataException, ServiceException {
		String personIdStr = m_binder.getLocal("PERSON_ID");
		
		if (StringUtils.stringIsBlank(personIdStr)) {
			throw new ServiceException("Unable to load person record: " +
			 "no PERSON_ID found.");
		}
		
		int personId = Integer.parseInt(personIdStr);
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		addPersonDataToBinder(personId, facade, m_binder);
	}
	
	public void getPersonBasic() throws DataException, ServiceException {
		String personIdStr = m_binder.getLocal("PERSON_ID");
		
		if (StringUtils.stringIsBlank(personIdStr)) {
			throw new ServiceException("Unable to load Entity record: " +
			 "no PERSON_ID found.");
		}
		
		int personId = Integer.parseInt(personIdStr);
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		addBasicPersonDataToBinder(personId, facade, m_binder);
	}

	/** Updates data for existing Person.
	 * 
	 * @throws DataException 
	 *  
	 * @throws DataException
	 * @throws ServiceException 
	 */	
	
	public void updatePerson() throws DataException, ServiceException {
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		Integer personId = CCLAUtils.getBinderIntegerValue(m_binder, SharedCols.PERSON);
		
		if (personId == null)
			throw new ServiceException("Unable to update Person: " + SharedCols.PERSON +
			 " missing");
		
		try {
			facade.beginTransaction();
			
			boolean hasChangedAuthenticateData = false;
			
			// sort out the date of birth in the DataBinder
			Date dob = PersonUtils.getDateOfBirth(m_binder);
			CCLAUtils.addQueryDateParamToBinder
			 (m_binder, "DATE_OF_BIRTH", dob);
			
			// update details in person table
			Person person = Person.get(personId, false, facade);
			hasChangedAuthenticateData = authenticateDataChanged(m_binder,person);
			
			person.setAttributes(m_binder);
			person.persist(facade, m_userData.m_name);

			// update address if it has changed
			/*
			String addressChanged = m_binder.getLocal("addressChanged");
			String oldAddressID = null;
			
			if(!StringUtils.stringIsBlank(addressChanged) 
				&& 
				addressChanged.equalsIgnoreCase("true")) {
				Log.debug("checking addressID");
				
				if(!StringUtils.stringIsBlank(oldAddressID)) {
					// Existing address passed - update it.
					
					int addressId = Integer.parseInt(oldAddressID);
					Log.debug("Existing address on file with ID: " + 
					 addressId + ". Performing update.");
					
					Address address = Address.get(addressId, facade);
					
					if (address == null) {
						throw new ServiceException("Unable to update address ID: " + 
						 addressId + ". Address not found.");
					}
					
					address.setAttributes(m_binder);
					address.persist(facade, null);
					// if this is experian address then mark as changed authentication data
					Contact addressContact = Contact.get(addressId, facade);
					if (addressContact.isExperian())
						hasChangedAuthenticateData = true;

				} else {
					// No existing address - add a new one for this person.
					Log.debug("No address ID in binder. Adding new address.");
					
					Address address = Address.add(m_binder, facade, username);
					Contact contact = Contact.add(personId, null,
					 Contact.SUBMETHOD_ADDRESS_HOME,
					 null, address.getAddressId(),true, false, true, false, null, 
					 facade, username);
					
					Log.debug("Created new Address with ID " + address.getAddressId() + 
					 " and linked to Person");
					AuthenticationScoreUtils.updateIdentityCheck(personId,facade);
				}
			}
			*/
			
			//updateValidationFields(facade,personId,m_binder);
			
			/*
			// Check for an Aurora Correspondent Number and Company. This is
			// used to create a new Person Aurora mapping.
			String corrIdStr	= m_binder.getLocal("CORR_ID");
			String companyIdStr	= m_binder.getLocal("COMPANY_ID");
			
			Integer corrId = null;
			Company company = null;
			
			// First see if only one is specified. This is an incomplete
			// mapping and an error must be thrown.
			if ((!StringUtils.stringIsBlank(corrIdStr)
				&&
				StringUtils.stringIsBlank(companyIdStr))
				||
				(StringUtils.stringIsBlank(corrIdStr)
				&&
				!StringUtils.stringIsBlank(companyIdStr))) {
				
				throw new DataException("Unable to link Aurora Correspondent: " +
				 "Aurora Correspondent Number/Company was missing. You must " +
				 "specify both or neither.");
				
			} else if (	!StringUtils.stringIsBlank(corrIdStr)
						&&
						!StringUtils.stringIsBlank(companyIdStr)) {
				// Sufficient data to create an Aurora Correspondent mapping.
				corrId 			= Integer.parseInt(corrIdStr);
				int companyId	= Integer.parseInt(companyIdStr);
				
				company	= Company.getCache().getCachedInstance(companyId);
			}
			if (corrId != null && company != null) {
				// Create a new Aurora Correspondent mapping.
				AuroraCorrespondent.add
				 (m_binder, facade, username);
				
				Log.debug("Linked to Aurora Correspondent ID: " + corrId);
			}
			*/
			
			// Update Element fields: 
			// do_not_contact_flag, preferred_contact_method
			Element thisElement = Element.get(personId, facade);
			
			thisElement.setDoNotContact(CCLAUtils.getBinderBoolValue
			 (m_binder, Element.Cols.DO_NOT_CONTACT_FLAG));
			thisElement.setPreferredContactMethod(CCLAUtils.getBinderIntegerValue
			 (m_binder, Element.Cols.PREFERRED_CONTACTMETHOD));
			
			thisElement.persist(facade, m_userData.m_name);
			
			if (hasChangedAuthenticateData) {
				IdentityCheck idCheck = IdentityCheck.get
				 (person.getPersonId(), facade);
				
				if (idCheck != null) {
					// Apply the 'Core Data Changed' flag to the Person's ID check
					AuthenticationScoreUtils.setCoreDataChanged
					 (idCheck, facade, m_userData.m_name);
				}
			}
			
			facade.commitTransaction();
		
		} catch (Exception e) {
			facade.rollbackTransaction();
			String msg = "Failed to update Person record: " + e.getMessage();
			
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		} 
	}
	
	/** Adds the full suite of Person data ResultSets to the given
	 *  DataBinder.
	 *   
	 * @param personId
	 * @param facade
	 * @param binder
	 * @throws ServiceException
	 * @throws DataException
	 */
	public static void addPersonDataToBinder
	 (int personId, FWFacade facade, DataBinder binder) 
	 throws ServiceException, DataException {
		
		addBasicPersonDataToBinder(personId, facade, binder);
		
		// Fetch Auth Statuses
		DataResultSet rsAuthStatuses =
		 AuthStatus.getAllData(facade);
		
		binder.addResultSet("rsAuthStatuses", rsAuthStatuses);
		
		// Fetch associated contact details (includes address data)
		DataResultSet rsContacts = 
		 Contact.getElementContactsData(personId, facade);
	
		binder.addResultSet("rsContactDetails", rsContacts);
		
		// Add entities related to this person
		DataResultSet rsRelatedEntities = 
		 Relation.getRelatedOrgsData(null, personId, facade);
		
		binder.addResultSet("rsRelatedEntities", rsRelatedEntities);
		
		RelationType relType = 
		 RelationType.getCache().getCachedInstance(RelationType.ORG_PERSON);
		
		DataResultSet rsEntityRelations = 
		 Relation.getRelationData(null, personId, 
		 relType, null, facade);
		
		binder.addResultSet("rsEntityRelations", rsEntityRelations);
		
		// Add Relation Map (assists in UI rendering)
		DataResultSet rsEntityRelationMap = 
		 Relation.getRelationMap(ElementType.ORGANISATION, 
		 null, personId, relType, null, facade);
		
		binder.addResultSet("rsEntityRelationMap", rsEntityRelationMap);
		
		// Add applied properties
		DataResultSet rsEntityRelationProperties = 
		 RelationPropertyApplied.getByRelationsData(null, 
		 personId, relType, null, facade);
		
		binder.addResultSet
		 ("rsEntityRelationProperties", rsEntityRelationProperties);	
		
		// Add any mapped Aurora Correspondent IDs
		DataResultSet rsPersonAuroraMap =
		 AuroraCorrespondent.getCorrespondentsDataByPersonId
		 (personId, facade);
		
		binder.addResultSet("rsPersonAuroraMap", rsPersonAuroraMap);
	}
	
	/** Service method for loading in Person Aurora Mapping data only.
	 *  
	 *  TODO replace with more generic getPersonDetails service method.
	 * 
	 * @throws DataException 
	 * 
	 */
	public void getPersonAuroraMaps() throws DataException {
		
		Integer personId = CCLAUtils.getBinderIntegerValue(m_binder, SharedCols.PERSON);
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		// Add any mapped Aurora Correspondent IDs
		DataResultSet rsPersonAuroraMap =
		 AuroraCorrespondent.getCorrespondentsDataByPersonId
		 (personId, facade);
		
		m_binder.addResultSet("rsPersonAuroraMap", rsPersonAuroraMap);
	}

	/** Adds basic Person data and Identity Checking details 
	 *  ResultSets to the given DataBinder.
	 *   
	 * @param personId
	 * @param facade
	 * @param binder
	 * @throws ServiceException
	 * @throws DataException
	 */
	public static void addBasicPersonDataToBinder
	 (int personId, FWFacade facade, DataBinder binder) 
	 throws ServiceException, DataException {
		
		// Fetch core Entity record first
		DataResultSet rsPerson = Person.getData(personId, facade);
		
		if (rsPerson.isEmpty())
			throw new ServiceException("Person with ID " + personId +
			 " not found.");
		
		binder.addResultSet("rsPerson", rsPerson);
		String title_id = rsPerson.getStringValueByName("TITLE_ID");
		if (!StringUtils.stringIsBlank(title_id))
		{
		try {
			int titleInt = Integer.parseInt(title_id);
			PersonTitle pTitle = PersonTitle.getCache().getCachedInstance(titleInt);
			String titleName = pTitle.getTitle();
			binder.putLocal("TITLE", titleName);
		} catch (NumberFormatException e) {
			Log.error("UNABLE TO PARSE TITLE STRING FOR PERSON:" + personId);
		}
		}
		Date dob = rsPerson.getDateValueByName("DATE_OF_BIRTH");

		if (dob != null)
			PersonUtils.splitDateOfBirth(dob, binder);
	
		DataResultSet rsValidationRecord = 
		 IdentityValidationCheck.getData(personId, facade);
		
		binder.addResultSet("rsValidationRecord", rsValidationRecord);
		
		// Refresh Identity Check score, if applicable
		AuthenticationScoreUtils.getIdentityCheckResult(personId, facade);
		
		// Add Identity Check record to binder (may be empty ResultSet
		// if no check has been logged yet)
		DataResultSet rsIdentityCheck = 
		 IdentityCheck.getData(personId, facade);
		
		binder.addResultSet("rsIdentityCheck", rsIdentityCheck);
		
		// Add Person attribute names
		DataResultSet rsElementAttributes = ElementAttribute.getElementAttributesData
		 (ElementType.PERSON, SelectionType.ALL, facade);
		
		binder.addResultSet("rsElementAttributes", rsElementAttributes);
		
		// Add applied Person attribute data
		DataResultSet rsElementAttributesApplied = 
		 ElementAttributeApplied.getAllData
		  (personId, null, false, facade);
		
		binder.addResultSet("rsElementAttributesApplied", rsElementAttributesApplied);
	}
	
	/** Gets whether a validation record exists for the person,
	 * true or false and puts resultset into binder
	 * as results set rsValidationRecord
	 * 
	 * @deprecated use IdentityValidationCheck.getData() instead. 
	 *  
	 * @throws DataException
	 */	
	public static DataResultSet getValidationRecord
	 (FWFacade fw, int personId, DataBinder binder) throws DataException {
		Log.debug("IN GETVALIDATIONRECORD");

		binder.putLocal("PERSON_ID",Integer.toString(personId));
		DataResultSet rs = 
		 fw.createResultSet("qClientServices_GetValidationRecord", binder);
		
		if (!rs.isEmpty()) {
			Log.debug("found validation record for:" + personId);
			binder.addResultSet("rsValidationRecord", rs);
		}
		
		return rs;	
	}
	
	/** looks for values for each validation check and then updates and/or adds the record
	 * for them in the table CCLA_IDENTITY_VALIDATION_CHECK
	 * 
	 *  
	 *  
	 * @throws DataException
	 */			
	public static void updateValidationFields(FWFacade fw, int personId, DataBinder binder)
	throws DataException
	{
		String DrivingLicence = binder.getLocal("DRIVING_LICENCE");
		String DrivingLicenceName = binder.getLocal("DRIVING_LICENCE_NAME");
		String PassportNumber = binder.getLocal("PASSPORT_NUMBER");
		String UtilityMailSort = binder.getLocal("UTILITY_MAILSORT");
		String UtilityMailSortDate = binder.getLocal("UTILITY_MAILSORT_DATE");
		String DrivingLicenceValid = binder.getLocal("DRIVING_LICENCE_CHECK");
		String PassportValid = binder.getLocal("PASSPORT_CHECK");
		String UtilityMailsortValid = binder.getLocal("MAILSORT_CHECK");
		String saveValidation = binder.getLocal("SAVE_VALIDATION");
		
		binder.putLocal(SharedCols.PERSON, Integer.toString(personId));
		
		DataResultSet rs = getValidationRecord(fw, personId, binder);
		
		if (saveValidation!=null && saveValidation.equalsIgnoreCase("Yes"))
		{
			if (rs.isEmpty())
			{
				
				Log.debug("CALLING qClientServices_InsertValidationRecord");
				fw.execute("qClientServices_InsertValidationRecord", binder);
			}
			if (DrivingLicence!=null)
			{
				if (PersonUtils.compareBinderWithResultSet(binder, rs, "DRIVING_LICENCE") 
					&& 
					PersonUtils.compareBinderWithResultSet(binder, rs, "DRIVING_LICENCE_NAME"))
				{
					// driving licence details match so don't have to do update
					Log.debug("Licence number matches, don't update");
				} else {
					// Need to run query to do update plus remove validated flag
				if (DrivingLicenceName==null)
					binder.putLocal("DRIVING_LICENCE_NAME", "");
				fw.execute("qClientServices_UpdateDrivingLicence", binder);
				}
			}
			if (PassportNumber!=null)
			{
				if (PersonUtils.compareBinderWithResultSet(binder, rs, "PASSPORT_NUMBER"))
				{
					// driving licence details match so don't have to do update
					Log.debug("passport details match, don't update");
				} else {
				fw.execute("qClientServices_UpdatePassportNumber", binder);
				}

			}			
			if (UtilityMailSort!=null)
			{
				if (PersonUtils.compareBinderWithResultSet(binder, rs, "UTILITY_MAILSORT_DATE") && PersonUtils.compareBinderWithResultSet(binder, rs, "UTILITY_MAILSORT"))
				{
					// details match so don't have to do update
					Log.debug("utility details match, don't update");
				} else {
				if (UtilityMailSortDate==null || UtilityMailSortDate.length()==0)
				{	
				Log.error("cannot get Utility Sort Date");
				CCLAUtils.addQueryDateParamToBinder(binder, "UTILITY_MAILSORT_DATE", null);
				} else {
				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");				 
		          try {
					Date dateMailSort = df.parse(UtilityMailSortDate);
					Log.debug("dateMailSort is " + dateMailSort.toString());
					binder.putLocalDate("UTILITY_MAILSORT_DATE", dateMailSort);
				} catch (ParseException e) {
					Log.error("Cannot parse date:" + UtilityMailSortDate, e);
					CCLAUtils.addQueryDateParamToBinder(binder, "UTILITY_MAILSORT_DATE", null);
					Log.debug("running query qClientServices_UpdateUtilityMailsort");
				}
				}
				}
			}
			fw.execute("qClientServices_UpdateUtilityMailsort", binder);
		}
		
	}
	
	/** Returns ResultSet of all processes associated with this PERSON. */
	public DataResultSet getProcessData(FWFacade facade) throws DataException,ServiceException {
		String personIdStr = m_binder.getLocal("PERSON_ID");
		
		if (StringUtils.stringIsBlank(personIdStr)) {
			throw new ServiceException("Unable to load person record: " +
			 "no PERSON_ID found.");
		}
		
		DataResultSet rsProcesses =
		 facade.createResultSet("qClientServices_GetPersonProcesses", m_binder);
		
		return rsProcesses;
	}	
	
	/** Adds a new entry to PERSON_AURORA_MAP.
	 * 
	 * @throws ServiceException
	 * @throws DataException
	 */
	public void addPersonAuroraMap() throws ServiceException, DataException  {
		
		Integer personId 	= CCLAUtils.getBinderIntegerValue
		 (m_binder, AuroraCorrespondent.Cols.PERSON);
		Integer corrId 		= CCLAUtils.getBinderIntegerValue
		 (m_binder, AuroraCorrespondent.Cols.CORR);
		Integer companyId 	= CCLAUtils.getBinderIntegerValue
		 (m_binder, AuroraCorrespondent.Cols.COMPANY);
		
		if (personId == null
			||
			companyId == null)
			throw new ServiceException
			 ("Failed to add Aurora Correspondent mapping: " +
			  "missing PERSON_ID, CORR_ID OR COMPANY_ID");
			
		FWFacade fw = CCLAUtils.getFacade(m_workspace, true);
		
		try {
			fw.beginTransaction();
			
			AuroraCorrespondent.add
			 (m_binder, fw, m_userData.m_name);
			
			fw.commitTransaction();
			
		} catch (Exception e) {
			fw.rollbackTransaction();
			
			String msg = "Failed to add Aurora Correspondent mapping: " + 
			 e.getMessage();
			
			Log.error(msg);
			throw new ServiceException(msg);
		}
	}
	
	/** Updates an existing entry in PERSON_AURORA_MAP.
	 * 
	 * @throws ServiceException
	 * @throws DataException
	 */
	public void updatePersonAuroraMap() throws ServiceException, DataException  {
		
		Integer mapId = CCLAUtils.getBinderIntegerValue
		 (m_binder, AuroraCorrespondent.Cols.ID);
		
		if (mapId == null)
			throw new ServiceException
			 ("Failed to update Aurora Correspondent mapping: " +
			  "missing PERSON_AURORA_MAP_ID");
			
		FWFacade fw = CCLAUtils.getFacade(m_workspace, true);
		
		try {
			fw.beginTransaction();
			
			AuroraCorrespondent corr = AuroraCorrespondent.get(mapId, fw);
			corr.setAttributes(m_binder);
			
			corr.persist(fw, m_userData.m_name);
			
			fw.commitTransaction();
			
		} catch (Exception e) {
			fw.rollbackTransaction();
			
			String msg = "Failed to update Aurora Correspondent mapping: " + 
			 e.getMessage();
			
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}
	
	public void removePersonAuroraMap() throws ServiceException, DataException {
		
		Integer mapId = CCLAUtils.getBinderIntegerValue
		 (m_binder, AuroraCorrespondent.Cols.ID);
		
		if (mapId == null) {
			throw new ServiceException
			 ("Failed to remove Aurora Person mapping: " +
			  "missing " + AuroraCorrespondent.Cols.ID);
		} 

		FWFacade fw 	= CCLAUtils.getFacade(m_workspace, true);
		
		try {
			fw.beginTransaction();
			
			AuroraCorrespondent.remove(mapId, fw, m_userData.m_name);
			
			fw.commitTransaction();
		} catch (DataException e) {
			fw.rollbackTransaction();
			
			String msg = "Failed to remove Aurora Correspondent mapping: " +
			 e.getMessage();
			
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}
	
	/**
	 *  Determines whether specific Person fields that are used for Experian checks 
	 *  have been updated.
	 *  
	 *  Currently only 2 fields are checked for changes: LAST_NAME and DATE_OF_BIRTH.
	 *  
	 * @param binder
	 * @param person
	 * @return
	 * @throws DataException
	 */
	public boolean authenticateDataChanged(DataBinder binder, Person person) 
	throws DataException
	{
		Log.debug("Checking for changes in Person authentication fields");
		
		String newLastName = binder.getLocal("LAST_NAME");
		
		if (!person.getLastName().equalsIgnoreCase(newLastName)) {
			Log.debug("Last Name was updated");
			return true;
		}
		
		Date newDOB = CCLAUtils.getBinderDateValue(binder, "DATE_OF_BIRTH");
		
		if ((person.getDateOfBirth() != null && newDOB != null) 
			&& !person.getDateOfBirth().equals(newDOB)) {
			Log.debug("DOB was updated");
			return true;
		}
		
		Log.debug("No changes detected");
		return false;
	}
	
	/**
	 * @UCM_Service CCLA_CS_PERSON_INFO
	 * @throws ServiceException
	 * @throws DataException
	 */
	public void getPersonInfo() throws ServiceException, DataException{
		//5:qClientServices_GetElementRelationNames:rsElementRelationTypeNames::null
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		m_binder.addResultSet(
				"rsElementRelationTypeNames", 
				facade.createResultSet("qClientServices_GetElementRelationNames", m_binder)
		);
	}
	
	/**
	 * @UCM_Service CCLA_CS_GET_PERSON_INTERACTIONS
	 * @throws ServiceException
	 * @throws DataException
	 */
	public void getPersonInteractions() throws ServiceException, DataException{
		//5:qClientServices_GetPersonInteractions:rsInteractions::null
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		m_binder.addResultSet(
				"rsInteractions", 
				facade.createResultSet("qClientServices_GetPersonInteractions", m_binder)
		);
	}
}
