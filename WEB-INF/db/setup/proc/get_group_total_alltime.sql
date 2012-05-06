drop function if exists get_group_total_alltime;
delimiter $$
create function get_group_total_alltime(group_id int) returns double
begin
	declare totalval double;
	 	
 
	 select sum((select cash+portfolio from ranking_history rh where rh.user_id = ug.user_id order by lastUpdate desc limit 1) )+
	 sum((select ucv.value from user_cumulative_value ucv where ucv.user_id = ug.user_id)) 
	 as total 
				into totalval from groups g inner join 
            user_group ug on ug.group_id = g.id 
            	where g.id= group_id;
	
	return totalval;
end $$