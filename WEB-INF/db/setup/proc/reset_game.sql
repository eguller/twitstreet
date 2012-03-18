drop procedure if exists reset_game;
delimiter $$
create procedure reset_game(IN initial_cash double)
begin
	call rerank();
	insert ignore into ranking_history(user_id, cash, portfolio, lastUpdate, rank) 
		select user_id, cash, portfolio,  lastUpdate, rank from ranking 
		
 	update users set cash = initial_cash;
 	delete from portfolio;
 	
 	call rerank();
 	insert ignore into ranking_history(user_id, cash, portfolio, lastUpdate, rank) 
		select user_id, cash, portfolio,  lastUpdate, rank from ranking 
end $$