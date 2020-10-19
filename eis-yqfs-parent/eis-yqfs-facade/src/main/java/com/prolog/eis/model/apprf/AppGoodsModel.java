package com.prolog.eis.model.apprf;

import com.prolog.eis.model.base.BaseModel;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @title 商品表
 * @author jinxf
  * @time 2020/4/23 10:58
  */
	@Table(value = "GOODS")
public class AppGoodsModel extends BaseModel {
	@Id
	@ApiModelProperty("Id标识列")
	private Integer id;

	@Column("OWNER_ID")
	@ApiModelProperty("业主id")
	private Integer ownerId;

	@Column("GOODS_NO")
	@ApiModelProperty("商品编号")
	private String goodsNo;

	@Column("GOODS_NAME")
	@ApiModelProperty("商品名称")
	private String goodsName;

	@Column("SPECIFICATION")
	@ApiModelProperty("规格")
	private String specification;

	@Column("UNIT_CATCH_WEIGHT")
	@ApiModelProperty("净重(克:g)")
	private BigDecimal unitCatchWeight;

	@Column("UNIT_GROSS_WEIGHT")
	@ApiModelProperty("毛重（克：g）")
	private BigDecimal unitGrossWeight;

	@Column("LENGTH")
	@ApiModelProperty("长（毫米:mm）")
	private BigDecimal length;

	@Column("WIDTH")
	@ApiModelProperty("宽（毫米:mm）")
	private BigDecimal width;

	@Column("HEIGHT")
	@ApiModelProperty("高（毫米:mm）")
	private BigDecimal height;

	@Column("UNIT_VOLUME")
	@ApiModelProperty("体积（立方毫米：mm3）")
	private BigDecimal unitVolume;

	@Column("PACKAGE_NUMBER")
	@ApiModelProperty("包装数量")
	private Integer packageNumber;

	@Column("CREATE_TIME")
	@ApiModelProperty("创建时间")
	private Date createTime;

	@Column("UPDATE_TIME")
	@ApiModelProperty("修改时间")
	private Date updateTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public BigDecimal getUnitCatchWeight() {
		return unitCatchWeight;
	}

	public void setUnitCatchWeight(BigDecimal unitCatchWeight) {
		this.unitCatchWeight = unitCatchWeight;
	}

	public BigDecimal getUnitGrossWeight() {
		return unitGrossWeight;
	}

	public void setUnitGrossWeight(BigDecimal unitGrossWeight) {
		this.unitGrossWeight = unitGrossWeight;
	}

	public BigDecimal getLength() {
		return length;
	}

	public void setLength(BigDecimal length) {
		this.length = length;
	}

	public BigDecimal getWidth() {
		return width;
	}

	public void setWidth(BigDecimal width) {
		this.width = width;
	}

	public BigDecimal getHeight() {
		return height;
	}

	public void setHeight(BigDecimal height) {
		this.height = height;
	}

	public BigDecimal getUnitVolume() {
		return unitVolume;
	}

	public void setUnitVolume(BigDecimal unitVolume) {
		this.unitVolume = unitVolume;
	}

	public Integer getPackageNumber() {
		return packageNumber;
	}

	public void setPackageNumber(Integer packageNumber) {
		this.packageNumber = packageNumber;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
}
