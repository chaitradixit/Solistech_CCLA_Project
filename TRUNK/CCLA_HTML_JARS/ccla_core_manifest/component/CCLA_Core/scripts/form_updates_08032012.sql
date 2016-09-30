-- ********* FORM table **********

--Add GEN_DOC_GUID, RET_DOC_GUID column to COMM table
ALTER TABLE FORM
ADD GEN_DOC_GUID varchar(20);

ALTER TABLE FORM
ADD RET_DOC_GUID varchar(20);

-- Now update the FORM table with the new guids 
-- *** NO NEED TO RUN THIS SCRIPT FOR GEN_DOC_ID as it is not used
UPDATE FORM f
SET f.RET_DOC_GUID = (
  SELECT (r.dDocName  || ':' ||  r.drevisionid) AS NEW_DOC_GUID
  FROM REVISIONS r 
  where (f.RET_DOC_ID = r.did)
  group by r.dDocName  || ':' ||  r.drevisionid
); 


--Finally drop the old GEN_DOC_ID, RET_DOC_ID column from the FORM  table
ALTER TABLE FORM
DROP COLUMN GEN_DOC_ID;

ALTER TABLE FORM
DROP COLUMN RET_DOC_ID;
