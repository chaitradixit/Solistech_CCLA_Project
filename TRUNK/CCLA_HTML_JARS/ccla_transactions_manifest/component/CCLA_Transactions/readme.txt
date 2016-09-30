CCLA_Transactions Component Release v1.1.5
================================================================================
  Full Release Name: 2014_02_05 v1.1.5 (Build 20831)

      Date: 05/14/2013 04:16 PM
    Author: tm
  Location: http://ecs-svn/stellent/CLIENTS/CCLA/Components/CCLA_Transactions/tags/v1.1.5
           
(c) Extended Content Solutions Limited 2014
================================================================================
﻿CCLA_Transactions Component
===========================

Version 1.0.0
-------------
-Initial build. Run the script generate_instruction_transaction_tables.sql

Version 1.0.1
-------------
-Run the update script instruction_transaction_tables_updates.sql

Version 1.0.2 - 1.0.4
-------------
- ????

Version 1.0.5 (18/05/2011)
-------------
- changed fund code of PS to PC.
- Added daily expenses charges.
- Added total share net income distribution to summary report.

Version 1.0.6 (23/05/2011)
-------------
- Moved all share class/share class group queries into Core component 
  (have not renamed them)
  
Version 1.0.7 (??/??/2011)
-------------
- UCI00208 Added Search and Filter to Completed End Of Day Listing. 
  Requires 15June_eod_report_changes.sql to be executed.

Version 1.0.8 (10/01/2012)
-------------
- Fixed view end of day transactions to use new query. Also made all psdf transactions 
  to use pooling as oppose to the native ucm connection.
- Run the following script:
  - PSDF_Transfer_Upgrade_Scripts_12122011.sql
- Updated restart queries to use trunc functions so all the data for the psdf eod is deleted 
  as the the times might change.
- Added checks for unit values in psdf transfers (unit value cannot be null or zero). 

Version 1.0.9 (31/01/2012)
--------------------------
- Add re-run process to PSDF end of day (remove previous completed EOD)
- Ability to run PSDF for any date range with force processing flag.
- Fixed insert db error when number is in scientific string format, usually occurs if number 
  is tiny like 1.01E-7.
- Refactored sequence names
- Added paging controls to PSDF EOD Report listing

Version 1.1.0 (23/04/2012) 
--------------------------
-- added idcToken for 11g migration
-- decoupled database and facade for 11g migration


Version 1.1.0 (24/05/2012)
-- This component has been converted to use the new ECS build process. 
-- Any changes must be put in the CHANGELOG not this readme. 
-- Configuration/installation notes can still be added to the top of this file

Version 1.1.1 (29/05/2012)
-- Fix issue with net/fund/gross yield calculations for the EndOfDayService. 
-- Replaced BigDecimal.equals with BigDecimal.compareTo.  

Version 1.1.2 (12/06/2012)
-- Ipad style changes.
************** END **************