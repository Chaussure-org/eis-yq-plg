package com.prolog.eis.model.store;

import com.prolog.framework.core.annotation.Column;
import com.prolog.framework.core.annotation.Table;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 四向库层小车配置表(SxCarAcrossConfig)实体类
 *
 * @author panteng
 * @since 2020-04-15 16:34:23
 */
@Table("sx_car_across_config")
public class SxCarAcrossConfig implements Serializable {
    private static final long serialVersionUID = -78964891766631534L;
    @Column("layer")
    @ApiModelProperty("层")
    private Integer layer;
    
    @Column("area")
    @ApiModelProperty("区域")
    private Integer area;
    
    @Column("least_cars")
    @ApiModelProperty("最少小车数量")
    private Integer leastCars;
    
    @Column("most_cars")
    @ApiModelProperty("最多小车数量")
    private Integer mostCars;
    
    @Column("layer_defualt_cars")
    @ApiModelProperty("默认小车数量")
    private Integer layerDefualtCars;
    


    public Integer getLayer() {
        return layer;
    }

    public void setLayer(Integer layer) {
        this.layer = layer;
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public Integer getLeastCars() {
        return leastCars;
    }

    public void setLeastCars(Integer leastCars) {
        this.leastCars = leastCars;
    }

    public Integer getMostCars() {
        return mostCars;
    }

    public void setMostCars(Integer mostCars) {
        this.mostCars = mostCars;
    }

    public Integer getLayerDefualtCars() {
        return layerDefualtCars;
    }

    public void setLayerDefualtCars(Integer layerDefualtCars) {
        this.layerDefualtCars = layerDefualtCars;
    }

    public String getBelongAreaAndlayer(){
        return layer + "/" + area;
    }

}