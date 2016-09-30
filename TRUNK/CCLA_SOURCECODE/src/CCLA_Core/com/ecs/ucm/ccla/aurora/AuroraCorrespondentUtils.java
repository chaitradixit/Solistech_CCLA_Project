package com.ecs.ucm.ccla.aurora;

import intradoc.common.ServiceException;
import intradoc.data.DataException;
import intradoc.shared.SharedObjects;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import com.aurora.webservice.Correspondent;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.Globals;
import com.ecs.ucm.ccla.aurora.compare.AuroraEntityComparator;
import com.ecs.ucm.ccla.aurora.compare.AuroraEntityComparisonOutcome;
import com.ecs.ucm.ccla.aurora.compare.CorrespondentFieldSet;
import com.ecs.ucm.ccla.aurora.compare.FieldGroup;
import com.ecs.ucm.ccla.aurora.compare.FieldGroupAmendmentDates;
import com.ecs.ucm.ccla.aurora.compare.FieldGroupConfig;
import com.ecs.ucm.ccla.data.AuroraCorrespondent;
import com.ecs.ucm.ccla.data.Company;
import com.ecs.ucm.ccla.data.Contact;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.audit.SDAudit;
import com.ecs.ucm.ccla.data.audit.SDAuditData;
import com.ecs.ucm.ccla.utils.ObjectUtils;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

/** 
 *  @Deprecated in favour of AuroraCorrespondentHandler
 *  
 *  Recommended for removal in future release
 *  
 * @author tm
 *
 */
public @Deprecated class AuroraCorrespondentUtils {
	
	// Determine whether new Correspondent Codes are generated by Aurora, when new
	// Correspondent records are added. If not, the codes are generated by a sequence 
	// in the Central DB and fed to Aurora.
	private static final boolean AURORA_GENERATED_CORR_IDS = !StringUtils.stringIsBlank(
	 SharedObjects.getEnvironmentValue("AURORA_AutoGenerateCorrCodes"));
	
	//Determine whether to use the abbreviated fullname 
	//i.e. Title (if exist) + first initial of FirstName + first initial of MiddleName 
	//(if exist) + LastName 
	//If this flag is not set, the full name is used: 
	//i.e. Title (if exist) + FirstName + MiddleName + LastName 
	private static final boolean AURORA_USE_ABBREVIATED_FULL_NAME = 
	 !StringUtils.stringIsBlank(
	 SharedObjects.getEnvironmentValue("AURORA_UseAbbreviatedFullName"));
	
	/* Determines whether multiple Aurora Correspondent maps for a single Person will
	 * share the same Correspondent Code.
	 * 
	 * If false, Correspondent Codes will always be uniquely generated, even for the
	 * same Person record.
	 * 
	 */
	private static final boolean PERSON_SINGLE_CORR_ID = 
	 !StringUtils.stringIsBlank(
	 SharedObjects.getEnvironmentValue("AURORA_PersonSingleCorrCode"));
	
	/** Returns the corresponding Aurora correspondent, null otherwise. Should be used 
	 *  to check for presence of correspondents in Aurora.
	 *  
	 *  The single AuroraCorrespondent correspondent/company mapping is extracted for 
	 *  the Person first. If one doesn't exist, an error is thrown immediately.
	 *  
	 *  Providing the AuroraCorrespondent mapping exists, the corr number is fed into 
	 *  the getCorrespondent web service, along with the passed Company.
	 * @throws DataException 
	 */
	public static Correspondent getCorrespondent
	 (Person person, Company company, FWFacade facade) 
	 throws ServiceException, DataException {
		
		AuroraCorrespondent corrMap = null;
		Correspondent existingCorrespondent = null;
		
		Vector<AuroraCorrespondent> corrsMap = AuroraCorrespondent.
		 getCorrespondentsByPersonId(person.getPersonId(), facade);
		
		if (corrsMap.isEmpty()) {
			// No mapped Correspondent Numbers for this Person
			String msg = "no mapped Correspondent Number for Person ID " + 
			 person.getPersonId();
			
			Log.debug(msg);
			return null;
		}
		
		// Try and find a Correspondent mapping for the required Company.
		corrMap = AuroraCorrespondent.getByCompany(corrsMap, company);
		
		if (corrMap == null) {
			// Add new Correspondent mapping.
			Log.debug("No existing Aurora Correspondent mapping for Person ID " + 
			 person.getPersonId() + " to Company: " + company.getCode());
			
			// TODO add new mapping entry. Re-use existing corr ID?
			return null;
		}
		
		try {
			existingCorrespondent = AuroraWebServiceHandler.getAuroraWS().
			 getCorrespondentByCorrespondentCode
			 (company.getCode(), corrMap.getCorrId());
			
		} catch (Exception e) {
			// Assume error was thrown by Aurora, as Correspondent did not exist.
			Log.debug("getCorrespondentByCorrespondentCode - error: " 
			 + e.getMessage());
		}
		
		return existingCorrespondent;
	}
	
	public static void addCorrespondent
	 (Person person, Company company, FWFacade facade) throws ServiceException {
		
		Log.debug("Adding new Aurora Correspondent for Person ID " 
		 + person.getPersonId());
		
		try {
			
			if (person.getContacts()==null || person.getContacts().isEmpty()) {
				throw new ServiceException("Cannot Add Correspondent, no contact information set for person with ID : "+person.getPersonId());
			}
			
			AuroraCorrespondent corrMap = null;
			
			Vector<AuroraCorrespondent> corrsMap = AuroraCorrespondent.
			 getCorrespondentsByPersonId(person.getPersonId(), facade);
			
			// Try and find a Correspondent mapping for the required Company.
			corrMap = AuroraCorrespondent.getByCompany(corrsMap, company);
			
			if (corrMap == null) {
				String msg = "No UCM Aurora Correspondent mapping for Person ID " + 
				 person.getPersonId() + " to Company: " + company.getCode()+", Please Check the Person Record.";
				
				Log.error(msg);
				throw new ServiceException(msg);
			}
			
			// Ensure Aurora Correspondent with this Number/Company doesn't
			// already exist.
			Correspondent existingCorrespondent = 
			 getCorrespondent(person, company, facade);
			
			if (existingCorrespondent != null) {
				throw new ServiceException("correspondent record with Correspondent " +
				 "Number: " + corrMap.getCorrId() + ", Company: " + company.getCode()
				 + " already exists");
			}
			
			Correspondent newCorr = getCorrespondentInstance(person, company, facade);
			Integer newCorrId = corrMap.getCorrId();
			
			// If the Person-Aurora Mapping doesn't have a Corr ID assigned yet, and
			// the Central DB is responsible for generating the Corr IDs, assign one
			// now.
			if (newCorrId == null && !AURORA_GENERATED_CORR_IDS) {
				
				if (PERSON_SINGLE_CORR_ID) {
					// Check existing correspondent maps for this Person, for a non-null 
					// Corr ID. This can be recycled.
					for (AuroraCorrespondent corr : corrsMap) {
						if (corr.getCorrId() != null) {
							newCorrId = corr.getCorrId();
						}
					}
				}
				
				if (newCorrId == null) {
					// No existing Correspondent ID. Use the Person's PERSON_ID/
					// ELEMENT_ID instead.
					newCorrId = person.getPersonId();
					
					Log.debug("Using person's existing Correspondent Code: " +
					 newCorrId);
					
					// Fetch unique Corr ID from DB sequence
					// Don't use this sequence any more!
					//newCorrId = AuroraCorrespondent.getNewCorrespondentNumber(facade);
				} else {
					Log.debug("Using person's PERSON_ID as the Correspondent Code: " +
					 newCorrId);
				}
				
				// Set against Aurora Correspondent instance
				newCorr.setCorrespondentCode(newCorrId);
			}
			
			//Must set
			if (newCorr.getReportUsage()==0) {
				newCorr.setReportUsage(1);
			}
			
			validateCorrespondent(newCorr);
			
			//Use Aurora Webservice to add the correspondent.
			Log.debug("Creating Aurora Correspondent: \n" + newCorr.toString());
			newCorrId = AuroraWebServiceHandler.getAuroraWS().createCorrespondent
			 (company.getCode(), newCorr, AURORA_GENERATED_CORR_IDS);
			
			// Set against Person-Aurora Mapping
			corrMap.setCorrId(newCorrId);
			
			// Persist changes to Person-Aurora Mapping
			corrMap.persist(facade, Globals.Users.System);				
			
			Log.debug("Successfully added new Aurora Correspondent record for " +
			 "Person ID " + person.getPersonId());
			
			// TODO audit
			
		} catch (Exception e) {
			String msg = "Failed to add new Aurora correspondent: " + e.getMessage();
			
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}
	
	public static void updateCorrespondent
	 (Person person, Company company, FWFacade facade) throws ServiceException {
		
		Log.debug("Updating Aurora Correspondent for Person ID " 
		 + person.getPersonId());
		
		try {
			// Ensure Aurora Correspondent with this Number/Company already exists
			Correspondent existingCorr = getCorrespondent(person, company, facade);
				
			if (existingCorr == null) {
				Vector<AuroraCorrespondent> corrsMap = AuroraCorrespondent.
				 getCorrespondentsByPersonId(person.getPersonId(), facade);
				
				throw new ServiceException("correspondent record with Correspondent " +
				 "Number: " + corrsMap.get(0).getCorrId() + ", Company: " + 
				 company.getCode() + " not found");
			}
			
			// Generate Aurora Correspondent directly from Person data
			Correspondent newCorr = getCorrespondentInstance(person, company, facade);
			
			updateCorrespondentInstance(existingCorr, newCorr);

			validateCorrespondent(existingCorr);
			
			Log.debug("Amending Aurora Correspondent: \n" + existingCorr.toString());
			AuroraWebServiceHandler.getAuroraWS().amendCorrespondent
			 (company.getCode(), existingCorr);
			
			Log.debug("Successfully updated Aurora Correspondent record for " +
			 "Person ID " + person.getPersonId() + ", Company: " + company.getCode());
			
			// TODO audit
			
		} catch (Exception e) {
			String msg = "Failed to update Aurora correspondent: " + e.getMessage();
			
			Log.error(msg, e);
			throw new ServiceException(msg, e);
		}
	}
	
	/** Final validation checks before a Correspondent instance is dispatched to Aurora
	 *  for creation/update.
	 *  
	 * @param corr
	 * @throws ServiceException 
	 */
	private static void validateCorrespondent(Correspondent corr) 
	 throws ServiceException {
		
		// Address check. All correspondents must have an address set!
		if (corr.getAddress() == null
			||
			corr.getAddress().getPostCode() == null) {
			throw new ServiceException("no default address set");
		}
	}

	/** Generates a new Aurora Correspondent instance, suitable to use with Aurora
	 *  web service calls.
	 *  
	 *  Doesn't actually create/update any Aurora records.
	 *  
	 * @param person
	 * @param company
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public static Correspondent getCorrespondentInstance
	 (Person person, Company company, FWFacade facade) throws DataException {
		
		AuroraCorrespondent corrMap = null;
		// Now build the new correspondent instance

		Correspondent corr = new Correspondent();	
		setDefaultValues(corr);
		
		// First determine whether a new Correspondent mapping needs to be added.
		Vector<AuroraCorrespondent> corrsMap = AuroraCorrespondent.
		 getCorrespondentsByPersonId(person.getPersonId(), facade);
		
		// Try and find a Correspondent mapping for the required Company.
		corrMap = AuroraCorrespondent.getByCompany(corrsMap, company);

		if (corrMap == null) {
			// Add new Correspondent mapping.
			String msg = "no existing Aurora Correspondent preferences for Company: " 
		     + company.getCode();
			
			throw new DataException(msg);
		}
		
		if (corrMap.getCorrId() != null)
			corr.setCorrespondentCode(corrMap.getCorrId());
		
		// Check if the Person record has had name data set correctly.
		// If 'Last Name' isn't explicitly set, this could be a legacy record which
		// hasn't been updated properly.
		if (StringUtils.stringIsBlank(person.getLastName())) {
			// Used to throw an exception here, not any more! Just post to Aurora the
			// full name field as it stands.
			//throw new DataException("Correspondent Name fields not set correctly");
			corr.setName(person.getFullName());
		} else {
			corr.setName(
			 AURORA_USE_ABBREVIATED_FULL_NAME ?
			 person.getAbbrevatedFullName():person.getFullName());
		}
		
		corr.setSalutation(person.getSalutation());
		corr.setAddress(AuroraWebServiceUtils.getAddressInstance(person.getContacts()));
		corr.setReportAndAccountsEmailIndicator(corrMap.isReportEmailIndicator());
		corr.setReportAndAccountsMailIndicator(corrMap.isReportMailIndicator());
		corr.setReportUsage
		 (corrMap.getReportUsage() == null ? 1 : corrMap.getReportUsage());

		return corr;
	}
	
	/* Copies a selection of field values from the newCorr instance to the corr
	 *  one.
	 *  
	 *  Expected that the client instance came from a direct getCorrespondent() 
	 *  Aurora web service call, while newCorr came from a local 
	 *  getCorrespondentInstance() call.
	 */  
	private static void updateCorrespondentInstance
	 (Correspondent corr, Correspondent newCorr) {
		
		corr.setName(newCorr.getName());
		corr.setSalutation(newCorr.getSalutation());
		corr.setAddress(newCorr.getAddress());
		corr.setReportAndAccountsEmailIndicator(newCorr.isReportAndAccountsEmailIndicator());
		corr.setReportAndAccountsMailIndicator(newCorr.isReportAndAccountsMailIndicator());
		corr.setReportUsage(newCorr.getReportUsage());
	}
	
	/**
	 * Sets the default values on the corespondent like empty strings etc..
	 * @param corr
	 */
	private static void setDefaultValues(Correspondent corr) {
		corr.setTypeCode("");
	}
	
	/** Test method for fetching a correspondent from Aurora.
	 *  
	 *  Used for testing web service authentication in isolation.
	 *  
	 * @param company
	 * @param corrId
	 * @return
	 * @throws ServiceException
	 */
	public static Correspondent getCorrespondent(String company, int corrId) 
	 throws ServiceException {
		
		try {
			Correspondent auroraCorrespondent =  AuroraWebServiceHandler.getAuroraWS()
			 .getCorrespondentByCorrespondentCode(company, corrId);
			
			Log.debug("Found Aurora Correspondent with ID: + " + corrId + 
			 ", company: " + company + "(" + auroraCorrespondent.toString());
			
			return auroraCorrespondent;
			
		} catch (Exception e) {
			String msg = "Failed to fetch Aurora Correspondent: " + e.getMessage();
			Log.error(msg, e);
			
			throw new ServiceException(msg, e);
		}
	}
	
	/**
	 * 
	 * @param person
	 * @param company
	 * @param facade
	 * @return
	 * @throws DataException
	 * @throws ServiceException
	 */
	public static boolean isCorrespondentEquals
	 (Person person, Company company, boolean debug, FWFacade facade) 
	 throws DataException, ServiceException {
		
		AuroraCorrespondentHandler corrHandler = new AuroraCorrespondentHandler();
		
		Correspondent cdbCorr = corrHandler.buildAuroraEntityInstance
		 (person, company, facade);
		
		Correspondent aurCorr = corrHandler.getExistingAuroraEntity
		 (person, company, facade);
		
		Log.debug("Testing Aurora data equality for Person ID " 
		 + person.getPersonId() + ", Company: " + company.getCode());
				
		if (aurCorr == null)
			throw new DataException("Unable to find corresponding Aurora Correspondent for "+
			 "Person ID " + person.getPersonId() + ", Company: " + company.getCode());
		
		if (debug) {
			Log.debug("describing cdbCorr");
			ObjectUtils.describeData(cdbCorr);
			
			Log.debug("describing aurCorr");
			ObjectUtils.describeData(aurCorr);
		}
		
		boolean equals = (
			(cdbCorr.getCorrespondentCode() == aurCorr.getCorrespondentCode()) &&
	
			((cdbCorr.getTypeCode()==null && aurCorr.getTypeCode()==null) || 
					(cdbCorr.getTypeCode()!=null &&
							cdbCorr.getTypeCode().equals(aurCorr.getTypeCode()))) &&
	
			((cdbCorr.getName()==null && aurCorr.getName()==null) || 
					(cdbCorr.getName()!=null &&
							cdbCorr.getName().equals(aurCorr.getName()))) &&
	
			
							
			((cdbCorr.getSalutation()==null && aurCorr.getSalutation()==null) || 
					(cdbCorr.getSalutation()!=null && 
							cdbCorr.getSalutation().equals(aurCorr.getSalutation()))) &&
	
			((cdbCorr.getAddress()==null && aurCorr.getAddress()==null) || 
					(cdbCorr.getAddress()!=null &&
							cdbCorr.getAddress().equals(aurCorr.getAddress()))) &&
	
			cdbCorr.getReportUsage() == aurCorr.getReportUsage() &&
			cdbCorr.isReportAndAccountsMailIndicator() == aurCorr.isReportAndAccountsMailIndicator() &&
			cdbCorr.isReportAndAccountsEmailIndicator() == aurCorr.isReportAndAccountsEmailIndicator()
		);
		
		Log.debug("Person data equal? " + equals);
		return equals;
	}
}
