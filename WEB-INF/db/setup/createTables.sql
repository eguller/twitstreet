-- create tables

-- user table
create table `users`(
	`id` bigint not null,
	`userName` varchar(45) not null,
	`firstLogin` datetime not null,
	`lastLogin` datetime not null,
	`cash` double not null default 10000,
	`lastIp` varchar(45) not null,
	`oauthToken` varchar(100) not null,
	`oauthTokenSecret` varchar(100) not null,
	`rank` int,
	`direction` tinyint,
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
	 primary key (`id`)
)  engine=innodb default charset=`utf8`;

-- portfolio table
create table `portfolio`(
	`id` bigint not null auto_increment,
	`user_id` bigint not null,
	`stock` bigint not null,
	`percentage` double not null,
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
  `amount` double null,
  `t_action` tinyint null,
  `t_date` timestamp,
  primary key (`id`) );

-- stock_history table
create  table `stock_history` (
	`id` bigint not null auto_increment,
	`stock` bigint not null,
	`name` varchar(45) not null,
	`total` int not null,
	`day` varchar(10),
	`stockLastUpdate` timestamp,
	 primary key (`id`),
	 unique key `unique_daily_stock` (`stock`, `day`)
)  engine=innodb default charset=`utf8`;



