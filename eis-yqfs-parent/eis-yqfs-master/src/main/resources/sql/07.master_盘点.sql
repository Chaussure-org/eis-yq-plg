create table `pd_task` (
  `id` int not null auto_increment comment 'id',
  `wms_pd_no` varchar(50) not null comment 'wms盘点单据编号',
  `remark` varchar(50) default null comment '备注',
  `pd_state` int not null comment '盘点状态 1:已创建 2:待盘点,3:盘点中,4:已完成,5上传完成',
  `pd_type` int not null comment '盘点类型 1 eis内部盘点  2 wms下发的盘点计划',
  `create_time` date not null comment '创建时间',
  `xiafa_time` date default null comment '下发时间',
  `start_time` date default null comment '开始时间',
  `end_time` date default null comment '作业完成时间',
  `upload_time` date default null comment '上传时间',
  primary key (`id`)
)comment='盘点计划';

create table `pd_task_his` (
  `id` int not null comment 'id',
  `wms_pd_no` varchar(50) not null comment 'wms盘点单据编号',
  `remark` varchar(50) default null comment '备注',
  `pd_state` int not null comment '盘点状态 1:已创建 2:待盘点,3:盘点中,4:已完成,5上传完成',
  `pd_type` int not null comment '盘点类型 1 eis内部盘点  2 wms下发的盘点计划',
  `create_time` date not null comment '创建时间',
  `xiafa_time` date default null comment '下发时间',
  `start_time` date default null comment '开始时间',
  `end_time` date default null comment '作业完成时间',
  `upload_time` date default null comment '上传时间',
  primary key (`id`)
)comment='盘点计划历史';

create table `pd_task_owner` (
  `id` int not null auto_increment comment 'id',
  `pd_task_id` int not null comment '盘点计划id',
  `owner_id` int not null comment '业主id',
  primary key (`id`)
)comment='盘点计划业主表';
alter table pd_task_owner  add  
  constraint fk_pto_taskid foreign key(pd_task_id) references pd_task (id);
alter table pd_task_owner  add  
  constraint fk_pto_ownerid foreign key(owner_id) references owner (id);
  
create table `pd_task_commodityrule` (
  `id` int not null auto_increment comment 'id',
  `pd_task_id` int not null comment '盘点计划id',
  `commodity_id` int not null comment '商品id',
  `create_time` date not null comment '创建时间',
  primary key (`id`)
)comment='盘点商品规则';
alter table pd_task_commodityrule add  
  constraint fk_ptc_taskid foreign key(pd_task_id) references pd_task (id);
alter table pd_task_commodityrule add  
  constraint fk_ptc_commodityid foreign key(commodity_id) references goods (id);
create unique index idx_pt_commodity_id on pd_task_commodityrule 
(
	pd_task_id asc,
	commodity_id asc
);

create table `pd_task_lotrule` (
  `id` int not null auto_increment comment 'id',
  `pd_task_id` int not null comment '盘点计划id',
  `lot_id` int not null comment '批次id',
  `create_time` date not null comment '创建时间',
  primary key (`id`)
)comment='盘点批次规则';
alter table pd_task_lotrule add  
  constraint fk_ptl_taskid foreign key(pd_task_id) references pd_task (id);
alter table pd_task_lotrule add  
  constraint fk_ptl_lotid foreign key(lot_id) references lotinfo (id);
create unique index idx_pt_lot_id on pd_task_lotrule 
(
	pd_task_id asc,
	lot_id asc
);

create table `pd_task_boxrule` (
  `id` int not null auto_increment comment 'id',
  `pd_task_id` int not null comment '盘点计划id',
  `lot_id` int not null comment '批次id',
  `box_no` varchar(20) not null comment '箱号',
  `task_state` int not null comment '任务状态 0创建 1下发 2 已出库 3盘点开始 4盘点完成',
  `stationid` int not null comment '站台id',
  `create_time` date not null comment '创建时间',
  `xiafa_time` date not null comment '下发时间',
  `chuku_time` date not null comment '出库时间',
  `pd_start_time` date not null comment '盘点开始时间',
  `pd_end_time` date not null comment '盘点结束时间',
  `finish_reason` varchar(200) not null comment '完成原因',
  primary key (`id`)
)comment='盘点料箱规则';
alter table pd_task_boxrule add
  constraint fk_ptb_taskid foreign key(pd_task_id) references pd_task (id);
alter table pd_task_boxrule add
  constraint fk_ptb_stationid foreign key(stationid) references station (id);
create index idx_ptid on pd_task_boxrule 
(
	pd_task_id asc
);
create index idx_ptbox on pd_task_boxrule 
(
	box_no asc
);
create index idx_ptb_stationid on pd_task_boxrule 
(
	stationid asc
);
create unique index idx_pt_box_no on pd_task_boxrule 
(
	pd_task_id asc,
	box_no asc
); 

create table `pd_task_boxrule_his` (
  `id` int not null comment 'id',
  `pd_task_id` int not null comment '盘点计划id',
  `lot_id` int not null comment '批次id',
  `box_no` varchar(20) not null comment '箱号',
  `task_state` int not null comment '任务状态 0创建 1下发 2 已出库 3盘点开始 4盘点完成',
  `stationid` int not null comment '站台id',
  `create_time` date not null comment '创建时间',
  `xiafa_time` date not null comment '下发时间',
  `chuku_time` date not null comment '出库时间',
  `pd_start_time` date not null comment '盘点开始时间',
  `pd_end_time` date not null comment '盘点结束时间',
  primary key (`id`)
)comment='盘点料箱规则历史';
alter table pd_task_boxrule_his add
  constraint fk_ptbhis_stationid foreign key(stationid) references station (id);
create index idx_pthis_stationid on pd_task_boxrule_his 
(
  stationid asc
);
create index idx_pthis_box_no on pd_task_boxrule_his 
(
  box_no asc
);

create table `pd_task_boxrule_detail` (
  `id` int not null auto_increment comment 'id',
  `pd_task_id` int not null comment '盘点计划id',
  `box_no` varchar(20) not null comment '箱号',
  `commodity_name` varchar(50) not null comment '商品名称',
  `bar_code` varchar(50) not null comment '商品条码',
  `original_count` int not null comment '原始数量',
  `modify_count` int not null comment '修改数量',
  `create_time` date not null comment '创建时间',
  `frequency` int not null comment '次数',
  primary key (`id`)
)comment='盘点料箱明细';
alter table pd_task_boxrule_detail add
  constraint fk_ptbd_taskid foreign key(pd_task_id) references pd_task (id);
create index idx_ptbd_stationid on pd_task_boxrule_detail 
(
	stationid asc
);
create index idx_ptbd_taskod_boxno on pd_task_boxrule_detail 
(
	pd_task_id asc,
    box_no asc
);

create table `pd_task_boxrule_detail_his` (
  `id` int not null comment 'id',
  `pd_task_id` int not null comment '盘点计划id',
  `box_no` varchar(20) not null comment '箱号',
  `commodity_name` varchar(50) not null comment '商品名称',
  `bar_code` varchar(50) not null comment '商品条码',
  `original_count` int not null comment '原始数量',
  `modify_count` int not null comment '修改数量',
  `create_time` date not null comment '创建时间',
  `frequency` int not null comment '次数',
  primary key (`id`)
)comment='盘点料箱明细';
alter table pd_task_boxrule_detail_his
	add primary key (id);
alter table pd_task_boxrule_detail_his
	add constraint fk_ptbd_taskhisid foreign key (pd_task_id)
references pd_task_his (id);

