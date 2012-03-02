-- create tables

-- user table
create table `users`(
    `id` bigint not null,
    `userName` varchar(45) not null,
    `firstLogin` datetime not null,
    `lastLogin` datetime not null,
    `cash` decimal(11,2) not null default 1000,
    `lastIp` varchar(45) not null,
    `oauthToken` varchar(100) not null,
    `oauthTokenSecret` varchar(100) not null,
    `pictureUrl` varchar(255),
     primary key (`id`),
     unique key `username_unique` (`username`)
)  engine=innodb default charset=`utf8`;

-- admin table
create table `admin`(
    `id` bigint not null auto_increment,
    `username` varchar(45) not null,
    `password` varchar(45) not null,
    `lastLogin` datetime not null,
    `lastIp` varchar(45),
     primary key (`id`)
)  engine=innodb default charset=`utf8`;

-- stock table
create table `stock`(
    `id` bigint not null auto_increment,
    `name` varchar(45) not null,
    `total` int not null,
    `pictureUrl` varchar(255),
    `lastUpdate` timestamp,
    `changePerHour` int,
    `verified` bit(1) DEFAULT b'0',
     primary key (`id`)
)  engine=innodb default charset=`utf8`;

-- portfolio table
create table `portfolio`(
    `id` bigint not null auto_increment,
    `user_id` bigint not null,
    `stock` bigint not null,
    `percentage` double not null,
    `capital` decimal(11,2) not null,
     primary key (`id`),
     unique key `unique_portfolio` (`user_id` , `stock`),
     key `fk_users` (`user_id`),
     key `fk_stock` (`stock`),
     constraint `fk_users` foreign key (`user_id`) references `users` (`id`) on delete cascade,
     constraint `fk_stock` foreign key (`stock`) references `stock` (`id`) on delete cascade
)  engine=innodb default charset=`utf8`;

-- config table
create table `config`(
    `id` bigint not null auto_increment,
    `parm` varchar(45) not null,
    `val` varchar(90) not null,
     primary key (`id`)
)  engine=innodb default charset=`utf8`;

-- transactions table
create  table `transactions` (
  `id` bigint not null auto_increment,
  `user_id` bigint null,
  `stock` bigint null,
  `amount` decimal(11,2) null,
  `t_action` tinyint null,
  `t_date` timestamp,
  primary key (`id`) 
) engine=innodb default charset=`utf8`;

-- stock_history table
create table `stock_history` (
  `id` bigint(20) not null auto_increment,
  `stock` bigint(20) not null,
  `total` int(11) not null,
  `date` varchar(10) not null,  
  `hour` int(11) not null default 0,
  `lastUpdate` timestamp,
  primary key (`id`),
  unique key `unique_hourly_stock` (`stock`,`date`,`hour`),
  constraint `fk_stock_history_stock` foreign key (`stock`) references `stock` (`id`)
) engine=innodb default charset=`utf8`;


-- groups table
create table `groups` (
   
    `id` bigint not null auto_increment,
	`name` varchar(45),
     primary key (`id`),
     unique key `unique_group_name` (`name`)
)  engine=innodb default charset=`utf8`;

-- group role table
create table `group_role` (
    `id` bigint not null,
	`name` varchar(45),     
     primary key (`id`),
     unique key `unique_role_name` (`name`)
)  engine=innodb default charset=`utf8`;

-- user group table
create table `user_group` (
   
    `group_id` bigint not null,
	`user_id` bigint not null,
	`role_id` bigint not null,
     primary key (`user_id`,`group_id`),
     constraint `fk_group_role` foreign key (`role_id`) references `group_role` (`id`),
     constraint `fk_group_user` foreign key (`user_id`) references `users` (`id`) on delete cascade,
     constraint `fk_user_group` foreign key (`group_id`) references `groups` (`id`) on delete cascade
)  engine=innodb default charset=`utf8`;

-- ranking table
create table `ranking` (
    `user_id` bigint not null,
	`cash` decimal(11,2),
	`portfolio` decimal(11,2),
    `rank` int,
    `oldRank` int,
    `direction` int,
    `profit` decimal(11,2),
    `lastUpdate` timestamp,
     primary key (`user_id`),
     constraint `fk_ranking_user` foreign key (`user_id`) references `users` (`id`)
)  engine=innodb default charset=`utf8`;

-- ranking history table
create table `ranking_history` (
    `user_id` bigint not null,
    `cash` decimal(11,2),
    `portfolio` decimal(11,2),
    `rank` int,
    `lastUpdate` timestamp,
     primary key (`user_id`,`lastUpdate`),
     constraint `fk_ranking_history_user` foreign key (`user_id`) references `users` (`id`)
)  engine=innodb default charset=`utf8`;


-- ranking history table
create table `user_stock_watch` (
    `user_id` bigint not null,    
    `stock_id` bigint not null, 
     primary key (`user_id`,`stock_id`),
     constraint `fk_user_stock_watch_user` foreign key (`user_id`) references `users` (`id`),
     constraint `fk_user_stock_watch_stock` foreign key (`stock_id`) references `stock` (`id`)
)  engine=innodb default charset=`utf8`;

-- twitter trends table
create table `twitter_trends` (  
    `stock_id` bigint not null, 
     primary key (`stock_id`),
    `lastUpdate` timestamp not null default now(),
     constraint `fk_twitter_trends_stock` foreign key (`stock_id`) references `stock` (`id`)
)  engine=innodb default charset=`utf8`;
