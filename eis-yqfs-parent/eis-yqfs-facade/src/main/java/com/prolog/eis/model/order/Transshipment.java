package com.prolog.eis.model.order;


import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Id;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

@Table("transshipment")
public class Transshipment {

    @Id
    @Column("transshipment_id")
    private int transshipmentId;

    @Column("transshipment_name")
    @ApiModelProperty("中转仓名称")
    private String transshipmentName;

    @Column("transshipment_no")
    @ApiModelProperty("中转仓编号")
    private String transshipmentNo;

    public Transshipment() {
    }

    public Transshipment(int transshipmentId, String transshipmentName, String transshipmentNo) {
        this.transshipmentId = transshipmentId;
        this.transshipmentName = transshipmentName;
        this.transshipmentNo = transshipmentNo;
    }

    public int getTransshipmentId() {
        return transshipmentId;
    }

    public void setTransshipmentId(int transshipmentId) {
        this.transshipmentId = transshipmentId;
    }

    public String getTransshipmentName() {
        return transshipmentName;
    }

    public void setTransshipmentName(String transshipmentName) {
        this.transshipmentName = transshipmentName;
    }

    public String getTransshipmentNo() {
        return transshipmentNo;
    }

    public void setTransshipmentNo(String transshipmentNo) {
        this.transshipmentNo = transshipmentNo;
    }
}
