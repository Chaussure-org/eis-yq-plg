package com.prolog.eis.dto.store;

import java.util.List;

public class LayerStoreTaskDto {
    private int layer;
    private List<StoreTaskDto> storeTasks;

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public List<StoreTaskDto> getStoreTasks() {
        return storeTasks;
    }

    public void setStoreTasks(List<StoreTaskDto> storeTasks) {
        this.storeTasks = storeTasks;
    }
}
