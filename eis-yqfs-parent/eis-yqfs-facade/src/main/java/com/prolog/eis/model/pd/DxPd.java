package com.prolog.eis.model.pd;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @author LuoLi
 * @version 1.0
 * @date 2020/7/12 16:54
 */
@Table("dx_pd")
public class DxPd {

    @ApiModelProperty("容器Id")
    @Column("container_no")
    private String containerNo;

    @ApiModelProperty("跟新时间")
    @Column("update_time")
    private Date updateTime;

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
