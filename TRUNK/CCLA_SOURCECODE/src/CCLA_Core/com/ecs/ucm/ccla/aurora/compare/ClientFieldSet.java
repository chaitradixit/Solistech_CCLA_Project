package com.ecs.ucm.ccla.aurora.compare;

import intradoc.data.DataException;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Vector;

import com.aurora.webservice.Client;
import com.ecs.ucm.ccla.aurora.AuroraSignatories;
import com.ecs.ucm.ccla.aurora.AuroraSignatory;
import com.ecs.ucm.ccla.aurora.AuroraWebServiceHandler;
import com.ecs.ucm.ccla.aurora.compare.AccountFieldSet.FieldNames;
import com.ecs.ucm.ccla.data.AuroraClient;
import com.ecs.ucm.ccla.data.Company;
import com.ecs.ucm.ccla.data.Element;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.Entity;
import com.ecs.ucm.ccla.data.Person;
import com.ecs.ucm.ccla.data.Relation;
import com.ecs.ucm.ccla.data.RelationName;
import com.ecs.ucm.ccla.data.RelationProperty;
import com.ecs.ucm.ccla.data.RelationPropertyApplied;
import com.ecs.ucm.ccla.data.audit.SDAuditData;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

public class ClientFieldSet extends AuroraFieldSet<Entity, Client> {

	/** Labels used for Client attribute name-value pairs.
	 *  
	 * @author tm
	 *
	 */
	public static enum FieldNames {
		Name,
		NominatedCorrespondentCode,	
		
		DataProtectionMailingGroupIndicator,
		
		// Statement Prefs fields
		StatementMonths1,
		StatementMonths2,
		StatementMonths3,
		StatementMonths4,
		StatementMonthsAll,
		
		IncomeDistReportIndicator,
		IncomeDistReportPaperIndicator,
		
		BulkDepositStatementIndicator,
		DepositWithdrawBooksIndicator,
		AutoStationaryOrderedIndicator,
		
		QuarterlyReportClientIndicator,
		QuarterlyReportManagerInitials,
		
		NumberOfStatementsDeposit,
		NumberOfStatementsUnitised,
		
		// Marketing Details
		MarketingGroupId,
		MarketingSubGroupId,
		AccountGroupId
	}
	
	/** Field Group configurations that make up a Client record */
	public static enum ClientFieldGroupConfig implements FieldGroupConfig {
		Name					("Client Name", true),
		NominatedCorrespondent	("Nominated Client Correspondent", true),
		
		/* DataProtection			("Data Protection Indicator", true), */
		
		Address					("Client Address", false),
		OtherContactDetails		("Other Contact Details", false),
		
		StatementPreferences	("Statement Preferences", false),
		MarketingDetails		("Marketing Details", false),
		
		SignatoryList			("Signatory List", true);
		
		private String label;
		private boolean critical;
		
		private ClientFieldGroupConfig(String label, boolean critical) {
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

	public ClientFieldSet() throws DataException {
		super();
	}

	public ClientFieldSet(Client auroraEntity) throws DataException {
		super(auroraEntity);
	}

	@Override
	protected Vector<FieldGroup<Entity, Client>> createFieldGroups()
			throws DataException {

		this.fieldGroups = new Vector<FieldGroup<Entity,Client>>();
		
		// Client Name
		FieldGroup<Entity,Client> name = new FieldGroup<Entity,Client>
		(ClientFieldGroupConfig.Name,
			
			new FieldGroupAttributes<Client>() {
				@Override
				public void applyValuesToEntity(Client auroraEntity) 
				 throws DataException {
					auroraEntity.setName
					 (getFieldValue(FieldNames.Name.toString()));
				}

				@Override
				public void setValuesFromEntity(Client auroraEntity) 
				 throws DataException {
					addFieldValue
					 (FieldNames.Name.toString(), auroraEntity.getName());
				}
			}
		) {
			@Override
			public void calculateAmendmentDates(
			 Entity dbEntity, Company company, FWFacade facade) 
			 throws DataException {
				this.amendmentDates = new NameAmendmentDates
				 (dbEntity, company, facade);
				
			}
		};
		
		// Nominated Correspondent
		FieldGroup<Entity,Client> nominatedCorr = new FieldGroup<Entity,Client>
		(ClientFieldGroupConfig.NominatedCorrespondent,
			
			new FieldGroupAttributes<Client>() {
				@Override
				public void applyValuesToEntity(Client auroraEntity) 
				 throws DataException {
					auroraEntity.setDefaultCorrespondentNumber
					(Integer.parseInt(
					getFieldValue(FieldNames.NominatedCorrespondentCode.toString())));
				}

				@Override
				public void setValuesFromEntity(Client auroraEntity) 
				 throws DataException {
					addFieldValue
					 (FieldNames.NominatedCorrespondentCode.toString(),
					 Integer.toString(
					 auroraEntity.getDefaultCorrespondentNumber()));
				}
			}
		) {
			@Override
			public void calculateAmendmentDates(
			 Entity dbEntity, Company company, FWFacade facade) 
					throws DataException {
				this.amendmentDates = new NominatedCorrespondentAmendmentDates
				 (dbEntity, company, facade);
				
			}
		};
		
		// Data Protection
		/*
		FieldGroup<Entity,Client> dataProtection = new FieldGroup<Entity,Client>
		(ClientFieldGroupConfig.DataProtection,
			
			new FieldGroupAttributes<Client>() {
				@Override
				public void applyValuesToEntity(Client auroraEntity) 
				 throws DataException {
					auroraEntity.setDataProtectionMailingGroupIndicator(
					 Boolean.parseBoolean(
					 (getFieldValue(FieldNames.DataProtectionMailingGroupIndicator.toString()))));
				}

				@Override
				public void setValuesFromEntity(Client auroraEntity) 
				 throws DataException {
					addFieldValue
					 (FieldNames.DataProtectionMailingGroupIndicator.toString(),
					 Boolean.toString(
					 auroraEntity.isDataProtectionMailingGroupIndicator()));
				}
			}
		) {
			@Override
			public void calculateAmendmentDates(
			 Entity dbEntity, Company company, FWFacade facade) 
					throws DataException {
				this.amendmentDates = new DataProtectionAmendmentDates
				 (dbEntity, company, facade);
				
			}
		};
		*/
		
		// Address
		FieldGroup<Entity,Client> address = new FieldGroup<Entity,Client>
		(ClientFieldGroupConfig.Address,
			
			new FieldGroupAttributes<Client>() {
				@Override
				public void applyValuesToEntity(Client auroraEntity) 
				 throws DataException {
					applyAddressData(auroraEntity.getAddress());
				}

				@Override
				public void setValuesFromEntity(Client auroraEntity) 
				 throws DataException {
					addAddressData(auroraEntity.getAddress());
				}
			}
		) {
			@Override
			public void calculateAmendmentDates(
			 Entity dbEntity, Company company, FWFacade facade) 
					throws DataException {
				this.amendmentDates = new AddressAmendmentDates
				 (dbEntity, company, facade);
			}
		};
		
		// Other Contact Details
		FieldGroup<Entity,Client> otherContactDetails = new FieldGroup<Entity,Client>
		(ClientFieldGroupConfig.OtherContactDetails,
			
			new FieldGroupAttributes<Client>() {
				@Override
				public void applyValuesToEntity(Client auroraEntity) 
				 throws DataException {
					applyOtherContactData(auroraEntity.getAddress());
				}

				@Override
				public void setValuesFromEntity(Client auroraEntity) 
				 throws DataException {
					addOtherContactData(auroraEntity.getAddress());
				}
			}
		) {
			@Override
			public void calculateAmendmentDates(
			 Entity dbEntity, Company company, FWFacade facade) 
					throws DataException {
				this.amendmentDates = new OtherContactDetailsAmendmentDates
				 (dbEntity, company, facade);
			}
		};
		
		// Statement Prefs
		FieldGroup<Entity,Client> statementPrefs = new FieldGroup<Entity,Client>
		(ClientFieldGroupConfig.StatementPreferences,
			
			new FieldGroupAttributes<Client>() {
				@Override
				public void applyValuesToEntity(Client auroraEntity) 
				 throws DataException {
					auroraEntity.setStatementMonths1
					 (Integer.parseInt(
					 getFieldValue(FieldNames.StatementMonths1.toString())));
					auroraEntity.setStatementMonths2
					 (Integer.parseInt(
					 getFieldValue(FieldNames.StatementMonths2.toString())));
					auroraEntity.setStatementMonths3
					 (Integer.parseInt(
					 getFieldValue(FieldNames.StatementMonths3.toString())));
					auroraEntity.setStatementMonths4
					 (Integer.parseInt(
					 getFieldValue(FieldNames.StatementMonths4.toString())));
					auroraEntity.setStatementMonthsAllIndicator
					 (Boolean.parseBoolean(
					 getFieldValue(FieldNames.StatementMonthsAll.toString())));
					
					auroraEntity.setIncomeDistributionReportIndicator
					 (Boolean.parseBoolean(
					 getFieldValue(FieldNames.IncomeDistReportIndicator.toString())));
					auroraEntity.setIncomeDistributionReportPaperIndicator
					 (Boolean.parseBoolean(
					 getFieldValue(FieldNames.IncomeDistReportPaperIndicator.toString())));
					
					auroraEntity.setBulkDepositStatementsIndicator
					 (Boolean.parseBoolean(
					 getFieldValue(FieldNames.BulkDepositStatementIndicator.toString())));
					auroraEntity.setDepositAndWithdrawalBooksIndicator
					 (Boolean.parseBoolean(
					 getFieldValue(FieldNames.DepositWithdrawBooksIndicator.toString())));
					auroraEntity.setAutomaticStationaryOrderedIndicator
					 (Boolean.parseBoolean(
					 getFieldValue(FieldNames.AutoStationaryOrderedIndicator.toString())));
					
					auroraEntity.setQuarterlyReportingClientIndicator
					 (Boolean.parseBoolean(
					 getFieldValue(FieldNames.QuarterlyReportClientIndicator.toString())));
					auroraEntity.setQuarterlyReportingManagerInitials
					 (getFieldValue(FieldNames.QuarterlyReportManagerInitials.toString()));
					
					auroraEntity.setNumberOfStatementsDeposit
					(Integer.parseInt(
					 getFieldValue(FieldNames.NumberOfStatementsDeposit.toString())));
					auroraEntity.setNumberOfStatementsUnitised
					 (Integer.parseInt(
					 getFieldValue(FieldNames.NumberOfStatementsUnitised.toString())));
				}

				@Override
				public void setValuesFromEntity(Client auroraEntity) 
				 throws DataException {
					addFieldValue(FieldNames.StatementMonths1.toString(),
					 Integer.toString(auroraEntity.getStatementMonths1()));
					addFieldValue(FieldNames.StatementMonths2.toString(),
					 Integer.toString(auroraEntity.getStatementMonths2()));
					addFieldValue(FieldNames.StatementMonths3.toString(),
					 Integer.toString(auroraEntity.getStatementMonths3()));
					addFieldValue(FieldNames.StatementMonths4.toString(),
					 Integer.toString(auroraEntity.getStatementMonths4()));
					addFieldValue(FieldNames.StatementMonthsAll.toString(),
					 Boolean.toString(auroraEntity.isStatementMonthsAllIndicator()));
					
					addFieldValue(FieldNames.IncomeDistReportIndicator.toString(),
					 Boolean.toString(auroraEntity.isIncomeDistributionReportIndicator()));
					addFieldValue(FieldNames.IncomeDistReportPaperIndicator.toString(),
					 Boolean.toString(auroraEntity.isIncomeDistributionReportPaperIndicator()));
					
					addFieldValue(FieldNames.BulkDepositStatementIndicator.toString(),
					 Boolean.toString(auroraEntity.isBulkDepositStatementsIndicator()));
					addFieldValue(FieldNames.DepositWithdrawBooksIndicator.toString(),
					 Boolean.toString(auroraEntity.isDepositAndWithdrawalBooksIndicator()));
					addFieldValue(FieldNames.AutoStationaryOrderedIndicator.toString(),
					 Boolean.toString(auroraEntity.isAutomaticStationaryOrderedIndicator()));
					
					addFieldValue(FieldNames.QuarterlyReportClientIndicator.toString(),
					 Boolean.toString(auroraEntity.isQuarterlyReportingClientIndicator()));
					addFieldValue(FieldNames.QuarterlyReportManagerInitials.toString(),
					 auroraEntity.getQuarterlyReportingManagerInitials());
					
					addFieldValue(FieldNames.NumberOfStatementsDeposit.toString(),
					 Integer.toString(auroraEntity.getNumberOfStatementsDeposit()));
					addFieldValue(FieldNames.NumberOfStatementsUnitised.toString(),
					 Integer.toString(auroraEntity.getNumberOfStatementsUnitised()));
				}
			}
		) {
			@Override
			public void calculateAmendmentDates(
			 Entity dbEntity, Company company, FWFacade facade) 
					throws DataException {
				this.amendmentDates = new StatementPrefAmendmentDates
				 (dbEntity, company, facade);
			}
		};
		
		// Marketing Details
		FieldGroup<Entity,Client> marketingDetails = new FieldGroup<Entity,Client>
		(ClientFieldGroupConfig.MarketingDetails,
			
			new FieldGroupAttributes<Client>() {
				@Override
				public void applyValuesToEntity(Client auroraEntity) 
				 throws DataException {
					auroraEntity.setMarketingGroup
					 (Integer.parseInt(
					 getFieldValue(FieldNames.MarketingGroupId.toString())));
					auroraEntity.setMarketingSubGroup
					 (Integer.parseInt(
					 getFieldValue(FieldNames.MarketingSubGroupId.toString())));
				}

				@Override
				public void setValuesFromEntity(Client auroraEntity) 
				 throws DataException {
					addFieldValue
					 (FieldNames.MarketingGroupId.toString(), 
					 Integer.toString(auroraEntity.getMarketingGroup()));
					addFieldValue
					 (FieldNames.MarketingSubGroupId.toString(), 
					 Integer.toString(auroraEntity.getMarketingSubGroup()));
				}
			}
		) {
			@Override
			public void calculateAmendmentDates(
			 Entity dbEntity, Company company, FWFacade facade) 
					throws DataException {
				this.amendmentDates = new MarketingDetailsAmendmentDates
				 (dbEntity, company, facade);
				
			}
		};
		
		// Signatory List
		FieldGroup<Entity,Client> signatoryList = new FieldGroup<Entity,Client>
		(ClientFieldGroupConfig.SignatoryList,
			
			new FieldGroupAttributes<Client>() {
				@Override
				public void applyValuesToEntity(Client auroraEntity) 
				 throws DataException {
					
					for (int i = 1; 
						i <= AuroraWebServiceHandler.MAXIMUM_AURORA_SIGNATORIES; i++) {
						
						try {
							String sigName = getFieldValue
							 (AuroraSignatories.FieldNamePrefixes.NAME + i);
							String sigPostcode = getFieldValue
							 (AuroraSignatories.FieldNamePrefixes.POSTCODE + i);
							String sigTelephone = getFieldValue
							 (AuroraSignatories.FieldNamePrefixes.TELEPHONE + i);
							
							AuroraSignatories.applyToAuroraEntity(
								sigName, 
								sigPostcode, 
								sigTelephone, 
								auroraEntity, 
								i
							);
							
						} catch (Exception e) {
							Log.error("Failed assignment of Signatory List values", e);
						}
					}
				}

				@Override
				public void setValuesFromEntity(Client auroraEntity) 
				 throws DataException {
					for (int i = 1; 
						i <= AuroraWebServiceHandler.MAXIMUM_AURORA_SIGNATORIES; i++) {
						
						try {
							AuroraSignatory sig = 
							 AuroraSignatories.getFromAuroraEntity(auroraEntity, i);
							
							addFieldValue
							 (AuroraSignatories.FieldNamePrefixes.NAME + i, sig.getName());
							addFieldValue
							 (AuroraSignatories.FieldNamePrefixes.POSTCODE + i, sig.getPostcode());
							addFieldValue
							 (AuroraSignatories.FieldNamePrefixes.TELEPHONE + i, sig.getTelephone());
						
						} catch (Exception e) {
							Log.error("Failed capture of Signatory List values", e);
						}
					}
				}
			}
		) {
			@Override
			public void calculateAmendmentDates(
			 Entity dbEntity, Company company, FWFacade facade) 
					throws DataException {
				this.amendmentDates = new SignatoryAmendmentDates
				 (dbEntity, company, facade);
				
			}
		};
		
		fieldGroups.add(name);
		fieldGroups.add(nominatedCorr);
		//fieldGroups.add(dataProtection);
		fieldGroups.add(address);
		fieldGroups.add(otherContactDetails);
		fieldGroups.add(statementPrefs);
		fieldGroups.add(marketingDetails);
		fieldGroups.add(signatoryList);
		
		return this.fieldGroups;
	}
	
	/* ====================
	 * 
	 * Amendment Date calculators
	 * 
	 * ====================
	 */
	
	public static class NameAmendmentDates extends FieldGroupAmendmentDates<Entity> {
		public NameAmendmentDates(Entity dbEntity, Company company,
				FWFacade facade) throws DataException {
			super(dbEntity, company, facade);
		}

		@Override
		protected void captureAmendmentDates() throws DataException {
			Date nameLastUpdated = SDAuditData.getLatestDataFieldUpdateDate
			 (this.dbEntity.getOrganisationId(), ElementType.ORGANISATION.getName(), 
			 Entity.Cols.ORGANISATION_NAME, facade);
			
			this.addAmendmentDate("Name", nameLastUpdated);
		}
	}
	
	public static class StatementPrefAmendmentDates extends FieldGroupAmendmentDates<Entity> {
		public StatementPrefAmendmentDates(Entity dbEntity, Company company,
				FWFacade facade) throws DataException {
			super(dbEntity, company, facade);
		}

		private static Vector<String> statementMonthsFieldNames = new Vector<String>();
		private static Vector<String> otherPrefsFieldNames = new Vector<String>();
		
		static {
			statementMonthsFieldNames.add(AuroraClient.Cols.STATEMENTS_MONTH_1);
			statementMonthsFieldNames.add(AuroraClient.Cols.STATEMENTS_MONTH_2);
			statementMonthsFieldNames.add(AuroraClient.Cols.STATEMENTS_MONTH_3);
			statementMonthsFieldNames.add(AuroraClient.Cols.STATEMENTS_MONTH_4);
			statementMonthsFieldNames.add(AuroraClient.Cols.STATEMENTS_MONTH_ALL);
			
			otherPrefsFieldNames.add(AuroraClient.Cols.INCOME_DIST_REPORT_IND);
			otherPrefsFieldNames.add(AuroraClient.Cols.INCOME_DIST_REPORT_PAPER_IND);
			otherPrefsFieldNames.add(AuroraClient.Cols.BULK_DEPOSIT_STATEMENT_IND);
			otherPrefsFieldNames.add(AuroraClient.Cols.DEPOSIT_WITHDRAW_BOOKS_IND);
			otherPrefsFieldNames.add(AuroraClient.Cols.AUTO_STATIONARY_ORDERED_IND);
			
			otherPrefsFieldNames.add(AuroraClient.Cols.QUARTERLY_RPT_CLIENT_IND);
			otherPrefsFieldNames.add(AuroraClient.Cols.QUARTERLY_RPT_MANAGER_INITS);
			otherPrefsFieldNames.add(AuroraClient.Cols.DEPOSIT_NO_OF_STATEMENTS);
			otherPrefsFieldNames.add(AuroraClient.Cols.UNITISED_NO_OF_STATEMENTS);
		}
		
		@Override
		protected void captureAmendmentDates() throws DataException {
			Date statementMonthsDate = null, otherStatementPrefsDate = null;
			
			// Find the Client Aurora Mapping
			AuroraClient clMap = Entity.getAuroraClientCompanyMapping
			 (dbEntity.getOrganisationId(), company, facade);
			
			if (clMap != null) {
				statementMonthsDate = SDAuditData.getLatestDataFieldGroupUpdateDate
				 (clMap.getMapId(), 
				 ElementType.SecondaryElementType.ClientAuroraMap.toString(), 
				 statementMonthsFieldNames, 
				 facade);
				
				otherStatementPrefsDate = SDAuditData.getLatestDataFieldGroupUpdateDate
				 (clMap.getMapId(), 
				 ElementType.SecondaryElementType.ClientAuroraMap.toString(), 
				 otherPrefsFieldNames, 
				 facade);
			}
			
			this.addAmendmentDate("Statement Months", statementMonthsDate);
			this.addAmendmentDate("Other Statement Prefs", otherStatementPrefsDate);
		}
	}
	
	public static class MarketingDetailsAmendmentDates extends FieldGroupAmendmentDates<Entity> {
		public MarketingDetailsAmendmentDates(Entity dbEntity, Company company,
				FWFacade facade) throws DataException {
			super(dbEntity, company, facade);
		}

		private static Vector<String> marketingDetailsFieldNames = new Vector<String>();
		
		static {
			marketingDetailsFieldNames.add(AuroraClient.Cols.MARKETING_GROUP_ID);
			marketingDetailsFieldNames.add(AuroraClient.Cols.MARKETING_SUBGROUP_ID);
		}
		
		@Override
		protected void captureAmendmentDates() throws DataException {
			Date marketingDetailsDate = null;
			
			// Find the Client Aurora Mapping
			AuroraClient clMap = Entity.getAuroraClientCompanyMapping
			 (dbEntity.getOrganisationId(), company, facade);
			
			if (clMap != null) {
				marketingDetailsDate = SDAuditData.getLatestDataFieldGroupUpdateDate
				 (clMap.getMapId(), 
				 ElementType.SecondaryElementType.ClientAuroraMap.toString(), 
				 marketingDetailsFieldNames, 
				 facade);
			}
			
			this.addAmendmentDate("Marketing Details", marketingDetailsDate);
		}
	}
	
	public static class DataProtectionAmendmentDates extends FieldGroupAmendmentDates<Entity> {
		public DataProtectionAmendmentDates(Entity dbEntity, Company company,
				FWFacade facade) throws DataException {
			super(dbEntity, company, facade);
		}

		@Override
		protected void captureAmendmentDates() throws DataException {
			Date doNotContactDate = SDAuditData.getLatestDataFieldUpdateDate
			 (dbEntity.getElementId(), 
			 "Element", 
			 Element.Cols.DO_NOT_CONTACT_FLAG, 
			 facade);

			this.addAmendmentDate("Do Not Contact Flag", doNotContactDate);
		}
	}
}
