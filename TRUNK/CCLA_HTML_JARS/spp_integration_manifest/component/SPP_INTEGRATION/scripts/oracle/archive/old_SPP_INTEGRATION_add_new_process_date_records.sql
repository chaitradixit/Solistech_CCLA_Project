INSERT INTO CCLA_PROCESS_DATES (NAME, DESCRIPTION, PROCESS_DATE, DATE_TYPE)
VALUES ('PSDFTime','PSDF Processing Time', TIMESTAMP '2000-01-01 9:35:00' , 'Time');

INSERT INTO CCLA_PROCESS_DATES (NAME, DESCRIPTION, PROCESS_DATE, DATE_TYPE)
VALUES ('DepositTime','Deposit Funds Processing Time', TIMESTAMP '2000-01-01 09:35:00' , 'Time');

INSERT INTO CCLA_PROCESS_DATES (NAME, DESCRIPTION, PROCESS_DATE, DATE_TYPE)
VALUES ('UnitisedTime','Unitised Fund Processing Time', TIMESTAMP '2000-01-01 09:35:00' , 'Time');

INSERT INTO CCLA_PROCESS_DATES (NAME, DESCRIPTION, PROCESS_DATE, DATE_TYPE)
VALUES ('RollbackTime','Rollback time, Allows User to specify a constant time to process against. Press Clear to revert to current time', null , 'Time');

INSERT INTO CCLA_PROCESS_DATES (NAME, DESCRIPTION, PROCESS_DATE, DATE_TYPE)
VALUES ('RollbackDate','Rollback date, Allows User to specify a constant date to process against. Press Clear to revert to current date', null , 'Date');

commit;