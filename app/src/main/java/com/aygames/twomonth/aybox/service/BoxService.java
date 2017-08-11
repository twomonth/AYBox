package com.aygames.twomonth.aybox.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.aygames.twomonth.aybox.util.Constans;
import com.aygames.twomonth.aybox.util.GetDateImpl;
import com.google.gson.jpush.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by wf05 on 2017/7/14.
 */

public class BoxService extends Service {
    public String imei,user_tel,os_ver,agent;
    public int version;
    public static final String TAG = "BoxService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.i("service服务","启动");
        super.onCreate();
        initData();
        new Thread(){
            @Override
            public void run() {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("imei",imei);
                    jsonObject.put("user_tel",user_tel);
                    jsonObject.put("os_ver",agent);
                    jsonObject.put("agent",agent);
                    jsonObject.put("version",version);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(Constans.ORDER_SHARE).openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setRequestProperty("Content-Type", "text/html");
                    httpURLConnection.connect();
                    OutputStreamWriter outputStreamWriter= new OutputStreamWriter(httpURLConnection.getOutputStream());
                    outputStreamWriter.write(jsonObject.toString());
                    Log.i("用户启动发送数据",jsonObject+"");
                    outputStreamWriter.flush();
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

    private void initData() {
        TelephonyManager tm = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
        try {
            // 获取手机串号
            imei = tm.getDeviceId();
            // 获取电话号码
            user_tel = tm.getLine1Number();
        } catch (Exception e) {
            e.printStackTrace();
            user_tel = "";
            imei = "";
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
    }
}
