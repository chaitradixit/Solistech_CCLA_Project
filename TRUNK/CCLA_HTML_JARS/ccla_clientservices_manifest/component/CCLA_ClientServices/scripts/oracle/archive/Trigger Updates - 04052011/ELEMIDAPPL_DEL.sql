create or replace
trigger ELEMIDAPPL_DEL
  before delete on element_identifiers_applied  
  for each row
declare
  -- local variables here
    /*
  SVN properties
  
  $Rev: 13151 $  
  $Author: tm $           
  $Date: 2011-05-04 19:04:34 +0100 (Wed, 04 May 2011) $
  $HeadURL: svn://eigg/stellent/CLIENTS/CCLA/Database/Central_Database/Triggers/ELEMIDAPPL_DEL.sql $
  */
  
  v_ei_id number(10);
  v_eia_id   int;
begin
  --check if an attribute is attached
  select count(*)
  into   v_ei_id
  from   ref_element_attributes ea
  where  ea.element_identifier_id = :old.element_identifier_id;
      
  if v_ei_id > 0 then
    
    --check if attributes have been linked
    select count(*)
    into   v_eia_id
    from   element_attribute_applied eaa
       inner join ref_element_attributes ea on ea.element_attribute_id = eaa.element_attribute_id
    where  eaa.element_id = :old.element_id
    and    ea.element_identifier_id = :old.element_identifier_id;
            
    if v_eia_id <> 0 then
      RAISE_APPLICATION_ERROR(-20000, 'The identifier cannot be deleted, a dependant element attribute exists');
    end if;
    
  end if;
end ELEMIDAPPL_DEL;
