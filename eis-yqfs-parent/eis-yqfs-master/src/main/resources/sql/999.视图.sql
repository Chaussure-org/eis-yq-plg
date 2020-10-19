CREATE 
	OR REPLACE VIEW repairplanview AS SELECT
	p.id AS repairPlanId,
	p.goods_id AS goodsId,
	g.goods_no as goodsNo,
	g.goods_name AS goodsName,
	p.create_time createTime,
	p.operator,
	sum( CASE WHEN m.repair_status = 1 THEN 1 ELSE 0 END ) AS unstartCount,
	sum( CASE WHEN m.repair_status = 2 THEN 1 ELSE 0 END ) AS publishedCount,
	sum( CASE WHEN m.repair_status = 3 THEN 1 ELSE 0 END ) AS finishedCount 
FROM
	repair_plan p
	LEFT JOIN repair_plan_mx m ON p.id = m.repair_plan_id
	LEFT JOIN goods g ON p.goods_id = g.id 
GROUP BY
	p.id,
	p.goods_id,
	g.goods_name,
	p.create_time;


CREATE OR REPLACE VIEW kucun_view AS
select gs.goods_name as goodsName,
       (SELECT GROUP_CONCAT(gbc.bar_code) from goods_bar_code gbc where gbc.goods_id = cs.commodity_id)
                     as barCode,
       ssln.layer as layer,
       ssln.x as x,
       ssln.y as y,
       cs.container_no as containerNo,
       cs.commodity_num as commodityNum,
       cs.container_sub_no as containerSubNo,
       cs.expiry_date as expiryDate,
       ss.IN_STORE_TIME as inStoreTime,
       ssln.depth as depth,
       ss.STORE_STATE as storeState
from container_sub_info cs
         join goods gs on gs.id = cs.commodity_id
         join sx_store ss on ss.CONTAINER_NO = cs.container_no
         join sx_store_location ssln on ssln.id = ss.source_location_id
where ss.STORE_STATE = 20;


CREATE OR REPLACE VIEW ztkucun_view AS
select zt.container_no        as containerNo,
       cs.container_sub_no    as containerSubNo,
       gs.goods_name          as goodsName,
       (SELECT GROUP_CONCAT(gbc.bar_code) from goods_bar_code gbc where gbc.goods_id = cs.commodity_id)
                              as barCode,
       zt.zt_state            as ztState,
       s.id                   as stationNo,
       zt.container_task_type as containerTaskType,
       cs.commodity_num       as commodityNum,
       zt.create_time         as createTime,
       zt.last_update_time    as lastUpdateTime
from zt_store zt
         join container_sub_info cs on cs.container_no = zt.container_no
         join goods gs on gs.id = cs.commodity_id
         left join station s on s.id = zt.station;
         
         
CREATE OR REPLACE VIEW containerinfoview AS
select  c.container_no AS containerNo,
		c.layout_type AS layoutType,
		c.station_id AS stationId,
		c.inBound_time AS inBoundTime
from 
		container_info c;


CREATE OR REPLACE VIEW orderboxinfoview AS
select o.order_box_no AS orderBoxNo,
		o.outBound_order_id AS outBoundOrderId 
from 
		order_box_info o;


CREATE OR REPLACE VIEW storestatusview AS
SELECT
	s.ID AS id,
	s.CONTAINER_NO AS containerNo,
	s.CONTAINER_SUB_NO AS containerSubNo,
	s.STORE_LOCATION_ID AS storeLocationId,
	s.SX_STORE_TYPE AS sxStoreType,
	s.TASK_TYPE AS taskType,
	s.TASK_PROPERTY1 AS taskProperty1,
	s.TASK_PROPERTY2 AS taskProperty2,
	s.BUSINESS_PROPERTY1 AS businessProperty1,
	s.BUSINESS_PROPERTY2 AS businessProperty2,
	s.BUSINESS_PROPERTY3 AS businessProperty3,
	s.BUSINESS_PROPERTY4 AS businessProperty4,
	s.BUSINESS_PROPERTY5 AS businessProperty5,
	s.STORE_STATE AS storeState,
	s.CAR_NO AS carNo,
	s.HOISTER_NO AS hoisterNo,
	s.IN_STORE_TIME AS inStoreTime,
	s.CREATE_TIME AS createTime,
	s.TASK_ID AS taskId,
	s.EMPTY_PALLET_COUNT AS emptyPalletCount,
	s.source_location_id AS sourceLocationId,
	s.WEIGHT AS weight,
	s.container_state AS containerState
FROM
	sx_store s
WHERE
		s.container_state = 2;
	

CREATE OR REPLACE VIEW yqfsstorelocationview AS
select t.id AS storeLocationId,
		t.store_location_group_id AS storeLocationGroupId,
		t.ascent_lock_state AS ascentLockState,
		t.layer AS layer,
		t.dept_num AS deptNum,
		t.location_index AS locationIndex,
		t.store_location_id1 AS storeLocationId1,
		t.store_location_id2 AS storeLocationId2,
		t.x AS x,
		t.y AS y,
		sg.GROUP_NO AS groupNo,
		sg.belong_area AS belongArea,
		sg.IN_OUT_NUM AS inOutNum,
		sg.ENTRANCE AS entrance,
		sg.reserved_location AS reservedLocation,
		t.create_time AS createTime 
from 
		sx_store_location t 
join 	sx_store_location_group sg 
on 		t.store_location_group_id = sg.ID;


CREATE OR REPLACE VIEW sxStoreLocationview AS
select  sx.id AS Id,
		sx.store_no AS StoreNo,
		sx.store_location_group_id AS StoreLocationGroupId,
		sx.layer AS Layer
		sx.x AS X,
		sx.y AS Y,
		sx.store_location_id1 AS StoreLocationId1,
		sx.store_location_id2 AS StoreLocationId2
		sx.ascent_lock_state AS AscentLockState,
		sx.location_index AS LocationIndex,
		sx.depth AS Depth,
		sx.dept_num AS DeptNum
		sx.create_time AS CreateTime,
		sx.vertical_location_group_id AS VerticalLocationGroupId,
		sx.actual_weight AS ActualWeight,
		sx.limit_weight AS LimitWeight
		sx.is_inBound_location AS IsInboundLocation
		sx.wms_store_no AS WmsStoreNo
		sx.task_lock AS TaskLock
from 
		sx_store_location sx;
		


CREATE OR REPLACE VIEW SYSUSERVIEW AS
  select u.id as id,
       u.loginname as loginName,
       u.username as userName,
       ud.fullpath as deptFullPath,
       u.sex as sex,
       r.rolename as roleName,
			u.work_no as workNo
from SysUser u
left join UserDept ud on u.userdeptid = ud.id
left join UserRole r on u.roleid = r.id;

CREATE OR REPLACE VIEW ownerview AS
select `owner`.`id` AS `id`,
`owner`.`owner_no` AS `ownerNo`,
`owner`.`owner_name` AS `ownerName`,
`owner`.`zhuj_code` AS `zhujCode`,
`owner`.`owner_simplename` AS `ownerSimplename`,
`owner`.`beactive` AS `beactive`,
`owner`.`create_time` AS `createTime`,
`owner`.`update_time` AS `updateTime` 
from `owner`;


		
		
-- 盘点视图 --

CREATE OR REPLACE VIEW pdtaskdetailview AS
select 
	sx.id AS id,
	sub.container_no AS containerNo,
	sub.container_sub_no AS containerSubNo,
	g.goods_no AS goodsNo,
	g.goods_name AS goodsName,
	sub.commodity_num AS originalCount,
	(SELECT GROUP_CONCAT(gbc.bar_code) from goods_bar_code gbc where gbc.goods_id = g.id)
                     as goodsBarCode
from sx_store sx
INNER JOIN container_sub_info sub on sub.container_no = sx.CONTAINER_NO
INNER JOIN goods g on g.id = sub.commodity_id
INNER JOIN sx_store_location sxl on sx.STORE_LOCATION_ID = sxl.id
INNER JOIN sx_store_location_group sxg on sxl.store_location_group_id = sxg.ID
WHERE sxg.IS_LOCK = 0 
AND sxg.ASCENT_LOCK_STATE = 0 
AND sxl.task_lock = 0 
and sx.TASK_TYPE = 0
AND sx.STORE_STATE = 20 ;


CREATE OR REPLACE VIEW pdtaskview AS
SELECT  pd.id AS id, 
	pd.pd_no AS PdNo,
	pd.remark AS remark, 
	pd.pd_state AS pdState, 
	pd.pd_type AS pdType,
	count(pt.id)  AS totalBoxCount,
	count(IF(pt.task_state = 0, true, null)) AS createCount,
	count(IF(pt.task_state = 10, true, null)) AS xiafaCount,
	count(IF(pt.task_state = 40, true, null)) AS finishCount,
	pd.create_time AS createTime
FROM pd_task pd 
INNER JOIN pd_task_detail pt on pd.id = pt.pd_task_id
INNER JOIN container_sub_info sub on pt.container_sub_no = sub.container_sub_no
INNER JOIN sx_store sx on sub.container_no = sx.CONTAINER_NO
INNER JOIN sx_store_location sxl on sx.STORE_LOCATION_ID = sxl.id
INNER JOIN sx_store_location_group sxg on sxl.store_location_group_id = sxg.ID
WHERE sxg.IS_LOCK = 0 
AND sxg.ASCENT_LOCK_STATE = 0 
AND sxl.task_lock = 0 
and sx.TASK_TYPE = 0
AND sx.STORE_STATE = 20 
GROUP BY pt.id ;


-- 合箱 --
CREATE OR REPLACE VIEW assemblboxview AS
SELECT a.id AS id, 
	g.goods_name AS goodsName,
	g.goods_no AS goodsNo,
	count(IF(mx.task_state = 0, true, null)) AS unStartCount,
	count(IF(mx.task_state = 1, true, null)) AS startCount,
	count(IF(mx.task_state = 2, true, null)) AS completeCount,
	a.create_time AS createTime
FROM assembl_box_hz a
INNER JOIN goods g on a.goods_id = g.id
INNER JOIN assembl_box_mx mx on mx.assembl_box_hz_id = a.id
GROUP BY mx.id;


CREATE OR REPLACE VIEW lxinfoview AS
SELECT s.ID AS id, 
	c.container_no AS containerNo ,
	g.goods_no AS goodsNo ,
	g.goods_name AS goodsName,
	(SELECT GROUP_CONCAT(gbc.bar_code) from goods_bar_code gbc where gbc.goods_id = g.id) AS goodsBarCode,
	s.IN_STORE_TIME AS inStoreTime
FROM container_sub_info c
INNER JOIN sx_store s on  c.container_no = s.CONTAINER_NO
INNER JOIN goods g on c.commodity_id = g.id;
