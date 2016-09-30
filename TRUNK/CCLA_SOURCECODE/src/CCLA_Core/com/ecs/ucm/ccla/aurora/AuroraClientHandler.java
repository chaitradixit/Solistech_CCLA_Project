package com.ecs.ucm.ccla.aurora;

import intradoc.common.ServiceException;
import intradoc.data.DataException;

import java.util.Date;
import java.util.Vector;

import com.aurora.webservice.Address;
import com.aurora.webservice.Client;
import com.aurora.webservice.EncryptionTypes;
import com.aurora.webservice.ExemptionTypes;
import com.ecs.ucm.ccla.Globals;
import com.ecs.ucm.ccla.aurora.compare.AuroraEntityComparisonOutcome;
import com.ecs.ucm.ccla.aurora.compare.ClientFieldSet;
import com.ecs.ucm.ccla.data.AuroraClient;
import com.ecs.ucm.ccla.data.AuroraCorrespondent;
import com.ecs.ucm.ccla.data.Company;
import com.ecs.ucm.ccla.data.Contact;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.SystemConfigVar;
import com.ecs.ucm.ccla.utils.DateUtils;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.embedded.FWFacade;

public class AuroraClientHandler 
 extends AuroraEntityHandler<Client, Entity> {
	
	public static final int MAX_CLIENTNAME_LENGTH = 80;
	
	@Override
	public Client buildAuroraEntityInstance(Entity org, Company company,
			FWFacade facade) throws DataException {

		validateDBInstance(org, company, facade);
		
		// Find Aurora Client mapping for this Organisation
		AuroraClient clMap = Entity.getAuroraClientCompanyMapping
		 (org.getOrganisationId(), company, facade);
		
		if (clMap == null) {
			// Fail if no mapping
			throw new DataException("No Aurora Client preferences set for " +
			 "Company " + company.getCode());
		}
		
		Person corr = Entity.getNominatedCorrespondent
		 (org.getOrganisationId(), false, facade);
		
		// Fail if no correspondent set
		if (corr == null) {
			String msg = "No Correspondent set, or there are multiple correspondents set "
			 + "with none marked as nominated";
			Log.error(msg);
			throw new DataException(msg);
		}
		
		Vector<AuroraCorrespondent> corrMap = 
		 AuroraCorrespondent.getCorrespondentsByPersonIdAndCompanyId
		 (corr.getPersonId(), company.getCompanyId(), facade);
		
		// Fail if correspondent doesn't have Aurora Correspondent mapping
		if (corrMap.isEmpty()) {
			String msg = "no " + company.getCode() + " Aurora Correspondent link set";
			Log.error(msg);
			throw new DataException(msg);
		}
		
		Integer defaultCorrId = corrMap.get(0).getCorrId();
		
		// Fail if correspondent link Code not set yet.
		if (defaultCorrId == null) {
			String msg = "Aurora Correspondent link for Company " 
			 + company.getCode() + " must be added to Aurora first";
			Log.error(msg);
			throw new DataException(msg);
		}
		
		// Now build the Aurora Client instance
		Client client = new Client();
		setDefaultValues(client);
		
		// Set the Client Number
		client.setClientNumber(clMap.getClientNumber());
		
		// Contributor Code cannot be null. If the Contributor Code field is null on the
		// Client mapping, set it to the Client Number instead.
		client.setContributorCode
		 (clMap.getContributorCode() == null 
		  ? clMap.getClientNumber() : clMap.getContributorCode());
		
		// Set the Client Name. Truncate if neccessary
		String truncOrgName = 
		 org.getOrganisationName().length() > MAX_CLIENTNAME_LENGTH ?
		  org.getOrganisationName().substring(0, MAX_CLIENTNAME_LENGTH) :
		  org.getOrganisationName();
		
		client.setName(truncOrgName);
		
		// Set the default correspondent ID
		client.setDefaultCorrespondentNumber(defaultCorrId);
		
		// Set the Data Protection indicator. This is based on the value of the Data
		// Protection flag against the default correspondent record.
		client.setDataProtectionMailingGroupIndicator(corr.isDoNotContact());
		
		// Set the address and contact details.
		Address addr = AuroraWebServiceUtils.getAddressInstance(org.getContacts());
		
		client.setAddress(addr);
		
		//Set the date opened
		client.setDateOpened
		 (DateUtils.formatddsMMsyyyyspHHcmm(org.getDateEnrolled() != null 
		 ? org.getDateEnrolled() : new Date()));
		
		// Set signatories.
		Vector<Person> sigs = AuroraSignatories.getPersonSignatoryList
		 (org, facade);
		
		Vector<AuroraSignatory> auroraSigs = AuroraSignatories.getSignatories(sigs);
		AuroraSignatories.applyToAuroraEntity(auroraSigs, client);
		
		// Statement month values cannot be null.
		client.setStatementMonths1
		 (clMap.getStatementMonths1() == null ? 1 : clMap.getStatementMonths1());
		client.setStatementMonths2
		 (clMap.getStatementMonths2() == null ? 0 : clMap.getStatementMonths2());
		client.setStatementMonths3
		 (clMap.getStatementMonths3() == null ? 0 : clMap.getStatementMonths3());
		client.setStatementMonths4
		 (clMap.getStatementMonths4() == null ? 0 : clMap.getStatementMonths4());

		// Contributor Type/Subdivision Codes cannot be null.
		client.setContributorTypeCode
		 (clMap.getContributorTypeCode() == null ? 0 : clMap.getContributorTypeCode());
		client.setSubDivisionCode
		 (clMap.getSubdivisionCode() == null ? 0 : clMap.getSubdivisionCode());
		
		// Other statement prefs stuff
		client.setIncomeDistributionReportIndicator(clMap.isIncomeDistReportIndicator());
		client.setIncomeDistributionReportPaperIndicator(clMap.isIncomeDistReportPaperIndicator());
		client.setStatementMonthsAllIndicator(clMap.isStatementMonthsAll());
		client.setBulkDepositStatementsIndicator(clMap.isBulkDepositStatementIndicator());
		client.setDepositAndWithdrawalBooksIndicator(clMap.isDepositWithdrawBooksIndicator());
		client.setAutomaticStationaryOrderedIndicator(clMap.isAutoStationaryOrderedIndicator());
		client.setQuarterlyReportingClientIndicator(clMap.isQuarterlyReportClientIndicator());
		client.setQuarterlyReportingManagerInitials(clMap.getQuarterlyReportManagerInitials());

		client.setNumberOfStatementsDeposit
		 (clMap.getNumDepositStatements() == null 
		 ? 1 : clMap.getNumDepositStatements());
		
		client.setNumberOfStatementsUnitised
		 (clMap.getNumUnitisedStatements() == null
		 ? 1 : clMap.getNumUnitisedStatements());
		
		if (clMap.getMarketingGroupId()!=null)
			client.setMarketingGroup(clMap.getMarketingGroupId());
		
		if (clMap.getMarketingSubGroupId()!=null)
			client.setMarketingSubGroup(clMap.getMarketingSubGroupId());
		
		if (clMap.getAccountGroupId()!=null)
			client.setAccountGroup(clMap.getAccountGroupId());
		
		Log.debug("Generated Client instance from Organisation ID: "
		 + org.getOrganisationId());
		Log.debug(client.toString());
		
		return client;
	}

	@Override
	protected void setDefaultValues(Client client) throws DataException {
		// Setting designated default values, required for create-client calls to work
		client.setIsSatisfactoryDocumentationPresent(true);
		client.setDepositStatementsIndicator(true);
		client.setDepositStatementsPaperIndicator(true);
		client.setInvestmentStatementsPaperIndicator(true);
		client.setAuditCertificatePrintIndicator(true);
		client.setAuditCertificateDestination("C");
		client.setAuditPriceType("BID");
		
		client.setIVSReferenceNumber("");
		client.setAuditPriceType("");
		client.setAuditCertificateDestination("");
		client.setAuthorityHeldDate("");
		client.setIVSCFID("");
		client.setMultipleSignatoryInformation("");
		client.setAuditorReference("");
		
		//Set these to standard defaults. They won't be checked/updated by future calls.
		client.setExemptionType(ExemptionTypes.OTHER);
		client.setExemptionNumber("TakeOn");
		
		client.setEncryptionMethod(EncryptionTypes.UNSET);
		
		Vector<Contact> addrVec = new Vector<Contact>();
		client.setAuditorAddress(AuroraWebServiceUtils.getAddressInstance(addrVec));
	}

	@Override
	public void validateDBInstance(Entity org, Company company,
			FWFacade facade) throws DataException {
		
		// Find Aurora Client mapping for this Organisation
		AuroraClient clMap = Entity.getAuroraClientCompanyMapping
		 (org.getOrganisationId(), company, facade);
		
		if (clMap == null) {
			// Fail if no mapping
			throw new DataException("No Aurora Client preferences set for " +
			 "Company " + company.getCode());
		}
		
		Person corr = Entity.getNominatedCorrespondent
		 (org.getOrganisationId(), false, facade);
		
		// Fail if no correspondent set
		if (corr == null) {
			String msg = "No Correspondent set, or there are multiple correspondents set "
			 + "with none marked as nominated";
			Log.error(msg);
			throw new DataException(msg);
		}
		
		Vector<AuroraCorrespondent> corrMap = 
		 AuroraCorrespondent.getCorrespondentsByPersonIdAndCompanyId
		 (corr.getPersonId(), company.getCompanyId(), facade);
		
		// Fail if correspondent doesn't have Aurora Correspondent mapping
		if (corrMap.isEmpty()) {
			String msg = "no " + company.getCode() + " Aurora Correspondent link set";
			Log.error(msg);
			throw new DataException(msg);
		}
		
		Integer defaultCorrId = corrMap.get(0).getCorrId();
		
		// Fail if correspondent link Code not set yet.
		if (defaultCorrId == null) {
			String msg = "Aurora Correspondent link for Company " 
			 + company.getCode() + " must be added to Aurora first";
			Log.error(msg);
			throw new DataException(msg);
		}
	}

	@Override
	public void validateAuroraInstance(Client client, Company company,
			FWFacade facade) throws DataException {
		if (client.getDefaultCorrespondentNumber() == 0)
			throw new DataException("Default Correspondent number not set");
		
	}

	@Override
	public Client getExistingAuroraEntity(Entity dbEntity, Company company,
	 FWFacade facade) throws DataException {
		
		Log.debug("Attempting to fetch existing Aurora Client for Org ID: "
		 + dbEntity.getOrganisationId() + ", Company: " + company.getCode());
		
		validateDBInstance(dbEntity, company, facade);
		
		AuroraClient clMap = Entity.getAuroraClientCompanyMapping
		 (dbEntity.getOrganisationId(), company, facade);
		
		if (clMap == null) {
			String msg = "No Aurora " + company.getCode() + " Client link set against " +
			 "Organisation ID " + dbEntity.getOrganisationId();
			
			Log.error(msg);
			throw new DataException(msg);
		}

		Client existingClient = null;

		if (AuroraWebServiceHandler.TEST_MODE) {
			Log.debug("Running in Web Service test mode.");
			SystemConfigVar testOrgId = SystemConfigVar.getCache().getCachedInstance
			 ("SDU_TestOrgId");
			
			if (testOrgId != null && testOrgId.getIntValue() != null) {
				Log.debug("Building test Aurora client instance from Org ID " 
				 + testOrgId.getIntValue());
				
				existingClient = buildAuroraEntityInstance
				 (Entity.get(testOrgId.getIntValue(), true, facade), company, facade);
			} else {
				Log.debug("No test Org ID found - returning null");
			}
		} else {
			try {
				existingClient = 
				 AuroraWebServiceHandler.getAuroraWS().getClientByClientNumber
				 (company.getCode(), clMap.getClientNumber());
			
			} catch (Exception e) {
				// Assume error was thrown by Aurora, as Client did not exist.
				Log.debug("Failed to fetch existing Aurora Client: " + e.getMessage());
			}
		}
		
		if (existingClient != null)
			Log.debug("Found existing Aurora Client with Client Number " 
			 + existingClient.getClientNumber());
		else
			Log.debug("No existing Aurora Client found");
			
		return existingClient;
	}

	@Override
	public void addAuroraEntity(Entity dbEntity, Company company,
			FWFacade facade) throws DataException, ServiceException {

		if (dbEntity.getDateEnrolled() == null) {
			Log.debug("Setting enrolment date against Organisation record"); 
			dbEntity.setDateEnrolled(new Date());
			dbEntity.persist(facade, Globals.Users.System);
		}
		
		super.addAuroraEntity(dbEntity, company, facade);
		
		Log.debug("Added new Aurora Client record: Organisation ID " 
		 + dbEntity.getOrganisationId() + ", Company " + company.getCode());
		
		Log.debug("Updating Central DB Client Mapping"); 
		Client newClient = getExistingAuroraEntity(dbEntity, company, facade);

		if (newClient!=null) {
			updateAuroraMappingWithClientData(dbEntity, newClient, company, facade);
		} else {
			throw new ServiceException("Client record from Organisation ID: " + 
			 dbEntity.getOrganisationId() + ", Company: " + company.getCode()
			 + " has been added but not found in Aurora");
		}
	}
	
	/**
	 * Update CDB AuroraClientMapping based on the data from the client (in Aurora)
	 * 
	 * @param org
	 * @param client
	 * @param facade
	 * @throws DataException
	 */
	private void updateAuroraMappingWithClientData
	 (Entity org, Client client, Company company, FWFacade facade) 
	 throws DataException, ServiceException {
		
		// Find Aurora Client mapping for this Organisation
		AuroraClient clMap = Entity.getAuroraClientCompanyMapping
		 (org.getOrganisationId(), company, facade);
		
		if (clMap == null) {
			// Fail if no mapping
			String msg = "No Aurora Client Number set for " +
			 "Organisation ID " + org.getOrganisationId() + 
			 ", Company " + company.getCode();
			
			Log.error(msg);
			throw new ServiceException(msg);
		}
	
		copyAuroraClientDataToAuroraClientMapping(clMap, client);
		clMap.persist(facade, Globals.Users.System);
	}
	
	/** Copies all attributes from an Aurora Client instance in the Aurora database to
	 *  its equivalent AuroraClient mapping in the Central DB.
	 *  
	 *  This will be called following a successfull create/amendment to an Aurora
	 *  Client record, copying fields into the Central DB that are known only by
	 *  Aurora.
	 *  
	 * @param auroraClient
	 * @param client
	 */
	private void copyAuroraClientDataToAuroraClientMapping
	 (AuroraClient auroraClient, Client client) {
		
		if (auroraClient==null || client==null)
			return;	
		
		auroraClient.setContributorCode(client.getContributorCode());
		auroraClient.setContributorTypeCode(client.getContributorTypeCode());
		auroraClient.setSubdivisionCode(client.getSubDivisionCode());
	}
	
	@Override
	protected Object addToAurora(Client auroraEntity, Company company,
			FWFacade facade) throws DataException {	
		// Call Aurora Web Service to add the client.
		Log.debug("Creating Aurora Client: \n" + auroraEntity.toString());
		
		try {
			if (AuroraWebServiceHandler.TEST_MODE) {
				Log.debug("Web Service Test Mode: skipping call, returning zero");
				return 0;
			} else {
				Integer clientNumber = AuroraWebServiceHandler.getAuroraWS().createClient
				 (company.getCode(), auroraEntity, false);
	
				return clientNumber;
			}
			
		} catch (Exception e) {
			String msg = "Failed to add new Aurora client: " + e.getMessage();
			
			Log.error(msg, e);
			throw new DataException(msg, e);
		}
	}


	@Override
	public void updateAuroraEntity(Entity dbEntity, Company company,
			Vector<String> fieldGroupNames, FWFacade facade)
			throws DataException, ServiceException {
		super.updateAuroraEntity(dbEntity, company, fieldGroupNames, facade);
		
		// Update local data
		Client client = getExistingAuroraEntity(dbEntity, company, facade);
		updateAuroraMappingWithClientData(dbEntity, client, company, facade);
	}
	
	@Override
	protected Object updateInAurora(Client auroraEntity, Company company,
			FWFacade facade) throws DataException {
		
		if (AuroraWebServiceHandler.TEST_MODE) {
			Log.debug("Web Service Test Mode: skipping call, returning true");
			return true;
		} else {
			try {
				return AuroraWebServiceHandler.getAuroraWS().amendClient
				 (company.getCode(), auroraEntity);
				
			} catch (Exception e) {
				String msg = "Failed to update Aurora client: " + e.getMessage();
				
				Log.error(msg, e);
				throw new DataException(msg, e);
			}
		}
	}

	@Override
	public ClientFieldSet getAuroraFieldSet(Client auroraEntity)
			throws DataException {
		return new ClientFieldSet(auroraEntity);
	}
}
