
-- Data load for CCLA override sources/attributes.
INSERT INTO REF_VERIFICATION_SOURCE VALUES (9, 'Client Services', null, 0);
INSERT INTO REF_VERIFICATION_SOURCE VALUES (10, 'Compliance Dept.', null, 0);

INSERT INTO REF_ELEMENT_ATTRIBUTES VALUES 
(5, 'CS Entity Check Override', 1, 
'Used to override standard Entity Checking for an organisation',
1, 9, null);

INSERT INTO REF_ELEMENT_ATTRIBUTES VALUES 
(6, 'Compl. Entity Check Override', 1, 
'Used to override standard Entity Checking for an organisation',
1, 10, null);

-- Add new IS_OVERRIDE column to REF_VERIFICATION_SOURCE
ALTER TABLE REF_VERIFICATION_SOURCE ADD (IS_OVERRIDE NUMBER(1,0) DEFAULT 0 CONSTRAINT VERSOURCE_ISOVERRIDE_NN NOT NULL);

-- Set new Verification sources as overrides
UPDATE REF_VERIFICATION_SOURCE SET IS_OVERRIDE = 1
WHERE VERIFICATION_SOURCE_ID IN (9,10);

-- Table SYSTEM_CONFIG_VAR

CREATE TABLE SYSTEM_CONFIG_VAR(
  CONFIG_VAR_NAME Varchar2(50 ) CONSTRAINT SYSCFGVAR_NAME_NN NOT NULL,
  CONFIG_VAR_DESCRIPTION Varchar2(500 ),
  CONFIG_VAR_DATA_TYPE Varchar2(30 ) CONSTRAINT SYSCFGVAR_DATATYPE_NN NOT NULL
        CONSTRAINT SYSCFGVAR_DATATYPE_CHK_CK CHECK (CONFIG_VAR_DATA_TYPE IN 
('STRING','INT','FLOAT','DATE','BOOL')),
  STRING_VALUE Varchar2(100 ),
  INT_VALUE Number(38,0),
  FLOAT_VALUE Number(38,15),
  DATE_VALUE Timestamp(6),
  BOOL_VALUE Number(1,0)
        CONSTRAINT SYSCFGVAR_BOOLVAR_CHK_CK CHECK (BOOL_VALUE IN (NULL, 0, 1)),
  LAST_UPDATED Timestamp(6) CONSTRAINT SYSCFGVAR_LASTUPD_NN NOT NULL,
  LAST_UPDATED_BY Varchar2(90 ) CONSTRAINT SYSCFGVAR_LASTUPDBY_NN NOT NULL
)
/

-- Add keys for table SYSTEM_CONFIG_VAR

ALTER TABLE SYSTEM_CONFIG_VAR ADD CONSTRAINT SYSCFGVAR_PK_PK PRIMARY KEY (CONFIG_VAR_NAME)
/

-- Table and Columns comments section
  
COMMENT ON TABLE SYSTEM_CONFIG_VAR IS 'Used to store configuration strings, numbers and dates used by applications connected to the central database'
/

INSERT INTO SYSTEM_CONFIG_VAR VALUES ('IdentityCheck_DaysResultValid', 
'No. of days which a cached Identity Check result is valid for. Results are automatically recalculated when this period has exceeded.',
'INT',
null, 7, null, null, null, SYSDATE, 'sysadmin');

INSERT INTO SYSTEM_CONFIG_VAR VALUES ('IdentityCheck_DaysCheckValid', 
'No. of days which Identity Check scores are valid for. When this period expires, a flag is set to indicate the score has expired and will need to be re-checked.',
'INT',
null, 730, null, null, null, SYSDATE, 'sysadmin');

INSERT INTO SYSTEM_CONFIG_VAR VALUES ('IdentityCheck_FixedExpiryDate', 
'If this date is non-null, it will be set as the Expiry Date for all subsequent identity checks.',
'DATE',
null, 730, null, TO_DATE('01/01/2015','dd/mm/yyyy'), null, SYSDATE, 'sysadmin');

CREATE OR REPLACE PUBLIC SYNONYM SYSTEM_CONFIG_VAR FOR CCLA.SYSTEM_CONFIG_VAR;
GRANT ALL ON SYSTEM_CONFIG_VAR TO UCMADMIN;

-- First generic IVS attribute
INSERT INTO REF_ELEMENT_ATTRIBUTES VALUES (7, 'Identity checked', 2, 
'Indicates the person''s identity has been authenticated and/or verified', 0, null, null);
-- Indicates that an Experian Authentication check took place, and the PEP risk indicator wasn't returned
INSERT INTO REF_ELEMENT_ATTRIBUTES VALUES (8, 'PEP checked', 2, 
'Indicates the person has had an Experian Authentication check against them, which didn''t return a PEP risk indicator', 0, null, null);


-- Create a dump of all Add-Relation audit records, in order to extract the usernames/times.
-- Run as UCMADMIN
CREATE TABLE RELATION_LAST_UPDATED_BY AS
(SELECT USERID, SDAUDIT.AUDIT_DATE, audRel.RELATIONID FROM SDAUDIT INNER JOIN SDAUDIT_RELATIONS audRel ON (SDAUDIT.AUDITID = audRel.AUDITID)
 WHERE SDAUDIT.ACTION = 'ADD' AND SDAUDIT.EVENT = 'Relation'
 AND audRel.RELATIONTYPE = 'Relation'
 AND audRel.RELATIONID IN (SELECT RELATION_ID FROM RELATIONS));

-- Add the new LAST_UPDATED_BY column to RELATIONS
-- Run as CCLA
ALTER TABLE RELATIONS ADD (LAST_UPDATED_BY VARCHAR2(90 CHAR));
 
-- Do a blanket update to set every single Last Update user value to 'System' 
-- Run as UCMADMIN
UPDATE RELATIONS relMain SET LAST_UPDATED_BY = 'System'; 
 
-- Now apply specific user names taken from the audit records
-- Run as UCMADMIN
UPDATE RELATIONS relMain SET LAST_UPDATED_BY = 
	(SELECT USERID FROM RELATION_LAST_UPDATED_BY relAud 
	WHERE (relAud.RELATIONID = relMain.RELATION_ID))

WHERE relmain.relation_id IN (
  
	(SELECT RELATIONID FROM RELATION_LAST_UPDATED_BY relAud 
	WHERE (relAud.RELATIONID = relMain.RELATION_ID))
);

-- Add a constraint to the new column
-- Run as CCLA
ALTER TABLE RELATIONS MODIFY (LAST_UPDATED_BY VARCHAR2(90 CHAR) CONSTRAINT RELATIONS_LSTUPDATEDBY_NN NOT NULL);

-- Finally, drop the temp table.
DROP TABLE RELATION_LAST_UPDATED_BY;
 
-- Add new relation property 'Job title'.
Insert into REF_PROPERTY (PROPERTY_ID,PROPERTY_NAME,VERIFICATION_SOURCE_ID) values (3,'Job title',null);
-- Link 'Job title' to Associate Org-Person relation
Insert into REF_RELATION_PROPERTY VALUES (6, 7, 3);

-- Mandate verification source. Requires document ID set.
INSERT INTO REF_VERIFICATION_SOURCE VALUES (11, 'Mandate', null, 1, 0);

-- Add new verification relation property 'Listed on Mandate'.
Insert into REF_PROPERTY (PROPERTY_ID,PROPERTY_NAME,VERIFICATION_SOURCE_ID) values (4,'Listed on Mandate',11);
-- Link 'Listed on Mandate' to Corr, A.Pers and Trst Org-Person relations
Insert into REF_RELATION_PROPERTY VALUES (7, 1, 4);
Insert into REF_RELATION_PROPERTY VALUES (8, 2, 4);
Insert into REF_RELATION_PROPERTY VALUES (9, 3, 4);
-- Link 'Listed on Mandate' to Corr, A.Pers and Sig Person-Account relations
Insert into REF_RELATION_PROPERTY VALUES (10, 80, 4);
Insert into REF_RELATION_PROPERTY VALUES (11, 81, 4);
Insert into REF_RELATION_PROPERTY VALUES (12, 84, 4);

-- Add new SET_BY_USER column to REF_PROPERTY table
ALTER TABLE REF_PROPERTY ADD (SET_BY_USER NUMBER(1,0) DEFAULT 1 CONSTRAINT REFPROP_SETBYUSER_NN NOT NULL);

-- Mark Guidestar property as not set by user
UPDATE REF_PROPERTY SET SET_BY_USER = 0 WHERE PROPERTY_ID IN (2);

-- Add new column to Rel Prop Applied.
ALTER TABLE RELATION_PROPERTY_APPLIED ADD (
  PROPERTY_STATUS NUMBER(1,0) DEFAULT 1 CONSTRAINT RELPROPAPPL_STATUS_NN NOT NULL
);

COMMENT ON COLUMN RELATION_PROPERTY_APPLIED.PROPERTY_STATUS IS 'Determines the status of the applied property. 0=False, 1=True.'; 