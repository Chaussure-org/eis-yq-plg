package com.prolog.eis.wcs.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CarListDTO {
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<CarInfoDTO> carryList;

    public List<CarInfoDTO> getCarryList() {
        return carryList;
    }

    public void setCarryList(List<CarInfoDTO> carryList) {
        this.carryList = carryList;
    }
}
