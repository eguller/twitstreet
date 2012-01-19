drop function if exists stock_sold;
delimiter $$
create function stock_sold(stock_id int) returns double
begin
    declare sold double default 0.0;
 	select sum(portfolio.percentage) into sold from portfolio where stock = stock_id;
	return ifnull(sold,0.0);
end $$