package com.prolog.eis.dto.outbound;

import java.util.List;

/**
 * 订单箱出库复核打印dto
 */
public class OrderBoxDto {

    private String orderBoxNo;//订单箱号

    private List<OrderBoxMxDto> orderListDtos;//订单箱明细

    public OrderBoxDto() {
    }

    public OrderBoxDto(String orderBoxNo, List<OrderBoxMxDto> orderListDtos) {
        this.orderBoxNo = orderBoxNo;
        this.orderListDtos = orderListDtos;
    }

    public String getOrderBoxNo() {
        return orderBoxNo;
    }

    public void setOrderBoxNo(String orderBoxNo) {
        this.orderBoxNo = orderBoxNo;
    }

    public List<OrderBoxMxDto> getOrderListDtos() {
        return orderListDtos;
    }

    public void setOrderListDtos(List<OrderBoxMxDto> orderListDtos) {
        this.orderListDtos = orderListDtos;
    }
}
