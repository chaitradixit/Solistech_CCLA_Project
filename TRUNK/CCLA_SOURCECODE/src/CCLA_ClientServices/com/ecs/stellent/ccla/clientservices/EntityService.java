package com.ecs.stellent.ccla.clientservices;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.server.Service;

import java.util.Iterator;
import java.util.Vector;

import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.AccountFlag;
import com.ecs.ucm.ccla.data.Activity;
import com.ecs.ucm.ccla.data.Address;
import com.ecs.ucm.ccla.data.AuroraClient;
import com.ecs.ucm.ccla.data.AuthStatus;
import com.ecs.ucm.ccla.data.BankAccount;
import com.ecs.ucm.ccla.data.Company;
import com.ecs.ucm.ccla.data.Contact;
import com.ecs.ucm.ccla.data.ElementAttribute;
import com.ecs.ucm.ccla.data.ElementAttributeApplied;
import com.ecs.ucm.ccla.data.ElementAttributeType;
import com.ecs.ucm.ccla.data.ElementIdentifier;
import com.ecs.ucm.ccla.data.ElementIdentifierApplied;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Fund;
import com.ecs.ucm.ccla.data.OrganisationCategory;
import com.ecs.ucm.ccla.data.Process;
import com.ecs.ucm.ccla.data.Relation;
import com.ecs.ucm.ccla.data.RelationName;
import com.ecs.ucm.ccla.data.RelationPropertyApplied;
import com.ecs.ucm.ccla.data.RelationType;
import com.ecs.ucm.ccla.data.ElementAttribute.SelectionType;
import com.ecs.ucm.ccla.data.Entity.ClientNumber;
import com.ecs.ucm.ccla.data.campaign.CampaignEnrolment;
import com.ecs.ucm.ccla.data.instruction.Instruction;
import com.ecs.ucm.ccla.data.instruction.InstructionType;
import com.ecs.ucm.ccla.data.staticdataupdate.StaticDataUpdateApplied;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Service methods for interacting with Entity objects, i.e. rows from
 *  the ORGANISATION table.
 *  
 * @author Tom
 *
 */
public class EntityService extends Service {
	
	/** Used to determine the Organisation data that is fetched.
	 * 
	 * @author Tom
	 *
	 */
	public static class OrganisationDataConfig {
		private boolean org;
		private boolean contacts;
		private boolean identifiers;
		private boolean relatedPersons;
		private boolean relatedAccounts;
		private boolean relatedOrgs;
		private boolean enrolments;
		private boolean activities;
		private boolean attribs;
		private boolean instructions;
		private boolean kyc;
		private boolean marketingAccountGroups;

		public OrganisationDataConfig(boolean org, boolean contacts,
				boolean identifiers, 
				boolean relatedPersons,
				boolean relatedAccounts, 
				boolean relatedOrgs,
				boolean enrolments, boolean activities,
				boolean attribs, boolean instructions, boolean kyc,
				boolean marketingAccountGroups) {
			this.org = org;
			this.contacts = contacts;
			this.identifiers = identifiers;
			this.relatedPersons = relatedPersons;
			this.relatedAccounts = relatedAccounts;
			this.relatedOrgs = relatedOrgs;
			this.enrolments = enrolments;
			this.activities = activities;
			this.attribs = attribs;
			this.instructions = instructions;
			this.kyc = kyc;
			this.marketingAccountGroups = marketingAccountGroups;
		}
		
		/** Create a config instance with all data marked for fetching. */
		public OrganisationDataConfig() {
			this(true, true, true, true, true, true, true, true, true, true, true, true);
		}
		
		/** Create a config instance from a comma-separate list of fetch data elements.
		 * 
		 * @param fetchData
		 */
		public OrganisationDataConfig(String fetchData) {
			String[] fetchElements = fetchData.split(",");
			
			for (String fetchElement : fetchElements) {
				if (fetchElement.equals("org")) {
					this.org = true;
				} else if (fetchElement.equals("contacts")) {
					this.contacts = true;
				} else if (fetchElement.equals("identifiers")) {
					this.identifiers = true;
				} else if (fetchElement.equals("relatedPersons")) {
					this.relatedPersons = true;
				} else if (fetchElement.equals("relatedAccounts")) {
					this.relatedAccounts = true;
				} else if (fetchElement.equals("relatedOrgs")) {
					this.relatedOrgs = true;
				} else if (fetchElement.equals("enrolments")) {
					this.enrolments = true;
				} else if (fetchElements.equals("activities")) {
					this.activities = true;
				} else if (fetchElement.equals("attribs")) {
					this.attribs = true;
				} else if (fetchElement.equals("instructions")) {
					this.instructions = true;
				} else if (fetchElement.equals("kyc")) {
					this.kyc = true;
				} else if (fetchElement.equals("marketingAccountGroups")) {
					this.marketingAccountGroups = true;
				}
			}
		}

		public boolean isOrg() {
			return org;
		}

		public boolean isIdentifiers() {
			return identifiers;
		}
	
		public boolean isRelatedPersons() {
			return relatedPersons;
		}

		public boolean isRelatedAccounts() {
			return relatedAccounts;
		}
		
		public boolean isEnrolments() {
			return enrolments;
		}

		public boolean isActivities() {
			return activities;
		}
		
		public boolean isAttribs() {
			return attribs;
		}

		public boolean isInstructions() {
			return instructions;
		}

		public boolean isKyc() {
			return kyc;
		}

		public boolean isContacts() {
			return contacts;
		}
		
		public boolean isMarketingAccountGroups() {
			return marketingAccountGroups;
		}

		public boolean isRelatedOrgs() {
			return relatedOrgs;
		}
	}
	
	/** Adds a custom selection of Organisation data to the binder, as specified by the 
	 *  passed dataConfig instance, when the Organisation ID is already known.
	 *  
	 * @param binder
	 * @param facade
	 * @param dataConfig
	 * @throws DataException
	 * @throws ServiceException
	 */
	public static void addEntityDataToBinder(DataBinder binder, int entityId, 
	 FWFacade facade, OrganisationDataConfig dataConfig, boolean debug) 
	 throws DataException, ServiceException {
		
		if (dataConfig.isOrg()) {	
			// Fetch core Entity record
			DataResultSet rsEntity 	= Entity.getExtendedData(entityId, facade);
			
			if (rsEntity.isEmpty())
				throw new ServiceException("Entity with ID " + entityId +
				 " not found.");
			
			binder.addResultSet("rsEntity", rsEntity);
			Entity entity = Entity.get(rsEntity);
		
			// Add Aurora client maps, if applicable.
			DataResultSet rsAuroraClientMap = 
			 Entity.getAuroraClientMappingData(entity.getOrganisationId(), facade);
			if (!rsAuroraClientMap.isEmpty())
				binder.addResultSet("rsAuroraClientMap", rsAuroraClientMap);
			
			// Add Account Companies
			DataResultSet rsAccountCompanies = 
			 Entity.getOrganisationCompaniesData(entity.getOrganisationId(), facade);
			
			binder.addResultSet("rsOrganisationCompanies", rsAccountCompanies);
			
			// Add selected category hierarchy
			if (entity.getCategoryId()!=null) {
				binder.addResultSet("rsSelectedCategories", 
						OrganisationCategory.getSelectedCategories
						(entity.getCategoryId(), facade));
			}
		}
		
		if (dataConfig.isContacts()) {
			// Fetch Auth Statuses
			DataResultSet rsAuthStatuses =
			 AuthStatus.getAllData(facade);
			
			binder.addResultSet("rsAuthStatuses", rsAuthStatuses);
			
			// Fetch associated contact details (includes address data)
			DataResultSet rsContacts = 
			 Contact.getElementContactsData(entityId, facade);
		
			binder.addResultSet("rsContactDetails", rsContacts);	
		}
		
		if (dataConfig.isRelatedAccounts()) {
			
			// Add totals by account type
			DataResultSet rsAccountTotals = Account.getClientAccountTotalsData
			 (entityId, facade);
			
			binder.addResultSet("rsAccountTotals", rsAccountTotals);
			
			// All org accounts
			DataResultSet rsAccounts = 
			 Account.getClientAccountsData(entityId, facade);
			
			binder.addResultSet("rsAccounts", Account.formatResultSet(rsAccounts));
		}
		
		if (dataConfig.isRelatedPersons()) {
			// Add persons related to this entity
			DataResultSet rsRelatedPersons = Relation.getRelatedPersonsData
			 (entityId, ElementType.ORGANISATION, facade);
			
			binder.addResultSet("rsRelatedPersons", rsRelatedPersons);
			
			// Add person relations
			RelationType relType = 
			 RelationType.getCache().getCachedInstance(RelationType.ORG_PERSON);
			
			DataResultSet rsPersonRelations =
			 Relation.getRelationData(entityId, null, 
			 relType, null, facade);
			
			binder.addResultSet("rsPersonRelations", rsPersonRelations);
			
			// Add Relation Map (assists in UI rendering)
			DataResultSet rsPersonRelationMap = 
			 Relation.getRelationMap(ElementType.PERSON, entityId, null, 
			 relType, null, facade);
			
			binder.addResultSet("rsPersonRelationMap", rsPersonRelationMap);
			
			// Add applied properties
			DataResultSet rsPersonRelationProperties = 
			 RelationPropertyApplied.getByRelationsData(entityId, 
			 null, relType, null, facade);
			
			binder.addResultSet
			 ("rsPersonRelationProperties", rsPersonRelationProperties);	
		}
		
		if (dataConfig.isRelatedOrgs()) {
			// Add orgs related to this entity
			DataResultSet rsRelatedOrgs = Relation.getRelatedOrgsData
			 (null, entityId, facade);
			
			binder.addResultSet("rsRelatedEntities", rsRelatedOrgs);
			
			// Add person relations
			RelationType relType = 
			 RelationType.getCache().getCachedInstance(RelationType.ORG_ORG);
			
			DataResultSet rsOrgRelations =
			 Relation.getRelationData(null, entityId, 
			 relType, null, facade);
			
			binder.addResultSet("rsEntityRelations", rsOrgRelations);
			
			// Add Relation Map (assists in UI rendering)
			DataResultSet rsOrgRelationMap = 
			 Relation.getRelationMap(ElementType.ORGANISATION, null, entityId, 
			 relType, null, facade);
			
			binder.addResultSet("rsEntityRelationMap", rsOrgRelationMap);
			
			// Add applied properties
			DataResultSet rsOrgRelationProperties = 
			 RelationPropertyApplied.getByRelationsData(null, 
			 entityId, relType, null, facade);
			
			binder.addResultSet
			 ("rsEntityRelationProperties", rsOrgRelationProperties);	
		}
		
		if (dataConfig.isEnrolments()) {
			addEntityEnrolmentDataToBinder(entityId, binder, facade);
		}
		
		if (dataConfig.isActivities()) {
			DataResultSet rsActivities = 
			 Activity.getOrganisationActivitiesData(entityId, 15, facade);
			 binder.addResultSet("rsActivities", rsActivities);
		}
		
		if (dataConfig.isInstructions()) {
			DataResultSet rsInstructions =
			 Instruction.getExtendedDataByOrg
			 (entityId, Globals.INSTRUCTIONS_MAX_RESULTS, facade);
			binder.addResultSet("rsInstructions", rsInstructions);
		}
		
		if (dataConfig.isAttribs()) {
			// Add all available element attributes
			binder.addResultSet("rsElementAttributes", 
			 ElementAttribute.getElementAttributesData(ElementType.ORGANISATION, 
			 ElementAttribute.SelectionType.ALL, facade));
			
			// Add all applied attributes for this Org
			binder.addResultSet("rsElementAttributesApplied", 
			 ElementAttributeApplied.getAllData
			 (entityId, null, false, facade));
		}
		
		if (dataConfig.isIdentifiers()) {
			// Add all available element identifier names
			DataResultSet rsElementIdentifiers = 
			 ElementIdentifier.getIdentifiersByElementTypeData
			  (ElementType.ORGANISATION, facade);

			binder.addResultSet("rsElementIdentifiers", rsElementIdentifiers);
			
			// Add applied element identifiers
			DataResultSet rsElementIdentifiersApplied = 
			 ElementIdentifierApplied.getElementIdentifiersAppliedData(entityId, facade);
			
			binder.addResultSet("rsElementIdentifiersApplied", 
			 rsElementIdentifiersApplied);	
		}
		
		if (dataConfig.isKyc()) {
			Entity.EntityCheck entityCheck = Entity.getEntityCheck(entityId, facade);
			entityCheck.addToBinder(binder);
		}
		
		if (dataConfig.isMarketingAccountGroups()) {
			// Add Marketing Group/Sub-Group/Account Group reference data
			binder.addResultSet("rsMarketingGroups", 
			 facade.createResultSet("qClientServices_GetMarketingGroups", binder));
			
			binder.addResultSet("rsMarketingSubGroups", 
			 facade.createResultSet("qClientServices_GetMarketingSubGroups", binder));
			
			binder.addResultSet("rsAccountGroups", 
			facade.createResultSet("qClientServices_GetAccountGroups", binder));
		}
	}
	
	/** Adds a custom selection of Organisation data to the binder, as specified by the 
	 *  passed dataConfig instance.
	 *  
	 * @param binder
	 * @param facade
	 * @param dataConfig
	 * @throws DataException
	 * @throws ServiceException
	 */
	public static void addEntityDataToBinder
	 (DataBinder binder, FWFacade facade, OrganisationDataConfig dataConfig, 
	 boolean debug) throws DataException, ServiceException {
		
		int entityId = resolveEntityId(binder, false, facade);
		addEntityDataToBinder(binder, entityId, facade, dataConfig, debug);
	}	
	
	/** Service-accessible method, designed to fetch customized data for the Org
	 *  specified in the binder.
	 *  
	 *  The 'fetchElements' binder parameter is expected to contain a comma-separated
	 *  list of data elements to be fetched. See the OrganisationDataConfig inner class 
	 *  for more details.
	 *  
	 *  If 'fetchElements' is emtpy/missing, all data elements are fetched.
	 *  
	 * @throws DataException 
	 * @throws ServiceException 
	 *  
	 */
	public void getEntityData() throws DataException, ServiceException {
		
		String fetchElements = m_binder.getLocal("fetchElements");
		OrganisationDataConfig dataConfig = null;
		
		if (StringUtils.stringIsBlank(fetchElements)) {
			dataConfig = new OrganisationDataConfig();
		} else {
			dataConfig = new OrganisationDataConfig(fetchElements);
		}
		
		boolean debug = !StringUtils.stringIsBlank(m_binder.getLocal("IsDebug"));
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		addEntityDataToBinder(m_binder, facade, dataConfig, debug);
	}
	
	/** Attempts to resolve an ORGANISATION_ID from the data in passed DataBinder.
	 *  
	 *  In the best case, the ORGANISATION_ID value will be present in the binder and
	 *  passed back following an Integer conversion.
	 *  
	 *  If the value isn't present, the method searches for (in the given order):
	 *  
	 *  1. Aurora fields CLIENT_NUMBER and COMPANY/FUND_CODE. The Aurora Client mapping 
	 *     is checked for a matching link that will yield the Org ID.
	 *  2. ORG_ACCOUNT_CODE.
	 *  
	 * @param binder
	 * @param allowMissing  if true, a RedirectUrl is added to the DataBinder
	 * 						pointing to the Add New Organisation page, if no
	 * 						matching Organisation is found for the passed params
	 * @param facade
	 * @return
	 * @throws ServiceException if the corresponding Entity isn't found,
	 * 							or required values are missing from the
	 * 							DataBinder.
	 * @throws DataException 
	 * @throws NumberFormatException 
	 */
	public static Integer resolveEntityId
	 (DataBinder binder, boolean allowMissing, FWFacade facade) 
	 throws ServiceException, NumberFormatException, DataException {
		
		String entityIdStr = binder.getLocal(Entity.Cols.ID);
		String orgAccountCode = binder.getLocal(Entity.Cols.ORG_ACCOUNT_CODE);
		
		if (!StringUtils.stringIsBlank(entityIdStr)) {
			// Easiest case - ORGANISATION_ID param present in binder.
			return Integer.parseInt(entityIdStr);
		} 
		
		// Look for Aurora CLIENT_NUMBER/COMPANY/ACCOUNT_NUMBER/FUND_CODE fields
		
		// Accept 'COMPANY' for the Company value.
		String company = binder.getLocal("COMPANY");
		
		String clientNumberStr 	= binder.getLocal(AuroraClient.Cols.CLIENT_NUMBER);
		String accountNumStr	= binder.getLocal("ACCOUNT_NUMBER"); 
		String fundCode	= binder.getLocal(Fund.COL_FUND_CODE);
		
		boolean auroraFieldsPresent = false;
		
		// Trim down strings.
		if (company != null) company = company.trim();
		if (accountNumStr != null) accountNumStr = accountNumStr.trim();
		if (clientNumberStr != null) clientNumberStr = clientNumberStr.trim();
		if (fundCode != null) fundCode = fundCode.trim();
		
		// We require a Client Number AND Account Number/Fund Code/Company to resolve an 
		// Org record.
		if (!StringUtils.stringIsBlank(clientNumberStr)
			&& (
				!StringUtils.stringIsBlank(accountNumStr)
				||
				!StringUtils.stringIsBlank(fundCode)
				||
				!StringUtils.stringIsBlank(company))
			) {
			auroraFieldsPresent = true;
		}
		
		if (auroraFieldsPresent) {
			// Try and find corresponding entry in Aurora Client mapping table.
			int clientNumber = Integer.parseInt(clientNumberStr);
			
			Entity entity = null;
			
			// Try and extract a valid Fund Code from the Account Number first.
			if (!StringUtils.stringIsBlank(accountNumStr)) {
				String fundCodeFromAccNum = CCLAUtils.getSuffixChars(accountNumStr);
				
				if (!StringUtils.stringIsBlank(fundCodeFromAccNum)) {
					// Overwrite passed Fund Code, if we found one in the Account Number
					// string!
					fundCode = fundCodeFromAccNum;
				}
			}
			
			// Ensure fund code always takes precedence over the actual Company
			// field.
			if (!StringUtils.stringIsBlank(fundCode)) {
				// Resolve company from Fund Code
				Fund fund =  Fund.getCache().getCachedInstance(fundCode);
				
				if (fund == null)
					throw new ServiceException("Fund Code " + fundCode + " is invalid");
				
				company = fund.getCompany().getCode();
			}
			
			// Attempt to resolve Entity from Client Number/Company combination.
			entity = Entity.getEntityFromAuroraValues(clientNumber, company, facade);
			
			// Whoop, found a matching org.
			if (entity != null) {
				CCLAUtils.addQueryIntParamToBinder
				 (binder, Entity.Cols.ID, entity.getOrganisationId());
				
				return entity.getOrganisationId();
			}
			
			// Corresponding Entity cannot be resolved from passed Aurora values.
			// If the allowMissing flag is set, we'll redirect to new Org page,
			// otherwise fail with error.
			if (allowMissing) {
				// Redirect to Add New Organisation screen, passing in the
				// Client Number/Company instead.
				String clientName = binder.getLocal("CLIENT_NAME");
				
				String redirectUrl = "?IdcService=CCLA_CS_ENTITY_NEW" +
				"&auroraClientMapMissing=1" +
				"&createNew=Yes" + "&CLIENT_NUMBER=" 
				+ clientNumberStr + 
				(company != null ? ("&COMPANY=" + company) : "");
				
				if (clientName != null)
					redirectUrl += "&useName=" + clientName;
				
				binder.putLocal("RedirectUrl", redirectUrl);
				return null;
				
			} else {
				throw new ServiceException
				 ("No corresponding Organisation with " +
				  "Client No: " + clientNumberStr + 
				  ", Company: " + company);
			}
		}
			
		// Finally, attempt to resolve Entity from Org Account Code
		if (!StringUtils.stringIsBlank(orgAccountCode)) {
			orgAccountCode = orgAccountCode.trim();
			
			Entity entity = Entity.getEntityByOrgAccountCode(orgAccountCode, facade);
			
			if (entity != null){
				Log.info("found orgId["+entity.getOrganisationId()+
				 "] from orgAccountCode["+orgAccountCode+"]");
				return entity.getOrganisationId();
			}else{
				throw new ServiceException("Invalid Org Account Code: "
				 +orgAccountCode);
			}
		}
		
		// If we got this far, there weren't any parameters to try doing a lookup
		// against.
		throw new ServiceException("Unable to resolve Organisation - " +
		 "insufficient parameters available");
	}
	
	/** Adds a new Entity.
	 * 
	 * @throws ServiceException 
	 * @throws DataException */
	public void addEntity() throws ServiceException, DataException {
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		try {
			facade.beginTransaction();
			
			// First, create the new Entity record. This will return
			// an Entity instance with the assigned Entity/Element ID.
			// user needed for audit table
			String username = m_userData.m_name;
			m_binder.putLocal("USER_ID", username);	
			Entity entity 	= Entity.add(m_binder, facade, username);
			int entityId	= entity.getOrganisationId();
			
			// Now create an Address record for the new entity, providing
			// there is sufficient address data.
			if (Address.addressDataExists(m_binder)) {
				String addressLabel = m_binder.getLocal("newAddressLabel");
				
				Address address = Address.add(m_binder, facade, username);
				int addressId	= address.getAddressId();
			
				// Add the 'Business address' contact mapping and mark it as
				// 'default' and designated address for Experian checks
				Contact.add(entityId, null, Contact.SUBMETHOD_ADDRESS_BUSINESS,
				 null, addressId, true, false, true, false, null, facade, username);
			}
			
			// Add common contact details, if user specified values
			String businessPhone = m_binder.getLocal("PHONE");
			if (!StringUtils.stringIsBlank(businessPhone))
				Contact.add(entityId, null, Contact.SUBMETHOD_PHONE_BUSINESS,
				 businessPhone, null, false, false, false, false, null, 
				 facade, username);

			String email =  m_binder.getLocal("EMAIL");
			if (!StringUtils.stringIsBlank(email))
				Contact.add(entityId, null, Contact.SUBMETHOD_EMAIL_BUSINESS,
				 email, null, false, false, false, false, null, facade, username);
			
			String website =  m_binder.getLocal("WEBSITE");
			if (!StringUtils.stringIsBlank(website))
				Contact.add(entityId, null, Contact.SUBMETHOD_WEBSITE_BUSINESS,
				 website, null, false, false, false, false, null, facade, username);
			
			// A relationship between an existing Person can be created
			// immediately for the new Entity. This is determined by the
			// presence of the related Person ID in the binder as
			// relatedPersonId.
			Integer relPersonId = CCLAUtils.getBinderIntegerValue
			 (m_binder, "relatedPersonId");
			Integer newRelationId = null;

			RelationName relName = null;
			
			Integer relationNameId = CCLAUtils.getBinderIntegerValue
			 (m_binder, "RELATION_NAME_ID");
			
			if (relationNameId != null)
				relName = RelationName.getCache().getCachedInstance(relationNameId);
			
			if (relPersonId != null) {
				Relation elemRelation = 
				 Relation.add(entityId, relPersonId, relName,
				 facade, 
				 username);
				
				newRelationId = elemRelation.getRelationId();
			}
			
			// Update applied element identifiers
			ElementIdentifierApplied.updateElementIdentifiersApplied
			 (entity.getOrganisationId(), ElementType.ORGANISATION, m_binder, 
			 facade, m_userData.m_name);
			
			facade.commitTransaction();
			
			// Append new entity ID (and relation ID, if applicable) to RedirectUrl
			String redirectUrl = m_binder.getLocal("RedirectUrl");
			
			if (!StringUtils.stringIsBlank(redirectUrl)) {
				redirectUrl += entityId;
				
				if (newRelationId != null)
					redirectUrl += "&newRelationId=" + newRelationId;
					
				m_binder.putLocal("RedirectUrl", redirectUrl);
			}
			
		} catch (Exception e){
			String msg = "Unable to create new Entity: " + e.getMessage();
			Log.error(msg, e);

			facade.rollbackTransaction();			
			throw new ServiceException(msg, e);
		}
	}
	
	/** Adds a new entry to the CLIENT_AURORA_MAP table.
	 * 
	 *  If a client number mapping already exists between the Org and a different
	 *  company, the client number is re-used in the new mapping, unless the 
	 *  overrideClientNumber flag is present, in which case the clientNumber value in
	 *  the binder is used instead.
	 *  
	 *  If no client number mapping previously existed, a new ID is generated from
	 *  sequence.
	 *  
	 * @throws DataException 
	 * @throws ServiceException 
	 * 
	 */
	public void addClientAuroraMap() throws DataException, ServiceException {
		
		Integer orgId = CCLAUtils.getBinderIntegerValue(m_binder, Entity.Cols.ID);
		Integer companyId = CCLAUtils.getBinderIntegerValue
		 (m_binder, AuroraClient.Cols.COMPANY);
		
		if (orgId == null || companyId == null)
			throw new ServiceException("Organisation ID/Company ID missing");
		
		Company newCompany = Company.getCache().getCachedInstance(companyId);
		
		Log.debug("Generating new Client Aurora Map for Org ID " + orgId + 
		 ", Company " + newCompany.getCode());
		
		if (newCompany == null)
			throw new ServiceException("No Company found with ID: " + companyId);
		
		Log.debug("Adding new Aurora Client Number mapping for Organisation ID: " +
		 orgId + ", Company: " + newCompany);
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		Vector<AuroraClient> auroraClients = 
		 Entity.getAuroraClientMapping(orgId, facade);
		
		Integer clientNo = null;
		
		boolean overrideClientNumber = CCLAUtils.getBinderBoolValue
		 (m_binder, "overrideClientNumber");
		
		// User opted to specify their own Client Number. Fetch it from the binder,
		// if its not present then throw an exception.
		if (overrideClientNumber) {
			clientNo = CCLAUtils.getBinderIntegerValue(m_binder, "clientNumber");
					
			Log.debug("User opted to override default Client Number allocation with " +
			 clientNo);
		}
		
		if (auroraClients.isEmpty() && clientNo == null) {
			// No mapped Aurora Client Numbers yet. We'll need to generate a new
			// one using the special sequence.
			clientNo = AuroraClient.getNewClientNumber(facade);
		} else {
			// Existing mappings present. Check there isn't a mapping for the same company
			// already.
			for (AuroraClient client : auroraClients) {
				if (clientNo == null) // Grab first mapped client number.
					clientNo = client.getClientNumber();
						
				if (client.getCompany().equals(newCompany)) {
					throw new ServiceException("Organisation already mapped to " +
					 newCompany.getCode() + " with ID: " + client.getClientNumber());
				}
			}	
		}
		
		try {
			facade.beginTransaction();
			
			ClientNumber clientNumber = new ClientNumber(clientNo, newCompany);
			clientNumber.addFieldsToBinder(m_binder);
			
			AuroraClient.add
			 (m_binder, facade, m_userData.m_name);
			
			facade.commitTransaction();
			
		} catch (Exception e) {
			Log.error("Failed to create new Client Aurora mapping" , e);
			throw new ServiceException(e);
		}
	}
	
	public void updateClientAuroraMap() throws DataException, ServiceException {
		
		Integer mapId = CCLAUtils.getBinderIntegerValue(m_binder, AuroraClient.Cols.ID);
		
		if (mapId == null)
			throw new ServiceException
			 ("Unable to update Aurora Client: mapping ID missing ");
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		try {
			facade.beginTransaction();
			
			AuroraClient auroraClient = AuroraClient.get(mapId, facade);
			
			auroraClient.setAttributes(m_binder);
			auroraClient.persist(facade, m_userData.m_name);
			
			facade.commitTransaction();
			
		} catch (Exception e) {
			Log.error("Failed to update Client Aurora mapping" , e);
			throw new ServiceException(e);
		}
	}
		
	/** Fetches entity data, suitable for use on an Edit screen.
	 * 
	 * @throws ServiceException if no matching entity is found in the DB
	 * @throws DataException 
	 */
	public void getEntity() throws ServiceException, DataException {

		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		Integer entityId = resolveEntityId(m_binder, true, facade);
		
		if (entityId != null) {
			OrganisationDataConfig dataConfig = new OrganisationDataConfig
			 (true, true, true, true, false, true, false, false, true, false, true, true);
			
			addEntityDataToBinder(m_binder, entityId, facade, dataConfig, false);
		}
	}

	/** Fetches full entity data, based on an ORGANISATION_ID in the binder.
	 *  
	 *  This loads the same stuff as the getEntity service, plus account,
	 *  processes and activity records associated with this Entity.
	 * 
	 * @throws ServiceException if ORGANISATION_ID isn't present, or no
	 * 							matching entity is found in the DB
	 * @throws DataException 
	 */
	public void getEntityExtended() throws ServiceException, DataException {
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		Integer entityId = resolveEntityId(m_binder, true, facade);
		
		if (entityId == null)
			return;

		if (entityId != null) {
			OrganisationDataConfig dataConfig = new OrganisationDataConfig
			 (true, true, true, true, true, true, true, true, true, false, true, true);
			addEntityDataToBinder(m_binder, entityId, facade, dataConfig, false);
		}
	}

	public static void addEntityEnrolmentDataToBinder
	 (int orgId, DataBinder binder, FWFacade facade) throws DataException {
		
		// Legacy campaign processes
		DataResultSet rsProcesses = 
		 Process.getOrganisationProcessData(orgId, facade);
		
		binder.addResultSet("rsProcesses", rsProcesses);
		
		// Campaign enrolments
		DataResultSet rsCampaignEnrolments = 
		 CampaignEnrolment.getDataExtendedByOrg(orgId, facade);
		 
		binder.addResultSet("rsCampaignEnrolments", rsCampaignEnrolments);
	}
	
	/** Fetches basic entity data, based on an ORGANISATION_ID in the binder.
	 *  
	 * @throws ServiceException if ORGANISATION_ID isn't present, or no
	 * 							matching entity is found in the DB
	 * @throws DataException 
	 */
	public void getEntityBasic() throws ServiceException, DataException {
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);

		//addBasicEntityDataToBinder(entityId, facade, m_binder);
		
		addEntityDataToBinder(m_binder, facade, new OrganisationDataConfig
		 (true, true, false, false, false, false, false, false, false, false, true, false), 
		 false);
	}	
	
	/** Updates an existing entity.
	 * 
	 * @throws DataException 
	 * @throws ServiceException 
	 */
	public void updateEntity() throws DataException, ServiceException {
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		try {
			facade.beginTransaction();
			
			Entity entity = getEntity(facade);
			
			// Update ORGANIASTION row
			entity.setAttributes(m_binder);
			entity.persist(facade, m_userData.m_name);
			
			Vector<AuroraClient> auroraClients = 
			 Entity.getAuroraClientMapping(entity.getOrganisationId(), facade);
			
			if (auroraClients.isEmpty()) {
				// No assigned Aurora Client Number yet. Check for Aurora identifiers
				
				/*
				Log.debug("Client Number not yet allocated to Organisation ID " +
				 entity.getOrganisationId() + ". Checking for Client Number/Company fields");
				
				// Extract Client Number/Company from binder
				ClientNumber clientNumber = 
				 Entity.getAuroraClientDataFromBinder(m_binder, facade);
				
				if (clientNumber != null) {
					AuroraClient.add(entity.getOrganisationId(), 
					 clientNumber.getCompany(), clientNumber.getClientNumber(), 
					 facade, m_userData.m_name);	
				}
				*/
			}
			
			// Update applied element identifiers
			ElementIdentifierApplied.updateElementIdentifiersApplied
			 (entity.getOrganisationId(), ElementType.ORGANISATION, m_binder, 
			 facade, m_userData.m_name);
			
			// update applied element attributes. Screen is hard-wired to only d
			// display/update 'Marketing Details' attributes at the moment.
			ElementAttributeApplied.updateElementAttributesApplied
			 (entity.getOrganisationId(), ElementType.ORGANISATION, 
			 SelectionType.NON_VERIFY_ONLY, 
			 ElementAttributeType.getCache().getCachedInstance(
			  ElementAttributeType.MARKETING_DETAILS
			 ), 
			 m_binder, facade,  m_userData.m_name);
			
			// update applied element attributes. Screen is hard-wired to only d
			// display/update 'Misc Organisation' attributes at the moment.
			ElementAttributeApplied.updateElementAttributesApplied
			 (entity.getOrganisationId(), ElementType.ORGANISATION, 
			 SelectionType.NON_VERIFY_ONLY, 
			 ElementAttributeType.getCache().getCachedInstance(
			  ElementAttributeType.MISC_ORG_DETAILS
			 ), 
			 m_binder, facade,  m_userData.m_name);
			
			facade.commitTransaction();
			
		} catch (Exception e) {
			facade.rollbackTransaction();
			
			String msg = "Failed to update Organisation: " + e.getMessage();
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}
	
	
	/** Checks the binder for an ORGANISATION_ID. If found, an Entity instance
	 *  is acquired and returned.
	 *  
	 * @return
	 * @throws ServiceException if ORGANISATION_ID is missing, or the corresponding
	 *   						Entity record isn't found.
	 * @throws DataException 
	 */
	private Entity getEntity(FWFacade facade) 
	 throws ServiceException, DataException {
		
		String entityIdStr = m_binder.getLocal("ORGANISATION_ID");
		
		if (StringUtils.stringIsBlank(entityIdStr)) {
			throw new ServiceException("Unable to load Entity record: " +
			 "no ORGANISATION_ID found.");
		}
		
		int entityId = Integer.parseInt(entityIdStr);
		Entity entity = Entity.get(entityId, facade);
		
		if (entity == null) {
			throw new ServiceException("Unable to load Entity record: " +
			 "no Entity with ID " + entityId + " found.");
		}
		
		return entity;
	}
	
	
	public void removeAuroraLink() throws ServiceException, DataException {
		
		String mapIdStr = m_binder.getLocal("CLIENT_AURORA_MAP_ID");
	
		if (StringUtils.stringIsBlank(mapIdStr)) {
			throw new ServiceException
			 ("Failed to remove Aurora Client mapping: " +
			  "missing CLIENT_AURORA_MAP_ID");
		} 

		int mapId		= Integer.parseInt(mapIdStr);
		FWFacade fw 	= CCLAUtils.getFacade(m_workspace, true);
		
		try {
			fw.beginTransaction();
			
			AuroraClient.remove(mapId, fw, m_userData.m_name);
			
			fw.commitTransaction();
		} catch (Exception e) {
			fw.rollbackTransaction();
			
			String msg = "Failed to remove Aurora Client mapping: " +
			 e.getMessage();
			
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}
	
	/** Used to fetch Entity Checking details and add them to the Binder.
	 * 
	 * @throws DataException 
	 * @throws ServiceException 
	 */
	public void getEntityVerificationDetails() throws DataException, ServiceException {
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		int entityId = resolveEntityId(m_binder, false, facade);
		
		Entity entity = Entity.get(entityId, facade);
		addEntityVerificationDetailsToBinder(entity, m_binder, facade);
	}
	
	/** Adds all ResultSets and other data used for determining Entity Check status.
	 *  
	 * @param entity
	 * @param binder
	 * @param facade
	 * @throws DataException
	 */
	private static void addEntityVerificationDetailsToBinder
	 (Entity entity, DataBinder binder, FWFacade facade) throws DataException {
		
		// Add all available verify-only element attributes
		binder.addResultSet("rsElementAttributes", 
		 ElementAttribute.getElementAttributesData(ElementType.ORGANISATION, 
		 ElementAttribute.SelectionType.VERIFY_ONLY, facade));
		
		// Add all applied verification attributes for this Org
		binder.addResultSet("rsElementAttributesApplied", 
		 ElementAttributeApplied.getAllData
		 (entity.getOrganisationId(), null, true, facade));
		
		// Add Entity Check result
		binder.putLocal("entityCheckStatus", 
		 Entity.getVerificationStatus(entity.getOrganisationId(), facade).toString());
	}
	
	/** Service method for fetching all processes for a given Entity.
	 * 
	 * @throws ServiceException
	 * @throws DataException
	 */
	public void getEntityProcessData() throws ServiceException, DataException {
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		int entityId = resolveEntityId(m_binder, false, facade);
	
		DataResultSet rsProcesses = 
		 Process.getOrganisationProcessData(entityId, facade);
		
		m_binder.addResultSet("rsProcesses", rsProcesses);
	}
	
	/** Fetches a list of account codes for a given Entity.
	 *  
	 *  These will be formatted as they are displayed in the external
	 *  account number, i.e. with 3/4 leading zeros.
	 * @throws DataException 
	 * @throws ServiceException 
	 * @throws NumberFormatException 
	 */
	public void getEntityAccountsForInstruction() 
	 throws DataException, ServiceException {
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		int entityId = resolveEntityId(m_binder, false, facade);
		
		DataResultSet rsAccounts = 
		 Account.getClientAccountsData(entityId, facade);
		
		Vector<Account> accounts 	= Account.getAccounts(rsAccounts);
		Iterator<Account> i 		= accounts.iterator(); 
		
		DataResultSet rsAccountNumbers = new DataResultSet(
		 new String[] { "ACCOUNT_NUMBER" });
		
		// Fetch associated Aurora Client IDs.
		Vector<AuroraClient> auroraClients = 
		 Entity.getAuroraClientMapping(entityId, facade);
		
		if (auroraClients.size() == 0)
			throw new ServiceException
			 ("No mapped Aurora client IDs for Entity ID: " + entityId);
		
		// Extract each account number/fund code and add to
		// ResultSet.
		while (i.hasNext()) {
			Account account = i.next();
			
			Iterator<AuroraClient> j = auroraClients.iterator();
			String accountNumber = null;
			
			while (j.hasNext() && accountNumber == null) {
				AuroraClient auroraClient = j.next();
				
				// Determine whether this Aurora Client ID matches
				// the current account's prefix.
				if (account.isAccountClientNumber
					(auroraClient.getClientNumber())) {
					
					accountNumber = account.getAccountNumberString();
				}
			}
			
			if (accountNumber == null) {
				throw new ServiceException("Unable to resolve account number " +
				 "for " + account.getAccNumExt() + ", no matching Aurora ID.");
			}
			
			Vector<String> v = new Vector<String>();
			v.add(accountNumber);
			
			rsAccountNumbers.addRow(v);
		}
		
		m_binder.addResultSet("rsAccountNumbers", rsAccountNumbers);
	}
	
	/** Fetches org/bank account data which is used to populate document instruction
	 *  fields.
	 *  
	 *  The first goal is to identify the Organisation ID the doc is indexed against,
	 *  via the resolveEntityId method.
	 *  
	 *  If an account code is passed (e.g. '003D'), this is used to fetch a 
	 *  ResultSet of all linked Bank Accounts, with extra details for the nominated
	 *  withdrawal account, if one exists.
	 *  
	 *  A ResultSet of nominated correspondent contact points are also returned, 
	 *  providing the document class is a valid Static Data Update type and account data
	 *  is present.
	 *  
	 * @throws DataException 
	 * @throws ServiceException 
	 * @throws NumberFormatException 
	 *  
	 */
	public void getEntityDataForInstruction() throws ServiceException, DataException {
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		// This method does a bunch of stuff to resolve an Org ID from various binder
		// attributes, including Aurora Client identifiers.
		int orgId = resolveEntityId(m_binder, false, facade);
		
		DataResultSet rsEntity = Entity.getData(orgId, facade);
		
		if (rsEntity.isEmpty())
			throw new ServiceException("Unable to find Entity with ID: " + orgId);
		
		// Add Org data to binder
		m_binder.addResultSet("rsEntity", rsEntity);
		
		String clientNumStr		= m_binder.getLocal(AuroraClient.Cols.CLIENT_NUMBER);
		String accountNumStr 	= m_binder.getLocal("ACCOUNT_NUMBER");
		
		// Fetch and lookup corresponding Fund Company from the Company Code (if present)
		String companyCode		= m_binder.getLocal("COMPANY");
		Company company			= null;
		
		if (!StringUtils.stringIsBlank(accountNumStr)) {
			// Account data passed, attempt to fetch Account data as well.
			Log.debug("Fetching instruction account info for Organisation ID: " 
			 + orgId + ", Account no: " + accountNumStr);
			
			try {
				// Use Organisation ID to perform Account lookup. Throws error if the
				// specified Account cannot be found.
				 Account account = Account.getAccountByIndexingValues
				 (orgId, accountNumStr, false, facade);
				
				if (account == null)
					// Probably won't ever trigger - the above fetch is very strict
					// and throws exception for various invalid fetch parameters.
					throw new ServiceException("No client account found with " +
					  " Account no: " + accountNumStr);
				 
				// Insert the Company, Fund and correctly-padded Client Number values into 
				// the binder.
				Fund fund = Fund.getCache().getCachedInstance(account.getFundCode());
				
				// Add Fund Code
				m_binder.putLocal(Fund.Cols.FUND_CODE, fund.getFundCode());
				
				// Add Company Code (xCompany field). This will add/replace the existing
				// Company Code on the displayed document.
				company = fund.getCompany();
				m_binder.putLocal(Company.Cols.CODE, company.getCode());
				 
				// Add Client Number
				if (!StringUtils.stringIsBlank(clientNumStr)) {
					// Strip away leading zeros
					clientNumStr = Integer.toString(Integer.parseInt(clientNumStr));
					
					m_binder.putLocal(AuroraClient.Cols.CLIENT_NUMBER, 
					 CCLAUtils.padString
					 (clientNumStr, '0', fund.getCompany().getClientNumberPadding()));
				}

				m_binder.putLocal("ENOUGH_DETAILS_FOR_CHECK","1");

				RelationName withdrawalRelName = RelationName.getCache()
				 .getCachedInstance(RelationName.AccountBankAccountRelation.WITHDRAWAL);
				
				// Get a list of all income/withdrawal bank accounts, with the 
				// authorized withdrawal account appearing first, if it exists.
				DataResultSet rsBankAccounts = 
				 Relation.getAccountBankAccountsData
				 (account.getAccountId(), withdrawalRelName, facade);
				
				// Add list of auth. bank accounts to binder
				m_binder.addResultSet("rsBankAccounts", rsBankAccounts);
				
				// Get the nominated withdrawal account from the previous ResultSet 
				//(must actually be marked as nominated in the DB)
				BankAccount nomBankAcc = 
				 Account.getNominatedBankAccount(rsBankAccounts);
				
				if (nomBankAcc != null) {
					m_binder.putLocal("IS_NOMINATED_BANK_ACCOUNT","1");
					
					 Log.debug("Returning details for the nominated withdrawal " +
					  "Bank Account: " + nomBankAcc.getAccountNumber());
					 
					 CCLAUtils.addQueryIntParamToBinder
					  (m_binder, "bankAccountId", nomBankAcc.getBankAccountId());
					 
					 m_binder.putLocal("bankAccountNumber", 
					 nomBankAcc.getAccountNumber());
	
					 m_binder.putLocal("bankSortCode", nomBankAcc.getSortCode());
					 
				} else {
					// No withdrawal account found.
					Log.debug("No withdrawal bank account found - returning empty " +
					 "bank account details");
					
					m_binder.putLocal("NON_RELATED_BANK_ACCOUNT", "1"); 
					
					m_binder.putLocal("bankAccountNumber", "00000000");
					m_binder.putLocal("bankSortCode", "000000");
				}
				
				// Attempt to add contact points belonging to the nominated account
				// correspondent. May do nothing if the doc class isn't valid type.
				String docClassStr     = m_binder.getLocal("DOC_CLASS");
				
				if (!StringUtils.stringIsBlank(docClassStr)) {
					// Determine the Instruction Type from the Document Class name.
					InstructionType instrType = 
					 InstructionType.getNameCache().getCachedInstance(docClassStr);

					if (instrType != null && StaticDataUpdateApplied
						.isInstructionGeneratingStaticDataInstructions(instrType))
						Contact.addNominatedCorrespondentContactPointsToBinder
						 (account, m_binder, facade);
				}
			
			} catch(Exception e) {
				Log.info(e.getMessage());
				m_binder.putLocal("EXTRA_ERROR_MSG", e.getMessage());
			}
		}
		
		// Now fetch (or replace) Aurora Client Number/Company field values, if 
		// applicable
		
		// Attempt to resolve Company from passed Company Code. If a valid Fund Code
		// was found before, Company will already be set previously.
		if (company == null)
			company	= companyCode != null ? Company.getNameCache()
			 .getCachedInstance(companyCode) : null;
		
		AuroraClient auroraClient = null;
			 
		if (company != null)
			auroraClient = Entity.getAuroraClientCompanyMapping
			 (orgId, company, facade);
		else {
			// If they only have 1 mapped Aurora Client, just assume that one.
			Vector<AuroraClient> auroraClients = 
			 Entity.getAuroraClientMapping(orgId, facade);
			
			if (auroraClients.size() == 1)
				auroraClient = auroraClients.get(0);
			else {
				// Ambiguous set of Aurora Client mappings!
				// Just add a flag to binder.
				m_binder.putLocal("multipleAuroraClientLinks", "1");
			}
		}
		
		if (auroraClient != null) {
			// Add the resolved CLIENT_NUMBER/COMPANY_CODE to the binder.
			Log.debug("Adding Aurora Client details to binder: " +
			 auroraClient.getCompany().getCode() + ", " + 
			 auroraClient.getPaddedClientNumber());
			m_binder.putLocal(AuroraClient.Cols.CLIENT_NUMBER, 
			 auroraClient.getPaddedClientNumber());
			m_binder.putLocal(Company.Cols.CODE, 
			 auroraClient.getCompany().getCode());
		} else {
			// No Aurora Client data found - wipe it out!
			m_binder.putLocal(AuroraClient.Cols.CLIENT_NUMBER, "");
			m_binder.putLocal(Company.Cols.CODE, "");
		}
	}

	/** Available Organisation 'bulk edit' modes.
	 * 
	 * @author Tom
	 *
	 */
	public static class BulkEditType {
		public static final String PERSON = "person";
		public static final String BANK_ACCOUNT = "bankAccount";
		public static final String NOMINATED_BANK_ACCOUNT = "nominatedBankAccount";
		public static final String ACCOUNT_FLAG = "accountFlag";
		public static final String NOMINATED_CORRESPONDENT = "nominatedCorrespondent";
		public static final String NUM_REQUIRED_SIGNATURES = "numRequiredSignatures";
	}
	
	/** Used to present data on the Organisation Bulk update page.
	 * 
	 *  Loads in some standard ResultSets relating to the current Organisation and its
	 *  Accounts.
	 *  
	 *  The other ResultSets that get loaded in are dependent on the 'editType' 
	 *  parameter in the binder.
	 *  
	 * @throws ServiceException if no matching entity is found in the DB
	 * @throws DataException 
	 */
	public void getEntityBulkUpdate() throws ServiceException, DataException {

		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		Integer entityId = resolveEntityId(m_binder, true, facade);
		
		if (entityId == null) {
			throw new ServiceException("Organisation ID missing");
		}
		
		// Get the standard entity information
		OrganisationDataConfig dataConfig = new OrganisationDataConfig
		 (true, true, false, false, true, false, false, false, false, false, true, false);
		
		addEntityDataToBinder(m_binder, entityId, facade, dataConfig, false);
				
		String editType = m_binder.getLocal("editType");
		
		// Default to bulk update of related persons.
		if (StringUtils.stringIsBlank(editType))
			editType = BulkEditType.PERSON;
		
		if (editType.equals(BulkEditType.PERSON)
			||
			editType.equals(BulkEditType.NOMINATED_CORRESPONDENT)) {
			// Returns all unique persons directly related to the Organisation ID, plus 
			// all persons related to accounts linked to the Organisation.
			DataResultSet rsRelatedPersons = facade.createResultSet
			 ("qClientServices_GetEntityAccountPersons", m_binder);
			
			m_binder.addResultSet("rsRelatedPersons", rsRelatedPersons);
			
		} else if (editType.equals(BulkEditType.BANK_ACCOUNT)
			|| editType.equals(BulkEditType.NOMINATED_BANK_ACCOUNT)) {
			// Returns all unique bank accounts linked to the Organisation ID.
			DataResultSet rsRelatedBankAccounts = facade.createResultSet
			 ("qClientServices_GetEntityAccountBankAccount", m_binder);
			
			m_binder.addResultSet("rsRelatedBankAccounts", rsRelatedBankAccounts);
			
		} else if (editType.equals(BulkEditType.ACCOUNT_FLAG)) {
			// Returns the static list of available Account Flags
			DataResultSet rsAccountFlags = AccountFlag.getAll(facade);
			m_binder.addResultSet("rsAccountFlags", rsAccountFlags);
		}
	}
	
	/**
	 * Fetches the required Organisation Category list for a selected Category
	 * ID, i.e. all categories which have the selected Category ID as their
	 * parent category. 
	 * 
	 * The Category ID can be null/empty - this will return the top-level
	 * categories which don't have a parent.
	 * 
	 * Should be used when refreshing the selectable category lists via AJAX.
	 * 
	 * @throws DataException 
	 * 
	 */
	public void getOrganisationCategoryLists() throws DataException {
		
		Integer categoryId = CCLAUtils.getBinderIntegerValue
		 (m_binder, "categoryId");
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		OrganisationCategory.addCategoryListsToBinder
		 (categoryId, facade, m_binder);
	}
	
	/** Service method called via web service, used by SPP.
	 *  
	 *  Returns a custom ResultSet containing three fields:
	 *  -QRS_CLIENT: 	flag indicating whether they are QRS client or not (0/1)
	 *  -CRM_INITIALS:	initials of customer relations manager
	 *  -CRM_NAME:		full name of CRM
	 *  
	 *  Both CRM fields may be returned blank.
	 *  
	 *  TODO: refactor to work with new code base.
	 *  
	 * @throws ServiceException
	 * @throws DataException 
	 */
	public void getClientQRSData() throws ServiceException, DataException {
		
		String clientIdStr 	= m_binder.getLocal("clientId");
		String company		= m_binder.getLocal("company");
		
		Log.debug("Fetching client QRS data for client: " + clientIdStr 
		 + ", company: " + company);

		if (StringUtils.stringIsBlank(clientIdStr) ||
			StringUtils.stringIsBlank(company)) {
			throw new ServiceException("Unable to fetch client QRS data: " +
			 "not enough parameters");
		}
		
		int clientId = Integer.parseInt(clientIdStr);
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		Entity entity   =
		 Entity.getEntityFromAuroraValues(clientId, company, facade);
		
		if (entity == null)
			throw new ServiceException("No organisation found for " +
			"client: " + clientIdStr + ", company: " + company);
		
		DataResultSet rsQRSAccounts = Entity.getOrganisationQRSAccountsData
		 (entity.getOrganisationId(), facade);
		
		Log.debug("Found " + rsQRSAccounts.getNumRows() + " QRS accounts");
		
		DataResultSet rsQRS = new DataResultSet( new String[] {
			"QRS_CLIENT",
			"CRM_INITIALS",
			"CRM_NAME"
		});
		
		String qrsClient 	= "0";
		
		if (rsQRSAccounts.getNumRows() > 0)
			qrsClient		= "1";
		
		String crmInitials 	= "";
		String crmName		= "";
		
		Vector<String> v = new Vector<String>();
		v.add(qrsClient);
		v.add(crmInitials);
		v.add(crmName);
		
		rsQRS.addRow(v);
		
		m_binder.addResultSet("rsQRS", rsQRS);
	}
	
	
	/** One-off service method for creating the ~150 TTLA organisations.
	 *  
	 *  Requires the temp table TTLA_ORGS_TEMP in place and filled with the TTLA Org
	 *  Names.
	 * @throws DataException 
	 * @throws ServiceException 
	 */
	public void createTTLAOrgs() throws DataException, ServiceException {
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		
		// Fetch names of TTLA Orgs from temp table
		DataResultSet rs = facade.createResultSetSQL
		 ("SELECT * FROM TTLA_ORGS_TEMP ORDER BY ORGANISATION_NAME");
		
		Log.debug("Adding " + rs.getNumRows() + " TTLA Orgs");
		String username = m_userData.m_name;
		
		try {
			facade.beginTransaction();
			
			if (rs.first()) {
				do {
					String orgName = rs.getStringValue(0);
					
					Log.debug("Adding new Organisation for TTLA: " + orgName);
					
					DataBinder binder = new DataBinder();
					
					// Add Org Name/Category ID
					binder.putLocal(Entity.Cols.ORGANISATION_NAME, orgName);
					CCLAUtils.addQueryIntParamToBinder(binder, Entity.Cols.CATEGORY_ID, 
					 OrganisationCategory.CategoryIds.TTLA);
					
					// Add special params
					binder.putLocal(Entity.ORG_ACCOUNT_CODE_PREFIX, "TTLA");
					CCLAUtils.addQueryBooleanParamToBinder(binder, 
					 Entity.SKIP_AURORA_CLIENT_NUMBER_GENERATION, true);
					
					Entity entity = Entity.add(binder, facade, username);
		
					Log.debug("Added with ID: " + entity.getOrganisationId() + ", " +
					 "Org Account Code: " + entity.getOrgAccountCode());
					
				} while (rs.next());
			}
			
			facade.commitTransaction();
			
			Log.debug("Added " + rs.getNumRows() + " TTLA Orgs");
			
		} catch (Exception e) {
			facade.rollbackTransaction();
			
			Log.error("Failed to create TTLA Orgs" ,e);
			throw new ServiceException("Failed to create TTLA Orgs", e);
		}
	}
	
	/** Fetches a list of all Organisations with an applied Element Attribute ID and 
	 *  Attribute Status.
	 *  
	 * @throws DataException 
	 * 
	 */
	public void getOrgsWithAttributeStatus() throws DataException {
		
		FWFacade facade = CCLAUtils.getFacade(m_workspace,true);
		
		Integer attributeId = CCLAUtils.getBinderIntegerValue
		 (m_binder, ElementAttribute.Cols.ID);
		
		if (attributeId == null) {
			throw new DataException("Unable to load Orgs with attribute status: no " +
			 ElementAttribute.Cols.ID + " passed in");
		}
		
		m_binder.addResultSet
		 ("rsOrgsWithAttributeStatus", Entity.getOrgsWithAttributeStatus
		 (attributeId, true, facade));
	}
	
	/** Fetch query type used when fetching recent Organisation Documents.
	 *  
	 * @author Tom
	 *
	 */
	public enum OrgDocFetchMethod {
		CLIENT_NUMBER_AND_COMPANY,
		ORG_ACCOUNT_CODE
	}
	
	/** Fetches a list of recent documents linked to a given Organisation ID.
	 *  
	 *  The link will always be indirect - the following doc metadata fields refer to
	 *  Org records:
	 *  
	 *  -xClientNumber/xCompany
	 *  -xOrgAccountCode
	 *  
	 *  The following parameters must be present in the binder before calling the
	 *  service:
	 *  
	 *  -numItems
	 *  -orgDocFetchMethod
	 *  
	 *  If the doc fetch method is CLIENT_NUMBER_AND_COMPANY, the following parameters
	 *  are required in the binder:
	 *  
	 *  -COMPANY_CODE
	 *  -CLIENT_NUMBER
	 *  -CLIENT_NUMBER_PADDED
	 *  
	 *  If the doc fetch method is ORG_ACCOUNT_CODE, the following parameters are
	 *  required in the binder:
	 *  
	 *  -ORG_ACCOUNT_CODE
	 *  
	 *  Optional:
	 *  
	 *  -invoiceDocsOnly. If non-null, the above fetch will only return docs of invoice
	 *  types (INV, INVHIST, MULTIINV)
	 *  
	 * @throws DataException 
	 * @throws ServiceException 
	 *  
	 */
	public void getRecentOrgDocs() throws DataException, ServiceException {
		
		// Max results to fetch
		Integer numItems = CCLAUtils.getBinderIntegerValue(m_binder, "numItems");

		if (numItems == null) {
			throw new ServiceException("Max doc fetch count missing");
		}
		
		String fetchMethodStr = m_binder.getLocal("orgDocFetchMethod");
		
		OrgDocFetchMethod fetchMethod = null;
		
		if (!StringUtils.stringIsBlank(fetchMethodStr)) {
			fetchMethod = OrgDocFetchMethod.valueOf(fetchMethodStr);
		}
		
		// Must use standard UCM facade here as we are fetching UCM doc metadata
		FWFacade facade = CCLAUtils.getFacade(m_workspace);
		
		DataResultSet rsOrganisationDocs = null;
		
		if (fetchMethod == OrgDocFetchMethod.CLIENT_NUMBER_AND_COMPANY) {
			String clientNumber = 
			 m_binder.getLocal(AuroraClient.Cols.CLIENT_NUMBER);
			String clientNumberPadded = 
			 m_binder.getLocal("CLIENT_NUMBER_PADDED");
			String companyCode = m_binder.getLocal("COMPANY_CODE");
			
			Log.debug("Fetching recent org docs for Client Number: " + clientNumber +
			 ", Padded Client Number: " + clientNumberPadded + ", Company: " + 
			 companyCode);
			
			// Fetch by specific Client Number/Company pair
			rsOrganisationDocs = facade.createResultSet
			 ("qClientServices_GetRecentOrgDocsByClientNumberAndCompany", m_binder);
		} else if (fetchMethod == OrgDocFetchMethod.ORG_ACCOUNT_CODE) {
			// Fetch by Org Code only
			String orgAccountCode = m_binder.getLocal(Entity.Cols.ORG_ACCOUNT_CODE);
			
			boolean invoiceDocsOnly = CCLAUtils.getBinderBoolValue
			 (m_binder, "invoiceDocsOnly");
			
			Log.debug("Fetching recent org docs for Org Account Code: " + 
			 orgAccountCode + ", Invoice Docs Only: " + invoiceDocsOnly);
			
			if (invoiceDocsOnly) {
				rsOrganisationDocs = facade.createResultSet
				 ("qClientServices_GetRecentOrgInvoiceDocsByOrgAccountCode", m_binder);
			} else {
				rsOrganisationDocs = facade.createResultSet
				 ("qClientServices_GetRecentOrgDocsByOrgAccountCode", m_binder);
			}
			
		} else {
			throw new ServiceException("Unknown Org document fetch method");
		}
		
		m_binder.addResultSet("rsOrganisationDocs", rsOrganisationDocs);
	}

	/**
	 * @UCM_Service CCLA_CS_GET_MARKETING_SUBGROUPS
	 * @throws ServiceException
	 * @throws DataException
	 */
	public void getMarketingSubgroups() throws ServiceException, DataException {
		//5:qClientServices_GetMarketingSubGroups:rsMarketingSubGroups::null
		FWFacade facade = CCLAUtils.getFacade(m_workspace, true);
		DataBinder binder = new DataBinder();		
		DataResultSet rsMarketingSubGroups = 
			facade.createResultSet("qClientServices_GetMarketingSubGroups", binder);
		m_binder.addResultSet("rsMarketingSubGroups", rsMarketingSubGroups);
	}	
	
}
