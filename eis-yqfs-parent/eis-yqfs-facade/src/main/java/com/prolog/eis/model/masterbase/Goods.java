package com.prolog.eis.model.masterbase;

import java.util.Date;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

/**
 * 商品资料表
 * @author tg
 *
 */
@Table("Goods")
public class Goods {
	
	@Id
	@ApiModelProperty("id")
	@Column("id")
	@AutoKey(type = AutoKey.TYPE_IDENTITY)
	private int id;
	
	@Column("owner_id")
	@ApiModelProperty("业主id")
	private int ownerId;
	
	@Column("goods_no")
	@ApiModelProperty("商品编号")
	private String goodsNo;
	
	@Column("goods_name")
	@ApiModelProperty("商品名称")
	private String goodsName;
	
	@Column("specification")
	@ApiModelProperty("规格")
	private String specification;
	
	@Column("unit_catch_weight")
	@ApiModelProperty("净重（克：g）")
	private float unitCatchWeight;
	
	@Column("unit_gross_weight")
	@ApiModelProperty("毛重（克：g）")
	private float unitGrossWeight;
	
	@Column("length")
	@ApiModelProperty("长（毫米：mm）")
	private float length;
	
	@Column("width")
	@ApiModelProperty("宽（毫米：mm）")
	private float width;
	
	@Column("height")
	@ApiModelProperty("高（毫米：mm）")
	private float height;
	
	@Column("unit_volume")
	@ApiModelProperty("体积（立方毫米：mm3）")
	private float unitVolume;
	
	@Column("package_number")
	@ApiModelProperty("包装数量")
	private int packageNumber;
	
	@Column("create_time")
	@ApiModelProperty("创建时间")
	private Date createTime;
	
	@Column("update_time")
	@ApiModelProperty("修改时间")
	private Date updateTime;
	
	@Column("recom_number")
	@ApiModelProperty("推存数量")
	private Integer recomNumber;
	
	@Column("recom_type")
	@ApiModelProperty("推存货格（1、整箱、2、日字、3、田字）")
	private Integer recomType;
	
	@Column("dealer_id")
	@ApiModelProperty("经销商id")
	private Integer dealerId;
	
	@Column("supplier_id")
	@ApiModelProperty("供应商id")
	private Integer supplierId;
	
	@Column("goods_state")
	@ApiModelProperty("商品状态1：合格,2：不合格")
	private int goodsState;

	@Column("abc")
	@ApiModelProperty("商品abc属性")
	private String abc;
	
	public int getGoodsState() {
		return goodsState;
	}

	public void setGoodsState(int goodsState) {
		this.goodsState = goodsState;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
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

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public float getUnitCatchWeight() {
		return unitCatchWeight;
	}

	public void setUnitCatchWeight(float unitCatchWeight) {
		this.unitCatchWeight = unitCatchWeight;
	}

	public float getUnitGrossWeight() {
		return unitGrossWeight;
	}

	public void setUnitGrossWeight(float unitGrossWeight) {
		this.unitGrossWeight = unitGrossWeight;
	}

	public float getLength() {
		return length;
	}

	public void setLength(float length) {
		this.length = length;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getUnitVolume() {
		return unitVolume;
	}

	public void setUnitVolume(float unitVolume) {
		this.unitVolume = unitVolume;
	}

	public int getPackageNumber() {
		return packageNumber;
	}

	public void setPackageNumber(int packageNumber) {
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

	public Goods(int id, int ownerId, String goodsNo, String goodsName, String specification, float unitCatchWeight,
			float unitGrossWeight, float length, float width, float height, float unitVolume, int packageNumber,
			Date createTime, Date updateTime, int goodsState, String abc) {
		super();
		this.id = id;
		this.ownerId = ownerId;
		this.goodsNo = goodsNo;
		this.goodsName = goodsName;
		this.specification = specification;
		this.unitCatchWeight = unitCatchWeight;
		this.unitGrossWeight = unitGrossWeight;
		this.length = length;
		this.width = width;
		this.height = height;
		this.unitVolume = unitVolume;
		this.packageNumber = packageNumber;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.goodsState = goodsState;
		this.abc = abc;
	}

	public Goods() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getRecomNumber() {
		return recomNumber;
	}

	public void setRecomNumber(Integer recomNumber) {
		this.recomNumber = recomNumber;
	}

	public Integer getRecomType() {
		return recomType;
	}

	public void setRecomType(Integer recomType) {
		this.recomType = recomType;
	}

	public Integer getDealerId() {
		return dealerId;
	}

	public void setDealerId(Integer dealerId) {
		this.dealerId = dealerId;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public String getAbc() {
		return abc;
	}

	public void setAbc(String abc) {
		this.abc = abc;
	}
}
