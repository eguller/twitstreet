drop procedure if exists rerankCumulative;
delimiter $$
create procedure rerankCumulative()
begin

    declare done int default false;
   
    declare  r_user_id bigint;
    declare value_cumulative double;
    declare  new_rank_cumulative  int default 1;
  
  
    declare cur cursor for select r.user_id, ucv.value + r.cash+r.portfolio-r.loan as cumulativeValue  
        from ranking r inner join user_cumulative_value ucv on r.user_id=ucv.user_id
        order by ucv.value + r.cash+r.portfolio-r.loan desc;
      declare continue handler for not found set done = true;
 
       
    
    insert ignore into user_cumulative_value(user_id,value) 
        select user_id,0 from ranking r;
    
    open cur;
    set @new_rank_cumulative := 1;
    read_loop: LOOP
    fetch cur into r_user_id,value_cumulative;   
        if done then
            leave read_loop;
        end if;   
        
        update ranking set rankCumulative = @new_rank_cumulative, valueCumulative = value_cumulative
        where ranking.user_id = r_user_id;
 		
        set @new_rank_cumulative := @new_rank_cumulative + 1;
    end loop read_loop;    
    close cur;
   
    
end $$
delimiter ; 