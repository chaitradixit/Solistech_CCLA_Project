/*
Created: 01/11/2010
Modified: 27/03/2012
Project: CCLA 
Model: Central Database
Company: Kainos Software
Author: Stephen Gibbons
Version: 8.10
Database: Oracle 11g Release 1
*/


-- Create tables section -------------------------------------------------

-- Table CCLA.CCLA_ACCOUNT

CREATE TABLE CCLA.CCLA_ACCOUNT(
  CCLA_ACCOUNT_ID Number(15,0) NOT NULL,
  CCLA_ACCOUNT_CODE Varchar2(16 ) NOT NULL,
  LAST_UPDATED_BY Varchar2(50 ) NOT NULL,
  DATE_ADDED Timestamp(6) CONSTRAINT PER_DATEADDED_NN NOT NULL,
  LAST_UPDATED Timestamp(6) CONSTRAINT PER_LASTUPDATED_NN NOT NULL
)
/

-- Add keys for table CCLA.CCLA_ACCOUNT

ALTER TABLE CCLA.CCLA_ACCOUNT ADD CONSTRAINT CCLAACCOUNT_PK PRIMARY KEY (CCLA_ACCOUNT_ID)
/

-- Table and Columns comments section
  
COMMENT ON COLUMN CCLA.CCLA_ACCOUNT.CCLA_ACCOUNT_ID IS 'CCLA Account primary key'
/
COMMENT ON COLUMN CCLA.CCLA_ACCOUNT.CCLA_ACCOUNT_CODE IS 'Unique Account code made up of 4 Alpha and 8 Numeric characters'
/
COMMENT ON COLUMN CCLA.CCLA_ACCOUNT.LAST_UPDATED_BY IS 'User who last updated the record'
/
COMMENT ON COLUMN CCLA.CCLA_ACCOUNT.DATE_ADDED IS 'Date record was added
'
/
COMMENT ON COLUMN CCLA.CCLA_ACCOUNT.LAST_UPDATED IS 'Date record was last updated
'
/

-- Create relationships section ------------------------------------------------- 

--CREATE INDEX IX_ELEMENT_CCLAACCOUNT_FK ON CCLA.CCLA_ACCOUNT (CCLA_ACCOUNT_ID) 
/
ALTER TABLE CCLA.CCLA_ACCOUNT ADD CONSTRAINT ELEMENT_CCLAACCOUNT_FK FOREIGN KEY (CCLA_ACCOUNT_ID) REFERENCES CCLA.ELEMENT (ELEMENT_ID)
/

CREATE INDEX IX_USERS_CCLA_ACCOUNT_FK ON CCLA.CCLA_ACCOUNT (LAST_UPDATED_BY) 
/
ALTER TABLE CCLA.CCLA_ACCOUNT ADD CONSTRAINT USERS_CCLA_ACCOUNT_FK FOREIGN KEY (LAST_UPDATED_BY) REFERENCES CCLA.USERS (DNAME)
/

--Alter Table Section

ALTER TABLE ccla.account ADD (acc_account_code VARCHAR2(12));

-- Rebuild Account Extended view with new column
CREATE OR REPLACE FORCE VIEW "CCLA"."V_ACCOUNT_EXTENDED_CLIENT" ("ACCOUNT_ID", "MANDATED_ACCOUNT_ID", "INCOME_DISTRIBUTION_METHOD", "FUND_CODE", "ACCOUNT_STATUS_ID", "AURORA_ACCOUNT", "ACCOUNTNUMBER", "ACCNUMEXT", "SUBTITLE", "DATE_OPENED", "SHARE_CLASS_ID", "REQ_SIGNATURES", "IS_EXCLUSIVE", "AML_CHECK_OVERRIDE_USER", "LAST_UPDATED", "ACCOUNT_TYPE_ID", "LOAN_TYPE_ID", "LOAN_TERM", "AGREEMENT_TYPE_ID", "ACC_ACCOUNT_CODE", "ACCOUNT_TYPE_NAME", "ACCOUNT_STATUS_NAME", "SHORT_NAME", "IDM_NAME", "ACC_CASH", "ACC_UNITS", "DATE_LAST_REFRESH", "ORGANISATION_ID", "ORG_ACCOUNT_CODE", "ORGANISATION_NAME", "CLIENT_NUMBER")
AS
  SELECT acc."ACCOUNT_ID",
    acc."MANDATED_ACCOUNT_ID",
    acc."INCOME_DISTRIBUTION_METHOD",
    acc."FUND_CODE",
    acc."ACCOUNT_STATUS_ID",
    acc."AURORA_ACCOUNT",
    acc."ACCOUNTNUMBER",
    acc."ACCNUMEXT",
    acc."SUBTITLE",
    acc."DATE_OPENED",
    acc."SHARE_CLASS_ID",
    acc."REQ_SIGNATURES",
    acc."IS_EXCLUSIVE",
    acc."AML_CHECK_OVERRIDE_USER",
    acc."LAST_UPDATED",
    acc."ACCOUNT_TYPE_ID",
    acc."LOAN_TYPE_ID",
    acc."LOAN_TERM",
    acc."AGREEMENT_TYPE_ID",
    acc."ACC_ACCOUNT_CODE",
    accType."ACCOUNT_TYPE_NAME",
    accStatus.ACCOUNT_STATUS_NAME,
    accStatus.SHORT_NAME,
    incDistMethod.IDM_NAME,
    accValue.ACC_CASH,
    accValue.ACC_UNITS,
    accValue.DATE_LAST_REFRESH,
    org.ORGANISATION_ID,
    org.ORG_ACCOUNT_CODE,
    org.ORGANISATION_NAME,
    cli.CLIENT_NUMBER
  FROM ACCOUNT acc
  INNER JOIN REF_ACCOUNT_STATUS accStatus
  ON (acc.ACCOUNT_STATUS_ID = accStatus.ACCOUNT_STATUS_ID)
  INNER JOIN REF_INCOME_DIST_METHOD incDistMethod
  ON (acc.INCOME_DISTRIBUTION_METHOD = incDistMethod.IDM_CODE)
  INNER JOIN REF_ACCOUNT_TYPE accType
  ON (acc.ACCOUNT_TYPE_ID = accType.ACCOUNT_TYPE_ID)
  LEFT JOIN ACCOUNT_VALUE accValue
  ON (acc.ACCOUNT_ID = accValue.ACCOUNT_ID)
  INNER JOIN RELATIONS rel
  ON (acc.ACCOUNT_ID       = rel.ELEMENT_ID2
  AND rel.RELATION_NAME_ID = 20)
  INNER JOIN ORGANISATION org
  ON (org.ORGANISATION_ID = rel.ELEMENT_ID1)
    -- Client Aurora Map. Group by CLIENT_NUMBER values to avoid duplicate rows
  LEFT JOIN
    (SELECT cli.CLIENT_NUMBER,
      cli.ORGANISATION_ID
    FROM CLIENT_AURORA_MAP cli
    GROUP BY cli.ORGANISATION_ID,
      cli.CLIENT_NUMBER
    ) cli
  ON (org.ORGANISATION_ID = cli.ORGANISATION_ID)
  ORDER BY org.ORGANISATION_ID,
    ACC.FUND_CODE,
    acc.accountnumber;

-- Data Insert Section-----------------------------------------------------------------


insert into ccla.ref_element_type values (5, 'CCLA Account');

insert into ccla.ref_relation_types values (5, 'Organisation-CCLAAccount', 1, 5);

insert into ccla.ref_relation_names values (70, 5, 'Owner','Owner',1);

insert into ccla.ref_relation_types values (23, 'Person-CCLAAccount', 2, 5);

insert into ccla.ref_relation_names values (88, 23, 'Owner','Owner',1);