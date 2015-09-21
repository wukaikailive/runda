package com.google.runda.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.runda.staticModel.ServerConfig;

/**
 * Http工具类
 *
 * @author william 利用HttpClient获取指定url返回的内容
 */
public class HttpUtil {
	public static HttpClient httpClient = CustomHttpClient.getHttpClient();

	/**
	 * @param url
	 * @return jsonString
	 * @throws Exception
	 *             get 请求
	 */
	public static String getRequest(String url) throws Exception {
		HttpGet get = new HttpGet(url);
		HttpResponse httpResponse = httpClient.execute(get);
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			String result = EntityUtils.toString(httpResponse.getEntity());
			return result;
		}
		return null;
	}

	/**
	 * @param url
	 * @return jsonString
	 * @throws Exception
	 *             post 请求
	 */
	public static String postRequest(String url, Map<String, String> rawParams) throws Exception {
		HttpPost post = new HttpPost(url);

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		if (rawParams != null) {
			for (String key : rawParams.keySet()) {
				params.add(new BasicNameValuePair(key, rawParams.get(key)));
			}
		}
		post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		HttpResponse httpResponse = httpClient.execute(post);

		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			String result = EntityUtils.toString(httpResponse.getEntity());
			return result;
		}

		return null;
	}

	public static final int OK = 0;
	public static final int FAILED = 1;

	// 异步获取数据
	public static void getRequest(final Handler handler, final String url) {

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				// Looper.prepare();
				Message msg = new Message();
				msg.what = FAILED;
				try {
					HttpGet get = new HttpGet(url);
					HttpResponse httpResponse = httpClient.execute(get);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						msg.what = OK;
						msg.obj = EntityUtils.toString(httpResponse.getEntity());
					}
				} catch (Exception e) {
					msg.what = FAILED;
					e.printStackTrace();
				}
				handler.sendMessage(msg);
				// Looper.loop();
			}
		});

		thread.start();
	}

	//根据url获取位图
	public static Bitmap
	getBitmap(String path,int dstWidth,int dstHeight) throws IOException {
        HttpGet get=new HttpGet(path);
        if(!(null== ServerConfig.PHPSESSID || ServerConfig.PHPSESSID.equals(""))){
            //为了与服务端的session交互，将SESSIONID发给服务器
            get.setHeader("Cookie", "PHPSESSID=" + ServerConfig.PHPSESSID);
        }
        //get.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        DefaultHttpClient client=new DefaultHttpClient();
        HttpResponse response=client.execute(get);
        if(response.getStatusLine().getStatusCode()== HttpStatus.SC_OK){
            HttpEntity entity=response.getEntity();
            InputStream is=entity.getContent();
            Bitmap bitmap=BitmapFactory.decodeStream(is);
            is.close();
            //得到服务端Cookie
            if((null == ServerConfig.PHPSESSID || (ServerConfig.PHPSESSID.equals("")))){
                //如果为空那么将session写入缓存
                List<Cookie>cookies=client.getCookieStore().getCookies();
                for(int i=0;i<cookies.size();i++){
                    //取得Cookie['JSESSIONID']的值存在静态变量中
                    if("PHPSESSID".equals(cookies.get(i).getName())){
                        ServerConfig.PHPSESSID=cookies.get(i).getValue();
                        break;
                    }
                }
            }
            Log.e("PHPSESSID_checkcode", ServerConfig.PHPSESSID);
            return bitmap;
        }
        return null;
//		URL url = new URL(path);
//		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//		conn.setConnectTimeout(5000);
//		conn.setRequestMethod("GET");
//		if (conn.getResponseCode() == 200) {
//
//            InputStream inputStream = conn.getInputStream();
//			//解码得到位图
//			Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//			conn.disconnect();
//
//			if (dstWidth<2 || dstHeight<2)//如果没有给出合理的目的宽度和高度，默认不缩放
//				return bitmap;
//			//缩放到指定大小
//			Bitmap dstBmp =	Bitmap .createScaledBitmap(bitmap, dstWidth, dstHeight, false);
//			//回收释放大图
//			//bitmap.recycle();
//			//返回指定大小的图
//			return dstBmp;
//		}
//		return null;
	}

    /**
     * 提交
     * @param url
     * @param rawParams
     * @return
     * @throws Exception
     */
    public static String post(String url, Map<String, String> rawParams)throws IOException {
        HttpPost post = new HttpPost(url);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        if (rawParams != null) {
            for (String key : rawParams.keySet()) {
                params.add(new BasicNameValuePair(key, rawParams.get(key)));
            }
        }
        post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		if(!(null== ServerConfig.PHPSESSID || ServerConfig.PHPSESSID.equals(""))){
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

    public static String get(String url) throws IOException {
		HttpGet get=new HttpGet(url);
		if(!(null== ServerConfig.PHPSESSID || ServerConfig.PHPSESSID.equals(""))){
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
	public static String getWithNoSession(String url) throws IOException {
		HttpGet get=new HttpGet(url);
		get.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		HttpResponse httpResponse = httpClient.execute(get);
		if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK/*200*/) {
			String result = EntityUtils.toString(httpResponse.getEntity());
			return result;
		}
		return null;
	}

}
