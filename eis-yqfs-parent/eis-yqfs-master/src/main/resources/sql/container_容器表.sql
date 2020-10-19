
DROP TABLE IF EXISTS `container_info`;
CREATE TABLE `container_info` (
  `container_no` varchar(50) NOT NULL  COMMENT '容器编号',
  `layout_type` int(4) NOT NULL COMMENT '容器布局类型（1.整箱、2.日字、3.田字）',
  `station_id` int(11) DEFAULT NULL COMMENT '站台Id（可为空）',
  `inBound_time` datetime DEFAULT NULL COMMENT '入库时间',
  PRIMARY KEY (`container_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='容器表';

DROP TABLE IF EXISTS `container_sub_info`;
CREATE TABLE `container_sub_info` (
  `container_sub_no` varchar(50) NOT NULL  COMMENT '子容器编号',
  `container_no` varchar(50) NOT NULL  COMMENT '容器编号',
  `commodity_id` int(11) DEFAULT NULL COMMENT '商品Id',
  `commodity_num` int(11) DEFAULT NULL COMMENT '商品数量',
  `expiry_date`	date comment '有效日期',
  `inBound_task_detail_id` int(11) DEFAULT NULL COMMENT '入库任务明细ID（可为空）',
  `inBound_plan_num` int(11) DEFAULT NULL COMMENT '入库计划数量（可为空）',
  PRIMARY KEY (`container_sub_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='子容器表';


DROP TABLE IF EXISTS `order_box_info`;
CREATE TABLE `order_box_info` (
  `order_box_no` varchar(50) NOT NULL  COMMENT '订单箱编号',
  `outBound_order_id` int(11) DEFAULT NULL  COMMENT '出库订单ID',
  PRIMARY KEY (`order_box_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单箱表';

DROP TABLE IF EXISTS `order_box_store_info`;
CREATE TABLE `order_box_store_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `order_box_no` varchar(50) NOT NULL  COMMENT '订单箱编号',
  `commodity_id` int(11) NOT NULL  COMMENT '商品ID',
  `commodity_num` int(11) NOT NULL COMMENT '商品数量',
  `lot_id` int(11) DEFAULT NULL COMMENT '批次号Id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单箱库存表';

