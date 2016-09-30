
-- New Account attribute
INSERT INTO REF_ELEMENT_ATTRIBUTES VALUES 
(49, 'No Mandate since Oct 16th 2010', 3, 'No mandate document received for this account since October 16th, 2010', 0, null, null, null, 'BOOL', 5);

--Add new Account attribute to all accounts that don't have a Mandate received since Oct 16th 2010.
INSERT INTO ELEMENT_ATTRIBUTE_APPLIED 
(ELEMENT_ID, ELEMENT_ATTRIBUTE_ID, DATE_ADDED, ATTRIBUTE_STATUS, SUPPORTING_DOC_ID, LAST_UPDATED, LAST_UPDATED_BY, ATTRIBUTE_VALUE) 

SELECT ACCOUNT_ID, 49, SYSDATE, 1, NULL, SYSDATE, 'System', NULL 
FROM ACCOUNT_EXTENDED_CLIENT WHERE WHERE ACCOUNT_STATUS_ID <> 2 AND ACCOUNT_ID NOT IN (

  SELECT acc.ACCOUNT_ID FROM ACCOUNT_EXTENDED_CLIENT acc INNER JOIN (
    
    SELECT * FROM (
    
      SELECT UPPER(xFund) AS FUND_CODE,
      TO_NUMBER(SUBSTR(xAccountNumber, 0, LENGTH(xAccountNumber) -  LENGTH(XFUND))) AS ACCOUNTNUMBER,
      TO_NUMBER(xClientNumber) AS CLIENT_NUMBER FROM (
      
        SELECT xClientNumber, xAccountNumber, dInDate, xFund
        FROM Revisions r INNER JOIN DocMeta dm ON (r.dID = dm.dID AND r.dRevRank = 0 AND r.dStatus = 'RELEASED')
        WHERE xDocumentClass IN ('MAND', 'MANDSHORT', 'AUTOMAND')
        AND dInDate >= TO_DATE('16/10/2010', 'dd/mm/yyyy')
        AND REGEXP_LIKE(xAccountNumber, '^[[:digit:]]+[[:alpha:]]+$')
        AND REGEXP_LIKE(xClientNumber, '^[[:digit:]]+$')
        AND REGEXP_LIKE((SUBSTR(xAccountNumber, 0, LENGTH(xAccountNumber) -  LENGTH(XFUND))), '^[[:digit:]]+$')
        AND xClientNumber IS NOT NULL
        AND xAccountNumber IS NOT NULL 
        AND xFund IS NOT NULL
        AND LENGTH(xAccountNumber) > LENGTH(xFund)
      
      ) newMandDocs
      
    ) newMandDocs WHERE REGEXP_LIKE(ACCOUNTNUMBER, '^[[:digit:]]+$')
    
  ) newMandAccs ON  (newMandAccs.CLIENT_NUMBER = ACC.CLIENT_NUMBER 
                    AND newMandAccs.FUND_CODE = ACC.FUND_CODE 
                    AND newMandAccs.ACCOUNTNUMBER = ACC.ACCOUNTNUMBER)
);