package com.google.runda.model;
//商店
public class Store {

	public String id;
	public String owner;//管理者id
	public String waterStoreName;//商店名字
	public String waterStoreTellPhone;
	public String waterStoreFixedLinePhone;
    public String waterStoreEmail;
    public String waterStoreImage;
    public String isLock;//是否被锁定 0、1
    public String waterStoreStatus;//状态
    public String auditStatus;//审核
    public String auditDetail;

    public String province;
    public String city;
    public String country;
	public String detailAddress;//详细地址
    public String waterStoreLongitude;//经度
    public String waterStoreLatitude;//纬度
    public String businessLicense;//营业执照照片url

}
