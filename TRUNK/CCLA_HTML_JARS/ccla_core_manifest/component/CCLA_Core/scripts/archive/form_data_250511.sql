
-- Add new PSDF Additional Account opening form
REM INSERTING into REF_FORM_TYPE
Insert into REF_FORM_TYPE (FORM_TYPE_ID,FORM_TYPE_NAME,GEN_INSTRUCTION_TYPE_ID,RET_INSTRUCTION_TYPE_ID,FORM_HANDLER_CLASS,FORM_TYPE_SHORTNAME,IS_SINGLETON) 
values (6,'PSDF Additional Account Form',8,8,'com.ecs.stellent.ccla.clientservices.form.PSDFRegistrationFormHandler','PSDF-ADD',1);
