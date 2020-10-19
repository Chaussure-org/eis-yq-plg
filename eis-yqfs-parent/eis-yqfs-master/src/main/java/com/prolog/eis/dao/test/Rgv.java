package com.prolog.eis.dao.test;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Table;

/**
 * @Author wangkang
 * @Description
 * @CreateTime 2020/8/10 0:31
 */
@Table("rgv")
public class Rgv {

    @Column("rgv_id")
    private int rgvId;

    @Column("layer")
    private int layer;

    @Column("status")
    private int status;

    public int getRgvId() {
        return rgvId;
    }

    public void setRgvId(int rgvId) {
        this.rgvId = rgvId;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
