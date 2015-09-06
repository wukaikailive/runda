package com.google.runda.model;
//用户表
public class User {

	public int ID;
	public String Name; //用户名
	public String Password;//密码
	public String NickName;//昵称
	public String CellPhone;//手机号码
	public String Email;//邮箱
	public String RealName;//真实姓名
	public String IDCardUrl;//身份证url
	public String IDCardNumber;//身份证编码
	public String Sex;
    public String HeadImgUrl;
    public String CheckCode;

	public int RoleID;//用户角色id
	public boolean IsLock;//是否被锁定
	public int ProvinceID;
    public String Province;
	public int CityID;
    public String City;
	public int CountyID;
    public String County;
	public String DetailAddress;//详细地址
}
