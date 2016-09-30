

ALTER TABLE COMM DROP CONSTRAINT UCM_DOCUMENT_COMM_FK;

-- ********* COMM TABLE **********
--Add DOC_GUID column to COMM table
ALTER TABLE COMM
ADD DOC_GUID varchar(20);

-- Now update the comm table with the new guids
UPDATE COMM c
SET c.DOC_GUID = (
  SELECT (r.dDocName  || ':' ||  r.drevisionid) AS NEW_DOC_GUID
  FROM REVISIONS r 
  where (c.doc_id = r.did)
  group by r.dDocName  || ':' ||  r.drevisionid
); 

-- Finally drop the old doc_id row
ALTER TABLE COMM
DROP COLUMN DOC_ID;


-- ********* INSTRUCTION table **********

ALTER TABLE INSTRUCTION DROP CONSTRAINT REV_INSTR_FK;

--Add DOC_GUID column to INSTRUCTION table
ALTER TABLE INSTRUCTION
ADD INSTRUCTION_DOC_GUID varchar(20);

-- Now update the comm table with the new guids
UPDATE INSTRUCTION i
SET i.INSTRUCTION_DOC_GUID = (
  SELECT (r.dDocName  || ':' ||  r.drevisionid) AS NEW_DOC_GUID
  FROM REVISIONS r 
  where (i.instruction_doc_id = r.did)
  group by r.dDocName  || ':' ||  r.drevisionid
); 

-- Finally drop the old doc_id row
ALTER TABLE INSTRUCTION
DROP COLUMN INSTRUCTION_DOC_ID;
