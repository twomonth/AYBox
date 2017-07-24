package com.aygames.twomonth.aybox.download.common;

import android.util.Log;

import java.lang.ref.SoftReference;

import cn.woblog.android.downloader.callback.AbsDownloadListener;
import cn.woblog.android.downloader.exception.DownloadException;

/**
 * Created by renpingqing on 17/1/22.
 */

public abstract class MyDownloadListener extends AbsDownloadListener {

  public MyDownloadListener() {
    super();
  }

  public MyDownloadListener(SoftReference<Object> userTag) {
    super(userTag);
  }

  @Override
  public void onStart() {
    onRefresh();
  }

  public abstract void onRefresh();

  @Override
  public void onWaited() {
    onRefresh();
  }

  @Override
  public void onDownloading(long progress, long size) {
    onRefresh();
    Log.i("下载监听器3，收到下载完成","");
  }

  @Override
  public void onRemoved() {
    onRefresh();
    Log.i("下载监听器2，收到下载完成","");
  }

  @Override
  public void onDownloadSuccess() {

    onRefresh();
    Log.i("下载监听器，收到下载完成","");
  }

  @Override
  public void onDownloadFailed(DownloadException e) {
    onRefresh();
  }

  @Override
  public void onPaused() {
    onRefresh();
  }
}
