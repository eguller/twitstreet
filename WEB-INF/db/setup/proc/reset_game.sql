drop procedure if exists reset_game;
delimiter $$
create procedure reset_game(INOUT initial_cash double)
begin

 	update users set cash = initial_cash;
 	delete from portfolio;
 	delete from transactions;
 	call rerank();
end $$