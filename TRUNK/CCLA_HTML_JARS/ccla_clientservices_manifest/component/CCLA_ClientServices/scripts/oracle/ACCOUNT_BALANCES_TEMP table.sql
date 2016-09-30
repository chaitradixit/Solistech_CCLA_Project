-- =========================
-- ONLY RUN IN DEV/UAT!

-- This will work in dev/UAT. DON'T run in live, use the script below instead!
-- This simply copies the out-of-date values from the ACCOUNT_VALUE table into
-- the new temp table.
CREATE TABLE ACCOUNT_BALANCES_TEMP AS
SELECT ab.ACCOUNT_ID, acc.ORGANISATION_ID, ab.ACC_CASH, ab.ACC_UNITS
FROM ACCOUNT_VALUE ab
INNER JOIN V_ACCOUNT_EXTENDED_CLIENT acc ON (ab.ACCOUNT_ID = acc.ACCOUNT_ID);

-- =========================
-- ONLY RUN IN LIVE! (Already done, 03/09/2012)

-- Below script will extract data from Stephen's table in the ETL_USER schema 
-- (aurora_account_balances_temp) and use it to create a new table in the CCLA
-- schema (ACCOUNT_BALANCES_TEMP)
CREATE TABLE ACCOUNT_BALANCES_TEMP AS
SELECT acc.ACCOUNT_ID, clMap.ORGANISATION_ID, accBal.ACC_CASH, accBal.ACC_UNITS 
FROM aurora_account_balances_temp accBal

INNER JOIN FUND f ON (accbal.acc_fundcode = f.FUND_CODE)

INNER JOIN CLIENT_AURORA_MAP clMap ON (
  accBal.ACC_CNTTYPCDE = clMap.CONTRIBUTER_TYPE_CODE
  AND
  accBal.ACC_SUBDIVISIONCODE = clMap.SUBDIVISION_CODE
  AND
  accBal.ACC_CONTRIBUTORCODE = clMap.CONTRIBUTER_CODE
  AND
  f.COMPANY_ID = clMap.COMPANY_ID
)

INNER JOIN V_ACCOUNT_EXTENDED_CLIENT acc ON (
  clMap.ORGANISATION_ID = acc.ORGANISATION_ID
  AND
  accBal.ACC_FUNDCODE = acc.FUND_CODE
  AND
  accbal.acc_accountnumber = acc.ACCOUNTNUMBER
);