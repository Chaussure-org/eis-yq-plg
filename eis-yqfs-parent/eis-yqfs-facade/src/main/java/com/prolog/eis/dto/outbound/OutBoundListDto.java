package com.prolog.eis.dto.outbound;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

/**
 * 发货清单
 * @author tg
 *
 */
public class OutBoundListDto {
	
	@ApiModelProperty("清单号")
	private int billNo;
	
	@ApiModelProperty("订单号")
	private String outboundCode;
	
	@ApiModelProperty("创建人")
	private String crtatStaff;
	
	@ApiModelProperty("建单时间")
	private Date dealTime;
	
	@ApiModelProperty("借件单号")
	private String loanNo;
	
	@ApiModelProperty("借件单日期")
	private Date loanDateTime;
	
	@ApiModelProperty("发货工厂")
	private String goodsFactory;
	
	@ApiModelProperty("客户号")
	private String dealerNo;
	
	@ApiModelProperty("客户名称")
	private String dealerName;
	
	@ApiModelProperty("所属中转库")
	private String transferLibrary;
	
	@ApiModelProperty("发货方式")
	private String deliverGoodsMode;
	
	@ApiModelProperty("发运点")
	private String shippingPoint;
	
	@ApiModelProperty("仓库号")
	private String storeNo;
	
	@ApiModelProperty("发运品种")
	private String deliverGoodsType;
	
	@ApiModelProperty("客户地址")
	private String dealerAddress;
	
	@ApiModelProperty("总重量")
	private double weight;
	
	@ApiModelProperty("净重")
	private double netWeight;
	
	@ApiModelProperty("总金额")
	private double money;
	
	@ApiModelProperty("确认时间")
	private Date confirmTime;
	
	@ApiModelProperty("零件号")
	private String goodsNo;
	
	@ApiModelProperty("零件名称")
	private String goodsName;
	
	@ApiModelProperty("数量")
	private int actualNum;

	public int getBillNo() {
		return billNo;
	}

	public void setBillNo(int billNo) {
		this.billNo = billNo;
	}

	public String getOutboundCode() {
		return outboundCode;
	}

	public void setOutboundCode(String outboundCode) {
		this.outboundCode = outboundCode;
	}

	public String getCrtatStaff() {
		return crtatStaff;
	}

	public void setCrtatStaff(String crtatStaff) {
		this.crtatStaff = crtatStaff;
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

	public String getGoodsFactory() {
		return goodsFactory;
	}

	public void setGoodsFactory(String goodsFactory) {
		this.goodsFactory = goodsFactory;
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

	public int getActualNum() {
		return actualNum;
	}

	public void setActualNum(int actualNum) {
		this.actualNum = actualNum;
	}

	public Date getDealTime() {
		return dealTime;
	}

	public void setDealTime(Date dealTime) {
		this.dealTime = dealTime;
	}
	
	

}
