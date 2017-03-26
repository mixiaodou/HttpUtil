package com.text.httputil.httpclient;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.w3c.dom.Entity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Zhdf on 2017/3/25.
 */

public class HttpClientutil {
    public static String doGet(String urlPath) {
        String result = null;
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet httpRequst = new HttpGet(urlPath);
        //设置 头部
        ////设置header,也可通过entity 参考doPost
        //httpRequst.setHeader("key", "value");
        try {
            //开始请求
            HttpResponse httpResponse = client.execute(httpRequst);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                //获取 响应实体数据
                result = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
                //删除多余的字符
                result.replaceAll("\r", "");
            } else {
                //中止请求
                httpRequst.abort();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String doPost(String urlPath) {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost httpRequest = new HttpPost(urlPath);
        String result = null;
        try {
            //设置请求参数
            //设置header,也可通过entity
            //httpRequest.setHeader("key", "value");
            //常用的是 key value,json形式
            JSONObject jsonObject = new JSONObject();
            StringEntity entity = new StringEntity(jsonObject.toString());
            entity.setContentEncoding("utf-8");
            //setContentType("application/json");//发送json数据需要设置contentType
            entity.setContentType("application/json");
            httpRequest.setEntity(entity);
            //开始请求
            HttpResponse httpResponse = client.execute(httpRequest);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(httpResponse.getEntity(),"utf-8");
                //删除多余的字符
                result.replaceAll("\r", "");
            } else {
                httpRequest.abort();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
