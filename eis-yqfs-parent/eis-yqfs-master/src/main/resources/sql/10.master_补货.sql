-- PRINT '------ 补货汇总 ------'
drop table if exists `repair_task_hz`;
create table `repair_task_hz` (
  `id` int not null auto_increment comment 'id',
  `goods_id` int not null comment '商品id',
  `operator` varchar(50) not null comment '操作人',
  `repair_status` int(1) not null comment '状态（1：进行中，2：已完成）',
  `create_time` datetime comment '创建时间（YYYY-MM-DD HH:MM:SS）',
  primary key (`id`)
)comment='补货汇总';

-- PRINT '------ 补货明细表 ------'
drop table if exists `repair_task_mx`;
create table `repair_task_mx` (
  `id` int not null auto_increment comment 'id',
  `repair_task_hz_id` int not null comment '入库单汇总id',
  `container_no` varchar(20) not null comment '容器编号',
  `goods_id` int not null comment '商品id',
  `repair_status` int(1) not null comment '状态（1：进行中，2：已完成）',
  `create_time` datetime comment '创建时间（YYYY-MM-DD HH:MM:SS）',
  `update_time` datetime comment '修改时间（YYYY-MM-DD HH:MM:SS）',
  primary key (`id`)
)comment='补货明细表';
alter table repair_task_mx  add  
  constraint fk_repair_task_mx_id foreign key(repair_task_hz_id) references repair_task_hz (id);
  
-- PRINT '------ 补货汇总历史 ------'
drop table if exists `repair_task_hz_his`;
create table `repair_task_hz_his` (
  `id` int not null comment 'id',
  `container_no` varchar(20) not null comment '容器编号',
  `operator` varchar(50) not null comment '操作人',
  `inbound_status` int(1) not null comment '状态（1：进行中，2：已完成）',
  `create_time` datetime comment '创建时间（YYYY-MM-DD HH:MM:SS）',
  primary key (`id`)
)comment='补货汇总历史';

-- PRINT '------ 补货明细表历史 ------'
drop table if exists `repair_task_mx_his`;
create table `repair_task_mx_his` (
  `id` int not null comment 'id',
  `repair_task_hz_id` int not null comment '入库单汇总id',
  `container_no` varchar(20) not null comment '容器编号',
  `goods_id` int not null comment '商品id',
  `repair_status` int(1) not null comment '状态（1：进行中，2：已完成）',
  `create_time` datetime comment '创建时间（YYYY-MM-DD HH:MM:SS）',
  `update_time` datetime comment '修改时间（YYYY-MM-DD HH:MM:SS）',
  primary key (`id`)
)comment='补货明细表历史';
alter table repair_task_mx_his  add
  constraint fk_repair_task_mx_his_id foreign key(repair_task_hz_id) references repair_task_hz_his (id);


-- PRINT '------ 补货计划 ------'
drop table if exists `repair_plan`;
create table `repair_plan` (
  `id` int not null AUTO_INCREMENT comment 'id',
  `goods_id` int not null comment '商品id',
  `operator` varchar(50) not null comment '操作人',
  `create_time` datetime comment '创建时间（YYYY-MM-DD HH:MM:SS）',
primary key (`id`)
)comment='补货计划';


-- PRINT '------ 补货计划明细 ------'
drop table if exists `repair_plan_mx`;
create table `repair_plan_mx` (
  `id` int not null AUTO_INCREMENT comment 'id',
  `repair_plan_id` int not null comment '补货计划id',
  `container_no` varchar(20) not null comment '容器编号',
  `repair_status` int(1) not null comment '状态1 未开始 ,  2 已下发 ,  3 补货完成',
  `create_time` datetime comment '创建时间（YYYY-MM-DD HH:MM:SS）',
  `container_sub_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '子容器编号',
primary key (`id`)
)comment='补货计划明细';
alter table repair_plan_mx  add
  constraint fk_repair_plan_mx_id foreign key(repair_plan_id) references repair_plan (id);


-- PRINT '------ 补货计划历史 ------'
drop table if exists `repair_plan_his`;
create table `repair_plan_his` (
  `id` int not null comment 'id',
  `goods_id` int not null comment '商品id',
  `operator` varchar(50) not null comment '操作人',
  `create_time` datetime comment '创建时间（YYYY-MM-DD HH:MM:SS）',
  `container_sub_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '子容器编号',
primary key (`id`)
)comment='补货计划历史';


-- PRINT '------ 补货计划明细历史 ------'
drop table if exists `repair_plan_mx_his`;
create table `repair_plan_mx_his` (
  `id` int not null comment 'id',
  `repair_plan_id` int not null comment '补货计划id',
  `container_no` varchar(20) not null comment '容器编号',
  `repair_status` int(1) not null comment '状态1 未开始 ,  2 已下发 ,  3 补货完成',
  `create_time` datetime comment '创建时间（YYYY-MM-DD HH:MM:SS）',
primary key (`id`)
)comment='补货计划明细历史';
alter table repair_plan_mx_his  add
  constraint fk_repair_plan_mx_his_id foreign key(repair_plan_id) references repair_plan_his (id);
  


