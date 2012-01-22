insert into admin(username,password,lastLogin,lastIp) values('{username}', '{password}', now(), '0.0.0.0');
insert into config(parm, val) values('consumerKey', '{consumerKey}');
insert into config(parm, val) values('consumerSecret', '{consumerSecret}');
insert into config(parm, val) values('minFollower', 500);
insert into config(parm, val) values('gaAccount', 'UA-7030369-5');