package com.ecs.ucm.ccla.aurora.compare;

import intradoc.data.DataException;

import java.util.Date;
import java.util.Vector;

import com.aurora.webservice.Correspondent;
import com.ecs.ucm.ccla.data.AuroraCorrespondent;
import com.ecs.ucm.ccla.data.Company;
import com.ecs.ucm.ccla.data.Contact;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.audit.SDAudit;
import com.ecs.ucm.ccla.data.audit.SDAuditData;
import com.ecs.utils.stellent.embedded.FWFacade;

/** Wrapper for an Aurora Correspondent instance, with its grouped field sets.
 *   
 * @author tm
 *
 */
public class CorrespondentFieldSet extends AuroraFieldSet<Person, Correspondent> {
	
	public CorrespondentFieldSet() throws DataException {
		super();
	}

	public CorrespondentFieldSet(Correspondent c) throws DataException {
		super(c);
	}

	/** Labels used for Correspondent attribute name-value pairs.
	 *  
	 * @author tm
	 *
	 */
	public static enum FieldNames {
		Full_Name,
		Salutation,	
		
		ReportAndAccountsEmailIndicator,
		ReportAndAccountsMailIndicator,
		ReportUsage
	}
	
	/** Field Group configurations that make up a Correspondent record */
	public static enum CorrFieldGroupConfig implements FieldGroupConfig {
		Name					("Person's full name and salutation", true),
		Address					("Nominated correspondence address", true),
		OtherContactDetails		("Other Contact Details", false),
		StatementPreferences	("Statement Preferences", false);
		
		private String label;
		private boolean critical;
		
		private CorrFieldGroupConfig(String label, boolean critical) {
			this.label = label;
			this.critical = critical;
		}

		public String getName() {
			return this.toString();
		}

		public String getLabel() {
			return this.label;
		}

		public boolean isCritical() {
			return this.critical;
		}
	}


	@Override
	protected Vector<FieldGroup<Person,Correspondent>> createFieldGroups() 
	 throws DataException {
		
		this.fieldGroups = new Vector<FieldGroup<Person,Correspondent>>();
		
		// Person Name
		{
			FieldGroup<Person,Correspondent> name = new FieldGroup<Person,Correspondent>
			(CorrFieldGroupConfig.Name,
				
				new FieldGroupAttributes<Correspondent>() {
					@Override
					public void applyValuesToEntity(Correspondent auroraEntity) 
					 throws DataException {
						auroraEntity.setName
						 (getFieldValue(FieldNames.Full_Name.toString()));
						auroraEntity.setSalutation
						 (getFieldValue(FieldNames.Salutation.toString()));
					}
	
					@Override
					public void setValuesFromEntity(Correspondent auroraEntity) 
					 throws DataException {
						addFieldValue
						 (FieldNames.Full_Name.toString(), auroraEntity.getName());
						addFieldValue
						 (FieldNames.Salutation.toString(), auroraEntity.getSalutation());
					}
				}
			) {

				@Override
				public void calculateAmendmentDates(
						Person dbEntity, Company company, FWFacade facade) 
						throws DataException {
					this.amendmentDates = new NameAmendmentDates
							 (dbEntity, company, facade);
					
				}
			};
			
			fieldGroups.add(name);
		}
		
		// Address
		{
			FieldGroup<Person,Correspondent> address = new FieldGroup<Person,Correspondent>
			 (CorrFieldGroupConfig.Address, 
			 
				 new FieldGroupAttributes<Correspondent>() {
					 @Override
					public void applyValuesToEntity(Correspondent auroraEntity)
					 throws DataException {
						applyAddressData(auroraEntity.getAddress());
					}
	
					@Override
					public void setValuesFromEntity(Correspondent auroraEntity) 
					 throws DataException {
						addAddressData(auroraEntity.getAddress());
					}
					 
				 }
			) {
				@Override
				public void calculateAmendmentDates(
						Person dbEntity, Company company, FWFacade facade) 
						throws DataException {
	
					this.amendmentDates =  new AddressAmendmentDates
					 (dbEntity, company, facade);
				}
				
			};
			
			fieldGroups.add(address);
		}

		// Other Contact Details
		{
			FieldGroup<Person,Correspondent> contactDetails = 
			 new FieldGroup<Person,Correspondent>
			 (CorrFieldGroupConfig.OtherContactDetails, 
			 
			 new FieldGroupAttributes<Correspondent>() {
				@Override
				public void applyValuesToEntity(Correspondent auroraEntity)
				 throws DataException {
					applyOtherContactData(auroraEntity.getAddress());
				}

				@Override
				public void setValuesFromEntity(Correspondent auroraEntity)
				 throws DataException {
					addOtherContactData(auroraEntity.getAddress());
				}
			}) {

				@Override
				public void calculateAmendmentDates(
				 Person dbEntity, Company company, FWFacade facade) 
					throws DataException {
					this.amendmentDates = new OtherContactDetailsAmendmentDates
					 (dbEntity, company, facade);
				}
				
			};

			fieldGroups.add(contactDetails);
		}
		
		// Statement Prefs
		{
			FieldGroup<Person,Correspondent> statementPrefs = 
			 new FieldGroup<Person,Correspondent>
			 (CorrFieldGroupConfig.StatementPreferences, 
			 new FieldGroupAttributes<Correspondent>() {
				 @Override
					public void applyValuesToEntity(Correspondent auroraEntity)
					 throws DataException {
						auroraEntity.setReportAndAccountsEmailIndicator(
						 Boolean.valueOf(
						 getFieldValue(FieldNames.ReportAndAccountsEmailIndicator.toString())));
						auroraEntity.setReportAndAccountsMailIndicator(
						 Boolean.valueOf(
						 getFieldValue(FieldNames.ReportAndAccountsMailIndicator.toString()))); 
						auroraEntity.setReportUsage(
						 Integer.parseInt(
						 getFieldValue(FieldNames.ReportUsage.toString()))); 
					}

					@Override
					public void setValuesFromEntity(Correspondent auroraEntity) 
					 throws DataException {
						addFieldValue
						 (FieldNames.ReportAndAccountsEmailIndicator.toString(), 
						 Boolean.toString(
						 auroraEntity.isReportAndAccountsEmailIndicator()));
						addFieldValue
						 (FieldNames.ReportAndAccountsMailIndicator.toString(),
						 Boolean.toString(
						 auroraEntity.isReportAndAccountsMailIndicator()));
						addFieldValue
						 (FieldNames.ReportUsage.toString(), 
						 Integer.toString(auroraEntity.getReportUsage()));
					}
			 }) {

				@Override
				public void calculateAmendmentDates(
						Person dbEntity, Company company, FWFacade facade)
						throws DataException {
					this.amendmentDates =
					 new StatementPrefAmendmentDates(dbEntity, company, facade);
				}
				
			};
			
			fieldGroups.add(statementPrefs);
		}
		
		return fieldGroups;
	}

	
	/* ====================
	 * 
	 * Amendment Date calculators
	 * 
	 * ====================
	 */
	
	public static class NameAmendmentDates extends FieldGroupAmendmentDates<Person> {
		
		public NameAmendmentDates(Person dbEntity, Company company,
				FWFacade facade) throws DataException {
			super(dbEntity, company, facade);
		}

		@Override
		protected void captureAmendmentDates() throws DataException {
			Date fullNameLastUpdated = SDAuditData.getLatestDataFieldUpdateDate
			 (this.dbEntity.getPersonId(), ElementType.PERSON.getName(), 
			 Person.Cols.FULL_NAME, facade);
			
			this.addAmendmentDate("Full Name", fullNameLastUpdated);
			
			Date salutationLastUpdated = SDAuditData.getLatestDataFieldUpdateDate
			 (dbEntity.getPersonId(), ElementType.PERSON.getName(), 
			 Person.Cols.SALUTATION, facade);
			
			this.addAmendmentDate("Salutation", salutationLastUpdated);
		}
	}
	
	public class StatementPrefAmendmentDates extends FieldGroupAmendmentDates<Person> {

		public StatementPrefAmendmentDates(Person dbEntity, Company company,
				FWFacade facade) throws DataException {
			super(dbEntity, company, facade);
		}

		@Override
		protected void captureAmendmentDates() throws DataException {
			// Check for last attribute update times against the relevant attributes in 
			// the AuroraCorrespondent entity (PERSON_AURORA_MAP table)
			
			Vector<AuroraCorrespondent> corrMaps = AuroraCorrespondent
			 .getCorrespondentsByPersonIdAndCompanyId
			 (dbEntity.getPersonId(), company.getCompanyId(), facade);
			
			if (corrMaps.size() != 1) {
				throw new DataException("Expected 1 Aurora Correspondent map, found " 
				 + corrMaps.size());
			}
			
			AuroraCorrespondent corrMap = corrMaps.get(0);
			{ 
				Date reportAndAccountsMailIndicator = 
				 SDAuditData.getLatestDataFieldUpdateDate
				 (corrMap.getMapId(), 
				 ElementType.SecondaryElementType.PersonAuroraMap.toString(), 
				 AuroraCorrespondent.Cols.REPORT_MAIL_INDICATOR, facade);
				
				addAmendmentDate
				 ("Report And Account Mail Indicator" , reportAndAccountsMailIndicator);
			}
			
			{
				Date reportAndAccountsEmailIndicator = 
				 SDAuditData.getLatestDataFieldUpdateDate
				 (corrMap.getMapId(), 
				 ElementType.SecondaryElementType.PersonAuroraMap.toString(), 
				 AuroraCorrespondent.Cols.REPORT_EMAIL_INDICATOR, facade);
				
				addAmendmentDate
				 ("Report And Account Email Indicator" , reportAndAccountsEmailIndicator);
			}
			{
				Date reportUsage = 
				 SDAuditData.getLatestDataFieldUpdateDate
				 (corrMap.getMapId(), 
				 ElementType.SecondaryElementType.PersonAuroraMap.toString(), 
				 AuroraCorrespondent.Cols.REPORT_USAGE, facade);
				
				addAmendmentDate
				 ("Report Usage", reportUsage);
			}
		}
	}
	
}
