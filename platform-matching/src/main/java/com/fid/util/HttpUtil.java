package com.fid.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Component
public class HttpUtil {
	public JSONObject postUrlWithJsonString(String url, String jsonString){
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);

		JSONObject retJson = null;

		try {
			StringEntity reqEntity = new StringEntity(jsonString, "utf-8");
			reqEntity.setContentEncoding("UTF-8");    
			reqEntity.setContentType("application/json");   
			httpPost.setEntity(reqEntity);
			CloseableHttpResponse response = httpclient.execute(httpPost);
			// 回去返回实体
			retJson = JSONObject.parseObject(EntityUtils.toString(response.getEntity()));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return retJson;
	}
	
	public JSONObject postUrlWithJson(String url, JSONObject json){
		return postUrlWithJsonString(url,json.toString());
	}

	public String postUrlRetString(String url, HashMap<String, String> hm) {
		String msg = "请求异常";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		if(hm != null){
			Iterator<Map.Entry<String, String>> iterator = hm.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, String> entry = iterator.next();
				params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
		}
		try {
			HttpClient client = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			HttpResponse response = client.execute(httpPost);
			msg = "推送返回的结果串:" + EntityUtils.toString(response.getEntity());
			//System.out.println(msg);
			return msg;
		} catch (Exception e) {
			e.printStackTrace();
			return msg;
		}
	}
	
	public JSONObject postUrlRetJson(String url, HashMap<String, String> hm) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		JSONObject retJson = null;
		if(hm != null){
			Iterator<Map.Entry<String, String>> iterator = hm.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, String> entry = iterator.next();
				params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
		}
		try {
			HttpClient client = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			HttpResponse response = client.execute(httpPost);
			retJson = JSONObject.parseObject(EntityUtils.toString(response.getEntity()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return retJson;
	}

	public JSONArray sendUrlRetJsonArray(String url, HashMap<String, String> hm) {
		JSONArray retJsonArray = null;
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		if(hm != null){
			Iterator<Map.Entry<String, String>> iterator = hm.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, String> entry = iterator.next();
				params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
		}
		try {
			HttpClient client = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			HttpResponse response = client.execute(httpPost);
			return JSONArray.parseArray(EntityUtils.toString(response.getEntity()));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

}
