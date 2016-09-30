-- New Instruction Data field: Parent Doc GUID.
INSERT INTO REF_INSTRUCTION_DATA VALUES (66, 'PARENT_DOC_GUID', 'STRING', 'Parent Doc GUID', 
'Doc GUID for the instruction document''s parent document, if one exists, otherwise its a direct reference to the Instruction Doc GUID');
-- Add translator.
INSERT INTO REF_UCM_METADATA_TRANSLATION VALUES (66, null, 'com.ecs.ucm.ccla.commproc.translation.ParentDocGUIDFieldHandler');

-- Add instruction type mappings - only INV instruction at the moment.
INSERT INTO APPLICABLE_INSTRUCTION_DATA VALUES (SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 66, 64, 0);