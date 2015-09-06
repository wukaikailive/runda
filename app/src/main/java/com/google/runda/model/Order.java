package com.google.runda.model;

import java.sql.Date;
//订单表
public class Order {

	public int ID;
	public int StoreID;//水店id
	public int DeliverymanID;//送水员id
	public int SubmitterID;//提交者id
	public String ReceiverName;//收货人姓名
	public String ReceiverCellPhone;//收货人电话
	public String ReceiverTime;//收货时间
	public String Comment;//备注
	public float TotalPrice;//总价
	public Date SubmitTime;//提交时间
	public int Status;//状态
	public String FailureComment;//失败备注
	public int ProvinceID;
	public int CityID;
	public int CountryID;
	public String DetailAddress;//详细地址
}
