package com.ecs.ucm.ccla.aurora.compare;

import intradoc.data.DataException;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Vector;

import com.ecs.ucm.ccla.aurora.AuroraAccountHandler;
import com.ecs.ucm.ccla.aurora.AuroraSignatories;
import com.ecs.ucm.ccla.aurora.AuroraSignatory;
import com.ecs.ucm.ccla.aurora.AuroraWebServiceHandler;
import com.ecs.ucm.ccla.data.Account;
import com.ecs.ucm.ccla.data.BankAccount;
import com.ecs.ucm.ccla.data.Company;
import com.ecs.ucm.ccla.data.ElementAttribute;
import com.ecs.ucm.ccla.data.ElementAttributeApplied;
import com.ecs.ucm.ccla.data.ElementType;
import com.ecs.ucm.ccla.data.Relation;
import com.ecs.ucm.ccla.data.RelationName;
import com.ecs.ucm.ccla.data.RelationProperty;
import com.ecs.ucm.ccla.data.RelationPropertyApplied;
import com.ecs.ucm.ccla.data.audit.SDAudit;
import com.ecs.ucm.ccla.data.audit.SDAuditData;
import com.ecs.utils.Log;
import com.ecs.utils.StringUtils;
import com.ecs.utils.stellent.embedded.FWFacade;

public class AccountFieldSet extends AuroraFieldSet<Account, com.aurora.webservice.Account> {

	/** Labels used for Account attribute name-value pairs.
	 *  
	 * @author tm
	 *
	 */
	public static enum FieldNames {
		Subtitle,
		NominatedCorrespondentCode,	
		
		IncomeDistributionMethod,
		MandatedCompany,
		MandatedAccount,
		
		BankAccountNameWithdrawal,
		BankAccountNumberWithdrawal,
		BankSortCodeWithdrawal,
		ShortNameOrBuildingSocietyRefWithdrawal,
		
		BankAccountNameIncome,
		BankAccountNumberIncome,
		BankSortCodeIncome,
		ShortNameOrBuildingSocietyRefIncome,

		StandingTransactions,
		ExternalAccount,
		
		ShareClassCode
	}
	
	/** Field Group configurations that make up a Client record */
	public static enum AccountFieldGroupConfig implements FieldGroupConfig {
		Name					("Account Name", true),
		NominatedCorrespondent	("Nominated Account Correspondent", true),
		
		IncomeDistributionPreferences	("Income Distribution Preferences", true),
		WithdrawalBankAccount	("Nominated Withdrawal Bank Account Details", true),
		IncomeBankAccount		("Nominated Income Bank Account Details", true),
		
		NumRequiredSignatures	("Number of required signatures", true),
		SignatoryList			("Account Signatories list", true),
		
		MiscPreferences			("Misc. Account Preferences", false),
		ShareClass				("Share Class", true);
		
		private String label;
		private boolean critical;
		
		private AccountFieldGroupConfig(String label, boolean critical) {
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
	
	public AccountFieldSet() throws DataException {
		super();
	}
	
	public AccountFieldSet(com.aurora.webservice.Account account) throws DataException {
		super(account);
	}

	@Override
	protected Vector<FieldGroup<Account, com.aurora.webservice.Account>> 
	 createFieldGroups() throws DataException {
		this.fieldGroups = new Vector<FieldGroup<Account, com.aurora.webservice.Account>>();
		
		// Client Name
		FieldGroup<Account,com.aurora.webservice.Account> name = 
		 new FieldGroup<Account,com.aurora.webservice.Account>
		 (AccountFieldGroupConfig.Name,
			
			new FieldGroupAttributes<com.aurora.webservice.Account>() {
				@Override
				public void applyValuesToEntity(com.aurora.webservice.Account auroraEntity) 
				 throws DataException {
					auroraEntity.setSubtitle
					 (getFieldValue(FieldNames.Subtitle.toString()));
				}

				@Override
				public void setValuesFromEntity(com.aurora.webservice.Account auroraEntity) 
				 throws DataException {
					addFieldValue
					 (FieldNames.Subtitle.toString(), auroraEntity.getSubtitle());
				}
			}
		) {
			@Override
			public void calculateAmendmentDates(
			 Account dbEntity, Company company, FWFacade facade) 
			 throws DataException {
				this.amendmentDates = new NameAmendmentDates
				 (dbEntity, company, facade);
				
			}
		};
		
		// Nominated Correspondent
		FieldGroup<Account, com.aurora.webservice.Account> nominatedCorr = 
		 new FieldGroup<Account, com.aurora.webservice.Account>
		 (AccountFieldGroupConfig.NominatedCorrespondent,
			
			new FieldGroupAttributes<com.aurora.webservice.Account>() {
				@Override
				public void applyValuesToEntity(com.aurora.webservice.Account auroraEntity) 
				 throws DataException {
					auroraEntity.setCorrespondentCode
					 (Integer.parseInt(
					 getFieldValue(FieldNames.NominatedCorrespondentCode.toString())));
				}

				@Override
				public void setValuesFromEntity(com.aurora.webservice.Account auroraEntity) 
				 throws DataException {
					addFieldValue
					 (FieldNames.NominatedCorrespondentCode.toString(),
					 Integer.toString(auroraEntity.getCorrespondentCode()));
				}
			}
		) {
			@Override
			public void calculateAmendmentDates(
			 Account dbEntity, Company company, FWFacade facade) 
					throws DataException {
				this.amendmentDates = new NominatedCorrespondentAmendmentDates
				 (dbEntity, company, facade);
				
			}
		};
		
		// Income Distribution Preferences
		FieldGroup<Account, com.aurora.webservice.Account> incomeDistPrefs = 
		 new FieldGroup<Account, com.aurora.webservice.Account>
		 (AccountFieldGroupConfig.IncomeDistributionPreferences,
			
			new FieldGroupAttributes<com.aurora.webservice.Account>() {
				@Override
				public void applyValuesToEntity(com.aurora.webservice.Account auroraEntity) 
				 throws DataException {		
					auroraEntity.setIncomeDistributionMethod
					 (AuroraAccountHandler.getIncomeDistributionMethod(
					 getFieldValue(FieldNames.IncomeDistributionMethod.toString())));
					
					auroraEntity.setMandatedAccount
					 (getFieldValue(FieldNames.MandatedAccount.toString()));
					auroraEntity.setMandatedCompany
					 (getFieldValue(FieldNames.MandatedCompany.toString()));
				}

				@Override
				public void setValuesFromEntity(com.aurora.webservice.Account auroraEntity) 
				 throws DataException {
					addFieldValue
					 (FieldNames.IncomeDistributionMethod.toString(),
					 auroraEntity.getIncomeDistributionMethod().toString());
					
					addFieldValue(FieldNames.MandatedAccount.toString(),
					 auroraEntity.getMandatedAccount());
					addFieldValue(FieldNames.MandatedCompany.toString(),
					 auroraEntity.getMandatedCompany());
				}
			}
		) {
			@Override
			public void calculateAmendmentDates(
			 Account dbEntity, Company company, FWFacade facade) 
					throws DataException {
				this.amendmentDates = new IncomeDistributionPrefAmendmentDates
				 (dbEntity, company, facade);
				
			}
		};

		// Wtihdrawal Bank Account
		FieldGroup<Account, com.aurora.webservice.Account> withdrawalBankAccount = 
		 new FieldGroup<Account, com.aurora.webservice.Account>
		 (AccountFieldGroupConfig.WithdrawalBankAccount,
			
			new FieldGroupAttributes<com.aurora.webservice.Account>() {
				@Override
				public void applyValuesToEntity(com.aurora.webservice.Account auroraEntity) 
				 throws DataException {		
					auroraEntity.setBankAccountNameWithdrawal
					 (getFieldValue(FieldNames.BankAccountNameWithdrawal.toString()));
					auroraEntity.setBankAccountNumberWithdrawal
					 (Integer.parseInt(
					 getFieldValue(FieldNames.BankAccountNumberWithdrawal.toString())));
					auroraEntity.setBankSortCodeWithdrawal
					 (Integer.parseInt(
					 getFieldValue(FieldNames.BankSortCodeWithdrawal.toString())));
					auroraEntity.setAccountShortNameOrBuildingSocietyReferenceWithdrawal
					 (getFieldValue(FieldNames.ShortNameOrBuildingSocietyRefWithdrawal.toString()));
				}

				@Override
				public void setValuesFromEntity(com.aurora.webservice.Account auroraEntity) 
				 throws DataException {
					addFieldValue(FieldNames.BankAccountNameWithdrawal.toString(),
					 auroraEntity.getBankAccountNameWithdrawal());
					addFieldValue(FieldNames.BankAccountNumberWithdrawal.toString(),
					 Integer.toString(auroraEntity.getBankAccountNumberWithdrawal()));
					addFieldValue(FieldNames.BankSortCodeWithdrawal.toString(),
					 Integer.toString(auroraEntity.getBankSortCodeWithdrawal()));
					addFieldValue(FieldNames.ShortNameOrBuildingSocietyRefWithdrawal.toString(),
					 auroraEntity.getAccountShortNameOrBuildingSocietyReferenceWithdrawal());
				}
			}
		) {
			@Override
			public void calculateAmendmentDates(
			 Account dbEntity, Company company, FWFacade facade) 
					throws DataException {
				this.amendmentDates = new BankAccountAmendmentDates
				 (dbEntity, company, facade, BankAccountType.Withdrawal);
				
			}
		};
		
		// Num Required Sigs
		FieldGroup<Account, com.aurora.webservice.Account> numReqSigs = 
		 new FieldGroup<Account, com.aurora.webservice.Account>
		 (AccountFieldGroupConfig.NumRequiredSignatures,
			
			new FieldGroupAttributes<com.aurora.webservice.Account>() {
				@Override
				public void applyValuesToEntity(com.aurora.webservice.Account auroraEntity) 
				 throws DataException {		
					auroraEntity.setMultipleSignaturesIndicator
					 (Boolean.parseBoolean(getFieldValue
					 (AuroraSignatories.FieldNames.MULTI_SIGS_INDICATOR)));
					
					auroraEntity.setMultipleSignaturesInformation
					 (getFieldValue(AuroraSignatories.FieldNames.MULTI_SIGS_INFO));
				}

				@Override
				public void setValuesFromEntity(com.aurora.webservice.Account auroraEntity) 
				 throws DataException {
					addFieldValue(AuroraSignatories.FieldNames.MULTI_SIGS_INDICATOR, 
					 Boolean.toString(auroraEntity.isMultipleSignaturesIndicator()));
					
					addFieldValue(AuroraSignatories.FieldNames.MULTI_SIGS_INFO, 
					 auroraEntity.getMultipleSignaturesInformation());
				}
		 	}
		) {
			@Override
			public void calculateAmendmentDates(
			 Account dbEntity, Company company, FWFacade facade) 
					throws DataException {
				this.amendmentDates = new NumReqSigsAmendmentDates
				 (dbEntity, company, facade);
				
			}
		};
		
		// Income Bank Account
		FieldGroup<Account, com.aurora.webservice.Account> incomeBankAccount = 
		 new FieldGroup<Account, com.aurora.webservice.Account>
		 (AccountFieldGroupConfig.IncomeBankAccount,
			
			new FieldGroupAttributes<com.aurora.webservice.Account>() {
				@Override
				public void applyValuesToEntity(com.aurora.webservice.Account auroraEntity) 
				 throws DataException {		
					auroraEntity.setBankAccountNameIncome
					 (getFieldValue(FieldNames.BankAccountNameIncome.toString()));
					auroraEntity.setBankAccountNumberIncome
					 (Integer.parseInt(
					 getFieldValue(FieldNames.BankAccountNumberIncome.toString())));
					auroraEntity.setBankSortCodeIncome
					 (Integer.parseInt(
					 getFieldValue(FieldNames.BankSortCodeIncome.toString())));
					auroraEntity.setAccountShortNameOrBuildingSocietyReferenceIncome
					 (getFieldValue(FieldNames.ShortNameOrBuildingSocietyRefIncome.toString()));
				}

				@Override
				public void setValuesFromEntity(com.aurora.webservice.Account auroraEntity) 
				 throws DataException {
					addFieldValue(FieldNames.BankAccountNameIncome.toString(),
					 auroraEntity.getBankAccountNameIncome());
					addFieldValue(FieldNames.BankAccountNumberIncome.toString(),
					 Integer.toString(auroraEntity.getBankAccountNumberIncome()));
					addFieldValue(FieldNames.BankSortCodeIncome.toString(),
					 Integer.toString(auroraEntity.getBankSortCodeIncome()));
					addFieldValue(FieldNames.ShortNameOrBuildingSocietyRefIncome.toString(),
					 auroraEntity.getAccountShortNameOrBuildingSocietyReferenceIncome());
				}
			}
		) {
			@Override
			public void calculateAmendmentDates(
			 Account dbEntity, Company company, FWFacade facade) 
					throws DataException {
				this.amendmentDates = new BankAccountAmendmentDates
				 (dbEntity, company, facade, BankAccountType.Income);
				
			}
		};
		
		FieldGroup<Account,com.aurora.webservice.Account> miscPrefs = 
		 new FieldGroup<Account,com.aurora.webservice.Account>
		 (AccountFieldGroupConfig.MiscPreferences,
			
			new FieldGroupAttributes<com.aurora.webservice.Account>() {
				@Override
				public void applyValuesToEntity(com.aurora.webservice.Account auroraEntity) 
				 throws DataException {
					auroraEntity.setStandingTransactionsIndicator
					 (Boolean.parseBoolean(
					 getFieldValue(FieldNames.StandingTransactions.toString())));
				}

				@Override
				public void setValuesFromEntity(com.aurora.webservice.Account auroraEntity) 
				 throws DataException {
					addFieldValue
					 (FieldNames.StandingTransactions.toString(), 
					 Boolean.toString(auroraEntity.isStandingTransactionsIndicator()));
				}
			}
		) {
			@Override
			public void calculateAmendmentDates(
			 Account dbEntity, Company company, FWFacade facade) 
			 throws DataException {
				this.amendmentDates = new MiscPreferencesAmendmentDates
				 (dbEntity, company, facade);
				
			}
		};
		
		// Share Class
		FieldGroup<Account,com.aurora.webservice.Account> shareClass = 
		 new FieldGroup<Account,com.aurora.webservice.Account>
		 (AccountFieldGroupConfig.ShareClass,
			
			new FieldGroupAttributes<com.aurora.webservice.Account>() {
				@Override
				public void applyValuesToEntity(com.aurora.webservice.Account auroraEntity) 
				 throws DataException {
					auroraEntity.setShareClassCode
					 (Integer.parseInt(
					 getFieldValue(FieldNames.ShareClassCode.toString())));
				}

				@Override
				public void setValuesFromEntity(com.aurora.webservice.Account auroraEntity) 
				 throws DataException {
					addFieldValue
					 (FieldNames.ShareClassCode.toString(), 
					 Integer.toString(auroraEntity.getShareClassCode()));
				}
			}
		) {
			@Override
			public void calculateAmendmentDates(
			 Account dbEntity, Company company, FWFacade facade) 
			 throws DataException {
				this.amendmentDates = new ShareClassAmendmentDates
				 (dbEntity, company, facade);
				
			}
		};
		
		// Signatory List
		FieldGroup<Account,com.aurora.webservice.Account> signatoryList = 
		 new FieldGroup<Account,com.aurora.webservice.Account>
		 (AccountFieldGroupConfig.SignatoryList,
			
			new FieldGroupAttributes<com.aurora.webservice.Account>() {
				@Override
				public void applyValuesToEntity(com.aurora.webservice.Account auroraEntity) 
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
				public void setValuesFromEntity(com.aurora.webservice.Account auroraEntity) 
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
			 Account dbEntity, Company company, FWFacade facade) 
					throws DataException {
				this.amendmentDates = new SignatoryAmendmentDates
				 (dbEntity, company, facade);
				
			}
		};
		
		fieldGroups.add(name);
		fieldGroups.add(nominatedCorr);
		fieldGroups.add(incomeDistPrefs);
		fieldGroups.add(withdrawalBankAccount);
		fieldGroups.add(incomeBankAccount);
		fieldGroups.add(numReqSigs);
		fieldGroups.add(signatoryList);
		fieldGroups.add(miscPrefs);
		fieldGroups.add(shareClass);

		return this.fieldGroups;
	}
	
	
	/* ====================
	 * 
	 * Amendment Date calculators
	 * 
	 * ====================
	 */
	
	protected static class NameAmendmentDates extends FieldGroupAmendmentDates<Account> {
		public NameAmendmentDates(Account dbEntity, Company company,
				FWFacade facade) throws DataException {
			super(dbEntity, company, facade);
		}

		@Override
		protected void captureAmendmentDates() throws DataException {
			Date nameLastUpdated = SDAuditData.getLatestDataFieldUpdateDate
			 (this.dbEntity.getAccountId(), ElementType.ACCOUNT.getName(), 
			 Account.Cols.SUBTITLE, facade);
			
			this.addAmendmentDate("Subtitle", nameLastUpdated);
		}
	}
	
	protected static class IncomeDistributionPrefAmendmentDates 
	 extends FieldGroupAmendmentDates<Account> {
		public IncomeDistributionPrefAmendmentDates(Account dbEntity, Company company,
				FWFacade facade) throws DataException {
			super(dbEntity, company, facade);
		}

		@Override
		protected void captureAmendmentDates() throws DataException {
			Date incDistMethodLastUpdated = SDAuditData.getLatestDataFieldUpdateDate
			 (this.dbEntity.getAccountId(), ElementType.ACCOUNT.getName(), 
			 Account.Cols.INC_DIST_METHOD, facade);
			
			Date mandatedAccountLastUpdated = SDAuditData.getLatestDataFieldUpdateDate
			 (this.dbEntity.getAccountId(), ElementType.ACCOUNT.getName(), 
			 Account.Cols.MANDATED_ACCOUNT, facade);
					
			this.addAmendmentDate
			 ("Income Distribution Method", incDistMethodLastUpdated);
			this.addAmendmentDate
			 ("Mandated Account", mandatedAccountLastUpdated);
		}
	}
	
	enum BankAccountType {
		Withdrawal,
		Income
	}
	
	protected static class BankAccountAmendmentDates 
	 extends FieldGroupAmendmentDates<Account> {
		
		private BankAccountType bankAccountType;
		
		public BankAccountAmendmentDates(Account dbEntity, Company company,
				FWFacade facade, BankAccountType bankAccountType) throws DataException {
			this.dbEntity = dbEntity;
			this.company = company;
			this.facade = facade;
			
			this.amendmentDates = new LinkedHashMap<String, Date>();
			this.latestAmendmentLabel = null;
			
			this.bankAccountType = bankAccountType;
			
			this.captureAmendmentDates();
		}

		@Override
		protected void captureAmendmentDates() throws DataException {
			
			RelationName relName 	= null;
			Integer relPropId 		= null;
			
			if (this.bankAccountType == BankAccountType.Withdrawal) {
				relName = RelationName.getCache().getCachedInstance
				 (RelationName.AccountBankAccountRelation.WITHDRAWAL);
				
				relPropId = RelationProperty.RELATION_PROPERTY_WITH_DEFAULT;
			} else {
				relName = RelationName.getCache().getCachedInstance
				 (RelationName.AccountBankAccountRelation.INCOME);
				
				relPropId = RelationProperty.RELATION_PROPERTY_INC_DEFAULT;
			}
			
			BankAccount nominatedBankAccount = Account.getNominatedBankAccount
			 (dbEntity.getAccountId(), relName, facade);
			
			Date lastAuditDate = null, relationAddedDate = null, 
			 defaultPropertySetDate = null;
			
			if (nominatedBankAccount != null) {
				// Find last audit entry for Bank Account record itself
				SDAudit latestAudit = SDAudit.getLatestSDAuditByEventAndRelationId
				 (ElementType.BANK_ACCOUNT.getName(), 
				 nominatedBankAccount.getBankAccountId(), facade);
				
				if (latestAudit != null)
					lastAuditDate = latestAudit.getAuditDate();
				
				// Find the Relation.
				Relation rel = Relation.getRelations(
					dbEntity.getAccountId(), 
					nominatedBankAccount.getBankAccountId(), 
					null, 
					relName, 
					facade
				).get(0);
				
				relationAddedDate = rel.getRelationDate();
				
				// Now check for the Default/Nominated property
				Vector<RelationPropertyApplied> relProps = 
				 RelationPropertyApplied.getByRelation
				 (rel.getRelationId(), facade);
				
				for (RelationPropertyApplied relProp : relProps) {
					if (relProp.getRelationProperty().getRelationPropertyId() 
						== relPropId) {
						// Found the prop. Now determine when it was added.
						defaultPropertySetDate = relProp.getDateAdded();
					}
				}
			} else {
				// No nominated bank account set - try and determine when the last
				// nominated bank account was removed instead.
				SDAudit audit = SDAudit.getLatestRelationPropertyDeletionByElementId
				 (dbEntity.getAccountId(), relPropId, facade);
				
				if (audit != null)
					defaultPropertySetDate = audit.getAuditDate();
			}
			
			addAmendmentDate(this.bankAccountType.toString() + 
			 " Account Relation added", relationAddedDate);
			addAmendmentDate(this.bankAccountType.toString() + 
			 " Account Relation Nominated Flag set", defaultPropertySetDate);
			addAmendmentDate(this.bankAccountType.toString() + 
			 " Account last audit event", lastAuditDate);
		}
	}
	
	protected static class MiscPreferencesAmendmentDates 
	 extends FieldGroupAmendmentDates<Account> {
		public MiscPreferencesAmendmentDates(Account dbEntity, Company company,
				FWFacade facade) throws DataException {
			super(dbEntity, company, facade);
		}

		@Override
		protected void captureAmendmentDates() throws DataException {
			
			Date standingTransactionsLastUpdated = null, internalAccountLastUpdated = null;
			
			ElementAttributeApplied standingTransactions = ElementAttributeApplied.get(
				dbEntity.getAccountId(), 
				ElementAttribute.AccountAttributes.STANDING_TRANSACTIONS, 
				facade
			);
			
			if (standingTransactions != null)
				standingTransactionsLastUpdated = standingTransactions.getLastUpdated();
			
			ElementAttributeApplied internalAccount = ElementAttributeApplied.get(
				dbEntity.getAccountId(), 
				ElementAttribute.AccountAttributes.INTERNAL_ACCOUNT, 
				facade
			);
			
			if (internalAccount != null)
				internalAccountLastUpdated = internalAccount.getLastUpdated();

			this.addAmendmentDate
			 ("Standing Transactions", standingTransactionsLastUpdated);
			this.addAmendmentDate
			 ("Internal Account", internalAccountLastUpdated);
		}
	}
	
	protected static class ShareClassAmendmentDates 
	 extends FieldGroupAmendmentDates<Account> {
		public ShareClassAmendmentDates(Account dbEntity, Company company,
				FWFacade facade) throws DataException {
			super(dbEntity, company, facade);
		}

		@Override
		protected void captureAmendmentDates() throws DataException {

			Date shareClassLastUpdated = SDAuditData.getLatestDataFieldUpdateDate
					 (this.dbEntity.getAccountId(), ElementType.ACCOUNT.getName(), 
					 Account.Cols.SHARE_CLASS, facade);
			
			this.addAmendmentDate
			 ("Share Class ID", shareClassLastUpdated);
		}
	}
	
	protected static class NumReqSigsAmendmentDates 
	 extends FieldGroupAmendmentDates<Account> {
		public NumReqSigsAmendmentDates(Account dbEntity, Company company,
				FWFacade facade) throws DataException {
			super(dbEntity, company, facade);
		}

		@Override
		protected void captureAmendmentDates() throws DataException {

			Date numReqSigsUpdated = SDAuditData.getLatestDataFieldUpdateDate
			 (this.dbEntity.getAccountId(), ElementType.ACCOUNT.getName(), 
			 Account.Cols.REQ_SIGNATURES, facade);
			
			this.addAmendmentDate
			 ("Num Req Signatures", numReqSigsUpdated);
		}
	}
}
