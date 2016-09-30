CCLA_reports Component Release v1.5.4
================================================================================
  Full Release Name: 2014_09_07 v1.5.4 (Build 22148)

      Date: 07/09/2014 04:07 PM
    Author: tm
  Location: http://ecs-svn/stellent/CLIENTS/CCLA/Components/CCLA_reports/tags/v1.5.4
           
(c) Extended Content Solutions Limited 2014
================================================================================
﻿CCLA_reports Component
======================

Build 3 (17/03/2009)
--------------------
Tom Marchant

-Added extra Java class, service and query to fix corrupted document classes.
-Execute by running service CCLA_FIX_DOC_CLASSES
-For directed logging of this component, add the following lines to ECSLogConfig:

log4j.appender.mylogfile5=org.apache.log4j.DailyRollingFileAppender
log4j.appender.mylogfile5.File=c:\\ECS_CCLA_FIX.log
log4j.appender.mylogfile5.DatePattern='.'yyyy-MM-dd 
log4j.appender.mylogfile5.layout=org.apache.log4j.PatternLayout
log4j.appender.mylogfile5.layout.ConversionPattern=%d{HH:mm:ss:SSS} %c[%t] %-5p - %m %x %n
log4j.logger.com.ecs.stellent.ccla=DEBUG,mylogfile5

Build 4 (07/09/2009)
--------------------
-Report headers updated to use Iris header
-Updated content report to use Iris configuration (i.e. show bundle ID/workflow ID separately)

Version 1.0.1 (11/09/2009)
--------------------------
-Added new view to Content Reports (displays items without Workflow Date set)

Version 1.0.2 (17/09/2009)
--------------------------
-Updated Aurora web services to use revised WSDL spec
-Aurora web service requests now use NTLM authentication (username/password in .cfg file)
-Added web services test page

Version 1.0.3 (21/09/2009)
--------------------------
-Consolidated report queries and templates from SPP_INTEGRATION component
-Improved presentation of reports, fixed queries and Java code to filter expired/old revisions

Version 1.0.4 (09/10/2009)
--------------------------
-Updated web service login credentials to use special UCMAuroraWebService user account -
 exists on UAT and live
-Added new web services provided by Kainos for Client Services component

Version 1.0.5 (16/10/2009)
--------------------------
-Fixed bug caused by fetching closed accounts
-getAccountsResultSet() Java method now has an extra parameter, which will filter closed
 accounts from the returned ResultSet
 
Version 1.0.6 (30/10/2009)
--------------------------
-Unit prices are now correctly converted to pounds

Version 1.0.7 (03/11/2009)
--------------------------
-Extended Content Report now shows Source column (xSource value)
-Extended Content Report can be filtered by Source

Version 1.1 (10/11/2009)
--------------------------
-Fixed queries so the two reports correspond to one another.
-Changed query built in UcmReporting.java rundExtendReportQuery to search by dInDate rather
 than dCreateDate

Version 1.1.1 (12/11/2009)
--------------------------
-Fixed issue with number of Mandates in including Swordfish Migration documents

Version 1.1.2 (17/11/2009)
--------------------------
-Changed scope of padClientNumber() Java method to public
-Added extra error logging when fetching correspondent accounts via Aurora web service

Version 1.1.3 (24/11/2009)
--------------------------
- Added Duplicate handling to extended content report

Version 1.1.4 (30/11/2009)
--------------------------
-Misc java tweaks for CCLA_ClientServices v1.1.0


Version 1.1.5 (10/12/2009)
--------------------------
-Misc java tweaks for CCLA_ClientServices v1.1.1

Version 1.1.6 (14/12/2009)
--------------------------
-Added Fund transfer reporting

Version 1.1.7 (15/12/2009)
--------------------------
-Change to Extended Content Report: items with xStatus of Archived will now be displayed with
 a status of 'Archived' instead of 'Pending in Iris'
-Tweaked Fund transfer report to include client name, and units plus total calculator
-Amended try/catch block in AuroraWebServiceHandler code to fix error caused by printing certain
 transfer forms

Version 1.1.8 (16/12/2009)
--------------------------
- Enhancements made Fund transfers report - added CS_FUND_TRANSFERS_NOTES table

Version 1.1.9 (17/12/2009)
--------------------------
- Enhancements made Fund transfers report - added Summary View

Version 1.2.0 (18/12/2009)
--------------------------
- Modifications made to Summary View of Fund Transfers report

Version 1.2.1 (29/12/2009)
--------------------------
- Amended queries used on Fund Transfers report so they reference CS_ACCOUNTS table
  instead of TEMP_ACCOUNTS

Version 1.2.2 (30/12/2009)
--------------------------
- Added new functionality on Fund Transfer report page to allow suspending/resuming
  of individual transfers. This has the effect of preventing suspended transfers from
  being returned by the GetUnbatchedTransfers web service.

Version 1.2.3 (05/01/2010)
--------------------------
- Fixed bug in UcmReporting class which caused a NullPointerException to be thrown, if
  a documentTypes variable was not present in the binder.
- Added 'Unclassified' document class filter to Extended Content Report. Allows searching
  on items with empty/null document class values

Version 1.2.4 (12/02/2010)
--------------------------
- Updated Aurora web service handler code. New method now available: GetBankDetails. Fetches
  bank/branch name for a given sortCode.
  
Version 1.2.5 (16/02/2010)
--------------------------
- Component now has code dependancy on CCLA_ClientServices. Uses core formatting functions to
  ensure that account numbers/sort codes are always returned with correct padding.
  
Version 1.2.6 (25/02/2010)
--------------------------
- Revised queries used for Parking Lot Report: mandate in/out counts were counting some 
  documents twice.
  
Version 1.2.7 (31/03/2010)
--------------------------
- Updated list of selectable document classes in Extended Content Reports to use the new
  vDOCUMENT_CLASSES query.
- Updated display logic used in Extended Content Report to treat AUTOMAND items the same
  as MAND items.
  
Version 1.2.8 (01/04/2010)
--------------------------
- Updated document status display in Extended Content Report. Some statuses are resolved
  immediatetely from the document xStatus value, rather than being calculated.

Version 1.2.9 (09/04/2010)
--------------------------
- Updated various queries used to calculate Mandate Parking Lot reporting figures. The
  queries now take into account the new AUTOMAND class and its check-in workflow called
  AutoMandateUpload.

Version 1.3.0 (20/04/2010)
--------------------------
- Extended Content Report was incorrectly showing Parking Lot items as 'Pending in Iris'.
  This has now been fixed.

Version 1.3.1 (17/05/2010)
--------------------------
- Fixed Summary/Full Report queries to filter out expired/deleted content.

Version 1.3.2 (19/05/2010)
--------------------------
- Amended Extended Content Report form to use GET request instead of POST. This allows
  people to save specific report filters as URLs.

Version 1.3.3 (22/09/2010)
--------------------------
- Removed all Aurora web service code and services to CCLA_ClientServices component.

Version 1.3.4 (05/10/2010)
--------------------------
- Added new Flagged Items report, accessible via service CCLA_FLAGGED_ITEMS_REPORT.
  Dependant on some queries in the CCLA_MailHandling component.

Version 1.3.5 (07/10/2010)
--------------------------
- Amended Flagged Items report, now uses Search and Filter database search to render
  the results.
  
Version 1.3.6 (21/10/2010)
--------------------------
- Amended view used by Flagged Items report. Run SQL update script to create new view.
- Added support for viewing bundle notes from Flagged Items Report, add new notes, and 
  unflagging bundles that are currently flagged.
  
Version 1.3.7 (05/10/2010)
--------------------------
- Added new Extended Content Report view (Cam Li). Linked via original report template.

Version 1.3.8 (29/11/10)
--------------------------
- Amended flagged item report so that you can select one or more flagged items and unflag them all at once.

Version 1.3.9 (16/12/10)
--------------------------
- Updated ExtendedViewReport to include selectable statuses and transactions.

Version 1.4.0 (14/01/11)
--------------------------
- UCI00029 Added datemasking to report fields. 
  - This has been removed for now from the Extended Content Report, due to issues with the time strings.
- UCI00096 Increased default date range on Flagged Items Report to 7 days
- UCI00098 Added 'Flagged' status to new Extended Content Report

Version 1.4.1 (04/03/11)
--------------------------
- UCI00139 - Added new document mismatch to reports

Version 1.4.2 (07/04/11)
--------------------------
- UCI00162 - Duplicate status display on Extended Content Report 
- UCI00163 - Remove old Extended Content Report 
- UCI00164 - Create new Duplicate report 

Version 1.4.3 (14/07/11)
------------------------
- Added Process Date filters
- Renamed Scan Date column to Creation Date
- Merged Duplicate Report into Extended Content Report (now uses same template/includes)
- Added 'searchMethod' paramter which determines the filters displayed on the Ext. Content Report.
  - all: 					default, see everything
  - duplicatesOnly: 		same as previous Duplicate Report
  - singleDocumentClass:	allows filtering by single doc class only
- New view CCLA_DOCUMENT_STATUS_WF. Same as CCLA_DOCUMENT_STATUS, but includes 4 extra
  columns containing bundle workflow auditing data.
- Extra submission button on Ext. Content Report allows generation of CSV file for download,
  instead of just displaying the results.
- Run the update script:
  - sql_updates_11072011.sql
  

Version 1.4.4 (31/01/12)
------------------------
- Added new flag "Process Date is Null" to extended content report
- Added new "Empty Process Date" Report (Pre-selected Extended Content Report)

Version 1.4.5 (10/02/12)
------------------------
- Removed redundant templates, services for unused views.
- Renamed views to v_[view_name]
  - Requires ucm_db_to_central_db_migration_view_01022012.sql in CCLA_CORE


Version 1.4.6 (23/04/12)
------------------------ 
-- added idcToken for 11g migration 

Version 1.4.7 (03/05/2012)
------------------------ 
-- changed service definition CCLA_FLAGGED_ITEMS_REPORT and CCLA_ODC_REPORTS to scriptable


Version 1.4.8 (24/05/2012)
-- This component has been converted to use the new ECS build process. 
-- Any changes must be put in the CHANGELOG not this readme. 
-- Configuration/installation notes can still be added to the top of this file

Version 1.4.9 (12/06/2012)
-- Fixed qIrisSummary report to work with ucm 11g upgrade.
-- Ipad style changes.

************** END **************