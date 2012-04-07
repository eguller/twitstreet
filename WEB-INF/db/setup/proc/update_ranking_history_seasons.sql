drop procedure if exists update_ranking_history_seasons;
delimiter $$
create procedure update_ranking_history_seasons()
begin
  	declare done int default false;
    declare curr_season_id int;
    declare cur1 cursor for select id from season_info;
 
    
    declare continue handler for not found set done = true;
    
    update ranking_history set lastUpdate=lastUpdate, season_id = null;
    
    open cur1;
    read_loop: LOOP
    fetch cur1 into curr_season_id;   
        if done then
            leave read_loop;
        end if;   
         update ranking_history set season_id = curr_season_id  , lastUpdate=lastUpdate  where  
            lastUpdate >=  (select startTime from season_info where id=curr_season_id) and 
            lastUpdate <=  (select endTime from season_info where id=curr_season_id) and season_id is null;

    end loop read_loop;    
    close cur1;
end $$
delimiter ; 