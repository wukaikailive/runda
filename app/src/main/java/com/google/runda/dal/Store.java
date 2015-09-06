package com.google.runda.dal;

import android.util.Log;

import com.google.runda.staticModel.ServerConfig;
import com.google.runda.util.CustomHttpClient;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wukai on 2015/5/5.
 */
public class Store {
    public static HttpClient httpClient = CustomHttpClient.getHttpClient();


    /**
     * 提交
     * @param url
     * @param rawParams
     * @return
     * @throws Exception
     */
    public String post(String url, Map<String, String> rawParams)throws IOException {
        HttpPost post = new HttpPost(url);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        if (rawParams != null) {
            for (String key : rawParams.keySet()) {
                params.add(new BasicNameValuePair(key, rawParams.get(key)));
            }
        }
        post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
        if(null!= ServerConfig.PHPSESSID || (!ServerConfig.PHPSESSID.equals(""))){
            //为了与服务端的session交互，将SESSIONID发给服务器
            post.setHeader("Cookie", "PHPSESSID=" + ServerConfig.PHPSESSID);
        }
        post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        HttpResponse httpResponse = httpClient.execute(post);
        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK/*200*/) {
            String result = EntityUtils.toString(httpResponse.getEntity());
            return result;
        }

        return null;
    }

    public String get(String url) throws IOException {
        HttpGet get=new HttpGet(url);
        if(null!= ServerConfig.PHPSESSID || (!ServerConfig.PHPSESSID.equals(""))){
            //为了与服务端的session交互，将SESSIONID发给服务器
            get.setHeader("Cookie", "PHPSESSID=" + ServerConfig.PHPSESSID);
        }
        get.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        HttpResponse httpResponse = httpClient.execute(get);
        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK/*200*/) {
            String result = EntityUtils.toString(httpResponse.getEntity());
            return result;
        }
        return null;
    }
}
