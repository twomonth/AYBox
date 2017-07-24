package com.aygames.twomonth.aybox.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import com.aygames.twomonth.aybox.R;

/**
 * Created by MyPC on 2017/4/3.
 */

public class FXActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //分享页面隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }
}
