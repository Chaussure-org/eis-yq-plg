package com.prolog.eis.dto.apprf;
import java.util.Date;

/**
 * @author jinxf
 * @time 2020/4/26 16:47
 */
public class AppInstockOrderCcceptanceDto {

	private Integer id;

	private String containerNo;
	
	private String grid;

	private String  containerSubNo;
	private String  goodsBarCode;

	private Integer  goodsId;

	private String  goodsNo;

	private String  goodsName;

	private Integer  goodsNum;
	private Integer  referenceNum;

	public String getGoodsBarCode() {
		return goodsBarCode;
	}

	public void setGoodsBarCode(String goodsBarCode) {
		this.goodsBarCode = goodsBarCode;
	}

	public Integer getReferenceNum() {
		return referenceNum;
	}

	public void setReferenceNum(Integer referenceNum) {
		this.referenceNum = referenceNum;
	}

	public Date getInboundTime() {
		return inboundTime;
	}

	public void setInboundTime(Date inboundTime) {
		this.inboundTime = inboundTime;
	}

	private Date expiryDate;
	private Date inboundTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	public String getGrid() {
		return grid;
	}

	public void setGrid(String grid) {
		this.grid = grid;
	}

	public String getContainerSubNo() {
		return containerSubNo;
	}

	public void setContainerSubNo(String containerSubNo) {
		this.containerSubNo = containerSubNo;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public Integer getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(Integer goodsNum) {
		this.goodsNum = goodsNum;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
}
