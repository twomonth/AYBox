package com.aygames.twomonth.aybox.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.aygames.twomonth.aybox.service.ServiceForMessage;


public class HiddenWaiter extends BroadcastReceiver {
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        String action = intent.getAction();
        switch (action) {
            case "android.intent.action.SCREEN_OFF":
                Log.i("test", "屏幕关闭" + System.currentTimeMillis());
                Intent intent1 = new Intent();
                intent1.setClass(context, ServiceForMessage.class);
                context.startService(intent1);
                break;

            case "android.intent.action.SCREEN_ON":
                Log.i("test", "屏幕开启");
                break;

            default:
                break;
        }
    }

    boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
