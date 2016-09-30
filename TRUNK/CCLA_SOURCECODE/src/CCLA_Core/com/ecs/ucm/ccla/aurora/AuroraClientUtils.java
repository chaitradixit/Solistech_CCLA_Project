package com.ecs.ucm.ccla.aurora;

import intradoc.common.ServiceException;
import intradoc.data.DataException;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.beanutils.BeanUtils;

import com.aurora.webservice.Address;
import com.aurora.webservice.Client;
import com.aurora.webservice.Correspondent;
import com.aurora.webservice.EncryptionTypes;
import com.aurora.webservice.ExemptionTypes;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals;
import com.ecs.ucm.ccla.data.AuroraClient;
import com.ecs.ucm.ccla.data.AuroraCorrespondent;
import com.ecs.ucm.ccla.data.Company;
import com.ecs.ucm.ccla.data.Contact;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.utils.DateUtils;
import com.ecs.ucm.ccla.utils.ObjectUtils;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Helper methods for dealing with Client objects required by Aurora web services.
 * 
 * @author Tom
 *
 */
public @Deprecated class AuroraClientUtils {
	
	public static final int MAX_CLIENTNAME_LENGTH = 80;
	
	/** Returns the corresponding Aurora client, null otherwise. Should be used to
	 *  check for presence of clients in Aurora.
	 *  
	 *  The single AuroraClient client/company mapping is extracted for the Organisation
	 *  first. If one doesn't exist, an error is thrown immediately.
	 *  
	 *  Providing the AuroraClient mapping exists, the client number is fed into the
	 *  getClient web service, along with the passed Company.
	 * 
	 * @param org
	 * @param facade
	 * @return
	 * @throws DataException 
	 * @throws ServiceException
	 * @throws DataException 
	 * @throws ServiceException 
	 */
	public static Client getClient(Entity org, Company company, FWFacade facade) 
	 throws DataException, ServiceException {
		
		AuroraClient clMap = Entity.getAuroraClientCompanyMapping
		 (org.getOrganisationId(), company, facade);
		
		if (clMap == null) {
			String msg = "No UCM Aurora Client Number set for " +
			 "Organisation ID " + org.getOrganisationId()+", Please check the Organisation Record.";
			Log.error(msg);
			throw new ServiceException(msg);
		}
		
		// Ensure Aurora client with this Number/Company doesn't already exist.
		Client existingClient = null;
		
		try {
			existingClient = 
			 AuroraWebServiceHandler.getAuroraWS().getClientByClientNumber
			 (company.getCode(), clMap.getClientNumber());
			
		} catch (Exception e) {
			// Assume error was thrown by Aurora, as Client did not exist.
			Log.debug("getClientByClientNumber - error: " + e.getMessage());
		}
		
		return existingClient;
	}
	
	/** Creates a new Client record in Aurora using the passed Organisation as input.
	 * 
	 *  The Organisation must have a Client Number mapped to it already (i.e. an
	 *  AuroraClient instance)
	 * 
	 * @param org
	 * @param facade
	 * @throws ServiceException 
	 */
	public static void addClient(Entity org, Company company, FWFacade facade) 
	 throws ServiceException {
		Log.debug("Adding new Aurora Client for Organisation ID " 
		 + org.getOrganisationId());
		
		try {
			// Ensure Aurora client with this Number/Company doesn't already exist.
			Client existingClient = getClient(org, company, facade);
			
			if (existingClient != null) {	
				throw new ServiceException("client record with Client Number: " + 
				existingClient.getClientNumber() + ", Company: " + company.getCode()
				 + " already exists");
			}
			
			// Generate Aurora Client directly from Org data
			Client newClient = getClientInstance(org, company, facade);

			if (newClient.getContributorCode()==0) {
				newClient.setContributorCode(newClient.getClientNumber());
			}
			
			Date dateOpened = DateUtils.getNow();
			newClient.setDateOpened(DateUtils.formatddsMMsyyyyspHHcmm(dateOpened));
			
			Log.debug("Creating Aurora Client: \n" + newClient.toString());
			int clientNumber = AuroraWebServiceHandler.getAuroraWS().createClient
			 (company.getCode(), newClient, false);

			//Set the enrolled date
			org.setDateEnrolled(dateOpened);
			org.persist(facade, Globals.Users.System);
			
			//Finally get the details and update our client
			existingClient = getClient(org, company, facade);

			//newClient = AuroraWebServiceHandler.getAuroraWS().getClientByClientNumber(company.getCode(), clientNumber);
			if (existingClient!=null) {
				updateAuroraMappingWithClientData(org, existingClient, company, facade);
			} else {
				throw new ServiceException("client record with Client Number: " + 
						clientNumber + ", Company: " + company.getCode()
						 + " has been added but not found in Aurora");
			}
			Log.debug("Successfully added new Aurora Client record for " +
			 "Organisation ID " + org.getOrganisationId());
			
			// TODO audit
			
		} catch (Exception e) {
			String msg = "Failed to add new Aurora client: " + e.getMessage();
			
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}
	
	/** Updates an existing Aurora client using the passed Organisation as input
	 * 
	 * @param org
	 * @param clMap
	 * @throws ServiceException 
	 * @throws RemoteException 
	 */
	public static void updateClient
	 (Entity org, Company company, FWFacade facade) throws ServiceException {
		
		Log.debug("Updating Aurora Client for Organisation ID " 
		 + org.getOrganisationId());
		
		try {
			// Fetch Aurora Client via web service call
			Client existingClient = getClient(org, company, facade);
			
			if (existingClient == null) {
				AuroraClient clMap = Entity.getAuroraClientCompanyMapping
				 (org.getOrganisationId(), company, facade);
				
				if (clMap == null) {
					String msg = "no Aurora Client Number set for " +
					 "Organisation ID " + org.getOrganisationId();
					Log.error(msg);
					throw new ServiceException(msg);
				}
				
				throw new ServiceException("no Aurora Client found with Client " +
				 "Number: " + clMap.getPaddedClientNumber() + ", Company: " + 
				company.getCode());
			}
			
			// Generate latest version of Aurora Client directly from Org data
			Client newClient = getClientInstance(org, company, facade);
			
			updateClientInstance(existingClient, newClient);
			
			Log.debug("Amending Aurora Client: \n" + newClient.toString());
			boolean response = AuroraWebServiceHandler.getAuroraWS().amendClient
			 (company.getCode(), existingClient);
			
			//finally update the aurora client mapping in the central database
			if (response) {
				newClient = getClient(org, company, facade);
				updateAuroraMappingWithClientData(org, newClient, company, facade);
			}
			
			Log.debug("Successfully updated Aurora Client for Organisation ID " 
			 + org.getOrganisationId());

		} catch (Exception e) {
			String msg = "Failed to update Aurora client: " + e.getMessage();
			
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}
	
	/** Generates a new Aurora Client instance from the passed Organisation. Does not
	 *  call any Aurora web services to actually create/update the Client record.
	 *  
	 * @param org must have Contact details pre-loaded
	 * @param facade
	 * @return
	 * @throws DataException 
	 * @throws ServiceException 
	 */
	public static Client getClientInstance(Entity org, Company company, FWFacade facade) 
	 throws DataException, ServiceException {
		
		// Find Aurora Client mapping for this Organisation
		AuroraClient clMap = Entity.getAuroraClientCompanyMapping
		 (org.getOrganisationId(), company, facade);
		
		if (clMap == null) {
			// Fail if no mapping
			throw new ServiceException("no Aurora Client preferences set for " +
			 "Company " + company.getCode());
		}
		
		Person corr = Entity.getNominatedCorrespondent
		 (org.getOrganisationId(), false, facade);
		
		// Fail if no correspondent set
		if (corr==null) {
			String msg = "no Correspondent or no nominated correspondent set";
			Log.error(msg);
			throw new ServiceException(msg);
		}
		
		Vector<AuroraCorrespondent> corrMap = 
		 AuroraCorrespondent.getCorrespondentsByPersonIdAndCompanyId
		 (corr.getPersonId(), company.getCompanyId(), facade);
		
		// Fail if correspondent doesn't have Aurora Correspondent mapping
		if (corrMap.isEmpty()) {
			String msg = "no Aurora Correspondent preferences set for " +
			 "organisation's Correspondent record. Ensure the Correspondent has " +
			 "been created in Aurora first.";
			Log.error(msg);
			throw new ServiceException(msg);
		}
		
		Integer defaultCorrId = corrMap.get(0).getCorrId();
		
		// Fail if correspondent link Code not set yet.
		if (defaultCorrId == null) {
			String msg = "Aurora Correspondent preferences for Company " 
			 + company.getCode() + " must be added to Aurora first";
			Log.error(msg);
			throw new ServiceException(msg);
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
		//special default setting.
		if (addr.getWebsiteAddress()==null) {
			addr.setWebsiteAddress("");
		}
		
		client.setAddress(addr);
		
		//Set the date opened
		client.setDateOpened(DateUtils.formatddsMMsyyyyspHHcmm(org.getDateEnrolled()));
		
		//Set these to standard defaults. They won't be checked/updated by future calls.
		client.setExemptionType(ExemptionTypes.OTHER);
		client.setExemptionNumber("TakeOn");
		
		client.setEncryptionMethod(EncryptionTypes.UNSET);
		
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
	
	/** Copies a selection of field values from the newClient instance to the client
	 *  one. Used when updating Aurora Client records.
	 *  
	 *  Expected that the client instance came from a direct getClient() Aurora web
	 *  service call, while newClient came from a local getClientInstance() call.
	 *  
	 * @param client
	 * @param newClient
	 */
	private static void updateClientInstance(Client client, Client newClient) {
		
		// Update the client name.
		client.setName(newClient.getName());
		
		// Update the address.
		client.setAddress(newClient.getAddress());
		
		// Update the default Correspondent ID.
		client.setDefaultCorrespondentNumber(newClient.getDefaultCorrespondentNumber());
	
		// Statement preferences
		client.setIncomeDistributionReportIndicator(newClient.isIncomeDistributionReportIndicator());
		client.setIncomeDistributionReportPaperIndicator(newClient.isIncomeDistributionReportPaperIndicator());
		client.setStatementMonthsAllIndicator(newClient.isStatementMonthsAllIndicator());
		client.setBulkDepositStatementsIndicator(newClient.isBulkDepositStatementsIndicator());
		client.setDepositAndWithdrawalBooksIndicator(newClient.isDepositAndWithdrawalBooksIndicator());
		client.setAutomaticStationaryOrderedIndicator(newClient.isAutomaticStationaryOrderedIndicator());
	
		client.setDataProtectionMailingGroupIndicator(newClient.isDataProtectionMailingGroupIndicator());
		
		// QRS
		client.setQuarterlyReportingClientIndicator(newClient.isQuarterlyReportingClientIndicator());
		client.setQuarterlyReportingManagerInitials(newClient.getQuarterlyReportingManagerInitials());
	
		// Statement Months
		client.setStatementMonths1(newClient.getStatementMonths1());
		client.setStatementMonths2(newClient.getStatementMonths2());
		client.setStatementMonths3(newClient.getStatementMonths3());
		client.setStatementMonths4(newClient.getStatementMonths4());
		
		// Marketing/Account Groups
		client.setMarketingGroup(newClient.getMarketingGroup());
		client.setMarketingSubGroup(newClient.getMarketingSubGroup());
		client.setAccountGroup(newClient.getAccountGroup());
	}
	
	/**
	 * Update CDB AuroraClientMapping based on the data from the client (in Aurora)
	 * @param org
	 * @param client
	 * @param facade
	 * @throws DataException
	 */
	private static void updateAuroraMappingWithClientData
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
	private static void copyAuroraClientDataToAuroraClientMapping
	 (AuroraClient auroraClient, Client client) {
		
		if (auroraClient==null || client==null)
			return;	
		
		auroraClient.setContributorCode(client.getContributorCode());
		auroraClient.setContributorTypeCode(client.getContributorTypeCode());
		auroraClient.setSubdivisionCode(client.getSubDivisionCode());
	}
	
	/**
	 * Sets the default values on the corespondent like empty strings etc..
	 * @param corr
	 */
	private static void setDefaultValues(Client client) {
		
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
		
		client.setAdditionalSignatoryName1("");
		client.setAdditionalSignatoryPostcode1("");
		client.setAdditionalSignatoryTelephone1("");

		client.setAdditionalSignatoryName2("");
		client.setAdditionalSignatoryPostcode2("");
		client.setAdditionalSignatoryTelephone2("");

		client.setAdditionalSignatoryName3("");
		client.setAdditionalSignatoryPostcode3("");
		client.setAdditionalSignatoryTelephone3("");

		client.setAdditionalSignatoryName4("");
		client.setAdditionalSignatoryPostcode4("");
		client.setAdditionalSignatoryTelephone4("");

		client.setAdditionalSignatoryName5("");
		client.setAdditionalSignatoryPostcode5("");
		client.setAdditionalSignatoryTelephone5("");

		client.setAdditionalSignatoryName6("");
		client.setAdditionalSignatoryPostcode6("");
		client.setAdditionalSignatoryTelephone6("");

		client.setAdditionalSignatoryName7("");
		client.setAdditionalSignatoryPostcode7("");
		client.setAdditionalSignatoryTelephone7("");

		client.setAdditionalSignatoryName8("");
		client.setAdditionalSignatoryPostcode8("");
		client.setAdditionalSignatoryTelephone8("");

		client.setAdditionalSignatoryName9("");
		client.setAdditionalSignatoryPostcode9("");
		client.setAdditionalSignatoryTelephone9("");

		client.setAdditionalSignatoryName10("");
		client.setAdditionalSignatoryPostcode10("");
		client.setAdditionalSignatoryTelephone10("");
		
		Vector<Contact> addrVec = new Vector<Contact>();
		client.setAuditorAddress(AuroraWebServiceUtils.getAddressInstance(addrVec));
	}
	
	/**
	 * 
	 * @param org
	 * @param company
	 * @param facade
	 * @return
	 * @throws DataException
	 * @throws ServiceException
	 */
	@Deprecated
	public static boolean isClientEquals
	 (Entity org, Company company, boolean debug, FWFacade facade) 
	 throws DataException, ServiceException {
		
		AuroraClientHandler handler = new AuroraClientHandler();
		
		Client cdbClient = handler.buildAuroraEntityInstance(org, company, facade);
		Client aurClient = handler.getExistingAuroraEntity(org, company, facade);
		
		Log.debug("Testing Aurora data equality for Org ID " 
		 + org.getOrganisationId() + ", Company: " + company.getCode());
				
		if (aurClient == null)
			throw new DataException("Unable to find corresponding Aurora Client for "+
			 "Org ID " + org.getOrganisationId() + ", Company: " + company.getCode());
		
		if (debug) {
			Log.debug("describing cdbClient");
			Map<String, Object> obj1 = ObjectUtils.describeData(cdbClient);
			
			Log.debug("describing aurClient");
			Map<String, Object> obj2 = ObjectUtils.describeData(aurClient);
			
			ObjectUtils.compare(obj1, obj2);
		}
			
		boolean equals = (
			cdbClient.getContributorTypeCode() == aurClient.getContributorTypeCode() &&
	        cdbClient.getSubDivisionCode() == aurClient.getSubDivisionCode() &&
	        cdbClient.getContributorCode() == aurClient.getContributorCode() &&
	        cdbClient.getClientNumber() == aurClient.getClientNumber() &&
            ((cdbClient.getName()==null && aurClient.getName()==null) || 
             (cdbClient.getName()!=null &&
              cdbClient.getName().equals(aurClient.getName()))) &&
              
            ((cdbClient.getAddress()==null && aurClient.getAddress()==null) || 
             (cdbClient.getAddress()!=null &&
              cdbClient.getAddress().equals(aurClient.getAddress()))) &&
            
            /*
            ((cdbClient.getExemptionType()==null && aurClient.getExemptionType()==null) || 
             (cdbClient.getExemptionType()!=null &&
              cdbClient.getExemptionType().equals(aurClient.getExemptionType()))) &&
              
            ((cdbClient.getExemptionNumber()==null && aurClient.getExemptionNumber()==null) || 
             (cdbClient.getExemptionNumber()!=null &&
              cdbClient.getExemptionNumber().equals(aurClient.getExemptionNumber()))) &&
			*/
             
            /*
            cdbClient.isIsSatisfactoryDocumentationPresent() == aurClient.isIsSatisfactoryDocumentationPresent() &&
            */
            cdbClient.getDefaultCorrespondentNumber() == aurClient.getDefaultCorrespondentNumber() &&
            
            /*
            cdbClient.getCorrespondentCodeBeneficialDefault() == aurClient.getCorrespondentCodeBeneficialDefault() &&
            cdbClient.isSubTotalIndicator() == aurClient.isSubTotalIndicator() &&
            */
            
            cdbClient.isIncomeDistributionReportIndicator() == aurClient.isIncomeDistributionReportIndicator() &&
            cdbClient.isIncomeDistributionReportPaperIndicator() == aurClient.isIncomeDistributionReportPaperIndicator() &&
           
            /*
            cdbClient.isIncomeDistributionReportDiskIndicator() == aurClient.isIncomeDistributionReportDiskIndicator() &&
            cdbClient.isIncomeDistributionReportEmailIndicator() == aurClient.isIncomeDistributionReportEmailIndicator() &&
            */
            
            cdbClient.getNumberOfStatementsDeposit() == aurClient.getNumberOfStatementsDeposit() &&
            cdbClient.getNumberOfStatementsUnitised() == aurClient.getNumberOfStatementsUnitised() &&
            
            cdbClient.getStatementMonths1() == aurClient.getStatementMonths1() &&
            cdbClient.getStatementMonths2() == aurClient.getStatementMonths2() &&
            cdbClient.getStatementMonths3() == aurClient.getStatementMonths3() &&
            cdbClient.getStatementMonths4() == aurClient.getStatementMonths4() &&
            cdbClient.isStatementMonthsAllIndicator() == aurClient.isStatementMonthsAllIndicator() &&
            
            /*
            cdbClient.isDepositStatementsIndicator() == aurClient.isDepositStatementsIndicator() &&
            cdbClient.isDepositStatementsPaperIndicator() == aurClient.isDepositStatementsPaperIndicator() &&
            cdbClient.isDepositStatementsDiskIndicator() == aurClient.isDepositStatementsDiskIndicator() &&
            cdbClient.isDepositStatementsEmailIndicator() == aurClient.isDepositStatementsEmailIndicator() &&
            cdbClient.isInvestmentStatementsPaperIndicator() == aurClient.isInvestmentStatementsPaperIndicator() &&
            cdbClient.isInvestmentStatementsDiskIndicator() == aurClient.isInvestmentStatementsDiskIndicator() &&
            cdbClient.isInvestmentStatementsEmailIndicator() == aurClient.isInvestmentStatementsEmailIndicator() &&
            */
            
            cdbClient.isBulkDepositStatementsIndicator() == aurClient.isBulkDepositStatementsIndicator() &&
            cdbClient.isDepositAndWithdrawalBooksIndicator() == aurClient.isDepositAndWithdrawalBooksIndicator() &&

// 				Don't compare this field is controlled by Aurora
//	            
//	            ((cdbClient.getLastStatementProduced()==null && aurClient.getLastStatementProduced()==null) || 
//	             (cdbClient.getLastStatementProduced()!=null &&
//	              cdbClient.getLastStatementProduced().equals(aurClient.getLastStatementProduced()))) &&
            
            cdbClient.isAutomaticStationaryOrderedIndicator() == aurClient.isAutomaticStationaryOrderedIndicator() &&
            
            /*
            ((cdbClient.getEncryptionMethod()==null && aurClient.getEncryptionMethod()==null) || 
             (cdbClient.getEncryptionMethod()!=null &&
              cdbClient.getEncryptionMethod().equals(aurClient.getEncryptionMethod()))) &&
            */
            
            /*
            cdbClient.isExternalUserAccessAllowedIndicator() == aurClient.isExternalUserAccessAllowedIndicator() &&
            */
            
            cdbClient.isQuarterlyReportingClientIndicator() == aurClient.isQuarterlyReportingClientIndicator() &&
            
            CCLAUtils.stringEquals(
             cdbClient.getQuarterlyReportingManagerInitials(), 
             cdbClient.getQuarterlyReportingManagerInitials()
            ) &&
            
            cdbClient.getMarketingGroup() == aurClient.getMarketingGroup() &&
            cdbClient.getMarketingSubGroup() == aurClient.getMarketingSubGroup() &&
            cdbClient.getAccountGroup() == aurClient.getAccountGroup()
            
            /*
            cdbClient.isQuarterlyUpdateIndicator() == aurClient.isQuarterlyUpdateIndicator()
            
           
            */
            
            /*
            cdbClient.isAuditCertificateSendIndicator() == aurClient.isAuditCertificateSendIndicator() &&
            cdbClient.isAuditCertificatePrintIndicator() == aurClient.isAuditCertificatePrintIndicator() &&
            cdbClient.getAuditCertificateDueDay() == aurClient.getAuditCertificateDueDay() &&
            cdbClient.getAuditCertificateDueMonth() == aurClient.getAuditCertificateDueMonth() &&
            cdbClient.isAuditCertificateClientAuthority() == aurClient.isAuditCertificateClientAuthority() &&
            
            ((cdbClient.getAuditCertificateDestination()==null && aurClient.getAuditCertificateDestination()==null) || 
             (cdbClient.getAuditCertificateDestination()!=null &&
              cdbClient.getAuditCertificateDestination().equals(aurClient.getAuditCertificateDestination()))) 
            */
              /*
            ((cdbClient.getAuditPriceType()==null && aurClient.getAuditPriceType()==null) || 
             (cdbClient.getAuditPriceType()!=null &&
              cdbClient.getAuditPriceType().equals(aurClient.getAuditPriceType()))) &&
              */
              
              /*
            ((cdbClient.getAuthorityHeldDate()==null && aurClient.getAuthorityHeldDate()==null) || 
             (cdbClient.getAuthorityHeldDate()!=null &&
              cdbClient.getAuthorityHeldDate().equals(aurClient.getAuthorityHeldDate()))) &&
              
            ((cdbClient.getAuditorReference()==null && aurClient.getAuditorReference()==null) || 
             (cdbClient.getAuditorReference()!=null &&
              cdbClient.getAuditorReference().equals(aurClient.getAuditorReference()))) &&
              
            ((cdbClient.getAuditorAddress()==null && aurClient.getAuditorAddress()==null) || 
             (cdbClient.getAuditorAddress()!=null &&
              cdbClient.getAuditorAddress().equals(aurClient.getAuditorAddress()))) &&
             
             
            cdbClient.isMultipleSignatoryIndicator() == aurClient.isMultipleSignatoryIndicator() &&
            
              
              
            ((cdbClient.getMultipleSignatoryInformation()==null && aurClient.getMultipleSignatoryInformation()==null) || 
             (cdbClient.getMultipleSignatoryInformation()!=null &&
              cdbClient.getMultipleSignatoryInformation().equals(aurClient.getMultipleSignatoryInformation()))) &&
              
              
            ((cdbClient.getAdditionalSignatoryName1()==null && aurClient.getAdditionalSignatoryName1()==null) || 
             (cdbClient.getAdditionalSignatoryName1()!=null &&
              cdbClient.getAdditionalSignatoryName1().equals(aurClient.getAdditionalSignatoryName1()))) &&
              
            ((cdbClient.getAdditionalSignatoryTelephone1()==null && aurClient.getAdditionalSignatoryTelephone1()==null) || 
             (cdbClient.getAdditionalSignatoryTelephone1()!=null &&
              cdbClient.getAdditionalSignatoryTelephone1().equals(aurClient.getAdditionalSignatoryTelephone1()))) &&
              
            ((cdbClient.getAdditionalSignatoryPostcode1()==null && aurClient.getAdditionalSignatoryPostcode1()==null) || 
             (cdbClient.getAdditionalSignatoryPostcode1()!=null &&
              cdbClient.getAdditionalSignatoryPostcode1().equals(aurClient.getAdditionalSignatoryPostcode1()))) &&
              
            ((cdbClient.getAdditionalSignatoryName2()==null && aurClient.getAdditionalSignatoryName2()==null) || 
             (cdbClient.getAdditionalSignatoryName2()!=null &&
              cdbClient.getAdditionalSignatoryName2().equals(aurClient.getAdditionalSignatoryName2()))) &&
              
            ((cdbClient.getAdditionalSignatoryTelephone2()==null && aurClient.getAdditionalSignatoryTelephone2()==null) || 
             (cdbClient.getAdditionalSignatoryTelephone2()!=null &&
              cdbClient.getAdditionalSignatoryTelephone2().equals(aurClient.getAdditionalSignatoryTelephone2()))) &&
              
            ((cdbClient.getAdditionalSignatoryPostcode2()==null && aurClient.getAdditionalSignatoryPostcode2()==null) || 
             (cdbClient.getAdditionalSignatoryPostcode2()!=null &&
              cdbClient.getAdditionalSignatoryPostcode2().equals(aurClient.getAdditionalSignatoryPostcode2()))) &&
              
            ((cdbClient.getAdditionalSignatoryName3()==null && aurClient.getAdditionalSignatoryName3()==null) || 
             (cdbClient.getAdditionalSignatoryName3()!=null &&
              cdbClient.getAdditionalSignatoryName3().equals(aurClient.getAdditionalSignatoryName3()))) &&
              
            ((cdbClient.getAdditionalSignatoryTelephone3()==null && aurClient.getAdditionalSignatoryTelephone3()==null) || 
             (cdbClient.getAdditionalSignatoryTelephone3()!=null &&
              cdbClient.getAdditionalSignatoryTelephone3().equals(aurClient.getAdditionalSignatoryTelephone3()))) &&
              
            ((cdbClient.getAdditionalSignatoryPostcode3()==null && aurClient.getAdditionalSignatoryPostcode3()==null) || 
             (cdbClient.getAdditionalSignatoryPostcode3()!=null &&
              cdbClient.getAdditionalSignatoryPostcode3().equals(aurClient.getAdditionalSignatoryPostcode3()))) &&
              
            ((cdbClient.getAdditionalSignatoryName4()==null && aurClient.getAdditionalSignatoryName4()==null) || 
             (cdbClient.getAdditionalSignatoryName4()!=null &&
              cdbClient.getAdditionalSignatoryName4().equals(aurClient.getAdditionalSignatoryName4()))) &&
            
            ((cdbClient.getAdditionalSignatoryTelephone4()==null && aurClient.getAdditionalSignatoryTelephone4()==null) || 
             (cdbClient.getAdditionalSignatoryTelephone4()!=null &&
              cdbClient.getAdditionalSignatoryTelephone4().equals(aurClient.getAdditionalSignatoryTelephone4()))) &&
              
            ((cdbClient.getAdditionalSignatoryPostcode4()==null && aurClient.getAdditionalSignatoryPostcode4()==null) || 
             (cdbClient.getAdditionalSignatoryPostcode4()!=null &&
              cdbClient.getAdditionalSignatoryPostcode4().equals(aurClient.getAdditionalSignatoryPostcode4()))) &&
              
            ((cdbClient.getAdditionalSignatoryName5()==null && aurClient.getAdditionalSignatoryName5()==null) || 
   	             (cdbClient.getAdditionalSignatoryName5()!=null &&
   	              cdbClient.getAdditionalSignatoryName5().equals(aurClient.getAdditionalSignatoryName5()))) &&
   	            
   	        ((cdbClient.getAdditionalSignatoryTelephone5()==null && aurClient.getAdditionalSignatoryTelephone5()==null) || 
   	             (cdbClient.getAdditionalSignatoryTelephone5()!=null &&
   	              cdbClient.getAdditionalSignatoryTelephone5().equals(aurClient.getAdditionalSignatoryTelephone5()))) &&
   	              
   	        ((cdbClient.getAdditionalSignatoryPostcode5()==null && aurClient.getAdditionalSignatoryPostcode5()==null) || 
   	             (cdbClient.getAdditionalSignatoryPostcode5()!=null &&
   	              cdbClient.getAdditionalSignatoryPostcode5().equals(aurClient.getAdditionalSignatoryPostcode5()))) &&
          
            ((cdbClient.getAdditionalSignatoryName6()==null && aurClient.getAdditionalSignatoryName6()==null) || 
	   	             (cdbClient.getAdditionalSignatoryName6()!=null &&
	   	              cdbClient.getAdditionalSignatoryName6().equals(aurClient.getAdditionalSignatoryName6()))) &&
	   	            
	   	    ((cdbClient.getAdditionalSignatoryTelephone6()==null && aurClient.getAdditionalSignatoryTelephone6()==null) || 
	   	             (cdbClient.getAdditionalSignatoryTelephone6()!=null &&
	   	              cdbClient.getAdditionalSignatoryTelephone6().equals(aurClient.getAdditionalSignatoryTelephone6()))) &&
	   	              
	   	    ((cdbClient.getAdditionalSignatoryPostcode6()==null && aurClient.getAdditionalSignatoryPostcode6()==null) || 
	   	             (cdbClient.getAdditionalSignatoryPostcode6()!=null &&
	   	              cdbClient.getAdditionalSignatoryPostcode6().equals(aurClient.getAdditionalSignatoryPostcode6()))) &&
              
            ((cdbClient.getAdditionalSignatoryName7()==null && aurClient.getAdditionalSignatoryName7()==null) || 
	   	             (cdbClient.getAdditionalSignatoryName7()!=null &&
	   	              cdbClient.getAdditionalSignatoryName7().equals(aurClient.getAdditionalSignatoryName7()))) &&
	   	            
	   	    ((cdbClient.getAdditionalSignatoryTelephone7()==null && aurClient.getAdditionalSignatoryTelephone7()==null) || 
	   	             (cdbClient.getAdditionalSignatoryTelephone7()!=null &&
	   	              cdbClient.getAdditionalSignatoryTelephone7().equals(aurClient.getAdditionalSignatoryTelephone7()))) &&
	   	              
	   	    ((cdbClient.getAdditionalSignatoryPostcode7()==null && aurClient.getAdditionalSignatoryPostcode7()==null) || 
	   	             (cdbClient.getAdditionalSignatoryPostcode7()!=null &&
	   	              cdbClient.getAdditionalSignatoryPostcode7().equals(aurClient.getAdditionalSignatoryPostcode7()))) &&
              
            ((cdbClient.getAdditionalSignatoryName8()==null && aurClient.getAdditionalSignatoryName8()==null) || 
	   	             (cdbClient.getAdditionalSignatoryName8()!=null &&
	   	              cdbClient.getAdditionalSignatoryName8().equals(aurClient.getAdditionalSignatoryName8()))) &&
	   	            
	   	    ((cdbClient.getAdditionalSignatoryTelephone8()==null && aurClient.getAdditionalSignatoryTelephone8()==null) || 
	   	             (cdbClient.getAdditionalSignatoryTelephone8()!=null &&
	   	              cdbClient.getAdditionalSignatoryTelephone8().equals(aurClient.getAdditionalSignatoryTelephone8()))) &&
	   	              
	   	    ((cdbClient.getAdditionalSignatoryPostcode8()==null && aurClient.getAdditionalSignatoryPostcode8()==null) || 
	   	             (cdbClient.getAdditionalSignatoryPostcode8()!=null &&
	   	              cdbClient.getAdditionalSignatoryPostcode8().equals(aurClient.getAdditionalSignatoryPostcode8()))) &&
              
            ((cdbClient.getAdditionalSignatoryName9()==null && aurClient.getAdditionalSignatoryName9()==null) || 
	   	             (cdbClient.getAdditionalSignatoryName9()!=null &&
	   	              cdbClient.getAdditionalSignatoryName9().equals(aurClient.getAdditionalSignatoryName9()))) &&
	   	            
	   	    ((cdbClient.getAdditionalSignatoryTelephone9()==null && aurClient.getAdditionalSignatoryTelephone9()==null) || 
	   	             (cdbClient.getAdditionalSignatoryTelephone9()!=null &&
	   	              cdbClient.getAdditionalSignatoryTelephone9().equals(aurClient.getAdditionalSignatoryTelephone9()))) &&
	   	              
	   	    ((cdbClient.getAdditionalSignatoryPostcode9()==null && aurClient.getAdditionalSignatoryPostcode9()==null) || 
	   	             (cdbClient.getAdditionalSignatoryPostcode9()!=null &&
	   	              cdbClient.getAdditionalSignatoryPostcode9().equals(aurClient.getAdditionalSignatoryPostcode9()))) &&
              				   	              
	        ((cdbClient.getAdditionalSignatoryName10()==null && aurClient.getAdditionalSignatoryName10()==null) || 
	   	             (cdbClient.getAdditionalSignatoryName10()!=null &&
	   	              cdbClient.getAdditionalSignatoryName10().equals(aurClient.getAdditionalSignatoryName10()))) &&
	   	            
	   	    ((cdbClient.getAdditionalSignatoryTelephone10()==null && aurClient.getAdditionalSignatoryTelephone10()==null) || 
	   	             (cdbClient.getAdditionalSignatoryTelephone10()!=null &&
	   	              cdbClient.getAdditionalSignatoryTelephone10().equals(aurClient.getAdditionalSignatoryTelephone10()))) &&
	   	              
	   	    ((cdbClient.getAdditionalSignatoryPostcode10()==null && aurClient.getAdditionalSignatoryPostcode10()==null) || 
	   	             (cdbClient.getAdditionalSignatoryPostcode10()!=null &&
	   	              cdbClient.getAdditionalSignatoryPostcode10().equals(aurClient.getAdditionalSignatoryPostcode10()))) &&

            cdbClient.isDataProtectionMailingGroupIndicator() == aurClient.isDataProtectionMailingGroupIndicator()
			*/
		
//				Don't Compare this field for now.				
//	            ((cdbClient.getDateOpened()==null && aurClient.getDateOpened()==null) || 
//	             (cdbClient.getDateOpened()!=null &&
//	              cdbClient.getDateOpened().equals(aurClient.getDateOpened()))) &&
            
            /*  
            ((cdbClient.getIVSCFID()==null && aurClient.getIVSCFID()==null) || 
             (cdbClient.getIVSCFID()!=null &&
              cdbClient.getIVSCFID().equals(aurClient.getIVSCFID()))) &&
              
            ((cdbClient.getIVSReferenceNumber()==null && aurClient.getIVSReferenceNumber()==null) || 
             (cdbClient.getIVSReferenceNumber()!=null &&
              cdbClient.getIVSReferenceNumber().equals(aurClient.getIVSReferenceNumber())))
			*/
		);
		
		Log.debug("Client data equal? " + equals);
		return equals;
	}
}
