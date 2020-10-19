package com.prolog.eis.dto.yqfs;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author LuoLi
 * @version 1.0
 * @date 2020/7/20 9:42
 */
public class PickContainerDto {

    @ApiModelProperty("拣选货格号")
    private String containerSubNo;

    @ApiModelProperty("拣选订单箱号")
    private String orderBoxNo;

    @ApiModelProperty("货格拣选数量")
    private int bindingNum;

    @ApiModelProperty("状态")
    private int status;

    public String getContainerSubNo() {
        return containerSubNo;
    }

    public void setContainerSubNo(String containerSubNo) {
        this.containerSubNo = containerSubNo;
    }

    public String getOrderBoxNo() {
        return orderBoxNo;
    }

    public void setOrderBoxNo(String orderBoxNo) {
        this.orderBoxNo = orderBoxNo;
    }

    public int getBindingNum() {
        return bindingNum;
    }

    public void setBindingNum(int bindingNum) {
        this.bindingNum = bindingNum;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
