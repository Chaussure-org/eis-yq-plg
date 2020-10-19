package com.prolog.eis.dto.yqfs;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author LuoLi
 * @version 1.0
 * @date 2020/7/18 15:19
 */
public class PickBillDetail {

    @ApiModelProperty("明细信息")
    List<PickBillDto> pickBillDtos;

    public List<PickBillDto> getPickBillDtos() {
        return pickBillDtos;
    }

    public void setPickBillDtos(List<PickBillDto> pickBillDtos) {
        this.pickBillDtos = pickBillDtos;
    }

}
