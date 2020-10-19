package com.prolog.eis.model.orderboxline;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;


/**
 * 订单箱线任务历史表(OrderboxLineTaskHis)实体类
 *
 * @author panteng
 * @since 2020-05-04 16:21:04
 */
@Table("orderbox_line_task_his")
public class OrderboxLineTaskHis implements Serializable {
    private static final long serialVersionUID = -77540830174082046L;
    @Column("task_id")
    @ApiModelProperty("任务id")
    private String taskId;
    
    @Column("container_no")
    @ApiModelProperty("订单箱号")
    private String containerNo;
    
    @Column("address")
    @ApiModelProperty("【请求地址 （定位到拣选站具有拣选位）】")
    private String address;
    
    @Column("create_time")
    @ApiModelProperty("创建时间")
    private Date createTime;
    
    @Column("task_status")
    @ApiModelProperty("1 发送失败 2 发送成功 3 任务开始 4 效验失败 5 任务完成")
    private Integer taskStatus;
    
    @Column("send_count")
    @ApiModelProperty("发送次数")
    private Integer sendCount;
    
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