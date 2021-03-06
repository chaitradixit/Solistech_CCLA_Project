-- Rename Views
RENAME CCLA_PROCESS_LAST_ACTIVITY TO V_PROCESS_LAST_ACTIVITY;
RENAME CCLA_PROCESS_ACTIVITY TO V_PROCESS_ACTIVITY;
RENAME CCLA_INTERACTIONS_EXT TO V_INTERACTIONS_EXT;
RENAME CCLA_FLAGGED_BUNDLES TO V_FLAGGED_BUNDLES;
RENAME CCLA_DOCUMENT_STATUS TO V_DOCUMENT_STATUS;
RENAME CCLA_DOCUMENT_STATUS_WF TO V_DOCUMENT_STATUS_WF;
RENAME CCLA_CLIENTS_AML_CAMPAIGN TO V_CLIENTS_AML_CAMPAIGN;
RENAME CCLA_ACTIVITY_NOTE TO V_ACTIVITY_NOTE;

DROP VIEW ACCOUNT_EXTENDED_CLIENT;
DROP VIEW ACCOUNT_EXTENDED;

CREATE OR REPLACE FORCE VIEW "CCLA"."V_ACCOUNT_EXTENDED_CLIENT" ("ACCOUNT_ID", "MANDATED_ACCOUNT_ID", "INCOME_DISTRIBUTION_METHOD", "FUND_CODE", "ACCOUNT_STATUS_ID", "AURORA_ACCOUNT", "ACCOUNTNUMBER", "ACCNUMEXT", "SUBTITLE", "DATE_OPENED", "SHARE_CLASS_ID", "REQ_SIGNATURES", "IS_EXCLUSIVE", "AML_CHECK_OVERRIDE_USER", "LAST_UPDATED", "ACCOUNT_TYPE_ID", "LOAN_TYPE_ID", "LOAN_TERM", "ACCOUNT_TYPE_NAME", "ACCOUNT_STATUS_NAME", "SHORT_NAME", "IDM_NAME", "ACC_CASH", "ACC_UNITS", "DATE_LAST_REFRESH", "ORGANISATION_ID", "ORG_ACCOUNT_CODE", "ORGANISATION_NAME", "CLIENT_NUMBER")
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
	
-- Create SYNONYM	
CREATE OR REPLACE PUBLIC SYNONYM V_ACCOUNT_EXTENDED_CLIENT FOR CCLA.V_ACCOUNT_EXTENDED_CLIENT;

-- Grant Select Account to views from UCM
GRANT SELECT ON V_ACCOUNT_EXTENDED_CLIENT TO UCMADMIN;