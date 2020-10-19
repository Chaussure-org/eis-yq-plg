package com.prolog.eis.enums;

/**
 * @author LuoLi
 * @version 1.0
 * @date 2020/6/15 11:50
 */
public enum ZtStateEnum {

    GO_STATION(1, "去往站台"),
    WAIT_SOW(2, "等待播种"),
    GO_SOW(3, "进入播种位"),
    SOWING(4, "播种中"),
    AWAY_STATION(5, "离开站台");

    private int ztState;
    private String ztStateValue;

    ZtStateEnum(int ztState, String ztStateValue) {
        this.ztState = ztState;
        this.ztStateValue = ztStateValue;
    }

    public int getZtState() {
        return ztState;
    }

    public String getZtStateValue() {
        return ztStateValue;
    }
}
