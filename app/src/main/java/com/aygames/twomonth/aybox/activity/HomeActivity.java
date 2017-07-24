package com.aygames.twomonth.aybox.activity;

import android.animation.ObjectAnimator;

import android.app.NotificationManager;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import android.graphics.drawable.BitmapDrawable;

import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.aygames.twomonth.aybox.R;
import com.aygames.twomonth.aybox.application.AyBoxApplication;
import com.aygames.twomonth.aybox.util.Constans;
import com.aygames.twomonth.aybox.util.GetDateImpl;
import com.aygames.twomonth.aybox.util.Installation;
import com.aygames.twomonth.aybox.util.StatusBarUtils;
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
import java.util.Calendar;

import cn.woblog.android.downloader.callback.DownloadManager;
import cn.woblog.android.downloader.domain.DownloadInfo;

public class HomeActivity extends Activity {


    private ObjectAnimator oa;
    private RelativeLayout rl_inneradver;
    private NotificationManager notificationManager;
    private WebView webView;
    private ImageView iv_inneradver;
    private ImageView iv_closeadver;
    private ImageView iv_download;
    private String path = "www.ofwan.com/";
    private String channel_name=null;
    public String imei,user_tel,os_ver,agent;
    public int version;
    private Handler mhandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setDownloadListener(new MyWebViewDownLoadListener());
            webView.loadUrl("http://"+path+channel_name);
            Log.i("渠道wap页面网址","http://"+ path+channel_name);
            webView.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // TODO Auto-generated method stub
                    return super.shouldOverrideUrlLoading(view,"http://"+path+channel_name);//"http://"+path+channel_name
                }
            });
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //调用工具类设置Activity颜色
        StatusBarUtils.setWindowStatusBarColor(this,R.color.colorAccent);
        init();
        SharedPreferences sharedPreferences = getSharedPreferences("advertisment",0);
        //获取系统日期
        Calendar calendar =Calendar.getInstance();
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        if (day!=sharedPreferences.getInt("day",0)){
            rl_inneradver.setVisibility(View.VISIBLE);
            iv_inneradver.setBackground(new BitmapDrawable(AyBoxApplication.bit_adver));
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("day",day);
            editor.commit();
        }
        new Thread(){
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("chid",GetDateImpl.getChannel(getApplicationContext()));
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(Constans.CHANNEL_NAME).openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setRequestProperty("Content-Type", "text/html");
                    httpURLConnection.connect();
                    OutputStreamWriter outputStreamWriter= new OutputStreamWriter(httpURLConnection.getOutputStream());
                    Log.i("发送渠道号",jsonObject.toString());
                    outputStreamWriter.write(jsonObject.toString());
                    outputStreamWriter.flush();
                    BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    JSONObject jsonObject_channel= new JSONObject(bufferedReader.readLine().toString());
                    channel_name=jsonObject_channel.getString("data").toString();
                    if (channel_name!=null){
                        Message message = new Message();
                        mhandler.sendMessage(message);
                    }else{
                        channel_name="game";
                        Message message=new Message();
                        mhandler.sendMessage(message);
                    }

                } catch (IOException e) {
                    Log.i("获取渠道名网络请求","错误");
                    channel_name="game";
                    Message message=new Message();
                    mhandler.sendMessage(message);
                    e.printStackTrace();
                } catch (JSONException e) {
                    Log.i("获取渠道名称发送Json","错误");
                    channel_name="game";
                    Message message=new Message();
                    mhandler.sendMessage(message);
                    e.printStackTrace();
                }
            }
        }.start();
        iv_closeadver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_inneradver.setVisibility(View.GONE);
            }
        });
        iv_inneradver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(),Activity_advertisement.class);
                intent.putExtra("url", AyBoxApplication.path_adver);
                startActivity(intent);
            }
        });
        rl_inneradver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        iv_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Activity_Download.class));
            }
        });
        login();
    }
    //初始化组件
    private void init() {
        webView= (WebView) findViewById(R.id.webview);
        rl_inneradver = (RelativeLayout) findViewById(R.id.rl_inneradver);
        iv_closeadver = (ImageView) findViewById(R.id.iv_closeadver);
        iv_inneradver = (ImageView) findViewById(R.id.iv_inneradver);
        iv_download = (ImageView) findViewById(R.id.iv_download);
//        FloatViewImpl.getInstance(this);
    }

    private class MyWebViewDownLoadListener implements DownloadListener{

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                    long contentLength) {

            File d = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "AYgames");
            if (!d.exists()) {
                d.mkdirs();
            }
            int index = url.lastIndexOf("/");
            String game_name = url.substring(index+1);
            String path = d.getAbsolutePath().concat("/").concat(game_name);
            DownloadInfo downloadInfo = new DownloadInfo.Builder().setUrl(url)
                    .setPath(path)
                    .build();
            AyBoxApplication.downloadManager.download(downloadInfo);

        }
    }

    public void login(){

        TelephonyManager tm = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
        try {
            // 获取手机串号
            imei = tm.getDeviceId();
            if (imei==null){
                imei = Installation.id(getApplicationContext());
            }
            // 获取电话号码
            user_tel = tm.getLine1Number();
        } catch (Exception e) {
            e.printStackTrace();
            user_tel = "";
            imei  = "";
        }
        // 获取操作系统版本
        os_ver = "android" + Build.VERSION.RELEASE;
        agent = GetDateImpl.getChannel(getApplicationContext());
        PackageManager packageManager = getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(getPackageName(),0);
            version = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        new Thread(){
            @Override
            public void run() {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("imei",imei);
                    jsonObject.put("user_tel",user_tel);
                    jsonObject.put("os_ver",os_ver);
                    jsonObject.put("agent",agent);
                    jsonObject.put("version",version);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(Constans.URL_IMEI).openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setRequestProperty("Content-Type", "text/html");
                    httpURLConnection.connect();
                    OutputStreamWriter outputStreamWriter= new OutputStreamWriter(httpURLConnection.getOutputStream());
                    outputStreamWriter.write(jsonObject.toString());
                    Log.i("用户启动发送数据",jsonObject+"");
                    outputStreamWriter.flush();
                    BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    Log.i("用户启动发送数据后回调数据",bufferedReader.readLine());
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        moveTaskToBack(false);
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
        return super.onKeyDown(keyCode, event);
    }
}