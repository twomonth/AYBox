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
    private String title,message,id,link;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences sharedPreferences = getSharedPreferences("message",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
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
            //获取PendingIntent
            Intent mainIntent = new Intent(this, HomeActivity.class);
            PendingIntent mainPendingIntent = PendingIntent.getActivity(this, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationManager notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            //创建 Notification.Builder 对象
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.hezi)
                    //点击通知后自动清除
                    .setAutoCancel(true)
                    .setContentTitle("我是带Action的Notification")
                    .setContentText("点我会打开MainActivity")
                    .setContentIntent(mainPendingIntent);
            //发送通知
            notifyManager.notify(1, builder.build());
            Intent intent = new Intent(this,ServiceForMessage.class);
            stopService(intent);
        }

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
                        Log.i("xiaoxi",jsonObject1.toString());
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
