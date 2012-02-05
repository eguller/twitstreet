drop function if exists user_stock_profit;
delimiter $$
create function user_stock_profit(user_id int, stock_id int) returns double
begin
	declare profit double default 0.0;
 	select stock.changePerHour * portfolio.percentage into profit
 	from portfolio, stock where 	
 	portfolio.user_id = user_id and
 	stock.id = portfolio.stock and 
 	stock.id = stock_id; 
	return ifnull(profit,0.0);
end $$