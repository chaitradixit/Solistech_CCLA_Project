/* make CCLA_ACCOUNT_AURORA_UPDATES UPDATE_TYPE COLUMN  a bit longer */

alter table CCLA_ACCOUNT_AURORA_UPDATES modify (UPDATE_TYPE varchar2(100));


/* make ACCOUNT ACCNUMEXT COLUMN  a bit longer */

alter table ACCOUNT modify ACCNUMEXT VARCHAR2(18 BYTE)

/* ALLOW NULL FOR DATE_OPENED (because of pending accounts */
alter table ACCOUNT modify DATE_OPENED NULL

/* set legacy checked to be a pass condition */
update CCLA_IDENTITY_CHECK_LOOKUP set pass_condition = 1 where outcome_id = 24

/* add last_updated to bank_account table*/
alter table
   bank_account
add
   LAST_UPDATED TIMESTAMP (6) 