-- create tables

-- user table
create table `users`(
	`id` bigint not null,
	`userName` varchar(45) not null,
	`firstLogin` datetime not null,
	`lastLogin` datetime not null,
	`cash` int not null default 10000,
	`portfolio` int not null default 0,
	`lastIp` varchar(45) not null,
	`oauthToken` varchar(100) not null,
	`oauthTokenSecret` varchar(100) not null,
	`rank` int,
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

-- admin table
create table `stock`(
	`id` bigint not null auto_increment,
	`name` varchar(45) not null,
	`total` int not null,
	`sold` double not null,
	 primary key (`id`)
)  engine=innodb default charset=`utf8`;

-- admin table
create table `portfolio`(
	`id` bigint not null auto_increment,
	`username` varchar(45) not null,
	`password` varchar(45) not null,
	`lastLogin` datetime not null,
	`lastIp` varchar(45) not null,
	 primary key (`id`)
)  engine=innodb default charset=`utf8`;

-- config table
create table `config`(
	`id` bigint not null auto_increment,
	`parm` varchar(45) not null,
	`val` varchar(90) not null,
	 primary key (`id`)
)  engine=innodb default charset=`utf8`;




