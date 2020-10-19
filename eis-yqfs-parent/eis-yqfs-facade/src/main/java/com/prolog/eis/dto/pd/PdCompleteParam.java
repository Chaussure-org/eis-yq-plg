package com.prolog.eis.dto.pd;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author LuoLi
 * @version 1.0
 * @date 2020/6/15 16:17
 */
public class PdCompleteParam {

    @ApiModelProperty("明细箱号")
    private String containerSubNo;

    @ApiModelProperty("库存实际数量")
    private int storeCount;

    public String getContainerSubNo() {
        return containerSubNo;
    }

    public void setContainerSubNo(String containerSubNo) {
        this.containerSubNo = containerSubNo;
    }

    public int getStoreCount() {
        return storeCount;
    }

    public void setStoreCount(int storeCount) {
        this.storeCount = storeCount;
    }
}
