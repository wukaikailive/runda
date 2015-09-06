package com.google.runda.bll;

import android.util.Log;

import com.google.runda.event.PullCategoriesFailEvent;
import com.google.runda.event.PullCategoriesSucceedEvent;
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
public class Category {

    /*加载类别信息 - - 开始*/

    public String getCategoriesJson() throws IOException {
        return HttpUtil.get(ServerConfig.WAY_CATEGORY);
    }

    PullCategoriesThread pullCategoriesThread =null;
    public void beginPullCategories(){
        synchronized (this){
            if(pullCategoriesThread !=null){
                return ;
            }
            pullCategoriesThread =new PullCategoriesThread();
            pullCategoriesThread.start();
        }
    }
    class PullCategoriesThread extends Thread{

        @Override
        public void run() {
            String jsonString="";
            try {
                jsonString = getCategoriesJson();
                Log.e("Categories jsonString", "" + jsonString);
                Map<String,String> result= JsonHelper.toMap(jsonString);
                String code=result.get("code");
                if(code.equals("200")){
                    List<com.google.runda.model.Category> categories=JsonHelper.toCategoryList(result.get("data"));
                    EventBus.getDefault().post(new PullCategoriesSucceedEvent(categories));
                }
                else{
                    EventBus.getDefault().post(new PullCategoriesFailEvent(result.get("message")));
                }
            } catch (JSONException e) {
                EventBus.getDefault().post(new PullCategoriesFailEvent("数据解析错误 "+jsonString));
                e.printStackTrace();
            }catch (Exception e){
                EventBus.getDefault().post(new PullCategoriesFailEvent(e.getLocalizedMessage()));
                e.printStackTrace();
            }
            synchronized (Category.this){
                pullCategoriesThread =null;
            }
        }
    }

    /*加载类别信息 - - 结束*/
}
