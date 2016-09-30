/* Last minute changes to support required updates to Job Priority rules */

alter table JOB_PRIORITY_RULES drop constraint JOB_PRIORITY_RULES_UQ;
drop index JOB_PRIORITY_RULES_UQ;

alter table JOB_PRIORITY_RULES add constraint JOB_PRIORITY_RULES_UQ UNIQUE (RULE_FIELD,RULE_TYPE,APPLIES_TO);

UPDATE JOB_PRIORITY_RULES SET RULE_NAME = 'Scan User (EnterDetails only)' WHERE RULE_ID=2;
Insert into JOB_PRIORITY_RULES (RULE_ID,RULE_NAME,RULE_FIELD,RULE_TYPE,RULE_ORDER,IS_ENABLED,APPLIES_TO) values (20,'Scan User (both steps)','xScanUser','BundleData',1,1,'EnterDetails,Validation');

/* This line may fail if data has been imported into priority rule values table. */
INSERT INTO JOB_PRIORITY_RULE_VALUES(SEQ_JOB_PRIORITY_RULE_VALUE.NEXTVAL, 20, 'SCANUSER3', 3);

/* New flag reason (very last-minute addition!) */
INSERT INTO REF_ACCOUNT_FLAG VALUES (9, 'Relationships not confirmed', 1);

/* Kill off old auditing tables in UCMADMIN tablespace */
DROP TRIGGER CCLA_IDENTITY_CHECK_AUDIT_TRIG;

DROP TABLE CCLA_IDENTITY_CHECK_AUDIT_OLD;
DROP TABLE CCLA_IDENTITY_VAL_CHECK_OLD;
DROP TABLE CCLA_IDENTITY_CHECK_OLD;
DROP TABLE CCLA_IDENTITY_CHECK_LOOKUP_OLD;