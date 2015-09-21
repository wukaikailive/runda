package com.google.runda.dal;

import android.util.Log;

import com.google.runda.staticModel.ServerConfig;
import com.google.runda.util.CustomHttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by wukai on 2015/4/25.
 */
public class Region {
    public static HttpClient httpClient = CustomHttpClient.getHttpClient();
    public String get(String url) throws IOException {
        HttpGet get=new HttpGet(url);
        if(!(null== ServerConfig.PHPSESSID || ServerConfig.PHPSESSID.equals(""))){
            //为了与服务端的session交互，将SESSIONID发给服务器
            get.setHeader("Cookie", "PHPSESSID=" + ServerConfig.PHPSESSID);
        }
        get.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        HttpResponse httpResponse = httpClient.execute(get);
        Log.e("dal.user/post.code", httpResponse.getStatusLine().getStatusCode() + "");
        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK/*200*/) {
            String result = EntityUtils.toString(httpResponse.getEntity());
            return result;
        }
        return null;
    }
}
