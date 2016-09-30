
-- New CBF Client Confirmation Campaign 
INSERT INTO CAMPAIGN
(CAMPAIGN_ID, CAMPAIGN_NAME, CAMPAIGN_DESCRIPTION, 
CAMPAIGN_OWNER, ENROLMENT_START_DATE, ENROLMENT_END_DATE,
CAMPAIGN_START_DATE, CAMPAIGN_END_DATE, CAMPAIGN_STATUS_ID, 
PROCESS_HANDLER_CLASS, IDOC_INCLUDE_INFIX, DATE_ADDED, 
LAST_UPDATED, LAST_UPDATED_BY, DEFAULT_ENROLMENT_STATUS_ID) 
VALUES 
(14, 'CBF Client Confirmation', 'AML chaser exercise for CBF client base', 
'weblogic', sysdate, to_date('01-01-2015', 'dd-mm-yyyy'),
sysdate, to_date('01-01-2015', 'dd-mm-yyyy'), 1,
'com.ecs.stellent.ccla.clientservices.campaign.CBFClientConfirmationEnrolmentHandler', 'cbf_client', sysdate,
sysdate, 'weblogic', 1);


-- New Generic Actions
INSERT INTO REF_CAMPAIGN_ENROLMENT_ACTION (ENROLMENT_ACTION_ID, ENROLMENT_ACTION_NAME, ENROLMENT_ACTION_DESCRIPTION)
VALUES (27, 'Generate Information Form', 'Generate the information form for the enrolled client.');
INSERT INTO REF_CAMPAIGN_ENROLMENT_ACTION (ENROLMENT_ACTION_ID, ENROLMENT_ACTION_NAME, ENROLMENT_ACTION_DESCRIPTION)
VALUES (28, 'Generate Confirmation Form', 'Generate the confirmation form for the enrolled client.');

-- Add new actions for CBF Client Confirmation Campaign
INSERT INTO ENROLMENT_STATUS_ACTION_APPL (CAMPAIGN_ID, ENROLMENT_STATUS_ID, ENROLMENT_ACTION_ID)
VALUES (14, 1, 27);

INSERT INTO ENROLMENT_STATUS_ACTION_APPL (CAMPAIGN_ID, ENROLMENT_STATUS_ID, ENROLMENT_ACTION_ID)
VALUES (14, 1, 28);

-- New enrolment attribute value
INSERT INTO REF_ENROLMENT_ATTRIBUTE (ENROLMENT_ATTRIBUTE_ID, ENROLMENT_ATTRIBUTE_NAME) VALUES (5,'Account');

-- New applicable enrolment attr for CBF Client Confirmation Campaign
INSERT INTO APPLICABLE_ENROLMENT_ATTR (APPLENROLATTR_ID, ENROLMENT_ATTRIBUTE_ID, CAMPAIGN_ID) VALUES (8,5,14);


--New campaign activity type
INSERT INTO REF_CAMPAIGN_ACTIVITY_TYPE (CAMPAIGN_ACTIVITY_TYPE_ID, CAMPAIGN_ACTIVITY_NAME, CAMPAIGN_ACTIVITY_DESCRIPTION) VALUES (100017, 'ACCOUNT SELECTED', 'Accounts selected and saved');
INSERT INTO REF_CAMPAIGN_ACTIVITY_TYPE (CAMPAIGN_ACTIVITY_TYPE_ID, CAMPAIGN_ACTIVITY_NAME, CAMPAIGN_ACTIVITY_DESCRIPTION) VALUES (100018, 'ACCOUNT CLEARED', 'Accounts cleared');


-- New Form Types
SET DEFINE OFF;
Insert into REF_FORM_TYPE (FORM_TYPE_ID,FORM_TYPE_NAME,GEN_INSTRUCTION_TYPE_ID,RET_INSTRUCTION_TYPE_ID,FORM_HANDLER_CLASS,FORM_TYPE_SHORTNAME,IS_SINGLETON,ELEMENT_TYPE_ID) values (21,'CBF Client Confirmation',8,8,'com.ecs.stellent.ccla.clientservices.form.CBFClientInfoFormHandler','CBF-CLIENT-CONF',1,1);
Insert into REF_FORM_TYPE (FORM_TYPE_ID,FORM_TYPE_NAME,GEN_INSTRUCTION_TYPE_ID,RET_INSTRUCTION_TYPE_ID,FORM_HANDLER_CLASS,FORM_TYPE_SHORTNAME,IS_SINGLETON,ELEMENT_TYPE_ID) values (22,'CBF Client Information',8,8,'com.ecs.stellent.ccla.clientservices.form.CBFClientInfoFormHandler','CBF-CLIENT-INFO',1,1);

-- New Config Vars
SET DEFINE OFF;
Insert into SYSTEM_CONFIG_VAR (CONFIG_VAR_NAME,CONFIG_VAR_DESCRIPTION,CONFIG_VAR_DATA_TYPE,STRING_VALUE,INT_VALUE,FLOAT_VALUE,DATE_VALUE,BOOL_VALUE,LAST_UPDATED,LAST_UPDATED_BY,CONFIG_VAR_TYPE) values ('CBFClientInfo_MinAuthPersons','Minimum number of Director/Controller sections to display on the CBF Client Info form','INT',null,2,null,null,null,to_timestamp('29-AUG-12 14.24.41.575000000','DD-MON-RR HH24.MI.SS.FF'),'weblogic','CAMPAIGN');
Insert into SYSTEM_CONFIG_VAR (CONFIG_VAR_NAME,CONFIG_VAR_DESCRIPTION,CONFIG_VAR_DATA_TYPE,STRING_VALUE,INT_VALUE,FLOAT_VALUE,DATE_VALUE,BOOL_VALUE,LAST_UPDATED,LAST_UPDATED_BY,CONFIG_VAR_TYPE) values ('CBFClientInfo_MinSignatories','Minimum number of Other Signatories sections to display on the CBF Client Info form','INT',null,3,null,null,null,to_timestamp('29-AUG-12 14.25.22.693000000','DD-MON-RR HH24.MI.SS.FF'),'weblogic','CAMPAIGN');

Insert into SYSTEM_CONFIG_VAR (CONFIG_VAR_NAME,CONFIG_VAR_DESCRIPTION,CONFIG_VAR_DATA_TYPE,STRING_VALUE,INT_VALUE,FLOAT_VALUE,DATE_VALUE,BOOL_VALUE,LAST_UPDATED,LAST_UPDATED_BY,CONFIG_VAR_TYPE) values ('CBFClientInfo_MaxAuthPersons','Maximum number of Director/Controller sections to display on the CBF Client Info form','INT',null,2,null,null,null,to_timestamp('29-AUG-12 14.24.41.575000000','DD-MON-RR HH24.MI.SS.FF'),'weblogic','CAMPAIGN');
Insert into SYSTEM_CONFIG_VAR (CONFIG_VAR_NAME,CONFIG_VAR_DESCRIPTION,CONFIG_VAR_DATA_TYPE,STRING_VALUE,INT_VALUE,FLOAT_VALUE,DATE_VALUE,BOOL_VALUE,LAST_UPDATED,LAST_UPDATED_BY,CONFIG_VAR_TYPE) values ('CBFClientInfo_MaxSignatories','Maximum number of Other Signatories sections to display on the CBF Client Info form','INT',null,3,null,null,null,to_timestamp('29-AUG-12 14.25.22.693000000','DD-MON-RR HH24.MI.SS.FF'),'weblogic','CAMPAIGN');
Insert into SYSTEM_CONFIG_VAR (CONFIG_VAR_NAME,CONFIG_VAR_DESCRIPTION,CONFIG_VAR_DATA_TYPE,STRING_VALUE,INT_VALUE,FLOAT_VALUE,DATE_VALUE,BOOL_VALUE,LAST_UPDATED,LAST_UPDATED_BY,CONFIG_VAR_TYPE) values ('CBFClientInfo_PopulatePersonSections','Determines whether existing person data will be populated on the CBF Client Info form','BOOL',null,null,null,null,1,to_timestamp('29-AUG-12 14.24.41.575000000','DD-MON-RR HH24.MI.SS.FF'),'weblogic','CAMPAIGN');
Insert into SYSTEM_CONFIG_VAR (CONFIG_VAR_NAME,CONFIG_VAR_DESCRIPTION,CONFIG_VAR_DATA_TYPE,STRING_VALUE,INT_VALUE,FLOAT_VALUE,DATE_VALUE,BOOL_VALUE,LAST_UPDATED,LAST_UPDATED_BY,CONFIG_VAR_TYPE) values ('CBFClientInfo_FormDeadlineDate','Deadline date to print on the CBF Client Info form. If blank, a date is calculated 2 months ahead of the form creation date, floored down to the first date of that month.','DATE',null,null,null,null,null,to_timestamp('29-AUG-12 14.24.41.575000000','DD-MON-RR HH24.MI.SS.FF'),'weblogic','CAMPAIGN');
