drop function if exists get_group_rank;
delimiter $$
create function get_group_rank(group_id int) returns int
begin
	declare grank int;
	 	
 
	
	select count(*) + 1 into grank from groups g where get_group_total(g.id) > get_group_total(group_id);
	
	return grank;
end $$
