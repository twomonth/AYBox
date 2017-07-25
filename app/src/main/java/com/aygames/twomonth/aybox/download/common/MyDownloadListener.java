package com.aygames.twomonth.aybox.download.common;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.aygames.twomonth.aybox.application.AyBoxApplication;

import java.lang.ref.SoftReference;
import java.util.List;

import cn.woblog.android.downloader.callback.AbsDownloadListener;
import cn.woblog.android.downloader.domain.DownloadInfo;
import cn.woblog.android.downloader.exception.DownloadException;

/**
 * Created by renpingqing on 17/1/22.
 */

public abstract class MyDownloadListener extends AbsDownloadListener {
  private Context context;
  public MyDownloadListener(Context context) {
    super();
    this.context=context;
    Log.i("抽象类被继承了","okok!!!!!!!");
  }

  public MyDownloadListener(SoftReference<Object> userTag) {
    super(userTag);
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
    List<DownloadInfo> info = AyBoxApplication.downloadManager.findAllDownloaded();
//    Log.i("ok","onDownloadSuccess");
    Log.i("ok",info.get(0).getPath());
    Intent install = new Intent(Intent.ACTION_VIEW);
    install.addCategory(Intent.CATEGORY_DEFAULT);
    install.setDataAndType(Uri.parse("file://" +info.get(0).getPath()), "application/vnd.android.package-archive");
    install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(install);
    onRefresh();
  }

  @Override
  public void onDownloadFailed(DownloadException e) {
    Log.i("ok","onDownloadFailed");
    onRefresh();
  }

  @Override
  public void onPaused() {
    Log.i("ok","onPaused");
    onRefresh();
  }
}
