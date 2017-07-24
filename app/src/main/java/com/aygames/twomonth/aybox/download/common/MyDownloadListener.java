package com.aygames.twomonth.aybox.download.common;

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

  public MyDownloadListener() {
    super();
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
    Log.i("ok",info.get(0).getPath());
    Log.i("ok","onDownloadSuccess");
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
