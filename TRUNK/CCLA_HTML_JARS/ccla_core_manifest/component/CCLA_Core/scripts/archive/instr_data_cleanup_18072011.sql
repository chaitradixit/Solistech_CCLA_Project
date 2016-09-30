-- This script will delete any 'dangling' Instr. Data. Applied rows, where an Instruction's
-- Instruction Type was changed.
DELETE FROM 
  (SELECT ida.*
  FROM APPLICABLE_INSTRUCTION_DATA aid
  INNER JOIN INSTRUCTION_DATA_APPLIED ida
  ON (aid.applicable_instruction_data_id = ida.applicable_instruction_data_id)
  
  INNER JOIN INSTRUCTION ins ON (ins.INSTRUCTION_ID = ida.INSTRUCTION_ID)
  
  WHERE ins.INSTRUCTION_TYPE_ID <> aid.INSTRUCTION_TYPE_ID);
  
-- Check for any Comm instances where the Group ID is null.  
SELECT * FROM COMM WHERE COMM_GROUP_ID IS NULL;

	-- Delete any applied data on linked instructions.
	DELETE FROM INSTRUCTION_DATA_APPLIED WHERE INSTRUCTION_ID IN (
	  SELECT INSTRUCTION_ID FROM INSTRUCTION INNER JOIN COMM ON (INSTRUCTION.COMM_ID = COMM.COMM_ID)
	  WHERE COMM.COMM_GROUP_ID IS NULL
	);

	-- Delete any audits on linked instructions.
	DELETE FROM INSTRUCTION_AUDIT WHERE INSTRUCTION_ID IN (
	  SELECT INSTRUCTION_ID FROM INSTRUCTION INNER JOIN COMM ON (INSTRUCTION.COMM_ID = COMM.COMM_ID)
	  WHERE COMM.COMM_GROUP_ID IS NULL
	);

	-- Delete instructions themselves.
	DELETE FROM INSTRUCTION WHERE INSTRUCTION_ID IN (
	  SELECT INSTRUCTION_ID FROM INSTRUCTION INNER JOIN COMM ON (INSTRUCTION.COMM_ID = COMM.COMM_ID)
	  WHERE COMM.COMM_GROUP_ID IS NULL
	);
	
	-- Finally delete the Comm objects.
	DELETE FROM COMM WHERE COMM_GROUP_ID IS NULL;
	

-- New view
CREATE OR REPLACE FORCE VIEW "CCLA"."V_INSTRUCTION_DATA_VALUE" 
  ("INSTRUCTION_ID", "INSTRUCTION_TYPE_ID", "INSTRUCTION_DATA_ID", 
  "INSTRUCTION_VALUE", 
  "INSTRUCTION_STRING_VALUE", "INSTRUCTION_NUM_VALUE", "INSTRUCTION_DATE_VALUE")
AS
  SELECT  ida.INSTRUCTION_ID,
          aid.INSTRUCTION_TYPE_ID,
          aid.INSTRUCTION_DATA_ID,
          ida.INSTRUCTION_VALUE,
          ida.INSTRUCTION_STRING_VALUE,
          ida.INSTRUCTION_NUM_VALUE,
          ida.INSTRUCTION_DATE_VALUE
          
  FROM APPLICABLE_INSTRUCTION_DATA aid
  INNER JOIN INSTRUCTION_DATA_APPLIED ida
  ON (aid.applicable_instruction_data_id = ida.applicable_instruction_data_id);

-- Run the below as SYS
CREATE OR REPLACE PUBLIC SYNONYM REVISIONS FOR UCMADMIN.REVISIONS;
GRANT SELECT, REFERENCES ON REVISIONS TO CCLA;
  
CREATE OR REPLACE PUBLIC SYNONYM V_INSTRUCTION_DATA_VALUE FOR CCLA.V_INSTRUCTION_DATA_VALUE;
GRANT ALL ON V_INSTRUCTION_DATA_VALUE TO UCMADMIN;

-- New Extended Data view
CREATE OR REPLACE FORCE VIEW "CCLA"."V_INSTRUCTIONS_EXTENDED_DATA" ("INSTRUCTION_ID", "COMM_ID", "COMM_SOURCE_ID", "COMM_SOURCE_NAME", "INTERACTION_ID", "COMM_TYPE_NAME", "DATE_COMM_ADDED", "CREATED_BY", 
"ORGANISATION_ID", "ORGANISATION_NAME", "CLIENT_NUMBER", "PADDED_CLIENT_NUMBER", "COMPANY_CODE", 
"COMM_GROUP_ID", "UCM_BATCH_REF", "SPP_BATCH_REF", 
"INSTRUCTION_STATUS_ID", "INSTRUCTION_STATUS_NAME", "PROCESS_DATE", "ACTUAL_PROCESS_DATE", "INSTRUCTION_TYPE_ID", "SPP_JOB_ID", 
"LAST_UPDATED", "DATE_ADDED", "LAST_UPDATED_BY", "DEPENDENT_INSTRUCTION_ID", "INSTRUCTION_DOC_ID", "PRIORITY", "INSTRUCTION_TYPE_NAME", "IS_FINANCIAL_TRANSACTION", "IS_SUSPENDED", 
"DOC_NAME", "DOC_TYPE", 
"CASH", "UNITS", "SOURCE_ACCOUNT_ID", "SOURCE_FUND_CODE", "SOURCE_ACCOUNTNUMBER", "SOURCE_ACCOUNTNUMBER_STRING")
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
    
    DECODE(acc_cmp.COMPANY_CODE, NULL, org_cmp.COMPANY_CODE, acc_cmp.COMPANY_CODE)
    AS COMPANY_CODE,

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
    r.dDocType,
    
    TO_CHAR(idv_cash.instruction_num_value, 'fm999G999G999G990d00') AS CASH,
    TO_CHAR(idv_units.instruction_num_value, 'fm999G999G999G990d00') AS UNITS,
    
    srcaccount.ACCOUNT_ID AS SOURCE_ACCOUNT_ID,
    srcaccount.FUND_CODE AS SOURCE_FUND_CODE,
    srcaccount.ACCOUNTNUMBER AS SOURCE_ACCOUNTNUMBER,

    CASE
      WHEN (acc_cmp.COMPANY_CODE IS NULL)
      THEN NULL
      WHEN (acc_cmp.COMPANY_CODE = 'CBF')
      THEN LPAD(srcaccount.ACCOUNTNUMBER, 3, '0') || srcaccount.FUND_CODE
      ELSE LPAD(srcaccount.ACCOUNTNUMBER, 4, '0') || srcaccount.FUND_CODE
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
  LEFT JOIN UCMADMIN.REVISIONS r
  ON (ins.INSTRUCTION_DOC_ID = r.dID)
  
  LEFT JOIN V_INSTRUCTION_DATA_VALUE idv_org 
  ON (idv_org.instruction_id = ins.instruction_id 
      AND idv_org.instruction_type_id = ins.instruction_type_id
      AND idv_org.instruction_data_id = 9)
  
  LEFT JOIN ORGANISATION org
  ON (idv_org.instruction_num_value = org.ORGANISATION_ID)
  LEFT JOIN CLIENT_AURORA_MAP cli
  ON (org.ORGANISATION_ID = cli.ORGANISATION_ID)
  LEFT JOIN REF_COMPANY org_cmp
  ON (cli.COMPANY_ID = org_cmp.COMPANY_ID)
  
  LEFT JOIN V_INSTRUCTION_DATA_VALUE idv_cash 
  ON (idv_cash.instruction_id = ins.instruction_id 
      AND idv_cash.instruction_type_id = ins.instruction_type_id 
      AND idv_cash.instruction_data_id = 3)
      
  LEFT JOIN V_INSTRUCTION_DATA_VALUE idv_units 
  ON (idv_units.instruction_id = ins.instruction_id 
      AND idv_units.instruction_type_id = ins.instruction_type_id 
      AND idv_units.instruction_data_id = 4)

  LEFT JOIN V_INSTRUCTION_DATA_VALUE idv_srcaccount 
  ON (idv_srcaccount.instruction_id = ins.instruction_id 
      AND idv_srcaccount.instruction_type_id = ins.instruction_type_id
      AND idv_srcaccount.instruction_data_id = 1)
  
  LEFT JOIN ACCOUNT srcaccount 
  ON (idv_srcaccount.instruction_num_value = srcaccount.ACCOUNT_ID)
  
  LEFT JOIN FUND acc_fnd
  ON (srcaccount.FUND_CODE = acc_fnd.FUND_CODE)
  LEFT JOIN REF_COMPANY acc_cmp
  ON (acc_fnd.COMPANY_ID = acc_cmp.COMPANY_ID);

  
-- Run these as SYS
CREATE OR REPLACE PUBLIC SYNONYM V_INSTRUCTIONS_EXTENDED_DATA FOR CCLA.V_INSTRUCTIONS_EXTENDED_TEST;
GRANT SELECT ON V_INSTRUCTIONS_EXTENDED_DATA TO UCMADMIN;

-- Ensure Comm Group ID is always NN
ALTER TABLE COMM MODIFY (
  COMM_GROUP_ID CONSTRAINT COMM_COMMGROUPID_NN NOT NULL
);