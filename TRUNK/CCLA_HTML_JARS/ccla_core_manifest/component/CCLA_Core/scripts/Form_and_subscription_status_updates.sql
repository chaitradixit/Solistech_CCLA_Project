-- Remove 'Generate Info Form' action from CBF Client Info campaign
DELETE from enrolment_status_action_appl WHERE CAMPAIGN_ID = 14 AND ENROLMENT_ACTION_ID = 27;

-- New sys config var
INSERT INTO SYSTEM_CONFIG_VAR VALUES
('DocMeta_PaymentRefCheckOnDocUpdate', 'Whether or not documents with a Deposit type and Payment Ref set will be checked against their Corresponding Subscription amount. For exact matches, the Subscription Status is updated to Cash Received', 'BOOL', null, null, null, null, 0, SYSDATE, 'weblogic', 'DOC_UPDATE');

-- New Form Statuses
INSERT INTO REF_FORM_STATUS (FORM_STATUS_ID, FORM_STATUS_NAME, FORM_STATUS_DESCRIPTION)
VALUES (9, 'Validated', 'Returned form was explicitly marked as valid by a user');

INSERT INTO REF_FORM_STATUS (FORM_STATUS_ID, FORM_STATUS_NAME, FORM_STATUS_DESCRIPTION)
VALUES (10, 'Invalidated', 'Returned form was explicitly marked as invalid by a user');

INSERT INTO REF_FORM_STATUS (FORM_STATUS_ID, FORM_STATUS_NAME, FORM_STATUS_DESCRIPTION)
VALUES (11, 'Indexed', 'Returned form was indexed by a user');

-- New Subscription Statuses
INSERT INTO REF_SUBSCRIPTION_STATUS VALUES (8, 'Form dispatched');
INSERT INTO REF_SUBSCRIPTION_STATUS VALUES (9, 'Partial cash received');
INSERT INTO REF_SUBSCRIPTION_STATUS VALUES (10, 'Excess cash received');
INSERT INTO REF_SUBSCRIPTION_STATUS VALUES (11, 'Different cash received');
INSERT INTO REF_SUBSCRIPTION_STATUS VALUES (12, 'Client confirmed');
INSERT INTO REF_SUBSCRIPTION_STATUS VALUES (13, 'Client confirmation invalid');
INSERT INTO REF_SUBSCRIPTION_STATUS VALUES (14, 'Pending investment decision');

UPDATE REF_SUBSCRIPTION_STATUS SET SUBSCRIPTION_STATUS_NAME = 'Cash received' WHERE SUBSCRIPTION_STATUS_ID = 7;

-- Add new IS_VALID boolean column. Defaults to NULL, only set explicitly valid/invalid by a user.
ALTER TABLE FORM ADD (
  IS_VALID NUMBER(1,0)
);

-- Add new CLIENT_CONFIRMED boolean column.
ALTER TABLE SUBSCRIPTION ADD (
  CLIENT_CONFIRMED NUMBER(1,0)
);
