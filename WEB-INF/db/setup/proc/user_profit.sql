drop function if exists user_profit;
delimiter $$
create function user_profit(user_id int) returns double
begin
	declare profit double default 0.0;
 	select sum(stock.changePerHour * portfolio.percentage) into profit
 	from portfolio, stock,users where
 	users.id = portfolio.user_id and
 	portfolio.user_id = user_id and
 	stock.id = portfolio.stock; 
	return ifnull(profit,0.0);
end $$