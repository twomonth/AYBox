package com.aygames.twomonth.aybox.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.aygames.twomonth.aybox.R;
import com.aygames.twomonth.aybox.util.Constans;
import com.aygames.twomonth.aybox.util.GetDataImpl;
import com.aygames.twomonth.aybox.util.GetDateImpl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by MyPC on 2017/3/23.
 * list: 0=gid,1=username,2=heroid,3=heroname,4=herorank
 */

public class FenxiangActivity extends Activity implements View.OnClickListener{

    private final static int SHARE_OK=1;
    private String gameid,title,imageurl,gameurl,text;
    private Button bt_weicheat,bt_pengyouquan,bt_qq,bt_qqkongjian,bt_sinaweibo,bt_lingqu,bt_back_fxsm,bt_lingqu_black;
    private TextView tv_money_today,tv_money_lj,tv_weixin,tv_pengyouquan,tv_QQ,tv_qqkongjian,tv_sinaweibo,tv_fenxiangshuoming;
    private ImageView iv_back_fx,iv_back_fx2;
    private RelativeLayout linearLayout;
    private LinearLayout line_fenxiangshuoming;
    private int day;
    private ArrayList<String> list = new ArrayList<>();
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            SharedPreferences sharedPreferences = getSharedPreferences(list.get(0)+"info",MODE_PRIVATE);
            int money_tody = sharedPreferences.getInt("sum",0);
            int money_lj = sharedPreferences.getInt("sum_lj",0);
            tv_money_today.setText(money_tody+"元");
            tv_money_lj.setText(money_lj+"元");
            if (money_lj>=50){
                bt_lingqu.setVisibility(View.VISIBLE);
                bt_lingqu_black.setVisibility(View.GONE);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //分享页面隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_fenxiang);
        list=getIntent().getStringArrayListExtra("msg");
        gameid=list.get(0);
        Log.i("从sdk接受到数据",list.toString());
        getMessageForGame();
        //横竖屏问题
        if (list.get(5)=="0"){
            if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }else if (list.get(5)=="1"){
            if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
            }
        }

        SharedPreferences sharedPreferences = getSharedPreferences(list.get(0)+"info",MODE_PRIVATE);
        //获取系统日期
        Calendar calendar =Calendar.getInstance();
        day=calendar.get(Calendar.DAY_OF_MONTH);
        //如果日期为新的一天，除累计金额外所有存储参数制 0 。
        if (day!=sharedPreferences.getInt("day",0)){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("day",day);
            editor.putInt("weixin",0);
            editor.putInt("pengyouquan",0);
            editor.putInt("qq",0);
            editor.putInt("qqkongjian",0);
            editor.putInt("sinaweibo",0);
            editor.putInt("sum",0);
            editor.commit();
            //初始化组件
            init();
            Message message = new Message();
            handler.sendMessage(message);
        }else{
            //初始化组件
            init();
            //微信
            if (sharedPreferences.getInt("weixin",0)==1){
                bt_weicheat.setVisibility(View.GONE);
                tv_weixin.setVisibility(View.VISIBLE);
            }else{
                bt_weicheat.setVisibility(View.VISIBLE);
                tv_weixin.setVisibility(View.GONE);
            }
            //朋友圈
            if (sharedPreferences.getInt("pengyouquan",0)==1){
                bt_pengyouquan.setVisibility(View.GONE);
                tv_pengyouquan.setVisibility(View.VISIBLE);
            }else {
                bt_pengyouquan.setVisibility(View.VISIBLE);
                tv_pengyouquan.setVisibility(View.GONE);
            }
            //QQ
            if (sharedPreferences.getInt("qq",0)==1){
                bt_qq.setVisibility(View.GONE);
                tv_QQ.setVisibility(View.VISIBLE);
            }else{
                bt_qq.setVisibility(View.VISIBLE);
                tv_QQ.setVisibility(View.GONE);
            }
            //QQ空间
            if (sharedPreferences.getInt("qqkongjian",0)==1){
                bt_qqkongjian.setVisibility(View.GONE);
                tv_qqkongjian.setVisibility(View.VISIBLE);
            }else {
                bt_qqkongjian.setVisibility(View.VISIBLE);
                tv_qqkongjian.setVisibility(View.GONE);
            }
            //新浪微博
            if (sharedPreferences.getInt("sinaweibo",0)==1){
                bt_sinaweibo.setVisibility(View.GONE);
                tv_sinaweibo.setVisibility(View.VISIBLE);
            }else {
                bt_sinaweibo.setVisibility(View.VISIBLE);
                tv_sinaweibo.setVisibility(View.GONE);
            }
            Message message = new Message();
            handler.sendMessage(message);

        }



        /**   组件设置监听 */
        bt_weicheat.setOnClickListener(this);
        bt_pengyouquan.setOnClickListener(this);
        bt_qqkongjian.setOnClickListener(this);
        bt_sinaweibo.setOnClickListener(this);
        bt_qq.setOnClickListener(this);
        bt_lingqu.setOnClickListener(this);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                finish();
            }
        });
        //返回按钮监听
        iv_back_fx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        iv_back_fx2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                line_fenxiangshuoming.setVisibility(View.GONE);
                iv_back_fx2.setVisibility(View.GONE);
                iv_back_fx.setVisibility(View.VISIBLE);
            }
        });
        //分享说明按钮监听
        tv_fenxiangshuoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                line_fenxiangshuoming.setVisibility(View.VISIBLE);
                iv_back_fx2.setVisibility(View.VISIBLE);
                iv_back_fx.setVisibility(View.GONE);
            }
        });

        //分享说明内部返回按钮
        bt_back_fxsm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                line_fenxiangshuoming.setVisibility(View.GONE);
            }
        });
        //分享功能
        ShareSDK.initSDK(this);

    }

    /** 初始化组件 */
    private void init(){
        linearLayout= (RelativeLayout) findViewById(R.id.line_fenxiang);
        bt_lingqu= (Button) findViewById(R.id.bt_lingqu);
        bt_weicheat= (Button) findViewById(R.id.bt_weichate);
        bt_pengyouquan= (Button) findViewById(R.id.bt_pengyouquan);
        bt_qq= (Button) findViewById(R.id.bt_qq);
        bt_qqkongjian= (Button) findViewById(R.id.bt_qqkongjian);
        bt_sinaweibo= (Button) findViewById(R.id.bt_sinaweibo);
        tv_money_today= (TextView) findViewById(R.id.tv_money_today);
        tv_money_lj= (TextView) findViewById(R.id.tv_money_lj);
        tv_weixin= (TextView) findViewById(R.id.tv_weixin);
        tv_pengyouquan= (TextView) findViewById(R.id.tv_pengyouquan);
        tv_QQ= (TextView) findViewById(R.id.tv_qq);
        tv_qqkongjian= (TextView) findViewById(R.id.tv_qqkongjian);
        tv_sinaweibo= (TextView) findViewById(R.id.tv_sinaweibo);
        iv_back_fx= (ImageView) findViewById(R.id.iv_back_fx);
        iv_back_fx2= (ImageView) findViewById(R.id.iv_back_fx2);
        tv_fenxiangshuoming= (TextView) findViewById(R.id.tv_fenxiangshuoming);
        bt_back_fxsm= (Button) findViewById(R.id.bt_back_fxsm);
        line_fenxiangshuoming= (LinearLayout) findViewById(R.id.line_fenxiangshuoming);
        bt_lingqu_black= (Button) findViewById(R.id.bt_lingqu_black);
    }

    /**监听按钮点击事件*/
    @Override
    public void onClick(View view) {
        //微信好友分享
        if (view.getId()==bt_weicheat.getId()){
            Platform.ShareParams sp = new Platform.ShareParams();
            sp.setTitle(title);
            sp.setText(text);
            sp.setImageUrl(imageurl);
            sp.setUrl(gameurl);
            sp.setShareType(Platform.SHARE_WEBPAGE);
            Platform weixin = ShareSDK.getPlatform(this, Wechat.NAME);
            weixin.setPlatformActionListener(new PlatformActionListener() {
                @Override
                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                    SharedPreferences sharedPreferences = getSharedPreferences(list.get(0)+"info",MODE_PRIVATE);
                    int sum = sharedPreferences.getInt("sum",0);
                    int sum_lj =sharedPreferences.getInt("sum_lj",0);
                    sum=sum+1;
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("weixin",1);
                    editor.putInt("sum",sum);
                    editor.putInt("sum_lj",sum_lj+1);
                    editor.commit();
                    bt_weicheat.setVisibility(View.GONE);
                    tv_weixin.setVisibility(View.VISIBLE);
                    Message message = new Message();
                    handler.sendMessage(message);
                    Toast.makeText(getApplicationContext(),"分享成功财富值+1",Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onError(Platform platform, int i, Throwable throwable) {
                    Toast.makeText(getApplicationContext(),"分享失败",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancel(Platform platform, int i) {
                    Toast.makeText(getApplicationContext(),"分享取消",Toast.LENGTH_SHORT).show();
                }
            });
            weixin.share(sp);

        }else if(view.getId()==bt_pengyouquan.getId()){
            //微信朋友圈分享
            Platform.ShareParams sp = new Platform.ShareParams();
            sp.setTitle(title+"\n"+text);
            sp.setTitleUrl(gameurl);
            sp.setText(text);
            sp.setImageUrl(imageurl);
            sp.setUrl(gameurl);
            sp.setShareType(Platform.SHARE_WEBPAGE);
            Platform pengyouquan = ShareSDK.getPlatform(this, WechatMoments.NAME);
            pengyouquan.setPlatformActionListener(new PlatformActionListener() {
                @Override
                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

                    SharedPreferences sharedPreferences = getSharedPreferences(list.get(0)+"info",MODE_PRIVATE);
                    int sum = sharedPreferences.getInt("sum",0);
                    int sum_lj =sharedPreferences.getInt("sum_lj",0);
                    sum=sum+2;
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("pengyouquan",1);
                    editor.putInt("sum",sum);
                    editor.putInt("sum_lj",sum_lj+2);
                    editor.commit();
                    bt_pengyouquan.setVisibility(View.GONE);
                    tv_pengyouquan.setVisibility(View.VISIBLE);
                    Message message = new Message();
                    handler.sendMessage(message);
                    Toast.makeText(getApplicationContext(),"分享成功财富值+2",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(Platform platform, int i, Throwable throwable) {
                    Toast.makeText(getApplicationContext(),"分享失败",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancel(Platform platform, int i) {
                    Toast.makeText(getApplicationContext(),"分享取消",Toast.LENGTH_SHORT).show();
                }
            });
            pengyouquan.share(sp);

        }else if (view.getId()==bt_sinaweibo.getId()){
            //新浪微博分享
            Platform.ShareParams sp = new Platform.ShareParams();
            sp.setTitle(title);
            sp.setText(text);
            sp.setImageUrl(imageurl);
            sp.setUrl(gameurl);
            sp.setShareType(Platform.SHARE_WEBPAGE);
            Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
            weibo.setPlatformActionListener(new PlatformActionListener() {
                @Override
                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                    SharedPreferences sharedPreferences = getSharedPreferences(list.get(0)+"info",MODE_PRIVATE);
                    int sum = sharedPreferences.getInt("sum",0);
                    int sum_lj =sharedPreferences.getInt("sum_lj",0);
                    sum=sum+2;
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("sinaweibo",1);
                    editor.putInt("sum",sum);
                    editor.putInt("sum_lj",sum_lj+2);
                    editor.commit();
                    bt_sinaweibo.setVisibility(View.GONE);
                    tv_sinaweibo.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),"分享成功财富值+2",Toast.LENGTH_SHORT).show();
                    Message message = new Message();
                    handler.sendMessage(message);
                }
                @Override
                public void onError(Platform platform, int i, Throwable throwable) {
                    Toast.makeText(getApplicationContext(),"分享失败",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancel(Platform platform, int i) {
                    Toast.makeText(getApplicationContext(),"分享取消",Toast.LENGTH_SHORT).show();
                }
            });

            weibo.share(sp);
        }else if (view.getId()==bt_qq.getId()){
            //QQ好友分享
            Platform.ShareParams sp=new Platform.ShareParams();
            sp.setTitle(title);
            sp.setText(text);
            sp.setImageUrl(imageurl);
            sp.setTitleUrl(gameurl);
            sp.setShareType(Platform.SHARE_WEBPAGE);
            Platform qq = ShareSDK.getPlatform(QQ.NAME);
            qq.setPlatformActionListener(new PlatformActionListener() {
                @Override
                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                    SharedPreferences sharedPreferences = getSharedPreferences(list.get(0)+"info",MODE_PRIVATE);
                    int sum = sharedPreferences.getInt("sum",0);
                    int sum_lj =sharedPreferences.getInt("sum_lj",0);
                    sum=sum+1;
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("qq",1);
                    editor.putInt("sum",sum);
                    editor.putInt("sum_lj",sum_lj+1);
                    editor.commit();
                    bt_qq.setVisibility(View.GONE);
                    tv_QQ.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),"分享成功财富值+1",Toast.LENGTH_SHORT).show();
                    Message message = new Message();
                    handler.sendMessage(message);
                }
                @Override
                public void onError(Platform platform, int i, Throwable throwable) {
                    Toast.makeText(getApplicationContext(),"分享失败",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancel(Platform platform, int i) {
                    Toast.makeText(getApplicationContext(),"分享取消",Toast.LENGTH_SHORT).show();
                }
            });
            qq.share(sp);
        }else if (view.getId()==bt_qqkongjian.getId()){
            //qq空间分享
            Platform.ShareParams sp = new Platform.ShareParams();
            sp.setTitle(title);
            sp.setTitleUrl(gameurl); // 标题的超链接
            sp.setText(text);
            sp.setImageUrl(imageurl);
            sp.setSite(title);
            sp.setSiteUrl(gameurl);
            sp.setShareType(Platform.SHARE_WEBPAGE);
            Platform qqkongjian = ShareSDK.getPlatform (QZone.NAME);
            qqkongjian.setPlatformActionListener (new PlatformActionListener() {
                @Override
                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                    SharedPreferences sharedPreferences = getSharedPreferences(list.get(0)+"info",MODE_PRIVATE);
                    int sum = sharedPreferences.getInt("sum",0);
                    int sum_lj =sharedPreferences.getInt("sum_lj",0);
                    sum=sum+2;
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("qqkongjian",1);
                    editor.putInt("sum",sum);
                    editor.putInt("sum_lj",sum_lj+2);
                    editor.commit();
                    bt_qqkongjian.setVisibility(View.GONE);
                    tv_qqkongjian.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),"分享成功财富值+2",Toast.LENGTH_SHORT).show();
                    Message message = new Message();
                    handler.sendMessage(message);
                }
                @Override
                public void onError(Platform platform, int i, Throwable throwable) {
                    Toast.makeText(getApplicationContext(),"分享失败",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancel(Platform platform, int i) {
                    Toast.makeText(getApplicationContext(),"分享取消",Toast.LENGTH_SHORT).show();
                }
            });
            qqkongjian.share(sp);
        }else if (view.getId()==bt_lingqu.getId()){
            //领取按钮
            new Thread(){
                @Override
                public void run() {
                    SharedPreferences sharedPreferences = getSharedPreferences(list.get(0)+"info",MODE_PRIVATE);
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("type", "fx");
                        jsonObject.put("amount", sharedPreferences.getInt("sum_lj",0));
                        jsonObject.put("gameid", FenxiangActivity.this.list.get(0));
                        jsonObject.put("serverid", FenxiangActivity.this.list.get(2));
                        jsonObject.put("username", FenxiangActivity.this.list.get(1));
                        jsonObject.put("role", FenxiangActivity.this.list.get(3));
                        jsonObject.put("sharenum", 3);
                        jsonObject.put("orderid", FenxiangActivity.this.getOutTradeNo());
                        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(Constans.ORDER_SHARE).openConnection();
                        httpURLConnection.setRequestMethod("POST");
                        httpURLConnection.setDoInput(true);
                        httpURLConnection.setDoOutput(true);
                        httpURLConnection.setRequestProperty("Content-Type", "text/html");
                        httpURLConnection.connect();
                        OutputStreamWriter outputStreamWriter= new OutputStreamWriter(httpURLConnection.getOutputStream());
                        outputStreamWriter.write(jsonObject.toString());
                        Log.i("分享订单数据",jsonObject+"");
                        outputStreamWriter.flush();
                        BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                        JSONObject jsonObject1 = new JSONObject(bufferedReader.readLine().toString());
                        String success = jsonObject1.getString("data");
                        Log.i("分享订返回数据",success+"");
                        if (success.equals("success")){
//                            SharedPreferences sharedPreferences = getSharedPreferences(list.get(0)+"info",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("sum",0);
                            editor.putInt("sum_lj",0);
                            editor.commit();
                            Message message = new Message();
                            handler.sendMessage(message);
                        }else {
                            Toast.makeText(getApplicationContext(),"分享失败",Toast.LENGTH_SHORT).show();
                        }
                        bufferedReader.close();
                    } catch (JSONException e) {
                        Log.i("分享订单生成json","错误");
                        e.printStackTrace();
                    } catch (MalformedURLException e) {
                        Log.i("返利订单网络连接","错误");
                        e.printStackTrace();
                    } catch (IOException e) {
                        Log.i("返利订单网络流","错误");
                        e.printStackTrace();
                    }
                }
            }.start();

        }
    }
    private void getMessageForGame(){
        new Thread(){
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("chid", GetDateImpl.getChannel(getApplicationContext()));
                    jsonObject.put("gid",list.get(0));
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(Constans.URL_GETMESSAGE_FENXIANG).openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setRequestProperty("Content-Type", "text/html");
                    httpURLConnection.connect();
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream());
                    Log.i("fenxiang发送", jsonObject.toString());
                    outputStreamWriter.write(jsonObject.toString());
                    outputStreamWriter.flush();
//                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    JSONObject jsonObject1 = new JSONObject(bufferedReader.readLine().toString());
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("data");
                    Log.i("分享返回数据",jsonObject2+"");
                    title = jsonObject2.getString("app_name_cn");
                    text = jsonObject2.getString("publicity");
                    imageurl = jsonObject2.getString("ico_url");
                    gameurl = jsonObject2.getString("url");
                } catch (MalformedURLException e) {
                    Log.i("获取APP下载地址时异常", e.toString());
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.i("输入输出异常", e.toString());
                    e.printStackTrace();
                } catch (JSONException e) {
                    Log.i("json解析异常", e.toString());
                    e.printStackTrace();
                }
            }
        }.start();
    }
    /** 获取分享订单号 */
    private String getOutTradeNo(){
        Random random = new Random();
        int s = random.nextInt(9999)%(9999-1000+1)+1000;
        return  "FX"+System.currentTimeMillis()+s;
    }

}
