package com.aygames.twomonth.aybox.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.aygames.twomonth.aybox.util.StreamUtil.stream2string;

/**
 * Created by MyPC on 2017/3/30.
 */

public class GetDateImpl {
    //渠道获取
    public static String getChannel(Context context) {
        // 从配置文件中 找到文件
        ApplicationInfo appinfo = context.getApplicationInfo();
        String sourceDir = appinfo.sourceDir;

        String ret = "";
        ZipFile zipfile = null;
        try {
            zipfile = new ZipFile(sourceDir);
            Enumeration<?> entries = zipfile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());

                String entryName = entry.getName();

                if (entryName.startsWith("META-INF/AY_")) {
                    ret = entryName;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zipfile != null) {
                try {
                    zipfile.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String[] split = ret.split("_");
        if (split != null && split.length >= 2) {

            return  split[1];
        }
        else {
            return "CH1149060018297";
        }
    }

}
