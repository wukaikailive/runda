package com.google.runda.bll;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.runda.event.IOExceptionEvent;
import com.google.runda.event.PullCitiesSucceedEvent;
import com.google.runda.event.PullCountiesSucceedEvent;
import com.google.runda.event.PullProvincesSucceedEvent;
import com.google.runda.model.Province;
import com.google.runda.staticModel.ServerConfig;
import com.google.runda.util.JsonHelper;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by wukai on 2015/4/25.
 */
public class Region {
    private com.google.runda.dal.Region dal = new com.google.runda.dal.Region();

    //获取省份json
    public String getProvinces() throws IOException {
        return dal.get(ServerConfig.WAY_PROVINCE);
    }

    public List<Province> toProvinceList(String jsonString){
        Gson gson=new Gson();
        try{
            List<Province> list=gson.fromJson(jsonString,new TypeToken<List<Province>>(){}.getType());
            return list;
        }catch (com.google.gson.JsonSyntaxException e){
            e.printStackTrace();
        }
        return null;
    }
    //获取市json
    public String getCities(String provinceID) throws IOException {
        return dal.get(ServerConfig.WAY_CITY + provinceID);
    }
    //获取县\区json
    public String getCounties(String cityID) throws IOException {
        return dal.get(ServerConfig.WAY_COUNTY + cityID);
    }


    //省
    PullProvincesThread pullProvincesThread=null;
    public void beginPullProvinces(){
        synchronized (this){
            if(pullProvincesThread!=null){
                return;
            }
            pullProvincesThread=new PullProvincesThread();
            pullProvincesThread.start();
        }
    }
    class PullProvincesThread extends Thread{
        @Override
        public void run() {
            try{
                String jsonProvinces=getProvinces();
                Map<String,String> result=JsonHelper.toMap(jsonProvinces);
                Log.e("wukaikai",""+result);
                if(result.get("code").equals("200")){
                    List<Province> list=JsonHelper.toBeanList(result.get("data"));
                    Log.e("provinces",list.toArray().toString()+"");
                    EventBus.getDefault().post(new PullProvincesSucceedEvent(list));
                }
            }catch (IOException e){
                EventBus.getDefault().post(new IOExceptionEvent(e.getLocalizedMessage()));
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            synchronized (Region.this){
                pullProvincesThread=null;
            }
        }
    }

    //市
    PullCitiesThread pullCitiesThread=null;
    public void beginPullCities(String provinceID){
        synchronized (this){
            if(pullCitiesThread==null){
                pullCitiesThread=new PullCitiesThread();
                pullCitiesThread.provinceID=provinceID;
                pullCitiesThread.start();
            }
        }
    }
    class PullCitiesThread extends Thread{
        public String provinceID;
        @Override
        public void run() {
            try{
                String jsonCities=getCities(provinceID);
                Map<String,String> result=JsonHelper.toMap(jsonCities);
                if(result.get("code").equals("200")){
                    Map<String,String> cities= JsonHelper.toMap(result.get("data"));
                    EventBus.getDefault().post(new PullCitiesSucceedEvent(cities));
                }
            }catch (Exception e){

            }
            synchronized (Region.this){
                pullCitiesThread=null;
            }
        }
    }

    //县、区
    PullCountiesThread pullCountiesThread=null;
    public void beginPullCounties(String provinceID){
        synchronized (this){
            if(pullCountiesThread==null){
                pullCountiesThread=new PullCountiesThread();
                pullCountiesThread.provinceID=provinceID;
                pullCountiesThread.start();
            }
        }
    }
    class PullCountiesThread extends Thread{
        public String provinceID;
        @Override
        public void run() {
            try{
                String jsonCounties=getCounties(provinceID);
                Map<String,String> result=JsonHelper.toMap(jsonCounties);
                if(result.get("code").equals("200")){
                    Map<String,String> counties= JsonHelper.toMap(result.get("data"));
                    EventBus.getDefault().post(new PullCountiesSucceedEvent(counties));
                }
            }catch (Exception e){

            }
            synchronized (Region.this){
                pullCountiesThread=null;
            }
        }
    }
}
