package com.prolog.eis.dao.masterbase;

import com.prolog.eis.dto.container.ContainerSubIRDto;
import com.prolog.eis.dto.hxdispatch.StationLxPositionHxDto;
import com.prolog.eis.dto.pd.PdDwContainerSubDto;
import com.prolog.eis.dto.yqfs.ContainerSubDto;
import com.prolog.eis.dto.yqfs.ContainerSubInfoDto;
import com.prolog.eis.model.masterbase.ContainerSub;
import com.prolog.eis.pickstation.dto.HxContainerInfoDto;
import com.prolog.framework.dao.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author 孙四月
 */
public interface ContainerSubMapper extends BaseMapper<ContainerSub> {

    /**
     * 查询子容器信息
     *
     * @param containerNo
     * @return
     */
    @Select("SELECT\n" +
            "\tcs.container_sub_no AS containerSubNo,\n" +
            "\tcs.container_no AS containerNo,\n" +
            "\tcs.commodity_id AS commodityId,\n" +
            "\tcs.commodity_num AS commodityNum,\n" +
            "\tgs.goods_name AS goodsName,\n" +
            "\tgs.goods_no AS goodsNo,\n" +
            "\tci.layout_type AS layoutType,\n" +
            "\t( SELECT GROUP_CONCAT( gbc.bar_code ) FROM goods_bar_code gbc WHERE gbc.goods_id = cs.commodity_id ) AS goodsBarCode,\n" +
            "IF\n" +
            "\t((\n" +
            "\t\tSELECT \n" +
            "\t\trpm.container_no\n" +
            "\t\tFROM\n" +
            "\t\t\trepair_plan rp\n" +
            "\t\t\tJOIN repair_plan_mx rpm ON rp.id = rpm.repair_plan_id \n" +
            "\t\tWHERE\n" +
            "\t\t\trp.goods_id = gs.id \n" +
            "\t\t\tAND rpm.container_sub_no = cs.container_sub_no limit 1\n" +
            "\t\t\t) IS NULL ,\n" +
            "\t\t0,\n" +
            "\t\t1 \n" +
            "\t) as needState\n" +
            "FROM\n" +
            "\tcontainer ci\n" +
            "\tJOIN container_sub cs ON cs.container_no = ci.container_no\n" +
            "\tLEFT JOIN goods gs ON gs.id = cs.commodity_id \n" +
            "WHERE\n" +
            "\tcs.container_no = #{containerNo}")
    List<ContainerSubDto> findSubContainerDetailByContainerNo(@Param("containerNo") String containerNo);

    /**
     * 查询50条字段为空数据
     */
    @Select("select c.container_sub_no as containerSubNo,c.container_no as containerNo,c.commodity_id as commodityId," +
            " c.commodity_num as commodityNum, c.expiry_date as expiryDate " +
            "from container_sub c where c.commodity_id is null and c.commodity_num is null and c.expiry_date is null limit 0 ,50")
//    @Select("select * from container_sub s where s.commodity_id is null and s.commodity_num is null and s.expiry_date is null limit 0 ,50")
    List<ContainerSub> findContainerSubInfo();

    /**
     * 查有效容器的商品详情
     */
    @Select("select cs.container_sub_no as containerSubNo,cs.container_no as containerNo,cs.commodity_id as commodityId,cs.commodity_num as commodityNum," +
            "cs.expiry_date as expiryDate," +
            "ss.STORE_LOCATION_ID as storeLocationId from container_sub cs inner join sx_store ss on " +
            "cs.container_no = ss.CONTAINER_NO")
    List<ContainerSubInfoDto> findValidContainerSubInfo();


    @Select("SELECT\n" +
            "\tsub.container_sub_no AS containerSubNo,\n" +
            "\to.owner_name AS ownerName,\n" +
            "\tgs.goods_name AS goodsName,\n" +
            "\tsub.commodity_num AS goodsNum,\n" +
            "\tgs.id AS goodsId,\n" +
            "\tsub.expiry_date AS expiryDate\n" +
            "\t\n" +
            "FROM\n" +
            "\tcontainer_sub AS sub\n" +
            "\tLEFT JOIN goods gs ON sub.commodity_id = gs.id\n" +
            "\tLEFT JOIN `owner` o ON gs.owner_id = o.id \n" +
            "WHERE\n" +
            "\tsub.container_sub_no = #{containerSubNo};")
    PdDwContainerSubDto findPdDwContainerSubDto(@Param("containerSubNo") String containerSubNo);

    @Select("select container_sub_no as containerSubNo from container_sub where commodity_id = #{commodityId} and container_no = #{containerNo}")
    List<String> findContainerSubNo(@Param("commodityId") Integer commodityId, @Param("containerNo") String containerNo);


    @Update("update container_sub  c set c.gmt_create_time = null ,c.commodity_id = null ," +
            " c.commodity_num = null ,c.expiry_date = null where c.container_sub_no IS NOT NULL")
    void updateContainerSubIsNull();

    @Update("update container_sub cs set cs.commodity_num = #{commodityNum} where cs.container_sub_no = #{containerSubNo}")
    void updateContainerSubCommodityNum(@Param("commodityNum") int commodityNum, @Param("containerSubNo") String containerSubNo);
    @Select("select cs.container_sub_no as containerSubNo,c.layout_type as layoutType ,slp.position_no as positionNo,slp.container_direction as containerDirection" +
            " from container_sub cs LEFT JOIN container c on cs.container_no = c.container_no left join station_lx_position slp on slp.container_no = c.container_no" +
            "  where slp.container_no = #{containerNo} and cs.commodity_id = #{commodityId}")
    public List<StationLxPositionHxDto> getHxContainerInfo(@Param("commodityId") int commodityId, @Param("containerNo") String containerNo);



    @Select("select cs.container_sub_no as containerSubNo,cs.commodity_num as commodityNum,g.goods_name AS goodsName," +
            "gbc.bar_code as goodsBarCode from container_sub cs LEFT JOIN goods_bar_code gbc on gbc.goods_id = cs.commodity_id" +
            " LEFT JOIN goods g on g.id = gbc.goods_id WHERE cs.container_no = #{containerNo}")
    List<HxContainerInfoDto> findHxContainerSub(@Param("containerNo") String containerNo);


    @Select("SELECT\n" +
            "             cs.container_no AS containerNo,\n" +
            "             cs.container_sub_no AS containerSubNo,\n" +
            "             g.id as goodsId,\n" +
            "             cs.commodity_num AS commodityNum \n" +
            "            FROM\n" +
            "             container_sub cs\n" +
            "             left JOIN goods g ON g.id = cs.commodity_id \n" +
            "            WHERE\n" +
            "             cs.container_no = #{containerNo}")
    List<ContainerSubIRDto> getContainerSubInfo(@Param("containerNo") String containerNo);
}