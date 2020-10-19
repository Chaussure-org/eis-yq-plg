-- PRINT '------ 入库汇总 ------'
create table `inbound_task_hz` (
  `id` int not null auto_increment comment 'id',
  `container_no` varchar(20) not null comment '容器编号',
  `operator` varchar(50) not null comment '操作人',
  `inbound_status` int(1) not null comment '状态（1：进行中，2：已完成）',
  `create_time` DATE comment '创建时间（YYYY-MM-DD HH:MM:SS）',
  primary key (`id`)
)comment='入库单汇总';

-- PRINT '------ 入库明细表 ------'
create table `inbound_task_mx` (
  `id` int not null auto_increment comment 'id',
  `inbound_task_hz_id` int not null comment '入库单汇总id',
  `container_sub_no` varchar(20) not null comment '子容器编号',
  `goods_id` int not null comment '商品id',
  `goods_num` int not null comment '商品数量',
  `expiry_date`	date comment '有效日期',
  `create_time` DATE comment '创建时间（YYYY-MM-DD HH:MM:SS）',
  primary key (`id`)
)comment='入库单明细表';
alter table inbound_task_mx  add  
  constraint fk_inbound_task_mx_id foreign key(inbound_task_hz_id) references inbound_task_hz (id);
  
-- PRINT '------ 入库汇总历史表 ------'
create table `inbound_task_hz_his` (
  `id` int not null comment 'id',
  `container_no` varchar(20) not null comment '容器编号',
  `operator` varchar(50) not null comment '操作人',
  `inbound_status` int(1) not null comment '状态（1：进行中，2：已完成）',
  `create_time` DATE comment '创建时间（YYYY-MM-DD HH:MM:SS）',
  primary key (`id`)
)comment='入库汇总历史表';

-- PRINT '------ 入库单明细表历史 ------'
create table `inbound_task_mx_his` (
  `id` int not null comment 'id',
  `inbound_task_hz_id` int not null comment '入库单汇总id',
  `container_sub_no` varchar(20) not null comment '子容器编号',
  `goods_id` int not null comment '商品id',
  `goods_num` int not null comment '商品数量',
   `expiry_date`	date comment '有效日期',
  `create_time` DATE comment '创建时间（YYYY-MM-DD HH:MM:SS）',
  primary key (`id`)
)comment='入库单明细表历史';
-- PRINT '------ 空箱入库表 ------'
create table `empty_container_inbound_task` (
  `id` int not null auto_increment comment 'id',
  `container_no` varchar(20) not null comment '容器编号',
  `create_time` datetime comment '创建时间（YYYY-MM-DD HH:MM:SS）',
  primary key (`id`)
)comment='空箱入库表';

 create table `distribute_roadway` (
  `container_no` varchar(20) not null comment '容器编号',
  `inBound_time` datetime not null comment '分配时间',
  `roadway`  int not null comment '巷道',
  primary key (`container_no`)
)comment='分配任务表';



