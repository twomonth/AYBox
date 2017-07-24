package com.aygames.twomonth.aybox.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.aygames.twomonth.aybox.R;
import com.aygames.twomonth.aybox.domain.AppInfo;
import com.aygames.twomonth.aybox.domain.AppInfoService;
import com.aygames.twomonth.aybox.download.common.DownloadingFragment;
import com.aygames.twomonth.aybox.util.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by twomonth on 2017/6/13.
 */

public class Activity_Download extends Activity {
    private FrameLayout fl;
    private GridView gridView;
    private List<AppInfo> appInfos = new ArrayList<AppInfo>();
    private Handler mhanlder = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            gridView.setAdapter(new MyAdapter(appInfos));
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_download_manager);
        //调用工具类设置Activity颜色
        StatusBarUtils.setWindowStatusBarColor(this,R.color.colorPrimary);
        initView();
        initData();
//        gridView.setAdapter(new MyAdapter(appInfos));
    }

    private void initData() {
//        gridView.setAdapter(new SimpleAdapter(getApplicationContext(),R.layout.item_gridview,new AppInfoService(getApplicationContext()).getAppInfos()));
        appInfos = new AppInfoService(getApplicationContext()).getAppInfos();
        Log.i("appinfos",appInfos.toString());
        Message message = new Message();
        mhanlder.sendMessage(message);


    }

    private void initView() {
        gridView = (GridView) findViewById(R.id.gv_downloaded);
        fl = (FrameLayout) findViewById(R.id.fl_downloading);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Fragment firstFragment = new DownloadingFragment();
        transaction.add(R.id.fl_downloading,firstFragment,null);
        transaction.commit();
    }

    class MyAdapter extends BaseAdapter{
        private List<AppInfo> appInfos = new ArrayList<AppInfo>();
        MyAdapter(List<AppInfo> appInfos){
            this.appInfos = appInfos;
        }
        @Override
        public int getCount() {
            return appInfos.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if (view == null){
                holder = new ViewHolder();
                view = View.inflate(getApplicationContext(),R.layout.item_gridview,null);
                holder.iv_icon_game = (ImageView) view.findViewById(R.id.iv_icon_game);
                holder.tv_name_game = (TextView) view.findViewById(R.id.tv_name_game);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.iv_icon_game.setImageDrawable(appInfos.get(i).getApp_icon());
            holder.tv_name_game.setText(appInfos.get(i).getApp_name());
            return view;
        }
        class ViewHolder
            {
            public ImageView iv_icon_game;
            public TextView tv_name_game;
            }
    }
}
