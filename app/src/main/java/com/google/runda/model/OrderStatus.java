package com.google.runda.model;

/**
 * Created by bigface on 2015/9/18.
 */
public enum OrderStatus {
    submitted("订单已提交,待处理"), //订单已提交 0
    to_be_distributed("订单待分配送水工"),//订单待分配送水工 1
    canceled("订单已取消"),//订单已取消 2
    allocated("已分配送水工"),//已分配送水工 3
    distribution("订单配送中"),//订单配送中 4
    distribution_unsuccessful("配送失败"),//配送失败 5
    distribution_successful("配送成功"),//配送成功 6
    confirm_receipt("已收货"),//确认收货 7
    finished("订单已完成");//订单已完成--评价之后 8

    private String tag;
    private OrderStatus (String tag){
        this.tag=tag;
    }

    public String getTag(){
        return this.tag;
    }
}

