CREATE SEQUENCE SEQ_INSTRUCTION_RULE_ID MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER NOCYCLE;
CREATE SEQUENCE SEQ_INSTRUCTION_CONDITION_ID MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER NOCYCLE;

CREATE OR REPLACE PUBLIC SYNONYM SEQ_INSTRUCTION_RULE_ID FOR CCLA.SEQ_INSTRUCTION_RULE_ID;
CREATE OR REPLACE PUBLIC SYNONYM SEQ_INSTRUCTION_CONDITION_ID FOR CCLA.SEQ_INSTRUCTION_CONDITION_ID;

GRANT ALL ON SEQ_INSTRUCTION_RULE_ID TO UCMADMIN;
GRANT ALL ON SEQ_INSTRUCTION_CONDITION_ID TO UCMADMIN;