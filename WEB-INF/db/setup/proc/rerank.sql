

drop procedure if exists rerank;
delimiter $$
create procedure rerank()
begin
  	declare done int default false;
    declare  user_id bigint;
    declare user_portfolio double;
    declare user_cash double;
    declare  new_rank  int default 1;
    declare cur1 cursor for select id, cash,portfolio_value(id) from users order by (portfolio_value(id) + cash - loan) desc, userName asc;
    declare continue handler for not found set done = true;
    open cur1;
    set @new_rank := 1;
    read_loop: LOOP
    fetch cur1 into user_id,user_cash,user_portfolio;   
        if done then
            leave read_loop;
        end if;   
        insert into ranking(user_id,rank,oldRank,direction,cash,portfolio,lastUpdate) 
        values (user_id,@new_rank,101,101-@new_rank,user_cash,user_portfolio,NOW())
 		ON DUPLICATE KEY UPDATE profit= user_profit(user_id), 
            oldRank = rank, rank = @new_rank, direction = oldRank - rank, cash = user_cash, 
            portfolio = user_portfolio,lastUpdate=NOW();      
 		
        set @new_rank := @new_rank + 1;
    end loop read_loop;    
    close cur1;
    
   call rerankCumulative();
   
    
end $$
delimiter ; 