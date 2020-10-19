package com.prolog.eis.model.masterbase;

import java.util.Date;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

/**
 * 经销商资料表 
 * @author tg
 *
 */
@Table("dealer")
public class Dealer {
	
	@Id
	@ApiModelProperty("id")
	@Column("id")
	@AutoKey(type = AutoKey.TYPE_IDENTITY)
	private int id;
	
	@Column("dealer_no")
	@ApiModelProperty("经销商编号")
	private String dealerNo;
	
	@Column("dealer_name")
	@ApiModelProperty("经销商名称")
	private String dealerName;
	
	@Column("brand")
	@ApiModelProperty("品牌")
	private String brand;
	
	@Column("normal")
	@ApiModelProperty("正常")
	private String normal;
	
	@Column("urgent")
	@ApiModelProperty("紧急")
	private String urgent;
	
	@Column("last_order_time")
	@ApiModelProperty("截单时间")
	private String lastOrderTime;

	@Column("route")
	@ApiModelProperty("截单时间")
	private String route;
	
	@Column("dealer_type")
	@ApiModelProperty("类型")
	private int dealerType;
	
	@Column("delivery_date")
	@ApiModelProperty("送货日期")
	private String deliveryDate;
	
	@Column("create_time")
	@ApiModelProperty("创建时间（YYYY-MM-DD HH:MM:SS）")
	private Date createTime;
	
	@Column("update_time")
	@ApiModelProperty("修改时间（YYYY-MM-DD HH:MM:SS）")
	private Date updateTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDealerNo() {
		return dealerNo;
	}

	public void setDealerNo(String dealerNo) {
		this.dealerNo = dealerNo;
	}

	public String getDealerName() {
		return dealerName;
	}

	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getNormal() {
		return normal;
	}

	public void setNormal(String normal) {
		this.normal = normal;
	}

	public String getUrgent() {
		return urgent;
	}

	public void setUrgent(String urgent) {
		this.urgent = urgent;
	}

	public String getLastOrderTime() {
		return lastOrderTime;
	}

	public void setLastOrderTime(String lastOrderTime) {
		this.lastOrderTime = lastOrderTime;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public int getDealerType() {
		return dealerType;
	}

	public void setDealerType(int dealerType) {
		this.dealerType = dealerType;
	}

	public String getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
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

	public Dealer(int id, String dealerNo, String dealerName, String brand, String normal, String urgent,
			String lastOrderTime, String route, int dealerType, String deliveryDate, Date createTime, Date updateTime) {
		super();
		this.id = id;
		this.dealerNo = dealerNo;
		this.dealerName = dealerName;
		this.brand = brand;
		this.normal = normal;
		this.urgent = urgent;
		this.lastOrderTime = lastOrderTime;
		this.route = route;
		this.dealerType = dealerType;
		this.deliveryDate = deliveryDate;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	public Dealer() {
		super();
		// TODO Auto-generated constructor stub
	}
	


}
