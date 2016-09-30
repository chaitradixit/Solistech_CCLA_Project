
update ref_relation_names set relation='Signatory', short_name='Sig' where relation='Ordinary Signatory';

update ref_relation_names set relation='Trustee', short_name='Trst' where relation='Ordinary Trustee';

/* FIND OUT IF THERE ARE ANY BENEFICIARIES SET, IF SO THEN MAKE A NOTE OF THEM AND THEN REMOVE THEM*/

select * from relations where relation_name_id in (21,85)

update ref_relation_names set relation='Director', short_name='Dir' where relation='Beneficiary';

/* MOVE AUTH SIG TO SIG*/
UPDATE RELATIONS SET RELATION_NAME_ID = 5 WHERE RELATION_NAME_ID=4
UPDATE RELATIONS SET RELATION_NAME_ID =84 WHERE RELATION_NAME_ID=83

/* MAKE SURE THERE ARE NONE LEFT */
SELECT * FROM RELATIONS WHERE RELATION_NAME_ID IN (4,83);

/* REMOVE THEM*/
DELETE FROM REF_RELATION_NAMES WHERE RELATION_NAME_ID IN (4,83);