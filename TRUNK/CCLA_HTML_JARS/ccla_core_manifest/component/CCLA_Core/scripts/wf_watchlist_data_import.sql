CREATE TABLE WF_WATCHLIST_DATA_TEMP (
  CLIENT_NUMBER INTEGER,
  ACCOUNTNUMBER VARCHAR2(10 CHAR),
  WATCHLIST_REASONS VARCHAR2(1000 CHAR)
);

-- Links Watchlist Account data to Central DB Account data
SELECT acc.ACCOUNT_ID, wl.WATCHLIST_REASONS FROM (
  SELECT wl.*, 
  SUBSTR(wl.ACCOUNTNUMBER, 1, (REGEXP_INSTR(wl.ACCOUNTNUMBER, '\D')-1)) AS WL_ACCOUNTNUMBER,
   UPPER(SUBSTR(wl.ACCOUNTNUMBER, REGEXP_INSTR(wl.ACCOUNTNUMBER, '\D'))) AS WL_FUND_CODE
  FROM WF_WATCHLIST_DATA_TEMP wl
) wl 

INNER JOIN V_ACCOUNT_EXTENDED_CLIENT acc ON (
  wl.CLIENT_NUMBER = acc.CLIENT_NUMBER AND 
  wl.WL_ACCOUNTNUMBER = acc.ACCOUNTNUMBER AND
  wl.WL_FUND_CODE = acc.FUND_CODE
);

-- Identifies all watchlist reasons that can't be linked to a specific Central DB Account Flag
SELECT * FROM WF_WATCHLIST_DATA_TEMP
WHERE WATCHLIST_REASONS NOT IN (
  SELECT WATCHLIST_REASONS wl FROM WF_WATCHLIST_DATA_TEMP wl
  INNER JOIN REF_ACCOUNT_FLAG af ON (INSTR(wl.WATCHLIST_REASONS,af.ACCOUNT_FLAG_NAME) > 0)
);

-- Accounts requiring 'CS Request' flag (WATCHLIST_REASONS string is blank/null)
SELECT ACCOUNT_ID, 7 AS ACCOUNT_FLAG_ID, 'CS Request' AS ACCOUNT_FLAG_NAME FROM
(
  SELECT acc.ACCOUNT_ID FROM (
    SELECT wl.*, 
    SUBSTR(wl.ACCOUNTNUMBER, 1, (REGEXP_INSTR(wl.ACCOUNTNUMBER, '\D')-1)) AS WL_ACCOUNTNUMBER,
    UPPER(SUBSTR(wl.ACCOUNTNUMBER, REGEXP_INSTR(wl.ACCOUNTNUMBER, '\D'))) AS WL_FUND_CODE
    FROM WF_WATCHLIST_DATA_TEMP wl
  ) wl 
  
  INNER JOIN V_ACCOUNT_EXTENDED_CLIENT acc ON (
    wl.CLIENT_NUMBER = acc.CLIENT_NUMBER AND 
    wl.WL_ACCOUNTNUMBER = acc.ACCOUNTNUMBER AND
    wl.WL_FUND_CODE = acc.FUND_CODE
  )
  
  WHERE wl.WATCHLIST_REASONS IS NULL
  
  AND ACCOUNT_ID NOT IN (
    SELECT ACCOUNT_ID FROM ACCOUNT_FLAG_APPLIED WHERE ACCOUNT_FLAG_ID = 7
  )
);

-- Accounts and their required flags
SELECT ACCOUNT_ID, ACCOUNT_FLAG_ID, ACCOUNT_FLAG_NAME FROM
(
  SELECT acc.ACCOUNT_ID, wl.ACCOUNT_FLAG_ID, wl.ACCOUNT_FLAG_NAME FROM (
    SELECT wl.CLIENT_NUMBER, 
    SUBSTR(wl.ACCOUNTNUMBER, 1, (REGEXP_INSTR(wl.ACCOUNTNUMBER, '\D')-1)) AS WL_ACCOUNTNUMBER,
    UPPER(SUBSTR(wl.ACCOUNTNUMBER, REGEXP_INSTR(wl.ACCOUNTNUMBER, '\D'))) AS WL_FUND_CODE,
    af.ACCOUNT_FLAG_ID,
    af.ACCOUNT_FLAG_NAME
    FROM WF_WATCHLIST_DATA_TEMP wl
    INNER JOIN REF_ACCOUNT_FLAG af ON (INSTR(wl.WATCHLIST_REASONS,af.ACCOUNT_FLAG_NAME) > 0)
    WHERE wl.WATCHLIST_REASONS IS NOT NULL
  ) wl 
  
  INNER JOIN V_ACCOUNT_EXTENDED_CLIENT acc ON (
    wl.CLIENT_NUMBER = acc.CLIENT_NUMBER AND 
    wl.WL_ACCOUNTNUMBER = acc.ACCOUNTNUMBER AND
    wl.WL_FUND_CODE = acc.FUND_CODE
  )

  AND ACCOUNT_ID NOT IN (
    SELECT ACCOUNT_ID FROM ACCOUNT_FLAG_APPLIED afa WHERE afa.ACCOUNT_FLAG_ID = wl.ACCOUNT_FLAG_ID
  )
);
