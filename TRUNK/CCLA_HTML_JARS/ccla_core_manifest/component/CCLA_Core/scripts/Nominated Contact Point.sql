-- Run the below as WCC user.

-- If this fails you may need to re-dim the sequence, using SELECT MAX(FIELD_ID) FROM REF_DUAL_INDEX_FIELDS as a starting seed.
INSERT INTO REF_DUAL_INDEX_FIELDS VALUES (SEQ_DUAL_INDEX_FIELD.NEXTVAL, 'xNominatedContactPoint');

-- Should insert 6 rows.
INSERT INTO DUAL_INDEX_RULE_FIELDS 
SELECT DUAL_INDEX_RULE_ID, FIELD_ID, SYSDATE
FROM DUAL_INDEX_RULES, REF_DUAL_INDEX_FIELDS 
WHERE DOCUMENT_CLASS IN ('APP','APPSHORT','AUTOAPP','AUTOMAND','MAND','MANDSHORT')
AND UCM_FIELD_NAME = 'xNominatedContactPoint';

-- Run the below as CCLA user.
INSERT INTO REF_INSTRUCTION_DATA VALUES (67, 'NOMINATED_CONTACT_ID', 'INT', 
'Nom. Contact Point', 'Nominated address contact for the account''s correspondent');

INSERT INTO REF_UCM_METADATA_TRANSLATION VALUES (67, 'xNominatedContactPoint', null);

-- Should insert 6 rows
INSERT INTO APPLICABLE_INSTRUCTION_DATA (APPLICABLE_INSTRUCTION_DATA_ID, INSTRUCTION_DATA_ID, INSTRUCTION_TYPE_ID, IS_OPTIONAL)
SELECT SEQ_APPLICABLE_INSTR_DATA.NEXTVAL, 67, INSTRUCTION_TYPE_ID, 0 FROM
REF_INSTRUCTION_TYPE WHERE INSTRUCTION_TYPE_NAME IN ('APP','APPSHORT','AUTOAPP','AUTOMAND','MAND','MANDSHORT');