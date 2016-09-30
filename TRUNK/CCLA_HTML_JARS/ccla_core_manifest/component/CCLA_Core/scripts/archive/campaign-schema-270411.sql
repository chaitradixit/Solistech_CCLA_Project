-- Full creation script for new Campaign schema.
-- Replaces existing tables/sequences in the UCMADMIN tablespace.

-- New sequences
-- -------------

-- Campaign Enrolment ID: will replace UCMADMIN Process ID sequence, hence starting at 50000 so there's no crossover.
-- Run as SYS!
CREATE SEQUENCE CCLA.SEQ_CAMPAIGN_ENROLMENT_ID MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 50000 CACHE 20 NOORDER NOCYCLE;
CREATE OR REPLACE PUBLIC SYNONYM SEQ_CAMPAIGN_ENROLMENT_ID FOR CCLA.SEQ_CAMPAIGN_ENROLMENT_ID;
GRANT ALL ON SEQ_CAMPAIGN_ENROLMENT_ID TO UCMADMIN;

-- Form ID: the old UCMADMIN Forms table didn't have its own sequence, using the Workflow ID sequence instead.
-- The new Form table will have its own ID but it can't clash with the old ones, hence starting at 250000
-- Run as SYS!
CREATE SEQUENCE CCLA.SEQ_FORM_ID MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 250000 CACHE 20 NOORDER NOCYCLE;
CREATE OR REPLACE PUBLIC SYNONYM SEQ_FORM_ID FOR CCLA.SEQ_FORM_ID;
GRANT ALL ON SEQ_FORM_ID TO UCMADMIN;

-- Campaign Activity ID: the old UCMADMIN Activity table had its own sequence. Activities were shared between Enrolments/Processes and
-- Interactions. The new sequence will be specific to Campaign activities but it can't clash with the old ones, hence starting at 10000
CREATE SEQUENCE CCLA.SEQ_CAMPAIGN_ACTIVITY_ID MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 100000 CACHE 20 NOORDER NOCYCLE;
CREATE OR REPLACE PUBLIC SYNONYM SEQ_CAMPAIGN_ACTIVITY_ID FOR CCLA.SEQ_CAMPAIGN_ACTIVITY_ID;
GRANT ALL ON SEQ_CAMPAIGN_ACTIVITY_ID TO UCMADMIN;

-- Enrolment Action Queue ID: based off the old PROCESS_ACTION_QUEUE table.
CREATE SEQUENCE CCLA.SEQ_ENROLMENT_ACTION_QUEUE_ID MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER NOCYCLE;
CREATE OR REPLACE PUBLIC SYNONYM SEQ_ENROLMENT_ACTION_QUEUE_ID FOR CCLA.SEQ_ENROLMENT_ACTION_QUEUE_ID;
GRANT ALL ON SEQ_ENROLMENT_ACTION_QUEUE_ID TO UCMADMIN;

-- Investment Intention ID: based off the old CCLA_ACCOUNT_INTENTION table.
-- Already created!
--CREATE SEQUENCE CCLA.SEQ_INVESTMENT_INTENTION_ID MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER NOCYCLE;
--CREATE OR REPLACE PUBLIC SYNONYM SEQ_INVESTMENT_INTENTION_ID FOR CCLA.SEQ_INVESTMENT_INTENTION_ID;
--GRANT ALL ON SEQ_INVESTMENT_INTENTION_ID TO UCMADMIN;

-- Grant select/references privileges to UCMADMIN.USERS table, so we can
-- reference it in CCLA DB user tables.
GRANT SELECT, REFERENCES ON UCMADMIN.USERS TO CCLA;

-- Below is the DDL script generated by Toad, pointing at the Campaigns Workspace.
-- Run as CCLA

/*
Created: 01/11/2010
Modified: 28/04/2011
Project: CCLA 
Model: Central Database
Company: Kainos Software
Author: Donna Hull
Version: 6.8
Database: Oracle 11g Release 1
*/

-- Create tables section -------------------------------------------------

-- Table REF_CAMPAIGN_STATUS

CREATE TABLE REF_CAMPAIGN_STATUS(
  CAMPAIGN_STATUS_ID Number(15,0) NOT NULL,
  CAMPAIGN_STATUS_NAME Varchar2(60 ) NOT NULL,
  STATUS_DESCRIPTION Varchar2(100 )
)
/

-- Add keys for table REF_CAMPAIGN_STATUS

ALTER TABLE REF_CAMPAIGN_STATUS ADD CONSTRAINT CAMPAIGN_STATUS_PK PRIMARY KEY (CAMPAIGN_STATUS_ID)
/

-- Table CAMPAIGN

CREATE TABLE CAMPAIGN(
  CAMPAIGN_ID Number(15,0) NOT NULL,
  CAMPAIGN_NAME Varchar2(60 ) CONSTRAINT CAMP_NAME_NN NOT NULL,
  CAMPAIGN_DESCRIPTION Varchar2(200 ),
  CAMPAIGN_OWNER Varchar2(50 ) CONSTRAINT CAMP_OWNER_NN NOT NULL,
  ENROLMENT_START_DATE Timestamp(6) CONSTRAINT CAMP_ENROLSTARTDATE_NN NOT NULL,
  ENROLMENT_END_DATE Timestamp(6) CONSTRAINT CAMP_ENROLENDDATE_NN NOT NULL,
  CAMPAIGN_START_DATE Timestamp(6) CONSTRAINT CAMP_STARTDATE_NN NOT NULL,
  CAMPAIGN_END_DATE Timestamp(6) CONSTRAINT CAMP_ENDDATE_NN NOT NULL,
  CAMPAIGN_STATUS_ID Number(15,0) CONSTRAINT CAMP_STATUS_NN NOT NULL,
  PROCESS_HANDLER_CLASS Varchar2(150 ) CONSTRAINT CAMP_HANDLERCLASS_NN NOT NULL,
  IDOC_INCLUDE_INFIX Varchar2(150 ),
  DATE_ADDED Timestamp(6) CONSTRAINT CAMP_DATEADDED_NN NOT NULL,
  LAST_UPDATED Timestamp(6) CONSTRAINT CAMP_LASTUPDATED_NN NOT NULL,
  LAST_UPDATED_BY Varchar2(50 ),
  DEFAULT_ENROLMENT_STATUS_ID Number(15,0) CONSTRAINT CAMP_ENROLSTATUS_NN NOT NULL
)
/

-- Add keys for table CAMPAIGN

ALTER TABLE CAMPAIGN ADD CONSTRAINT CAMPAIGN_PK PRIMARY KEY (CAMPAIGN_ID)
/

-- Table and Columns comments section
  
COMMENT ON COLUMN CAMPAIGN.CAMPAIGN_OWNER IS 'The owner is the only user that should be allowed to alter the campaign details (e.g. campaign start/end date, campaign status, etc)'
/
COMMENT ON COLUMN CAMPAIGN.ENROLMENT_START_DATE IS 'The date that candidates can start being enrolled to the campaign'
/
COMMENT ON COLUMN CAMPAIGN.ENROLMENT_END_DATE IS 'The date that candidates stop being enrolled to the campaign'
/
COMMENT ON COLUMN CAMPAIGN.CAMPAIGN_START_DATE IS 'Date that the Campaign is due to run from'
/
COMMENT ON COLUMN CAMPAIGN.CAMPAIGN_END_DATE IS 'Date that the Campaign is due to end'
/
COMMENT ON COLUMN CAMPAIGN.PROCESS_HANDLER_CLASS IS 'This is the java class specific to the campaign. This is included for legacy reasons and may not be used in future'
/
COMMENT ON COLUMN CAMPAIGN.IDOC_INCLUDE_INFIX IS 'Idoc include for the campaign, controls portions of the user interface, eg only display certain client details, etc. Included for legacy reasons and may not be used in future.'
/
COMMENT ON COLUMN CAMPAIGN.DEFAULT_ENROLMENT_STATUS_ID IS 'Default status assigned to new enrolments.

May be overriden by the Process Handler class.'
/

-- Table CAMPAIGN_ENROLMENT

CREATE TABLE CAMPAIGN_ENROLMENT(
  CAMPAIGN_ENROLMENT_ID Number(15,0) CONSTRAINT CAMPENROL_ID_NN NOT NULL,
  CAMPAIGN_ID Number(15,0) CONSTRAINT CAMPENROL_CAMPAIGN_NN NOT NULL,
  PERSON_ID Number(15,0),
  ORGANISATION_ID Number(15,0),
  ENROLMENT_STATUS_ID Number(15,0) CONSTRAINT CAMPENROL_STATUS_NN NOT NULL,
  CONTACT_ID Number(15,0),
  DATE_ADDED Timestamp(6) CONSTRAINT CAMPENROL_DATE_NN NOT NULL,
  LAST_UPDATED Timestamp(6) CONSTRAINT CAMPENROL_UPDATED_NN NOT NULL,
  LAST_UPDATED_BY Varchar2(50 ) CONSTRAINT CAMPENROL_UPDATEDBY_NN NOT NULL,
  CONSTRAINT CAMPAIGN_ENROLMENT_CK CHECK (NOT (PERSON_ID IS NULL AND ORGANISATION_ID IS NULL))
)
/

-- Create indexes for table CAMPAIGN_ENROLMENT

CREATE INDEX CAMPAIGN_ENROLMENT_UK ON CAMPAIGN_ENROLMENT (CAMPAIGN_ID,PERSON_ID,ORGANISATION_ID)
/

-- Add keys for table CAMPAIGN_ENROLMENT

ALTER TABLE CAMPAIGN_ENROLMENT ADD CONSTRAINT CAMPAIGN_ENROLMENT_PK PRIMARY KEY (CAMPAIGN_ENROLMENT_ID)
/

-- Table and Columns comments section
  
COMMENT ON TABLE CAMPAIGN_ENROLMENT IS 'All the clients or persons that are enrolled into the campaign.'
/
COMMENT ON COLUMN CAMPAIGN_ENROLMENT.CONTACT_ID IS 'Contact details to be used if the person or organisation has requested a specific contact point is to be used for this campaign'
/

-- Table REF_CAMPAIGN_ENROLMENT_STATUS

CREATE TABLE REF_CAMPAIGN_ENROLMENT_STATUS(
  ENROLMENT_STATUS_ID Number(15,0) CONSTRAINT ENROLSTATUS_ID_NN NOT NULL,
  ENROLMENT_STATUS_NAME Varchar2(60 ) CONSTRAINT ENROLSTATUSNAME_NN NOT NULL,
  STATUS_DESCRIPTION Varchar2(200 )
)
/

-- Add keys for table REF_CAMPAIGN_ENROLMENT_STATUS

ALTER TABLE REF_CAMPAIGN_ENROLMENT_STATUS ADD CONSTRAINT CAMPENROLSTATUS_PK PRIMARY KEY (ENROLMENT_STATUS_ID)
/

-- Table and Columns comments section
  
COMMENT ON TABLE REF_CAMPAIGN_ENROLMENT_STATUS IS 'e.g. enrolled, contacted, forms returned, completed, on hold,  etc. This is also the place to EXCLUDE clients from the campaign, with an Exclude status'
/

-- Table REF_CAMPAIGN_ACTIVITY_TYPE

CREATE TABLE REF_CAMPAIGN_ACTIVITY_TYPE(
  CAMPAIGN_ACTIVITY_TYPE_ID Number(15,0) NOT NULL,
  CAMPAIGN_ACTIVITY_NAME Varchar2(50 ) CONSTRAINT CAMPACTTYPE_NAME_NN NOT NULL,
  CAMPAIGN_ACTIVITY_DESCRIPTION Varchar2(200 )
)
/

-- Add keys for table REF_CAMPAIGN_ACTIVITY_TYPE

ALTER TABLE REF_CAMPAIGN_ACTIVITY_TYPE ADD CONSTRAINT CAMPAIGN_ACTIVITY_TYPE_PK PRIMARY KEY (CAMPAIGN_ACTIVITY_TYPE_ID)
/

-- Table and Columns comments section
  
COMMENT ON TABLE REF_CAMPAIGN_ACTIVITY_TYPE IS 'e.g. enrolment, status updated, form created, form returned, etc'
/

-- Table CAMPAIGN_ACTIVITY

CREATE TABLE CAMPAIGN_ACTIVITY(
  CAMPAIGN_ACTIVITY_ID Number(15,0) CONSTRAINT CAMPACT_ID_NN NOT NULL,
  CAMPAIGN_ENROLMENT_ID Number(15,0) CONSTRAINT CAMPACT_ENROL_NN NOT NULL,
  PERSON_ID Number(15,0),
  CAMPAIGN_ACTIVITY_TYPE_ID Number(15,0) CONSTRAINT CAMPACT_TYPE_NN NOT NULL,
  ACTIVITY_DESCRIPTION Varchar2(250 ),
  DATE_ADDED Timestamp(6) CONSTRAINT CAMPACT_DATE_NN NOT NULL,
  LAST_UPDATED_BY Varchar2(50 ) CONSTRAINT CAMPACT_LASTUPDATED_NN NOT NULL,
  NOTE_ID Number(15,0)
)
/

-- Add keys for table CAMPAIGN_ACTIVITY

ALTER TABLE CAMPAIGN_ACTIVITY ADD CONSTRAINT CAMPACT_PK PRIMARY KEY (CAMPAIGN_ACTIVITY_ID)
/

-- Table and Columns comments section
  
COMMENT ON COLUMN CAMPAIGN_ACTIVITY.PERSON_ID IS 'If the person related to the activity is different to the person from the enrolment'
/
COMMENT ON COLUMN CAMPAIGN_ACTIVITY.ACTIVITY_DESCRIPTION IS 'Free-text entry which clarifies the outcome of the activity.'
/


-- Table NOTE

CREATE TABLE NOTE(
  NOTE_ID Number(15,0) CONSTRAINT NOTE_ID_NN NOT NULL,
  USER_ID Varchar2(50 ) CONSTRAINT NOTE_USER_NN NOT NULL,
  DATE_ADDED Timestamp(6) CONSTRAINT NOTES_DATEADDED_NN NOT NULL,
  LAST_UPDATED Timestamp(6),
  LAST_UPDATED_BY Varchar2(50 ),
  NOTE_CONTENT Varchar2(500 ) CONSTRAINT NOTE_CONTENT_NN NOT NULL
)
/

-- Add keys for table NOTE

ALTER TABLE NOTE ADD CONSTRAINT NOTE_PK PRIMARY KEY (NOTE_ID)
/

-- Table and Columns comments section
  
COMMENT ON TABLE NOTE IS 'Used to tag short, 500-character notes and messages to other elements in the DB which feature a NOTE_ID foreign key.

The note can be updated at a later date, this is why the LAST_UPDATED and LAST_UPDATED_BY are present.'
/

-- Table FORM

CREATE TABLE FORM(
  FORM_ID Number(15,0) CONSTRAINT FORM_ID_NN NOT NULL,
  FORM_TYPE_ID Number(15,0) CONSTRAINT FORM_FORMTYPE_NN NOT NULL,
  FORM_STATUS_ID Number(15,0) CONSTRAINT FORM_STATUS_NN NOT NULL,
  CAMPAIGN_ENROLMENT_ID Number(15,0),
  CAMPAIGN_ACTIVITY_ID Number(15,0),
  USER_ID Varchar2(50 ),
  PERSON_ID Number(15,0),
  GEN_DOC_ID Number(38,0),
  RET_DOC_ID Number(38,0),
  DATE_ADDED Timestamp(6) CONSTRAINT FORM_DATEADDED_NN NOT NULL,
  DATE_GENERATED Timestamp(6),
  DATE_UPLOADED Timestamp(6),
  DATE_PRINTED Timestamp(6),
  DATE_RETURNED Timestamp(6),
  LAST_UPDATED Timestamp(6) CONSTRAINT FORM_LASTUPDATED_NN NOT NULL
)
/

-- Add keys for table FORM

ALTER TABLE FORM ADD CONSTRAINT FORM_PK PRIMARY KEY (FORM_ID)
/

-- Table and Columns comments section
  
COMMENT ON TABLE FORM IS 'List of generated Forms. The FORM_ID primary key will be printed on the generated form documents in barcode format.

'
/
COMMENT ON COLUMN FORM.CAMPAIGN_ACTIVITY_ID IS 'Reference to the enrolment activity that generated the form'
/
COMMENT ON COLUMN FORM.USER_ID IS 'User who created the form originally'
/
COMMENT ON COLUMN FORM.PERSON_ID IS 'Who the form was addressed to'
/
COMMENT ON COLUMN FORM.GEN_DOC_ID IS 'UCM Document reference to the generated form document'
/
COMMENT ON COLUMN FORM.RET_DOC_ID IS 'UCM Document reference to the returned form document'
/
COMMENT ON COLUMN FORM.DATE_GENERATED IS 'Date that the PDF form rendition was generated.'
/
COMMENT ON COLUMN FORM.DATE_UPLOADED IS 'Date that the generated PDF form was uploaded to UCM'
/
COMMENT ON COLUMN FORM.DATE_PRINTED IS 'Date the form was last printed out'
/
COMMENT ON COLUMN FORM.DATE_RETURNED IS 'Date the form was returned'
/

-- Table REF_FORM_TYPE

CREATE TABLE REF_FORM_TYPE(
  FORM_TYPE_ID Number(15,0) CONSTRAINT FORM_TYPE_ID_NN NOT NULL,
  FORM_TYPE_NAME Varchar2(50 ) NOT NULL,
  GEN_INSTRUCTION_TYPE_ID Number(15,0) CONSTRAINT FORMTYPE_GENTYPE_NN NOT NULL,
  RET_INSTRUCTION_TYPE_ID Number(15,0) CONSTRAINT FORMTYPE_RETTYPE_NN NOT NULL,
  FORM_HANDLER_CLASS Varchar2(150 )
)
/

-- Add keys for table REF_FORM_TYPE

ALTER TABLE REF_FORM_TYPE ADD CONSTRAINT FORM_TYPE_PK PRIMARY KEY (FORM_TYPE_ID)
/

-- Table and Columns comments section
  
COMMENT ON COLUMN REF_FORM_TYPE.GEN_INSTRUCTION_TYPE_ID IS 'Default Instruction Type for generated form documents.

Can be overriden by the Handler Class.'
/
COMMENT ON COLUMN REF_FORM_TYPE.RET_INSTRUCTION_TYPE_ID IS 'Default Instruction Type for returned form documents.

Can be overriden by the Handler Class.'
/
COMMENT ON COLUMN REF_FORM_TYPE.FORM_HANDLER_CLASS IS 'Java class implementing the com.ecs.stellent.ccla.clientservices.form.FormHandler interface.

Used to handle special actions when form documents are uploaded to UCM'
/

-- Table REF_FORM_STATUS

CREATE TABLE REF_FORM_STATUS(
  FORM_STATUS_ID Number(15,0) NOT NULL,
  FORM_STATUS_NAME Varchar2(50 ) CONSTRAINT FORMSTATUS_NAME_NN NOT NULL,
  FORM_STATUS_DESCRIPTION Varchar2(200 )
)
/

-- Add keys for table REF_FORM_STATUS

ALTER TABLE REF_FORM_STATUS ADD CONSTRAINT FORM_STATUS_PK PRIMARY KEY (FORM_STATUS_ID)
/

-- Table and Columns comments section
  
COMMENT ON TABLE REF_FORM_STATUS IS 'e.g. Generated, Uploaded, Printed, Returned'
/

-- Table FORM_ELEMENT_APPLIED

CREATE TABLE FORM_ELEMENT_APPLIED(
  FORM_ID Number(15,0) NOT NULL,
  ELEMENT_ID Number(15,0) NOT NULL
)
/

-- Add keys for table FORM_ELEMENT_APPLIED

ALTER TABLE FORM_ELEMENT_APPLIED ADD CONSTRAINT FORMELEMAPPL_PK PRIMARY KEY (FORM_ID,ELEMENT_ID)
/

-- Table and Columns comments section
  
COMMENT ON TABLE FORM_ELEMENT_APPLIED IS 'Mapping between Forms and Elements (e.g. Accounts, Organisations)'
/

-- Table ENROLMENT_ACTION_QUEUE

CREATE TABLE ENROLMENT_ACTION_QUEUE(
  ENROLMENT_ACTION_QUEUE_ID Number(15,0) NOT NULL,
  CAMPAIGN_ENROLMENT_ID Number(15,0),
  DATE_ADDED Timestamp(6) CONSTRAINT ENROLACTQ_DATEADDED_NN NOT NULL,
  DATE_EXECUTED Timestamp(6),
  IS_COMPLETED Number(1,0) CONSTRAINT ENROLACTQ_COMPLETED_NN NOT NULL
        CONSTRAINT ENROLACTQ_COMPLETED_CK CHECK (IS_COMPLETED IN (0,1)),
  ERROR_MESSAGE Varchar2(200 ),
  ENROLMENT_ACTION_ID Number(15,0) CONSTRAINT ENROLACTQ_ACTION_NN NOT NULL
)
/

-- Add keys for table ENROLMENT_ACTION_QUEUE

ALTER TABLE ENROLMENT_ACTION_QUEUE ADD CONSTRAINT ENROLACTIONQUEUE_PK PRIMARY KEY (ENROLMENT_ACTION_QUEUE_ID)
/

-- Table and Columns comments section
  
COMMENT ON TABLE ENROLMENT_ACTION_QUEUE IS 'Used to queue large batches of enrolment actions, e.g. generating/printing forms.'
/
COMMENT ON COLUMN ENROLMENT_ACTION_QUEUE.DATE_EXECUTED IS 'Date the queued action was executed.

Null values here indicate a pending action.'
/
COMMENT ON COLUMN ENROLMENT_ACTION_QUEUE.IS_COMPLETED IS 'Determines whether the action executed successfully (0 = pending/error, 1 = success)

Non-null DATE_EXECUTED, IS_COMPLETED=1: action was successful
Non-null DATE_EXECUTED, IS_COMPLETED=0: action failed'
/
COMMENT ON COLUMN ENROLMENT_ACTION_QUEUE.ERROR_MESSAGE IS 'Captures the error message produced from UCM if the action execution failed'
/

-- Table REF_CAMPAIGN_ENROLMENT_ACTION

CREATE TABLE REF_CAMPAIGN_ENROLMENT_ACTION(
  ENROLMENT_ACTION_ID Number(15,0) NOT NULL,
  ENROLMENT_ACTION_NAME Varchar2(50 ) CONSTRAINT ENROLACT_NAME_NN NOT NULL,
  ENROLMENT_ACTION_DESCRIPTION Varchar2(200 )
)
/

-- Add keys for table REF_CAMPAIGN_ENROLMENT_ACTION

ALTER TABLE REF_CAMPAIGN_ENROLMENT_ACTION ADD CONSTRAINT ENROLMENT_ACTION_PK PRIMARY KEY (ENROLMENT_ACTION_ID)
/

ALTER TABLE REF_CAMPAIGN_ENROLMENT_ACTION ADD CONSTRAINT ENROLACT_NAME_UQ UNIQUE (ENROLMENT_ACTION_NAME)
/

-- Table and Columns comments section
  
COMMENT ON TABLE REF_CAMPAIGN_ENROLMENT_ACTION IS 'List of actions that can be performed on CAMPAIGN_ENROLMENT instances, e.g. "Generate Forms", "Exclude", "Put on hold", "Mark as contacted" '
/

-- Table ENROLMENT_STATUS_ACTION_APPL

CREATE TABLE ENROLMENT_STATUS_ACTION_APPL(
  CAMPAIGN_ID Number(15,0) CONSTRAINT ENROLSTATUSACT_CAMP_NN NOT NULL,
  ENROLMENT_STATUS_ID Number(15,0) CONSTRAINT ENROLSTATUSACT_STATUS_NN NOT NULL,
  ENROLMENT_ACTION_ID Number(15,0) CONSTRAINT ENROLSTATUSACT_ACTION_NN NOT NULL
)
/

-- Add keys for table ENROLMENT_STATUS_ACTION_APPL

ALTER TABLE ENROLMENT_STATUS_ACTION_APPL ADD CONSTRAINT ENROLSTATUSACT_PK PRIMARY KEY (ENROLMENT_ACTION_ID,CAMPAIGN_ID,ENROLMENT_STATUS_ID)
/

-- Table and Columns comments section
  
COMMENT ON TABLE ENROLMENT_STATUS_ACTION_APPL IS 'Stores the available actions for each status for a campaign.

Each row indicates a permitted action for a given status of the given campaign.'
/

/* 

-- Table FUND_INVESTMENT_INTENTION

(Added by previous FUND_INVESTMENT_INTENTION script)


CREATE TABLE FUND_INVESTMENT_INTENTION(
  INVESTMENT_INTENTION_ID Number(15,0) CONSTRAINT INVESTINT_ID_NN NOT NULL,
  FUND_CODE Varchar2(2 CHAR) CONSTRAINT INVESTINT_FUNDCODE_NN NOT NULL,
  ORGANISATION_ID Number(15,0) NOT NULL,
  PERSON_ID Number(15,0),
  CAMPAIGN_ID Number(15,0),
  CASH Number(20,6),
 UNITS Number(20,6),
  DATE_ADDED Timestamp(6) CONSTRAINT INVESTINT_DATEADDED_NN NOT NULL,
  LAST_UPDATED Timestamp(6) CONSTRAINT INVESTINT_LASTUPDATEDBY_NN NOT NULL,
  LAST_UPDATED_BY Varchar2(50 )
)
/

-- Add keys for table FUND_INVESTMENT_INTENTION

ALTER TABLE FUND_INVESTMENT_INTENTION ADD CONSTRAINT INVESTMENT_INTENTION_PK PRIMARY KEY (INVESTMENT_INTENTION_ID)
/

-- Table and Columns comments section
  
COMMENT ON TABLE FUND_INVESTMENT_INTENTION IS 'Stores the investment intentions for a given Organisation/Fund.'
/
COMMENT ON COLUMN FUND_INVESTMENT_INTENTION.ORGANISATION_ID IS 'The Organisation that conveyed the intention'
/
COMMENT ON COLUMN FUND_INVESTMENT_INTENTION.PERSON_ID IS 'The person who conveyed or last amended the intention - should be related to the given Organisation'
/

*/

-- Create relationships section ------------------------------------------------- 

CREATE INDEX CAMPSTATUS_CAMPAIGN_IX ON CAMPAIGN (CAMPAIGN_STATUS_ID) 
/
ALTER TABLE CAMPAIGN ADD CONSTRAINT CAMPSTATUS_CAMPAIGN_FK FOREIGN KEY (CAMPAIGN_STATUS_ID) REFERENCES REF_CAMPAIGN_STATUS (CAMPAIGN_STATUS_ID)
/

CREATE INDEX CAMPAIGN_CAMPENROL_IX ON CAMPAIGN_ENROLMENT (CAMPAIGN_ID) 
/
ALTER TABLE CAMPAIGN_ENROLMENT ADD CONSTRAINT CAMPAIGN_CAMPENROL_FK FOREIGN KEY (CAMPAIGN_ID) REFERENCES CAMPAIGN (CAMPAIGN_ID)
/

CREATE INDEX PERSON_CAMPENROL_IX ON CAMPAIGN_ENROLMENT (PERSON_ID) 
/
ALTER TABLE CAMPAIGN_ENROLMENT ADD CONSTRAINT PERSON_CAMPENROL_FK FOREIGN KEY (PERSON_ID) REFERENCES PERSON (PERSON_ID)
/

CREATE INDEX ORG_CAMPENROL_IX ON CAMPAIGN_ENROLMENT (ORGANISATION_ID) 
/
ALTER TABLE CAMPAIGN_ENROLMENT ADD CONSTRAINT ORG_CAMPENROL_FK FOREIGN KEY (ORGANISATION_ID) REFERENCES ORGANISATION (ORGANISATION_ID)
/

CREATE INDEX CAMPENROLSTATUS_CAMPENROL_IX ON CAMPAIGN_ENROLMENT (ENROLMENT_STATUS_ID) 
/
ALTER TABLE CAMPAIGN_ENROLMENT ADD CONSTRAINT CAMPENROLSTATUS_CAMPENROL_FK FOREIGN KEY (ENROLMENT_STATUS_ID) REFERENCES REF_CAMPAIGN_ENROLMENT_STATUS (ENROLMENT_STATUS_ID)
/

CREATE INDEX USERS_CAMPAIGNOWNER_IX ON CAMPAIGN (CAMPAIGN_OWNER) 
/
ALTER TABLE CAMPAIGN ADD CONSTRAINT USERS_CAMPAIGNOWNER_FK FOREIGN KEY (CAMPAIGN_OWNER) REFERENCES UCMADMIN.USERS (DNAME)
/

CREATE INDEX CONTACTPOINT_CAMPENROL_IX ON CAMPAIGN_ENROLMENT (CONTACT_ID) 
/
ALTER TABLE CAMPAIGN_ENROLMENT ADD CONSTRAINT CONTACTPOINT_CAMPENROL_FK FOREIGN KEY (CONTACT_ID) REFERENCES CONTACT_POINT (CONTACT_ID)
/

CREATE INDEX CAMPENROL_CAMPACT_IX ON CAMPAIGN_ACTIVITY (CAMPAIGN_ENROLMENT_ID) 
/
ALTER TABLE CAMPAIGN_ACTIVITY ADD CONSTRAINT CAMPENROL_CAMPACT_FK FOREIGN KEY (CAMPAIGN_ENROLMENT_ID) REFERENCES CAMPAIGN_ENROLMENT (CAMPAIGN_ENROLMENT_ID)
/

CREATE INDEX PERSON_CAMPACT_IX ON CAMPAIGN_ACTIVITY (PERSON_ID) 
/
ALTER TABLE CAMPAIGN_ACTIVITY ADD CONSTRAINT PERSON_CAMPACT_FK FOREIGN KEY (PERSON_ID) REFERENCES PERSON (PERSON_ID)
/

CREATE INDEX CAMPACTTYPE_CAMPACT_IX ON CAMPAIGN_ACTIVITY (CAMPAIGN_ACTIVITY_TYPE_ID) 
/
ALTER TABLE CAMPAIGN_ACTIVITY ADD CONSTRAINT CAMPACTTYPE_CAMPACT_FK FOREIGN KEY (CAMPAIGN_ACTIVITY_TYPE_ID) REFERENCES REF_CAMPAIGN_ACTIVITY_TYPE (CAMPAIGN_ACTIVITY_TYPE_ID)
/

CREATE INDEX USERS_CAMPACTIVITY_IX ON CAMPAIGN_ACTIVITY (LAST_UPDATED_BY) 
/
ALTER TABLE CAMPAIGN_ACTIVITY ADD CONSTRAINT USERS_CAMPACTIVITY_FK FOREIGN KEY (LAST_UPDATED_BY) REFERENCES UCMADMIN.USERS (DNAME)
/

CREATE INDEX USERS_CAMPAIGNUPDATED_IX ON CAMPAIGN (LAST_UPDATED_BY) 
/
ALTER TABLE CAMPAIGN ADD CONSTRAINT USERS_CAMPAIGNUPDATED_FK FOREIGN KEY (LAST_UPDATED_BY) REFERENCES UCMADMIN.USERS (DNAME)
/

CREATE INDEX USERS_CAMPENROL_IX ON CAMPAIGN_ENROLMENT (LAST_UPDATED_BY) 
/
ALTER TABLE CAMPAIGN_ENROLMENT ADD CONSTRAINT USERS_CAMPENROL_FK FOREIGN KEY (LAST_UPDATED_BY) REFERENCES UCMADMIN.USERS (DNAME)
/

CREATE INDEX NOTES_CAMPACT_IX ON CAMPAIGN_ACTIVITY (NOTE_ID) 
/
ALTER TABLE CAMPAIGN_ACTIVITY ADD CONSTRAINT NOTES_CAMPACT_FK FOREIGN KEY (NOTE_ID) REFERENCES NOTE (NOTE_ID)
/

CREATE INDEX IX_UCM_USER_TABLE_NOTES_FK ON NOTE (USER_ID) 
/
ALTER TABLE NOTE ADD CONSTRAINT USER_NOTES_ADDEDBY_FK FOREIGN KEY (USER_ID) REFERENCES UCMADMIN.USERS (DNAME)
/

CREATE INDEX IX_UCM_USER_TABLE_NOTES_FK ON NOTE (LAST_UPDATED_BY) 
/
ALTER TABLE NOTE ADD CONSTRAINT USER_NOTES_UPDATEDBY_FK FOREIGN KEY (LAST_UPDATED_BY) REFERENCES UCMADMIN.USERS (DNAME)
/

CREATE INDEX FORM_TYPE_FORM_IX ON FORM (FORM_TYPE_ID) 
/
ALTER TABLE FORM ADD CONSTRAINT FORM_TYPE_FORM_FK FOREIGN KEY (FORM_TYPE_ID) REFERENCES REF_FORM_TYPE (FORM_TYPE_ID)
/

CREATE INDEX ENROLMENT_FORM_IX ON FORM (CAMPAIGN_ENROLMENT_ID) 
/
ALTER TABLE FORM ADD CONSTRAINT ENROLMENT_FORM_FK FOREIGN KEY (CAMPAIGN_ENROLMENT_ID) REFERENCES CAMPAIGN_ENROLMENT (CAMPAIGN_ENROLMENT_ID)
/

CREATE INDEX INSTRTYPE_FORM_GEN_IX ON REF_FORM_TYPE (GEN_INSTRUCTION_TYPE_ID) 
/
ALTER TABLE REF_FORM_TYPE ADD CONSTRAINT INSTRTYPE_FORM_GEN_FK FOREIGN KEY (GEN_INSTRUCTION_TYPE_ID) REFERENCES REF_INSTRUCTION_TYPE (INSTRUCTION_TYPE_ID)
/

CREATE INDEX INSTRTYPE_FORM_RET_IX ON REF_FORM_TYPE (RET_INSTRUCTION_TYPE_ID) 
/
ALTER TABLE REF_FORM_TYPE ADD CONSTRAINT INSTRTYPE_FORM_RET_FK FOREIGN KEY (RET_INSTRUCTION_TYPE_ID) REFERENCES REF_INSTRUCTION_TYPE (INSTRUCTION_TYPE_ID)
/

CREATE INDEX REVISIONS_FORM_GEN_IX ON FORM (GEN_DOC_ID) 
/
ALTER TABLE FORM ADD CONSTRAINT REVISIONS_FORM_GEN_FK FOREIGN KEY (GEN_DOC_ID) REFERENCES UCMADMIN.REVISIONS (DID)
/

CREATE INDEX REVISIONS_FORM_RET_FK ON FORM (RET_DOC_ID) 
/
ALTER TABLE FORM ADD CONSTRAINT REVISIONS_FORM_RET_FK FOREIGN KEY (RET_DOC_ID) REFERENCES UCMADMIN.REVISIONS (DID)
/

CREATE INDEX PERSON_FORM_IX ON FORM (PERSON_ID) 
/
ALTER TABLE FORM ADD CONSTRAINT PERSON_FORM_FK FOREIGN KEY (PERSON_ID) REFERENCES PERSON (PERSON_ID)
/

CREATE INDEX FORMSTATUS_FORM_IX ON FORM (FORM_STATUS_ID) 
/
ALTER TABLE FORM ADD CONSTRAINT FORMSTATUS_FORM_FK FOREIGN KEY (FORM_STATUS_ID) REFERENCES REF_FORM_STATUS (FORM_STATUS_ID)
/

CREATE INDEX FORM_FORMELEMAPPL_IX ON FORM_ELEMENT_APPLIED (FORM_ID) 
/
ALTER TABLE FORM_ELEMENT_APPLIED ADD CONSTRAINT FORM_FORMELEMAPPL_FK FOREIGN KEY (FORM_ID) REFERENCES FORM (FORM_ID)
/

CREATE INDEX ELEMENT_FORMELEMAPPL_IX ON FORM_ELEMENT_APPLIED (ELEMENT_ID) 
/
ALTER TABLE FORM_ELEMENT_APPLIED ADD CONSTRAINT ELEMENT_FORMELEMAPPL_FK FOREIGN KEY (ELEMENT_ID) REFERENCES ELEMENT (ELEMENT_ID)
/

CREATE INDEX CAMPENROL_ENROLACTIONQUEUE_IX ON ENROLMENT_ACTION_QUEUE (CAMPAIGN_ENROLMENT_ID) 
/
ALTER TABLE ENROLMENT_ACTION_QUEUE ADD CONSTRAINT CAMPENROL_ENROLACTIONQUEUE_FK FOREIGN KEY (CAMPAIGN_ENROLMENT_ID) REFERENCES CAMPAIGN_ENROLMENT (CAMPAIGN_ENROLMENT_ID)
/

CREATE INDEX ENROLACT_ENROLACTQ_IX ON ENROLMENT_ACTION_QUEUE (ENROLMENT_ACTION_ID) 
/
ALTER TABLE ENROLMENT_ACTION_QUEUE ADD CONSTRAINT ENROLACT_ENROLACTQ_FK FOREIGN KEY (ENROLMENT_ACTION_ID) REFERENCES REF_CAMPAIGN_ENROLMENT_ACTION (ENROLMENT_ACTION_ID)
/

CREATE INDEX CAMPAIGN_ENROLSTATUSACT_IX ON ENROLMENT_STATUS_ACTION_APPL (CAMPAIGN_ID) 
/
ALTER TABLE ENROLMENT_STATUS_ACTION_APPL ADD CONSTRAINT CAMPAIGN_ENROLSTATUSACT_FK FOREIGN KEY (CAMPAIGN_ID) REFERENCES CAMPAIGN (CAMPAIGN_ID)
/

CREATE INDEX ENROLSTATUS_ENROLSTATUSACT_IX ON ENROLMENT_STATUS_ACTION_APPL (ENROLMENT_STATUS_ID) 
/
ALTER TABLE ENROLMENT_STATUS_ACTION_APPL ADD CONSTRAINT ENROLSTATUS_ENROLSTATUSACT_FK FOREIGN KEY (ENROLMENT_STATUS_ID) REFERENCES REF_CAMPAIGN_ENROLMENT_STATUS (ENROLMENT_STATUS_ID)
/

CREATE INDEX ENROLACT_ENROLSTATUSACT_IX ON ENROLMENT_STATUS_ACTION_APPL (ENROLMENT_ACTION_ID) 
/
ALTER TABLE ENROLMENT_STATUS_ACTION_APPL ADD CONSTRAINT ENROLACT_ENROLSTATUSACT_FK FOREIGN KEY (ENROLMENT_ACTION_ID) REFERENCES REF_CAMPAIGN_ENROLMENT_ACTION (ENROLMENT_ACTION_ID)
/

CREATE INDEX ENROLSTATUS_CAMP_IX ON CAMPAIGN (DEFAULT_ENROLMENT_STATUS_ID) 
/
ALTER TABLE CAMPAIGN ADD CONSTRAINT ENROLSTATUS_CAMP_FK FOREIGN KEY (DEFAULT_ENROLMENT_STATUS_ID) REFERENCES REF_CAMPAIGN_ENROLMENT_STATUS (ENROLMENT_STATUS_ID)
/

CREATE INDEX CAMPACT_FORM_IX ON FORM (CAMPAIGN_ACTIVITY_ID) 
/
ALTER TABLE FORM ADD CONSTRAINT CAMPACT_FORM_FK FOREIGN KEY (CAMPAIGN_ACTIVITY_ID) REFERENCES CAMPAIGN_ACTIVITY (CAMPAIGN_ACTIVITY_ID)
/

CREATE INDEX IX_USERS_FORM_FK ON FORM (USER_ID) 
/
ALTER TABLE FORM ADD CONSTRAINT USERS_FORM_FK FOREIGN KEY (USER_ID) REFERENCES UCMADMIN.USERS (DNAME)
/

CREATE INDEX CAMP_INVESTINTENT_IX ON FUND_INVESTMENT_INTENTION (CAMPAIGN_ID) 
/
ALTER TABLE FUND_INVESTMENT_INTENTION ADD CONSTRAINT CAMP_INVESTINTENT_FK FOREIGN KEY (CAMPAIGN_ID) REFERENCES CAMPAIGN (CAMPAIGN_ID)
/


-- Alterations to existing tables
-- ------------------------------
-- Table REF_INVESTMENT_INTENT_STATUS

CREATE TABLE REF_INVESTMENT_INTENT_STATUS(
  INVINTENTSTATUS_ID Number(15,0) CONSTRAINT INVINTENTSTATUS_ID_NN NOT NULL,
  INVINTENTSTATUS_NAME Varchar2(50 ) CONSTRAINT INVINTENTSTATUS_NAME_NN NOT NULL,
  INVINTENTSTATUS_DESCRIPTION Varchar2(200 )
)
/

-- Add keys for table REF_INVESTMENT_INTENT_STATUS

ALTER TABLE REF_INVESTMENT_INTENT_STATUS ADD CONSTRAINT INVINTENTSTATUS_PK PRIMARY KEY (INVINTENTSTATUS_ID)
/

ALTER TABLE REF_INVESTMENT_INTENT_STATUS ADD CONSTRAINT INVINTENTSTATUS_NAME_UQ UNIQUE (INVINTENTSTATUS_NAME)
/

-- Table and Columns comments section
  
COMMENT ON TABLE REF_INVESTMENT_INTENT_STATUS IS 'Status of an Investment Intention. Generally indicates the client/persons confidence factor over the investment amount, e.g. 100%, 50%, Undecided.'
/

-- Table REF_CAMPAIGN_SUBJECT_STATUS

CREATE TABLE REF_CAMPAIGN_SUBJECT_STATUS(
  CAMPSUBJECTSTATUS_ID Number(15,0) CONSTRAINT CAMPSUBJECTSTATUS_ID_NN NOT NULL,
  CAMPSUBJECTSTATUS_NAME Varchar2(60 ) CONSTRAINT CAMPSUBJECTSTATUS_NAME_NN NOT NULL,
  CAMPSUBJECTSTATUS_DESCRIPTION Varchar2(200 )
)
/

-- Add keys for table REF_CAMPAIGN_SUBJECT_STATUS

ALTER TABLE REF_CAMPAIGN_SUBJECT_STATUS ADD CONSTRAINT CAMPSUBJECTSTATUS_PK PRIMARY KEY (CAMPSUBJECTSTATUS_ID)
/

-- Table and Columns comments section
  
COMMENT ON TABLE REF_CAMPAIGN_SUBJECT_STATUS IS 'The status of the enrolled Client/Person with respect to their interest in the campaign e.g. Interested, Undecided, Not interested'
/


-- New column on Fund Investment Intention

ALTER TABLE FUND_INVESTMENT_INTENTION ADD(
  INVINTENTSTATUS_ID Number(15,0)
);

REM INSERTING into REF_INVESTMENT_INTENT_STATUS
Insert into REF_INVESTMENT_INTENT_STATUS (INVINTENTSTATUS_ID,INVINTENTSTATUS_NAME,INVINTENTSTATUS_DESCRIPTION) values (1,'Unconfirmed',null);
Insert into REF_INVESTMENT_INTENT_STATUS (INVINTENTSTATUS_ID,INVINTENTSTATUS_NAME,INVINTENTSTATUS_DESCRIPTION) values (2,'Undecided',null);
Insert into REF_INVESTMENT_INTENT_STATUS (INVINTENTSTATUS_ID,INVINTENTSTATUS_NAME,INVINTENTSTATUS_DESCRIPTION) values (4,'25%',null);
Insert into REF_INVESTMENT_INTENT_STATUS (INVINTENTSTATUS_ID,INVINTENTSTATUS_NAME,INVINTENTSTATUS_DESCRIPTION) values (5,'50%',null);
Insert into REF_INVESTMENT_INTENT_STATUS (INVINTENTSTATUS_ID,INVINTENTSTATUS_NAME,INVINTENTSTATUS_DESCRIPTION) values (6,'75%',null);
Insert into REF_INVESTMENT_INTENT_STATUS (INVINTENTSTATUS_ID,INVINTENTSTATUS_NAME,INVINTENTSTATUS_DESCRIPTION) values (7,'100%',null);
Insert into REF_INVESTMENT_INTENT_STATUS (INVINTENTSTATUS_ID,INVINTENTSTATUS_NAME,INVINTENTSTATUS_DESCRIPTION) values (8,'Confirmed','Investment has been made');

-- Set all investments to default status
UPDATE FUND_INVESTMENT_INTENTION SET INVINTENTSTATUS_ID=1;

-- Add NN constraint
ALTER TABLE FUND_INVESTMENT_INTENTION MODIFY(
  INVINTENTSTATUS_ID Number(15,0) CONSTRAINT INVINTENT_STATUS_ID_NN NOT NULL
);

CREATE INDEX INVINTENTSTATUS_INVINTENT_IX ON FUND_INVESTMENT_INTENTION (INVINTENTSTATUS_ID) 
/
ALTER TABLE FUND_INVESTMENT_INTENTION ADD CONSTRAINT INVINTENTSTATUS_INVINTENT_FK FOREIGN KEY (INVINTENTSTATUS_ID) REFERENCES REF_INVESTMENT_INTENT_STATUS (INVINTENTSTATUS_ID)
/


ALTER TABLE CAMPAIGN_ENROLMENT ADD (
	CAMPSUBJECTSTATUS_ID Number(15,0)
);

REM INSERTING into REF_CAMPAIGN_SUBJECT_STATUS
Insert into REF_CAMPAIGN_SUBJECT_STATUS (CAMPSUBJECTSTATUS_ID,CAMPSUBJECTSTATUS_NAME,CAMPSUBJECTSTATUS_DESCRIPTION) values (1,'Undecided','Client/Person is unsure');
Insert into REF_CAMPAIGN_SUBJECT_STATUS (CAMPSUBJECTSTATUS_ID,CAMPSUBJECTSTATUS_NAME,CAMPSUBJECTSTATUS_DESCRIPTION) values (2,'Interested','Client/Person has confirmed their interest in the campaign');
Insert into REF_CAMPAIGN_SUBJECT_STATUS (CAMPSUBJECTSTATUS_ID,CAMPSUBJECTSTATUS_NAME,CAMPSUBJECTSTATUS_DESCRIPTION) values (3,'Not interested','Client/Person has confirmed they are not interested in the campaign');
Insert into REF_CAMPAIGN_SUBJECT_STATUS (CAMPSUBJECTSTATUS_ID,CAMPSUBJECTSTATUS_NAME,CAMPSUBJECTSTATUS_DESCRIPTION) values (4,'Excluded','Client/Person is excluded from campaign');

-- Set all enrolments to default status
UPDATE CAMPAIGN_ENROLMENT SET CAMPSUBJECTSTATUS_ID=1;

ALTER TABLE CAMPAIGN_ENROLMENT MODIFY (
	CAMPSUBJECTSTATUS_ID Number(15,0) CONSTRAINT CAMPENROL_SUBJSTATUSID_NN NOT NULL
);

CREATE INDEX CAMPSUBJSTATUS_CAMPENROL_IX ON CAMPAIGN_ENROLMENT (CAMPSUBJECTSTATUS_ID) 
/
ALTER TABLE CAMPAIGN_ENROLMENT ADD CONSTRAINT CAMPSUBJSTATUS_CAMPENROL_FK FOREIGN KEY (CAMPSUBJECTSTATUS_ID) REFERENCES REF_CAMPAIGN_SUBJECT_STATUS (CAMPSUBJECTSTATUS_ID)
/

CREATE OR REPLACE FORCE VIEW "CCLA"."ENROLMENT_ACTIVITY_LAST" ("PROCESS_ID", "CAMPAIGN_ACTIVITY_ID", "PERSON_ID", "ORGANISATION_ID", "USER_ID", "ACTIVITY_DATE", "ACTIVITY_TYPE", "ACTIVITY_ACTION", "CLIENT_STATUS", "PROCESS_STATUS", "PROCESS_DATE", "LAST_ACTION", "PROCESS_ENTITY_ID", "CAMPAIGN_ID", "CAMPAIGN_NAME", "ORGANISATION_NAME")
AS
  (SELECT CE.CAMPAIGN_ENROLMENT_ID AS PROCESS_ID,
    CA.CAMPAIGN_ACTIVITY_ID,
    CE.PERSON_ID,
    CE.ORGANISATION_ID,
    CA.LAST_UPDATED_BY          AS USER_ID,
    CA.DATE_ADDED               AS ACTIVITY_DATE,
    RCAT.CAMPAIGN_ACTIVITY_NAME AS ACTIVITY_TYPE,
    CA.ACTIVITY_DESCRIPTION     AS ACTIVITY_ACTION,
    RCSS.CAMPSUBJECTSTATUS_NAME AS CLIENT_STATUS,
    RCE.ENROLMENT_STATUS_NAME   AS PROCESS_STATUS,
    CE.DATE_ADDED               AS PROCESS_DATE,
    CE.LAST_UPDATED             AS LAST_ACTION,
    CE.ORGANISATION_ID          AS PROCESS_ENTITY_ID,
    CE.CAMPAIGN_ID,
    C.CAMPAIGN_NAME,
	org.ORGANISATION_NAME
  FROM CAMPAIGN_ENROLMENT CE
  INNER JOIN REF_CAMPAIGN_ENROLMENT_STATUS RCE
  ON (CE.ENROLMENT_STATUS_ID = RCE.ENROLMENT_STATUS_ID)
  INNER JOIN REF_CAMPAIGN_SUBJECT_STATUS RCSS
  ON (CE.CAMPSUBJECTSTATUS_ID = RCSS.CAMPSUBJECTSTATUS_ID)
  LEFT JOIN CAMPAIGN_ACTIVITY CA
  ON (CE.CAMPAIGN_ENROLMENT_ID=CA.CAMPAIGN_ENROLMENT_ID)
  INNER JOIN REF_CAMPAIGN_ACTIVITY_TYPE RCAT
  ON (CA.CAMPAIGN_ACTIVITY_TYPE_ID = RCAT.CAMPAIGN_ACTIVITY_TYPE_ID)
  INNER JOIN CAMPAIGN C
  ON (CE.CAMPAIGN_ID            = C.CAMPAIGN_ID)
  LEFT JOIN ORGANISATION org
  ON (CE.ORGANISATION_ID = org.ORGANISATION_ID)
  WHERE CA.CAMPAIGN_ACTIVITY_ID =
    (SELECT MAX(CAMPAIGN_ACTIVITY_ID)
    FROM CAMPAIGN_ACTIVITY
    WHERE CAMPAIGN_ACTIVITY.CAMPAIGN_ENROLMENT_ID = CE.CAMPAIGN_ENROLMENT_ID
    )
  );


-- Grant permissions section -------------------------------------------------
CREATE OR REPLACE PUBLIC SYNONYM REF_CAMPAIGN_STATUS FOR CCLA.REF_CAMPAIGN_STATUS;
CREATE OR REPLACE PUBLIC SYNONYM CAMPAIGN FOR CCLA.CAMPAIGN;
CREATE OR REPLACE PUBLIC SYNONYM CAMPAIGN_ENROLMENT FOR CCLA.CAMPAIGN_ENROLMENT;
CREATE OR REPLACE PUBLIC SYNONYM REF_CAMPAIGN_ENROLMENT_STATUS FOR CCLA.REF_CAMPAIGN_ENROLMENT_STATUS;
CREATE OR REPLACE PUBLIC SYNONYM REF_CAMPAIGN_ACTIVITY_TYPE FOR CCLA.REF_CAMPAIGN_ACTIVITY_TYPE;
CREATE OR REPLACE PUBLIC SYNONYM CAMPAIGN_ACTIVITY FOR CCLA.CAMPAIGN_ACTIVITY;
CREATE OR REPLACE PUBLIC SYNONYM NOTE FOR CCLA.NOTE;
CREATE OR REPLACE PUBLIC SYNONYM FORM FOR CCLA.FORM;
CREATE OR REPLACE PUBLIC SYNONYM REF_FORM_TYPE FOR CCLA.REF_FORM_TYPE;
CREATE OR REPLACE PUBLIC SYNONYM REF_FORM_STATUS FOR CCLA.REF_FORM_STATUS;
CREATE OR REPLACE PUBLIC SYNONYM FORM_ELEMENT_APPLIED FOR CCLA.FORM_ELEMENT_APPLIED;
CREATE OR REPLACE PUBLIC SYNONYM ENROLMENT_ACTION_QUEUE FOR CCLA.ENROLMENT_ACTION_QUEUE;
CREATE OR REPLACE PUBLIC SYNONYM REF_CAMPAIGN_ENROLMENT_ACTION FOR CCLA.REF_CAMPAIGN_ENROLMENT_ACTION;
CREATE OR REPLACE PUBLIC SYNONYM ENROLMENT_STATUS_ACTION_APPL FOR CCLA.ENROLMENT_STATUS_ACTION_APPL;
CREATE OR REPLACE PUBLIC SYNONYM FUND_INVESTMENT_INTENTION FOR CCLA.FUND_INVESTMENT_INTENTION;
CREATE OR REPLACE PUBLIC SYNONYM REF_INVESTMENT_INTENT_STATUS FOR CCLA.REF_INVESTMENT_INTENT_STATUS;
CREATE OR REPLACE PUBLIC SYNONYM REF_CAMPAIGN_SUBJECT_STATUS FOR CCLA.REF_CAMPAIGN_SUBJECT_STATUS;
CREATE OR REPLACE PUBLIC SYNONYM ENROLMENT_ACTIVITY_LAST FOR CCLA.ENROLMENT_ACTIVITY_LAST; 

GRANT ALL ON REF_CAMPAIGN_STATUS TO UCMADMIN;
GRANT ALL ON CAMPAIGN TO UCMADMIN;
GRANT ALL ON CAMPAIGN_ENROLMENT TO UCMADMIN;
GRANT ALL ON REF_CAMPAIGN_ENROLMENT_STATUS TO UCMADMIN;
GRANT ALL ON REF_CAMPAIGN_ACTIVITY_TYPE TO UCMADMIN;
GRANT ALL ON CAMPAIGN_ACTIVITY TO UCMADMIN;
GRANT ALL ON NOTE TO UCMADMIN;
GRANT ALL ON FORM TO UCMADMIN;
GRANT ALL ON REF_FORM_TYPE TO UCMADMIN;
GRANT ALL ON REF_FORM_STATUS TO UCMADMIN;
GRANT ALL ON FORM_ELEMENT_APPLIED TO UCMADMIN;
GRANT ALL ON ENROLMENT_ACTION_QUEUE TO UCMADMIN;
GRANT ALL ON REF_CAMPAIGN_ENROLMENT_ACTION TO UCMADMIN;
GRANT ALL ON ENROLMENT_STATUS_ACTION_APPL TO UCMADMIN;
GRANT ALL ON FUND_INVESTMENT_INTENTION TO UCMADMIN;
GRANT ALL ON REF_INVESTMENT_INTENT_STATUS TO UCMADMIN;
GRANT ALL ON REF_CAMPAIGN_SUBJECT_STATUS TO UCMADMIN;
GRANT ALL ON ENROLMENT_ACTIVITY_LAST TO UCMADMIN;