package com.aygames.twomonth.aybox.activity;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.aygames.twomonth.aybox.R;
import com.aygames.twomonth.aybox.application.AyBoxApplication;
import com.aygames.twomonth.aybox.download.common.DBController;
import com.aygames.twomonth.aybox.download.common.MyDownloadListener;
import com.aygames.twomonth.aybox.util.Constans;
import com.aygames.twomonth.aybox.util.GetDataDownLoad;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;

import cn.woblog.android.downloader.domain.DownloadInfo;
import cn.woblog.android.downloader.exception.DownloadException;

import static com.aygames.twomonth.aybox.R.id.webview_jpush;

/**
 * Created by MyPC on 2017/3/3.
 */

public class JpushGG extends Activity {
    private WebView webView;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guanggao);
        String url = getIntent().getStringExtra("link");
        id = getIntent().getStringExtra("id");
        webView = (WebView) findViewById(webview_jpush);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setDownloadListener(new MyWebViewDownLoadListener());
        webView.loadUrl(url);
        Log.i("极光推送wap页面网址","http://"+ url);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                return super.shouldOverrideUrlLoading(view,url);//"http://"+path+channel_name
            }
        });
        //点击数+1
        click();
    }

    private void click() {
        new Thread(){
            @Override
            public void run() {
                try{
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(Constans.APP_DOWNLOAD
                            +id).openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setRequestProperty("Content-Type", "text/html");
                    httpURLConnection.connect();
                    BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    Log.i("状态返回：",bufferedReader.toString());
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
        finish();
        return super.onKeyDown(keyCode, event);
    }

//    class MyWebViewDownLoadListener implements DownloadListener {
//
//        @Override
//        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
//                                    long contentLength) {
//            Toast.makeText(getApplicationContext(),"正在创建下载任务",Toast.LENGTH_LONG).show();
//            DownloadManager downloadManager;
//            downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
//            Uri downloadUri = Uri.parse(url);
//            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
//            //request.setNotificationVisibility(View.GONE);
//            //设置文件存放目录
//            //request.setDestinationInExternalFilesDir(getApplicationContext(), Environment.getExternalStorageDirectory().getAbsolutePath(), "");
//            Log.i("下载任务路径", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
//            int index = url.lastIndexOf("/");
//            String game_name = url.substring(index+1);
//            // 不同的手机不同Android版本的SD卡的挂载点可能会不一样，因此通过系统方式获取。
//            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdir();
//            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, game_name);
//            //request.setDestinationInExternalPublicDir(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(),game_name);
//            request.setDescription(game_name);
//            request.allowScanningByMediaScanner();
//            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
//            // 下载标题
//            request.setTitle(game_name);
//            request.setVisibleInDownloadsUi(true);
//            long id = downloadManager.enqueue(request);
//            // 把当前下载的ID保存起来
//            SharedPreferences sPreferences = getSharedPreferences("downloadplato", 0);
//            sPreferences.edit().putString(id+"",Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+"/"+game_name).commit();
//            Log.i("下载任务id",id+"");
////            downloadManager.enqueue(request);
//            Toast.makeText(getApplicationContext(),"正在下载",Toast.LENGTH_LONG).show();
//            // 文件将存放在外部存储的确实download文件内，如果无此文件夹，创建之，如果有，下面将返回false。
//            // 不同的手机不同Android版本的SD卡的挂载点可能会不一样，因此通过系统方式获取。
//            // Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdir();
//            // request.setDestinationInExternalPublicDir("/download/", game_name);
//            // 下载文件类型
//            // String extension = MimeTypeMap.getFileExtensionFromUrl(url);
//            // String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
//            // request.setMimeType(mimeType);
//            // 设置UI是否可见
//
//        }
//    }
private class MyWebViewDownLoadListener implements DownloadListener {
    String gid = null;

    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                long contentLength) {
        File d = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "AYgames");
        if (!d.exists()) {
            d.mkdirs();
        }
        int index = url.lastIndexOf("/");
        String game_name = url.substring(index + 1);
        gid = game_name.substring(0, game_name.indexOf("_"));
        String path = d.getAbsolutePath().concat("/").concat(game_name);
        final DownloadInfo downloadInfo = new DownloadInfo.Builder().setUrl(url)
                .setPath(path)
                .build();
        AyBoxApplication.downloadManager.download(downloadInfo);
        Toast.makeText(getApplicationContext(), "开始下载，点击右上角下载按钮查看", Toast.LENGTH_LONG).show();
        downloadInfo.setDownloadListener(new MyDownloadListener(getApplicationContext(), downloadInfo.getPath()) {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onDownloadFailed(DownloadException e) {
                if (e.getCode() == 5){
                    try {
                        DBController.getInstance(getApplicationContext()).deleteMyDownloadInfo(downloadInfo.getUri().hashCode());
                    } catch (SQLException sql) {
                        sql.printStackTrace();
                    }
                }
            }
        });
        new Thread() {
            @Override
            public void run() {
                super.run();
                GetDataDownLoad.statisticsDownload(gid, 1);
            }
        }.start();

    }
}
}
