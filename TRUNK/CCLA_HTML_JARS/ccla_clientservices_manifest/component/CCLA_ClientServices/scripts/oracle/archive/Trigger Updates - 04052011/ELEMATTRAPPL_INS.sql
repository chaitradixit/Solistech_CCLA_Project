create or replace
trigger ELEMATTRAPPL_INS
  before insert on element_attribute_applied  
  for each row
declare
  -- local variables here
  /*
  SVN properties
  
  $Rev: 13151 $  
  $Author: tm $           
  $Date: 2011-05-04 19:04:34 +0100 (Wed, 04 May 2011) $
  $HeadURL: svn://eigg/stellent/CLIENTS/CCLA/Database/Central_Database/Triggers/ELEMATTRAPPL_INS.sql $
  */
  
                v_ei_id number(10);
                v_eia_id   int;
begin
  --check if an identifier is required
                select ea.element_identifier_id
                into   v_ei_id
                from   ref_element_attributes ea
                where  ea.element_attribute_id = :new.element_attribute_id;
                
                if v_ei_id is not null and :new.attribute_status = 1 then
                  
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
