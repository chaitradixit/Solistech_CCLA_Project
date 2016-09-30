CCLA_SignatureChecking Component Release v1.1.9
================================================================================
  Full Release Name: 2014_02_08 v1.1.9 (Build 21203)

      Date: 08/14/2013 10:36 AM
    Author: tm
  Location: http://ecs-svn/stellent/CLIENTS/CCLA/Components/CCLA_SignatureChecking/tags/v1.1.9
           
(c) Extended Content Solutions Limited 2014
================================================================================
﻿CCLA_MailHandling Component
===========================
Paul Thomas

Description
-----------

Contains signature capture and checking functionality. 
Allows users to capture signature images from pdf documents and assign them 
to users. 
Also allows users to visually verify a clients signature against those in a document 
and to save the results of either matching or not present. 


Installation Notes: 

  - CCLA_ClientServices, ECS_BaseUtils is needed for this component to function correctly.
  - Run the SQL scripts in the CCLA_SignatureCheckingt/scripts folder after installation.



Change log
----------

V1.0.0  07/02/2011
------
-Initial release

V1.0.1 08/02/2011
------
-Changed the location that files uploaded through a jsp were being saved to
-added exception handling around checking out a file using JSP

V1.0.2 10/02/2011
------
-refactored two jsp pages to now be one.
-added error handling and visual upload indicators on pasting images

V1.0.3 10/02/2011
------
-removed jsp code that was throwing errors with smaller images being uploaded
-removed ability to update signature if content item is not released.

V1.0.4 14/02/2011
------
-Updated central database schema to include new PERSON_SIGNATURE table. See
 the Signature_checking_schema_070111.sql file.
-Added extra functionality on the accounts page to quickly paste signatures. 

V1.0.5 14/02/2011
------
-Changed applet init URL to <$HttpWebRoot$>
-Added help text on the person creation screens to promote signature capture

V1.0.6 17/02/2011
------
-Increased maximum image height to 500 pixels
-Writes the validation outcome to document metadata every time the verified signature
 list is updated on a document.
 -Requires new metadata field: SignaturesValid (type=int, default=0)
-Run SQL update script:
 -sql_updates_170211.sql
-After running the script, update the UCM table/view associated with the 
 DOCUMENT_CLASSES table, so the new columns are visible.

V1.0.7 25/02/2011
------
- UCI00128 Fixed IE crashing as a result of applet error.

V1.0.8 15/03/2011
-----------------
- Refactored code to match new Cache model

V1.0.9 30/03/2011
-----------------
- Refactored database tables:
  - Fixed ID column to use proper numeric type
  - Replaced DOCNAME columns with DOC_ID, to bring document references in line with
    other parts of the central database.
  - Run the script sql_updates_060411.sql to apply these changes.
- Changed signature checking panel to use IDOCScript include, instead of a full HTML
  template.
  
V1.0.10 26/05/2011
-----------------
- Minor Change - Slimmed down bundle page UI

V1.1.0 (01/07/2011)
-------------------
- Related persons displayed in the Signature Checking panel has been expanded to
  list persons with other relations to the account (not just 'Signatory') This is 
  controlled by the env. var CCLA_SC_SIGNATORY_RELATION_NAME_IDS.
  
V1.1.1 (08/08/2011)
-------------------
- Removed LWDocument(dID) constructors in favour of LWDocument(dID, true) to ensure
  direct database lookup is used, as opposed to GET_SEARCH_RESULTS. Many times more 
  efficient. Also replaced LWDocument(dDocName) with LWDocument(dDocName, true)

V1.1.2 (26/09/2011)
-------------------
- Run the following update script:
  - person_signature_updates_26092011.sql
- Prevented capture of signature images from static data pages, if the new 
  CCLA_SC_RequireSourceDocId flag is set. When set, signatures can only be captured
  in Iris.
- The assumed source document ID (dID) where a signature was captured from is now
  stored in the PERSON_SIGNATURE table as SOURCE_DOC_ID. This parameter is enforced
  if CCLA_SC_RequireSourceDocId flag is set.
- Prevented capture of signature images from non-transaction documents in Iris, if the
  new CCLA_SC_PreventTransactionCapture flag is set.  
- Added 'signatureCaptureAllowed' binder flag when the getIsSignatureCheckRequired()
  method is called. This flag is only added to the binder for non-transaction 
  documents.

  
V1.1.3 (13/03/2012)
---------------------------
- Updated signature checking to use DOC_GUID references instead of DOC_ID
- Run the following update script:
	person_signature_updates_07032012.sql
	[Added by TM 13/03] comm_signature_updates_13032012.sql
- Added try catch to jsp updates and add signature methods
- Updated signal capture panel to use either DOC_ID or DOC_GUID.

  
V1.1.4 (23/03/2012)
---------------------------
- Additional updates to refactor sig checking to use DOC_GUID references instead of DOC_ID
- Run the following update script:
	extra_person_signature_updates_16032012.sql
	
V1.1.5 (23/04/2012)
-----------	
- Switched DB connections to use pooled facade where applicable
- Added idcToken for 11g migration
	
	
V1.1.6 (27/04/2012)
-----------	

	
V1.1.7 (01/05/2012)
-----------	
- refactored uploading signatures to use a service instead of a jsp
- added <$js(UserName)$> to escape the \ in AD usernames


Version 1.1.8 (24/05/2012)
-- This component has been converted to use the new ECS build process. 
-- Any changes must be put in the CHANGELOG not this readme. 
-- Configuration/installation notes can still be added to the top of this file

************** END **************