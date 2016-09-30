

-- Add Companies House as a verification source
INSERT INTO REF_ELEMENT_ATTRIBUTES VALUES (9, 'Verified by Companies House', 1, null, 1, 4, 3);

-- Rename 'Company Ref' identifier to 'Company Reg. No'
UPDATE REF_ELEMENT_IDENTIFIERS SET IDENTIFIER_NAME = 'Company Reg. No' WHERE ELEMENT_IDENTIFIER_ID = 3;

-- Diocese Dir/Page No. identifier
INSERT INTO REF_ELEMENT_IDENTIFIERS VALUES (6, 1, 'Diocese Dir/Page No');

-- 'Letter of Authenticity' verification source and attribute
INSERT INTO REF_VERIFICATION_SOURCE VALUES (12, 'Letter of Authenticity', 'A letter from a trusted source, vouching for this entity', 1, 0);
INSERT INTO REF_ELEMENT_ATTRIBUTES VALUES (10, 'Verified by Letter', 1, null, 1, 12, null);

-- Fix typo in column name
ALTER TABLE REF_VERIFICATION_SOURCE RENAME COLUMN SUPPORTING_DOC_REQURIED TO SUPPORTING_DOC_REQUIRED;

-- Add 'Property Type' column
ALTER TABLE REF_PROPERTY ADD (PROPERTY_TYPE VARCHAR2(10 CHAR));

-- Insert values for 'Property Type'
UPDATE REF_PROPERTY SET PROPERTY_TYPE = 'STRING' WHERE PROPERTY_ID = 3;
UPDATE REF_PROPERTY SET PROPERTY_TYPE = 'FLAG' WHERE PROPERTY_ID = 1;
UPDATE REF_PROPERTY SET PROPERTY_TYPE = 'BOOL' WHERE PROPERTY_ID IN (4,2);

-- Add NN constraint
ALTER TABLE REF_PROPERTY MODIFY (PROPERTY_TYPE VARCHAR2(10 CHAR) CONSTRAINT PROP_PROPTYPE_NN NOT NULL);

-- Add 'Singleton' column
ALTER TABLE REF_PROPERTY ADD (IS_SINGLETON NUMBER(1,0));
UPDATE REF_PROPERTY SET IS_SINGLETON = 0;
UPDATE REF_PROPERTY SET IS_SINGLETON = 1 WHERE PROPERTY_ID = 1;

ALTER TABLE REF_PROPERTY MODIFY (IS_SINGLETON NUMBER(1,0) CONSTRAINT PROP_ISSINGLETON_NN NOT NULL);


-- Run as CCLA
create or replace trigger ELEMATTRAPPL_INS
  before insert on element_attribute_applied  
  for each row
declare
  -- local variables here
	v_ei_id number(10);
	v_eia_id   int;
begin
  --check if an identifier is required
	select ea.element_identifier_id
	into   v_ei_id
	from   ref_element_attributes ea
	where  ea.element_attribute_id = :new.element_attribute_id;
	
	if v_ei_id is not null then
	  
		--check that the appropriate identifier exists
		select count(*)
		into   v_eia_id
		from   element_identifiers_applied eia
		where  eia.element_id = :new.element_id
		and    eia.element_identifier_id = v_ei_id;
		
		if v_eia_id = 0 then
		  RAISE_APPLICATION_ERROR(-20000, 'The element does not have the appropriate element identifier');
		end if;
		
	end if;
end ELEMATTRAPPL_INS;
/
create or replace trigger ELEMATTRAPPL_UPD

  before update on element_attribute_applied  
  for each row
declare
  -- local variables here
  v_ei_id number(10);
  v_eia_id   int;
begin
  --check if an identifier is required
  select ea.element_identifier_id
  into   v_ei_id
  from   ref_element_attributes ea
  where  ea.element_attribute_id = :new.element_attribute_id;
  
  if v_ei_id is not null then
    
    --check that the appropriate identifier exists
    select count(*)
    into   v_eia_id
    from   element_identifiers_applied eia
    where  eia.element_id = :new.element_id
    and    eia.element_identifier_id = v_ei_id;
    
    if v_eia_id = 0 then
      RAISE_APPLICATION_ERROR(-20000, 'The element does not have the appropriate element identifier');
    end if;
    
  end if;
end ELEMATTRAPPL_UPD;
/

-- Run as CCLA
create or replace trigger ELEMIDAPPL_DEL
  before delete on element_identifiers_applied  
  for each row
declare
  -- local variables here
  v_ei_id number(10);
  v_eia_id   int;
begin
  --check if an attribute is attached
  select ea.element_attribute_id
  into   v_ei_id
  from   ref_element_attributes ea
  where  ea.element_identifier_id = :old.element_identifier_id;
	
  if v_ei_id is not null then
    
    --check if attributes have been linked
    select count(*)
    into   v_eia_id
    from   element_attribute_applied eaa
    where  eaa.element_id = :old.element_id
    and    eaa.element_attribute_id = v_ei_id;
		
    if v_eia_id <> 0 then
      RAISE_APPLICATION_ERROR(-20000, 'The identifier cannot be deleted, a dependant element attribute exists');
    end if;
    
  end if;
end ELEMIDAPPL_DEL;
/
