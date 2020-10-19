package com.prolog.eis.dto.outbound;

/**
 * 订单箱标签Dto
 */
public class OutBoundOrderBoxDto {

    private String bcNo;//波次

    private String orderBoxNo;//订单箱编号

    private int supplierNo;

    private String supplierName;

    private String area;

    public OutBoundOrderBoxDto() {
    }

    public OutBoundOrderBoxDto(String bcNo, String orderBoxNo, int supplierNo, String supplierName, String area) {
        this.bcNo = bcNo;
        this.orderBoxNo = orderBoxNo;
        this.supplierNo = supplierNo;
        this.supplierName = supplierName;
        this.area = area;
    }

    public String getBcNo() {
        return bcNo;
    }

    public void setBcNo(String bcNo) {
        this.bcNo = bcNo;
    }

    public String getOrderBoxNo() {
        return orderBoxNo;
    }

    public void setOrderBoxNo(String orderBoxNo) {
        this.orderBoxNo = orderBoxNo;
    }

    public int getSupplierNo() {
        return supplierNo;
    }

    public void setSupplierNo(int supplierNo) {
        this.supplierNo = supplierNo;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
