CREATE OR REPLACE FORCE VIEW V_INSTRUCTIONS_EXTENDED (
  INSTRUCTION_ID,
  comm_id,
  comm_source_id,
  comm_source_name,
  date_comm_added,
  created_by,
  organisation_id,
  ORGANISATION_NAME,
  CLIENT_NUMBER,
  PADDED_CLIENT_NUMBER,
  COMPANY_CODE,
  comm_group_id,
  ucm_batch_ref,
  spp_batch_ref,
  INSTRUCTION_STATUS_ID,
  INSTRUCTION_STATUS_NAME,
  PROCESS_DATE,
  ACTUAL_PROCESS_DATE,
  INSTRUCTION_TYPE_ID,
  SPP_JOB_ID,
  LAST_UPDATED,
  DATE_ADDED,
  LAST_UPDATED_BY,
  DEPENDENT_INSTRUCTION_ID,
  instruction_doc_id,
  priority,
  INSTRUCTION_TYPE_NAME,
  IS_FINANCIAL_TRANSACTION,
  is_suspended
) AS
SELECT 
  ins.INSTRUCTION_ID,
  com.comm_id,
  com.comm_source_id,
  src.comm_source_name,
  com.date_added AS DATE_COMM_ADDED,
  com.created_by,
  com.organisation_id,
  org.ORGANISATION_NAME,
  cli.CLIENT_NUMBER,
  CASE
    WHEN (cli.CLIENT_NUMBER IS NULL) THEN NULL
    WHEN (cli.CLIENT_NUMBER >= 100000) THEN (TO_CHAR(cli.CLIENT_NUMBER))
    ELSE LPAD(cli.CLIENT_NUMBER, 5, '0') 
    END AS PADDED_CLIENT_NUMBER,
  cmp.COMPANY_CODE,
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
  INNER JOIN COMM com ON (ins.COMM_ID = com.COMM_ID)
  INNER JOIN REF_COMM_SOURCE src ON (com.COMM_SOURCE_ID = src.COMM_SOURCE_ID)
  INNER JOIN COMM_GROUP grp ON (com.COMM_GROUP_ID = grp.COMM_GROUP_ID)
  INNER JOIN ref_instruction_type typ ON (typ.instruction_type_id = ins.instruction_type_id)
  INNER JOIN ref_instruction_status ist ON (ins.instruction_status_id = ist.instruction_status_id)
  
  LEFT JOIN ORGANISATION org ON (com.ORGANISATION_ID = org.ORGANISATION_ID)
  LEFT JOIN CLIENT_AURORA_MAP cli ON (org.ORGANISATION_ID = cli.ORGANISATION_ID)
  LEFT JOIN REF_COMPANY cmp ON (cli.COMPANY_ID = cmp.COMPANY_ID)
  
  LEFT JOIN INSTRUCTION_LOCK lok ON (ins.INSTRUCTION_ID = lok.INSTRUCTION_ID);

CREATE OR REPLACE PUBLIC SYNONYM V_INSTRUCTIONS_EXTENDED FOR CCLA.V_INSTRUCTIONS_EXTENDED;
GRANT SELECT, REFERENCES ON V_INSTRUCTIONS_EXTENDED TO UCMADMIN;


CREATE OR REPLACE FORCE VIEW V_INSTRUCTION_VALUES
(
    INSTRUCTION_ID,
    instruction_data_name,
    instruction_data_label,
    instruction_data_type,
    APPLICABLE_INSTRUCTION_DATA_ID,
    INSTRUCTION_VALUE,
    INSTRUCTION_STRING_VALUE,
    INSTRUCTION_NUM_VALUE,
    INSTRUCTION_DATE_VALUE,
    is_optional 
)
AS
  SELECT 
    ida.INSTRUCTION_ID,
    rid.instruction_data_name,
    rid.instruction_data_label,
    rid.instruction_data_type,
    ida.APPLICABLE_INSTRUCTION_DATA_ID,
    ida.INSTRUCTION_VALUE,
    ida.INSTRUCTION_STRING_VALUE,
    ida.INSTRUCTION_NUM_VALUE,
    ida.INSTRUCTION_DATE_VALUE,
    aid.is_optional
  FROM REF_INSTRUCTION_DATA rid
  LEFT JOIN APPLICABLE_INSTRUCTION_DATA aid
  ON (rid.instruction_data_id = aid.instruction_data_id)
  LEFT JOIN REF_INSTRUCTION_TYPE rit
  ON (rit.instruction_type_id = aid.instruction_type_id)
  INNER JOIN INSTRUCTION ins
  ON (ins.instruction_type_id = aid.instruction_type_id)
  INNER JOIN instruction_data_applied ida
  ON (ida.applicable_instruction_data_id = aid.applicable_instruction_data_id
  AND ins.instruction_id                 = ida.instruction_id);
  
CREATE OR REPLACE PUBLIC SYNONYM V_INSTRUCTION_VALUES FOR CCLA.V_INSTRUCTION_VALUES;
GRANT SELECT, REFERENCES ON V_INSTRUCTION_VALUES TO UCMADMIN;