package com.aygames.twomonth.aybox.util;

import android.content.pm.PackageManager;
import android.util.Log;

import com.aygames.twomonth.aybox.application.AyBoxApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by wf05 on 2017/7/27.
 */

public class GetDataDownLoad {

    public static void statisticsDownload (String gid, int code){
                try {
                    Log.i("开始下载统计接口：",Constans.APP_DOWNLOAD
                            +"imei"+"/" +Constans.iemi+"/"
                            +"chid"+"/"+Constans.channel+"/"
                            +"gid"+"/"+gid+"/"
                            +"code"+"/"+code);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(Constans.APP_DOWNLOAD
                            +"imei"+"/" +Constans.iemi+"/"
                            +"chid"+"/"+Constans.channel+"/"
                            +"gid"+"/"+gid+"/"
                            +"code"+"/"+code)
                            .openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setRequestProperty("Content-Type", "text/html");
                    httpURLConnection.connect();
                    BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    Log.i("状态返回：",bufferedReader.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
    }
    public static void installOk(final String packageName){
        PackageManager manager = AyBoxApplication.ctx.getPackageManager();
        ArrayList<Map<String, Object>> arrayList = new ArrayList<>();
        arrayList = Constans.arrayList;
        for (int i=0;i<arrayList.size();i++){
            if (packageName.equals(arrayList.get(i).get("packageName"))){
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        GetDataDownLoad.statisticsDownload(packageName,3);
                        Log.i("有程序安装","||"+packageName);
                    }
                }.start();
            }
        }
    }
}
