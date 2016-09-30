-- 1. CREATE AND AMEND TABLES

-- Table CCLA.REF_FUND_DEFINITION_CODE
CREATE TABLE REF_FUND_DEFINITION_CODE(
  FUND_DEFCODE_ID Number(10,0) NOT NULL,
  FUND_DEFCODE Varchar2(10 CHAR) NOT NULL,
  DEFCODE_DESC Varchar2(50 CHAR)
)
/

-- Add keys for table REF_FUND_DEFINITION_CODE
ALTER TABLE REF_FUND_DEFINITION_CODE 
ADD CONSTRAINT FUND_DEFCODE_ID_PK PRIMARY KEY (FUND_DEFCODE_ID)
/

-- Table FUND
ALTER TABLE FUND
ADD FUND_DEFCODE_ID Number(10,0);


-- 2. TABLE ACCESS

/* Synonyms */
CREATE OR REPLACE PUBLIC SYNONYM REF_FUND_DEFINITION_CODE FOR CCLA.REF_FUND_DEFINITION_CODE;
/* Grant Access */
GRANT ALL ON REF_FUND_DEFINITION_CODE TO UCMADMIN;
/

-- 3. INSERT DATA
/
INSERT INTO REF_FUND_DEFINITION_CODE (FUND_DEFCODE_ID, FUND_DEFCODE, DEFCODE_DESC) VALUES (1, 'DEP', 'Deposit Type Fund');
INSERT INTO REF_FUND_DEFINITION_CODE (FUND_DEFCODE_ID, FUND_DEFCODE, DEFCODE_DESC) VALUES (2, 'INV', 'Investment Type Fund');
INSERT INTO REF_FUND_DEFINITION_CODE (FUND_DEFCODE_ID, FUND_DEFCODE, DEFCODE_DESC) VALUES (3, 'FIXINT', 'Fixed Interest Type Fund');
INSERT INTO REF_FUND_DEFINITION_CODE (FUND_DEFCODE_ID, FUND_DEFCODE, DEFCODE_DESC) VALUES (4, 'GLOBEQU', 'Global Equity Type Fund');
INSERT INTO REF_FUND_DEFINITION_CODE (FUND_DEFCODE_ID, FUND_DEFCODE, DEFCODE_DESC) VALUES (5, 'PROP', 'Property Type Fund');
INSERT INTO REF_FUND_DEFINITION_CODE (FUND_DEFCODE_ID, FUND_DEFCODE, DEFCODE_DESC) VALUES (6, 'UKEQU', 'UK Equity Type Fund');
INSERT INTO REF_FUND_DEFINITION_CODE (FUND_DEFCODE_ID, FUND_DEFCODE, DEFCODE_DESC) VALUES (7, 'ETHINV', 'Ethical Investment Type Fund');
INSERT INTO REF_FUND_DEFINITION_CODE (FUND_DEFCODE_ID, FUND_DEFCODE, DEFCODE_DESC) VALUES (8, 'PSDF', 'PSDF Type Fund');


UPDATE FUND SET FUND_DEFCODE_ID=1 WHERE FUND_CODE IN ('C','D');
UPDATE FUND SET FUND_DEFCODE_ID=2 WHERE FUND_CODE IN ('R','T','S','J');
UPDATE FUND SET FUND_DEFCODE_ID=3 WHERE FUND_CODE IN ('F','K','A','B');
UPDATE FUND SET FUND_DEFCODE_ID=4 WHERE FUND_CODE IN ('L','M','U','Z');
UPDATE FUND SET FUND_DEFCODE_ID=5 WHERE FUND_CODE IN ('N','P','V','W','62');
UPDATE FUND SET FUND_DEFCODE_ID=6 WHERE FUND_CODE IN ('X','Y');
UPDATE FUND SET FUND_DEFCODE_ID=7 WHERE FUND_CODE IN ('AA','AB');
UPDATE FUND SET FUND_DEFCODE_ID=8 WHERE FUND_CODE IN ('PS');

ALTER TABLE FUND
MODIFY (FUND_DEFCODE_ID Number(10,0) not null);
/
ALTER TABLE FUND ADD CONSTRAINT FUNDDEFCODE_FUND_FK FOREIGN KEY (FUND_DEFCODE_ID) REFERENCES REF_FUND_DEFINITION_CODE (FUND_DEFCODE_ID)
/
CREATE INDEX IX_FUNDDEFCODE_FUND_FK ON FUND (FUND_DEFCODE_ID)
/
-- Table and Columns comments section
COMMENT ON COLUMN FUND.FUND_DEFCODE_ID IS 'The definition of this type of fund, i.e deposit, investment, Global Equity, UK, fixed interest etc..'
/