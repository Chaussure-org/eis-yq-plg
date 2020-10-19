-- PRINT '------ 业主资料表 ------'
create table `owner` (
  `id` int not null auto_increment comment 'id',
  `owner_no` varchar(50) default null comment '业主编号',
  `owner_name` varchar(50) not null comment '业主名称',
  `zhuj_code`  varchar(50) comment '助计码',
  `owner_simplename` varchar(50) comment '业主简称',
  `beactive` int(1) not null comment '是否活动：1、活动，2、不活动',
  `create_time` DATE comment '创建时间（YYYY-MM-DD HH:MM:SS）',
  `update_time` DATE comment '修改时间（YYYY-MM-DD HH:MM:SS）',
  primary key (`id`)
)comment='业主资料表';

-- PRINT '------ 供应商资料表 ------'
CREATE TABLE `supplier` (
	`id` int(11) NOT NULL auto_increment COMMENT 'id',
	`supplier_no` varchar(50) DEFAULT NULL COMMENT '供应商编号',
	`supplier_name` varchar(50) NOT NULL COMMENT '供应商名称',
	`zhuj_code` varchar(50) DEFAULT NULL COMMENT '助计码',
	`beactive` int(1) NOT NULL COMMENT '是否活动：1、活动，2、不活动',
	`create_time` timestamp DEFAULT NULL COMMENT '创建时间（YYYY-MM-DD HH:MM:SS）',
	`update_time` timestamp DEFAULT NULL COMMENT '修改时间（YYYY-MM-DD HH:MM:SS）',
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='供应商资料表';

-- PRINT '------ 经销商资料表 ------'
CREATE TABLE `dealer` (
	`id` int(11) NOT NULL auto_increment COMMENT 'id',
	`dealer_no` varchar(50) DEFAULT NULL COMMENT '经销商编号',
	`dealer_name` varchar(50) NOT NULL COMMENT '经销商名称',
	`brand` varchar(50) DEFAULT NULL COMMENT '品牌',
	`normal` varchar(50) DEFAULT NULL COMMENT '正常',
	`urgent` varchar(50) DEFAULT NULL COMMENT '紧急',
	`last_order_time` varchar(50) DEFAULT NULL COMMENT '截单时间',
	`route` varchar(50) DEFAULT NULL COMMENT '截单时间',
	`dealer_type` int DEFAULT NULL COMMENT '类型',
	`delivery_date`  varchar(50) DEFAULT NULL COMMENT '送货日期',
	`create_time` timestamp DEFAULT NULL COMMENT '创建时间（YYYY-MM-DD HH:MM:SS）',
	`update_time` timestamp DEFAULT NULL COMMENT '修改时间（YYYY-MM-DD HH:MM:SS）',
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='经销商资料表';


-- print '------ 商品资料表 ------'
create table goods (
	id int not null auto_increment comment 'id',
	owner_id int not null comment '业主id',
	goods_no  varchar(30) not null comment '商品编号',
	goods_name varchar(100) not null comment '商品名称',
	specification varchar(200) default null comment '规格',
	unit_catch_weight float default null comment '净重（克：g）',							
	unit_gross_weight float default null comment '毛重（克：g）',								
	length float default null comment '长（毫米：mm）',				
	width float default null comment '宽（毫米：mm）',							
	height float default null comment '高（毫米：mm）',							
	unit_volume float default null comment '体积（立方毫米：mm3）',							
	package_number int default null comment '包装数量',	
	goods_state int default null comment '商品状态1：合格,2：不合格',
	recom_number int default null comment '推荐数量',
	recom_type  int default null comment '推荐货格（1.整箱、2.日字、3.田字）',
	dealer_id int default null comment '经销商id',
	supplier_id int default null comment '供应商id',
	create_time timestamp not null comment '创建时间',                    
	update_time timestamp default null comment '修改时间',	
	primary key (`id`)
)comment='商品资料表';


-- print '------ 商品条码资料表 ------'
create table goods_bar_code (
	id int not null auto_increment comment 'id',
	goods_id int not null comment '商品id(外键)',								 
	bar_code varchar(30) not null comment ' 商品条码',						
	isdefault int not null comment '是否默认条码（0：否，1：是）',													
	create_time timestamp not null comment '创建时间',
	primary key (`id`)
)comment='商品条码资料表';
alter table goods_bar_code  add  
  constraint fk_goods_id foreign key(goods_id) references goods (id);
  
---- PRINT '------ 批号资料表 ------'
-- create table lotinfo (
--	id int not null comment 'id',
--	owner_id int not null comment '业主id(外键)',
--	goods_id int not null comment '商品id(外键)',							
--	lot varchar(50) not null comment '批号',
--	lot_num varchar(50) not null comment '批次号',
--	production_date	date comment '生产日期',	
--	expiry_date	date comment '有效日期',
--	status int comment '批次状态（1：正常，2：异常）',
--	remark varchar(500) comment '备注',
--	create_time date not null comment '创建时间',                   
--	update_time date comment '修改时间',
--	primary key (`id`)
-- )comment='批号资料表';
-- alter table lotinfo  add  
--  constraint fk_lotinfo_goods_id foreign key(goods_id) references goods (id);
-- alter table lotinfo  add  
--  constraint fk_lotinfo_owner_id foreign key(owner_id) references owner (id);


 


