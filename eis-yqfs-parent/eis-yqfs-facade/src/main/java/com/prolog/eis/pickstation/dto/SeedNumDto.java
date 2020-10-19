package com.prolog.eis.pickstation.dto;

/**
 * @author SunPP and Rod Johnson
 * @date 2020/07/04 16:01
 */
public class SeedNumDto {

    private int hzId;
    private String orderBoxNo;
    private int boxNum;

    public int getHzId() {
        return hzId;
    }

    public void setHzId(int hzId) {
        this.hzId = hzId;
    }

    public String getOrderBoxNo() {
        return orderBoxNo;
    }

    public void setOrderBoxNo(String orderBoxNo) {
        this.orderBoxNo = orderBoxNo;
    }

    public int getBoxNum() {
        return boxNum;
    }

    public void setBoxNum(int boxNum) {
        this.boxNum = boxNum;
    }
}
