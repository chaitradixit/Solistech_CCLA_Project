--------------------------------------------------------
--  File created - Wednesday-December-19-2012   
--------------------------------------------------------
REM INSERTING into SYSTEM_CONFIG_VAR
SET DEFINE OFF;
Insert into SYSTEM_CONFIG_VAR (CONFIG_VAR_NAME,CONFIG_VAR_DESCRIPTION,CONFIG_VAR_DATA_TYPE,STRING_VALUE,INT_VALUE,FLOAT_VALUE,DATE_VALUE,BOOL_VALUE,LAST_UPDATED,LAST_UPDATED_BY,CONFIG_VAR_TYPE) values ('ElementLocking_IsEnabled','Whether or not Element Locking functionality is enabled when adding and removing Relations','BOOL',null,1,null,null,1,to_timestamp('13-DEC-12 16.08.56.000000000','DD-MON-RR HH24.MI.SS.FF'),'weblogic','ELEMENT_LOCKING');
Insert into SYSTEM_CONFIG_VAR (CONFIG_VAR_NAME,CONFIG_VAR_DESCRIPTION,CONFIG_VAR_DATA_TYPE,STRING_VALUE,INT_VALUE,FLOAT_VALUE,DATE_VALUE,BOOL_VALUE,LAST_UPDATED,LAST_UPDATED_BY,CONFIG_VAR_TYPE) values ('ElementLocking_RaiseErrorOnCollision','Whether or not an Element Lock collision will immediately throw an error, instead of sleeping','BOOL',null,null,null,null,0,to_timestamp('13-DEC-12 16.56.36.000000000','DD-MON-RR HH24.MI.SS.FF'),'weblogic','ELEMENT_LOCKING');
Insert into SYSTEM_CONFIG_VAR (CONFIG_VAR_NAME,CONFIG_VAR_DESCRIPTION,CONFIG_VAR_DATA_TYPE,STRING_VALUE,INT_VALUE,FLOAT_VALUE,DATE_VALUE,BOOL_VALUE,LAST_UPDATED,LAST_UPDATED_BY,CONFIG_VAR_TYPE) values ('ElementLocking_SleepTime','Time to sleep between Element Lock retries following a collision','INT',null,3000,null,null,null,to_timestamp('13-DEC-12 16.57.13.000000000','DD-MON-RR HH24.MI.SS.FF'),'weblogic','ELEMENT_LOCKING');
Insert into SYSTEM_CONFIG_VAR (CONFIG_VAR_NAME,CONFIG_VAR_DESCRIPTION,CONFIG_VAR_DATA_TYPE,STRING_VALUE,INT_VALUE,FLOAT_VALUE,DATE_VALUE,BOOL_VALUE,LAST_UPDATED,LAST_UPDATED_BY,CONFIG_VAR_TYPE) values ('ElementLocking_MaxSleepTime','Maximum time an Element Lock request will sleep for before throwing error','INT',null,90000,null,null,null,to_timestamp('13-DEC-12 17.01.13.000000000','DD-MON-RR HH24.MI.SS.FF'),'weblogic','ELEMENT_LOCKING');
