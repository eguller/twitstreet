drop procedure if exists new_season;
delimiter $$
create procedure new_season(initial_cash double)
begin
	call rerank();
	insert ignore into ranking_history(user_id, cash, portfolio, lastUpdate, rank, season_id) 
		select user_id, cash, portfolio,  lastUpdate, rank,(select id from season_info where active is true) from ranking;
		
 	update users set cash = initial_cash;
 	delete from portfolio;
 	
 	update season_info set active=false where id = season_to_end;
 	update season_info set active=true where id = season_to_end+1;
 	
	call rerank();
	insert ignore into ranking_history(user_id, cash, portfolio, lastUpdate, rank, season_id) 
		select user_id, cash, portfolio,  lastUpdate, rank,(select id from season_info where active is true) from ranking;
end $$

