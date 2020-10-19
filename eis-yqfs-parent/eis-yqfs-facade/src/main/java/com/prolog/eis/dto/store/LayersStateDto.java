package com.prolog.eis.dto.store;

import java.util.List;

public class LayersStateDto {

    private List<SxLayerDto> cengStateList;

    public LayersStateDto() {
    }

    public LayersStateDto(List<SxLayerDto> cengStateList) {
        this.cengStateList = cengStateList;
    }

    public List<SxLayerDto> getCengStateList() {
        return cengStateList;
    }

    public void setCengStateList(List<SxLayerDto> cengStateList) {
        this.cengStateList = cengStateList;
    }
}
