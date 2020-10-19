package com.prolog.eis.dto.pickorder;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;

/**
 * @Author wangkang
 * @Description
 * @CreateTime 2020/7/18 21:02
 */
public class PickLxBindDto {

    @ApiModelProperty("料箱号")
    private String liaoXiangNo;

    @ApiModelProperty("箱库库存id")
    private int xkKuCunId;

    @ApiModelProperty("在途库存id")
    private int ztKuCunId;

    @ApiModelProperty("订单明细id")
    private int orderMxId;

    @ApiModelProperty("是否完成")
    private int isFinish;

    @ApiModelProperty("计划数量")
    private int planNum;

    public String getLiaoXiangNo() {
        return liaoXiangNo;
    }

    public void setLiaoXiangNo(String liaoXiangNo) {
        this.liaoXiangNo = liaoXiangNo;
    }

    public int getXkKuCunId() {
        return xkKuCunId;
    }

    public void setXkKuCunId(int xkKuCunId) {
        this.xkKuCunId = xkKuCunId;
    }

    public int getZtKuCunId() {
        return ztKuCunId;
    }

    public void setZtKuCunId(int ztKuCunId) {
        this.ztKuCunId = ztKuCunId;
    }

    public int getOrderMxId() {
        return orderMxId;
    }

    public void setOrderMxId(int orderMxId) {
        this.orderMxId = orderMxId;
    }

    public int isFinish() {
        return isFinish;
    }

    public void setFinish(int finish) {
        isFinish = finish;
    }

    public int getPlanNum() {
        return planNum;
    }

    public void setPlanNum(int planNum) {
        this.planNum = planNum;
    }
}
