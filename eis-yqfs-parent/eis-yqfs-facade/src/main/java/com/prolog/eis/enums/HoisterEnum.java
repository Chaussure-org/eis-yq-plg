package com.prolog.eis.enums;

/**
 * @author LuoLi
 * @version 1.0
 * @date 2020/7/13 22:00
 */
public enum HoisterEnum {

    R01("R01", "T01"),
    R02("R02", "T02"),
    R03("R03", "T03");

    private String target;
    private String value;


    HoisterEnum(String target, String value) {
        this.target = target;
        this.value = value;
    }

    public static String getValues(String address) {
        for (HoisterEnum hoisterEnum : HoisterEnum.values()) {
            if (hoisterEnum.target.equals(address)) {
                return hoisterEnum.value;
            }
        }
        return null;
    }

    public static String getAddress(String address) {
        for (HoisterEnum hoisterEnum : HoisterEnum.values()) {
            if (hoisterEnum.value.equals(address)) {
                return hoisterEnum.target;
            }
        }
        return null;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


}
