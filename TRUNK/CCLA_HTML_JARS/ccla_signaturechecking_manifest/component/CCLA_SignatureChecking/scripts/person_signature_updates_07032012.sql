-- ********* PERSON_SIGNATURE table **********
--Add DOC_GUID column to COMM table
ALTER TABLE PERSON_SIGNATURE
ADD DOC_GUID varchar(20);

-- Now update the comm table with the new guids
UPDATE PERSON_SIGNATURE ps
SET ps.DOC_GUID = (
  SELECT (r.dDocName  || ':' ||  r.drevisionid) AS NEW_DOC_GUID
  FROM REVISIONS r 
  where (ps.source_doc_id = r.did)
  group by r.dDocName  || ':' ||  r.drevisionid
); 

-- Finally drop the old doc_id row
ALTER TABLE PERSON_SIGNATURE
DROP COLUMN SOURCE_DOC_ID;
