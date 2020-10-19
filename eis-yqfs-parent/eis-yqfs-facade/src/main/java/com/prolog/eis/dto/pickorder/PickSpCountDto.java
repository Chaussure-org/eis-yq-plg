package com.prolog.eis.dto.pickorder;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author wangkang
 * @Description
 * @CreateTime 2020/7/18 21:12
 */
public class PickSpCountDto {

    @ApiModelProperty("商品id")
    private int commodityId;

    @ApiModelProperty("箱库数量")
    private int xkCount;

    public int getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(int commodityId) {
        this.commodityId = commodityId;
    }

    public int getXkCount() {
        return xkCount;
    }

    public void setXkCount(int xkCount) {
        this.xkCount = xkCount;
    }
}
