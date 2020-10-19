package com.prolog.eis.model.masterbase;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 商品条码资料表
 * @author tg
 *
 */
@Table("goods_bar_code")
public class GoodsBarCode {

	@Id
	@ApiModelProperty("id")
	@Column("id")
	private int id;
	
	@Column("goods_id")
	@ApiModelProperty("商品id(外键)")
	private int goodsId;
	
	@Column("bar_code")
	@ApiModelProperty("商品条码")
	private String barCode;
	
	@Column("isdefault")
	@ApiModelProperty("是否默认条码（0：否，1：是）")
	private int isdefault;
	
	@Column("create_time")
	@ApiModelProperty("创建时间")
	private Date createTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public int getIsdefault() {
		return isdefault;
	}

	public void setIsdefault(int isdefault) {
		this.isdefault = isdefault;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public GoodsBarCode(int id, int goodsId, String barCode, int isdefault, Date createTime) {
		super();
		this.id = id;
		this.goodsId = goodsId;
		this.barCode = barCode;
		this.isdefault = isdefault;
		this.createTime = createTime;
	}

	public GoodsBarCode() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	
	
}
