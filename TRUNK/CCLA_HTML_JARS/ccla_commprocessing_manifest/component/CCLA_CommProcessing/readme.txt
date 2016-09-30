CCLA_CommProcessing Component Release v1.3.9
================================================================================
  Full Release Name: 2014_09_07 v1.3.9 (Build 22147)

      Date: 07/09/2014 04:05 PM
    Author: tm
  Location: http://ecs-svn/stellent/CLIENTS/CCLA/Components/CCLA_CommProcessing/tags/v1.3.9
           
(c) Extended Content Solutions Limited 2014
================================================================================
CCLA_CommProcessing Component Release v1.2.7
================================================================================
  Full Release Name: 2012_07_06 v1.2.7 (Build 18572)

      Date: 06/07/2012 03:55 PM
    Author: tm
  Location: http://ecs-svn/stellent/CLIENTS/CCLA/Components/CCLA_CommProcessing/tags/v1.2.7
           
(c) Extended Content Solutions Limited 2012
================================================================================
﻿CCLA_CommProcessing Component
=============================
Tom Marchant

Manages the Communication/Instruction Registers and their
surrounding processes.

Build History
-------------

Version 1.0.0 (17/03/11)
------------------------
-First build. Contains UCMMetadataTranslator classes required for
 initial data caching in CCLA_Core.
 
Version 1.0.1 (23/05/11)
------------------------
-Add the following users via the User Admin applet:
 -SCANUSER1
 -SCANUSER2
 -SCANUSER3
 -System
 -DripFeed
 Give them the password 'idc' and grant no rights. These are required as
 foreign key references when creating new Comm entries in the database.
-Copied service definition ECS_IRIS_APPLY_POST_WORKFLOW_ACTION_SUB from the
 SPP_INTEGRATION component. Once deployed, this component will override the
 existing service from SPP_INT and handle post-workflow activity for UCM
 bundle items. 
 
Version 1.0.2 (24/05/11)
------------------------
-Added bindings for xScanUser values originating from DripFeed application.
 Always sets the Comm 'createdBy' field to 'DripFeed' in these cases.
-Added auditing for CommGroup, Comm and Instruction objects.

Version 1.0.3 (25/05/11)
------------------------
-Added fix to apply 'Archived' status to MULTIDOC items when they are 
 submitted to Instruction Register
 
Version 1.0.4 (31/05/11)
------------------------
-Added Instruction Audit features to Instruction Info. Required CCLA_CORE 
 and SQL script :  instruction_audit_tables_240511.sql
-Updated Instruction Info page to allow Instruction Status updates
-Update SPP Web Service cloneAndSubmitDocToSpp to point at new service:
 -CCLA_CP_CLONE_DOCUMENT_AND_RESET_BUNDLE
 
Version 1.0.5 (16/06/11)
------------------------
-Added hard fail if an attempt is made to update an existing instruction's 
 document to an unbound document class, e.g. MULTIDOC.
-Fixed issue which prevented multiple 'clone document' operations originating
 from SPP (via CCLA_CP_CLONE_DOCUMENT_AND_RESET_BUNDLE service). Each clone
 action will check the workflow status of the parent bundle - if it isn't in
 workflow it is checked out/checked in.
 
Version 1.0.6 (24/06/11)
------------------------ 
- UCI00202 Create Instruction from interactions. 
  - Requires 16_June_interaction_instruction_data_load.sql
- Instruction Info Panel Update 
  - Requires 24_June_Instruction_Info_View_Update.sql
-Workflow submissions from the Instruction Register will now fail if the document
 has a Workflow Date/xWorkflowDate set, as opposed to a UCM_JOB_ID/xJobId.
 
Version 1.0.7 (14/07/11)
-----------------------
- UCI00230 Implemented new User Work Group functionality. Allows dynamic 
  assignment of bundles via Get Next Pending Bundle, based on the current user's
  Work Group. Administration page available via CCLA_CP_WORKGROUPS_ADMIN service.
  Get Next Pending Bundle service/codebase now resides in this component as
  opposed to SPP_INTEGRATION.
  
Version 1.0.8 (08/08/11)
------------------------
- Preadvice/Dicondin CR work (disabled for now)
- Re-implemented xDependantDocName assignment on bundle completion. See method
  setDependantDocNames() in DocumentBundleServices.

Version 1.0.9 (16/08/11)
------------------------
- Disabled Instruction Priority calculation referenced in Interaction Translator
  class
  
Version 1.1.0 (23/08/11)
------------------------
- Fixed bug in applyProcessAction method. Was originally using alternative DB
  connector to execute actions, this didn't work correctly if the action makes
  changes to a UCM document. Changed back to default DB connector.

Version 1.1.1 (31/08/11)
------------------------
- Added hook to doPostValidateActions FormHandler method in 
  applyPostWorkflowAction.
- (CL) Started work on DEPBNK/BUYBNK reconcilliation
  
Version 1.1.2 (22/09/11)
------------------------
- Fixed bug in setDependantDocNames Java method. Was originally trying to place
  dDocName values into the xWithInstruction field instead of xDependantDocName.

Version 1.1.3 (26/09/11)
------------------------
- FileStoreProvider Install: 
Refer to http://extranet.extended-content.com/ucmxe/groups/public/@ccla/documents/case_document/xnet002765.pdf for details 
- DICONDIN-PREADVICE CR - Requires Transaction_Batch_19092011.sql (in Core).
NB. This can be rolled-out without running the script as this is not finished.  

Version 1.1.4 (18/11/11)
------------------------
- Static Data Update. Requires StaticDataUpdateCoreTables_01112011.sql in core package
- New web service: GetPendingSDUInstructions. Calls CCLA_CP_GET_PENDING_SDU_INSTRUCTIONS service,
  returns a ResultSet of all pending SDU instructions. Requires update of SPP.WSDL

Version 1.1.5 (30/11/11)
------------------------
- Fixed various bugs in SDU Update method, including formatting of usernames to match entries
  in the UCMADMIN.USERS table (names must be lowercase)
- Added 'Terminate' Instruction Action, 'Terminated' Instruction Status
- Add 'SDU Aurora Execution Failure' Instruction Process

Version 1.1.6 (10/01/12)
------------------------
- Added timing log entries for all document-based Filter classes
- Added check condition to UpdateFilter to ensure it only executes if the docType is
  ChildDocument or Document
- Organisation documents are now searched using correct client number padding, as dictated by
  the REF_COMPANY table.
- Externalised config variables in SYSTEM_CONFIG_VARS. See script in CCLA_Core:
  - new_config_vars_12122011.sql
- Inter-module and inter-cycle delay times are now configurable at run-time via the System
  Config Var administration page.
- Added new metadata translator for destination account id. See script in CCLA_CORE:
  - PSDF_Transfer_Upgrade_Scripts_12122011.sql  
- Check all mandatory fields are populated for an instruction. 
- Added BigDecimal fixes to Transaction Batch Processing, requires Core v1.3.2
- Removed Workflow job triggers on SDU instruction rejection/execution failure.
  See env. vars: 
  - CCLA_CommProc_TriggerNotificationJobOnSDUInstructionRejection
  - CCLA_CommProc_TriggerNotificationJobOnSDUInstructionExecutionFailure
- Added new service CCLA_CP_GET_FILE. Used to return a web-viewable rendition of
  a given content item. Request parameters:
  - UCM_DOC_NAME, UCM_REVISION_ID (passed Revision ID is ignored currently)
  - or INSTRUCTION_ID
- Added switches to force specification of Income/Withdrawal Bank Accounts when
  adding/updating Aurora Accounts via SDU process
- Adjusted duplicate check on sdu instructions. Only applied to active sdu instructions 
  (i.e not in status completed, archived, terminated or duplicate). 
  Requires script StaticDataProcessing_24102011.sql to be executed (in core).
- Amended ordering on queries used to fetch pending instructions, to ensure they
  are returned in required execution order for Static Data Updates   

Version 1.1.7 (12/01/12)
------------------------
- Refactored Transaction Batch to use Bank_Account_ID instead of Sort_Code or Bank_Account_No. 
  Requires "Transaction_Batch_Update_12012012.sql" to be executed (in CCLA_Core).
- Fixed DestinationAccountNumberTranslator to correctly extract the account number.  

Version 1.1.8 (31/01/12)
------------------------
- Up to 2 SDU Correspondent instructions may now be generated, per Static Data Update instruction.
  One for the Account Correspondent, one for the Client Correspondent, if they differ.
- Error messages during SDU Instruction Generation are now stored against the source instruction.
- The cloneDocumentAndResetBundle service will now refresh the bundle's priority at the end
  to account for changes brought about by cloned items.
- Fixed bug in document bundle translator, which caused it to fall over if there were any
  existing instructions in the bundle which didn't have a DOC_ID set (e.g. Static Data Update
  instructions)

Version 1.1.9 (20/02/12)
------------------------
- Added support for AML Tracker attribute/property removal for all Mandate instructions detected
  in bundles post-EnterDetails step. Controlled by env. var:
  - CCLA_CommProc_RemoveAMLTrackerAttributesPostEnterDetails
  - WORK IN PROGRESS - still need a way to remove Account Flag overrides etc.
  - Disabled for now.
- Added extra clause to StaticDataUpdate module to only accept instructions that are linked to
  an Aurora Account. This will prevent SDU instructions being generated for non-Aurora Accounts.
  
Version 1.2.0 (13/03/2012)
------------------------
- Added ability to batch update documents based on following criteria: 
  http://ccla-ap14-dev/ucm/idcplg?IdcService=CCLA_CP_RESUBMIT_INSTRUCTION_DOCS
  &IsJava=1&DOCUMENT_CLASS=INV&FROM_DATE=23/02/2011&TO_DATE=23/02/2012&getCSV=1
  
  Required Binder Params
  - DOC_CLASS
  - TO_DATE
  - FROM_DATE
  
  Optional Binder Params 
  - getCSV - if this is present a CSV file will be returned with the failed docs
  
  Return Binder Params
  - TOTAL_NUM_DOCS - total number of docs returned from the query
  - NUM_DOCS_UPDATED - total number of docs successfully updated		
  - rsFailedDocs - resultset of docs failed, with error msg and metadata
- Updated CCLA_CP_GET_FILE service to take in a "GUID" param which can be a 
  [DOCNAME] or [DOCNAME]:[REVISION]
- Added InvoiceService helper services for handling invoice capture/indexing.
- Fixed duplicate checking for CREATE_CORR SDU instructions.
- Added service class for handling Invoice processing.  


Version 1.2.1 (28/03/2012)
--------------------------
- Updated QueryJobProfile to have instructionId and xPaymentDate
- Amended resubmitSDUInstructionForAuthorisation web service to throw a ServiceException
  if the resubmission call fails.
- SDU Instruction fetch queries used by Workflow now return 2 new columns:
  - SPP_BATCH_REF (Workflow Batch Number/Enelope ID)
  - UCM_BATCH_REF (UCM Envelope ID)
- SDU Instruction fetch queries now ordered by SPP_BATCH_REF, then instruction type
- SDU Instruction fetch queries used by Workflow now return padded CLIENT_NUMBER values
- New admin service available: CCLA_CP_POPULATE_MISSING_ORG_ACCOUNT_CODES. This will set the
  xOrgAccountCode values against invoice items where an xClientNumber is already set.
  Takes the following parameters:
  - maxRows: total invoice docs fetched and processed
  - testMode: optional, just returns no. of items to be updated.
- Refactored V_INSTRUCTION_EXTENDED to not link to any ucmadmin tables.
  - Requires DecouplingViews_21-03-2012.sql to be executed (in core package).

Version 1.2.2 (23/04/2012)
--------------------------
- Refactored Instruction and Comm code/queries and tables to use docGuids instead of docIds
- Refactored caching and ensure correct Facade is used. 
-- added idcToken for 11g migration
-- decoupled database and facade for 11g migration
   Requires Datapump to be performed.
- Added new Parent Doc GUID instruction data field. See Core script:
  - ParentDocGUID.sql   
- Fixed archive service issue with incorrect facade.

Version 1.2.3 (27/04/2012)
--------------------------
- Fixed issue with DocumentBundleTranslator. Was preventing bundles being converted to
  instructions, if they'd been converted previously.
- Fixed issue with UCM Document links on Instruction Info page.
  
Version 1.2.4 (30/04/2012)
--------------------------
- Removed use of UPDATE_DOCINFO_NOTRANS service method when updating LWDocument instances.
  This was done by setting lwDoc.setUseTransaction(false). Since switching over to use the
  alternate DB connector, it makes no sense to suppress transactions for LWDocument
  instances, and causes various issues including child document update failures, in
  particular inserts into the Documents table for metadata-only items.
- Fixed incorrect facade usage in SDU fetch/update methods.
- Added new service CCLA_CP_CHECKIN_CHECKOUT_DOCS. Takes a queryText parameter, which is
  expected to yield a ResultSet containing dDocName values, which will be checked out/
  checked in, resetting their workflow state where applicable. Only relevant for metadata-
  only content items.
- Added new test service CCLA_CP_TEST_DOC_UPDATES. Used for testing concurrent document
  updates.
  
Version 1.2.5 (10/05/2012)
--------------------------
- Added extra validation to OrganisationIdFieldHandler Instruction Data field translator.
  Document data translation will now fail if the indexed Org Account Code doesn't correspond
  to the indexed Client Number/Company.
- Edit Applied Instruction Data screen now available. Admin-only link available on Instruction
  Info pages.
  
  
  
Version 1.2.5 (24/05/2012)
-- This component has been converted to use the new ECS build process. 
-- Any changes must be put in the CHANGELOG not this readme. 
-- Configuration/installation notes can still be added to the top of this file

************** END **************