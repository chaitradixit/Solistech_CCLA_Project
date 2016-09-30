-- New Instruction Data Field
Insert into REF_INSTRUCTION_DATA 
(INSTRUCTION_DATA_ID,INSTRUCTION_DATA_NAME,INSTRUCTION_DATA_TYPE,INSTRUCTION_DATA_LABEL,INSTRUCTION_DATA_DESCRIPTION) 
values (32,'TRANSACTION_REF','STRING','Transaction Reference','The transaction that this instruction is referencing');

-- Add metadata translation field
Insert into REF_UCM_METADATA_TRANSLATION (INSTRUCTION_DATA_ID, UCM_FIELD_NAME, TRANSLATION_HANDLER_CLASS)
values (32,'xTransactionRef',NULL);

-- Add new instruction data as applicable data to PREADVICE
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 32, 84, 1);

-- Add new instruction data as applicable data to DICONDIN
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 32, 113, 1);

-- Add amount fields to PREADVICE and DICONDIN
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 3, 84, 0);
Insert into APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
values (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 3, 113, 0);


-- Add new instruction status
Insert into REF_INSTRUCTION_STATUS (INSTRUCTION_STATUS_ID,INSTRUCTION_STATUS_NAME,INSTRUCTION_STATUS_DESCRIPTION) 
values (16,'Ready For Pend Trans Checks','Item ready for pending transaction checks');
Insert into REF_INSTRUCTION_STATUS (INSTRUCTION_STATUS_ID,INSTRUCTION_STATUS_NAME,INSTRUCTION_STATUS_DESCRIPTION) 
values (17,'Passed Pend Trans Checks','Item passed pending transaction checks');
Insert into REF_INSTRUCTION_STATUS (INSTRUCTION_STATUS_ID,INSTRUCTION_STATUS_NAME,INSTRUCTION_STATUS_DESCRIPTION) 
values (18,'Failed Pend Trans Checks','Item failed pending transaction checks');
Insert into REF_INSTRUCTION_STATUS (INSTRUCTION_STATUS_ID,INSTRUCTION_STATUS_NAME,INSTRUCTION_STATUS_DESCRIPTION) 
values (19,'Ready For Archive Checks','Item ready for achive checks');

-- Update routing module
update INSTRUCTION_ROUTING_MODULE set EXECUTION_ORDER=3, LISTEN_STATUS_ID=19 where MODULE_ID=3;
update INSTRUCTION_ROUTING_MODULE set EXECUTION_ORDER=2 where MODULE_ID=2;
update INSTRUCTION_ROUTING_MODULE set EXECUTION_ORDER=4 where MODULE_ID=1;
update INSTRUCTION_ROUTING_MODULE set EXECUTION_ORDER=5 where MODULE_ID=4;

-- Add new routing module
Insert into INSTRUCTION_ROUTING_MODULE (MODULE_ID,MODULE_NAME,LISTEN_STATUS_ID,SKIP_STATUS_ID,ENTRY_STATUS_ID,PASS_STATUS_ID,FAIL_STATUS_ID,IS_ENABLED,HANDLER_CLASS,LAST_UPDATED,EXECUTION_ORDER) 
values (5,'Pending Transaction Checks',5,19,16,17,18,1,'com.ecs.ucm.ccla.commproc.modulehandler.TransactionPendingModuleHandler',SYSDATE,1);


--1 (2,'Duplicate Checking',13,null,null,9,9,0,'com.ecs.ucm.ccla.commproc.modulehandler.DuplicateCheckModuleHandler',to_timestamp('23-MAY-11 16.35.40.000000000','DD-MON-RR HH24.MI.SS.FF'),1);
--2 (3,'Archive Check',5,null,2,11,14,1,'com.ecs.ucm.ccla.commproc.modulehandler.ArchiveCheckModuleHandler',to_timestamp('23-MAY-11 15.54.36.000000000','DD-MON-RR HH24.MI.SS.FF'),2);
--3 (1,'Instruction Verification',3,null,null,12,14,0,'com.ecs.ucm.ccla.commproc.modulehandler.VerificationModuleHandler',to_timestamp('23-MAY-11 13.30.34.000000000','DD-MON-RR HH24.MI.SS.FF'),3);
--4 (4,'SPP Release',14,null,null,10,15,1,'com.ecs.ucm.ccla.commproc.modulehandler.SPPJobModuleHandler',to_timestamp('23-MAY-11 15.55.48.000000000','DD-MON-RR HH24.MI.SS.FF'),4);
