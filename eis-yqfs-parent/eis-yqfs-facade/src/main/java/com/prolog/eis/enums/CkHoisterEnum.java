package com.prolog.eis.enums;

public enum CkHoisterEnum {

    R01("020010006502", "T01"),
    R02("020008006502", "T02"),
    R03("020006006502", "T03");

    private String target;
    private String value;


    CkHoisterEnum(String target, String value) {
        this.target = target;
        this.value = value;
    }

    public static String getValues(String target) {
        for (CkHoisterEnum ckHoisterEnum : CkHoisterEnum.values()) {
            if (ckHoisterEnum.target.equals(target)){
                return ckHoisterEnum.value;
            }
        }
        return null;
    }


    public static String getAddress(String value) {
        for (CkHoisterEnum ckHoisterEnum : CkHoisterEnum.values()) {
            if (ckHoisterEnum.value.equals(value)){
                return ckHoisterEnum.target;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        String t02 = CkHoisterEnum.getAddress("T02");
        System.out.println(t02);
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
