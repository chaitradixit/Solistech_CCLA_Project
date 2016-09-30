-- Changed linking mechanic used for joining to Client Aurora Map.


CREATE OR REPLACE FORCE VIEW "CCLA"."V_ACCOUNT_EXTENDED_CLIENT" ("ACCOUNT_ID", "MANDATED_ACCOUNT_ID", "INCOME_DISTRIBUTION_METHOD", "FUND_CODE", "ACCOUNT_STATUS_ID", "AURORA_ACCOUNT", "ACCOUNTNUMBER", "ACCNUMEXT", "SUBTITLE", "DATE_OPENED", "SHARE_CLASS_ID", "REQ_SIGNATURES", "IS_EXCLUSIVE", "AML_CHECK_OVERRIDE_USER", "LAST_UPDATED", "ACCOUNT_TYPE_ID", "LOAN_TYPE_ID", "LOAN_TERM", "AGREEMENT_TYPE_ID", "ACC_ACCOUNT_CODE", "ACCOUNT_TYPE_NAME", "ACCOUNT_STATUS_NAME", "SHORT_NAME", "IDM_NAME", "ACC_CASH", "ACC_UNITS", "DATE_LAST_REFRESH", "ORGANISATION_ID", "ORG_ACCOUNT_CODE", "ORGANISATION_NAME", "CLIENT_NUMBER") AS 
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
  INNER JOIN FUND f ON (acc.FUND_CODE = f.FUND_CODE)
  
  -- Client Aurora Mapping. Join on Org AND Company ID values to avoid duplicate rows
  LEFT JOIN (
    SELECT cli.CLIENT_NUMBER, cli.ORGANISATION_ID, cli.COMPANY_ID
    FROM CLIENT_AURORA_MAP cli
  ) cli ON (org.ORGANISATION_ID = cli.ORGANISATION_ID AND cli.COMPANY_ID = f.COMPANY_ID)
  
  ORDER BY org.ORGANISATION_ID,
    ACC.FUND_CODE,
    acc.accountnumber;
 
  -- Link to Aurora Client Table via Org ID AND Company
  CREATE OR REPLACE FORCE VIEW "CCLA"."V_INSTRUCTIONS_EXTENDED" ("INSTRUCTION_ID", "COMM_ID", "COMM_SOURCE_ID", "COMM_SOURCE_NAME", "INTERACTION_ID", "COMM_TYPE_NAME", "DATE_COMM_ADDED", "CREATED_BY", "ORGANISATION_ID", "ORG_ACCOUNT_CODE", "ORGANISATION_NAME", "CLIENT_NUMBER", "PADDED_CLIENT_NUMBER", "COMPANY_CODE", "ACCOUNT_ID", "FUND_CODE", "ACCOUNTNUMBER", "COMM_GROUP_ID", "UCM_BATCH_REF", "SPP_BATCH_REF", "INSTRUCTION_STATUS_ID", "INSTRUCTION_STATUS_NAME", "PROCESS_DATE", "ACTUAL_PROCESS_DATE", "INSTRUCTION_TYPE_ID", "SPP_JOB_ID", "LAST_UPDATED", "DATE_ADDED", "LAST_UPDATED_BY", "DEPENDENT_INSTRUCTION_ID", "INSTRUCTION_DOC_GUID", "PRIORITY", "INSTRUCTION_TYPE_NAME", "IS_FINANCIAL_TRANSACTION", "IS_SUSPENDED") AS 
  SELECT ins.INSTRUCTION_ID,
    com.comm_id,
    com.comm_source_id,
    src.comm_source_name,
    com.interaction_id,
    rct.comm_type_name,
    com.date_added AS DATE_COMM_ADDED,
    com.created_by,
    org.organisation_id,
    org.ORG_ACCOUNT_CODE,
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
    ins.INSTRUCTION_DOC_GUID,
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
    -- Org data value (may not always match the COMM's Org ID)
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
  ON (org.ORGANISATION_ID = DECODE(idv_org.INSTRUCTION_NUM_VALUE, NULL, com.ORGANISATION_ID, idv_org.INSTRUCTION_NUM_VALUE))
  
  -- Client Aurora Map. Join by Company value as well to prevent multiple rows
  -- being returned
  LEFT JOIN (
    SELECT cli.CLIENT_NUMBER, cli.ORGANISATION_ID, cli.COMPANY_ID
    FROM CLIENT_AURORA_MAP cli
  ) cli ON (org.ORGANISATION_ID = cli.ORGANISATION_ID AND cmp.COMPANY_ID = cli.COMPANY_ID)
  
  LEFT JOIN INSTRUCTION_LOCK lok
  ON (ins.INSTRUCTION_ID = lok.INSTRUCTION_ID);
  
  
  CREATE OR REPLACE FORCE VIEW "CCLA"."V_INSTRUCTIONS_LISTED" ("INSTRUCTION_ID", "COMM_ID", "COMM_SOURCE_ID", "COMM_SOURCE_NAME", "INTERACTION_ID", "COMM_TYPE_NAME", "DATE_COMM_ADDED", "CREATED_BY", "ORGANISATION_ID", "ORG_ACCOUNT_CODE", "ORGANISATION_NAME", "CLIENT_NUMBER", "PADDED_CLIENT_NUMBER", "COMPANY_CODE", "ACCOUNT_ID", "FUND_CODE", "ACCOUNTNUMBER", "COMM_GROUP_ID", "UCM_BATCH_REF", "SPP_BATCH_REF", "INSTRUCTION_STATUS_ID", "INSTRUCTION_STATUS_NAME", "PROCESS_DATE", "ACTUAL_PROCESS_DATE", "INSTRUCTION_TYPE_ID", "SPP_JOB_ID", "LAST_UPDATED", "DATE_ADDED", "LAST_UPDATED_BY", "DEPENDENT_INSTRUCTION_ID", "INSTRUCTION_DOC_GUID", "PRIORITY", "INSTRUCTION_TYPE_NAME", "IS_FINANCIAL_TRANSACTION", "IS_SUSPENDED") AS 
  SELECT ins.INSTRUCTION_ID,
    com.comm_id,
    com.comm_source_id,
    src.comm_source_name,
    com.interaction_id,
    rct.comm_type_name,
    com.date_added AS DATE_COMM_ADDED,
    com.created_by,
    org.organisation_id,
    org.ORG_ACCOUNT_CODE,
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
    ins.instruction_doc_guid,
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
  
  -- Client Aurora Map. Join by Company value as well to prevent multiple rows
  -- being returned
  LEFT JOIN (
    SELECT cli.CLIENT_NUMBER, cli.ORGANISATION_ID, cli.COMPANY_ID
    FROM CLIENT_AURORA_MAP cli
  ) cli ON (org.ORGANISATION_ID = cli.ORGANISATION_ID AND cmp.COMPANY_ID = cli.COMPANY_ID)

  LEFT JOIN INSTRUCTION_LOCK lok
  ON (ins.INSTRUCTION_ID = lok.INSTRUCTION_ID);
 


  CREATE OR REPLACE FORCE VIEW "CCLA"."V_INSTRUCTIONS_EXTENDED_DATA" ("INSTRUCTION_ID", "COMM_ID", "COMM_SOURCE_ID", "COMM_SOURCE_NAME", "INTERACTION_ID", "COMM_TYPE_NAME", "DATE_COMM_ADDED", "CREATED_BY", "ORGANISATION_ID", "ORG_ACCOUNT_CODE", "ORGANISATION_NAME", "CLIENT_NUMBER", "PADDED_CLIENT_NUMBER", "COMPANY_CODE", "COMM_GROUP_ID", "UCM_BATCH_REF", "SPP_BATCH_REF", "INSTRUCTION_STATUS_ID", "INSTRUCTION_STATUS_NAME", "PROCESS_DATE", "ACTUAL_PROCESS_DATE", "INSTRUCTION_TYPE_ID", "SPP_JOB_ID", "LAST_UPDATED", "DATE_ADDED", "LAST_UPDATED_BY", "DEPENDENT_INSTRUCTION_ID", "INSTRUCTION_DOC_GUID", "PRIORITY", "INSTRUCTION_TYPE_NAME", "IS_FINANCIAL_TRANSACTION", "IS_SUSPENDED", "CASH", "UNITS", "SOURCE_ACCOUNT_ID", "SOURCE_FUND_CODE", "SOURCE_ACCOUNTNUMBER", "SOURCE_ACCOUNTNUMBER_STRING") AS 
  SELECT ins.INSTRUCTION_ID,
    com.comm_id,
    com.comm_source_id,
    src.comm_source_name,
    com.interaction_id,
    rct.comm_type_name,
    com.date_added AS DATE_COMM_ADDED,
    com.created_by,
    org.organisation_id,
    org.ORG_ACCOUNT_CODE,
    org.ORGANISATION_NAME,
    cli.CLIENT_NUMBER,
    CASE
      WHEN (cli.CLIENT_NUMBER IS NULL)
      THEN NULL
      WHEN (cli.CLIENT_NUMBER >= 100000)
      THEN (TO_CHAR(cli.CLIENT_NUMBER))
      ELSE LPAD(cli.CLIENT_NUMBER, 5, '0')
    END                                                                            AS PADDED_CLIENT_NUMBER,
    DECODE(acc_cmp.COMPANY_CODE, NULL, org_cmp.COMPANY_CODE, acc_cmp.COMPANY_CODE) AS COMPANY_CODE,
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
    ins.INSTRUCTION_DOC_GUID,
    ins.priority,
    typ.INSTRUCTION_TYPE_NAME,
    typ.IS_FINANCIAL_TRANSACTION,
    lok.is_suspended,
    TO_CHAR(idv_cash.instruction_num_value, 'fm999G999G999G990d00')  AS CASH,
    TO_CHAR(idv_units.instruction_num_value, 'fm999G999G999G990d00') AS UNITS,
    srcaccount.ACCOUNT_ID                                            AS SOURCE_ACCOUNT_ID,
    srcaccount.FUND_CODE                                             AS SOURCE_FUND_CODE,
    srcaccount.ACCOUNTNUMBER                                         AS SOURCE_ACCOUNTNUMBER,
    CASE
      WHEN (acc_cmp.COMPANY_CODE IS NULL)
      THEN NULL
      WHEN (acc_cmp.COMPANY_CODE = 'CBF')
      THEN LPAD(srcaccount.ACCOUNTNUMBER, 3, '0')
        || srcaccount.FUND_CODE
      ELSE LPAD(srcaccount.ACCOUNTNUMBER, 4, '0')
        || srcaccount.FUND_CODE
    END AS SOURCE_ACCOUNTNUMBER_STRING
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
  LEFT JOIN INSTRUCTION_LOCK lok
  ON (ins.INSTRUCTION_ID = lok.INSTRUCTION_ID)
  LEFT JOIN V_INSTRUCTION_DATA_VALUE idv_org
  ON (idv_org.instruction_id      = ins.instruction_id
  AND idv_org.instruction_type_id = ins.instruction_type_id
  AND idv_org.instruction_data_id = 9)
  LEFT JOIN ORGANISATION org
  ON (idv_org.instruction_num_value = org.ORGANISATION_ID)
  
  LEFT JOIN V_INSTRUCTION_DATA_VALUE idv_comp
  ON (ins.INSTRUCTION_ID           = idv_comp.INSTRUCTION_ID
  AND idv_comp.INSTRUCTION_DATA_ID = 50)
  
  LEFT JOIN REF_COMPANY org_cmp
  ON (idv_comp.INSTRUCTION_NUM_VALUE = org_cmp.COMPANY_ID)
  
  LEFT JOIN CLIENT_AURORA_MAP cli
  ON (org.ORGANISATION_ID = cli.ORGANISATION_ID AND org_cmp.COMPANY_ID = cli.COMPANY_ID)

  LEFT JOIN V_INSTRUCTION_DATA_VALUE idv_cash
  ON (idv_cash.instruction_id      = ins.instruction_id
  AND idv_cash.instruction_type_id = ins.instruction_type_id
  AND idv_cash.instruction_data_id = 3)
  LEFT JOIN V_INSTRUCTION_DATA_VALUE idv_units
  ON (idv_units.instruction_id      = ins.instruction_id
  AND idv_units.instruction_type_id = ins.instruction_type_id
  AND idv_units.instruction_data_id = 4)
  LEFT JOIN V_INSTRUCTION_DATA_VALUE idv_srcaccount
  ON (idv_srcaccount.instruction_id      = ins.instruction_id
  AND idv_srcaccount.instruction_type_id = ins.instruction_type_id
  AND idv_srcaccount.instruction_data_id = 1)
  LEFT JOIN ACCOUNT srcaccount
  ON (idv_srcaccount.instruction_num_value = srcaccount.ACCOUNT_ID)
  LEFT JOIN FUND acc_fnd
  ON (srcaccount.FUND_CODE = acc_fnd.FUND_CODE)
  LEFT JOIN REF_COMPANY acc_cmp
  ON (acc_fnd.COMPANY_ID = acc_cmp.COMPANY_ID);
 

