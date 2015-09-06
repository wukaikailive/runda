package com.google.runda.util;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
//自定义http客户端，这个类配置了一些客户端需要的属性
public class CustomHttpClient {

	private static HttpClient customHttpClient;
	
	private CustomHttpClient(){}
	
	public static synchronized HttpClient getHttpClient()
	{
		if(customHttpClient == null)
		{
			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	        HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
	        HttpProtocolParams.setUseExpectContinue(params, true);
	        
	        ConnManagerParams.setTimeout(params, 60000);

	        HttpConnectionParams.setConnectionTimeout(params, 60000);
	        HttpConnectionParams.setSoTimeout(params, 60000);
	        
	        SchemeRegistry schReg = new SchemeRegistry();
	        schReg.register(new Scheme("http", 
	                        PlainSocketFactory.getSocketFactory(), 80));
	        schReg.register(new Scheme("https", 
	                        SSLSocketFactory.getSocketFactory(), 443));
	        ClientConnectionManager conMgr = new 
	                        ThreadSafeClientConnManager(params,schReg);
	        
	        customHttpClient = new DefaultHttpClient(conMgr, params);
		}
		return customHttpClient;
	}
}

