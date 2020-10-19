package com.prolog.eis.dto.store;

/**
 * 层任务数
 * @author SunPP and Rod Johnson
 * @date 2020/07/11 15:06
 */
public class LayerTaskCountDto {
    private int layer;
    private int outCount;
    private int inCount;
    private String rgvId;
    private int status;

    public String getRgvId() {
        return rgvId;
    }

    public void setRgvId(String rgvId) {
        this.rgvId = rgvId;
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

    public int getOutCount() {
        return outCount;
    }

    public void setOutCount(int outCount) {
        this.outCount = outCount;
    }

    public int getInCount() {
        return inCount;
    }

    public void setInCount(int inCount) {
        this.inCount = inCount;
    }
}
