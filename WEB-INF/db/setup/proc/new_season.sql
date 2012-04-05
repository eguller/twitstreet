
drop procedure if exists new_season;
delimiter $$
create procedure new_season(initial_cash double)
begin

  declare timeNow timestamp;
  declare active_season int;
  declare next_season int;
    declare active_season_end timestamp;
    
select id, endTime into @active_season,@active_season_end from season_info where active is true ;
 
    set @next_season := @active_season+1;

	call rerank();
	insert ignore into ranking_history(user_id, cash, portfolio, lastUpdate, rank, season_id) 
		select user_id, cash, portfolio,  lastUpdate, rank,@active_season from ranking;
		
	call create_season_result(@active_season);	
	
	
 	update users set cash = initial_cash;
 	delete from portfolio;
 	
    
 	update season_info set active=false where id = @active_season;
    
    set @timeNow := now();
  	insert into season_info(id,startTime,endTime,active) values(@next_season, @timeNow, @active_season_end + INTERVAL 7 DAY, true);
    
    
	call rerank();
	insert ignore into ranking_history(user_id, cash, portfolio, lastUpdate, rank, season_id) 
		select user_id, cash, portfolio,  lastUpdate, rank,@next_season from ranking;
end $$
