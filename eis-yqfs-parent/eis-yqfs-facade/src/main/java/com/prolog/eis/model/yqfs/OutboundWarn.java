package com.prolog.eis.model.yqfs;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Ignore;
import com.prolog.framework.core.annotation.Table;

import io.swagger.annotations.ApiModelProperty;

@Table("outbound_warn")
public class OutboundWarn {
	
	@Column("goods_id")
	@ApiModelProperty("商品id")
	private int goodsId;

	@Column("EXPECT_OUTNUM")
	@ApiModelProperty("预计出库值")
	private int expectOutnum;

	@Column("LAST_UPDATE_TIME")
	@ApiModelProperty("最后更新时间")
	@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
	private Date lastUpdateTime;

	@Ignore
	@ApiModelProperty("日均出库")
	private int dayOut;

	public int getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}

	public int getExpectOutnum() {
		return expectOutnum;
	}

	public void setExpectOutnum(int expectOutnum) {
		this.expectOutnum = expectOutnum;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public int getDayOut() {
		return dayOut;
	}

	public void setDayOut(int dayOut) {
		this.dayOut = dayOut;
	}

}
