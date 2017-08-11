package com.aygames.twomonth.aybox.activity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.aygames.twomonth.aybox.R;
import com.aygames.twomonth.aybox.application.AyBoxApplication;
import com.aygames.twomonth.aybox.util.Constans;
import com.aygames.twomonth.aybox.util.StreamUtil;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.aygames.twomonth.aybox.util.StreamUtil.stream2string;

/**
 * 闪屏页面
 * 展示公司logo，公司品牌，商标，初始化项目
 * @author twomonth
 */

public class SplashActivity extends Activity {

    Thread thread = new Thread();
    int pid;
    private NotificationManager notificationManager;
    private static final int CODE_UPDATE_DIALOG = 1;
    private static final int CODE_ENTER_HOME = 2;
    private static final int CODE_URL_ERROR = 3;
    private static final int CODE_NETWORK_ERROR = 4;
    private static final int CODE_JSON_ERROR = 5;
    private static final int GET_MESSAGE = 6;
    private static final int GET_IMAGE = 7;
    private Bitmap bm = null;
    private RelativeLayout activity_main;
    private ImageView iv_advertisement;
    private TextView tv_versionname,tv_tiaoguo;
    //版本名称，描述，下载链接，版本号
    private String mversionName,mdes,murl,path_iamge,path_game,adver_id,box_id;
    private int mversionCode;
    private Handler mhandler = new Handler(){
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case CODE_UPDATE_DIALOG:
                    showUpdateDialog();
                    break;
                case CODE_ENTER_HOME:
                    enterHome();
                    break;
                case CODE_NETWORK_ERROR:
                    Toast.makeText(getApplicationContext(), "网络异常",Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case CODE_JSON_ERROR:
                    Toast.makeText(getApplicationContext(), "数据解析异常",Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case CODE_URL_ERROR :
                    enterHome();
                    break;
                case GET_MESSAGE:
                    getImage();
                    break;
                case GET_IMAGE:
//                    iv_advertisement.setImageBitmap((Bitmap)msg.obj);
//                    iv_advertisement.setImageDrawable((Drawable) msg.obj);
                    iv_advertisement.setBackground(new BitmapDrawable((Bitmap)msg.obj));
                    break;
                default:
                    enterHome();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tv_versionname= (TextView) findViewById(R.id.tv_version);
        iv_advertisement = (ImageView) findViewById(R.id.iv_advertisement);
        tv_versionname.setText("版本："+ getMversionName());
        activity_main= (RelativeLayout) findViewById(R.id.activity_main);
        tv_tiaoguo = (TextView) findViewById(R.id.tv_tiaoguo);

//        try{
//            Intent i = new Intent();
//            i.setComponent(new ComponentName("com.aygames.twomonth.aybox", "com.aygames.twomonth.aybox.server.MyService")); // change if changing package name
//            getApplicationContext().startService(i);
//            Toast.makeText(getApplicationContext(),"started successfully!",Toast.LENGTH_SHORT).show();
//        }
//        catch(Exception e){
//            Toast.makeText(getApplicationContext(),"error starting service!",Toast.LENGTH_SHORT).show();
//        }


        getMessage();
        checkVersion();

        tv_tiaoguo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mhandler=new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                    }
                };
                enterHome();

            }
        });
        iv_advertisement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(),Activity_advertisement.class);
                intent.putExtra("url",path_game);
                startActivity(intent);
                mhandler=new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                    }
                };
                finish();
            }
        });


        //闪屏页面渐变动画
        AlphaAnimation alphaAnimation=new AlphaAnimation(0,1);
        alphaAnimation.setDuration(2000);
        activity_main.startAnimation(alphaAnimation);
    }

    /**
     * 检测版本
     */
    private void checkVersion() {
        new Thread(){
            @Override
            public void run() {
                Message message=Message.obtain();
                long startTime=System.currentTimeMillis();
                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL(Constans.APP_UPDATE).openConnection();
                    connection.setRequestMethod("GET");
                    //连接超时
                    connection.setConnectTimeout(2000);
                    //读取超时
                    connection.setReadTimeout(2000);
                    //连接
                     connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200){
                        InputStream in = connection.getInputStream();
                        //将返回信息转换为字符串
                        String result= StreamUtil.stream2string(in);
                        Log.v("result",result);
                        JSONObject json0= new JSONObject(result);
                        JSONObject json = json0.getJSONObject("data");
                        mversionName =json.getString("versionname");
                        mversionCode=json.getInt("versioncode");
                        mdes=json.getString("des");
                        murl=json.getString("url");
                        box_id = json.getString("id");
                        Log.v("tag",mversionName+mversionCode+mdes+murl);
                        if (getVersionCode()<mversionCode){
                            Log.v("tag","有更新");
                            message.what=CODE_UPDATE_DIALOG;
                        }else {
                            Log.v("tag","没更新");
                            message.what=CODE_ENTER_HOME;
                        }

                    }
                } catch (MalformedURLException e) {
                    // url错误
                    e.printStackTrace();
                    message.what = CODE_URL_ERROR;
                }catch (IOException e) {
                    message.what=CODE_NETWORK_ERROR;
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                    message.what=CODE_JSON_ERROR;
                    Log.v("tag","数据解析异常");
                } finally {
                    long endTime = System.currentTimeMillis();
                    long timeUsed = endTime - startTime;// 访问网络使用的时间
                    try {
                        if (timeUsed < 3000) {
                            thread.sleep(3000 - timeUsed);// 强制等待一段时间, 凑够两秒钟
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mhandler.sendMessage(message);
                }
            }
        }.start();
    }

    /**
     * 获取版本名称
     */
    private String getMversionName(){
        PackageManager packageManager=getPackageManager();
        try {
            PackageInfo packageInfo= packageManager.getPackageInfo(getPackageName(),0);
            Log.v("baoming",getPackageName());
            String versionName=packageInfo.versionName;//版本名称
            return  versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * 获取版本号，用于检测是否更新版本
     */
    private int getVersionCode(){
        PackageManager packageManager=getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(),0);
            int versioncode=packageInfo.versionCode;
            return versioncode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 显示版本更新对话框
     */
    private void showUpdateDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);// 这里必须传一个activity对象
        builder.setTitle("发现新版本:" + mversionName);
        builder.setMessage(mdes);
        // builder.setCancelable(false);//不可取消,点返回键弹窗不消失, 尽量不要用,用户体验不好
        builder.setPositiveButton("立即更新",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        downloadApk();
                        Log.v("tag","xia zai apk qu le");
                        enterHome();
                    }
                });
        builder.setNegativeButton("以后再说",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        enterHome();
                    }
                });

        // 用户取消弹窗的监听,比如点返回键
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                enterHome();
            }
        });

        builder.show();

    }
    /**
     * 跳转到主页面
     */
    private void enterHome(){
        startActivity(new Intent(this,HomeActivity.class));
        finish();
    }

    /**
     * 启动通知
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private Notification startNotify(){

        notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new Notification.Builder(this)
                .setAutoCancel(true)
                .setTicker("AY最新消息！！！")
                .setSmallIcon(R.mipmap.hezi)
                .setContentTitle("游戏开服了！！！")
                .setContentText("傲视遮天，最新开服列表")
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis()).build();
        return notification;

    }
    /**
     * 下载安装包
     */
    protected void downloadApk(){
        //判断sd卡是否存在
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
        new Thread(){
            @Override
            public void run() {
                try {
                    Log.i("更新数量统计接口：",Constans.URL_APPISUPDATECLICK+box_id);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(Constans.URL_APPISUPDATECLICK+box_id).openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setRequestProperty("Content-Type", "text/html");
                    httpURLConnection.connect();
                    BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    Log.i("更新返回：",bufferedReader.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
            String path= Environment.getExternalStorageDirectory().getAbsolutePath()+"/AYBox.apk";
            HttpUtils utils = new HttpUtils();
            Log.v("tag","utils创建了额");
            utils.download(murl, path, new RequestCallBack<File>() {
                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    super.onLoading(total, current, isUploading);
                    //下载进度
                }

                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    //下载成功
                    // 跳转系统安装页面
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setDataAndType(Uri.fromFile(responseInfo.result),"application/vnd.android.package-archive");
                    startActivityForResult(intent, 0);
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Log.v("tag",s);
                }
            });
    }

    //用户取消安装按钮监听

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        enterHome();
    }




    /**
     * 广告资源获取，获取广告图片和超链接
     */
    private void getImage (){
        new Thread(){
            @Override
            public void run() {
                // 1、确定网址
                // 2、获取Uri
                try {
                    URL uri = new URL("http://cdn.symi.cn/Public/config/"+path_iamge);
                    // 3、获取连接对象、此时还没有建立连接
                    HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
                    // 4、初始化连接对象
                    // 设置请求的方法，注意大写
                    connection.setRequestMethod("GET");
                    // 读取超时
                    connection.setReadTimeout(5000);
                    // 设置连接超时
                    connection.setConnectTimeout(5000);
                    // 5、建立连接
                    connection.connect();
                    // 6、获取成功判断,获取响应码
                    if (connection.getResponseCode() == 200) {
                        // 7、拿到服务器返回的流，客户端请求的数据，就保存在流当中
                        InputStream is = connection.getInputStream();
                        // 8、从流中读取数据，构造一个图片对象GoogleAPI
                        bm = BitmapFactory.decodeStream(is);
                        AyBoxApplication.bit_adver = bm;
                        // 9、把图片设置到UI主线程
                        // ImageView中,获取网络资源是耗时操作需放在子线程中进行,通过创建消息发送消息给主线程刷新控件；
                        Message msg = new Message();
                        msg.what=GET_IMAGE;
                        msg.obj=bm;
                        mhandler.sendMessage(msg);
                        Log.i("获取广告图片", "网络请求成功");

                    } else {
                        Log.v("获取广告图片", "网络请求失败");
                        bm = null;
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void getMessage(){
        new Thread(){
            @Override
            public void run() {
                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL(Constans.URL_ADVERTISINT).openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();
                    if (connection.getResponseCode()==200) {
                        InputStream in = connection.getInputStream();
                        String result = stream2string(in);
                        JSONObject json = new JSONObject(result);
                        JSONObject json2=json.getJSONObject("data");
                        path_game=json2.getString("gg_url");
                        path_iamge=json2.getString("gg_img");
                        adver_id = json2.getString("id");
                        AyBoxApplication.path_adver = path_game;
                        AyBoxApplication.id_adver = adver_id;
//                        BitmapUtils bitmapUtils = new BitmapUtils(SplashActivity.this);
//                        // 加载网络图片
//                        bitmapUtils.display(iv_advertisement, "http://cdn.symi.cn/Public/config/"+path_iamge);
                        Log.i("message",path_game+"||||||"+path_iamge);
                        Message msg = new Message();
                        msg.what=GET_MESSAGE;
                        mhandler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    Message msg = new Message();
                    msg.what=4;
                    mhandler.sendMessage(msg);
                    e.printStackTrace();
                }
            }
        }.start();
    }

}
