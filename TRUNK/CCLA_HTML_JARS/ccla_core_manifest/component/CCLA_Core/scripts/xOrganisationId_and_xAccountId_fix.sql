-- Run this script AFTER adding the new OrganisationId/AccountId metadata attributes.

-- Remove zeroes added by UCM
UPDATE DOCMETA SET XORGANISATIONID = NULL, XACCOUNTID = NULL;

-- Add FK constraints
alter table DOCMETA
  add constraint FK_DOCMETA_ORGID foreign key (XORGANISATIONID)
  references ccla.organisation (ORGANISATION_ID);
  
alter table DOCMETA
  add constraint FK_DOCMETA_ACCID foreign key (XACCOUNTID)
  references ccla.account (ACCOUNT_ID);
