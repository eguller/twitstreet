drop function if exists portfolio_value;
delimiter $$
create function portfolio_value(user_id int) returns double
begin
 declare portfolio double default 0.0;
 	select sum(stock.total * portfolio.percentage) into portfolio from users, portfolio, stock where users.id = portfolio.user_id and stock.id = portfolio.stock and users.id = user_id;
	return ifnull(portfolio,0.0);
end $$