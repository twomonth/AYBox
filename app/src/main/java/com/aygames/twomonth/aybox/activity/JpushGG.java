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

import static com.aygames.twomonth.aybox.R.id.webview_jpush;

/**
 * Created by MyPC on 2017/3/3.
 */

public class JpushGG extends Activity {
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guanggao);
        String url = getIntent().getStringExtra("url");
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
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
        finish();
        return super.onKeyDown(keyCode, event);
    }

    class MyWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                    long contentLength) {
            Toast.makeText(getApplicationContext(),"正在创建下载任务",Toast.LENGTH_LONG).show();
            DownloadManager downloadManager;
            downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
            //request.setNotificationVisibility(View.GONE);
            //设置文件存放目录
            //request.setDestinationInExternalFilesDir(getApplicationContext(), Environment.getExternalStorageDirectory().getAbsolutePath(), "");
            Log.i("下载任务路径", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
            int index = url.lastIndexOf("/");
            String game_name = url.substring(index+1);
            // 不同的手机不同Android版本的SD卡的挂载点可能会不一样，因此通过系统方式获取。
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdir();
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, game_name);
            //request.setDestinationInExternalPublicDir(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(),game_name);
            request.setDescription(game_name);
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
            // 下载标题
            request.setTitle(game_name);
            request.setVisibleInDownloadsUi(true);
            long id = downloadManager.enqueue(request);
            // 把当前下载的ID保存起来
            SharedPreferences sPreferences = getSharedPreferences("downloadplato", 0);
            sPreferences.edit().putString(id+"",Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+"/"+game_name).commit();
            Log.i("下载任务id",id+"");
//            downloadManager.enqueue(request);
            Toast.makeText(getApplicationContext(),"正在下载",Toast.LENGTH_LONG).show();
            // 文件将存放在外部存储的确实download文件内，如果无此文件夹，创建之，如果有，下面将返回false。
            // 不同的手机不同Android版本的SD卡的挂载点可能会不一样，因此通过系统方式获取。
            // Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdir();
            // request.setDestinationInExternalPublicDir("/download/", game_name);
            // 下载文件类型
            // String extension = MimeTypeMap.getFileExtensionFromUrl(url);
            // String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            // request.setMimeType(mimeType);
            // 设置UI是否可见

        }
    }
}
