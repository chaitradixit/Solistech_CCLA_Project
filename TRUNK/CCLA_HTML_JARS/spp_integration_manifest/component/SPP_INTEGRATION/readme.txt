SPP_INTEGRATION Component Release v2.7.3
================================================================================
  Full Release Name: 2015_01_03 v2.7.3 (Build 21926)

      Date: 03/13/2014 03:59 PM
    Author: tm
  Location: http://ecs-svn/stellent/CLIENTS/CCLA/Components/SPP_INTEGRATION/tags/v2.7.3
           
(c) Extended Content Solutions Limited 2015
================================================================================
﻿SPP_INTEGRATION Component
=========================

Notes
-----
When on live, ensure the env file has SPP_INT_TRIGGER_SPP_WORKFLOWS=true

Build history
-------------

Version 1.6.6 (Aug 11th 2009)
-----------------------------
-(TM) Altered the way dates are parsed and sent to SPP

Version 1.7.0 (Sep 5th 2009)
----------------------------
-(TM) Added various extra conditions to control SPP submissions
-Hooked into DOCUMENT_CLASSES table, used to determine whether
 items are transaction/supporting docs

Version 1.7.2 (Sep 11th 2009)
-----------------------------
-Updated Extended Content Report to account for content pending in Iris

Version 1.7.3 (Sep 17th 2009)
-----------------------------
-Added check to ensure all items have workflow ID set before SPP submission
-Added method to automatically insert workflow ID on items without existing ID
-Baseline build before moving component back to ECS dev

Version 1.7.4 (Sep 24th 2009)
-----------------------------
-Removed reporting queries and templates into CCLA_reports component
-Consolidated all resources from SPP_INTEGRATION_SOW6 component:
 -Instruction documents popup
 -Standard UCM menu hooks
 -Various queries and services for instruction documents
 -Instruction document config variables
-Added server-side data sanitization:
 -Fund field auto-completion, based on Account Number value
 -Upper-case conversation of Account Number value
 -Decimal formatting for amount values
-Minor code refactoring:
 -New package com.ecs.stellent.spp.service contains ECS classes
 -ServiceHandlers class renamed to SppIntegrationServices
 -ServiceHandlersSow6 class renamed to InstructionDocServices
 -CacheDataFromAuroraSow6 class renamed to AuroraCacheServices
 -New class BundleServices, should really belong in CCLA_MailHandling class
  but this component doesn't have its own jarfile yet.
-MAND items will now be released straight into SPP based on the value of
 their With Instruction flag (i.e. xWithInstruction=Yes)

Version 1.7.5 (Sep 29th 2009)
-----------------------------
-Fixed bug preventing MAND submission to parking lot

Version 1.7.6 (Nov 2nd 2009)
----------------------------
-Fixed bug which caused doubling-up of 'Content Actions' menu items in 
 standard UCM interface
-Optimized batch SPP submission code
-Added sub-service and supporting code for auto-generation of envelope IDs
 when using the manual check-in screen.

Version 1.7.7 (Nov 9th 2009)
----------------------------
-Added new filter event which auto-adds createPrimaryMetaFile flag to binder
 if the dDocType is ChildDocument. This prevents errors thrown when users update
 child doc items in SPP.

Version 1.7.8 (Nov 17th 2009)
-----------------------------
- Added fund java code to complete up to 3 trailing characters from an account field.
- Added for SPP calls to DOC_INFO_UPDATE calls to make sure the dDocType of the content
  item has not been changed. If it has, it gets reverted back. This requires Singularity
  to add isSPP flag to their web service calls - currently not done.
- First update to WSDL file: 
  C:\ORACLE\ucm\server\data\soap\custom\wsdl_custom.hda
  This file needs to be copied to the corresponding folder on live. Backup the current one
  first.

Version 1.7.9 (Nov 19th 2009)
-----------------------------
-Added extra clauses to child document checkin routines to ensure ChildDocument items are
 created for each transfer listed on a returned CLIENTCONF form.

Version 1.8.0 (Nov 24th 2009)
-----------------------------
- Added Duplicate functionality (idoc and java)

Version 1.9.0 (Dec 10th 2009)
-----------------------------
- Changed multidoc/with instruction functionality monumentally

Version 1.9.1 (Dec 12th 2009)
-----------------------------
- Fixed bug temporarily, but SPP still need to fix the bug permanently by always passing in
  workflowId to the getDependantDocsForRootDoc method

Version 1.9.2 (Dec 14th 2009)
-----------------------------
- Added Bank source field and corresponding b-envelope prefix

Version 1.9.3 (Dec 15th 2009)
-----------------------------
- Updates to Child Document generation, now uses LWDocument instead of LWFacade to
  perform checkins. The checkin action is synchronous to prevent concurrency issues
- Amended UpdateFilter code to remove Form ID values from metadata, unless the item is
  a CLIENTCONF. Form ID values of zero will also be removed.

Version 1.9.4 (Dec 16th 2009)
-----------------------------
- Implemented list of restricted child doc classes. Users will not be able to select
  this document classes in the instruction/multi-doc popup window.
  This list currently contains one entry (CLIENTCONF), this is used to prevent users
  indexing CLIENTCONFs incorrectly as child docs.
  
Version 1.9.5 (Dec 18th 2009)
-----------------------------
- Added custom DocumentValidator implementor class. This is intended to offer some
  speed increase when batches go through validation before submission to SPP.
  The Iris profile config must be updated to point to the new validator class:
  com.ecs.stellent.spp.validation.CclaBatchValidator

Version 1.9.6 (Jan 4th 2010)
----------------------------
- Exta checkin filter added to apply 'Pending' status to newly checked-in items with
  dDocType of Document/ChildDocument and a non-null bundle ref.
- Amended SPP workflow trigger code to exclude all items with a document class in the
  env var list CCLA_multiDocClasses (see CCLA_MailHandling component). This allows for
  a custom list of document classes to take the role of the old 'XXX' doc class.
- Added new class to handle importing MAND items held in the Parking Lot (VerifyMandate
  UCM workflow) into the new SPP Queue. This requires the following update to the Exit
  step of the VerifyMandate workflow:
  
   <$if not (xStatus like "Importing to queue") and not (xDocumentClass like 'XXX')$>
		 <$executeService("ECS_MANUAL_TRIGGER_SPP_WORKFLOW")$>
		 
   This method can be triggered by running the CCLA_IMPORT_PARKING_LOT_ITEMS_TO_QUEUE
   service as an admin.

Version 1.9.7 (Jan 7th 2010)
----------------------------
- Various optimizations to workflow submission/child document generation code
- Timing debug information added

Version 1.9.8 (Jan 8th 2010)
----------------------------
- ECS_SPLIT_DOCS_SUB service and references now removed from checkin services.
- Added further code optimizations to child document checkins. Can now be configured to
  run asynchronously or synchronously. Greatly increased speed of child document
  checkins.
- More concise timing information added for various service methods.

Version 1.9.9 (Jan 13th 2010)
-----------------------------
- Added 'View in Iris' link to standard UCM doc info page

Version 2.0.0 (Jan 18th 2010)
-----------------------------
- xIndexer field values now copied to child documents when they are checked in.
- Updated all LWDocument constructors used in SPP submission/child document
  generation to use the database. Prevents issues caused by extra-long checkin/
  indexing times.
- Extra validation added to Instruction Document popup. Now checks for valid client
  numbers (i.e. correct length for company)
  
Version 2.0.1 (Jan 28th 2010)
-----------------------------
- Urgent bug fix: Automatic checking in/out of Mandates had ceased to work since the
  previous update. This has since been fixed by overwriting 2 queries used by the
  LWDocument instance in ECS_BaseUtils. In time, the BaseUtils component will be
  updated itself to include these changes.
  
Version 2.0.2 (Feb 24th 2010)
-----------------------------
- Amendments to backend code to support ClientServices component: new getNextEnvelopeId()
  method which takes a FWFacade parameter. Other methods refactored to take FWFacade
  parameter.

Version 2.0.3 (March 1st 2010)
------------------------------
- Amended createDependantDocsForRootDocs web service used by SPP. The csvClientIds/
  AccountNumbers parameters are now optional. This permits supporting documents to be
  attached to APP items, which may not have these fields set initially.

Version 2.0.4 (March 3rd 2010)
------------------------------
- Added static method and associated query to allow easy appending of pending child
  document entries by other components.
- Moved xFormId filter validation to CCLA_ClientServices component.
- New document class AUTOMAND has been accounted for in the backend code. Functions in
  an identical way to the existing MAND class.
- New workflow AutoMandateUpload added, accounts for new document class AUTOMAND.

Version 2.0.5 (March 11th 2010)
-------------------------------
- Amended document class configuration: now uses static Java cache. Cache expiry time
  can be adjusted via the SPP_DOC_CLASS_CACHE_EXPIRY env. variable.
  
Version 2.0.6 (March 19th 2010)
-------------------------------
- CheckinFilter class has been updated to automatically append new bundle references
  to new check-ins, if the 'addBundleRef' flag is present in the binder, and an xBundleRef
  value wasn't specified in the checkin parameters.
  
Version 2.0.7 (March 25th 2010)
-------------------------------
- Amended CreateNewJob service to re-use parent document's workflow ID

Version 2.0.8 (March 26th 2010)
-------------------------------
- Amended checkItemWorkflowId method, to ensure new workflow IDs are generated on submission
  for documents which don't have a workflow ID/bundle ref set.
  
Version 2.0.9 (March 31st 2010)
-------------------------------
- Fixed document class option list on Add Instruction Docs pop-up screen, so it uses the new
  vDOCUMENT_CLASSES UCM view.
- Documents will only use their xFormId value as an SPP workflow ID if their document class is
  listed in SPP_FORM_ID_DOC_CLASSES.

Version 2.1.0 (April 22nd 2010)
-------------------------------
- Restored old supporting document functionality:
  - Supporting document types (specified by IS_SUPPORTING column in DOCUMENT_CLASSES table)
    will not be submitted to SPP, if they arrive in a bundle with a MAND/AUTOMAND item.
  - This new behaviour is controlled by the flag:
    SPP_SUPPRESS_MAND_SUPPORTING_DOC_SUBMISSION
  -	Remove the flag to restore previous functionality (i.e. supporting docs are always
    submitted to SPP)
	
Version 2.1.1 (April 23rd 2010)
-------------------------------
- Added xComments to list of SPP-mapped fields (pDOC_COMMENT)
- Returned GUIDs from SPP are no longer stored in the document xComments field. They are
  now stored in one of the extra parameter fields in the 'SPP submission' audit entry.
- Added fix for 1-hour offset issue caused during British Summer-time. When date values
  are submitted to SPP, they are checked to see if the timestamp is before 1am. If true,
  the timestamp is offset by 2 hours. This behaviour can be toggled with the flag:
  SPP_APPLY_DATE_TIME_OFFSET

Version 2.1.2 (May 13th 2010)
-----------------------------
- Amended QAddChildDoc query to map to column names directly. This fixes an issue caused
  by mismatched TWITHINSTRUCTIONDOCS table design between UAT/Live systems.
- Added SPP field mappings for xDestinationFund, xDestinationAccount.
- Fixed bug in initDependantDocsForChildDocs method, was throwing exceptions due to the
  referenced rsBatchItems ResultSet being in a post-looped state without a .first() 
  reset call.
  
Version 2.1.3 (May 26th 2010)
-----------------------------
- Added support for auto-completed xProcessDate field. Requires new table CCLA_PROCESS_DATES.
- New service CCLA_PROCESS_DATE_ADMIN can be used to forward-date existing process dates.

Version 2.1.4 (May 27th 2010)
-----------------------------
- Updated JavaScript used for calculating total amount on Child Document pop-up view. Attempt
  to increase accuracy of calculated amount to two decimal places.
  
Version 2.1.5 (June 3rd 2010)
-----------------------------
- Removed call to getBundleItemNotes in triggerSppWorkflow method - this isn't required any
  more and was quite an expensive database call.
- CANCELLED: Added xProcessDate/pPROCESS_DATE SPP field mapping.
- Added special batch SPP trigger page for triggering multiple single-item SPP jobs. Access it
  here: ?IdcService=GET_DOC_PAGE&Action=GetTemplatePage&Page=SPP_TRIGGER_BATCH_JOBS

Version 2.1.6 (July 2nd 2010)
-----------------------------
- Fixed bug in xDependantDocName assignment. Child documents of MultiDoc items (e.g. 
  MULTIDOC,XXX,FAX) were setting their xDependantDocName value to point at the MultiDoc item.
  The child document generation method has now been changed to not set the xDependantDocName
  value for children of MultiDoc items.

Version 2.1.8 (Oct 3rd 2010)
----------------------------
- Extended supporting document job suppression: if a bundle contains an APP/APPSHORT/AUTOAPP
  item, supporting docs in the bundle will not kick off any jobs.
    - This new behaviour is controlled by the flag:
    SPP_SUPPRESS_APP_SUPPORTING_DOC_SUBMISSION
- Added new Java methods/services for resetting Parking Lot bundles.
- Mandate Parking Lot submission can be switched off entirely by clearing the flag:
   SPP_UPREVISION_ORPHAN_MANDS_ON_SUBMISSION
- Added new utility methods for document bundles in the codebase.   

Version 2.1.9 (Oct 4th 2010)
----------------------------
- Added new flagBundle() utility method for auditing Bundle flag actions in greater detail.

Version 2.2.0 (Oct 5th 2010)
----------------------------
- The flagBundle() method now takes a metadata snapshot of the problem item. This is visible
  via the Flagged Bundles Report in the CCLA_reports component.
- The custom batch validator class now checks if the bundle has a status of 'Flagged'. If so,
  workflow actions are prevented.

Version 2.2.1 (Oct 7th 2010)
----------------------------
- Amended checkDuplicate methods in codebase to support items which were previously marked
  as duplicates. Ignoring potential duplicates is now audited.
- The getNextPendingBundle service method can now lock bundles pre-emptively, i.e. just after
  they are found and before being returned to the user to be loaded on-screen. This should
  prevent the problem where a user is presented with a bundle that was just recently fetched
  by another user.
  This functionality can be toggled using the env. var CCLA_preemptiveAutoFetchLock in the
  CCLA_MailHandling component.
- Implemented basic Priority allocation to bundle items.

Version 2.2.2 (Oct 14th 2010)
----------------------------
- Updated getNextPendingBundle service to restrict very new bundles being returned.
- Started work on implementing new SPP fetch web services and CLIENTQUERY job triggering
- SPP job variable mapping now uses 'Profiles' to accommodate multiple job data 
  configurations
  
Version 2.2.3 (Oct 18th 2010)
-----------------------------
- Removed temporary prioritization of 'EnterDetails' bundles (required last weekend)

Version 2.2.4 (Oct 20th 2010)
-----------------------------
- Started work on new Process/Rollover Date/Dealing Date handling. Requires a new table with
  pre-allocated dealing dates up until Oct 2012.

Version 2.2.5 (Oct 27th 2010)
-----------------------------
- Process/Rollover/Dealing Date handling finalized for deployment. Process Dates are no longer
  assigned on document checkin. Instead they will be assigned to all bundle items when it is
  approved at EnterDetails/Validation stages, based on the document's Fund code and associated
  dealing date for the Fund.
  Documents without a Fund, or a non-transaction document class, will be assigned the base 
  process date.
  The Process Date will never be set automatically if it has an existing value.
- Requires import of all generated Dealing Dates from UAT.
- Added switch to control application of Process Dates: SPP_ApplyDocumentProcessDate

Version 2.2.6 (Nov 5th 2010)
-----------------------------
- Enhanced CclaBatchValidator to check for required account fields
- Added Job Priority Admin views

Version 2.2.7 (Nov 12th 2010)
-----------------------------
- Updated getNextPendingBundle method to run alternate query for 'Mandate-only' users. See env.
  var CCLA_mandateOnlyBundleUsers.

Version 2.2.8 (Nov 16th 2010)
-----------------------------
- Amended the UpdateFilter to ensure xFund values are always cast to uppercase.

Version 2.2.9 (Nov 20th 2010)
-----------------------------
- Added support for updated SPP web services GetComplaintCategories and 
  GetComplaintSubCategories.
- Added 'createdBy' and 'subCauseId' parameters to the QueryJobData class.

Version 2.3.0 (Nov 22nd 2010)
-----------------------------
- Fixed hard-coded adminTM username in setWorkflowWSCredentials method, which was rejecting
  authentication requests in live.
  
Version 2.3.1 (29/11/10)
-----------------------------
- Added code to enable multiple unflag of bundles (for report component but jar is the SPP one)

Version 2.3.2 (02/12/10)
-----------------------------
- Added logging to method checkForAllPendingDocsInBundle [BundleServices class]  (part of UCI00046).
- Added new method refreshAllBundlePriorities [BundlePriorityServices class] for refreshing all 
  bundle proirities (UCI00050)

Version 2.3.3 (08/12/10)
------------------------
- Added support for new Iris Dual Indexing functionality
- Requires new Flag Reason added: see sql_updates_20101206.sql
- Refactored DOCUMENT_CLASSES table, new data column SUBMIT_TO_SPP, and corrected data for IS_TRANSACTION.
  Requires sql_updates_20101208.sql and view refresh for vDocumentClasses in UCM
  
Version 2.3.4 (15/12/10)
-----------------
- New service method to support Dual Index reset function.
- (UCI00044) Updated ajax request to POST instead of GET
- (SPI00080) Added 'Created By' parameter to SPP query job trigger.

Version 2.3.5 (24/12/10)
-----------------
- (UCI00090) Added removeDuplicate method

Version 2.3.6 (13/01/11)
-----------------
- Fixed UCM-SPP date translations to account for timezone change. Times should always match between
  the systems now, regardless of the time of year.
- Enabling document Process Date functionality


version 2.3.7 (11/02/11)
-----------------
- (UCI00102) Fund Process Dates CR moved from ClientServices.

version 2.3.8 (14/02/11)
-----------------
- Minor bug fixes for UCI00102.

version 2.3.9, 2.4.0 (15/02/11)
-----------------
- UCI00120 - Process Dates CBF fixes.

version 2.4.1 (17/02/11)
------------------------
- UCI00123 Validation of Standing Orders at indexing stage
- Added new columns to DOCUMENT_CLASSES table. The script required for the SIGNATURES_REQUIRED
  column is in an update script in the CCLA_SignatureChecking component.

version 2.4.2 (25/02/11)
------------------------
- UCI00134 Fixed Process Dates Caching
- UCI00137 Fixed mandate checking

version 2.4.3 (17/03/11)
-----------------
- Attempting fix for UCI00138 (removed redundant lwDocument.update() call in 
  triggerSppWorkflow Java method)
- UCI00136 Client Query Boost
- UCI00148 Process Date Amendments
- UCI00151 additional mandates checks for pending bundles.
- UCI00154 CloneAndSubmitDocToSpp service.
  - Requires new SPP web service added. 
	-name:				CloneAndSubmitDocToSpp
	-IdcService:		CCLA_CLONE_AND_SUBMIT_DOC_TO_SPP
	-request params: 	docId field:int
						extraParams propertyList
						
Version 2.4.4 (23/03/11)
------------------------
- Amended 'Parking Lot Bundles' page to show extra totals.
- UCI00160 updated process date method in cloneAndSubmitDocument method.

Version 2.4.5 (24/03/11)
------------------------
- Fixed SPP boolean variable handling to be more strict. A String metadata
  value is only considered 'true' if all the following conditions hold: 
  -String is not null/empty
  -String does not match "N", "No", "False", "0"

Version 2.4.6 (07/04/11)
------------------------
- UCI00165 - The CCLA_CLONE_AND_SUBMIT_DOC_TO_SPP service has been updated to preserve 
  the original date of any CONDINS items that are passed to it.
- Updated load order of startup filter to 20 to ensure CCLA_Core startup 
  filter is called first.
- UCI00169 - Amended the UpdateFilter and ServiceSuccessFilter to enable automatic
  refresh of bundle priority on document class update in Iris.
- UCI00170 - Auto assign a priority of 10 when mandate is greater or equal than 8 days old.
- UCI00173 - Forced upper case for xDestinationFund and xDestinationAccount
- Run script CCLA_PublicHoliday_25032011.sql
- Run script job_priority_updates_07042011.sql

Version 2.4.7 (20/04/11)
------------------------
- UCI00187 Fixed Get Next Pending Bundle service to always return a bundle which is
  locked to the requester
- Added extra debugging to Refresh All Bundle Priorities button, to give a better
  indicator of its progress.
- Bundles won't have their priority updated if it hasn't changed via the Refresh
  All Bundle Priorities function.
  
Version 2.4.8 (06/06/11)
------------------------
- Minor updates to codebase to reflect changes in CCLA_Core. As a result this build
  is dependant on CCLA_Core v1.0.4.
- Moved most of the UpdateFilter code into ClientSerivces UpdateFilter.

Version 2.4.9 (24/06/11)
------------------------
- Added new services to handle checking of document Process dates in Iris
- UCI00210 Updated Get Next Pending Bundle service to refer to different list of users. 
  See config var MandateBundleUsers.
- UCI00206 Process Date notifications
- UCI00210 Updated Get Next Pending Bundle service and user list  
- consolidated validation methods into panelValidationExtra
- UCI00202 Create Instruction from interactions. Requires 16_June_interaction_instruction_data_load.sql
- Enabled flag SPP_KeepOriginalDatesForCloneDocuments, so cloned docs coming
  from SPP will always have a Release Date that matches their parent item.

Version 2.5.0 (01/07/11)
------------------------
- Refactored System Config Var cache to use new caching method

Version 2.5.1 (14/07/11)
------------------------
- Added getActiveBundleCounts service method. This fetches and caches a set of
  ResultSets which hold aggregate counters of active/pending bundles grouped
  by various attributes. The service and template are stored in the CCLA_MailHandling 
  component.
- Moved getNextPendingBundle codebase into CCLA_CommProc component.

Version 2.5.2 (08/08/11)
------------------------
- Preadvice/Dicondin CR work (disabled for now)
- Removed LWDocument(dID) constructors in favour of LWDocument(dID, true) to ensure
  direct database lookup is used, as opposed to GET_SEARCH_RESULTS. Many times more 
  efficient. Also replaced LWDocument(dDocName) with LWDocument(dDocName, true)
- Added new service method for fetching pending Mandate/App documents by client number
  and company. This is now used in place of the previous service method, which fetched
  all pending Mandate/App documents for all client number/company pairs in a given
  bundle. Much faster query.

Version 2.5.3 (22/09/11)
------------------------
- Refactored WorkflowProfile Java object into SPPJobProfile. Paving way for direct
  submission of Instruction entries to SPP.
- Changed pFILENAME mapped UCM metadata field to DocUrl instead of dOriginalName.

Version 2.5.4 (26/09/11)
------------------
- Removed mention of 'SPP' from various screens. Replaced with 'Workflow'
- FileStoreProvider Install: 
  Refer to http://extranet.extended-content.com/ucmxe/groups/public/@ccla/documents/case_document/xnet002765.pdf for details

Version 2.5.5 (18/11/11)
------------------------
- Added extra xUnits parameter to checkForDuplicateItems method.
- Added modified jar to build path (axis-ecs.jar), replaces old axis.jar. Required to
  fix empty xmlns="" attributes appearing in Aurora web service requests, and issues
  with multiple web service requests being fired when using NTLM authentication.
  See the ECS-CHANGES.txt in the jar's root directory for a full list of changes.

Version 2.5.6 (10/01/12)
------------------------
- Added timing log entries for all document-based Filter classes
- Deprecated UPDATE_DOCINFO and UPDATE_DOCINFO_BYFORM service overrides
- Bundle priorities are no longer updated after a new Bundle item is checked in. Priority
  calculation now occurs pre-checkin.
- Moved/added Bundle Priority config vars to CCLA_MailHandling component:
  - CCLA_refreshBundlePriorityOnBundleCheckin
  - CCLA_refreshBundlePriorityOnDocClassUpdate
  - CCLA_refreshBundlePriorityOnBundleStatusUpdate
- Added call to refresh priority of bundle items when their status is updated, via the
  existing updateItemStatus method. This ensures that bundle priorities are refreshed
  when a bundle is un-flagged, even when the 'CCLA_refreshPriorityOnDocClassUpdate' config 
  var is switched off. This behaviour can be toggled with the env var:
  - CCLA_refreshBundlePriorityOnBundleStatusUpdate
  
Version 2.5.7 (24/01/12)
------------------------
- Fixed potential bug with bundle priority update after bundle status is updated via
  updateItemStatus method.
- Bundle priority is now updated when a bundle is flagged.
- Refactored sequence names
  
Version 2.5.8 (08/02/12)
------------------------
- Removed redundant templates, services for unused views.
- Renamed views to v_[view_name]
  - Requires ucm_db_to_central_db_migration_view_01022012.sql in CCLA_CORE  
- Community First Change Request 
  - requires community_first_updates_01022012.sql
  
Version 2.5.9 (13/03/12)
------------------------
- Amended getNextDealingDate method to return the base processing date, if the next
  dealing date can't be found for a given Fund.
- BundleServices.panelValidationExtra() method now returns a ResultSet of form data, if 
  the document has a Form ID specified.
- Before triggering a standard SPP job, there was a check agains the document xMultiDoc 
  field. If the value was 'Yes' then the job submission was suppressed. This has been 
  updated to check if the doc class is 'MULTIDOC/MULTIINV' instead.  
- Added 3 new mapping fields to default Controller Map:
  -pINSTRUCTIONID
  -pUCM_GUID
  -pPAYMENT_DATE
  Previous mapping configuration is commented out in the env. file.
- Document GUID values are calculated and dispatched to workflow during job submission
- Quick fix to ensure INV jobs will still work with the metadata field overloading.
  During workflow job tests, a fully-indexed INV will fail, due to the use of the xUnits
  field for 'Invoice Number'. To remedy this quickly, there is an env. var in
  CCLA_CommProcessing which lists all UCM metadata fields which will be passed as blank
  values in their associated job data fields. This env var is called:
  - CCLA_CommProc_Invoices_BlankValuesOnWorkflowSubmission
  
Version 2.6.0 (23/03/12)
------------------------
-- Updated QueryJobProfile to have instructionId and xPaymentDate

Version 2.6.1 (23/04/12)
------------------------  
-- added idcToken for 11g migration
-- decoupled database and facade for 11g migration
   Requires Datapump to be performed.
- Removed old Iris workflow batch trigger methods.
- Added 1 new mapping field to default Controller Map:
  - pLIABILITY fed from the xDestinationOrgAccountCode field
- Content URLS will no longer be built and sent in workflow job data as pFILENAME values.
  The pFILENAME value will always be blank for future job submissions. To toggle this behaviour,
  add a non-null value to the new config var SPP_SEND_CONTENT_LINKS_WITH_JOB_DATA
  
Version 2.6.2 (27/04/12)
------------------------  
- Fix facade issue with CCLA_GET_NEXT_PROCESS_DATE_BY_JOB_ID.
- Fixed service calls for ECS_Q_GET_NEW_ENVELOPE_ID and ECS_Q_GET_NEW_WORKFLOW_ID 

Version 2.6.3 (30/04/12)
------------------------
- Fixed single item 'Submit to Workflow' button displayed on Iris document view.
- Removed commons-logging-1.0.4.jar from component build as this is not required (cause issues with 11g)

Version 2.6.4 (10/05/12)
------------------------
- Fixed calling of jobId and webservice:GetBundleRef
- change facade to ccla facade



Version 2.6.5 (24/05/2012)
-- This component has been converted to use the new ECS build process. 
-- Any changes must be put in the CHANGELOG not this readme. 
-- Configuration/installation notes can still be added to the top of this file

************** END **************