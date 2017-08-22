//package com.aygames.twomonth.aybox.manager;
//
//import android.app.Activity;
//import android.content.ComponentName;
//import android.content.Intent;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.content.pm.ResolveInfo;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ListView;
//
//import com.aygames.twomonth.aybox.R;
//import com.aygames.twomonth.aybox.adapter.AppManagerAdapter;
//import com.aygames.twomonth.aybox.domain.AppInfo;
//import com.aygames.twomonth.aybox.domain.AppInfoService;
//
//import java.util.List;
//
///**
// * Created by wf05 on 2017/7/6.
// */
//
//public class AppManager extends Activity {
//    private ListView lv_app;
//    private List<AppInfo> list;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_appmanager);
//        initData();
//        initView();
//        lv_app.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String packagename = list.get(position).getPackagename();
//                doStartApplicationWithPackageName(packagename);
//            }
//        });
//    }
//
//    private void initData() {
//        AppInfoService appInfoService = new AppInfoService(this);
//        list = appInfoService.getAppInfos();
//
//    }
//
//    private void initView() {
//        lv_app = (ListView) findViewById(R.id.lv_app);
//        lv_app.setAdapter(new AppManagerAdapter(this,list));
//
//    }
//
//
//    private void doStartApplicationWithPackageName(String packagename) {
//
//        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
//        PackageInfo packageinfo = null;
//        try {
//            packageinfo = getPackageManager().getPackageInfo(packagename, 0);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        if (packageinfo == null) {
//            return;
//        }
//
//        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
//        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
//        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//        resolveIntent.setPackage(packageinfo.packageName);
//
//        // 通过getPackageManager()的queryIntentActivities方法遍历
//        List<ResolveInfo> resolveinfoList = getPackageManager()
//                .queryIntentActivities(resolveIntent, 0);
//
//        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
//        if (resolveinfo != null) {
//            // packagename = 参数packname
//            String packageName = resolveinfo.activityInfo.packageName;
//            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
//            String className = resolveinfo.activityInfo.name;
//            // LAUNCHER Intent
//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.addCategory(Intent.CATEGORY_LAUNCHER);
//
//            // 设置ComponentName参数1:packagename参数2:MainActivity路径
//            ComponentName cn = new ComponentName(packageName, className);
//
//            intent.setComponent(cn);
//            startActivity(intent);
//        }
//    }
//}
