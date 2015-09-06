package com.google.runda.bll;

import android.util.Log;

import com.google.runda.event.PullStoresFailEvent;
import com.google.runda.event.PullStoresSucceedEvent;
import com.google.runda.staticModel.ServerConfig;
import com.google.runda.util.JsonHelper;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by wukai on 2015/5/5.
 */
public class Store {

    private com.google.runda.dal.Store dal=new com.google.runda.dal.Store();

    /*加载水站列表 - - 开始*/

    public String pullStoresJson(int size) throws IOException {
        return dal.get(ServerConfig.WAY_WATER_STORE_LIST+size);
    }
    pullStoresThread pullStoresThread =null;
    public void beginPullStores(int size){
        synchronized (this){
            if(pullStoresThread!=null){
                return ;
            }
            pullStoresThread=new pullStoresThread();
            pullStoresThread.size=size;
            pullStoresThread.start();
        }
    }
    class pullStoresThread extends Thread{
        public int size;

        @Override
        public void run() {
            String jsonString="";
            try {
                jsonString = pullStoresJson(size);
                Log.e("stores jsonString", "" + jsonString);
                Map<String,String> result= JsonHelper.toMap(jsonString);
                String code=result.get("code");
                if(code.equals("200")){
                    List<com.google.runda.model.Store> stores=JsonHelper.toStoreList(result.get("data"));
                    EventBus.getDefault().post(new PullStoresSucceedEvent(stores));
                }
                else{
                    EventBus.getDefault().post(new PullStoresFailEvent(result.get("message")));
                }
            } catch (JSONException e) {
                EventBus.getDefault().post(new PullStoresFailEvent("数据解析错误 "+jsonString));
                e.printStackTrace();
            }catch (Exception e){
                EventBus.getDefault().post(new PullStoresFailEvent(e.getLocalizedMessage()));
                e.printStackTrace();
            }
            synchronized (Store.this){
                pullStoresThread =null;
            }
        }
    }
   /*加载水站列表 - - 结束*/
}
