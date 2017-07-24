package com.aygames.twomonth.aybox.service;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.test.LoaderTestCase;
import android.util.Log;

import com.aygames.twomonth.aybox.download.common.DownloadStatusChanged;

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
        fileListneer = new FileListneer(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+"AYgames");
        fileListneer.startWatching();
        Log.i("fileservice","start");
        return super.onStartCommand(intent, flags, startId);
    }
}
