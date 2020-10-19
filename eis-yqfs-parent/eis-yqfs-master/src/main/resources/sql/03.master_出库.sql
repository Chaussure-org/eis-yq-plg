-- PRINT '------ 出库单汇总 ------'
drop table if exists `outbound_task_hz`;
CREATE TABLE `outbound_task_hz` (
  `id` int(11) NOT NULL auto_increment COMMENT 'id',
  `picking_order_id` int(20) DEFAULT NULL  COMMENT '拣选单Id',
  `outbound_code` varchar(50) DEFAULT NULL COMMENT '出库单编号',
  `bill_no` varchar(50) DEFAULT NULL COMMENT '清单编号',
  `loan_no` varchar(50) DEFAULT NULL COMMENT '借件单号',
  `loan_date_time` datetime DEFAULT NULL COMMENT '借件单日期',
  `expect_time` datetime DEFAULT NULL COMMENT '时效（YYYY-MM-DD HH:MM:SS）',
  `priority` int(20) DEFAULT NULL  COMMENT '优先级',
  `pick_date_time` datetime DEFAULT NULL COMMENT '拣选开始时间',
  `order_box_no` varchar(50) DEFAULT NULL  COMMENT '订单箱编号',
  `station_id` int(20) DEFAULT NULL  COMMENT '站台id',
  `dealer_id` int(20) DEFAULT NULL  COMMENT '经销商id',
  `is_add_pool` int(20) NOT NULL  COMMENT '是否加入订单池（0：否，1：是）',
  `goods_factory` varchar(20) DEFAULT NULL  COMMENT '发货工厂',
  `transfer_library` varchar(20) DEFAULT NULL  COMMENT '中转库',
  `deliver_goods_mode` varchar(20) DEFAULT NULL  COMMENT '发货方式',
  `shipping_point` varchar(20) DEFAULT NULL  COMMENT '发运点',
  `store_no` varchar(20) DEFAULT NULL  COMMENT '仓库号',
  `deliver_goods_type` varchar(20) DEFAULT NULL  COMMENT '发运品种',
  `dealer_address` varchar(20) DEFAULT NULL  COMMENT '客户地址',
  `weight` double DEFAULT NULL  COMMENT '总重量',
  `net_weight` double DEFAULT NULL  COMMENT '净重',
  `money` double DEFAULT NULL  COMMENT '金额',
  `confirm_time` timestamp DEFAULT NULL  COMMENT '确认时间',
  `type` varchar(20) DEFAULT NULL  COMMENT '订单类型',
  `crtat_staff` varchar(20) DEFAULT NULL  COMMENT '创建人',
  `deal_time` timestamp DEFAULT NULL COMMENT '建单时间',
  `order_create_time` timestamp DEFAULT NULL  COMMENT '确认时间',
  `create_time` timestamp DEFAULT NULL COMMENT '创建时间（YYYY-MM-DD HH:MM:SS）',
  `last_date_time` timestamp DEFAULT NULL COMMENT '最后操作时间（YYYY-MM-DD HH:MM:SS）',
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='出库单汇总';

-- PRINT '------ 出库单明细表 ------'
CREATE TABLE `outbound_task_mx` (
  `id` int(11) NOT NULL auto_increment COMMENT 'id',
  `outbound_task_hz_id` int(11) NOT NULL COMMENT '入库单汇总id',
  `goods_id` int(11) NOT NULL COMMENT '商品id',
  `plan_num` int(11) NOT NULL COMMENT '计划数量',
  `actual_num` int(11) NOT NULL COMMENT '实际数量',
  `is_complete` int(1) NOT NULL COMMENT '是否完成：（0：否，1：是）',
  `is_not_pick` int(1) NOT NULL COMMENT '是否断拣：（0：否，1：是）',
  `create_time` timestamp DEFAULT NULL COMMENT '创建时间（YYYY-MM-DD HH:MM:SS）',
  `update_time` timestamp DEFAULT NULL COMMENT '修改时间（YYYY-MM-DD HH:MM:SS）',
PRIMARY KEY (`id`),
KEY `fk_mx_id` (`outbound_task_hz_id`),
CONSTRAINT `fk_mx_id` FOREIGN KEY (`outbound_task_hz_id`) REFERENCES `outbound_task_hz` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='出库单明细表';


-- PRINT '------ 出库单汇总历史 ------'
drop table if exists `outbound_task_hz_history`;
CREATE TABLE `outbound_task_hz_history` (
 `id` int(11) NOT NULL auto_increment COMMENT 'id',
  `picking_order_id` int(20) DEFAULT NULL  COMMENT '拣选单Id',
  `outbound_code` varchar(50) DEFAULT NULL COMMENT '出库单编号',
  `bill_no` varchar(50) DEFAULT NULL COMMENT '清单编号',
  `loan_no` varchar(50) DEFAULT NULL COMMENT '借件单号',
  `loan_date_time` datetime DEFAULT NULL COMMENT '借件单日期',
  `expect_time` datetime DEFAULT NULL COMMENT '时效（YYYY-MM-DD HH:MM:SS）',
  `priority` int(20) DEFAULT NULL  COMMENT '优先级',
  `pick_date_time` datetime DEFAULT NULL COMMENT '拣选开始时间',
  `order_box_no` varchar(50) DEFAULT NULL  COMMENT '订单箱编号',
  `station_id` int(20) DEFAULT NULL  COMMENT '站台id',
  `dealer_id` int(20) DEFAULT NULL  COMMENT '经销商id',
  `is_add_pool` int(20) NOT NULL  COMMENT '是否加入订单池（0：否，1：是）',
  `goods_factory` varchar(20) DEFAULT NULL  COMMENT '发货工厂',
  `transfer_library` varchar(20) DEFAULT NULL  COMMENT '中转库',
  `deliver_goods_mode` varchar(20) DEFAULT NULL  COMMENT '发货方式',
  `shipping_point` varchar(20) DEFAULT NULL  COMMENT '发运点',
  `store_no` varchar(20) DEFAULT NULL  COMMENT '仓库号',
  `deliver_goods_type` varchar(20) DEFAULT NULL  COMMENT '发运品种',
  `dealer_address` varchar(20) DEFAULT NULL  COMMENT '客户地址',
  `weight` double DEFAULT NULL  COMMENT '总重量',
  `net_weight` double DEFAULT NULL  COMMENT '净重',
  `money` double DEFAULT NULL  COMMENT '金额',
  `confirm_time` timestamp DEFAULT NULL  COMMENT '确认时间',
  `type` varchar(20) DEFAULT NULL  COMMENT '订单类型',
  `crtat_staff` varchar(20) DEFAULT NULL  COMMENT '创建人',
  `deal_time` timestamp DEFAULT NULL COMMENT '建单时间',
  `order_create_time` timestamp DEFAULT NULL  COMMENT '确认时间',
  `create_time` timestamp DEFAULT NULL COMMENT '创建时间（YYYY-MM-DD HH:MM:SS）',
  `last_date_time` timestamp DEFAULT NULL COMMENT '最后操作时间（YYYY-MM-DD HH:MM:SS）',
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='出库单汇总历史';

-- PRINT '------ 出库单明细表历史 ------'
CREATE TABLE `outbound_task_mx_history` (
  `id` int(11) NOT NULL COMMENT 'id',
  `outbound_task_hz_id` int(11) NOT NULL COMMENT '入库单汇总id',
  `goods_id` int(11) NOT NULL COMMENT '商品id',
  `plan_num` int(11) NOT NULL COMMENT '计划数量',
  `actual_num` int(11) NOT NULL COMMENT '实际数量',
  `is_complete` int(1) NOT NULL COMMENT '是否完成：（0：否，1：是）',
  `is_not_pick` int(1) NOT NULL COMMENT '是否断拣：（0：否，1：是）',
  `create_time` timestamp DEFAULT NULL COMMENT '创建时间（YYYY-MM-DD HH:MM:SS）',
  `update_time` timestamp DEFAULT NULL COMMENT '修改时间（YYYY-MM-DD HH:MM:SS）',
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='出库单明细表历史';

-- PRINT '------商品出库预警表  ------'
CREATE TABLE `outbound_warn` 
(
  `goods_id`     int(11) NOT NULL COMMENT '商品id',
  `expect_outnum`    int(11) DEFAULT 0 COMMENT '预计出库值',
  `last_update_time` DATE COMMENT '最后更新时间'
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品出库预警表';


 


