package com.aygames.twomonth.aybox.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by wf05 on 2017/8/18.
 */

public class ServiceForMessage extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences sharedPreferences = getSharedPreferences("time",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Log.i("test","fuwuqidong");
        Log.i("test", System.currentTimeMillis()+"");
        Long time_now = System.currentTimeMillis();
        Long time_get = sharedPreferences.getLong("lasttime",0);
        if (time_now-time_get>1000){
            editor.putLong("lasttime",time_now);
            editor.commit();
            Log.i("lasttime",time_now.toString());

            Intent intent = new Intent(this,ServiceForMessage.class);
            stopService(intent);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
