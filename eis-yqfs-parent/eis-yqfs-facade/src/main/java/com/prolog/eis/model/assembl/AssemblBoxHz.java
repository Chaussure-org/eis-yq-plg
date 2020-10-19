package com.prolog.eis.model.assembl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prolog.framework.core.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import springfox.documentation.annotations.ApiIgnore;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 合箱计划汇总表(AssemblBoxHz)实体类
 *
 * @author panteng
 * @since 2020-05-03 16:56:41
 */
@Table("assembl_box_hz")
public class AssemblBoxHz implements Serializable {
    private static final long serialVersionUID = -27388219993135114L;

    public static final int TASK_STATE_CREAYE = 0;//创建状态
    public static final int TASK_STATE_ISSUE = 1;//下发状态
    public static final int TASK_STATE_EXECUTE = 2;//执行中
    public static final int TASK_STATE_FINISH = 3;//已完成
    public static final int TASK_STATE_ERROR = 4;//异常

    @Id
    @Column("id")
    @ApiModelProperty("id")
	@AutoKey(type = AutoKey.TYPE_IDENTITY)
    private int id;
    
    @Column("goods_id")
    @ApiModelProperty("商品id")
    private int goodsId;
    
    @Column("station_id")
    @ApiModelProperty("作业站台id")
    private int stationId;
    
    @Column("task_state")
    @ApiModelProperty("任务状态 0创建 1下发")
    private int taskState;
    
    @Column("create_time")
    @ApiModelProperty("创建时间（YYYY-MM-DD HH:MM:SS）")
    private Date createTime;

    @Column("release_time")
    @ApiModelProperty("下发时间（YYYY-MM-DD HH:MM:SS）")
    private Date releaseTime;

    @Column("all_is_out")
    @ApiModelProperty("是否全部出库")
    private boolean allIsOut;

    public boolean isAllIsOut() {
        return allIsOut;
    }

    public void setAllIsOut(boolean allIsOut) {
        this.allIsOut = allIsOut;
    }

    @Ignore
    private List<AssemblBoxMx> mxs;

    public List<AssemblBoxMx> getMxs() {
        return mxs;
    }

    public void setMxs(List<AssemblBoxMx> mxs) {
        this.mxs = mxs;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public int getTaskState() {
        return taskState;
    }

    public void setTaskState(int taskState) {
        this.taskState = taskState;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }
}