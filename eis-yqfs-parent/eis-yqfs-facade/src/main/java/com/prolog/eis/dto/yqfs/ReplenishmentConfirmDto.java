package com.prolog.eis.dto.yqfs;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.prolog.framework.core.annotation.Many;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author panteng
 * @description:
 * @date 2020/4/23 14:27
 */
public class ReplenishmentConfirmDto {

    @NotEmpty(message = "商品编号不能为空")
    @ApiModelProperty("商品编号")
    private String goodsNo;

    @ApiModelProperty("商品数量")
    private Integer goodsNum;

    @ApiModelProperty("子容器编号")
    @NotEmpty(message = "子容器编号不能为空")
    private String containerSubNo;

    @ApiModelProperty("有效日期")
    @JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
    private Date expiryDate;

    @ApiModelProperty("操作人")
    @NotEmpty(message = "操作人不能为空")
    private String operator;

    @ApiModelProperty("容器编号")
    @NotEmpty(message = "容器编号不能为空")
    private String containerNo;

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public Integer getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(Integer goodsNum) {
		this.goodsNum = goodsNum;
	}

	public String getContainerSubNo() {
		return containerSubNo;
	}

	public void setContainerSubNo(String containerSubNo) {
		this.containerSubNo = containerSubNo;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	@Override
	public String toString() {
		return "ReplenishmentConfirmDto{" +
				"goodsNo='" + goodsNo + '\'' +
				", goodsNum=" + goodsNum +
				", containerSubNo='" + containerSubNo + '\'' +
				", expiryDate=" + expiryDate +
				", operator='" + operator + '\'' +
				", containerNo='" + containerNo + '\'' +
				'}';
	}
}
