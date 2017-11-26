package com.aygames.twomonth.aybox.download.common;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.aygames.twomonth.aybox.BuildConfig;
import com.aygames.twomonth.aybox.R;
import com.aygames.twomonth.aybox.application.AyBoxApplication;
import com.aygames.twomonth.aybox.util.Constans;
import com.aygames.twomonth.aybox.util.GetDataDownLoad;

import java.io.File;
import java.lang.ref.SoftReference;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.woblog.android.downloader.callback.AbsDownloadListener;
import cn.woblog.android.downloader.domain.DownloadInfo;
import cn.woblog.android.downloader.exception.DownloadException;

/**
 * Created by renpingqing on 17/1/22.
 */

public abstract class MyDownloadListener extends AbsDownloadListener {
  private Context context ;
  private String path;
  public MyDownloadListener(Context context,String path) {
    super();
    this.context=context;
    this.path = path;
  }

  public MyDownloadListener(Context context,String path,SoftReference<Object> userTag) {
    super(userTag);
    this.context=context;
    this.path=path;
    Log.i("ok","MyDownloadListener");
  }

  @Override
  public void onStart() {
    Log.i("ok","onStart");
    onRefresh();
  }

  public abstract void onRefresh();

  @Override
  public void onWaited() {
    Log.i("ok","onWaited");
    onRefresh();
  }

  @Override
  public void onDownloading(long progress, long size) {
    Log.i("ok","onDownloading");
    onRefresh();
  }

  @Override
  public void onRemoved() {
    Log.i("ok","onRemoved");
    onRefresh();
  }

  @Override
  public void onDownloadSuccess() {
    Log.i("ok",path+"");
    Intent install = new Intent(Intent.ACTION_VIEW);
    install.addCategory(Intent.CATEGORY_DEFAULT);
      install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//    if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.N){
//        Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider", new File(path));
//        install.setDataAndType(contentUri,"application/vnd.android.package-archive");
//    }
    install.setDataAndType(Uri.parse("file://"+path), "application/vnd.android.package-archive");

    context.startActivity(install);
    initList();
    new Thread(){
      @Override
      public void run() {
        super.run();
        String game_name = path.substring(path.lastIndexOf("/"));
        String gid = game_name.substring(0,game_name.indexOf("_"));
        GetDataDownLoad.statisticsDownload(gid,2);
      }
    }.start();
    onRefresh();
  }

  @Override
  public void onDownloadFailed(DownloadException e) {
    Log.i("ok","onDownloadFailed"+e.getCode());
    if (e.getCode()==5){
//      Log.i("ok",path+"");
//      Intent install = new Intent(Intent.ACTION_VIEW);
//      install.addCategory(Intent.CATEGORY_DEFAULT);
//      install.setDataAndType(Uri.parse("file://"+path), "application/vnd.android.package-archive");
//      install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//      context.startActivity(install);
//      initList();
//      new Thread(){
//        @Override
//        public void run() {
//          super.run();
//          String game_name = path.substring(path.lastIndexOf("/"));
//          String gid = game_name.substring(0,game_name.indexOf("_"));
//          GetDataDownLoad.statisticsDownload(gid,2);
//        }
//      }
      onDownloadSuccess();
    }
//    onRefresh();
  }

  @Override
  public void onPaused() {
    Log.i("ok","onPaused");
    onRefresh();
  }
  public void initList(){
    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "AYgames");
    File [] apkFilelist = file.listFiles();
    PackageManager packageManager = AyBoxApplication.ctx.getPackageManager();
    ArrayList<Map<String, Object>> arrayList = new ArrayList<>();
    for (int i = 0; i < apkFilelist.length; i++) {
      PackageInfo packageInfo = null;
      String appName = null;
      String packageName = null;
      String version = null;
      Drawable icon1 = null;
      try {
        packageInfo = packageManager.getPackageArchiveInfo(apkFilelist[i].getPath(), PackageManager.GET_ACTIVITIES);
        ApplicationInfo appInfo = packageInfo.applicationInfo;
                /* 必须加这两句，不然下面icon获取是default icon而不是应用包的icon */
        appInfo.sourceDir = apkFilelist[i].getPath();
        appInfo.publicSourceDir = apkFilelist[i].getPath();
        appName = packageManager.getApplicationLabel(appInfo).toString();// 得到应用名
        packageName = appInfo.packageName; // 得到包名
        version = packageInfo.versionName; // 得到版本信息
        icon1 = packageManager.getApplicationIcon(appInfo);// 得到图标信息
        Map<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("icon", icon1);
        hashMap.put("appName", appName);
        hashMap.put("packageName", packageName);
        hashMap.put("version", version);
        hashMap.put("apkFileName", apkFilelist[i]);
        arrayList.add(hashMap);
      }catch (Exception e){
        Map<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("icon", AyBoxApplication.ctx.getResources().getDrawable(R.mipmap.ic_launcher));
        hashMap.put("appName", "尚未完成");
        hashMap.put("packageName", "x");
        hashMap.put("version","x");
        hashMap.put("apkFileName", apkFilelist[i]);
        arrayList.add(hashMap);
      }
    }
    Constans.arrayList = arrayList;
  }
}
