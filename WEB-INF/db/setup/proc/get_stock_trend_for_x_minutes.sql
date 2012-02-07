drop function if exists get_stock_trend_for_x_minutes;
delimiter $$
create function get_stock_trend_for_x_minutes(stock_id int, minutes int) returns double
begin
	declare oldValue double default 0.0;
	 	
	select (s.total - sh.total) * minutes / TIMESTAMPDIFF(minute,sh.lastUpdate,s.lastUpdate) into oldValue from stock_history sh,stock s 
	where 
	(TIMESTAMPDIFF(minute,s.lastUpdate,now()) <= 5 and TIMESTAMPDIFF(minute,sh.lastUpdate,s.lastUpdate)  > minutes * 0.25 and TIMESTAMPDIFF(minute,sh.lastUpdate,s.lastUpdate)  <= 1.5 * minutes) and 
	sh.stock = stock_id and
	sh.stock = s.id
	order by sh.lastUpdate asc limit 1;
	
	return ifnull(oldValue,0.0);
end $$