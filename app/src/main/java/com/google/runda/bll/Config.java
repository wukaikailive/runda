package com.google.runda.bll;

import android.content.Context;
import android.util.Log;

import com.google.runda.model.FontSize;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by pinipala on 2015/3/26.
 */
public class Config {

    public static String CONFIG_FILENAME = "config.properties";
    private com.google.runda.dal.Config dal = new com.google.runda.dal.Config();
    private Properties mProperties;

    //初始化
    public Config() {
        mProperties = loadConfig();
        if (mProperties == null || mProperties.size() == 0) {
                setConfig(getDefaultConfig());
                mProperties = loadConfig();
            }
        }

    //获取某个配置项
    public String getSetting(String name) {
        return mProperties.getProperty(name);
    }

    public void setSetting(String name,String value){
        mProperties.setProperty(name,value);
    }


    //加载配置文件
    public Properties loadConfig() {
        return dal.loadConfig(CONFIG_FILENAME);
    }

    //写入配置
    private void writeConfig(com.google.runda.model.Config config, Properties pro) {
        //放入数据
        pro.setProperty("name", config.userName);
        pro.setProperty("phone",config.phone);
        pro.setProperty("password", config.password);
        pro.setProperty("isAlwaysNeedLogin", String.valueOf(config.isAlwaysNeedLogin));
        pro.setProperty("isAllowAd", String.valueOf(config.isAllowAd));
        pro.setProperty("isAllowAuto", String.valueOf(config.isAllowAuto));
        pro.setProperty("fontSize", String.valueOf(config.fontSize));
        //写入配置文件
        dal.saveConfig(CONFIG_FILENAME, pro);
    }

    public void save(){
        dal.saveConfig(CONFIG_FILENAME,mProperties);
    }

    public com.google.runda.model.Config getConfig(){
        com.google.runda.model.Config config=new com.google.runda.model.Config();
        config.userName=getSetting("name");
        config.phone=getSetting("phone");
        config.password=getSetting("password");
//        config.province=getSetting("province");
//        config.city=getSetting("city");
//        config.country=getSetting("country");
        config.fontSize= Integer.parseInt(getSetting("fontSize"));
        config.isAlwaysNeedLogin= Boolean.parseBoolean(getSetting("isAlwaysNeedLogin"));
        config.isAllowAd= Boolean.parseBoolean(getSetting("isAllowAd"));
        config.isAllowAuto= Boolean.parseBoolean(getSetting("isAllowAuto"));
        return config;
    }

    //恢复默认设置
    public void setConfig(com.google.runda.model.Config config) {
        this.writeConfig(config, new Properties());
    }

    public com.google.runda.model.Config getDefaultConfig(){
        com.google.runda.model.Config config = new com.google.runda.model.Config();

        config.userName = "";//用户名为空
        config.phone="";
        config.password = "";//密码为空
        config.province="";
        config.city="";
        config.country="";
        config.isAlwaysNeedLogin = false;//不总是登陆
        config.isAllowAd = true;//允许广告
        config.isAllowAuto = true;//允许推送订单消息
        config.fontSize = FontSize.middle.GetValue();//中号字体
        return config;
    }

    public void setDefault(){
        com.google.runda.model.Config model=getDefaultConfig();
        setConfig(model);
    }
}
