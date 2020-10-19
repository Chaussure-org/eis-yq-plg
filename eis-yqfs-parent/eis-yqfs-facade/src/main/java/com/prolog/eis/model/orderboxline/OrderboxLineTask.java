package com.prolog.eis.model.orderboxline;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


/**
 * 订单箱线任务表(OrderboxLineTask)实体类
 *
 * @author panteng
 * @since 2020-05-04 16:20:45
 */
@Table("orderbox_line_task")
public class OrderboxLineTask implements Serializable {
    private static final long serialVersionUID = 411790047570342863L;
    @Id
    @Column("task_id")
    @ApiModelProperty("任务id")
    @NotEmpty(message = "任务id不能为空")
    private String taskId;
    
    @Column("container_no")
    @ApiModelProperty("订单箱号")
    @NotEmpty(message = "订单箱号不能为空")
    private String containerNo;

	@Column("station_no")
	@ApiModelProperty("拣选站编号")
	private String stationNo;

    @Column("address")
    @ApiModelProperty("【请求地址 （定位到拣选站具有拣选位）】")
    @NotEmpty(message = "请求地址不能为空")
    private String address;
    
    @Column("create_time")
    @ApiModelProperty("创建时间")
    private Date createTime;
    
    @Column("task_status")
    @ApiModelProperty("0 已创建 1 发送失败 2 发送成功 3 任务开始 4 效验失败 5 任务完成")
    @NotNull(message = "任务状态不能为空")
    private Integer taskStatus;
    
    @Column("send_count")
    @ApiModelProperty("发送次数")
    private Integer sendCount;

    @Column("store_index")
    @ApiModelProperty("优先级（级别高的优先分配）")
    private int storeIndex;

    @Column("send_error_msg")
    @ApiModelProperty("发送失败  接收的错误消息")
    private String sendErrorMsg;
    
    @Column("validat_error_msg")
    @ApiModelProperty("效验时错误信息")
    private String validatErrorMsg;
    
    @Column("start_time")
    @ApiModelProperty("任务开始时间")
    private Date startTime;
    
    @Column("finish_time")
    @ApiModelProperty("任务完成时间")
    private Date finishTime;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public String getStationNo() {
        return stationNo;
    }

    public void setStationNo(String stationNo) {
        this.stationNo = stationNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Integer getSendCount() {
        return sendCount;
    }

    public void setSendCount(Integer sendCount) {
        this.sendCount = sendCount;
    }

    public int getStoreIndex() {
        return storeIndex;
    }

    public void setStoreIndex(int storeIndex) {
        this.storeIndex = storeIndex;
    }

    public String getSendErrorMsg() {
        return sendErrorMsg;
    }

    public void setSendErrorMsg(String sendErrorMsg) {
        this.sendErrorMsg = sendErrorMsg;
    }

    public String getValidatErrorMsg() {
        return validatErrorMsg;
    }

    public void setValidatErrorMsg(String validatErrorMsg) {
        this.validatErrorMsg = validatErrorMsg;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }
}