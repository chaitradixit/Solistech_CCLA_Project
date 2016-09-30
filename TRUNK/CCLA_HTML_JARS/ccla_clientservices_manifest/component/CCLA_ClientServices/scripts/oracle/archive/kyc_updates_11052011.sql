-- Add new CS IVS Override attribute for Accounts
INSERT INTO REF_ELEMENT_ATTRIBUTES VALUES 
(31, 'CS IVS Check Override', 3, 
'Used to override standard IVS Checking for an account',
1, 9, null, 'CCLA-Client Service', 'BOOL', 3);

-- Add new Compliance IVS Override attribute for Accounts
INSERT INTO REF_ELEMENT_ATTRIBUTES VALUES 
(32, 'Compl. IVS Check Override', 3, 
'Used to override standard IVS Checking for an account',
1, 10, null, 'CCLA-Compliance', 'BOOL', 3);

-- Add new CS Relation Override attribute for Accounts
INSERT INTO REF_ELEMENT_ATTRIBUTES VALUES 
(33, 'CS Relation Check Override', 1, 
'Used to override standard Relationship Checking for an organisation',
1, 9, null, 'CCLA-Client Service', 'BOOL', 2);

-- Add new Compliance Relation Override attribute for Accounts
INSERT INTO REF_ELEMENT_ATTRIBUTES VALUES 
(34, 'Compl. Relation Check Override', 1, 
'Used to override standard Relationship Checking for an organisation',
1, 10, null, 'CCLA-Compliance', 'BOOL', 2);

-- Katie's attribs
REM INSERTING into REF_ELEMENT_ATTRIBUTES
Insert into REF_ELEMENT_ATTRIBUTES (ELEMENT_ATTRIBUTE_ID,ELEMENT_ATTRIBUTE_NAME,ELEMENT_TYPE_ID,ELEMENT_ATTRIBUTE_DESCRIPTION,SET_BY_USER,VERIFICATION_SOURCE_ID,ELEMENT_IDENTIFIER_ID,USER_GROUPS,ELEMENT_ATTRIBUTE_DATA_TYPE,ELEMENT_ATTRIBUTE_TYPE_ID) values (35,'PSDF Seed Fund Account',3,'Is account a seed fund account',1,null,null,'CCLA-Client Service','BOOL',5);
Insert into REF_ELEMENT_ATTRIBUTES (ELEMENT_ATTRIBUTE_ID,ELEMENT_ATTRIBUTE_NAME,ELEMENT_TYPE_ID,ELEMENT_ATTRIBUTE_DESCRIPTION,SET_BY_USER,VERIFICATION_SOURCE_ID,ELEMENT_IDENTIFIER_ID,USER_GROUPS,ELEMENT_ATTRIBUTE_DATA_TYPE,ELEMENT_ATTRIBUTE_TYPE_ID) values (36,'PSDF Early Investor Account',3,'Is account part of early investor group?',1,null,null,'CCLA-Client Service','BOOL',5);
Insert into REF_ELEMENT_ATTRIBUTES (ELEMENT_ATTRIBUTE_ID,ELEMENT_ATTRIBUTE_NAME,ELEMENT_TYPE_ID,ELEMENT_ATTRIBUTE_DESCRIPTION,SET_BY_USER,VERIFICATION_SOURCE_ID,ELEMENT_IDENTIFIER_ID,USER_GROUPS,ELEMENT_ATTRIBUTE_DATA_TYPE,ELEMENT_ATTRIBUTE_TYPE_ID) values (37,'Manual PEP Override',2,'Indicates PEP check has been manually overridden',1,null,null,null,'BOOL',1);

-- Add new Legacy IVS Override attribute for Accounts. This
-- will be used to import the current account overrides
INSERT INTO REF_ELEMENT_ATTRIBUTES VALUES 
(38, 'Legacy IVS Check Override', 3, 
'Used to override standard IVS Checking for an account',
1, 13, null, null, 'BOOL', 3);

-- Add check constraint to Elem Attrib Appl.
ALTER TABLE ELEMENT_ATTRIBUTE_APPLIED ADD CONSTRAINT ELEMATTRAPPL_VAL_CK CHECK (NOT
(ATTRIBUTE_STATUS IS NULL 
AND 
ATTRIBUTE_VALUE IS NULL));


-- Updates to Relation Properties

-- Delete all applied 'Listed on Mandate' properties
DELETE FROM RELATION_PROPERTY_APPLIED WHERE RELATION_PROPERTY_ID IN (7,8,9,10,11,12,16);

-- Delete all Relation-Property mappings for 'Listed on Mandate' so its no longer available
DELETE FROM REF_RELATION_PROPERTY WHERE RELATION_PROPERTY_ID IN (7,8,9,10,11,12,16);

-- New Verification Sources
REM INSERTING into REF_VERIFICATION_SOURCE
Insert into REF_VERIFICATION_SOURCE (VERIFICATION_SOURCE_ID,VER_SOURCE_NAME,VER_SOURCE_DESCRIPTION,SUPPORTING_DOC_REQUIRED,IS_OVERRIDE) values (17,'Meeting Minutes','Minutes from organisation meeting, used as proof of Person relationships to organisation',1,0);
Insert into REF_VERIFICATION_SOURCE (VERIFICATION_SOURCE_ID,VER_SOURCE_NAME,VER_SOURCE_DESCRIPTION,SUPPORTING_DOC_REQUIRED,IS_OVERRIDE) values (18,'Report and Accounts',null,0,0);

-- New Properties for Ver Sources above.
REM INSERTING into REF_PROPERTY
Insert into REF_PROPERTY (PROPERTY_ID,PROPERTY_NAME,VERIFICATION_SOURCE_ID,SET_BY_USER,PROPERTY_TYPE,IS_SINGLETON) values (12,'Verified by Meeting Minutes',17,1,'BOOL',0);
Insert into REF_PROPERTY (PROPERTY_ID,PROPERTY_NAME,VERIFICATION_SOURCE_ID,SET_BY_USER,PROPERTY_TYPE,IS_SINGLETON) values (13,'Verified by R and A',18,1,'BOOL',0);

-- New Relation-Property maps for Auth Person/S151 officer relations
REM INSERTING into REF_RELATION_PROPERTY
Insert into REF_RELATION_PROPERTY (RELATION_PROPERTY_ID,RELATION_NAME_ID,PROPERTY_ID) values (24,2,12);
Insert into REF_RELATION_PROPERTY (RELATION_PROPERTY_ID,RELATION_NAME_ID,PROPERTY_ID) values (25,2,13);
Insert into REF_RELATION_PROPERTY (RELATION_PROPERTY_ID,RELATION_NAME_ID,PROPERTY_ID) values (26,11,12);

-- Add CS IVS Overrides for every account that has an AML_CHECK_OVERRIDE user set.
INSERT INTO ELEMENT_ATTRIBUTE_APPLIED (ELEMENT_ID, ELEMENT_ATTRIBUTE_ID, DATE_ADDED, ATTRIBUTE_STATUS, LAST_UPDATED, LAST_UPDATED_BY, SUPPORTING_DOC_ID, ATTRIBUTE_VALUE)
SELECT ACCOUNT_ID, 38, SYSDATE, 1, SYSDATE, AML_CHECK_OVERRIDE_USER, null, null
FROM ACCOUNT WHERE AML_CHECK_OVERRIDE_USER IS NOT NULL;


UPDATE REF_ACCOUNT_FLAG SET ADD_TO_WATCHLIST = 0 WHERE ACCOUNT_FLAG_ID = 10;