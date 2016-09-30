
-- Add new instruction type 
REM INSERTING into REF_INSTRUCTION_TYPE
Insert into REF_INSTRUCTION_TYPE (INSTRUCTION_TYPE_ID,INSTRUCTION_TYPE_NAME,TRANSACTION_TYPE_ID,INSTRUCTION_DESCRIPTION,IS_FINANCIAL_TRANSACTION,SUBMIT_TO_SPP,REQUIRE_SIGNATURES,IS_SUPPORTING,DATE_ADDED,LAST_UPDATED,SETTLEMENT_OFFSET) 
values (123,'BNKCONDIN',NULL,'Bank Condin',0,1,0,0,SYSDATE,SYSDATE,0);

Insert into REF_INSTRUCTION_TYPE (INSTRUCTION_TYPE_ID,INSTRUCTION_TYPE_NAME,TRANSACTION_TYPE_ID,INSTRUCTION_DESCRIPTION,IS_FINANCIAL_TRANSACTION,SUBMIT_TO_SPP,REQUIRE_SIGNATURES,IS_SUPPORTING,DATE_ADDED,LAST_UPDATED,SETTLEMENT_OFFSET) 
values (124,'IAT',NULL,'IAT Sweep',0,0,0,0,SYSDATE,SYSDATE,0);

Insert into REF_INSTRUCTION_TYPE (INSTRUCTION_TYPE_ID,INSTRUCTION_TYPE_NAME,TRANSACTION_TYPE_ID,INSTRUCTION_DESCRIPTION,IS_FINANCIAL_TRANSACTION,SUBMIT_TO_SPP,REQUIRE_SIGNATURES,IS_SUPPORTING,DATE_ADDED,LAST_UPDATED,SETTLEMENT_OFFSET) 
values (125,'RETFUNDS',NULL,'Return Funds',1,1,0,0,SYSDATE,SYSDATE,0);


-- New Instruction Data Field

-- Add new instruction data as applicable data to Instructions

-- PREADVICE
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 33, 84, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 38, 84, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 41, 84, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 42, 84, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 43, 84, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 44, 84, 1);

-- DEPBNK
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 33, 49, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 35, 49, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 36, 49, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 37, 49, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 38, 49, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 39, 49, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 40, 49, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 7, 49, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 41, 49, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 42, 49, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 43, 49, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 44, 49, 1);

-- BUYBNK
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 33, 14, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 35, 14, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 36, 14, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 37, 14, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 38, 14, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 39, 14, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 40, 14, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 7, 14, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 41, 14, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 42, 14, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 43, 14, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 44, 14, 1);

-- DICONDIN
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 2, 113, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 34, 113, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 35, 113, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 36, 113, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 37, 113, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 38, 113, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 40, 113, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 7, 113, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 41, 113, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 42, 113, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 43, 113, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 44, 113, 1);

-- BNKCONDIN
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 2, 123, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 3, 123, 0);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 9, 123, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 16, 123, 0);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 17, 123, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 18, 123, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 19, 123, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 21, 123, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 22, 123, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 23, 123, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 32, 123, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 34, 123, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 35, 123, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 36, 123, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 37, 123, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 38, 123, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 40, 123, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 7, 123, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 41, 123, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 42, 123, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 43, 123, 1);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 44, 123, 1);

-- IAT Instruction
-- Fund code
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 38, 124, 0);
-- Cash Amount
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 3, 124, 0);
-- Bank Account Id
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 2, 124, 1);


-- RETFUNDS Instruction
-- Narrative
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 35, 125, 0);
-- Cash Amount
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 3, 125, 0);
-- Bank Account Id
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 2, 125, 1);
-- condin ref
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 33, 125, 1);



-- New Instruction Statuses
INSERT INTO REF_INSTRUCTION_STATUS (INSTRUCTION_STATUS_ID, INSTRUCTION_STATUS_NAME, INSTRUCTION_STATUS_DESCRIPTION) VALUES (20, 'Pending IAT Sweep', 'Item pending IAT Sweep');
INSERT INTO REF_INSTRUCTION_STATUS (INSTRUCTION_STATUS_ID, INSTRUCTION_STATUS_NAME, INSTRUCTION_STATUS_DESCRIPTION) VALUES (21, 'Pending Match', 'Item pending match');
INSERT INTO REF_INSTRUCTION_STATUS (INSTRUCTION_STATUS_ID, INSTRUCTION_STATUS_NAME, INSTRUCTION_STATUS_DESCRIPTION) VALUES (22, 'Completed IAT Sweep', 'Item completed IAT Sweep');
INSERT INTO REF_INSTRUCTION_STATUS (INSTRUCTION_STATUS_ID, INSTRUCTION_STATUS_NAME, INSTRUCTION_STATUS_DESCRIPTION) VALUES (23, 'Completed Match', 'Item completed match');
INSERT INTO REF_INSTRUCTION_STATUS (INSTRUCTION_STATUS_ID, INSTRUCTION_STATUS_NAME, INSTRUCTION_STATUS_DESCRIPTION) VALUES (24, 'Returned Fund', 'Funds returned');







