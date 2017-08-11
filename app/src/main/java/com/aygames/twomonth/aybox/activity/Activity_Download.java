package com.aygames.twomonth.aybox.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.aygames.twomonth.aybox.R;
import com.aygames.twomonth.aybox.domain.AppInfo;
import com.aygames.twomonth.aybox.domain.AppInfoService;
import com.aygames.twomonth.aybox.download.common.DownloadingFragment;
import com.aygames.twomonth.aybox.util.Constans;
import com.aygames.twomonth.aybox.util.StatusBarUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by twomonth on 2017/6/13.
 */

public class Activity_Download extends Activity {
    private FrameLayout fl;
    private GridView gridView;
    private ImageView iv_back_download;
    //    private List<AppInfo> appInfos = new ArrayList<AppInfo>();
    private File[] apkFilelist;
    PackageManager packageManager;
    private ArrayList<Map<String, Object>> arrayList = new ArrayList<>();

    //    private Handler mhanlder = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            gridView.setAdapter(new MyAdapter(appInfos));
//        }
//    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_download_manager);
        //调用工具类设置Activity颜色
        StatusBarUtils.setWindowStatusBarColor(this, R.color.colorOrange);
        initView();
        initData();
        gridView.setAdapter(new DownloadedAdapter(arrayList));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String appPackageName = arrayList.get(i).get("packageName").toString();
                String appPath = arrayList.get(i).get("apkFileName").toString();
                try{
                    Intent intent = packageManager.getLaunchIntentForPackage(appPackageName);
                    startActivity(intent);
                }catch(Exception e){
                    Intent install = new Intent(Intent.ACTION_VIEW);
                    install.addCategory(Intent.CATEGORY_DEFAULT);
                    install.setDataAndType(Uri.parse("file://"+appPath), "application/vnd.android.package-archive");
                    install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(install);
                }
            }
        });
        iv_back_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

//    private void initData() {
//        appInfos = new AppInfoService(getApplicationContext()).getAppInfos();
//        Log.i("appinfos",appInfos.toString());
//        Message message = new Message();
//        mhanlder.sendMessage(message);
//    }

    private void initView() {
        gridView = (GridView) findViewById(R.id.gv_downloaded);
        iv_back_download = (ImageView) findViewById(R.id.iv_back_download);
        fl = (FrameLayout) findViewById(R.id.fl_downloading);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Fragment firstFragment = new DownloadingFragment();
        transaction.add(R.id.fl_downloading, firstFragment, null);
        transaction.commit();
    }

    private void initData() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "AYgames");
        apkFilelist = file.listFiles();
        packageManager = this.getPackageManager();
        for (int i = 0; i < apkFilelist.length; i++) {
            PackageInfo packageInfo = null;
            String appName = null;
            String packageName = null;
            String version = null;
            Drawable icon1 = null;
            try {
                packageInfo = packageManager.getPackageArchiveInfo(apkFilelist[i].getPath(), PackageManager.GET_ACTIVITIES);
                ApplicationInfo appInfo = packageInfo.applicationInfo;
                /* 必须加这两句，不然下面icon获取是default icon而不是应用包的icon */
                appInfo.sourceDir = apkFilelist[i].getPath();
                appInfo.publicSourceDir = apkFilelist[i].getPath();
                appName = packageManager.getApplicationLabel(appInfo).toString();// 得到应用名
                packageName = appInfo.packageName; // 得到包名
                version = packageInfo.versionName; // 得到版本信息
                icon1 = packageManager.getApplicationIcon(appInfo);// 得到图标信息
                Map<String, Object> hashMap = new HashMap<String, Object>();
                hashMap.put("icon", icon1);
                hashMap.put("appName", appName);
                hashMap.put("packageName", packageName);
                hashMap.put("version", version);
                hashMap.put("apkFileName", apkFilelist[i]);
                arrayList.add(hashMap);
            }catch (Exception e){
                Map<String, Object> hashMap = new HashMap<String, Object>();
                hashMap.put("icon", getResources().getDrawable(R.mipmap.ic_launcher));
                hashMap.put("appName", "下载尚未完成");
                hashMap.put("packageName", "x");
                hashMap.put("version","x");
                hashMap.put("apkFileName", apkFilelist[i]);
                arrayList.add(hashMap);
            }
        }
        Constans.arrayList = arrayList;
    }

    class DownloadedAdapter extends BaseAdapter {
        private ArrayList<Map<String, Object>> arrayList;

        DownloadedAdapter(ArrayList<Map<String, Object>> arrayList) {
            this.arrayList = arrayList;
        }

        @Override
        public int getCount() {
            return apkFilelist.length;
        }

        @Override
        public Object getItem(int position) {
            return arrayList.get(position).get("packageName");
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(getApplicationContext(), R.layout.item_gridview, null);
                holder.iv_icon_game = (ImageView) convertView.findViewById(R.id.iv_icon_game);
                holder.tv_name_game = (TextView) convertView.findViewById(R.id.tv_name_game);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.iv_icon_game.setImageDrawable((Drawable) arrayList.get(position).get("icon"));
            holder.tv_name_game.setText((String) arrayList.get(position).get("appName"));
            return convertView;
        }

        class ViewHolder {
            public ImageView iv_icon_game;
            public TextView tv_name_game;
        }

    }

//    class MyAdapter extends BaseAdapter{
//        private List<AppInfo> appInfos = new ArrayList<AppInfo>();
//        MyAdapter(List<AppInfo> appInfos){
//            this.appInfos = appInfos;
//        }
//        @Override
//        public int getCount() {
//            return appInfos.size();
//        }
//
//        @Override
//        public Object getItem(int i) {
//            return i;
//        }
//
//        @Override
//        public long getItemId(int i) {
//            return i;
//        }
//
//        @Override
//        public View getView(int i, View view, ViewGroup viewGroup) {
//            ViewHolder holder;
//            if (view == null){
//                holder = new ViewHolder();
//                view = View.inflate(getApplicationContext(),R.layout.item_gridview,null);
//                holder.iv_icon_game = (ImageView) view.findViewById(R.id.iv_icon_game);
//                holder.tv_name_game = (TextView) view.findViewById(R.id.tv_name_game);
//                view.setTag(holder);
//            } else {
//                holder = (ViewHolder) view.getTag();
//            }
//            holder.iv_icon_game.setImageDrawable(appInfos.get(i).getApp_icon());
//            holder.tv_name_game.setText(appInfos.get(i).getApp_name());
//            return view;
//        }
//        class ViewHolder
//            {
//            public ImageView iv_icon_game;
//            public TextView tv_name_game;
//            }
//    }
}
