CREATE TABLE `back_store_plan` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '退库计划名称',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `state` int(11) DEFAULT '0' COMMENT '计划状态(0新建;10已下发;20执行中)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='退库计划';

CREATE TABLE `back_store_plan_mx` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `back_store_plan_id` int(11) NOT NULL COMMENT '退库计划主键',
  `box_no` varchar(255) NOT NULL COMMENT '料箱号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `back_store_plan_mx_index` (`back_store_plan_id`,`box_no`) COMMENT '计划id+料箱唯一',
  CONSTRAINT `fk_back_store_plan_mx` FOREIGN KEY (`back_store_plan_id`) REFERENCES `back_store_plan` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='退库计划明细';