package com.aygames.twomonth.aybox.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.aygames.twomonth.aybox.util.Constans;

/**
 * Created by wf05 on 2017/7/27.
 */

public class AppInstallReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        PackageManager manager = context.getPackageManager();
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            for (int i=0;i<Constans.arrayList.size();i++){
                String baoming = Constans.arrayList.get(i).get("packageName").toString();
                if (baoming.equals(packageName)){
                    //kanzheli
                }
            }
        }
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
            String packageName = intent.getData().getSchemeSpecificPart();
            String name = intent.getDataString();
            String name2 = intent.getData().getEncodedPath();
            Log.i("卸载成功","||"+packageName);
        }
    }
}
