-- PRINT '------ 合箱计划汇总表 ------'
drop table if exists `assembl_box_hz`;
create table `assembl_box_hz` (
                                  `id` int not null auto_increment comment 'id',
                                  `goods_id` int default null comment '商品id',
                                  `station_id` int default null comment '作业站台id',
                                  `task_state` int not null comment '任务状态 0创建 1下发',
                                  `create_time` datetime comment '创建时间（YYYY-MM-DD HH:MM:SS）',
                                  primary key (`id`)
    )comment='合箱计划汇总表';
alter table assembl_box_hz add UNIQUE KEY assembl_box_hz_goods_id(`goods_id`);
alter table assembl_box_hz add
    constraint fk_assembl_hz_goods_id foreign key(goods_id) references goods (id);
alter table assembl_box_hz add
    constraint fk_assembl_hz_station_id foreign key(station_id) references station_pick (id);

-- PRINT '------ 合箱计划明细表 ------'
create table `assembl_box_mx` (
  `id` int not null auto_increment comment 'id',
  `assembl_box_hz_id` int not null comment '合箱计划汇总id',
  `container_no` varchar(10) not null comment '容器编号',
  `task_state` int not null comment '任务状态 0未开始 1进行中 2已完成',
  `create_time` datetime comment '创建时间（YYYY-MM-DD HH:MM:SS）',
  primary key (`id`)
)comment='合箱计划明细表';
alter table assembl_box_mx  add
  constraint fk_assembl_mx_id foreign key(assembl_box_hz_id) references assembl_box_hz (id);

-- PRINT '------ 合箱计划操作表 ------'
create table `assembl_box_operate`(
  `id` int not null auto_increment comment 'id',
  `assembl_box_hz_id` int not null comment '合箱计划汇总id',
  `source_sub_container_no` varchar(10) not null comment '原子容器编号',
  `target_sub_container_no` varchar(10) not null comment '目子标容器编号',
  `goods_num` int(11) NOT NULL COMMENT '商品数量',
  `create_time` datetime comment '创建时间（YYYY-MM-DD HH:MM:SS）',
  primary key (`id`)
)comment='合箱计划操作表';
alter table assembl_box_operate  add
  constraint fk_assembl_box_operate_hz_id foreign key(assembl_box_hz_id) references assembl_box_hz (id);



-- PRINT '------ 合箱计划汇总历史表 ------'
create table `assembl_box_hz_history` (
  `id` int not null comment 'id',
  `goods_id` int not null comment '商品id',
  `station_id` int not null comment '作业站台id',
  `task_state` int not null comment '任务状态 0创建 1下发',
  `create_time` datetime comment '创建时间（YYYY-MM-DD HH:MM:SS）',
  primary key (`id`)
)comment='合箱计划汇总历史表';

-- PRINT '------ 合箱计划明细历史表 ------'
create table `assembl_box_mx_history` (
  `id` int not null comment 'id',
  `assembl_box_hz_id` int not null comment '合箱计划汇总id',
  `container_no` varchar(10) not null comment '容器编号',
  `task_state` int not null comment '任务状态  0未开始 1进行中 2已完成',
  `create_time` datetime comment '创建时间（YYYY-MM-DD HH:MM:SS）',
  primary key (`id`)
)comment='合箱计划明细历史表';

-- PRINT '------ 合箱计划操作历史表 ------'
create table `assembl_box_operate_history`(
  `id` int not null comment 'id',
  `assembl_box_hz_id` int not null comment '合箱计划汇总id',
  `source_container_no` varchar(10) not null comment '原容器编号',
  `target_container_no` varchar(10) not null comment '目标容器编号',
  `goods_num` int(11) NOT NULL COMMENT '商品数量',
  `create_time` datetime comment '创建时间（YYYY-MM-DD HH:MM:SS）',
  primary key (`id`)
)comment='合箱计划操作历史表';

