package com.aygames.twomonth.aybox.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.aygames.twomonth.aybox.R;
import com.aygames.twomonth.aybox.activity.HomeActivity;
import com.aygames.twomonth.aybox.activity.JpushGG;
import com.aygames.twomonth.aybox.util.Constans;
import com.aygames.twomonth.aybox.util.StreamUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by wf05 on 2017/8/18.
 */

public class ServiceForMessage extends Service {
    private String title,ccontent,id,link;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        final SharedPreferences sharedPreferences = getSharedPreferences("message",MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        Log.i("message","消息推送接收启动");
        Log.i("message", System.currentTimeMillis()+"");
        Long time_now = System.currentTimeMillis();
        Long time_get = sharedPreferences.getLong("lasttime",0);
        if (time_now-time_get>1000){
            Log.i("time_get",time_get.toString());
            Log.i("time_now",time_now.toString());
            editor.putLong("lasttime",time_now);
            editor.commit();
            //从后台获取推送信息
            getMessage();
            new Thread(){
                @Override
                public void run() {
                    try {
                        sleep(2000);
                        //本地文件比较
                        if (sharedPreferences.getString("id","0").equals(id)){
                            stopService(new Intent(getApplicationContext(),ServiceForMessage.class));
                            Log.i("ServiceForMessage","关闭");
                        }else {
                            showNotifi();
                            editor.putString("id",id);
                            Log.i("id",id);
                            editor.commit();
                            Intent intent = new Intent(getApplicationContext(),ServiceForMessage.class);
                            stopService(intent);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();

        }

    }

    private void showNotifi() {
        //获取PendingIntent
        Intent mainIntent = new Intent(this, JpushGG.class);
        mainIntent.putExtra("link",link);
        mainIntent.putExtra("id",id);
        PendingIntent mainPendingIntent = PendingIntent.getActivity(this, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //创建 Notification.Builder 对象
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.hezi)
                //点击通知后自动清除
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(ccontent)
                .setContentIntent(mainPendingIntent);
        //发送通知
        notifyManager.notify(1, builder.build());
        //展示数量+1
        showMessage();
    }

    private void showMessage() {
        new Thread(){
            @Override
            public void run() {
                try{
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(Constans.APP_DOWNLOAD
                            +id).openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setRequestProperty("Content-Type", "text/html");
                    httpURLConnection.connect();
                    BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    Log.i("状态返回：",bufferedReader.toString());
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void getMessage() {
        new Thread(){
            @Override
            public void run() {
                try {
                    Log.i("消息推送：", Constans.URL_MESSAGE);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(Constans.URL_MESSAGE).openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setReadTimeout(5000);
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.connect();
                    if (httpURLConnection.getResponseCode()==200){
                        InputStream is = httpURLConnection.getInputStream();
                        String string = StreamUtil.stream2string(is);
                        JSONObject jsonObject = new JSONObject(string);
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        Log.i("data",jsonObject1.toString());
                        id = jsonObject1.getString("id");
                        ccontent = jsonObject1.getString("content");
                        title = jsonObject1.getString("title");
                        link = jsonObject1.getString("url");
                        Log.i("message",id+","+ccontent+","+title+","+link);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
