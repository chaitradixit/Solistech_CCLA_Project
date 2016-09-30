CCLA_MailHandling Component Release v2.0.2
================================================================================
  Full Release Name: 2015_01_03 v2.0.2 (Build 21928)

      Date: 03/13/2014 04:01 PM
    Author: tm
  Location: http://ecs-svn/stellent/CLIENTS/CCLA/Components/CCLA_MailHandling/tags/v2.0.2
           
(c) Extended Content Solutions Limited 2015
================================================================================
﻿CCLA_MailHandling Component Release v1.8.9
================================================================================
  Full Release Name: 2014_05_05 v1.8.9 (Build 18494)

      Date: 05/29/2012 11:16 AM
    Author: camli
  Location: http://ecs-svn/stellent/CLIENTS/CCLA/Components/CCLA_MailHandling/tags/v1.8.9
           
(c) Extended Content Solutions Limited 2014
================================================================================
﻿CCLA_MailHandling Component
===========================
Tom Marchant

Description
-----------

Contains include/environment overrides for the Iris product, tailoring
its use towards CCLA bundle indexing and document management.

Change log
----------

V1.0.0
------
-Initial release

V1.0.1 09/06/09
---------------
-Signature support

V1.0.2 16/06/09
---------------
-Baseline before CCLA deployment

V1.0.3 29/06/09
---------------
-Various bug-fixes

V1.0.4 05/09/09
---------------
-Support for client slip indexing
-Various enhancements to validation functions
-Aurora web service integration

V1.0.5 17/09/09
---------------
-Enabled 'Submit to SPP' button for single submissions on ChildDocuments
-Baseline build before moving component back to ECS dev

V1.0.6 21/09/09
---------------
-Enforced selection of 'XXX' document class when using multi-doc feature
-Allowed WF_Scanning Manager role access to Iris Reports menu

V1.0.7 23/09/09
---------------
-Allow deletion of bundles by WF_COO role and administrators
 -Bundle item released from workflow, status updated to Deleted
 -Bundle items marked as expired (dOutDate set to current date)
-Allow bundles to be manually marked as 'indexed'
 -Updated bundle status to 'Indexed'
-Preservation of Scanned Envelopes search/sort filters after submitting
 bundle to SPP

 V1.0.8 09/10/09
----------------
-Updated style sheet to add ClientServices custom classes

 V1.0.9 14/10/09
----------------
-Moved ClientServices styles out of component
-Members of WF_Scanning Manager can now unlock items in Iris
-Document Date field auto-completes to current date when focused
-Document Date field value can be changed using up/down arrow keys

V1.1.0 02/11/09
---------------
-Changed storage location of log4j files to D:\Logs
-Added checkForceLogin method to all service calls
-Fixed client name lookup function on batch indexing screen
-Fixed bug when paging through S&F with a date filter applied.
 This required an override to the S&F buildParameterList include - must
 be applied to base component.
-Shortened header bar so it occupies less space
-Added CCLA favicon
-Changed manual checkin screen to hide Envelope ID field (forces,
 auto-generation) and added 'Fax' option to Source field
 
V1.1.1 03/11/09
---------------
-Added Source filter field to Filing Cabinet view
-Changed first column of filing cabinet to show Content ID instead

V1.1.2 17/11/09
---------------
-New additions to support barcoded forms:
 -New metadata field required: FormId (text)
 -Add FormId field to CCLA Iris metadata config
  -custom include: ccla_form_id_fiel
 -Requires CCLA_ClientServices v1.0.7
-Updated Fund value auto-completion code to account for multiple-character
 fund codes. Done in Java backend and JavaScript front-end
-Amended client data lookup button. If an account number is specified,
 corresponding bank account no/sort code field values will be filled
 automatically.

v1.1.3 24/11/09
---------------
- Added Account Number to advanced search of filing cabinet
- Added year check onto Document Date entering

v1.1.4 10/12/09
---------------
- Multi/withinstructions enhancements

v1.1.5 15/12/09
---------------
- Cosmetic changes to With Instruction advanced options button

v1.1.6 17/12/09
---------------
- Changed date range validation to use new IrisCore JavaScript function
- Updated look and feel for Duplicate check pop-up window
- Exposed Report menu to all users

v1.1.7 18/12/09
---------------
- Added env. var:
  ECS_PopupMenus_use_fade_animations=
  (suppresses background fade on pop-up dialogs)
- Changed the query method used for determining duplicate items.
  A standard SQL query is now called on each bundle item instead of a
  GET_SEARCH_RESULTS call. This may help to speed up SPP submission.

v1.1.8 21/12/09
---------------
- Updated QGetDuplicates query to filter out old revisions and documents
  more than 5 days older than the comparison document.
- Added background SPP submission module for perceived speed improvements. 
  Can be turned off by setting CCLA_USE_BACKGROUND_SPP_SUBMISSION=0 in 
  ccla_mailhandling_environment.cfg.

v1.1.9 05/01/10
---------------
- Amended new pop-up submission to ensure user is redirected back to
  Envelope Listing page correctly (with their previous queries/paging
  intact)
- Updated Filing Cabinet page:
  - New Account No. column
  - Document Class values are now clickable
  - Implemented new batch item features from IrisBatchDocuments component
  - Client numbers now have a configurable search type when using Advanced
    Search mode
- New env. var CCLA_multiDocClasses holds a comma-separated list of document
  class values which can be indexed as multi-docs.
- Updated QGetDuplicates query to filter out existing items marked as 
  Duplicate.
  
v1.2.0 07/01/10
---------------
- New image added (tick.gif)
- Updated handling of SPP bundle submission. Checking in child docs is now
  a discrete process visible to the user

v1.2.1 08/01/10
---------------
- New env. var CCLA_synchronousChildDocCheckin added. If blank, child document
 checkins are executed asynchronously. Greatly increases checkin speed when you
 have many child documents.
- Housekeeping on resource files

v1.2.2 12/01/10
---------------
- Updated various IrisBatchDocument include overrides to reflect recent component 
  changes.
- Batch item display on the DOC_APPROVAL page is now controlled by the BATCH_GET_ITEMS
  service.

v1.2.3 13/01/10
---------------
- Updated QGetDuplicates query to exclude expired content.
- Removed most fixed links to document content and thumbnail images. These now use the
  new ccla_get_doc_url and updated searchapi_result... include. The only remaining fixed
  link can be found in the overriden URL link displayed on the standard content info page.

v1.2.4 19/01/10
---------------
- Fixed bug in thumbnail URL include which caused dead image links to appear for content
  items without proper thumbnails.
- Extended access to 'Delete Bundle' link in Iris interface so that members of 
  WF_Scanning Manager now have access.
- Added confirmation prompt to 'Delete Bundle' link
- Fixed bug which prevented persistence of custom ResultCount selection on the Filing
  Cabinet screen.
  
v1.2.5 28/01/10
---------------
- Added Checkout/Checkin button to single-document approval screen. This is a temporary
  addition to help fix all the failed checkout/checkins that occured recently.

v1.2.6 04/03/10
---------------
- Amended service call used to fetch form data via AJAX call.
- Added 'xFormId' to list of restricted batch-update metdata fields.

v1.2.7 11/03/10
---------------
-Errors during child document generation are now captured and displayed to the user,
 rather than ignored.
 
v1.2.8 19/03/10
---------------
-Added addBundleRef value to Add New Item screen. This ensures that items will be tagged
 with new bundle references on checkin, via the CheckinFilter in SPP_INTEGRATION component.

v1.2.9 31/03/10
---------------
-Fixed document class option list on Add New Item/Filing Cabinet screen, so it uses the 
 new vDOCUMENT_CLASSES UCM view. 
-Removed 'Show pending documents' checkbox on Filing Cabinet view 

v1.3.0 08/03/10
---------------
-Added Form ID filter to Filing Cabinet view.
-Enhanced display of content IDs in Filing Cabinet view: Form IDs are now displayed
 beneath content ID where applicable.

v1.3.1 12/04/10
---------------
-Added custom client ID indexing field (see include ccla_client_num_field). Adds an extra
 button which will open the corresponding Client Info page in the Client Services 
 interface. 
-Removed the signature lookup button for now. 

v1.3.2 13/04/10
---------------
-Amended 'Add new item' screen to allow checkin of items without generating a parent
 envelope item.

v.1.3.3 21/04/10
----------------
-Minor amendments to custom xFormId field display: 'Update Form Status' link now opens
 in new browser window, rather than a pop-up div which obscured the document behind. 

v.1.3.4 22/04/10
----------------
-Amendment to Confirm SPP Release pop-up panel. Takes into account supporting document
 retention, see build 2.1.0 of SPP_INTEGRATION component. 

v.1.3.5 13/05/10
----------------
-Updated option list displayed underneath With Instruction field to include AUTOMAND items.
 See include: ccla_get_with_instruction_parent_docs
 
v.1.3.6 14/05/10
----------------
-Updated thumbnail links used on Scanned Envelopes page to function in the same way to the
 text-based bundle reference links. This ensures S&F variables are passed when opening the
 DOC_APPROVAL page.
-Added MANDSHORT and APPSHORT as permissible 'With Instruction' document classes. See
 JavaScript function openChildDocPopup().

v.1.3.7 20/05/10
----------------
-Updated option list displayed underneath With Instruction field to include AUTOMAND items.
 Added APPSHORT class.
 See include: ccla_get_with_instruction_parent_docs

v 1.3.8 03/06/10
----------------
-Fixed issue which prevented display of ChildDocument content in single-document view in Iris,
 if a ChildDocument has a ChildDocument parent itself. See iris_get_content_url_js include.
 
v 1.3.9 03/10/10
----------------
-Amended VerifyMandate workflow 'Update' event with the following exit condition:

	<$if xStatus like "Pending"$>
		<$wfExit(100,100)$>
	<$endif$>

 This is used when Parking lot items in this workflow step need to be taken out of workflow,
 without submitting them to SPP first.
-New template/service method for resubmitting 'Parking Lot bundles', i.e. Completed bundles
 which contain items now held in the Mandate Parking Lot.
 Access the page using this service: CCLA_GET_PARKING_LOT_BUNDLE_COUNTS
 Code for these methods held in SPP_INTEGRATION component.
-New 'power features' in Iris include an updated Index Data Lookup button, pointing at central
 database and an Account Info button. These don't work currently due to rushed go-live rollout.
 Will be fixed in next build.
-Indexing panels reordered, so Bundle Details appears after Item Details. 

v 1.3.9a 04/10/10
-----------------
- Reverted lookup button on Iris document indexing to point at old Aurora web services.

v 1.4.0 04/10/10
----------------
- Enabled 'Flag with Reason' pop-up window and audit action. Back-end code in SPP_INTEGRATION

v 1.4.1 05/10/10
----------------
- Added support for Document Metadata snapshots, using new table DOCMETA_AUDIT. Run the dated
  SQL file in the scripts directory to generate the required table/sequences. 
  
v 1.4.2 07/10/10
----------------
- Added new field: xPriority
- Amended duplicate checking code (see SPP_INTEGRATION component)
- Added support for automated bundle flagging when there is a disagreement over document
  duplicate status
- Amended bundle auto-fetching: there is now a configurable delay before the fetch page
  will query for the next pending bundle. This can be adjusted via the env. variable:
  CCLA_intialAutoFetchDelay
  
v 1.4.3 08/10/10
----------------
- Added CCLA_minimumBundleAge env. var, this restricts very new bundles being returned by the
  auto-fetch function.

v 1.4.4 20/10/10
----------------
- Fix Parking Lot Bundle fetch queries so they always return bundles in ascending date order.
- Added refresh button to panel, moved open next bundle to right, added anchor links and 
  reordered workflow/bundle panel, made some style changes
  
v 1.4.5 25/10/10
----------------
- Added custom field display for new Process Date field. Only editable by WF_COO, WF_Scanning
  Manager and admin roles. Requires Configuration update for Document/ChildDocument metadata 
  configurations.
- Switched off display of batch item paging panel.

v 1.4.6 31/10/10
----------------
- Batch validation template enhanced to list documents that lack required account fields.
  
v 1.4.7 21/11/10
----------------
- Added new env. var CCLA_mandateOnlyBundleUsers. This should be a comma-separated list of 
  user names who will only be presented with bundles containing mandates when using the
  auto-fetch function.
- Fixed bug on the Process Date Admin screen, which was incorrectly calculating the base
  process date (based on the current time).

v 1.4.8 26/11/10
----------------
- Added user-friendly error message if a ServiceException occurs during bundle auto-fetching
  (UCI00042)
  
v 1.4.9 02/12/10
----------------
- Moved pending mandate checks when document is loaded for indexing. 
  (UCI00046). Requires latest SPP_Integration (v2.3.2) package.
- Updated fix for UCI00046 to include transactions as well.

v 1.5.0 (08/12/10)
-------
- (TM) Amended QGetAllPendingDocsForClientCompanyForBundle query to ensure old document 
  revisions arent returned. See issue UCI00046
- Removed hard-coded check list to read of database table instead (UCI00058). Requires SPP_INTEGRATION v2.3.3


v 1.5.1 (09/12/10)
------------------
- Fixed Iris javascript.

v 1.5.2 (15/12/10)
-----------
- Added new service CCLA_RESET_BUNDLE_DUAL_INDEXING. New link available in Bundle Details view: 
  'Reset Dual Indexing'. This calls the new service and will generate new Dual Index items for the
  given bundle.
- UCI00044 Updated AJAX request to POST instead of GET
- Added new 'lock checks' to intial bundle submission confirmation screen, and the subsequent screen
  which lists the items which will be sent to SPP. Users will be unable to progress at each stage
  unless they have the lock on the bundle.

v 1.5.3 (24/12/10)
-----------
- (UCI00090) Added removeDuplicate method


v 1.5.4 (14/02/11)
-----------
- (UCI00108) Added checks for standing orders.

v 1.5.5 (14/02/11)
-----------
- Signature fixes

v 1.5.6 (15/02/11)
-----------
- UCI00121 Fixed standing order validation

v 1.5.7 (17/02/11)
-----------------
- UCI00123 Validation of Standing Orders at indexing stage
- Updated Document Class queries, JavaScript object to include new columns 
  (IS_STANDING_ORDER, SIGNATURES_REQUIRED)

v 1.5.8 (25/02/11)
-----------------
- UCI00137 Fixed mandate checking

v 1.5.9 (07/03/11)
------------------
- UCI00083 As part of Entity Checking enhancements, the database screens need
  a view onto the Filing Cabinet to allow linking of documents. The Filing
  Cabinet now supports a 'select' mode (&selectMode=1) along with a callback
  JavaScript function, doDocSelectCallback().
  
v 1.6.0 (17/03/11)
------------------
- UCI00151 additional mandates checks for pending bundles.
- Added new Audit Log type CLONE-AND-SUB.

v 1.6.1 (25/02/11)
------------------
- UCI00159 Amended 'Parking Lot Bundles' page to show extra totals

v 1.6.2 (07/02/11)
------------------
- Updated 'Get Next Pending Bundle' queries to order by the following:
  -xPriority DESC
  -xStatus (EnterDetails, Validation) DESC
  -dInDate ASC
  So a bundle in the Validation step which contains identical items to a bundle
  in the EnterDetails step will always take precedence, regardless of date.
- Bundle priority is now displayed in faded text in the Bundle Details panel.  
- Added support for auto-refresh of bundle priority after Document Class is 
  updated in Iris. Requires latest build of SPP_INTEGRATION component.
  Functionality can be switched on/off using the env. var:
   CCLA_refreshBundlePriorityOnDocClassUpdate
- Added Scan User field on Add New Item screen for administrators only. Useful
  when testing priority rules.

v 1.6.3 (20/04/11)
------------------
- Amended 'Get Next Pending Bundle' initial delay and retry times

v 1.6.4 (04/05/11)
------------------
- UCI00188 Document Class captions displayed on document thumbnail panel will now 
  show a '?' character when a bundle is in Validation status. This is used to mask 
  the previous user's choice of Document Class so it doesn't influence their decision
  while dual indexing.
- Various changes to support Instruction Register translation. Includes new audit
  messages.
  
v 1.6.5 (18/05/11)
------------------
- Amended Form ID data lookup button to work with new Form data/table.  

v 1.6.6 (23/05/11)
------------------
- Amended some bundle audit messages to deal with new messages coming from the
  CommProcessing component.

v 1.6.7 (24/05/11)
------------------
- Added default xSource value of 'User Upload' for items uploaded to existing bundles.

v 1.6.8 (26/05/11)
------------------
- Minor Change - Slimmed down bundle page UI

v 1.6.9 (31/05/11)
------------------
- UCI00191 Added warning for specific fields before flagging the bundle. 
  Controlled using var CCLA_DualIndexFinalCheck. 
- Updated Bank Account field display in Iris to indicate validity of bank account 
  details.

v 1.7.1 (24/06/11)
------------------
- (PT) Updated bank account details colouring functions 
- UCI00210 Updated Get Next Pending Bundle queries:
  - both queries now exclude SCANUSER4 bundles
  - the mandate-only query fetches SCANUSER3 bundles, as well as bundles containing
    mandate items
- UCI00210 List of 'mandate-only' bundles now uses the System Config Var 
  MandateBundleUsers, instead of the environment var.
- Added xBatchNumber to Iris_batchCopyFields env. var. This ensures that 
  newly-generated bundle items will copy over the SPP Batch ID number from the bundle
  document, if it exists.
- UCI00211 Updated Add New Item template to ensure the SPP Batch ID field is always 
  enabled, changed info messages that are displayed in the form footer.
- Client/Account Lookup button in Iris now fetches a list of all related
  bank accounts for a given CCLA account. The user can tab through/click on a returned
  bank account to copy in the bank account no/sort code into the corresponding metadata
  fields. The nominated withdrawal account is displayed in green (if present)
- Further to the above, the xBankAccountNumber and xSortCode fields are forced into
  a read-only state when the doc metadata panel is loaded. Their contents can only be
  changed by selecting a linked bank account.

v 1.7.2 (01/07/11)
------------------
- Disabled access to Scanned Envelopes and Filing Cabinet views for all users except
  members of the 'admin' and 'WF_COO' roles.  

v 1.7.3 (14/07/11)
------------------
- Fixed bug in GetNextPendingBundle queries which prevented bundles being returned if
  their xScanUser field was empty.
- Rewired Get Next Pending Bundle service to point at new codebase location in 
  CCLA_CommProcessing.  
- Removed 'Flagged' bundle exclusion from Get Next Pending Bundle queries.  
- Added 'Bundle Counter' template/service, accessible via Admin menu.

Version 1.7.4 - 14/07/2011
------------------
- Added dInDate parameter to document info/update panel
- Amended Bundle Counter page to auto-refresh as soon as new figures are available

Version 1.7.5 - 08/08/2011
--------------------------
- Preadvice/Dicondin CR work (disabled for now)
- UCI00234 (this was hot-fixed in live on the 19/07/2011)
- Changed redirect URL on Add New Item page to point back to same page
- Added Company field to Add New Item page
- Added new service method for fetching pending Mandate/App documents by client number
  and company. This is now used in place of the previous service method, which fetched
  all pending Mandate/App documents for all client number/company pairs in a given
  bundle. Much faster query.
- Run all scripts in dir GetNextPendingBundle. Adds new table and triggers, used to
  replace current GetNextPendingBundle queries.

Version 1.7.6 - 09/09/2011
--------------------------
- Replaced GetNextPendingBundle queries to point at new ActiveBundle table

Version 1.7.8 (18/11/2011)
--------------------------
- Added some fixes to ensure batch items are displayed correctly when using 
  FileStoreProvider
- Removed mention of 'SPP' from various screens. Replaced with 'Workflow'
- UCI00241
  Added check for xUnits value as well as xAmount when Duplicate Checking.
  If an xAmount or xUnits value is present, both will be checked against
  xAmount AND xUnits values on other documents. So a document with an xAmount
  of '1000' may match against a document with an xUnits value of '1000'
- Dependant on version 2.5.5 of SPP_INTEGRATION
- Added help button feature and admin - Requires CCLA_CS_HELP_BUTTON_21102011.sql 
  in (CCLA_ClientServices)
- Added timestamp to Lookup Account Data function in Iris, to ensure server is
  always queried for bank account list etc.
- Added Bundle Priority config vars:
  - CCLA_refreshBundlePriorityOnBundleCheckin
  - CCLA_refreshBundlePriorityOnDocClassUpdate
  - CCLA_refreshBundlePriorityOnBundleStatusUpdate

Version 1.7.9 (30/11/2011)
--------------------------  
- Added xDocumentDate to Iris_preventBatchUpdateFields list.  

Version 1.8.0 (21/12/2011)
--------------------------
- Added Bundle Priority calculation configuration vars to this component.
- Added special user list env. var CCLA_indexAndValidateUsers.
  - Any users in this list will be able to index AND validate the same bundles.
  - Replace the CCLA_MailHandling EnterDetails Exit step IdocScript with the below:

<$isSpecialIndexer = ""$>
<$exec rsMakeFromString("rsIndexAndValidateUsers", #env.CCLA_indexAndValidateUsers)$>

<$loop rsIndexAndValidateUsers$>
	<$if strEquals(dUser,rsIndexAndValidateUsers.row)$>
		<$isSpecialIndexer = 1$>
	<$endif$>
<$endloop$>

<$if (#local.isSpecialIndexer)$>
	<$wfUpdateMetaData("xIndexer","SpecialIndexer")$>
	<$wfSet("lastIndexer","SpecialIndexer")$>
<$else$>
	<$wfUpdateMetaData("xIndexer",dUser)$>
	<$wfSet("lastIndexer",dUser)$>
<$endif$>

<$wfUpdateMetaData("xStatus","Validation")$>
<$bundleRef = xBundleRef$> 
 
- Archive job required to update all existing bundles where the xIndexer value is 
  'CCLA\j.campbell'. The xIndexer value should be updated to 'SpecialIndexer' instead.
  This will ensure the user can validate bundles they indexed previously to this
  update.

Version 1.8.1 (31/01/2012)
--------------------------
- Added CCLA\\d.stewart to CCLA_indexAndValidateUsers 
- Refactored DB sequence name
  
Version 1.8.2 (13/03/2012)
--------------------------
- New metadata fields. Upload attached CMU bundle: NewMetadataFields_06032012. Adds 
  the following new fields:
  - OrgAccountCode (text)
  - PaymentDate (date)
  - DestinationOrgAccountCode (text)
  - Description (long text)
  - Currency (text)
  - CapitalOrIncome (text)
	- Option List values: Capital, Income
- Upload CCLA IrisProfile to content server (checkin new revision). Has Content ID of
  'CCLA'.
- Custom Iris profiles for INV and MULTIINV document classes
- Added xOrgAccountCode metadata field and Iris lookup functionality
- Added onchange event to look up profile conditions when changing document class. 
  To disable change CCLA_ENABLE_DYNAMIC_DOCCLASS_PROFILE_CONDITION to 0
- Added 'View subscription details' link to Iris indexing panel, underneath the
  Form ID panel.
- Added condition config to content item: CCLA
- Run invoice_fields_added.sql to add the db rows for invoice metadata mapping
- Moved lookupClientData and associated CCLA/Iris Javascript functions into the
  iris_client_num_field include, so the functions are still available without an
  account number present.
- Refactored element_attribute_applied and form tables, queries and code to use GUID's 
  instead of DOC_ID's
- Removed Client Number length checking from Iris doc validation. Client Numbers are
  now auto-padded at the back-end.
- Added header reference to new Core JavaScript file, ccla_indexing.js. See include
  doc_approval_extra.  
- New screen for indexing Invoice line items. Services/back-end code in 
  CCLA_CommProcessing component
- Fixed tab focus issue 
  
Version 1.8.3 (23/03/2012)
--------------------------
- Added new metadata field:
  - PaymentRef (text) Label: Payment Ref
- Fixed iris indexing screens which allows indexing of negative amounts 
  for docclasses in the list "CCLA_allowNegativeAmountDocClasses".
- Added Org Code, Dest Org Code, Units/Inv No, Company search fields to Filing Cabinet 
  view
- Document Class display on Filing Cabinet view now shows Units/Inv No and Amounts in
  the sub-text.


Version 1.8.4 (23/04/2012)
--------------------------
-- added idcToken for 11g migration
-- fixed 11g login button issue

Version 1.8.5 (04/05/2012)
--------------------------
-- updated workflow token code for 11g compatibilty 

Version 1.8.6 (18/05/2012)
------------------
- Added debug switch debugDocUrlGeneration to output doc/thumbnail URL resolution to the
  Content Server log.

Component dependencies
----------------------

-ECS_BaseUtils
-ECS_SearchAndFilter
-IrisCore, IrisProfiles
-IrisBatchDocuments
-SPP_INTEGRATION

Config requirements
-------------------

-New Iris profile: CCLA

-New docTypes: 
	'Document' - used for all scanned content
	'ChildDocument' - child docs
	'Bundle' - grouping item for scanned bundles
	
-New metadata fields:
  'xIndexer' [string]
	'xBatchNumber' [int]
	'xStatus' [string]
	'xDocumentClass' [string]
	'xScanDate' [date]
	'xWorkflowDate' [date]
	'xSupportingDoc' [string]
	
-New workflow: CCLA_MailHandling
  Step 1: EnterDetails

  
  
Version 1.8.8 (24/05/2012)
-- This component has been converted to use the new ECS build process. 
-- Any changes must be put in the CHANGELOG not this readme. 
-- Configuration/installation notes can still be added to the top of this file

Version 1.8.9 (29/05/2012)
-- Fixed childDocument thumbnails on FilingCabinet page.

Version 1.9.0 (12/06/2012)
-- IPad style changes.

************** END **************
