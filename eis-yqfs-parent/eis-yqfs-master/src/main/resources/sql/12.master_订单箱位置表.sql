-- PRINT '------ 订单箱位置表 ------'
drop table if exists `ddx_location`;
create table `ddx_location` (
  `id` int not null auto_increment comment 'id',
  `station_id` int not null comment '站台id',
  `location_no` varchar default null comment '作业位置',
  `order_box` varchar(20) default null comment '订单箱编号',
  `outbound_task_hz_id` int default null comment '订单id', 
  `create_time` timestamp DEFAULT NULL COMMENT '创建时间（YYYY-MM-DD HH:MM:SS）',
  `update_time` timestamp DEFAULT NULL COMMENT '修改时间（YYYY-MM-DD HH:MM:SS）',
primary key (`id`)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_0900_ai_ci comment='订单箱位置表'; 
