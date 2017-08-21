package com.aygames.twomonth.aybox.application;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import cn.woblog.android.downloader.DownloadService;
import cn.woblog.android.downloader.callback.DownloadManager;
import cn.woblog.android.downloader.config.Config;

/**
 * Created by MyPC on 2017/6/13.
 */

public class AyBoxApplication extends Application {
    public static String path_adver = null;
    public static Bitmap bit_adver = null;
    public static String id_adver=null;
    public static DownloadManager downloadManager;
    public static Context ctx;
    @Override
    public void onCreate() {
        super.onCreate();

        ctx = getApplicationContext();
        Config config = new Config();
        //set database path.
        //    config.setDatabaseName("/sdcard/a/d.db");
        //      config.setDownloadDBController(dbController);
        //set download quantity at the same time.
        config.setDownloadThread(1);
        //set each download info thread number
        config.setEachDownloadThread(1);
        // set connect timeout,unit millisecond
        config.setConnectTimeout(10000);
        // set read data timeout,unit millisecond
        config.setReadTimeout(10000);
        downloadManager = DownloadService.getDownloadManager(this, config);
    }
}
