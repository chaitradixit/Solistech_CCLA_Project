
/* change to identity check table */
alter table ccla_identity_check  add core_data_changed number;

/* add entry for when data changes that could affect authenticate/validate score */
Insert into CCLA_IDENTITY_CHECK_LOOKUP (OUTCOME_ID,DESCRIPTION,EXPERIAN_RISK_CODE,EXPERIAN_DECISION_CODE,EXPERIAN_DECISION_TEXT,HAPPY_SCORE,UNHAPPY_SCORE,FAIL_CONDITION,PASS_CONDITION,AMBER_CONDITION) values (82,'Name, age or address data changed - awaiting  reauthentication',null,null,null,null,79,null,null,1);


/* properties for withdrawal and income account relations*/
Insert into REF_RELATION_PROPERTY (RELATION_PROPERTY_ID,RELATION_NAME_ID,PROPERTY_ID) values (1,120,1);
Insert into REF_RELATION_PROPERTY (RELATION_PROPERTY_ID,RELATION_NAME_ID,PROPERTY_ID) values (2,121,1);

/* SEQUENCE FOR PROPERTY APPLIED TABLE*/
CREATE SEQUENCE  SEQ_PROPERTY_APPLIED_ID 			MINVALUE 1 MAXVALUE 999999999999999999999999999 INCREMENT BY 1 START WITH 2000000 CACHE 20 NOORDER NOCYCLE;

