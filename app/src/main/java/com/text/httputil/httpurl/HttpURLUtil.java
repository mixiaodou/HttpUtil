package com.text.httputil.httpurl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Zhdf on 2017/3/25.
 * 使用 URLConnection
 */

public class HttpURLUtil {
    private static final String url = "";

    /*
    * get 请求，请求参数 在url后面拼接,"?key=value&key=value"
    * */
    public static String doGet(String urlPath) {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        StringBuffer strBuffer = null;
        try {
            URL url = new URL(urlPath);
            connection = (HttpURLConnection) url.openConnection();
            //设置 请求方法,请求超时时间
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            //设置 请求头部(header)
            //Content-Type 内容格式，
            // application/json   JSON数据格式
            // application/xml    XML数据格式
            // multipart/form-data 在表单中进行文件上传时，就需要使用该格式
            connection.setRequestProperty("Content-Type", "application/json");
            // 编码格式
            connection.setRequestProperty("Accept-Charset", "utf-8");
            //开始连接
            connection.connect();
            //200，则说明连接正常
            if (connection.getResponseCode() == 200) {
                //获取响应 实体
                //获取服务器返回的输入流
                inputStream = connection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                strBuffer = new StringBuffer();
                while ((temp = bufferedReader.readLine()) != null) {
                    strBuffer.append(temp);
                }
            } else {
                //请求失败
            }
            //获取 相应的header
            //URLConnection urlConnection = connection;
            //urlConnection.getHeaderFields();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //关流 结束连接
        finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (inputStream != null) {
                    bufferedReader.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return strBuffer.toString();
    }

    /*
    * post请求 请求参数在 请求实体(boday)中
    *  请求参数 常用的是
    *  json结构 params="{}" key value结构 params="key=value&key=value"
    * */
    public static String doPost(String urlPath, String params) {
        HttpURLConnection connection = null;
        BufferedWriter bufferedWriter = null;
        BufferedReader bufferedReader = null;
        StringBuffer stringBuffer = null;
        try {
            URL url = new URL(urlPath);
            connection = (HttpURLConnection) url.openConnection();
            //设置 请求方法,请求超时时间
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            connection.setRequestProperty("Accept-Charset", "utf-8");
            connection.connect();
            //设置 请求参数
            bufferedWriter = new BufferedWriter(
                    new OutputStreamWriter(connection.getOutputStream()));
            bufferedWriter.write(params);
            bufferedWriter.flush();
            if (connection.getResponseCode() == 200) {
                //获取 相应的 实体数据
                bufferedReader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                String temp;
                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuffer.append(temp);
                }
            } else {
                //请求失败
            }
            //获取 相应的header
            // URLConnection urlConnection = connection;
            // urlConnection.getHeaderFields();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (bufferedWriter != null) {
                    bufferedReader.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return stringBuffer.toString();
    }
}
