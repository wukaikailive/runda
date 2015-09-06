package com.google.runda.dal;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * Created by pinipala on 2015/3/26.
 */
public class Config {


    public static String FILEPATH="/data/data/com.google.runda/files/";

    //加载配置文件 在assets文件目录下
    public Properties loadConfig(String file) {
        Properties properties = new Properties();
        try {
            File f=new File(FILEPATH);
            if(!f.exists()){
                f.mkdir();
            }
            File f1=new File(FILEPATH+file);
            if(!f1.exists()){
                f1.createNewFile();
            }
           // Log.e("loadConfig",context.getAssets().get)
            FileInputStream s = new FileInputStream(FILEPATH+file);
            properties.load(s);
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return properties;
    }

    //写配置文件 在assets文件目录下
    public void saveConfig(String file, Properties properties) {
        try {
            Log.e("wukaikai saveconfig", FILEPATH);
            FileOutputStream s = new FileOutputStream(FILEPATH+file, false);
            //properties.
           // FileOutputStream s
            properties.store(s, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
