drop procedure if exists refine_stock_history;
delimiter $$
create procedure refine_stock_history(dayDate date)
begin
   
drop table if exists permanent_record;

create  temporary table permanent_record select distinct stock,lastUpdate from
   (
   select  stock, max(lastUpdate) as lastUpdate from stock_history
       where lastUpdate > timestamp(dayDate) and
           lastUpdate < timestampadd(day,1,dayDate)   group by stock,date(lastUpdate)
         union  
    select stock, min(lastUpdate)  as lastUpdate from stock_history
       where lastUpdate > timestamp(dayDate) and
           lastUpdate < timestampadd(day,1,dayDate)   group by stock,date(lastUpdate)
       ) as permanent_stock ;
   
drop table if exists permanent_record_id;

create  temporary  table permanent_record_id
   select sh.id as id from permanent_record pr inner join stock_history sh on pr.stock = sh.stock and pr.lastUpdate = sh.lastUpdate;
ALTER TABLE `permanent_record_id` ADD PRIMARY KEY(`id`);
 
 delete from stock_history 
     where lastUpdate > timestamp(dayDate) and         
            lastUpdate < timestampadd(day,1,dayDate) and
			id not in (select * from permanent_record_id);
     
    
end $$
delimiter ; 