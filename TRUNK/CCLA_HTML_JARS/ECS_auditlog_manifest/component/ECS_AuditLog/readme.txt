ECS_AuditLog Component (Version 1.3)
------------------------------------

This component allows you to store audit entries in a database table and to
retrieve them based on certain criteria.  To use this component you must
first have installed the ECS_BaseUtils component.

A description of the revised set of auditing fields can be found on the test
page (run the ECS_AUDIT_TEST service to view this page)

Version History
===============

Version 1.4
-----------
Fixed issue with qAuditEntriesByRef query - was configured as a Select Query instead of
Select Cache. This was causing problems when fetching audit entries - they had to be
fetched multiple times to yield the latest entries.

Version 1.3
-------------
Added new summary query [for last 10 entries by parent id]. Compatible with Oracle DB only.
Added support for audit of Logins (requires server to use form based login).
Added alpha support for audit of document views (viewing a thumbnail is tracked 
as a document view so of limited use at present).

Version 1.2
-----------
Added configuration tables to config manager for easier maintenance.

Version 1.1
-----------
Added environmental flags to control debugging and maximum allowed field size in audit table

Version 1
---------
- Added new services, queries and Java methods to support a new way of auditing, using filter onEndServiceRequestActions. 
- Added 3 mapping/configuration tables: tAudit_BinderMapConfig, tAudit_FixedMapConfig and tAudit_ServiceMapConfig that must be configured 
with services and metadata that must be passed in order to store auditing information. See  configuration guide.txt for information on 
how to configure auditing services. 

INSTALLATION (Version 1.3)
==========================
1. Install the CMU bundle in /config to create the audit log table
   (TAUDIT), audit configuration tables (TAUDITCONFIG) and 
   accompanying schema views.

   NOTE: If legacy audit support required install the CMU bundle
         legacyAuditTable to install the table tAuditLogs.

2. Install the auditlog_config archive in config/archive.  This will
   load the audit configuration data into the TAUDITCONFIG tables.

   NOTE: This audit configuration is for Hera Case Management only,
         see the document 'configuration guide.txt' for an
         explanation for adding audits to other UCM Services.

3. Install the component and restart the server.

4. The maximum allowed field size is set by default to 100.  This means
   that any field entries added to the audit table which exceed this
   size are truncated to 100 characters.  You can change this setting
   by modifying ECS_AuditLog_MAX_FIELD_SIZE.  You will need to restart
   the server for the change to take effect.

5. To test: first, you need to add some auditing configuration to 
   mapping/configuration tables (see configuration guide.txt). 
   After that you can test auditing on the action you configured to 
   be audited in the system by running the service for that action. 
   For each action, a new row in the audit table should appear. 
   
6. Check there are no errors and the audit entry was successfully
   inserted into the TAUDIT table.


Build 5
-------
- Update on old tAuditLog custom table to use upper case column names and prefix AL, i.e.:
		Service to ALSERVICE,
		Reference to ALREFERENCE, 
		UserName to ALUSERNAME,
		Message to ALMESSAGE,
		LogDate to ALLOGDATE,
		Type to ALTYPE,
		DocTitle to ALDOCTITLE,
		CaseStatus to ALCASESTATUS.
- Update on tAuditLogs custom table to use upper case column names.
- Column names in database query qLogGetEntries have been changed to upper case and references to columns in idoc have been 
  replaced to upper case in file ECS_AuditLog_LogDisplay.htm.
- If uploading this component to a system running MSSQL database, renaming all columns in tAuditLog (HERA only) as above 
	is necessary for audit logging to work properly. Also, and renaming tAuditLogs column names to use upper case if this component is 
	used in any other application. 

Build 3
-------
-Added new set of services, queries and Java methods to support a more generalized
 method of auditing.
-Added startup filter which automatically generates the log table, tAuditLogs, if
 it does not yet exist in the database.
-Older auditing methods still present for legacy applications (HERA)

Build 4
-------
-Attempt to fix inconsistency between HERA audit logging and current logging system. 

INSTALLATION (Build 5)
================================
- If you are instaling this component on a system running MSSQL server database, change all column names to use 
	upper case on tAuditLog (HERA only) and tAuditLogs table.
  
- Install the component and restart the server. The component should detect the absence of the audit log table 
  and automatically create it (tAuditLogs)

- To test: open '<server cgi path>?IdcService=ECS_AUDIT_TEST' in a web browser and attempt to add an audit 
  entry using the test form.
   
- Check there are no errors and the audit entry was successfully inserted into the tAuditLogs table.

INSTALLATION (Build 3 and later)
================================

1. Install the component and restart the server. The component
   should detect the absence of the audit log table and automatically
   create it (tAuditLogs)

2. To test: open '<server cgi path>?IdcService=ECS_AUDIT_TEST' in a
   web browser and attempt to add an audit entry using the test form.
   
3. Check there are no errors and the audit entry was successfully
   inserted into the tAuditLogs table.



LEGACY INSTALLATION (Build 2 and before)
========================================

1. Create a database table called tAuditLog with the following columns

	Service		varchar 50
	Reference	varchar	20
	UserName	varchar	50
	Message		varchar	255
	LogDate		date
	
	If you intend to use extended audit logging then add these additional
	fields:
	
	Type		varchar 50
	DocTitle	varchar 1024
	CaseStatus	varchar 50

2. Install the ECS_AuditLog component

USAGE

To log a new audit entry use the followig methods

1. Call the service LOG_ADDENTRY with the parameters

	lService 	- the name of the action e.g. MailHandling
	lRef 		- the item reference i.e. dDocName
	lUser		- the username
	lMessage	- the log entry description
	lDate		- the log date

	To set extended parameters call the service LOG_ADDENTRY_EXT with the above
	parameters plus:
	
	lType 		- a type value
	dDocTitle   - the dDocTitle if this log entry is connected to a content id
	lCaseStatus - a status value
	
2. To call from a Service use the sub service LOG_ADDENTRY_SUB or
   LOG_ADDENTRY_EXT_SUB if passing extended parameters

3. From Java use the class com.ecs.stellent.auditlog.AuditLogHelper as follows;

	AuditLogHelper auditLogHelper = AuditLogHelper.getInstance();
	auditLogHelper.addLog(<serviceName>,<reference>,<username>,<message>)
	
	(or for extended parameters)
	auditLogHelper.addLog(<serviceName>,<reference>,<username>,<message>,<type>,<docTitle>,<caseStatus>)
	
4. There's no idoc script function yet

To retrieve the log entries call the service with the parameters

	lService
	lRef

