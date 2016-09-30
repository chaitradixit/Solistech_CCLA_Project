package com.ecs.ucm.ccla.aurora;

import intradoc.common.ServiceException;
import intradoc.data.DataBinder;
import intradoc.data.DataException;
import intradoc.data.DataResultSet;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Vector;

import com.aurora.webservice.Correspondent;
import com.ecs.ucm.ccla.CCLAUtils;
import com.ecs.ucm.ccla.aurora.compare.AuroraEntityComparator;
import com.ecs.ucm.ccla.aurora.compare.AuroraEntityComparisonOutcome;
import com.ecs.ucm.ccla.aurora.compare.AuroraEntitySource;
import com.ecs.ucm.ccla.aurora.compare.AuroraFieldSet;
import com.ecs.ucm.ccla.aurora.compare.ClientFieldSet;
import com.ecs.ucm.ccla.aurora.compare.ComparisonUtils;
import com.ecs.ucm.ccla.aurora.compare.CorrespondentFieldSet;
import com.ecs.ucm.ccla.aurora.compare.FieldGroup;
import com.ecs.ucm.ccla.aurora.compare.FieldGroupConfig;
import com.ecs.ucm.ccla.aurora.compare.AuroraEntityComparisonOutcome.CompanyComparisonOutcome;
import com.ecs.ucm.ccla.aurora.compare.AuroraEntityComparisonOutcome.ComparisonOutcome;
import com.ecs.ucm.ccla.data.Company;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.utils.DateUtils;
import com.ecs.utils.Log;
import com.ecs.utils.stellent.embedded.FWFacade;

/**
 * All methods required for static data STP updates against an Aurora entity. Includes
 * support for detailed comparison checks and partial updates.
 * 
 * The public methods for actually adding/updating Aurora entities:
 * 
 * addAuroraEntity()
 * updateAuroraEntity()
 * 
 * The abstract methods that must be implemented with appropriate web service calls:
 * 
 * addToAurora()
 * updateInAurora()
 * getExistingAuroraEntity()
 * 
 * 
 * @author tm
 *
 * @param <A> Aurora Entity type e.g. Correspondent
 * @param <C> source Central DB Entity type e.g. Person
 */
public abstract class AuroraEntityHandler<A, C extends AuroraEntitySource> {
	
	/** Generates a fresh instance of an Aurora Entity by mapping information from the 
	 *  passed Central DB entity.
	 *  
	 *  Shouldn't need to call any Aurora web services to fetch information.
	 *  
	 *  Must throw an error if the Central DB entity data isn't valid for Aurora 
	 *  translation, e.g. required fields are missing or the required Company mapping 
	 *  entry isn't present in the Central DB.
	 *  
	 * @param dbEntity
	 * @param company	
	 * @param facade
	 * @return
	 */
	public abstract A buildAuroraEntityInstance(C dbEntity, Company company, 
	 FWFacade facade) throws DataException;
	
	/** Sets default values against the passed Aurora entity. Should be called during
	 *  build of a fresh Aurora entity instance.
	 *  
	 * @param auroraEntity
	 * @throws DataException
	 */
	protected abstract void setDefaultValues(A auroraEntity) throws DataException;
	
	/** Compares the passed Central DB entity with its equivalent in Aurora, for the
	 *  given Company.
	 *  
	 *  Handles situations where the Central DB record is invalid to run a comparison
	 *  against, or there is no equivalent Aurora entity to compare against.
	 *  
	 * @param dbEntity
	 * @param company
	 * @param facade
	 * @return
	 * @throws DataException 
	 */
	public AuroraEntityComparisonOutcome<A> compare
	 (C dbEntity, Company company, FWFacade facade) throws DataException {		
		A existingAuroraEntity = null;
		
		try {
			existingAuroraEntity = getExistingAuroraEntity(dbEntity, company, facade);
		} catch (Exception e) {
			// May be an issue fetching the existing Aurora entity, due to invalid
			// DB entity data etc.
			return new AuroraEntityComparisonOutcome<A>
			 (ComparisonOutcome.INVALID_DATA, e.getMessage());
		}
		
		if (existingAuroraEntity != null) {
			// Build a Field Set instance from the existing Aurora record.
			AuroraFieldSet<C,A> auroraFieldSet = getAuroraFieldSet(existingAuroraEntity);
			
			try {
				// Construct an Aurora entity from Central DB data.
				A newAuroraEntity = buildAuroraEntityInstance(dbEntity, company, facade);
				
				// Build another Field Set instance from the Aurora record constructed
				// from Central DB data.
				AuroraFieldSet<C,A> dbFieldSet = getAuroraFieldSet(newAuroraEntity);	
				
				AuroraEntityComparator<C, A> comparator =
				 new AuroraEntityComparator<C, A>();
				
				// Compare the 2 field sets.
				return comparator.compare(dbFieldSet, auroraFieldSet);
				
			} catch (Exception e) {
				// Assume one of the methods above threw an error due to invalid data.
				return new AuroraEntityComparisonOutcome<A>
				 (ComparisonOutcome.INVALID_DATA, e.getMessage());
			}
		} else {
			return new AuroraEntityComparisonOutcome<A>
			 (ComparisonOutcome.NO_AURORA_ENTITY, 
			 "Unable to find corresponding Aurora record for this Company/Code");
		}
	}
	
	/** Checks that the Central DB has sufficient data set (including Aurora mapping
	 *  attributes). Should be called before attempting to create/update the equivalent
	 *  Aurora entity.
	 *  
	 * @param dbEntity
	 * @param company
	 * @param facade
	 * @throws DataException if any validation check fails.
	 */
	public abstract void validateDBInstance(C dbEntity, Company company, FWFacade facade)
	 throws DataException;
	
	/** Checks that the generated/amended Aurora Entity instance is valid for publishing
	 *  to Aurora. Should be called just before executing the Aurora Add/Amend web 
	 *  service.
	 *  
	 * @param auroraEntity
	 * @param company
	 * @param facade
	 * @throws DataException
	 */
	public abstract void validateAuroraInstance
	 (A auroraEntity, Company company, FWFacade facade)
	 throws DataException;
	
	/** Adds the Central DB entity to Aurora via web service call, providing it doesn't
	 *  exist there already.
	 *  
	 *  Can be overridden to implement further validation/logging.
	 *  
	 * @param entity
	 * @param company
	 * @param facade
	 * @throws ServiceException 
	 */
	public void addAuroraEntity(C dbEntity, Company company, FWFacade facade)
	 throws DataException, ServiceException {
		A existingAuroraEntity = getExistingAuroraEntity(dbEntity, company, facade);
		
		if (existingAuroraEntity != null) {
			String msg = "Unable to add new Aurora entity - already exists in Aurora";
			Log.error(msg);
			throw new DataException(msg);
		}
	
		A auroraEntity = buildAuroraEntityInstance(dbEntity, company, facade);
		validateAuroraInstance(auroraEntity, company, facade);
		
		addToAurora(auroraEntity, company, facade);
	}
	
	/** Attempts to fetch the equivalent Aurora Entity instance from Aurora.
	 * 
	 *  Returns null if the equivalent entity doesn't exist in Aurora.
	 * 
	 *  Must throw an error if the required Company mapping entry isn't present in the
	 *  Central DB.
	 * 
	 * @param dbEntity	The equivalent DB entity
	 * @param company	The Aurora Company database to search in
	 * @param facade
	 * @return 			The equivalent Aurora Entity, if it exists
	 */
	public abstract A getExistingAuroraEntity
	 (C dbEntity, Company company, FWFacade facade) throws DataException;
	
	/** Adds the corresponding Aurora entity to Aurora via web service call.
	 *  
	 * @param auroraEntity
	 * @param facade
	 * @return web service response, if applicable
	 * @throws ServiceException 
	 * @throws DataException 
	 */
	protected abstract Object addToAurora
	 (A auroraEntity, Company company, FWFacade facade) throws DataException;
	
	/** Updates the corresponding Aurora entity in Aurora via web service call.
	 *  
	 * @param auroraEntity
	 * @param facade
	 * @return web service response, if applicable
	 */
	protected abstract Object updateInAurora
	 (A auroraEntity, Company company, FWFacade facade) throws DataException;
	
	/** Performs a partial update of the passed Aurora entity instance based on the
	 *  list of Field Groups.
	 *  
	 *  Doesn't call any Aurora web service to actually apply the update!
	 *  
	 * @param auroraEntity
	 * @param fieldGroups
	 * @throws DataException
	 */
	public void updateAuroraEntityInstance
	 (A auroraEntity, Vector<FieldGroup<C,A>> fieldGroups)
	 throws DataException {
		Log.debug("Running partial update of Aurora entity");

		Log.debug("Pre-update: " + auroraEntity.toString());
		
		for (FieldGroup<C,A> fieldGroup : fieldGroups) {
			Log.debug("Updating Field Group: " + fieldGroup.getName());
			fieldGroup.getFieldGroupAttributes().applyValuesToEntity(auroraEntity);
		}
		
		Log.debug("Post-update: " + auroraEntity.toString());
	}
	
	/** Performs a partial update of the corresponding Aurora entity in Aurora by 
	 *  extracting the passed FieldGroup attribute values. 
	 *  
	 *  Calls the relevant Aurora update web service to apply the change.
	 *  
	 * @param dbEntity
	 * @param company
	 * @param fieldGroupNames
	 * @param facade
	 * @throws DataException	if the corresponding Aurora entity doesn't exist
	 * @throws ServiceException 
	 */
	public void updateAuroraEntity
	 (C dbEntity, Company company, Vector<String> fieldGroupNames, FWFacade facade)
	 throws DataException, ServiceException {
		Log.debug("updateAuroraEntity::Source DB entity type=" 
		 + dbEntity.getClass().getSimpleName() + ", Company=" + company.getCode());
		
		Log.debug("Passed fieldGroupNames: " +
		 fieldGroupNames != null ? 
		 CCLAUtils.getCSVFromStringList(fieldGroupNames, true) : "[null]");
		
		// Aurora rendition of entity
		A existingAuroraEntity = getExistingAuroraEntity(dbEntity, company, facade);

		if (existingAuroraEntity == null) {
			String msg = "Unable to update Aurora entity. " +
			 "Couldn't find corresponding entity in Aurora";
			Log.error(msg);
			throw new DataException(msg);
		}
		
		Log.debug("Running pre-update validation checks against DB entity");
		validateDBInstance(dbEntity, company, facade);
		
		// Central DB rendition of entity
		A cdbAuroraEntity = buildAuroraEntityInstance(dbEntity, company, facade);
		
		AuroraFieldSet<C,A> fieldSet = getAuroraFieldSet(cdbAuroraEntity);
		Vector<FieldGroup<C,A>> updateFieldGroups = new Vector<FieldGroup<C,A>>();
		
		if (fieldGroupNames != null && fieldGroupNames.size() > 0) {
			updateFieldGroups = fieldSet.getFilteredGroupsByNames(fieldGroupNames);
		} else {
			Log.debug("List of updated field group names is null/empty. " +
			 "Marking all field groups for update");
			
			updateFieldGroups = fieldSet.getFieldGroups();
		}
		
		Log.debug("Updating local Aurora entity instance");
		updateAuroraEntityInstance(existingAuroraEntity, updateFieldGroups);
		
		Log.debug("Running pre-update validation checks against Aurora entity");
		validateAuroraInstance(existingAuroraEntity, company, facade);
		
		updateInAurora(existingAuroraEntity, company, facade);
	}

	/** Returns a fresh instance of the appropriate AuroraFieldSet.
	 * 
	 * @param auroraEntity
	 * @return
	 * @throws DataException 
	 */
	public abstract AuroraFieldSet<C,A> getAuroraFieldSet(A auroraEntity) 
	 throws DataException;
	
	/** Adds detailed comparison report data to the DataBinder, related to the passed
	 *  Central DB entity.
	 *  
	 *  Creates a group of ResultSets for each mapped Company.
	 *  
	 * @param dbEntity
	 * @param binder
	 * @param facade
	 * @throws DataException
	 */
	public void addComparisonReportDataToBinder
	 (C dbEntity, DataBinder binder, FWFacade facade) throws DataException {
		
		AuroraFieldSet<C, A> fieldSet = getAuroraFieldSet(null);
		
		// Report Label
		binder.putLocal("ReportLabel",dbEntity.getComparisonLabel());
		// Field Groups
		binder.addResultSet("rsFieldGroups",fieldSet.getFieldGroupsResultSet());
		
		// Calculate an amendment limit date, based on the current time.
		Date limitDate = ComparisonUtils.getAmendmentLimitDate(new Date());
		binder.putLocalDate("AmendmentLimitDate", limitDate);
		
		// Companies
		DataResultSet rsCompanies = dbEntity.getAuroraCompaniesData(facade);
		binder.addResultSet("rsCompanies", rsCompanies);
				
		Vector<Company> companies = Company.getAll(rsCompanies);
		
		// Step through all mapped companies.
		for (Company company : companies) {
			fieldSet.calculateGroupAmendmentDates(dbEntity, company, facade);
			
			// Field Group Amendment dates
			binder.addResultSet
			 ("rsFieldGroupAmendmentDates_CompanyId_" + company.getCompanyId(),
			 fieldSet.getFieldGroupAmendmentDatesResultSet());

			// Amended Field Group names
			Vector<String> amendedGroupNames = 
			 fieldSet.getFilteredGroupNamesByAmendmentDate(limitDate);
			binder.putLocal("AmendedFieldGroups_CompanyId_" + company.getCompanyId(),
			 CCLAUtils.getCSVFromStringList(amendedGroupNames, true));
			
			AuroraEntityComparisonOutcome<A> comparisonOutcome =
			 compare(dbEntity, company, facade);
			
			DataResultSet rs = comparisonOutcome.getSummaryResultSet();
			binder.addResultSet
			 ("rsComparisonOutcome_CompanyId_" + company.getCompanyId(), rs);
			
			if (comparisonOutcome.getComparisons() != null) {
				DataResultSet rsMismatchedGroups = comparisonOutcome
				 .getMismatchedGroupsResultSet();
				binder.addResultSet
				 ("rsMismatchedGroupNames_CompanyId_" + company.getCompanyId(), 
				 rsMismatchedGroups);
				
				LinkedHashMap<FieldGroupConfig, DataResultSet> rsComparisons 
				 = comparisonOutcome.getDetailResultSets();
				
				for (FieldGroupConfig fieldGroupConfig : rsComparisons.keySet()) {
					binder.addResultSet
					 ("rsComparisonDetail_CompanyId_" + company.getCompanyId() +
					 "_FieldGroup_" + fieldGroupConfig.getName(), 
					 rsComparisons.get(fieldGroupConfig));
				}
			} 
		}
	}
	
	/** Fetches basic Aurora correspondent comparison outcomes for all Aurora companies 
	 *  associated with the passed Central DB entity.
	 *  
	 * @param person
	 * @param facade
	 * @return
	 * @throws DataException
	 */
	public Vector<CompanyComparisonOutcome<A>> getCompanyComparisonOutcomes
	 (C cdbEntity, FWFacade facade) throws DataException {
		
		Vector<CompanyComparisonOutcome<A>> outcomes = 
		 new Vector<CompanyComparisonOutcome<A>>();

		Vector<Company> companies = cdbEntity.getAuroraCompanies(facade);
		
		if (companies.isEmpty())
			return outcomes;
		
		Log.debug("Running Aurora comparison check on Central DB entity: " 
		 + cdbEntity.getClass().getSimpleName() + ", ID " + cdbEntity.getId() + 
		 " against " + companies.size() + " companies");
		
		for (Company company : companies) {
			AuroraEntityComparisonOutcome<A> comparisonOutcome
		     = compare(cdbEntity, company, facade);
			outcomes.add(
			 new CompanyComparisonOutcome<A>(company, comparisonOutcome));
		}
		
		return outcomes;
	}
}
