package com.aygames.twomonth.aybox.activity;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.aygames.twomonth.aybox.R;
import com.aygames.twomonth.aybox.application.AyBoxApplication;
import com.aygames.twomonth.aybox.download.common.MyDownloadListener;
import com.aygames.twomonth.aybox.util.Constans;
import com.aygames.twomonth.aybox.util.GetDataDownLoad;
import com.aygames.twomonth.aybox.util.GetDateImpl;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import cn.woblog.android.downloader.domain.DownloadInfo;

/**
 * Created by MyPC on 2017/6/20.
 */

public class Activity_advertisement extends Activity {
    private WebView webView;
    private String path;
    private String gid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_advertisement);
        path = getIntent().getStringExtra("url");
        webView = (WebView) findViewById(R.id.web_advertisement);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setDownloadListener(new MyWebViewDownLoadListener());
        webView.loadUrl(path);
//        Log.i("渠道wap页面网址","http://"+ channel_name+path);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                return super.shouldOverrideUrlLoading(view,path);
            }
        });
        adverAdd();
        this.finish();
    }

    private void adverAdd() {
        new Thread(){
            @Override
            public void run() {
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(Constans.URL_ADVERADD+AyBoxApplication.id_adver).openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setRequestProperty("Content-Type", "text/html");
                    httpURLConnection.connect();
                    BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        finish();
        startActivity(new Intent(this,HomeActivity.class));
        return super.onKeyDown(keyCode, event);
    }

    class MyWebViewDownLoadListener implements DownloadListener{

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                    long contentLength) {
            File d = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "AYgames");
            if (!d.exists()) {
                d.mkdirs();
            }
            int index = url.lastIndexOf("/");
            String game_name = url.substring(index + 1);
            gid = game_name.substring(0, game_name.indexOf("_"));
            String path = d.getAbsolutePath().concat("/").concat(game_name);
            DownloadInfo downloadInfo = new DownloadInfo.Builder().setUrl(url)
                    .setPath(path)
                    .build();
            AyBoxApplication.downloadManager.download(downloadInfo);
            Toast.makeText(getApplicationContext(), "开始下载，点击右上角下载图标查看。", Toast.LENGTH_LONG).show();
            downloadInfo.setDownloadListener(new MyDownloadListener(getApplicationContext(), downloadInfo.getPath()) {
                @Override
                public void onRefresh() {

                }
            });
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    GetDataDownLoad.statisticsDownload(gid, 1);
                }
            }.start();
        }
    }
}
