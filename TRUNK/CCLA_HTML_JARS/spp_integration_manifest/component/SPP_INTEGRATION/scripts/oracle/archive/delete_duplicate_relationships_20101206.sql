set serveroutput on size 1000000

-- Created on 30/11/2010 by DONNA 
declare 
  -- Local variables here
  v_min_relId     integer;
	v_count         integer := 0;
	v_output        varchar2(100);
	v_deleted       integer;
	v_totaldeleted  integer := 0;
begin
  -- print header
	dbms_output.put_line(' Element_1   Element_2  RelationTypeId  OriginalTotal   Deleted');
	
	-- process each duplicated relation
	FOR rel_cur in 
	  (select r.relation_name_id, r.element_id1, r.element_id2, count(*) total 
		 from relations r 
     group by r.relation_name_id, r.element_id1, r.element_id2
     having count(*) > 1
		)
	loop
	   
	   -- Find the Min relation to keep
		 v_count := v_count + 1;
		 select min(r.relation_id)
		 into   v_min_relId
		 from   relations r
		 where  r.relation_name_id = rel_cur.relation_name_id
		 and    r.element_id1 = rel_cur.element_id1
		 and    r.element_id2 = rel_cur.element_id2;

     -- Delete each duplicate
     delete 
		 from   relations r
		 where  r.relation_name_id = rel_cur.relation_name_id
		 and    r.element_id1 = rel_cur.element_id1
		 and    r.element_id2 = rel_cur.element_id2
		 and    r.relation_id <> v_min_relId;
		 v_deleted := sql%rowcount;
		 v_totaldeleted := v_totaldeleted + v_deleted;
		 
		 --Print Deleted details
		 v_output := lpad(to_char(rel_cur.element_id1),10,' ')||lpad(to_char(rel_cur.element_id2),12,' ')||
		             lpad(to_char(rel_cur.relation_name_id),16,' ')||lpad(to_char(rel_cur.total),15,' ')||
								 lpad(to_char(v_deleted),10,' ');		 
		 dbms_output.put_line(v_output);

     commit;
		 
	end loop; -- rel_cur
  dbms_output.put_line('Total Processed = '||v_count||', Total Deleted = '||v_totaldeleted);
end;
/

alter table RELATIONS
  add constraint RELS_UK unique (ELEMENT_ID1, ELEMENT_ID2, RELATION_NAME_ID); 
