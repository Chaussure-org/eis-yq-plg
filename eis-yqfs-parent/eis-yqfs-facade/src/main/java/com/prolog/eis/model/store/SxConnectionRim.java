package com.prolog.eis.model.store;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@Table("sx_connection_rim")
public class SxConnectionRim {

	@Id
	@ApiModelProperty("ID")
	@AutoKey(type = AutoKey.TYPE_IDENTITY)
	private int id;
	
	@Column("sx_hoister_id")
	@ApiModelProperty("提升机ID")
	private int sxHoisterId;
	
	@Column("line_state")
	@ApiModelProperty("线体状态(0：无、1：向内、2：向外、3：双向)")
	private int lineState;
	
	@Column("entry_code")
	@ApiModelProperty("出入口编号例如：T150101,T150102,T150103")
	private String entryCode;

	@Column("wms_entry_code")
	@ApiModelProperty("wms出入口(xyz拼接）")
	private String wmsEntryCode;
	
	@Column("entry_name")
	@ApiModelProperty("出入口名称(WMS系统下传的货位名称)")
	private String entryName;
	
	@Column("entry_type")
	@ApiModelProperty("出入口类型(10：入库口/出库口,20：接驳口)//1.内侧、2.外侧")
	private int entryType;
	
	@Column("section")
	@ApiModelProperty("库区")
	private int section;
	
	@Column("route_code")
	@ApiModelProperty("路由code")
	private String routeCode;
	
	@Column("route_name")
	@ApiModelProperty("路由名")
	private String routeName;
	
	@Column("status")
	@ApiModelProperty("容器状态 0 = Disabled，禁用 1 = Enabled，启用")
	private int status;
	
	@Column("layer")
	@ApiModelProperty("层")
	private int layer;
	
	@Column("x")
	@ApiModelProperty("X")
	private int x;
	
	@Column("y")
	@ApiModelProperty("Y")
	private int y;

	@Column("line_read_only")
	@ApiModelProperty("线体流向是否只读  0否 1是")
	private int lineReadOnly;
	
	@Column("create_time")
	@ApiModelProperty("创建时间")
	private Date createTime;
	
	@Column("last_update_time")
	@ApiModelProperty("最后更新时间")
	private Date lastUpdateTime;
	
	@Column("cache_count")
	@ApiModelProperty("缓存位数量")
	private int cacheCount;

	@Column("error_container_no")
	@ApiModelProperty("异常托盘号")
	private String errorContainerNo;
	
	@Column("error_msg")
	@ApiModelProperty("异常消息")
	private String errorMsg;
	
	@Column("error_state")
	@ApiModelProperty("异常状态'0.正常、1、发送托盘回滚")
	private int errorState;
	
	@Column("is_outBound_rim")
	@ApiModelProperty("是否允许gcs到达(0.否、1.是)")
	private int isOutBoundRim;
	
	
	public SxConnectionRim() {
		super();
		// TODO Auto-generated constructor stub
	}


	public SxConnectionRim(int id, int sxHoisterId, int lineState, String entryCode, String wmsEntryCode,
			String entryName, int entryType, int section, String routeCode, String routeName, int status, int layer,
			int x, int y, int lineReadOnly, Date createTime, Date lastUpdateTime, int cacheCount,
			String errorContainerNo, String errorMsg, int errorState, int isOutBoundRim) {
		super();
		this.id = id;
		this.sxHoisterId = sxHoisterId;
		this.lineState = lineState;
		this.entryCode = entryCode;
		this.wmsEntryCode = wmsEntryCode;
		this.entryName = entryName;
		this.entryType = entryType;
		this.section = section;
		this.routeCode = routeCode;
		this.routeName = routeName;
		this.status = status;
		this.layer = layer;
		this.x = x;
		this.y = y;
		this.lineReadOnly = lineReadOnly;
		this.createTime = createTime;
		this.lastUpdateTime = lastUpdateTime;
		this.cacheCount = cacheCount;
		this.errorContainerNo = errorContainerNo;
		this.errorMsg = errorMsg;
		this.errorState = errorState;
		this.isOutBoundRim = isOutBoundRim;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getSxHoisterId() {
		return sxHoisterId;
	}


	public void setSxHoisterId(int sxHoisterId) {
		this.sxHoisterId = sxHoisterId;
	}


	public int getLineState() {
		return lineState;
	}


	public void setLineState(int lineState) {
		this.lineState = lineState;
	}


	public String getEntryCode() {
		return entryCode;
	}


	public void setEntryCode(String entryCode) {
		this.entryCode = entryCode;
	}


	public String getWmsEntryCode() {
		return wmsEntryCode;
	}


	public void setWmsEntryCode(String wmsEntryCode) {
		this.wmsEntryCode = wmsEntryCode;
	}


	public String getEntryName() {
		return entryName;
	}


	public void setEntryName(String entryName) {
		this.entryName = entryName;
	}


	public int getEntryType() {
		return entryType;
	}


	public void setEntryType(int entryType) {
		this.entryType = entryType;
	}


	public int getSection() {
		return section;
	}


	public void setSection(int section) {
		this.section = section;
	}


	public String getRouteCode() {
		return routeCode;
	}


	public void setRouteCode(String routeCode) {
		this.routeCode = routeCode;
	}


	public String getRouteName() {
		return routeName;
	}


	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}


	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
	}


	public int getLayer() {
		return layer;
	}


	public void setLayer(int layer) {
		this.layer = layer;
	}


	public int getX() {
		return x;
	}


	public void setX(int x) {
		this.x = x;
	}


	public int getY() {
		return y;
	}


	public void setY(int y) {
		this.y = y;
	}


	public int getLineReadOnly() {
		return lineReadOnly;
	}


	public void setLineReadOnly(int lineReadOnly) {
		this.lineReadOnly = lineReadOnly;
	}


	public Date getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}


	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}


	public int getCacheCount() {
		return cacheCount;
	}


	public void setCacheCount(int cacheCount) {
		this.cacheCount = cacheCount;
	}


	public String getErrorContainerNo() {
		return errorContainerNo;
	}


	public void setErrorContainerNo(String errorContainerNo) {
		this.errorContainerNo = errorContainerNo;
	}


	public String getErrorMsg() {
		return errorMsg;
	}


	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}


	public int getErrorState() {
		return errorState;
	}


	public void setErrorState(int errorState) {
		this.errorState = errorState;
	}


	public int getIsOutBoundRim() {
		return isOutBoundRim;
	}


	public void setIsOutBoundRim(int isOutBoundRim) {
		this.isOutBoundRim = isOutBoundRim;
	}


	@Override
	public String toString() {
		return "SxConnectionRim [id=" + id + ", sxHoisterId=" + sxHoisterId + ", lineState=" + lineState
				+ ", entryCode=" + entryCode + ", wmsEntryCode=" + wmsEntryCode + ", entryName=" + entryName
				+ ", entryType=" + entryType + ", section=" + section + ", routeCode=" + routeCode + ", routeName="
				+ routeName + ", status=" + status + ", layer=" + layer + ", x=" + x + ", y=" + y + ", lineReadOnly="
				+ lineReadOnly + ", createTime=" + createTime + ", lastUpdateTime=" + lastUpdateTime + ", cacheCount="
				+ cacheCount + ", errorContainerNo=" + errorContainerNo + ", errorMsg=" + errorMsg + ", errorState="
				+ errorState + ", isOutBoundRim=" + isOutBoundRim + "]";
	}



	
}
