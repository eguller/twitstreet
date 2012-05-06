drop function if exists get_group_rank_alltime;
delimiter $$
create function get_group_rank_alltime(group_id int) returns int
begin
	declare grank int;
	 	
 
	
	select count(*) + 1 into grank from groups g where get_group_total_alltime(g.id) > get_group_total_alltime(group_id);
	
	return grank;
end $$