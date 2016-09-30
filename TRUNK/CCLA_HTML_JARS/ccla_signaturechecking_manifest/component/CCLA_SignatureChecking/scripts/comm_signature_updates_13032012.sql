-- ********* COMM_SIGNATURE table **********
--Add DOC_GUID column to COMM table
ALTER TABLE COMM_SIGNATURE
ADD DOC_GUID varchar(20);

-- Now update the comm table with the new guids
UPDATE COMM_SIGNATURE ps
SET ps.DOC_GUID = (
  SELECT (r.dDocName  || ':' ||  r.drevisionid) AS NEW_DOC_GUID
  FROM REVISIONS r 
  where (ps.doc_id = r.did)
  group by r.dDocName  || ':' ||  r.drevisionid
); 
