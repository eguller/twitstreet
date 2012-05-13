drop function if exists get_group_total;
delimiter $$
create function get_group_total(group_id int) returns double
begin
	declare totalval double;
	 	
 
	 select sum((select cash+portfolio-loan from ranking_history rh where user_id = ug.user_id order by lastUpdate desc limit 1) ) as total 
				into totalval from groups g inner join 
            user_group ug on ug.group_id = g.id 
            	where g.id= group_id;
	
	return totalval;
end $$
