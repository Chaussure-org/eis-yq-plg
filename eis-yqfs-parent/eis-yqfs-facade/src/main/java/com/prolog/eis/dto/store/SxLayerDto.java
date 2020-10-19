package com.prolog.eis.dto.store;

/**
 * 层状态Dto
 */
public class SxLayerDto {

    private int layer;

    private int inboundCount;//入库任务数

    private int outBoundCount;

    private int isLock;

    public SxLayerDto() {
    }

    public SxLayerDto(int layer, int inboundCount, int outBoundCount, int isLock) {
        this.layer = layer;
        this.inboundCount = inboundCount;
        this.outBoundCount = outBoundCount;
        this.isLock = isLock;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public int getInboundCount() {
        return inboundCount;
    }

    public void setInboundCount(int inboundCount) {
        this.inboundCount = inboundCount;
    }

    public int getOutBoundCount() {
        return outBoundCount;
    }

    public void setOutBoundCount(int outBoundCount) {
        this.outBoundCount = outBoundCount;
    }

    public int getIsLock() {
        return isLock;
    }

    public void setIsLock(int isLock) {
        this.isLock = isLock;
    }
}
