drop trigger if exists recalcportfolio;
delimiter $$
create trigger recalcportfolio before update on stock
    for each row begin
        declare done int default false;
        declare stock_total int;
        declare portfolio_user_id long;
        declare portfolio_percentage double;
        declare cur1 cursor for select user_id, percentage from portfolio where stock = NEW.id;
        declare continue handler for not found set done := true;
        
        open cur1;
        
        set @stock_total := (select total from stock where id = NEW.id);
        if NEW.total != @stock_total then 
            
            read_loop: loop
                fetch cur1 into portfolio_user_id, portfolio_percentage;
                    if done then
                        leave read_loop;
                    end if;
                    update users set portfolio = portfolio + ((NEW.total - @stock_total) * portfolio_percentage) where id = portfolio_user_id;
            end loop read_loop;	
        end if;
        close cur1;
    end;
$$
delimiter ;