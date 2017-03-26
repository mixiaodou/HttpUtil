package com.text.httputil.httpurl;

import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by Zhdf on 2017/3/25.
 * 使用httpURL 上传下载 文件
 */

public class HttpURLUpDownUtil {
    /*
    * 上传 文件，post方式
    * */
    public static void doUpFile(String urlPath, ArrayList<String> list) {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        //
        HttpURLConnection connection = null;
        DataOutputStream dataOutPut = null;
        BufferedReader bufferedReader = null;
        StringBuffer stringBuffer = null;
        try {
            URL url = new URL(urlPath);
            connection = (HttpURLConnection) url.openConnection();
            //设置
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            //post不能使用缓存
            connection.setUseCaches(false);
            //设置 header
            //字符编码连接参数
            connection.setRequestProperty("Connection", "Keep-Alive");
            //字符编码
            connection.setRequestProperty("Charset", "UTF-8");
            //请求内容类型
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="
                    + boundary);
            connection.connect();
            dataOutPut = new DataOutputStream(connection.getOutputStream());
            for (int i = 0; i < list.size(); i++) {
                String filePath = list.get(i);
                String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
                //写入 文件信息
                dataOutPut.writeBytes(twoHyphens + boundary + end);
                dataOutPut.writeBytes("Content-Disposition: form-data; "
                        + "name=\"file" + i + "\";filename=\"" + fileName
                        + "\"" + end);
                //写入 文件信息 结束
                dataOutPut.writeBytes(end);
                // 写入 文件数据
                FileInputStream fileInputStream = new FileInputStream(filePath);
                byte[] temp = new byte[1024];
                int tempLength = 0;
                while ((tempLength = fileInputStream.read(temp)) != 0) {
                    dataOutPut.write(temp, 0, tempLength);
                    dataOutPut.flush();
                }
                // 写入 文件数据 结束
                dataOutPut.writeBytes(end);
                fileInputStream.close();
            }
            //写入结束
            dataOutPut.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dataOutPut.flush();
            if (connection.getResponseCode() == 200) {
                //获取 返回数据
                bufferedReader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                stringBuffer = new StringBuffer();
                String temp = null;
                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuffer.append(temp);
                }
                bufferedReader.close();
                //上传 文件成功
                Log.i("", stringBuffer.toString());

            } else {
                //上传 文件失败
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (dataOutPut != null) {
                    dataOutPut.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /*
    * 使用get方法,fileDir下载的文件夹,可通过url获取文件名
    * */
    public static void doDownFile(String urlPath, String fileDir) {
        HttpURLConnection connection = null;
        BufferedReader bufferedReader = null;
        File file = null;
        FileOutputStream fileOutputStream = null;
        try {
            URL url = new URL(urlPath);
            connection = (HttpURLConnection) url.openConnection();
            //设置 请求超时时间
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            //
            connection.setRequestProperty("Charset", "UTF-8");
            connection.connect();
            if (connection.getResponseCode() == 200) {
                //获取 文件信息
                int fSize = connection.getContentLength();
                String fPath = connection.getURL().getFile();
                String fName = fPath.substring(fPath.lastIndexOf("/") + 1);
                //
                bufferedReader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                file = new File(fileDir + "/" + fName);
                fileOutputStream = new FileOutputStream(file);
                byte[] temp = new byte[1024];
                int tempLength = 0;
                //进度
                int progressLength = 0;
                while ((tempLength = bufferedReader.read()) != 0) {
                    progressLength += tempLength;
                    fileOutputStream.write(temp, 0, tempLength);
                    fileOutputStream.flush();
                }
            } else {
                //下载 失败
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
