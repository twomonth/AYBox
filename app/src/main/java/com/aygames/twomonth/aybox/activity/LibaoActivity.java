package com.aygames.twomonth.aybox.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.aygames.twomonth.aybox.R;
import com.aygames.twomonth.aybox.util.Constans;
import com.aygames.twomonth.aybox.util.GetDateImpl;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by wf05 on 2017/9/25.
 */

public class LibaoActivity extends Activity {
    public String url ;
    String gameID;
    private WebView lblqy;
    private TextView lblq_back;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            lblqy=(WebView) findViewById(R.id.webview_libao);
            lblq_back=(TextView) findViewById(R.id.lblq_back);
            lblqy.getSettings().setJavaScriptEnabled(true);
            lblqy.loadUrl(url);
            lblqy.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // TODO Auto-generated method stub
                    return super.shouldOverrideUrlLoading(view, url);
                }
            });

            lblq_back.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    finish();

                }
            });
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_libao);
        gameID = getIntent().getStringArrayListExtra("msg").get(0);
        Log.i("获取gid",gameID);
        getUrlAndDownload();
    }
    private void getUrlAndDownload(){
        new Thread(){
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("gid", gameID);
                    jsonObject.put("chid", GetDateImpl.getChannel(getApplicationContext()));
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(Constans.URL_GETGIFT).openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setRequestProperty("Content-Type", "text/html");
                    httpURLConnection.connect();
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream());
                    outputStreamWriter.write(jsonObject.toString());
                    Log.i("礼包界面发送数据", jsonObject.toString());
                    outputStreamWriter.flush();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    JSONObject jsonObject2 = new JSONObject(bufferedReader.readLine().toString());
                    Log.i("礼包领取页地址",jsonObject2.toString());
                    url = jsonObject2.getString("data");
                    Message m = new Message();
                    handler.sendMessage(m);
                    Log.i("礼包领取页地址",url);
                } catch (MalformedURLException e) {
                    Log.i("获取APP下载地址时异常", e.toString());
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.i("输入输出异常", e.toString());
                    e.printStackTrace();
                } catch (JSONException e) {
                    Log.i("json解析异常", e.toString());
                    e.printStackTrace();
                }
            };
        }.start();
    }

}
