IrisCore Component Release v1.5.5
================================================================================
  Full Release Name: 2013_12_04 v1.5.5 (Build 20655)

      Date: 04/12/2013 11:18 AM
    Author: tm
  Location: http://ecs-svn/stellent/PRODUCTS/Iris/Components/IrisCore/tags/v1.5.5
           
(c) Extended Content Solutions Limited 2013
================================================================================
﻿IrisCore Component
==================
Tom Marchant

Contains core Iris templates and resource includes for
displaying listing page, approval page, check-in page etc.

NOTES: As of Version 1.0a, the auditing system used by
IrisCore is now outdated. It is expected that all auditing
will be updated to use the ECS_AuditLog component in the
future (as the new Iris Batch Documents component does)

Dependencies
------------
-IrisProfiles
-ECS_Paginator_Iris
-ECS_SearchAndFilter
-ECS_BaseUtils
-PopupMenus

Required fields
---------------
-xParentId

Required tables
---------------
-IRIS_LOCKEDITEMS
 -DOCNAME (varchar 100)
 -LOCKUSER (varchar 100)
 -LOCKDATE (date)
 -LOCKTIMESTAMP (varchar 15)

Build history
=============

Build 3 (27th March 2008)
Tom Marchant
-------------------------

-Removed Travelocity-specific code
-Added thumbnail support originally developed for M&S

Version 1.0a (4th Aug 2008)
Tom Marchant
---------------------------

-Iris batch documents component developed
-Overrides core includes and functionality
-Auditing overhauled, IrisCore must be updated.

Version 1.1.0 (18th Aug 2008)
Tom Marchant
-----------------------------

-Now supports auditing through ECS_AuditLog
-Many old services and resources removed
-Ensured interoperability between batch/standard Iris.

Version 1.1.1 (20th Aug 2008)
Tom Marchant
-----------------------------

-Paginator resource references are now namespaced to
prevent conflicts with standard paginator
-Various changes to support interoperability between
Iris and Hera.

Version 1.1.2 (27th Aug 2008)
Tom Marchant
-----------------------------

-Further changes to S&F configuration to support 
interoperability between Iris and Hera.

Version 1.2.0 (5th June 2009)
Tom Marchant
-----------------------------

-JQuery/AJAX enhancements
-Custom workflow action validation

Version 1.2.1 (9th June 2009)
Tom Marchant
-----------------------------

-Polishing

Version 1.2.2 (16th June 2009)
Tom Marchant
------------------------------

-Baseline before CCLA deployment

Version 1.2.3 (18th June 2009)
Tom Marchant
------------------------------

-Merged with ECS_Paginator_Iris component. Separate
 paginator component no longer required.
 
Version 1.2.4 (29th June 2009)
Tom Marchant
------------------------------

-Updated item locking to use database table and queries
 instead of metadata field update.
-Autosaving script methods added to supplement batch
 document editing
-Custom button support, uses anchor elements to allow
 button tabbing. Currently only supported on document
 metadata panel.
 
Version 1.2.5 (13th Oct 2009)
Tom Marchant
-----------------------------

-Changed various AJAX calls to use POST instead of GET.
 This helped to fix a caching problem when saving document info.
-Added new utility methods in iris_common.js to deal with
 JSON content returned by AJAX requests.
 
Version 1.2.6 (17th Nov 2009)
Tom Marchant
-----------------------------
-Added extra CSS class to core iris stylesheet

Version 1.2.7 (17th Nov 2009)
Tom Marchant
-----------------------------
-Added extra JavaScript validation method: validateDateRange().
 See iris_doc_validation_utils_js include.

Version 1.2.8 (21st Dec 2009)
-----------------------------
- Added submitWfActionGetForm javascript method for ajax posting 
  of approval form. Also modified submitWfAction but changes should
  have no effect on form submission.

Version 1.2.9 (5th March 2010)
------------------------------
- Amended include ecs_popup_hide_elements_extra to ensure pop-ups
  work on single-document approval screen.
  
Version 1.3.0 (11th March 2010)
-------------------------------
- Document metadata panel now supports view-based option lists.

Version 1.3.1 (20th April 2010)
-------------------------------
- Removed html, body {height: 100%} CSS rule in core stylesheet.
  This was causing issues with the pop-up div elements.
- Added new info icon and associated styles for informational
  messages.

Version 1.3.2 (12th May 2010)
-----------------------------
- Changed default profile name to MASTERPROFILE
- Promoted env. file load order to 15
- Added custom logging file IrisLogConfig and log config location
  to env. file.

Version 1.3.3 (7th Oct 2010)
----------------------------
-Added static utility methods for locking/unlocking of items.  

Version 1.3.4 (21st Oct 2010)
----------------------------
-Added javascript function so you can link to anchors on panels, made change to main_container class to reduce height  
  
Version 1.3.5 (Oct 2010)
------------------------
- Minor CSS enhancements. Assign classes to misc divs, and gave round
  corner gifs transparent background. (DJ)
  
Version 1.3.6 (Nov 2010)
------------------------
- Added extra check to acquireItemLock Java method, a ServiceException is
  now thrown if a user attempts to acquire an item lock for an item which
  is locked to somebody else.

Version 1.3.7 (8th Dec 2010)
----------------------------
- Updated codebase to use new Binder/ResultSet utility methods offered by new
  build of ECS_BaseUtils.
- Fixed bug which was causing JavaScript errors when document browse controls
  are disabled
  
Version 1.3.8 (21st Jan 2010)
-----------------------------
- Allowed configuration of auto-unlock minumum time delay. See env. var:
  Iris_autoUnlockMinimumTimeDelay. This has been set to 1300 ms to try and avoid
  items getting unlocked on page refreshes. 

Version 1.3.9
-------------
- Fixed minor JavaScript error on page load, see showMsg function

Version 1.3.10 (26/05/11)
-------------
- Minor Change - Slimmed down bundle page UI

Version 1.4.0 (31st May 2011)
------------------------------
- Added check statement to getDocPanel JavaScript method to prevent 'double-loading'
  of Doc Panel.
- Moved the onunload removeItemLock() event to beforeunload to improve reliability
  of automatic unlocking
  
Version 1.4.1 (14th July 2011)
------------------------------
- Moved the unbeforeunload removeItemLock() back to onunload, as IE triggers the
  previous event far too often (every time an href is clicked regardless of its 
  action)
- Added some extra hooks for Dual Indexing status display

Version 1.4.2 (8th Aug 2011)
----------------------------
- Added dInDate parameter to document info/update panel
- Modified pop-up div JavaScript hook to use special method instead of inline
  code. See include ecs_popup_hide_elements_extra
- Replaced LWDocument(dDocName) constructor references with LWDocument(dDocName, true)

Version 1.4.3 (18/11/2011)
--------------------------
- Fixed bug in Dual-Indexing document header region which broke all JavaScript in
  IE
- Modified error output when saving documents to be more clear and readable.  

Version 1.4.4. (21/12/2011)
---------------------------
- Enabled optional DB connection pooling when using item lock services. See the
  environment var Iris_itemLockPoolId 
  
Version 1.4.5 (23/04/2012)
---------------------------
-- added idcToken for 11g migration
-- refactored class loading code for 11g migration
  

Version 1.4.6 (23/04/2012)
-------------------------
-- Updated query names as this was conflicting with the core queries
-- fixed 11g login button issue
 
 Version 1.4.7 (04/05/2012)
-------------------------
-- added fix for 11g  std_page_end issue. 



Version 1.4.8 (24/05/2012)
-- This component has been converted to use the new ECS build process. 
-- Any changes must be put in the CHANGELOG not this readme. 
-- Configuration/installation notes can still be added to the top of this file

Version 1.4.9 (12/06/2012)
-- Ipad style changes

************** END **************