package com.google.runda.model;

/**
 * Created by bigface on 2015/9/14.
 */
public class PlaceOrderFormModel {

    public String waterGoodsID; //水id
    public String waterGoodsCount; //数量
    public String waterGoodsPrice; //价格

    public String waterStoreID; //水店id
    public String recieverPersonName;//
    public String recieverPersonPhone;
    public String recieverAddress;
    public String recieverTime;//收货时间
    public String remark;//备注
    public String settleMethod="货到付款";
}
