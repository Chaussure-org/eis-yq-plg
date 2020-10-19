package org.eis.yqfs.master;

import com.prolog.eis.Application;
import com.prolog.eis.boxbank.rule.CarTaskCountRule;
import com.prolog.eis.boxbank.rule.StoreLocationDTO;
import com.prolog.eis.dao.masterbase.ContainerSubMapper;
import com.prolog.eis.dto.yqfs.ReplenishmentConfirmDto;
import com.prolog.eis.model.masterbase.ContainerSub;
import com.prolog.eis.model.repair.InboundRepairInfo;
import com.prolog.eis.model.store.SxStoreLocation;
import com.prolog.eis.service.inbound.IInboundService;
import com.prolog.eis.service.inbound.impl.InboundTaskServiceImpl;
import com.prolog.eis.service.order.IOrderHzService;
import com.prolog.eis.service.repair.InboundRepairInfoService;
import com.prolog.eis.wcs.dto.BCRDataDTO;
import com.prolog.framework.common.message.RestMessage;
import com.prolog.framework.utils.MapUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author wangkang
 * @Description 小车换层任务计算
 * @CreateTime 2020/8/8 13:00
 */

@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
public class CarTest {

    @Autowired
    private CarTaskCountRule carTaskCountRule;

    @Autowired
    private ContainerSubMapper containerSubMapper;
    @Autowired
    public InboundRepairInfoService inboundRepairInfoService;
    @Autowired
    private IInboundService iInboundService;
    @Autowired
    private IOrderHzService orderHzService;

    @Test
    public void testcar()throws Exception{
        StoreLocationDTO location = new StoreLocationDTO();
        location.setLayer(1);
        carTaskCountRule.execute(location ,null);
    }

    @Test
    public void testInbound() throws Exception{
      iInboundService.saveInboundInfo("88000074");
    }
    @Test
    public void testRepair(){
        ReplenishmentConfirmDto dto = new ReplenishmentConfirmDto();
        dto.setContainerNo("111111");
        dto.setContainerSubNo("111111");
        dto.setGoodsNo("dasasdadas");
        dto.setGoodsNum(1111);
        ContainerSub containerSub = new ContainerSub();
        containerSub.setContainerNo("1111");

        //保存补货记录
        InboundRepairInfo inboundRepairInfo = new InboundRepairInfo();
        inboundRepairInfo.setContainerNo(dto.getContainerNo());
        inboundRepairInfo.setContainerSubNo(dto.getContainerSubNo());
        inboundRepairInfo.setCommodityNum(dto.getGoodsNum());
        inboundRepairInfo.setCreateTime(new Date());
        inboundRepairInfo.setGoodsId(containerSub.getCommodityId());
        inboundRepairInfo.setInboundStatu(InboundRepairInfo.REPQAIR_STATUS);//补货
        inboundRepairInfoService.save(inboundRepairInfo);
    }
    @Test
    public void testCreatOrder() throws Exception {
        RestMessage<String> order = orderHzService.createOrder();
    }
}

