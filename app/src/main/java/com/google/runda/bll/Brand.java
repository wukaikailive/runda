package com.google.runda.bll;

import android.util.Log;

import com.google.runda.event.PullBrandsFailEvent;
import com.google.runda.event.PullBrandsSucceedEvent;
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
public class Brand {

    /*加载品牌信息 - - 开始*/

    public String getBrandsJson() throws IOException {
        return HttpUtil.get(ServerConfig.WAY_BRAND);
    }

    PullBrandsThread pullBrandsThread =null;
    public void beginPullBrands(){
        synchronized (this){
            if(pullBrandsThread !=null){
                return ;
            }
            pullBrandsThread =new PullBrandsThread();
            pullBrandsThread.start();
        }
    }
    class PullBrandsThread extends Thread{

        @Override
        public void run() {
            String jsonString="";
            try {
                jsonString = getBrandsJson();
                Log.e("brands jsonString", "" + jsonString);
                Map<String,String> result= JsonHelper.toMap(jsonString);
                String code=result.get("code");
                if(code.equals("200")){
                    List<com.google.runda.model.Brand> brands=JsonHelper.toBrandList(result.get("data"));
                    EventBus.getDefault().post(new PullBrandsSucceedEvent(brands));
                }
                else{
                    EventBus.getDefault().post(new PullBrandsFailEvent(result.get("message")));
                }
            } catch (JSONException e) {
                EventBus.getDefault().post(new PullBrandsFailEvent("数据解析错误 "+jsonString));
                e.printStackTrace();
            }catch (Exception e){
                EventBus.getDefault().post(new PullBrandsFailEvent(e.getLocalizedMessage()));
                e.printStackTrace();
            }
            synchronized (Brand.this){
                pullBrandsThread =null;
            }
        }
    }

    /*加载品牌信息 - - 结束*/
}
