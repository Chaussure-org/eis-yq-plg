package com.prolog.eis.pickstation.dto;

import java.util.List;
import java.util.Objects;

/**
 * @Author
 * @Description 当前商品汇总消息
 * @CreateTime 2020/6/13 16:25
 */
public class SendCurrentStoreHzDto {

    /**
     *  商品id
     */
    private String ID;

    /**
     *  商品名称
     */
    private String goodsName;

    /**
     *  商品条码
     */
    private List<String> goodsBarCode;

    /**
     *  料箱号
     */
    private String containerNo;

    /**
     *  拣选总计数量
     */
    private int pickTotal;

    /**
     *  拣选总计数量
     */
    private String containerSubNo;

    /**
     * 拣选订单箱号
     */
    private List<Integer> ids;

    public SendCurrentStoreHzDto(String ID, String goodsName, List<String> goodsBarCode, String containerNo, int pickTotal, List<Integer> ids) {
        this.ID = ID;
        this.goodsName = goodsName;
        this.goodsBarCode = goodsBarCode;
        this.containerNo = containerNo;
        this.pickTotal = pickTotal;
        this.ids = ids;
    }

    public SendCurrentStoreHzDto() {
        super();
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public List<String> getGoodsBarCode() {
        return goodsBarCode;
    }

    public void setGoodsBarCode(List<String> goodsBarCode) {
        this.goodsBarCode = goodsBarCode;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public int getPickTotal() {
        return pickTotal;
    }

    public void setPickTotal(int pickTotal) {
        this.pickTotal = pickTotal;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public String getContainerSubNo() {
        return containerSubNo;
    }

    public void setContainerSubNo(String containerSubNo) {
        this.containerSubNo = containerSubNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SendCurrentStoreHzDto that = (SendCurrentStoreHzDto) o;
        return pickTotal == that.pickTotal &&
                Objects.equals(ID, that.ID) &&
                Objects.equals(goodsName, that.goodsName) &&
                Objects.equals(goodsBarCode, that.goodsBarCode) &&
                Objects.equals(containerNo, that.containerNo) &&
                Objects.equals(ids, that.ids);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, goodsName, goodsBarCode, containerNo, pickTotal, ids);
    }

    @Override
    public String toString() {
        return "SendCurrentStoreHzDto{" +
                "ID='" + ID + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", goodsBarCode=" + goodsBarCode +
                ", containerNo='" + containerNo + '\'' +
                ", pickTotal=" + pickTotal +
                ", ids=" + ids +
                '}';
    }
}
