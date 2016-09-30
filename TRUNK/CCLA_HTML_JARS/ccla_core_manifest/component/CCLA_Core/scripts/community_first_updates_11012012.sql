-- New Campaign entry for Community First
REM INSERTING into CAMPAIGN
Insert into CAMPAIGN (CAMPAIGN_ID,CAMPAIGN_NAME,CAMPAIGN_DESCRIPTION,CAMPAIGN_OWNER,ENROLMENT_START_DATE,ENROLMENT_END_DATE,CAMPAIGN_START_DATE,CAMPAIGN_END_DATE,CAMPAIGN_STATUS_ID,PROCESS_HANDLER_CLASS,IDOC_INCLUDE_INFIX,DATE_ADDED,LAST_UPDATED,LAST_UPDATED_BY,DEFAULT_ENROLMENT_STATUS_ID) values (13,'Community First',null,'sysadmin',to_timestamp('11-JAN-12 11.41.22.211000000','DD-MON-RR HH24.MI.SS.FF'),to_timestamp('01-JAN-15 00.00.00.000000000','DD-MON-RR HH24.MI.SS.FF'),to_timestamp('11-JAN-12 11.41.38.876000000','DD-MON-RR HH24.MI.SS.FF'),to_timestamp('01-JAN-15 00.00.00.000000000','DD-MON-RR HH24.MI.SS.FF'),1,'com.ecs.stellent.ccla.clientservices.campaign.CommunityFirstClientEnrolmentHandler','comm_first',to_timestamp('11-JAN-12 11.46.37.217000000','DD-MON-RR HH24.MI.SS.FF'),to_timestamp('11-JAN-12 11.46.40.440000000','DD-MON-RR HH24.MI.SS.FF'),'sysadmin',1);

-- New Form Type
REM INSERTING into REF_FORM_TYPE
Insert into REF_FORM_TYPE (FORM_TYPE_ID,FORM_TYPE_NAME,GEN_INSTRUCTION_TYPE_ID,RET_INSTRUCTION_TYPE_ID,FORM_HANDLER_CLASS,FORM_TYPE_SHORTNAME,IS_SINGLETON,ELEMENT_TYPE_ID) values (17,'Community First Client Registration',8,8,'com.ecs.stellent.ccla.clientservices.form.CommunityFirstClientInfoFormHandler','CF-INFO',1,1);

-- Generate Forms action at status 'Enrolled'
INSERT INTO ENROLMENT_STATUS_ACTION_APPL VALUES (13, 1, 10);

-- New System Config Vars for controlling min section sizes on new form
REM INSERTING into SYSTEM_CONFIG_VAR
Insert into SYSTEM_CONFIG_VAR (CONFIG_VAR_NAME,CONFIG_VAR_DESCRIPTION,CONFIG_VAR_DATA_TYPE,STRING_VALUE,INT_VALUE,FLOAT_VALUE,DATE_VALUE,BOOL_VALUE,LAST_UPDATED,LAST_UPDATED_BY,CONFIG_VAR_TYPE) values ('CommunityFirst_MinAuthPersons','Minimum number of Director/Controller sections to display on the Community First Client Info form','INT',null,2,null,null,null,to_timestamp('11-JAN-12 13.01.41.949000000','DD-MON-RR HH24.MI.SS.FF'),'sysadmin','CAMPAIGN');
Insert into SYSTEM_CONFIG_VAR (CONFIG_VAR_NAME,CONFIG_VAR_DESCRIPTION,CONFIG_VAR_DATA_TYPE,STRING_VALUE,INT_VALUE,FLOAT_VALUE,DATE_VALUE,BOOL_VALUE,LAST_UPDATED,LAST_UPDATED_BY,CONFIG_VAR_TYPE) values ('CommunityFirst_MinSignatories','Minimum number of Other Signatories sections to display on the Community First Client Info form','INT',null,2,null,null,null,to_timestamp('11-JAN-12 13.02.37.512000000','DD-MON-RR HH24.MI.SS.FF'),'sysadmin','CAMPAIGN');

-- Add 'Bank Account' attribute to new campaign
INSERT INTO APPLICABLE_ENROLMENT_ATTR VALUES (5, 1, 13);
-- Add 'Min Auth Persons' attribute to new campaign
INSERT INTO APPLICABLE_ENROLMENT_ATTR VALUES (6, 3, 13);
-- Add 'Min Signatories' attribute to new campaign
INSERT INTO APPLICABLE_ENROLMENT_ATTR VALUES (7, 4, 13);

-- New Form Type
REM INSERTING into REF_FORM_TYPE
Insert into REF_FORM_TYPE (FORM_TYPE_ID,FORM_TYPE_NAME,GEN_INSTRUCTION_TYPE_ID,RET_INSTRUCTION_TYPE_ID,FORM_HANDLER_CLASS,FORM_TYPE_SHORTNAME,IS_SINGLETON,ELEMENT_TYPE_ID) values (18,'Email Indemnity (Community First)',57,57,'com.ecs.stellent.ccla.clientservices.form.EmailIndemnityFormHandler','EMAIL-INDEM-CF',1,1);
