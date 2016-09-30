-- Run as CCLA user

-- Add new 'RPISURPLUSDD' Instruction Type
SET DEFINE OFF;
Insert into REF_INSTRUCTION_TYPE 
(INSTRUCTION_TYPE_ID,INSTRUCTION_TYPE_NAME,TRANSACTION_TYPE_ID,INSTRUCTION_DESCRIPTION,IS_FINANCIAL_TRANSACTION,SUBMIT_TO_SPP,REQUIRE_SIGNATURES,IS_SUPPORTING,DATE_ADDED,LAST_UPDATED,SETTLEMENT_OFFSET,IS_STATIC_DATA_UPDATE) 
values 
(132,'RPISURPLUSDD',null,'Community First RPI Surplus Drawdown. Confirms the RPI surplus drawdown amounts for an LCF''s Donors/TTLAs',0,1,1,0,to_timestamp('26-FEB-13 17.11.07.871000000','DD-MON-RR HH24.MI.SS.FF'),to_timestamp('26-FEB-13 17.11.12.311000000','DD-MON-RR HH24.MI.SS.FF'),0,0);

-- Add applicable data fields

-- Org ID
INSERT INTO APPLICABLE_INSTRUCTION_DATA VALUES (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 9, 132, 0);

-- Account ID
INSERT INTO APPLICABLE_INSTRUCTION_DATA VALUES (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 1, 132, 0);

-- Form ID
INSERT INTO APPLICABLE_INSTRUCTION_DATA VALUES (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 17, 132, 1);

-- Comments
INSERT INTO APPLICABLE_INSTRUCTION_DATA VALUES (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 18, 132, 1);

-- UCM Job ID
INSERT INTO APPLICABLE_INSTRUCTION_DATA VALUES (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 19, 132, 1);
-- WF Date
INSERT INTO APPLICABLE_INSTRUCTION_DATA VALUES (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 22, 132, 1);
-- Sigs Valid
INSERT INTO APPLICABLE_INSTRUCTION_DATA VALUES (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 23, 132, 1);

-- Parent Instr. Ref
INSERT INTO APPLICABLE_INSTRUCTION_DATA VALUES (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 52, 132, 1);

-- Calculation Date
INSERT INTO REF_INSTRUCTION_DATA VALUES (68, 'CALCULATION_DATE', 'DATE', 'Calculation Date',
'Cut-off date used for a periodic calculation that the instruction refers to, i.e. the date that RPI surplus amounts were calculated up to.');

INSERT INTO ref_ucm_metadata_translation VALUES 
(68, null, 'com.ecs.ucm.ccla.commproc.translation.CalculationDateFieldHandler');

INSERT INTO APPLICABLE_INSTRUCTION_DATA VALUES (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 68, 132, 1);

-- Updates to withdrawal instruction types...

-- New data field, Beneficiary Org
INSERT INTO REF_INSTRUCTION_DATA VALUES (69, 'BENEFICIARY_ORG_ID', 'INT', 'Beneficiery Org',
'Organisation who will be the beneficiary of a payment, when not the same as the primary Organisation who owns the account. For a Community First withdrawal, this would store the LCF Org reference');
INSERT INTO APPLICABLE_INSTRUCTION_DATA VALUES (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 69, 132, 1);

-- Add against all Withdrawal instruction types.
INSERT INTO APPLICABLE_INSTRUCTION_DATA
SELECT SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 69, INSTRUCTION_TYPE_ID, 1 FROM REF_INSTRUCTION_TYPE
WHERE ref_instruction_type.transaction_type_id = 2;

-- Add new RPI-SURPLUS-DD Form Type
REM INSERTING into REF_FORM_TYPE
SET DEFINE OFF;
Insert into REF_FORM_TYPE 
(FORM_TYPE_ID,FORM_TYPE_NAME,GEN_INSTRUCTION_TYPE_ID,RET_INSTRUCTION_TYPE_ID,FORM_HANDLER_CLASS,FORM_TYPE_SHORTNAME,IS_SINGLETON,ELEMENT_TYPE_ID) 
values 
(23,'Community First RPI Surplus Drawdown',132,132,'com.ecs.stellent.ccla.clientservices.form.CommunityFirstRPISurplusFormHandler','CF-RPI-SURPLUS',1,3);

-- New Comm Source 'Comm First DB'
INSERT INTO REF_COMM_SOURCE VALUES (9, 'Comm First DB');
INSERT INTO REF_COMM_TYPE VALUES (6, 'Comm First Transaction Feed');

-- Add 'Dest Fund Code' as applicable data field for all transfer types
INSERT INTO APPLICABLE_INSTRUCTION_DATA
SELECT SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 40, rit.INSTRUCTION_TYPE_ID, 1 
FROM REF_INSTRUCTION_TYPE rit 
WHERE TRANSACTION_TYPE_ID = 3;

-- Add Contribution/Allocation/Share Type codes
UPDATE ref_contribution_type SET contribution_type_code = 'E' WHERE CONTRIBUTION_TYPE_ID = 1;
UPDATE ref_contribution_type SET contribution_type_code = 'GA' WHERE CONTRIBUTION_TYPE_ID = 2;
UPDATE ref_contribution_type SET contribution_type_code = 'GA' WHERE CONTRIBUTION_TYPE_ID = 3;
UPDATE ref_contribution_type SET contribution_type_code = 'G' WHERE CONTRIBUTION_TYPE_ID = 4;

-- Remove CF Coll. Type attribute from new withdrawal account 3C
DELETE FROM ELEMENT_ATTRIBUTE_APPLIED WHERE ELEMENT_ID = 6189187 AND ELEMENT_ATTRIBUTE_ID = 63;

-- New system config var for ADF RPI drawdown page
SET DEFINE OFF;
Insert into SYSTEM_CONFIG_VAR 
(CONFIG_VAR_NAME,CONFIG_VAR_DESCRIPTION,CONFIG_VAR_DATA_TYPE,STRING_VALUE,INT_VALUE,FLOAT_VALUE,DATE_VALUE,BOOL_VALUE,LAST_UPDATED,LAST_UPDATED_BY,CONFIG_VAR_TYPE) 
values 
('CommunityFirst_RPISurplusDrawdown_ADFPageURL','Full ADF Page URL for RPI Surplus drawdown entry screen','STRING','http://oracle/CommunityFirst/faces/custom',null,null,null,null,to_timestamp('07-MAR-13 11.45.09.000000000','DD-MON-RR HH24.MI.SS.FF'),'weblogic','CAMPAIGN');

Insert into SYSTEM_CONFIG_VAR 
(CONFIG_VAR_NAME,CONFIG_VAR_DESCRIPTION,CONFIG_VAR_DATA_TYPE,STRING_VALUE,INT_VALUE,FLOAT_VALUE,DATE_VALUE,BOOL_VALUE,LAST_UPDATED,LAST_UPDATED_BY,CONFIG_VAR_TYPE) 
values 
('CommunityFirst_RPISurplusDrawdownReport_ADFPageURL','Full ADF Page URL for RPI Surplus drawdown report screen','STRING','http://oracle/CommunityFirst/faces/rpiOverview',null,null,null,null,to_timestamp('07-MAR-13 11.45.09.000000000','DD-MON-RR HH24.MI.SS.FF'),'weblogic','CAMPAIGN');


----------
-- Run as UCM/WCC user
----------
-- Add new Doc Class.
INSERT INTO DOCUMENT_CLASSES VALUES (129, SYSDATE, SYSDATE, 'cclaucmdev', 'RPISURPLUSDD', 'N', 'Community First RPI Surplus Drawdown. Confirms the RPI surplus drawdown amounts for an LCF''s Donors/TTLAs', 'N', 'N', 'N', 'N', 'Y', 'N', 'N');

