
-- Run scripts as CCLA user unless specified otherwise.

-- New Banking/Settlement Date Instruction Data fields
INSERT INTO REF_INSTRUCTION_DATA VALUES (11, 'BANKING_DATE', 'DATE', 'Banking Date',
'Date the instruction was submitted to the bank');
INSERT INTO REF_INSTRUCTION_DATA VALUES (12, 'SETTLEMENT_DATE', 'DATE', 'Settlement Date',
'Date the instruction was settled by the bank');

-- Run as UCMADMIN
UPDATE DOCUMENT_CLASSES SET SIGNATURES_REQUIRED = 'Y' WHERE DOC_CLASS IN ('CONDINS','CLOSEACS');
UPDATE REF_INSTRUCTION_TYPE SET REQUIRE_SIGNATURES = 1 WHERE INSTRUCTION_TYPE_NAME IN ('CONDINS','CLOSEACS');
