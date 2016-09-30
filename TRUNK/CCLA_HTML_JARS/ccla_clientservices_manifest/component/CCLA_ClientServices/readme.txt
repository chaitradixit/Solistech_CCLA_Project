CCLA_ClientServices Component Release v2.4.8
================================================================================
  Full Release Name: 2016_07_07 v2.4.8 (Build 22240)

      Date: 07/31/2014 04:37 PM
    Author: tm
  Location: http://ecs-svn/stellent/CLIENTS/CCLA/Components/CCLA_ClientServices/tags/v2.4.8
           
(c) Extended Content Solutions Limited 2016
================================================================================
﻿CCLA_ClientServices Component Release v2.2.6
================================================================================
  Full Release Name: 2014_01_05 v2.2.6 (Build 18440)

      Date: 05/25/2012 02:38 PM
    Author: PAULT
  Location: http://ecs-svn/stellent/CLIENTS/CCLA/Components/CCLA_ClientServices/tags/v2.2.6
           
(c) Extended Content Solutions Limited 2014
================================================================================
﻿CCLA_ClientServices Component
=============================

Manages all assets for CCLA Client Services.

Dependencies
------------
-ECS_BaseUtils 1.5.9 or greater
-CCLA_MailHandling
-CCLA_reports (contains web services)

Installation
------------
-On first time install, you must run the CCLA_CS_INIT_SEQUENCES service
 and initialise all the database sequences.
-Requires 5 DB tables. To create these, you must run the appropriate script
 in the <component dir>/scripts directory.
-Requires AML database provider configured. The provider.hda file can be
 found in the <component dir>/providers/aml directory. It is recommended you
 set up the provider via the web interface and then amend the generated file
 to match this one.
-Requires the following new metadata fields:
 -CorrespondentCode
 -ProcessId
-Requires the following new xDocumentClass value:
 -CLIENTCONF
-In order to use the provider above, you must have the AML_UCM database account
 configured on the AML database. There is a script for this in the scripts/ms
 directory.
-To use the Aurora Form Printing capability, the UCM Windows service must be set
 to log in as an AD user, rather than Local System. It is recommended that you
 use the UCMAuroraWebService AD account for this.

Build History
-------------

Version 1.0.0 (09/10/2009)
--------------------------
-First deployment to live

Version 1.0.1 (12/10/2009)
--------------------------
-Added Oracle database script for table generation (CMU bundle was unreliable)

Version 1.0.2 (14/10/2009)
--------------------------
-Added AML checking
-Improving question recording interface
-Externalised option list values to environment file

Version 1.0.3 (16/10/2009)
--------------------------
-Closed accounts are now filtered out of the lists, this fixes a bug
 caused when any closed accounts are fetched.
 
Version 1.0.4 (30/10/2009)
--------------------------
-Aurora Form Printing integration, includes service methods for spool file
 generation and form printing.
-New requirement for UCM Windows Service: must log on using AD account
-New env parameters used for location of Form Printer device and temporary
 location for spool files to be printed.
-Empty accounts are no longer displayed in account lists.

Version 1.0.5 (02/11/2009)
--------------------------
-Made correspondent confirmation optional if user selects certain activity
 types (Outgoing call, Amendment)
-Updated spool file creation code to account for updated Create!Form template
 supplied by Kainos
 
Version 1.0.6 (09/11/2009)
--------------------------
-DB schema updates - run the SQL script in the file:
 sql_update_fund_transfers_table_20091104.txt
 to apply latest changes, if this is a component update. If you are installing
 a new build of the component these updates are included in the base SQL file.
-Updated fund transfer UI and spool file creation to include an 'Undecided'
 transfer option, this leaves the relevant boxes on the transfer form blank,
 allowing clients to specifiy their own transfer preferences.
-New service: CCLA_CS_PROCESS_LISTING. Lists all processes in a S&F display.
-Updated spool file generation code to add covering letter

Version 1.0.7 (17/11/2009)
--------------------------
-Added checkin filter class and helper methods to auto-complete metadata fields
 for passed xFormId values

Version 1.0.9 (24/11/2009)
--------------------------
- Fixed bug where a barcode with A-Z characters from ODC causes the form data query
qClientServices_GetExtendedFormDataById to crash because it's expecting numbers only.

Version 1.1.0 (30/11/09)
--------------------------
- Added correspondent details and address caching (CS_PERSON_DETAILS and CS_ADDRESSES)
- Added new communications log (uses CS_CORRESPONDENT_COMMS
- Added QRS flag query for each account (queries QRS provider db)
- Added 'Not interested' option for fund transfer options (for cold calling purposes)

Version 1.1.1 (10/12/09)
--------------------------
- Added Suspended flag for batches
- Add indications of when a client confirmed has been received
- Changed new Process creation to account for client ID as well as correspondent/process 
  name

Version 1.1.2 (11/12/09)
--------------------------
- Fixed bug with web service calls acting on the form id rather than the form id 
  AND the client ID
  - Affected services:
	CCLA_CS_MARK_FUND_TRANSFERS_CLIENT_CONFIRMED/MarkFundTransferFormConfirmed (web service)
	CCLA_CS_GET_REPONSE_FUND_TRANSFERS_BY_FORM_ID/GetResponseFundTransfersByForm (web service) 
  
Version 1.1.3 (15/12/09)
------------------------
- Backed out previous changes to Process creation. This was causing duplicate processes/
  transfers in the database.
- Amended queries used for fetching pending fund transfers to only return transfers with transfer
  type of 'all' or 'amount'. These queries are called by web services used by SPP.
- Fixed code bug in checkin filter which prevented auto-completion of form data

Version 1.1.4 (16/12/09)
------------------------
- Enhancements made Fund transfers report - added CS_FUND_TRANSFERS_NOTES table

Version 1.1.5 (18/12/09)
------------------------
- Modified qClientServices_GetResponseFundTransfersByFormId and qClientServices_GetPendingFundTransfers 
  to not include rows with ACCOUNT_STATUS of 'suspended'.
- Added 6 new columns to CS_FUND_TRANSFERS table. Run the oracle script:
  sql_update_fund_transfers_table_20091217.txt
  to add the new columns and set default flag values.
- Modified qClientServices_GetPendingFundTransfers and qClientServices_GetFundTransfersRequiringAction to
  only include rows with TRANSFER_TYPE='amount' or 'all'
  
Version 1.1.6 (30/12/09)
------------------------
- Amended query GetUnbatchedTransfersByFund to filter out unconfirmed/unsuspended transfers

Version 1.2.0 (29/01/10)
------------------------
- New templates, services and queries to support AML checking 2010.
  -Required UCM config changes:
   -Add new xSource value: Form Generator
   -Update Entry step of MandateUpload workflow:
	<$if dRevLabel > 1 and not (xSource like "Form Generator")$>
   -Add new document class: AMLCHASER
   -Add new view for Campaigns
   
Version 1.2.1 (23/02/10)
------------------------
- Schema and query updates to new Client Services UI and Mandate Form generation
- Account similarity check is used before displaying account information on generated
  Mandate forms. If accounts are not similar, no account information is printed.

Version 1.2.2 (10/03/10)
------------------------
- Updates to Mandate form spool file generation to handle CBF forms
- Basic support for account actions

Version 1.2.3 (11/03/10)
------------------------
- Amendment to Mandate form spool file generation: now features blank page padding at
  the end of the form.
  
Version 1.2.4 (19/03/10)
------------------------
- Fixed bug in FormHandler code, which was appending xDocumentDate values in an incorrect
  format.

Version 1.2.5 (23/03/10)
------------------------
- Added releaseConnection() method call in the Process Action Queue Monitor. This is an
  attempt to resolve the database connection leak issue.
- Amended new account action handler code to generate external account numbers.

Version 1.2.6 (31/03/10)
------------------------
- Updated form status pop-up screen used in Iris indexing interface. Allows the user to
  set data flags, instead of changing form status.
- Fixed bug in Form ID validation, which causes valid form IDs to get stripped when a
  barcoded document is up-revisioned. 

Version 1.2.7 (08/04/10)
------------------------
- Updated CCLA_FORMS table to support extra data flags. Form status pop-up screen has
  been enhanced to allow updating of new flags.
- Pop-up screen will auto-update the xDocumentClass value for forms marked as invalid.
- Redesign of CCLA_ACTIVITIES table. No longer contains ACTIVITY_OUTCOME and confirmation
  fields.
  
Version 1.2.8 (09/04/10)
------------------------
- Added new 'Checks completed' action to AML Check process
- Allowed for process action queue interval of less than 1 second

Version 1.2.9 (12/04/10)
------------------------
- Fixed getExtendedClientInfo method to throw exception if client info lookup fails.

Version 1.3.0 (13/04/10)
------------------------
- Restricted access to 'Cancel', 'Exclude' and 'Enroll' process actions
- Automatic updating of mandate process status when all active forms are flagged as 
  returned. Status set to 'Forms returned'
- Added new universal action to AML Process: 'Updated form returned'
- Added new Category field and classification flags to Interaction entities

Version 1.3.1 (21/04/10)
------------------------
- Added 'Returned mail' checkbox to Update Form Status pop-up. Automatically re-indexes
  parent document as CSLETTERS.
- Polished alpha version of Interaction logging in Client Services UI. Previous
  interactions are now displayed on Client/Person Info views.

Version 1.3.2 (23/04/10)
------------------------
- Minor amendments to Interaction Logging system. 'Last updated' field for interactions
  is now stored correctly.
- 'Returned mail' mandate documents are no longer automatically re-indexed to CSLETTERS
 
Version 1.3.3 (30/04/10) - DJ
------------------------
- Exposed IS_QUERY column of CCLA_INTERACTIONS table to all end users of Interaction 
  Listing S&F screen
- Added JOB_ID column to CCLA_INTERACTIONS table, and adjusted relevant queries and exposed
  it on the Interactions S&F page, visible only to COO.
- Created a 'quick edit' button/template page on Interactions S&F to allow COO to update 
  the freetext field and the flag checkboxes 
  
Version 1.3.4 (05/05/10) - DJ
------------------------
- Modifications made to the Interactions Listing S&F screen:
	- Removed the no longer needed 'Type' column
	- Add the Category column
	- Squeeze some extra space out of the other columns in the table

Version 1.3.5 (13/05/10)
------------------------
- Updates the the AML Summary Report:
	- Legacy data no longer displayed by default
	- 'Show legacy data' link must be clicked to display legacy stats

Version 1.3.6 (17/05/10)
------------------------
- Increased visibility of Interaction Listing update controls. Can now be seen by the
  following roles:
  - admin
  - WF_Scanning Manager
- Improved Interaction Listing page to show summary of categories. The listed categories
  in the filter are now a reflection of existing categories, rather than the fixed list
  used in the Interaction Logging interface.
- Interaction Quick-Edit popup now features a canned list of 'job codes' which can be
  selected to record basic actions for existing interactions.
  
Version 1.3.7 (18/05/10)
------------------------
- Improved AML Summary report to show remaining legacy AML clients who weren't included
  as part of the new campaign.
- Added 'With Client' to option list on Quick-Edit Interaction popup.

Version 1.3.8 (28/05/10)
------------------------
- Added extra functionality to UpdateFilter class to auto-complete xCompany field, if
  Fund code is present.
  
Version 1.3.9 (01/06/10)
------------------------
- Fixed bug in FundCache class which was causing transaction rollbacks during document
  checkin, just after a server restart.

Version 1.4.0 (16/06/10)
------------------------
- Updated account number auto-completion used on returned AUTOMANDS to use correct number
  of characters (based on external account number string)
  
Version 1.5.0a (11/08/10)
-------------------------
- Intermediate build of component with queries, code and views pointing at new schema.
  Some previous views/queries still remain.

Version 1.5.1a (02/10/10)
-------------------------
- Initial go-live with new screens. Very recent update of schema hasn't yet been reflected
  in most pages/code/queries.
- Some steps taken to refactor Form/Activity/Process objects to integrate with new schema.
  Organisations, Persons, Accounts, Funds, Relations still pending refactor.
- Overriden HOME_PAGE template to point at custom CCLA Home Page.

Version 1.5.2 (14/10/10)
-------------------------
- Most queries/codebase now updated to point at new schema tables
- Homepage features new 'Answer a call' link
- DJ: Ammended view CCLA_ACTIVITY_NOTE, CCLA_PROCESS_LAST_ACTIVITY, CCLA_PROCESS_ACTIVITY 
  for CCLA Schema

Version 1.5.3 (18/10/10)
------------------------
- Fixed Atlas issues UCI00014, UCI00013, UCI00010, UCI00009

Version 1.5.4 (19/10/10)
------------------------
- Added 'Flagged Items Report' link to Reports menu
- Amended GetAccountAMLStatus method to return Entity Check status even if an account number
  isn't specified.
 
Version 1.5.6 (22/10/10)
------------------------
- Quick fix to authenticationscore.get plus hardcode of amber scores (as cache is not 
  currently working for them)

Version 1.5.7 (27/10/10)
------------------------
- permanent fix for amber scores
- changed logic for account codes for person/org to include client/correspondent number 
  from aurora, added account creation and addition of bank accounts
  
Version 1.5.8 (31/10/10)
------------------------
- Added support for new view, ACCOUNT_EXTENDED_CLIENT. Same as ACCOUNT_EXTENDED, but
  also includes the owning client name.
- Improved display of accounts on Person Info page to show them grouped by Client.
- Changed layout and added new search fields on global lookup view.

Version 1.5.9 (12/11/10)
------------------------
- New column in PERSON table, SOURCE_ID, used to record the Person's source using the
  same key table as the Organisation Source ID. Run the corresponding SQL update
  scripts to create the new column/constraint.
- Intermediate build of ClientServices code base. Doesn't contain recent updates for
  new schema.  
- Fixed bug caused by using incorrect sequence to assign workflow IDs to CLIENTQUERY
  jobs started by Interactions.
- Removed IVS check requirement for Auth. Trustees linked to accounts 

Version 1.6.0 (16/11/10)
------------------------
- Deployment of updated database schema (many changes - see script directory)
- Added warning for unsaved address data when updating a Person page
  
Version 1.6.1 (19/11/10)
------------------------
- Bulk update screens
- combined person/org creation screens

Version 1.6.2 (20/11/10)
------------------------
- Updated Client Query job screen to use new complaint category/sub-category lists.
- Sub-cause ID and the job creator are being passed at the back end for Query jobs, 
  but not currently mapped to SPP fields due to an error on SPP side.
  
Version 1.6.3 (22/11/10)
---------------------------
- Added new account only basic service so that the create person screen is not polluted
  with existing people related to account
- increased width of fund column on bulk update screen.

Version 1.6.4 (29/11/10)
-----------------
- Re-implemented GetClientQRSData web service. Requires rebinding of web service to UCM
  service CCLA_CS_GET_CLIENT_QRS_DATA, and new table CCLA_QRS_ACCOUNTS.
- Added check on new Sub-Case client query field. If sub-causes are available for the 
  selected cause, one must be selected before the interaction/query can be processed.
- Added 'Do not contact' checkbox to person create and edit screens.  Also added big red 'DECEASED'
  indication on person info panel for when someone is marked as deceased.  
- Fixed issue with Person Edit screens where default address is not displayed. (UCI00035)
- Fixed various issues with Form/Document integration:
  -xCompany field now automatically completed when a valid Form ID is present on the document
  -Document account data now automatically completed for single-account AUTOMAND forms
  -Multi-account AUTOMAND forms will now create all required child documents on checkin/update
  -AML Process Handler now correctly detects when all latest forms have been returned for a
   particular process, and updates the Process Status accordingly (UCI00043).
   
Version 1.6.5 (1/12/10)
------------------

Version 1.6.6 (02/12/10)
-----------------
- Added checking for client-org relationships when starting a starting a clientquery (UCI00054)
- Fixed issue when editing an interaction and un-checking the follow-up flag. (UCI00055) 

Version 1.6.7 (07/12/10)
-----------------
- Fixed duplicate org-person relationship being entered from sync (UCI00057)
- Updated cfg file to specify default relationship as 86 instead of 84 (UCI00061)
- added logic so that when last name/age/experian address is changed the status of person changes to AMBER
- added check for company code/id on create account page so you can only add account/funds for the relevant company

Version 1.6.8 (08/12/10)
-----------------
- Fixed bug when creating person, caused by age change check code

Version 1.6.9 (15/12/10)
-----------------
- Document Class caching script is now present in the CCLA menubar include
- Added extra entry CCLA_CS_AccAuroraUpdateTypes env. var
- Fixed tab index (UCI00067).
- Fixed Person search name not being copied into Add New screen (UCI00070)
- Updated Select (Person-entity, entity-Person) relationship panels to edit (UCI00071). 
- QRS Client support is back. 
  - Run the script SCHEMA_UPDATES_131210.sql to create the required tables. 
  - The CSV file QRS Accounts Data 131210.csv must be imported into the new table.
  - Update the SPP WSDL configuration to point the web service GetClientQRSData at the
    UCM service CCLA_CS_GET_CLIENT_QRS_DATA.
- UCI00044 Updated ajax request to POST instead of GET
- UCI00073 Bank Account searching now accepts Sort Code as an extra parameter.
- UCI00074 Fixed adding entity from the select-person-entity screens 
- Document Class JavaScript cache is now cached as an IdocScript include every 30 mins.
- UCI00069 Various fixes to the interaction screens.
- UCI00065 ability to set default inc and with bank accounts

Version 1.7.0 (16/12/10)
-----------------
- Moved Aurora web service auth credential parameters from CCLA_Reports into this 
  component 
- Changed most forms to have method of POST (only on standard html forms, not AJAX calls) UCI00086
- added ability to add multiple flags to an account (UCI00076)


Version 1.7.1 (24/12/10)
-----------------
- Re-fixed UCI00054 so that the interaction query is not selectable if an organisation is not selected. 
- UCI00084 added "quick add person" to bulk update entity screen.
- UCI00088 Bank account is now validated against the aurora ws. 
- UCI00029 Adding masking for DOB fields. *Not working*

Version 1.7.2 (14/01/11)
-----------------
- UCI00029 Added datemasking to report fields and fixed issue with keypad numbers
- UCI00094 Fixed error when loading Organisation pages for organisations without Category set.
- UCI00095 Fixed qClientServices_InsertValidationRecord query.
- Started work on Signature capture. UI elements disabled for this release.

Version 1.7.3 (21/01/11)
-----------------
- Fix to validatePerson to allow insertion of crockfords data when user has no previous valdiation record
- UCI00101 removed alert() from Bulk Update screen

Version 1.7.4 (?)
-----------------
- Significant extensions to the schema to deal with new Comm Processing model, Share Class support
  and Income Distribution calculations
  - CCLA_DB_Upgrade_v2.3_v2.4.sql
  - CCLA_DB_Upgrade_v2.4_v2.5.sql
  - SCHEMA_UPDATES_180111.txt
- UCI00104 - Fixed audit entry for usernames containing apostrophe
- UCI00107 - Fixed salutation fields.
- CCLA_TITLE_DB_UPGRADE_270111.sql updates revd entry to have a male/female gender.

Version 1.7.5 (07/02/2011)
-----------------
Added signature capture and checking functionality to the user edit / info pages
- UCI00114

Version 1.7.6 (10/02/2011)
-----------------
- UCI00115 Fixed bug when searching on Charity Ref numbers and other unique identifiers
- UCI00102 Rewrite using new DB schema, Requires CCLA_FundProcessingDates_Schema_100211.sql in CS
- Refactored codebase package/class names

Version 1.7.7 (11/02/2011)
--------------------------
- UCI00102 Refactored UCI00102 to SPP_INTEGRATION

Version 1.7.8 (17/02/2011)
--------------------------
- UCI00117 - Validation check for gender/Title Combinations
- UCI00113 - Added timestamp to status check text to prevent caching
- Run the following SQL against the database on deployment:
	ALTER TABLE SDAUDIT RENAME COLUMN "TIMESTAMP" TO AUDIT_DATE;
  'Timestamp' is a reserved word in Oracle SQL so it needs to be changed.
- Added auditing to ContactPoint and Element objects
- Implemented Element Attribute handling
- Work started on new Attribute-based Entity Verification. UI elements
  disabled for this release.
- UCI00118 - Fixed duplicate person records.
	
Version 1.7.9 (17/03/2011)
-------------
- Removed 'Person-Account Relations' grid on Entity Info page (no longer relevant)
- UCI00130 Order lists of related Persons by surname
- Refactored CCLA_IDENTITY_* tables to IDENTITY_* tables
  Requires the following SQL:
	CCLA_Identity_Check_22022011_RunAs_CCLA_p1.SQL
	CCLA_Identity_Check_22022011_RunAs_SYS_p2.SQL
 	CCLA_Identity_Check_22022011_RunAs_UCM_p3.SQL
- Added Person entity check attributes
- UCI00135 Fixed issue that prevents updating external identifiers for Organisations
- Various schema changes and new reference data, run the scripts:
	sql_updates_18022011.sql
	sql_updates_07032011.sql
- Refactored package name com.ecs.stellent.ccla.clientservices.experian to
  com.ecs.ucm.ccla.experian. All asociated service classes have been updated.
- Moved all com.ecs.ucm.ccla classes to CCLA_Core codebase
- UCI00136 Client Query Boost
- UCI00148 Process Date Amendments

Version 1.8.0 (07/04/2011)
--------------------------
- UCI00156 Error calling Experian web service (fixed silent error in doValidate() method)
- Granted WF_Scanning Manager members access to Admin menu
- UCI00159 Add new totals to Parking Lot Bundles page 
- UCI00163 - Remove old Extended Content Report 
- UCI00164 - Create new Duplicate report
- Updated page titles for Organisation, Person, Account, and bulk-update pages to appear
  more consistent and display the element name first.
- Added configurable list of SPP Query Cause IDs which will trigger the Client Priority
  'Boost' rule. This can be found as CCLA_CS_ClientBoostQueryCauseIds
- Amended new Entity Checking/Verification panel to restrict access to attributes by
  security group. The new panel is still disabled for this release.
  
Version 1.8.1 (08/04/11)
------------------------
- update to two queries to change SHARE_CLASS to SHARE_CLASS_ID

Version 1.8.2 (18/04/11)
------------------------
- Updated query qClientServices_GetElementAttributeApplied so it only returns data
  from the ELEMENT_ATTRIBUTE_APPLIED table. This ensures that the audit entries do
  not end up logging data from joined reference tables.
- UCI00184 Income bank account details being returned in Iris if no withdrawal bank 
  account. Has been fixed to return withdrawal bank account ONLY, zeroes otherwise.  

Version 1.8.3 (04/05/11)
------------------------
- Run the following scripts:
	- clientservices_changes_19_April.sql
	- element-location-changes-28-April.sql
- Switched on new Entity Verification system

Version 1.8.4 (07/05/11)
------------------------
- Switched Entity Verification System off again
- Disabled 'Create new address' button after AJAX submission
- Run the following scripts:
	- /Trigger Updates - 04052011/*.sql
  	- new_campaign_schema_static_data_load_030511.sql

Version 1.8.5 (09/05/11)
------------------------
- Extra updates to PSDF campaign/spool file generation

Version 1.8.6 (09/05/11)
------------------------
- Extra updates to PSDF campaign/spool file generation

Version 1.8.7 (10/05/11)
------------------------
- Amended PSDF Spool File:
  - Include bank details via Aurora web service lookup
  - Bank account/sort code values now padded out
  - Include FormID and PageNumber after Report headers
  - Implements new dynamic layout
- Updated Required Details Checklist:
  - Account names/subtitles must be non-null
  
Version 1.8.8 (11/05/11)
------------------------
- Amended PSDF Spool File:
  - Fixed Org address so it gets crammed into the 21x4 
    grid area.  

Version 1.8.9 (13/05/11)
------------------------
- Run the following scripts:
  - elem_attrib_updates_11052011.sql
- PI companion accounts are now automatically generated on creation
  of a PC account.
- Added service CCLA_CS_CREATE_COMPANIONS_FOR_ORPHANED_PC_FUNDS. 
  Call from the URL to generate companion PI fund accounts for 
  existing PC funds
- Aurora Client Number mappings are now auto-generated for new 
  Organisations. Behaviour can be toggled using the following flags:
  -CCLA_CS_EnableClientNumberGeneration
  -CCLA_CS_ForceClientNumberGeneration

Version 1.9.0 (16/05/11)
------------------------
- Aurora Client Number mappings flags are now switched on.
- Changes to Entity fetch methods using Client Numbers, so Company
  values are only referred to when there are multiple Organisation ID
  mappings for the same Client Number.
  
Version 1.9.1 (18/05/11)
------------------------
- Fixed bug in address display, which prevented an address being
  displayed at all if there was no default address set.
- Minor amendments to signatory section of Non-LA PSDF Reg. form output
- Added switches to control display of new override panels:
  - CCLA_CS_EnableRelationCheckOverride
  - CCLA_CS_EnableAccountIVSCheckOverride

Version 1.9.2 (20/05/11)
------------------------
- PC/PI accounts now move from TEMP to PEND status automatically on return
  of their associated App. form.
- Fixed PSDF Reg. Form Handler to ensure enrolment process is marked as
  'Forms returned' once all PC accounts for the client become active. 
- Enabled new KYC override panels and Entity Checking functionality
- Added custom Error Page template
- Added relation check outcome to getAccountAMLStatus service method.
- Run the following scripts:
  - kyc_updates_11052011.sql
  - pdsf_activity_view.sql  

Version 1.9.3 (23/05/11)
------------------------
- PC/PI accounts now move from TEMP to OPEN status automatically on return
  of their associated App. form.  
- 'Generate Welcome Pack' buttons now available on Enrolment Info screen
- UCM Filters for handling form IDs has been extended to deal with non-
  singleton forms, i.e. forms that can be returned more than once.
- Account IVS checking has been amended to look for account-level overrides
  against related persons.
- An account IVS check will now fail if there are no authorising persons
  associated with the account.
  
Version 1.9.4 (24/05/11)
------------------------
- Minor amendments to Subs/Reg PSDF Spool File creation
- Minor query change to display PSDF Summary Report to include clients without intentions.  

Version 1.9.5 (25/05/11)
------------------------
- Added support for PSDF Additional Account creation

Version 1.9.6 (31/05/11)
------------------------
- Added admin links to instruction audit and process date calculator.
- Fixed Interaction Query SPP job trigger to ensure a Fund Code is always passed
  if a Fund Code/Account is selected for the interaction.

Version 1.9.8 (06/06/11)
------------------------
- Moved most of the UpdateFilter code from the SPP_INT UpdateFilter to the
  ClientServices one, so all metadata validation etc. is in one place.
- UpdateFilter extended to write over Fund Code/Dest. Fund Code values with
  the Account Number/Dest. Account Number value suffix, if present.
  e.g. '0001C' -> places 'C' in the Fund field regardless of the current Fund
  value.

Version 1.9.9 (24/06/11)
------------------------
- Added new service method call in CCLA_CS_HOME, which determines whether the
  current user is 'Mandate-only'. Requires latest build of SPP_INTEGRATION.
- The getEntityDataForInstruction service method now returns a ResultSet of
  all linked bank accounts for a given CCLA account.
- The first linked bank account is no longer assumed to be the 'nominated' one,
  if the env. var flag CCLA_UseStrictNominatedBankAccount is set. If set, one
  of the withdrawal accounts must be explicitly marked as nominated.
- UCI00214 - Changes to Fund Intentions - Requires 13_June_Intention_Update.sql SQL to be run.
- UCI00196
- UCI00210 Updated homepage to reflect new Mandate-Only user list. Requires SPP_INT
- consoladated validation methods into panelValidationExtra
- UCI00206 Process Date notifications
- UCI00216 Condins Process Date notifications
- Schema modification to BANK_ACCOUNT table. Run script:
  - bank_account_table_updates_22062011.sql
- UCI00202 Create Instruction from interactions. Run script:
  - Requires 16_June_interaction_instruction_data_load.sql
- Added support for forms:
  - Change of Dividend Payment
  - Additional Withdrawal Account
- All PSDF forms now feature an 'EmailIndemnityReceived' flag on the cover page.
  This is true if the client has sent in an EMAILINDEMNITY document type.
  
Version 2.0.0 (01/07/11)
------------------------
- Added new flag 'GenerateCSLetter' as a response parameter to the GetAccountAMLStatus
  service. Requires updates to the GetAccountAMLStatus and GetAccountKYCStatus web
  services.

Version 2.0.1 (14/07/11)
------------------------
- Amended homepage to reflect new Work Group assignment
- Added 'Work Groups Administration' link to Admin menu
- Added new Extended Content Report links to Reports menu
- UCI00231 Fixed Documents listing panel on Organisation Info page, ensures that
  documents will be displayed even if the Org doesn't have any accounts. 
- Menubar includes now cached in session scope

Version 2.0.2 (08/08/11)
------------------------
- UCI00235 Added 'Email' as a searchable field on the global Lookup screen
- UCI00236 Allow users to remove existing addresses
- UCI00233 Allow Account Flags to be batch-updated
- Fixed bug which prevented Document list being displayed on Record Interaction 
  screen
  
Version 2.0.3 (16/08/11)
------------------------
- Added new response vars to Account AML/KYC Status:
  - EntityCheckFailReason
  - RelationCheckFailReason
  Requires new WSDL configuration. See wsdl_config/16.08.11 folder
- Added new administrator menu link: Data Cache Administration. Allows inidividual
  data caches to be rebuilt via UI
- New pop-up menu for account listing rows, which is displayed on the Interaction 
  Recording and Enrolment screens. The 'Generate Forms' menu item allows a set of
  account forms to be generated on the fly.  
- New side bar menu link for enrolling clients to the Dio Loan campaign
- Added support for generation of various Dio Loan forms:
  - Application
  - Drawdown
  - Redemption
  
Version 2.0.4 (23/08/11)
------------------------
- Added timestamp to bank account lookup requests to ensure IE won't pull back
  local cached search results
- Improved Dio Loan campaign management, form generation and handling
  
Version 2.0.5 (31/08/11)
------------------------
- Updated Account Listing displays to show accounts grouped by their type
- Added requirement for 'Dio Loan Resolution' verification source to be set 
  against organisation before Dio Loan draw-down form can be generated
- Added limit checker to ensure a single client can't draw down more than
  £1m of loans.
- Updated available loan terms to be in 12 month increments, starting at
  12 months (REVERTED)
- Account listings on Organisation/Person Info pages are now shown grouped
  by Account Type, and collapsed by default
- Fixed spool file output directory to remove the PSIC prefix. All spool
  files generated previously into folders PSICCBF and PSICPSIC should be
  moved to the CBF and PSIC folders after this deployment.
- Added support for Email Indemnity form creation. Requires DB updates
  which are bundled with v1.2.6 of CCLA_Core
- Changed Dio Loan Application form Client Address to be printed in grid format
- Checkin the following PDF to UCM:
  - docs/ccla_dioloan_guide.pdf
  - Content ID: CCLA_DIOLOAN_GUIDE
  - DocType: System

Version 2.0.6 (22/09/11)
------------------------
- Removed Experian config vars, now live in CCLA_Core
- Further changes to Dio Loan Application form spool file output
- Added Account Agreement Type field to Add/Update Account screens

Version 2.0.7 (26/09/11)
------------------
- Changed default no. of Required Signatures for a Dio Loan account to 2
- Fixed bug which prevented Dio Loan application form being generated for
  clients with more than 1000 accounts
- FileStoreProvider Install: 
  Refer to http://extranet.extended-content.com/ucmxe/groups/public/@ccla/documents/case_document/xnet002765.pdf for details

Version 2.0.8 (18/11/11)
------------------------
- Added xUnits field formatting to UpdateFilter
- Added xDocumentDate value auto-completion to UpdateFilter. Behaviour
  controlled by new env. var CCLA_AutocompleteDocumentDate
- Allowed users to change the label applied to postal addresses
- Added help button feature and admin - Requires CCLA_CS_HELP_BUTTON_21102011.sql
- Removed 'deleted' documents from the recent documents display panel on the Org
  Info page.  
- Fix for account matching during indexing. Requires CCLA_CORE.  
- 'Aurora Account' flag now configurable by user on Add/Edit Account screen
- All Account attributes now visible/updatable on Edit Account screen
- Increased Search Results limit used on Lookup screen to return 25 orgs/persons/
  accounts instead of 15.
- A single Account/Org Correspondent relation can now be set as the nominated one
- Building Society Roll/Reference numbers can now be specified or added to existing
  bank accounts
- In the iris configuration, change the dynamic html to "iris_ccla_doc_class_with_help" 
  for xDocumentClass field for content type "Document" and "Child Document" 
- Loan Accounts aren't displayed on the Org Batch Update screen any more.
- Recent Instructions panel now visible on Org/Account Info screens.
- Client Numbers are now re-padded in Iris to match the zero-padding used by the Fund's
  Company. i.e. CBF client numbers always padded to 6 digits, others 5 digits.
- Fixed display bug on Account Info page which showed incorrect Company Code in Fund
  Info panel

Version 2.0.9 (30/11/11)
------------------------
- Account Number auto-padding now applied during metadata updates. This can be
  controlled by the CCLA_AutoPadAccountNumber flag.
- Added new env flags to control display and editing of Client/Person Aurora mapping
  data:
  - CCLA_DisplayNewAuroraMappingFields
  - CCLA_EnableAuroraMappingUpdates
- Fixed custom Error Page so it will be displayed correctly in pop-ups.
- Fixed bug when generating AuroraClient instances in the UCMForm.get(DataResultSet)
  method. This was preventing older form IDs indexing against documents
  
Version 2.1.0 (10/01/12)
------------------------
- Added trimming for person name when doing Experian validation to prevent unknown errors.
- Added trimming for person name after their record is updated, or a new one is added.
- Deprecated old services CCLA_CS_SEARCH_PERSON, CCLA_CS_SEARCH_CLIENT
- Replaced older env vars for controlling UpdateFilter behaviour with new SystemConfigVars
- Added timing log entries for all document-based Filter classes
- Enrolments now displayed again on Organisation Info screen
- Made the UpdateFilter more resilient, will now catch a NumberFormatException when trying
  to parse an invalid xAccountNumber/xClientNumber value, rather than throwing back to user.
- Client Number auto-padding now works providing the user specifies a Fund Code OR Company
  Code against a document
- Bulk Update screen now features 'Select All' and 'Select None' buttons, for selecting or
  de-selecting all add/remove relation checkboxes at once.
- Segregated Client enrolment now available. Run the following script:
  - seg_client_updates_02122011.sql
  This required additions to schema to allow flexible sets of attributes to be applied to
  campaign enrolments.
- Updates to Identity Checking logic to account for Legacy AML Checked attribute.
- Added new service CCLA_CS_RECALCULATE_ALL_IDENTITY_CHECKS. Forces recalculation for all
  Identity Checks with a Recalculate Date in the past.
- Added new service CCLA_CS_FORCE_RECALCULATE_IDENTITY_CHECK. Forces recalculation for a
  single Identity Check, regardless of its current recalculation date.
- Correspondent Codes are now user-editable. See env. var CCLA_EnableAuroraCorrCodeUpdates
- Removed 'Account Level Override' button on Edit Person screen. This has been replaced
  by an Identity Checking Override panel at the bottom of the screen.
- New Aurora Client/Correspondent mapping fields visible and editable as part of this
  release. See env vars:
  - CCLA_DisplayNewAuroraMappingFields
  - CCLA_EnableAuroraMappingUpdates
  - CCLA_EnableAuroraCorrCodeUpdates
- Users can now add multiple Aurora Client/Correspondent maps against the same record.
  See env vars:  
  - CCLA_CS_AllowMultipleAuroraClientMaps
  - CCLA_CS_AllowMultipleAuroraPersonMaps
- Added auto-complete functionality to the Account Short Name field. Takes the value
  of the Building Society Number or Account Name fields, if they are specified first.

Version 2.1.1 (12/01/2012)
--------------------------
- Amended Community First Client Info spool file generation
  - Added Org Account Code to first page
- Fixed panel loading bug on Interation Edit/Review screens (hotfixed in live)
- Removed needless joins from qClientServices_GetRelation query. Was adding uneccessary
  reference data to audit entries
- Removed Bulk Edit Bank Account template (has since been consolidated into single Bulk
  Edit template)
  
Version 2.1.2 (31/01/2012)
--------------------------  
- Added new Bulk Update mode: Nominated Bank Account. Used to batch-apply Nominated Bank
  Account properties to Account-Bank Account relationships.
- Added Bank/Branch Details lookup on Bank Account Info/Edit/Add pages.  
- Added Back link to custom Error Page
- Added display of nominated Organisation relationships on Person Info display.  
- Removed old Person IVS Override templates/services.
- Allowed users to remove Aurora Correspondent links
- Amended 'set address preferences' method to ensure that two similar contacts cannot
  have the Default/Experian flags set.
- Fixed 'apply action' method on the Enrolment Info page, so that the selected action
  is correctly picked up.
- Refactored sequence names and locations
  
Version 2.1.3  (08/02/12)
-------------------------
- Removed redundant templates, services for unused views.
- Ensured that quotes and other characters are stripped from the 'Account Short Name' field
  when Bank Account data is fetched by the JSON lookup screen.
- Renamed views to v_[view_name]
  - Requires ucm_db_to_central_db_migration_view_01022012.sql in CCLA_CORE
- Community First Change Request 
  - requires community_first_updates_01022012.sql
- Added services/UI for creating new Investments, Donations and their associated Fund/TTLA 
  Allocations.
- Run the service CCLA_CS_CREATE_TTLA_ORGS once after deployment and running the latest DB
  update scripts. Just once! This will generate all 152 TTLA Organisation entries in the DB.

Version 2.1.4 (10/02/12)
------------------------
- Added Global Relation Check Override flag (see CCLA_CS_GlobalRelationCheckOverride env var)
  When switched on, Relation Checks will always return true regardless of relation status.
  Switched on for this release due to issues with failed checks in Workflow. Should be switched
  off as soon as this is resolved.
- Added support for Anonymous Donor creation

Version 2.1.5 (13/02/12)
------------------------
- Entity Relation Checks can now return a third UNKNOWN status, under the following conditions:
  - No Auth Person relationships set against Org
  - 1 or more Auth Person relationships with unknown Verification status, and no Auth Person
    relationships with failed Verification status.
- Added new config var for Entity Relation Checking: CCLA_CS_TreatUnknownRelationCheckAsPassed.
  This overrides any 'Unknown' relation check outcome with PASSED instead.
- In this build, the global override flag is switched off, and the 'Treat Unknown Relation
  Check as Passed' is switched on.
- Relation display grids will now display red ticks for explicitly unverified relationships.
- Changed component build to include the entire images/ccla/ directory, instead of the
  images/ccla/clientservices/ sub-directory.
- Edit Relation pop-up will now close automatically after applied properties are updated.  
- Edit Relation pop-up link has JavaScript timestamp appended, to ensure it always fetches the
  latest information from the server rather than local cache. Users were previously seeing
  out-of-date information on the pop-up until they explicitly refreshed.
- Added 'Generate On-Boarding Covering Letter' campaign action to the Community First campaign.

Version 2.1.6 (15/02/12)
------------------------
- Added Donor Subscription Form generation from Investment Screens.
- Remapped a set of 3 Account data queries to query the raw ACCOUNT table, instead of the
  V_ACCOUNT_EXTENDED_CLIENT view. This ensures fetches/auditing will only work against the
  core data columns.
  - qClientServices_GetAccount
  - qClientServices_GetAccountByAccNumExt
  - qClientServices_GetAccountByValues
  Previous versions of the queries remain for now, suffixed with _OLD.
- Changed wording of 'Nominated recipient' on Enrolment Info screen to 'Campaign Recipient'
- Added extra hint text to Enrolment Info screen clarifying what Campaign Recipient means
- Added related organisation data to binder on Edit Org/Org Info screen. Not displayed yet.

Version 2.1.7 (17/02/12)
------------------------
- Fixed Form Document links
- Added override for standard UCM screen to remove delete functionality for standard users. 
- Amended spool file creation for Community First On-Boarding form and Segregated Client
  Application form
- Community First Client Info forms will now populate Account data fields on documents,
  providing the Donation Account has been created
  
Version 2.1.8 (20/02/12)
------------------------
- Added dynamic floating actions menu that moves with horizontal scrolling. 
- Amended spool file creation for Community First On-Boarding form and Segregated Client
  Application form:
  - Organisation Name is now printed after the Correspondent Name in the address panel.
  - Comm First Client Info form now prints list of Signatories instead of Auth Persons
  - Comm First Client Info form takes the list of signatory names from the Account, rather 
    than the Organisation
- Community First Client Info forms will now populate Account data fields on documents,
  providing the Donation Account has been created  
- Set default number of required signatories on Community First Donation Accounts to 2

Version 2.1.9 (13/03/12)
------------------------
- Added date to header of Community First On-Boarding form
- Amended Seg Client Registration Form: changed ordering of some Director fields
- Added Admin-only service for bulk addition/removal of Account Flags. See template page:
  CCLA_CS_ACCOUNT_FLAGS_BULK_EDIT. Accessible via the Admin menu.
- Added new toggle panels to Edit Organisation/Organisation Info page:
  - Marketing Details
  - Other Details
  - Aurora Client Details
  - Entity Checking/Verification
- Entire toggle panel headers can now be clicked to toggle panel state
- Fixed bugs in Organisation Category field:
  - Didn't work correctly when the Organisation had related Organisations linked to it
  - Users were unable to completely deselect the Category
- Hidden unused Classification field on Edit Organisation/Organisation Info page
- added element lock services/code/admin screen
- Updated getEntityDataForForInstruction method called from Iris to perform lookup on
  the new xOrgAccountCode field value, if one is supplied.
- Refactored element_attribute_applied and form tables, queries and code to use GUID's instead of DOC_ID's
- Added generate child document for returned CF donor subscription forms.
- Lock down subscription forms until return of donor subscription form.

Version 2.2.0 (14/03/12)
------------------
- Fixed person relationship panel not showing up on organisation and account pages (IE issue).

Version 2.2.1 (23/03/12)
------------------------
- potential fixed AJAX People Relation update on account edit page (only visible on IE8+)
- Fixed issue with TTLA percentage to amount calculation and always use the amount column when 
  populating the form.
- Fixed donor subscription form investment detail section. Not printing the actual amounts.
- 'Generate Donor Subscription Form' button is now hidden on Edit Subscription page, unless
  the override flag is present, or the Subscription is at 'New' status
- Added confirmation dialog to above button.
- Migration of the INTERACTION schema -- See CCLA_Core Readme.txt for more info.
- Fixed bug which prevented documents being checked in that had Form ID values set.
- Organisation Document lists are now split into 2 groups:
  - Client Number/Company
  - Invoices
- View All Documents links in Organisation Document lists will now display results in
  Advanced Search mode, restricted by Company.
- Edit Subscription page displays new Subscription fields:
  - RPI list
  - Date Form Received
  - Date Cash Processed
- When a Donor Subscription Form is initially returned, the transfer instructions are
  now set to PREADVICE by default, instead of DEPBNK/BUYDF.
- Document UpdateFilter extended to parse values in new xPaymentRef field and compare to
  Subscription Payment References. If a match is found, the 'Date Cash Processed' is set
  or updated on the corresponding subscription. If the 'Date Cash Processed' field on the
  Subscription was initially empty, the Subscription Status is updated to 'Cash Received'

Version 2.2.2 (23/04/2012)
------------------
- Fixed bug with UpdateFilter: prevented update of document that had xPaymentRef field set,
  but not xProcessDate.
- Fixed bug with Edit Organisation page, which displayed incorrect Organisation Account code
  and other info if an Organisation has related Organisations associated with it.
- Fixed bug with Edit Organisation page, which prevented the Category field being deselected.  
-- added idcToken for 11g migration
-- decoupled database and facade for 11g migration
   Requires Datapump to be performed.
-- refactored class loading code for 11g migration
- TTLA Allocation pop-up now defaults to amount value allocation, as opposed to percentage
- Fixed error with deleting contact details for person/organisation.
- Fixed error with searching for a bank account.
- Fixed error with idcToken appearing on PSDF Campaign Enrolment Info Page.

Version 2.2.3 (27/04/2012)
------------------
- Fixed instruction panel document link to use doc_guid.

Version 2.2.4 (30/04/2012)
--------------------------
- Added delay period setting to document UpdateFilter. Add arbitrary delays to document updates
  by specifying an integer value to updateDelayMillis in the update request parameters.
- Fixed bug that prevented documents being indexed with non-singleton Form IDs, e.g. 
  PSDF Subscription/Redemption forms.
- made CCLA_CS_PERSON_EDIT service scriptable due to idcToken redirect url issues

Version 2.2.5 (xx/xx/2012)
--------------------------
- Added fix for styling on org details page to ensure Element Identifiers flow correctly.
- Changed Add/Edit Account page to disable Income Distribution Method selector and force the
  value to REIN when an Accumulation Fund is selected.
- Removed old link to iris user guide
- Altered indexing data lookup call to ensure the Client Number field will always correspond to 
  the Org Account Code. See getEntityDataForInstruction method.
- Amended Error Page CCLA logo image URL to use <$HttpImagesRoot$>, ensures URL is resolved
  correctly on UCM 11g.
- Added missing idcToken to relationship info panels


Version 2.2.6 (24/05/2012)
-- This component has been converted to use the new ECS build process. 
-- Any changes must be put in the CHANGELOG not this readme. 
-- Configuration/installation notes can still be added to the top of this file

Version 2.2.7 (12/06/2012)
-- Ipad style changes
-- Fixed issue with help administrator facade.

************** END **************