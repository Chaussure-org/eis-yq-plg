package com.prolog.eis.model.store;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 小车跨层历史表(SxCarAcross)实体类
 *
 * @author panteng
 * @since 2020-04-13 15:49:21
 */
@Table("sx_car_across_his")
public class SxCarAcrossHis implements Serializable {
    private static final long serialVersionUID = 567482874639713738L;

    @Id
    @Column("id")
    @ApiModelProperty("主键id")
    private Integer id;

    @Column("source_layer")
    @ApiModelProperty("源层")
    private Integer sourceLayer;

    @Column("source_area")
    @ApiModelProperty("源区域")
    private Integer sourceArea;

    @Column("tar_layer")
    @ApiModelProperty("目标层")
    private Integer tarLayer;

    @Column("tar_area")
    @ApiModelProperty("目标区域")
    private Integer tarArea;

    @Column("car_no")
    @ApiModelProperty("小车编号")
    private String carNo;

    @Column("sx_hoister_id")
    @ApiModelProperty("唯一指定跨层提升机")
    private Integer sxHoisterId;

    @Column("sx_hoister_no")
    @ApiModelProperty("提升机编号")
    private String sxHoisterNo;

    @Column("across_status")
    @ApiModelProperty("跨层状态(0,已创建 ,1 小车去往源层接驳口, 2 小车到达源层接驳口 , 3.提升机去往源层 ,4 提升机到达源层 ,5 小车去往提升机, 6 小车到达提升机,7 提升机去往目标层 ,8 提升机到达目标层, 9 小车去往目标层接驳口,10 小车到达目标层接驳口, 11 回告提升机解锁完成)")
    private Integer acrossStatus;

    @Column("create_time")
    @ApiModelProperty("创建时间")
    private Date createTime;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSourceLayer() {
        return sourceLayer;
    }

    public void setSourceLayer(Integer sourceLayer) {
        this.sourceLayer = sourceLayer;
    }

    public Integer getSourceArea() {
        return sourceArea;
    }

    public void setSourceArea(Integer sourceArea) {
        this.sourceArea = sourceArea;
    }

    public Integer getTarLayer() {
        return tarLayer;
    }

    public void setTarLayer(Integer tarLayer) {
        this.tarLayer = tarLayer;
    }

    public Integer getTarArea() {
        return tarArea;
    }

    public void setTarArea(Integer tarArea) {
        this.tarArea = tarArea;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public Integer getSxHoisterId() {
        return sxHoisterId;
    }

    public void setSxHoisterId(Integer sxHoisterId) {
        this.sxHoisterId = sxHoisterId;
    }

    public String getSxHoisterNo() {
        return sxHoisterNo;
    }

    public void setSxHoisterNo(String sxHoisterNo) {
        this.sxHoisterNo = sxHoisterNo;
    }

    public Integer getAcrossStatus() {
        return acrossStatus;
    }

    public void setAcrossStatus(Integer acrossStatus) {
        this.acrossStatus = acrossStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}