
ALTER TABLE CS_ACCOUNTS RENAME TO CS_ACCOUNTS_OLD;
ALTER TABLE CS_ADDRESSES RENAME TO CS_ADDRESSES_OLD;
ALTER TABLE CS_CORRESPONDENT_COMMS RENAME TO CS_CORRESPONDENT_COMMS_OLD;
ALTER TABLE CS_CORRESPONDENT_EXCHANGES RENAME TO CS_CORRESPONDENT_EXCHANGES_OLD;
ALTER TABLE CS_CORRESPONDENT_QUESTIONS RENAME TO CS_CORRESPONDENT_QUESTIONS_OLD;
ALTER TABLE CS_FUND_TRANSFERS RENAME TO CS_FUND_TRANSFERS_OLD;
ALTER TABLE CS_FUND_TRANSFERS_NOTES RENAME TO CS_FUND_TRANSFERS_NOTES_OLD;
ALTER TABLE CS_PERSON_DETAILS RENAME TO CS_PERSON_DETAILS_OLD;
ALTER TABLE CS_PROCESS_NOTES RENAME TO CS_PROCESS_NOTES_OLD;

ALTER TABLE MN_CLIENT RENAME TO MN_CLIENT_OLD;
ALTER TABLE MN_PERSONS RENAME TO MN_PERSONS_OLD;
ALTER TABLE TAURORACACHE RENAME TO TAURORACACHE_OLD;
ALTER TABLE TCLIENTS RENAME TO TCLIENTS_OLD;

ALTER TABLE CCLA_ACTIVITIES RENAME TO CCLA_ACTIVITIES_OLD;
ALTER TABLE CCLA_ACCOUNTS RENAME TO CCLA_ACCOUNTS_OLD;
ALTER TABLE CCLA_FUNDS RENAME TO CCLA_FUNDS_OLD;
ALTER TABLE CCLA_FORMS RENAME TO CCLA_FORMS_OLD;
ALTER TABLE CCLA_CLIENTS RENAME TO CCLA_CLIENTS_OLD;
ALTER TABLE CCLA_PERSONS RENAME TO CCLA_PERSONS_OLD;
ALTER TABLE CCLA_PROCESSES RENAME TO CCLA_PROCESSES_OLD;
ALTER TABLE CCLA_PERSON_AURORA_MAP RENAME TO CCLA_PERSON_AURORA_MAP_OLD;
ALTER TABLE CCLA_RELATIONSHIPS RENAME TO CCLA_RELATIONSHIPS_OLD;
ALTER TABLE CCLA_TITLE RENAME TO CCLA_TITLE_OLD;
ALTER TABLE CCLA_TITLE_CATEGORY RENAME TO CCLA_TITLE_CATEGORY_OLD;
ALTER TABLE CCLA_SORTCODES RENAME TO CCLA_SORTCODES_OLD;
ALTER TABLE CCLA_ACTIVE_PERSON_ACCOUNTS RENAME TO CCLA_ACTIVE_PERSON_ACCOUNTSOLD;
