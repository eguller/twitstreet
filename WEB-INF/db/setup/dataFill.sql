insert into admin(username,password,lastLogin,lastIp) values('{username}', '{password}', now(), '0.0.0.0');
insert into config(parm, val) values('consumerKey', '{consumerKey}');
insert into config(parm, val) values('consumerSecret', '{consumerSecret}');
insert into config(parm, val) values('minFollower', 500);
insert into config(parm, val) values('gaAccount', 'UA-7030369-5');

insert into groups(name,id) values('Overall',1);

insert into group_role(id,name) values(0,'user');
insert into group_role(id,name) values(1,'moderator');
insert into group_role(id,name) values(2,'admin');
