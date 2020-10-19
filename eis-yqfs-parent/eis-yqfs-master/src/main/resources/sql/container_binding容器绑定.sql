
DROP TABLE IF EXISTS `container_binding_hz`;
CREATE TABLE `container_binding_hz` (
  `container_no` varchar(50) NOT NULL  COMMENT '容器编号',
  `picking_order_id` int(11) NOT NULL COMMENT '拣选单ID',  
  `xk_store_id` int(11) DEFAULT NULL COMMENT '箱库库存Id',
  `zt_store_id` int(11) DEFAULT NULL COMMENT '在途库存ID',
  `station_id` int(11) NOT NULL COMMENT '站台ID',
  `is_complete` int  NOT NULL COMMENT '是否完成(0、未完成，1、完成)',
  PRIMARY KEY (`container_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='容器绑定汇总';


-- DROP TABLE IF EXISTS `container_sub_binding_hz`;
-- CREATE TABLE `container_sub_binding_hz` (
--   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
--  `container_no` varchar(50) NOT NULL  COMMENT '容器编号',
--  `container_sub_no` varchar(50) NOT NULL COMMENT '货格编号',
--  `create_time` datetime NOT NULL COMMENT '创建时间',
--  PRIMARY KEY (`id`) USING BTREE
-- ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='子容器绑定汇总';

DROP TABLE IF EXISTS `container_sub_binding_mx`;
CREATE TABLE `container_sub_binding_mx`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `container_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '容器编号',
  `container_sub_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '货格编号',
  `order_hz_id` int(11) NOT NULL COMMENT '订单汇总ID',
  `order_mx_id` int(11) NOT NULL COMMENT '订单明细ID',
  `binding_num` int(11) NOT NULL COMMENT '绑定数量',
  `is_finish` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '是否完成(0、未完成，1、完成)',
  `create_time` int(11) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK_CONTAINER_NO`(`container_no`) USING BTREE,
  UNIQUE INDEX `UQ_HUOGE_MXID`(`container_sub_no`, `order_mx_id`) USING BTREE,
  CONSTRAINT `FK_CONTAINER_NO` FOREIGN KEY (`container_no`) REFERENCES `container_binding_hz` (`container_no`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '子容器绑定明细' ROW_FORMAT = Dynamic;
