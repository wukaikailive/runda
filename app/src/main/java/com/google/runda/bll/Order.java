package com.google.runda.bll;

import android.util.Log;

import com.google.runda.event.PullBrandsFailEvent;
import com.google.runda.event.PullBrandsSucceedEvent;
import com.google.runda.event.PullOrdersFailEvent;
import com.google.runda.event.PullOrdersSucceedEvent;
import com.google.runda.util.HttpUtil;
import com.google.runda.util.JsonHelper;

import org.json.JSONException;

import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by bigface on 2015/9/10.
 */
public class Order {

    public String getOrdersJson(){
        return "{\n" +
                "    \"code\": \"200\",\n" +
                "    \"message\": \"获取数据成功\",\n" +
                "    \"data\": [\n" +
                "        {\n" +
                "            \"Id\": \"1\",\n" +
                "            \"StoreId\": \"1\",\n" +
                "            \"WaterStoreName\": \"顺丰之水\",\n" +
                "            \"DeliverymanID\": \"10001\",\n" +
                "            \"SubmitterID\": \"10002\",\n" +
                "            \"ReceiverName\": \"测试员1\",\n" +
                "            \"ReceiverCellPhone\": \"18798874377\",\n" +
                "            \"ReceiverTime\": \"2015-9-11\",\n" +
                "            \"Comment\": \"快些送\",\n" +
                "            \"CommodityID\": \"1\",\n" +
                "            \"Quantity\": 2,\n" +
                "            \"Price\": 9,\n" +
                "            \"TotalPrice\": 18,\n" +
                "            \"Description\": \"天上之水，买满60还可以来本店抽奖\",\n" +
                "            \"SubmitTime\": \"2015-9-10\",\n" +
                "            \"Status\": 1,\n" +
                "            \"FailureComment\": \"\",\n" +
                "            \"ProvinceName\": \"贵州省\",\n" +
                "            \"CityName\": \"贵阳市\",\n" +
                "            \"CountryName\": \"花溪区\",\n" +
                "            \"DetailAddress\": \"贵州大学北校区\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"Id\": \"2\",\n" +
                "            \"StoreId\": \"1\",\n" +
                "            \"WaterStoreName\": \"顺丰之水\",\n" +
                "            \"DeliverymanID\": \"10001\",\n" +
                "            \"SubmitterID\": \"10002\",\n" +
                "            \"ReceiverName\": \"测试员1\",\n" +
                "            \"ReceiverCellPhone\": \"18798874377\",\n" +
                "            \"ReceiverTime\": \"2015-9-11\",\n" +
                "            \"Comment\": \"快些送\",\n" +
                "            \"Quantity\": 2,\n" +
                "            \"Price\": 9,\n" +
                "            \"TotalPrice\": 18,\n" +
                "            \"SubmitTime\": \"2015-9-10\",\n" +
                "            \"Status\": 1,\n" +
                "            \"FailureComment\": \"\",\n" +
                "            \"ProvinceName\": \"贵州省\",\n" +
                "            \"CityName\": \"贵阳市\",\n" +
                "            \"CountryName\": \"花溪区\",\n" +
                "            \"DetailAddress\": \"贵州大学北校区\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
    }
    PullOrdersThread pullOrdersThread=null;

    public void beginPullOrders(){
        synchronized (this){
            if(pullOrdersThread !=null){
                return ;
            }
            pullOrdersThread =new PullOrdersThread();
            pullOrdersThread.start();
        }
    }
    class PullOrdersThread extends Thread{

        @Override
        public void run() {
            String jsonString="";
            try {
                //todo 需加上参数
                jsonString = getOrdersJson();
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
                pullOrdersThread =null;
            }
        }
    }
}
