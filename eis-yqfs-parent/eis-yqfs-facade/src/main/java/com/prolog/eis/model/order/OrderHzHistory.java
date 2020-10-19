package com.prolog.eis.model.order;

import java.util.Date;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

/**
 * 出库单汇总历史
 * 
 * @author tg
 *
 */
@Table("order_hz_history")
public class OrderHzHistory {

	@Column("id")
	@ApiModelProperty("id")
	private Integer id;

	@Column("picking_order_id")
	@ApiModelProperty("拣选单Id")
	private Integer pickingOrderId;

	@Column("outbound_code")
	@ApiModelProperty("出库单编号")
	private String outboundCode;

	@Column("pick_date_time")
	@ApiModelProperty("拣选开始时间")
	private Date pickDateTime;

	@Column("create_time")
	@ApiModelProperty("创建时间（YYYY-MM-DD HH:MM:SS）")
	private Date createTime;

	@Column("last_date_time")
	@ApiModelProperty("最后操作时间（YYYY-MM-DD HH:MM:SS）")
	private Date lastDateTime;

	@Column("expect_time")
	@ApiModelProperty("时效（YYYY-MM-DD HH:MM:SS）")
	private Date expectTime;

	@Column("priority")
	@ApiModelProperty("优先级")
	private Integer priority;

	@Column("order_box_no")
	@ApiModelProperty("订单箱编号")
	private String orderBoxNo;

	@Column("station_id")
	@ApiModelProperty("站台id")
	private Integer stationId;

	@Column("is_add_pool")
	@ApiModelProperty("是否加入订单池（0：否，1：是）")
	private Integer isAddPool;

	@Column("bill_no")
	@ApiModelProperty("清单编号")
	private String billNo;// 清单编号

	@Column("loan_no")
	@ApiModelProperty("借件单号")
	private String loanNo;// 借件单号

	@Column("loan_date_time")
	@ApiModelProperty("借件单日期")
	private Date loanDateTime;// 借件单日期

	@Column("dealer_id")
	@ApiModelProperty("经销商id")
	private int dealerId;// 经销商id

	@Column("goods_factory")
	@ApiModelProperty("发货工厂")
	private String goodsFactory;// 发货工厂

	@Column("transfer_library")
	@ApiModelProperty("中转库")
	private String transferLibrary;// 中转库

	@Column("deliver_goods_mode")
	@ApiModelProperty("发货方式")
	private String deliverGoodsMode;// 发货方式

	@Column("shipping_point")
	@ApiModelProperty("发运点")
	private String shippingPoint;// 发运点

	@Column("store_no")
	@ApiModelProperty("仓库号")
	private String storeNo;// 仓库号

	@Column("deliver_goods_type")
	@ApiModelProperty("发运品种")
	private String deliverGoodsType;// 发运品种

	@Column("dealer_address")
	@ApiModelProperty("客户地址")
	private String dealerAddress;// 客户地址

	@Column("weight")
	@ApiModelProperty("总重量")
	private double weight;// 总重量

	@Column("net_weight")
	@ApiModelProperty("净重")
	private double netWeight;// 净重

	@Column("money")
	@ApiModelProperty("金额")
	private double money;// 金额

	@Column("confirm_time")
	@ApiModelProperty("确认时间")
	private Date confirmTime;// 确认时间

	@Column("crtat_staff")
	@ApiModelProperty("创建人")
	private String crtatStaff;// 创建人

	@Column("order_create_time")
	@ApiModelProperty("建单时间")
	private Date orderCreateTime;// 建单时间

	@Column("sto_no")
	@ApiModelProperty("移库单订单号")
	private String stoNo;

	public String getStoNo() {
		return stoNo;
	}

	public void setStoNo(String stoNo) {
		this.stoNo = stoNo;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getLoanNo() {
		return loanNo;
	}

	public void setLoanNo(String loanNo) {
		this.loanNo = loanNo;
	}

	public Date getLoanDateTime() {
		return loanDateTime;
	}

	public void setLoanDateTime(Date loanDateTime) {
		this.loanDateTime = loanDateTime;
	}

	public int getDealerId() {
		return dealerId;
	}

	public void setDealerId(int dealerId) {
		this.dealerId = dealerId;
	}

	public String getGoodsFactory() {
		return goodsFactory;
	}

	public void setGoodsFactory(String goodsFactory) {
		this.goodsFactory = goodsFactory;
	}

	public String getTransferLibrary() {
		return transferLibrary;
	}

	public void setTransferLibrary(String transferLibrary) {
		this.transferLibrary = transferLibrary;
	}

	public String getDeliverGoodsMode() {
		return deliverGoodsMode;
	}

	public void setDeliverGoodsMode(String deliverGoodsMode) {
		this.deliverGoodsMode = deliverGoodsMode;
	}

	public String getShippingPoint() {
		return shippingPoint;
	}

	public void setShippingPoint(String shippingPoint) {
		this.shippingPoint = shippingPoint;
	}

	public String getStoreNo() {
		return storeNo;
	}

	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
	}

	public String getDeliverGoodsType() {
		return deliverGoodsType;
	}

	public void setDeliverGoodsType(String deliverGoodsType) {
		this.deliverGoodsType = deliverGoodsType;
	}

	public String getDealerAddress() {
		return dealerAddress;
	}

	public void setDealerAddress(String dealerAddress) {
		this.dealerAddress = dealerAddress;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getNetWeight() {
		return netWeight;
	}

	public void setNetWeight(double netWeight) {
		this.netWeight = netWeight;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public Date getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(Date confirmTime) {
		this.confirmTime = confirmTime;
	}

	public String getCrtatStaff() {
		return crtatStaff;
	}

	public void setCrtatStaff(String crtatStaff) {
		this.crtatStaff = crtatStaff;
	}

	public Date getOrderCreateTime() {
		return orderCreateTime;
	}

	public void setOrderCreateTime(Date orderCreateTime) {
		this.orderCreateTime = orderCreateTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPickingOrderId() {
		return pickingOrderId;
	}

	public void setPickingOrderId(Integer pickingOrderId) {
		this.pickingOrderId = pickingOrderId;
	}

	public String getOutboundCode() {
		return outboundCode;
	}

	public void setOutboundCode(String outboundCode) {
		this.outboundCode = outboundCode;
	}

	public Date getPickDateTime() {
		return pickDateTime;
	}

	public void setPickDateTime(Date pickDateTime) {
		this.pickDateTime = pickDateTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastDateTime() {
		return lastDateTime;
	}

	public void setLastDateTime(Date lastDateTime) {
		this.lastDateTime = lastDateTime;
	}

	public Date getExpectTime() {
		return expectTime;
	}

	public void setExpectTime(Date expectTime) {
		this.expectTime = expectTime;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getOrderBoxNo() {
		return orderBoxNo;
	}

	public void setOrderBoxNo(String orderBoxNo) {
		this.orderBoxNo = orderBoxNo;
	}

	public Integer getStationId() {
		return stationId;
	}

	public void setStationId(Integer stationId) {
		this.stationId = stationId;
	}

	public Integer getIsAddPool() {
		return isAddPool;
	}

	public void setIsAddPool(Integer isAddPool) {
		this.isAddPool = isAddPool;
	}

}
