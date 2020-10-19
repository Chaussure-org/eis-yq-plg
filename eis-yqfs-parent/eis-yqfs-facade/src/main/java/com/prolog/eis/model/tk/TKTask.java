package com.prolog.eis.model.tk;

import com.prolog.framework.core.annotation.AutoKey;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;

import java.util.Date;

@Table("tk_task")
public class TKTask {

    public static final int STATUS_NEW=0;//新建
    public static final int STATUS_RELEASE=1;//下发
    public static final int STATUS_PROCESSING=2;//退库中
    public static final int STATUS_FINISH=3;//完成

    @Id
    @AutoKey(type=AutoKey.TYPE_IDENTITY)
    private int id;
    @Column("status")
    private int status;
    @Column("container_no")
    private String containerNo;
    @Column("gmt_create_time")
    private Date gmtCreateTime;
    @Column("gmt_update_time")
    private Date gmtUpdateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public Date getGmtCreateTime() {
        return gmtCreateTime;
    }

    public void setGmtCreateTime(Date gmtCreateTime) {
        this.gmtCreateTime = gmtCreateTime;
    }

    public Date getGmtUpdateTime() {
        return gmtUpdateTime;
    }

    public void setGmtUpdateTime(Date gmtUpdateTime) {
        this.gmtUpdateTime = gmtUpdateTime;
    }
}
