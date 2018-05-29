use sync_demo;

drop table if exists `question`;
drop table if exists `answer`;

####################################################################################################
drop table if exists `question`;
create table `question` (

    `id` int not null AUTO_INCREMENT,
    `local_id` int not null default 0,

    `name` varchar(60) default null,
    `say` varchar(60) default null,
    `image_url` varchar(256) default null,
    `question` varchar(1024) default null,

    `created_at` int DEFAULT 0,
    `updated_at` int DEFAULT 0,

    KEY `local_id` (`local_id`),
    PRIMARY KEY (`id`)

) DEFAULT CHARSET=utf8;

####################################################################################################
drop table if exists `answer`;
create table `answer`(

    `id` int not null auto_increment,
    `local_id` int not null default 0,
    `question_id` int not null,

    `name` varchar(60) default null,
    `answer` varchar(1024) default null,

    `created_at` int DEFAULT 0,
    `updated_at` int DEFAULT 0,

    KEY `question_id` (`question_id`),
    KEY `local_id` (`local_id`),
    PRIMARY KEY (`id`)

) default charset = utf8;