package com.prolog.eis.model.store;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@Table("SX_HOISTER")
public class SxHoister {

	@Id
	@ApiModelProperty("提升机ID")
	@AutoKey(type = AutoKey.TYPE_IDENTITY)
	private int id;
	
	
	@Column("HOISTER_NO")
	@ApiModelProperty("提升机编号")
	private String hoisterNo;
	
	@Column("IS_LOCK")
	@ApiModelProperty("是否锁定")
	private int isLock;
	
	@Column("HOISTER_TYPE")
	@ApiModelProperty("提升机类型 1.库内(跨层) 2.库外 3.直通口 4拆叠盘机")
	private int hoisterType;
	
	@Column("CREATE_TIME")
	@ApiModelProperty("创建时间")
	private Date createTime;

	@Column("error_status")
	@ApiModelProperty("异常状态(1异常, 2正常)")
	private int errorStatus;

	@Column("error_code")
	@ApiModelProperty("'异常状态码'")
	private int errorCode;

	@ApiModelProperty("故障 异常信息")
	@Column("error_msg")
	private String errorMsg;

	@Column("area")
	@ApiModelProperty("提升机区域")
	private int area;
	
	public SxHoister() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getHoisterNo() {
		return hoisterNo;
	}

	public void setHoisterNo(String hoisterNo) {
		this.hoisterNo = hoisterNo;
	}

	public int getIsLock() {
		return isLock;
	}

	public void setIsLock(int isLock) {
		this.isLock = isLock;
	}

	public int getHoisterType() {
		return hoisterType;
	}

	public void setHoisterType(int hoisterType) {
		this.hoisterType = hoisterType;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getErrorStatus() {
		return errorStatus;
	}

	public void setErrorStatus(int errorStatus) {
		this.errorStatus = errorStatus;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public int getArea() {
		return area;
	}

	public void setArea(int area) {
		this.area = area;
	}

	public SxHoister(int id, String hoisterNo, int isLock, int hoisterType, Date createTime, int errorStatus,
			int errorCode, String errorMsg, int area) {
		super();
		this.id = id;
		this.hoisterNo = hoisterNo;
		this.isLock = isLock;
		this.hoisterType = hoisterType;
		this.createTime = createTime;
		this.errorStatus = errorStatus;
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
		this.area = area;
	}



}