package com.google.runda.bll;

import android.util.Log;

import com.google.gson.JsonSyntaxException;
import com.google.runda.event.PullCommoditiesBasicFailEvent;
import com.google.runda.event.PullCommoditiesBasicSucceedEvent;
import com.google.runda.event.PullCommodityDetailFailEvent;
import com.google.runda.event.PullCommodityDetailSucceedEvent;
import com.google.runda.staticModel.ServerConfig;
import com.google.runda.util.HttpUtil;
import com.google.runda.util.JsonHelper;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by wukai on 2015/5/9.
 */
public class Commodity {

    /*加载水的基本信息 - - 开始*/

    public String getCommoditiesBasicJson(String id) throws IOException {
        return HttpUtil.get(ServerConfig.WAY_WATER_BASIC+id);
    }

    PullCommoditiesBasicThread pullCommoditiesBasicThread =null;
    public void beginPullCommoditiesBasic(String id){
        synchronized (this){
            if(pullCommoditiesBasicThread !=null){
                return ;
            }
            pullCommoditiesBasicThread =new PullCommoditiesBasicThread();
            pullCommoditiesBasicThread.id=id;
            pullCommoditiesBasicThread.start();
        }
    }
    class PullCommoditiesBasicThread extends Thread{
        public String id;
        @Override
        public void run() {
            String jsonString="";
            try {
                jsonString = getCommoditiesBasicJson(id);
                Log.e("Commodities jsonString", "" + jsonString);
                Map<String,String> result= JsonHelper.toMap(jsonString);
                String code=result.get("code");
                if(code.equals("200")){
                    List<com.google.runda.model.Commodity> commodities=JsonHelper.toCommodityList(result.get("data"));
                    EventBus.getDefault().post(new PullCommoditiesBasicSucceedEvent(commodities));
                }
                else{
                    EventBus.getDefault().post(new PullCommoditiesBasicFailEvent(result.get("message")));
                }
            }catch (JsonSyntaxException e){
                EventBus.getDefault().post(new PullCommoditiesBasicFailEvent("数据解析错误 "+jsonString+" "+e.getLocalizedMessage()));
                e.printStackTrace();
            }
            catch (JSONException e) {
                EventBus.getDefault().post(new PullCommoditiesBasicFailEvent("数据解析错误 "+jsonString));
                e.printStackTrace();
            }catch (Exception e){
                EventBus.getDefault().post(new PullCommoditiesBasicFailEvent(e.getLocalizedMessage()));
                e.printStackTrace();
            }
            synchronized (Commodity.this){
                pullCommoditiesBasicThread =null;
            }
        }
    }

    /*加载水的基本信息 - - 结束*/


       /*加载水的基本信息 - - 开始*/

    public String getCommoditiesDetailJson(String id) throws IOException {
        return HttpUtil.get(ServerConfig.WAY_WATER_DETAIL+id);
    }

    PullCommoditiesDetailThread pullCommoditiesDetailThread =null;
    public void beginPullCommoditiesDetail(String id){
        synchronized (this){
            if(pullCommoditiesDetailThread !=null){
                return ;
            }
            pullCommoditiesDetailThread =new PullCommoditiesDetailThread();
            pullCommoditiesDetailThread.id=id;
            pullCommoditiesDetailThread.start();
        }
    }
    class PullCommoditiesDetailThread extends Thread{

        public String id;

        @Override
        public void run() {
            String jsonString="";
            try {
                jsonString = getCommoditiesDetailJson(id);
                Log.e("Commodity jsonString", "" + jsonString);
                Map<String,String> result= JsonHelper.toMap(jsonString);
                String code=result.get("code");
                if(code.equals("200")){
                    com.google.runda.model.Commodity commodity=JsonHelper.toBean(result.get("data"),com.google.runda.model.Commodity.class);
                    Log.e("commodity name"," "+commodity.waterGoodsName);
                    EventBus.getDefault().post(new PullCommodityDetailSucceedEvent(commodity));
                }
                else{
                    EventBus.getDefault().post(new PullCommodityDetailFailEvent(result.get("message")));
                }
            } catch (JSONException e) {
                EventBus.getDefault().post(new PullCommodityDetailFailEvent("数据解析错误 "+jsonString));
                e.printStackTrace();
            }catch (Exception e){
                EventBus.getDefault().post(new PullCommodityDetailFailEvent(e.getLocalizedMessage()));
                e.printStackTrace();
            }
            synchronized (Commodity.this){
                pullCommoditiesDetailThread =null;
            }
        }
    }

    /*加载水的基本信息 - - 结束*/
}
