package com.prolog.eis.dto.yqfs;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author LuoLi
 * @version 1.0
 * @date 2020/7/16 10:58
 */
public class PickOrderDetail {

    @ApiModelProperty("容器编号")
    private String containerNo;

    @ApiModelProperty("子容器编号")
    private String containerSubNo;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("实际数量")
    private int actualNum;

    @ApiModelProperty("绑定数量")
    private int bindingNum;

    @ApiModelProperty("状态")
    private int status;

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public String getContainerSubNo() {
        return containerSubNo;
    }

    public void setContainerSubNo(String containerSubNo) {
        this.containerSubNo = containerSubNo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getActualNum() {
        return actualNum;
    }

    public void setActualNum(int actualNum) {
        this.actualNum = actualNum;
    }

    public int getBindingNum() {
        return bindingNum;
    }

    public void setBindingNum(int bindingNum) {
        this.bindingNum = bindingNum;
    }
}
