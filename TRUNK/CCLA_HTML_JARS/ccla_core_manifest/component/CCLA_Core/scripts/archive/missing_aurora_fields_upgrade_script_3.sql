-- Nullify new CLIENT_AURORA_MAP fields
UPDATE CLIENT_AURORA_MAP 
SET CONTRIBUTER_TYPE_CODE = NULL, SUBDIVISION_CODE = NULL, CONTRIBUTER_CODE = NULL,
STATEMENTS_MONTH_ALL = NULL,
STATEMENTS_MONTH_1 = NULL, STATEMENTS_MONTH_2 = NULL, STATEMENTS_MONTH_3 = NULL, STATEMENTS_MONTH_4 = NULL,
INCOME_DIST_REPORT_IND = NULL,
INCOME_DIST_REPORT_PAPER_IND = NULL,
DEPOSIT_NO_OF_STATEMENTS = NULL,
UNITISED_NO_OF_STATEMENTS = NULL,
BULK_DEPOSIT_STATEMENT_IND = NULL,
DEPOSIT_WITHDRAW_BOOKS_IND = NULL,
AUTO_STATIONARY_ORDERED_IND = NULL,
QUARTERLY_RPT_CLIENT_IND = NULL,
QUARTERLY_RPT_MANAGER_INITS = NULL,
MARKETING_GROUP_ID = NULL,
MARKETING_SUBGROUP_ID = NULL,
ACCOUNT_GROUP_ID = NULL;

-- Nullify new PERSON_AURORA_MAP fields
UPDATE PERSON_AURORA_MAP
SET REPORT_USAGE = NULL, REPORT_MAIL_INDICATOR = NULL, REPORT_EMAIL_INDICATOR = NULL;

-- New bits added 01/12/2011
INSERT INTO REF_INSTRUCTION_STATUS VALUES 
(32, 'Terminated', 'Instruction manually cancelled or terminated by user before being executed');

INSERT INTO REF_INSTR_AUDIT_ACTION VALUES
(15, 'Terminated', 'Instruction manually terminated');

INSERT INTO REF_INSTRUCTION_ACTION VALUES (8, 'Terminate');

INSERT INTO REF_INSTRUCTION_PROCESS VALUES (3, 'SDU Aurora Execution Failure', 'SDU Instruction failed to execute successfully in Aurora', null, null);

INSERT INTO INSTR_PROCESS_ACTION_APPLIED VALUES (3, 1, 'Retry execution', 'Retries execution in Aurora');
INSERT INTO INSTR_PROCESS_ACTION_APPLIED VALUES (3, 3, 'Mark as failed', 'Set instruction to failed status');
INSERT INTO INSTR_PROCESS_ACTION_APPLIED VALUES (3, 8, 'Terminate', 'Terminate instruction');

-- Fix REPORT_USAGE constraint
ALTER TABLE PERSON_AURORA_MAP DROP CONSTRAINT CK_PAM_REPORTUSAGE;
ALTER TABLE PERSON_AURORA_MAP ADD CONSTRAINT CK_PAM_REPORTUSAGE CHECK (REPORT_USAGE BETWEEN 0 AND 99);

-- Add ORG_ID as a required field for CREATE/UPDATE_ACCOUNT instruction type
INSERT INTO APPLICABLE_INSTRUCTION_DATA VALUES (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 9, 130, 0);
INSERT INTO APPLICABLE_INSTRUCTION_DATA VALUES (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 9, 131, 0);

-- Add USER_ID column to view
CREATE OR REPLACE FORCE VIEW "CCLA"."V_INSTRUCTION_AUDIT" 
("INSTRUCTION_AUDIT_ID", "INSTRUCTION_ID", "INSTRUCTION_TYPE_ID", "INSTRUCTION_TYPE_NAME", "INSTRUCTION_STATUS_ID", "INSTRUCTION_STATUS_NAME", "MODULE_ID", "MODULE_NAME", "INSTR_AUDIT_ACTION_ID", "INSTR_AUDIT_ACTION_NAME", "INSTRUCTION_ACTION_ID", "INSTRUCTION_ACTION_LABEL", 
"USER_ID", "AUDIT_DATE")
AS
  SELECT ia.instruction_audit_id,
    ia.instruction_id,
    rit.instruction_type_id,
    rit.instruction_type_name,
    ris.instruction_status_id,
    ris.instruction_status_name,
    irm.module_id,
    irm.module_name,
    riaa.instr_audit_action_id,
    riaa.instr_audit_action_name,
    ia.instruction_action_id,
    ria.instruction_action_label,
    ia.user_id,
    ia.audit_date
  FROM instruction_audit ia
  INNER JOIN instruction i
  ON (ia.instruction_id = i.instruction_id)
  LEFT JOIN ref_instruction_type rit
  ON (i.instruction_type_id = rit.instruction_type_id)
  INNER JOIN ref_instr_audit_action riaa
  ON (ia.instr_audit_action_id = riaa.instr_audit_action_id)
  LEFT JOIN instruction_routing_module irm
  ON (ia.module_id = irm.module_id)
  LEFT JOIN ref_instruction_status ris
  ON (ia.instruction_status_id = ris.instruction_status_id)
  LEFT JOIN ref_instruction_action ria
  ON (ia.instruction_action_id = ria.instruction_action_id)
  ORDER BY ia.audit_date DESC;
  
  
-- New query for use on Instruction Listing screen
CREATE OR REPLACE FORCE VIEW "CCLA"."V_INSTRUCTIONS_LISTED" 
("INSTRUCTION_ID", "COMM_ID", "COMM_SOURCE_ID", "COMM_SOURCE_NAME", "INTERACTION_ID", 
"COMM_TYPE_NAME", "DATE_COMM_ADDED", "CREATED_BY", "ORGANISATION_ID", "ORGANISATION_NAME", "CLIENT_NUMBER", "PADDED_CLIENT_NUMBER", "COMPANY_CODE", 
"ACCOUNT_ID", "FUND_CODE", "ACCOUNTNUMBER", "COMM_GROUP_ID", "UCM_BATCH_REF", "SPP_BATCH_REF", "INSTRUCTION_STATUS_ID", "INSTRUCTION_STATUS_NAME", 
"PROCESS_DATE", "ACTUAL_PROCESS_DATE", "INSTRUCTION_TYPE_ID", "SPP_JOB_ID", "LAST_UPDATED", "DATE_ADDED", "LAST_UPDATED_BY", 
"DEPENDENT_INSTRUCTION_ID", "INSTRUCTION_DOC_ID", "PRIORITY", "INSTRUCTION_TYPE_NAME", "IS_FINANCIAL_TRANSACTION", "IS_SUSPENDED")
AS
SELECT ins.INSTRUCTION_ID,
    com.comm_id,
    com.comm_source_id,
    src.comm_source_name,
    com.interaction_id,
    rct.comm_type_name,
    com.date_added AS DATE_COMM_ADDED,
    com.created_by,
    org.organisation_id,
    org.ORGANISATION_NAME,
    cli.CLIENT_NUMBER,
    CASE
      WHEN (cli.CLIENT_NUMBER IS NULL)
      THEN NULL
      WHEN (cli.CLIENT_NUMBER >= 100000)
      THEN (TO_CHAR(cli.CLIENT_NUMBER))
      ELSE LPAD(cli.CLIENT_NUMBER, 5, '0')
    END AS PADDED_CLIENT_NUMBER,
    cmp.COMPANY_CODE,
    acc.ACCOUNT_ID,
    acc.FUND_CODE,
    acc.ACCOUNTNUMBER,
    grp.comm_group_id,
    grp.ucm_batch_ref,
    grp.spp_batch_ref,
    ins.INSTRUCTION_STATUS_ID,
    ist.INSTRUCTION_STATUS_NAME,
    ins.PROCESS_DATE,
    ins.ACTUAL_PROCESS_DATE,
    ins.INSTRUCTION_TYPE_ID,
    ins.SPP_JOB_ID,
    ins.LAST_UPDATED,
    ins.DATE_ADDED,
    ins.LAST_UPDATED_BY,
    ins.DEPENDENT_INSTRUCTION_ID,
    ins.instruction_doc_id,
    ins.priority,
    typ.INSTRUCTION_TYPE_NAME,
    typ.IS_FINANCIAL_TRANSACTION,
    
    lok.is_suspended
  FROM instruction ins
  INNER JOIN COMM com
  ON (ins.COMM_ID = com.COMM_ID)
  INNER JOIN REF_COMM_SOURCE src
  ON (com.COMM_SOURCE_ID = src.COMM_SOURCE_ID)
  INNER JOIN REF_COMM_TYPE rct
  ON (com.COMM_TYPE_ID = rct.COMM_TYPE_ID)
  INNER JOIN COMM_GROUP grp
  ON (com.COMM_GROUP_ID = grp.COMM_GROUP_ID)
  INNER JOIN ref_instruction_type typ
  ON (typ.instruction_type_id = ins.instruction_type_id)
  INNER JOIN ref_instruction_status ist
  ON (ins.instruction_status_id = ist.instruction_status_id)
    -- Company data value
  LEFT JOIN V_INSTRUCTION_DATA_VALUE idv_comp
  ON (ins.INSTRUCTION_ID           = idv_comp.INSTRUCTION_ID
  AND idv_comp.INSTRUCTION_DATA_ID = 50)
  LEFT JOIN REF_COMPANY cmp
  ON (idv_comp.INSTRUCTION_NUM_VALUE = cmp.COMPANY_ID)
    -- Org data value
  LEFT JOIN V_INSTRUCTION_DATA_VALUE idv_org
  ON (ins.INSTRUCTION_ID          = idv_org.INSTRUCTION_ID
  AND idv_org.INSTRUCTION_DATA_ID = 9)
    -- Source Account data value
  LEFT JOIN V_INSTRUCTION_DATA_VALUE idv_acc
  ON (ins.INSTRUCTION_ID          = idv_acc.INSTRUCTION_ID
  AND idv_acc.INSTRUCTION_DATA_ID = 1)
  LEFT JOIN ACCOUNT acc
  ON (idv_acc.INSTRUCTION_NUM_VALUE = acc.ACCOUNT_ID)
  LEFT JOIN ORGANISATION org
  ON (org.ORGANISATION_ID = idv_org.INSTRUCTION_NUM_VALUE)
    -- Client Aurora Map. Group by CLIENT_NUMBER values to avoid duplicate rows
  LEFT JOIN
    (SELECT cli.CLIENT_NUMBER,
      cli.ORGANISATION_ID
    FROM CLIENT_AURORA_MAP cli
    GROUP BY cli.ORGANISATION_ID,
      cli.CLIENT_NUMBER
    ) cli
  ON (org.ORGANISATION_ID = cli.ORGANISATION_ID)
  LEFT JOIN INSTRUCTION_LOCK lok
  ON (ins.INSTRUCTION_ID = lok.INSTRUCTION_ID);

CREATE OR REPLACE PUBLIC SYNONYM V_INSTRUCTIONS_LISTED FOR CCLA.V_INSTRUCTIONS_LISTED;
GRANT SELECT ON V_INSTRUCTIONS_LISTED TO UCMADMIN;
  
  