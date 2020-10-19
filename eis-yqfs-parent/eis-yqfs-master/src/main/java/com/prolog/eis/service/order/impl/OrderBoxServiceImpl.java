package com.prolog.eis.service.order.impl;

import com.prolog.eis.configuration.EisProperties;
import com.prolog.eis.dao.masterbase.ContainerSubMapper;
import com.prolog.eis.dao.order.OrderBoxHzHistoryMapper;
import com.prolog.eis.dao.order.OrderBoxHzMapper;
import com.prolog.eis.dao.order.OrderBoxMxHistoryMapper;
import com.prolog.eis.dao.order.OrderBoxMxMapper;
import com.prolog.eis.dto.outbound.OrderBoxDto;
import com.prolog.eis.dto.outbound.OrderBoxMxDto;
import com.prolog.eis.dto.outbound.OutBoundOrderBoxDto;
import com.prolog.eis.model.masterbase.ContainerSub;
import com.prolog.eis.model.order.OrderBoxHz;
import com.prolog.eis.model.order.OrderBoxHzHistory;
import com.prolog.eis.model.order.OrderBoxMx;
import com.prolog.eis.model.order.OrderBoxMxHistory;
import com.prolog.eis.service.order.IOrderBoxService;
import com.prolog.eis.util.PrologApiJsonHelper;
import com.prolog.eis.util.PrologHttpUtils;
import com.prolog.framework.utils.MapUtils;
import com.prolog.framework.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class OrderBoxServiceImpl implements IOrderBoxService {
    private final Logger logger = LoggerFactory.getLogger(OrderBoxServiceImpl.class);
    @Autowired
    private OrderBoxHzMapper hzMapper;
    @Autowired
    private OrderBoxMxMapper mxMapper;
    @Autowired
    private OrderBoxHzHistoryMapper hisHzMapper;
    @Autowired
    private OrderBoxMxHistoryMapper hisMxMapper;
    @Autowired
    private ContainerSubMapper csubMapper;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private EisProperties properties;
    @Override
    public void add(OrderBoxHz orderBoxHz) throws Exception {
        if (orderBoxHz == null) {
            throw new Exception("orderBoxHz保存为空");
        }
        hzMapper.save(orderBoxHz);
    }

    @Override
    public void update(OrderBoxHz orderBoxHz) throws Exception {
        if (orderBoxHz == null) {
            throw new Exception("orderBoxHz保存为空");
        }
        hzMapper.update(orderBoxHz);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(int id) {
        if (id == 0) {
            return;
        }
        String orderBoxNo = hzMapper.findById(id, OrderBoxHz.class).getOrderBoxNo();
        mxMapper.deleteByMap(MapUtils.put("orderBoxNo", orderBoxNo).getMap(), OrderBoxMx.class);
        hzMapper.deleteById(id, OrderBoxHz.class);
    }

    @Override
    public OrderBoxHz getByNo(String orderBoxNo) {
        List<OrderBoxHz> orderBoxNoList = hzMapper.findByMap(MapUtils.put("orderBoxNo", orderBoxNo).getMap(), OrderBoxHz.class);
        if (orderBoxNoList.size() == 0) {
            return null;
        }
        return orderBoxNoList.get(0);
    }

    @Override
    public OrderBoxHz getById(int id) {
        if (id == 0) {
            return null;
        }
        return hzMapper.findById(id, OrderBoxHz.class);
    }

    @Override
    public List<OrderBoxHz> getByOrderId(int orderId) {
        if (orderId == 0) {
            return null;
        }
        return hzMapper.findByMap(MapUtils.put("orderHzId", orderId).getMap(), OrderBoxHz.class);
    }

    @Override
    public List<OrderBoxHz> getByMap(Map<String, Object> map) {
        if (map.size() == 0) {
            return null;
        }
        return hzMapper.findByMap(map, OrderBoxHz.class);
    }

    @Override
    public void addMx(OrderBoxMx mx) throws Exception {
        if (mx == null) {
            throw new Exception("OrderBoxMx为空");
        }
        mxMapper.save(mx);
    }

    @Override
    public void addMxs(List<OrderBoxMx> mxList) throws Exception {
        if (mxList.size() == 0) {
            return;
        }
        mxMapper.saveBatch(mxList);
    }

    @Override
    public void updateMx(OrderBoxMx mx) throws Exception {
        if (mx == null) {
            throw new Exception("OrderBoxMx为空");
        }
        mxMapper.update(mx);
    }

    @Override
    public void deleteMx(int id) throws Exception {
        if (id == 0) {
            return;
        }
        mxMapper.deleteById(id, OrderBoxMx.class);
    }

    @Override
    public OrderBoxMx getMxById(int id) {
        if (id == 0) {
            return null;
        }
        return mxMapper.findById(id, OrderBoxMx.class);
    }

    @Override
    public List<OrderBoxMx> getMxsByHzNo(String orderBoxNo) {
        if (StringUtils.isBlank(orderBoxNo)) {
            return null;
        }
        return mxMapper.findByMap(MapUtils.put("orderBoxNo", orderBoxNo).getMap(), OrderBoxMx.class);
    }

    @Override
    public List<OrderBoxMx> getMxsByMap(Map<String, Object> map) {
        if (map.size()==0){
            return null;
        }
        return mxMapper.findByMap(map, OrderBoxMx.class);
    }

    @Override
    public void toHistory(OrderBoxHz orderBoxHz) {
        if (orderBoxHz == null) {
            return;
        }
        List<OrderBoxHzHistory> list = hisHzMapper.findByMap(MapUtils.put("orderBoxNo", orderBoxHz.getOrderBoxNo()).getMap(), OrderBoxHzHistory.class);
        if (list.size()>0){
            hisHzMapper.deleteByMap(MapUtils.put("orderBoxNo", orderBoxHz.getOrderBoxNo()).getMap(), OrderBoxHzHistory.class);
        }
        OrderBoxHzHistory orderBoxHzHistory = new OrderBoxHzHistory();
        orderBoxHzHistory.setId(orderBoxHz.getId());
        orderBoxHzHistory.setOrderBoxNo(orderBoxHz.getOrderBoxNo());
        orderBoxHzHistory.setOrderHzId(orderBoxHz.getOrderHzId());
        hisHzMapper.save(orderBoxHzHistory);
        hzMapper.deleteById(orderBoxHz.getId(), OrderBoxHz.class);

        List<OrderBoxMx> mxs = this.getMxsByHzNo(orderBoxHz.getOrderBoxNo());
        List<OrderBoxMxHistory> List = hisMxMapper.findByMap(MapUtils.put("orderBoxNo", mxs.get(0).getOrderBoxNo()).getMap(), OrderBoxMxHistory.class);
        if (List.size()>0){
            hisMxMapper.deleteByMap(MapUtils.put("orderBoxNo", mxs.get(0).getOrderBoxNo()).getMap(), OrderBoxMxHistory.class);
        }
        if(mxs.size()>0){
            mxs.forEach(x->this.toHistory(x));
        }
    }

    @Override
    public void toHistory(OrderBoxMx mx) {
        if (mx == null) {
            return;
        }
//        List<OrderBoxMxHistory> List = hisMxMapper.findByMap(MapUtils.put("orderBoxNo", mx.getOrderBoxNo()).getMap(), OrderBoxMxHistory.class);
//        if (List.size()>0){
//            hisMxMapper.deleteByMap(MapUtils.put("orderBoxNo", mx.getOrderBoxNo()).getMap(), OrderBoxMxHistory.class);
//        }
        OrderBoxMxHistory orderBoxMxHistory = new OrderBoxMxHistory();
        orderBoxMxHistory.setCommodityId(mx.getCommodityId());
        orderBoxMxHistory.setCommodityNum(mx.getCommodityNum());
        orderBoxMxHistory.setContainerNo(mx.getContainerNo());
        orderBoxMxHistory.setContainerSubNo(mx.getContainerSubNo());
        orderBoxMxHistory.setId(mx.getId());
        orderBoxMxHistory.setOrderBoxNo(mx.getOrderBoxNo());
        orderBoxMxHistory.setContainerNo(mx.getContainerNo());
        hisMxMapper.save(orderBoxMxHistory);
        mxMapper.deleteById(mx.getId(), OrderBoxMx.class);
    }

    @Override
    public void toHistoryByOrderId(int orderId) throws Exception {
        List<OrderBoxHz> hzs = this.getByOrderId(orderId);
        if (hzs.size() == 0) {
            return;
        }

        hzs.forEach(x -> {
            this.toHistory(x);
        });
    }


    /**
     * 更新orderbox
     *
     * @param orderBoxNo
     * @param orderId`
     * @param orderMxId
     * @param containerSubNo
     * @param count
     * @throws Exception
     */
    @Override
    public void update(String orderBoxNo, int orderId, int orderMxId, String containerSubNo, int count) throws Exception {
        if (count == 0)
            return;

        OrderBoxHz hz = this.getByNo(orderBoxNo);
        if (hz == null) {
            hz = new OrderBoxHz();
            hz.setOrderBoxNo(orderBoxNo);
            hz.setOrderHzId(orderId);
            this.add(hz);
        }

        ContainerSub sub = csubMapper.findById(containerSubNo, ContainerSub.class);
        if (sub == null)
            throw new RuntimeException("找不到货格(" + containerSubNo + ")");
        OrderBoxMx mx = new OrderBoxMx();
        mx.setOrderBoxNo(orderBoxNo);
        mx.setContainerNo(sub.getContainerNo());
        mx.setContainerSubNo(containerSubNo);
        mx.setCommodityId(sub.getCommodityId());
        mx.setCommodityNum(count);
        mx.setOrderMxId(orderMxId);
        this.addMx(mx);

    }

    /**
     * 得到订单箱出库复核打印数据
     * @param orderBoxNo
     * @return
     * @throws Exception
     */
    @Override
    public void getOutBoundOrderBox(String orderBoxNo) throws Exception {
        OrderBoxDto orderBoxDto = new OrderBoxDto();
        orderBoxDto.setOrderBoxNo(orderBoxNo);

        List<OrderBoxMxDto> orderBoxInfo = hisMxMapper.findOrderBoxInfo(orderBoxNo);
        orderBoxDto.setOrderListDtos(orderBoxInfo);
        String url = "/PrintOrderBoxMsg";
        String orderBoxMxStr = PrologApiJsonHelper.toJson(orderBoxDto);
        this.sendData(orderBoxNo,orderBoxMxStr,url);
    }

    /**
     * 订单箱标签
     * @param orderBoxNo
     * @throws Exception
     */
    @Override
    public void getOrderBoxLabel(String orderBoxNo) throws Exception {
        String url = "/PrintOrderDtoMsg";
        List<OutBoundOrderBoxDto> outBoundOrderBoxInfo = hisHzMapper.findOutBoundOrderBoxInfo(orderBoxNo);
        OutBoundOrderBoxDto outBoundOrderBoxDto = outBoundOrderBoxInfo.get(0);
        if (outBoundOrderBoxInfo.size() > 0){
            String outBoundOrderStr= PrologApiJsonHelper.toJson(outBoundOrderBoxDto);
            this.sendData(orderBoxNo,outBoundOrderStr,url);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendOrderBoxIndToClient(String orderBoxNo) throws Exception {
        //发送订单箱标签
        try {
            this.getOrderBoxLabel(orderBoxNo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //发送订单箱明细
        this.getOutBoundOrderBox(orderBoxNo);


    }

    @Override
    public void sendData(String orderBoxNo,String dataStr, String url) throws Exception {

        try {
            String ipAddress = "192.166.11.200";
            String postUrl = String.format("http://%s:%s%s", ipAddress, "8901", url);
            logger.info("EIS -> EIS Client {} : params:{}", postUrl, dataStr);
            String restJson = restTemplate.postForObject(postUrl, PrologHttpUtils.getRequestEntity(dataStr), String.class);
            logger.info("EIS <- EIS Client {} : result:{}", postUrl, restJson);
            PrologApiJsonHelper helper = PrologApiJsonHelper.createHelper(restJson);
            Boolean ret = helper.getBoolean("Success");
            if (ret) {
                logger.info(orderBoxNo + "订单箱详情发送成功" +dataStr);
            } else {
                logger.error(orderBoxNo + "订单箱详情发送失败"+dataStr);
            }
        } catch (Exception e){
            logger.error(orderBoxNo + "订单箱详情发送失败" + e.toString());
        }
    }
}
