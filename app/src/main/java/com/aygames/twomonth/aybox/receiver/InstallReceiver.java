package com.aygames.twomonth.aybox.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.FileNotFoundException;

/**
 * Created by wf05 on 2017/7/14.
 */

public class InstallReceiver extends BroadcastReceiver {

    private static final String TAG = "InstallReceiver";
    // 安装下载接收器
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("广播寄售期","shoudao xiazaiwanchengguangbo ");
        long myDwonloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        Log.i("myDwonloadID",myDwonloadID+"");
        SharedPreferences sPreferences = context.getSharedPreferences("downloadplato", 0);
        String game_path = sPreferences.getString(myDwonloadID+"", null);
        Log.i("game_path",game_path);
        String serviceString = Context.DOWNLOAD_SERVICE;
        DownloadManager dManager = (DownloadManager) context.getSystemService(serviceString);
        Intent install = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        install.setDataAndType(Uri.parse("file://" +game_path), "application/vnd.android.package-archive");
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(install);
    }
}

