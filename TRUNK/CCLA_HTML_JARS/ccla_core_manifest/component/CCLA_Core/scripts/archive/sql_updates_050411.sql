
-- New column USER_GROUPS
ALTER TABLE REF_ELEMENT_ATTRIBUTES ADD (USER_GROUPS VARCHAR2(100 CHAR));

COMMENT ON COLUMN REF_ELEMENT_ATTRIBUTES.USER_GROUPS IS 'Stores a comma-separated list of Active Directory Security Group names. If non-null for a particular ELEMENT_ATTRIBUTE, a user must be a member of one of the listed groups to allow them to apply the attribute to an element.'; 

-- Update override attributes with access control lists.
UPDATE REF_ELEMENT_ATTRIBUTES SET USER_GROUPS = 'CCLA-Compliance' WHERE ELEMENT_ATTRIBUTE_ID = 6;
UPDATE REF_ELEMENT_ATTRIBUTES SET USER_GROUPS = 'CCLA-Client Service' WHERE ELEMENT_ATTRIBUTE_ID = 5;
