-- Fix typo in Account Type
UPDATE REF_ACCOUNT_TYPE SET ACCOUNT_TYPE_NAME = 'Unitised' WHERE ACCOUNT_TYPE_NAME = 'Unitized';
-- Fix typo in Fund Type Code
UPDATE REF_FUND_TYPECODE SET TYPECODE_DESC = 'Unitised' WHERE FUND_TYPECODE = 'UNI';