create or replace view indirect_contact_points as
with rel_sel as
(  select r.element_id2 main_element_id,r.element_id1 element_id, r.relation_name_id, r.relation_id
   from ccla.relations r
   union  
   select r.element_id1 main_element_id,r.element_id2 element_id, r.relation_name_id, r.relation_id
   from ccla.relations r
)
select r.main_element_id
      ,r.element_id
      ,rn.relation
      ,rt.relation_label
      ,case 
         when cp.value is not null then cp.value 
         else a.flat||' '||a.housename||' '||a.housenumber||' '||a.street||' '||a.postcode 
         end as contact 
      ,cs.submethod_name
      ,cs.method_id
from rel_sel r 
  inner join ccla.ref_relation_names rn on rn.relation_name_id = r.relation_name_id
  inner join ccla.ref_relation_types rt on rt.relation_type_id = rn.relation_type_id
  inner join ccla.contact_point cp on cp.relation_id = r.relation_id
  inner join ccla.ref_contact_submethod cs on cs.submethod_id = cp.submethod_id
  left outer join ccla.address a on a.address_id = cp.address_id;

CREATE OR REPLACE PUBLIC SYNONYM indirect_contact_points FOR CCLA.indirect_contact_points;
GRANT SELECT ON indirect_contact_points TO UCMADMIN;