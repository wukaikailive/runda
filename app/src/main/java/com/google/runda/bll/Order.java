package com.google.runda.bll;

import android.util.Log;

import com.google.runda.event.PlaceOrderFailEvent;
import com.google.runda.event.PlaceOrderSucceedEvent;
import com.google.runda.event.PullOrdersFailEvent;
import com.google.runda.event.PullOrdersSucceedEvent;
import com.google.runda.model.PlaceOrderFormModel;
import com.google.runda.staticModel.ServerConfig;
import com.google.runda.util.BeanHelper;
import com.google.runda.util.HttpUtil;
import com.google.runda.util.JsonHelper;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by bigface on 2015/9/10.
 */
public class Order {


    public String getUnfinishedOrdersJson() throws IOException {
        return HttpUtil.get(ServerConfig.WAY_UNFINISHED_ORDER);
    }
    PullUnfinishedOrdersThread pullUnfinishedOrdersThread =null;

    public void beginPullUnfinishedOrders(){
        synchronized (this){
            if(pullUnfinishedOrdersThread !=null){
                return ;
            }
            pullUnfinishedOrdersThread =new PullUnfinishedOrdersThread();
            pullUnfinishedOrdersThread.start();
        }
    }
    class PullUnfinishedOrdersThread extends Thread{

        @Override
        public void run() {
            String jsonString="";
            try {
                //todo 需加上参数
                jsonString = getUnfinishedOrdersJson();
                Log.e("orders jsonString", "" + jsonString);
                Map<String,String> result= JsonHelper.toMap(jsonString);
                String code=result.get("code");
                if(code.equals("200")){
                    List<com.google.runda.model.Order> orders=JsonHelper.toOrderList(result.get("data"));
                    EventBus.getDefault().post(new PullOrdersSucceedEvent(orders));
                }
                else{
                    EventBus.getDefault().post(new PullOrdersFailEvent(result.get("message")));
                }
            } catch (JSONException e) {
                EventBus.getDefault().post(new PullOrdersFailEvent("数据解析错误 "+jsonString));
                e.printStackTrace();
            }catch (Exception e){
                EventBus.getDefault().post(new PullOrdersFailEvent(e.getLocalizedMessage()));
                e.printStackTrace();
            }
            synchronized (Order.this){
                pullUnfinishedOrdersThread =null;
            }
        }
    }

    public String getPlaceOrderJson(com.google.runda.model.PlaceOrderFormModel model) throws IllegalAccessException, IOException {
        Map<String,String> rawParams = BeanHelper.objToHash(model);
        return HttpUtil.post(ServerConfig.WAY_PLACE_ORDER, rawParams);
    }

    public void beginPlaceOrder(PlaceOrderFormModel model){

        synchronized (this){
            if(placeOrderThread !=null){
                return ;
            }
            placeOrderThread =new PlaceOrderThread();
            placeOrderThread.model=model;
            placeOrderThread.start();
        }
    }
    PlaceOrderThread placeOrderThread=null;

    class PlaceOrderThread extends Thread{
        public PlaceOrderFormModel model;
        @Override
        public void run() {
            String jsonString="";
            try {
                jsonString = getPlaceOrderJson(model);
                Log.e("orders jsonString", "" + jsonString);
                Map<String,String> result= JsonHelper.toMap(jsonString);
                String code=result.get("code");
                if(code.equals("200")){
                    Map<String,String> data=JsonHelper.toMap(result.get("data"));
                    String orderID=data.get("orderID");
                    EventBus.getDefault().post(new PlaceOrderSucceedEvent(orderID));
                }
                else{
                    EventBus.getDefault().post(new PlaceOrderFailEvent(result.get("message")));
                }
            } catch (JSONException e) {
                EventBus.getDefault().post(new PlaceOrderFailEvent("数据解析错误 "+jsonString));
                e.printStackTrace();
            }catch (Exception e){
                EventBus.getDefault().post(new PlaceOrderFailEvent(e.getLocalizedMessage()));
                e.printStackTrace();
            }
            synchronized (Order.this){
                placeOrderThread =null;
            }
        }
    }

}
