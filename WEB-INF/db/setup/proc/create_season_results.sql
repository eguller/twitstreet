drop procedure if exists create_season_results;
delimiter $$
create procedure create_season_results()
begin
	delete from season_result;

	insert into season_result(season_id,ranking_history_id)
		select rh.season_id, rh.id from ( select user_id, max(lastUpdate) as seasonLastUpdate from ranking_history where season_id in (select id from season_info where active !=true) group by user_id,season_id ) as user_season_ranking inner join ranking_history as rh on rh.user_id = user_season_ranking.user_id and rh.lastUpdate = user_season_ranking.seasonLastUpdate;
end $$