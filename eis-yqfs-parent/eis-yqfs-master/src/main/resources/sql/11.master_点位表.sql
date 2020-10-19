  
-- PRINT '------ BCR位置表 ------'
drop table if exists `BCR_LOCATION`;
create table `BCR_LOCATION` (
  `BCR_ID` varchar(20) not null  comment 'BCRID',
  `BCR_NAME` varchar(20) not null comment 'BCR名称',
  `BCR_TYPE` int not null comment '1、入库口;2、站台BCR;3、尺寸检测',
  `STATION_ID` int default null comment '站台ID',   
primary key (`BCR_ID`)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_0900_ai_ci comment='BCR位置表'; 

-- PRINT '------ 点位表 ------'
drop table if exists `POINT_LOCATION`;
create table `POINT_LOCATION` (
  `POINT_ID` varchar(20) not null  comment 'POINTID',
  `POINT_NAME` varchar(20) not null comment 'POINT名称',
  `POINT_TYPE` int not null comment '点位类型1、入库接驳口;2、出库站台接驳口;3、站台BCR点4、异常出库口',
  `STATION_ID` int default null comment '站台ID', 
  `HOISTER_ID` int default null comment '提升机ID', 
primary key (`POINT_ID`)
) engine=innodb default charset=utf8mb4 collate=utf8mb4_0900_ai_ci comment='点位表';  