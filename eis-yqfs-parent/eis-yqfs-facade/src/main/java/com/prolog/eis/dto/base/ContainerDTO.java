package com.prolog.eis.dto.base;

/**
 * 容器数据对象
 */
public class ContainerDTO {
    private String number;//编号
    private String direction;//方位

    public ContainerDTO(){}
    public ContainerDTO(String number,String direction){
        this.number = number;
        this.direction = direction;
    }
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

}
