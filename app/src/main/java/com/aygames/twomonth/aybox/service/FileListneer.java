package com.aygames.twomonth.aybox.service;

import android.os.FileObserver;
import android.util.Log;

/**
 * Created by wf05 on 2017/7/24.
 */

public class FileListneer extends FileObserver {
    public FileListneer(String path) {
        super(path);
    }

    @Override
    public void onEvent(int i, String s) {
        switch(i){
            case FileObserver.ACCESS:
                Log.i("fileobserver","文件被访问");
                break;
            case FileObserver.MODIFY:
                Log.i("fileobserver","文件被修改");
                break;
            case FileObserver.CLOSE_WRITE|CLOSE_NOWRITE:
                Log.i("fileobserver","可写文件被修改关闭"+s);
                break;
            case FileObserver.OPEN:
                Log.i("fileobserver","文件被打开");
                break;
            case FileObserver.DELETE:
                Log.i("fileobserver","文件被删除");
                break;
        }
    }
}
