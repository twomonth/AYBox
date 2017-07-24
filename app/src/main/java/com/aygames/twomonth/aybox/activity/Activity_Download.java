package com.aygames.twomonth.aybox.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Window;
import android.widget.FrameLayout;
import com.aygames.twomonth.aybox.R;
import com.aygames.twomonth.aybox.download.common.DownloadingFragment;
import com.aygames.twomonth.aybox.util.StatusBarUtils;

/**
 * Created by MyPC on 2017/6/13.
 */

public class Activity_Download extends Activity {
    private FrameLayout fl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_download_manager);
        //调用工具类设置Activity颜色
        StatusBarUtils.setWindowStatusBarColor(this,R.color.colorPrimary);
        initView();
    }

    private void initView() {
        fl = (FrameLayout) findViewById(R.id.fl_downloading);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Fragment firstFragment = new DownloadingFragment();
        transaction.add(R.id.fl_downloading,firstFragment,null);
        transaction.commit();
    }
}
