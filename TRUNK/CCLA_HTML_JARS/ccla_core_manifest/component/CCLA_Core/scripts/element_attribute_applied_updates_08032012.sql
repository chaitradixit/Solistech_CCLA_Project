-- ********* ELEMENT_ATTRIBUTE_APPLIED TABLE **********

--Add DOC_GUID column to ELEMENT_ATTRIBUTE_APPLIED table
ALTER TABLE ELEMENT_ATTRIBUTE_APPLIED
ADD DOC_GUID varchar(20);

-- Now update the ELEMENT_ATTRIBUTE_APPLIED table with the new guids
UPDATE ELEMENT_ATTRIBUTE_APPLIED eaa
SET eaa.DOC_GUID = (
  SELECT (r.dDocName  || ':' ||  r.drevisionid) AS NEW_DOC_GUID
  FROM REVISIONS r 
  where (eaa.supporting_doc_id = r.did)
  group by r.dDocName  || ':' ||  r.drevisionid
); 

-- Finally drop the old doc_id row
ALTER TABLE ELEMENT_ATTRIBUTE_APPLIED
DROP COLUMN SUPPORTING_DOC_ID;

