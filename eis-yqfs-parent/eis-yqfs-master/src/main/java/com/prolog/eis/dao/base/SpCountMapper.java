package com.prolog.eis.dao.base;

import com.prolog.eis.dto.enginee.KuCunTotalDto;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author wangkang
 * @Description spcount
 * @CreateTime 2020/8/26 16:43
 */
public interface SpCountMapper {

    @Select("select mx.goods_id as spId, (sum(mx.plan_num) - sum(mx.actual_num)) as num \n" +
            "from order_mx mx \n" +
            "INNER JOIN order_hz hz on hz.id = mx.order_hz_id\n" +
            "INNER JOIN pick_order po on po.id = hz.picking_order_id GROUP BY mx.goods_id")
    List<KuCunTotalDto> findAllUsedKuCun();

    @Select("select csi.commodity_id as spId ,sum(csi.commodity_num) as num from container_sub csi\n" +
            "INNER JOIN sx_store sx on csi.container_no = sx.CONTAINER_NO\n" +
            "INNER JOIN sx_store_location sl on sl.id = sx.STORE_LOCATION_ID\n" +
            "INNER JOIN sx_store_location_group slg on slg.id = sl.store_location_group_id\n" +
            "where sx.STORE_STATE = 20 and slg.IS_LOCK = 0 and slg.ASCENT_LOCK_STATE = 0  and csi.commodity_id is not null GROUP BY csi.commodity_id")
    List<KuCunTotalDto> findAllXkKuCun();

    @Select("select csi.commodity_id as spId ,sum(csi.commodity_num) as num from container_sub csi\n" +
            "INNER JOIN zt_store zt on zt.CONTAINER_NO = csi.container_no\n" +
            "where zt.task_type = 20 GROUP BY csi.commodity_id")
    List<KuCunTotalDto> findAllztKuCun();
}
