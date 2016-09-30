-- Table REF_AUTH_STATUS

CREATE TABLE REF_AUTH_STATUS(
  AUTH_STATUS_ID Number(15,0) CONSTRAINT AUTHSTATUS_ID_NN NOT NULL,
  AUTH_STATUS_NAME Varchar2(50 ) CONSTRAINT AUTHSTATUS_NAME_NN NOT NULL
)
/

-- Add keys for table REF_AUTH_STATUS

ALTER TABLE REF_AUTH_STATUS ADD CONSTRAINT AUTHSTATUS_PK PRIMARY KEY (AUTH_STATUS_ID)
/

-- Table and Columns comments section
  
COMMENT ON TABLE REF_AUTH_STATUS IS 'Authorisation statuses, currently used for storing the authorisation status of email addresses in the CONTACT_POINT table but could be expanded further.'
/


REM INSERTING into REF_AUTH_STATUS
Insert into REF_AUTH_STATUS (AUTH_STATUS_ID,AUTH_STATUS_NAME) values (1,'Unauthorised');
Insert into REF_AUTH_STATUS (AUTH_STATUS_ID,AUTH_STATUS_NAME) values (2,'Pending authorisation');
Insert into REF_AUTH_STATUS (AUTH_STATUS_ID,AUTH_STATUS_NAME) values (3,'Authorised');

-- Table FORM_CONTACT_APPLIED

CREATE TABLE FORM_CONTACT_APPLIED(
  FORM_ID Number(15,0) CONSTRAINT FRMCONTAPPL_FORMID_NN NOT NULL,
  CONTACT_ID Number(15,0) NOT NULL
)
/

-- Add keys for table FORM_CONTACT_APPLIED

ALTER TABLE FORM_CONTACT_APPLIED ADD CONSTRAINT FRMCONTAPPL_UQ PRIMARY KEY (FORM_ID,CONTACT_ID)
/

-- Table and Columns comments section
  
COMMENT ON TABLE FORM_CONTACT_APPLIED IS 'Mapping between Form IDs and Contact Point IDs. Currently used for Email Indemnity forms, to map the email addresses that are printed.'
/

CREATE INDEX CONTACT_FRMCONTAPPL_IX ON FORM_CONTACT_APPLIED (CONTACT_ID) 
/
ALTER TABLE FORM_CONTACT_APPLIED ADD CONSTRAINT CONTACT_FRMCONTAPPL_FK FOREIGN KEY (CONTACT_ID) REFERENCES CONTACT_POINT (CONTACT_ID)
/

CREATE INDEX FORM_FRMCONAPPL_IX ON FORM_CONTACT_APPLIED (FORM_ID) 
/
ALTER TABLE FORM_CONTACT_APPLIED ADD CONSTRAINT FORM_FRMCONAPPL_FK FOREIGN KEY (FORM_ID) REFERENCES FORM (FORM_ID)
/

CREATE OR REPLACE PUBLIC SYNONYM REF_AUTH_STATUS FOR CCLA.REF_AUTH_STATUS;
CREATE OR REPLACE PUBLIC SYNONYM FORM_CONTACT_APPLIED FOR CCLA.FORM_CONTACT_APPLIED;

GRANT SELECT ON REF_AUTH_STATUS TO UCMADMIN;
GRANT SELECT ON FORM_CONTACT_APPLIED TO UCMADMIN;

-- New column in CONTACT_POINT
ALTER TABLE CONTACT_POINT ADD (
	AUTH_STATUS_ID NUMBER(15,0)
);

CREATE INDEX AUTHSTATUS_CONTPOINT_IX ON CONTACT_POINT (AUTH_STATUS_ID) 
/
ALTER TABLE CONTACT_POINT ADD CONSTRAINT AUTHSTATUS_CONTPOINT_PK FOREIGN KEY (AUTH_STATUS_ID) REFERENCES REF_AUTH_STATUS (AUTH_STATUS_ID)
/

-- Set all Auth Statuses to 1
UPDATE CONTACT_POINT SET AUTH_STATUS_ID = 1;

ALTER TABLE CONTACT_POINT MODIFY (
	AUTH_STATUS_ID Number(15,0) CONSTRAINT CONTPOINT_AUTHSTATUS_NN NOT NULL
);
