insert into admin(username,password,lastLogin,lastIp) values('{username}', '{password}', now(), '0.0.0.0');
insert into config(parm, val) values('consumerKey', '{consumerKey}');
insert into config(parm, val) values('consumerSecret', '{consumerSecret}');
insert into config(parm, val) values('minFollower', 0);

7, announcerConsumerKey, CSiOKSpSlIxKgCg2YD8og
8, announcerConsumerSecret, UsbejOcKjcHc0GdAdEARDwlWkZs9NKhmTk1fwjGSfZQ
9, announcerAccessToken, 273572038-s6qY3ZaLMaNBdf95gGIlOqEjwMc9ZIcvGsgVHaaD
10, announcerAccessSecret, I7WBanQwKWJW7FwqqJ5VwQlVCmFdLJP1ka2Ukm4aZk

insert into config(parm, val) values('gaAccount', 'UA-7030369-5');
insert into config(parm, val) values('comissionTreshold', '15000');

insert into config(parm, val) values('announcerConsumerKey', '{announcerConsumerKey}');
insert into config(parm, val) values('announcerConsumerSecret', '{announcerConsumerSecret}');
insert into config(parm, val) values('announcerAccessToken', '{announcerAccessToken}');
insert into config(parm, val) values('announcerAccessSecret', '{announcerAccessSecret}');

insert into config(parm, val) values('server-count', '{server-count}');

insert into groups(name,id) values('Overall',1);

insert into group_role(id,name) values(0,'user');
insert into group_role(id,name) values(1,'moderator');
insert into group_role(id,name) values(2,'admin');

INSERT INTO `twitstreet`.`season_info` (`id`, `startTime`, `endTime`, `active`) VALUES (1, '2012-01-19 00:00:00', '2012-01-31 00:00:00', false);

INSERT INTO `twitstreet`.`season_info` (`id`, `startTime`, `endTime`, `active`) VALUES (2, '2012-02-01 00:00:00', '2012-02-29 00:00:00', false);

INSERT INTO `twitstreet`.`season_info` (`id`, `startTime`, `endTime`, `active`) VALUES (3, '2012-03-01 00:00:00', '2012-03-11 00:00:00', false);

INSERT INTO `twitstreet`.`season_info` (`id`, `startTime`, `endTime`, `active`) VALUES (4, '2012-03-12 00:00:00', '2012-03-18 00:00:00', false);

INSERT INTO `twitstreet`.`season_info` (`id`, `startTime`, `endTime`, `active`) VALUES (5, '2012-03-19 00:00:00', '2012-03-25 00:00:00', true);
