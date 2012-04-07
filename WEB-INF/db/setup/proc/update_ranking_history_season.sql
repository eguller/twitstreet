drop procedure if exists update_ranking_history_season;
delimiter $$
create procedure update_ranking_history_season(s_id int)
begin
 
         update ranking_history set season_id = s_id  , lastUpdate=lastUpdate  where  
            lastUpdate >=  (select startTime from season_info where id=s_id) and 
            lastUpdate <=  (select endTime from season_info where id=s_id);

end $$
delimiter ; 