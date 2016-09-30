-- Add metadata translation field for DEST_ACCOUNT_ID 
Insert into REF_UCM_METADATA_TRANSLATION (INSTRUCTION_DATA_ID, UCM_FIELD_NAME, TRANSLATION_HANDLER_CLASS)
values (6,NULL,'com.ecs.ucm.ccla.commproc.translation.DestAccountIdFieldHandler');