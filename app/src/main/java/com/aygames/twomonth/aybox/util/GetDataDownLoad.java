package com.aygames.twomonth.aybox.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by wf05 on 2017/7/27.
 */

public class GetDataDownLoad {

    public static void statisticsDownload (String gid, int code){
                try {
                    Log.i("开始下载统计接口：",Constans.APP_DOWNLOAD+Constans.iemi+"/"+Constans.channel+"/"+gid+"/"+code);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(Constans
                            .URL_APPISUPDATECLICK+Constans.iemi+"/"+Constans.channel+"/"+gid+"/"+code)
                            .openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setRequestProperty("Content-Type", "text/html");
                    httpURLConnection.connect();
                    BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    Log.i("更新返回：",bufferedReader.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
    }
}
