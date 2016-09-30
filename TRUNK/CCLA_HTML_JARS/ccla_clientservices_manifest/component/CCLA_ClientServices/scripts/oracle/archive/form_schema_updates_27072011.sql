-- Adds new ref column to REF_FORM_TYPE.

ALTER TABLE REF_FORM_TYPE ADD (
   ELEMENT_TYPE_ID Number(10,0)
)
/

COMMENT ON COLUMN REF_FORM_TYPE.ELEMENT_TYPE_ID IS 'If set, this determines which Element Type this Form Type can be generated for.

E.g. an Account Subscription form would be mapped to the ''Account'' Element Type. An Application form would be mapped to the ''Organisation'' Element Type.'
/

CREATE INDEX ELEMTYPE_FORMTYPE_IX ON REF_FORM_TYPE (ELEMENT_TYPE_ID) 
/
ALTER TABLE REF_FORM_TYPE ADD CONSTRAINT ELEMTYPE_FORMTYPE_FK FOREIGN KEY (ELEMENT_TYPE_ID) REFERENCES REF_ELEMENT_TYPE (ELEMENT_TYPE_ID)
/

-- Set all Element Type IDs to 'Account' for all Form Types, apart from the Blank/Web Forms.
UPDATE REF_FORM_TYPE SET ELEMENT_TYPE_ID = 3 WHERE FORM_TYPE_NAME NOT LIKE '%Blank %';