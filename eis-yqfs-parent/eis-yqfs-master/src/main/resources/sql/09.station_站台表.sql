-- PRINT '------ 站台拣选单表 ------'
drop table if exists `station_pick`;
create table `station_pick` (
  `id` int not null comment 'id(站台ID)',
  `is_picking` int not null comment '是否当前站台正在拣选（1：是，2：否）',
  `is_ck_complete` int not null comment '是否出库完成（1：是，2：否）',
  `is_all_arrive` int not null comment '料箱是否全部达到（1：是，0：否）',
  `create_time` date not null  comment '创建时间',
  `update_time` date default null  comment '修改时间',
primary key (`id`)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_0900_ai_ci comment='站台拣选单表';

-- PRINT '------ 站台拣选单历史表 ------'
drop table if exists `station_pick_history`;
create table `station_pick_history` (
  `id` int not null auto_increment comment 'id',
  `station_id` int default null comment '站台Id',
  `is_picking` int not null comment '是否当前站台正在拣选（1：是，2：否）',
  `is_ck_complete` int not null comment '是否出库完成（1：是，2：否）',
  `is_all_arrive` int not null comment '料箱是否全部达到（1：是，0：否）',
  `create_time` date not null  comment '创建时间',
  `update_time` date default null  comment '修改时间',
primary key (`id`)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_0900_ai_ci comment='站台拣选单历史表';

-- PRINT '------ 站台表 ------'
drop table if exists `station`;
create table `station` (
  `id` int not null auto_increment comment 'id',
  `hoister_id` int default null comment '提升机id',
  `current_lx_no` varchar(20) default null comment '当前料箱',
  `current_station_pick_id` int default null comment '当前拣选单id',
  `lx_max_count` int default null comment '料箱最大数量',
  `is_claim` int default null  comment '是否索取：1、索取订单，2、不索取任务 3 索取盘点任务',
  `is_lock` int default null  comment '是否锁定：1、锁定，2、不锁定',
  `station_task_type` int not null  comment '站台作业类型 0:空闲  1:拣选作业  2 盘点作业',
  `create_time` date not null  comment '创建时间',
  `update_time` date default null  comment '修改时间',
  `login_userid` int default null  comment '当前登陆用户id',
  `login_username` varchar(50) default null  comment '当前登陆用户姓名',
  `ip_location` varchar(50) not null comment 'ip地址',
primary key (`id`)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_0900_ai_ci comment='站台拣选单历史表'; 
alter table station add
  constraint fk_pick_id foreign key(current_station_pick_id) references station_pick(id);
  

  
  
  
-- PRINT '------ 补货站台表 ------'
drop table if exists `repair_station`;
CREATE TABLE `repair_station` (
  `station_no` varchar(50) NOT NULL COMMENT '补货站台编号',
  `curr_work_container_no` varchar(20) DEFAULT NULL COMMENT '当前补货料箱',
  `last_update_date` datetime DEFAULT NULL COMMENT '最后更新时间',
PRIMARY KEY (`station_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `repair_station` VALUES ('001', NULL, '2020-4-25 10:27:39');

