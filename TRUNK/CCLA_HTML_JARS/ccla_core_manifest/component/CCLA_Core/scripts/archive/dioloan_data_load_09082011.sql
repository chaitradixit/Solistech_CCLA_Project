-- New Campaign
INSERT INTO CAMPAIGN VAlUES (11, 
'Diocesan Loans', 
'Issuing of loans to the 43 Diocese, with capital being sourced from the CBF Deposit Fund. Requires setting up a loan account for each Diocese taking part. Each Diocese may withdraw up to £1 million.',
'sysadmin',
TO_DATE('01/07/2011', 'dd/mm/yyyy'),
TO_DATE('01/07/2015', 'dd/mm/yyyy'),
TO_DATE('01/07/2011', 'dd/mm/yyyy'),
TO_DATE('01/07/2015', 'dd/mm/yyyy'),
1,
'com.ecs.stellent.ccla.clientservices.campaign.DioLoanEnrolmentHandler',
'dioloan',
SYSDATE, SYSDATE, 'sysadmin', 1);

-- New form type (Dio. Loan App Form)
REM INSERTING into REF_FORM_TYPE
Insert into REF_FORM_TYPE (FORM_TYPE_ID,FORM_TYPE_NAME,GEN_INSTRUCTION_TYPE_ID,RET_INSTRUCTION_TYPE_ID,FORM_HANDLER_CLASS,FORM_TYPE_SHORTNAME,IS_SINGLETON,ELEMENT_TYPE_ID) 
values (12,'Diocesan Loan Application Form',8,8,'com.ecs.stellent.ccla.clientservices.form.DioLoanRegistrationFormHandler','DIOL-APP',1,3);

-- TODO: Drawdown/Redemption Form
REM INSERTING into REF_FORM_TYPE
Insert into REF_FORM_TYPE (FORM_TYPE_ID,FORM_TYPE_NAME,GEN_INSTRUCTION_TYPE_ID,RET_INSTRUCTION_TYPE_ID,FORM_HANDLER_CLASS,FORM_TYPE_SHORTNAME,IS_SINGLETON,ELEMENT_TYPE_ID) 
values (13,'Diocesan Loan Draw-Down Form',106,106,'com.ecs.stellent.ccla.clientservices.form.DioLoanAccountFormHandler','DIOL-DRAWD',0,3);
Insert into REF_FORM_TYPE (FORM_TYPE_ID,FORM_TYPE_NAME,GEN_INSTRUCTION_TYPE_ID,RET_INSTRUCTION_TYPE_ID,FORM_HANDLER_CLASS,FORM_TYPE_SHORTNAME,IS_SINGLETON,ELEMENT_TYPE_ID) 
values (14,'Diocesan Loan Redemption Form',49,49,'com.ecs.stellent.ccla.clientservices.form.DioLoanAccountFormHandler','DIOL-REDEMPT',0,3);

-- Update form handler classes
UPDATE REF_FORM_TYPE SET FORM_HANDLER_CLASS = 'com.ecs.stellent.ccla.clientservices.form.PSDFAccountFormHandler'
WHERE FORM_HANDLER_CLASS = 'com.ecs.stellent.ccla.clientservices.form.AccountFormHandler';

-- Remove 'Form' from all Form Type names
UPDATE REF_FORM_TYPE SET FORM_TYPE_NAME = REGEXP_REPLACE(FORM_TYPE_NAME, ' Form', '');