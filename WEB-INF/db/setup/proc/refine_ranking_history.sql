drop procedure if exists refine_ranking_history;
delimiter $$
create procedure refine_ranking_history(dayDate date)
begin
   
drop table if exists permanent_record;

create  temporary table permanent_record select distinct user_id,lastUpdate from
   (
   select  user_id, max(lastUpdate) as lastUpdate from ranking_history
       where lastUpdate > timestamp(dayDate) and
           lastUpdate < timestampadd(day,1,dayDate)   group by user_id,date(lastUpdate)
         union  
    select user_id, min(lastUpdate)  as lastUpdate from ranking_history
       where lastUpdate > timestamp(dayDate) and
           lastUpdate < timestampadd(day,1,dayDate)   group by user_id,date(lastUpdate)
         union  
	select user_id, min(lastUpdate)  as lastUpdate from ranking_history
       where season_id = (select season_id from ranking_history where lastUpdate < timestampadd(day,1,date(dayDate)) limit 1)
        group by user_id, season_id 
       ) as perm_record;
       

	   
drop table if exists permanent_record_id;

create  temporary  table permanent_record_id
   select rh.id as id from permanent_record pr inner join ranking_history rh on pr.user_id = rh.user_id and pr.lastUpdate = rh.lastUpdate;
ALTER TABLE `permanent_record_id` ADD PRIMARY KEY(`id`);
 
 delete from ranking_history 
     where lastUpdate > timestamp(dayDate) and         
            lastUpdate < timestampadd(day,1,dayDate) and
			id not in (select * from permanent_record_id) and id not in (select ranking_history_id from season_result);
     
    
end $$
delimiter ; 