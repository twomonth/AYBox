package com.aygames.twomonth.aybox.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class FileService extends Service {
    private FileListneer fileListneer;
    public FileService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        fileListneer = new FileListneer(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+"AYgames");
//        fileListneer.startWatching();
//        Log.i("fileservice","start");
//        MyDownloadListener lisTener = new LISTener();
        //监听正在下载的任务数量
//        List<DownloadInfo> downloadInfos = AyBoxApplication.downloadManager.findAllDownloading();
//        Log.i("downloadinfo",downloadInfos.toString()+"111111");
        return super.onStartCommand(intent, flags, startId);
    }
}
