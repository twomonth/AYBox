package com.aygames.twomonth.aybox.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.aygames.twomonth.aybox.R;
import com.aygames.twomonth.aybox.floatview.FloatViewImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by MyPC on 2017/6/13.
 */

public class Activity_left extends Activity {
    LinearLayout ll_close;
    ImageView iv_head;
    TextView tv_jmessage;
    GridView gv_menu;
    private String[] name = { "用户管理", "货币充值", "下载管理", "通信" };
    private int [] images = {R.mipmap.bgggg,R.mipmap.bgggg,R.mipmap.bgggg,R.mipmap.bgggg};
    private List<Map<String,Object>> list_map = new ArrayList<Map<String,Object>>(); //定义一个适配器对象
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_left);
        initView();
        for(int i=0;i<4;i++){
            Map<String,Object> items = new HashMap<String, Object>(); //创建一个键值对的Map集合，用来存放名字和头像
            items.put("pic", images[i]);  //放入头像， 根据下标获取数组
            items.put("name", name[i]);      //放入名字， 根据下标获取数组
            list_map.add(items);   //把这个存放好数据的Map集合放入到list中，这就完成类数据源的准备工作
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,list_map
                ,R.layout.adapter_left
                ,new String[]{"pic","name"}
                ,new int[]{R.id.iv_menu_left,R.id.tv_menu_left});
        gv_menu.setAdapter(simpleAdapter);

        gv_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==2){
                    Intent intent = new Intent(getApplicationContext(),Activity_Download.class);
                    startActivity(intent);
                }
            }
        });

        ll_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FloatViewImpl.getInstance(getApplicationContext());
                FloatViewImpl.ShowFloat();
                finish();
            }
        });
        tv_jmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void initView() {
        tv_jmessage = (TextView) findViewById(R.id.tv_jmessage);
        ll_close = (LinearLayout) findViewById(R.id.ll_close);
        iv_head = (ImageView) findViewById(R.id.iv_headportrait);
        gv_menu = (GridView) findViewById(R.id.gv_menu);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            FloatViewImpl.getInstance(getApplicationContext());
            FloatViewImpl.ShowFloat();
            finish();
        }
        return false;
    }
}
