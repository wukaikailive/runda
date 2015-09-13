package com.google.runda.bll;

import android.util.Log;

import com.google.runda.event.AddFeedBackFailEvent;
import com.google.runda.event.AddFeedBackSucceedEvent;
import com.google.runda.event.PullBrandsFailEvent;
import com.google.runda.model.*;
import com.google.runda.staticModel.ServerConfig;
import com.google.runda.util.HttpUtil;
import com.google.runda.util.JsonHelper;

import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by bigface on 2015/7/25.
 */
public class FeedBack {

    public String addFeedBack(com.google.runda.model.FeedBack model) throws IOException {
        Map<String,String> map=new HashMap<String, String>();
        map.put("type",String.valueOf(model.type == true ? 1 : 0));
        map.put("content",model.content);
        map.put("email",model.email);
        return HttpUtil.post(ServerConfig.WAY_SUBMIT_FEEDBACK,map);
    }

    public void beginAddFeedBack(com.google.runda.model.FeedBack model){
        synchronized (this){
            if(addFeedBackThread !=null){
                return ;
            }
            addFeedBackThread =new AddFeedBackThread();
            addFeedBackThread.model=model;
            addFeedBackThread.start();
        }
    }
    AddFeedBackThread addFeedBackThread=null;
    class AddFeedBackThread extends Thread{
        public com.google.runda.model.FeedBack model;

        @Override
        public void run() {
            String jsonString="";
            try{
                jsonString=addFeedBack(model);
                Log.e("feedback jsonString", "" + jsonString);
                Map<String,String> result= JsonHelper.toMap(jsonString);
                String code=result.get("code");
                if(code.equals("200")){
                    EventBus.getDefault().post(new AddFeedBackSucceedEvent());
                }
                else{
                    EventBus.getDefault().post(new AddFeedBackFailEvent(result.get("message")));
                }
            }catch (JSONException e) {
                EventBus.getDefault().post(new AddFeedBackFailEvent("数据解析错误 "+jsonString));
                e.printStackTrace();
            }catch (Exception e){
                EventBus.getDefault().post(new AddFeedBackFailEvent(e.getLocalizedMessage()));
                e.printStackTrace();
            }
            synchronized (FeedBack.this){
                addFeedBackThread=null;
            }
        }
    }
}
