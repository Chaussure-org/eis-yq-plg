package com.prolog.eis.dto.yqfs;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author LuoLi
 * @version 1.0
 * @date 2020/7/18 15:09
 */
public class PickBillDto {

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("商品编号")
    private String goodsNo;

    @ApiModelProperty("绑定数量")
    private int pickNum;

    @ApiModelProperty("是否短拣")
    private int isNotPick;

    @ApiModelProperty("当前状态")
    private int status;

    @ApiModelProperty("拣选容器信息")
    private PickContainerDto pickContainerDto;


    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    public int getPickNum() {
        return pickNum;
    }

    public void setPickNum(int pickNum) {
        this.pickNum = pickNum;
    }

    public int getIsNotPick() {
        return isNotPick;
    }

    public void setIsNotPick(int isNotPick) {
        this.isNotPick = isNotPick;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public PickContainerDto getPickContainerDto() {
        return pickContainerDto;
    }

    public void setPickContainerDto(PickContainerDto pickContainerDto) {
        this.pickContainerDto = pickContainerDto;
    }
}
