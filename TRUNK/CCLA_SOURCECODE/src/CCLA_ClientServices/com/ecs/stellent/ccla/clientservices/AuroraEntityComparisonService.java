package com.ecs.stellent.ccla.clientservices;

import java.util.Vector;

import intradoc.common.ServiceException;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;
import intradoc.server.Service;

import com.aurora.webservice.Client;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.aurora.AuroraAccountHandler;
import com.ecs.ucm.ccla.aurora.AuroraClientHandler;
import com.ecs.ucm.ccla.aurora.AuroraCorrespondentHandler;
import com.ecs.ucm.ccla.aurora.AuroraSignatories;
import com.ecs.ucm.ccla.aurora.CandidatePersonSignatory;
import com.ecs.ucm.ccla.aurora.compare.AuroraEntityComparisonOutcome.CompanyComparisonOutcome;
import com.ecs.ucm.ccla.aurora.compare.AuroraEntitySource;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.AuroraClient;
import com.ecs.ucm.ccla.data.Company;
import com.ecs.ucm.ccla.data.Element;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.Relation;
import com.ecs.ucm.ccla.data.RelationName;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Service methods for running Central DB-Aurora comparison checks and returning their
 *  results.
 *  
 *  Also contains services for import of signatories from Aurora.
 *  
 * @author tm
 *
 */
public class AuroraEntityComparisonService extends Service {
	
	/**
	 *  Populates the binder with summary Aurora comparison data, associated with the
	 *  ELEMENT_ID in the binder.
	 *  
	 *  The ELEMENT_ID value must correspond to a Person, Org or Account ID.
	 * 
	 * @throws DataException  
	 * @throws ServiceException 
	 */
	public void getComparisonSummary() 
	 throws DataException, ServiceException {
		
		FWFacade facade = CCLAUtils.getFacade(true);

		Integer elementId = CCLAUtils.getBinderIntegerValue(m_binder, Element.Cols.ID);
		Element elem = Element.get(elementId, facade);
		
		if (elem == null) {
			throw new ServiceException("Invalid Element ID: " + elementId);
		}
		
		DataResultSet rsOutcomes = null;
		
		if (elem.getType().equals(ElementType.PERSON)) {
			Person cdbEntity = Person.get(elementId, true, facade);
			
			AuroraCorrespondentHandler handler = new AuroraCorrespondentHandler();
			rsOutcomes = CompanyComparisonOutcome.getResultSet(
			 handler.getCompanyComparisonOutcomes(cdbEntity, facade));
			
		} else if (elem.getType().equals(ElementType.ORGANISATION)) {
			Entity cdbEntity = Entity.get(elementId, true, facade);
			
			AuroraClientHandler handler = new AuroraClientHandler();
			rsOutcomes = CompanyComparisonOutcome.getResultSet(
			 handler.getCompanyComparisonOutcomes(cdbEntity, facade));
			
		} else if (elem.getType().equals(ElementType.ACCOUNT)) {
			Account cdbEntity = Account.get(elementId, facade);
			
			AuroraAccountHandler handler = new AuroraAccountHandler();
			rsOutcomes = CompanyComparisonOutcome.getResultSet(
			 handler.getCompanyComparisonOutcomes(cdbEntity, facade));
			
		} else {
			throw new ServiceException
			 ("Unsupported Element Type for Aurora comparison check");
		}
		
		m_binder.addResultSet("rsAuroraComparisonOutcomes", rsOutcomes);	
	}
	
	/** Populates the binder with comprehensive Aurora comparison report data, associated 
	 *  with the ELEMENT_ID in the binder.
	 *  
	 *  The ELEMENT_ID value must correspond to a Person, Org or Account ID.
	 *  
	 * @throws DataException
	 * @throws ServiceException
	 */
	public void getComparisonReport() throws DataException, ServiceException {
		
		FWFacade facade = CCLAUtils.getFacade(true);
		
		Integer elementId = CCLAUtils.getBinderIntegerValue(m_binder, Element.Cols.ID);
		Element elem = Element.get(elementId, facade);
		
		if (elem == null) {
			throw new ServiceException
			 ("Invalid Element ID: " + elementId);
		}
		
		if (elem.getType().equals(ElementType.PERSON)) {
			// Run correspondent comparison report.
			Person dbEntity = Person.get(elementId, true, facade);

			AuroraCorrespondentHandler handler = new AuroraCorrespondentHandler();
			handler.addComparisonReportDataToBinder
			 (dbEntity, m_binder, facade);
			
		} else if (elem.getType().equals(ElementType.ORGANISATION)) {
			// Run correspondent comparison report.
			Entity dbEntity = Entity.get(elementId, true, facade);

			AuroraClientHandler handler = new AuroraClientHandler();
			handler.addComparisonReportDataToBinder
			 (dbEntity, m_binder, facade);
			
		} else if (elem.getType().equals(ElementType.ACCOUNT)) {
			// Run correspondent comparison report.
			Account dbEntity = Account.get(elementId, facade);

			AuroraAccountHandler handler = new AuroraAccountHandler();
			handler.addComparisonReportDataToBinder
			 (dbEntity, m_binder, facade);
							
		} else {
			throw new ServiceException
			 ("Unsupported Element Type for Aurora comparison check");
		}
	}
	
	/** Extracts candidate person names from the signatory data belonging to the
	 *  associated Aurora record.
	 * 
	 * @throws DataException 
	 * @throws ServiceException 
	 *  
	 */
	public void extractPersonNamesFromAuroraSignatoryData()
	 throws DataException, ServiceException {
		
		FWFacade facade = CCLAUtils.getFacade(true);
		Integer elementId = CCLAUtils.getBinderIntegerValue(m_binder, Element.Cols.ID);
		Integer companyId = CCLAUtils.getBinderIntegerValue(m_binder, Company.Cols.ID);
		
		Element elem = Element.get(elementId, facade);
		if (!(elem.getType().equals(ElementType.ORGANISATION)
			||
			elem.getType().equals(ElementType.ACCOUNT))) {
			throw new ServiceException
			 ("Wrong Element Type for Aurora signatory extraction");
		}
		
		AuroraEntitySource dbEntity = null;
		
		m_binder.putLocal("elementType", elem.getType().getName());
		
		// Check the Element Type is valid.
		if (elem.getType().equals(ElementType.ORGANISATION)) {
			dbEntity = Entity.get(elementId, facade);	
		} else if (elem.getType().equals(ElementType.ACCOUNT)) {
			dbEntity = Account.get(elementId, facade);
		}
		
		// Add Aurora Companies selector list
		DataResultSet rsCompanies = dbEntity.getAuroraCompaniesData(facade);
		m_binder.addResultSet("rsCompanies", rsCompanies);
		
		// Determine Aurora company to run extraction against
		Vector<Company> companies = Company.getAll(rsCompanies);
		Company selCompany = null;
		
		String errorMsg = null;
		
		if (companyId != null) {
			selCompany = Company.getCache().getCachedInstance(companyId);
		} else if (companies.isEmpty()) {
			// No mapped Aurora companies!
			errorMsg = "No Aurora Client links for this entity";
		
		} else {
			// No company selected - grab the first one from the available Aurora 
			// companies
			selCompany = companies.get(0);
			
			CCLAUtils.addQueryIntParamToBinder
			 (m_binder, Company.Cols.ID, selCompany.getCompanyId());
		}
		
		// Stores a list of all Persons related to the involved orgs/accounts.
		//
		// If we are extracting sigs from an account, this will include all persons
		// linked to the account in question, the owning org, and all the org's other
		// accounts.
		Vector<Person> existingRelatedPersons = new Vector<Person>();
		
		// Stores a list of all 'Signatory' relationships to the Account/Entity we are
		// running the extraction against.
		Vector<Relation> existingSigRelations = new Vector<Relation>();
		
		// Either the passed Org ID, or the owning Org of the passed Account ID.
		Integer ownerOrgId = null;
		
		if (selCompany != null) {
			Object auroraEntity = null;
			
			try {
				if (dbEntity instanceof Entity) {
					AuroraClientHandler handler = new AuroraClientHandler();
					auroraEntity = handler.getExistingAuroraEntity
					 ((Entity)dbEntity, selCompany, facade);
					
					ownerOrgId = elementId;
					
					RelationName relName = RelationName.getCache().getCachedInstance
					 (RelationName.OrganisationPersonRelation.SIGNATORY);
					
					existingSigRelations = Relation.getRelations
					 (elementId, null, null, relName, facade);
					
				} else {
					AuroraAccountHandler handler = new AuroraAccountHandler();
					auroraEntity = handler.getExistingAuroraEntity
					 ((Account)dbEntity, selCompany, facade);
					
					ownerOrgId = Account.getOwnerOrganisationId(elementId, facade);
					
					RelationName relName = RelationName.getCache().getCachedInstance
					 (RelationName.PersonAccountRelation.SIGNATORY);
					
					existingSigRelations = Relation.getRelations
					 (null, elementId, null, relName, facade);
				}
				
			} catch (Exception e) {
				Log.error("Failed to fetch Aurora record: " + e.getMessage());
				errorMsg = e.getMessage();
			}
			
			Vector<CandidatePersonSignatory> candidateSigs = null;
			
			if (auroraEntity == null) {
				// Associated Aurora entity doesn't exist in Aurora, so nothing to
				// import from!
				String msg = "No signatory data available for import - Aurora record " +
				 "doesn't exist";
				
				Log.error(msg);
				errorMsg = msg;
				
			} else {
				// Extract list of candidate signatories from Aurora entity.
				candidateSigs = 
				 AuroraSignatories.extractSignatoryNamesFromAuroraEntity(auroraEntity);
				
				Log.debug("Fetching all related Persons for Org ID: " + ownerOrgId);
				
				// Fetch all unique related persons.
				DataResultSet rsPersons =
				 Relation.getRelatedEntityAccountPersonsData(ownerOrgId, facade);
				
				// Build existing Person instances.
				if (rsPersons.first()) {
					do {
						existingRelatedPersons.add(Person.get(rsPersons));
					} while (rsPersons.next());
				}
			}
			
			if (candidateSigs != null) {
				// Alrighty that's enough data. Now to cross-check the candidate names
				// extracted from the Aurora record against the current related Persons in
				// the DB.
				
				Log.debug("Checking " + candidateSigs.size() + " candidate signatories " +
				 "against " + existingRelatedPersons.size() + " existing related Persons");
				
				int numDupes = 0;
				int numRelations = 0;
				
				for (CandidatePersonSignatory candidateSig : candidateSigs) {
					// Check for a duplicate name.
					for (Person existingPerson : existingRelatedPersons) {
						// Do case-insensitive check against the full person's name and the
						// abbreviated version.
						if (candidateSig.getName().equalsIgnoreCase
							(existingPerson.getFullName())
							|| 
							candidateSig.getName().equalsIgnoreCase
							(existingPerson.getAbbrevatedFullName())) {
							// Found a duplicate Person.
							Log.debug("Found a duplicate person with ID: " 
							 + existingPerson.getPersonId() + " for candidate: " 
							 + candidateSig.getName());
							
							numDupes++;
							candidateSig.setDuplicatePerson(existingPerson);
							break;
						}
					}
					
					if (candidateSig.getDuplicatePerson() != null) {
						// OK, we found a duplicate, see if there is already a sig relation
						// for them or not.
						Person dupePerson = candidateSig.getDuplicatePerson();
						
						for (Relation rel : existingSigRelations) {
							if (rel.getElementId1() == dupePerson.getPersonId()
								||
								rel.getElementId2() == dupePerson.getPersonId()) {
								// Yep, they already have sig relation set.
								Log.debug("Found existing Signatory relation with ID: " +
								 rel.getRelationId());
								
								numRelations++;
								candidateSig.setExistingRelation(true);
								break;
							}
						}
					}
				}
				
				Log.debug("Completed check, found " + numDupes + " duplicates and " 
				 + numRelations + " existing relations");
				
				DataResultSet rsCandidateSigs = 
				 AuroraSignatories.getCandidatePersonSignatoryResultSet(candidateSigs);
				
				m_binder.addResultSet("rsCandidateSigs", rsCandidateSigs);
			}
		}
		
		if (errorMsg != null)
			m_binder.putLocal("AuroraSignatoryLookupErrorMessage", errorMsg);
	}
	
	/** Creates new Person records and/or signatory relations against the given Element
	 *  ID.
	 * @throws DataException 
	 * @throws ServiceException 
	 *  
	 */
	public void addPersonsFromAuroraSignatoryData() 
	 throws DataException, ServiceException {
		
		Log.debug("addPersonsFromAuroraSignatoryData:begin");
		
		FWFacade facade = CCLAUtils.getFacade(true);
		Integer elementId = CCLAUtils.getBinderIntegerValue(m_binder, Element.Cols.ID);
		Integer companyId = CCLAUtils.getBinderIntegerValue(m_binder, Company.Cols.ID);
		
		Integer numRowsToCheck = CCLAUtils.getBinderIntegerValue(m_binder, "numSigRows");
		if (numRowsToCheck == null)
			numRowsToCheck = 20;
		
		Element elem = Element.get(elementId, facade);
		if (!(elem.getType().equals(ElementType.ORGANISATION)
			||
			elem.getType().equals(ElementType.ACCOUNT))) {
			throw new ServiceException
			 ("Wrong Element Type for Aurora signatory import");
		}
		
		Log.debug("Adding signatory persons/relations against " 
		 + elem.getType().getName() + " ID: " + elementId);
		
		AuroraEntitySource dbEntity = null;
		RelationName sigRelName = null;
		
		// Check the Element Type is valid and capture the required RelationName for
		// later.
		if (elem.getType().equals(ElementType.ORGANISATION)) {
			dbEntity = Entity.get(elementId, facade);			
			
			sigRelName = RelationName.getCache().getCachedInstance
			 (RelationName.OrganisationPersonRelation.SIGNATORY);
			
		} else if (elem.getType().equals(ElementType.ACCOUNT)) {
			dbEntity = Account.get(elementId, facade);
			
			sigRelName = RelationName.getCache().getCachedInstance
			 (RelationName.PersonAccountRelation.SIGNATORY);
		}
		
		if (dbEntity == null) {
			throw new ServiceException("Failed to resolve " 
			 + elem.getType().getName() + " with ID " + elementId);
		}
		
		// Step through all row data in the binder.
		try {
			facade.beginTransaction();
			
			for (int i = 1; i<=numRowsToCheck; i++) {
				Log.debug("Checking row " + i + "/" + numRowsToCheck + 
				 " for signatory import data");
				
				boolean importSig = 
				 CCLAUtils.getBinderBoolValue(m_binder, "IMPORT_SIGNATORY_" + i);
				
				if (!importSig)
					continue;
				
				Log.debug("Row " + i + " marked for import.");
				
				//String sigName = CCLAUtils.getBinderStringValue
				// (m_binder, "SIGNATORY_NAME_" + i);
				
				Integer dupePersonId = CCLAUtils.getBinderIntegerValue
				 (m_binder, "DUPLICATE_PERSON_ID_" + i);	
				
				Integer titleId = CCLAUtils.getBinderIntegerValue
				 (m_binder, "SIGNATORY_TITLE_ID_" + i);
				String firstName = CCLAUtils.getBinderStringValue
				 (m_binder, "SIGNATORY_FIRST_NAME_" + i);
				String middleNames = CCLAUtils.getBinderStringValue
				 (m_binder, "SIGNATORY_MIDDLE_NAMES_" + i);
				String lastName = CCLAUtils.getBinderStringValue
				 (m_binder, "SIGNATORY_LAST_NAME_" + i);
					
				if (dupePersonId == null 
					&& (titleId == null || lastName == null)) {
					throw new ServiceException
					 ("Unable to create new person from Signatory data: title/surname " +
					 "missing from row " + i);
				}
				
				String sigTel = CCLAUtils.getBinderStringValue
				 (m_binder, "SIGNATORY_TELEPHONE_" + i);
				String sigPostcode = CCLAUtils.getBinderStringValue
				 (m_binder, "SIGNATORY_POSTCODE_" + i);			
				
				Integer personId = null;
				
				if (dupePersonId == null) {
					Person newPerson = Person.addFromSignatoryData
					 (titleId, firstName, middleNames, lastName, 
					 sigTel, sigPostcode, facade, m_userData.m_name);
					
					personId = newPerson.getPersonId();
					Log.debug("Generated new Person record with ID " + personId);
					
				} else {
					Log.debug("Duplicate Person ID found: " + dupePersonId + 
					 ". Skipping Person creation");
					
					personId = dupePersonId;
				}
				
				Relation r = null;
				
				if (dbEntity instanceof Entity) {
					// Add Org-Person Signatory relation
					r = Relation.add(
						Integer.parseInt(dbEntity.getId()),
						personId,
						sigRelName, 
						facade, m_userData.m_name
					);
				} else {
					// Add Person-Account Signatory relation
					r = Relation.add(
						personId,
						Integer.parseInt(dbEntity.getId()), 
						sigRelName, 
						facade, m_userData.m_name
					);
				}
				
				Log.debug("Generated new Signatory relation with ID " 
				 + r.getRelationId());
			}
			
			facade.commitTransaction();
			
		} catch (Exception e) {
			facade.rollbackTransaction();
			
			Log.error(e.getMessage(), e);
			throw new ServiceException("Failed to generate Persons from signatory data: " 
			 + e.getMessage());
		}
		
		Log.debug("addPersonsFromAuroraSignatoryData:end");
	}
}
