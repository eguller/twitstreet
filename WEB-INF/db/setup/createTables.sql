-- create tables

-- user table
create table `users`(
    `id` bigint not null,
    `userName` varchar(45) not null,
    `longName` varchar(255) default null,
    `firstLogin` datetime not null,
    `lastLogin` datetime not null,
    `cash` decimal(11,2) not null default 1000,
    `language` varchar(10) not null default 'en',
    `lastIp` varchar(45) not null,
    `oauthToken` varchar(100) not null,
    `oauthTokenSecret` varchar(100) not null,
    `pictureUrl` varchar(255),
    `location` varchar(255) DEFAULT NULL,
    `description` varchar(255) DEFAULT NULL,
    `inviteActive` tinyint(1) DEFAULT '1',
    `newSeasonInfoSent` tinyint(1) NOT NULL DEFAULT '1',
     `language` varchar(45) NOT NULL DEFAULT 'en',
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
    `longName` varchar(255) default null,
    `description` varchar(255) default null,
    `total` int not null,
    `pictureUrl` varchar(255),
    `lastUpdate` timestamp,
    `changePerHour` int,
    `createdAt` date not null default '2000-01-01',
    `updating` bit(1) DEFAULT b'0',
    `verified` bit(1) DEFAULT b'0',
    `location` varchar(45) default NULL,
     primary key (`id`),
     KEY `idx_name` (`name`)
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
  primary key (`id`),
  KEY `idx_user_stock` (`user_id`,`stock`)
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
    `adminId` bigint not null,
	`name` varchar(45),
    `status` int not null default 0, index (`status`),
     primary key (`id`),
     constraint `fk_group_admin` foreign key (`adminId`) references `users` (`id`),
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

-- user group table block
create table `user_group_block` (
   
    `group_id` bigint not null,
	`user_id` bigint not null,
     primary key (`user_id`,`group_id`),
     constraint `fk_user_group_block_user` foreign key (`user_id`) references `users` (`id`) on delete cascade,
     constraint `fk_user_group_block_group` foreign key (`group_id`) references `groups` (`id`) on delete cascade
)  engine=innodb default charset=`utf8`;

-- ranking table
create table `ranking` (
    `user_id` bigint not null,
	`cash` decimal(11,2),
	`portfolio` decimal(11,2),
	`valueCumulative` decimal(11,2),
    `rank` int,
    `rankCumulative` int,
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
    `lastUpdate` timestamp, index (`lastUpdate`),
     `season_id` int, 
    `id` bigint(20) not null auto_increment,
     primary key (`id`),
     constraint `fk_ranking_history_user` foreign key (`user_id`) references `users` (`id`),
     constraint `fk_ranking_history_season` foreign key (`season_id`) references `season_info` (`id`)
)  engine=innodb default charset=`utf8`;

create table `season_result` (
    `ranking_history_id` bigint not null,
    `season_id` int not null,
     primary key (`ranking_history_id`),
     constraint `fk_season_result_ranking_history_id` foreign key (`ranking_history_id`) references `ranking_history` (`id`),
     constraint `fk_season_result_season_id` foreign key (`season_id`) references `season_info` (`id`)
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
-- suggested stocks table
create table `suggested_stocks` (  
    `stock_id` bigint not null, 
     primary key (`stock_id`),
     constraint `fk_suggested_stocks_stock` foreign key (`stock_id`) references `stock` (`id`)
)  engine=innodb default charset=`utf8`;

-- announcement table
create table `announcement` (  
    `stock_id` bigint not null, 
     primary key (`stock_id`),
    `timeSent` timestamp not null default now(),
     constraint `fk_announcement_stock` foreign key (`stock_id`) references `stock` (`id`)
)  engine=innodb default charset=`utf8`;

create table `invite` (
  `id` bigint(20) not null auto_increment,
  `invitor` bigint(20) not null,
  `invited` bigint(20) not null,
  `invite_date` datetime default null,
  primary key (`id`),
  unique key `invited_unique` (`invited`),
  key `fk_invited` (`invited`),
  key `fk_invitor` (`invitor`),
  constraint `fk_invitor` foreign key (`invitor`) references `users` (`id`) on delete no action on update no action,
  constraint `fk_invited` foreign key (`invited`) references `users` (`id`) on delete no action on update no action
) engine=innodb default charset=utf8;

create table `inactive_user` (
  `user_id` bigint not null,
  primary key (`user_id`),
  constraint `fk_inactive_user` foreign key (`user_id`) references `users` (`id`)
) engine=innodb default charset=utf8;

create table `season_info` (
  `id` int not null,
  primary key (`id`),
  `startTime` timestamp not null default now(),
  `endTime` timestamp,
  `active` bit(1) DEFAULT b'0',
  `updateInProgress` bit(1) DEFAULT b'0'
) engine=innodb default charset=utf8;

create table `user_cumulative_value` (
  `user_id` bigint not null,
  `value` decimal(11,2) not null , 
  primary key (`user_id`),
  constraint `fk_user_cumulative_value_user` foreign key (`user_id`) references `users` (`id`)
) engine=innodb default charset=utf8;

CREATE TABLE `announcer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `consumerKey` varchar(255) DEFAULT NULL,
  `consumerSecret` varchar(255) DEFAULT NULL,
  `accessToken` varchar(255) DEFAULT NULL,
  `accessTokenSecret` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf;

