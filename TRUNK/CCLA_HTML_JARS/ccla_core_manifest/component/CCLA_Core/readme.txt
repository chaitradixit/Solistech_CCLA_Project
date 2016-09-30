CCLA_Core Component Release v1.6.8
================================================================================
  Full Release Name: 2014_11_07 v1.6.8 (Build 22156)

      Date: 07/11/2014 01:59 PM
    Author: tm
  Location: http://ecs-svn/stellent/CLIENTS/CCLA/Components/CCLA_Core/tags/v1.6.8
           
(c) Extended Content Solutions Limited 2014
================================================================================
﻿CCLA_Core Component
===================
Tom Marchant

Contains all Java DAOs and data caches in the codebase. Shared config vars
and queries are included in component resource.


Build History
-------------

Version 1.0.0 (17/03/11)
------------------------
- First build.
- UCI00136 Client Query Boost

Version 1.0.1 (28/03/11)
-------------------------
- Run the script sql_updates_17032011.sql
- UCI00155 Enable Signature Checking on CONDINS, CLOSEACS instructions 
- Moved Holiday Dates code resources from SPP_INTEGRATION

Version 1.0.2 (07/04/11)
------------------------
- Run the scripts in the given order: 
	system_config_var_update_01042011.sql
	instruction_data_fix_28032011.sql
	ref_instruction_type_update_01042011.sql
- Updated load order of startup filter to 15 to ensure it is loaded before
  other CCLA components.

Version 1.0.3 (18/04/11)
------------------------
- Amended Entity Checking to incorporate the flag CCLA_CS_EnableEntityVerification.
  Entity Checking will execute differently based on the status of this flag.

Version 1.0.4 (04/05/11)
------------------------
- Moved some SPP_INTEGRATION Java methods to CCLAUtils class.
- Refactored queries and DAOs to match schema updates.
- Run the scripts in the given order:
	sql_updates_050411.sql
	PSDF_fund_data_load.sql

Version 1.0.5 (07/05/11)
------------------------
- Run the following scripts:
  - campaign-schema-270411.sql
  - sql_updates_17032011.sql
 
Version 1.0.6 (09/05/11)
------------------------
- Amended Person class to fetch its parent Element information
- Extra updates to support PSDF Campaign/spool file generation

Version 1.0.7 (09/05/11)
------------------------
- Extra updates to support PSDF Campaign/spool file generation

Version 1.0.8 (10/05/11)
------------------------
- Run the script: Enrolment_status_action_applied_data_load_100511.sql
- Various minor changes to support spool file generation

Version 1.0.9 (13/05/11)
------------------------
- Updated DateUtils for Daily Transaction Expenses Calculations
- PI companion accounts are now automatically generated on creation
  of a PC account.

Version 1.1.0 (16/05/11)
------------------------
- Fixed BankAccount.validate() function to ensure padded account number/
  sort code are dispatched to Aurora ADF Bank Finder service. This was
  causing errors before if the account number/sort code had leading zeroes.  

Version 1.1.1 (18/05/11)
------------------------
- Updated Client Number allocation to use Org Account Code numeric suffix,
  if it is a valid Aurora Client Number
  
Version 1.1.2 (20/05/11)
------------------------
- Amended DAOs to deal with new KYC requirements

Version 1.1.3 (23/05/11)
------------------------
- Account IVS checking has been amended to look for account-level overrides
  against related persons.
- An account IVS check will now fail if there are no authorising persons
  associated with the account.
- (Misc.) Run the following scripts:
  	org_account_cod_regexp.sql
	20_MAY_PERSON_OVERRIDE.sql
- (Form handling) Run the following script:
    form_updates_21052011.sql
- (Instruction Register) Run the following scripts:
	sql_updates_23032011.sql
	sql_updates_4.7 to 4.8.sql
	sql_updates_140411_instruction_lock_table.sql
	instruction_register_views_030511.sql
	instruction_rule_sequences_14042011.sql  

Version 1.1.4 (24/05/11)
------------------------
- Added auditing for CommGroup, Comm and Instruction objects.

Version 1.1.5 (25/05/11)
------------------------
- Instruction Audit Changes.

Version 1.1.6 (25/05/11)
------------------------
- Fix to getInstructions method in CommGroup DAO
- Fix to String value representation of large Float values in
  InstructionDataApplied elements.

Version 1.1.7 (31/05/11)
------------------------
- Instruction Audit Changes. Requires:  instruction_audit_tables_240511.sql
- Run the following scripts:
  - appinstrdata_orgid_270511.sql
 
Version 1.1.8 (06/06/11)
-----------------
- Minor updates to core codebase, required for latest builds of 
  CCLA_CommProcessing and CCLA_ClientServices
 
Version 1.1.9 (06/06/11)
-----------------
- UCI00195 xAmount field rounding issue in Iris

Version 1.2.0 (24/06/11)
-----------------
- UCI00214 Account Intentions, Requires 13_June_Intention_Update.sql to be ran against live.
- Consoladated validation methods into panelValidationExtra
- UCI00221 Fixed verification Sources getting batch-updated incorrectly
- UCI00202 Create Instruction from interactions. Requires 16_June_interaction_instruction_data_load.sql
- UCI00183 Fixed Lamit account lookup
- Instruction Info Panel Update - Requires 24_June_Instruction_Info_View_Update.sql

Version 1.2.1 (01/07/11)
------------------------
- Created Java class for AccountFlag (entries in REF_ACCOUNT_FLAG table)

Version 1.2.2 (14/07/11)
------------------------
- Fixed bug which returned a positive Entity Check result when a Relation Check 
  override was set.
- Added WorkGroup and WorkGroupUser DB objects to codebase
- Run the following scripts:
  - work_group_schema_06072011.sql
  
Version 1.2.3 (08/08/11)
------------------------
- Amended InstructionDataApplied batch-updating to remove invalid applied data values, e.g. when
  the Instruction's Type is changed.
- Run the following scripts:
  - instr_data_cleanup_18072011.sql
- UCI00235 Added email field to search form.

Version 1.2.4 (16/08/11)
------------------------
- Added long value truncation to audit data values. This prevents excessively long string values
  from breaking inserts to the SDAUDIT_DATA table.
- Run the following scripts:
  - dioloan_data_load_09082011.sql

Version 1.2.5 (23/08/11)
------------------------
- Run the following scripts:
  - account_schema_updates_17082011.sql
- Update schema base via Config Manager after running the above.
- Added new document classes, instruction types and accompanying Dual Index rules

Version 1.2.6 (31/08/11)
------------------------
- Added support for post-validation actions on documents with Form IDs. Currently
  implemented for Email Indemnity forms only.
- Contact Point table now has an Auth Status column. Only updatable for Email contacts
  currently.  
- Run the following scripts:
  - diodepbnk_update_24082011.sql
  - form_contact_updates_26082011.sql

Version 1.2.7 (22/09/11)
------------------------
- Externalized web service endpoint address for Validator Plus web services:
  - EXPERIAN_ValidatorPlusWebServiceAddress
- Externalized Experian server name:
  - EXPERIAN_ServerName
- Added Account Agreement Type field
- Run the following scripts:
  - account_schema_updates_22092011.sql
  
Version 1.2.8 (26/09/2011)
------------------
- Fixed 'Unitized' typo. DB update script already executed in all environments
- Added size check to getMultipleRelationsData to ensure a sensible error
  message is output if either element list has more than 1000 elements.
- Added new method to Relation class for fetching all unique bank accounts
  linked to an Organisation. Allows all bank accounts to be fetched for
  Organisations with more than 1000 accounts.
- DICONDIN-PREADVICE CR - Requires Transaction_Batch_19092011.sql (in Core).
  NB. This can be rolled-out without running the script as this is not finished.    
- FileStoreProvider Install: 
  Refer to http://extranet.extended-content.com/ucmxe/groups/public/@ccla/documents/case_document/xnet002765.pdf for details

Version 1.2.9 (18/11/11)
------------------------
- Added validation method to Address objects. If 'House Number' field value
  exceeds 20 chars, the validation method will throw an appropriate error.
- Add two new metadata fields via Config Manager (already done in live):
  - OrganisationId (Integer, untick Enable on User Interface & ENable on Search Index)
  - AccountId (Integer, untick Enable on User Interface & ENable on Search Index)  
- Run the following scripts:
  - Transaction_Batch_Core_1112011.sql
  - indirect_contact_points_view.sql
  - StaticDataUpdateCoreTables_01112011.sql
  - missing_aurora_fields_upgrade_script.sql
  - missing_aurora_fields_upgrade_script_2.sql
  - multi_client_aurora_map_updates.sql
  - ref_company_updates_28112011.sql
- Static Data Update. Requires StaticDataUpdateCoreTables_01112011.sql in core package
- Fix for account matching during indexing. Requires CCLA_CORE.  

Version 1.3.0 (30/11/11)
------------------------
- Changed Client/PersonAuroraMap DAO objects to allow all new field values to be null.
- Run the following scripts:
  - missing_aurora_fields_upgrade_script_3.sql

Version 1.3.1 (10/01/2012)
--------------------------
- Updated aurora sdu account to check for mandate accounts and create them first if necessary.
- Added methods for working out share class id for a given account.
- Added value for new Iris env. var Iris_itemLockPoolId, to ensure pooled DB connections are
  used when updating Iris item locks. Requires latest version of IrisCore deployed before
  this will come into play (v1.4.4)
- Added new SystemConfigVars to control ClientServices UpdateFilter behaviour:
  - DocMeta_AutoCompleteDocDate
  - DocMeta_AutoPadClientNumber
  - DocMeta_AutoPadAccountNumber
- Added BigDecimal Support
- Added new CCLAUtils methods: 
  - getBinderBigDecimalValue()
  - getResultSetBigDecimalValue()
  These should always be used in preference of the getFloatValue() methods, if the numbers
  are to be used in any sort of arithmetic operations. Float types do not have guaranteed
  accuracy.
- Ensure 'CCLA' directory is created in the 'Spool' output folder on the Aurora machine,
  and a 'CCLA' directory is present at \\ccla-fs01\CCLA Electronic Documents\Aurora\
- Run the following scripts:
  - new_config_vars_12122011.sql
  - seg_client_updates_02122011.sql
  - FundBankAccount_Schema_22122011.sql
  - aml_ref_data_load_20111103.sql
  - community_first_updates_11012012.sql
  - StaticDataProcessing_24102011.sql
- Added new Fund_Bank_Account schema
- Added Fund Bank Details
- Added AURORA_UseAbbreviatedFullName cfg to control which full name to use when creating the correspondent.

Version 1.3.2 (12/01/2012)
--------------------------
- Refactored Transaction Batch to use Bank_Account_ID instead of Sort_Code or Bank_Account_No. 
  Requires "Transaction_Batch_Update_12012012.sql" to be executed.
- Fixed bug in AuthenticationScoreUtils which prevented the 'Identity Checked' element attribute 
  being updated from failed to passed
- Run the following scripts:
  - Identity_Check_attribute_fix.sql
  - Transaction_Batch_Update_12012012.sql

Version 1.3.3 (31/01/2012)
--------------------------
- Added extra validation check to BankAccount instances: the Short Name must now match the
  Building Society Number, when the latter is non-null.
- Fixed incorrect exception when adding/updating Aurora Accounts, when EITHER income/withdrawal 
  bank account short name fields aren't set, regardless of there being an income/withdrawal bank 
  account being set. 
- Client and Account data values are now copied against CREATE/UPDATE_CLIENT and CREATE/UPDATE_CORR
  instructions, so they will be displayed in pending SDU Instruction listings.
- 'Date Executed' date is now stored against SDU instructions after they are successfully executed
  in Aurora.
- Suppressed xUnits field formatting for 'Invoice' Document Classes in Iris. See env. var 
  CCLA_CS_InvoiceDocClasses for the list of Doc Classes considered to be Invoices.
- Aurora Client Data Protection flag is now being set via Create/Update Aurora Client instructions.
  Is set based on the Data Protection flag of the nominated Correspondent.
- Removed use of separate Aurora Correspondent Code sequence, when generating new Aurora Correspondent
  records. The person's PERSON_ID is now used instead, unless they already have a Correspondent Code
  linked to them. In this case their existing Correspondent Code is used instead.
- Switched on flag AURORA_PersonSingleCorrCode. This ensures that a Person record will use the same
  Correspondent Code throughout all Aurora Company DBs.
- Amended Correspondent table constraints, so that null Correspondent Codes are no longer permitted.
  New Correspondent links will now allocate a Correspondent Code automatically as per the rules above.
- Run the following scripts:
  - stp_data_updates_13012012.sql
  - ucm_db_to_central_db_migration_27012012.sql
- Updated PSDF end of day classes. Backwards compatible with older version of CCLATransaction.
- Refactored sequence names and locations

Version 1.3.4 (08/02/2012)
--------------------------
- Removed redundant templates, services for unused views.
- Renamed views to v_[view_name]
  - Requires ucm_db_to_central_db_migration_view_01022012.sql in CCLA_CORE
- Community First Change Request 
  - requires community_first_updates_01022012.sql
- Added DAOs for creating new Investments, Donations and their associated Fund/TTLA Allocations.
- Amended Account.getIVSCheck method to return a status of 'UNKNOWN', if there are no Failed IVS 
  persons connected to an Account and 1 or more persons with Unknown IVS status.

Version 1.3.5 (10/02/2012)
--------------------------
- Run the following scripts:
  - community_first_updates_09022012.sql
- Added Global Relation Check Override flag (see CCLA_CS_GlobalRelationCheckOverride env var)
  When switched on, Relation Checks will always return true regardless of relation status.

Version 1.3.6 (13/02/2012)
--------------------------
- Fixed issue with PSDF Transfer instructions being double counted.
- Run the following scripts:
  - community_first_updates_13022012.sql 


Version 1.3.7 (15/02/12)
------------------------
- Added Donor Subscription Form generation from Investment Screens.
- Changes to Account data queries (see CCLA_ClientServices readme for details). Added new
  methods and parameters to Account DAO fetch methods to support the new data queries.
  This should ensure auditing entries against the ACCOUNT table no longer contain spurious
  column data from other tables.

Version 1.3.8 (17/02/12)
------------------------
- Fixed PERSON_ACCOUNT_CODE mapping in Person DAO.
- Amended Form fetch queries used on Enrolment Info page to return dDocName values
- Organisation Account Codes will now ‘fix’ themselves to use a newly-allocated Aurora Client 
  Number as the numeric suffix. This applies to bulk-loaded Organisation records that enrol 
  to CCLA at a later date (e.g. GuideStar records)

Version 1.3.9 (20/02/12)
------------------------
- Property ID externalization

Version 1.4.0 (13/03/2012)
--------------------------
- **Fixed Pooled DB Connection config to use 'CCLA' user, instead of 'UCMADMIN'. If there are
  issues, switch the ECS_CONN_POOL_USER_CCLA_POOL1 var back to 'UCMADMIN' to restore
  to previous config.
	** REVERTED back to UCMADMIN for now, causing issues with GetNextBundle
	and permissions in Iris, possibly other things!
- Run the following scripts:
  - AlterScript_CommunityFirst.sql
  - CommunityFirst_BaseAccount_Attribute_06032012.sql
  - invoice_fields.sql
  - element_attribute_applied_updates_08032012.sql
  - form_updates_08032012.sql  
- Updated Subscription Type Names:
  - Endowment (Eligible for Government Match)
  - Endowment (Not Eligible for Government Match)
  - Gift Aid (Grants and Social Investments)
- Changed the NumberToWords.convertToGBP method to use the word "and" in the conversion  
- Added element locking functionality when updating sets of relations. Can be toggled
  using CCLA_ELEMENT_LOCK_ENABLED env flag. Disabled for now
- Refactored element_attribute_applied and form tables, queries and code to use GUID's instead 
  of DOC_ID's
- Fixed bug in Client Aurora Mapping generation - Contributor Code, Type Code and 
  Sub-Division codes are now filled in for new Client Aurora map entries.
- Tweaked behaviour of Aurora Client Number generation to ensure it is optional for new
  Organisations. Users are now permitted to create new Organisations without specifying a
  Company (and therefore allocating them an Aurora Client Number)
- New external JavaScript files available:
  - ccla_common.js
  - ccla_indexing.js
  See include ccla_common_js.

Version 1.4.1 (14/03/2012)
--------------------------
- Added COMM_SIGNATURE sequence fix
  - Run script ccla_commSignature_fix_14032012.sql.
- Disable ElementLockManager properly and fixed potential null pointer exception.

Version 1.4.2 (23/03/2012)
--------------------------
- Fixed issue with TTLA percentage to amount calculation and always use the amount column 
  when populating the Subscription form.
- Added Account subtitle length truncation to 40 characters when building Aurora Account
  objects for creation/update.
- Run the following scripts:
  - extra_person_signature_updates_16032012.sql (CCLA_SigChecking)
- Migration of the INTERACTION schema
  -- Run following scripts: 
     InteractionDBMigration_21-03-2012.sql
     InteractionDBData_21-03-2012.sql
     Redundant_Live_Sequence_21-03-2012.sql
	 comm_first_updates_16032012.sql
- Fixed ordering on ttla allocation in donor subscription form.
- Further updates since 23/03/2012:
  - Fixed spurious inserts of ' and ' in the NumbersToWords function.  

Version 1.4.3 (23/04/2012)
------------------
- Refactored Instruction and Comm code/queries and tables to use docGuids instead of dIDs
- Major refactoring/data migration for Subscription/Contribution schema. Added new Payment
  tables (currently unused)
- Run the following scripts:  
 - Comm_Instruction_Doc_Guid_Scripts.sql  
 - CommunityFirst_8.9_to_8.11_v3.sql
 - CCLA_ACCOUNTS_schema.sql
 - ParentDocGUID.sql
- Refactored caching and ensure correct Facade is used.  
- Person name fields no longer need to be set for CREATE_CORR and UPDATE_CORR instructions.
  This has been hot-fixed in live by copying over the latest build of the class file
  com.ecs.ucm.ccla.aurora.AuroraCorrespondentUtils into CCLA_Core.jar
-- added idcToken for 11g migration
- Decoupled database and facade for 11g migration
  - Requires Datapump to be performed.
 - Run Script (Requires Datapump to be performed first and all other existing scripts): 
  - DB_DECOUPLING_1.sql
  - DB_DECOUPLING_2.sql
  - Config Update: remove the view vCCLA_CAMPAIGNS from Config Manager.
- Added new column to ACCOUNT table: ACC_ACCOUNT_CODE. Will function in a similar way to
  PERSON_ACCOUNT_CODE and ORG_ACCOUNT_CODE. Still requires population.
- Added new CCLA_ACCOUNT element table, and associated relations (not populated)
- Modified default Aurora Client data field values:
  - Send Statements Every Month - defaults to False for all new clients now
  - When PSIC Company is selected, the months March, June, September and December are selected
  - For other companies, only January is selected
-- refactored class loading code for 11g migration
  
Version 1.4.4 (27/04/2012)
------------------
- Reverted back caching to use old caching due to problems with startup on live db  
  

Version 1.4.5 (30/04/2012)
--------------------------
- Fixed iris indexing for 11g. DataResultSet.getValueByName doesn't work as expected on 11g.

Version 1.4.6 (10/05/2012)
--------------------------
- Changed how Aurora Account instances are populated in STP AmendAccount/AddAccount web
  service calls: Mandated Company field is always populated for accounts with an Inc. Dist.
  Method of RETN (Retain Interest). The mandated company will match the Account's Fund Company.
- Organisation Names are now truncated to 80 characters when converted to Aurora Client Names.
- Added extra logging calls to Amend/Create Aurora STP web services.
- Removed duplication of House Name in Street field of Aurora address data fields, where Street
  isn't specified in an Central DB address.
- Updated Account DAO validation to force Income Distribution Method to REIN when an Accumulation 
  Fund is selected.
- Run the following scripts:
  - IncomeDistMethod_Fix_08052012.sql
- Switched Experian Authenticate server address from ccla-vs-ap01 to ccla-vs-ap03. Port number 
  also changed from 2022 to 2021
  
Version 1.4.7 (18/05/2012)
--------------------------
- Added new env. switch CCLA_forceGeneratedDocURLsToLowerCase. If present, this will force any
  URLs generated by DocumentUtils.generateDocUrl method to lower-case. Fixes an issue with
  capital letters appearing in these strings, yielding invalid links in a Linux environment.
- Forced Mandated Company field to be set on all Aurora accounts.  
  
Future version (incomplete features, suspended work or requires sign-off)
----------------------------------
- Run the following scripts:  
  - sdaudit_synonyms_and_grants.sql
  - DocMeta.sql
  - aml_tracker_override.sql
  - xOrganisationId_and_xAccountId_fix.sql
- Preadvice/Dicondin CR work 
  requires Preadvice_Dicondins_Instruction_data_15072011.sql


Version 1.4.8 (24/05/2012)
-- This component has been converted to use the new ECS build process. 
-- Any changes must be put in the CHANGELOG not this readme. 
-- Configuration/installation notes can still be added to the top of this file

************** END **************