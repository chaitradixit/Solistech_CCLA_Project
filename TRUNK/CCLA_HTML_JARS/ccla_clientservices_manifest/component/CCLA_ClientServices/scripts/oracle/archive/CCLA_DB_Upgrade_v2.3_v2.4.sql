
CREATE TABLE COMMUNICATION(
  COMM_ID Number(15,0) CONSTRAINT COMM_ID_NN NOT NULL,
  COMM_TYPE Varchar2(50 ) CONSTRAINT COMM_TYPE_NN NOT NULL,
  COMM_STATUS_ID Number(15,0) CONSTRAINT COMM_STATUS_NN NOT NULL,
  ORGANISATION_ID Number(15,0),
  PERSON_ID Number(15,0),
  SOURCE_ACCOUNT_ID Number(15,0),
  SOURCE_BANK_ACCOUNT_ID Number(15,0),
  CASH Number(15,10),
  UNITS Number(15,10),
  ORIGINAL_VALUE Number(15,10),
  DEST_ACCOUNT_ID Number(15,0),
  DEST_BANK_ACCOUNT_ID Number(15,0),
  SHARE_CLASS_ID Number(15,0),
  DOCNAME Varchar2(50 ),
  INTERACTION_ID Number(15,0),
  BUNDLE_REF Varchar2(50 ),
  WORKFLOW_ID Number(15,0),
  SPP_JOB_ID Varchar2(40 ),
  USER_ID Varchar2(90 ) CONSTRAINT COMMUSERID_NN NOT NULL,
  SUSPENDED Number(1,0) NOT NULL,
  COMPLETED Number(1,0) CONSTRAINT COMMCOMPLETED_NN NOT NULL,
  PROCESS_DATE Timestamp(6),
  DEPENDENT_COMM_ID Number(15,0),
  DATE_ADDED Timestamp(6) CONSTRAINT COMMDATEADDED_NN NOT NULL,
  DATE_COMPLETED Timestamp(6),
  LAST_UPDATED Timestamp(6),
  LAST_UPDATED_BY Varchar2(90 ),
  CONSTRAINT COMM_SOURCE_NN CHECK ((DOCNAME IS NOT NULL OR INTERACTION_ID IS NOT NULL))
)
/

ALTER TABLE COMMUNICATION ADD CONSTRAINT COMMUNICATION_PK PRIMARY KEY (COMM_ID)
/

COMMENT ON TABLE COMMUNICATION IS 'Stores all transactions/instructions/supporting doument data, with state tracking.'
/

COMMENT ON COLUMN COMMUNICATION.COMM_TYPE IS 'Currently refers to entries in the UCMADMIN.DOCUMENT_CLASSES.DOC_CLASS column'
/

COMMENT ON COLUMN COMMUNICATION.DEST_ACCOUNT_ID IS 'Must be specified for transfer instructions'
/



-- Table REF_COMM_STATUS

CREATE TABLE REF_COMM_STATUS(
  COMM_STATUS_ID Number(15,0) CONSTRAINT REFCOMMSTATUSID_NN NOT NULL,
  STATUS_NAME Varchar2(50 ) CONSTRAINT REFCOMMSTATUS_NAME_NN NOT NULL
)
/

-- Add keys for table REF_COMM_STATUS

ALTER TABLE REF_COMM_STATUS ADD CONSTRAINT REF_COMM_STATUS_PK PRIMARY KEY (COMM_STATUS_ID)
/

ALTER TABLE REF_COMM_STATUS ADD CONSTRAINT REFCOMMSTATUS_NAME_UQ UNIQUE (STATUS_NAME)
/



CREATE TABLE TRANSACTION(
  COMM_ID Number(15,0) CONSTRAINT TRANSACTION_COMMID_NN NOT NULL
)
/

ALTER TABLE TRANSACTION ADD CONSTRAINT TRANSACTION_PK PRIMARY KEY (COMM_ID)
/

COMMENT ON TABLE TRANSACTION IS 'A list of all Communication entries which are determined to be valid, immutable transactions. Income distribution and other monetary calculations will refer to the entries here.'
/



CREATE TABLE COMM_AURORA_BATCHING(
  COMM_ID Number(15,0),
  BATCH_ID Varchar2(50 ) CONSTRAINT COMMAURORABATCHING_BATCHID_NN NOT NULL,
  SUBMITTED_FOR_AUTH Number(1,0),
  BATCH_ERRORS Varchar2(50 ),
  AUTH_STATUS Varchar2(50 )
)
/

COMMENT ON TABLE COMM_AURORA_BATCHING IS 'Tracks Aurora batching state of Communication entries which are submitted for STP. 

All fields are supplied by Aurora web service responses.'
/



CREATE TABLE REF_SHARE_CLASS(
  SHARE_CLASS_ID Number(15,0) CONSTRAINT REFSHARECLASSID_NN NOT NULL,
  SHARE_CLASS_NAME Varchar2(20 ) CONSTRAINT REFSHARECLASS_NAME_NN NOT NULL,
  FUND_CODE Varchar2(2 CHAR) CONSTRAINT REFSHARECLASS_FUNDCODE_NN NOT NULL,
  DESCRIPTION Varchar2(500 ),
  IS_ENABLED Number(1,0) CONSTRAINT REFSHARECLASS_ENABLED_NN NOT NULL,
  MIN_THRESHOLD Number(15,10),
  CUSTOM_ELIGIBILITY Number(1,0) CONSTRAINT REFSHARECLASS_CUSTELIG_NN NOT NULL,
  MANAGEMENT_RATE Number(15,10) CONSTRAINT REFSHARECLASS_MGMTRATE_NN NOT NULL,
  INITIAL_CHARGE Number(15,10) CONSTRAINT REFSHARECLASS_INITCHRGE_NN NOT NULL,
  EXIT_CHARGE Number(15,10) CONSTRAINT REFSHARECLASS_EXITCHRGE_NN NOT NULL,
  DATE_ADDED Timestamp(6) CONSTRAINT REFSHARECLASS_DATEADDED_NN NOT NULL,
  LAST_UPDATED Timestamp(6)
)
/

ALTER TABLE REF_SHARE_CLASS ADD CONSTRAINT REF_SHARE_CLASS_PK PRIMARY KEY (SHARE_CLASS_ID)
/




CREATE TABLE SHARE_CLASS_OVERRIDE(
  ACCOUNT_ID Number(15,0),
  OVERRIDE_USER_ID Varchar2(90 ) CONSTRAINT SCOVERRIDE_USERID_NN NOT NULL,
  OVERRIDE_DATE Timestamp(6) CONSTRAINT SCOVERRIDE_DATE_NN NOT NULL,
  OVERRIDE_REASON Varchar2(500 ) CONSTRAINT SCOVERRIDE_REASON_NN NOT NULL
)
/

COMMENT ON TABLE SHARE_CLASS_OVERRIDE IS 'Lists Accounts which will not have their share classes updated automatically - their share class value is ''frozen'' while a corresponding entry exists in this table.'
/



CREATE TABLE SHARE_CLASS_MOVEMENT(
  ACCOUNT_ID Number(15,0) CONSTRAINT SCMOVEMENT_ACCOUNTID_NN NOT NULL,
  MOVE_DATE Timestamp(6) CONSTRAINT SCMOVEMENT_DATE_NN NOT NULL,
  MOVE_TYPE Varchar2(30 ) CONSTRAINT SCMOVEMENT_TYPE_NN NOT NULL,
  SHARE_CLASS_ID Number(15,0) CONSTRAINT SCMOVEMENT_SHARECLASSID_NN NOT NULL,
  OLD_SHARE_CLASS_ID Number(15,0)
)
/

COMMENT ON TABLE SHARE_CLASS_MOVEMENT IS 'Audits movements in account share classes'
/



CREATE TABLE REF_INCOME_EXPENSE(
  INCOME_EXPENSE_ID Number(15,0) NOT NULL,
  FUND_CODE Varchar2(2 CHAR),
  SHARE_CLASS_ID Number(15,0),
  INCOME_EXPENSE_TYPE_ID Number(15,0) CONSTRAINT REFINCOMEEXP_TYPE_NN NOT NULL,
  INCOME_EXPENSE_NAME Varchar2(80 ) CONSTRAINT INCOMEEXP_NAME_NN NOT NULL,
  DESCRIPTION Varchar2(500 ),
  FREQUENCY_ID Char(20 ),
  IS_PERSIST Number(1,0) CONSTRAINT INCOMEEXP_PERSIST_NN NOT NULL,
  DEFAULT_VALUE Number(15,10),
  DATE_ADDED Timestamp(6) CONSTRAINT INCOMEEXP_DATEADDED_NN NOT NULL,
  LAST_UPDATED Timestamp(6),
  USER_ID Varchar2(90 )
)
/

ALTER TABLE REF_INCOME_EXPENSE ADD CONSTRAINT REF_INCOME_EXPENSE_PK PRIMARY KEY (INCOME_EXPENSE_ID)
/



CREATE TABLE REF_INCOME_EXPENSE_TYPE(
  INCOME_EXPENSE_TYPE_ID Number(15,0) CONSTRAINT INCOMEEXP_ID_NN NOT NULL,
  INCOME_EXPENSE_TYPE_NAME Varchar2(50 ) CONSTRAINT REFINCOMEEXPTYPE_NAME_NN NOT NULL,
  IS_INCOME Number(1,0) CONSTRAINT REFINCOMEEXPTYPE_INCOME_NN NOT NULL
)
/

ALTER TABLE REF_INCOME_EXPENSE_TYPE ADD CONSTRAINT REF_INCOME_EXPENSE_TYPE_PK PRIMARY KEY (INCOME_EXPENSE_TYPE_ID)
/

COMMENT ON TABLE REF_INCOME_EXPENSE_TYPE IS 'Grouping label for income/expense entries in REF_INCOME_EXPENSE_TYPE'
/

COMMENT ON COLUMN REF_INCOME_EXPENSE_TYPE.IS_INCOME IS 'Determines whether this type is an Income or Expense. True = Income, False = Expense.'
/

CREATE INDEX IX_REFCOMMSTATUS_COMMN_FK ON COMMUNICATION (COMM_STATUS_ID) 
/
ALTER TABLE COMMUNICATION ADD CONSTRAINT REFCOMMSTATUS_COMM_FK FOREIGN KEY (COMM_STATUS_ID) REFERENCES REF_COMM_STATUS (COMM_STATUS_ID)
/
CREATE INDEX IX_ACCOUNT_COMM_FK ON COMMUNICATION (SOURCE_ACCOUNT_ID) 
/
ALTER TABLE COMMUNICATION ADD CONSTRAINT ACC_COMM_SOURCEACC_FK FOREIGN KEY (SOURCE_ACCOUNT_ID) REFERENCES ACCOUNT (ACCOUNT_ID)
/
CREATE INDEX IX_ACCOUNT_DEST_COMM_FK ON COMMUNICATION (DEST_ACCOUNT_ID) 
/
ALTER TABLE COMMUNICATION ADD CONSTRAINT ACC_COMM_DESTACC_FK FOREIGN KEY (DEST_ACCOUNT_ID) REFERENCES ACCOUNT (ACCOUNT_ID)
/
CREATE INDEX IX_ORG_COMM_FK ON COMMUNICATION (ORGANISATION_ID) 
/
ALTER TABLE COMMUNICATION ADD CONSTRAINT ORG_COMM_FK FOREIGN KEY (ORGANISATION_ID) REFERENCES ORGANISATION (ORGANISATION_ID)
/
CREATE INDEX IX_PERSON_COMM_FK ON COMMUNICATION (PERSON_ID) 
/
ALTER TABLE COMMUNICATION ADD CONSTRAINT PERSON_COMM_FK FOREIGN KEY (PERSON_ID) REFERENCES PERSON (PERSON_ID)
/
CREATE INDEX IX_BANKACCOUNT_COMM_FK ON COMMUNICATION (SOURCE_BANK_ACCOUNT_ID) 
/
ALTER TABLE COMMUNICATION ADD CONSTRAINT BANKACC_COMM_SRCBANKACC_FK FOREIGN KEY (SOURCE_BANK_ACCOUNT_ID) REFERENCES BANK_ACCOUNT (BANK_ACCOUNT_ID)
/
CREATE INDEX IX_BANKACCOUNT_DEST_COMM_FK ON COMMUNICATION (DEST_BANK_ACCOUNT_ID) 
/
ALTER TABLE COMMUNICATION ADD CONSTRAINT BANKACC_COMM_DESTBANKACC_FK FOREIGN KEY (DEST_BANK_ACCOUNT_ID) REFERENCES BANK_ACCOUNT (BANK_ACCOUNT_ID)
/
CREATE INDEX IX_COMM_DEPCOMM_FK ON COMMUNICATION (DEPENDENT_COMM_ID) 
/
ALTER TABLE COMMUNICATION ADD CONSTRAINT COMM_DEPCOMM_FK FOREIGN KEY (DEPENDENT_COMM_ID) REFERENCES COMMUNICATION (COMM_ID)
/
CREATE INDEX IX_COMM_TRANSACTION_FK ON TRANSACTION (COMM_ID) 
/
ALTER TABLE TRANSACTION ADD CONSTRAINT COMM_TRANSACTION_FK FOREIGN KEY (COMM_ID) REFERENCES COMMUNICATION (COMM_ID)
/
CREATE INDEX IX_COMM_COMMAURORABATCHING_FK ON COMM_AURORA_BATCHING (COMM_ID) 
/
ALTER TABLE COMM_AURORA_BATCHING ADD CONSTRAINT COMM_COMMAURORABATCHING_FK FOREIGN KEY (COMM_ID) REFERENCES COMMUNICATION (COMM_ID)
/
CREATE INDEX IX_FUND_REF_SHARE_CLASS_FK ON REF_SHARE_CLASS (FUND_CODE) 
/
ALTER TABLE REF_SHARE_CLASS ADD CONSTRAINT FUND_REFSHARECLASS_FK FOREIGN KEY (FUND_CODE) REFERENCES FUND (FUND_CODE)
/

CREATE INDEX IX_REFSHARECLASS_ACCOUNT_FK ON ACCOUNT (SHARE_CLASS) 
/

ALTER TABLE ACCOUNT ADD CONSTRAINT REFSHARECLASS_ACCOUNT_FK FOREIGN KEY (SHARE_CLASS) REFERENCES REF_SHARE_CLASS (SHARE_CLASS_ID)
/
CREATE INDEX IX_ACCOUNT_SHARE_CLASS_OVERRIDE_FK ON SHARE_CLASS_OVERRIDE (ACCOUNT_ID) 
/
ALTER TABLE SHARE_CLASS_OVERRIDE ADD CONSTRAINT ACCOUNT_SHARE_CLASS_OVERRIDE_FK FOREIGN KEY (ACCOUNT_ID) REFERENCES ACCOUNT (ACCOUNT_ID)
/
CREATE INDEX IX_REF_SHARE_CLASS_SHARE_CLASS_MOVEMENT_FK ON SHARE_CLASS_MOVEMENT (SHARE_CLASS_ID) 
/
ALTER TABLE SHARE_CLASS_MOVEMENT ADD CONSTRAINT SHARECLASS_SCMOVEMENT_FK FOREIGN KEY (SHARE_CLASS_ID) REFERENCES REF_SHARE_CLASS (SHARE_CLASS_ID)
/
CREATE INDEX IX_REF_SHARE_CLASS_SHARE_CLASS_MOVEMENT_FK ON SHARE_CLASS_MOVEMENT (OLD_SHARE_CLASS_ID) 
/
ALTER TABLE SHARE_CLASS_MOVEMENT ADD CONSTRAINT SHARECLASS_SCMOVEMENT_OLD_FK FOREIGN KEY (OLD_SHARE_CLASS_ID) REFERENCES REF_SHARE_CLASS (SHARE_CLASS_ID)
/
CREATE INDEX IX_ACCOUNT_SHARE_CLASS_MOVEMENT_FK ON SHARE_CLASS_MOVEMENT (ACCOUNT_ID) 
/
ALTER TABLE SHARE_CLASS_MOVEMENT ADD CONSTRAINT ACCOUNT_SHARE_CLASS_MOVEMENT_FK FOREIGN KEY (ACCOUNT_ID) REFERENCES ACCOUNT (ACCOUNT_ID)
/
CREATE INDEX IX_REF_INCOME_EXPENSE_TYPE_REF_INCOME_EXPENSE_FK ON REF_INCOME_EXPENSE (INCOME_EXPENSE_TYPE_ID) 
/
ALTER TABLE REF_INCOME_EXPENSE ADD CONSTRAINT REFINCOMEEXPTYPE_REFINCOMEEXP_FK FOREIGN KEY (INCOME_EXPENSE_TYPE_ID) REFERENCES REF_INCOME_EXPENSE_TYPE (INCOME_EXPENSE_TYPE_ID)
/
CREATE INDEX IX_REF_SHARE_CLASS_REF_INCOME_EXPENSE_FK ON REF_INCOME_EXPENSE (SHARE_CLASS_ID) 
/
ALTER TABLE REF_INCOME_EXPENSE ADD CONSTRAINT REFSHARECLASS_REFINCOMEEXP_FK FOREIGN KEY (SHARE_CLASS_ID) REFERENCES REF_SHARE_CLASS (SHARE_CLASS_ID)
/
CREATE INDEX IX_FUND_REF_INCOME_EXPENSE_FK ON REF_INCOME_EXPENSE (FUND_CODE) 
/
ALTER TABLE REF_INCOME_EXPENSE ADD CONSTRAINT FUND_REFINCOMEEXP_FK FOREIGN KEY (FUND_CODE) REFERENCES FUND (FUND_CODE)
/

/* Synonyms */
CREATE OR REPLACE PUBLIC SYNONYM REF_SHARE_CLASS FOR CCLA.REF_SHARE_CLASS;
CREATE OR REPLACE PUBLIC SYNONYM SHARE_CLASS_OVERRIDE FOR CCLA.SHARE_CLASS_OVERRIDE;
CREATE OR REPLACE PUBLIC SYNONYM REF_INCOME_EXPENSE FOR CCLA.REF_INCOME_EXPENSE;
CREATE OR REPLACE PUBLIC SYNONYM REF_INCOME_EXPENSE_TYPE FOR CCLA.REF_INCOME_EXPENSE_TYPE;
CREATE OR REPLACE PUBLIC SYNONYM COMMUNICATION FOR CCLA.COMMUNICATION;
CREATE OR REPLACE PUBLIC SYNONYM "TRANSACTION" FOR CCLA.TRANSACTION;
CREATE OR REPLACE PUBLIC SYNONYM COMM_AURORA_BATCHING FOR CCLA.COMM_AURORA_BATCHING;
CREATE OR REPLACE PUBLIC SYNONYM SHARE_CLASS_MOVEMENT FOR CCLA.SHARE_CLASS_MOVEMENT;

/* Grants */
GRANT ALL ON REF_SHARE_CLASS TO UCMADMIN;
GRANT ALL ON SHARE_CLASS_OVERRIDE TO UCMADMIN;
GRANT ALL ON REF_INCOME_EXPENSE TO UCMADMIN;
GRANT ALL ON REF_INCOME_EXPENSE_TYPE TO UCMADMIN;
GRANT ALL ON COMMUNICATION TO UCMADMIN;
GRANT ALL ON "TRANSACTION" TO UCMADMIN;
GRANT ALL ON "COMM_AURORA_BATCHING" TO UCMADMIN;
GRANT ALL ON SHARE_CLASS_MOVEMENT TO UCMADMIN;

/* sequences (AS UCMADMIN USER*/
CREATE SEQUENCE  SEQ_INCOME_EXPENSE_ID MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER NOCYCLE;
CREATE SEQUENCE  SEQ_SHARE_CLASS_MOVE_ID MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER NOCYCLE;
CREATE SEQUENCE  SEQ_INCOME_EXPENSE_APPLIED_ID MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER NOCYCLE;
  
