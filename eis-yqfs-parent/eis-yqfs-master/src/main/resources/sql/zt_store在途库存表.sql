DROP TABLE IF EXISTS `zt_store`;
CREATE TABLE `zt_store` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `container_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '容器编号',
  `zt_state` int(11) NOT NULL COMMENT '在途状态',
  `container_task_type` int(11) NOT NULL COMMENT '料箱任务类型  1 播种任务 2 盘点任务 3 退库任务 4 补货任务 5 入库换箱 6.合箱任务 7 空托盘入库任务',
  `create_time` datetime DEFAULT NULL,
  `lastck_time` datetime DEFAULT NULL COMMENT '最后一次出库时间',
  `ruku_time` datetime DEFAULT NULL COMMENT '入库时间',
  `lastpd_time` datetime DEFAULT NULL COMMENT '最后一次盘点时间',
  `pd_count` int(11) DEFAULT NULL COMMENT '盘点次数',
  `last_update_time` datetime DEFAULT NULL COMMENT '最后修改时间',
  `station_id` int(11) DEFAULT NULL COMMENT '站台Id',
	`hoister_id` int(11) DEFAULT NULL COMMENT '提升机ID',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `zt_store` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='在途库存表';
ALTER TABLE `zt_store` ADD UNIQUE (`CONTAINER_NO`);