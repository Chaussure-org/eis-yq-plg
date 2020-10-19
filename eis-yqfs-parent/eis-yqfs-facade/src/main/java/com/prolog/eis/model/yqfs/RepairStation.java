package com.prolog.eis.model.yqfs;

import java.util.Date;
import java.io.Serializable;
import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

/**
 * (RepairStation)实体类
 *
 * @author panteng
 * @since 2020-04-25 10:28:40
 */
@Table("repair_station")
public class RepairStation implements Serializable {
    private static final long serialVersionUID = -22013527287817778L;
    @Id
    @Column("station_no")
    @ApiModelProperty("补货站台编号")
    private String stationNo;
    
    @Column("curr_work_container_no")
    @ApiModelProperty("当前补货料箱")
    private String currWorkContainerNo;
    
    @Column("last_update_date")
    @ApiModelProperty("最后更新时间")
    private Date lastUpdateDate;
    


    public String getStationNo() {
        return stationNo;
    }

    public void setStationNo(String stationNo) {
        this.stationNo = stationNo;
    }

    public String getCurrWorkContainerNo() {
        return currWorkContainerNo;
    }

    public void setCurrWorkContainerNo(String currWorkContainerNo) {
        this.currWorkContainerNo = currWorkContainerNo;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

}