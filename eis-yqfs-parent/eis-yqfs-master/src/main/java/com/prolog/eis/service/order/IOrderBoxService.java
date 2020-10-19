package com.prolog.eis.service.order;

import com.prolog.eis.dto.outbound.OrderBoxDto;
import com.prolog.eis.dto.outbound.OutBoundOrderBoxDto;
import com.prolog.eis.model.order.ContainerSubBindingMx;
import com.prolog.eis.model.order.OrderBoxHz;
import com.prolog.eis.model.order.OrderBoxMx;

import java.util.List;
import java.util.Map;

/**
 * @Author wangkang
 * @Description 订单箱汇总服务接口
 * @CreateTime 2020/7/4 12:02
 */
public interface IOrderBoxService {
    void add(OrderBoxHz orderBoxHz) throws Exception;
    void update(OrderBoxHz orderBoxHz) throws Exception;
    void delete(int id);
    OrderBoxHz getByNo(String orderBoxNo);
    OrderBoxHz getById(int id);
    List<OrderBoxHz> getByOrderId(int orderId);
    List<OrderBoxHz> getByMap(Map<String,Object> map);

    void addMx(OrderBoxMx mx) throws Exception;
    void addMxs(List<OrderBoxMx> mxList) throws Exception;
    void updateMx(OrderBoxMx mx) throws Exception;
    void deleteMx(int id) throws Exception;
    OrderBoxMx getMxById(int id);
    List<OrderBoxMx> getMxsByHzNo(String orderBoxNo);
    List<OrderBoxMx> getMxsByMap(Map<String,Object> map);

    void toHistory(OrderBoxHz orderBoxHz);
    void toHistory(OrderBoxMx mx);
    void toHistoryByOrderId(int orderId) throws Exception;


    /**
     * 更新orderbox
     * @param orderBoxNo
     * @param orderId
     * @param orderMxId
     * @param containerSubNo
     * @param count
     * @throws Exception
     */
    void update(String orderBoxNo,int orderId,int orderMxId,String containerSubNo,int count) throws Exception;


    /**
     * 得到订单箱出库复核打印数据
     * @param orderBoxNo 订单箱编号
     * @return
     * @throws Exception
     */
    void getOutBoundOrderBox(String orderBoxNo) throws Exception;

    /**
     * 订单箱出库复核标签数据
     */
    void getOrderBoxLabel(String orderBoxNo) throws Exception;


    void sendOrderBoxIndToClient(String orderBoxNo) throws Exception;


    void sendData(String orderBoxNo,String dataStr,String url) throws Exception;
}
