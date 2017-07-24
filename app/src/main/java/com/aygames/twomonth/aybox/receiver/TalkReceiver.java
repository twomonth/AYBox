package com.aygames.twomonth.aybox.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.aygames.twomonth.aybox.R;
import com.aygames.twomonth.aybox.activity.HomeActivity;
import com.aygames.twomonth.aybox.activity.JpushGG;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by wf05 on 2017/7/17.
 */

public class TalkReceiver extends BroadcastReceiver {

    private static final String TAG = "自定义通知";
    private NotificationManager nm;
    private String title,url;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (null == nm) {
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        Bundle bundle = intent.getExtras();
//        String title = bundle.getString(JPushInterface.EXTRA_TITLE);
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        String file = bundle.getString(JPushInterface.EXTRA_MSG_ID);
        Log.i("极光推送自定义消息","|     |"+message+"||"+extras+"||"+file);
        if (extras==null){

        }else{
            // 在Android进行通知处理，首先需要重系统哪里获得通知管理器NotificationManager，它是一个系统Service。
            //        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            Intent intent1 = new Intent(context,JpushGG.class);
            try {
                JSONObject jsonObject = new JSONObject(extras);
                title = jsonObject.getString("title");
                intent1.putExtra("title",title);
                intent1.putExtra("url",jsonObject.getString("url"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent1,0);
            //获取NotificationManager实例
            NotificationManager notifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            //实例化NotificationCompat.Builde并设置相关属性
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    //设置小图标
                    .setSmallIcon(R.mipmap.hezi)
                    //设置通知标题
                    .setContentTitle(title)
                    //设置通知内容
                    .setContentText(message)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL);
            //设置通知时间，默认为系统发出通知的时间，通常不用设置
            //.setWhen(System.currentTimeMillis());
            //通过builder.build()方法生成Notification对象,并发送通知,id=1
            notifyManager.notify(1, builder.build());
        }

    }
}
