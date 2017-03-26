package com.text.httputil.httpclient;

import com.android.internal.http.multipart.FilePart;
import com.android.internal.http.multipart.MultipartEntity;
import com.android.internal.http.multipart.Part;
import com.android.internal.http.multipart.StringPart;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Zhdf on 2017/3/25.
 * 使用httpClient 上传下载 文件
 */

public class HttpClientUpDownUtil {
    /*
      * 文件上传 使用post
      * */
    public static void doUpFile(String urlPath, ArrayList<String> list) {
        HttpPost httpRequest = new HttpPost(urlPath);
        HttpClient client = new DefaultHttpClient();
        //
        try {
            Part[] parts = new Part[list.size()];
            for (int i = 0; i < list.size(); i++) {
                String filePath = list.get(i);
                String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
                parts[i] = new FilePart(fileName, new File(filePath));
            }
            //设置 请求参数
            //parts[0]=new StringPart("key","value");
            MultipartEntity multipartEntity = new MultipartEntity(parts);
            httpRequest.setEntity(multipartEntity);
            //开始
            HttpResponse httpResponse = client.execute(httpRequest);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                String result = EntityUtils.toString(httpResponse.getEntity());
                String msg = httpResponse.getStatusLine().getReasonPhrase();
            } else {
                httpRequest.abort();
                //上传 失败
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    * 文件下载,使用get请求方法,可通过url获取文件名
    * */
    public static void doDownFile(String urlPath, String filePath) {
        HttpGet httpRequest = new HttpGet(urlPath);
        DefaultHttpClient client = new DefaultHttpClient();
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        //设置 头部
        httpRequest.addHeader("key", "value");
        try {
            HttpResponse httpResponse = client.execute(httpRequest);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                inputStream = httpResponse.getEntity().getContent();
                //获取 文件信息
                int fSize = (int) httpResponse.getEntity().getContentLength();
                String fPath = httpRequest.getURI().getPath();
                String fName = fPath.substring(fPath.lastIndexOf("/") + 1);
                File file = new File(filePath);
                fileOutputStream = new FileOutputStream(file);
                byte[] temp = new byte[1024];
                int tempLength = 0;
                int progressLength = 0;
                while ((tempLength = inputStream.read(temp)) != 0) {
                    progressLength += tempLength;
                    fileOutputStream.write(temp, 0, tempLength);
                    fileOutputStream.flush();
                }
            } else {
                httpRequest.abort();
                //下载失败
            }
            fileOutputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
