
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


	call create_season_result(@active_season);	
	
	delete from user_cumulative_value;
	insert into user_cumulative_value(value,user_id) select sum(cash+portfolio-loan) as total, rh.user_id from season_result sr inner join ranking_history rh on rh.id = sr.ranking_history_id 
		where sr.season_id >= 4 group by rh.user_id order by total desc;

	update users set cash = initial_cash;
 	
 	insert ignore into twitter_trends(stock_id) select distinct stock from portfolio;
 	update users set cash = initial_cash, loan = 0;
 	delete from portfolio;
 	
    
 	update season_info set active=false where id = @active_season;
    
    set @timeNow := now();
  	insert into season_info(id,startTime,endTime,active) values(@next_season, @timeNow, @active_season_end + INTERVAL 7 DAY, true);
    
    
	call rerank();
	insert ignore into ranking_history(user_id, cash, portfolio, loan, lastUpdate, rank, season_id) 
		select user_id, cash, portfolio, loan, lastUpdate, rank,@next_season from ranking;
		
	delete from user_stock_watch;
	delete from ranking_history where id not in (select ranking_history_id from season_result);
	delete from stock_history where stock not in (select stock from portfolio) and stock not in (select stock_id from suggested_stocks) and stock not in (select stock_id from twitter_trends);
	delete from transactions where datediff(now(), t_date) > 8;
	delete from stock where id not in (select stock from portfolio) and id not in (select stock from stock_history) and id not in (select stock_id from suggested_stocks) and id not in (select stock_id from twitter_trends) and id not in (select stock_id from announcement);
	delete from stock_history where stock not in (select stock from portfolio) and stock not in (select stock_id from suggested_stocks) and stock not in (select stock_id from twitter_trends);
	
end $$
delimiter ;
