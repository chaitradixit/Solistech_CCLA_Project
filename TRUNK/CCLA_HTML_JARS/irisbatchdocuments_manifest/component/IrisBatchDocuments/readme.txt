IrisBatchDocuments Component Release v1.3.0
================================================================================
  Full Release Name: 2014_07_08 v1.3.0 (Build 19304)

      Date: 08/31/2012 06:42 PM
    Author: camli
  Location: http://ecs-svn/stellent/PRODUCTS/Iris/Components/IrisBatchDocuments/tags/v1.3.0
           
(c) Extended Content Solutions Limited 2014
================================================================================
IrisBatchDocuments Component
============================
Stewart Leach

Description
-----------
Used to allow grouping of documents under a single batch/bundle item. The
'grouping' field is defined as xBundleRef by default. This field, plus
the dDocType of parent bundle/child items can be configured via environment
variables.

Checkin filters will automatically detect child documents being added 
to the system. If there is no parent bundle item with a corresponding bundle ref 
value, the parent bundle item is automatically generated.

The content iframe is displayed differently when a batch is opened in
approval mode. A filmstrip-style display shows all child items belonging
to the bundle, with the currently-selected item being displayed below in
another iframe.

Component dependencies
----------------------
-IrisCore
-IrisProfiles
-ECS_Paginator_Iris
-ECS_SearchAndFilter
-ECS_BaseUtils
-ECS_AuditLog
-PopupMenus

Required metadata fields
------------------------
-Default bundle ref field:
 xBundleRef [Text]

-Filing cabinet default fiels:
 xDepartment [Text] (values: various top-level departments)
 xSubDept [Text] (values: various sub-departments)

Required security groups
------------------------
-Mail

Notes
======
The workflow example resource file is for reference only, and does not yet 
have a correpsonding 

Overriding 'IRIS_DOC_THUMBS' service from IrisCore component.  This also has an overridden template, 'IRIS_THUMB_PREVIEW'.
This is for updating the page to allow documents to be attached and a thumbnail view displayed.
UPDATE: This service/template is no longer in use.

Version History
===============

Version 1.0.0  - 4th Aug 2008
-----------------------------
-First formal release

Version 1.0.1  - 27th Aug 2008
-----------------------------
-Updates to S&F config to support interoperability
between Iris and Hera

Version 1.0.2  - 30th Dec 2008
------------------------------
-Various updates to support single-document indexing
 alongside bundle indexing
 
Version 1.0.3  - 20th May 2009
------------------------------
-Vertical thumbnail listing
-Batch locking
-Batch updating

Version 1.0.4  - 8th June 2009
------------------------------
-Removed dependencies on xBundleRef field
-Added support for bundle ref fields with numeric datatypes

Version 1.0.5  - 9th June 2009
------------------------------
-Added support for batch item paging and hotkeys
-Added support for optional list of child batch item docTypes
-Error handling for missing parent batch items

Version 1.0.6  - 16th June 2009
-------------------------------
-Baseline before CCLA deployment
-Batch detection improved, items with an empty batch ID field
 will no longer trigger parent batch item creation.

Version 1.0.7  - 29th June 2009
-------------------------------
-Auto-save functionality when paging through thumbnails

Version 1.0.8  - 5th Jan 2010
-----------------------------
-Added new service IRIS_BATCH_GET_ITEM_COUNTS. Useful when
 a set of batch item counts is required at once, i.e. on
 a search results page. 
 This new service is referenced in the JavaScript include:
 iris_sf_bundle_item_counts_js
-Amended Thumbs/Preview templates to remove unneccesary
 GET_SEARCH_RESULTS calls
 
Version 1.0.9  - 12th Jan 2010
------------------------------
-Updated Java code to use LWFacade as parameter argument, instead
 of Workspace instances.
-Removed unneccessary call to BATCH_GET_ITEMS on DOC_APPROVAL page.
 This was original used to display the number of batch items on the
 Thumbnails tab label. This is now achieved using a JavaScript
 callback after the thumbnail frame loads.
-Added new env. var Iris_useBatchItemSearchResults. If set to true,
 a GET_SEARCH_RESULTS call is used to fetch batch items on the
 DOC_APPROVAL page. If false, the BATCH_GET_ITEMS service is used
 instead.

Version 1.1.0  - 13th Jan 2010
------------------------------
-Added new env. var Iris_contentUrlHash. The value of this var is
 appended to any content URL for batch documents. Useful for passing
 in parameters for consumption by PDF plugins.
 
Version 1.1.1  - 18th Jan 2010
------------------------------
-Fixed bug in getWIPBatchItems query building code.
-Updates to JavaScript used to add bundle notes.

Version 1.1.2 - 11th May 2010
-----------------------------
-Changed default value of Iris_batchEditMode to 'document'. The 'parent'
 mode no longer works and needs to be re-implemented.
-Modified thumbnail strip to hide selected item panel by default.
-Extra classes added to thumbnail elements to ensure they display correctly
 in the default vertical mode.
 
Version 1.1.3 - 26th Oct 2010
-----------------------------
-Added env. var. toggle for batch item paging controls: 
	Iris_enableBatchItemPagingControls
 This is switched on by default.
 
Version 1.1.4 - 8/12/2010
----------------------
-Added Dual Indexing support. Includes the following:
 -New schema (see scripts directory)
 -Dual Index match results page
 -Configuration page IRIS_DUAL_INDEX_CONFIGURATION, allows configuration for 
  metadata field dual indexing by document class (xDocumentClass). Rule
  creation is restricted to CCLA requirements for now.
 -New getBatchItems Java method, accepts flag to force substitution of batch
  item field data with pending Dual Index data, where applicable.

Version 1.1.5 - 09/12/2010
--------------------------
-Improved performance of Dual Index Configuration screen when there are many
 rule fields.
-Updated compareBundleDualIndexItems to remove the entry from 
 DUAL_INDEX_PARENT_ITEMS following a persisted comparison.

Version 1.1.6 - 15/12/10
-----------------
-New support for Dual Index item regeneration.
-Updated Dual Index Administration form to use POST method instead of GET,
 should fix form submission issue in IE.

Version 1.1.7 - 15/01/11
------------------------
-Extra item lock request on page load, this helps to avoid bundles being auto-
 unlocked on page reloads.
 
 Version 1.1.8 - 26/05/11
------------------------
 - Minor change: Slimmed down bundle page UI
 
Version 1.1.9 - 24/06/11
------------------------
- Moved the onunload removeItemLock() event to beforeunload to improve reliability
  of automatic unlocking

Version 1.2.0 - 14/07/11
------------------------
- Undid previous removeItemLock() change
- Added extra display controls and services for Dual Indexing
- 'Resetting' Dual Indexing for a bundle will no longer fail if there is a parent
  Dual Index item present. Instead it will be ignored and any items with a 'failed'
  Dual index attempt or no previous attempt will have Dual Index items generated.
  
Version 1.2.1 - 08/08/2011
--------------------------
- Added dInDate parameter to document info/update panel
- Replaced LWDocument(dDocName) constructor references with LWDocument(dDocName, true)

Version 1.2.2 - 26/09/2011
--------------------------
- FileStoreProvider Install: 
  Refer to http://extranet.extended-content.com/ucmxe/groups/public/@ccla/documents/case_document/xnet002765.pdf for details

Version 1.2.3 - 21/12/2011
--------------------------
- Added timing logs to BatchCheckinFilter methods

Version 1.2.4 - 03/05/2012
--------------------------
- Removed physical reference to Bundle.txt when creating a batch item. 
- made the weblayout folder name all lower case as path's are case sensitive in linux. 
   
   

Version 1.2.5 (24/05/2012)
--------------------------
-- This component has been converted to use the new ECS build process. 
-- Any changes must be put in the CHANGELOG not this readme. 
-- Configuration/installation notes can still be added to the top of this file

Version 1.2.6 (12/06/2012)
--------------------------
-- Ipad Style changes


Version 1.2.7 (??/??/2012)


Version 1.2.8 (??/??/2012)


Version 1.2.9 (19/07/2012)
--------------------------
-- Webcenter spaces compatibility fix.

************** END **************   