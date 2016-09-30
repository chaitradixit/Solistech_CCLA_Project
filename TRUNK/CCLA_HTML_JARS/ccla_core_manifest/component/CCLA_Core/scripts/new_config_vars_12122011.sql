
-- New SystemConfigVars, controlling docmeta update filters
Insert into SYSTEM_CONFIG_VAR (CONFIG_VAR_NAME,CONFIG_VAR_DESCRIPTION,CONFIG_VAR_DATA_TYPE,STRING_VALUE,INT_VALUE,FLOAT_VALUE,DATE_VALUE,BOOL_VALUE,LAST_UPDATED,LAST_UPDATED_BY,CONFIG_VAR_TYPE) values ('DocMeta_AutoCompleteDocDate','Determines where the xDocumentDate field is auto-completed on all documents via Update Filter','BOOL',null,null,null,null,1,to_timestamp('08-DEC-11 19.15.22.000000000','DD-MON-RR HH24.MI.SS.FF'),'sysadmin','DOC_UPDATE');
Insert into SYSTEM_CONFIG_VAR (CONFIG_VAR_NAME,CONFIG_VAR_DESCRIPTION,CONFIG_VAR_DATA_TYPE,STRING_VALUE,INT_VALUE,FLOAT_VALUE,DATE_VALUE,BOOL_VALUE,LAST_UPDATED,LAST_UPDATED_BY,CONFIG_VAR_TYPE) values ('DocMeta_AutoPadAccountNumber','Determines where the xAccountNumber field is auto-completed on all documents via Update Filter','BOOL',null,null,null,null,1,to_timestamp('08-DEC-11 19.08.11.000000000','DD-MON-RR HH24.MI.SS.FF'),'sysadmin','DOC_UPDATE');
Insert into SYSTEM_CONFIG_VAR (CONFIG_VAR_NAME,CONFIG_VAR_DESCRIPTION,CONFIG_VAR_DATA_TYPE,STRING_VALUE,INT_VALUE,FLOAT_VALUE,DATE_VALUE,BOOL_VALUE,LAST_UPDATED,LAST_UPDATED_BY,CONFIG_VAR_TYPE) values ('DocMeta_AutoPadClientNumber','Determines whether the xClientNumber field is auto-padded on all documents via Update Filter','BOOL',null,null,null,null,1,to_timestamp('08-DEC-11 19.16.05.000000000','DD-MON-RR HH24.MI.SS.FF'),'sysadmin','DOC_UPDATE');

-- New SystemConfigVars, controlling Routing Module Manager
Insert into SYSTEM_CONFIG_VAR (CONFIG_VAR_NAME,CONFIG_VAR_DESCRIPTION,CONFIG_VAR_DATA_TYPE,STRING_VALUE,INT_VALUE,FLOAT_VALUE,DATE_VALUE,BOOL_VALUE,LAST_UPDATED,LAST_UPDATED_BY,CONFIG_VAR_TYPE) values ('CommProc_InterCycleRestPeriod','Time, in ms, between running the set of active Routing Modules','INT',null,10000,null,null,null,to_timestamp('12-DEC-11 11.43.43.000000000','DD-MON-RR HH24.MI.SS.FF'),'sysadmin','ROUTING_MODULE');
Insert into SYSTEM_CONFIG_VAR (CONFIG_VAR_NAME,CONFIG_VAR_DESCRIPTION,CONFIG_VAR_DATA_TYPE,STRING_VALUE,INT_VALUE,FLOAT_VALUE,DATE_VALUE,BOOL_VALUE,LAST_UPDATED,LAST_UPDATED_BY,CONFIG_VAR_TYPE) values ('CommProc_InterModuleRestPeriod','Time, in ms, between running each Routing Module','INT',null,1000,null,null,null,to_timestamp('12-DEC-11 11.45.16.000000000','DD-MON-RR HH24.MI.SS.FF'),'sysadmin','ROUTING_MODULE');
Insert into SYSTEM_CONFIG_VAR (CONFIG_VAR_NAME,CONFIG_VAR_DESCRIPTION,CONFIG_VAR_DATA_TYPE,STRING_VALUE,INT_VALUE,FLOAT_VALUE,DATE_VALUE,BOOL_VALUE,LAST_UPDATED,LAST_UPDATED_BY,CONFIG_VAR_TYPE) values ('CommProc_NumInstructionsToProcess','Limit on the total number of instructions to process per Routing Module during a single cycle','INT',null,30,null,null,null,to_timestamp('12-DEC-11 11.46.16.000000000','DD-MON-RR HH24.MI.SS.FF'),'sysadmin','ROUTING_MODULE');

-- Extra bits for SDU
-- ------------------

-- New Instruction Audit Actions
INSERT INTO REF_INSTR_AUDIT_ACTION VALUES (16, 'Started Notification Job', 'Notification job started, based on this instruction');
INSERT INTO REF_INSTR_AUDIT_ACTION VALUES (17, 'Resubmitted', 'Resubmitted for processing or approval');
INSERT INTO REF_INSTR_AUDIT_ACTION VALUES (18, 'Reset Authorisation', 'Reset authorisation state');

-- New Instruction Action
INSERT INTO REF_INSTRUCTION_ACTION VALUES (9, 'Resubmit');

-- Make new action available for suspended SDU instructions
REM INSERTING into INSTR_PROCESS_ACTION_APPLIED
Insert into INSTR_PROCESS_ACTION_APPLIED (INSTRUCTION_PROCESS_ID,INSTRUCTION_ACTION_ID,APPLIED_ACTION_LABEL,APPLIED_ACTION_DESCRIPTION) 
values (3,9,'Resubmit for authorisation','Mark the instruction as authorisation pending');

-- New Instruction Status
INSERT INTO REF_INSTRUCTION_STATUS VALUES (33, 'Pending SDU Re-authorisation', 
'Item originally rejected or failed execution. Work has now been done to address the issue and its ready for re-authorisation');
