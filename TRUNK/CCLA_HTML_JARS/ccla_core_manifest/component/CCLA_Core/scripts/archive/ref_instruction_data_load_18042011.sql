-- Full data loader scripts for all instruction data reference tables.

-- Add Document Date as a required data field for every single instruction type.
INSERT INTO APPLICABLE_INSTRUCTION_DATA
SELECT SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 16, INSTRUCTION_TYPE_ID, 0
FROM REF_INSTRUCTION_TYPE;

-- Add Instruction Comment as an optional data field for every single instruction type.
INSERT INTO APPLICABLE_INSTRUCTION_DATA
SELECT SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 18, INSTRUCTION_TYPE_ID, 1
FROM REF_INSTRUCTION_TYPE;