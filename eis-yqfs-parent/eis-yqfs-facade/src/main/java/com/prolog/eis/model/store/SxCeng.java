package com.prolog.eis.model.store;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;


/**
 * 四向库层表(SxCeng)实体类
 *
 * @author panteng
 * @since 2020-05-08 17:21:36
 */
@Table("sx_layer")
public class SxCeng implements Serializable {
    private static final long serialVersionUID = -93571877907474615L;
    @Column("layer")
    @ApiModelProperty("层")
    private Integer layer;
    
    @Column("lock_state")
    @ApiModelProperty("0 未锁定 1 锁定")
    private Integer lockState;

    @Column("is_disable")
    @ApiModelProperty("0:不禁用 1:禁用")
    private Boolean isDisable;


    public Integer getLayer() {
        return layer;
    }

    public void setLayer(Integer layer) {
        this.layer = layer;
    }

    public Integer getLockState() {
        return lockState;
    }

    public void setLockState(Integer lockState) {
        this.lockState = lockState;
    }

    public Boolean getDisable() {
        return isDisable;
    }

    public void setDisable(Boolean disable) {
        isDisable = disable;
    }
}