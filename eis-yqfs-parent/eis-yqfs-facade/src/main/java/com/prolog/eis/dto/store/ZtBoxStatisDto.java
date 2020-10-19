package com.prolog.eis.dto.store;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author panteng
 * @description: 在途料箱监控
 * @date 2020/5/8 20:10
 */
public class ZtBoxStatisDto {

    @ApiModelProperty("播种任务数")
    private int sowingTasks;

    @ApiModelProperty("盘点任务数")
    private int inventoryTasks;

    @ApiModelProperty("退库任务数")
    private int returnTask;

    @ApiModelProperty("补货任务数")
    private int replenishmentTasks;

    @ApiModelProperty("入库换箱任务数")
    private int arehousingTasks;

    @ApiModelProperty("合箱任务数")
    private int boxingTasks;

    @ApiModelProperty("空托盘入库任务")
    private int emptytraystorageTasks;

    public int getSowingTasks() {
        return sowingTasks;
    }

    public void setSowingTasks(int sowingTasks) {
        this.sowingTasks = sowingTasks;
    }

    public int getInventoryTasks() {
        return inventoryTasks;
    }

    public void setInventoryTasks(int inventoryTasks) {
        this.inventoryTasks = inventoryTasks;
    }

    public int getReturnTask() {
        return returnTask;
    }

    public void setReturnTask(int returnTask) {
        this.returnTask = returnTask;
    }

    public int getReplenishmentTasks() {
        return replenishmentTasks;
    }

    public void setReplenishmentTasks(int replenishmentTasks) {
        this.replenishmentTasks = replenishmentTasks;
    }

    public int getArehousingTasks() {
        return arehousingTasks;
    }

    public void setArehousingTasks(int arehousingTasks) {
        this.arehousingTasks = arehousingTasks;
    }

    public int getBoxingTasks() {
        return boxingTasks;
    }

    public void setBoxingTasks(int boxingTasks) {
        this.boxingTasks = boxingTasks;
    }

    public int getEmptytraystorageTasks() {
        return emptytraystorageTasks;
    }

    public void setEmptytraystorageTasks(int emptytraystorageTasks) {
        this.emptytraystorageTasks = emptytraystorageTasks;
    }
}
