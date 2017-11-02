package com.aygames.twomonth.aybox.activity;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aygames.twomonth.aybox.R;
import com.aygames.twomonth.aybox.util.Constans;
import com.aygames.twomonth.aybox.util.QRUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
/**
 * Created by MyPC on 2017/3/29.
 */

public class TuiguangActivity extends Activity implements View.OnClickListener{

    private String number,amount,code,url,userid;
    private ImageView iv_tg_erweima;
    private TextView tv_back_tg;//退出按钮
    private ArrayList<String> list = new ArrayList<>();
    private TextView tv_number,tv_amount,tv_url;
    private Button bt_tg_bctp,bt_tg_copyurl,bt_tg_lingqu;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==20){
                bt_tg_lingqu.setVisibility(View.VISIBLE);
            }else if (msg.what==21){
                bt_tg_lingqu.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"领取成功",Toast.LENGTH_SHORT).show();
            }

            tv_url.setText(url);
            tv_amount.setText(amount);
            tv_number.setText(number);
            //生成二维码
            try {
                Bitmap bitmap = QRUtils.encodeToQRWidth(url,300);//要生成二维码的内容，我这就是一个网址
                iv_tg_erweima.setImageBitmap(bitmap);
            }
            catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "生成二维码失败", Toast.LENGTH_SHORT);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_tuiguang_landscape);
        list=getIntent().getStringArrayListExtra("msg");
        getDataFrom();
        init();


        bt_tg_bctp.setOnClickListener(this);
        tv_back_tg.setOnClickListener(this);
        bt_tg_copyurl.setOnClickListener(this);
        bt_tg_lingqu.setOnClickListener(this);

    }

    private void init() {
        tv_back_tg = (TextView) findViewById(R.id.iv_back_tg_landscape);
        tv_number= (TextView) findViewById(R.id.tv_number);
        tv_amount= (TextView) findViewById(R.id.tv_amount);
        tv_url= (TextView) findViewById(R.id.tv_url);
        iv_tg_erweima= (ImageView) findViewById(R.id.iv_tg_erweima);
        bt_tg_bctp= (Button) findViewById(R.id.bt_tg_bctp);
        bt_tg_copyurl= (Button) findViewById(R.id.bt_tg_copyurl);
        bt_tg_lingqu= (Button) findViewById(R.id.bt_tg_lingqu);

    }

    @Override
    public void onClick(View view) {
        if (view.getId()==tv_back_tg.getId()){
            //关闭按钮
            finish();
        }else if (view.getId()==bt_tg_bctp.getId()){
            //从imageview 中获取图片
            iv_tg_erweima.setDrawingCacheEnabled(true);
            Bitmap obmp = Bitmap.createBitmap(iv_tg_erweima.getDrawingCache());
            MediaStore.Images.Media.insertImage(getContentResolver(), obmp, "symerweima", "description");
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File("/sdcard/Boohee/image.jpg"))));
            Toast.makeText(getApplicationContext(),"二维码保存成功",Toast.LENGTH_SHORT).show();
        }else if (view.getId()==bt_tg_copyurl.getId()){
            ClipboardManager cmb = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setText(tv_url.getText().toString().trim()); //将内容放入粘贴管理器
            Toast.makeText(getApplicationContext(),"成功复制链接",Toast.LENGTH_SHORT).show();
        }else if (view.getId()==bt_tg_lingqu.getId()){
            //向服务器发送订单
            order2server();
//            number=0+"";
//            amount=0+"";
            Message message = new Message();
            handler.sendMessage(message);
        }
    }

    //向服务器发送申请
    private void getDataFrom(){
        new Thread(){
            @Override
            public void run() {
                JSONObject jsonObject = new JSONObject();
                try {
//                    jsonObject.put("type", "fx");
                    jsonObject.put("gameid", list.get(0));
                    jsonObject.put("serverid", list.get(2));
                    jsonObject.put("username", list.get(1));
                    jsonObject.put("role", list.get(3));
                    jsonObject.put("orderid", getOutTradeNo());
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(Constans.TUIGUANG).openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setRequestProperty("Content-Type", "text/html");
                    httpURLConnection.connect();
                    OutputStreamWriter outputStreamWriter= new OutputStreamWriter(httpURLConnection.getOutputStream());
                    outputStreamWriter.write(jsonObject.toString());
                    Log.i("推广发送数据",jsonObject+"");
                    outputStreamWriter.flush();
                    BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
//                    String s = bufferedReader.readLine();
//                    Log.i("推广服务器返回", s);
                    jsonObject=new JSONObject(bufferedReader.readLine());
                    JSONObject jsonObjectdata= jsonObject.getJSONObject("data");
                    Log.i("返回json数据", jsonObjectdata.toString());
                    number = jsonObjectdata.getString("number");
                    amount = jsonObjectdata.getString("amount");
                    code = jsonObjectdata.getString("code");
                    url =jsonObjectdata.getString("url");
                    userid = jsonObjectdata.getString("userid");
                    float am =Float.valueOf(amount);
                    if (am>=10){
                        Message message = new Message();
                        message.what=20;
                        handler.sendMessage(message);
                    }
                    bufferedReader.close();
                    Message message = new Message();
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    Log.i("推广json","错误");
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    Log.i("推广网络连接","错误");
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.i("推广网络流","错误");
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 发送订单到服务端
     */
    private void order2server(){
        new Thread(){
            @Override
            public void run() {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("gameid",list.get(0));
                    jsonObject.put("username",list.get(1));
                    jsonObject.put("serverid",list.get(2));
                    jsonObject.put("role",list.get(3));
                    jsonObject.put("code",code);
                    jsonObject.put("orderid",getOutTradeNo());
                    jsonObject.put("number",Integer.valueOf(number));
                    jsonObject.put("amount",amount);
                    jsonObject.put("userid",userid);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(Constans.ORDER_TUIGUANG).openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setRequestProperty("Content-Type", "text/html");
                    httpURLConnection.connect();
                    OutputStreamWriter outputStreamWriter= new OutputStreamWriter(httpURLConnection.getOutputStream());
                    outputStreamWriter.write(jsonObject.toString());
                    Log.i("推广订单数据",jsonObject+"");
                    outputStreamWriter.flush();
                    BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    JSONObject jsonObject1 = new JSONObject(bufferedReader.readLine().toString());
                    String success = jsonObject1.getString("data");
                    Log.i("推广订单返回数据",success);
                    if (success.equals("success")){
                        Message message = new Message();
                        message.what=21;
                        handler.sendMessage(message);
                    }
                } catch (JSONException e) {
                    Log.i("推广json","错误");
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    Log.i("推广订单网络连接","错误");
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.i("网络流读取","错误");
                    e.printStackTrace();
                }
            }
        }.start();
    }
    /** 获取推广订单号 */
    private String getOutTradeNo(){
        Random random = new Random();
        int s = random.nextInt(9999)%(9999-1000+1)+1000;
        return  "TG"+System.currentTimeMillis()+s;
    }
}
