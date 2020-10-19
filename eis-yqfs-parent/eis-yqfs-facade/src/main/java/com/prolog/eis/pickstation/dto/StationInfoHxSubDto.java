package com.prolog.eis.pickstation.dto;

public class StationInfoHxSubDto extends StationInfoSubLxDto {
    //是否合箱，默认0表示不合箱
    private int isIntegrationLx = 0;
    //合箱商品数量
    private int integrationCount;

    public StationInfoHxSubDto() {
    }

    public StationInfoHxSubDto(int isIntegrationLx, int integrationCount) {
        this.isIntegrationLx = isIntegrationLx;
        this.integrationCount = integrationCount;
    }

    public int getIsIntegrationLx() {
        return isIntegrationLx;
    }

    public void setIsIntegrationLx(int isIntegrationLx) {
        this.isIntegrationLx = isIntegrationLx;
    }

    public int getIntegrationCount() {
        return integrationCount;
    }

    public void setIntegrationCount(int integrationCount) {
        this.integrationCount = integrationCount;
    }
}
