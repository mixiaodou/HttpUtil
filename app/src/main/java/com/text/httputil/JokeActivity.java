package com.text.httputil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.text.httputil.httpclient.HttpClientutil;
import com.text.httputil.httpurl.HttpURLUtil;

//笑话
public class JokeActivity extends AppCompatActivity {
    private String url = "http://japi.juhe.cn/joke/content/list.from" +
            "?key=ba5de80f9a2055cae29f1fdb37e92129";
    private CheckBox checkBox;
    private boolean userHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);
        checkBox = ((CheckBox) findViewById(R.id.joke_box));
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userHttpClient = isChecked;
            }
        });

    }

    public void click(View v) {
        switch (v.getId()) {
            case R.id.joke_btn:
                if (userHttpClient) {
                    getDataHttpClient();
                } else {
                    getDataHttpURL();
                }
                break;
        }
    }

    private void getDataHttpURL() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                String str = HttpURLUtil.doGet(url
                        + "&time=" + System.currentTimeMillis() / 1000);
                Log.i("httpurl get请求结果", str);
            }
        }.start();
    }

    private void getDataHttpClient() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                String str = HttpClientutil.doGet(url
                        + "&time=" + System.currentTimeMillis() / 1000);
                Log.i("httpClient get请求结果", str);
            }
        }.start();
    }
}
