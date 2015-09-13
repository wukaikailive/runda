package com.google.runda.staticModel;

import com.google.runda.model.Brand;
import com.google.runda.model.Category;
import com.google.runda.model.User;

import java.util.List;

/**
 * Created by wukai on 2015/4/16.
 */
public class ServerConfig {
    /*服务器地址*/
    //public static final String WAY_ROOT ="http://192.168.1.155:8080";
  //  public static final String WAY_ROOT ="http://ifuxs.cn";
    public static final String WAY_ROOT="http://192.168.1.3:8080";
    /*注册地址*/
    public static final String WAY_USER_REGISTER=WAY_ROOT+"/"+"index.php?controller=Home&method=registerProc";

    /*使用用户名登陆地址*/
    public static final String WAY_LOGIN_ROUTE_USERNAME = WAY_ROOT +"/"+"index.php?controller=Home&method=loginProc&tokenType=userName";

    /*使用手机号登录地址*/
    public static final String WAY_LOGIN_ROUTE_PHONE = WAY_ROOT +"/"+"index.php?controller=Home&method=loginProc&tokenType=phone";

    /*拉取验证码地址 返回结果是位图*/
    public static final String WAY_CHECK_CODE = WAY_ROOT +"/"+"index.php?controller=Home&method=getCode";

    /*获取用户信息地址*/
    public static final String WAY_USER_INFO = WAY_ROOT +"/"+"index.php?controller=Home&method=myInformationPhone";

    /*获取字符串验证码 用于开启一个会话*/
    public static final String WAY_CHECK_CODE_STRING=WAY_ROOT +"/"+"index.php?controller=Home&method=getCodeString";

    /*验证手机号 后接手机号码*/
    public static final String WAY_CHECK_PHONE=WAY_ROOT+"/"+"index.php?controller=Home&method=checkPhoneNumber&phoneNumber=";

    /*验证验证码 后接验证码*/
    public static final String WAY_CHECK_CHECKCODE=WAY_ROOT+"/"+"index.php?controller=Home&method=checkCode&checkCode=";

    /*获取水站信息 默认为6 get*/
    public static final String WAY_WATER_STORE_LIST_DEFAULT=WAY_ROOT+"/"+"index.php?controller=RunDa&method=getNearbyWaterStore";

    /*获取水站信息 后接数量 get*/
    public static final String WAY_WATER_STORE_LIST=WAY_ROOT+"/"+"index.php?controller=RunDa&method=getNearbyWaterStore&count=";

    /*获取省份集合*/
    public static final String WAY_PROVINCE=WAY_ROOT+"/"+"index.php?controller=Region&method=getProvincesJson";

    /*获取市集合，后接省份ID*/
    public static final String WAY_CITY=WAY_ROOT+"/"+"index.php?controller=Region&method=getCitiesJson&provinceID=";

    /*获取县\区集合，后接市ID*/
    public static final String WAY_COUNTY=WAY_ROOT+"/"+"index.php?controller=Region&method=getCountriesJson&cityID=";

    /*服务器返回的会话ID，用以维持会话*/
    public static String PHPSESSID="";

    /*用户的信息暂存于此*/
    public static User USER=new User();


//water
    /*获取桶装水分类 get*/
    public static final String WAY_CATEGORY=WAY_ROOT+"/"+"index.php?controller=WaterStore&method=getBarrelWaterCategory";

    /*获取桶装水品牌 get*/
    public static final String WAY_BRAND=WAY_ROOT+"/"+"index.php?controller=WaterStore&method=getBarrelWaterBrand";

    /*根据水站id获取其已经上架的水的信息 后接水站id get*/
    public static final String WAY_WATER_BASIC=WAY_ROOT+"/"+"index.php?controller=WaterStore&method=getAllBarrelWaterGoodsOfOneWaterStore&id=";

    /*根据水id获取其详情 后接水id get*/
    public static final String WAY_WATER_DETAIL=WAY_ROOT+"/"+"index.php?controller=RunDa&method=getBarrelWaterGoodsDetailByID&barrelWaterGoodsID=";


    //以下两个几个必须先于

    /*品牌集合*/
    public static List<Brand> BRANDS;
    /*分类集合*/
    public static List<Category> CATEGORIES;

//water end


//feedback
    /*提交反馈的地址*/
    public static final String WAY_SUBMIT_FEEDBACK=WAY_ROOT+"/"+"index.php?controller=FeedBack&method=addFeedBack";
//feedback end
}
