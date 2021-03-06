-- Table REF_VERIFICATION_SOURCE

-- Table REF_VERIFICATION_SOURCE

CREATE TABLE REF_VERIFICATION_SOURCE(
  VERIFICATION_SOURCE_ID Number(10,0) NOT NULL,
  VER_SOURCE_NAME Varchar2(50 ) CONSTRAINT VERSOURCE_NAME_NN NOT NULL,
  VER_SOURCE_DESCRIPTION Varchar2(300 ),
  SUPPORTING_DOC_REQURIED Integer DEFAULT 0 CONSTRAINT VERSOURCE_SUPPORTINGDOCREQ_NN NOT NULL
)
/

-- Add keys for table REF_VERIFICATION_SOURCE

ALTER TABLE REF_VERIFICATION_SOURCE ADD CONSTRAINT VERSOURCE_PK PRIMARY KEY (VERIFICATION_SOURCE_ID)
/

ALTER TABLE REF_VERIFICATION_SOURCE ADD CONSTRAINT VERIFICATIONSOURCE_NAME_UK UNIQUE (VER_SOURCE_NAME)
/

-- Table REF_ELEMENT_ATTRIBUTES
ALTER TABLE REF_ELEMENT_ATTRIBUTES
ADD  (VERIFICATION_SOURCE_ID Number(10,0),
	ELEMENT_IDENTIFIER_ID Number(10,0))
/

ALTER TABLE REF_ELEMENT_ATTRIBUTES ADD CONSTRAINT ELEMATTR_ELEMIDS_FK FOREIGN KEY (ELEMENT_IDENTIFIER_ID) REFERENCES REF_ELEMENT_IDENTIFIERS (ELEMENT_IDENTIFIER_ID)
/


CREATE INDEX IX_VERSOURCE_ELEMATTRS_FK ON REF_ELEMENT_ATTRIBUTES (VERIFICATION_SOURCE_ID) 
/
ALTER TABLE REF_ELEMENT_ATTRIBUTES ADD CONSTRAINT VERSOURCE_ELEMATTRS_FK FOREIGN KEY (VERIFICATION_SOURCE_ID) REFERENCES REF_VERIFICATION_SOURCE (VERIFICATION_SOURCE_ID)
/

-- Table ELEMENT_ATTRIBUTE_APPLIED

ALTER TABLE ELEMENT_ATTRIBUTE_APPLIED
ADD (ATTRIBUTE_STATUS Number(1,0), 
	SUPPORTING_DOC_ID Number(38,0),
	LAST_UPDATED TIMESTAMP(6), 
	LAST_UPDATED_BY varchar2(90 CHAR))
/

ALTER TABLE ELEMENT_IDENTIFIERS_APPLIED
ADD (LAST_UPDATED TIMESTAMP(6), 
	LAST_UPDATED_BY varchar2(90 CHAR))
/

-- Run as SYS
GRANT References ON UCMADMIN.REVISIONS TO CCLA;

-- Add foreign key for dID (run as CCLA)
ALTER TABLE ELEMENT_ATTRIBUTE_APPLIED ADD CONSTRAINT ELEMATTRAPPL_DOCID_FK FOREIGN KEY (SUPPORTING_DOC_ID) REFERENCES UCMADMIN.REVISIONS (DID)
/

-- Table REF_PROPERTY

ALTER TABLE REF_PROPERTY
ADD VERIFICATION_SOURCE_ID Number(10,0)
/

CREATE INDEX IX_VERSOURCE_PROPERTY_FK ON REF_PROPERTY (VERIFICATION_SOURCE_ID) 
/
ALTER TABLE REF_PROPERTY ADD CONSTRAINT VERSOURCE_PROPERTY_FK FOREIGN KEY (VERIFICATION_SOURCE_ID) REFERENCES REF_VERIFICATION_SOURCE (VERIFICATION_SOURCE_ID)
/

ALTER TABLE REF_RELATION_PROPERTY ADD CONSTRAINT RELAPROP_UK UNIQUE (RELATION_NAME_ID,PROPERTY_ID)
/

-- Table and Columns comments section
  

COMMENT ON COLUMN ELEMENT_ATTRIBUTE_APPLIED.ATTRIBUTE_STATUS IS 'This will store the appropriate value when an attribute can have a status of 0 or 1.'
/

INSERT INTO REF_VERIFICATION_SOURCE values (1,'Guidestar',NULL,0);
INSERT INTO REF_VERIFICATION_SOURCE values (2,'Charity Commission',NULL,0);
INSERT INTO REF_VERIFICATION_SOURCE values (3,'HMRC','Her Majesty''s Revenue and Customs',0);
INSERT INTO REF_VERIFICATION_SOURCE values (4,'Companies House',NULL,0);
INSERT INTO REF_VERIFICATION_SOURCE values (5,'ONS','Office of National Statistics',0);
INSERT INTO REF_VERIFICATION_SOURCE values (6,'Crockfords',NULL,0);
INSERT INTO REF_VERIFICATION_SOURCE values (7,'Diocese Directories',NULL,0);
INSERT INTO REF_VERIFICATION_SOURCE values (8,'FSA',NULL,0);

INSERT INTO ref_element_attributes values (1,'Verified By Guidestar',1,NULL,0,1,1); -- Guidestar
INSERT INTO ref_element_attributes values (2,'Verified By Charity Commission',1,NULL,1,2,1); -- Charity Commission
INSERT INTO ref_element_attributes values (3,'Verified By ONS Database',1,NULL,1,5,2); -- ONS
INSERT INTO ref_element_attributes values (4,'Verified By Diocese Directory',1,NULL,1,7,4); -- Diocese Dir.


insert into REF_PROPERTY values (2,'Verified By Guidestar',1);

insert into ref_relation_property values (3,1,2);
insert into ref_relation_property values (4,3,2);
insert into ref_relation_property values (5,5,2);

ALTER TABLE RELATION_PROPERTY_APPLIED
ADD (LAST_UPDATED TIMESTAMP(6), 
	LAST_UPDATED_BY varchar2(90 CHAR))
/

-- Synonyms and grants
CREATE OR REPLACE PUBLIC SYNONYM REF_ELEMENT_ATTRIBUTES FOR CCLA.REF_ELEMENT_ATTRIBUTES;
CREATE OR REPLACE PUBLIC SYNONYM ELEMENT_IDENTIFIERS_APPLIED FOR CCLA.ELEMENT_IDENTIFIERS_APPLIED;
CREATE OR REPLACE PUBLIC SYNONYM REF_VERIFICATION_SOURCE FOR CCLA.REF_VERIFICATION_SOURCE;
CREATE OR REPLACE PUBLIC SYNONYM RELATION_PROPERTY_APPLIED FOR CCLA.RELATION_PROPERTY_APPLIED;

GRANT ALL ON REF_VERIFICATION_SOURCE TO UCMADMIN;