ECS_BaseUtils Component Release V2.1.0
================================================================================
  Full Release Name: 2014_04_06 V2.1.0 (Build 18790)

      Date: 06/28/2012 05:17 PM
    Author: tm
  Location: http://ecs-svn/stellent/COMPONENTS/ECS_BaseUtils/tags/V2.1.0
           
(c) Extended Content Solutions Limited 2014
================================================================================

******IMPORTANT UPGRADE INSTRUCTIONS******
If you are upgrading from Version 1.0 Build 17 of ECS_BaseUtils or earlier and
are using the Upload Applet then you must change the paths to the embed.js and 
file_uploader.jsp files.  

Change all references to common/embed.js to common/ECS_BaseUtils/embed.js and
all references to jsp/file_uploader.jsp to jsp/ECS_BaseUtils/file_uploader.jsp.

Since Version 1.1 (Build 20), the applet size has changed from 200x60 to 250x60
pixels. You may need to adjust any older versions of the custom dnd_applet 
include to reflect the new size.

If you prefer to use the older upload style (pop-up window displays upload
progress, instead of the applet itself), you must remove the following lines
from embed.js and other references in the ECSBaseUtils resource file:

	<param name="monitor.type" value="compact">
	<param name="monitor.embed" value="yes">

****************************************??**

The ECS_BaseUtils component provides a set of Java utilities (com.ecs.utils)
which can be used by other custom Java components running in the Oracle Stellent
Content Server.

To install this component:

1. Install ECS_BaseUtils through the Component Wizard/Configuration Manager

2. ECS logging is configured by the ECS_LOG_CONFIG_LOCATION parameter in the
   component configuration file (ecs_baseutils_environment.cfg).  By default
   this is set to ../custom/ECS_BaseUtils/ECSLogConfig.  This path (taken from
   the Server bin directory) points to the log4j config file.  If using 11g then
   the it is taken from  "<Weblogic domain DIR>/ucm/cs/" so you will probably want
   to use "custom/ECS_BaseUtils/ECSLogConfig". If the component
   has not been installed in the standard component directory (custom) then
   this configuration setting must be modified before logging will work.
   
3. (10g specific) If logging fails it may be due to core server processes invoking the log4j
   framework before BaseUtils.  If this is the case you should ensure that the
   core server log4j configuration file contains the ECSLogConfig entries.
   Note: You can specify the ECS_LOC_CONFIG_LOCATION by adding it to the
   java VM parameters setting JAVA_SERVICE_EXTRA_OPTIONS in intradoc.cfg:
   
   -DECS_LOG_CONFIG_LOCATION=$SHAREDDIR/../custom/ECS_BaseUtils/ECSLogConfig
   
   [on 10gR3 JAVA_SERVICE_EXTRA_OPTIONS may be quoted ("). If this field
    already contains an entry you will need to add extra quotes so that these
    parameters are correctly split on the command line, for example:
    
   JAVA_SERVICE_EXTRA_OPTIONS=-Xrs" "-DECS_LOG_CONFIG_LOCATION=$SHAREDDIR/../custom/ECS_BaseUtils/ECSLogConfig
    
   ]
   
4. (10g specific) On Stellent 7.X and Oracle UCM 10gR3 systems configure IdcserverBean support 
   by adding the following to the END of the CLASSPATH (or on 10gR3
   JAVA_CLASSPATH_defaultjdbc) parameter in the Server bin/intradoc.cfg file:
   
   $SHAREDDIR/../custom/ECS_BaseUtils/lib/IdcServerBean.jar;
   
   If you are installing on a Linux system, you will need to user : instead of ; 
   to separate jar files, and you may also have to add $COMPUTEDCLASSPATH at the
   beginning of the variable. 
   E.g. 
   JAVA_CLASSPATH_defaultjdbc=$COMPUTEDCLASSPATH:$SHAREDDIR/classes/ojdbc14.jar:$SHAREDDIR/../custom/ECS_BaseUtils/lib/IdcServerBean.jar

   NOTE: IdcServerBean is not supported on Oracle UCM 11g.  When using UCM 11g
   RIDC must be used.

5. Alter the paths of each of the log files in ECSLogConfig. The paths will be 
   relative to the java property 'user.dir' which by default is the ucm weblogic
   domain e.g. <Middleware DIR>/user_projects/domains/ucm_domain/. You may also
   specify absolute paths.
   
6. To configure RIDC (experimental) support set the configuration parameter
   USE_RIDC_LIBRARY=1.  RIDC is compatible with Oracle UCM 10gR3 and 11g 
   systems.
   
7. If using the Upload Applet refer to the section below on configuring the
   upload applet.
    
8. Restart the Content Server

Resources Installed by ECS_BaseUtils
====================================
custom/ECS_BaseUtils
weblayout/common/ECS_BaseUtils
weblayout/jsp/common/ECS_BaseUtils

Other Java  Libraries Installed by this component
=================================================

When you install the ECS_BaseUtils component the following packages are also
installed:

/lib/log4j-1.2.14.jar
/lib/IdcServerBean.jar
/lib/IdcServerBeanX.jar (IdcServerBean jar minus the core server libraries)
/lib/oracle.ucm.ridc-11.1.1.jar

In addition the ECS_BaseUtils depends on but does not install (they are included
as part of the Stellent core build) the following packages:

xalan.jar
xercesImpl.jar

As of Version 1.3, some Castor XML library references have been used in the new
cache class and other utility functions. This requires the following jar:

castor-1.1-xml.jar

Currently, this jar is not included on the component class path because it may
conflict with other versions of Castor currently in use by other apps (e.g HERA)
however you will need it on your Eclipse build path.

Using Logging
=============
ECS_BaseUtils provides log4j logging.  The log config file ECSLogConfig is in
the component directory.  The config file is monitored so logging can be
changed without restarting the Content Server.

To use the logging in your code refer to the javadoc, briefly the main details
are:

Logging Class: com.ecs.utils.Log

Important (static) Methods:
.debug(string)             - log a debug message string
.info(string)              - log an info message string
.warn(string)              - log a warning message string
.error(string)             - log an error message string
.debug(string, throwable)  - log a debug message string and print stack trace
etc..

To log a username add the environment variable ECS_LOG_OUTPUT_USERNAME.

Using the Upload Applet
=======================
ECS_BaseUtils includes an upload applet which can be called via the UPLOAD_APPLET_TEST.

To configure the applet and test page:

1. Enable JSP pages in the server configuration (Set IsJspServerEnabled=1 in config.cfg)

2. Edit the embed.js script in weblayout\common so that the value attribute on line 4 
   has the correct RelativeWebRoot set.  By default this will be set to /idc/.

3. Edit the ECS_BaseUtils configuration file (ecs_baseutils_environment.cfg) and ensure
   the UPLOAD_DOCTYPE attribute is set to a Document type that exists.  The test upload
   will upload content with this type so this entry must be correct before the uploader
   will work. [NB: The test uploader requires that the content server is configured to
   automatically generate content IDs, without it uploads will fail.  To set automatic
   ID generation set IsAutoNumber=true & AutoNumberPrefix= in config.cfg)

3. Restart the Content Server

Refer to the ECS_BaseUtils config file and accompanying JavaDoc and JSP for more
details on implementing the applet.

Troubleshooting
===============
If the server fails to start after enabling the component start the server from the
command line (Server install dir bin/IdcServer) and monitor the console output.
Alternatively, set UseRedirectedOutput=true in the intradoc.cfg file, restart the
service and monitor the IdcServerNT.log file (or Unix equivalent).

Extended Content Solutions Ltd 2009

Changelog
=========

version 2.0.11 (12/06/2012)
- (TM) Improved resilience of OracleJDBCFacade connections. Capable of recovering gracefully if the DB connection is terminated

version 2.0.10
-------------
-(CL) Fixed potential issue with new check in for 11g ridc libraries. dCreateDate must be empty for new check-in.


Version 2.0.9
-------------
- Fixed component readme versioning to match SVN.
- (CL) Fix issue with 11g checkin as this requires a physical file to exist on the file 
  system "bundle.txt"
- (CL) Fix issue with incorrect format used to parse date data for LWDocument when RIDC
  libraries are enabled
- (TM) Fixed issue with reading RIDC-formatted dates from LWDocument data fields when
  RIDC libraries are enabled
- (TM) Added better error reporting to OracleJDBCFacade calls

Version 1.6.5 (04/23/12)
------------------------
-(CL) Added ability to override the DateFormatter via the config files and flag to control 
 the auto-commit for the oracleJDBCFacade when getting new connections. See cfg for more info.

Version 1.6.4 (28/11/11)
------------------
- (TM) Switched off auto-commit flag on new Connection instances. This fixes the issue
  with transaction blocks not working correctly - before, every single execute line 
  was automatically committed regardless of the current transaction state.

Version 1.6.3  (27/04/11)
-------------------------
- (GS) Added experimental support for RIDC & UCM 11g.

Version 1.6.3  (27/04/11)
-------------------------
-(TM) Added new useTransaction switch to LWDocument instance. Defaults to true. If
 set to false, the update method will use the new service UPDATE_DOCINFO_NOTRANS, 
 instead of UPDATE_DOCINFO. This won't execute in its own transaction block and allows
 document updates to be included as part of a larger transaction block.
 -NOTE: The no-transaction functionality does not yet extend to the executeForcedUpdate
  and checkin methods.
-(TM) Added new isInTransaction() method to FWFacade, returns true if the facade is
 inside an active transaction block.

Version 1.6.21 (30/11/10)
-------------------------
-(TM) Revised the new ResultSetUtils/BinderUtils classes:
 -ResultSetUtils is now called DataResultSetUtils to avoid amibiguity with
  intradoc.data.ResultSetUtils class
 -Removed references to newer core method resultset.getStringValueByName(),
  as this is only available on 10g UCM server builds.
 -Both classes now contain methods that operate purely on String values for
  completeness.

Version 1.6.2  (25/11/10)
-------------------------
-(TM) Added new methods to StringUtils:
 -padString
 -trimLeadingChars
-(TM) Added new utility classes:
 -ResultSetUtils
 -BinderUtils
 These are used to support database-centric components and used for casting
 the base String values in Binders/ResultSets to more useful types.

Version 1.6.15 (20/07/10)
-------------------------
Added GET_NODE_STATUS service to return content server status - to be used by load
balancer to determine whether node is in the cluster.  If Status=ACTIVE then OK, if
Status anything else then no.  To return status in short form (rather than webpage)
add shortForm=1 to service call.

Version 1.5.14 (22/06/10)
-------------------------
-(TM) Updated RadInks DND Plus applet jarfile to version 4.12. Prevents invalid
 certificate error being displayed when the applet loads.

r6809          	(27/11/09)
-------------------------
added FormValidation Class that will validate certain inputs agaisnt a regular 
expression, using for form validation. 

r6482			(15/10/09)
-------------------------
-KW00109 Added code to prevent more then one thread using database provider at a time. 

Version 1.5.13 (08/09/09)
-------------------------
-Bug fix to DocGenerator to ensure temp files are cleaned up.

Version 1.5.12 (04/09/09)
-------------------------
-Added service to output all services for a UCM instance for audit purposes.
 

Version 1.5.11 (01/09/09)
-------------------------
-Changed the LWDocument instantiation method to use database lookup, 
 this prevents errors when checking in 2 generated items with the same 
 dDocName in quick succession.

Version 1.5.10 (14/08/09)
-------------------------
-Bug fix to DocGenerator

Version 1.5.9 (07/08/09)
------------------------
-Query bug fix plus extension to DocGenerator

Version 1.5.8 (04/08/09)
------------------------
-Added wrapper for UCM UserData class

Version 1.5.7 (29/07/09)
------------------------
-Added Sequence support for workspace wrapper class

Version 1.5.6 (14/07/09)
------------------------
-Extention to workspace wrapper class

Version 1.5.5 (12/07/09)
------------------------
-Added support for in process calls with a workspace wrapper class

Version 1.5.4 (10/06/09)
------------------------
-Changed load-order of component cfg file to allow overriding

Version 1.5.3 (09/05/09)
------------------------
-Added support for calling command line executables and fuzzy date logic
-Support for custom Idoc Script functions:
 formatDateRelative(date1)
 log4j(string1)

Version 1.5.2 (06/04/09)
------------------------
-Fixed bug in ECS_VIEW_CONTENT service relating to revision labels

Version 1.5.1 (30/03/09)
------------------------
-Added new methods to LWDocument to allow a user name to be specified more easily
-Added extension to logging so that the logged in user is now output.
-Added new file to bytes handler

Version 1.5.0 (05/02/09)
------------------------
-Added new methods on LWDocument, LWFacade classes to handle alternate file check-ins.
-You can now check-in a LWDocument with a null primary file, this will perform a 
 metadata-only check-in.
-Added MD5 encryption utility

Version 1.4.5 (26/11/08)
------------------------
-Added new filter class, hooked into the start of every service request.
 Adds the server version number to the binder, and a flag to indicate whether
 the server is Oracle UCM.

Version 1.4.4 (14/11/08)
----------------------
-Added two new services, made for viewing content items in a browser window
 if you only have the dID/dDocName.
-ECS_VIEW_CONTENT will load an initial page to resolve the content URL, befor
 auto-refreshing to show the URL.
-ECS_VIEW_CONTENT_FRAMED will load a frameset, showing a summary of metadata
 in the top-level frame and the content in a frame below it.
-Minor change in DocGenerator, now passes back dID along with other fields.

Version 1.4.3a (10/10/08)
-------------------------
-Added a template (CHECK_OUT_AND_OPEN_POPUP) for performing Check Out and Open  
 via a service call.

Version 1.4.3 (24/09/08)
------------------------
-Modified LWDocument metadata calls to use matches keyword
 to ensure 7.5/10g support across Verity/DB searches.
 
Version 1.4.2 (12/09/08)
------------------------
-Improved UPDATE_LATEST function, now checks whether
 the item is locked before performing update.

Version 1.4.1 (10/09/08)
------------------------
-Added Castor libraries v0.95,v1.1 to /lib dir
-Castor v0.95 added to classpath

Version 1.4 (10/09/08)
----------------------
-Added DOC_INFO_LATEST service
-Added executeForcedUpdate() method to LWFacade

Version 1.3 (01/09/08)
----------------------
-Added dynamic XML cache
-Added Castor XML utility class
-Added executeServiceRaw method (DJ)

Version 1.2 (07/08/08)
----------------------
-Updated GenerateDocument functions to make them easier to integrate with

Version 1.1 (04/07/08)
----------------------
-Updated Drag and Drop resources to latest version from Radinks
-Changed applet size and configuration