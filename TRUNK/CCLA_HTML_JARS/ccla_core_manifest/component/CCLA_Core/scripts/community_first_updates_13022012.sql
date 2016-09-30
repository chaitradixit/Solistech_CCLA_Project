-- Extra schema/data updates required for Community First

-- New Campaign Enrolment Action
INSERT INTO REF_CAMPAIGN_ENROLMENT_ACTION VALUES (26, 'Generate on-boarding covering letter', null);

-- Link action to Comm First campaign
INSERT INTO ENROLMENT_STATUS_ACTION_APPL VALUES (13, 1, 26);

-- New Form Type
INSERT INTO REF_FORM_TYPE VALUES (20, 'Community First On-Boarding', 46, 46, 'com.ecs.stellent.ccla.clientservices.form.CommunityFirstOnBoardingCoveringLetterFormHandler', 'CF-OB-COVLET', 1, 1);

-- Amend previous Form Type name to remove 'Form'
UPDATE REF_FORM_TYPE SET FORM_TYPE_NAME = 'Community First Donor Subscription' WHERE FORM_TYPE_ID = 19;