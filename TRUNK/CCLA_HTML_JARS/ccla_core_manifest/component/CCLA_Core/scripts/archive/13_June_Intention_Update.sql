-- Add account id 
ALTER TABLE FUND_INVESTMENT_INTENTION 
ADD ACCOUNT_ID Number (15,0);
/
UPDATE FUND_INVESTMENT_INTENTION SET ACCOUNT_ID=6147343	WHERE INVESTMENT_INTENTION_ID=64;
UPDATE FUND_INVESTMENT_INTENTION SET ACCOUNT_ID=6147570	WHERE INVESTMENT_INTENTION_ID=69;
UPDATE FUND_INVESTMENT_INTENTION SET ACCOUNT_ID=6148784	WHERE INVESTMENT_INTENTION_ID=146;
UPDATE FUND_INVESTMENT_INTENTION SET ACCOUNT_ID=6147366	WHERE INVESTMENT_INTENTION_ID=67;
UPDATE FUND_INVESTMENT_INTENTION SET ACCOUNT_ID=6148720	WHERE INVESTMENT_INTENTION_ID=144;
UPDATE FUND_INVESTMENT_INTENTION SET ACCOUNT_ID=6148748	WHERE INVESTMENT_INTENTION_ID=145;
UPDATE FUND_INVESTMENT_INTENTION SET ACCOUNT_ID=6147616	WHERE INVESTMENT_INTENTION_ID=71;
UPDATE FUND_INVESTMENT_INTENTION SET ACCOUNT_ID=6148425	WHERE INVESTMENT_INTENTION_ID=121;
UPDATE FUND_INVESTMENT_INTENTION SET ACCOUNT_ID=6148443	WHERE INVESTMENT_INTENTION_ID=104;
UPDATE FUND_INVESTMENT_INTENTION SET ACCOUNT_ID=6147349	WHERE INVESTMENT_INTENTION_ID=66;
UPDATE FUND_INVESTMENT_INTENTION SET ACCOUNT_ID=6147286	WHERE INVESTMENT_INTENTION_ID=61;
UPDATE FUND_INVESTMENT_INTENTION SET ACCOUNT_ID=6148657	WHERE INVESTMENT_INTENTION_ID=142;
UPDATE FUND_INVESTMENT_INTENTION SET ACCOUNT_ID=6147328	WHERE INVESTMENT_INTENTION_ID=62;
UPDATE FUND_INVESTMENT_INTENTION SET ACCOUNT_ID=6147957	WHERE INVESTMENT_INTENTION_ID=101;
UPDATE FUND_INVESTMENT_INTENTION SET ACCOUNT_ID=6147526	WHERE INVESTMENT_INTENTION_ID=68;
UPDATE FUND_INVESTMENT_INTENTION SET ACCOUNT_ID=6148434	WHERE INVESTMENT_INTENTION_ID=122;
UPDATE FUND_INVESTMENT_INTENTION SET ACCOUNT_ID=6148441	WHERE INVESTMENT_INTENTION_ID=103;
UPDATE FUND_INVESTMENT_INTENTION SET ACCOUNT_ID=6147602	WHERE INVESTMENT_INTENTION_ID=70;
UPDATE FUND_INVESTMENT_INTENTION SET ACCOUNT_ID=6148553	WHERE INVESTMENT_INTENTION_ID=143;
UPDATE FUND_INVESTMENT_INTENTION SET ACCOUNT_ID=6151879	WHERE INVESTMENT_INTENTION_ID=161;
UPDATE FUND_INVESTMENT_INTENTION SET ACCOUNT_ID=6147345	WHERE INVESTMENT_INTENTION_ID=65;
UPDATE FUND_INVESTMENT_INTENTION SET ACCOUNT_ID=6148440	WHERE INVESTMENT_INTENTION_ID=102;
UPDATE FUND_INVESTMENT_INTENTION SET ACCOUNT_ID=6148435	WHERE INVESTMENT_INTENTION_ID=105;
UPDATE FUND_INVESTMENT_INTENTION SET ACCOUNT_ID=6147853	WHERE INVESTMENT_INTENTION_ID=81;
UPDATE FUND_INVESTMENT_INTENTION SET ACCOUNT_ID=6147336	WHERE INVESTMENT_INTENTION_ID=63;
UPDATE FUND_INVESTMENT_INTENTION SET ACCOUNT_ID=6159650	WHERE INVESTMENT_INTENTION_ID=181;
/
-- Create empty intentions campaign enrolments with multiple accounts.
INSERT INTO FUND_INVESTMENT_INTENTION 
		(INVESTMENT_INTENTION_ID, FUND_CODE, ORGANISATION_ID, PERSON_ID, 
		CAMPAIGN_ID, CASH, UNITS, DATE_ADDED, LAST_UPDATED, 
		LAST_UPDATED_BY, INVINTENTSTATUS_ID, ACCOUNT_ID) 
(
select SEQ_INVESTMENT_INTENTION_ID.NEXTVAL,'PC',o.organisation_id,0,10,0,0,SYSDATE, SYSDATE,'sysadmin',2, a.account_id
from relations r
inner join ref_relation_names rrn on (r.relation_name_id = rrn.relation_name_id)
inner join ref_relation_types rrt on (rrn.relation_type_id = rrt.relation_type_id)
inner join organisation o on (r.element_id1=o.organisation_id) 
inner join account a on (r.element_id2 = a.account_id)
inner join campaign_enrolment ce on (o.organisation_id = ce.organisation_id)
where rrt.relation_type_id = 2 and a.fund_code='PC' and ce.campaign_id=10 and a.account_id not in (
  select min(a.account_id)
  from relations r
  inner join ref_relation_names rrn on (r.relation_name_id = rrn.relation_name_id)
  inner join ref_relation_types rrt on (rrn.relation_type_id = rrt.relation_type_id)
  inner join organisation o on (r.element_id1=o.organisation_id) 
  inner join account a on (r.element_id2 = a.account_id)
  inner join campaign_enrolment ce on (o.organisation_id = ce.organisation_id)
  where rrt.relation_type_id = 2 and a.fund_code='PC' and ce.campaign_id=10 
  group by o.organisation_id, o.organisation_name)
);
/
-- Delete redundant intentions
--DELETE FROM FUND_INVESTMENT_INTENTION WHERE CAMPAIGN_ID IS NULL;
/
--ALTER TABLE FUND_INVESTMENT_INTENTION 
--MODIFY ACCOUNT_ID Number (15,0) NOT NULL;
/
CREATE INDEX ACCOUNT_FUNDINVINT_IX ON FUND_INVESTMENT_INTENTION (ACCOUNT_ID); 
/
ALTER TABLE FUND_INVESTMENT_INTENTION ADD CONSTRAINT ACCOUNT_FUNDINVINT_FK FOREIGN KEY (ACCOUNT_ID) REFERENCES ACCOUNT (ACCOUNT_ID);

-- Update View to display account units and account id
/
CREATE OR REPLACE FORCE VIEW "CCLA"."V_PSDF_ENROLMENT_ACTIVITY_LAST" ("PROCESS_ID", "CAMPAIGN_ACTIVITY_ID", "PERSON_ID", "FULL_NAME", 
"ORGANISATION_ID", "ORG_ACCOUNT_CODE", "INTENTION_UNITS", "INTENTION_STATUS_ID", "INTENTION_STATUS_NAME", "ACCOUNT_ID", 
"ACC_UNITS","USER_ID", "ACTIVITY_DATE", "ACTIVITY_TYPE", "ACTIVITY_ACTION", "CLIENT_STATUS", "PROCESS_STATUS", "PROCESS_DATE", "LAST_ACTION", 
"PROCESS_ENTITY_ID", "CAMPAIGN_ID", "CAMPAIGN_NAME", "ORGANISATION_NAME")
AS
  (SELECT CE.CAMPAIGN_ENROLMENT_ID AS PROCESS_ID,
    CA.CAMPAIGN_ACTIVITY_ID,
    CE.PERSON_ID,
    P.FULL_NAME,
    CE.ORGANISATION_ID,
    ORG.ORG_ACCOUNT_CODE,
    FII.UNITS                   AS INTENTION_UNITS,
    FII.INVINTENTSTATUS_ID      AS INTENTION_STATUS_ID,
    RIIS.INVINTENTSTATUS_NAME   AS INTENTION_STATUS_NAME,
    FII.ACCOUNT_ID,
    ACCV.ACC_UNITS,
    CA.LAST_UPDATED_BY          AS USER_ID,
    CA.DATE_ADDED               AS ACTIVITY_DATE,
    RCAT.CAMPAIGN_ACTIVITY_NAME AS ACTIVITY_TYPE,
    CA.ACTIVITY_DESCRIPTION     AS ACTIVITY_ACTION,
    RCSS.CAMPSUBJECTSTATUS_NAME AS CLIENT_STATUS,
    RCE.ENROLMENT_STATUS_NAME   AS PROCESS_STATUS,
    CE.DATE_ADDED               AS PROCESS_DATE,
    CE.LAST_UPDATED             AS LAST_ACTION,
    CE.ORGANISATION_ID          AS PROCESS_ENTITY_ID,
    CE.CAMPAIGN_ID,
    C.CAMPAIGN_NAME,
    org.ORGANISATION_NAME
  FROM CAMPAIGN_ENROLMENT CE
  INNER JOIN REF_CAMPAIGN_ENROLMENT_STATUS RCE
  ON (CE.ENROLMENT_STATUS_ID = RCE.ENROLMENT_STATUS_ID)
  INNER JOIN REF_CAMPAIGN_SUBJECT_STATUS RCSS
  ON (CE.CAMPSUBJECTSTATUS_ID = RCSS.CAMPSUBJECTSTATUS_ID)
  LEFT JOIN CAMPAIGN_ACTIVITY CA
  ON (CE.CAMPAIGN_ENROLMENT_ID=CA.CAMPAIGN_ENROLMENT_ID)
  LEFT JOIN FUND_INVESTMENT_INTENTION FII
  ON (CE.ORGANISATION_ID = FII.ORGANISATION_ID
  AND FII.CAMPAIGN_ID    =10)
  LEFT JOIN (REF_INVESTMENT_INTENT_STATUS RIIS)
  ON (FII.INVINTENTSTATUS_ID = RIIS.INVINTENTSTATUS_ID)
  LEFT JOIN ACCOUNT_VALUE ACCV ON (FII.ACCOUNT_ID = ACCV.ACCOUNT_ID)
  INNER JOIN REF_CAMPAIGN_ACTIVITY_TYPE RCAT
  ON (CA.CAMPAIGN_ACTIVITY_TYPE_ID = RCAT.CAMPAIGN_ACTIVITY_TYPE_ID)
  INNER JOIN CAMPAIGN C
  ON (CE.CAMPAIGN_ID = C.CAMPAIGN_ID)
  LEFT JOIN PERSON P
  ON (CE.PERSON_ID = P.PERSON_ID)
  LEFT JOIN ORGANISATION org
  ON (CE.ORGANISATION_ID        = org.ORGANISATION_ID)
  WHERE CA.CAMPAIGN_ACTIVITY_ID =
    (SELECT MAX(CAMPAIGN_ACTIVITY_ID)
    FROM CAMPAIGN_ACTIVITY
    WHERE CAMPAIGN_ACTIVITY.CAMPAIGN_ENROLMENT_ID = CE.CAMPAIGN_ENROLMENT_ID
    )
  ); 


