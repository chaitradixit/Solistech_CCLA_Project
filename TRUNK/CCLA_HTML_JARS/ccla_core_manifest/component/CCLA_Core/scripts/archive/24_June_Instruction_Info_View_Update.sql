CREATE OR REPLACE FORCE VIEW "CCLA"."V_INSTRUCTIONS_EXTENDED" ("INSTRUCTION_ID", "COMM_ID", "COMM_SOURCE_ID", "COMM_SOURCE_NAME", "INTERACTION_ID","COMM_TYPE_NAME", "DATE_COMM_ADDED", "CREATED_BY", "ORGANISATION_ID", "ORGANISATION_NAME", "CLIENT_NUMBER", "PADDED_CLIENT_NUMBER", "COMPANY_CODE", "COMM_GROUP_ID", "UCM_BATCH_REF", "SPP_BATCH_REF", "INSTRUCTION_STATUS_ID", "INSTRUCTION_STATUS_NAME", "PROCESS_DATE", "ACTUAL_PROCESS_DATE", "INSTRUCTION_TYPE_ID", "SPP_JOB_ID", "LAST_UPDATED", "DATE_ADDED", "LAST_UPDATED_BY", "DEPENDENT_INSTRUCTION_ID", "INSTRUCTION_DOC_ID", "PRIORITY", "INSTRUCTION_TYPE_NAME", "IS_FINANCIAL_TRANSACTION", "IS_SUSPENDED", "DOC_NAME", "DOC_TYPE")
AS
  SELECT ins.INSTRUCTION_ID,
    com.comm_id,
    com.comm_source_id,
    src.comm_source_name,
    com.interaction_id,
    rct.comm_type_name,
    com.date_added AS DATE_COMM_ADDED,
    com.created_by,
    com.organisation_id,
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
    lok.is_suspended,
    r.dDocName,
    r.dDocType
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
  LEFT JOIN ORGANISATION org
  ON (com.ORGANISATION_ID = org.ORGANISATION_ID)
  LEFT JOIN CLIENT_AURORA_MAP cli
  ON (org.ORGANISATION_ID = cli.ORGANISATION_ID)
  LEFT JOIN REF_COMPANY cmp
  ON (cli.COMPANY_ID = cmp.COMPANY_ID)
  LEFT JOIN INSTRUCTION_LOCK lok
  ON (ins.INSTRUCTION_ID = lok.INSTRUCTION_ID)
  LEFT JOIN UCMADMIN.REVISIONS r
  ON (ins.INSTRUCTION_DOC_ID = r.dID);
  
  