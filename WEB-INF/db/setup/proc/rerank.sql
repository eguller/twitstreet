drop procedure if exists rerank;
delimiter $$
create procedure rerank()
begin
    declare done int default false;
    declare  user_id int;
    declare  userRank int;
    declare  new_rank  int default 1;
    declare cur1 cursor for select id, rank from users order by (portfolio_value(id) + cash) desc;
    declare continue handler for not found set done = true;
    open cur1;
    set @new_rank := 1;
    read_loop: LOOP
    fetch cur1 into user_id, userRank;   
        if done then
            leave read_loop;
        end if;   
        if @new_rank > userRank then
            update users set rank = @new_rank, direction = 1 where id = user_id;
        else
            update users set rank = @new_rank, direction = 0 where id = user_id;
        end if;
        set @new_rank := @new_rank + 1;
    end loop read_loop;    
    close cur1;
end $$
delimiter ; 