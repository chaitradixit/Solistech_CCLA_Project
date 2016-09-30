-- Extra schema/data updates required for Community First

-- New Name Title Category and 'Anonymous' Category
INSERT INTO REF_TITLE_CATEGORY VALUES (7, 'Special');
INSERT INTO REF_TITLE VALUES (64, null, 'Anonymous',7,'Anonymous',null);

-- Add new 'Anonymous Donor' Org Category Name/Category
INSERT INTO REF_ORG_CATEGORY_NAME VALUES (307, 'Anonymous Donor');
INSERT INTO REF_ORG_CATEGORY VALUES (1057, null, 'None', null, 307);

-- Add missing RegExp constraint to PERSON_ACCOUNT_CODE
ALTER TABLE PERSON ADD (
  CONSTRAINT PERS_PERSACCCODE_CHK CHECK (REGEXP_LIKE(PERSON_ACCOUNT_CODE, '^\S{4}\d{8}$')) ENABLE
);

-- New Form Type
REM INSERTING into REF_FORM_TYPE
Insert into REF_FORM_TYPE 
(FORM_TYPE_ID,FORM_TYPE_NAME,GEN_INSTRUCTION_TYPE_ID,RET_INSTRUCTION_TYPE_ID,FORM_HANDLER_CLASS,FORM_TYPE_SHORTNAME,IS_SINGLETON,ELEMENT_TYPE_ID) 
values (19,'Community First Donor Subscription Form',84,84,'com.ecs.stellent.ccla.clientservices.form.CommunityFirstDonorSubscriptionFormHandler','CF-DONOR-SUBS',1,1);
